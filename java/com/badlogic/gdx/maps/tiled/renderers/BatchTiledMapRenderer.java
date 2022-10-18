/*     */ package com.badlogic.gdx.maps.tiled.renderers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.MapLayer;
/*     */ import com.badlogic.gdx.maps.MapObject;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMap;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
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
/*     */ public abstract class BatchTiledMapRenderer
/*     */   implements TiledMapRenderer, Disposable
/*     */ {
/*     */   protected static final int NUM_VERTICES = 20;
/*     */   protected TiledMap map;
/*     */   protected float unitScale;
/*     */   protected Batch batch;
/*     */   protected Rectangle viewBounds;
/*  68 */   protected Rectangle imageBounds = new Rectangle();
/*     */   
/*     */   protected boolean ownsBatch;
/*     */   
/*  72 */   protected float[] vertices = new float[20];
/*     */   
/*     */   public TiledMap getMap() {
/*  75 */     return this.map;
/*     */   }
/*     */   
/*     */   public void setMap(TiledMap map) {
/*  79 */     this.map = map;
/*     */   }
/*     */   
/*     */   public float getUnitScale() {
/*  83 */     return this.unitScale;
/*     */   }
/*     */   
/*     */   public Batch getBatch() {
/*  87 */     return this.batch;
/*     */   }
/*     */   
/*     */   public Rectangle getViewBounds() {
/*  91 */     return this.viewBounds;
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map) {
/*  95 */     this(map, 1.0F);
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map, float unitScale) {
/*  99 */     this.map = map;
/* 100 */     this.unitScale = unitScale;
/* 101 */     this.viewBounds = new Rectangle();
/* 102 */     this.batch = (Batch)new SpriteBatch();
/* 103 */     this.ownsBatch = true;
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map, Batch batch) {
/* 107 */     this(map, 1.0F, batch);
/*     */   }
/*     */   
/*     */   public BatchTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
/* 111 */     this.map = map;
/* 112 */     this.unitScale = unitScale;
/* 113 */     this.viewBounds = new Rectangle();
/* 114 */     this.batch = batch;
/* 115 */     this.ownsBatch = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setView(OrthographicCamera camera) {
/* 120 */     this.batch.setProjectionMatrix(camera.combined);
/* 121 */     float width = camera.viewportWidth * camera.zoom;
/* 122 */     float height = camera.viewportHeight * camera.zoom;
/* 123 */     float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
/* 124 */     float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);
/* 125 */     this.viewBounds.set(camera.position.x - w / 2.0F, camera.position.y - h / 2.0F, w, h);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setView(Matrix4 projection, float x, float y, float width, float height) {
/* 130 */     this.batch.setProjectionMatrix(projection);
/* 131 */     this.viewBounds.set(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/* 136 */     beginRender();
/* 137 */     for (MapLayer layer : this.map.getLayers()) {
/* 138 */       if (layer.isVisible()) {
/* 139 */         if (layer instanceof TiledMapTileLayer) {
/* 140 */           renderTileLayer((TiledMapTileLayer)layer); continue;
/* 141 */         }  if (layer instanceof TiledMapImageLayer) {
/* 142 */           renderImageLayer((TiledMapImageLayer)layer); continue;
/*     */         } 
/* 144 */         renderObjects(layer);
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     endRender();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int[] layers) {
/* 153 */     beginRender();
/* 154 */     for (int layerIdx : layers) {
/* 155 */       MapLayer layer = this.map.getLayers().get(layerIdx);
/* 156 */       if (layer.isVisible()) {
/* 157 */         if (layer instanceof TiledMapTileLayer) {
/* 158 */           renderTileLayer((TiledMapTileLayer)layer);
/* 159 */         } else if (layer instanceof TiledMapImageLayer) {
/* 160 */           renderImageLayer((TiledMapImageLayer)layer);
/*     */         } else {
/* 162 */           renderObjects(layer);
/*     */         } 
/*     */       }
/*     */     } 
/* 166 */     endRender();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderObjects(MapLayer layer) {
/* 171 */     for (MapObject object : layer.getObjects()) {
/* 172 */       renderObject(object);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderObject(MapObject object) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderImageLayer(TiledMapImageLayer layer) {
/* 183 */     Color batchColor = this.batch.getColor();
/* 184 */     float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer
/*     */ 
/*     */         
/* 187 */         .getOpacity());
/*     */     
/* 189 */     float[] vertices = this.vertices;
/*     */     
/* 191 */     TextureRegion region = layer.getTextureRegion();
/*     */     
/* 193 */     if (region == null) {
/*     */       return;
/*     */     }
/*     */     
/* 197 */     float x = layer.getX();
/* 198 */     float y = layer.getY();
/* 199 */     float x1 = x * this.unitScale;
/* 200 */     float y1 = y * this.unitScale;
/* 201 */     float x2 = x1 + region.getRegionWidth() * this.unitScale;
/* 202 */     float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */     
/* 204 */     this.imageBounds.set(x1, y1, x2 - x1, y2 - y1);
/*     */     
/* 206 */     if (this.viewBounds.contains(this.imageBounds) || this.viewBounds.overlaps(this.imageBounds)) {
/* 207 */       float u1 = region.getU();
/* 208 */       float v1 = region.getV2();
/* 209 */       float u2 = region.getU2();
/* 210 */       float v2 = region.getV();
/*     */       
/* 212 */       vertices[0] = x1;
/* 213 */       vertices[1] = y1;
/* 214 */       vertices[2] = color;
/* 215 */       vertices[3] = u1;
/* 216 */       vertices[4] = v1;
/*     */       
/* 218 */       vertices[5] = x1;
/* 219 */       vertices[6] = y2;
/* 220 */       vertices[7] = color;
/* 221 */       vertices[8] = u1;
/* 222 */       vertices[9] = v2;
/*     */       
/* 224 */       vertices[10] = x2;
/* 225 */       vertices[11] = y2;
/* 226 */       vertices[12] = color;
/* 227 */       vertices[13] = u2;
/* 228 */       vertices[14] = v2;
/*     */       
/* 230 */       vertices[15] = x2;
/* 231 */       vertices[16] = y1;
/* 232 */       vertices[17] = color;
/* 233 */       vertices[18] = u2;
/* 234 */       vertices[19] = v1;
/*     */       
/* 236 */       this.batch.draw(region.getTexture(), vertices, 0, 20);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beginRender() {
/* 242 */     AnimatedTiledMapTile.updateAnimationBaseTime();
/* 243 */     this.batch.begin();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endRender() {
/* 248 */     this.batch.end();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 253 */     if (this.ownsBatch)
/* 254 */       this.batch.dispose(); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\renderers\BatchTiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */