/*     */ package com.badlogic.gdx.graphics.g3d.attributes;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
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
/*     */ public class TextureAttribute
/*     */   extends Attribute
/*     */ {
/*     */   public static final String DiffuseAlias = "diffuseTexture";
/*  29 */   public static final long Diffuse = register("diffuseTexture");
/*     */   public static final String SpecularAlias = "specularTexture";
/*  31 */   public static final long Specular = register("specularTexture");
/*     */   public static final String BumpAlias = "bumpTexture";
/*  33 */   public static final long Bump = register("bumpTexture");
/*     */   public static final String NormalAlias = "normalTexture";
/*  35 */   public static final long Normal = register("normalTexture");
/*     */   public static final String AmbientAlias = "ambientTexture";
/*  37 */   public static final long Ambient = register("ambientTexture");
/*     */   public static final String EmissiveAlias = "emissiveTexture";
/*  39 */   public static final long Emissive = register("emissiveTexture");
/*     */   public static final String ReflectionAlias = "reflectionTexture";
/*  41 */   public static final long Reflection = register("reflectionTexture");
/*     */   
/*  43 */   protected static long Mask = Diffuse | Specular | Bump | Normal | Ambient | Emissive | Reflection;
/*     */   
/*     */   public static final boolean is(long mask) {
/*  46 */     return ((mask & Mask) != 0L);
/*     */   }
/*     */   public final TextureDescriptor<Texture> textureDescription;
/*     */   public static TextureAttribute createDiffuse(Texture texture) {
/*  50 */     return new TextureAttribute(Diffuse, texture);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createDiffuse(TextureRegion region) {
/*  54 */     return new TextureAttribute(Diffuse, region);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createSpecular(Texture texture) {
/*  58 */     return new TextureAttribute(Specular, texture);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createSpecular(TextureRegion region) {
/*  62 */     return new TextureAttribute(Specular, region);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createNormal(Texture texture) {
/*  66 */     return new TextureAttribute(Normal, texture);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createNormal(TextureRegion region) {
/*  70 */     return new TextureAttribute(Normal, region);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createBump(Texture texture) {
/*  74 */     return new TextureAttribute(Bump, texture);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createBump(TextureRegion region) {
/*  78 */     return new TextureAttribute(Bump, region);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createAmbient(Texture texture) {
/*  82 */     return new TextureAttribute(Ambient, texture);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createAmbient(TextureRegion region) {
/*  86 */     return new TextureAttribute(Ambient, region);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createEmissive(Texture texture) {
/*  90 */     return new TextureAttribute(Emissive, texture);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createEmissive(TextureRegion region) {
/*  94 */     return new TextureAttribute(Emissive, region);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createReflection(Texture texture) {
/*  98 */     return new TextureAttribute(Reflection, texture);
/*     */   }
/*     */   
/*     */   public static TextureAttribute createReflection(TextureRegion region) {
/* 102 */     return new TextureAttribute(Reflection, region);
/*     */   }
/*     */ 
/*     */   
/* 106 */   public float offsetU = 0.0F;
/* 107 */   public float offsetV = 0.0F;
/* 108 */   public float scaleU = 1.0F;
/* 109 */   public float scaleV = 1.0F;
/*     */ 
/*     */ 
/*     */   
/* 113 */   public int uvIndex = 0;
/*     */   
/*     */   public TextureAttribute(long type) {
/* 116 */     super(type);
/* 117 */     if (!is(type)) throw new GdxRuntimeException("Invalid type specified"); 
/* 118 */     this.textureDescription = new TextureDescriptor();
/*     */   }
/*     */   
/*     */   public <T extends Texture> TextureAttribute(long type, TextureDescriptor<T> textureDescription) {
/* 122 */     this(type);
/* 123 */     this.textureDescription.set(textureDescription);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Texture> TextureAttribute(long type, TextureDescriptor<T> textureDescription, float offsetU, float offsetV, float scaleU, float scaleV, int uvIndex) {
/* 128 */     this(type, textureDescription);
/* 129 */     this.offsetU = offsetU;
/* 130 */     this.offsetV = offsetV;
/* 131 */     this.scaleU = scaleU;
/* 132 */     this.scaleV = scaleV;
/* 133 */     this.uvIndex = uvIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Texture> TextureAttribute(long type, TextureDescriptor<T> textureDescription, float offsetU, float offsetV, float scaleU, float scaleV) {
/* 138 */     this(type, textureDescription, offsetU, offsetV, scaleU, scaleV, 0);
/*     */   }
/*     */   
/*     */   public TextureAttribute(long type, Texture texture) {
/* 142 */     this(type);
/* 143 */     this.textureDescription.texture = (GLTexture)texture;
/*     */   }
/*     */   
/*     */   public TextureAttribute(long type, TextureRegion region) {
/* 147 */     this(type);
/* 148 */     set(region);
/*     */   }
/*     */   
/*     */   public TextureAttribute(TextureAttribute copyFrom) {
/* 152 */     this(copyFrom.type, copyFrom.textureDescription, copyFrom.offsetU, copyFrom.offsetV, copyFrom.scaleU, copyFrom.scaleV, copyFrom.uvIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(TextureRegion region) {
/* 157 */     this.textureDescription.texture = (GLTexture)region.getTexture();
/* 158 */     this.offsetU = region.getU();
/* 159 */     this.offsetV = region.getV();
/* 160 */     this.scaleU = region.getU2() - this.offsetU;
/* 161 */     this.scaleV = region.getV2() - this.offsetV;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute copy() {
/* 166 */     return new TextureAttribute(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 171 */     int result = super.hashCode();
/* 172 */     result = 991 * result + this.textureDescription.hashCode();
/* 173 */     result = 991 * result + NumberUtils.floatToRawIntBits(this.offsetU);
/* 174 */     result = 991 * result + NumberUtils.floatToRawIntBits(this.offsetV);
/* 175 */     result = 991 * result + NumberUtils.floatToRawIntBits(this.scaleU);
/* 176 */     result = 991 * result + NumberUtils.floatToRawIntBits(this.scaleV);
/* 177 */     result = 991 * result + this.uvIndex;
/* 178 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Attribute o) {
/* 183 */     if (this.type != o.type) return (this.type < o.type) ? -1 : 1; 
/* 184 */     TextureAttribute other = (TextureAttribute)o;
/* 185 */     int c = this.textureDescription.compareTo(other.textureDescription);
/* 186 */     if (c != 0) return c; 
/* 187 */     if (this.uvIndex != other.uvIndex) return this.uvIndex - other.uvIndex; 
/* 188 */     if (!MathUtils.isEqual(this.scaleU, other.scaleU)) return (this.scaleU > other.scaleU) ? 1 : -1; 
/* 189 */     if (!MathUtils.isEqual(this.scaleV, other.scaleV)) return (this.scaleV > other.scaleV) ? 1 : -1; 
/* 190 */     if (!MathUtils.isEqual(this.offsetU, other.offsetU)) return (this.offsetU > other.offsetU) ? 1 : -1; 
/* 191 */     if (!MathUtils.isEqual(this.offsetV, other.offsetV)) return (this.offsetV > other.offsetV) ? 1 : -1; 
/* 192 */     return 0;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\TextureAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */