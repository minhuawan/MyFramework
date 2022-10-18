/*    */ package com.badlogic.gdx.graphics.glutils;
/*    */ 
/*    */ import com.badlogic.gdx.Application;
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
/*    */ public class FloatFrameBuffer
/*    */   extends FrameBuffer
/*    */ {
/*    */   public FloatFrameBuffer(int width, int height, boolean hasDepth) {
/* 36 */     super((Pixmap.Format)null, width, height, hasDepth);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Texture createColorTexture() {
/* 41 */     FloatTextureData data = new FloatTextureData(this.width, this.height);
/* 42 */     Texture result = new Texture(data);
/* 43 */     if (Gdx.app.getType() == Application.ApplicationType.Desktop || Gdx.app.getType() == Application.ApplicationType.Applet) {
/* 44 */       result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
/*    */     } else {
/*    */       
/* 47 */       result.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
/* 48 */     }  result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
/* 49 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void disposeColorTexture(Texture colorTexture) {
/* 54 */     colorTexture.dispose();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\FloatFrameBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */