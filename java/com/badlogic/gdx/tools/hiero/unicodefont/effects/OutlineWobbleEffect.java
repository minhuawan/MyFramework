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
/*     */ public class OutlineWobbleEffect
/*     */   extends OutlineEffect
/*     */ {
/*  29 */   float detail = 1.0F;
/*  30 */   float amplitude = 1.0F;
/*     */   
/*     */   public OutlineWobbleEffect() {
/*  33 */     setStroke(new WobbleStroke());
/*     */   }
/*     */   
/*     */   public OutlineWobbleEffect(int width, Color color) {
/*  37 */     super(width, color);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  41 */     return "Outline (Wobble)";
/*     */   }
/*     */   
/*     */   public List getValues() {
/*  45 */     List<ConfigurableEffect.Value> values = super.getValues();
/*  46 */     values.remove(2);
/*  47 */     values.add(EffectUtil.floatValue("Detail", this.detail, 1.0F, 50.0F, "This setting controls how detailed the outline will be. Smaller numbers cause the outline to have more detail."));
/*     */     
/*  49 */     values.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5F, 50.0F, "This setting controls the amplitude of the outline."));
/*  50 */     return values;
/*     */   }
/*     */   
/*     */   public void setValues(List values) {
/*  54 */     super.setValues(values);
/*  55 */     for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/*  56 */       ConfigurableEffect.Value value = iter.next();
/*  57 */       if (value.getName().equals("Detail")) {
/*  58 */         this.detail = ((Float)value.getObject()).floatValue(); continue;
/*  59 */       }  if (value.getName().equals("Amplitude"))
/*  60 */         this.amplitude = ((Float)value.getObject()).floatValue(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   class WobbleStroke
/*     */     implements Stroke {
/*     */     private static final float FLATNESS = 1.0F;
/*     */     
/*     */     public Shape createStrokedShape(Shape shape) {
/*  69 */       GeneralPath result = new GeneralPath();
/*  70 */       shape = (new BasicStroke(OutlineWobbleEffect.this.getWidth(), 2, OutlineWobbleEffect.this.getJoin())).createStrokedShape(shape);
/*  71 */       PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1.0D);
/*  72 */       float[] points = new float[6];
/*  73 */       float moveX = 0.0F, moveY = 0.0F;
/*  74 */       float lastX = 0.0F, lastY = 0.0F;
/*  75 */       float thisX = 0.0F, thisY = 0.0F;
/*  76 */       int type = 0;
/*  77 */       float next = 0.0F;
/*  78 */       while (!it.isDone()) {
/*  79 */         float dx, dy, distance; type = it.currentSegment(points);
/*  80 */         switch (type) {
/*     */           case 0:
/*  82 */             moveX = lastX = randomize(points[0]);
/*  83 */             moveY = lastY = randomize(points[1]);
/*  84 */             result.moveTo(moveX, moveY);
/*  85 */             next = 0.0F;
/*     */             break;
/*     */           
/*     */           case 4:
/*  89 */             points[0] = moveX;
/*  90 */             points[1] = moveY;
/*     */ 
/*     */           
/*     */           case 1:
/*  94 */             thisX = randomize(points[0]);
/*  95 */             thisY = randomize(points[1]);
/*  96 */             dx = thisX - lastX;
/*  97 */             dy = thisY - lastY;
/*  98 */             distance = (float)Math.sqrt((dx * dx + dy * dy));
/*  99 */             if (distance >= next) {
/* 100 */               float r = 1.0F / distance;
/* 101 */               while (distance >= next) {
/* 102 */                 float x = lastX + next * dx * r;
/* 103 */                 float y = lastY + next * dy * r;
/* 104 */                 result.lineTo(randomize(x), randomize(y));
/* 105 */                 next += OutlineWobbleEffect.this.detail;
/*     */               } 
/*     */             } 
/* 108 */             next -= distance;
/* 109 */             lastX = thisX;
/* 110 */             lastY = thisY;
/*     */             break;
/*     */         } 
/* 113 */         it.next();
/*     */       } 
/*     */       
/* 116 */       return result;
/*     */     }
/*     */     
/*     */     private float randomize(float x) {
/* 120 */       return x + (float)Math.random() * OutlineWobbleEffect.this.amplitude * 2.0F - 1.0F;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\OutlineWobbleEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */