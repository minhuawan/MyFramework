/*     */ package com.badlogic.gdx.graphics.g3d;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.DefaultRenderableSorter;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.TextureBinder;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.FlushablePool;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ 
/*     */ public class ModelBatch
/*     */   implements Disposable
/*     */ {
/*     */   protected Camera camera;
/*     */   
/*     */   protected static class RenderablePool
/*     */     extends FlushablePool<Renderable>
/*     */   {
/*     */     protected Renderable newObject() {
/*  48 */       return new Renderable();
/*     */     }
/*     */ 
/*     */     
/*     */     public Renderable obtain() {
/*  53 */       Renderable renderable = (Renderable)super.obtain();
/*  54 */       renderable.environment = null;
/*  55 */       renderable.material = null;
/*  56 */       renderable.meshPart.set("", null, 0, 0, 0);
/*  57 */       renderable.shader = null;
/*  58 */       return renderable;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  63 */   protected final RenderablePool renderablesPool = new RenderablePool();
/*     */   
/*  65 */   protected final Array<Renderable> renderables = new Array();
/*     */ 
/*     */   
/*     */   protected final RenderContext context;
/*     */ 
/*     */   
/*     */   private final boolean ownContext;
/*     */   
/*     */   protected final ShaderProvider shaderProvider;
/*     */   
/*     */   protected final RenderableSorter sorter;
/*     */ 
/*     */   
/*     */   public ModelBatch(RenderContext context, ShaderProvider shaderProvider, RenderableSorter sorter) {
/*  79 */     this.sorter = (sorter == null) ? (RenderableSorter)new DefaultRenderableSorter() : sorter;
/*  80 */     this.ownContext = (context == null);
/*  81 */     this.context = (context == null) ? new RenderContext((TextureBinder)new DefaultTextureBinder(1, 1)) : context;
/*  82 */     this.shaderProvider = (shaderProvider == null) ? (ShaderProvider)new DefaultShaderProvider() : shaderProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(RenderContext context, ShaderProvider shaderProvider) {
/*  89 */     this(context, shaderProvider, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(RenderContext context, RenderableSorter sorter) {
/*  96 */     this(context, null, sorter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(RenderContext context) {
/* 102 */     this(context, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(ShaderProvider shaderProvider, RenderableSorter sorter) {
/* 109 */     this(null, shaderProvider, sorter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(RenderableSorter sorter) {
/* 115 */     this(null, null, sorter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(ShaderProvider shaderProvider) {
/* 121 */     this(null, shaderProvider, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(FileHandle vertexShader, FileHandle fragmentShader) {
/* 129 */     this(null, (ShaderProvider)new DefaultShaderProvider(vertexShader, fragmentShader), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelBatch(String vertexShader, String fragmentShader) {
/* 137 */     this(null, (ShaderProvider)new DefaultShaderProvider(vertexShader, fragmentShader), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBatch() {
/* 142 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void begin(Camera cam) {
/* 150 */     if (this.camera != null) throw new GdxRuntimeException("Call end() first."); 
/* 151 */     this.camera = cam;
/* 152 */     if (this.ownContext) this.context.begin();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCamera(Camera cam) {
/* 159 */     if (this.camera == null) throw new GdxRuntimeException("Call begin() first."); 
/* 160 */     if (this.renderables.size > 0) flush(); 
/* 161 */     this.camera = cam;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Camera getCamera() {
/* 168 */     return this.camera;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ownsRenderContext() {
/* 176 */     return this.ownContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderContext getRenderContext() {
/* 181 */     return this.context;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShaderProvider getShaderProvider() {
/* 186 */     return this.shaderProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderableSorter getRenderableSorter() {
/* 191 */     return this.sorter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {
/* 197 */     this.sorter.sort(this.camera, this.renderables);
/* 198 */     Shader currentShader = null;
/* 199 */     for (int i = 0; i < this.renderables.size; i++) {
/* 200 */       Renderable renderable = (Renderable)this.renderables.get(i);
/* 201 */       if (currentShader != renderable.shader) {
/* 202 */         if (currentShader != null) currentShader.end(); 
/* 203 */         currentShader = renderable.shader;
/* 204 */         currentShader.begin(this.camera, this.context);
/*     */       } 
/* 206 */       currentShader.render(renderable);
/*     */     } 
/* 208 */     if (currentShader != null) currentShader.end(); 
/* 209 */     this.renderablesPool.flush();
/* 210 */     this.renderables.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() {
/* 217 */     flush();
/* 218 */     if (this.ownContext) this.context.end(); 
/* 219 */     this.camera = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Renderable renderable) {
/* 226 */     renderable.shader = this.shaderProvider.getShader(renderable);
/* 227 */     renderable.meshPart.mesh.setAutoBind(false);
/* 228 */     this.renderables.add(renderable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(RenderableProvider renderableProvider) {
/* 235 */     int offset = this.renderables.size;
/* 236 */     renderableProvider.getRenderables(this.renderables, (Pool<Renderable>)this.renderablesPool);
/* 237 */     for (int i = offset; i < this.renderables.size; i++) {
/* 238 */       Renderable renderable = (Renderable)this.renderables.get(i);
/* 239 */       renderable.shader = this.shaderProvider.getShader(renderable);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends RenderableProvider> void render(Iterable<T> renderableProviders) {
/* 247 */     for (RenderableProvider renderableProvider : renderableProviders) {
/* 248 */       render(renderableProvider);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(RenderableProvider renderableProvider, Environment environment) {
/* 257 */     int offset = this.renderables.size;
/* 258 */     renderableProvider.getRenderables(this.renderables, (Pool<Renderable>)this.renderablesPool);
/* 259 */     for (int i = offset; i < this.renderables.size; i++) {
/* 260 */       Renderable renderable = (Renderable)this.renderables.get(i);
/* 261 */       renderable.environment = environment;
/* 262 */       renderable.shader = this.shaderProvider.getShader(renderable);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends RenderableProvider> void render(Iterable<T> renderableProviders, Environment environment) {
/* 272 */     for (RenderableProvider renderableProvider : renderableProviders) {
/* 273 */       render(renderableProvider, environment);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(RenderableProvider renderableProvider, Shader shader) {
/* 282 */     int offset = this.renderables.size;
/* 283 */     renderableProvider.getRenderables(this.renderables, (Pool<Renderable>)this.renderablesPool);
/* 284 */     for (int i = offset; i < this.renderables.size; i++) {
/* 285 */       Renderable renderable = (Renderable)this.renderables.get(i);
/* 286 */       renderable.shader = shader;
/* 287 */       renderable.shader = this.shaderProvider.getShader(renderable);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends RenderableProvider> void render(Iterable<T> renderableProviders, Shader shader) {
/* 297 */     for (RenderableProvider renderableProvider : renderableProviders) {
/* 298 */       render(renderableProvider, shader);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(RenderableProvider renderableProvider, Environment environment, Shader shader) {
/* 309 */     int offset = this.renderables.size;
/* 310 */     renderableProvider.getRenderables(this.renderables, (Pool<Renderable>)this.renderablesPool);
/* 311 */     for (int i = offset; i < this.renderables.size; i++) {
/* 312 */       Renderable renderable = (Renderable)this.renderables.get(i);
/* 313 */       renderable.environment = environment;
/* 314 */       renderable.shader = shader;
/* 315 */       renderable.shader = this.shaderProvider.getShader(renderable);
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
/*     */   public <T extends RenderableProvider> void render(Iterable<T> renderableProviders, Environment environment, Shader shader) {
/* 328 */     for (RenderableProvider renderableProvider : renderableProviders) {
/* 329 */       render(renderableProvider, environment, shader);
/*     */     }
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 334 */     this.shaderProvider.dispose();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\ModelBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */