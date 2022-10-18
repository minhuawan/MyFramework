/*    */ package com.badlogic.gdx.utils.async;
/*    */ 
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
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
/*    */ public class AsyncResult<T>
/*    */ {
/*    */   private final Future<T> future;
/*    */   
/*    */   AsyncResult(Future<T> future) {
/* 30 */     this.future = future;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 35 */     return this.future.isDone();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T get() {
/*    */     try {
/* 42 */       return this.future.get();
/* 43 */     } catch (InterruptedException ex) {
/* 44 */       return null;
/* 45 */     } catch (ExecutionException ex) {
/* 46 */       throw new GdxRuntimeException(ex.getCause());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\async\AsyncResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */