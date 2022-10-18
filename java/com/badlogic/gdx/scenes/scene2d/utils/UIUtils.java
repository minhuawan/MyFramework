/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UIUtils
/*    */ {
/*  9 */   public static boolean isMac = System.getProperty("os.name").contains("OS X");
/* 10 */   public static boolean isWindows = System.getProperty("os.name").contains("Windows");
/* 11 */   public static boolean isLinux = System.getProperty("os.name").contains("Linux");
/*    */   
/*    */   public static boolean left() {
/* 14 */     return Gdx.input.isButtonPressed(0);
/*    */   }
/*    */   
/*    */   public static boolean left(int button) {
/* 18 */     return (button == 0);
/*    */   }
/*    */   
/*    */   public static boolean right() {
/* 22 */     return Gdx.input.isButtonPressed(1);
/*    */   }
/*    */   
/*    */   public static boolean right(int button) {
/* 26 */     return (button == 1);
/*    */   }
/*    */   
/*    */   public static boolean middle() {
/* 30 */     return Gdx.input.isButtonPressed(2);
/*    */   }
/*    */   
/*    */   public static boolean middle(int button) {
/* 34 */     return (button == 2);
/*    */   }
/*    */   
/*    */   public static boolean shift() {
/* 38 */     return (Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60));
/*    */   }
/*    */   
/*    */   public static boolean shift(int keycode) {
/* 42 */     return (keycode == 59 || keycode == 60);
/*    */   }
/*    */   
/*    */   public static boolean ctrl() {
/* 46 */     if (isMac) {
/* 47 */       return Gdx.input.isKeyPressed(63);
/*    */     }
/* 49 */     return (Gdx.input.isKeyPressed(129) || Gdx.input.isKeyPressed(130));
/*    */   }
/*    */   
/*    */   public static boolean ctrl(int keycode) {
/* 53 */     if (isMac) {
/* 54 */       return (keycode == 63);
/*    */     }
/* 56 */     return (keycode == 129 || keycode == 130);
/*    */   }
/*    */   
/*    */   public static boolean alt() {
/* 60 */     return (Gdx.input.isKeyPressed(57) || Gdx.input.isKeyPressed(58));
/*    */   }
/*    */   
/*    */   public static boolean alt(int keycode) {
/* 64 */     return (keycode == 57 || keycode == 58);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\UIUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */