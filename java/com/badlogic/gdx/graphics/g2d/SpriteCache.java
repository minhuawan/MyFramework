/*      */ package com.badlogic.gdx.graphics.g2d;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.GL20;
/*      */ import com.badlogic.gdx.graphics.Mesh;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.VertexAttribute;
/*      */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Matrix4;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.Disposable;
/*      */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*      */ import com.badlogic.gdx.utils.IntArray;
/*      */ import com.badlogic.gdx.utils.NumberUtils;
/*      */ import java.nio.FloatBuffer;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SpriteCache
/*      */   implements Disposable
/*      */ {
/*   70 */   private static final float[] tempVertices = new float[30];
/*      */   
/*      */   private final Mesh mesh;
/*      */   private boolean drawing;
/*   74 */   private final Matrix4 transformMatrix = new Matrix4();
/*   75 */   private final Matrix4 projectionMatrix = new Matrix4();
/*   76 */   private Array<Cache> caches = new Array();
/*      */   
/*   78 */   private final Matrix4 combinedMatrix = new Matrix4();
/*      */   
/*      */   private final ShaderProgram shader;
/*      */   private Cache currentCache;
/*   82 */   private final Array<Texture> textures = new Array(8);
/*   83 */   private final IntArray counts = new IntArray(8);
/*      */   
/*   85 */   private float color = Color.WHITE.toFloatBits();
/*   86 */   private Color tempColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   
/*   88 */   private ShaderProgram customShader = null;
/*      */ 
/*      */   
/*   91 */   public int renderCalls = 0;
/*      */ 
/*      */   
/*   94 */   public int totalRenderCalls = 0;
/*      */ 
/*      */   
/*      */   public SpriteCache() {
/*   98 */     this(1000, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SpriteCache(int size, boolean useIndices) {
/*  106 */     this(size, createDefaultShader(), useIndices);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SpriteCache(int size, ShaderProgram shader, boolean useIndices) {
/*  114 */     this.shader = shader;
/*      */     
/*  116 */     if (useIndices && size > 8191) throw new IllegalArgumentException("Can't have more than 8191 sprites per batch: " + size);
/*      */     
/*  118 */     this.mesh = new Mesh(true, size * (useIndices ? 4 : 6), useIndices ? (size * 6) : 0, new VertexAttribute[] { new VertexAttribute(1, 2, "a_position"), new VertexAttribute(4, 4, "a_color"), new VertexAttribute(16, 2, "a_texCoord0") });
/*      */ 
/*      */     
/*  121 */     this.mesh.setAutoBind(false);
/*      */     
/*  123 */     if (useIndices) {
/*  124 */       int length = size * 6;
/*  125 */       short[] indices = new short[length];
/*  126 */       short j = 0;
/*  127 */       for (int i = 0; i < length; i += 6, j = (short)(j + 4)) {
/*  128 */         indices[i + 0] = j;
/*  129 */         indices[i + 1] = (short)(j + 1);
/*  130 */         indices[i + 2] = (short)(j + 2);
/*  131 */         indices[i + 3] = (short)(j + 2);
/*  132 */         indices[i + 4] = (short)(j + 3);
/*  133 */         indices[i + 5] = j;
/*      */       } 
/*  135 */       this.mesh.setIndices(indices);
/*      */     } 
/*      */     
/*  138 */     this.projectionMatrix.setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(Color tint) {
/*  143 */     this.color = tint.toFloatBits();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  148 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/*  149 */     this.color = NumberUtils.intToFloatColor(intBits);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColor(float color) {
/*  155 */     this.color = color;
/*      */   }
/*      */   
/*      */   public Color getColor() {
/*  159 */     int intBits = NumberUtils.floatToIntColor(this.color);
/*  160 */     Color color = this.tempColor;
/*  161 */     color.r = (intBits & 0xFF) / 255.0F;
/*  162 */     color.g = (intBits >>> 8 & 0xFF) / 255.0F;
/*  163 */     color.b = (intBits >>> 16 & 0xFF) / 255.0F;
/*  164 */     color.a = (intBits >>> 24 & 0xFF) / 255.0F;
/*  165 */     return color;
/*      */   }
/*      */ 
/*      */   
/*      */   public void beginCache() {
/*  170 */     if (this.currentCache != null) throw new IllegalStateException("endCache must be called before begin."); 
/*  171 */     int verticesPerImage = (this.mesh.getNumIndices() > 0) ? 4 : 6;
/*  172 */     this.currentCache = new Cache(this.caches.size, this.mesh.getVerticesBuffer().limit());
/*  173 */     this.caches.add(this.currentCache);
/*  174 */     this.mesh.getVerticesBuffer().compact();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginCache(int cacheID) {
/*  181 */     if (this.currentCache != null) throw new IllegalStateException("endCache must be called before begin."); 
/*  182 */     if (cacheID == this.caches.size - 1) {
/*  183 */       Cache oldCache = (Cache)this.caches.removeIndex(cacheID);
/*  184 */       this.mesh.getVerticesBuffer().limit(oldCache.offset);
/*  185 */       beginCache();
/*      */       return;
/*      */     } 
/*  188 */     this.currentCache = (Cache)this.caches.get(cacheID);
/*  189 */     this.mesh.getVerticesBuffer().position(this.currentCache.offset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int endCache() {
/*  194 */     if (this.currentCache == null) throw new IllegalStateException("beginCache must be called before endCache."); 
/*  195 */     Cache cache = this.currentCache;
/*  196 */     int cacheCount = this.mesh.getVerticesBuffer().position() - cache.offset;
/*  197 */     if (cache.textures == null) {
/*      */       
/*  199 */       cache.maxCount = cacheCount;
/*  200 */       cache.textureCount = this.textures.size;
/*  201 */       cache.textures = (Texture[])this.textures.toArray(Texture.class);
/*  202 */       cache.counts = new int[cache.textureCount];
/*  203 */       for (int i = 0, n = this.counts.size; i < n; i++) {
/*  204 */         cache.counts[i] = this.counts.get(i);
/*      */       }
/*  206 */       this.mesh.getVerticesBuffer().flip();
/*      */     } else {
/*      */       
/*  209 */       if (cacheCount > cache.maxCount) {
/*  210 */         throw new GdxRuntimeException("If a cache is not the last created, it cannot be redefined with more entries than when it was first created: " + cacheCount + " (" + cache.maxCount + " max)");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  215 */       cache.textureCount = this.textures.size;
/*      */       
/*  217 */       if (cache.textures.length < cache.textureCount) cache.textures = new Texture[cache.textureCount];  int i, n;
/*  218 */       for (i = 0, n = cache.textureCount; i < n; i++) {
/*  219 */         cache.textures[i] = (Texture)this.textures.get(i);
/*      */       }
/*  221 */       if (cache.counts.length < cache.textureCount) cache.counts = new int[cache.textureCount]; 
/*  222 */       for (i = 0, n = cache.textureCount; i < n; i++) {
/*  223 */         cache.counts[i] = this.counts.get(i);
/*      */       }
/*  225 */       FloatBuffer vertices = this.mesh.getVerticesBuffer();
/*  226 */       vertices.position(0);
/*  227 */       Cache lastCache = (Cache)this.caches.get(this.caches.size - 1);
/*  228 */       vertices.limit(lastCache.offset + lastCache.maxCount);
/*      */     } 
/*      */     
/*  231 */     this.currentCache = null;
/*  232 */     this.textures.clear();
/*  233 */     this.counts.clear();
/*      */     
/*  235 */     return cache.id;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  240 */     this.caches.clear();
/*  241 */     this.mesh.getVerticesBuffer().clear().flip();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(Texture texture, float[] vertices, int offset, int length) {
/*  248 */     if (this.currentCache == null) throw new IllegalStateException("beginCache must be called before add.");
/*      */     
/*  250 */     int verticesPerImage = (this.mesh.getNumIndices() > 0) ? 4 : 6;
/*  251 */     int count = length / verticesPerImage * 5 * 6;
/*  252 */     int lastIndex = this.textures.size - 1;
/*  253 */     if (lastIndex < 0 || this.textures.get(lastIndex) != texture) {
/*  254 */       this.textures.add(texture);
/*  255 */       this.counts.add(count);
/*      */     } else {
/*  257 */       this.counts.incr(lastIndex, count);
/*      */     } 
/*  259 */     this.mesh.getVerticesBuffer().put(vertices, offset, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void add(Texture texture, float x, float y) {
/*  264 */     float fx2 = x + texture.getWidth();
/*  265 */     float fy2 = y + texture.getHeight();
/*      */     
/*  267 */     tempVertices[0] = x;
/*  268 */     tempVertices[1] = y;
/*  269 */     tempVertices[2] = this.color;
/*  270 */     tempVertices[3] = 0.0F;
/*  271 */     tempVertices[4] = 1.0F;
/*      */     
/*  273 */     tempVertices[5] = x;
/*  274 */     tempVertices[6] = fy2;
/*  275 */     tempVertices[7] = this.color;
/*  276 */     tempVertices[8] = 0.0F;
/*  277 */     tempVertices[9] = 0.0F;
/*      */     
/*  279 */     tempVertices[10] = fx2;
/*  280 */     tempVertices[11] = fy2;
/*  281 */     tempVertices[12] = this.color;
/*  282 */     tempVertices[13] = 1.0F;
/*  283 */     tempVertices[14] = 0.0F;
/*      */     
/*  285 */     if (this.mesh.getNumIndices() > 0) {
/*  286 */       tempVertices[15] = fx2;
/*  287 */       tempVertices[16] = y;
/*  288 */       tempVertices[17] = this.color;
/*  289 */       tempVertices[18] = 1.0F;
/*  290 */       tempVertices[19] = 1.0F;
/*  291 */       add(texture, tempVertices, 0, 20);
/*      */     } else {
/*  293 */       tempVertices[15] = fx2;
/*  294 */       tempVertices[16] = fy2;
/*  295 */       tempVertices[17] = this.color;
/*  296 */       tempVertices[18] = 1.0F;
/*  297 */       tempVertices[19] = 0.0F;
/*      */       
/*  299 */       tempVertices[20] = fx2;
/*  300 */       tempVertices[21] = y;
/*  301 */       tempVertices[22] = this.color;
/*  302 */       tempVertices[23] = 1.0F;
/*  303 */       tempVertices[24] = 1.0F;
/*      */       
/*  305 */       tempVertices[25] = x;
/*  306 */       tempVertices[26] = y;
/*  307 */       tempVertices[27] = this.color;
/*  308 */       tempVertices[28] = 0.0F;
/*  309 */       tempVertices[29] = 1.0F;
/*  310 */       add(texture, tempVertices, 0, 30);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(Texture texture, float x, float y, int srcWidth, int srcHeight, float u, float v, float u2, float v2, float color) {
/*  317 */     float fx2 = x + srcWidth;
/*  318 */     float fy2 = y + srcHeight;
/*      */     
/*  320 */     tempVertices[0] = x;
/*  321 */     tempVertices[1] = y;
/*  322 */     tempVertices[2] = color;
/*  323 */     tempVertices[3] = u;
/*  324 */     tempVertices[4] = v;
/*      */     
/*  326 */     tempVertices[5] = x;
/*  327 */     tempVertices[6] = fy2;
/*  328 */     tempVertices[7] = color;
/*  329 */     tempVertices[8] = u;
/*  330 */     tempVertices[9] = v2;
/*      */     
/*  332 */     tempVertices[10] = fx2;
/*  333 */     tempVertices[11] = fy2;
/*  334 */     tempVertices[12] = color;
/*  335 */     tempVertices[13] = u2;
/*  336 */     tempVertices[14] = v2;
/*      */     
/*  338 */     if (this.mesh.getNumIndices() > 0) {
/*  339 */       tempVertices[15] = fx2;
/*  340 */       tempVertices[16] = y;
/*  341 */       tempVertices[17] = color;
/*  342 */       tempVertices[18] = u2;
/*  343 */       tempVertices[19] = v;
/*  344 */       add(texture, tempVertices, 0, 20);
/*      */     } else {
/*  346 */       tempVertices[15] = fx2;
/*  347 */       tempVertices[16] = fy2;
/*  348 */       tempVertices[17] = color;
/*  349 */       tempVertices[18] = u2;
/*  350 */       tempVertices[19] = v2;
/*      */       
/*  352 */       tempVertices[20] = fx2;
/*  353 */       tempVertices[21] = y;
/*  354 */       tempVertices[22] = color;
/*  355 */       tempVertices[23] = u2;
/*  356 */       tempVertices[24] = v;
/*      */       
/*  358 */       tempVertices[25] = x;
/*  359 */       tempVertices[26] = y;
/*  360 */       tempVertices[27] = color;
/*  361 */       tempVertices[28] = u;
/*  362 */       tempVertices[29] = v;
/*  363 */       add(texture, tempVertices, 0, 30);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
/*  369 */     float invTexWidth = 1.0F / texture.getWidth();
/*  370 */     float invTexHeight = 1.0F / texture.getHeight();
/*  371 */     float u = srcX * invTexWidth;
/*  372 */     float v = (srcY + srcHeight) * invTexHeight;
/*  373 */     float u2 = (srcX + srcWidth) * invTexWidth;
/*  374 */     float v2 = srcY * invTexHeight;
/*  375 */     float fx2 = x + srcWidth;
/*  376 */     float fy2 = y + srcHeight;
/*      */     
/*  378 */     tempVertices[0] = x;
/*  379 */     tempVertices[1] = y;
/*  380 */     tempVertices[2] = this.color;
/*  381 */     tempVertices[3] = u;
/*  382 */     tempVertices[4] = v;
/*      */     
/*  384 */     tempVertices[5] = x;
/*  385 */     tempVertices[6] = fy2;
/*  386 */     tempVertices[7] = this.color;
/*  387 */     tempVertices[8] = u;
/*  388 */     tempVertices[9] = v2;
/*      */     
/*  390 */     tempVertices[10] = fx2;
/*  391 */     tempVertices[11] = fy2;
/*  392 */     tempVertices[12] = this.color;
/*  393 */     tempVertices[13] = u2;
/*  394 */     tempVertices[14] = v2;
/*      */     
/*  396 */     if (this.mesh.getNumIndices() > 0) {
/*  397 */       tempVertices[15] = fx2;
/*  398 */       tempVertices[16] = y;
/*  399 */       tempVertices[17] = this.color;
/*  400 */       tempVertices[18] = u2;
/*  401 */       tempVertices[19] = v;
/*  402 */       add(texture, tempVertices, 0, 20);
/*      */     } else {
/*  404 */       tempVertices[15] = fx2;
/*  405 */       tempVertices[16] = fy2;
/*  406 */       tempVertices[17] = this.color;
/*  407 */       tempVertices[18] = u2;
/*  408 */       tempVertices[19] = v2;
/*      */       
/*  410 */       tempVertices[20] = fx2;
/*  411 */       tempVertices[21] = y;
/*  412 */       tempVertices[22] = this.color;
/*  413 */       tempVertices[23] = u2;
/*  414 */       tempVertices[24] = v;
/*      */       
/*  416 */       tempVertices[25] = x;
/*  417 */       tempVertices[26] = y;
/*  418 */       tempVertices[27] = this.color;
/*  419 */       tempVertices[28] = u;
/*  420 */       tempVertices[29] = v;
/*  421 */       add(texture, tempVertices, 0, 30);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*  429 */     float invTexWidth = 1.0F / texture.getWidth();
/*  430 */     float invTexHeight = 1.0F / texture.getHeight();
/*  431 */     float u = srcX * invTexWidth;
/*  432 */     float v = (srcY + srcHeight) * invTexHeight;
/*  433 */     float u2 = (srcX + srcWidth) * invTexWidth;
/*  434 */     float v2 = srcY * invTexHeight;
/*  435 */     float fx2 = x + width;
/*  436 */     float fy2 = y + height;
/*      */     
/*  438 */     if (flipX) {
/*  439 */       float tmp = u;
/*  440 */       u = u2;
/*  441 */       u2 = tmp;
/*      */     } 
/*  443 */     if (flipY) {
/*  444 */       float tmp = v;
/*  445 */       v = v2;
/*  446 */       v2 = tmp;
/*      */     } 
/*      */     
/*  449 */     tempVertices[0] = x;
/*  450 */     tempVertices[1] = y;
/*  451 */     tempVertices[2] = this.color;
/*  452 */     tempVertices[3] = u;
/*  453 */     tempVertices[4] = v;
/*      */     
/*  455 */     tempVertices[5] = x;
/*  456 */     tempVertices[6] = fy2;
/*  457 */     tempVertices[7] = this.color;
/*  458 */     tempVertices[8] = u;
/*  459 */     tempVertices[9] = v2;
/*      */     
/*  461 */     tempVertices[10] = fx2;
/*  462 */     tempVertices[11] = fy2;
/*  463 */     tempVertices[12] = this.color;
/*  464 */     tempVertices[13] = u2;
/*  465 */     tempVertices[14] = v2;
/*      */     
/*  467 */     if (this.mesh.getNumIndices() > 0) {
/*  468 */       tempVertices[15] = fx2;
/*  469 */       tempVertices[16] = y;
/*  470 */       tempVertices[17] = this.color;
/*  471 */       tempVertices[18] = u2;
/*  472 */       tempVertices[19] = v;
/*  473 */       add(texture, tempVertices, 0, 20);
/*      */     } else {
/*  475 */       tempVertices[15] = fx2;
/*  476 */       tempVertices[16] = fy2;
/*  477 */       tempVertices[17] = this.color;
/*  478 */       tempVertices[18] = u2;
/*  479 */       tempVertices[19] = v2;
/*      */       
/*  481 */       tempVertices[20] = fx2;
/*  482 */       tempVertices[21] = y;
/*  483 */       tempVertices[22] = this.color;
/*  484 */       tempVertices[23] = u2;
/*  485 */       tempVertices[24] = v;
/*      */       
/*  487 */       tempVertices[25] = x;
/*  488 */       tempVertices[26] = y;
/*  489 */       tempVertices[27] = this.color;
/*  490 */       tempVertices[28] = u;
/*  491 */       tempVertices[29] = v;
/*  492 */       add(texture, tempVertices, 0, 30);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
/*  501 */     float x1, y1, x2, y2, x3, y3, x4, y4, worldOriginX = x + originX;
/*  502 */     float worldOriginY = y + originY;
/*  503 */     float fx = -originX;
/*  504 */     float fy = -originY;
/*  505 */     float fx2 = width - originX;
/*  506 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  509 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  510 */       fx *= scaleX;
/*  511 */       fy *= scaleY;
/*  512 */       fx2 *= scaleX;
/*  513 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  517 */     float p1x = fx;
/*  518 */     float p1y = fy;
/*  519 */     float p2x = fx;
/*  520 */     float p2y = fy2;
/*  521 */     float p3x = fx2;
/*  522 */     float p3y = fy2;
/*  523 */     float p4x = fx2;
/*  524 */     float p4y = fy;
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
/*  536 */     if (rotation != 0.0F) {
/*  537 */       float cos = MathUtils.cosDeg(rotation);
/*  538 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  540 */       x1 = cos * p1x - sin * p1y;
/*  541 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  543 */       x2 = cos * p2x - sin * p2y;
/*  544 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  546 */       x3 = cos * p3x - sin * p3y;
/*  547 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  549 */       x4 = x1 + x3 - x2;
/*  550 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  552 */       x1 = p1x;
/*  553 */       y1 = p1y;
/*      */       
/*  555 */       x2 = p2x;
/*  556 */       y2 = p2y;
/*      */       
/*  558 */       x3 = p3x;
/*  559 */       y3 = p3y;
/*      */       
/*  561 */       x4 = p4x;
/*  562 */       y4 = p4y;
/*      */     } 
/*      */     
/*  565 */     x1 += worldOriginX;
/*  566 */     y1 += worldOriginY;
/*  567 */     x2 += worldOriginX;
/*  568 */     y2 += worldOriginY;
/*  569 */     x3 += worldOriginX;
/*  570 */     y3 += worldOriginY;
/*  571 */     x4 += worldOriginX;
/*  572 */     y4 += worldOriginY;
/*      */     
/*  574 */     float invTexWidth = 1.0F / texture.getWidth();
/*  575 */     float invTexHeight = 1.0F / texture.getHeight();
/*  576 */     float u = srcX * invTexWidth;
/*  577 */     float v = (srcY + srcHeight) * invTexHeight;
/*  578 */     float u2 = (srcX + srcWidth) * invTexWidth;
/*  579 */     float v2 = srcY * invTexHeight;
/*      */     
/*  581 */     if (flipX) {
/*  582 */       float tmp = u;
/*  583 */       u = u2;
/*  584 */       u2 = tmp;
/*      */     } 
/*      */     
/*  587 */     if (flipY) {
/*  588 */       float tmp = v;
/*  589 */       v = v2;
/*  590 */       v2 = tmp;
/*      */     } 
/*      */     
/*  593 */     tempVertices[0] = x1;
/*  594 */     tempVertices[1] = y1;
/*  595 */     tempVertices[2] = this.color;
/*  596 */     tempVertices[3] = u;
/*  597 */     tempVertices[4] = v;
/*      */     
/*  599 */     tempVertices[5] = x2;
/*  600 */     tempVertices[6] = y2;
/*  601 */     tempVertices[7] = this.color;
/*  602 */     tempVertices[8] = u;
/*  603 */     tempVertices[9] = v2;
/*      */     
/*  605 */     tempVertices[10] = x3;
/*  606 */     tempVertices[11] = y3;
/*  607 */     tempVertices[12] = this.color;
/*  608 */     tempVertices[13] = u2;
/*  609 */     tempVertices[14] = v2;
/*      */     
/*  611 */     if (this.mesh.getNumIndices() > 0) {
/*  612 */       tempVertices[15] = x4;
/*  613 */       tempVertices[16] = y4;
/*  614 */       tempVertices[17] = this.color;
/*  615 */       tempVertices[18] = u2;
/*  616 */       tempVertices[19] = v;
/*  617 */       add(texture, tempVertices, 0, 20);
/*      */     } else {
/*  619 */       tempVertices[15] = x3;
/*  620 */       tempVertices[16] = y3;
/*  621 */       tempVertices[17] = this.color;
/*  622 */       tempVertices[18] = u2;
/*  623 */       tempVertices[19] = v2;
/*      */       
/*  625 */       tempVertices[20] = x4;
/*  626 */       tempVertices[21] = y4;
/*  627 */       tempVertices[22] = this.color;
/*  628 */       tempVertices[23] = u2;
/*  629 */       tempVertices[24] = v;
/*      */       
/*  631 */       tempVertices[25] = x1;
/*  632 */       tempVertices[26] = y1;
/*  633 */       tempVertices[27] = this.color;
/*  634 */       tempVertices[28] = u;
/*  635 */       tempVertices[29] = v;
/*  636 */       add(texture, tempVertices, 0, 30);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void add(TextureRegion region, float x, float y) {
/*  642 */     add(region, x, y, region.getRegionWidth(), region.getRegionHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void add(TextureRegion region, float x, float y, float width, float height) {
/*  647 */     float fx2 = x + width;
/*  648 */     float fy2 = y + height;
/*  649 */     float u = region.u;
/*  650 */     float v = region.v2;
/*  651 */     float u2 = region.u2;
/*  652 */     float v2 = region.v;
/*      */     
/*  654 */     tempVertices[0] = x;
/*  655 */     tempVertices[1] = y;
/*  656 */     tempVertices[2] = this.color;
/*  657 */     tempVertices[3] = u;
/*  658 */     tempVertices[4] = v;
/*      */     
/*  660 */     tempVertices[5] = x;
/*  661 */     tempVertices[6] = fy2;
/*  662 */     tempVertices[7] = this.color;
/*  663 */     tempVertices[8] = u;
/*  664 */     tempVertices[9] = v2;
/*      */     
/*  666 */     tempVertices[10] = fx2;
/*  667 */     tempVertices[11] = fy2;
/*  668 */     tempVertices[12] = this.color;
/*  669 */     tempVertices[13] = u2;
/*  670 */     tempVertices[14] = v2;
/*      */     
/*  672 */     if (this.mesh.getNumIndices() > 0) {
/*  673 */       tempVertices[15] = fx2;
/*  674 */       tempVertices[16] = y;
/*  675 */       tempVertices[17] = this.color;
/*  676 */       tempVertices[18] = u2;
/*  677 */       tempVertices[19] = v;
/*  678 */       add(region.texture, tempVertices, 0, 20);
/*      */     } else {
/*  680 */       tempVertices[15] = fx2;
/*  681 */       tempVertices[16] = fy2;
/*  682 */       tempVertices[17] = this.color;
/*  683 */       tempVertices[18] = u2;
/*  684 */       tempVertices[19] = v2;
/*      */       
/*  686 */       tempVertices[20] = fx2;
/*  687 */       tempVertices[21] = y;
/*  688 */       tempVertices[22] = this.color;
/*  689 */       tempVertices[23] = u2;
/*  690 */       tempVertices[24] = v;
/*      */       
/*  692 */       tempVertices[25] = x;
/*  693 */       tempVertices[26] = y;
/*  694 */       tempVertices[27] = this.color;
/*  695 */       tempVertices[28] = u;
/*  696 */       tempVertices[29] = v;
/*  697 */       add(region.texture, tempVertices, 0, 30);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*  706 */     float x1, y1, x2, y2, x3, y3, x4, y4, worldOriginX = x + originX;
/*  707 */     float worldOriginY = y + originY;
/*  708 */     float fx = -originX;
/*  709 */     float fy = -originY;
/*  710 */     float fx2 = width - originX;
/*  711 */     float fy2 = height - originY;
/*      */ 
/*      */     
/*  714 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  715 */       fx *= scaleX;
/*  716 */       fy *= scaleY;
/*  717 */       fx2 *= scaleX;
/*  718 */       fy2 *= scaleY;
/*      */     } 
/*      */ 
/*      */     
/*  722 */     float p1x = fx;
/*  723 */     float p1y = fy;
/*  724 */     float p2x = fx;
/*  725 */     float p2y = fy2;
/*  726 */     float p3x = fx2;
/*  727 */     float p3y = fy2;
/*  728 */     float p4x = fx2;
/*  729 */     float p4y = fy;
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
/*  741 */     if (rotation != 0.0F) {
/*  742 */       float cos = MathUtils.cosDeg(rotation);
/*  743 */       float sin = MathUtils.sinDeg(rotation);
/*      */       
/*  745 */       x1 = cos * p1x - sin * p1y;
/*  746 */       y1 = sin * p1x + cos * p1y;
/*      */       
/*  748 */       x2 = cos * p2x - sin * p2y;
/*  749 */       y2 = sin * p2x + cos * p2y;
/*      */       
/*  751 */       x3 = cos * p3x - sin * p3y;
/*  752 */       y3 = sin * p3x + cos * p3y;
/*      */       
/*  754 */       x4 = x1 + x3 - x2;
/*  755 */       y4 = y3 - y2 - y1;
/*      */     } else {
/*  757 */       x1 = p1x;
/*  758 */       y1 = p1y;
/*      */       
/*  760 */       x2 = p2x;
/*  761 */       y2 = p2y;
/*      */       
/*  763 */       x3 = p3x;
/*  764 */       y3 = p3y;
/*      */       
/*  766 */       x4 = p4x;
/*  767 */       y4 = p4y;
/*      */     } 
/*      */     
/*  770 */     x1 += worldOriginX;
/*  771 */     y1 += worldOriginY;
/*  772 */     x2 += worldOriginX;
/*  773 */     y2 += worldOriginY;
/*  774 */     x3 += worldOriginX;
/*  775 */     y3 += worldOriginY;
/*  776 */     x4 += worldOriginX;
/*  777 */     y4 += worldOriginY;
/*      */     
/*  779 */     float u = region.u;
/*  780 */     float v = region.v2;
/*  781 */     float u2 = region.u2;
/*  782 */     float v2 = region.v;
/*      */     
/*  784 */     tempVertices[0] = x1;
/*  785 */     tempVertices[1] = y1;
/*  786 */     tempVertices[2] = this.color;
/*  787 */     tempVertices[3] = u;
/*  788 */     tempVertices[4] = v;
/*      */     
/*  790 */     tempVertices[5] = x2;
/*  791 */     tempVertices[6] = y2;
/*  792 */     tempVertices[7] = this.color;
/*  793 */     tempVertices[8] = u;
/*  794 */     tempVertices[9] = v2;
/*      */     
/*  796 */     tempVertices[10] = x3;
/*  797 */     tempVertices[11] = y3;
/*  798 */     tempVertices[12] = this.color;
/*  799 */     tempVertices[13] = u2;
/*  800 */     tempVertices[14] = v2;
/*      */     
/*  802 */     if (this.mesh.getNumIndices() > 0) {
/*  803 */       tempVertices[15] = x4;
/*  804 */       tempVertices[16] = y4;
/*  805 */       tempVertices[17] = this.color;
/*  806 */       tempVertices[18] = u2;
/*  807 */       tempVertices[19] = v;
/*  808 */       add(region.texture, tempVertices, 0, 20);
/*      */     } else {
/*  810 */       tempVertices[15] = x3;
/*  811 */       tempVertices[16] = y3;
/*  812 */       tempVertices[17] = this.color;
/*  813 */       tempVertices[18] = u2;
/*  814 */       tempVertices[19] = v2;
/*      */       
/*  816 */       tempVertices[20] = x4;
/*  817 */       tempVertices[21] = y4;
/*  818 */       tempVertices[22] = this.color;
/*  819 */       tempVertices[23] = u2;
/*  820 */       tempVertices[24] = v;
/*      */       
/*  822 */       tempVertices[25] = x1;
/*  823 */       tempVertices[26] = y1;
/*  824 */       tempVertices[27] = this.color;
/*  825 */       tempVertices[28] = u;
/*  826 */       tempVertices[29] = v;
/*  827 */       add(region.texture, tempVertices, 0, 30);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void add(Sprite sprite) {
/*  833 */     if (this.mesh.getNumIndices() > 0) {
/*  834 */       add(sprite.getTexture(), sprite.getVertices(), 0, 20);
/*      */       
/*      */       return;
/*      */     } 
/*  838 */     float[] spriteVertices = sprite.getVertices();
/*  839 */     System.arraycopy(spriteVertices, 0, tempVertices, 0, 15);
/*  840 */     System.arraycopy(spriteVertices, 10, tempVertices, 15, 5);
/*  841 */     System.arraycopy(spriteVertices, 15, tempVertices, 20, 5);
/*  842 */     System.arraycopy(spriteVertices, 0, tempVertices, 25, 5);
/*  843 */     add(sprite.getTexture(), tempVertices, 0, 30);
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin() {
/*  848 */     if (this.drawing) throw new IllegalStateException("end must be called before begin."); 
/*  849 */     this.renderCalls = 0;
/*  850 */     this.combinedMatrix.set(this.projectionMatrix).mul(this.transformMatrix);
/*      */     
/*  852 */     Gdx.gl20.glDepthMask(false);
/*      */     
/*  854 */     if (this.customShader != null) {
/*  855 */       this.customShader.begin();
/*  856 */       this.customShader.setUniformMatrix("u_proj", this.projectionMatrix);
/*  857 */       this.customShader.setUniformMatrix("u_trans", this.transformMatrix);
/*  858 */       this.customShader.setUniformMatrix("u_projTrans", this.combinedMatrix);
/*  859 */       this.customShader.setUniformi("u_texture", 0);
/*  860 */       this.mesh.bind(this.customShader);
/*      */     } else {
/*  862 */       this.shader.begin();
/*  863 */       this.shader.setUniformMatrix("u_projectionViewMatrix", this.combinedMatrix);
/*  864 */       this.shader.setUniformi("u_texture", 0);
/*  865 */       this.mesh.bind(this.shader);
/*      */     } 
/*  867 */     this.drawing = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void end() {
/*  872 */     if (!this.drawing) throw new IllegalStateException("begin must be called before end."); 
/*  873 */     this.drawing = false;
/*      */     
/*  875 */     this.shader.end();
/*  876 */     GL20 gl = Gdx.gl20;
/*  877 */     gl.glDepthMask(true);
/*  878 */     if (this.customShader != null) {
/*  879 */       this.mesh.unbind(this.customShader);
/*      */     } else {
/*  881 */       this.mesh.unbind(this.shader);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void draw(int cacheID) {
/*  886 */     if (!this.drawing) throw new IllegalStateException("SpriteCache.begin must be called before draw.");
/*      */     
/*  888 */     Cache cache = (Cache)this.caches.get(cacheID);
/*  889 */     int verticesPerImage = (this.mesh.getNumIndices() > 0) ? 4 : 6;
/*  890 */     int offset = cache.offset / verticesPerImage * 5 * 6;
/*  891 */     Texture[] textures = cache.textures;
/*  892 */     int[] counts = cache.counts;
/*  893 */     int textureCount = cache.textureCount;
/*  894 */     for (int i = 0; i < textureCount; i++) {
/*  895 */       int count = counts[i];
/*  896 */       textures[i].bind();
/*  897 */       if (this.customShader != null) {
/*  898 */         this.mesh.render(this.customShader, 4, offset, count);
/*      */       } else {
/*  900 */         this.mesh.render(this.shader, 4, offset, count);
/*  901 */       }  offset += count;
/*      */     } 
/*  903 */     this.renderCalls += textureCount;
/*  904 */     this.totalRenderCalls += textureCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(int cacheID, int offset, int length) {
/*  911 */     if (!this.drawing) throw new IllegalStateException("SpriteCache.begin must be called before draw.");
/*      */     
/*  913 */     Cache cache = (Cache)this.caches.get(cacheID);
/*  914 */     offset = offset * 6 + cache.offset;
/*  915 */     length *= 6;
/*  916 */     Texture[] textures = cache.textures;
/*  917 */     int[] counts = cache.counts;
/*  918 */     int textureCount = cache.textureCount;
/*  919 */     for (int i = 0; i < textureCount; i++) {
/*  920 */       textures[i].bind();
/*  921 */       int count = counts[i];
/*  922 */       if (count > length) {
/*  923 */         i = textureCount;
/*  924 */         count = length;
/*      */       } else {
/*  926 */         length -= count;
/*  927 */       }  if (this.customShader != null) {
/*  928 */         this.mesh.render(this.customShader, 4, offset, count);
/*      */       } else {
/*  930 */         this.mesh.render(this.shader, 4, offset, count);
/*  931 */       }  offset += count;
/*      */     } 
/*  933 */     this.renderCalls += cache.textureCount;
/*  934 */     this.totalRenderCalls += textureCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dispose() {
/*  939 */     this.mesh.dispose();
/*  940 */     if (this.shader != null) this.shader.dispose(); 
/*      */   }
/*      */   
/*      */   public Matrix4 getProjectionMatrix() {
/*  944 */     return this.projectionMatrix;
/*      */   }
/*      */   
/*      */   public void setProjectionMatrix(Matrix4 projection) {
/*  948 */     if (this.drawing) throw new IllegalStateException("Can't set the matrix within begin/end."); 
/*  949 */     this.projectionMatrix.set(projection);
/*      */   }
/*      */   
/*      */   public Matrix4 getTransformMatrix() {
/*  953 */     return this.transformMatrix;
/*      */   }
/*      */   
/*      */   public void setTransformMatrix(Matrix4 transform) {
/*  957 */     if (this.drawing) throw new IllegalStateException("Can't set the matrix within begin/end."); 
/*  958 */     this.transformMatrix.set(transform);
/*      */   }
/*      */   
/*      */   private static class Cache {
/*      */     final int id;
/*      */     final int offset;
/*      */     int maxCount;
/*      */     int textureCount;
/*      */     Texture[] textures;
/*      */     int[] counts;
/*      */     
/*      */     public Cache(int id, int offset) {
/*  970 */       this.id = id;
/*  971 */       this.offset = offset;
/*      */     }
/*      */   }
/*      */   
/*      */   static ShaderProgram createDefaultShader() {
/*  976 */     String vertexShader = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projectionViewMatrix;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_color.a = v_color.a * (255.0/254.0);\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projectionViewMatrix * a_position;\n}\n";
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
/*  990 */     String fragmentShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1000 */     ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
/* 1001 */     if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog()); 
/* 1002 */     return shader;
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
/*      */   public void setShader(ShaderProgram shader) {
/* 1015 */     this.customShader = shader;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\SpriteCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */