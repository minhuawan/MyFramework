/*     */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*     */ 
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*     */ import java.awt.Color;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
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
/*     */ public class GradientEffect
/*     */   implements ConfigurableEffect
/*     */ {
/*  33 */   private Color topColor = Color.cyan; private Color bottomColor = Color.blue;
/*  34 */   private int offset = 0;
/*  35 */   private float scale = 1.0F;
/*     */   
/*     */   private boolean cyclic;
/*     */   
/*     */   public GradientEffect() {}
/*     */   
/*     */   public GradientEffect(Color topColor, Color bottomColor, float scale) {
/*  42 */     this.topColor = topColor;
/*  43 */     this.bottomColor = bottomColor;
/*  44 */     this.scale = scale;
/*     */   }
/*     */   
/*     */   public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
/*  48 */     int ascent = unicodeFont.getAscent();
/*  49 */     float height = ascent * this.scale;
/*  50 */     float top = (-glyph.getYOffset() + unicodeFont.getDescent() + this.offset + ascent / 2) - height / 2.0F;
/*  51 */     g.setPaint(new GradientPaint(0.0F, top, this.topColor, 0.0F, top + height, this.bottomColor, this.cyclic));
/*  52 */     g.fill(glyph.getShape());
/*     */   }
/*     */   
/*     */   public Color getTopColor() {
/*  56 */     return this.topColor;
/*     */   }
/*     */   
/*     */   public void setTopColor(Color topColor) {
/*  60 */     this.topColor = topColor;
/*     */   }
/*     */   
/*     */   public Color getBottomColor() {
/*  64 */     return this.bottomColor;
/*     */   }
/*     */   
/*     */   public void setBottomColor(Color bottomColor) {
/*  68 */     this.bottomColor = bottomColor;
/*     */   }
/*     */   
/*     */   public int getOffset() {
/*  72 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOffset(int offset) {
/*  77 */     this.offset = offset;
/*     */   }
/*     */   
/*     */   public float getScale() {
/*  81 */     return this.scale;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scale) {
/*  86 */     this.scale = scale;
/*     */   }
/*     */   
/*     */   public boolean isCyclic() {
/*  90 */     return this.cyclic;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCyclic(boolean cyclic) {
/*  95 */     this.cyclic = cyclic;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  99 */     return "Gradient";
/*     */   }
/*     */   
/*     */   public List getValues() {
/* 103 */     List<ConfigurableEffect.Value> values = new ArrayList();
/* 104 */     values.add(EffectUtil.colorValue("Top color", this.topColor));
/* 105 */     values.add(EffectUtil.colorValue("Bottom color", this.bottomColor));
/* 106 */     values.add(EffectUtil.intValue("Offset", this.offset, "This setting allows you to move the gradient up or down. The gradient is normally centered on the glyph."));
/*     */     
/* 108 */     values.add(EffectUtil.floatValue("Scale", this.scale, 0.0F, 10.0F, "This setting allows you to change the height of the gradient by apercentage. The gradient is normally the height of most glyphs in the font."));
/*     */     
/* 110 */     values.add(EffectUtil.booleanValue("Cyclic", this.cyclic, "If this setting is checked, the gradient will repeat."));
/* 111 */     return values;
/*     */   }
/*     */   
/*     */   public void setValues(List values) {
/* 115 */     for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/* 116 */       ConfigurableEffect.Value value = iter.next();
/* 117 */       if (value.getName().equals("Top color")) {
/* 118 */         this.topColor = (Color)value.getObject(); continue;
/* 119 */       }  if (value.getName().equals("Bottom color")) {
/* 120 */         this.bottomColor = (Color)value.getObject(); continue;
/* 121 */       }  if (value.getName().equals("Offset")) {
/* 122 */         this.offset = ((Integer)value.getObject()).intValue(); continue;
/* 123 */       }  if (value.getName().equals("Scale")) {
/* 124 */         this.scale = ((Float)value.getObject()).floatValue(); continue;
/* 125 */       }  if (value.getName().equals("Cyclic"))
/* 126 */         this.cyclic = ((Boolean)value.getObject()).booleanValue(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\GradientEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */