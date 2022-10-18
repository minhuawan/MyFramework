/*     */ package com.badlogic.gdx.graphics.g3d.utils;public interface MeshPartBuilder { MeshPart getMeshPart();
/*     */   int getPrimitiveType();
/*     */   VertexAttributes getAttributes();
/*     */   void setColor(Color paramColor);
/*     */   void setColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
/*     */   void setUVRange(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
/*     */   void setUVRange(TextureRegion paramTextureRegion);
/*     */   Matrix4 getVertexTransform(Matrix4 paramMatrix4);
/*     */   void setVertexTransform(Matrix4 paramMatrix4);
/*     */   boolean isVertexTransformationEnabled();
/*     */   void setVertexTransformationEnabled(boolean paramBoolean);
/*     */   
/*     */   void ensureVertices(int paramInt);
/*     */   
/*     */   void ensureIndices(int paramInt);
/*     */   
/*     */   void ensureCapacity(int paramInt1, int paramInt2);
/*     */   
/*     */   void ensureTriangleIndices(int paramInt);
/*     */   
/*     */   void ensureRectangleIndices(int paramInt);
/*     */   
/*     */   short vertex(float... paramVarArgs);
/*     */   
/*     */   short vertex(Vector3 paramVector31, Vector3 paramVector32, Color paramColor, Vector2 paramVector2);
/*     */   
/*     */   short vertex(VertexInfo paramVertexInfo);
/*     */   
/*     */   short lastIndex();
/*     */   
/*     */   void index(short paramShort);
/*     */   
/*     */   void index(short paramShort1, short paramShort2);
/*     */   
/*     */   void index(short paramShort1, short paramShort2, short paramShort3);
/*     */   
/*     */   void index(short paramShort1, short paramShort2, short paramShort3, short paramShort4);
/*     */   
/*     */   void index(short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5, short paramShort6);
/*     */   
/*     */   void index(short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5, short paramShort6, short paramShort7, short paramShort8);
/*     */   
/*     */   void line(short paramShort1, short paramShort2);
/*     */   
/*     */   void line(VertexInfo paramVertexInfo1, VertexInfo paramVertexInfo2);
/*     */   
/*     */   void line(Vector3 paramVector31, Vector3 paramVector32);
/*     */   
/*     */   void line(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);
/*     */   
/*     */   void line(Vector3 paramVector31, Color paramColor1, Vector3 paramVector32, Color paramColor2);
/*     */   
/*     */   void triangle(short paramShort1, short paramShort2, short paramShort3);
/*     */   
/*     */   void triangle(VertexInfo paramVertexInfo1, VertexInfo paramVertexInfo2, VertexInfo paramVertexInfo3);
/*     */   
/*     */   void triangle(Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33);
/*     */   
/*     */   void triangle(Vector3 paramVector31, Color paramColor1, Vector3 paramVector32, Color paramColor2, Vector3 paramVector33, Color paramColor3);
/*     */   
/*     */   void rect(short paramShort1, short paramShort2, short paramShort3, short paramShort4);
/*     */   
/*     */   void rect(VertexInfo paramVertexInfo1, VertexInfo paramVertexInfo2, VertexInfo paramVertexInfo3, VertexInfo paramVertexInfo4);
/*     */   
/*     */   void rect(Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33, Vector3 paramVector34, Vector3 paramVector35);
/*     */   
/*     */   void rect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15);
/*     */   
/*     */   void addMesh(Mesh paramMesh);
/*     */   
/*     */   void addMesh(MeshPart paramMeshPart);
/*     */   
/*     */   void addMesh(Mesh paramMesh, int paramInt1, int paramInt2);
/*     */   
/*     */   void addMesh(float[] paramArrayOffloat, short[] paramArrayOfshort);
/*     */   
/*     */   void addMesh(float[] paramArrayOffloat, short[] paramArrayOfshort, int paramInt1, int paramInt2);
/*     */   
/*     */   @Deprecated
/*     */   void patch(VertexInfo paramVertexInfo1, VertexInfo paramVertexInfo2, VertexInfo paramVertexInfo3, VertexInfo paramVertexInfo4, int paramInt1, int paramInt2);
/*     */   
/*     */   @Deprecated
/*     */   void patch(Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33, Vector3 paramVector34, Vector3 paramVector35, int paramInt1, int paramInt2);
/*     */   
/*     */   @Deprecated
/*     */   void patch(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, int paramInt1, int paramInt2);
/*     */   
/*     */   @Deprecated
/*     */   void box(VertexInfo paramVertexInfo1, VertexInfo paramVertexInfo2, VertexInfo paramVertexInfo3, VertexInfo paramVertexInfo4, VertexInfo paramVertexInfo5, VertexInfo paramVertexInfo6, VertexInfo paramVertexInfo7, VertexInfo paramVertexInfo8);
/*     */   
/*     */   @Deprecated
/*     */   void box(Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33, Vector3 paramVector34, Vector3 paramVector35, Vector3 paramVector36, Vector3 paramVector37, Vector3 paramVector38);
/*     */   
/*     */   @Deprecated
/*     */   void box(Matrix4 paramMatrix4);
/*     */   
/*     */   @Deprecated
/*     */   void box(float paramFloat1, float paramFloat2, float paramFloat3);
/*     */   
/*     */   @Deprecated
/*     */   void box(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat1, int paramInt, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat, int paramInt, Vector3 paramVector31, Vector3 paramVector32);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat, int paramInt, Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33, Vector3 paramVector34);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat1, int paramInt, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat1, int paramInt, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat1, int paramInt, Vector3 paramVector31, Vector3 paramVector32, float paramFloat2, float paramFloat3);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat1, int paramInt, Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33, Vector3 paramVector34, float paramFloat2, float paramFloat3);
/*     */   
/*     */   @Deprecated
/*     */   void circle(float paramFloat1, int paramInt, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, Vector3 paramVector31, Vector3 paramVector32);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33, Vector3 paramVector34);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, Vector3 paramVector31, Vector3 paramVector32, float paramFloat3, float paramFloat4);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33, Vector3 paramVector34, float paramFloat3, float paramFloat4);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, int paramInt, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16, float paramFloat17, float paramFloat18);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10);
/*     */   
/*     */   @Deprecated
/*     */   void ellipse(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, Vector3 paramVector31, Vector3 paramVector32);
/*     */   
/*     */   @Deprecated
/*     */   void cylinder(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt);
/*     */   
/*     */   @Deprecated
/*     */   void cylinder(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, float paramFloat5);
/*     */   
/*     */   @Deprecated
/*     */   void cylinder(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, float paramFloat5, boolean paramBoolean);
/*     */   
/*     */   @Deprecated
/*     */   void cone(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt);
/*     */   
/*     */   @Deprecated
/*     */   void cone(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, float paramFloat5);
/*     */   
/*     */   @Deprecated
/*     */   void sphere(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2);
/*     */   
/*     */   @Deprecated
/*     */   void sphere(Matrix4 paramMatrix4, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2);
/*     */   
/*     */   @Deprecated
/*     */   void sphere(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
/*     */   
/*     */   @Deprecated
/*     */   void sphere(Matrix4 paramMatrix4, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
/*     */   
/*     */   @Deprecated
/*     */   void capsule(float paramFloat1, float paramFloat2, int paramInt);
/*     */   
/*     */   @Deprecated
/*     */   void arrow(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, int paramInt);
/*     */   
/* 196 */   public static class VertexInfo implements Pool.Poolable { public final Vector3 position = new Vector3();
/*     */     public boolean hasPosition;
/* 198 */     public final Vector3 normal = new Vector3(0.0F, 1.0F, 0.0F);
/*     */     public boolean hasNormal;
/* 200 */     public final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     public boolean hasColor;
/* 202 */     public final Vector2 uv = new Vector2();
/*     */     
/*     */     public boolean hasUV;
/*     */     
/*     */     public void reset() {
/* 207 */       this.position.set(0.0F, 0.0F, 0.0F);
/* 208 */       this.normal.set(0.0F, 1.0F, 0.0F);
/* 209 */       this.color.set(1.0F, 1.0F, 1.0F, 1.0F);
/* 210 */       this.uv.set(0.0F, 0.0F);
/*     */     }
/*     */     
/*     */     public VertexInfo set(Vector3 pos, Vector3 nor, Color col, Vector2 uv) {
/* 214 */       reset();
/* 215 */       if ((this.hasPosition = (pos != null)) == true) this.position.set(pos); 
/* 216 */       if ((this.hasNormal = (nor != null)) == true) this.normal.set(nor); 
/* 217 */       if ((this.hasColor = (col != null)) == true) this.color.set(col); 
/* 218 */       if ((this.hasUV = (uv != null)) == true) this.uv.set(uv); 
/* 219 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo set(VertexInfo other) {
/* 223 */       if (other == null) return set(null, null, null, null); 
/* 224 */       this.hasPosition = other.hasPosition;
/* 225 */       this.position.set(other.position);
/* 226 */       this.hasNormal = other.hasNormal;
/* 227 */       this.normal.set(other.normal);
/* 228 */       this.hasColor = other.hasColor;
/* 229 */       this.color.set(other.color);
/* 230 */       this.hasUV = other.hasUV;
/* 231 */       this.uv.set(other.uv);
/* 232 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setPos(float x, float y, float z) {
/* 236 */       this.position.set(x, y, z);
/* 237 */       this.hasPosition = true;
/* 238 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setPos(Vector3 pos) {
/* 242 */       if ((this.hasPosition = (pos != null)) == true) this.position.set(pos); 
/* 243 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setNor(float x, float y, float z) {
/* 247 */       this.normal.set(x, y, z);
/* 248 */       this.hasNormal = true;
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setNor(Vector3 nor) {
/* 253 */       if ((this.hasNormal = (nor != null)) == true) this.normal.set(nor); 
/* 254 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setCol(float r, float g, float b, float a) {
/* 258 */       this.color.set(r, g, b, a);
/* 259 */       this.hasColor = true;
/* 260 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setCol(Color col) {
/* 264 */       if ((this.hasColor = (col != null)) == true) this.color.set(col); 
/* 265 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setUV(float u, float v) {
/* 269 */       this.uv.set(u, v);
/* 270 */       this.hasUV = true;
/* 271 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo setUV(Vector2 uv) {
/* 275 */       if ((this.hasUV = (uv != null)) == true) this.uv.set(uv); 
/* 276 */       return this;
/*     */     }
/*     */     
/*     */     public VertexInfo lerp(VertexInfo target, float alpha) {
/* 280 */       if (this.hasPosition && target.hasPosition) this.position.lerp(target.position, alpha); 
/* 281 */       if (this.hasNormal && target.hasNormal) this.normal.lerp(target.normal, alpha); 
/* 282 */       if (this.hasColor && target.hasColor) this.color.lerp(target.color, alpha); 
/* 283 */       if (this.hasUV && target.hasUV) this.uv.lerp(target.uv, alpha); 
/* 284 */       return this;
/*     */     } }
/*     */    }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\MeshPartBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */