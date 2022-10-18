/*    */ package com.badlogic.gdx.graphics.g3d.attributes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.utils.NumberUtils;
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
/*    */ public class FloatAttribute
/*    */   extends Attribute
/*    */ {
/*    */   public static final String ShininessAlias = "shininess";
/* 25 */   public static final long Shininess = register("shininess");
/*    */   
/*    */   public static FloatAttribute createShininess(float value) {
/* 28 */     return new FloatAttribute(Shininess, value);
/*    */   }
/*    */   
/*    */   public static final String AlphaTestAlias = "alphaTest";
/* 32 */   public static final long AlphaTest = register("alphaTest");
/*    */   
/*    */   public static FloatAttribute createAlphaTest(float value) {
/* 35 */     return new FloatAttribute(AlphaTest, value);
/*    */   }
/*    */   
/*    */   public float value;
/*    */   
/*    */   public FloatAttribute(long type) {
/* 41 */     super(type);
/*    */   }
/*    */   
/*    */   public FloatAttribute(long type, float value) {
/* 45 */     super(type);
/* 46 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Attribute copy() {
/* 51 */     return new FloatAttribute(this.type, this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     int result = super.hashCode();
/* 57 */     result = 977 * result + NumberUtils.floatToRawIntBits(this.value);
/* 58 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Attribute o) {
/* 63 */     if (this.type != o.type) return (int)(this.type - o.type); 
/* 64 */     float v = ((FloatAttribute)o).value;
/* 65 */     return MathUtils.isEqual(this.value, v) ? 0 : ((this.value < v) ? -1 : 1);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\FloatAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */