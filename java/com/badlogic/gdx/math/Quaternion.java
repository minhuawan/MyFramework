/*     */ package com.badlogic.gdx.math;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Quaternion
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7661875440774897168L;
/*  30 */   private static Quaternion tmp1 = new Quaternion(0.0F, 0.0F, 0.0F, 0.0F);
/*  31 */   private static Quaternion tmp2 = new Quaternion(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*     */   
/*     */   public float x;
/*     */   
/*     */   public float y;
/*     */   
/*     */   public float z;
/*     */   
/*     */   public float w;
/*     */ 
/*     */   
/*     */   public Quaternion(float x, float y, float z, float w) {
/*  44 */     set(x, y, z, w);
/*     */   }
/*     */   
/*     */   public Quaternion() {
/*  48 */     idt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion(Quaternion quaternion) {
/*  55 */     set(quaternion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion(Vector3 axis, float angle) {
/*  63 */     set(axis, angle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion set(float x, float y, float z, float w) {
/*  73 */     this.x = x;
/*  74 */     this.y = y;
/*  75 */     this.z = z;
/*  76 */     this.w = w;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion set(Quaternion quaternion) {
/*  84 */     return set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion set(Vector3 axis, float angle) {
/*  93 */     return setFromAxis(axis.x, axis.y, axis.z, angle);
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion cpy() {
/*  98 */     return new Quaternion(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float len(float x, float y, float z, float w) {
/* 103 */     return (float)Math.sqrt((x * x + y * y + z * z + w * w));
/*     */   }
/*     */ 
/*     */   
/*     */   public float len() {
/* 108 */     return (float)Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return "[" + this.x + "|" + this.y + "|" + this.z + "|" + this.w + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setEulerAngles(float yaw, float pitch, float roll) {
/* 122 */     return setEulerAnglesRad(yaw * 0.017453292F, pitch * 0.017453292F, roll * 0.017453292F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setEulerAnglesRad(float yaw, float pitch, float roll) {
/* 132 */     float hr = roll * 0.5F;
/* 133 */     float shr = (float)Math.sin(hr);
/* 134 */     float chr = (float)Math.cos(hr);
/* 135 */     float hp = pitch * 0.5F;
/* 136 */     float shp = (float)Math.sin(hp);
/* 137 */     float chp = (float)Math.cos(hp);
/* 138 */     float hy = yaw * 0.5F;
/* 139 */     float shy = (float)Math.sin(hy);
/* 140 */     float chy = (float)Math.cos(hy);
/* 141 */     float chy_shp = chy * shp;
/* 142 */     float shy_chp = shy * chp;
/* 143 */     float chy_chp = chy * chp;
/* 144 */     float shy_shp = shy * shp;
/*     */     
/* 146 */     this.x = chy_shp * chr + shy_chp * shr;
/* 147 */     this.y = shy_chp * chr - chy_shp * shr;
/* 148 */     this.z = chy_chp * shr - shy_shp * chr;
/* 149 */     this.w = chy_chp * chr + shy_shp * shr;
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGimbalPole() {
/* 156 */     float t = this.y * this.x + this.z * this.w;
/* 157 */     return (t > 0.499F) ? 1 : ((t < -0.499F) ? -1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRollRad() {
/* 163 */     int pole = getGimbalPole();
/* 164 */     return (pole == 0) ? MathUtils.atan2(2.0F * (this.w * this.z + this.y * this.x), 1.0F - 2.0F * (this.x * this.x + this.z * this.z)) : (pole * 2.0F * 
/* 165 */       MathUtils.atan2(this.y, this.w));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRoll() {
/* 171 */     return getRollRad() * 57.295776F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitchRad() {
/* 177 */     int pole = getGimbalPole();
/* 178 */     return (pole == 0) ? (float)Math.asin(MathUtils.clamp(2.0F * (this.w * this.x - this.z * this.y), -1.0F, 1.0F)) : (pole * 3.1415927F * 0.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 184 */     return getPitchRad() * 57.295776F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYawRad() {
/* 190 */     return (getGimbalPole() == 0) ? MathUtils.atan2(2.0F * (this.y * this.w + this.x * this.z), 1.0F - 2.0F * (this.y * this.y + this.x * this.x)) : 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/* 196 */     return getYawRad() * 57.295776F;
/*     */   }
/*     */   
/*     */   public static final float len2(float x, float y, float z, float w) {
/* 200 */     return x * x + y * y + z * z + w * w;
/*     */   }
/*     */ 
/*     */   
/*     */   public float len2() {
/* 205 */     return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion nor() {
/* 211 */     float len = len2();
/* 212 */     if (len != 0.0F && !MathUtils.isEqual(len, 1.0F)) {
/* 213 */       len = (float)Math.sqrt(len);
/* 214 */       this.w /= len;
/* 215 */       this.x /= len;
/* 216 */       this.y /= len;
/* 217 */       this.z /= len;
/*     */     } 
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion conjugate() {
/* 226 */     this.x = -this.x;
/* 227 */     this.y = -this.y;
/* 228 */     this.z = -this.z;
/* 229 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 transform(Vector3 v) {
/* 237 */     tmp2.set(this);
/* 238 */     tmp2.conjugate();
/* 239 */     tmp2.mulLeft(tmp1.set(v.x, v.y, v.z, 0.0F)).mulLeft(this);
/*     */     
/* 241 */     v.x = tmp2.x;
/* 242 */     v.y = tmp2.y;
/* 243 */     v.z = tmp2.z;
/* 244 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mul(Quaternion other) {
/* 252 */     float newX = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
/* 253 */     float newY = this.w * other.y + this.y * other.w + this.z * other.x - this.x * other.z;
/* 254 */     float newZ = this.w * other.z + this.z * other.w + this.x * other.y - this.y * other.x;
/* 255 */     float newW = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
/* 256 */     this.x = newX;
/* 257 */     this.y = newY;
/* 258 */     this.z = newZ;
/* 259 */     this.w = newW;
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mul(float x, float y, float z, float w) {
/* 271 */     float newX = this.w * x + this.x * w + this.y * z - this.z * y;
/* 272 */     float newY = this.w * y + this.y * w + this.z * x - this.x * z;
/* 273 */     float newZ = this.w * z + this.z * w + this.x * y - this.y * x;
/* 274 */     float newW = this.w * w - this.x * x - this.y * y - this.z * z;
/* 275 */     this.x = newX;
/* 276 */     this.y = newY;
/* 277 */     this.z = newZ;
/* 278 */     this.w = newW;
/* 279 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mulLeft(Quaternion other) {
/* 287 */     float newX = other.w * this.x + other.x * this.w + other.y * this.z - other.z * this.y;
/* 288 */     float newY = other.w * this.y + other.y * this.w + other.z * this.x - other.x * this.z;
/* 289 */     float newZ = other.w * this.z + other.z * this.w + other.x * this.y - other.y * this.x;
/* 290 */     float newW = other.w * this.w - other.x * this.x - other.y * this.y - other.z * this.z;
/* 291 */     this.x = newX;
/* 292 */     this.y = newY;
/* 293 */     this.z = newZ;
/* 294 */     this.w = newW;
/* 295 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mulLeft(float x, float y, float z, float w) {
/* 306 */     float newX = w * this.x + x * this.w + y * this.z - z * this.y;
/* 307 */     float newY = w * this.y + y * this.w + z * this.x - x * this.z;
/* 308 */     float newZ = w * this.z + z * this.w + x * this.y - y * this.x;
/* 309 */     float newW = w * this.w - x * this.x - y * this.y - z * this.z;
/* 310 */     this.x = newX;
/* 311 */     this.y = newY;
/* 312 */     this.z = newZ;
/* 313 */     this.w = newW;
/* 314 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion add(Quaternion quaternion) {
/* 319 */     this.x += quaternion.x;
/* 320 */     this.y += quaternion.y;
/* 321 */     this.z += quaternion.z;
/* 322 */     this.w += quaternion.w;
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion add(float qx, float qy, float qz, float qw) {
/* 328 */     this.x += qx;
/* 329 */     this.y += qy;
/* 330 */     this.z += qz;
/* 331 */     this.w += qw;
/* 332 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toMatrix(float[] matrix) {
/* 341 */     float xx = this.x * this.x;
/* 342 */     float xy = this.x * this.y;
/* 343 */     float xz = this.x * this.z;
/* 344 */     float xw = this.x * this.w;
/* 345 */     float yy = this.y * this.y;
/* 346 */     float yz = this.y * this.z;
/* 347 */     float yw = this.y * this.w;
/* 348 */     float zz = this.z * this.z;
/* 349 */     float zw = this.z * this.w;
/*     */     
/* 351 */     matrix[0] = 1.0F - 2.0F * (yy + zz);
/* 352 */     matrix[4] = 2.0F * (xy - zw);
/* 353 */     matrix[8] = 2.0F * (xz + yw);
/* 354 */     matrix[12] = 0.0F;
/* 355 */     matrix[1] = 2.0F * (xy + zw);
/* 356 */     matrix[5] = 1.0F - 2.0F * (xx + zz);
/* 357 */     matrix[9] = 2.0F * (yz - xw);
/* 358 */     matrix[13] = 0.0F;
/* 359 */     matrix[2] = 2.0F * (xz - yw);
/* 360 */     matrix[6] = 2.0F * (yz + xw);
/* 361 */     matrix[10] = 1.0F - 2.0F * (xx + yy);
/* 362 */     matrix[14] = 0.0F;
/* 363 */     matrix[3] = 0.0F;
/* 364 */     matrix[7] = 0.0F;
/* 365 */     matrix[11] = 0.0F;
/* 366 */     matrix[15] = 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion idt() {
/* 372 */     return set(0.0F, 0.0F, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIdentity() {
/* 377 */     return (MathUtils.isZero(this.x) && MathUtils.isZero(this.y) && MathUtils.isZero(this.z) && MathUtils.isEqual(this.w, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIdentity(float tolerance) {
/* 382 */     return (MathUtils.isZero(this.x, tolerance) && MathUtils.isZero(this.y, tolerance) && MathUtils.isZero(this.z, tolerance) && 
/* 383 */       MathUtils.isEqual(this.w, 1.0F, tolerance));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxis(Vector3 axis, float degrees) {
/* 393 */     return setFromAxis(axis.x, axis.y, axis.z, degrees);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxisRad(Vector3 axis, float radians) {
/* 402 */     return setFromAxisRad(axis.x, axis.y, axis.z, radians);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxis(float x, float y, float z, float degrees) {
/* 412 */     return setFromAxisRad(x, y, z, degrees * 0.017453292F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxisRad(float x, float y, float z, float radians) {
/* 422 */     float d = Vector3.len(x, y, z);
/* 423 */     if (d == 0.0F) return idt(); 
/* 424 */     d = 1.0F / d;
/* 425 */     float l_ang = (radians < 0.0F) ? (6.2831855F - -radians % 6.2831855F) : (radians % 6.2831855F);
/* 426 */     float l_sin = (float)Math.sin((l_ang / 2.0F));
/* 427 */     float l_cos = (float)Math.cos((l_ang / 2.0F));
/* 428 */     return set(d * x * l_sin, d * y * l_sin, d * z * l_sin, l_cos).nor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(boolean normalizeAxes, Matrix4 matrix) {
/* 433 */     return setFromAxes(normalizeAxes, matrix.val[0], matrix.val[4], matrix.val[8], matrix.val[1], matrix.val[5], matrix.val[9], matrix.val[2], matrix.val[6], matrix.val[10]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(Matrix4 matrix) {
/* 440 */     return setFromMatrix(false, matrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(boolean normalizeAxes, Matrix3 matrix) {
/* 445 */     return setFromAxes(normalizeAxes, matrix.val[0], matrix.val[3], matrix.val[6], matrix.val[1], matrix.val[4], matrix.val[7], matrix.val[2], matrix.val[5], matrix.val[8]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromMatrix(Matrix3 matrix) {
/* 452 */     return setFromMatrix(false, matrix);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromAxes(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
/* 474 */     return setFromAxes(false, xx, xy, xz, yx, yy, yz, zx, zy, zz);
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
/*     */   public Quaternion setFromAxes(boolean normalizeAxes, float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
/* 498 */     if (normalizeAxes) {
/* 499 */       float lx = 1.0F / Vector3.len(xx, xy, xz);
/* 500 */       float ly = 1.0F / Vector3.len(yx, yy, yz);
/* 501 */       float lz = 1.0F / Vector3.len(zx, zy, zz);
/* 502 */       xx *= lx;
/* 503 */       xy *= lx;
/* 504 */       xz *= lx;
/* 505 */       yx *= ly;
/* 506 */       yy *= ly;
/* 507 */       yz *= ly;
/* 508 */       zx *= lz;
/* 509 */       zy *= lz;
/* 510 */       zz *= lz;
/*     */     } 
/*     */ 
/*     */     
/* 514 */     float t = xx + yy + zz;
/*     */ 
/*     */     
/* 517 */     if (t >= 0.0F) {
/* 518 */       float s = (float)Math.sqrt((t + 1.0F));
/* 519 */       this.w = 0.5F * s;
/* 520 */       s = 0.5F / s;
/* 521 */       this.x = (zy - yz) * s;
/* 522 */       this.y = (xz - zx) * s;
/* 523 */       this.z = (yx - xy) * s;
/* 524 */     } else if (xx > yy && xx > zz) {
/* 525 */       float s = (float)Math.sqrt(1.0D + xx - yy - zz);
/* 526 */       this.x = s * 0.5F;
/* 527 */       s = 0.5F / s;
/* 528 */       this.y = (yx + xy) * s;
/* 529 */       this.z = (xz + zx) * s;
/* 530 */       this.w = (zy - yz) * s;
/* 531 */     } else if (yy > zz) {
/* 532 */       float s = (float)Math.sqrt(1.0D + yy - xx - zz);
/* 533 */       this.y = s * 0.5F;
/* 534 */       s = 0.5F / s;
/* 535 */       this.x = (yx + xy) * s;
/* 536 */       this.z = (zy + yz) * s;
/* 537 */       this.w = (xz - zx) * s;
/*     */     } else {
/* 539 */       float s = (float)Math.sqrt(1.0D + zz - xx - yy);
/* 540 */       this.z = s * 0.5F;
/* 541 */       s = 0.5F / s;
/* 542 */       this.x = (xz + zx) * s;
/* 543 */       this.y = (zy + yz) * s;
/* 544 */       this.w = (yx - xy) * s;
/*     */     } 
/*     */     
/* 547 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion setFromCross(Vector3 v1, Vector3 v2) {
/* 555 */     float dot = MathUtils.clamp(v1.dot(v2), -1.0F, 1.0F);
/* 556 */     float angle = (float)Math.acos(dot);
/* 557 */     return setFromAxisRad(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x, angle);
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
/*     */   public Quaternion setFromCross(float x1, float y1, float z1, float x2, float y2, float z2) {
/* 569 */     float dot = MathUtils.clamp(Vector3.dot(x1, y1, z1, x2, y2, z2), -1.0F, 1.0F);
/* 570 */     float angle = (float)Math.acos(dot);
/* 571 */     return setFromAxisRad(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2, angle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion slerp(Quaternion end, float alpha) {
/* 580 */     float d = this.x * end.x + this.y * end.y + this.z * end.z + this.w * end.w;
/* 581 */     float absDot = (d < 0.0F) ? -d : d;
/*     */ 
/*     */     
/* 584 */     float scale0 = 1.0F - alpha;
/* 585 */     float scale1 = alpha;
/*     */ 
/*     */ 
/*     */     
/* 589 */     if ((1.0F - absDot) > 0.1D) {
/*     */       
/* 591 */       float angle = (float)Math.acos(absDot);
/* 592 */       float invSinTheta = 1.0F / (float)Math.sin(angle);
/*     */ 
/*     */ 
/*     */       
/* 596 */       scale0 = (float)Math.sin(((1.0F - alpha) * angle)) * invSinTheta;
/* 597 */       scale1 = (float)Math.sin((alpha * angle)) * invSinTheta;
/*     */     } 
/*     */     
/* 600 */     if (d < 0.0F) scale1 = -scale1;
/*     */ 
/*     */ 
/*     */     
/* 604 */     this.x = scale0 * this.x + scale1 * end.x;
/* 605 */     this.y = scale0 * this.y + scale1 * end.y;
/* 606 */     this.z = scale0 * this.z + scale1 * end.z;
/* 607 */     this.w = scale0 * this.w + scale1 * end.w;
/*     */ 
/*     */     
/* 610 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion slerp(Quaternion[] q) {
/* 620 */     float w = 1.0F / q.length;
/* 621 */     set(q[0]).exp(w);
/* 622 */     for (int i = 1; i < q.length; i++)
/* 623 */       mul(tmp1.set(q[i]).exp(w)); 
/* 624 */     nor();
/* 625 */     return this;
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
/*     */   public Quaternion slerp(Quaternion[] q, float[] w) {
/* 637 */     set(q[0]).exp(w[0]);
/* 638 */     for (int i = 1; i < q.length; i++)
/* 639 */       mul(tmp1.set(q[i]).exp(w[i])); 
/* 640 */     nor();
/* 641 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion exp(float alpha) {
/* 651 */     float norm = len();
/* 652 */     float normExp = (float)Math.pow(norm, alpha);
/*     */ 
/*     */     
/* 655 */     float theta = (float)Math.acos((this.w / norm));
/*     */ 
/*     */     
/* 658 */     float coeff = 0.0F;
/* 659 */     if (Math.abs(theta) < 0.001D) {
/*     */       
/* 661 */       coeff = normExp * alpha / norm;
/*     */     } else {
/* 663 */       coeff = (float)(normExp * Math.sin((alpha * theta)) / norm * Math.sin(theta));
/*     */     } 
/*     */     
/* 666 */     this.w = (float)(normExp * Math.cos((alpha * theta)));
/* 667 */     this.x *= coeff;
/* 668 */     this.y *= coeff;
/* 669 */     this.z *= coeff;
/*     */ 
/*     */     
/* 672 */     nor();
/*     */     
/* 674 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 679 */     int prime = 31;
/* 680 */     int result = 1;
/* 681 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.w);
/* 682 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.x);
/* 683 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.y);
/* 684 */     result = 31 * result + NumberUtils.floatToRawIntBits(this.z);
/* 685 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 690 */     if (this == obj) {
/* 691 */       return true;
/*     */     }
/* 693 */     if (obj == null) {
/* 694 */       return false;
/*     */     }
/* 696 */     if (!(obj instanceof Quaternion)) {
/* 697 */       return false;
/*     */     }
/* 699 */     Quaternion other = (Quaternion)obj;
/* 700 */     return (NumberUtils.floatToRawIntBits(this.w) == NumberUtils.floatToRawIntBits(other.w) && 
/* 701 */       NumberUtils.floatToRawIntBits(this.x) == NumberUtils.floatToRawIntBits(other.x) && 
/* 702 */       NumberUtils.floatToRawIntBits(this.y) == NumberUtils.floatToRawIntBits(other.y) && 
/* 703 */       NumberUtils.floatToRawIntBits(this.z) == NumberUtils.floatToRawIntBits(other.z));
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
/*     */ 
/*     */   
/*     */   public static final float dot(float x1, float y1, float z1, float w1, float x2, float y2, float z2, float w2) {
/* 718 */     return x1 * x2 + y1 * y2 + z1 * z2 + w1 * w2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dot(Quaternion other) {
/* 725 */     return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dot(float x, float y, float z, float w) {
/* 735 */     return this.x * x + this.y * y + this.z * z + this.w * w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion mul(float scalar) {
/* 742 */     this.x *= scalar;
/* 743 */     this.y *= scalar;
/* 744 */     this.z *= scalar;
/* 745 */     this.w *= scalar;
/* 746 */     return this;
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
/*     */   
/*     */   public float getAxisAngle(Vector3 axis) {
/* 760 */     return getAxisAngleRad(axis) * 57.295776F;
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
/*     */   
/*     */   public float getAxisAngleRad(Vector3 axis) {
/* 774 */     if (this.w > 1.0F) nor(); 
/* 775 */     float angle = (float)(2.0D * Math.acos(this.w));
/* 776 */     double s = Math.sqrt((1.0F - this.w * this.w));
/* 777 */     if (s < 9.999999974752427E-7D) {
/*     */       
/* 779 */       axis.x = this.x;
/* 780 */       axis.y = this.y;
/* 781 */       axis.z = this.z;
/*     */     } else {
/* 783 */       axis.x = (float)(this.x / s);
/* 784 */       axis.y = (float)(this.y / s);
/* 785 */       axis.z = (float)(this.z / s);
/*     */     } 
/*     */     
/* 788 */     return angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleRad() {
/* 796 */     return (float)(2.0D * Math.acos((this.w > 1.0F) ? (this.w / len()) : this.w));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngle() {
/* 803 */     return getAngleRad() * 57.295776F;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void getSwingTwist(float axisX, float axisY, float axisZ, Quaternion swing, Quaternion twist) {
/* 819 */     float d = Vector3.dot(this.x, this.y, this.z, axisX, axisY, axisZ);
/* 820 */     twist.set(axisX * d, axisY * d, axisZ * d, this.w).nor();
/* 821 */     if (d < 0.0F) twist.mul(-1.0F); 
/* 822 */     swing.set(twist).conjugate().mulLeft(this);
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
/*     */   public void getSwingTwist(Vector3 axis, Quaternion swing, Quaternion twist) {
/* 835 */     getSwingTwist(axis.x, axis.y, axis.z, swing, twist);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAroundRad(float axisX, float axisY, float axisZ) {
/* 844 */     float d = Vector3.dot(this.x, this.y, this.z, axisX, axisY, axisZ);
/* 845 */     float l2 = len2(axisX * d, axisY * d, axisZ * d, this.w);
/* 846 */     return MathUtils.isZero(l2) ? 0.0F : (float)(2.0D * Math.acos(MathUtils.clamp(
/* 847 */           (float)(((d < 0.0F) ? -this.w : this.w) / Math.sqrt(l2)), -1.0F, 1.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAroundRad(Vector3 axis) {
/* 854 */     return getAngleAroundRad(axis.x, axis.y, axis.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAround(float axisX, float axisY, float axisZ) {
/* 863 */     return getAngleAroundRad(axisX, axisY, axisZ) * 57.295776F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAngleAround(Vector3 axis) {
/* 870 */     return getAngleAround(axis.x, axis.y, axis.z);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Quaternion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */