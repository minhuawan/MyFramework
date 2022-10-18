/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
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
/*     */ public class ObjectSet<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   public int size;
/*     */   T[] keyTable;
/*     */   int capacity;
/*     */   int stashSize;
/*     */   private float loadFactor;
/*     */   private int hashShift;
/*     */   private int mask;
/*     */   private int threshold;
/*     */   private int stashCapacity;
/*     */   private int pushIterations;
/*     */   private ObjectSetIterator iterator1;
/*     */   private ObjectSetIterator iterator2;
/*     */   
/*     */   public ObjectSet() {
/*  50 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectSet(int initialCapacity) {
/*  56 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectSet(int initialCapacity, float loadFactor) {
/*  63 */     if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity); 
/*  64 */     initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil((initialCapacity / loadFactor)));
/*  65 */     if (initialCapacity > 1073741824) throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity); 
/*  66 */     this.capacity = initialCapacity;
/*     */     
/*  68 */     if (loadFactor <= 0.0F) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor); 
/*  69 */     this.loadFactor = loadFactor;
/*     */     
/*  71 */     this.threshold = (int)(this.capacity * loadFactor);
/*  72 */     this.mask = this.capacity - 1;
/*  73 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
/*  74 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  75 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  77 */     this.keyTable = (T[])new Object[this.capacity + this.stashCapacity];
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectSet(ObjectSet set) {
/*  82 */     this((int)Math.floor((set.capacity * set.loadFactor)), set.loadFactor);
/*  83 */     this.stashSize = set.stashSize;
/*  84 */     System.arraycopy(set.keyTable, 0, this.keyTable, 0, set.keyTable.length);
/*  85 */     this.size = set.size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(T key) {
/*  91 */     if (key == null) throw new IllegalArgumentException("key cannot be null."); 
/*  92 */     T[] keyTable = this.keyTable;
/*     */ 
/*     */     
/*  95 */     int hashCode = key.hashCode();
/*  96 */     int index1 = hashCode & this.mask;
/*  97 */     T key1 = keyTable[index1];
/*  98 */     if (key.equals(key1)) return false;
/*     */     
/* 100 */     int index2 = hash2(hashCode);
/* 101 */     T key2 = keyTable[index2];
/* 102 */     if (key.equals(key2)) return false;
/*     */     
/* 104 */     int index3 = hash3(hashCode);
/* 105 */     T key3 = keyTable[index3];
/* 106 */     if (key.equals(key3)) return false;
/*     */ 
/*     */     
/* 109 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 110 */       if (key.equals(keyTable[i])) return false;
/*     */     
/*     */     } 
/* 113 */     if (key1 == null) {
/* 114 */       keyTable[index1] = key;
/* 115 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 116 */       return true;
/*     */     } 
/*     */     
/* 119 */     if (key2 == null) {
/* 120 */       keyTable[index2] = key;
/* 121 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 122 */       return true;
/*     */     } 
/*     */     
/* 125 */     if (key3 == null) {
/* 126 */       keyTable[index3] = key;
/* 127 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 128 */       return true;
/*     */     } 
/*     */     
/* 131 */     push(key, index1, key1, index2, key2, index3, key3);
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   public void addAll(Array<? extends T> array) {
/* 136 */     addAll(array, 0, array.size);
/*     */   }
/*     */   
/*     */   public void addAll(Array<? extends T> array, int offset, int length) {
/* 140 */     if (offset + length > array.size)
/* 141 */       throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size); 
/* 142 */     addAll(array.items, offset, length);
/*     */   }
/*     */   
/*     */   public void addAll(T... array) {
/* 146 */     addAll(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void addAll(T[] array, int offset, int length) {
/* 150 */     ensureCapacity(length);
/* 151 */     for (int i = offset, n = i + length; i < n; i++)
/* 152 */       add(array[i]); 
/*     */   }
/*     */   
/*     */   public void addAll(ObjectSet<T> set) {
/* 156 */     ensureCapacity(set.size);
/* 157 */     for (ObjectSetIterator<T> objectSetIterator = set.iterator(); objectSetIterator.hasNext(); ) { T key = objectSetIterator.next();
/* 158 */       add(key); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void addResize(T key) {
/* 164 */     int hashCode = key.hashCode();
/* 165 */     int index1 = hashCode & this.mask;
/* 166 */     T key1 = this.keyTable[index1];
/* 167 */     if (key1 == null) {
/* 168 */       this.keyTable[index1] = key;
/* 169 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 173 */     int index2 = hash2(hashCode);
/* 174 */     T key2 = this.keyTable[index2];
/* 175 */     if (key2 == null) {
/* 176 */       this.keyTable[index2] = key;
/* 177 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 181 */     int index3 = hash3(hashCode);
/* 182 */     T key3 = this.keyTable[index3];
/* 183 */     if (key3 == null) {
/* 184 */       this.keyTable[index3] = key;
/* 185 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 189 */     push(key, index1, key1, index2, key2, index3, key3);
/*     */   }
/*     */   
/*     */   private void push(T insertKey, int index1, T key1, int index2, T key2, int index3, T key3) {
/* 193 */     T evictedKey, keyTable[] = this.keyTable;
/* 194 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */     
/* 198 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 201 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 203 */           evictedKey = key1;
/* 204 */           keyTable[index1] = insertKey;
/*     */           break;
/*     */         case 1:
/* 207 */           evictedKey = key2;
/* 208 */           keyTable[index2] = insertKey;
/*     */           break;
/*     */         default:
/* 211 */           evictedKey = key3;
/* 212 */           keyTable[index3] = insertKey;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 217 */       int hashCode = evictedKey.hashCode();
/* 218 */       index1 = hashCode & mask;
/* 219 */       key1 = keyTable[index1];
/* 220 */       if (key1 == null) {
/* 221 */         keyTable[index1] = evictedKey;
/* 222 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 226 */       index2 = hash2(hashCode);
/* 227 */       key2 = keyTable[index2];
/* 228 */       if (key2 == null) {
/* 229 */         keyTable[index2] = evictedKey;
/* 230 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 234 */       index3 = hash3(hashCode);
/* 235 */       key3 = keyTable[index3];
/* 236 */       if (key3 == null) {
/* 237 */         keyTable[index3] = evictedKey;
/* 238 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 242 */       if (++i == pushIterations)
/*     */         break; 
/* 244 */       insertKey = evictedKey;
/*     */     } 
/*     */     
/* 247 */     addStash(evictedKey);
/*     */   }
/*     */   
/*     */   private void addStash(T key) {
/* 251 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 253 */       resize(this.capacity << 1);
/* 254 */       add(key);
/*     */       
/*     */       return;
/*     */     } 
/* 258 */     int index = this.capacity + this.stashSize;
/* 259 */     this.keyTable[index] = key;
/* 260 */     this.stashSize++;
/* 261 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(T key) {
/* 266 */     int hashCode = key.hashCode();
/* 267 */     int index = hashCode & this.mask;
/* 268 */     if (key.equals(this.keyTable[index])) {
/* 269 */       this.keyTable[index] = null;
/* 270 */       this.size--;
/* 271 */       return true;
/*     */     } 
/*     */     
/* 274 */     index = hash2(hashCode);
/* 275 */     if (key.equals(this.keyTable[index])) {
/* 276 */       this.keyTable[index] = null;
/* 277 */       this.size--;
/* 278 */       return true;
/*     */     } 
/*     */     
/* 281 */     index = hash3(hashCode);
/* 282 */     if (key.equals(this.keyTable[index])) {
/* 283 */       this.keyTable[index] = null;
/* 284 */       this.size--;
/* 285 */       return true;
/*     */     } 
/*     */     
/* 288 */     return removeStash(key);
/*     */   }
/*     */   
/*     */   boolean removeStash(T key) {
/* 292 */     T[] keyTable = this.keyTable;
/* 293 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 294 */       if (key.equals(keyTable[i])) {
/* 295 */         removeStashIndex(i);
/* 296 */         this.size--;
/* 297 */         return true;
/*     */       } 
/*     */     } 
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 305 */     this.stashSize--;
/* 306 */     int lastIndex = this.capacity + this.stashSize;
/* 307 */     if (index < lastIndex) this.keyTable[index] = this.keyTable[lastIndex];
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 313 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 314 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 315 */     if (this.capacity <= maximumCapacity)
/* 316 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 317 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 322 */     if (this.capacity <= maximumCapacity) {
/* 323 */       clear();
/*     */       return;
/*     */     } 
/* 326 */     this.size = 0;
/* 327 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 331 */     if (this.size == 0)
/* 332 */       return;  T[] keyTable = this.keyTable;
/* 333 */     for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 334 */       keyTable[i] = null; 
/* 335 */     this.size = 0;
/* 336 */     this.stashSize = 0;
/*     */   }
/*     */   
/*     */   public boolean contains(T key) {
/* 340 */     int hashCode = key.hashCode();
/* 341 */     int index = hashCode & this.mask;
/* 342 */     if (!key.equals(this.keyTable[index])) {
/* 343 */       index = hash2(hashCode);
/* 344 */       if (!key.equals(this.keyTable[index])) {
/* 345 */         index = hash3(hashCode);
/* 346 */         if (!key.equals(this.keyTable[index])) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 349 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(T key) {
/* 353 */     T[] keyTable = this.keyTable;
/* 354 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 355 */       if (key.equals(keyTable[i])) return true; 
/* 356 */     }  return false;
/*     */   }
/*     */   
/*     */   public T first() {
/* 360 */     T[] keyTable = this.keyTable;
/* 361 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 362 */       if (keyTable[i] != null) return keyTable[i]; 
/* 363 */     }  throw new IllegalStateException("ObjectSet is empty.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 369 */     int sizeNeeded = this.size + additionalCapacity;
/* 370 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 374 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 376 */     this.capacity = newSize;
/* 377 */     this.threshold = (int)(newSize * this.loadFactor);
/* 378 */     this.mask = newSize - 1;
/* 379 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 380 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 381 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 383 */     T[] oldKeyTable = this.keyTable;
/*     */     
/* 385 */     this.keyTable = (T[])new Object[newSize + this.stashCapacity];
/*     */     
/* 387 */     int oldSize = this.size;
/* 388 */     this.size = 0;
/* 389 */     this.stashSize = 0;
/* 390 */     if (oldSize > 0)
/* 391 */       for (int i = 0; i < oldEndIndex; i++) {
/* 392 */         T key = oldKeyTable[i];
/* 393 */         if (key != null) addResize(key);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 399 */     h *= -1262997959;
/* 400 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 404 */     h *= -825114047;
/* 405 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 409 */     int h = 0;
/* 410 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 411 */       if (this.keyTable[i] != null) h += this.keyTable[i].hashCode(); 
/* 412 */     }  return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 416 */     if (!(obj instanceof ObjectSet)) return false; 
/* 417 */     ObjectSet<T> other = (ObjectSet)obj;
/* 418 */     if (other.size != this.size) return false; 
/* 419 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 420 */       if (this.keyTable[i] != null && !other.contains(this.keyTable[i])) return false; 
/* 421 */     }  return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 425 */     return '{' + toString(", ") + '}';
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 429 */     if (this.size == 0) return ""; 
/* 430 */     StringBuilder buffer = new StringBuilder(32);
/* 431 */     T[] keyTable = this.keyTable;
/* 432 */     int i = keyTable.length;
/* 433 */     while (i-- > 0) {
/* 434 */       T key = keyTable[i];
/* 435 */       if (key == null)
/* 436 */         continue;  buffer.append(key);
/*     */     } 
/*     */     
/* 439 */     while (i-- > 0) {
/* 440 */       T key = keyTable[i];
/* 441 */       if (key == null)
/* 442 */         continue;  buffer.append(separator);
/* 443 */       buffer.append(key);
/*     */     } 
/* 445 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectSetIterator<T> iterator() {
/* 451 */     if (this.iterator1 == null) {
/* 452 */       this.iterator1 = new ObjectSetIterator<T>(this);
/* 453 */       this.iterator2 = new ObjectSetIterator<T>(this);
/*     */     } 
/* 455 */     if (!this.iterator1.valid) {
/* 456 */       this.iterator1.reset();
/* 457 */       this.iterator1.valid = true;
/* 458 */       this.iterator2.valid = false;
/* 459 */       return this.iterator1;
/*     */     } 
/* 461 */     this.iterator2.reset();
/* 462 */     this.iterator2.valid = true;
/* 463 */     this.iterator1.valid = false;
/* 464 */     return this.iterator2;
/*     */   }
/*     */   
/*     */   public static <T> ObjectSet<T> with(T... array) {
/* 468 */     ObjectSet<T> set = new ObjectSet();
/* 469 */     set.addAll(array);
/* 470 */     return set;
/*     */   }
/*     */   
/*     */   public static class ObjectSetIterator<K> implements Iterable<K>, Iterator<K> {
/*     */     public boolean hasNext;
/*     */     final ObjectSet<K> set;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public ObjectSetIterator(ObjectSet<K> set) {
/* 481 */       this.set = set;
/* 482 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 486 */       this.currentIndex = -1;
/* 487 */       this.nextIndex = -1;
/* 488 */       findNextIndex();
/*     */     }
/*     */     
/*     */     void findNextIndex() {
/* 492 */       this.hasNext = false;
/* 493 */       T[] arrayOfT = this.set.keyTable;
/* 494 */       for (int n = this.set.capacity + this.set.stashSize; ++this.nextIndex < n;) {
/* 495 */         if (arrayOfT[this.nextIndex] != null) {
/* 496 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 503 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 504 */       if (this.currentIndex >= this.set.capacity) {
/* 505 */         this.set.removeStashIndex(this.currentIndex);
/* 506 */         this.nextIndex = this.currentIndex - 1;
/* 507 */         findNextIndex();
/*     */       } else {
/* 509 */         this.set.keyTable[this.currentIndex] = null;
/*     */       } 
/* 511 */       this.currentIndex = -1;
/* 512 */       this.set.size--;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 516 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 517 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public K next() {
/* 521 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 522 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 523 */       T t = this.set.keyTable[this.nextIndex];
/* 524 */       this.currentIndex = this.nextIndex;
/* 525 */       findNextIndex();
/* 526 */       return (K)t;
/*     */     }
/*     */     
/*     */     public ObjectSetIterator<K> iterator() {
/* 530 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray(Array<K> array) {
/* 535 */       while (this.hasNext)
/* 536 */         array.add(next()); 
/* 537 */       return array;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray() {
/* 542 */       return toArray(new Array<K>(true, this.set.size));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ObjectSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */