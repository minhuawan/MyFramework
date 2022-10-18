/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class ImmediateModeRenderer20
/*     */   implements ImmediateModeRenderer
/*     */ {
/*     */   private int primitiveType;
/*     */   private int vertexIdx;
/*     */   private int numSetTexCoords;
/*     */   private final int maxVertices;
/*     */   private int numVertices;
/*     */   private final Mesh mesh;
/*     */   private ShaderProgram shader;
/*     */   private boolean ownsShader;
/*     */   private final int numTexCoords;
/*     */   private final int vertexSize;
/*     */   private final int normalOffset;
/*     */   private final int colorOffset;
/*     */   private final int texCoordOffset;
/*  46 */   private final Matrix4 projModelView = new Matrix4();
/*     */   private final float[] vertices;
/*     */   private final String[] shaderUniformNames;
/*     */   
/*     */   public ImmediateModeRenderer20(boolean hasNormals, boolean hasColors, int numTexCoords) {
/*  51 */     this(5000, hasNormals, hasColors, numTexCoords, createDefaultShader(hasNormals, hasColors, numTexCoords));
/*  52 */     this.ownsShader = true;
/*     */   }
/*     */   
/*     */   public ImmediateModeRenderer20(int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords) {
/*  56 */     this(maxVertices, hasNormals, hasColors, numTexCoords, createDefaultShader(hasNormals, hasColors, numTexCoords));
/*  57 */     this.ownsShader = true;
/*     */   }
/*     */   
/*     */   public ImmediateModeRenderer20(int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords, ShaderProgram shader) {
/*  61 */     this.maxVertices = maxVertices;
/*  62 */     this.numTexCoords = numTexCoords;
/*  63 */     this.shader = shader;
/*     */     
/*  65 */     VertexAttribute[] attribs = buildVertexAttributes(hasNormals, hasColors, numTexCoords);
/*  66 */     this.mesh = new Mesh(false, maxVertices, 0, attribs);
/*     */     
/*  68 */     this.vertices = new float[maxVertices * (this.mesh.getVertexAttributes()).vertexSize / 4];
/*  69 */     this.vertexSize = (this.mesh.getVertexAttributes()).vertexSize / 4;
/*  70 */     this.normalOffset = (this.mesh.getVertexAttribute(8) != null) ? ((this.mesh.getVertexAttribute(8)).offset / 4) : 0;
/*  71 */     this.colorOffset = (this.mesh.getVertexAttribute(4) != null) ? ((this.mesh.getVertexAttribute(4)).offset / 4) : 0;
/*     */     
/*  73 */     this
/*  74 */       .texCoordOffset = (this.mesh.getVertexAttribute(16) != null) ? ((this.mesh.getVertexAttribute(16)).offset / 4) : 0;
/*     */     
/*  76 */     this.shaderUniformNames = new String[numTexCoords];
/*  77 */     for (int i = 0; i < numTexCoords; i++) {
/*  78 */       this.shaderUniformNames[i] = "u_sampler" + i;
/*     */     }
/*     */   }
/*     */   
/*     */   private VertexAttribute[] buildVertexAttributes(boolean hasNormals, boolean hasColor, int numTexCoords) {
/*  83 */     Array<VertexAttribute> attribs = new Array();
/*  84 */     attribs.add(new VertexAttribute(1, 3, "a_position"));
/*  85 */     if (hasNormals) attribs.add(new VertexAttribute(8, 3, "a_normal")); 
/*  86 */     if (hasColor) attribs.add(new VertexAttribute(4, 4, "a_color")); 
/*  87 */     for (int i = 0; i < numTexCoords; i++) {
/*  88 */       attribs.add(new VertexAttribute(16, 2, "a_texCoord" + i));
/*     */     }
/*  90 */     VertexAttribute[] array = new VertexAttribute[attribs.size];
/*  91 */     for (int j = 0; j < attribs.size; j++)
/*  92 */       array[j] = (VertexAttribute)attribs.get(j); 
/*  93 */     return array;
/*     */   }
/*     */   
/*     */   public void setShader(ShaderProgram shader) {
/*  97 */     if (this.ownsShader) this.shader.dispose(); 
/*  98 */     this.shader = shader;
/*  99 */     this.ownsShader = false;
/*     */   }
/*     */   
/*     */   public void begin(Matrix4 projModelView, int primitiveType) {
/* 103 */     this.projModelView.set(projModelView);
/* 104 */     this.primitiveType = primitiveType;
/*     */   }
/*     */   
/*     */   public void color(Color color) {
/* 108 */     this.vertices[this.vertexIdx + this.colorOffset] = color.toFloatBits();
/*     */   }
/*     */   
/*     */   public void color(float r, float g, float b, float a) {
/* 112 */     this.vertices[this.vertexIdx + this.colorOffset] = Color.toFloatBits(r, g, b, a);
/*     */   }
/*     */   
/*     */   public void color(float colorBits) {
/* 116 */     this.vertices[this.vertexIdx + this.colorOffset] = colorBits;
/*     */   }
/*     */   
/*     */   public void texCoord(float u, float v) {
/* 120 */     int idx = this.vertexIdx + this.texCoordOffset;
/* 121 */     this.vertices[idx + this.numSetTexCoords] = u;
/* 122 */     this.vertices[idx + this.numSetTexCoords + 1] = v;
/* 123 */     this.numSetTexCoords += 2;
/*     */   }
/*     */   
/*     */   public void normal(float x, float y, float z) {
/* 127 */     int idx = this.vertexIdx + this.normalOffset;
/* 128 */     this.vertices[idx] = x;
/* 129 */     this.vertices[idx + 1] = y;
/* 130 */     this.vertices[idx + 2] = z;
/*     */   }
/*     */   
/*     */   public void vertex(float x, float y, float z) {
/* 134 */     int idx = this.vertexIdx;
/* 135 */     this.vertices[idx] = x;
/* 136 */     this.vertices[idx + 1] = y;
/* 137 */     this.vertices[idx + 2] = z;
/*     */     
/* 139 */     this.numSetTexCoords = 0;
/* 140 */     this.vertexIdx += this.vertexSize;
/* 141 */     this.numVertices++;
/*     */   }
/*     */   
/*     */   public void flush() {
/* 145 */     if (this.numVertices == 0)
/* 146 */       return;  this.shader.begin();
/* 147 */     this.shader.setUniformMatrix("u_projModelView", this.projModelView);
/* 148 */     for (int i = 0; i < this.numTexCoords; i++)
/* 149 */       this.shader.setUniformi(this.shaderUniformNames[i], i); 
/* 150 */     this.mesh.setVertices(this.vertices, 0, this.vertexIdx);
/* 151 */     this.mesh.render(this.shader, this.primitiveType);
/* 152 */     this.shader.end();
/*     */     
/* 154 */     this.numSetTexCoords = 0;
/* 155 */     this.vertexIdx = 0;
/* 156 */     this.numVertices = 0;
/*     */   }
/*     */   
/*     */   public void end() {
/* 160 */     flush();
/*     */   }
/*     */   
/*     */   public int getNumVertices() {
/* 164 */     return this.numVertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxVertices() {
/* 169 */     return this.maxVertices;
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 173 */     if (this.ownsShader && this.shader != null) this.shader.dispose(); 
/* 174 */     this.mesh.dispose();
/*     */   }
/*     */   
/*     */   private static String createVertexShader(boolean hasNormals, boolean hasColors, int numTexCoords) {
/* 178 */     String shader = "attribute vec4 a_position;\n" + (hasNormals ? "attribute vec3 a_normal;\n" : "") + (hasColors ? "attribute vec4 a_color;\n" : "");
/*     */     
/*     */     int i;
/*     */     
/* 182 */     for (i = 0; i < numTexCoords; i++) {
/* 183 */       shader = shader + "attribute vec2 a_texCoord" + i + ";\n";
/*     */     }
/*     */     
/* 186 */     shader = shader + "uniform mat4 u_projModelView;\n";
/* 187 */     shader = shader + (hasColors ? "varying vec4 v_col;\n" : "");
/*     */     
/* 189 */     for (i = 0; i < numTexCoords; i++) {
/* 190 */       shader = shader + "varying vec2 v_tex" + i + ";\n";
/*     */     }
/*     */     
/* 193 */     shader = shader + "void main() {\n   gl_Position = u_projModelView * a_position;\n" + (hasColors ? "   v_col = a_color;\n" : "");
/*     */ 
/*     */     
/* 196 */     for (i = 0; i < numTexCoords; i++) {
/* 197 */       shader = shader + "   v_tex" + i + " = " + "a_texCoord" + i + ";\n";
/*     */     }
/* 199 */     shader = shader + "   gl_PointSize = 1.0;\n";
/* 200 */     shader = shader + "}\n";
/* 201 */     return shader;
/*     */   }
/*     */   
/*     */   private static String createFragmentShader(boolean hasNormals, boolean hasColors, int numTexCoords) {
/* 205 */     String shader = "#ifdef GL_ES\nprecision mediump float;\n#endif\n";
/*     */     
/* 207 */     if (hasColors) shader = shader + "varying vec4 v_col;\n";  int i;
/* 208 */     for (i = 0; i < numTexCoords; i++) {
/* 209 */       shader = shader + "varying vec2 v_tex" + i + ";\n";
/* 210 */       shader = shader + "uniform sampler2D u_sampler" + i + ";\n";
/*     */     } 
/*     */     
/* 213 */     shader = shader + "void main() {\n   gl_FragColor = " + (hasColors ? "v_col" : "vec4(1, 1, 1, 1)");
/*     */     
/* 215 */     if (numTexCoords > 0) shader = shader + " * ";
/*     */     
/* 217 */     for (i = 0; i < numTexCoords; i++) {
/* 218 */       if (i == numTexCoords - 1) {
/* 219 */         shader = shader + " texture2D(u_sampler" + i + ",  v_tex" + i + ")";
/*     */       } else {
/* 221 */         shader = shader + " texture2D(u_sampler" + i + ",  v_tex" + i + ") *";
/*     */       } 
/*     */     } 
/*     */     
/* 225 */     shader = shader + ";\n}";
/* 226 */     return shader;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ShaderProgram createDefaultShader(boolean hasNormals, boolean hasColors, int numTexCoords) {
/* 231 */     String vertexShader = createVertexShader(hasNormals, hasColors, numTexCoords);
/* 232 */     String fragmentShader = createFragmentShader(hasNormals, hasColors, numTexCoords);
/* 233 */     ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
/* 234 */     return program;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\ImmediateModeRenderer20.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */