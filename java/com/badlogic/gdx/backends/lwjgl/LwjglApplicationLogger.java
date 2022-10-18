/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.ApplicationLogger;
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
/*    */ public class LwjglApplicationLogger
/*    */   implements ApplicationLogger
/*    */ {
/*    */   public void log(String tag, String message) {
/* 28 */     System.out.println(tag + ": " + message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void log(String tag, String message, Throwable exception) {
/* 33 */     System.out.println(tag + ": " + message);
/* 34 */     exception.printStackTrace(System.out);
/*    */   }
/*    */ 
/*    */   
/*    */   public void error(String tag, String message) {
/* 39 */     System.err.println(tag + ": " + message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void error(String tag, String message, Throwable exception) {
/* 44 */     System.err.println(tag + ": " + message);
/* 45 */     exception.printStackTrace(System.err);
/*    */   }
/*    */ 
/*    */   
/*    */   public void debug(String tag, String message) {
/* 50 */     System.out.println(tag + ": " + message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void debug(String tag, String message, Throwable exception) {
/* 55 */     System.out.println(tag + ": " + message);
/* 56 */     exception.printStackTrace(System.out);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglApplicationLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */