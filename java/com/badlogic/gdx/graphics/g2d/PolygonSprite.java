/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.utils.NumberUtils;
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
/*     */ public class PolygonSprite
/*     */ {
/*     */   PolygonRegion region;
/*     */   private float x;
/*     */   private float y;
/*     */   private float width;
/*     */   private float height;
/*  30 */   private float scaleX = 1.0F; private float scaleY = 1.0F; private float rotation;
/*     */   private float originX;
/*     */   private float originY;
/*     */   private float[] vertices;
/*     */   private boolean dirty;
/*  35 */   private Rectangle bounds = new Rectangle();
/*  36 */   private final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   
/*     */   public PolygonSprite(PolygonRegion region) {
/*  39 */     setRegion(region);
/*  40 */     setColor(1.0F, 1.0F, 1.0F, 1.0F);
/*  41 */     setSize(region.region.regionWidth, region.region.regionHeight);
/*  42 */     setOrigin(this.width / 2.0F, this.height / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public PolygonSprite(PolygonSprite sprite) {
/*  47 */     set(sprite);
/*     */   }
/*     */   
/*     */   public void set(PolygonSprite sprite) {
/*  51 */     if (sprite == null) throw new IllegalArgumentException("sprite cannot be null.");
/*     */     
/*  53 */     setRegion(sprite.region);
/*     */     
/*  55 */     this.x = sprite.x;
/*  56 */     this.y = sprite.y;
/*  57 */     this.width = sprite.width;
/*  58 */     this.height = sprite.height;
/*  59 */     this.originX = sprite.originX;
/*  60 */     this.originY = sprite.originY;
/*  61 */     this.rotation = sprite.rotation;
/*  62 */     this.scaleX = sprite.scaleX;
/*  63 */     this.scaleY = sprite.scaleY;
/*  64 */     this.color.set(sprite.color);
/*  65 */     this.dirty = sprite.dirty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBounds(float x, float y, float width, float height) {
/*  71 */     this.x = x;
/*  72 */     this.y = y;
/*  73 */     this.width = width;
/*  74 */     this.height = height;
/*     */     
/*  76 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(float width, float height) {
/*  83 */     this.width = width;
/*  84 */     this.height = height;
/*     */     
/*  86 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y) {
/*  93 */     translate(x - this.x, y - this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(float x) {
/* 100 */     translateX(x - this.x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(float y) {
/* 107 */     translateY(y - this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void translateX(float xAmount) {
/* 113 */     this.x += xAmount;
/*     */     
/* 115 */     if (this.dirty)
/*     */       return; 
/* 117 */     float[] vertices = this.vertices;
/* 118 */     for (int i = 0; i < vertices.length; i += 5) {
/* 119 */       vertices[i] = vertices[i] + xAmount;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void translateY(float yAmount) {
/* 125 */     this.y += yAmount;
/*     */     
/* 127 */     if (this.dirty)
/*     */       return; 
/* 129 */     float[] vertices = this.vertices;
/* 130 */     for (int i = 1; i < vertices.length; i += 5) {
/* 131 */       vertices[i] = vertices[i] + yAmount;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void translate(float xAmount, float yAmount) {
/* 137 */     this.x += xAmount;
/* 138 */     this.y += yAmount;
/*     */     
/* 140 */     if (this.dirty)
/*     */       return; 
/* 142 */     float[] vertices = this.vertices;
/* 143 */     for (int i = 0; i < vertices.length; i += 5) {
/* 144 */       vertices[i] = vertices[i] + xAmount;
/* 145 */       vertices[i + 1] = vertices[i + 1] + yAmount;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setColor(Color tint) {
/* 150 */     this.color.set(tint);
/* 151 */     float color = tint.toFloatBits();
/*     */     
/* 153 */     float[] vertices = this.vertices;
/* 154 */     for (int i = 2; i < vertices.length; i += 5)
/* 155 */       vertices[i] = color; 
/*     */   }
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 159 */     this.color.set(r, g, b, a);
/* 160 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/* 161 */     float color = NumberUtils.intToFloatColor(intBits);
/* 162 */     float[] vertices = this.vertices;
/* 163 */     for (int i = 2; i < vertices.length; i += 5) {
/* 164 */       vertices[i] = color;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOrigin(float originX, float originY) {
/* 169 */     this.originX = originX;
/* 170 */     this.originY = originY;
/* 171 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setRotation(float degrees) {
/* 175 */     this.rotation = degrees;
/* 176 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(float degrees) {
/* 181 */     this.rotation += degrees;
/* 182 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setScale(float scaleXY) {
/* 186 */     this.scaleX = scaleXY;
/* 187 */     this.scaleY = scaleXY;
/* 188 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setScale(float scaleX, float scaleY) {
/* 192 */     this.scaleX = scaleX;
/* 193 */     this.scaleY = scaleY;
/* 194 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(float amount) {
/* 199 */     this.scaleX += amount;
/* 200 */     this.scaleY += amount;
/* 201 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getVertices() {
/* 206 */     if (!this.dirty) return this.vertices; 
/* 207 */     this.dirty = false;
/*     */     
/* 209 */     float originX = this.originX;
/* 210 */     float originY = this.originY;
/* 211 */     float scaleX = this.scaleX;
/* 212 */     float scaleY = this.scaleY;
/* 213 */     PolygonRegion region = this.region;
/* 214 */     float[] vertices = this.vertices;
/* 215 */     float[] regionVertices = region.vertices;
/*     */     
/* 217 */     float worldOriginX = this.x + originX;
/* 218 */     float worldOriginY = this.y + originY;
/* 219 */     float sX = this.width / region.region.getRegionWidth();
/* 220 */     float sY = this.height / region.region.getRegionHeight();
/* 221 */     float cos = MathUtils.cosDeg(this.rotation);
/* 222 */     float sin = MathUtils.sinDeg(this.rotation);
/*     */ 
/*     */     
/* 225 */     for (int i = 0, v = 0, n = regionVertices.length; i < n; i += 2, v += 5) {
/* 226 */       float fx = (regionVertices[i] * sX - originX) * scaleX;
/* 227 */       float fy = (regionVertices[i + 1] * sY - originY) * scaleY;
/* 228 */       vertices[v] = cos * fx - sin * fy + worldOriginX;
/* 229 */       vertices[v + 1] = sin * fx + cos * fy + worldOriginY;
/*     */     } 
/* 231 */     return vertices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBoundingRectangle() {
/* 239 */     float[] vertices = getVertices();
/*     */     
/* 241 */     float minx = vertices[0];
/* 242 */     float miny = vertices[1];
/* 243 */     float maxx = vertices[0];
/* 244 */     float maxy = vertices[1];
/*     */     
/* 246 */     for (int i = 5; i < vertices.length; i += 5) {
/* 247 */       float x = vertices[i];
/* 248 */       float y = vertices[i + 1];
/* 249 */       minx = (minx > x) ? x : minx;
/* 250 */       maxx = (maxx < x) ? x : maxx;
/* 251 */       miny = (miny > y) ? y : miny;
/* 252 */       maxy = (maxy < y) ? y : maxy;
/*     */     } 
/*     */     
/* 255 */     this.bounds.x = minx;
/* 256 */     this.bounds.y = miny;
/* 257 */     this.bounds.width = maxx - minx;
/* 258 */     this.bounds.height = maxy - miny;
/* 259 */     return this.bounds;
/*     */   }
/*     */   
/*     */   public void draw(PolygonSpriteBatch spriteBatch) {
/* 263 */     PolygonRegion region = this.region;
/* 264 */     spriteBatch.draw(region.region.texture, getVertices(), 0, this.vertices.length, region.triangles, 0, region.triangles.length);
/*     */   }
/*     */   
/*     */   public void draw(PolygonSpriteBatch spriteBatch, float alphaModulation) {
/* 268 */     Color color = getColor();
/* 269 */     float oldAlpha = color.a;
/* 270 */     color.a *= alphaModulation;
/* 271 */     setColor(color);
/* 272 */     draw(spriteBatch);
/* 273 */     color.a = oldAlpha;
/* 274 */     setColor(color);
/*     */   }
/*     */   
/*     */   public float getX() {
/* 278 */     return this.x;
/*     */   }
/*     */   
/*     */   public float getY() {
/* 282 */     return this.y;
/*     */   }
/*     */   
/*     */   public float getWidth() {
/* 286 */     return this.width;
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 290 */     return this.height;
/*     */   }
/*     */   
/*     */   public float getOriginX() {
/* 294 */     return this.originX;
/*     */   }
/*     */   
/*     */   public float getOriginY() {
/* 298 */     return this.originY;
/*     */   }
/*     */   
/*     */   public float getRotation() {
/* 302 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public float getScaleX() {
/* 306 */     return this.scaleX;
/*     */   }
/*     */   
/*     */   public float getScaleY() {
/* 310 */     return this.scaleY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 316 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getVertexColor() {
/* 322 */     int intBits = NumberUtils.floatToIntColor(this.vertices[2]);
/* 323 */     Color color = this.color;
/* 324 */     color.r = (intBits & 0xFF) / 255.0F;
/* 325 */     color.g = (intBits >>> 8 & 0xFF) / 255.0F;
/* 326 */     color.b = (intBits >>> 16 & 0xFF) / 255.0F;
/* 327 */     color.a = (intBits >>> 24 & 0xFF) / 255.0F;
/* 328 */     return color;
/*     */   }
/*     */   
/*     */   public void setRegion(PolygonRegion region) {
/* 332 */     this.region = region;
/*     */     
/* 334 */     float[] regionVertices = region.vertices;
/* 335 */     float[] textureCoords = region.textureCoords;
/*     */     
/* 337 */     if (this.vertices == null || regionVertices.length != this.vertices.length) this.vertices = new float[regionVertices.length / 2 * 5];
/*     */ 
/*     */     
/* 340 */     float[] vertices = this.vertices;
/* 341 */     for (int i = 0, v = 2, n = regionVertices.length; i < n; i += 2, v += 5) {
/* 342 */       vertices[v] = this.color.toFloatBits();
/* 343 */       vertices[v + 1] = textureCoords[i];
/* 344 */       vertices[v + 2] = textureCoords[i + 1];
/*     */     } 
/*     */     
/* 347 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public PolygonRegion getRegion() {
/* 351 */     return this.region;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\PolygonSprite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */