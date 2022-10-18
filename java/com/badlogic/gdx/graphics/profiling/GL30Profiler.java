/*      */ package com.badlogic.gdx.graphics.profiling;
/*      */ 
/*      */ import com.badlogic.gdx.graphics.GL30;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.LongBuffer;
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
/*      */ public class GL30Profiler
/*      */   extends GLProfiler
/*      */   implements GL30
/*      */ {
/*      */   public final GL30 gl30;
/*      */   
/*      */   protected GL30Profiler(GL30 gl30) {
/*   34 */     this.gl30 = gl30;
/*      */   }
/*      */   
/*      */   private void check() {
/*   38 */     int error = this.gl30.glGetError();
/*   39 */     while (error != 0) {
/*   40 */       listener.onError(error);
/*   41 */       error = this.gl30.glGetError();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void glActiveTexture(int texture) {
/*   47 */     calls++;
/*   48 */     this.gl30.glActiveTexture(texture);
/*   49 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindTexture(int target, int texture) {
/*   54 */     textureBindings++;
/*   55 */     calls++;
/*   56 */     this.gl30.glBindTexture(target, texture);
/*   57 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendFunc(int sfactor, int dfactor) {
/*   62 */     calls++;
/*   63 */     this.gl30.glBlendFunc(sfactor, dfactor);
/*   64 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClear(int mask) {
/*   69 */     calls++;
/*   70 */     this.gl30.glClear(mask);
/*   71 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearColor(float red, float green, float blue, float alpha) {
/*   76 */     calls++;
/*   77 */     this.gl30.glClearColor(red, green, blue, alpha);
/*   78 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearDepthf(float depth) {
/*   83 */     calls++;
/*   84 */     this.gl30.glClearDepthf(depth);
/*   85 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearStencil(int s) {
/*   90 */     calls++;
/*   91 */     this.gl30.glClearStencil(s);
/*   92 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
/*   97 */     calls++;
/*   98 */     this.gl30.glColorMask(red, green, blue, alpha);
/*   99 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
/*  105 */     calls++;
/*  106 */     this.gl30.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
/*  107 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
/*  113 */     calls++;
/*  114 */     this.gl30.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
/*  115 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
/*  120 */     calls++;
/*  121 */     this.gl30.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
/*  122 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
/*  127 */     calls++;
/*  128 */     this.gl30.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
/*  129 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCullFace(int mode) {
/*  134 */     calls++;
/*  135 */     this.gl30.glCullFace(mode);
/*  136 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteTextures(int n, IntBuffer textures) {
/*  141 */     calls++;
/*  142 */     this.gl30.glDeleteTextures(n, textures);
/*  143 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteTexture(int texture) {
/*  148 */     calls++;
/*  149 */     this.gl30.glDeleteTexture(texture);
/*  150 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDepthFunc(int func) {
/*  155 */     calls++;
/*  156 */     this.gl30.glDepthFunc(func);
/*  157 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDepthMask(boolean flag) {
/*  162 */     calls++;
/*  163 */     this.gl30.glDepthMask(flag);
/*  164 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDepthRangef(float zNear, float zFar) {
/*  169 */     calls++;
/*  170 */     this.gl30.glDepthRangef(zNear, zFar);
/*  171 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDisable(int cap) {
/*  176 */     calls++;
/*  177 */     this.gl30.glDisable(cap);
/*  178 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawArrays(int mode, int first, int count) {
/*  183 */     vertexCount.put(count);
/*  184 */     drawCalls++;
/*  185 */     calls++;
/*  186 */     this.gl30.glDrawArrays(mode, first, count);
/*  187 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawElements(int mode, int count, int type, Buffer indices) {
/*  192 */     vertexCount.put(count);
/*  193 */     drawCalls++;
/*  194 */     calls++;
/*  195 */     this.gl30.glDrawElements(mode, count, type, indices);
/*  196 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glEnable(int cap) {
/*  201 */     calls++;
/*  202 */     this.gl30.glEnable(cap);
/*  203 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFinish() {
/*  208 */     calls++;
/*  209 */     this.gl30.glFinish();
/*  210 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFlush() {
/*  215 */     calls++;
/*  216 */     this.gl30.glFlush();
/*  217 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFrontFace(int mode) {
/*  222 */     calls++;
/*  223 */     this.gl30.glFrontFace(mode);
/*  224 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenTextures(int n, IntBuffer textures) {
/*  229 */     calls++;
/*  230 */     this.gl30.glGenTextures(n, textures);
/*  231 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenTexture() {
/*  236 */     calls++;
/*  237 */     int result = this.gl30.glGenTexture();
/*  238 */     check();
/*  239 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetError() {
/*  244 */     calls++;
/*      */     
/*  246 */     return this.gl30.glGetError();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetIntegerv(int pname, IntBuffer params) {
/*  251 */     calls++;
/*  252 */     this.gl30.glGetIntegerv(pname, params);
/*  253 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetString(int name) {
/*  258 */     calls++;
/*  259 */     String result = this.gl30.glGetString(name);
/*  260 */     check();
/*  261 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glHint(int target, int mode) {
/*  266 */     calls++;
/*  267 */     this.gl30.glHint(target, mode);
/*  268 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glLineWidth(float width) {
/*  273 */     calls++;
/*  274 */     this.gl30.glLineWidth(width);
/*  275 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glPixelStorei(int pname, int param) {
/*  280 */     calls++;
/*  281 */     this.gl30.glPixelStorei(pname, param);
/*  282 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glPolygonOffset(float factor, float units) {
/*  287 */     calls++;
/*  288 */     this.gl30.glPolygonOffset(factor, units);
/*  289 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
/*  294 */     calls++;
/*  295 */     this.gl30.glReadPixels(x, y, width, height, format, type, pixels);
/*  296 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glScissor(int x, int y, int width, int height) {
/*  301 */     calls++;
/*  302 */     this.gl30.glScissor(x, y, width, height);
/*  303 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilFunc(int func, int ref, int mask) {
/*  308 */     calls++;
/*  309 */     this.gl30.glStencilFunc(func, ref, mask);
/*  310 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilMask(int mask) {
/*  315 */     calls++;
/*  316 */     this.gl30.glStencilMask(mask);
/*  317 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilOp(int fail, int zfail, int zpass) {
/*  322 */     calls++;
/*  323 */     this.gl30.glStencilOp(fail, zfail, zpass);
/*  324 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
/*  330 */     calls++;
/*  331 */     this.gl30.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
/*  332 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameterf(int target, int pname, float param) {
/*  337 */     calls++;
/*  338 */     this.gl30.glTexParameterf(target, pname, param);
/*  339 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
/*  345 */     calls++;
/*  346 */     this.gl30.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
/*  347 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glViewport(int x, int y, int width, int height) {
/*  352 */     calls++;
/*  353 */     this.gl30.glViewport(x, y, width, height);
/*  354 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glAttachShader(int program, int shader) {
/*  359 */     calls++;
/*  360 */     this.gl30.glAttachShader(program, shader);
/*  361 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindAttribLocation(int program, int index, String name) {
/*  366 */     calls++;
/*  367 */     this.gl30.glBindAttribLocation(program, index, name);
/*  368 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindBuffer(int target, int buffer) {
/*  373 */     calls++;
/*  374 */     this.gl30.glBindBuffer(target, buffer);
/*  375 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindFramebuffer(int target, int framebuffer) {
/*  380 */     calls++;
/*  381 */     this.gl30.glBindFramebuffer(target, framebuffer);
/*  382 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindRenderbuffer(int target, int renderbuffer) {
/*  387 */     calls++;
/*  388 */     this.gl30.glBindRenderbuffer(target, renderbuffer);
/*  389 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendColor(float red, float green, float blue, float alpha) {
/*  394 */     calls++;
/*  395 */     this.gl30.glBlendColor(red, green, blue, alpha);
/*  396 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendEquation(int mode) {
/*  401 */     calls++;
/*  402 */     this.gl30.glBlendEquation(mode);
/*  403 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
/*  408 */     calls++;
/*  409 */     this.gl30.glBlendEquationSeparate(modeRGB, modeAlpha);
/*  410 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
/*  415 */     calls++;
/*  416 */     this.gl30.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
/*  417 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBufferData(int target, int size, Buffer data, int usage) {
/*  422 */     calls++;
/*  423 */     this.gl30.glBufferData(target, size, data, usage);
/*  424 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBufferSubData(int target, int offset, int size, Buffer data) {
/*  429 */     calls++;
/*  430 */     this.gl30.glBufferSubData(target, offset, size, data);
/*  431 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glCheckFramebufferStatus(int target) {
/*  436 */     calls++;
/*  437 */     int result = this.gl30.glCheckFramebufferStatus(target);
/*  438 */     check();
/*  439 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCompileShader(int shader) {
/*  444 */     calls++;
/*  445 */     this.gl30.glCompileShader(shader);
/*  446 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glCreateProgram() {
/*  451 */     calls++;
/*  452 */     int result = this.gl30.glCreateProgram();
/*  453 */     check();
/*  454 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public int glCreateShader(int type) {
/*  459 */     calls++;
/*  460 */     int result = this.gl30.glCreateShader(type);
/*  461 */     check();
/*  462 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteBuffer(int buffer) {
/*  467 */     calls++;
/*  468 */     this.gl30.glDeleteBuffer(buffer);
/*  469 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteBuffers(int n, IntBuffer buffers) {
/*  474 */     calls++;
/*  475 */     this.gl30.glDeleteBuffers(n, buffers);
/*  476 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteFramebuffer(int framebuffer) {
/*  481 */     calls++;
/*  482 */     this.gl30.glDeleteFramebuffer(framebuffer);
/*  483 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
/*  488 */     calls++;
/*  489 */     this.gl30.glDeleteFramebuffers(n, framebuffers);
/*  490 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteProgram(int program) {
/*  495 */     calls++;
/*  496 */     this.gl30.glDeleteProgram(program);
/*  497 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteRenderbuffer(int renderbuffer) {
/*  502 */     calls++;
/*  503 */     this.gl30.glDeleteRenderbuffer(renderbuffer);
/*  504 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
/*  509 */     calls++;
/*  510 */     this.gl30.glDeleteRenderbuffers(n, renderbuffers);
/*  511 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteShader(int shader) {
/*  516 */     calls++;
/*  517 */     this.gl30.glDeleteShader(shader);
/*  518 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDetachShader(int program, int shader) {
/*  523 */     calls++;
/*  524 */     this.gl30.glDetachShader(program, shader);
/*  525 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDisableVertexAttribArray(int index) {
/*  530 */     calls++;
/*  531 */     this.gl30.glDisableVertexAttribArray(index);
/*  532 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawElements(int mode, int count, int type, int indices) {
/*  537 */     vertexCount.put(count);
/*  538 */     drawCalls++;
/*  539 */     calls++;
/*  540 */     this.gl30.glDrawElements(mode, count, type, indices);
/*  541 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glEnableVertexAttribArray(int index) {
/*  546 */     calls++;
/*  547 */     this.gl30.glEnableVertexAttribArray(index);
/*  548 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
/*  553 */     calls++;
/*  554 */     this.gl30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
/*  555 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
/*  560 */     calls++;
/*  561 */     this.gl30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*  562 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenBuffer() {
/*  567 */     calls++;
/*  568 */     int result = this.gl30.glGenBuffer();
/*  569 */     check();
/*  570 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenBuffers(int n, IntBuffer buffers) {
/*  575 */     calls++;
/*  576 */     this.gl30.glGenBuffers(n, buffers);
/*  577 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenerateMipmap(int target) {
/*  582 */     calls++;
/*  583 */     this.gl30.glGenerateMipmap(target);
/*  584 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenFramebuffer() {
/*  589 */     calls++;
/*  590 */     int result = this.gl30.glGenFramebuffer();
/*  591 */     check();
/*  592 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenFramebuffers(int n, IntBuffer framebuffers) {
/*  597 */     calls++;
/*  598 */     this.gl30.glGenFramebuffers(n, framebuffers);
/*  599 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenRenderbuffer() {
/*  604 */     calls++;
/*  605 */     int result = this.gl30.glGenRenderbuffer();
/*  606 */     check();
/*  607 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
/*  612 */     calls++;
/*  613 */     this.gl30.glGenRenderbuffers(n, renderbuffers);
/*  614 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetActiveAttrib(int program, int index, IntBuffer size, Buffer type) {
/*  619 */     calls++;
/*  620 */     String result = this.gl30.glGetActiveAttrib(program, index, size, type);
/*  621 */     check();
/*  622 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetActiveUniform(int program, int index, IntBuffer size, Buffer type) {
/*  627 */     calls++;
/*  628 */     String result = this.gl30.glGetActiveUniform(program, index, size, type);
/*  629 */     check();
/*  630 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
/*  635 */     calls++;
/*  636 */     this.gl30.glGetAttachedShaders(program, maxcount, count, shaders);
/*  637 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetAttribLocation(int program, String name) {
/*  642 */     calls++;
/*  643 */     int result = this.gl30.glGetAttribLocation(program, name);
/*  644 */     check();
/*  645 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetBooleanv(int pname, Buffer params) {
/*  650 */     calls++;
/*  651 */     this.gl30.glGetBooleanv(pname, params);
/*  652 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
/*  657 */     calls++;
/*  658 */     this.gl30.glGetBufferParameteriv(target, pname, params);
/*  659 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetFloatv(int pname, FloatBuffer params) {
/*  664 */     calls++;
/*  665 */     this.gl30.glGetFloatv(pname, params);
/*  666 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
/*  671 */     calls++;
/*  672 */     this.gl30.glGetFramebufferAttachmentParameteriv(target, attachment, pname, params);
/*  673 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetProgramiv(int program, int pname, IntBuffer params) {
/*  678 */     calls++;
/*  679 */     this.gl30.glGetProgramiv(program, pname, params);
/*  680 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetProgramInfoLog(int program) {
/*  685 */     calls++;
/*  686 */     String result = this.gl30.glGetProgramInfoLog(program);
/*  687 */     check();
/*  688 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
/*  693 */     calls++;
/*  694 */     this.gl30.glGetRenderbufferParameteriv(target, pname, params);
/*  695 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetShaderiv(int shader, int pname, IntBuffer params) {
/*  700 */     calls++;
/*  701 */     this.gl30.glGetShaderiv(shader, pname, params);
/*  702 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetShaderInfoLog(int shader) {
/*  707 */     calls++;
/*  708 */     String result = this.gl30.glGetShaderInfoLog(shader);
/*  709 */     check();
/*  710 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
/*  715 */     calls++;
/*  716 */     this.gl30.glGetShaderPrecisionFormat(shadertype, precisiontype, range, precision);
/*  717 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
/*  722 */     calls++;
/*  723 */     this.gl30.glGetTexParameterfv(target, pname, params);
/*  724 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
/*  729 */     calls++;
/*  730 */     this.gl30.glGetTexParameteriv(target, pname, params);
/*  731 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetUniformfv(int program, int location, FloatBuffer params) {
/*  736 */     calls++;
/*  737 */     this.gl30.glGetUniformfv(program, location, params);
/*  738 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetUniformiv(int program, int location, IntBuffer params) {
/*  743 */     calls++;
/*  744 */     this.gl30.glGetUniformiv(program, location, params);
/*  745 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetUniformLocation(int program, String name) {
/*  750 */     calls++;
/*  751 */     int result = this.gl30.glGetUniformLocation(program, name);
/*  752 */     check();
/*  753 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
/*  758 */     calls++;
/*  759 */     this.gl30.glGetVertexAttribfv(index, pname, params);
/*  760 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
/*  765 */     calls++;
/*  766 */     this.gl30.glGetVertexAttribiv(index, pname, params);
/*  767 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribPointerv(int index, int pname, Buffer pointer) {
/*  772 */     calls++;
/*  773 */     this.gl30.glGetVertexAttribPointerv(index, pname, pointer);
/*  774 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsBuffer(int buffer) {
/*  779 */     calls++;
/*  780 */     boolean result = this.gl30.glIsBuffer(buffer);
/*  781 */     check();
/*  782 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsEnabled(int cap) {
/*  787 */     calls++;
/*  788 */     boolean result = this.gl30.glIsEnabled(cap);
/*  789 */     check();
/*  790 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsFramebuffer(int framebuffer) {
/*  795 */     calls++;
/*  796 */     boolean result = this.gl30.glIsFramebuffer(framebuffer);
/*  797 */     check();
/*  798 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsProgram(int program) {
/*  803 */     calls++;
/*  804 */     boolean result = this.gl30.glIsProgram(program);
/*  805 */     check();
/*  806 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsRenderbuffer(int renderbuffer) {
/*  811 */     calls++;
/*  812 */     boolean result = this.gl30.glIsRenderbuffer(renderbuffer);
/*  813 */     check();
/*  814 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsShader(int shader) {
/*  819 */     calls++;
/*  820 */     boolean result = this.gl30.glIsShader(shader);
/*  821 */     check();
/*  822 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsTexture(int texture) {
/*  827 */     calls++;
/*  828 */     boolean result = this.gl30.glIsTexture(texture);
/*  829 */     check();
/*  830 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glLinkProgram(int program) {
/*  835 */     calls++;
/*  836 */     this.gl30.glLinkProgram(program);
/*  837 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glReleaseShaderCompiler() {
/*  842 */     calls++;
/*  843 */     this.gl30.glReleaseShaderCompiler();
/*  844 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
/*  849 */     calls++;
/*  850 */     this.gl30.glRenderbufferStorage(target, internalformat, width, height);
/*  851 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glSampleCoverage(float value, boolean invert) {
/*  856 */     calls++;
/*  857 */     this.gl30.glSampleCoverage(value, invert);
/*  858 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
/*  863 */     calls++;
/*  864 */     this.gl30.glShaderBinary(n, shaders, binaryformat, binary, length);
/*  865 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glShaderSource(int shader, String string) {
/*  870 */     calls++;
/*  871 */     this.gl30.glShaderSource(shader, string);
/*  872 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
/*  877 */     calls++;
/*  878 */     this.gl30.glStencilFuncSeparate(face, func, ref, mask);
/*  879 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilMaskSeparate(int face, int mask) {
/*  884 */     calls++;
/*  885 */     this.gl30.glStencilMaskSeparate(face, mask);
/*  886 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
/*  891 */     calls++;
/*  892 */     this.gl30.glStencilOpSeparate(face, fail, zfail, zpass);
/*  893 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameterfv(int target, int pname, FloatBuffer params) {
/*  898 */     calls++;
/*  899 */     this.gl30.glTexParameterfv(target, pname, params);
/*  900 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameteri(int target, int pname, int param) {
/*  905 */     calls++;
/*  906 */     this.gl30.glTexParameteri(target, pname, param);
/*  907 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameteriv(int target, int pname, IntBuffer params) {
/*  912 */     calls++;
/*  913 */     this.gl30.glTexParameteriv(target, pname, params);
/*  914 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1f(int location, float x) {
/*  919 */     calls++;
/*  920 */     this.gl30.glUniform1f(location, x);
/*  921 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1fv(int location, int count, FloatBuffer v) {
/*  926 */     calls++;
/*  927 */     this.gl30.glUniform1fv(location, count, v);
/*  928 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1fv(int location, int count, float[] v, int offset) {
/*  933 */     calls++;
/*  934 */     this.gl30.glUniform1fv(location, count, v, offset);
/*  935 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1i(int location, int x) {
/*  940 */     calls++;
/*  941 */     this.gl30.glUniform1i(location, x);
/*  942 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1iv(int location, int count, IntBuffer v) {
/*  947 */     calls++;
/*  948 */     this.gl30.glUniform1iv(location, count, v);
/*  949 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1iv(int location, int count, int[] v, int offset) {
/*  954 */     calls++;
/*  955 */     this.gl30.glUniform1iv(location, count, v, offset);
/*  956 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2f(int location, float x, float y) {
/*  961 */     calls++;
/*  962 */     this.gl30.glUniform2f(location, x, y);
/*  963 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2fv(int location, int count, FloatBuffer v) {
/*  968 */     calls++;
/*  969 */     this.gl30.glUniform2fv(location, count, v);
/*  970 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2fv(int location, int count, float[] v, int offset) {
/*  975 */     calls++;
/*  976 */     this.gl30.glUniform2fv(location, count, v, offset);
/*  977 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2i(int location, int x, int y) {
/*  982 */     calls++;
/*  983 */     this.gl30.glUniform2i(location, x, y);
/*  984 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2iv(int location, int count, IntBuffer v) {
/*  989 */     calls++;
/*  990 */     this.gl30.glUniform2iv(location, count, v);
/*  991 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2iv(int location, int count, int[] v, int offset) {
/*  996 */     calls++;
/*  997 */     this.gl30.glUniform2iv(location, count, v, offset);
/*  998 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3f(int location, float x, float y, float z) {
/* 1003 */     calls++;
/* 1004 */     this.gl30.glUniform3f(location, x, y, z);
/* 1005 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3fv(int location, int count, FloatBuffer v) {
/* 1010 */     calls++;
/* 1011 */     this.gl30.glUniform3fv(location, count, v);
/* 1012 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3fv(int location, int count, float[] v, int offset) {
/* 1017 */     calls++;
/* 1018 */     this.gl30.glUniform3fv(location, count, v, offset);
/* 1019 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3i(int location, int x, int y, int z) {
/* 1024 */     calls++;
/* 1025 */     this.gl30.glUniform3i(location, x, y, z);
/* 1026 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3iv(int location, int count, IntBuffer v) {
/* 1031 */     calls++;
/* 1032 */     this.gl30.glUniform3iv(location, count, v);
/* 1033 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3iv(int location, int count, int[] v, int offset) {
/* 1038 */     calls++;
/* 1039 */     this.gl30.glUniform3iv(location, count, v, offset);
/* 1040 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4f(int location, float x, float y, float z, float w) {
/* 1045 */     calls++;
/* 1046 */     this.gl30.glUniform4f(location, x, y, z, w);
/* 1047 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4fv(int location, int count, FloatBuffer v) {
/* 1052 */     calls++;
/* 1053 */     this.gl30.glUniform4fv(location, count, v);
/* 1054 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4fv(int location, int count, float[] v, int offset) {
/* 1059 */     calls++;
/* 1060 */     this.gl30.glUniform4fv(location, count, v, offset);
/* 1061 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4i(int location, int x, int y, int z, int w) {
/* 1066 */     calls++;
/* 1067 */     this.gl30.glUniform4i(location, x, y, z, w);
/* 1068 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4iv(int location, int count, IntBuffer v) {
/* 1073 */     calls++;
/* 1074 */     this.gl30.glUniform4iv(location, count, v);
/* 1075 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4iv(int location, int count, int[] v, int offset) {
/* 1080 */     calls++;
/* 1081 */     this.gl30.glUniform4iv(location, count, v, offset);
/* 1082 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1087 */     calls++;
/* 1088 */     this.gl30.glUniformMatrix2fv(location, count, transpose, value);
/* 1089 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 1094 */     calls++;
/* 1095 */     this.gl30.glUniformMatrix2fv(location, count, transpose, value, offset);
/* 1096 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1101 */     calls++;
/* 1102 */     this.gl30.glUniformMatrix3fv(location, count, transpose, value);
/* 1103 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 1108 */     calls++;
/* 1109 */     this.gl30.glUniformMatrix3fv(location, count, transpose, value, offset);
/* 1110 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1115 */     calls++;
/* 1116 */     this.gl30.glUniformMatrix4fv(location, count, transpose, value);
/* 1117 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 1122 */     calls++;
/* 1123 */     this.gl30.glUniformMatrix4fv(location, count, transpose, value, offset);
/* 1124 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUseProgram(int program) {
/* 1129 */     shaderSwitches++;
/* 1130 */     calls++;
/* 1131 */     this.gl30.glUseProgram(program);
/* 1132 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glValidateProgram(int program) {
/* 1137 */     calls++;
/* 1138 */     this.gl30.glValidateProgram(program);
/* 1139 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib1f(int indx, float x) {
/* 1144 */     calls++;
/* 1145 */     this.gl30.glVertexAttrib1f(indx, x);
/* 1146 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib1fv(int indx, FloatBuffer values) {
/* 1151 */     calls++;
/* 1152 */     this.gl30.glVertexAttrib1fv(indx, values);
/* 1153 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib2f(int indx, float x, float y) {
/* 1158 */     calls++;
/* 1159 */     this.gl30.glVertexAttrib2f(indx, x, y);
/* 1160 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib2fv(int indx, FloatBuffer values) {
/* 1165 */     calls++;
/* 1166 */     this.gl30.glVertexAttrib2fv(indx, values);
/* 1167 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib3f(int indx, float x, float y, float z) {
/* 1172 */     calls++;
/* 1173 */     this.gl30.glVertexAttrib3f(indx, x, y, z);
/* 1174 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib3fv(int indx, FloatBuffer values) {
/* 1179 */     calls++;
/* 1180 */     this.gl30.glVertexAttrib3fv(indx, values);
/* 1181 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
/* 1186 */     calls++;
/* 1187 */     this.gl30.glVertexAttrib4f(indx, x, y, z, w);
/* 1188 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib4fv(int indx, FloatBuffer values) {
/* 1193 */     calls++;
/* 1194 */     this.gl30.glVertexAttrib4fv(indx, values);
/* 1195 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
/* 1200 */     calls++;
/* 1201 */     this.gl30.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
/* 1202 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
/* 1207 */     calls++;
/* 1208 */     this.gl30.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
/* 1209 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void glReadBuffer(int mode) {
/* 1216 */     calls++;
/* 1217 */     this.gl30.glReadBuffer(mode);
/* 1218 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawRangeElements(int mode, int start, int end, int count, int type, Buffer indices) {
/* 1223 */     vertexCount.put(count);
/* 1224 */     drawCalls++;
/* 1225 */     calls++;
/* 1226 */     this.gl30.glDrawRangeElements(mode, start, end, count, type, indices);
/* 1227 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawRangeElements(int mode, int start, int end, int count, int type, int offset) {
/* 1232 */     vertexCount.put(count);
/* 1233 */     drawCalls++;
/* 1234 */     calls++;
/* 1235 */     this.gl30.glDrawRangeElements(mode, start, end, count, type, offset);
/* 1236 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, Buffer pixels) {
/* 1242 */     calls++;
/* 1243 */     this.gl30.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
/* 1244 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, int offset) {
/* 1250 */     calls++;
/* 1251 */     this.gl30.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, offset);
/* 1252 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, Buffer pixels) {
/* 1258 */     calls++;
/* 1259 */     this.gl30.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
/* 1260 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, int offset) {
/* 1266 */     calls++;
/* 1267 */     this.gl30.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, offset);
/* 1268 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
/* 1274 */     calls++;
/* 1275 */     this.gl30.glCopyTexSubImage3D(target, level, xoffset, yoffset, zoffset, x, y, width, height);
/* 1276 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenQueries(int n, int[] ids, int offset) {
/* 1281 */     calls++;
/* 1282 */     this.gl30.glGenQueries(n, ids, offset);
/* 1283 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenQueries(int n, IntBuffer ids) {
/* 1288 */     calls++;
/* 1289 */     this.gl30.glGenQueries(n, ids);
/* 1290 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteQueries(int n, int[] ids, int offset) {
/* 1295 */     calls++;
/* 1296 */     this.gl30.glDeleteQueries(n, ids, offset);
/* 1297 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteQueries(int n, IntBuffer ids) {
/* 1302 */     calls++;
/* 1303 */     this.gl30.glDeleteQueries(n, ids);
/* 1304 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsQuery(int id) {
/* 1309 */     calls++;
/* 1310 */     boolean result = this.gl30.glIsQuery(id);
/* 1311 */     check();
/* 1312 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBeginQuery(int target, int id) {
/* 1317 */     calls++;
/* 1318 */     this.gl30.glBeginQuery(target, id);
/* 1319 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glEndQuery(int target) {
/* 1324 */     calls++;
/* 1325 */     this.gl30.glEndQuery(target);
/* 1326 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetQueryiv(int target, int pname, IntBuffer params) {
/* 1331 */     calls++;
/* 1332 */     this.gl30.glGetQueryiv(target, pname, params);
/* 1333 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetQueryObjectuiv(int id, int pname, IntBuffer params) {
/* 1338 */     calls++;
/* 1339 */     this.gl30.glGetQueryObjectuiv(id, pname, params);
/* 1340 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glUnmapBuffer(int target) {
/* 1345 */     calls++;
/* 1346 */     boolean result = this.gl30.glUnmapBuffer(target);
/* 1347 */     check();
/* 1348 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public Buffer glGetBufferPointerv(int target, int pname) {
/* 1353 */     calls++;
/* 1354 */     Buffer result = this.gl30.glGetBufferPointerv(target, pname);
/* 1355 */     check();
/* 1356 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawBuffers(int n, IntBuffer bufs) {
/* 1361 */     drawCalls++;
/* 1362 */     calls++;
/* 1363 */     this.gl30.glDrawBuffers(n, bufs);
/* 1364 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix2x3fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1369 */     calls++;
/* 1370 */     this.gl30.glUniformMatrix2x3fv(location, count, transpose, value);
/* 1371 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix3x2fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1376 */     calls++;
/* 1377 */     this.gl30.glUniformMatrix3x2fv(location, count, transpose, value);
/* 1378 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix2x4fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1383 */     calls++;
/* 1384 */     this.gl30.glUniformMatrix2x4fv(location, count, transpose, value);
/* 1385 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix4x2fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1390 */     calls++;
/* 1391 */     this.gl30.glUniformMatrix4x2fv(location, count, transpose, value);
/* 1392 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix3x4fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1397 */     calls++;
/* 1398 */     this.gl30.glUniformMatrix3x4fv(location, count, transpose, value);
/* 1399 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix4x3fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1404 */     calls++;
/* 1405 */     this.gl30.glUniformMatrix4x3fv(location, count, transpose, value);
/* 1406 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 1412 */     calls++;
/* 1413 */     this.gl30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/* 1414 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
/* 1419 */     calls++;
/* 1420 */     this.gl30.glRenderbufferStorageMultisample(target, samples, internalformat, width, height);
/* 1421 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
/* 1426 */     calls++;
/* 1427 */     this.gl30.glFramebufferTextureLayer(target, attachment, texture, level, layer);
/* 1428 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFlushMappedBufferRange(int target, int offset, int length) {
/* 1433 */     calls++;
/* 1434 */     this.gl30.glFlushMappedBufferRange(target, offset, length);
/* 1435 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindVertexArray(int array) {
/* 1440 */     calls++;
/* 1441 */     this.gl30.glBindVertexArray(array);
/* 1442 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteVertexArrays(int n, int[] arrays, int offset) {
/* 1447 */     calls++;
/* 1448 */     this.gl30.glDeleteVertexArrays(n, arrays, offset);
/* 1449 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteVertexArrays(int n, IntBuffer arrays) {
/* 1454 */     calls++;
/* 1455 */     this.gl30.glDeleteVertexArrays(n, arrays);
/* 1456 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenVertexArrays(int n, int[] arrays, int offset) {
/* 1461 */     calls++;
/* 1462 */     this.gl30.glGenVertexArrays(n, arrays, offset);
/* 1463 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenVertexArrays(int n, IntBuffer arrays) {
/* 1468 */     calls++;
/* 1469 */     this.gl30.glGenVertexArrays(n, arrays);
/* 1470 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsVertexArray(int array) {
/* 1475 */     calls++;
/* 1476 */     boolean result = this.gl30.glIsVertexArray(array);
/* 1477 */     check();
/* 1478 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBeginTransformFeedback(int primitiveMode) {
/* 1483 */     calls++;
/* 1484 */     this.gl30.glBeginTransformFeedback(primitiveMode);
/* 1485 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glEndTransformFeedback() {
/* 1490 */     calls++;
/* 1491 */     this.gl30.glEndTransformFeedback();
/* 1492 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindBufferRange(int target, int index, int buffer, int offset, int size) {
/* 1497 */     calls++;
/* 1498 */     this.gl30.glBindBufferRange(target, index, buffer, offset, size);
/* 1499 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindBufferBase(int target, int index, int buffer) {
/* 1504 */     calls++;
/* 1505 */     this.gl30.glBindBufferBase(target, index, buffer);
/* 1506 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTransformFeedbackVaryings(int program, String[] varyings, int bufferMode) {
/* 1511 */     calls++;
/* 1512 */     this.gl30.glTransformFeedbackVaryings(program, varyings, bufferMode);
/* 1513 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribIPointer(int index, int size, int type, int stride, int offset) {
/* 1518 */     calls++;
/* 1519 */     this.gl30.glVertexAttribIPointer(index, size, type, stride, offset);
/* 1520 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribIiv(int index, int pname, IntBuffer params) {
/* 1525 */     calls++;
/* 1526 */     this.gl30.glGetVertexAttribIiv(index, pname, params);
/* 1527 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribIuiv(int index, int pname, IntBuffer params) {
/* 1532 */     calls++;
/* 1533 */     this.gl30.glGetVertexAttribIuiv(index, pname, params);
/* 1534 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribI4i(int index, int x, int y, int z, int w) {
/* 1539 */     calls++;
/* 1540 */     this.gl30.glVertexAttribI4i(index, x, y, z, w);
/* 1541 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribI4ui(int index, int x, int y, int z, int w) {
/* 1546 */     calls++;
/* 1547 */     this.gl30.glVertexAttribI4ui(index, x, y, z, w);
/* 1548 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetUniformuiv(int program, int location, IntBuffer params) {
/* 1553 */     calls++;
/* 1554 */     this.gl30.glGetUniformuiv(program, location, params);
/* 1555 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetFragDataLocation(int program, String name) {
/* 1560 */     calls++;
/* 1561 */     int result = this.gl30.glGetFragDataLocation(program, name);
/* 1562 */     check();
/* 1563 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1uiv(int location, int count, IntBuffer value) {
/* 1568 */     calls++;
/* 1569 */     this.gl30.glUniform1uiv(location, count, value);
/* 1570 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3uiv(int location, int count, IntBuffer value) {
/* 1575 */     calls++;
/* 1576 */     this.gl30.glUniform3uiv(location, count, value);
/* 1577 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4uiv(int location, int count, IntBuffer value) {
/* 1582 */     calls++;
/* 1583 */     this.gl30.glUniform4uiv(location, count, value);
/* 1584 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearBufferiv(int buffer, int drawbuffer, IntBuffer value) {
/* 1589 */     calls++;
/* 1590 */     this.gl30.glClearBufferiv(buffer, drawbuffer, value);
/* 1591 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearBufferuiv(int buffer, int drawbuffer, IntBuffer value) {
/* 1596 */     calls++;
/* 1597 */     this.gl30.glClearBufferuiv(buffer, drawbuffer, value);
/* 1598 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearBufferfv(int buffer, int drawbuffer, FloatBuffer value) {
/* 1603 */     calls++;
/* 1604 */     this.gl30.glClearBufferfv(buffer, drawbuffer, value);
/* 1605 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearBufferfi(int buffer, int drawbuffer, float depth, int stencil) {
/* 1610 */     calls++;
/* 1611 */     this.gl30.glClearBufferfi(buffer, drawbuffer, depth, stencil);
/* 1612 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetStringi(int name, int index) {
/* 1617 */     calls++;
/* 1618 */     String result = this.gl30.glGetStringi(name, index);
/* 1619 */     check();
/* 1620 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCopyBufferSubData(int readTarget, int writeTarget, int readOffset, int writeOffset, int size) {
/* 1625 */     calls++;
/* 1626 */     this.gl30.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
/* 1627 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetUniformIndices(int program, String[] uniformNames, IntBuffer uniformIndices) {
/* 1632 */     calls++;
/* 1633 */     this.gl30.glGetUniformIndices(program, uniformNames, uniformIndices);
/* 1634 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetActiveUniformsiv(int program, int uniformCount, IntBuffer uniformIndices, int pname, IntBuffer params) {
/* 1639 */     calls++;
/* 1640 */     this.gl30.glGetActiveUniformsiv(program, uniformCount, uniformIndices, pname, params);
/* 1641 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetUniformBlockIndex(int program, String uniformBlockName) {
/* 1646 */     calls++;
/* 1647 */     int result = this.gl30.glGetUniformBlockIndex(program, uniformBlockName);
/* 1648 */     check();
/* 1649 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, IntBuffer params) {
/* 1654 */     calls++;
/* 1655 */     this.gl30.glGetActiveUniformBlockiv(program, uniformBlockIndex, pname, params);
/* 1656 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetActiveUniformBlockName(int program, int uniformBlockIndex, Buffer length, Buffer uniformBlockName) {
/* 1661 */     calls++;
/* 1662 */     this.gl30.glGetActiveUniformBlockName(program, uniformBlockIndex, length, uniformBlockName);
/* 1663 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetActiveUniformBlockName(int program, int uniformBlockIndex) {
/* 1668 */     calls++;
/* 1669 */     String result = this.gl30.glGetActiveUniformBlockName(program, uniformBlockIndex);
/* 1670 */     check();
/* 1671 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
/* 1676 */     calls++;
/* 1677 */     this.gl30.glUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding);
/* 1678 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawArraysInstanced(int mode, int first, int count, int instanceCount) {
/* 1683 */     vertexCount.put(count);
/* 1684 */     drawCalls++;
/* 1685 */     calls++;
/* 1686 */     this.gl30.glDrawArraysInstanced(mode, first, count, instanceCount);
/* 1687 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawElementsInstanced(int mode, int count, int type, int indicesOffset, int instanceCount) {
/* 1692 */     vertexCount.put(count);
/* 1693 */     drawCalls++;
/* 1694 */     calls++;
/* 1695 */     this.gl30.glDrawElementsInstanced(mode, count, type, indicesOffset, instanceCount);
/* 1696 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetInteger64v(int pname, LongBuffer params) {
/* 1701 */     calls++;
/* 1702 */     this.gl30.glGetInteger64v(pname, params);
/* 1703 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetBufferParameteri64v(int target, int pname, LongBuffer params) {
/* 1708 */     calls++;
/* 1709 */     this.gl30.glGetBufferParameteri64v(target, pname, params);
/* 1710 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenSamplers(int count, int[] samplers, int offset) {
/* 1715 */     calls++;
/* 1716 */     this.gl30.glGenSamplers(count, samplers, offset);
/* 1717 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenSamplers(int count, IntBuffer samplers) {
/* 1722 */     calls++;
/* 1723 */     this.gl30.glGenSamplers(count, samplers);
/* 1724 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteSamplers(int count, int[] samplers, int offset) {
/* 1729 */     calls++;
/* 1730 */     this.gl30.glDeleteSamplers(count, samplers, offset);
/* 1731 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteSamplers(int count, IntBuffer samplers) {
/* 1736 */     calls++;
/* 1737 */     this.gl30.glDeleteSamplers(count, samplers);
/* 1738 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsSampler(int sampler) {
/* 1743 */     calls++;
/* 1744 */     boolean result = this.gl30.glIsSampler(sampler);
/* 1745 */     check();
/* 1746 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindSampler(int unit, int sampler) {
/* 1751 */     calls++;
/* 1752 */     this.gl30.glBindSampler(unit, sampler);
/* 1753 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glSamplerParameteri(int sampler, int pname, int param) {
/* 1758 */     calls++;
/* 1759 */     this.gl30.glSamplerParameteri(sampler, pname, param);
/* 1760 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glSamplerParameteriv(int sampler, int pname, IntBuffer param) {
/* 1765 */     calls++;
/* 1766 */     this.gl30.glSamplerParameteriv(sampler, pname, param);
/* 1767 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glSamplerParameterf(int sampler, int pname, float param) {
/* 1772 */     calls++;
/* 1773 */     this.gl30.glSamplerParameterf(sampler, pname, param);
/* 1774 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glSamplerParameterfv(int sampler, int pname, FloatBuffer param) {
/* 1779 */     calls++;
/* 1780 */     this.gl30.glSamplerParameterfv(sampler, pname, param);
/* 1781 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetSamplerParameteriv(int sampler, int pname, IntBuffer params) {
/* 1786 */     calls++;
/* 1787 */     this.gl30.glGetSamplerParameteriv(sampler, pname, params);
/* 1788 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetSamplerParameterfv(int sampler, int pname, FloatBuffer params) {
/* 1793 */     calls++;
/* 1794 */     this.gl30.glGetSamplerParameterfv(sampler, pname, params);
/* 1795 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribDivisor(int index, int divisor) {
/* 1800 */     calls++;
/* 1801 */     this.gl30.glVertexAttribDivisor(index, divisor);
/* 1802 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindTransformFeedback(int target, int id) {
/* 1807 */     calls++;
/* 1808 */     this.gl30.glBindTransformFeedback(target, id);
/* 1809 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteTransformFeedbacks(int n, int[] ids, int offset) {
/* 1814 */     calls++;
/* 1815 */     this.gl30.glDeleteTransformFeedbacks(n, ids, offset);
/* 1816 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteTransformFeedbacks(int n, IntBuffer ids) {
/* 1821 */     calls++;
/* 1822 */     this.gl30.glDeleteTransformFeedbacks(n, ids);
/* 1823 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenTransformFeedbacks(int n, int[] ids, int offset) {
/* 1828 */     calls++;
/* 1829 */     this.gl30.glGenTransformFeedbacks(n, ids, offset);
/* 1830 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenTransformFeedbacks(int n, IntBuffer ids) {
/* 1835 */     calls++;
/* 1836 */     this.gl30.glGenTransformFeedbacks(n, ids);
/* 1837 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsTransformFeedback(int id) {
/* 1842 */     calls++;
/* 1843 */     boolean result = this.gl30.glIsTransformFeedback(id);
/* 1844 */     check();
/* 1845 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glPauseTransformFeedback() {
/* 1850 */     calls++;
/* 1851 */     this.gl30.glPauseTransformFeedback();
/* 1852 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glResumeTransformFeedback() {
/* 1857 */     calls++;
/* 1858 */     this.gl30.glResumeTransformFeedback();
/* 1859 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glProgramParameteri(int program, int pname, int value) {
/* 1864 */     calls++;
/* 1865 */     this.gl30.glProgramParameteri(program, pname, value);
/* 1866 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glInvalidateFramebuffer(int target, int numAttachments, IntBuffer attachments) {
/* 1871 */     calls++;
/* 1872 */     this.gl30.glInvalidateFramebuffer(target, numAttachments, attachments);
/* 1873 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glInvalidateSubFramebuffer(int target, int numAttachments, IntBuffer attachments, int x, int y, int width, int height) {
/* 1879 */     calls++;
/* 1880 */     this.gl30.glInvalidateSubFramebuffer(target, numAttachments, attachments, x, y, width, height);
/* 1881 */     check();
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\profiling\GL30Profiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */