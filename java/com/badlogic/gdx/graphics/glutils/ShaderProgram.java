/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.math.Matrix3;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.ObjectIntMap;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
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
/*     */ public class ShaderProgram
/*     */   implements Disposable
/*     */ {
/*     */   public static final String POSITION_ATTRIBUTE = "a_position";
/*     */   public static final String NORMAL_ATTRIBUTE = "a_normal";
/*     */   public static final String COLOR_ATTRIBUTE = "a_color";
/*     */   public static final String TEXCOORD_ATTRIBUTE = "a_texCoord";
/*     */   public static final String TANGENT_ATTRIBUTE = "a_tangent";
/*     */   public static final String BINORMAL_ATTRIBUTE = "a_binormal";
/*     */   public static boolean pedantic = true;
/*  88 */   public static String prependVertexCode = "";
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static String prependFragmentCode = "";
/*     */ 
/*     */   
/*  95 */   private static final ObjectMap<Application, Array<ShaderProgram>> shaders = new ObjectMap();
/*     */ 
/*     */   
/*  98 */   private String log = "";
/*     */ 
/*     */   
/*     */   private boolean isCompiled;
/*     */ 
/*     */   
/* 104 */   private final ObjectIntMap<String> uniforms = new ObjectIntMap();
/*     */ 
/*     */   
/* 107 */   private final ObjectIntMap<String> uniformTypes = new ObjectIntMap();
/*     */ 
/*     */   
/* 110 */   private final ObjectIntMap<String> uniformSizes = new ObjectIntMap();
/*     */ 
/*     */   
/*     */   private String[] uniformNames;
/*     */ 
/*     */   
/* 116 */   private final ObjectIntMap<String> attributes = new ObjectIntMap();
/*     */ 
/*     */   
/* 119 */   private final ObjectIntMap<String> attributeTypes = new ObjectIntMap();
/*     */ 
/*     */   
/* 122 */   private final ObjectIntMap<String> attributeSizes = new ObjectIntMap();
/*     */ 
/*     */   
/*     */   private String[] attributeNames;
/*     */ 
/*     */   
/*     */   private int program;
/*     */ 
/*     */   
/*     */   private int vertexShaderHandle;
/*     */ 
/*     */   
/*     */   private int fragmentShaderHandle;
/*     */ 
/*     */   
/*     */   private final FloatBuffer matrix;
/*     */ 
/*     */   
/*     */   private final String vertexShaderSource;
/*     */ 
/*     */   
/*     */   private final String fragmentShaderSource;
/*     */ 
/*     */   
/*     */   private boolean invalidated;
/*     */ 
/*     */   
/* 149 */   private int refCount = 0;
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
/*     */   public ShaderProgram(FileHandle vertexShader, FileHandle fragmentShader) {
/* 178 */     this(vertexShader.readString(), fragmentShader.readString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void compileShaders(String vertexShader, String fragmentShader) {
/* 186 */     this.vertexShaderHandle = loadShader(35633, vertexShader);
/* 187 */     this.fragmentShaderHandle = loadShader(35632, fragmentShader);
/*     */     
/* 189 */     if (this.vertexShaderHandle == -1 || this.fragmentShaderHandle == -1) {
/* 190 */       this.isCompiled = false;
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     this.program = linkProgram(createProgram());
/* 195 */     if (this.program == -1) {
/* 196 */       this.isCompiled = false;
/*     */       
/*     */       return;
/*     */     } 
/* 200 */     this.isCompiled = true;
/*     */   }
/*     */   
/*     */   private int loadShader(int type, String source) {
/* 204 */     GL20 gl = Gdx.gl20;
/* 205 */     IntBuffer intbuf = BufferUtils.newIntBuffer(1);
/*     */     
/* 207 */     int shader = gl.glCreateShader(type);
/* 208 */     if (shader == 0) return -1;
/*     */     
/* 210 */     gl.glShaderSource(shader, source);
/* 211 */     gl.glCompileShader(shader);
/* 212 */     gl.glGetShaderiv(shader, 35713, intbuf);
/*     */     
/* 214 */     int compiled = intbuf.get(0);
/* 215 */     if (compiled == 0) {
/*     */ 
/*     */ 
/*     */       
/* 219 */       String infoLog = gl.glGetShaderInfoLog(shader);
/* 220 */       this.log += infoLog;
/*     */       
/* 222 */       return -1;
/*     */     } 
/*     */     
/* 225 */     return shader;
/*     */   }
/*     */   
/*     */   protected int createProgram() {
/* 229 */     GL20 gl = Gdx.gl20;
/* 230 */     int program = gl.glCreateProgram();
/* 231 */     return (program != 0) ? program : -1;
/*     */   }
/*     */   
/*     */   private int linkProgram(int program) {
/* 235 */     GL20 gl = Gdx.gl20;
/* 236 */     if (program == -1) return -1;
/*     */     
/* 238 */     gl.glAttachShader(program, this.vertexShaderHandle);
/* 239 */     gl.glAttachShader(program, this.fragmentShaderHandle);
/* 240 */     gl.glLinkProgram(program);
/*     */     
/* 242 */     ByteBuffer tmp = ByteBuffer.allocateDirect(4);
/* 243 */     tmp.order(ByteOrder.nativeOrder());
/* 244 */     IntBuffer intbuf = tmp.asIntBuffer();
/*     */     
/* 246 */     gl.glGetProgramiv(program, 35714, intbuf);
/* 247 */     int linked = intbuf.get(0);
/* 248 */     if (linked == 0) {
/*     */ 
/*     */ 
/*     */       
/* 252 */       this.log = Gdx.gl20.glGetProgramInfoLog(program);
/*     */       
/* 254 */       return -1;
/*     */     } 
/*     */     
/* 257 */     return program;
/*     */   }
/*     */   
/* 260 */   static final IntBuffer intbuf = BufferUtils.newIntBuffer(1);
/*     */   IntBuffer params;
/*     */   IntBuffer type;
/*     */   
/*     */   public String getLog() {
/* 265 */     if (this.isCompiled) {
/*     */ 
/*     */ 
/*     */       
/* 269 */       this.log = Gdx.gl20.glGetProgramInfoLog(this.program);
/*     */       
/* 271 */       return this.log;
/*     */     } 
/* 273 */     return this.log;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompiled() {
/* 279 */     return this.isCompiled;
/*     */   }
/*     */   
/*     */   private int fetchAttributeLocation(String name) {
/* 283 */     GL20 gl = Gdx.gl20;
/*     */     
/*     */     int location;
/*     */     
/* 287 */     if ((location = this.attributes.get(name, -2)) == -2) {
/* 288 */       location = gl.glGetAttribLocation(this.program, name);
/* 289 */       this.attributes.put(name, location);
/*     */     } 
/* 291 */     return location;
/*     */   }
/*     */   
/*     */   private int fetchUniformLocation(String name) {
/* 295 */     return fetchUniformLocation(name, pedantic);
/*     */   }
/*     */   
/*     */   public int fetchUniformLocation(String name, boolean pedantic) {
/* 299 */     GL20 gl = Gdx.gl20;
/*     */     
/*     */     int location;
/*     */     
/* 303 */     if ((location = this.uniforms.get(name, -2)) == -2) {
/* 304 */       location = gl.glGetUniformLocation(this.program, name);
/* 305 */       if (location == -1 && pedantic) throw new IllegalArgumentException("no uniform with name '" + name + "' in shader"); 
/* 306 */       this.uniforms.put(name, location);
/*     */     } 
/* 308 */     return location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformi(String name, int value) {
/* 316 */     GL20 gl = Gdx.gl20;
/* 317 */     checkManaged();
/* 318 */     int location = fetchUniformLocation(name);
/* 319 */     gl.glUniform1i(location, value);
/*     */   }
/*     */   
/*     */   public void setUniformi(int location, int value) {
/* 323 */     GL20 gl = Gdx.gl20;
/* 324 */     checkManaged();
/* 325 */     gl.glUniform1i(location, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformi(String name, int value1, int value2) {
/* 334 */     GL20 gl = Gdx.gl20;
/* 335 */     checkManaged();
/* 336 */     int location = fetchUniformLocation(name);
/* 337 */     gl.glUniform2i(location, value1, value2);
/*     */   }
/*     */   
/*     */   public void setUniformi(int location, int value1, int value2) {
/* 341 */     GL20 gl = Gdx.gl20;
/* 342 */     checkManaged();
/* 343 */     gl.glUniform2i(location, value1, value2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformi(String name, int value1, int value2, int value3) {
/* 353 */     GL20 gl = Gdx.gl20;
/* 354 */     checkManaged();
/* 355 */     int location = fetchUniformLocation(name);
/* 356 */     gl.glUniform3i(location, value1, value2, value3);
/*     */   }
/*     */   
/*     */   public void setUniformi(int location, int value1, int value2, int value3) {
/* 360 */     GL20 gl = Gdx.gl20;
/* 361 */     checkManaged();
/* 362 */     gl.glUniform3i(location, value1, value2, value3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformi(String name, int value1, int value2, int value3, int value4) {
/* 373 */     GL20 gl = Gdx.gl20;
/* 374 */     checkManaged();
/* 375 */     int location = fetchUniformLocation(name);
/* 376 */     gl.glUniform4i(location, value1, value2, value3, value4);
/*     */   }
/*     */   
/*     */   public void setUniformi(int location, int value1, int value2, int value3, int value4) {
/* 380 */     GL20 gl = Gdx.gl20;
/* 381 */     checkManaged();
/* 382 */     gl.glUniform4i(location, value1, value2, value3, value4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformf(String name, float value) {
/* 390 */     GL20 gl = Gdx.gl20;
/* 391 */     checkManaged();
/* 392 */     int location = fetchUniformLocation(name);
/* 393 */     gl.glUniform1f(location, value);
/*     */   }
/*     */   
/*     */   public void setUniformf(int location, float value) {
/* 397 */     GL20 gl = Gdx.gl20;
/* 398 */     checkManaged();
/* 399 */     gl.glUniform1f(location, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformf(String name, float value1, float value2) {
/* 408 */     GL20 gl = Gdx.gl20;
/* 409 */     checkManaged();
/* 410 */     int location = fetchUniformLocation(name);
/* 411 */     gl.glUniform2f(location, value1, value2);
/*     */   }
/*     */   
/*     */   public void setUniformf(int location, float value1, float value2) {
/* 415 */     GL20 gl = Gdx.gl20;
/* 416 */     checkManaged();
/* 417 */     gl.glUniform2f(location, value1, value2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformf(String name, float value1, float value2, float value3) {
/* 427 */     GL20 gl = Gdx.gl20;
/* 428 */     checkManaged();
/* 429 */     int location = fetchUniformLocation(name);
/* 430 */     gl.glUniform3f(location, value1, value2, value3);
/*     */   }
/*     */   
/*     */   public void setUniformf(int location, float value1, float value2, float value3) {
/* 434 */     GL20 gl = Gdx.gl20;
/* 435 */     checkManaged();
/* 436 */     gl.glUniform3f(location, value1, value2, value3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformf(String name, float value1, float value2, float value3, float value4) {
/* 447 */     GL20 gl = Gdx.gl20;
/* 448 */     checkManaged();
/* 449 */     int location = fetchUniformLocation(name);
/* 450 */     gl.glUniform4f(location, value1, value2, value3, value4);
/*     */   }
/*     */   
/*     */   public void setUniformf(int location, float value1, float value2, float value3, float value4) {
/* 454 */     GL20 gl = Gdx.gl20;
/* 455 */     checkManaged();
/* 456 */     gl.glUniform4f(location, value1, value2, value3, value4);
/*     */   }
/*     */   
/*     */   public void setUniform1fv(String name, float[] values, int offset, int length) {
/* 460 */     GL20 gl = Gdx.gl20;
/* 461 */     checkManaged();
/* 462 */     int location = fetchUniformLocation(name);
/* 463 */     gl.glUniform1fv(location, length, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniform1fv(int location, float[] values, int offset, int length) {
/* 467 */     GL20 gl = Gdx.gl20;
/* 468 */     checkManaged();
/* 469 */     gl.glUniform1fv(location, length, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniform2fv(String name, float[] values, int offset, int length) {
/* 473 */     GL20 gl = Gdx.gl20;
/* 474 */     checkManaged();
/* 475 */     int location = fetchUniformLocation(name);
/* 476 */     gl.glUniform2fv(location, length / 2, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniform2fv(int location, float[] values, int offset, int length) {
/* 480 */     GL20 gl = Gdx.gl20;
/* 481 */     checkManaged();
/* 482 */     gl.glUniform2fv(location, length / 2, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniform3fv(String name, float[] values, int offset, int length) {
/* 486 */     GL20 gl = Gdx.gl20;
/* 487 */     checkManaged();
/* 488 */     int location = fetchUniformLocation(name);
/* 489 */     gl.glUniform3fv(location, length / 3, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniform3fv(int location, float[] values, int offset, int length) {
/* 493 */     GL20 gl = Gdx.gl20;
/* 494 */     checkManaged();
/* 495 */     gl.glUniform3fv(location, length / 3, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniform4fv(String name, float[] values, int offset, int length) {
/* 499 */     GL20 gl = Gdx.gl20;
/* 500 */     checkManaged();
/* 501 */     int location = fetchUniformLocation(name);
/* 502 */     gl.glUniform4fv(location, length / 4, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniform4fv(int location, float[] values, int offset, int length) {
/* 506 */     GL20 gl = Gdx.gl20;
/* 507 */     checkManaged();
/* 508 */     gl.glUniform4fv(location, length / 4, values, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformMatrix(String name, Matrix4 matrix) {
/* 516 */     setUniformMatrix(name, matrix, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformMatrix(String name, Matrix4 matrix, boolean transpose) {
/* 525 */     setUniformMatrix(fetchUniformLocation(name), matrix, transpose);
/*     */   }
/*     */   
/*     */   public void setUniformMatrix(int location, Matrix4 matrix) {
/* 529 */     setUniformMatrix(location, matrix, false);
/*     */   }
/*     */   
/*     */   public void setUniformMatrix(int location, Matrix4 matrix, boolean transpose) {
/* 533 */     GL20 gl = Gdx.gl20;
/* 534 */     checkManaged();
/* 535 */     gl.glUniformMatrix4fv(location, 1, transpose, matrix.val, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformMatrix(String name, Matrix3 matrix) {
/* 543 */     setUniformMatrix(name, matrix, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformMatrix(String name, Matrix3 matrix, boolean transpose) {
/* 552 */     setUniformMatrix(fetchUniformLocation(name), matrix, transpose);
/*     */   }
/*     */   
/*     */   public void setUniformMatrix(int location, Matrix3 matrix) {
/* 556 */     setUniformMatrix(location, matrix, false);
/*     */   }
/*     */   
/*     */   public void setUniformMatrix(int location, Matrix3 matrix, boolean transpose) {
/* 560 */     GL20 gl = Gdx.gl20;
/* 561 */     checkManaged();
/* 562 */     gl.glUniformMatrix3fv(location, 1, transpose, matrix.val, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformMatrix3fv(String name, FloatBuffer buffer, int count, boolean transpose) {
/* 571 */     GL20 gl = Gdx.gl20;
/* 572 */     checkManaged();
/* 573 */     buffer.position(0);
/* 574 */     int location = fetchUniformLocation(name);
/* 575 */     gl.glUniformMatrix3fv(location, count, transpose, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformMatrix4fv(String name, FloatBuffer buffer, int count, boolean transpose) {
/* 584 */     GL20 gl = Gdx.gl20;
/* 585 */     checkManaged();
/* 586 */     buffer.position(0);
/* 587 */     int location = fetchUniformLocation(name);
/* 588 */     gl.glUniformMatrix4fv(location, count, transpose, buffer);
/*     */   }
/*     */   
/*     */   public void setUniformMatrix4fv(int location, float[] values, int offset, int length) {
/* 592 */     GL20 gl = Gdx.gl20;
/* 593 */     checkManaged();
/* 594 */     gl.glUniformMatrix4fv(location, length / 16, false, values, offset);
/*     */   }
/*     */   
/*     */   public void setUniformMatrix4fv(String name, float[] values, int offset, int length) {
/* 598 */     setUniformMatrix4fv(fetchUniformLocation(name), values, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformf(String name, Vector2 values) {
/* 606 */     setUniformf(name, values.x, values.y);
/*     */   }
/*     */   
/*     */   public void setUniformf(int location, Vector2 values) {
/* 610 */     setUniformf(location, values.x, values.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformf(String name, Vector3 values) {
/* 618 */     setUniformf(name, values.x, values.y, values.z);
/*     */   }
/*     */   
/*     */   public void setUniformf(int location, Vector3 values) {
/* 622 */     setUniformf(location, values.x, values.y, values.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniformf(String name, Color values) {
/* 630 */     setUniformf(name, values.r, values.g, values.b, values.a);
/*     */   }
/*     */   
/*     */   public void setUniformf(int location, Color values) {
/* 634 */     setUniformf(location, values.r, values.g, values.b, values.a);
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
/*     */   public void setVertexAttribute(String name, int size, int type, boolean normalize, int stride, Buffer buffer) {
/* 647 */     GL20 gl = Gdx.gl20;
/* 648 */     checkManaged();
/* 649 */     int location = fetchAttributeLocation(name);
/* 650 */     if (location == -1)
/* 651 */       return;  gl.glVertexAttribPointer(location, size, type, normalize, stride, buffer);
/*     */   }
/*     */   
/*     */   public void setVertexAttribute(int location, int size, int type, boolean normalize, int stride, Buffer buffer) {
/* 655 */     GL20 gl = Gdx.gl20;
/* 656 */     checkManaged();
/* 657 */     gl.glVertexAttribPointer(location, size, type, normalize, stride, buffer);
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
/*     */   public void setVertexAttribute(String name, int size, int type, boolean normalize, int stride, int offset) {
/* 670 */     GL20 gl = Gdx.gl20;
/* 671 */     checkManaged();
/* 672 */     int location = fetchAttributeLocation(name);
/* 673 */     if (location == -1)
/* 674 */       return;  gl.glVertexAttribPointer(location, size, type, normalize, stride, offset);
/*     */   }
/*     */   
/*     */   public void setVertexAttribute(int location, int size, int type, boolean normalize, int stride, int offset) {
/* 678 */     GL20 gl = Gdx.gl20;
/* 679 */     checkManaged();
/* 680 */     gl.glVertexAttribPointer(location, size, type, normalize, stride, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void begin() {
/* 686 */     GL20 gl = Gdx.gl20;
/* 687 */     checkManaged();
/* 688 */     gl.glUseProgram(this.program);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() {
/* 694 */     GL20 gl = Gdx.gl20;
/* 695 */     gl.glUseProgram(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 700 */     GL20 gl = Gdx.gl20;
/* 701 */     gl.glUseProgram(0);
/* 702 */     gl.glDeleteShader(this.vertexShaderHandle);
/* 703 */     gl.glDeleteShader(this.fragmentShaderHandle);
/* 704 */     gl.glDeleteProgram(this.program);
/* 705 */     if (shaders.get(Gdx.app) != null) ((Array)shaders.get(Gdx.app)).removeValue(this, true);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void disableVertexAttribute(String name) {
/* 712 */     GL20 gl = Gdx.gl20;
/* 713 */     checkManaged();
/* 714 */     int location = fetchAttributeLocation(name);
/* 715 */     if (location == -1)
/* 716 */       return;  gl.glDisableVertexAttribArray(location);
/*     */   }
/*     */   
/*     */   public void disableVertexAttribute(int location) {
/* 720 */     GL20 gl = Gdx.gl20;
/* 721 */     checkManaged();
/* 722 */     gl.glDisableVertexAttribArray(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableVertexAttribute(String name) {
/* 729 */     GL20 gl = Gdx.gl20;
/* 730 */     checkManaged();
/* 731 */     int location = fetchAttributeLocation(name);
/* 732 */     if (location == -1)
/* 733 */       return;  gl.glEnableVertexAttribArray(location);
/*     */   }
/*     */   
/*     */   public void enableVertexAttribute(int location) {
/* 737 */     GL20 gl = Gdx.gl20;
/* 738 */     checkManaged();
/* 739 */     gl.glEnableVertexAttribArray(location);
/*     */   }
/*     */   
/*     */   private void checkManaged() {
/* 743 */     if (this.invalidated) {
/* 744 */       compileShaders(this.vertexShaderSource, this.fragmentShaderSource);
/* 745 */       this.invalidated = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addManagedShader(Application app, ShaderProgram shaderProgram) {
/* 750 */     Array<ShaderProgram> managedResources = (Array<ShaderProgram>)shaders.get(app);
/* 751 */     if (managedResources == null) managedResources = new Array(); 
/* 752 */     managedResources.add(shaderProgram);
/* 753 */     shaders.put(app, managedResources);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void invalidateAllShaderPrograms(Application app) {
/* 759 */     if (Gdx.gl20 == null)
/*     */       return; 
/* 761 */     Array<ShaderProgram> shaderArray = (Array<ShaderProgram>)shaders.get(app);
/* 762 */     if (shaderArray == null)
/*     */       return; 
/* 764 */     for (int i = 0; i < shaderArray.size; i++) {
/* 765 */       ((ShaderProgram)shaderArray.get(i)).invalidated = true;
/* 766 */       ((ShaderProgram)shaderArray.get(i)).checkManaged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clearAllShaderPrograms(Application app) {
/* 771 */     shaders.remove(app);
/*     */   }
/*     */   
/*     */   public static String getManagedStatus() {
/* 775 */     StringBuilder builder = new StringBuilder();
/* 776 */     int i = 0;
/* 777 */     builder.append("Managed shaders/app: { ");
/* 778 */     for (ObjectMap.Keys<Application> keys = shaders.keys().iterator(); keys.hasNext(); ) { Application app = keys.next();
/* 779 */       builder.append(((Array)shaders.get(app)).size);
/* 780 */       builder.append(" "); }
/*     */     
/* 782 */     builder.append("}");
/* 783 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumManagedShaderPrograms() {
/* 788 */     return ((Array)shaders.get(Gdx.app)).size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributef(String name, float value1, float value2, float value3, float value4) {
/* 799 */     GL20 gl = Gdx.gl20;
/* 800 */     int location = fetchAttributeLocation(name);
/* 801 */     gl.glVertexAttrib4f(location, value1, value2, value3, value4);
/*     */   }
/*     */   
/* 804 */   public ShaderProgram(String vertexShader, String fragmentShader) { this.params = BufferUtils.newIntBuffer(1);
/* 805 */     this.type = BufferUtils.newIntBuffer(1); if (vertexShader == null) throw new IllegalArgumentException("vertex shader must not be null");  if (fragmentShader == null)
/*     */       throw new IllegalArgumentException("fragment shader must not be null");  if (prependVertexCode != null && prependVertexCode.length() > 0)
/*     */       vertexShader = prependVertexCode + vertexShader;  if (prependFragmentCode != null && prependFragmentCode.length() > 0)
/* 808 */       fragmentShader = prependFragmentCode + fragmentShader;  this.vertexShaderSource = vertexShader; this.fragmentShaderSource = fragmentShader; this.matrix = BufferUtils.newFloatBuffer(16); compileShaders(vertexShader, fragmentShader); if (isCompiled()) { fetchAttributes(); fetchUniforms(); addManagedShader(Gdx.app, this); }  } private void fetchUniforms() { this.params.clear();
/* 809 */     Gdx.gl20.glGetProgramiv(this.program, 35718, this.params);
/* 810 */     int numUniforms = this.params.get(0);
/*     */     
/* 812 */     this.uniformNames = new String[numUniforms];
/*     */     
/* 814 */     for (int i = 0; i < numUniforms; i++) {
/* 815 */       this.params.clear();
/* 816 */       this.params.put(0, 1);
/* 817 */       this.type.clear();
/* 818 */       String name = Gdx.gl20.glGetActiveUniform(this.program, i, this.params, this.type);
/* 819 */       int location = Gdx.gl20.glGetUniformLocation(this.program, name);
/* 820 */       this.uniforms.put(name, location);
/* 821 */       this.uniformTypes.put(name, this.type.get(0));
/* 822 */       this.uniformSizes.put(name, this.params.get(0));
/* 823 */       this.uniformNames[i] = name;
/*     */     }  }
/*     */ 
/*     */   
/*     */   private void fetchAttributes() {
/* 828 */     this.params.clear();
/* 829 */     Gdx.gl20.glGetProgramiv(this.program, 35721, this.params);
/* 830 */     int numAttributes = this.params.get(0);
/*     */     
/* 832 */     this.attributeNames = new String[numAttributes];
/*     */     
/* 834 */     for (int i = 0; i < numAttributes; i++) {
/* 835 */       this.params.clear();
/* 836 */       this.params.put(0, 1);
/* 837 */       this.type.clear();
/* 838 */       String name = Gdx.gl20.glGetActiveAttrib(this.program, i, this.params, this.type);
/* 839 */       int location = Gdx.gl20.glGetAttribLocation(this.program, name);
/* 840 */       this.attributes.put(name, location);
/* 841 */       this.attributeTypes.put(name, this.type.get(0));
/* 842 */       this.attributeSizes.put(name, this.params.get(0));
/* 843 */       this.attributeNames[i] = name;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAttribute(String name) {
/* 850 */     return this.attributes.containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeType(String name) {
/* 856 */     return this.attributeTypes.get(name, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeLocation(String name) {
/* 862 */     return this.attributes.get(name, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeSize(String name) {
/* 868 */     return this.attributeSizes.get(name, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasUniform(String name) {
/* 874 */     return this.uniforms.containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUniformType(String name) {
/* 880 */     return this.uniformTypes.get(name, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUniformLocation(String name) {
/* 886 */     return this.uniforms.get(name, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUniformSize(String name) {
/* 892 */     return this.uniformSizes.get(name, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAttributes() {
/* 897 */     return this.attributeNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getUniforms() {
/* 902 */     return this.uniformNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVertexShaderSource() {
/* 907 */     return this.vertexShaderSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFragmentShaderSource() {
/* 912 */     return this.fragmentShaderSource;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\ShaderProgram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */