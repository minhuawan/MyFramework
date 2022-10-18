/*    */ package com.badlogic.gdx.controllers.desktop.ois;
/*    */ 
/*    */ import com.badlogic.gdx.ApplicationAdapter;
/*    */ import com.badlogic.gdx.controllers.desktop.DesktopControllersBuild;
/*    */ import com.badlogic.gdx.controllers.desktop.OisControllers;
/*    */ import com.badlogic.gdx.utils.SharedLibraryLoader;
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
/*    */ public class OisTest
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 27 */     DesktopControllersBuild.main(null);
/* 28 */     (new SharedLibraryLoader("libs/gdx-controllers-desktop-natives.jar")).load("gdx-controllers-desktop");
/*    */     
/* 30 */     ApplicationAdapter app = new ApplicationAdapter() {
/*    */         Ois ois;
/*    */         
/*    */         public void create() {
/* 34 */           this.ois = new Ois(OisControllers.getWindowHandle());
/* 35 */           if (this.ois.getJoysticks().size() > 0) {
/* 36 */             ((OisJoystick)this.ois.getJoysticks().get(0)).setListener(new OisListener()
/*    */                 {
/*    */                   public void xSliderMoved(OisJoystick joystick, int slider, boolean value) {
/* 39 */                     System.out.println("xSliderMoved: " + slider + ", " + value);
/*    */                   }
/*    */ 
/*    */                   
/*    */                   public void ySliderMoved(OisJoystick joystick, int slider, boolean value) {
/* 44 */                     System.out.println("ySliderMoved: " + slider + ", " + value);
/*    */                   }
/*    */ 
/*    */                   
/*    */                   public void povMoved(OisJoystick joystick, int pov, OisJoystick.OisPov value) {
/* 49 */                     System.out.println("povMoved: " + pov + ", " + value);
/*    */                   }
/*    */ 
/*    */ 
/*    */                   
/*    */                   public void buttonReleased(OisJoystick joystick, int button) {
/* 55 */                     System.out.println("buttonReleased: " + button);
/*    */                   }
/*    */ 
/*    */                   
/*    */                   public void buttonPressed(OisJoystick joystick, int button) {
/* 60 */                     System.out.println("buttonPressed: " + button);
/*    */                   }
/*    */ 
/*    */                   
/*    */                   public void axisMoved(OisJoystick joystick, int axis, float value) {
/* 65 */                     System.out.println("axisMoved: " + axis + ", " + value);
/*    */                   }
/*    */                 });
/*    */           }
/*    */         }
/*    */         
/*    */         public void render() {
/* 72 */           this.ois.update();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\desktop\ois\OisTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */