/*    */ package com.badlogic.gdx.utils.async;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Disposable;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public class AsyncExecutor
/*    */   implements Disposable
/*    */ {
/*    */   private final ExecutorService executor;
/*    */   
/*    */   public AsyncExecutor(int maxConcurrent) {
/* 38 */     this.executor = Executors.newFixedThreadPool(maxConcurrent, new ThreadFactory()
/*    */         {
/*    */           public Thread newThread(Runnable r) {
/* 41 */             Thread thread = new Thread(r, "AsynchExecutor-Thread");
/* 42 */             thread.setDaemon(true);
/* 43 */             return thread;
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> AsyncResult<T> submit(final AsyncTask<T> task) {
/* 52 */     if (this.executor.isShutdown()) {
/* 53 */       throw new GdxRuntimeException("Cannot run tasks on an executor that has been shutdown (disposed)");
/*    */     }
/* 55 */     return new AsyncResult<T>(this.executor.submit(new Callable<T>()
/*    */           {
/*    */             public T call() throws Exception {
/* 58 */               return task.call();
/*    */             }
/*    */           }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 67 */     this.executor.shutdown();
/*    */     try {
/* 69 */       this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
/* 70 */     } catch (InterruptedException e) {
/* 71 */       throw new GdxRuntimeException("Couldn't shutdown loading thread", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\async\AsyncExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */