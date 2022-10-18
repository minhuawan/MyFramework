/*      */ package com.badlogic.gdx.scenes.scene2d.ui;
/*      */ 
/*      */ import com.badlogic.gdx.Files;
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*      */ import com.badlogic.gdx.utils.Pool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Cell<T extends Actor>
/*      */   implements Pool.Poolable
/*      */ {
/*   14 */   private static final Float zerof = Float.valueOf(0.0F); private static final Float onef = Float.valueOf(1.0F);
/*   15 */   private static final Integer zeroi = Integer.valueOf(0), onei = Integer.valueOf(1);
/*   16 */   private static final Integer centeri = onei, topi = Integer.valueOf(2), bottomi = Integer.valueOf(4), lefti = Integer.valueOf(8);
/*   17 */   private static final Integer righti = Integer.valueOf(16);
/*      */   
/*      */   private static Files files;
/*      */   
/*      */   private static Cell defaults;
/*      */   
/*      */   Value minWidth;
/*      */   
/*      */   Value minHeight;
/*      */   
/*      */   Value prefWidth;
/*      */   Value prefHeight;
/*      */   Value maxWidth;
/*      */   Value maxHeight;
/*      */   Value spaceTop;
/*      */   Value spaceLeft;
/*      */   Value spaceBottom;
/*      */   Value spaceRight;
/*      */   Value padTop;
/*      */   Value padLeft;
/*      */   Value padBottom;
/*      */   Value padRight;
/*      */   Float fillX;
/*      */   Float fillY;
/*      */   Integer align;
/*      */   
/*      */   public Cell() {
/*   44 */     reset();
/*      */   }
/*      */   Integer expandX; Integer expandY; Integer colspan; Boolean uniformX; Boolean uniformY; Actor actor; float actorX; float actorY; float actorWidth; float actorHeight; private Table table; boolean endRow; int column; int row; int cellAboveIndex; float computedPadTop; float computedPadLeft; float computedPadBottom; float computedPadRight;
/*      */   public void setLayout(Table table) {
/*   48 */     this.table = table;
/*      */   }
/*      */ 
/*      */   
/*      */   public <A extends Actor> Cell<A> setActor(A newActor) {
/*   53 */     if (this.actor != newActor) {
/*   54 */       if (this.actor != null) this.actor.remove(); 
/*   55 */       this.actor = (Actor)newActor;
/*   56 */       if (newActor != null) this.table.addActor((Actor)newActor); 
/*      */     } 
/*   58 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> clearActor() {
/*   63 */     setActor(null);
/*   64 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public T getActor() {
/*   69 */     return (T)this.actor;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasActor() {
/*   74 */     return (this.actor != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> size(Value size) {
/*   79 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/*   80 */     this.minWidth = size;
/*   81 */     this.minHeight = size;
/*   82 */     this.prefWidth = size;
/*   83 */     this.prefHeight = size;
/*   84 */     this.maxWidth = size;
/*   85 */     this.maxHeight = size;
/*   86 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> size(Value width, Value height) {
/*   91 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/*   92 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/*   93 */     this.minWidth = width;
/*   94 */     this.minHeight = height;
/*   95 */     this.prefWidth = width;
/*   96 */     this.prefHeight = height;
/*   97 */     this.maxWidth = width;
/*   98 */     this.maxHeight = height;
/*   99 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> size(float size) {
/*  104 */     size(new Value.Fixed(size));
/*  105 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> size(float width, float height) {
/*  110 */     size(new Value.Fixed(width), new Value.Fixed(height));
/*  111 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> width(Value width) {
/*  116 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/*  117 */     this.minWidth = width;
/*  118 */     this.prefWidth = width;
/*  119 */     this.maxWidth = width;
/*  120 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> width(float width) {
/*  125 */     width(new Value.Fixed(width));
/*  126 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> height(Value height) {
/*  131 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/*  132 */     this.minHeight = height;
/*  133 */     this.prefHeight = height;
/*  134 */     this.maxHeight = height;
/*  135 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> height(float height) {
/*  140 */     height(new Value.Fixed(height));
/*  141 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> minSize(Value size) {
/*  146 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/*  147 */     this.minWidth = size;
/*  148 */     this.minHeight = size;
/*  149 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> minSize(Value width, Value height) {
/*  154 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/*  155 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/*  156 */     this.minWidth = width;
/*  157 */     this.minHeight = height;
/*  158 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> minWidth(Value minWidth) {
/*  162 */     if (minWidth == null) throw new IllegalArgumentException("minWidth cannot be null."); 
/*  163 */     this.minWidth = minWidth;
/*  164 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> minHeight(Value minHeight) {
/*  168 */     if (minHeight == null) throw new IllegalArgumentException("minHeight cannot be null."); 
/*  169 */     this.minHeight = minHeight;
/*  170 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> minSize(float size) {
/*  175 */     minSize(new Value.Fixed(size));
/*  176 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> minSize(float width, float height) {
/*  181 */     minSize(new Value.Fixed(width), new Value.Fixed(height));
/*  182 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> minWidth(float minWidth) {
/*  186 */     this.minWidth = new Value.Fixed(minWidth);
/*  187 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> minHeight(float minHeight) {
/*  191 */     this.minHeight = new Value.Fixed(minHeight);
/*  192 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> prefSize(Value size) {
/*  197 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/*  198 */     this.prefWidth = size;
/*  199 */     this.prefHeight = size;
/*  200 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> prefSize(Value width, Value height) {
/*  205 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/*  206 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/*  207 */     this.prefWidth = width;
/*  208 */     this.prefHeight = height;
/*  209 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> prefWidth(Value prefWidth) {
/*  213 */     if (prefWidth == null) throw new IllegalArgumentException("prefWidth cannot be null."); 
/*  214 */     this.prefWidth = prefWidth;
/*  215 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> prefHeight(Value prefHeight) {
/*  219 */     if (prefHeight == null) throw new IllegalArgumentException("prefHeight cannot be null."); 
/*  220 */     this.prefHeight = prefHeight;
/*  221 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> prefSize(float width, float height) {
/*  226 */     prefSize(new Value.Fixed(width), new Value.Fixed(height));
/*  227 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> prefSize(float size) {
/*  232 */     prefSize(new Value.Fixed(size));
/*  233 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> prefWidth(float prefWidth) {
/*  237 */     this.prefWidth = new Value.Fixed(prefWidth);
/*  238 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> prefHeight(float prefHeight) {
/*  242 */     this.prefHeight = new Value.Fixed(prefHeight);
/*  243 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> maxSize(Value size) {
/*  248 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/*  249 */     this.maxWidth = size;
/*  250 */     this.maxHeight = size;
/*  251 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> maxSize(Value width, Value height) {
/*  256 */     if (width == null) throw new IllegalArgumentException("width cannot be null."); 
/*  257 */     if (height == null) throw new IllegalArgumentException("height cannot be null."); 
/*  258 */     this.maxWidth = width;
/*  259 */     this.maxHeight = height;
/*  260 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> maxWidth(Value maxWidth) {
/*  264 */     if (maxWidth == null) throw new IllegalArgumentException("maxWidth cannot be null."); 
/*  265 */     this.maxWidth = maxWidth;
/*  266 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> maxHeight(Value maxHeight) {
/*  270 */     if (maxHeight == null) throw new IllegalArgumentException("maxHeight cannot be null."); 
/*  271 */     this.maxHeight = maxHeight;
/*  272 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> maxSize(float size) {
/*  277 */     maxSize(new Value.Fixed(size));
/*  278 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> maxSize(float width, float height) {
/*  283 */     maxSize(new Value.Fixed(width), new Value.Fixed(height));
/*  284 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> maxWidth(float maxWidth) {
/*  288 */     this.maxWidth = new Value.Fixed(maxWidth);
/*  289 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> maxHeight(float maxHeight) {
/*  293 */     this.maxHeight = new Value.Fixed(maxHeight);
/*  294 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> space(Value space) {
/*  299 */     if (space == null) throw new IllegalArgumentException("space cannot be null."); 
/*  300 */     this.spaceTop = space;
/*  301 */     this.spaceLeft = space;
/*  302 */     this.spaceBottom = space;
/*  303 */     this.spaceRight = space;
/*  304 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> space(Value top, Value left, Value bottom, Value right) {
/*  308 */     if (top == null) throw new IllegalArgumentException("top cannot be null."); 
/*  309 */     if (left == null) throw new IllegalArgumentException("left cannot be null."); 
/*  310 */     if (bottom == null) throw new IllegalArgumentException("bottom cannot be null."); 
/*  311 */     if (right == null) throw new IllegalArgumentException("right cannot be null."); 
/*  312 */     this.spaceTop = top;
/*  313 */     this.spaceLeft = left;
/*  314 */     this.spaceBottom = bottom;
/*  315 */     this.spaceRight = right;
/*  316 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceTop(Value spaceTop) {
/*  320 */     if (spaceTop == null) throw new IllegalArgumentException("spaceTop cannot be null."); 
/*  321 */     this.spaceTop = spaceTop;
/*  322 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceLeft(Value spaceLeft) {
/*  326 */     if (spaceLeft == null) throw new IllegalArgumentException("spaceLeft cannot be null."); 
/*  327 */     this.spaceLeft = spaceLeft;
/*  328 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceBottom(Value spaceBottom) {
/*  332 */     if (spaceBottom == null) throw new IllegalArgumentException("spaceBottom cannot be null."); 
/*  333 */     this.spaceBottom = spaceBottom;
/*  334 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceRight(Value spaceRight) {
/*  338 */     if (spaceRight == null) throw new IllegalArgumentException("spaceRight cannot be null."); 
/*  339 */     this.spaceRight = spaceRight;
/*  340 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> space(float space) {
/*  345 */     if (space < 0.0F) throw new IllegalArgumentException("space cannot be < 0."); 
/*  346 */     space(new Value.Fixed(space));
/*  347 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> space(float top, float left, float bottom, float right) {
/*  351 */     if (top < 0.0F) throw new IllegalArgumentException("top cannot be < 0."); 
/*  352 */     if (left < 0.0F) throw new IllegalArgumentException("left cannot be < 0."); 
/*  353 */     if (bottom < 0.0F) throw new IllegalArgumentException("bottom cannot be < 0."); 
/*  354 */     if (right < 0.0F) throw new IllegalArgumentException("right cannot be < 0."); 
/*  355 */     space(new Value.Fixed(top), new Value.Fixed(left), new Value.Fixed(bottom), new Value.Fixed(right));
/*  356 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceTop(float spaceTop) {
/*  360 */     if (spaceTop < 0.0F) throw new IllegalArgumentException("spaceTop cannot be < 0."); 
/*  361 */     this.spaceTop = new Value.Fixed(spaceTop);
/*  362 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceLeft(float spaceLeft) {
/*  366 */     if (spaceLeft < 0.0F) throw new IllegalArgumentException("spaceLeft cannot be < 0."); 
/*  367 */     this.spaceLeft = new Value.Fixed(spaceLeft);
/*  368 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceBottom(float spaceBottom) {
/*  372 */     if (spaceBottom < 0.0F) throw new IllegalArgumentException("spaceBottom cannot be < 0."); 
/*  373 */     this.spaceBottom = new Value.Fixed(spaceBottom);
/*  374 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> spaceRight(float spaceRight) {
/*  378 */     if (spaceRight < 0.0F) throw new IllegalArgumentException("spaceRight cannot be < 0."); 
/*  379 */     this.spaceRight = new Value.Fixed(spaceRight);
/*  380 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> pad(Value pad) {
/*  385 */     if (pad == null) throw new IllegalArgumentException("pad cannot be null."); 
/*  386 */     this.padTop = pad;
/*  387 */     this.padLeft = pad;
/*  388 */     this.padBottom = pad;
/*  389 */     this.padRight = pad;
/*  390 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> pad(Value top, Value left, Value bottom, Value right) {
/*  394 */     if (top == null) throw new IllegalArgumentException("top cannot be null."); 
/*  395 */     if (left == null) throw new IllegalArgumentException("left cannot be null."); 
/*  396 */     if (bottom == null) throw new IllegalArgumentException("bottom cannot be null."); 
/*  397 */     if (right == null) throw new IllegalArgumentException("right cannot be null."); 
/*  398 */     this.padTop = top;
/*  399 */     this.padLeft = left;
/*  400 */     this.padBottom = bottom;
/*  401 */     this.padRight = right;
/*  402 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padTop(Value padTop) {
/*  406 */     if (padTop == null) throw new IllegalArgumentException("padTop cannot be null."); 
/*  407 */     this.padTop = padTop;
/*  408 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padLeft(Value padLeft) {
/*  412 */     if (padLeft == null) throw new IllegalArgumentException("padLeft cannot be null."); 
/*  413 */     this.padLeft = padLeft;
/*  414 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padBottom(Value padBottom) {
/*  418 */     if (padBottom == null) throw new IllegalArgumentException("padBottom cannot be null."); 
/*  419 */     this.padBottom = padBottom;
/*  420 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padRight(Value padRight) {
/*  424 */     if (padRight == null) throw new IllegalArgumentException("padRight cannot be null."); 
/*  425 */     this.padRight = padRight;
/*  426 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> pad(float pad) {
/*  431 */     pad(new Value.Fixed(pad));
/*  432 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> pad(float top, float left, float bottom, float right) {
/*  436 */     pad(new Value.Fixed(top), new Value.Fixed(left), new Value.Fixed(bottom), new Value.Fixed(right));
/*  437 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padTop(float padTop) {
/*  441 */     this.padTop = new Value.Fixed(padTop);
/*  442 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padLeft(float padLeft) {
/*  446 */     this.padLeft = new Value.Fixed(padLeft);
/*  447 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padBottom(float padBottom) {
/*  451 */     this.padBottom = new Value.Fixed(padBottom);
/*  452 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> padRight(float padRight) {
/*  456 */     this.padRight = new Value.Fixed(padRight);
/*  457 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> fill() {
/*  462 */     this.fillX = onef;
/*  463 */     this.fillY = onef;
/*  464 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> fillX() {
/*  469 */     this.fillX = onef;
/*  470 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> fillY() {
/*  475 */     this.fillY = onef;
/*  476 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> fill(float x, float y) {
/*  480 */     this.fillX = Float.valueOf(x);
/*  481 */     this.fillY = Float.valueOf(y);
/*  482 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> fill(boolean x, boolean y) {
/*  487 */     this.fillX = x ? onef : zerof;
/*  488 */     this.fillY = y ? onef : zerof;
/*  489 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> fill(boolean fill) {
/*  494 */     this.fillX = fill ? onef : zerof;
/*  495 */     this.fillY = fill ? onef : zerof;
/*  496 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Cell<T> align(int align) {
/*  502 */     this.align = Integer.valueOf(align);
/*  503 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> center() {
/*  508 */     this.align = centeri;
/*  509 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> top() {
/*  514 */     if (this.align == null) {
/*  515 */       this.align = topi;
/*      */     } else {
/*  517 */       this.align = Integer.valueOf((this.align.intValue() | 0x2) & 0xFFFFFFFB);
/*  518 */     }  return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> left() {
/*  523 */     if (this.align == null) {
/*  524 */       this.align = lefti;
/*      */     } else {
/*  526 */       this.align = Integer.valueOf((this.align.intValue() | 0x8) & 0xFFFFFFEF);
/*  527 */     }  return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> bottom() {
/*  532 */     if (this.align == null) {
/*  533 */       this.align = bottomi;
/*      */     } else {
/*  535 */       this.align = Integer.valueOf((this.align.intValue() | 0x4) & 0xFFFFFFFD);
/*  536 */     }  return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> right() {
/*  541 */     if (this.align == null) {
/*  542 */       this.align = righti;
/*      */     } else {
/*  544 */       this.align = Integer.valueOf((this.align.intValue() | 0x10) & 0xFFFFFFF7);
/*  545 */     }  return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> grow() {
/*  550 */     this.expandX = onei;
/*  551 */     this.expandY = onei;
/*  552 */     this.fillX = onef;
/*  553 */     this.fillY = onef;
/*  554 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> growX() {
/*  559 */     this.expandX = onei;
/*  560 */     this.fillX = onef;
/*  561 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> growY() {
/*  566 */     this.expandY = onei;
/*  567 */     this.fillY = onef;
/*  568 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> expand() {
/*  573 */     this.expandX = onei;
/*  574 */     this.expandY = onei;
/*  575 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> expandX() {
/*  580 */     this.expandX = onei;
/*  581 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> expandY() {
/*  586 */     this.expandY = onei;
/*  587 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> expand(int x, int y) {
/*  591 */     this.expandX = Integer.valueOf(x);
/*  592 */     this.expandY = Integer.valueOf(y);
/*  593 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> expand(boolean x, boolean y) {
/*  598 */     this.expandX = x ? onei : zeroi;
/*  599 */     this.expandY = y ? onei : zeroi;
/*  600 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> colspan(int colspan) {
/*  604 */     this.colspan = Integer.valueOf(colspan);
/*  605 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> uniform() {
/*  610 */     this.uniformX = Boolean.TRUE;
/*  611 */     this.uniformY = Boolean.TRUE;
/*  612 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> uniformX() {
/*  617 */     this.uniformX = Boolean.TRUE;
/*  618 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<T> uniformY() {
/*  623 */     this.uniformY = Boolean.TRUE;
/*  624 */     return this;
/*      */   }
/*      */   
/*      */   public Cell<T> uniform(boolean x, boolean y) {
/*  628 */     this.uniformX = Boolean.valueOf(x);
/*  629 */     this.uniformY = Boolean.valueOf(y);
/*  630 */     return this;
/*      */   }
/*      */   
/*      */   public void setActorBounds(float x, float y, float width, float height) {
/*  634 */     this.actorX = x;
/*  635 */     this.actorY = y;
/*  636 */     this.actorWidth = width;
/*  637 */     this.actorHeight = height;
/*      */   }
/*      */   
/*      */   public float getActorX() {
/*  641 */     return this.actorX;
/*      */   }
/*      */   
/*      */   public void setActorX(float actorX) {
/*  645 */     this.actorX = actorX;
/*      */   }
/*      */   
/*      */   public float getActorY() {
/*  649 */     return this.actorY;
/*      */   }
/*      */   
/*      */   public void setActorY(float actorY) {
/*  653 */     this.actorY = actorY;
/*      */   }
/*      */   
/*      */   public float getActorWidth() {
/*  657 */     return this.actorWidth;
/*      */   }
/*      */   
/*      */   public void setActorWidth(float actorWidth) {
/*  661 */     this.actorWidth = actorWidth;
/*      */   }
/*      */   
/*      */   public float getActorHeight() {
/*  665 */     return this.actorHeight;
/*      */   }
/*      */   
/*      */   public void setActorHeight(float actorHeight) {
/*  669 */     this.actorHeight = actorHeight;
/*      */   }
/*      */   
/*      */   public int getColumn() {
/*  673 */     return this.column;
/*      */   }
/*      */   
/*      */   public int getRow() {
/*  677 */     return this.row;
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getMinWidthValue() {
/*  682 */     return this.minWidth;
/*      */   }
/*      */   
/*      */   public float getMinWidth() {
/*  686 */     return this.minWidth.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getMinHeightValue() {
/*  691 */     return this.minHeight;
/*      */   }
/*      */   
/*      */   public float getMinHeight() {
/*  695 */     return this.minHeight.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getPrefWidthValue() {
/*  700 */     return this.prefWidth;
/*      */   }
/*      */   
/*      */   public float getPrefWidth() {
/*  704 */     return this.prefWidth.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getPrefHeightValue() {
/*  709 */     return this.prefHeight;
/*      */   }
/*      */   
/*      */   public float getPrefHeight() {
/*  713 */     return this.prefHeight.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getMaxWidthValue() {
/*  718 */     return this.maxWidth;
/*      */   }
/*      */   
/*      */   public float getMaxWidth() {
/*  722 */     return this.maxWidth.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getMaxHeightValue() {
/*  727 */     return this.maxHeight;
/*      */   }
/*      */   
/*      */   public float getMaxHeight() {
/*  731 */     return this.maxHeight.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getSpaceTopValue() {
/*  736 */     return this.spaceTop;
/*      */   }
/*      */   
/*      */   public float getSpaceTop() {
/*  740 */     return this.spaceTop.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getSpaceLeftValue() {
/*  745 */     return this.spaceLeft;
/*      */   }
/*      */   
/*      */   public float getSpaceLeft() {
/*  749 */     return this.spaceLeft.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getSpaceBottomValue() {
/*  754 */     return this.spaceBottom;
/*      */   }
/*      */   
/*      */   public float getSpaceBottom() {
/*  758 */     return this.spaceBottom.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getSpaceRightValue() {
/*  763 */     return this.spaceRight;
/*      */   }
/*      */   
/*      */   public float getSpaceRight() {
/*  767 */     return this.spaceRight.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getPadTopValue() {
/*  772 */     return this.padTop;
/*      */   }
/*      */   
/*      */   public float getPadTop() {
/*  776 */     return this.padTop.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getPadLeftValue() {
/*  781 */     return this.padLeft;
/*      */   }
/*      */   
/*      */   public float getPadLeft() {
/*  785 */     return this.padLeft.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getPadBottomValue() {
/*  790 */     return this.padBottom;
/*      */   }
/*      */   
/*      */   public float getPadBottom() {
/*  794 */     return this.padBottom.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Value getPadRightValue() {
/*  799 */     return this.padRight;
/*      */   }
/*      */   
/*      */   public float getPadRight() {
/*  803 */     return this.padRight.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPadX() {
/*  808 */     return this.padLeft.get(this.actor) + this.padRight.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPadY() {
/*  813 */     return this.padTop.get(this.actor) + this.padBottom.get(this.actor);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFillX() {
/*  818 */     return this.fillX.floatValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFillY() {
/*  823 */     return this.fillY.floatValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getAlign() {
/*  828 */     return this.align.intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getExpandX() {
/*  833 */     return this.expandX.intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getExpandY() {
/*  838 */     return this.expandY.intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getColspan() {
/*  843 */     return this.colspan.intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getUniformX() {
/*  848 */     return this.uniformX.booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getUniformY() {
/*  853 */     return this.uniformY.booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEndRow() {
/*  858 */     return this.endRow;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getComputedPadTop() {
/*  863 */     return this.computedPadTop;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getComputedPadLeft() {
/*  868 */     return this.computedPadLeft;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getComputedPadBottom() {
/*  873 */     return this.computedPadBottom;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getComputedPadRight() {
/*  878 */     return this.computedPadRight;
/*      */   }
/*      */   
/*      */   public void row() {
/*  882 */     this.table.row();
/*      */   }
/*      */   
/*      */   public Table getTable() {
/*  886 */     return this.table;
/*      */   }
/*      */ 
/*      */   
/*      */   void clear() {
/*  891 */     this.minWidth = null;
/*  892 */     this.minHeight = null;
/*  893 */     this.prefWidth = null;
/*  894 */     this.prefHeight = null;
/*  895 */     this.maxWidth = null;
/*  896 */     this.maxHeight = null;
/*  897 */     this.spaceTop = null;
/*  898 */     this.spaceLeft = null;
/*  899 */     this.spaceBottom = null;
/*  900 */     this.spaceRight = null;
/*  901 */     this.padTop = null;
/*  902 */     this.padLeft = null;
/*  903 */     this.padBottom = null;
/*  904 */     this.padRight = null;
/*  905 */     this.fillX = null;
/*  906 */     this.fillY = null;
/*  907 */     this.align = null;
/*  908 */     this.expandX = null;
/*  909 */     this.expandY = null;
/*  910 */     this.colspan = null;
/*  911 */     this.uniformX = null;
/*  912 */     this.uniformY = null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void reset() {
/*  917 */     this.actor = null;
/*  918 */     this.table = null;
/*  919 */     this.endRow = false;
/*  920 */     this.cellAboveIndex = -1;
/*      */     
/*  922 */     Cell defaults = defaults();
/*  923 */     if (defaults != null) set(defaults); 
/*      */   }
/*      */   
/*      */   void set(Cell cell) {
/*  927 */     this.minWidth = cell.minWidth;
/*  928 */     this.minHeight = cell.minHeight;
/*  929 */     this.prefWidth = cell.prefWidth;
/*  930 */     this.prefHeight = cell.prefHeight;
/*  931 */     this.maxWidth = cell.maxWidth;
/*  932 */     this.maxHeight = cell.maxHeight;
/*  933 */     this.spaceTop = cell.spaceTop;
/*  934 */     this.spaceLeft = cell.spaceLeft;
/*  935 */     this.spaceBottom = cell.spaceBottom;
/*  936 */     this.spaceRight = cell.spaceRight;
/*  937 */     this.padTop = cell.padTop;
/*  938 */     this.padLeft = cell.padLeft;
/*  939 */     this.padBottom = cell.padBottom;
/*  940 */     this.padRight = cell.padRight;
/*  941 */     this.fillX = cell.fillX;
/*  942 */     this.fillY = cell.fillY;
/*  943 */     this.align = cell.align;
/*  944 */     this.expandX = cell.expandX;
/*  945 */     this.expandY = cell.expandY;
/*  946 */     this.colspan = cell.colspan;
/*  947 */     this.uniformX = cell.uniformX;
/*  948 */     this.uniformY = cell.uniformY;
/*      */   }
/*      */ 
/*      */   
/*      */   void merge(Cell cell) {
/*  953 */     if (cell == null)
/*  954 */       return;  if (cell.minWidth != null) this.minWidth = cell.minWidth; 
/*  955 */     if (cell.minHeight != null) this.minHeight = cell.minHeight; 
/*  956 */     if (cell.prefWidth != null) this.prefWidth = cell.prefWidth; 
/*  957 */     if (cell.prefHeight != null) this.prefHeight = cell.prefHeight; 
/*  958 */     if (cell.maxWidth != null) this.maxWidth = cell.maxWidth; 
/*  959 */     if (cell.maxHeight != null) this.maxHeight = cell.maxHeight; 
/*  960 */     if (cell.spaceTop != null) this.spaceTop = cell.spaceTop; 
/*  961 */     if (cell.spaceLeft != null) this.spaceLeft = cell.spaceLeft; 
/*  962 */     if (cell.spaceBottom != null) this.spaceBottom = cell.spaceBottom; 
/*  963 */     if (cell.spaceRight != null) this.spaceRight = cell.spaceRight; 
/*  964 */     if (cell.padTop != null) this.padTop = cell.padTop; 
/*  965 */     if (cell.padLeft != null) this.padLeft = cell.padLeft; 
/*  966 */     if (cell.padBottom != null) this.padBottom = cell.padBottom; 
/*  967 */     if (cell.padRight != null) this.padRight = cell.padRight; 
/*  968 */     if (cell.fillX != null) this.fillX = cell.fillX; 
/*  969 */     if (cell.fillY != null) this.fillY = cell.fillY; 
/*  970 */     if (cell.align != null) this.align = cell.align; 
/*  971 */     if (cell.expandX != null) this.expandX = cell.expandX; 
/*  972 */     if (cell.expandY != null) this.expandY = cell.expandY; 
/*  973 */     if (cell.colspan != null) this.colspan = cell.colspan; 
/*  974 */     if (cell.uniformX != null) this.uniformX = cell.uniformX; 
/*  975 */     if (cell.uniformY != null) this.uniformY = cell.uniformY; 
/*      */   }
/*      */   
/*      */   public String toString() {
/*  979 */     return (this.actor != null) ? this.actor.toString() : super.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Cell defaults() {
/*  985 */     if (files == null || files != Gdx.files) {
/*  986 */       files = Gdx.files;
/*  987 */       defaults = new Cell<Actor>();
/*  988 */       defaults.minWidth = Value.minWidth;
/*  989 */       defaults.minHeight = Value.minHeight;
/*  990 */       defaults.prefWidth = Value.prefWidth;
/*  991 */       defaults.prefHeight = Value.prefHeight;
/*  992 */       defaults.maxWidth = Value.maxWidth;
/*  993 */       defaults.maxHeight = Value.maxHeight;
/*  994 */       defaults.spaceTop = Value.zero;
/*  995 */       defaults.spaceLeft = Value.zero;
/*  996 */       defaults.spaceBottom = Value.zero;
/*  997 */       defaults.spaceRight = Value.zero;
/*  998 */       defaults.padTop = Value.zero;
/*  999 */       defaults.padLeft = Value.zero;
/* 1000 */       defaults.padBottom = Value.zero;
/* 1001 */       defaults.padRight = Value.zero;
/* 1002 */       defaults.fillX = zerof;
/* 1003 */       defaults.fillY = zerof;
/* 1004 */       defaults.align = centeri;
/* 1005 */       defaults.expandX = zeroi;
/* 1006 */       defaults.expandY = zeroi;
/* 1007 */       defaults.colspan = onei;
/* 1008 */       defaults.uniformX = null;
/* 1009 */       defaults.uniformY = null;
/*      */     } 
/* 1011 */     return defaults;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Cell.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */