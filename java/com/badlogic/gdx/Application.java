/*     */ package com.badlogic.gdx;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Clipboard;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Application
/*     */ {
/*     */   public static final int LOG_NONE = 0;
/*     */   public static final int LOG_DEBUG = 3;
/*     */   public static final int LOG_INFO = 2;
/*     */   public static final int LOG_ERROR = 1;
/*     */   
/*     */   ApplicationListener getApplicationListener();
/*     */   
/*     */   Graphics getGraphics();
/*     */   
/*     */   Audio getAudio();
/*     */   
/*     */   Input getInput();
/*     */   
/*     */   Files getFiles();
/*     */   
/*     */   Net getNet();
/*     */   
/*     */   void log(String paramString1, String paramString2);
/*     */   
/*     */   void log(String paramString1, String paramString2, Throwable paramThrowable);
/*     */   
/*     */   void error(String paramString1, String paramString2);
/*     */   
/*     */   void error(String paramString1, String paramString2, Throwable paramThrowable);
/*     */   
/*     */   void debug(String paramString1, String paramString2);
/*     */   
/*     */   void debug(String paramString1, String paramString2, Throwable paramThrowable);
/*     */   
/*     */   void setLogLevel(int paramInt);
/*     */   
/*     */   int getLogLevel();
/*     */   
/*     */   void setApplicationLogger(ApplicationLogger paramApplicationLogger);
/*     */   
/*     */   ApplicationLogger getApplicationLogger();
/*     */   
/*     */   ApplicationType getType();
/*     */   
/*     */   int getVersion();
/*     */   
/*     */   long getJavaHeap();
/*     */   
/*     */   long getNativeHeap();
/*     */   
/*     */   Preferences getPreferences(String paramString);
/*     */   
/*     */   Clipboard getClipboard();
/*     */   
/*     */   void postRunnable(Runnable paramRunnable);
/*     */   
/*     */   void exit();
/*     */   
/*     */   void addLifecycleListener(LifecycleListener paramLifecycleListener);
/*     */   
/*     */   void removeLifecycleListener(LifecycleListener paramLifecycleListener);
/*     */   
/*     */   public enum ApplicationType
/*     */   {
/* 102 */     Android, Desktop, HeadlessDesktop, Applet, WebGL, iOS;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\Application.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */