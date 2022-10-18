/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
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
/*     */ public class DragListener
/*     */   extends InputListener
/*     */ {
/*  28 */   private float tapSquareSize = 14.0F; private float touchDownX = -1.0F; private float touchDownY = -1.0F; private float stageTouchDownX = -1.0F; private float stageTouchDownY = -1.0F;
/*  29 */   private int pressedPointer = -1; private int button;
/*     */   private boolean dragging;
/*     */   private float deltaX;
/*     */   private float deltaY;
/*     */   
/*     */   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  35 */     if (this.pressedPointer != -1) return false; 
/*  36 */     if (pointer == 0 && this.button != -1 && button != this.button) return false; 
/*  37 */     this.pressedPointer = pointer;
/*  38 */     this.touchDownX = x;
/*  39 */     this.touchDownY = y;
/*  40 */     this.stageTouchDownX = event.getStageX();
/*  41 */     this.stageTouchDownY = event.getStageY();
/*  42 */     return true;
/*     */   }
/*     */   
/*     */   public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  46 */     if (pointer != this.pressedPointer)
/*  47 */       return;  if (!this.dragging && (Math.abs(this.touchDownX - x) > this.tapSquareSize || Math.abs(this.touchDownY - y) > this.tapSquareSize)) {
/*  48 */       this.dragging = true;
/*  49 */       dragStart(event, x, y, pointer);
/*  50 */       this.deltaX = x;
/*  51 */       this.deltaY = y;
/*     */     } 
/*  53 */     if (this.dragging) {
/*  54 */       this.deltaX -= x;
/*  55 */       this.deltaY -= y;
/*  56 */       drag(event, x, y, pointer);
/*  57 */       this.deltaX = x;
/*  58 */       this.deltaY = y;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  63 */     if (pointer == this.pressedPointer) {
/*  64 */       if (this.dragging) dragStop(event, x, y, pointer); 
/*  65 */       cancel();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dragStart(InputEvent event, float x, float y, int pointer) {}
/*     */ 
/*     */   
/*     */   public void drag(InputEvent event, float x, float y, int pointer) {}
/*     */ 
/*     */   
/*     */   public void dragStop(InputEvent event, float x, float y, int pointer) {}
/*     */ 
/*     */   
/*     */   public void cancel() {
/*  80 */     this.dragging = false;
/*  81 */     this.pressedPointer = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDragging() {
/*  86 */     return this.dragging;
/*     */   }
/*     */   
/*     */   public void setTapSquareSize(float halfTapSquareSize) {
/*  90 */     this.tapSquareSize = halfTapSquareSize;
/*     */   }
/*     */   
/*     */   public float getTapSquareSize() {
/*  94 */     return this.tapSquareSize;
/*     */   }
/*     */   
/*     */   public float getTouchDownX() {
/*  98 */     return this.touchDownX;
/*     */   }
/*     */   
/*     */   public float getTouchDownY() {
/* 102 */     return this.touchDownY;
/*     */   }
/*     */   
/*     */   public float getStageTouchDownX() {
/* 106 */     return this.stageTouchDownX;
/*     */   }
/*     */   
/*     */   public float getStageTouchDownY() {
/* 110 */     return this.stageTouchDownY;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDeltaX() {
/* 115 */     return this.deltaX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDeltaY() {
/* 120 */     return this.deltaY;
/*     */   }
/*     */   
/*     */   public int getButton() {
/* 124 */     return this.button;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setButton(int button) {
/* 129 */     this.button = button;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\DragListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */