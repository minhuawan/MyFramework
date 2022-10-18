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
/*     */ public class LongMap<V>
/*     */   implements Iterable<LongMap.Entry<V>>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   private static final int EMPTY = 0;
/*     */   public int size;
/*     */   long[] keyTable;
/*     */   V[] valueTable;
/*     */   int capacity;
/*     */   int stashSize;
/*     */   V zeroValue;
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
/*     */   public LongMap() {
/*  56 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LongMap(int initialCapacity) {
/*  62 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongMap(int initialCapacity, float loadFactor) {
/*  69 */     if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity); 
/*  70 */     initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil((initialCapacity / loadFactor)));
/*  71 */     if (initialCapacity > 1073741824) throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity); 
/*  72 */     this.capacity = initialCapacity;
/*     */     
/*  74 */     if (loadFactor <= 0.0F) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor); 
/*  75 */     this.loadFactor = loadFactor;
/*     */     
/*  77 */     this.threshold = (int)(this.capacity * loadFactor);
/*  78 */     this.mask = this.capacity - 1;
/*  79 */     this.hashShift = 63 - Long.numberOfTrailingZeros(this.capacity);
/*  80 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  81 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  83 */     this.keyTable = new long[this.capacity + this.stashCapacity];
/*  84 */     this.valueTable = (V[])new Object[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public LongMap(LongMap<? extends V> map) {
/*  89 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  90 */     this.stashSize = map.stashSize;
/*  91 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  92 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  93 */     this.size = map.size;
/*  94 */     this.zeroValue = map.zeroValue;
/*  95 */     this.hasZeroValue = map.hasZeroValue;
/*     */   }
/*     */   
/*     */   public V put(long key, V value) {
/*  99 */     if (key == 0L) {
/* 100 */       V oldValue = this.zeroValue;
/* 101 */       this.zeroValue = value;
/* 102 */       if (!this.hasZeroValue) {
/* 103 */         this.hasZeroValue = true;
/* 104 */         this.size++;
/*     */       } 
/* 106 */       return oldValue;
/*     */     } 
/*     */     
/* 109 */     long[] keyTable = this.keyTable;
/*     */ 
/*     */     
/* 112 */     int index1 = (int)(key & this.mask);
/* 113 */     long key1 = keyTable[index1];
/* 114 */     if (key1 == key) {
/* 115 */       V oldValue = this.valueTable[index1];
/* 116 */       this.valueTable[index1] = value;
/* 117 */       return oldValue;
/*     */     } 
/*     */     
/* 120 */     int index2 = hash2(key);
/* 121 */     long key2 = keyTable[index2];
/* 122 */     if (key2 == key) {
/* 123 */       V oldValue = this.valueTable[index2];
/* 124 */       this.valueTable[index2] = value;
/* 125 */       return oldValue;
/*     */     } 
/*     */     
/* 128 */     int index3 = hash3(key);
/* 129 */     long key3 = keyTable[index3];
/* 130 */     if (key3 == key) {
/* 131 */       V oldValue = this.valueTable[index3];
/* 132 */       this.valueTable[index3] = value;
/* 133 */       return oldValue;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 138 */       if (keyTable[i] == key) {
/* 139 */         V oldValue = this.valueTable[i];
/* 140 */         this.valueTable[i] = value;
/* 141 */         return oldValue;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 146 */     if (key1 == 0L) {
/* 147 */       keyTable[index1] = key;
/* 148 */       this.valueTable[index1] = value;
/* 149 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 150 */       return null;
/*     */     } 
/*     */     
/* 153 */     if (key2 == 0L) {
/* 154 */       keyTable[index2] = key;
/* 155 */       this.valueTable[index2] = value;
/* 156 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 157 */       return null;
/*     */     } 
/*     */     
/* 160 */     if (key3 == 0L) {
/* 161 */       keyTable[index3] = key;
/* 162 */       this.valueTable[index3] = value;
/* 163 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 164 */       return null;
/*     */     } 
/*     */     
/* 167 */     push(key, value, index1, key1, index2, key2, index3, key3);
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   public void putAll(LongMap<V> map) {
/* 172 */     for (Entry<V> entry : map.entries()) {
/* 173 */       put(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */   
/*     */   private void putResize(long key, V value) {
/* 178 */     if (key == 0L) {
/* 179 */       this.zeroValue = value;
/* 180 */       this.hasZeroValue = true;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 185 */     int index1 = (int)(key & this.mask);
/* 186 */     long key1 = this.keyTable[index1];
/* 187 */     if (key1 == 0L) {
/* 188 */       this.keyTable[index1] = key;
/* 189 */       this.valueTable[index1] = value;
/* 190 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     int index2 = hash2(key);
/* 195 */     long key2 = this.keyTable[index2];
/* 196 */     if (key2 == 0L) {
/* 197 */       this.keyTable[index2] = key;
/* 198 */       this.valueTable[index2] = value;
/* 199 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 203 */     int index3 = hash3(key);
/* 204 */     long key3 = this.keyTable[index3];
/* 205 */     if (key3 == 0L) {
/* 206 */       this.keyTable[index3] = key;
/* 207 */       this.valueTable[index3] = value;
/* 208 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 212 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   } private void push(long insertKey, V insertValue, int index1, long key1, int index2, long key2, int index3, long key3) {
/*     */     long evictedKey;
/*     */     V evictedValue;
/* 216 */     long[] keyTable = this.keyTable;
/* 217 */     V[] valueTable = this.valueTable;
/* 218 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 226 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 228 */           evictedKey = key1;
/* 229 */           evictedValue = valueTable[index1];
/* 230 */           keyTable[index1] = insertKey;
/* 231 */           valueTable[index1] = insertValue;
/*     */           break;
/*     */         case 1:
/* 234 */           evictedKey = key2;
/* 235 */           evictedValue = valueTable[index2];
/* 236 */           keyTable[index2] = insertKey;
/* 237 */           valueTable[index2] = insertValue;
/*     */           break;
/*     */         default:
/* 240 */           evictedKey = key3;
/* 241 */           evictedValue = valueTable[index3];
/* 242 */           keyTable[index3] = insertKey;
/* 243 */           valueTable[index3] = insertValue;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 248 */       index1 = (int)(evictedKey & mask);
/* 249 */       key1 = keyTable[index1];
/* 250 */       if (key1 == 0L) {
/* 251 */         keyTable[index1] = evictedKey;
/* 252 */         valueTable[index1] = evictedValue;
/* 253 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 257 */       index2 = hash2(evictedKey);
/* 258 */       key2 = keyTable[index2];
/* 259 */       if (key2 == 0L) {
/* 260 */         keyTable[index2] = evictedKey;
/* 261 */         valueTable[index2] = evictedValue;
/* 262 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 266 */       index3 = hash3(evictedKey);
/* 267 */       key3 = keyTable[index3];
/* 268 */       if (key3 == 0L) {
/* 269 */         keyTable[index3] = evictedKey;
/* 270 */         valueTable[index3] = evictedValue;
/* 271 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 275 */       if (++i == pushIterations)
/*     */         break; 
/* 277 */       insertKey = evictedKey;
/* 278 */       insertValue = evictedValue;
/*     */     } 
/*     */     
/* 281 */     putStash(evictedKey, evictedValue);
/*     */   }
/*     */   
/*     */   private void putStash(long key, V value) {
/* 285 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 287 */       resize(this.capacity << 1);
/* 288 */       put(key, value);
/*     */       
/*     */       return;
/*     */     } 
/* 292 */     int index = this.capacity + this.stashSize;
/* 293 */     this.keyTable[index] = key;
/* 294 */     this.valueTable[index] = value;
/* 295 */     this.stashSize++;
/* 296 */     this.size++;
/*     */   }
/*     */   
/*     */   public V get(long key) {
/* 300 */     if (key == 0L) {
/* 301 */       if (!this.hasZeroValue) return null; 
/* 302 */       return this.zeroValue;
/*     */     } 
/* 304 */     int index = (int)(key & this.mask);
/* 305 */     if (this.keyTable[index] != key) {
/* 306 */       index = hash2(key);
/* 307 */       if (this.keyTable[index] != key) {
/* 308 */         index = hash3(key);
/* 309 */         if (this.keyTable[index] != key) return getStash(key, null); 
/*     */       } 
/*     */     } 
/* 312 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   public V get(long key, V defaultValue) {
/* 316 */     if (key == 0L) {
/* 317 */       if (!this.hasZeroValue) return defaultValue; 
/* 318 */       return this.zeroValue;
/*     */     } 
/* 320 */     int index = (int)(key & this.mask);
/* 321 */     if (this.keyTable[index] != key) {
/* 322 */       index = hash2(key);
/* 323 */       if (this.keyTable[index] != key) {
/* 324 */         index = hash3(key);
/* 325 */         if (this.keyTable[index] != key) return getStash(key, defaultValue); 
/*     */       } 
/*     */     } 
/* 328 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private V getStash(long key, V defaultValue) {
/* 332 */     long[] keyTable = this.keyTable;
/* 333 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 334 */       if (keyTable[i] == key) return this.valueTable[i]; 
/* 335 */     }  return defaultValue;
/*     */   }
/*     */   
/*     */   public V remove(long key) {
/* 339 */     if (key == 0L) {
/* 340 */       if (!this.hasZeroValue) return null; 
/* 341 */       V oldValue = this.zeroValue;
/* 342 */       this.zeroValue = null;
/* 343 */       this.hasZeroValue = false;
/* 344 */       this.size--;
/* 345 */       return oldValue;
/*     */     } 
/*     */     
/* 348 */     int index = (int)(key & this.mask);
/* 349 */     if (this.keyTable[index] == key) {
/* 350 */       this.keyTable[index] = 0L;
/* 351 */       V oldValue = this.valueTable[index];
/* 352 */       this.valueTable[index] = null;
/* 353 */       this.size--;
/* 354 */       return oldValue;
/*     */     } 
/*     */     
/* 357 */     index = hash2(key);
/* 358 */     if (this.keyTable[index] == key) {
/* 359 */       this.keyTable[index] = 0L;
/* 360 */       V oldValue = this.valueTable[index];
/* 361 */       this.valueTable[index] = null;
/* 362 */       this.size--;
/* 363 */       return oldValue;
/*     */     } 
/*     */     
/* 366 */     index = hash3(key);
/* 367 */     if (this.keyTable[index] == key) {
/* 368 */       this.keyTable[index] = 0L;
/* 369 */       V oldValue = this.valueTable[index];
/* 370 */       this.valueTable[index] = null;
/* 371 */       this.size--;
/* 372 */       return oldValue;
/*     */     } 
/*     */     
/* 375 */     return removeStash(key);
/*     */   }
/*     */   
/*     */   V removeStash(long key) {
/* 379 */     long[] keyTable = this.keyTable;
/* 380 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 381 */       if (keyTable[i] == key) {
/* 382 */         V oldValue = this.valueTable[i];
/* 383 */         removeStashIndex(i);
/* 384 */         this.size--;
/* 385 */         return oldValue;
/*     */       } 
/*     */     } 
/* 388 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 393 */     this.stashSize--;
/* 394 */     int lastIndex = this.capacity + this.stashSize;
/* 395 */     if (index < lastIndex) {
/* 396 */       this.keyTable[index] = this.keyTable[lastIndex];
/* 397 */       this.valueTable[index] = this.valueTable[lastIndex];
/* 398 */       this.valueTable[lastIndex] = null;
/*     */     } else {
/* 400 */       this.valueTable[index] = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 406 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 407 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 408 */     if (this.capacity <= maximumCapacity)
/* 409 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 410 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 415 */     if (this.capacity <= maximumCapacity) {
/* 416 */       clear();
/*     */       return;
/*     */     } 
/* 419 */     this.zeroValue = null;
/* 420 */     this.hasZeroValue = false;
/* 421 */     this.size = 0;
/* 422 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 426 */     if (this.size == 0)
/* 427 */       return;  long[] keyTable = this.keyTable;
/* 428 */     V[] valueTable = this.valueTable;
/* 429 */     for (int i = this.capacity + this.stashSize; i-- > 0; ) {
/* 430 */       keyTable[i] = 0L;
/* 431 */       valueTable[i] = null;
/*     */     } 
/* 433 */     this.size = 0;
/* 434 */     this.stashSize = 0;
/* 435 */     this.zeroValue = null;
/* 436 */     this.hasZeroValue = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value, boolean identity) {
/* 442 */     V[] valueTable = this.valueTable;
/* 443 */     if (value == null)
/* 444 */     { if (this.hasZeroValue && this.zeroValue == null) return true; 
/* 445 */       long[] keyTable = this.keyTable;
/* 446 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 447 */       { if (keyTable[i] != 0L && valueTable[i] == null) return true;  }  }
/* 448 */     else if (identity)
/* 449 */     { if (value == this.zeroValue) return true; 
/* 450 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 451 */         if (valueTable[i] == value) return true; 
/*     */       }  }
/* 453 */     else { if (this.hasZeroValue && value.equals(this.zeroValue)) return true; 
/* 454 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 455 */         if (value.equals(valueTable[i])) return true; 
/*     */       }  }
/* 457 */      return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(long key) {
/* 461 */     if (key == 0L) return this.hasZeroValue; 
/* 462 */     int index = (int)(key & this.mask);
/* 463 */     if (this.keyTable[index] != key) {
/* 464 */       index = hash2(key);
/* 465 */       if (this.keyTable[index] != key) {
/* 466 */         index = hash3(key);
/* 467 */         if (this.keyTable[index] != key) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 470 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(long key) {
/* 474 */     long[] keyTable = this.keyTable;
/* 475 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 476 */       if (keyTable[i] == key) return true; 
/* 477 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long findKey(Object value, boolean identity, long notFound) {
/* 485 */     V[] valueTable = this.valueTable;
/* 486 */     if (value == null)
/* 487 */     { if (this.hasZeroValue && this.zeroValue == null) return 0L; 
/* 488 */       long[] keyTable = this.keyTable;
/* 489 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 490 */       { if (keyTable[i] != 0L && valueTable[i] == null) return keyTable[i];  }  }
/* 491 */     else if (identity)
/* 492 */     { if (value == this.zeroValue) return 0L; 
/* 493 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 494 */         if (valueTable[i] == value) return this.keyTable[i]; 
/*     */       }  }
/* 496 */     else { if (this.hasZeroValue && value.equals(this.zeroValue)) return 0L; 
/* 497 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 498 */         if (value.equals(valueTable[i])) return this.keyTable[i]; 
/*     */       }  }
/* 500 */      return notFound;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 506 */     int sizeNeeded = this.size + additionalCapacity;
/* 507 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 511 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 513 */     this.capacity = newSize;
/* 514 */     this.threshold = (int)(newSize * this.loadFactor);
/* 515 */     this.mask = newSize - 1;
/* 516 */     this.hashShift = 63 - Long.numberOfTrailingZeros(newSize);
/* 517 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 518 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 520 */     long[] oldKeyTable = this.keyTable;
/* 521 */     V[] oldValueTable = this.valueTable;
/*     */     
/* 523 */     this.keyTable = new long[newSize + this.stashCapacity];
/* 524 */     this.valueTable = (V[])new Object[newSize + this.stashCapacity];
/*     */     
/* 526 */     int oldSize = this.size;
/* 527 */     this.size = this.hasZeroValue ? 1 : 0;
/* 528 */     this.stashSize = 0;
/* 529 */     if (oldSize > 0)
/* 530 */       for (int i = 0; i < oldEndIndex; i++) {
/* 531 */         long key = oldKeyTable[i];
/* 532 */         if (key != 0L) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(long h) {
/* 538 */     h *= -1262997959L;
/* 539 */     return (int)((h ^ h >>> this.hashShift) & this.mask);
/*     */   }
/*     */   
/*     */   private int hash3(long h) {
/* 543 */     h *= -825114047L;
/* 544 */     return (int)((h ^ h >>> this.hashShift) & this.mask);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 548 */     int h = 0;
/* 549 */     if (this.hasZeroValue && this.zeroValue != null) {
/* 550 */       h += this.zeroValue.hashCode();
/*     */     }
/* 552 */     long[] keyTable = this.keyTable;
/* 553 */     V[] valueTable = this.valueTable;
/* 554 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 555 */       long key = keyTable[i];
/* 556 */       if (key != 0L) {
/* 557 */         h += (int)(key ^ key >>> 32L) * 31;
/*     */         
/* 559 */         V value = valueTable[i];
/* 560 */         if (value != null) {
/* 561 */           h += value.hashCode();
/*     */         }
/*     */       } 
/*     */     } 
/* 565 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 569 */     if (obj == this) return true; 
/* 570 */     if (!(obj instanceof LongMap)) return false; 
/* 571 */     LongMap<V> other = (LongMap<V>)obj;
/* 572 */     if (other.size != this.size) return false; 
/* 573 */     if (other.hasZeroValue != this.hasZeroValue) return false; 
/* 574 */     if (this.hasZeroValue) {
/* 575 */       if (other.zeroValue == null)
/* 576 */       { if (this.zeroValue != null) return false;
/*     */          }
/* 578 */       else if (!other.zeroValue.equals(this.zeroValue)) { return false; }
/*     */     
/*     */     }
/* 581 */     long[] keyTable = this.keyTable;
/* 582 */     V[] valueTable = this.valueTable;
/* 583 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 584 */       long key = keyTable[i];
/* 585 */       if (key != 0L) {
/* 586 */         V value = valueTable[i];
/* 587 */         if (value == null) {
/* 588 */           if (!other.containsKey(key) || other.get(key) != null) {
/* 589 */             return false;
/*     */           }
/*     */         }
/* 592 */         else if (!value.equals(other.get(key))) {
/* 593 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 598 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 602 */     if (this.size == 0) return "[]"; 
/* 603 */     StringBuilder buffer = new StringBuilder(32);
/* 604 */     buffer.append('[');
/* 605 */     long[] keyTable = this.keyTable;
/* 606 */     V[] valueTable = this.valueTable;
/* 607 */     int i = keyTable.length;
/* 608 */     while (i-- > 0) {
/* 609 */       long key = keyTable[i];
/* 610 */       if (key == 0L)
/* 611 */         continue;  buffer.append(key);
/* 612 */       buffer.append('=');
/* 613 */       buffer.append(valueTable[i]);
/*     */     } 
/*     */     
/* 616 */     while (i-- > 0) {
/* 617 */       long key = keyTable[i];
/* 618 */       if (key == 0L)
/* 619 */         continue;  buffer.append(", ");
/* 620 */       buffer.append(key);
/* 621 */       buffer.append('=');
/* 622 */       buffer.append(valueTable[i]);
/*     */     } 
/* 624 */     buffer.append(']');
/* 625 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Iterator<Entry<V>> iterator() {
/* 629 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries<V> entries() {
/* 635 */     if (this.entries1 == null) {
/* 636 */       this.entries1 = new Entries(this);
/* 637 */       this.entries2 = new Entries(this);
/*     */     } 
/* 639 */     if (!this.entries1.valid) {
/* 640 */       this.entries1.reset();
/* 641 */       this.entries1.valid = true;
/* 642 */       this.entries2.valid = false;
/* 643 */       return this.entries1;
/*     */     } 
/* 645 */     this.entries2.reset();
/* 646 */     this.entries2.valid = true;
/* 647 */     this.entries1.valid = false;
/* 648 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values<V> values() {
/* 654 */     if (this.values1 == null) {
/* 655 */       this.values1 = new Values<V>(this);
/* 656 */       this.values2 = new Values<V>(this);
/*     */     } 
/* 658 */     if (!this.values1.valid) {
/* 659 */       this.values1.reset();
/* 660 */       this.values1.valid = true;
/* 661 */       this.values2.valid = false;
/* 662 */       return this.values1;
/*     */     } 
/* 664 */     this.values2.reset();
/* 665 */     this.values2.valid = true;
/* 666 */     this.values1.valid = false;
/* 667 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys keys() {
/* 673 */     if (this.keys1 == null) {
/* 674 */       this.keys1 = new Keys(this);
/* 675 */       this.keys2 = new Keys(this);
/*     */     } 
/* 677 */     if (!this.keys1.valid) {
/* 678 */       this.keys1.reset();
/* 679 */       this.keys1.valid = true;
/* 680 */       this.keys2.valid = false;
/* 681 */       return this.keys1;
/*     */     } 
/* 683 */     this.keys2.reset();
/* 684 */     this.keys2.valid = true;
/* 685 */     this.keys1.valid = false;
/* 686 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry<V> {
/*     */     public long key;
/*     */     public V value;
/*     */     
/*     */     public String toString() {
/* 694 */       return this.key + "=" + this.value;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MapIterator<V>
/*     */   {
/*     */     static final int INDEX_ILLEGAL = -2;
/*     */     static final int INDEX_ZERO = -1;
/*     */     public boolean hasNext;
/*     */     final LongMap<V> map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(LongMap<V> map) {
/* 709 */       this.map = map;
/* 710 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 714 */       this.currentIndex = -2;
/* 715 */       this.nextIndex = -1;
/* 716 */       if (this.map.hasZeroValue) {
/* 717 */         this.hasNext = true;
/*     */       } else {
/* 719 */         findNextIndex();
/*     */       } 
/*     */     }
/*     */     void findNextIndex() {
/* 723 */       this.hasNext = false;
/* 724 */       long[] keyTable = this.map.keyTable;
/* 725 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 726 */         if (keyTable[this.nextIndex] != 0L) {
/* 727 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 734 */       if (this.currentIndex == -1 && this.map.hasZeroValue)
/* 735 */       { this.map.zeroValue = null;
/* 736 */         this.map.hasZeroValue = false; }
/* 737 */       else { if (this.currentIndex < 0)
/* 738 */           throw new IllegalStateException("next must be called before remove."); 
/* 739 */         if (this.currentIndex >= this.map.capacity) {
/* 740 */           this.map.removeStashIndex(this.currentIndex);
/* 741 */           this.nextIndex = this.currentIndex - 1;
/* 742 */           findNextIndex();
/*     */         } else {
/* 744 */           this.map.keyTable[this.currentIndex] = 0L;
/* 745 */           this.map.valueTable[this.currentIndex] = null;
/*     */         }  }
/* 747 */        this.currentIndex = -2;
/* 748 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries<V> extends MapIterator<V> implements Iterable<Entry<V>>, Iterator<Entry<V>> {
/* 753 */     private LongMap.Entry<V> entry = new LongMap.Entry<V>();
/*     */     
/*     */     public Entries(LongMap<V> map) {
/* 756 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public LongMap.Entry<V> next() {
/* 761 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 762 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 763 */       long[] keyTable = this.map.keyTable;
/* 764 */       if (this.nextIndex == -1) {
/* 765 */         this.entry.key = 0L;
/* 766 */         this.entry.value = this.map.zeroValue;
/*     */       } else {
/* 768 */         this.entry.key = keyTable[this.nextIndex];
/* 769 */         this.entry.value = this.map.valueTable[this.nextIndex];
/*     */       } 
/* 771 */       this.currentIndex = this.nextIndex;
/* 772 */       findNextIndex();
/* 773 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 777 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 778 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Iterator<LongMap.Entry<V>> iterator() {
/* 782 */       return this;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 786 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values<V> extends MapIterator<V> implements Iterable<V>, Iterator<V> {
/*     */     public Values(LongMap<V> map) {
/* 792 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 796 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 797 */       return this.hasNext;
/*     */     }
/*     */     public V next() {
/*     */       V value;
/* 801 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 802 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       
/* 804 */       if (this.nextIndex == -1) {
/* 805 */         value = this.map.zeroValue;
/*     */       } else {
/* 807 */         value = this.map.valueTable[this.nextIndex];
/* 808 */       }  this.currentIndex = this.nextIndex;
/* 809 */       findNextIndex();
/* 810 */       return value;
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 814 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<V> toArray() {
/* 819 */       Array<V> array = new Array(true, this.map.size);
/* 820 */       while (this.hasNext)
/* 821 */         array.add(next()); 
/* 822 */       return array;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 826 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys extends MapIterator {
/*     */     public Keys(LongMap<V> map) {
/* 832 */       super(map);
/*     */     }
/*     */     
/*     */     public long next() {
/* 836 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 837 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 838 */       long key = (this.nextIndex == -1) ? 0L : this.map.keyTable[this.nextIndex];
/* 839 */       this.currentIndex = this.nextIndex;
/* 840 */       findNextIndex();
/* 841 */       return key;
/*     */     }
/*     */ 
/*     */     
/*     */     public LongArray toArray() {
/* 846 */       LongArray array = new LongArray(true, this.map.size);
/* 847 */       while (this.hasNext)
/* 848 */         array.add(next()); 
/* 849 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\LongMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */