/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
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
/*     */ public class BaseDrawable
/*     */   implements Drawable
/*     */ {
/*     */   private String name;
/*     */   private float leftWidth;
/*     */   private float rightWidth;
/*     */   private float topHeight;
/*     */   private float bottomHeight;
/*     */   private float minWidth;
/*     */   private float minHeight;
/*     */   
/*     */   public BaseDrawable() {}
/*     */   
/*     */   public BaseDrawable(Drawable drawable) {
/*  33 */     if (drawable instanceof BaseDrawable) this.name = ((BaseDrawable)drawable).getName(); 
/*  34 */     this.leftWidth = drawable.getLeftWidth();
/*  35 */     this.rightWidth = drawable.getRightWidth();
/*  36 */     this.topHeight = drawable.getTopHeight();
/*  37 */     this.bottomHeight = drawable.getBottomHeight();
/*  38 */     this.minWidth = drawable.getMinWidth();
/*  39 */     this.minHeight = drawable.getMinHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float x, float y, float width, float height) {}
/*     */   
/*     */   public float getLeftWidth() {
/*  46 */     return this.leftWidth;
/*     */   }
/*     */   
/*     */   public void setLeftWidth(float leftWidth) {
/*  50 */     this.leftWidth = leftWidth;
/*     */   }
/*     */   
/*     */   public float getRightWidth() {
/*  54 */     return this.rightWidth;
/*     */   }
/*     */   
/*     */   public void setRightWidth(float rightWidth) {
/*  58 */     this.rightWidth = rightWidth;
/*     */   }
/*     */   
/*     */   public float getTopHeight() {
/*  62 */     return this.topHeight;
/*     */   }
/*     */   
/*     */   public void setTopHeight(float topHeight) {
/*  66 */     this.topHeight = topHeight;
/*     */   }
/*     */   
/*     */   public float getBottomHeight() {
/*  70 */     return this.bottomHeight;
/*     */   }
/*     */   
/*     */   public void setBottomHeight(float bottomHeight) {
/*  74 */     this.bottomHeight = bottomHeight;
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/*  78 */     return this.minWidth;
/*     */   }
/*     */   
/*     */   public void setMinWidth(float minWidth) {
/*  82 */     this.minWidth = minWidth;
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/*  86 */     return this.minHeight;
/*     */   }
/*     */   
/*     */   public void setMinHeight(float minHeight) {
/*  90 */     this.minHeight = minHeight;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  94 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  98 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 102 */     if (this.name == null) return ClassReflection.getSimpleName(getClass()); 
/* 103 */     return this.name;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\BaseDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */