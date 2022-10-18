/*    */ package com.badlogic.gdx.graphics.profiling;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GLErrorListener
/*    */ {
/* 36 */   public static final GLErrorListener LOGGING_LISTENER = new GLErrorListener()
/*    */     {
/*    */       public void onError(int error) {
/* 39 */         String place = null;
/*    */         try {
/* 41 */           StackTraceElement[] stack = Thread.currentThread().getStackTrace();
/* 42 */           for (int i = 0; i < stack.length; i++) {
/* 43 */             if ("check".equals(stack[i].getMethodName())) {
/* 44 */               if (i + 1 < stack.length) {
/* 45 */                 StackTraceElement glMethod = stack[i + 1];
/* 46 */                 place = glMethod.getMethodName();
/*    */               } 
/*    */               break;
/*    */             } 
/*    */           } 
/* 51 */         } catch (Exception exception) {}
/*    */ 
/*    */         
/* 54 */         if (place != null) {
/* 55 */           Gdx.app.error("GLProfiler", "Error " + GLProfiler.resolveErrorNumber(error) + " from " + place);
/*    */         } else {
/* 57 */           Gdx.app.error("GLProfiler", "Error " + GLProfiler.resolveErrorNumber(error) + " at: ", new Exception());
/*    */         } 
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */   
/* 64 */   public static final GLErrorListener THROWING_LISTENER = new GLErrorListener()
/*    */     {
/*    */       public void onError(int error) {
/* 67 */         throw new GdxRuntimeException("GLProfiler: Got GL error " + GLProfiler.resolveErrorNumber(error));
/*    */       }
/*    */     };
/*    */   
/*    */   void onError(int paramInt);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\profiling\GLErrorListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */