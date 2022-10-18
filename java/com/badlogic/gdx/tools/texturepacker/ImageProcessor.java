/*     */ package com.badlogic.gdx.tools.texturepacker;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class ImageProcessor
/*     */ {
/*  42 */   private static final BufferedImage emptyImage = new BufferedImage(1, 1, 6);
/*  43 */   private static Pattern indexPattern = Pattern.compile("(.+)_(\\d+)$");
/*     */   
/*     */   private String rootPath;
/*     */   private final TexturePacker.Settings settings;
/*  47 */   private final HashMap<String, TexturePacker.Rect> crcs = new HashMap<String, TexturePacker.Rect>();
/*  48 */   private final Array<TexturePacker.Rect> rects = new Array();
/*  49 */   private float scale = 1.0F;
/*     */ 
/*     */   
/*     */   public ImageProcessor(File rootDir, TexturePacker.Settings settings) {
/*  53 */     this.settings = settings;
/*     */     
/*  55 */     if (rootDir != null) {
/*  56 */       this.rootPath = rootDir.getAbsolutePath().replace('\\', '/');
/*  57 */       if (!this.rootPath.endsWith("/")) this.rootPath += "/"; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ImageProcessor(TexturePacker.Settings settings) {
/*  62 */     this(null, settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addImage(File file) {
/*     */     BufferedImage image;
/*     */     try {
/*  69 */       image = ImageIO.read(file);
/*  70 */     } catch (IOException ex) {
/*  71 */       throw new RuntimeException("Error reading image: " + file, ex);
/*     */     } 
/*  73 */     if (image == null) throw new RuntimeException("Unable to read image: " + file);
/*     */     
/*  75 */     String name = file.getAbsolutePath().replace('\\', '/');
/*     */ 
/*     */     
/*  78 */     if (this.rootPath != null) {
/*  79 */       if (!name.startsWith(this.rootPath)) throw new RuntimeException("Path '" + name + "' does not start with root: " + this.rootPath); 
/*  80 */       name = name.substring(this.rootPath.length());
/*     */     } 
/*     */ 
/*     */     
/*  84 */     int dotIndex = name.lastIndexOf('.');
/*  85 */     if (dotIndex != -1) name = name.substring(0, dotIndex);
/*     */     
/*  87 */     TexturePacker.Rect rect = addImage(image, name);
/*  88 */     if (rect != null && this.settings.limitMemory) rect.unloadImage(file);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public TexturePacker.Rect addImage(BufferedImage image, String name) {
/*  94 */     TexturePacker.Rect rect = processImage(image, name);
/*     */     
/*  96 */     if (rect == null) {
/*  97 */       if (!this.settings.silent) System.out.println("Ignoring blank input image: " + name); 
/*  98 */       return null;
/*     */     } 
/*     */     
/* 101 */     if (this.settings.alias) {
/* 102 */       String crc = hash(rect.getImage(this));
/* 103 */       TexturePacker.Rect existing = this.crcs.get(crc);
/* 104 */       if (existing != null) {
/* 105 */         if (!this.settings.silent) System.out.println(rect.name + " (alias of " + existing.name + ")"); 
/* 106 */         existing.aliases.add(new TexturePacker.Alias(rect));
/* 107 */         return null;
/*     */       } 
/* 109 */       this.crcs.put(crc, rect);
/*     */     } 
/*     */     
/* 112 */     this.rects.add(rect);
/* 113 */     return rect;
/*     */   }
/*     */   
/*     */   public void setScale(float scale) {
/* 117 */     this.scale = scale;
/*     */   }
/*     */   
/*     */   public Array<TexturePacker.Rect> getImages() {
/* 121 */     return this.rects;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 125 */     this.rects.clear();
/* 126 */     this.crcs.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   TexturePacker.Rect processImage(BufferedImage image, String name) {
/* 131 */     if (this.scale <= 0.0F) throw new IllegalArgumentException("scale cannot be <= 0: " + this.scale);
/*     */     
/* 133 */     int width = image.getWidth(), height = image.getHeight();
/*     */     
/* 135 */     if (image.getType() != 6) {
/* 136 */       BufferedImage newImage = new BufferedImage(width, height, 6);
/* 137 */       newImage.getGraphics().drawImage(image, 0, 0, null);
/* 138 */       image = newImage;
/*     */     } 
/*     */     
/* 141 */     boolean isPatch = name.endsWith(".9");
/* 142 */     int[] splits = null, pads = null;
/* 143 */     TexturePacker.Rect rect = null;
/* 144 */     if (isPatch) {
/*     */       
/* 146 */       name = name.substring(0, name.length() - 2);
/* 147 */       splits = getSplits(image, name);
/* 148 */       pads = getPads(image, name, splits);
/*     */       
/* 150 */       width -= 2;
/* 151 */       height -= 2;
/* 152 */       BufferedImage newImage = new BufferedImage(width, height, 6);
/* 153 */       newImage.getGraphics().drawImage(image, 0, 0, width, height, 1, 1, width + 1, height + 1, null);
/* 154 */       image = newImage;
/*     */     } 
/*     */ 
/*     */     
/* 158 */     if (this.scale != 1.0F) {
/* 159 */       int originalWidth = width, originalHeight = height;
/* 160 */       width = Math.max(1, Math.round(width * this.scale));
/* 161 */       height = Math.max(1, Math.round(height * this.scale));
/* 162 */       BufferedImage newImage = new BufferedImage(width, height, 6);
/* 163 */       if (this.scale < 1.0F) {
/* 164 */         newImage.getGraphics().drawImage(image.getScaledInstance(width, height, 16), 0, 0, null);
/*     */       } else {
/* 166 */         Graphics2D g = (Graphics2D)newImage.getGraphics();
/* 167 */         g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/* 168 */         g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
/* 169 */         g.drawImage(image, 0, 0, width, height, null);
/*     */       } 
/* 171 */       image = newImage;
/*     */     } 
/*     */     
/* 174 */     if (isPatch) {
/*     */       
/* 176 */       rect = new TexturePacker.Rect(image, 0, 0, width, height, true);
/* 177 */       rect.splits = splits;
/* 178 */       rect.pads = pads;
/* 179 */       rect.canRotate = false;
/*     */     } else {
/* 181 */       rect = stripWhitespace(image);
/* 182 */       if (rect == null) return null;
/*     */     
/*     */     } 
/*     */     
/* 186 */     int index = -1;
/* 187 */     if (this.settings.useIndexes) {
/* 188 */       Matcher matcher = indexPattern.matcher(name);
/* 189 */       if (matcher.matches()) {
/* 190 */         name = matcher.group(1);
/* 191 */         index = Integer.parseInt(matcher.group(2));
/*     */       } 
/*     */     } 
/*     */     
/* 195 */     rect.name = name;
/* 196 */     rect.index = index;
/* 197 */     return rect;
/*     */   }
/*     */ 
/*     */   
/*     */   private TexturePacker.Rect stripWhitespace(BufferedImage source) {
/* 202 */     WritableRaster alphaRaster = source.getAlphaRaster();
/* 203 */     if (alphaRaster == null || (!this.settings.stripWhitespaceX && !this.settings.stripWhitespaceY))
/* 204 */       return new TexturePacker.Rect(source, 0, 0, source.getWidth(), source.getHeight(), false); 
/* 205 */     byte[] a = new byte[1];
/* 206 */     int top = 0;
/* 207 */     int bottom = source.getHeight();
/* 208 */     if (this.settings.stripWhitespaceX) {
/*     */       int y;
/* 210 */       label81: for (y = 0; y < source.getHeight(); y++) {
/* 211 */         for (int x = 0; x < source.getWidth(); x++) {
/* 212 */           alphaRaster.getDataElements(x, y, a);
/* 213 */           int alpha = a[0];
/* 214 */           if (alpha < 0) alpha += 256; 
/* 215 */           if (alpha > this.settings.alphaThreshold)
/*     */             break label81; 
/* 217 */         }  top++;
/*     */       } 
/*     */       
/* 220 */       for (y = source.getHeight(); --y >= top; ) {
/* 221 */         for (int x = 0; x < source.getWidth(); x++) {
/* 222 */           alphaRaster.getDataElements(x, y, a);
/* 223 */           int alpha = a[0];
/* 224 */           if (alpha < 0) alpha += 256; 
/* 225 */           if (alpha > this.settings.alphaThreshold)
/*     */             // Byte code: goto -> 233 
/* 227 */         }  bottom--;
/*     */       } 
/*     */     } 
/* 230 */     int left = 0;
/* 231 */     int right = source.getWidth();
/* 232 */     if (this.settings.stripWhitespaceY) {
/*     */       int x;
/* 234 */       label82: for (x = 0; x < source.getWidth(); x++) {
/* 235 */         for (int y = top; y < bottom; y++) {
/* 236 */           alphaRaster.getDataElements(x, y, a);
/* 237 */           int alpha = a[0];
/* 238 */           if (alpha < 0) alpha += 256; 
/* 239 */           if (alpha > this.settings.alphaThreshold)
/*     */             break label82; 
/* 241 */         }  left++;
/*     */       } 
/*     */       
/* 244 */       for (x = source.getWidth(); --x >= left; ) {
/* 245 */         for (int y = top; y < bottom; y++) {
/* 246 */           alphaRaster.getDataElements(x, y, a);
/* 247 */           int alpha = a[0];
/* 248 */           if (alpha < 0) alpha += 256; 
/* 249 */           if (alpha > this.settings.alphaThreshold)
/*     */             // Byte code: goto -> 411 
/* 251 */         }  right--;
/*     */       } 
/*     */     } 
/* 254 */     int newWidth = right - left;
/* 255 */     int newHeight = bottom - top;
/* 256 */     if (newWidth <= 0 || newHeight <= 0) {
/* 257 */       if (this.settings.ignoreBlankImages) {
/* 258 */         return null;
/*     */       }
/* 260 */       return new TexturePacker.Rect(emptyImage, 0, 0, 1, 1, false);
/*     */     } 
/* 262 */     return new TexturePacker.Rect(source, left, top, newWidth, newHeight, false);
/*     */   }
/*     */   
/*     */   private static String splitError(int x, int y, int[] rgba, String name) {
/* 266 */     throw new RuntimeException("Invalid " + name + " ninepatch split pixel at " + x + ", " + y + ", rgba: " + rgba[0] + ", " + rgba[1] + ", " + rgba[2] + ", " + rgba[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] getSplits(BufferedImage image, String name) {
/* 273 */     WritableRaster raster = image.getRaster();
/*     */     
/* 275 */     int startX = getSplitPoint(raster, name, 1, 0, true, true);
/* 276 */     int endX = getSplitPoint(raster, name, startX, 0, false, true);
/* 277 */     int startY = getSplitPoint(raster, name, 0, 1, true, false);
/* 278 */     int endY = getSplitPoint(raster, name, 0, startY, false, false);
/*     */ 
/*     */     
/* 281 */     getSplitPoint(raster, name, endX + 1, 0, true, true);
/* 282 */     getSplitPoint(raster, name, 0, endY + 1, true, false);
/*     */ 
/*     */     
/* 285 */     if (startX == 0 && endX == 0 && startY == 0 && endY == 0) return null;
/*     */ 
/*     */     
/* 288 */     if (startX != 0) {
/* 289 */       startX--;
/* 290 */       endX = raster.getWidth() - 2 - endX - 1;
/*     */     } else {
/*     */       
/* 293 */       endX = raster.getWidth() - 2;
/*     */     } 
/* 295 */     if (startY != 0) {
/* 296 */       startY--;
/* 297 */       endY = raster.getHeight() - 2 - endY - 1;
/*     */     } else {
/*     */       
/* 300 */       endY = raster.getHeight() - 2;
/*     */     } 
/*     */     
/* 303 */     if (this.scale != 1.0F) {
/* 304 */       startX = Math.round(startX * this.scale);
/* 305 */       endX = Math.round(endX * this.scale);
/* 306 */       startY = Math.round(startY * this.scale);
/* 307 */       endY = Math.round(endY * this.scale);
/*     */     } 
/*     */     
/* 310 */     return new int[] { startX, endX, startY, endY };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] getPads(BufferedImage image, String name, int[] splits) {
/* 316 */     WritableRaster raster = image.getRaster();
/*     */     
/* 318 */     int bottom = raster.getHeight() - 1;
/* 319 */     int right = raster.getWidth() - 1;
/*     */     
/* 321 */     int startX = getSplitPoint(raster, name, 1, bottom, true, true);
/* 322 */     int startY = getSplitPoint(raster, name, right, 1, true, false);
/*     */ 
/*     */     
/* 325 */     int endX = 0;
/* 326 */     int endY = 0;
/* 327 */     if (startX != 0) endX = getSplitPoint(raster, name, startX + 1, bottom, false, true); 
/* 328 */     if (startY != 0) endY = getSplitPoint(raster, name, right, startY + 1, false, false);
/*     */ 
/*     */     
/* 331 */     getSplitPoint(raster, name, endX + 1, bottom, true, true);
/* 332 */     getSplitPoint(raster, name, right, endY + 1, true, false);
/*     */ 
/*     */     
/* 335 */     if (startX == 0 && endX == 0 && startY == 0 && endY == 0) {
/* 336 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 340 */     if (startX == 0 && endX == 0) {
/* 341 */       startX = -1;
/* 342 */       endX = -1;
/*     */     }
/* 344 */     else if (startX > 0) {
/* 345 */       startX--;
/* 346 */       endX = raster.getWidth() - 2 - endX - 1;
/*     */     } else {
/*     */       
/* 349 */       endX = raster.getWidth() - 2;
/*     */     } 
/*     */     
/* 352 */     if (startY == 0 && endY == 0) {
/* 353 */       startY = -1;
/* 354 */       endY = -1;
/*     */     }
/* 356 */     else if (startY > 0) {
/* 357 */       startY--;
/* 358 */       endY = raster.getHeight() - 2 - endY - 1;
/*     */     } else {
/*     */       
/* 361 */       endY = raster.getHeight() - 2;
/*     */     } 
/*     */ 
/*     */     
/* 365 */     if (this.scale != 1.0F) {
/* 366 */       startX = Math.round(startX * this.scale);
/* 367 */       endX = Math.round(endX * this.scale);
/* 368 */       startY = Math.round(startY * this.scale);
/* 369 */       endY = Math.round(endY * this.scale);
/*     */     } 
/*     */     
/* 372 */     int[] pads = { startX, endX, startY, endY };
/*     */     
/* 374 */     if (splits != null && Arrays.equals(pads, splits)) {
/* 375 */       return null;
/*     */     }
/*     */     
/* 378 */     return pads;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getSplitPoint(WritableRaster raster, String name, int startX, int startY, boolean startPoint, boolean xAxis) {
/* 386 */     int[] rgba = new int[4];
/*     */     
/* 388 */     int next = xAxis ? startX : startY;
/* 389 */     int end = xAxis ? raster.getWidth() : raster.getHeight();
/* 390 */     int breakA = startPoint ? 255 : 0;
/*     */     
/* 392 */     int x = startX;
/* 393 */     int y = startY;
/* 394 */     while (next != end) {
/* 395 */       if (xAxis) {
/* 396 */         x = next;
/*     */       } else {
/* 398 */         y = next;
/*     */       } 
/* 400 */       raster.getPixel(x, y, rgba);
/* 401 */       if (rgba[3] == breakA) return next;
/*     */       
/* 403 */       if (!startPoint && (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0 || rgba[3] != 255)) splitError(x, y, rgba, name);
/*     */       
/* 405 */       next++;
/*     */     } 
/*     */     
/* 408 */     return 0;
/*     */   }
/*     */   
/*     */   private static String hash(BufferedImage image) {
/*     */     try {
/* 413 */       MessageDigest digest = MessageDigest.getInstance("SHA1");
/*     */ 
/*     */       
/* 416 */       int width = image.getWidth();
/* 417 */       int height = image.getHeight();
/* 418 */       if (image.getType() != 2) {
/* 419 */         BufferedImage newImage = new BufferedImage(width, height, 2);
/* 420 */         newImage.getGraphics().drawImage(image, 0, 0, null);
/* 421 */         image = newImage;
/*     */       } 
/*     */       
/* 424 */       WritableRaster raster = image.getRaster();
/* 425 */       int[] pixels = new int[width];
/* 426 */       for (int y = 0; y < height; y++) {
/* 427 */         raster.getDataElements(0, y, width, 1, pixels);
/* 428 */         for (int x = 0; x < width; x++) {
/* 429 */           hash(digest, pixels[x]);
/*     */         }
/*     */       } 
/* 432 */       hash(digest, width);
/* 433 */       hash(digest, height);
/*     */       
/* 435 */       return (new BigInteger(1, digest.digest())).toString(16);
/* 436 */     } catch (NoSuchAlgorithmException ex) {
/* 437 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void hash(MessageDigest digest, int value) {
/* 442 */     digest.update((byte)(value >> 24));
/* 443 */     digest.update((byte)(value >> 16));
/* 444 */     digest.update((byte)(value >> 8));
/* 445 */     digest.update((byte)value);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\ImageProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */