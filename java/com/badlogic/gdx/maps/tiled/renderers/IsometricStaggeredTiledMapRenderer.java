/*     */ package com.badlogic.gdx.maps.tiled.renderers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMap;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTile;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
/*     */ public class IsometricStaggeredTiledMapRenderer
/*     */   extends BatchTiledMapRenderer
/*     */ {
/*     */   public IsometricStaggeredTiledMapRenderer(TiledMap map) {
/*  32 */     super(map);
/*     */   }
/*     */   
/*     */   public IsometricStaggeredTiledMapRenderer(TiledMap map, Batch batch) {
/*  36 */     super(map, batch);
/*     */   }
/*     */   
/*     */   public IsometricStaggeredTiledMapRenderer(TiledMap map, float unitScale) {
/*  40 */     super(map, unitScale);
/*     */   }
/*     */   
/*     */   public IsometricStaggeredTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
/*  44 */     super(map, unitScale, batch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderTileLayer(TiledMapTileLayer layer) {
/*  49 */     Color batchColor = this.batch.getColor();
/*  50 */     float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());
/*     */     
/*  52 */     int layerWidth = layer.getWidth();
/*  53 */     int layerHeight = layer.getHeight();
/*     */     
/*  55 */     float layerTileWidth = layer.getTileWidth() * this.unitScale;
/*  56 */     float layerTileHeight = layer.getTileHeight() * this.unitScale;
/*     */     
/*  58 */     float layerTileWidth50 = layerTileWidth * 0.5F;
/*  59 */     float layerTileHeight50 = layerTileHeight * 0.5F;
/*     */     
/*  61 */     int minX = Math.max(0, (int)((this.viewBounds.x - layerTileWidth50) / layerTileWidth));
/*  62 */     int maxX = Math.min(layerWidth, (int)((this.viewBounds.x + this.viewBounds.width + layerTileWidth + layerTileWidth50) / layerTileWidth));
/*     */ 
/*     */     
/*  65 */     int minY = Math.max(0, (int)((this.viewBounds.y - layerTileHeight) / layerTileHeight));
/*  66 */     int maxY = Math.min(layerHeight, (int)((this.viewBounds.y + this.viewBounds.height + layerTileHeight) / layerTileHeight50));
/*     */     
/*  68 */     for (int y = maxY - 1; y >= minY; y--) {
/*  69 */       float offsetX = (y % 2 == 1) ? layerTileWidth50 : 0.0F;
/*  70 */       for (int x = maxX - 1; x >= minX; x--) {
/*  71 */         TiledMapTileLayer.Cell cell = layer.getCell(x, y);
/*  72 */         if (cell != null) {
/*  73 */           TiledMapTile tile = cell.getTile();
/*     */           
/*  75 */           if (tile != null) {
/*  76 */             boolean flipX = cell.getFlipHorizontally();
/*  77 */             boolean flipY = cell.getFlipVertically();
/*  78 */             int rotations = cell.getRotation();
/*  79 */             TextureRegion region = tile.getTextureRegion();
/*     */             
/*  81 */             float x1 = x * layerTileWidth - offsetX + tile.getOffsetX() * this.unitScale;
/*  82 */             float y1 = y * layerTileHeight50 + tile.getOffsetY() * this.unitScale;
/*  83 */             float x2 = x1 + region.getRegionWidth() * this.unitScale;
/*  84 */             float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */             
/*  86 */             float u1 = region.getU();
/*  87 */             float v1 = region.getV2();
/*  88 */             float u2 = region.getU2();
/*  89 */             float v2 = region.getV();
/*     */             
/*  91 */             this.vertices[0] = x1;
/*  92 */             this.vertices[1] = y1;
/*  93 */             this.vertices[2] = color;
/*  94 */             this.vertices[3] = u1;
/*  95 */             this.vertices[4] = v1;
/*     */             
/*  97 */             this.vertices[5] = x1;
/*  98 */             this.vertices[6] = y2;
/*  99 */             this.vertices[7] = color;
/* 100 */             this.vertices[8] = u1;
/* 101 */             this.vertices[9] = v2;
/*     */             
/* 103 */             this.vertices[10] = x2;
/* 104 */             this.vertices[11] = y2;
/* 105 */             this.vertices[12] = color;
/* 106 */             this.vertices[13] = u2;
/* 107 */             this.vertices[14] = v2;
/*     */             
/* 109 */             this.vertices[15] = x2;
/* 110 */             this.vertices[16] = y1;
/* 111 */             this.vertices[17] = color;
/* 112 */             this.vertices[18] = u2;
/* 113 */             this.vertices[19] = v1;
/*     */             
/* 115 */             if (flipX) {
/* 116 */               float temp = this.vertices[3];
/* 117 */               this.vertices[3] = this.vertices[13];
/* 118 */               this.vertices[13] = temp;
/* 119 */               temp = this.vertices[8];
/* 120 */               this.vertices[8] = this.vertices[18];
/* 121 */               this.vertices[18] = temp;
/*     */             } 
/*     */             
/* 124 */             if (flipY) {
/* 125 */               float temp = this.vertices[4];
/* 126 */               this.vertices[4] = this.vertices[14];
/* 127 */               this.vertices[14] = temp;
/* 128 */               temp = this.vertices[9];
/* 129 */               this.vertices[9] = this.vertices[19];
/* 130 */               this.vertices[19] = temp;
/*     */             } 
/*     */             
/* 133 */             if (rotations != 0) {
/* 134 */               float f1; float tempU; float tempV; float f4; float f3; float f2; switch (rotations) {
/*     */                 case 1:
/* 136 */                   f1 = this.vertices[4];
/* 137 */                   this.vertices[4] = this.vertices[9];
/* 138 */                   this.vertices[9] = this.vertices[14];
/* 139 */                   this.vertices[14] = this.vertices[19];
/* 140 */                   this.vertices[19] = f1;
/*     */                   
/* 142 */                   f4 = this.vertices[3];
/* 143 */                   this.vertices[3] = this.vertices[8];
/* 144 */                   this.vertices[8] = this.vertices[13];
/* 145 */                   this.vertices[13] = this.vertices[18];
/* 146 */                   this.vertices[18] = f4;
/*     */                   break;
/*     */                 
/*     */                 case 2:
/* 150 */                   tempU = this.vertices[3];
/* 151 */                   this.vertices[3] = this.vertices[13];
/* 152 */                   this.vertices[13] = tempU;
/* 153 */                   tempU = this.vertices[8];
/* 154 */                   this.vertices[8] = this.vertices[18];
/* 155 */                   this.vertices[18] = tempU;
/* 156 */                   f3 = this.vertices[4];
/* 157 */                   this.vertices[4] = this.vertices[14];
/* 158 */                   this.vertices[14] = f3;
/* 159 */                   f3 = this.vertices[9];
/* 160 */                   this.vertices[9] = this.vertices[19];
/* 161 */                   this.vertices[19] = f3;
/*     */                   break;
/*     */                 
/*     */                 case 3:
/* 165 */                   tempV = this.vertices[4];
/* 166 */                   this.vertices[4] = this.vertices[19];
/* 167 */                   this.vertices[19] = this.vertices[14];
/* 168 */                   this.vertices[14] = this.vertices[9];
/* 169 */                   this.vertices[9] = tempV;
/*     */                   
/* 171 */                   f2 = this.vertices[3];
/* 172 */                   this.vertices[3] = this.vertices[18];
/* 173 */                   this.vertices[18] = this.vertices[13];
/* 174 */                   this.vertices[13] = this.vertices[8];
/* 175 */                   this.vertices[8] = f2;
/*     */                   break;
/*     */               } 
/*     */             
/*     */             } 
/* 180 */             this.batch.draw(region.getTexture(), this.vertices, 0, 20);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\renderers\IsometricStaggeredTiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */