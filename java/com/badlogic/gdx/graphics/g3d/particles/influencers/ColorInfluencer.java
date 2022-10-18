/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.GradientColorValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
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
/*     */ public abstract class ColorInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   ParallelArray.FloatChannel colorChannel;
/*     */   
/*     */   public static class Random
/*     */     extends ColorInfluencer
/*     */   {
/*     */     ParallelArray.FloatChannel colorChannel;
/*     */     
/*     */     public void allocateChannels() {
/*  37 */       this.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Color);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  42 */       for (int i = startIndex * this.colorChannel.strideSize, c = i + count * this.colorChannel.strideSize; i < c; i += this.colorChannel.strideSize) {
/*  43 */         this.colorChannel.data[i + 0] = MathUtils.random();
/*  44 */         this.colorChannel.data[i + 1] = MathUtils.random();
/*  45 */         this.colorChannel.data[i + 2] = MathUtils.random();
/*  46 */         this.colorChannel.data[i + 3] = MathUtils.random();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/*  52 */       return new Random();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Single
/*     */     extends ColorInfluencer {
/*     */     ParallelArray.FloatChannel alphaInterpolationChannel;
/*     */     ParallelArray.FloatChannel lifeChannel;
/*     */     public ScaledNumericValue alphaValue;
/*     */     public GradientColorValue colorValue;
/*     */     
/*     */     public Single() {
/*  64 */       this.colorValue = new GradientColorValue();
/*  65 */       this.alphaValue = new ScaledNumericValue();
/*  66 */       this.alphaValue.setHigh(1.0F);
/*     */     }
/*     */     
/*     */     public Single(Single billboardColorInfluencer) {
/*  70 */       this();
/*  71 */       set(billboardColorInfluencer);
/*     */     }
/*     */     
/*     */     public void set(Single colorInfluencer) {
/*  75 */       this.colorValue.load(colorInfluencer.colorValue);
/*  76 */       this.alphaValue.load(colorInfluencer.alphaValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/*  81 */       super.allocateChannels();
/*     */       
/*  83 */       ParticleChannels.Interpolation.id = this.controller.particleChannels.newId();
/*  84 */       this.alphaInterpolationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation);
/*  85 */       this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  90 */       int i = startIndex * this.colorChannel.strideSize, a = startIndex * this.alphaInterpolationChannel.strideSize, l = startIndex * this.lifeChannel.strideSize + 2;
/*  91 */       for (int c = i + count * this.colorChannel.strideSize; i < c; i += this.colorChannel.strideSize, a += this.alphaInterpolationChannel.strideSize, l += this.lifeChannel.strideSize) {
/*  92 */         float alphaStart = this.alphaValue.newLowValue();
/*  93 */         float alphaDiff = this.alphaValue.newHighValue() - alphaStart;
/*  94 */         this.colorValue.getColor(0.0F, this.colorChannel.data, i);
/*  95 */         this.colorChannel.data[i + 3] = alphaStart + alphaDiff * this.alphaValue
/*  96 */           .getScale(this.lifeChannel.data[l]);
/*  97 */         this.alphaInterpolationChannel.data[a + 0] = alphaStart;
/*  98 */         this.alphaInterpolationChannel.data[a + 1] = alphaDiff;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 104 */       int i = 0, a = 0, l = 2, c = i + this.controller.particles.size * this.colorChannel.strideSize;
/* 105 */       for (; i < c; i += this.colorChannel.strideSize, a += this.alphaInterpolationChannel.strideSize, l += this.lifeChannel.strideSize) {
/*     */         
/* 107 */         float lifePercent = this.lifeChannel.data[l];
/* 108 */         this.colorValue.getColor(lifePercent, this.colorChannel.data, i);
/* 109 */         this.colorChannel.data[i + 3] = this.alphaInterpolationChannel.data[a + 0] + this.alphaInterpolationChannel.data[a + 1] * this.alphaValue
/*     */           
/* 111 */           .getScale(lifePercent);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/* 117 */       return new Single(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/* 122 */       json.writeValue("alpha", this.alphaValue);
/* 123 */       json.writeValue("color", this.colorValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 128 */       this.alphaValue = (ScaledNumericValue)json.readValue("alpha", ScaledNumericValue.class, jsonData);
/* 129 */       this.colorValue = (GradientColorValue)json.readValue("color", GradientColorValue.class, jsonData);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 137 */     this.colorChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Color);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\ColorInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */