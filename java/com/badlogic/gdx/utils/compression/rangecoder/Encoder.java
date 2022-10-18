/*     */ package com.badlogic.gdx.utils.compression.rangecoder;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Encoder
/*     */ {
/*     */   static final int kTopMask = -16777216;
/*     */   static final int kNumBitModelTotalBits = 11;
/*     */   static final int kBitModelTotal = 2048;
/*     */   static final int kNumMoveBits = 5;
/*     */   OutputStream Stream;
/*     */   long Low;
/*     */   int Range;
/*     */   int _cacheSize;
/*     */   int _cache;
/*     */   long _position;
/*     */   static final int kNumMoveReducingBits = 2;
/*     */   public static final int kNumBitPriceShiftBits = 6;
/*     */   
/*     */   public void SetStream(OutputStream stream) {
/*  38 */     this.Stream = stream;
/*     */   }
/*     */   
/*     */   public void ReleaseStream() {
/*  42 */     this.Stream = null;
/*     */   }
/*     */   
/*     */   public void Init() {
/*  46 */     this._position = 0L;
/*  47 */     this.Low = 0L;
/*  48 */     this.Range = -1;
/*  49 */     this._cacheSize = 1;
/*  50 */     this._cache = 0;
/*     */   }
/*     */   
/*     */   public void FlushData() throws IOException {
/*  54 */     for (int i = 0; i < 5; i++)
/*  55 */       ShiftLow(); 
/*     */   }
/*     */   
/*     */   public void FlushStream() throws IOException {
/*  59 */     this.Stream.flush();
/*     */   }
/*     */   
/*     */   public void ShiftLow() throws IOException {
/*  63 */     int LowHi = (int)(this.Low >>> 32L);
/*  64 */     if (LowHi != 0 || this.Low < 4278190080L) {
/*  65 */       this._position += this._cacheSize;
/*  66 */       int temp = this._cache;
/*     */       do {
/*  68 */         this.Stream.write(temp + LowHi);
/*  69 */         temp = 255;
/*  70 */       } while (--this._cacheSize != 0);
/*  71 */       this._cache = (int)this.Low >>> 24;
/*     */     } 
/*  73 */     this._cacheSize++;
/*  74 */     this.Low = (this.Low & 0xFFFFFFL) << 8L;
/*     */   }
/*     */   
/*     */   public void EncodeDirectBits(int v, int numTotalBits) throws IOException {
/*  78 */     for (int i = numTotalBits - 1; i >= 0; i--) {
/*  79 */       this.Range >>>= 1;
/*  80 */       if ((v >>> i & 0x1) == 1) this.Low += this.Range; 
/*  81 */       if ((this.Range & 0xFF000000) == 0) {
/*  82 */         this.Range <<= 8;
/*  83 */         ShiftLow();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public long GetProcessedSizeAdd() {
/*  89 */     return this._cacheSize + this._position + 4L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void InitBitModels(short[] probs) {
/*  96 */     for (int i = 0; i < probs.length; i++)
/*  97 */       probs[i] = 1024; 
/*     */   }
/*     */   
/*     */   public void Encode(short[] probs, int index, int symbol) throws IOException {
/* 101 */     int prob = probs[index];
/* 102 */     int newBound = (this.Range >>> 11) * prob;
/* 103 */     if (symbol == 0) {
/* 104 */       this.Range = newBound;
/* 105 */       probs[index] = (short)(prob + (2048 - prob >>> 5));
/*     */     } else {
/* 107 */       this.Low += newBound & 0xFFFFFFFFL;
/* 108 */       this.Range -= newBound;
/* 109 */       probs[index] = (short)(prob - (prob >>> 5));
/*     */     } 
/* 111 */     if ((this.Range & 0xFF000000) == 0) {
/* 112 */       this.Range <<= 8;
/* 113 */       ShiftLow();
/*     */     } 
/*     */   }
/*     */   
/* 117 */   private static int[] ProbPrices = new int[512];
/*     */   
/*     */   static {
/* 120 */     int kNumBits = 9;
/* 121 */     for (int i = kNumBits - 1; i >= 0; i--) {
/* 122 */       int start = 1 << kNumBits - i - 1;
/* 123 */       int end = 1 << kNumBits - i;
/* 124 */       for (int j = start; j < end; j++)
/* 125 */         ProbPrices[j] = (i << 6) + (end - j << 6 >>> kNumBits - i - 1); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int GetPrice(int Prob, int symbol) {
/* 130 */     return ProbPrices[((Prob - symbol ^ -symbol) & 0x7FF) >>> 2];
/*     */   }
/*     */   
/*     */   public static int GetPrice0(int Prob) {
/* 134 */     return ProbPrices[Prob >>> 2];
/*     */   }
/*     */   
/*     */   public static int GetPrice1(int Prob) {
/* 138 */     return ProbPrices[2048 - Prob >>> 2];
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\rangecoder\Encoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */