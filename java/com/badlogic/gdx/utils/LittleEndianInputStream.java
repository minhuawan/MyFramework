/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FilterInputStream;
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
/*     */ public class LittleEndianInputStream
/*     */   extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*     */   private DataInputStream din;
/*     */   
/*     */   public LittleEndianInputStream(InputStream in) {
/*  32 */     super(in);
/*  33 */     this.din = new DataInputStream(in);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b) throws IOException {
/*  37 */     this.din.readFully(b);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException {
/*  41 */     this.din.readFully(b, off, len);
/*     */   }
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/*  45 */     return this.din.skipBytes(n);
/*     */   }
/*     */   
/*     */   public boolean readBoolean() throws IOException {
/*  49 */     return this.din.readBoolean();
/*     */   }
/*     */   
/*     */   public byte readByte() throws IOException {
/*  53 */     return this.din.readByte();
/*     */   }
/*     */   
/*     */   public int readUnsignedByte() throws IOException {
/*  57 */     return this.din.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public short readShort() throws IOException {
/*  61 */     int low = this.din.read();
/*  62 */     int high = this.din.read();
/*  63 */     return (short)(high << 8 | low & 0xFF);
/*     */   }
/*     */   
/*     */   public int readUnsignedShort() throws IOException {
/*  67 */     int low = this.din.read();
/*  68 */     int high = this.din.read();
/*  69 */     return (high & 0xFF) << 8 | low & 0xFF;
/*     */   }
/*     */   
/*     */   public char readChar() throws IOException {
/*  73 */     return this.din.readChar();
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException {
/*  77 */     int[] res = new int[4];
/*  78 */     for (int i = 3; i >= 0; i--) {
/*  79 */       res[i] = this.din.read();
/*     */     }
/*  81 */     return (res[0] & 0xFF) << 24 | (res[1] & 0xFF) << 16 | (res[2] & 0xFF) << 8 | res[3] & 0xFF;
/*     */   }
/*     */   
/*     */   public long readLong() throws IOException {
/*  85 */     int[] res = new int[8];
/*  86 */     for (int i = 7; i >= 0; i--) {
/*  87 */       res[i] = this.din.read();
/*     */     }
/*  89 */     return (res[0] & 0xFF) << 56L | (res[1] & 0xFF) << 48L | (res[2] & 0xFF) << 40L | (res[3] & 0xFF) << 32L | (res[4] & 0xFF) << 24L | (res[5] & 0xFF) << 16L | (res[6] & 0xFF) << 8L | (res[7] & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float readFloat() throws IOException {
/*  95 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */   
/*     */   public double readDouble() throws IOException {
/*  99 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */   
/*     */   public final String readLine() throws IOException {
/* 103 */     return this.din.readLine();
/*     */   }
/*     */   
/*     */   public String readUTF() throws IOException {
/* 107 */     return this.din.readUTF();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\LittleEndianInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */