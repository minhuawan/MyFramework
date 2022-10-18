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
/*     */ public class IntFloatMap
/*     */   implements Iterable<IntFloatMap.Entry>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   private static final int EMPTY = 0;
/*     */   public int size;
/*     */   int[] keyTable;
/*     */   float[] valueTable;
/*     */   int capacity;
/*     */   int stashSize;
/*     */   float zeroValue;
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
/*     */   public IntFloatMap() {
/*  57 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFloatMap(int initialCapacity) {
/*  63 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFloatMap(int initialCapacity, float loadFactor) {
/*  70 */     if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity); 
/*  71 */     initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil((initialCapacity / loadFactor)));
/*  72 */     if (initialCapacity > 1073741824) throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity); 
/*  73 */     this.capacity = initialCapacity;
/*     */     
/*  75 */     if (loadFactor <= 0.0F) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor); 
/*  76 */     this.loadFactor = loadFactor;
/*     */     
/*  78 */     this.threshold = (int)(this.capacity * loadFactor);
/*  79 */     this.mask = this.capacity - 1;
/*  80 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
/*  81 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  82 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  84 */     this.keyTable = new int[this.capacity + this.stashCapacity];
/*  85 */     this.valueTable = new float[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFloatMap(IntFloatMap map) {
/*  90 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  91 */     this.stashSize = map.stashSize;
/*  92 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  93 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  94 */     this.size = map.size;
/*  95 */     this.zeroValue = map.zeroValue;
/*  96 */     this.hasZeroValue = map.hasZeroValue;
/*     */   }
/*     */   
/*     */   public void put(int key, float value) {
/* 100 */     if (key == 0) {
/* 101 */       this.zeroValue = value;
/* 102 */       if (!this.hasZeroValue) {
/* 103 */         this.hasZeroValue = true;
/* 104 */         this.size++;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 109 */     int[] keyTable = this.keyTable;
/*     */ 
/*     */     
/* 112 */     int index1 = key & this.mask;
/* 113 */     int key1 = keyTable[index1];
/* 114 */     if (key == key1) {
/* 115 */       this.valueTable[index1] = value;
/*     */       
/*     */       return;
/*     */     } 
/* 119 */     int index2 = hash2(key);
/* 120 */     int key2 = keyTable[index2];
/* 121 */     if (key == key2) {
/* 122 */       this.valueTable[index2] = value;
/*     */       
/*     */       return;
/*     */     } 
/* 126 */     int index3 = hash3(key);
/* 127 */     int key3 = keyTable[index3];
/* 128 */     if (key == key3) {
/* 129 */       this.valueTable[index3] = value;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 134 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 135 */       if (key == keyTable[i]) {
/* 136 */         this.valueTable[i] = value;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     if (key1 == 0) {
/* 143 */       keyTable[index1] = key;
/* 144 */       this.valueTable[index1] = value;
/* 145 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 149 */     if (key2 == 0) {
/* 150 */       keyTable[index2] = key;
/* 151 */       this.valueTable[index2] = value;
/* 152 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 156 */     if (key3 == 0) {
/* 157 */       keyTable[index3] = key;
/* 158 */       this.valueTable[index3] = value;
/* 159 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 163 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   }
/*     */   
/*     */   public void putAll(IntFloatMap map) {
/* 167 */     for (Entry entry : map.entries()) {
/* 168 */       put(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */   
/*     */   private void putResize(int key, float value) {
/* 173 */     if (key == 0) {
/* 174 */       this.zeroValue = value;
/* 175 */       this.hasZeroValue = true;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 180 */     int index1 = key & this.mask;
/* 181 */     int key1 = this.keyTable[index1];
/* 182 */     if (key1 == 0) {
/* 183 */       this.keyTable[index1] = key;
/* 184 */       this.valueTable[index1] = value;
/* 185 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 189 */     int index2 = hash2(key);
/* 190 */     int key2 = this.keyTable[index2];
/* 191 */     if (key2 == 0) {
/* 192 */       this.keyTable[index2] = key;
/* 193 */       this.valueTable[index2] = value;
/* 194 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 198 */     int index3 = hash3(key);
/* 199 */     int key3 = this.keyTable[index3];
/* 200 */     if (key3 == 0) {
/* 201 */       this.keyTable[index3] = key;
/* 202 */       this.valueTable[index3] = value;
/* 203 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 207 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   } private void push(int insertKey, float insertValue, int index1, int key1, int index2, int key2, int index3, int key3) {
/*     */     int evictedKey;
/*     */     float evictedValue;
/* 211 */     int[] keyTable = this.keyTable;
/* 212 */     float[] valueTable = this.valueTable;
/* 213 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 221 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 223 */           evictedKey = key1;
/* 224 */           evictedValue = valueTable[index1];
/* 225 */           keyTable[index1] = insertKey;
/* 226 */           valueTable[index1] = insertValue;
/*     */           break;
/*     */         case 1:
/* 229 */           evictedKey = key2;
/* 230 */           evictedValue = valueTable[index2];
/* 231 */           keyTable[index2] = insertKey;
/* 232 */           valueTable[index2] = insertValue;
/*     */           break;
/*     */         default:
/* 235 */           evictedKey = key3;
/* 236 */           evictedValue = valueTable[index3];
/* 237 */           keyTable[index3] = insertKey;
/* 238 */           valueTable[index3] = insertValue;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 243 */       index1 = evictedKey & mask;
/* 244 */       key1 = keyTable[index1];
/* 245 */       if (key1 == 0) {
/* 246 */         keyTable[index1] = evictedKey;
/* 247 */         valueTable[index1] = evictedValue;
/* 248 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 252 */       index2 = hash2(evictedKey);
/* 253 */       key2 = keyTable[index2];
/* 254 */       if (key2 == 0) {
/* 255 */         keyTable[index2] = evictedKey;
/* 256 */         valueTable[index2] = evictedValue;
/* 257 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 261 */       index3 = hash3(evictedKey);
/* 262 */       key3 = keyTable[index3];
/* 263 */       if (key3 == 0) {
/* 264 */         keyTable[index3] = evictedKey;
/* 265 */         valueTable[index3] = evictedValue;
/* 266 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 270 */       if (++i == pushIterations)
/*     */         break; 
/* 272 */       insertKey = evictedKey;
/* 273 */       insertValue = evictedValue;
/*     */     } 
/*     */     
/* 276 */     putStash(evictedKey, evictedValue);
/*     */   }
/*     */   
/*     */   private void putStash(int key, float value) {
/* 280 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 282 */       resize(this.capacity << 1);
/* 283 */       put(key, value);
/*     */       
/*     */       return;
/*     */     } 
/* 287 */     int index = this.capacity + this.stashSize;
/* 288 */     this.keyTable[index] = key;
/* 289 */     this.valueTable[index] = value;
/* 290 */     this.stashSize++;
/* 291 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public float get(int key, float defaultValue) {
/* 296 */     if (key == 0) {
/* 297 */       if (!this.hasZeroValue) return defaultValue; 
/* 298 */       return this.zeroValue;
/*     */     } 
/* 300 */     int index = key & this.mask;
/* 301 */     if (this.keyTable[index] != key) {
/* 302 */       index = hash2(key);
/* 303 */       if (this.keyTable[index] != key) {
/* 304 */         index = hash3(key);
/* 305 */         if (this.keyTable[index] != key) return getStash(key, defaultValue); 
/*     */       } 
/*     */     } 
/* 308 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private float getStash(int key, float defaultValue) {
/* 312 */     int[] keyTable = this.keyTable;
/* 313 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 314 */       if (key == keyTable[i]) return this.valueTable[i]; 
/* 315 */     }  return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAndIncrement(int key, float defaultValue, float increment) {
/* 321 */     if (key == 0) {
/* 322 */       if (this.hasZeroValue) {
/* 323 */         float f = this.zeroValue;
/* 324 */         this.zeroValue += increment;
/* 325 */         return f;
/*     */       } 
/* 327 */       this.hasZeroValue = true;
/* 328 */       this.zeroValue = defaultValue + increment;
/* 329 */       this.size++;
/* 330 */       return defaultValue;
/*     */     } 
/*     */     
/* 333 */     int index = key & this.mask;
/* 334 */     if (key != this.keyTable[index]) {
/* 335 */       index = hash2(key);
/* 336 */       if (key != this.keyTable[index]) {
/* 337 */         index = hash3(key);
/* 338 */         if (key != this.keyTable[index]) return getAndIncrementStash(key, defaultValue, increment); 
/*     */       } 
/*     */     } 
/* 341 */     float value = this.valueTable[index];
/* 342 */     this.valueTable[index] = value + increment;
/* 343 */     return value;
/*     */   }
/*     */   
/*     */   private float getAndIncrementStash(int key, float defaultValue, float increment) {
/* 347 */     int[] keyTable = this.keyTable;
/* 348 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 349 */       if (key == keyTable[i]) {
/* 350 */         float value = this.valueTable[i];
/* 351 */         this.valueTable[i] = value + increment;
/* 352 */         return value;
/*     */       } 
/* 354 */     }  put(key, defaultValue + increment);
/* 355 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public float remove(int key, float defaultValue) {
/* 359 */     if (key == 0) {
/* 360 */       if (!this.hasZeroValue) return defaultValue; 
/* 361 */       this.hasZeroValue = false;
/* 362 */       this.size--;
/* 363 */       return this.zeroValue;
/*     */     } 
/*     */     
/* 366 */     int index = key & this.mask;
/* 367 */     if (key == this.keyTable[index]) {
/* 368 */       this.keyTable[index] = 0;
/* 369 */       float oldValue = this.valueTable[index];
/* 370 */       this.size--;
/* 371 */       return oldValue;
/*     */     } 
/*     */     
/* 374 */     index = hash2(key);
/* 375 */     if (key == this.keyTable[index]) {
/* 376 */       this.keyTable[index] = 0;
/* 377 */       float oldValue = this.valueTable[index];
/* 378 */       this.size--;
/* 379 */       return oldValue;
/*     */     } 
/*     */     
/* 382 */     index = hash3(key);
/* 383 */     if (key == this.keyTable[index]) {
/* 384 */       this.keyTable[index] = 0;
/* 385 */       float oldValue = this.valueTable[index];
/* 386 */       this.size--;
/* 387 */       return oldValue;
/*     */     } 
/*     */     
/* 390 */     return removeStash(key, defaultValue);
/*     */   }
/*     */   
/*     */   float removeStash(int key, float defaultValue) {
/* 394 */     int[] keyTable = this.keyTable;
/* 395 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 396 */       if (key == keyTable[i]) {
/* 397 */         float oldValue = this.valueTable[i];
/* 398 */         removeStashIndex(i);
/* 399 */         this.size--;
/* 400 */         return oldValue;
/*     */       } 
/*     */     } 
/* 403 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 408 */     this.stashSize--;
/* 409 */     int lastIndex = this.capacity + this.stashSize;
/* 410 */     if (index < lastIndex) {
/* 411 */       this.keyTable[index] = this.keyTable[lastIndex];
/* 412 */       this.valueTable[index] = this.valueTable[lastIndex];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 419 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 420 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 421 */     if (this.capacity <= maximumCapacity)
/* 422 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 423 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 428 */     if (this.capacity <= maximumCapacity) {
/* 429 */       clear();
/*     */       return;
/*     */     } 
/* 432 */     this.hasZeroValue = false;
/* 433 */     this.size = 0;
/* 434 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 438 */     if (this.size == 0)
/* 439 */       return;  int[] keyTable = this.keyTable;
/* 440 */     for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 441 */       keyTable[i] = 0; 
/* 442 */     this.hasZeroValue = false;
/* 443 */     this.size = 0;
/* 444 */     this.stashSize = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(float value) {
/* 450 */     if (this.hasZeroValue && this.zeroValue == value) return true; 
/* 451 */     int[] keyTable = this.keyTable;
/* 452 */     float[] valueTable = this.valueTable;
/* 453 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 454 */       if (keyTable[i] != 0 && valueTable[i] == value) return true; 
/* 455 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(float value, float epsilon) {
/* 461 */     if (this.hasZeroValue && Math.abs(this.zeroValue - value) <= epsilon) return true; 
/* 462 */     float[] valueTable = this.valueTable;
/* 463 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 464 */       if (Math.abs(valueTable[i] - value) <= epsilon) return true; 
/* 465 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(int key) {
/* 469 */     if (key == 0) return this.hasZeroValue; 
/* 470 */     int index = key & this.mask;
/* 471 */     if (this.keyTable[index] != key) {
/* 472 */       index = hash2(key);
/* 473 */       if (this.keyTable[index] != key) {
/* 474 */         index = hash3(key);
/* 475 */         if (this.keyTable[index] != key) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 478 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(int key) {
/* 482 */     int[] keyTable = this.keyTable;
/* 483 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 484 */       if (key == keyTable[i]) return true; 
/* 485 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int findKey(float value, int notFound) {
/* 491 */     if (this.hasZeroValue && this.zeroValue == value) return 0; 
/* 492 */     int[] keyTable = this.keyTable;
/* 493 */     float[] valueTable = this.valueTable;
/* 494 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 495 */       if (keyTable[i] != 0 && valueTable[i] == value) return keyTable[i]; 
/* 496 */     }  return notFound;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 502 */     int sizeNeeded = this.size + additionalCapacity;
/* 503 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 507 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 509 */     this.capacity = newSize;
/* 510 */     this.threshold = (int)(newSize * this.loadFactor);
/* 511 */     this.mask = newSize - 1;
/* 512 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 513 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 514 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 516 */     int[] oldKeyTable = this.keyTable;
/* 517 */     float[] oldValueTable = this.valueTable;
/*     */     
/* 519 */     this.keyTable = new int[newSize + this.stashCapacity];
/* 520 */     this.valueTable = new float[newSize + this.stashCapacity];
/*     */     
/* 522 */     int oldSize = this.size;
/* 523 */     this.size = this.hasZeroValue ? 1 : 0;
/* 524 */     this.stashSize = 0;
/* 525 */     if (oldSize > 0)
/* 526 */       for (int i = 0; i < oldEndIndex; i++) {
/* 527 */         int key = oldKeyTable[i];
/* 528 */         if (key != 0) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 534 */     h *= -1262997959;
/* 535 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 539 */     h *= -825114047;
/* 540 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 544 */     int h = 0;
/* 545 */     if (this.hasZeroValue) {
/* 546 */       h += Float.floatToIntBits(this.zeroValue);
/*     */     }
/* 548 */     int[] keyTable = this.keyTable;
/* 549 */     float[] valueTable = this.valueTable;
/* 550 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 551 */       int key = keyTable[i];
/* 552 */       if (key != 0) {
/* 553 */         h += key * 31;
/*     */         
/* 555 */         float value = valueTable[i];
/* 556 */         h += Float.floatToIntBits(value);
/*     */       } 
/*     */     } 
/* 559 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 563 */     if (obj == this) return true; 
/* 564 */     if (!(obj instanceof IntFloatMap)) return false; 
/* 565 */     IntFloatMap other = (IntFloatMap)obj;
/* 566 */     if (other.size != this.size) return false; 
/* 567 */     if (other.hasZeroValue != this.hasZeroValue) return false; 
/* 568 */     if (this.hasZeroValue && other.zeroValue != this.zeroValue) {
/* 569 */       return false;
/*     */     }
/* 571 */     int[] keyTable = this.keyTable;
/* 572 */     float[] valueTable = this.valueTable;
/* 573 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 574 */       int key = keyTable[i];
/* 575 */       if (key != 0) {
/* 576 */         float otherValue = other.get(key, 0.0F);
/* 577 */         if (otherValue == 0.0F && !other.containsKey(key)) return false; 
/* 578 */         float value = valueTable[i];
/* 579 */         if (otherValue != value) return false; 
/*     */       } 
/*     */     } 
/* 582 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 586 */     if (this.size == 0) return "{}"; 
/* 587 */     StringBuilder buffer = new StringBuilder(32);
/* 588 */     buffer.append('{');
/* 589 */     int[] keyTable = this.keyTable;
/* 590 */     float[] valueTable = this.valueTable;
/* 591 */     int i = keyTable.length;
/* 592 */     if (this.hasZeroValue) {
/* 593 */       buffer.append("0=");
/* 594 */       buffer.append(this.zeroValue);
/*     */     } else {
/* 596 */       while (i-- > 0) {
/* 597 */         int key = keyTable[i];
/* 598 */         if (key == 0)
/* 599 */           continue;  buffer.append(key);
/* 600 */         buffer.append('=');
/* 601 */         buffer.append(valueTable[i]);
/*     */       } 
/*     */     } 
/*     */     
/* 605 */     while (i-- > 0) {
/* 606 */       int key = keyTable[i];
/* 607 */       if (key == 0)
/* 608 */         continue;  buffer.append(", ");
/* 609 */       buffer.append(key);
/* 610 */       buffer.append('=');
/* 611 */       buffer.append(valueTable[i]);
/*     */     } 
/* 613 */     buffer.append('}');
/* 614 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Iterator<Entry> iterator() {
/* 618 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries entries() {
/* 624 */     if (this.entries1 == null) {
/* 625 */       this.entries1 = new Entries(this);
/* 626 */       this.entries2 = new Entries(this);
/*     */     } 
/* 628 */     if (!this.entries1.valid) {
/* 629 */       this.entries1.reset();
/* 630 */       this.entries1.valid = true;
/* 631 */       this.entries2.valid = false;
/* 632 */       return this.entries1;
/*     */     } 
/* 634 */     this.entries2.reset();
/* 635 */     this.entries2.valid = true;
/* 636 */     this.entries1.valid = false;
/* 637 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values values() {
/* 643 */     if (this.values1 == null) {
/* 644 */       this.values1 = new Values(this);
/* 645 */       this.values2 = new Values(this);
/*     */     } 
/* 647 */     if (!this.values1.valid) {
/* 648 */       this.values1.reset();
/* 649 */       this.values1.valid = true;
/* 650 */       this.values2.valid = false;
/* 651 */       return this.values1;
/*     */     } 
/* 653 */     this.values2.reset();
/* 654 */     this.values2.valid = true;
/* 655 */     this.values1.valid = false;
/* 656 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys keys() {
/* 662 */     if (this.keys1 == null) {
/* 663 */       this.keys1 = new Keys(this);
/* 664 */       this.keys2 = new Keys(this);
/*     */     } 
/* 666 */     if (!this.keys1.valid) {
/* 667 */       this.keys1.reset();
/* 668 */       this.keys1.valid = true;
/* 669 */       this.keys2.valid = false;
/* 670 */       return this.keys1;
/*     */     } 
/* 672 */     this.keys2.reset();
/* 673 */     this.keys2.valid = true;
/* 674 */     this.keys1.valid = false;
/* 675 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry {
/*     */     public int key;
/*     */     public float value;
/*     */     
/*     */     public String toString() {
/* 683 */       return this.key + "=" + this.value;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MapIterator
/*     */   {
/*     */     static final int INDEX_ILLEGAL = -2;
/*     */     static final int INDEX_ZERO = -1;
/*     */     public boolean hasNext;
/*     */     final IntFloatMap map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(IntFloatMap map) {
/* 698 */       this.map = map;
/* 699 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 703 */       this.currentIndex = -2;
/* 704 */       this.nextIndex = -1;
/* 705 */       if (this.map.hasZeroValue) {
/* 706 */         this.hasNext = true;
/*     */       } else {
/* 708 */         findNextIndex();
/*     */       } 
/*     */     }
/*     */     void findNextIndex() {
/* 712 */       this.hasNext = false;
/* 713 */       int[] keyTable = this.map.keyTable;
/* 714 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 715 */         if (keyTable[this.nextIndex] != 0) {
/* 716 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 723 */       if (this.currentIndex == -1 && this.map.hasZeroValue)
/* 724 */       { this.map.hasZeroValue = false; }
/* 725 */       else { if (this.currentIndex < 0)
/* 726 */           throw new IllegalStateException("next must be called before remove."); 
/* 727 */         if (this.currentIndex >= this.map.capacity) {
/* 728 */           this.map.removeStashIndex(this.currentIndex);
/* 729 */           this.nextIndex = this.currentIndex - 1;
/* 730 */           findNextIndex();
/*     */         } else {
/* 732 */           this.map.keyTable[this.currentIndex] = 0;
/*     */         }  }
/* 734 */        this.currentIndex = -2;
/* 735 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries extends MapIterator implements Iterable<Entry>, Iterator<Entry> {
/* 740 */     private IntFloatMap.Entry entry = new IntFloatMap.Entry();
/*     */     
/*     */     public Entries(IntFloatMap map) {
/* 743 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public IntFloatMap.Entry next() {
/* 748 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 749 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 750 */       int[] keyTable = this.map.keyTable;
/* 751 */       if (this.nextIndex == -1) {
/* 752 */         this.entry.key = 0;
/* 753 */         this.entry.value = this.map.zeroValue;
/*     */       } else {
/* 755 */         this.entry.key = keyTable[this.nextIndex];
/* 756 */         this.entry.value = this.map.valueTable[this.nextIndex];
/*     */       } 
/* 758 */       this.currentIndex = this.nextIndex;
/* 759 */       findNextIndex();
/* 760 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 764 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 765 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Iterator<IntFloatMap.Entry> iterator() {
/* 769 */       return this;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 773 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values extends MapIterator {
/*     */     public Values(IntFloatMap map) {
/* 779 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 783 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 784 */       return this.hasNext;
/*     */     }
/*     */     public float next() {
/*     */       float value;
/* 788 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 789 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       
/* 791 */       if (this.nextIndex == -1) {
/* 792 */         value = this.map.zeroValue;
/*     */       } else {
/* 794 */         value = this.map.valueTable[this.nextIndex];
/* 795 */       }  this.currentIndex = this.nextIndex;
/* 796 */       findNextIndex();
/* 797 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public FloatArray toArray() {
/* 802 */       FloatArray array = new FloatArray(true, this.map.size);
/* 803 */       while (this.hasNext)
/* 804 */         array.add(next()); 
/* 805 */       return array;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys extends MapIterator {
/*     */     public Keys(IntFloatMap map) {
/* 811 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 815 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 816 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public int next() {
/* 820 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 821 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 822 */       int key = (this.nextIndex == -1) ? 0 : this.map.keyTable[this.nextIndex];
/* 823 */       this.currentIndex = this.nextIndex;
/* 824 */       findNextIndex();
/* 825 */       return key;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntArray toArray() {
/* 830 */       IntArray array = new IntArray(true, this.map.size);
/* 831 */       while (this.hasNext)
/* 832 */         array.add(next()); 
/* 833 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\IntFloatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */