/*     */ package com.badlogic.gdx.graphics.g3d.shaders;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Attributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.Shader;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.math.Matrix3;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.IntIntMap;
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
/*     */ public abstract class BaseShader
/*     */   implements Shader
/*     */ {
/*     */   public static interface Validator
/*     */   {
/*     */     boolean validate(BaseShader param1BaseShader, int param1Int, Renderable param1Renderable);
/*     */   }
/*     */   
/*     */   public static interface Setter
/*     */   {
/*     */     boolean isGlobal(BaseShader param1BaseShader, int param1Int);
/*     */     
/*     */     void set(BaseShader param1BaseShader, int param1Int, Renderable param1Renderable, Attributes param1Attributes);
/*     */   }
/*     */   
/*     */   public static abstract class GlobalSetter
/*     */     implements Setter
/*     */   {
/*     */     public boolean isGlobal(BaseShader shader, int inputID) {
/*  58 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class LocalSetter
/*     */     implements Setter {
/*     */     public boolean isGlobal(BaseShader shader, int inputID) {
/*  65 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Uniform implements Validator {
/*     */     public final String alias;
/*     */     public final long materialMask;
/*     */     public final long environmentMask;
/*     */     public final long overallMask;
/*     */     
/*     */     public Uniform(String alias, long materialMask, long environmentMask, long overallMask) {
/*  76 */       this.alias = alias;
/*  77 */       this.materialMask = materialMask;
/*  78 */       this.environmentMask = environmentMask;
/*  79 */       this.overallMask = overallMask;
/*     */     }
/*     */     
/*     */     public Uniform(String alias, long materialMask, long environmentMask) {
/*  83 */       this(alias, materialMask, environmentMask, 0L);
/*     */     }
/*     */     
/*     */     public Uniform(String alias, long overallMask) {
/*  87 */       this(alias, 0L, 0L, overallMask);
/*     */     }
/*     */     
/*     */     public Uniform(String alias) {
/*  91 */       this(alias, 0L, 0L);
/*     */     }
/*     */     
/*     */     public boolean validate(BaseShader shader, int inputID, Renderable renderable) {
/*  95 */       long matFlags = (renderable != null && renderable.material != null) ? renderable.material.getMask() : 0L;
/*  96 */       long envFlags = (renderable != null && renderable.environment != null) ? renderable.environment.getMask() : 0L;
/*  97 */       return ((matFlags & this.materialMask) == this.materialMask && (envFlags & this.environmentMask) == this.environmentMask && ((matFlags | envFlags) & this.overallMask) == this.overallMask);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 102 */   private final Array<String> uniforms = new Array();
/* 103 */   private final Array<Validator> validators = new Array();
/* 104 */   private final Array<Setter> setters = new Array();
/*     */   private int[] locations;
/* 106 */   private final IntArray globalUniforms = new IntArray();
/* 107 */   private final IntArray localUniforms = new IntArray();
/* 108 */   private final IntIntMap attributes = new IntIntMap();
/*     */   
/*     */   public ShaderProgram program;
/*     */   
/*     */   public RenderContext context;
/*     */   
/*     */   public Camera camera;
/*     */   private Mesh currentMesh;
/*     */   
/*     */   public int register(String alias, Validator validator, Setter setter) {
/* 118 */     if (this.locations != null) throw new GdxRuntimeException("Cannot register an uniform after initialization"); 
/* 119 */     int existing = getUniformID(alias);
/* 120 */     if (existing >= 0) {
/* 121 */       this.validators.set(existing, validator);
/* 122 */       this.setters.set(existing, setter);
/* 123 */       return existing;
/*     */     } 
/* 125 */     this.uniforms.add(alias);
/* 126 */     this.validators.add(validator);
/* 127 */     this.setters.add(setter);
/* 128 */     return this.uniforms.size - 1;
/*     */   }
/*     */   
/*     */   public int register(String alias, Validator validator) {
/* 132 */     return register(alias, validator, null);
/*     */   }
/*     */   
/*     */   public int register(String alias, Setter setter) {
/* 136 */     return register(alias, null, setter);
/*     */   }
/*     */   
/*     */   public int register(String alias) {
/* 140 */     return register(alias, null, null);
/*     */   }
/*     */   
/*     */   public int register(Uniform uniform, Setter setter) {
/* 144 */     return register(uniform.alias, uniform, setter);
/*     */   }
/*     */   
/*     */   public int register(Uniform uniform) {
/* 148 */     return register(uniform, (Setter)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUniformID(String alias) {
/* 153 */     int n = this.uniforms.size;
/* 154 */     for (int i = 0; i < n; i++) {
/* 155 */       if (((String)this.uniforms.get(i)).equals(alias)) return i; 
/* 156 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUniformAlias(int id) {
/* 161 */     return (String)this.uniforms.get(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShaderProgram program, Renderable renderable) {
/* 166 */     if (this.locations != null) throw new GdxRuntimeException("Already initialized"); 
/* 167 */     if (!program.isCompiled()) throw new GdxRuntimeException(program.getLog()); 
/* 168 */     this.program = program;
/*     */     
/* 170 */     int n = this.uniforms.size;
/* 171 */     this.locations = new int[n];
/* 172 */     for (int i = 0; i < n; i++) {
/* 173 */       String input = (String)this.uniforms.get(i);
/* 174 */       Validator validator = (Validator)this.validators.get(i);
/* 175 */       Setter setter = (Setter)this.setters.get(i);
/* 176 */       if (validator != null && !validator.validate(this, i, renderable)) {
/* 177 */         this.locations[i] = -1;
/*     */       } else {
/* 179 */         this.locations[i] = program.fetchUniformLocation(input, false);
/* 180 */         if (this.locations[i] >= 0 && setter != null)
/* 181 */           if (setter.isGlobal(this, i)) {
/* 182 */             this.globalUniforms.add(i);
/*     */           } else {
/* 184 */             this.localUniforms.add(i);
/*     */           }  
/*     */       } 
/* 187 */       if (this.locations[i] < 0) {
/* 188 */         this.validators.set(i, null);
/* 189 */         this.setters.set(i, null);
/*     */       } 
/*     */     } 
/* 192 */     if (renderable != null) {
/* 193 */       VertexAttributes attrs = renderable.meshPart.mesh.getVertexAttributes();
/* 194 */       int c = attrs.size();
/* 195 */       for (int j = 0; j < c; j++) {
/* 196 */         VertexAttribute attr = attrs.get(j);
/* 197 */         int location = program.getAttributeLocation(attr.alias);
/* 198 */         if (location >= 0) this.attributes.put(attr.getKey(), location);
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void begin(Camera camera, RenderContext context) {
/* 205 */     this.camera = camera;
/* 206 */     this.context = context;
/* 207 */     this.program.begin();
/* 208 */     this.currentMesh = null;
/* 209 */     for (int i = 0; i < this.globalUniforms.size; i++) {
/* 210 */       int u; if (this.setters.get(u = this.globalUniforms.get(i)) != null) ((Setter)this.setters.get(u)).set(this, u, null, null); 
/*     */     } 
/*     */   }
/* 213 */   private final IntArray tempArray = new IntArray();
/*     */   
/*     */   private final int[] getAttributeLocations(VertexAttributes attrs) {
/* 216 */     this.tempArray.clear();
/* 217 */     int n = attrs.size();
/* 218 */     for (int i = 0; i < n; i++) {
/* 219 */       this.tempArray.add(this.attributes.get(attrs.get(i).getKey(), -1));
/*     */     }
/* 221 */     return this.tempArray.items;
/*     */   }
/*     */   
/* 224 */   private Attributes combinedAttributes = new Attributes();
/*     */ 
/*     */   
/*     */   public void render(Renderable renderable) {
/* 228 */     if (renderable.worldTransform.det3x3() == 0.0F)
/* 229 */       return;  this.combinedAttributes.clear();
/* 230 */     if (renderable.environment != null) this.combinedAttributes.set((Iterable)renderable.environment); 
/* 231 */     if (renderable.material != null) this.combinedAttributes.set((Iterable)renderable.material); 
/* 232 */     render(renderable, this.combinedAttributes);
/*     */   }
/*     */   
/*     */   public void render(Renderable renderable, Attributes combinedAttributes) {
/* 236 */     for (int i = 0; i < this.localUniforms.size; i++) {
/* 237 */       int u; if (this.setters.get(u = this.localUniforms.get(i)) != null) ((Setter)this.setters.get(u)).set(this, u, renderable, combinedAttributes); 
/* 238 */     }  if (this.currentMesh != renderable.meshPart.mesh) {
/* 239 */       if (this.currentMesh != null) this.currentMesh.unbind(this.program, this.tempArray.items); 
/* 240 */       this.currentMesh = renderable.meshPart.mesh;
/* 241 */       this.currentMesh.bind(this.program, getAttributeLocations(renderable.meshPart.mesh.getVertexAttributes()));
/*     */     } 
/* 243 */     renderable.meshPart.render(this.program, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 248 */     if (this.currentMesh != null) {
/* 249 */       this.currentMesh.unbind(this.program, this.tempArray.items);
/* 250 */       this.currentMesh = null;
/*     */     } 
/* 252 */     this.program.end();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 257 */     this.program = null;
/* 258 */     this.uniforms.clear();
/* 259 */     this.validators.clear();
/* 260 */     this.setters.clear();
/* 261 */     this.localUniforms.clear();
/* 262 */     this.globalUniforms.clear();
/* 263 */     this.locations = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean has(int inputID) {
/* 268 */     return (inputID >= 0 && inputID < this.locations.length && this.locations[inputID] >= 0);
/*     */   }
/*     */   
/*     */   public final int loc(int inputID) {
/* 272 */     return (inputID >= 0 && inputID < this.locations.length) ? this.locations[inputID] : -1;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, Matrix4 value) {
/* 276 */     if (this.locations[uniform] < 0) return false; 
/* 277 */     this.program.setUniformMatrix(this.locations[uniform], value);
/* 278 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, Matrix3 value) {
/* 282 */     if (this.locations[uniform] < 0) return false; 
/* 283 */     this.program.setUniformMatrix(this.locations[uniform], value);
/* 284 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, Vector3 value) {
/* 288 */     if (this.locations[uniform] < 0) return false; 
/* 289 */     this.program.setUniformf(this.locations[uniform], value);
/* 290 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, Vector2 value) {
/* 294 */     if (this.locations[uniform] < 0) return false; 
/* 295 */     this.program.setUniformf(this.locations[uniform], value);
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, Color value) {
/* 300 */     if (this.locations[uniform] < 0) return false; 
/* 301 */     this.program.setUniformf(this.locations[uniform], value);
/* 302 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, float value) {
/* 306 */     if (this.locations[uniform] < 0) return false; 
/* 307 */     this.program.setUniformf(this.locations[uniform], value);
/* 308 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, float v1, float v2) {
/* 312 */     if (this.locations[uniform] < 0) return false; 
/* 313 */     this.program.setUniformf(this.locations[uniform], v1, v2);
/* 314 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, float v1, float v2, float v3) {
/* 318 */     if (this.locations[uniform] < 0) return false; 
/* 319 */     this.program.setUniformf(this.locations[uniform], v1, v2, v3);
/* 320 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, float v1, float v2, float v3, float v4) {
/* 324 */     if (this.locations[uniform] < 0) return false; 
/* 325 */     this.program.setUniformf(this.locations[uniform], v1, v2, v3, v4);
/* 326 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, int value) {
/* 330 */     if (this.locations[uniform] < 0) return false; 
/* 331 */     this.program.setUniformi(this.locations[uniform], value);
/* 332 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, int v1, int v2) {
/* 336 */     if (this.locations[uniform] < 0) return false; 
/* 337 */     this.program.setUniformi(this.locations[uniform], v1, v2);
/* 338 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, int v1, int v2, int v3) {
/* 342 */     if (this.locations[uniform] < 0) return false; 
/* 343 */     this.program.setUniformi(this.locations[uniform], v1, v2, v3);
/* 344 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, int v1, int v2, int v3, int v4) {
/* 348 */     if (this.locations[uniform] < 0) return false; 
/* 349 */     this.program.setUniformi(this.locations[uniform], v1, v2, v3, v4);
/* 350 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, TextureDescriptor textureDesc) {
/* 354 */     if (this.locations[uniform] < 0) return false; 
/* 355 */     this.program.setUniformi(this.locations[uniform], this.context.textureBinder.bind(textureDesc));
/* 356 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean set(int uniform, GLTexture texture) {
/* 360 */     if (this.locations[uniform] < 0) return false; 
/* 361 */     this.program.setUniformi(this.locations[uniform], this.context.textureBinder.bind(texture));
/* 362 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\shaders\BaseShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */