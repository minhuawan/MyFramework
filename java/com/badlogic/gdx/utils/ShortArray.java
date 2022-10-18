/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import java.util.Arrays;
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
/*     */ public class ShortArray
/*     */ {
/*     */   public short[] items;
/*     */   public int size;
/*     */   public boolean ordered;
/*     */   
/*     */   public ShortArray() {
/*  33 */     this(true, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShortArray(int capacity) {
/*  38 */     this(true, capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArray(boolean ordered, int capacity) {
/*  45 */     this.ordered = ordered;
/*  46 */     this.items = new short[capacity];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArray(ShortArray array) {
/*  53 */     this.ordered = array.ordered;
/*  54 */     this.size = array.size;
/*  55 */     this.items = new short[this.size];
/*  56 */     System.arraycopy(array.items, 0, this.items, 0, this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArray(short[] array) {
/*  62 */     this(true, array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArray(boolean ordered, short[] array, int startIndex, int count) {
/*  70 */     this(ordered, count);
/*  71 */     this.size = count;
/*  72 */     System.arraycopy(array, startIndex, this.items, 0, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int value) {
/*  77 */     short[] items = this.items;
/*  78 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/*  79 */     items[this.size++] = (short)value;
/*     */   }
/*     */   
/*     */   public void add(short value) {
/*  83 */     short[] items = this.items;
/*  84 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/*  85 */     items[this.size++] = value;
/*     */   }
/*     */   
/*     */   public void addAll(ShortArray array) {
/*  89 */     addAll(array, 0, array.size);
/*     */   }
/*     */   
/*     */   public void addAll(ShortArray array, int offset, int length) {
/*  93 */     if (offset + length > array.size)
/*  94 */       throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size); 
/*  95 */     addAll(array.items, offset, length);
/*     */   }
/*     */   
/*     */   public void addAll(short... array) {
/*  99 */     addAll(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void addAll(short[] array, int offset, int length) {
/* 103 */     short[] items = this.items;
/* 104 */     int sizeNeeded = this.size + length;
/* 105 */     if (sizeNeeded > items.length) items = resize(Math.max(8, (int)(sizeNeeded * 1.75F))); 
/* 106 */     System.arraycopy(array, offset, items, this.size, length);
/* 107 */     this.size += length;
/*     */   }
/*     */   
/*     */   public short get(int index) {
/* 111 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 112 */     return this.items[index];
/*     */   }
/*     */   
/*     */   public void set(int index, short value) {
/* 116 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 117 */     this.items[index] = value;
/*     */   }
/*     */   
/*     */   public void incr(int index, short value) {
/* 121 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 122 */     this.items[index] = (short)(this.items[index] + value);
/*     */   }
/*     */   
/*     */   public void mul(int index, short value) {
/* 126 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 127 */     this.items[index] = (short)(this.items[index] * value);
/*     */   }
/*     */   
/*     */   public void insert(int index, short value) {
/* 131 */     if (index > this.size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + this.size); 
/* 132 */     short[] items = this.items;
/* 133 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/* 134 */     if (this.ordered) {
/* 135 */       System.arraycopy(items, index, items, index + 1, this.size - index);
/*     */     } else {
/* 137 */       items[this.size] = items[index];
/* 138 */     }  this.size++;
/* 139 */     items[index] = value;
/*     */   }
/*     */   
/*     */   public void swap(int first, int second) {
/* 143 */     if (first >= this.size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + this.size); 
/* 144 */     if (second >= this.size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + this.size); 
/* 145 */     short[] items = this.items;
/* 146 */     short firstValue = items[first];
/* 147 */     items[first] = items[second];
/* 148 */     items[second] = firstValue;
/*     */   }
/*     */   
/*     */   public boolean contains(short value) {
/* 152 */     int i = this.size - 1;
/* 153 */     short[] items = this.items;
/* 154 */     while (i >= 0) {
/* 155 */       if (items[i--] == value) return true; 
/* 156 */     }  return false;
/*     */   }
/*     */   
/*     */   public int indexOf(short value) {
/* 160 */     short[] items = this.items;
/* 161 */     for (int i = 0, n = this.size; i < n; i++) {
/* 162 */       if (items[i] == value) return i; 
/* 163 */     }  return -1;
/*     */   }
/*     */   
/*     */   public int lastIndexOf(char value) {
/* 167 */     short[] items = this.items;
/* 168 */     for (int i = this.size - 1; i >= 0; i--) {
/* 169 */       if (items[i] == value) return i; 
/* 170 */     }  return -1;
/*     */   }
/*     */   
/*     */   public boolean removeValue(short value) {
/* 174 */     short[] items = this.items;
/* 175 */     for (int i = 0, n = this.size; i < n; i++) {
/* 176 */       if (items[i] == value) {
/* 177 */         removeIndex(i);
/* 178 */         return true;
/*     */       } 
/*     */     } 
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public short removeIndex(int index) {
/* 186 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 187 */     short[] items = this.items;
/* 188 */     short value = items[index];
/* 189 */     this.size--;
/* 190 */     if (this.ordered) {
/* 191 */       System.arraycopy(items, index + 1, items, index, this.size - index);
/*     */     } else {
/* 193 */       items[index] = items[this.size];
/* 194 */     }  return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeRange(int start, int end) {
/* 199 */     if (end >= this.size) throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + this.size); 
/* 200 */     if (start > end) throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end); 
/* 201 */     short[] items = this.items;
/* 202 */     int count = end - start + 1;
/* 203 */     if (this.ordered) {
/* 204 */       System.arraycopy(items, start + count, items, start, this.size - start + count);
/*     */     } else {
/* 206 */       int lastIndex = this.size - 1;
/* 207 */       for (int i = 0; i < count; i++)
/* 208 */         items[start + i] = items[lastIndex - i]; 
/*     */     } 
/* 210 */     this.size -= count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(ShortArray array) {
/* 216 */     int size = this.size;
/* 217 */     int startSize = size;
/* 218 */     short[] items = this.items;
/* 219 */     for (int i = 0, n = array.size; i < n; i++) {
/* 220 */       short item = array.get(i);
/* 221 */       for (int ii = 0; ii < size; ii++) {
/* 222 */         if (item == items[ii]) {
/* 223 */           removeIndex(ii);
/* 224 */           size--;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 229 */     return (size != startSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public short pop() {
/* 234 */     return this.items[--this.size];
/*     */   }
/*     */ 
/*     */   
/*     */   public short peek() {
/* 239 */     return this.items[this.size - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public short first() {
/* 244 */     if (this.size == 0) throw new IllegalStateException("Array is empty."); 
/* 245 */     return this.items[0];
/*     */   }
/*     */   
/*     */   public void clear() {
/* 249 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] shrink() {
/* 256 */     if (this.items.length != this.size) resize(this.size); 
/* 257 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] ensureCapacity(int additionalCapacity) {
/* 264 */     int sizeNeeded = this.size + additionalCapacity;
/* 265 */     if (sizeNeeded > this.items.length) resize(Math.max(8, sizeNeeded)); 
/* 266 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] setSize(int newSize) {
/* 272 */     if (newSize > this.items.length) resize(Math.max(8, newSize)); 
/* 273 */     this.size = newSize;
/* 274 */     return this.items;
/*     */   }
/*     */   
/*     */   protected short[] resize(int newSize) {
/* 278 */     short[] newItems = new short[newSize];
/* 279 */     short[] items = this.items;
/* 280 */     System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
/* 281 */     this.items = newItems;
/* 282 */     return newItems;
/*     */   }
/*     */   
/*     */   public void sort() {
/* 286 */     Arrays.sort(this.items, 0, this.size);
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 290 */     short[] items = this.items;
/* 291 */     for (int i = 0, lastIndex = this.size - 1, n = this.size / 2; i < n; i++) {
/* 292 */       int ii = lastIndex - i;
/* 293 */       short temp = items[i];
/* 294 */       items[i] = items[ii];
/* 295 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shuffle() {
/* 300 */     short[] items = this.items;
/* 301 */     for (int i = this.size - 1; i >= 0; i--) {
/* 302 */       int ii = MathUtils.random(i);
/* 303 */       short temp = items[i];
/* 304 */       items[i] = items[ii];
/* 305 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void truncate(int newSize) {
/* 312 */     if (this.size > newSize) this.size = newSize;
/*     */   
/*     */   }
/*     */   
/*     */   public short random() {
/* 317 */     if (this.size == 0) return 0; 
/* 318 */     return this.items[MathUtils.random(0, this.size - 1)];
/*     */   }
/*     */   
/*     */   public short[] toArray() {
/* 322 */     short[] array = new short[this.size];
/* 323 */     System.arraycopy(this.items, 0, array, 0, this.size);
/* 324 */     return array;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 328 */     if (!this.ordered) return super.hashCode(); 
/* 329 */     short[] items = this.items;
/* 330 */     int h = 1;
/* 331 */     for (int i = 0, n = this.size; i < n; i++)
/* 332 */       h = h * 31 + items[i]; 
/* 333 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/* 337 */     if (object == this) return true; 
/* 338 */     if (!this.ordered) return false; 
/* 339 */     if (!(object instanceof ShortArray)) return false; 
/* 340 */     ShortArray array = (ShortArray)object;
/* 341 */     if (!array.ordered) return false; 
/* 342 */     int n = this.size;
/* 343 */     if (n != array.size) return false; 
/* 344 */     short[] items1 = this.items;
/* 345 */     short[] items2 = array.items;
/* 346 */     for (int i = 0; i < n; i++) {
/* 347 */       if (this.items[i] != array.items[i]) return false; 
/* 348 */     }  return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 352 */     if (this.size == 0) return "[]"; 
/* 353 */     short[] items = this.items;
/* 354 */     StringBuilder buffer = new StringBuilder(32);
/* 355 */     buffer.append('[');
/* 356 */     buffer.append(items[0]);
/* 357 */     for (int i = 1; i < this.size; i++) {
/* 358 */       buffer.append(", ");
/* 359 */       buffer.append(items[i]);
/*     */     } 
/* 361 */     buffer.append(']');
/* 362 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 366 */     if (this.size == 0) return ""; 
/* 367 */     short[] items = this.items;
/* 368 */     StringBuilder buffer = new StringBuilder(32);
/* 369 */     buffer.append(items[0]);
/* 370 */     for (int i = 1; i < this.size; i++) {
/* 371 */       buffer.append(separator);
/* 372 */       buffer.append(items[i]);
/*     */     } 
/* 374 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ShortArray with(short... array) {
/* 379 */     return new ShortArray(array);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ShortArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */