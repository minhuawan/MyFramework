/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.ApplicationLogger;
/*     */ import com.badlogic.gdx.Audio;
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.Input;
/*     */ import com.badlogic.gdx.LifecycleListener;
/*     */ import com.badlogic.gdx.Net;
/*     */ import com.badlogic.gdx.Preferences;
/*     */ import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Clipboard;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.PaintEvent;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.opengl.AWTGLCanvas;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.Drawable;
/*     */ import org.lwjgl.opengl.PixelFormat;
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
/*     */ public class LwjglAWTCanvas
/*     */   implements Application
/*     */ {
/*     */   static int instanceCount;
/*     */   LwjglGraphics graphics;
/*     */   OpenALAudio audio;
/*     */   LwjglFiles files;
/*     */   LwjglAWTInput input;
/*     */   LwjglNet net;
/*     */   final ApplicationListener listener;
/*     */   AWTGLCanvas canvas;
/*  70 */   final Array<Runnable> runnables = new Array();
/*  71 */   final Array<Runnable> executedRunnables = new Array();
/*  72 */   final Array<LifecycleListener> lifecycleListeners = new Array();
/*     */   boolean running = true;
/*     */   int lastWidth;
/*     */   int lastHeight;
/*  76 */   int logLevel = 2;
/*     */   ApplicationLogger applicationLogger;
/*  78 */   final String logTag = "LwjglAWTCanvas"; Cursor cursor;
/*     */   Map<String, Preferences> preferences;
/*     */   
/*     */   public LwjglAWTCanvas(ApplicationListener listener) {
/*  82 */     this(listener, null, null);
/*     */   }
/*     */   
/*     */   public LwjglAWTCanvas(ApplicationListener listener, LwjglAWTCanvas sharedContextCanvas) {
/*  86 */     this(listener, null, sharedContextCanvas);
/*     */   }
/*     */   
/*     */   public LwjglAWTCanvas(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  90 */     this(listener, config, null);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setDisplayMode(int width, int height) {}
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setTitle(String title) {}
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
/*     */ 
/*     */ 
/*     */   
/*     */   public ApplicationListener getApplicationListener() {
/* 183 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Canvas getCanvas() {
/* 187 */     return (Canvas)this.canvas;
/*     */   }
/*     */ 
/*     */   
/*     */   public Audio getAudio() {
/* 192 */     return Gdx.audio;
/*     */   }
/*     */ 
/*     */   
/*     */   public Files getFiles() {
/* 197 */     return this.files;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics getGraphics() {
/* 202 */     return this.graphics;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input getInput() {
/* 207 */     return this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Net getNet() {
/* 212 */     return this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   public Application.ApplicationType getType() {
/* 217 */     return Application.ApplicationType.Desktop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 222 */     return 0;
/*     */   }
/*     */   
/*     */   void setGlobals() {
/* 226 */     Gdx.app = this;
/* 227 */     if (this.audio != null) Gdx.audio = (Audio)this.audio; 
/* 228 */     if (this.files != null) Gdx.files = this.files; 
/* 229 */     if (this.net != null) Gdx.net = this.net; 
/* 230 */     Gdx.graphics = this.graphics;
/* 231 */     Gdx.input = this.input;
/*     */   }
/*     */   
/*     */   void create() {
/*     */     try {
/* 236 */       setGlobals();
/* 237 */       this.graphics.initiateGL();
/* 238 */       this.canvas.setVSyncEnabled(this.graphics.config.vSyncEnabled);
/* 239 */       this.listener.create();
/* 240 */       this.lastWidth = Math.max(1, this.graphics.getWidth());
/* 241 */       this.lastHeight = Math.max(1, this.graphics.getHeight());
/* 242 */       this.listener.resize(this.lastWidth, this.lastHeight);
/* 243 */       start();
/* 244 */     } catch (Throwable ex) {
/* 245 */       stopped();
/* 246 */       exception(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   void render(boolean shouldRender) throws LWJGLException {
/* 251 */     if (!this.running)
/*     */       return; 
/* 253 */     setGlobals();
/* 254 */     this.canvas.setCursor(this.cursor);
/*     */     
/* 256 */     int width = Math.max(1, this.graphics.getWidth());
/* 257 */     int height = Math.max(1, this.graphics.getHeight());
/* 258 */     if (this.lastWidth != width || this.lastHeight != height) {
/* 259 */       this.lastWidth = width;
/* 260 */       this.lastHeight = height;
/* 261 */       Gdx.gl.glViewport(0, 0, this.lastWidth, this.lastHeight);
/* 262 */       resize(width, height);
/* 263 */       this.listener.resize(width, height);
/* 264 */       shouldRender = true;
/*     */     } 
/*     */     
/* 267 */     if (executeRunnables()) shouldRender = true;
/*     */ 
/*     */     
/* 270 */     if (!this.running)
/*     */       return; 
/* 272 */     shouldRender |= this.graphics.shouldRender();
/* 273 */     this.input.processEvents();
/* 274 */     if (this.audio != null) this.audio.update();
/*     */     
/* 276 */     if (shouldRender) {
/* 277 */       this.graphics.updateTime();
/* 278 */       this.graphics.frameId++;
/* 279 */       this.listener.render();
/* 280 */       this.canvas.swapBuffers();
/*     */     } 
/*     */     
/* 283 */     Display.sync(getFrameRate() * instanceCount);
/*     */   }
/*     */   
/*     */   public boolean executeRunnables() {
/* 287 */     synchronized (this.runnables) {
/* 288 */       for (int i = this.runnables.size - 1; i >= 0; i--) {
/* 289 */         this.executedRunnables.addAll((Object[])new Runnable[] { (Runnable)this.runnables.get(i) });
/* 290 */       }  this.runnables.clear();
/*     */     } 
/* 292 */     if (this.executedRunnables.size == 0) return false; 
/*     */     while (true) {
/* 294 */       ((Runnable)this.executedRunnables.pop()).run();
/* 295 */       if (this.executedRunnables.size <= 0)
/* 296 */         return true; 
/*     */     } 
/*     */   }
/*     */   protected int getFrameRate() {
/* 300 */     int frameRate = isActive() ? this.graphics.config.foregroundFPS : this.graphics.config.backgroundFPS;
/* 301 */     if (frameRate == -1) frameRate = 10; 
/* 302 */     if (frameRate == 0) frameRate = this.graphics.config.backgroundFPS; 
/* 303 */     if (frameRate == 0) frameRate = 30; 
/* 304 */     return frameRate;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 309 */     Component root = SwingUtilities.getRoot((Component)this.canvas);
/* 310 */     return (root instanceof Frame) ? ((Frame)root).isActive() : true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resize(int width, int height) {}
/*     */ 
/*     */   
/*     */   protected void stopped() {}
/*     */ 
/*     */   
/*     */   public void stop() {
/* 326 */     if (!this.running)
/* 327 */       return;  this.running = false;
/* 328 */     setGlobals();
/* 329 */     Array<LifecycleListener> listeners = this.lifecycleListeners;
/*     */ 
/*     */     
/* 332 */     if (this.canvas.isDisplayable()) {
/* 333 */       makeCurrent();
/*     */     } else {
/* 335 */       error("LwjglAWTCanvas", "OpenGL context destroyed before application listener has had a chance to dispose of textures.");
/*     */     } 
/*     */     
/* 338 */     synchronized (listeners) {
/* 339 */       for (LifecycleListener listener : listeners) {
/* 340 */         listener.pause();
/* 341 */         listener.dispose();
/*     */       } 
/*     */     } 
/* 344 */     this.listener.pause();
/* 345 */     this.listener.dispose();
/*     */     
/* 347 */     Gdx.app = null;
/*     */     
/* 349 */     Gdx.graphics = null;
/*     */     
/* 351 */     if (this.audio != null) {
/* 352 */       this.audio.dispose();
/* 353 */       Gdx.audio = null;
/*     */     } 
/*     */     
/* 356 */     if (this.files != null) Gdx.files = null;
/*     */     
/* 358 */     if (this.net != null) Gdx.net = null;
/*     */     
/* 360 */     instanceCount--;
/*     */     
/* 362 */     stopped();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getJavaHeap() {
/* 367 */     return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNativeHeap() {
/* 372 */     return getJavaHeap();
/*     */   }
/*     */   
/* 375 */   public LwjglAWTCanvas(ApplicationListener listener, LwjglApplicationConfiguration config, LwjglAWTCanvas sharedContextCanvas) { this.preferences = new HashMap<String, Preferences>(); this.listener = listener; if (config == null) config = new LwjglApplicationConfiguration();  LwjglNativesLoader.load(); setApplicationLogger(new LwjglApplicationLogger()); instanceCount++; AWTGLCanvas sharedDrawable = (sharedContextCanvas != null) ? sharedContextCanvas.canvas : null; try { this.canvas = new AWTGLCanvas(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), new PixelFormat(), (Drawable)sharedDrawable) {
/*     */           private final Dimension minSize = new Dimension(0, 0); private final LwjglAWTCanvas.NonSystemPaint nonSystemPaint = new LwjglAWTCanvas.NonSystemPaint(this); public Dimension getMinimumSize() { return this.minSize; } public void initGL() { LwjglAWTCanvas.this.create(); } public void paintGL() { try { boolean systemPaint = !(EventQueue.getCurrentEvent() instanceof LwjglAWTCanvas.NonSystemPaint); LwjglAWTCanvas.this.render(systemPaint); Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(this.nonSystemPaint); } catch (Throwable ex) { LwjglAWTCanvas.this.exception(ex); }  }
/*     */         }; } catch (Throwable ex) { exception(ex); return; }  this.canvas.setBackground(new Color(config.initialBackgroundColor.r, config.initialBackgroundColor.g, config.initialBackgroundColor.b, config.initialBackgroundColor.a)); this.graphics = new LwjglGraphics((Canvas)this.canvas, config) {
/*     */         public void setTitle(String title) { super.setTitle(title); LwjglAWTCanvas.this.setTitle(title); } public boolean setWindowedMode(int width, int height) { if (!super.setWindowedMode(width, height)) return false;  LwjglAWTCanvas.this.setDisplayMode(width, height); return true; } public boolean setFullscreenMode(Graphics.DisplayMode displayMode) { if (!super.setFullscreenMode(displayMode)) return false;  LwjglAWTCanvas.this.setDisplayMode(displayMode.width, displayMode.height); return true; } public boolean shouldRender() { synchronized (this) { boolean rq = this.requestRendering; this.requestRendering = false; return (rq || this.isContinuous); }  }
/* 379 */       }; if (!LwjglApplicationConfiguration.disableAudio && Gdx.audio == null) this.audio = new OpenALAudio();  if (Gdx.files == null) this.files = new LwjglFiles();  if (Gdx.net == null) this.net = new LwjglNet();  this.input = new LwjglAWTInput(this); setGlobals(); } public Preferences getPreferences(String name) { if (this.preferences.containsKey(name)) {
/* 380 */       return this.preferences.get(name);
/*     */     }
/* 382 */     Preferences prefs = new LwjglPreferences(name, ".prefs/");
/* 383 */     this.preferences.put(name, prefs);
/* 384 */     return prefs; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Clipboard getClipboard() {
/* 390 */     return new LwjglClipboard();
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRunnable(Runnable runnable) {
/* 395 */     synchronized (this.runnables) {
/* 396 */       this.runnables.add(runnable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message) {
/* 402 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void debug(String tag, String message, Throwable exception) {
/* 407 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message) {
/* 412 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message, Throwable exception) {
/* 417 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message) {
/* 422 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message, Throwable exception) {
/* 427 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 432 */     this.logLevel = logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLogLevel() {
/* 437 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationLogger(ApplicationLogger applicationLogger) {
/* 442 */     this.applicationLogger = applicationLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public ApplicationLogger getApplicationLogger() {
/* 447 */     return this.applicationLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public void exit() {
/* 452 */     postRunnable(new Runnable()
/*     */         {
/*     */           public void run() {
/* 455 */             LwjglAWTCanvas.this.stop();
/* 456 */             System.exit(-1);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeCurrent() {
/*     */     try {
/* 465 */       this.canvas.makeCurrent();
/* 466 */       setGlobals();
/* 467 */     } catch (Throwable ex) {
/* 468 */       exception(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCurrent() {
/*     */     try {
/* 475 */       return this.canvas.isCurrent();
/* 476 */     } catch (Throwable ex) {
/* 477 */       exception(ex);
/* 478 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursor(Cursor cursor) {
/* 484 */     this.cursor = cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLifecycleListener(LifecycleListener listener) {
/* 489 */     synchronized (this.lifecycleListeners) {
/* 490 */       this.lifecycleListeners.add(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeLifecycleListener(LifecycleListener listener) {
/* 496 */     synchronized (this.lifecycleListeners) {
/* 497 */       this.lifecycleListeners.removeValue(listener, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void exception(Throwable ex) {
/* 502 */     ex.printStackTrace();
/* 503 */     stop();
/*     */   }
/*     */   
/*     */   public static class NonSystemPaint extends PaintEvent {
/*     */     public NonSystemPaint(AWTGLCanvas canvas) {
/* 508 */       super((Component)canvas, 801, new Rectangle(0, 0, 99999, 99999));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglAWTCanvas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */