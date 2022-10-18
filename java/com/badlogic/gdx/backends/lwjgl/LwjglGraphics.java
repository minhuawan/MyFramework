/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.graphics.Cursor;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.GL30;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.glutils.GLVersion;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Toolkit;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.ContextAttribs;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.DisplayMode;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL30;
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
/*     */ public class LwjglGraphics
/*     */   implements Graphics
/*     */ {
/*     */   static Array<String> extensions;
/*     */   static GLVersion glVersion;
/*     */   GL20 gl20;
/*     */   GL30 gl30;
/*  54 */   long frameId = -1L;
/*  55 */   float deltaTime = 0.0F;
/*  56 */   long frameStart = 0L;
/*  57 */   int frames = 0;
/*     */   int fps;
/*  59 */   long lastTime = System.nanoTime();
/*     */   Canvas canvas;
/*     */   boolean vsync = false;
/*     */   boolean resize = false;
/*     */   LwjglApplicationConfiguration config;
/*  64 */   Graphics.BufferFormat bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 16, 8, 0, false);
/*     */   volatile boolean isContinuous = true;
/*     */   volatile boolean requestRendering = false;
/*     */   boolean softwareMode;
/*     */   boolean usingGL30;
/*     */   
/*     */   LwjglGraphics(LwjglApplicationConfiguration config) {
/*  71 */     this.config = config;
/*     */   }
/*     */   
/*     */   LwjglGraphics(Canvas canvas) {
/*  75 */     this.config = new LwjglApplicationConfiguration();
/*  76 */     this.config.width = canvas.getWidth();
/*  77 */     this.config.height = canvas.getHeight();
/*  78 */     this.canvas = canvas;
/*     */   }
/*     */   
/*     */   LwjglGraphics(Canvas canvas, LwjglApplicationConfiguration config) {
/*  82 */     this.config = config;
/*  83 */     this.canvas = canvas;
/*     */   }
/*     */   
/*     */   public GL20 getGL20() {
/*  87 */     return this.gl20;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  91 */     if (this.canvas != null) {
/*  92 */       return Math.max(1, this.canvas.getHeight());
/*     */     }
/*  94 */     return (int)(Display.getHeight() * Display.getPixelScaleFactor());
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  98 */     if (this.canvas != null) {
/*  99 */       return Math.max(1, this.canvas.getWidth());
/*     */     }
/* 101 */     return (int)(Display.getWidth() * Display.getPixelScaleFactor());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBackBufferWidth() {
/* 106 */     return getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBackBufferHeight() {
/* 111 */     return getHeight();
/*     */   }
/*     */   
/*     */   public boolean isGL20Available() {
/* 115 */     return (this.gl20 != null);
/*     */   }
/*     */   
/*     */   public long getFrameId() {
/* 119 */     return this.frameId;
/*     */   }
/*     */   
/*     */   public float getDeltaTime() {
/* 123 */     return this.deltaTime;
/*     */   }
/*     */   
/*     */   public float getRawDeltaTime() {
/* 127 */     return this.deltaTime;
/*     */   }
/*     */   
/*     */   public Graphics.GraphicsType getType() {
/* 131 */     return Graphics.GraphicsType.LWJGL;
/*     */   }
/*     */   
/*     */   public GLVersion getGLVersion() {
/* 135 */     return glVersion;
/*     */   }
/*     */   
/*     */   public int getFramesPerSecond() {
/* 139 */     return this.fps;
/*     */   }
/*     */   
/*     */   void updateTime() {
/* 143 */     long time = System.nanoTime();
/* 144 */     this.deltaTime = (float)(time - this.lastTime) / 1.0E9F;
/* 145 */     this.lastTime = time;
/*     */     
/* 147 */     if (time - this.frameStart >= 1000000000L) {
/* 148 */       this.fps = this.frames;
/* 149 */       this.frames = 0;
/* 150 */       this.frameStart = time;
/*     */     } 
/* 152 */     this.frames++;
/*     */   }
/*     */   
/*     */   void setupDisplay() throws LWJGLException {
/* 156 */     if (this.config.useHDPI) {
/* 157 */       System.setProperty("org.lwjgl.opengl.Display.enableHighDPI", "true");
/*     */     }
/*     */     
/* 160 */     if (this.canvas != null) {
/* 161 */       Display.setParent(this.canvas);
/*     */     } else {
/* 163 */       boolean displayCreated = false;
/*     */       
/* 165 */       if (!this.config.fullscreen) {
/* 166 */         displayCreated = setWindowedMode(this.config.width, this.config.height);
/*     */       } else {
/* 168 */         Graphics.DisplayMode bestMode = null;
/* 169 */         for (Graphics.DisplayMode mode : getDisplayModes()) {
/* 170 */           if (mode.width == this.config.width && mode.height == this.config.height && (
/* 171 */             bestMode == null || bestMode.refreshRate < (getDisplayMode()).refreshRate)) {
/* 172 */             bestMode = mode;
/*     */           }
/*     */         } 
/*     */         
/* 176 */         if (bestMode == null) {
/* 177 */           bestMode = getDisplayMode();
/*     */         }
/* 179 */         displayCreated = setFullscreenMode(bestMode);
/*     */       } 
/* 181 */       if (!displayCreated) {
/* 182 */         if (this.config.setDisplayModeCallback != null) {
/* 183 */           this.config = this.config.setDisplayModeCallback.onFailure(this.config);
/* 184 */           if (this.config != null) {
/* 185 */             displayCreated = setWindowedMode(this.config.width, this.config.height);
/*     */           }
/*     */         } 
/* 188 */         if (!displayCreated) {
/* 189 */           throw new GdxRuntimeException("Couldn't set display mode " + this.config.width + "x" + this.config.height + ", fullscreen: " + this.config.fullscreen);
/*     */         }
/*     */       } 
/*     */       
/* 193 */       if (this.config.iconPaths.size > 0) {
/* 194 */         ByteBuffer[] icons = new ByteBuffer[this.config.iconPaths.size];
/* 195 */         for (int i = 0, n = this.config.iconPaths.size; i < n; i++) {
/* 196 */           Pixmap pixmap = new Pixmap(Gdx.files.getFileHandle((String)this.config.iconPaths.get(i), (Files.FileType)this.config.iconFileTypes.get(i)));
/* 197 */           if (pixmap.getFormat() != Pixmap.Format.RGBA8888) {
/* 198 */             Pixmap rgba = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
/* 199 */             rgba.drawPixmap(pixmap, 0, 0);
/* 200 */             pixmap.dispose();
/* 201 */             pixmap = rgba;
/*     */           } 
/* 203 */           icons[i] = ByteBuffer.allocateDirect(pixmap.getPixels().limit());
/* 204 */           icons[i].put(pixmap.getPixels()).flip();
/* 205 */           pixmap.dispose();
/*     */         } 
/* 207 */         Display.setIcon(icons);
/*     */       } 
/*     */     } 
/* 210 */     Display.setTitle(this.config.title);
/* 211 */     Display.setResizable(this.config.resizable);
/* 212 */     Display.setInitialBackground(this.config.initialBackgroundColor.r, this.config.initialBackgroundColor.g, this.config.initialBackgroundColor.b);
/*     */ 
/*     */     
/* 215 */     Display.setLocation(this.config.x, this.config.y);
/* 216 */     createDisplayPixelFormat(this.config.useGL30, this.config.gles30ContextMajorVersion, this.config.gles30ContextMinorVersion);
/* 217 */     initiateGL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void initiateGL() {
/* 224 */     extractVersion();
/* 225 */     extractExtensions();
/* 226 */     initiateGLInstances();
/*     */   }
/*     */   
/*     */   private static void extractVersion() {
/* 230 */     String versionString = GL11.glGetString(7938);
/* 231 */     String vendorString = GL11.glGetString(7936);
/* 232 */     String rendererString = GL11.glGetString(7937);
/* 233 */     glVersion = new GLVersion(Application.ApplicationType.Desktop, versionString, vendorString, rendererString);
/*     */   }
/*     */   
/*     */   private static void extractExtensions() {
/* 237 */     extensions = new Array();
/* 238 */     if (glVersion.isVersionEqualToOrHigher(3, 2)) {
/* 239 */       int numExtensions = GL11.glGetInteger(33309);
/* 240 */       for (int i = 0; i < numExtensions; i++)
/* 241 */         extensions.add(GL30.glGetStringi(7939, i)); 
/*     */     } else {
/* 243 */       extensions.addAll((Object[])GL11.glGetString(7939).split(" "));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean fullCompatibleWithGLES3() {
/* 250 */     return glVersion.isVersionEqualToOrHigher(4, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean fullCompatibleWithGLES2() {
/* 257 */     return (glVersion.isVersionEqualToOrHigher(4, 1) || extensions.contains("GL_ARB_ES2_compatibility", false));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean supportsFBO() {
/* 262 */     return (glVersion.isVersionEqualToOrHigher(3, 0) || extensions.contains("GL_EXT_framebuffer_object", false) || extensions
/* 263 */       .contains("GL_ARB_framebuffer_object", false));
/*     */   }
/*     */   
/*     */   private void createDisplayPixelFormat(boolean useGL30, int gles30ContextMajor, int gles30ContextMinor) {
/*     */     try {
/* 268 */       if (useGL30) {
/*     */         
/* 270 */         ContextAttribs context = (new ContextAttribs(gles30ContextMajor, gles30ContextMinor)).withForwardCompatible(false).withProfileCore(true);
/*     */         try {
/* 272 */           Display.create(new PixelFormat(this.config.r + this.config.g + this.config.b, this.config.a, this.config.depth, this.config.stencil, this.config.samples), context);
/*     */         }
/* 274 */         catch (Exception e) {
/* 275 */           System.out.println("LwjglGraphics: OpenGL " + gles30ContextMajor + "." + gles30ContextMinor + "+ core profile (GLES 3.0) not supported.");
/*     */           
/* 277 */           createDisplayPixelFormat(false, gles30ContextMajor, gles30ContextMinor);
/*     */           return;
/*     */         } 
/* 280 */         System.out.println("LwjglGraphics: created OpenGL " + gles30ContextMajor + "." + gles30ContextMinor + "+ core profile (GLES 3.0) context. This is experimental!");
/*     */         
/* 282 */         this.usingGL30 = true;
/*     */       } else {
/*     */         
/* 285 */         Display.create(new PixelFormat(this.config.r + this.config.g + this.config.b, this.config.a, this.config.depth, this.config.stencil, this.config.samples));
/* 286 */         this.usingGL30 = false;
/*     */       } 
/* 288 */       this.bufferFormat = new Graphics.BufferFormat(this.config.r, this.config.g, this.config.b, this.config.a, this.config.depth, this.config.stencil, this.config.samples, false);
/*     */     }
/* 290 */     catch (Exception ex) {
/* 291 */       Display.destroy();
/*     */       try {
/* 293 */         Thread.sleep(200L);
/* 294 */       } catch (InterruptedException interruptedException) {}
/*     */       
/*     */       try {
/* 297 */         Display.create(new PixelFormat(0, 16, 8));
/* 298 */         if ((getDisplayMode()).bitsPerPixel == 16) {
/* 299 */           this.bufferFormat = new Graphics.BufferFormat(5, 6, 5, 0, 16, 8, 0, false);
/*     */         }
/* 301 */         if ((getDisplayMode()).bitsPerPixel == 24) {
/* 302 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 0, 16, 8, 0, false);
/*     */         }
/* 304 */         if ((getDisplayMode()).bitsPerPixel == 32) {
/* 305 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 16, 8, 0, false);
/*     */         }
/* 307 */       } catch (Exception ex2) {
/* 308 */         Display.destroy();
/*     */         try {
/* 310 */           Thread.sleep(200L);
/* 311 */         } catch (InterruptedException interruptedException) {}
/*     */         
/*     */         try {
/* 314 */           Display.create(new PixelFormat());
/* 315 */         } catch (Exception ex3) {
/* 316 */           if (!this.softwareMode && this.config.allowSoftwareMode) {
/* 317 */             this.softwareMode = true;
/* 318 */             System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
/* 319 */             createDisplayPixelFormat(useGL30, gles30ContextMajor, gles30ContextMinor);
/*     */             return;
/*     */           } 
/* 322 */           throw new GdxRuntimeException("OpenGL is not supported by the video driver.", ex3);
/*     */         } 
/* 324 */         if ((getDisplayMode()).bitsPerPixel == 16) {
/* 325 */           this.bufferFormat = new Graphics.BufferFormat(5, 6, 5, 0, 8, 0, 0, false);
/*     */         }
/* 327 */         if ((getDisplayMode()).bitsPerPixel == 24) {
/* 328 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 0, 8, 0, 0, false);
/*     */         }
/* 330 */         if ((getDisplayMode()).bitsPerPixel == 32) {
/* 331 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 8, 0, 0, false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initiateGLInstances() {
/* 338 */     if (this.usingGL30) {
/* 339 */       this.gl30 = new LwjglGL30();
/* 340 */       this.gl20 = (GL20)this.gl30;
/*     */     } else {
/* 342 */       this.gl20 = new LwjglGL20();
/*     */     } 
/*     */     
/* 345 */     if (!glVersion.isVersionEqualToOrHigher(2, 0)) {
/* 346 */       throw new GdxRuntimeException("OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: " + 
/* 347 */           GL11.glGetString(7938) + "\n" + glVersion.getDebugVersionString());
/*     */     }
/* 349 */     if (!supportsFBO()) {
/* 350 */       throw new GdxRuntimeException("OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: " + 
/* 351 */           GL11.glGetString(7938) + ", FBO extension: false\n" + glVersion.getDebugVersionString());
/*     */     }
/*     */     
/* 354 */     Gdx.gl = this.gl20;
/* 355 */     Gdx.gl20 = this.gl20;
/* 356 */     Gdx.gl30 = this.gl30;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpiX() {
/* 361 */     return Toolkit.getDefaultToolkit().getScreenResolution();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpiY() {
/* 366 */     return Toolkit.getDefaultToolkit().getScreenResolution();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpcX() {
/* 371 */     return Toolkit.getDefaultToolkit().getScreenResolution() / 2.54F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpcY() {
/* 376 */     return Toolkit.getDefaultToolkit().getScreenResolution() / 2.54F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDensity() {
/* 381 */     if (this.config.overrideDensity != -1) return this.config.overrideDensity / 160.0F; 
/* 382 */     return Toolkit.getDefaultToolkit().getScreenResolution() / 160.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsDisplayModeChange() {
/* 387 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor getPrimaryMonitor() {
/* 392 */     return new LwjglMonitor(0, 0, "Primary Monitor");
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor getMonitor() {
/* 397 */     return getPrimaryMonitor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor[] getMonitors() {
/* 402 */     return new Graphics.Monitor[] { getPrimaryMonitor() };
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode[] getDisplayModes(Graphics.Monitor monitor) {
/* 407 */     return getDisplayModes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode getDisplayMode(Graphics.Monitor monitor) {
/* 412 */     return getDisplayMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setFullscreenMode(Graphics.DisplayMode displayMode) {
/* 417 */     DisplayMode mode = ((LwjglDisplayMode)displayMode).mode;
/*     */     try {
/* 419 */       if (!mode.isFullscreenCapable()) {
/* 420 */         Display.setDisplayMode(mode);
/*     */       } else {
/* 422 */         Display.setDisplayModeAndFullscreen(mode);
/*     */       } 
/* 424 */       float scaleFactor = Display.getPixelScaleFactor();
/* 425 */       this.config.width = (int)(mode.getWidth() * scaleFactor);
/* 426 */       this.config.height = (int)(mode.getHeight() * scaleFactor);
/* 427 */       if (Gdx.gl != null) Gdx.gl.glViewport(0, 0, this.config.width, this.config.height); 
/* 428 */       this.resize = true;
/* 429 */       return true;
/* 430 */     } catch (LWJGLException e) {
/* 431 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setWindowedMode(int width, int height) {
/* 438 */     if (getWidth() == width && getHeight() == height && !Display.isFullscreen()) {
/* 439 */       return true;
/*     */     }
/*     */     
/*     */     try {
/* 443 */       DisplayMode targetDisplayMode = null;
/* 444 */       boolean fullscreen = false;
/*     */       
/* 446 */       if (fullscreen) {
/* 447 */         DisplayMode[] modes = Display.getAvailableDisplayModes();
/* 448 */         int freq = 0;
/*     */         
/* 450 */         for (int i = 0; i < modes.length; i++) {
/* 451 */           DisplayMode current = modes[i];
/*     */           
/* 453 */           if (current.getWidth() == width && current.getHeight() == height) {
/* 454 */             if ((targetDisplayMode == null || current.getFrequency() >= freq) && (
/* 455 */               targetDisplayMode == null || current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
/* 456 */               targetDisplayMode = current;
/* 457 */               freq = targetDisplayMode.getFrequency();
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 464 */             if (current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel() && current
/* 465 */               .getFrequency() == Display.getDesktopDisplayMode().getFrequency()) {
/* 466 */               targetDisplayMode = current;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 472 */         targetDisplayMode = new DisplayMode(width, height);
/*     */       } 
/*     */       
/* 475 */       if (targetDisplayMode == null) {
/* 476 */         return false;
/*     */       }
/*     */       
/* 479 */       boolean resizable = (!fullscreen && this.config.resizable);
/*     */       
/* 481 */       Display.setDisplayMode(targetDisplayMode);
/* 482 */       Display.setFullscreen(fullscreen);
/*     */       
/* 484 */       if (resizable == Display.isResizable()) {
/* 485 */         Display.setResizable(!resizable);
/*     */       }
/* 487 */       Display.setResizable(resizable);
/*     */       
/* 489 */       float scaleFactor = Display.getPixelScaleFactor();
/* 490 */       this.config.width = (int)(targetDisplayMode.getWidth() * scaleFactor);
/* 491 */       this.config.height = (int)(targetDisplayMode.getHeight() * scaleFactor);
/* 492 */       if (Gdx.gl != null) Gdx.gl.glViewport(0, 0, this.config.width, this.config.height); 
/* 493 */       this.resize = true;
/* 494 */       return true;
/* 495 */     } catch (LWJGLException e) {
/* 496 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode[] getDisplayModes() {
/*     */     try {
/* 503 */       DisplayMode[] availableDisplayModes = Display.getAvailableDisplayModes();
/* 504 */       Graphics.DisplayMode[] modes = new Graphics.DisplayMode[availableDisplayModes.length];
/*     */       
/* 506 */       int idx = 0;
/* 507 */       for (DisplayMode mode : availableDisplayModes) {
/* 508 */         if (mode.isFullscreenCapable()) {
/* 509 */           modes[idx++] = new LwjglDisplayMode(mode.getWidth(), mode.getHeight(), mode.getFrequency(), mode
/* 510 */               .getBitsPerPixel(), mode);
/*     */         }
/*     */       } 
/*     */       
/* 514 */       return modes;
/* 515 */     } catch (LWJGLException e) {
/* 516 */       throw new GdxRuntimeException("Couldn't fetch available display modes", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode getDisplayMode() {
/* 522 */     DisplayMode mode = Display.getDesktopDisplayMode();
/* 523 */     return new LwjglDisplayMode(mode.getWidth(), mode.getHeight(), mode.getFrequency(), mode.getBitsPerPixel(), mode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTitle(String title) {
/* 528 */     Display.setTitle(title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUndecorated(boolean undecorated) {
/* 537 */     System.setProperty("org.lwjgl.opengl.Window.undecorated", undecorated ? "true" : "false");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResizable(boolean resizable) {
/* 546 */     this.config.resizable = resizable;
/* 547 */     Display.setResizable(resizable);
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.BufferFormat getBufferFormat() {
/* 552 */     return this.bufferFormat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVSync(boolean vsync) {
/* 557 */     this.vsync = vsync;
/* 558 */     Display.setVSyncEnabled(vsync);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsExtension(String extension) {
/* 563 */     return extensions.contains(extension, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContinuousRendering(boolean isContinuous) {
/* 568 */     this.isContinuous = isContinuous;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isContinuousRendering() {
/* 573 */     return this.isContinuous;
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestRendering() {
/* 578 */     synchronized (this) {
/* 579 */       this.requestRendering = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldRender() {
/* 584 */     synchronized (this) {
/* 585 */       boolean rq = this.requestRendering;
/* 586 */       this.requestRendering = false;
/* 587 */       return (rq || this.isContinuous || Display.isDirty());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullscreen() {
/* 593 */     return Display.isFullscreen();
/*     */   }
/*     */   
/*     */   public boolean isSoftwareMode() {
/* 597 */     return this.softwareMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGL30Available() {
/* 602 */     return (this.gl30 != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public GL30 getGL30() {
/* 607 */     return this.gl30;
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
/*     */   public Cursor newCursor(Pixmap pixmap, int xHotspot, int yHotspot) {
/* 621 */     return new LwjglCursor(pixmap, xHotspot, yHotspot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursor(Cursor cursor) {
/* 626 */     if (this.canvas != null && SharedLibraryLoader.isMac) {
/*     */       return;
/*     */     }
/*     */     try {
/* 630 */       Mouse.setNativeCursor(((LwjglCursor)cursor).lwjglCursor);
/* 631 */     } catch (LWJGLException e) {
/* 632 */       throw new GdxRuntimeException("Could not set cursor image.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSystemCursor(Cursor.SystemCursor systemCursor) {
/* 638 */     if (this.canvas != null && SharedLibraryLoader.isMac) {
/*     */       return;
/*     */     }
/*     */     try {
/* 642 */       Mouse.setNativeCursor(null);
/* 643 */     } catch (LWJGLException e) {
/* 644 */       throw new GdxRuntimeException("Couldn't set system cursor");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface SetDisplayModeCallback {
/*     */     LwjglApplicationConfiguration onFailure(LwjglApplicationConfiguration param1LwjglApplicationConfiguration); }
/*     */   
/*     */   private class LwjglDisplayMode extends Graphics.DisplayMode { public LwjglDisplayMode(int width, int height, int refreshRate, int bitsPerPixel, DisplayMode mode) {
/* 652 */       super(width, height, refreshRate, bitsPerPixel);
/* 653 */       this.mode = mode;
/*     */     }
/*     */     
/*     */     DisplayMode mode; }
/*     */   
/*     */   private class LwjglMonitor extends Graphics.Monitor { protected LwjglMonitor(int virtualX, int virtualY, String name) {
/* 659 */       super(virtualX, virtualY, name);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglGraphics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */