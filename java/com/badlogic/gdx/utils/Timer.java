/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.LifecycleListener;
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
/*     */ public class Timer
/*     */ {
/*  27 */   static final Array<Timer> instances = new Array<Timer>(1);
/*     */   
/*     */   static TimerThread thread;
/*     */   
/*     */   private static final int CANCELLED = -1;
/*     */   private static final int FOREVER = -2;
/*  33 */   static Timer instance = new Timer();
/*     */   
/*     */   public static Timer instance() {
/*  36 */     if (instance == null) {
/*  37 */       instance = new Timer();
/*     */     }
/*  39 */     return instance;
/*     */   }
/*     */   
/*  42 */   private final Array<Task> tasks = new Array<Task>(false, 8);
/*     */   
/*     */   public Timer() {
/*  45 */     start();
/*     */   }
/*     */ 
/*     */   
/*     */   public Task postTask(Task task) {
/*  50 */     return scheduleTask(task, 0.0F, 0.0F, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Task scheduleTask(Task task, float delaySeconds) {
/*  55 */     return scheduleTask(task, delaySeconds, 0.0F, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Task scheduleTask(Task task, float delaySeconds, float intervalSeconds) {
/*  60 */     return scheduleTask(task, delaySeconds, intervalSeconds, -2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Task scheduleTask(Task task, float delaySeconds, float intervalSeconds, int repeatCount) {
/*  66 */     synchronized (task) {
/*  67 */       if (task.repeatCount != -1) throw new IllegalArgumentException("The same task may not be scheduled twice."); 
/*  68 */       task.executeTimeMillis = System.nanoTime() / 1000000L + (long)(delaySeconds * 1000.0F);
/*  69 */       task.intervalMillis = (long)(intervalSeconds * 1000.0F);
/*  70 */       task.repeatCount = repeatCount;
/*     */     } 
/*  72 */     synchronized (this) {
/*  73 */       this.tasks.add(task);
/*     */     } 
/*  75 */     wake();
/*     */     
/*  77 */     return task;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  82 */     synchronized (instances) {
/*  83 */       instances.removeValue(this, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  89 */     synchronized (instances) {
/*  90 */       if (instances.contains(this, true))
/*  91 */         return;  instances.add(this);
/*  92 */       if (thread == null) thread = new TimerThread(); 
/*  93 */       wake();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  99 */     synchronized (this) {
/* 100 */       for (int i = 0, n = this.tasks.size; i < n; i++)
/* 101 */         ((Task)this.tasks.get(i)).cancel(); 
/* 102 */       this.tasks.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 109 */     synchronized (this) {
/* 110 */       return (this.tasks.size == 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   long update(long timeMillis, long waitMillis) {
/* 115 */     synchronized (this) {
/* 116 */       for (int i = 0, n = this.tasks.size; i < n; i++) {
/* 117 */         Task task = this.tasks.get(i);
/* 118 */         synchronized (task) {
/* 119 */           if (task.executeTimeMillis > timeMillis) {
/* 120 */             waitMillis = Math.min(waitMillis, task.executeTimeMillis - timeMillis);
/*     */           } else {
/*     */             
/* 123 */             if (task.repeatCount != -1) {
/* 124 */               if (task.repeatCount == 0) task.repeatCount = -1; 
/* 125 */               task.app.postRunnable(task);
/*     */             } 
/* 127 */             if (task.repeatCount == -1) {
/* 128 */               this.tasks.removeIndex(i);
/* 129 */               i--;
/* 130 */               n--;
/*     */             } else {
/* 132 */               task.executeTimeMillis = timeMillis + task.intervalMillis;
/* 133 */               waitMillis = Math.min(waitMillis, task.intervalMillis);
/* 134 */               if (task.repeatCount > 0) task.repeatCount--; 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 139 */     }  return waitMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public void delay(long delayMillis) {
/* 144 */     synchronized (this) {
/* 145 */       for (int i = 0, n = this.tasks.size; i < n; i++) {
/* 146 */         Task task = this.tasks.get(i);
/* 147 */         synchronized (task) {
/* 148 */           task.executeTimeMillis += delayMillis;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void wake() {
/* 155 */     synchronized (instances) {
/* 156 */       instances.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Task post(Task task) {
/* 163 */     return instance().postTask(task);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Task schedule(Task task, float delaySeconds) {
/* 169 */     return instance().scheduleTask(task, delaySeconds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Task schedule(Task task, float delaySeconds, float intervalSeconds) {
/* 175 */     return instance().scheduleTask(task, delaySeconds, intervalSeconds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Task schedule(Task task, float delaySeconds, float intervalSeconds, int repeatCount) {
/* 181 */     return instance().scheduleTask(task, delaySeconds, intervalSeconds, repeatCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Task
/*     */     implements Runnable
/*     */   {
/*     */     long executeTimeMillis;
/*     */     long intervalMillis;
/* 190 */     int repeatCount = -1;
/*     */     Application app;
/*     */     
/*     */     public Task() {
/* 194 */       this.app = Gdx.app;
/* 195 */       if (this.app == null) throw new IllegalStateException("Gdx.app not available.");
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract void run();
/*     */ 
/*     */     
/*     */     public synchronized void cancel() {
/* 204 */       this.executeTimeMillis = 0L;
/* 205 */       this.repeatCount = -1;
/*     */     }
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
/*     */     public synchronized boolean isScheduled() {
/* 219 */       return (this.repeatCount != -1);
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized long getExecuteTimeMillis() {
/* 224 */       return this.executeTimeMillis;
/*     */     }
/*     */   }
/*     */   
/*     */   static class TimerThread
/*     */     implements Runnable, LifecycleListener
/*     */   {
/*     */     Files files;
/*     */     private long pauseMillis;
/*     */     
/*     */     public TimerThread() {
/* 235 */       Gdx.app.addLifecycleListener(this);
/* 236 */       resume();
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       while (true) {
/* 241 */         synchronized (Timer.instances) {
/* 242 */           if (this.files != Gdx.files)
/*     */             return; 
/* 244 */           long timeMillis = System.nanoTime() / 1000000L;
/* 245 */           long waitMillis = 5000L;
/* 246 */           for (int i = 0, n = Timer.instances.size; i < n; i++) {
/*     */             try {
/* 248 */               waitMillis = ((Timer)Timer.instances.get(i)).update(timeMillis, waitMillis);
/* 249 */             } catch (Throwable ex) {
/* 250 */               throw new GdxRuntimeException("Task failed: " + ((Timer)Timer.instances.get(i)).getClass().getName(), ex);
/*     */             } 
/*     */           } 
/*     */           
/* 254 */           if (this.files != Gdx.files)
/*     */             return; 
/*     */           try {
/* 257 */             if (waitMillis > 0L) Timer.instances.wait(waitMillis); 
/* 258 */           } catch (InterruptedException interruptedException) {}
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void resume() {
/* 265 */       long delayMillis = System.nanoTime() / 1000000L - this.pauseMillis;
/* 266 */       synchronized (Timer.instances) {
/* 267 */         for (int i = 0, n = Timer.instances.size; i < n; i++) {
/* 268 */           ((Timer)Timer.instances.get(i)).delay(delayMillis);
/*     */         }
/*     */       } 
/* 271 */       this.files = Gdx.files;
/* 272 */       Thread t = new Thread(this, "Timer");
/* 273 */       t.setDaemon(true);
/* 274 */       t.start();
/* 275 */       Timer.thread = this;
/*     */     }
/*     */     
/*     */     public void pause() {
/* 279 */       this.pauseMillis = System.nanoTime() / 1000000L;
/* 280 */       synchronized (Timer.instances) {
/* 281 */         this.files = null;
/* 282 */         Timer.wake();
/*     */       } 
/* 284 */       Timer.thread = null;
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 288 */       pause();
/* 289 */       Gdx.app.removeLifecycleListener(this);
/* 290 */       Timer.instances.clear();
/* 291 */       Timer.instance = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Timer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */