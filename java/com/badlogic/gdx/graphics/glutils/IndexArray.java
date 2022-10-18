/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ShortBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndexArray
/*     */   implements IndexData
/*     */ {
/*     */   final ShortBuffer buffer;
/*     */   final ByteBuffer byteBuffer;
/*     */   private final boolean empty;
/*     */   
/*     */   public IndexArray(int maxIndices) {
/*  36 */     this.empty = (maxIndices == 0);
/*  37 */     if (this.empty) {
/*  38 */       maxIndices = 1;
/*     */     }
/*     */     
/*  41 */     this.byteBuffer = BufferUtils.newUnsafeByteBuffer(maxIndices * 2);
/*  42 */     this.buffer = this.byteBuffer.asShortBuffer();
/*  43 */     this.buffer.flip();
/*  44 */     this.byteBuffer.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumIndices() {
/*  49 */     return this.empty ? 0 : this.buffer.limit();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumMaxIndices() {
/*  54 */     return this.empty ? 0 : this.buffer.capacity();
/*     */   }
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
/*     */   public void setIndices(short[] indices, int offset, int count) {
/*  70 */     this.buffer.clear();
/*  71 */     this.buffer.put(indices, offset, count);
/*  72 */     this.buffer.flip();
/*  73 */     this.byteBuffer.position(0);
/*  74 */     this.byteBuffer.limit(count << 1);
/*     */   }
/*     */   
/*     */   public void setIndices(ShortBuffer indices) {
/*  78 */     int pos = indices.position();
/*  79 */     this.buffer.clear();
/*  80 */     this.buffer.limit(indices.remaining());
/*  81 */     this.buffer.put(indices);
/*  82 */     this.buffer.flip();
/*  83 */     indices.position(pos);
/*  84 */     this.byteBuffer.position(0);
/*  85 */     this.byteBuffer.limit(this.buffer.limit() << 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateIndices(int targetOffset, short[] indices, int offset, int count) {
/*  90 */     int pos = this.byteBuffer.position();
/*  91 */     this.byteBuffer.position(targetOffset * 2);
/*  92 */     BufferUtils.copy(indices, offset, this.byteBuffer, count);
/*  93 */     this.byteBuffer.position(pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBuffer getBuffer() {
/* 103 */     return this.buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbind() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidate() {}
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 120 */     BufferUtils.disposeUnsafeByteBuffer(this.byteBuffer);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\IndexArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */