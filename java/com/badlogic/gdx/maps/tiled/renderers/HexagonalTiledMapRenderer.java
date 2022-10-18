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
/*     */ public class HexagonalTiledMapRenderer
/*     */   extends BatchTiledMapRenderer
/*     */ {
/*     */   private boolean staggerAxisX = true;
/*     */   private boolean staggerIndexEven = false;
/*  39 */   private float hexSideLength = 0.0F;
/*     */   
/*     */   public HexagonalTiledMapRenderer(TiledMap map) {
/*  42 */     super(map);
/*  43 */     init(map);
/*     */   }
/*     */   
/*     */   public HexagonalTiledMapRenderer(TiledMap map, float unitScale) {
/*  47 */     super(map, unitScale);
/*  48 */     init(map);
/*     */   }
/*     */   
/*     */   public HexagonalTiledMapRenderer(TiledMap map, Batch batch) {
/*  52 */     super(map, batch);
/*  53 */     init(map);
/*     */   }
/*     */   
/*     */   public HexagonalTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
/*  57 */     super(map, unitScale, batch);
/*  58 */     init(map);
/*     */   }
/*     */   
/*     */   private void init(TiledMap map) {
/*  62 */     String axis = (String)map.getProperties().get("staggeraxis", String.class);
/*  63 */     if (axis != null) {
/*  64 */       if (axis.equals("x")) {
/*  65 */         this.staggerAxisX = true;
/*     */       } else {
/*  67 */         this.staggerAxisX = false;
/*     */       } 
/*     */     }
/*     */     
/*  71 */     String index = (String)map.getProperties().get("staggerindex", String.class);
/*  72 */     if (index != null) {
/*  73 */       if (index.equals("even")) {
/*  74 */         this.staggerIndexEven = true;
/*     */       } else {
/*  76 */         this.staggerIndexEven = false;
/*     */       } 
/*     */     }
/*     */     
/*  80 */     Integer length = (Integer)map.getProperties().get("hexsidelength", Integer.class);
/*  81 */     if (length != null) {
/*  82 */       this.hexSideLength = length.intValue();
/*     */     }
/*  84 */     else if (this.staggerAxisX) {
/*  85 */       length = (Integer)map.getProperties().get("tilewidth", Integer.class);
/*  86 */       if (length != null) {
/*  87 */         this.hexSideLength = 0.5F * length.intValue();
/*     */       } else {
/*  89 */         TiledMapTileLayer tmtl = (TiledMapTileLayer)map.getLayers().get(0);
/*  90 */         this.hexSideLength = 0.5F * tmtl.getTileWidth();
/*     */       } 
/*     */     } else {
/*  93 */       length = (Integer)map.getProperties().get("tileheight", Integer.class);
/*  94 */       if (length != null) {
/*  95 */         this.hexSideLength = 0.5F * length.intValue();
/*     */       } else {
/*  97 */         TiledMapTileLayer tmtl = (TiledMapTileLayer)map.getLayers().get(0);
/*  98 */         this.hexSideLength = 0.5F * tmtl.getTileHeight();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderTileLayer(TiledMapTileLayer layer) {
/* 106 */     Color batchColor = this.batch.getColor();
/* 107 */     float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());
/*     */     
/* 109 */     int layerWidth = layer.getWidth();
/* 110 */     int layerHeight = layer.getHeight();
/*     */     
/* 112 */     float layerTileWidth = layer.getTileWidth() * this.unitScale;
/* 113 */     float layerTileHeight = layer.getTileHeight() * this.unitScale;
/*     */     
/* 115 */     float layerHexLength = this.hexSideLength * this.unitScale;
/*     */     
/* 117 */     if (this.staggerAxisX) {
/* 118 */       float tileWidthLowerCorner = (layerTileWidth - layerHexLength) / 2.0F;
/* 119 */       float tileWidthUpperCorner = (layerTileWidth + layerHexLength) / 2.0F;
/* 120 */       float layerTileHeight50 = layerTileHeight * 0.5F;
/*     */       
/* 122 */       int row1 = Math.max(0, (int)((this.viewBounds.y - layerTileHeight50) / layerTileHeight));
/* 123 */       int row2 = Math.min(layerHeight, (int)((this.viewBounds.y + this.viewBounds.height + layerTileHeight) / layerTileHeight));
/*     */       
/* 125 */       int col1 = Math.max(0, (int)((this.viewBounds.x - tileWidthLowerCorner) / tileWidthUpperCorner));
/* 126 */       int col2 = Math.min(layerWidth, (int)((this.viewBounds.x + this.viewBounds.width + tileWidthUpperCorner) / tileWidthUpperCorner));
/*     */ 
/*     */ 
/*     */       
/* 130 */       int colA = (this.staggerIndexEven == ((col1 % 2 == 0))) ? (col1 + 1) : col1;
/* 131 */       int colB = (this.staggerIndexEven == ((col1 % 2 == 0))) ? col1 : (col1 + 1);
/*     */       
/* 133 */       for (int row = row2 - 1; row >= row1; row--) {
/* 134 */         int col; for (col = colA; col < col2; col += 2) {
/* 135 */           renderCell(layer.getCell(col, row), tileWidthUpperCorner * col, layerTileHeight50 + layerTileHeight * row, color);
/*     */         }
/*     */         
/* 138 */         for (col = colB; col < col2; col += 2) {
/* 139 */           renderCell(layer.getCell(col, row), tileWidthUpperCorner * col, layerTileHeight * row, color);
/*     */         }
/*     */       } 
/*     */     } else {
/* 143 */       float tileHeightLowerCorner = (layerTileHeight - layerHexLength) / 2.0F;
/* 144 */       float tileHeightUpperCorner = (layerTileHeight + layerHexLength) / 2.0F;
/* 145 */       float layerTileWidth50 = layerTileWidth * 0.5F;
/*     */       
/* 147 */       int row1 = Math.max(0, (int)((this.viewBounds.y - tileHeightLowerCorner) / tileHeightUpperCorner));
/* 148 */       int row2 = Math.min(layerHeight, (int)((this.viewBounds.y + this.viewBounds.height + tileHeightUpperCorner) / tileHeightUpperCorner));
/*     */ 
/*     */       
/* 151 */       int col1 = Math.max(0, (int)((this.viewBounds.x - layerTileWidth50) / layerTileWidth));
/* 152 */       int col2 = Math.min(layerWidth, (int)((this.viewBounds.x + this.viewBounds.width + layerTileWidth) / layerTileWidth));
/*     */       
/* 154 */       float shiftX = 0.0F;
/* 155 */       for (int row = row2 - 1; row >= row1; row--) {
/*     */         
/* 157 */         if (((row % 2 == 0)) == this.staggerIndexEven) {
/* 158 */           shiftX = 0.0F;
/*     */         } else {
/* 160 */           shiftX = layerTileWidth50;
/* 161 */         }  for (int col = col1; col < col2; col++) {
/* 162 */           renderCell(layer.getCell(col, row), layerTileWidth * col + shiftX, tileHeightUpperCorner * row, color);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderCell(TiledMapTileLayer.Cell cell, float x, float y, float color) {
/* 170 */     if (cell != null) {
/* 171 */       TiledMapTile tile = cell.getTile();
/* 172 */       if (tile != null) {
/* 173 */         if (tile instanceof com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile)
/*     */           return; 
/* 175 */         boolean flipX = cell.getFlipHorizontally();
/* 176 */         boolean flipY = cell.getFlipVertically();
/* 177 */         int rotations = cell.getRotation();
/*     */         
/* 179 */         TextureRegion region = tile.getTextureRegion();
/*     */         
/* 181 */         float x1 = x + tile.getOffsetX() * this.unitScale;
/* 182 */         float y1 = y + tile.getOffsetY() * this.unitScale;
/* 183 */         float x2 = x1 + region.getRegionWidth() * this.unitScale;
/* 184 */         float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */         
/* 186 */         float u1 = region.getU();
/* 187 */         float v1 = region.getV2();
/* 188 */         float u2 = region.getU2();
/* 189 */         float v2 = region.getV();
/*     */         
/* 191 */         this.vertices[0] = x1;
/* 192 */         this.vertices[1] = y1;
/* 193 */         this.vertices[2] = color;
/* 194 */         this.vertices[3] = u1;
/* 195 */         this.vertices[4] = v1;
/*     */         
/* 197 */         this.vertices[5] = x1;
/* 198 */         this.vertices[6] = y2;
/* 199 */         this.vertices[7] = color;
/* 200 */         this.vertices[8] = u1;
/* 201 */         this.vertices[9] = v2;
/*     */         
/* 203 */         this.vertices[10] = x2;
/* 204 */         this.vertices[11] = y2;
/* 205 */         this.vertices[12] = color;
/* 206 */         this.vertices[13] = u2;
/* 207 */         this.vertices[14] = v2;
/*     */         
/* 209 */         this.vertices[15] = x2;
/* 210 */         this.vertices[16] = y1;
/* 211 */         this.vertices[17] = color;
/* 212 */         this.vertices[18] = u2;
/* 213 */         this.vertices[19] = v1;
/*     */         
/* 215 */         if (flipX) {
/* 216 */           float temp = this.vertices[3];
/* 217 */           this.vertices[3] = this.vertices[13];
/* 218 */           this.vertices[13] = temp;
/* 219 */           temp = this.vertices[8];
/* 220 */           this.vertices[8] = this.vertices[18];
/* 221 */           this.vertices[18] = temp;
/*     */         } 
/* 223 */         if (flipY) {
/* 224 */           float temp = this.vertices[4];
/* 225 */           this.vertices[4] = this.vertices[14];
/* 226 */           this.vertices[14] = temp;
/* 227 */           temp = this.vertices[9];
/* 228 */           this.vertices[9] = this.vertices[19];
/* 229 */           this.vertices[19] = temp;
/*     */         } 
/* 231 */         if (rotations == 2) {
/* 232 */           float tempU = this.vertices[3];
/* 233 */           this.vertices[3] = this.vertices[13];
/* 234 */           this.vertices[13] = tempU;
/* 235 */           tempU = this.vertices[8];
/* 236 */           this.vertices[8] = this.vertices[18];
/* 237 */           this.vertices[18] = tempU;
/* 238 */           float tempV = this.vertices[4];
/* 239 */           this.vertices[4] = this.vertices[14];
/* 240 */           this.vertices[14] = tempV;
/* 241 */           tempV = this.vertices[9];
/* 242 */           this.vertices[9] = this.vertices[19];
/* 243 */           this.vertices[19] = tempV;
/*     */         } 
/* 245 */         this.batch.draw(region.getTexture(), this.vertices, 0, 20);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\renderers\HexagonalTiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */