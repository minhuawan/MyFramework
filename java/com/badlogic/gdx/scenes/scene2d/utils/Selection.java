/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.OrderedSet;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Selection<T>
/*     */   implements Disableable, Iterable<T>
/*     */ {
/*     */   private Actor actor;
/*  17 */   final OrderedSet<T> selected = new OrderedSet();
/*  18 */   private final OrderedSet<T> old = new OrderedSet();
/*     */   
/*     */   boolean isDisabled;
/*     */   private boolean toggle;
/*     */   boolean multiple;
/*     */   boolean required;
/*     */   private boolean programmaticChangeEvents = true;
/*     */   T lastSelected;
/*     */   
/*     */   public void setActor(Actor actor) {
/*  28 */     this.actor = actor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void choose(T item) {
/*  34 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/*  35 */     if (this.isDisabled)
/*  36 */       return;  snapshot();
/*     */     
/*  38 */     try { if ((this.toggle || (!this.required && this.selected.size == 1) || UIUtils.ctrl()) && this.selected.contains(item)) {
/*  39 */         if (this.required && this.selected.size == 1)
/*  40 */           return;  this.selected.remove(item);
/*  41 */         this.lastSelected = null;
/*     */       } else {
/*  43 */         boolean modified = false;
/*  44 */         if (!this.multiple || (!this.toggle && !UIUtils.ctrl())) {
/*  45 */           if (this.selected.size == 1 && this.selected.contains(item))
/*  46 */             return;  modified = (this.selected.size > 0);
/*  47 */           this.selected.clear();
/*     */         } 
/*  49 */         if (!this.selected.add(item) && !modified)
/*  50 */           return;  this.lastSelected = item;
/*     */       } 
/*  52 */       if (fireChangeEvent()) {
/*  53 */         revert();
/*     */       } else {
/*  55 */         changed();
/*     */       }  }
/*  57 */     finally { cleanup(); }
/*     */   
/*     */   }
/*     */   
/*     */   public boolean hasItems() {
/*  62 */     return (this.selected.size > 0);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  66 */     return (this.selected.size == 0);
/*     */   }
/*     */   
/*     */   public int size() {
/*  70 */     return this.selected.size;
/*     */   }
/*     */   
/*     */   public OrderedSet<T> items() {
/*  74 */     return this.selected;
/*     */   }
/*     */ 
/*     */   
/*     */   public T first() {
/*  79 */     return (this.selected.size == 0) ? null : (T)this.selected.first();
/*     */   }
/*     */   
/*     */   void snapshot() {
/*  83 */     this.old.clear();
/*  84 */     this.old.addAll((ObjectSet)this.selected);
/*     */   }
/*     */   
/*     */   void revert() {
/*  88 */     this.selected.clear();
/*  89 */     this.selected.addAll((ObjectSet)this.old);
/*     */   }
/*     */   
/*     */   void cleanup() {
/*  93 */     this.old.clear(32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(T item) {
/*  98 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/*  99 */     if (this.selected.size == 1 && this.selected.first() == item)
/* 100 */       return;  snapshot();
/* 101 */     this.selected.clear();
/* 102 */     this.selected.add(item);
/* 103 */     if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 104 */       revert();
/*     */     } else {
/* 106 */       this.lastSelected = item;
/* 107 */       changed();
/*     */     } 
/* 109 */     cleanup();
/*     */   }
/*     */   
/*     */   public void setAll(Array<T> items) {
/* 113 */     boolean added = false;
/* 114 */     snapshot();
/* 115 */     this.lastSelected = null;
/* 116 */     this.selected.clear();
/* 117 */     for (int i = 0, n = items.size; i < n; i++) {
/* 118 */       T item = (T)items.get(i);
/* 119 */       if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 120 */       if (this.selected.add(item)) added = true; 
/*     */     } 
/* 122 */     if (added) {
/* 123 */       if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 124 */         revert();
/* 125 */       } else if (items.size > 0) {
/* 126 */         this.lastSelected = (T)items.peek();
/* 127 */         changed();
/*     */       } 
/*     */     }
/* 130 */     cleanup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(T item) {
/* 135 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 136 */     if (!this.selected.add(item))
/* 137 */       return;  if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 138 */       this.selected.remove(item);
/*     */     } else {
/* 140 */       this.lastSelected = item;
/* 141 */       changed();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAll(Array<T> items) {
/* 146 */     boolean added = false;
/* 147 */     snapshot();
/* 148 */     for (int i = 0, n = items.size; i < n; i++) {
/* 149 */       T item = (T)items.get(i);
/* 150 */       if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 151 */       if (this.selected.add(item)) added = true; 
/*     */     } 
/* 153 */     if (added) {
/* 154 */       if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 155 */         revert();
/*     */       } else {
/* 157 */         this.lastSelected = (T)items.peek();
/* 158 */         changed();
/*     */       } 
/*     */     }
/* 161 */     cleanup();
/*     */   }
/*     */   
/*     */   public void remove(T item) {
/* 165 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 166 */     if (!this.selected.remove(item))
/* 167 */       return;  if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 168 */       this.selected.add(item);
/*     */     } else {
/* 170 */       this.lastSelected = null;
/* 171 */       changed();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeAll(Array<T> items) {
/* 176 */     boolean removed = false;
/* 177 */     snapshot();
/* 178 */     for (int i = 0, n = items.size; i < n; i++) {
/* 179 */       T item = (T)items.get(i);
/* 180 */       if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 181 */       if (this.selected.remove(item)) removed = true; 
/*     */     } 
/* 183 */     if (removed) {
/* 184 */       if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 185 */         revert();
/*     */       } else {
/* 187 */         this.lastSelected = null;
/* 188 */         changed();
/*     */       } 
/*     */     }
/* 191 */     cleanup();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 195 */     if (this.selected.size == 0)
/* 196 */       return;  snapshot();
/* 197 */     this.selected.clear();
/* 198 */     if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 199 */       revert();
/*     */     } else {
/* 201 */       this.lastSelected = null;
/* 202 */       changed();
/*     */     } 
/* 204 */     cleanup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void changed() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean fireChangeEvent() {
/* 215 */     if (this.actor == null) return false; 
/* 216 */     ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/*     */     try {
/* 218 */       return this.actor.fire(changeEvent);
/*     */     } finally {
/* 220 */       Pools.free(changeEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(T item) {
/* 225 */     if (item == null) return false; 
/* 226 */     return this.selected.contains(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getLastSelected() {
/* 231 */     if (this.lastSelected != null)
/* 232 */       return this.lastSelected; 
/* 233 */     if (this.selected.size > 0) {
/* 234 */       return (T)this.selected.first();
/*     */     }
/* 236 */     return null;
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 240 */     return (Iterator<T>)this.selected.iterator();
/*     */   }
/*     */   
/*     */   public Array<T> toArray() {
/* 244 */     return this.selected.iterator().toArray();
/*     */   }
/*     */   
/*     */   public Array<T> toArray(Array<T> array) {
/* 248 */     return this.selected.iterator().toArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean isDisabled) {
/* 253 */     this.isDisabled = isDisabled;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 257 */     return this.isDisabled;
/*     */   }
/*     */   
/*     */   public boolean getToggle() {
/* 261 */     return this.toggle;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setToggle(boolean toggle) {
/* 266 */     this.toggle = toggle;
/*     */   }
/*     */   
/*     */   public boolean getMultiple() {
/* 270 */     return this.multiple;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMultiple(boolean multiple) {
/* 275 */     this.multiple = multiple;
/*     */   }
/*     */   
/*     */   public boolean getRequired() {
/* 279 */     return this.required;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequired(boolean required) {
/* 284 */     this.required = required;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
/* 289 */     this.programmaticChangeEvents = programmaticChangeEvents;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 293 */     return this.selected.toString();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\Selection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */