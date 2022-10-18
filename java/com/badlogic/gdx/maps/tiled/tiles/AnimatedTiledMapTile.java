/*     */ package com.badlogic.gdx.maps.tiled.tiles;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.MapProperties;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTile;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
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
/*     */ public class AnimatedTiledMapTile
/*     */   implements TiledMapTile
/*     */ {
/*  30 */   private static long lastTiledMapRenderTime = 0L;
/*     */   
/*     */   private int id;
/*     */   
/*  34 */   private TiledMapTile.BlendMode blendMode = TiledMapTile.BlendMode.ALPHA;
/*     */   
/*     */   private MapProperties properties;
/*     */   
/*     */   private StaticTiledMapTile[] frameTiles;
/*     */   
/*     */   private int[] animationIntervals;
/*  41 */   private int frameCount = 0;
/*     */   private int loopDuration;
/*  43 */   private static final long initialTimeOffset = TimeUtils.millis();
/*     */ 
/*     */   
/*     */   public int getId() {
/*  47 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(int id) {
/*  52 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public TiledMapTile.BlendMode getBlendMode() {
/*  57 */     return this.blendMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlendMode(TiledMapTile.BlendMode blendMode) {
/*  62 */     this.blendMode = blendMode;
/*     */   }
/*     */   
/*     */   public int getCurrentFrameIndex() {
/*  66 */     int currentTime = (int)(lastTiledMapRenderTime % this.loopDuration);
/*     */     
/*  68 */     for (int i = 0; i < this.animationIntervals.length; i++) {
/*  69 */       int animationInterval = this.animationIntervals[i];
/*  70 */       if (currentTime <= animationInterval) return i; 
/*  71 */       currentTime -= animationInterval;
/*     */     } 
/*     */     
/*  74 */     throw new GdxRuntimeException("Could not determine current animation frame in AnimatedTiledMapTile.  This should never happen.");
/*     */   }
/*     */ 
/*     */   
/*     */   public TiledMapTile getCurrentFrame() {
/*  79 */     return this.frameTiles[getCurrentFrameIndex()];
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureRegion getTextureRegion() {
/*  84 */     return getCurrentFrame().getTextureRegion();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTextureRegion(TextureRegion textureRegion) {
/*  89 */     throw new GdxRuntimeException("Cannot set the texture region of AnimatedTiledMapTile.");
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffsetX() {
/*  94 */     return getCurrentFrame().getOffsetX();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOffsetX(float offsetX) {
/*  99 */     throw new GdxRuntimeException("Cannot set offset of AnimatedTiledMapTile.");
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffsetY() {
/* 104 */     return getCurrentFrame().getOffsetY();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOffsetY(float offsetY) {
/* 109 */     throw new GdxRuntimeException("Cannot set offset of AnimatedTiledMapTile.");
/*     */   }
/*     */   
/*     */   public int[] getAnimationIntervals() {
/* 113 */     return this.animationIntervals;
/*     */   }
/*     */   
/*     */   public void setAnimationIntervals(int[] intervals) {
/* 117 */     if (intervals.length == this.animationIntervals.length) {
/* 118 */       this.animationIntervals = intervals;
/*     */       
/* 120 */       this.loopDuration = 0;
/* 121 */       for (int i = 0; i < intervals.length; i++) {
/* 122 */         this.loopDuration += intervals[i];
/*     */       }
/*     */     } else {
/*     */       
/* 126 */       throw new GdxRuntimeException("Cannot set " + intervals.length + " frame intervals. The given int[] must have a size of " + this.animationIntervals.length + ".");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapProperties getProperties() {
/* 133 */     if (this.properties == null) {
/* 134 */       this.properties = new MapProperties();
/*     */     }
/* 136 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateAnimationBaseTime() {
/* 142 */     lastTiledMapRenderTime = TimeUtils.millis() - initialTimeOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimatedTiledMapTile(float interval, Array<StaticTiledMapTile> frameTiles) {
/* 150 */     this.frameTiles = new StaticTiledMapTile[frameTiles.size];
/* 151 */     this.frameCount = frameTiles.size;
/*     */     
/* 153 */     this.loopDuration = frameTiles.size * (int)(interval * 1000.0F);
/* 154 */     this.animationIntervals = new int[frameTiles.size];
/* 155 */     for (int i = 0; i < frameTiles.size; i++) {
/* 156 */       this.frameTiles[i] = (StaticTiledMapTile)frameTiles.get(i);
/* 157 */       this.animationIntervals[i] = (int)(interval * 1000.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimatedTiledMapTile(IntArray intervals, Array<StaticTiledMapTile> frameTiles) {
/* 166 */     this.frameTiles = new StaticTiledMapTile[frameTiles.size];
/* 167 */     this.frameCount = frameTiles.size;
/*     */     
/* 169 */     this.animationIntervals = intervals.toArray();
/* 170 */     this.loopDuration = 0;
/*     */     
/* 172 */     for (int i = 0; i < intervals.size; i++) {
/* 173 */       this.frameTiles[i] = (StaticTiledMapTile)frameTiles.get(i);
/* 174 */       this.loopDuration += intervals.get(i);
/*     */     } 
/*     */   }
/*     */   
/*     */   public StaticTiledMapTile[] getFrameTiles() {
/* 179 */     return this.frameTiles;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\tiles\AnimatedTiledMapTile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */