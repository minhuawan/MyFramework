/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Container<T extends Actor>
/*     */   extends WidgetGroup
/*     */ {
/*     */   private T actor;
/*  19 */   private Value minWidth = Value.minWidth, minHeight = Value.minHeight;
/*  20 */   private Value prefWidth = Value.prefWidth, prefHeight = Value.prefHeight;
/*  21 */   private Value maxWidth = Value.zero, maxHeight = Value.zero;
/*  22 */   private Value padTop = Value.zero; private Value padLeft = Value.zero; private Value padBottom = Value.zero; private Value padRight = Value.zero;
/*     */   private float fillX;
/*     */   private float fillY;
/*     */   private int align;
/*     */   private Drawable background;
/*     */   private boolean clip;
/*     */   private boolean round = true;
/*     */   
/*     */   public Container() {
/*  31 */     setTouchable(Touchable.childrenOnly);
/*  32 */     setTransform(false);
/*     */   }
/*     */   
/*     */   public Container(T actor) {
/*  36 */     this();
/*  37 */     setActor(actor);
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*  41 */     validate();
/*  42 */     if (isTransform()) {
/*  43 */       applyTransform(batch, computeTransform());
/*  44 */       drawBackground(batch, parentAlpha, 0.0F, 0.0F);
/*  45 */       if (this.clip) {
/*  46 */         batch.flush();
/*  47 */         float padLeft = this.padLeft.get((Actor)this), padBottom = this.padBottom.get((Actor)this);
/*  48 */         if (clipBegin(padLeft, padBottom, getWidth() - padLeft - this.padRight.get((Actor)this), 
/*  49 */             getHeight() - padBottom - this.padTop.get((Actor)this))) {
/*  50 */           drawChildren(batch, parentAlpha);
/*  51 */           batch.flush();
/*  52 */           clipEnd();
/*     */         } 
/*     */       } else {
/*  55 */         drawChildren(batch, parentAlpha);
/*  56 */       }  resetTransform(batch);
/*     */     } else {
/*  58 */       drawBackground(batch, parentAlpha, getX(), getY());
/*  59 */       super.draw(batch, parentAlpha);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
/*  66 */     if (this.background == null)
/*  67 */       return;  Color color = getColor();
/*  68 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*  69 */     this.background.draw(batch, x, y, getWidth(), getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackground(Drawable background) {
/*  75 */     setBackground(background, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackground(Drawable background, boolean adjustPadding) {
/*  83 */     if (this.background == background)
/*  84 */       return;  this.background = background;
/*  85 */     if (adjustPadding) {
/*  86 */       if (background == null) {
/*  87 */         pad(Value.zero);
/*     */       } else {
/*  89 */         pad(background.getTopHeight(), background.getLeftWidth(), background.getBottomHeight(), background.getRightWidth());
/*  90 */       }  invalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> background(Drawable background) {
/*  96 */     setBackground(background);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public Drawable getBackground() {
/* 101 */     return this.background;
/*     */   }
/*     */   public void layout() {
/*     */     float width, height;
/* 105 */     if (this.actor == null)
/*     */       return; 
/* 107 */     float padLeft = this.padLeft.get((Actor)this), padBottom = this.padBottom.get((Actor)this);
/* 108 */     float containerWidth = getWidth() - padLeft - this.padRight.get((Actor)this);
/* 109 */     float containerHeight = getHeight() - padBottom - this.padTop.get((Actor)this);
/* 110 */     float minWidth = this.minWidth.get((Actor)this.actor), minHeight = this.minHeight.get((Actor)this.actor);
/* 111 */     float prefWidth = this.prefWidth.get((Actor)this.actor), prefHeight = this.prefHeight.get((Actor)this.actor);
/* 112 */     float maxWidth = this.maxWidth.get((Actor)this.actor), maxHeight = this.maxHeight.get((Actor)this.actor);
/*     */ 
/*     */     
/* 115 */     if (this.fillX > 0.0F) {
/* 116 */       width = containerWidth * this.fillX;
/*     */     } else {
/* 118 */       width = Math.min(prefWidth, containerWidth);
/* 119 */     }  if (width < minWidth) width = minWidth; 
/* 120 */     if (maxWidth > 0.0F && width > maxWidth) width = maxWidth;
/*     */ 
/*     */     
/* 123 */     if (this.fillY > 0.0F) {
/* 124 */       height = containerHeight * this.fillY;
/*     */     } else {
/* 126 */       height = Math.min(prefHeight, containerHeight);
/* 127 */     }  if (height < minHeight) height = minHeight; 
/* 128 */     if (maxHeight > 0.0F && height > maxHeight) height = maxHeight;
/*     */     
/* 130 */     float x = padLeft;
/* 131 */     if ((this.align & 0x10) != 0) {
/* 132 */       x += containerWidth - width;
/* 133 */     } else if ((this.align & 0x8) == 0) {
/* 134 */       x += (containerWidth - width) / 2.0F;
/*     */     } 
/* 136 */     float y = padBottom;
/* 137 */     if ((this.align & 0x2) != 0) {
/* 138 */       y += containerHeight - height;
/* 139 */     } else if ((this.align & 0x4) == 0) {
/* 140 */       y += (containerHeight - height) / 2.0F;
/*     */     } 
/* 142 */     if (this.round) {
/* 143 */       x = Math.round(x);
/* 144 */       y = Math.round(y);
/* 145 */       width = Math.round(width);
/* 146 */       height = Math.round(height);
/*     */     } 
/*     */     
/* 149 */     this.actor.setBounds(x, y, width, height);
/* 150 */     if (this.actor instanceof Layout) ((Layout)this.actor).validate();
/*     */   
/*     */   }
/*     */   
/*     */   public void setActor(T actor) {
/* 155 */     if (actor == this) throw new IllegalArgumentException("actor cannot be the Container."); 
/* 156 */     if (actor == this.actor)
/* 157 */       return;  if (this.actor != null) super.removeActor((Actor)this.actor); 
/* 158 */     this.actor = actor;
/* 159 */     if (actor != null) super.addActor((Actor)actor);
/*     */   
/*     */   }
/*     */   
/*     */   public T getActor() {
/* 164 */     return this.actor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActor(Actor actor) {
/* 170 */     throw new UnsupportedOperationException("Use Container#setActor.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorAt(int index, Actor actor) {
/* 176 */     throw new UnsupportedOperationException("Use Container#setActor.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorBefore(Actor actorBefore, Actor actor) {
/* 182 */     throw new UnsupportedOperationException("Use Container#setActor.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorAfter(Actor actorAfter, Actor actor) {
/* 188 */     throw new UnsupportedOperationException("Use Container#setActor.");
/*     */   }
/*     */   
/*     */   public boolean removeActor(Actor actor) {
/* 192 */     if (actor != this.actor) return false; 
/* 193 */     setActor((T)null);
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> size(Value size) {
/* 199 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/* 200 */     this.minWidth = size;
/* 201 */     this.minHeight = size;
/* 202 */     this.prefWidth = size;
/* 203 */     this.prefHeight = size;
/* 204 */     this.maxWidth = size;
/* 205 */     this.maxHeight = size;
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> size(Value width, Value height) {
/* 211 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/* 212 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/* 213 */     this.minWidth = width;
/* 214 */     this.minHeight = height;
/* 215 */     this.prefWidth = width;
/* 216 */     this.prefHeight = height;
/* 217 */     this.maxWidth = width;
/* 218 */     this.maxHeight = height;
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> size(float size) {
/* 224 */     size(new Value.Fixed(size));
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> size(float width, float height) {
/* 230 */     size(new Value.Fixed(width), new Value.Fixed(height));
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> width(Value width) {
/* 236 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/* 237 */     this.minWidth = width;
/* 238 */     this.prefWidth = width;
/* 239 */     this.maxWidth = width;
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> width(float width) {
/* 245 */     width(new Value.Fixed(width));
/* 246 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> height(Value height) {
/* 251 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/* 252 */     this.minHeight = height;
/* 253 */     this.prefHeight = height;
/* 254 */     this.maxHeight = height;
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> height(float height) {
/* 260 */     height(new Value.Fixed(height));
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> minSize(Value size) {
/* 266 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/* 267 */     this.minWidth = size;
/* 268 */     this.minHeight = size;
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> minSize(Value width, Value height) {
/* 274 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/* 275 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/* 276 */     this.minWidth = width;
/* 277 */     this.minHeight = height;
/* 278 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> minWidth(Value minWidth) {
/* 282 */     if (minWidth == null) throw new IllegalArgumentException("minWidth cannot be null."); 
/* 283 */     this.minWidth = minWidth;
/* 284 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> minHeight(Value minHeight) {
/* 288 */     if (minHeight == null) throw new IllegalArgumentException("minHeight cannot be null."); 
/* 289 */     this.minHeight = minHeight;
/* 290 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> minSize(float size) {
/* 295 */     minSize(new Value.Fixed(size));
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> minSize(float width, float height) {
/* 301 */     minSize(new Value.Fixed(width), new Value.Fixed(height));
/* 302 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> minWidth(float minWidth) {
/* 306 */     this.minWidth = new Value.Fixed(minWidth);
/* 307 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> minHeight(float minHeight) {
/* 311 */     this.minHeight = new Value.Fixed(minHeight);
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> prefSize(Value size) {
/* 317 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/* 318 */     this.prefWidth = size;
/* 319 */     this.prefHeight = size;
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> prefSize(Value width, Value height) {
/* 325 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/* 326 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/* 327 */     this.prefWidth = width;
/* 328 */     this.prefHeight = height;
/* 329 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> prefWidth(Value prefWidth) {
/* 333 */     if (prefWidth == null) throw new IllegalArgumentException("prefWidth cannot be null."); 
/* 334 */     this.prefWidth = prefWidth;
/* 335 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> prefHeight(Value prefHeight) {
/* 339 */     if (prefHeight == null) throw new IllegalArgumentException("prefHeight cannot be null."); 
/* 340 */     this.prefHeight = prefHeight;
/* 341 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> prefSize(float width, float height) {
/* 346 */     prefSize(new Value.Fixed(width), new Value.Fixed(height));
/* 347 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> prefSize(float size) {
/* 352 */     prefSize(new Value.Fixed(size));
/* 353 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> prefWidth(float prefWidth) {
/* 357 */     this.prefWidth = new Value.Fixed(prefWidth);
/* 358 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> prefHeight(float prefHeight) {
/* 362 */     this.prefHeight = new Value.Fixed(prefHeight);
/* 363 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> maxSize(Value size) {
/* 368 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/* 369 */     this.maxWidth = size;
/* 370 */     this.maxHeight = size;
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> maxSize(Value width, Value height) {
/* 376 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/* 377 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/* 378 */     this.maxWidth = width;
/* 379 */     this.maxHeight = height;
/* 380 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> maxWidth(Value maxWidth) {
/* 384 */     if (maxWidth == null) throw new IllegalArgumentException("maxWidth cannot be null."); 
/* 385 */     this.maxWidth = maxWidth;
/* 386 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> maxHeight(Value maxHeight) {
/* 390 */     if (maxHeight == null) throw new IllegalArgumentException("maxHeight cannot be null."); 
/* 391 */     this.maxHeight = maxHeight;
/* 392 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> maxSize(float size) {
/* 397 */     maxSize(new Value.Fixed(size));
/* 398 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> maxSize(float width, float height) {
/* 403 */     maxSize(new Value.Fixed(width), new Value.Fixed(height));
/* 404 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> maxWidth(float maxWidth) {
/* 408 */     this.maxWidth = new Value.Fixed(maxWidth);
/* 409 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> maxHeight(float maxHeight) {
/* 413 */     this.maxHeight = new Value.Fixed(maxHeight);
/* 414 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> pad(Value pad) {
/* 419 */     if (pad == null) throw new IllegalArgumentException("pad cannot be null."); 
/* 420 */     this.padTop = pad;
/* 421 */     this.padLeft = pad;
/* 422 */     this.padBottom = pad;
/* 423 */     this.padRight = pad;
/* 424 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> pad(Value top, Value left, Value bottom, Value right) {
/* 428 */     if (top == null) throw new IllegalArgumentException("top cannot be null."); 
/* 429 */     if (left == null) throw new IllegalArgumentException("left cannot be null."); 
/* 430 */     if (bottom == null) throw new IllegalArgumentException("bottom cannot be null."); 
/* 431 */     if (right == null) throw new IllegalArgumentException("right cannot be null."); 
/* 432 */     this.padTop = top;
/* 433 */     this.padLeft = left;
/* 434 */     this.padBottom = bottom;
/* 435 */     this.padRight = right;
/* 436 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padTop(Value padTop) {
/* 440 */     if (padTop == null) throw new IllegalArgumentException("padTop cannot be null."); 
/* 441 */     this.padTop = padTop;
/* 442 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padLeft(Value padLeft) {
/* 446 */     if (padLeft == null) throw new IllegalArgumentException("padLeft cannot be null."); 
/* 447 */     this.padLeft = padLeft;
/* 448 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padBottom(Value padBottom) {
/* 452 */     if (padBottom == null) throw new IllegalArgumentException("padBottom cannot be null."); 
/* 453 */     this.padBottom = padBottom;
/* 454 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padRight(Value padRight) {
/* 458 */     if (padRight == null) throw new IllegalArgumentException("padRight cannot be null."); 
/* 459 */     this.padRight = padRight;
/* 460 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> pad(float pad) {
/* 465 */     Value value = new Value.Fixed(pad);
/* 466 */     this.padTop = value;
/* 467 */     this.padLeft = value;
/* 468 */     this.padBottom = value;
/* 469 */     this.padRight = value;
/* 470 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> pad(float top, float left, float bottom, float right) {
/* 474 */     this.padTop = new Value.Fixed(top);
/* 475 */     this.padLeft = new Value.Fixed(left);
/* 476 */     this.padBottom = new Value.Fixed(bottom);
/* 477 */     this.padRight = new Value.Fixed(right);
/* 478 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padTop(float padTop) {
/* 482 */     this.padTop = new Value.Fixed(padTop);
/* 483 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padLeft(float padLeft) {
/* 487 */     this.padLeft = new Value.Fixed(padLeft);
/* 488 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padBottom(float padBottom) {
/* 492 */     this.padBottom = new Value.Fixed(padBottom);
/* 493 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> padRight(float padRight) {
/* 497 */     this.padRight = new Value.Fixed(padRight);
/* 498 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> fill() {
/* 503 */     this.fillX = 1.0F;
/* 504 */     this.fillY = 1.0F;
/* 505 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> fillX() {
/* 510 */     this.fillX = 1.0F;
/* 511 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> fillY() {
/* 516 */     this.fillY = 1.0F;
/* 517 */     return this;
/*     */   }
/*     */   
/*     */   public Container<T> fill(float x, float y) {
/* 521 */     this.fillX = x;
/* 522 */     this.fillY = y;
/* 523 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> fill(boolean x, boolean y) {
/* 528 */     this.fillX = x ? 1.0F : 0.0F;
/* 529 */     this.fillY = y ? 1.0F : 0.0F;
/* 530 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> fill(boolean fill) {
/* 535 */     this.fillX = fill ? 1.0F : 0.0F;
/* 536 */     this.fillY = fill ? 1.0F : 0.0F;
/* 537 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Container<T> align(int align) {
/* 543 */     this.align = align;
/* 544 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> center() {
/* 549 */     this.align = 1;
/* 550 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> top() {
/* 555 */     this.align |= 0x2;
/* 556 */     this.align &= 0xFFFFFFFB;
/* 557 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> left() {
/* 562 */     this.align |= 0x8;
/* 563 */     this.align &= 0xFFFFFFEF;
/* 564 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> bottom() {
/* 569 */     this.align |= 0x4;
/* 570 */     this.align &= 0xFFFFFFFD;
/* 571 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Container<T> right() {
/* 576 */     this.align |= 0x10;
/* 577 */     this.align &= 0xFFFFFFF7;
/* 578 */     return this;
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/* 582 */     return this.minWidth.get((Actor)this.actor) + this.padLeft.get((Actor)this) + this.padRight.get((Actor)this);
/*     */   }
/*     */   
/*     */   public Value getMinHeightValue() {
/* 586 */     return this.minHeight;
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/* 590 */     return this.minHeight.get((Actor)this.actor) + this.padTop.get((Actor)this) + this.padBottom.get((Actor)this);
/*     */   }
/*     */   
/*     */   public Value getPrefWidthValue() {
/* 594 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 598 */     float v = this.prefWidth.get((Actor)this.actor);
/* 599 */     if (this.background != null) v = Math.max(v, this.background.getMinWidth()); 
/* 600 */     return Math.max(getMinWidth(), v + this.padLeft.get((Actor)this) + this.padRight.get((Actor)this));
/*     */   }
/*     */   
/*     */   public Value getPrefHeightValue() {
/* 604 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 608 */     float v = this.prefHeight.get((Actor)this.actor);
/* 609 */     if (this.background != null) v = Math.max(v, this.background.getMinHeight()); 
/* 610 */     return Math.max(getMinHeight(), v + this.padTop.get((Actor)this) + this.padBottom.get((Actor)this));
/*     */   }
/*     */   
/*     */   public Value getMaxWidthValue() {
/* 614 */     return this.maxWidth;
/*     */   }
/*     */   
/*     */   public float getMaxWidth() {
/* 618 */     float v = this.maxWidth.get((Actor)this.actor);
/* 619 */     if (v > 0.0F) v += this.padLeft.get((Actor)this) + this.padRight.get((Actor)this); 
/* 620 */     return v;
/*     */   }
/*     */   
/*     */   public Value getMaxHeightValue() {
/* 624 */     return this.maxHeight;
/*     */   }
/*     */   
/*     */   public float getMaxHeight() {
/* 628 */     float v = this.maxHeight.get((Actor)this.actor);
/* 629 */     if (v > 0.0F) v += this.padTop.get((Actor)this) + this.padBottom.get((Actor)this); 
/* 630 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   public Value getPadTopValue() {
/* 635 */     return this.padTop;
/*     */   }
/*     */   
/*     */   public float getPadTop() {
/* 639 */     return this.padTop.get((Actor)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Value getPadLeftValue() {
/* 644 */     return this.padLeft;
/*     */   }
/*     */   
/*     */   public float getPadLeft() {
/* 648 */     return this.padLeft.get((Actor)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Value getPadBottomValue() {
/* 653 */     return this.padBottom;
/*     */   }
/*     */   
/*     */   public float getPadBottom() {
/* 657 */     return this.padBottom.get((Actor)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Value getPadRightValue() {
/* 662 */     return this.padRight;
/*     */   }
/*     */   
/*     */   public float getPadRight() {
/* 666 */     return this.padRight.get((Actor)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadX() {
/* 671 */     return this.padLeft.get((Actor)this) + this.padRight.get((Actor)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPadY() {
/* 676 */     return this.padTop.get((Actor)this) + this.padBottom.get((Actor)this);
/*     */   }
/*     */   
/*     */   public float getFillX() {
/* 680 */     return this.fillX;
/*     */   }
/*     */   
/*     */   public float getFillY() {
/* 684 */     return this.fillY;
/*     */   }
/*     */   
/*     */   public int getAlign() {
/* 688 */     return this.align;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRound(boolean round) {
/* 693 */     this.round = round;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClip(boolean enabled) {
/* 699 */     this.clip = enabled;
/* 700 */     setTransform(enabled);
/* 701 */     invalidate();
/*     */   }
/*     */   
/*     */   public boolean getClip() {
/* 705 */     return this.clip;
/*     */   }
/*     */   
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 709 */     if (this.clip) {
/* 710 */       if (touchable && getTouchable() == Touchable.disabled) return null; 
/* 711 */       if (x < 0.0F || x >= getWidth() || y < 0.0F || y >= getHeight()) return null; 
/*     */     } 
/* 713 */     return super.hit(x, y, touchable);
/*     */   }
/*     */   
/*     */   public void drawDebug(ShapeRenderer shapes) {
/* 717 */     validate();
/* 718 */     if (isTransform()) {
/* 719 */       applyTransform(shapes, computeTransform());
/* 720 */       if (this.clip) {
/* 721 */         shapes.flush();
/* 722 */         float padLeft = this.padLeft.get((Actor)this), padBottom = this.padBottom.get((Actor)this);
/* 723 */         boolean draw = (this.background == null) ? clipBegin(0.0F, 0.0F, getWidth(), getHeight()) : clipBegin(padLeft, padBottom, 
/* 724 */             getWidth() - padLeft - this.padRight.get((Actor)this), getHeight() - padBottom - this.padTop.get((Actor)this));
/* 725 */         if (draw) {
/* 726 */           drawDebugChildren(shapes);
/* 727 */           clipEnd();
/*     */         } 
/*     */       } else {
/* 730 */         drawDebugChildren(shapes);
/* 731 */       }  resetTransform(shapes);
/*     */     } else {
/* 733 */       super.drawDebug(shapes);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Container.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */