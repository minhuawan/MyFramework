/*     */ package com.badlogic.gdx.maps.tiled.tiles;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.MapProperties;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTile;
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
/*     */ public class StaticTiledMapTile
/*     */   implements TiledMapTile
/*     */ {
/*     */   private int id;
/*  28 */   private TiledMapTile.BlendMode blendMode = TiledMapTile.BlendMode.ALPHA;
/*     */   
/*     */   private MapProperties properties;
/*     */   
/*     */   private TextureRegion textureRegion;
/*     */   
/*     */   private float offsetX;
/*     */   
/*     */   private float offsetY;
/*     */ 
/*     */   
/*     */   public int getId() {
/*  40 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(int id) {
/*  45 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public TiledMapTile.BlendMode getBlendMode() {
/*  50 */     return this.blendMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlendMode(TiledMapTile.BlendMode blendMode) {
/*  55 */     this.blendMode = blendMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public MapProperties getProperties() {
/*  60 */     if (this.properties == null) {
/*  61 */       this.properties = new MapProperties();
/*     */     }
/*  63 */     return this.properties;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureRegion getTextureRegion() {
/*  68 */     return this.textureRegion;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTextureRegion(TextureRegion textureRegion) {
/*  73 */     this.textureRegion = textureRegion;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffsetX() {
/*  78 */     return this.offsetX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOffsetX(float offsetX) {
/*  83 */     this.offsetX = offsetX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffsetY() {
/*  88 */     return this.offsetY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOffsetY(float offsetY) {
/*  93 */     this.offsetY = offsetY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticTiledMapTile(TextureRegion textureRegion) {
/* 100 */     this.textureRegion = textureRegion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticTiledMapTile(StaticTiledMapTile copy) {
/* 107 */     if (copy.properties != null) {
/* 108 */       getProperties().putAll(copy.properties);
/*     */     }
/* 110 */     this.textureRegion = copy.textureRegion;
/* 111 */     this.id = copy.id;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\tiles\StaticTiledMapTile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */