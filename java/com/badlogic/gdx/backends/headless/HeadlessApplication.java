/*     */ package com.badlogic.gdx.backends.headless;
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
/*     */ import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
/*     */ import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
/*     */ import com.badlogic.gdx.backends.headless.mock.input.MockInput;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Clipboard;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
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
/*     */ public class HeadlessApplication
/*     */   implements Application
/*     */ {
/*     */   protected final ApplicationListener listener;
/*     */   protected Thread mainLoopThread;
/*     */   protected final HeadlessFiles files;
/*     */   protected final HeadlessNet net;
/*     */   protected final MockAudio audio;
/*     */   protected final MockInput input;
/*     */   protected final MockGraphics graphics;
/*     */   protected boolean running = true;
/*  50 */   protected final Array<Runnable> runnables = new Array();
/*  51 */   protected final Array<Runnable> executedRunnables = new Array();
/*  52 */   protected final Array<LifecycleListener> lifecycleListeners = new Array();
/*  53 */   protected int logLevel = 2; protected ApplicationLogger applicationLogger;
/*     */   private String preferencesdir;
/*     */   private final long renderInterval;
/*     */   ObjectMap<String, Preferences> preferences;
/*     */   
/*     */   public HeadlessApplication(ApplicationListener listener) {
/*  59 */     this(listener, null);
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
/*     */   private void initialize() {
/*  92 */     this.mainLoopThread = new Thread("HeadlessApplication")
/*     */       {
/*     */         public void run() {
/*     */           try {
/*  96 */             HeadlessApplication.this.mainLoop();
/*  97 */           } catch (Throwable t) {
/*  98 */             if (t instanceof RuntimeException) {
/*  99 */               throw (RuntimeException)t;
/*     */             }
/* 101 */             throw new GdxRuntimeException(t);
/*     */           } 
/*     */         }
/*     */       };
/* 105 */     this.mainLoopThread.start();
/*     */   }
/*     */   
/*     */   void mainLoop() {
/* 109 */     Array<LifecycleListener> lifecycleListeners = this.lifecycleListeners;
/*     */     
/* 111 */     this.listener.create();
/*     */ 
/*     */ 
/*     */     
/* 115 */     long t = TimeUtils.nanoTime() + this.renderInterval;
/* 116 */     if ((float)this.renderInterval >= 0.0F)
/* 117 */       while (this.running) {
/* 118 */         long n = TimeUtils.nanoTime();
/* 119 */         if (t > n) {
/*     */           try {
/* 121 */             Thread.sleep((t - n) / 1000000L);
/* 122 */           } catch (InterruptedException interruptedException) {}
/* 123 */           t = TimeUtils.nanoTime() + this.renderInterval;
/*     */         } else {
/* 125 */           t = n + this.renderInterval;
/*     */         } 
/* 127 */         executeRunnables();
/* 128 */         this.graphics.incrementFrameId();
/* 129 */         this.listener.render();
/* 130 */         this.graphics.updateTime();
/*     */ 
/*     */         
/* 133 */         if (!this.running) {
/*     */           break;
/*     */         }
/*     */       }  
/* 137 */     synchronized (lifecycleListeners) {
/* 138 */       for (LifecycleListener listener : lifecycleListeners) {
/* 139 */         listener.pause();
/* 140 */         listener.dispose();
/*     */       } 
/*     */     } 
/* 143 */     this.listener.pause();
/* 144 */     this.listener.dispose();
/*     */   }
/*     */   
/*     */   public boolean executeRunnables() {
/* 148 */     synchronized (this.runnables) {
/* 149 */       for (int j = this.runnables.size - 1; j >= 0; j--)
/* 150 */         this.executedRunnables.add(this.runnables.get(j)); 
/* 151 */       this.runnables.clear();
/*     */     } 
/* 153 */     if (this.executedRunnables.size == 0) return false; 
/* 154 */     for (int i = this.executedRunnables.size - 1; i >= 0; i--)
/* 155 */       ((Runnable)this.executedRunnables.removeIndex(i)).run(); 
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ApplicationListener getApplicationListener() {
/* 161 */     return this.listener;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics getGraphics() {
/* 166 */     return (Graphics)this.graphics;
/*     */   }
/*     */ 
/*     */   
/*     */   public Audio getAudio() {
/* 171 */     return (Audio)this.audio;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input getInput() {
/* 176 */     return (Input)this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Files getFiles() {
/* 181 */     return this.files;
/*     */   }
/*     */ 
/*     */   
/*     */   public Net getNet() {
/* 186 */     return this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   public Application.ApplicationType getType() {
/* 191 */     return Application.ApplicationType.HeadlessDesktop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 196 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getJavaHeap() {
/* 201 */     return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNativeHeap() {
/* 206 */     return getJavaHeap();
/*     */   }
/*     */   
/* 209 */   public HeadlessApplication(ApplicationListener listener, HeadlessApplicationConfiguration config) { this.preferences = new ObjectMap(); if (config == null)
/*     */       config = new HeadlessApplicationConfiguration();  HeadlessNativesLoader.load(); setApplicationLogger(new HeadlessApplicationLogger()); this.listener = listener; this.files = new HeadlessFiles(); this.net = new HeadlessNet(); this.graphics = new MockGraphics(); this.audio = new MockAudio(); this.input = new MockInput(); this.preferencesdir = config.preferencesDirectory; Gdx.app = this; Gdx.files = this.files; Gdx.net = this.net; Gdx.audio = (Audio)this.audio; Gdx.graphics = (Graphics)this.graphics;
/*     */     Gdx.input = (Input)this.input;
/*     */     this.renderInterval = (config.renderInterval > 0.0F) ? (long)(config.renderInterval * 1.0E9F) : ((config.renderInterval < 0.0F) ? -1L : 0L);
/* 213 */     initialize(); } public Preferences getPreferences(String name) { if (this.preferences.containsKey(name)) {
/* 214 */       return (Preferences)this.preferences.get(name);
/*     */     }
/* 216 */     Preferences prefs = new HeadlessPreferences(name, this.preferencesdir);
/* 217 */     this.preferences.put(name, prefs);
/* 218 */     return prefs; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Clipboard getClipboard() {
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRunnable(Runnable runnable) {
/* 230 */     synchronized (this.runnables) {
/* 231 */       this.runnables.add(runnable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message) {
/* 237 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void debug(String tag, String message, Throwable exception) {
/* 242 */     if (this.logLevel >= 3) getApplicationLogger().debug(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message) {
/* 247 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void log(String tag, String message, Throwable exception) {
/* 252 */     if (this.logLevel >= 2) getApplicationLogger().log(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message) {
/* 257 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message);
/*     */   
/*     */   }
/*     */   
/*     */   public void error(String tag, String message, Throwable exception) {
/* 262 */     if (this.logLevel >= 1) getApplicationLogger().error(tag, message, exception);
/*     */   
/*     */   }
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 267 */     this.logLevel = logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLogLevel() {
/* 272 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationLogger(ApplicationLogger applicationLogger) {
/* 277 */     this.applicationLogger = applicationLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public ApplicationLogger getApplicationLogger() {
/* 282 */     return this.applicationLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public void exit() {
/* 287 */     postRunnable(new Runnable()
/*     */         {
/*     */           public void run() {
/* 290 */             HeadlessApplication.this.running = false;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLifecycleListener(LifecycleListener listener) {
/* 297 */     synchronized (this.lifecycleListeners) {
/* 298 */       this.lifecycleListeners.add(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeLifecycleListener(LifecycleListener listener) {
/* 304 */     synchronized (this.lifecycleListeners) {
/* 305 */       this.lifecycleListeners.removeValue(listener, true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\HeadlessApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */