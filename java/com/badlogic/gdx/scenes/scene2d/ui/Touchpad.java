/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.math.Circle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Event;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.Pools;
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
/*     */ public class Touchpad
/*     */   extends Widget
/*     */ {
/*     */   private TouchpadStyle style;
/*     */   boolean touched;
/*     */   boolean resetOnTouchUp = true;
/*     */   private float deadzoneRadius;
/*  43 */   private final Circle knobBounds = new Circle(0.0F, 0.0F, 0.0F);
/*  44 */   private final Circle touchBounds = new Circle(0.0F, 0.0F, 0.0F);
/*  45 */   private final Circle deadzoneBounds = new Circle(0.0F, 0.0F, 0.0F);
/*  46 */   private final Vector2 knobPosition = new Vector2();
/*  47 */   private final Vector2 knobPercent = new Vector2();
/*     */ 
/*     */   
/*     */   public Touchpad(float deadzoneRadius, Skin skin) {
/*  51 */     this(deadzoneRadius, skin.<TouchpadStyle>get(TouchpadStyle.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public Touchpad(float deadzoneRadius, Skin skin, String styleName) {
/*  56 */     this(deadzoneRadius, skin.<TouchpadStyle>get(styleName, TouchpadStyle.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public Touchpad(float deadzoneRadius, TouchpadStyle style) {
/*  61 */     if (deadzoneRadius < 0.0F) throw new IllegalArgumentException("deadzoneRadius must be > 0"); 
/*  62 */     this.deadzoneRadius = deadzoneRadius;
/*     */     
/*  64 */     this.knobPosition.set(getWidth() / 2.0F, getHeight() / 2.0F);
/*     */     
/*  66 */     setStyle(style);
/*  67 */     setSize(getPrefWidth(), getPrefHeight());
/*     */     
/*  69 */     addListener((EventListener)new InputListener()
/*     */         {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  72 */             if (Touchpad.this.touched) return false; 
/*  73 */             Touchpad.this.touched = true;
/*  74 */             Touchpad.this.calculatePositionAndValue(x, y, false);
/*  75 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  80 */             Touchpad.this.calculatePositionAndValue(x, y, false);
/*     */           }
/*     */ 
/*     */           
/*     */           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  85 */             Touchpad.this.touched = false;
/*  86 */             Touchpad.this.calculatePositionAndValue(x, y, Touchpad.this.resetOnTouchUp);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   void calculatePositionAndValue(float x, float y, boolean isTouchUp) {
/*  92 */     float oldPositionX = this.knobPosition.x;
/*  93 */     float oldPositionY = this.knobPosition.y;
/*  94 */     float oldPercentX = this.knobPercent.x;
/*  95 */     float oldPercentY = this.knobPercent.y;
/*  96 */     float centerX = this.knobBounds.x;
/*  97 */     float centerY = this.knobBounds.y;
/*  98 */     this.knobPosition.set(centerX, centerY);
/*  99 */     this.knobPercent.set(0.0F, 0.0F);
/* 100 */     if (!isTouchUp && 
/* 101 */       !this.deadzoneBounds.contains(x, y)) {
/* 102 */       this.knobPercent.set((x - centerX) / this.knobBounds.radius, (y - centerY) / this.knobBounds.radius);
/* 103 */       float length = this.knobPercent.len();
/* 104 */       if (length > 1.0F) this.knobPercent.scl(1.0F / length); 
/* 105 */       if (this.knobBounds.contains(x, y)) {
/* 106 */         this.knobPosition.set(x, y);
/*     */       } else {
/* 108 */         this.knobPosition.set(this.knobPercent).nor().scl(this.knobBounds.radius).add(this.knobBounds.x, this.knobBounds.y);
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     if (oldPercentX != this.knobPercent.x || oldPercentY != this.knobPercent.y) {
/* 113 */       ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/* 114 */       if (fire((Event)changeEvent)) {
/* 115 */         this.knobPercent.set(oldPercentX, oldPercentY);
/* 116 */         this.knobPosition.set(oldPositionX, oldPositionY);
/*     */       } 
/* 118 */       Pools.free(changeEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setStyle(TouchpadStyle style) {
/* 123 */     if (style == null) throw new IllegalArgumentException("style cannot be null"); 
/* 124 */     this.style = style;
/* 125 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TouchpadStyle getStyle() {
/* 131 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 136 */     return this.touchBounds.contains(x, y) ? this : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void layout() {
/* 142 */     float halfWidth = getWidth() / 2.0F;
/* 143 */     float halfHeight = getHeight() / 2.0F;
/* 144 */     float radius = Math.min(halfWidth, halfHeight);
/* 145 */     this.touchBounds.set(halfWidth, halfHeight, radius);
/* 146 */     if (this.style.knob != null) radius -= Math.max(this.style.knob.getMinWidth(), this.style.knob.getMinHeight()) / 2.0F; 
/* 147 */     this.knobBounds.set(halfWidth, halfHeight, radius);
/* 148 */     this.deadzoneBounds.set(halfWidth, halfHeight, this.deadzoneRadius);
/*     */     
/* 150 */     this.knobPosition.set(halfWidth, halfHeight);
/* 151 */     this.knobPercent.set(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 156 */     validate();
/*     */     
/* 158 */     Color c = getColor();
/* 159 */     batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
/*     */     
/* 161 */     float x = getX();
/* 162 */     float y = getY();
/* 163 */     float w = getWidth();
/* 164 */     float h = getHeight();
/*     */     
/* 166 */     Drawable bg = this.style.background;
/* 167 */     if (bg != null) bg.draw(batch, x, y, w, h);
/*     */     
/* 169 */     Drawable knob = this.style.knob;
/* 170 */     if (knob != null) {
/* 171 */       x += this.knobPosition.x - knob.getMinWidth() / 2.0F;
/* 172 */       y += this.knobPosition.y - knob.getMinHeight() / 2.0F;
/* 173 */       knob.draw(batch, x, y, knob.getMinWidth(), knob.getMinHeight());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPrefWidth() {
/* 179 */     return (this.style.background != null) ? this.style.background.getMinWidth() : 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPrefHeight() {
/* 184 */     return (this.style.background != null) ? this.style.background.getMinHeight() : 0.0F;
/*     */   }
/*     */   
/*     */   public boolean isTouched() {
/* 188 */     return this.touched;
/*     */   }
/*     */   
/*     */   public boolean getResetOnTouchUp() {
/* 192 */     return this.resetOnTouchUp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setResetOnTouchUp(boolean reset) {
/* 197 */     this.resetOnTouchUp = reset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDeadzone(float deadzoneRadius) {
/* 202 */     if (deadzoneRadius < 0.0F) throw new IllegalArgumentException("deadzoneRadius must be > 0"); 
/* 203 */     this.deadzoneRadius = deadzoneRadius;
/* 204 */     invalidate();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getKnobX() {
/* 209 */     return this.knobPosition.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getKnobY() {
/* 214 */     return this.knobPosition.y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getKnobPercentX() {
/* 220 */     return this.knobPercent.x;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getKnobPercentY() {
/* 226 */     return this.knobPercent.y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TouchpadStyle
/*     */   {
/*     */     public Drawable background;
/*     */     
/*     */     public Drawable knob;
/*     */ 
/*     */     
/*     */     public TouchpadStyle() {}
/*     */ 
/*     */     
/*     */     public TouchpadStyle(Drawable background, Drawable knob) {
/* 242 */       this.background = background;
/* 243 */       this.knob = knob;
/*     */     }
/*     */     
/*     */     public TouchpadStyle(TouchpadStyle style) {
/* 247 */       this.background = style.background;
/* 248 */       this.knob = style.knob;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Touchpad.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */