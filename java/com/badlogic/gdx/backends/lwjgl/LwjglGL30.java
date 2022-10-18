/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.GL30;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL12;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ import org.lwjgl.opengl.GL20;
/*     */ import org.lwjgl.opengl.GL21;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GL31;
/*     */ import org.lwjgl.opengl.GL32;
/*     */ import org.lwjgl.opengl.GL33;
/*     */ import org.lwjgl.opengl.GL40;
/*     */ import org.lwjgl.opengl.GL41;
/*     */ import org.lwjgl.opengl.GL43;
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
/*     */ class LwjglGL30
/*     */   extends LwjglGL20
/*     */   implements GL30
/*     */ {
/*     */   public void glReadBuffer(int mode) {
/*  45 */     GL11.glReadBuffer(mode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDrawRangeElements(int mode, int start, int end, int count, int type, Buffer indices) {
/*  50 */     if (indices instanceof ByteBuffer) { GL12.glDrawRangeElements(mode, start, end, (ByteBuffer)indices); }
/*  51 */     else if (indices instanceof ShortBuffer) { GL12.glDrawRangeElements(mode, start, end, (ShortBuffer)indices); }
/*  52 */     else if (indices instanceof IntBuffer) { GL12.glDrawRangeElements(mode, start, end, (IntBuffer)indices); }
/*  53 */     else { throw new GdxRuntimeException("indices must be byte, short or int buffer"); }
/*     */   
/*     */   }
/*     */   
/*     */   public void glDrawRangeElements(int mode, int start, int end, int count, int type, int offset) {
/*  58 */     GL12.glDrawRangeElements(mode, start, end, count, type, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, Buffer pixels) {
/*  64 */     if (pixels == null) {
/*  65 */       GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, (ByteBuffer)null);
/*  66 */     } else if (pixels instanceof ByteBuffer) {
/*  67 */       GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, (ByteBuffer)pixels);
/*  68 */     } else if (pixels instanceof ShortBuffer) {
/*  69 */       GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, (ShortBuffer)pixels);
/*  70 */     } else if (pixels instanceof IntBuffer) {
/*  71 */       GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, (IntBuffer)pixels);
/*  72 */     } else if (pixels instanceof FloatBuffer) {
/*  73 */       GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, (FloatBuffer)pixels);
/*  74 */     } else if (pixels instanceof DoubleBuffer) {
/*  75 */       GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, (DoubleBuffer)pixels);
/*     */     } else {
/*  77 */       throw new GdxRuntimeException("Can't use " + pixels.getClass().getName() + " with this method. Use ByteBuffer, ShortBuffer, IntBuffer, FloatBuffer or DoubleBuffer instead. Blame LWJGL");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, int offset) {
/*  84 */     GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, Buffer pixels) {
/*  90 */     if (pixels instanceof ByteBuffer) {
/*  91 */       GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (ByteBuffer)pixels);
/*  92 */     } else if (pixels instanceof ShortBuffer) {
/*  93 */       GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (ShortBuffer)pixels);
/*  94 */     } else if (pixels instanceof IntBuffer) {
/*  95 */       GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (IntBuffer)pixels);
/*  96 */     } else if (pixels instanceof FloatBuffer) {
/*  97 */       GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (FloatBuffer)pixels);
/*  98 */     } else if (pixels instanceof DoubleBuffer) {
/*  99 */       GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (DoubleBuffer)pixels);
/*     */     } else {
/* 101 */       throw new GdxRuntimeException("Can't use " + pixels.getClass().getName() + " with this method. Use ByteBuffer, ShortBuffer, IntBuffer, FloatBuffer or DoubleBuffer instead. Blame LWJGL");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, int offset) {
/* 108 */     GL12.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glCopyTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
/* 114 */     GL12.glCopyTexSubImage3D(target, level, xoffset, yoffset, zoffset, x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenQueries(int n, int[] ids, int offset) {
/* 119 */     for (int i = offset; i < offset + n; i++) {
/* 120 */       ids[i] = GL15.glGenQueries();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenQueries(int n, IntBuffer ids) {
/* 126 */     for (int i = 0; i < n; i++) {
/* 127 */       ids.put(GL15.glGenQueries());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteQueries(int n, int[] ids, int offset) {
/* 133 */     for (int i = offset; i < offset + n; i++) {
/* 134 */       GL15.glDeleteQueries(ids[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteQueries(int n, IntBuffer ids) {
/* 140 */     for (int i = 0; i < n; i++) {
/* 141 */       GL15.glDeleteQueries(ids.get());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean glIsQuery(int id) {
/* 147 */     return GL15.glIsQuery(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBeginQuery(int target, int id) {
/* 152 */     GL15.glBeginQuery(target, id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glEndQuery(int target) {
/* 157 */     GL15.glEndQuery(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetQueryiv(int target, int pname, IntBuffer params) {
/* 162 */     GL15.glGetQuery(target, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetQueryObjectuiv(int id, int pname, IntBuffer params) {
/* 167 */     GL15.glGetQueryObjectu(id, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean glUnmapBuffer(int target) {
/* 172 */     return GL15.glUnmapBuffer(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public Buffer glGetBufferPointerv(int target, int pname) {
/* 177 */     return GL15.glGetBufferPointer(target, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDrawBuffers(int n, IntBuffer bufs) {
/* 182 */     GL20.glDrawBuffers(bufs);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniformMatrix2x3fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 187 */     GL21.glUniformMatrix2x3(location, transpose, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniformMatrix3x2fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 192 */     GL21.glUniformMatrix3x2(location, transpose, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniformMatrix2x4fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 197 */     GL21.glUniformMatrix2x4(location, transpose, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniformMatrix4x2fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 202 */     GL21.glUniformMatrix4x2(location, transpose, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniformMatrix3x4fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 207 */     GL21.glUniformMatrix3x4(location, transpose, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glUniformMatrix4x3fv(int location, int count, boolean transpose, FloatBuffer value) {
/* 213 */     GL21.glUniformMatrix4x3(location, transpose, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 219 */     GL30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBindFramebuffer(int target, int framebuffer) {
/* 224 */     GL30.glBindFramebuffer(target, framebuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBindRenderbuffer(int target, int renderbuffer) {
/* 229 */     GL30.glBindRenderbuffer(target, renderbuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public int glCheckFramebufferStatus(int target) {
/* 234 */     return GL30.glCheckFramebufferStatus(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
/* 239 */     GL30.glDeleteFramebuffers(framebuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteFramebuffer(int framebuffer) {
/* 244 */     GL30.glDeleteFramebuffers(framebuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
/* 249 */     GL30.glDeleteRenderbuffers(renderbuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteRenderbuffer(int renderbuffer) {
/* 254 */     GL30.glDeleteRenderbuffers(renderbuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenerateMipmap(int target) {
/* 259 */     GL30.glGenerateMipmap(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenFramebuffers(int n, IntBuffer framebuffers) {
/* 264 */     GL30.glGenFramebuffers(framebuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public int glGenFramebuffer() {
/* 269 */     return GL30.glGenFramebuffers();
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
/* 274 */     GL30.glGenRenderbuffers(renderbuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public int glGenRenderbuffer() {
/* 279 */     return GL30.glGenRenderbuffers();
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
/* 284 */     GL30.glGetRenderbufferParameter(target, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean glIsFramebuffer(int framebuffer) {
/* 289 */     return GL30.glIsFramebuffer(framebuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean glIsRenderbuffer(int renderbuffer) {
/* 294 */     return GL30.glIsRenderbuffer(renderbuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
/* 299 */     GL30.glRenderbufferStorage(target, internalformat, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
/* 304 */     GL30.glRenderbufferStorageMultisample(target, samples, internalformat, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
/* 309 */     GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
/* 314 */     GL30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
/* 319 */     GL30.glFramebufferTextureLayer(target, attachment, texture, level, layer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glFlushMappedBufferRange(int target, int offset, int length) {
/* 324 */     GL30.glFlushMappedBufferRange(target, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBindVertexArray(int array) {
/* 329 */     GL30.glBindVertexArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteVertexArrays(int n, int[] arrays, int offset) {
/* 334 */     for (int i = offset; i < offset + n; i++) {
/* 335 */       GL30.glDeleteVertexArrays(arrays[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteVertexArrays(int n, IntBuffer arrays) {
/* 341 */     GL30.glDeleteVertexArrays(arrays);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenVertexArrays(int n, int[] arrays, int offset) {
/* 346 */     for (int i = offset; i < offset + n; i++) {
/* 347 */       arrays[i] = GL30.glGenVertexArrays();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenVertexArrays(int n, IntBuffer arrays) {
/* 353 */     GL30.glGenVertexArrays(arrays);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean glIsVertexArray(int array) {
/* 358 */     return GL30.glIsVertexArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBeginTransformFeedback(int primitiveMode) {
/* 363 */     GL30.glBeginTransformFeedback(primitiveMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glEndTransformFeedback() {
/* 368 */     GL30.glEndTransformFeedback();
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBindBufferRange(int target, int index, int buffer, int offset, int size) {
/* 373 */     GL30.glBindBufferRange(target, index, buffer, offset, size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBindBufferBase(int target, int index, int buffer) {
/* 378 */     GL30.glBindBufferBase(target, index, buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glTransformFeedbackVaryings(int program, String[] varyings, int bufferMode) {
/* 383 */     GL30.glTransformFeedbackVaryings(program, (CharSequence[])varyings, bufferMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glVertexAttribIPointer(int index, int size, int type, int stride, int offset) {
/* 388 */     GL30.glVertexAttribIPointer(index, size, type, stride, offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetVertexAttribIiv(int index, int pname, IntBuffer params) {
/* 393 */     GL30.glGetVertexAttribI(index, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetVertexAttribIuiv(int index, int pname, IntBuffer params) {
/* 398 */     GL30.glGetVertexAttribIu(index, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glVertexAttribI4i(int index, int x, int y, int z, int w) {
/* 403 */     GL30.glVertexAttribI4i(index, x, y, z, w);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glVertexAttribI4ui(int index, int x, int y, int z, int w) {
/* 408 */     GL30.glVertexAttribI4ui(index, x, y, z, w);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetUniformuiv(int program, int location, IntBuffer params) {
/* 413 */     GL30.glGetUniformu(program, location, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public int glGetFragDataLocation(int program, String name) {
/* 418 */     return GL30.glGetFragDataLocation(program, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniform1uiv(int location, int count, IntBuffer value) {
/* 423 */     GL30.glUniform1u(location, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniform3uiv(int location, int count, IntBuffer value) {
/* 428 */     GL30.glUniform3u(location, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniform4uiv(int location, int count, IntBuffer value) {
/* 433 */     GL30.glUniform4u(location, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glClearBufferiv(int buffer, int drawbuffer, IntBuffer value) {
/* 438 */     GL30.glClearBuffer(buffer, drawbuffer, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glClearBufferuiv(int buffer, int drawbuffer, IntBuffer value) {
/* 443 */     GL30.glClearBufferu(buffer, drawbuffer, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glClearBufferfv(int buffer, int drawbuffer, FloatBuffer value) {
/* 448 */     GL30.glClearBuffer(buffer, drawbuffer, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glClearBufferfi(int buffer, int drawbuffer, float depth, int stencil) {
/* 453 */     GL30.glClearBufferfi(buffer, drawbuffer, depth, stencil);
/*     */   }
/*     */ 
/*     */   
/*     */   public String glGetStringi(int name, int index) {
/* 458 */     return GL30.glGetStringi(name, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glCopyBufferSubData(int readTarget, int writeTarget, int readOffset, int writeOffset, int size) {
/* 463 */     GL31.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetUniformIndices(int program, String[] uniformNames, IntBuffer uniformIndices) {
/* 468 */     GL31.glGetUniformIndices(program, (CharSequence[])uniformNames, uniformIndices);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetActiveUniformsiv(int program, int uniformCount, IntBuffer uniformIndices, int pname, IntBuffer params) {
/* 473 */     GL31.glGetActiveUniforms(program, uniformIndices, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public int glGetUniformBlockIndex(int program, String uniformBlockName) {
/* 478 */     return GL31.glGetUniformBlockIndex(program, uniformBlockName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, IntBuffer params) {
/* 483 */     params.put(GL31.glGetActiveUniformBlocki(program, uniformBlockIndex, pname));
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetActiveUniformBlockName(int program, int uniformBlockIndex, Buffer length, Buffer uniformBlockName) {
/* 488 */     GL31.glGetActiveUniformBlockName(program, uniformBlockIndex, (IntBuffer)length, (ByteBuffer)uniformBlockName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String glGetActiveUniformBlockName(int program, int uniformBlockIndex) {
/* 493 */     return GL31.glGetActiveUniformBlockName(program, uniformBlockIndex, 1024);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
/* 498 */     GL31.glUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDrawArraysInstanced(int mode, int first, int count, int instanceCount) {
/* 503 */     GL31.glDrawArraysInstanced(mode, first, count, instanceCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDrawElementsInstanced(int mode, int count, int type, int indicesOffset, int instanceCount) {
/* 508 */     GL31.glDrawElementsInstanced(mode, count, type, indicesOffset, instanceCount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glGetInteger64v(int pname, LongBuffer params) {
/* 514 */     GL32.glGetInteger64(pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetBufferParameteri64v(int target, int pname, LongBuffer params) {
/* 519 */     params.put(GL32.glGetBufferParameteri64(target, pname));
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenSamplers(int count, int[] samplers, int offset) {
/* 524 */     for (int i = offset; i < offset + count; i++) {
/* 525 */       samplers[i] = GL33.glGenSamplers();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenSamplers(int count, IntBuffer samplers) {
/* 531 */     GL33.glGenSamplers(samplers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteSamplers(int count, int[] samplers, int offset) {
/* 536 */     for (int i = offset; i < offset + count; i++) {
/* 537 */       GL33.glDeleteSamplers(samplers[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteSamplers(int count, IntBuffer samplers) {
/* 543 */     GL33.glDeleteSamplers(samplers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean glIsSampler(int sampler) {
/* 548 */     return GL33.glIsSampler(sampler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBindSampler(int unit, int sampler) {
/* 553 */     GL33.glBindSampler(unit, sampler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glSamplerParameteri(int sampler, int pname, int param) {
/* 558 */     GL33.glSamplerParameteri(sampler, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glSamplerParameteriv(int sampler, int pname, IntBuffer param) {
/* 563 */     GL33.glSamplerParameter(sampler, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glSamplerParameterf(int sampler, int pname, float param) {
/* 568 */     GL33.glSamplerParameterf(sampler, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glSamplerParameterfv(int sampler, int pname, FloatBuffer param) {
/* 573 */     GL33.glSamplerParameter(sampler, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetSamplerParameteriv(int sampler, int pname, IntBuffer params) {
/* 578 */     GL33.glGetSamplerParameterI(sampler, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGetSamplerParameterfv(int sampler, int pname, FloatBuffer params) {
/* 583 */     GL33.glGetSamplerParameter(sampler, pname, params);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glVertexAttribDivisor(int index, int divisor) {
/* 589 */     GL33.glVertexAttribDivisor(index, divisor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glBindTransformFeedback(int target, int id) {
/* 594 */     GL40.glBindTransformFeedback(target, id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteTransformFeedbacks(int n, int[] ids, int offset) {
/* 599 */     for (int i = offset; i < offset + n; i++) {
/* 600 */       GL40.glDeleteTransformFeedbacks(ids[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glDeleteTransformFeedbacks(int n, IntBuffer ids) {
/* 606 */     GL40.glDeleteTransformFeedbacks(ids);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenTransformFeedbacks(int n, int[] ids, int offset) {
/* 611 */     for (int i = offset; i < offset + n; i++) {
/* 612 */       ids[i] = GL40.glGenTransformFeedbacks();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void glGenTransformFeedbacks(int n, IntBuffer ids) {
/* 618 */     GL40.glGenTransformFeedbacks(ids);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean glIsTransformFeedback(int id) {
/* 623 */     return GL40.glIsTransformFeedback(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glPauseTransformFeedback() {
/* 628 */     GL40.glPauseTransformFeedback();
/*     */   }
/*     */ 
/*     */   
/*     */   public void glResumeTransformFeedback() {
/* 633 */     GL40.glResumeTransformFeedback();
/*     */   }
/*     */ 
/*     */   
/*     */   public void glProgramParameteri(int program, int pname, int value) {
/* 638 */     GL41.glProgramParameteri(program, pname, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void glInvalidateFramebuffer(int target, int numAttachments, IntBuffer attachments) {
/* 643 */     GL43.glInvalidateFramebuffer(target, attachments);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void glInvalidateSubFramebuffer(int target, int numAttachments, IntBuffer attachments, int x, int y, int width, int height) {
/* 649 */     GL43.glInvalidateSubFramebuffer(target, attachments, x, y, width, height);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglGL30.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */