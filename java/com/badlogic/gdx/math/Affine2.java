/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Affine2
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1524569123485049187L;
/*  32 */   public float m00 = 1.0F, m01 = 0.0F, m02 = 0.0F;
/*  33 */   public float m10 = 0.0F, m11 = 1.0F, m12 = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2(Affine2 other) {
/*  45 */     set(other);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 idt() {
/*  51 */     this.m00 = 1.0F;
/*  52 */     this.m01 = 0.0F;
/*  53 */     this.m02 = 0.0F;
/*  54 */     this.m10 = 0.0F;
/*  55 */     this.m11 = 1.0F;
/*  56 */     this.m12 = 0.0F;
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 set(Affine2 other) {
/*  64 */     this.m00 = other.m00;
/*  65 */     this.m01 = other.m01;
/*  66 */     this.m02 = other.m02;
/*  67 */     this.m10 = other.m10;
/*  68 */     this.m11 = other.m11;
/*  69 */     this.m12 = other.m12;
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 set(Matrix3 matrix) {
/*  77 */     float[] other = matrix.val;
/*     */     
/*  79 */     this.m00 = other[0];
/*  80 */     this.m01 = other[3];
/*  81 */     this.m02 = other[6];
/*  82 */     this.m10 = other[1];
/*  83 */     this.m11 = other[4];
/*  84 */     this.m12 = other[7];
/*  85 */     return this;
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
/*     */   
/*     */   public Affine2 set(Matrix4 matrix) {
/*  98 */     float[] other = matrix.val;
/*     */     
/* 100 */     this.m00 = other[0];
/* 101 */     this.m01 = other[4];
/* 102 */     this.m02 = other[12];
/* 103 */     this.m10 = other[1];
/* 104 */     this.m11 = other[5];
/* 105 */     this.m12 = other[13];
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToTranslation(float x, float y) {
/* 114 */     this.m00 = 1.0F;
/* 115 */     this.m01 = 0.0F;
/* 116 */     this.m02 = x;
/* 117 */     this.m10 = 0.0F;
/* 118 */     this.m11 = 1.0F;
/* 119 */     this.m12 = y;
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToTranslation(Vector2 trn) {
/* 127 */     return setToTranslation(trn.x, trn.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToScaling(float scaleX, float scaleY) {
/* 135 */     this.m00 = scaleX;
/* 136 */     this.m01 = 0.0F;
/* 137 */     this.m02 = 0.0F;
/* 138 */     this.m10 = 0.0F;
/* 139 */     this.m11 = scaleY;
/* 140 */     this.m12 = 0.0F;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToScaling(Vector2 scale) {
/* 148 */     return setToScaling(scale.x, scale.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToRotation(float degrees) {
/* 155 */     float cos = MathUtils.cosDeg(degrees);
/* 156 */     float sin = MathUtils.sinDeg(degrees);
/*     */     
/* 158 */     this.m00 = cos;
/* 159 */     this.m01 = -sin;
/* 160 */     this.m02 = 0.0F;
/* 161 */     this.m10 = sin;
/* 162 */     this.m11 = cos;
/* 163 */     this.m12 = 0.0F;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToRotationRad(float radians) {
/* 171 */     float cos = MathUtils.cos(radians);
/* 172 */     float sin = MathUtils.sin(radians);
/*     */     
/* 174 */     this.m00 = cos;
/* 175 */     this.m01 = -sin;
/* 176 */     this.m02 = 0.0F;
/* 177 */     this.m10 = sin;
/* 178 */     this.m11 = cos;
/* 179 */     this.m12 = 0.0F;
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToRotation(float cos, float sin) {
/* 188 */     this.m00 = cos;
/* 189 */     this.m01 = -sin;
/* 190 */     this.m02 = 0.0F;
/* 191 */     this.m10 = sin;
/* 192 */     this.m11 = cos;
/* 193 */     this.m12 = 0.0F;
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToShearing(float shearX, float shearY) {
/* 202 */     this.m00 = 1.0F;
/* 203 */     this.m01 = shearX;
/* 204 */     this.m02 = 0.0F;
/* 205 */     this.m10 = shearY;
/* 206 */     this.m11 = 1.0F;
/* 207 */     this.m12 = 0.0F;
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToShearing(Vector2 shear) {
/* 215 */     return setToShearing(shear.x, shear.y);
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
/*     */   public Affine2 setToTrnRotScl(float x, float y, float degrees, float scaleX, float scaleY) {
/* 227 */     this.m02 = x;
/* 228 */     this.m12 = y;
/*     */     
/* 230 */     if (degrees == 0.0F) {
/* 231 */       this.m00 = scaleX;
/* 232 */       this.m01 = 0.0F;
/* 233 */       this.m10 = 0.0F;
/* 234 */       this.m11 = scaleY;
/*     */     } else {
/* 236 */       float sin = MathUtils.sinDeg(degrees);
/* 237 */       float cos = MathUtils.cosDeg(degrees);
/*     */       
/* 239 */       this.m00 = cos * scaleX;
/* 240 */       this.m01 = -sin * scaleY;
/* 241 */       this.m10 = sin * scaleX;
/* 242 */       this.m11 = cos * scaleY;
/*     */     } 
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToTrnRotScl(Vector2 trn, float degrees, Vector2 scale) {
/* 254 */     return setToTrnRotScl(trn.x, trn.y, degrees, scale.x, scale.y);
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
/*     */   public Affine2 setToTrnRotRadScl(float x, float y, float radians, float scaleX, float scaleY) {
/* 266 */     this.m02 = x;
/* 267 */     this.m12 = y;
/*     */     
/* 269 */     if (radians == 0.0F) {
/* 270 */       this.m00 = scaleX;
/* 271 */       this.m01 = 0.0F;
/* 272 */       this.m10 = 0.0F;
/* 273 */       this.m11 = scaleY;
/*     */     } else {
/* 275 */       float sin = MathUtils.sin(radians);
/* 276 */       float cos = MathUtils.cos(radians);
/*     */       
/* 278 */       this.m00 = cos * scaleX;
/* 279 */       this.m01 = -sin * scaleY;
/* 280 */       this.m10 = sin * scaleX;
/* 281 */       this.m11 = cos * scaleY;
/*     */     } 
/* 283 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToTrnRotRadScl(Vector2 trn, float radians, Vector2 scale) {
/* 293 */     return setToTrnRotRadScl(trn.x, trn.y, radians, scale.x, scale.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToTrnScl(float x, float y, float scaleX, float scaleY) {
/* 304 */     this.m00 = scaleX;
/* 305 */     this.m01 = 0.0F;
/* 306 */     this.m02 = x;
/* 307 */     this.m10 = 0.0F;
/* 308 */     this.m11 = scaleY;
/* 309 */     this.m12 = y;
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToTrnScl(Vector2 trn, Vector2 scale) {
/* 319 */     return setToTrnScl(trn.x, trn.y, scale.x, scale.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 setToProduct(Affine2 l, Affine2 r) {
/* 327 */     this.m00 = l.m00 * r.m00 + l.m01 * r.m10;
/* 328 */     this.m01 = l.m00 * r.m01 + l.m01 * r.m11;
/* 329 */     this.m02 = l.m00 * r.m02 + l.m01 * r.m12 + l.m02;
/* 330 */     this.m10 = l.m10 * r.m00 + l.m11 * r.m10;
/* 331 */     this.m11 = l.m10 * r.m01 + l.m11 * r.m11;
/* 332 */     this.m12 = l.m10 * r.m02 + l.m11 * r.m12 + l.m12;
/* 333 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 inv() {
/* 340 */     float det = det();
/* 341 */     if (det == 0.0F) throw new GdxRuntimeException("Can't invert a singular affine matrix");
/*     */     
/* 343 */     float invDet = 1.0F / det;
/*     */     
/* 345 */     float tmp00 = this.m11;
/* 346 */     float tmp01 = -this.m01;
/* 347 */     float tmp02 = this.m01 * this.m12 - this.m11 * this.m02;
/* 348 */     float tmp10 = -this.m10;
/* 349 */     float tmp11 = this.m00;
/* 350 */     float tmp12 = this.m10 * this.m02 - this.m00 * this.m12;
/*     */     
/* 352 */     this.m00 = invDet * tmp00;
/* 353 */     this.m01 = invDet * tmp01;
/* 354 */     this.m02 = invDet * tmp02;
/* 355 */     this.m10 = invDet * tmp10;
/* 356 */     this.m11 = invDet * tmp11;
/* 357 */     this.m12 = invDet * tmp12;
/* 358 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 mul(Affine2 other) {
/* 369 */     float tmp00 = this.m00 * other.m00 + this.m01 * other.m10;
/* 370 */     float tmp01 = this.m00 * other.m01 + this.m01 * other.m11;
/* 371 */     float tmp02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02;
/* 372 */     float tmp10 = this.m10 * other.m00 + this.m11 * other.m10;
/* 373 */     float tmp11 = this.m10 * other.m01 + this.m11 * other.m11;
/* 374 */     float tmp12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12;
/*     */     
/* 376 */     this.m00 = tmp00;
/* 377 */     this.m01 = tmp01;
/* 378 */     this.m02 = tmp02;
/* 379 */     this.m10 = tmp10;
/* 380 */     this.m11 = tmp11;
/* 381 */     this.m12 = tmp12;
/* 382 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preMul(Affine2 other) {
/* 393 */     float tmp00 = other.m00 * this.m00 + other.m01 * this.m10;
/* 394 */     float tmp01 = other.m00 * this.m01 + other.m01 * this.m11;
/* 395 */     float tmp02 = other.m00 * this.m02 + other.m01 * this.m12 + other.m02;
/* 396 */     float tmp10 = other.m10 * this.m00 + other.m11 * this.m10;
/* 397 */     float tmp11 = other.m10 * this.m01 + other.m11 * this.m11;
/* 398 */     float tmp12 = other.m10 * this.m02 + other.m11 * this.m12 + other.m12;
/*     */     
/* 400 */     this.m00 = tmp00;
/* 401 */     this.m01 = tmp01;
/* 402 */     this.m02 = tmp02;
/* 403 */     this.m10 = tmp10;
/* 404 */     this.m11 = tmp11;
/* 405 */     this.m12 = tmp12;
/* 406 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 translate(float x, float y) {
/* 414 */     this.m02 += this.m00 * x + this.m01 * y;
/* 415 */     this.m12 += this.m10 * x + this.m11 * y;
/* 416 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 translate(Vector2 trn) {
/* 423 */     return translate(trn.x, trn.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preTranslate(float x, float y) {
/* 431 */     this.m02 += x;
/* 432 */     this.m12 += y;
/* 433 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preTranslate(Vector2 trn) {
/* 440 */     return preTranslate(trn.x, trn.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 scale(float scaleX, float scaleY) {
/* 448 */     this.m00 *= scaleX;
/* 449 */     this.m01 *= scaleY;
/* 450 */     this.m10 *= scaleX;
/* 451 */     this.m11 *= scaleY;
/* 452 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 scale(Vector2 scale) {
/* 459 */     return scale(scale.x, scale.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preScale(float scaleX, float scaleY) {
/* 467 */     this.m00 *= scaleX;
/* 468 */     this.m01 *= scaleX;
/* 469 */     this.m02 *= scaleX;
/* 470 */     this.m10 *= scaleY;
/* 471 */     this.m11 *= scaleY;
/* 472 */     this.m12 *= scaleY;
/* 473 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preScale(Vector2 scale) {
/* 480 */     return preScale(scale.x, scale.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 rotate(float degrees) {
/* 487 */     if (degrees == 0.0F) return this;
/*     */     
/* 489 */     float cos = MathUtils.cosDeg(degrees);
/* 490 */     float sin = MathUtils.sinDeg(degrees);
/*     */     
/* 492 */     float tmp00 = this.m00 * cos + this.m01 * sin;
/* 493 */     float tmp01 = this.m00 * -sin + this.m01 * cos;
/* 494 */     float tmp10 = this.m10 * cos + this.m11 * sin;
/* 495 */     float tmp11 = this.m10 * -sin + this.m11 * cos;
/*     */     
/* 497 */     this.m00 = tmp00;
/* 498 */     this.m01 = tmp01;
/* 499 */     this.m10 = tmp10;
/* 500 */     this.m11 = tmp11;
/* 501 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 rotateRad(float radians) {
/* 508 */     if (radians == 0.0F) return this;
/*     */     
/* 510 */     float cos = MathUtils.cos(radians);
/* 511 */     float sin = MathUtils.sin(radians);
/*     */     
/* 513 */     float tmp00 = this.m00 * cos + this.m01 * sin;
/* 514 */     float tmp01 = this.m00 * -sin + this.m01 * cos;
/* 515 */     float tmp10 = this.m10 * cos + this.m11 * sin;
/* 516 */     float tmp11 = this.m10 * -sin + this.m11 * cos;
/*     */     
/* 518 */     this.m00 = tmp00;
/* 519 */     this.m01 = tmp01;
/* 520 */     this.m10 = tmp10;
/* 521 */     this.m11 = tmp11;
/* 522 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preRotate(float degrees) {
/* 529 */     if (degrees == 0.0F) return this;
/*     */     
/* 531 */     float cos = MathUtils.cosDeg(degrees);
/* 532 */     float sin = MathUtils.sinDeg(degrees);
/*     */     
/* 534 */     float tmp00 = cos * this.m00 - sin * this.m10;
/* 535 */     float tmp01 = cos * this.m01 - sin * this.m11;
/* 536 */     float tmp02 = cos * this.m02 - sin * this.m12;
/* 537 */     float tmp10 = sin * this.m00 + cos * this.m10;
/* 538 */     float tmp11 = sin * this.m01 + cos * this.m11;
/* 539 */     float tmp12 = sin * this.m02 + cos * this.m12;
/*     */     
/* 541 */     this.m00 = tmp00;
/* 542 */     this.m01 = tmp01;
/* 543 */     this.m02 = tmp02;
/* 544 */     this.m10 = tmp10;
/* 545 */     this.m11 = tmp11;
/* 546 */     this.m12 = tmp12;
/* 547 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preRotateRad(float radians) {
/* 554 */     if (radians == 0.0F) return this;
/*     */     
/* 556 */     float cos = MathUtils.cos(radians);
/* 557 */     float sin = MathUtils.sin(radians);
/*     */     
/* 559 */     float tmp00 = cos * this.m00 - sin * this.m10;
/* 560 */     float tmp01 = cos * this.m01 - sin * this.m11;
/* 561 */     float tmp02 = cos * this.m02 - sin * this.m12;
/* 562 */     float tmp10 = sin * this.m00 + cos * this.m10;
/* 563 */     float tmp11 = sin * this.m01 + cos * this.m11;
/* 564 */     float tmp12 = sin * this.m02 + cos * this.m12;
/*     */     
/* 566 */     this.m00 = tmp00;
/* 567 */     this.m01 = tmp01;
/* 568 */     this.m02 = tmp02;
/* 569 */     this.m10 = tmp10;
/* 570 */     this.m11 = tmp11;
/* 571 */     this.m12 = tmp12;
/* 572 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 shear(float shearX, float shearY) {
/* 580 */     float tmp0 = this.m00 + shearY * this.m01;
/* 581 */     float tmp1 = this.m01 + shearX * this.m00;
/* 582 */     this.m00 = tmp0;
/* 583 */     this.m01 = tmp1;
/*     */     
/* 585 */     tmp0 = this.m10 + shearY * this.m11;
/* 586 */     tmp1 = this.m11 + shearX * this.m10;
/* 587 */     this.m10 = tmp0;
/* 588 */     this.m11 = tmp1;
/* 589 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 shear(Vector2 shear) {
/* 596 */     return shear(shear.x, shear.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preShear(float shearX, float shearY) {
/* 604 */     float tmp00 = this.m00 + shearX * this.m10;
/* 605 */     float tmp01 = this.m01 + shearX * this.m11;
/* 606 */     float tmp02 = this.m02 + shearX * this.m12;
/* 607 */     float tmp10 = this.m10 + shearY * this.m00;
/* 608 */     float tmp11 = this.m11 + shearY * this.m01;
/* 609 */     float tmp12 = this.m12 + shearY * this.m02;
/*     */     
/* 611 */     this.m00 = tmp00;
/* 612 */     this.m01 = tmp01;
/* 613 */     this.m02 = tmp02;
/* 614 */     this.m10 = tmp10;
/* 615 */     this.m11 = tmp11;
/* 616 */     this.m12 = tmp12;
/* 617 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Affine2 preShear(Vector2 shear) {
/* 624 */     return preShear(shear.x, shear.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float det() {
/* 630 */     return this.m00 * this.m11 - this.m01 * this.m10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 getTranslation(Vector2 position) {
/* 637 */     position.x = this.m02;
/* 638 */     position.y = this.m12;
/* 639 */     return position;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTranslation() {
/* 645 */     return (this.m00 == 1.0F && this.m11 == 1.0F && this.m01 == 0.0F && this.m10 == 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIdt() {
/* 651 */     return (this.m00 == 1.0F && this.m02 == 0.0F && this.m12 == 0.0F && this.m11 == 1.0F && this.m01 == 0.0F && this.m10 == 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Vector2 point) {
/* 656 */     float x = point.x;
/* 657 */     float y = point.y;
/* 658 */     point.x = this.m00 * x + this.m01 * y + this.m02;
/* 659 */     point.y = this.m10 * x + this.m11 * y + this.m12;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 664 */     return "[" + this.m00 + "|" + this.m01 + "|" + this.m02 + "]\n[" + this.m10 + "|" + this.m11 + "|" + this.m12 + "]\n[0.0|0.0|0.1]";
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Affine2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */