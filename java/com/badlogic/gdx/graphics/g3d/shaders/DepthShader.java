/*     */ package com.badlogic.gdx.graphics.g3d.shaders;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Attributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
/*     */ public class DepthShader
/*     */   extends DefaultShader
/*     */ {
/*     */   public static class Config
/*     */     extends DefaultShader.Config
/*     */   {
/*     */     public boolean depthBufferOnly = false;
/*  35 */     public float defaultAlphaTest = 0.5F;
/*     */ 
/*     */     
/*     */     public Config() {
/*  39 */       this.defaultCullFace = 1028;
/*     */     }
/*     */     
/*     */     public Config(String vertexShader, String fragmentShader) {
/*  43 */       super(vertexShader, fragmentShader);
/*     */     }
/*     */   }
/*     */   
/*  47 */   private static String defaultVertexShader = null;
/*     */   
/*     */   public static final String getDefaultVertexShader() {
/*  50 */     if (defaultVertexShader == null)
/*  51 */       defaultVertexShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/shaders/depth.vertex.glsl").readString(); 
/*  52 */     return defaultVertexShader;
/*     */   }
/*     */   
/*  55 */   private static String defaultFragmentShader = null; public final int numBones;
/*     */   
/*     */   public static final String getDefaultFragmentShader() {
/*  58 */     if (defaultFragmentShader == null)
/*  59 */       defaultFragmentShader = Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/shaders/depth.fragment.glsl").readString(); 
/*  60 */     return defaultFragmentShader;
/*     */   }
/*     */   public final int weights; private final FloatAttribute alphaTestAttribute;
/*     */   public static String createPrefix(Renderable renderable, Config config) {
/*  64 */     String prefix = DefaultShader.createPrefix(renderable, config);
/*  65 */     if (!config.depthBufferOnly) prefix = prefix + "#define PackedDepthFlag\n"; 
/*  66 */     return prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DepthShader(Renderable renderable) {
/*  74 */     this(renderable, new Config());
/*     */   }
/*     */   
/*     */   public DepthShader(Renderable renderable, Config config) {
/*  78 */     this(renderable, config, createPrefix(renderable, config));
/*     */   }
/*     */   
/*     */   public DepthShader(Renderable renderable, Config config, String prefix) {
/*  82 */     this(renderable, config, prefix, (config.vertexShader != null) ? config.vertexShader : getDefaultVertexShader(), (config.fragmentShader != null) ? config.fragmentShader : 
/*  83 */         getDefaultFragmentShader());
/*     */   }
/*     */ 
/*     */   
/*     */   public DepthShader(Renderable renderable, Config config, String prefix, String vertexShader, String fragmentShader) {
/*  88 */     this(renderable, config, new ShaderProgram(prefix + vertexShader, prefix + fragmentShader));
/*     */   }
/*     */   
/*     */   public DepthShader(Renderable renderable, Config config, ShaderProgram shaderProgram) {
/*  92 */     super(renderable, config, shaderProgram);
/*  93 */     Attributes attributes = combineAttributes(renderable);
/*  94 */     this.numBones = (renderable.bones == null) ? 0 : config.numBones;
/*  95 */     int w = 0;
/*  96 */     int n = renderable.meshPart.mesh.getVertexAttributes().size();
/*  97 */     for (int i = 0; i < n; i++) {
/*  98 */       VertexAttribute attr = renderable.meshPart.mesh.getVertexAttributes().get(i);
/*  99 */       if (attr.usage == 64) w |= 1 << attr.unit; 
/*     */     } 
/* 101 */     this.weights = w;
/* 102 */     this.alphaTestAttribute = new FloatAttribute(FloatAttribute.AlphaTest, config.defaultAlphaTest);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin(Camera camera, RenderContext context) {
/* 107 */     super.begin(camera, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() {
/* 114 */     super.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRender(Renderable renderable) {
/* 120 */     Attributes attributes = combineAttributes(renderable);
/* 121 */     if (attributes.has(BlendingAttribute.Type)) {
/* 122 */       if ((this.attributesMask & BlendingAttribute.Type) != BlendingAttribute.Type)
/* 123 */         return false; 
/* 124 */       if (attributes.has(TextureAttribute.Diffuse) != (((this.attributesMask & TextureAttribute.Diffuse) == TextureAttribute.Diffuse)))
/* 125 */         return false; 
/*     */     } 
/* 127 */     boolean skinned = ((renderable.meshPart.mesh.getVertexAttributes().getMask() & 0x40L) == 64L);
/* 128 */     if (skinned != ((this.numBones > 0))) return false; 
/* 129 */     if (!skinned) return true; 
/* 130 */     int w = 0;
/* 131 */     int n = renderable.meshPart.mesh.getVertexAttributes().size();
/* 132 */     for (int i = 0; i < n; i++) {
/* 133 */       VertexAttribute attr = renderable.meshPart.mesh.getVertexAttributes().get(i);
/* 134 */       if (attr.usage == 64) w |= 1 << attr.unit; 
/*     */     } 
/* 136 */     return (w == this.weights);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Renderable renderable, Attributes combinedAttributes) {
/* 141 */     if (combinedAttributes.has(BlendingAttribute.Type)) {
/* 142 */       BlendingAttribute blending = (BlendingAttribute)combinedAttributes.get(BlendingAttribute.Type);
/* 143 */       combinedAttributes.remove(BlendingAttribute.Type);
/* 144 */       boolean hasAlphaTest = combinedAttributes.has(FloatAttribute.AlphaTest);
/* 145 */       if (!hasAlphaTest)
/* 146 */         combinedAttributes.set((Attribute)this.alphaTestAttribute); 
/* 147 */       if (blending.opacity >= ((FloatAttribute)combinedAttributes.get(FloatAttribute.AlphaTest)).value)
/* 148 */         super.render(renderable, combinedAttributes); 
/* 149 */       if (!hasAlphaTest)
/* 150 */         combinedAttributes.remove(FloatAttribute.AlphaTest); 
/* 151 */       combinedAttributes.set((Attribute)blending);
/*     */     } else {
/* 153 */       super.render(renderable, combinedAttributes);
/*     */     } 
/*     */   }
/* 156 */   private static final Attributes tmpAttributes = new Attributes();
/*     */   
/*     */   private static final Attributes combineAttributes(Renderable renderable) {
/* 159 */     tmpAttributes.clear();
/* 160 */     if (renderable.environment != null) tmpAttributes.set((Iterable)renderable.environment); 
/* 161 */     if (renderable.material != null) tmpAttributes.set((Iterable)renderable.material); 
/* 162 */     return tmpAttributes;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\shaders\DepthShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */