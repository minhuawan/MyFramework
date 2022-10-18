/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public class DataInput
/*     */   extends DataInputStream
/*     */ {
/*  26 */   private char[] chars = new char[32];
/*     */   
/*     */   public DataInput(InputStream in) {
/*  29 */     super(in);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readInt(boolean optimizePositive) throws IOException {
/*  34 */     int b = read();
/*  35 */     int result = b & 0x7F;
/*  36 */     if ((b & 0x80) != 0) {
/*  37 */       b = read();
/*  38 */       result |= (b & 0x7F) << 7;
/*  39 */       if ((b & 0x80) != 0) {
/*  40 */         b = read();
/*  41 */         result |= (b & 0x7F) << 14;
/*  42 */         if ((b & 0x80) != 0) {
/*  43 */           b = read();
/*  44 */           result |= (b & 0x7F) << 21;
/*  45 */           if ((b & 0x80) != 0) {
/*  46 */             b = read();
/*  47 */             result |= (b & 0x7F) << 28;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  52 */     return optimizePositive ? result : (result >>> 1 ^ -(result & 0x1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String readString() throws IOException {
/*  58 */     int charCount = readInt(true);
/*  59 */     switch (charCount) {
/*     */       case 0:
/*  61 */         return null;
/*     */       case 1:
/*  63 */         return "";
/*     */     } 
/*  65 */     charCount--;
/*  66 */     if (this.chars.length < charCount) this.chars = new char[charCount]; 
/*  67 */     char[] chars = this.chars;
/*     */     
/*  69 */     int charIndex = 0;
/*  70 */     int b = 0;
/*  71 */     while (charIndex < charCount) {
/*  72 */       b = read();
/*  73 */       if (b > 127)
/*  74 */         break;  chars[charIndex++] = (char)b;
/*     */     } 
/*     */     
/*  77 */     if (charIndex < charCount) readUtf8_slow(charCount, charIndex, b); 
/*  78 */     return new String(chars, 0, charCount);
/*     */   }
/*     */   
/*     */   private void readUtf8_slow(int charCount, int charIndex, int b) throws IOException {
/*  82 */     char[] chars = this.chars;
/*     */     while (true) {
/*  84 */       switch (b >> 4) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/*     */         case 6:
/*     */         case 7:
/*  93 */           chars[charIndex] = (char)b;
/*     */           break;
/*     */         case 12:
/*     */         case 13:
/*  97 */           chars[charIndex] = (char)((b & 0x1F) << 6 | read() & 0x3F);
/*     */           break;
/*     */         case 14:
/* 100 */           chars[charIndex] = (char)((b & 0xF) << 12 | (read() & 0x3F) << 6 | read() & 0x3F);
/*     */           break;
/*     */       } 
/* 103 */       if (++charIndex >= charCount)
/* 104 */         break;  b = read() & 0xFF;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\DataInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */