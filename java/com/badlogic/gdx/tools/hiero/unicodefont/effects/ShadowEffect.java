/*     */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*     */ 
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ConvolveOp;
/*     */ import java.awt.image.Kernel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class ShadowEffect
/*     */   implements ConfigurableEffect
/*     */ {
/*     */   public static final int NUM_KERNELS = 16;
/*  39 */   public static final float[][] GAUSSIAN_BLUR_KERNELS = generateGaussianBlurKernels(16);
/*     */   
/*  41 */   private Color color = Color.black;
/*  42 */   private float opacity = 0.6F;
/*  43 */   private float xDistance = 2.0F; private float yDistance = 2.0F;
/*  44 */   private int blurKernelSize = 0;
/*  45 */   private int blurPasses = 1;
/*     */ 
/*     */   
/*     */   public ShadowEffect() {}
/*     */   
/*     */   public ShadowEffect(Color color, int xDistance, int yDistance, float opacity) {
/*  51 */     this.color = color;
/*  52 */     this.xDistance = xDistance;
/*  53 */     this.yDistance = yDistance;
/*  54 */     this.opacity = opacity;
/*     */   }
/*     */   
/*     */   public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
/*  58 */     g = (Graphics2D)g.create();
/*  59 */     g.translate(this.xDistance, this.yDistance);
/*  60 */     g.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.round(this.opacity * 255.0F)));
/*  61 */     g.fill(glyph.getShape());
/*     */ 
/*     */     
/*  64 */     for (Iterator<Effect> iter = unicodeFont.getEffects().iterator(); iter.hasNext(); ) {
/*  65 */       Effect effect = iter.next();
/*  66 */       if (effect instanceof OutlineEffect) {
/*  67 */         Composite composite = g.getComposite();
/*  68 */         g.setComposite(AlphaComposite.Src);
/*     */         
/*  70 */         g.setStroke(((OutlineEffect)effect).getStroke());
/*  71 */         g.draw(glyph.getShape());
/*     */         
/*  73 */         g.setComposite(composite);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  78 */     g.dispose();
/*  79 */     if (this.blurKernelSize > 1 && this.blurKernelSize < 16 && this.blurPasses > 0) blur(image); 
/*     */   }
/*     */   
/*     */   private void blur(BufferedImage image) {
/*  83 */     float[] matrix = GAUSSIAN_BLUR_KERNELS[this.blurKernelSize - 1];
/*  84 */     Kernel gaussianBlur1 = new Kernel(matrix.length, 1, matrix);
/*  85 */     Kernel gaussianBlur2 = new Kernel(1, matrix.length, matrix);
/*  86 */     RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
/*  87 */     ConvolveOp gaussianOp1 = new ConvolveOp(gaussianBlur1, 1, hints);
/*  88 */     ConvolveOp gaussianOp2 = new ConvolveOp(gaussianBlur2, 1, hints);
/*  89 */     BufferedImage scratchImage = EffectUtil.getScratchImage();
/*  90 */     for (int i = 0; i < this.blurPasses; i++) {
/*  91 */       gaussianOp1.filter(image, scratchImage);
/*  92 */       gaussianOp2.filter(scratchImage, image);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Color getColor() {
/*  97 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 101 */     this.color = color;
/*     */   }
/*     */   
/*     */   public float getXDistance() {
/* 105 */     return this.xDistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXDistance(float distance) {
/* 110 */     this.xDistance = distance;
/*     */   }
/*     */   
/*     */   public float getYDistance() {
/* 114 */     return this.yDistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYDistance(float distance) {
/* 119 */     this.yDistance = distance;
/*     */   }
/*     */   
/*     */   public int getBlurKernelSize() {
/* 123 */     return this.blurKernelSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlurKernelSize(int blurKernelSize) {
/* 128 */     this.blurKernelSize = blurKernelSize;
/*     */   }
/*     */   
/*     */   public int getBlurPasses() {
/* 132 */     return this.blurPasses;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlurPasses(int blurPasses) {
/* 137 */     this.blurPasses = blurPasses;
/*     */   }
/*     */   
/*     */   public float getOpacity() {
/* 141 */     return this.opacity;
/*     */   }
/*     */   
/*     */   public void setOpacity(float opacity) {
/* 145 */     this.opacity = opacity;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 149 */     return "Shadow";
/*     */   }
/*     */   
/*     */   public List getValues() {
/* 153 */     List<ConfigurableEffect.Value> values = new ArrayList();
/* 154 */     values.add(EffectUtil.colorValue("Color", this.color));
/* 155 */     values.add(EffectUtil.floatValue("Opacity", this.opacity, 0.0F, 1.0F, "This setting sets the translucency of the shadow."));
/* 156 */     values.add(EffectUtil.floatValue("X distance", this.xDistance, -99.0F, 99.0F, "This setting is the amount of pixels to offset the shadow on the x axis. The glyphs will need padding so the shadow doesn't get clipped."));
/*     */     
/* 158 */     values.add(EffectUtil.floatValue("Y distance", this.yDistance, -99.0F, 99.0F, "This setting is the amount of pixels to offset the shadow on the y axis. The glyphs will need padding so the shadow doesn't get clipped."));
/*     */ 
/*     */     
/* 161 */     List<String[]> options = new ArrayList();
/* 162 */     options.add(new String[] { "None", "0" });
/* 163 */     for (int i = 2; i < 16; i++) {
/* 164 */       options.add(new String[] { String.valueOf(i) });
/* 165 */     }  String[][] optionsArray = options.<String[]>toArray(new String[options.size()][]);
/* 166 */     values.add(EffectUtil.optionValue("Blur kernel size", String.valueOf(this.blurKernelSize), optionsArray, "This setting controls how many neighboring pixels are used to blur the shadow. Set to \"None\" for no blur."));
/*     */ 
/*     */     
/* 169 */     values.add(EffectUtil.intValue("Blur passes", this.blurPasses, "The setting is the number of times to apply a blur to the shadow. Set to \"0\" for no blur."));
/*     */     
/* 171 */     return values;
/*     */   }
/*     */   
/*     */   public void setValues(List values) {
/* 175 */     for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/* 176 */       ConfigurableEffect.Value value = iter.next();
/* 177 */       if (value.getName().equals("Color")) {
/* 178 */         this.color = (Color)value.getObject(); continue;
/* 179 */       }  if (value.getName().equals("Opacity")) {
/* 180 */         this.opacity = ((Float)value.getObject()).floatValue(); continue;
/* 181 */       }  if (value.getName().equals("X distance")) {
/* 182 */         this.xDistance = ((Float)value.getObject()).floatValue(); continue;
/* 183 */       }  if (value.getName().equals("Y distance")) {
/* 184 */         this.yDistance = ((Float)value.getObject()).floatValue(); continue;
/* 185 */       }  if (value.getName().equals("Blur kernel size")) {
/* 186 */         this.blurKernelSize = Integer.parseInt((String)value.getObject()); continue;
/* 187 */       }  if (value.getName().equals("Blur passes")) {
/* 188 */         this.blurPasses = ((Integer)value.getObject()).intValue();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float[][] generateGaussianBlurKernels(int level) {
/* 198 */     float[][] pascalsTriangle = generatePascalsTriangle(level);
/* 199 */     float[][] gaussianTriangle = new float[pascalsTriangle.length][];
/* 200 */     for (int i = 0; i < gaussianTriangle.length; i++) {
/* 201 */       float total = 0.0F;
/* 202 */       gaussianTriangle[i] = new float[(pascalsTriangle[i]).length];
/* 203 */       for (int j = 0; j < (pascalsTriangle[i]).length; j++)
/* 204 */         total += pascalsTriangle[i][j]; 
/* 205 */       float coefficient = 1.0F / total;
/* 206 */       for (int k = 0; k < (pascalsTriangle[i]).length; k++)
/* 207 */         gaussianTriangle[i][k] = coefficient * pascalsTriangle[i][k]; 
/*     */     } 
/* 209 */     return gaussianTriangle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float[][] generatePascalsTriangle(int level) {
/* 217 */     if (level < 2) level = 2; 
/* 218 */     float[][] triangle = new float[level][];
/* 219 */     triangle[0] = new float[1];
/* 220 */     triangle[1] = new float[2];
/* 221 */     triangle[0][0] = 1.0F;
/* 222 */     triangle[1][0] = 1.0F;
/* 223 */     triangle[1][1] = 1.0F;
/* 224 */     for (int i = 2; i < level; i++) {
/* 225 */       triangle[i] = new float[i + 1];
/* 226 */       triangle[i][0] = 1.0F;
/* 227 */       triangle[i][i] = 1.0F;
/* 228 */       for (int j = 1; j < (triangle[i]).length - 1; j++)
/* 229 */         triangle[i][j] = triangle[i - 1][j - 1] + triangle[i - 1][j]; 
/*     */     } 
/* 231 */     return triangle;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\ShadowEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */