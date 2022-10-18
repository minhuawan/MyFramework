/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.MathUtils;
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
/*    */ public class RangedNumericValue
/*    */   extends ParticleValue
/*    */ {
/*    */   private float lowMin;
/*    */   private float lowMax;
/*    */   
/*    */   public float newLowValue() {
/* 29 */     return this.lowMin + (this.lowMax - this.lowMin) * MathUtils.random();
/*    */   }
/*    */   
/*    */   public void setLow(float value) {
/* 33 */     this.lowMin = value;
/* 34 */     this.lowMax = value;
/*    */   }
/*    */   
/*    */   public void setLow(float min, float max) {
/* 38 */     this.lowMin = min;
/* 39 */     this.lowMax = max;
/*    */   }
/*    */   
/*    */   public float getLowMin() {
/* 43 */     return this.lowMin;
/*    */   }
/*    */   
/*    */   public void setLowMin(float lowMin) {
/* 47 */     this.lowMin = lowMin;
/*    */   }
/*    */   
/*    */   public float getLowMax() {
/* 51 */     return this.lowMax;
/*    */   }
/*    */   
/*    */   public void setLowMax(float lowMax) {
/* 55 */     this.lowMax = lowMax;
/*    */   }
/*    */   
/*    */   public void load(RangedNumericValue value) {
/* 59 */     load(value);
/* 60 */     this.lowMax = value.lowMax;
/* 61 */     this.lowMin = value.lowMin;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 66 */     super.write(json);
/* 67 */     json.writeValue("lowMin", Float.valueOf(this.lowMin));
/* 68 */     json.writeValue("lowMax", Float.valueOf(this.lowMax));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 73 */     super.read(json, jsonData);
/* 74 */     this.lowMin = ((Float)json.readValue("lowMin", float.class, jsonData)).floatValue();
/* 75 */     this.lowMax = ((Float)json.readValue("lowMax", float.class, jsonData)).floatValue();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\RangedNumericValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */