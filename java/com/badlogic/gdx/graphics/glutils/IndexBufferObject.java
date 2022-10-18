/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class IndexBufferObject
/*     */   implements IndexData
/*     */ {
/*     */   final ShortBuffer buffer;
/*     */   final ByteBuffer byteBuffer;
/*     */   int bufferHandle;
/*     */   final boolean isDirect;
/*     */   boolean isDirty = true;
/*     */   boolean isBound = false;
/*     */   final int usage;
/*     */   private final boolean empty;
/*     */   
/*     */   public IndexBufferObject(int maxIndices) {
/*  63 */     this(true, maxIndices);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexBufferObject(boolean isStatic, int maxIndices) {
/*  72 */     this.empty = (maxIndices == 0);
/*  73 */     if (this.empty) {
/*  74 */       maxIndices = 1;
/*     */     }
/*     */     
/*  77 */     this.byteBuffer = BufferUtils.newUnsafeByteBuffer(maxIndices * 2);
/*  78 */     this.isDirect = true;
/*     */     
/*  80 */     this.buffer = this.byteBuffer.asShortBuffer();
/*  81 */     this.buffer.flip();
/*  82 */     this.byteBuffer.flip();
/*  83 */     this.bufferHandle = Gdx.gl20.glGenBuffer();
/*  84 */     this.usage = isStatic ? 35044 : 35048;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumIndices() {
/*  89 */     return this.empty ? 0 : this.buffer.limit();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumMaxIndices() {
/*  94 */     return this.empty ? 0 : this.buffer.capacity();
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
/* 110 */     this.isDirty = true;
/* 111 */     this.buffer.clear();
/* 112 */     this.buffer.put(indices, offset, count);
/* 113 */     this.buffer.flip();
/* 114 */     this.byteBuffer.position(0);
/* 115 */     this.byteBuffer.limit(count << 1);
/*     */     
/* 117 */     if (this.isBound) {
/* 118 */       Gdx.gl20.glBufferData(34963, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 119 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setIndices(ShortBuffer indices) {
/* 124 */     this.isDirty = true;
/* 125 */     int pos = indices.position();
/* 126 */     this.buffer.clear();
/* 127 */     this.buffer.put(indices);
/* 128 */     this.buffer.flip();
/* 129 */     indices.position(pos);
/* 130 */     this.byteBuffer.position(0);
/* 131 */     this.byteBuffer.limit(this.buffer.limit() << 1);
/*     */     
/* 133 */     if (this.isBound) {
/* 134 */       Gdx.gl20.glBufferData(34963, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 135 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateIndices(int targetOffset, short[] indices, int offset, int count) {
/* 141 */     this.isDirty = true;
/* 142 */     int pos = this.byteBuffer.position();
/* 143 */     this.byteBuffer.position(targetOffset * 2);
/* 144 */     BufferUtils.copy(indices, offset, this.byteBuffer, count);
/* 145 */     this.byteBuffer.position(pos);
/* 146 */     this.buffer.position(0);
/*     */     
/* 148 */     if (this.isBound) {
/* 149 */       Gdx.gl20.glBufferData(34963, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 150 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBuffer getBuffer() {
/* 161 */     this.isDirty = true;
/* 162 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind() {
/* 167 */     if (this.bufferHandle == 0) throw new GdxRuntimeException("No buffer allocated!");
/*     */     
/* 169 */     Gdx.gl20.glBindBuffer(34963, this.bufferHandle);
/* 170 */     if (this.isDirty) {
/* 171 */       this.byteBuffer.limit(this.buffer.limit() * 2);
/* 172 */       Gdx.gl20.glBufferData(34963, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 173 */       this.isDirty = false;
/*     */     } 
/* 175 */     this.isBound = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unbind() {
/* 180 */     Gdx.gl20.glBindBuffer(34963, 0);
/* 181 */     this.isBound = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 186 */     this.bufferHandle = Gdx.gl20.glGenBuffer();
/* 187 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 192 */     Gdx.gl20.glBindBuffer(34963, 0);
/* 193 */     Gdx.gl20.glDeleteBuffer(this.bufferHandle);
/* 194 */     this.bufferHandle = 0;
/*     */     
/* 196 */     BufferUtils.disposeUnsafeByteBuffer(this.byteBuffer);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\IndexBufferObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */