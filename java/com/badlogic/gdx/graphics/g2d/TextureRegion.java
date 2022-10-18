/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Texture;
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
/*     */ public class TextureRegion
/*     */ {
/*     */   Texture texture;
/*     */   float u;
/*     */   float v;
/*     */   float u2;
/*     */   float v2;
/*     */   int regionWidth;
/*     */   int regionHeight;
/*     */   
/*     */   public TextureRegion() {}
/*     */   
/*     */   public TextureRegion(Texture texture) {
/*  37 */     if (texture == null) throw new IllegalArgumentException("texture cannot be null."); 
/*  38 */     this.texture = texture;
/*  39 */     setRegion(0, 0, texture.getWidth(), texture.getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion(Texture texture, int width, int height) {
/*  45 */     this.texture = texture;
/*  46 */     setRegion(0, 0, width, height);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion(Texture texture, int x, int y, int width, int height) {
/*  52 */     this.texture = texture;
/*  53 */     setRegion(x, y, width, height);
/*     */   }
/*     */   
/*     */   public TextureRegion(Texture texture, float u, float v, float u2, float v2) {
/*  57 */     this.texture = texture;
/*  58 */     setRegion(u, v, u2, v2);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureRegion(TextureRegion region) {
/*  63 */     setRegion(region);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion(TextureRegion region, int x, int y, int width, int height) {
/*  70 */     setRegion(region, x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRegion(Texture texture) {
/*  75 */     this.texture = texture;
/*  76 */     setRegion(0, 0, texture.getWidth(), texture.getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegion(int x, int y, int width, int height) {
/*  82 */     float invTexWidth = 1.0F / this.texture.getWidth();
/*  83 */     float invTexHeight = 1.0F / this.texture.getHeight();
/*  84 */     setRegion(x * invTexWidth, y * invTexHeight, (x + width) * invTexWidth, (y + height) * invTexHeight);
/*  85 */     this.regionWidth = Math.abs(width);
/*  86 */     this.regionHeight = Math.abs(height);
/*     */   }
/*     */   
/*     */   public void setRegion(float u, float v, float u2, float v2) {
/*  90 */     int texWidth = this.texture.getWidth(), texHeight = this.texture.getHeight();
/*  91 */     this.regionWidth = Math.round(Math.abs(u2 - u) * texWidth);
/*  92 */     this.regionHeight = Math.round(Math.abs(v2 - v) * texHeight);
/*     */ 
/*     */     
/*  95 */     if (this.regionWidth == 1 && this.regionHeight == 1) {
/*  96 */       float adjustX = 0.25F / texWidth;
/*  97 */       u += adjustX;
/*  98 */       u2 -= adjustX;
/*  99 */       float adjustY = 0.25F / texHeight;
/* 100 */       v += adjustY;
/* 101 */       v2 -= adjustY;
/*     */     } 
/*     */     
/* 104 */     this.u = u;
/* 105 */     this.v = v;
/* 106 */     this.u2 = u2;
/* 107 */     this.v2 = v2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRegion(TextureRegion region) {
/* 112 */     this.texture = region.texture;
/* 113 */     setRegion(region.u, region.v, region.u2, region.v2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRegion(TextureRegion region, int x, int y, int width, int height) {
/* 118 */     this.texture = region.texture;
/* 119 */     setRegion(region.getRegionX() + x, region.getRegionY() + y, width, height);
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/* 123 */     return this.texture;
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture) {
/* 127 */     this.texture = texture;
/*     */   }
/*     */   
/*     */   public float getU() {
/* 131 */     return this.u;
/*     */   }
/*     */   
/*     */   public void setU(float u) {
/* 135 */     this.u = u;
/* 136 */     this.regionWidth = Math.round(Math.abs(this.u2 - u) * this.texture.getWidth());
/*     */   }
/*     */   
/*     */   public float getV() {
/* 140 */     return this.v;
/*     */   }
/*     */   
/*     */   public void setV(float v) {
/* 144 */     this.v = v;
/* 145 */     this.regionHeight = Math.round(Math.abs(this.v2 - v) * this.texture.getHeight());
/*     */   }
/*     */   
/*     */   public float getU2() {
/* 149 */     return this.u2;
/*     */   }
/*     */   
/*     */   public void setU2(float u2) {
/* 153 */     this.u2 = u2;
/* 154 */     this.regionWidth = Math.round(Math.abs(u2 - this.u) * this.texture.getWidth());
/*     */   }
/*     */   
/*     */   public float getV2() {
/* 158 */     return this.v2;
/*     */   }
/*     */   
/*     */   public void setV2(float v2) {
/* 162 */     this.v2 = v2;
/* 163 */     this.regionHeight = Math.round(Math.abs(v2 - this.v) * this.texture.getHeight());
/*     */   }
/*     */   
/*     */   public int getRegionX() {
/* 167 */     return Math.round(this.u * this.texture.getWidth());
/*     */   }
/*     */   
/*     */   public void setRegionX(int x) {
/* 171 */     setU(x / this.texture.getWidth());
/*     */   }
/*     */   
/*     */   public int getRegionY() {
/* 175 */     return Math.round(this.v * this.texture.getHeight());
/*     */   }
/*     */   
/*     */   public void setRegionY(int y) {
/* 179 */     setV(y / this.texture.getHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRegionWidth() {
/* 184 */     return this.regionWidth;
/*     */   }
/*     */   
/*     */   public void setRegionWidth(int width) {
/* 188 */     if (isFlipX()) {
/* 189 */       setU(this.u2 + width / this.texture.getWidth());
/*     */     } else {
/* 191 */       setU2(this.u + width / this.texture.getWidth());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRegionHeight() {
/* 197 */     return this.regionHeight;
/*     */   }
/*     */   
/*     */   public void setRegionHeight(int height) {
/* 201 */     if (isFlipY()) {
/* 202 */       setV(this.v2 + height / this.texture.getHeight());
/*     */     } else {
/* 204 */       setV2(this.v + height / this.texture.getHeight());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flip(boolean x, boolean y) {
/* 209 */     if (x) {
/* 210 */       float temp = this.u;
/* 211 */       this.u = this.u2;
/* 212 */       this.u2 = temp;
/*     */     } 
/* 214 */     if (y) {
/* 215 */       float temp = this.v;
/* 216 */       this.v = this.v2;
/* 217 */       this.v2 = temp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFlipX() {
/* 222 */     return (this.u > this.u2);
/*     */   }
/*     */   
/*     */   public boolean isFlipY() {
/* 226 */     return (this.v > this.v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scroll(float xAmount, float yAmount) {
/* 234 */     if (xAmount != 0.0F) {
/* 235 */       float width = (this.u2 - this.u) * this.texture.getWidth();
/* 236 */       this.u = (this.u + xAmount) % 1.0F;
/* 237 */       this.u2 = this.u + width / this.texture.getWidth();
/*     */     } 
/* 239 */     if (yAmount != 0.0F) {
/* 240 */       float height = (this.v2 - this.v) * this.texture.getHeight();
/* 241 */       this.v = (this.v + yAmount) % 1.0F;
/* 242 */       this.v2 = this.v + height / this.texture.getHeight();
/*     */     } 
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
/*     */   public TextureRegion[][] split(int tileWidth, int tileHeight) {
/* 255 */     int x = getRegionX();
/* 256 */     int y = getRegionY();
/* 257 */     int width = this.regionWidth;
/* 258 */     int height = this.regionHeight;
/*     */     
/* 260 */     int rows = height / tileHeight;
/* 261 */     int cols = width / tileWidth;
/*     */     
/* 263 */     int startX = x;
/* 264 */     TextureRegion[][] tiles = new TextureRegion[rows][cols];
/* 265 */     for (int row = 0; row < rows; row++, y += tileHeight) {
/* 266 */       x = startX;
/* 267 */       for (int col = 0; col < cols; col++, x += tileWidth) {
/* 268 */         tiles[row][col] = new TextureRegion(this.texture, x, y, tileWidth, tileHeight);
/*     */       }
/*     */     } 
/*     */     
/* 272 */     return tiles;
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
/*     */   public static TextureRegion[][] split(Texture texture, int tileWidth, int tileHeight) {
/* 284 */     TextureRegion region = new TextureRegion(texture);
/* 285 */     return region.split(tileWidth, tileHeight);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\TextureRegion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */