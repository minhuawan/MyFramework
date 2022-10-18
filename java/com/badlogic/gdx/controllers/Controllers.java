/*     */ package com.badlogic.gdx.controllers;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.LifecycleListener;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Controllers
/*     */ {
/*     */   private static final String TAG = "Controllers";
/*  40 */   static final ObjectMap<Application, ControllerManager> managers = new ObjectMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Array<Controller> getControllers() {
/*  46 */     initialize();
/*  47 */     return getManager().getControllers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addListener(ControllerListener listener) {
/*  54 */     initialize();
/*  55 */     getManager().addListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeListener(ControllerListener listener) {
/*  61 */     initialize();
/*  62 */     getManager().removeListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clearListeners() {
/*  67 */     initialize();
/*  68 */     getManager().clearListeners();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Array<ControllerListener> getListeners() {
/*  73 */     initialize();
/*  74 */     return getManager().getListeners();
/*     */   }
/*     */   
/*     */   private static ControllerManager getManager() {
/*  78 */     return (ControllerManager)managers.get(Gdx.app);
/*     */   }
/*     */   
/*     */   private static void initialize() {
/*  82 */     if (managers.containsKey(Gdx.app))
/*     */       return; 
/*  84 */     String className = null;
/*  85 */     Application.ApplicationType type = Gdx.app.getType();
/*  86 */     ControllerManager manager = null;
/*     */     
/*  88 */     if (type == Application.ApplicationType.Android) {
/*  89 */       if (Gdx.app.getVersion() >= 12) {
/*  90 */         className = "com.badlogic.gdx.controllers.android.AndroidControllers";
/*     */       } else {
/*  92 */         Gdx.app.log("Controllers", "No controller manager is available for Android versions < API level 12");
/*  93 */         manager = new ControllerManagerStub();
/*     */       } 
/*  95 */     } else if (type == Application.ApplicationType.Desktop) {
/*  96 */       if (Gdx.graphics.getType() == Graphics.GraphicsType.LWJGL3) {
/*  97 */         className = "com.badlogic.gdx.controllers.lwjgl3.Lwjgl3ControllerManager";
/*     */       } else {
/*  99 */         className = "com.badlogic.gdx.controllers.desktop.DesktopControllerManager";
/*     */       } 
/* 101 */     } else if (type == Application.ApplicationType.WebGL) {
/* 102 */       className = "com.badlogic.gdx.controllers.gwt.GwtControllers";
/*     */     } else {
/* 104 */       Gdx.app.log("Controllers", "No controller manager is available for: " + Gdx.app.getType());
/* 105 */       manager = new ControllerManagerStub();
/*     */     } 
/*     */     
/* 108 */     if (manager == null) {
/*     */       try {
/* 110 */         Class controllerManagerClass = ClassReflection.forName(className);
/* 111 */         manager = (ControllerManager)ClassReflection.newInstance(controllerManagerClass);
/* 112 */       } catch (Throwable ex) {
/* 113 */         throw new GdxRuntimeException("Error creating controller manager: " + className, ex);
/*     */       } 
/*     */     }
/*     */     
/* 117 */     managers.put(Gdx.app, manager);
/* 118 */     final Application app = Gdx.app;
/* 119 */     Gdx.app.addLifecycleListener(new LifecycleListener()
/*     */         {
/*     */           public void resume() {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void pause() {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void dispose() {
/* 130 */             Controllers.managers.remove(app);
/* 131 */             Gdx.app.log("Controllers", "removed manager for application, " + Controllers.managers.size + " managers active");
/*     */           }
/*     */         });
/*     */     
/* 135 */     Gdx.app.log("Controllers", "added manager for application, " + managers.size + " managers active");
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\Controllers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */