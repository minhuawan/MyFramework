/*      */ package com.badlogic.gdx.scenes.scene2d.ui;
/*      */ 
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.g2d.Batch;
/*      */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*      */ import com.badlogic.gdx.math.Rectangle;
/*      */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*      */ import com.badlogic.gdx.scenes.scene2d.Group;
/*      */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.Pool;
/*      */ import com.badlogic.gdx.utils.Pools;
/*      */ import com.badlogic.gdx.utils.SnapshotArray;
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
/*      */ public class Table
/*      */   extends WidgetGroup
/*      */ {
/*   41 */   public static Color debugTableColor = new Color(0.0F, 0.0F, 1.0F, 1.0F);
/*   42 */   public static Color debugCellColor = new Color(1.0F, 0.0F, 0.0F, 1.0F);
/*   43 */   public static Color debugActorColor = new Color(0.0F, 1.0F, 0.0F, 1.0F);
/*      */   
/*   45 */   static final Pool<Cell> cellPool = new Pool<Cell>() {
/*      */       protected Cell newObject() {
/*   47 */         return new Cell<Actor>();
/*      */       }
/*      */     };
/*      */   private static float[] columnWeightedWidth;
/*      */   private static float[] rowWeightedHeight;
/*      */   private int columns;
/*      */   private int rows;
/*      */   private boolean implicitEndRow;
/*   55 */   private final Array<Cell> cells = new Array(4); private final Cell cellDefaults; private Cell rowDefaults; private boolean sizeInvalid = true; private float[] columnMinWidth; private float[] rowMinHeight; private float[] columnPrefWidth; private float[] rowPrefHeight;
/*      */   private float tableMinWidth;
/*   57 */   private final Array<Cell> columnDefaults = new Array(2);
/*      */   
/*      */   private float tableMinHeight;
/*      */   
/*      */   private float tablePrefWidth;
/*      */   
/*      */   private float tablePrefHeight;
/*      */   private float[] columnWidth;
/*      */   private float[] rowHeight;
/*      */   private float[] expandWidth;
/*      */   private float[] expandHeight;
/*   68 */   Value padTop = backgroundTop; Value padLeft = backgroundLeft; Value padBottom = backgroundBottom; Value padRight = backgroundRight;
/*   69 */   int align = 1;
/*      */   
/*   71 */   Debug debug = Debug.none;
/*      */   
/*      */   Array<DebugRect> debugRects;
/*      */   Drawable background;
/*      */   private boolean clip;
/*      */   private Skin skin;
/*      */   boolean round = true;
/*      */   
/*      */   public Table() {
/*   80 */     this((Skin)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Table(Skin skin) {
/*   86 */     this.skin = skin;
/*      */     
/*   88 */     this.cellDefaults = obtainCell();
/*      */     
/*   90 */     setTransform(false);
/*   91 */     setTouchable(Touchable.childrenOnly);
/*      */   }
/*      */   
/*      */   private Cell obtainCell() {
/*   95 */     Cell cell = (Cell)cellPool.obtain();
/*   96 */     cell.setLayout(this);
/*   97 */     return cell;
/*      */   }
/*      */   
/*      */   public void draw(Batch batch, float parentAlpha) {
/*  101 */     validate();
/*  102 */     if (isTransform()) {
/*  103 */       applyTransform(batch, computeTransform());
/*  104 */       drawBackground(batch, parentAlpha, 0.0F, 0.0F);
/*  105 */       if (this.clip) {
/*  106 */         batch.flush();
/*  107 */         float padLeft = this.padLeft.get((Actor)this), padBottom = this.padBottom.get((Actor)this);
/*  108 */         if (clipBegin(padLeft, padBottom, getWidth() - padLeft - this.padRight.get((Actor)this), 
/*  109 */             getHeight() - padBottom - this.padTop.get((Actor)this))) {
/*  110 */           drawChildren(batch, parentAlpha);
/*  111 */           batch.flush();
/*  112 */           clipEnd();
/*      */         } 
/*      */       } else {
/*  115 */         drawChildren(batch, parentAlpha);
/*  116 */       }  resetTransform(batch);
/*      */     } else {
/*  118 */       drawBackground(batch, parentAlpha, getX(), getY());
/*  119 */       super.draw(batch, parentAlpha);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
/*  126 */     if (this.background == null)
/*  127 */       return;  Color color = getColor();
/*  128 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*  129 */     this.background.draw(batch, x, y, getWidth(), getHeight());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBackground(String drawableName) {
/*  136 */     if (this.skin == null) throw new IllegalStateException("Table must have a skin set to use this method."); 
/*  137 */     setBackground(this.skin.getDrawable(drawableName));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBackground(Drawable background) {
/*  142 */     if (this.background == background)
/*  143 */       return;  float padTopOld = getPadTop(), padLeftOld = getPadLeft(), padBottomOld = getPadBottom(), padRightOld = getPadRight();
/*  144 */     this.background = background;
/*  145 */     float padTopNew = getPadTop(), padLeftNew = getPadLeft(), padBottomNew = getPadBottom(), padRightNew = getPadRight();
/*  146 */     if (padTopOld + padBottomOld != padTopNew + padBottomNew || padLeftOld + padRightOld != padLeftNew + padRightNew) {
/*  147 */       invalidateHierarchy();
/*  148 */     } else if (padTopOld != padTopNew || padLeftOld != padLeftNew || padBottomOld != padBottomNew || padRightOld != padRightNew) {
/*  149 */       invalidate();
/*      */     } 
/*      */   }
/*      */   
/*      */   public Table background(Drawable background) {
/*  154 */     setBackground(background);
/*  155 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table background(String drawableName) {
/*  160 */     setBackground(drawableName);
/*  161 */     return this;
/*      */   }
/*      */   
/*      */   public Drawable getBackground() {
/*  165 */     return this.background;
/*      */   }
/*      */   
/*      */   public Actor hit(float x, float y, boolean touchable) {
/*  169 */     if (this.clip) {
/*  170 */       if (touchable && getTouchable() == Touchable.disabled) return null; 
/*  171 */       if (x < 0.0F || x >= getWidth() || y < 0.0F || y >= getHeight()) return null; 
/*      */     } 
/*  173 */     return super.hit(x, y, touchable);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClip(boolean enabled) {
/*  179 */     this.clip = enabled;
/*  180 */     setTransform(enabled);
/*  181 */     invalidate();
/*      */   }
/*      */   
/*      */   public boolean getClip() {
/*  185 */     return this.clip;
/*      */   }
/*      */   
/*      */   public void invalidate() {
/*  189 */     this.sizeInvalid = true;
/*  190 */     super.invalidate();
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Actor> Cell<T> add(T actor) {
/*  195 */     Cell<T> cell = obtainCell();
/*  196 */     cell.actor = (Actor)actor;
/*      */ 
/*      */     
/*  199 */     if (this.implicitEndRow) {
/*  200 */       this.implicitEndRow = false;
/*  201 */       this.rows--;
/*  202 */       ((Cell)this.cells.peek()).endRow = false;
/*      */     } 
/*      */     
/*  205 */     Array<Cell> cells = this.cells;
/*  206 */     int cellCount = cells.size;
/*  207 */     if (cellCount > 0) {
/*      */       
/*  209 */       Cell lastCell = (Cell)cells.peek();
/*  210 */       if (!lastCell.endRow) {
/*  211 */         cell.column = lastCell.column + lastCell.colspan.intValue();
/*  212 */         cell.row = lastCell.row;
/*      */       } else {
/*  214 */         cell.column = 0;
/*  215 */         cell.row = lastCell.row + 1;
/*      */       } 
/*      */       
/*  218 */       if (cell.row > 0)
/*      */       {
/*  220 */         for (int i = cellCount - 1; i >= 0; i--) {
/*  221 */           Cell other = (Cell)cells.get(i);
/*  222 */           for (int column = other.column, nn = column + other.colspan.intValue(); column < nn; column++) {
/*  223 */             if (column == cell.column) {
/*  224 */               cell.cellAboveIndex = i;
/*      */               // Byte code: goto -> 224
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } else {
/*  231 */       cell.column = 0;
/*  232 */       cell.row = 0;
/*      */     } 
/*  234 */     cells.add(cell);
/*      */     
/*  236 */     cell.set(this.cellDefaults);
/*  237 */     if (cell.column < this.columnDefaults.size) {
/*  238 */       Cell columnCell = (Cell)this.columnDefaults.get(cell.column);
/*  239 */       if (columnCell != null) cell.merge(columnCell); 
/*      */     } 
/*  241 */     cell.merge(this.rowDefaults);
/*      */     
/*  243 */     if (actor != null) addActor((Actor)actor);
/*      */     
/*  245 */     return cell;
/*      */   }
/*      */   
/*      */   public void add(Actor... actors) {
/*  249 */     for (int i = 0, n = actors.length; i < n; i++) {
/*  250 */       add(actors[i]);
/*      */     }
/*      */   }
/*      */   
/*      */   public Cell<Label> add(CharSequence text) {
/*  255 */     if (this.skin == null) throw new IllegalStateException("Table must have a skin set to use this method."); 
/*  256 */     return add(new Label(text, this.skin));
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<Label> add(CharSequence text, String labelStyleName) {
/*  261 */     if (this.skin == null) throw new IllegalStateException("Table must have a skin set to use this method."); 
/*  262 */     return add(new Label(text, this.skin.<Label.LabelStyle>get(labelStyleName, Label.LabelStyle.class)));
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<Label> add(CharSequence text, String fontName, Color color) {
/*  267 */     if (this.skin == null) throw new IllegalStateException("Table must have a skin set to use this method."); 
/*  268 */     return add(new Label(text, new Label.LabelStyle(this.skin.getFont(fontName), color)));
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell<Label> add(CharSequence text, String fontName, String colorName) {
/*  273 */     if (this.skin == null) throw new IllegalStateException("Table must have a skin set to use this method."); 
/*  274 */     return add(new Label(text, new Label.LabelStyle(this.skin.getFont(fontName), this.skin.getColor(colorName))));
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell add() {
/*  279 */     return add((Actor)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Cell<Stack> stack(Actor... actors) {
/*  285 */     Stack stack = new Stack();
/*  286 */     if (actors != null)
/*  287 */       for (int i = 0, n = actors.length; i < n; i++) {
/*  288 */         stack.addActor(actors[i]);
/*      */       } 
/*  290 */     return add(stack);
/*      */   }
/*      */   
/*      */   public boolean removeActor(Actor actor) {
/*  294 */     return removeActor(actor, true);
/*      */   }
/*      */   
/*      */   public boolean removeActor(Actor actor, boolean unfocus) {
/*  298 */     if (!super.removeActor(actor, unfocus)) return false; 
/*  299 */     Cell<Actor> cell = getCell(actor);
/*  300 */     if (cell != null) cell.actor = null; 
/*  301 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearChildren() {
/*  306 */     Array<Cell> cells = this.cells;
/*  307 */     for (int i = cells.size - 1; i >= 0; i--) {
/*  308 */       Cell cell = (Cell)cells.get(i);
/*  309 */       Actor actor = cell.actor;
/*  310 */       if (actor != null) actor.remove(); 
/*      */     } 
/*  312 */     cellPool.freeAll(cells);
/*  313 */     cells.clear();
/*  314 */     this.rows = 0;
/*  315 */     this.columns = 0;
/*  316 */     if (this.rowDefaults != null) cellPool.free(this.rowDefaults); 
/*  317 */     this.rowDefaults = null;
/*  318 */     this.implicitEndRow = false;
/*      */     
/*  320 */     super.clearChildren();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset() {
/*  326 */     clearChildren();
/*  327 */     this.padTop = backgroundTop;
/*  328 */     this.padLeft = backgroundLeft;
/*  329 */     this.padBottom = backgroundBottom;
/*  330 */     this.padRight = backgroundRight;
/*  331 */     this.align = 1;
/*  332 */     debug(Debug.none);
/*  333 */     this.cellDefaults.reset();
/*  334 */     for (int i = 0, n = this.columnDefaults.size; i < n; i++) {
/*  335 */       Cell columnCell = (Cell)this.columnDefaults.get(i);
/*  336 */       if (columnCell != null) cellPool.free(columnCell); 
/*      */     } 
/*  338 */     this.columnDefaults.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Cell row() {
/*  344 */     if (this.cells.size > 0) {
/*  345 */       if (!this.implicitEndRow) endRow(); 
/*  346 */       invalidate();
/*      */     } 
/*  348 */     this.implicitEndRow = false;
/*  349 */     if (this.rowDefaults != null) cellPool.free(this.rowDefaults); 
/*  350 */     this.rowDefaults = obtainCell();
/*  351 */     this.rowDefaults.clear();
/*  352 */     return this.rowDefaults;
/*      */   }
/*      */   
/*      */   private void endRow() {
/*  356 */     Array<Cell> cells = this.cells;
/*  357 */     int rowColumns = 0;
/*  358 */     for (int i = cells.size - 1; i >= 0; i--) {
/*  359 */       Cell cell = (Cell)cells.get(i);
/*  360 */       if (cell.endRow)
/*  361 */         break;  rowColumns += cell.colspan.intValue();
/*      */     } 
/*  363 */     this.columns = Math.max(this.columns, rowColumns);
/*  364 */     this.rows++;
/*  365 */     ((Cell)cells.peek()).endRow = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Cell columnDefaults(int column) {
/*  371 */     Cell cell = (this.columnDefaults.size > column) ? (Cell)this.columnDefaults.get(column) : null;
/*  372 */     if (cell == null) {
/*  373 */       cell = obtainCell();
/*  374 */       cell.clear();
/*  375 */       if (column >= this.columnDefaults.size) {
/*  376 */         for (int i = this.columnDefaults.size; i < column; i++)
/*  377 */           this.columnDefaults.add(null); 
/*  378 */         this.columnDefaults.add(cell);
/*      */       } else {
/*  380 */         this.columnDefaults.set(column, cell);
/*      */       } 
/*  382 */     }  return cell;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Actor> Cell<T> getCell(T actor) {
/*  387 */     Array<Cell> cells = this.cells;
/*  388 */     for (int i = 0, n = cells.size; i < n; i++) {
/*  389 */       Cell<T> c = (Cell)cells.get(i);
/*  390 */       if (c.actor == actor) return c; 
/*      */     } 
/*  392 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Array<Cell> getCells() {
/*  397 */     return this.cells;
/*      */   }
/*      */   
/*      */   public float getPrefWidth() {
/*  401 */     if (this.sizeInvalid) computeSize(); 
/*  402 */     float width = this.tablePrefWidth;
/*  403 */     if (this.background != null) return Math.max(width, this.background.getMinWidth()); 
/*  404 */     return width;
/*      */   }
/*      */   
/*      */   public float getPrefHeight() {
/*  408 */     if (this.sizeInvalid) computeSize(); 
/*  409 */     float height = this.tablePrefHeight;
/*  410 */     if (this.background != null) return Math.max(height, this.background.getMinHeight()); 
/*  411 */     return height;
/*      */   }
/*      */   
/*      */   public float getMinWidth() {
/*  415 */     if (this.sizeInvalid) computeSize(); 
/*  416 */     return this.tableMinWidth;
/*      */   }
/*      */   
/*      */   public float getMinHeight() {
/*  420 */     if (this.sizeInvalid) computeSize(); 
/*  421 */     return this.tableMinHeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public Cell defaults() {
/*  426 */     return this.cellDefaults;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table pad(Value pad) {
/*  431 */     if (pad == null) throw new IllegalArgumentException("pad cannot be null."); 
/*  432 */     this.padTop = pad;
/*  433 */     this.padLeft = pad;
/*  434 */     this.padBottom = pad;
/*  435 */     this.padRight = pad;
/*  436 */     this.sizeInvalid = true;
/*  437 */     return this;
/*      */   }
/*      */   
/*      */   public Table pad(Value top, Value left, Value bottom, Value right) {
/*  441 */     if (top == null) throw new IllegalArgumentException("top cannot be null."); 
/*  442 */     if (left == null) throw new IllegalArgumentException("left cannot be null."); 
/*  443 */     if (bottom == null) throw new IllegalArgumentException("bottom cannot be null."); 
/*  444 */     if (right == null) throw new IllegalArgumentException("right cannot be null."); 
/*  445 */     this.padTop = top;
/*  446 */     this.padLeft = left;
/*  447 */     this.padBottom = bottom;
/*  448 */     this.padRight = right;
/*  449 */     this.sizeInvalid = true;
/*  450 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padTop(Value padTop) {
/*  455 */     if (padTop == null) throw new IllegalArgumentException("padTop cannot be null."); 
/*  456 */     this.padTop = padTop;
/*  457 */     this.sizeInvalid = true;
/*  458 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padLeft(Value padLeft) {
/*  463 */     if (padLeft == null) throw new IllegalArgumentException("padLeft cannot be null."); 
/*  464 */     this.padLeft = padLeft;
/*  465 */     this.sizeInvalid = true;
/*  466 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padBottom(Value padBottom) {
/*  471 */     if (padBottom == null) throw new IllegalArgumentException("padBottom cannot be null."); 
/*  472 */     this.padBottom = padBottom;
/*  473 */     this.sizeInvalid = true;
/*  474 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padRight(Value padRight) {
/*  479 */     if (padRight == null) throw new IllegalArgumentException("padRight cannot be null."); 
/*  480 */     this.padRight = padRight;
/*  481 */     this.sizeInvalid = true;
/*  482 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table pad(float pad) {
/*  487 */     pad(new Value.Fixed(pad));
/*  488 */     return this;
/*      */   }
/*      */   
/*      */   public Table pad(float top, float left, float bottom, float right) {
/*  492 */     this.padTop = new Value.Fixed(top);
/*  493 */     this.padLeft = new Value.Fixed(left);
/*  494 */     this.padBottom = new Value.Fixed(bottom);
/*  495 */     this.padRight = new Value.Fixed(right);
/*  496 */     this.sizeInvalid = true;
/*  497 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padTop(float padTop) {
/*  502 */     this.padTop = new Value.Fixed(padTop);
/*  503 */     this.sizeInvalid = true;
/*  504 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padLeft(float padLeft) {
/*  509 */     this.padLeft = new Value.Fixed(padLeft);
/*  510 */     this.sizeInvalid = true;
/*  511 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padBottom(float padBottom) {
/*  516 */     this.padBottom = new Value.Fixed(padBottom);
/*  517 */     this.sizeInvalid = true;
/*  518 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table padRight(float padRight) {
/*  523 */     this.padRight = new Value.Fixed(padRight);
/*  524 */     this.sizeInvalid = true;
/*  525 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Table align(int align) {
/*  531 */     this.align = align;
/*  532 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table center() {
/*  537 */     this.align = 1;
/*  538 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table top() {
/*  543 */     this.align |= 0x2;
/*  544 */     this.align &= 0xFFFFFFFB;
/*  545 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table left() {
/*  550 */     this.align |= 0x8;
/*  551 */     this.align &= 0xFFFFFFEF;
/*  552 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table bottom() {
/*  557 */     this.align |= 0x4;
/*  558 */     this.align &= 0xFFFFFFFD;
/*  559 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table right() {
/*  564 */     this.align |= 0x10;
/*  565 */     this.align &= 0xFFFFFFF7;
/*  566 */     return this;
/*      */   }
/*      */   
/*      */   public void setDebug(boolean enabled) {
/*  570 */     debug(enabled ? Debug.all : Debug.none);
/*      */   }
/*      */   
/*      */   public Table debug() {
/*  574 */     super.debug();
/*  575 */     return this;
/*      */   }
/*      */   
/*      */   public Table debugAll() {
/*  579 */     super.debugAll();
/*  580 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table debugTable() {
/*  585 */     super.setDebug(true);
/*  586 */     if (this.debug != Debug.table) {
/*  587 */       this.debug = Debug.table;
/*  588 */       invalidate();
/*      */     } 
/*  590 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table debugCell() {
/*  595 */     super.setDebug(true);
/*  596 */     if (this.debug != Debug.cell) {
/*  597 */       this.debug = Debug.cell;
/*  598 */       invalidate();
/*      */     } 
/*  600 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table debugActor() {
/*  605 */     super.setDebug(true);
/*  606 */     if (this.debug != Debug.actor) {
/*  607 */       this.debug = Debug.actor;
/*  608 */       invalidate();
/*      */     } 
/*  610 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Table debug(Debug debug) {
/*  615 */     super.setDebug((debug != Debug.none));
/*  616 */     if (this.debug != debug) {
/*  617 */       this.debug = debug;
/*  618 */       if (debug == Debug.none) {
/*  619 */         clearDebugRects();
/*      */       } else {
/*  621 */         invalidate();
/*      */       } 
/*  623 */     }  return this;
/*      */   }
/*      */   
/*      */   public Debug getTableDebug() {
/*  627 */     return this.debug;
/*      */   }
/*      */   
/*      */   public Value getPadTopValue() {
/*  631 */     return this.padTop;
/*      */   }
/*      */   
/*      */   public float getPadTop() {
/*  635 */     return this.padTop.get((Actor)this);
/*      */   }
/*      */   
/*      */   public Value getPadLeftValue() {
/*  639 */     return this.padLeft;
/*      */   }
/*      */   
/*      */   public float getPadLeft() {
/*  643 */     return this.padLeft.get((Actor)this);
/*      */   }
/*      */   
/*      */   public Value getPadBottomValue() {
/*  647 */     return this.padBottom;
/*      */   }
/*      */   
/*      */   public float getPadBottom() {
/*  651 */     return this.padBottom.get((Actor)this);
/*      */   }
/*      */   
/*      */   public Value getPadRightValue() {
/*  655 */     return this.padRight;
/*      */   }
/*      */   
/*      */   public float getPadRight() {
/*  659 */     return this.padRight.get((Actor)this);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPadX() {
/*  664 */     return this.padLeft.get((Actor)this) + this.padRight.get((Actor)this);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPadY() {
/*  669 */     return this.padTop.get((Actor)this) + this.padBottom.get((Actor)this);
/*      */   }
/*      */   
/*      */   public int getAlign() {
/*  673 */     return this.align;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRow(float y) {
/*  679 */     Array<Cell> cells = this.cells;
/*  680 */     int row = 0;
/*  681 */     y += getPadTop();
/*  682 */     int i = 0, n = cells.size;
/*  683 */     if (n == 0) return -1; 
/*  684 */     if (n == 1) return 0; 
/*  685 */     while (i < n) {
/*  686 */       Cell c = (Cell)cells.get(i++);
/*  687 */       if (c.actorY + c.computedPadTop < y)
/*  688 */         break;  if (c.endRow) row++; 
/*      */     } 
/*  690 */     return row;
/*      */   }
/*      */   
/*      */   public void setSkin(Skin skin) {
/*  694 */     this.skin = skin;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRound(boolean round) {
/*  699 */     this.round = round;
/*      */   }
/*      */   
/*      */   public int getRows() {
/*  703 */     return this.rows;
/*      */   }
/*      */   
/*      */   public int getColumns() {
/*  707 */     return this.columns;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRowHeight(int rowIndex) {
/*  712 */     return this.rowHeight[rowIndex];
/*      */   }
/*      */ 
/*      */   
/*      */   public float getColumnWidth(int columnIndex) {
/*  717 */     return this.columnWidth[columnIndex];
/*      */   }
/*      */   
/*      */   private float[] ensureSize(float[] array, int size) {
/*  721 */     if (array == null || array.length < size) return new float[size]; 
/*  722 */     for (int i = 0, n = array.length; i < n; i++)
/*  723 */       array[i] = 0.0F; 
/*  724 */     return array;
/*      */   }
/*      */   
/*      */   public void layout() {
/*  728 */     float width = getWidth();
/*  729 */     float height = getHeight();
/*      */     
/*  731 */     layout(0.0F, 0.0F, width, height);
/*      */     
/*  733 */     Array<Cell> cells = this.cells;
/*  734 */     if (this.round) {
/*  735 */       for (int j = 0, k = cells.size; j < k; j++) {
/*  736 */         Cell c = (Cell)cells.get(j);
/*  737 */         float actorWidth = Math.round(c.actorWidth);
/*  738 */         float actorHeight = Math.round(c.actorHeight);
/*  739 */         float actorX = Math.round(c.actorX);
/*  740 */         float actorY = height - Math.round(c.actorY) - actorHeight;
/*  741 */         c.setActorBounds(actorX, actorY, actorWidth, actorHeight);
/*  742 */         Actor actor = c.actor;
/*  743 */         if (actor != null) actor.setBounds(actorX, actorY, actorWidth, actorHeight); 
/*      */       } 
/*      */     } else {
/*  746 */       for (int j = 0, k = cells.size; j < k; j++) {
/*  747 */         Cell c = (Cell)cells.get(j);
/*  748 */         float actorHeight = c.actorHeight;
/*  749 */         float actorY = height - c.actorY - actorHeight;
/*  750 */         c.setActorY(actorY);
/*  751 */         Actor actor = c.actor;
/*  752 */         if (actor != null) actor.setBounds(c.actorX, actorY, c.actorWidth, actorHeight);
/*      */       
/*      */       } 
/*      */     } 
/*  756 */     SnapshotArray snapshotArray = getChildren();
/*  757 */     for (int i = 0, n = ((Array)snapshotArray).size; i < n; i++) {
/*  758 */       Actor child = (Actor)snapshotArray.get(i);
/*  759 */       if (child instanceof Layout) ((Layout)child).validate(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void computeSize() {
/*  764 */     this.sizeInvalid = false;
/*      */     
/*  766 */     Array<Cell> cells = this.cells;
/*  767 */     int cellCount = cells.size;
/*      */ 
/*      */     
/*  770 */     if (cellCount > 0 && !((Cell)cells.peek()).endRow) {
/*  771 */       endRow();
/*  772 */       this.implicitEndRow = true;
/*      */     } 
/*      */     
/*  775 */     int columns = this.columns, rows = this.rows;
/*  776 */     float[] columnMinWidth = this.columnMinWidth = ensureSize(this.columnMinWidth, columns);
/*  777 */     float[] rowMinHeight = this.rowMinHeight = ensureSize(this.rowMinHeight, rows);
/*  778 */     float[] columnPrefWidth = this.columnPrefWidth = ensureSize(this.columnPrefWidth, columns);
/*  779 */     float[] rowPrefHeight = this.rowPrefHeight = ensureSize(this.rowPrefHeight, rows);
/*  780 */     float[] columnWidth = this.columnWidth = ensureSize(this.columnWidth, columns);
/*  781 */     float[] rowHeight = this.rowHeight = ensureSize(this.rowHeight, rows);
/*  782 */     float[] expandWidth = this.expandWidth = ensureSize(this.expandWidth, columns);
/*  783 */     float[] expandHeight = this.expandHeight = ensureSize(this.expandHeight, rows);
/*      */     
/*  785 */     float spaceRightLast = 0.0F;
/*  786 */     for (int i = 0; i < cellCount; i++) {
/*  787 */       Cell c = (Cell)cells.get(i);
/*  788 */       int column = c.column, row = c.row, colspan = c.colspan.intValue();
/*  789 */       Actor a = c.actor;
/*      */ 
/*      */       
/*  792 */       if (c.expandY.intValue() != 0 && expandHeight[row] == 0.0F) expandHeight[row] = c.expandY.intValue(); 
/*  793 */       if (colspan == 1 && c.expandX.intValue() != 0 && expandWidth[column] == 0.0F) expandWidth[column] = c.expandX.intValue();
/*      */ 
/*      */ 
/*      */       
/*  797 */       c.computedPadLeft = c.padLeft.get(a) + ((column == 0) ? 0.0F : Math.max(0.0F, c.spaceLeft.get(a) - spaceRightLast));
/*  798 */       c.computedPadTop = c.padTop.get(a);
/*  799 */       if (c.cellAboveIndex != -1) {
/*  800 */         Cell above = (Cell)cells.get(c.cellAboveIndex);
/*  801 */         c.computedPadTop += Math.max(0.0F, c.spaceTop.get(a) - above.spaceBottom.get(a));
/*      */       } 
/*  803 */       float spaceRight = c.spaceRight.get(a);
/*  804 */       c.computedPadRight = c.padRight.get(a) + ((column + colspan == columns) ? 0.0F : spaceRight);
/*  805 */       c.computedPadBottom = c.padBottom.get(a) + ((row == rows - 1) ? 0.0F : c.spaceBottom.get(a));
/*  806 */       spaceRightLast = spaceRight;
/*      */ 
/*      */       
/*  809 */       float prefWidth = c.prefWidth.get(a);
/*  810 */       float prefHeight = c.prefHeight.get(a);
/*  811 */       float minWidth = c.minWidth.get(a);
/*  812 */       float minHeight = c.minHeight.get(a);
/*  813 */       float maxWidth = c.maxWidth.get(a);
/*  814 */       float maxHeight = c.maxHeight.get(a);
/*  815 */       if (prefWidth < minWidth) prefWidth = minWidth; 
/*  816 */       if (prefHeight < minHeight) prefHeight = minHeight; 
/*  817 */       if (maxWidth > 0.0F && prefWidth > maxWidth) prefWidth = maxWidth; 
/*  818 */       if (maxHeight > 0.0F && prefHeight > maxHeight) prefHeight = maxHeight;
/*      */       
/*  820 */       if (colspan == 1) {
/*  821 */         float f = c.computedPadLeft + c.computedPadRight;
/*  822 */         columnPrefWidth[column] = Math.max(columnPrefWidth[column], prefWidth + f);
/*  823 */         columnMinWidth[column] = Math.max(columnMinWidth[column], minWidth + f);
/*      */       } 
/*  825 */       float f1 = c.computedPadTop + c.computedPadBottom;
/*  826 */       rowPrefHeight[row] = Math.max(rowPrefHeight[row], prefHeight + f1);
/*  827 */       rowMinHeight[row] = Math.max(rowMinHeight[row], minHeight + f1);
/*      */     } 
/*      */     
/*  830 */     float uniformMinWidth = 0.0F, uniformMinHeight = 0.0F;
/*  831 */     float uniformPrefWidth = 0.0F, uniformPrefHeight = 0.0F; int j;
/*  832 */     label127: for (j = 0; j < cellCount; j++) {
/*  833 */       Cell c = (Cell)cells.get(j);
/*  834 */       int column = c.column;
/*      */ 
/*      */       
/*  837 */       int expandX = c.expandX.intValue();
/*      */       
/*  839 */       if (expandX != 0) {
/*  840 */         int nn = column + c.colspan.intValue(); int ii;
/*  841 */         for (ii = column; ii < nn; ii++)
/*  842 */         { if (expandWidth[ii] != 0.0F)
/*  843 */             continue label127;  }  for (ii = column; ii < nn; ii++) {
/*  844 */           expandWidth[ii] = expandX;
/*      */         }
/*      */       } 
/*      */       
/*  848 */       if (c.uniformX == Boolean.TRUE && c.colspan.intValue() == 1) {
/*  849 */         float f = c.computedPadLeft + c.computedPadRight;
/*  850 */         uniformMinWidth = Math.max(uniformMinWidth, columnMinWidth[column] - f);
/*  851 */         uniformPrefWidth = Math.max(uniformPrefWidth, columnPrefWidth[column] - f);
/*      */       } 
/*  853 */       if (c.uniformY == Boolean.TRUE) {
/*  854 */         float f = c.computedPadTop + c.computedPadBottom;
/*  855 */         uniformMinHeight = Math.max(uniformMinHeight, rowMinHeight[c.row] - f);
/*  856 */         uniformPrefHeight = Math.max(uniformPrefHeight, rowPrefHeight[c.row] - f);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  861 */     if (uniformPrefWidth > 0.0F || uniformPrefHeight > 0.0F) {
/*  862 */       for (j = 0; j < cellCount; j++) {
/*  863 */         Cell c = (Cell)cells.get(j);
/*  864 */         if (uniformPrefWidth > 0.0F && c.uniformX == Boolean.TRUE && c.colspan.intValue() == 1) {
/*  865 */           float f = c.computedPadLeft + c.computedPadRight;
/*  866 */           columnMinWidth[c.column] = uniformMinWidth + f;
/*  867 */           columnPrefWidth[c.column] = uniformPrefWidth + f;
/*      */         } 
/*  869 */         if (uniformPrefHeight > 0.0F && c.uniformY == Boolean.TRUE) {
/*  870 */           float f = c.computedPadTop + c.computedPadBottom;
/*  871 */           rowMinHeight[c.row] = uniformMinHeight + f;
/*  872 */           rowPrefHeight[c.row] = uniformPrefHeight + f;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  878 */     for (j = 0; j < cellCount; j++) {
/*  879 */       Cell c = (Cell)cells.get(j);
/*  880 */       int colspan = c.colspan.intValue();
/*  881 */       if (colspan != 1) {
/*  882 */         int column = c.column;
/*      */         
/*  884 */         Actor a = c.actor;
/*  885 */         float minWidth = c.minWidth.get(a);
/*  886 */         float prefWidth = c.prefWidth.get(a);
/*  887 */         float maxWidth = c.maxWidth.get(a);
/*  888 */         if (prefWidth < minWidth) prefWidth = minWidth; 
/*  889 */         if (maxWidth > 0.0F && prefWidth > maxWidth) prefWidth = maxWidth;
/*      */         
/*  891 */         float spannedMinWidth = -(c.computedPadLeft + c.computedPadRight), spannedPrefWidth = spannedMinWidth;
/*  892 */         float totalExpandWidth = 0.0F;
/*  893 */         for (int ii = column, nn = ii + colspan; ii < nn; ii++) {
/*  894 */           spannedMinWidth += columnMinWidth[ii];
/*  895 */           spannedPrefWidth += columnPrefWidth[ii];
/*  896 */           totalExpandWidth += expandWidth[ii];
/*      */         } 
/*      */         
/*  899 */         float extraMinWidth = Math.max(0.0F, minWidth - spannedMinWidth);
/*  900 */         float extraPrefWidth = Math.max(0.0F, prefWidth - spannedPrefWidth);
/*  901 */         for (int k = column, m = k + colspan; k < m; k++) {
/*  902 */           float ratio = (totalExpandWidth == 0.0F) ? (1.0F / colspan) : (expandWidth[k] / totalExpandWidth);
/*  903 */           columnMinWidth[k] = columnMinWidth[k] + extraMinWidth * ratio;
/*  904 */           columnPrefWidth[k] = columnPrefWidth[k] + extraPrefWidth * ratio;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  909 */     this.tableMinWidth = 0.0F;
/*  910 */     this.tableMinHeight = 0.0F;
/*  911 */     this.tablePrefWidth = 0.0F;
/*  912 */     this.tablePrefHeight = 0.0F;
/*  913 */     for (j = 0; j < columns; j++) {
/*  914 */       this.tableMinWidth += columnMinWidth[j];
/*  915 */       this.tablePrefWidth += columnPrefWidth[j];
/*      */     } 
/*  917 */     for (j = 0; j < rows; j++) {
/*  918 */       this.tableMinHeight += rowMinHeight[j];
/*  919 */       this.tablePrefHeight += Math.max(rowMinHeight[j], rowPrefHeight[j]);
/*      */     } 
/*  921 */     float hpadding = this.padLeft.get((Actor)this) + this.padRight.get((Actor)this);
/*  922 */     float vpadding = this.padTop.get((Actor)this) + this.padBottom.get((Actor)this);
/*  923 */     this.tableMinWidth += hpadding;
/*  924 */     this.tableMinHeight += vpadding;
/*  925 */     this.tablePrefWidth = Math.max(this.tablePrefWidth + hpadding, this.tableMinWidth);
/*  926 */     this.tablePrefHeight = Math.max(this.tablePrefHeight + vpadding, this.tableMinHeight);
/*      */   }
/*      */ 
/*      */   
/*      */   private void layout(float layoutX, float layoutY, float layoutWidth, float layoutHeight) {
/*      */     float[] columnWeightedWidth, rowWeightedHeight;
/*  932 */     Array<Cell> cells = this.cells;
/*  933 */     int cellCount = cells.size;
/*      */     
/*  935 */     if (this.sizeInvalid) computeSize();
/*      */     
/*  937 */     float padLeft = this.padLeft.get((Actor)this);
/*  938 */     float hpadding = padLeft + this.padRight.get((Actor)this);
/*  939 */     float padTop = this.padTop.get((Actor)this);
/*  940 */     float vpadding = padTop + this.padBottom.get((Actor)this);
/*      */     
/*  942 */     int columns = this.columns, rows = this.rows;
/*  943 */     float[] expandWidth = this.expandWidth, expandHeight = this.expandHeight;
/*  944 */     float[] columnWidth = this.columnWidth, rowHeight = this.rowHeight;
/*      */     
/*  946 */     float totalExpandWidth = 0.0F, totalExpandHeight = 0.0F; int i;
/*  947 */     for (i = 0; i < columns; i++)
/*  948 */       totalExpandWidth += expandWidth[i]; 
/*  949 */     for (i = 0; i < rows; i++) {
/*  950 */       totalExpandHeight += expandHeight[i];
/*      */     }
/*      */ 
/*      */     
/*  954 */     float totalGrowWidth = this.tablePrefWidth - this.tableMinWidth;
/*  955 */     if (totalGrowWidth == 0.0F) {
/*  956 */       columnWeightedWidth = this.columnMinWidth;
/*      */     } else {
/*  958 */       float extraWidth = Math.min(totalGrowWidth, Math.max(0.0F, layoutWidth - this.tableMinWidth));
/*  959 */       columnWeightedWidth = Table.columnWeightedWidth = ensureSize(Table.columnWeightedWidth, columns);
/*  960 */       float[] columnMinWidth = this.columnMinWidth, columnPrefWidth = this.columnPrefWidth;
/*  961 */       for (int n = 0; n < columns; n++) {
/*  962 */         float growWidth = columnPrefWidth[n] - columnMinWidth[n];
/*  963 */         float growRatio = growWidth / totalGrowWidth;
/*  964 */         columnWeightedWidth[n] = columnMinWidth[n] + extraWidth * growRatio;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  969 */     float totalGrowHeight = this.tablePrefHeight - this.tableMinHeight;
/*  970 */     if (totalGrowHeight == 0.0F) {
/*  971 */       rowWeightedHeight = this.rowMinHeight;
/*      */     } else {
/*  973 */       rowWeightedHeight = Table.rowWeightedHeight = ensureSize(Table.rowWeightedHeight, rows);
/*  974 */       float extraHeight = Math.min(totalGrowHeight, Math.max(0.0F, layoutHeight - this.tableMinHeight));
/*  975 */       float[] rowMinHeight = this.rowMinHeight, rowPrefHeight = this.rowPrefHeight;
/*  976 */       for (int n = 0; n < rows; n++) {
/*  977 */         float growHeight = rowPrefHeight[n] - rowMinHeight[n];
/*  978 */         float growRatio = growHeight / totalGrowHeight;
/*  979 */         rowWeightedHeight[n] = rowMinHeight[n] + extraHeight * growRatio;
/*      */       } 
/*      */     } 
/*      */     
/*      */     int j;
/*  984 */     for (j = 0; j < cellCount; j++) {
/*  985 */       Cell c = (Cell)cells.get(j);
/*  986 */       int column = c.column, row = c.row;
/*  987 */       Actor a = c.actor;
/*      */       
/*  989 */       float spannedWeightedWidth = 0.0F;
/*  990 */       int colspan = c.colspan.intValue();
/*  991 */       for (int ii = column, nn = ii + colspan; ii < nn; ii++)
/*  992 */         spannedWeightedWidth += columnWeightedWidth[ii]; 
/*  993 */       float weightedHeight = rowWeightedHeight[row];
/*      */       
/*  995 */       float prefWidth = c.prefWidth.get(a);
/*  996 */       float prefHeight = c.prefHeight.get(a);
/*  997 */       float minWidth = c.minWidth.get(a);
/*  998 */       float minHeight = c.minHeight.get(a);
/*  999 */       float maxWidth = c.maxWidth.get(a);
/* 1000 */       float maxHeight = c.maxHeight.get(a);
/* 1001 */       if (prefWidth < minWidth) prefWidth = minWidth; 
/* 1002 */       if (prefHeight < minHeight) prefHeight = minHeight; 
/* 1003 */       if (maxWidth > 0.0F && prefWidth > maxWidth) prefWidth = maxWidth; 
/* 1004 */       if (maxHeight > 0.0F && prefHeight > maxHeight) prefHeight = maxHeight;
/*      */       
/* 1006 */       c.actorWidth = Math.min(spannedWeightedWidth - c.computedPadLeft - c.computedPadRight, prefWidth);
/* 1007 */       c.actorHeight = Math.min(weightedHeight - c.computedPadTop - c.computedPadBottom, prefHeight);
/*      */       
/* 1009 */       if (colspan == 1) columnWidth[column] = Math.max(columnWidth[column], spannedWeightedWidth); 
/* 1010 */       rowHeight[row] = Math.max(rowHeight[row], weightedHeight);
/*      */     } 
/*      */ 
/*      */     
/* 1014 */     if (totalExpandWidth > 0.0F) {
/* 1015 */       float extra = layoutWidth - hpadding;
/* 1016 */       for (int n = 0; n < columns; n++)
/* 1017 */         extra -= columnWidth[n]; 
/* 1018 */       float used = 0.0F;
/* 1019 */       int lastIndex = 0;
/* 1020 */       for (int i1 = 0; i1 < columns; i1++) {
/* 1021 */         if (expandWidth[i1] != 0.0F) {
/* 1022 */           float amount = extra * expandWidth[i1] / totalExpandWidth;
/* 1023 */           columnWidth[i1] = columnWidth[i1] + amount;
/* 1024 */           used += amount;
/* 1025 */           lastIndex = i1;
/*      */         } 
/* 1027 */       }  columnWidth[lastIndex] = columnWidth[lastIndex] + extra - used;
/*      */     } 
/* 1029 */     if (totalExpandHeight > 0.0F) {
/* 1030 */       float extra = layoutHeight - vpadding;
/* 1031 */       for (int n = 0; n < rows; n++)
/* 1032 */         extra -= rowHeight[n]; 
/* 1033 */       float used = 0.0F;
/* 1034 */       int lastIndex = 0;
/* 1035 */       for (int i1 = 0; i1 < rows; i1++) {
/* 1036 */         if (expandHeight[i1] != 0.0F) {
/* 1037 */           float amount = extra * expandHeight[i1] / totalExpandHeight;
/* 1038 */           rowHeight[i1] = rowHeight[i1] + amount;
/* 1039 */           used += amount;
/* 1040 */           lastIndex = i1;
/*      */         } 
/* 1042 */       }  rowHeight[lastIndex] = rowHeight[lastIndex] + extra - used;
/*      */     } 
/*      */ 
/*      */     
/* 1046 */     for (j = 0; j < cellCount; j++) {
/* 1047 */       Cell c = (Cell)cells.get(j);
/* 1048 */       int colspan = c.colspan.intValue();
/* 1049 */       if (colspan != 1) {
/*      */         
/* 1051 */         float extraWidth = 0.0F; int column, nn;
/* 1052 */         for (column = c.column, nn = column + colspan; column < nn; column++)
/* 1053 */           extraWidth += columnWeightedWidth[column] - columnWidth[column]; 
/* 1054 */         extraWidth -= Math.max(0.0F, c.computedPadLeft + c.computedPadRight);
/*      */         
/* 1056 */         extraWidth /= colspan;
/* 1057 */         if (extraWidth > 0.0F) {
/* 1058 */           for (column = c.column, nn = column + colspan; column < nn; column++) {
/* 1059 */             columnWidth[column] = columnWidth[column] + extraWidth;
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/* 1064 */     float tableWidth = hpadding, tableHeight = vpadding; int k;
/* 1065 */     for (k = 0; k < columns; k++)
/* 1066 */       tableWidth += columnWidth[k]; 
/* 1067 */     for (k = 0; k < rows; k++) {
/* 1068 */       tableHeight += rowHeight[k];
/*      */     }
/*      */     
/* 1071 */     int align = this.align;
/* 1072 */     float x = layoutX + padLeft;
/* 1073 */     if ((align & 0x10) != 0) {
/* 1074 */       x += layoutWidth - tableWidth;
/* 1075 */     } else if ((align & 0x8) == 0) {
/* 1076 */       x += (layoutWidth - tableWidth) / 2.0F;
/*      */     } 
/* 1078 */     float y = layoutY + padTop;
/* 1079 */     if ((align & 0x4) != 0) {
/* 1080 */       y += layoutHeight - tableHeight;
/* 1081 */     } else if ((align & 0x2) == 0) {
/* 1082 */       y += (layoutHeight - tableHeight) / 2.0F;
/*      */     } 
/*      */     
/* 1085 */     float currentX = x, currentY = y; int m;
/* 1086 */     for (m = 0; m < cellCount; m++) {
/* 1087 */       Cell c = (Cell)cells.get(m);
/*      */       
/* 1089 */       float spannedCellWidth = 0.0F;
/* 1090 */       for (int column = c.column, nn = column + c.colspan.intValue(); column < nn; column++)
/* 1091 */         spannedCellWidth += columnWidth[column]; 
/* 1092 */       spannedCellWidth -= c.computedPadLeft + c.computedPadRight;
/*      */       
/* 1094 */       currentX += c.computedPadLeft;
/*      */       
/* 1096 */       float fillX = c.fillX.floatValue(), fillY = c.fillY.floatValue();
/* 1097 */       if (fillX > 0.0F) {
/* 1098 */         c.actorWidth = Math.max(spannedCellWidth * fillX, c.minWidth.get(c.actor));
/* 1099 */         float maxWidth = c.maxWidth.get(c.actor);
/* 1100 */         if (maxWidth > 0.0F) c.actorWidth = Math.min(c.actorWidth, maxWidth); 
/*      */       } 
/* 1102 */       if (fillY > 0.0F) {
/* 1103 */         c.actorHeight = Math.max(rowHeight[c.row] * fillY - c.computedPadTop - c.computedPadBottom, c.minHeight.get(c.actor));
/* 1104 */         float maxHeight = c.maxHeight.get(c.actor);
/* 1105 */         if (maxHeight > 0.0F) c.actorHeight = Math.min(c.actorHeight, maxHeight);
/*      */       
/*      */       } 
/* 1108 */       align = c.align.intValue();
/* 1109 */       if ((align & 0x8) != 0) {
/* 1110 */         c.actorX = currentX;
/* 1111 */       } else if ((align & 0x10) != 0) {
/* 1112 */         c.actorX = currentX + spannedCellWidth - c.actorWidth;
/*      */       } else {
/* 1114 */         c.actorX = currentX + (spannedCellWidth - c.actorWidth) / 2.0F;
/*      */       } 
/* 1116 */       if ((align & 0x2) != 0) {
/* 1117 */         c.actorY = currentY + c.computedPadTop;
/* 1118 */       } else if ((align & 0x4) != 0) {
/* 1119 */         c.actorY = currentY + rowHeight[c.row] - c.actorHeight - c.computedPadBottom;
/*      */       } else {
/* 1121 */         c.actorY = currentY + (rowHeight[c.row] - c.actorHeight + c.computedPadTop - c.computedPadBottom) / 2.0F;
/*      */       } 
/* 1123 */       if (c.endRow) {
/* 1124 */         currentX = x;
/* 1125 */         currentY += rowHeight[c.row];
/*      */       } else {
/* 1127 */         currentX += spannedCellWidth + c.computedPadRight;
/*      */       } 
/*      */     } 
/*      */     
/* 1131 */     if (this.debug == Debug.none)
/* 1132 */       return;  clearDebugRects();
/* 1133 */     currentX = x;
/* 1134 */     currentY = y;
/* 1135 */     if (this.debug == Debug.table || this.debug == Debug.all) {
/* 1136 */       addDebugRect(layoutX, layoutY, layoutWidth, layoutHeight, debugTableColor);
/* 1137 */       addDebugRect(x, y, tableWidth - hpadding, tableHeight - vpadding, debugTableColor);
/*      */     } 
/* 1139 */     for (m = 0; m < cellCount; m++) {
/* 1140 */       Cell c = (Cell)cells.get(m);
/*      */ 
/*      */       
/* 1143 */       if (this.debug == Debug.actor || this.debug == Debug.all) {
/* 1144 */         addDebugRect(c.actorX, c.actorY, c.actorWidth, c.actorHeight, debugActorColor);
/*      */       }
/*      */       
/* 1147 */       float spannedCellWidth = 0.0F;
/* 1148 */       for (int column = c.column, nn = column + c.colspan.intValue(); column < nn; column++)
/* 1149 */         spannedCellWidth += columnWidth[column]; 
/* 1150 */       spannedCellWidth -= c.computedPadLeft + c.computedPadRight;
/* 1151 */       currentX += c.computedPadLeft;
/* 1152 */       if (this.debug == Debug.cell || this.debug == Debug.all) {
/* 1153 */         addDebugRect(currentX, currentY + c.computedPadTop, spannedCellWidth, rowHeight[c.row] - c.computedPadTop - c.computedPadBottom, debugCellColor);
/*      */       }
/*      */ 
/*      */       
/* 1157 */       if (c.endRow) {
/* 1158 */         currentX = x;
/* 1159 */         currentY += rowHeight[c.row];
/*      */       } else {
/* 1161 */         currentX += spannedCellWidth + c.computedPadRight;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private void clearDebugRects() {
/* 1166 */     if (this.debugRects == null)
/* 1167 */       return;  DebugRect.pool.freeAll(this.debugRects);
/* 1168 */     this.debugRects.clear();
/*      */   }
/*      */   
/*      */   private void addDebugRect(float x, float y, float w, float h, Color color) {
/* 1172 */     if (this.debugRects == null) this.debugRects = new Array(); 
/* 1173 */     DebugRect rect = (DebugRect)DebugRect.pool.obtain();
/* 1174 */     rect.color = color;
/* 1175 */     rect.set(x, getHeight() - y - h, w, h);
/* 1176 */     this.debugRects.add(rect);
/*      */   }
/*      */   
/*      */   public void drawDebug(ShapeRenderer shapes) {
/* 1180 */     if (isTransform()) {
/* 1181 */       applyTransform(shapes, computeTransform());
/* 1182 */       drawDebugRects(shapes);
/* 1183 */       if (this.clip) {
/* 1184 */         shapes.flush();
/* 1185 */         float x = 0.0F, y = 0.0F, width = getWidth(), height = getHeight();
/* 1186 */         if (this.background != null) {
/* 1187 */           x = this.padLeft.get((Actor)this);
/* 1188 */           y = this.padBottom.get((Actor)this);
/* 1189 */           width -= x + this.padRight.get((Actor)this);
/* 1190 */           height -= y + this.padTop.get((Actor)this);
/*      */         } 
/* 1192 */         if (clipBegin(x, y, width, height)) {
/* 1193 */           drawDebugChildren(shapes);
/* 1194 */           clipEnd();
/*      */         } 
/*      */       } else {
/* 1197 */         drawDebugChildren(shapes);
/* 1198 */       }  resetTransform(shapes);
/*      */     } else {
/* 1200 */       drawDebugRects(shapes);
/* 1201 */       super.drawDebug(shapes);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void drawDebugBounds(ShapeRenderer shapes) {}
/*      */   
/*      */   private void drawDebugRects(ShapeRenderer shapes) {
/* 1209 */     if (this.debugRects == null || !getDebug())
/* 1210 */       return;  shapes.set(ShapeRenderer.ShapeType.Line);
/* 1211 */     shapes.setColor(getStage().getDebugColor());
/* 1212 */     float x = 0.0F, y = 0.0F;
/* 1213 */     if (!isTransform()) {
/* 1214 */       x = getX();
/* 1215 */       y = getY();
/*      */     } 
/* 1217 */     for (int i = 0, n = this.debugRects.size; i < n; i++) {
/* 1218 */       DebugRect debugRect = (DebugRect)this.debugRects.get(i);
/* 1219 */       shapes.setColor(debugRect.color);
/* 1220 */       shapes.rect(x + debugRect.x, y + debugRect.y, debugRect.width, debugRect.height);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Skin getSkin() {
/* 1226 */     return this.skin;
/*      */   }
/*      */   
/*      */   public static class DebugRect
/*      */     extends Rectangle {
/* 1231 */     static Pool<DebugRect> pool = Pools.get(DebugRect.class);
/*      */     Color color;
/*      */   }
/*      */   
/*      */   public enum Debug
/*      */   {
/* 1237 */     none, all, table, cell, actor;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1242 */   public static Value backgroundTop = new Value() {
/*      */       public float get(Actor context) {
/* 1244 */         Drawable background = ((Table)context).background;
/* 1245 */         return (background == null) ? 0.0F : background.getTopHeight();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/* 1251 */   public static Value backgroundLeft = new Value() {
/*      */       public float get(Actor context) {
/* 1253 */         Drawable background = ((Table)context).background;
/* 1254 */         return (background == null) ? 0.0F : background.getLeftWidth();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/* 1260 */   public static Value backgroundBottom = new Value() {
/*      */       public float get(Actor context) {
/* 1262 */         Drawable background = ((Table)context).background;
/* 1263 */         return (background == null) ? 0.0F : background.getBottomHeight();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/* 1269 */   public static Value backgroundRight = new Value() {
/*      */       public float get(Actor context) {
/* 1271 */         Drawable background = ((Table)context).background;
/* 1272 */         return (background == null) ? 0.0F : background.getRightWidth();
/*      */       }
/*      */     };
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Table.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */