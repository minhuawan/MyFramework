/*      */ package com.badlogic.gdx.graphics.profiling;
/*      */ 
/*      */ import com.badlogic.gdx.graphics.GL20;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
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
/*      */ public class GL20Profiler
/*      */   extends GLProfiler
/*      */   implements GL20
/*      */ {
/*      */   public final GL20 gl20;
/*      */   
/*      */   protected GL20Profiler(GL20 gl20) {
/*   33 */     this.gl20 = gl20;
/*      */   }
/*      */   
/*      */   private void check() {
/*   37 */     int error = this.gl20.glGetError();
/*   38 */     while (error != 0) {
/*   39 */       listener.onError(error);
/*   40 */       error = this.gl20.glGetError();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void glActiveTexture(int texture) {
/*   46 */     calls++;
/*   47 */     this.gl20.glActiveTexture(texture);
/*   48 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindTexture(int target, int texture) {
/*   53 */     textureBindings++;
/*   54 */     calls++;
/*   55 */     this.gl20.glBindTexture(target, texture);
/*   56 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendFunc(int sfactor, int dfactor) {
/*   61 */     calls++;
/*   62 */     this.gl20.glBlendFunc(sfactor, dfactor);
/*   63 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClear(int mask) {
/*   68 */     calls++;
/*   69 */     this.gl20.glClear(mask);
/*   70 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearColor(float red, float green, float blue, float alpha) {
/*   75 */     calls++;
/*   76 */     this.gl20.glClearColor(red, green, blue, alpha);
/*   77 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearDepthf(float depth) {
/*   82 */     calls++;
/*   83 */     this.gl20.glClearDepthf(depth);
/*   84 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glClearStencil(int s) {
/*   89 */     calls++;
/*   90 */     this.gl20.glClearStencil(s);
/*   91 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
/*   96 */     calls++;
/*   97 */     this.gl20.glColorMask(red, green, blue, alpha);
/*   98 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
/*  104 */     calls++;
/*  105 */     this.gl20.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
/*  106 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
/*  112 */     calls++;
/*  113 */     this.gl20.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
/*  114 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
/*  119 */     calls++;
/*  120 */     this.gl20.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
/*  121 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
/*  126 */     calls++;
/*  127 */     this.gl20.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
/*  128 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCullFace(int mode) {
/*  133 */     calls++;
/*  134 */     this.gl20.glCullFace(mode);
/*  135 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteTextures(int n, IntBuffer textures) {
/*  140 */     calls++;
/*  141 */     this.gl20.glDeleteTextures(n, textures);
/*  142 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteTexture(int texture) {
/*  147 */     calls++;
/*  148 */     this.gl20.glDeleteTexture(texture);
/*  149 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDepthFunc(int func) {
/*  154 */     calls++;
/*  155 */     this.gl20.glDepthFunc(func);
/*  156 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDepthMask(boolean flag) {
/*  161 */     calls++;
/*  162 */     this.gl20.glDepthMask(flag);
/*  163 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDepthRangef(float zNear, float zFar) {
/*  168 */     calls++;
/*  169 */     this.gl20.glDepthRangef(zNear, zFar);
/*  170 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDisable(int cap) {
/*  175 */     calls++;
/*  176 */     this.gl20.glDisable(cap);
/*  177 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawArrays(int mode, int first, int count) {
/*  182 */     vertexCount.put(count);
/*  183 */     drawCalls++;
/*  184 */     calls++;
/*  185 */     this.gl20.glDrawArrays(mode, first, count);
/*  186 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawElements(int mode, int count, int type, Buffer indices) {
/*  191 */     vertexCount.put(count);
/*  192 */     drawCalls++;
/*  193 */     calls++;
/*  194 */     this.gl20.glDrawElements(mode, count, type, indices);
/*  195 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glEnable(int cap) {
/*  200 */     calls++;
/*  201 */     this.gl20.glEnable(cap);
/*  202 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFinish() {
/*  207 */     calls++;
/*  208 */     this.gl20.glFinish();
/*  209 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFlush() {
/*  214 */     calls++;
/*  215 */     this.gl20.glFlush();
/*  216 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFrontFace(int mode) {
/*  221 */     calls++;
/*  222 */     this.gl20.glFrontFace(mode);
/*  223 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenTextures(int n, IntBuffer textures) {
/*  228 */     calls++;
/*  229 */     this.gl20.glGenTextures(n, textures);
/*  230 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenTexture() {
/*  235 */     calls++;
/*  236 */     int result = this.gl20.glGenTexture();
/*  237 */     check();
/*  238 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetError() {
/*  243 */     calls++;
/*      */     
/*  245 */     return this.gl20.glGetError();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetIntegerv(int pname, IntBuffer params) {
/*  250 */     calls++;
/*  251 */     this.gl20.glGetIntegerv(pname, params);
/*  252 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetString(int name) {
/*  257 */     calls++;
/*  258 */     String result = this.gl20.glGetString(name);
/*  259 */     check();
/*  260 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glHint(int target, int mode) {
/*  265 */     calls++;
/*  266 */     this.gl20.glHint(target, mode);
/*  267 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glLineWidth(float width) {
/*  272 */     calls++;
/*  273 */     this.gl20.glLineWidth(width);
/*  274 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glPixelStorei(int pname, int param) {
/*  279 */     calls++;
/*  280 */     this.gl20.glPixelStorei(pname, param);
/*  281 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glPolygonOffset(float factor, float units) {
/*  286 */     calls++;
/*  287 */     this.gl20.glPolygonOffset(factor, units);
/*  288 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
/*  293 */     calls++;
/*  294 */     this.gl20.glReadPixels(x, y, width, height, format, type, pixels);
/*  295 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glScissor(int x, int y, int width, int height) {
/*  300 */     calls++;
/*  301 */     this.gl20.glScissor(x, y, width, height);
/*  302 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilFunc(int func, int ref, int mask) {
/*  307 */     calls++;
/*  308 */     this.gl20.glStencilFunc(func, ref, mask);
/*  309 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilMask(int mask) {
/*  314 */     calls++;
/*  315 */     this.gl20.glStencilMask(mask);
/*  316 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilOp(int fail, int zfail, int zpass) {
/*  321 */     calls++;
/*  322 */     this.gl20.glStencilOp(fail, zfail, zpass);
/*  323 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
/*  329 */     calls++;
/*  330 */     this.gl20.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
/*  331 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameterf(int target, int pname, float param) {
/*  336 */     calls++;
/*  337 */     this.gl20.glTexParameterf(target, pname, param);
/*  338 */     check();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
/*  344 */     calls++;
/*  345 */     this.gl20.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
/*  346 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glViewport(int x, int y, int width, int height) {
/*  351 */     calls++;
/*  352 */     this.gl20.glViewport(x, y, width, height);
/*  353 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glAttachShader(int program, int shader) {
/*  358 */     calls++;
/*  359 */     this.gl20.glAttachShader(program, shader);
/*  360 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindAttribLocation(int program, int index, String name) {
/*  365 */     calls++;
/*  366 */     this.gl20.glBindAttribLocation(program, index, name);
/*  367 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindBuffer(int target, int buffer) {
/*  372 */     calls++;
/*  373 */     this.gl20.glBindBuffer(target, buffer);
/*  374 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindFramebuffer(int target, int framebuffer) {
/*  379 */     calls++;
/*  380 */     this.gl20.glBindFramebuffer(target, framebuffer);
/*  381 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBindRenderbuffer(int target, int renderbuffer) {
/*  386 */     calls++;
/*  387 */     this.gl20.glBindRenderbuffer(target, renderbuffer);
/*  388 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendColor(float red, float green, float blue, float alpha) {
/*  393 */     calls++;
/*  394 */     this.gl20.glBlendColor(red, green, blue, alpha);
/*  395 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendEquation(int mode) {
/*  400 */     calls++;
/*  401 */     this.gl20.glBlendEquation(mode);
/*  402 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
/*  407 */     calls++;
/*  408 */     this.gl20.glBlendEquationSeparate(modeRGB, modeAlpha);
/*  409 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
/*  414 */     calls++;
/*  415 */     this.gl20.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
/*  416 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBufferData(int target, int size, Buffer data, int usage) {
/*  421 */     calls++;
/*  422 */     this.gl20.glBufferData(target, size, data, usage);
/*  423 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glBufferSubData(int target, int offset, int size, Buffer data) {
/*  428 */     calls++;
/*  429 */     this.gl20.glBufferSubData(target, offset, size, data);
/*  430 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glCheckFramebufferStatus(int target) {
/*  435 */     calls++;
/*  436 */     int result = this.gl20.glCheckFramebufferStatus(target);
/*  437 */     check();
/*  438 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glCompileShader(int shader) {
/*  443 */     calls++;
/*  444 */     this.gl20.glCompileShader(shader);
/*  445 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glCreateProgram() {
/*  450 */     calls++;
/*  451 */     int result = this.gl20.glCreateProgram();
/*  452 */     check();
/*  453 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public int glCreateShader(int type) {
/*  458 */     calls++;
/*  459 */     int result = this.gl20.glCreateShader(type);
/*  460 */     check();
/*  461 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteBuffer(int buffer) {
/*  466 */     calls++;
/*  467 */     this.gl20.glDeleteBuffer(buffer);
/*  468 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteBuffers(int n, IntBuffer buffers) {
/*  473 */     calls++;
/*  474 */     this.gl20.glDeleteBuffers(n, buffers);
/*  475 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteFramebuffer(int framebuffer) {
/*  480 */     calls++;
/*  481 */     this.gl20.glDeleteFramebuffer(framebuffer);
/*  482 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
/*  487 */     calls++;
/*  488 */     this.gl20.glDeleteFramebuffers(n, framebuffers);
/*  489 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteProgram(int program) {
/*  494 */     calls++;
/*  495 */     this.gl20.glDeleteProgram(program);
/*  496 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteRenderbuffer(int renderbuffer) {
/*  501 */     calls++;
/*  502 */     this.gl20.glDeleteRenderbuffer(renderbuffer);
/*  503 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
/*  508 */     calls++;
/*  509 */     this.gl20.glDeleteRenderbuffers(n, renderbuffers);
/*  510 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDeleteShader(int shader) {
/*  515 */     calls++;
/*  516 */     this.gl20.glDeleteShader(shader);
/*  517 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDetachShader(int program, int shader) {
/*  522 */     calls++;
/*  523 */     this.gl20.glDetachShader(program, shader);
/*  524 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDisableVertexAttribArray(int index) {
/*  529 */     calls++;
/*  530 */     this.gl20.glDisableVertexAttribArray(index);
/*  531 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glDrawElements(int mode, int count, int type, int indices) {
/*  536 */     vertexCount.put(count);
/*  537 */     drawCalls++;
/*  538 */     calls++;
/*  539 */     this.gl20.glDrawElements(mode, count, type, indices);
/*  540 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glEnableVertexAttribArray(int index) {
/*  545 */     calls++;
/*  546 */     this.gl20.glEnableVertexAttribArray(index);
/*  547 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
/*  552 */     calls++;
/*  553 */     this.gl20.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
/*  554 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
/*  559 */     calls++;
/*  560 */     this.gl20.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*  561 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenBuffer() {
/*  566 */     calls++;
/*  567 */     int result = this.gl20.glGenBuffer();
/*  568 */     check();
/*  569 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenBuffers(int n, IntBuffer buffers) {
/*  574 */     calls++;
/*  575 */     this.gl20.glGenBuffers(n, buffers);
/*  576 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenerateMipmap(int target) {
/*  581 */     calls++;
/*  582 */     this.gl20.glGenerateMipmap(target);
/*  583 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenFramebuffer() {
/*  588 */     calls++;
/*  589 */     int result = this.gl20.glGenFramebuffer();
/*  590 */     check();
/*  591 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenFramebuffers(int n, IntBuffer framebuffers) {
/*  596 */     calls++;
/*  597 */     this.gl20.glGenFramebuffers(n, framebuffers);
/*  598 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGenRenderbuffer() {
/*  603 */     calls++;
/*  604 */     int result = this.gl20.glGenRenderbuffer();
/*  605 */     check();
/*  606 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
/*  611 */     calls++;
/*  612 */     this.gl20.glGenRenderbuffers(n, renderbuffers);
/*  613 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetActiveAttrib(int program, int index, IntBuffer size, Buffer type) {
/*  618 */     calls++;
/*  619 */     String result = this.gl20.glGetActiveAttrib(program, index, size, type);
/*  620 */     check();
/*  621 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetActiveUniform(int program, int index, IntBuffer size, Buffer type) {
/*  626 */     calls++;
/*  627 */     String result = this.gl20.glGetActiveUniform(program, index, size, type);
/*  628 */     check();
/*  629 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
/*  634 */     calls++;
/*  635 */     this.gl20.glGetAttachedShaders(program, maxcount, count, shaders);
/*  636 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetAttribLocation(int program, String name) {
/*  641 */     calls++;
/*  642 */     int result = this.gl20.glGetAttribLocation(program, name);
/*  643 */     check();
/*  644 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetBooleanv(int pname, Buffer params) {
/*  649 */     calls++;
/*  650 */     this.gl20.glGetBooleanv(pname, params);
/*  651 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
/*  656 */     calls++;
/*  657 */     this.gl20.glGetBufferParameteriv(target, pname, params);
/*  658 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetFloatv(int pname, FloatBuffer params) {
/*  663 */     calls++;
/*  664 */     this.gl20.glGetFloatv(pname, params);
/*  665 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
/*  670 */     calls++;
/*  671 */     this.gl20.glGetFramebufferAttachmentParameteriv(target, attachment, pname, params);
/*  672 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetProgramiv(int program, int pname, IntBuffer params) {
/*  677 */     calls++;
/*  678 */     this.gl20.glGetProgramiv(program, pname, params);
/*  679 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetProgramInfoLog(int program) {
/*  684 */     calls++;
/*  685 */     String result = this.gl20.glGetProgramInfoLog(program);
/*  686 */     check();
/*  687 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
/*  692 */     calls++;
/*  693 */     this.gl20.glGetRenderbufferParameteriv(target, pname, params);
/*  694 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetShaderiv(int shader, int pname, IntBuffer params) {
/*  699 */     calls++;
/*  700 */     this.gl20.glGetShaderiv(shader, pname, params);
/*  701 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public String glGetShaderInfoLog(int shader) {
/*  706 */     calls++;
/*  707 */     String result = this.gl20.glGetShaderInfoLog(shader);
/*  708 */     check();
/*  709 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
/*  714 */     calls++;
/*  715 */     this.gl20.glGetShaderPrecisionFormat(shadertype, precisiontype, range, precision);
/*  716 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
/*  721 */     calls++;
/*  722 */     this.gl20.glGetTexParameterfv(target, pname, params);
/*  723 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
/*  728 */     calls++;
/*  729 */     this.gl20.glGetTexParameteriv(target, pname, params);
/*  730 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetUniformfv(int program, int location, FloatBuffer params) {
/*  735 */     calls++;
/*  736 */     this.gl20.glGetUniformfv(program, location, params);
/*  737 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetUniformiv(int program, int location, IntBuffer params) {
/*  742 */     calls++;
/*  743 */     this.gl20.glGetUniformiv(program, location, params);
/*  744 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public int glGetUniformLocation(int program, String name) {
/*  749 */     calls++;
/*  750 */     int result = this.gl20.glGetUniformLocation(program, name);
/*  751 */     check();
/*  752 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
/*  757 */     calls++;
/*  758 */     this.gl20.glGetVertexAttribfv(index, pname, params);
/*  759 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
/*  764 */     calls++;
/*  765 */     this.gl20.glGetVertexAttribiv(index, pname, params);
/*  766 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glGetVertexAttribPointerv(int index, int pname, Buffer pointer) {
/*  771 */     calls++;
/*  772 */     this.gl20.glGetVertexAttribPointerv(index, pname, pointer);
/*  773 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsBuffer(int buffer) {
/*  778 */     calls++;
/*  779 */     boolean result = this.gl20.glIsBuffer(buffer);
/*  780 */     check();
/*  781 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsEnabled(int cap) {
/*  786 */     calls++;
/*  787 */     boolean result = this.gl20.glIsEnabled(cap);
/*  788 */     check();
/*  789 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsFramebuffer(int framebuffer) {
/*  794 */     calls++;
/*  795 */     boolean result = this.gl20.glIsFramebuffer(framebuffer);
/*  796 */     check();
/*  797 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsProgram(int program) {
/*  802 */     calls++;
/*  803 */     boolean result = this.gl20.glIsProgram(program);
/*  804 */     check();
/*  805 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsRenderbuffer(int renderbuffer) {
/*  810 */     calls++;
/*  811 */     boolean result = this.gl20.glIsRenderbuffer(renderbuffer);
/*  812 */     check();
/*  813 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsShader(int shader) {
/*  818 */     calls++;
/*  819 */     boolean result = this.gl20.glIsShader(shader);
/*  820 */     check();
/*  821 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean glIsTexture(int texture) {
/*  826 */     calls++;
/*  827 */     boolean result = this.gl20.glIsTexture(texture);
/*  828 */     check();
/*  829 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void glLinkProgram(int program) {
/*  834 */     calls++;
/*  835 */     this.gl20.glLinkProgram(program);
/*  836 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glReleaseShaderCompiler() {
/*  841 */     calls++;
/*  842 */     this.gl20.glReleaseShaderCompiler();
/*  843 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
/*  848 */     calls++;
/*  849 */     this.gl20.glRenderbufferStorage(target, internalformat, width, height);
/*  850 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glSampleCoverage(float value, boolean invert) {
/*  855 */     calls++;
/*  856 */     this.gl20.glSampleCoverage(value, invert);
/*  857 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
/*  862 */     calls++;
/*  863 */     this.gl20.glShaderBinary(n, shaders, binaryformat, binary, length);
/*  864 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glShaderSource(int shader, String string) {
/*  869 */     calls++;
/*  870 */     this.gl20.glShaderSource(shader, string);
/*  871 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
/*  876 */     calls++;
/*  877 */     this.gl20.glStencilFuncSeparate(face, func, ref, mask);
/*  878 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilMaskSeparate(int face, int mask) {
/*  883 */     calls++;
/*  884 */     this.gl20.glStencilMaskSeparate(face, mask);
/*  885 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
/*  890 */     calls++;
/*  891 */     this.gl20.glStencilOpSeparate(face, fail, zfail, zpass);
/*  892 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameterfv(int target, int pname, FloatBuffer params) {
/*  897 */     calls++;
/*  898 */     this.gl20.glTexParameterfv(target, pname, params);
/*  899 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameteri(int target, int pname, int param) {
/*  904 */     calls++;
/*  905 */     this.gl20.glTexParameteri(target, pname, param);
/*  906 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glTexParameteriv(int target, int pname, IntBuffer params) {
/*  911 */     calls++;
/*  912 */     this.gl20.glTexParameteriv(target, pname, params);
/*  913 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1f(int location, float x) {
/*  918 */     calls++;
/*  919 */     this.gl20.glUniform1f(location, x);
/*  920 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1fv(int location, int count, FloatBuffer v) {
/*  925 */     calls++;
/*  926 */     this.gl20.glUniform1fv(location, count, v);
/*  927 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1fv(int location, int count, float[] v, int offset) {
/*  932 */     calls++;
/*  933 */     this.gl20.glUniform1fv(location, count, v, offset);
/*  934 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1i(int location, int x) {
/*  939 */     calls++;
/*  940 */     this.gl20.glUniform1i(location, x);
/*  941 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1iv(int location, int count, IntBuffer v) {
/*  946 */     calls++;
/*  947 */     this.gl20.glUniform1iv(location, count, v);
/*  948 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform1iv(int location, int count, int[] v, int offset) {
/*  953 */     calls++;
/*  954 */     this.gl20.glUniform1iv(location, count, v, offset);
/*  955 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2f(int location, float x, float y) {
/*  960 */     calls++;
/*  961 */     this.gl20.glUniform2f(location, x, y);
/*  962 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2fv(int location, int count, FloatBuffer v) {
/*  967 */     calls++;
/*  968 */     this.gl20.glUniform2fv(location, count, v);
/*  969 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2fv(int location, int count, float[] v, int offset) {
/*  974 */     calls++;
/*  975 */     this.gl20.glUniform2fv(location, count, v, offset);
/*  976 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2i(int location, int x, int y) {
/*  981 */     calls++;
/*  982 */     this.gl20.glUniform2i(location, x, y);
/*  983 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2iv(int location, int count, IntBuffer v) {
/*  988 */     calls++;
/*  989 */     this.gl20.glUniform2iv(location, count, v);
/*  990 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform2iv(int location, int count, int[] v, int offset) {
/*  995 */     calls++;
/*  996 */     this.gl20.glUniform2iv(location, count, v, offset);
/*  997 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3f(int location, float x, float y, float z) {
/* 1002 */     calls++;
/* 1003 */     this.gl20.glUniform3f(location, x, y, z);
/* 1004 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3fv(int location, int count, FloatBuffer v) {
/* 1009 */     calls++;
/* 1010 */     this.gl20.glUniform3fv(location, count, v);
/* 1011 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3fv(int location, int count, float[] v, int offset) {
/* 1016 */     calls++;
/* 1017 */     this.gl20.glUniform3fv(location, count, v, offset);
/* 1018 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3i(int location, int x, int y, int z) {
/* 1023 */     calls++;
/* 1024 */     this.gl20.glUniform3i(location, x, y, z);
/* 1025 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3iv(int location, int count, IntBuffer v) {
/* 1030 */     calls++;
/* 1031 */     this.gl20.glUniform3iv(location, count, v);
/* 1032 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform3iv(int location, int count, int[] v, int offset) {
/* 1037 */     calls++;
/* 1038 */     this.gl20.glUniform3iv(location, count, v, offset);
/* 1039 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4f(int location, float x, float y, float z, float w) {
/* 1044 */     calls++;
/* 1045 */     this.gl20.glUniform4f(location, x, y, z, w);
/* 1046 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4fv(int location, int count, FloatBuffer v) {
/* 1051 */     calls++;
/* 1052 */     this.gl20.glUniform4fv(location, count, v);
/* 1053 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4fv(int location, int count, float[] v, int offset) {
/* 1058 */     calls++;
/* 1059 */     this.gl20.glUniform4fv(location, count, v, offset);
/* 1060 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4i(int location, int x, int y, int z, int w) {
/* 1065 */     calls++;
/* 1066 */     this.gl20.glUniform4i(location, x, y, z, w);
/* 1067 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4iv(int location, int count, IntBuffer v) {
/* 1072 */     calls++;
/* 1073 */     this.gl20.glUniform4iv(location, count, v);
/* 1074 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniform4iv(int location, int count, int[] v, int offset) {
/* 1079 */     calls++;
/* 1080 */     this.gl20.glUniform4iv(location, count, v, offset);
/* 1081 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1086 */     calls++;
/* 1087 */     this.gl20.glUniformMatrix2fv(location, count, transpose, value);
/* 1088 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 1093 */     calls++;
/* 1094 */     this.gl20.glUniformMatrix2fv(location, count, transpose, value, offset);
/* 1095 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1100 */     calls++;
/* 1101 */     this.gl20.glUniformMatrix3fv(location, count, transpose, value);
/* 1102 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 1107 */     calls++;
/* 1108 */     this.gl20.glUniformMatrix3fv(location, count, transpose, value, offset);
/* 1109 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 1114 */     calls++;
/* 1115 */     this.gl20.glUniformMatrix4fv(location, count, transpose, value);
/* 1116 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
/* 1121 */     calls++;
/* 1122 */     this.gl20.glUniformMatrix4fv(location, count, transpose, value, offset);
/* 1123 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glUseProgram(int program) {
/* 1128 */     shaderSwitches++;
/* 1129 */     calls++;
/* 1130 */     this.gl20.glUseProgram(program);
/* 1131 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glValidateProgram(int program) {
/* 1136 */     calls++;
/* 1137 */     this.gl20.glValidateProgram(program);
/* 1138 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib1f(int indx, float x) {
/* 1143 */     calls++;
/* 1144 */     this.gl20.glVertexAttrib1f(indx, x);
/* 1145 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib1fv(int indx, FloatBuffer values) {
/* 1150 */     calls++;
/* 1151 */     this.gl20.glVertexAttrib1fv(indx, values);
/* 1152 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib2f(int indx, float x, float y) {
/* 1157 */     calls++;
/* 1158 */     this.gl20.glVertexAttrib2f(indx, x, y);
/* 1159 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib2fv(int indx, FloatBuffer values) {
/* 1164 */     calls++;
/* 1165 */     this.gl20.glVertexAttrib2fv(indx, values);
/* 1166 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib3f(int indx, float x, float y, float z) {
/* 1171 */     calls++;
/* 1172 */     this.gl20.glVertexAttrib3f(indx, x, y, z);
/* 1173 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib3fv(int indx, FloatBuffer values) {
/* 1178 */     calls++;
/* 1179 */     this.gl20.glVertexAttrib3fv(indx, values);
/* 1180 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
/* 1185 */     calls++;
/* 1186 */     this.gl20.glVertexAttrib4f(indx, x, y, z, w);
/* 1187 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttrib4fv(int indx, FloatBuffer values) {
/* 1192 */     calls++;
/* 1193 */     this.gl20.glVertexAttrib4fv(indx, values);
/* 1194 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
/* 1199 */     calls++;
/* 1200 */     this.gl20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
/* 1201 */     check();
/*      */   }
/*      */ 
/*      */   
/*      */   public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
/* 1206 */     calls++;
/* 1207 */     this.gl20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
/* 1208 */     check();
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\profiling\GL20Profiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */