/*    */ package com.badlogic.gdx.utils.compression;
/*    */ 
/*    */ 
/*    */ public class CRC
/*    */ {
/*  6 */   public static int[] Table = new int[256];
/*    */   
/*    */   static {
/*  9 */     for (int i = 0; i < 256; i++) {
/* 10 */       int r = i;
/* 11 */       for (int j = 0; j < 8; j++) {
/* 12 */         if ((r & 0x1) != 0)
/* 13 */         { r = r >>> 1 ^ 0xEDB88320; }
/*    */         else
/* 15 */         { r >>>= 1; } 
/* 16 */       }  Table[i] = r;
/*    */     } 
/*    */   }
/*    */   
/* 20 */   int _value = -1;
/*    */   
/*    */   public void Init() {
/* 23 */     this._value = -1;
/*    */   }
/*    */   
/*    */   public void Update(byte[] data, int offset, int size) {
/* 27 */     for (int i = 0; i < size; i++)
/* 28 */       this._value = Table[(this._value ^ data[offset + i]) & 0xFF] ^ this._value >>> 8; 
/*    */   }
/*    */   
/*    */   public void Update(byte[] data) {
/* 32 */     int size = data.length;
/* 33 */     for (int i = 0; i < size; i++)
/* 34 */       this._value = Table[(this._value ^ data[i]) & 0xFF] ^ this._value >>> 8; 
/*    */   }
/*    */   
/*    */   public void UpdateByte(int b) {
/* 38 */     this._value = Table[(this._value ^ b) & 0xFF] ^ this._value >>> 8;
/*    */   }
/*    */   
/*    */   public int GetDigest() {
/* 42 */     return this._value ^ 0xFFFFFFFF;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\CRC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */