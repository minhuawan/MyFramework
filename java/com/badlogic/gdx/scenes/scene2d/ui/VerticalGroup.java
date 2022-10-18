/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*     */ import com.badlogic.gdx.utils.FloatArray;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
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
/*     */ public class VerticalGroup
/*     */   extends WidgetGroup
/*     */ {
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   private float lastPrefWidth;
/*     */   private boolean sizeInvalid = true;
/*     */   private FloatArray columnSizes;
/*  43 */   private int align = 2; private int columnAlign; private boolean reverse; private boolean round = true; private boolean wrap;
/*     */   private boolean expand;
/*     */   private float space;
/*     */   
/*     */   public VerticalGroup() {
/*  48 */     setTouchable(Touchable.childrenOnly);
/*     */   }
/*     */   private float wrapSpace; private float fill; private float padTop; private float padLeft; private float padBottom; private float padRight;
/*     */   public void invalidate() {
/*  52 */     super.invalidate();
/*  53 */     this.sizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void computeSize() {
/*  57 */     this.sizeInvalid = false;
/*  58 */     SnapshotArray<Actor> children = getChildren();
/*  59 */     int n = children.size;
/*  60 */     this.prefWidth = 0.0F;
/*  61 */     if (this.wrap) {
/*  62 */       this.prefHeight = 0.0F;
/*  63 */       if (this.columnSizes == null) {
/*  64 */         this.columnSizes = new FloatArray();
/*     */       } else {
/*  66 */         this.columnSizes.clear();
/*  67 */       }  FloatArray columnSizes = this.columnSizes;
/*  68 */       float space = this.space, wrapSpace = this.wrapSpace;
/*  69 */       float pad = this.padTop + this.padBottom, groupHeight = getHeight() - pad, x = 0.0F, y = 0.0F, columnWidth = 0.0F;
/*  70 */       int i = 0, incr = 1;
/*  71 */       if (this.reverse) {
/*  72 */         i = n - 1;
/*  73 */         n = -1;
/*  74 */         incr = -1;
/*     */       } 
/*  76 */       for (; i != n; i += incr) {
/*  77 */         float width, height; Actor child = (Actor)children.get(i);
/*     */ 
/*     */         
/*  80 */         if (child instanceof Layout) {
/*  81 */           Layout layout = (Layout)child;
/*  82 */           width = layout.getPrefWidth();
/*  83 */           height = layout.getPrefHeight();
/*     */         } else {
/*  85 */           width = child.getWidth();
/*  86 */           height = child.getHeight();
/*     */         } 
/*     */         
/*  89 */         float incrY = height + ((y > 0.0F) ? space : 0.0F);
/*  90 */         if (y + incrY > groupHeight && y > 0.0F) {
/*  91 */           columnSizes.add(y);
/*  92 */           columnSizes.add(columnWidth);
/*  93 */           this.prefHeight = Math.max(this.prefHeight, y + pad);
/*  94 */           if (x > 0.0F) x += wrapSpace; 
/*  95 */           x += columnWidth;
/*  96 */           columnWidth = 0.0F;
/*  97 */           y = 0.0F;
/*  98 */           incrY = height;
/*     */         } 
/* 100 */         y += incrY;
/* 101 */         columnWidth = Math.max(columnWidth, width);
/*     */       } 
/* 103 */       columnSizes.add(y);
/* 104 */       columnSizes.add(columnWidth);
/* 105 */       this.prefHeight = Math.max(this.prefHeight, y + pad);
/* 106 */       if (x > 0.0F) x += wrapSpace; 
/* 107 */       this.prefWidth = Math.max(this.prefWidth, x + columnWidth);
/*     */     } else {
/* 109 */       this.prefHeight = this.padTop + this.padBottom + this.space * (n - 1);
/* 110 */       for (int i = 0; i < n; i++) {
/* 111 */         Actor child = (Actor)children.get(i);
/* 112 */         if (child instanceof Layout) {
/* 113 */           Layout layout = (Layout)child;
/* 114 */           this.prefWidth = Math.max(this.prefWidth, layout.getPrefWidth());
/* 115 */           this.prefHeight += layout.getPrefHeight();
/*     */         } else {
/* 117 */           this.prefWidth = Math.max(this.prefWidth, child.getWidth());
/* 118 */           this.prefHeight += child.getHeight();
/*     */         } 
/*     */       } 
/*     */     } 
/* 122 */     this.prefWidth += this.padLeft + this.padRight;
/* 123 */     if (this.round) {
/* 124 */       this.prefWidth = Math.round(this.prefWidth);
/* 125 */       this.prefHeight = Math.round(this.prefHeight);
/*     */     } 
/*     */   }
/*     */   public void layout() {
/*     */     float startX;
/* 130 */     if (this.sizeInvalid) computeSize();
/*     */     
/* 132 */     if (this.wrap) {
/* 133 */       layoutWrapped();
/*     */       
/*     */       return;
/*     */     } 
/* 137 */     boolean round = this.round;
/* 138 */     int align = this.align;
/* 139 */     float space = this.space, padLeft = this.padLeft, fill = this.fill;
/* 140 */     float columnWidth = (this.expand ? getWidth() : this.prefWidth) - padLeft - this.padRight, y = this.prefHeight - this.padTop + space;
/*     */     
/* 142 */     if ((align & 0x2) != 0) {
/* 143 */       y += getHeight() - this.prefHeight;
/* 144 */     } else if ((align & 0x4) == 0) {
/* 145 */       y += (getHeight() - this.prefHeight) / 2.0F;
/*     */     } 
/*     */     
/* 148 */     if ((align & 0x8) != 0) {
/* 149 */       startX = padLeft;
/* 150 */     } else if ((align & 0x10) != 0) {
/* 151 */       startX = getWidth() - this.padRight - columnWidth;
/*     */     } else {
/* 153 */       startX = padLeft + (getWidth() - padLeft - this.padRight - columnWidth) / 2.0F;
/*     */     } 
/* 155 */     align = this.columnAlign;
/*     */     
/* 157 */     SnapshotArray<Actor> children = getChildren();
/* 158 */     int i = 0, n = children.size, incr = 1;
/* 159 */     if (this.reverse) {
/* 160 */       i = n - 1;
/* 161 */       n = -1;
/* 162 */       incr = -1;
/*     */     } 
/* 164 */     for (int r = 0; i != n; i += incr) {
/* 165 */       float f1, height; Actor child = (Actor)children.get(i);
/*     */ 
/*     */       
/* 168 */       Layout layout = null;
/* 169 */       if (child instanceof Layout) {
/* 170 */         layout = (Layout)child;
/* 171 */         f1 = layout.getPrefWidth();
/* 172 */         height = layout.getPrefHeight();
/*     */       } else {
/* 174 */         f1 = child.getWidth();
/* 175 */         height = child.getHeight();
/*     */       } 
/*     */       
/* 178 */       if (fill > 0.0F) f1 = columnWidth * fill;
/*     */       
/* 180 */       if (layout != null) {
/* 181 */         f1 = Math.max(f1, layout.getMinWidth());
/* 182 */         float maxWidth = layout.getMaxWidth();
/* 183 */         if (maxWidth > 0.0F && f1 > maxWidth) f1 = maxWidth;
/*     */       
/*     */       } 
/* 186 */       float x = startX;
/* 187 */       if ((align & 0x10) != 0) {
/* 188 */         x += columnWidth - f1;
/* 189 */       } else if ((align & 0x8) == 0) {
/* 190 */         x += (columnWidth - f1) / 2.0F;
/*     */       } 
/* 192 */       y -= height + space;
/* 193 */       if (round) {
/* 194 */         child.setBounds(Math.round(x), Math.round(y), Math.round(f1), Math.round(height));
/*     */       } else {
/* 196 */         child.setBounds(x, y, f1, height);
/*     */       } 
/* 198 */       if (layout != null) layout.validate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void layoutWrapped() {
/* 203 */     float prefWidth = getPrefWidth();
/* 204 */     if (prefWidth != this.lastPrefWidth) {
/* 205 */       this.lastPrefWidth = prefWidth;
/* 206 */       invalidateHierarchy();
/*     */     } 
/*     */     
/* 209 */     int align = this.align;
/* 210 */     boolean round = this.round;
/* 211 */     float space = this.space, padLeft = this.padLeft, fill = this.fill, wrapSpace = this.wrapSpace;
/* 212 */     float maxHeight = this.prefHeight - this.padTop - this.padBottom;
/* 213 */     float columnX = padLeft, groupHeight = getHeight();
/* 214 */     float yStart = this.prefHeight - this.padTop + space, y = 0.0F, columnWidth = 0.0F;
/*     */     
/* 216 */     if ((align & 0x10) != 0) {
/* 217 */       columnX += getWidth() - prefWidth;
/* 218 */     } else if ((align & 0x8) == 0) {
/* 219 */       columnX += (getWidth() - prefWidth) / 2.0F;
/*     */     } 
/* 221 */     if ((align & 0x2) != 0) {
/* 222 */       yStart += groupHeight - this.prefHeight;
/* 223 */     } else if ((align & 0x4) == 0) {
/* 224 */       yStart += (groupHeight - this.prefHeight) / 2.0F;
/*     */     } 
/* 226 */     groupHeight -= this.padTop;
/* 227 */     align = this.columnAlign;
/*     */     
/* 229 */     FloatArray columnSizes = this.columnSizes;
/* 230 */     SnapshotArray<Actor> children = getChildren();
/* 231 */     int i = 0, n = children.size, incr = 1;
/* 232 */     if (this.reverse) {
/* 233 */       i = n - 1;
/* 234 */       n = -1;
/* 235 */       incr = -1;
/*     */     } 
/* 237 */     for (int r = 0; i != n; i += incr) {
/* 238 */       float f1, height; Actor child = (Actor)children.get(i);
/*     */ 
/*     */       
/* 241 */       Layout layout = null;
/* 242 */       if (child instanceof Layout) {
/* 243 */         layout = (Layout)child;
/* 244 */         f1 = layout.getPrefWidth();
/* 245 */         height = layout.getPrefHeight();
/*     */       } else {
/* 247 */         f1 = child.getWidth();
/* 248 */         height = child.getHeight();
/*     */       } 
/*     */       
/* 251 */       if (y - height - space < this.padBottom || r == 0) {
/* 252 */         y = yStart;
/* 253 */         if ((align & 0x4) != 0) {
/* 254 */           y -= maxHeight - columnSizes.get(r);
/* 255 */         } else if ((align & 0x2) == 0) {
/* 256 */           y -= (maxHeight - columnSizes.get(r)) / 2.0F;
/* 257 */         }  if (r > 0) {
/* 258 */           columnX += wrapSpace;
/* 259 */           columnX += columnWidth;
/*     */         } 
/* 261 */         columnWidth = columnSizes.get(r + 1);
/* 262 */         r += 2;
/*     */       } 
/*     */       
/* 265 */       if (fill > 0.0F) f1 = columnWidth * fill;
/*     */       
/* 267 */       if (layout != null) {
/* 268 */         f1 = Math.max(f1, layout.getMinWidth());
/* 269 */         float maxWidth = layout.getMaxWidth();
/* 270 */         if (maxWidth > 0.0F && f1 > maxWidth) f1 = maxWidth;
/*     */       
/*     */       } 
/* 273 */       float x = columnX;
/* 274 */       if ((align & 0x10) != 0) {
/* 275 */         x += columnWidth - f1;
/* 276 */       } else if ((align & 0x8) == 0) {
/* 277 */         x += (columnWidth - f1) / 2.0F;
/*     */       } 
/* 279 */       y -= height + space;
/* 280 */       if (round) {
/* 281 */         child.setBounds(Math.round(x), Math.round(y), Math.round(f1), Math.round(height));
/*     */       } else {
/* 283 */         child.setBounds(x, y, f1, height);
/*     */       } 
/* 285 */       if (layout != null) layout.validate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 290 */     if (this.sizeInvalid) computeSize(); 
/* 291 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 295 */     if (this.wrap) return 0.0F; 
/* 296 */     if (this.sizeInvalid) computeSize(); 
/* 297 */     return this.prefHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRound(boolean round) {
/* 302 */     this.round = round;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup reverse() {
/* 307 */     this.reverse = true;
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup reverse(boolean reverse) {
/* 313 */     this.reverse = reverse;
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getReverse() {
/* 318 */     return this.reverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup space(float space) {
/* 323 */     this.space = space;
/* 324 */     return this;
/*     */   }
/*     */   
/*     */   public float getSpace() {
/* 328 */     return this.space;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup wrapSpace(float wrapSpace) {
/* 333 */     this.wrapSpace = wrapSpace;
/* 334 */     return this;
/*     */   }
/*     */   
/*     */   public float getWrapSpace() {
/* 338 */     return this.wrapSpace;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup pad(float pad) {
/* 343 */     this.padTop = pad;
/* 344 */     this.padLeft = pad;
/* 345 */     this.padBottom = pad;
/* 346 */     this.padRight = pad;
/* 347 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup pad(float top, float left, float bottom, float right) {
/* 351 */     this.padTop = top;
/* 352 */     this.padLeft = left;
/* 353 */     this.padBottom = bottom;
/* 354 */     this.padRight = right;
/* 355 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padTop(float padTop) {
/* 359 */     this.padTop = padTop;
/* 360 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padLeft(float padLeft) {
/* 364 */     this.padLeft = padLeft;
/* 365 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padBottom(float padBottom) {
/* 369 */     this.padBottom = padBottom;
/* 370 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padRight(float padRight) {
/* 374 */     this.padRight = padRight;
/* 375 */     return this;
/*     */   }
/*     */   
/*     */   public float getPadTop() {
/* 379 */     return this.padTop;
/*     */   }
/*     */   
/*     */   public float getPadLeft() {
/* 383 */     return this.padLeft;
/*     */   }
/*     */   
/*     */   public float getPadBottom() {
/* 387 */     return this.padBottom;
/*     */   }
/*     */   
/*     */   public float getPadRight() {
/* 391 */     return this.padRight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalGroup align(int align) {
/* 397 */     this.align = align;
/* 398 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup center() {
/* 403 */     this.align = 1;
/* 404 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup top() {
/* 409 */     this.align |= 0x2;
/* 410 */     this.align &= 0xFFFFFFFB;
/* 411 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup left() {
/* 416 */     this.align |= 0x8;
/* 417 */     this.align &= 0xFFFFFFEF;
/* 418 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup bottom() {
/* 423 */     this.align |= 0x4;
/* 424 */     this.align &= 0xFFFFFFFD;
/* 425 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup right() {
/* 430 */     this.align |= 0x10;
/* 431 */     this.align &= 0xFFFFFFF7;
/* 432 */     return this;
/*     */   }
/*     */   
/*     */   public int getAlign() {
/* 436 */     return this.align;
/*     */   }
/*     */   
/*     */   public VerticalGroup fill() {
/* 440 */     this.fill = 1.0F;
/* 441 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup fill(float fill) {
/* 446 */     this.fill = fill;
/* 447 */     return this;
/*     */   }
/*     */   
/*     */   public float getFill() {
/* 451 */     return this.fill;
/*     */   }
/*     */   
/*     */   public VerticalGroup expand() {
/* 455 */     this.expand = true;
/* 456 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup expand(boolean expand) {
/* 461 */     this.expand = expand;
/* 462 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getExpand() {
/* 466 */     return this.expand;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup grow() {
/* 471 */     this.expand = true;
/* 472 */     this.fill = 1.0F;
/* 473 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalGroup wrap() {
/* 484 */     this.wrap = true;
/* 485 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup wrap(boolean wrap) {
/* 489 */     this.wrap = wrap;
/* 490 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getWrap() {
/* 494 */     return this.wrap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalGroup columnAlign(int columnAlign) {
/* 500 */     this.columnAlign = columnAlign;
/* 501 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup columnCenter() {
/* 506 */     this.columnAlign = 1;
/* 507 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup columnLeft() {
/* 512 */     this.columnAlign |= 0x8;
/* 513 */     this.columnAlign &= 0xFFFFFFEF;
/* 514 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup columnRight() {
/* 519 */     this.columnAlign |= 0x10;
/* 520 */     this.columnAlign &= 0xFFFFFFF7;
/* 521 */     return this;
/*     */   }
/*     */   
/*     */   protected void drawDebugBounds(ShapeRenderer shapes) {
/* 525 */     super.drawDebugBounds(shapes);
/* 526 */     if (!getDebug())
/* 527 */       return;  shapes.set(ShapeRenderer.ShapeType.Line);
/* 528 */     shapes.setColor(getStage().getDebugColor());
/* 529 */     shapes.rect(getX() + this.padLeft, getY() + this.padBottom, getOriginX(), getOriginY(), getWidth() - this.padLeft - this.padRight, 
/* 530 */         getHeight() - this.padBottom - this.padTop, getScaleX(), getScaleY(), getRotation());
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\VerticalGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */