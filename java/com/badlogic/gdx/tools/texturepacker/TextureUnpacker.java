/*     */ package com.badlogic.gdx.tools.texturepacker;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.AffineTransformOp;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageIO;
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
/*     */ public class TextureUnpacker
/*     */ {
/*     */   private static final String DEFAULT_OUTPUT_PATH = "output";
/*     */   private static final int NINEPATCH_PADDING = 1;
/*     */   private static final String OUTPUT_TYPE = "png";
/*     */   private static final String HELP = "Usage: atlasFile [imageDir] [outputDir]";
/*     */   private static final String ATLAS_FILE_EXTENSION = ".atlas";
/*     */   
/*     */   private int parseArguments(String[] args) {
/*  48 */     int numArgs = args.length;
/*     */     
/*  50 */     if (numArgs < 1) return 0;
/*     */     
/*  52 */     boolean extension = args[0].substring(args[0].length() - ".atlas".length()).equals(".atlas");
/*     */     
/*  54 */     boolean directory = true;
/*  55 */     if (numArgs >= 2) directory &= checkDirectoryValidity(args[1]); 
/*  56 */     if (numArgs == 3) directory &= checkDirectoryValidity(args[2]); 
/*  57 */     return (extension && directory) ? numArgs : 0;
/*     */   }
/*     */   
/*     */   private boolean checkDirectoryValidity(String directory) {
/*  61 */     File checkFile = new File(directory);
/*  62 */     boolean path = true;
/*     */     
/*     */     try {
/*  65 */       checkFile.getCanonicalPath();
/*  66 */     } catch (Exception e) {
/*  67 */       path = false;
/*     */     } 
/*  69 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void splitAtlas(TextureAtlas.TextureAtlasData atlas, String outputDir) {
/*  75 */     File outputDirFile = new File(outputDir);
/*  76 */     if (!outputDirFile.exists()) {
/*  77 */       outputDirFile.mkdirs();
/*  78 */       System.out.println(String.format("Creating directory: %s", new Object[] { outputDirFile.getPath() }));
/*     */     } 
/*     */     
/*  81 */     for (TextureAtlas.TextureAtlasData.Page page : atlas.getPages()) {
/*     */       
/*  83 */       BufferedImage img = null;
/*     */       try {
/*  85 */         img = ImageIO.read(page.textureFile.file());
/*  86 */       } catch (IOException e) {
/*  87 */         printExceptionAndExit(e);
/*     */       } 
/*  89 */       for (TextureAtlas.TextureAtlasData.Region region : atlas.getRegions()) {
/*  90 */         System.out.println(String.format("Processing image for %s: x[%s] y[%s] w[%s] h[%s], rotate[%s]", new Object[] { region.name, 
/*  91 */                 Integer.valueOf(region.left), Integer.valueOf(region.top), Integer.valueOf(region.width), Integer.valueOf(region.height), Boolean.valueOf(region.rotate) }));
/*     */ 
/*     */         
/*  94 */         if (region.page == page) {
/*  95 */           BufferedImage splitImage = null;
/*  96 */           String extension = null;
/*     */ 
/*     */           
/*  99 */           if (region.splits == null) {
/* 100 */             splitImage = extractImage(img, region, outputDirFile, 0);
/* 101 */             extension = "png";
/*     */           } else {
/* 103 */             splitImage = extractNinePatch(img, region, outputDirFile);
/* 104 */             extension = String.format("9.%s", new Object[] { "png" });
/*     */           } 
/*     */ 
/*     */           
/* 108 */           File imgOutput = new File(outputDirFile, String.format("%s.%s", new Object[] { (region.index == -1) ? region.name : (region.name + "_" + region.index), extension }));
/* 109 */           File imgDir = imgOutput.getParentFile();
/* 110 */           if (!imgDir.exists()) {
/* 111 */             System.out.println(String.format("Creating directory: %s", new Object[] { imgDir.getPath() }));
/* 112 */             imgDir.mkdirs();
/*     */           } 
/*     */ 
/*     */           
/*     */           try {
/* 117 */             ImageIO.write(splitImage, "png", imgOutput);
/* 118 */           } catch (IOException e) {
/* 119 */             printExceptionAndExit(e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferedImage extractImage(BufferedImage page, TextureAtlas.TextureAtlasData.Region region, File outputDirFile, int padding) {
/* 133 */     BufferedImage splitImage = null;
/*     */ 
/*     */     
/* 136 */     if (region.rotate) {
/* 137 */       BufferedImage srcImage = page.getSubimage(region.left, region.top, region.height, region.width);
/* 138 */       splitImage = new BufferedImage(region.width, region.height, page.getType());
/*     */       
/* 140 */       AffineTransform transform = new AffineTransform();
/* 141 */       transform.rotate(Math.toRadians(90.0D));
/* 142 */       transform.translate(0.0D, -region.width);
/* 143 */       AffineTransformOp op = new AffineTransformOp(transform, 2);
/* 144 */       op.filter(srcImage, splitImage);
/*     */     } else {
/* 146 */       splitImage = page.getSubimage(region.left, region.top, region.width, region.height);
/*     */     } 
/*     */ 
/*     */     
/* 150 */     if (padding > 0) {
/*     */       
/* 152 */       BufferedImage paddedImage = new BufferedImage(splitImage.getWidth() + padding * 2, splitImage.getHeight() + padding * 2, page.getType());
/* 153 */       Graphics2D g2 = paddedImage.createGraphics();
/* 154 */       g2.drawImage(splitImage, padding, padding, (ImageObserver)null);
/* 155 */       g2.dispose();
/* 156 */       return paddedImage;
/*     */     } 
/* 158 */     return splitImage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferedImage extractNinePatch(BufferedImage page, TextureAtlas.TextureAtlasData.Region region, File outputDirFile) {
/* 167 */     BufferedImage splitImage = extractImage(page, region, outputDirFile, 1);
/* 168 */     Graphics2D g2 = splitImage.createGraphics();
/* 169 */     g2.setColor(Color.BLACK);
/*     */ 
/*     */     
/* 172 */     int startX = region.splits[0] + 1;
/* 173 */     int endX = region.width - region.splits[1] + 1 - 1;
/* 174 */     int startY = region.splits[2] + 1;
/* 175 */     int endY = region.height - region.splits[3] + 1 - 1;
/* 176 */     if (endX >= startX) g2.drawLine(startX, 0, endX, 0); 
/* 177 */     if (endY >= startY) g2.drawLine(0, startY, 0, endY); 
/* 178 */     if (region.pads != null) {
/* 179 */       int padStartX = region.pads[0] + 1;
/* 180 */       int padEndX = region.width - region.pads[1] + 1 - 1;
/* 181 */       int padStartY = region.pads[2] + 1;
/* 182 */       int padEndY = region.height - region.pads[3] + 1 - 1;
/* 183 */       g2.drawLine(padStartX, splitImage.getHeight() - 1, padEndX, splitImage.getHeight() - 1);
/* 184 */       g2.drawLine(splitImage.getWidth() - 1, padStartY, splitImage.getWidth() - 1, padEndY);
/*     */     } 
/* 186 */     g2.dispose();
/*     */     
/* 188 */     return splitImage;
/*     */   }
/*     */   
/*     */   private void printExceptionAndExit(Exception e) {
/* 192 */     e.printStackTrace();
/* 193 */     System.exit(1);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 197 */     TextureUnpacker unpacker = new TextureUnpacker();
/*     */     
/* 199 */     String atlasFile = null, imageDir = null, outputDir = null;
/*     */ 
/*     */     
/* 202 */     switch (unpacker.parseArguments(args)) {
/*     */       case 0:
/* 204 */         System.out.println("Usage: atlasFile [imageDir] [outputDir]");
/*     */         return;
/*     */       case 3:
/* 207 */         outputDir = args[2];
/*     */       case 2:
/* 209 */         imageDir = args[1];
/*     */       case 1:
/* 211 */         atlasFile = args[0];
/*     */         break;
/*     */     } 
/* 214 */     File atlasFileHandle = new File(atlasFile);
/* 215 */     String atlasParentPath = atlasFileHandle.getParentFile().getAbsolutePath();
/*     */ 
/*     */     
/* 218 */     if (imageDir == null) imageDir = atlasParentPath; 
/* 219 */     if (outputDir == null) outputDir = (new File(atlasParentPath, "output")).getAbsolutePath();
/*     */ 
/*     */     
/* 222 */     TextureAtlas.TextureAtlasData atlas = new TextureAtlas.TextureAtlasData(new FileHandle(atlasFile), new FileHandle(imageDir), false);
/* 223 */     unpacker.splitAtlas(atlas, outputDir);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\TextureUnpacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */