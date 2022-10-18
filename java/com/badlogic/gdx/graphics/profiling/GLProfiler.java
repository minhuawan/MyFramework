/*     */ package com.badlogic.gdx.graphics.profiling;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.math.FloatCounter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GLProfiler
/*     */ {
/*     */   public static int calls;
/*     */   public static int textureBindings;
/*     */   public static int drawCalls;
/*     */   public static int shaderSwitches;
/*  52 */   public static final FloatCounter vertexCount = new FloatCounter(0);
/*     */   
/*     */   public static String resolveErrorNumber(int error) {
/*  55 */     switch (error) {
/*     */       case 1281:
/*  57 */         return "GL_INVALID_VALUE";
/*     */       case 1282:
/*  59 */         return "GL_INVALID_OPERATION";
/*     */       case 1286:
/*  61 */         return "GL_INVALID_FRAMEBUFFER_OPERATION";
/*     */       case 1280:
/*  63 */         return "GL_INVALID_ENUM";
/*     */       case 1285:
/*  65 */         return "GL_OUT_OF_MEMORY";
/*     */     } 
/*  67 */     return "number " + error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static GLErrorListener listener = GLErrorListener.LOGGING_LISTENER;
/*     */ 
/*     */   
/*     */   public static void enable() {
/*  78 */     if (!isEnabled()) {
/*  79 */       Gdx.gl30 = (Gdx.gl30 == null) ? null : new GL30Profiler(Gdx.gl30);
/*  80 */       Gdx.gl20 = (Gdx.gl30 != null) ? (GL20)Gdx.gl30 : new GL20Profiler(Gdx.gl20);
/*  81 */       Gdx.gl = Gdx.gl20;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void disable() {
/*  87 */     if (Gdx.gl30 != null && Gdx.gl30 instanceof GL30Profiler) Gdx.gl30 = ((GL30Profiler)Gdx.gl30).gl30; 
/*  88 */     if (Gdx.gl20 != null && Gdx.gl20 instanceof GL20Profiler) Gdx.gl20 = ((GL20Profiler)Gdx.gl).gl20; 
/*  89 */     if (Gdx.gl != null && Gdx.gl instanceof GL20Profiler) Gdx.gl = ((GL20Profiler)Gdx.gl).gl20;
/*     */   
/*     */   }
/*     */   
/*     */   public static boolean isEnabled() {
/*  94 */     return (Gdx.gl30 instanceof GL30Profiler || Gdx.gl20 instanceof GL20Profiler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reset() {
/* 100 */     calls = 0;
/* 101 */     textureBindings = 0;
/* 102 */     drawCalls = 0;
/* 103 */     shaderSwitches = 0;
/* 104 */     vertexCount.reset();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\profiling\GLProfiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */