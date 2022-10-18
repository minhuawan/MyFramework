/*     */ package com.badlogic.gdx.graphics.g3d.particles.batches;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Material;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.Shader;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardControllerRenderData;
/*     */ import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Matrix3;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public class BillboardParticleBatch
/*     */   extends BufferedParticleBatch<BillboardControllerRenderData>
/*     */ {
/*  50 */   protected static final Vector3 TMP_V1 = new Vector3(), TMP_V2 = new Vector3(), TMP_V3 = new Vector3(), TMP_V4 = new Vector3();
/*  51 */   protected static final Vector3 TMP_V5 = new Vector3(); protected static final Vector3 TMP_V6 = new Vector3();
/*  52 */   protected static final Matrix3 TMP_M3 = new Matrix3();
/*     */   protected static final int sizeAndRotationUsage = 512;
/*     */   protected static final int directionUsage = 1024;
/*  55 */   private static final VertexAttributes GPU_ATTRIBUTES = new VertexAttributes(new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(16, 2, "a_texCoord0"), new VertexAttribute(2, 4, "a_color"), new VertexAttribute(512, 4, "a_sizeAndRotation") });
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
/*  66 */   private static final VertexAttributes CPU_ATTRIBUTES = new VertexAttributes(new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(16, 2, "a_texCoord0"), new VertexAttribute(2, 4, "a_color") });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private static final int GPU_POSITION_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(1)).offset / 4);
/*  72 */   private static final int GPU_UV_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(16)).offset / 4);
/*  73 */   private static final int GPU_SIZE_ROTATION_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(512)).offset / 4);
/*  74 */   private static final int GPU_COLOR_OFFSET = (short)((GPU_ATTRIBUTES.findByUsage(2)).offset / 4);
/*  75 */   private static final int GPU_VERTEX_SIZE = GPU_ATTRIBUTES.vertexSize / 4;
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
/*  87 */   private static final int CPU_POSITION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(1)).offset / 4);
/*  88 */   private static final int CPU_UV_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(16)).offset / 4);
/*  89 */   private static final int CPU_COLOR_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(2)).offset / 4);
/*  90 */   private static final int CPU_VERTEX_SIZE = CPU_ATTRIBUTES.vertexSize / 4;
/*     */   private static final int MAX_PARTICLES_PER_MESH = 8191;
/*     */   private static final int MAX_VERTICES_PER_MESH = 32764;
/*     */   private RenderablePool renderablePool;
/*     */   private Array<Renderable> renderables;
/*     */   private float[] vertices;
/*     */   private short[] indices;
/*     */   
/*     */   private class RenderablePool extends Pool<Renderable> { public Renderable newObject() {
/*  99 */       return BillboardParticleBatch.this.allocRenderable();
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Config
/*     */   {
/*     */     boolean useGPU;
/*     */     
/*     */     public Config(boolean useGPU, ParticleShader.AlignMode mode) {
/* 108 */       this.useGPU = useGPU;
/* 109 */       this.mode = mode;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ParticleShader.AlignMode mode;
/*     */ 
/*     */     
/*     */     public Config() {}
/*     */   }
/*     */   
/* 120 */   private int currentVertexSize = 0;
/*     */   private VertexAttributes currentAttributes;
/*     */   protected boolean useGPU = false;
/* 123 */   protected ParticleShader.AlignMode mode = ParticleShader.AlignMode.Screen;
/*     */ 
/*     */   
/*     */   protected Texture texture;
/*     */ 
/*     */   
/*     */   protected BlendingAttribute blendingAttribute;
/*     */   
/*     */   protected DepthTestAttribute depthTestAttribute;
/*     */   
/*     */   Shader shader;
/*     */ 
/*     */   
/*     */   public BillboardParticleBatch(ParticleShader.AlignMode mode, boolean useGPU, int capacity, BlendingAttribute blendingAttribute, DepthTestAttribute depthTestAttribute) {
/* 137 */     super(BillboardControllerRenderData.class);
/* 138 */     this.renderables = new Array();
/* 139 */     this.renderablePool = new RenderablePool();
/* 140 */     this.blendingAttribute = blendingAttribute;
/* 141 */     this.depthTestAttribute = depthTestAttribute;
/*     */     
/* 143 */     if (this.blendingAttribute == null)
/* 144 */       this.blendingAttribute = new BlendingAttribute(1, 771, 1.0F); 
/* 145 */     if (this.depthTestAttribute == null) this.depthTestAttribute = new DepthTestAttribute(515, false);
/*     */     
/* 147 */     allocIndices();
/* 148 */     initRenderData();
/* 149 */     ensureCapacity(capacity);
/* 150 */     setUseGpu(useGPU);
/* 151 */     setAlignMode(mode);
/*     */   }
/*     */   
/*     */   public BillboardParticleBatch(ParticleShader.AlignMode mode, boolean useGPU, int capacity) {
/* 155 */     this(mode, useGPU, capacity, (BlendingAttribute)null, (DepthTestAttribute)null);
/*     */   }
/*     */   
/*     */   public BillboardParticleBatch() {
/* 159 */     this(ParticleShader.AlignMode.Screen, false, 100);
/*     */   }
/*     */   
/*     */   public BillboardParticleBatch(int capacity) {
/* 163 */     this(ParticleShader.AlignMode.Screen, false, capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocParticlesData(int capacity) {
/* 168 */     this.vertices = new float[this.currentVertexSize * 4 * capacity];
/* 169 */     allocRenderables(capacity);
/*     */   }
/*     */   
/*     */   protected Renderable allocRenderable() {
/* 173 */     Renderable renderable = new Renderable();
/* 174 */     renderable.meshPart.primitiveType = 4;
/* 175 */     renderable.meshPart.offset = 0;
/* 176 */     renderable.material = new Material(new Attribute[] { (Attribute)this.blendingAttribute, (Attribute)this.depthTestAttribute, (Attribute)TextureAttribute.createDiffuse(this.texture) });
/* 177 */     renderable.meshPart.mesh = new Mesh(false, 32764, 49146, this.currentAttributes);
/* 178 */     renderable.meshPart.mesh.setIndices(this.indices);
/* 179 */     renderable.shader = this.shader;
/* 180 */     return renderable;
/*     */   }
/*     */   
/*     */   private void allocIndices() {
/* 184 */     int indicesCount = 49146;
/* 185 */     this.indices = new short[indicesCount];
/* 186 */     for (int i = 0, vertex = 0; i < indicesCount; i += 6, vertex += 4) {
/* 187 */       this.indices[i] = (short)vertex;
/* 188 */       this.indices[i + 1] = (short)(vertex + 1);
/* 189 */       this.indices[i + 2] = (short)(vertex + 2);
/* 190 */       this.indices[i + 3] = (short)(vertex + 2);
/* 191 */       this.indices[i + 4] = (short)(vertex + 3);
/* 192 */       this.indices[i + 5] = (short)vertex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void allocRenderables(int capacity) {
/* 198 */     int meshCount = MathUtils.ceil((capacity / 8191)), free = this.renderablePool.getFree();
/* 199 */     if (free < meshCount)
/* 200 */       for (int i = 0, left = meshCount - free; i < left; i++) {
/* 201 */         this.renderablePool.free(this.renderablePool.newObject());
/*     */       } 
/*     */   }
/*     */   
/*     */   private Shader getShader(Renderable renderable) {
/* 206 */     Shader shader = this.useGPU ? (Shader)new ParticleShader(renderable, new ParticleShader.Config(this.mode)) : (Shader)new DefaultShader(renderable);
/* 207 */     shader.init();
/* 208 */     return shader;
/*     */   }
/*     */   
/*     */   private void allocShader() {
/* 212 */     Renderable newRenderable = allocRenderable();
/* 213 */     this.shader = newRenderable.shader = getShader(newRenderable);
/* 214 */     this.renderablePool.free(newRenderable);
/*     */   }
/*     */   
/*     */   private void clearRenderablesPool() {
/* 218 */     this.renderablePool.freeAll(this.renderables);
/* 219 */     for (int i = 0, free = this.renderablePool.getFree(); i < free; i++) {
/* 220 */       Renderable renderable = (Renderable)this.renderablePool.obtain();
/* 221 */       renderable.meshPart.mesh.dispose();
/*     */     } 
/* 223 */     this.renderables.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVertexData() {
/* 228 */     if (this.useGPU) {
/* 229 */       this.currentAttributes = GPU_ATTRIBUTES;
/* 230 */       this.currentVertexSize = GPU_VERTEX_SIZE;
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 236 */       this.currentAttributes = CPU_ATTRIBUTES;
/* 237 */       this.currentVertexSize = CPU_VERTEX_SIZE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void initRenderData() {
/* 243 */     setVertexData();
/* 244 */     clearRenderablesPool();
/* 245 */     allocShader();
/* 246 */     resetCapacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAlignMode(ParticleShader.AlignMode mode) {
/* 251 */     if (mode != this.mode) {
/* 252 */       this.mode = mode;
/* 253 */       if (this.useGPU) {
/* 254 */         initRenderData();
/* 255 */         allocRenderables(this.bufferedParticlesCount);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ParticleShader.AlignMode getAlignMode() {
/* 261 */     return this.mode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUseGpu(boolean useGPU) {
/* 266 */     if (this.useGPU != useGPU) {
/* 267 */       this.useGPU = useGPU;
/* 268 */       initRenderData();
/* 269 */       allocRenderables(this.bufferedParticlesCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isUseGPU() {
/* 274 */     return this.useGPU;
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture) {
/* 278 */     this.renderablePool.freeAll(this.renderables);
/* 279 */     this.renderables.clear();
/* 280 */     for (int i = 0, free = this.renderablePool.getFree(); i < free; i++) {
/* 281 */       Renderable renderable = (Renderable)this.renderablePool.obtain();
/* 282 */       TextureAttribute attribute = (TextureAttribute)renderable.material.get(TextureAttribute.Diffuse);
/* 283 */       attribute.textureDescription.texture = (GLTexture)texture;
/*     */     } 
/* 285 */     this.texture = texture;
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/* 289 */     return this.texture;
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/* 294 */     super.begin();
/* 295 */     this.renderablePool.freeAll(this.renderables);
/* 296 */     this.renderables.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void putVertex(float[] vertices, int offset, float x, float y, float z, float u, float v, float scaleX, float scaleY, float cosRotation, float sinRotation, float r, float g, float b, float a) {
/* 304 */     vertices[offset + GPU_POSITION_OFFSET] = x;
/* 305 */     vertices[offset + GPU_POSITION_OFFSET + 1] = y;
/* 306 */     vertices[offset + GPU_POSITION_OFFSET + 2] = z;
/*     */     
/* 308 */     vertices[offset + GPU_UV_OFFSET] = u;
/* 309 */     vertices[offset + GPU_UV_OFFSET + 1] = v;
/*     */     
/* 311 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET] = scaleX;
/* 312 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET + 1] = scaleY;
/* 313 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET + 2] = cosRotation;
/* 314 */     vertices[offset + GPU_SIZE_ROTATION_OFFSET + 3] = sinRotation;
/*     */     
/* 316 */     vertices[offset + GPU_COLOR_OFFSET] = r;
/* 317 */     vertices[offset + GPU_COLOR_OFFSET + 1] = g;
/* 318 */     vertices[offset + GPU_COLOR_OFFSET + 2] = b;
/* 319 */     vertices[offset + GPU_COLOR_OFFSET + 3] = a;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void putVertex(float[] vertices, int offset, Vector3 p, float u, float v, float r, float g, float b, float a) {
/* 339 */     vertices[offset + CPU_POSITION_OFFSET] = p.x;
/* 340 */     vertices[offset + CPU_POSITION_OFFSET + 1] = p.y;
/* 341 */     vertices[offset + CPU_POSITION_OFFSET + 2] = p.z;
/*     */     
/* 343 */     vertices[offset + CPU_UV_OFFSET] = u;
/* 344 */     vertices[offset + CPU_UV_OFFSET + 1] = v;
/*     */     
/* 346 */     vertices[offset + CPU_COLOR_OFFSET] = r;
/* 347 */     vertices[offset + CPU_COLOR_OFFSET + 1] = g;
/* 348 */     vertices[offset + CPU_COLOR_OFFSET + 2] = b;
/* 349 */     vertices[offset + CPU_COLOR_OFFSET + 3] = a;
/*     */   }
/*     */   
/*     */   private void fillVerticesGPU(int[] particlesOffset) {
/* 353 */     int tp = 0;
/* 354 */     for (BillboardControllerRenderData data : this.renderData) {
/* 355 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 356 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 357 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 358 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 359 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/* 360 */       for (int p = 0, c = data.controller.particles.size; p < c; p++, tp++) {
/* 361 */         int baseOffset = particlesOffset[tp] * this.currentVertexSize * 4;
/* 362 */         float scale = scaleChannel.data[p * scaleChannel.strideSize];
/* 363 */         int regionOffset = p * regionChannel.strideSize;
/* 364 */         int positionOffset = p * positionChannel.strideSize;
/* 365 */         int colorOffset = p * colorChannel.strideSize;
/* 366 */         int rotationOffset = p * rotationChannel.strideSize;
/* 367 */         float px = positionChannel.data[positionOffset + 0], py = positionChannel.data[positionOffset + 1];
/* 368 */         float pz = positionChannel.data[positionOffset + 2];
/* 369 */         float u = regionChannel.data[regionOffset + 0];
/* 370 */         float v = regionChannel.data[regionOffset + 1];
/* 371 */         float u2 = regionChannel.data[regionOffset + 2];
/* 372 */         float v2 = regionChannel.data[regionOffset + 3];
/* 373 */         float sx = regionChannel.data[regionOffset + 4] * scale, sy = regionChannel.data[regionOffset + 5] * scale;
/*     */ 
/*     */         
/* 376 */         float r = colorChannel.data[colorOffset + 0];
/* 377 */         float g = colorChannel.data[colorOffset + 1];
/* 378 */         float b = colorChannel.data[colorOffset + 2];
/* 379 */         float a = colorChannel.data[colorOffset + 3];
/* 380 */         float cosRotation = rotationChannel.data[rotationOffset + 0];
/* 381 */         float sinRotation = rotationChannel.data[rotationOffset + 1];
/*     */ 
/*     */         
/* 384 */         putVertex(this.vertices, baseOffset, px, py, pz, u, v2, -sx, -sy, cosRotation, sinRotation, r, g, b, a);
/* 385 */         baseOffset += this.currentVertexSize;
/* 386 */         putVertex(this.vertices, baseOffset, px, py, pz, u2, v2, sx, -sy, cosRotation, sinRotation, r, g, b, a);
/* 387 */         baseOffset += this.currentVertexSize;
/* 388 */         putVertex(this.vertices, baseOffset, px, py, pz, u2, v, sx, sy, cosRotation, sinRotation, r, g, b, a);
/* 389 */         baseOffset += this.currentVertexSize;
/* 390 */         putVertex(this.vertices, baseOffset, px, py, pz, u, v, -sx, sy, cosRotation, sinRotation, r, g, b, a);
/* 391 */         baseOffset += this.currentVertexSize;
/*     */       } 
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
/*     */   private void fillVerticesToViewPointCPU(int[] particlesOffset) {
/* 469 */     int tp = 0;
/* 470 */     for (BillboardControllerRenderData data : this.renderData) {
/* 471 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 472 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 473 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 474 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 475 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/*     */       
/* 477 */       for (int p = 0, c = data.controller.particles.size; p < c; p++, tp++) {
/* 478 */         int baseOffset = particlesOffset[tp] * this.currentVertexSize * 4;
/* 479 */         float scale = scaleChannel.data[p * scaleChannel.strideSize];
/* 480 */         int regionOffset = p * regionChannel.strideSize;
/* 481 */         int positionOffset = p * positionChannel.strideSize;
/* 482 */         int colorOffset = p * colorChannel.strideSize;
/* 483 */         int rotationOffset = p * rotationChannel.strideSize;
/* 484 */         float px = positionChannel.data[positionOffset + 0], py = positionChannel.data[positionOffset + 1];
/* 485 */         float pz = positionChannel.data[positionOffset + 2];
/* 486 */         float u = regionChannel.data[regionOffset + 0];
/* 487 */         float v = regionChannel.data[regionOffset + 1];
/* 488 */         float u2 = regionChannel.data[regionOffset + 2];
/* 489 */         float v2 = regionChannel.data[regionOffset + 3];
/* 490 */         float sx = regionChannel.data[regionOffset + 4] * scale, sy = regionChannel.data[regionOffset + 5] * scale;
/*     */ 
/*     */         
/* 493 */         float r = colorChannel.data[colorOffset + 0];
/* 494 */         float g = colorChannel.data[colorOffset + 1];
/* 495 */         float b = colorChannel.data[colorOffset + 2];
/* 496 */         float a = colorChannel.data[colorOffset + 3];
/* 497 */         float cosRotation = rotationChannel.data[rotationOffset + 0];
/* 498 */         float sinRotation = rotationChannel.data[rotationOffset + 1];
/* 499 */         Vector3 look = TMP_V3.set(this.camera.position).sub(px, py, pz).nor();
/* 500 */         Vector3 right = TMP_V1.set(this.camera.up).crs(look).nor();
/* 501 */         Vector3 up = TMP_V2.set(look).crs(right);
/* 502 */         right.scl(sx);
/* 503 */         up.scl(sy);
/*     */         
/* 505 */         if (cosRotation != 1.0F) {
/* 506 */           TMP_M3.setToRotation(look, cosRotation, sinRotation);
/* 507 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 508 */               .set(-TMP_V1.x - TMP_V2.x, -TMP_V1.y - TMP_V2.y, -TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v2, r, g, b, a);
/*     */           
/* 510 */           baseOffset += this.currentVertexSize;
/* 511 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 512 */               .set(TMP_V1.x - TMP_V2.x, TMP_V1.y - TMP_V2.y, TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v2, r, g, b, a);
/*     */           
/* 514 */           baseOffset += this.currentVertexSize;
/* 515 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 516 */               .set(TMP_V1.x + TMP_V2.x, TMP_V1.y + TMP_V2.y, TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v, r, g, b, a);
/*     */           
/* 518 */           baseOffset += this.currentVertexSize;
/* 519 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 520 */               .set(-TMP_V1.x + TMP_V2.x, -TMP_V1.y + TMP_V2.y, -TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v, r, g, b, a);
/*     */         } else {
/*     */           
/* 523 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 524 */               .set(-TMP_V1.x - TMP_V2.x + px, -TMP_V1.y - TMP_V2.y + py, -TMP_V1.z - TMP_V2.z + pz), u, v2, r, g, b, a);
/* 525 */           baseOffset += this.currentVertexSize;
/* 526 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 527 */               .set(TMP_V1.x - TMP_V2.x + px, TMP_V1.y - TMP_V2.y + py, TMP_V1.z - TMP_V2.z + pz), u2, v2, r, g, b, a);
/* 528 */           baseOffset += this.currentVertexSize;
/* 529 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 530 */               .set(TMP_V1.x + TMP_V2.x + px, TMP_V1.y + TMP_V2.y + py, TMP_V1.z + TMP_V2.z + pz), u2, v, r, g, b, a);
/* 531 */           baseOffset += this.currentVertexSize;
/* 532 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 533 */               .set(-TMP_V1.x + TMP_V2.x + px, -TMP_V1.y + TMP_V2.y + py, -TMP_V1.z + TMP_V2.z + pz), u, v, r, g, b, a);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fillVerticesToScreenCPU(int[] particlesOffset) {
/* 540 */     Vector3 look = TMP_V3.set(this.camera.direction).scl(-1.0F);
/* 541 */     Vector3 right = TMP_V4.set(this.camera.up).crs(look).nor();
/* 542 */     Vector3 up = this.camera.up;
/*     */     
/* 544 */     int tp = 0;
/* 545 */     for (BillboardControllerRenderData data : this.renderData) {
/* 546 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 547 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 548 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 549 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 550 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/*     */       
/* 552 */       for (int p = 0, c = data.controller.particles.size; p < c; p++, tp++) {
/* 553 */         int baseOffset = particlesOffset[tp] * this.currentVertexSize * 4;
/* 554 */         float scale = scaleChannel.data[p * scaleChannel.strideSize];
/* 555 */         int regionOffset = p * regionChannel.strideSize;
/* 556 */         int positionOffset = p * positionChannel.strideSize;
/* 557 */         int colorOffset = p * colorChannel.strideSize;
/* 558 */         int rotationOffset = p * rotationChannel.strideSize;
/* 559 */         float px = positionChannel.data[positionOffset + 0], py = positionChannel.data[positionOffset + 1];
/* 560 */         float pz = positionChannel.data[positionOffset + 2];
/* 561 */         float u = regionChannel.data[regionOffset + 0];
/* 562 */         float v = regionChannel.data[regionOffset + 1];
/* 563 */         float u2 = regionChannel.data[regionOffset + 2];
/* 564 */         float v2 = regionChannel.data[regionOffset + 3];
/* 565 */         float sx = regionChannel.data[regionOffset + 4] * scale, sy = regionChannel.data[regionOffset + 5] * scale;
/*     */ 
/*     */         
/* 568 */         float r = colorChannel.data[colorOffset + 0];
/* 569 */         float g = colorChannel.data[colorOffset + 1];
/* 570 */         float b = colorChannel.data[colorOffset + 2];
/* 571 */         float a = colorChannel.data[colorOffset + 3];
/* 572 */         float cosRotation = rotationChannel.data[rotationOffset + 0];
/* 573 */         float sinRotation = rotationChannel.data[rotationOffset + 1];
/* 574 */         TMP_V1.set(right).scl(sx);
/* 575 */         TMP_V2.set(up).scl(sy);
/*     */         
/* 577 */         if (cosRotation != 1.0F) {
/* 578 */           TMP_M3.setToRotation(look, cosRotation, sinRotation);
/* 579 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 580 */               .set(-TMP_V1.x - TMP_V2.x, -TMP_V1.y - TMP_V2.y, -TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v2, r, g, b, a);
/*     */           
/* 582 */           baseOffset += this.currentVertexSize;
/* 583 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 584 */               .set(TMP_V1.x - TMP_V2.x, TMP_V1.y - TMP_V2.y, TMP_V1.z - TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v2, r, g, b, a);
/*     */           
/* 586 */           baseOffset += this.currentVertexSize;
/* 587 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 588 */               .set(TMP_V1.x + TMP_V2.x, TMP_V1.y + TMP_V2.y, TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u2, v, r, g, b, a);
/*     */           
/* 590 */           baseOffset += this.currentVertexSize;
/* 591 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 592 */               .set(-TMP_V1.x + TMP_V2.x, -TMP_V1.y + TMP_V2.y, -TMP_V1.z + TMP_V2.z).mul(TMP_M3).add(px, py, pz), u, v, r, g, b, a);
/*     */         } else {
/*     */           
/* 595 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 596 */               .set(-TMP_V1.x - TMP_V2.x + px, -TMP_V1.y - TMP_V2.y + py, -TMP_V1.z - TMP_V2.z + pz), u, v2, r, g, b, a);
/* 597 */           baseOffset += this.currentVertexSize;
/* 598 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 599 */               .set(TMP_V1.x - TMP_V2.x + px, TMP_V1.y - TMP_V2.y + py, TMP_V1.z - TMP_V2.z + pz), u2, v2, r, g, b, a);
/* 600 */           baseOffset += this.currentVertexSize;
/* 601 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 602 */               .set(TMP_V1.x + TMP_V2.x + px, TMP_V1.y + TMP_V2.y + py, TMP_V1.z + TMP_V2.z + pz), u2, v, r, g, b, a);
/* 603 */           baseOffset += this.currentVertexSize;
/* 604 */           putVertex(this.vertices, baseOffset, TMP_V6
/* 605 */               .set(-TMP_V1.x + TMP_V2.x + px, -TMP_V1.y + TMP_V2.y + py, -TMP_V1.z + TMP_V2.z + pz), u, v, r, g, b, a);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flush(int[] offsets) {
/* 615 */     if (this.useGPU)
/*     */     
/* 617 */     { fillVerticesGPU(offsets);
/*     */       
/*     */        }
/*     */     
/* 621 */     else if (this.mode == ParticleShader.AlignMode.Screen)
/* 622 */     { fillVerticesToScreenCPU(offsets); }
/* 623 */     else if (this.mode == ParticleShader.AlignMode.ViewPoint) { fillVerticesToViewPointCPU(offsets); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 629 */     int addedVertexCount = 0;
/* 630 */     int vCount = this.bufferedParticlesCount * 4; int v;
/* 631 */     for (v = 0; v < vCount; v += addedVertexCount) {
/* 632 */       addedVertexCount = Math.min(vCount - v, 32764);
/* 633 */       Renderable renderable = (Renderable)this.renderablePool.obtain();
/* 634 */       renderable.meshPart.size = addedVertexCount / 4 * 6;
/* 635 */       renderable.meshPart.mesh.setVertices(this.vertices, this.currentVertexSize * v, this.currentVertexSize * addedVertexCount);
/* 636 */       renderable.meshPart.update();
/* 637 */       this.renderables.add(renderable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 643 */     for (Renderable renderable : this.renderables) {
/* 644 */       renderables.add(((Renderable)pool.obtain()).set(renderable));
/*     */     }
/*     */   }
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 649 */     ResourceData.SaveData data = resources.createSaveData("billboardBatch");
/* 650 */     data.save("cfg", new Config(this.useGPU, this.mode));
/* 651 */     data.saveAsset(manager.getAssetFileName(this.texture), Texture.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 656 */     ResourceData.SaveData data = resources.getSaveData("billboardBatch");
/* 657 */     if (data != null) {
/* 658 */       setTexture((Texture)manager.get(data.loadAsset()));
/* 659 */       Config cfg = (Config)data.load("cfg");
/* 660 */       setUseGpu(cfg.useGPU);
/* 661 */       setAlignMode(cfg.mode);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\batches\BillboardParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */