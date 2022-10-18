/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.Buffer;
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
/*     */ public class VertexBufferObject
/*     */   implements VertexData
/*     */ {
/*     */   private VertexAttributes attributes;
/*     */   private FloatBuffer buffer;
/*     */   private ByteBuffer byteBuffer;
/*     */   private boolean ownsBuffer;
/*     */   private int bufferHandle;
/*     */   private int usage;
/*     */   boolean isDirty = false;
/*     */   boolean isBound = false;
/*     */   
/*     */   public VertexBufferObject(boolean isStatic, int numVertices, VertexAttribute... attributes) {
/*  69 */     this(isStatic, numVertices, new VertexAttributes(attributes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexBufferObject(boolean isStatic, int numVertices, VertexAttributes attributes) {
/*  78 */     this.bufferHandle = Gdx.gl20.glGenBuffer();
/*     */     
/*  80 */     ByteBuffer data = BufferUtils.newUnsafeByteBuffer(attributes.vertexSize * numVertices);
/*  81 */     data.limit(0);
/*  82 */     setBuffer(data, true, attributes);
/*  83 */     setUsage(isStatic ? 35044 : 35048);
/*     */   }
/*     */   
/*     */   protected VertexBufferObject(int usage, ByteBuffer data, boolean ownsBuffer, VertexAttributes attributes) {
/*  87 */     this.bufferHandle = Gdx.gl20.glGenBuffer();
/*     */     
/*  89 */     setBuffer(data, ownsBuffer, attributes);
/*  90 */     setUsage(usage);
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexAttributes getAttributes() {
/*  95 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumVertices() {
/* 100 */     return this.buffer.limit() * 4 / this.attributes.vertexSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumMaxVertices() {
/* 105 */     return this.byteBuffer.capacity() / this.attributes.vertexSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatBuffer getBuffer() {
/* 110 */     this.isDirty = true;
/* 111 */     return this.buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBuffer(Buffer data, boolean ownsBuffer, VertexAttributes value) {
/* 119 */     if (this.isBound) throw new GdxRuntimeException("Cannot change attributes while VBO is bound"); 
/* 120 */     if (this.ownsBuffer && this.byteBuffer != null)
/* 121 */       BufferUtils.disposeUnsafeByteBuffer(this.byteBuffer); 
/* 122 */     this.attributes = value;
/* 123 */     if (data instanceof ByteBuffer) {
/* 124 */       this.byteBuffer = (ByteBuffer)data;
/*     */     } else {
/* 126 */       throw new GdxRuntimeException("Only ByteBuffer is currently supported");
/* 127 */     }  this.ownsBuffer = ownsBuffer;
/*     */     
/* 129 */     int l = this.byteBuffer.limit();
/* 130 */     this.byteBuffer.limit(this.byteBuffer.capacity());
/* 131 */     this.buffer = this.byteBuffer.asFloatBuffer();
/* 132 */     this.byteBuffer.limit(l);
/* 133 */     this.buffer.limit(l / 4);
/*     */   }
/*     */   
/*     */   private void bufferChanged() {
/* 137 */     if (this.isBound) {
/* 138 */       Gdx.gl20.glBufferData(34962, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 139 */       this.isDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVertices(float[] vertices, int offset, int count) {
/* 145 */     this.isDirty = true;
/* 146 */     BufferUtils.copy(vertices, this.byteBuffer, count, offset);
/* 147 */     this.buffer.position(0);
/* 148 */     this.buffer.limit(count);
/* 149 */     bufferChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateVertices(int targetOffset, float[] vertices, int sourceOffset, int count) {
/* 154 */     this.isDirty = true;
/* 155 */     int pos = this.byteBuffer.position();
/* 156 */     this.byteBuffer.position(targetOffset * 4);
/* 157 */     BufferUtils.copy(vertices, sourceOffset, count, this.byteBuffer);
/* 158 */     this.byteBuffer.position(pos);
/* 159 */     this.buffer.position(0);
/* 160 */     bufferChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getUsage() {
/* 166 */     return this.usage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setUsage(int value) {
/* 172 */     if (this.isBound) throw new GdxRuntimeException("Cannot change usage while VBO is bound"); 
/* 173 */     this.usage = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader) {
/* 180 */     bind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader, int[] locations) {
/* 185 */     GL20 gl = Gdx.gl20;
/*     */     
/* 187 */     gl.glBindBuffer(34962, this.bufferHandle);
/* 188 */     if (this.isDirty) {
/* 189 */       this.byteBuffer.limit(this.buffer.limit() * 4);
/* 190 */       gl.glBufferData(34962, this.byteBuffer.limit(), this.byteBuffer, this.usage);
/* 191 */       this.isDirty = false;
/*     */     } 
/*     */     
/* 194 */     int numAttributes = this.attributes.size();
/* 195 */     if (locations == null) {
/* 196 */       for (int i = 0; i < numAttributes; i++) {
/* 197 */         VertexAttribute attribute = this.attributes.get(i);
/* 198 */         int location = shader.getAttributeLocation(attribute.alias);
/* 199 */         if (location >= 0) {
/* 200 */           shader.enableVertexAttribute(location);
/*     */           
/* 202 */           shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, attribute.offset);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 207 */       for (int i = 0; i < numAttributes; i++) {
/* 208 */         VertexAttribute attribute = this.attributes.get(i);
/* 209 */         int location = locations[i];
/* 210 */         if (location >= 0) {
/* 211 */           shader.enableVertexAttribute(location);
/*     */           
/* 213 */           shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, attribute.offset);
/*     */         } 
/*     */       } 
/*     */     } 
/* 217 */     this.isBound = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader) {
/* 225 */     unbind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader, int[] locations) {
/* 230 */     GL20 gl = Gdx.gl20;
/* 231 */     int numAttributes = this.attributes.size();
/* 232 */     if (locations == null) {
/* 233 */       for (int i = 0; i < numAttributes; i++) {
/* 234 */         shader.disableVertexAttribute((this.attributes.get(i)).alias);
/*     */       }
/*     */     } else {
/* 237 */       for (int i = 0; i < numAttributes; i++) {
/* 238 */         int location = locations[i];
/* 239 */         if (location >= 0) shader.disableVertexAttribute(location); 
/*     */       } 
/*     */     } 
/* 242 */     gl.glBindBuffer(34962, 0);
/* 243 */     this.isBound = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 249 */     this.bufferHandle = Gdx.gl20.glGenBuffer();
/* 250 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 256 */     GL20 gl = Gdx.gl20;
/* 257 */     gl.glBindBuffer(34962, 0);
/* 258 */     gl.glDeleteBuffer(this.bufferHandle);
/* 259 */     this.bufferHandle = 0;
/* 260 */     if (this.ownsBuffer) BufferUtils.disposeUnsafeByteBuffer(this.byteBuffer); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\VertexBufferObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */