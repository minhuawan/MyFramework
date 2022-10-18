/*    */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
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
/*    */ public class ScaleInfluencer
/*    */   extends SimpleInfluencer
/*    */ {
/*    */   public ScaleInfluencer() {
/* 28 */     this.valueChannelDescriptor = ParticleChannels.Scale;
/*    */   }
/*    */ 
/*    */   
/*    */   public void activateParticles(int startIndex, int count) {
/* 33 */     if (this.value.isRelative()) {
/* 34 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 35 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 36 */         float start = this.value.newLowValue() * this.controller.scale.x;
/* 37 */         float diff = this.value.newHighValue() * this.controller.scale.x;
/* 38 */         this.interpolationChannel.data[a + 0] = start;
/* 39 */         this.interpolationChannel.data[a + 1] = diff;
/* 40 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } else {
/* 43 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 44 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 45 */         float start = this.value.newLowValue() * this.controller.scale.x;
/* 46 */         float diff = this.value.newHighValue() * this.controller.scale.x - start;
/* 47 */         this.interpolationChannel.data[a + 0] = start;
/* 48 */         this.interpolationChannel.data[a + 1] = diff;
/* 49 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public ScaleInfluencer(ScaleInfluencer scaleInfluencer) {
/* 55 */     super(scaleInfluencer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleControllerComponent copy() {
/* 60 */     return new ScaleInfluencer(this);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\ScaleInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */