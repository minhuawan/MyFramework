/*      */ package com.badlogic.gdx.utils.compression.lzma;
/*      */ 
/*      */ import com.badlogic.gdx.utils.compression.ICodeProgress;
/*      */ import com.badlogic.gdx.utils.compression.lz.BinTree;
/*      */ import com.badlogic.gdx.utils.compression.rangecoder.BitTreeEncoder;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Encoder
/*      */ {
/*      */   public static final int EMatchFinderTypeBT2 = 0;
/*      */   public static final int EMatchFinderTypeBT4 = 1;
/*      */   static final int kIfinityPrice = 268435455;
/*   30 */   static byte[] g_FastPos = new byte[2048];
/*      */   
/*      */   static {
/*   33 */     int kFastSlots = 22;
/*   34 */     int c = 2;
/*   35 */     g_FastPos[0] = 0;
/*   36 */     g_FastPos[1] = 1;
/*   37 */     for (int slotFast = 2; slotFast < kFastSlots; slotFast++) {
/*   38 */       int k = 1 << (slotFast >> 1) - 1;
/*   39 */       for (int j = 0; j < k; j++, c++)
/*   40 */         g_FastPos[c] = (byte)slotFast; 
/*      */     } 
/*      */   }
/*      */   
/*      */   static int GetPosSlot(int pos) {
/*   45 */     if (pos < 2048) return g_FastPos[pos]; 
/*   46 */     if (pos < 2097152) return g_FastPos[pos >> 10] + 20; 
/*   47 */     return g_FastPos[pos >> 20] + 40;
/*      */   }
/*      */   
/*      */   static int GetPosSlot2(int pos) {
/*   51 */     if (pos < 131072) return g_FastPos[pos >> 6] + 12; 
/*   52 */     if (pos < 134217728) return g_FastPos[pos >> 16] + 32; 
/*   53 */     return g_FastPos[pos >> 26] + 52;
/*      */   }
/*      */   
/*   56 */   int _state = Base.StateInit();
/*      */   byte _previousByte;
/*   58 */   int[] _repDistances = new int[4]; static final int kDefaultDictionaryLogSize = 22; static final int kNumFastBytesDefault = 32; public static final int kNumLenSpecSymbols = 16; static final int kNumOpts = 4096;
/*      */   
/*      */   void BaseInit() {
/*   61 */     this._state = Base.StateInit();
/*   62 */     this._previousByte = 0;
/*   63 */     for (int i = 0; i < 4; i++)
/*   64 */       this._repDistances[i] = 0; 
/*      */   }
/*      */   
/*      */   class LiteralEncoder { Encoder2[] m_Coders;
/*      */     int m_NumPrevBits;
/*      */     int m_NumPosBits;
/*      */     int m_PosMask;
/*      */     
/*   72 */     class Encoder2 { short[] m_Encoders = new short[768];
/*      */       
/*      */       public void Init() {
/*   75 */         com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this.m_Encoders);
/*      */       }
/*      */       
/*      */       public void Encode(com.badlogic.gdx.utils.compression.rangecoder.Encoder rangeEncoder, byte symbol) throws IOException {
/*   79 */         int context = 1;
/*   80 */         for (int i = 7; i >= 0; i--) {
/*   81 */           int bit = symbol >> i & 0x1;
/*   82 */           rangeEncoder.Encode(this.m_Encoders, context, bit);
/*   83 */           context = context << 1 | bit;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       public void EncodeMatched(com.badlogic.gdx.utils.compression.rangecoder.Encoder rangeEncoder, byte matchByte, byte symbol) throws IOException {
/*   89 */         int context = 1;
/*   90 */         boolean same = true;
/*   91 */         for (int i = 7; i >= 0; i--) {
/*   92 */           int bit = symbol >> i & 0x1;
/*   93 */           int state = context;
/*   94 */           if (same) {
/*   95 */             int matchBit = matchByte >> i & 0x1;
/*   96 */             state += 1 + matchBit << 8;
/*   97 */             same = (matchBit == bit);
/*      */           } 
/*   99 */           rangeEncoder.Encode(this.m_Encoders, state, bit);
/*  100 */           context = context << 1 | bit;
/*      */         } 
/*      */       }
/*      */       
/*      */       public int GetPrice(boolean matchMode, byte matchByte, byte symbol) {
/*  105 */         int price = 0;
/*  106 */         int context = 1;
/*  107 */         int i = 7;
/*  108 */         if (matchMode) {
/*  109 */           for (; i >= 0; i--) {
/*  110 */             int matchBit = matchByte >> i & 0x1;
/*  111 */             int bit = symbol >> i & 0x1;
/*  112 */             price += com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice(this.m_Encoders[(1 + matchBit << 8) + context], bit);
/*      */             
/*  114 */             context = context << 1 | bit;
/*  115 */             if (matchBit != bit) {
/*  116 */               i--;
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/*  121 */         for (; i >= 0; i--) {
/*  122 */           int bit = symbol >> i & 0x1;
/*  123 */           price += com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice(this.m_Encoders[context], bit);
/*  124 */           context = context << 1 | bit;
/*      */         } 
/*  126 */         return price;
/*      */       } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void Create(int numPosBits, int numPrevBits) {
/*  136 */       if (this.m_Coders != null && this.m_NumPrevBits == numPrevBits && this.m_NumPosBits == numPosBits)
/*  137 */         return;  this.m_NumPosBits = numPosBits;
/*  138 */       this.m_PosMask = (1 << numPosBits) - 1;
/*  139 */       this.m_NumPrevBits = numPrevBits;
/*  140 */       int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits;
/*  141 */       this.m_Coders = new Encoder2[numStates];
/*  142 */       for (int i = 0; i < numStates; i++)
/*  143 */         this.m_Coders[i] = new Encoder2(); 
/*      */     }
/*      */     
/*      */     public void Init() {
/*  147 */       int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits;
/*  148 */       for (int i = 0; i < numStates; i++)
/*  149 */         this.m_Coders[i].Init(); 
/*      */     }
/*      */     
/*      */     public Encoder2 GetSubCoder(int pos, byte prevByte) {
/*  153 */       return this.m_Coders[((pos & this.m_PosMask) << this.m_NumPrevBits) + ((prevByte & 0xFF) >>> 8 - this.m_NumPrevBits)];
/*      */     } }
/*      */    class Encoder2 { short[] m_Encoders = new short[768]; public void Init() { com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this.m_Encoders); } public void Encode(com.badlogic.gdx.utils.compression.rangecoder.Encoder rangeEncoder, byte symbol) throws IOException { int context = 1; for (int i = 7; i >= 0; i--) { int bit = symbol >> i & 0x1; rangeEncoder.Encode(this.m_Encoders, context, bit); context = context << 1 | bit; }  }
/*      */     public void EncodeMatched(com.badlogic.gdx.utils.compression.rangecoder.Encoder rangeEncoder, byte matchByte, byte symbol) throws IOException { int context = 1; boolean same = true; for (int i = 7; i >= 0; i--) { int bit = symbol >> i & 0x1; int state = context; if (same) { int matchBit = matchByte >> i & 0x1; state += 1 + matchBit << 8; same = (matchBit == bit); }  rangeEncoder.Encode(this.m_Encoders, state, bit); context = context << 1 | bit; }  }
/*      */     public int GetPrice(boolean matchMode, byte matchByte, byte symbol) { int price = 0; int context = 1; int i = 7; if (matchMode) for (; i >= 0; i--) { int matchBit = matchByte >> i & 0x1; int bit = symbol >> i & 0x1; price += com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice(this.m_Encoders[(1 + matchBit << 8) + context], bit); context = context << 1 | bit; if (matchBit != bit) { i--; break; }  }   for (; i >= 0; i--) { int bit = symbol >> i & 0x1; price += com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice(this.m_Encoders[context], bit); context = context << 1 | bit; }  return price; } }
/*  158 */   class LenEncoder { short[] _choice = new short[2];
/*  159 */     BitTreeEncoder[] _lowCoder = new BitTreeEncoder[16];
/*  160 */     BitTreeEncoder[] _midCoder = new BitTreeEncoder[16];
/*  161 */     BitTreeEncoder _highCoder = new BitTreeEncoder(8);
/*      */     
/*      */     public LenEncoder() {
/*  164 */       for (int posState = 0; posState < 16; posState++) {
/*  165 */         this._lowCoder[posState] = new BitTreeEncoder(3);
/*  166 */         this._midCoder[posState] = new BitTreeEncoder(3);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void Init(int numPosStates) {
/*  171 */       com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._choice);
/*      */       
/*  173 */       for (int posState = 0; posState < numPosStates; posState++) {
/*  174 */         this._lowCoder[posState].Init();
/*  175 */         this._midCoder[posState].Init();
/*      */       } 
/*  177 */       this._highCoder.Init();
/*      */     }
/*      */ 
/*      */     
/*      */     public void Encode(com.badlogic.gdx.utils.compression.rangecoder.Encoder rangeEncoder, int symbol, int posState) throws IOException {
/*  182 */       if (symbol < 8) {
/*  183 */         rangeEncoder.Encode(this._choice, 0, 0);
/*  184 */         this._lowCoder[posState].Encode(rangeEncoder, symbol);
/*      */       } else {
/*  186 */         symbol -= 8;
/*  187 */         rangeEncoder.Encode(this._choice, 0, 1);
/*  188 */         if (symbol < 8) {
/*  189 */           rangeEncoder.Encode(this._choice, 1, 0);
/*  190 */           this._midCoder[posState].Encode(rangeEncoder, symbol);
/*      */         } else {
/*  192 */           rangeEncoder.Encode(this._choice, 1, 1);
/*  193 */           this._highCoder.Encode(rangeEncoder, symbol - 8);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void SetPrices(int posState, int numSymbols, int[] prices, int st) {
/*  199 */       int a0 = com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._choice[0]);
/*  200 */       int a1 = com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._choice[0]);
/*  201 */       int b0 = a1 + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._choice[1]);
/*  202 */       int b1 = a1 + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._choice[1]);
/*  203 */       int i = 0;
/*  204 */       for (i = 0; i < 8; i++) {
/*  205 */         if (i >= numSymbols)
/*  206 */           return;  prices[st + i] = a0 + this._lowCoder[posState].GetPrice(i);
/*      */       } 
/*  208 */       for (; i < 16; i++) {
/*  209 */         if (i >= numSymbols)
/*  210 */           return;  prices[st + i] = b0 + this._midCoder[posState].GetPrice(i - 8);
/*      */       } 
/*  212 */       for (; i < numSymbols; i++)
/*  213 */         prices[st + i] = b1 + this._highCoder.GetPrice(i - 8 - 8); 
/*      */     } }
/*      */   class LenPriceTableEncoder extends LenEncoder { int[] _prices;
/*      */     int _tableSize;
/*      */     int[] _counters;
/*      */     
/*      */     LenPriceTableEncoder() {
/*  220 */       this._prices = new int[4352];
/*      */       
/*  222 */       this._counters = new int[16];
/*      */     }
/*      */     public void SetTableSize(int tableSize) {
/*  225 */       this._tableSize = tableSize;
/*      */     }
/*      */     
/*      */     public int GetPrice(int symbol, int posState) {
/*  229 */       return this._prices[posState * 272 + symbol];
/*      */     }
/*      */     
/*      */     void UpdateTable(int posState) {
/*  233 */       SetPrices(posState, this._tableSize, this._prices, posState * 272);
/*  234 */       this._counters[posState] = this._tableSize;
/*      */     }
/*      */     
/*      */     public void UpdateTables(int numPosStates) {
/*  238 */       for (int posState = 0; posState < numPosStates; posState++) {
/*  239 */         UpdateTable(posState);
/*      */       }
/*      */     }
/*      */     
/*      */     public void Encode(com.badlogic.gdx.utils.compression.rangecoder.Encoder rangeEncoder, int symbol, int posState) throws IOException {
/*  244 */       super.Encode(rangeEncoder, symbol, posState);
/*  245 */       this._counters[posState] = this._counters[posState] - 1; if (this._counters[posState] - 1 == 0) UpdateTable(posState);
/*      */     
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   class Optimal
/*      */   {
/*      */     public int State;
/*      */     
/*      */     public boolean Prev1IsChar;
/*      */     
/*      */     public boolean Prev2;
/*      */     
/*      */     public int PosPrev2;
/*      */     public int BackPrev2;
/*      */     public int Price;
/*      */     public int PosPrev;
/*      */     public int BackPrev;
/*      */     public int Backs0;
/*      */     public int Backs1;
/*      */     public int Backs2;
/*      */     public int Backs3;
/*      */     
/*      */     public void MakeAsChar() {
/*  270 */       this.BackPrev = -1;
/*  271 */       this.Prev1IsChar = false;
/*      */     }
/*      */     
/*      */     public void MakeAsShortRep() {
/*  275 */       this.BackPrev = 0;
/*      */       
/*  277 */       this.Prev1IsChar = false;
/*      */     }
/*      */     
/*      */     public boolean IsShortRep() {
/*  281 */       return (this.BackPrev == 0);
/*      */     }
/*      */   }
/*      */   
/*  285 */   Optimal[] _optimum = new Optimal[4096];
/*  286 */   BinTree _matchFinder = null;
/*  287 */   com.badlogic.gdx.utils.compression.rangecoder.Encoder _rangeEncoder = new com.badlogic.gdx.utils.compression.rangecoder.Encoder();
/*      */   
/*  289 */   short[] _isMatch = new short[192];
/*  290 */   short[] _isRep = new short[12];
/*  291 */   short[] _isRepG0 = new short[12];
/*  292 */   short[] _isRepG1 = new short[12];
/*  293 */   short[] _isRepG2 = new short[12];
/*  294 */   short[] _isRep0Long = new short[192];
/*      */   
/*  296 */   BitTreeEncoder[] _posSlotEncoder = new BitTreeEncoder[4];
/*      */   
/*  298 */   short[] _posEncoders = new short[114];
/*  299 */   BitTreeEncoder _posAlignEncoder = new BitTreeEncoder(4);
/*      */   
/*  301 */   LenPriceTableEncoder _lenEncoder = new LenPriceTableEncoder();
/*  302 */   LenPriceTableEncoder _repMatchLenEncoder = new LenPriceTableEncoder();
/*      */   
/*  304 */   LiteralEncoder _literalEncoder = new LiteralEncoder();
/*      */   
/*  306 */   int[] _matchDistances = new int[548];
/*      */   
/*  308 */   int _numFastBytes = 32;
/*      */   
/*      */   int _longestMatchLength;
/*      */   
/*      */   int _numDistancePairs;
/*      */   
/*      */   int _additionalOffset;
/*      */   
/*      */   int _optimumEndIndex;
/*      */   int _optimumCurrentIndex;
/*      */   boolean _longestMatchWasFound;
/*  319 */   int[] _posSlotPrices = new int[256];
/*  320 */   int[] _distancesPrices = new int[512];
/*  321 */   int[] _alignPrices = new int[16];
/*      */   
/*      */   int _alignPriceCount;
/*  324 */   int _distTableSize = 44;
/*      */   
/*  326 */   int _posStateBits = 2;
/*  327 */   int _posStateMask = 3;
/*  328 */   int _numLiteralPosStateBits = 0;
/*  329 */   int _numLiteralContextBits = 3;
/*      */   
/*  331 */   int _dictionarySize = 4194304;
/*  332 */   int _dictionarySizePrev = -1;
/*  333 */   int _numFastBytesPrev = -1;
/*      */   
/*      */   long nowPos64;
/*      */   
/*      */   boolean _finished;
/*      */   InputStream _inStream;
/*  339 */   int _matchFinderType = 1; boolean _writeEndMark = false; boolean _needReleaseMFStream = false; int[] reps; int[] repLens; int backRes; long[] processedInSize; long[] processedOutSize; boolean[] finished; public static final int kPropSize = 5;
/*      */   byte[] properties;
/*      */   int[] tempPrices;
/*      */   int _matchPriceCount;
/*      */   
/*      */   void Create() {
/*  345 */     if (this._matchFinder == null) {
/*  346 */       BinTree bt = new BinTree();
/*  347 */       int numHashBytes = 4;
/*  348 */       if (this._matchFinderType == 0) numHashBytes = 2; 
/*  349 */       bt.SetType(numHashBytes);
/*  350 */       this._matchFinder = bt;
/*      */     } 
/*  352 */     this._literalEncoder.Create(this._numLiteralPosStateBits, this._numLiteralContextBits);
/*      */     
/*  354 */     if (this._dictionarySize == this._dictionarySizePrev && this._numFastBytesPrev == this._numFastBytes)
/*  355 */       return;  this._matchFinder.Create(this._dictionarySize, 4096, this._numFastBytes, 274);
/*  356 */     this._dictionarySizePrev = this._dictionarySize;
/*  357 */     this._numFastBytesPrev = this._numFastBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void SetWriteEndMarkerMode(boolean writeEndMarker) {
/*  368 */     this._writeEndMark = writeEndMarker;
/*      */   }
/*      */   
/*      */   void Init() {
/*  372 */     BaseInit();
/*  373 */     this._rangeEncoder.Init();
/*      */     
/*  375 */     com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._isMatch);
/*  376 */     com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._isRep0Long);
/*  377 */     com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._isRep);
/*  378 */     com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._isRepG0);
/*  379 */     com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._isRepG1);
/*  380 */     com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._isRepG2);
/*  381 */     com.badlogic.gdx.utils.compression.rangecoder.Encoder.InitBitModels(this._posEncoders);
/*      */     
/*  383 */     this._literalEncoder.Init();
/*  384 */     for (int i = 0; i < 4; i++) {
/*  385 */       this._posSlotEncoder[i].Init();
/*      */     }
/*  387 */     this._lenEncoder.Init(1 << this._posStateBits);
/*  388 */     this._repMatchLenEncoder.Init(1 << this._posStateBits);
/*      */     
/*  390 */     this._posAlignEncoder.Init();
/*      */     
/*  392 */     this._longestMatchWasFound = false;
/*  393 */     this._optimumEndIndex = 0;
/*  394 */     this._optimumCurrentIndex = 0;
/*  395 */     this._additionalOffset = 0;
/*      */   }
/*      */   
/*      */   int ReadMatchDistances() throws IOException {
/*  399 */     int lenRes = 0;
/*  400 */     this._numDistancePairs = this._matchFinder.GetMatches(this._matchDistances);
/*  401 */     if (this._numDistancePairs > 0) {
/*  402 */       lenRes = this._matchDistances[this._numDistancePairs - 2];
/*  403 */       if (lenRes == this._numFastBytes) {
/*  404 */         lenRes += this._matchFinder.GetMatchLen(lenRes - 1, this._matchDistances[this._numDistancePairs - 1], 273 - lenRes);
/*      */       }
/*      */     } 
/*  407 */     this._additionalOffset++;
/*  408 */     return lenRes;
/*      */   }
/*      */   
/*      */   void MovePos(int num) throws IOException {
/*  412 */     if (num > 0) {
/*  413 */       this._matchFinder.Skip(num);
/*  414 */       this._additionalOffset += num;
/*      */     } 
/*      */   }
/*      */   
/*      */   int GetRepLen1Price(int state, int posState) {
/*  419 */     return com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isRepG0[state]) + 
/*  420 */       com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isRep0Long[(state << 4) + posState]);
/*      */   }
/*      */ 
/*      */   
/*      */   int GetPureRepPrice(int repIndex, int state, int posState) {
/*      */     int price;
/*  426 */     if (repIndex == 0) {
/*  427 */       price = com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isRepG0[state]);
/*  428 */       price += 
/*  429 */         com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRep0Long[(state << 4) + posState]);
/*      */     } else {
/*  431 */       price = com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRepG0[state]);
/*  432 */       if (repIndex == 1) {
/*  433 */         price += com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isRepG1[state]);
/*      */       } else {
/*  435 */         price += com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRepG1[state]);
/*  436 */         price += com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice(this._isRepG2[state], repIndex - 2);
/*      */       } 
/*      */     } 
/*  439 */     return price;
/*      */   }
/*      */   
/*      */   int GetRepPrice(int repIndex, int len, int state, int posState) {
/*  443 */     int price = this._repMatchLenEncoder.GetPrice(len - 2, posState);
/*  444 */     return price + GetPureRepPrice(repIndex, state, posState);
/*      */   }
/*      */ 
/*      */   
/*      */   int GetPosLenPrice(int pos, int len, int posState) {
/*  449 */     int price, lenToPosState = Base.GetLenToPosState(len);
/*  450 */     if (pos < 128) {
/*  451 */       price = this._distancesPrices[lenToPosState * 128 + pos];
/*      */     } else {
/*  453 */       price = this._posSlotPrices[(lenToPosState << 6) + GetPosSlot2(pos)] + this._alignPrices[pos & 0xF];
/*  454 */     }  return price + this._lenEncoder.GetPrice(len - 2, posState);
/*      */   }
/*      */   
/*      */   int Backward(int cur) {
/*  458 */     this._optimumEndIndex = cur;
/*  459 */     int posMem = (this._optimum[cur]).PosPrev;
/*  460 */     int backMem = (this._optimum[cur]).BackPrev;
/*      */     while (true) {
/*  462 */       if ((this._optimum[cur]).Prev1IsChar) {
/*  463 */         this._optimum[posMem].MakeAsChar();
/*  464 */         (this._optimum[posMem]).PosPrev = posMem - 1;
/*  465 */         if ((this._optimum[cur]).Prev2) {
/*  466 */           (this._optimum[posMem - 1]).Prev1IsChar = false;
/*  467 */           (this._optimum[posMem - 1]).PosPrev = (this._optimum[cur]).PosPrev2;
/*  468 */           (this._optimum[posMem - 1]).BackPrev = (this._optimum[cur]).BackPrev2;
/*      */         } 
/*      */       } 
/*  471 */       int posPrev = posMem;
/*  472 */       int backCur = backMem;
/*      */       
/*  474 */       backMem = (this._optimum[posPrev]).BackPrev;
/*  475 */       posMem = (this._optimum[posPrev]).PosPrev;
/*      */       
/*  477 */       (this._optimum[posPrev]).BackPrev = backCur;
/*  478 */       (this._optimum[posPrev]).PosPrev = cur;
/*  479 */       cur = posPrev;
/*  480 */       if (cur <= 0) {
/*  481 */         this.backRes = (this._optimum[0]).BackPrev;
/*  482 */         this._optimumCurrentIndex = (this._optimum[0]).PosPrev;
/*  483 */         return this._optimumCurrentIndex;
/*      */       } 
/*      */     } 
/*  486 */   } public Encoder() { this.reps = new int[4];
/*  487 */     this.repLens = new int[4];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1122 */     this.processedInSize = new long[1];
/* 1123 */     this.processedOutSize = new long[1];
/* 1124 */     this.finished = new boolean[1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1145 */     this.properties = new byte[5];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1154 */     this.tempPrices = new int[128]; int i; for (i = 0; i < 4096; i++) this._optimum[i] = new Optimal();  for (i = 0; i < 4; i++) this._posSlotEncoder[i] = new BitTreeEncoder(6);  }
/*      */   int GetOptimum(int position) throws IOException { int lenMain; if (this._optimumEndIndex != this._optimumCurrentIndex) { int lenRes = (this._optimum[this._optimumCurrentIndex]).PosPrev - this._optimumCurrentIndex; this.backRes = (this._optimum[this._optimumCurrentIndex]).BackPrev; this._optimumCurrentIndex = (this._optimum[this._optimumCurrentIndex]).PosPrev; return lenRes; }  this._optimumCurrentIndex = this._optimumEndIndex = 0; if (!this._longestMatchWasFound) { lenMain = ReadMatchDistances(); } else { lenMain = this._longestMatchLength; this._longestMatchWasFound = false; }  int numDistancePairs = this._numDistancePairs; int numAvailableBytes = this._matchFinder.GetNumAvailableBytes() + 1; if (numAvailableBytes < 2) { this.backRes = -1; return 1; }  if (numAvailableBytes > 273) numAvailableBytes = 273;  int repMaxIndex = 0; int i; for (i = 0; i < 4; i++) { this.reps[i] = this._repDistances[i]; this.repLens[i] = this._matchFinder.GetMatchLen(-1, this.reps[i], 273); if (this.repLens[i] > this.repLens[repMaxIndex]) repMaxIndex = i;  }  if (this.repLens[repMaxIndex] >= this._numFastBytes) { this.backRes = repMaxIndex; int lenRes = this.repLens[repMaxIndex]; MovePos(lenRes - 1); return lenRes; }  if (lenMain >= this._numFastBytes) { this.backRes = this._matchDistances[numDistancePairs - 1] + 4; MovePos(lenMain - 1); return lenMain; }  byte currentByte = this._matchFinder.GetIndexByte(-1); byte matchByte = this._matchFinder.GetIndexByte(0 - this._repDistances[0] - 1 - 1); if (lenMain < 2 && currentByte != matchByte && this.repLens[repMaxIndex] < 2) { this.backRes = -1; return 1; }  (this._optimum[0]).State = this._state; int posState = position & this._posStateMask; (this._optimum[1]).Price = com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isMatch[(this._state << 4) + posState]) + this._literalEncoder.GetSubCoder(position, this._previousByte).GetPrice(!Base.StateIsCharState(this._state), matchByte, currentByte); this._optimum[1].MakeAsChar(); int matchPrice = com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isMatch[(this._state << 4) + posState]); int repMatchPrice = matchPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRep[this._state]); if (matchByte == currentByte) { int shortRepPrice = repMatchPrice + GetRepLen1Price(this._state, posState); if (shortRepPrice < (this._optimum[1]).Price) { (this._optimum[1]).Price = shortRepPrice; this._optimum[1].MakeAsShortRep(); }  }  int lenEnd = (lenMain >= this.repLens[repMaxIndex]) ? lenMain : this.repLens[repMaxIndex]; if (lenEnd < 2) { this.backRes = (this._optimum[1]).BackPrev; return 1; }  (this._optimum[1]).PosPrev = 0; (this._optimum[0]).Backs0 = this.reps[0]; (this._optimum[0]).Backs1 = this.reps[1]; (this._optimum[0]).Backs2 = this.reps[2]; (this._optimum[0]).Backs3 = this.reps[3]; int len = lenEnd; do { (this._optimum[len--]).Price = 268435455; } while (len >= 2); for (i = 0; i < 4; i++) { int repLen = this.repLens[i]; if (repLen >= 2) { int price = repMatchPrice + GetPureRepPrice(i, this._state, posState); do { int curAndLenPrice = price + this._repMatchLenEncoder.GetPrice(repLen - 2, posState); Optimal optimum = this._optimum[repLen]; if (curAndLenPrice >= optimum.Price) continue;  optimum.Price = curAndLenPrice; optimum.PosPrev = 0; optimum.BackPrev = i; optimum.Prev1IsChar = false; } while (--repLen >= 2); }  }  int normalMatchPrice = matchPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isRep[this._state]); len = (this.repLens[0] >= 2) ? (this.repLens[0] + 1) : 2; if (len <= lenMain) { int offs = 0; while (len > this._matchDistances[offs]) offs += 2;  for (;; len++) { int distance = this._matchDistances[offs + 1]; int curAndLenPrice = normalMatchPrice + GetPosLenPrice(distance, len, posState); Optimal optimum = this._optimum[len]; if (curAndLenPrice < optimum.Price) { optimum.Price = curAndLenPrice; optimum.PosPrev = 0; optimum.BackPrev = distance + 4; optimum.Prev1IsChar = false; }  offs += 2; if (len == this._matchDistances[offs] && offs == numDistancePairs) break;  }  }  int cur = 0; label238: while (true) { int state; cur++; if (cur == lenEnd) return Backward(cur);  int newLen = ReadMatchDistances(); numDistancePairs = this._numDistancePairs; if (newLen >= this._numFastBytes) { this._longestMatchLength = newLen; this._longestMatchWasFound = true; return Backward(cur); }  position++; int posPrev = (this._optimum[cur]).PosPrev; if ((this._optimum[cur]).Prev1IsChar) { posPrev--; if ((this._optimum[cur]).Prev2) { state = (this._optimum[(this._optimum[cur]).PosPrev2]).State; if ((this._optimum[cur]).BackPrev2 < 4) { state = Base.StateUpdateRep(state); } else { state = Base.StateUpdateMatch(state); }  } else { state = (this._optimum[posPrev]).State; }  state = Base.StateUpdateChar(state); } else { state = (this._optimum[posPrev]).State; }  if (posPrev == cur - 1) { if (this._optimum[cur].IsShortRep()) { state = Base.StateUpdateShortRep(state); } else { state = Base.StateUpdateChar(state); }  } else { int pos; if ((this._optimum[cur]).Prev1IsChar && (this._optimum[cur]).Prev2) { posPrev = (this._optimum[cur]).PosPrev2; pos = (this._optimum[cur]).BackPrev2; state = Base.StateUpdateRep(state); } else { pos = (this._optimum[cur]).BackPrev; if (pos < 4) { state = Base.StateUpdateRep(state); } else { state = Base.StateUpdateMatch(state); }  }  Optimal opt = this._optimum[posPrev]; if (pos < 4) { if (pos == 0) { this.reps[0] = opt.Backs0; this.reps[1] = opt.Backs1; this.reps[2] = opt.Backs2; this.reps[3] = opt.Backs3; } else if (pos == 1) { this.reps[0] = opt.Backs1; this.reps[1] = opt.Backs0; this.reps[2] = opt.Backs2; this.reps[3] = opt.Backs3; } else if (pos == 2) { this.reps[0] = opt.Backs2; this.reps[1] = opt.Backs0; this.reps[2] = opt.Backs1; this.reps[3] = opt.Backs3; } else { this.reps[0] = opt.Backs3; this.reps[1] = opt.Backs0; this.reps[2] = opt.Backs1; this.reps[3] = opt.Backs2; }  } else { this.reps[0] = pos - 4; this.reps[1] = opt.Backs0; this.reps[2] = opt.Backs1; this.reps[3] = opt.Backs2; }  }  (this._optimum[cur]).State = state; (this._optimum[cur]).Backs0 = this.reps[0]; (this._optimum[cur]).Backs1 = this.reps[1]; (this._optimum[cur]).Backs2 = this.reps[2]; (this._optimum[cur]).Backs3 = this.reps[3]; int curPrice = (this._optimum[cur]).Price; currentByte = this._matchFinder.GetIndexByte(-1); matchByte = this._matchFinder.GetIndexByte(0 - this.reps[0] - 1 - 1); posState = position & this._posStateMask; int curAnd1Price = curPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isMatch[(state << 4) + posState]) + this._literalEncoder.GetSubCoder(position, this._matchFinder.GetIndexByte(-2)).GetPrice(!Base.StateIsCharState(state), matchByte, currentByte); Optimal nextOptimum = this._optimum[cur + 1]; boolean nextIsChar = false; if (curAnd1Price < nextOptimum.Price) { nextOptimum.Price = curAnd1Price; nextOptimum.PosPrev = cur; nextOptimum.MakeAsChar(); nextIsChar = true; }  matchPrice = curPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isMatch[(state << 4) + posState]); repMatchPrice = matchPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRep[state]); if (matchByte == currentByte && (nextOptimum.PosPrev >= cur || nextOptimum.BackPrev != 0)) { int shortRepPrice = repMatchPrice + GetRepLen1Price(state, posState); if (shortRepPrice <= nextOptimum.Price) { nextOptimum.Price = shortRepPrice; nextOptimum.PosPrev = cur; nextOptimum.MakeAsShortRep(); nextIsChar = true; }  }  int numAvailableBytesFull = this._matchFinder.GetNumAvailableBytes() + 1; numAvailableBytesFull = Math.min(4095 - cur, numAvailableBytesFull); numAvailableBytes = numAvailableBytesFull; if (numAvailableBytes < 2) continue;  if (numAvailableBytes > this._numFastBytes) numAvailableBytes = this._numFastBytes;  if (!nextIsChar && matchByte != currentByte) { int t = Math.min(numAvailableBytesFull - 1, this._numFastBytes); int lenTest2 = this._matchFinder.GetMatchLen(0, this.reps[0], t); if (lenTest2 >= 2) { int state2 = Base.StateUpdateChar(state); int posStateNext = position + 1 & this._posStateMask; int nextRepMatchPrice = curAnd1Price + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isMatch[(state2 << 4) + posStateNext]) + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRep[state2]); int offset = cur + 1 + lenTest2; while (lenEnd < offset) (this._optimum[++lenEnd]).Price = 268435455;  int curAndLenPrice = nextRepMatchPrice + GetRepPrice(0, lenTest2, state2, posStateNext); Optimal optimum = this._optimum[offset]; if (curAndLenPrice < optimum.Price) { optimum.Price = curAndLenPrice; optimum.PosPrev = cur + 1; optimum.BackPrev = 0; optimum.Prev1IsChar = true; optimum.Prev2 = false; }  }  }  int startLen = 2; for (int repIndex = 0; repIndex < 4; repIndex++) { int lenTest = this._matchFinder.GetMatchLen(-1, this.reps[repIndex], numAvailableBytes); if (lenTest >= 2) { int lenTestTemp = lenTest; while (true) { while (lenEnd < cur + lenTest) (this._optimum[++lenEnd]).Price = 268435455;  int curAndLenPrice = repMatchPrice + GetRepPrice(repIndex, lenTest, state, posState); Optimal optimum = this._optimum[cur + lenTest]; if (curAndLenPrice < optimum.Price) { optimum.Price = curAndLenPrice; optimum.PosPrev = cur; optimum.BackPrev = repIndex; optimum.Prev1IsChar = false; }  if (--lenTest < 2) { lenTest = lenTestTemp; if (repIndex == 0) startLen = lenTest + 1;  if (lenTest < numAvailableBytesFull) { int t = Math.min(numAvailableBytesFull - 1 - lenTest, this._numFastBytes); int lenTest2 = this._matchFinder.GetMatchLen(lenTest, this.reps[repIndex], t); if (lenTest2 >= 2) { int state2 = Base.StateUpdateRep(state); int posStateNext = position + lenTest & this._posStateMask; int curAndLenCharPrice = repMatchPrice + GetRepPrice(repIndex, lenTest, state, posState) + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isMatch[(state2 << 4) + posStateNext]) + this._literalEncoder.GetSubCoder(position + lenTest, this._matchFinder.GetIndexByte(lenTest - 1 - 1)).GetPrice(true, this._matchFinder.GetIndexByte(lenTest - 1 - this.reps[repIndex] + 1), this._matchFinder.GetIndexByte(lenTest - 1)); state2 = Base.StateUpdateChar(state2); posStateNext = position + lenTest + 1 & this._posStateMask; int nextMatchPrice = curAndLenCharPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isMatch[(state2 << 4) + posStateNext]); int nextRepMatchPrice = nextMatchPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRep[state2]); int offset = lenTest + 1 + lenTest2; while (lenEnd < cur + offset) (this._optimum[++lenEnd]).Price = 268435455;  int j = nextRepMatchPrice + GetRepPrice(0, lenTest2, state2, posStateNext); Optimal optimal = this._optimum[cur + offset]; if (j < optimal.Price) { optimal.Price = j; optimal.PosPrev = cur + lenTest + 1; optimal.BackPrev = 0; optimal.Prev1IsChar = true; optimal.Prev2 = true; optimal.PosPrev2 = cur; optimal.BackPrev2 = repIndex; }  }  }  break; }  }  }  }  if (newLen > numAvailableBytes) { newLen = numAvailableBytes; for (numDistancePairs = 0; newLen > this._matchDistances[numDistancePairs]; numDistancePairs += 2); this._matchDistances[numDistancePairs] = newLen; numDistancePairs += 2; }  if (newLen >= startLen) { normalMatchPrice = matchPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isRep[state]); while (lenEnd < cur + newLen) (this._optimum[++lenEnd]).Price = 268435455;  int offs = 0; while (startLen > this._matchDistances[offs]) offs += 2;  for (int lenTest = startLen;; lenTest++) { int curBack = this._matchDistances[offs + 1]; int curAndLenPrice = normalMatchPrice + GetPosLenPrice(curBack, lenTest, posState); Optimal optimum = this._optimum[cur + lenTest]; if (curAndLenPrice < optimum.Price) { optimum.Price = curAndLenPrice; optimum.PosPrev = cur; optimum.BackPrev = curBack + 4; optimum.Prev1IsChar = false; }  if (lenTest == this._matchDistances[offs]) { if (lenTest < numAvailableBytesFull) { int t = Math.min(numAvailableBytesFull - 1 - lenTest, this._numFastBytes); int lenTest2 = this._matchFinder.GetMatchLen(lenTest, curBack, t); if (lenTest2 >= 2) { int state2 = Base.StateUpdateMatch(state); int posStateNext = position + lenTest & this._posStateMask; int curAndLenCharPrice = curAndLenPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice0(this._isMatch[(state2 << 4) + posStateNext]) + this._literalEncoder.GetSubCoder(position + lenTest, this._matchFinder.GetIndexByte(lenTest - 1 - 1)).GetPrice(true, this._matchFinder.GetIndexByte(lenTest - curBack + 1 - 1), this._matchFinder.GetIndexByte(lenTest - 1)); state2 = Base.StateUpdateChar(state2); posStateNext = position + lenTest + 1 & this._posStateMask; int nextMatchPrice = curAndLenCharPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isMatch[(state2 << 4) + posStateNext]); int nextRepMatchPrice = nextMatchPrice + com.badlogic.gdx.utils.compression.rangecoder.Encoder.GetPrice1(this._isRep[state2]); int offset = lenTest + 1 + lenTest2; while (lenEnd < cur + offset) (this._optimum[++lenEnd]).Price = 268435455;  curAndLenPrice = nextRepMatchPrice + GetRepPrice(0, lenTest2, state2, posStateNext); optimum = this._optimum[cur + offset]; if (curAndLenPrice < optimum.Price) { optimum.Price = curAndLenPrice; optimum.PosPrev = cur + lenTest + 1; optimum.BackPrev = 0; optimum.Prev1IsChar = true; optimum.Prev2 = true; optimum.PosPrev2 = cur; optimum.BackPrev2 = curBack + 4; }  }  }  offs += 2; if (offs == numDistancePairs) continue label238;  }  }  break; }  }  }
/*      */   boolean ChangePair(int smallDist, int bigDist) { int kDif = 7; return (smallDist < 1 << 32 - kDif && bigDist >= smallDist << kDif); }
/*      */   void WriteEndMarker(int posState) throws IOException { if (!this._writeEndMark) return;  this._rangeEncoder.Encode(this._isMatch, (this._state << 4) + posState, 1); this._rangeEncoder.Encode(this._isRep, this._state, 0); this._state = Base.StateUpdateMatch(this._state); int len = 2; this._lenEncoder.Encode(this._rangeEncoder, len - 2, posState); int posSlot = 63; int lenToPosState = Base.GetLenToPosState(len); this._posSlotEncoder[lenToPosState].Encode(this._rangeEncoder, posSlot); int footerBits = 30; int posReduced = (1 << footerBits) - 1; this._rangeEncoder.EncodeDirectBits(posReduced >> 4, footerBits - 4); this._posAlignEncoder.ReverseEncode(this._rangeEncoder, posReduced & 0xF); }
/* 1158 */   void Flush(int nowPos) throws IOException { ReleaseMFStream(); WriteEndMarker(nowPos & this._posStateMask); this._rangeEncoder.FlushData(); this._rangeEncoder.FlushStream(); } public void CodeOneBlock(long[] inSize, long[] outSize, boolean[] finished) throws IOException { inSize[0] = 0L; outSize[0] = 0L; finished[0] = true; if (this._inStream != null) { this._matchFinder.SetStream(this._inStream); this._matchFinder.Init(); this._needReleaseMFStream = true; this._inStream = null; }  if (this._finished) return;  this._finished = true; long progressPosValuePrev = this.nowPos64; if (this.nowPos64 == 0L) { if (this._matchFinder.GetNumAvailableBytes() == 0) { Flush((int)this.nowPos64); return; }  ReadMatchDistances(); int posState = (int)this.nowPos64 & this._posStateMask; this._rangeEncoder.Encode(this._isMatch, (this._state << 4) + posState, 0); this._state = Base.StateUpdateChar(this._state); byte curByte = this._matchFinder.GetIndexByte(0 - this._additionalOffset); this._literalEncoder.GetSubCoder((int)this.nowPos64, this._previousByte).Encode(this._rangeEncoder, curByte); this._previousByte = curByte; this._additionalOffset--; this.nowPos64++; }  if (this._matchFinder.GetNumAvailableBytes() == 0) { Flush((int)this.nowPos64); return; }  while (true) { int len = GetOptimum((int)this.nowPos64); int pos = this.backRes; int posState = (int)this.nowPos64 & this._posStateMask; int complexState = (this._state << 4) + posState; if (len == 1 && pos == -1) { this._rangeEncoder.Encode(this._isMatch, complexState, 0); byte curByte = this._matchFinder.GetIndexByte(0 - this._additionalOffset); LiteralEncoder.Encoder2 subCoder = this._literalEncoder.GetSubCoder((int)this.nowPos64, this._previousByte); if (!Base.StateIsCharState(this._state)) { byte matchByte = this._matchFinder.GetIndexByte(0 - this._repDistances[0] - 1 - this._additionalOffset); subCoder.EncodeMatched(this._rangeEncoder, matchByte, curByte); } else { subCoder.Encode(this._rangeEncoder, curByte); }  this._previousByte = curByte; this._state = Base.StateUpdateChar(this._state); } else { this._rangeEncoder.Encode(this._isMatch, complexState, 1); if (pos < 4) { this._rangeEncoder.Encode(this._isRep, this._state, 1); if (pos == 0) { this._rangeEncoder.Encode(this._isRepG0, this._state, 0); if (len == 1) { this._rangeEncoder.Encode(this._isRep0Long, complexState, 0); } else { this._rangeEncoder.Encode(this._isRep0Long, complexState, 1); }  } else { this._rangeEncoder.Encode(this._isRepG0, this._state, 1); if (pos == 1) { this._rangeEncoder.Encode(this._isRepG1, this._state, 0); } else { this._rangeEncoder.Encode(this._isRepG1, this._state, 1); this._rangeEncoder.Encode(this._isRepG2, this._state, pos - 2); }  }  if (len == 1) { this._state = Base.StateUpdateShortRep(this._state); } else { this._repMatchLenEncoder.Encode(this._rangeEncoder, len - 2, posState); this._state = Base.StateUpdateRep(this._state); }  int distance = this._repDistances[pos]; if (pos != 0) { for (int i = pos; i >= 1; i--) this._repDistances[i] = this._repDistances[i - 1];  this._repDistances[0] = distance; }  } else { this._rangeEncoder.Encode(this._isRep, this._state, 0); this._state = Base.StateUpdateMatch(this._state); this._lenEncoder.Encode(this._rangeEncoder, len - 2, posState); pos -= 4; int posSlot = GetPosSlot(pos); int lenToPosState = Base.GetLenToPosState(len); this._posSlotEncoder[lenToPosState].Encode(this._rangeEncoder, posSlot); if (posSlot >= 4) { int footerBits = (posSlot >> 1) - 1; int baseVal = (0x2 | posSlot & 0x1) << footerBits; int posReduced = pos - baseVal; if (posSlot < 14) { BitTreeEncoder.ReverseEncode(this._posEncoders, baseVal - posSlot - 1, this._rangeEncoder, footerBits, posReduced); } else { this._rangeEncoder.EncodeDirectBits(posReduced >> 4, footerBits - 4); this._posAlignEncoder.ReverseEncode(this._rangeEncoder, posReduced & 0xF); this._alignPriceCount++; }  }  int distance = pos; for (int i = 3; i >= 1; i--) this._repDistances[i] = this._repDistances[i - 1];  this._repDistances[0] = distance; this._matchPriceCount++; }  this._previousByte = this._matchFinder.GetIndexByte(len - 1 - this._additionalOffset); }  this._additionalOffset -= len; this.nowPos64 += len; if (this._additionalOffset == 0) { if (this._matchPriceCount >= 128) FillDistancesPrices();  if (this._alignPriceCount >= 16) FillAlignPrices();  inSize[0] = this.nowPos64; outSize[0] = this._rangeEncoder.GetProcessedSizeAdd(); if (this._matchFinder.GetNumAvailableBytes() == 0) { Flush((int)this.nowPos64); return; }  if (this.nowPos64 - progressPosValuePrev >= 4096L) { this._finished = false; finished[0] = false; return; }  }  }  } void ReleaseMFStream() { if (this._matchFinder != null && this._needReleaseMFStream) { this._matchFinder.ReleaseStream(); this._needReleaseMFStream = false; }  } void FillDistancesPrices() { for (int i = 4; i < 128; i++) {
/* 1159 */       int posSlot = GetPosSlot(i);
/* 1160 */       int footerBits = (posSlot >> 1) - 1;
/* 1161 */       int baseVal = (0x2 | posSlot & 0x1) << footerBits;
/* 1162 */       this.tempPrices[i] = BitTreeEncoder.ReverseGetPrice(this._posEncoders, baseVal - posSlot - 1, footerBits, i - baseVal);
/*      */     } 
/*      */     
/* 1165 */     for (int lenToPosState = 0; lenToPosState < 4; lenToPosState++) {
/*      */       
/* 1167 */       BitTreeEncoder encoder = this._posSlotEncoder[lenToPosState];
/*      */       
/* 1169 */       int st = lenToPosState << 6; int posSlot;
/* 1170 */       for (posSlot = 0; posSlot < this._distTableSize; posSlot++)
/* 1171 */         this._posSlotPrices[st + posSlot] = encoder.GetPrice(posSlot); 
/* 1172 */       for (posSlot = 14; posSlot < this._distTableSize; posSlot++) {
/* 1173 */         this._posSlotPrices[st + posSlot] = this._posSlotPrices[st + posSlot] + ((posSlot >> 1) - 1 - 4 << 6);
/*      */       }
/* 1175 */       int st2 = lenToPosState * 128;
/*      */       int j;
/* 1177 */       for (j = 0; j < 4; j++)
/* 1178 */         this._distancesPrices[st2 + j] = this._posSlotPrices[st + j]; 
/* 1179 */       for (; j < 128; j++)
/* 1180 */         this._distancesPrices[st2 + j] = this._posSlotPrices[st + GetPosSlot(j)] + this.tempPrices[j]; 
/*      */     } 
/* 1182 */     this._matchPriceCount = 0; }
/*      */   void SetOutStream(OutputStream outStream) { this._rangeEncoder.SetStream(outStream); }
/*      */   void ReleaseOutStream() { this._rangeEncoder.ReleaseStream(); }
/*      */   void ReleaseStreams() { ReleaseMFStream(); ReleaseOutStream(); }
/* 1186 */   void SetStreams(InputStream inStream, OutputStream outStream, long inSize, long outSize) { this._inStream = inStream; this._finished = false; Create(); SetOutStream(outStream); Init(); FillDistancesPrices(); FillAlignPrices(); this._lenEncoder.SetTableSize(this._numFastBytes + 1 - 2); this._lenEncoder.UpdateTables(1 << this._posStateBits); this._repMatchLenEncoder.SetTableSize(this._numFastBytes + 1 - 2); this._repMatchLenEncoder.UpdateTables(1 << this._posStateBits); this.nowPos64 = 0L; } public void Code(InputStream inStream, OutputStream outStream, long inSize, long outSize, ICodeProgress progress) throws IOException { this._needReleaseMFStream = false; try { SetStreams(inStream, outStream, inSize, outSize); while (true) { CodeOneBlock(this.processedInSize, this.processedOutSize, this.finished); if (this.finished[0]) return;  if (progress != null) progress.SetProgress(this.processedInSize[0], this.processedOutSize[0]);  }  } finally { ReleaseStreams(); }  } public void WriteCoderProperties(OutputStream outStream) throws IOException { this.properties[0] = (byte)((this._posStateBits * 5 + this._numLiteralPosStateBits) * 9 + this._numLiteralContextBits); for (int i = 0; i < 4; i++) this.properties[1 + i] = (byte)(this._dictionarySize >> 8 * i);  outStream.write(this.properties, 0, 5); } void FillAlignPrices() { for (int i = 0; i < 16; i++)
/* 1187 */       this._alignPrices[i] = this._posAlignEncoder.ReverseGetPrice(i); 
/* 1188 */     this._alignPriceCount = 0; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean SetAlgorithm(int algorithm) {
/* 1195 */     return true;
/*      */   }
/*      */   
/*      */   public boolean SetDictionarySize(int dictionarySize) {
/* 1199 */     int kDicLogSizeMaxCompress = 29;
/* 1200 */     if (dictionarySize < 1 || dictionarySize > 1 << kDicLogSizeMaxCompress) return false; 
/* 1201 */     this._dictionarySize = dictionarySize;
/*      */     int dicLogSize;
/* 1203 */     for (dicLogSize = 0; dictionarySize > 1 << dicLogSize; dicLogSize++);
/*      */     
/* 1205 */     this._distTableSize = dicLogSize * 2;
/* 1206 */     return true;
/*      */   }
/*      */   
/*      */   public boolean SetNumFastBytes(int numFastBytes) {
/* 1210 */     if (numFastBytes < 5 || numFastBytes > 273) return false; 
/* 1211 */     this._numFastBytes = numFastBytes;
/* 1212 */     return true;
/*      */   }
/*      */   
/*      */   public boolean SetMatchFinder(int matchFinderIndex) {
/* 1216 */     if (matchFinderIndex < 0 || matchFinderIndex > 2) return false; 
/* 1217 */     int matchFinderIndexPrev = this._matchFinderType;
/* 1218 */     this._matchFinderType = matchFinderIndex;
/* 1219 */     if (this._matchFinder != null && matchFinderIndexPrev != this._matchFinderType) {
/* 1220 */       this._dictionarySizePrev = -1;
/* 1221 */       this._matchFinder = null;
/*      */     } 
/* 1223 */     return true;
/*      */   }
/*      */   
/*      */   public boolean SetLcLpPb(int lc, int lp, int pb) {
/* 1227 */     if (lp < 0 || lp > 4 || lc < 0 || lc > 8 || pb < 0 || pb > 4)
/* 1228 */       return false; 
/* 1229 */     this._numLiteralPosStateBits = lp;
/* 1230 */     this._numLiteralContextBits = lc;
/* 1231 */     this._posStateBits = pb;
/* 1232 */     this._posStateMask = (1 << this._posStateBits) - 1;
/* 1233 */     return true;
/*      */   }
/*      */   
/*      */   public void SetEndMarkerMode(boolean endMarkerMode) {
/* 1237 */     this._writeEndMark = endMarkerMode;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\lzma\Encoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */