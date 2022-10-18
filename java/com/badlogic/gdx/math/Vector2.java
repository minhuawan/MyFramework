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
/*     */ public class Vector2
/*     */   implements Serializable, Vector<Vector2>
/*     */ {
/*     */   private static final long serialVersionUID = 913902788239530931L;
/*  29 */   public static final Vector2 X = new Vector2(1.0F, 0.0F);
/*  30 */   public static final Vector2 Y = new Vector2(0.0F, 1.0F);
/*  31 */   public static final Vector2 Zero = new Vector2(0.0F, 0.0F);
/*     */ 
/*     */   
/*     */   public float x;
/*     */ 
/*     */   
/*     */   public float y;
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2(float x, float y) {
/*  46 */     this.x = x;
/*  47 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2(Vector2 v) {
/*  53 */     set(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 cpy() {
/*  58 */     return new Vector2(this);
/*     */   }
/*     */   
/*     */   public static float len(float x, float y) {
/*  62 */     return (float)Math.sqrt((x * x + y * y));
/*     */   }
/*     */ 
/*     */   
/*     */   public float len() {
/*  67 */     return (float)Math.sqrt((this.x * this.x + this.y * this.y));
/*     */   }
/*     */   
/*     */   public static float len2(float x, float y) {
/*  71 */     return x * x + y * y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float len2() {
/*  76 */     return this.x * this.x + this.y * this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 set(Vector2 v) {
/*  81 */     this.x = v.x;
/*  82 */     this.y = v.y;
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 set(float x, float y) {
/*  91 */     this.x = x;
/*  92 */     this.y = y;
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 sub(Vector2 v) {
/*  98 */     this.x -= v.x;
/*  99 */     this.y -= v.y;
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 sub(float x, float y) {
/* 108 */     this.x -= x;
/* 109 */     this.y -= y;
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 nor() {
/* 115 */     float len = len();
/* 116 */     if (len != 0.0F) {
/* 117 */       this.x /= len;
/* 118 */       this.y /= len;
/*     */     } 
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 add(Vector2 v) {
/* 125 */     this.x += v.x;
/* 126 */     this.y += v.y;
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 add(float x, float y) {
/* 135 */     this.x += x;
/* 136 */     this.y += y;
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public static float dot(float x1, float y1, float x2, float y2) {
/* 141 */     return x1 * x2 + y1 * y2;
/*     */   }
/*     */ 
/*     */   
/*     */   public float dot(Vector2 v) {
/* 146 */     return this.x * v.x + this.y * v.y;
/*     */   }
/*     */   
/*     */   public float dot(float ox, float oy) {
/* 150 */     return this.x * ox + this.y * oy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 scl(float scalar) {
/* 155 */     this.x *= scalar;
/* 156 */     this.y *= scalar;
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 scl(float x, float y) {
/* 163 */     this.x *= x;
/* 164 */     this.y *= y;
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 scl(Vector2 v) {
/* 170 */     this.x *= v.x;
/* 171 */     this.y *= v.y;
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 mulAdd(Vector2 vec, float scalar) {
/* 177 */     this.x += vec.x * scalar;
/* 178 */     this.y += vec.y * scalar;
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 mulAdd(Vector2 vec, Vector2 mulVec) {
/* 184 */     this.x += vec.x * mulVec.x;
/* 185 */     this.y += vec.y * mulVec.y;
/* 186 */     return this;
/*     */   }
/*     */   
/*     */   public static float dst(float x1, float y1, float x2, float y2) {
/* 190 */     float x_d = x2 - x1;
/* 191 */     float y_d = y2 - y1;
/* 192 */     return (float)Math.sqrt((x_d * x_d + y_d * y_d));
/*     */   }
/*     */ 
/*     */   
/*     */   public float dst(Vector2 v) {
/* 197 */     float x_d = v.x - this.x;
/* 198 */     float y_d = v.y - this.y;
/* 199 */     return (float)Math.sqrt((x_d * x_d + y_d * y_d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst(float x, float y) {
/* 206 */     float x_d = x - this.x;
/* 207 */     float y_d = y - this.y;
/* 208 */     return (float)Math.sqrt((x_d * x_d + y_d * y_d));
/*     */   }
/*     */   
/*     */   public static float dst2(float x1, float y1, float x2, float y2) {
/* 212 */     float x_d = x2 - x1;
/* 213 */     float y_d = y2 - y1;
/* 214 */     return x_d * x_d + y_d * y_d;
/*     */   }
/*     */ 
/*     */   
/*     */   public float dst2(Vector2 v) {
/* 219 */     float x_d = v.x - this.x;
/* 220 */     float y_d = v.y - this.y;
/* 221 */     return x_d * x_d + y_d * y_d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst2(float x, float y) {
/* 228 */     float x_d = x - this.x;
/* 229 */     float y_d = y - this.y;
/* 230 */     return x_d * x_d + y_d * y_d;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 limit(float limit) {
/* 235 */     return limit2(limit * limit);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 limit2(float limit2) {
/* 240 */     float len2 = len2();
/* 241 */     if (len2 > limit2) {
/* 242 */       return scl((float)Math.sqrt((limit2 / len2)));
/*     */     }
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 clamp(float min, float max) {
/* 249 */     float len2 = len2();
/* 250 */     if (len2 == 0.0F) return this; 
/* 251 */     float max2 = max * max;
/* 252 */     if (len2 > max2) return scl((float)Math.sqrt((max2 / len2))); 
/* 253 */     float min2 = min * min;
/* 254 */     if (len2 < min2) return scl((float)Math.sqrt((min2 / len2))); 
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 setLength(float len) {
/* 260 */     return setLength2(len * len);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 setLength2(float len2) {
/* 265 */     float oldLen2 = len2();
/* 266 */     return (oldLen2 == 0.0F || oldLen2 == len2) ? this : scl((float)Math.sqrt((len2 / oldLen2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 273 */     return "(" + this.x + "," + this.y + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 fromString(String v) {
/* 280 */     int s = v.indexOf(',', 1);
/* 281 */     if (s != -1 && v.charAt(0) == '(' && v.charAt(v.length() - 1) == ')') {
/*     */       try {
/* 283 */         float x = Float.parseFloat(v.substring(1, s));
/* 284 */         float y = Float.parseFloat(v.substring(s + 1, v.length() - 1));
/* 285 */         return set(x, y);
/* 286 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */     
/* 290 */     throw new GdxRuntimeException("Malformed Vector2: " + v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 mul(Matrix3 mat) {
/* 297 */     float x = this.x * mat.val[0] + this.y * mat.val[3] + mat.val[6];
/* 298 */     float y = this.x * mat.val[1] + this.y * mat.val[4] + mat.val[7];
/* 299 */     this.x = x;
/* 300 */     this.y = y;
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float crs(Vector2 v) {
/* 308 */     return this.x * v.y - this.y * v.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float crs(float x, float y) {
/* 316 */     return this.x * y - this.y * x;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float angle() {
/* 322 */     float angle = (float)Math.atan2(this.y, this.x) * 57.295776F;
/* 323 */     if (angle < 0.0F) angle += 360.0F; 
/* 324 */     return angle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float angle(Vector2 reference) {
/* 330 */     return (float)Math.atan2(crs(reference), dot(reference)) * 57.295776F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float angleRad() {
/* 336 */     return (float)Math.atan2(this.y, this.x);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float angleRad(Vector2 reference) {
/* 342 */     return (float)Math.atan2(crs(reference), dot(reference));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 setAngle(float degrees) {
/* 348 */     return setAngleRad(degrees * 0.017453292F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 setAngleRad(float radians) {
/* 354 */     set(len(), 0.0F);
/* 355 */     rotateRad(radians);
/*     */     
/* 357 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 rotate(float degrees) {
/* 363 */     return rotateRad(degrees * 0.017453292F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 rotateRad(float radians) {
/* 369 */     float cos = (float)Math.cos(radians);
/* 370 */     float sin = (float)Math.sin(radians);
/*     */     
/* 372 */     float newX = this.x * cos - this.y * sin;
/* 373 */     float newY = this.x * sin + this.y * cos;
/*     */     
/* 375 */     this.x = newX;
/* 376 */     this.y = newY;
/*     */     
/* 378 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 rotate90(int dir) {
/* 383 */     float x = this.x;
/* 384 */     if (dir >= 0) {
/* 385 */       this.x = -this.y;
/* 386 */       this.y = x;
/*     */     } else {
/* 388 */       this.x = this.y;
/* 389 */       this.y = -x;
/*     */     } 
/* 391 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 lerp(Vector2 target, float alpha) {
/* 396 */     float invAlpha = 1.0F - alpha;
/* 397 */     this.x = this.x * invAlpha + target.x * alpha;
/* 398 */     this.y = this.y * invAlpha + target.y * alpha;
/* 399 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 interpolate(Vector2 target, float alpha, Interpolation interpolation) {
/* 404 */     return lerp(target, interpolation.apply(alpha));
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 setToRandomDirection() {
/* 409 */     float theta = MathUtils.random(0.0F, 6.2831855F);
/* 410 */     return set(MathUtils.cos(theta), MathUtils.sin(theta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 415 */     int prime = 31;
/* 416 */     int result = 1;
/* 417 */     result = 31 * result + NumberUtils.floatToIntBits(this.x);
/* 418 */     result = 31 * result + NumberUtils.floatToIntBits(this.y);
/* 419 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 424 */     if (this == obj) return true; 
/* 425 */     if (obj == null) return false; 
/* 426 */     if (getClass() != obj.getClass()) return false; 
/* 427 */     Vector2 other = (Vector2)obj;
/* 428 */     if (NumberUtils.floatToIntBits(this.x) != NumberUtils.floatToIntBits(other.x)) return false; 
/* 429 */     if (NumberUtils.floatToIntBits(this.y) != NumberUtils.floatToIntBits(other.y)) return false; 
/* 430 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean epsilonEquals(Vector2 other, float epsilon) {
/* 435 */     if (other == null) return false; 
/* 436 */     if (Math.abs(other.x - this.x) > epsilon) return false; 
/* 437 */     if (Math.abs(other.y - this.y) > epsilon) return false; 
/* 438 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean epsilonEquals(float x, float y, float epsilon) {
/* 444 */     if (Math.abs(x - this.x) > epsilon) return false; 
/* 445 */     if (Math.abs(y - this.y) > epsilon) return false; 
/* 446 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnit() {
/* 451 */     return isUnit(1.0E-9F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnit(float margin) {
/* 456 */     return (Math.abs(len2() - 1.0F) < margin);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/* 461 */     return (this.x == 0.0F && this.y == 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero(float margin) {
/* 466 */     return (len2() < margin);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnLine(Vector2 other) {
/* 471 */     return MathUtils.isZero(this.x * other.y - this.y * other.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnLine(Vector2 other, float epsilon) {
/* 476 */     return MathUtils.isZero(this.x * other.y - this.y * other.x, epsilon);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinear(Vector2 other, float epsilon) {
/* 481 */     return (isOnLine(other, epsilon) && dot(other) > 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinear(Vector2 other) {
/* 486 */     return (isOnLine(other) && dot(other) > 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinearOpposite(Vector2 other, float epsilon) {
/* 491 */     return (isOnLine(other, epsilon) && dot(other) < 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollinearOpposite(Vector2 other) {
/* 496 */     return (isOnLine(other) && dot(other) < 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPerpendicular(Vector2 vector) {
/* 501 */     return MathUtils.isZero(dot(vector));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPerpendicular(Vector2 vector, float epsilon) {
/* 506 */     return MathUtils.isZero(dot(vector), epsilon);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSameDirection(Vector2 vector) {
/* 511 */     return (dot(vector) > 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasOppositeDirection(Vector2 vector) {
/* 516 */     return (dot(vector) < 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 setZero() {
/* 521 */     this.x = 0.0F;
/* 522 */     this.y = 0.0F;
/* 523 */     return this;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Vector2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */