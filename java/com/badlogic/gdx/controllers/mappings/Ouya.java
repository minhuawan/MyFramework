/*    */ package com.badlogic.gdx.controllers.mappings;
/*    */ 
/*    */ import java.lang.reflect.Field;
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
/*    */ 
/*    */ 
/*    */ public class Ouya
/*    */ {
/*    */   public static final String ID = "OUYA Game Controller";
/*    */   public static final int BUTTON_O = 96;
/*    */   public static final int BUTTON_U = 99;
/*    */   public static final int BUTTON_Y = 100;
/*    */   public static final int BUTTON_A = 97;
/*    */   public static final int BUTTON_MENU = 82;
/*    */   public static final int BUTTON_DPAD_UP = 19;
/*    */   public static final int BUTTON_DPAD_DOWN = 20;
/*    */   public static final int BUTTON_DPAD_RIGHT = 22;
/*    */   public static final int BUTTON_DPAD_LEFT = 21;
/*    */   public static final int BUTTON_L1 = 104;
/*    */   public static final int BUTTON_L2 = 102;
/*    */   public static final int BUTTON_L3 = 106;
/*    */   public static final int BUTTON_R1 = 105;
/*    */   public static final int BUTTON_R2 = 103;
/*    */   public static final int BUTTON_R3 = 107;
/*    */   public static final int AXIS_LEFT_X = 0;
/*    */   public static final int AXIS_LEFT_Y = 1;
/*    */   public static final int AXIS_LEFT_TRIGGER = 2;
/*    */   public static final int AXIS_RIGHT_X = 3;
/*    */   public static final int AXIS_RIGHT_Y = 4;
/*    */   public static final int AXIS_RIGHT_TRIGGER = 5;
/*    */   public static final float STICK_DEADZONE = 0.25F;
/*    */   public static final boolean runningOnOuya;
/*    */   public static boolean simulateRunningOnOuya = false;
/*    */   
/*    */   static {
/* 57 */     boolean isOuya = false;
/*    */     try {
/* 59 */       Class<?> buildClass = Class.forName("android.os.Build");
/* 60 */       Field deviceField = buildClass.getDeclaredField("DEVICE");
/* 61 */       Object device = deviceField.get(null);
/* 62 */       isOuya = ("ouya_1_1".equals(device) || "cardhu".equals(device));
/* 63 */     } catch (Exception exception) {}
/*    */     
/* 65 */     runningOnOuya = isOuya;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isRunningOnOuya() {
/* 70 */     return (runningOnOuya || simulateRunningOnOuya);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\mappings\Ouya.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */