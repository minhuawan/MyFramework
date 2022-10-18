/*     */ package com.badlogic.gdx.controllers.desktop;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.controllers.ControlType;
/*     */ import com.badlogic.gdx.controllers.Controller;
/*     */ import com.badlogic.gdx.controllers.ControllerListener;
/*     */ import com.badlogic.gdx.controllers.PovDirection;
/*     */ import com.badlogic.gdx.controllers.desktop.ois.Ois;
/*     */ import com.badlogic.gdx.controllers.desktop.ois.OisJoystick;
/*     */ import com.badlogic.gdx.controllers.desktop.ois.OisListener;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.awt.Component;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ public class OisControllers
/*     */ {
/*     */   final DesktopControllerManager manager;
/*  43 */   long hwnd = getWindowHandle();
/*  44 */   Ois ois = new Ois(this.hwnd);
/*     */   
/*     */   OisController[] controllers;
/*  47 */   private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");
/*  48 */   private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
/*     */   private static final long CHECK_FOR_LOST_WINDOW_HANDLE_INTERVAL = 1000000000L;
/*     */   
/*     */   public OisControllers(final DesktopControllerManager manager) {
/*  52 */     this.manager = manager;
/*  53 */     ArrayList<OisJoystick> joysticks = this.ois.getJoysticks();
/*  54 */     this.controllers = new OisController[joysticks.size()];
/*  55 */     for (int i = 0, n = joysticks.size(); i < n; i++) {
/*  56 */       OisJoystick joystick = joysticks.get(i);
/*  57 */       this.controllers[i] = new OisController(joystick);
/*  58 */       manager.controllers.add(this.controllers[i]);
/*     */     } 
/*     */     
/*  61 */     (new Runnable()
/*     */       {
/*     */         private long lastCheckForLostWindowHandleTime;
/*     */ 
/*     */         
/*     */         public void run() {
/*  67 */           long now = System.nanoTime();
/*  68 */           if (now - this.lastCheckForLostWindowHandleTime > 1000000000L) {
/*  69 */             this.lastCheckForLostWindowHandleTime = now;
/*     */             
/*  71 */             long newWindowHandle = OisControllers.getWindowHandle();
/*  72 */             if (OisControllers.this.hwnd != newWindowHandle) {
/*  73 */               OisControllers.this.hwnd = newWindowHandle;
/*  74 */               OisControllers.this.ois = new Ois(newWindowHandle);
/*     */               
/*  76 */               ArrayList<OisJoystick> joysticks = OisControllers.this.ois.getJoysticks();
/*  77 */               OisControllers.this.controllers = new OisControllers.OisController[joysticks.size()];
/*  78 */               manager.controllers.clear();
/*  79 */               for (int i = 0, n = joysticks.size(); i < n; i++) {
/*  80 */                 OisJoystick joystick = joysticks.get(i);
/*  81 */                 OisControllers.this.controllers[i] = new OisControllers.OisController(joystick);
/*  82 */                 manager.controllers.add(OisControllers.this.controllers[i]);
/*     */               } 
/*     */             } 
/*     */           } 
/*  86 */           OisControllers.this.ois.update();
/*  87 */           Gdx.app.postRunnable(this);
/*     */         }
/*  89 */       }).run();
/*     */   }
/*     */   
/*     */   class OisController
/*     */     implements Controller {
/*     */     private final OisJoystick joystick;
/*  95 */     final Array<ControllerListener> listeners = new Array();
/*     */     
/*     */     public OisController(OisJoystick joystick) {
/*  98 */       this.joystick = joystick;
/*  99 */       joystick.setListener(new OisListener() {
/*     */             public void buttonReleased(OisJoystick joystick, int buttonIndex) {
/* 101 */               Array<ControllerListener> allListeners = OisControllers.this.manager.listeners; int ii, nn;
/* 102 */               for (ii = 0, nn = allListeners.size; ii < nn; ii++)
/* 103 */                 ((ControllerListener)allListeners.get(ii)).buttonUp(OisControllers.OisController.this, buttonIndex); 
/* 104 */               for (ii = 0, nn = OisControllers.OisController.this.listeners.size; ii < nn; ii++)
/* 105 */                 ((ControllerListener)OisControllers.OisController.this.listeners.get(ii)).buttonUp(OisControllers.OisController.this, buttonIndex); 
/*     */             }
/*     */             
/*     */             public void buttonPressed(OisJoystick joystick, int buttonIndex) {
/* 109 */               Array<ControllerListener> allListeners = OisControllers.this.manager.listeners; int ii, nn;
/* 110 */               for (ii = 0, nn = allListeners.size; ii < nn; ii++)
/* 111 */                 ((ControllerListener)allListeners.get(ii)).buttonDown(OisControllers.OisController.this, buttonIndex); 
/* 112 */               for (ii = 0, nn = OisControllers.OisController.this.listeners.size; ii < nn; ii++)
/* 113 */                 ((ControllerListener)OisControllers.OisController.this.listeners.get(ii)).buttonDown(OisControllers.OisController.this, buttonIndex); 
/*     */             }
/*     */             
/*     */             public void axisMoved(OisJoystick joystick, int axisIndex, float value) {
/* 117 */               Array<ControllerListener> allListeners = OisControllers.this.manager.listeners; int ii, nn;
/* 118 */               for (ii = 0, nn = allListeners.size; ii < nn; ii++)
/* 119 */                 ((ControllerListener)allListeners.get(ii)).axisMoved(OisControllers.OisController.this, axisIndex, value); 
/* 120 */               for (ii = 0, nn = OisControllers.OisController.this.listeners.size; ii < nn; ii++)
/* 121 */                 ((ControllerListener)OisControllers.OisController.this.listeners.get(ii)).axisMoved(OisControllers.OisController.this, axisIndex, value); 
/*     */             }
/*     */             
/*     */             public void povMoved(OisJoystick joystick, int povIndex, OisJoystick.OisPov ignored) {
/* 125 */               PovDirection value = OisControllers.OisController.this.getPov(povIndex);
/* 126 */               Array<ControllerListener> allListeners = OisControllers.this.manager.listeners; int ii, nn;
/* 127 */               for (ii = 0, nn = allListeners.size; ii < nn; ii++)
/* 128 */                 ((ControllerListener)allListeners.get(ii)).povMoved(OisControllers.OisController.this, povIndex, value); 
/* 129 */               for (ii = 0, nn = OisControllers.OisController.this.listeners.size; ii < nn; ii++)
/* 130 */                 ((ControllerListener)OisControllers.OisController.this.listeners.get(ii)).povMoved(OisControllers.OisController.this, povIndex, value); 
/*     */             }
/*     */             
/*     */             public void xSliderMoved(OisJoystick joystick, int sliderIndex, boolean value) {
/* 134 */               Array<ControllerListener> allListeners = OisControllers.this.manager.listeners; int ii, nn;
/* 135 */               for (ii = 0, nn = allListeners.size; ii < nn; ii++)
/* 136 */                 ((ControllerListener)allListeners.get(ii)).xSliderMoved(OisControllers.OisController.this, sliderIndex, value); 
/* 137 */               for (ii = 0, nn = OisControllers.OisController.this.listeners.size; ii < nn; ii++)
/* 138 */                 ((ControllerListener)OisControllers.OisController.this.listeners.get(ii)).xSliderMoved(OisControllers.OisController.this, sliderIndex, value); 
/*     */             }
/*     */             
/*     */             public void ySliderMoved(OisJoystick joystick, int sliderIndex, boolean value) {
/* 142 */               Array<ControllerListener> allListeners = OisControllers.this.manager.listeners; int ii, nn;
/* 143 */               for (ii = 0, nn = allListeners.size; ii < nn; ii++)
/* 144 */                 ((ControllerListener)allListeners.get(ii)).ySliderMoved(OisControllers.OisController.this, sliderIndex, value); 
/* 145 */               for (ii = 0, nn = OisControllers.OisController.this.listeners.size; ii < nn; ii++)
/* 146 */                 ((ControllerListener)OisControllers.OisController.this.listeners.get(ii)).ySliderMoved(OisControllers.OisController.this, sliderIndex, value); 
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public boolean getButton(int buttonIndex) {
/* 152 */       return this.joystick.isButtonPressed(buttonIndex);
/*     */     }
/*     */     
/*     */     public float getAxis(int axisIndex) {
/* 156 */       return this.joystick.getAxis(axisIndex);
/*     */     }
/*     */     
/*     */     public PovDirection getPov(int povIndex) {
/* 160 */       OisJoystick.OisPov pov = this.joystick.getPov(povIndex);
/* 161 */       switch (pov) {
/*     */         case button:
/* 163 */           return PovDirection.center;
/*     */         case axis:
/* 165 */           return PovDirection.east;
/*     */         case slider:
/* 167 */           return PovDirection.north;
/*     */         case pov:
/* 169 */           return PovDirection.northEast;
/*     */         case null:
/* 171 */           return PovDirection.northWest;
/*     */         case null:
/* 173 */           return PovDirection.south;
/*     */         case null:
/* 175 */           return PovDirection.southEast;
/*     */         case null:
/* 177 */           return PovDirection.southWest;
/*     */         case null:
/* 179 */           return PovDirection.west;
/*     */       } 
/* 181 */       return null;
/*     */     }
/*     */     
/*     */     public boolean getSliderX(int sliderIndex) {
/* 185 */       return this.joystick.getSliderX(sliderIndex);
/*     */     }
/*     */     
/*     */     public boolean getSliderY(int sliderIndex) {
/* 189 */       return this.joystick.getSliderY(sliderIndex);
/*     */     }
/*     */     
/*     */     public Vector3 getAccelerometer(int accelerometerIndex) {
/* 193 */       throw new GdxRuntimeException("Invalid accelerometer index: " + accelerometerIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAccelerometerSensitivity(float sensitivity) {}
/*     */     
/*     */     public int getControlCount(ControlType type) {
/* 200 */       switch (type) {
/*     */         case button:
/* 202 */           return this.joystick.getButtonCount();
/*     */         case axis:
/* 204 */           return this.joystick.getAxisCount();
/*     */         case slider:
/* 206 */           return this.joystick.getSliderCount();
/*     */         case pov:
/* 208 */           return this.joystick.getPovCount();
/*     */       } 
/* 210 */       return 0;
/*     */     }
/*     */     
/*     */     public void addListener(ControllerListener listener) {
/* 214 */       this.listeners.add(listener);
/*     */     }
/*     */     
/*     */     public void removeListener(ControllerListener listener) {
/* 218 */       this.listeners.removeValue(listener, true);
/*     */     }
/*     */     
/*     */     public String getName() {
/* 222 */       return this.joystick.getName();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 226 */       return this.joystick.getName();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getWindowHandle() {
/* 233 */     if (IS_MAC) {
/* 234 */       return 0L;
/*     */     }
/*     */     
/*     */     try {
/* 238 */       if (Gdx.graphics.getType() == Graphics.GraphicsType.JGLFW) {
/* 239 */         return ((Long)Gdx.graphics.getClass().getDeclaredMethod("getWindow", new Class[0]).invoke(null, new Object[0])).longValue();
/*     */       }
/* 241 */       if (Gdx.graphics.getType() == Graphics.GraphicsType.LWJGL) {
/* 242 */         if (Gdx.app.getClass().getName().equals("com.badlogic.gdx.backends.lwjgl.LwjglCanvas")) {
/* 243 */           Class<?> canvasClass = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglCanvas");
/* 244 */           Object canvas = canvasClass.getDeclaredMethod("getCanvas", new Class[0]).invoke(Gdx.app, new Object[0]);
/* 245 */           return ((Long)invokeMethod(invokeMethod(SwingUtilities.windowForComponent((Component)canvas), "getPeer"), "getHWnd")).longValue();
/*     */         } 
/*     */         
/* 248 */         Class<?> displayClass = Class.forName("org.lwjgl.opengl.Display");
/* 249 */         Method getImplementation = displayClass.getDeclaredMethod("getImplementation", new Class[0]);
/* 250 */         getImplementation.setAccessible(true);
/* 251 */         Object display = getImplementation.invoke(null, (Object[])null);
/* 252 */         Field field = display.getClass().getDeclaredField(IS_WINDOWS ? "hwnd" : "parent_window");
/* 253 */         field.setAccessible(true);
/* 254 */         return ((Long)field.get(display)).longValue();
/*     */       } 
/* 256 */     } catch (Exception ex) {
/* 257 */       throw new RuntimeException("Unable to get window handle.", ex);
/*     */     } 
/*     */     
/* 260 */     return 0L;
/*     */   }
/*     */   
/*     */   private static Object invokeMethod(Object object, String methodName) throws Exception {
/* 264 */     for (Method m : object.getClass().getMethods()) {
/* 265 */       if (m.getName().equals(methodName)) return m.invoke(object, new Object[0]); 
/* 266 */     }  throw new RuntimeException("Could not find method '" + methodName + "' on class: " + object.getClass());
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\desktop\OisControllers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */