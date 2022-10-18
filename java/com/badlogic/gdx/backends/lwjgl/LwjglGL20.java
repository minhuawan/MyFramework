/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.EXTFramebufferObject;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL13;
/*     */ import org.lwjgl.opengl.GL14;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ import org.lwjgl.opengl.GL20;
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
/*     */ class LwjglGL20
/*     */   implements GL20
/*     */ {
/*  42 */   private ByteBuffer buffer = null;
/*  43 */   private FloatBuffer floatBuffer = null;
/*  44 */   private IntBuffer intBuffer = null;
/*     */   
/*     */   private void ensureBufferCapacity(int numBytes) {
/*  47 */     if (this.buffer == null || this.buffer.capacity() < numBytes) {
/*  48 */       this.buffer = BufferUtils.newByteBuffer(numBytes);
/*  49 */       this.floatBuffer = this.buffer.asFloatBuffer();
/*  50 */       this.intBuffer = this.buffer.asIntBuffer();
/*     */     } 
/*     */   }
/*     */   
/*     */   private FloatBuffer toFloatBuffer(float[] v, int offset, int count) {
/*  55 */     ensureBufferCapacity(count << 2);
/*  56 */     this.floatBuffer.clear();
/*  57 */     BufferUtils.copy(v, this.floatBuffer, count, offset);
/*  58 */     return this.floatBuffer;
/*     */   }
/*     */   
/*     */   private IntBuffer toIntBuffer(int[] v, int offset, int count) {
/*  62 */     ensureBufferCapacity(count << 2);
/*  63 */     this.intBuffer.clear();
/*  64 */     BufferUtils.copy(v, count, offset, this.intBuffer);
/*  65 */     return this.intBuffer;
/*     */   }
/*     */   
/*     */   public void glActiveTexture(int texture) {
/*  69 */     GL13.glActiveTexture(texture);
/*     */   }
/*     */   
/*     */   public void glAttachShader(int program, int shader) {
/*  73 */     GL20.glAttachShader(program, shader);
/*     */   }
/*     */   
/*     */   public void glBindAttribLocation(int program, int index, String name) {
/*  77 */     GL20.glBindAttribLocation(program, index, name);
/*     */   }
/*     */   
/*     */   public void glBindBuffer(int target, int buffer) {
/*  81 */     GL15.glBindBuffer(target, buffer);
/*     */   }
/*     */   
/*     */   public void glBindFramebuffer(int target, int framebuffer) {
/*  85 */     EXTFramebufferObject.glBindFramebufferEXT(target, framebuffer);
/*     */   }
/*     */   
/*     */   public void glBindRenderbuffer(int target, int renderbuffer) {
/*  89 */     EXTFramebufferObject.glBindRenderbufferEXT(target, renderbuffer);
/*     */   }
/*     */   
/*     */   public void glBindTexture(int target, int texture) {
/*  93 */     GL11.glBindTexture(target, texture);
/*     */   }
/*     */   
/*     */   public void glBlendColor(float red, float green, float blue, float alpha) {
/*  97 */     GL14.glBlendColor(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public void glBlendEquation(int mode) {
/* 101 */     GL14.glBlendEquation(mode);
/*     */   }
/*     */   
/*     */   public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
/* 105 */     GL20.glBlendEquationSeparate(modeRGB, modeAlpha);
/*     */   }
/*     */   
/*     */   public void glBlendFunc(int sfactor, int dfactor) {
/* 109 */     GL11.glBlendFunc(sfactor, dfactor);
/*     */   }
/*     */   
/*     */   public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
/* 113 */     GL14.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
/*     */   }
/*     */   
/*     */   public void glBufferData(int target, int size, Buffer data, int usage) {
/* 117 */     if (data == null) {
/* 118 */       GL15.glBufferData(target, size, usage);
/* 119 */     } else if (data instanceof ByteBuffer) {
/* 120 */       GL15.glBufferData(target, (ByteBuffer)data, usage);
/* 121 */     } else if (data instanceof IntBuffer) {
/* 122 */       GL15.glBufferData(target, (IntBuffer)data, usage);
/* 123 */     } else if (data instanceof FloatBuffer) {
/* 124 */       GL15.glBufferData(target, (FloatBuffer)data, usage);
/* 125 */     } else if (data instanceof DoubleBuffer) {
/* 126 */       GL15.glBufferData(target, (DoubleBuffer)data, usage);
/* 127 */     } else if (data instanceof ShortBuffer) {
/* 128 */       GL15.glBufferData(target, (ShortBuffer)data, usage);
/*     */     } 
/*     */   }
/*     */   public void glBufferSubData(int target, int offset, int size, Buffer data) {
/* 132 */     if (data == null)
/* 133 */       throw new GdxRuntimeException("Using null for the data not possible, blame LWJGL"); 
/* 134 */     if (data instanceof ByteBuffer) {
/* 135 */       GL15.glBufferSubData(target, offset, (ByteBuffer)data);
/* 136 */     } else if (data instanceof IntBuffer) {
/* 137 */       GL15.glBufferSubData(target, offset, (IntBuffer)data);
/* 138 */     } else if (data instanceof FloatBuffer) {
/* 139 */       GL15.glBufferSubData(target, offset, (FloatBuffer)data);
/* 140 */     } else if (data instanceof DoubleBuffer) {
/* 141 */       GL15.glBufferSubData(target, offset, (DoubleBuffer)data);
/* 142 */     } else if (data instanceof ShortBuffer) {
/* 143 */       GL15.glBufferSubData(target, offset, (ShortBuffer)data);
/*     */     } 
/*     */   }
/*     */   public int glCheckFramebufferStatus(int target) {
/* 147 */     return EXTFramebufferObject.glCheckFramebufferStatusEXT(target);
/*     */   }
/*     */   
/*     */   public void glClear(int mask) {
/* 151 */     GL11.glClear(mask);
/*     */   }
/*     */   
/*     */   public void glClearColor(float red, float green, float blue, float alpha) {
/* 155 */     GL11.glClearColor(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public void glClearDepthf(float depth) {
/* 159 */     GL11.glClearDepth(depth);
/*     */   }
/*     */   
/*     */   public void glClearStencil(int s) {
/* 163 */     GL11.glClearStencil(s);
/*     */   }
/*     */   
/*     */   public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
/* 167 */     GL11.glColorMask(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public void glCompileShader(int shader) {
/* 171 */     GL20.glCompileShader(shader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
/* 176 */     if (data instanceof ByteBuffer) {
/* 177 */       GL13.glCompressedTexImage2D(target, level, internalformat, width, height, border, (ByteBuffer)data);
/*     */     } else {
/* 179 */       throw new GdxRuntimeException("Can't use " + data.getClass().getName() + " with this method. Use ByteBuffer instead.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
/* 185 */     throw new GdxRuntimeException("not implemented");
/*     */   }
/*     */   
/*     */   public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
/* 189 */     GL11.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
/*     */   }
/*     */   
/*     */   public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
/* 193 */     GL11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
/*     */   }
/*     */   
/*     */   public int glCreateProgram() {
/* 197 */     return GL20.glCreateProgram();
/*     */   }
/*     */   
/*     */   public int glCreateShader(int type) {
/* 201 */     return GL20.glCreateShader(type);
/*     */   }
/*     */   
/*     */   public void glCullFace(int mode) {
/* 205 */     GL11.glCullFace(mode);
/*     */   }
/*     */   
/*     */   public void glDeleteBuffers(int n, IntBuffer buffers) {
/* 209 */     GL15.glDeleteBuffers(buffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteBuffer(int buffer) {
/* 214 */     GL15.glDeleteBuffers(buffer);
/*     */   }
/*     */   
/*     */   public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
/* 218 */     EXTFramebufferObject.glDeleteFramebuffersEXT(framebuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteFramebuffer(int framebuffer) {
/* 223 */     EXTFramebufferObject.glDeleteFramebuffersEXT(framebuffer);
/*     */   }
/*     */   
/*     */   public void glDeleteProgram(int program) {
/* 227 */     GL20.glDeleteProgram(program);
/*     */   }
/*     */   
/*     */   public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
/* 231 */     EXTFramebufferObject.glDeleteRenderbuffersEXT(renderbuffers);
/*     */   }
/*     */   
/*     */   public void glDeleteRenderbuffer(int renderbuffer) {
/* 235 */     EXTFramebufferObject.glDeleteRenderbuffersEXT(renderbuffer);
/*     */   }
/*     */   
/*     */   public void glDeleteShader(int shader) {
/* 239 */     GL20.glDeleteShader(shader);
/*     */   }
/*     */   
/*     */   public void glDeleteTextures(int n, IntBuffer textures) {
/* 243 */     GL11.glDeleteTextures(textures);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteTexture(int texture) {
/* 248 */     GL11.glDeleteTextures(texture);
/*     */   }
/*     */   
/*     */   public void glDepthFunc(int func) {
/* 252 */     GL11.glDepthFunc(func);
/*     */   }
/*     */   
/*     */   public void glDepthMask(boolean flag) {
/* 256 */     GL11.glDepthMask(flag);
/*     */   }
/*     */   
/*     */   public void glDepthRangef(float zNear, float zFar) {
/* 260 */     GL11.glDepthRange(zNear, zFar);
/*     */   }
/*     */   
/*     */   public void glDetachShader(int program, int shader) {
/* 264 */     GL20.glDetachShader(program, shader);
/*     */   }
/*     */   
/*     */   public void glDisable(int cap) {
/* 268 */     GL11.glDisable(cap);
/*     */   }
/*     */   
/*     */   public void glDisableVertexAttribArray(int index) {
/* 272 */     GL20.glDisableVertexAttribArray(index);
/*     */   }
/*     */   
/*     */   public void glDrawArrays(int mode, int first, int count) {
/* 276 */     GL11.glDrawArrays(mode, first, count);
/*     */   }
/*     */   
/*     */   public void glDrawElements(int mode, int count, int type, Buffer indices) {
/* 280 */     if (indices instanceof ShortBuffer && type == 5123) {
/* 281 */       GL11.glDrawElements(mode, (ShortBuffer)indices);
/* 282 */     } else if (indices instanceof ByteBuffer && type == 5123) {
/* 283 */       GL11.glDrawElements(mode, ((ByteBuffer)indices).asShortBuffer());
/* 284 */     } else if (indices instanceof ByteBuffer && type == 5121) {
/* 285 */       GL11.glDrawElements(mode, (ByteBuffer)indices);
/*     */     } else {
/* 287 */       throw new GdxRuntimeException("Can't use " + indices.getClass().getName() + " with this method. Use ShortBuffer or ByteBuffer instead. Blame LWJGL");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void glEnable(int cap) {
/* 292 */     GL11.glEnable(cap);
/*     */   }
/*     */   
/*     */   public void glEnableVertexAttribArray(int index) {
/* 296 */     GL20.glEnableVertexAttribArray(index);
/*     */   }
/*     */   
/*     */   public void glFinish() {
/* 300 */     GL11.glFinish();
/*     */   }
/*     */   
/*     */   public void glFlush() {
/* 304 */     GL11.glFlush();
/*     */   }
/*     */   
/*     */   public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
/* 308 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(target, attachment, renderbuffertarget, renderbuffer);
/*     */   }
/*     */   
/*     */   public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
/* 312 */     EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, textarget, texture, level);
/*     */   }
/*     */   
/*     */   public void glFrontFace(int mode) {
/* 316 */     GL11.glFrontFace(mode);
/*     */   }
/*     */   
/*     */   public void glGenBuffers(int n, IntBuffer buffers) {
/* 320 */     GL15.glGenBuffers(buffers);
/*     */   }
/*     */   
/*     */   public int glGenBuffer() {
/* 324 */     return GL15.glGenBuffers();
/*     */   }
/*     */   
/*     */   public void glGenFramebuffers(int n, IntBuffer framebuffers) {
/* 328 */     EXTFramebufferObject.glGenFramebuffersEXT(framebuffers);
/*     */   }
/*     */   
/*     */   public int glGenFramebuffer() {
/* 332 */     return EXTFramebufferObject.glGenFramebuffersEXT();
/*     */   }
/*     */   
/*     */   public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
/* 336 */     EXTFramebufferObject.glGenRenderbuffersEXT(renderbuffers);
/*     */   }
/*     */   
/*     */   public int glGenRenderbuffer() {
/* 340 */     return EXTFramebufferObject.glGenRenderbuffersEXT();
/*     */   }
/*     */   
/*     */   public void glGenTextures(int n, IntBuffer textures) {
/* 344 */     GL11.glGenTextures(textures);
/*     */   }
/*     */   
/*     */   public int glGenTexture() {
/* 348 */     return GL11.glGenTextures();
/*     */   }
/*     */   
/*     */   public void glGenerateMipmap(int target) {
/* 352 */     EXTFramebufferObject.glGenerateMipmapEXT(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public String glGetActiveAttrib(int program, int index, IntBuffer size, Buffer type) {
/* 357 */     IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
/* 358 */     String name = GL20.glGetActiveAttrib(program, index, 256, typeTmp);
/* 359 */     size.put(typeTmp.get(0));
/* 360 */     if (type instanceof IntBuffer) ((IntBuffer)type).put(typeTmp.get(1)); 
/* 361 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String glGetActiveUniform(int program, int index, IntBuffer size, Buffer type) {
/* 366 */     IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
/* 367 */     String name = GL20.glGetActiveUniform(program, index, 256, typeTmp);
/* 368 */     size.put(typeTmp.get(0));
/* 369 */     if (type instanceof IntBuffer) ((IntBuffer)type).put(typeTmp.get(1)); 
/* 370 */     return name;
/*     */   }
/*     */   
/*     */   public void glGetAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
/* 374 */     GL20.glGetAttachedShaders(program, (IntBuffer)count, shaders);
/*     */   }
/*     */   
/*     */   public int glGetAttribLocation(int program, String name) {
/* 378 */     return GL20.glGetAttribLocation(program, name);
/*     */   }
/*     */   
/*     */   public void glGetBooleanv(int pname, Buffer params) {
/* 382 */     GL11.glGetBoolean(pname, (ByteBuffer)params);
/*     */   }
/*     */   
/*     */   public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
/* 386 */     GL15.glGetBufferParameter(target, pname, params);
/*     */   }
/*     */   
/*     */   public int glGetError() {
/* 390 */     return GL11.glGetError();
/*     */   }
/*     */   
/*     */   public void glGetFloatv(int pname, FloatBuffer params) {
/* 394 */     GL11.glGetFloat(pname, params);
/*     */   }
/*     */   
/*     */   public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
/* 398 */     EXTFramebufferObject.glGetFramebufferAttachmentParameterEXT(target, attachment, pname, params);
/*     */   }
/*     */   
/*     */   public void glGetIntegerv(int pname, IntBuffer params) {
/* 402 */     GL11.glGetInteger(pname, params);
/*     */   }
/*     */   
/*     */   public String glGetProgramInfoLog(int program) {
/* 406 */     ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
/* 407 */     buffer.order(ByteOrder.nativeOrder());
/* 408 */     ByteBuffer tmp = ByteBuffer.allocateDirect(4);
/* 409 */     tmp.order(ByteOrder.nativeOrder());
/* 410 */     IntBuffer intBuffer = tmp.asIntBuffer();
/*     */     
/* 412 */     GL20.glGetProgramInfoLog(program, intBuffer, buffer);
/* 413 */     int numBytes = intBuffer.get(0);
/* 414 */     byte[] bytes = new byte[numBytes];
/* 415 */     buffer.get(bytes);
/* 416 */     return new String(bytes);
/*     */   }
/*     */   
/*     */   public void glGetProgramiv(int program, int pname, IntBuffer params) {
/* 420 */     GL20.glGetProgram(program, pname, params);
/*     */   }
/*     */   
/*     */   public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
/* 424 */     EXTFramebufferObject.glGetRenderbufferParameterEXT(target, pname, params);
/*     */   }
/*     */   
/*     */   public String glGetShaderInfoLog(int shader) {
/* 428 */     ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
/* 429 */     buffer.order(ByteOrder.nativeOrder());
/* 430 */     ByteBuffer tmp = ByteBuffer.allocateDirect(4);
/* 431 */     tmp.order(ByteOrder.nativeOrder());
/* 432 */     IntBuffer intBuffer = tmp.asIntBuffer();
/*     */     
/* 434 */     GL20.glGetShaderInfoLog(shader, intBuffer, buffer);
/* 435 */     int numBytes = intBuffer.get(0);
/* 436 */     byte[] bytes = new byte[numBytes];
/* 437 */     buffer.get(bytes);
/* 438 */     return new String(bytes);
/*     */   }
/*     */   
/*     */   public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
/* 442 */     throw new UnsupportedOperationException("unsupported, won't implement");
/*     */   }
/*     */   
/*     */   public void glGetShaderiv(int shader, int pname, IntBuffer params) {
/* 446 */     GL20.glGetShader(shader, pname, params);
/*     */   }
/*     */   
/*     */   public String glGetString(int name) {
/* 450 */     return GL11.glGetString(name);
/*     */   }
/*     */   
/*     */   public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
/* 454 */     GL11.glGetTexParameter(target, pname, params);
/*     */   }
/*     */   
/*     */   public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
/* 458 */     GL11.glGetTexParameter(target, pname, params);
/*     */   }
/*     */   
/*     */   public int glGetUniformLocation(int program, String name) {
/* 462 */     return GL20.glGetUniformLocation(program, name);
/*     */   }
/*     */   
/*     */   public void glGetUniformfv(int program, int location, FloatBuffer params) {
/* 466 */     GL20.glGetUniform(program, location, params);
/*     */   }
/*     */   
/*     */   public void glGetUniformiv(int program, int location, IntBuffer params) {
/* 470 */     GL20.glGetUniform(program, location, params);
/*     */   }
/*     */   
/*     */   public void glGetVertexAttribPointerv(int index, int pname, Buffer pointer) {
/* 474 */     throw new UnsupportedOperationException("unsupported, won't implement");
/*     */   }
/*     */   
/*     */   public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
/* 478 */     GL20.glGetVertexAttrib(index, pname, params);
/*     */   }
/*     */   
/*     */   public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
/* 482 */     GL20.glGetVertexAttrib(index, pname, params);
/*     */   }
/*     */   
/*     */   public void glHint(int target, int mode) {
/* 486 */     GL11.glHint(target, mode);
/*     */   }
/*     */   
/*     */   public boolean glIsBuffer(int buffer) {
/* 490 */     return GL15.glIsBuffer(buffer);
/*     */   }
/*     */   
/*     */   public boolean glIsEnabled(int cap) {
/* 494 */     return GL11.glIsEnabled(cap);
/*     */   }
/*     */   
/*     */   public boolean glIsFramebuffer(int framebuffer) {
/* 498 */     return EXTFramebufferObject.glIsFramebufferEXT(framebuffer);
/*     */   }
/*     */   
/*     */   public boolean glIsProgram(int program) {
/* 502 */     return GL20.glIsProgram(program);
/*     */   }
/*     */   
/*     */   public boolean glIsRenderbuffer(int renderbuffer) {
/* 506 */     return EXTFramebufferObject.glIsRenderbufferEXT(renderbuffer);
/*     */   }
/*     */   
/*     */   public boolean glIsShader(int shader) {
/* 510 */     return GL20.glIsShader(shader);
/*     */   }
/*     */   
/*     */   public boolean glIsTexture(int texture) {
/* 514 */     return GL11.glIsTexture(texture);
/*     */   }
/*     */   
/*     */   public void glLineWidth(float width) {
/* 518 */     GL11.glLineWidth(width);
/*     */   }
/*     */   
/*     */   public void glLinkProgram(int program) {
/* 522 */     GL20.glLinkProgram(program);
/*     */   }
/*     */   
/*     */   public void glPixelStorei(int pname, int param) {
/* 526 */     GL11.glPixelStorei(pname, param);
/*     */   }
/*     */   
/*     */   public void glPolygonOffset(float factor, float units) {
/* 530 */     GL11.glPolygonOffset(factor, units);
/*     */   }
/*     */   
/*     */   public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
/* 534 */     if (pixels instanceof ByteBuffer) {
/* 535 */       GL11.glReadPixels(x, y, width, height, format, type, (ByteBuffer)pixels);
/* 536 */     } else if (pixels instanceof ShortBuffer) {
/* 537 */       GL11.glReadPixels(x, y, width, height, format, type, (ShortBuffer)pixels);
/* 538 */     } else if (pixels instanceof IntBuffer) {
/* 539 */       GL11.glReadPixels(x, y, width, height, format, type, (IntBuffer)pixels);
/* 540 */     } else if (pixels instanceof FloatBuffer) {
/* 541 */       GL11.glReadPixels(x, y, width, height, format, type, (FloatBuffer)pixels);
/*     */     } else {
/* 543 */       throw new GdxRuntimeException("Can't use " + pixels.getClass().getName() + " with this method. Use ByteBuffer, ShortBuffer, IntBuffer or FloatBuffer instead. Blame LWJGL");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void glReleaseShaderCompiler() {}
/*     */ 
/*     */   
/*     */   public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
/* 552 */     EXTFramebufferObject.glRenderbufferStorageEXT(target, internalformat, width, height);
/*     */   }
/*     */   
/*     */   public void glSampleCoverage(float value, boolean invert) {
/* 556 */     GL13.glSampleCoverage(value, invert);
/*     */   }
/*     */   
/*     */   public void glScissor(int x, int y, int width, int height) {
/* 560 */     GL11.glScissor(x, y, width, height);
/*     */   }
/*     */   
/*     */   public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
/* 564 */     throw new UnsupportedOperationException("unsupported, won't implement");
/*     */   }
/*     */   
/*     */   public void glShaderSource(int shader, String string) {
/* 568 */     GL20.glShaderSource(shader, string);
/*     */   }
/*     */   
/*     */   public void glStencilFunc(int func, int ref, int mask) {
/* 572 */     GL11.glStencilFunc(func, ref, mask);
/*     */   }
/*     */   
/*     */   public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
/* 576 */     GL20.glStencilFuncSeparate(face, func, ref, mask);
/*     */   }
/*     */   
/*     */   public void glStencilMask(int mask) {
/* 580 */     GL11.glStencilMask(mask);
/*     */   }
/*     */   
/*     */   public void glStencilMaskSeparate(int face, int mask) {
/* 584 */     GL20.glStencilMaskSeparate(face, mask);
/*     */   }
/*     */   
/*     */   public void glStencilOp(int fail, int zfail, int zpass) {
/* 588 */     GL11.glStencilOp(fail, zfail, zpass);
/*     */   }
/*     */   
/*     */   public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
/* 592 */     GL20.glStencilOpSeparate(face, fail, zfail, zpass);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
/* 597 */     if (pixels == null) {
/* 598 */       GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ByteBuffer)null);
/* 599 */     } else if (pixels instanceof ByteBuffer) {
/* 600 */       GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ByteBuffer)pixels);
/* 601 */     } else if (pixels instanceof ShortBuffer) {
/* 602 */       GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ShortBuffer)pixels);
/* 603 */     } else if (pixels instanceof IntBuffer) {
/* 604 */       GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (IntBuffer)pixels);
/* 605 */     } else if (pixels instanceof FloatBuffer) {
/* 606 */       GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (FloatBuffer)pixels);
/* 607 */     } else if (pixels instanceof DoubleBuffer) {
/* 608 */       GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (DoubleBuffer)pixels);
/*     */     } else {
/* 610 */       throw new GdxRuntimeException("Can't use " + pixels.getClass().getName() + " with this method. Use ByteBuffer, ShortBuffer, IntBuffer, FloatBuffer or DoubleBuffer instead. Blame LWJGL");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void glTexParameterf(int target, int pname, float param) {
/* 615 */     GL11.glTexParameterf(target, pname, param);
/*     */   }
/*     */   
/*     */   public void glTexParameterfv(int target, int pname, FloatBuffer params) {
/* 619 */     GL11.glTexParameter(target, pname, params);
/*     */   }
/*     */   
/*     */   public void glTexParameteri(int target, int pname, int param) {
/* 623 */     GL11.glTexParameteri(target, pname, param);
/*     */   }
/*     */   
/*     */   public void glTexParameteriv(int target, int pname, IntBuffer params) {
/* 627 */     GL11.glTexParameter(target, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
/* 632 */     if (pixels instanceof ByteBuffer) {
/* 633 */       GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (ByteBuffer)pixels);
/* 634 */     } else if (pixels instanceof ShortBuffer) {
/* 635 */       GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (ShortBuffer)pixels);
/* 636 */     } else if (pixels instanceof IntBuffer) {
/* 637 */       GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (IntBuffer)pixels);
/* 638 */     } else if (pixels instanceof FloatBuffer) {
/* 639 */       GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (FloatBuffer)pixels);
/* 640 */     } else if (pixels instanceof DoubleBuffer) {
/* 641 */       GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (DoubleBuffer)pixels);
/*     */     } else {
/* 643 */       throw new GdxRuntimeException("Can't use " + pixels.getClass().getName() + " with this method. Use ByteBuffer, ShortBuffer, IntBuffer, FloatBuffer or DoubleBuffer instead. Blame LWJGL");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void glUniform1f(int location, float x) {
/* 648 */     GL20.glUniform1f(location, x);
/*     */   }
/*     */   
/*     */   public void glUniform1fv(int location, int count, FloatBuffer v) {
/* 652 */     GL20.glUniform1(location, v);
/*     */   }
/*     */   
/*     */   public void glUniform1fv(int location, int count, float[] v, int offset) {
/* 656 */     GL20.glUniform1(location, toFloatBuffer(v, offset, count));
/*     */   }
/*     */   
/*     */   public void glUniform1i(int location, int x) {
/* 660 */     GL20.glUniform1i(location, x);
/*     */   }
/*     */   
/*     */   public void glUniform1iv(int location, int count, IntBuffer v) {
/* 664 */     GL20.glUniform1(location, v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniform1iv(int location, int count, int[] v, int offset) {
/* 669 */     GL20.glUniform1(location, toIntBuffer(v, offset, count));
/*     */   }
/*     */   
/*     */   public void glUniform2f(int location, float x, float y) {
/* 673 */     GL20.glUniform2f(location, x, y);
/*     */   }
/*     */   
/*     */   public void glUniform2fv(int location, int count, FloatBuffer v) {
/* 677 */     GL20.glUniform2(location, v);
/*     */   }
/*     */   
/*     */   public void glUniform2fv(int location, int count, float[] v, int offset) {
/* 681 */     GL20.glUniform2(location, toFloatBuffer(v, offset, count << 1));
/*     */   }
/*     */   
/*     */   public void glUniform2i(int location, int x, int y) {
/* 685 */     GL20.glUniform2i(location, x, y);
/*     */   }
/*     */   
/*     */   public void glUniform2iv(int location, int count, IntBuffer v) {
/* 689 */     GL20.glUniform2(location, v);
/*     */   }
/*     */   
/*     */   public void glUniform2iv(int location, int count, int[] v, int offset) {
/* 693 */     GL20.glUniform2(location, toIntBuffer(v, offset, count << 1));
/*     */   }
/*     */   
/*     */   public void glUniform3f(int location, float x, float y, float z) {
/* 697 */     GL20.glUniform3f(location, x, y, z);
/*     */   }
/*     */   
/*     */   public void glUniform3fv(int location, int count, FloatBuffer v) {
/* 701 */     GL20.glUniform3(location, v);
/*     */   }
/*     */   
/*     */   public void glUniform3fv(int location, int count, float[] v, int offset) {
/* 705 */     GL20.glUniform3(location, toFloatBuffer(v, offset, count * 3));
/*     */   }
/*     */   
/*     */   public void glUniform3i(int location, int x, int y, int z) {
/* 709 */     GL20.glUniform3i(location, x, y, z);
/*     */   }
/*     */   
/*     */   public void glUniform3iv(int location, int count, IntBuffer v) {
/* 713 */     GL20.glUniform3(location, v);
/*     */   }
/*     */   
/*     */   public void glUniform3iv(int location, int count, int[] v, int offset) {
/* 717 */     GL20.glUniform3(location, toIntBuffer(v, offset, count * 3));
/*     */   }
/*     */   
/*     */   public void glUniform4f(int location, float x, float y, float z, float w) {
/* 721 */     GL20.glUniform4f(location, x, y, z, w);
/*     */   }
/*     */   
/*     */   public void glUniform4fv(int location, int count, FloatBuffer v) {
/* 725 */     GL20.glUniform4(location, v);
/*     */   }
/*     */   
/*     */   public void glUniform4fv(int location, int count, float[] v, int offset) {
/* 729 */     GL20.glUniform4(location, toFloatBuffer(v, offset, count << 2));
/*     */   }
/*     */   
/*     */   public void glUniform4i(int location, int x, int y, int z, int w) {
/* 733 */     GL20.glUniform4i(location, x, y, z, w);
/*     */   }
/*     */   
/*     */   public void glUniform4iv(int location, int count, IntBuffer v) {
/* 737 */     GL20.glUniform4(location, v);
/*     */   }
/*     */   
/*     */   public void glUniform4iv(int location, int count, int[] v, int offset) {
/* 741 */     GL20.glUniform4(location, toIntBuffer(v, offset, count << 2));
/*     */   }
/*     */   
/*     */   public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 745 */     GL20.glUniformMatrix2(location, transpose, value);
/*     */   }
/*     */   
/*     */   public void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 749 */     GL20.glUniformMatrix2(location, transpose, toFloatBuffer(value, offset, count << 2));
/*     */   }
/*     */   
/*     */   public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 753 */     GL20.glUniformMatrix3(location, transpose, value);
/*     */   }
/*     */   
/*     */   public void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 757 */     GL20.glUniformMatrix3(location, transpose, toFloatBuffer(value, offset, count * 9));
/*     */   }
/*     */   
/*     */   public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 761 */     GL20.glUniformMatrix4(location, transpose, value);
/*     */   }
/*     */   
/*     */   public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 765 */     GL20.glUniformMatrix4(location, transpose, toFloatBuffer(value, offset, count << 4));
/*     */   }
/*     */   
/*     */   public void glUseProgram(int program) {
/* 769 */     GL20.glUseProgram(program);
/*     */   }
/*     */   
/*     */   public void glValidateProgram(int program) {
/* 773 */     GL20.glValidateProgram(program);
/*     */   }
/*     */   
/*     */   public void glVertexAttrib1f(int indx, float x) {
/* 777 */     GL20.glVertexAttrib1f(indx, x);
/*     */   }
/*     */   
/*     */   public void glVertexAttrib1fv(int indx, FloatBuffer values) {
/* 781 */     GL20.glVertexAttrib1f(indx, values.get());
/*     */   }
/*     */   
/*     */   public void glVertexAttrib2f(int indx, float x, float y) {
/* 785 */     GL20.glVertexAttrib2f(indx, x, y);
/*     */   }
/*     */   
/*     */   public void glVertexAttrib2fv(int indx, FloatBuffer values) {
/* 789 */     GL20.glVertexAttrib2f(indx, values.get(), values.get());
/*     */   }
/*     */   
/*     */   public void glVertexAttrib3f(int indx, float x, float y, float z) {
/* 793 */     GL20.glVertexAttrib3f(indx, x, y, z);
/*     */   }
/*     */   
/*     */   public void glVertexAttrib3fv(int indx, FloatBuffer values) {
/* 797 */     GL20.glVertexAttrib3f(indx, values.get(), values.get(), values.get());
/*     */   }
/*     */   
/*     */   public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
/* 801 */     GL20.glVertexAttrib4f(indx, x, y, z, w);
/*     */   }
/*     */   
/*     */   public void glVertexAttrib4fv(int indx, FloatBuffer values) {
/* 805 */     GL20.glVertexAttrib4f(indx, values.get(), values.get(), values.get(), values.get());
/*     */   }
/*     */   
/*     */   public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer buffer) {
/* 809 */     if (buffer instanceof ByteBuffer) {
/* 810 */       if (type == 5120) {
/* 811 */         GL20.glVertexAttribPointer(indx, size, false, normalized, stride, (ByteBuffer)buffer);
/* 812 */       } else if (type == 5121) {
/* 813 */         GL20.glVertexAttribPointer(indx, size, true, normalized, stride, (ByteBuffer)buffer);
/* 814 */       } else if (type == 5122) {
/* 815 */         GL20.glVertexAttribPointer(indx, size, false, normalized, stride, ((ByteBuffer)buffer).asShortBuffer());
/* 816 */       } else if (type == 5123) {
/* 817 */         GL20.glVertexAttribPointer(indx, size, true, normalized, stride, ((ByteBuffer)buffer).asShortBuffer());
/* 818 */       } else if (type == 5126) {
/* 819 */         GL20.glVertexAttribPointer(indx, size, normalized, stride, ((ByteBuffer)buffer).asFloatBuffer());
/*     */       } else {
/* 821 */         throw new GdxRuntimeException("Can't use " + buffer
/*     */             
/* 823 */             .getClass().getName() + " with type " + type + " with this method. Use ByteBuffer and one of GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT, GL_UNSIGNED_SHORT or GL_FLOAT for type. Blame LWJGL");
/*     */       }
/*     */     
/*     */     }
/* 827 */     else if (buffer instanceof FloatBuffer) {
/* 828 */       if (type == 5126) {
/* 829 */         GL20.glVertexAttribPointer(indx, size, normalized, stride, (FloatBuffer)buffer);
/*     */       } else {
/* 831 */         throw new GdxRuntimeException("Can't use " + buffer.getClass().getName() + " with type " + type + " with this method.");
/*     */       } 
/*     */     } else {
/* 834 */       throw new GdxRuntimeException("Can't use " + buffer.getClass().getName() + " with this method. Use ByteBuffer instead. Blame LWJGL");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void glViewport(int x, int y, int width, int height) {
/* 839 */     GL11.glViewport(x, y, width, height);
/*     */   }
/*     */   
/*     */   public void glDrawElements(int mode, int count, int type, int indices) {
/* 843 */     GL11.glDrawElements(mode, count, type, indices);
/*     */   }
/*     */   
/*     */   public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
/* 847 */     GL20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglGL20.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */