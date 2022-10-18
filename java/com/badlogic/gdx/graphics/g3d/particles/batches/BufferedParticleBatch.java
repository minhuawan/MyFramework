/*    */ package com.badlogic.gdx.graphics.g3d.particles.batches;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleSorter;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderData;
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ public abstract class BufferedParticleBatch<T extends ParticleControllerRenderData>
/*    */   implements ParticleBatch<T>
/*    */ {
/*    */   protected Array<T> renderData;
/*    */   protected int bufferedParticlesCount;
/* 28 */   protected int currentCapacity = 0;
/*    */   protected ParticleSorter sorter;
/*    */   protected Camera camera;
/*    */   
/*    */   protected BufferedParticleBatch(Class<T> type) {
/* 33 */     this.sorter = (ParticleSorter)new ParticleSorter.Distance();
/* 34 */     this.renderData = new Array(false, 10, type);
/*    */   }
/*    */   
/*    */   public void begin() {
/* 38 */     this.renderData.clear();
/* 39 */     this.bufferedParticlesCount = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(T data) {
/* 44 */     if (((ParticleControllerRenderData)data).controller.particles.size > 0) {
/* 45 */       this.renderData.add(data);
/* 46 */       this.bufferedParticlesCount += ((ParticleControllerRenderData)data).controller.particles.size;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {
/* 52 */     if (this.bufferedParticlesCount > 0) {
/* 53 */       ensureCapacity(this.bufferedParticlesCount);
/* 54 */       flush(this.sorter.sort(this.renderData));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void ensureCapacity(int capacity) {
/* 60 */     if (this.currentCapacity >= capacity)
/* 61 */       return;  this.sorter.ensureCapacity(capacity);
/* 62 */     allocParticlesData(capacity);
/* 63 */     this.currentCapacity = capacity;
/*    */   }
/*    */   
/*    */   public void resetCapacity() {
/* 67 */     this.currentCapacity = this.bufferedParticlesCount = 0;
/*    */   }
/*    */   
/*    */   protected abstract void allocParticlesData(int paramInt);
/*    */   
/*    */   public void setCamera(Camera camera) {
/* 73 */     this.camera = camera;
/* 74 */     this.sorter.setCamera(camera);
/*    */   }
/*    */   
/*    */   public ParticleSorter getSorter() {
/* 78 */     return this.sorter;
/*    */   }
/*    */   
/*    */   public void setSorter(ParticleSorter sorter) {
/* 82 */     this.sorter = sorter;
/* 83 */     sorter.setCamera(this.camera);
/* 84 */     sorter.ensureCapacity(this.currentCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void flush(int[] paramArrayOfint);
/*    */ 
/*    */   
/*    */   public int getBufferedCount() {
/* 93 */     return this.bufferedParticlesCount;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\batches\BufferedParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */