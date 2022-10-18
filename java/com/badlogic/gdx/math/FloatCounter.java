/*    */ package com.badlogic.gdx.math;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FloatCounter
/*    */ {
/*    */   public int count;
/*    */   public float total;
/*    */   public float min;
/*    */   public float max;
/*    */   public float average;
/*    */   public float latest;
/*    */   public float value;
/*    */   public final WindowedMean mean;
/*    */   
/*    */   public FloatCounter(int windowSize) {
/* 46 */     this.mean = (windowSize > 1) ? new WindowedMean(windowSize) : null;
/* 47 */     reset();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void put(float value) {
/* 53 */     this.latest = value;
/* 54 */     this.total += value;
/* 55 */     this.count++;
/* 56 */     this.average = this.total / this.count;
/*    */     
/* 58 */     if (this.mean != null) {
/* 59 */       this.mean.addValue(value);
/* 60 */       this.value = this.mean.getMean();
/*    */     } else {
/* 62 */       this.value = this.latest;
/*    */     } 
/* 64 */     if (this.mean == null || this.mean.hasEnoughData()) {
/* 65 */       if (this.value < this.min) this.min = this.value; 
/* 66 */       if (this.value > this.max) this.max = this.value;
/*    */     
/*    */     } 
/*    */   }
/*    */   
/*    */   public void reset() {
/* 72 */     this.count = 0;
/* 73 */     this.total = 0.0F;
/* 74 */     this.min = Float.MAX_VALUE;
/* 75 */     this.max = Float.MIN_VALUE;
/* 76 */     this.average = 0.0F;
/* 77 */     this.latest = 0.0F;
/* 78 */     this.value = 0.0F;
/* 79 */     if (this.mean != null) this.mean.clear(); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\FloatCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */