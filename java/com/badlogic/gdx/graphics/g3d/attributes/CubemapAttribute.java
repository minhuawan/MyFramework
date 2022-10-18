/*    */ package com.badlogic.gdx.graphics.g3d.attributes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Cubemap;
/*    */ import com.badlogic.gdx.graphics.GLTexture;
/*    */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*    */ import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*    */ public class CubemapAttribute
/*    */   extends Attribute
/*    */ {
/*    */   public static final String EnvironmentMapAlias = "environmentCubemap";
/* 26 */   public static final long EnvironmentMap = register("environmentCubemap");
/*    */   
/* 28 */   protected static long Mask = EnvironmentMap;
/*    */   
/*    */   public static final boolean is(long mask) {
/* 31 */     return ((mask & Mask) != 0L);
/*    */   }
/*    */   
/*    */   public final TextureDescriptor<Cubemap> textureDescription;
/*    */   
/*    */   public CubemapAttribute(long type) {
/* 37 */     super(type);
/* 38 */     if (!is(type)) throw new GdxRuntimeException("Invalid type specified"); 
/* 39 */     this.textureDescription = new TextureDescriptor();
/*    */   }
/*    */   
/*    */   public <T extends Cubemap> CubemapAttribute(long type, TextureDescriptor<T> textureDescription) {
/* 43 */     this(type);
/* 44 */     this.textureDescription.set(textureDescription);
/*    */   }
/*    */   
/*    */   public CubemapAttribute(long type, Cubemap texture) {
/* 48 */     this(type);
/* 49 */     this.textureDescription.texture = (GLTexture)texture;
/*    */   }
/*    */   
/*    */   public CubemapAttribute(CubemapAttribute copyFrom) {
/* 53 */     this(copyFrom.type, copyFrom.textureDescription);
/*    */   }
/*    */ 
/*    */   
/*    */   public Attribute copy() {
/* 58 */     return new CubemapAttribute(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     int result = super.hashCode();
/* 64 */     result = 967 * result + this.textureDescription.hashCode();
/* 65 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Attribute o) {
/* 70 */     if (this.type != o.type) return (int)(this.type - o.type); 
/* 71 */     return this.textureDescription.compareTo(((CubemapAttribute)o).textureDescription);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\attributes\CubemapAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */