/*    */ package com.badlogic.gdx.graphics.g3d.attributes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Attribute;
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
/*    */ public class IntAttribute
/*    */   extends Attribute
/*    */ {
/*    */   public static final String CullFaceAlias = "cullface";
/* 23 */   public static final long CullFace = register("cullface");
/*    */   
/*    */   public static IntAttribute createCullFace(int value) {
/* 26 */     return new IntAttribute(CullFace, value);
/*    */   }
/*    */   
/*    */   public int value;
/*    */   
/*    */   public IntAttribute(long type) {
/* 32 */     super(type);
/*    */   }
/*    */   
/*    */   public IntAttribute(long type, int value) {
/* 36 */     super(type);
/* 37 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Attribute copy() {
/* 42 */     return new IntAttribute(this.type, this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 47 */     int result = super.hashCode();
/* 48 */     result = 983 * result + this.value;
/* 49 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Attribute o) {
/* 54 */     if (this.type != o.type) return (int)(this.type - o.type); 
/* 55 */     return this.value - ((IntAttribute)o).value;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\IntAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */