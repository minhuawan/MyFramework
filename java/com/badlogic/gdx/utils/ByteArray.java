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
/*     */ public class ByteArray
/*     */ {
/*     */   public byte[] items;
/*     */   public int size;
/*     */   public boolean ordered;
/*     */   
/*     */   public ByteArray() {
/*  33 */     this(true, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteArray(int capacity) {
/*  38 */     this(true, capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray(boolean ordered, int capacity) {
/*  45 */     this.ordered = ordered;
/*  46 */     this.items = new byte[capacity];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray(ByteArray array) {
/*  53 */     this.ordered = array.ordered;
/*  54 */     this.size = array.size;
/*  55 */     this.items = new byte[this.size];
/*  56 */     System.arraycopy(array.items, 0, this.items, 0, this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray(byte[] array) {
/*  62 */     this(true, array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray(boolean ordered, byte[] array, int startIndex, int count) {
/*  70 */     this(ordered, count);
/*  71 */     this.size = count;
/*  72 */     System.arraycopy(array, startIndex, this.items, 0, count);
/*     */   }
/*     */   
/*     */   public void add(byte value) {
/*  76 */     byte[] items = this.items;
/*  77 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/*  78 */     items[this.size++] = value;
/*     */   }
/*     */   
/*     */   public void addAll(ByteArray array) {
/*  82 */     addAll(array, 0, array.size);
/*     */   }
/*     */   
/*     */   public void addAll(ByteArray array, int offset, int length) {
/*  86 */     if (offset + length > array.size)
/*  87 */       throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size); 
/*  88 */     addAll(array.items, offset, length);
/*     */   }
/*     */   
/*     */   public void addAll(byte... array) {
/*  92 */     addAll(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void addAll(byte[] array, int offset, int length) {
/*  96 */     byte[] items = this.items;
/*  97 */     int sizeNeeded = this.size + length;
/*  98 */     if (sizeNeeded > items.length) items = resize(Math.max(8, (int)(sizeNeeded * 1.75F))); 
/*  99 */     System.arraycopy(array, offset, items, this.size, length);
/* 100 */     this.size += length;
/*     */   }
/*     */   
/*     */   public byte get(int index) {
/* 104 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 105 */     return this.items[index];
/*     */   }
/*     */   
/*     */   public void set(int index, byte value) {
/* 109 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 110 */     this.items[index] = value;
/*     */   }
/*     */   
/*     */   public void incr(int index, byte value) {
/* 114 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 115 */     this.items[index] = (byte)(this.items[index] + value);
/*     */   }
/*     */   
/*     */   public void mul(int index, byte value) {
/* 119 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 120 */     this.items[index] = (byte)(this.items[index] * value);
/*     */   }
/*     */   
/*     */   public void insert(int index, byte value) {
/* 124 */     if (index > this.size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + this.size); 
/* 125 */     byte[] items = this.items;
/* 126 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/* 127 */     if (this.ordered) {
/* 128 */       System.arraycopy(items, index, items, index + 1, this.size - index);
/*     */     } else {
/* 130 */       items[this.size] = items[index];
/* 131 */     }  this.size++;
/* 132 */     items[index] = value;
/*     */   }
/*     */   
/*     */   public void swap(int first, int second) {
/* 136 */     if (first >= this.size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + this.size); 
/* 137 */     if (second >= this.size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + this.size); 
/* 138 */     byte[] items = this.items;
/* 139 */     byte firstValue = items[first];
/* 140 */     items[first] = items[second];
/* 141 */     items[second] = firstValue;
/*     */   }
/*     */   
/*     */   public boolean contains(byte value) {
/* 145 */     int i = this.size - 1;
/* 146 */     byte[] items = this.items;
/* 147 */     while (i >= 0) {
/* 148 */       if (items[i--] == value) return true; 
/* 149 */     }  return false;
/*     */   }
/*     */   
/*     */   public int indexOf(byte value) {
/* 153 */     byte[] items = this.items;
/* 154 */     for (int i = 0, n = this.size; i < n; i++) {
/* 155 */       if (items[i] == value) return i; 
/* 156 */     }  return -1;
/*     */   }
/*     */   
/*     */   public int lastIndexOf(byte value) {
/* 160 */     byte[] items = this.items;
/* 161 */     for (int i = this.size - 1; i >= 0; i--) {
/* 162 */       if (items[i] == value) return i; 
/* 163 */     }  return -1;
/*     */   }
/*     */   
/*     */   public boolean removeValue(byte value) {
/* 167 */     byte[] items = this.items;
/* 168 */     for (int i = 0, n = this.size; i < n; i++) {
/* 169 */       if (items[i] == value) {
/* 170 */         removeIndex(i);
/* 171 */         return true;
/*     */       } 
/*     */     } 
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int removeIndex(int index) {
/* 179 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 180 */     byte[] items = this.items;
/* 181 */     int value = items[index];
/* 182 */     this.size--;
/* 183 */     if (this.ordered) {
/* 184 */       System.arraycopy(items, index + 1, items, index, this.size - index);
/*     */     } else {
/* 186 */       items[index] = items[this.size];
/* 187 */     }  return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeRange(int start, int end) {
/* 192 */     if (end >= this.size) throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + this.size); 
/* 193 */     if (start > end) throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end); 
/* 194 */     byte[] items = this.items;
/* 195 */     int count = end - start + 1;
/* 196 */     if (this.ordered) {
/* 197 */       System.arraycopy(items, start + count, items, start, this.size - start + count);
/*     */     } else {
/* 199 */       int lastIndex = this.size - 1;
/* 200 */       for (int i = 0; i < count; i++)
/* 201 */         items[start + i] = items[lastIndex - i]; 
/*     */     } 
/* 203 */     this.size -= count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(ByteArray array) {
/* 209 */     int size = this.size;
/* 210 */     int startSize = size;
/* 211 */     byte[] items = this.items;
/* 212 */     for (int i = 0, n = array.size; i < n; i++) {
/* 213 */       int item = array.get(i);
/* 214 */       for (int ii = 0; ii < size; ii++) {
/* 215 */         if (item == items[ii]) {
/* 216 */           removeIndex(ii);
/* 217 */           size--;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 222 */     return (size != startSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte pop() {
/* 227 */     return this.items[--this.size];
/*     */   }
/*     */ 
/*     */   
/*     */   public byte peek() {
/* 232 */     return this.items[this.size - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public byte first() {
/* 237 */     if (this.size == 0) throw new IllegalStateException("Array is empty."); 
/* 238 */     return this.items[0];
/*     */   }
/*     */   
/*     */   public void clear() {
/* 242 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] shrink() {
/* 249 */     if (this.items.length != this.size) resize(this.size); 
/* 250 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] ensureCapacity(int additionalCapacity) {
/* 257 */     int sizeNeeded = this.size + additionalCapacity;
/* 258 */     if (sizeNeeded > this.items.length) resize(Math.max(8, sizeNeeded)); 
/* 259 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] setSize(int newSize) {
/* 265 */     if (newSize > this.items.length) resize(Math.max(8, newSize)); 
/* 266 */     this.size = newSize;
/* 267 */     return this.items;
/*     */   }
/*     */   
/*     */   protected byte[] resize(int newSize) {
/* 271 */     byte[] newItems = new byte[newSize];
/* 272 */     byte[] items = this.items;
/* 273 */     System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
/* 274 */     this.items = newItems;
/* 275 */     return newItems;
/*     */   }
/*     */   
/*     */   public void sort() {
/* 279 */     Arrays.sort(this.items, 0, this.size);
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 283 */     byte[] items = this.items;
/* 284 */     for (int i = 0, lastIndex = this.size - 1, n = this.size / 2; i < n; i++) {
/* 285 */       int ii = lastIndex - i;
/* 286 */       byte temp = items[i];
/* 287 */       items[i] = items[ii];
/* 288 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shuffle() {
/* 293 */     byte[] items = this.items;
/* 294 */     for (int i = this.size - 1; i >= 0; i--) {
/* 295 */       int ii = MathUtils.random(i);
/* 296 */       byte temp = items[i];
/* 297 */       items[i] = items[ii];
/* 298 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void truncate(int newSize) {
/* 305 */     if (this.size > newSize) this.size = newSize;
/*     */   
/*     */   }
/*     */   
/*     */   public byte random() {
/* 310 */     if (this.size == 0) return 0; 
/* 311 */     return this.items[MathUtils.random(0, this.size - 1)];
/*     */   }
/*     */   
/*     */   public byte[] toArray() {
/* 315 */     byte[] array = new byte[this.size];
/* 316 */     System.arraycopy(this.items, 0, array, 0, this.size);
/* 317 */     return array;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 321 */     if (!this.ordered) return super.hashCode(); 
/* 322 */     byte[] items = this.items;
/* 323 */     int h = 1;
/* 324 */     for (int i = 0, n = this.size; i < n; i++)
/* 325 */       h = h * 31 + items[i]; 
/* 326 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/* 330 */     if (object == this) return true; 
/* 331 */     if (!this.ordered) return false; 
/* 332 */     if (!(object instanceof ByteArray)) return false; 
/* 333 */     ByteArray array = (ByteArray)object;
/* 334 */     if (!array.ordered) return false; 
/* 335 */     int n = this.size;
/* 336 */     if (n != array.size) return false; 
/* 337 */     byte[] items1 = this.items;
/* 338 */     byte[] items2 = array.items;
/* 339 */     for (int i = 0; i < n; i++) {
/* 340 */       if (items1[i] != items2[i]) return false; 
/* 341 */     }  return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 345 */     if (this.size == 0) return "[]"; 
/* 346 */     byte[] items = this.items;
/* 347 */     StringBuilder buffer = new StringBuilder(32);
/* 348 */     buffer.append('[');
/* 349 */     buffer.append(items[0]);
/* 350 */     for (int i = 1; i < this.size; i++) {
/* 351 */       buffer.append(", ");
/* 352 */       buffer.append(items[i]);
/*     */     } 
/* 354 */     buffer.append(']');
/* 355 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 359 */     if (this.size == 0) return ""; 
/* 360 */     byte[] items = this.items;
/* 361 */     StringBuilder buffer = new StringBuilder(32);
/* 362 */     buffer.append(items[0]);
/* 363 */     for (int i = 1; i < this.size; i++) {
/* 364 */       buffer.append(separator);
/* 365 */       buffer.append(items[i]);
/*     */     } 
/* 367 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteArray with(byte... array) {
/* 372 */     return new ByteArray(array);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ByteArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */