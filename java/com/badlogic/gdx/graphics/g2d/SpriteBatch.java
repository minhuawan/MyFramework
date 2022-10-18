/*      */ package com.badlogic.gdx.graphics.g2d;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.GL20;
/*      */ import com.badlogic.gdx.graphics.Mesh;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.VertexAttribute;
/*      */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*      */ import com.badlogic.gdx.math.Affine2;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Matrix4;
/*      */ import com.badlogic.gdx.utils.NumberUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SpriteBatch
/*      */   implements Batch
/*      */ {
/*      */   @Deprecated
/*   40 */   public static Mesh.VertexDataType defaultVertexDataType = Mesh.VertexDataType.VertexArray;
/*      */   
/*      */   private Mesh mesh;
/*      */   
/*      */   final float[] vertices;
/*   45 */   int idx = 0;
/*   46 */   Texture lastTexture = null;
/*   47 */   float invTexWidth = 0.0F; float invTexHeight = 0.0F;
/*      */   
/*      */   boolean drawing = false;
/*      */   
/*   51 */   private final Matrix4 transformMatrix = new Matrix4();
/*   52 */   private final Matrix4 projectionMatrix = new Matrix4();
/*   53 */   private final Matrix4 combinedMatrix = new Matrix4();
/*      */   
/*      */   private boolean blendingDisabled = false;
/*   56 */   private int blendSrcFunc = 770;
/*   57 */   private int blendDstFunc = 771;
/*      */   
/*      */   private final ShaderProgram shader;
/*   60 */   private ShaderProgram customShader = null;
/*      */   
/*      */   private boolean ownsShader;
/*   63 */   float color = Color.WHITE.toFloatBits();
/*   64 */   private Color tempColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */ 
/*      */   
/*   67 */   public int renderCalls = 0;
/*      */ 
/*      */   
/*   70 */   public int totalRenderCalls = 0;
/*      */ 
/*      */   
/*   73 */   public int maxSpritesInBatch = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   public SpriteBatch() {
/*   78 */     this(1000, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SpriteBatch(int size) {
/*   84 */     this(size, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SpriteBatch(int size, ShaderProgram defaultShader) {
/*   97 */     if (size > 8191) throw new IllegalArgumentException("Can't have more than 8191 sprites per batch: " + size);
/*      */     
/*   99 */     Mesh.VertexDataType vertexDataType = (Gdx.gl30 != null) ? Mesh.VertexDataType.VertexBufferObjectWithVAO : defaultVertexDataType;
/*      */     
/*  101 */     this.mesh = new Mesh(vertexDataType, false, size * 4, size * 6, new VertexAttribute[] { new VertexAttribute(1, 2, "a_position"), new VertexAttribute(4, 4, "a_color"), new VertexAttribute(16, 2, "a_texCoord0") });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  106 */     this.projectionMatrix.setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*      */     
/*  108 */     this.vertices = new float[size * 20];
/*      */     
/*  110 */     int len = size * 6;
/*  111 */     short[] indices = new short[len];
/*  112 */     short j = 0;
/*  113 */     for (int i = 0; i < len; i += 6, j = (short)(j + 4)) {
/*  114 */       indices[i] = j;
/*  115 */       indices[i + 1] = (short)(j + 1);
/*  116 */       indices[i + 2] = (short)(j + 2);
/*  117 */       indices[i + 3] = (short)(j + 2);
/*  118 */       indices[i + 4] = (short)(j + 3);
/*  119 */       indices[i + 5] = j;
/*      */     } 
/*  121 */     this.mesh.setIndices(indices);
/*      */     
/*  123 */     if (defaultShader == null) {
/*  124 */       this.shader = createDefaultShader();
/*  125 */       this.ownsShader = true;
/*      */     } else {
/*  127 */       this.shader = defaultShader;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static ShaderProgram createDefaultShader() {
/*  132 */     String vertexShader = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_color.a = v_color.a * (255.0/254.0);\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projTrans * a_position;\n}\n";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  146 */     String fragmentShader = "#ifdef GL_ES\n#define LOWP lowp\nprecision mediump float;\n#else\n#define LOWP \n#endif\nvarying LOWP vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  160 */     ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
/*  161 */     if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog()); 
/*  162 */     return shader;
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin() {
/*  167 */     if (this.drawing) throw new IllegalStateException("SpriteBatch.end must be called before begin."); 
/*  168 */     this.renderCalls = 0;
/*      */     
/*  170 */     Gdx.gl.glDepthMask(false);
/*  171 */     if (this.customShader != null) {
/*  172 */       this.customShader.begin();
/*      */     } else {
/*  174 */       this.shader.begin();
/*  175 */     }  setupMatrices();
/*      */     
/*  177 */     this.drawing = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void end() {
/*  182 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before end."); 
/*  183 */     if (this.idx > 0) flush(); 
/*  184 */     this.lastTexture = null;
/*  185 */     this.drawing = false;
/*      */     
/*  187 */     GL20 gl = Gdx.gl;
/*  188 */     gl.glDepthMask(true);
/*  189 */     if (isBlendingEnabled()) gl.glDisable(3042);
/*      */     
/*  191 */     if (this.customShader != null) {
/*  192 */       this.customShader.end();
/*      */     } else {
/*  194 */       this.shader.end();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setColor(Color tint) {
/*  199 */     this.color = tint.toFloatBits();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  204 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/*  205 */     this.color = NumberUtils.intToFloatColor(intBits);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float color) {
/*  210 */     this.color = color;
/*      */   }
/*      */ 
/*      */   
/*      */   public Color getColor() {
/*  215 */     int intBits = NumberUtils.floatToIntColor(this.color);
/*  216 */     Color color = this.tempColor;
/*  217 */     color.r = (intBits & 0xFF) / 255.0F;
/*  218 */     color.g = (intBits >>> 8 & 0xFF) / 255.0F;
/*  219 */     color.b = (intBits >>> 16 & 0xFF) / 255.0F;
/*  220 */     color.a = (intBits >>> 24 & 0xFF) / 255.0F;
/*  221 */     return color;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPackedColor() {
/*  226 */     return this.color;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  232 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  234 */     float[] vertices = this.vertices;
/*      */     
/*  236 */     if (texture != this.lastTexture) {
/*  237 */       switchTexture(texture);
/*  238 */     } else if (this.idx == vertices.length) {
/*  239 */       flush();
/*      */     } 
/*      */     
/*  242 */     float worldOriginX = x + originX;
/*  243 */     float worldOriginY = y + originY;
/*  244 */     float fx = -originX;
/*  245 */     float fy = -originY;
/*  246 */     float fx2 = width - originX;
/*  247 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  250 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  251 */       fx *= scaleX;
/*  252 */       fy *= scaleY;
/*  253 */       fx2 *= scaleX;
/*  254 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  258 */     float p1x = fx;
/*  259 */     float p1y = fy;
/*  260 */     float p2x = fx;
/*  261 */     float p2y = fy2;
/*  262 */     float p3x = fx2;
/*  263 */     float p3y = fy2;
/*  264 */     float p4x = fx2;
/*  265 */     float p4y = fy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  277 */     if (rotation != 0.0F) {
/*  278 */       float cos = MathUtils.cosDeg(rotation);
/*  279 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  281 */       x1 = cos * p1x - sin * p1y;
/*  282 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  284 */       x2 = cos * p2x - sin * p2y;
/*  285 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  287 */       x3 = cos * p3x - sin * p3y;
/*  288 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  290 */       x4 = x1 + x3 - x2;
/*  291 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  293 */       x1 = p1x;
/*  294 */       y1 = p1y;
/*      */       
/*  296 */       x2 = p2x;
/*  297 */       y2 = p2y;
/*      */       
/*  299 */       x3 = p3x;
/*  300 */       y3 = p3y;
/*      */       
/*  302 */       x4 = p4x;
/*  303 */       y4 = p4y;
/*      */     } 
/*      */     
/*  306 */     x1 += worldOriginX;
/*  307 */     y1 += worldOriginY;
/*  308 */     x2 += worldOriginX;
/*  309 */     y2 += worldOriginY;
/*  310 */     x3 += worldOriginX;
/*  311 */     y3 += worldOriginY;
/*  312 */     x4 += worldOriginX;
/*  313 */     y4 += worldOriginY;
/*      */     
/*  315 */     float u = srcX * this.invTexWidth;
/*  316 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  317 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  318 */     float v2 = srcY * this.invTexHeight;
/*      */     
/*  320 */     if (flipX) {
/*  321 */       float tmp = u;
/*  322 */       u = u2;
/*  323 */       u2 = tmp;
/*      */     } 
/*      */     
/*  326 */     if (flipY) {
/*  327 */       float tmp = v;
/*  328 */       v = v2;
/*  329 */       v2 = tmp;
/*      */     } 
/*      */     
/*  332 */     float color = this.color;
/*  333 */     int idx = this.idx;
/*  334 */     vertices[idx] = x1;
/*  335 */     vertices[idx + 1] = y1;
/*  336 */     vertices[idx + 2] = color;
/*  337 */     vertices[idx + 3] = u;
/*  338 */     vertices[idx + 4] = v;
/*      */     
/*  340 */     vertices[idx + 5] = x2;
/*  341 */     vertices[idx + 6] = y2;
/*  342 */     vertices[idx + 7] = color;
/*  343 */     vertices[idx + 8] = u;
/*  344 */     vertices[idx + 9] = v2;
/*      */     
/*  346 */     vertices[idx + 10] = x3;
/*  347 */     vertices[idx + 11] = y3;
/*  348 */     vertices[idx + 12] = color;
/*  349 */     vertices[idx + 13] = u2;
/*  350 */     vertices[idx + 14] = v2;
/*      */     
/*  352 */     vertices[idx + 15] = x4;
/*  353 */     vertices[idx + 16] = y4;
/*  354 */     vertices[idx + 17] = color;
/*  355 */     vertices[idx + 18] = u2;
/*  356 */     vertices[idx + 19] = v;
/*  357 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*  363 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  365 */     float[] vertices = this.vertices;
/*      */     
/*  367 */     if (texture != this.lastTexture) {
/*  368 */       switchTexture(texture);
/*  369 */     } else if (this.idx == vertices.length) {
/*  370 */       flush();
/*      */     } 
/*  372 */     float u = srcX * this.invTexWidth;
/*  373 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  374 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  375 */     float v2 = srcY * this.invTexHeight;
/*  376 */     float fx2 = x + width;
/*  377 */     float fy2 = y + height;
/*      */     
/*  379 */     if (flipX) {
/*  380 */       float tmp = u;
/*  381 */       u = u2;
/*  382 */       u2 = tmp;
/*      */     } 
/*      */     
/*  385 */     if (flipY) {
/*  386 */       float tmp = v;
/*  387 */       v = v2;
/*  388 */       v2 = tmp;
/*      */     } 
/*      */     
/*  391 */     float color = this.color;
/*  392 */     int idx = this.idx;
/*  393 */     vertices[idx] = x;
/*  394 */     vertices[idx + 1] = y;
/*  395 */     vertices[idx + 2] = color;
/*  396 */     vertices[idx + 3] = u;
/*  397 */     vertices[idx + 4] = v;
/*      */     
/*  399 */     vertices[idx + 5] = x;
/*  400 */     vertices[idx + 6] = fy2;
/*  401 */     vertices[idx + 7] = color;
/*  402 */     vertices[idx + 8] = u;
/*  403 */     vertices[idx + 9] = v2;
/*      */     
/*  405 */     vertices[idx + 10] = fx2;
/*  406 */     vertices[idx + 11] = fy2;
/*  407 */     vertices[idx + 12] = color;
/*  408 */     vertices[idx + 13] = u2;
/*  409 */     vertices[idx + 14] = v2;
/*      */     
/*  411 */     vertices[idx + 15] = fx2;
/*  412 */     vertices[idx + 16] = y;
/*  413 */     vertices[idx + 17] = color;
/*  414 */     vertices[idx + 18] = u2;
/*  415 */     vertices[idx + 19] = v;
/*  416 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
/*  421 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  423 */     float[] vertices = this.vertices;
/*      */     
/*  425 */     if (texture != this.lastTexture) {
/*  426 */       switchTexture(texture);
/*  427 */     } else if (this.idx == vertices.length) {
/*  428 */       flush();
/*      */     } 
/*  430 */     float u = srcX * this.invTexWidth;
/*  431 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  432 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  433 */     float v2 = srcY * this.invTexHeight;
/*  434 */     float fx2 = x + srcWidth;
/*  435 */     float fy2 = y + srcHeight;
/*      */     
/*  437 */     float color = this.color;
/*  438 */     int idx = this.idx;
/*  439 */     vertices[idx] = x;
/*  440 */     vertices[idx + 1] = y;
/*  441 */     vertices[idx + 2] = color;
/*  442 */     vertices[idx + 3] = u;
/*  443 */     vertices[idx + 4] = v;
/*      */     
/*  445 */     vertices[idx + 5] = x;
/*  446 */     vertices[idx + 6] = fy2;
/*  447 */     vertices[idx + 7] = color;
/*  448 */     vertices[idx + 8] = u;
/*  449 */     vertices[idx + 9] = v2;
/*      */     
/*  451 */     vertices[idx + 10] = fx2;
/*  452 */     vertices[idx + 11] = fy2;
/*  453 */     vertices[idx + 12] = color;
/*  454 */     vertices[idx + 13] = u2;
/*  455 */     vertices[idx + 14] = v2;
/*      */     
/*  457 */     vertices[idx + 15] = fx2;
/*  458 */     vertices[idx + 16] = y;
/*  459 */     vertices[idx + 17] = color;
/*  460 */     vertices[idx + 18] = u2;
/*  461 */     vertices[idx + 19] = v;
/*  462 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
/*  467 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  469 */     float[] vertices = this.vertices;
/*      */     
/*  471 */     if (texture != this.lastTexture) {
/*  472 */       switchTexture(texture);
/*  473 */     } else if (this.idx == vertices.length) {
/*  474 */       flush();
/*      */     } 
/*  476 */     float fx2 = x + width;
/*  477 */     float fy2 = y + height;
/*      */     
/*  479 */     float color = this.color;
/*  480 */     int idx = this.idx;
/*  481 */     vertices[idx] = x;
/*  482 */     vertices[idx + 1] = y;
/*  483 */     vertices[idx + 2] = color;
/*  484 */     vertices[idx + 3] = u;
/*  485 */     vertices[idx + 4] = v;
/*      */     
/*  487 */     vertices[idx + 5] = x;
/*  488 */     vertices[idx + 6] = fy2;
/*  489 */     vertices[idx + 7] = color;
/*  490 */     vertices[idx + 8] = u;
/*  491 */     vertices[idx + 9] = v2;
/*      */     
/*  493 */     vertices[idx + 10] = fx2;
/*  494 */     vertices[idx + 11] = fy2;
/*  495 */     vertices[idx + 12] = color;
/*  496 */     vertices[idx + 13] = u2;
/*  497 */     vertices[idx + 14] = v2;
/*      */     
/*  499 */     vertices[idx + 15] = fx2;
/*  500 */     vertices[idx + 16] = y;
/*  501 */     vertices[idx + 17] = color;
/*  502 */     vertices[idx + 18] = u2;
/*  503 */     vertices[idx + 19] = v;
/*  504 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y) {
/*  509 */     draw(texture, x, y, texture.getWidth(), texture.getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height) {
/*  514 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  516 */     float[] vertices = this.vertices;
/*      */     
/*  518 */     if (texture != this.lastTexture) {
/*  519 */       switchTexture(texture);
/*  520 */     } else if (this.idx == vertices.length) {
/*  521 */       flush();
/*      */     } 
/*  523 */     float fx2 = x + width;
/*  524 */     float fy2 = y + height;
/*  525 */     float u = 0.0F;
/*  526 */     float v = 1.0F;
/*  527 */     float u2 = 1.0F;
/*  528 */     float v2 = 0.0F;
/*      */     
/*  530 */     float color = this.color;
/*  531 */     int idx = this.idx;
/*  532 */     vertices[idx] = x;
/*  533 */     vertices[idx + 1] = y;
/*  534 */     vertices[idx + 2] = color;
/*  535 */     vertices[idx + 3] = 0.0F;
/*  536 */     vertices[idx + 4] = 1.0F;
/*      */     
/*  538 */     vertices[idx + 5] = x;
/*  539 */     vertices[idx + 6] = fy2;
/*  540 */     vertices[idx + 7] = color;
/*  541 */     vertices[idx + 8] = 0.0F;
/*  542 */     vertices[idx + 9] = 0.0F;
/*      */     
/*  544 */     vertices[idx + 10] = fx2;
/*  545 */     vertices[idx + 11] = fy2;
/*  546 */     vertices[idx + 12] = color;
/*  547 */     vertices[idx + 13] = 1.0F;
/*  548 */     vertices[idx + 14] = 0.0F;
/*      */     
/*  550 */     vertices[idx + 15] = fx2;
/*  551 */     vertices[idx + 16] = y;
/*  552 */     vertices[idx + 17] = color;
/*  553 */     vertices[idx + 18] = 1.0F;
/*  554 */     vertices[idx + 19] = 1.0F;
/*  555 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
/*  560 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  562 */     int verticesLength = this.vertices.length;
/*  563 */     int remainingVertices = verticesLength;
/*  564 */     if (texture != this.lastTexture) {
/*  565 */       switchTexture(texture);
/*      */     } else {
/*  567 */       remainingVertices -= this.idx;
/*  568 */       if (remainingVertices == 0) {
/*  569 */         flush();
/*  570 */         remainingVertices = verticesLength;
/*      */       } 
/*      */     } 
/*  573 */     int copyCount = Math.min(remainingVertices, count);
/*      */     
/*  575 */     System.arraycopy(spriteVertices, offset, this.vertices, this.idx, copyCount);
/*  576 */     this.idx += copyCount;
/*  577 */     count -= copyCount;
/*  578 */     while (count > 0) {
/*  579 */       offset += copyCount;
/*  580 */       flush();
/*  581 */       copyCount = Math.min(verticesLength, count);
/*  582 */       System.arraycopy(spriteVertices, offset, this.vertices, 0, copyCount);
/*  583 */       this.idx += copyCount;
/*  584 */       count -= copyCount;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y) {
/*  590 */     draw(region, x, y, region.getRegionWidth(), region.getRegionHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float width, float height) {
/*  595 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  597 */     float[] vertices = this.vertices;
/*      */     
/*  599 */     Texture texture = region.texture;
/*  600 */     if (texture != this.lastTexture) {
/*  601 */       switchTexture(texture);
/*  602 */     } else if (this.idx == vertices.length) {
/*  603 */       flush();
/*      */     } 
/*  605 */     float fx2 = x + width;
/*  606 */     float fy2 = y + height;
/*  607 */     float u = region.u;
/*  608 */     float v = region.v2;
/*  609 */     float u2 = region.u2;
/*  610 */     float v2 = region.v;
/*      */     
/*  612 */     float color = this.color;
/*  613 */     int idx = this.idx;
/*  614 */     vertices[idx] = x;
/*  615 */     vertices[idx + 1] = y;
/*  616 */     vertices[idx + 2] = color;
/*  617 */     vertices[idx + 3] = u;
/*  618 */     vertices[idx + 4] = v;
/*      */     
/*  620 */     vertices[idx + 5] = x;
/*  621 */     vertices[idx + 6] = fy2;
/*  622 */     vertices[idx + 7] = color;
/*  623 */     vertices[idx + 8] = u;
/*  624 */     vertices[idx + 9] = v2;
/*      */     
/*  626 */     vertices[idx + 10] = fx2;
/*  627 */     vertices[idx + 11] = fy2;
/*  628 */     vertices[idx + 12] = color;
/*  629 */     vertices[idx + 13] = u2;
/*  630 */     vertices[idx + 14] = v2;
/*      */     
/*  632 */     vertices[idx + 15] = fx2;
/*  633 */     vertices[idx + 16] = y;
/*  634 */     vertices[idx + 17] = color;
/*  635 */     vertices[idx + 18] = u2;
/*  636 */     vertices[idx + 19] = v;
/*  637 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  643 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  645 */     float[] vertices = this.vertices;
/*      */     
/*  647 */     Texture texture = region.texture;
/*  648 */     if (texture != this.lastTexture) {
/*  649 */       switchTexture(texture);
/*  650 */     } else if (this.idx == vertices.length) {
/*  651 */       flush();
/*      */     } 
/*      */     
/*  654 */     float worldOriginX = x + originX;
/*  655 */     float worldOriginY = y + originY;
/*  656 */     float fx = -originX;
/*  657 */     float fy = -originY;
/*  658 */     float fx2 = width - originX;
/*  659 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  662 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  663 */       fx *= scaleX;
/*  664 */       fy *= scaleY;
/*  665 */       fx2 *= scaleX;
/*  666 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  670 */     float p1x = fx;
/*  671 */     float p1y = fy;
/*  672 */     float p2x = fx;
/*  673 */     float p2y = fy2;
/*  674 */     float p3x = fx2;
/*  675 */     float p3y = fy2;
/*  676 */     float p4x = fx2;
/*  677 */     float p4y = fy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  689 */     if (rotation != 0.0F) {
/*  690 */       float cos = MathUtils.cosDeg(rotation);
/*  691 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  693 */       x1 = cos * p1x - sin * p1y;
/*  694 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  696 */       x2 = cos * p2x - sin * p2y;
/*  697 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  699 */       x3 = cos * p3x - sin * p3y;
/*  700 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  702 */       x4 = x1 + x3 - x2;
/*  703 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  705 */       x1 = p1x;
/*  706 */       y1 = p1y;
/*      */       
/*  708 */       x2 = p2x;
/*  709 */       y2 = p2y;
/*      */       
/*  711 */       x3 = p3x;
/*  712 */       y3 = p3y;
/*      */       
/*  714 */       x4 = p4x;
/*  715 */       y4 = p4y;
/*      */     } 
/*      */     
/*  718 */     x1 += worldOriginX;
/*  719 */     y1 += worldOriginY;
/*  720 */     x2 += worldOriginX;
/*  721 */     y2 += worldOriginY;
/*  722 */     x3 += worldOriginX;
/*  723 */     y3 += worldOriginY;
/*  724 */     x4 += worldOriginX;
/*  725 */     y4 += worldOriginY;
/*      */     
/*  727 */     float u = region.u;
/*  728 */     float v = region.v2;
/*  729 */     float u2 = region.u2;
/*  730 */     float v2 = region.v;
/*      */     
/*  732 */     float color = this.color;
/*  733 */     int idx = this.idx;
/*  734 */     vertices[idx] = x1;
/*  735 */     vertices[idx + 1] = y1;
/*  736 */     vertices[idx + 2] = color;
/*  737 */     vertices[idx + 3] = u;
/*  738 */     vertices[idx + 4] = v;
/*      */     
/*  740 */     vertices[idx + 5] = x2;
/*  741 */     vertices[idx + 6] = y2;
/*  742 */     vertices[idx + 7] = color;
/*  743 */     vertices[idx + 8] = u;
/*  744 */     vertices[idx + 9] = v2;
/*      */     
/*  746 */     vertices[idx + 10] = x3;
/*  747 */     vertices[idx + 11] = y3;
/*  748 */     vertices[idx + 12] = color;
/*  749 */     vertices[idx + 13] = u2;
/*  750 */     vertices[idx + 14] = v2;
/*      */     
/*  752 */     vertices[idx + 15] = x4;
/*  753 */     vertices[idx + 16] = y4;
/*  754 */     vertices[idx + 17] = color;
/*  755 */     vertices[idx + 18] = u2;
/*  756 */     vertices[idx + 19] = v;
/*  757 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4;
/*  763 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  765 */     float[] vertices = this.vertices;
/*      */     
/*  767 */     Texture texture = region.texture;
/*  768 */     if (texture != this.lastTexture) {
/*  769 */       switchTexture(texture);
/*  770 */     } else if (this.idx == vertices.length) {
/*  771 */       flush();
/*      */     } 
/*      */     
/*  774 */     float worldOriginX = x + originX;
/*  775 */     float worldOriginY = y + originY;
/*  776 */     float fx = -originX;
/*  777 */     float fy = -originY;
/*  778 */     float fx2 = width - originX;
/*  779 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  782 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  783 */       fx *= scaleX;
/*  784 */       fy *= scaleY;
/*  785 */       fx2 *= scaleX;
/*  786 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  790 */     float p1x = fx;
/*  791 */     float p1y = fy;
/*  792 */     float p2x = fx;
/*  793 */     float p2y = fy2;
/*  794 */     float p3x = fx2;
/*  795 */     float p3y = fy2;
/*  796 */     float p4x = fx2;
/*  797 */     float p4y = fy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  809 */     if (rotation != 0.0F) {
/*  810 */       float cos = MathUtils.cosDeg(rotation);
/*  811 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  813 */       x1 = cos * p1x - sin * p1y;
/*  814 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  816 */       x2 = cos * p2x - sin * p2y;
/*  817 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  819 */       x3 = cos * p3x - sin * p3y;
/*  820 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  822 */       x4 = x1 + x3 - x2;
/*  823 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  825 */       x1 = p1x;
/*  826 */       y1 = p1y;
/*      */       
/*  828 */       x2 = p2x;
/*  829 */       y2 = p2y;
/*      */       
/*  831 */       x3 = p3x;
/*  832 */       y3 = p3y;
/*      */       
/*  834 */       x4 = p4x;
/*  835 */       y4 = p4y;
/*      */     } 
/*      */     
/*  838 */     x1 += worldOriginX;
/*  839 */     y1 += worldOriginY;
/*  840 */     x2 += worldOriginX;
/*  841 */     y2 += worldOriginY;
/*  842 */     x3 += worldOriginX;
/*  843 */     y3 += worldOriginY;
/*  844 */     x4 += worldOriginX;
/*  845 */     y4 += worldOriginY;
/*      */ 
/*      */     
/*  848 */     if (clockwise) {
/*  849 */       u1 = region.u2;
/*  850 */       v1 = region.v2;
/*  851 */       u2 = region.u;
/*  852 */       v2 = region.v2;
/*  853 */       u3 = region.u;
/*  854 */       v3 = region.v;
/*  855 */       u4 = region.u2;
/*  856 */       v4 = region.v;
/*      */     } else {
/*  858 */       u1 = region.u;
/*  859 */       v1 = region.v;
/*  860 */       u2 = region.u2;
/*  861 */       v2 = region.v;
/*  862 */       u3 = region.u2;
/*  863 */       v3 = region.v2;
/*  864 */       u4 = region.u;
/*  865 */       v4 = region.v2;
/*      */     } 
/*      */     
/*  868 */     float color = this.color;
/*  869 */     int idx = this.idx;
/*  870 */     vertices[idx] = x1;
/*  871 */     vertices[idx + 1] = y1;
/*  872 */     vertices[idx + 2] = color;
/*  873 */     vertices[idx + 3] = u1;
/*  874 */     vertices[idx + 4] = v1;
/*      */     
/*  876 */     vertices[idx + 5] = x2;
/*  877 */     vertices[idx + 6] = y2;
/*  878 */     vertices[idx + 7] = color;
/*  879 */     vertices[idx + 8] = u2;
/*  880 */     vertices[idx + 9] = v2;
/*      */     
/*  882 */     vertices[idx + 10] = x3;
/*  883 */     vertices[idx + 11] = y3;
/*  884 */     vertices[idx + 12] = color;
/*  885 */     vertices[idx + 13] = u3;
/*  886 */     vertices[idx + 14] = v3;
/*      */     
/*  888 */     vertices[idx + 15] = x4;
/*  889 */     vertices[idx + 16] = y4;
/*  890 */     vertices[idx + 17] = color;
/*  891 */     vertices[idx + 18] = u4;
/*  892 */     vertices[idx + 19] = v4;
/*  893 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float width, float height, Affine2 transform) {
/*  898 */     if (!this.drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
/*      */     
/*  900 */     float[] vertices = this.vertices;
/*      */     
/*  902 */     Texture texture = region.texture;
/*  903 */     if (texture != this.lastTexture) {
/*  904 */       switchTexture(texture);
/*  905 */     } else if (this.idx == vertices.length) {
/*  906 */       flush();
/*      */     } 
/*      */ 
/*      */     
/*  910 */     float x1 = transform.m02;
/*  911 */     float y1 = transform.m12;
/*  912 */     float x2 = transform.m01 * height + transform.m02;
/*  913 */     float y2 = transform.m11 * height + transform.m12;
/*  914 */     float x3 = transform.m00 * width + transform.m01 * height + transform.m02;
/*  915 */     float y3 = transform.m10 * width + transform.m11 * height + transform.m12;
/*  916 */     float x4 = transform.m00 * width + transform.m02;
/*  917 */     float y4 = transform.m10 * width + transform.m12;
/*      */     
/*  919 */     float u = region.u;
/*  920 */     float v = region.v2;
/*  921 */     float u2 = region.u2;
/*  922 */     float v2 = region.v;
/*      */     
/*  924 */     float color = this.color;
/*  925 */     int idx = this.idx;
/*  926 */     vertices[idx] = x1;
/*  927 */     vertices[idx + 1] = y1;
/*  928 */     vertices[idx + 2] = color;
/*  929 */     vertices[idx + 3] = u;
/*  930 */     vertices[idx + 4] = v;
/*      */     
/*  932 */     vertices[idx + 5] = x2;
/*  933 */     vertices[idx + 6] = y2;
/*  934 */     vertices[idx + 7] = color;
/*  935 */     vertices[idx + 8] = u;
/*  936 */     vertices[idx + 9] = v2;
/*      */     
/*  938 */     vertices[idx + 10] = x3;
/*  939 */     vertices[idx + 11] = y3;
/*  940 */     vertices[idx + 12] = color;
/*  941 */     vertices[idx + 13] = u2;
/*  942 */     vertices[idx + 14] = v2;
/*      */     
/*  944 */     vertices[idx + 15] = x4;
/*  945 */     vertices[idx + 16] = y4;
/*  946 */     vertices[idx + 17] = color;
/*  947 */     vertices[idx + 18] = u2;
/*  948 */     vertices[idx + 19] = v;
/*  949 */     this.idx = idx + 20;
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() {
/*  954 */     if (this.idx == 0)
/*      */       return; 
/*  956 */     this.renderCalls++;
/*  957 */     this.totalRenderCalls++;
/*  958 */     int spritesInBatch = this.idx / 20;
/*  959 */     if (spritesInBatch > this.maxSpritesInBatch) this.maxSpritesInBatch = spritesInBatch; 
/*  960 */     int count = spritesInBatch * 6;
/*      */     
/*  962 */     this.lastTexture.bind();
/*  963 */     Mesh mesh = this.mesh;
/*  964 */     mesh.setVertices(this.vertices, 0, this.idx);
/*  965 */     mesh.getIndicesBuffer().position(0);
/*  966 */     mesh.getIndicesBuffer().limit(count);
/*      */     
/*  968 */     if (this.blendingDisabled) {
/*  969 */       Gdx.gl.glDisable(3042);
/*      */     } else {
/*  971 */       Gdx.gl.glEnable(3042);
/*  972 */       if (this.blendSrcFunc != -1) Gdx.gl.glBlendFunc(this.blendSrcFunc, this.blendDstFunc);
/*      */     
/*      */     } 
/*  975 */     mesh.render((this.customShader != null) ? this.customShader : this.shader, 4, 0, count);
/*      */     
/*  977 */     this.idx = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void disableBlending() {
/*  982 */     if (this.blendingDisabled)
/*  983 */       return;  flush();
/*  984 */     this.blendingDisabled = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void enableBlending() {
/*  989 */     if (!this.blendingDisabled)
/*  990 */       return;  flush();
/*  991 */     this.blendingDisabled = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlendFunction(int srcFunc, int dstFunc) {
/*  996 */     if (this.blendSrcFunc == srcFunc && this.blendDstFunc == dstFunc)
/*  997 */       return;  flush();
/*  998 */     this.blendSrcFunc = srcFunc;
/*  999 */     this.blendDstFunc = dstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendSrcFunc() {
/* 1004 */     return this.blendSrcFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendDstFunc() {
/* 1009 */     return this.blendDstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dispose() {
/* 1014 */     this.mesh.dispose();
/* 1015 */     if (this.ownsShader && this.shader != null) this.shader.dispose();
/*      */   
/*      */   }
/*      */   
/*      */   public Matrix4 getProjectionMatrix() {
/* 1020 */     return this.projectionMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getTransformMatrix() {
/* 1025 */     return this.transformMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setProjectionMatrix(Matrix4 projection) {
/* 1030 */     if (this.drawing) flush(); 
/* 1031 */     this.projectionMatrix.set(projection);
/* 1032 */     if (this.drawing) setupMatrices();
/*      */   
/*      */   }
/*      */   
/*      */   public void setTransformMatrix(Matrix4 transform) {
/* 1037 */     if (this.drawing) flush(); 
/* 1038 */     this.transformMatrix.set(transform);
/* 1039 */     if (this.drawing) setupMatrices(); 
/*      */   }
/*      */   
/*      */   private void setupMatrices() {
/* 1043 */     this.combinedMatrix.set(this.projectionMatrix).mul(this.transformMatrix);
/* 1044 */     if (this.customShader != null) {
/* 1045 */       this.customShader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1046 */       this.customShader.setUniformi("u_texture", 0);
/*      */     } else {
/* 1048 */       this.shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1049 */       this.shader.setUniformi("u_texture", 0);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void switchTexture(Texture texture) {
/* 1054 */     flush();
/* 1055 */     this.lastTexture = texture;
/* 1056 */     this.invTexWidth = 1.0F / texture.getWidth();
/* 1057 */     this.invTexHeight = 1.0F / texture.getHeight();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShader(ShaderProgram shader) {
/* 1062 */     if (this.drawing) {
/* 1063 */       flush();
/* 1064 */       if (this.customShader != null) {
/* 1065 */         this.customShader.end();
/*      */       } else {
/* 1067 */         this.shader.end();
/*      */       } 
/* 1069 */     }  this.customShader = shader;
/* 1070 */     if (this.drawing) {
/* 1071 */       if (this.customShader != null) {
/* 1072 */         this.customShader.begin();
/*      */       } else {
/* 1074 */         this.shader.begin();
/* 1075 */       }  setupMatrices();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ShaderProgram getShader() {
/* 1081 */     if (this.customShader == null) {
/* 1082 */       return this.shader;
/*      */     }
/* 1084 */     return this.customShader;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlendingEnabled() {
/* 1089 */     return !this.blendingDisabled;
/*      */   }
/*      */   
/*      */   public boolean isDrawing() {
/* 1093 */     return this.drawing;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\SpriteBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */