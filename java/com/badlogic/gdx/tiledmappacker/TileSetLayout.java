/*    */ package com.badlogic.gdx.tiledmappacker;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
/*    */ import com.badlogic.gdx.math.Vector2;
/*    */ import com.badlogic.gdx.utils.IntMap;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TileSetLayout
/*    */ {
/*    */   public final BufferedImage image;
/*    */   private final IntMap<Vector2> imageTilePositions;
/*    */   private int numRows;
/*    */   private int numCols;
/*    */   public final int numTiles;
/*    */   public final int firstgid;
/*    */   
/*    */   protected TileSetLayout(int firstgid, TiledMapTileSet tileset, FileHandle baseDir) throws IOException {
/* 45 */     int tileWidth = ((Integer)tileset.getProperties().get("tilewidth", Integer.class)).intValue();
/* 46 */     int tileHeight = ((Integer)tileset.getProperties().get("tileheight", Integer.class)).intValue();
/* 47 */     int margin = ((Integer)tileset.getProperties().get("margin", Integer.class)).intValue();
/* 48 */     int spacing = ((Integer)tileset.getProperties().get("spacing", Integer.class)).intValue();
/*    */     
/* 50 */     this.firstgid = firstgid;
/*    */     
/* 52 */     this.image = ImageIO.read(baseDir.child((String)tileset.getProperties().get("imagesource", String.class)).read());
/*    */     
/* 54 */     this.imageTilePositions = new IntMap();
/*    */ 
/*    */     
/* 57 */     int tile = 0;
/* 58 */     this.numRows = 0;
/* 59 */     this.numCols = 0;
/*    */     
/* 61 */     int stopWidth = this.image.getWidth() - tileWidth;
/* 62 */     int stopHeight = this.image.getHeight() - tileHeight;
/*    */     int y;
/* 64 */     for (y = margin; y <= stopHeight; y += tileHeight + spacing) {
/* 65 */       int x; for (x = margin; x <= stopWidth; x += tileWidth + spacing) {
/* 66 */         if (y == margin) this.numCols++; 
/* 67 */         this.imageTilePositions.put(tile, new Vector2(x, y));
/* 68 */         tile++;
/*    */       } 
/* 70 */       this.numRows++;
/*    */     } 
/*    */     
/* 73 */     this.numTiles = this.numRows * this.numCols;
/*    */   }
/*    */   
/*    */   public int getNumRows() {
/* 77 */     return this.numRows;
/*    */   }
/*    */   
/*    */   public int getNumCols() {
/* 81 */     return this.numCols;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vector2 getLocation(int tile) {
/* 86 */     return (Vector2)this.imageTilePositions.get(tile - this.firstgid);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tiledmappacker\TileSetLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */