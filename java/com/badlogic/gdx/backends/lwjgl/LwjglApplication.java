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
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
/*     */ import java.awt.Canvas;
/*     */ import java.io.File;
/*     */ import org.lwjgl.LWJGLException;
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
/*     */ public class LwjglApplication
/*     */   implements Application
/*     */ {
/*     */   protected final LwjglGraphics graphics;
/*     */   protected OpenALAudio audio;
/*     */   protected final LwjglFiles files;
/*     */   protected final LwjglInput input;
/*     */   protected final LwjglNet net;
/*     */   protected final ApplicationListener listener;
/*     */   protected Thread mainLoopThread;
/*     */   protected boolean running = true;
/*  52 */   protected final Array<Runnable> runnables = new Array();
/*  53 */   protected final Array<Runnable> executedRunnables = new Array();
/*  54 */   protected final SnapshotArray<LifecycleListener> lifecycleListeners = new SnapshotArray(LifecycleListener.class);
/*  55 */   protected int logLevel = 2; protected ApplicationLogger applicationLogger;
/*     */   protected String preferencesdir;
/*     */   protected Files.FileType preferencesFileType;
/*     */   ObjectMap<String, Preferences> preferences;
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, String title, int width, int height) {
/*  61 */     this(listener, createConfig(title, width, height));
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener) {
/*  65 */     this(listener, null, 640, 480);
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  69 */     this(listener, config, new LwjglGraphics(config));
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, Canvas canvas) {
/*  73 */     this(listener, new LwjglApplicationConfiguration(), new LwjglGraphics(canvas));
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, LwjglApplicationConfiguration config, Canvas canvas) {
/*  77 */     this(listener, config, new LwjglGraphics(canvas, config));
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
/*     */ 
/*     */   
/*     */   private static LwjglApplicationConfiguration createConfig(String title, int width, int height) {
/* 112 */     LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
/* 113 */     config.title = title;
/* 114 */     config.width = width;
/* 115 */     config.height = height;
/* 116 */     config.vSyncEnabled = true;
/* 117 */     return config;
/*     */   }
/*     */   
/*     */   private void initialize() {
/* 121 */     this.mainLoopThread = new Thread("LWJGL Application")
/*     */       {
/*     */         public void run() {
/* 124 */           LwjglApplication.this.graphics.setVSync(LwjglApplication.this.graphics.config.vSyncEnabled);
/*     */           try {
/* 126 */             LwjglApplication.this.mainLoop();
/* 127 */           } catch (Throwable t) {
/* 128 */             if (LwjglApplication.this.audio != null) LwjglApplication.this.audio.dispose(); 
/* 129 */             Gdx.input.setCursorCatched(false);
/* 130 */             if (t instanceof RuntimeException) {
/* 131 */               throw (RuntimeException)t;
/*     */             }
/* 133 */             throw new GdxRuntimeException(t);
/*     */           } 
/*     */         }
/*     */       };
/* 137 */     this.mainLoopThread.start();
/*     */   }
/*     */   
/*     */   void mainLoop() {
/* 141 */     SnapshotArray<LifecycleListener> lifecycleListeners = this.lifecycleListeners;
/*     */     
/*     */     try {
/* 144 */       this.graphics.setupDisplay();
/* 145 */     } catch (LWJGLException e) {
/* 146 */       throw new GdxRuntimeException(e);
/*     */     } 
/*     */     
/* 149 */     this.listener.create();
/* 150 */     this.graphics.resize = true;
/*     */     
/* 152 */     int lastWidth = this.graphics.getWidth();
/* 153 */     int lastHeight = this.graphics.getHeight();
/*     */     
/* 155 */     this.graphics.lastTime = System.nanoTime();
/* 156 */     boolean wasActive = true;
/* 157 */     while (this.running) {
/* 158 */       Display.processMessages();
/* 159 */       if (Display.isCloseRequested()) exit();
/*     */       
/* 161 */       boolean isActive = Display.isActive();
/* 162 */       if (wasActive && !isActive) {
/* 163 */         wasActive = false;
/* 164 */         synchronized (lifecycleListeners) {
/* 165 */           LifecycleListener[] listeners = (LifecycleListener[])lifecycleListeners.begin();
/* 166 */           for (int i = 0, n = lifecycleListeners.size; i < n; i++)
/* 167 */             listeners[i].pause(); 
/* 168 */           lifecycleListeners.end();
/*     */         } 
/* 170 */         this.listener.pause();
/*     */       } 
/* 172 */       if (!wasActive && isActive) {
/* 173 */         wasActive = true;
/* 174 */         synchronized (lifecycleListeners) {
/* 175 */           LifecycleListener[] listeners = (LifecycleListener[])lifecycleListeners.begin();
/* 176 */           for (int i = 0, n = lifecycleListeners.size; i < n; i++)
/* 177 */             listeners[i].resume(); 
/* 178 */           lifecycleListeners.end();
/*     */         } 
/* 180 */         this.listener.resume();
/*     */       } 
/*     */       
/* 183 */       boolean shouldRender = false;
/*     */       
/* 185 */       if (this.graphics.canvas != null) {
/* 186 */         int width = this.graphics.canvas.getWidth();
/* 187 */         int height = this.graphics.canvas.getHeight();
/* 188 */         if (lastWidth != width || lastHeight != height) {
/* 189 */           lastWidth = width;
/* 190 */           lastHeight = height;
/* 191 */           Gdx.gl.glViewport(0, 0, lastWidth, lastHeight);
/* 192 */           this.listener.resize(lastWidth, lastHeight);
/* 193 */           shouldRender = true;
/*     */         } 
/*     */       } else {
/* 196 */         this.graphics.config.x = Display.getX();
/* 197 */         this.graphics.config.y = Display.getY();
/* 198 */         if (this.graphics.resize || Display.wasResized() || 
/* 199 */           (int)(Display.getWidth() * Display.getPixelScaleFactor()) != this.graphics.config.width || 
/* 200 */           (int)(Display.getHeight() * Display.getPixelScaleFactor()) != this.graphics.config.height) {
/* 201 */           this.graphics.resize = false;
/* 202 */           this.graphics.config.width = (int)(Display.getWidth() * Display.getPixelScaleFactor());
/* 203 */           this.graphics.config.height = (int)(Display.getHeight() * Display.getPixelScaleFactor());
/* 204 */           Gdx.gl.glViewport(0, 0, this.graphics.config.width, this.graphics.config.height);
/* 205 */           if (this.listener != null) this.listener.resize(this.graphics.config.width, this.graphics.config.height); 
/* 206 */           this.graphics.requestRendering();
/*     */         } 
/*     */       } 
/*     */       
/* 210 */       if (executeRunnables()) shouldRender = true;
/*     */ 
/*     */       
/* 213 */       if (!this.running)
/*     */         break; 
/* 215 */       this.input.update();
/* 216 */       shouldRender |= this.graphics.shouldRender();
/* 217 */       this.input.processEvents();
/* 218 */       if (this.audio != null) this.audio.update();
/*     */       
/* 220 */       if (!isActive && this.graphics.config.backgroundFPS == -1) shouldRender = false; 
/* 221 */       int frameRate = isActive ? this.graphics.config.foregroundFPS : this.graphics.config.backgroundFPS;
/* 222 */       if (shouldRender) {
/* 223 */         this.graphics.updateTime();
/* 224 */         this.graphics.frameId++;
/* 225 */         this.listener.render();
/* 226 */         Display.update(false);
/*     */       } else {
/*     */         
/* 229 */         if (frameRate == -1) frameRate = 10; 
/* 230 */         if (frameRate == 0) frameRate = this.graphics.config.backgroundFPS; 
/* 231 */         if (frameRate == 0) frameRate = 30; 
/*     */       } 
/* 233 */       if (frameRate > 0) Display.sync(frameRate);
/*     */     
/*     */     } 
/* 236 */     synchronized (lifecycleListeners) {
/* 237 */       LifecycleListener[] listeners = (LifecycleListener[])lifecycleListeners.begin();
/* 238 */       for (int i = 0, n = lifecycleListeners.size; i < n; i++) {
/* 239 */         listeners[i].pause();
/* 240 */         listeners[i].dispose();
/*     */       } 
/* 242 */       lifecycleListeners.end();
/*     */     } 
/* 244 */     this.listener.pause();
/* 245 */     this.listener.dispose();
/* 246 */     Display.destroy();
/* 247 */     if (this.audio != null) this.audio.dispose(); 
/* 248 */     if (this.graphics.config.forceExit) System.exit(-1); 
/*     */   }
/*     */   
/*     */   public boolean executeRunnables() {
/* 252 */     synchronized (this.runnables) {
/* 253 */       for (int i = this.runnables.size - 1; i >= 0; i--)
/* 254 */         this.executedRunnables.add(this.runnables.get(i)); 
/* 255 */       this.runnables.clear();
/*     */     } 
/* 257 */     if (this.executedRunnables.size == 0) return false; 
/*     */     while (true) {
/* 259 */       ((Runnable)this.executedRunnables.pop()).run();
/* 260 */       if (this.executedRunnables.size <= 0)
/* 261 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ApplicationListener getApplicationListener() {
/* 266 */     return this.listener;
/*     */   }
/*     */ 
/*     */   
/*     */   public Audio getAudio() {
/* 271 */     return (Audio)this.audio;
/*     */   }
/*     */ 
/*     */   
/*     */   public Files getFiles() {
/* 276 */     return this.files;
/*     */   }
/*     */ 
/*     */   
/*     */   public LwjglGraphics getGraphics() {
/* 281 */     return this.graphics;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input getInput() {
/* 286 */     return this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Net getNet() {
/* 291 */     return this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   public Application.ApplicationType getType() {
/* 296 */     return Application.ApplicationType.Desktop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 301 */     return 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 305 */     this.running = false;
/*     */     try {
/* 307 */       this.mainLoopThread.join();
/* 308 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getJavaHeap() {
/* 314 */     return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNativeHeap() {
/* 319 */     return getJavaHeap();
/*     */   }
/*     */   
/* 322 */   public LwjglApplication(ApplicationListener listener, LwjglApplicationConfiguration config, LwjglGraphics graphics) { this.preferences = new ObjectMap(); LwjglNativesLoader.load(); setApplicationLogger(new LwjglApplicationLogger()); if (config.title == null)
/*     */       config.title = listener.getClass().getSimpleName();  this.graphics = graphics; if (!LwjglApplicationConfiguration.disableAudio)
/*     */       try { this.audio = new OpenALAudio(config.audioDeviceSimultaneousSources, config.audioDeviceBufferCount, config.audioDeviceBufferSize); }
/*     */       catch (Throwable t) { log("LwjglApplication", "Couldn't initialize audio, disabling audio", t); LwjglApplicationConfiguration.disableAudio = true; }
/* 326 */         this.files = new LwjglFiles(); this.input = new LwjglInput(); this.net = new LwjglNet(); this.listener = listener; this.preferencesdir = config.preferencesDirectory; this.preferencesFileType = config.preferencesFileType; Gdx.app = this; Gdx.graphics = graphics; Gdx.audio = (Audio)this.audio; Gdx.files = this.files; Gdx.input = this.input; Gdx.net = this.net; initialize(); } public Preferences getPreferences(String name) { if (this.preferences.containsKey(name)) {
/* 327 */       return (Preferences)this.preferences.get(name);
/*     */     }
/* 329 */     Preferences prefs = new LwjglPreferences(new LwjglFileHandle(new File(this.preferencesdir, name), this.preferencesFileType));
/* 330 */     this.preferences.put(name, prefs);
/* 331 */     return prefs; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Clipboard getClipboard() {
/* 337 */     return new LwjglClipboard();
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRunnable(Runnable runnable) {
/* 342 */     synchronized (this.runnables) {
/* 343 */       this.runnables.add(runnable);
/* 344 */       Gdx.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message) {
/* 350 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void debug(String tag, String message, Throwable exception) {
/* 355 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message) {
/* 360 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message, Throwable exception) {
/* 365 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message) {
/* 370 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message, Throwable exception) {
/* 375 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 380 */     this.logLevel = logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLogLevel() {
/* 385 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationLogger(ApplicationLogger applicationLogger) {
/* 390 */     this.applicationLogger = applicationLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public ApplicationLogger getApplicationLogger() {
/* 395 */     return this.applicationLogger;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exit() {
/* 401 */     postRunnable(new Runnable()
/*     */         {
/*     */           public void run() {
/* 404 */             LwjglApplication.this.running = false;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLifecycleListener(LifecycleListener listener) {
/* 411 */     synchronized (this.lifecycleListeners) {
/* 412 */       this.lifecycleListeners.add(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeLifecycleListener(LifecycleListener listener) {
/* 418 */     synchronized (this.lifecycleListeners) {
/* 419 */       this.lifecycleListeners.removeValue(listener, true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */