/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
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
/*     */ public class IndexBufferObjectSubData
/*     */   implements IndexData
/*     */ {
/*     */   final ShortBuffer buffer;
/*     */   final ByteBuffer byteBuffer;
/*     */   int bufferHandle;
/*     */   final boolean isDirect;
/*     */   boolean isDirty = true;
/*     */   boolean isBound = false;
/*     */   final int usage;
/*     */   
/*     */   public IndexBufferObjectSubData(boolean isStatic, int maxIndices) {
/*  56 */     this.byteBuffer = BufferUtils.newByteBuffer(maxIndices * 2);
/*  57 */     this.isDirect = true;
/*     */     
/*  59 */     this.usage = isStatic ? 35044 : 35048;
/*  60 */     this.buffer = this.byteBuffer.asShortBuffer();
/*  61 */     this.buffer.flip();
/*  62 */     this.byteBuffer.flip();
/*  63 */     this.bufferHandle = createBufferObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexBufferObjectSubData(int maxIndices) {
/*  70 */     this.byteBuffer = BufferUtils.newByteBuffer(maxIndices * 2);
/*  71 */     this.isDirect = true;
/*     */     
/*  73 */     this.usage = 35044;
/*  74 */     this.buffer = this.byteBuffer.asShortBuffer();
/*  75 */     this.buffer.flip();
/*  76 */     this.byteBuffer.flip();
/*  77 */     this.bufferHandle = createBufferObject();
/*     */   }
/*     */   
/*     */   private int createBufferObject() {
/*  81 */     int result = Gdx.gl20.glGenBuffer();
/*  82 */     Gdx.gl20.glBindBuffer(34963, result);
/*  83 */     Gdx.gl20.glBufferData(34963, this.byteBuffer.capacity(), null, this.usage);
/*  84 */     Gdx.gl20.glBindBuffer(34963, 0);
/*  85 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumIndices() {
/*  90 */     return this.buffer.limit();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumMaxIndices() {
/*  95 */     return this.buffer.capacity();
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
/* 111 */     this.isDirty = true;
/* 112 */     this.buffer.clear();
/* 113 */     this.buffer.put(indices, offset, count);
/* 114 */     this.buffer.flip();
/* 115 */     this.byteBuffer.position(0);
/* 116 */     this.byteBuffer.limit(count << 1);
/*     */     
/* 118 */     if (this.isBound) {
/* 119 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 120 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setIndices(ShortBuffer indices) {
/* 125 */     int pos = indices.position();
/* 126 */     this.isDirty = true;
/* 127 */     this.buffer.clear();
/* 128 */     this.buffer.put(indices);
/* 129 */     this.buffer.flip();
/* 130 */     indices.position(pos);
/* 131 */     this.byteBuffer.position(0);
/* 132 */     this.byteBuffer.limit(this.buffer.limit() << 1);
/*     */     
/* 134 */     if (this.isBound) {
/* 135 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 136 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateIndices(int targetOffset, short[] indices, int offset, int count) {
/* 142 */     this.isDirty = true;
/* 143 */     int pos = this.byteBuffer.position();
/* 144 */     this.byteBuffer.position(targetOffset * 2);
/* 145 */     BufferUtils.copy(indices, offset, this.byteBuffer, count);
/* 146 */     this.byteBuffer.position(pos);
/* 147 */     this.buffer.position(0);
/*     */     
/* 149 */     if (this.isBound) {
/* 150 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 151 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBuffer getBuffer() {
/* 163 */     this.isDirty = true;
/* 164 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind() {
/* 169 */     if (this.bufferHandle == 0) throw new GdxRuntimeException("IndexBufferObject cannot be used after it has been disposed.");
/*     */     
/* 171 */     Gdx.gl20.glBindBuffer(34963, this.bufferHandle);
/* 172 */     if (this.isDirty) {
/* 173 */       this.byteBuffer.limit(this.buffer.limit() * 2);
/* 174 */       Gdx.gl20.glBufferSubData(34963, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 175 */       this.isDirty = false;
/*     */     } 
/* 177 */     this.isBound = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unbind() {
/* 182 */     Gdx.gl20.glBindBuffer(34963, 0);
/* 183 */     this.isBound = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 188 */     this.bufferHandle = createBufferObject();
/* 189 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 194 */     GL20 gl = Gdx.gl20;
/* 195 */     gl.glBindBuffer(34963, 0);
/* 196 */     gl.glDeleteBuffer(this.bufferHandle);
/* 197 */     this.bufferHandle = 0;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\IndexBufferObjectSubData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */