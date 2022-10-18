/*    */ package com.badlogic.gdx.utils;
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
/*    */ public class PerformanceCounters
/*    */ {
/*    */   private static final float nano2seconds = 1.0E-9F;
/* 25 */   private long lastTick = 0L;
/* 26 */   public final Array<PerformanceCounter> counters = new Array<PerformanceCounter>();
/*    */   
/*    */   public PerformanceCounter add(String name, int windowSize) {
/* 29 */     PerformanceCounter result = new PerformanceCounter(name, windowSize);
/* 30 */     this.counters.add(result);
/* 31 */     return result;
/*    */   }
/*    */   
/*    */   public PerformanceCounter add(String name) {
/* 35 */     PerformanceCounter result = new PerformanceCounter(name);
/* 36 */     this.counters.add(result);
/* 37 */     return result;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 41 */     long t = TimeUtils.nanoTime();
/* 42 */     if (this.lastTick > 0L) tick((float)(t - this.lastTick) * 1.0E-9F); 
/* 43 */     this.lastTick = t;
/*    */   }
/*    */   
/*    */   public void tick(float deltaTime) {
/* 47 */     for (int i = 0; i < this.counters.size; i++)
/* 48 */       ((PerformanceCounter)this.counters.get(i)).tick(deltaTime); 
/*    */   }
/*    */   
/*    */   public StringBuilder toString(StringBuilder sb) {
/* 52 */     sb.setLength(0);
/* 53 */     for (int i = 0; i < this.counters.size; i++) {
/* 54 */       if (i != 0) sb.append("; "); 
/* 55 */       ((PerformanceCounter)this.counters.get(i)).toString(sb);
/*    */     } 
/* 57 */     return sb;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\PerformanceCounters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */