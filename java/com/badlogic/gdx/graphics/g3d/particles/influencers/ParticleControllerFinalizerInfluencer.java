/*    */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
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
/*    */ public class ParticleControllerFinalizerInfluencer
/*    */   extends Influencer
/*    */ {
/*    */   ParallelArray.FloatChannel positionChannel;
/*    */   ParallelArray.FloatChannel scaleChannel;
/*    */   ParallelArray.FloatChannel rotationChannel;
/*    */   ParallelArray.ObjectChannel<ParticleController> controllerChannel;
/*    */   boolean hasScale;
/*    */   boolean hasRotation;
/*    */   
/*    */   public void init() {
/* 38 */     this.controllerChannel = (ParallelArray.ObjectChannel<ParticleController>)this.controller.particles.getChannel(ParticleChannels.ParticleController);
/* 39 */     if (this.controllerChannel == null) {
/* 40 */       throw new GdxRuntimeException("ParticleController channel not found, specify an influencer which will allocate it please.");
/*    */     }
/* 42 */     this.scaleChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Scale);
/* 43 */     this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Rotation3D);
/* 44 */     this.hasScale = (this.scaleChannel != null);
/* 45 */     this.hasRotation = (this.rotationChannel != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 50 */     this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 55 */     for (int i = 0, positionOffset = 0, c = this.controller.particles.size; i < c; i++, positionOffset += this.positionChannel.strideSize) {
/* 56 */       ParticleController particleController = ((ParticleController[])this.controllerChannel.data)[i];
/* 57 */       float scale = this.hasScale ? this.scaleChannel.data[i] : 1.0F;
/* 58 */       float qx = 0.0F, qy = 0.0F, qz = 0.0F, qw = 1.0F;
/* 59 */       if (this.hasRotation) {
/* 60 */         int rotationOffset = i * this.rotationChannel.strideSize;
/* 61 */         qx = this.rotationChannel.data[rotationOffset + 0];
/* 62 */         qy = this.rotationChannel.data[rotationOffset + 1];
/* 63 */         qz = this.rotationChannel.data[rotationOffset + 2];
/* 64 */         qw = this.rotationChannel.data[rotationOffset + 3];
/*    */       } 
/* 66 */       particleController.setTransform(this.positionChannel.data[positionOffset + 0], this.positionChannel.data[positionOffset + 1], this.positionChannel.data[positionOffset + 2], qx, qy, qz, qw, scale);
/*    */ 
/*    */       
/* 69 */       particleController.update();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerFinalizerInfluencer copy() {
/* 75 */     return new ParticleControllerFinalizerInfluencer();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\ParticleControllerFinalizerInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */