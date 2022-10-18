/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.math.MathUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NinePatch
/*     */ {
/*     */   public static final int TOP_LEFT = 0;
/*     */   public static final int TOP_CENTER = 1;
/*     */   public static final int TOP_RIGHT = 2;
/*     */   public static final int MIDDLE_LEFT = 3;
/*     */   public static final int MIDDLE_CENTER = 4;
/*     */   public static final int MIDDLE_RIGHT = 5;
/*     */   public static final int BOTTOM_LEFT = 6;
/*     */   public static final int BOTTOM_CENTER = 7;
/*     */   public static final int BOTTOM_RIGHT = 8;
/*  50 */   private static final Color tmpDrawColor = new Color();
/*     */   
/*     */   private Texture texture;
/*  53 */   private int bottomLeft = -1, bottomCenter = -1, bottomRight = -1;
/*  54 */   private int middleLeft = -1, middleCenter = -1, middleRight = -1;
/*  55 */   private int topLeft = -1; private int topCenter = -1; private int topRight = -1; private float leftWidth; private float rightWidth;
/*     */   private float middleWidth;
/*  57 */   private float[] vertices = new float[180]; private float middleHeight; private float topHeight; private float bottomHeight;
/*     */   private int idx;
/*  59 */   private final Color color = new Color(Color.WHITE);
/*  60 */   private float padLeft = -1.0F, padRight = -1.0F, padTop = -1.0F, padBottom = -1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NinePatch(Texture texture, int left, int right, int top, int bottom) {
/*  70 */     this(new TextureRegion(texture), left, right, top, bottom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NinePatch(TextureRegion region, int left, int right, int top, int bottom) {
/*  81 */     if (region == null) throw new IllegalArgumentException("region cannot be null."); 
/*  82 */     int middleWidth = region.getRegionWidth() - left - right;
/*  83 */     int middleHeight = region.getRegionHeight() - top - bottom;
/*     */     
/*  85 */     TextureRegion[] patches = new TextureRegion[9];
/*  86 */     if (top > 0) {
/*  87 */       if (left > 0) patches[0] = new TextureRegion(region, 0, 0, left, top); 
/*  88 */       if (middleWidth > 0) patches[1] = new TextureRegion(region, left, 0, middleWidth, top); 
/*  89 */       if (right > 0) patches[2] = new TextureRegion(region, left + middleWidth, 0, right, top); 
/*     */     } 
/*  91 */     if (middleHeight > 0) {
/*  92 */       if (left > 0) patches[3] = new TextureRegion(region, 0, top, left, middleHeight); 
/*  93 */       if (middleWidth > 0) patches[4] = new TextureRegion(region, left, top, middleWidth, middleHeight); 
/*  94 */       if (right > 0) patches[5] = new TextureRegion(region, left + middleWidth, top, right, middleHeight); 
/*     */     } 
/*  96 */     if (bottom > 0) {
/*  97 */       if (left > 0) patches[6] = new TextureRegion(region, 0, top + middleHeight, left, bottom); 
/*  98 */       if (middleWidth > 0) patches[7] = new TextureRegion(region, left, top + middleHeight, middleWidth, bottom); 
/*  99 */       if (right > 0) patches[8] = new TextureRegion(region, left + middleWidth, top + middleHeight, right, bottom);
/*     */     
/*     */     } 
/*     */     
/* 103 */     if (left == 0 && middleWidth == 0) {
/* 104 */       patches[1] = patches[2];
/* 105 */       patches[4] = patches[5];
/* 106 */       patches[7] = patches[8];
/* 107 */       patches[2] = null;
/* 108 */       patches[5] = null;
/* 109 */       patches[8] = null;
/*     */     } 
/*     */     
/* 112 */     if (top == 0 && middleHeight == 0) {
/* 113 */       patches[3] = patches[6];
/* 114 */       patches[4] = patches[7];
/* 115 */       patches[5] = patches[8];
/* 116 */       patches[6] = null;
/* 117 */       patches[7] = null;
/* 118 */       patches[8] = null;
/*     */     } 
/*     */     
/* 121 */     load(patches);
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(Texture texture, Color color) {
/* 126 */     this(texture);
/* 127 */     setColor(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(Texture texture) {
/* 132 */     this(new TextureRegion(texture));
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(TextureRegion region, Color color) {
/* 137 */     this(region);
/* 138 */     setColor(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public NinePatch(TextureRegion region) {
/* 143 */     load(new TextureRegion[] { null, null, null, null, region, null, null, null, null });
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
/*     */   public NinePatch(TextureRegion... patches) {
/* 155 */     if (patches == null || patches.length != 9) throw new IllegalArgumentException("NinePatch needs nine TextureRegions");
/*     */     
/* 157 */     load(patches);
/*     */     
/* 159 */     float leftWidth = getLeftWidth();
/* 160 */     if ((patches[0] != null && patches[0].getRegionWidth() != leftWidth) || (patches[3] != null && patches[3]
/* 161 */       .getRegionWidth() != leftWidth) || (patches[6] != null && patches[6]
/* 162 */       .getRegionWidth() != leftWidth)) {
/* 163 */       throw new GdxRuntimeException("Left side patches must have the same width");
/*     */     }
/*     */     
/* 166 */     float rightWidth = getRightWidth();
/* 167 */     if ((patches[2] != null && patches[2].getRegionWidth() != rightWidth) || (patches[5] != null && patches[5]
/* 168 */       .getRegionWidth() != rightWidth) || (patches[8] != null && patches[8]
/* 169 */       .getRegionWidth() != rightWidth)) {
/* 170 */       throw new GdxRuntimeException("Right side patches must have the same width");
/*     */     }
/*     */     
/* 173 */     float bottomHeight = getBottomHeight();
/* 174 */     if ((patches[6] != null && patches[6].getRegionHeight() != bottomHeight) || (patches[7] != null && patches[7]
/* 175 */       .getRegionHeight() != bottomHeight) || (patches[8] != null && patches[8]
/* 176 */       .getRegionHeight() != bottomHeight)) {
/* 177 */       throw new GdxRuntimeException("Bottom side patches must have the same height");
/*     */     }
/*     */     
/* 180 */     float topHeight = getTopHeight();
/* 181 */     if ((patches[0] != null && patches[0].getRegionHeight() != topHeight) || (patches[1] != null && patches[1]
/* 182 */       .getRegionHeight() != topHeight) || (patches[2] != null && patches[2]
/* 183 */       .getRegionHeight() != topHeight)) {
/* 184 */       throw new GdxRuntimeException("Top side patches must have the same height");
/*     */     }
/*     */   }
/*     */   
/*     */   public NinePatch(NinePatch ninePatch) {
/* 189 */     this(ninePatch, ninePatch.color);
/*     */   }
/*     */   
/*     */   public NinePatch(NinePatch ninePatch, Color color) {
/* 193 */     this.texture = ninePatch.texture;
/*     */     
/* 195 */     this.bottomLeft = ninePatch.bottomLeft;
/* 196 */     this.bottomCenter = ninePatch.bottomCenter;
/* 197 */     this.bottomRight = ninePatch.bottomRight;
/* 198 */     this.middleLeft = ninePatch.middleLeft;
/* 199 */     this.middleCenter = ninePatch.middleCenter;
/* 200 */     this.middleRight = ninePatch.middleRight;
/* 201 */     this.topLeft = ninePatch.topLeft;
/* 202 */     this.topCenter = ninePatch.topCenter;
/* 203 */     this.topRight = ninePatch.topRight;
/*     */     
/* 205 */     this.leftWidth = ninePatch.leftWidth;
/* 206 */     this.rightWidth = ninePatch.rightWidth;
/* 207 */     this.middleWidth = ninePatch.middleWidth;
/* 208 */     this.middleHeight = ninePatch.middleHeight;
/* 209 */     this.topHeight = ninePatch.topHeight;
/* 210 */     this.bottomHeight = ninePatch.bottomHeight;
/*     */     
/* 212 */     this.padLeft = ninePatch.padLeft;
/* 213 */     this.padTop = ninePatch.padTop;
/* 214 */     this.padBottom = ninePatch.padBottom;
/* 215 */     this.padRight = ninePatch.padRight;
/*     */     
/* 217 */     this.vertices = new float[ninePatch.vertices.length];
/* 218 */     System.arraycopy(ninePatch.vertices, 0, this.vertices, 0, ninePatch.vertices.length);
/* 219 */     this.idx = ninePatch.idx;
/* 220 */     this.color.set(color);
/*     */   }
/*     */   
/*     */   private void load(TextureRegion[] patches) {
/* 224 */     float color = Color.WHITE.toFloatBits();
/*     */     
/* 226 */     if (patches[6] != null) {
/* 227 */       this.bottomLeft = add(patches[6], color, false, false);
/* 228 */       this.leftWidth = patches[6].getRegionWidth();
/* 229 */       this.bottomHeight = patches[6].getRegionHeight();
/*     */     } 
/* 231 */     if (patches[7] != null) {
/* 232 */       this.bottomCenter = add(patches[7], color, true, false);
/* 233 */       this.middleWidth = Math.max(this.middleWidth, patches[7].getRegionWidth());
/* 234 */       this.bottomHeight = Math.max(this.bottomHeight, patches[7].getRegionHeight());
/*     */     } 
/* 236 */     if (patches[8] != null) {
/* 237 */       this.bottomRight = add(patches[8], color, false, false);
/* 238 */       this.rightWidth = Math.max(this.rightWidth, patches[8].getRegionWidth());
/* 239 */       this.bottomHeight = Math.max(this.bottomHeight, patches[8].getRegionHeight());
/*     */     } 
/* 241 */     if (patches[3] != null) {
/* 242 */       this.middleLeft = add(patches[3], color, false, true);
/* 243 */       this.leftWidth = Math.max(this.leftWidth, patches[3].getRegionWidth());
/* 244 */       this.middleHeight = Math.max(this.middleHeight, patches[3].getRegionHeight());
/*     */     } 
/* 246 */     if (patches[4] != null) {
/* 247 */       this.middleCenter = add(patches[4], color, true, true);
/* 248 */       this.middleWidth = Math.max(this.middleWidth, patches[4].getRegionWidth());
/* 249 */       this.middleHeight = Math.max(this.middleHeight, patches[4].getRegionHeight());
/*     */     } 
/* 251 */     if (patches[5] != null) {
/* 252 */       this.middleRight = add(patches[5], color, false, true);
/* 253 */       this.rightWidth = Math.max(this.rightWidth, patches[5].getRegionWidth());
/* 254 */       this.middleHeight = Math.max(this.middleHeight, patches[5].getRegionHeight());
/*     */     } 
/* 256 */     if (patches[0] != null) {
/* 257 */       this.topLeft = add(patches[0], color, false, false);
/* 258 */       this.leftWidth = Math.max(this.leftWidth, patches[0].getRegionWidth());
/* 259 */       this.topHeight = Math.max(this.topHeight, patches[0].getRegionHeight());
/*     */     } 
/* 261 */     if (patches[1] != null) {
/* 262 */       this.topCenter = add(patches[1], color, true, false);
/* 263 */       this.middleWidth = Math.max(this.middleWidth, patches[1].getRegionWidth());
/* 264 */       this.topHeight = Math.max(this.topHeight, patches[1].getRegionHeight());
/*     */     } 
/* 266 */     if (patches[2] != null) {
/* 267 */       this.topRight = add(patches[2], color, false, false);
/* 268 */       this.rightWidth = Math.max(this.rightWidth, patches[2].getRegionWidth());
/* 269 */       this.topHeight = Math.max(this.topHeight, patches[2].getRegionHeight());
/*     */     } 
/* 271 */     if (this.idx < this.vertices.length) {
/* 272 */       float[] newVertices = new float[this.idx];
/* 273 */       System.arraycopy(this.vertices, 0, newVertices, 0, this.idx);
/* 274 */       this.vertices = newVertices;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int add(TextureRegion region, float color, boolean isStretchW, boolean isStretchH) {
/* 279 */     if (this.texture == null) {
/* 280 */       this.texture = region.getTexture();
/* 281 */     } else if (this.texture != region.getTexture()) {
/* 282 */       throw new IllegalArgumentException("All regions must be from the same texture.");
/*     */     } 
/* 284 */     float u = region.u;
/* 285 */     float v = region.v2;
/* 286 */     float u2 = region.u2;
/* 287 */     float v2 = region.v;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     if (isStretchW) {
/* 293 */       float halfTexelWidth = 0.5F / this.texture.getWidth();
/* 294 */       u += halfTexelWidth;
/* 295 */       u2 -= halfTexelWidth;
/*     */     } 
/* 297 */     if (isStretchH) {
/* 298 */       float halfTexelHeight = 0.5F / this.texture.getHeight();
/* 299 */       v -= halfTexelHeight;
/* 300 */       v2 += halfTexelHeight;
/*     */     } 
/*     */     
/* 303 */     float[] vertices = this.vertices;
/*     */     
/* 305 */     vertices[this.idx + 2] = color;
/* 306 */     vertices[this.idx + 3] = u;
/* 307 */     vertices[this.idx + 4] = v;
/*     */     
/* 309 */     vertices[this.idx + 7] = color;
/* 310 */     vertices[this.idx + 8] = u;
/* 311 */     vertices[this.idx + 9] = v2;
/*     */     
/* 313 */     vertices[this.idx + 12] = color;
/* 314 */     vertices[this.idx + 13] = u2;
/* 315 */     vertices[this.idx + 14] = v2;
/*     */     
/* 317 */     vertices[this.idx + 17] = color;
/* 318 */     vertices[this.idx + 18] = u2;
/* 319 */     vertices[this.idx + 19] = v;
/* 320 */     this.idx += 20;
/*     */     
/* 322 */     return this.idx - 20;
/*     */   }
/*     */ 
/*     */   
/*     */   private void set(int idx, float x, float y, float width, float height, float color) {
/* 327 */     float fx2 = x + width;
/* 328 */     float fy2 = y + height;
/* 329 */     float[] vertices = this.vertices;
/* 330 */     vertices[idx] = x;
/* 331 */     vertices[idx + 1] = y;
/* 332 */     vertices[idx + 2] = color;
/*     */     
/* 334 */     vertices[idx + 5] = x;
/* 335 */     vertices[idx + 6] = fy2;
/* 336 */     vertices[idx + 7] = color;
/*     */     
/* 338 */     vertices[idx + 10] = fx2;
/* 339 */     vertices[idx + 11] = fy2;
/* 340 */     vertices[idx + 12] = color;
/*     */     
/* 342 */     vertices[idx + 15] = fx2;
/* 343 */     vertices[idx + 16] = y;
/* 344 */     vertices[idx + 17] = color;
/*     */   }
/*     */   
/*     */   private void prepareVertices(Batch batch, float x, float y, float width, float height) {
/* 348 */     float centerColumnX = x + this.leftWidth;
/* 349 */     float rightColumnX = x + width - this.rightWidth;
/* 350 */     float middleRowY = y + this.bottomHeight;
/* 351 */     float topRowY = y + height - this.topHeight;
/* 352 */     float c = tmpDrawColor.set(this.color).mul(batch.getColor()).toFloatBits();
/*     */     
/* 354 */     if (this.bottomLeft != -1) set(this.bottomLeft, x, y, centerColumnX - x, middleRowY - y, c); 
/* 355 */     if (this.bottomCenter != -1) set(this.bottomCenter, centerColumnX, y, rightColumnX - centerColumnX, middleRowY - y, c); 
/* 356 */     if (this.bottomRight != -1) set(this.bottomRight, rightColumnX, y, x + width - rightColumnX, middleRowY - y, c); 
/* 357 */     if (this.middleLeft != -1) set(this.middleLeft, x, middleRowY, centerColumnX - x, topRowY - middleRowY, c); 
/* 358 */     if (this.middleCenter != -1) set(this.middleCenter, centerColumnX, middleRowY, rightColumnX - centerColumnX, topRowY - middleRowY, c); 
/* 359 */     if (this.middleRight != -1) set(this.middleRight, rightColumnX, middleRowY, x + width - rightColumnX, topRowY - middleRowY, c); 
/* 360 */     if (this.topLeft != -1) set(this.topLeft, x, topRowY, centerColumnX - x, y + height - topRowY, c); 
/* 361 */     if (this.topCenter != -1) set(this.topCenter, centerColumnX, topRowY, rightColumnX - centerColumnX, y + height - topRowY, c); 
/* 362 */     if (this.topRight != -1) set(this.topRight, rightColumnX, topRowY, x + width - rightColumnX, y + height - topRowY, c); 
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float x, float y, float width, float height) {
/* 366 */     prepareVertices(batch, x, y, width, height);
/* 367 */     batch.draw(this.texture, this.vertices, 0, this.idx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/* 372 */     prepareVertices(batch, x, y, width, height);
/* 373 */     float worldOriginX = x + originX, worldOriginY = y + originY;
/* 374 */     int n = this.idx;
/* 375 */     float[] vertices = this.vertices;
/* 376 */     if (rotation != 0.0F) {
/* 377 */       for (int i = 0; i < n; i += 5) {
/* 378 */         float vx = (vertices[i] - worldOriginX) * scaleX, vy = (vertices[i + 1] - worldOriginY) * scaleY;
/* 379 */         float cos = MathUtils.cosDeg(rotation), sin = MathUtils.sinDeg(rotation);
/* 380 */         vertices[i] = cos * vx - sin * vy + worldOriginX;
/* 381 */         vertices[i + 1] = sin * vx + cos * vy + worldOriginY;
/*     */       } 
/* 383 */     } else if (scaleX != 1.0F || scaleY != 1.0F) {
/* 384 */       for (int i = 0; i < n; i += 5) {
/* 385 */         vertices[i] = (vertices[i] - worldOriginX) * scaleX + worldOriginX;
/* 386 */         vertices[i + 1] = (vertices[i + 1] - worldOriginY) * scaleY + worldOriginY;
/*     */       } 
/*     */     } 
/* 389 */     batch.draw(this.texture, vertices, 0, n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 395 */     this.color.set(color);
/*     */   }
/*     */   
/*     */   public Color getColor() {
/* 399 */     return this.color;
/*     */   }
/*     */   
/*     */   public float getLeftWidth() {
/* 403 */     return this.leftWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLeftWidth(float leftWidth) {
/* 408 */     this.leftWidth = leftWidth;
/*     */   }
/*     */   
/*     */   public float getRightWidth() {
/* 412 */     return this.rightWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRightWidth(float rightWidth) {
/* 417 */     this.rightWidth = rightWidth;
/*     */   }
/*     */   
/*     */   public float getTopHeight() {
/* 421 */     return this.topHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTopHeight(float topHeight) {
/* 426 */     this.topHeight = topHeight;
/*     */   }
/*     */   
/*     */   public float getBottomHeight() {
/* 430 */     return this.bottomHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBottomHeight(float bottomHeight) {
/* 435 */     this.bottomHeight = bottomHeight;
/*     */   }
/*     */   
/*     */   public float getMiddleWidth() {
/* 439 */     return this.middleWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMiddleWidth(float middleWidth) {
/* 446 */     this.middleWidth = middleWidth;
/*     */   }
/*     */   
/*     */   public float getMiddleHeight() {
/* 450 */     return this.middleHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMiddleHeight(float middleHeight) {
/* 457 */     this.middleHeight = middleHeight;
/*     */   }
/*     */   
/*     */   public float getTotalWidth() {
/* 461 */     return this.leftWidth + this.middleWidth + this.rightWidth;
/*     */   }
/*     */   
/*     */   public float getTotalHeight() {
/* 465 */     return this.topHeight + this.middleHeight + this.bottomHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPadding(float left, float right, float top, float bottom) {
/* 471 */     this.padLeft = left;
/* 472 */     this.padRight = right;
/* 473 */     this.padTop = top;
/* 474 */     this.padBottom = bottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadLeft() {
/* 479 */     if (this.padLeft == -1.0F) return getLeftWidth(); 
/* 480 */     return this.padLeft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadLeft(float left) {
/* 485 */     this.padLeft = left;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadRight() {
/* 490 */     if (this.padRight == -1.0F) return getRightWidth(); 
/* 491 */     return this.padRight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadRight(float right) {
/* 496 */     this.padRight = right;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadTop() {
/* 501 */     if (this.padTop == -1.0F) return getTopHeight(); 
/* 502 */     return this.padTop;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadTop(float top) {
/* 507 */     this.padTop = top;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadBottom() {
/* 512 */     if (this.padBottom == -1.0F) return getBottomHeight(); 
/* 513 */     return this.padBottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadBottom(float bottom) {
/* 518 */     this.padBottom = bottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(float scaleX, float scaleY) {
/* 523 */     this.leftWidth *= scaleX;
/* 524 */     this.rightWidth *= scaleX;
/* 525 */     this.topHeight *= scaleY;
/* 526 */     this.bottomHeight *= scaleY;
/* 527 */     this.middleWidth *= scaleX;
/* 528 */     this.middleHeight *= scaleY;
/* 529 */     if (this.padLeft != -1.0F) this.padLeft *= scaleX; 
/* 530 */     if (this.padRight != -1.0F) this.padRight *= scaleX; 
/* 531 */     if (this.padTop != -1.0F) this.padTop *= scaleY; 
/* 532 */     if (this.padBottom != -1.0F) this.padBottom *= scaleY; 
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/* 536 */     return this.texture;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\NinePatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */