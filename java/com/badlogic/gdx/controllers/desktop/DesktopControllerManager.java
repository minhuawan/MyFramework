/*    */ package com.badlogic.gdx.controllers.desktop;
/*    */ 
/*    */ import com.badlogic.gdx.controllers.Controller;
/*    */ import com.badlogic.gdx.controllers.ControllerListener;
/*    */ import com.badlogic.gdx.controllers.ControllerManager;
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ public class DesktopControllerManager
/*    */   implements ControllerManager
/*    */ {
/* 27 */   final Array<Controller> controllers = new Array();
/* 28 */   final Array<ControllerListener> listeners = new Array();
/*    */   
/*    */   public DesktopControllerManager() {
/* 31 */     (new SharedLibraryLoader()).load("gdx-controllers-desktop");
/* 32 */     new OisControllers(this);
/*    */   }
/*    */   
/*    */   public Array<Controller> getControllers() {
/* 36 */     return this.controllers;
/*    */   }
/*    */   
/*    */   public void addListener(ControllerListener listener) {
/* 40 */     this.listeners.add(listener);
/*    */   }
/*    */   
/*    */   public void removeListener(ControllerListener listener) {
/* 44 */     this.listeners.removeValue(listener, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearListeners() {
/* 49 */     this.listeners.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<ControllerListener> getListeners() {
/* 54 */     return this.listeners;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\desktop\DesktopControllerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */