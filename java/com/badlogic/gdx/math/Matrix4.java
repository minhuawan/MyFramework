/*      */ package com.badlogic.gdx.math;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Matrix4
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -2717655254359579617L;
/*      */   public static final int M00 = 0;
/*      */   public static final int M01 = 4;
/*      */   public static final int M02 = 8;
/*      */   public static final int M03 = 12;
/*      */   public static final int M10 = 1;
/*      */   public static final int M11 = 5;
/*      */   public static final int M12 = 9;
/*      */   public static final int M13 = 13;
/*      */   public static final int M20 = 2;
/*      */   public static final int M21 = 6;
/*      */   public static final int M22 = 10;
/*      */   public static final int M23 = 14;
/*      */   public static final int M30 = 3;
/*      */   public static final int M31 = 7;
/*      */   public static final int M32 = 11;
/*      */   public static final int M33 = 15;
/*   73 */   private static final float[] tmp = new float[16];
/*   74 */   public final float[] val = new float[16];
/*      */ 
/*      */   
/*      */   public Matrix4() {
/*   78 */     this.val[0] = 1.0F;
/*   79 */     this.val[5] = 1.0F;
/*   80 */     this.val[10] = 1.0F;
/*   81 */     this.val[15] = 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4(Matrix4 matrix) {
/*   88 */     set(matrix);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4(float[] values) {
/*   95 */     set(values);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4(Quaternion quaternion) {
/*  101 */     set(quaternion);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4(Vector3 position, Quaternion rotation, Vector3 scale) {
/*  109 */     set(position, rotation, scale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(Matrix4 matrix) {
/*  117 */     return set(matrix.val);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(float[] values) {
/*  127 */     System.arraycopy(values, 0, this.val, 0, this.val.length);
/*  128 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(Quaternion quaternion) {
/*  136 */     return set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(float quaternionX, float quaternionY, float quaternionZ, float quaternionW) {
/*  147 */     return set(0.0F, 0.0F, 0.0F, quaternionX, quaternionY, quaternionZ, quaternionW);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(Vector3 position, Quaternion orientation) {
/*  155 */     return set(position.x, position.y, position.z, orientation.x, orientation.y, orientation.z, orientation.w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(float translationX, float translationY, float translationZ, float quaternionX, float quaternionY, float quaternionZ, float quaternionW) {
/*  170 */     float xs = quaternionX * 2.0F, ys = quaternionY * 2.0F, zs = quaternionZ * 2.0F;
/*  171 */     float wx = quaternionW * xs, wy = quaternionW * ys, wz = quaternionW * zs;
/*  172 */     float xx = quaternionX * xs, xy = quaternionX * ys, xz = quaternionX * zs;
/*  173 */     float yy = quaternionY * ys, yz = quaternionY * zs, zz = quaternionZ * zs;
/*      */     
/*  175 */     this.val[0] = 1.0F - yy + zz;
/*  176 */     this.val[4] = xy - wz;
/*  177 */     this.val[8] = xz + wy;
/*  178 */     this.val[12] = translationX;
/*      */     
/*  180 */     this.val[1] = xy + wz;
/*  181 */     this.val[5] = 1.0F - xx + zz;
/*  182 */     this.val[9] = yz - wx;
/*  183 */     this.val[13] = translationY;
/*      */     
/*  185 */     this.val[2] = xz - wy;
/*  186 */     this.val[6] = yz + wx;
/*  187 */     this.val[10] = 1.0F - xx + yy;
/*  188 */     this.val[14] = translationZ;
/*      */     
/*  190 */     this.val[3] = 0.0F;
/*  191 */     this.val[7] = 0.0F;
/*  192 */     this.val[11] = 0.0F;
/*  193 */     this.val[15] = 1.0F;
/*  194 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(Vector3 position, Quaternion orientation, Vector3 scale) {
/*  203 */     return set(position.x, position.y, position.z, orientation.x, orientation.y, orientation.z, orientation.w, scale.x, scale.y, scale.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(float translationX, float translationY, float translationZ, float quaternionX, float quaternionY, float quaternionZ, float quaternionW, float scaleX, float scaleY, float scaleZ) {
/*  222 */     float xs = quaternionX * 2.0F, ys = quaternionY * 2.0F, zs = quaternionZ * 2.0F;
/*  223 */     float wx = quaternionW * xs, wy = quaternionW * ys, wz = quaternionW * zs;
/*  224 */     float xx = quaternionX * xs, xy = quaternionX * ys, xz = quaternionX * zs;
/*  225 */     float yy = quaternionY * ys, yz = quaternionY * zs, zz = quaternionZ * zs;
/*      */     
/*  227 */     this.val[0] = scaleX * (1.0F - yy + zz);
/*  228 */     this.val[4] = scaleY * (xy - wz);
/*  229 */     this.val[8] = scaleZ * (xz + wy);
/*  230 */     this.val[12] = translationX;
/*      */     
/*  232 */     this.val[1] = scaleX * (xy + wz);
/*  233 */     this.val[5] = scaleY * (1.0F - xx + zz);
/*  234 */     this.val[9] = scaleZ * (yz - wx);
/*  235 */     this.val[13] = translationY;
/*      */     
/*  237 */     this.val[2] = scaleX * (xz - wy);
/*  238 */     this.val[6] = scaleY * (yz + wx);
/*  239 */     this.val[10] = scaleZ * (1.0F - xx + yy);
/*  240 */     this.val[14] = translationZ;
/*      */     
/*  242 */     this.val[3] = 0.0F;
/*  243 */     this.val[7] = 0.0F;
/*  244 */     this.val[11] = 0.0F;
/*  245 */     this.val[15] = 1.0F;
/*  246 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(Vector3 xAxis, Vector3 yAxis, Vector3 zAxis, Vector3 pos) {
/*  257 */     this.val[0] = xAxis.x;
/*  258 */     this.val[4] = xAxis.y;
/*  259 */     this.val[8] = xAxis.z;
/*  260 */     this.val[1] = yAxis.x;
/*  261 */     this.val[5] = yAxis.y;
/*  262 */     this.val[9] = yAxis.z;
/*  263 */     this.val[2] = zAxis.x;
/*  264 */     this.val[6] = zAxis.y;
/*  265 */     this.val[10] = zAxis.z;
/*  266 */     this.val[12] = pos.x;
/*  267 */     this.val[13] = pos.y;
/*  268 */     this.val[14] = pos.z;
/*  269 */     this.val[3] = 0.0F;
/*  270 */     this.val[7] = 0.0F;
/*  271 */     this.val[11] = 0.0F;
/*  272 */     this.val[15] = 1.0F;
/*  273 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 cpy() {
/*  278 */     return new Matrix4(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 trn(Vector3 vector) {
/*  286 */     this.val[12] = this.val[12] + vector.x;
/*  287 */     this.val[13] = this.val[13] + vector.y;
/*  288 */     this.val[14] = this.val[14] + vector.z;
/*  289 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 trn(float x, float y, float z) {
/*  299 */     this.val[12] = this.val[12] + x;
/*  300 */     this.val[13] = this.val[13] + y;
/*  301 */     this.val[14] = this.val[14] + z;
/*  302 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public float[] getValues() {
/*  307 */     return this.val;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 mul(Matrix4 matrix) {
/*  319 */     mul(this.val, matrix.val);
/*  320 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 mulLeft(Matrix4 matrix) {
/*  332 */     tmpMat.set(matrix);
/*  333 */     mul(tmpMat.val, this.val);
/*  334 */     return set(tmpMat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 tra() {
/*  341 */     tmp[0] = this.val[0];
/*  342 */     tmp[4] = this.val[1];
/*  343 */     tmp[8] = this.val[2];
/*  344 */     tmp[12] = this.val[3];
/*  345 */     tmp[1] = this.val[4];
/*  346 */     tmp[5] = this.val[5];
/*  347 */     tmp[9] = this.val[6];
/*  348 */     tmp[13] = this.val[7];
/*  349 */     tmp[2] = this.val[8];
/*  350 */     tmp[6] = this.val[9];
/*  351 */     tmp[10] = this.val[10];
/*  352 */     tmp[14] = this.val[11];
/*  353 */     tmp[3] = this.val[12];
/*  354 */     tmp[7] = this.val[13];
/*  355 */     tmp[11] = this.val[14];
/*  356 */     tmp[15] = this.val[15];
/*  357 */     return set(tmp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 idt() {
/*  364 */     this.val[0] = 1.0F;
/*  365 */     this.val[4] = 0.0F;
/*  366 */     this.val[8] = 0.0F;
/*  367 */     this.val[12] = 0.0F;
/*  368 */     this.val[1] = 0.0F;
/*  369 */     this.val[5] = 1.0F;
/*  370 */     this.val[9] = 0.0F;
/*  371 */     this.val[13] = 0.0F;
/*  372 */     this.val[2] = 0.0F;
/*  373 */     this.val[6] = 0.0F;
/*  374 */     this.val[10] = 1.0F;
/*  375 */     this.val[14] = 0.0F;
/*  376 */     this.val[3] = 0.0F;
/*  377 */     this.val[7] = 0.0F;
/*  378 */     this.val[11] = 0.0F;
/*  379 */     this.val[15] = 1.0F;
/*  380 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 inv() {
/*  388 */     float l_det = this.val[3] * this.val[6] * this.val[9] * this.val[12] - this.val[2] * this.val[7] * this.val[9] * this.val[12] - this.val[3] * this.val[5] * this.val[10] * this.val[12] + this.val[1] * this.val[7] * this.val[10] * this.val[12] + this.val[2] * this.val[5] * this.val[11] * this.val[12] - this.val[1] * this.val[6] * this.val[11] * this.val[12] - this.val[3] * this.val[6] * this.val[8] * this.val[13] + this.val[2] * this.val[7] * this.val[8] * this.val[13] + this.val[3] * this.val[4] * this.val[10] * this.val[13] - this.val[0] * this.val[7] * this.val[10] * this.val[13] - this.val[2] * this.val[4] * this.val[11] * this.val[13] + this.val[0] * this.val[6] * this.val[11] * this.val[13] + this.val[3] * this.val[5] * this.val[8] * this.val[14] - this.val[1] * this.val[7] * this.val[8] * this.val[14] - this.val[3] * this.val[4] * this.val[9] * this.val[14] + this.val[0] * this.val[7] * this.val[9] * this.val[14] + this.val[1] * this.val[4] * this.val[11] * this.val[14] - this.val[0] * this.val[5] * this.val[11] * this.val[14] - this.val[2] * this.val[5] * this.val[8] * this.val[15] + this.val[1] * this.val[6] * this.val[8] * this.val[15] + this.val[2] * this.val[4] * this.val[9] * this.val[15] - this.val[0] * this.val[6] * this.val[9] * this.val[15] - this.val[1] * this.val[4] * this.val[10] * this.val[15] + this.val[0] * this.val[5] * this.val[10] * this.val[15];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  397 */     if (l_det == 0.0F) throw new RuntimeException("non-invertible matrix"); 
/*  398 */     float inv_det = 1.0F / l_det;
/*  399 */     tmp[0] = this.val[9] * this.val[14] * this.val[7] - this.val[13] * this.val[10] * this.val[7] + this.val[13] * this.val[6] * this.val[11] - this.val[5] * this.val[14] * this.val[11] - this.val[9] * this.val[6] * this.val[15] + this.val[5] * this.val[10] * this.val[15];
/*      */     
/*  401 */     tmp[4] = this.val[12] * this.val[10] * this.val[7] - this.val[8] * this.val[14] * this.val[7] - this.val[12] * this.val[6] * this.val[11] + this.val[4] * this.val[14] * this.val[11] + this.val[8] * this.val[6] * this.val[15] - this.val[4] * this.val[10] * this.val[15];
/*      */     
/*  403 */     tmp[8] = this.val[8] * this.val[13] * this.val[7] - this.val[12] * this.val[9] * this.val[7] + this.val[12] * this.val[5] * this.val[11] - this.val[4] * this.val[13] * this.val[11] - this.val[8] * this.val[5] * this.val[15] + this.val[4] * this.val[9] * this.val[15];
/*      */     
/*  405 */     tmp[12] = this.val[12] * this.val[9] * this.val[6] - this.val[8] * this.val[13] * this.val[6] - this.val[12] * this.val[5] * this.val[10] + this.val[4] * this.val[13] * this.val[10] + this.val[8] * this.val[5] * this.val[14] - this.val[4] * this.val[9] * this.val[14];
/*      */     
/*  407 */     tmp[1] = this.val[13] * this.val[10] * this.val[3] - this.val[9] * this.val[14] * this.val[3] - this.val[13] * this.val[2] * this.val[11] + this.val[1] * this.val[14] * this.val[11] + this.val[9] * this.val[2] * this.val[15] - this.val[1] * this.val[10] * this.val[15];
/*      */     
/*  409 */     tmp[5] = this.val[8] * this.val[14] * this.val[3] - this.val[12] * this.val[10] * this.val[3] + this.val[12] * this.val[2] * this.val[11] - this.val[0] * this.val[14] * this.val[11] - this.val[8] * this.val[2] * this.val[15] + this.val[0] * this.val[10] * this.val[15];
/*      */     
/*  411 */     tmp[9] = this.val[12] * this.val[9] * this.val[3] - this.val[8] * this.val[13] * this.val[3] - this.val[12] * this.val[1] * this.val[11] + this.val[0] * this.val[13] * this.val[11] + this.val[8] * this.val[1] * this.val[15] - this.val[0] * this.val[9] * this.val[15];
/*      */     
/*  413 */     tmp[13] = this.val[8] * this.val[13] * this.val[2] - this.val[12] * this.val[9] * this.val[2] + this.val[12] * this.val[1] * this.val[10] - this.val[0] * this.val[13] * this.val[10] - this.val[8] * this.val[1] * this.val[14] + this.val[0] * this.val[9] * this.val[14];
/*      */     
/*  415 */     tmp[2] = this.val[5] * this.val[14] * this.val[3] - this.val[13] * this.val[6] * this.val[3] + this.val[13] * this.val[2] * this.val[7] - this.val[1] * this.val[14] * this.val[7] - this.val[5] * this.val[2] * this.val[15] + this.val[1] * this.val[6] * this.val[15];
/*      */     
/*  417 */     tmp[6] = this.val[12] * this.val[6] * this.val[3] - this.val[4] * this.val[14] * this.val[3] - this.val[12] * this.val[2] * this.val[7] + this.val[0] * this.val[14] * this.val[7] + this.val[4] * this.val[2] * this.val[15] - this.val[0] * this.val[6] * this.val[15];
/*      */     
/*  419 */     tmp[10] = this.val[4] * this.val[13] * this.val[3] - this.val[12] * this.val[5] * this.val[3] + this.val[12] * this.val[1] * this.val[7] - this.val[0] * this.val[13] * this.val[7] - this.val[4] * this.val[1] * this.val[15] + this.val[0] * this.val[5] * this.val[15];
/*      */     
/*  421 */     tmp[14] = this.val[12] * this.val[5] * this.val[2] - this.val[4] * this.val[13] * this.val[2] - this.val[12] * this.val[1] * this.val[6] + this.val[0] * this.val[13] * this.val[6] + this.val[4] * this.val[1] * this.val[14] - this.val[0] * this.val[5] * this.val[14];
/*      */     
/*  423 */     tmp[3] = this.val[9] * this.val[6] * this.val[3] - this.val[5] * this.val[10] * this.val[3] - this.val[9] * this.val[2] * this.val[7] + this.val[1] * this.val[10] * this.val[7] + this.val[5] * this.val[2] * this.val[11] - this.val[1] * this.val[6] * this.val[11];
/*      */     
/*  425 */     tmp[7] = this.val[4] * this.val[10] * this.val[3] - this.val[8] * this.val[6] * this.val[3] + this.val[8] * this.val[2] * this.val[7] - this.val[0] * this.val[10] * this.val[7] - this.val[4] * this.val[2] * this.val[11] + this.val[0] * this.val[6] * this.val[11];
/*      */     
/*  427 */     tmp[11] = this.val[8] * this.val[5] * this.val[3] - this.val[4] * this.val[9] * this.val[3] - this.val[8] * this.val[1] * this.val[7] + this.val[0] * this.val[9] * this.val[7] + this.val[4] * this.val[1] * this.val[11] - this.val[0] * this.val[5] * this.val[11];
/*      */     
/*  429 */     tmp[15] = this.val[4] * this.val[9] * this.val[2] - this.val[8] * this.val[5] * this.val[2] + this.val[8] * this.val[1] * this.val[6] - this.val[0] * this.val[9] * this.val[6] - this.val[4] * this.val[1] * this.val[10] + this.val[0] * this.val[5] * this.val[10];
/*      */     
/*  431 */     this.val[0] = tmp[0] * inv_det;
/*  432 */     this.val[4] = tmp[4] * inv_det;
/*  433 */     this.val[8] = tmp[8] * inv_det;
/*  434 */     this.val[12] = tmp[12] * inv_det;
/*  435 */     this.val[1] = tmp[1] * inv_det;
/*  436 */     this.val[5] = tmp[5] * inv_det;
/*  437 */     this.val[9] = tmp[9] * inv_det;
/*  438 */     this.val[13] = tmp[13] * inv_det;
/*  439 */     this.val[2] = tmp[2] * inv_det;
/*  440 */     this.val[6] = tmp[6] * inv_det;
/*  441 */     this.val[10] = tmp[10] * inv_det;
/*  442 */     this.val[14] = tmp[14] * inv_det;
/*  443 */     this.val[3] = tmp[3] * inv_det;
/*  444 */     this.val[7] = tmp[7] * inv_det;
/*  445 */     this.val[11] = tmp[11] * inv_det;
/*  446 */     this.val[15] = tmp[15] * inv_det;
/*  447 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public float det() {
/*  452 */     return this.val[3] * this.val[6] * this.val[9] * this.val[12] - this.val[2] * this.val[7] * this.val[9] * this.val[12] - this.val[3] * this.val[5] * this.val[10] * this.val[12] + this.val[1] * this.val[7] * this.val[10] * this.val[12] + this.val[2] * this.val[5] * this.val[11] * this.val[12] - this.val[1] * this.val[6] * this.val[11] * this.val[12] - this.val[3] * this.val[6] * this.val[8] * this.val[13] + this.val[2] * this.val[7] * this.val[8] * this.val[13] + this.val[3] * this.val[4] * this.val[10] * this.val[13] - this.val[0] * this.val[7] * this.val[10] * this.val[13] - this.val[2] * this.val[4] * this.val[11] * this.val[13] + this.val[0] * this.val[6] * this.val[11] * this.val[13] + this.val[3] * this.val[5] * this.val[8] * this.val[14] - this.val[1] * this.val[7] * this.val[8] * this.val[14] - this.val[3] * this.val[4] * this.val[9] * this.val[14] + this.val[0] * this.val[7] * this.val[9] * this.val[14] + this.val[1] * this.val[4] * this.val[11] * this.val[14] - this.val[0] * this.val[5] * this.val[11] * this.val[14] - this.val[2] * this.val[5] * this.val[8] * this.val[15] + this.val[1] * this.val[6] * this.val[8] * this.val[15] + this.val[2] * this.val[4] * this.val[9] * this.val[15] - this.val[0] * this.val[6] * this.val[9] * this.val[15] - this.val[1] * this.val[4] * this.val[10] * this.val[15] + this.val[0] * this.val[5] * this.val[10] * this.val[15];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float det3x3() {
/*  465 */     return this.val[0] * this.val[5] * this.val[10] + this.val[4] * this.val[9] * this.val[2] + this.val[8] * this.val[1] * this.val[6] - this.val[0] * this.val[9] * this.val[6] - this.val[4] * this.val[1] * this.val[10] - this.val[8] * this.val[5] * this.val[2];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToProjection(float near, float far, float fovy, float aspectRatio) {
/*  479 */     idt();
/*  480 */     float l_fd = (float)(1.0D / Math.tan(fovy * 0.017453292519943295D / 2.0D));
/*  481 */     float l_a1 = (far + near) / (near - far);
/*  482 */     float l_a2 = 2.0F * far * near / (near - far);
/*  483 */     this.val[0] = l_fd / aspectRatio;
/*  484 */     this.val[1] = 0.0F;
/*  485 */     this.val[2] = 0.0F;
/*  486 */     this.val[3] = 0.0F;
/*  487 */     this.val[4] = 0.0F;
/*  488 */     this.val[5] = l_fd;
/*  489 */     this.val[6] = 0.0F;
/*  490 */     this.val[7] = 0.0F;
/*  491 */     this.val[8] = 0.0F;
/*  492 */     this.val[9] = 0.0F;
/*  493 */     this.val[10] = l_a1;
/*  494 */     this.val[11] = -1.0F;
/*  495 */     this.val[12] = 0.0F;
/*  496 */     this.val[13] = 0.0F;
/*  497 */     this.val[14] = l_a2;
/*  498 */     this.val[15] = 0.0F;
/*      */     
/*  500 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToProjection(float left, float right, float bottom, float top, float near, float far) {
/*  515 */     float x = 2.0F * near / (right - left);
/*  516 */     float y = 2.0F * near / (top - bottom);
/*  517 */     float a = (right + left) / (right - left);
/*  518 */     float b = (top + bottom) / (top - bottom);
/*  519 */     float l_a1 = (far + near) / (near - far);
/*  520 */     float l_a2 = 2.0F * far * near / (near - far);
/*  521 */     this.val[0] = x;
/*  522 */     this.val[1] = 0.0F;
/*  523 */     this.val[2] = 0.0F;
/*  524 */     this.val[3] = 0.0F;
/*  525 */     this.val[4] = 0.0F;
/*  526 */     this.val[5] = y;
/*  527 */     this.val[6] = 0.0F;
/*  528 */     this.val[7] = 0.0F;
/*  529 */     this.val[8] = a;
/*  530 */     this.val[9] = b;
/*  531 */     this.val[10] = l_a1;
/*  532 */     this.val[11] = -1.0F;
/*  533 */     this.val[12] = 0.0F;
/*  534 */     this.val[13] = 0.0F;
/*  535 */     this.val[14] = l_a2;
/*  536 */     this.val[15] = 0.0F;
/*      */     
/*  538 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToOrtho2D(float x, float y, float width, float height) {
/*  550 */     setToOrtho(x, x + width, y, y + height, 0.0F, 1.0F);
/*  551 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToOrtho2D(float x, float y, float width, float height, float near, float far) {
/*  565 */     setToOrtho(x, x + width, y, y + height, near, far);
/*  566 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToOrtho(float left, float right, float bottom, float top, float near, float far) {
/*  581 */     idt();
/*  582 */     float x_orth = 2.0F / (right - left);
/*  583 */     float y_orth = 2.0F / (top - bottom);
/*  584 */     float z_orth = -2.0F / (far - near);
/*      */     
/*  586 */     float tx = -(right + left) / (right - left);
/*  587 */     float ty = -(top + bottom) / (top - bottom);
/*  588 */     float tz = -(far + near) / (far - near);
/*      */     
/*  590 */     this.val[0] = x_orth;
/*  591 */     this.val[1] = 0.0F;
/*  592 */     this.val[2] = 0.0F;
/*  593 */     this.val[3] = 0.0F;
/*  594 */     this.val[4] = 0.0F;
/*  595 */     this.val[5] = y_orth;
/*  596 */     this.val[6] = 0.0F;
/*  597 */     this.val[7] = 0.0F;
/*  598 */     this.val[8] = 0.0F;
/*  599 */     this.val[9] = 0.0F;
/*  600 */     this.val[10] = z_orth;
/*  601 */     this.val[11] = 0.0F;
/*  602 */     this.val[12] = tx;
/*  603 */     this.val[13] = ty;
/*  604 */     this.val[14] = tz;
/*  605 */     this.val[15] = 1.0F;
/*      */     
/*  607 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setTranslation(Vector3 vector) {
/*  615 */     this.val[12] = vector.x;
/*  616 */     this.val[13] = vector.y;
/*  617 */     this.val[14] = vector.z;
/*  618 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setTranslation(float x, float y, float z) {
/*  628 */     this.val[12] = x;
/*  629 */     this.val[13] = y;
/*  630 */     this.val[14] = z;
/*  631 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToTranslation(Vector3 vector) {
/*  640 */     idt();
/*  641 */     this.val[12] = vector.x;
/*  642 */     this.val[13] = vector.y;
/*  643 */     this.val[14] = vector.z;
/*  644 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToTranslation(float x, float y, float z) {
/*  655 */     idt();
/*  656 */     this.val[12] = x;
/*  657 */     this.val[13] = y;
/*  658 */     this.val[14] = z;
/*  659 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToTranslationAndScaling(Vector3 translation, Vector3 scaling) {
/*  669 */     idt();
/*  670 */     this.val[12] = translation.x;
/*  671 */     this.val[13] = translation.y;
/*  672 */     this.val[14] = translation.z;
/*  673 */     this.val[0] = scaling.x;
/*  674 */     this.val[5] = scaling.y;
/*  675 */     this.val[10] = scaling.z;
/*  676 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToTranslationAndScaling(float translationX, float translationY, float translationZ, float scalingX, float scalingY, float scalingZ) {
/*  691 */     idt();
/*  692 */     this.val[12] = translationX;
/*  693 */     this.val[13] = translationY;
/*  694 */     this.val[14] = translationZ;
/*  695 */     this.val[0] = scalingX;
/*  696 */     this.val[5] = scalingY;
/*  697 */     this.val[10] = scalingZ;
/*  698 */     return this;
/*      */   }
/*      */   
/*  701 */   static Quaternion quat = new Quaternion();
/*  702 */   static Quaternion quat2 = new Quaternion();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToRotation(Vector3 axis, float degrees) {
/*  710 */     if (degrees == 0.0F) {
/*  711 */       idt();
/*  712 */       return this;
/*      */     } 
/*  714 */     return set(quat.set(axis, degrees));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToRotationRad(Vector3 axis, float radians) {
/*  723 */     if (radians == 0.0F) {
/*  724 */       idt();
/*  725 */       return this;
/*      */     } 
/*  727 */     return set(quat.setFromAxisRad(axis, radians));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToRotation(float axisX, float axisY, float axisZ, float degrees) {
/*  738 */     if (degrees == 0.0F) {
/*  739 */       idt();
/*  740 */       return this;
/*      */     } 
/*  742 */     return set(quat.setFromAxis(axisX, axisY, axisZ, degrees));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToRotationRad(float axisX, float axisY, float axisZ, float radians) {
/*  753 */     if (radians == 0.0F) {
/*  754 */       idt();
/*  755 */       return this;
/*      */     } 
/*  757 */     return set(quat.setFromAxisRad(axisX, axisY, axisZ, radians));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToRotation(Vector3 v1, Vector3 v2) {
/*  765 */     return set(quat.setFromCross(v1, v2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToRotation(float x1, float y1, float z1, float x2, float y2, float z2) {
/*  777 */     return set(quat.setFromCross(x1, y1, z1, x2, y2, z2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setFromEulerAngles(float yaw, float pitch, float roll) {
/*  786 */     quat.setEulerAngles(yaw, pitch, roll);
/*  787 */     return set(quat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setFromEulerAnglesRad(float yaw, float pitch, float roll) {
/*  796 */     quat.setEulerAnglesRad(yaw, pitch, roll);
/*  797 */     return set(quat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToScaling(Vector3 vector) {
/*  805 */     idt();
/*  806 */     this.val[0] = vector.x;
/*  807 */     this.val[5] = vector.y;
/*  808 */     this.val[10] = vector.z;
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToScaling(float x, float y, float z) {
/*  819 */     idt();
/*  820 */     this.val[0] = x;
/*  821 */     this.val[5] = y;
/*  822 */     this.val[10] = z;
/*  823 */     return this;
/*      */   }
/*      */   
/*  826 */   static final Vector3 l_vez = new Vector3();
/*  827 */   static final Vector3 l_vex = new Vector3();
/*  828 */   static final Vector3 l_vey = new Vector3();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToLookAt(Vector3 direction, Vector3 up) {
/*  837 */     l_vez.set(direction).nor();
/*  838 */     l_vex.set(direction).nor();
/*  839 */     l_vex.crs(up).nor();
/*  840 */     l_vey.set(l_vex).crs(l_vez).nor();
/*  841 */     idt();
/*  842 */     this.val[0] = l_vex.x;
/*  843 */     this.val[4] = l_vex.y;
/*  844 */     this.val[8] = l_vex.z;
/*  845 */     this.val[1] = l_vey.x;
/*  846 */     this.val[5] = l_vey.y;
/*  847 */     this.val[9] = l_vey.z;
/*  848 */     this.val[2] = -l_vez.x;
/*  849 */     this.val[6] = -l_vez.y;
/*  850 */     this.val[10] = -l_vez.z;
/*      */     
/*  852 */     return this;
/*      */   }
/*      */   
/*  855 */   static final Vector3 tmpVec = new Vector3();
/*  856 */   static final Matrix4 tmpMat = new Matrix4();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setToLookAt(Vector3 position, Vector3 target, Vector3 up) {
/*  865 */     tmpVec.set(target).sub(position);
/*  866 */     setToLookAt(tmpVec, up);
/*  867 */     mul(tmpMat.setToTranslation(-position.x, -position.y, -position.z));
/*      */     
/*  869 */     return this;
/*      */   }
/*      */   
/*  872 */   static final Vector3 right = new Vector3();
/*  873 */   static final Vector3 tmpForward = new Vector3();
/*  874 */   static final Vector3 tmpUp = new Vector3();
/*      */   
/*      */   public Matrix4 setToWorld(Vector3 position, Vector3 forward, Vector3 up) {
/*  877 */     tmpForward.set(forward).nor();
/*  878 */     right.set(tmpForward).crs(up).nor();
/*  879 */     tmpUp.set(right).crs(tmpForward).nor();
/*      */     
/*  881 */     set(right, tmpUp, tmpForward.scl(-1.0F), position);
/*  882 */     return this;
/*      */   }
/*      */   
/*      */   public String toString() {
/*  886 */     return "[" + this.val[0] + "|" + this.val[4] + "|" + this.val[8] + "|" + this.val[12] + "]\n[" + this.val[1] + "|" + this.val[5] + "|" + this.val[9] + "|" + this.val[13] + "]\n[" + this.val[2] + "|" + this.val[6] + "|" + this.val[10] + "|" + this.val[14] + "]\n[" + this.val[3] + "|" + this.val[7] + "|" + this.val[11] + "|" + this.val[15] + "]\n";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 lerp(Matrix4 matrix, float alpha) {
/*  896 */     for (int i = 0; i < 16; i++)
/*  897 */       this.val[i] = this.val[i] * (1.0F - alpha) + matrix.val[i] * alpha; 
/*  898 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 avg(Matrix4 other, float w) {
/*  907 */     getScale(tmpVec);
/*  908 */     other.getScale(tmpForward);
/*      */     
/*  910 */     getRotation(quat);
/*  911 */     other.getRotation(quat2);
/*      */     
/*  913 */     getTranslation(tmpUp);
/*  914 */     other.getTranslation(right);
/*      */     
/*  916 */     setToScaling(tmpVec.scl(w).add(tmpForward.scl(1.0F - w)));
/*  917 */     rotate(quat.slerp(quat2, 1.0F - w));
/*  918 */     setTranslation(tmpUp.scl(w).add(right.scl(1.0F - w)));
/*      */     
/*  920 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 avg(Matrix4[] t) {
/*  928 */     float w = 1.0F / t.length;
/*      */     
/*  930 */     tmpVec.set(t[0].getScale(tmpUp).scl(w));
/*  931 */     quat.set(t[0].getRotation(quat2).exp(w));
/*  932 */     tmpForward.set(t[0].getTranslation(tmpUp).scl(w));
/*      */     
/*  934 */     for (int i = 1; i < t.length; i++) {
/*  935 */       tmpVec.add(t[i].getScale(tmpUp).scl(w));
/*  936 */       quat.mul(t[i].getRotation(quat2).exp(w));
/*  937 */       tmpForward.add(t[i].getTranslation(tmpUp).scl(w));
/*      */     } 
/*  939 */     quat.nor();
/*      */     
/*  941 */     setToScaling(tmpVec);
/*  942 */     rotate(quat);
/*  943 */     setTranslation(tmpForward);
/*      */     
/*  945 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 avg(Matrix4[] t, float[] w) {
/*  955 */     tmpVec.set(t[0].getScale(tmpUp).scl(w[0]));
/*  956 */     quat.set(t[0].getRotation(quat2).exp(w[0]));
/*  957 */     tmpForward.set(t[0].getTranslation(tmpUp).scl(w[0]));
/*      */     
/*  959 */     for (int i = 1; i < t.length; i++) {
/*  960 */       tmpVec.add(t[i].getScale(tmpUp).scl(w[i]));
/*  961 */       quat.mul(t[i].getRotation(quat2).exp(w[i]));
/*  962 */       tmpForward.add(t[i].getTranslation(tmpUp).scl(w[i]));
/*      */     } 
/*  964 */     quat.nor();
/*      */     
/*  966 */     setToScaling(tmpVec);
/*  967 */     rotate(quat);
/*  968 */     setTranslation(tmpForward);
/*      */     
/*  970 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(Matrix3 mat) {
/*  976 */     this.val[0] = mat.val[0];
/*  977 */     this.val[1] = mat.val[1];
/*  978 */     this.val[2] = mat.val[2];
/*  979 */     this.val[3] = 0.0F;
/*  980 */     this.val[4] = mat.val[3];
/*  981 */     this.val[5] = mat.val[4];
/*  982 */     this.val[6] = mat.val[5];
/*  983 */     this.val[7] = 0.0F;
/*  984 */     this.val[8] = 0.0F;
/*  985 */     this.val[9] = 0.0F;
/*  986 */     this.val[10] = 1.0F;
/*  987 */     this.val[11] = 0.0F;
/*  988 */     this.val[12] = mat.val[6];
/*  989 */     this.val[13] = mat.val[7];
/*  990 */     this.val[14] = 0.0F;
/*  991 */     this.val[15] = mat.val[8];
/*  992 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 set(Affine2 affine) {
/* 1006 */     this.val[0] = affine.m00;
/* 1007 */     this.val[1] = affine.m10;
/* 1008 */     this.val[2] = 0.0F;
/* 1009 */     this.val[3] = 0.0F;
/* 1010 */     this.val[4] = affine.m01;
/* 1011 */     this.val[5] = affine.m11;
/* 1012 */     this.val[6] = 0.0F;
/* 1013 */     this.val[7] = 0.0F;
/* 1014 */     this.val[8] = 0.0F;
/* 1015 */     this.val[9] = 0.0F;
/* 1016 */     this.val[10] = 1.0F;
/* 1017 */     this.val[11] = 0.0F;
/* 1018 */     this.val[12] = affine.m02;
/* 1019 */     this.val[13] = affine.m12;
/* 1020 */     this.val[14] = 0.0F;
/* 1021 */     this.val[15] = 1.0F;
/* 1022 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setAsAffine(Affine2 affine) {
/* 1037 */     this.val[0] = affine.m00;
/* 1038 */     this.val[1] = affine.m10;
/* 1039 */     this.val[4] = affine.m01;
/* 1040 */     this.val[5] = affine.m11;
/* 1041 */     this.val[12] = affine.m02;
/* 1042 */     this.val[13] = affine.m12;
/* 1043 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 setAsAffine(Matrix4 mat) {
/* 1057 */     this.val[0] = mat.val[0];
/* 1058 */     this.val[1] = mat.val[1];
/* 1059 */     this.val[4] = mat.val[4];
/* 1060 */     this.val[5] = mat.val[5];
/* 1061 */     this.val[12] = mat.val[12];
/* 1062 */     this.val[13] = mat.val[13];
/* 1063 */     return this;
/*      */   }
/*      */   
/*      */   public Matrix4 scl(Vector3 scale) {
/* 1067 */     this.val[0] = this.val[0] * scale.x;
/* 1068 */     this.val[5] = this.val[5] * scale.y;
/* 1069 */     this.val[10] = this.val[10] * scale.z;
/* 1070 */     return this;
/*      */   }
/*      */   
/*      */   public Matrix4 scl(float x, float y, float z) {
/* 1074 */     this.val[0] = this.val[0] * x;
/* 1075 */     this.val[5] = this.val[5] * y;
/* 1076 */     this.val[10] = this.val[10] * z;
/* 1077 */     return this;
/*      */   }
/*      */   
/*      */   public Matrix4 scl(float scale) {
/* 1081 */     this.val[0] = this.val[0] * scale;
/* 1082 */     this.val[5] = this.val[5] * scale;
/* 1083 */     this.val[10] = this.val[10] * scale;
/* 1084 */     return this;
/*      */   }
/*      */   
/*      */   public Vector3 getTranslation(Vector3 position) {
/* 1088 */     position.x = this.val[12];
/* 1089 */     position.y = this.val[13];
/* 1090 */     position.z = this.val[14];
/* 1091 */     return position;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Quaternion getRotation(Quaternion rotation, boolean normalizeAxes) {
/* 1099 */     return rotation.setFromMatrix(normalizeAxes, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Quaternion getRotation(Quaternion rotation) {
/* 1106 */     return rotation.setFromMatrix(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScaleXSquared() {
/* 1111 */     return this.val[0] * this.val[0] + this.val[4] * this.val[4] + this.val[8] * this.val[8];
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScaleYSquared() {
/* 1116 */     return this.val[1] * this.val[1] + this.val[5] * this.val[5] + this.val[9] * this.val[9];
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScaleZSquared() {
/* 1121 */     return this.val[2] * this.val[2] + this.val[6] * this.val[6] + this.val[10] * this.val[10];
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScaleX() {
/* 1126 */     return (MathUtils.isZero(this.val[4]) && MathUtils.isZero(this.val[8])) ? Math.abs(this.val[0]) : 
/* 1127 */       (float)Math.sqrt(getScaleXSquared());
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScaleY() {
/* 1132 */     return (MathUtils.isZero(this.val[1]) && MathUtils.isZero(this.val[9])) ? Math.abs(this.val[5]) : 
/* 1133 */       (float)Math.sqrt(getScaleYSquared());
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScaleZ() {
/* 1138 */     return (MathUtils.isZero(this.val[2]) && MathUtils.isZero(this.val[6])) ? Math.abs(this.val[10]) : 
/* 1139 */       (float)Math.sqrt(getScaleZSquared());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector3 getScale(Vector3 scale) {
/* 1145 */     return scale.set(getScaleX(), getScaleY(), getScaleZ());
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 toNormalMatrix() {
/* 1150 */     this.val[12] = 0.0F;
/* 1151 */     this.val[13] = 0.0F;
/* 1152 */     this.val[14] = 0.0F;
/* 1153 */     return inv().tra();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native void mul(float[] paramArrayOffloat1, float[] paramArrayOffloat2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native void mulVec(float[] paramArrayOffloat1, float[] paramArrayOffloat2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native void mulVec(float[] paramArrayOffloat1, float[] paramArrayOffloat2, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native void prj(float[] paramArrayOffloat1, float[] paramArrayOffloat2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native void prj(float[] paramArrayOffloat1, float[] paramArrayOffloat2, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native void rot(float[] paramArrayOffloat1, float[] paramArrayOffloat2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native void rot(float[] paramArrayOffloat1, float[] paramArrayOffloat2, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native boolean inv(float[] paramArrayOffloat);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static native float det(float[] paramArrayOffloat);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 translate(Vector3 translation) {
/* 1416 */     return translate(translation.x, translation.y, translation.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 translate(float x, float y, float z) {
/* 1426 */     tmp[0] = 1.0F;
/* 1427 */     tmp[4] = 0.0F;
/* 1428 */     tmp[8] = 0.0F;
/* 1429 */     tmp[12] = x;
/* 1430 */     tmp[1] = 0.0F;
/* 1431 */     tmp[5] = 1.0F;
/* 1432 */     tmp[9] = 0.0F;
/* 1433 */     tmp[13] = y;
/* 1434 */     tmp[2] = 0.0F;
/* 1435 */     tmp[6] = 0.0F;
/* 1436 */     tmp[10] = 1.0F;
/* 1437 */     tmp[14] = z;
/* 1438 */     tmp[3] = 0.0F;
/* 1439 */     tmp[7] = 0.0F;
/* 1440 */     tmp[11] = 0.0F;
/* 1441 */     tmp[15] = 1.0F;
/*      */     
/* 1443 */     mul(this.val, tmp);
/* 1444 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 rotate(Vector3 axis, float degrees) {
/* 1454 */     if (degrees == 0.0F) return this; 
/* 1455 */     quat.set(axis, degrees);
/* 1456 */     return rotate(quat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 rotateRad(Vector3 axis, float radians) {
/* 1466 */     if (radians == 0.0F) return this; 
/* 1467 */     quat.setFromAxisRad(axis, radians);
/* 1468 */     return rotate(quat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 rotate(float axisX, float axisY, float axisZ, float degrees) {
/* 1479 */     if (degrees == 0.0F) return this; 
/* 1480 */     quat.setFromAxis(axisX, axisY, axisZ, degrees);
/* 1481 */     return rotate(quat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 rotateRad(float axisX, float axisY, float axisZ, float radians) {
/* 1492 */     if (radians == 0.0F) return this; 
/* 1493 */     quat.setFromAxisRad(axisX, axisY, axisZ, radians);
/* 1494 */     return rotate(quat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 rotate(Quaternion rotation) {
/* 1503 */     rotation.toMatrix(tmp);
/* 1504 */     mul(this.val, tmp);
/* 1505 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 rotate(Vector3 v1, Vector3 v2) {
/* 1513 */     return rotate(quat.setFromCross(v1, v2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix4 scale(float scaleX, float scaleY, float scaleZ) {
/* 1523 */     tmp[0] = scaleX;
/* 1524 */     tmp[4] = 0.0F;
/* 1525 */     tmp[8] = 0.0F;
/* 1526 */     tmp[12] = 0.0F;
/* 1527 */     tmp[1] = 0.0F;
/* 1528 */     tmp[5] = scaleY;
/* 1529 */     tmp[9] = 0.0F;
/* 1530 */     tmp[13] = 0.0F;
/* 1531 */     tmp[2] = 0.0F;
/* 1532 */     tmp[6] = 0.0F;
/* 1533 */     tmp[10] = scaleZ;
/* 1534 */     tmp[14] = 0.0F;
/* 1535 */     tmp[3] = 0.0F;
/* 1536 */     tmp[7] = 0.0F;
/* 1537 */     tmp[11] = 0.0F;
/* 1538 */     tmp[15] = 1.0F;
/*      */     
/* 1540 */     mul(this.val, tmp);
/* 1541 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void extract4x3Matrix(float[] dst) {
/* 1547 */     dst[0] = this.val[0];
/* 1548 */     dst[1] = this.val[1];
/* 1549 */     dst[2] = this.val[2];
/* 1550 */     dst[3] = this.val[4];
/* 1551 */     dst[4] = this.val[5];
/* 1552 */     dst[5] = this.val[6];
/* 1553 */     dst[6] = this.val[8];
/* 1554 */     dst[7] = this.val[9];
/* 1555 */     dst[8] = this.val[10];
/* 1556 */     dst[9] = this.val[12];
/* 1557 */     dst[10] = this.val[13];
/* 1558 */     dst[11] = this.val[14];
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasRotationOrScaling() {
/* 1563 */     return (!MathUtils.isEqual(this.val[0], 1.0F) || !MathUtils.isEqual(this.val[5], 1.0F) || !MathUtils.isEqual(this.val[10], 1.0F) || 
/* 1564 */       !MathUtils.isZero(this.val[4]) || !MathUtils.isZero(this.val[8]) || !MathUtils.isZero(this.val[1]) || !MathUtils.isZero(this.val[9]) || 
/* 1565 */       !MathUtils.isZero(this.val[2]) || !MathUtils.isZero(this.val[6]));
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Matrix4.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */