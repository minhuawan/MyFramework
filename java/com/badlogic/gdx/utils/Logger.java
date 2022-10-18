/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
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
/*    */ public class Logger
/*    */ {
/*    */   public static final int NONE = 0;
/*    */   public static final int ERROR = 1;
/*    */   public static final int INFO = 2;
/*    */   public static final int DEBUG = 3;
/*    */   private final String tag;
/*    */   private int level;
/*    */   
/*    */   public Logger(String tag) {
/* 36 */     this(tag, 1);
/*    */   }
/*    */   
/*    */   public Logger(String tag, int level) {
/* 40 */     this.tag = tag;
/* 41 */     this.level = level;
/*    */   }
/*    */   
/*    */   public void debug(String message) {
/* 45 */     if (this.level >= 3) Gdx.app.debug(this.tag, message); 
/*    */   }
/*    */   
/*    */   public void debug(String message, Exception exception) {
/* 49 */     if (this.level >= 3) Gdx.app.debug(this.tag, message, exception); 
/*    */   }
/*    */   
/*    */   public void info(String message) {
/* 53 */     if (this.level >= 2) Gdx.app.log(this.tag, message); 
/*    */   }
/*    */   
/*    */   public void info(String message, Exception exception) {
/* 57 */     if (this.level >= 2) Gdx.app.log(this.tag, message, exception); 
/*    */   }
/*    */   
/*    */   public void error(String message) {
/* 61 */     if (this.level >= 1) Gdx.app.error(this.tag, message); 
/*    */   }
/*    */   
/*    */   public void error(String message, Throwable exception) {
/* 65 */     if (this.level >= 1) Gdx.app.error(this.tag, message, exception);
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLevel(int level) {
/* 72 */     this.level = level;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 76 */     return this.level;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */