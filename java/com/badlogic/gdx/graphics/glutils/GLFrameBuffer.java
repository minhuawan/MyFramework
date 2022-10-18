/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class GLFrameBuffer<T extends GLTexture>
/*     */   implements Disposable
/*     */ {
/*  52 */   private static final Map<Application, Array<GLFrameBuffer>> buffers = new HashMap<Application, Array<GLFrameBuffer>>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int GL_DEPTH24_STENCIL8_OES = 35056;
/*     */ 
/*     */ 
/*     */   
/*     */   protected T colorTexture;
/*     */ 
/*     */   
/*     */   private static int defaultFramebufferHandle;
/*     */ 
/*     */   
/*     */   private static boolean defaultFramebufferHandleInitialized = false;
/*     */ 
/*     */   
/*     */   private int framebufferHandle;
/*     */ 
/*     */   
/*     */   private int depthbufferHandle;
/*     */ 
/*     */   
/*     */   private int stencilbufferHandle;
/*     */ 
/*     */   
/*     */   private int depthStencilPackedBufferHandle;
/*     */ 
/*     */   
/*     */   protected final int width;
/*     */ 
/*     */   
/*     */   protected final int height;
/*     */ 
/*     */   
/*     */   protected final boolean hasDepth;
/*     */ 
/*     */   
/*     */   protected final boolean hasStencil;
/*     */ 
/*     */   
/*     */   private boolean hasDepthStencilPackedBuffer;
/*     */ 
/*     */   
/*     */   protected final Pixmap.Format format;
/*     */ 
/*     */ 
/*     */   
/*     */   public GLFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth) {
/* 101 */     this(format, width, height, hasDepth, false);
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
/*     */   public GLFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
/* 113 */     this.width = width;
/* 114 */     this.height = height;
/* 115 */     this.format = format;
/* 116 */     this.hasDepth = hasDepth;
/* 117 */     this.hasStencil = hasStencil;
/* 118 */     build();
/*     */     
/* 120 */     addManagedFrameBuffer(Gdx.app, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract T createColorTexture();
/*     */ 
/*     */   
/*     */   protected abstract void disposeColorTexture(T paramT);
/*     */ 
/*     */   
/*     */   protected abstract void attachFrameBufferColorTexture();
/*     */   
/*     */   private void build() {
/* 133 */     GL20 gl = Gdx.gl20;
/*     */ 
/*     */     
/* 136 */     if (!defaultFramebufferHandleInitialized) {
/* 137 */       defaultFramebufferHandleInitialized = true;
/* 138 */       if (Gdx.app.getType() == Application.ApplicationType.iOS) {
/* 139 */         IntBuffer intbuf = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
/* 140 */         gl.glGetIntegerv(36006, intbuf);
/* 141 */         defaultFramebufferHandle = intbuf.get(0);
/*     */       } else {
/* 143 */         defaultFramebufferHandle = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 147 */     this.colorTexture = createColorTexture();
/*     */     
/* 149 */     this.framebufferHandle = gl.glGenFramebuffer();
/*     */     
/* 151 */     if (this.hasDepth) {
/* 152 */       this.depthbufferHandle = gl.glGenRenderbuffer();
/*     */     }
/*     */     
/* 155 */     if (this.hasStencil) {
/* 156 */       this.stencilbufferHandle = gl.glGenRenderbuffer();
/*     */     }
/*     */     
/* 159 */     gl.glBindTexture(((GLTexture)this.colorTexture).glTarget, this.colorTexture.getTextureObjectHandle());
/*     */     
/* 161 */     if (this.hasDepth) {
/* 162 */       gl.glBindRenderbuffer(36161, this.depthbufferHandle);
/* 163 */       gl.glRenderbufferStorage(36161, 33189, this.colorTexture.getWidth(), this.colorTexture
/* 164 */           .getHeight());
/*     */     } 
/*     */     
/* 167 */     if (this.hasStencil) {
/* 168 */       gl.glBindRenderbuffer(36161, this.stencilbufferHandle);
/* 169 */       gl.glRenderbufferStorage(36161, 36168, this.colorTexture.getWidth(), this.colorTexture.getHeight());
/*     */     } 
/*     */     
/* 172 */     gl.glBindFramebuffer(36160, this.framebufferHandle);
/*     */     
/* 174 */     attachFrameBufferColorTexture();
/*     */     
/* 176 */     if (this.hasDepth) {
/* 177 */       gl.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthbufferHandle);
/*     */     }
/*     */     
/* 180 */     if (this.hasStencil) {
/* 181 */       gl.glFramebufferRenderbuffer(36160, 36128, 36161, this.stencilbufferHandle);
/*     */     }
/*     */     
/* 184 */     gl.glBindRenderbuffer(36161, 0);
/* 185 */     gl.glBindTexture(((GLTexture)this.colorTexture).glTarget, 0);
/*     */     
/* 187 */     int result = gl.glCheckFramebufferStatus(36160);
/*     */     
/* 189 */     if (result == 36061 && this.hasDepth && this.hasStencil && (Gdx.graphics
/* 190 */       .supportsExtension("GL_OES_packed_depth_stencil") || Gdx.graphics
/* 191 */       .supportsExtension("GL_EXT_packed_depth_stencil"))) {
/* 192 */       if (this.hasDepth) {
/* 193 */         gl.glDeleteRenderbuffer(this.depthbufferHandle);
/* 194 */         this.depthbufferHandle = 0;
/*     */       } 
/* 196 */       if (this.hasStencil) {
/* 197 */         gl.glDeleteRenderbuffer(this.stencilbufferHandle);
/* 198 */         this.stencilbufferHandle = 0;
/*     */       } 
/*     */       
/* 201 */       this.depthStencilPackedBufferHandle = gl.glGenRenderbuffer();
/* 202 */       this.hasDepthStencilPackedBuffer = true;
/* 203 */       gl.glBindRenderbuffer(36161, this.depthStencilPackedBufferHandle);
/* 204 */       gl.glRenderbufferStorage(36161, 35056, this.colorTexture.getWidth(), this.colorTexture.getHeight());
/* 205 */       gl.glBindRenderbuffer(36161, 0);
/*     */       
/* 207 */       gl.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthStencilPackedBufferHandle);
/* 208 */       gl.glFramebufferRenderbuffer(36160, 36128, 36161, this.depthStencilPackedBufferHandle);
/* 209 */       result = gl.glCheckFramebufferStatus(36160);
/*     */     } 
/*     */     
/* 212 */     gl.glBindFramebuffer(36160, defaultFramebufferHandle);
/*     */     
/* 214 */     if (result != 36053) {
/* 215 */       disposeColorTexture(this.colorTexture);
/*     */       
/* 217 */       if (this.hasDepthStencilPackedBuffer) {
/* 218 */         gl.glDeleteBuffer(this.depthStencilPackedBufferHandle);
/*     */       } else {
/* 220 */         if (this.hasDepth) gl.glDeleteRenderbuffer(this.depthbufferHandle); 
/* 221 */         if (this.hasStencil) gl.glDeleteRenderbuffer(this.stencilbufferHandle);
/*     */       
/*     */       } 
/* 224 */       gl.glDeleteFramebuffer(this.framebufferHandle);
/*     */       
/* 226 */       if (result == 36054)
/* 227 */         throw new IllegalStateException("frame buffer couldn't be constructed: incomplete attachment"); 
/* 228 */       if (result == 36057)
/* 229 */         throw new IllegalStateException("frame buffer couldn't be constructed: incomplete dimensions"); 
/* 230 */       if (result == 36055)
/* 231 */         throw new IllegalStateException("frame buffer couldn't be constructed: missing attachment"); 
/* 232 */       if (result == 36061)
/* 233 */         throw new IllegalStateException("frame buffer couldn't be constructed: unsupported combination of formats"); 
/* 234 */       throw new IllegalStateException("frame buffer couldn't be constructed: unknown error " + result);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 241 */     GL20 gl = Gdx.gl20;
/*     */     
/* 243 */     disposeColorTexture(this.colorTexture);
/*     */     
/* 245 */     if (this.hasDepthStencilPackedBuffer) {
/* 246 */       gl.glDeleteRenderbuffer(this.depthStencilPackedBufferHandle);
/*     */     } else {
/* 248 */       if (this.hasDepth) gl.glDeleteRenderbuffer(this.depthbufferHandle); 
/* 249 */       if (this.hasStencil) gl.glDeleteRenderbuffer(this.stencilbufferHandle);
/*     */     
/*     */     } 
/* 252 */     gl.glDeleteFramebuffer(this.framebufferHandle);
/*     */     
/* 254 */     if (buffers.get(Gdx.app) != null) ((Array)buffers.get(Gdx.app)).removeValue(this, true);
/*     */   
/*     */   }
/*     */   
/*     */   public void bind() {
/* 259 */     Gdx.gl20.glBindFramebuffer(36160, this.framebufferHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void unbind() {
/* 264 */     Gdx.gl20.glBindFramebuffer(36160, defaultFramebufferHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/* 269 */     bind();
/* 270 */     setFrameBufferViewport();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setFrameBufferViewport() {
/* 275 */     Gdx.gl20.glViewport(0, 0, this.colorTexture.getWidth(), this.colorTexture.getHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 280 */     end(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end(int x, int y, int width, int height) {
/* 290 */     unbind();
/* 291 */     Gdx.gl20.glViewport(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getColorBufferTexture() {
/* 296 */     return this.colorTexture;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFramebufferHandle() {
/* 301 */     return this.framebufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepthBufferHandle() {
/* 306 */     return this.depthbufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStencilBufferHandle() {
/* 311 */     return this.stencilbufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDepthStencilPackedBuffer() {
/* 316 */     return this.depthStencilPackedBufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 321 */     return this.colorTexture.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 326 */     return this.colorTexture.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 331 */     return this.colorTexture.getDepth();
/*     */   }
/*     */   
/*     */   private static void addManagedFrameBuffer(Application app, GLFrameBuffer frameBuffer) {
/* 335 */     Array<GLFrameBuffer> managedResources = buffers.get(app);
/* 336 */     if (managedResources == null) managedResources = new Array(); 
/* 337 */     managedResources.add(frameBuffer);
/* 338 */     buffers.put(app, managedResources);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void invalidateAllFrameBuffers(Application app) {
/* 344 */     if (Gdx.gl20 == null)
/*     */       return; 
/* 346 */     Array<GLFrameBuffer> bufferArray = buffers.get(app);
/* 347 */     if (bufferArray == null)
/* 348 */       return;  for (int i = 0; i < bufferArray.size; i++) {
/* 349 */       ((GLFrameBuffer)bufferArray.get(i)).build();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clearAllFrameBuffers(Application app) {
/* 354 */     buffers.remove(app);
/*     */   }
/*     */   
/*     */   public static StringBuilder getManagedStatus(StringBuilder builder) {
/* 358 */     builder.append("Managed buffers/app: { ");
/* 359 */     for (Application app : buffers.keySet()) {
/* 360 */       builder.append(((Array)buffers.get(app)).size);
/* 361 */       builder.append(" ");
/*     */     } 
/* 363 */     builder.append("}");
/* 364 */     return builder;
/*     */   }
/*     */   
/*     */   public static String getManagedStatus() {
/* 368 */     return getManagedStatus(new StringBuilder()).toString();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\GLFrameBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */