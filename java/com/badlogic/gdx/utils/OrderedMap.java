/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class OrderedMap<K, V>
/*     */   extends ObjectMap<K, V>
/*     */ {
/*     */   final Array<K> keys;
/*     */   private ObjectMap.Entries entries1;
/*     */   private ObjectMap.Entries entries2;
/*     */   private ObjectMap.Values values1;
/*     */   private ObjectMap.Values values2;
/*     */   private ObjectMap.Keys keys1;
/*     */   private ObjectMap.Keys keys2;
/*     */   
/*     */   public OrderedMap() {
/*  36 */     this.keys = new Array<K>();
/*     */   }
/*     */   
/*     */   public OrderedMap(int initialCapacity) {
/*  40 */     super(initialCapacity);
/*  41 */     this.keys = new Array<K>(this.capacity);
/*     */   }
/*     */   
/*     */   public OrderedMap(int initialCapacity, float loadFactor) {
/*  45 */     super(initialCapacity, loadFactor);
/*  46 */     this.keys = new Array<K>(this.capacity);
/*     */   }
/*     */   
/*     */   public OrderedMap(OrderedMap<? extends K, ? extends V> map) {
/*  50 */     super(map);
/*  51 */     this.keys = new Array<K>(map.keys);
/*     */   }
/*     */   
/*     */   public V put(K key, V value) {
/*  55 */     if (!containsKey(key)) this.keys.add(key); 
/*  56 */     return super.put(key, value);
/*     */   }
/*     */   
/*     */   public V remove(K key) {
/*  60 */     this.keys.removeValue(key, false);
/*  61 */     return super.remove(key);
/*     */   }
/*     */   
/*     */   public void clear(int maximumCapacity) {
/*  65 */     this.keys.clear();
/*  66 */     super.clear(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  70 */     this.keys.clear();
/*  71 */     super.clear();
/*     */   }
/*     */   
/*     */   public Array<K> orderedKeys() {
/*  75 */     return this.keys;
/*     */   }
/*     */   
/*     */   public ObjectMap.Entries<K, V> iterator() {
/*  79 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMap.Entries<K, V> entries() {
/*  85 */     if (this.entries1 == null) {
/*  86 */       this.entries1 = new OrderedMapEntries<K, V>(this);
/*  87 */       this.entries2 = new OrderedMapEntries<K, V>(this);
/*     */     } 
/*  89 */     if (!this.entries1.valid) {
/*  90 */       this.entries1.reset();
/*  91 */       this.entries1.valid = true;
/*  92 */       this.entries2.valid = false;
/*  93 */       return this.entries1;
/*     */     } 
/*  95 */     this.entries2.reset();
/*  96 */     this.entries2.valid = true;
/*  97 */     this.entries1.valid = false;
/*  98 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMap.Values<V> values() {
/* 104 */     if (this.values1 == null) {
/* 105 */       this.values1 = new OrderedMapValues<V>(this);
/* 106 */       this.values2 = new OrderedMapValues<V>(this);
/*     */     } 
/* 108 */     if (!this.values1.valid) {
/* 109 */       this.values1.reset();
/* 110 */       this.values1.valid = true;
/* 111 */       this.values2.valid = false;
/* 112 */       return this.values1;
/*     */     } 
/* 114 */     this.values2.reset();
/* 115 */     this.values2.valid = true;
/* 116 */     this.values1.valid = false;
/* 117 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMap.Keys<K> keys() {
/* 123 */     if (this.keys1 == null) {
/* 124 */       this.keys1 = new OrderedMapKeys<K>(this);
/* 125 */       this.keys2 = new OrderedMapKeys<K>(this);
/*     */     } 
/* 127 */     if (!this.keys1.valid) {
/* 128 */       this.keys1.reset();
/* 129 */       this.keys1.valid = true;
/* 130 */       this.keys2.valid = false;
/* 131 */       return this.keys1;
/*     */     } 
/* 133 */     this.keys2.reset();
/* 134 */     this.keys2.valid = true;
/* 135 */     this.keys1.valid = false;
/* 136 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 140 */     if (this.size == 0) return "{}"; 
/* 141 */     StringBuilder buffer = new StringBuilder(32);
/* 142 */     buffer.append('{');
/* 143 */     Array<K> keys = this.keys;
/* 144 */     for (int i = 0, n = keys.size; i < n; i++) {
/* 145 */       K key = keys.get(i);
/* 146 */       if (i > 0) buffer.append(", "); 
/* 147 */       buffer.append(key);
/* 148 */       buffer.append('=');
/* 149 */       buffer.append(get(key));
/*     */     } 
/* 151 */     buffer.append('}');
/* 152 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public static class OrderedMapEntries<K, V> extends ObjectMap.Entries<K, V> {
/*     */     private Array<K> keys;
/*     */     
/*     */     public OrderedMapEntries(OrderedMap<K, V> map) {
/* 159 */       super(map);
/* 160 */       this.keys = map.keys;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 164 */       this.nextIndex = 0;
/* 165 */       this.hasNext = (this.map.size > 0);
/*     */     }
/*     */     
/*     */     public ObjectMap.Entry next() {
/* 169 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 170 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 171 */       this.entry.key = this.keys.get(this.nextIndex);
/* 172 */       this.entry.value = this.map.get(this.entry.key);
/* 173 */       this.nextIndex++;
/* 174 */       this.hasNext = (this.nextIndex < this.map.size);
/* 175 */       return this.entry;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 179 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 180 */       this.map.remove(this.entry.key);
/* 181 */       this.nextIndex--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OrderedMapKeys<K> extends ObjectMap.Keys<K> {
/*     */     private Array<K> keys;
/*     */     
/*     */     public OrderedMapKeys(OrderedMap<K, ?> map) {
/* 189 */       super(map);
/* 190 */       this.keys = map.keys;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 194 */       this.nextIndex = 0;
/* 195 */       this.hasNext = (this.map.size > 0);
/*     */     }
/*     */     
/*     */     public K next() {
/* 199 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 200 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 201 */       K key = this.keys.get(this.nextIndex);
/* 202 */       this.currentIndex = this.nextIndex;
/* 203 */       this.nextIndex++;
/* 204 */       this.hasNext = (this.nextIndex < this.map.size);
/* 205 */       return key;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 209 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 210 */       this.map.remove(this.keys.get(this.nextIndex - 1));
/* 211 */       this.nextIndex = this.currentIndex;
/* 212 */       this.currentIndex = -1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OrderedMapValues<V> extends ObjectMap.Values<V> {
/*     */     private Array keys;
/*     */     
/*     */     public OrderedMapValues(OrderedMap<?, V> map) {
/* 220 */       super(map);
/* 221 */       this.keys = map.keys;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 225 */       this.nextIndex = 0;
/* 226 */       this.hasNext = (this.map.size > 0);
/*     */     }
/*     */     
/*     */     public V next() {
/* 230 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 231 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 232 */       V value = this.map.get(this.keys.get(this.nextIndex));
/* 233 */       this.currentIndex = this.nextIndex;
/* 234 */       this.nextIndex++;
/* 235 */       this.hasNext = (this.nextIndex < this.map.size);
/* 236 */       return value;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 240 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 241 */       this.map.remove(this.keys.get(this.currentIndex));
/* 242 */       this.nextIndex = this.currentIndex;
/* 243 */       this.currentIndex = -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\OrderedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */