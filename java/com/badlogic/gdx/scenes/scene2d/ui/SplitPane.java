/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
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
/*     */ public class SplitPane
/*     */   extends WidgetGroup
/*     */ {
/*     */   SplitPaneStyle style;
/*     */   private Actor firstWidget;
/*     */   private Actor secondWidget;
/*     */   boolean vertical;
/*  43 */   float splitAmount = 0.5F; float maxAmount = 1.0F;
/*     */   float minAmount;
/*     */   private float oldSplitAmount;
/*  46 */   private Rectangle firstWidgetBounds = new Rectangle();
/*  47 */   private Rectangle secondWidgetBounds = new Rectangle();
/*  48 */   Rectangle handleBounds = new Rectangle();
/*  49 */   private Rectangle firstScissors = new Rectangle();
/*  50 */   private Rectangle secondScissors = new Rectangle();
/*     */   
/*  52 */   Vector2 lastPoint = new Vector2();
/*  53 */   Vector2 handlePosition = new Vector2();
/*     */ 
/*     */ 
/*     */   
/*     */   public SplitPane(Actor firstWidget, Actor secondWidget, boolean vertical, Skin skin) {
/*  58 */     this(firstWidget, secondWidget, vertical, skin, "default-" + (vertical ? "vertical" : "horizontal"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SplitPane(Actor firstWidget, Actor secondWidget, boolean vertical, Skin skin, String styleName) {
/*  64 */     this(firstWidget, secondWidget, vertical, skin.<SplitPaneStyle>get(styleName, SplitPaneStyle.class));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SplitPane(Actor firstWidget, Actor secondWidget, boolean vertical, SplitPaneStyle style) {
/*  70 */     this.firstWidget = firstWidget;
/*  71 */     this.secondWidget = secondWidget;
/*  72 */     this.vertical = vertical;
/*  73 */     setStyle(style);
/*  74 */     setFirstWidget(firstWidget);
/*  75 */     setSecondWidget(secondWidget);
/*  76 */     setSize(getPrefWidth(), getPrefHeight());
/*  77 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  81 */     addListener((EventListener)new InputListener() {
/*  82 */           int draggingPointer = -1;
/*     */           
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  85 */             if (this.draggingPointer != -1) return false; 
/*  86 */             if (pointer == 0 && button != 0) return false; 
/*  87 */             if (SplitPane.this.handleBounds.contains(x, y)) {
/*  88 */               this.draggingPointer = pointer;
/*  89 */               SplitPane.this.lastPoint.set(x, y);
/*  90 */               SplitPane.this.handlePosition.set(SplitPane.this.handleBounds.x, SplitPane.this.handleBounds.y);
/*  91 */               return true;
/*     */             } 
/*  93 */             return false;
/*     */           }
/*     */           
/*     */           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  97 */             if (pointer == this.draggingPointer) this.draggingPointer = -1; 
/*     */           }
/*     */           
/*     */           public void touchDragged(InputEvent event, float x, float y, int pointer) {
/* 101 */             if (pointer != this.draggingPointer)
/*     */               return; 
/* 103 */             Drawable handle = SplitPane.this.style.handle;
/* 104 */             if (!SplitPane.this.vertical) {
/* 105 */               float delta = x - SplitPane.this.lastPoint.x;
/* 106 */               float availWidth = SplitPane.this.getWidth() - handle.getMinWidth();
/* 107 */               float dragX = SplitPane.this.handlePosition.x + delta;
/* 108 */               SplitPane.this.handlePosition.x = dragX;
/* 109 */               dragX = Math.max(0.0F, dragX);
/* 110 */               dragX = Math.min(availWidth, dragX);
/* 111 */               SplitPane.this.splitAmount = dragX / availWidth;
/* 112 */               if (SplitPane.this.splitAmount < SplitPane.this.minAmount) SplitPane.this.splitAmount = SplitPane.this.minAmount; 
/* 113 */               if (SplitPane.this.splitAmount > SplitPane.this.maxAmount) SplitPane.this.splitAmount = SplitPane.this.maxAmount; 
/* 114 */               SplitPane.this.lastPoint.set(x, y);
/*     */             } else {
/* 116 */               float delta = y - SplitPane.this.lastPoint.y;
/* 117 */               float availHeight = SplitPane.this.getHeight() - handle.getMinHeight();
/* 118 */               float dragY = SplitPane.this.handlePosition.y + delta;
/* 119 */               SplitPane.this.handlePosition.y = dragY;
/* 120 */               dragY = Math.max(0.0F, dragY);
/* 121 */               dragY = Math.min(availHeight, dragY);
/* 122 */               SplitPane.this.splitAmount = 1.0F - dragY / availHeight;
/* 123 */               if (SplitPane.this.splitAmount < SplitPane.this.minAmount) SplitPane.this.splitAmount = SplitPane.this.minAmount; 
/* 124 */               if (SplitPane.this.splitAmount > SplitPane.this.maxAmount) SplitPane.this.splitAmount = SplitPane.this.maxAmount; 
/* 125 */               SplitPane.this.lastPoint.set(x, y);
/*     */             } 
/* 127 */             SplitPane.this.invalidate();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void setStyle(SplitPaneStyle style) {
/* 133 */     this.style = style;
/* 134 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SplitPaneStyle getStyle() {
/* 140 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public void layout() {
/* 145 */     if (!this.vertical) {
/* 146 */       calculateHorizBoundsAndPositions();
/*     */     } else {
/* 148 */       calculateVertBoundsAndPositions();
/*     */     } 
/* 150 */     Actor firstWidget = this.firstWidget;
/* 151 */     if (firstWidget != null) {
/* 152 */       Rectangle firstWidgetBounds = this.firstWidgetBounds;
/* 153 */       firstWidget.setBounds(firstWidgetBounds.x, firstWidgetBounds.y, firstWidgetBounds.width, firstWidgetBounds.height);
/* 154 */       if (firstWidget instanceof Layout) ((Layout)firstWidget).validate(); 
/*     */     } 
/* 156 */     Actor secondWidget = this.secondWidget;
/* 157 */     if (secondWidget != null) {
/* 158 */       Rectangle secondWidgetBounds = this.secondWidgetBounds;
/* 159 */       secondWidget.setBounds(secondWidgetBounds.x, secondWidgetBounds.y, secondWidgetBounds.width, secondWidgetBounds.height);
/* 160 */       if (secondWidget instanceof Layout) ((Layout)secondWidget).validate();
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPrefWidth() {
/* 167 */     float first = (this.firstWidget == null) ? 0.0F : ((this.firstWidget instanceof Layout) ? ((Layout)this.firstWidget).getPrefWidth() : this.firstWidget.getWidth());
/*     */     
/* 169 */     float second = (this.secondWidget == null) ? 0.0F : ((this.secondWidget instanceof Layout) ? ((Layout)this.secondWidget).getPrefWidth() : this.secondWidget.getWidth());
/* 170 */     if (this.vertical) return Math.max(first, second); 
/* 171 */     return first + this.style.handle.getMinWidth() + second;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPrefHeight() {
/* 177 */     float first = (this.firstWidget == null) ? 0.0F : ((this.firstWidget instanceof Layout) ? ((Layout)this.firstWidget).getPrefHeight() : this.firstWidget.getHeight());
/*     */     
/* 179 */     float second = (this.secondWidget == null) ? 0.0F : ((this.secondWidget instanceof Layout) ? ((Layout)this.secondWidget).getPrefHeight() : this.secondWidget.getHeight());
/* 180 */     if (!this.vertical) return Math.max(first, second); 
/* 181 */     return first + this.style.handle.getMinHeight() + second;
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/* 185 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/* 189 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public void setVertical(boolean vertical) {
/* 193 */     this.vertical = vertical;
/*     */   }
/*     */   
/*     */   private void calculateHorizBoundsAndPositions() {
/* 197 */     Drawable handle = this.style.handle;
/*     */     
/* 199 */     float height = getHeight();
/*     */     
/* 201 */     float availWidth = getWidth() - handle.getMinWidth();
/* 202 */     float leftAreaWidth = (int)(availWidth * this.splitAmount);
/* 203 */     float rightAreaWidth = availWidth - leftAreaWidth;
/* 204 */     float handleWidth = handle.getMinWidth();
/*     */     
/* 206 */     this.firstWidgetBounds.set(0.0F, 0.0F, leftAreaWidth, height);
/* 207 */     this.secondWidgetBounds.set(leftAreaWidth + handleWidth, 0.0F, rightAreaWidth, height);
/* 208 */     this.handleBounds.set(leftAreaWidth, 0.0F, handleWidth, height);
/*     */   }
/*     */   
/*     */   private void calculateVertBoundsAndPositions() {
/* 212 */     Drawable handle = this.style.handle;
/*     */     
/* 214 */     float width = getWidth();
/* 215 */     float height = getHeight();
/*     */     
/* 217 */     float availHeight = height - handle.getMinHeight();
/* 218 */     float topAreaHeight = (int)(availHeight * this.splitAmount);
/* 219 */     float bottomAreaHeight = availHeight - topAreaHeight;
/* 220 */     float handleHeight = handle.getMinHeight();
/*     */     
/* 222 */     this.firstWidgetBounds.set(0.0F, height - topAreaHeight, width, topAreaHeight);
/* 223 */     this.secondWidgetBounds.set(0.0F, 0.0F, width, bottomAreaHeight);
/* 224 */     this.handleBounds.set(0.0F, bottomAreaHeight, width, handleHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 229 */     validate();
/*     */     
/* 231 */     Color color = getColor();
/*     */     
/* 233 */     Drawable handle = this.style.handle;
/* 234 */     applyTransform(batch, computeTransform());
/* 235 */     Matrix4 transform = batch.getTransformMatrix();
/* 236 */     if (this.firstWidget != null) {
/* 237 */       batch.flush();
/* 238 */       getStage().calculateScissors(this.firstWidgetBounds, this.firstScissors);
/* 239 */       if (ScissorStack.pushScissors(this.firstScissors)) {
/* 240 */         if (this.firstWidget.isVisible()) this.firstWidget.draw(batch, parentAlpha * color.a); 
/* 241 */         batch.flush();
/* 242 */         ScissorStack.popScissors();
/*     */       } 
/*     */     } 
/* 245 */     if (this.secondWidget != null) {
/* 246 */       batch.flush();
/* 247 */       getStage().calculateScissors(this.secondWidgetBounds, this.secondScissors);
/* 248 */       if (ScissorStack.pushScissors(this.secondScissors)) {
/* 249 */         if (this.secondWidget.isVisible()) this.secondWidget.draw(batch, parentAlpha * color.a); 
/* 250 */         batch.flush();
/* 251 */         ScissorStack.popScissors();
/*     */       } 
/*     */     } 
/* 254 */     batch.setColor(color.r, color.g, color.b, parentAlpha * color.a);
/* 255 */     handle.draw(batch, this.handleBounds.x, this.handleBounds.y, this.handleBounds.width, this.handleBounds.height);
/* 256 */     resetTransform(batch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSplitAmount(float split) {
/* 261 */     this.splitAmount = Math.max(Math.min(this.maxAmount, split), this.minAmount);
/* 262 */     invalidate();
/*     */   }
/*     */   
/*     */   public float getSplit() {
/* 266 */     return this.splitAmount;
/*     */   }
/*     */   
/*     */   public void setMinSplitAmount(float minAmount) {
/* 270 */     if (minAmount < 0.0F) throw new GdxRuntimeException("minAmount has to be >= 0"); 
/* 271 */     if (minAmount >= this.maxAmount) throw new GdxRuntimeException("minAmount has to be < maxAmount"); 
/* 272 */     this.minAmount = minAmount;
/*     */   }
/*     */   
/*     */   public void setMaxSplitAmount(float maxAmount) {
/* 276 */     if (maxAmount > 1.0F) throw new GdxRuntimeException("maxAmount has to be <= 1"); 
/* 277 */     if (maxAmount <= this.minAmount) throw new GdxRuntimeException("maxAmount has to be > minAmount"); 
/* 278 */     this.maxAmount = maxAmount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFirstWidget(Actor widget) {
/* 283 */     if (this.firstWidget != null) super.removeActor(this.firstWidget); 
/* 284 */     this.firstWidget = widget;
/* 285 */     if (widget != null) super.addActor(widget); 
/* 286 */     invalidate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSecondWidget(Actor widget) {
/* 291 */     if (this.secondWidget != null) super.removeActor(this.secondWidget); 
/* 292 */     this.secondWidget = widget;
/* 293 */     if (widget != null) super.addActor(widget); 
/* 294 */     invalidate();
/*     */   }
/*     */   
/*     */   public void addActor(Actor actor) {
/* 298 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
/*     */   }
/*     */   
/*     */   public void addActorAt(int index, Actor actor) {
/* 302 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
/*     */   }
/*     */   
/*     */   public void addActorBefore(Actor actorBefore, Actor actor) {
/* 306 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
/*     */   }
/*     */   
/*     */   public boolean removeActor(Actor actor) {
/* 310 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget(null).");
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SplitPaneStyle
/*     */   {
/*     */     public Drawable handle;
/*     */ 
/*     */     
/*     */     public SplitPaneStyle() {}
/*     */ 
/*     */     
/*     */     public SplitPaneStyle(Drawable handle) {
/* 323 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     public SplitPaneStyle(SplitPaneStyle style) {
/* 327 */       this.handle = style.handle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\SplitPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */