/*    */ package com.badlogic.gdx.controllers.desktop.ois;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ public class Ois
/*    */ {
/*    */   private final long inputManagerPtr;
/* 26 */   private final ArrayList<OisJoystick> joysticks = new ArrayList<OisJoystick>();
/*    */   
/*    */   public Ois(long hwnd) {
/* 29 */     this.inputManagerPtr = createInputManager(hwnd);
/*    */     
/* 31 */     String[] names = getJoystickNames(this.inputManagerPtr);
/* 32 */     for (int i = 0, n = names.length; i < n; i++)
/* 33 */       this.joysticks.add(new OisJoystick(createJoystick(this.inputManagerPtr), names[i])); 
/*    */   }
/*    */   
/*    */   public ArrayList<OisJoystick> getJoysticks() {
/* 37 */     return this.joysticks;
/*    */   }
/*    */   
/*    */   public void update() {
/* 41 */     for (int i = 0, n = this.joysticks.size(); i < n; i++)
/* 42 */       ((OisJoystick)this.joysticks.get(i)).update(); 
/*    */   }
/*    */   
/*    */   public int getVersionNumber() {
/* 46 */     return getVersionNumber(this.inputManagerPtr);
/*    */   }
/*    */   
/*    */   public String getVersionName() {
/* 50 */     return getVersionName(this.inputManagerPtr);
/*    */   }
/*    */   
/*    */   public String getInputSystemName() {
/* 54 */     return getInputSystemName(this.inputManagerPtr);
/*    */   }
/*    */   
/*    */   private native long createInputManager(long paramLong);
/*    */   
/*    */   private native String[] getJoystickNames(long paramLong);
/*    */   
/*    */   private native int getVersionNumber(long paramLong);
/*    */   
/*    */   private native String getVersionName(long paramLong);
/*    */   
/*    */   private native String getInputSystemName(long paramLong);
/*    */   
/*    */   private native long createJoystick(long paramLong);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\desktop\ois\Ois.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */