/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.math.Interpolation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Slider
/*     */   extends ProgressBar
/*     */ {
/*  42 */   int draggingPointer = -1;
/*     */   boolean mouseOver;
/*  44 */   private Interpolation visualInterpolationInverse = Interpolation.linear;
/*     */   private float[] snapValues;
/*     */   private float threshold;
/*     */   
/*     */   public Slider(float min, float max, float stepSize, boolean vertical, Skin skin) {
/*  49 */     this(min, max, stepSize, vertical, skin.<SliderStyle>get("default-" + (vertical ? "vertical" : "horizontal"), SliderStyle.class));
/*     */   }
/*     */   
/*     */   public Slider(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName) {
/*  53 */     this(min, max, stepSize, vertical, skin.<SliderStyle>get(styleName, SliderStyle.class));
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
/*     */   public Slider(float min, float max, float stepSize, boolean vertical, SliderStyle style) {
/*  65 */     super(min, max, stepSize, vertical, style);
/*     */     
/*  67 */     addListener((EventListener)new InputListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  69 */             if (Slider.this.disabled) return false; 
/*  70 */             if (Slider.this.draggingPointer != -1) return false; 
/*  71 */             Slider.this.draggingPointer = pointer;
/*  72 */             Slider.this.calculatePositionAndValue(x, y);
/*  73 */             return true;
/*     */           }
/*     */           
/*     */           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  77 */             if (pointer != Slider.this.draggingPointer)
/*  78 */               return;  Slider.this.draggingPointer = -1;
/*  79 */             if (!Slider.this.calculatePositionAndValue(x, y)) {
/*     */               
/*  81 */               ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/*  82 */               Slider.this.fire((Event)changeEvent);
/*  83 */               Pools.free(changeEvent);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  88 */             Slider.this.calculatePositionAndValue(x, y);
/*     */           }
/*     */ 
/*     */           
/*     */           public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
/*  93 */             if (pointer == -1) Slider.this.mouseOver = true;
/*     */           
/*     */           }
/*     */           
/*     */           public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/*  98 */             if (pointer == -1) Slider.this.mouseOver = false; 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void setStyle(SliderStyle style) {
/* 104 */     if (style == null) throw new NullPointerException("style cannot be null"); 
/* 105 */     if (!(style instanceof SliderStyle)) throw new IllegalArgumentException("style must be a SliderStyle."); 
/* 106 */     setStyle(style);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SliderStyle getStyle() {
/* 112 */     return (SliderStyle)super.getStyle();
/*     */   }
/*     */   
/*     */   protected Drawable getKnobDrawable() {
/* 116 */     SliderStyle style = getStyle();
/* 117 */     return (this.disabled && style.disabledKnob != null) ? style.disabledKnob : ((
/* 118 */       isDragging() && style.knobDown != null) ? style.knobDown : ((this.mouseOver && style.knobOver != null) ? style.knobOver : style.knob));
/*     */   }
/*     */   
/*     */   boolean calculatePositionAndValue(float x, float y) {
/*     */     float value;
/* 123 */     SliderStyle style = getStyle();
/* 124 */     Drawable knob = getKnobDrawable();
/* 125 */     Drawable bg = (this.disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
/*     */ 
/*     */     
/* 128 */     float oldPosition = this.position;
/*     */     
/* 130 */     float min = getMinValue();
/* 131 */     float max = getMaxValue();
/*     */     
/* 133 */     if (this.vertical) {
/* 134 */       float height = getHeight() - bg.getTopHeight() - bg.getBottomHeight();
/* 135 */       float knobHeight = (knob == null) ? 0.0F : knob.getMinHeight();
/* 136 */       this.position = y - bg.getBottomHeight() - knobHeight * 0.5F;
/* 137 */       value = min + (max - min) * this.visualInterpolationInverse.apply(this.position / (height - knobHeight));
/* 138 */       this.position = Math.max(0.0F, this.position);
/* 139 */       this.position = Math.min(height - knobHeight, this.position);
/*     */     } else {
/* 141 */       float width = getWidth() - bg.getLeftWidth() - bg.getRightWidth();
/* 142 */       float knobWidth = (knob == null) ? 0.0F : knob.getMinWidth();
/* 143 */       this.position = x - bg.getLeftWidth() - knobWidth * 0.5F;
/* 144 */       value = min + (max - min) * this.visualInterpolationInverse.apply(this.position / (width - knobWidth));
/* 145 */       this.position = Math.max(0.0F, this.position);
/* 146 */       this.position = Math.min(width - knobWidth, this.position);
/*     */     } 
/*     */     
/* 149 */     float oldValue = value;
/* 150 */     if (!Gdx.input.isKeyPressed(59) && !Gdx.input.isKeyPressed(60)) value = snap(value); 
/* 151 */     boolean valueSet = setValue(value);
/* 152 */     if (value == oldValue) this.position = oldPosition; 
/* 153 */     return valueSet;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float snap(float value) {
/* 158 */     if (this.snapValues == null) return value; 
/* 159 */     for (int i = 0; i < this.snapValues.length; i++) {
/* 160 */       if (Math.abs(value - this.snapValues[i]) <= this.threshold) return this.snapValues[i]; 
/*     */     } 
/* 162 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSnapToValues(float[] values, float threshold) {
/* 168 */     this.snapValues = values;
/* 169 */     this.threshold = threshold;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDragging() {
/* 174 */     return (this.draggingPointer != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisualInterpolationInverse(Interpolation interpolation) {
/* 180 */     this.visualInterpolationInverse = interpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SliderStyle
/*     */     extends ProgressBar.ProgressBarStyle
/*     */   {
/*     */     public Drawable knobOver;
/*     */     
/*     */     public Drawable knobDown;
/*     */     
/*     */     public SliderStyle() {}
/*     */     
/*     */     public SliderStyle(Drawable background, Drawable knob) {
/* 194 */       super(background, knob);
/*     */     }
/*     */     
/*     */     public SliderStyle(SliderStyle style) {
/* 198 */       super(style);
/* 199 */       this.knobOver = style.knobOver;
/* 200 */       this.knobDown = style.knobDown;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Slider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */