/*     */ package com.badlogic.gdx.graphics;
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
/*     */ public final class VertexAttribute
/*     */ {
/*     */   public final int usage;
/*     */   public final int numComponents;
/*     */   public final boolean normalized;
/*     */   public final int type;
/*     */   public int offset;
/*     */   public String alias;
/*     */   public int unit;
/*     */   private final int usageIndex;
/*     */   
/*     */   public VertexAttribute(int usage, int numComponents, String alias) {
/*  56 */     this(usage, numComponents, alias, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexAttribute(int usage, int numComponents, String alias, int index) {
/*  67 */     this(usage, numComponents, (usage == 4) ? 5121 : 5126, (usage == 4), alias, index);
/*     */   }
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
/*     */   protected VertexAttribute(int usage, int numComponents, int type, boolean normalized, String alias) {
/*  97 */     this(usage, numComponents, type, normalized, alias, 0);
/*     */   }
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
/*     */   protected VertexAttribute(int usage, int numComponents, int type, boolean normalized, String alias, int index) {
/* 127 */     this.usage = usage;
/* 128 */     this.numComponents = numComponents;
/* 129 */     this.type = type;
/* 130 */     this.normalized = normalized;
/* 131 */     this.alias = alias;
/* 132 */     this.unit = index;
/* 133 */     this.usageIndex = Integer.numberOfTrailingZeros(usage);
/*     */   }
/*     */   
/*     */   public static VertexAttribute Position() {
/* 137 */     return new VertexAttribute(1, 3, "a_position");
/*     */   }
/*     */   
/*     */   public static VertexAttribute TexCoords(int unit) {
/* 141 */     return new VertexAttribute(16, 2, "a_texCoord" + unit, unit);
/*     */   }
/*     */   
/*     */   public static VertexAttribute Normal() {
/* 145 */     return new VertexAttribute(8, 3, "a_normal");
/*     */   }
/*     */   
/*     */   public static VertexAttribute ColorPacked() {
/* 149 */     return new VertexAttribute(4, 4, 5121, true, "a_color");
/*     */   }
/*     */   
/*     */   public static VertexAttribute ColorUnpacked() {
/* 153 */     return new VertexAttribute(2, 4, 5126, false, "a_color");
/*     */   }
/*     */   
/*     */   public static VertexAttribute Tangent() {
/* 157 */     return new VertexAttribute(128, 3, "a_tangent");
/*     */   }
/*     */   
/*     */   public static VertexAttribute Binormal() {
/* 161 */     return new VertexAttribute(256, 3, "a_binormal");
/*     */   }
/*     */   
/*     */   public static VertexAttribute BoneWeight(int unit) {
/* 165 */     return new VertexAttribute(64, 2, "a_boneWeight" + unit, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 171 */     if (!(obj instanceof VertexAttribute)) {
/* 172 */       return false;
/*     */     }
/* 174 */     return equals((VertexAttribute)obj);
/*     */   }
/*     */   
/*     */   public boolean equals(VertexAttribute other) {
/* 178 */     return (other != null && this.usage == other.usage && this.numComponents == other.numComponents && this.alias.equals(other.alias) && this.unit == other.unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKey() {
/* 184 */     return (this.usageIndex << 8) + (this.unit & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 189 */     int result = getKey();
/* 190 */     result = 541 * result + this.numComponents;
/* 191 */     result = 541 * result + this.alias.hashCode();
/* 192 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\VertexAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */