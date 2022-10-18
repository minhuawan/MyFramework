/*     */ package com.badlogic.gdx.graphics.g3d.environment;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ public class AmbientCubemap
/*     */ {
/*     */   public final float[] data;
/*     */   
/*     */   private static final float clamp(float v) {
/*  25 */     return (v < 0.0F) ? 0.0F : ((v > 1.0F) ? 1.0F : v);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AmbientCubemap() {
/*  31 */     this.data = new float[18];
/*     */   }
/*     */   
/*     */   public AmbientCubemap(float[] copyFrom) {
/*  35 */     if (copyFrom.length != 18) throw new GdxRuntimeException("Incorrect array size"); 
/*  36 */     this.data = new float[copyFrom.length];
/*  37 */     System.arraycopy(copyFrom, 0, this.data, 0, this.data.length);
/*     */   }
/*     */   
/*     */   public AmbientCubemap(AmbientCubemap copyFrom) {
/*  41 */     this(copyFrom.data);
/*     */   }
/*     */   
/*     */   public AmbientCubemap set(float[] values) {
/*  45 */     for (int i = 0; i < this.data.length; i++)
/*  46 */       this.data[i] = values[i]; 
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   public AmbientCubemap set(AmbientCubemap other) {
/*  51 */     return set(other.data);
/*     */   }
/*     */   
/*     */   public AmbientCubemap set(Color color) {
/*  55 */     return set(color.r, color.g, color.b);
/*     */   }
/*     */   
/*     */   public AmbientCubemap set(float r, float g, float b) {
/*  59 */     for (int idx = 0; idx < this.data.length; ) {
/*  60 */       this.data[idx++] = r;
/*  61 */       this.data[idx++] = g;
/*  62 */       this.data[idx++] = b;
/*     */     } 
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public Color getColor(Color out, int side) {
/*  68 */     side *= 3;
/*  69 */     return out.set(this.data[side], this.data[side + 1], this.data[side + 2], 1.0F);
/*     */   }
/*     */   
/*     */   public AmbientCubemap clear() {
/*  73 */     for (int i = 0; i < this.data.length; i++)
/*  74 */       this.data[i] = 0.0F; 
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public AmbientCubemap clamp() {
/*  79 */     for (int i = 0; i < this.data.length; i++)
/*  80 */       this.data[i] = clamp(this.data[i]); 
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(float r, float g, float b) {
/*  85 */     for (int idx = 0; idx < this.data.length; ) {
/*  86 */       this.data[idx++] = this.data[idx++] + r;
/*  87 */       this.data[idx++] = this.data[idx++] + g;
/*  88 */       this.data[idx++] = this.data[idx++] + b;
/*     */     } 
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(Color color) {
/*  94 */     return add(color.r, color.g, color.b);
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(float r, float g, float b, float x, float y, float z) {
/*  98 */     float x2 = x * x, y2 = y * y, z2 = z * z;
/*  99 */     float d = x2 + y2 + z2;
/* 100 */     if (d == 0.0F) return this; 
/* 101 */     d = 1.0F / d * (d + 1.0F);
/* 102 */     float rd = r * d, gd = g * d, bd = b * d;
/* 103 */     int idx = (x > 0.0F) ? 0 : 3;
/* 104 */     this.data[idx] = this.data[idx] + x2 * rd;
/* 105 */     this.data[idx + 1] = this.data[idx + 1] + x2 * gd;
/* 106 */     this.data[idx + 2] = this.data[idx + 2] + x2 * bd;
/* 107 */     idx = (y > 0.0F) ? 6 : 9;
/* 108 */     this.data[idx] = this.data[idx] + y2 * rd;
/* 109 */     this.data[idx + 1] = this.data[idx + 1] + y2 * gd;
/* 110 */     this.data[idx + 2] = this.data[idx + 2] + y2 * bd;
/* 111 */     idx = (z > 0.0F) ? 12 : 15;
/* 112 */     this.data[idx] = this.data[idx] + z2 * rd;
/* 113 */     this.data[idx + 1] = this.data[idx + 1] + z2 * gd;
/* 114 */     this.data[idx + 2] = this.data[idx + 2] + z2 * bd;
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(Color color, Vector3 direction) {
/* 119 */     return add(color.r, color.g, color.b, direction.x, direction.y, direction.z);
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(float r, float g, float b, Vector3 direction) {
/* 123 */     return add(r, g, b, direction.x, direction.y, direction.z);
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(Color color, float x, float y, float z) {
/* 127 */     return add(color.r, color.g, color.b, x, y, z);
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(Color color, Vector3 point, Vector3 target) {
/* 131 */     return add(color.r, color.g, color.b, target.x - point.x, target.y - point.y, target.z - point.z);
/*     */   }
/*     */   
/*     */   public AmbientCubemap add(Color color, Vector3 point, Vector3 target, float intensity) {
/* 135 */     float t = intensity / (1.0F + target.dst(point));
/* 136 */     return add(color.r * t, color.g * t, color.b * t, target.x - point.x, target.y - point.y, target.z - point.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     String result = "";
/* 142 */     for (int i = 0; i < this.data.length; i += 3) {
/* 143 */       result = result + Float.toString(this.data[i]) + ", " + Float.toString(this.data[i + 1]) + ", " + Float.toString(this.data[i + 2]) + "\n";
/*     */     }
/* 145 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\environment\AmbientCubemap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */