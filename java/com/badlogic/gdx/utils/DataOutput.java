/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public class DataOutput
/*    */   extends DataOutputStream
/*    */ {
/*    */   public DataOutput(OutputStream out) {
/* 28 */     super(out);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int writeInt(int value, boolean optimizePositive) throws IOException {
/* 35 */     if (!optimizePositive) value = value << 1 ^ value >> 31; 
/* 36 */     if (value >>> 7 == 0) {
/* 37 */       write((byte)value);
/* 38 */       return 1;
/*    */     } 
/* 40 */     write((byte)(value & 0x7F | 0x80));
/* 41 */     if (value >>> 14 == 0) {
/* 42 */       write((byte)(value >>> 7));
/* 43 */       return 2;
/*    */     } 
/* 45 */     write((byte)(value >>> 7 | 0x80));
/* 46 */     if (value >>> 21 == 0) {
/* 47 */       write((byte)(value >>> 14));
/* 48 */       return 3;
/*    */     } 
/* 50 */     write((byte)(value >>> 14 | 0x80));
/* 51 */     if (value >>> 28 == 0) {
/* 52 */       write((byte)(value >>> 21));
/* 53 */       return 4;
/*    */     } 
/* 55 */     write((byte)(value >>> 21 | 0x80));
/* 56 */     write((byte)(value >>> 28));
/* 57 */     return 5;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeString(String value) throws IOException {
/* 63 */     if (value == null) {
/* 64 */       write(0);
/*    */       return;
/*    */     } 
/* 67 */     int charCount = value.length();
/* 68 */     if (charCount == 0) {
/* 69 */       writeByte(1);
/*    */       return;
/*    */     } 
/* 72 */     writeInt(charCount + 1, true);
/*    */     
/* 74 */     int charIndex = 0;
/* 75 */     for (; charIndex < charCount; charIndex++) {
/* 76 */       int c = value.charAt(charIndex);
/* 77 */       if (c > 127)
/* 78 */         break;  write((byte)c);
/*    */     } 
/* 80 */     if (charIndex < charCount) writeString_slow(value, charCount, charIndex); 
/*    */   }
/*    */   
/*    */   private void writeString_slow(String value, int charCount, int charIndex) throws IOException {
/* 84 */     for (; charIndex < charCount; charIndex++) {
/* 85 */       int c = value.charAt(charIndex);
/* 86 */       if (c <= 127) {
/* 87 */         write((byte)c);
/* 88 */       } else if (c > 2047) {
/* 89 */         write((byte)(0xE0 | c >> 12 & 0xF));
/* 90 */         write((byte)(0x80 | c >> 6 & 0x3F));
/* 91 */         write((byte)(0x80 | c & 0x3F));
/*    */       } else {
/* 93 */         write((byte)(0xC0 | c >> 6 & 0x1F));
/* 94 */         write((byte)(0x80 | c & 0x3F));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\DataOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */