/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
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
/*     */ 
/*     */ public class IntSet
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   private static final int EMPTY = 0;
/*     */   public int size;
/*     */   int[] keyTable;
/*     */   int capacity;
/*     */   int stashSize;
/*     */   boolean hasZeroValue;
/*     */   private float loadFactor;
/*     */   private int hashShift;
/*     */   private int mask;
/*     */   private int threshold;
/*     */   private int stashCapacity;
/*     */   private int pushIterations;
/*     */   private IntSetIterator iterator1;
/*     */   private IntSetIterator iterator2;
/*     */   
/*     */   public IntSet() {
/*  51 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IntSet(int initialCapacity) {
/*  57 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntSet(int initialCapacity, float loadFactor) {
/*  64 */     if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity); 
/*  65 */     initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil((initialCapacity / loadFactor)));
/*  66 */     if (initialCapacity > 1073741824) throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity); 
/*  67 */     this.capacity = initialCapacity;
/*     */     
/*  69 */     if (loadFactor <= 0.0F) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor); 
/*  70 */     this.loadFactor = loadFactor;
/*     */     
/*  72 */     this.threshold = (int)(this.capacity * loadFactor);
/*  73 */     this.mask = this.capacity - 1;
/*  74 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
/*  75 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  76 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  78 */     this.keyTable = new int[this.capacity + this.stashCapacity];
/*     */   }
/*     */ 
/*     */   
/*     */   public IntSet(IntSet set) {
/*  83 */     this((int)Math.floor((set.capacity * set.loadFactor)), set.loadFactor);
/*  84 */     this.stashSize = set.stashSize;
/*  85 */     System.arraycopy(set.keyTable, 0, this.keyTable, 0, set.keyTable.length);
/*  86 */     this.size = set.size;
/*  87 */     this.hasZeroValue = set.hasZeroValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(int key) {
/*  92 */     if (key == 0) {
/*  93 */       if (this.hasZeroValue) return false; 
/*  94 */       this.hasZeroValue = true;
/*  95 */       this.size++;
/*  96 */       return true;
/*     */     } 
/*     */     
/*  99 */     int[] keyTable = this.keyTable;
/*     */ 
/*     */     
/* 102 */     int index1 = key & this.mask;
/* 103 */     int key1 = keyTable[index1];
/* 104 */     if (key1 == key) return false;
/*     */     
/* 106 */     int index2 = hash2(key);
/* 107 */     int key2 = keyTable[index2];
/* 108 */     if (key2 == key) return false;
/*     */     
/* 110 */     int index3 = hash3(key);
/* 111 */     int key3 = keyTable[index3];
/* 112 */     if (key3 == key) return false;
/*     */ 
/*     */     
/* 115 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 116 */       if (keyTable[i] == key) return false;
/*     */     
/*     */     } 
/* 119 */     if (key1 == 0) {
/* 120 */       keyTable[index1] = key;
/* 121 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 122 */       return true;
/*     */     } 
/*     */     
/* 125 */     if (key2 == 0) {
/* 126 */       keyTable[index2] = key;
/* 127 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 128 */       return true;
/*     */     } 
/*     */     
/* 131 */     if (key3 == 0) {
/* 132 */       keyTable[index3] = key;
/* 133 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 134 */       return true;
/*     */     } 
/*     */     
/* 137 */     push(key, index1, key1, index2, key2, index3, key3);
/* 138 */     return true;
/*     */   }
/*     */   
/*     */   public void addAll(IntArray array) {
/* 142 */     addAll(array, 0, array.size);
/*     */   }
/*     */   
/*     */   public void addAll(IntArray array, int offset, int length) {
/* 146 */     if (offset + length > array.size)
/* 147 */       throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size); 
/* 148 */     addAll(array.items, offset, length);
/*     */   }
/*     */   
/*     */   public void addAll(int... array) {
/* 152 */     addAll(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void addAll(int[] array, int offset, int length) {
/* 156 */     ensureCapacity(length);
/* 157 */     for (int i = offset, n = i + length; i < n; i++)
/* 158 */       add(array[i]); 
/*     */   }
/*     */   
/*     */   public void addAll(IntSet set) {
/* 162 */     ensureCapacity(set.size);
/* 163 */     IntSetIterator iterator = set.iterator();
/* 164 */     while (iterator.hasNext) {
/* 165 */       add(iterator.next());
/*     */     }
/*     */   }
/*     */   
/*     */   private void addResize(int key) {
/* 170 */     if (key == 0) {
/* 171 */       this.hasZeroValue = true;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 176 */     int index1 = key & this.mask;
/* 177 */     int key1 = this.keyTable[index1];
/* 178 */     if (key1 == 0) {
/* 179 */       this.keyTable[index1] = key;
/* 180 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 184 */     int index2 = hash2(key);
/* 185 */     int key2 = this.keyTable[index2];
/* 186 */     if (key2 == 0) {
/* 187 */       this.keyTable[index2] = key;
/* 188 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 192 */     int index3 = hash3(key);
/* 193 */     int key3 = this.keyTable[index3];
/* 194 */     if (key3 == 0) {
/* 195 */       this.keyTable[index3] = key;
/* 196 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 200 */     push(key, index1, key1, index2, key2, index3, key3);
/*     */   }
/*     */   
/*     */   private void push(int insertKey, int index1, int key1, int index2, int key2, int index3, int key3) {
/* 204 */     int evictedKey, keyTable[] = this.keyTable;
/*     */     
/* 206 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */     
/* 210 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 213 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 215 */           evictedKey = key1;
/* 216 */           keyTable[index1] = insertKey;
/*     */           break;
/*     */         case 1:
/* 219 */           evictedKey = key2;
/* 220 */           keyTable[index2] = insertKey;
/*     */           break;
/*     */         default:
/* 223 */           evictedKey = key3;
/* 224 */           keyTable[index3] = insertKey;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 229 */       index1 = evictedKey & mask;
/* 230 */       key1 = keyTable[index1];
/* 231 */       if (key1 == 0) {
/* 232 */         keyTable[index1] = evictedKey;
/* 233 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 237 */       index2 = hash2(evictedKey);
/* 238 */       key2 = keyTable[index2];
/* 239 */       if (key2 == 0) {
/* 240 */         keyTable[index2] = evictedKey;
/* 241 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 245 */       index3 = hash3(evictedKey);
/* 246 */       key3 = keyTable[index3];
/* 247 */       if (key3 == 0) {
/* 248 */         keyTable[index3] = evictedKey;
/* 249 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 253 */       if (++i == pushIterations)
/*     */         break; 
/* 255 */       insertKey = evictedKey;
/*     */     } 
/*     */     
/* 258 */     addStash(evictedKey);
/*     */   }
/*     */   
/*     */   private void addStash(int key) {
/* 262 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 264 */       resize(this.capacity << 1);
/* 265 */       add(key);
/*     */       
/*     */       return;
/*     */     } 
/* 269 */     int index = this.capacity + this.stashSize;
/* 270 */     this.keyTable[index] = key;
/* 271 */     this.stashSize++;
/* 272 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(int key) {
/* 277 */     if (key == 0) {
/* 278 */       if (!this.hasZeroValue) return false; 
/* 279 */       this.hasZeroValue = false;
/* 280 */       this.size--;
/* 281 */       return true;
/*     */     } 
/*     */     
/* 284 */     int index = key & this.mask;
/* 285 */     if (this.keyTable[index] == key) {
/* 286 */       this.keyTable[index] = 0;
/* 287 */       this.size--;
/* 288 */       return true;
/*     */     } 
/*     */     
/* 291 */     index = hash2(key);
/* 292 */     if (this.keyTable[index] == key) {
/* 293 */       this.keyTable[index] = 0;
/* 294 */       this.size--;
/* 295 */       return true;
/*     */     } 
/*     */     
/* 298 */     index = hash3(key);
/* 299 */     if (this.keyTable[index] == key) {
/* 300 */       this.keyTable[index] = 0;
/* 301 */       this.size--;
/* 302 */       return true;
/*     */     } 
/*     */     
/* 305 */     return removeStash(key);
/*     */   }
/*     */   
/*     */   boolean removeStash(int key) {
/* 309 */     int[] keyTable = this.keyTable;
/* 310 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 311 */       if (keyTable[i] == key) {
/* 312 */         removeStashIndex(i);
/* 313 */         this.size--;
/* 314 */         return true;
/*     */       } 
/*     */     } 
/* 317 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 322 */     this.stashSize--;
/* 323 */     int lastIndex = this.capacity + this.stashSize;
/* 324 */     if (index < lastIndex) this.keyTable[index] = this.keyTable[lastIndex];
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 330 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 331 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 332 */     if (this.capacity <= maximumCapacity)
/* 333 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 334 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 339 */     if (this.capacity <= maximumCapacity) {
/* 340 */       clear();
/*     */       return;
/*     */     } 
/* 343 */     this.hasZeroValue = false;
/* 344 */     this.size = 0;
/* 345 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 349 */     if (this.size == 0)
/* 350 */       return;  int[] keyTable = this.keyTable;
/* 351 */     for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 352 */       keyTable[i] = 0; 
/* 353 */     this.size = 0;
/* 354 */     this.stashSize = 0;
/* 355 */     this.hasZeroValue = false;
/*     */   }
/*     */   
/*     */   public boolean contains(int key) {
/* 359 */     if (key == 0) return this.hasZeroValue; 
/* 360 */     int index = key & this.mask;
/* 361 */     if (this.keyTable[index] != key) {
/* 362 */       index = hash2(key);
/* 363 */       if (this.keyTable[index] != key) {
/* 364 */         index = hash3(key);
/* 365 */         if (this.keyTable[index] != key) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 368 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(int key) {
/* 372 */     int[] keyTable = this.keyTable;
/* 373 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 374 */       if (keyTable[i] == key) return true; 
/* 375 */     }  return false;
/*     */   }
/*     */   
/*     */   public int first() {
/* 379 */     if (this.hasZeroValue) return 0; 
/* 380 */     int[] keyTable = this.keyTable;
/* 381 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 382 */       if (keyTable[i] != 0) return keyTable[i]; 
/* 383 */     }  throw new IllegalStateException("IntSet is empty.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 389 */     int sizeNeeded = this.size + additionalCapacity;
/* 390 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 394 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 396 */     this.capacity = newSize;
/* 397 */     this.threshold = (int)(newSize * this.loadFactor);
/* 398 */     this.mask = newSize - 1;
/* 399 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 400 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 401 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 403 */     int[] oldKeyTable = this.keyTable;
/*     */     
/* 405 */     this.keyTable = new int[newSize + this.stashCapacity];
/*     */     
/* 407 */     int oldSize = this.size;
/* 408 */     this.size = this.hasZeroValue ? 1 : 0;
/* 409 */     this.stashSize = 0;
/* 410 */     if (oldSize > 0)
/* 411 */       for (int i = 0; i < oldEndIndex; i++) {
/* 412 */         int key = oldKeyTable[i];
/* 413 */         if (key != 0) addResize(key);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 419 */     h *= -1262997959;
/* 420 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 424 */     h *= -825114047;
/* 425 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 429 */     int h = 0;
/* 430 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 431 */       if (this.keyTable[i] != 0) h += this.keyTable[i]; 
/* 432 */     }  return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 436 */     if (!(obj instanceof IntSet)) return false; 
/* 437 */     IntSet other = (IntSet)obj;
/* 438 */     if (other.size != this.size) return false; 
/* 439 */     if (other.hasZeroValue != this.hasZeroValue) return false; 
/* 440 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 441 */       if (this.keyTable[i] != 0 && !other.contains(this.keyTable[i])) return false; 
/* 442 */     }  return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 446 */     if (this.size == 0) return "[]"; 
/* 447 */     StringBuilder buffer = new StringBuilder(32);
/* 448 */     buffer.append('[');
/* 449 */     int[] keyTable = this.keyTable;
/* 450 */     int i = keyTable.length;
/* 451 */     if (this.hasZeroValue) {
/* 452 */       buffer.append("0");
/*     */     } else {
/* 454 */       while (i-- > 0) {
/* 455 */         int key = keyTable[i];
/* 456 */         if (key == 0)
/* 457 */           continue;  buffer.append(key);
/*     */       } 
/*     */     } 
/*     */     
/* 461 */     while (i-- > 0) {
/* 462 */       int key = keyTable[i];
/* 463 */       if (key == 0)
/* 464 */         continue;  buffer.append(", ");
/* 465 */       buffer.append(key);
/*     */     } 
/* 467 */     buffer.append(']');
/* 468 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IntSetIterator iterator() {
/* 474 */     if (this.iterator1 == null) {
/* 475 */       this.iterator1 = new IntSetIterator(this);
/* 476 */       this.iterator2 = new IntSetIterator(this);
/*     */     } 
/* 478 */     if (!this.iterator1.valid) {
/* 479 */       this.iterator1.reset();
/* 480 */       this.iterator1.valid = true;
/* 481 */       this.iterator2.valid = false;
/* 482 */       return this.iterator1;
/*     */     } 
/* 484 */     this.iterator2.reset();
/* 485 */     this.iterator2.valid = true;
/* 486 */     this.iterator1.valid = false;
/* 487 */     return this.iterator2;
/*     */   }
/*     */   
/*     */   public static IntSet with(int... array) {
/* 491 */     IntSet set = new IntSet();
/* 492 */     set.addAll(array);
/* 493 */     return set;
/*     */   }
/*     */   
/*     */   public static class IntSetIterator
/*     */   {
/*     */     static final int INDEX_ILLEGAL = -2;
/*     */     static final int INDEX_ZERO = -1;
/*     */     public boolean hasNext;
/*     */     final IntSet set;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public IntSetIterator(IntSet set) {
/* 507 */       this.set = set;
/* 508 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 512 */       this.currentIndex = -2;
/* 513 */       this.nextIndex = -1;
/* 514 */       if (this.set.hasZeroValue) {
/* 515 */         this.hasNext = true;
/*     */       } else {
/* 517 */         findNextIndex();
/*     */       } 
/*     */     }
/*     */     void findNextIndex() {
/* 521 */       this.hasNext = false;
/* 522 */       int[] keyTable = this.set.keyTable;
/* 523 */       for (int n = this.set.capacity + this.set.stashSize; ++this.nextIndex < n;) {
/* 524 */         if (keyTable[this.nextIndex] != 0) {
/* 525 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 532 */       if (this.currentIndex == -1 && this.set.hasZeroValue)
/* 533 */       { this.set.hasZeroValue = false; }
/* 534 */       else { if (this.currentIndex < 0)
/* 535 */           throw new IllegalStateException("next must be called before remove."); 
/* 536 */         if (this.currentIndex >= this.set.capacity) {
/* 537 */           this.set.removeStashIndex(this.currentIndex);
/* 538 */           this.nextIndex = this.currentIndex - 1;
/* 539 */           findNextIndex();
/*     */         } else {
/* 541 */           this.set.keyTable[this.currentIndex] = 0;
/*     */         }  }
/* 543 */        this.currentIndex = -2;
/* 544 */       this.set.size--;
/*     */     }
/*     */     
/*     */     public int next() {
/* 548 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 549 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 550 */       int key = (this.nextIndex == -1) ? 0 : this.set.keyTable[this.nextIndex];
/* 551 */       this.currentIndex = this.nextIndex;
/* 552 */       findNextIndex();
/* 553 */       return key;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntArray toArray() {
/* 558 */       IntArray array = new IntArray(true, this.set.size);
/* 559 */       while (this.hasNext)
/* 560 */         array.add(next()); 
/* 561 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\IntSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */