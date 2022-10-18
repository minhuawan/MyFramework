/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.graphics.Cursor;
/*    */ import com.badlogic.gdx.graphics.Pixmap;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*    */ import java.nio.ByteOrder;
/*    */ import java.nio.IntBuffer;
/*    */ import org.lwjgl.LWJGLException;
/*    */ import org.lwjgl.input.Cursor;
/*    */ 
/*    */ public class LwjglCursor
/*    */   implements Cursor
/*    */ {
/* 16 */   Cursor lwjglCursor = null;
/*    */   
/*    */   public LwjglCursor(Pixmap pixmap, int xHotspot, int yHotspot) {
/* 19 */     if (((LwjglGraphics)Gdx.graphics).canvas != null && SharedLibraryLoader.isMac) {
/*    */       return;
/*    */     }
/*    */     try {
/* 23 */       if (pixmap == null) {
/* 24 */         this.lwjglCursor = null;
/*    */         
/*    */         return;
/*    */       } 
/* 28 */       if (pixmap.getFormat() != Pixmap.Format.RGBA8888) {
/* 29 */         throw new GdxRuntimeException("Cursor image pixmap is not in RGBA8888 format.");
/*    */       }
/*    */       
/* 32 */       if ((pixmap.getWidth() & pixmap.getWidth() - 1) != 0) {
/* 33 */         throw new GdxRuntimeException("Cursor image pixmap width of " + pixmap.getWidth() + " is not a power-of-two greater than zero.");
/*    */       }
/*    */ 
/*    */       
/* 37 */       if ((pixmap.getHeight() & pixmap.getHeight() - 1) != 0) {
/* 38 */         throw new GdxRuntimeException("Cursor image pixmap height of " + pixmap.getHeight() + " is not a power-of-two greater than zero.");
/*    */       }
/*    */ 
/*    */       
/* 42 */       if (xHotspot < 0 || xHotspot >= pixmap.getWidth()) {
/* 43 */         throw new GdxRuntimeException("xHotspot coordinate of " + xHotspot + " is not within image width bounds: [0, " + pixmap
/* 44 */             .getWidth() + ").");
/*    */       }
/*    */       
/* 47 */       if (yHotspot < 0 || yHotspot >= pixmap.getHeight()) {
/* 48 */         throw new GdxRuntimeException("yHotspot coordinate of " + yHotspot + " is not within image height bounds: [0, " + pixmap
/* 49 */             .getHeight() + ").");
/*    */       }
/*    */ 
/*    */       
/* 53 */       IntBuffer pixelBuffer = pixmap.getPixels().asIntBuffer();
/* 54 */       int[] pixelsRGBA = new int[pixelBuffer.capacity()];
/* 55 */       pixelBuffer.get(pixelsRGBA);
/* 56 */       int[] pixelsARGBflipped = new int[pixelBuffer.capacity()];
/*    */       
/* 58 */       if (pixelBuffer.order() == ByteOrder.BIG_ENDIAN) {
/* 59 */         for (int y = 0; y < pixmap.getHeight(); y++) {
/* 60 */           for (int x = 0; x < pixmap.getWidth(); x++) {
/* 61 */             int pixel = pixelsRGBA[x + y * pixmap.getWidth()];
/* 62 */             pixelsARGBflipped[x + (pixmap.getHeight() - 1 - y) * pixmap.getWidth()] = pixel >> 8 & 0xFFFFFF | pixel << 24 & 0xFF000000;
/*    */           } 
/*    */         } 
/*    */       } else {
/*    */         
/* 67 */         for (int y = 0; y < pixmap.getHeight(); y++) {
/* 68 */           for (int x = 0; x < pixmap.getWidth(); x++) {
/* 69 */             int pixel = pixelsRGBA[x + y * pixmap.getWidth()];
/* 70 */             pixelsARGBflipped[x + (pixmap.getHeight() - 1 - y) * pixmap.getWidth()] = (pixel & 0xFF) << 16 | (pixel & 0xFF0000) >> 16 | pixel & 0xFF00FF00;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */ 
/*    */       
/* 76 */       this
/* 77 */         .lwjglCursor = new Cursor(pixmap.getWidth(), pixmap.getHeight(), xHotspot, pixmap.getHeight() - yHotspot - 1, 1, IntBuffer.wrap(pixelsARGBflipped), null);
/* 78 */     } catch (LWJGLException e) {
/* 79 */       throw new GdxRuntimeException("Could not create cursor image.", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void dispose() {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglCursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */