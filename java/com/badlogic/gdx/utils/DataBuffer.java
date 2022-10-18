/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataBuffer
/*    */   extends DataOutput
/*    */ {
/*    */   private final StreamUtils.OptimizedByteArrayOutputStream outStream;
/*    */   
/*    */   public DataBuffer() {
/* 12 */     this(32);
/*    */   }
/*    */   
/*    */   public DataBuffer(int initialSize) {
/* 16 */     super(new StreamUtils.OptimizedByteArrayOutputStream(initialSize));
/* 17 */     this.outStream = (StreamUtils.OptimizedByteArrayOutputStream)this.out;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getBuffer() {
/* 22 */     return this.outStream.getBuffer();
/*    */   }
/*    */   
/*    */   public byte[] toArray() {
/* 26 */     return this.outStream.toByteArray();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\DataBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */