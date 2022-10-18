/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.math.Affine2;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class CpuSpriteBatch
/*     */   extends SpriteBatch
/*     */ {
/*  35 */   private final Matrix4 virtualMatrix = new Matrix4();
/*  36 */   private final Affine2 adjustAffine = new Affine2();
/*     */   
/*     */   private boolean adjustNeeded;
/*     */   private boolean haveIdentityRealMatrix = true;
/*  40 */   private final Affine2 tmpAffine = new Affine2();
/*     */ 
/*     */ 
/*     */   
/*     */   public CpuSpriteBatch() {
/*  45 */     this(1000);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CpuSpriteBatch(int size) {
/*  51 */     this(size, (ShaderProgram)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CpuSpriteBatch(int size, ShaderProgram defaultShader) {
/*  57 */     super(size, defaultShader);
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
/*     */   public void flushAndSyncTransformMatrix() {
/*  70 */     flush();
/*     */     
/*  72 */     if (this.adjustNeeded) {
/*     */       
/*  74 */       this.haveIdentityRealMatrix = checkIdt(this.virtualMatrix);
/*     */       
/*  76 */       if (!this.haveIdentityRealMatrix && this.virtualMatrix.det() == 0.0F) {
/*  77 */         throw new GdxRuntimeException("Transform matrix is singular, can't sync");
/*     */       }
/*  79 */       this.adjustNeeded = false;
/*  80 */       super.setTransformMatrix(this.virtualMatrix);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Matrix4 getTransformMatrix() {
/*  86 */     return this.adjustNeeded ? this.virtualMatrix : super.getTransformMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransformMatrix(Matrix4 transform) {
/*  95 */     Matrix4 realMatrix = super.getTransformMatrix();
/*     */     
/*  97 */     if (checkEqual(realMatrix, transform)) {
/*  98 */       this.adjustNeeded = false;
/*     */     }
/* 100 */     else if (isDrawing()) {
/* 101 */       this.virtualMatrix.setAsAffine(transform);
/* 102 */       this.adjustNeeded = true;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       if (this.haveIdentityRealMatrix) {
/* 108 */         this.adjustAffine.set(transform);
/*     */       } else {
/* 110 */         this.tmpAffine.set(transform);
/* 111 */         this.adjustAffine.set(realMatrix).inv().mul(this.tmpAffine);
/*     */       } 
/*     */     } else {
/* 114 */       realMatrix.setAsAffine(transform);
/* 115 */       this.haveIdentityRealMatrix = checkIdt(realMatrix);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransformMatrix(Affine2 transform) {
/* 125 */     Matrix4 realMatrix = super.getTransformMatrix();
/*     */     
/* 127 */     if (checkEqual(realMatrix, transform)) {
/* 128 */       this.adjustNeeded = false;
/*     */     } else {
/* 130 */       this.virtualMatrix.setAsAffine(transform);
/*     */       
/* 132 */       if (isDrawing()) {
/* 133 */         this.adjustNeeded = true;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 138 */         if (this.haveIdentityRealMatrix) {
/* 139 */           this.adjustAffine.set(transform);
/*     */         } else {
/* 141 */           this.adjustAffine.set(realMatrix).inv().mul(transform);
/*     */         } 
/*     */       } else {
/* 144 */         realMatrix.setAsAffine(transform);
/* 145 */         this.haveIdentityRealMatrix = checkIdt(realMatrix);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/* 153 */     if (!this.adjustNeeded) {
/* 154 */       super.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
/*     */     } else {
/*     */       
/* 157 */       drawAdjusted(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/* 165 */     if (!this.adjustNeeded) {
/* 166 */       super.draw(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
/*     */     } else {
/* 168 */       drawAdjusted(texture, x, y, 0.0F, 0.0F, width, height, 1.0F, 1.0F, 0.0F, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
/* 174 */     if (!this.adjustNeeded) {
/* 175 */       super.draw(texture, x, y, srcX, srcY, srcWidth, srcHeight);
/*     */     } else {
/* 177 */       drawAdjusted(texture, x, y, 0.0F, 0.0F, srcWidth, srcHeight, 1.0F, 1.0F, 0.0F, srcX, srcY, srcWidth, srcHeight, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
/* 184 */     if (!this.adjustNeeded) {
/* 185 */       super.draw(texture, x, y, width, height, u, v, u2, v2);
/*     */     } else {
/* 187 */       drawAdjustedUV(texture, x, y, 0.0F, 0.0F, width, height, 1.0F, 1.0F, 0.0F, u, v, u2, v2, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Texture texture, float x, float y) {
/* 193 */     if (!this.adjustNeeded) {
/* 194 */       super.draw(texture, x, y);
/*     */     } else {
/* 196 */       drawAdjusted(texture, x, y, 0.0F, 0.0F, texture.getWidth(), texture.getHeight(), 1.0F, 1.0F, 0.0F, 0, 1, 1, 0, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Texture texture, float x, float y, float width, float height) {
/* 202 */     if (!this.adjustNeeded) {
/* 203 */       super.draw(texture, x, y, width, height);
/*     */     } else {
/* 205 */       drawAdjusted(texture, x, y, 0.0F, 0.0F, width, height, 1.0F, 1.0F, 0.0F, 0, 1, 1, 0, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(TextureRegion region, float x, float y) {
/* 211 */     if (!this.adjustNeeded) {
/* 212 */       super.draw(region, x, y);
/*     */     } else {
/* 214 */       drawAdjusted(region, x, y, 0.0F, 0.0F, region.getRegionWidth(), region.getRegionHeight(), 1.0F, 1.0F, 0.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(TextureRegion region, float x, float y, float width, float height) {
/* 220 */     if (!this.adjustNeeded) {
/* 221 */       super.draw(region, x, y, width, height);
/*     */     } else {
/* 223 */       drawAdjusted(region, x, y, 0.0F, 0.0F, width, height, 1.0F, 1.0F, 0.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/* 230 */     if (!this.adjustNeeded) {
/* 231 */       super.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
/*     */     } else {
/* 233 */       drawAdjusted(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
/* 240 */     if (!this.adjustNeeded) {
/* 241 */       super.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, clockwise);
/*     */     } else {
/* 243 */       drawAdjusted(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, clockwise);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
/* 249 */     if (count % 20 != 0) throw new GdxRuntimeException("invalid vertex count");
/*     */     
/* 251 */     if (!this.adjustNeeded) {
/* 252 */       super.draw(texture, spriteVertices, offset, count);
/*     */     } else {
/* 254 */       drawAdjusted(texture, spriteVertices, offset, count);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(TextureRegion region, float width, float height, Affine2 transform) {
/* 260 */     if (!this.adjustNeeded) {
/* 261 */       super.draw(region, width, height, transform);
/*     */     } else {
/* 263 */       drawAdjusted(region, width, height, transform);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawAdjusted(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/* 270 */     drawAdjustedUV(region.texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, region.u, region.v2, region.u2, region.v, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawAdjusted(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/* 276 */     float invTexWidth = 1.0F / texture.getWidth();
/* 277 */     float invTexHeight = 1.0F / texture.getHeight();
/*     */     
/* 279 */     float u = srcX * invTexWidth;
/* 280 */     float v = (srcY + srcHeight) * invTexHeight;
/* 281 */     float u2 = (srcX + srcWidth) * invTexWidth;
/* 282 */     float v2 = srcY * invTexHeight;
/*     */     
/* 284 */     drawAdjustedUV(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, u, v, u2, v2, flipX, flipY);
/*     */   }
/*     */   
/*     */   private void drawAdjustedUV(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, float u, float v, float u2, float v2, boolean flipX, boolean flipY) {
/*     */     float x1, y1, x2, y2, x3, y3, x4, y4;
/* 289 */     if (!this.drawing) throw new IllegalStateException("CpuSpriteBatch.begin must be called before draw.");
/*     */     
/* 291 */     if (texture != this.lastTexture)
/* 292 */     { switchTexture(texture); }
/* 293 */     else if (this.idx == this.vertices.length) { flush(); }
/*     */ 
/*     */     
/* 296 */     float worldOriginX = x + originX;
/* 297 */     float worldOriginY = y + originY;
/* 298 */     float fx = -originX;
/* 299 */     float fy = -originY;
/* 300 */     float fx2 = width - originX;
/* 301 */     float fy2 = height - originY;
/*     */ 
/*     */     
/* 304 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/* 305 */       fx *= scaleX;
/* 306 */       fy *= scaleY;
/* 307 */       fx2 *= scaleX;
/* 308 */       fy2 *= scaleY;
/*     */     } 
/*     */ 
/*     */     
/* 312 */     float p1x = fx;
/* 313 */     float p1y = fy;
/* 314 */     float p2x = fx;
/* 315 */     float p2y = fy2;
/* 316 */     float p3x = fx2;
/* 317 */     float p3y = fy2;
/* 318 */     float p4x = fx2;
/* 319 */     float p4y = fy;
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
/* 331 */     if (rotation != 0.0F) {
/* 332 */       float cos = MathUtils.cosDeg(rotation);
/* 333 */       float sin = MathUtils.sinDeg(rotation);
/*     */       
/* 335 */       x1 = cos * p1x - sin * p1y;
/* 336 */       y1 = sin * p1x + cos * p1y;
/*     */       
/* 338 */       x2 = cos * p2x - sin * p2y;
/* 339 */       y2 = sin * p2x + cos * p2y;
/*     */       
/* 341 */       x3 = cos * p3x - sin * p3y;
/* 342 */       y3 = sin * p3x + cos * p3y;
/*     */       
/* 344 */       x4 = x1 + x3 - x2;
/* 345 */       y4 = y3 - y2 - y1;
/*     */     } else {
/* 347 */       x1 = p1x;
/* 348 */       y1 = p1y;
/*     */       
/* 350 */       x2 = p2x;
/* 351 */       y2 = p2y;
/*     */       
/* 353 */       x3 = p3x;
/* 354 */       y3 = p3y;
/*     */       
/* 356 */       x4 = p4x;
/* 357 */       y4 = p4y;
/*     */     } 
/*     */     
/* 360 */     x1 += worldOriginX;
/* 361 */     y1 += worldOriginY;
/* 362 */     x2 += worldOriginX;
/* 363 */     y2 += worldOriginY;
/* 364 */     x3 += worldOriginX;
/* 365 */     y3 += worldOriginY;
/* 366 */     x4 += worldOriginX;
/* 367 */     y4 += worldOriginY;
/*     */     
/* 369 */     if (flipX) {
/* 370 */       float tmp = u;
/* 371 */       u = u2;
/* 372 */       u2 = tmp;
/*     */     } 
/* 374 */     if (flipY) {
/* 375 */       float tmp = v;
/* 376 */       v = v2;
/* 377 */       v2 = tmp;
/*     */     } 
/*     */     
/* 380 */     Affine2 t = this.adjustAffine;
/*     */     
/* 382 */     this.vertices[this.idx + 0] = t.m00 * x1 + t.m01 * y1 + t.m02;
/* 383 */     this.vertices[this.idx + 1] = t.m10 * x1 + t.m11 * y1 + t.m12;
/* 384 */     this.vertices[this.idx + 2] = this.color;
/* 385 */     this.vertices[this.idx + 3] = u;
/* 386 */     this.vertices[this.idx + 4] = v;
/*     */     
/* 388 */     this.vertices[this.idx + 5] = t.m00 * x2 + t.m01 * y2 + t.m02;
/* 389 */     this.vertices[this.idx + 6] = t.m10 * x2 + t.m11 * y2 + t.m12;
/* 390 */     this.vertices[this.idx + 7] = this.color;
/* 391 */     this.vertices[this.idx + 8] = u;
/* 392 */     this.vertices[this.idx + 9] = v2;
/*     */     
/* 394 */     this.vertices[this.idx + 10] = t.m00 * x3 + t.m01 * y3 + t.m02;
/* 395 */     this.vertices[this.idx + 11] = t.m10 * x3 + t.m11 * y3 + t.m12;
/* 396 */     this.vertices[this.idx + 12] = this.color;
/* 397 */     this.vertices[this.idx + 13] = u2;
/* 398 */     this.vertices[this.idx + 14] = v2;
/*     */     
/* 400 */     this.vertices[this.idx + 15] = t.m00 * x4 + t.m01 * y4 + t.m02;
/* 401 */     this.vertices[this.idx + 16] = t.m10 * x4 + t.m11 * y4 + t.m12;
/* 402 */     this.vertices[this.idx + 17] = this.color;
/* 403 */     this.vertices[this.idx + 18] = u2;
/* 404 */     this.vertices[this.idx + 19] = v;
/*     */     
/* 406 */     this.idx += 20;
/*     */   }
/*     */   
/*     */   private void drawAdjusted(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
/*     */     float x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4;
/* 411 */     if (!this.drawing) throw new IllegalStateException("CpuSpriteBatch.begin must be called before draw.");
/*     */     
/* 413 */     if (region.texture != this.lastTexture)
/* 414 */     { switchTexture(region.texture); }
/* 415 */     else if (this.idx == this.vertices.length) { flush(); }
/*     */ 
/*     */     
/* 418 */     float worldOriginX = x + originX;
/* 419 */     float worldOriginY = y + originY;
/* 420 */     float fx = -originX;
/* 421 */     float fy = -originY;
/* 422 */     float fx2 = width - originX;
/* 423 */     float fy2 = height - originY;
/*     */ 
/*     */     
/* 426 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/* 427 */       fx *= scaleX;
/* 428 */       fy *= scaleY;
/* 429 */       fx2 *= scaleX;
/* 430 */       fy2 *= scaleY;
/*     */     } 
/*     */ 
/*     */     
/* 434 */     float p1x = fx;
/* 435 */     float p1y = fy;
/* 436 */     float p2x = fx;
/* 437 */     float p2y = fy2;
/* 438 */     float p3x = fx2;
/* 439 */     float p3y = fy2;
/* 440 */     float p4x = fx2;
/* 441 */     float p4y = fy;
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
/* 453 */     if (rotation != 0.0F) {
/* 454 */       float cos = MathUtils.cosDeg(rotation);
/* 455 */       float sin = MathUtils.sinDeg(rotation);
/*     */       
/* 457 */       x1 = cos * p1x - sin * p1y;
/* 458 */       y1 = sin * p1x + cos * p1y;
/*     */       
/* 460 */       x2 = cos * p2x - sin * p2y;
/* 461 */       y2 = sin * p2x + cos * p2y;
/*     */       
/* 463 */       x3 = cos * p3x - sin * p3y;
/* 464 */       y3 = sin * p3x + cos * p3y;
/*     */       
/* 466 */       x4 = x1 + x3 - x2;
/* 467 */       y4 = y3 - y2 - y1;
/*     */     } else {
/* 469 */       x1 = p1x;
/* 470 */       y1 = p1y;
/*     */       
/* 472 */       x2 = p2x;
/* 473 */       y2 = p2y;
/*     */       
/* 475 */       x3 = p3x;
/* 476 */       y3 = p3y;
/*     */       
/* 478 */       x4 = p4x;
/* 479 */       y4 = p4y;
/*     */     } 
/*     */     
/* 482 */     x1 += worldOriginX;
/* 483 */     y1 += worldOriginY;
/* 484 */     x2 += worldOriginX;
/* 485 */     y2 += worldOriginY;
/* 486 */     x3 += worldOriginX;
/* 487 */     y3 += worldOriginY;
/* 488 */     x4 += worldOriginX;
/* 489 */     y4 += worldOriginY;
/*     */ 
/*     */     
/* 492 */     if (clockwise) {
/* 493 */       u1 = region.u2;
/* 494 */       v1 = region.v2;
/* 495 */       u2 = region.u;
/* 496 */       v2 = region.v2;
/* 497 */       u3 = region.u;
/* 498 */       v3 = region.v;
/* 499 */       u4 = region.u2;
/* 500 */       v4 = region.v;
/*     */     } else {
/* 502 */       u1 = region.u;
/* 503 */       v1 = region.v;
/* 504 */       u2 = region.u2;
/* 505 */       v2 = region.v;
/* 506 */       u3 = region.u2;
/* 507 */       v3 = region.v2;
/* 508 */       u4 = region.u;
/* 509 */       v4 = region.v2;
/*     */     } 
/*     */     
/* 512 */     Affine2 t = this.adjustAffine;
/*     */     
/* 514 */     this.vertices[this.idx + 0] = t.m00 * x1 + t.m01 * y1 + t.m02;
/* 515 */     this.vertices[this.idx + 1] = t.m10 * x1 + t.m11 * y1 + t.m12;
/* 516 */     this.vertices[this.idx + 2] = this.color;
/* 517 */     this.vertices[this.idx + 3] = u1;
/* 518 */     this.vertices[this.idx + 4] = v1;
/*     */     
/* 520 */     this.vertices[this.idx + 5] = t.m00 * x2 + t.m01 * y2 + t.m02;
/* 521 */     this.vertices[this.idx + 6] = t.m10 * x2 + t.m11 * y2 + t.m12;
/* 522 */     this.vertices[this.idx + 7] = this.color;
/* 523 */     this.vertices[this.idx + 8] = u2;
/* 524 */     this.vertices[this.idx + 9] = v2;
/*     */     
/* 526 */     this.vertices[this.idx + 10] = t.m00 * x3 + t.m01 * y3 + t.m02;
/* 527 */     this.vertices[this.idx + 11] = t.m10 * x3 + t.m11 * y3 + t.m12;
/* 528 */     this.vertices[this.idx + 12] = this.color;
/* 529 */     this.vertices[this.idx + 13] = u3;
/* 530 */     this.vertices[this.idx + 14] = v3;
/*     */     
/* 532 */     this.vertices[this.idx + 15] = t.m00 * x4 + t.m01 * y4 + t.m02;
/* 533 */     this.vertices[this.idx + 16] = t.m10 * x4 + t.m11 * y4 + t.m12;
/* 534 */     this.vertices[this.idx + 17] = this.color;
/* 535 */     this.vertices[this.idx + 18] = u4;
/* 536 */     this.vertices[this.idx + 19] = v4;
/*     */     
/* 538 */     this.idx += 20;
/*     */   }
/*     */   
/*     */   private void drawAdjusted(TextureRegion region, float width, float height, Affine2 transform) {
/* 542 */     if (!this.drawing) throw new IllegalStateException("CpuSpriteBatch.begin must be called before draw.");
/*     */     
/* 544 */     if (region.texture != this.lastTexture)
/* 545 */     { switchTexture(region.texture); }
/* 546 */     else if (this.idx == this.vertices.length) { flush(); }
/*     */     
/* 548 */     Affine2 t = transform;
/*     */ 
/*     */     
/* 551 */     float x1 = t.m02;
/* 552 */     float y1 = t.m12;
/* 553 */     float x2 = t.m01 * height + t.m02;
/* 554 */     float y2 = t.m11 * height + t.m12;
/* 555 */     float x3 = t.m00 * width + t.m01 * height + t.m02;
/* 556 */     float y3 = t.m10 * width + t.m11 * height + t.m12;
/* 557 */     float x4 = t.m00 * width + t.m02;
/* 558 */     float y4 = t.m10 * width + t.m12;
/*     */ 
/*     */     
/* 561 */     float u = region.u;
/* 562 */     float v = region.v2;
/* 563 */     float u2 = region.u2;
/* 564 */     float v2 = region.v;
/*     */     
/* 566 */     t = this.adjustAffine;
/*     */     
/* 568 */     this.vertices[this.idx + 0] = t.m00 * x1 + t.m01 * y1 + t.m02;
/* 569 */     this.vertices[this.idx + 1] = t.m10 * x1 + t.m11 * y1 + t.m12;
/* 570 */     this.vertices[this.idx + 2] = this.color;
/* 571 */     this.vertices[this.idx + 3] = u;
/* 572 */     this.vertices[this.idx + 4] = v;
/*     */     
/* 574 */     this.vertices[this.idx + 5] = t.m00 * x2 + t.m01 * y2 + t.m02;
/* 575 */     this.vertices[this.idx + 6] = t.m10 * x2 + t.m11 * y2 + t.m12;
/* 576 */     this.vertices[this.idx + 7] = this.color;
/* 577 */     this.vertices[this.idx + 8] = u;
/* 578 */     this.vertices[this.idx + 9] = v2;
/*     */     
/* 580 */     this.vertices[this.idx + 10] = t.m00 * x3 + t.m01 * y3 + t.m02;
/* 581 */     this.vertices[this.idx + 11] = t.m10 * x3 + t.m11 * y3 + t.m12;
/* 582 */     this.vertices[this.idx + 12] = this.color;
/* 583 */     this.vertices[this.idx + 13] = u2;
/* 584 */     this.vertices[this.idx + 14] = v2;
/*     */     
/* 586 */     this.vertices[this.idx + 15] = t.m00 * x4 + t.m01 * y4 + t.m02;
/* 587 */     this.vertices[this.idx + 16] = t.m10 * x4 + t.m11 * y4 + t.m12;
/* 588 */     this.vertices[this.idx + 17] = this.color;
/* 589 */     this.vertices[this.idx + 18] = u2;
/* 590 */     this.vertices[this.idx + 19] = v;
/*     */     
/* 592 */     this.idx += 20;
/*     */   }
/*     */   
/*     */   private void drawAdjusted(Texture texture, float[] spriteVertices, int offset, int count) {
/* 596 */     if (!this.drawing) throw new IllegalStateException("CpuSpriteBatch.begin must be called before draw.");
/*     */     
/* 598 */     if (texture != this.lastTexture) switchTexture(texture);
/*     */     
/* 600 */     Affine2 t = this.adjustAffine;
/*     */     
/* 602 */     int copyCount = Math.min(this.vertices.length - this.idx, count);
/*     */     do {
/* 604 */       count -= copyCount;
/* 605 */       while (copyCount > 0) {
/* 606 */         float x = spriteVertices[offset];
/* 607 */         float y = spriteVertices[offset + 1];
/*     */         
/* 609 */         this.vertices[this.idx] = t.m00 * x + t.m01 * y + t.m02;
/* 610 */         this.vertices[this.idx + 1] = t.m10 * x + t.m11 * y + t.m12;
/* 611 */         this.vertices[this.idx + 2] = spriteVertices[offset + 2];
/* 612 */         this.vertices[this.idx + 3] = spriteVertices[offset + 3];
/* 613 */         this.vertices[this.idx + 4] = spriteVertices[offset + 4];
/*     */         
/* 615 */         this.idx += 5;
/* 616 */         offset += 5;
/* 617 */         copyCount -= 5;
/*     */       } 
/*     */       
/* 620 */       if (count <= 0)
/* 621 */         continue;  flush();
/* 622 */       copyCount = Math.min(this.vertices.length, count);
/*     */     }
/* 624 */     while (count > 0);
/*     */   }
/*     */   
/*     */   private static boolean checkEqual(Matrix4 a, Matrix4 b) {
/* 628 */     if (a == b) return true;
/*     */ 
/*     */     
/* 631 */     return (a.val[0] == b.val[0] && a.val[1] == b.val[1] && a.val[4] == b.val[4] && a.val[5] == b.val[5] && a.val[12] == b.val[12] && a.val[13] == b.val[13]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean checkEqual(Matrix4 matrix, Affine2 affine) {
/* 637 */     float[] val = matrix.getValues();
/*     */ 
/*     */     
/* 640 */     return (val[0] == affine.m00 && val[1] == affine.m10 && val[4] == affine.m01 && val[5] == affine.m11 && val[12] == affine.m02 && val[13] == affine.m12);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean checkIdt(Matrix4 matrix) {
/* 645 */     float[] val = matrix.getValues();
/*     */ 
/*     */     
/* 648 */     return (val[0] == 1.0F && val[1] == 0.0F && val[4] == 0.0F && val[5] == 1.0F && val[12] == 0.0F && val[13] == 0.0F);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\CpuSpriteBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */