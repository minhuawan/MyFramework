/*    */ package com.badlogic.gdx.utils.compression.rangecoder;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Decoder
/*    */ {
/*    */   static final int kTopMask = -16777216;
/*    */   static final int kNumBitModelTotalBits = 11;
/*    */   static final int kBitModelTotal = 2048;
/*    */   static final int kNumMoveBits = 5;
/*    */   int Range;
/*    */   int Code;
/*    */   InputStream Stream;
/*    */   
/*    */   public final void SetStream(InputStream stream) {
/* 34 */     this.Stream = stream;
/*    */   }
/*    */   
/*    */   public final void ReleaseStream() {
/* 38 */     this.Stream = null;
/*    */   }
/*    */   
/*    */   public final void Init() throws IOException {
/* 42 */     this.Code = 0;
/* 43 */     this.Range = -1;
/* 44 */     for (int i = 0; i < 5; i++)
/* 45 */       this.Code = this.Code << 8 | this.Stream.read(); 
/*    */   }
/*    */   
/*    */   public final int DecodeDirectBits(int numTotalBits) throws IOException {
/* 49 */     int result = 0;
/* 50 */     for (int i = numTotalBits; i != 0; i--) {
/* 51 */       this.Range >>>= 1;
/* 52 */       int t = this.Code - this.Range >>> 31;
/* 53 */       this.Code -= this.Range & t - 1;
/* 54 */       result = result << 1 | 1 - t;
/*    */       
/* 56 */       if ((this.Range & 0xFF000000) == 0) {
/* 57 */         this.Code = this.Code << 8 | this.Stream.read();
/* 58 */         this.Range <<= 8;
/*    */       } 
/*    */     } 
/* 61 */     return result;
/*    */   }
/*    */   
/*    */   public int DecodeBit(short[] probs, int index) throws IOException {
/* 65 */     int prob = probs[index];
/* 66 */     int newBound = (this.Range >>> 11) * prob;
/* 67 */     if ((this.Code ^ Integer.MIN_VALUE) < (newBound ^ Integer.MIN_VALUE)) {
/* 68 */       this.Range = newBound;
/* 69 */       probs[index] = (short)(prob + (2048 - prob >>> 5));
/* 70 */       if ((this.Range & 0xFF000000) == 0) {
/* 71 */         this.Code = this.Code << 8 | this.Stream.read();
/* 72 */         this.Range <<= 8;
/*    */       } 
/* 74 */       return 0;
/*    */     } 
/* 76 */     this.Range -= newBound;
/* 77 */     this.Code -= newBound;
/* 78 */     probs[index] = (short)(prob - (prob >>> 5));
/* 79 */     if ((this.Range & 0xFF000000) == 0) {
/* 80 */       this.Code = this.Code << 8 | this.Stream.read();
/* 81 */       this.Range <<= 8;
/*    */     } 
/* 83 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void InitBitModels(short[] probs) {
/* 88 */     for (int i = 0; i < probs.length; i++)
/* 89 */       probs[i] = 1024; 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\rangecoder\Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */