/*     */ package com.badlogic.gdx;public interface Graphics { boolean isGL30Available();
/*     */   GL20 getGL20();
/*     */   GL30 getGL30();
/*     */   int getWidth();
/*     */   int getHeight();
/*     */   int getBackBufferWidth();
/*     */   int getBackBufferHeight();
/*     */   long getFrameId();
/*     */   float getDeltaTime();
/*     */   float getRawDeltaTime();
/*     */   int getFramesPerSecond();
/*     */   GraphicsType getType();
/*     */   GLVersion getGLVersion();
/*     */   float getPpiX();
/*     */   float getPpiY();
/*     */   float getPpcX();
/*     */   float getPpcY();
/*     */   float getDensity();
/*     */   boolean supportsDisplayModeChange();
/*     */   Monitor getPrimaryMonitor();
/*     */   Monitor getMonitor();
/*     */   Monitor[] getMonitors();
/*     */   DisplayMode[] getDisplayModes();
/*     */   DisplayMode[] getDisplayModes(Monitor paramMonitor);
/*     */   DisplayMode getDisplayMode();
/*     */   DisplayMode getDisplayMode(Monitor paramMonitor);
/*     */   boolean setFullscreenMode(DisplayMode paramDisplayMode);
/*     */   boolean setWindowedMode(int paramInt1, int paramInt2);
/*     */   void setTitle(String paramString);
/*     */   void setUndecorated(boolean paramBoolean);
/*     */   void setResizable(boolean paramBoolean);
/*     */   
/*     */   void setVSync(boolean paramBoolean);
/*     */   
/*     */   BufferFormat getBufferFormat();
/*     */   
/*     */   boolean supportsExtension(String paramString);
/*     */   
/*     */   void setContinuousRendering(boolean paramBoolean);
/*     */   
/*     */   boolean isContinuousRendering();
/*     */   
/*     */   void requestRendering();
/*     */   
/*     */   boolean isFullscreen();
/*     */   
/*     */   Cursor newCursor(Pixmap paramPixmap, int paramInt1, int paramInt2);
/*     */   
/*     */   void setCursor(Cursor paramCursor);
/*     */   
/*     */   void setSystemCursor(Cursor.SystemCursor paramSystemCursor);
/*     */   
/*  53 */   public enum GraphicsType { AndroidGL, LWJGL, WebGL, iOSGL, JGLFW, Mock, LWJGL3; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DisplayMode
/*     */   {
/*     */     public final int width;
/*     */     
/*     */     public final int height;
/*     */     
/*     */     public final int refreshRate;
/*     */     
/*     */     public final int bitsPerPixel;
/*     */ 
/*     */     
/*     */     protected DisplayMode(int width, int height, int refreshRate, int bitsPerPixel) {
/*  70 */       this.width = width;
/*  71 */       this.height = height;
/*  72 */       this.refreshRate = refreshRate;
/*  73 */       this.bitsPerPixel = bitsPerPixel;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  77 */       return this.width + "x" + this.height + ", bpp: " + this.bitsPerPixel + ", hz: " + this.refreshRate;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Monitor
/*     */   {
/*     */     public final int virtualX;
/*     */     
/*     */     public final int virtualY;
/*     */     
/*     */     public final String name;
/*     */     
/*     */     protected Monitor(int virtualX, int virtualY, String name) {
/*  91 */       this.virtualX = virtualX;
/*  92 */       this.virtualY = virtualY;
/*  93 */       this.name = name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BufferFormat
/*     */   {
/*     */     public final int r;
/*     */     public final int g;
/*     */     public final int b;
/*     */     public final int a;
/*     */     public final int depth;
/*     */     public final int stencil;
/*     */     public final int samples;
/*     */     public final boolean coverageSampling;
/*     */     
/*     */     public BufferFormat(int r, int g, int b, int a, int depth, int stencil, int samples, boolean coverageSampling) {
/* 109 */       this.r = r;
/* 110 */       this.g = g;
/* 111 */       this.b = b;
/* 112 */       this.a = a;
/* 113 */       this.depth = depth;
/* 114 */       this.stencil = stencil;
/* 115 */       this.samples = samples;
/* 116 */       this.coverageSampling = coverageSampling;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 120 */       return "r: " + this.r + ", g: " + this.g + ", b: " + this.b + ", a: " + this.a + ", depth: " + this.depth + ", stencil: " + this.stencil + ", num samples: " + this.samples + ", coverage sampling: " + this.coverageSampling;
/*     */     }
/*     */   } }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\Graphics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */