/*     */ package com.badlogic.gdx.graphics.g3d.attributes;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.NumberUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DepthTestAttribute
/*     */   extends Attribute
/*     */ {
/*     */   public static final String Alias = "depthStencil";
/*  27 */   public static final long Type = register("depthStencil");
/*     */   
/*  29 */   protected static long Mask = Type;
/*     */   
/*     */   public static final boolean is(long mask) {
/*  32 */     return ((mask & Mask) != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public int depthFunc;
/*     */   
/*     */   public float depthRangeNear;
/*     */   
/*     */   public float depthRangeFar;
/*     */   
/*     */   public boolean depthMask;
/*     */   
/*     */   public DepthTestAttribute() {
/*  45 */     this(515);
/*     */   }
/*     */   
/*     */   public DepthTestAttribute(boolean depthMask) {
/*  49 */     this(515, depthMask);
/*     */   }
/*     */   
/*     */   public DepthTestAttribute(int depthFunc) {
/*  53 */     this(depthFunc, true);
/*     */   }
/*     */   
/*     */   public DepthTestAttribute(int depthFunc, boolean depthMask) {
/*  57 */     this(depthFunc, 0.0F, 1.0F, depthMask);
/*     */   }
/*     */   
/*     */   public DepthTestAttribute(int depthFunc, float depthRangeNear, float depthRangeFar) {
/*  61 */     this(depthFunc, depthRangeNear, depthRangeFar, true);
/*     */   }
/*     */   
/*     */   public DepthTestAttribute(int depthFunc, float depthRangeNear, float depthRangeFar, boolean depthMask) {
/*  65 */     this(Type, depthFunc, depthRangeNear, depthRangeFar, depthMask);
/*     */   }
/*     */   
/*     */   public DepthTestAttribute(long type, int depthFunc, float depthRangeNear, float depthRangeFar, boolean depthMask) {
/*  69 */     super(type);
/*  70 */     if (!is(type)) throw new GdxRuntimeException("Invalid type specified"); 
/*  71 */     this.depthFunc = depthFunc;
/*  72 */     this.depthRangeNear = depthRangeNear;
/*  73 */     this.depthRangeFar = depthRangeFar;
/*  74 */     this.depthMask = depthMask;
/*     */   }
/*     */   
/*     */   public DepthTestAttribute(DepthTestAttribute rhs) {
/*  78 */     this(rhs.type, rhs.depthFunc, rhs.depthRangeNear, rhs.depthRangeFar, rhs.depthMask);
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute copy() {
/*  83 */     return new DepthTestAttribute(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  88 */     int result = super.hashCode();
/*  89 */     result = 971 * result + this.depthFunc;
/*  90 */     result = 971 * result + NumberUtils.floatToRawIntBits(this.depthRangeNear);
/*  91 */     result = 971 * result + NumberUtils.floatToRawIntBits(this.depthRangeFar);
/*  92 */     result = 971 * result + (this.depthMask ? 1 : 0);
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Attribute o) {
/*  98 */     if (this.type != o.type) return (int)(this.type - o.type); 
/*  99 */     DepthTestAttribute other = (DepthTestAttribute)o;
/* 100 */     if (this.depthFunc != other.depthFunc) return this.depthFunc - other.depthFunc; 
/* 101 */     if (this.depthMask != other.depthMask) return this.depthMask ? -1 : 1; 
/* 102 */     if (!MathUtils.isEqual(this.depthRangeNear, other.depthRangeNear))
/* 103 */       return (this.depthRangeNear < other.depthRangeNear) ? -1 : 1; 
/* 104 */     if (!MathUtils.isEqual(this.depthRangeFar, other.depthRangeFar))
/* 105 */       return (this.depthRangeFar < other.depthRangeFar) ? -1 : 1; 
/* 106 */     return 0;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\DepthTestAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */