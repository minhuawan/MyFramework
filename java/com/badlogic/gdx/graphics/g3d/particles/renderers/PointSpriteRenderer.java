/*    */ package com.badlogic.gdx.graphics.g3d.particles.renderers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
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
/*    */ public class PointSpriteRenderer
/*    */   extends ParticleControllerRenderer<PointSpriteControllerRenderData, PointSpriteParticleBatch>
/*    */ {
/*    */   public PointSpriteRenderer() {
/* 33 */     super(new PointSpriteControllerRenderData());
/*    */   }
/*    */   
/*    */   public PointSpriteRenderer(PointSpriteParticleBatch batch) {
/* 37 */     this();
/* 38 */     setBatch((ParticleBatch<?>)batch);
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 43 */     this.renderData.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/* 44 */     this.renderData.regionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.TextureRegion, (ParallelArray.ChannelInitializer)ParticleChannels.TextureRegionInitializer.get());
/* 45 */     this.renderData.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Color, (ParallelArray.ChannelInitializer)ParticleChannels.ColorInitializer.get());
/* 46 */     this.renderData.scaleChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Scale, (ParallelArray.ChannelInitializer)ParticleChannels.ScaleInitializer.get());
/* 47 */     this.renderData.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation2D, (ParallelArray.ChannelInitializer)ParticleChannels.Rotation2dInitializer.get());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompatible(ParticleBatch<?> batch) {
/* 52 */     return batch instanceof PointSpriteParticleBatch;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerComponent copy() {
/* 57 */     return new PointSpriteRenderer(this.batch);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\renderers\PointSpriteRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */