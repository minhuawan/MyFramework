/*     */ package com.badlogic.gdx.graphics.g3d.particles.batches;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
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
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.PointSpriteControllerRenderData;
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
/*     */ public class PointSpriteParticleBatch
/*     */   extends BufferedParticleBatch<PointSpriteControllerRenderData>
/*     */ {
/*     */   private static boolean pointSpritesEnabled = false;
/*  49 */   protected static final Vector3 TMP_V1 = new Vector3();
/*     */   protected static final int sizeAndRotationUsage = 512;
/*  51 */   protected static final VertexAttributes CPU_ATTRIBUTES = new VertexAttributes(new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(2, 4, "a_color"), new VertexAttribute(16, 4, "a_region"), new VertexAttribute(512, 3, "a_sizeAndRotation") });
/*     */ 
/*     */ 
/*     */   
/*  55 */   protected static final int CPU_VERTEX_SIZE = (short)(CPU_ATTRIBUTES.vertexSize / 4);
/*  56 */   protected static final int CPU_POSITION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(1)).offset / 4);
/*  57 */   protected static final int CPU_COLOR_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(2)).offset / 4);
/*  58 */   protected static final int CPU_REGION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(16)).offset / 4);
/*  59 */   protected static final int CPU_SIZE_AND_ROTATION_OFFSET = (short)((CPU_ATTRIBUTES.findByUsage(512)).offset / 4);
/*     */   
/*     */   private static void enablePointSprites() {
/*  62 */     Gdx.gl.glEnable(34370);
/*  63 */     if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
/*  64 */       Gdx.gl.glEnable(34913);
/*     */     }
/*  66 */     pointSpritesEnabled = true;
/*     */   }
/*     */   
/*     */   private float[] vertices;
/*     */   Renderable renderable;
/*     */   
/*     */   public PointSpriteParticleBatch() {
/*  73 */     this(1000);
/*     */   }
/*     */   
/*     */   public PointSpriteParticleBatch(int capacity) {
/*  77 */     this(capacity, new ParticleShader.Config(ParticleShader.ParticleType.Point));
/*     */   }
/*     */   
/*     */   public PointSpriteParticleBatch(int capacity, ParticleShader.Config shaderConfig) {
/*  81 */     super(PointSpriteControllerRenderData.class);
/*     */     
/*  83 */     if (!pointSpritesEnabled) enablePointSprites();
/*     */     
/*  85 */     allocRenderable();
/*  86 */     ensureCapacity(capacity);
/*  87 */     this.renderable.shader = (Shader)new ParticleShader(this.renderable, shaderConfig);
/*  88 */     this.renderable.shader.init();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void allocParticlesData(int capacity) {
/*  93 */     this.vertices = new float[capacity * CPU_VERTEX_SIZE];
/*  94 */     if (this.renderable.meshPart.mesh != null) this.renderable.meshPart.mesh.dispose(); 
/*  95 */     this.renderable.meshPart.mesh = new Mesh(false, capacity, 0, CPU_ATTRIBUTES);
/*     */   }
/*     */   
/*     */   protected void allocRenderable() {
/*  99 */     this.renderable = new Renderable();
/* 100 */     this.renderable.meshPart.primitiveType = 0;
/* 101 */     this.renderable.meshPart.offset = 0;
/* 102 */     this.renderable
/* 103 */       .material = new Material(new Attribute[] { (Attribute)new BlendingAttribute(1, 771, 1.0F), (Attribute)new DepthTestAttribute(515, false), (Attribute)TextureAttribute.createDiffuse((Texture)null) });
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture) {
/* 107 */     TextureAttribute attribute = (TextureAttribute)this.renderable.material.get(TextureAttribute.Diffuse);
/* 108 */     attribute.textureDescription.texture = (GLTexture)texture;
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/* 112 */     TextureAttribute attribute = (TextureAttribute)this.renderable.material.get(TextureAttribute.Diffuse);
/* 113 */     return (Texture)attribute.textureDescription.texture;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flush(int[] offsets) {
/* 118 */     int tp = 0;
/* 119 */     for (PointSpriteControllerRenderData data : this.renderData) {
/* 120 */       ParallelArray.FloatChannel scaleChannel = data.scaleChannel;
/* 121 */       ParallelArray.FloatChannel regionChannel = data.regionChannel;
/* 122 */       ParallelArray.FloatChannel positionChannel = data.positionChannel;
/* 123 */       ParallelArray.FloatChannel colorChannel = data.colorChannel;
/* 124 */       ParallelArray.FloatChannel rotationChannel = data.rotationChannel;
/*     */       
/* 126 */       for (int p = 0; p < data.controller.particles.size; p++, tp++) {
/* 127 */         int offset = offsets[tp] * CPU_VERTEX_SIZE;
/* 128 */         int regionOffset = p * regionChannel.strideSize;
/* 129 */         int positionOffset = p * positionChannel.strideSize;
/* 130 */         int colorOffset = p * colorChannel.strideSize;
/* 131 */         int rotationOffset = p * rotationChannel.strideSize;
/*     */         
/* 133 */         this.vertices[offset + CPU_POSITION_OFFSET] = positionChannel.data[positionOffset + 0];
/* 134 */         this.vertices[offset + CPU_POSITION_OFFSET + 1] = positionChannel.data[positionOffset + 1];
/* 135 */         this.vertices[offset + CPU_POSITION_OFFSET + 2] = positionChannel.data[positionOffset + 2];
/* 136 */         this.vertices[offset + CPU_COLOR_OFFSET] = colorChannel.data[colorOffset + 0];
/* 137 */         this.vertices[offset + CPU_COLOR_OFFSET + 1] = colorChannel.data[colorOffset + 1];
/* 138 */         this.vertices[offset + CPU_COLOR_OFFSET + 2] = colorChannel.data[colorOffset + 2];
/* 139 */         this.vertices[offset + CPU_COLOR_OFFSET + 3] = colorChannel.data[colorOffset + 3];
/* 140 */         this.vertices[offset + CPU_SIZE_AND_ROTATION_OFFSET] = scaleChannel.data[p * scaleChannel.strideSize];
/* 141 */         this.vertices[offset + CPU_SIZE_AND_ROTATION_OFFSET + 1] = rotationChannel.data[rotationOffset + 0];
/*     */         
/* 143 */         this.vertices[offset + CPU_SIZE_AND_ROTATION_OFFSET + 2] = rotationChannel.data[rotationOffset + 1];
/*     */         
/* 145 */         this.vertices[offset + CPU_REGION_OFFSET] = regionChannel.data[regionOffset + 0];
/* 146 */         this.vertices[offset + CPU_REGION_OFFSET + 1] = regionChannel.data[regionOffset + 1];
/* 147 */         this.vertices[offset + CPU_REGION_OFFSET + 2] = regionChannel.data[regionOffset + 2];
/* 148 */         this.vertices[offset + CPU_REGION_OFFSET + 3] = regionChannel.data[regionOffset + 3];
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     this.renderable.meshPart.size = this.bufferedParticlesCount;
/* 153 */     this.renderable.meshPart.mesh.setVertices(this.vertices, 0, this.bufferedParticlesCount * CPU_VERTEX_SIZE);
/* 154 */     this.renderable.meshPart.update();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 159 */     if (this.bufferedParticlesCount > 0) renderables.add(((Renderable)pool.obtain()).set(this.renderable));
/*     */   
/*     */   }
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 164 */     ResourceData.SaveData data = resources.createSaveData("pointSpriteBatch");
/* 165 */     data.saveAsset(manager.getAssetFileName(getTexture()), Texture.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 170 */     ResourceData.SaveData data = resources.getSaveData("pointSpriteBatch");
/* 171 */     if (data != null) setTexture((Texture)manager.get(data.loadAsset())); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\batches\PointSpriteParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */