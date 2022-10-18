/*     */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*     */ 
/*     */ import com.badlogic.gdx.tools.distancefield.DistanceFieldGenerator;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DistanceFieldEffect
/*     */   implements ConfigurableEffect
/*     */ {
/*  45 */   private Color color = Color.WHITE;
/*  46 */   private int scale = 1;
/*  47 */   private float spread = 1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawGlyph(BufferedImage image, Glyph glyph) {
/*  56 */     Graphics2D inputG = (Graphics2D)image.getGraphics();
/*  57 */     inputG.setTransform(AffineTransform.getScaleInstance(this.scale, this.scale));
/*     */ 
/*     */     
/*  60 */     inputG.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
/*  61 */     inputG.setColor(Color.WHITE);
/*  62 */     inputG.fill(glyph.getShape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
/*  69 */     BufferedImage input = new BufferedImage(this.scale * glyph.getWidth(), this.scale * glyph.getHeight(), 12);
/*     */     
/*  71 */     drawGlyph(input, glyph);
/*     */     
/*  73 */     DistanceFieldGenerator generator = new DistanceFieldGenerator();
/*  74 */     generator.setColor(this.color);
/*  75 */     generator.setDownscale(this.scale);
/*     */ 
/*     */     
/*  78 */     generator.setSpread(this.scale * this.spread);
/*  79 */     BufferedImage distanceField = generator.generateDistanceField(input);
/*     */     
/*  81 */     g.drawImage(distanceField, new AffineTransform(), (ImageObserver)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  86 */     return "Distance field";
/*     */   }
/*     */ 
/*     */   
/*     */   public List getValues() {
/*  91 */     List<ConfigurableEffect.Value> values = new ArrayList();
/*  92 */     values.add(EffectUtil.colorValue("Color", this.color));
/*  93 */     values.add(EffectUtil.intValue("Scale", this.scale, "The distance field is computed from an image larger than the output glyph by this factor. Set this to a higher value for more accuracy, but slower font generation."));
/*  94 */     values.add(EffectUtil.floatValue("Spread", this.spread, 1.0F, Float.MAX_VALUE, "The maximum distance from edges where the effect of the distance field is seen. Set this to about half the width of lines in your output font."));
/*  95 */     return values;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValues(List values) {
/* 100 */     for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/* 101 */       ConfigurableEffect.Value value = iter.next();
/* 102 */       if ("Color".equals(value.getName())) {
/* 103 */         this.color = (Color)value.getObject(); continue;
/* 104 */       }  if ("Scale".equals(value.getName())) {
/* 105 */         this.scale = Math.max(1, ((Integer)value.getObject()).intValue()); continue;
/* 106 */       }  if ("Spread".equals(value.getName()))
/* 107 */         this.spread = Math.max(0.0F, ((Float)value.getObject()).floatValue()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\DistanceFieldEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */