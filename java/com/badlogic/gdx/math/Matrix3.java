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
/*     */ public class Matrix3
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7907569533774959788L;
/*     */   public static final int M00 = 0;
/*     */   public static final int M01 = 3;
/*     */   public static final int M02 = 6;
/*     */   public static final int M10 = 1;
/*     */   public static final int M11 = 4;
/*     */   public static final int M12 = 7;
/*     */   public static final int M20 = 2;
/*     */   public static final int M21 = 5;
/*     */   public static final int M22 = 8;
/*  38 */   public float[] val = new float[9];
/*  39 */   private float[] tmp = new float[9];
/*     */   
/*     */   public Matrix3() {
/*  42 */     idt();
/*     */   }
/*     */   
/*     */   public Matrix3(Matrix3 matrix) {
/*  46 */     set(matrix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3(float[] values) {
/*  54 */     set(values);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 idt() {
/*  60 */     float[] val = this.val;
/*  61 */     val[0] = 1.0F;
/*  62 */     val[1] = 0.0F;
/*  63 */     val[2] = 0.0F;
/*  64 */     val[3] = 0.0F;
/*  65 */     val[4] = 1.0F;
/*  66 */     val[5] = 0.0F;
/*  67 */     val[6] = 0.0F;
/*  68 */     val[7] = 0.0F;
/*  69 */     val[8] = 1.0F;
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 mul(Matrix3 m) {
/*  81 */     float[] val = this.val;
/*     */     
/*  83 */     float v00 = val[0] * m.val[0] + val[3] * m.val[1] + val[6] * m.val[2];
/*  84 */     float v01 = val[0] * m.val[3] + val[3] * m.val[4] + val[6] * m.val[5];
/*  85 */     float v02 = val[0] * m.val[6] + val[3] * m.val[7] + val[6] * m.val[8];
/*     */     
/*  87 */     float v10 = val[1] * m.val[0] + val[4] * m.val[1] + val[7] * m.val[2];
/*  88 */     float v11 = val[1] * m.val[3] + val[4] * m.val[4] + val[7] * m.val[5];
/*  89 */     float v12 = val[1] * m.val[6] + val[4] * m.val[7] + val[7] * m.val[8];
/*     */     
/*  91 */     float v20 = val[2] * m.val[0] + val[5] * m.val[1] + val[8] * m.val[2];
/*  92 */     float v21 = val[2] * m.val[3] + val[5] * m.val[4] + val[8] * m.val[5];
/*  93 */     float v22 = val[2] * m.val[6] + val[5] * m.val[7] + val[8] * m.val[8];
/*     */     
/*  95 */     val[0] = v00;
/*  96 */     val[1] = v10;
/*  97 */     val[2] = v20;
/*  98 */     val[3] = v01;
/*  99 */     val[4] = v11;
/* 100 */     val[5] = v21;
/* 101 */     val[6] = v02;
/* 102 */     val[7] = v12;
/* 103 */     val[8] = v22;
/*     */     
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 mulLeft(Matrix3 m) {
/* 116 */     float[] val = this.val;
/*     */     
/* 118 */     float v00 = m.val[0] * val[0] + m.val[3] * val[1] + m.val[6] * val[2];
/* 119 */     float v01 = m.val[0] * val[3] + m.val[3] * val[4] + m.val[6] * val[5];
/* 120 */     float v02 = m.val[0] * val[6] + m.val[3] * val[7] + m.val[6] * val[8];
/*     */     
/* 122 */     float v10 = m.val[1] * val[0] + m.val[4] * val[1] + m.val[7] * val[2];
/* 123 */     float v11 = m.val[1] * val[3] + m.val[4] * val[4] + m.val[7] * val[5];
/* 124 */     float v12 = m.val[1] * val[6] + m.val[4] * val[7] + m.val[7] * val[8];
/*     */     
/* 126 */     float v20 = m.val[2] * val[0] + m.val[5] * val[1] + m.val[8] * val[2];
/* 127 */     float v21 = m.val[2] * val[3] + m.val[5] * val[4] + m.val[8] * val[5];
/* 128 */     float v22 = m.val[2] * val[6] + m.val[5] * val[7] + m.val[8] * val[8];
/*     */     
/* 130 */     val[0] = v00;
/* 131 */     val[1] = v10;
/* 132 */     val[2] = v20;
/* 133 */     val[3] = v01;
/* 134 */     val[4] = v11;
/* 135 */     val[5] = v21;
/* 136 */     val[6] = v02;
/* 137 */     val[7] = v12;
/* 138 */     val[8] = v22;
/*     */     
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 setToRotation(float degrees) {
/* 147 */     return setToRotationRad(0.017453292F * degrees);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 setToRotationRad(float radians) {
/* 154 */     float cos = (float)Math.cos(radians);
/* 155 */     float sin = (float)Math.sin(radians);
/* 156 */     float[] val = this.val;
/*     */     
/* 158 */     val[0] = cos;
/* 159 */     val[1] = sin;
/* 160 */     val[2] = 0.0F;
/*     */     
/* 162 */     val[3] = -sin;
/* 163 */     val[4] = cos;
/* 164 */     val[5] = 0.0F;
/*     */     
/* 166 */     val[6] = 0.0F;
/* 167 */     val[7] = 0.0F;
/* 168 */     val[8] = 1.0F;
/*     */     
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Matrix3 setToRotation(Vector3 axis, float degrees) {
/* 174 */     return setToRotation(axis, MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
/*     */   }
/*     */   
/*     */   public Matrix3 setToRotation(Vector3 axis, float cos, float sin) {
/* 178 */     float[] val = this.val;
/* 179 */     float oc = 1.0F - cos;
/* 180 */     val[0] = oc * axis.x * axis.x + cos;
/* 181 */     val[1] = oc * axis.x * axis.y - axis.z * sin;
/* 182 */     val[2] = oc * axis.z * axis.x + axis.y * sin;
/* 183 */     val[3] = oc * axis.x * axis.y + axis.z * sin;
/* 184 */     val[4] = oc * axis.y * axis.y + cos;
/* 185 */     val[5] = oc * axis.y * axis.z - axis.x * sin;
/* 186 */     val[6] = oc * axis.z * axis.x - axis.y * sin;
/* 187 */     val[7] = oc * axis.y * axis.z + axis.x * sin;
/* 188 */     val[8] = oc * axis.z * axis.z + cos;
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 setToTranslation(float x, float y) {
/* 197 */     float[] val = this.val;
/*     */     
/* 199 */     val[0] = 1.0F;
/* 200 */     val[1] = 0.0F;
/* 201 */     val[2] = 0.0F;
/*     */     
/* 203 */     val[3] = 0.0F;
/* 204 */     val[4] = 1.0F;
/* 205 */     val[5] = 0.0F;
/*     */     
/* 207 */     val[6] = x;
/* 208 */     val[7] = y;
/* 209 */     val[8] = 1.0F;
/*     */     
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 setToTranslation(Vector2 translation) {
/* 218 */     float[] val = this.val;
/*     */     
/* 220 */     val[0] = 1.0F;
/* 221 */     val[1] = 0.0F;
/* 222 */     val[2] = 0.0F;
/*     */     
/* 224 */     val[3] = 0.0F;
/* 225 */     val[4] = 1.0F;
/* 226 */     val[5] = 0.0F;
/*     */     
/* 228 */     val[6] = translation.x;
/* 229 */     val[7] = translation.y;
/* 230 */     val[8] = 1.0F;
/*     */     
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 setToScaling(float scaleX, float scaleY) {
/* 241 */     float[] val = this.val;
/* 242 */     val[0] = scaleX;
/* 243 */     val[1] = 0.0F;
/* 244 */     val[2] = 0.0F;
/* 245 */     val[3] = 0.0F;
/* 246 */     val[4] = scaleY;
/* 247 */     val[5] = 0.0F;
/* 248 */     val[6] = 0.0F;
/* 249 */     val[7] = 0.0F;
/* 250 */     val[8] = 1.0F;
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 setToScaling(Vector2 scale) {
/* 258 */     float[] val = this.val;
/* 259 */     val[0] = scale.x;
/* 260 */     val[1] = 0.0F;
/* 261 */     val[2] = 0.0F;
/* 262 */     val[3] = 0.0F;
/* 263 */     val[4] = scale.y;
/* 264 */     val[5] = 0.0F;
/* 265 */     val[6] = 0.0F;
/* 266 */     val[7] = 0.0F;
/* 267 */     val[8] = 1.0F;
/* 268 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 272 */     float[] val = this.val;
/* 273 */     return "[" + val[0] + "|" + val[3] + "|" + val[6] + "]\n[" + val[1] + "|" + val[4] + "|" + val[7] + "]\n[" + val[2] + "|" + val[5] + "|" + val[8] + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float det() {
/* 280 */     float[] val = this.val;
/* 281 */     return val[0] * val[4] * val[8] + val[3] * val[7] * val[2] + val[6] * val[1] * val[5] - val[0] * val[7] * val[5] - val[3] * val[1] * val[8] - val[6] * val[4] * val[2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 inv() {
/* 289 */     float det = det();
/* 290 */     if (det == 0.0F) throw new GdxRuntimeException("Can't invert a singular matrix");
/*     */     
/* 292 */     float inv_det = 1.0F / det;
/* 293 */     float[] tmp = this.tmp, val = this.val;
/*     */     
/* 295 */     tmp[0] = val[4] * val[8] - val[5] * val[7];
/* 296 */     tmp[1] = val[2] * val[7] - val[1] * val[8];
/* 297 */     tmp[2] = val[1] * val[5] - val[2] * val[4];
/* 298 */     tmp[3] = val[5] * val[6] - val[3] * val[8];
/* 299 */     tmp[4] = val[0] * val[8] - val[2] * val[6];
/* 300 */     tmp[5] = val[2] * val[3] - val[0] * val[5];
/* 301 */     tmp[6] = val[3] * val[7] - val[4] * val[6];
/* 302 */     tmp[7] = val[1] * val[6] - val[0] * val[7];
/* 303 */     tmp[8] = val[0] * val[4] - val[1] * val[3];
/*     */     
/* 305 */     val[0] = inv_det * tmp[0];
/* 306 */     val[1] = inv_det * tmp[1];
/* 307 */     val[2] = inv_det * tmp[2];
/* 308 */     val[3] = inv_det * tmp[3];
/* 309 */     val[4] = inv_det * tmp[4];
/* 310 */     val[5] = inv_det * tmp[5];
/* 311 */     val[6] = inv_det * tmp[6];
/* 312 */     val[7] = inv_det * tmp[7];
/* 313 */     val[8] = inv_det * tmp[8];
/*     */     
/* 315 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 set(Matrix3 mat) {
/* 322 */     System.arraycopy(mat.val, 0, this.val, 0, this.val.length);
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 set(Affine2 affine) {
/* 330 */     float[] val = this.val;
/*     */     
/* 332 */     val[0] = affine.m00;
/* 333 */     val[1] = affine.m10;
/* 334 */     val[2] = 0.0F;
/* 335 */     val[3] = affine.m01;
/* 336 */     val[4] = affine.m11;
/* 337 */     val[5] = 0.0F;
/* 338 */     val[6] = affine.m02;
/* 339 */     val[7] = affine.m12;
/* 340 */     val[8] = 1.0F;
/*     */     
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 set(Matrix4 mat) {
/* 349 */     float[] val = this.val;
/* 350 */     val[0] = mat.val[0];
/* 351 */     val[1] = mat.val[1];
/* 352 */     val[2] = mat.val[2];
/* 353 */     val[3] = mat.val[4];
/* 354 */     val[4] = mat.val[5];
/* 355 */     val[5] = mat.val[6];
/* 356 */     val[6] = mat.val[8];
/* 357 */     val[7] = mat.val[9];
/* 358 */     val[8] = mat.val[10];
/* 359 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 set(float[] values) {
/* 369 */     System.arraycopy(values, 0, this.val, 0, this.val.length);
/* 370 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 trn(Vector2 vector) {
/* 377 */     this.val[6] = this.val[6] + vector.x;
/* 378 */     this.val[7] = this.val[7] + vector.y;
/* 379 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 trn(float x, float y) {
/* 387 */     this.val[6] = this.val[6] + x;
/* 388 */     this.val[7] = this.val[7] + y;
/* 389 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 trn(Vector3 vector) {
/* 396 */     this.val[6] = this.val[6] + vector.x;
/* 397 */     this.val[7] = this.val[7] + vector.y;
/* 398 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 translate(float x, float y) {
/* 407 */     float[] val = this.val;
/* 408 */     this.tmp[0] = 1.0F;
/* 409 */     this.tmp[1] = 0.0F;
/* 410 */     this.tmp[2] = 0.0F;
/*     */     
/* 412 */     this.tmp[3] = 0.0F;
/* 413 */     this.tmp[4] = 1.0F;
/* 414 */     this.tmp[5] = 0.0F;
/*     */     
/* 416 */     this.tmp[6] = x;
/* 417 */     this.tmp[7] = y;
/* 418 */     this.tmp[8] = 1.0F;
/* 419 */     mul(val, this.tmp);
/* 420 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 translate(Vector2 translation) {
/* 428 */     float[] val = this.val;
/* 429 */     this.tmp[0] = 1.0F;
/* 430 */     this.tmp[1] = 0.0F;
/* 431 */     this.tmp[2] = 0.0F;
/*     */     
/* 433 */     this.tmp[3] = 0.0F;
/* 434 */     this.tmp[4] = 1.0F;
/* 435 */     this.tmp[5] = 0.0F;
/*     */     
/* 437 */     this.tmp[6] = translation.x;
/* 438 */     this.tmp[7] = translation.y;
/* 439 */     this.tmp[8] = 1.0F;
/* 440 */     mul(val, this.tmp);
/* 441 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 rotate(float degrees) {
/* 449 */     return rotateRad(0.017453292F * degrees);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 rotateRad(float radians) {
/* 457 */     if (radians == 0.0F) return this; 
/* 458 */     float cos = (float)Math.cos(radians);
/* 459 */     float sin = (float)Math.sin(radians);
/* 460 */     float[] tmp = this.tmp;
/*     */     
/* 462 */     tmp[0] = cos;
/* 463 */     tmp[1] = sin;
/* 464 */     tmp[2] = 0.0F;
/*     */     
/* 466 */     tmp[3] = -sin;
/* 467 */     tmp[4] = cos;
/* 468 */     tmp[5] = 0.0F;
/*     */     
/* 470 */     tmp[6] = 0.0F;
/* 471 */     tmp[7] = 0.0F;
/* 472 */     tmp[8] = 1.0F;
/* 473 */     mul(this.val, tmp);
/* 474 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 scale(float scaleX, float scaleY) {
/* 483 */     float[] tmp = this.tmp;
/* 484 */     tmp[0] = scaleX;
/* 485 */     tmp[1] = 0.0F;
/* 486 */     tmp[2] = 0.0F;
/* 487 */     tmp[3] = 0.0F;
/* 488 */     tmp[4] = scaleY;
/* 489 */     tmp[5] = 0.0F;
/* 490 */     tmp[6] = 0.0F;
/* 491 */     tmp[7] = 0.0F;
/* 492 */     tmp[8] = 1.0F;
/* 493 */     mul(this.val, tmp);
/* 494 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 scale(Vector2 scale) {
/* 502 */     float[] tmp = this.tmp;
/* 503 */     tmp[0] = scale.x;
/* 504 */     tmp[1] = 0.0F;
/* 505 */     tmp[2] = 0.0F;
/* 506 */     tmp[3] = 0.0F;
/* 507 */     tmp[4] = scale.y;
/* 508 */     tmp[5] = 0.0F;
/* 509 */     tmp[6] = 0.0F;
/* 510 */     tmp[7] = 0.0F;
/* 511 */     tmp[8] = 1.0F;
/* 512 */     mul(this.val, tmp);
/* 513 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getValues() {
/* 519 */     return this.val;
/*     */   }
/*     */   
/*     */   public Vector2 getTranslation(Vector2 position) {
/* 523 */     position.x = this.val[6];
/* 524 */     position.y = this.val[7];
/* 525 */     return position;
/*     */   }
/*     */   
/*     */   public Vector2 getScale(Vector2 scale) {
/* 529 */     float[] val = this.val;
/* 530 */     scale.x = (float)Math.sqrt((val[0] * val[0] + val[3] * val[3]));
/* 531 */     scale.y = (float)Math.sqrt((val[1] * val[1] + val[4] * val[4]));
/* 532 */     return scale;
/*     */   }
/*     */   
/*     */   public float getRotation() {
/* 536 */     return 57.295776F * (float)Math.atan2(this.val[1], this.val[0]);
/*     */   }
/*     */   
/*     */   public float getRotationRad() {
/* 540 */     return (float)Math.atan2(this.val[1], this.val[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 scl(float scale) {
/* 547 */     this.val[0] = this.val[0] * scale;
/* 548 */     this.val[4] = this.val[4] * scale;
/* 549 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 scl(Vector2 scale) {
/* 556 */     this.val[0] = this.val[0] * scale.x;
/* 557 */     this.val[4] = this.val[4] * scale.y;
/* 558 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 scl(Vector3 scale) {
/* 565 */     this.val[0] = this.val[0] * scale.x;
/* 566 */     this.val[4] = this.val[4] * scale.y;
/* 567 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix3 transpose() {
/* 574 */     float[] val = this.val;
/* 575 */     float v01 = val[1];
/* 576 */     float v02 = val[2];
/* 577 */     float v10 = val[3];
/* 578 */     float v12 = val[5];
/* 579 */     float v20 = val[6];
/* 580 */     float v21 = val[7];
/* 581 */     val[3] = v01;
/* 582 */     val[6] = v02;
/* 583 */     val[1] = v10;
/* 584 */     val[7] = v12;
/* 585 */     val[2] = v20;
/* 586 */     val[5] = v21;
/* 587 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void mul(float[] mata, float[] matb) {
/* 598 */     float v00 = mata[0] * matb[0] + mata[3] * matb[1] + mata[6] * matb[2];
/* 599 */     float v01 = mata[0] * matb[3] + mata[3] * matb[4] + mata[6] * matb[5];
/* 600 */     float v02 = mata[0] * matb[6] + mata[3] * matb[7] + mata[6] * matb[8];
/*     */     
/* 602 */     float v10 = mata[1] * matb[0] + mata[4] * matb[1] + mata[7] * matb[2];
/* 603 */     float v11 = mata[1] * matb[3] + mata[4] * matb[4] + mata[7] * matb[5];
/* 604 */     float v12 = mata[1] * matb[6] + mata[4] * matb[7] + mata[7] * matb[8];
/*     */     
/* 606 */     float v20 = mata[2] * matb[0] + mata[5] * matb[1] + mata[8] * matb[2];
/* 607 */     float v21 = mata[2] * matb[3] + mata[5] * matb[4] + mata[8] * matb[5];
/* 608 */     float v22 = mata[2] * matb[6] + mata[5] * matb[7] + mata[8] * matb[8];
/*     */     
/* 610 */     mata[0] = v00;
/* 611 */     mata[1] = v10;
/* 612 */     mata[2] = v20;
/* 613 */     mata[3] = v01;
/* 614 */     mata[4] = v11;
/* 615 */     mata[5] = v21;
/* 616 */     mata[6] = v02;
/* 617 */     mata[7] = v12;
/* 618 */     mata[8] = v22;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Matrix3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */