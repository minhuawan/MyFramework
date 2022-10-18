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
/*     */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.lwjgl.opengl.Display;
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
/*     */ public class LwjglCanvas
/*     */   implements Application
/*     */ {
/*  51 */   static boolean isWindows = System.getProperty("os.name").contains("Windows");
/*     */   
/*     */   LwjglGraphics graphics;
/*     */   OpenALAudio audio;
/*     */   LwjglFiles files;
/*     */   LwjglInput input;
/*     */   LwjglNet net;
/*     */   ApplicationListener listener;
/*     */   Canvas canvas;
/*  60 */   final Array<Runnable> runnables = new Array();
/*  61 */   final Array<Runnable> executedRunnables = new Array();
/*  62 */   final Array<LifecycleListener> lifecycleListeners = new Array();
/*     */   boolean running = true;
/*  64 */   int logLevel = 2;
/*     */ 
/*     */   
/*     */   ApplicationLogger applicationLogger;
/*     */ 
/*     */   
/*     */   Cursor cursor;
/*     */ 
/*     */   
/*     */   Map<String, Preferences> preferences;
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  78 */     LwjglNativesLoader.load();
/*  79 */     setApplicationLogger(new LwjglApplicationLogger());
/*  80 */     this.canvas = new Canvas() {
/*  81 */         private final Dimension minSize = new Dimension(1, 1);
/*     */         
/*     */         public final void addNotify() {
/*  84 */           super.addNotify();
/*  85 */           if (SharedLibraryLoader.isMac) {
/*  86 */             EventQueue.invokeLater(new Runnable() {
/*     */                   public void run() {
/*  88 */                     LwjglCanvas.this.create();
/*     */                   }
/*     */                 });
/*     */           } else {
/*  92 */             LwjglCanvas.this.create();
/*     */           } 
/*     */         }
/*     */         public final void removeNotify() {
/*  96 */           LwjglCanvas.this.stop();
/*  97 */           super.removeNotify();
/*     */         }
/*     */         
/*     */         public Dimension getMinimumSize() {
/* 101 */           return this.minSize;
/*     */         }
/*     */       };
/* 104 */     this.canvas.setSize(1, 1);
/* 105 */     this.canvas.setIgnoreRepaint(true);
/*     */     
/* 107 */     this.graphics = new LwjglGraphics(this.canvas, config) {
/*     */         public void setTitle(String title) {
/* 109 */           super.setTitle(title);
/* 110 */           LwjglCanvas.this.setTitle(title);
/*     */         }
/*     */         
/*     */         public boolean setWindowedMode(int width, int height, boolean fullscreen) {
/* 114 */           if (!setWindowedMode(width, height)) return false; 
/* 115 */           if (!fullscreen) LwjglCanvas.this.setDisplayMode(width, height); 
/* 116 */           return true;
/*     */         }
/*     */         
/*     */         public boolean setFullscreenMode(Graphics.DisplayMode displayMode) {
/* 120 */           if (!super.setFullscreenMode(displayMode)) return false; 
/* 121 */           LwjglCanvas.this.setDisplayMode(displayMode.width, displayMode.height);
/* 122 */           return true;
/*     */         }
/*     */       };
/* 125 */     this.graphics.setVSync(config.vSyncEnabled);
/* 126 */     if (!LwjglApplicationConfiguration.disableAudio) this.audio = new OpenALAudio(); 
/* 127 */     this.files = new LwjglFiles();
/* 128 */     this.input = new LwjglInput();
/* 129 */     this.net = new LwjglNet();
/* 130 */     this.listener = listener;
/*     */     
/* 132 */     Gdx.app = this;
/* 133 */     Gdx.graphics = this.graphics;
/* 134 */     Gdx.audio = (Audio)this.audio;
/* 135 */     Gdx.files = this.files;
/* 136 */     Gdx.input = this.input;
/* 137 */     Gdx.net = this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDisplayMode(int width, int height) {}
/*     */ 
/*     */   
/*     */   protected void setTitle(String title) {}
/*     */ 
/*     */   
/*     */   public ApplicationListener getApplicationListener() {
/* 148 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Canvas getCanvas() {
/* 152 */     return this.canvas;
/*     */   }
/*     */ 
/*     */   
/*     */   public Audio getAudio() {
/* 157 */     return (Audio)this.audio;
/*     */   }
/*     */ 
/*     */   
/*     */   public Files getFiles() {
/* 162 */     return this.files;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics getGraphics() {
/* 167 */     return this.graphics;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input getInput() {
/* 172 */     return this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Net getNet() {
/* 177 */     return this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   public Application.ApplicationType getType() {
/* 182 */     return Application.ApplicationType.Desktop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 187 */     return 0;
/*     */   }
/*     */   
/*     */   void create() {
/*     */     try {
/* 192 */       this.graphics.setupDisplay();
/*     */       
/* 194 */       this.listener.create();
/* 195 */       this.listener.resize(Math.max(1, this.graphics.getWidth()), Math.max(1, this.graphics.getHeight()));
/*     */       
/* 197 */       start();
/* 198 */     } catch (Exception ex) {
/* 199 */       stopped();
/* 200 */       exception(ex);
/*     */       
/*     */       return;
/*     */     } 
/* 204 */     EventQueue.invokeLater(new Runnable() {
/* 205 */           int lastWidth = Math.max(1, LwjglCanvas.this.graphics.getWidth());
/* 206 */           int lastHeight = Math.max(1, LwjglCanvas.this.graphics.getHeight());
/*     */           
/*     */           public void run() {
/* 209 */             if (!LwjglCanvas.this.running || Display.isCloseRequested()) {
/* 210 */               LwjglCanvas.this.running = false;
/* 211 */               LwjglCanvas.this.stopped();
/*     */               return;
/*     */             } 
/*     */             try {
/* 215 */               Display.processMessages();
/* 216 */               if (LwjglCanvas.this.cursor != null || !LwjglCanvas.isWindows) LwjglCanvas.this.canvas.setCursor(LwjglCanvas.this.cursor);
/*     */               
/* 218 */               boolean shouldRender = false;
/*     */               
/* 220 */               int width = Math.max(1, LwjglCanvas.this.graphics.getWidth());
/* 221 */               int height = Math.max(1, LwjglCanvas.this.graphics.getHeight());
/* 222 */               if (this.lastWidth != width || this.lastHeight != height) {
/* 223 */                 this.lastWidth = width;
/* 224 */                 this.lastHeight = height;
/* 225 */                 Gdx.gl.glViewport(0, 0, this.lastWidth, this.lastHeight);
/* 226 */                 LwjglCanvas.this.resize(width, height);
/* 227 */                 LwjglCanvas.this.listener.resize(width, height);
/* 228 */                 shouldRender = true;
/*     */               } 
/*     */               
/* 231 */               if (LwjglCanvas.this.executeRunnables()) shouldRender = true;
/*     */ 
/*     */               
/* 234 */               if (!LwjglCanvas.this.running)
/*     */                 return; 
/* 236 */               LwjglCanvas.this.input.update();
/* 237 */               shouldRender |= LwjglCanvas.this.graphics.shouldRender();
/* 238 */               LwjglCanvas.this.input.processEvents();
/* 239 */               if (LwjglCanvas.this.audio != null) LwjglCanvas.this.audio.update();
/*     */               
/* 241 */               if (shouldRender) {
/* 242 */                 LwjglCanvas.this.graphics.updateTime();
/* 243 */                 LwjglCanvas.this.graphics.frameId++;
/* 244 */                 LwjglCanvas.this.listener.render();
/* 245 */                 Display.update(false);
/*     */               } 
/*     */               
/* 248 */               Display.sync(LwjglCanvas.this.getFrameRate());
/* 249 */             } catch (Throwable ex) {
/* 250 */               LwjglCanvas.this.exception(ex);
/*     */             } 
/* 252 */             EventQueue.invokeLater(this);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean executeRunnables() {
/* 258 */     synchronized (this.runnables) {
/* 259 */       for (int i = this.runnables.size - 1; i >= 0; i--) {
/* 260 */         this.executedRunnables.addAll((Object[])new Runnable[] { (Runnable)this.runnables.get(i) });
/* 261 */       }  this.runnables.clear();
/*     */     } 
/* 263 */     if (this.executedRunnables.size == 0) return false; 
/*     */     while (true) {
/* 265 */       ((Runnable)this.executedRunnables.pop()).run();
/* 266 */       if (this.executedRunnables.size <= 0)
/* 267 */         return true; 
/*     */     } 
/*     */   }
/*     */   protected int getFrameRate() {
/* 271 */     int frameRate = Display.isActive() ? this.graphics.config.foregroundFPS : this.graphics.config.backgroundFPS;
/* 272 */     if (frameRate == -1) frameRate = 10; 
/* 273 */     if (frameRate == 0) frameRate = this.graphics.config.backgroundFPS; 
/* 274 */     if (frameRate == 0) frameRate = 30; 
/* 275 */     return frameRate;
/*     */   }
/*     */   
/*     */   protected void exception(Throwable ex) {
/* 279 */     ex.printStackTrace();
/* 280 */     stop();
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
/* 296 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/* 298 */             if (!LwjglCanvas.this.running)
/* 299 */               return;  LwjglCanvas.this.running = false;
/* 300 */             Array<LifecycleListener> listeners = LwjglCanvas.this.lifecycleListeners;
/* 301 */             synchronized (listeners) {
/* 302 */               for (LifecycleListener listener : listeners) {
/* 303 */                 listener.pause();
/* 304 */                 listener.dispose();
/*     */               } 
/*     */             } 
/* 307 */             LwjglCanvas.this.listener.pause();
/* 308 */             LwjglCanvas.this.listener.dispose();
/*     */             try {
/* 310 */               Display.destroy();
/* 311 */               if (LwjglCanvas.this.audio != null) LwjglCanvas.this.audio.dispose(); 
/* 312 */             } catch (Throwable throwable) {}
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getJavaHeap() {
/* 320 */     return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNativeHeap() {
/* 325 */     return getJavaHeap();
/*     */   }
/*     */   
/* 328 */   public LwjglCanvas(ApplicationListener listener) { this.preferences = new HashMap<String, Preferences>(); LwjglApplicationConfiguration config = new LwjglApplicationConfiguration(); initialize(listener, config); } public LwjglCanvas(ApplicationListener listener, LwjglApplicationConfiguration config) { this.preferences = new HashMap<String, Preferences>();
/*     */     initialize(listener, config); }
/*     */   
/*     */   public Preferences getPreferences(String name) {
/* 332 */     if (this.preferences.containsKey(name)) {
/* 333 */       return this.preferences.get(name);
/*     */     }
/* 335 */     Preferences prefs = new LwjglPreferences(name, ".prefs/");
/* 336 */     this.preferences.put(name, prefs);
/* 337 */     return prefs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Clipboard getClipboard() {
/* 343 */     return new LwjglClipboard();
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRunnable(Runnable runnable) {
/* 348 */     synchronized (this.runnables) {
/* 349 */       this.runnables.add(runnable);
/* 350 */       Gdx.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message) {
/* 356 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void debug(String tag, String message, Throwable exception) {
/* 361 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message) {
/* 366 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message, Throwable exception) {
/* 371 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message) {
/* 376 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message, Throwable exception) {
/* 381 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 386 */     this.logLevel = logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLogLevel() {
/* 391 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationLogger(ApplicationLogger applicationLogger) {
/* 396 */     this.applicationLogger = applicationLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public ApplicationLogger getApplicationLogger() {
/* 401 */     return this.applicationLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public void exit() {
/* 406 */     postRunnable(new Runnable()
/*     */         {
/*     */           public void run() {
/* 409 */             LwjglCanvas.this.listener.pause();
/* 410 */             LwjglCanvas.this.listener.dispose();
/* 411 */             if (LwjglCanvas.this.audio != null) LwjglCanvas.this.audio.dispose(); 
/* 412 */             System.exit(-1);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursor(Cursor cursor) {
/* 419 */     this.cursor = cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLifecycleListener(LifecycleListener listener) {
/* 424 */     synchronized (this.lifecycleListeners) {
/* 425 */       this.lifecycleListeners.add(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeLifecycleListener(LifecycleListener listener) {
/* 431 */     synchronized (this.lifecycleListeners) {
/* 432 */       this.lifecycleListeners.removeValue(listener, true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglCanvas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */