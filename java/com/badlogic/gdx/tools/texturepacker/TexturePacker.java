/*     */ package com.badlogic.gdx.tools.texturepacker;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.imageio.IIOImage;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.imageio.ImageWriteParam;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.stream.ImageOutputStream;
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
/*     */ public class TexturePacker
/*     */ {
/*     */   private final Settings settings;
/*     */   private final Packer packer;
/*     */   private final ImageProcessor imageProcessor;
/*  55 */   private final Array<InputImage> inputImages = new Array();
/*     */   
/*     */   private File rootDir;
/*     */   
/*     */   public TexturePacker(File rootDir, Settings settings) {
/*  60 */     this.rootDir = rootDir;
/*  61 */     this.settings = settings;
/*     */     
/*  63 */     if (settings.pot) {
/*  64 */       if (settings.maxWidth != MathUtils.nextPowerOfTwo(settings.maxWidth))
/*  65 */         throw new RuntimeException("If pot is true, maxWidth must be a power of two: " + settings.maxWidth); 
/*  66 */       if (settings.maxHeight != MathUtils.nextPowerOfTwo(settings.maxHeight)) {
/*  67 */         throw new RuntimeException("If pot is true, maxHeight must be a power of two: " + settings.maxHeight);
/*     */       }
/*     */     } 
/*  70 */     if (settings.grid) {
/*  71 */       this.packer = new GridPacker(settings);
/*     */     } else {
/*  73 */       this.packer = new MaxRectsPacker(settings);
/*  74 */     }  this.imageProcessor = new ImageProcessor(rootDir, settings);
/*     */   }
/*     */   
/*     */   public TexturePacker(Settings settings) {
/*  78 */     this(null, settings);
/*     */   }
/*     */   
/*     */   public void addImage(File file) {
/*  82 */     InputImage inputImage = new InputImage();
/*  83 */     inputImage.file = file;
/*  84 */     this.inputImages.add(inputImage);
/*     */   }
/*     */   
/*     */   public void addImage(BufferedImage image, String name) {
/*  88 */     InputImage inputImage = new InputImage();
/*  89 */     inputImage.image = image;
/*  90 */     inputImage.name = name;
/*  91 */     this.inputImages.add(inputImage);
/*     */   }
/*     */   
/*     */   public void pack(File outputDir, String packFileName) {
/*  95 */     if (packFileName.endsWith(this.settings.atlasExtension))
/*  96 */       packFileName = packFileName.substring(0, packFileName.length() - this.settings.atlasExtension.length()); 
/*  97 */     outputDir.mkdirs();
/*     */     
/*  99 */     for (int i = 0, n = this.settings.scale.length; i < n; i++) {
/* 100 */       this.imageProcessor.setScale(this.settings.scale[i]);
/* 101 */       for (InputImage inputImage : this.inputImages) {
/* 102 */         if (inputImage.file != null) {
/* 103 */           this.imageProcessor.addImage(inputImage.file); continue;
/*     */         } 
/* 105 */         this.imageProcessor.addImage(inputImage.image, inputImage.name);
/*     */       } 
/*     */       
/* 108 */       Array<Page> pages = this.packer.pack(this.imageProcessor.getImages());
/*     */       
/* 110 */       String scaledPackFileName = this.settings.getScaledPackFileName(packFileName, i);
/* 111 */       writeImages(outputDir, scaledPackFileName, pages);
/*     */       try {
/* 113 */         writePackFile(outputDir, scaledPackFileName, pages);
/* 114 */       } catch (IOException ex) {
/* 115 */         throw new RuntimeException("Error writing pack file.", ex);
/*     */       } 
/* 117 */       this.imageProcessor.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeImages(File outputDir, String scaledPackFileName, Array<Page> pages) {
/* 122 */     File packFileNoExt = new File(outputDir, scaledPackFileName);
/* 123 */     File packDir = packFileNoExt.getParentFile();
/* 124 */     String imageName = packFileNoExt.getName();
/*     */     
/* 126 */     int fileIndex = 0;
/* 127 */     for (Page page : pages) {
/* 128 */       File outputFile; int width = page.width, height = page.height;
/* 129 */       int paddingX = this.settings.paddingX;
/* 130 */       int paddingY = this.settings.paddingY;
/* 131 */       if (this.settings.duplicatePadding) {
/* 132 */         paddingX /= 2;
/* 133 */         paddingY /= 2;
/*     */       } 
/* 135 */       width -= this.settings.paddingX;
/* 136 */       height -= this.settings.paddingY;
/* 137 */       if (this.settings.edgePadding) {
/* 138 */         page.x = paddingX;
/* 139 */         page.y = paddingY;
/* 140 */         width += paddingX * 2;
/* 141 */         height += paddingY * 2;
/*     */       } 
/* 143 */       if (this.settings.pot) {
/* 144 */         width = MathUtils.nextPowerOfTwo(width);
/* 145 */         height = MathUtils.nextPowerOfTwo(height);
/*     */       } 
/* 147 */       width = Math.max(this.settings.minWidth, width);
/* 148 */       height = Math.max(this.settings.minHeight, height);
/* 149 */       page.imageWidth = width;
/* 150 */       page.imageHeight = height;
/*     */ 
/*     */       
/*     */       do {
/* 154 */         outputFile = new File(packDir, imageName + ((fileIndex++ == 0) ? "" : (String)Integer.valueOf(fileIndex)) + "." + this.settings.outputFormat);
/* 155 */       } while (outputFile.exists());
/*     */       
/* 157 */       (new FileHandle(outputFile)).parent().mkdirs();
/* 158 */       page.imageName = outputFile.getName();
/*     */       
/* 160 */       BufferedImage canvas = new BufferedImage(width, height, getBufferedImageType(this.settings.format));
/* 161 */       Graphics2D g = (Graphics2D)canvas.getGraphics();
/*     */       
/* 163 */       if (!this.settings.silent) System.out.println("Writing " + canvas.getWidth() + "x" + canvas.getHeight() + ": " + outputFile);
/*     */       
/* 165 */       for (Rect rect : page.outputRects) {
/* 166 */         BufferedImage image = rect.getImage(this.imageProcessor);
/* 167 */         int iw = image.getWidth();
/* 168 */         int ih = image.getHeight();
/* 169 */         int rectX = page.x + rect.x, rectY = page.y + page.height - rect.y - rect.height;
/* 170 */         if (this.settings.duplicatePadding) {
/* 171 */           int amountX = this.settings.paddingX / 2;
/* 172 */           int amountY = this.settings.paddingY / 2;
/* 173 */           if (rect.rotated) {
/*     */             int i;
/* 175 */             for (i = 1; i <= amountX; i++) {
/* 176 */               for (int j = 1; j <= amountY; j++) {
/* 177 */                 plot(canvas, rectX - j, rectY + iw - 1 + i, image.getRGB(0, 0));
/* 178 */                 plot(canvas, rectX + ih - 1 + j, rectY + iw - 1 + i, image.getRGB(0, ih - 1));
/* 179 */                 plot(canvas, rectX - j, rectY - i, image.getRGB(iw - 1, 0));
/* 180 */                 plot(canvas, rectX + ih - 1 + j, rectY - i, image.getRGB(iw - 1, ih - 1));
/*     */               } 
/*     */             } 
/*     */             
/* 184 */             for (i = 1; i <= amountY; i++) {
/* 185 */               for (int j = 0; j < iw; j++) {
/* 186 */                 plot(canvas, rectX - i, rectY + iw - 1 - j, image.getRGB(j, 0));
/* 187 */                 plot(canvas, rectX + ih - 1 + i, rectY + iw - 1 - j, image.getRGB(j, ih - 1));
/*     */               } 
/*     */             } 
/* 190 */             for (i = 1; i <= amountX; i++) {
/* 191 */               for (int j = 0; j < ih; j++) {
/* 192 */                 plot(canvas, rectX + j, rectY - i, image.getRGB(iw - 1, j));
/* 193 */                 plot(canvas, rectX + j, rectY + iw - 1 + i, image.getRGB(0, j));
/*     */               } 
/*     */             } 
/*     */           } else {
/*     */             int i;
/* 198 */             for (i = 1; i <= amountX; i++) {
/* 199 */               for (int j = 1; j <= amountY; j++) {
/* 200 */                 plot(canvas, rectX - i, rectY - j, image.getRGB(0, 0));
/* 201 */                 plot(canvas, rectX - i, rectY + ih - 1 + j, image.getRGB(0, ih - 1));
/* 202 */                 plot(canvas, rectX + iw - 1 + i, rectY - j, image.getRGB(iw - 1, 0));
/* 203 */                 plot(canvas, rectX + iw - 1 + i, rectY + ih - 1 + j, image.getRGB(iw - 1, ih - 1));
/*     */               } 
/*     */             } 
/*     */             
/* 207 */             for (i = 1; i <= amountY; i++) {
/* 208 */               copy(image, 0, 0, iw, 1, canvas, rectX, rectY - i, rect.rotated);
/* 209 */               copy(image, 0, ih - 1, iw, 1, canvas, rectX, rectY + ih - 1 + i, rect.rotated);
/*     */             } 
/* 211 */             for (i = 1; i <= amountX; i++) {
/* 212 */               copy(image, 0, 0, 1, ih, canvas, rectX - i, rectY, rect.rotated);
/* 213 */               copy(image, iw - 1, 0, 1, ih, canvas, rectX + iw - 1 + i, rectY, rect.rotated);
/*     */             } 
/*     */           } 
/*     */         } 
/* 217 */         copy(image, 0, 0, iw, ih, canvas, rectX, rectY, rect.rotated);
/* 218 */         if (this.settings.debug) {
/* 219 */           g.setColor(Color.magenta);
/* 220 */           g.drawRect(rectX, rectY, rect.width - this.settings.paddingX - 1, rect.height - this.settings.paddingY - 1);
/*     */         } 
/*     */       } 
/*     */       
/* 224 */       if (this.settings.bleed && !this.settings.premultiplyAlpha && 
/* 225 */         !this.settings.outputFormat.equalsIgnoreCase("jpg") && !this.settings.outputFormat.equalsIgnoreCase("jpeg")) {
/* 226 */         canvas = (new ColorBleedEffect()).processImage(canvas, 2);
/* 227 */         g = (Graphics2D)canvas.getGraphics();
/*     */       } 
/*     */       
/* 230 */       if (this.settings.debug) {
/* 231 */         g.setColor(Color.magenta);
/* 232 */         g.drawRect(0, 0, width - 1, height - 1);
/*     */       } 
/*     */       
/* 235 */       ImageOutputStream ios = null;
/*     */       try {
/* 237 */         if (this.settings.outputFormat.equalsIgnoreCase("jpg") || this.settings.outputFormat.equalsIgnoreCase("jpeg")) {
/* 238 */           BufferedImage newImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), 5);
/* 239 */           newImage.getGraphics().drawImage(canvas, 0, 0, null);
/* 240 */           canvas = newImage;
/*     */           
/* 242 */           Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
/* 243 */           ImageWriter writer = writers.next();
/* 244 */           ImageWriteParam param = writer.getDefaultWriteParam();
/* 245 */           param.setCompressionMode(2);
/* 246 */           param.setCompressionQuality(this.settings.jpegQuality);
/* 247 */           ios = ImageIO.createImageOutputStream(outputFile);
/* 248 */           writer.setOutput(ios);
/* 249 */           writer.write(null, new IIOImage(canvas, null, null), param);
/*     */         } else {
/* 251 */           if (this.settings.premultiplyAlpha) canvas.getColorModel().coerceData(canvas.getRaster(), true); 
/* 252 */           ImageIO.write(canvas, "png", outputFile);
/*     */         } 
/* 254 */       } catch (IOException ex) {
/* 255 */         throw new RuntimeException("Error writing file: " + outputFile, ex);
/*     */       } finally {
/* 257 */         if (ios != null) {
/*     */           try {
/* 259 */             ios.close();
/* 260 */           } catch (Exception exception) {}
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void plot(BufferedImage dst, int x, int y, int argb) {
/* 268 */     if (0 <= x && x < dst.getWidth() && 0 <= y && y < dst.getHeight()) dst.setRGB(x, y, argb); 
/*     */   }
/*     */   
/*     */   private static void copy(BufferedImage src, int x, int y, int w, int h, BufferedImage dst, int dx, int dy, boolean rotated) {
/* 272 */     if (rotated)
/* 273 */     { for (int i = 0; i < w; i++) {
/* 274 */         for (int j = 0; j < h; j++)
/* 275 */           plot(dst, dx + j, dy + w - i - 1, src.getRGB(x + i, y + j)); 
/*     */       }  }
/* 277 */     else { for (int i = 0; i < w; i++) {
/* 278 */         for (int j = 0; j < h; j++)
/* 279 */           plot(dst, dx + i, dy + j, src.getRGB(x + i, y + j)); 
/*     */       }  }
/*     */   
/*     */   }
/*     */   private void writePackFile(File outputDir, String scaledPackFileName, Array<Page> pages) throws IOException {
/* 284 */     File packFile = new File(outputDir, scaledPackFileName + this.settings.atlasExtension);
/* 285 */     File packDir = packFile.getParentFile();
/* 286 */     packDir.mkdirs();
/*     */     
/* 288 */     if (packFile.exists()) {
/*     */       
/* 290 */       TextureAtlas.TextureAtlasData textureAtlasData = new TextureAtlas.TextureAtlasData(new FileHandle(packFile), new FileHandle(packFile), false);
/* 291 */       for (Page page : pages) {
/* 292 */         for (Rect rect : page.outputRects) {
/* 293 */           String rectName = Rect.getAtlasName(rect.name, this.settings.flattenPaths);
/* 294 */           for (TextureAtlas.TextureAtlasData.Region region : textureAtlasData.getRegions()) {
/* 295 */             if (region.name.equals(rectName)) {
/* 296 */               throw new GdxRuntimeException("A region with the name \"" + rectName + "\" has already been packed: " + rect.name);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 304 */     Writer writer = new OutputStreamWriter(new FileOutputStream(packFile, true), "UTF-8");
/* 305 */     for (Page page : pages) {
/* 306 */       writer.write("\n" + page.imageName + "\n");
/* 307 */       writer.write("size: " + page.imageWidth + "," + page.imageHeight + "\n");
/* 308 */       writer.write("format: " + this.settings.format + "\n");
/* 309 */       writer.write("filter: " + this.settings.filterMin + "," + this.settings.filterMag + "\n");
/* 310 */       writer.write("repeat: " + getRepeatValue() + "\n");
/*     */       
/* 312 */       page.outputRects.sort();
/* 313 */       for (Rect rect : page.outputRects) {
/* 314 */         writeRect(writer, page, rect, rect.name);
/* 315 */         Array<Alias> aliases = new Array(rect.aliases.toArray());
/* 316 */         aliases.sort();
/* 317 */         for (Alias alias : aliases) {
/* 318 */           Rect aliasRect = new Rect();
/* 319 */           aliasRect.set(rect);
/* 320 */           alias.apply(aliasRect);
/* 321 */           writeRect(writer, page, aliasRect, alias.name);
/*     */         } 
/*     */       } 
/*     */     } 
/* 325 */     writer.close();
/*     */   }
/*     */   
/*     */   private void writeRect(Writer writer, Page page, Rect rect, String name) throws IOException {
/* 329 */     writer.write(Rect.getAtlasName(name, this.settings.flattenPaths) + "\n");
/* 330 */     writer.write("  rotate: " + rect.rotated + "\n");
/* 331 */     writer.write("  xy: " + (page.x + rect.x) + ", " + (page.y + page.height - rect.height - rect.y) + "\n");
/*     */     
/* 333 */     writer.write("  size: " + rect.regionWidth + ", " + rect.regionHeight + "\n");
/* 334 */     if (rect.splits != null) {
/* 335 */       writer.write("  split: " + rect.splits[0] + ", " + rect.splits[1] + ", " + rect.splits[2] + ", " + rect.splits[3] + "\n");
/*     */     }
/*     */     
/* 338 */     if (rect.pads != null) {
/* 339 */       if (rect.splits == null) writer.write("  split: 0, 0, 0, 0\n"); 
/* 340 */       writer.write("  pad: " + rect.pads[0] + ", " + rect.pads[1] + ", " + rect.pads[2] + ", " + rect.pads[3] + "\n");
/*     */     } 
/* 342 */     writer.write("  orig: " + rect.originalWidth + ", " + rect.originalHeight + "\n");
/* 343 */     writer.write("  offset: " + rect.offsetX + ", " + (rect.originalHeight - rect.regionHeight - rect.offsetY) + "\n");
/* 344 */     writer.write("  index: " + rect.index + "\n");
/*     */   }
/*     */   
/*     */   private String getRepeatValue() {
/* 348 */     if (this.settings.wrapX == Texture.TextureWrap.Repeat && this.settings.wrapY == Texture.TextureWrap.Repeat) return "xy"; 
/* 349 */     if (this.settings.wrapX == Texture.TextureWrap.Repeat && this.settings.wrapY == Texture.TextureWrap.ClampToEdge) return "x"; 
/* 350 */     if (this.settings.wrapX == Texture.TextureWrap.ClampToEdge && this.settings.wrapY == Texture.TextureWrap.Repeat) return "y"; 
/* 351 */     return "none";
/*     */   }
/*     */   
/*     */   private int getBufferedImageType(Pixmap.Format format) {
/* 355 */     switch (this.settings.format) {
/*     */       case RGBA8888:
/*     */       case RGBA4444:
/* 358 */         return 2;
/*     */       case RGB565:
/*     */       case RGB888:
/* 361 */         return 1;
/*     */       case Alpha:
/* 363 */         return 10;
/*     */     } 
/* 365 */     throw new RuntimeException("Unsupported format: " + this.settings.format);
/*     */   }
/*     */   public static class Page {
/*     */     public String imageName; public Array<TexturePacker.Rect> outputRects; public Array<TexturePacker.Rect> remainingRects;
/*     */     public float occupancy;
/*     */     public int x;
/*     */     public int y;
/*     */     public int width;
/*     */     public int height;
/*     */     public int imageWidth;
/*     */     public int imageHeight; }
/*     */   
/*     */   public static class Alias implements Comparable<Alias> { public String name;
/*     */     public int index;
/*     */     public int[] splits;
/*     */     public int[] pads;
/*     */     public int offsetX;
/*     */     public int offsetY;
/*     */     public int originalWidth;
/*     */     public int originalHeight;
/*     */     
/*     */     public Alias(TexturePacker.Rect rect) {
/* 387 */       this.name = rect.name;
/* 388 */       this.index = rect.index;
/* 389 */       this.splits = rect.splits;
/* 390 */       this.pads = rect.pads;
/* 391 */       this.offsetX = rect.offsetX;
/* 392 */       this.offsetY = rect.offsetY;
/* 393 */       this.originalWidth = rect.originalWidth;
/* 394 */       this.originalHeight = rect.originalHeight;
/*     */     }
/*     */     
/*     */     public void apply(TexturePacker.Rect rect) {
/* 398 */       rect.name = this.name;
/* 399 */       rect.index = this.index;
/* 400 */       rect.splits = this.splits;
/* 401 */       rect.pads = this.pads;
/* 402 */       rect.offsetX = this.offsetX;
/* 403 */       rect.offsetY = this.offsetY;
/* 404 */       rect.originalWidth = this.originalWidth;
/* 405 */       rect.originalHeight = this.originalHeight;
/*     */     }
/*     */     
/*     */     public int compareTo(Alias o) {
/* 409 */       return this.name.compareTo(o.name);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Rect
/*     */     implements Comparable<Rect> {
/*     */     public String name;
/*     */     public int offsetX;
/*     */     public int offsetY;
/*     */     public int regionWidth;
/*     */     public int regionHeight;
/*     */     public int originalWidth;
/* 421 */     public Set<TexturePacker.Alias> aliases = new HashSet<TexturePacker.Alias>(); public int originalHeight; public int x; public int y; public int width; public int height; public int index; public boolean rotated;
/*     */     public int[] splits;
/*     */     public int[] pads;
/*     */     public boolean canRotate = true;
/*     */     private boolean isPatch;
/*     */     private BufferedImage image;
/*     */     private File file;
/*     */     int score1;
/*     */     int score2;
/*     */     
/*     */     Rect(BufferedImage source, int left, int top, int newWidth, int newHeight, boolean isPatch) {
/* 432 */       this
/*     */         
/* 434 */         .image = new BufferedImage(source.getColorModel(), source.getRaster().createWritableChild(left, top, newWidth, newHeight, 0, 0, null), source.getColorModel().isAlphaPremultiplied(), null);
/* 435 */       this.offsetX = left;
/* 436 */       this.offsetY = top;
/* 437 */       this.regionWidth = newWidth;
/* 438 */       this.regionHeight = newHeight;
/* 439 */       this.originalWidth = source.getWidth();
/* 440 */       this.originalHeight = source.getHeight();
/* 441 */       this.width = newWidth;
/* 442 */       this.height = newHeight;
/* 443 */       this.isPatch = isPatch;
/*     */     }
/*     */ 
/*     */     
/*     */     public void unloadImage(File file) {
/* 448 */       this.file = file;
/* 449 */       this.image = null;
/*     */     }
/*     */     public BufferedImage getImage(ImageProcessor imageProcessor) {
/*     */       BufferedImage image;
/* 453 */       if (this.image != null) return this.image;
/*     */ 
/*     */       
/*     */       try {
/* 457 */         image = ImageIO.read(this.file);
/* 458 */       } catch (IOException ex) {
/* 459 */         throw new RuntimeException("Error reading image: " + this.file, ex);
/*     */       } 
/* 461 */       if (image == null) throw new RuntimeException("Unable to read image: " + this.file); 
/* 462 */       String name = this.name;
/* 463 */       if (this.isPatch) name = name + ".9"; 
/* 464 */       return imageProcessor.processImage(image, name).getImage(null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Rect(Rect rect) {
/* 471 */       this.x = rect.x;
/* 472 */       this.y = rect.y;
/* 473 */       this.width = rect.width;
/* 474 */       this.height = rect.height;
/*     */     }
/*     */     
/*     */     void set(Rect rect) {
/* 478 */       this.name = rect.name;
/* 479 */       this.image = rect.image;
/* 480 */       this.offsetX = rect.offsetX;
/* 481 */       this.offsetY = rect.offsetY;
/* 482 */       this.regionWidth = rect.regionWidth;
/* 483 */       this.regionHeight = rect.regionHeight;
/* 484 */       this.originalWidth = rect.originalWidth;
/* 485 */       this.originalHeight = rect.originalHeight;
/* 486 */       this.x = rect.x;
/* 487 */       this.y = rect.y;
/* 488 */       this.width = rect.width;
/* 489 */       this.height = rect.height;
/* 490 */       this.index = rect.index;
/* 491 */       this.rotated = rect.rotated;
/* 492 */       this.aliases = rect.aliases;
/* 493 */       this.splits = rect.splits;
/* 494 */       this.pads = rect.pads;
/* 495 */       this.canRotate = rect.canRotate;
/* 496 */       this.score1 = rect.score1;
/* 497 */       this.score2 = rect.score2;
/* 498 */       this.file = rect.file;
/* 499 */       this.isPatch = rect.isPatch;
/*     */     }
/*     */     
/*     */     public int compareTo(Rect o) {
/* 503 */       return this.name.compareTo(o.name);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 508 */       if (this == obj) return true; 
/* 509 */       if (obj == null) return false; 
/* 510 */       if (getClass() != obj.getClass()) return false; 
/* 511 */       Rect other = (Rect)obj;
/* 512 */       if (this.name == null)
/* 513 */       { if (other.name != null) return false;  }
/* 514 */       else if (!this.name.equals(other.name)) { return false; }
/* 515 */        return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 520 */       return this.name + "[" + this.x + "," + this.y + " " + this.width + "x" + this.height + "]";
/*     */     }
/*     */     
/*     */     public static String getAtlasName(String name, boolean flattenPaths) {
/* 524 */       return flattenPaths ? (new FileHandle(name)).name() : name;
/*     */     }
/*     */     
/*     */     Rect() {} }
/*     */   
/*     */   public static class Settings {
/*     */     public boolean pot = true;
/* 531 */     public int paddingX = 2; public int paddingY = 2;
/*     */     public boolean edgePadding = true;
/*     */     public boolean duplicatePadding = false;
/*     */     public boolean rotation;
/* 535 */     public int minWidth = 16, minHeight = 16;
/* 536 */     public int maxWidth = 1024; public int maxHeight = 1024; public boolean square = false;
/*     */     public boolean stripWhitespaceX;
/*     */     public boolean stripWhitespaceY;
/*     */     public int alphaThreshold;
/* 540 */     public Texture.TextureFilter filterMin = Texture.TextureFilter.Nearest; public Texture.TextureFilter filterMag = Texture.TextureFilter.Nearest;
/* 541 */     public Texture.TextureWrap wrapX = Texture.TextureWrap.ClampToEdge; public Texture.TextureWrap wrapY = Texture.TextureWrap.ClampToEdge;
/* 542 */     public Pixmap.Format format = Pixmap.Format.RGBA8888;
/*     */     public boolean alias = true;
/* 544 */     public String outputFormat = "png";
/* 545 */     public float jpegQuality = 0.9F;
/*     */     public boolean ignoreBlankImages = true;
/*     */     public boolean fast;
/*     */     public boolean debug;
/*     */     public boolean silent;
/*     */     public boolean combineSubdirectories;
/*     */     public boolean ignore;
/*     */     public boolean flattenPaths;
/*     */     public boolean premultiplyAlpha;
/*     */     public boolean useIndexes = true;
/*     */     public boolean bleed = true;
/*     */     public boolean limitMemory = true;
/*     */     public boolean grid;
/* 558 */     public float[] scale = new float[] { 1.0F };
/* 559 */     public String[] scaleSuffix = new String[] { "" };
/* 560 */     public String atlasExtension = ".atlas";
/*     */ 
/*     */     
/*     */     public Settings() {}
/*     */ 
/*     */     
/*     */     public Settings(Settings settings) {
/* 567 */       set(settings);
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(Settings settings) {
/* 572 */       this.fast = settings.fast;
/* 573 */       this.rotation = settings.rotation;
/* 574 */       this.pot = settings.pot;
/* 575 */       this.minWidth = settings.minWidth;
/* 576 */       this.minHeight = settings.minHeight;
/* 577 */       this.maxWidth = settings.maxWidth;
/* 578 */       this.maxHeight = settings.maxHeight;
/* 579 */       this.paddingX = settings.paddingX;
/* 580 */       this.paddingY = settings.paddingY;
/* 581 */       this.edgePadding = settings.edgePadding;
/* 582 */       this.duplicatePadding = settings.duplicatePadding;
/* 583 */       this.alphaThreshold = settings.alphaThreshold;
/* 584 */       this.ignoreBlankImages = settings.ignoreBlankImages;
/* 585 */       this.stripWhitespaceX = settings.stripWhitespaceX;
/* 586 */       this.stripWhitespaceY = settings.stripWhitespaceY;
/* 587 */       this.alias = settings.alias;
/* 588 */       this.format = settings.format;
/* 589 */       this.jpegQuality = settings.jpegQuality;
/* 590 */       this.outputFormat = settings.outputFormat;
/* 591 */       this.filterMin = settings.filterMin;
/* 592 */       this.filterMag = settings.filterMag;
/* 593 */       this.wrapX = settings.wrapX;
/* 594 */       this.wrapY = settings.wrapY;
/* 595 */       this.debug = settings.debug;
/* 596 */       this.silent = settings.silent;
/* 597 */       this.combineSubdirectories = settings.combineSubdirectories;
/* 598 */       this.ignore = settings.ignore;
/* 599 */       this.flattenPaths = settings.flattenPaths;
/* 600 */       this.premultiplyAlpha = settings.premultiplyAlpha;
/* 601 */       this.square = settings.square;
/* 602 */       this.useIndexes = settings.useIndexes;
/* 603 */       this.bleed = settings.bleed;
/* 604 */       this.limitMemory = settings.limitMemory;
/* 605 */       this.grid = settings.grid;
/* 606 */       this.scale = settings.scale;
/* 607 */       this.scaleSuffix = settings.scaleSuffix;
/* 608 */       this.atlasExtension = settings.atlasExtension;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getScaledPackFileName(String packFileName, int scaleIndex) {
/* 613 */       if (this.scaleSuffix[scaleIndex].length() > 0) {
/* 614 */         packFileName = packFileName + this.scaleSuffix[scaleIndex];
/*     */       } else {
/*     */         
/* 617 */         float scaleValue = this.scale[scaleIndex];
/* 618 */         if (this.scale.length != 1) {
/* 619 */           packFileName = ((scaleValue == (int)scaleValue) ? Integer.toString((int)scaleValue) : Float.toString(scaleValue)) + "/" + packFileName;
/*     */         }
/*     */       } 
/*     */       
/* 623 */       return packFileName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void process(String input, String output, String packFileName) {
/* 630 */     process(new Settings(), input, output, packFileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void process(Settings settings, String input, String output, String packFileName) {
/*     */     try {
/* 638 */       TexturePackerFileProcessor processor = new TexturePackerFileProcessor(settings, packFileName);
/*     */       
/* 640 */       processor.setComparator(new Comparator<File>() {
/*     */             public int compare(File file1, File file2) {
/* 642 */               return file1.getName().compareTo(file2.getName());
/*     */             }
/*     */           });
/* 645 */       processor.process(new File(input), new File(output));
/* 646 */     } catch (Exception ex) {
/* 647 */       throw new RuntimeException("Error packing images.", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isModified(String input, String output, String packFileName, Settings settings) {
/* 654 */     String packFullFileName = output;
/*     */     
/* 656 */     if (!packFullFileName.endsWith("/")) {
/* 657 */       packFullFileName = packFullFileName + "/";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 662 */     packFullFileName = packFullFileName + packFileName;
/* 663 */     packFullFileName = packFullFileName + settings.atlasExtension;
/* 664 */     File outputFile = new File(packFullFileName);
/*     */     
/* 666 */     if (!outputFile.exists()) {
/* 667 */       return true;
/*     */     }
/*     */     
/* 670 */     File inputFile = new File(input);
/* 671 */     if (!inputFile.exists()) {
/* 672 */       throw new IllegalArgumentException("Input file does not exist: " + inputFile.getAbsolutePath());
/*     */     }
/*     */     
/* 675 */     return isModified(inputFile, outputFile.lastModified());
/*     */   }
/*     */   
/*     */   private static boolean isModified(File file, long lastModified) {
/* 679 */     if (file.lastModified() > lastModified) return true; 
/* 680 */     File[] children = file.listFiles();
/* 681 */     if (children != null)
/* 682 */       for (File child : children) {
/* 683 */         if (isModified(child, lastModified)) return true; 
/*     */       }  
/* 685 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean processIfModified(String input, String output, String packFileName) {
/* 690 */     Settings settings = new Settings();
/*     */     
/* 692 */     if (isModified(input, output, packFileName, settings)) {
/* 693 */       process(settings, input, output, packFileName);
/* 694 */       return true;
/*     */     } 
/* 696 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean processIfModified(Settings settings, String input, String output, String packFileName) {
/* 700 */     if (isModified(input, output, packFileName, settings)) {
/* 701 */       process(settings, input, output, packFileName);
/* 702 */       return true;
/*     */     } 
/* 704 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class InputImage
/*     */   {
/*     */     File file;
/*     */     
/*     */     String name;
/*     */     
/*     */     BufferedImage image;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 718 */     Settings settings = null;
/* 719 */     String input = null, output = null, packFileName = "pack.atlas";
/*     */     
/* 721 */     switch (args.length) {
/*     */       case 4:
/* 723 */         settings = (Settings)(new Json()).fromJson(Settings.class, new FileReader(args[3]));
/*     */       case 3:
/* 725 */         packFileName = args[2];
/*     */       case 2:
/* 727 */         output = args[1];
/*     */       case 1:
/* 729 */         input = args[0];
/*     */         break;
/*     */       default:
/* 732 */         System.out.println("Usage: inputDir [outputDir] [packFileName] [settingsFileName]");
/* 733 */         System.exit(0);
/*     */         break;
/*     */     } 
/* 736 */     if (output == null) {
/* 737 */       File inputFile = new File(input);
/* 738 */       output = (new File(inputFile.getParentFile(), inputFile.getName() + "-packed")).getAbsolutePath();
/*     */     } 
/* 740 */     if (settings == null) settings = new Settings();
/*     */     
/* 742 */     process(settings, input, output, packFileName);
/*     */   }
/*     */   
/*     */   public static interface Packer {
/*     */     Array<TexturePacker.Page> pack(Array<TexturePacker.Rect> param1Array);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\TexturePacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */