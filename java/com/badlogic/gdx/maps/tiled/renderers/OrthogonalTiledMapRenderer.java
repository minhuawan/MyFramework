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
/*     */ public class OrthogonalTiledMapRenderer
/*     */   extends BatchTiledMapRenderer
/*     */ {
/*     */   public OrthogonalTiledMapRenderer(TiledMap map) {
/*  51 */     super(map);
/*     */   }
/*     */   
/*     */   public OrthogonalTiledMapRenderer(TiledMap map, Batch batch) {
/*  55 */     super(map, batch);
/*     */   }
/*     */   
/*     */   public OrthogonalTiledMapRenderer(TiledMap map, float unitScale) {
/*  59 */     super(map, unitScale);
/*     */   }
/*     */   
/*     */   public OrthogonalTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
/*  63 */     super(map, unitScale, batch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderTileLayer(TiledMapTileLayer layer) {
/*  68 */     Color batchColor = this.batch.getColor();
/*  69 */     float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());
/*     */     
/*  71 */     int layerWidth = layer.getWidth();
/*  72 */     int layerHeight = layer.getHeight();
/*     */     
/*  74 */     float layerTileWidth = layer.getTileWidth() * this.unitScale;
/*  75 */     float layerTileHeight = layer.getTileHeight() * this.unitScale;
/*     */     
/*  77 */     int col1 = Math.max(0, (int)(this.viewBounds.x / layerTileWidth));
/*  78 */     int col2 = Math.min(layerWidth, (int)((this.viewBounds.x + this.viewBounds.width + layerTileWidth) / layerTileWidth));
/*     */     
/*  80 */     int row1 = Math.max(0, (int)(this.viewBounds.y / layerTileHeight));
/*  81 */     int row2 = Math.min(layerHeight, (int)((this.viewBounds.y + this.viewBounds.height + layerTileHeight) / layerTileHeight));
/*     */     
/*  83 */     float y = row2 * layerTileHeight;
/*  84 */     float xStart = col1 * layerTileWidth;
/*  85 */     float[] vertices = this.vertices;
/*     */     
/*  87 */     for (int row = row2; row >= row1; row--) {
/*  88 */       float x = xStart;
/*  89 */       for (int col = col1; col < col2; col++) {
/*  90 */         TiledMapTileLayer.Cell cell = layer.getCell(col, row);
/*  91 */         if (cell == null) {
/*  92 */           x += layerTileWidth;
/*     */         } else {
/*     */           
/*  95 */           TiledMapTile tile = cell.getTile();
/*     */           
/*  97 */           if (tile != null) {
/*  98 */             boolean flipX = cell.getFlipHorizontally();
/*  99 */             boolean flipY = cell.getFlipVertically();
/* 100 */             int rotations = cell.getRotation();
/*     */             
/* 102 */             TextureRegion region = tile.getTextureRegion();
/*     */             
/* 104 */             float x1 = x + tile.getOffsetX() * this.unitScale;
/* 105 */             float y1 = y + tile.getOffsetY() * this.unitScale;
/* 106 */             float x2 = x1 + region.getRegionWidth() * this.unitScale;
/* 107 */             float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */             
/* 109 */             float u1 = region.getU();
/* 110 */             float v1 = region.getV2();
/* 111 */             float u2 = region.getU2();
/* 112 */             float v2 = region.getV();
/*     */             
/* 114 */             vertices[0] = x1;
/* 115 */             vertices[1] = y1;
/* 116 */             vertices[2] = color;
/* 117 */             vertices[3] = u1;
/* 118 */             vertices[4] = v1;
/*     */             
/* 120 */             vertices[5] = x1;
/* 121 */             vertices[6] = y2;
/* 122 */             vertices[7] = color;
/* 123 */             vertices[8] = u1;
/* 124 */             vertices[9] = v2;
/*     */             
/* 126 */             vertices[10] = x2;
/* 127 */             vertices[11] = y2;
/* 128 */             vertices[12] = color;
/* 129 */             vertices[13] = u2;
/* 130 */             vertices[14] = v2;
/*     */             
/* 132 */             vertices[15] = x2;
/* 133 */             vertices[16] = y1;
/* 134 */             vertices[17] = color;
/* 135 */             vertices[18] = u2;
/* 136 */             vertices[19] = v1;
/*     */             
/* 138 */             if (flipX) {
/* 139 */               float temp = vertices[3];
/* 140 */               vertices[3] = vertices[13];
/* 141 */               vertices[13] = temp;
/* 142 */               temp = vertices[8];
/* 143 */               vertices[8] = vertices[18];
/* 144 */               vertices[18] = temp;
/*     */             } 
/* 146 */             if (flipY) {
/* 147 */               float temp = vertices[4];
/* 148 */               vertices[4] = vertices[14];
/* 149 */               vertices[14] = temp;
/* 150 */               temp = vertices[9];
/* 151 */               vertices[9] = vertices[19];
/* 152 */               vertices[19] = temp;
/*     */             } 
/* 154 */             if (rotations != 0) {
/* 155 */               float f1; float tempU; float tempV; float f4; float f3; float f2; switch (rotations) {
/*     */                 case 1:
/* 157 */                   f1 = vertices[4];
/* 158 */                   vertices[4] = vertices[9];
/* 159 */                   vertices[9] = vertices[14];
/* 160 */                   vertices[14] = vertices[19];
/* 161 */                   vertices[19] = f1;
/*     */                   
/* 163 */                   f4 = vertices[3];
/* 164 */                   vertices[3] = vertices[8];
/* 165 */                   vertices[8] = vertices[13];
/* 166 */                   vertices[13] = vertices[18];
/* 167 */                   vertices[18] = f4;
/*     */                   break;
/*     */                 
/*     */                 case 2:
/* 171 */                   tempU = vertices[3];
/* 172 */                   vertices[3] = vertices[13];
/* 173 */                   vertices[13] = tempU;
/* 174 */                   tempU = vertices[8];
/* 175 */                   vertices[8] = vertices[18];
/* 176 */                   vertices[18] = tempU;
/* 177 */                   f3 = vertices[4];
/* 178 */                   vertices[4] = vertices[14];
/* 179 */                   vertices[14] = f3;
/* 180 */                   f3 = vertices[9];
/* 181 */                   vertices[9] = vertices[19];
/* 182 */                   vertices[19] = f3;
/*     */                   break;
/*     */                 
/*     */                 case 3:
/* 186 */                   tempV = vertices[4];
/* 187 */                   vertices[4] = vertices[19];
/* 188 */                   vertices[19] = vertices[14];
/* 189 */                   vertices[14] = vertices[9];
/* 190 */                   vertices[9] = tempV;
/*     */                   
/* 192 */                   f2 = vertices[3];
/* 193 */                   vertices[3] = vertices[18];
/* 194 */                   vertices[18] = vertices[13];
/* 195 */                   vertices[13] = vertices[8];
/* 196 */                   vertices[8] = f2;
/*     */                   break;
/*     */               } 
/*     */             
/*     */             } 
/* 201 */             this.batch.draw(region.getTexture(), vertices, 0, 20);
/*     */           } 
/* 203 */           x += layerTileWidth;
/*     */         } 
/* 205 */       }  y -= layerTileHeight;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\renderers\OrthogonalTiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */