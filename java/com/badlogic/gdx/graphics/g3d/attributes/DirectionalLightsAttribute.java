/*    */ package com.badlogic.gdx.graphics.g3d.attributes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*    */ import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectionalLightsAttribute
/*    */   extends Attribute
/*    */ {
/*    */   public static final String Alias = "directionalLights";
/* 15 */   public static final long Type = register("directionalLights");
/*    */   
/*    */   public static final boolean is(long mask) {
/* 18 */     return ((mask & Type) == mask);
/*    */   }
/*    */   
/*    */   public final Array<DirectionalLight> lights;
/*    */   
/*    */   public DirectionalLightsAttribute() {
/* 24 */     super(Type);
/* 25 */     this.lights = new Array(1);
/*    */   }
/*    */   
/*    */   public DirectionalLightsAttribute(DirectionalLightsAttribute copyFrom) {
/* 29 */     this();
/* 30 */     this.lights.addAll(copyFrom.lights);
/*    */   }
/*    */ 
/*    */   
/*    */   public DirectionalLightsAttribute copy() {
/* 35 */     return new DirectionalLightsAttribute(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 40 */     int result = super.hashCode();
/* 41 */     for (DirectionalLight light : this.lights)
/* 42 */       result = 1229 * result + ((light == null) ? 0 : light.hashCode()); 
/* 43 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Attribute o) {
/* 48 */     if (this.type != o.type) return (this.type < o.type) ? -1 : 1; 
/* 49 */     return 0;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\DirectionalLightsAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */