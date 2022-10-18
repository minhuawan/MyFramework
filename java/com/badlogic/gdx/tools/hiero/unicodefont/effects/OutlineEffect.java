/*     */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*     */ 
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Stroke;
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
/*     */ public class OutlineEffect
/*     */   implements ConfigurableEffect
/*     */ {
/*  34 */   private float width = 2.0F;
/*  35 */   private Color color = Color.black;
/*  36 */   private int join = 2;
/*     */   
/*     */   private Stroke stroke;
/*     */   
/*     */   public OutlineEffect() {}
/*     */   
/*     */   public OutlineEffect(int width, Color color) {
/*  43 */     this.width = width;
/*  44 */     this.color = color;
/*     */   }
/*     */   
/*     */   public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
/*  48 */     g = (Graphics2D)g.create();
/*  49 */     if (this.stroke != null) {
/*  50 */       g.setStroke(this.stroke);
/*     */     } else {
/*  52 */       g.setStroke(getStroke());
/*  53 */     }  g.setColor(this.color);
/*  54 */     g.draw(glyph.getShape());
/*  55 */     g.dispose();
/*     */   }
/*     */   
/*     */   public float getWidth() {
/*  59 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWidth(int width) {
/*  64 */     this.width = width;
/*     */   }
/*     */   
/*     */   public Color getColor() {
/*  68 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/*  72 */     this.color = color;
/*     */   }
/*     */   
/*     */   public int getJoin() {
/*  76 */     return this.join;
/*     */   }
/*     */   
/*     */   public Stroke getStroke() {
/*  80 */     if (this.stroke == null) return new BasicStroke(this.width, 2, this.join); 
/*  81 */     return this.stroke;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStroke(Stroke stroke) {
/*  86 */     this.stroke = stroke;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJoin(int join) {
/*  92 */     this.join = join;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  96 */     return "Outline";
/*     */   }
/*     */   
/*     */   public List getValues() {
/* 100 */     List<ConfigurableEffect.Value> values = new ArrayList();
/* 101 */     values.add(EffectUtil.colorValue("Color", this.color));
/* 102 */     values.add(EffectUtil.floatValue("Width", this.width, 0.1F, 999.0F, "This setting controls the width of the outline. The glyphs will need padding so the outline doesn't get clipped."));
/*     */     
/* 104 */     values.add(EffectUtil.optionValue("Join", String.valueOf(this.join), new String[][] { { "Bevel", "2" }, , { "Miter", "0" }, , { "Round", "1" },  }, "This setting defines how the corners of the outline are drawn. This is usually only noticeable at large outline widths."));
/*     */ 
/*     */ 
/*     */     
/* 108 */     return values;
/*     */   }
/*     */   
/*     */   public void setValues(List values) {
/* 112 */     for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/* 113 */       ConfigurableEffect.Value value = iter.next();
/* 114 */       if (value.getName().equals("Color")) {
/* 115 */         this.color = (Color)value.getObject(); continue;
/* 116 */       }  if (value.getName().equals("Width")) {
/* 117 */         this.width = ((Float)value.getObject()).floatValue(); continue;
/* 118 */       }  if (value.getName().equals("Join"))
/* 119 */         this.join = Integer.parseInt((String)value.getObject()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\OutlineEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */