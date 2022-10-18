/*     */ package com.badlogic.gdx.maps.tiled.renderers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMap;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTile;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ public class IsometricTiledMapRenderer
/*     */   extends BatchTiledMapRenderer
/*     */ {
/*     */   private Matrix4 isoTransform;
/*     */   private Matrix4 invIsotransform;
/*  36 */   private Vector3 screenPos = new Vector3();
/*     */   
/*  38 */   private Vector2 topRight = new Vector2();
/*  39 */   private Vector2 bottomLeft = new Vector2();
/*  40 */   private Vector2 topLeft = new Vector2();
/*  41 */   private Vector2 bottomRight = new Vector2();
/*     */   
/*     */   public IsometricTiledMapRenderer(TiledMap map) {
/*  44 */     super(map);
/*  45 */     init();
/*     */   }
/*     */   
/*     */   public IsometricTiledMapRenderer(TiledMap map, Batch batch) {
/*  49 */     super(map, batch);
/*  50 */     init();
/*     */   }
/*     */   
/*     */   public IsometricTiledMapRenderer(TiledMap map, float unitScale) {
/*  54 */     super(map, unitScale);
/*  55 */     init();
/*     */   }
/*     */   
/*     */   public IsometricTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
/*  59 */     super(map, unitScale, batch);
/*  60 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   private void init() {
/*  65 */     this.isoTransform = new Matrix4();
/*  66 */     this.isoTransform.idt();
/*     */ 
/*     */     
/*  69 */     this.isoTransform.scale((float)(Math.sqrt(2.0D) / 2.0D), (float)(Math.sqrt(2.0D) / 4.0D), 1.0F);
/*  70 */     this.isoTransform.rotate(0.0F, 0.0F, 1.0F, -45.0F);
/*     */ 
/*     */     
/*  73 */     this.invIsotransform = new Matrix4(this.isoTransform);
/*  74 */     this.invIsotransform.inv();
/*     */   }
/*     */   
/*     */   private Vector3 translateScreenToIso(Vector2 vec) {
/*  78 */     this.screenPos.set(vec.x, vec.y, 0.0F);
/*  79 */     this.screenPos.mul(this.invIsotransform);
/*     */     
/*  81 */     return this.screenPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderTileLayer(TiledMapTileLayer layer) {
/*  86 */     Color batchColor = this.batch.getColor();
/*  87 */     float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());
/*     */     
/*  89 */     float tileWidth = layer.getTileWidth() * this.unitScale;
/*  90 */     float tileHeight = layer.getTileHeight() * this.unitScale;
/*  91 */     float halfTileWidth = tileWidth * 0.5F;
/*  92 */     float halfTileHeight = tileHeight * 0.5F;
/*     */ 
/*     */ 
/*     */     
/*  96 */     this.topRight.set(this.viewBounds.x + this.viewBounds.width, this.viewBounds.y);
/*     */     
/*  98 */     this.bottomLeft.set(this.viewBounds.x, this.viewBounds.y + this.viewBounds.height);
/*     */     
/* 100 */     this.topLeft.set(this.viewBounds.x, this.viewBounds.y);
/*     */     
/* 102 */     this.bottomRight.set(this.viewBounds.x + this.viewBounds.width, this.viewBounds.y + this.viewBounds.height);
/*     */ 
/*     */     
/* 105 */     int row1 = (int)((translateScreenToIso(this.topLeft)).y / tileWidth) - 2;
/* 106 */     int row2 = (int)((translateScreenToIso(this.bottomRight)).y / tileWidth) + 2;
/*     */     
/* 108 */     int col1 = (int)((translateScreenToIso(this.bottomLeft)).x / tileWidth) - 2;
/* 109 */     int col2 = (int)((translateScreenToIso(this.topRight)).x / tileWidth) + 2;
/*     */     
/* 111 */     for (int row = row2; row >= row1; row--) {
/* 112 */       for (int col = col1; col <= col2; col++) {
/* 113 */         float x = col * halfTileWidth + row * halfTileWidth;
/* 114 */         float y = row * halfTileHeight - col * halfTileHeight;
/*     */         
/* 116 */         TiledMapTileLayer.Cell cell = layer.getCell(col, row);
/* 117 */         if (cell != null) {
/* 118 */           TiledMapTile tile = cell.getTile();
/*     */           
/* 120 */           if (tile != null) {
/* 121 */             boolean flipX = cell.getFlipHorizontally();
/* 122 */             boolean flipY = cell.getFlipVertically();
/* 123 */             int rotations = cell.getRotation();
/*     */             
/* 125 */             TextureRegion region = tile.getTextureRegion();
/*     */             
/* 127 */             float x1 = x + tile.getOffsetX() * this.unitScale;
/* 128 */             float y1 = y + tile.getOffsetY() * this.unitScale;
/* 129 */             float x2 = x1 + region.getRegionWidth() * this.unitScale;
/* 130 */             float y2 = y1 + region.getRegionHeight() * this.unitScale;
/*     */             
/* 132 */             float u1 = region.getU();
/* 133 */             float v1 = region.getV2();
/* 134 */             float u2 = region.getU2();
/* 135 */             float v2 = region.getV();
/*     */             
/* 137 */             this.vertices[0] = x1;
/* 138 */             this.vertices[1] = y1;
/* 139 */             this.vertices[2] = color;
/* 140 */             this.vertices[3] = u1;
/* 141 */             this.vertices[4] = v1;
/*     */             
/* 143 */             this.vertices[5] = x1;
/* 144 */             this.vertices[6] = y2;
/* 145 */             this.vertices[7] = color;
/* 146 */             this.vertices[8] = u1;
/* 147 */             this.vertices[9] = v2;
/*     */             
/* 149 */             this.vertices[10] = x2;
/* 150 */             this.vertices[11] = y2;
/* 151 */             this.vertices[12] = color;
/* 152 */             this.vertices[13] = u2;
/* 153 */             this.vertices[14] = v2;
/*     */             
/* 155 */             this.vertices[15] = x2;
/* 156 */             this.vertices[16] = y1;
/* 157 */             this.vertices[17] = color;
/* 158 */             this.vertices[18] = u2;
/* 159 */             this.vertices[19] = v1;
/*     */             
/* 161 */             if (flipX) {
/* 162 */               float temp = this.vertices[3];
/* 163 */               this.vertices[3] = this.vertices[13];
/* 164 */               this.vertices[13] = temp;
/* 165 */               temp = this.vertices[8];
/* 166 */               this.vertices[8] = this.vertices[18];
/* 167 */               this.vertices[18] = temp;
/*     */             } 
/* 169 */             if (flipY) {
/* 170 */               float temp = this.vertices[4];
/* 171 */               this.vertices[4] = this.vertices[14];
/* 172 */               this.vertices[14] = temp;
/* 173 */               temp = this.vertices[9];
/* 174 */               this.vertices[9] = this.vertices[19];
/* 175 */               this.vertices[19] = temp;
/*     */             } 
/* 177 */             if (rotations != 0) {
/* 178 */               float f1; float tempU; float tempV; float f4; float f3; float f2; switch (rotations) {
/*     */                 case 1:
/* 180 */                   f1 = this.vertices[4];
/* 181 */                   this.vertices[4] = this.vertices[9];
/* 182 */                   this.vertices[9] = this.vertices[14];
/* 183 */                   this.vertices[14] = this.vertices[19];
/* 184 */                   this.vertices[19] = f1;
/*     */                   
/* 186 */                   f4 = this.vertices[3];
/* 187 */                   this.vertices[3] = this.vertices[8];
/* 188 */                   this.vertices[8] = this.vertices[13];
/* 189 */                   this.vertices[13] = this.vertices[18];
/* 190 */                   this.vertices[18] = f4;
/*     */                   break;
/*     */                 
/*     */                 case 2:
/* 194 */                   tempU = this.vertices[3];
/* 195 */                   this.vertices[3] = this.vertices[13];
/* 196 */                   this.vertices[13] = tempU;
/* 197 */                   tempU = this.vertices[8];
/* 198 */                   this.vertices[8] = this.vertices[18];
/* 199 */                   this.vertices[18] = tempU;
/* 200 */                   f3 = this.vertices[4];
/* 201 */                   this.vertices[4] = this.vertices[14];
/* 202 */                   this.vertices[14] = f3;
/* 203 */                   f3 = this.vertices[9];
/* 204 */                   this.vertices[9] = this.vertices[19];
/* 205 */                   this.vertices[19] = f3;
/*     */                   break;
/*     */                 
/*     */                 case 3:
/* 209 */                   tempV = this.vertices[4];
/* 210 */                   this.vertices[4] = this.vertices[19];
/* 211 */                   this.vertices[19] = this.vertices[14];
/* 212 */                   this.vertices[14] = this.vertices[9];
/* 213 */                   this.vertices[9] = tempV;
/*     */                   
/* 215 */                   f2 = this.vertices[3];
/* 216 */                   this.vertices[3] = this.vertices[18];
/* 217 */                   this.vertices[18] = this.vertices[13];
/* 218 */                   this.vertices[13] = this.vertices[8];
/* 219 */                   this.vertices[8] = f2;
/*     */                   break;
/*     */               } 
/*     */             
/*     */             } 
/* 224 */             this.batch.draw(region.getTexture(), this.vertices, 0, 20);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\renderers\IsometricTiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */