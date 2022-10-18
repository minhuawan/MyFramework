/*    */ package com.badlogic.gdx.graphics.g3d.particles.renderers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Material;
/*    */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*    */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*    */ import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ModelInstanceParticleBatch;
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
/*    */ public class ModelInstanceRenderer
/*    */   extends ParticleControllerRenderer<ModelInstanceControllerRenderData, ModelInstanceParticleBatch>
/*    */ {
/*    */   private boolean hasColor;
/*    */   private boolean hasScale;
/*    */   private boolean hasRotation;
/*    */   
/*    */   public ModelInstanceRenderer() {
/* 36 */     super(new ModelInstanceControllerRenderData());
/*    */   }
/*    */   
/*    */   public ModelInstanceRenderer(ModelInstanceParticleBatch batch) {
/* 40 */     this();
/* 41 */     setBatch((ParticleBatch<?>)batch);
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 46 */     this.renderData.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 51 */     this.renderData.modelInstanceChannel = (ParallelArray.ObjectChannel<ModelInstance>)this.controller.particles.getChannel(ParticleChannels.ModelInstance);
/* 52 */     this.renderData.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Color);
/* 53 */     this.renderData.scaleChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Scale);
/* 54 */     this.renderData.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Rotation3D);
/* 55 */     this.hasColor = (this.renderData.colorChannel != null);
/* 56 */     this.hasScale = (this.renderData.scaleChannel != null);
/* 57 */     this.hasRotation = (this.renderData.rotationChannel != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 62 */     for (int i = 0, positionOffset = 0, c = this.controller.particles.size; i < c; i++, positionOffset += this.renderData.positionChannel.strideSize) {
/* 63 */       ModelInstance instance = ((ModelInstance[])this.renderData.modelInstanceChannel.data)[i];
/* 64 */       float scale = this.hasScale ? this.renderData.scaleChannel.data[i] : 1.0F;
/* 65 */       float qx = 0.0F, qy = 0.0F, qz = 0.0F, qw = 1.0F;
/* 66 */       if (this.hasRotation) {
/* 67 */         int rotationOffset = i * this.renderData.rotationChannel.strideSize;
/* 68 */         qx = this.renderData.rotationChannel.data[rotationOffset + 0];
/* 69 */         qy = this.renderData.rotationChannel.data[rotationOffset + 1];
/* 70 */         qz = this.renderData.rotationChannel.data[rotationOffset + 2];
/* 71 */         qw = this.renderData.rotationChannel.data[rotationOffset + 3];
/*    */       } 
/*    */       
/* 74 */       instance.transform.set(this.renderData.positionChannel.data[positionOffset + 0], this.renderData.positionChannel.data[positionOffset + 1], this.renderData.positionChannel.data[positionOffset + 2], qx, qy, qz, qw, scale, scale, scale);
/*    */ 
/*    */       
/* 77 */       if (this.hasColor) {
/* 78 */         int colorOffset = i * this.renderData.colorChannel.strideSize;
/* 79 */         ColorAttribute colorAttribute = (ColorAttribute)((Material)instance.materials.get(0)).get(ColorAttribute.Diffuse);
/* 80 */         BlendingAttribute blendingAttribute = (BlendingAttribute)((Material)instance.materials.get(0)).get(BlendingAttribute.Type);
/* 81 */         colorAttribute.color.r = this.renderData.colorChannel.data[colorOffset + 0];
/* 82 */         colorAttribute.color.g = this.renderData.colorChannel.data[colorOffset + 1];
/* 83 */         colorAttribute.color.b = this.renderData.colorChannel.data[colorOffset + 2];
/* 84 */         if (blendingAttribute != null)
/* 85 */           blendingAttribute.opacity = this.renderData.colorChannel.data[colorOffset + 3]; 
/*    */       } 
/*    */     } 
/* 88 */     super.update();
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerComponent copy() {
/* 93 */     return new ModelInstanceRenderer(this.batch);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompatible(ParticleBatch<?> batch) {
/* 98 */     return batch instanceof ModelInstanceParticleBatch;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\renderers\ModelInstanceRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */