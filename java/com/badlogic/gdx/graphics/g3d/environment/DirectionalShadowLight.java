/*     */ package com.badlogic.gdx.graphics.g3d.environment;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
/*     */ import com.badlogic.gdx.graphics.glutils.FrameBuffer;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DirectionalShadowLight
/*     */   extends DirectionalLight
/*     */   implements ShadowMap, Disposable
/*     */ {
/*     */   protected FrameBuffer fbo;
/*     */   protected Camera cam;
/*     */   protected float halfDepth;
/*     */   protected float halfHeight;
/*  38 */   protected final Vector3 tmpV = new Vector3();
/*     */   
/*     */   protected final TextureDescriptor textureDesc;
/*     */ 
/*     */   
/*     */   public DirectionalShadowLight(int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight, float shadowNear, float shadowFar) {
/*  44 */     this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, shadowMapWidth, shadowMapHeight, true);
/*  45 */     this.cam = (Camera)new OrthographicCamera(shadowViewportWidth, shadowViewportHeight);
/*  46 */     this.cam.near = shadowNear;
/*  47 */     this.cam.far = shadowFar;
/*  48 */     this.halfHeight = shadowViewportHeight * 0.5F;
/*  49 */     this.halfDepth = shadowNear + 0.5F * (shadowFar - shadowNear);
/*  50 */     this.textureDesc = new TextureDescriptor();
/*  51 */     this.textureDesc.minFilter = this.textureDesc.magFilter = Texture.TextureFilter.Nearest;
/*  52 */     this.textureDesc.uWrap = this.textureDesc.vWrap = Texture.TextureWrap.ClampToEdge;
/*     */   }
/*     */   
/*     */   public void update(Camera camera) {
/*  56 */     update(this.tmpV.set(camera.direction).scl(this.halfHeight), camera.direction);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Vector3 center, Vector3 forward) {
/*  61 */     this.cam.position.set(this.direction).scl(-this.halfDepth).add(center);
/*  62 */     this.cam.direction.set(this.direction).nor();
/*     */     
/*  64 */     this.cam.normalizeUp();
/*  65 */     this.cam.update();
/*     */   }
/*     */   
/*     */   public void begin(Camera camera) {
/*  69 */     update(camera);
/*  70 */     begin();
/*     */   }
/*     */   
/*     */   public void begin(Vector3 center, Vector3 forward) {
/*  74 */     update(center, forward);
/*  75 */     begin();
/*     */   }
/*     */   
/*     */   public void begin() {
/*  79 */     int w = this.fbo.getWidth();
/*  80 */     int h = this.fbo.getHeight();
/*  81 */     this.fbo.begin();
/*  82 */     Gdx.gl.glViewport(0, 0, w, h);
/*  83 */     Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
/*  84 */     Gdx.gl.glClear(16640);
/*  85 */     Gdx.gl.glEnable(3089);
/*  86 */     Gdx.gl.glScissor(1, 1, w - 2, h - 2);
/*     */   }
/*     */   
/*     */   public void end() {
/*  90 */     Gdx.gl.glDisable(3089);
/*  91 */     this.fbo.end();
/*     */   }
/*     */   
/*     */   public FrameBuffer getFrameBuffer() {
/*  95 */     return this.fbo;
/*     */   }
/*     */   
/*     */   public Camera getCamera() {
/*  99 */     return this.cam;
/*     */   }
/*     */ 
/*     */   
/*     */   public Matrix4 getProjViewTrans() {
/* 104 */     return this.cam.combined;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureDescriptor getDepthMap() {
/* 109 */     this.textureDesc.texture = this.fbo.getColorBufferTexture();
/* 110 */     return this.textureDesc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 115 */     if (this.fbo != null) this.fbo.dispose(); 
/* 116 */     this.fbo = null;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\environment\DirectionalShadowLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */