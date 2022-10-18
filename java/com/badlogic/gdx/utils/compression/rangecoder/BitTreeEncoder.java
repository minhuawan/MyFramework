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
/*    */ 
/*    */ 
/*    */ public class BitTreeEncoder
/*    */ {
/*    */   short[] Models;
/*    */   int NumBitLevels;
/*    */   
/*    */   public BitTreeEncoder(int numBitLevels) {
/* 26 */     this.NumBitLevels = numBitLevels;
/* 27 */     this.Models = new short[1 << numBitLevels];
/*    */   }
/*    */   
/*    */   public void Init() {
/* 31 */     Decoder.InitBitModels(this.Models);
/*    */   }
/*    */   
/*    */   public void Encode(Encoder rangeEncoder, int symbol) throws IOException {
/* 35 */     int m = 1;
/* 36 */     for (int bitIndex = this.NumBitLevels; bitIndex != 0; ) {
/* 37 */       bitIndex--;
/* 38 */       int bit = symbol >>> bitIndex & 0x1;
/* 39 */       rangeEncoder.Encode(this.Models, m, bit);
/* 40 */       m = m << 1 | bit;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void ReverseEncode(Encoder rangeEncoder, int symbol) throws IOException {
/* 45 */     int m = 1;
/* 46 */     for (int i = 0; i < this.NumBitLevels; i++) {
/* 47 */       int bit = symbol & 0x1;
/* 48 */       rangeEncoder.Encode(this.Models, m, bit);
/* 49 */       m = m << 1 | bit;
/* 50 */       symbol >>= 1;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int GetPrice(int symbol) {
/* 55 */     int price = 0;
/* 56 */     int m = 1;
/* 57 */     for (int bitIndex = this.NumBitLevels; bitIndex != 0; ) {
/* 58 */       bitIndex--;
/* 59 */       int bit = symbol >>> bitIndex & 0x1;
/* 60 */       price += Encoder.GetPrice(this.Models[m], bit);
/* 61 */       m = (m << 1) + bit;
/*    */     } 
/* 63 */     return price;
/*    */   }
/*    */   
/*    */   public int ReverseGetPrice(int symbol) {
/* 67 */     int price = 0;
/* 68 */     int m = 1;
/* 69 */     for (int i = this.NumBitLevels; i != 0; i--) {
/* 70 */       int bit = symbol & 0x1;
/* 71 */       symbol >>>= 1;
/* 72 */       price += Encoder.GetPrice(this.Models[m], bit);
/* 73 */       m = m << 1 | bit;
/*    */     } 
/* 75 */     return price;
/*    */   }
/*    */   
/*    */   public static int ReverseGetPrice(short[] Models, int startIndex, int NumBitLevels, int symbol) {
/* 79 */     int price = 0;
/* 80 */     int m = 1;
/* 81 */     for (int i = NumBitLevels; i != 0; i--) {
/* 82 */       int bit = symbol & 0x1;
/* 83 */       symbol >>>= 1;
/* 84 */       price += Encoder.GetPrice(Models[startIndex + m], bit);
/* 85 */       m = m << 1 | bit;
/*    */     } 
/* 87 */     return price;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void ReverseEncode(short[] Models, int startIndex, Encoder rangeEncoder, int NumBitLevels, int symbol) throws IOException {
/* 92 */     int m = 1;
/* 93 */     for (int i = 0; i < NumBitLevels; i++) {
/* 94 */       int bit = symbol & 0x1;
/* 95 */       rangeEncoder.Encode(Models, startIndex + m, bit);
/* 96 */       m = m << 1 | bit;
/* 97 */       symbol >>= 1;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\rangecoder\BitTreeEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */