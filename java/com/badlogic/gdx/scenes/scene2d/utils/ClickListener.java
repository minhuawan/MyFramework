/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
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
/*     */ public class ClickListener
/*     */   extends InputListener
/*     */ {
/*  34 */   public static float visualPressedDuration = 0.1F;
/*     */   
/*  36 */   private float tapSquareSize = 14.0F; private float touchDownX = -1.0F; private float touchDownY = -1.0F;
/*  37 */   private int pressedPointer = -1;
/*  38 */   private int pressedButton = -1;
/*     */   
/*     */   private int button;
/*     */   private boolean pressed;
/*  42 */   private long tapCountInterval = 400000000L;
/*     */   
/*     */   private boolean over;
/*     */   private boolean cancelled;
/*     */   private long visualPressedTime;
/*     */   private int tapCount;
/*     */   private long lastTapTime;
/*     */   
/*     */   public ClickListener() {}
/*     */   
/*     */   public ClickListener(int button) {
/*  53 */     this.button = button;
/*     */   }
/*     */   
/*     */   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  57 */     if (this.pressed) return false; 
/*  58 */     if (pointer == 0 && this.button != -1 && button != this.button) return false; 
/*  59 */     this.pressed = true;
/*  60 */     this.pressedPointer = pointer;
/*  61 */     this.pressedButton = button;
/*  62 */     this.touchDownX = x;
/*  63 */     this.touchDownY = y;
/*  64 */     this.visualPressedTime = TimeUtils.millis() + (long)(visualPressedDuration * 1000.0F);
/*  65 */     return true;
/*     */   }
/*     */   
/*     */   public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  69 */     if (pointer != this.pressedPointer || this.cancelled)
/*  70 */       return;  this.pressed = isOver(event.getListenerActor(), x, y);
/*  71 */     if (this.pressed && pointer == 0 && this.button != -1 && !Gdx.input.isButtonPressed(this.button)) this.pressed = false; 
/*  72 */     if (!this.pressed)
/*     */     {
/*  74 */       invalidateTapSquare();
/*     */     }
/*     */   }
/*     */   
/*     */   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  79 */     if (pointer == this.pressedPointer) {
/*  80 */       if (!this.cancelled) {
/*  81 */         boolean touchUpOver = isOver(event.getListenerActor(), x, y);
/*     */         
/*  83 */         if (touchUpOver && pointer == 0 && this.button != -1 && button != this.button) touchUpOver = false; 
/*  84 */         if (touchUpOver) {
/*  85 */           long time = TimeUtils.nanoTime();
/*  86 */           if (time - this.lastTapTime > this.tapCountInterval) this.tapCount = 0; 
/*  87 */           this.tapCount++;
/*  88 */           this.lastTapTime = time;
/*  89 */           clicked(event, x, y);
/*     */         } 
/*     */       } 
/*  92 */       this.pressed = false;
/*  93 */       this.pressedPointer = -1;
/*  94 */       this.pressedButton = -1;
/*  95 */       this.cancelled = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
/* 100 */     if (pointer == -1 && !this.cancelled) this.over = true; 
/*     */   }
/*     */   
/*     */   public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/* 104 */     if (pointer == -1 && !this.cancelled) this.over = false;
/*     */   
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 109 */     if (this.pressedPointer == -1)
/* 110 */       return;  this.cancelled = true;
/* 111 */     this.pressed = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clicked(InputEvent event, float x, float y) {}
/*     */ 
/*     */   
/*     */   public boolean isOver(Actor actor, float x, float y) {
/* 119 */     Actor hit = actor.hit(x, y, true);
/* 120 */     if (hit == null || !hit.isDescendantOf(actor)) return inTapSquare(x, y); 
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   public boolean inTapSquare(float x, float y) {
/* 125 */     if (this.touchDownX == -1.0F && this.touchDownY == -1.0F) return false; 
/* 126 */     return (Math.abs(x - this.touchDownX) < this.tapSquareSize && Math.abs(y - this.touchDownY) < this.tapSquareSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean inTapSquare() {
/* 131 */     return (this.touchDownX != -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateTapSquare() {
/* 136 */     this.touchDownX = -1.0F;
/* 137 */     this.touchDownY = -1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPressed() {
/* 142 */     return this.pressed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVisualPressed() {
/* 148 */     if (this.pressed) return true; 
/* 149 */     if (this.visualPressedTime <= 0L) return false; 
/* 150 */     if (this.visualPressedTime > TimeUtils.millis()) return true; 
/* 151 */     this.visualPressedTime = 0L;
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOver() {
/* 157 */     return (this.over || this.pressed);
/*     */   }
/*     */   
/*     */   public void setTapSquareSize(float halfTapSquareSize) {
/* 161 */     this.tapSquareSize = halfTapSquareSize;
/*     */   }
/*     */   
/*     */   public float getTapSquareSize() {
/* 165 */     return this.tapSquareSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTapCountInterval(float tapCountInterval) {
/* 170 */     this.tapCountInterval = (long)(tapCountInterval * 1.0E9F);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTapCount() {
/* 175 */     return this.tapCount;
/*     */   }
/*     */   
/*     */   public void setTapCount(int tapCount) {
/* 179 */     this.tapCount = tapCount;
/*     */   }
/*     */   
/*     */   public float getTouchDownX() {
/* 183 */     return this.touchDownX;
/*     */   }
/*     */   
/*     */   public float getTouchDownY() {
/* 187 */     return this.touchDownY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPressedButton() {
/* 192 */     return this.pressedButton;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPressedPointer() {
/* 197 */     return this.pressedPointer;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getButton() {
/* 202 */     return this.button;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setButton(int button) {
/* 207 */     this.button = button;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\ClickListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */