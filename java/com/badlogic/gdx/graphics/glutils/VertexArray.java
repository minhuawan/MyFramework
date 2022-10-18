/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
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
/*     */ public class VertexArray
/*     */   implements VertexData
/*     */ {
/*     */   final VertexAttributes attributes;
/*     */   final FloatBuffer buffer;
/*     */   final ByteBuffer byteBuffer;
/*     */   boolean isBound = false;
/*     */   
/*     */   public VertexArray(int numVertices, VertexAttribute... attributes) {
/*  48 */     this(numVertices, new VertexAttributes(attributes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexArray(int numVertices, VertexAttributes attributes) {
/*  56 */     this.attributes = attributes;
/*  57 */     this.byteBuffer = BufferUtils.newUnsafeByteBuffer(this.attributes.vertexSize * numVertices);
/*  58 */     this.buffer = this.byteBuffer.asFloatBuffer();
/*  59 */     this.buffer.flip();
/*  60 */     this.byteBuffer.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  65 */     BufferUtils.disposeUnsafeByteBuffer(this.byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatBuffer getBuffer() {
/*  70 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumVertices() {
/*  75 */     return this.buffer.limit() * 4 / this.attributes.vertexSize;
/*     */   }
/*     */   
/*     */   public int getNumMaxVertices() {
/*  79 */     return this.byteBuffer.capacity() / this.attributes.vertexSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVertices(float[] vertices, int offset, int count) {
/*  84 */     BufferUtils.copy(vertices, this.byteBuffer, count, offset);
/*  85 */     this.buffer.position(0);
/*  86 */     this.buffer.limit(count);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateVertices(int targetOffset, float[] vertices, int sourceOffset, int count) {
/*  91 */     int pos = this.byteBuffer.position();
/*  92 */     this.byteBuffer.position(targetOffset * 4);
/*  93 */     BufferUtils.copy(vertices, sourceOffset, count, this.byteBuffer);
/*  94 */     this.byteBuffer.position(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader) {
/*  99 */     bind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ShaderProgram shader, int[] locations) {
/* 104 */     int numAttributes = this.attributes.size();
/* 105 */     this.byteBuffer.limit(this.buffer.limit() * 4);
/* 106 */     if (locations == null) {
/* 107 */       for (int i = 0; i < numAttributes; i++) {
/* 108 */         VertexAttribute attribute = this.attributes.get(i);
/* 109 */         int location = shader.getAttributeLocation(attribute.alias);
/* 110 */         if (location >= 0) {
/* 111 */           shader.enableVertexAttribute(location);
/*     */           
/* 113 */           if (attribute.type == 5126) {
/* 114 */             this.buffer.position(attribute.offset / 4);
/* 115 */             shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, this.buffer);
/*     */           } else {
/*     */             
/* 118 */             this.byteBuffer.position(attribute.offset);
/* 119 */             shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, this.byteBuffer);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 124 */       for (int i = 0; i < numAttributes; i++) {
/* 125 */         VertexAttribute attribute = this.attributes.get(i);
/* 126 */         int location = locations[i];
/* 127 */         if (location >= 0) {
/* 128 */           shader.enableVertexAttribute(location);
/*     */           
/* 130 */           if (attribute.type == 5126) {
/* 131 */             this.buffer.position(attribute.offset / 4);
/* 132 */             shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, this.buffer);
/*     */           } else {
/*     */             
/* 135 */             this.byteBuffer.position(attribute.offset);
/* 136 */             shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized, this.attributes.vertexSize, this.byteBuffer);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     this.isBound = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader) {
/* 149 */     unbind(shader, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unbind(ShaderProgram shader, int[] locations) {
/* 154 */     int numAttributes = this.attributes.size();
/* 155 */     if (locations == null) {
/* 156 */       for (int i = 0; i < numAttributes; i++) {
/* 157 */         shader.disableVertexAttribute((this.attributes.get(i)).alias);
/*     */       }
/*     */     } else {
/* 160 */       for (int i = 0; i < numAttributes; i++) {
/* 161 */         int location = locations[i];
/* 162 */         if (location >= 0) shader.disableVertexAttribute(location); 
/*     */       } 
/*     */     } 
/* 165 */     this.isBound = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexAttributes getAttributes() {
/* 170 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public void invalidate() {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\VertexArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */