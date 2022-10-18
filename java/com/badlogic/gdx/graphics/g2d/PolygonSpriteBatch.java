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
/*      */ public class PolygonSpriteBatch
/*      */   implements Batch
/*      */ {
/*      */   private Mesh mesh;
/*      */   private final float[] vertices;
/*      */   private final short[] triangles;
/*      */   private int vertexIndex;
/*      */   private int triangleIndex;
/*      */   private Texture lastTexture;
/*   66 */   private float invTexWidth = 0.0F; private float invTexHeight = 0.0F;
/*      */   
/*      */   private boolean drawing;
/*   69 */   private final Matrix4 transformMatrix = new Matrix4();
/*   70 */   private final Matrix4 projectionMatrix = new Matrix4();
/*   71 */   private final Matrix4 combinedMatrix = new Matrix4();
/*      */   
/*      */   private boolean blendingDisabled;
/*   74 */   private int blendSrcFunc = 770;
/*   75 */   private int blendDstFunc = 771;
/*      */   
/*      */   private final ShaderProgram shader;
/*      */   
/*      */   private ShaderProgram customShader;
/*      */   private boolean ownsShader;
/*   81 */   float color = Color.WHITE.toFloatBits();
/*   82 */   private Color tempColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */ 
/*      */   
/*   85 */   public int renderCalls = 0;
/*      */ 
/*      */   
/*   88 */   public int totalRenderCalls = 0;
/*      */ 
/*      */   
/*   91 */   public int maxTrianglesInBatch = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   public PolygonSpriteBatch() {
/*   96 */     this(2000, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PolygonSpriteBatch(int size) {
/*  103 */     this(size, size * 2, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PolygonSpriteBatch(int size, ShaderProgram defaultShader) {
/*  110 */     this(size, size * 2, defaultShader);
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
/*      */ 
/*      */   
/*      */   public PolygonSpriteBatch(int maxVertices, int maxTriangles, ShaderProgram defaultShader) {
/*  125 */     if (maxVertices > 32767) {
/*  126 */       throw new IllegalArgumentException("Can't have more than 32767 vertices per batch: " + maxVertices);
/*      */     }
/*  128 */     Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
/*  129 */     if (Gdx.gl30 != null) {
/*  130 */       vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
/*      */     }
/*  132 */     this.mesh = new Mesh(vertexDataType, false, maxVertices, maxTriangles * 3, new VertexAttribute[] { new VertexAttribute(1, 2, "a_position"), new VertexAttribute(4, 4, "a_color"), new VertexAttribute(16, 2, "a_texCoord0") });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  137 */     this.vertices = new float[maxVertices * 5];
/*  138 */     this.triangles = new short[maxTriangles * 3];
/*      */     
/*  140 */     if (defaultShader == null) {
/*  141 */       this.shader = SpriteBatch.createDefaultShader();
/*  142 */       this.ownsShader = true;
/*      */     } else {
/*  144 */       this.shader = defaultShader;
/*      */     } 
/*  146 */     this.projectionMatrix.setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin() {
/*  151 */     if (this.drawing) throw new IllegalStateException("PolygonSpriteBatch.end must be called before begin."); 
/*  152 */     this.renderCalls = 0;
/*      */     
/*  154 */     Gdx.gl.glDepthMask(false);
/*  155 */     if (this.customShader != null) {
/*  156 */       this.customShader.begin();
/*      */     } else {
/*  158 */       this.shader.begin();
/*  159 */     }  setupMatrices();
/*      */     
/*  161 */     this.drawing = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void end() {
/*  166 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before end."); 
/*  167 */     if (this.vertexIndex > 0) flush(); 
/*  168 */     this.lastTexture = null;
/*  169 */     this.drawing = false;
/*      */     
/*  171 */     GL20 gl = Gdx.gl;
/*  172 */     gl.glDepthMask(true);
/*  173 */     if (isBlendingEnabled()) gl.glDisable(3042);
/*      */     
/*  175 */     if (this.customShader != null) {
/*  176 */       this.customShader.end();
/*      */     } else {
/*  178 */       this.shader.end();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setColor(Color tint) {
/*  183 */     this.color = tint.toFloatBits();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  188 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/*  189 */     this.color = NumberUtils.intToFloatColor(intBits);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float color) {
/*  194 */     this.color = color;
/*      */   }
/*      */ 
/*      */   
/*      */   public Color getColor() {
/*  199 */     int intBits = NumberUtils.floatToIntColor(this.color);
/*  200 */     Color color = this.tempColor;
/*  201 */     color.r = (intBits & 0xFF) / 255.0F;
/*  202 */     color.g = (intBits >>> 8 & 0xFF) / 255.0F;
/*  203 */     color.b = (intBits >>> 16 & 0xFF) / 255.0F;
/*  204 */     color.a = (intBits >>> 24 & 0xFF) / 255.0F;
/*  205 */     return color;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPackedColor() {
/*  210 */     return this.color;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(PolygonRegion region, float x, float y) {
/*  215 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  217 */     short[] triangles = this.triangles;
/*  218 */     short[] regionTriangles = region.triangles;
/*  219 */     int regionTrianglesLength = regionTriangles.length;
/*  220 */     float[] regionVertices = region.vertices;
/*  221 */     int regionVerticesLength = regionVertices.length;
/*      */     
/*  223 */     Texture texture = region.region.texture;
/*  224 */     if (texture != this.lastTexture) {
/*  225 */       switchTexture(texture);
/*  226 */     } else if (this.triangleIndex + regionTrianglesLength > triangles.length || this.vertexIndex + regionVerticesLength * 5 / 2 > this.vertices.length) {
/*  227 */       flush();
/*      */     } 
/*  229 */     int triangleIndex = this.triangleIndex;
/*  230 */     int vertexIndex = this.vertexIndex;
/*  231 */     int startVertex = vertexIndex / 5;
/*      */     
/*  233 */     for (int i = 0; i < regionTrianglesLength; i++)
/*  234 */       triangles[triangleIndex++] = (short)(regionTriangles[i] + startVertex); 
/*  235 */     this.triangleIndex = triangleIndex;
/*      */     
/*  237 */     float[] vertices = this.vertices;
/*  238 */     float color = this.color;
/*  239 */     float[] textureCoords = region.textureCoords;
/*      */     
/*  241 */     for (int j = 0; j < regionVerticesLength; j += 2) {
/*  242 */       vertices[vertexIndex++] = regionVertices[j] + x;
/*  243 */       vertices[vertexIndex++] = regionVertices[j + 1] + y;
/*  244 */       vertices[vertexIndex++] = color;
/*  245 */       vertices[vertexIndex++] = textureCoords[j];
/*  246 */       vertices[vertexIndex++] = textureCoords[j + 1];
/*      */     } 
/*  248 */     this.vertexIndex = vertexIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(PolygonRegion region, float x, float y, float width, float height) {
/*  253 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  255 */     short[] triangles = this.triangles;
/*  256 */     short[] regionTriangles = region.triangles;
/*  257 */     int regionTrianglesLength = regionTriangles.length;
/*  258 */     float[] regionVertices = region.vertices;
/*  259 */     int regionVerticesLength = regionVertices.length;
/*  260 */     TextureRegion textureRegion = region.region;
/*      */     
/*  262 */     Texture texture = textureRegion.texture;
/*  263 */     if (texture != this.lastTexture) {
/*  264 */       switchTexture(texture);
/*  265 */     } else if (this.triangleIndex + regionTrianglesLength > triangles.length || this.vertexIndex + regionVerticesLength * 5 / 2 > this.vertices.length) {
/*  266 */       flush();
/*      */     } 
/*  268 */     int triangleIndex = this.triangleIndex;
/*  269 */     int vertexIndex = this.vertexIndex;
/*  270 */     int startVertex = vertexIndex / 5;
/*      */     
/*  272 */     for (int i = 0, n = regionTriangles.length; i < n; i++)
/*  273 */       triangles[triangleIndex++] = (short)(regionTriangles[i] + startVertex); 
/*  274 */     this.triangleIndex = triangleIndex;
/*      */     
/*  276 */     float[] vertices = this.vertices;
/*  277 */     float color = this.color;
/*  278 */     float[] textureCoords = region.textureCoords;
/*  279 */     float sX = width / textureRegion.regionWidth;
/*  280 */     float sY = height / textureRegion.regionHeight;
/*      */     
/*  282 */     for (int j = 0; j < regionVerticesLength; j += 2) {
/*  283 */       vertices[vertexIndex++] = regionVertices[j] * sX + x;
/*  284 */       vertices[vertexIndex++] = regionVertices[j + 1] * sY + y;
/*  285 */       vertices[vertexIndex++] = color;
/*  286 */       vertices[vertexIndex++] = textureCoords[j];
/*  287 */       vertices[vertexIndex++] = textureCoords[j + 1];
/*      */     } 
/*  289 */     this.vertexIndex = vertexIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(PolygonRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*  298 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  300 */     short[] triangles = this.triangles;
/*  301 */     short[] regionTriangles = region.triangles;
/*  302 */     int regionTrianglesLength = regionTriangles.length;
/*  303 */     float[] regionVertices = region.vertices;
/*  304 */     int regionVerticesLength = regionVertices.length;
/*  305 */     TextureRegion textureRegion = region.region;
/*      */     
/*  307 */     Texture texture = textureRegion.texture;
/*  308 */     if (texture != this.lastTexture) {
/*  309 */       switchTexture(texture);
/*  310 */     } else if (this.triangleIndex + regionTrianglesLength > triangles.length || this.vertexIndex + regionVerticesLength * 5 / 2 > this.vertices.length) {
/*  311 */       flush();
/*      */     } 
/*  313 */     int triangleIndex = this.triangleIndex;
/*  314 */     int vertexIndex = this.vertexIndex;
/*  315 */     int startVertex = vertexIndex / 5;
/*      */     
/*  317 */     for (int i = 0; i < regionTrianglesLength; i++)
/*  318 */       triangles[triangleIndex++] = (short)(regionTriangles[i] + startVertex); 
/*  319 */     this.triangleIndex = triangleIndex;
/*      */     
/*  321 */     float[] vertices = this.vertices;
/*  322 */     float color = this.color;
/*  323 */     float[] textureCoords = region.textureCoords;
/*      */     
/*  325 */     float worldOriginX = x + originX;
/*  326 */     float worldOriginY = y + originY;
/*  327 */     float sX = width / textureRegion.regionWidth;
/*  328 */     float sY = height / textureRegion.regionHeight;
/*  329 */     float cos = MathUtils.cosDeg(rotation);
/*  330 */     float sin = MathUtils.sinDeg(rotation);
/*      */ 
/*      */     
/*  333 */     for (int j = 0; j < regionVerticesLength; j += 2) {
/*  334 */       float fx = (regionVertices[j] * sX - originX) * scaleX;
/*  335 */       float fy = (regionVertices[j + 1] * sY - originY) * scaleY;
/*  336 */       vertices[vertexIndex++] = cos * fx - sin * fy + worldOriginX;
/*  337 */       vertices[vertexIndex++] = sin * fx + cos * fy + worldOriginY;
/*  338 */       vertices[vertexIndex++] = color;
/*  339 */       vertices[vertexIndex++] = textureCoords[j];
/*  340 */       vertices[vertexIndex++] = textureCoords[j + 1];
/*      */     } 
/*  342 */     this.vertexIndex = vertexIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float[] polygonVertices, int verticesOffset, int verticesCount, short[] polygonTriangles, int trianglesOffset, int trianglesCount) {
/*  349 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  351 */     short[] triangles = this.triangles;
/*  352 */     float[] vertices = this.vertices;
/*      */     
/*  354 */     if (texture != this.lastTexture) {
/*  355 */       switchTexture(texture);
/*  356 */     } else if (this.triangleIndex + trianglesCount > triangles.length || this.vertexIndex + verticesCount > vertices.length) {
/*  357 */       flush();
/*      */     } 
/*  359 */     int triangleIndex = this.triangleIndex;
/*  360 */     int vertexIndex = this.vertexIndex;
/*  361 */     int startVertex = vertexIndex / 5;
/*      */     
/*  363 */     for (int i = trianglesOffset, n = i + trianglesCount; i < n; i++)
/*  364 */       triangles[triangleIndex++] = (short)(polygonTriangles[i] + startVertex); 
/*  365 */     this.triangleIndex = triangleIndex;
/*      */     
/*  367 */     System.arraycopy(polygonVertices, verticesOffset, vertices, vertexIndex, verticesCount);
/*  368 */     this.vertexIndex += verticesCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  374 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  376 */     short[] triangles = this.triangles;
/*  377 */     float[] vertices = this.vertices;
/*      */     
/*  379 */     if (texture != this.lastTexture) {
/*  380 */       switchTexture(texture);
/*  381 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  382 */       flush();
/*      */     } 
/*  384 */     int triangleIndex = this.triangleIndex;
/*  385 */     int startVertex = this.vertexIndex / 5;
/*  386 */     triangles[triangleIndex++] = (short)startVertex;
/*  387 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  388 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  389 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  390 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  391 */     triangles[triangleIndex++] = (short)startVertex;
/*  392 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/*  395 */     float worldOriginX = x + originX;
/*  396 */     float worldOriginY = y + originY;
/*  397 */     float fx = -originX;
/*  398 */     float fy = -originY;
/*  399 */     float fx2 = width - originX;
/*  400 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  403 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  404 */       fx *= scaleX;
/*  405 */       fy *= scaleY;
/*  406 */       fx2 *= scaleX;
/*  407 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  411 */     float p1x = fx;
/*  412 */     float p1y = fy;
/*  413 */     float p2x = fx;
/*  414 */     float p2y = fy2;
/*  415 */     float p3x = fx2;
/*  416 */     float p3y = fy2;
/*  417 */     float p4x = fx2;
/*  418 */     float p4y = fy;
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
/*  430 */     if (rotation != 0.0F) {
/*  431 */       float cos = MathUtils.cosDeg(rotation);
/*  432 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  434 */       x1 = cos * p1x - sin * p1y;
/*  435 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  437 */       x2 = cos * p2x - sin * p2y;
/*  438 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  440 */       x3 = cos * p3x - sin * p3y;
/*  441 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  443 */       x4 = x1 + x3 - x2;
/*  444 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  446 */       x1 = p1x;
/*  447 */       y1 = p1y;
/*      */       
/*  449 */       x2 = p2x;
/*  450 */       y2 = p2y;
/*      */       
/*  452 */       x3 = p3x;
/*  453 */       y3 = p3y;
/*      */       
/*  455 */       x4 = p4x;
/*  456 */       y4 = p4y;
/*      */     } 
/*      */     
/*  459 */     x1 += worldOriginX;
/*  460 */     y1 += worldOriginY;
/*  461 */     x2 += worldOriginX;
/*  462 */     y2 += worldOriginY;
/*  463 */     x3 += worldOriginX;
/*  464 */     y3 += worldOriginY;
/*  465 */     x4 += worldOriginX;
/*  466 */     y4 += worldOriginY;
/*      */     
/*  468 */     float u = srcX * this.invTexWidth;
/*  469 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  470 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  471 */     float v2 = srcY * this.invTexHeight;
/*      */     
/*  473 */     if (flipX) {
/*  474 */       float tmp = u;
/*  475 */       u = u2;
/*  476 */       u2 = tmp;
/*      */     } 
/*      */     
/*  479 */     if (flipY) {
/*  480 */       float tmp = v;
/*  481 */       v = v2;
/*  482 */       v2 = tmp;
/*      */     } 
/*      */     
/*  485 */     float color = this.color;
/*  486 */     int idx = this.vertexIndex;
/*  487 */     vertices[idx++] = x1;
/*  488 */     vertices[idx++] = y1;
/*  489 */     vertices[idx++] = color;
/*  490 */     vertices[idx++] = u;
/*  491 */     vertices[idx++] = v;
/*      */     
/*  493 */     vertices[idx++] = x2;
/*  494 */     vertices[idx++] = y2;
/*  495 */     vertices[idx++] = color;
/*  496 */     vertices[idx++] = u;
/*  497 */     vertices[idx++] = v2;
/*      */     
/*  499 */     vertices[idx++] = x3;
/*  500 */     vertices[idx++] = y3;
/*  501 */     vertices[idx++] = color;
/*  502 */     vertices[idx++] = u2;
/*  503 */     vertices[idx++] = v2;
/*      */     
/*  505 */     vertices[idx++] = x4;
/*  506 */     vertices[idx++] = y4;
/*  507 */     vertices[idx++] = color;
/*  508 */     vertices[idx++] = u2;
/*  509 */     vertices[idx++] = v;
/*  510 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*  516 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  518 */     short[] triangles = this.triangles;
/*  519 */     float[] vertices = this.vertices;
/*      */     
/*  521 */     if (texture != this.lastTexture) {
/*  522 */       switchTexture(texture);
/*  523 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  524 */       flush();
/*      */     } 
/*  526 */     int triangleIndex = this.triangleIndex;
/*  527 */     int startVertex = this.vertexIndex / 5;
/*  528 */     triangles[triangleIndex++] = (short)startVertex;
/*  529 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  530 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  531 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  532 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  533 */     triangles[triangleIndex++] = (short)startVertex;
/*  534 */     this.triangleIndex = triangleIndex;
/*      */     
/*  536 */     float u = srcX * this.invTexWidth;
/*  537 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  538 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  539 */     float v2 = srcY * this.invTexHeight;
/*  540 */     float fx2 = x + width;
/*  541 */     float fy2 = y + height;
/*      */     
/*  543 */     if (flipX) {
/*  544 */       float tmp = u;
/*  545 */       u = u2;
/*  546 */       u2 = tmp;
/*      */     } 
/*      */     
/*  549 */     if (flipY) {
/*  550 */       float tmp = v;
/*  551 */       v = v2;
/*  552 */       v2 = tmp;
/*      */     } 
/*      */     
/*  555 */     float color = this.color;
/*  556 */     int idx = this.vertexIndex;
/*  557 */     vertices[idx++] = x;
/*  558 */     vertices[idx++] = y;
/*  559 */     vertices[idx++] = color;
/*  560 */     vertices[idx++] = u;
/*  561 */     vertices[idx++] = v;
/*      */     
/*  563 */     vertices[idx++] = x;
/*  564 */     vertices[idx++] = fy2;
/*  565 */     vertices[idx++] = color;
/*  566 */     vertices[idx++] = u;
/*  567 */     vertices[idx++] = v2;
/*      */     
/*  569 */     vertices[idx++] = fx2;
/*  570 */     vertices[idx++] = fy2;
/*  571 */     vertices[idx++] = color;
/*  572 */     vertices[idx++] = u2;
/*  573 */     vertices[idx++] = v2;
/*      */     
/*  575 */     vertices[idx++] = fx2;
/*  576 */     vertices[idx++] = y;
/*  577 */     vertices[idx++] = color;
/*  578 */     vertices[idx++] = u2;
/*  579 */     vertices[idx++] = v;
/*  580 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
/*  585 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  587 */     short[] triangles = this.triangles;
/*  588 */     float[] vertices = this.vertices;
/*      */     
/*  590 */     if (texture != this.lastTexture) {
/*  591 */       switchTexture(texture);
/*  592 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  593 */       flush();
/*      */     } 
/*  595 */     int triangleIndex = this.triangleIndex;
/*  596 */     int startVertex = this.vertexIndex / 5;
/*  597 */     triangles[triangleIndex++] = (short)startVertex;
/*  598 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  599 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  600 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  601 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  602 */     triangles[triangleIndex++] = (short)startVertex;
/*  603 */     this.triangleIndex = triangleIndex;
/*      */     
/*  605 */     float u = srcX * this.invTexWidth;
/*  606 */     float v = (srcY + srcHeight) * this.invTexHeight;
/*  607 */     float u2 = (srcX + srcWidth) * this.invTexWidth;
/*  608 */     float v2 = srcY * this.invTexHeight;
/*  609 */     float fx2 = x + srcWidth;
/*  610 */     float fy2 = y + srcHeight;
/*      */     
/*  612 */     float color = this.color;
/*  613 */     int idx = this.vertexIndex;
/*  614 */     vertices[idx++] = x;
/*  615 */     vertices[idx++] = y;
/*  616 */     vertices[idx++] = color;
/*  617 */     vertices[idx++] = u;
/*  618 */     vertices[idx++] = v;
/*      */     
/*  620 */     vertices[idx++] = x;
/*  621 */     vertices[idx++] = fy2;
/*  622 */     vertices[idx++] = color;
/*  623 */     vertices[idx++] = u;
/*  624 */     vertices[idx++] = v2;
/*      */     
/*  626 */     vertices[idx++] = fx2;
/*  627 */     vertices[idx++] = fy2;
/*  628 */     vertices[idx++] = color;
/*  629 */     vertices[idx++] = u2;
/*  630 */     vertices[idx++] = v2;
/*      */     
/*  632 */     vertices[idx++] = fx2;
/*  633 */     vertices[idx++] = y;
/*  634 */     vertices[idx++] = color;
/*  635 */     vertices[idx++] = u2;
/*  636 */     vertices[idx++] = v;
/*  637 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
/*  642 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  644 */     short[] triangles = this.triangles;
/*  645 */     float[] vertices = this.vertices;
/*      */     
/*  647 */     if (texture != this.lastTexture) {
/*  648 */       switchTexture(texture);
/*  649 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  650 */       flush();
/*      */     } 
/*  652 */     int triangleIndex = this.triangleIndex;
/*  653 */     int startVertex = this.vertexIndex / 5;
/*  654 */     triangles[triangleIndex++] = (short)startVertex;
/*  655 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  656 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  657 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  658 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  659 */     triangles[triangleIndex++] = (short)startVertex;
/*  660 */     this.triangleIndex = triangleIndex;
/*      */     
/*  662 */     float fx2 = x + width;
/*  663 */     float fy2 = y + height;
/*      */     
/*  665 */     float color = this.color;
/*  666 */     int idx = this.vertexIndex;
/*  667 */     vertices[idx++] = x;
/*  668 */     vertices[idx++] = y;
/*  669 */     vertices[idx++] = color;
/*  670 */     vertices[idx++] = u;
/*  671 */     vertices[idx++] = v;
/*      */     
/*  673 */     vertices[idx++] = x;
/*  674 */     vertices[idx++] = fy2;
/*  675 */     vertices[idx++] = color;
/*  676 */     vertices[idx++] = u;
/*  677 */     vertices[idx++] = v2;
/*      */     
/*  679 */     vertices[idx++] = fx2;
/*  680 */     vertices[idx++] = fy2;
/*  681 */     vertices[idx++] = color;
/*  682 */     vertices[idx++] = u2;
/*  683 */     vertices[idx++] = v2;
/*      */     
/*  685 */     vertices[idx++] = fx2;
/*  686 */     vertices[idx++] = y;
/*  687 */     vertices[idx++] = color;
/*  688 */     vertices[idx++] = u2;
/*  689 */     vertices[idx++] = v;
/*  690 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y) {
/*  695 */     draw(texture, x, y, texture.getWidth(), texture.getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float x, float y, float width, float height) {
/*  700 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  702 */     short[] triangles = this.triangles;
/*  703 */     float[] vertices = this.vertices;
/*      */     
/*  705 */     if (texture != this.lastTexture) {
/*  706 */       switchTexture(texture);
/*  707 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  708 */       flush();
/*      */     } 
/*  710 */     int triangleIndex = this.triangleIndex;
/*  711 */     int startVertex = this.vertexIndex / 5;
/*  712 */     triangles[triangleIndex++] = (short)startVertex;
/*  713 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  714 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  715 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  716 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  717 */     triangles[triangleIndex++] = (short)startVertex;
/*  718 */     this.triangleIndex = triangleIndex;
/*      */     
/*  720 */     float fx2 = x + width;
/*  721 */     float fy2 = y + height;
/*  722 */     float u = 0.0F;
/*  723 */     float v = 1.0F;
/*  724 */     float u2 = 1.0F;
/*  725 */     float v2 = 0.0F;
/*      */     
/*  727 */     float color = this.color;
/*  728 */     int idx = this.vertexIndex;
/*  729 */     vertices[idx++] = x;
/*  730 */     vertices[idx++] = y;
/*  731 */     vertices[idx++] = color;
/*  732 */     vertices[idx++] = 0.0F;
/*  733 */     vertices[idx++] = 1.0F;
/*      */     
/*  735 */     vertices[idx++] = x;
/*  736 */     vertices[idx++] = fy2;
/*  737 */     vertices[idx++] = color;
/*  738 */     vertices[idx++] = 0.0F;
/*  739 */     vertices[idx++] = 0.0F;
/*      */     
/*  741 */     vertices[idx++] = fx2;
/*  742 */     vertices[idx++] = fy2;
/*  743 */     vertices[idx++] = color;
/*  744 */     vertices[idx++] = 1.0F;
/*  745 */     vertices[idx++] = 0.0F;
/*      */     
/*  747 */     vertices[idx++] = fx2;
/*  748 */     vertices[idx++] = y;
/*  749 */     vertices[idx++] = color;
/*  750 */     vertices[idx++] = 1.0F;
/*  751 */     vertices[idx++] = 1.0F;
/*  752 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
/*  757 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  759 */     short[] triangles = this.triangles;
/*  760 */     float[] vertices = this.vertices;
/*      */     
/*  762 */     int triangleCount = count / 20 * 6;
/*  763 */     if (texture != this.lastTexture) {
/*  764 */       switchTexture(texture);
/*  765 */     } else if (this.triangleIndex + triangleCount > triangles.length || this.vertexIndex + count > vertices.length) {
/*  766 */       flush();
/*      */     } 
/*  768 */     int vertexIndex = this.vertexIndex;
/*  769 */     int triangleIndex = this.triangleIndex;
/*  770 */     short vertex = (short)(vertexIndex / 5);
/*  771 */     for (int n = triangleIndex + triangleCount; triangleIndex < n; triangleIndex += 6, vertex = (short)(vertex + 4)) {
/*  772 */       triangles[triangleIndex] = vertex;
/*  773 */       triangles[triangleIndex + 1] = (short)(vertex + 1);
/*  774 */       triangles[triangleIndex + 2] = (short)(vertex + 2);
/*  775 */       triangles[triangleIndex + 3] = (short)(vertex + 2);
/*  776 */       triangles[triangleIndex + 4] = (short)(vertex + 3);
/*  777 */       triangles[triangleIndex + 5] = vertex;
/*      */     } 
/*  779 */     this.triangleIndex = triangleIndex;
/*      */     
/*  781 */     System.arraycopy(spriteVertices, offset, vertices, vertexIndex, count);
/*  782 */     this.vertexIndex += count;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y) {
/*  787 */     draw(region, x, y, region.getRegionWidth(), region.getRegionHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float width, float height) {
/*  792 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  794 */     short[] triangles = this.triangles;
/*  795 */     float[] vertices = this.vertices;
/*      */     
/*  797 */     Texture texture = region.texture;
/*  798 */     if (texture != this.lastTexture) {
/*  799 */       switchTexture(texture);
/*  800 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  801 */       flush();
/*      */     } 
/*  803 */     int triangleIndex = this.triangleIndex;
/*  804 */     int startVertex = this.vertexIndex / 5;
/*  805 */     triangles[triangleIndex++] = (short)startVertex;
/*  806 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  807 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  808 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  809 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  810 */     triangles[triangleIndex++] = (short)startVertex;
/*  811 */     this.triangleIndex = triangleIndex;
/*      */     
/*  813 */     float fx2 = x + width;
/*  814 */     float fy2 = y + height;
/*  815 */     float u = region.u;
/*  816 */     float v = region.v2;
/*  817 */     float u2 = region.u2;
/*  818 */     float v2 = region.v;
/*      */     
/*  820 */     float color = this.color;
/*  821 */     int idx = this.vertexIndex;
/*  822 */     vertices[idx++] = x;
/*  823 */     vertices[idx++] = y;
/*  824 */     vertices[idx++] = color;
/*  825 */     vertices[idx++] = u;
/*  826 */     vertices[idx++] = v;
/*      */     
/*  828 */     vertices[idx++] = x;
/*  829 */     vertices[idx++] = fy2;
/*  830 */     vertices[idx++] = color;
/*  831 */     vertices[idx++] = u;
/*  832 */     vertices[idx++] = v2;
/*      */     
/*  834 */     vertices[idx++] = fx2;
/*  835 */     vertices[idx++] = fy2;
/*  836 */     vertices[idx++] = color;
/*  837 */     vertices[idx++] = u2;
/*  838 */     vertices[idx++] = v2;
/*      */     
/*  840 */     vertices[idx++] = fx2;
/*  841 */     vertices[idx++] = y;
/*  842 */     vertices[idx++] = color;
/*  843 */     vertices[idx++] = u2;
/*  844 */     vertices[idx++] = v;
/*  845 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4;
/*  851 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  853 */     short[] triangles = this.triangles;
/*  854 */     float[] vertices = this.vertices;
/*      */     
/*  856 */     Texture texture = region.texture;
/*  857 */     if (texture != this.lastTexture) {
/*  858 */       switchTexture(texture);
/*  859 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  860 */       flush();
/*      */     } 
/*  862 */     int triangleIndex = this.triangleIndex;
/*  863 */     int startVertex = this.vertexIndex / 5;
/*  864 */     triangles[triangleIndex++] = (short)startVertex;
/*  865 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  866 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  867 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  868 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/*  869 */     triangles[triangleIndex++] = (short)startVertex;
/*  870 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/*  873 */     float worldOriginX = x + originX;
/*  874 */     float worldOriginY = y + originY;
/*  875 */     float fx = -originX;
/*  876 */     float fy = -originY;
/*  877 */     float fx2 = width - originX;
/*  878 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  881 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  882 */       fx *= scaleX;
/*  883 */       fy *= scaleY;
/*  884 */       fx2 *= scaleX;
/*  885 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  889 */     float p1x = fx;
/*  890 */     float p1y = fy;
/*  891 */     float p2x = fx;
/*  892 */     float p2y = fy2;
/*  893 */     float p3x = fx2;
/*  894 */     float p3y = fy2;
/*  895 */     float p4x = fx2;
/*  896 */     float p4y = fy;
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
/*  908 */     if (rotation != 0.0F) {
/*  909 */       float cos = MathUtils.cosDeg(rotation);
/*  910 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  912 */       x1 = cos * p1x - sin * p1y;
/*  913 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  915 */       x2 = cos * p2x - sin * p2y;
/*  916 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  918 */       x3 = cos * p3x - sin * p3y;
/*  919 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  921 */       x4 = x1 + x3 - x2;
/*  922 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  924 */       x1 = p1x;
/*  925 */       y1 = p1y;
/*      */       
/*  927 */       x2 = p2x;
/*  928 */       y2 = p2y;
/*      */       
/*  930 */       x3 = p3x;
/*  931 */       y3 = p3y;
/*      */       
/*  933 */       x4 = p4x;
/*  934 */       y4 = p4y;
/*      */     } 
/*      */     
/*  937 */     x1 += worldOriginX;
/*  938 */     y1 += worldOriginY;
/*  939 */     x2 += worldOriginX;
/*  940 */     y2 += worldOriginY;
/*  941 */     x3 += worldOriginX;
/*  942 */     y3 += worldOriginY;
/*  943 */     x4 += worldOriginX;
/*  944 */     y4 += worldOriginY;
/*      */     
/*  946 */     float u = region.u;
/*  947 */     float v = region.v2;
/*  948 */     float u2 = region.u2;
/*  949 */     float v2 = region.v;
/*      */     
/*  951 */     float color = this.color;
/*  952 */     int idx = this.vertexIndex;
/*  953 */     vertices[idx++] = x1;
/*  954 */     vertices[idx++] = y1;
/*  955 */     vertices[idx++] = color;
/*  956 */     vertices[idx++] = u;
/*  957 */     vertices[idx++] = v;
/*      */     
/*  959 */     vertices[idx++] = x2;
/*  960 */     vertices[idx++] = y2;
/*  961 */     vertices[idx++] = color;
/*  962 */     vertices[idx++] = u;
/*  963 */     vertices[idx++] = v2;
/*      */     
/*  965 */     vertices[idx++] = x3;
/*  966 */     vertices[idx++] = y3;
/*  967 */     vertices[idx++] = color;
/*  968 */     vertices[idx++] = u2;
/*  969 */     vertices[idx++] = v2;
/*      */     
/*  971 */     vertices[idx++] = x4;
/*  972 */     vertices[idx++] = y4;
/*  973 */     vertices[idx++] = color;
/*  974 */     vertices[idx++] = u2;
/*  975 */     vertices[idx++] = v;
/*  976 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
/*      */     float x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4;
/*  982 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/*  984 */     short[] triangles = this.triangles;
/*  985 */     float[] vertices = this.vertices;
/*      */     
/*  987 */     Texture texture = region.texture;
/*  988 */     if (texture != this.lastTexture) {
/*  989 */       switchTexture(texture);
/*  990 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/*  991 */       flush();
/*      */     } 
/*  993 */     int triangleIndex = this.triangleIndex;
/*  994 */     int startVertex = this.vertexIndex / 5;
/*  995 */     triangles[triangleIndex++] = (short)startVertex;
/*  996 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/*  997 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  998 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/*  999 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/* 1000 */     triangles[triangleIndex++] = (short)startVertex;
/* 1001 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/* 1004 */     float worldOriginX = x + originX;
/* 1005 */     float worldOriginY = y + originY;
/* 1006 */     float fx = -originX;
/* 1007 */     float fy = -originY;
/* 1008 */     float fx2 = width - originX;
/* 1009 */     float fy2 = height - originY;
/*      */ 
/*      */     
/* 1012 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/* 1013 */       fx *= scaleX;
/* 1014 */       fy *= scaleY;
/* 1015 */       fx2 *= scaleX;
/* 1016 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/* 1020 */     float p1x = fx;
/* 1021 */     float p1y = fy;
/* 1022 */     float p2x = fx;
/* 1023 */     float p2y = fy2;
/* 1024 */     float p3x = fx2;
/* 1025 */     float p3y = fy2;
/* 1026 */     float p4x = fx2;
/* 1027 */     float p4y = fy;
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
/* 1039 */     if (rotation != 0.0F) {
/* 1040 */       float cos = MathUtils.cosDeg(rotation);
/* 1041 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/* 1043 */       x1 = cos * p1x - sin * p1y;
/* 1044 */       y1 = sin * p1x + cos * p1y;
/*      */       
/* 1046 */       x2 = cos * p2x - sin * p2y;
/* 1047 */       y2 = sin * p2x + cos * p2y;
/*      */       
/* 1049 */       x3 = cos * p3x - sin * p3y;
/* 1050 */       y3 = sin * p3x + cos * p3y;
/*      */       
/* 1052 */       x4 = x1 + x3 - x2;
/* 1053 */       y4 = y3 - y2 - y1;
/*      */     } else {
/* 1055 */       x1 = p1x;
/* 1056 */       y1 = p1y;
/*      */       
/* 1058 */       x2 = p2x;
/* 1059 */       y2 = p2y;
/*      */       
/* 1061 */       x3 = p3x;
/* 1062 */       y3 = p3y;
/*      */       
/* 1064 */       x4 = p4x;
/* 1065 */       y4 = p4y;
/*      */     } 
/*      */     
/* 1068 */     x1 += worldOriginX;
/* 1069 */     y1 += worldOriginY;
/* 1070 */     x2 += worldOriginX;
/* 1071 */     y2 += worldOriginY;
/* 1072 */     x3 += worldOriginX;
/* 1073 */     y3 += worldOriginY;
/* 1074 */     x4 += worldOriginX;
/* 1075 */     y4 += worldOriginY;
/*      */ 
/*      */     
/* 1078 */     if (clockwise) {
/* 1079 */       u1 = region.u2;
/* 1080 */       v1 = region.v2;
/* 1081 */       u2 = region.u;
/* 1082 */       v2 = region.v2;
/* 1083 */       u3 = region.u;
/* 1084 */       v3 = region.v;
/* 1085 */       u4 = region.u2;
/* 1086 */       v4 = region.v;
/*      */     } else {
/* 1088 */       u1 = region.u;
/* 1089 */       v1 = region.v;
/* 1090 */       u2 = region.u2;
/* 1091 */       v2 = region.v;
/* 1092 */       u3 = region.u2;
/* 1093 */       v3 = region.v2;
/* 1094 */       u4 = region.u;
/* 1095 */       v4 = region.v2;
/*      */     } 
/*      */     
/* 1098 */     float color = this.color;
/* 1099 */     int idx = this.vertexIndex;
/* 1100 */     vertices[idx++] = x1;
/* 1101 */     vertices[idx++] = y1;
/* 1102 */     vertices[idx++] = color;
/* 1103 */     vertices[idx++] = u1;
/* 1104 */     vertices[idx++] = v1;
/*      */     
/* 1106 */     vertices[idx++] = x2;
/* 1107 */     vertices[idx++] = y2;
/* 1108 */     vertices[idx++] = color;
/* 1109 */     vertices[idx++] = u2;
/* 1110 */     vertices[idx++] = v2;
/*      */     
/* 1112 */     vertices[idx++] = x3;
/* 1113 */     vertices[idx++] = y3;
/* 1114 */     vertices[idx++] = color;
/* 1115 */     vertices[idx++] = u3;
/* 1116 */     vertices[idx++] = v3;
/*      */     
/* 1118 */     vertices[idx++] = x4;
/* 1119 */     vertices[idx++] = y4;
/* 1120 */     vertices[idx++] = color;
/* 1121 */     vertices[idx++] = u4;
/* 1122 */     vertices[idx++] = v4;
/* 1123 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(TextureRegion region, float width, float height, Affine2 transform) {
/* 1128 */     if (!this.drawing) throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");
/*      */     
/* 1130 */     short[] triangles = this.triangles;
/* 1131 */     float[] vertices = this.vertices;
/*      */     
/* 1133 */     Texture texture = region.texture;
/* 1134 */     if (texture != this.lastTexture) {
/* 1135 */       switchTexture(texture);
/* 1136 */     } else if (this.triangleIndex + 6 > triangles.length || this.vertexIndex + 20 > vertices.length) {
/* 1137 */       flush();
/*      */     } 
/* 1139 */     int triangleIndex = this.triangleIndex;
/* 1140 */     int startVertex = this.vertexIndex / 5;
/* 1141 */     triangles[triangleIndex++] = (short)startVertex;
/* 1142 */     triangles[triangleIndex++] = (short)(startVertex + 1);
/* 1143 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/* 1144 */     triangles[triangleIndex++] = (short)(startVertex + 2);
/* 1145 */     triangles[triangleIndex++] = (short)(startVertex + 3);
/* 1146 */     triangles[triangleIndex++] = (short)startVertex;
/* 1147 */     this.triangleIndex = triangleIndex;
/*      */ 
/*      */     
/* 1150 */     float x1 = transform.m02;
/* 1151 */     float y1 = transform.m12;
/* 1152 */     float x2 = transform.m01 * height + transform.m02;
/* 1153 */     float y2 = transform.m11 * height + transform.m12;
/* 1154 */     float x3 = transform.m00 * width + transform.m01 * height + transform.m02;
/* 1155 */     float y3 = transform.m10 * width + transform.m11 * height + transform.m12;
/* 1156 */     float x4 = transform.m00 * width + transform.m02;
/* 1157 */     float y4 = transform.m10 * width + transform.m12;
/*      */     
/* 1159 */     float u = region.u;
/* 1160 */     float v = region.v2;
/* 1161 */     float u2 = region.u2;
/* 1162 */     float v2 = region.v;
/*      */     
/* 1164 */     float color = this.color;
/* 1165 */     int idx = this.vertexIndex;
/* 1166 */     vertices[idx++] = x1;
/* 1167 */     vertices[idx++] = y1;
/* 1168 */     vertices[idx++] = color;
/* 1169 */     vertices[idx++] = u;
/* 1170 */     vertices[idx++] = v;
/*      */     
/* 1172 */     vertices[idx++] = x2;
/* 1173 */     vertices[idx++] = y2;
/* 1174 */     vertices[idx++] = color;
/* 1175 */     vertices[idx++] = u;
/* 1176 */     vertices[idx++] = v2;
/*      */     
/* 1178 */     vertices[idx++] = x3;
/* 1179 */     vertices[idx++] = y3;
/* 1180 */     vertices[idx++] = color;
/* 1181 */     vertices[idx++] = u2;
/* 1182 */     vertices[idx++] = v2;
/*      */     
/* 1184 */     vertices[idx++] = x4;
/* 1185 */     vertices[idx++] = y4;
/* 1186 */     vertices[idx++] = color;
/* 1187 */     vertices[idx++] = u2;
/* 1188 */     vertices[idx++] = v;
/* 1189 */     this.vertexIndex = idx;
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() {
/* 1194 */     if (this.vertexIndex == 0)
/*      */       return; 
/* 1196 */     this.renderCalls++;
/* 1197 */     this.totalRenderCalls++;
/* 1198 */     int trianglesInBatch = this.triangleIndex;
/* 1199 */     if (trianglesInBatch > this.maxTrianglesInBatch) this.maxTrianglesInBatch = trianglesInBatch;
/*      */     
/* 1201 */     this.lastTexture.bind();
/* 1202 */     Mesh mesh = this.mesh;
/* 1203 */     mesh.setVertices(this.vertices, 0, this.vertexIndex);
/* 1204 */     mesh.setIndices(this.triangles, 0, this.triangleIndex);
/* 1205 */     if (this.blendingDisabled) {
/* 1206 */       Gdx.gl.glDisable(3042);
/*      */     } else {
/* 1208 */       Gdx.gl.glEnable(3042);
/* 1209 */       if (this.blendSrcFunc != -1) Gdx.gl.glBlendFunc(this.blendSrcFunc, this.blendDstFunc);
/*      */     
/*      */     } 
/* 1212 */     mesh.render((this.customShader != null) ? this.customShader : this.shader, 4, 0, trianglesInBatch);
/*      */     
/* 1214 */     this.vertexIndex = 0;
/* 1215 */     this.triangleIndex = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void disableBlending() {
/* 1220 */     flush();
/* 1221 */     this.blendingDisabled = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void enableBlending() {
/* 1226 */     flush();
/* 1227 */     this.blendingDisabled = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlendFunction(int srcFunc, int dstFunc) {
/* 1232 */     if (this.blendSrcFunc == srcFunc && this.blendDstFunc == dstFunc)
/* 1233 */       return;  flush();
/* 1234 */     this.blendSrcFunc = srcFunc;
/* 1235 */     this.blendDstFunc = dstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendSrcFunc() {
/* 1240 */     return this.blendSrcFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlendDstFunc() {
/* 1245 */     return this.blendDstFunc;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dispose() {
/* 1250 */     this.mesh.dispose();
/* 1251 */     if (this.ownsShader && this.shader != null) this.shader.dispose();
/*      */   
/*      */   }
/*      */   
/*      */   public Matrix4 getProjectionMatrix() {
/* 1256 */     return this.projectionMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getTransformMatrix() {
/* 1261 */     return this.transformMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setProjectionMatrix(Matrix4 projection) {
/* 1266 */     if (this.drawing) flush(); 
/* 1267 */     this.projectionMatrix.set(projection);
/* 1268 */     if (this.drawing) setupMatrices();
/*      */   
/*      */   }
/*      */   
/*      */   public void setTransformMatrix(Matrix4 transform) {
/* 1273 */     if (this.drawing) flush(); 
/* 1274 */     this.transformMatrix.set(transform);
/* 1275 */     if (this.drawing) setupMatrices(); 
/*      */   }
/*      */   
/*      */   private void setupMatrices() {
/* 1279 */     this.combinedMatrix.set(this.projectionMatrix).mul(this.transformMatrix);
/* 1280 */     if (this.customShader != null) {
/* 1281 */       this.customShader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1282 */       this.customShader.setUniformi("u_texture", 0);
/*      */     } else {
/* 1284 */       this.shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/* 1285 */       this.shader.setUniformi("u_texture", 0);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void switchTexture(Texture texture) {
/* 1290 */     flush();
/* 1291 */     this.lastTexture = texture;
/* 1292 */     this.invTexWidth = 1.0F / texture.getWidth();
/* 1293 */     this.invTexHeight = 1.0F / texture.getHeight();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShader(ShaderProgram shader) {
/* 1298 */     if (this.drawing) {
/* 1299 */       flush();
/* 1300 */       if (this.customShader != null) {
/* 1301 */         this.customShader.end();
/*      */       } else {
/* 1303 */         this.shader.end();
/*      */       } 
/* 1305 */     }  this.customShader = shader;
/* 1306 */     if (this.drawing) {
/* 1307 */       if (this.customShader != null) {
/* 1308 */         this.customShader.begin();
/*      */       } else {
/* 1310 */         this.shader.begin();
/* 1311 */       }  setupMatrices();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ShaderProgram getShader() {
/* 1317 */     if (this.customShader == null) {
/* 1318 */       return this.shader;
/*      */     }
/* 1320 */     return this.customShader;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlendingEnabled() {
/* 1325 */     return !this.blendingDisabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDrawing() {
/* 1330 */     return this.drawing;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\PolygonSpriteBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */