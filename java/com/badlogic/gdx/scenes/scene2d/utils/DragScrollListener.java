/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.math.Interpolation;
/*    */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*    */ import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
/*    */ import com.badlogic.gdx.utils.Timer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DragScrollListener
/*    */   extends DragListener
/*    */ {
/*    */   private ScrollPane scroll;
/*    */   private Timer.Task scrollUp;
/*    */   private Timer.Task scrollDown;
/* 30 */   Interpolation interpolation = (Interpolation)Interpolation.exp5In;
/* 31 */   float minSpeed = 15.0F; float maxSpeed = 75.0F; float tickSecs = 0.05F; long startTime;
/* 32 */   long rampTime = 1750L;
/*    */   
/*    */   public DragScrollListener(final ScrollPane scroll) {
/* 35 */     this.scroll = scroll;
/*    */     
/* 37 */     this.scrollUp = new Timer.Task() {
/*    */         public void run() {
/* 39 */           scroll.setScrollY(scroll.getScrollY() - DragScrollListener.this.getScrollPixels());
/*    */         }
/*    */       };
/* 42 */     this.scrollDown = new Timer.Task() {
/*    */         public void run() {
/* 44 */           scroll.setScrollY(scroll.getScrollY() + DragScrollListener.this.getScrollPixels());
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public void setup(float minSpeedPixels, float maxSpeedPixels, float tickSecs, float rampSecs) {
/* 50 */     this.minSpeed = minSpeedPixels;
/* 51 */     this.maxSpeed = maxSpeedPixels;
/* 52 */     this.tickSecs = tickSecs;
/* 53 */     this.rampTime = (long)(rampSecs * 1000.0F);
/*    */   }
/*    */   
/*    */   float getScrollPixels() {
/* 57 */     return this.interpolation.apply(this.minSpeed, this.maxSpeed, Math.min(1.0F, (float)(System.currentTimeMillis() - this.startTime) / (float)this.rampTime));
/*    */   }
/*    */   
/*    */   public void drag(InputEvent event, float x, float y, int pointer) {
/* 61 */     if (x >= 0.0F && x < this.scroll.getWidth()) {
/* 62 */       if (y >= this.scroll.getHeight()) {
/* 63 */         this.scrollDown.cancel();
/* 64 */         if (!this.scrollUp.isScheduled()) {
/* 65 */           this.startTime = System.currentTimeMillis();
/* 66 */           Timer.schedule(this.scrollUp, this.tickSecs, this.tickSecs);
/*    */         }  return;
/*    */       } 
/* 69 */       if (y < 0.0F) {
/* 70 */         this.scrollUp.cancel();
/* 71 */         if (!this.scrollDown.isScheduled()) {
/* 72 */           this.startTime = System.currentTimeMillis();
/* 73 */           Timer.schedule(this.scrollDown, this.tickSecs, this.tickSecs);
/*    */         } 
/*    */         return;
/*    */       } 
/*    */     } 
/* 78 */     this.scrollUp.cancel();
/* 79 */     this.scrollDown.cancel();
/*    */   }
/*    */   
/*    */   public void dragStop(InputEvent event, float x, float y, int pointer) {
/* 83 */     this.scrollUp.cancel();
/* 84 */     this.scrollDown.cancel();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\DragScrollListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */