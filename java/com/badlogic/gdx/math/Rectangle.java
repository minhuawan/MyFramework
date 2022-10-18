/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.NumberUtils;
/*     */ import java.io.Serializable;
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
/*     */ public class Rectangle
/*     */   implements Serializable, Shape2D
/*     */ {
/*  26 */   public static final Rectangle tmp = new Rectangle();
/*     */ 
/*     */   
/*  29 */   public static final Rectangle tmp2 = new Rectangle();
/*     */   
/*     */   private static final long serialVersionUID = 5733252015138115702L;
/*     */   
/*     */   public float x;
/*     */   
/*     */   public float y;
/*     */   
/*     */   public float width;
/*     */   
/*     */   public float height;
/*     */ 
/*     */   
/*     */   public Rectangle() {}
/*     */ 
/*     */   
/*     */   public Rectangle(float x, float y, float width, float height) {
/*  46 */     this.x = x;
/*  47 */     this.y = y;
/*  48 */     this.width = width;
/*  49 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle(Rectangle rect) {
/*  55 */     this.x = rect.x;
/*  56 */     this.y = rect.y;
/*  57 */     this.width = rect.width;
/*  58 */     this.height = rect.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle set(float x, float y, float width, float height) {
/*  67 */     this.x = x;
/*  68 */     this.y = y;
/*  69 */     this.width = width;
/*  70 */     this.height = height;
/*     */     
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/*  77 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setX(float x) {
/*  84 */     this.x = x;
/*     */     
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/*  91 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setY(float y) {
/*  98 */     this.y = y;
/*     */     
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 105 */     return this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setWidth(float width) {
/* 112 */     this.width = width;
/*     */     
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 119 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setHeight(float height) {
/* 126 */     this.height = height;
/*     */     
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 getPosition(Vector2 position) {
/* 134 */     return position.set(this.x, this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setPosition(Vector2 position) {
/* 141 */     this.x = position.x;
/* 142 */     this.y = position.y;
/*     */     
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setPosition(float x, float y) {
/* 152 */     this.x = x;
/* 153 */     this.y = y;
/*     */     
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setSize(float width, float height) {
/* 163 */     this.width = width;
/* 164 */     this.height = height;
/*     */     
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setSize(float sizeXY) {
/* 173 */     this.width = sizeXY;
/* 174 */     this.height = sizeXY;
/*     */     
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 getSize(Vector2 size) {
/* 182 */     return size.set(this.width, this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(float x, float y) {
/* 189 */     return (this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Vector2 point) {
/* 195 */     return contains(point.x, point.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Circle circle) {
/* 201 */     return (circle.x - circle.radius >= this.x && circle.x + circle.radius <= this.x + this.width && circle.y - circle.radius >= this.y && circle.y + circle.radius <= this.y + this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Rectangle rectangle) {
/* 208 */     float xmin = rectangle.x;
/* 209 */     float xmax = xmin + rectangle.width;
/*     */     
/* 211 */     float ymin = rectangle.y;
/* 212 */     float ymax = ymin + rectangle.height;
/*     */     
/* 214 */     return (xmin > this.x && xmin < this.x + this.width && xmax > this.x && xmax < this.x + this.width && ymin > this.y && ymin < this.y + this.height && ymax > this.y && ymax < this.y + this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overlaps(Rectangle r) {
/* 221 */     return (this.x < r.x + r.width && this.x + this.width > r.x && this.y < r.y + r.height && this.y + this.height > r.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle set(Rectangle rect) {
/* 228 */     this.x = rect.x;
/* 229 */     this.y = rect.y;
/* 230 */     this.width = rect.width;
/* 231 */     this.height = rect.height;
/*     */     
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(Rectangle rect) {
/* 240 */     float minX = Math.min(this.x, rect.x);
/* 241 */     float maxX = Math.max(this.x + this.width, rect.x + rect.width);
/* 242 */     this.x = minX;
/* 243 */     this.width = maxX - minX;
/*     */     
/* 245 */     float minY = Math.min(this.y, rect.y);
/* 246 */     float maxY = Math.max(this.y + this.height, rect.y + rect.height);
/* 247 */     this.y = minY;
/* 248 */     this.height = maxY - minY;
/*     */     
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(float x, float y) {
/* 258 */     float minX = Math.min(this.x, x);
/* 259 */     float maxX = Math.max(this.x + this.width, x);
/* 260 */     this.x = minX;
/* 261 */     this.width = maxX - minX;
/*     */     
/* 263 */     float minY = Math.min(this.y, y);
/* 264 */     float maxY = Math.max(this.y + this.height, y);
/* 265 */     this.y = minY;
/* 266 */     this.height = maxY - minY;
/*     */     
/* 268 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(Vector2 vec) {
/* 275 */     return merge(vec.x, vec.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle merge(Vector2[] vecs) {
/* 282 */     float minX = this.x;
/* 283 */     float maxX = this.x + this.width;
/* 284 */     float minY = this.y;
/* 285 */     float maxY = this.y + this.height;
/* 286 */     for (int i = 0; i < vecs.length; i++) {
/* 287 */       Vector2 v = vecs[i];
/* 288 */       minX = Math.min(minX, v.x);
/* 289 */       maxX = Math.max(maxX, v.x);
/* 290 */       minY = Math.min(minY, v.y);
/* 291 */       maxY = Math.max(maxY, v.y);
/*     */     } 
/* 293 */     this.x = minX;
/* 294 */     this.width = maxX - minX;
/* 295 */     this.y = minY;
/* 296 */     this.height = maxY - minY;
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAspectRatio() {
/* 303 */     return (this.height == 0.0F) ? Float.NaN : (this.width / this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 getCenter(Vector2 vector) {
/* 310 */     vector.x = this.x + this.width / 2.0F;
/* 311 */     vector.y = this.y + this.height / 2.0F;
/* 312 */     return vector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setCenter(float x, float y) {
/* 320 */     setPosition(x - this.width / 2.0F, y - this.height / 2.0F);
/* 321 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle setCenter(Vector2 position) {
/* 328 */     setPosition(position.x - this.width / 2.0F, position.y - this.height / 2.0F);
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle fitOutside(Rectangle rect) {
/* 338 */     float ratio = getAspectRatio();
/*     */     
/* 340 */     if (ratio > rect.getAspectRatio()) {
/*     */       
/* 342 */       setSize(rect.height * ratio, rect.height);
/*     */     } else {
/*     */       
/* 345 */       setSize(rect.width, rect.width / ratio);
/*     */     } 
/*     */     
/* 348 */     setPosition(rect.x + rect.width / 2.0F - this.width / 2.0F, rect.y + rect.height / 2.0F - this.height / 2.0F);
/* 349 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle fitInside(Rectangle rect) {
/* 358 */     float ratio = getAspectRatio();
/*     */     
/* 360 */     if (ratio < rect.getAspectRatio()) {
/*     */       
/* 362 */       setSize(rect.height * ratio, rect.height);
/*     */     } else {
/*     */       
/* 365 */       setSize(rect.width, rect.width / ratio);
/*     */     } 
/*     */     
/* 368 */     setPosition(rect.x + rect.width / 2.0F - this.width / 2.0F, rect.y + rect.height / 2.0F - this.height / 2.0F);
/* 369 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 375 */     return "[" + this.x + "," + this.y + "," + this.width + "," + this.height + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle fromString(String v) {
/* 383 */     int s0 = v.indexOf(',', 1);
/* 384 */     int s1 = v.indexOf(',', s0 + 1);
/* 385 */     int s2 = v.indexOf(',', s1 + 1);
/* 386 */     if (s0 != -1 && s1 != -1 && s2 != -1 && v.charAt(0) == '[' && v.charAt(v.length() - 1) == ']') {
/*     */       try {
/* 388 */         float x = Float.parseFloat(v.substring(1, s0));
/* 389 */         float y = Float.parseFloat(v.substring(s0 + 1, s1));
/* 390 */         float width = Float.parseFloat(v.substring(s1 + 1, s2));
/* 391 */         float height = Float.parseFloat(v.substring(s2 + 1, v.length() - 1));
/* 392 */         return set(x, y, width, height);
/* 393 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */     
/* 397 */     throw new GdxRuntimeException("Malformed Rectangle: " + v);
/*     */   }
/*     */   
/*     */   public float area() {
/* 401 */     return this.width * this.height;
/*     */   }
/*     */   
/*     */   public float perimeter() {
/* 405 */     return 2.0F * (this.width + this.height);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 409 */     int prime = 31;
/* 410 */     int result = 1;
/* 411 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.height);
/* 412 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.width);
/* 413 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.x);
/* 414 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.y);
/* 415 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 419 */     if (this == obj) return true; 
/* 420 */     if (obj == null) return false; 
/* 421 */     if (getClass() != obj.getClass()) return false; 
/* 422 */     Rectangle other = (Rectangle)obj;
/* 423 */     if (NumberUtils.floatToRawIntBits(this.height) != NumberUtils.floatToRawIntBits(other.height)) return false; 
/* 424 */     if (NumberUtils.floatToRawIntBits(this.width) != NumberUtils.floatToRawIntBits(other.width)) return false; 
/* 425 */     if (NumberUtils.floatToRawIntBits(this.x) != NumberUtils.floatToRawIntBits(other.x)) return false; 
/* 426 */     if (NumberUtils.floatToRawIntBits(this.y) != NumberUtils.floatToRawIntBits(other.y)) return false; 
/* 427 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Rectangle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */