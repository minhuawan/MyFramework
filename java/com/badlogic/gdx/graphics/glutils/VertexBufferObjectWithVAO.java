/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.GL30;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
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
/*     */ public class VertexBufferObjectWithVAO
/*     */   implements VertexData
/*     */ {
/*  36 */   static final IntBuffer tmpHandle = BufferUtils.newIntBuffer(1);
/*     */   
/*     */   final VertexAttributes attributes;
/*     */   final FloatBuffer buffer;
/*     */   final ByteBuffer byteBuffer;
/*     */   int bufferHandle;
/*     */   final boolean isStatic;
/*     */   final int usage;
/*     */   boolean isDirty = false;
/*     */   boolean isBound = false;
/*  46 */   int vaoHandle = -1;
/*  47 */   IntArray cachedLocations = new IntArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexBufferObjectWithVAO(boolean isStatic, int numVertices, VertexAttribute... attributes) {
/*  58 */     this(isStatic, numVertices, new VertexAttributes(attributes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexBufferObjectWithVAO(boolean isStatic, int numVertices, VertexAttributes attributes) {
/*  69 */     this.isStatic = isStatic;
/*  70 */     this.attributes = attributes;
/*     */     
/*  72 */     this.byteBuffer = BufferUtils.newUnsafeByteBuffer(this.attributes.vertexSize * numVertices);
/*  73 */     this.buffer = this.byteBuffer.asFloatBuffer();
/*  74 */     this.buffer.flip();
/*  75 */     this.byteBuffer.flip();
/*  76 */     this.bufferHandle = Gdx.gl20.glGenBuffer();
/*  77 */     this.usage = isStatic ? 35044 : 35048;
/*  78 */     createVAO();
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexAttributes getAttributes() {
/*  83 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumVertices() {
/*  88 */     return this.buffer.limit() * 4 / this.attributes.vertexSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumMaxVertices() {
/*  93 */     return this.byteBuffer.capacity() / this.attributes.vertexSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatBuffer getBuffer() {
/*  98 */     this.isDirty = true;
/*  99 */     return this.buffer;
/*     */   }
/*     */   
/*     */   private void bufferChanged() {
/* 103 */     if (this.isBound) {
/* 104 */       Gdx.gl20.glBufferData(34962, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 105 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVertices(float[] vertices, int offset, int count) {
/* 111 */     this.isDirty = true;
/* 112 */     BufferUtils.copy(vertices, this.byteBuffer, count, offset);
/* 113 */     this.buffer.position(0);
/* 114 */     this.buffer.limit(count);
/* 115 */     bufferChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateVertices(int targetOffset, float[] vertices, int sourceOffset, int count) {
/* 120 */     this.isDirty = true;
/* 121 */     int pos = this.byteBuffer.position();
/* 122 */     this.byteBuffer.position(targetOffset * 4);
/* 123 */     BufferUtils.copy(vertices, sourceOffset, count, this.byteBuffer);
/* 124 */     this.byteBuffer.position(pos);
/* 125 */     this.buffer.position(0);
/* 126 */     bufferChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader) {
/* 136 */     bind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader, int[] locations) {
/* 141 */     GL30 gl = Gdx.gl30;
/*     */     
/* 143 */     gl.glBindVertexArray(this.vaoHandle);
/*     */     
/* 145 */     bindAttributes(shader, locations);
/*     */ 
/*     */     
/* 148 */     bindData((GL20)gl);
/*     */     
/* 150 */     this.isBound = true;
/*     */   }
/*     */   
/*     */   private void bindAttributes(ShaderProgram shader, int[] locations) {
/* 154 */     boolean stillValid = (this.cachedLocations.size != 0);
/* 155 */     int numAttributes = this.attributes.size();
/*     */     
/* 157 */     if (stillValid) {
/* 158 */       if (locations == null) {
/* 159 */         for (int i = 0; stillValid && i < numAttributes; i++) {
/* 160 */           VertexAttribute attribute = this.attributes.get(i);
/* 161 */           int location = shader.getAttributeLocation(attribute.alias);
/* 162 */           stillValid = (location == this.cachedLocations.get(i));
/*     */         } 
/*     */       } else {
/* 165 */         stillValid = (locations.length == this.cachedLocations.size);
/* 166 */         for (int i = 0; stillValid && i < numAttributes; i++) {
/* 167 */           stillValid = (locations[i] == this.cachedLocations.get(i));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 172 */     if (!stillValid) {
/* 173 */       Gdx.gl.glBindBuffer(34962, this.bufferHandle);
/* 174 */       unbindAttributes(shader);
/* 175 */       this.cachedLocations.clear();
/*     */       
/* 177 */       for (int i = 0; i < numAttributes; i++) {
/* 178 */         VertexAttribute attribute = this.attributes.get(i);
/* 179 */         if (locations == null) {
/* 180 */           this.cachedLocations.add(shader.getAttributeLocation(attribute.alias));
/*     */         } else {
/* 182 */           this.cachedLocations.add(locations[i]);
/*     */         } 
/*     */         
/* 185 */         int location = this.cachedLocations.get(i);
/* 186 */         if (location >= 0) {
/*     */ 
/*     */ 
/*     */           
/* 190 */           shader.enableVertexAttribute(location);
/* 191 */           shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, attribute.offset);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void unbindAttributes(ShaderProgram shaderProgram) {
/* 197 */     if (this.cachedLocations.size == 0) {
/*     */       return;
/*     */     }
/* 200 */     int numAttributes = this.attributes.size();
/* 201 */     for (int i = 0; i < numAttributes; i++) {
/* 202 */       int location = this.cachedLocations.get(i);
/* 203 */       if (location >= 0)
/*     */       {
/*     */         
/* 206 */         shaderProgram.disableVertexAttribute(location); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void bindData(GL20 gl) {
/* 211 */     if (this.isDirty) {
/* 212 */       gl.glBindBuffer(34962, this.bufferHandle);
/* 213 */       this.byteBuffer.limit(this.buffer.limit() * 4);
/* 214 */       gl.glBufferData(34962, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 215 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader) {
/* 226 */     unbind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader, int[] locations) {
/* 231 */     GL30 gl = Gdx.gl30;
/* 232 */     gl.glBindVertexArray(0);
/* 233 */     this.isBound = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 241 */     this.bufferHandle = Gdx.gl30.glGenBuffer();
/* 242 */     createVAO();
/* 243 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 251 */     GL30 gl = Gdx.gl30;
/*     */     
/* 253 */     gl.glBindBuffer(34962, 0);
/* 254 */     gl.glDeleteBuffer(this.bufferHandle);
/* 255 */     this.bufferHandle = 0;
/* 256 */     BufferUtils.disposeUnsafeByteBuffer(this.byteBuffer);
/* 257 */     deleteVAO();
/*     */   }
/*     */   
/*     */   private void createVAO() {
/* 261 */     tmpHandle.clear();
/* 262 */     Gdx.gl30.glGenVertexArrays(1, tmpHandle);
/* 263 */     this.vaoHandle = tmpHandle.get();
/*     */   }
/*     */   
/*     */   private void deleteVAO() {
/* 267 */     if (this.vaoHandle != -1) {
/* 268 */       tmpHandle.clear();
/* 269 */       tmpHandle.put(this.vaoHandle);
/* 270 */       tmpHandle.flip();
/* 271 */       Gdx.gl30.glDeleteVertexArrays(1, tmpHandle);
/* 272 */       this.vaoHandle = -1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\VertexBufferObjectWithVAO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */