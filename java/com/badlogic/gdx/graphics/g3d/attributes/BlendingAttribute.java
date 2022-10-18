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
/*    */ 
/*    */ public class BlendingAttribute
/*    */   extends Attribute
/*    */ {
/*    */   public static final String Alias = "blended";
/* 26 */   public static final long Type = register("blended");
/*    */   
/*    */   public static final boolean is(long mask) {
/* 29 */     return ((mask & Type) == mask);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean blended;
/*    */ 
/*    */   
/*    */   public int sourceFunction;
/*    */   
/*    */   public int destFunction;
/*    */   
/* 41 */   public float opacity = 1.0F;
/*    */   
/*    */   public BlendingAttribute() {
/* 44 */     this((BlendingAttribute)null);
/*    */   }
/*    */   
/*    */   public BlendingAttribute(boolean blended, int sourceFunc, int destFunc, float opacity) {
/* 48 */     super(Type);
/* 49 */     this.blended = blended;
/* 50 */     this.sourceFunction = sourceFunc;
/* 51 */     this.destFunction = destFunc;
/* 52 */     this.opacity = opacity;
/*    */   }
/*    */   
/*    */   public BlendingAttribute(int sourceFunc, int destFunc, float opacity) {
/* 56 */     this(true, sourceFunc, destFunc, opacity);
/*    */   }
/*    */   
/*    */   public BlendingAttribute(int sourceFunc, int destFunc) {
/* 60 */     this(sourceFunc, destFunc, 1.0F);
/*    */   }
/*    */   
/*    */   public BlendingAttribute(boolean blended, float opacity) {
/* 64 */     this(blended, 770, 771, opacity);
/*    */   }
/*    */   
/*    */   public BlendingAttribute(float opacity) {
/* 68 */     this(true, opacity);
/*    */   }
/*    */   
/*    */   public BlendingAttribute(BlendingAttribute copyFrom) {
/* 72 */     this((copyFrom == null) ? true : copyFrom.blended, (copyFrom == null) ? 770 : copyFrom.sourceFunction, (copyFrom == null) ? 771 : copyFrom.destFunction, (copyFrom == null) ? 1.0F : copyFrom.opacity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlendingAttribute copy() {
/* 78 */     return new BlendingAttribute(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 83 */     int result = super.hashCode();
/* 84 */     result = 947 * result + (this.blended ? 1 : 0);
/* 85 */     result = 947 * result + this.sourceFunction;
/* 86 */     result = 947 * result + this.destFunction;
/* 87 */     result = 947 * result + NumberUtils.floatToRawIntBits(this.opacity);
/* 88 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Attribute o) {
/* 93 */     if (this.type != o.type) return (int)(this.type - o.type); 
/* 94 */     BlendingAttribute other = (BlendingAttribute)o;
/* 95 */     if (this.blended != other.blended) return this.blended ? 1 : -1; 
/* 96 */     if (this.sourceFunction != other.sourceFunction) return this.sourceFunction - other.sourceFunction; 
/* 97 */     if (this.destFunction != other.destFunction) return this.destFunction - other.destFunction; 
/* 98 */     return MathUtils.isEqual(this.opacity, other.opacity) ? 0 : ((this.opacity < other.opacity) ? 1 : -1);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\BlendingAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */