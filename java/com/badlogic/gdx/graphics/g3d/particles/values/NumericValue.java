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
/*    */ public class NumericValue
/*    */   extends ParticleValue
/*    */ {
/*    */   private float value;
/*    */   
/*    */   public float getValue() {
/* 28 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 32 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void load(NumericValue value) {
/* 36 */     load(value);
/* 37 */     this.value = value.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 42 */     super.write(json);
/* 43 */     json.writeValue("value", Float.valueOf(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 48 */     super.read(json, jsonData);
/* 49 */     this.value = ((Float)json.readValue("value", float.class, jsonData)).floatValue();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\NumericValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */