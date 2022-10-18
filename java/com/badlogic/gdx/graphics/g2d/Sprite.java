/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
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
/*     */ public class Sprite
/*     */   extends TextureRegion
/*     */ {
/*     */   static final int VERTEX_SIZE = 5;
/*     */   static final int SPRITE_SIZE = 20;
/*  39 */   final float[] vertices = new float[20]; private float x; private float y; float width; float height;
/*  40 */   private final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   
/*     */   private float originX;
/*     */   private float originY;
/*     */   private float rotation;
/*  45 */   private float scaleX = 1.0F; private float scaleY = 1.0F;
/*     */   
/*     */   private boolean dirty = true;
/*     */   private Rectangle bounds;
/*     */   
/*     */   public Sprite() {
/*  51 */     setColor(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Sprite(Texture texture) {
/*  56 */     this(texture, 0, 0, texture.getWidth(), texture.getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sprite(Texture texture, int srcWidth, int srcHeight) {
/*  64 */     this(texture, 0, 0, srcWidth, srcHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
/*  71 */     if (texture == null) throw new IllegalArgumentException("texture cannot be null."); 
/*  72 */     this.texture = texture;
/*  73 */     setRegion(srcX, srcY, srcWidth, srcHeight);
/*  74 */     setColor(1.0F, 1.0F, 1.0F, 1.0F);
/*  75 */     setSize(Math.abs(srcWidth), Math.abs(srcHeight));
/*  76 */     setOrigin(this.width / 2.0F, this.height / 2.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sprite(TextureRegion region) {
/*  83 */     setRegion(region);
/*  84 */     setColor(1.0F, 1.0F, 1.0F, 1.0F);
/*  85 */     setSize(region.getRegionWidth(), region.getRegionHeight());
/*  86 */     setOrigin(this.width / 2.0F, this.height / 2.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sprite(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
/*  94 */     setRegion(region, srcX, srcY, srcWidth, srcHeight);
/*  95 */     setColor(1.0F, 1.0F, 1.0F, 1.0F);
/*  96 */     setSize(Math.abs(srcWidth), Math.abs(srcHeight));
/*  97 */     setOrigin(this.width / 2.0F, this.height / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Sprite(Sprite sprite) {
/* 102 */     set(sprite);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Sprite sprite) {
/* 107 */     if (sprite == null) throw new IllegalArgumentException("sprite cannot be null."); 
/* 108 */     System.arraycopy(sprite.vertices, 0, this.vertices, 0, 20);
/* 109 */     this.texture = sprite.texture;
/* 110 */     this.u = sprite.u;
/* 111 */     this.v = sprite.v;
/* 112 */     this.u2 = sprite.u2;
/* 113 */     this.v2 = sprite.v2;
/* 114 */     this.x = sprite.x;
/* 115 */     this.y = sprite.y;
/* 116 */     this.width = sprite.width;
/* 117 */     this.height = sprite.height;
/* 118 */     this.regionWidth = sprite.regionWidth;
/* 119 */     this.regionHeight = sprite.regionHeight;
/* 120 */     this.originX = sprite.originX;
/* 121 */     this.originY = sprite.originY;
/* 122 */     this.rotation = sprite.rotation;
/* 123 */     this.scaleX = sprite.scaleX;
/* 124 */     this.scaleY = sprite.scaleY;
/* 125 */     this.color.set(sprite.color);
/* 126 */     this.dirty = sprite.dirty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBounds(float x, float y, float width, float height) {
/* 132 */     this.x = x;
/* 133 */     this.y = y;
/* 134 */     this.width = width;
/* 135 */     this.height = height;
/*     */     
/* 137 */     if (this.dirty)
/*     */       return; 
/* 139 */     float x2 = x + width;
/* 140 */     float y2 = y + height;
/* 141 */     float[] vertices = this.vertices;
/* 142 */     vertices[0] = x;
/* 143 */     vertices[1] = y;
/*     */     
/* 145 */     vertices[5] = x;
/* 146 */     vertices[6] = y2;
/*     */     
/* 148 */     vertices[10] = x2;
/* 149 */     vertices[11] = y2;
/*     */     
/* 151 */     vertices[15] = x2;
/* 152 */     vertices[16] = y;
/*     */     
/* 154 */     if (this.rotation != 0.0F || this.scaleX != 1.0F || this.scaleY != 1.0F) this.dirty = true;
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(float width, float height) {
/* 161 */     this.width = width;
/* 162 */     this.height = height;
/*     */     
/* 164 */     if (this.dirty)
/*     */       return; 
/* 166 */     float x2 = this.x + width;
/* 167 */     float y2 = this.y + height;
/* 168 */     float[] vertices = this.vertices;
/* 169 */     vertices[0] = this.x;
/* 170 */     vertices[1] = this.y;
/*     */     
/* 172 */     vertices[5] = this.x;
/* 173 */     vertices[6] = y2;
/*     */     
/* 175 */     vertices[10] = x2;
/* 176 */     vertices[11] = y2;
/*     */     
/* 178 */     vertices[15] = x2;
/* 179 */     vertices[16] = this.y;
/*     */     
/* 181 */     if (this.rotation != 0.0F || this.scaleX != 1.0F || this.scaleY != 1.0F) this.dirty = true;
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 188 */     translate(x - this.x, y - this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(float x) {
/* 195 */     translateX(x - this.x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(float y) {
/* 202 */     translateY(y - this.y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCenterX(float x) {
/* 207 */     setX(x - this.width / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCenterY(float y) {
/* 212 */     setY(y - this.height / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCenter(float x, float y) {
/* 217 */     setCenterX(x);
/* 218 */     setCenterY(y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void translateX(float xAmount) {
/* 224 */     this.x += xAmount;
/*     */     
/* 226 */     if (this.dirty)
/*     */       return; 
/* 228 */     float[] vertices = this.vertices;
/* 229 */     vertices[0] = vertices[0] + xAmount;
/* 230 */     vertices[5] = vertices[5] + xAmount;
/* 231 */     vertices[10] = vertices[10] + xAmount;
/* 232 */     vertices[15] = vertices[15] + xAmount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void translateY(float yAmount) {
/* 238 */     this.y += yAmount;
/*     */     
/* 240 */     if (this.dirty)
/*     */       return; 
/* 242 */     float[] vertices = this.vertices;
/* 243 */     vertices[1] = vertices[1] + yAmount;
/* 244 */     vertices[6] = vertices[6] + yAmount;
/* 245 */     vertices[11] = vertices[11] + yAmount;
/* 246 */     vertices[16] = vertices[16] + yAmount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void translate(float xAmount, float yAmount) {
/* 252 */     this.x += xAmount;
/* 253 */     this.y += yAmount;
/*     */     
/* 255 */     if (this.dirty)
/*     */       return; 
/* 257 */     float[] vertices = this.vertices;
/* 258 */     vertices[0] = vertices[0] + xAmount;
/* 259 */     vertices[1] = vertices[1] + yAmount;
/*     */     
/* 261 */     vertices[5] = vertices[5] + xAmount;
/* 262 */     vertices[6] = vertices[6] + yAmount;
/*     */     
/* 264 */     vertices[10] = vertices[10] + xAmount;
/* 265 */     vertices[11] = vertices[11] + yAmount;
/*     */     
/* 267 */     vertices[15] = vertices[15] + xAmount;
/* 268 */     vertices[16] = vertices[16] + yAmount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(Color tint) {
/* 273 */     float color = tint.toFloatBits();
/* 274 */     float[] vertices = this.vertices;
/* 275 */     vertices[2] = color;
/* 276 */     vertices[7] = color;
/* 277 */     vertices[12] = color;
/* 278 */     vertices[17] = color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAlpha(float a) {
/* 283 */     int intBits = NumberUtils.floatToIntColor(this.vertices[2]);
/* 284 */     int alphaBits = (int)(255.0F * a) << 24;
/*     */ 
/*     */     
/* 287 */     intBits &= 0xFFFFFF;
/*     */     
/* 289 */     intBits |= alphaBits;
/* 290 */     float color = NumberUtils.intToFloatColor(intBits);
/* 291 */     this.vertices[2] = color;
/* 292 */     this.vertices[7] = color;
/* 293 */     this.vertices[12] = color;
/* 294 */     this.vertices[17] = color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 299 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/* 300 */     float color = NumberUtils.intToFloatColor(intBits);
/* 301 */     float[] vertices = this.vertices;
/* 302 */     vertices[2] = color;
/* 303 */     vertices[7] = color;
/* 304 */     vertices[12] = color;
/* 305 */     vertices[17] = color;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(float color) {
/* 311 */     float[] vertices = this.vertices;
/* 312 */     vertices[2] = color;
/* 313 */     vertices[7] = color;
/* 314 */     vertices[12] = color;
/* 315 */     vertices[17] = color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrigin(float originX, float originY) {
/* 320 */     this.originX = originX;
/* 321 */     this.originY = originY;
/* 322 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOriginCenter() {
/* 327 */     this.originX = this.width / 2.0F;
/* 328 */     this.originY = this.height / 2.0F;
/* 329 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotation(float degrees) {
/* 334 */     this.rotation = degrees;
/* 335 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRotation() {
/* 340 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(float degrees) {
/* 346 */     if (degrees == 0.0F)
/* 347 */       return;  this.rotation += degrees;
/* 348 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate90(boolean clockwise) {
/* 354 */     float[] vertices = this.vertices;
/*     */     
/* 356 */     if (clockwise) {
/* 357 */       float temp = vertices[4];
/* 358 */       vertices[4] = vertices[19];
/* 359 */       vertices[19] = vertices[14];
/* 360 */       vertices[14] = vertices[9];
/* 361 */       vertices[9] = temp;
/*     */       
/* 363 */       temp = vertices[3];
/* 364 */       vertices[3] = vertices[18];
/* 365 */       vertices[18] = vertices[13];
/* 366 */       vertices[13] = vertices[8];
/* 367 */       vertices[8] = temp;
/*     */     } else {
/* 369 */       float temp = vertices[4];
/* 370 */       vertices[4] = vertices[9];
/* 371 */       vertices[9] = vertices[14];
/* 372 */       vertices[14] = vertices[19];
/* 373 */       vertices[19] = temp;
/*     */       
/* 375 */       temp = vertices[3];
/* 376 */       vertices[3] = vertices[8];
/* 377 */       vertices[8] = vertices[13];
/* 378 */       vertices[13] = vertices[18];
/* 379 */       vertices[18] = temp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScale(float scaleXY) {
/* 386 */     this.scaleX = scaleXY;
/* 387 */     this.scaleY = scaleXY;
/* 388 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScale(float scaleX, float scaleY) {
/* 394 */     this.scaleX = scaleX;
/* 395 */     this.scaleY = scaleY;
/* 396 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scale(float amount) {
/* 403 */     this.scaleX += amount;
/* 404 */     this.scaleY += amount;
/* 405 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getVertices() {
/* 410 */     if (this.dirty) {
/* 411 */       this.dirty = false;
/*     */       
/* 413 */       float[] vertices = this.vertices;
/* 414 */       float localX = -this.originX;
/* 415 */       float localY = -this.originY;
/* 416 */       float localX2 = localX + this.width;
/* 417 */       float localY2 = localY + this.height;
/* 418 */       float worldOriginX = this.x - localX;
/* 419 */       float worldOriginY = this.y - localY;
/* 420 */       if (this.scaleX != 1.0F || this.scaleY != 1.0F) {
/* 421 */         localX *= this.scaleX;
/* 422 */         localY *= this.scaleY;
/* 423 */         localX2 *= this.scaleX;
/* 424 */         localY2 *= this.scaleY;
/*     */       } 
/* 426 */       if (this.rotation != 0.0F) {
/* 427 */         float cos = MathUtils.cosDeg(this.rotation);
/* 428 */         float sin = MathUtils.sinDeg(this.rotation);
/* 429 */         float localXCos = localX * cos;
/* 430 */         float localXSin = localX * sin;
/* 431 */         float localYCos = localY * cos;
/* 432 */         float localYSin = localY * sin;
/* 433 */         float localX2Cos = localX2 * cos;
/* 434 */         float localX2Sin = localX2 * sin;
/* 435 */         float localY2Cos = localY2 * cos;
/* 436 */         float localY2Sin = localY2 * sin;
/*     */         
/* 438 */         float x1 = localXCos - localYSin + worldOriginX;
/* 439 */         float y1 = localYCos + localXSin + worldOriginY;
/* 440 */         vertices[0] = x1;
/* 441 */         vertices[1] = y1;
/*     */         
/* 443 */         float x2 = localXCos - localY2Sin + worldOriginX;
/* 444 */         float y2 = localY2Cos + localXSin + worldOriginY;
/* 445 */         vertices[5] = x2;
/* 446 */         vertices[6] = y2;
/*     */         
/* 448 */         float x3 = localX2Cos - localY2Sin + worldOriginX;
/* 449 */         float y3 = localY2Cos + localX2Sin + worldOriginY;
/* 450 */         vertices[10] = x3;
/* 451 */         vertices[11] = y3;
/*     */         
/* 453 */         vertices[15] = x1 + x3 - x2;
/* 454 */         vertices[16] = y3 - y2 - y1;
/*     */       } else {
/* 456 */         float x1 = localX + worldOriginX;
/* 457 */         float y1 = localY + worldOriginY;
/* 458 */         float x2 = localX2 + worldOriginX;
/* 459 */         float y2 = localY2 + worldOriginY;
/*     */         
/* 461 */         vertices[0] = x1;
/* 462 */         vertices[1] = y1;
/*     */         
/* 464 */         vertices[5] = x1;
/* 465 */         vertices[6] = y2;
/*     */         
/* 467 */         vertices[10] = x2;
/* 468 */         vertices[11] = y2;
/*     */         
/* 470 */         vertices[15] = x2;
/* 471 */         vertices[16] = y1;
/*     */       } 
/*     */     } 
/* 474 */     return this.vertices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBoundingRectangle() {
/* 483 */     float[] vertices = getVertices();
/*     */     
/* 485 */     float minx = vertices[0];
/* 486 */     float miny = vertices[1];
/* 487 */     float maxx = vertices[0];
/* 488 */     float maxy = vertices[1];
/*     */     
/* 490 */     minx = (minx > vertices[5]) ? vertices[5] : minx;
/* 491 */     minx = (minx > vertices[10]) ? vertices[10] : minx;
/* 492 */     minx = (minx > vertices[15]) ? vertices[15] : minx;
/*     */     
/* 494 */     maxx = (maxx < vertices[5]) ? vertices[5] : maxx;
/* 495 */     maxx = (maxx < vertices[10]) ? vertices[10] : maxx;
/* 496 */     maxx = (maxx < vertices[15]) ? vertices[15] : maxx;
/*     */     
/* 498 */     miny = (miny > vertices[6]) ? vertices[6] : miny;
/* 499 */     miny = (miny > vertices[11]) ? vertices[11] : miny;
/* 500 */     miny = (miny > vertices[16]) ? vertices[16] : miny;
/*     */     
/* 502 */     maxy = (maxy < vertices[6]) ? vertices[6] : maxy;
/* 503 */     maxy = (maxy < vertices[11]) ? vertices[11] : maxy;
/* 504 */     maxy = (maxy < vertices[16]) ? vertices[16] : maxy;
/*     */     
/* 506 */     if (this.bounds == null) this.bounds = new Rectangle(); 
/* 507 */     this.bounds.x = minx;
/* 508 */     this.bounds.y = miny;
/* 509 */     this.bounds.width = maxx - minx;
/* 510 */     this.bounds.height = maxy - miny;
/* 511 */     return this.bounds;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch) {
/* 515 */     batch.draw(this.texture, getVertices(), 0, 20);
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float alphaModulation) {
/* 519 */     float oldAlpha = (getColor()).a;
/* 520 */     setAlpha(oldAlpha * alphaModulation);
/* 521 */     draw(batch);
/* 522 */     setAlpha(oldAlpha);
/*     */   }
/*     */   
/*     */   public float getX() {
/* 526 */     return this.x;
/*     */   }
/*     */   
/*     */   public float getY() {
/* 530 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 535 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 540 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOriginX() {
/* 546 */     return this.originX;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOriginY() {
/* 552 */     return this.originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleX() {
/* 557 */     return this.scaleX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleY() {
/* 562 */     return this.scaleY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 568 */     int intBits = NumberUtils.floatToIntColor(this.vertices[2]);
/* 569 */     Color color = this.color;
/* 570 */     color.r = (intBits & 0xFF) / 255.0F;
/* 571 */     color.g = (intBits >>> 8 & 0xFF) / 255.0F;
/* 572 */     color.b = (intBits >>> 16 & 0xFF) / 255.0F;
/* 573 */     color.a = (intBits >>> 24 & 0xFF) / 255.0F;
/* 574 */     return color;
/*     */   }
/*     */   
/*     */   public void setRegion(float u, float v, float u2, float v2) {
/* 578 */     super.setRegion(u, v, u2, v2);
/*     */     
/* 580 */     float[] vertices = this.vertices;
/* 581 */     vertices[3] = u;
/* 582 */     vertices[4] = v2;
/*     */     
/* 584 */     vertices[8] = u;
/* 585 */     vertices[9] = v;
/*     */     
/* 587 */     vertices[13] = u2;
/* 588 */     vertices[14] = v;
/*     */     
/* 590 */     vertices[18] = u2;
/* 591 */     vertices[19] = v2;
/*     */   }
/*     */   
/*     */   public void setU(float u) {
/* 595 */     super.setU(u);
/* 596 */     this.vertices[3] = u;
/* 597 */     this.vertices[8] = u;
/*     */   }
/*     */   
/*     */   public void setV(float v) {
/* 601 */     super.setV(v);
/* 602 */     this.vertices[9] = v;
/* 603 */     this.vertices[14] = v;
/*     */   }
/*     */   
/*     */   public void setU2(float u2) {
/* 607 */     super.setU2(u2);
/* 608 */     this.vertices[13] = u2;
/* 609 */     this.vertices[18] = u2;
/*     */   }
/*     */   
/*     */   public void setV2(float v2) {
/* 613 */     super.setV2(v2);
/* 614 */     this.vertices[4] = v2;
/* 615 */     this.vertices[19] = v2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFlip(boolean x, boolean y) {
/* 622 */     boolean performX = false;
/* 623 */     boolean performY = false;
/* 624 */     if (isFlipX() != x) {
/* 625 */       performX = true;
/*     */     }
/* 627 */     if (isFlipY() != y) {
/* 628 */       performY = true;
/*     */     }
/* 630 */     flip(performX, performY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flip(boolean x, boolean y) {
/* 637 */     super.flip(x, y);
/* 638 */     float[] vertices = this.vertices;
/* 639 */     if (x) {
/* 640 */       float temp = vertices[3];
/* 641 */       vertices[3] = vertices[13];
/* 642 */       vertices[13] = temp;
/* 643 */       temp = vertices[8];
/* 644 */       vertices[8] = vertices[18];
/* 645 */       vertices[18] = temp;
/*     */     } 
/* 647 */     if (y) {
/* 648 */       float temp = vertices[4];
/* 649 */       vertices[4] = vertices[14];
/* 650 */       vertices[14] = temp;
/* 651 */       temp = vertices[9];
/* 652 */       vertices[9] = vertices[19];
/* 653 */       vertices[19] = temp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void scroll(float xAmount, float yAmount) {
/* 658 */     float[] vertices = this.vertices;
/* 659 */     if (xAmount != 0.0F) {
/* 660 */       float u = (vertices[3] + xAmount) % 1.0F;
/* 661 */       float u2 = u + this.width / this.texture.getWidth();
/* 662 */       this.u = u;
/* 663 */       this.u2 = u2;
/* 664 */       vertices[3] = u;
/* 665 */       vertices[8] = u;
/* 666 */       vertices[13] = u2;
/* 667 */       vertices[18] = u2;
/*     */     } 
/* 669 */     if (yAmount != 0.0F) {
/* 670 */       float v = (vertices[9] + yAmount) % 1.0F;
/* 671 */       float v2 = v + this.height / this.texture.getHeight();
/* 672 */       this.v = v;
/* 673 */       this.v2 = v2;
/* 674 */       vertices[4] = v2;
/* 675 */       vertices[9] = v;
/* 676 */       vertices[14] = v;
/* 677 */       vertices[19] = v2;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\Sprite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */