/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
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
/*     */ public abstract class Value
/*     */ {
/*  30 */   public static final Fixed zero = new Fixed(0.0F);
/*     */   
/*     */   public static class Fixed
/*     */     extends Value
/*     */   {
/*     */     private final float value;
/*     */     
/*     */     public Fixed(float value) {
/*  38 */       this.value = value;
/*     */     }
/*     */     
/*     */     public float get(Actor context) {
/*  42 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  47 */   public static Value minWidth = new Value() {
/*     */       public float get(Actor context) {
/*  49 */         if (context instanceof Layout) return ((Layout)context).getMinWidth(); 
/*  50 */         return (context == null) ? 0.0F : context.getWidth();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  55 */   public static Value minHeight = new Value() {
/*     */       public float get(Actor context) {
/*  57 */         if (context instanceof Layout) return ((Layout)context).getMinHeight(); 
/*  58 */         return (context == null) ? 0.0F : context.getHeight();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  63 */   public static Value prefWidth = new Value() {
/*     */       public float get(Actor context) {
/*  65 */         if (context instanceof Layout) return ((Layout)context).getPrefWidth(); 
/*  66 */         return (context == null) ? 0.0F : context.getWidth();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static Value prefHeight = new Value() {
/*     */       public float get(Actor context) {
/*  74 */         if (context instanceof Layout) return ((Layout)context).getPrefHeight(); 
/*  75 */         return (context == null) ? 0.0F : context.getHeight();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  80 */   public static Value maxWidth = new Value() {
/*     */       public float get(Actor context) {
/*  82 */         if (context instanceof Layout) return ((Layout)context).getMaxWidth(); 
/*  83 */         return (context == null) ? 0.0F : context.getWidth();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  88 */   public static Value maxHeight = new Value() {
/*     */       public float get(Actor context) {
/*  90 */         if (context instanceof Layout) return ((Layout)context).getMaxHeight(); 
/*  91 */         return (context == null) ? 0.0F : context.getHeight();
/*     */       }
/*     */     };
/*     */   public abstract float get(Actor paramActor);
/*     */   
/*     */   public static Value percentWidth(final float percent) {
/*  97 */     return new Value() {
/*     */         public float get(Actor actor) {
/*  99 */           return actor.getWidth() * percent;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Value percentHeight(final float percent) {
/* 106 */     return new Value() {
/*     */         public float get(Actor actor) {
/* 108 */           return actor.getHeight() * percent;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Value percentWidth(final float percent, final Actor actor) {
/* 115 */     if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 116 */     return new Value() {
/*     */         public float get(Actor context) {
/* 118 */           return actor.getWidth() * percent;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Value percentHeight(final float percent, final Actor actor) {
/* 125 */     if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 126 */     return new Value() {
/*     */         public float get(Actor context) {
/* 128 */           return actor.getHeight() * percent;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Value.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */