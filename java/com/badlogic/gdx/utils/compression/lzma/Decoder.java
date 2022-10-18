/*     */ package com.badlogic.gdx.utils.compression.lzma;
/*     */ 
/*     */ import com.badlogic.gdx.utils.compression.lz.OutWindow;
/*     */ import com.badlogic.gdx.utils.compression.rangecoder.BitTreeDecoder;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Decoder
/*     */ {
/*     */   class LenDecoder
/*     */   {
/*  26 */     short[] m_Choice = new short[2];
/*  27 */     BitTreeDecoder[] m_LowCoder = new BitTreeDecoder[16];
/*  28 */     BitTreeDecoder[] m_MidCoder = new BitTreeDecoder[16];
/*  29 */     BitTreeDecoder m_HighCoder = new BitTreeDecoder(8);
/*  30 */     int m_NumPosStates = 0;
/*     */     
/*     */     public void Create(int numPosStates) {
/*  33 */       for (; this.m_NumPosStates < numPosStates; this.m_NumPosStates++) {
/*  34 */         this.m_LowCoder[this.m_NumPosStates] = new BitTreeDecoder(3);
/*  35 */         this.m_MidCoder[this.m_NumPosStates] = new BitTreeDecoder(3);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void Init() {
/*  40 */       com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_Choice);
/*  41 */       for (int posState = 0; posState < this.m_NumPosStates; posState++) {
/*  42 */         this.m_LowCoder[posState].Init();
/*  43 */         this.m_MidCoder[posState].Init();
/*     */       } 
/*  45 */       this.m_HighCoder.Init();
/*     */     }
/*     */     
/*     */     public int Decode(com.badlogic.gdx.utils.compression.rangecoder.Decoder rangeDecoder, int posState) throws IOException {
/*  49 */       if (rangeDecoder.DecodeBit(this.m_Choice, 0) == 0) return this.m_LowCoder[posState].Decode(rangeDecoder); 
/*  50 */       int symbol = 8;
/*  51 */       if (rangeDecoder.DecodeBit(this.m_Choice, 1) == 0) {
/*  52 */         symbol += this.m_MidCoder[posState].Decode(rangeDecoder);
/*     */       } else {
/*  54 */         symbol += 8 + this.m_HighCoder.Decode(rangeDecoder);
/*  55 */       }  return symbol;
/*     */     } }
/*     */   class LiteralDecoder { Decoder2[] m_Coders; int m_NumPrevBits;
/*     */     int m_NumPosBits;
/*     */     int m_PosMask;
/*     */     
/*  61 */     class Decoder2 { short[] m_Decoders = new short[768];
/*     */       
/*     */       public void Init() {
/*  64 */         com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_Decoders);
/*     */       }
/*     */       
/*     */       public byte DecodeNormal(com.badlogic.gdx.utils.compression.rangecoder.Decoder rangeDecoder) throws IOException {
/*  68 */         int symbol = 1;
/*     */         while (true) {
/*  70 */           symbol = symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol);
/*  71 */           if (symbol >= 256)
/*  72 */             return (byte)symbol; 
/*     */         } 
/*     */       }
/*     */       
/*     */       public byte DecodeWithMatchByte(com.badlogic.gdx.utils.compression.rangecoder.Decoder rangeDecoder, byte matchByte) throws IOException {
/*  77 */         int symbol = 1;
/*     */         do {
/*  79 */           int matchBit = matchByte >> 7 & 0x1;
/*  80 */           matchByte = (byte)(matchByte << 1);
/*  81 */           int bit = rangeDecoder.DecodeBit(this.m_Decoders, (1 + matchBit << 8) + symbol);
/*  82 */           symbol = symbol << 1 | bit;
/*  83 */           if (matchBit != bit) {
/*  84 */             while (symbol < 256)
/*  85 */               symbol = symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol); 
/*     */             break;
/*     */           } 
/*  88 */         } while (symbol < 256);
/*  89 */         return (byte)symbol;
/*     */       } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void Create(int numPosBits, int numPrevBits) {
/*  99 */       if (this.m_Coders != null && this.m_NumPrevBits == numPrevBits && this.m_NumPosBits == numPosBits)
/* 100 */         return;  this.m_NumPosBits = numPosBits;
/* 101 */       this.m_PosMask = (1 << numPosBits) - 1;
/* 102 */       this.m_NumPrevBits = numPrevBits;
/* 103 */       int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits;
/* 104 */       this.m_Coders = new Decoder2[numStates];
/* 105 */       for (int i = 0; i < numStates; i++)
/* 106 */         this.m_Coders[i] = new Decoder2(); 
/*     */     }
/*     */     
/*     */     public void Init() {
/* 110 */       int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits;
/* 111 */       for (int i = 0; i < numStates; i++)
/* 112 */         this.m_Coders[i].Init(); 
/*     */     }
/*     */     
/*     */     Decoder2 GetDecoder(int pos, byte prevByte) {
/* 116 */       return this.m_Coders[((pos & this.m_PosMask) << this.m_NumPrevBits) + ((prevByte & 0xFF) >>> 8 - this.m_NumPrevBits)];
/*     */     } }
/*     */ 
/*     */   
/* 120 */   OutWindow m_OutWindow = new OutWindow(); class Decoder2 {
/* 121 */     short[] m_Decoders; Decoder2() { this.m_Decoders = new short[768]; } public void Init() { com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_Decoders); } public byte DecodeNormal(com.badlogic.gdx.utils.compression.rangecoder.Decoder rangeDecoder) throws IOException { int symbol = 1; while (true) { symbol = symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol); if (symbol >= 256) return (byte)symbol;  }  } public byte DecodeWithMatchByte(com.badlogic.gdx.utils.compression.rangecoder.Decoder rangeDecoder, byte matchByte) throws IOException { int symbol = 1; do { int matchBit = matchByte >> 7 & 0x1; matchByte = (byte)(matchByte << 1); int bit = rangeDecoder.DecodeBit(this.m_Decoders, (1 + matchBit << 8) + symbol); symbol = symbol << 1 | bit; if (matchBit != bit) { while (symbol < 256) symbol = symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol);  break; }  } while (symbol < 256); return (byte)symbol; } } com.badlogic.gdx.utils.compression.rangecoder.Decoder m_RangeDecoder = new com.badlogic.gdx.utils.compression.rangecoder.Decoder();
/*     */   
/* 123 */   short[] m_IsMatchDecoders = new short[192];
/* 124 */   short[] m_IsRepDecoders = new short[12];
/* 125 */   short[] m_IsRepG0Decoders = new short[12];
/* 126 */   short[] m_IsRepG1Decoders = new short[12];
/* 127 */   short[] m_IsRepG2Decoders = new short[12];
/* 128 */   short[] m_IsRep0LongDecoders = new short[192];
/*     */   
/* 130 */   BitTreeDecoder[] m_PosSlotDecoder = new BitTreeDecoder[4];
/* 131 */   short[] m_PosDecoders = new short[114];
/*     */   
/* 133 */   BitTreeDecoder m_PosAlignDecoder = new BitTreeDecoder(4);
/*     */   
/* 135 */   LenDecoder m_LenDecoder = new LenDecoder();
/* 136 */   LenDecoder m_RepLenDecoder = new LenDecoder();
/*     */   
/* 138 */   LiteralDecoder m_LiteralDecoder = new LiteralDecoder();
/*     */   
/* 140 */   int m_DictionarySize = -1;
/* 141 */   int m_DictionarySizeCheck = -1;
/*     */   
/*     */   int m_PosStateMask;
/*     */   
/*     */   public Decoder() {
/* 146 */     for (int i = 0; i < 4; i++)
/* 147 */       this.m_PosSlotDecoder[i] = new BitTreeDecoder(6); 
/*     */   }
/*     */   
/*     */   boolean SetDictionarySize(int dictionarySize) {
/* 151 */     if (dictionarySize < 0) return false; 
/* 152 */     if (this.m_DictionarySize != dictionarySize) {
/* 153 */       this.m_DictionarySize = dictionarySize;
/* 154 */       this.m_DictionarySizeCheck = Math.max(this.m_DictionarySize, 1);
/* 155 */       this.m_OutWindow.Create(Math.max(this.m_DictionarySizeCheck, 4096));
/*     */     } 
/* 157 */     return true;
/*     */   }
/*     */   
/*     */   boolean SetLcLpPb(int lc, int lp, int pb) {
/* 161 */     if (lc > 8 || lp > 4 || pb > 4) return false; 
/* 162 */     this.m_LiteralDecoder.Create(lp, lc);
/* 163 */     int numPosStates = 1 << pb;
/* 164 */     this.m_LenDecoder.Create(numPosStates);
/* 165 */     this.m_RepLenDecoder.Create(numPosStates);
/* 166 */     this.m_PosStateMask = numPosStates - 1;
/* 167 */     return true;
/*     */   }
/*     */   
/*     */   void Init() throws IOException {
/* 171 */     this.m_OutWindow.Init(false);
/*     */     
/* 173 */     com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_IsMatchDecoders);
/* 174 */     com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_IsRep0LongDecoders);
/* 175 */     com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_IsRepDecoders);
/* 176 */     com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_IsRepG0Decoders);
/* 177 */     com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_IsRepG1Decoders);
/* 178 */     com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_IsRepG2Decoders);
/* 179 */     com.badlogic.gdx.utils.compression.rangecoder.Decoder.InitBitModels(this.m_PosDecoders);
/*     */     
/* 181 */     this.m_LiteralDecoder.Init();
/*     */     
/* 183 */     for (int i = 0; i < 4; i++)
/* 184 */       this.m_PosSlotDecoder[i].Init(); 
/* 185 */     this.m_LenDecoder.Init();
/* 186 */     this.m_RepLenDecoder.Init();
/* 187 */     this.m_PosAlignDecoder.Init();
/* 188 */     this.m_RangeDecoder.Init();
/*     */   }
/*     */   
/*     */   public boolean Code(InputStream inStream, OutputStream outStream, long outSize) throws IOException {
/* 192 */     this.m_RangeDecoder.SetStream(inStream);
/* 193 */     this.m_OutWindow.SetStream(outStream);
/* 194 */     Init();
/*     */     
/* 196 */     int state = Base.StateInit();
/* 197 */     int rep0 = 0, rep1 = 0, rep2 = 0, rep3 = 0;
/*     */     
/* 199 */     long nowPos64 = 0L;
/* 200 */     byte prevByte = 0;
/* 201 */     while (outSize < 0L || nowPos64 < outSize) {
/* 202 */       int len, posState = (int)nowPos64 & this.m_PosStateMask;
/* 203 */       if (this.m_RangeDecoder.DecodeBit(this.m_IsMatchDecoders, (state << 4) + posState) == 0) {
/* 204 */         LiteralDecoder.Decoder2 decoder2 = this.m_LiteralDecoder.GetDecoder((int)nowPos64, prevByte);
/* 205 */         if (!Base.StateIsCharState(state)) {
/* 206 */           prevByte = decoder2.DecodeWithMatchByte(this.m_RangeDecoder, this.m_OutWindow.GetByte(rep0));
/*     */         } else {
/* 208 */           prevByte = decoder2.DecodeNormal(this.m_RangeDecoder);
/* 209 */         }  this.m_OutWindow.PutByte(prevByte);
/* 210 */         state = Base.StateUpdateChar(state);
/* 211 */         nowPos64++;
/*     */         continue;
/*     */       } 
/* 214 */       if (this.m_RangeDecoder.DecodeBit(this.m_IsRepDecoders, state) == 1) {
/* 215 */         len = 0;
/* 216 */         if (this.m_RangeDecoder.DecodeBit(this.m_IsRepG0Decoders, state) == 0) {
/* 217 */           if (this.m_RangeDecoder.DecodeBit(this.m_IsRep0LongDecoders, (state << 4) + posState) == 0) {
/* 218 */             state = Base.StateUpdateShortRep(state);
/* 219 */             len = 1;
/*     */           } 
/*     */         } else {
/*     */           int distance;
/* 223 */           if (this.m_RangeDecoder.DecodeBit(this.m_IsRepG1Decoders, state) == 0) {
/* 224 */             distance = rep1;
/*     */           } else {
/* 226 */             if (this.m_RangeDecoder.DecodeBit(this.m_IsRepG2Decoders, state) == 0) {
/* 227 */               distance = rep2;
/*     */             } else {
/* 229 */               distance = rep3;
/* 230 */               rep3 = rep2;
/*     */             } 
/* 232 */             rep2 = rep1;
/*     */           } 
/* 234 */           rep1 = rep0;
/* 235 */           rep0 = distance;
/*     */         } 
/* 237 */         if (len == 0) {
/* 238 */           len = this.m_RepLenDecoder.Decode(this.m_RangeDecoder, posState) + 2;
/* 239 */           state = Base.StateUpdateRep(state);
/*     */         } 
/*     */       } else {
/* 242 */         rep3 = rep2;
/* 243 */         rep2 = rep1;
/* 244 */         rep1 = rep0;
/* 245 */         len = 2 + this.m_LenDecoder.Decode(this.m_RangeDecoder, posState);
/* 246 */         state = Base.StateUpdateMatch(state);
/* 247 */         int posSlot = this.m_PosSlotDecoder[Base.GetLenToPosState(len)].Decode(this.m_RangeDecoder);
/* 248 */         if (posSlot >= 4) {
/* 249 */           int numDirectBits = (posSlot >> 1) - 1;
/* 250 */           rep0 = (0x2 | posSlot & 0x1) << numDirectBits;
/* 251 */           if (posSlot < 14) {
/* 252 */             rep0 += BitTreeDecoder.ReverseDecode(this.m_PosDecoders, rep0 - posSlot - 1, this.m_RangeDecoder, numDirectBits);
/*     */           } else {
/* 254 */             rep0 += this.m_RangeDecoder.DecodeDirectBits(numDirectBits - 4) << 4;
/* 255 */             rep0 += this.m_PosAlignDecoder.ReverseDecode(this.m_RangeDecoder);
/* 256 */             if (rep0 < 0) {
/* 257 */               if (rep0 == -1)
/* 258 */                 break;  return false;
/*     */             } 
/*     */           } 
/*     */         } else {
/* 262 */           rep0 = posSlot;
/*     */         } 
/* 264 */       }  if (rep0 >= nowPos64 || rep0 >= this.m_DictionarySizeCheck)
/*     */       {
/* 266 */         return false;
/*     */       }
/* 268 */       this.m_OutWindow.CopyBlock(rep0, len);
/* 269 */       nowPos64 += len;
/* 270 */       prevByte = this.m_OutWindow.GetByte(0);
/*     */     } 
/*     */     
/* 273 */     this.m_OutWindow.Flush();
/* 274 */     this.m_OutWindow.ReleaseStream();
/* 275 */     this.m_RangeDecoder.ReleaseStream();
/* 276 */     return true;
/*     */   }
/*     */   
/*     */   public boolean SetDecoderProperties(byte[] properties) {
/* 280 */     if (properties.length < 5) return false; 
/* 281 */     int val = properties[0] & 0xFF;
/* 282 */     int lc = val % 9;
/* 283 */     int remainder = val / 9;
/* 284 */     int lp = remainder % 5;
/* 285 */     int pb = remainder / 5;
/* 286 */     int dictionarySize = 0;
/* 287 */     for (int i = 0; i < 4; i++)
/* 288 */       dictionarySize += (properties[1 + i] & 0xFF) << i * 8; 
/* 289 */     if (!SetLcLpPb(lc, lp, pb)) return false; 
/* 290 */     return SetDictionarySize(dictionarySize);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\lzma\Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */