/*     */ package com.badlogic.gdx.graphics.g3d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.SpotLightsAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.PointLight;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.ShadowMap;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Environment
/*     */   extends Attributes
/*     */ {
/*     */   public ShadowMap shadowMap;
/*     */   
/*     */   public Environment add(BaseLight... lights) {
/*  38 */     for (BaseLight light : lights)
/*  39 */       add(light); 
/*  40 */     return this;
/*     */   }
/*     */   
/*     */   public Environment add(Array<BaseLight> lights) {
/*  44 */     for (BaseLight light : lights)
/*  45 */       add(light); 
/*  46 */     return this;
/*     */   }
/*     */   
/*     */   public Environment add(BaseLight light) {
/*  50 */     if (light instanceof DirectionalLight) {
/*  51 */       add((DirectionalLight)light);
/*  52 */     } else if (light instanceof PointLight) {
/*  53 */       add((PointLight)light);
/*  54 */     } else if (light instanceof SpotLight) {
/*  55 */       add((SpotLight)light);
/*     */     } else {
/*  57 */       throw new GdxRuntimeException("Unknown light type");
/*  58 */     }  return this;
/*     */   }
/*     */   
/*     */   public Environment add(DirectionalLight light) {
/*  62 */     DirectionalLightsAttribute dirLights = (DirectionalLightsAttribute)get(DirectionalLightsAttribute.Type);
/*  63 */     if (dirLights == null) set((Attribute)(dirLights = new DirectionalLightsAttribute())); 
/*  64 */     dirLights.lights.add(light);
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public Environment add(PointLight light) {
/*  69 */     PointLightsAttribute pointLights = (PointLightsAttribute)get(PointLightsAttribute.Type);
/*  70 */     if (pointLights == null) set((Attribute)(pointLights = new PointLightsAttribute())); 
/*  71 */     pointLights.lights.add(light);
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public Environment add(SpotLight light) {
/*  76 */     SpotLightsAttribute spotLights = (SpotLightsAttribute)get(SpotLightsAttribute.Type);
/*  77 */     if (spotLights == null) set((Attribute)(spotLights = new SpotLightsAttribute())); 
/*  78 */     spotLights.lights.add(light);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public Environment remove(BaseLight... lights) {
/*  83 */     for (BaseLight light : lights)
/*  84 */       remove(light); 
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Environment remove(Array<BaseLight> lights) {
/*  89 */     for (BaseLight light : lights)
/*  90 */       remove(light); 
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public Environment remove(BaseLight light) {
/*  95 */     if (light instanceof DirectionalLight) {
/*  96 */       remove((DirectionalLight)light);
/*  97 */     } else if (light instanceof PointLight) {
/*  98 */       remove((PointLight)light);
/*  99 */     } else if (light instanceof SpotLight) {
/* 100 */       remove((SpotLight)light);
/*     */     } else {
/* 102 */       throw new GdxRuntimeException("Unknown light type");
/* 103 */     }  return this;
/*     */   }
/*     */   
/*     */   public Environment remove(DirectionalLight light) {
/* 107 */     if (has(DirectionalLightsAttribute.Type)) {
/* 108 */       DirectionalLightsAttribute dirLights = (DirectionalLightsAttribute)get(DirectionalLightsAttribute.Type);
/* 109 */       dirLights.lights.removeValue(light, false);
/* 110 */       if (dirLights.lights.size == 0)
/* 111 */         remove(DirectionalLightsAttribute.Type); 
/*     */     } 
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public Environment remove(PointLight light) {
/* 117 */     if (has(PointLightsAttribute.Type)) {
/* 118 */       PointLightsAttribute pointLights = (PointLightsAttribute)get(PointLightsAttribute.Type);
/* 119 */       pointLights.lights.removeValue(light, false);
/* 120 */       if (pointLights.lights.size == 0)
/* 121 */         remove(PointLightsAttribute.Type); 
/*     */     } 
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public Environment remove(SpotLight light) {
/* 127 */     if (has(SpotLightsAttribute.Type)) {
/* 128 */       SpotLightsAttribute spotLights = (SpotLightsAttribute)get(SpotLightsAttribute.Type);
/* 129 */       spotLights.lights.removeValue(light, false);
/* 130 */       if (spotLights.lights.size == 0)
/* 131 */         remove(SpotLightsAttribute.Type); 
/*     */     } 
/* 133 */     return this;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\Environment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */