/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Group;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
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
/*     */ public class WidgetGroup
/*     */   extends Group
/*     */   implements Layout
/*     */ {
/*     */   private boolean needsLayout = true;
/*     */   private boolean fillParent;
/*     */   private boolean layoutEnabled = true;
/*     */   
/*     */   public WidgetGroup() {}
/*     */   
/*     */   public WidgetGroup(Actor... actors) {
/*  47 */     for (Actor actor : actors)
/*  48 */       addActor(actor); 
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/*  52 */     return getPrefWidth();
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/*  56 */     return getPrefHeight();
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/*  60 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/*  64 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getMaxWidth() {
/*  68 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getMaxHeight() {
/*  72 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public void setLayoutEnabled(boolean enabled) {
/*  76 */     if (this.layoutEnabled == enabled)
/*  77 */       return;  this.layoutEnabled = enabled;
/*  78 */     setLayoutEnabled(this, enabled);
/*     */   }
/*     */   
/*     */   private void setLayoutEnabled(Group parent, boolean enabled) {
/*  82 */     SnapshotArray<Actor> children = parent.getChildren();
/*  83 */     for (int i = 0, n = children.size; i < n; i++) {
/*  84 */       Actor actor = (Actor)children.get(i);
/*  85 */       if (actor instanceof Layout) {
/*  86 */         ((Layout)actor).setLayoutEnabled(enabled);
/*  87 */       } else if (actor instanceof Group) {
/*  88 */         setLayoutEnabled((Group)actor, enabled);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void validate() {
/*  93 */     if (!this.layoutEnabled)
/*     */       return; 
/*  95 */     Group parent = getParent();
/*  96 */     if (this.fillParent && parent != null) {
/*     */       float parentWidth, parentHeight;
/*  98 */       Stage stage = getStage();
/*  99 */       if (stage != null && parent == stage.getRoot()) {
/* 100 */         parentWidth = stage.getWidth();
/* 101 */         parentHeight = stage.getHeight();
/*     */       } else {
/* 103 */         parentWidth = parent.getWidth();
/* 104 */         parentHeight = parent.getHeight();
/*     */       } 
/* 106 */       if (getWidth() != parentWidth || getHeight() != parentHeight) {
/* 107 */         setWidth(parentWidth);
/* 108 */         setHeight(parentHeight);
/* 109 */         invalidate();
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     if (!this.needsLayout)
/* 114 */       return;  this.needsLayout = false;
/* 115 */     layout();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean needsLayout() {
/* 120 */     return this.needsLayout;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/* 124 */     this.needsLayout = true;
/*     */   }
/*     */   
/*     */   public void invalidateHierarchy() {
/* 128 */     invalidate();
/* 129 */     Group parent = getParent();
/* 130 */     if (parent instanceof Layout) ((Layout)parent).invalidateHierarchy(); 
/*     */   }
/*     */   
/*     */   protected void childrenChanged() {
/* 134 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   protected void sizeChanged() {
/* 138 */     invalidate();
/*     */   }
/*     */   
/*     */   public void pack() {
/* 142 */     setSize(getPrefWidth(), getPrefHeight());
/* 143 */     validate();
/*     */ 
/*     */     
/* 146 */     if (this.needsLayout) {
/* 147 */       setSize(getPrefWidth(), getPrefHeight());
/* 148 */       validate();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFillParent(boolean fillParent) {
/* 153 */     this.fillParent = fillParent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void layout() {}
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 162 */     validate();
/* 163 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\WidgetGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */