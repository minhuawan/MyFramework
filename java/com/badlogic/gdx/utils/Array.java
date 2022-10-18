/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.reflect.ArrayReflection;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Array<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   public T[] items;
/*     */   public int size;
/*     */   public boolean ordered;
/*     */   private ArrayIterable iterable;
/*     */   private Predicate.PredicateIterable<T> predicateIterable;
/*     */   
/*     */   public Array() {
/*  42 */     this(true, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public Array(int capacity) {
/*  47 */     this(true, capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array(boolean ordered, int capacity) {
/*  54 */     this.ordered = ordered;
/*  55 */     this.items = (T[])new Object[capacity];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array(boolean ordered, int capacity, Class arrayType) {
/*  63 */     this.ordered = ordered;
/*  64 */     this.items = (T[])ArrayReflection.newInstance(arrayType, capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public Array(Class arrayType) {
/*  69 */     this(true, 16, arrayType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array(Array<? extends T> array) {
/*  76 */     this(array.ordered, array.size, array.items.getClass().getComponentType());
/*  77 */     this.size = array.size;
/*  78 */     System.arraycopy(array.items, 0, this.items, 0, this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array(T[] array) {
/*  85 */     this(true, array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array(boolean ordered, T[] array, int start, int count) {
/*  93 */     this(ordered, count, array.getClass().getComponentType());
/*  94 */     this.size = count;
/*  95 */     System.arraycopy(array, start, this.items, 0, this.size);
/*     */   }
/*     */   
/*     */   public void add(T value) {
/*  99 */     T[] items = this.items;
/* 100 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/* 101 */     items[this.size++] = value;
/*     */   }
/*     */   
/*     */   public void addAll(Array<? extends T> array) {
/* 105 */     addAll(array, 0, array.size);
/*     */   }
/*     */   
/*     */   public void addAll(Array<? extends T> array, int start, int count) {
/* 109 */     if (start + count > array.size)
/* 110 */       throw new IllegalArgumentException("start + count must be <= size: " + start + " + " + count + " <= " + array.size); 
/* 111 */     addAll(array.items, start, count);
/*     */   }
/*     */   
/*     */   public void addAll(T... array) {
/* 115 */     addAll(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void addAll(T[] array, int start, int count) {
/* 119 */     T[] items = this.items;
/* 120 */     int sizeNeeded = this.size + count;
/* 121 */     if (sizeNeeded > items.length) items = resize(Math.max(8, (int)(sizeNeeded * 1.75F))); 
/* 122 */     System.arraycopy(array, start, items, this.size, count);
/* 123 */     this.size += count;
/*     */   }
/*     */   
/*     */   public T get(int index) {
/* 127 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 128 */     return this.items[index];
/*     */   }
/*     */   
/*     */   public void set(int index, T value) {
/* 132 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 133 */     this.items[index] = value;
/*     */   }
/*     */   
/*     */   public void insert(int index, T value) {
/* 137 */     if (index > this.size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + this.size); 
/* 138 */     T[] items = this.items;
/* 139 */     if (this.size == items.length) items = resize(Math.max(8, (int)(this.size * 1.75F))); 
/* 140 */     if (this.ordered) {
/* 141 */       System.arraycopy(items, index, items, index + 1, this.size - index);
/*     */     } else {
/* 143 */       items[this.size] = items[index];
/* 144 */     }  this.size++;
/* 145 */     items[index] = value;
/*     */   }
/*     */   
/*     */   public void swap(int first, int second) {
/* 149 */     if (first >= this.size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + this.size); 
/* 150 */     if (second >= this.size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + this.size); 
/* 151 */     T[] items = this.items;
/* 152 */     T firstValue = items[first];
/* 153 */     items[first] = items[second];
/* 154 */     items[second] = firstValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(T value, boolean identity) {
/* 162 */     T[] items = this.items;
/* 163 */     int i = this.size - 1;
/* 164 */     if (identity || value == null)
/* 165 */     { while (i >= 0) {
/* 166 */         if (items[i--] == value) return true; 
/*     */       }  }
/* 168 */     else { while (i >= 0) {
/* 169 */         if (value.equals(items[i--])) return true; 
/*     */       }  }
/* 171 */      return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(T value, boolean identity) {
/* 179 */     T[] items = this.items;
/* 180 */     if (identity || value == null)
/* 181 */     { for (int i = 0, n = this.size; i < n; i++) {
/* 182 */         if (items[i] == value) return i; 
/*     */       }  }
/* 184 */     else { for (int i = 0, n = this.size; i < n; i++) {
/* 185 */         if (value.equals(items[i])) return i; 
/*     */       }  }
/* 187 */      return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(T value, boolean identity) {
/* 196 */     T[] items = this.items;
/* 197 */     if (identity || value == null)
/* 198 */     { for (int i = this.size - 1; i >= 0; i--) {
/* 199 */         if (items[i] == value) return i; 
/*     */       }  }
/* 201 */     else { for (int i = this.size - 1; i >= 0; i--) {
/* 202 */         if (value.equals(items[i])) return i; 
/*     */       }  }
/* 204 */      return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeValue(T value, boolean identity) {
/* 212 */     T[] items = this.items;
/* 213 */     if (identity || value == null) {
/* 214 */       for (int i = 0, n = this.size; i < n; i++) {
/* 215 */         if (items[i] == value) {
/* 216 */           removeIndex(i);
/* 217 */           return true;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 221 */       for (int i = 0, n = this.size; i < n; i++) {
/* 222 */         if (value.equals(items[i])) {
/* 223 */           removeIndex(i);
/* 224 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public T removeIndex(int index) {
/* 233 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 234 */     T[] items = this.items;
/* 235 */     T value = items[index];
/* 236 */     this.size--;
/* 237 */     if (this.ordered) {
/* 238 */       System.arraycopy(items, index + 1, items, index, this.size - index);
/*     */     } else {
/* 240 */       items[index] = items[this.size];
/* 241 */     }  items[this.size] = null;
/* 242 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeRange(int start, int end) {
/* 247 */     if (end >= this.size) throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + this.size); 
/* 248 */     if (start > end) throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end); 
/* 249 */     T[] items = this.items;
/* 250 */     int count = end - start + 1;
/* 251 */     if (this.ordered) {
/* 252 */       System.arraycopy(items, start + count, items, start, this.size - start + count);
/*     */     } else {
/* 254 */       int lastIndex = this.size - 1;
/* 255 */       for (int i = 0; i < count; i++)
/* 256 */         items[start + i] = items[lastIndex - i]; 
/*     */     } 
/* 258 */     this.size -= count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(Array<? extends T> array, boolean identity) {
/* 265 */     int size = this.size;
/* 266 */     int startSize = size;
/* 267 */     T[] items = this.items;
/* 268 */     if (identity) {
/* 269 */       for (int i = 0, n = array.size; i < n; i++) {
/* 270 */         T item = array.get(i);
/* 271 */         for (int ii = 0; ii < size; ii++) {
/* 272 */           if (item == items[ii]) {
/* 273 */             removeIndex(ii);
/* 274 */             size--;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 280 */       for (int i = 0, n = array.size; i < n; i++) {
/* 281 */         T item = array.get(i);
/* 282 */         for (int ii = 0; ii < size; ii++) {
/* 283 */           if (item.equals(items[ii])) {
/* 284 */             removeIndex(ii);
/* 285 */             size--;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 291 */     return (size != startSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public T pop() {
/* 296 */     if (this.size == 0) throw new IllegalStateException("Array is empty."); 
/* 297 */     this.size--;
/* 298 */     T item = this.items[this.size];
/* 299 */     this.items[this.size] = null;
/* 300 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public T peek() {
/* 305 */     if (this.size == 0) throw new IllegalStateException("Array is empty."); 
/* 306 */     return this.items[this.size - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public T first() {
/* 311 */     if (this.size == 0) throw new IllegalStateException("Array is empty."); 
/* 312 */     return this.items[0];
/*     */   }
/*     */   
/*     */   public void clear() {
/* 316 */     T[] items = this.items;
/* 317 */     for (int i = 0, n = this.size; i < n; i++)
/* 318 */       items[i] = null; 
/* 319 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T[] shrink() {
/* 326 */     if (this.items.length != this.size) resize(this.size); 
/* 327 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T[] ensureCapacity(int additionalCapacity) {
/* 334 */     int sizeNeeded = this.size + additionalCapacity;
/* 335 */     if (sizeNeeded > this.items.length) resize(Math.max(8, sizeNeeded)); 
/* 336 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T[] setSize(int newSize) {
/* 342 */     truncate(newSize);
/* 343 */     if (newSize > this.items.length) resize(Math.max(8, newSize)); 
/* 344 */     this.size = newSize;
/* 345 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected T[] resize(int newSize) {
/* 350 */     T[] items = this.items;
/* 351 */     T[] newItems = (T[])ArrayReflection.newInstance(items.getClass().getComponentType(), newSize);
/* 352 */     System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
/* 353 */     this.items = newItems;
/* 354 */     return newItems;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sort() {
/* 360 */     Sort.instance().sort(this.items, 0, this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sort(Comparator<? super T> comparator) {
/* 365 */     Sort.instance().sort(this.items, comparator, 0, this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T selectRanked(Comparator<T> comparator, int kthLowest) {
/* 376 */     if (kthLowest < 1) {
/* 377 */       throw new GdxRuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
/*     */     }
/* 379 */     return Select.instance().select(this.items, comparator, kthLowest, this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int selectRankedIndex(Comparator<T> comparator, int kthLowest) {
/* 388 */     if (kthLowest < 1) {
/* 389 */       throw new GdxRuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
/*     */     }
/* 391 */     return Select.instance().selectIndex(this.items, comparator, kthLowest, this.size);
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 395 */     T[] items = this.items;
/* 396 */     for (int i = 0, lastIndex = this.size - 1, n = this.size / 2; i < n; i++) {
/* 397 */       int ii = lastIndex - i;
/* 398 */       T temp = items[i];
/* 399 */       items[i] = items[ii];
/* 400 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shuffle() {
/* 405 */     T[] items = this.items;
/* 406 */     for (int i = this.size - 1; i >= 0; i--) {
/* 407 */       int ii = MathUtils.random(i);
/* 408 */       T temp = items[i];
/* 409 */       items[i] = items[ii];
/* 410 */       items[ii] = temp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 417 */     if (this.iterable == null) this.iterable = new ArrayIterable<T>(this); 
/* 418 */     return this.iterable.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<T> select(Predicate<T> predicate) {
/* 425 */     if (this.predicateIterable == null) {
/* 426 */       this.predicateIterable = new Predicate.PredicateIterable<T>(this, predicate);
/*     */     } else {
/* 428 */       this.predicateIterable.set(this, predicate);
/* 429 */     }  return this.predicateIterable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void truncate(int newSize) {
/* 435 */     if (this.size <= newSize)
/* 436 */       return;  for (int i = newSize; i < this.size; i++)
/* 437 */       this.items[i] = null; 
/* 438 */     this.size = newSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public T random() {
/* 443 */     if (this.size == 0) return null; 
/* 444 */     return this.items[MathUtils.random(0, this.size - 1)];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T[] toArray() {
/* 450 */     return (T[])toArray(this.items.getClass().getComponentType());
/*     */   }
/*     */   
/*     */   public <V> V[] toArray(Class type) {
/* 454 */     V[] result = (V[])ArrayReflection.newInstance(type, this.size);
/* 455 */     System.arraycopy(this.items, 0, result, 0, this.size);
/* 456 */     return result;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 460 */     if (!this.ordered) return super.hashCode(); 
/* 461 */     T[] arrayOfT = this.items;
/* 462 */     int h = 1;
/* 463 */     for (int i = 0, n = this.size; i < n; i++) {
/* 464 */       h *= 31;
/* 465 */       Object item = arrayOfT[i];
/* 466 */       if (item != null) h += item.hashCode(); 
/*     */     } 
/* 468 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/* 472 */     if (object == this) return true; 
/* 473 */     if (!this.ordered) return false; 
/* 474 */     if (!(object instanceof Array)) return false; 
/* 475 */     Array array = (Array)object;
/* 476 */     if (!array.ordered) return false; 
/* 477 */     int n = this.size;
/* 478 */     if (n != array.size) return false; 
/* 479 */     T[] arrayOfT1 = this.items;
/* 480 */     T[] arrayOfT2 = array.items;
/* 481 */     for (int i = 0; i < n; ) {
/* 482 */       Object o1 = arrayOfT1[i];
/* 483 */       Object o2 = arrayOfT2[i];
/* 484 */       if ((o1 == null) ? (o2 == null) : o1.equals(o2)) { i++; continue; }  return false;
/*     */     } 
/* 486 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 490 */     if (this.size == 0) return "[]"; 
/* 491 */     T[] items = this.items;
/* 492 */     StringBuilder buffer = new StringBuilder(32);
/* 493 */     buffer.append('[');
/* 494 */     buffer.append(items[0]);
/* 495 */     for (int i = 1; i < this.size; i++) {
/* 496 */       buffer.append(", ");
/* 497 */       buffer.append(items[i]);
/*     */     } 
/* 499 */     buffer.append(']');
/* 500 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 504 */     if (this.size == 0) return ""; 
/* 505 */     T[] items = this.items;
/* 506 */     StringBuilder buffer = new StringBuilder(32);
/* 507 */     buffer.append(items[0]);
/* 508 */     for (int i = 1; i < this.size; i++) {
/* 509 */       buffer.append(separator);
/* 510 */       buffer.append(items[i]);
/*     */     } 
/* 512 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Array<T> of(Class<T> arrayType) {
/* 517 */     return new Array<T>(arrayType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Array<T> of(boolean ordered, int capacity, Class<T> arrayType) {
/* 522 */     return new Array<T>(ordered, capacity, arrayType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Array<T> with(T... array) {
/* 527 */     return new Array<T>(array);
/*     */   }
/*     */   
/*     */   public static class ArrayIterator<T>
/*     */     implements Iterator<T>, Iterable<T>
/*     */   {
/*     */     private final Array<T> array;
/*     */     private final boolean allowRemove;
/*     */     int index;
/*     */     boolean valid = true;
/*     */     
/*     */     public ArrayIterator(Array<T> array) {
/* 539 */       this(array, true);
/*     */     }
/*     */     
/*     */     public ArrayIterator(Array<T> array, boolean allowRemove) {
/* 543 */       this.array = array;
/* 544 */       this.allowRemove = allowRemove;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 548 */       if (!this.valid)
/*     */       {
/* 550 */         throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       }
/* 552 */       return (this.index < this.array.size);
/*     */     }
/*     */     
/*     */     public T next() {
/* 556 */       if (this.index >= this.array.size) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 557 */       if (!this.valid)
/*     */       {
/* 559 */         throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       }
/* 561 */       return this.array.items[this.index++];
/*     */     }
/*     */     
/*     */     public void remove() {
/* 565 */       if (!this.allowRemove) throw new GdxRuntimeException("Remove not allowed."); 
/* 566 */       this.index--;
/* 567 */       this.array.removeIndex(this.index);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 571 */       this.index = 0;
/*     */     }
/*     */     
/*     */     public Iterator<T> iterator() {
/* 575 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ArrayIterable<T>
/*     */     implements Iterable<T> {
/*     */     private final Array<T> array;
/*     */     private final boolean allowRemove;
/*     */     private Array.ArrayIterator iterator1;
/*     */     private Array.ArrayIterator iterator2;
/*     */     
/*     */     public ArrayIterable(Array<T> array) {
/* 587 */       this(array, true);
/*     */     }
/*     */     
/*     */     public ArrayIterable(Array<T> array, boolean allowRemove) {
/* 591 */       this.array = array;
/* 592 */       this.allowRemove = allowRemove;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 598 */       if (this.iterator1 == null) {
/* 599 */         this.iterator1 = new Array.ArrayIterator<T>(this.array, this.allowRemove);
/* 600 */         this.iterator2 = new Array.ArrayIterator<T>(this.array, this.allowRemove);
/*     */       } 
/*     */ 
/*     */       
/* 604 */       if (!this.iterator1.valid) {
/* 605 */         this.iterator1.index = 0;
/* 606 */         this.iterator1.valid = true;
/* 607 */         this.iterator2.valid = false;
/* 608 */         return this.iterator1;
/*     */       } 
/* 610 */       this.iterator2.index = 0;
/* 611 */       this.iterator2.valid = true;
/* 612 */       this.iterator1.valid = false;
/* 613 */       return this.iterator2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */