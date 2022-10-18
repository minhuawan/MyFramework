/*    */ package com.badlogic.gdx.graphics.g2d;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.PixmapIO;
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.math.Rectangle;
/*    */ import com.badlogic.gdx.utils.ObjectMap;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PixmapPackerIO
/*    */ {
/*    */   public enum ImageFormat
/*    */   {
/* 20 */     CIM(".cim"),
/*    */     
/* 22 */     PNG(".png");
/*    */     
/*    */     private final String extension;
/*    */ 
/*    */     
/*    */     public String getExtension() {
/* 28 */       return this.extension;
/*    */     }
/*    */     
/*    */     ImageFormat(String extension) {
/* 32 */       this.extension = extension;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SaveParameters
/*    */   {
/* 38 */     public PixmapPackerIO.ImageFormat format = PixmapPackerIO.ImageFormat.PNG;
/* 39 */     public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
/* 40 */     public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void save(FileHandle file, PixmapPacker packer) throws IOException {
/* 50 */     save(file, packer, new SaveParameters());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void save(FileHandle file, PixmapPacker packer, SaveParameters parameters) throws IOException {
/* 61 */     Writer writer = file.writer(false);
/* 62 */     int index = 0;
/* 63 */     for (PixmapPacker.Page page : packer.pages) {
/* 64 */       if (page.rects.size > 0) {
/* 65 */         FileHandle pageFile = file.sibling(file.nameWithoutExtension() + "_" + ++index + parameters.format.getExtension());
/* 66 */         switch (parameters.format) {
/*    */           case CIM:
/* 68 */             PixmapIO.writeCIM(pageFile, page.image);
/*    */             break;
/*    */           
/*    */           case PNG:
/* 72 */             PixmapIO.writePNG(pageFile, page.image);
/*    */             break;
/*    */         } 
/*    */         
/* 76 */         writer.write("\n");
/* 77 */         writer.write(pageFile.name() + "\n");
/* 78 */         writer.write("size: " + page.image.getWidth() + "," + page.image.getHeight() + "\n");
/* 79 */         writer.write("format: " + packer.pageFormat.name() + "\n");
/* 80 */         writer.write("filter: " + parameters.minFilter.name() + "," + parameters.magFilter.name() + "\n");
/* 81 */         writer.write("repeat: none\n");
/* 82 */         for (ObjectMap.Keys<String> keys = page.rects.keys().iterator(); keys.hasNext(); ) { String name = keys.next();
/* 83 */           writer.write(name + "\n");
/* 84 */           Rectangle rect = (Rectangle)page.rects.get(name);
/* 85 */           writer.write("rotate: false\n");
/* 86 */           writer.write("xy: " + (int)rect.x + "," + (int)rect.y + "\n");
/* 87 */           writer.write("size: " + (int)rect.width + "," + (int)rect.height + "\n");
/* 88 */           writer.write("orig: " + (int)rect.width + "," + (int)rect.height + "\n");
/* 89 */           writer.write("offset: 0, 0\n");
/* 90 */           writer.write("index: -1\n"); }
/*    */       
/*    */       } 
/*    */     } 
/* 94 */     writer.close();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\PixmapPackerIO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */