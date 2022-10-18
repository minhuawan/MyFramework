/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Attributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Material;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.Shader;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
/*     */ import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.math.Matrix4;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleShader
/*     */   extends BaseShader
/*     */ {
/*     */   public enum ParticleType
/*     */   {
/*  45 */     Billboard, Point;
/*     */   }
/*     */   
/*     */   public enum AlignMode {
/*  49 */     Screen, ViewPoint;
/*     */   }
/*     */   
/*     */   public static class Config
/*     */   {
/*  54 */     public String vertexShader = null;
/*     */     
/*  56 */     public String fragmentShader = null;
/*     */     
/*     */     public boolean ignoreUnimplemented = true;
/*  59 */     public int defaultCullFace = -1;
/*     */     
/*  61 */     public int defaultDepthFunc = -1;
/*  62 */     public ParticleShader.AlignMode align = ParticleShader.AlignMode.Screen;
/*  63 */     public ParticleShader.ParticleType type = ParticleShader.ParticleType.Billboard;
/*     */ 
/*     */     
/*     */     public Config() {}
/*     */     
/*     */     public Config(ParticleShader.AlignMode align, ParticleShader.ParticleType type) {
/*  69 */       this.align = align;
/*  70 */       this.type = type;
/*     */     }
/*     */     
/*     */     public Config(ParticleShader.AlignMode align) {
/*  74 */       this.align = align;
/*     */     }
/*     */     
/*     */     public Config(ParticleShader.ParticleType type) {
/*  78 */       this.type = type;
/*     */     }
/*     */     
/*     */     public Config(String vertexShader, String fragmentShader) {
/*  82 */       this.vertexShader = vertexShader;
/*  83 */       this.fragmentShader = fragmentShader;
/*     */     }
/*     */   }
/*     */   
/*  87 */   private static String defaultVertexShader = null;
/*     */   
/*     */   public static String getDefaultVertexShader() {
/*  90 */     if (defaultVertexShader == null)
/*  91 */       defaultVertexShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/particles/particles.vertex.glsl").readString(); 
/*  92 */     return defaultVertexShader;
/*     */   }
/*     */   
/*  95 */   private static String defaultFragmentShader = null;
/*     */   
/*     */   public static String getDefaultFragmentShader() {
/*  98 */     if (defaultFragmentShader == null)
/*     */     {
/* 100 */       defaultFragmentShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/particles/particles.fragment.glsl").readString(); } 
/* 101 */     return defaultFragmentShader;
/*     */   }
/*     */   
/* 104 */   protected static long implementedFlags = BlendingAttribute.Type | TextureAttribute.Diffuse; private Renderable renderable; private long materialMask;
/*     */   private long vertexMask;
/* 106 */   static final Vector3 TMP_VECTOR3 = new Vector3();
/*     */   protected final Config config;
/*     */   
/* 109 */   public static class Inputs { public static final BaseShader.Uniform cameraRight = new BaseShader.Uniform("u_cameraRight");
/* 110 */     public static final BaseShader.Uniform cameraInvDirection = new BaseShader.Uniform("u_cameraInvDirection");
/* 111 */     public static final BaseShader.Uniform screenWidth = new BaseShader.Uniform("u_screenWidth");
/* 112 */     public static final BaseShader.Uniform regionSize = new BaseShader.Uniform("u_regionSize"); }
/*     */ 
/*     */   
/*     */   public static class Setters {
/* 116 */     public static final BaseShader.Setter cameraRight = new BaseShader.Setter()
/*     */       {
/*     */         public boolean isGlobal(BaseShader shader, int inputID) {
/* 119 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 124 */           shader.set(inputID, ParticleShader.TMP_VECTOR3.set(shader.camera.direction).crs(shader.camera.up).nor());
/*     */         }
/*     */       };
/*     */     
/* 128 */     public static final BaseShader.Setter cameraUp = new BaseShader.Setter()
/*     */       {
/*     */         public boolean isGlobal(BaseShader shader, int inputID) {
/* 131 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 136 */           shader.set(inputID, ParticleShader.TMP_VECTOR3.set(shader.camera.up).nor());
/*     */         }
/*     */       };
/*     */     
/* 140 */     public static final BaseShader.Setter cameraInvDirection = new BaseShader.Setter()
/*     */       {
/*     */         public boolean isGlobal(BaseShader shader, int inputID) {
/* 143 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 148 */           shader.set(inputID, ParticleShader.TMP_VECTOR3
/* 149 */               .set(-shader.camera.direction.x, -shader.camera.direction.y, -shader.camera.direction.z).nor());
/*     */         }
/*     */       };
/* 152 */     public static final BaseShader.Setter cameraPosition = new BaseShader.Setter()
/*     */       {
/*     */         public boolean isGlobal(BaseShader shader, int inputID) {
/* 155 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 160 */           shader.set(inputID, shader.camera.position);
/*     */         }
/*     */       };
/* 163 */     public static final BaseShader.Setter screenWidth = new BaseShader.Setter()
/*     */       {
/*     */         public boolean isGlobal(BaseShader shader, int inputID) {
/* 166 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 171 */           shader.set(inputID, Gdx.graphics.getWidth());
/*     */         }
/*     */       };
/* 174 */     public static final BaseShader.Setter worldViewTrans = new BaseShader.Setter() {
/* 175 */         final Matrix4 temp = new Matrix4();
/*     */ 
/*     */         
/*     */         public boolean isGlobal(BaseShader shader, int inputID) {
/* 179 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
/* 184 */           shader.set(inputID, this.temp.set(shader.camera.view).mul(renderable.worldTransform));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   private static final long optionalAttributes = IntAttribute.CullFace | DepthTestAttribute.Type; Material currentMaterial;
/*     */   
/*     */   public ParticleShader(Renderable renderable) {
/* 198 */     this(renderable, new Config());
/*     */   }
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config) {
/* 202 */     this(renderable, config, createPrefix(renderable, config));
/*     */   }
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config, String prefix) {
/* 206 */     this(renderable, config, prefix, (config.vertexShader != null) ? config.vertexShader : getDefaultVertexShader(), (config.fragmentShader != null) ? config.fragmentShader : 
/* 207 */         getDefaultFragmentShader());
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config, String prefix, String vertexShader, String fragmentShader) {
/* 212 */     this(renderable, config, new ShaderProgram(prefix + vertexShader, prefix + fragmentShader));
/*     */   }
/*     */   
/*     */   public ParticleShader(Renderable renderable, Config config, ShaderProgram shaderProgram) {
/* 216 */     this.config = config;
/* 217 */     this.program = shaderProgram;
/* 218 */     this.renderable = renderable;
/* 219 */     this.materialMask = renderable.material.getMask() | optionalAttributes;
/* 220 */     this.vertexMask = renderable.meshPart.mesh.getVertexAttributes().getMask();
/*     */     
/* 222 */     if (!config.ignoreUnimplemented && (implementedFlags & this.materialMask) != this.materialMask) {
/* 223 */       throw new GdxRuntimeException("Some attributes not implemented yet (" + this.materialMask + ")");
/*     */     }
/*     */     
/* 226 */     register(DefaultShader.Inputs.viewTrans, DefaultShader.Setters.viewTrans);
/* 227 */     register(DefaultShader.Inputs.projViewTrans, DefaultShader.Setters.projViewTrans);
/* 228 */     register(DefaultShader.Inputs.projTrans, DefaultShader.Setters.projTrans);
/* 229 */     register(Inputs.screenWidth, Setters.screenWidth);
/* 230 */     register(DefaultShader.Inputs.cameraUp, Setters.cameraUp);
/* 231 */     register(Inputs.cameraRight, Setters.cameraRight);
/* 232 */     register(Inputs.cameraInvDirection, Setters.cameraInvDirection);
/* 233 */     register(DefaultShader.Inputs.cameraPosition, Setters.cameraPosition);
/*     */ 
/*     */     
/* 236 */     register(DefaultShader.Inputs.diffuseTexture, DefaultShader.Setters.diffuseTexture);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 241 */     ShaderProgram program = this.program;
/* 242 */     this.program = null;
/* 243 */     init(program, this.renderable);
/* 244 */     this.renderable = null;
/*     */   }
/*     */   
/*     */   public static String createPrefix(Renderable renderable, Config config) {
/* 248 */     String prefix = "";
/* 249 */     if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
/* 250 */       prefix = prefix + "#version 120\n";
/*     */     } else {
/* 252 */       prefix = prefix + "#version 100\n";
/* 253 */     }  if (config.type == ParticleType.Billboard) {
/* 254 */       prefix = prefix + "#define billboard\n";
/* 255 */       if (config.align == AlignMode.Screen)
/* 256 */       { prefix = prefix + "#define screenFacing\n"; }
/* 257 */       else if (config.align == AlignMode.ViewPoint) { prefix = prefix + "#define viewPointFacing\n"; }
/*     */     
/*     */     } 
/*     */     
/* 261 */     return prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRender(Renderable renderable) {
/* 266 */     return (this.materialMask == (renderable.material.getMask() | optionalAttributes) && this.vertexMask == renderable.meshPart.mesh
/* 267 */       .getVertexAttributes().getMask());
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Shader other) {
/* 272 */     if (other == null) return -1; 
/* 273 */     if (other == this) return 0; 
/* 274 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 279 */     return (obj instanceof ParticleShader) ? equals((ParticleShader)obj) : false;
/*     */   }
/*     */   
/*     */   public boolean equals(ParticleShader obj) {
/* 283 */     return (obj == this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin(Camera camera, RenderContext context) {
/* 288 */     super.begin(camera, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Renderable renderable) {
/* 293 */     if (!renderable.material.has(BlendingAttribute.Type))
/* 294 */       this.context.setBlending(false, 770, 771); 
/* 295 */     bindMaterial(renderable);
/* 296 */     super.render(renderable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 301 */     this.currentMaterial = null;
/* 302 */     super.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindMaterial(Renderable renderable) {
/* 308 */     if (this.currentMaterial == renderable.material)
/*     */       return; 
/* 310 */     int cullFace = (this.config.defaultCullFace == -1) ? 1029 : this.config.defaultCullFace;
/* 311 */     int depthFunc = (this.config.defaultDepthFunc == -1) ? 515 : this.config.defaultDepthFunc;
/* 312 */     float depthRangeNear = 0.0F;
/* 313 */     float depthRangeFar = 1.0F;
/* 314 */     boolean depthMask = true;
/*     */     
/* 316 */     this.currentMaterial = renderable.material;
/* 317 */     for (Attribute attr : this.currentMaterial) {
/* 318 */       long t = attr.type;
/* 319 */       if (BlendingAttribute.is(t)) {
/* 320 */         this.context.setBlending(true, ((BlendingAttribute)attr).sourceFunction, ((BlendingAttribute)attr).destFunction); continue;
/* 321 */       }  if ((t & DepthTestAttribute.Type) == DepthTestAttribute.Type) {
/* 322 */         DepthTestAttribute dta = (DepthTestAttribute)attr;
/* 323 */         depthFunc = dta.depthFunc;
/* 324 */         depthRangeNear = dta.depthRangeNear;
/* 325 */         depthRangeFar = dta.depthRangeFar;
/* 326 */         depthMask = dta.depthMask; continue;
/* 327 */       }  if (!this.config.ignoreUnimplemented) throw new GdxRuntimeException("Unknown material attribute: " + attr.toString());
/*     */     
/*     */     } 
/* 330 */     this.context.setCullFace(cullFace);
/* 331 */     this.context.setDepthTest(depthFunc, depthRangeNear, depthRangeFar);
/* 332 */     this.context.setDepthMask(depthMask);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 337 */     this.program.dispose();
/* 338 */     super.dispose();
/*     */   }
/*     */   
/*     */   public int getDefaultCullFace() {
/* 342 */     return (this.config.defaultCullFace == -1) ? 1029 : this.config.defaultCullFace;
/*     */   }
/*     */   
/*     */   public void setDefaultCullFace(int cullFace) {
/* 346 */     this.config.defaultCullFace = cullFace;
/*     */   }
/*     */   
/*     */   public int getDefaultDepthFunc() {
/* 350 */     return (this.config.defaultDepthFunc == -1) ? 515 : this.config.defaultDepthFunc;
/*     */   }
/*     */   
/*     */   public void setDefaultDepthFunc(int depthFunc) {
/* 354 */     this.config.defaultDepthFunc = depthFunc;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParticleShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */