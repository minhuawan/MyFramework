/*    */ package com.badlogic.gdx.utils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PauseableThread
/*    */   extends Thread
/*    */ {
/*    */   final Runnable runnable;
/*    */   boolean paused = false;
/*    */   boolean exit = false;
/*    */   
/*    */   public PauseableThread(Runnable runnable) {
/* 32 */     this.runnable = runnable;
/*    */   }
/*    */   
/*    */   public void run() {
/*    */     while (true) {
/* 37 */       synchronized (this) { while (true) {
/*    */           try {
/* 39 */             if (this.paused)
/* 40 */             { wait(); continue; } 
/* 41 */           } catch (InterruptedException e) {
/* 42 */             e.printStackTrace();
/*    */           }  break;
/*    */         }  }
/*    */       
/* 46 */       if (this.exit)
/*    */         return; 
/* 48 */       this.runnable.run();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPause() {
/* 54 */     this.paused = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onResume() {
/* 59 */     synchronized (this) {
/* 60 */       this.paused = false;
/* 61 */       notifyAll();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPaused() {
/* 67 */     return this.paused;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopThread() {
/* 72 */     this.exit = true;
/* 73 */     if (this.paused) onResume(); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\PauseableThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */