/*    */ package com.badlogic.gdx.graphics.glutils;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.graphics.GLTexture;
/*    */ import com.badlogic.gdx.graphics.Pixmap;
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FrameBuffer
/*    */   extends GLFrameBuffer<Texture>
/*    */ {
/*    */   public FrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth) {
/* 46 */     this(format, width, height, hasDepth, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
/* 58 */     super(format, width, height, hasDepth, hasStencil);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Texture createColorTexture() {
/* 63 */     int glFormat = Pixmap.Format.toGlFormat(this.format);
/* 64 */     int glType = Pixmap.Format.toGlType(this.format);
/* 65 */     GLOnlyTextureData data = new GLOnlyTextureData(this.width, this.height, 0, glFormat, glFormat, glType);
/* 66 */     Texture result = new Texture(data);
/* 67 */     result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
/* 68 */     result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
/* 69 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void disposeColorTexture(Texture colorTexture) {
/* 74 */     colorTexture.dispose();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void attachFrameBufferColorTexture() {
/* 79 */     Gdx.gl20.glFramebufferTexture2D(36160, 36064, 3553, this.colorTexture
/* 80 */         .getTextureObjectHandle(), 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void unbind() {
/* 85 */     GLFrameBuffer.unbind();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\FrameBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */