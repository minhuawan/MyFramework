/*    */ package com.badlogic.gdx.controllers;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ public class ControllerManagerStub
/*    */   implements ControllerManager
/*    */ {
/* 24 */   Array<Controller> controllers = new Array();
/*    */ 
/*    */   
/*    */   public Array<Controller> getControllers() {
/* 28 */     return this.controllers;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addListener(ControllerListener listener) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeListener(ControllerListener listener) {}
/*    */ 
/*    */   
/*    */   public void clearListeners() {}
/*    */ 
/*    */   
/*    */   public Array<ControllerListener> getListeners() {
/* 44 */     return new Array();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\ControllerManagerStub.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */