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
/*     */ public class IntIntMap
/*     */   implements Iterable<IntIntMap.Entry>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   private static final int EMPTY = 0;
/*     */   public int size;
/*     */   int[] keyTable;
/*     */   int[] valueTable;
/*     */   int capacity;
/*     */   int stashSize;
/*     */   int zeroValue;
/*     */   boolean hasZeroValue;
/*     */   private float loadFactor;
/*     */   private int hashShift;
/*     */   private int mask;
/*     */   private int threshold;
/*     */   private int stashCapacity;
/*     */   private int pushIterations;
/*     */   private Entries entries1;
/*     */   private Entries entries2;
/*     */   private Values values1;
/*     */   private Values values2;
/*     */   private Keys keys1;
/*     */   private Keys keys2;
/*     */   
/*     */   public IntIntMap() {
/*  55 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IntIntMap(int initialCapacity) {
/*  61 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntIntMap(int initialCapacity, float loadFactor) {
/*  68 */     if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity); 
/*  69 */     initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil((initialCapacity / loadFactor)));
/*  70 */     if (initialCapacity > 1073741824) throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity); 
/*  71 */     this.capacity = initialCapacity;
/*     */     
/*  73 */     if (loadFactor <= 0.0F) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor); 
/*  74 */     this.loadFactor = loadFactor;
/*     */     
/*  76 */     this.threshold = (int)(this.capacity * loadFactor);
/*  77 */     this.mask = this.capacity - 1;
/*  78 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
/*  79 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  80 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  82 */     this.keyTable = new int[this.capacity + this.stashCapacity];
/*  83 */     this.valueTable = new int[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public IntIntMap(IntIntMap map) {
/*  88 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  89 */     this.stashSize = map.stashSize;
/*  90 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  91 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  92 */     this.size = map.size;
/*  93 */     this.zeroValue = map.zeroValue;
/*  94 */     this.hasZeroValue = map.hasZeroValue;
/*     */   }
/*     */   
/*     */   public void put(int key, int value) {
/*  98 */     if (key == 0) {
/*  99 */       this.zeroValue = value;
/* 100 */       if (!this.hasZeroValue) {
/* 101 */         this.hasZeroValue = true;
/* 102 */         this.size++;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 107 */     int[] keyTable = this.keyTable;
/*     */ 
/*     */     
/* 110 */     int index1 = key & this.mask;
/* 111 */     int key1 = keyTable[index1];
/* 112 */     if (key == key1) {
/* 113 */       this.valueTable[index1] = value;
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     int index2 = hash2(key);
/* 118 */     int key2 = keyTable[index2];
/* 119 */     if (key == key2) {
/* 120 */       this.valueTable[index2] = value;
/*     */       
/*     */       return;
/*     */     } 
/* 124 */     int index3 = hash3(key);
/* 125 */     int key3 = keyTable[index3];
/* 126 */     if (key == key3) {
/* 127 */       this.valueTable[index3] = value;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 132 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 133 */       if (key == keyTable[i]) {
/* 134 */         this.valueTable[i] = value;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     if (key1 == 0) {
/* 141 */       keyTable[index1] = key;
/* 142 */       this.valueTable[index1] = value;
/* 143 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 147 */     if (key2 == 0) {
/* 148 */       keyTable[index2] = key;
/* 149 */       this.valueTable[index2] = value;
/* 150 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 154 */     if (key3 == 0) {
/* 155 */       keyTable[index3] = key;
/* 156 */       this.valueTable[index3] = value;
/* 157 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 161 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   }
/*     */   
/*     */   public void putAll(IntIntMap map) {
/* 165 */     for (Entry entry : map.entries()) {
/* 166 */       put(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */   
/*     */   private void putResize(int key, int value) {
/* 171 */     if (key == 0) {
/* 172 */       this.zeroValue = value;
/* 173 */       this.hasZeroValue = true;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 178 */     int index1 = key & this.mask;
/* 179 */     int key1 = this.keyTable[index1];
/* 180 */     if (key1 == 0) {
/* 181 */       this.keyTable[index1] = key;
/* 182 */       this.valueTable[index1] = value;
/* 183 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 187 */     int index2 = hash2(key);
/* 188 */     int key2 = this.keyTable[index2];
/* 189 */     if (key2 == 0) {
/* 190 */       this.keyTable[index2] = key;
/* 191 */       this.valueTable[index2] = value;
/* 192 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 196 */     int index3 = hash3(key);
/* 197 */     int key3 = this.keyTable[index3];
/* 198 */     if (key3 == 0) {
/* 199 */       this.keyTable[index3] = key;
/* 200 */       this.valueTable[index3] = value;
/* 201 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 205 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   }
/*     */   
/*     */   private void push(int insertKey, int insertValue, int index1, int key1, int index2, int key2, int index3, int key3) {
/* 209 */     int evictedKey, evictedValue, keyTable[] = this.keyTable;
/* 210 */     int[] valueTable = this.valueTable;
/* 211 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 219 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 221 */           evictedKey = key1;
/* 222 */           evictedValue = valueTable[index1];
/* 223 */           keyTable[index1] = insertKey;
/* 224 */           valueTable[index1] = insertValue;
/*     */           break;
/*     */         case 1:
/* 227 */           evictedKey = key2;
/* 228 */           evictedValue = valueTable[index2];
/* 229 */           keyTable[index2] = insertKey;
/* 230 */           valueTable[index2] = insertValue;
/*     */           break;
/*     */         default:
/* 233 */           evictedKey = key3;
/* 234 */           evictedValue = valueTable[index3];
/* 235 */           keyTable[index3] = insertKey;
/* 236 */           valueTable[index3] = insertValue;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 241 */       index1 = evictedKey & mask;
/* 242 */       key1 = keyTable[index1];
/* 243 */       if (key1 == 0) {
/* 244 */         keyTable[index1] = evictedKey;
/* 245 */         valueTable[index1] = evictedValue;
/* 246 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 250 */       index2 = hash2(evictedKey);
/* 251 */       key2 = keyTable[index2];
/* 252 */       if (key2 == 0) {
/* 253 */         keyTable[index2] = evictedKey;
/* 254 */         valueTable[index2] = evictedValue;
/* 255 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 259 */       index3 = hash3(evictedKey);
/* 260 */       key3 = keyTable[index3];
/* 261 */       if (key3 == 0) {
/* 262 */         keyTable[index3] = evictedKey;
/* 263 */         valueTable[index3] = evictedValue;
/* 264 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 268 */       if (++i == pushIterations)
/*     */         break; 
/* 270 */       insertKey = evictedKey;
/* 271 */       insertValue = evictedValue;
/*     */     } 
/*     */     
/* 274 */     putStash(evictedKey, evictedValue);
/*     */   }
/*     */   
/*     */   private void putStash(int key, int value) {
/* 278 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 280 */       resize(this.capacity << 1);
/* 281 */       put(key, value);
/*     */       
/*     */       return;
/*     */     } 
/* 285 */     int index = this.capacity + this.stashSize;
/* 286 */     this.keyTable[index] = key;
/* 287 */     this.valueTable[index] = value;
/* 288 */     this.stashSize++;
/* 289 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int get(int key, int defaultValue) {
/* 294 */     if (key == 0) {
/* 295 */       if (!this.hasZeroValue) return defaultValue; 
/* 296 */       return this.zeroValue;
/*     */     } 
/* 298 */     int index = key & this.mask;
/* 299 */     if (this.keyTable[index] != key) {
/* 300 */       index = hash2(key);
/* 301 */       if (this.keyTable[index] != key) {
/* 302 */         index = hash3(key);
/* 303 */         if (this.keyTable[index] != key) return getStash(key, defaultValue); 
/*     */       } 
/*     */     } 
/* 306 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private int getStash(int key, int defaultValue) {
/* 310 */     int[] keyTable = this.keyTable;
/* 311 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 312 */       if (key == keyTable[i]) return this.valueTable[i]; 
/* 313 */     }  return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAndIncrement(int key, int defaultValue, int increment) {
/* 319 */     if (key == 0) {
/* 320 */       if (this.hasZeroValue) {
/* 321 */         int i = this.zeroValue;
/* 322 */         this.zeroValue += increment;
/* 323 */         return i;
/*     */       } 
/* 325 */       this.hasZeroValue = true;
/* 326 */       this.zeroValue = defaultValue + increment;
/* 327 */       this.size++;
/* 328 */       return defaultValue;
/*     */     } 
/*     */     
/* 331 */     int index = key & this.mask;
/* 332 */     if (key != this.keyTable[index]) {
/* 333 */       index = hash2(key);
/* 334 */       if (key != this.keyTable[index]) {
/* 335 */         index = hash3(key);
/* 336 */         if (key != this.keyTable[index]) return getAndIncrementStash(key, defaultValue, increment); 
/*     */       } 
/*     */     } 
/* 339 */     int value = this.valueTable[index];
/* 340 */     this.valueTable[index] = value + increment;
/* 341 */     return value;
/*     */   }
/*     */   
/*     */   private int getAndIncrementStash(int key, int defaultValue, int increment) {
/* 345 */     int[] keyTable = this.keyTable;
/* 346 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 347 */       if (key == keyTable[i]) {
/* 348 */         int value = this.valueTable[i];
/* 349 */         this.valueTable[i] = value + increment;
/* 350 */         return value;
/*     */       } 
/* 352 */     }  put(key, defaultValue + increment);
/* 353 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public int remove(int key, int defaultValue) {
/* 357 */     if (key == 0) {
/* 358 */       if (!this.hasZeroValue) return defaultValue; 
/* 359 */       this.hasZeroValue = false;
/* 360 */       this.size--;
/* 361 */       return this.zeroValue;
/*     */     } 
/*     */     
/* 364 */     int index = key & this.mask;
/* 365 */     if (key == this.keyTable[index]) {
/* 366 */       this.keyTable[index] = 0;
/* 367 */       int oldValue = this.valueTable[index];
/* 368 */       this.size--;
/* 369 */       return oldValue;
/*     */     } 
/*     */     
/* 372 */     index = hash2(key);
/* 373 */     if (key == this.keyTable[index]) {
/* 374 */       this.keyTable[index] = 0;
/* 375 */       int oldValue = this.valueTable[index];
/* 376 */       this.size--;
/* 377 */       return oldValue;
/*     */     } 
/*     */     
/* 380 */     index = hash3(key);
/* 381 */     if (key == this.keyTable[index]) {
/* 382 */       this.keyTable[index] = 0;
/* 383 */       int oldValue = this.valueTable[index];
/* 384 */       this.size--;
/* 385 */       return oldValue;
/*     */     } 
/*     */     
/* 388 */     return removeStash(key, defaultValue);
/*     */   }
/*     */   
/*     */   int removeStash(int key, int defaultValue) {
/* 392 */     int[] keyTable = this.keyTable;
/* 393 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 394 */       if (key == keyTable[i]) {
/* 395 */         int oldValue = this.valueTable[i];
/* 396 */         removeStashIndex(i);
/* 397 */         this.size--;
/* 398 */         return oldValue;
/*     */       } 
/*     */     } 
/* 401 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 406 */     this.stashSize--;
/* 407 */     int lastIndex = this.capacity + this.stashSize;
/* 408 */     if (index < lastIndex) {
/* 409 */       this.keyTable[index] = this.keyTable[lastIndex];
/* 410 */       this.valueTable[index] = this.valueTable[lastIndex];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 417 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 418 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 419 */     if (this.capacity <= maximumCapacity)
/* 420 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 421 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 426 */     if (this.capacity <= maximumCapacity) {
/* 427 */       clear();
/*     */       return;
/*     */     } 
/* 430 */     this.hasZeroValue = false;
/* 431 */     this.size = 0;
/* 432 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 436 */     if (this.size == 0)
/* 437 */       return;  int[] keyTable = this.keyTable;
/* 438 */     for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 439 */       keyTable[i] = 0; 
/* 440 */     this.size = 0;
/* 441 */     this.stashSize = 0;
/* 442 */     this.hasZeroValue = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(int value) {
/* 448 */     if (this.hasZeroValue && this.zeroValue == value) return true; 
/* 449 */     int[] keyTable = this.keyTable, valueTable = this.valueTable;
/* 450 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 451 */       if (keyTable[i] != 0 && valueTable[i] == value) return true; 
/* 452 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(int key) {
/* 456 */     if (key == 0) return this.hasZeroValue; 
/* 457 */     int index = key & this.mask;
/* 458 */     if (this.keyTable[index] != key) {
/* 459 */       index = hash2(key);
/* 460 */       if (this.keyTable[index] != key) {
/* 461 */         index = hash3(key);
/* 462 */         if (this.keyTable[index] != key) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 465 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(int key) {
/* 469 */     int[] keyTable = this.keyTable;
/* 470 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 471 */       if (key == keyTable[i]) return true; 
/* 472 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int findKey(int value, int notFound) {
/* 478 */     if (this.hasZeroValue && this.zeroValue == value) return 0; 
/* 479 */     int[] keyTable = this.keyTable, valueTable = this.valueTable;
/* 480 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 481 */       if (keyTable[i] != 0 && valueTable[i] == value) return keyTable[i]; 
/* 482 */     }  return notFound;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 488 */     int sizeNeeded = this.size + additionalCapacity;
/* 489 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 493 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 495 */     this.capacity = newSize;
/* 496 */     this.threshold = (int)(newSize * this.loadFactor);
/* 497 */     this.mask = newSize - 1;
/* 498 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 499 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 500 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 502 */     int[] oldKeyTable = this.keyTable;
/* 503 */     int[] oldValueTable = this.valueTable;
/*     */     
/* 505 */     this.keyTable = new int[newSize + this.stashCapacity];
/* 506 */     this.valueTable = new int[newSize + this.stashCapacity];
/*     */     
/* 508 */     int oldSize = this.size;
/* 509 */     this.size = this.hasZeroValue ? 1 : 0;
/* 510 */     this.stashSize = 0;
/* 511 */     if (oldSize > 0)
/* 512 */       for (int i = 0; i < oldEndIndex; i++) {
/* 513 */         int key = oldKeyTable[i];
/* 514 */         if (key != 0) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 520 */     h *= -1262997959;
/* 521 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 525 */     h *= -825114047;
/* 526 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 530 */     int h = 0;
/* 531 */     if (this.hasZeroValue) {
/* 532 */       h += Float.floatToIntBits(this.zeroValue);
/*     */     }
/* 534 */     int[] keyTable = this.keyTable;
/* 535 */     int[] valueTable = this.valueTable;
/* 536 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 537 */       int key = keyTable[i];
/* 538 */       if (key != 0) {
/* 539 */         h += key * 31;
/*     */         
/* 541 */         int value = valueTable[i];
/* 542 */         h += value;
/*     */       } 
/*     */     } 
/* 545 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 549 */     if (obj == this) return true; 
/* 550 */     if (!(obj instanceof IntIntMap)) return false; 
/* 551 */     IntIntMap other = (IntIntMap)obj;
/* 552 */     if (other.size != this.size) return false; 
/* 553 */     if (other.hasZeroValue != this.hasZeroValue) return false; 
/* 554 */     if (this.hasZeroValue && other.zeroValue != this.zeroValue) {
/* 555 */       return false;
/*     */     }
/* 557 */     int[] keyTable = this.keyTable;
/* 558 */     int[] valueTable = this.valueTable;
/* 559 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 560 */       int key = keyTable[i];
/* 561 */       if (key != 0) {
/* 562 */         int otherValue = other.get(key, 0);
/* 563 */         if (otherValue == 0 && !other.containsKey(key)) return false; 
/* 564 */         int value = valueTable[i];
/* 565 */         if (otherValue != value) return false; 
/*     */       } 
/*     */     } 
/* 568 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 572 */     if (this.size == 0) return "{}"; 
/* 573 */     StringBuilder buffer = new StringBuilder(32);
/* 574 */     buffer.append('{');
/* 575 */     int[] keyTable = this.keyTable;
/* 576 */     int[] valueTable = this.valueTable;
/* 577 */     int i = keyTable.length;
/* 578 */     if (this.hasZeroValue) {
/* 579 */       buffer.append("0=");
/* 580 */       buffer.append(this.zeroValue);
/*     */     } else {
/* 582 */       while (i-- > 0) {
/* 583 */         int key = keyTable[i];
/* 584 */         if (key == 0)
/* 585 */           continue;  buffer.append(key);
/* 586 */         buffer.append('=');
/* 587 */         buffer.append(valueTable[i]);
/*     */       } 
/*     */     } 
/*     */     
/* 591 */     while (i-- > 0) {
/* 592 */       int key = keyTable[i];
/* 593 */       if (key == 0)
/* 594 */         continue;  buffer.append(", ");
/* 595 */       buffer.append(key);
/* 596 */       buffer.append('=');
/* 597 */       buffer.append(valueTable[i]);
/*     */     } 
/* 599 */     buffer.append('}');
/* 600 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Iterator<Entry> iterator() {
/* 604 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries entries() {
/* 610 */     if (this.entries1 == null) {
/* 611 */       this.entries1 = new Entries(this);
/* 612 */       this.entries2 = new Entries(this);
/*     */     } 
/* 614 */     if (!this.entries1.valid) {
/* 615 */       this.entries1.reset();
/* 616 */       this.entries1.valid = true;
/* 617 */       this.entries2.valid = false;
/* 618 */       return this.entries1;
/*     */     } 
/* 620 */     this.entries2.reset();
/* 621 */     this.entries2.valid = true;
/* 622 */     this.entries1.valid = false;
/* 623 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values values() {
/* 629 */     if (this.values1 == null) {
/* 630 */       this.values1 = new Values(this);
/* 631 */       this.values2 = new Values(this);
/*     */     } 
/* 633 */     if (!this.values1.valid) {
/* 634 */       this.values1.reset();
/* 635 */       this.values1.valid = true;
/* 636 */       this.values2.valid = false;
/* 637 */       return this.values1;
/*     */     } 
/* 639 */     this.values2.reset();
/* 640 */     this.values2.valid = true;
/* 641 */     this.values1.valid = false;
/* 642 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys keys() {
/* 648 */     if (this.keys1 == null) {
/* 649 */       this.keys1 = new Keys(this);
/* 650 */       this.keys2 = new Keys(this);
/*     */     } 
/* 652 */     if (!this.keys1.valid) {
/* 653 */       this.keys1.reset();
/* 654 */       this.keys1.valid = true;
/* 655 */       this.keys2.valid = false;
/* 656 */       return this.keys1;
/*     */     } 
/* 658 */     this.keys2.reset();
/* 659 */     this.keys2.valid = true;
/* 660 */     this.keys1.valid = false;
/* 661 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry {
/*     */     public int key;
/*     */     public int value;
/*     */     
/*     */     public String toString() {
/* 669 */       return this.key + "=" + this.value;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MapIterator
/*     */   {
/*     */     static final int INDEX_ILLEGAL = -2;
/*     */     static final int INDEX_ZERO = -1;
/*     */     public boolean hasNext;
/*     */     final IntIntMap map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(IntIntMap map) {
/* 684 */       this.map = map;
/* 685 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 689 */       this.currentIndex = -2;
/* 690 */       this.nextIndex = -1;
/* 691 */       if (this.map.hasZeroValue) {
/* 692 */         this.hasNext = true;
/*     */       } else {
/* 694 */         findNextIndex();
/*     */       } 
/*     */     }
/*     */     void findNextIndex() {
/* 698 */       this.hasNext = false;
/* 699 */       int[] keyTable = this.map.keyTable;
/* 700 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 701 */         if (keyTable[this.nextIndex] != 0) {
/* 702 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 709 */       if (this.currentIndex == -1 && this.map.hasZeroValue)
/* 710 */       { this.map.hasZeroValue = false; }
/* 711 */       else { if (this.currentIndex < 0)
/* 712 */           throw new IllegalStateException("next must be called before remove."); 
/* 713 */         if (this.currentIndex >= this.map.capacity) {
/* 714 */           this.map.removeStashIndex(this.currentIndex);
/* 715 */           this.nextIndex = this.currentIndex - 1;
/* 716 */           findNextIndex();
/*     */         } else {
/* 718 */           this.map.keyTable[this.currentIndex] = 0;
/*     */         }  }
/* 720 */        this.currentIndex = -2;
/* 721 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries extends MapIterator implements Iterable<Entry>, Iterator<Entry> {
/* 726 */     private IntIntMap.Entry entry = new IntIntMap.Entry();
/*     */     
/*     */     public Entries(IntIntMap map) {
/* 729 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public IntIntMap.Entry next() {
/* 734 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 735 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 736 */       int[] keyTable = this.map.keyTable;
/* 737 */       if (this.nextIndex == -1) {
/* 738 */         this.entry.key = 0;
/* 739 */         this.entry.value = this.map.zeroValue;
/*     */       } else {
/* 741 */         this.entry.key = keyTable[this.nextIndex];
/* 742 */         this.entry.value = this.map.valueTable[this.nextIndex];
/*     */       } 
/* 744 */       this.currentIndex = this.nextIndex;
/* 745 */       findNextIndex();
/* 746 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 750 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 751 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Iterator<IntIntMap.Entry> iterator() {
/* 755 */       return this;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 759 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values extends MapIterator {
/*     */     public Values(IntIntMap map) {
/* 765 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 769 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 770 */       return this.hasNext;
/*     */     }
/*     */     public int next() {
/*     */       int value;
/* 774 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 775 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       
/* 777 */       if (this.nextIndex == -1) {
/* 778 */         value = this.map.zeroValue;
/*     */       } else {
/* 780 */         value = this.map.valueTable[this.nextIndex];
/* 781 */       }  this.currentIndex = this.nextIndex;
/* 782 */       findNextIndex();
/* 783 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntArray toArray() {
/* 788 */       IntArray array = new IntArray(true, this.map.size);
/* 789 */       while (this.hasNext)
/* 790 */         array.add(next()); 
/* 791 */       return array;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys extends MapIterator {
/*     */     public Keys(IntIntMap map) {
/* 797 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 801 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 802 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public int next() {
/* 806 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 807 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 808 */       int key = (this.nextIndex == -1) ? 0 : this.map.keyTable[this.nextIndex];
/* 809 */       this.currentIndex = this.nextIndex;
/* 810 */       findNextIndex();
/* 811 */       return key;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntArray toArray() {
/* 816 */       IntArray array = new IntArray(true, this.map.size);
/* 817 */       while (this.hasNext)
/* 818 */         array.add(next()); 
/* 819 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\IntIntMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */