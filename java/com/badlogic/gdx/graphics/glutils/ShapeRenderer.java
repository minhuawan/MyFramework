/*      */ package com.badlogic.gdx.graphics.glutils;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Matrix4;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.badlogic.gdx.math.Vector3;
/*      */ import com.badlogic.gdx.utils.Disposable;
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
/*      */ public class ShapeRenderer
/*      */   implements Disposable
/*      */ {
/*      */   private final ImmediateModeRenderer renderer;
/*      */   
/*      */   public enum ShapeType
/*      */   {
/*   82 */     Point(0), Line(1), Filled(4);
/*      */     
/*      */     private final int glType;
/*      */     
/*      */     ShapeType(int glType) {
/*   87 */       this.glType = glType;
/*      */     }
/*      */     
/*      */     public int getGlType() {
/*   91 */       return this.glType;
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean matrixDirty = false;
/*      */   
/*   97 */   private final Matrix4 projectionMatrix = new Matrix4();
/*   98 */   private final Matrix4 transformMatrix = new Matrix4();
/*   99 */   private final Matrix4 combinedMatrix = new Matrix4();
/*  100 */   private final Vector2 tmp = new Vector2();
/*  101 */   private final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   private ShapeType shapeType;
/*      */   private boolean autoShapeType;
/*  104 */   private float defaultRectLineWidth = 0.75F;
/*      */   
/*      */   public ShapeRenderer() {
/*  107 */     this(5000);
/*      */   }
/*      */   
/*      */   public ShapeRenderer(int maxVertices) {
/*  111 */     this(maxVertices, null);
/*      */   }
/*      */   
/*      */   public ShapeRenderer(int maxVertices, ShaderProgram defaultShader) {
/*  115 */     if (defaultShader == null) {
/*  116 */       this.renderer = new ImmediateModeRenderer20(maxVertices, false, true, 0);
/*      */     } else {
/*  118 */       this.renderer = new ImmediateModeRenderer20(maxVertices, false, true, 0, defaultShader);
/*      */     } 
/*  120 */     this.projectionMatrix.setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*  121 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(Color color) {
/*  126 */     this.color.set(color);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  131 */     this.color.set(r, g, b, a);
/*      */   }
/*      */   
/*      */   public Color getColor() {
/*  135 */     return this.color;
/*      */   }
/*      */   
/*      */   public void updateMatrices() {
/*  139 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProjectionMatrix(Matrix4 matrix) {
/*  145 */     this.projectionMatrix.set(matrix);
/*  146 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getProjectionMatrix() {
/*  151 */     return this.projectionMatrix;
/*      */   }
/*      */   
/*      */   public void setTransformMatrix(Matrix4 matrix) {
/*  155 */     this.transformMatrix.set(matrix);
/*  156 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Matrix4 getTransformMatrix() {
/*  161 */     return this.transformMatrix;
/*      */   }
/*      */ 
/*      */   
/*      */   public void identity() {
/*  166 */     this.transformMatrix.idt();
/*  167 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void translate(float x, float y, float z) {
/*  172 */     this.transformMatrix.translate(x, y, z);
/*  173 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void rotate(float axisX, float axisY, float axisZ, float degrees) {
/*  178 */     this.transformMatrix.rotate(axisX, axisY, axisZ, degrees);
/*  179 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void scale(float scaleX, float scaleY, float scaleZ) {
/*  184 */     this.transformMatrix.scale(scaleX, scaleY, scaleZ);
/*  185 */     this.matrixDirty = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoShapeType(boolean autoShapeType) {
/*  192 */     this.autoShapeType = autoShapeType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin() {
/*  198 */     if (!this.autoShapeType) throw new IllegalStateException("autoShapeType must be true to use this method."); 
/*  199 */     begin(ShapeType.Line);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin(ShapeType type) {
/*  206 */     if (this.shapeType != null) throw new IllegalStateException("Call end() before beginning a new shape batch."); 
/*  207 */     this.shapeType = type;
/*  208 */     if (this.matrixDirty) {
/*  209 */       this.combinedMatrix.set(this.projectionMatrix);
/*  210 */       Matrix4.mul(this.combinedMatrix.val, this.transformMatrix.val);
/*  211 */       this.matrixDirty = false;
/*      */     } 
/*  213 */     this.renderer.begin(this.combinedMatrix, this.shapeType.getGlType());
/*      */   }
/*      */   
/*      */   public void set(ShapeType type) {
/*  217 */     if (this.shapeType == type)
/*  218 */       return;  if (this.shapeType == null) throw new IllegalStateException("begin must be called first."); 
/*  219 */     if (!this.autoShapeType) throw new IllegalStateException("autoShapeType must be enabled."); 
/*  220 */     end();
/*  221 */     begin(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public void point(float x, float y, float z) {
/*  226 */     if (this.shapeType == ShapeType.Line) {
/*  227 */       float size = this.defaultRectLineWidth * 0.5F;
/*  228 */       line(x - size, y - size, z, x + size, y + size, z); return;
/*      */     } 
/*  230 */     if (this.shapeType == ShapeType.Filled) {
/*  231 */       float size = this.defaultRectLineWidth * 0.5F;
/*  232 */       box(x - size, y - size, z - size, this.defaultRectLineWidth, this.defaultRectLineWidth, this.defaultRectLineWidth);
/*      */       return;
/*      */     } 
/*  235 */     check(ShapeType.Point, null, 1);
/*  236 */     this.renderer.color(this.color);
/*  237 */     this.renderer.vertex(x, y, z);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(float x, float y, float z, float x2, float y2, float z2) {
/*  242 */     line(x, y, z, x2, y2, z2, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(Vector3 v0, Vector3 v1) {
/*  247 */     line(v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(float x, float y, float x2, float y2) {
/*  252 */     line(x, y, 0.0F, x2, y2, 0.0F, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(Vector2 v0, Vector2 v1) {
/*  257 */     line(v0.x, v0.y, 0.0F, v1.x, v1.y, 0.0F, this.color, this.color);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void line(float x, float y, float x2, float y2, Color c1, Color c2) {
/*  262 */     line(x, y, 0.0F, x2, y2, 0.0F, c1, c2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void line(float x, float y, float z, float x2, float y2, float z2, Color c1, Color c2) {
/*  268 */     if (this.shapeType == ShapeType.Filled) {
/*  269 */       rectLine(x, y, x2, y2, this.defaultRectLineWidth, c1, c2);
/*      */       return;
/*      */     } 
/*  272 */     check(ShapeType.Line, null, 2);
/*  273 */     this.renderer.color(c1.r, c1.g, c1.b, c1.a);
/*  274 */     this.renderer.vertex(x, y, z);
/*  275 */     this.renderer.color(c2.r, c2.g, c2.b, c2.a);
/*  276 */     this.renderer.vertex(x2, y2, z2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void curve(float x1, float y1, float cx1, float cy1, float cx2, float cy2, float x2, float y2, int segments) {
/*  281 */     check(ShapeType.Line, null, segments * 2 + 2);
/*  282 */     float colorBits = this.color.toFloatBits();
/*      */ 
/*      */     
/*  285 */     float subdiv_step = 1.0F / segments;
/*  286 */     float subdiv_step2 = subdiv_step * subdiv_step;
/*  287 */     float subdiv_step3 = subdiv_step * subdiv_step * subdiv_step;
/*      */     
/*  289 */     float pre1 = 3.0F * subdiv_step;
/*  290 */     float pre2 = 3.0F * subdiv_step2;
/*  291 */     float pre4 = 6.0F * subdiv_step2;
/*  292 */     float pre5 = 6.0F * subdiv_step3;
/*      */     
/*  294 */     float tmp1x = x1 - cx1 * 2.0F + cx2;
/*  295 */     float tmp1y = y1 - cy1 * 2.0F + cy2;
/*      */     
/*  297 */     float tmp2x = (cx1 - cx2) * 3.0F - x1 + x2;
/*  298 */     float tmp2y = (cy1 - cy2) * 3.0F - y1 + y2;
/*      */     
/*  300 */     float fx = x1;
/*  301 */     float fy = y1;
/*      */     
/*  303 */     float dfx = (cx1 - x1) * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
/*  304 */     float dfy = (cy1 - y1) * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;
/*      */     
/*  306 */     float ddfx = tmp1x * pre4 + tmp2x * pre5;
/*  307 */     float ddfy = tmp1y * pre4 + tmp2y * pre5;
/*      */     
/*  309 */     float dddfx = tmp2x * pre5;
/*  310 */     float dddfy = tmp2y * pre5;
/*      */     
/*  312 */     while (segments-- > 0) {
/*  313 */       this.renderer.color(colorBits);
/*  314 */       this.renderer.vertex(fx, fy, 0.0F);
/*  315 */       fx += dfx;
/*  316 */       fy += dfy;
/*  317 */       dfx += ddfx;
/*  318 */       dfy += ddfy;
/*  319 */       ddfx += dddfx;
/*  320 */       ddfy += dddfy;
/*  321 */       this.renderer.color(colorBits);
/*  322 */       this.renderer.vertex(fx, fy, 0.0F);
/*      */     } 
/*  324 */     this.renderer.color(colorBits);
/*  325 */     this.renderer.vertex(fx, fy, 0.0F);
/*  326 */     this.renderer.color(colorBits);
/*  327 */     this.renderer.vertex(x2, y2, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
/*  332 */     check(ShapeType.Line, ShapeType.Filled, 6);
/*  333 */     float colorBits = this.color.toFloatBits();
/*  334 */     if (this.shapeType == ShapeType.Line) {
/*  335 */       this.renderer.color(colorBits);
/*  336 */       this.renderer.vertex(x1, y1, 0.0F);
/*  337 */       this.renderer.color(colorBits);
/*  338 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */       
/*  340 */       this.renderer.color(colorBits);
/*  341 */       this.renderer.vertex(x2, y2, 0.0F);
/*  342 */       this.renderer.color(colorBits);
/*  343 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  345 */       this.renderer.color(colorBits);
/*  346 */       this.renderer.vertex(x3, y3, 0.0F);
/*  347 */       this.renderer.color(colorBits);
/*  348 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } else {
/*  350 */       this.renderer.color(colorBits);
/*  351 */       this.renderer.vertex(x1, y1, 0.0F);
/*  352 */       this.renderer.color(colorBits);
/*  353 */       this.renderer.vertex(x2, y2, 0.0F);
/*  354 */       this.renderer.color(colorBits);
/*  355 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void triangle(float x1, float y1, float x2, float y2, float x3, float y3, Color col1, Color col2, Color col3) {
/*  361 */     check(ShapeType.Line, ShapeType.Filled, 6);
/*  362 */     if (this.shapeType == ShapeType.Line) {
/*  363 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  364 */       this.renderer.vertex(x1, y1, 0.0F);
/*  365 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  366 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */       
/*  368 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  369 */       this.renderer.vertex(x2, y2, 0.0F);
/*  370 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  371 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  373 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  374 */       this.renderer.vertex(x3, y3, 0.0F);
/*  375 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  376 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } else {
/*  378 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  379 */       this.renderer.vertex(x1, y1, 0.0F);
/*  380 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  381 */       this.renderer.vertex(x2, y2, 0.0F);
/*  382 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  383 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float width, float height) {
/*  389 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*  390 */     float colorBits = this.color.toFloatBits();
/*  391 */     if (this.shapeType == ShapeType.Line) {
/*  392 */       this.renderer.color(colorBits);
/*  393 */       this.renderer.vertex(x, y, 0.0F);
/*  394 */       this.renderer.color(colorBits);
/*  395 */       this.renderer.vertex(x + width, y, 0.0F);
/*      */       
/*  397 */       this.renderer.color(colorBits);
/*  398 */       this.renderer.vertex(x + width, y, 0.0F);
/*  399 */       this.renderer.color(colorBits);
/*  400 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  402 */       this.renderer.color(colorBits);
/*  403 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  404 */       this.renderer.color(colorBits);
/*  405 */       this.renderer.vertex(x, y + height, 0.0F);
/*      */       
/*  407 */       this.renderer.color(colorBits);
/*  408 */       this.renderer.vertex(x, y + height, 0.0F);
/*  409 */       this.renderer.color(colorBits);
/*  410 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } else {
/*  412 */       this.renderer.color(colorBits);
/*  413 */       this.renderer.vertex(x, y, 0.0F);
/*  414 */       this.renderer.color(colorBits);
/*  415 */       this.renderer.vertex(x + width, y, 0.0F);
/*  416 */       this.renderer.color(colorBits);
/*  417 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  419 */       this.renderer.color(colorBits);
/*  420 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  421 */       this.renderer.color(colorBits);
/*  422 */       this.renderer.vertex(x, y + height, 0.0F);
/*  423 */       this.renderer.color(colorBits);
/*  424 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float width, float height, Color col1, Color col2, Color col3, Color col4) {
/*  435 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*      */     
/*  437 */     if (this.shapeType == ShapeType.Line) {
/*  438 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  439 */       this.renderer.vertex(x, y, 0.0F);
/*  440 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  441 */       this.renderer.vertex(x + width, y, 0.0F);
/*      */       
/*  443 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  444 */       this.renderer.vertex(x + width, y, 0.0F);
/*  445 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  446 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  448 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  449 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  450 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  451 */       this.renderer.vertex(x, y + height, 0.0F);
/*      */       
/*  453 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  454 */       this.renderer.vertex(x, y + height, 0.0F);
/*  455 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  456 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } else {
/*  458 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  459 */       this.renderer.vertex(x, y, 0.0F);
/*  460 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  461 */       this.renderer.vertex(x + width, y, 0.0F);
/*  462 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  463 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*      */       
/*  465 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  466 */       this.renderer.vertex(x + width, y + height, 0.0F);
/*  467 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  468 */       this.renderer.vertex(x, y + height, 0.0F);
/*  469 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  470 */       this.renderer.vertex(x, y, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float degrees) {
/*  478 */     rect(x, y, originX, originY, width, height, scaleX, scaleY, degrees, this.color, this.color, this.color, this.color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rect(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float degrees, Color col1, Color col2, Color col3, Color col4) {
/*  489 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*      */     
/*  491 */     float cos = MathUtils.cosDeg(degrees);
/*  492 */     float sin = MathUtils.sinDeg(degrees);
/*  493 */     float fx = -originX;
/*  494 */     float fy = -originY;
/*  495 */     float fx2 = width - originX;
/*  496 */     float fy2 = height - originY;
/*      */     
/*  498 */     if (scaleX != 1.0F || scaleY != 1.0F) {
/*  499 */       fx *= scaleX;
/*  500 */       fy *= scaleY;
/*  501 */       fx2 *= scaleX;
/*  502 */       fy2 *= scaleY;
/*      */     } 
/*      */     
/*  505 */     float worldOriginX = x + originX;
/*  506 */     float worldOriginY = y + originY;
/*      */     
/*  508 */     float x1 = cos * fx - sin * fy + worldOriginX;
/*  509 */     float y1 = sin * fx + cos * fy + worldOriginY;
/*      */     
/*  511 */     float x2 = cos * fx2 - sin * fy + worldOriginX;
/*  512 */     float y2 = sin * fx2 + cos * fy + worldOriginY;
/*      */     
/*  514 */     float x3 = cos * fx2 - sin * fy2 + worldOriginX;
/*  515 */     float y3 = sin * fx2 + cos * fy2 + worldOriginY;
/*      */     
/*  517 */     float x4 = x1 + x3 - x2;
/*  518 */     float y4 = y3 - y2 - y1;
/*      */     
/*  520 */     if (this.shapeType == ShapeType.Line) {
/*  521 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  522 */       this.renderer.vertex(x1, y1, 0.0F);
/*  523 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  524 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */       
/*  526 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  527 */       this.renderer.vertex(x2, y2, 0.0F);
/*  528 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  529 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  531 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  532 */       this.renderer.vertex(x3, y3, 0.0F);
/*  533 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  534 */       this.renderer.vertex(x4, y4, 0.0F);
/*      */       
/*  536 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  537 */       this.renderer.vertex(x4, y4, 0.0F);
/*  538 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  539 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } else {
/*  541 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  542 */       this.renderer.vertex(x1, y1, 0.0F);
/*  543 */       this.renderer.color(col2.r, col2.g, col2.b, col2.a);
/*  544 */       this.renderer.vertex(x2, y2, 0.0F);
/*  545 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  546 */       this.renderer.vertex(x3, y3, 0.0F);
/*      */       
/*  548 */       this.renderer.color(col3.r, col3.g, col3.b, col3.a);
/*  549 */       this.renderer.vertex(x3, y3, 0.0F);
/*  550 */       this.renderer.color(col4.r, col4.g, col4.b, col4.a);
/*  551 */       this.renderer.vertex(x4, y4, 0.0F);
/*  552 */       this.renderer.color(col1.r, col1.g, col1.b, col1.a);
/*  553 */       this.renderer.vertex(x1, y1, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void rectLine(float x1, float y1, float x2, float y2, float width) {
/*  560 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*  561 */     float colorBits = this.color.toFloatBits();
/*  562 */     Vector2 t = this.tmp.set(y2 - y1, x1 - x2).nor();
/*  563 */     width *= 0.5F;
/*  564 */     float tx = t.x * width;
/*  565 */     float ty = t.y * width;
/*  566 */     if (this.shapeType == ShapeType.Line) {
/*  567 */       this.renderer.color(colorBits);
/*  568 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*  569 */       this.renderer.color(colorBits);
/*  570 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */       
/*  572 */       this.renderer.color(colorBits);
/*  573 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  574 */       this.renderer.color(colorBits);
/*  575 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*      */       
/*  577 */       this.renderer.color(colorBits);
/*  578 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  579 */       this.renderer.color(colorBits);
/*  580 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*      */       
/*  582 */       this.renderer.color(colorBits);
/*  583 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*  584 */       this.renderer.color(colorBits);
/*  585 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */     } else {
/*  587 */       this.renderer.color(colorBits);
/*  588 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*  589 */       this.renderer.color(colorBits);
/*  590 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*  591 */       this.renderer.color(colorBits);
/*  592 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*      */       
/*  594 */       this.renderer.color(colorBits);
/*  595 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*  596 */       this.renderer.color(colorBits);
/*  597 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  598 */       this.renderer.color(colorBits);
/*  599 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void rectLine(float x1, float y1, float x2, float y2, float width, Color c1, Color c2) {
/*  605 */     check(ShapeType.Line, ShapeType.Filled, 8);
/*  606 */     float col1Bits = c1.toFloatBits();
/*  607 */     float col2Bits = c2.toFloatBits();
/*  608 */     Vector2 t = this.tmp.set(y2 - y1, x1 - x2).nor();
/*  609 */     width *= 0.5F;
/*  610 */     float tx = t.x * width;
/*  611 */     float ty = t.y * width;
/*  612 */     if (this.shapeType == ShapeType.Line) {
/*  613 */       this.renderer.color(col1Bits);
/*  614 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*  615 */       this.renderer.color(col1Bits);
/*  616 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */       
/*  618 */       this.renderer.color(col2Bits);
/*  619 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  620 */       this.renderer.color(col2Bits);
/*  621 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*      */       
/*  623 */       this.renderer.color(col2Bits);
/*  624 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  625 */       this.renderer.color(col1Bits);
/*  626 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*      */       
/*  628 */       this.renderer.color(col2Bits);
/*  629 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*  630 */       this.renderer.color(col1Bits);
/*  631 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */     } else {
/*  633 */       this.renderer.color(col1Bits);
/*  634 */       this.renderer.vertex(x1 + tx, y1 + ty, 0.0F);
/*  635 */       this.renderer.color(col1Bits);
/*  636 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*  637 */       this.renderer.color(col2Bits);
/*  638 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*      */       
/*  640 */       this.renderer.color(col2Bits);
/*  641 */       this.renderer.vertex(x2 - tx, y2 - ty, 0.0F);
/*  642 */       this.renderer.color(col2Bits);
/*  643 */       this.renderer.vertex(x2 + tx, y2 + ty, 0.0F);
/*  644 */       this.renderer.color(col1Bits);
/*  645 */       this.renderer.vertex(x1 - tx, y1 - ty, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void rectLine(Vector2 p1, Vector2 p2, float width) {
/*  651 */     rectLine(p1.x, p1.y, p2.x, p2.y, width);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void box(float x, float y, float z, float width, float height, float depth) {
/*  657 */     depth = -depth;
/*  658 */     float colorBits = this.color.toFloatBits();
/*  659 */     if (this.shapeType == ShapeType.Line) {
/*  660 */       check(ShapeType.Line, ShapeType.Filled, 24);
/*      */       
/*  662 */       this.renderer.color(colorBits);
/*  663 */       this.renderer.vertex(x, y, z);
/*  664 */       this.renderer.color(colorBits);
/*  665 */       this.renderer.vertex(x + width, y, z);
/*      */       
/*  667 */       this.renderer.color(colorBits);
/*  668 */       this.renderer.vertex(x + width, y, z);
/*  669 */       this.renderer.color(colorBits);
/*  670 */       this.renderer.vertex(x + width, y, z + depth);
/*      */       
/*  672 */       this.renderer.color(colorBits);
/*  673 */       this.renderer.vertex(x + width, y, z + depth);
/*  674 */       this.renderer.color(colorBits);
/*  675 */       this.renderer.vertex(x, y, z + depth);
/*      */       
/*  677 */       this.renderer.color(colorBits);
/*  678 */       this.renderer.vertex(x, y, z + depth);
/*  679 */       this.renderer.color(colorBits);
/*  680 */       this.renderer.vertex(x, y, z);
/*      */       
/*  682 */       this.renderer.color(colorBits);
/*  683 */       this.renderer.vertex(x, y, z);
/*  684 */       this.renderer.color(colorBits);
/*  685 */       this.renderer.vertex(x, y + height, z);
/*      */       
/*  687 */       this.renderer.color(colorBits);
/*  688 */       this.renderer.vertex(x, y + height, z);
/*  689 */       this.renderer.color(colorBits);
/*  690 */       this.renderer.vertex(x + width, y + height, z);
/*      */       
/*  692 */       this.renderer.color(colorBits);
/*  693 */       this.renderer.vertex(x + width, y + height, z);
/*  694 */       this.renderer.color(colorBits);
/*  695 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  697 */       this.renderer.color(colorBits);
/*  698 */       this.renderer.vertex(x + width, y + height, z + depth);
/*  699 */       this.renderer.color(colorBits);
/*  700 */       this.renderer.vertex(x, y + height, z + depth);
/*      */       
/*  702 */       this.renderer.color(colorBits);
/*  703 */       this.renderer.vertex(x, y + height, z + depth);
/*  704 */       this.renderer.color(colorBits);
/*  705 */       this.renderer.vertex(x, y + height, z);
/*      */       
/*  707 */       this.renderer.color(colorBits);
/*  708 */       this.renderer.vertex(x + width, y, z);
/*  709 */       this.renderer.color(colorBits);
/*  710 */       this.renderer.vertex(x + width, y + height, z);
/*      */       
/*  712 */       this.renderer.color(colorBits);
/*  713 */       this.renderer.vertex(x + width, y, z + depth);
/*  714 */       this.renderer.color(colorBits);
/*  715 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  717 */       this.renderer.color(colorBits);
/*  718 */       this.renderer.vertex(x, y, z + depth);
/*  719 */       this.renderer.color(colorBits);
/*  720 */       this.renderer.vertex(x, y + height, z + depth);
/*      */     } else {
/*  722 */       check(ShapeType.Line, ShapeType.Filled, 36);
/*      */ 
/*      */       
/*  725 */       this.renderer.color(colorBits);
/*  726 */       this.renderer.vertex(x, y, z);
/*  727 */       this.renderer.color(colorBits);
/*  728 */       this.renderer.vertex(x + width, y, z);
/*  729 */       this.renderer.color(colorBits);
/*  730 */       this.renderer.vertex(x + width, y + height, z);
/*      */       
/*  732 */       this.renderer.color(colorBits);
/*  733 */       this.renderer.vertex(x, y, z);
/*  734 */       this.renderer.color(colorBits);
/*  735 */       this.renderer.vertex(x + width, y + height, z);
/*  736 */       this.renderer.color(colorBits);
/*  737 */       this.renderer.vertex(x, y + height, z);
/*      */ 
/*      */       
/*  740 */       this.renderer.color(colorBits);
/*  741 */       this.renderer.vertex(x + width, y, z + depth);
/*  742 */       this.renderer.color(colorBits);
/*  743 */       this.renderer.vertex(x, y, z + depth);
/*  744 */       this.renderer.color(colorBits);
/*  745 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  747 */       this.renderer.color(colorBits);
/*  748 */       this.renderer.vertex(x, y + height, z + depth);
/*  749 */       this.renderer.color(colorBits);
/*  750 */       this.renderer.vertex(x, y, z + depth);
/*  751 */       this.renderer.color(colorBits);
/*  752 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */ 
/*      */       
/*  755 */       this.renderer.color(colorBits);
/*  756 */       this.renderer.vertex(x, y, z + depth);
/*  757 */       this.renderer.color(colorBits);
/*  758 */       this.renderer.vertex(x, y, z);
/*  759 */       this.renderer.color(colorBits);
/*  760 */       this.renderer.vertex(x, y + height, z);
/*      */       
/*  762 */       this.renderer.color(colorBits);
/*  763 */       this.renderer.vertex(x, y, z + depth);
/*  764 */       this.renderer.color(colorBits);
/*  765 */       this.renderer.vertex(x, y + height, z);
/*  766 */       this.renderer.color(colorBits);
/*  767 */       this.renderer.vertex(x, y + height, z + depth);
/*      */ 
/*      */       
/*  770 */       this.renderer.color(colorBits);
/*  771 */       this.renderer.vertex(x + width, y, z);
/*  772 */       this.renderer.color(colorBits);
/*  773 */       this.renderer.vertex(x + width, y, z + depth);
/*  774 */       this.renderer.color(colorBits);
/*  775 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  777 */       this.renderer.color(colorBits);
/*  778 */       this.renderer.vertex(x + width, y, z);
/*  779 */       this.renderer.color(colorBits);
/*  780 */       this.renderer.vertex(x + width, y + height, z + depth);
/*  781 */       this.renderer.color(colorBits);
/*  782 */       this.renderer.vertex(x + width, y + height, z);
/*      */ 
/*      */       
/*  785 */       this.renderer.color(colorBits);
/*  786 */       this.renderer.vertex(x, y + height, z);
/*  787 */       this.renderer.color(colorBits);
/*  788 */       this.renderer.vertex(x + width, y + height, z);
/*  789 */       this.renderer.color(colorBits);
/*  790 */       this.renderer.vertex(x + width, y + height, z + depth);
/*      */       
/*  792 */       this.renderer.color(colorBits);
/*  793 */       this.renderer.vertex(x, y + height, z);
/*  794 */       this.renderer.color(colorBits);
/*  795 */       this.renderer.vertex(x + width, y + height, z + depth);
/*  796 */       this.renderer.color(colorBits);
/*  797 */       this.renderer.vertex(x, y + height, z + depth);
/*      */ 
/*      */       
/*  800 */       this.renderer.color(colorBits);
/*  801 */       this.renderer.vertex(x, y, z + depth);
/*  802 */       this.renderer.color(colorBits);
/*  803 */       this.renderer.vertex(x + width, y, z + depth);
/*  804 */       this.renderer.color(colorBits);
/*  805 */       this.renderer.vertex(x + width, y, z);
/*      */       
/*  807 */       this.renderer.color(colorBits);
/*  808 */       this.renderer.vertex(x, y, z + depth);
/*  809 */       this.renderer.color(colorBits);
/*  810 */       this.renderer.vertex(x + width, y, z);
/*  811 */       this.renderer.color(colorBits);
/*  812 */       this.renderer.vertex(x, y, z);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void x(float x, float y, float size) {
/*  819 */     line(x - size, y - size, x + size, y + size);
/*  820 */     line(x - size, y + size, x + size, y - size);
/*      */   }
/*      */ 
/*      */   
/*      */   public void x(Vector2 p, float size) {
/*  825 */     x(p.x, p.y, size);
/*      */   }
/*      */ 
/*      */   
/*      */   public void arc(float x, float y, float radius, float start, float degrees) {
/*  830 */     arc(x, y, radius, start, degrees, Math.max(1, (int)(6.0F * (float)Math.cbrt(radius) * degrees / 360.0F)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void arc(float x, float y, float radius, float start, float degrees, int segments) {
/*  835 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  836 */     float colorBits = this.color.toFloatBits();
/*  837 */     float theta = 6.2831855F * degrees / 360.0F / segments;
/*  838 */     float cos = MathUtils.cos(theta);
/*  839 */     float sin = MathUtils.sin(theta);
/*  840 */     float cx = radius * MathUtils.cos(start * 0.017453292F);
/*  841 */     float cy = radius * MathUtils.sin(start * 0.017453292F);
/*      */     
/*  843 */     if (this.shapeType == ShapeType.Line) {
/*  844 */       check(ShapeType.Line, ShapeType.Filled, segments * 2 + 2);
/*      */       
/*  846 */       this.renderer.color(colorBits);
/*  847 */       this.renderer.vertex(x, y, 0.0F);
/*  848 */       this.renderer.color(colorBits);
/*  849 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  850 */       for (int i = 0; i < segments; i++) {
/*  851 */         this.renderer.color(colorBits);
/*  852 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  853 */         float f = cx;
/*  854 */         cx = cos * cx - sin * cy;
/*  855 */         cy = sin * f + cos * cy;
/*  856 */         this.renderer.color(colorBits);
/*  857 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*  859 */       this.renderer.color(colorBits);
/*  860 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } else {
/*  862 */       check(ShapeType.Line, ShapeType.Filled, segments * 3 + 3);
/*      */       
/*  864 */       for (int i = 0; i < segments; i++) {
/*  865 */         this.renderer.color(colorBits);
/*  866 */         this.renderer.vertex(x, y, 0.0F);
/*  867 */         this.renderer.color(colorBits);
/*  868 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  869 */         float f = cx;
/*  870 */         cx = cos * cx - sin * cy;
/*  871 */         cy = sin * f + cos * cy;
/*  872 */         this.renderer.color(colorBits);
/*  873 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*  875 */       this.renderer.color(colorBits);
/*  876 */       this.renderer.vertex(x, y, 0.0F);
/*  877 */       this.renderer.color(colorBits);
/*  878 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } 
/*      */     
/*  881 */     float temp = cx;
/*  882 */     cx = 0.0F;
/*  883 */     cy = 0.0F;
/*  884 */     this.renderer.color(colorBits);
/*  885 */     this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void circle(float x, float y, float radius) {
/*  890 */     circle(x, y, radius, Math.max(1, (int)(6.0F * (float)Math.cbrt(radius))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void circle(float x, float y, float radius, int segments) {
/*  895 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  896 */     float colorBits = this.color.toFloatBits();
/*  897 */     float angle = 6.2831855F / segments;
/*  898 */     float cos = MathUtils.cos(angle);
/*  899 */     float sin = MathUtils.sin(angle);
/*  900 */     float cx = radius, cy = 0.0F;
/*  901 */     if (this.shapeType == ShapeType.Line) {
/*  902 */       check(ShapeType.Line, ShapeType.Filled, segments * 2 + 2);
/*  903 */       for (int i = 0; i < segments; i++) {
/*  904 */         this.renderer.color(colorBits);
/*  905 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  906 */         float f = cx;
/*  907 */         cx = cos * cx - sin * cy;
/*  908 */         cy = sin * f + cos * cy;
/*  909 */         this.renderer.color(colorBits);
/*  910 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*      */       
/*  913 */       this.renderer.color(colorBits);
/*  914 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } else {
/*  916 */       check(ShapeType.Line, ShapeType.Filled, segments * 3 + 3);
/*  917 */       segments--;
/*  918 */       for (int i = 0; i < segments; i++) {
/*  919 */         this.renderer.color(colorBits);
/*  920 */         this.renderer.vertex(x, y, 0.0F);
/*  921 */         this.renderer.color(colorBits);
/*  922 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*  923 */         float f = cx;
/*  924 */         cx = cos * cx - sin * cy;
/*  925 */         cy = sin * f + cos * cy;
/*  926 */         this.renderer.color(colorBits);
/*  927 */         this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */       } 
/*      */       
/*  930 */       this.renderer.color(colorBits);
/*  931 */       this.renderer.vertex(x, y, 0.0F);
/*  932 */       this.renderer.color(colorBits);
/*  933 */       this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */     } 
/*      */     
/*  936 */     float temp = cx;
/*  937 */     cx = radius;
/*  938 */     cy = 0.0F;
/*  939 */     this.renderer.color(colorBits);
/*  940 */     this.renderer.vertex(x + cx, y + cy, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height) {
/*  945 */     ellipse(x, y, width, height, Math.max(1, (int)(12.0F * (float)Math.cbrt(Math.max(width * 0.5F, height * 0.5F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height, int segments) {
/*  950 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  951 */     check(ShapeType.Line, ShapeType.Filled, segments * 3);
/*  952 */     float colorBits = this.color.toFloatBits();
/*  953 */     float angle = 6.2831855F / segments;
/*      */     
/*  955 */     float cx = x + width / 2.0F, cy = y + height / 2.0F;
/*  956 */     if (this.shapeType == ShapeType.Line) {
/*  957 */       for (int i = 0; i < segments; i++) {
/*  958 */         this.renderer.color(colorBits);
/*  959 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos(i * angle), cy + height * 0.5F * MathUtils.sin(i * angle), 0.0F);
/*      */         
/*  961 */         this.renderer.color(colorBits);
/*  962 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos((i + 1) * angle), cy + height * 0.5F * 
/*  963 */             MathUtils.sin((i + 1) * angle), 0.0F);
/*      */       } 
/*      */     } else {
/*  966 */       for (int i = 0; i < segments; i++) {
/*  967 */         this.renderer.color(colorBits);
/*  968 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos(i * angle), cy + height * 0.5F * MathUtils.sin(i * angle), 0.0F);
/*      */         
/*  970 */         this.renderer.color(colorBits);
/*  971 */         this.renderer.vertex(cx, cy, 0.0F);
/*      */         
/*  973 */         this.renderer.color(colorBits);
/*  974 */         this.renderer.vertex(cx + width * 0.5F * MathUtils.cos((i + 1) * angle), cy + height * 0.5F * 
/*  975 */             MathUtils.sin((i + 1) * angle), 0.0F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height, float rotation) {
/*  982 */     ellipse(x, y, width, height, rotation, Math.max(1, (int)(12.0F * (float)Math.cbrt(Math.max(width * 0.5F, height * 0.5F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void ellipse(float x, float y, float width, float height, float rotation, int segments) {
/*  987 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/*  988 */     check(ShapeType.Line, ShapeType.Filled, segments * 3);
/*  989 */     float colorBits = this.color.toFloatBits();
/*  990 */     float angle = 6.2831855F / segments;
/*      */     
/*  992 */     rotation = 3.1415927F * rotation / 180.0F;
/*  993 */     float sin = MathUtils.sin(rotation);
/*  994 */     float cos = MathUtils.cos(rotation);
/*      */     
/*  996 */     float cx = x + width / 2.0F, cy = y + height / 2.0F;
/*  997 */     float x1 = width * 0.5F;
/*  998 */     float y1 = 0.0F;
/*  999 */     if (this.shapeType == ShapeType.Line) {
/* 1000 */       for (int i = 0; i < segments; i++) {
/* 1001 */         this.renderer.color(colorBits);
/* 1002 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */         
/* 1004 */         x1 = width * 0.5F * MathUtils.cos((i + 1) * angle);
/* 1005 */         y1 = height * 0.5F * MathUtils.sin((i + 1) * angle);
/*      */         
/* 1007 */         this.renderer.color(colorBits);
/* 1008 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */       } 
/*      */     } else {
/* 1011 */       for (int i = 0; i < segments; i++) {
/* 1012 */         this.renderer.color(colorBits);
/* 1013 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */         
/* 1015 */         this.renderer.color(colorBits);
/* 1016 */         this.renderer.vertex(cx, cy, 0.0F);
/*      */         
/* 1018 */         x1 = width * 0.5F * MathUtils.cos((i + 1) * angle);
/* 1019 */         y1 = height * 0.5F * MathUtils.sin((i + 1) * angle);
/*      */         
/* 1021 */         this.renderer.color(colorBits);
/* 1022 */         this.renderer.vertex(cx + cos * x1 - sin * y1, cy + sin * x1 + cos * y1, 0.0F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void cone(float x, float y, float z, float radius, float height) {
/* 1030 */     cone(x, y, z, radius, height, Math.max(1, (int)(4.0F * (float)Math.sqrt(radius))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void cone(float x, float y, float z, float radius, float height, int segments) {
/* 1035 */     if (segments <= 0) throw new IllegalArgumentException("segments must be > 0."); 
/* 1036 */     check(ShapeType.Line, ShapeType.Filled, segments * 4 + 2);
/* 1037 */     float colorBits = this.color.toFloatBits();
/* 1038 */     float angle = 6.2831855F / segments;
/* 1039 */     float cos = MathUtils.cos(angle);
/* 1040 */     float sin = MathUtils.sin(angle);
/* 1041 */     float cx = radius, cy = 0.0F;
/* 1042 */     if (this.shapeType == ShapeType.Line) {
/* 1043 */       for (int i = 0; i < segments; i++) {
/* 1044 */         this.renderer.color(colorBits);
/* 1045 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1046 */         this.renderer.color(colorBits);
/* 1047 */         this.renderer.vertex(x, y, z + height);
/* 1048 */         this.renderer.color(colorBits);
/* 1049 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1050 */         float f = cx;
/* 1051 */         cx = cos * cx - sin * cy;
/* 1052 */         cy = sin * f + cos * cy;
/* 1053 */         this.renderer.color(colorBits);
/* 1054 */         this.renderer.vertex(x + cx, y + cy, z);
/*      */       } 
/*      */       
/* 1057 */       this.renderer.color(colorBits);
/* 1058 */       this.renderer.vertex(x + cx, y + cy, z);
/*      */     } else {
/* 1060 */       segments--;
/* 1061 */       for (int i = 0; i < segments; i++) {
/* 1062 */         this.renderer.color(colorBits);
/* 1063 */         this.renderer.vertex(x, y, z);
/* 1064 */         this.renderer.color(colorBits);
/* 1065 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1066 */         float f1 = cx;
/* 1067 */         float f2 = cy;
/* 1068 */         cx = cos * cx - sin * cy;
/* 1069 */         cy = sin * f1 + cos * cy;
/* 1070 */         this.renderer.color(colorBits);
/* 1071 */         this.renderer.vertex(x + cx, y + cy, z);
/*      */         
/* 1073 */         this.renderer.color(colorBits);
/* 1074 */         this.renderer.vertex(x + f1, y + f2, z);
/* 1075 */         this.renderer.color(colorBits);
/* 1076 */         this.renderer.vertex(x + cx, y + cy, z);
/* 1077 */         this.renderer.color(colorBits);
/* 1078 */         this.renderer.vertex(x, y, z + height);
/*      */       } 
/*      */       
/* 1081 */       this.renderer.color(colorBits);
/* 1082 */       this.renderer.vertex(x, y, z);
/* 1083 */       this.renderer.color(colorBits);
/* 1084 */       this.renderer.vertex(x + cx, y + cy, z);
/*      */     } 
/* 1086 */     float temp = cx;
/* 1087 */     float temp2 = cy;
/* 1088 */     cx = radius;
/* 1089 */     cy = 0.0F;
/* 1090 */     this.renderer.color(colorBits);
/* 1091 */     this.renderer.vertex(x + cx, y + cy, z);
/* 1092 */     if (this.shapeType != ShapeType.Line) {
/* 1093 */       this.renderer.color(colorBits);
/* 1094 */       this.renderer.vertex(x + temp, y + temp2, z);
/* 1095 */       this.renderer.color(colorBits);
/* 1096 */       this.renderer.vertex(x + cx, y + cy, z);
/* 1097 */       this.renderer.color(colorBits);
/* 1098 */       this.renderer.vertex(x, y, z + height);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void polygon(float[] vertices, int offset, int count) {
/* 1104 */     if (count < 6) throw new IllegalArgumentException("Polygons must contain at least 3 points."); 
/* 1105 */     if (count % 2 != 0) throw new IllegalArgumentException("Polygons must have an even number of vertices.");
/*      */     
/* 1107 */     check(ShapeType.Line, null, count);
/* 1108 */     float colorBits = this.color.toFloatBits();
/* 1109 */     float firstX = vertices[0];
/* 1110 */     float firstY = vertices[1];
/*      */     
/* 1112 */     for (int i = offset, n = offset + count; i < n; i += 2) {
/* 1113 */       float x2, y2, x1 = vertices[i];
/* 1114 */       float y1 = vertices[i + 1];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1119 */       if (i + 2 >= count) {
/* 1120 */         x2 = firstX;
/* 1121 */         y2 = firstY;
/*      */       } else {
/* 1123 */         x2 = vertices[i + 2];
/* 1124 */         y2 = vertices[i + 3];
/*      */       } 
/*      */       
/* 1127 */       this.renderer.color(colorBits);
/* 1128 */       this.renderer.vertex(x1, y1, 0.0F);
/* 1129 */       this.renderer.color(colorBits);
/* 1130 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void polygon(float[] vertices) {
/* 1136 */     polygon(vertices, 0, vertices.length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void polyline(float[] vertices, int offset, int count) {
/* 1141 */     if (count < 4) throw new IllegalArgumentException("Polylines must contain at least 2 points."); 
/* 1142 */     if (count % 2 != 0) throw new IllegalArgumentException("Polylines must have an even number of vertices.");
/*      */     
/* 1144 */     check(ShapeType.Line, null, count);
/* 1145 */     float colorBits = this.color.toFloatBits();
/* 1146 */     for (int i = offset, n = offset + count - 2; i < n; i += 2) {
/* 1147 */       float x1 = vertices[i];
/* 1148 */       float y1 = vertices[i + 1];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1153 */       float x2 = vertices[i + 2];
/* 1154 */       float y2 = vertices[i + 3];
/*      */       
/* 1156 */       this.renderer.color(colorBits);
/* 1157 */       this.renderer.vertex(x1, y1, 0.0F);
/* 1158 */       this.renderer.color(colorBits);
/* 1159 */       this.renderer.vertex(x2, y2, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void polyline(float[] vertices) {
/* 1165 */     polyline(vertices, 0, vertices.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private void check(ShapeType preferred, ShapeType other, int newVertices) {
/* 1170 */     if (this.shapeType == null) throw new IllegalStateException("begin must be called first.");
/*      */     
/* 1172 */     if (this.shapeType != preferred && this.shapeType != other) {
/*      */       
/* 1174 */       if (!this.autoShapeType) {
/* 1175 */         if (other == null) {
/* 1176 */           throw new IllegalStateException("Must call begin(ShapeType." + preferred + ").");
/*      */         }
/* 1178 */         throw new IllegalStateException("Must call begin(ShapeType." + preferred + ") or begin(ShapeType." + other + ").");
/*      */       } 
/* 1180 */       end();
/* 1181 */       begin(preferred);
/* 1182 */     } else if (this.matrixDirty) {
/*      */       
/* 1184 */       ShapeType type = this.shapeType;
/* 1185 */       end();
/* 1186 */       begin(type);
/* 1187 */     } else if (this.renderer.getMaxVertices() - this.renderer.getNumVertices() < newVertices) {
/*      */       
/* 1189 */       ShapeType type = this.shapeType;
/* 1190 */       end();
/* 1191 */       begin(type);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void end() {
/* 1197 */     this.renderer.end();
/* 1198 */     this.shapeType = null;
/*      */   }
/*      */   
/*      */   public void flush() {
/* 1202 */     ShapeType type = this.shapeType;
/* 1203 */     end();
/* 1204 */     begin(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public ShapeType getCurrentType() {
/* 1209 */     return this.shapeType;
/*      */   }
/*      */   
/*      */   public ImmediateModeRenderer getRenderer() {
/* 1213 */     return this.renderer;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDrawing() {
/* 1218 */     return (this.shapeType != null);
/*      */   }
/*      */   
/*      */   public void dispose() {
/* 1222 */     this.renderer.dispose();
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\ShapeRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */