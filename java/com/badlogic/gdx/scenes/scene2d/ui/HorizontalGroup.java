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
/*     */ public class HorizontalGroup
/*     */   extends WidgetGroup
/*     */ {
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   private float lastPrefHeight;
/*     */   private boolean sizeInvalid = true;
/*     */   private FloatArray rowSizes;
/*  43 */   private int align = 8; private int rowAlign; private boolean reverse; private boolean round = true; private boolean wrap;
/*     */   private boolean expand;
/*     */   private float space;
/*     */   
/*     */   public HorizontalGroup() {
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
/*  60 */     this.prefHeight = 0.0F;
/*  61 */     if (this.wrap) {
/*  62 */       this.prefWidth = 0.0F;
/*  63 */       if (this.rowSizes == null) {
/*  64 */         this.rowSizes = new FloatArray();
/*     */       } else {
/*  66 */         this.rowSizes.clear();
/*  67 */       }  FloatArray rowSizes = this.rowSizes;
/*  68 */       float space = this.space, wrapSpace = this.wrapSpace;
/*  69 */       float pad = this.padLeft + this.padRight, groupWidth = getWidth() - pad, x = 0.0F, y = 0.0F, rowHeight = 0.0F;
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
/*  89 */         float incrX = width + ((x > 0.0F) ? space : 0.0F);
/*  90 */         if (x + incrX > groupWidth && x > 0.0F) {
/*  91 */           rowSizes.add(x);
/*  92 */           rowSizes.add(rowHeight);
/*  93 */           this.prefWidth = Math.max(this.prefWidth, x + pad);
/*  94 */           if (y > 0.0F) y += wrapSpace; 
/*  95 */           y += rowHeight;
/*  96 */           rowHeight = 0.0F;
/*  97 */           x = 0.0F;
/*  98 */           incrX = width;
/*     */         } 
/* 100 */         x += incrX;
/* 101 */         rowHeight = Math.max(rowHeight, height);
/*     */       } 
/* 103 */       rowSizes.add(x);
/* 104 */       rowSizes.add(rowHeight);
/* 105 */       this.prefWidth = Math.max(this.prefWidth, x + pad);
/* 106 */       if (y > 0.0F) y += wrapSpace; 
/* 107 */       this.prefHeight = Math.max(this.prefHeight, y + rowHeight);
/*     */     } else {
/* 109 */       this.prefWidth = this.padLeft + this.padRight + this.space * (n - 1);
/* 110 */       for (int i = 0; i < n; i++) {
/* 111 */         Actor child = (Actor)children.get(i);
/* 112 */         if (child instanceof Layout) {
/* 113 */           Layout layout = (Layout)child;
/* 114 */           this.prefWidth += layout.getPrefWidth();
/* 115 */           this.prefHeight = Math.max(this.prefHeight, layout.getPrefHeight());
/*     */         } else {
/* 117 */           this.prefWidth += child.getWidth();
/* 118 */           this.prefHeight = Math.max(this.prefHeight, child.getHeight());
/*     */         } 
/*     */       } 
/*     */     } 
/* 122 */     this.prefHeight += this.padTop + this.padBottom;
/* 123 */     if (this.round) {
/* 124 */       this.prefWidth = Math.round(this.prefWidth);
/* 125 */       this.prefHeight = Math.round(this.prefHeight);
/*     */     } 
/*     */   }
/*     */   public void layout() {
/*     */     float startY;
/* 130 */     if (this.sizeInvalid) computeSize();
/*     */     
/* 132 */     if (this.wrap) {
/* 133 */       layoutWrapped();
/*     */       
/*     */       return;
/*     */     } 
/* 137 */     boolean round = this.round;
/* 138 */     int align = this.align;
/* 139 */     float space = this.space, padBottom = this.padBottom, fill = this.fill;
/* 140 */     float rowHeight = (this.expand ? getHeight() : this.prefHeight) - this.padTop - padBottom, x = this.padLeft;
/*     */     
/* 142 */     if ((align & 0x10) != 0) {
/* 143 */       x += getWidth() - this.prefWidth;
/* 144 */     } else if ((align & 0x8) == 0) {
/* 145 */       x += (getWidth() - this.prefWidth) / 2.0F;
/*     */     } 
/*     */     
/* 148 */     if ((align & 0x4) != 0) {
/* 149 */       startY = padBottom;
/* 150 */     } else if ((align & 0x2) != 0) {
/* 151 */       startY = getHeight() - this.padTop - rowHeight;
/*     */     } else {
/* 153 */       startY = padBottom + (getHeight() - padBottom - this.padTop - rowHeight) / 2.0F;
/*     */     } 
/* 155 */     align = this.rowAlign;
/*     */     
/* 157 */     SnapshotArray<Actor> children = getChildren();
/* 158 */     int i = 0, n = children.size, incr = 1;
/* 159 */     if (this.reverse) {
/* 160 */       i = n - 1;
/* 161 */       n = -1;
/* 162 */       incr = -1;
/*     */     } 
/* 164 */     for (int r = 0; i != n; i += incr) {
/* 165 */       float width, f1; Actor child = (Actor)children.get(i);
/*     */ 
/*     */       
/* 168 */       Layout layout = null;
/* 169 */       if (child instanceof Layout) {
/* 170 */         layout = (Layout)child;
/* 171 */         width = layout.getPrefWidth();
/* 172 */         f1 = layout.getPrefHeight();
/*     */       } else {
/* 174 */         width = child.getWidth();
/* 175 */         f1 = child.getHeight();
/*     */       } 
/*     */       
/* 178 */       if (fill > 0.0F) f1 = rowHeight * fill;
/*     */       
/* 180 */       if (layout != null) {
/* 181 */         f1 = Math.max(f1, layout.getMinHeight());
/* 182 */         float maxHeight = layout.getMaxHeight();
/* 183 */         if (maxHeight > 0.0F && f1 > maxHeight) f1 = maxHeight;
/*     */       
/*     */       } 
/* 186 */       float y = startY;
/* 187 */       if ((align & 0x2) != 0) {
/* 188 */         y += rowHeight - f1;
/* 189 */       } else if ((align & 0x4) == 0) {
/* 190 */         y += (rowHeight - f1) / 2.0F;
/*     */       } 
/* 192 */       if (round) {
/* 193 */         child.setBounds(Math.round(x), Math.round(y), Math.round(width), Math.round(f1));
/*     */       } else {
/* 195 */         child.setBounds(x, y, width, f1);
/* 196 */       }  x += width + space;
/*     */       
/* 198 */       if (layout != null) layout.validate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void layoutWrapped() {
/* 203 */     float prefHeight = getPrefHeight();
/* 204 */     if (prefHeight != this.lastPrefHeight) {
/* 205 */       this.lastPrefHeight = prefHeight;
/* 206 */       invalidateHierarchy();
/*     */     } 
/*     */     
/* 209 */     int align = this.align;
/* 210 */     boolean round = this.round;
/* 211 */     float space = this.space, padBottom = this.padBottom, fill = this.fill, wrapSpace = this.wrapSpace;
/* 212 */     float maxWidth = this.prefWidth - this.padLeft - this.padRight;
/* 213 */     float rowY = prefHeight - this.padTop, groupWidth = getWidth(), xStart = this.padLeft, x = 0.0F, rowHeight = 0.0F;
/*     */     
/* 215 */     if ((align & 0x2) != 0) {
/* 216 */       rowY += getHeight() - prefHeight;
/* 217 */     } else if ((align & 0x4) == 0) {
/* 218 */       rowY += (getHeight() - prefHeight) / 2.0F;
/*     */     } 
/* 220 */     if ((align & 0x10) != 0) {
/* 221 */       xStart += groupWidth - this.prefWidth;
/* 222 */     } else if ((align & 0x8) == 0) {
/* 223 */       xStart += (groupWidth - this.prefWidth) / 2.0F;
/*     */     } 
/* 225 */     groupWidth -= this.padRight;
/* 226 */     align = this.rowAlign;
/*     */     
/* 228 */     FloatArray rowSizes = this.rowSizes;
/* 229 */     SnapshotArray<Actor> children = getChildren();
/* 230 */     int i = 0, n = children.size, incr = 1;
/* 231 */     if (this.reverse) {
/* 232 */       i = n - 1;
/* 233 */       n = -1;
/* 234 */       incr = -1;
/*     */     } 
/* 236 */     for (int r = 0; i != n; i += incr) {
/* 237 */       float width, f1; Actor child = (Actor)children.get(i);
/*     */ 
/*     */       
/* 240 */       Layout layout = null;
/* 241 */       if (child instanceof Layout) {
/* 242 */         layout = (Layout)child;
/* 243 */         width = layout.getPrefWidth();
/* 244 */         f1 = layout.getPrefHeight();
/*     */       } else {
/* 246 */         width = child.getWidth();
/* 247 */         f1 = child.getHeight();
/*     */       } 
/*     */       
/* 250 */       if (x + width > groupWidth || r == 0) {
/* 251 */         x = xStart;
/* 252 */         if ((align & 0x10) != 0) {
/* 253 */           x += maxWidth - rowSizes.get(r);
/* 254 */         } else if ((align & 0x8) == 0) {
/* 255 */           x += (maxWidth - rowSizes.get(r)) / 2.0F;
/* 256 */         }  rowHeight = rowSizes.get(r + 1);
/* 257 */         if (r > 0) rowY -= wrapSpace; 
/* 258 */         rowY -= rowHeight;
/* 259 */         r += 2;
/*     */       } 
/*     */       
/* 262 */       if (fill > 0.0F) f1 = rowHeight * fill;
/*     */       
/* 264 */       if (layout != null) {
/* 265 */         f1 = Math.max(f1, layout.getMinHeight());
/* 266 */         float maxHeight = layout.getMaxHeight();
/* 267 */         if (maxHeight > 0.0F && f1 > maxHeight) f1 = maxHeight;
/*     */       
/*     */       } 
/* 270 */       float y = rowY;
/* 271 */       if ((align & 0x2) != 0) {
/* 272 */         y += rowHeight - f1;
/* 273 */       } else if ((align & 0x4) == 0) {
/* 274 */         y += (rowHeight - f1) / 2.0F;
/*     */       } 
/* 276 */       if (round) {
/* 277 */         child.setBounds(Math.round(x), Math.round(y), Math.round(width), Math.round(f1));
/*     */       } else {
/* 279 */         child.setBounds(x, y, width, f1);
/* 280 */       }  x += width + space;
/*     */       
/* 282 */       if (layout != null) layout.validate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 287 */     if (this.wrap) return 0.0F; 
/* 288 */     if (this.sizeInvalid) computeSize(); 
/* 289 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 293 */     if (this.sizeInvalid) computeSize(); 
/* 294 */     return this.prefHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRound(boolean round) {
/* 299 */     this.round = round;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup reverse() {
/* 304 */     this.reverse = true;
/* 305 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup reverse(boolean reverse) {
/* 310 */     this.reverse = reverse;
/* 311 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getReverse() {
/* 315 */     return this.reverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup space(float space) {
/* 320 */     this.space = space;
/* 321 */     return this;
/*     */   }
/*     */   
/*     */   public float getSpace() {
/* 325 */     return this.space;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup wrapSpace(float wrapSpace) {
/* 330 */     this.wrapSpace = wrapSpace;
/* 331 */     return this;
/*     */   }
/*     */   
/*     */   public float getWrapSpace() {
/* 335 */     return this.wrapSpace;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup pad(float pad) {
/* 340 */     this.padTop = pad;
/* 341 */     this.padLeft = pad;
/* 342 */     this.padBottom = pad;
/* 343 */     this.padRight = pad;
/* 344 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup pad(float top, float left, float bottom, float right) {
/* 348 */     this.padTop = top;
/* 349 */     this.padLeft = left;
/* 350 */     this.padBottom = bottom;
/* 351 */     this.padRight = right;
/* 352 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padTop(float padTop) {
/* 356 */     this.padTop = padTop;
/* 357 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padLeft(float padLeft) {
/* 361 */     this.padLeft = padLeft;
/* 362 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padBottom(float padBottom) {
/* 366 */     this.padBottom = padBottom;
/* 367 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padRight(float padRight) {
/* 371 */     this.padRight = padRight;
/* 372 */     return this;
/*     */   }
/*     */   
/*     */   public float getPadTop() {
/* 376 */     return this.padTop;
/*     */   }
/*     */   
/*     */   public float getPadLeft() {
/* 380 */     return this.padLeft;
/*     */   }
/*     */   
/*     */   public float getPadBottom() {
/* 384 */     return this.padBottom;
/*     */   }
/*     */   
/*     */   public float getPadRight() {
/* 388 */     return this.padRight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HorizontalGroup align(int align) {
/* 394 */     this.align = align;
/* 395 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup center() {
/* 400 */     this.align = 1;
/* 401 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup top() {
/* 406 */     this.align |= 0x2;
/* 407 */     this.align &= 0xFFFFFFFB;
/* 408 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup left() {
/* 413 */     this.align |= 0x8;
/* 414 */     this.align &= 0xFFFFFFEF;
/* 415 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup bottom() {
/* 420 */     this.align |= 0x4;
/* 421 */     this.align &= 0xFFFFFFFD;
/* 422 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup right() {
/* 427 */     this.align |= 0x10;
/* 428 */     this.align &= 0xFFFFFFF7;
/* 429 */     return this;
/*     */   }
/*     */   
/*     */   public int getAlign() {
/* 433 */     return this.align;
/*     */   }
/*     */   
/*     */   public HorizontalGroup fill() {
/* 437 */     this.fill = 1.0F;
/* 438 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup fill(float fill) {
/* 443 */     this.fill = fill;
/* 444 */     return this;
/*     */   }
/*     */   
/*     */   public float getFill() {
/* 448 */     return this.fill;
/*     */   }
/*     */   
/*     */   public HorizontalGroup expand() {
/* 452 */     this.expand = true;
/* 453 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup expand(boolean expand) {
/* 458 */     this.expand = expand;
/* 459 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getExpand() {
/* 463 */     return this.expand;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup grow() {
/* 468 */     this.expand = true;
/* 469 */     this.fill = 1.0F;
/* 470 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HorizontalGroup wrap() {
/* 481 */     this.wrap = true;
/* 482 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup wrap(boolean wrap) {
/* 486 */     this.wrap = wrap;
/* 487 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getWrap() {
/* 491 */     return this.wrap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HorizontalGroup rowAlign(int row) {
/* 497 */     this.rowAlign = row;
/* 498 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup rowCenter() {
/* 503 */     this.rowAlign = 1;
/* 504 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup rowTop() {
/* 509 */     this.rowAlign |= 0x2;
/* 510 */     this.rowAlign &= 0xFFFFFFFB;
/* 511 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup rowBottom() {
/* 516 */     this.rowAlign |= 0x4;
/* 517 */     this.rowAlign &= 0xFFFFFFFD;
/* 518 */     return this;
/*     */   }
/*     */   
/*     */   protected void drawDebugBounds(ShapeRenderer shapes) {
/* 522 */     super.drawDebugBounds(shapes);
/* 523 */     if (!getDebug())
/* 524 */       return;  shapes.set(ShapeRenderer.ShapeType.Line);
/* 525 */     shapes.setColor(getStage().getDebugColor());
/* 526 */     shapes.rect(getX() + this.padLeft, getY() + this.padBottom, getOriginX(), getOriginY(), getWidth() - this.padLeft - this.padRight, 
/* 527 */         getHeight() - this.padBottom - this.padTop, getScaleX(), getScaleY(), getRotation());
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\HorizontalGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */