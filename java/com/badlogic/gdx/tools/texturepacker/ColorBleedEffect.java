/*     */ package com.badlogic.gdx.tools.texturepacker;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class ColorBleedEffect
/*     */ {
/*  28 */   static int TO_PROCESS = 0;
/*  29 */   static int IN_PROCESS = 1;
/*  30 */   static int REALDATA = 2;
/*  31 */   static int[][] offsets = new int[][] { { -1, -1 }, { 0, -1 }, { 1, -1 }, { -1, 0 }, { 1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
/*     */   
/*  33 */   ARGBColor color = new ARGBColor();
/*     */   
/*     */   public BufferedImage processImage(BufferedImage image, int maxIterations) {
/*  36 */     int width = image.getWidth();
/*  37 */     int height = image.getHeight();
/*     */     
/*  39 */     BufferedImage processedImage = new BufferedImage(width, height, 2);
/*  40 */     int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
/*  41 */     Mask mask = new Mask(rgb);
/*     */     
/*  43 */     int iterations = 0;
/*  44 */     int lastPending = -1;
/*  45 */     while (mask.pendingSize > 0 && mask.pendingSize != lastPending && iterations < maxIterations) {
/*  46 */       lastPending = mask.pendingSize;
/*  47 */       executeIteration(rgb, mask, width, height);
/*  48 */       iterations++;
/*     */     } 
/*     */     
/*  51 */     processedImage.setRGB(0, 0, width, height, rgb, 0, width);
/*  52 */     return processedImage;
/*     */   }
/*     */   
/*     */   private void executeIteration(int[] rgb, Mask mask, int width, int height) {
/*  56 */     mask.getClass(); Mask.MaskIterator iterator = new Mask.MaskIterator();
/*  57 */     while (iterator.hasNext()) {
/*  58 */       int pixelIndex = iterator.next();
/*  59 */       int x = pixelIndex % width;
/*  60 */       int y = pixelIndex / width;
/*  61 */       int r = 0, g = 0, b = 0;
/*  62 */       int count = 0;
/*     */       
/*  64 */       for (int i = 0, n = offsets.length; i < n; i++) {
/*  65 */         int[] offset = offsets[i];
/*  66 */         int column = x + offset[0];
/*  67 */         int row = y + offset[1];
/*     */         
/*  69 */         if (column >= 0 && column < width && row >= 0 && row < height) {
/*     */           
/*  71 */           int currentPixelIndex = getPixelIndex(width, column, row);
/*  72 */           if (mask.getMask(currentPixelIndex) == REALDATA) {
/*  73 */             this.color.argb = rgb[currentPixelIndex];
/*  74 */             r += this.color.red();
/*  75 */             g += this.color.green();
/*  76 */             b += this.color.blue();
/*  77 */             count++;
/*     */           } 
/*     */         } 
/*     */       } 
/*  81 */       if (count != 0) {
/*  82 */         this.color.setARGBA(0, r / count, g / count, b / count);
/*  83 */         rgb[pixelIndex] = this.color.argb;
/*  84 */         iterator.markAsInProgress();
/*     */       } 
/*     */     } 
/*     */     
/*  88 */     iterator.reset();
/*     */   }
/*     */   
/*     */   private int getPixelIndex(int width, int x, int y) {
/*  92 */     return y * width + x;
/*     */   }
/*     */   static class Mask { int[] data; int[] pending;
/*     */     int[] changing;
/*     */     int pendingSize;
/*     */     int changingSize;
/*     */     
/*     */     Mask(int[] rgb) {
/* 100 */       this.data = new int[rgb.length];
/* 101 */       this.pending = new int[rgb.length];
/* 102 */       this.changing = new int[rgb.length];
/* 103 */       ColorBleedEffect.ARGBColor color = new ColorBleedEffect.ARGBColor();
/* 104 */       for (int i = 0; i < rgb.length; i++) {
/* 105 */         color.argb = rgb[i];
/* 106 */         if (color.alpha() == 0) {
/* 107 */           this.data[i] = ColorBleedEffect.TO_PROCESS;
/* 108 */           this.pending[this.pendingSize] = i;
/* 109 */           this.pendingSize++;
/*     */         } else {
/* 111 */           this.data[i] = ColorBleedEffect.REALDATA;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     int getMask(int index) {
/* 116 */       return this.data[index];
/*     */     }
/*     */     
/*     */     int removeIndex(int index) {
/* 120 */       if (index >= this.pendingSize) throw new IndexOutOfBoundsException(String.valueOf(index)); 
/* 121 */       int value = this.pending[index];
/* 122 */       this.pendingSize--;
/* 123 */       this.pending[index] = this.pending[this.pendingSize];
/* 124 */       return value;
/*     */     }
/*     */     
/*     */     class MaskIterator {
/*     */       private int index;
/*     */       
/*     */       boolean hasNext() {
/* 131 */         return (this.index < ColorBleedEffect.Mask.this.pendingSize);
/*     */       }
/*     */       
/*     */       int next() {
/* 135 */         if (this.index >= ColorBleedEffect.Mask.this.pendingSize) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 136 */         return ColorBleedEffect.Mask.this.pending[this.index++];
/*     */       }
/*     */       
/*     */       void markAsInProgress() {
/* 140 */         this.index--;
/* 141 */         ColorBleedEffect.Mask.this.changing[ColorBleedEffect.Mask.this.changingSize] = ColorBleedEffect.Mask.this.removeIndex(this.index);
/* 142 */         ColorBleedEffect.Mask.this.changingSize++;
/*     */       }
/*     */       
/*     */       void reset() {
/* 146 */         this.index = 0;
/* 147 */         for (int i = 0; i < ColorBleedEffect.Mask.this.changingSize; i++) {
/* 148 */           int index = ColorBleedEffect.Mask.this.changing[i];
/* 149 */           ColorBleedEffect.Mask.this.data[index] = ColorBleedEffect.REALDATA;
/*     */         } 
/* 151 */         ColorBleedEffect.Mask.this.changingSize = 0; } } } class MaskIterator { private int index; void reset() { this.index = 0; for (int i = 0; i < ColorBleedEffect.Mask.this.changingSize; i++) { int index = ColorBleedEffect.Mask.this.changing[i]; ColorBleedEffect.Mask.this.data[index] = ColorBleedEffect.REALDATA; }  ColorBleedEffect.Mask.this.changingSize = 0; } boolean hasNext() { return (this.index < ColorBleedEffect.Mask.this.pendingSize); } int next() { if (this.index >= ColorBleedEffect.Mask.this.pendingSize)
/*     */         throw new NoSuchElementException(String.valueOf(this.index));  return ColorBleedEffect.Mask.this.pending[this.index++]; } void markAsInProgress() { this.index--;
/*     */       ColorBleedEffect.Mask.this.changing[ColorBleedEffect.Mask.this.changingSize] = ColorBleedEffect.Mask.this.removeIndex(this.index);
/*     */       ColorBleedEffect.Mask.this.changingSize++; } }
/*     */    static class ARGBColor
/*     */   {
/* 157 */     int argb = -16777216;
/*     */     
/*     */     public int red() {
/* 160 */       return this.argb >> 16 & 0xFF;
/*     */     }
/*     */     
/*     */     public int green() {
/* 164 */       return this.argb >> 8 & 0xFF;
/*     */     }
/*     */     
/*     */     public int blue() {
/* 168 */       return this.argb >> 0 & 0xFF;
/*     */     }
/*     */     
/*     */     public int alpha() {
/* 172 */       return this.argb >> 24 & 0xFF;
/*     */     }
/*     */     
/*     */     public void setARGBA(int a, int r, int g, int b) {
/* 176 */       if (a < 0 || a > 255 || r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255)
/* 177 */         throw new IllegalArgumentException("Invalid RGBA: " + r + ", " + g + "," + b + "," + a); 
/* 178 */       this.argb = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\ColorBleedEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */