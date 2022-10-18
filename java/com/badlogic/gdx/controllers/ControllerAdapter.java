/*    */ package com.badlogic.gdx.controllers;
/*    */ 
/*    */ import com.badlogic.gdx.math.Vector3;
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
/*    */ public class ControllerAdapter
/*    */   implements ControllerListener
/*    */ {
/*    */   public boolean buttonDown(Controller controller, int buttonIndex) {
/* 26 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean buttonUp(Controller controller, int buttonIndex) {
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean axisMoved(Controller controller, int axisIndex, float value) {
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean xSliderMoved(Controller controller, int sliderIndex, boolean value) {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean ySliderMoved(Controller controller, int sliderIndex, boolean value) {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accelerometerMoved(Controller controller, int accelerometerIndex, Vector3 value) {
/* 56 */     return false;
/*    */   }
/*    */   
/*    */   public void connected(Controller controller) {}
/*    */   
/*    */   public void disconnected(Controller controller) {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\ControllerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */