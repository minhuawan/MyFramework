/*      */ package com.badlogic.gdx.scenes.scene2d.ui;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.g2d.Batch;
/*      */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*      */ import com.badlogic.gdx.math.Interpolation;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Rectangle;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*      */ import com.badlogic.gdx.scenes.scene2d.Event;
/*      */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*      */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
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
/*      */ public class ScrollPane
/*      */   extends WidgetGroup
/*      */ {
/*      */   private ScrollPaneStyle style;
/*      */   private Actor widget;
/*   51 */   final Rectangle hScrollBounds = new Rectangle();
/*   52 */   final Rectangle vScrollBounds = new Rectangle();
/*   53 */   final Rectangle hKnobBounds = new Rectangle();
/*   54 */   final Rectangle vKnobBounds = new Rectangle();
/*   55 */   private final Rectangle widgetAreaBounds = new Rectangle();
/*   56 */   private final Rectangle widgetCullingArea = new Rectangle(); private ActorGestureListener flickScrollListener; boolean scrollX; boolean scrollY; boolean vScrollOnRight = true; boolean hScrollOnBottom = true; float amountX; float amountY;
/*   57 */   private final Rectangle scissorBounds = new Rectangle();
/*      */   
/*      */   float visualAmountX;
/*      */   
/*      */   float visualAmountY;
/*      */   
/*      */   float maxX;
/*      */   float maxY;
/*      */   boolean touchScrollH;
/*      */   boolean touchScrollV;
/*   67 */   final Vector2 lastPoint = new Vector2(); float areaWidth; float areaHeight; private boolean fadeScrollBars = true;
/*      */   private boolean smoothScrolling = true;
/*      */   float fadeAlpha;
/*   70 */   float fadeAlphaSeconds = 1.0F; float fadeDelay; float fadeDelaySeconds = 1.0F; boolean cancelTouchFocus = true;
/*      */   boolean flickScroll = true;
/*      */   float velocityX;
/*      */   float velocityY;
/*      */   float flingTimer;
/*      */   private boolean overscrollX = true;
/*      */   private boolean overscrollY = true;
/*   77 */   float flingTime = 1.0F;
/*   78 */   private float overscrollDistance = 50.0F; private float overscrollSpeedMin = 30.0F; private float overscrollSpeedMax = 200.0F; private boolean forceScrollX; private boolean forceScrollY;
/*      */   boolean disableX;
/*      */   boolean disableY;
/*      */   private boolean clamp = true;
/*      */   private boolean scrollbarsOnTop;
/*      */   private boolean variableSizeKnobs = true;
/*   84 */   int draggingPointer = -1;
/*      */ 
/*      */   
/*      */   public ScrollPane(Actor widget) {
/*   88 */     this(widget, new ScrollPaneStyle());
/*      */   }
/*      */ 
/*      */   
/*      */   public ScrollPane(Actor widget, Skin skin) {
/*   93 */     this(widget, skin.<ScrollPaneStyle>get(ScrollPaneStyle.class));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScrollPane(Actor widget, Skin skin, String styleName) {
/*   98 */     this(widget, skin.<ScrollPaneStyle>get(styleName, ScrollPaneStyle.class));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScrollPane(Actor widget, ScrollPaneStyle style) {
/*  103 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  104 */     this.style = style;
/*  105 */     setWidget(widget);
/*  106 */     setSize(150.0F, 150.0F);
/*      */     
/*  108 */     addCaptureListener((EventListener)new InputListener() {
/*      */           private float handlePosition;
/*      */           
/*      */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  112 */             if (ScrollPane.this.draggingPointer != -1) return false; 
/*  113 */             if (pointer == 0 && button != 0) return false; 
/*  114 */             ScrollPane.this.getStage().setScrollFocus((Actor)ScrollPane.this);
/*      */             
/*  116 */             if (!ScrollPane.this.flickScroll) ScrollPane.this.resetFade();
/*      */             
/*  118 */             if (ScrollPane.this.fadeAlpha == 0.0F) return false;
/*      */             
/*  120 */             if (ScrollPane.this.scrollX && ScrollPane.this.hScrollBounds.contains(x, y)) {
/*  121 */               event.stop();
/*  122 */               ScrollPane.this.resetFade();
/*  123 */               if (ScrollPane.this.hKnobBounds.contains(x, y)) {
/*  124 */                 ScrollPane.this.lastPoint.set(x, y);
/*  125 */                 this.handlePosition = ScrollPane.this.hKnobBounds.x;
/*  126 */                 ScrollPane.this.touchScrollH = true;
/*  127 */                 ScrollPane.this.draggingPointer = pointer;
/*  128 */                 return true;
/*      */               } 
/*  130 */               ScrollPane.this.setScrollX(ScrollPane.this.amountX + ScrollPane.this.areaWidth * ((x < ScrollPane.this.hKnobBounds.x) ? -1 : true));
/*  131 */               return true;
/*      */             } 
/*  133 */             if (ScrollPane.this.scrollY && ScrollPane.this.vScrollBounds.contains(x, y)) {
/*  134 */               event.stop();
/*  135 */               ScrollPane.this.resetFade();
/*  136 */               if (ScrollPane.this.vKnobBounds.contains(x, y)) {
/*  137 */                 ScrollPane.this.lastPoint.set(x, y);
/*  138 */                 this.handlePosition = ScrollPane.this.vKnobBounds.y;
/*  139 */                 ScrollPane.this.touchScrollV = true;
/*  140 */                 ScrollPane.this.draggingPointer = pointer;
/*  141 */                 return true;
/*      */               } 
/*  143 */               ScrollPane.this.setScrollY(ScrollPane.this.amountY + ScrollPane.this.areaHeight * ((y < ScrollPane.this.vKnobBounds.y) ? true : -1));
/*  144 */               return true;
/*      */             } 
/*  146 */             return false;
/*      */           }
/*      */           
/*      */           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  150 */             if (pointer != ScrollPane.this.draggingPointer)
/*  151 */               return;  ScrollPane.this.cancel();
/*      */           }
/*      */           
/*      */           public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  155 */             if (pointer != ScrollPane.this.draggingPointer)
/*  156 */               return;  if (ScrollPane.this.touchScrollH) {
/*  157 */               float delta = x - ScrollPane.this.lastPoint.x;
/*  158 */               float scrollH = this.handlePosition + delta;
/*  159 */               this.handlePosition = scrollH;
/*  160 */               scrollH = Math.max(ScrollPane.this.hScrollBounds.x, scrollH);
/*  161 */               scrollH = Math.min(ScrollPane.this.hScrollBounds.x + ScrollPane.this.hScrollBounds.width - ScrollPane.this.hKnobBounds.width, scrollH);
/*  162 */               float total = ScrollPane.this.hScrollBounds.width - ScrollPane.this.hKnobBounds.width;
/*  163 */               if (total != 0.0F) ScrollPane.this.setScrollPercentX((scrollH - ScrollPane.this.hScrollBounds.x) / total); 
/*  164 */               ScrollPane.this.lastPoint.set(x, y);
/*  165 */             } else if (ScrollPane.this.touchScrollV) {
/*  166 */               float delta = y - ScrollPane.this.lastPoint.y;
/*  167 */               float scrollV = this.handlePosition + delta;
/*  168 */               this.handlePosition = scrollV;
/*  169 */               scrollV = Math.max(ScrollPane.this.vScrollBounds.y, scrollV);
/*  170 */               scrollV = Math.min(ScrollPane.this.vScrollBounds.y + ScrollPane.this.vScrollBounds.height - ScrollPane.this.vKnobBounds.height, scrollV);
/*  171 */               float total = ScrollPane.this.vScrollBounds.height - ScrollPane.this.vKnobBounds.height;
/*  172 */               if (total != 0.0F) ScrollPane.this.setScrollPercentY(1.0F - (scrollV - ScrollPane.this.vScrollBounds.y) / total); 
/*  173 */               ScrollPane.this.lastPoint.set(x, y);
/*      */             } 
/*      */           }
/*      */           
/*      */           public boolean mouseMoved(InputEvent event, float x, float y) {
/*  178 */             if (!ScrollPane.this.flickScroll) ScrollPane.this.resetFade(); 
/*  179 */             return false;
/*      */           }
/*      */         });
/*      */     
/*  183 */     this.flickScrollListener = new ActorGestureListener() {
/*      */         public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
/*  185 */           ScrollPane.this.resetFade();
/*  186 */           ScrollPane.this.amountX -= deltaX;
/*  187 */           ScrollPane.this.amountY += deltaY;
/*  188 */           ScrollPane.this.clamp();
/*  189 */           if (ScrollPane.this.cancelTouchFocus && ((ScrollPane.this.scrollX && deltaX != 0.0F) || (ScrollPane.this.scrollY && deltaY != 0.0F))) ScrollPane.this.cancelTouchFocus(); 
/*      */         }
/*      */         
/*      */         public void fling(InputEvent event, float x, float y, int button) {
/*  193 */           if (Math.abs(x) > 150.0F && ScrollPane.this.scrollX) {
/*  194 */             ScrollPane.this.flingTimer = ScrollPane.this.flingTime;
/*  195 */             ScrollPane.this.velocityX = x;
/*  196 */             if (ScrollPane.this.cancelTouchFocus) ScrollPane.this.cancelTouchFocus(); 
/*      */           } 
/*  198 */           if (Math.abs(y) > 150.0F && ScrollPane.this.scrollY) {
/*  199 */             ScrollPane.this.flingTimer = ScrollPane.this.flingTime;
/*  200 */             ScrollPane.this.velocityY = -y;
/*  201 */             if (ScrollPane.this.cancelTouchFocus) ScrollPane.this.cancelTouchFocus(); 
/*      */           } 
/*      */         }
/*      */         
/*      */         public boolean handle(Event event) {
/*  206 */           if (super.handle(event)) {
/*  207 */             if (((InputEvent)event).getType() == InputEvent.Type.touchDown) ScrollPane.this.flingTimer = 0.0F; 
/*  208 */             return true;
/*      */           } 
/*  210 */           return false;
/*      */         }
/*      */       };
/*  213 */     addListener((EventListener)this.flickScrollListener);
/*      */     
/*  215 */     addListener((EventListener)new InputListener() {
/*      */           public boolean scrolled(InputEvent event, float x, float y, int amount) {
/*  217 */             ScrollPane.this.resetFade();
/*  218 */             if (ScrollPane.this.scrollY) {
/*  219 */               ScrollPane.this.setScrollY(ScrollPane.this.amountY + ScrollPane.this.getMouseWheelY() * amount);
/*  220 */             } else if (ScrollPane.this.scrollX) {
/*  221 */               ScrollPane.this.setScrollX(ScrollPane.this.amountX + ScrollPane.this.getMouseWheelX() * amount);
/*      */             } else {
/*  223 */               return false;
/*  224 */             }  return true;
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   void resetFade() {
/*  230 */     this.fadeAlpha = this.fadeAlphaSeconds;
/*  231 */     this.fadeDelay = this.fadeDelaySeconds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void cancelTouchFocus() {
/*  238 */     Stage stage = getStage();
/*  239 */     if (stage != null) stage.cancelTouchFocusExcept((EventListener)this.flickScrollListener, (Actor)this);
/*      */   
/*      */   }
/*      */   
/*      */   public void cancel() {
/*  244 */     this.draggingPointer = -1;
/*  245 */     this.touchScrollH = false;
/*  246 */     this.touchScrollV = false;
/*  247 */     this.flickScrollListener.getGestureDetector().cancel();
/*      */   }
/*      */   
/*      */   void clamp() {
/*  251 */     if (!this.clamp)
/*  252 */       return;  scrollX(this.overscrollX ? MathUtils.clamp(this.amountX, -this.overscrollDistance, this.maxX + this.overscrollDistance) : 
/*  253 */         MathUtils.clamp(this.amountX, 0.0F, this.maxX));
/*  254 */     scrollY(this.overscrollY ? MathUtils.clamp(this.amountY, -this.overscrollDistance, this.maxY + this.overscrollDistance) : 
/*  255 */         MathUtils.clamp(this.amountY, 0.0F, this.maxY));
/*      */   }
/*      */   
/*      */   public void setStyle(ScrollPaneStyle style) {
/*  259 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  260 */     this.style = style;
/*  261 */     invalidateHierarchy();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ScrollPaneStyle getStyle() {
/*  267 */     return this.style;
/*      */   }
/*      */   
/*      */   public void act(float delta) {
/*  271 */     super.act(delta);
/*      */     
/*  273 */     boolean panning = this.flickScrollListener.getGestureDetector().isPanning();
/*  274 */     boolean animating = false;
/*      */     
/*  276 */     if (this.fadeAlpha > 0.0F && this.fadeScrollBars && !panning && !this.touchScrollH && !this.touchScrollV) {
/*  277 */       this.fadeDelay -= delta;
/*  278 */       if (this.fadeDelay <= 0.0F) this.fadeAlpha = Math.max(0.0F, this.fadeAlpha - delta); 
/*  279 */       animating = true;
/*      */     } 
/*      */     
/*  282 */     if (this.flingTimer > 0.0F) {
/*  283 */       resetFade();
/*      */       
/*  285 */       float alpha = this.flingTimer / this.flingTime;
/*  286 */       this.amountX -= this.velocityX * alpha * delta;
/*  287 */       this.amountY -= this.velocityY * alpha * delta;
/*  288 */       clamp();
/*      */ 
/*      */       
/*  291 */       if (this.amountX == -this.overscrollDistance) this.velocityX = 0.0F; 
/*  292 */       if (this.amountX >= this.maxX + this.overscrollDistance) this.velocityX = 0.0F; 
/*  293 */       if (this.amountY == -this.overscrollDistance) this.velocityY = 0.0F; 
/*  294 */       if (this.amountY >= this.maxY + this.overscrollDistance) this.velocityY = 0.0F;
/*      */       
/*  296 */       this.flingTimer -= delta;
/*  297 */       if (this.flingTimer <= 0.0F) {
/*  298 */         this.velocityX = 0.0F;
/*  299 */         this.velocityY = 0.0F;
/*      */       } 
/*      */       
/*  302 */       animating = true;
/*      */     } 
/*      */     
/*  305 */     if (this.smoothScrolling && this.flingTimer <= 0.0F && !panning && (!this.touchScrollH || (this.scrollX && this.maxX / (this.hScrollBounds.width - this.hKnobBounds.width) > this.areaWidth * 0.1F)) && (!this.touchScrollV || (this.scrollY && this.maxY / (this.vScrollBounds.height - this.vKnobBounds.height) > this.areaHeight * 0.1F))) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  310 */       if (this.visualAmountX != this.amountX) {
/*  311 */         if (this.visualAmountX < this.amountX) {
/*  312 */           visualScrollX(Math.min(this.amountX, this.visualAmountX + Math.max(200.0F * delta, (this.amountX - this.visualAmountX) * 7.0F * delta)));
/*      */         } else {
/*  314 */           visualScrollX(Math.max(this.amountX, this.visualAmountX - Math.max(200.0F * delta, (this.visualAmountX - this.amountX) * 7.0F * delta)));
/*  315 */         }  animating = true;
/*      */       } 
/*  317 */       if (this.visualAmountY != this.amountY) {
/*  318 */         if (this.visualAmountY < this.amountY) {
/*  319 */           visualScrollY(Math.min(this.amountY, this.visualAmountY + Math.max(200.0F * delta, (this.amountY - this.visualAmountY) * 7.0F * delta)));
/*      */         } else {
/*  321 */           visualScrollY(Math.max(this.amountY, this.visualAmountY - Math.max(200.0F * delta, (this.visualAmountY - this.amountY) * 7.0F * delta)));
/*  322 */         }  animating = true;
/*      */       } 
/*      */     } else {
/*  325 */       if (this.visualAmountX != this.amountX) visualScrollX(this.amountX); 
/*  326 */       if (this.visualAmountY != this.amountY) visualScrollY(this.amountY);
/*      */     
/*      */     } 
/*  329 */     if (!panning) {
/*  330 */       if (this.overscrollX && this.scrollX) {
/*  331 */         if (this.amountX < 0.0F) {
/*  332 */           resetFade();
/*  333 */           this.amountX += (this.overscrollSpeedMin + (this.overscrollSpeedMax - this.overscrollSpeedMin) * -this.amountX / this.overscrollDistance) * delta;
/*      */           
/*  335 */           if (this.amountX > 0.0F) scrollX(0.0F); 
/*  336 */           animating = true;
/*  337 */         } else if (this.amountX > this.maxX) {
/*  338 */           resetFade();
/*  339 */           this.amountX -= (this.overscrollSpeedMin + (this.overscrollSpeedMax - this.overscrollSpeedMin) * -(this.maxX - this.amountX) / this.overscrollDistance) * delta;
/*      */           
/*  341 */           if (this.amountX < this.maxX) scrollX(this.maxX); 
/*  342 */           animating = true;
/*      */         } 
/*      */       }
/*  345 */       if (this.overscrollY && this.scrollY) {
/*  346 */         if (this.amountY < 0.0F) {
/*  347 */           resetFade();
/*  348 */           this.amountY += (this.overscrollSpeedMin + (this.overscrollSpeedMax - this.overscrollSpeedMin) * -this.amountY / this.overscrollDistance) * delta;
/*      */           
/*  350 */           if (this.amountY > 0.0F) scrollY(0.0F); 
/*  351 */           animating = true;
/*  352 */         } else if (this.amountY > this.maxY) {
/*  353 */           resetFade();
/*  354 */           this.amountY -= (this.overscrollSpeedMin + (this.overscrollSpeedMax - this.overscrollSpeedMin) * -(this.maxY - this.amountY) / this.overscrollDistance) * delta;
/*      */           
/*  356 */           if (this.amountY < this.maxY) scrollY(this.maxY); 
/*  357 */           animating = true;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  362 */     if (animating) {
/*  363 */       Stage stage = getStage();
/*  364 */       if (stage != null && stage.getActionsRequestRendering()) Gdx.graphics.requestRendering(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void layout() {
/*  369 */     Drawable bg = this.style.background;
/*  370 */     Drawable hScrollKnob = this.style.hScrollKnob;
/*  371 */     Drawable vScrollKnob = this.style.vScrollKnob;
/*      */     
/*  373 */     float bgLeftWidth = 0.0F, bgRightWidth = 0.0F, bgTopHeight = 0.0F, bgBottomHeight = 0.0F;
/*  374 */     if (bg != null) {
/*  375 */       bgLeftWidth = bg.getLeftWidth();
/*  376 */       bgRightWidth = bg.getRightWidth();
/*  377 */       bgTopHeight = bg.getTopHeight();
/*  378 */       bgBottomHeight = bg.getBottomHeight();
/*      */     } 
/*      */     
/*  381 */     float width = getWidth();
/*  382 */     float height = getHeight();
/*      */     
/*  384 */     float scrollbarHeight = 0.0F;
/*  385 */     if (hScrollKnob != null) scrollbarHeight = hScrollKnob.getMinHeight(); 
/*  386 */     if (this.style.hScroll != null) scrollbarHeight = Math.max(scrollbarHeight, this.style.hScroll.getMinHeight()); 
/*  387 */     float scrollbarWidth = 0.0F;
/*  388 */     if (vScrollKnob != null) scrollbarWidth = vScrollKnob.getMinWidth(); 
/*  389 */     if (this.style.vScroll != null) scrollbarWidth = Math.max(scrollbarWidth, this.style.vScroll.getMinWidth());
/*      */ 
/*      */     
/*  392 */     this.areaWidth = width - bgLeftWidth - bgRightWidth;
/*  393 */     this.areaHeight = height - bgTopHeight - bgBottomHeight;
/*      */     
/*  395 */     if (this.widget == null) {
/*      */       return;
/*      */     }
/*      */     
/*  399 */     if (this.widget instanceof Layout) {
/*  400 */       Layout layout = (Layout)this.widget;
/*  401 */       widgetWidth = layout.getPrefWidth();
/*  402 */       widgetHeight = layout.getPrefHeight();
/*      */     } else {
/*  404 */       widgetWidth = this.widget.getWidth();
/*  405 */       widgetHeight = this.widget.getHeight();
/*      */     } 
/*      */ 
/*      */     
/*  409 */     this.scrollX = (this.forceScrollX || (widgetWidth > this.areaWidth && !this.disableX));
/*  410 */     this.scrollY = (this.forceScrollY || (widgetHeight > this.areaHeight && !this.disableY));
/*      */     
/*  412 */     boolean fade = this.fadeScrollBars;
/*  413 */     if (!fade) {
/*      */       
/*  415 */       if (this.scrollY) {
/*  416 */         this.areaWidth -= scrollbarWidth;
/*  417 */         if (!this.scrollX && widgetWidth > this.areaWidth && !this.disableX) this.scrollX = true; 
/*      */       } 
/*  419 */       if (this.scrollX) {
/*  420 */         this.areaHeight -= scrollbarHeight;
/*  421 */         if (!this.scrollY && widgetHeight > this.areaHeight && !this.disableY) {
/*  422 */           this.scrollY = true;
/*  423 */           this.areaWidth -= scrollbarWidth;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  429 */     this.widgetAreaBounds.set(bgLeftWidth, bgBottomHeight, this.areaWidth, this.areaHeight);
/*      */     
/*  431 */     if (fade) {
/*      */       
/*  433 */       if (this.scrollX && this.scrollY) {
/*  434 */         this.areaHeight -= scrollbarHeight;
/*  435 */         this.areaWidth -= scrollbarWidth;
/*      */       }
/*      */     
/*  438 */     } else if (this.scrollbarsOnTop) {
/*      */       
/*  440 */       if (this.scrollX) this.widgetAreaBounds.height += scrollbarHeight; 
/*  441 */       if (this.scrollY) this.widgetAreaBounds.width += scrollbarWidth;
/*      */     
/*      */     } else {
/*  444 */       if (this.scrollX && this.hScrollOnBottom) this.widgetAreaBounds.y += scrollbarHeight;
/*      */       
/*  446 */       if (this.scrollY && !this.vScrollOnRight) this.widgetAreaBounds.x += scrollbarWidth;
/*      */     
/*      */     } 
/*      */ 
/*      */     
/*  451 */     float widgetWidth = this.disableX ? this.areaWidth : Math.max(this.areaWidth, widgetWidth);
/*  452 */     float widgetHeight = this.disableY ? this.areaHeight : Math.max(this.areaHeight, widgetHeight);
/*      */     
/*  454 */     this.maxX = widgetWidth - this.areaWidth;
/*  455 */     this.maxY = widgetHeight - this.areaHeight;
/*  456 */     if (fade)
/*      */     {
/*  458 */       if (this.scrollX && this.scrollY) {
/*  459 */         this.maxY -= scrollbarHeight;
/*  460 */         this.maxX -= scrollbarWidth;
/*      */       } 
/*      */     }
/*  463 */     scrollX(MathUtils.clamp(this.amountX, 0.0F, this.maxX));
/*  464 */     scrollY(MathUtils.clamp(this.amountY, 0.0F, this.maxY));
/*      */ 
/*      */     
/*  467 */     if (this.scrollX) {
/*  468 */       if (hScrollKnob != null) {
/*  469 */         float hScrollHeight = (this.style.hScroll != null) ? this.style.hScroll.getMinHeight() : hScrollKnob.getMinHeight();
/*      */         
/*  471 */         float boundsX = this.vScrollOnRight ? bgLeftWidth : (bgLeftWidth + scrollbarWidth);
/*      */         
/*  473 */         float boundsY = this.hScrollOnBottom ? bgBottomHeight : (height - bgTopHeight - hScrollHeight);
/*  474 */         this.hScrollBounds.set(boundsX, boundsY, this.areaWidth, hScrollHeight);
/*  475 */         if (this.variableSizeKnobs) {
/*  476 */           this.hKnobBounds.width = Math.max(hScrollKnob.getMinWidth(), (int)(this.hScrollBounds.width * this.areaWidth / widgetWidth));
/*      */         } else {
/*  478 */           this.hKnobBounds.width = hScrollKnob.getMinWidth();
/*      */         } 
/*  480 */         this.hKnobBounds.height = hScrollKnob.getMinHeight();
/*      */         
/*  482 */         this.hScrollBounds.x += (int)((this.hScrollBounds.width - this.hKnobBounds.width) * getScrollPercentX());
/*  483 */         this.hKnobBounds.y = this.hScrollBounds.y;
/*      */       } else {
/*  485 */         this.hScrollBounds.set(0.0F, 0.0F, 0.0F, 0.0F);
/*  486 */         this.hKnobBounds.set(0.0F, 0.0F, 0.0F, 0.0F);
/*      */       } 
/*      */     }
/*  489 */     if (this.scrollY) {
/*  490 */       if (vScrollKnob != null) {
/*  491 */         float boundsX, boundsY, vScrollWidth = (this.style.vScroll != null) ? this.style.vScroll.getMinWidth() : vScrollKnob.getMinWidth();
/*      */ 
/*      */         
/*  494 */         if (this.hScrollOnBottom) {
/*  495 */           boundsY = height - bgTopHeight - this.areaHeight;
/*      */         } else {
/*  497 */           boundsY = bgBottomHeight;
/*      */         } 
/*      */         
/*  500 */         if (this.vScrollOnRight) {
/*  501 */           boundsX = width - bgRightWidth - vScrollWidth;
/*      */         } else {
/*  503 */           boundsX = bgLeftWidth;
/*      */         } 
/*  505 */         this.vScrollBounds.set(boundsX, boundsY, vScrollWidth, this.areaHeight);
/*  506 */         this.vKnobBounds.width = vScrollKnob.getMinWidth();
/*  507 */         if (this.variableSizeKnobs) {
/*  508 */           this.vKnobBounds.height = Math.max(vScrollKnob.getMinHeight(), (int)(this.vScrollBounds.height * this.areaHeight / widgetHeight));
/*      */         } else {
/*  510 */           this.vKnobBounds.height = vScrollKnob.getMinHeight();
/*      */         } 
/*  512 */         if (this.vScrollOnRight) {
/*  513 */           this.vKnobBounds.x = width - bgRightWidth - vScrollKnob.getMinWidth();
/*      */         } else {
/*  515 */           this.vKnobBounds.x = bgLeftWidth;
/*      */         } 
/*  517 */         this.vScrollBounds.y += (int)((this.vScrollBounds.height - this.vKnobBounds.height) * (1.0F - getScrollPercentY()));
/*      */       } else {
/*  519 */         this.vScrollBounds.set(0.0F, 0.0F, 0.0F, 0.0F);
/*  520 */         this.vKnobBounds.set(0.0F, 0.0F, 0.0F, 0.0F);
/*      */       } 
/*      */     }
/*      */     
/*  524 */     this.widget.setSize(widgetWidth, widgetHeight);
/*  525 */     if (this.widget instanceof Layout) ((Layout)this.widget).validate();
/*      */   
/*      */   }
/*      */   
/*      */   public void draw(Batch batch, float parentAlpha) {
/*  530 */     if (this.widget == null)
/*      */       return; 
/*  532 */     validate();
/*      */ 
/*      */     
/*  535 */     applyTransform(batch, computeTransform());
/*      */     
/*  537 */     if (this.scrollX) this.hScrollBounds.x += (int)((this.hScrollBounds.width - this.hKnobBounds.width) * getVisualScrollPercentX()); 
/*  538 */     if (this.scrollY) {
/*  539 */       this.vScrollBounds.y += (int)((this.vScrollBounds.height - this.vKnobBounds.height) * (1.0F - getVisualScrollPercentY()));
/*      */     }
/*      */     
/*  542 */     float y = this.widgetAreaBounds.y;
/*  543 */     if (!this.scrollY) {
/*  544 */       y -= (int)this.maxY;
/*      */     } else {
/*  546 */       y -= (int)(this.maxY - this.visualAmountY);
/*      */     } 
/*  548 */     float x = this.widgetAreaBounds.x;
/*  549 */     if (this.scrollX) x -= (int)this.visualAmountX;
/*      */     
/*  551 */     if (!this.fadeScrollBars && this.scrollbarsOnTop) {
/*  552 */       if (this.scrollX && this.hScrollOnBottom) {
/*  553 */         float scrollbarHeight = 0.0F;
/*  554 */         if (this.style.hScrollKnob != null) scrollbarHeight = this.style.hScrollKnob.getMinHeight(); 
/*  555 */         if (this.style.hScroll != null) scrollbarHeight = Math.max(scrollbarHeight, this.style.hScroll.getMinHeight()); 
/*  556 */         y += scrollbarHeight;
/*      */       } 
/*  558 */       if (this.scrollY && !this.vScrollOnRight) {
/*  559 */         float scrollbarWidth = 0.0F;
/*  560 */         if (this.style.hScrollKnob != null) scrollbarWidth = this.style.hScrollKnob.getMinWidth(); 
/*  561 */         if (this.style.hScroll != null) scrollbarWidth = Math.max(scrollbarWidth, this.style.hScroll.getMinWidth()); 
/*  562 */         x += scrollbarWidth;
/*      */       } 
/*      */     } 
/*      */     
/*  566 */     this.widget.setPosition(x, y);
/*      */     
/*  568 */     if (this.widget instanceof Cullable) {
/*  569 */       this.widgetCullingArea.x = -this.widget.getX() + this.widgetAreaBounds.x;
/*  570 */       this.widgetCullingArea.y = -this.widget.getY() + this.widgetAreaBounds.y;
/*  571 */       this.widgetCullingArea.width = this.widgetAreaBounds.width;
/*  572 */       this.widgetCullingArea.height = this.widgetAreaBounds.height;
/*  573 */       ((Cullable)this.widget).setCullingArea(this.widgetCullingArea);
/*      */     } 
/*      */ 
/*      */     
/*  577 */     Color color = getColor();
/*  578 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*  579 */     if (this.style.background != null) this.style.background.draw(batch, 0.0F, 0.0F, getWidth(), getHeight());
/*      */ 
/*      */ 
/*      */     
/*  583 */     getStage().calculateScissors(this.widgetAreaBounds, this.scissorBounds);
/*      */ 
/*      */     
/*  586 */     batch.flush();
/*  587 */     if (ScissorStack.pushScissors(this.scissorBounds)) {
/*  588 */       drawChildren(batch, parentAlpha);
/*  589 */       batch.flush();
/*  590 */       ScissorStack.popScissors();
/*      */     } 
/*      */ 
/*      */     
/*  594 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha * Interpolation.fade.apply(this.fadeAlpha / this.fadeAlphaSeconds));
/*  595 */     if (this.scrollX && this.scrollY && 
/*  596 */       this.style.corner != null) {
/*  597 */       this.style.corner.draw(batch, this.hScrollBounds.x + this.hScrollBounds.width, this.hScrollBounds.y, this.vScrollBounds.width, this.vScrollBounds.y);
/*      */     }
/*      */ 
/*      */     
/*  601 */     if (this.scrollX) {
/*  602 */       if (this.style.hScroll != null)
/*  603 */         this.style.hScroll.draw(batch, this.hScrollBounds.x, this.hScrollBounds.y, this.hScrollBounds.width, this.hScrollBounds.height); 
/*  604 */       if (this.style.hScrollKnob != null)
/*  605 */         this.style.hScrollKnob.draw(batch, this.hKnobBounds.x, this.hKnobBounds.y, this.hKnobBounds.width, this.hKnobBounds.height); 
/*      */     } 
/*  607 */     if (this.scrollY) {
/*  608 */       if (this.style.vScroll != null)
/*  609 */         this.style.vScroll.draw(batch, this.vScrollBounds.x, this.vScrollBounds.y, this.vScrollBounds.width, this.vScrollBounds.height); 
/*  610 */       if (this.style.vScrollKnob != null) {
/*  611 */         this.style.vScrollKnob.draw(batch, this.vKnobBounds.x, this.vKnobBounds.y, this.vKnobBounds.width, this.vKnobBounds.height);
/*      */       }
/*      */     } 
/*  614 */     resetTransform(batch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fling(float flingTime, float velocityX, float velocityY) {
/*  622 */     this.flingTimer = flingTime;
/*  623 */     this.velocityX = velocityX;
/*  624 */     this.velocityY = velocityY;
/*      */   }
/*      */   
/*      */   public float getPrefWidth() {
/*  628 */     if (this.widget instanceof Layout) {
/*  629 */       float width = ((Layout)this.widget).getPrefWidth();
/*  630 */       if (this.style.background != null) width += this.style.background.getLeftWidth() + this.style.background.getRightWidth(); 
/*  631 */       if (this.forceScrollY) {
/*  632 */         float scrollbarWidth = 0.0F;
/*  633 */         if (this.style.vScrollKnob != null) scrollbarWidth = this.style.vScrollKnob.getMinWidth(); 
/*  634 */         if (this.style.vScroll != null) scrollbarWidth = Math.max(scrollbarWidth, this.style.vScroll.getMinWidth()); 
/*  635 */         width += scrollbarWidth;
/*      */       } 
/*  637 */       return width;
/*      */     } 
/*  639 */     return 150.0F;
/*      */   }
/*      */   
/*      */   public float getPrefHeight() {
/*  643 */     if (this.widget instanceof Layout) {
/*  644 */       float height = ((Layout)this.widget).getPrefHeight();
/*  645 */       if (this.style.background != null) height += this.style.background.getTopHeight() + this.style.background.getBottomHeight(); 
/*  646 */       if (this.forceScrollX) {
/*  647 */         float scrollbarHeight = 0.0F;
/*  648 */         if (this.style.hScrollKnob != null) scrollbarHeight = this.style.hScrollKnob.getMinHeight(); 
/*  649 */         if (this.style.hScroll != null) scrollbarHeight = Math.max(scrollbarHeight, this.style.hScroll.getMinHeight()); 
/*  650 */         height += scrollbarHeight;
/*      */       } 
/*  652 */       return height;
/*      */     } 
/*  654 */     return 150.0F;
/*      */   }
/*      */   
/*      */   public float getMinWidth() {
/*  658 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public float getMinHeight() {
/*  662 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWidget(Actor widget) {
/*  668 */     if (widget == this) throw new IllegalArgumentException("widget cannot be the ScrollPane."); 
/*  669 */     if (this.widget != null) super.removeActor(this.widget); 
/*  670 */     this.widget = widget;
/*  671 */     if (widget != null) super.addActor(widget);
/*      */   
/*      */   }
/*      */   
/*      */   public Actor getWidget() {
/*  676 */     return this.widget;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addActor(Actor actor) {
/*  682 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addActorAt(int index, Actor actor) {
/*  688 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addActorBefore(Actor actorBefore, Actor actor) {
/*  694 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addActorAfter(Actor actorAfter, Actor actor) {
/*  700 */     throw new UnsupportedOperationException("Use ScrollPane#setWidget.");
/*      */   }
/*      */   
/*      */   public boolean removeActor(Actor actor) {
/*  704 */     if (actor != this.widget) return false; 
/*  705 */     setWidget((Actor)null);
/*  706 */     return true;
/*      */   }
/*      */   
/*      */   public Actor hit(float x, float y, boolean touchable) {
/*  710 */     if (x < 0.0F || x >= getWidth() || y < 0.0F || y >= getHeight()) return null; 
/*  711 */     if (this.scrollX && this.hScrollBounds.contains(x, y)) return (Actor)this; 
/*  712 */     if (this.scrollY && this.vScrollBounds.contains(x, y)) return (Actor)this; 
/*  713 */     return super.hit(x, y, touchable);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void scrollX(float pixelsX) {
/*  718 */     this.amountX = pixelsX;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void scrollY(float pixelsY) {
/*  723 */     this.amountY = pixelsY;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void visualScrollX(float pixelsX) {
/*  728 */     this.visualAmountX = pixelsX;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void visualScrollY(float pixelsY) {
/*  733 */     this.visualAmountY = pixelsY;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getMouseWheelX() {
/*  738 */     return Math.min(this.areaWidth, Math.max(this.areaWidth * 0.9F, this.maxX * 0.1F) / 4.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getMouseWheelY() {
/*  743 */     return Math.min(this.areaHeight, Math.max(this.areaHeight * 0.9F, this.maxY * 0.1F) / 4.0F);
/*      */   }
/*      */   
/*      */   public void setScrollX(float pixels) {
/*  747 */     scrollX(MathUtils.clamp(pixels, 0.0F, this.maxX));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScrollX() {
/*  752 */     return this.amountX;
/*      */   }
/*      */   
/*      */   public void setScrollY(float pixels) {
/*  756 */     scrollY(MathUtils.clamp(pixels, 0.0F, this.maxY));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScrollY() {
/*  761 */     return this.amountY;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateVisualScroll() {
/*  767 */     this.visualAmountX = this.amountX;
/*  768 */     this.visualAmountY = this.amountY;
/*      */   }
/*      */   
/*      */   public float getVisualScrollX() {
/*  772 */     return !this.scrollX ? 0.0F : this.visualAmountX;
/*      */   }
/*      */   
/*      */   public float getVisualScrollY() {
/*  776 */     return !this.scrollY ? 0.0F : this.visualAmountY;
/*      */   }
/*      */   
/*      */   public float getVisualScrollPercentX() {
/*  780 */     return MathUtils.clamp(this.visualAmountX / this.maxX, 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public float getVisualScrollPercentY() {
/*  784 */     return MathUtils.clamp(this.visualAmountY / this.maxY, 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public float getScrollPercentX() {
/*  788 */     return MathUtils.clamp(this.amountX / this.maxX, 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public void setScrollPercentX(float percentX) {
/*  792 */     scrollX(this.maxX * MathUtils.clamp(percentX, 0.0F, 1.0F));
/*      */   }
/*      */   
/*      */   public float getScrollPercentY() {
/*  796 */     return MathUtils.clamp(this.amountY / this.maxY, 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public void setScrollPercentY(float percentY) {
/*  800 */     scrollY(this.maxY * MathUtils.clamp(percentY, 0.0F, 1.0F));
/*      */   }
/*      */   
/*      */   public void setFlickScroll(boolean flickScroll) {
/*  804 */     if (this.flickScroll == flickScroll)
/*  805 */       return;  this.flickScroll = flickScroll;
/*  806 */     if (flickScroll) {
/*  807 */       addListener((EventListener)this.flickScrollListener);
/*      */     } else {
/*  809 */       removeListener((EventListener)this.flickScrollListener);
/*  810 */     }  invalidate();
/*      */   }
/*      */   
/*      */   public void setFlickScrollTapSquareSize(float halfTapSquareSize) {
/*  814 */     this.flickScrollListener.getGestureDetector().setTapSquareSize(halfTapSquareSize);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void scrollTo(float x, float y, float width, float height) {
/*  820 */     scrollTo(x, y, width, height, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void scrollTo(float x, float y, float width, float height, boolean centerHorizontal, boolean centerVertical) {
/*  826 */     float amountX = this.amountX;
/*  827 */     if (centerHorizontal) {
/*  828 */       amountX = x - this.areaWidth / 2.0F + width / 2.0F;
/*      */     } else {
/*  830 */       if (x + width > amountX + this.areaWidth) amountX = x + width - this.areaWidth; 
/*  831 */       if (x < amountX) amountX = x; 
/*      */     } 
/*  833 */     scrollX(MathUtils.clamp(amountX, 0.0F, this.maxX));
/*      */     
/*  835 */     float amountY = this.amountY;
/*  836 */     if (centerVertical) {
/*  837 */       amountY = this.maxY - y + this.areaHeight / 2.0F - height / 2.0F;
/*      */     } else {
/*  839 */       if (amountY > this.maxY - y - height + this.areaHeight) amountY = this.maxY - y - height + this.areaHeight; 
/*  840 */       if (amountY < this.maxY - y) amountY = this.maxY - y; 
/*      */     } 
/*  842 */     scrollY(MathUtils.clamp(amountY, 0.0F, this.maxY));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getMaxX() {
/*  847 */     return this.maxX;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getMaxY() {
/*  852 */     return this.maxY;
/*      */   }
/*      */   
/*      */   public float getScrollBarHeight() {
/*  856 */     if (!this.scrollX) return 0.0F; 
/*  857 */     float height = 0.0F;
/*  858 */     if (this.style.hScrollKnob != null) height = this.style.hScrollKnob.getMinHeight(); 
/*  859 */     if (this.style.hScroll != null) height = Math.max(height, this.style.hScroll.getMinHeight()); 
/*  860 */     return height;
/*      */   }
/*      */   
/*      */   public float getScrollBarWidth() {
/*  864 */     if (!this.scrollY) return 0.0F; 
/*  865 */     float width = 0.0F;
/*  866 */     if (this.style.vScrollKnob != null) width = this.style.vScrollKnob.getMinWidth(); 
/*  867 */     if (this.style.vScroll != null) width = Math.max(width, this.style.vScroll.getMinWidth()); 
/*  868 */     return width;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScrollWidth() {
/*  873 */     return this.areaWidth;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getScrollHeight() {
/*  878 */     return this.areaHeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isScrollX() {
/*  883 */     return this.scrollX;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isScrollY() {
/*  888 */     return this.scrollY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setScrollingDisabled(boolean x, boolean y) {
/*  893 */     this.disableX = x;
/*  894 */     this.disableY = y;
/*      */   }
/*      */   
/*      */   public boolean isScrollingDisabledX() {
/*  898 */     return this.disableX;
/*      */   }
/*      */   
/*      */   public boolean isScrollingDisabledY() {
/*  902 */     return this.disableY;
/*      */   }
/*      */   
/*      */   public boolean isLeftEdge() {
/*  906 */     return (!this.scrollX || this.amountX <= 0.0F);
/*      */   }
/*      */   
/*      */   public boolean isRightEdge() {
/*  910 */     return (!this.scrollX || this.amountX >= this.maxX);
/*      */   }
/*      */   
/*      */   public boolean isTopEdge() {
/*  914 */     return (!this.scrollY || this.amountY <= 0.0F);
/*      */   }
/*      */   
/*      */   public boolean isBottomEdge() {
/*  918 */     return (!this.scrollY || this.amountY >= this.maxY);
/*      */   }
/*      */   
/*      */   public boolean isDragging() {
/*  922 */     return (this.draggingPointer != -1);
/*      */   }
/*      */   
/*      */   public boolean isPanning() {
/*  926 */     return this.flickScrollListener.getGestureDetector().isPanning();
/*      */   }
/*      */   
/*      */   public boolean isFlinging() {
/*  930 */     return (this.flingTimer > 0.0F);
/*      */   }
/*      */   
/*      */   public void setVelocityX(float velocityX) {
/*  934 */     this.velocityX = velocityX;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getVelocityX() {
/*  939 */     return this.velocityX;
/*      */   }
/*      */   
/*      */   public void setVelocityY(float velocityY) {
/*  943 */     this.velocityY = velocityY;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getVelocityY() {
/*  948 */     return this.velocityY;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOverscroll(boolean overscrollX, boolean overscrollY) {
/*  954 */     this.overscrollX = overscrollX;
/*  955 */     this.overscrollY = overscrollY;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setupOverscroll(float distance, float speedMin, float speedMax) {
/*  961 */     this.overscrollDistance = distance;
/*  962 */     this.overscrollSpeedMin = speedMin;
/*  963 */     this.overscrollSpeedMax = speedMax;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setForceScroll(boolean x, boolean y) {
/*  969 */     this.forceScrollX = x;
/*  970 */     this.forceScrollY = y;
/*      */   }
/*      */   
/*      */   public boolean isForceScrollX() {
/*  974 */     return this.forceScrollX;
/*      */   }
/*      */   
/*      */   public boolean isForceScrollY() {
/*  978 */     return this.forceScrollY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFlingTime(float flingTime) {
/*  983 */     this.flingTime = flingTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClamp(boolean clamp) {
/*  988 */     this.clamp = clamp;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setScrollBarPositions(boolean bottom, boolean right) {
/*  993 */     this.hScrollOnBottom = bottom;
/*  994 */     this.vScrollOnRight = right;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFadeScrollBars(boolean fadeScrollBars) {
/*  999 */     if (this.fadeScrollBars == fadeScrollBars)
/* 1000 */       return;  this.fadeScrollBars = fadeScrollBars;
/* 1001 */     if (!fadeScrollBars) this.fadeAlpha = this.fadeAlphaSeconds; 
/* 1002 */     invalidate();
/*      */   }
/*      */   
/*      */   public void setupFadeScrollBars(float fadeAlphaSeconds, float fadeDelaySeconds) {
/* 1006 */     this.fadeAlphaSeconds = fadeAlphaSeconds;
/* 1007 */     this.fadeDelaySeconds = fadeDelaySeconds;
/*      */   }
/*      */   
/*      */   public void setSmoothScrolling(boolean smoothScrolling) {
/* 1011 */     this.smoothScrolling = smoothScrolling;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScrollbarsOnTop(boolean scrollbarsOnTop) {
/* 1018 */     this.scrollbarsOnTop = scrollbarsOnTop;
/* 1019 */     invalidate();
/*      */   }
/*      */   
/*      */   public boolean getVariableSizeKnobs() {
/* 1023 */     return this.variableSizeKnobs;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableSizeKnobs(boolean variableSizeKnobs) {
/* 1029 */     this.variableSizeKnobs = variableSizeKnobs;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCancelTouchFocus(boolean cancelTouchFocus) {
/* 1035 */     this.cancelTouchFocus = cancelTouchFocus;
/*      */   }
/*      */   
/*      */   public void drawDebug(ShapeRenderer shapes) {
/* 1039 */     shapes.flush();
/* 1040 */     applyTransform(shapes, computeTransform());
/* 1041 */     if (ScissorStack.pushScissors(this.scissorBounds)) {
/* 1042 */       drawDebugChildren(shapes);
/* 1043 */       ScissorStack.popScissors();
/*      */     } 
/* 1045 */     resetTransform(shapes);
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ScrollPaneStyle
/*      */   {
/*      */     public Drawable background;
/*      */     
/*      */     public Drawable corner;
/*      */     
/*      */     public Drawable hScroll;
/*      */     
/*      */     public Drawable hScrollKnob;
/*      */     public Drawable vScroll;
/*      */     public Drawable vScrollKnob;
/*      */     
/*      */     public ScrollPaneStyle() {}
/*      */     
/*      */     public ScrollPaneStyle(Drawable background, Drawable hScroll, Drawable hScrollKnob, Drawable vScroll, Drawable vScrollKnob) {
/* 1064 */       this.background = background;
/* 1065 */       this.hScroll = hScroll;
/* 1066 */       this.hScrollKnob = hScrollKnob;
/* 1067 */       this.vScroll = vScroll;
/* 1068 */       this.vScrollKnob = vScrollKnob;
/*      */     }
/*      */     
/*      */     public ScrollPaneStyle(ScrollPaneStyle style) {
/* 1072 */       this.background = style.background;
/* 1073 */       this.hScroll = style.hScroll;
/* 1074 */       this.hScrollKnob = style.hScrollKnob;
/* 1075 */       this.vScroll = style.vScroll;
/* 1076 */       this.vScrollKnob = style.vScrollKnob;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\ScrollPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */