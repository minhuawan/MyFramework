/*      */ package com.badlogic.gdx.graphics;
/*      */ 
/*      */ import com.badlogic.gdx.Application;
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.glutils.IndexArray;
/*      */ import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
/*      */ import com.badlogic.gdx.graphics.glutils.IndexBufferObjectSubData;
/*      */ import com.badlogic.gdx.graphics.glutils.IndexData;
/*      */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*      */ import com.badlogic.gdx.graphics.glutils.VertexArray;
/*      */ import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
/*      */ import com.badlogic.gdx.graphics.glutils.VertexBufferObjectSubData;
/*      */ import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO;
/*      */ import com.badlogic.gdx.graphics.glutils.VertexData;
/*      */ import com.badlogic.gdx.math.Matrix3;
/*      */ import com.badlogic.gdx.math.Matrix4;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.badlogic.gdx.math.Vector3;
/*      */ import com.badlogic.gdx.math.collision.BoundingBox;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.Disposable;
/*      */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.ShortBuffer;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Mesh
/*      */   implements Disposable
/*      */ {
/*      */   public enum VertexDataType
/*      */   {
/*   69 */     VertexArray, VertexBufferObject, VertexBufferObjectSubData, VertexBufferObjectWithVAO;
/*      */   }
/*      */ 
/*      */   
/*   73 */   static final Map<Application, Array<Mesh>> meshes = new HashMap<Application, Array<Mesh>>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final VertexData vertices;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final IndexData indices;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean autoBind = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isVertexArray;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Vector3 tmpV;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private VertexData makeVertexBuffer(boolean isStatic, int maxVertices, VertexAttributes vertexAttributes) {
/*  137 */     if (Gdx.gl30 != null) {
/*  138 */       return (VertexData)new VertexBufferObjectWithVAO(isStatic, maxVertices, vertexAttributes);
/*      */     }
/*  140 */     return (VertexData)new VertexBufferObject(isStatic, maxVertices, vertexAttributes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh setVertices(float[] vertices) {
/*  185 */     this.vertices.setVertices(vertices, 0, vertices.length);
/*      */     
/*  187 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh setVertices(float[] vertices, int offset, int count) {
/*  197 */     this.vertices.setVertices(vertices, offset, count);
/*      */     
/*  199 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh updateVertices(int targetOffset, float[] source) {
/*  206 */     return updateVertices(targetOffset, source, 0, source.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh updateVertices(int targetOffset, float[] source, int sourceOffset, int count) {
/*  215 */     this.vertices.updateVertices(targetOffset, source, sourceOffset, count);
/*  216 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] getVertices(float[] vertices) {
/*  222 */     return getVertices(0, -1, vertices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] getVertices(int srcOffset, float[] vertices) {
/*  230 */     return getVertices(srcOffset, -1, vertices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] getVertices(int srcOffset, int count, float[] vertices) {
/*  238 */     return getVertices(srcOffset, count, vertices, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] getVertices(int srcOffset, int count, float[] vertices, int destOffset) {
/*  249 */     int max = getNumVertices() * getVertexSize() / 4;
/*  250 */     if (count == -1) {
/*  251 */       count = max - srcOffset;
/*  252 */       if (count > vertices.length - destOffset) count = vertices.length - destOffset; 
/*      */     } 
/*  254 */     if (srcOffset < 0 || count <= 0 || srcOffset + count > max || destOffset < 0 || destOffset >= vertices.length)
/*  255 */       throw new IndexOutOfBoundsException(); 
/*  256 */     if (vertices.length - destOffset < count) {
/*  257 */       throw new IllegalArgumentException("not enough room in vertices array, has " + vertices.length + " floats, needs " + count);
/*      */     }
/*  259 */     int pos = getVerticesBuffer().position();
/*  260 */     getVerticesBuffer().position(srcOffset);
/*  261 */     getVerticesBuffer().get(vertices, destOffset, count);
/*  262 */     getVerticesBuffer().position(pos);
/*  263 */     return vertices;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh setIndices(short[] indices) {
/*  271 */     this.indices.setIndices(indices, 0, indices.length);
/*      */     
/*  273 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh setIndices(short[] indices, int offset, int count) {
/*  283 */     this.indices.setIndices(indices, offset, count);
/*      */     
/*  285 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void getIndices(short[] indices) {
/*  291 */     getIndices(indices, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getIndices(short[] indices, int destOffset) {
/*  299 */     getIndices(0, indices, destOffset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getIndices(int srcOffset, short[] indices, int destOffset) {
/*  308 */     getIndices(srcOffset, -1, indices, destOffset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getIndices(int srcOffset, int count, short[] indices, int destOffset) {
/*  318 */     int max = getNumIndices();
/*  319 */     if (count < 0) count = max - srcOffset; 
/*  320 */     if (srcOffset < 0 || srcOffset >= max || srcOffset + count > max) {
/*  321 */       throw new IllegalArgumentException("Invalid range specified, offset: " + srcOffset + ", count: " + count + ", max: " + max);
/*      */     }
/*  323 */     if (indices.length - destOffset < count)
/*  324 */       throw new IllegalArgumentException("not enough room in indices array, has " + indices.length + " shorts, needs " + count); 
/*  325 */     int pos = getIndicesBuffer().position();
/*  326 */     getIndicesBuffer().position(srcOffset);
/*  327 */     getIndicesBuffer().get(indices, destOffset, count);
/*  328 */     getIndicesBuffer().position(pos);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumIndices() {
/*  333 */     return this.indices.getNumIndices();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumVertices() {
/*  338 */     return this.vertices.getNumVertices();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxVertices() {
/*  343 */     return this.vertices.getNumMaxVertices();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxIndices() {
/*  348 */     return this.indices.getNumMaxIndices();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getVertexSize() {
/*  353 */     return (this.vertices.getAttributes()).vertexSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoBind(boolean autoBind) {
/*  363 */     this.autoBind = autoBind;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void bind(ShaderProgram shader) {
/*  371 */     bind(shader, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void bind(ShaderProgram shader, int[] locations) {
/*  380 */     this.vertices.bind(shader, locations);
/*  381 */     if (this.indices.getNumIndices() > 0) this.indices.bind();
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unbind(ShaderProgram shader) {
/*  389 */     unbind(shader, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unbind(ShaderProgram shader, int[] locations) {
/*  398 */     this.vertices.unbind(shader, locations);
/*  399 */     if (this.indices.getNumIndices() > 0) this.indices.unbind();
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void render(ShaderProgram shader, int primitiveType) {
/*  422 */     render(shader, primitiveType, 0, (this.indices.getNumMaxIndices() > 0) ? getNumIndices() : getNumVertices(), this.autoBind);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void render(ShaderProgram shader, int primitiveType, int offset, int count) {
/*  449 */     render(shader, primitiveType, offset, count, this.autoBind);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void render(ShaderProgram shader, int primitiveType, int offset, int count, boolean autoBind) {
/*  477 */     if (count == 0)
/*      */       return; 
/*  479 */     if (autoBind) bind(shader);
/*      */     
/*  481 */     if (this.isVertexArray) {
/*  482 */       if (this.indices.getNumIndices() > 0) {
/*  483 */         ShortBuffer buffer = this.indices.getBuffer();
/*  484 */         int oldPosition = buffer.position();
/*  485 */         int oldLimit = buffer.limit();
/*  486 */         buffer.position(offset);
/*  487 */         buffer.limit(offset + count);
/*  488 */         Gdx.gl20.glDrawElements(primitiveType, count, 5123, buffer);
/*  489 */         buffer.position(oldPosition);
/*  490 */         buffer.limit(oldLimit);
/*      */       } else {
/*  492 */         Gdx.gl20.glDrawArrays(primitiveType, offset, count);
/*      */       }
/*      */     
/*  495 */     } else if (this.indices.getNumIndices() > 0) {
/*  496 */       Gdx.gl20.glDrawElements(primitiveType, count, 5123, offset * 2);
/*      */     } else {
/*  498 */       Gdx.gl20.glDrawArrays(primitiveType, offset, count);
/*      */     } 
/*      */     
/*  501 */     if (autoBind) unbind(shader);
/*      */   
/*      */   }
/*      */   
/*      */   public void dispose() {
/*  506 */     if (meshes.get(Gdx.app) != null) ((Array)meshes.get(Gdx.app)).removeValue(this, true); 
/*  507 */     this.vertices.dispose();
/*  508 */     this.indices.dispose();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VertexAttribute getVertexAttribute(int usage) {
/*  516 */     VertexAttributes attributes = this.vertices.getAttributes();
/*  517 */     int len = attributes.size();
/*  518 */     for (int i = 0; i < len; i++) {
/*  519 */       if ((attributes.get(i)).usage == usage) return attributes.get(i); 
/*      */     } 
/*  521 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public VertexAttributes getVertexAttributes() {
/*  526 */     return this.vertices.getAttributes();
/*      */   }
/*      */ 
/*      */   
/*      */   public FloatBuffer getVerticesBuffer() {
/*  531 */     return this.vertices.getBuffer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoundingBox calculateBoundingBox() {
/*  539 */     BoundingBox bbox = new BoundingBox();
/*  540 */     calculateBoundingBox(bbox);
/*  541 */     return bbox;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void calculateBoundingBox(BoundingBox bbox) {
/*  549 */     int i, numVertices = getNumVertices();
/*  550 */     if (numVertices == 0) throw new GdxRuntimeException("No vertices defined");
/*      */     
/*  552 */     FloatBuffer verts = this.vertices.getBuffer();
/*  553 */     bbox.inf();
/*  554 */     VertexAttribute posAttrib = getVertexAttribute(1);
/*  555 */     int offset = posAttrib.offset / 4;
/*  556 */     int vertexSize = (this.vertices.getAttributes()).vertexSize / 4;
/*  557 */     int idx = offset;
/*      */     
/*  559 */     switch (posAttrib.numComponents) {
/*      */       case 1:
/*  561 */         for (i = 0; i < numVertices; i++) {
/*  562 */           bbox.ext(verts.get(idx), 0.0F, 0.0F);
/*  563 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */       case 2:
/*  567 */         for (i = 0; i < numVertices; i++) {
/*  568 */           bbox.ext(verts.get(idx), verts.get(idx + 1), 0.0F);
/*  569 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */       case 3:
/*  573 */         for (i = 0; i < numVertices; i++) {
/*  574 */           bbox.ext(verts.get(idx), verts.get(idx + 1), verts.get(idx + 2));
/*  575 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoundingBox calculateBoundingBox(BoundingBox out, int offset, int count) {
/*  587 */     return extendBoundingBox(out.inf(), offset, count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoundingBox calculateBoundingBox(BoundingBox out, int offset, int count, Matrix4 transform) {
/*  596 */     return extendBoundingBox(out.inf(), offset, count, transform);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoundingBox extendBoundingBox(BoundingBox out, int offset, int count) {
/*  605 */     return extendBoundingBox(out, offset, count, null);
/*      */   }
/*      */   
/*  608 */   protected Mesh(VertexData vertices, IndexData indices, boolean isVertexArray) { this.tmpV = new Vector3(); this.vertices = vertices; this.indices = indices; this.isVertexArray = isVertexArray; addManagedMesh(Gdx.app, this); } public Mesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) { this.tmpV = new Vector3(); this.vertices = makeVertexBuffer(isStatic, maxVertices, new VertexAttributes(attributes)); this.indices = (IndexData)new IndexBufferObject(isStatic, maxIndices); this.isVertexArray = false; addManagedMesh(Gdx.app, this); } public Mesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes) { this.tmpV = new Vector3(); this.vertices = makeVertexBuffer(isStatic, maxVertices, attributes); this.indices = (IndexData)new IndexBufferObject(isStatic, maxIndices); this.isVertexArray = false; addManagedMesh(Gdx.app, this); } public Mesh(boolean staticVertices, boolean staticIndices, int maxVertices, int maxIndices, VertexAttributes attributes) { this.tmpV = new Vector3(); this.vertices = makeVertexBuffer(staticVertices, maxVertices, attributes); this.indices = (IndexData)new IndexBufferObject(staticIndices, maxIndices); this.isVertexArray = false; addManagedMesh(Gdx.app, this); } public Mesh(VertexDataType type, boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) { this.tmpV = new Vector3(); switch (type) { case VertexBufferObject:
/*      */         this.vertices = (VertexData)new VertexBufferObject(isStatic, maxVertices, attributes); this.indices = (IndexData)new IndexBufferObject(isStatic, maxIndices); this.isVertexArray = false; break;
/*      */       case VertexBufferObjectSubData:
/*      */         this.vertices = (VertexData)new VertexBufferObjectSubData(isStatic, maxVertices, attributes); this.indices = (IndexData)new IndexBufferObjectSubData(isStatic, maxIndices); this.isVertexArray = false; break;
/*      */       case VertexBufferObjectWithVAO:
/*      */         this.vertices = (VertexData)new VertexBufferObjectWithVAO(isStatic, maxVertices, attributes); this.indices = (IndexData)new IndexBufferObjectSubData(isStatic, maxIndices); this.isVertexArray = false; break;
/*      */       default:
/*      */         this.vertices = (VertexData)new VertexArray(maxVertices, attributes); this.indices = (IndexData)new IndexArray(maxIndices); this.isVertexArray = true; break; }
/*  616 */      addManagedMesh(Gdx.app, this); } public BoundingBox extendBoundingBox(BoundingBox out, int offset, int count, Matrix4 transform) { int i, numIndices = getNumIndices();
/*  617 */     int numVertices = getNumVertices();
/*  618 */     int max = (numIndices == 0) ? numVertices : numIndices;
/*  619 */     if (offset < 0 || count < 1 || offset + count > max) {
/*  620 */       throw new GdxRuntimeException("Invalid part specified ( offset=" + offset + ", count=" + count + ", max=" + max + " )");
/*      */     }
/*  622 */     FloatBuffer verts = this.vertices.getBuffer();
/*  623 */     ShortBuffer index = this.indices.getBuffer();
/*  624 */     VertexAttribute posAttrib = getVertexAttribute(1);
/*  625 */     int posoff = posAttrib.offset / 4;
/*  626 */     int vertexSize = (this.vertices.getAttributes()).vertexSize / 4;
/*  627 */     int end = offset + count;
/*      */     
/*  629 */     switch (posAttrib.numComponents) {
/*      */       case 1:
/*  631 */         if (numIndices > 0) {
/*  632 */           for (int j = offset; j < end; j++) {
/*  633 */             int idx = index.get(j) * vertexSize + posoff;
/*  634 */             this.tmpV.set(verts.get(idx), 0.0F, 0.0F);
/*  635 */             if (transform != null) this.tmpV.mul(transform); 
/*  636 */             out.ext(this.tmpV);
/*      */           }  break;
/*      */         } 
/*  639 */         for (i = offset; i < end; i++) {
/*  640 */           int idx = i * vertexSize + posoff;
/*  641 */           this.tmpV.set(verts.get(idx), 0.0F, 0.0F);
/*  642 */           if (transform != null) this.tmpV.mul(transform); 
/*  643 */           out.ext(this.tmpV);
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 2:
/*  648 */         if (numIndices > 0) {
/*  649 */           for (i = offset; i < end; i++) {
/*  650 */             int idx = index.get(i) * vertexSize + posoff;
/*  651 */             this.tmpV.set(verts.get(idx), verts.get(idx + 1), 0.0F);
/*  652 */             if (transform != null) this.tmpV.mul(transform); 
/*  653 */             out.ext(this.tmpV);
/*      */           }  break;
/*      */         } 
/*  656 */         for (i = offset; i < end; i++) {
/*  657 */           int idx = i * vertexSize + posoff;
/*  658 */           this.tmpV.set(verts.get(idx), verts.get(idx + 1), 0.0F);
/*  659 */           if (transform != null) this.tmpV.mul(transform); 
/*  660 */           out.ext(this.tmpV);
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 3:
/*  665 */         if (numIndices > 0) {
/*  666 */           for (i = offset; i < end; i++) {
/*  667 */             int idx = index.get(i) * vertexSize + posoff;
/*  668 */             this.tmpV.set(verts.get(idx), verts.get(idx + 1), verts.get(idx + 2));
/*  669 */             if (transform != null) this.tmpV.mul(transform); 
/*  670 */             out.ext(this.tmpV);
/*      */           }  break;
/*      */         } 
/*  673 */         for (i = offset; i < end; i++) {
/*  674 */           int idx = i * vertexSize + posoff;
/*  675 */           this.tmpV.set(verts.get(idx), verts.get(idx + 1), verts.get(idx + 2));
/*  676 */           if (transform != null) this.tmpV.mul(transform); 
/*  677 */           out.ext(this.tmpV);
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/*  682 */     return out; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateRadiusSquared(float centerX, float centerY, float centerZ, int offset, int count, Matrix4 transform) {
/*  694 */     int i, numIndices = getNumIndices();
/*  695 */     if (offset < 0 || count < 1 || offset + count > numIndices) throw new GdxRuntimeException("Not enough indices");
/*      */     
/*  697 */     FloatBuffer verts = this.vertices.getBuffer();
/*  698 */     ShortBuffer index = this.indices.getBuffer();
/*  699 */     VertexAttribute posAttrib = getVertexAttribute(1);
/*  700 */     int posoff = posAttrib.offset / 4;
/*  701 */     int vertexSize = (this.vertices.getAttributes()).vertexSize / 4;
/*  702 */     int end = offset + count;
/*      */     
/*  704 */     float result = 0.0F;
/*      */     
/*  706 */     switch (posAttrib.numComponents) {
/*      */       case 1:
/*  708 */         for (i = offset; i < end; i++) {
/*  709 */           int idx = index.get(i) * vertexSize + posoff;
/*  710 */           this.tmpV.set(verts.get(idx), 0.0F, 0.0F);
/*  711 */           if (transform != null) this.tmpV.mul(transform); 
/*  712 */           float r = this.tmpV.sub(centerX, centerY, centerZ).len2();
/*  713 */           if (r > result) result = r; 
/*      */         } 
/*      */         break;
/*      */       case 2:
/*  717 */         for (i = offset; i < end; i++) {
/*  718 */           int idx = index.get(i) * vertexSize + posoff;
/*  719 */           this.tmpV.set(verts.get(idx), verts.get(idx + 1), 0.0F);
/*  720 */           if (transform != null) this.tmpV.mul(transform); 
/*  721 */           float r = this.tmpV.sub(centerX, centerY, centerZ).len2();
/*  722 */           if (r > result) result = r; 
/*      */         } 
/*      */         break;
/*      */       case 3:
/*  726 */         for (i = offset; i < end; i++) {
/*  727 */           int idx = index.get(i) * vertexSize + posoff;
/*  728 */           this.tmpV.set(verts.get(idx), verts.get(idx + 1), verts.get(idx + 2));
/*  729 */           if (transform != null) this.tmpV.mul(transform); 
/*  730 */           float r = this.tmpV.sub(centerX, centerY, centerZ).len2();
/*  731 */           if (r > result) result = r; 
/*      */         } 
/*      */         break;
/*      */     } 
/*  735 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateRadius(float centerX, float centerY, float centerZ, int offset, int count, Matrix4 transform) {
/*  747 */     return (float)Math.sqrt(calculateRadiusSquared(centerX, centerY, centerZ, offset, count, transform));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateRadius(Vector3 center, int offset, int count, Matrix4 transform) {
/*  756 */     return calculateRadius(center.x, center.y, center.z, offset, count, transform);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateRadius(float centerX, float centerY, float centerZ, int offset, int count) {
/*  767 */     return calculateRadius(centerX, centerY, centerZ, offset, count, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateRadius(Vector3 center, int offset, int count) {
/*  776 */     return calculateRadius(center.x, center.y, center.z, offset, count, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateRadius(float centerX, float centerY, float centerZ) {
/*  785 */     return calculateRadius(centerX, centerY, centerZ, 0, getNumIndices(), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateRadius(Vector3 center) {
/*  792 */     return calculateRadius(center.x, center.y, center.z, 0, getNumIndices(), null);
/*      */   }
/*      */ 
/*      */   
/*      */   public ShortBuffer getIndicesBuffer() {
/*  797 */     return this.indices.getBuffer();
/*      */   }
/*      */   
/*      */   private static void addManagedMesh(Application app, Mesh mesh) {
/*  801 */     Array<Mesh> managedResources = meshes.get(app);
/*  802 */     if (managedResources == null) managedResources = new Array(); 
/*  803 */     managedResources.add(mesh);
/*  804 */     meshes.put(app, managedResources);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void invalidateAllMeshes(Application app) {
/*  810 */     Array<Mesh> meshesArray = meshes.get(app);
/*  811 */     if (meshesArray == null)
/*  812 */       return;  for (int i = 0; i < meshesArray.size; i++) {
/*  813 */       ((Mesh)meshesArray.get(i)).vertices.invalidate();
/*  814 */       ((Mesh)meshesArray.get(i)).indices.invalidate();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void clearAllMeshes(Application app) {
/*  820 */     meshes.remove(app);
/*      */   }
/*      */   
/*      */   public static String getManagedStatus() {
/*  824 */     StringBuilder builder = new StringBuilder();
/*  825 */     int i = 0;
/*  826 */     builder.append("Managed meshes/app: { ");
/*  827 */     for (Application app : meshes.keySet()) {
/*  828 */       builder.append(((Array)meshes.get(app)).size);
/*  829 */       builder.append(" ");
/*      */     } 
/*  831 */     builder.append("}");
/*  832 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void scale(float scaleX, float scaleY, float scaleZ) {
/*      */     int i;
/*  842 */     VertexAttribute posAttr = getVertexAttribute(1);
/*  843 */     int offset = posAttr.offset / 4;
/*  844 */     int numComponents = posAttr.numComponents;
/*  845 */     int numVertices = getNumVertices();
/*  846 */     int vertexSize = getVertexSize() / 4;
/*      */     
/*  848 */     float[] vertices = new float[numVertices * vertexSize];
/*  849 */     getVertices(vertices);
/*      */     
/*  851 */     int idx = offset;
/*  852 */     switch (numComponents) {
/*      */       case 1:
/*  854 */         for (i = 0; i < numVertices; i++) {
/*  855 */           vertices[idx] = vertices[idx] * scaleX;
/*  856 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */       case 2:
/*  860 */         for (i = 0; i < numVertices; i++) {
/*  861 */           vertices[idx] = vertices[idx] * scaleX;
/*  862 */           vertices[idx + 1] = vertices[idx + 1] * scaleY;
/*  863 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */       case 3:
/*  867 */         for (i = 0; i < numVertices; i++) {
/*  868 */           vertices[idx] = vertices[idx] * scaleX;
/*  869 */           vertices[idx + 1] = vertices[idx + 1] * scaleY;
/*  870 */           vertices[idx + 2] = vertices[idx + 2] * scaleZ;
/*  871 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/*  876 */     setVertices(vertices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transform(Matrix4 matrix) {
/*  884 */     transform(matrix, 0, getNumVertices());
/*      */   }
/*      */ 
/*      */   
/*      */   public void transform(Matrix4 matrix, int start, int count) {
/*  889 */     VertexAttribute posAttr = getVertexAttribute(1);
/*  890 */     int posOffset = posAttr.offset / 4;
/*  891 */     int stride = getVertexSize() / 4;
/*  892 */     int numComponents = posAttr.numComponents;
/*  893 */     int numVertices = getNumVertices();
/*      */     
/*  895 */     float[] vertices = new float[count * stride];
/*  896 */     getVertices(start * stride, count * stride, vertices);
/*      */     
/*  898 */     transform(matrix, vertices, stride, posOffset, numComponents, 0, count);
/*      */     
/*  900 */     updateVertices(start * stride, vertices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void transform(Matrix4 matrix, float[] vertices, int vertexSize, int offset, int dimensions, int start, int count) {
/*      */     int i;
/*  914 */     if (offset < 0 || dimensions < 1 || offset + dimensions > vertexSize) throw new IndexOutOfBoundsException(); 
/*  915 */     if (start < 0 || count < 1 || (start + count) * vertexSize > vertices.length) {
/*  916 */       throw new IndexOutOfBoundsException("start = " + start + ", count = " + count + ", vertexSize = " + vertexSize + ", length = " + vertices.length);
/*      */     }
/*      */     
/*  919 */     Vector3 tmp = new Vector3();
/*      */     
/*  921 */     int idx = offset + start * vertexSize;
/*  922 */     switch (dimensions) {
/*      */       case 1:
/*  924 */         for (i = 0; i < count; i++) {
/*  925 */           tmp.set(vertices[idx], 0.0F, 0.0F).mul(matrix);
/*  926 */           vertices[idx] = tmp.x;
/*  927 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */       case 2:
/*  931 */         for (i = 0; i < count; i++) {
/*  932 */           tmp.set(vertices[idx], vertices[idx + 1], 0.0F).mul(matrix);
/*  933 */           vertices[idx] = tmp.x;
/*  934 */           vertices[idx + 1] = tmp.y;
/*  935 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */       case 3:
/*  939 */         for (i = 0; i < count; i++) {
/*  940 */           tmp.set(vertices[idx], vertices[idx + 1], vertices[idx + 2]).mul(matrix);
/*  941 */           vertices[idx] = tmp.x;
/*  942 */           vertices[idx + 1] = tmp.y;
/*  943 */           vertices[idx + 2] = tmp.z;
/*  944 */           idx += vertexSize;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transformUV(Matrix3 matrix) {
/*  955 */     transformUV(matrix, 0, getNumVertices());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void transformUV(Matrix3 matrix, int start, int count) {
/*  960 */     VertexAttribute posAttr = getVertexAttribute(16);
/*  961 */     int offset = posAttr.offset / 4;
/*  962 */     int vertexSize = getVertexSize() / 4;
/*  963 */     int numVertices = getNumVertices();
/*      */     
/*  965 */     float[] vertices = new float[numVertices * vertexSize];
/*      */     
/*  967 */     getVertices(0, vertices.length, vertices);
/*  968 */     transformUV(matrix, vertices, vertexSize, offset, start, count);
/*  969 */     setVertices(vertices, 0, vertices.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void transformUV(Matrix3 matrix, float[] vertices, int vertexSize, int offset, int start, int count) {
/*  981 */     if (start < 0 || count < 1 || (start + count) * vertexSize > vertices.length) {
/*  982 */       throw new IndexOutOfBoundsException("start = " + start + ", count = " + count + ", vertexSize = " + vertexSize + ", length = " + vertices.length);
/*      */     }
/*      */     
/*  985 */     Vector2 tmp = new Vector2();
/*      */     
/*  987 */     int idx = offset + start * vertexSize;
/*  988 */     for (int i = 0; i < count; i++) {
/*  989 */       tmp.set(vertices[idx], vertices[idx + 1]).mul(matrix);
/*  990 */       vertices[idx] = tmp.x;
/*  991 */       vertices[idx + 1] = tmp.y;
/*  992 */       idx += vertexSize;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh copy(boolean isStatic, boolean removeDuplicates, int[] usage) {
/*      */     Mesh result;
/* 1005 */     int vertexSize = getVertexSize() / 4;
/* 1006 */     int numVertices = getNumVertices();
/* 1007 */     float[] vertices = new float[numVertices * vertexSize];
/* 1008 */     getVertices(0, vertices.length, vertices);
/* 1009 */     short[] checks = null;
/* 1010 */     VertexAttribute[] attrs = null;
/* 1011 */     int newVertexSize = 0;
/* 1012 */     if (usage != null) {
/* 1013 */       int size = 0;
/* 1014 */       int as = 0;
/* 1015 */       for (int i = 0; i < usage.length; i++) {
/* 1016 */         if (getVertexAttribute(usage[i]) != null) {
/* 1017 */           size += (getVertexAttribute(usage[i])).numComponents;
/* 1018 */           as++;
/*      */         } 
/* 1020 */       }  if (size > 0) {
/* 1021 */         attrs = new VertexAttribute[as];
/* 1022 */         checks = new short[size];
/* 1023 */         int idx = -1;
/* 1024 */         int ai = -1;
/* 1025 */         for (int j = 0; j < usage.length; j++) {
/* 1026 */           VertexAttribute a = getVertexAttribute(usage[j]);
/* 1027 */           if (a != null) {
/* 1028 */             for (int k = 0; k < a.numComponents; k++)
/* 1029 */               checks[++idx] = (short)(a.offset + k); 
/* 1030 */             attrs[++ai] = new VertexAttribute(a.usage, a.numComponents, a.alias);
/* 1031 */             newVertexSize += a.numComponents;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1035 */     }  if (checks == null) {
/* 1036 */       checks = new short[vertexSize]; short i;
/* 1037 */       for (i = 0; i < vertexSize; i = (short)(i + 1))
/* 1038 */         checks[i] = i; 
/* 1039 */       newVertexSize = vertexSize;
/*      */     } 
/*      */     
/* 1042 */     int numIndices = getNumIndices();
/* 1043 */     short[] indices = null;
/* 1044 */     if (numIndices > 0) {
/* 1045 */       indices = new short[numIndices];
/* 1046 */       getIndices(indices);
/* 1047 */       if (removeDuplicates || newVertexSize != vertexSize) {
/* 1048 */         float[] tmp = new float[vertices.length];
/* 1049 */         int size = 0;
/* 1050 */         for (int i = 0; i < numIndices; i++) {
/* 1051 */           int idx1 = indices[i] * vertexSize;
/* 1052 */           short newIndex = -1;
/* 1053 */           if (removeDuplicates) {
/* 1054 */             short j; for (j = 0; j < size && newIndex < 0; j = (short)(j + 1)) {
/* 1055 */               int idx2 = j * newVertexSize;
/* 1056 */               boolean found = true;
/* 1057 */               for (int k = 0; k < checks.length && found; k++) {
/* 1058 */                 if (tmp[idx2 + k] != vertices[idx1 + checks[k]]) found = false; 
/*      */               } 
/* 1060 */               if (found) newIndex = j; 
/*      */             } 
/*      */           } 
/* 1063 */           if (newIndex > 0) {
/* 1064 */             indices[i] = newIndex;
/*      */           } else {
/* 1066 */             int idx = size * newVertexSize;
/* 1067 */             for (int j = 0; j < checks.length; j++)
/* 1068 */               tmp[idx + j] = vertices[idx1 + checks[j]]; 
/* 1069 */             indices[i] = (short)size;
/* 1070 */             size++;
/*      */           } 
/*      */         } 
/* 1073 */         vertices = tmp;
/* 1074 */         numVertices = size;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1079 */     if (attrs == null) {
/* 1080 */       result = new Mesh(isStatic, numVertices, (indices == null) ? 0 : indices.length, getVertexAttributes());
/*      */     } else {
/* 1082 */       result = new Mesh(isStatic, numVertices, (indices == null) ? 0 : indices.length, attrs);
/* 1083 */     }  result.setVertices(vertices, 0, numVertices * newVertexSize);
/* 1084 */     result.setIndices(indices);
/* 1085 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh copy(boolean isStatic) {
/* 1092 */     return copy(isStatic, false, null);
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\Mesh.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */