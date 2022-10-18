/*    */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.PointSpawnShapeValue;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.SpawnShapeValue;
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
/*    */ public class SpawnInfluencer
/*    */   extends Influencer
/*    */ {
/*    */   public SpawnShapeValue spawnShapeValue;
/*    */   ParallelArray.FloatChannel positionChannel;
/*    */   
/*    */   public SpawnInfluencer() {
/* 36 */     this.spawnShapeValue = (SpawnShapeValue)new PointSpawnShapeValue();
/*    */   }
/*    */   
/*    */   public SpawnInfluencer(SpawnShapeValue spawnShapeValue) {
/* 40 */     this.spawnShapeValue = spawnShapeValue;
/*    */   }
/*    */   
/*    */   public SpawnInfluencer(SpawnInfluencer source) {
/* 44 */     this.spawnShapeValue = source.spawnShapeValue.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 49 */     this.spawnShapeValue.init();
/*    */   }
/*    */ 
/*    */   
/*    */   public void allocateChannels() {
/* 54 */     this.positionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Position);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 59 */     this.spawnShapeValue.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public void activateParticles(int startIndex, int count) {
/* 64 */     for (int i = startIndex * this.positionChannel.strideSize, c = i + count * this.positionChannel.strideSize; i < c; i += this.positionChannel.strideSize) {
/* 65 */       this.spawnShapeValue.spawn(TMP_V1, this.controller.emitter.percent);
/* 66 */       TMP_V1.mul(this.controller.transform);
/* 67 */       this.positionChannel.data[i + 0] = TMP_V1.x;
/* 68 */       this.positionChannel.data[i + 1] = TMP_V1.y;
/* 69 */       this.positionChannel.data[i + 2] = TMP_V1.z;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnInfluencer copy() {
/* 75 */     return new SpawnInfluencer(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 80 */     json.writeValue("spawnShape", this.spawnShapeValue, SpawnShapeValue.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 85 */     this.spawnShapeValue = (SpawnShapeValue)json.readValue("spawnShape", SpawnShapeValue.class, jsonData);
/*    */   }
/*    */ 
/*    */   
/*    */   public void save(AssetManager manager, ResourceData data) {
/* 90 */     this.spawnShapeValue.save(manager, data);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(AssetManager manager, ResourceData data) {
/* 95 */     this.spawnShapeValue.load(manager, data);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\SpawnInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */