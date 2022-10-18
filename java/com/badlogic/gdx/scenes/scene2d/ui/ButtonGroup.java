/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class ButtonGroup<T extends Button>
/*     */ {
/*  27 */   private final Array<T> buttons = new Array();
/*  28 */   private Array<T> checkedButtons = new Array(1);
/*  29 */   private int maxCheckCount = 1; private int minCheckCount;
/*     */   private boolean uncheckLast = true;
/*     */   private T lastChecked;
/*     */   
/*     */   public ButtonGroup() {
/*  34 */     this.minCheckCount = 1;
/*     */   }
/*     */   
/*     */   public ButtonGroup(T... buttons) {
/*  38 */     this.minCheckCount = 0;
/*  39 */     add(buttons);
/*  40 */     this.minCheckCount = 1;
/*     */   }
/*     */   
/*     */   public void add(T button) {
/*  44 */     if (button == null) throw new IllegalArgumentException("button cannot be null."); 
/*  45 */     ((Button)button).buttonGroup = null;
/*  46 */     boolean shouldCheck = (button.isChecked() || this.buttons.size < this.minCheckCount);
/*  47 */     button.setChecked(false);
/*  48 */     ((Button)button).buttonGroup = this;
/*  49 */     this.buttons.add(button);
/*  50 */     button.setChecked(shouldCheck);
/*     */   }
/*     */   
/*     */   public void add(T... buttons) {
/*  54 */     if (buttons == null) throw new IllegalArgumentException("buttons cannot be null."); 
/*  55 */     for (int i = 0, n = buttons.length; i < n; i++)
/*  56 */       add(buttons[i]); 
/*     */   }
/*     */   
/*     */   public void remove(T button) {
/*  60 */     if (button == null) throw new IllegalArgumentException("button cannot be null."); 
/*  61 */     ((Button)button).buttonGroup = null;
/*  62 */     this.buttons.removeValue(button, true);
/*  63 */     this.checkedButtons.removeValue(button, true);
/*     */   }
/*     */   
/*     */   public void remove(T... buttons) {
/*  67 */     if (buttons == null) throw new IllegalArgumentException("buttons cannot be null."); 
/*  68 */     for (int i = 0, n = buttons.length; i < n; i++)
/*  69 */       remove(buttons[i]); 
/*     */   }
/*     */   
/*     */   public void clear() {
/*  73 */     this.buttons.clear();
/*  74 */     this.checkedButtons.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChecked(String text) {
/*  79 */     if (text == null) throw new IllegalArgumentException("text cannot be null."); 
/*  80 */     for (int i = 0, n = this.buttons.size; i < n; i++) {
/*  81 */       Button button = (Button)this.buttons.get(i);
/*  82 */       if (button instanceof TextButton && text.contentEquals(((TextButton)button).getText())) {
/*  83 */         button.setChecked(true);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canCheck(T button, boolean newState) {
/*  93 */     if (((Button)button).isChecked == newState) return false;
/*     */     
/*  95 */     if (!newState) {
/*     */       
/*  97 */       if (this.checkedButtons.size <= this.minCheckCount) return false; 
/*  98 */       this.checkedButtons.removeValue(button, true);
/*     */     } else {
/*     */       
/* 101 */       if (this.maxCheckCount != -1 && this.checkedButtons.size >= this.maxCheckCount)
/* 102 */         if (this.uncheckLast) {
/* 103 */           int old = this.minCheckCount;
/* 104 */           this.minCheckCount = 0;
/* 105 */           this.lastChecked.setChecked(false);
/* 106 */           this.minCheckCount = old;
/*     */         } else {
/* 108 */           return false;
/*     */         }  
/* 110 */       this.checkedButtons.add(button);
/* 111 */       this.lastChecked = button;
/*     */     } 
/*     */     
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void uncheckAll() {
/* 119 */     int old = this.minCheckCount;
/* 120 */     this.minCheckCount = 0;
/* 121 */     for (int i = 0, n = this.buttons.size; i < n; i++) {
/* 122 */       Button button = (Button)this.buttons.get(i);
/* 123 */       button.setChecked(false);
/*     */     } 
/* 125 */     this.minCheckCount = old;
/*     */   }
/*     */ 
/*     */   
/*     */   public T getChecked() {
/* 130 */     if (this.checkedButtons.size > 0) return (T)this.checkedButtons.get(0); 
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCheckedIndex() {
/* 136 */     if (this.checkedButtons.size > 0) return this.buttons.indexOf(this.checkedButtons.get(0), true); 
/* 137 */     return -1;
/*     */   }
/*     */   
/*     */   public Array<T> getAllChecked() {
/* 141 */     return this.checkedButtons;
/*     */   }
/*     */   
/*     */   public Array<T> getButtons() {
/* 145 */     return this.buttons;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMinCheckCount(int minCheckCount) {
/* 150 */     this.minCheckCount = minCheckCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxCheckCount(int maxCheckCount) {
/* 155 */     if (maxCheckCount == 0) maxCheckCount = -1; 
/* 156 */     this.maxCheckCount = maxCheckCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUncheckLast(boolean uncheckLast) {
/* 163 */     this.uncheckLast = uncheckLast;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\ButtonGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */