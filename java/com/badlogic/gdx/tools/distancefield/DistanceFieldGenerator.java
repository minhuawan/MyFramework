/*     */ package com.badlogic.gdx.tools.distancefield;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.BufferedImage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DistanceFieldGenerator
/*     */ {
/*  43 */   private Color color = Color.white;
/*  44 */   private int downscale = 1;
/*  45 */   private float spread = 1.0F;
/*     */ 
/*     */   
/*     */   public Color getColor() {
/*  49 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/*  57 */     this.color = color;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDownscale() {
/*  62 */     return this.downscale;
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
/*     */ 
/*     */   
/*     */   public void setDownscale(int downscale) {
/*  76 */     if (downscale <= 0)
/*  77 */       throw new IllegalArgumentException("downscale must be positive"); 
/*  78 */     this.downscale = downscale;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSpread() {
/*  83 */     return this.spread;
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
/*     */   public void setSpread(float spread) {
/*  95 */     if (spread <= 0.0F)
/*  96 */       throw new IllegalArgumentException("spread must be positive"); 
/*  97 */     this.spread = spread;
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
/*     */ 
/*     */   
/*     */   private static int squareDist(int x1, int y1, int x2, int y2) {
/* 111 */     int dx = x1 - x2;
/* 112 */     int dy = y1 - y2;
/* 113 */     return dx * dx + dy * dy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedImage generateDistanceField(BufferedImage inImage) {
/* 132 */     int inWidth = inImage.getWidth();
/* 133 */     int inHeight = inImage.getHeight();
/* 134 */     int outWidth = inWidth / this.downscale;
/* 135 */     int outHeight = inHeight / this.downscale;
/* 136 */     BufferedImage outImage = new BufferedImage(outWidth, outHeight, 6);
/*     */ 
/*     */     
/* 139 */     boolean[][] bitmap = new boolean[inHeight][inWidth]; int y;
/* 140 */     for (y = 0; y < inHeight; y++) {
/* 141 */       for (int x = 0; x < inWidth; x++) {
/* 142 */         bitmap[y][x] = isInside(inImage.getRGB(x, y));
/*     */       }
/*     */     } 
/*     */     
/* 146 */     for (y = 0; y < outHeight; y++) {
/*     */       
/* 148 */       for (int x = 0; x < outWidth; x++) {
/*     */         
/* 150 */         int centerX = x * this.downscale + this.downscale / 2;
/* 151 */         int centerY = y * this.downscale + this.downscale / 2;
/* 152 */         float signedDistance = findSignedDistance(centerX, centerY, bitmap);
/* 153 */         outImage.setRGB(x, y, distanceToRGB(signedDistance));
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     return outImage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isInside(int rgb) {
/* 168 */     return ((rgb & 0x808080) != 0 && (rgb & Integer.MIN_VALUE) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int distanceToRGB(float signedDistance) {
/* 178 */     float alpha = 0.5F + 0.5F * signedDistance / this.spread;
/* 179 */     alpha = Math.min(1.0F, Math.max(0.0F, alpha));
/* 180 */     int alphaByte = (int)(alpha * 255.0F);
/* 181 */     return alphaByte << 24 | this.color.getRGB() & 0xFFFFFF;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float findSignedDistance(int centerX, int centerY, boolean[][] bitmap) {
/* 199 */     int width = (bitmap[0]).length;
/* 200 */     int height = bitmap.length;
/* 201 */     boolean base = bitmap[centerY][centerX];
/*     */     
/* 203 */     int delta = (int)Math.ceil(this.spread);
/* 204 */     int startX = Math.max(0, centerX - delta);
/* 205 */     int endX = Math.min(width - 1, centerX + delta);
/* 206 */     int startY = Math.max(0, centerY - delta);
/* 207 */     int endY = Math.min(height - 1, centerY + delta);
/*     */     
/* 209 */     int closestSquareDist = delta * delta;
/*     */     
/* 211 */     for (int y = startY; y <= endY; y++) {
/*     */       
/* 213 */       for (int x = startX; x <= endX; x++) {
/*     */         
/* 215 */         if (base != bitmap[y][x]) {
/*     */           
/* 217 */           int squareDist = squareDist(centerX, centerY, x, y);
/* 218 */           if (squareDist < closestSquareDist)
/*     */           {
/* 220 */             closestSquareDist = squareDist;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     float closestDist = (float)Math.sqrt(closestSquareDist);
/* 227 */     return (base ? true : -1) * Math.min(closestDist, this.spread);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void usage() {
/* 232 */     System.out.println("Generates a distance field image from a black and white input image.\nThe distance field image contains a solid color and stores the distance\nin the alpha channel.\n\nThe output file format is inferred from the file name.\n\nCommand line arguments: INFILE OUTFILE [OPTION...]\n\nPossible options:\n  --color rrggbb    color of output image (default: ffffff)\n  --downscale n     downscale by factor of n (default: 1)\n  --spread n        edge scan distance (default: 1)\n");
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CommandLineArgumentException
/*     */     extends IllegalArgumentException
/*     */   {
/*     */     public CommandLineArgumentException(String message) {
/* 250 */       super(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 262 */       run(args);
/* 263 */     } catch (CommandLineArgumentException e) {
/* 264 */       System.err.println("Error: " + e.getMessage() + "\n");
/* 265 */       usage();
/* 266 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void run(String[] args) {
/* 276 */     DistanceFieldGenerator generator = new DistanceFieldGenerator();
/* 277 */     String inputFile = null;
/* 278 */     String outputFile = null;
/*     */     
/* 280 */     int i = 0;
/*     */     try {
/* 282 */       for (; i < args.length; i++) {
/* 283 */         String arg = args[i];
/* 284 */         if (arg.startsWith("-")) {
/* 285 */           if ("--help".equals(arg)) {
/* 286 */             usage();
/* 287 */             System.exit(0);
/* 288 */           } else if ("--color".equals(arg)) {
/* 289 */             i++;
/* 290 */             generator.setColor(new Color(Integer.parseInt(args[i], 16)));
/* 291 */           } else if ("--downscale".equals(arg)) {
/* 292 */             i++;
/* 293 */             generator.setDownscale(Integer.parseInt(args[i]));
/* 294 */           } else if ("--spread".equals(arg)) {
/* 295 */             i++;
/* 296 */             generator.setSpread(Float.parseFloat(args[i]));
/*     */           } else {
/* 298 */             throw new CommandLineArgumentException("unknown option " + arg);
/*     */           }
/*     */         
/* 301 */         } else if (inputFile == null) {
/* 302 */           inputFile = arg;
/* 303 */         } else if (outputFile == null) {
/* 304 */           outputFile = arg;
/*     */         } else {
/* 306 */           throw new CommandLineArgumentException("exactly two file names are expected");
/*     */         }
/*     */       
/*     */       } 
/* 310 */     } catch (IndexOutOfBoundsException e) {
/* 311 */       throw new CommandLineArgumentException("option " + args[args.length - 1] + " requires an argument");
/* 312 */     } catch (NumberFormatException e) {
/* 313 */       throw new CommandLineArgumentException(args[i] + " is not a number");
/*     */     } 
/* 315 */     if (inputFile == null) {
/* 316 */       throw new CommandLineArgumentException("no input file specified");
/*     */     }
/* 318 */     if (outputFile == null) {
/* 319 */       throw new CommandLineArgumentException("no output file specified");
/*     */     }
/*     */     
/* 322 */     String outputFormat = outputFile.substring(outputFile.lastIndexOf('.') + 1);
/*     */     
/* 324 */     if (!ImageIO.getImageWritersByFormatName(outputFormat).hasNext()) {
/* 325 */       throw new RuntimeException("No image writers found that can handle the format '" + outputFormat + "'");
/*     */     }
/*     */     
/* 328 */     BufferedImage input = null;
/*     */     try {
/* 330 */       input = ImageIO.read(new File(inputFile));
/* 331 */     } catch (IOException e) {
/* 332 */       System.err.println("Failed to load image: " + e.getMessage());
/*     */     } 
/*     */     
/* 335 */     BufferedImage output = generator.generateDistanceField(input);
/*     */     
/*     */     try {
/* 338 */       ImageIO.write(output, outputFormat, new File(outputFile));
/* 339 */     } catch (IOException e) {
/* 340 */       System.err.println("Failed to write output image: " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\distancefield\DistanceFieldGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */