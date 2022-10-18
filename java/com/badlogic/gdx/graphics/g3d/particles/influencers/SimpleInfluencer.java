/*    */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
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
/*    */ public abstract class SimpleInfluencer
/*    */   extends Influencer
/*    */ {
/*    */   public ScaledNumericValue value;
/*    */   ParallelArray.FloatChannel valueChannel;
/*    */   ParallelArray.FloatChannel interpolationChannel;
/*    */   ParallelArray.FloatChannel lifeChannel;
/*    */   ParallelArray.ChannelDescriptor valueChannelDescriptor;
/*    */   
/*    */   public SimpleInfluencer() {
/* 36 */     this.value = new ScaledNumericValue();
/* 37 */     this.value.setHigh(1.0F);
/*    */   }
/*    */   
/*    */   public SimpleInfluencer(SimpleInfluencer billboardScaleinfluencer) {
/* 41 */     this();
/* 42 */     set(billboardScaleinfluencer);
/*    */   }
/*    */   
/*    */   private void set(SimpleInfluencer scaleInfluencer) {
/* 46 */     this.value.load(scaleInfluencer.value);
/* 47 */     this.valueChannelDescriptor = scaleInfluencer.valueChannelDescriptor;
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 52 */     this.valueChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(this.valueChannelDescriptor);
/* 53 */     ParticleChannels.Interpolation.id = this.controller.particleChannels.newId();
/* 54 */     this.interpolationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation);
/* 55 */     this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*    */   }
/*    */ 
/*    */   
/*    */   public void activateParticles(int startIndex, int count) {
/* 60 */     if (!this.value.isRelative()) {
/* 61 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 62 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 63 */         float start = this.value.newLowValue();
/* 64 */         float diff = this.value.newHighValue() - start;
/* 65 */         this.interpolationChannel.data[a + 0] = start;
/* 66 */         this.interpolationChannel.data[a + 1] = diff;
/* 67 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } else {
/* 70 */       int i = startIndex * this.valueChannel.strideSize, a = startIndex * this.interpolationChannel.strideSize, c = i + count * this.valueChannel.strideSize;
/* 71 */       for (; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize) {
/* 72 */         float start = this.value.newLowValue();
/* 73 */         float diff = this.value.newHighValue();
/* 74 */         this.interpolationChannel.data[a + 0] = start;
/* 75 */         this.interpolationChannel.data[a + 1] = diff;
/* 76 */         this.valueChannel.data[i] = start + diff * this.value.getScale(0.0F);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 83 */     for (int i = 0, a = 0, l = 2, c = i + this.controller.particles.size * this.valueChannel.strideSize; i < c; i += this.valueChannel.strideSize, a += this.interpolationChannel.strideSize, l += this.lifeChannel.strideSize)
/*    */     {
/* 85 */       this.valueChannel.data[i] = this.interpolationChannel.data[a + 0] + this.interpolationChannel.data[a + 1] * this.value
/* 86 */         .getScale(this.lifeChannel.data[l]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 92 */     json.writeValue("value", this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 97 */     this.value = (ScaledNumericValue)json.readValue("value", ScaledNumericValue.class, jsonData);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\SimpleInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */