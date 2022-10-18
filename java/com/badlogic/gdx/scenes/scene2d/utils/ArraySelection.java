/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.OrderedSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArraySelection<T>
/*    */   extends Selection<T>
/*    */ {
/*    */   private Array<T> array;
/*    */   private boolean rangeSelect = true;
/*    */   private int rangeStart;
/*    */   
/*    */   public ArraySelection(Array<T> array) {
/* 18 */     this.array = array;
/*    */   }
/*    */   
/*    */   public void choose(T item) {
/* 22 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 23 */     if (this.isDisabled)
/* 24 */       return;  int index = this.array.indexOf(item, false);
/* 25 */     if (this.selected.size > 0 && this.rangeSelect && this.multiple && UIUtils.shift()) {
/* 26 */       int oldRangeState = this.rangeStart;
/* 27 */       snapshot();
/*    */       
/* 29 */       int start = this.rangeStart, end = index;
/* 30 */       if (start > end) {
/* 31 */         int temp = end;
/* 32 */         end = start;
/* 33 */         start = temp;
/*    */       } 
/* 35 */       if (!UIUtils.ctrl()) this.selected.clear(); 
/* 36 */       for (int i = start; i <= end; i++)
/* 37 */         this.selected.add(this.array.get(i)); 
/* 38 */       if (fireChangeEvent()) {
/* 39 */         this.rangeStart = oldRangeState;
/* 40 */         revert();
/*    */       } 
/* 42 */       cleanup();
/*    */       return;
/*    */     } 
/* 45 */     this.rangeStart = index;
/* 46 */     super.choose(item);
/*    */   }
/*    */   
/*    */   public boolean getRangeSelect() {
/* 50 */     return this.rangeSelect;
/*    */   }
/*    */   
/*    */   public void setRangeSelect(boolean rangeSelect) {
/* 54 */     this.rangeSelect = rangeSelect;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate() {
/* 60 */     Array<T> array = this.array;
/* 61 */     if (array.size == 0) {
/* 62 */       clear();
/*    */       return;
/*    */     } 
/* 65 */     for (OrderedSet.OrderedSetIterator<T> orderedSetIterator = items().iterator(); orderedSetIterator.hasNext(); ) {
/* 66 */       T selected = orderedSetIterator.next();
/* 67 */       if (!array.contains(selected, false)) orderedSetIterator.remove(); 
/*    */     } 
/* 69 */     if (this.required && this.selected.size == 0) set((T)array.first()); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\ArraySelection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */