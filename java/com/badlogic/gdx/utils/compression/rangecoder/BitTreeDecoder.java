/*    */ package com.badlogic.gdx.utils.compression.rangecoder;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class BitTreeDecoder
/*    */ {
/*    */   short[] Models;
/*    */   int NumBitLevels;
/*    */   
/*    */   public BitTreeDecoder(int numBitLevels) {
/* 24 */     this.NumBitLevels = numBitLevels;
/* 25 */     this.Models = new short[1 << numBitLevels];
/*    */   }
/*    */   
/*    */   public void Init() {
/* 29 */     Decoder.InitBitModels(this.Models);
/*    */   }
/*    */   
/*    */   public int Decode(Decoder rangeDecoder) throws IOException {
/* 33 */     int m = 1;
/* 34 */     for (int bitIndex = this.NumBitLevels; bitIndex != 0; bitIndex--)
/* 35 */       m = (m << 1) + rangeDecoder.DecodeBit(this.Models, m); 
/* 36 */     return m - (1 << this.NumBitLevels);
/*    */   }
/*    */   
/*    */   public int ReverseDecode(Decoder rangeDecoder) throws IOException {
/* 40 */     int m = 1;
/* 41 */     int symbol = 0;
/* 42 */     for (int bitIndex = 0; bitIndex < this.NumBitLevels; bitIndex++) {
/* 43 */       int bit = rangeDecoder.DecodeBit(this.Models, m);
/* 44 */       m <<= 1;
/* 45 */       m += bit;
/* 46 */       symbol |= bit << bitIndex;
/*    */     } 
/* 48 */     return symbol;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int ReverseDecode(short[] Models, int startIndex, Decoder rangeDecoder, int NumBitLevels) throws IOException {
/* 53 */     int m = 1;
/* 54 */     int symbol = 0;
/* 55 */     for (int bitIndex = 0; bitIndex < NumBitLevels; bitIndex++) {
/* 56 */       int bit = rangeDecoder.DecodeBit(Models, startIndex + m);
/* 57 */       m <<= 1;
/* 58 */       m += bit;
/* 59 */       symbol |= bit << bitIndex;
/*    */     } 
/* 61 */     return symbol;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\rangecoder\BitTreeDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */