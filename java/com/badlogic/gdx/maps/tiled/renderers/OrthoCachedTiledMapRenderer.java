/*     */ package com.badlogic.gdx.maps.tiled.renderers;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteCache;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.MapLayer;
/*     */ import com.badlogic.gdx.maps.MapLayers;
/*     */ import com.badlogic.gdx.maps.MapObject;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMap;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTile;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
/*     */ public class OrthoCachedTiledMapRenderer
/*     */   implements TiledMapRenderer, Disposable
/*     */ {
/*     */   private static final float tolerance = 1.0E-5F;
/*     */   protected static final int NUM_VERTICES = 20;
/*     */   protected final TiledMap map;
/*     */   protected final SpriteCache spriteCache;
/*  55 */   protected final float[] vertices = new float[20];
/*     */   
/*     */   protected boolean blending;
/*     */   protected float unitScale;
/*  59 */   protected final Rectangle viewBounds = new Rectangle();
/*  60 */   protected final Rectangle cacheBounds = new Rectangle();
/*     */   
/*  62 */   protected float overCache = 0.5F; protected float maxTileWidth; protected float maxTileHeight; protected boolean cached;
/*     */   protected int count;
/*     */   protected boolean canCacheMoreN;
/*     */   protected boolean canCacheMoreE;
/*     */   protected boolean canCacheMoreW;
/*     */   protected boolean canCacheMoreS;
/*     */   
/*     */   public OrthoCachedTiledMapRenderer(TiledMap map) {
/*  70 */     this(map, 1.0F, 2000);
/*     */   }
/*     */ 
/*     */   
/*     */   public OrthoCachedTiledMapRenderer(TiledMap map, float unitScale) {
/*  75 */     this(map, unitScale, 2000);
/*     */   }
/*     */ 
/*     */   
/*     */   public OrthoCachedTiledMapRenderer(TiledMap map, float unitScale, int cacheSize) {
/*  80 */     this.map = map;
/*  81 */     this.unitScale = unitScale;
/*  82 */     this.spriteCache = new SpriteCache(cacheSize, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setView(OrthographicCamera camera) {
/*  87 */     this.spriteCache.setProjectionMatrix(camera.combined);
/*  88 */     float width = camera.viewportWidth * camera.zoom + this.maxTileWidth * 2.0F * this.unitScale;
/*  89 */     float height = camera.viewportHeight * camera.zoom + this.maxTileHeight * 2.0F * this.unitScale;
/*  90 */     this.viewBounds.set(camera.position.x - width / 2.0F, camera.position.y - height / 2.0F, width, height);
/*     */     
/*  92 */     if ((this.canCacheMoreW && this.viewBounds.x < this.cacheBounds.x - 1.0E-5F) || (this.canCacheMoreS && this.viewBounds.y < this.cacheBounds.y - 1.0E-5F) || (this.canCacheMoreE && this.viewBounds.x + this.viewBounds.width > this.cacheBounds.x + this.cacheBounds.width + 1.0E-5F) || (this.canCacheMoreN && this.viewBounds.y + this.viewBounds.height > this.cacheBounds.y + this.cacheBounds.height + 1.0E-5F))
/*     */     {
/*     */ 
/*     */       
/*  96 */       this.cached = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setView(Matrix4 projection, float x, float y, float width, float height) {
/* 101 */     this.spriteCache.setProjectionMatrix(projection);
/* 102 */     x -= this.maxTileWidth * this.unitScale;
/* 103 */     y -= this.maxTileHeight * this.unitScale;
/* 104 */     width += this.maxTileWidth * 2.0F * this.unitScale;
/* 105 */     height += this.maxTileHeight * 2.0F * this.unitScale;
/* 106 */     this.viewBounds.set(x, y, width, height);
/*     */     
/* 108 */     if ((this.canCacheMoreW && this.viewBounds.x < this.cacheBounds.x - 1.0E-5F) || (this.canCacheMoreS && this.viewBounds.y < this.cacheBounds.y - 1.0E-5F) || (this.canCacheMoreE && this.viewBounds.x + this.viewBounds.width > this.cacheBounds.x + this.cacheBounds.width + 1.0E-5F) || (this.canCacheMoreN && this.viewBounds.y + this.viewBounds.height > this.cacheBounds.y + this.cacheBounds.height + 1.0E-5F))
/*     */     {
/*     */ 
/*     */       
/* 112 */       this.cached = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public void render() {
/* 117 */     if (!this.cached) {
/* 118 */       this.cached = true;
/* 119 */       this.count = 0;
/* 120 */       this.spriteCache.clear();
/*     */       
/* 122 */       float extraWidth = this.viewBounds.width * this.overCache;
/* 123 */       float extraHeight = this.viewBounds.height * this.overCache;
/* 124 */       this.viewBounds.x -= extraWidth;
/* 125 */       this.viewBounds.y -= extraHeight;
/* 126 */       this.viewBounds.width += extraWidth * 2.0F;
/* 127 */       this.viewBounds.height += extraHeight * 2.0F;
/*     */       
/* 129 */       for (MapLayer layer : this.map.getLayers()) {
/* 130 */         this.spriteCache.beginCache();
/* 131 */         if (layer instanceof TiledMapTileLayer) {
/* 132 */           renderTileLayer((TiledMapTileLayer)layer);
/* 133 */         } else if (layer instanceof TiledMapImageLayer) {
/* 134 */           renderImageLayer((TiledMapImageLayer)layer);
/*     */         } 
/* 136 */         this.spriteCache.endCache();
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     if (this.blending) {
/* 141 */       Gdx.gl.glEnable(3042);
/* 142 */       Gdx.gl.glBlendFunc(770, 771);
/*     */     } 
/* 144 */     this.spriteCache.begin();
/* 145 */     MapLayers mapLayers = this.map.getLayers();
/* 146 */     for (int i = 0, j = mapLayers.getCount(); i < j; i++) {
/* 147 */       MapLayer layer = mapLayers.get(i);
/* 148 */       if (layer.isVisible()) {
/* 149 */         this.spriteCache.draw(i);
/* 150 */         renderObjects(layer);
/*     */       } 
/*     */     } 
/* 153 */     this.spriteCache.end();
/* 154 */     if (this.blending) Gdx.gl.glDisable(3042);
/*     */   
/*     */   }
/*     */   
/*     */   public void render(int[] layers) {
/* 159 */     if (!this.cached) {
/* 160 */       this.cached = true;
/* 161 */       this.count = 0;
/* 162 */       this.spriteCache.clear();
/*     */       
/* 164 */       float extraWidth = this.viewBounds.width * this.overCache;
/* 165 */       float extraHeight = this.viewBounds.height * this.overCache;
/* 166 */       this.viewBounds.x -= extraWidth;
/* 167 */       this.viewBounds.y -= extraHeight;
/* 168 */       this.viewBounds.width += extraWidth * 2.0F;
/* 169 */       this.viewBounds.height += extraHeight * 2.0F;
/*     */       
/* 171 */       for (MapLayer layer : this.map.getLayers()) {
/* 172 */         this.spriteCache.beginCache();
/* 173 */         if (layer instanceof TiledMapTileLayer) {
/* 174 */           renderTileLayer((TiledMapTileLayer)layer);
/* 175 */         } else if (layer instanceof TiledMapImageLayer) {
/* 176 */           renderImageLayer((TiledMapImageLayer)layer);
/*     */         } 
/* 178 */         this.spriteCache.endCache();
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     if (this.blending) {
/* 183 */       Gdx.gl.glEnable(3042);
/* 184 */       Gdx.gl.glBlendFunc(770, 771);
/*     */     } 
/* 186 */     this.spriteCache.begin();
/* 187 */     MapLayers mapLayers = this.map.getLayers();
/* 188 */     for (int i : layers) {
/* 189 */       MapLayer layer = mapLayers.get(i);
/* 190 */       if (layer.isVisible()) {
/* 191 */         this.spriteCache.draw(i);
/* 192 */         renderObjects(layer);
/*     */       } 
/*     */     } 
/* 195 */     this.spriteCache.end();
/* 196 */     if (this.blending) Gdx.gl.glDisable(3042);
/*     */   
/*     */   }
/*     */   
/*     */   public void renderObjects(MapLayer layer) {
/* 201 */     for (MapObject object : layer.getObjects()) {
/* 202 */       renderObject(object);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderObject(MapObject object) {}
/*     */ 
/*     */   
/*     */   public void renderTileLayer(TiledMapTileLayer layer) {
/* 212 */     float color = Color.toFloatBits(1.0F, 1.0F, 1.0F, layer.getOpacity());
/*     */     
/* 214 */     int layerWidth = layer.getWidth();
/* 215 */     int layerHeight = layer.getHeight();
/*     */     
/* 217 */     float layerTileWidth = layer.getTileWidth() * this.unitScale;
/* 218 */     float layerTileHeight = layer.getTileHeight() * this.unitScale;
/*     */     
/* 220 */     int col1 = Math.max(0, (int)(this.cacheBounds.x / layerTileWidth));
/* 221 */     int col2 = Math.min(layerWidth, (int)((this.cacheBounds.x + this.cacheBounds.width + layerTileWidth) / layerTileWidth));
/*     */     
/* 223 */     int row1 = Math.max(0, (int)(this.cacheBounds.y / layerTileHeight));
/* 224 */     int row2 = Math.min(layerHeight, (int)((this.cacheBounds.y + this.cacheBounds.height + layerTileHeight) / layerTileHeight));
/*     */     
/* 226 */     this.canCacheMoreN = (row2 < layerHeight);
/* 227 */     this.canCacheMoreE = (col2 < layerWidth);
/* 228 */     this.canCacheMoreW = (col1 > 0);
/* 229 */     this.canCacheMoreS = (row1 > 0);
/*     */     
/* 231 */     float[] vertices = this.vertices;
/* 232 */     for (int row = row2; row >= row1; row--) {
/* 233 */       for (int col = col1; col < col2; col++) {
/* 234 */         TiledMapTileLayer.Cell cell = layer.getCell(col, row);
/* 235 */         if (cell != null) {
/*     */           
/* 237 */           TiledMapTile tile = cell.getTile();
/* 238 */           if (tile != null) {
/*     */             
/* 240 */             this.count++;
/* 241 */             boolean flipX = cell.getFlipHorizontally();
/* 242 */             boolean flipY = cell.getFlipVertically();
/* 243 */             int rotations = cell.getRotation();
/*     */             
/* 245 */             TextureRegion region = tile.getTextureRegion();
/* 246 */             Texture texture = region.getTexture();
/*     */             
/* 248 */             float x1 = col * layerTileWidth + tile.getOffsetX() * this.unitScale;
/* 249 */             float y1 = row * layerTileHeight + tile.getOffsetY() * this.unitScale;
/* 250 */             float x2 = x1 + region.getRegionWidth() * this.unitScale;
/* 251 */             float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */             
/* 253 */             float adjustX = 0.5F / texture.getWidth();
/* 254 */             float adjustY = 0.5F / texture.getHeight();
/* 255 */             float u1 = region.getU() + adjustX;
/* 256 */             float v1 = region.getV2() - adjustY;
/* 257 */             float u2 = region.getU2() - adjustX;
/* 258 */             float v2 = region.getV() + adjustY;
/*     */             
/* 260 */             vertices[0] = x1;
/* 261 */             vertices[1] = y1;
/* 262 */             vertices[2] = color;
/* 263 */             vertices[3] = u1;
/* 264 */             vertices[4] = v1;
/*     */             
/* 266 */             vertices[5] = x1;
/* 267 */             vertices[6] = y2;
/* 268 */             vertices[7] = color;
/* 269 */             vertices[8] = u1;
/* 270 */             vertices[9] = v2;
/*     */             
/* 272 */             vertices[10] = x2;
/* 273 */             vertices[11] = y2;
/* 274 */             vertices[12] = color;
/* 275 */             vertices[13] = u2;
/* 276 */             vertices[14] = v2;
/*     */             
/* 278 */             vertices[15] = x2;
/* 279 */             vertices[16] = y1;
/* 280 */             vertices[17] = color;
/* 281 */             vertices[18] = u2;
/* 282 */             vertices[19] = v1;
/*     */             
/* 284 */             if (flipX) {
/* 285 */               float temp = vertices[3];
/* 286 */               vertices[3] = vertices[13];
/* 287 */               vertices[13] = temp;
/* 288 */               temp = vertices[8];
/* 289 */               vertices[8] = vertices[18];
/* 290 */               vertices[18] = temp;
/*     */             } 
/* 292 */             if (flipY) {
/* 293 */               float temp = vertices[4];
/* 294 */               vertices[4] = vertices[14];
/* 295 */               vertices[14] = temp;
/* 296 */               temp = vertices[9];
/* 297 */               vertices[9] = vertices[19];
/* 298 */               vertices[19] = temp;
/*     */             } 
/* 300 */             if (rotations != 0) {
/* 301 */               float f1; float tempU; float tempV; float f4; float f3; float f2; switch (rotations) {
/*     */                 case 1:
/* 303 */                   f1 = vertices[4];
/* 304 */                   vertices[4] = vertices[9];
/* 305 */                   vertices[9] = vertices[14];
/* 306 */                   vertices[14] = vertices[19];
/* 307 */                   vertices[19] = f1;
/*     */                   
/* 309 */                   f4 = vertices[3];
/* 310 */                   vertices[3] = vertices[8];
/* 311 */                   vertices[8] = vertices[13];
/* 312 */                   vertices[13] = vertices[18];
/* 313 */                   vertices[18] = f4;
/*     */                   break;
/*     */                 
/*     */                 case 2:
/* 317 */                   tempU = vertices[3];
/* 318 */                   vertices[3] = vertices[13];
/* 319 */                   vertices[13] = tempU;
/* 320 */                   tempU = vertices[8];
/* 321 */                   vertices[8] = vertices[18];
/* 322 */                   vertices[18] = tempU;
/* 323 */                   f3 = vertices[4];
/* 324 */                   vertices[4] = vertices[14];
/* 325 */                   vertices[14] = f3;
/* 326 */                   f3 = vertices[9];
/* 327 */                   vertices[9] = vertices[19];
/* 328 */                   vertices[19] = f3;
/*     */                   break;
/*     */                 
/*     */                 case 3:
/* 332 */                   tempV = vertices[4];
/* 333 */                   vertices[4] = vertices[19];
/* 334 */                   vertices[19] = vertices[14];
/* 335 */                   vertices[14] = vertices[9];
/* 336 */                   vertices[9] = tempV;
/*     */                   
/* 338 */                   f2 = vertices[3];
/* 339 */                   vertices[3] = vertices[18];
/* 340 */                   vertices[18] = vertices[13];
/* 341 */                   vertices[13] = vertices[8];
/* 342 */                   vertices[8] = f2;
/*     */                   break;
/*     */               } 
/*     */             
/*     */             } 
/* 347 */             this.spriteCache.add(texture, vertices, 0, 20);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void renderImageLayer(TiledMapImageLayer layer) {
/* 354 */     float color = Color.toFloatBits(1.0F, 1.0F, 1.0F, layer.getOpacity());
/* 355 */     float[] vertices = this.vertices;
/*     */     
/* 357 */     TextureRegion region = layer.getTextureRegion();
/*     */     
/* 359 */     if (region == null) {
/*     */       return;
/*     */     }
/*     */     
/* 363 */     float x = layer.getX();
/* 364 */     float y = layer.getY();
/* 365 */     float x1 = x * this.unitScale;
/* 366 */     float y1 = y * this.unitScale;
/* 367 */     float x2 = x1 + region.getRegionWidth() * this.unitScale;
/* 368 */     float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */     
/* 370 */     float u1 = region.getU();
/* 371 */     float v1 = region.getV2();
/* 372 */     float u2 = region.getU2();
/* 373 */     float v2 = region.getV();
/*     */     
/* 375 */     vertices[0] = x1;
/* 376 */     vertices[1] = y1;
/* 377 */     vertices[2] = color;
/* 378 */     vertices[3] = u1;
/* 379 */     vertices[4] = v1;
/*     */     
/* 381 */     vertices[5] = x1;
/* 382 */     vertices[6] = y2;
/* 383 */     vertices[7] = color;
/* 384 */     vertices[8] = u1;
/* 385 */     vertices[9] = v2;
/*     */     
/* 387 */     vertices[10] = x2;
/* 388 */     vertices[11] = y2;
/* 389 */     vertices[12] = color;
/* 390 */     vertices[13] = u2;
/* 391 */     vertices[14] = v2;
/*     */     
/* 393 */     vertices[15] = x2;
/* 394 */     vertices[16] = y1;
/* 395 */     vertices[17] = color;
/* 396 */     vertices[18] = u2;
/* 397 */     vertices[19] = v1;
/*     */     
/* 399 */     this.spriteCache.add(region.getTexture(), vertices, 0, 20);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateCache() {
/* 404 */     this.cached = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCached() {
/* 409 */     return this.cached;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverCache(float overCache) {
/* 417 */     this.overCache = overCache;
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
/*     */   public void setMaxTileSize(float maxPixelWidth, float maxPixelHeight) {
/* 429 */     this.maxTileWidth = maxPixelWidth;
/* 430 */     this.maxTileHeight = maxPixelHeight;
/*     */   }
/*     */   
/*     */   public void setBlending(boolean blending) {
/* 434 */     this.blending = blending;
/*     */   }
/*     */   
/*     */   public SpriteCache getSpriteCache() {
/* 438 */     return this.spriteCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 443 */     this.spriteCache.dispose();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\renderers\OrthoCachedTiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */