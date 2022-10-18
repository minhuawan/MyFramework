/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DynamicsModifier
/*     */   extends Influencer
/*     */ {
/*  35 */   protected static final Vector3 TMP_V1 = new Vector3(); protected static final Vector3 TMP_V2 = new Vector3(); protected static final Vector3 TMP_V3 = new Vector3();
/*  36 */   protected static final Quaternion TMP_Q = new Quaternion();
/*     */   
/*     */   public static class FaceDirection extends DynamicsModifier {
/*     */     ParallelArray.FloatChannel rotationChannel;
/*     */     ParallelArray.FloatChannel accellerationChannel;
/*     */     
/*     */     public FaceDirection() {}
/*     */     
/*     */     public FaceDirection(FaceDirection rotation) {
/*  45 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/*  50 */       this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation3D);
/*  51 */       this.accellerationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/*  56 */       for (int i = 0, accelOffset = 0, c = i + this.controller.particles.size * this.rotationChannel.strideSize; i < c; i += this.rotationChannel.strideSize, accelOffset += this.accellerationChannel.strideSize) {
/*     */ 
/*     */ 
/*     */         
/*  60 */         Vector3 axisZ = TMP_V1.set(this.accellerationChannel.data[accelOffset + 0], this.accellerationChannel.data[accelOffset + 1], this.accellerationChannel.data[accelOffset + 2]).nor();
/*  61 */         Vector3 axisY = TMP_V2.set(TMP_V1).crs(Vector3.Y).nor().crs(TMP_V1).nor(), axisX = TMP_V3.set(axisY).crs(axisZ).nor();
/*  62 */         TMP_Q.setFromAxes(false, axisX.x, axisY.x, axisZ.x, axisX.y, axisY.y, axisZ.y, axisX.z, axisY.z, axisZ.z);
/*  63 */         this.rotationChannel.data[i + 0] = TMP_Q.x;
/*  64 */         this.rotationChannel.data[i + 1] = TMP_Q.y;
/*  65 */         this.rotationChannel.data[i + 2] = TMP_Q.z;
/*  66 */         this.rotationChannel.data[i + 3] = TMP_Q.w;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ParticleControllerComponent copy() {
/*  72 */       return new FaceDirection(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Strength extends DynamicsModifier {
/*     */     protected ParallelArray.FloatChannel strengthChannel;
/*     */     public ScaledNumericValue strengthValue;
/*     */     
/*     */     public Strength() {
/*  81 */       this.strengthValue = new ScaledNumericValue();
/*     */     }
/*     */     
/*     */     public Strength(Strength rotation) {
/*  85 */       super(rotation);
/*  86 */       this.strengthValue = new ScaledNumericValue();
/*  87 */       this.strengthValue.load(rotation.strengthValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/*  92 */       super.allocateChannels();
/*  93 */       ParticleChannels.Interpolation.id = this.controller.particleChannels.newId();
/*  94 */       this.strengthChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation);
/*     */     }
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*     */       int i;
/*     */       int c;
/* 100 */       for (i = startIndex * this.strengthChannel.strideSize, c = i + count * this.strengthChannel.strideSize; i < c; i += this.strengthChannel.strideSize) {
/* 101 */         float start = this.strengthValue.newLowValue();
/* 102 */         float diff = this.strengthValue.newHighValue();
/* 103 */         if (!this.strengthValue.isRelative()) diff -= start; 
/* 104 */         this.strengthChannel.data[i + 0] = start;
/* 105 */         this.strengthChannel.data[i + 1] = diff;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/* 111 */       super.write(json);
/* 112 */       json.writeValue("strengthValue", this.strengthValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 117 */       super.read(json, jsonData);
/* 118 */       this.strengthValue = (ScaledNumericValue)json.readValue("strengthValue", ScaledNumericValue.class, jsonData);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Angular
/*     */     extends Strength
/*     */   {
/*     */     protected ParallelArray.FloatChannel angularChannel;
/*     */     public ScaledNumericValue thetaValue;
/*     */     public ScaledNumericValue phiValue;
/*     */     
/*     */     public Angular() {
/* 130 */       this.thetaValue = new ScaledNumericValue();
/* 131 */       this.phiValue = new ScaledNumericValue();
/*     */     }
/*     */     
/*     */     public Angular(Angular value) {
/* 135 */       super(value);
/* 136 */       this.thetaValue = new ScaledNumericValue();
/* 137 */       this.phiValue = new ScaledNumericValue();
/* 138 */       this.thetaValue.load(value.thetaValue);
/* 139 */       this.phiValue.load(value.phiValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 144 */       super.allocateChannels();
/* 145 */       ParticleChannels.Interpolation4.id = this.controller.particleChannels.newId();
/* 146 */       this.angularChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Interpolation4);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/* 151 */       super.activateParticles(startIndex, count);
/*     */       int i, c;
/* 153 */       for (i = startIndex * this.angularChannel.strideSize, c = i + count * this.angularChannel.strideSize; i < c; i += this.angularChannel.strideSize) {
/*     */ 
/*     */         
/* 156 */         float start = this.thetaValue.newLowValue();
/* 157 */         float diff = this.thetaValue.newHighValue();
/* 158 */         if (!this.thetaValue.isRelative()) diff -= start; 
/* 159 */         this.angularChannel.data[i + 0] = start;
/* 160 */         this.angularChannel.data[i + 1] = diff;
/*     */ 
/*     */         
/* 163 */         start = this.phiValue.newLowValue();
/* 164 */         diff = this.phiValue.newHighValue();
/* 165 */         if (!this.phiValue.isRelative()) diff -= start; 
/* 166 */         this.angularChannel.data[i + 2] = start;
/* 167 */         this.angularChannel.data[i + 3] = diff;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/* 173 */       super.write(json);
/* 174 */       json.writeValue("thetaValue", this.thetaValue);
/* 175 */       json.writeValue("phiValue", this.phiValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 180 */       super.read(json, jsonData);
/* 181 */       this.thetaValue = (ScaledNumericValue)json.readValue("thetaValue", ScaledNumericValue.class, jsonData);
/* 182 */       this.phiValue = (ScaledNumericValue)json.readValue("phiValue", ScaledNumericValue.class, jsonData);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Rotational2D
/*     */     extends Strength {
/*     */     ParallelArray.FloatChannel rotationalVelocity2dChannel;
/*     */     
/*     */     public Rotational2D() {}
/*     */     
/*     */     public Rotational2D(Rotational2D rotation) {
/* 193 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 198 */       super.allocateChannels();
/* 199 */       this.rotationalVelocity2dChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.AngularVelocity2D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 204 */       int i = 0, l = 2, s = 0, c = i + this.controller.particles.size * this.rotationalVelocity2dChannel.strideSize;
/* 205 */       for (; i < c; s += this.strengthChannel.strideSize, i += this.rotationalVelocity2dChannel.strideSize, l += this.lifeChannel.strideSize) {
/* 206 */         this.rotationalVelocity2dChannel.data[i] = this.rotationalVelocity2dChannel.data[i] + this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue
/*     */           
/* 208 */           .getScale(this.lifeChannel.data[l]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Rotational2D copy() {
/* 214 */       return new Rotational2D(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Rotational3D extends Angular {
/*     */     ParallelArray.FloatChannel rotationChannel;
/*     */     ParallelArray.FloatChannel rotationalForceChannel;
/*     */     
/*     */     public Rotational3D() {}
/*     */     
/*     */     public Rotational3D(Rotational3D rotation) {
/* 225 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 230 */       super.allocateChannels();
/* 231 */       this.rotationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Rotation3D);
/* 232 */       this.rotationalForceChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.AngularVelocity3D);
/*     */     }
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
/*     */     public void update() {
/* 263 */       int i = 0, l = 2, s = 0, a = 0, c = this.controller.particles.size * this.rotationalForceChannel.strideSize;
/* 264 */       for (; i < c; s += this.strengthChannel.strideSize, i += this.rotationalForceChannel.strideSize, a += this.angularChannel.strideSize, l += this.lifeChannel.strideSize) {
/*     */         
/* 266 */         float lifePercent = this.lifeChannel.data[l];
/*     */         
/* 268 */         float strength = this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue.getScale(lifePercent);
/*     */         
/* 270 */         float phi = this.angularChannel.data[a + 2] + this.angularChannel.data[a + 3] * this.phiValue.getScale(lifePercent);
/*     */         
/* 272 */         float theta = this.angularChannel.data[a + 0] + this.angularChannel.data[a + 1] * this.thetaValue.getScale(lifePercent);
/*     */         
/* 274 */         float cosTheta = MathUtils.cosDeg(theta), sinTheta = MathUtils.sinDeg(theta), cosPhi = MathUtils.cosDeg(phi);
/* 275 */         float sinPhi = MathUtils.sinDeg(phi);
/*     */         
/* 277 */         TMP_V3.set(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi);
/* 278 */         TMP_V3.scl(strength * 0.017453292F);
/*     */         
/* 280 */         this.rotationalForceChannel.data[i + 0] = this.rotationalForceChannel.data[i + 0] + TMP_V3.x;
/* 281 */         this.rotationalForceChannel.data[i + 1] = this.rotationalForceChannel.data[i + 1] + TMP_V3.y;
/* 282 */         this.rotationalForceChannel.data[i + 2] = this.rotationalForceChannel.data[i + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Rotational3D copy() {
/* 288 */       return new Rotational3D(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CentripetalAcceleration
/*     */     extends Strength {
/*     */     ParallelArray.FloatChannel accelerationChannel;
/*     */     ParallelArray.FloatChannel positionChannel;
/*     */     
/*     */     public CentripetalAcceleration() {}
/*     */     
/*     */     public CentripetalAcceleration(CentripetalAcceleration rotation) {
/* 300 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 305 */       super.allocateChannels();
/* 306 */       this.accelerationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/* 307 */       this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 312 */       float cx = 0.0F, cy = 0.0F, cz = 0.0F;
/* 313 */       if (!this.isGlobal) {
/* 314 */         float[] val = this.controller.transform.val;
/* 315 */         cx = val[12];
/* 316 */         cy = val[13];
/* 317 */         cz = val[14];
/*     */       } 
/*     */       
/* 320 */       int lifeOffset = 2, strengthOffset = 0, positionOffset = 0, forceOffset = 0;
/* 321 */       for (int i = 0, c = this.controller.particles.size; i < c; i++, positionOffset += this.positionChannel.strideSize, strengthOffset += this.strengthChannel.strideSize, forceOffset += this.accelerationChannel.strideSize, lifeOffset += this.lifeChannel.strideSize) {
/*     */ 
/*     */ 
/*     */         
/* 325 */         float strength = this.strengthChannel.data[strengthOffset + 0] + this.strengthChannel.data[strengthOffset + 1] * this.strengthValue.getScale(this.lifeChannel.data[lifeOffset]);
/* 326 */         TMP_V3
/* 327 */           .set(this.positionChannel.data[positionOffset + 0] - cx, this.positionChannel.data[positionOffset + 1] - cy, this.positionChannel.data[positionOffset + 2] - cz)
/*     */           
/* 329 */           .nor().scl(strength);
/* 330 */         this.accelerationChannel.data[forceOffset + 0] = this.accelerationChannel.data[forceOffset + 0] + TMP_V3.x;
/* 331 */         this.accelerationChannel.data[forceOffset + 1] = this.accelerationChannel.data[forceOffset + 1] + TMP_V3.y;
/* 332 */         this.accelerationChannel.data[forceOffset + 2] = this.accelerationChannel.data[forceOffset + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public CentripetalAcceleration copy() {
/* 338 */       return new CentripetalAcceleration(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PolarAcceleration
/*     */     extends Angular {
/*     */     ParallelArray.FloatChannel directionalVelocityChannel;
/*     */     
/*     */     public PolarAcceleration() {}
/*     */     
/*     */     public PolarAcceleration(PolarAcceleration rotation) {
/* 349 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 354 */       super.allocateChannels();
/* 355 */       this.directionalVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 360 */       int i = 0, l = 2, s = 0, a = 0, c = i + this.controller.particles.size * this.directionalVelocityChannel.strideSize;
/* 361 */       for (; i < c; s += this.strengthChannel.strideSize, i += this.directionalVelocityChannel.strideSize, a += this.angularChannel.strideSize, l += this.lifeChannel.strideSize) {
/*     */         
/* 363 */         float lifePercent = this.lifeChannel.data[l];
/*     */         
/* 365 */         float strength = this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue.getScale(lifePercent);
/*     */         
/* 367 */         float phi = this.angularChannel.data[a + 2] + this.angularChannel.data[a + 3] * this.phiValue.getScale(lifePercent);
/*     */         
/* 369 */         float theta = this.angularChannel.data[a + 0] + this.angularChannel.data[a + 1] * this.thetaValue.getScale(lifePercent);
/*     */         
/* 371 */         float cosTheta = MathUtils.cosDeg(theta), sinTheta = MathUtils.sinDeg(theta), cosPhi = MathUtils.cosDeg(phi);
/* 372 */         float sinPhi = MathUtils.sinDeg(phi);
/* 373 */         TMP_V3.set(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi).nor().scl(strength);
/* 374 */         this.directionalVelocityChannel.data[i + 0] = this.directionalVelocityChannel.data[i + 0] + TMP_V3.x;
/* 375 */         this.directionalVelocityChannel.data[i + 1] = this.directionalVelocityChannel.data[i + 1] + TMP_V3.y;
/* 376 */         this.directionalVelocityChannel.data[i + 2] = this.directionalVelocityChannel.data[i + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public PolarAcceleration copy() {
/* 382 */       return new PolarAcceleration(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TangentialAcceleration extends Angular {
/*     */     ParallelArray.FloatChannel directionalVelocityChannel;
/*     */     ParallelArray.FloatChannel positionChannel;
/*     */     
/*     */     public TangentialAcceleration() {}
/*     */     
/*     */     public TangentialAcceleration(TangentialAcceleration rotation) {
/* 393 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 398 */       super.allocateChannels();
/* 399 */       this.directionalVelocityChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/* 400 */       this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 405 */       int i = 0, l = 2, s = 0, a = 0, positionOffset = 0, c = i + this.controller.particles.size * this.directionalVelocityChannel.strideSize;
/* 406 */       for (; i < c; s += this.strengthChannel.strideSize, i += this.directionalVelocityChannel.strideSize, a += this.angularChannel.strideSize, l += this.lifeChannel.strideSize, positionOffset += this.positionChannel.strideSize) {
/*     */         
/* 408 */         float lifePercent = this.lifeChannel.data[l];
/*     */         
/* 410 */         float strength = this.strengthChannel.data[s + 0] + this.strengthChannel.data[s + 1] * this.strengthValue.getScale(lifePercent);
/*     */         
/* 412 */         float phi = this.angularChannel.data[a + 2] + this.angularChannel.data[a + 3] * this.phiValue.getScale(lifePercent);
/*     */         
/* 414 */         float theta = this.angularChannel.data[a + 0] + this.angularChannel.data[a + 1] * this.thetaValue.getScale(lifePercent);
/*     */         
/* 416 */         float cosTheta = MathUtils.cosDeg(theta), sinTheta = MathUtils.sinDeg(theta), cosPhi = MathUtils.cosDeg(phi);
/* 417 */         float sinPhi = MathUtils.sinDeg(phi);
/* 418 */         TMP_V3
/* 419 */           .set(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi)
/* 420 */           .crs(this.positionChannel.data[positionOffset + 0], this.positionChannel.data[positionOffset + 1], this.positionChannel.data[positionOffset + 2])
/*     */           
/* 422 */           .nor().scl(strength);
/* 423 */         this.directionalVelocityChannel.data[i + 0] = this.directionalVelocityChannel.data[i + 0] + TMP_V3.x;
/* 424 */         this.directionalVelocityChannel.data[i + 1] = this.directionalVelocityChannel.data[i + 1] + TMP_V3.y;
/* 425 */         this.directionalVelocityChannel.data[i + 2] = this.directionalVelocityChannel.data[i + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public TangentialAcceleration copy() {
/* 431 */       return new TangentialAcceleration(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BrownianAcceleration
/*     */     extends Strength {
/*     */     ParallelArray.FloatChannel accelerationChannel;
/*     */     
/*     */     public BrownianAcceleration() {}
/*     */     
/*     */     public BrownianAcceleration(BrownianAcceleration rotation) {
/* 442 */       super(rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 447 */       super.allocateChannels();
/* 448 */       this.accelerationChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Acceleration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 453 */       int lifeOffset = 2, strengthOffset = 0, forceOffset = 0;
/* 454 */       for (int i = 0, c = this.controller.particles.size; i < c; i++, strengthOffset += this.strengthChannel.strideSize, forceOffset += this.accelerationChannel.strideSize, lifeOffset += this.lifeChannel.strideSize) {
/*     */ 
/*     */ 
/*     */         
/* 458 */         float strength = this.strengthChannel.data[strengthOffset + 0] + this.strengthChannel.data[strengthOffset + 1] * this.strengthValue.getScale(this.lifeChannel.data[lifeOffset]);
/* 459 */         TMP_V3.set(MathUtils.random(-1.0F, 1.0F), MathUtils.random(-1.0F, 1.0F), MathUtils.random(-1.0F, 1.0F)).nor().scl(strength);
/* 460 */         this.accelerationChannel.data[forceOffset + 0] = this.accelerationChannel.data[forceOffset + 0] + TMP_V3.x;
/* 461 */         this.accelerationChannel.data[forceOffset + 1] = this.accelerationChannel.data[forceOffset + 1] + TMP_V3.y;
/* 462 */         this.accelerationChannel.data[forceOffset + 2] = this.accelerationChannel.data[forceOffset + 2] + TMP_V3.z;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public BrownianAcceleration copy() {
/* 468 */       return new BrownianAcceleration(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isGlobal = false;
/*     */   
/*     */   protected ParallelArray.FloatChannel lifeChannel;
/*     */   
/*     */   public DynamicsModifier() {}
/*     */   
/*     */   public DynamicsModifier(DynamicsModifier modifier) {
/* 479 */     this.isGlobal = modifier.isGlobal;
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 484 */     this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 489 */     super.write(json);
/* 490 */     json.writeValue("isGlobal", Boolean.valueOf(this.isGlobal));
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 495 */     super.read(json, jsonData);
/* 496 */     this.isGlobal = ((Boolean)json.readValue("isGlobal", boolean.class, jsonData)).booleanValue();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\DynamicsModifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */