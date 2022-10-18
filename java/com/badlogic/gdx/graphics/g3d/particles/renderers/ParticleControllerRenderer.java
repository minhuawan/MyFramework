/*    */ package com.badlogic.gdx.graphics.g3d.particles.renderers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
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
/*    */ public abstract class ParticleControllerRenderer<D extends ParticleControllerRenderData, T extends ParticleBatch<D>>
/*    */   extends ParticleControllerComponent
/*    */ {
/*    */   protected T batch;
/*    */   protected D renderData;
/*    */   
/*    */   protected ParticleControllerRenderer() {}
/*    */   
/*    */   protected ParticleControllerRenderer(D renderData) {
/* 35 */     this.renderData = renderData;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 40 */     this.batch.draw((ParticleControllerRenderData)this.renderData);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean setBatch(ParticleBatch<?> batch) {
/* 45 */     if (isCompatible(batch)) {
/* 46 */       this.batch = (T)batch;
/* 47 */       return true;
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract boolean isCompatible(ParticleBatch<?> paramParticleBatch);
/*    */   
/*    */   public void set(ParticleController particleController) {
/* 56 */     super.set(particleController);
/* 57 */     if (this.renderData != null) ((ParticleControllerRenderData)this.renderData).controller = this.controller; 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\renderers\ParticleControllerRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */