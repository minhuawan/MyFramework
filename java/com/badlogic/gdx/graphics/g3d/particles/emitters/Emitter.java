/*    */ package com.badlogic.gdx.graphics.g3d.particles.emitters;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
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
/*    */ 
/*    */ public abstract class Emitter
/*    */   extends ParticleControllerComponent
/*    */   implements Json.Serializable
/*    */ {
/*    */   public int minParticleCount;
/* 30 */   public int maxParticleCount = 4;
/*    */   
/*    */   public float percent;
/*    */ 
/*    */   
/*    */   public Emitter(Emitter regularEmitter) {
/* 36 */     set(regularEmitter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Emitter() {}
/*    */ 
/*    */   
/*    */   public void init() {
/* 44 */     this.controller.particles.size = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {
/* 49 */     this.controller.particles.size = 0;
/*    */   }
/*    */   
/*    */   public boolean isComplete() {
/* 53 */     return (this.percent >= 1.0F);
/*    */   }
/*    */   
/*    */   public int getMinParticleCount() {
/* 57 */     return this.minParticleCount;
/*    */   }
/*    */   
/*    */   public void setMinParticleCount(int minParticleCount) {
/* 61 */     this.minParticleCount = minParticleCount;
/*    */   }
/*    */   
/*    */   public int getMaxParticleCount() {
/* 65 */     return this.maxParticleCount;
/*    */   }
/*    */   
/*    */   public void setMaxParticleCount(int maxParticleCount) {
/* 69 */     this.maxParticleCount = maxParticleCount;
/*    */   }
/*    */   
/*    */   public void setParticleCount(int aMin, int aMax) {
/* 73 */     setMinParticleCount(aMin);
/* 74 */     setMaxParticleCount(aMax);
/*    */   }
/*    */   
/*    */   public void set(Emitter emitter) {
/* 78 */     this.minParticleCount = emitter.minParticleCount;
/* 79 */     this.maxParticleCount = emitter.maxParticleCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 84 */     json.writeValue("minParticleCount", Integer.valueOf(this.minParticleCount));
/* 85 */     json.writeValue("maxParticleCount", Integer.valueOf(this.maxParticleCount));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 90 */     this.minParticleCount = ((Integer)json.readValue("minParticleCount", int.class, jsonData)).intValue();
/* 91 */     this.maxParticleCount = ((Integer)json.readValue("maxParticleCount", int.class, jsonData)).intValue();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\emitters\Emitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */