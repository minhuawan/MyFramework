/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.awt.DisplayMode;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.util.ArrayList;
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
/*     */ public class LwjglApplicationConfiguration
/*     */ {
/*     */   public static boolean disableAudio;
/*     */   public boolean useGL30 = false;
/*  43 */   public int gles30ContextMajorVersion = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public int gles30ContextMinorVersion = 2;
/*     */ 
/*     */   
/*  52 */   public int r = 8, g = 8, b = 8, a = 8;
/*     */   
/*  54 */   public int depth = 16, stencil = 0;
/*     */   
/*  56 */   public int samples = 0;
/*     */   
/*  58 */   public int width = 640, height = 480;
/*     */   
/*  60 */   public int x = -1; public int y = -1;
/*     */   
/*     */   public boolean fullscreen = false;
/*     */   
/*  64 */   public int overrideDensity = -1;
/*     */   
/*     */   public boolean vSyncEnabled = true;
/*     */   
/*     */   public String title;
/*     */   
/*     */   public boolean forceExit = true;
/*     */   
/*     */   public boolean resizable = true;
/*     */   
/*  74 */   public int audioDeviceSimultaneousSources = 16;
/*     */   
/*  76 */   public int audioDeviceBufferSize = 512;
/*     */   
/*  78 */   public int audioDeviceBufferCount = 9;
/*  79 */   public Color initialBackgroundColor = Color.BLACK;
/*     */   
/*  81 */   public int foregroundFPS = 60;
/*     */   
/*  83 */   public int backgroundFPS = 60;
/*     */ 
/*     */   
/*     */   public boolean allowSoftwareMode = false;
/*     */   
/*  88 */   public String preferencesDirectory = ".prefs/";
/*     */   
/*  90 */   public Files.FileType preferencesFileType = Files.FileType.External;
/*     */   
/*     */   public LwjglGraphics.SetDisplayModeCallback setDisplayModeCallback;
/*     */   
/*     */   public boolean useHDPI = false;
/*     */   
/*  96 */   Array<String> iconPaths = new Array();
/*  97 */   Array<Files.FileType> iconFileTypes = new Array();
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIcon(String path, Files.FileType fileType) {
/* 102 */     this.iconPaths.add(path);
/* 103 */     this.iconFileTypes.add(fileType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromDisplayMode(Graphics.DisplayMode mode) {
/* 109 */     this.width = mode.width;
/* 110 */     this.height = mode.height;
/* 111 */     if (mode.bitsPerPixel == 16) {
/* 112 */       this.r = 5;
/* 113 */       this.g = 6;
/* 114 */       this.b = 5;
/* 115 */       this.a = 0;
/*     */     } 
/* 117 */     if (mode.bitsPerPixel == 24) {
/* 118 */       this.r = 8;
/* 119 */       this.g = 8;
/* 120 */       this.b = 8;
/* 121 */       this.a = 0;
/*     */     } 
/* 123 */     if (mode.bitsPerPixel == 32) {
/* 124 */       this.r = 8;
/* 125 */       this.g = 8;
/* 126 */       this.b = 8;
/* 127 */       this.a = 8;
/*     */     } 
/* 129 */     this.fullscreen = true;
/*     */   }
/*     */   
/*     */   protected static class LwjglApplicationConfigurationDisplayMode extends Graphics.DisplayMode {
/*     */     protected LwjglApplicationConfigurationDisplayMode(int width, int height, int refreshRate, int bitsPerPixel) {
/* 134 */       super(width, height, refreshRate, bitsPerPixel);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Graphics.DisplayMode getDesktopDisplayMode() {
/* 139 */     GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 140 */     GraphicsDevice device = genv.getDefaultScreenDevice();
/* 141 */     DisplayMode mode = device.getDisplayMode();
/* 142 */     return new LwjglApplicationConfigurationDisplayMode(mode.getWidth(), mode.getHeight(), mode.getRefreshRate(), mode
/* 143 */         .getBitDepth());
/*     */   }
/*     */   
/*     */   public static Graphics.DisplayMode[] getDisplayModes() {
/* 147 */     GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 148 */     GraphicsDevice device = genv.getDefaultScreenDevice();
/* 149 */     DisplayMode desktopMode = device.getDisplayMode();
/* 150 */     DisplayMode[] displayModes = device.getDisplayModes();
/* 151 */     ArrayList<Graphics.DisplayMode> modes = new ArrayList<Graphics.DisplayMode>();
/* 152 */     int idx = 0;
/* 153 */     for (DisplayMode mode : displayModes) {
/* 154 */       boolean duplicate = false;
/* 155 */       for (int i = 0; i < modes.size(); i++) {
/* 156 */         if (((Graphics.DisplayMode)modes.get(i)).width == mode.getWidth() && ((Graphics.DisplayMode)modes.get(i)).height == mode.getHeight() && ((Graphics.DisplayMode)modes
/* 157 */           .get(i)).bitsPerPixel == mode.getBitDepth()) {
/* 158 */           duplicate = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 162 */       if (!duplicate && 
/* 163 */         mode.getBitDepth() == desktopMode.getBitDepth()) {
/* 164 */         modes.add(new LwjglApplicationConfigurationDisplayMode(mode.getWidth(), mode.getHeight(), mode.getRefreshRate(), mode
/* 165 */               .getBitDepth()));
/*     */       }
/*     */     } 
/* 168 */     return modes.<Graphics.DisplayMode>toArray(new Graphics.DisplayMode[modes.size()]);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglApplicationConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */