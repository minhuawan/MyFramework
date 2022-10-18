/*     */ package com.badlogic.gdx.graphics.g3d.shaders;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Attributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Environment;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.Shader;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.SpotLightsAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.AmbientCubemap;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.PointLight;
/*     */ import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.math.Matrix3;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ public class DefaultShader
/*     */   extends BaseShader
/*     */ {
/*     */   public static class Config
/*     */   {
/*  54 */     public String vertexShader = null;
/*     */     
/*  56 */     public String fragmentShader = null;
/*     */     
/*  58 */     public int numDirectionalLights = 2;
/*     */     
/*  60 */     public int numPointLights = 5;
/*     */     
/*  62 */     public int numSpotLights = 0;
/*     */     
/*  64 */     public int numBones = 12;
/*     */     
/*     */     public boolean ignoreUnimplemented = true;
/*     */     
/*  68 */     public int defaultCullFace = -1;
/*     */     
/*  70 */     public int defaultDepthFunc = -1;
/*     */ 
/*     */     
/*     */     public Config() {}
/*     */     
/*     */     public Config(String vertexShader, String fragmentShader) {
/*  76 */       this.vertexShader = vertexShader;
/*  77 */       this.fragmentShader = fragmentShader;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Inputs {
/*  82 */     public static final BaseShader.Uniform projTrans = new BaseShader.Uniform("u_projTrans");
/*  83 */     public static final BaseShader.Uniform viewTrans = new BaseShader.Uniform("u_viewTrans");
/*  84 */     public static final BaseShader.Uniform projViewTrans = new BaseShader.Uniform("u_projViewTrans");
/*  85 */     public static final BaseShader.Uniform cameraPosition = new BaseShader.Uniform("u_cameraPosition");
/*  86 */     public static final BaseShader.Uniform cameraDirection = new BaseShader.Uniform("u_cameraDirection");
/*  87 */     public static final BaseShader.Uniform cameraUp = new BaseShader.Uniform("u_cameraUp");
/*  88 */     public static final BaseShader.Uniform cameraNearFar = new BaseShader.Uniform("u_cameraNearFar");
/*     */     
/*  90 */     public static final BaseShader.Uniform worldTrans = new BaseShader.Uniform("u_worldTrans");
/*  91 */     public static final BaseShader.Uniform viewWorldTrans = new BaseShader.Uniform("u_viewWorldTrans");
/*  92 */     public static final BaseShader.Uniform projViewWorldTrans = new BaseShader.Uniform("u_projViewWorldTrans");
/*  93 */     public static final BaseShader.Uniform normalMatrix = new BaseShader.Uniform("u_normalMatrix");
/*  94 */     public static final BaseShader.Uniform bones = new BaseShader.Uniform("u_bones");
/*     */     
/*  96 */     public static final BaseShader.Uniform shininess = new BaseShader.Uniform("u_shininess", FloatAttribute.Shininess);
/*  97 */     public static final BaseShader.Uniform opacity = new BaseShader.Uniform("u_opacity", BlendingAttribute.Type);
/*  98 */     public static final BaseShader.Uniform diffuseColor = new BaseShader.Uniform("u_diffuseColor", ColorAttribute.Diffuse);
/*  99 */     public static final BaseShader.Uniform diffuseTexture = new BaseShader.Uniform("u_diffuseTexture", TextureAttribute.Diffuse);
/* 100 */     public static final BaseShader.Uniform diffuseUVTransform = new BaseShader.Uniform("u_diffuseUVTransform", TextureAttribute.Diffuse);
/* 101 */     public static final BaseShader.Uniform specularColor = new BaseShader.Uniform("u_specularColor", ColorAttribute.Specular);
/* 102 */     public static final BaseShader.Uniform specularTexture = new BaseShader.Uniform("u_specularTexture", TextureAttribute.Specular);
/* 103 */     public static final BaseShader.Uniform specularUVTransform = new BaseShader.Uniform("u_specularUVTransform", TextureAttribute.Specular);
/* 104 */     public static final BaseShader.Uniform emissiveColor = new BaseShader.Uniform("u_emissiveColor", ColorAttribute.Emissive);
/* 105 */     public static final BaseShader.Uniform emissiveTexture = new BaseShader.Uniform("u_emissiveTexture", TextureAttribute.Emissive);
/* 106 */     public static final BaseShader.Uniform emissiveUVTransform = new BaseShader.Uniform("u_emissiveUVTransform", TextureAttribute.Emissive);
/* 107 */     public static final BaseShader.Uniform reflectionColor = new BaseShader.Uniform("u_reflectionColor", ColorAttribute.Reflection);
/* 108 */     public static final BaseShader.Uniform reflectionTexture = new BaseShader.Uniform("u_reflectionTexture", TextureAttribute.Reflection);
/* 109 */     public static final BaseShader.Uniform reflectionUVTransform = new BaseShader.Uniform("u_reflectionUVTransform", TextureAttribute.Reflection);
/* 110 */     public static final BaseShader.Uniform normalTexture = new BaseShader.Uniform("u_normalTexture", TextureAttribute.Normal);
/* 111 */     public static final BaseShader.Uniform normalUVTransform = new BaseShader.Uniform("u_normalUVTransform", TextureAttribute.Normal);
/* 112 */     public static final BaseShader.Uniform ambientTexture = new BaseShader.Uniform("u_ambientTexture", TextureAttribute.Ambient);
/* 113 */     public static final BaseShader.Uniform ambientUVTransform = new BaseShader.Uniform("u_ambientUVTransform", TextureAttribute.Ambient);
/* 114 */     public static final BaseShader.Uniform alphaTest = new BaseShader.Uniform("u_alphaTest");
/*     */     
/* 116 */     public static final BaseShader.Uniform ambientCube = new BaseShader.Uniform("u_ambientCubemap");
/* 117 */     public static final BaseShader.Uniform dirLights = new BaseShader.Uniform("u_dirLights");
/* 118 */     public static final BaseShader.Uniform pointLights = new BaseShader.Uniform("u_pointLights");
/* 119 */     public static final BaseShader.Uniform spotLights = new BaseShader.Uniform("u_spotLights");
/* 120 */     public static final BaseShader.Uniform environmentCubemap = new BaseShader.Uniform("u_environmentCubemap");
/*     */   }
/*     */   
/*     */   public static class Setters {
/* 124 */     public static final BaseShader.Setter projTrans = new BaseShader.GlobalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 127 */           shader.set(inputID, shader.camera.projection);
/*     */         }
/*     */       };
/* 130 */     public static final BaseShader.Setter viewTrans = new BaseShader.GlobalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 133 */           shader.set(inputID, shader.camera.view);
/*     */         }
/*     */       };
/* 136 */     public static final BaseShader.Setter projViewTrans = new BaseShader.GlobalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 139 */           shader.set(inputID, shader.camera.combined);
/*     */         }
/*     */       };
/* 142 */     public static final BaseShader.Setter cameraPosition = new BaseShader.GlobalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 145 */           shader.set(inputID, shader.camera.position.x, shader.camera.position.y, shader.camera.position.z, 1.1881F / shader.camera.far * shader.camera.far);
/*     */         }
/*     */       };
/*     */     
/* 149 */     public static final BaseShader.Setter cameraDirection = new BaseShader.GlobalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 152 */           shader.set(inputID, shader.camera.direction);
/*     */         }
/*     */       };
/* 155 */     public static final BaseShader.Setter cameraUp = new BaseShader.GlobalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 158 */           shader.set(inputID, shader.camera.up);
/*     */         }
/*     */       };
/* 161 */     public static final BaseShader.Setter cameraNearFar = new BaseShader.GlobalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 164 */           shader.set(inputID, shader.camera.near, shader.camera.far);
/*     */         }
/*     */       };
/* 167 */     public static final BaseShader.Setter worldTrans = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 170 */           shader.set(inputID, renderable.worldTransform);
/*     */         }
/*     */       };
/* 173 */     public static final BaseShader.Setter viewWorldTrans = new BaseShader.LocalSetter() {
/* 174 */         final Matrix4 temp = new Matrix4();
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 178 */           shader.set(inputID, this.temp.set(shader.camera.view).mul(renderable.worldTransform));
/*     */         }
/*     */       };
/* 181 */     public static final BaseShader.Setter projViewWorldTrans = new BaseShader.LocalSetter() {
/* 182 */         final Matrix4 temp = new Matrix4();
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 186 */           shader.set(inputID, this.temp.set(shader.camera.combined).mul(renderable.worldTransform));
/*     */         }
/*     */       };
/* 189 */     public static final BaseShader.Setter normalMatrix = new BaseShader.LocalSetter() {
/* 190 */         private final Matrix3 tmpM = new Matrix3();
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 194 */           shader.set(inputID, this.tmpM.set(renderable.worldTransform).inv().transpose());
/*     */         }
/*     */       };
/*     */     
/*     */     public static class Bones extends BaseShader.LocalSetter {
/* 199 */       private static final Matrix4 idtMatrix = new Matrix4();
/*     */       public final float[] bones;
/*     */       
/*     */       public Bones(int numBones) {
/* 203 */         this.bones = new float[numBones * 16];
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 208 */         for (int i = 0; i < this.bones.length; i++) {
/* 209 */           int idx = i / 16;
/* 210 */           this.bones[i] = (renderable.bones == null || idx >= renderable.bones.length || renderable.bones[idx] == null) ? idtMatrix.val[i % 16] : (renderable.bones[idx]).val[i % 16];
/*     */         } 
/*     */         
/* 213 */         shader.program.setUniformMatrix4fv(shader.loc(inputID), this.bones, 0, this.bones.length);
/*     */       }
/*     */     }
/*     */     
/* 217 */     public static final BaseShader.Setter shininess = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 220 */           shader.set(inputID, ((FloatAttribute)combinedAttributes.get(FloatAttribute.Shininess)).value);
/*     */         }
/*     */       };
/* 223 */     public static final BaseShader.Setter diffuseColor = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 226 */           shader.set(inputID, ((ColorAttribute)combinedAttributes.get(ColorAttribute.Diffuse)).color);
/*     */         }
/*     */       };
/* 229 */     public static final BaseShader.Setter diffuseTexture = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 232 */           int unit = shader.context.textureBinder.bind(((TextureAttribute)combinedAttributes
/* 233 */               .get(TextureAttribute.Diffuse)).textureDescription);
/* 234 */           shader.set(inputID, unit);
/*     */         }
/*     */       };
/* 237 */     public static final BaseShader.Setter diffuseUVTransform = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 240 */           TextureAttribute ta = (TextureAttribute)combinedAttributes.get(TextureAttribute.Diffuse);
/* 241 */           shader.set(inputID, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
/*     */         }
/*     */       };
/* 244 */     public static final BaseShader.Setter specularColor = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 247 */           shader.set(inputID, ((ColorAttribute)combinedAttributes.get(ColorAttribute.Specular)).color);
/*     */         }
/*     */       };
/* 250 */     public static final BaseShader.Setter specularTexture = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 253 */           int unit = shader.context.textureBinder.bind(((TextureAttribute)combinedAttributes
/* 254 */               .get(TextureAttribute.Specular)).textureDescription);
/* 255 */           shader.set(inputID, unit);
/*     */         }
/*     */       };
/* 258 */     public static final BaseShader.Setter specularUVTransform = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 261 */           TextureAttribute ta = (TextureAttribute)combinedAttributes.get(TextureAttribute.Specular);
/* 262 */           shader.set(inputID, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
/*     */         }
/*     */       };
/* 265 */     public static final BaseShader.Setter emissiveColor = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 268 */           shader.set(inputID, ((ColorAttribute)combinedAttributes.get(ColorAttribute.Emissive)).color);
/*     */         }
/*     */       };
/* 271 */     public static final BaseShader.Setter emissiveTexture = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 274 */           int unit = shader.context.textureBinder.bind(((TextureAttribute)combinedAttributes
/* 275 */               .get(TextureAttribute.Emissive)).textureDescription);
/* 276 */           shader.set(inputID, unit);
/*     */         }
/*     */       };
/* 279 */     public static final BaseShader.Setter emissiveUVTransform = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 282 */           TextureAttribute ta = (TextureAttribute)combinedAttributes.get(TextureAttribute.Emissive);
/* 283 */           shader.set(inputID, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
/*     */         }
/*     */       };
/* 286 */     public static final BaseShader.Setter reflectionColor = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 289 */           shader.set(inputID, ((ColorAttribute)combinedAttributes.get(ColorAttribute.Reflection)).color);
/*     */         }
/*     */       };
/* 292 */     public static final BaseShader.Setter reflectionTexture = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 295 */           int unit = shader.context.textureBinder.bind(((TextureAttribute)combinedAttributes
/* 296 */               .get(TextureAttribute.Reflection)).textureDescription);
/* 297 */           shader.set(inputID, unit);
/*     */         }
/*     */       };
/* 300 */     public static final BaseShader.Setter reflectionUVTransform = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 303 */           TextureAttribute ta = (TextureAttribute)combinedAttributes.get(TextureAttribute.Reflection);
/* 304 */           shader.set(inputID, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
/*     */         }
/*     */       };
/* 307 */     public static final BaseShader.Setter normalTexture = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 310 */           int unit = shader.context.textureBinder.bind(((TextureAttribute)combinedAttributes
/* 311 */               .get(TextureAttribute.Normal)).textureDescription);
/* 312 */           shader.set(inputID, unit);
/*     */         }
/*     */       };
/* 315 */     public static final BaseShader.Setter normalUVTransform = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 318 */           TextureAttribute ta = (TextureAttribute)combinedAttributes.get(TextureAttribute.Normal);
/* 319 */           shader.set(inputID, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
/*     */         }
/*     */       };
/* 322 */     public static final BaseShader.Setter ambientTexture = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 325 */           int unit = shader.context.textureBinder.bind(((TextureAttribute)combinedAttributes
/* 326 */               .get(TextureAttribute.Ambient)).textureDescription);
/* 327 */           shader.set(inputID, unit);
/*     */         }
/*     */       };
/* 330 */     public static final BaseShader.Setter ambientUVTransform = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 333 */           TextureAttribute ta = (TextureAttribute)combinedAttributes.get(TextureAttribute.Ambient);
/* 334 */           shader.set(inputID, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
/*     */         }
/*     */       };
/*     */     
/*     */     public static class ACubemap extends BaseShader.LocalSetter {
/* 339 */       private static final float[] ones = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F };
/* 340 */       private final AmbientCubemap cacheAmbientCubemap = new AmbientCubemap();
/* 341 */       private static final Vector3 tmpV1 = new Vector3();
/*     */       public final int dirLightsOffset;
/*     */       public final int pointLightsOffset;
/*     */       
/*     */       public ACubemap(int dirLightsOffset, int pointLightsOffset) {
/* 346 */         this.dirLightsOffset = dirLightsOffset;
/* 347 */         this.pointLightsOffset = pointLightsOffset;
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 352 */         if (renderable.environment == null) {
/* 353 */           shader.program.setUniform3fv(shader.loc(inputID), ones, 0, ones.length);
/*     */         } else {
/* 355 */           renderable.worldTransform.getTranslation(tmpV1);
/* 356 */           if (combinedAttributes.has(ColorAttribute.AmbientLight)) {
/* 357 */             this.cacheAmbientCubemap.set(((ColorAttribute)combinedAttributes.get(ColorAttribute.AmbientLight)).color);
/*     */           }
/* 359 */           if (combinedAttributes.has(DirectionalLightsAttribute.Type)) {
/*     */             
/* 361 */             Array<DirectionalLight> lights = ((DirectionalLightsAttribute)combinedAttributes.get(DirectionalLightsAttribute.Type)).lights;
/* 362 */             for (int i = this.dirLightsOffset; i < lights.size; i++) {
/* 363 */               this.cacheAmbientCubemap.add(((DirectionalLight)lights.get(i)).color, ((DirectionalLight)lights.get(i)).direction);
/*     */             }
/*     */           } 
/* 366 */           if (combinedAttributes.has(PointLightsAttribute.Type)) {
/* 367 */             Array<PointLight> lights = ((PointLightsAttribute)combinedAttributes.get(PointLightsAttribute.Type)).lights;
/* 368 */             for (int i = this.pointLightsOffset; i < lights.size; i++) {
/* 369 */               this.cacheAmbientCubemap.add(((PointLight)lights.get(i)).color, ((PointLight)lights.get(i)).position, tmpV1, ((PointLight)lights.get(i)).intensity);
/*     */             }
/*     */           } 
/* 372 */           this.cacheAmbientCubemap.clamp();
/* 373 */           shader.program.setUniform3fv(shader.loc(inputID), this.cacheAmbientCubemap.data, 0, this.cacheAmbientCubemap.data.length);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/* 378 */     public static final BaseShader.Setter environmentCubemap = new BaseShader.LocalSetter()
/*     */       {
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 381 */           if (combinedAttributes.has(CubemapAttribute.EnvironmentMap))
/* 382 */             shader.set(inputID, shader.context.textureBinder.bind(((CubemapAttribute)combinedAttributes
/* 383 */                   .get(CubemapAttribute.EnvironmentMap)).textureDescription)); 
/*     */         }
/*     */       }; } public static class Bones extends BaseShader.LocalSetter { private static final Matrix4 idtMatrix = new Matrix4(); public final float[] bones; public Bones(int numBones) { this.bones = new float[numBones * 16]; }
/*     */     public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) { for (int i = 0; i < this.bones.length; i++) { int idx = i / 16; this.bones[i] = (renderable.bones == null || idx >= renderable.bones.length || renderable.bones[idx] == null) ? idtMatrix.val[i % 16] : (renderable.bones[idx]).val[i % 16]; }  shader.program.setUniformMatrix4fv(shader.loc(inputID), this.bones, 0, this.bones.length); } }
/*     */   public static class ACubemap extends BaseShader.LocalSetter { private static final float[] ones = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F }; private final AmbientCubemap cacheAmbientCubemap = new AmbientCubemap(); private static final Vector3 tmpV1 = new Vector3(); public final int dirLightsOffset; public final int pointLightsOffset;
/*     */     public ACubemap(int dirLightsOffset, int pointLightsOffset) { this.dirLightsOffset = dirLightsOffset; this.pointLightsOffset = pointLightsOffset; }
/* 389 */     public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) { if (renderable.environment == null) { shader.program.setUniform3fv(shader.loc(inputID), ones, 0, ones.length); } else { renderable.worldTransform.getTranslation(tmpV1); if (combinedAttributes.has(ColorAttribute.AmbientLight)) this.cacheAmbientCubemap.set(((ColorAttribute)combinedAttributes.get(ColorAttribute.AmbientLight)).color);  if (combinedAttributes.has(DirectionalLightsAttribute.Type)) { Array<DirectionalLight> lights = ((DirectionalLightsAttribute)combinedAttributes.get(DirectionalLightsAttribute.Type)).lights; for (int i = this.dirLightsOffset; i < lights.size; i++) this.cacheAmbientCubemap.add(((DirectionalLight)lights.get(i)).color, ((DirectionalLight)lights.get(i)).direction);  }  if (combinedAttributes.has(PointLightsAttribute.Type)) { Array<PointLight> lights = ((PointLightsAttribute)combinedAttributes.get(PointLightsAttribute.Type)).lights; for (int i = this.pointLightsOffset; i < lights.size; i++) this.cacheAmbientCubemap.add(((PointLight)lights.get(i)).color, ((PointLight)lights.get(i)).position, tmpV1, ((PointLight)lights.get(i)).intensity);  }  this.cacheAmbientCubemap.clamp(); shader.program.setUniform3fv(shader.loc(inputID), this.cacheAmbientCubemap.data, 0, this.cacheAmbientCubemap.data.length); }  } } private static String defaultVertexShader = null;
/*     */   
/*     */   public static String getDefaultVertexShader() {
/* 392 */     if (defaultVertexShader == null)
/* 393 */       defaultVertexShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/shaders/default.vertex.glsl").readString(); 
/* 394 */     return defaultVertexShader;
/*     */   }
/*     */   
/* 397 */   private static String defaultFragmentShader = null;
/*     */   
/*     */   public static String getDefaultFragmentShader() {
/* 400 */     if (defaultFragmentShader == null)
/* 401 */       defaultFragmentShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/shaders/default.fragment.glsl").readString(); 
/* 402 */     return defaultFragmentShader;
/*     */   }
/*     */   
/* 405 */   protected static long implementedFlags = BlendingAttribute.Type | TextureAttribute.Diffuse | ColorAttribute.Diffuse | ColorAttribute.Specular | FloatAttribute.Shininess;
/*     */ 
/*     */   
/*     */   @Deprecated
/* 409 */   public static int defaultCullFace = 1029;
/*     */   @Deprecated
/* 411 */   public static int defaultDepthFunc = 515;
/*     */   
/*     */   public final int u_projTrans;
/*     */   
/*     */   public final int u_viewTrans;
/*     */   
/*     */   public final int u_projViewTrans;
/*     */   
/*     */   public final int u_cameraPosition;
/*     */   
/*     */   public final int u_cameraDirection;
/*     */   public final int u_cameraUp;
/*     */   public final int u_cameraNearFar;
/*     */   public final int u_time;
/*     */   public final int u_worldTrans;
/*     */   public final int u_viewWorldTrans;
/*     */   public final int u_projViewWorldTrans;
/*     */   public final int u_normalMatrix;
/*     */   public final int u_bones;
/*     */   public final int u_shininess;
/*     */   public final int u_opacity;
/*     */   public final int u_diffuseColor;
/*     */   public final int u_diffuseTexture;
/*     */   public final int u_diffuseUVTransform;
/*     */   public final int u_specularColor;
/*     */   public final int u_specularTexture;
/*     */   public final int u_specularUVTransform;
/*     */   public final int u_emissiveColor;
/*     */   public final int u_emissiveTexture;
/*     */   public final int u_emissiveUVTransform;
/*     */   public final int u_reflectionColor;
/*     */   public final int u_reflectionTexture;
/*     */   public final int u_reflectionUVTransform;
/*     */   public final int u_normalTexture;
/*     */   public final int u_normalUVTransform;
/*     */   public final int u_ambientTexture;
/*     */   public final int u_ambientUVTransform;
/*     */   public final int u_alphaTest;
/*     */   protected final int u_ambientCubemap;
/*     */   protected final int u_environmentCubemap;
/* 451 */   protected final int u_dirLights0color = register(new BaseShader.Uniform("u_dirLights[0].color"));
/* 452 */   protected final int u_dirLights0direction = register(new BaseShader.Uniform("u_dirLights[0].direction"));
/* 453 */   protected final int u_dirLights1color = register(new BaseShader.Uniform("u_dirLights[1].color"));
/* 454 */   protected final int u_pointLights0color = register(new BaseShader.Uniform("u_pointLights[0].color"));
/* 455 */   protected final int u_pointLights0position = register(new BaseShader.Uniform("u_pointLights[0].position"));
/* 456 */   protected final int u_pointLights0intensity = register(new BaseShader.Uniform("u_pointLights[0].intensity"));
/* 457 */   protected final int u_pointLights1color = register(new BaseShader.Uniform("u_pointLights[1].color"));
/* 458 */   protected final int u_spotLights0color = register(new BaseShader.Uniform("u_spotLights[0].color"));
/* 459 */   protected final int u_spotLights0position = register(new BaseShader.Uniform("u_spotLights[0].position"));
/* 460 */   protected final int u_spotLights0intensity = register(new BaseShader.Uniform("u_spotLights[0].intensity"));
/* 461 */   protected final int u_spotLights0direction = register(new BaseShader.Uniform("u_spotLights[0].direction"));
/* 462 */   protected final int u_spotLights0cutoffAngle = register(new BaseShader.Uniform("u_spotLights[0].cutoffAngle"));
/* 463 */   protected final int u_spotLights0exponent = register(new BaseShader.Uniform("u_spotLights[0].exponent"));
/* 464 */   protected final int u_spotLights1color = register(new BaseShader.Uniform("u_spotLights[1].color"));
/* 465 */   protected final int u_fogColor = register(new BaseShader.Uniform("u_fogColor"));
/* 466 */   protected final int u_shadowMapProjViewTrans = register(new BaseShader.Uniform("u_shadowMapProjViewTrans"));
/* 467 */   protected final int u_shadowTexture = register(new BaseShader.Uniform("u_shadowTexture"));
/* 468 */   protected final int u_shadowPCFOffset = register(new BaseShader.Uniform("u_shadowPCFOffset"));
/*     */   
/*     */   protected int dirLightsLoc;
/*     */   
/*     */   protected int dirLightsColorOffset;
/*     */   
/*     */   protected int dirLightsDirectionOffset;
/*     */   protected int dirLightsSize;
/*     */   protected int pointLightsLoc;
/*     */   protected int pointLightsColorOffset;
/*     */   protected int pointLightsPositionOffset;
/*     */   protected int pointLightsIntensityOffset;
/*     */   protected int pointLightsSize;
/*     */   protected int spotLightsLoc;
/*     */   protected int spotLightsColorOffset;
/*     */   protected int spotLightsPositionOffset;
/*     */   protected int spotLightsDirectionOffset;
/*     */   protected int spotLightsIntensityOffset;
/*     */   protected int spotLightsCutoffAngleOffset;
/*     */   protected int spotLightsExponentOffset;
/*     */   protected int spotLightsSize;
/*     */   protected final boolean lighting;
/*     */   protected final boolean environmentCubemap;
/*     */   protected final boolean shadowMap;
/* 492 */   protected final AmbientCubemap ambientCubemap = new AmbientCubemap();
/*     */   
/*     */   protected final DirectionalLight[] directionalLights;
/*     */   
/*     */   protected final PointLight[] pointLights;
/*     */   
/*     */   protected final SpotLight[] spotLights;
/*     */   
/*     */   private Renderable renderable;
/*     */   protected final long attributesMask;
/*     */   private final long vertexMask;
/*     */   protected final Config config;
/* 504 */   private static final long optionalAttributes = IntAttribute.CullFace | DepthTestAttribute.Type;
/*     */   
/*     */   public DefaultShader(Renderable renderable) {
/* 507 */     this(renderable, new Config());
/*     */   }
/*     */   
/*     */   public DefaultShader(Renderable renderable, Config config) {
/* 511 */     this(renderable, config, createPrefix(renderable, config));
/*     */   }
/*     */   
/*     */   public DefaultShader(Renderable renderable, Config config, String prefix) {
/* 515 */     this(renderable, config, prefix, (config.vertexShader != null) ? config.vertexShader : getDefaultVertexShader(), (config.fragmentShader != null) ? config.fragmentShader : 
/* 516 */         getDefaultFragmentShader());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultShader(Renderable renderable, Config config, String prefix, String vertexShader, String fragmentShader) {
/* 521 */     this(renderable, config, new ShaderProgram(prefix + vertexShader, prefix + fragmentShader));
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
/*     */   public void init() {
/* 593 */     ShaderProgram program = this.program;
/* 594 */     this.program = null;
/* 595 */     init(program, this.renderable);
/* 596 */     this.renderable = null;
/*     */     
/* 598 */     this.dirLightsLoc = loc(this.u_dirLights0color);
/* 599 */     this.dirLightsColorOffset = loc(this.u_dirLights0color) - this.dirLightsLoc;
/* 600 */     this.dirLightsDirectionOffset = loc(this.u_dirLights0direction) - this.dirLightsLoc;
/* 601 */     this.dirLightsSize = loc(this.u_dirLights1color) - this.dirLightsLoc;
/* 602 */     if (this.dirLightsSize < 0) this.dirLightsSize = 0;
/*     */     
/* 604 */     this.pointLightsLoc = loc(this.u_pointLights0color);
/* 605 */     this.pointLightsColorOffset = loc(this.u_pointLights0color) - this.pointLightsLoc;
/* 606 */     this.pointLightsPositionOffset = loc(this.u_pointLights0position) - this.pointLightsLoc;
/* 607 */     this.pointLightsIntensityOffset = has(this.u_pointLights0intensity) ? (loc(this.u_pointLights0intensity) - this.pointLightsLoc) : -1;
/* 608 */     this.pointLightsSize = loc(this.u_pointLights1color) - this.pointLightsLoc;
/* 609 */     if (this.pointLightsSize < 0) this.pointLightsSize = 0;
/*     */     
/* 611 */     this.spotLightsLoc = loc(this.u_spotLights0color);
/* 612 */     this.spotLightsColorOffset = loc(this.u_spotLights0color) - this.spotLightsLoc;
/* 613 */     this.spotLightsPositionOffset = loc(this.u_spotLights0position) - this.spotLightsLoc;
/* 614 */     this.spotLightsDirectionOffset = loc(this.u_spotLights0direction) - this.spotLightsLoc;
/* 615 */     this.spotLightsIntensityOffset = has(this.u_spotLights0intensity) ? (loc(this.u_spotLights0intensity) - this.spotLightsLoc) : -1;
/* 616 */     this.spotLightsCutoffAngleOffset = loc(this.u_spotLights0cutoffAngle) - this.spotLightsLoc;
/* 617 */     this.spotLightsExponentOffset = loc(this.u_spotLights0exponent) - this.spotLightsLoc;
/* 618 */     this.spotLightsSize = loc(this.u_spotLights1color) - this.spotLightsLoc;
/* 619 */     if (this.spotLightsSize < 0) this.spotLightsSize = 0; 
/*     */   }
/*     */   
/*     */   private static final boolean and(long mask, long flag) {
/* 623 */     return ((mask & flag) == flag);
/*     */   }
/*     */   
/*     */   private static final boolean or(long mask, long flag) {
/* 627 */     return ((mask & flag) != 0L);
/*     */   }
/*     */   
/* 630 */   private static final Attributes tmpAttributes = new Attributes(); private final Matrix3 normalMatrix; private float time; private boolean lightsSet;
/*     */   private final Vector3 tmpV1;
/*     */   
/*     */   private static final Attributes combineAttributes(Renderable renderable) {
/* 634 */     tmpAttributes.clear();
/* 635 */     if (renderable.environment != null) tmpAttributes.set((Iterable)renderable.environment); 
/* 636 */     if (renderable.material != null) tmpAttributes.set((Iterable)renderable.material); 
/* 637 */     return tmpAttributes;
/*     */   }
/*     */   
/*     */   public static String createPrefix(Renderable renderable, Config config) {
/* 641 */     Attributes attributes = combineAttributes(renderable);
/* 642 */     String prefix = "";
/* 643 */     long attributesMask = attributes.getMask();
/* 644 */     long vertexMask = renderable.meshPart.mesh.getVertexAttributes().getMask();
/* 645 */     if (and(vertexMask, 1L)) prefix = prefix + "#define positionFlag\n"; 
/* 646 */     if (or(vertexMask, 6L)) prefix = prefix + "#define colorFlag\n"; 
/* 647 */     if (and(vertexMask, 256L)) prefix = prefix + "#define binormalFlag\n"; 
/* 648 */     if (and(vertexMask, 128L)) prefix = prefix + "#define tangentFlag\n"; 
/* 649 */     if (and(vertexMask, 8L)) prefix = prefix + "#define normalFlag\n"; 
/* 650 */     if ((and(vertexMask, 8L) || and(vertexMask, 384L)) && 
/* 651 */       renderable.environment != null) {
/* 652 */       prefix = prefix + "#define lightingFlag\n";
/* 653 */       prefix = prefix + "#define ambientCubemapFlag\n";
/* 654 */       prefix = prefix + "#define numDirectionalLights " + config.numDirectionalLights + "\n";
/* 655 */       prefix = prefix + "#define numPointLights " + config.numPointLights + "\n";
/* 656 */       prefix = prefix + "#define numSpotLights " + config.numSpotLights + "\n";
/* 657 */       if (attributes.has(ColorAttribute.Fog)) {
/* 658 */         prefix = prefix + "#define fogFlag\n";
/*     */       }
/* 660 */       if (renderable.environment.shadowMap != null) prefix = prefix + "#define shadowMapFlag\n"; 
/* 661 */       if (attributes.has(CubemapAttribute.EnvironmentMap)) prefix = prefix + "#define environmentCubemapFlag\n";
/*     */     
/*     */     } 
/* 664 */     int n = renderable.meshPart.mesh.getVertexAttributes().size();
/* 665 */     for (int i = 0; i < n; i++) {
/* 666 */       VertexAttribute attr = renderable.meshPart.mesh.getVertexAttributes().get(i);
/* 667 */       if (attr.usage == 64)
/* 668 */       { prefix = prefix + "#define boneWeight" + attr.unit + "Flag\n"; }
/* 669 */       else if (attr.usage == 16) { prefix = prefix + "#define texCoord" + attr.unit + "Flag\n"; }
/*     */     
/* 671 */     }  if ((attributesMask & BlendingAttribute.Type) == BlendingAttribute.Type)
/* 672 */       prefix = prefix + "#define blendedFlag\n"; 
/* 673 */     if ((attributesMask & TextureAttribute.Diffuse) == TextureAttribute.Diffuse) {
/* 674 */       prefix = prefix + "#define diffuseTextureFlag\n";
/* 675 */       prefix = prefix + "#define diffuseTextureCoord texCoord0\n";
/*     */     } 
/* 677 */     if ((attributesMask & TextureAttribute.Specular) == TextureAttribute.Specular) {
/* 678 */       prefix = prefix + "#define specularTextureFlag\n";
/* 679 */       prefix = prefix + "#define specularTextureCoord texCoord0\n";
/*     */     } 
/* 681 */     if ((attributesMask & TextureAttribute.Normal) == TextureAttribute.Normal) {
/* 682 */       prefix = prefix + "#define normalTextureFlag\n";
/* 683 */       prefix = prefix + "#define normalTextureCoord texCoord0\n";
/*     */     } 
/* 685 */     if ((attributesMask & TextureAttribute.Emissive) == TextureAttribute.Emissive) {
/* 686 */       prefix = prefix + "#define emissiveTextureFlag\n";
/* 687 */       prefix = prefix + "#define emissiveTextureCoord texCoord0\n";
/*     */     } 
/* 689 */     if ((attributesMask & TextureAttribute.Reflection) == TextureAttribute.Reflection) {
/* 690 */       prefix = prefix + "#define reflectionTextureFlag\n";
/* 691 */       prefix = prefix + "#define reflectionTextureCoord texCoord0\n";
/*     */     } 
/* 693 */     if ((attributesMask & TextureAttribute.Ambient) == TextureAttribute.Ambient) {
/* 694 */       prefix = prefix + "#define ambientTextureFlag\n";
/* 695 */       prefix = prefix + "#define ambientTextureCoord texCoord0\n";
/*     */     } 
/* 697 */     if ((attributesMask & ColorAttribute.Diffuse) == ColorAttribute.Diffuse)
/* 698 */       prefix = prefix + "#define diffuseColorFlag\n"; 
/* 699 */     if ((attributesMask & ColorAttribute.Specular) == ColorAttribute.Specular)
/* 700 */       prefix = prefix + "#define specularColorFlag\n"; 
/* 701 */     if ((attributesMask & ColorAttribute.Emissive) == ColorAttribute.Emissive)
/* 702 */       prefix = prefix + "#define emissiveColorFlag\n"; 
/* 703 */     if ((attributesMask & ColorAttribute.Reflection) == ColorAttribute.Reflection)
/* 704 */       prefix = prefix + "#define reflectionColorFlag\n"; 
/* 705 */     if ((attributesMask & FloatAttribute.Shininess) == FloatAttribute.Shininess)
/* 706 */       prefix = prefix + "#define shininessFlag\n"; 
/* 707 */     if ((attributesMask & FloatAttribute.AlphaTest) == FloatAttribute.AlphaTest)
/* 708 */       prefix = prefix + "#define alphaTestFlag\n"; 
/* 709 */     if (renderable.bones != null && config.numBones > 0) prefix = prefix + "#define numBones " + config.numBones + "\n"; 
/* 710 */     return prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRender(Renderable renderable) {
/* 715 */     Attributes attributes = combineAttributes(renderable);
/* 716 */     return (this.attributesMask == (attributes.getMask() | optionalAttributes) && this.vertexMask == renderable.meshPart.mesh
/* 717 */       .getVertexAttributes().getMask() && ((renderable.environment != null)) == this.lighting);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Shader other) {
/* 722 */     if (other == null) return -1; 
/* 723 */     if (other == this) return 0; 
/* 724 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 729 */     return (obj instanceof DefaultShader) ? equals((DefaultShader)obj) : false;
/*     */   }
/*     */   
/*     */   public boolean equals(DefaultShader obj) {
/* 733 */     return (obj == this);
/*     */   } public void begin(Camera camera, RenderContext context) { super.begin(camera, context); for (DirectionalLight dirLight : this.directionalLights) dirLight.set(0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F);  for (PointLight pointLight : this.pointLights) pointLight.set(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);  for (SpotLight spotLight : this.spotLights)
/*     */       spotLight.set(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 0.0F);  this.lightsSet = false; if (has(this.u_time))
/* 736 */       set(this.u_time, this.time += Gdx.graphics.getDeltaTime());  } public DefaultShader(Renderable renderable, Config config, ShaderProgram shaderProgram) { this.normalMatrix = new Matrix3();
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
/* 799 */     this.tmpV1 = new Vector3(); Attributes attributes = combineAttributes(renderable); this.config = config; this.program = shaderProgram; this.lighting = (renderable.environment != null); this.environmentCubemap = (attributes.has(CubemapAttribute.EnvironmentMap) || (this.lighting && attributes.has(CubemapAttribute.EnvironmentMap))); this.shadowMap = (this.lighting && renderable.environment.shadowMap != null); this.renderable = renderable; this.attributesMask = attributes.getMask() | optionalAttributes; this.vertexMask = renderable.meshPart.mesh.getVertexAttributes().getMask(); this.directionalLights = new DirectionalLight[(this.lighting && config.numDirectionalLights > 0) ? config.numDirectionalLights : 0]; int i; for (i = 0; i < this.directionalLights.length; i++) this.directionalLights[i] = new DirectionalLight();  this.pointLights = new PointLight[(this.lighting && config.numPointLights > 0) ? config.numPointLights : 0]; for (i = 0; i < this.pointLights.length; i++) this.pointLights[i] = new PointLight();  this.spotLights = new SpotLight[(this.lighting && config.numSpotLights > 0) ? config.numSpotLights : 0]; for (i = 0; i < this.spotLights.length; i++) this.spotLights[i] = new SpotLight();  if (!config.ignoreUnimplemented && (implementedFlags & this.attributesMask) != this.attributesMask) throw new GdxRuntimeException("Some attributes not implemented yet (" + this.attributesMask + ")");  this.u_projTrans = register(Inputs.projTrans, Setters.projTrans); this.u_viewTrans = register(Inputs.viewTrans, Setters.viewTrans); this.u_projViewTrans = register(Inputs.projViewTrans, Setters.projViewTrans); this.u_cameraPosition = register(Inputs.cameraPosition, Setters.cameraPosition); this.u_cameraDirection = register(Inputs.cameraDirection, Setters.cameraDirection); this.u_cameraUp = register(Inputs.cameraUp, Setters.cameraUp); this.u_cameraNearFar = register(Inputs.cameraNearFar, Setters.cameraNearFar); this.u_time = register(new BaseShader.Uniform("u_time")); this.u_worldTrans = register(Inputs.worldTrans, Setters.worldTrans); this.u_viewWorldTrans = register(Inputs.viewWorldTrans, Setters.viewWorldTrans); this.u_projViewWorldTrans = register(Inputs.projViewWorldTrans, Setters.projViewWorldTrans); this.u_normalMatrix = register(Inputs.normalMatrix, Setters.normalMatrix); this.u_bones = (renderable.bones != null && config.numBones > 0) ? register(Inputs.bones, new Setters.Bones(config.numBones)) : -1; this.u_shininess = register(Inputs.shininess, Setters.shininess); this.u_opacity = register(Inputs.opacity); this.u_diffuseColor = register(Inputs.diffuseColor, Setters.diffuseColor); this.u_diffuseTexture = register(Inputs.diffuseTexture, Setters.diffuseTexture); this.u_diffuseUVTransform = register(Inputs.diffuseUVTransform, Setters.diffuseUVTransform); this.u_specularColor = register(Inputs.specularColor, Setters.specularColor); this.u_specularTexture = register(Inputs.specularTexture, Setters.specularTexture); this.u_specularUVTransform = register(Inputs.specularUVTransform, Setters.specularUVTransform); this.u_emissiveColor = register(Inputs.emissiveColor, Setters.emissiveColor); this.u_emissiveTexture = register(Inputs.emissiveTexture, Setters.emissiveTexture); this.u_emissiveUVTransform = register(Inputs.emissiveUVTransform, Setters.emissiveUVTransform); this.u_reflectionColor = register(Inputs.reflectionColor, Setters.reflectionColor); this.u_reflectionTexture = register(Inputs.reflectionTexture, Setters.reflectionTexture); this.u_reflectionUVTransform = register(Inputs.reflectionUVTransform, Setters.reflectionUVTransform); this.u_normalTexture = register(Inputs.normalTexture, Setters.normalTexture); this.u_normalUVTransform = register(Inputs.normalUVTransform, Setters.normalUVTransform); this.u_ambientTexture = register(Inputs.ambientTexture, Setters.ambientTexture); this.u_ambientUVTransform = register(Inputs.ambientUVTransform, Setters.ambientUVTransform); this.u_alphaTest = register(Inputs.alphaTest); this.u_ambientCubemap = this.lighting ? register(Inputs.ambientCube, new Setters.ACubemap(config.numDirectionalLights, config.numPointLights)) : -1; this.u_environmentCubemap = this.environmentCubemap ? register(Inputs.environmentCubemap, Setters.environmentCubemap) : -1; }
/*     */   public void render(Renderable renderable, Attributes combinedAttributes) { if (!combinedAttributes.has(BlendingAttribute.Type))
/*     */       this.context.setBlending(false, 770, 771);  bindMaterial(combinedAttributes); if (this.lighting)
/* 802 */       bindLights(renderable, combinedAttributes);  super.render(renderable, combinedAttributes); } protected void bindLights(Renderable renderable, Attributes attributes) { Environment lights = renderable.environment;
/* 803 */     DirectionalLightsAttribute dla = (DirectionalLightsAttribute)attributes.get(DirectionalLightsAttribute.class, DirectionalLightsAttribute.Type);
/* 804 */     Array<DirectionalLight> dirs = (dla == null) ? null : dla.lights;
/* 805 */     PointLightsAttribute pla = (PointLightsAttribute)attributes.get(PointLightsAttribute.class, PointLightsAttribute.Type);
/* 806 */     Array<PointLight> points = (pla == null) ? null : pla.lights;
/* 807 */     SpotLightsAttribute sla = (SpotLightsAttribute)attributes.get(SpotLightsAttribute.class, SpotLightsAttribute.Type);
/* 808 */     Array<SpotLight> spots = (sla == null) ? null : sla.lights;
/*     */     
/* 810 */     if (this.dirLightsLoc >= 0)
/* 811 */       for (int i = 0; i < this.directionalLights.length; i++) {
/* 812 */         if (dirs == null || i >= dirs.size)
/* 813 */         { if (this.lightsSet && (this.directionalLights[i]).color.r == 0.0F && (this.directionalLights[i]).color.g == 0.0F && (this.directionalLights[i]).color.b == 0.0F)
/*     */             continue; 
/* 815 */           (this.directionalLights[i]).color.set(0.0F, 0.0F, 0.0F, 1.0F); }
/* 816 */         else { if (this.lightsSet && this.directionalLights[i].equals((DirectionalLight)dirs.get(i))) {
/*     */             continue;
/*     */           }
/* 819 */           this.directionalLights[i].set((DirectionalLight)dirs.get(i)); }
/*     */         
/* 821 */         int idx = this.dirLightsLoc + i * this.dirLightsSize;
/* 822 */         this.program.setUniformf(idx + this.dirLightsColorOffset, (this.directionalLights[i]).color.r, (this.directionalLights[i]).color.g, (this.directionalLights[i]).color.b);
/*     */         
/* 824 */         this.program.setUniformf(idx + this.dirLightsDirectionOffset, (this.directionalLights[i]).direction.x, (this.directionalLights[i]).direction.y, (this.directionalLights[i]).direction.z);
/*     */         
/* 826 */         if (this.dirLightsSize <= 0)
/*     */           break; 
/*     */         continue;
/*     */       }  
/* 830 */     if (this.pointLightsLoc >= 0)
/* 831 */       for (int i = 0; i < this.pointLights.length; i++) {
/* 832 */         if (points == null || i >= points.size)
/* 833 */         { if (this.lightsSet && (this.pointLights[i]).intensity == 0.0F)
/* 834 */             continue;  (this.pointLights[i]).intensity = 0.0F; }
/* 835 */         else { if (this.lightsSet && this.pointLights[i].equals((PointLight)points.get(i))) {
/*     */             continue;
/*     */           }
/* 838 */           this.pointLights[i].set((PointLight)points.get(i)); }
/*     */         
/* 840 */         int idx = this.pointLightsLoc + i * this.pointLightsSize;
/* 841 */         this.program.setUniformf(idx + this.pointLightsColorOffset, (this.pointLights[i]).color.r * (this.pointLights[i]).intensity, (this.pointLights[i]).color.g * (this.pointLights[i]).intensity, (this.pointLights[i]).color.b * (this.pointLights[i]).intensity);
/*     */         
/* 843 */         this.program.setUniformf(idx + this.pointLightsPositionOffset, (this.pointLights[i]).position.x, (this.pointLights[i]).position.y, (this.pointLights[i]).position.z);
/*     */         
/* 845 */         if (this.pointLightsIntensityOffset >= 0) this.program.setUniformf(idx + this.pointLightsIntensityOffset, (this.pointLights[i]).intensity); 
/* 846 */         if (this.pointLightsSize <= 0)
/*     */           break; 
/*     */         continue;
/*     */       }  
/* 850 */     if (this.spotLightsLoc >= 0)
/* 851 */       for (int i = 0; i < this.spotLights.length; i++) {
/* 852 */         if (spots == null || i >= spots.size)
/* 853 */         { if (this.lightsSet && (this.spotLights[i]).intensity == 0.0F)
/* 854 */             continue;  (this.spotLights[i]).intensity = 0.0F; }
/* 855 */         else { if (this.lightsSet && this.spotLights[i].equals((SpotLight)spots.get(i))) {
/*     */             continue;
/*     */           }
/* 858 */           this.spotLights[i].set((SpotLight)spots.get(i)); }
/*     */         
/* 860 */         int idx = this.spotLightsLoc + i * this.spotLightsSize;
/* 861 */         this.program.setUniformf(idx + this.spotLightsColorOffset, (this.spotLights[i]).color.r * (this.spotLights[i]).intensity, (this.spotLights[i]).color.g * (this.spotLights[i]).intensity, (this.spotLights[i]).color.b * (this.spotLights[i]).intensity);
/*     */         
/* 863 */         this.program.setUniformf(idx + this.spotLightsPositionOffset, (this.spotLights[i]).position);
/* 864 */         this.program.setUniformf(idx + this.spotLightsDirectionOffset, (this.spotLights[i]).direction);
/* 865 */         this.program.setUniformf(idx + this.spotLightsCutoffAngleOffset, (this.spotLights[i]).cutoffAngle);
/* 866 */         this.program.setUniformf(idx + this.spotLightsExponentOffset, (this.spotLights[i]).exponent);
/* 867 */         if (this.spotLightsIntensityOffset >= 0)
/* 868 */           this.program.setUniformf(idx + this.spotLightsIntensityOffset, (this.spotLights[i]).intensity); 
/* 869 */         if (this.spotLightsSize <= 0)
/*     */           break; 
/*     */         continue;
/*     */       }  
/* 873 */     if (attributes.has(ColorAttribute.Fog)) {
/* 874 */       set(this.u_fogColor, ((ColorAttribute)attributes.get(ColorAttribute.Fog)).color);
/*     */     }
/*     */     
/* 877 */     if (lights != null && lights.shadowMap != null) {
/* 878 */       set(this.u_shadowMapProjViewTrans, lights.shadowMap.getProjViewTrans());
/* 879 */       set(this.u_shadowTexture, lights.shadowMap.getDepthMap());
/* 880 */       set(this.u_shadowPCFOffset, 1.0F / 2.0F * (lights.shadowMap.getDepthMap()).texture.getWidth());
/*     */     } 
/*     */     
/* 883 */     this.lightsSet = true; } public void end() { super.end(); }
/*     */   protected void bindMaterial(Attributes attributes) { int cullFace = (this.config.defaultCullFace == -1) ? defaultCullFace : this.config.defaultCullFace; int depthFunc = (this.config.defaultDepthFunc == -1) ? defaultDepthFunc : this.config.defaultDepthFunc; float depthRangeNear = 0.0F; float depthRangeFar = 1.0F; boolean depthMask = true; for (Attribute attr : attributes) { long t = attr.type; if (BlendingAttribute.is(t)) { this.context.setBlending(true, ((BlendingAttribute)attr).sourceFunction, ((BlendingAttribute)attr).destFunction); set(this.u_opacity, ((BlendingAttribute)attr).opacity); continue; }  if ((t & IntAttribute.CullFace) == IntAttribute.CullFace) { cullFace = ((IntAttribute)attr).value; continue; }  if ((t & FloatAttribute.AlphaTest) == FloatAttribute.AlphaTest) { set(this.u_alphaTest, ((FloatAttribute)attr).value); continue; }  if ((t & DepthTestAttribute.Type) == DepthTestAttribute.Type) { DepthTestAttribute dta = (DepthTestAttribute)attr; depthFunc = dta.depthFunc; depthRangeNear = dta.depthRangeNear; depthRangeFar = dta.depthRangeFar; depthMask = dta.depthMask; continue; }
/*     */        if (!this.config.ignoreUnimplemented)
/*     */         throw new GdxRuntimeException("Unknown material attribute: " + attr.toString());  }
/*     */      this.context.setCullFace(cullFace); this.context.setDepthTest(depthFunc, depthRangeNear, depthRangeFar); this.context.setDepthMask(depthMask); }
/* 888 */   public void dispose() { this.program.dispose();
/* 889 */     super.dispose(); }
/*     */ 
/*     */   
/*     */   public int getDefaultCullFace() {
/* 893 */     return (this.config.defaultCullFace == -1) ? defaultCullFace : this.config.defaultCullFace;
/*     */   }
/*     */   
/*     */   public void setDefaultCullFace(int cullFace) {
/* 897 */     this.config.defaultCullFace = cullFace;
/*     */   }
/*     */   
/*     */   public int getDefaultDepthFunc() {
/* 901 */     return (this.config.defaultDepthFunc == -1) ? defaultDepthFunc : this.config.defaultDepthFunc;
/*     */   }
/*     */   
/*     */   public void setDefaultDepthFunc(int depthFunc) {
/* 905 */     this.config.defaultDepthFunc = depthFunc;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\shaders\DefaultShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */