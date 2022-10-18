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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Vector3
/*     */   implements Serializable, Vector<Vector3>
/*     */ {
/*     */   private static final long serialVersionUID = 3840054589595372522L;
/*     */   public float x;
/*     */   public float y;
/*     */   public float z;
/*  36 */   public static final Vector3 X = new Vector3(1.0F, 0.0F, 0.0F);
/*  37 */   public static final Vector3 Y = new Vector3(0.0F, 1.0F, 0.0F);
/*  38 */   public static final Vector3 Z = new Vector3(0.0F, 0.0F, 1.0F);
/*  39 */   public static final Vector3 Zero = new Vector3(0.0F, 0.0F, 0.0F);
/*     */   
/*  41 */   private static final Matrix4 tmpMat = new Matrix4();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3(float x, float y, float z) {
/*  52 */     set(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3(Vector3 vector) {
/*  58 */     set(vector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3(float[] values) {
/*  65 */     set(values[0], values[1], values[2]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3(Vector2 vector, float z) {
/*  73 */     set(vector.x, vector.y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 set(float x, float y, float z) {
/*  83 */     this.x = x;
/*  84 */     this.y = y;
/*  85 */     this.z = z;
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 set(Vector3 vector) {
/*  91 */     return set(vector.x, vector.y, vector.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 set(float[] values) {
/*  99 */     return set(values[0], values[1], values[2]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 set(Vector2 vector, float z) {
/* 108 */     return set(vector.x, vector.y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 setFromSpherical(float azimuthalAngle, float polarAngle) {
/* 116 */     float cosPolar = MathUtils.cos(polarAngle);
/* 117 */     float sinPolar = MathUtils.sin(polarAngle);
/*     */     
/* 119 */     float cosAzim = MathUtils.cos(azimuthalAngle);
/* 120 */     float sinAzim = MathUtils.sin(azimuthalAngle);
/*     */     
/* 122 */     return set(cosAzim * sinPolar, sinAzim * sinPolar, cosPolar);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 setToRandomDirection() {
/* 127 */     float u = MathUtils.random();
/* 128 */     float v = MathUtils.random();
/*     */     
/* 130 */     float theta = 6.2831855F * u;
/* 131 */     float phi = (float)Math.acos((2.0F * v - 1.0F));
/*     */     
/* 133 */     return setFromSpherical(theta, phi);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 cpy() {
/* 138 */     return new Vector3(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 add(Vector3 vector) {
/* 143 */     return add(vector.x, vector.y, vector.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 add(float x, float y, float z) {
/* 152 */     return set(this.x + x, this.y + y, this.z + z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 add(float values) {
/* 160 */     return set(this.x + values, this.y + values, this.z + values);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 sub(Vector3 a_vec) {
/* 165 */     return sub(a_vec.x, a_vec.y, a_vec.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 sub(float x, float y, float z) {
/* 175 */     return set(this.x - x, this.y - y, this.z - z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 sub(float value) {
/* 183 */     return set(this.x - value, this.y - value, this.z - value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 scl(float scalar) {
/* 188 */     return set(this.x * scalar, this.y * scalar, this.z * scalar);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 scl(Vector3 other) {
/* 193 */     return set(this.x * other.x, this.y * other.y, this.z * other.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 scl(float vx, float vy, float vz) {
/* 202 */     return set(this.x * vx, this.y * vy, this.z * vz);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 mulAdd(Vector3 vec, float scalar) {
/* 207 */     this.x += vec.x * scalar;
/* 208 */     this.y += vec.y * scalar;
/* 209 */     this.z += vec.z * scalar;
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 mulAdd(Vector3 vec, Vector3 mulVec) {
/* 215 */     this.x += vec.x * mulVec.x;
/* 216 */     this.y += vec.y * mulVec.y;
/* 217 */     this.z += vec.z * mulVec.z;
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float len(float x, float y, float z) {
/* 223 */     return (float)Math.sqrt((x * x + y * y + z * z));
/*     */   }
/*     */ 
/*     */   
/*     */   public float len() {
/* 228 */     return (float)Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float len2(float x, float y, float z) {
/* 233 */     return x * x + y * y + z * z;
/*     */   }
/*     */ 
/*     */   
/*     */   public float len2() {
/* 238 */     return this.x * this.x + this.y * this.y + this.z * this.z;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean idt(Vector3 vector) {
/* 244 */     return (this.x == vector.x && this.y == vector.y && this.z == vector.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float dst(float x1, float y1, float z1, float x2, float y2, float z2) {
/* 249 */     float a = x2 - x1;
/* 250 */     float b = y2 - y1;
/* 251 */     float c = z2 - z1;
/* 252 */     return (float)Math.sqrt((a * a + b * b + c * c));
/*     */   }
/*     */ 
/*     */   
/*     */   public float dst(Vector3 vector) {
/* 257 */     float a = vector.x - this.x;
/* 258 */     float b = vector.y - this.y;
/* 259 */     float c = vector.z - this.z;
/* 260 */     return (float)Math.sqrt((a * a + b * b + c * c));
/*     */   }
/*     */ 
/*     */   
/*     */   public float dst(float x, float y, float z) {
/* 265 */     float a = x - this.x;
/* 266 */     float b = y - this.y;
/* 267 */     float c = z - this.z;
/* 268 */     return (float)Math.sqrt((a * a + b * b + c * c));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float dst2(float x1, float y1, float z1, float x2, float y2, float z2) {
/* 273 */     float a = x2 - x1;
/* 274 */     float b = y2 - y1;
/* 275 */     float c = z2 - z1;
/* 276 */     return a * a + b * b + c * c;
/*     */   }
/*     */ 
/*     */   
/*     */   public float dst2(Vector3 point) {
/* 281 */     float a = point.x - this.x;
/* 282 */     float b = point.y - this.y;
/* 283 */     float c = point.z - this.z;
/* 284 */     return a * a + b * b + c * c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst2(float x, float y, float z) {
/* 293 */     float a = x - this.x;
/* 294 */     float b = y - this.y;
/* 295 */     float c = z - this.z;
/* 296 */     return a * a + b * b + c * c;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 nor() {
/* 301 */     float len2 = len2();
/* 302 */     if (len2 == 0.0F || len2 == 1.0F) return this; 
/* 303 */     return scl(1.0F / (float)Math.sqrt(len2));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
/* 308 */     return x1 * x2 + y1 * y2 + z1 * z2;
/*     */   }
/*     */ 
/*     */   
/*     */   public float dot(Vector3 vector) {
/* 313 */     return this.x * vector.x + this.y * vector.y + this.z * vector.z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dot(float x, float y, float z) {
/* 322 */     return this.x * x + this.y * y + this.z * z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 crs(Vector3 vector) {
/* 329 */     return set(this.y * vector.z - this.z * vector.y, this.z * vector.x - this.x * vector.z, this.x * vector.y - this.y * vector.x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 crs(float x, float y, float z) {
/* 338 */     return set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 mul4x3(float[] matrix) {
/* 346 */     return set(this.x * matrix[0] + this.y * matrix[3] + this.z * matrix[6] + matrix[9], this.x * matrix[1] + this.y * matrix[4] + this.z * matrix[7] + matrix[10], this.x * matrix[2] + this.y * matrix[5] + this.z * matrix[8] + matrix[11]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 mul(Matrix4 matrix) {
/* 354 */     float[] l_mat = matrix.val;
/* 355 */     return set(this.x * l_mat[0] + this.y * l_mat[4] + this.z * l_mat[8] + l_mat[12], this.x * l_mat[1] + this.y * l_mat[5] + this.z * l_mat[9] + l_mat[13], this.x * l_mat[2] + this.y * l_mat[6] + this.z * l_mat[10] + l_mat[14]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 traMul(Matrix4 matrix) {
/* 364 */     float[] l_mat = matrix.val;
/* 365 */     return set(this.x * l_mat[0] + this.y * l_mat[1] + this.z * l_mat[2] + l_mat[3], this.x * l_mat[4] + this.y * l_mat[5] + this.z * l_mat[6] + l_mat[7], this.x * l_mat[8] + this.y * l_mat[9] + this.z * l_mat[10] + l_mat[11]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 mul(Matrix3 matrix) {
/* 374 */     float[] l_mat = matrix.val;
/* 375 */     return set(this.x * l_mat[0] + this.y * l_mat[3] + this.z * l_mat[6], this.x * l_mat[1] + this.y * l_mat[4] + this.z * l_mat[7], this.x * l_mat[2] + this.y * l_mat[5] + this.z * l_mat[8]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 traMul(Matrix3 matrix) {
/* 383 */     float[] l_mat = matrix.val;
/* 384 */     return set(this.x * l_mat[0] + this.y * l_mat[1] + this.z * l_mat[2], this.x * l_mat[3] + this.y * l_mat[4] + this.z * l_mat[5], this.x * l_mat[6] + this.y * l_mat[7] + this.z * l_mat[8]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 mul(Quaternion quat) {
/* 391 */     return quat.transform(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 prj(Matrix4 matrix) {
/* 400 */     float[] l_mat = matrix.val;
/* 401 */     float l_w = 1.0F / (this.x * l_mat[3] + this.y * l_mat[7] + this.z * l_mat[11] + l_mat[15]);
/* 402 */     return set((this.x * l_mat[0] + this.y * l_mat[4] + this.z * l_mat[8] + l_mat[12]) * l_w, (this.x * l_mat[1] + this.y * l_mat[5] + this.z * l_mat[9] + l_mat[13]) * l_w, (this.x * l_mat[2] + this.y * l_mat[6] + this.z * l_mat[10] + l_mat[14]) * l_w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 rot(Matrix4 matrix) {
/* 412 */     float[] l_mat = matrix.val;
/* 413 */     return set(this.x * l_mat[0] + this.y * l_mat[4] + this.z * l_mat[8], this.x * l_mat[1] + this.y * l_mat[5] + this.z * l_mat[9], this.x * l_mat[2] + this.y * l_mat[6] + this.z * l_mat[10]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 unrotate(Matrix4 matrix) {
/* 422 */     float[] l_mat = matrix.val;
/* 423 */     return set(this.x * l_mat[0] + this.y * l_mat[1] + this.z * l_mat[2], this.x * l_mat[4] + this.y * l_mat[5] + this.z * l_mat[6], this.x * l_mat[8] + this.y * l_mat[9] + this.z * l_mat[10]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 untransform(Matrix4 matrix) {
/* 433 */     float[] l_mat = matrix.val;
/* 434 */     this.x -= l_mat[12];
/* 435 */     this.y -= l_mat[12];
/* 436 */     this.z -= l_mat[12];
/* 437 */     return set(this.x * l_mat[0] + this.y * l_mat[1] + this.z * l_mat[2], this.x * l_mat[4] + this.y * l_mat[5] + this.z * l_mat[6], this.x * l_mat[8] + this.y * l_mat[9] + this.z * l_mat[10]);
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
/*     */   public Vector3 rotate(float degrees, float axisX, float axisY, float axisZ) {
/* 449 */     return mul(tmpMat.setToRotation(axisX, axisY, axisZ, degrees));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 rotateRad(float radians, float axisX, float axisY, float axisZ) {
/* 460 */     return mul(tmpMat.setToRotationRad(axisX, axisY, axisZ, radians));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 rotate(Vector3 axis, float degrees) {
/* 469 */     tmpMat.setToRotation(axis, degrees);
/* 470 */     return mul(tmpMat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 rotateRad(Vector3 axis, float radians) {
/* 479 */     tmpMat.setToRotationRad(axis, radians);
/* 480 */     return mul(tmpMat);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnit() {
/* 485 */     return isUnit(1.0E-9F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnit(float margin) {
/* 490 */     return (Math.abs(len2() - 1.0F) < margin);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/* 495 */     return (this.x == 0.0F && this.y == 0.0F && this.z == 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero(float margin) {
/* 500 */     return (len2() < margin);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnLine(Vector3 other, float epsilon) {
/* 505 */     return (len2(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x) <= epsilon);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnLine(Vector3 other) {
/* 510 */     return (len2(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x) <= 1.0E-6F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinear(Vector3 other, float epsilon) {
/* 515 */     return (isOnLine(other, epsilon) && hasSameDirection(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinear(Vector3 other) {
/* 520 */     return (isOnLine(other) && hasSameDirection(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinearOpposite(Vector3 other, float epsilon) {
/* 525 */     return (isOnLine(other, epsilon) && hasOppositeDirection(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinearOpposite(Vector3 other) {
/* 530 */     return (isOnLine(other) && hasOppositeDirection(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPerpendicular(Vector3 vector) {
/* 535 */     return MathUtils.isZero(dot(vector));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPerpendicular(Vector3 vector, float epsilon) {
/* 540 */     return MathUtils.isZero(dot(vector), epsilon);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSameDirection(Vector3 vector) {
/* 545 */     return (dot(vector) > 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasOppositeDirection(Vector3 vector) {
/* 550 */     return (dot(vector) < 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 lerp(Vector3 target, float alpha) {
/* 555 */     this.x += alpha * (target.x - this.x);
/* 556 */     this.y += alpha * (target.y - this.y);
/* 557 */     this.z += alpha * (target.z - this.z);
/* 558 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 interpolate(Vector3 target, float alpha, Interpolation interpolator) {
/* 563 */     return lerp(target, interpolator.apply(0.0F, 1.0F, alpha));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 slerp(Vector3 target, float alpha) {
/* 573 */     float dot = dot(target);
/*     */     
/* 575 */     if (dot > 0.9995D || dot < -0.9995D) return lerp(target, alpha);
/*     */ 
/*     */     
/* 578 */     float theta0 = (float)Math.acos(dot);
/*     */     
/* 580 */     float theta = theta0 * alpha;
/*     */     
/* 582 */     float st = (float)Math.sin(theta);
/* 583 */     float tx = target.x - this.x * dot;
/* 584 */     float ty = target.y - this.y * dot;
/* 585 */     float tz = target.z - this.z * dot;
/* 586 */     float l2 = tx * tx + ty * ty + tz * tz;
/* 587 */     float dl = st * ((l2 < 1.0E-4F) ? 1.0F : (1.0F / (float)Math.sqrt(l2)));
/*     */     
/* 589 */     return scl((float)Math.cos(theta)).add(tx * dl, ty * dl, tz * dl).nor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 596 */     return "(" + this.x + "," + this.y + "," + this.z + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 fromString(String v) {
/* 603 */     int s0 = v.indexOf(',', 1);
/* 604 */     int s1 = v.indexOf(',', s0 + 1);
/* 605 */     if (s0 != -1 && s1 != -1 && v.charAt(0) == '(' && v.charAt(v.length() - 1) == ')') {
/*     */       try {
/* 607 */         float x = Float.parseFloat(v.substring(1, s0));
/* 608 */         float y = Float.parseFloat(v.substring(s0 + 1, s1));
/* 609 */         float z = Float.parseFloat(v.substring(s1 + 1, v.length() - 1));
/* 610 */         return set(x, y, z);
/* 611 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */     
/* 615 */     throw new GdxRuntimeException("Malformed Vector3: " + v);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 limit(float limit) {
/* 620 */     return limit2(limit * limit);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 limit2(float limit2) {
/* 625 */     float len2 = len2();
/* 626 */     if (len2 > limit2) {
/* 627 */       scl((float)Math.sqrt((limit2 / len2)));
/*     */     }
/* 629 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 setLength(float len) {
/* 634 */     return setLength2(len * len);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 setLength2(float len2) {
/* 639 */     float oldLen2 = len2();
/* 640 */     return (oldLen2 == 0.0F || oldLen2 == len2) ? this : scl((float)Math.sqrt((len2 / oldLen2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 clamp(float min, float max) {
/* 645 */     float len2 = len2();
/* 646 */     if (len2 == 0.0F) return this; 
/* 647 */     float max2 = max * max;
/* 648 */     if (len2 > max2) return scl((float)Math.sqrt((max2 / len2))); 
/* 649 */     float min2 = min * min;
/* 650 */     if (len2 < min2) return scl((float)Math.sqrt((min2 / len2))); 
/* 651 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 656 */     int prime = 31;
/* 657 */     int result = 1;
/* 658 */     result = 31 * result + NumberUtils.floatToIntBits(this.x);
/* 659 */     result = 31 * result + NumberUtils.floatToIntBits(this.y);
/* 660 */     result = 31 * result + NumberUtils.floatToIntBits(this.z);
/* 661 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 666 */     if (this == obj) return true; 
/* 667 */     if (obj == null) return false; 
/* 668 */     if (getClass() != obj.getClass()) return false; 
/* 669 */     Vector3 other = (Vector3)obj;
/* 670 */     if (NumberUtils.floatToIntBits(this.x) != NumberUtils.floatToIntBits(other.x)) return false; 
/* 671 */     if (NumberUtils.floatToIntBits(this.y) != NumberUtils.floatToIntBits(other.y)) return false; 
/* 672 */     if (NumberUtils.floatToIntBits(this.z) != NumberUtils.floatToIntBits(other.z)) return false; 
/* 673 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean epsilonEquals(Vector3 other, float epsilon) {
/* 678 */     if (other == null) return false; 
/* 679 */     if (Math.abs(other.x - this.x) > epsilon) return false; 
/* 680 */     if (Math.abs(other.y - this.y) > epsilon) return false; 
/* 681 */     if (Math.abs(other.z - this.z) > epsilon) return false; 
/* 682 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean epsilonEquals(float x, float y, float z, float epsilon) {
/* 688 */     if (Math.abs(x - this.x) > epsilon) return false; 
/* 689 */     if (Math.abs(y - this.y) > epsilon) return false; 
/* 690 */     if (Math.abs(z - this.z) > epsilon) return false; 
/* 691 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 setZero() {
/* 696 */     this.x = 0.0F;
/* 697 */     this.y = 0.0F;
/* 698 */     this.z = 0.0F;
/* 699 */     return this;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Vector3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */