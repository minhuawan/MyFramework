/*     */ package com.badlogic.gdx.maps.tiled;
/*     */ 
/*     */ import com.badlogic.gdx.maps.MapLayer;
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
/*     */ public class TiledMapTileLayer
/*     */   extends MapLayer
/*     */ {
/*     */   private int width;
/*     */   private int height;
/*     */   private float tileWidth;
/*     */   private float tileHeight;
/*     */   private Cell[][] cells;
/*     */   
/*     */   public int getWidth() {
/*  34 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  39 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTileWidth() {
/*  44 */     return this.tileWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTileHeight() {
/*  49 */     return this.tileHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TiledMapTileLayer(int width, int height, int tileWidth, int tileHeight) {
/*  60 */     this.width = width;
/*  61 */     this.height = height;
/*  62 */     this.tileWidth = tileWidth;
/*  63 */     this.tileHeight = tileHeight;
/*  64 */     this.cells = new Cell[width][height];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cell getCell(int x, int y) {
/*  71 */     if (x < 0 || x >= this.width) return null; 
/*  72 */     if (y < 0 || y >= this.height) return null; 
/*  73 */     return this.cells[x][y];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCell(int x, int y, Cell cell) {
/*  82 */     if (x < 0 || x >= this.width)
/*  83 */       return;  if (y < 0 || y >= this.height)
/*  84 */       return;  this.cells[x][y] = cell;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Cell
/*     */   {
/*     */     private TiledMapTile tile;
/*     */     private boolean flipHorizontally;
/*     */     private boolean flipVertically;
/*     */     private int rotation;
/*     */     public static final int ROTATE_0 = 0;
/*     */     public static final int ROTATE_90 = 1;
/*     */     public static final int ROTATE_180 = 2;
/*     */     public static final int ROTATE_270 = 3;
/*     */     
/*     */     public TiledMapTile getTile() {
/* 100 */       return this.tile;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cell setTile(TiledMapTile tile) {
/* 108 */       this.tile = tile;
/* 109 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getFlipHorizontally() {
/* 114 */       return this.flipHorizontally;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cell setFlipHorizontally(boolean flipHorizontally) {
/* 122 */       this.flipHorizontally = flipHorizontally;
/* 123 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getFlipVertically() {
/* 128 */       return this.flipVertically;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cell setFlipVertically(boolean flipVertically) {
/* 136 */       this.flipVertically = flipVertically;
/* 137 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRotation() {
/* 142 */       return this.rotation;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cell setRotation(int rotation) {
/* 150 */       this.rotation = rotation;
/* 151 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TiledMapTileLayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */