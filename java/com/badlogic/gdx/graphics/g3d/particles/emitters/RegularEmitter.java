/*     */ package com.badlogic.gdx.graphics.g3d.particles.emitters;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.RangedNumericValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ 
/*     */ public class RegularEmitter
/*     */   extends Emitter implements Json.Serializable {
/*     */   public RangedNumericValue delayValue;
/*     */   public RangedNumericValue durationValue;
/*     */   public ScaledNumericValue lifeOffsetValue;
/*     */   public ScaledNumericValue lifeValue;
/*     */   public ScaledNumericValue emissionValue;
/*     */   protected int emission;
/*     */   protected int emissionDiff;
/*     */   protected int emissionDelta;
/*     */   protected int lifeOffset;
/*     */   protected int lifeOffsetDiff;
/*     */   protected int life;
/*     */   protected int lifeDiff;
/*     */   protected float duration;
/*     */   protected float delay;
/*     */   protected float durationTimer;
/*     */   protected float delayTimer;
/*     */   private boolean continuous;
/*     */   private EmissionMode emissionMode;
/*     */   private ParallelArray.FloatChannel lifeChannel;
/*     */   
/*     */   public enum EmissionMode {
/*  34 */     Enabled,
/*     */ 
/*     */     
/*  37 */     EnabledUntilCycleEnd,
/*     */     
/*  39 */     Disabled;
/*     */   }
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
/*     */   public RegularEmitter() {
/*  54 */     this.delayValue = new RangedNumericValue();
/*  55 */     this.durationValue = new RangedNumericValue();
/*  56 */     this.lifeOffsetValue = new ScaledNumericValue();
/*  57 */     this.lifeValue = new ScaledNumericValue();
/*  58 */     this.emissionValue = new ScaledNumericValue();
/*     */     
/*  60 */     this.durationValue.setActive(true);
/*  61 */     this.emissionValue.setActive(true);
/*  62 */     this.lifeValue.setActive(true);
/*  63 */     this.continuous = true;
/*  64 */     this.emissionMode = EmissionMode.Enabled;
/*     */   }
/*     */   
/*     */   public RegularEmitter(RegularEmitter regularEmitter) {
/*  68 */     this();
/*  69 */     set(regularEmitter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/*  74 */     this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  79 */     this.delay = this.delayValue.active ? this.delayValue.newLowValue() : 0.0F;
/*  80 */     this.delayTimer = 0.0F;
/*  81 */     this.durationTimer = 0.0F;
/*     */     
/*  83 */     this.duration = this.durationValue.newLowValue();
/*  84 */     this.percent = this.durationTimer / this.duration;
/*     */     
/*  86 */     this.emission = (int)this.emissionValue.newLowValue();
/*  87 */     this.emissionDiff = (int)this.emissionValue.newHighValue();
/*  88 */     if (!this.emissionValue.isRelative()) this.emissionDiff -= this.emission;
/*     */     
/*  90 */     this.life = (int)this.lifeValue.newLowValue();
/*  91 */     this.lifeDiff = (int)this.lifeValue.newHighValue();
/*  92 */     if (!this.lifeValue.isRelative()) this.lifeDiff -= this.life;
/*     */     
/*  94 */     this.lifeOffset = this.lifeOffsetValue.active ? (int)this.lifeOffsetValue.newLowValue() : 0;
/*  95 */     this.lifeOffsetDiff = (int)this.lifeOffsetValue.newHighValue();
/*  96 */     if (!this.lifeOffsetValue.isRelative()) this.lifeOffsetDiff -= this.lifeOffset; 
/*     */   }
/*     */   
/*     */   public void init() {
/* 100 */     super.init();
/* 101 */     this.emissionDelta = 0;
/* 102 */     this.durationTimer = this.duration;
/*     */   }
/*     */   
/*     */   public void activateParticles(int startIndex, int count) {
/* 106 */     int currentTotaLife = this.life + (int)(this.lifeDiff * this.lifeValue.getScale(this.percent)), currentLife = currentTotaLife;
/* 107 */     int offsetTime = (int)(this.lifeOffset + this.lifeOffsetDiff * this.lifeOffsetValue.getScale(this.percent));
/* 108 */     if (offsetTime > 0) {
/* 109 */       if (offsetTime >= currentLife) offsetTime = currentLife - 1; 
/* 110 */       currentLife -= offsetTime;
/*     */     } 
/* 112 */     float lifePercent = 1.0F - currentLife / currentTotaLife;
/*     */     int i, c;
/* 114 */     for (i = startIndex * this.lifeChannel.strideSize, c = i + count * this.lifeChannel.strideSize; i < c; i += this.lifeChannel.strideSize) {
/* 115 */       this.lifeChannel.data[i + 0] = currentLife;
/* 116 */       this.lifeChannel.data[i + 1] = currentTotaLife;
/* 117 */       this.lifeChannel.data[i + 2] = lifePercent;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void update() {
/* 122 */     int deltaMillis = (int)(this.controller.deltaTime * 1000.0F);
/*     */     
/* 124 */     if (this.delayTimer < this.delay) {
/* 125 */       this.delayTimer += deltaMillis;
/*     */     } else {
/* 127 */       boolean emit = (this.emissionMode != EmissionMode.Disabled);
/*     */       
/* 129 */       if (this.durationTimer < this.duration) {
/* 130 */         this.durationTimer += deltaMillis;
/* 131 */         this.percent = this.durationTimer / this.duration;
/*     */       }
/* 133 */       else if (this.continuous && emit && this.emissionMode == EmissionMode.Enabled) {
/* 134 */         this.controller.start();
/*     */       } else {
/* 136 */         emit = false;
/*     */       } 
/*     */       
/* 139 */       if (emit) {
/*     */         
/* 141 */         this.emissionDelta += deltaMillis;
/* 142 */         float emissionTime = this.emission + this.emissionDiff * this.emissionValue.getScale(this.percent);
/* 143 */         if (emissionTime > 0.0F) {
/* 144 */           emissionTime = 1000.0F / emissionTime;
/* 145 */           if (this.emissionDelta >= emissionTime) {
/* 146 */             int emitCount = (int)(this.emissionDelta / emissionTime);
/* 147 */             emitCount = Math.min(emitCount, this.maxParticleCount - this.controller.particles.size);
/* 148 */             this.emissionDelta = (int)(this.emissionDelta - emitCount * emissionTime);
/* 149 */             this.emissionDelta = (int)(this.emissionDelta % emissionTime);
/* 150 */             addParticles(emitCount);
/*     */           } 
/*     */         } 
/* 153 */         if (this.controller.particles.size < this.minParticleCount) addParticles(this.minParticleCount - this.controller.particles.size);
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     int activeParticles = this.controller.particles.size; int k;
/* 159 */     for (int i = 0; i < this.controller.particles.size; ) {
/* 160 */       this.lifeChannel.data[k + 0] = this.lifeChannel.data[k + 0] - deltaMillis; if (this.lifeChannel.data[k + 0] - deltaMillis <= 0.0F) {
/* 161 */         this.controller.particles.removeElement(i);
/*     */         continue;
/*     */       } 
/* 164 */       this.lifeChannel.data[k + 2] = 1.0F - this.lifeChannel.data[k + 0] / this.lifeChannel.data[k + 1];
/*     */ 
/*     */ 
/*     */       
/* 168 */       i++;
/* 169 */       k += this.lifeChannel.strideSize;
/*     */     } 
/*     */     
/* 172 */     if (this.controller.particles.size < activeParticles) {
/* 173 */       this.controller.killParticles(this.controller.particles.size, activeParticles - this.controller.particles.size);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addParticles(int count) {
/* 178 */     count = Math.min(count, this.maxParticleCount - this.controller.particles.size);
/* 179 */     if (count <= 0)
/* 180 */       return;  this.controller.activateParticles(this.controller.particles.size, count);
/* 181 */     this.controller.particles.size += count;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getLife() {
/* 185 */     return this.lifeValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getEmission() {
/* 189 */     return this.emissionValue;
/*     */   }
/*     */   
/*     */   public RangedNumericValue getDuration() {
/* 193 */     return this.durationValue;
/*     */   }
/*     */   
/*     */   public RangedNumericValue getDelay() {
/* 197 */     return this.delayValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getLifeOffset() {
/* 201 */     return this.lifeOffsetValue;
/*     */   }
/*     */   
/*     */   public boolean isContinuous() {
/* 205 */     return this.continuous;
/*     */   }
/*     */   
/*     */   public void setContinuous(boolean continuous) {
/* 209 */     this.continuous = continuous;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EmissionMode getEmissionMode() {
/* 215 */     return this.emissionMode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmissionMode(EmissionMode emissionMode) {
/* 221 */     this.emissionMode = emissionMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 226 */     if (this.delayTimer < this.delay) return false; 
/* 227 */     return (this.durationTimer >= this.duration && this.controller.particles.size == 0);
/*     */   }
/*     */   
/*     */   public float getPercentComplete() {
/* 231 */     if (this.delayTimer < this.delay) return 0.0F; 
/* 232 */     return Math.min(1.0F, this.durationTimer / this.duration);
/*     */   }
/*     */   
/*     */   public void set(RegularEmitter emitter) {
/* 236 */     set(emitter);
/* 237 */     this.delayValue.load(emitter.delayValue);
/* 238 */     this.durationValue.load(emitter.durationValue);
/* 239 */     this.lifeOffsetValue.load(emitter.lifeOffsetValue);
/* 240 */     this.lifeValue.load(emitter.lifeValue);
/* 241 */     this.emissionValue.load(emitter.emissionValue);
/* 242 */     this.emission = emitter.emission;
/* 243 */     this.emissionDiff = emitter.emissionDiff;
/* 244 */     this.emissionDelta = emitter.emissionDelta;
/* 245 */     this.lifeOffset = emitter.lifeOffset;
/* 246 */     this.lifeOffsetDiff = emitter.lifeOffsetDiff;
/* 247 */     this.life = emitter.life;
/* 248 */     this.lifeDiff = emitter.lifeDiff;
/* 249 */     this.duration = emitter.duration;
/* 250 */     this.delay = emitter.delay;
/* 251 */     this.durationTimer = emitter.durationTimer;
/* 252 */     this.delayTimer = emitter.delayTimer;
/* 253 */     this.continuous = emitter.continuous;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleControllerComponent copy() {
/* 258 */     return new RegularEmitter(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 263 */     super.write(json);
/* 264 */     json.writeValue("continous", Boolean.valueOf(this.continuous));
/* 265 */     json.writeValue("emission", this.emissionValue);
/* 266 */     json.writeValue("delay", this.delayValue);
/* 267 */     json.writeValue("duration", this.durationValue);
/* 268 */     json.writeValue("life", this.lifeValue);
/* 269 */     json.writeValue("lifeOffset", this.lifeOffsetValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 274 */     super.read(json, jsonData);
/* 275 */     this.continuous = ((Boolean)json.readValue("continous", boolean.class, jsonData)).booleanValue();
/* 276 */     this.emissionValue = (ScaledNumericValue)json.readValue("emission", ScaledNumericValue.class, jsonData);
/* 277 */     this.delayValue = (RangedNumericValue)json.readValue("delay", RangedNumericValue.class, jsonData);
/* 278 */     this.durationValue = (RangedNumericValue)json.readValue("duration", RangedNumericValue.class, jsonData);
/* 279 */     this.lifeValue = (ScaledNumericValue)json.readValue("life", ScaledNumericValue.class, jsonData);
/* 280 */     this.lifeOffsetValue = (ScaledNumericValue)json.readValue("lifeOffset", ScaledNumericValue.class, jsonData);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\emitters\RegularEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */