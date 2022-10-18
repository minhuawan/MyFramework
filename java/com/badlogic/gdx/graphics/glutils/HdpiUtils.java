/*    */ package com.badlogic.gdx.graphics.glutils;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
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
/*    */ public class HdpiUtils
/*    */ {
/*    */   public static void glScissor(int x, int y, int width, int height) {
/* 33 */     if (Gdx.graphics.getWidth() != Gdx.graphics.getBackBufferWidth() || Gdx.graphics
/* 34 */       .getHeight() != Gdx.graphics.getBackBufferHeight()) {
/* 35 */       Gdx.gl.glScissor(toBackBufferX(x), toBackBufferY(y), toBackBufferX(width), toBackBufferY(height));
/*    */     } else {
/* 37 */       Gdx.gl.glScissor(x, y, width, height);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void glViewport(int x, int y, int width, int height) {
/* 44 */     if (Gdx.graphics.getWidth() != Gdx.graphics.getBackBufferWidth() || Gdx.graphics
/* 45 */       .getHeight() != Gdx.graphics.getBackBufferHeight()) {
/* 46 */       Gdx.gl.glViewport(toBackBufferX(x), toBackBufferY(y), toBackBufferX(width), toBackBufferY(height));
/*    */     } else {
/* 48 */       Gdx.gl.glViewport(x, y, width, height);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int toLogicalX(int backBufferX) {
/* 57 */     return (int)((backBufferX * Gdx.graphics.getWidth()) / Gdx.graphics.getBackBufferWidth());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int toLogicalY(int backBufferY) {
/* 65 */     return (int)((backBufferY * Gdx.graphics.getHeight()) / Gdx.graphics.getBackBufferHeight());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int toBackBufferX(int logicalX) {
/* 73 */     return (int)((logicalX * Gdx.graphics.getBackBufferWidth()) / Gdx.graphics.getWidth());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int toBackBufferY(int logicalY) {
/* 81 */     return (int)((logicalY * Gdx.graphics.getBackBufferHeight()) / Gdx.graphics.getHeight());
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\HdpiUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */