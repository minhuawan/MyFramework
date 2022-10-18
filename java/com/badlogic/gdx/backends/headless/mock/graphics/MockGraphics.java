/*     */ package com.badlogic.gdx.backends.headless.mock.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.graphics.Cursor;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.GL30;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.glutils.GLVersion;
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
/*     */ public class MockGraphics
/*     */   implements Graphics
/*     */ {
/*  32 */   long frameId = -1L;
/*  33 */   float deltaTime = 0.0F;
/*  34 */   long frameStart = 0L;
/*  35 */   int frames = 0;
/*     */   int fps;
/*  37 */   long lastTime = System.nanoTime();
/*  38 */   GLVersion glVersion = new GLVersion(Application.ApplicationType.HeadlessDesktop, "", "", "");
/*     */   
/*     */   public boolean isGL30Available() {
/*  41 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public GL20 getGL20() {
/*  46 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public GL30 getGL30() {
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  56 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  61 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBackBufferWidth() {
/*  66 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBackBufferHeight() {
/*  71 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFrameId() {
/*  76 */     return this.frameId;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDeltaTime() {
/*  81 */     return this.deltaTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRawDeltaTime() {
/*  86 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFramesPerSecond() {
/*  91 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.GraphicsType getType() {
/*  96 */     return Graphics.GraphicsType.Mock;
/*     */   }
/*     */ 
/*     */   
/*     */   public GLVersion getGLVersion() {
/* 101 */     return this.glVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpiX() {
/* 106 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpiY() {
/* 111 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpcX() {
/* 116 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpcY() {
/* 121 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDensity() {
/* 126 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsDisplayModeChange() {
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode[] getDisplayModes() {
/* 136 */     return new Graphics.DisplayMode[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode getDisplayMode() {
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setFullscreenMode(Graphics.DisplayMode displayMode) {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setWindowedMode(int width, int height) {
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(String title) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVSync(boolean vsync) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Graphics.BufferFormat getBufferFormat() {
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsExtension(String extension) {
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContinuousRendering(boolean isContinuous) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContinuousRendering() {
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestRendering() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullscreen() {
/* 191 */     return false;
/*     */   }
/*     */   
/*     */   public void updateTime() {
/* 195 */     long time = System.nanoTime();
/* 196 */     this.deltaTime = (float)(time - this.lastTime) / 1.0E9F;
/* 197 */     this.lastTime = time;
/*     */     
/* 199 */     if (time - this.frameStart >= 1000000000L) {
/* 200 */       this.fps = this.frames;
/* 201 */       this.frames = 0;
/* 202 */       this.frameStart = time;
/*     */     } 
/* 204 */     this.frames++;
/*     */   }
/*     */   
/*     */   public void incrementFrameId() {
/* 208 */     this.frameId++;
/*     */   }
/*     */ 
/*     */   
/*     */   public Cursor newCursor(Pixmap pixmap, int xHotspot, int yHotspot) {
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursor(Cursor cursor) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSystemCursor(Cursor.SystemCursor systemCursor) {}
/*     */ 
/*     */   
/*     */   public Graphics.Monitor getPrimaryMonitor() {
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor getMonitor() {
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor[] getMonitors() {
/* 236 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode[] getDisplayModes(Graphics.Monitor monitor) {
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode getDisplayMode(Graphics.Monitor monitor) {
/* 246 */     return null;
/*     */   }
/*     */   
/*     */   public void setUndecorated(boolean undecorated) {}
/*     */   
/*     */   public void setResizable(boolean resizable) {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\mock\graphics\MockGraphics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */