/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ import java.util.Arrays;
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
/*     */ public class DynamicsInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   public Array<DynamicsModifier> velocities;
/*     */   private ParallelArray.FloatChannel accellerationChannel;
/*     */   private ParallelArray.FloatChannel positionChannel;
/*     */   private ParallelArray.FloatChannel previousPositionChannel;
/*     */   private ParallelArray.FloatChannel rotationChannel;
/*     */   private ParallelArray.FloatChannel angularVelocityChannel;
/*     */   boolean hasAcceleration;
/*     */   boolean has2dAngularVelocity;
/*     */   boolean has3dAngularVelocity;
/*     */   
/*     */   public DynamicsInfluencer() {
/*  37 */     this.velocities = new Array(true, 3, DynamicsModifier.class);
/*     */   }
/*     */   
/*     */   public DynamicsInfluencer(DynamicsModifier... velocities) {
/*  41 */     this.velocities = new Array(true, velocities.length, DynamicsModifier.class);
/*  42 */     for (DynamicsModifier value : velocities) {
/*  43 */       this.velocities.add(value.copy());
/*     */     }
/*     */   }
/*     */   
/*     */   public DynamicsInfluencer(DynamicsInfluencer velocityInfluencer) {
/*  48 */     this((DynamicsModifier[])velocityInfluencer.velocities.toArray(DynamicsModifier.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/*  53 */     for (int k = 0; k < this.velocities.size; k++) {
/*  54 */       ((DynamicsModifier[])this.velocities.items)[k].allocateChannels();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  59 */     this.accellerationChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.Acceleration);
/*  60 */     this.hasAcceleration = (this.accellerationChannel != null);
/*  61 */     if (this.hasAcceleration) {
/*  62 */       this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*  63 */       this.previousPositionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.PreviousPosition);
/*     */     } 
/*     */ 
/*     */     
/*  67 */     this.angularVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.AngularVelocity2D);
/*  68 */     this.has2dAngularVelocity = (this.angularVelocityChannel != null);
/*  69 */     if (this.has2dAngularVelocity) {
/*  70 */       this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation2D);
/*  71 */       this.has3dAngularVelocity = false;
/*     */     } else {
/*  73 */       this.angularVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.getChannel(ParticleChannels.AngularVelocity3D);
/*  74 */       this.has3dAngularVelocity = (this.angularVelocityChannel != null);
/*  75 */       if (this.has3dAngularVelocity) this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation3D);
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(ParticleController particleController) {
/*  81 */     super.set(particleController);
/*  82 */     for (int k = 0; k < this.velocities.size; k++) {
/*  83 */       ((DynamicsModifier[])this.velocities.items)[k].set(particleController);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  89 */     for (int k = 0; k < this.velocities.size; k++) {
/*  90 */       ((DynamicsModifier[])this.velocities.items)[k].init();
/*     */     }
/*     */   }
/*     */   
/*     */   public void activateParticles(int startIndex, int count) {
/*  95 */     if (this.hasAcceleration)
/*     */     {
/*     */       
/*  98 */       for (int i = startIndex * this.positionChannel.strideSize, c = i + count * this.positionChannel.strideSize; i < c; i += this.positionChannel.strideSize) {
/*  99 */         this.previousPositionChannel.data[i + 0] = this.positionChannel.data[i + 0];
/* 100 */         this.previousPositionChannel.data[i + 1] = this.positionChannel.data[i + 1];
/* 101 */         this.previousPositionChannel.data[i + 2] = this.positionChannel.data[i + 2];
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (this.has2dAngularVelocity) {
/*     */       
/* 112 */       for (int i = startIndex * this.rotationChannel.strideSize, c = i + count * this.rotationChannel.strideSize; i < c; i += this.rotationChannel.strideSize) {
/* 113 */         this.rotationChannel.data[i + 0] = 1.0F;
/* 114 */         this.rotationChannel.data[i + 1] = 0.0F;
/*     */       } 
/* 116 */     } else if (this.has3dAngularVelocity) {
/*     */       
/* 118 */       for (int i = startIndex * this.rotationChannel.strideSize, c = i + count * this.rotationChannel.strideSize; i < c; i += this.rotationChannel.strideSize) {
/* 119 */         this.rotationChannel.data[i + 0] = 0.0F;
/* 120 */         this.rotationChannel.data[i + 1] = 0.0F;
/* 121 */         this.rotationChannel.data[i + 2] = 0.0F;
/* 122 */         this.rotationChannel.data[i + 3] = 1.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     for (int k = 0; k < this.velocities.size; k++) {
/* 127 */       ((DynamicsModifier[])this.velocities.items)[k].activateParticles(startIndex, count);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 133 */     if (this.hasAcceleration)
/* 134 */       Arrays.fill(this.accellerationChannel.data, 0, this.controller.particles.size * this.accellerationChannel.strideSize, 0.0F); 
/* 135 */     if (this.has2dAngularVelocity || this.has3dAngularVelocity) {
/* 136 */       Arrays.fill(this.angularVelocityChannel.data, 0, this.controller.particles.size * this.angularVelocityChannel.strideSize, 0.0F);
/*     */     }
/*     */     
/* 139 */     for (int k = 0; k < this.velocities.size; k++) {
/* 140 */       ((DynamicsModifier[])this.velocities.items)[k].update();
/*     */     }
/*     */ 
/*     */     
/* 144 */     if (this.hasAcceleration)
/*     */     {
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
/* 158 */       for (int i = 0, offset = 0; i < this.controller.particles.size; i++, offset += this.positionChannel.strideSize) {
/* 159 */         float x = this.positionChannel.data[offset + 0], y = this.positionChannel.data[offset + 1];
/* 160 */         float z = this.positionChannel.data[offset + 2];
/* 161 */         this.positionChannel.data[offset + 0] = 2.0F * x - this.previousPositionChannel.data[offset + 0] + this.accellerationChannel.data[offset + 0] * this.controller.deltaTimeSqr;
/*     */ 
/*     */         
/* 164 */         this.positionChannel.data[offset + 1] = 2.0F * y - this.previousPositionChannel.data[offset + 1] + this.accellerationChannel.data[offset + 1] * this.controller.deltaTimeSqr;
/*     */ 
/*     */         
/* 167 */         this.positionChannel.data[offset + 2] = 2.0F * z - this.previousPositionChannel.data[offset + 2] + this.accellerationChannel.data[offset + 2] * this.controller.deltaTimeSqr;
/*     */ 
/*     */         
/* 170 */         this.previousPositionChannel.data[offset + 0] = x;
/* 171 */         this.previousPositionChannel.data[offset + 1] = y;
/* 172 */         this.previousPositionChannel.data[offset + 2] = z;
/*     */       } 
/*     */     }
/*     */     
/* 176 */     if (this.has2dAngularVelocity) {
/* 177 */       for (int i = 0, offset = 0; i < this.controller.particles.size; i++, offset += this.rotationChannel.strideSize) {
/* 178 */         float rotation = this.angularVelocityChannel.data[i] * this.controller.deltaTime;
/* 179 */         if (rotation != 0.0F) {
/* 180 */           float cosBeta = MathUtils.cosDeg(rotation), sinBeta = MathUtils.sinDeg(rotation);
/* 181 */           float currentCosine = this.rotationChannel.data[offset + 0];
/* 182 */           float currentSine = this.rotationChannel.data[offset + 1];
/* 183 */           float newCosine = currentCosine * cosBeta - currentSine * sinBeta, newSine = currentSine * cosBeta + currentCosine * sinBeta;
/*     */           
/* 185 */           this.rotationChannel.data[offset + 0] = newCosine;
/* 186 */           this.rotationChannel.data[offset + 1] = newSine;
/*     */         } 
/*     */       } 
/* 189 */     } else if (this.has3dAngularVelocity) {
/* 190 */       for (int i = 0, offset = 0, angularOffset = 0; i < this.controller.particles.size; i++, offset += this.rotationChannel.strideSize, angularOffset += this.angularVelocityChannel.strideSize) {
/*     */         
/* 192 */         float wx = this.angularVelocityChannel.data[angularOffset + 0], wy = this.angularVelocityChannel.data[angularOffset + 1];
/* 193 */         float wz = this.angularVelocityChannel.data[angularOffset + 2], qx = this.rotationChannel.data[offset + 0];
/* 194 */         float qy = this.rotationChannel.data[offset + 1], qz = this.rotationChannel.data[offset + 2];
/* 195 */         float qw = this.rotationChannel.data[offset + 3];
/* 196 */         TMP_Q.set(wx, wy, wz, 0.0F).mul(qx, qy, qz, qw).mul(0.5F * this.controller.deltaTime).add(qx, qy, qz, qw).nor();
/* 197 */         this.rotationChannel.data[offset + 0] = TMP_Q.x;
/* 198 */         this.rotationChannel.data[offset + 1] = TMP_Q.y;
/* 199 */         this.rotationChannel.data[offset + 2] = TMP_Q.z;
/* 200 */         this.rotationChannel.data[offset + 3] = TMP_Q.w;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicsInfluencer copy() {
/* 207 */     return new DynamicsInfluencer(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 212 */     json.writeValue("velocities", this.velocities, Array.class, DynamicsModifier.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 217 */     this.velocities.addAll((Array)json.readValue("velocities", Array.class, DynamicsModifier.class, jsonData));
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\DynamicsInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */