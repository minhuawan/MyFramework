/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.scenes.scene2d.Event;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
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
/*     */ 
/*     */ 
/*     */ public class ProgressBar
/*     */   extends Widget
/*     */   implements Disableable
/*     */ {
/*     */   private ProgressBarStyle style;
/*     */   private float min;
/*     */   private float max;
/*     */   private float stepSize;
/*     */   private float value;
/*     */   private float animateFromValue;
/*     */   float position;
/*     */   final boolean vertical;
/*     */   private float animateDuration;
/*     */   private float animateTime;
/*  51 */   private Interpolation animateInterpolation = Interpolation.linear;
/*     */   boolean disabled;
/*  53 */   private Interpolation visualInterpolation = Interpolation.linear;
/*     */   private boolean round = true;
/*     */   
/*     */   public ProgressBar(float min, float max, float stepSize, boolean vertical, Skin skin) {
/*  57 */     this(min, max, stepSize, vertical, skin.<ProgressBarStyle>get("default-" + (vertical ? "vertical" : "horizontal"), ProgressBarStyle.class));
/*     */   }
/*     */   
/*     */   public ProgressBar(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName) {
/*  61 */     this(min, max, stepSize, vertical, skin.<ProgressBarStyle>get(styleName, ProgressBarStyle.class));
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
/*     */   public ProgressBar(float min, float max, float stepSize, boolean vertical, ProgressBarStyle style) {
/*  75 */     if (min > max) throw new IllegalArgumentException("max must be > min. min,max: " + min + ", " + max); 
/*  76 */     if (stepSize <= 0.0F) throw new IllegalArgumentException("stepSize must be > 0: " + stepSize); 
/*  77 */     setStyle(style);
/*  78 */     this.min = min;
/*  79 */     this.max = max;
/*  80 */     this.stepSize = stepSize;
/*  81 */     this.vertical = vertical;
/*  82 */     this.value = min;
/*  83 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public void setStyle(ProgressBarStyle style) {
/*  87 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  88 */     this.style = style;
/*  89 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ProgressBarStyle getStyle() {
/*  95 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public void act(float delta) {
/* 100 */     super.act(delta);
/* 101 */     if (this.animateTime > 0.0F) {
/* 102 */       this.animateTime -= delta;
/* 103 */       Stage stage = getStage();
/* 104 */       if (stage != null && stage.getActionsRequestRendering()) Gdx.graphics.requestRendering();
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 110 */     ProgressBarStyle style = this.style;
/* 111 */     boolean disabled = this.disabled;
/* 112 */     Drawable knob = getKnobDrawable();
/* 113 */     Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
/* 114 */     Drawable knobBefore = (disabled && style.disabledKnobBefore != null) ? style.disabledKnobBefore : style.knobBefore;
/* 115 */     Drawable knobAfter = (disabled && style.disabledKnobAfter != null) ? style.disabledKnobAfter : style.knobAfter;
/*     */     
/* 117 */     Color color = getColor();
/* 118 */     float x = getX();
/* 119 */     float y = getY();
/* 120 */     float width = getWidth();
/* 121 */     float height = getHeight();
/* 122 */     float knobHeight = (knob == null) ? 0.0F : knob.getMinHeight();
/* 123 */     float knobWidth = (knob == null) ? 0.0F : knob.getMinWidth();
/* 124 */     float percent = getVisualPercent();
/*     */     
/* 126 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*     */     
/* 128 */     if (this.vertical) {
/* 129 */       float positionHeight = height;
/*     */       
/* 131 */       float bgTopHeight = 0.0F;
/* 132 */       if (bg != null) {
/* 133 */         if (this.round) {
/* 134 */           bg.draw(batch, Math.round(x + (width - bg.getMinWidth()) * 0.5F), y, Math.round(bg.getMinWidth()), height);
/*     */         } else {
/* 136 */           bg.draw(batch, x + width - bg.getMinWidth() * 0.5F, y, bg.getMinWidth(), height);
/* 137 */         }  bgTopHeight = bg.getTopHeight();
/* 138 */         positionHeight -= bgTopHeight + bg.getBottomHeight();
/*     */       } 
/*     */       
/* 141 */       float knobHeightHalf = 0.0F;
/* 142 */       if (this.min != this.max) {
/* 143 */         if (knob == null) {
/* 144 */           knobHeightHalf = (knobBefore == null) ? 0.0F : (knobBefore.getMinHeight() * 0.5F);
/* 145 */           this.position = (positionHeight - knobHeightHalf) * percent;
/* 146 */           this.position = Math.min(positionHeight - knobHeightHalf, this.position);
/*     */         } else {
/* 148 */           knobHeightHalf = knobHeight * 0.5F;
/* 149 */           this.position = (positionHeight - knobHeight) * percent;
/* 150 */           this.position = Math.min(positionHeight - knobHeight, this.position) + bg.getBottomHeight();
/*     */         } 
/* 152 */         this.position = Math.max(0.0F, this.position);
/*     */       } 
/*     */       
/* 155 */       if (knobBefore != null) {
/* 156 */         float offset = 0.0F;
/* 157 */         if (bg != null) offset = bgTopHeight; 
/* 158 */         if (this.round) {
/* 159 */           knobBefore.draw(batch, Math.round(x + (width - knobBefore.getMinWidth()) * 0.5F), Math.round(y + offset), Math.round(knobBefore.getMinWidth()), 
/* 160 */               Math.round(this.position + knobHeightHalf));
/*     */         } else {
/* 162 */           knobBefore.draw(batch, x + (width - knobBefore.getMinWidth()) * 0.5F, y + offset, knobBefore.getMinWidth(), this.position + knobHeightHalf);
/*     */         } 
/*     */       } 
/* 165 */       if (knobAfter != null)
/* 166 */         if (this.round) {
/* 167 */           knobAfter.draw(batch, Math.round(x + (width - knobAfter.getMinWidth()) * 0.5F), Math.round(y + this.position + knobHeightHalf), 
/* 168 */               Math.round(knobAfter.getMinWidth()), Math.round(height - this.position - knobHeightHalf));
/*     */         } else {
/* 170 */           knobAfter.draw(batch, x + (width - knobAfter.getMinWidth()) * 0.5F, y + this.position + knobHeightHalf, knobAfter
/* 171 */               .getMinWidth(), height - this.position - knobHeightHalf);
/*     */         }  
/* 173 */       if (knob != null)
/* 174 */         if (this.round) {
/* 175 */           knob.draw(batch, Math.round(x + (width - knobWidth) * 0.5F), Math.round(y + this.position), Math.round(knobWidth), Math.round(knobHeight));
/*     */         } else {
/* 177 */           knob.draw(batch, x + (width - knobWidth) * 0.5F, y + this.position, knobWidth, knobHeight);
/*     */         }  
/*     */     } else {
/* 180 */       float positionWidth = width;
/*     */       
/* 182 */       float bgLeftWidth = 0.0F;
/* 183 */       if (bg != null) {
/* 184 */         if (this.round) {
/* 185 */           bg.draw(batch, x, Math.round(y + (height - bg.getMinHeight()) * 0.5F), width, Math.round(bg.getMinHeight()));
/*     */         } else {
/* 187 */           bg.draw(batch, x, y + (height - bg.getMinHeight()) * 0.5F, width, bg.getMinHeight());
/* 188 */         }  bgLeftWidth = bg.getLeftWidth();
/* 189 */         positionWidth -= bgLeftWidth + bg.getRightWidth();
/*     */       } 
/*     */       
/* 192 */       float knobWidthHalf = 0.0F;
/* 193 */       if (this.min != this.max) {
/* 194 */         if (knob == null) {
/* 195 */           knobWidthHalf = (knobBefore == null) ? 0.0F : (knobBefore.getMinWidth() * 0.5F);
/* 196 */           this.position = (positionWidth - knobWidthHalf) * percent;
/* 197 */           this.position = Math.min(positionWidth - knobWidthHalf, this.position);
/*     */         } else {
/* 199 */           knobWidthHalf = knobWidth * 0.5F;
/* 200 */           this.position = (positionWidth - knobWidth) * percent;
/* 201 */           this.position = Math.min(positionWidth - knobWidth, this.position) + bgLeftWidth;
/*     */         } 
/* 203 */         this.position = Math.max(0.0F, this.position);
/*     */       } 
/*     */       
/* 206 */       if (knobBefore != null) {
/* 207 */         float offset = 0.0F;
/* 208 */         if (bg != null) offset = bgLeftWidth; 
/* 209 */         if (this.round) {
/* 210 */           knobBefore.draw(batch, Math.round(x + offset), Math.round(y + (height - knobBefore.getMinHeight()) * 0.5F), 
/* 211 */               Math.round(this.position + knobWidthHalf), Math.round(knobBefore.getMinHeight()));
/*     */         } else {
/* 213 */           knobBefore.draw(batch, x + offset, y + (height - knobBefore.getMinHeight()) * 0.5F, this.position + knobWidthHalf, knobBefore
/* 214 */               .getMinHeight());
/*     */         } 
/* 216 */       }  if (knobAfter != null)
/* 217 */         if (this.round) {
/* 218 */           knobAfter.draw(batch, Math.round(x + this.position + knobWidthHalf), Math.round(y + (height - knobAfter.getMinHeight()) * 0.5F), 
/* 219 */               Math.round(width - this.position - knobWidthHalf), Math.round(knobAfter.getMinHeight()));
/*     */         } else {
/* 221 */           knobAfter.draw(batch, x + this.position + knobWidthHalf, y + (height - knobAfter.getMinHeight()) * 0.5F, width - this.position - knobWidthHalf, knobAfter
/* 222 */               .getMinHeight());
/*     */         }  
/* 224 */       if (knob != null)
/* 225 */         if (this.round) {
/* 226 */           knob.draw(batch, Math.round(x + this.position), Math.round(y + (height - knobHeight) * 0.5F), Math.round(knobWidth), Math.round(knobHeight));
/*     */         } else {
/* 228 */           knob.draw(batch, x + this.position, y + (height - knobHeight) * 0.5F, knobWidth, knobHeight);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getValue() {
/* 234 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVisualValue() {
/* 239 */     if (this.animateTime > 0.0F) return this.animateInterpolation.apply(this.animateFromValue, this.value, 1.0F - this.animateTime / this.animateDuration); 
/* 240 */     return this.value;
/*     */   }
/*     */   
/*     */   public float getPercent() {
/* 244 */     return (this.value - this.min) / (this.max - this.min);
/*     */   }
/*     */   
/*     */   public float getVisualPercent() {
/* 248 */     return this.visualInterpolation.apply((getVisualValue() - this.min) / (this.max - this.min));
/*     */   }
/*     */   
/*     */   protected Drawable getKnobDrawable() {
/* 252 */     return (this.disabled && this.style.disabledKnob != null) ? this.style.disabledKnob : this.style.knob;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getKnobPosition() {
/* 257 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setValue(float value) {
/* 265 */     value = clamp(Math.round(value / this.stepSize) * this.stepSize);
/* 266 */     float oldValue = this.value;
/* 267 */     if (value == oldValue) return false; 
/* 268 */     float oldVisualValue = getVisualValue();
/* 269 */     this.value = value;
/* 270 */     ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/* 271 */     boolean cancelled = fire((Event)changeEvent);
/* 272 */     if (cancelled) {
/* 273 */       this.value = oldValue;
/* 274 */     } else if (this.animateDuration > 0.0F) {
/* 275 */       this.animateFromValue = oldVisualValue;
/* 276 */       this.animateTime = this.animateDuration;
/*     */     } 
/* 278 */     Pools.free(changeEvent);
/* 279 */     return !cancelled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float clamp(float value) {
/* 285 */     return MathUtils.clamp(value, this.min, this.max);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRange(float min, float max) {
/* 290 */     if (min > max) throw new IllegalArgumentException("min must be <= max"); 
/* 291 */     this.min = min;
/* 292 */     this.max = max;
/* 293 */     if (this.value < min)
/* 294 */     { setValue(min); }
/* 295 */     else if (this.value > max) { setValue(max); }
/*     */   
/*     */   }
/*     */   public void setStepSize(float stepSize) {
/* 299 */     if (stepSize <= 0.0F) throw new IllegalArgumentException("steps must be > 0: " + stepSize); 
/* 300 */     this.stepSize = stepSize;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 304 */     if (this.vertical) {
/* 305 */       Drawable knob = getKnobDrawable();
/* 306 */       Drawable bg = (this.disabled && this.style.disabledBackground != null) ? this.style.disabledBackground : this.style.background;
/* 307 */       return Math.max((knob == null) ? 0.0F : knob.getMinWidth(), bg.getMinWidth());
/*     */     } 
/* 309 */     return 140.0F;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 313 */     if (this.vertical) {
/* 314 */       return 140.0F;
/*     */     }
/* 316 */     Drawable knob = getKnobDrawable();
/* 317 */     Drawable bg = (this.disabled && this.style.disabledBackground != null) ? this.style.disabledBackground : this.style.background;
/* 318 */     return Math.max((knob == null) ? 0.0F : knob.getMinHeight(), (bg == null) ? 0.0F : bg.getMinHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMinValue() {
/* 323 */     return this.min;
/*     */   }
/*     */   
/*     */   public float getMaxValue() {
/* 327 */     return this.max;
/*     */   }
/*     */   
/*     */   public float getStepSize() {
/* 331 */     return this.stepSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnimateDuration(float duration) {
/* 336 */     this.animateDuration = duration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnimateInterpolation(Interpolation animateInterpolation) {
/* 341 */     if (animateInterpolation == null) throw new IllegalArgumentException("animateInterpolation cannot be null."); 
/* 342 */     this.animateInterpolation = animateInterpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisualInterpolation(Interpolation interpolation) {
/* 347 */     this.visualInterpolation = interpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRound(boolean round) {
/* 352 */     this.round = round;
/*     */   }
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 356 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 360 */     return this.disabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ProgressBarStyle
/*     */   {
/*     */     public Drawable background;
/*     */     
/*     */     public Drawable disabledBackground;
/*     */     
/*     */     public Drawable knob;
/*     */     public Drawable disabledKnob;
/*     */     public Drawable knobBefore;
/*     */     public Drawable knobAfter;
/*     */     public Drawable disabledKnobBefore;
/*     */     public Drawable disabledKnobAfter;
/*     */     
/*     */     public ProgressBarStyle() {}
/*     */     
/*     */     public ProgressBarStyle(Drawable background, Drawable knob) {
/* 380 */       this.background = background;
/* 381 */       this.knob = knob;
/*     */     }
/*     */     
/*     */     public ProgressBarStyle(ProgressBarStyle style) {
/* 385 */       this.background = style.background;
/* 386 */       this.disabledBackground = style.disabledBackground;
/* 387 */       this.knob = style.knob;
/* 388 */       this.disabledKnob = style.disabledKnob;
/* 389 */       this.knobBefore = style.knobBefore;
/* 390 */       this.knobAfter = style.knobAfter;
/* 391 */       this.disabledKnobBefore = style.disabledKnobBefore;
/* 392 */       this.disabledKnobAfter = style.disabledKnobAfter;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\ProgressBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */