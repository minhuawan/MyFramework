/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
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
/*     */ 
/*     */ 
/*     */ public class VertexBufferObjectSubData
/*     */   implements VertexData
/*     */ {
/*     */   final VertexAttributes attributes;
/*     */   final FloatBuffer buffer;
/*     */   final ByteBuffer byteBuffer;
/*     */   int bufferHandle;
/*     */   final boolean isDirect;
/*     */   final boolean isStatic;
/*     */   final int usage;
/*     */   boolean isDirty = false;
/*     */   boolean isBound = false;
/*     */   
/*     */   public VertexBufferObjectSubData(boolean isStatic, int numVertices, VertexAttribute... attributes) {
/*  69 */     this.isStatic = isStatic;
/*  70 */     this.attributes = new VertexAttributes(attributes);
/*  71 */     this.byteBuffer = BufferUtils.newByteBuffer(this.attributes.vertexSize * numVertices);
/*  72 */     this.isDirect = true;
/*     */     
/*  74 */     this.usage = isStatic ? 35044 : 35048;
/*  75 */     this.buffer = this.byteBuffer.asFloatBuffer();
/*  76 */     this.bufferHandle = createBufferObject();
/*  77 */     this.buffer.flip();
/*  78 */     this.byteBuffer.flip();
/*     */   }
/*     */   
/*     */   private int createBufferObject() {
/*  82 */     int result = Gdx.gl20.glGenBuffer();
/*  83 */     Gdx.gl20.glBindBuffer(34962, result);
/*  84 */     Gdx.gl20.glBufferData(34962, this.byteBuffer.capacity(), null, this.usage);
/*  85 */     Gdx.gl20.glBindBuffer(34962, 0);
/*  86 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexAttributes getAttributes() {
/*  91 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumVertices() {
/*  96 */     return this.buffer.limit() * 4 / this.attributes.vertexSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumMaxVertices() {
/* 101 */     return this.byteBuffer.capacity() / this.attributes.vertexSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatBuffer getBuffer() {
/* 106 */     this.isDirty = true;
/* 107 */     return this.buffer;
/*     */   }
/*     */   
/*     */   private void bufferChanged() {
/* 111 */     if (this.isBound) {
/* 112 */       Gdx.gl20.glBufferSubData(34962, 0, this.byteBuffer.limit(), this.byteBuffer);
/* 113 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVertices(float[] vertices, int offset, int count) {
/* 119 */     this.isDirty = true;
/* 120 */     if (this.isDirect) {
/* 121 */       BufferUtils.copy(vertices, this.byteBuffer, count, offset);
/* 122 */       this.buffer.position(0);
/* 123 */       this.buffer.limit(count);
/*     */     } else {
/* 125 */       this.buffer.clear();
/* 126 */       this.buffer.put(vertices, offset, count);
/* 127 */       this.buffer.flip();
/* 128 */       this.byteBuffer.position(0);
/* 129 */       this.byteBuffer.limit(this.buffer.limit() << 2);
/*     */     } 
/*     */     
/* 132 */     bufferChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateVertices(int targetOffset, float[] vertices, int sourceOffset, int count) {
/* 137 */     this.isDirty = true;
/* 138 */     if (this.isDirect) {
/* 139 */       int pos = this.byteBuffer.position();
/* 140 */       this.byteBuffer.position(targetOffset * 4);
/* 141 */       BufferUtils.copy(vertices, sourceOffset, count, this.byteBuffer);
/* 142 */       this.byteBuffer.position(pos);
/*     */     } else {
/* 144 */       throw new GdxRuntimeException("Buffer must be allocated direct.");
/*     */     } 
/* 146 */     bufferChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader) {
/* 154 */     bind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader, int[] locations) {
/* 159 */     GL20 gl = Gdx.gl20;
/*     */     
/* 161 */     gl.glBindBuffer(34962, this.bufferHandle);
/* 162 */     if (this.isDirty) {
/* 163 */       this.byteBuffer.limit(this.buffer.limit() * 4);
/* 164 */       gl.glBufferData(34962, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 165 */       this.isDirty = false;
/*     */     } 
/*     */     
/* 168 */     int numAttributes = this.attributes.size();
/* 169 */     if (locations == null) {
/* 170 */       for (int i = 0; i < numAttributes; i++) {
/* 171 */         VertexAttribute attribute = this.attributes.get(i);
/* 172 */         int location = shader.getAttributeLocation(attribute.alias);
/* 173 */         if (location >= 0) {
/* 174 */           shader.enableVertexAttribute(location);
/*     */           
/* 176 */           shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, attribute.offset);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 180 */       for (int i = 0; i < numAttributes; i++) {
/* 181 */         VertexAttribute attribute = this.attributes.get(i);
/* 182 */         int location = locations[i];
/* 183 */         if (location >= 0) {
/* 184 */           shader.enableVertexAttribute(location);
/*     */           
/* 186 */           shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, attribute.offset);
/*     */         } 
/*     */       } 
/*     */     } 
/* 190 */     this.isBound = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader) {
/* 198 */     unbind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader, int[] locations) {
/* 203 */     GL20 gl = Gdx.gl20;
/* 204 */     int numAttributes = this.attributes.size();
/* 205 */     if (locations == null) {
/* 206 */       for (int i = 0; i < numAttributes; i++) {
/* 207 */         shader.disableVertexAttribute((this.attributes.get(i)).alias);
/*     */       }
/*     */     } else {
/* 210 */       for (int i = 0; i < numAttributes; i++) {
/* 211 */         int location = locations[i];
/* 212 */         if (location >= 0) shader.disableVertexAttribute(location); 
/*     */       } 
/*     */     } 
/* 215 */     gl.glBindBuffer(34962, 0);
/* 216 */     this.isBound = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 221 */     this.bufferHandle = createBufferObject();
/* 222 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 228 */     GL20 gl = Gdx.gl20;
/* 229 */     gl.glBindBuffer(34962, 0);
/* 230 */     gl.glDeleteBuffer(this.bufferHandle);
/* 231 */     this.bufferHandle = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferHandle() {
/* 237 */     return this.bufferHandle;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\VertexBufferObjectSubData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */