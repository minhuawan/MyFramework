/*     */ package com.badlogic.gdx.graphics.g3d.decals;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ 
/*     */ 
/*     */ public class Decal
/*     */ {
/*     */   private static final int VERTEX_SIZE = 6;
/*     */   public static final int SIZE = 24;
/*  41 */   private static Vector3 tmp = new Vector3();
/*  42 */   private static Vector3 tmp2 = new Vector3();
/*     */ 
/*     */   
/*     */   public int value;
/*     */   
/*  47 */   protected float[] vertices = new float[24];
/*  48 */   protected Vector3 position = new Vector3();
/*  49 */   protected Quaternion rotation = new Quaternion();
/*  50 */   protected Vector2 scale = new Vector2(1.0F, 1.0F);
/*  51 */   protected Color color = new Color();
/*     */ 
/*     */ 
/*     */   
/*  55 */   public Vector2 transformationOffset = null;
/*  56 */   protected Vector2 dimensions = new Vector2();
/*     */   
/*     */   protected DecalMaterial material;
/*     */   protected boolean updated = false;
/*     */   
/*     */   public Decal() {
/*  62 */     this.material = new DecalMaterial();
/*     */   }
/*     */   
/*     */   public Decal(DecalMaterial material) {
/*  66 */     this.material = material;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/*  76 */     this.color.set(r, g, b, a);
/*  77 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/*  78 */     float color = NumberUtils.intToFloatColor(intBits);
/*  79 */     this.vertices[3] = color;
/*  80 */     this.vertices[9] = color;
/*  81 */     this.vertices[15] = color;
/*  82 */     this.vertices[21] = color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(Color tint) {
/*  87 */     this.color.set(tint);
/*  88 */     float color = tint.toFloatBits();
/*  89 */     this.vertices[3] = color;
/*  90 */     this.vertices[9] = color;
/*  91 */     this.vertices[15] = color;
/*  92 */     this.vertices[21] = color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(float color) {
/*  97 */     this.color.set(NumberUtils.floatToIntColor(color));
/*  98 */     this.vertices[3] = color;
/*  99 */     this.vertices[9] = color;
/* 100 */     this.vertices[15] = color;
/* 101 */     this.vertices[21] = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotationX(float angle) {
/* 108 */     this.rotation.set(Vector3.X, angle);
/* 109 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotationY(float angle) {
/* 116 */     this.rotation.set(Vector3.Y, angle);
/* 117 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotationZ(float angle) {
/* 124 */     this.rotation.set(Vector3.Z, angle);
/* 125 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotateX(float angle) {
/* 132 */     rotator.set(Vector3.X, angle);
/* 133 */     this.rotation.mul(rotator);
/* 134 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotateY(float angle) {
/* 141 */     rotator.set(Vector3.Y, angle);
/* 142 */     this.rotation.mul(rotator);
/* 143 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotateZ(float angle) {
/* 150 */     rotator.set(Vector3.Z, angle);
/* 151 */     this.rotation.mul(rotator);
/* 152 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(float yaw, float pitch, float roll) {
/* 160 */     this.rotation.setEulerAngles(yaw, pitch, roll);
/* 161 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(Vector3 dir, Vector3 up) {
/* 168 */     tmp.set(up).crs(dir).nor();
/* 169 */     tmp2.set(dir).crs(tmp).nor();
/* 170 */     this.rotation.setFromAxes(tmp.x, tmp2.x, dir.x, tmp.y, tmp2.y, dir.y, tmp.z, tmp2.z, dir.z);
/* 171 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(Quaternion q) {
/* 177 */     this.rotation.set(q);
/* 178 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quaternion getRotation() {
/* 185 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void translateX(float units) {
/* 192 */     this.position.x += units;
/* 193 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(float x) {
/* 200 */     this.position.x = x;
/* 201 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/* 206 */     return this.position.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void translateY(float units) {
/* 213 */     this.position.y += units;
/* 214 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(float y) {
/* 221 */     this.position.y = y;
/* 222 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 227 */     return this.position.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void translateZ(float units) {
/* 234 */     this.position.z += units;
/* 235 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZ(float z) {
/* 242 */     this.position.z = z;
/* 243 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getZ() {
/* 248 */     return this.position.z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void translate(float x, float y, float z) {
/* 257 */     this.position.add(x, y, z);
/* 258 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translate(Vector3 trans) {
/* 263 */     this.position.add(trans);
/* 264 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y, float z) {
/* 273 */     this.position.set(x, y, z);
/* 274 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosition(Vector3 pos) {
/* 279 */     this.position.set(pos);
/* 280 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 287 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 getPosition() {
/* 294 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleX(float scale) {
/* 301 */     this.scale.x = scale;
/* 302 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleX() {
/* 307 */     return this.scale.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleY(float scale) {
/* 314 */     this.scale.y = scale;
/* 315 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleY() {
/* 320 */     return this.scale.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScale(float scaleX, float scaleY) {
/* 328 */     this.scale.set(scaleX, scaleY);
/* 329 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScale(float scale) {
/* 336 */     this.scale.set(scale, scale);
/* 337 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWidth(float width) {
/* 344 */     this.dimensions.x = width;
/* 345 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 350 */     return this.dimensions.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeight(float height) {
/* 357 */     this.dimensions.y = height;
/* 358 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 363 */     return this.dimensions.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDimensions(float width, float height) {
/* 371 */     this.dimensions.set(width, height);
/* 372 */     this.updated = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getVertices() {
/* 380 */     return this.vertices;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void update() {
/* 385 */     if (!this.updated) {
/* 386 */       resetVertices();
/* 387 */       transformVertices();
/*     */     } 
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
/*     */   protected void transformVertices() {
/*     */     float tx, ty;
/* 403 */     if (this.transformationOffset != null) {
/* 404 */       tx = -this.transformationOffset.x;
/* 405 */       ty = -this.transformationOffset.y;
/*     */     } else {
/* 407 */       tx = ty = 0.0F;
/*     */     } 
/*     */ 
/*     */     
/* 411 */     float x = (this.vertices[0] + tx) * this.scale.x;
/* 412 */     float y = (this.vertices[1] + ty) * this.scale.y;
/* 413 */     float z = this.vertices[2];
/*     */     
/* 415 */     this.vertices[0] = this.rotation.w * x + this.rotation.y * z - this.rotation.z * y;
/* 416 */     this.vertices[1] = this.rotation.w * y + this.rotation.z * x - this.rotation.x * z;
/* 417 */     this.vertices[2] = this.rotation.w * z + this.rotation.x * y - this.rotation.y * x;
/* 418 */     float w = -this.rotation.x * x - this.rotation.y * y - this.rotation.z * z;
/* 419 */     this.rotation.conjugate();
/* 420 */     x = this.vertices[0];
/* 421 */     y = this.vertices[1];
/* 422 */     z = this.vertices[2];
/* 423 */     this.vertices[0] = w * this.rotation.x + x * this.rotation.w + y * this.rotation.z - z * this.rotation.y;
/* 424 */     this.vertices[1] = w * this.rotation.y + y * this.rotation.w + z * this.rotation.x - x * this.rotation.z;
/* 425 */     this.vertices[2] = w * this.rotation.z + z * this.rotation.w + x * this.rotation.y - y * this.rotation.x;
/* 426 */     this.rotation.conjugate();
/*     */     
/* 428 */     this.vertices[0] = this.vertices[0] + this.position.x - tx;
/* 429 */     this.vertices[1] = this.vertices[1] + this.position.y - ty;
/* 430 */     this.vertices[2] = this.vertices[2] + this.position.z;
/*     */ 
/*     */     
/* 433 */     x = (this.vertices[6] + tx) * this.scale.x;
/* 434 */     y = (this.vertices[7] + ty) * this.scale.y;
/* 435 */     z = this.vertices[8];
/*     */     
/* 437 */     this.vertices[6] = this.rotation.w * x + this.rotation.y * z - this.rotation.z * y;
/* 438 */     this.vertices[7] = this.rotation.w * y + this.rotation.z * x - this.rotation.x * z;
/* 439 */     this.vertices[8] = this.rotation.w * z + this.rotation.x * y - this.rotation.y * x;
/* 440 */     w = -this.rotation.x * x - this.rotation.y * y - this.rotation.z * z;
/* 441 */     this.rotation.conjugate();
/* 442 */     x = this.vertices[6];
/* 443 */     y = this.vertices[7];
/* 444 */     z = this.vertices[8];
/* 445 */     this.vertices[6] = w * this.rotation.x + x * this.rotation.w + y * this.rotation.z - z * this.rotation.y;
/* 446 */     this.vertices[7] = w * this.rotation.y + y * this.rotation.w + z * this.rotation.x - x * this.rotation.z;
/* 447 */     this.vertices[8] = w * this.rotation.z + z * this.rotation.w + x * this.rotation.y - y * this.rotation.x;
/* 448 */     this.rotation.conjugate();
/*     */     
/* 450 */     this.vertices[6] = this.vertices[6] + this.position.x - tx;
/* 451 */     this.vertices[7] = this.vertices[7] + this.position.y - ty;
/* 452 */     this.vertices[8] = this.vertices[8] + this.position.z;
/*     */ 
/*     */     
/* 455 */     x = (this.vertices[12] + tx) * this.scale.x;
/* 456 */     y = (this.vertices[13] + ty) * this.scale.y;
/* 457 */     z = this.vertices[14];
/*     */     
/* 459 */     this.vertices[12] = this.rotation.w * x + this.rotation.y * z - this.rotation.z * y;
/* 460 */     this.vertices[13] = this.rotation.w * y + this.rotation.z * x - this.rotation.x * z;
/* 461 */     this.vertices[14] = this.rotation.w * z + this.rotation.x * y - this.rotation.y * x;
/* 462 */     w = -this.rotation.x * x - this.rotation.y * y - this.rotation.z * z;
/* 463 */     this.rotation.conjugate();
/* 464 */     x = this.vertices[12];
/* 465 */     y = this.vertices[13];
/* 466 */     z = this.vertices[14];
/* 467 */     this.vertices[12] = w * this.rotation.x + x * this.rotation.w + y * this.rotation.z - z * this.rotation.y;
/* 468 */     this.vertices[13] = w * this.rotation.y + y * this.rotation.w + z * this.rotation.x - x * this.rotation.z;
/* 469 */     this.vertices[14] = w * this.rotation.z + z * this.rotation.w + x * this.rotation.y - y * this.rotation.x;
/* 470 */     this.rotation.conjugate();
/*     */     
/* 472 */     this.vertices[12] = this.vertices[12] + this.position.x - tx;
/* 473 */     this.vertices[13] = this.vertices[13] + this.position.y - ty;
/* 474 */     this.vertices[14] = this.vertices[14] + this.position.z;
/*     */ 
/*     */     
/* 477 */     x = (this.vertices[18] + tx) * this.scale.x;
/* 478 */     y = (this.vertices[19] + ty) * this.scale.y;
/* 479 */     z = this.vertices[20];
/*     */     
/* 481 */     this.vertices[18] = this.rotation.w * x + this.rotation.y * z - this.rotation.z * y;
/* 482 */     this.vertices[19] = this.rotation.w * y + this.rotation.z * x - this.rotation.x * z;
/* 483 */     this.vertices[20] = this.rotation.w * z + this.rotation.x * y - this.rotation.y * x;
/* 484 */     w = -this.rotation.x * x - this.rotation.y * y - this.rotation.z * z;
/* 485 */     this.rotation.conjugate();
/* 486 */     x = this.vertices[18];
/* 487 */     y = this.vertices[19];
/* 488 */     z = this.vertices[20];
/* 489 */     this.vertices[18] = w * this.rotation.x + x * this.rotation.w + y * this.rotation.z - z * this.rotation.y;
/* 490 */     this.vertices[19] = w * this.rotation.y + y * this.rotation.w + z * this.rotation.x - x * this.rotation.z;
/* 491 */     this.vertices[20] = w * this.rotation.z + z * this.rotation.w + x * this.rotation.y - y * this.rotation.x;
/* 492 */     this.rotation.conjugate();
/*     */     
/* 494 */     this.vertices[18] = this.vertices[18] + this.position.x - tx;
/* 495 */     this.vertices[19] = this.vertices[19] + this.position.y - ty;
/* 496 */     this.vertices[20] = this.vertices[20] + this.position.z;
/* 497 */     this.updated = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resetVertices() {
/* 502 */     float left = -this.dimensions.x / 2.0F;
/* 503 */     float right = left + this.dimensions.x;
/* 504 */     float top = this.dimensions.y / 2.0F;
/* 505 */     float bottom = top - this.dimensions.y;
/*     */ 
/*     */     
/* 508 */     this.vertices[0] = left;
/* 509 */     this.vertices[1] = top;
/* 510 */     this.vertices[2] = 0.0F;
/*     */     
/* 512 */     this.vertices[6] = right;
/* 513 */     this.vertices[7] = top;
/* 514 */     this.vertices[8] = 0.0F;
/*     */     
/* 516 */     this.vertices[12] = left;
/* 517 */     this.vertices[13] = bottom;
/* 518 */     this.vertices[14] = 0.0F;
/*     */     
/* 520 */     this.vertices[18] = right;
/* 521 */     this.vertices[19] = bottom;
/* 522 */     this.vertices[20] = 0.0F;
/*     */     
/* 524 */     this.updated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateUVs() {
/* 529 */     TextureRegion tr = this.material.textureRegion;
/*     */     
/* 531 */     this.vertices[4] = tr.getU();
/* 532 */     this.vertices[5] = tr.getV();
/*     */     
/* 534 */     this.vertices[10] = tr.getU2();
/* 535 */     this.vertices[11] = tr.getV();
/*     */     
/* 537 */     this.vertices[16] = tr.getU();
/* 538 */     this.vertices[17] = tr.getV2();
/*     */     
/* 540 */     this.vertices[22] = tr.getU2();
/* 541 */     this.vertices[23] = tr.getV2();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextureRegion(TextureRegion textureRegion) {
/* 548 */     this.material.textureRegion = textureRegion;
/* 549 */     updateUVs();
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureRegion getTextureRegion() {
/* 554 */     return this.material.textureRegion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlending(int srcBlendFactor, int dstBlendFactor) {
/* 562 */     this.material.srcBlendFactor = srcBlendFactor;
/* 563 */     this.material.dstBlendFactor = dstBlendFactor;
/*     */   }
/*     */   
/*     */   public DecalMaterial getMaterial() {
/* 567 */     return this.material;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaterial(DecalMaterial material) {
/* 575 */     this.material = material;
/*     */   }
/*     */   
/* 578 */   static final Vector3 dir = new Vector3();
/*     */   public static final int X1 = 0;
/*     */   public static final int Y1 = 1;
/*     */   public static final int Z1 = 2;
/*     */   
/*     */   public void lookAt(Vector3 position, Vector3 up) {
/* 584 */     dir.set(position).sub(this.position).nor();
/* 585 */     setRotation(dir, up);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int C1 = 3;
/*     */   
/*     */   public static final int U1 = 4;
/*     */   
/*     */   public static final int V1 = 5;
/*     */   
/*     */   public static final int X2 = 6;
/*     */   
/*     */   public static final int Y2 = 7;
/*     */   public static final int Z2 = 8;
/*     */   public static final int C2 = 9;
/*     */   public static final int U2 = 10;
/*     */   public static final int V2 = 11;
/*     */   public static final int X3 = 12;
/*     */   public static final int Y3 = 13;
/*     */   public static final int Z3 = 14;
/*     */   public static final int C3 = 15;
/*     */   public static final int U3 = 16;
/*     */   public static final int V3 = 17;
/*     */   public static final int X4 = 18;
/*     */   public static final int Y4 = 19;
/*     */   public static final int Z4 = 20;
/*     */   public static final int C4 = 21;
/*     */   public static final int U4 = 22;
/*     */   public static final int V4 = 23;
/* 614 */   protected static Quaternion rotator = new Quaternion(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Decal newDecal(TextureRegion textureRegion) {
/* 621 */     return newDecal(textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), textureRegion, -1, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Decal newDecal(TextureRegion textureRegion, boolean hasTransparency) {
/* 631 */     return newDecal(textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), textureRegion, hasTransparency ? 770 : -1, hasTransparency ? 771 : -1);
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
/*     */   public static Decal newDecal(float width, float height, TextureRegion textureRegion) {
/* 645 */     return newDecal(width, height, textureRegion, -1, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Decal newDecal(float width, float height, TextureRegion textureRegion, boolean hasTransparency) {
/* 656 */     return newDecal(width, height, textureRegion, hasTransparency ? 770 : -1, hasTransparency ? 771 : -1);
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
/*     */   public static Decal newDecal(float width, float height, TextureRegion textureRegion, int srcBlendFactor, int dstBlendFactor) {
/* 669 */     Decal decal = new Decal();
/* 670 */     decal.setTextureRegion(textureRegion);
/* 671 */     decal.setBlending(srcBlendFactor, dstBlendFactor);
/* 672 */     decal.dimensions.x = width;
/* 673 */     decal.dimensions.y = height;
/* 674 */     decal.setColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 675 */     return decal;
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
/*     */   public static Decal newDecal(float width, float height, TextureRegion textureRegion, int srcBlendFactor, int dstBlendFactor, DecalMaterial material) {
/* 689 */     Decal decal = new Decal(material);
/* 690 */     decal.setTextureRegion(textureRegion);
/* 691 */     decal.setBlending(srcBlendFactor, dstBlendFactor);
/* 692 */     decal.dimensions.x = width;
/* 693 */     decal.dimensions.y = height;
/* 694 */     decal.setColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 695 */     return decal;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\decals\Decal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */