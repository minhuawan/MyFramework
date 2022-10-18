/*     */ package com.badlogic.gdx.graphics.g3d.attributes;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class ColorAttribute
/*     */   extends Attribute
/*     */ {
/*     */   public static final String DiffuseAlias = "diffuseColor";
/*  25 */   public static final long Diffuse = register("diffuseColor");
/*     */   public static final String SpecularAlias = "specularColor";
/*  27 */   public static final long Specular = register("specularColor");
/*     */   public static final String AmbientAlias = "ambientColor";
/*  29 */   public static final long Ambient = register("ambientColor");
/*     */   public static final String EmissiveAlias = "emissiveColor";
/*  31 */   public static final long Emissive = register("emissiveColor");
/*     */   public static final String ReflectionAlias = "reflectionColor";
/*  33 */   public static final long Reflection = register("reflectionColor");
/*     */   public static final String AmbientLightAlias = "ambientLightColor";
/*  35 */   public static final long AmbientLight = register("ambientLightColor");
/*     */   public static final String FogAlias = "fogColor";
/*  37 */   public static final long Fog = register("fogColor");
/*     */   
/*  39 */   protected static long Mask = Ambient | Diffuse | Specular | Emissive | Reflection | AmbientLight | Fog;
/*     */   
/*     */   public static final boolean is(long mask) {
/*  42 */     return ((mask & Mask) != 0L);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createAmbient(Color color) {
/*  46 */     return new ColorAttribute(Ambient, color);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createAmbient(float r, float g, float b, float a) {
/*  50 */     return new ColorAttribute(Ambient, r, g, b, a);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createDiffuse(Color color) {
/*  54 */     return new ColorAttribute(Diffuse, color);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createDiffuse(float r, float g, float b, float a) {
/*  58 */     return new ColorAttribute(Diffuse, r, g, b, a);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createSpecular(Color color) {
/*  62 */     return new ColorAttribute(Specular, color);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createSpecular(float r, float g, float b, float a) {
/*  66 */     return new ColorAttribute(Specular, r, g, b, a);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createReflection(Color color) {
/*  70 */     return new ColorAttribute(Reflection, color);
/*     */   }
/*     */   
/*     */   public static final ColorAttribute createReflection(float r, float g, float b, float a) {
/*  74 */     return new ColorAttribute(Reflection, r, g, b, a);
/*     */   }
/*     */   
/*  77 */   public final Color color = new Color();
/*     */   
/*     */   public ColorAttribute(long type) {
/*  80 */     super(type);
/*  81 */     if (!is(type)) throw new GdxRuntimeException("Invalid type specified"); 
/*     */   }
/*     */   
/*     */   public ColorAttribute(long type, Color color) {
/*  85 */     this(type);
/*  86 */     if (color != null) this.color.set(color); 
/*     */   }
/*     */   
/*     */   public ColorAttribute(long type, float r, float g, float b, float a) {
/*  90 */     this(type);
/*  91 */     this.color.set(r, g, b, a);
/*     */   }
/*     */   
/*     */   public ColorAttribute(ColorAttribute copyFrom) {
/*  95 */     this(copyFrom.type, copyFrom.color);
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute copy() {
/* 100 */     return new ColorAttribute(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     int result = super.hashCode();
/* 106 */     result = 953 * result + this.color.toIntBits();
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Attribute o) {
/* 112 */     if (this.type != o.type) return (int)(this.type - o.type); 
/* 113 */     return ((ColorAttribute)o).color.toIntBits() - this.color.toIntBits();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\ColorAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */