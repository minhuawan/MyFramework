/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*    */ import com.badlogic.gdx.math.Vector3;
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
/*    */ 
/*    */ public abstract class SpawnShapeValue
/*    */   extends ParticleValue
/*    */   implements ResourceData.Configurable, Json.Serializable
/*    */ {
/* 32 */   public RangedNumericValue xOffsetValue = new RangedNumericValue();
/* 33 */   public RangedNumericValue yOffsetValue = new RangedNumericValue();
/* 34 */   public RangedNumericValue zOffsetValue = new RangedNumericValue();
/*    */ 
/*    */   
/*    */   public SpawnShapeValue(SpawnShapeValue spawnShapeValue) {
/* 38 */     this();
/*    */   }
/*    */   public SpawnShapeValue() {}
/*    */   public abstract void spawnAux(Vector3 paramVector3, float paramFloat);
/*    */   
/*    */   public final Vector3 spawn(Vector3 vector, float percent) {
/* 44 */     spawnAux(vector, percent);
/* 45 */     if (this.xOffsetValue.active) vector.x += this.xOffsetValue.newLowValue(); 
/* 46 */     if (this.yOffsetValue.active) vector.y += this.yOffsetValue.newLowValue(); 
/* 47 */     if (this.zOffsetValue.active) vector.z += this.zOffsetValue.newLowValue(); 
/* 48 */     return vector;
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {}
/*    */ 
/*    */   
/*    */   public void start() {}
/*    */ 
/*    */   
/*    */   public void load(ParticleValue value) {
/* 59 */     super.load(value);
/* 60 */     SpawnShapeValue shape = (SpawnShapeValue)value;
/* 61 */     this.xOffsetValue.load(shape.xOffsetValue);
/* 62 */     this.yOffsetValue.load(shape.yOffsetValue);
/* 63 */     this.zOffsetValue.load(shape.zOffsetValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract SpawnShapeValue copy();
/*    */   
/*    */   public void write(Json json) {
/* 70 */     super.write(json);
/* 71 */     json.writeValue("xOffsetValue", this.xOffsetValue);
/* 72 */     json.writeValue("yOffsetValue", this.yOffsetValue);
/* 73 */     json.writeValue("zOffsetValue", this.zOffsetValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 78 */     super.read(json, jsonData);
/* 79 */     this.xOffsetValue = (RangedNumericValue)json.readValue("xOffsetValue", RangedNumericValue.class, jsonData);
/* 80 */     this.yOffsetValue = (RangedNumericValue)json.readValue("yOffsetValue", RangedNumericValue.class, jsonData);
/* 81 */     this.zOffsetValue = (RangedNumericValue)json.readValue("zOffsetValue", RangedNumericValue.class, jsonData);
/*    */   }
/*    */   
/*    */   public void save(AssetManager manager, ResourceData data) {}
/*    */   
/*    */   public void load(AssetManager manager, ResourceData data) {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\SpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */