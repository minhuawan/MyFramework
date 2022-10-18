/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
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
/*     */ public class BooleanArray
/*     */ {
/*     */   public boolean[] items;
/*     */   public int size;
/*     */   public boolean ordered;
/*     */   
/*     */   public BooleanArray() {
/*  35 */     this(true, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public BooleanArray(int capacity) {
/*  40 */     this(true, capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanArray(boolean ordered, int capacity) {
/*  47 */     this.ordered = ordered;
/*  48 */     this.items = new boolean[capacity];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanArray(BooleanArray array) {
/*  55 */     this.ordered = array.ordered;
/*  56 */     this.size = array.size;
/*  57 */     this.items = new boolean[this.size];
/*  58 */     System.arraycopy(array.items, 0, this.items, 0, this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanArray(boolean[] array) {
/*  64 */     this(true, array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanArray(boolean ordered, boolean[] array, int startIndex, int count) {
/*  72 */     this(ordered, count);
/*  73 */     this.size = count;
/*  74 */     System.arraycopy(array, startIndex, this.items, 0, count);
/*     */   }
/*     */   
/*     */   public void add(boolean value) {
/*  78 */     boolean[] items = this.items;
/*  79 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/*  80 */     items[this.size++] = value;
/*     */   }
/*     */   
/*     */   public void addAll(BooleanArray array) {
/*  84 */     addAll(array, 0, array.size);
/*     */   }
/*     */   
/*     */   public void addAll(BooleanArray array, int offset, int length) {
/*  88 */     if (offset + length > array.size)
/*  89 */       throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size); 
/*  90 */     addAll(array.items, offset, length);
/*     */   }
/*     */   
/*     */   public void addAll(boolean... array) {
/*  94 */     addAll(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void addAll(boolean[] array, int offset, int length) {
/*  98 */     boolean[] items = this.items;
/*  99 */     int sizeNeeded = this.size + length;
/* 100 */     if (sizeNeeded > items.length) items = resize(Math.max(8, (int)(sizeNeeded * 1.75F))); 
/* 101 */     System.arraycopy(array, offset, items, this.size, length);
/* 102 */     this.size += length;
/*     */   }
/*     */   
/*     */   public boolean get(int index) {
/* 106 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 107 */     return this.items[index];
/*     */   }
/*     */   
/*     */   public void set(int index, boolean value) {
/* 111 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 112 */     this.items[index] = value;
/*     */   }
/*     */   
/*     */   public void insert(int index, boolean value) {
/* 116 */     if (index > this.size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + this.size); 
/* 117 */     boolean[] items = this.items;
/* 118 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/* 119 */     if (this.ordered) {
/* 120 */       System.arraycopy(items, index, items, index + 1, this.size - index);
/*     */     } else {
/* 122 */       items[this.size] = items[index];
/* 123 */     }  this.size++;
/* 124 */     items[index] = value;
/*     */   }
/*     */   
/*     */   public void swap(int first, int second) {
/* 128 */     if (first >= this.size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + this.size); 
/* 129 */     if (second >= this.size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + this.size); 
/* 130 */     boolean[] items = this.items;
/* 131 */     boolean firstValue = items[first];
/* 132 */     items[first] = items[second];
/* 133 */     items[second] = firstValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeIndex(int index) {
/* 138 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 139 */     boolean[] items = this.items;
/* 140 */     boolean value = items[index];
/* 141 */     this.size--;
/* 142 */     if (this.ordered) {
/* 143 */       System.arraycopy(items, index + 1, items, index, this.size - index);
/*     */     } else {
/* 145 */       items[index] = items[this.size];
/* 146 */     }  return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeRange(int start, int end) {
/* 151 */     if (end >= this.size) throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + this.size); 
/* 152 */     if (start > end) throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end); 
/* 153 */     boolean[] items = this.items;
/* 154 */     int count = end - start + 1;
/* 155 */     if (this.ordered) {
/* 156 */       System.arraycopy(items, start + count, items, start, this.size - start + count);
/*     */     } else {
/* 158 */       int lastIndex = this.size - 1;
/* 159 */       for (int i = 0; i < count; i++)
/* 160 */         items[start + i] = items[lastIndex - i]; 
/*     */     } 
/* 162 */     this.size -= count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(BooleanArray array) {
/* 168 */     int size = this.size;
/* 169 */     int startSize = size;
/* 170 */     boolean[] items = this.items;
/* 171 */     for (int i = 0, n = array.size; i < n; i++) {
/* 172 */       boolean item = array.get(i);
/* 173 */       for (int ii = 0; ii < size; ii++) {
/* 174 */         if (item == items[ii]) {
/* 175 */           removeIndex(ii);
/* 176 */           size--;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 181 */     return (size != startSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean pop() {
/* 186 */     return this.items[--this.size];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean peek() {
/* 191 */     return this.items[this.size - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean first() {
/* 196 */     if (this.size == 0) throw new IllegalStateException("Array is empty."); 
/* 197 */     return this.items[0];
/*     */   }
/*     */   
/*     */   public void clear() {
/* 201 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean[] shrink() {
/* 208 */     if (this.items.length != this.size) resize(this.size); 
/* 209 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean[] ensureCapacity(int additionalCapacity) {
/* 216 */     int sizeNeeded = this.size + additionalCapacity;
/* 217 */     if (sizeNeeded > this.items.length) resize(Math.max(8, sizeNeeded)); 
/* 218 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean[] setSize(int newSize) {
/* 224 */     if (newSize > this.items.length) resize(Math.max(8, newSize)); 
/* 225 */     this.size = newSize;
/* 226 */     return this.items;
/*     */   }
/*     */   
/*     */   protected boolean[] resize(int newSize) {
/* 230 */     boolean[] newItems = new boolean[newSize];
/* 231 */     boolean[] items = this.items;
/* 232 */     System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
/* 233 */     this.items = newItems;
/* 234 */     return newItems;
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 238 */     boolean[] items = this.items;
/* 239 */     for (int i = 0, lastIndex = this.size - 1, n = this.size / 2; i < n; i++) {
/* 240 */       int ii = lastIndex - i;
/* 241 */       boolean temp = items[i];
/* 242 */       items[i] = items[ii];
/* 243 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shuffle() {
/* 248 */     boolean[] items = this.items;
/* 249 */     for (int i = this.size - 1; i >= 0; i--) {
/* 250 */       int ii = MathUtils.random(i);
/* 251 */       boolean temp = items[i];
/* 252 */       items[i] = items[ii];
/* 253 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void truncate(int newSize) {
/* 260 */     if (this.size > newSize) this.size = newSize;
/*     */   
/*     */   }
/*     */   
/*     */   public boolean random() {
/* 265 */     if (this.size == 0) return false; 
/* 266 */     return this.items[MathUtils.random(0, this.size - 1)];
/*     */   }
/*     */   
/*     */   public boolean[] toArray() {
/* 270 */     boolean[] array = new boolean[this.size];
/* 271 */     System.arraycopy(this.items, 0, array, 0, this.size);
/* 272 */     return array;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 276 */     if (!this.ordered) return super.hashCode(); 
/* 277 */     boolean[] items = this.items;
/* 278 */     int h = 1;
/* 279 */     for (int i = 0, n = this.size; i < n; i++)
/* 280 */       h = h * 31 + (items[i] ? 1231 : 1237); 
/* 281 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/* 285 */     if (object == this) return true; 
/* 286 */     if (!this.ordered) return false; 
/* 287 */     if (!(object instanceof BooleanArray)) return false; 
/* 288 */     BooleanArray array = (BooleanArray)object;
/* 289 */     if (!array.ordered) return false; 
/* 290 */     int n = this.size;
/* 291 */     if (n != array.size) return false; 
/* 292 */     boolean[] items1 = this.items;
/* 293 */     boolean[] items2 = array.items;
/* 294 */     for (int i = 0; i < n; i++) {
/* 295 */       if (items1[i] != items2[i]) return false; 
/* 296 */     }  return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 300 */     if (this.size == 0) return "[]"; 
/* 301 */     boolean[] items = this.items;
/* 302 */     StringBuilder buffer = new StringBuilder(32);
/* 303 */     buffer.append('[');
/* 304 */     buffer.append(items[0]);
/* 305 */     for (int i = 1; i < this.size; i++) {
/* 306 */       buffer.append(", ");
/* 307 */       buffer.append(items[i]);
/*     */     } 
/* 309 */     buffer.append(']');
/* 310 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 314 */     if (this.size == 0) return ""; 
/* 315 */     boolean[] items = this.items;
/* 316 */     StringBuilder buffer = new StringBuilder(32);
/* 317 */     buffer.append(items[0]);
/* 318 */     for (int i = 1; i < this.size; i++) {
/* 319 */       buffer.append(separator);
/* 320 */       buffer.append(items[i]);
/*     */     } 
/* 322 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static BooleanArray with(boolean... array) {
/* 327 */     return new BooleanArray(array);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\BooleanArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */