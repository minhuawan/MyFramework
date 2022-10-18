/*    */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*    */ 
/*    */ import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
/*    */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ColorEffect
/*    */   implements ConfigurableEffect
/*    */ {
/* 32 */   private Color color = Color.white;
/*    */ 
/*    */   
/*    */   public ColorEffect() {}
/*    */   
/*    */   public ColorEffect(Color color) {
/* 38 */     this.color = color;
/*    */   }
/*    */   
/*    */   public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
/* 42 */     g.setColor(this.color);
/*    */     try {
/* 44 */       g.fill(glyph.getShape());
/* 45 */     } catch (Throwable throwable) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getColor() {
/* 50 */     return this.color;
/*    */   }
/*    */   
/*    */   public void setColor(Color color) {
/* 54 */     if (color == null) throw new IllegalArgumentException("color cannot be null."); 
/* 55 */     this.color = color;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 59 */     return "Color";
/*    */   }
/*    */   
/*    */   public List getValues() {
/* 63 */     List<ConfigurableEffect.Value> values = new ArrayList();
/* 64 */     values.add(EffectUtil.colorValue("Color", this.color));
/* 65 */     return values;
/*    */   }
/*    */   
/*    */   public void setValues(List values) {
/* 69 */     for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/* 70 */       ConfigurableEffect.Value value = iter.next();
/* 71 */       if (value.getName().equals("Color"))
/* 72 */         setColor((Color)value.getObject()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\ColorEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */