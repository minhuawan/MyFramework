/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
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
/*    */ public class ParticleValue
/*    */   implements Json.Serializable
/*    */ {
/*    */   public boolean active;
/*    */   
/*    */   public ParticleValue() {}
/*    */   
/*    */   public ParticleValue(ParticleValue value) {
/* 32 */     this.active = value.active;
/*    */   }
/*    */   
/*    */   public boolean isActive() {
/* 36 */     return this.active;
/*    */   }
/*    */   
/*    */   public void setActive(boolean active) {
/* 40 */     this.active = active;
/*    */   }
/*    */   
/*    */   public void load(ParticleValue value) {
/* 44 */     this.active = value.active;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 49 */     json.writeValue("active", Boolean.valueOf(this.active));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 54 */     this.active = ((Boolean)json.readValue("active", Boolean.class, jsonData)).booleanValue();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\ParticleValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */