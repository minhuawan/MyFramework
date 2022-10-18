/*     */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.FlatteningPathIterator;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.PathIterator;
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
/*     */ public class OutlineZigzagEffect
/*     */   extends OutlineEffect
/*     */ {
/*  29 */   float amplitude = 1.0F;
/*  30 */   float wavelength = 3.0F;
/*     */   
/*     */   public OutlineZigzagEffect() {
/*  33 */     setStroke(new ZigzagStroke());
/*     */   }
/*     */   
/*     */   public OutlineZigzagEffect(int width, Color color) {
/*  37 */     super(width, color);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  41 */     return "Outline (Zigzag)";
/*     */   }
/*     */   
/*     */   public List getValues() {
/*  45 */     List<ConfigurableEffect.Value> values = super.getValues();
/*  46 */     values.add(EffectUtil.floatValue("Wavelength", this.wavelength, 1.0F, 100.0F, "This setting controls the wavelength of the outline. The smaller the value, the more segments will be used to draw the outline."));
/*     */     
/*  48 */     values.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5F, 50.0F, "This setting controls the amplitude of the outline. The bigger the value, the more the zigzags will vary."));
/*     */     
/*  50 */     return values;
/*     */   }
/*     */   
/*     */   public void setValues(List values) {
/*  54 */     super.setValues(values);
/*  55 */     for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/*  56 */       ConfigurableEffect.Value value = iter.next();
/*  57 */       if (value.getName().equals("Wavelength")) {
/*  58 */         this.wavelength = ((Float)value.getObject()).floatValue(); continue;
/*  59 */       }  if (value.getName().equals("Amplitude"))
/*  60 */         this.amplitude = ((Float)value.getObject()).floatValue(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   class ZigzagStroke
/*     */     implements Stroke {
/*     */     private static final float FLATNESS = 1.0F;
/*     */     
/*     */     public Shape createStrokedShape(Shape shape) {
/*  69 */       GeneralPath result = new GeneralPath();
/*  70 */       PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1.0D);
/*  71 */       float[] points = new float[6];
/*  72 */       float moveX = 0.0F, moveY = 0.0F;
/*  73 */       float lastX = 0.0F, lastY = 0.0F;
/*  74 */       float thisX = 0.0F, thisY = 0.0F;
/*  75 */       int type = 0;
/*  76 */       float next = 0.0F;
/*  77 */       int phase = 0;
/*  78 */       while (!it.isDone()) {
/*  79 */         float dx, dy, distance; type = it.currentSegment(points);
/*  80 */         switch (type) {
/*     */           case 0:
/*  82 */             moveX = lastX = points[0];
/*  83 */             moveY = lastY = points[1];
/*  84 */             result.moveTo(moveX, moveY);
/*  85 */             next = OutlineZigzagEffect.this.wavelength / 2.0F;
/*     */             break;
/*     */           
/*     */           case 4:
/*  89 */             points[0] = moveX;
/*  90 */             points[1] = moveY;
/*     */ 
/*     */           
/*     */           case 1:
/*  94 */             thisX = points[0];
/*  95 */             thisY = points[1];
/*  96 */             dx = thisX - lastX;
/*  97 */             dy = thisY - lastY;
/*  98 */             distance = (float)Math.sqrt((dx * dx + dy * dy));
/*  99 */             if (distance >= next) {
/* 100 */               float r = 1.0F / distance;
/* 101 */               while (distance >= next) {
/* 102 */                 float x = lastX + next * dx * r;
/* 103 */                 float y = lastY + next * dy * r;
/* 104 */                 if ((phase & 0x1) == 0) {
/* 105 */                   result.lineTo(x + OutlineZigzagEffect.this.amplitude * dy * r, y - OutlineZigzagEffect.this.amplitude * dx * r);
/*     */                 } else {
/* 107 */                   result.lineTo(x - OutlineZigzagEffect.this.amplitude * dy * r, y + OutlineZigzagEffect.this.amplitude * dx * r);
/* 108 */                 }  next += OutlineZigzagEffect.this.wavelength;
/* 109 */                 phase++;
/*     */               } 
/*     */             } 
/* 112 */             next -= distance;
/* 113 */             lastX = thisX;
/* 114 */             lastY = thisY;
/* 115 */             if (type == 4) result.closePath(); 
/*     */             break;
/*     */         } 
/* 118 */         it.next();
/*     */       } 
/* 120 */       return (new BasicStroke(OutlineZigzagEffect.this.getWidth(), 2, OutlineZigzagEffect.this.getJoin())).createStrokedShape(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\OutlineZigzagEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */