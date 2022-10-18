/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class Stack
/*     */   extends WidgetGroup
/*     */ {
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   private float minWidth;
/*     */   private float minHeight;
/*     */   private float maxWidth;
/*     */   private float maxHeight;
/*     */   private boolean sizeInvalid = true;
/*     */   
/*     */   public Stack() {
/*  46 */     setTransform(false);
/*  47 */     setWidth(150.0F);
/*  48 */     setHeight(150.0F);
/*  49 */     setTouchable(Touchable.childrenOnly);
/*     */   }
/*     */   
/*     */   public Stack(Actor... actors) {
/*  53 */     this();
/*  54 */     for (Actor actor : actors)
/*  55 */       addActor(actor); 
/*     */   }
/*     */   
/*     */   public void invalidate() {
/*  59 */     super.invalidate();
/*  60 */     this.sizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void computeSize() {
/*  64 */     this.sizeInvalid = false;
/*  65 */     this.prefWidth = 0.0F;
/*  66 */     this.prefHeight = 0.0F;
/*  67 */     this.minWidth = 0.0F;
/*  68 */     this.minHeight = 0.0F;
/*  69 */     this.maxWidth = 0.0F;
/*  70 */     this.maxHeight = 0.0F;
/*  71 */     SnapshotArray<Actor> children = getChildren();
/*  72 */     for (int i = 0, n = children.size; i < n; i++) {
/*  73 */       float childMaxWidth, childMaxHeight; Actor child = (Actor)children.get(i);
/*     */       
/*  75 */       if (child instanceof Layout) {
/*  76 */         Layout layout = (Layout)child;
/*  77 */         this.prefWidth = Math.max(this.prefWidth, layout.getPrefWidth());
/*  78 */         this.prefHeight = Math.max(this.prefHeight, layout.getPrefHeight());
/*  79 */         this.minWidth = Math.max(this.minWidth, layout.getMinWidth());
/*  80 */         this.minHeight = Math.max(this.minHeight, layout.getMinHeight());
/*  81 */         childMaxWidth = layout.getMaxWidth();
/*  82 */         childMaxHeight = layout.getMaxHeight();
/*     */       } else {
/*  84 */         this.prefWidth = Math.max(this.prefWidth, child.getWidth());
/*  85 */         this.prefHeight = Math.max(this.prefHeight, child.getHeight());
/*  86 */         this.minWidth = Math.max(this.minWidth, child.getWidth());
/*  87 */         this.minHeight = Math.max(this.minHeight, child.getHeight());
/*  88 */         childMaxWidth = 0.0F;
/*  89 */         childMaxHeight = 0.0F;
/*     */       } 
/*  91 */       if (childMaxWidth > 0.0F) this.maxWidth = (this.maxWidth == 0.0F) ? childMaxWidth : Math.min(this.maxWidth, childMaxWidth); 
/*  92 */       if (childMaxHeight > 0.0F) this.maxHeight = (this.maxHeight == 0.0F) ? childMaxHeight : Math.min(this.maxHeight, childMaxHeight); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void add(Actor actor) {
/*  97 */     addActor(actor);
/*     */   }
/*     */   
/*     */   public void layout() {
/* 101 */     if (this.sizeInvalid) computeSize(); 
/* 102 */     float width = getWidth(), height = getHeight();
/* 103 */     SnapshotArray snapshotArray = getChildren();
/* 104 */     for (int i = 0, n = ((Array)snapshotArray).size; i < n; i++) {
/* 105 */       Actor child = (Actor)snapshotArray.get(i);
/* 106 */       child.setBounds(0.0F, 0.0F, width, height);
/* 107 */       if (child instanceof Layout) ((Layout)child).validate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 112 */     if (this.sizeInvalid) computeSize(); 
/* 113 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 117 */     if (this.sizeInvalid) computeSize(); 
/* 118 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/* 122 */     if (this.sizeInvalid) computeSize(); 
/* 123 */     return this.minWidth;
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/* 127 */     if (this.sizeInvalid) computeSize(); 
/* 128 */     return this.minHeight;
/*     */   }
/*     */   
/*     */   public float getMaxWidth() {
/* 132 */     if (this.sizeInvalid) computeSize(); 
/* 133 */     return this.maxWidth;
/*     */   }
/*     */   
/*     */   public float getMaxHeight() {
/* 137 */     if (this.sizeInvalid) computeSize(); 
/* 138 */     return this.maxHeight;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Stack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */