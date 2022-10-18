/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Group;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
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
/*     */ public class Widget
/*     */   extends Actor
/*     */   implements Layout
/*     */ {
/*     */   private boolean needsLayout = true;
/*     */   private boolean fillParent;
/*     */   private boolean layoutEnabled = true;
/*     */   
/*     */   public float getMinWidth() {
/*  41 */     return getPrefWidth();
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/*  45 */     return getPrefHeight();
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/*  49 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/*  53 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getMaxWidth() {
/*  57 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getMaxHeight() {
/*  61 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public void setLayoutEnabled(boolean enabled) {
/*  65 */     this.layoutEnabled = enabled;
/*  66 */     if (enabled) invalidateHierarchy(); 
/*     */   }
/*     */   
/*     */   public void validate() {
/*  70 */     if (!this.layoutEnabled)
/*     */       return; 
/*  72 */     Group parent = getParent();
/*  73 */     if (this.fillParent && parent != null) {
/*     */       float parentWidth, parentHeight;
/*  75 */       Stage stage = getStage();
/*  76 */       if (stage != null && parent == stage.getRoot()) {
/*  77 */         parentWidth = stage.getWidth();
/*  78 */         parentHeight = stage.getHeight();
/*     */       } else {
/*  80 */         parentWidth = parent.getWidth();
/*  81 */         parentHeight = parent.getHeight();
/*     */       } 
/*  83 */       setSize(parentWidth, parentHeight);
/*     */     } 
/*     */     
/*  86 */     if (!this.needsLayout)
/*  87 */       return;  this.needsLayout = false;
/*  88 */     layout();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean needsLayout() {
/*  93 */     return this.needsLayout;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/*  97 */     this.needsLayout = true;
/*     */   }
/*     */   
/*     */   public void invalidateHierarchy() {
/* 101 */     if (!this.layoutEnabled)
/* 102 */       return;  invalidate();
/* 103 */     Group parent = getParent();
/* 104 */     if (parent instanceof Layout) ((Layout)parent).invalidateHierarchy(); 
/*     */   }
/*     */   
/*     */   protected void sizeChanged() {
/* 108 */     invalidate();
/*     */   }
/*     */   
/*     */   public void pack() {
/* 112 */     setSize(getPrefWidth(), getPrefHeight());
/* 113 */     validate();
/*     */   }
/*     */   
/*     */   public void setFillParent(boolean fillParent) {
/* 117 */     this.fillParent = fillParent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 122 */     validate();
/*     */   }
/*     */   
/*     */   public void layout() {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Widget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */