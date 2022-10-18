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
/*     */ public class IntMap<V>
/*     */   implements Iterable<IntMap.Entry<V>>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   private static final int EMPTY = 0;
/*     */   public int size;
/*     */   int[] keyTable;
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
/*     */   public IntMap() {
/*  56 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IntMap(int initialCapacity) {
/*  62 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntMap(int initialCapacity, float loadFactor) {
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
/*  79 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
/*  80 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  81 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  83 */     this.keyTable = new int[this.capacity + this.stashCapacity];
/*  84 */     this.valueTable = (V[])new Object[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public IntMap(IntMap<? extends V> map) {
/*  89 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  90 */     this.stashSize = map.stashSize;
/*  91 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  92 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  93 */     this.size = map.size;
/*  94 */     this.zeroValue = map.zeroValue;
/*  95 */     this.hasZeroValue = map.hasZeroValue;
/*     */   }
/*     */   
/*     */   public V put(int key, V value) {
/*  99 */     if (key == 0) {
/* 100 */       V oldValue = this.zeroValue;
/* 101 */       this.zeroValue = value;
/* 102 */       if (!this.hasZeroValue) {
/* 103 */         this.hasZeroValue = true;
/* 104 */         this.size++;
/*     */       } 
/* 106 */       return oldValue;
/*     */     } 
/*     */     
/* 109 */     int[] keyTable = this.keyTable;
/*     */ 
/*     */     
/* 112 */     int index1 = key & this.mask;
/* 113 */     int key1 = keyTable[index1];
/* 114 */     if (key1 == key) {
/* 115 */       V oldValue = this.valueTable[index1];
/* 116 */       this.valueTable[index1] = value;
/* 117 */       return oldValue;
/*     */     } 
/*     */     
/* 120 */     int index2 = hash2(key);
/* 121 */     int key2 = keyTable[index2];
/* 122 */     if (key2 == key) {
/* 123 */       V oldValue = this.valueTable[index2];
/* 124 */       this.valueTable[index2] = value;
/* 125 */       return oldValue;
/*     */     } 
/*     */     
/* 128 */     int index3 = hash3(key);
/* 129 */     int key3 = keyTable[index3];
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
/* 146 */     if (key1 == 0) {
/* 147 */       keyTable[index1] = key;
/* 148 */       this.valueTable[index1] = value;
/* 149 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 150 */       return null;
/*     */     } 
/*     */     
/* 153 */     if (key2 == 0) {
/* 154 */       keyTable[index2] = key;
/* 155 */       this.valueTable[index2] = value;
/* 156 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 157 */       return null;
/*     */     } 
/*     */     
/* 160 */     if (key3 == 0) {
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
/*     */   public void putAll(IntMap<V> map) {
/* 172 */     for (Entry<V> entry : map.entries()) {
/* 173 */       put(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */   
/*     */   private void putResize(int key, V value) {
/* 178 */     if (key == 0) {
/* 179 */       this.zeroValue = value;
/* 180 */       this.hasZeroValue = true;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 185 */     int index1 = key & this.mask;
/* 186 */     int key1 = this.keyTable[index1];
/* 187 */     if (key1 == 0) {
/* 188 */       this.keyTable[index1] = key;
/* 189 */       this.valueTable[index1] = value;
/* 190 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     int index2 = hash2(key);
/* 195 */     int key2 = this.keyTable[index2];
/* 196 */     if (key2 == 0) {
/* 197 */       this.keyTable[index2] = key;
/* 198 */       this.valueTable[index2] = value;
/* 199 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 203 */     int index3 = hash3(key);
/* 204 */     int key3 = this.keyTable[index3];
/* 205 */     if (key3 == 0) {
/* 206 */       this.keyTable[index3] = key;
/* 207 */       this.valueTable[index3] = value;
/* 208 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 212 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   } private void push(int insertKey, V insertValue, int index1, int key1, int index2, int key2, int index3, int key3) {
/*     */     int evictedKey;
/*     */     V evictedValue;
/* 216 */     int[] keyTable = this.keyTable;
/*     */     
/* 218 */     V[] valueTable = this.valueTable;
/* 219 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 227 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 229 */           evictedKey = key1;
/* 230 */           evictedValue = valueTable[index1];
/* 231 */           keyTable[index1] = insertKey;
/* 232 */           valueTable[index1] = insertValue;
/*     */           break;
/*     */         case 1:
/* 235 */           evictedKey = key2;
/* 236 */           evictedValue = valueTable[index2];
/* 237 */           keyTable[index2] = insertKey;
/* 238 */           valueTable[index2] = insertValue;
/*     */           break;
/*     */         default:
/* 241 */           evictedKey = key3;
/* 242 */           evictedValue = valueTable[index3];
/* 243 */           keyTable[index3] = insertKey;
/* 244 */           valueTable[index3] = insertValue;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 249 */       index1 = evictedKey & mask;
/* 250 */       key1 = keyTable[index1];
/* 251 */       if (key1 == 0) {
/* 252 */         keyTable[index1] = evictedKey;
/* 253 */         valueTable[index1] = evictedValue;
/* 254 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 258 */       index2 = hash2(evictedKey);
/* 259 */       key2 = keyTable[index2];
/* 260 */       if (key2 == 0) {
/* 261 */         keyTable[index2] = evictedKey;
/* 262 */         valueTable[index2] = evictedValue;
/* 263 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 267 */       index3 = hash3(evictedKey);
/* 268 */       key3 = keyTable[index3];
/* 269 */       if (key3 == 0) {
/* 270 */         keyTable[index3] = evictedKey;
/* 271 */         valueTable[index3] = evictedValue;
/* 272 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 276 */       if (++i == pushIterations)
/*     */         break; 
/* 278 */       insertKey = evictedKey;
/* 279 */       insertValue = evictedValue;
/*     */     } 
/*     */     
/* 282 */     putStash(evictedKey, evictedValue);
/*     */   }
/*     */   
/*     */   private void putStash(int key, V value) {
/* 286 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 288 */       resize(this.capacity << 1);
/* 289 */       put(key, value);
/*     */       
/*     */       return;
/*     */     } 
/* 293 */     int index = this.capacity + this.stashSize;
/* 294 */     this.keyTable[index] = key;
/* 295 */     this.valueTable[index] = value;
/* 296 */     this.stashSize++;
/* 297 */     this.size++;
/*     */   }
/*     */   
/*     */   public V get(int key) {
/* 301 */     if (key == 0) {
/* 302 */       if (!this.hasZeroValue) return null; 
/* 303 */       return this.zeroValue;
/*     */     } 
/* 305 */     int index = key & this.mask;
/* 306 */     if (this.keyTable[index] != key) {
/* 307 */       index = hash2(key);
/* 308 */       if (this.keyTable[index] != key) {
/* 309 */         index = hash3(key);
/* 310 */         if (this.keyTable[index] != key) return getStash(key, null); 
/*     */       } 
/*     */     } 
/* 313 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   public V get(int key, V defaultValue) {
/* 317 */     if (key == 0) {
/* 318 */       if (!this.hasZeroValue) return defaultValue; 
/* 319 */       return this.zeroValue;
/*     */     } 
/* 321 */     int index = key & this.mask;
/* 322 */     if (this.keyTable[index] != key) {
/* 323 */       index = hash2(key);
/* 324 */       if (this.keyTable[index] != key) {
/* 325 */         index = hash3(key);
/* 326 */         if (this.keyTable[index] != key) return getStash(key, defaultValue); 
/*     */       } 
/*     */     } 
/* 329 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private V getStash(int key, V defaultValue) {
/* 333 */     int[] keyTable = this.keyTable;
/* 334 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 335 */       if (keyTable[i] == key) return this.valueTable[i]; 
/* 336 */     }  return defaultValue;
/*     */   }
/*     */   
/*     */   public V remove(int key) {
/* 340 */     if (key == 0) {
/* 341 */       if (!this.hasZeroValue) return null; 
/* 342 */       V oldValue = this.zeroValue;
/* 343 */       this.zeroValue = null;
/* 344 */       this.hasZeroValue = false;
/* 345 */       this.size--;
/* 346 */       return oldValue;
/*     */     } 
/*     */     
/* 349 */     int index = key & this.mask;
/* 350 */     if (this.keyTable[index] == key) {
/* 351 */       this.keyTable[index] = 0;
/* 352 */       V oldValue = this.valueTable[index];
/* 353 */       this.valueTable[index] = null;
/* 354 */       this.size--;
/* 355 */       return oldValue;
/*     */     } 
/*     */     
/* 358 */     index = hash2(key);
/* 359 */     if (this.keyTable[index] == key) {
/* 360 */       this.keyTable[index] = 0;
/* 361 */       V oldValue = this.valueTable[index];
/* 362 */       this.valueTable[index] = null;
/* 363 */       this.size--;
/* 364 */       return oldValue;
/*     */     } 
/*     */     
/* 367 */     index = hash3(key);
/* 368 */     if (this.keyTable[index] == key) {
/* 369 */       this.keyTable[index] = 0;
/* 370 */       V oldValue = this.valueTable[index];
/* 371 */       this.valueTable[index] = null;
/* 372 */       this.size--;
/* 373 */       return oldValue;
/*     */     } 
/*     */     
/* 376 */     return removeStash(key);
/*     */   }
/*     */   
/*     */   V removeStash(int key) {
/* 380 */     int[] keyTable = this.keyTable;
/* 381 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 382 */       if (keyTable[i] == key) {
/* 383 */         V oldValue = this.valueTable[i];
/* 384 */         removeStashIndex(i);
/* 385 */         this.size--;
/* 386 */         return oldValue;
/*     */       } 
/*     */     } 
/* 389 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 394 */     this.stashSize--;
/* 395 */     int lastIndex = this.capacity + this.stashSize;
/* 396 */     if (index < lastIndex) {
/* 397 */       this.keyTable[index] = this.keyTable[lastIndex];
/* 398 */       this.valueTable[index] = this.valueTable[lastIndex];
/* 399 */       this.valueTable[lastIndex] = null;
/*     */     } else {
/* 401 */       this.valueTable[index] = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 407 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 408 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 409 */     if (this.capacity <= maximumCapacity)
/* 410 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 411 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 416 */     if (this.capacity <= maximumCapacity) {
/* 417 */       clear();
/*     */       return;
/*     */     } 
/* 420 */     this.zeroValue = null;
/* 421 */     this.hasZeroValue = false;
/* 422 */     this.size = 0;
/* 423 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 427 */     if (this.size == 0)
/* 428 */       return;  int[] keyTable = this.keyTable;
/* 429 */     V[] valueTable = this.valueTable;
/* 430 */     for (int i = this.capacity + this.stashSize; i-- > 0; ) {
/* 431 */       keyTable[i] = 0;
/* 432 */       valueTable[i] = null;
/*     */     } 
/* 434 */     this.size = 0;
/* 435 */     this.stashSize = 0;
/* 436 */     this.zeroValue = null;
/* 437 */     this.hasZeroValue = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value, boolean identity) {
/* 445 */     V[] valueTable = this.valueTable;
/* 446 */     if (value == null)
/* 447 */     { if (this.hasZeroValue && this.zeroValue == null) return true; 
/* 448 */       int[] keyTable = this.keyTable;
/* 449 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 450 */       { if (keyTable[i] != 0 && valueTable[i] == null) return true;  }  }
/* 451 */     else if (identity)
/* 452 */     { if (value == this.zeroValue) return true; 
/* 453 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 454 */         if (valueTable[i] == value) return true; 
/*     */       }  }
/* 456 */     else { if (this.hasZeroValue && value.equals(this.zeroValue)) return true; 
/* 457 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 458 */         if (value.equals(valueTable[i])) return true; 
/*     */       }  }
/* 460 */      return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(int key) {
/* 464 */     if (key == 0) return this.hasZeroValue; 
/* 465 */     int index = key & this.mask;
/* 466 */     if (this.keyTable[index] != key) {
/* 467 */       index = hash2(key);
/* 468 */       if (this.keyTable[index] != key) {
/* 469 */         index = hash3(key);
/* 470 */         if (this.keyTable[index] != key) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 473 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(int key) {
/* 477 */     int[] keyTable = this.keyTable;
/* 478 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 479 */       if (keyTable[i] == key) return true; 
/* 480 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int findKey(Object value, boolean identity, int notFound) {
/* 488 */     V[] valueTable = this.valueTable;
/* 489 */     if (value == null)
/* 490 */     { if (this.hasZeroValue && this.zeroValue == null) return 0; 
/* 491 */       int[] keyTable = this.keyTable;
/* 492 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 493 */       { if (keyTable[i] != 0 && valueTable[i] == null) return keyTable[i];  }  }
/* 494 */     else if (identity)
/* 495 */     { if (value == this.zeroValue) return 0; 
/* 496 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 497 */         if (valueTable[i] == value) return this.keyTable[i]; 
/*     */       }  }
/* 499 */     else { if (this.hasZeroValue && value.equals(this.zeroValue)) return 0; 
/* 500 */       for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 501 */         if (value.equals(valueTable[i])) return this.keyTable[i]; 
/*     */       }  }
/* 503 */      return notFound;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 509 */     int sizeNeeded = this.size + additionalCapacity;
/* 510 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 514 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 516 */     this.capacity = newSize;
/* 517 */     this.threshold = (int)(newSize * this.loadFactor);
/* 518 */     this.mask = newSize - 1;
/* 519 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 520 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 521 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 523 */     int[] oldKeyTable = this.keyTable;
/* 524 */     V[] oldValueTable = this.valueTable;
/*     */     
/* 526 */     this.keyTable = new int[newSize + this.stashCapacity];
/* 527 */     this.valueTable = (V[])new Object[newSize + this.stashCapacity];
/*     */     
/* 529 */     int oldSize = this.size;
/* 530 */     this.size = this.hasZeroValue ? 1 : 0;
/* 531 */     this.stashSize = 0;
/* 532 */     if (oldSize > 0)
/* 533 */       for (int i = 0; i < oldEndIndex; i++) {
/* 534 */         int key = oldKeyTable[i];
/* 535 */         if (key != 0) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 541 */     h *= -1262997959;
/* 542 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 546 */     h *= -825114047;
/* 547 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 551 */     int h = 0;
/* 552 */     if (this.hasZeroValue && this.zeroValue != null) {
/* 553 */       h += this.zeroValue.hashCode();
/*     */     }
/* 555 */     int[] keyTable = this.keyTable;
/* 556 */     V[] valueTable = this.valueTable;
/* 557 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 558 */       int key = keyTable[i];
/* 559 */       if (key != 0) {
/* 560 */         h += key * 31;
/*     */         
/* 562 */         V value = valueTable[i];
/* 563 */         if (value != null) {
/* 564 */           h += value.hashCode();
/*     */         }
/*     */       } 
/*     */     } 
/* 568 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 572 */     if (obj == this) return true; 
/* 573 */     if (!(obj instanceof IntMap)) return false; 
/* 574 */     IntMap<V> other = (IntMap<V>)obj;
/* 575 */     if (other.size != this.size) return false; 
/* 576 */     if (other.hasZeroValue != this.hasZeroValue) return false; 
/* 577 */     if (this.hasZeroValue) {
/* 578 */       if (other.zeroValue == null)
/* 579 */       { if (this.zeroValue != null) return false;
/*     */          }
/* 581 */       else if (!other.zeroValue.equals(this.zeroValue)) { return false; }
/*     */     
/*     */     }
/* 584 */     int[] keyTable = this.keyTable;
/* 585 */     V[] valueTable = this.valueTable;
/* 586 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 587 */       int key = keyTable[i];
/* 588 */       if (key != 0) {
/* 589 */         V value = valueTable[i];
/* 590 */         if (value == null) {
/* 591 */           if (!other.containsKey(key) || other.get(key) != null) {
/* 592 */             return false;
/*     */           }
/*     */         }
/* 595 */         else if (!value.equals(other.get(key))) {
/* 596 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 601 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 605 */     if (this.size == 0) return "[]"; 
/* 606 */     StringBuilder buffer = new StringBuilder(32);
/* 607 */     buffer.append('[');
/* 608 */     int[] keyTable = this.keyTable;
/* 609 */     V[] valueTable = this.valueTable;
/* 610 */     int i = keyTable.length;
/* 611 */     if (this.hasZeroValue) {
/* 612 */       buffer.append("0=");
/* 613 */       buffer.append(this.zeroValue);
/*     */     } else {
/* 615 */       while (i-- > 0) {
/* 616 */         int key = keyTable[i];
/* 617 */         if (key == 0)
/* 618 */           continue;  buffer.append(key);
/* 619 */         buffer.append('=');
/* 620 */         buffer.append(valueTable[i]);
/*     */       } 
/*     */     } 
/*     */     
/* 624 */     while (i-- > 0) {
/* 625 */       int key = keyTable[i];
/* 626 */       if (key == 0)
/* 627 */         continue;  buffer.append(", ");
/* 628 */       buffer.append(key);
/* 629 */       buffer.append('=');
/* 630 */       buffer.append(valueTable[i]);
/*     */     } 
/* 632 */     buffer.append(']');
/* 633 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Iterator<Entry<V>> iterator() {
/* 637 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries<V> entries() {
/* 643 */     if (this.entries1 == null) {
/* 644 */       this.entries1 = new Entries(this);
/* 645 */       this.entries2 = new Entries(this);
/*     */     } 
/* 647 */     if (!this.entries1.valid) {
/* 648 */       this.entries1.reset();
/* 649 */       this.entries1.valid = true;
/* 650 */       this.entries2.valid = false;
/* 651 */       return this.entries1;
/*     */     } 
/* 653 */     this.entries2.reset();
/* 654 */     this.entries2.valid = true;
/* 655 */     this.entries1.valid = false;
/* 656 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values<V> values() {
/* 662 */     if (this.values1 == null) {
/* 663 */       this.values1 = new Values<V>(this);
/* 664 */       this.values2 = new Values<V>(this);
/*     */     } 
/* 666 */     if (!this.values1.valid) {
/* 667 */       this.values1.reset();
/* 668 */       this.values1.valid = true;
/* 669 */       this.values2.valid = false;
/* 670 */       return this.values1;
/*     */     } 
/* 672 */     this.values2.reset();
/* 673 */     this.values2.valid = true;
/* 674 */     this.values1.valid = false;
/* 675 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys keys() {
/* 681 */     if (this.keys1 == null) {
/* 682 */       this.keys1 = new Keys(this);
/* 683 */       this.keys2 = new Keys(this);
/*     */     } 
/* 685 */     if (!this.keys1.valid) {
/* 686 */       this.keys1.reset();
/* 687 */       this.keys1.valid = true;
/* 688 */       this.keys2.valid = false;
/* 689 */       return this.keys1;
/*     */     } 
/* 691 */     this.keys2.reset();
/* 692 */     this.keys2.valid = true;
/* 693 */     this.keys1.valid = false;
/* 694 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry<V> {
/*     */     public int key;
/*     */     public V value;
/*     */     
/*     */     public String toString() {
/* 702 */       return this.key + "=" + this.value;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MapIterator<V>
/*     */   {
/*     */     static final int INDEX_ILLEGAL = -2;
/*     */     static final int INDEX_ZERO = -1;
/*     */     public boolean hasNext;
/*     */     final IntMap<V> map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(IntMap<V> map) {
/* 717 */       this.map = map;
/* 718 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 722 */       this.currentIndex = -2;
/* 723 */       this.nextIndex = -1;
/* 724 */       if (this.map.hasZeroValue) {
/* 725 */         this.hasNext = true;
/*     */       } else {
/* 727 */         findNextIndex();
/*     */       } 
/*     */     }
/*     */     void findNextIndex() {
/* 731 */       this.hasNext = false;
/* 732 */       int[] keyTable = this.map.keyTable;
/* 733 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 734 */         if (keyTable[this.nextIndex] != 0) {
/* 735 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 742 */       if (this.currentIndex == -1 && this.map.hasZeroValue)
/* 743 */       { this.map.zeroValue = null;
/* 744 */         this.map.hasZeroValue = false; }
/* 745 */       else { if (this.currentIndex < 0)
/* 746 */           throw new IllegalStateException("next must be called before remove."); 
/* 747 */         if (this.currentIndex >= this.map.capacity) {
/* 748 */           this.map.removeStashIndex(this.currentIndex);
/* 749 */           this.nextIndex = this.currentIndex - 1;
/* 750 */           findNextIndex();
/*     */         } else {
/* 752 */           this.map.keyTable[this.currentIndex] = 0;
/* 753 */           this.map.valueTable[this.currentIndex] = null;
/*     */         }  }
/* 755 */        this.currentIndex = -2;
/* 756 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries<V> extends MapIterator<V> implements Iterable<Entry<V>>, Iterator<Entry<V>> {
/* 761 */     private IntMap.Entry<V> entry = new IntMap.Entry<V>();
/*     */     
/*     */     public Entries(IntMap<V> map) {
/* 764 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public IntMap.Entry<V> next() {
/* 769 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 770 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 771 */       int[] keyTable = this.map.keyTable;
/* 772 */       if (this.nextIndex == -1) {
/* 773 */         this.entry.key = 0;
/* 774 */         this.entry.value = this.map.zeroValue;
/*     */       } else {
/* 776 */         this.entry.key = keyTable[this.nextIndex];
/* 777 */         this.entry.value = this.map.valueTable[this.nextIndex];
/*     */       } 
/* 779 */       this.currentIndex = this.nextIndex;
/* 780 */       findNextIndex();
/* 781 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 785 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 786 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Iterator<IntMap.Entry<V>> iterator() {
/* 790 */       return this;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 794 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values<V> extends MapIterator<V> implements Iterable<V>, Iterator<V> {
/*     */     public Values(IntMap<V> map) {
/* 800 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 804 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 805 */       return this.hasNext;
/*     */     }
/*     */     public V next() {
/*     */       V value;
/* 809 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 810 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       
/* 812 */       if (this.nextIndex == -1) {
/* 813 */         value = this.map.zeroValue;
/*     */       } else {
/* 815 */         value = this.map.valueTable[this.nextIndex];
/* 816 */       }  this.currentIndex = this.nextIndex;
/* 817 */       findNextIndex();
/* 818 */       return value;
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 822 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<V> toArray() {
/* 827 */       Array<V> array = new Array(true, this.map.size);
/* 828 */       while (this.hasNext)
/* 829 */         array.add(next()); 
/* 830 */       return array;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 834 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys extends MapIterator {
/*     */     public Keys(IntMap<V> map) {
/* 840 */       super(map);
/*     */     }
/*     */     
/*     */     public int next() {
/* 844 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 845 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 846 */       int key = (this.nextIndex == -1) ? 0 : this.map.keyTable[this.nextIndex];
/* 847 */       this.currentIndex = this.nextIndex;
/* 848 */       findNextIndex();
/* 849 */       return key;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntArray toArray() {
/* 854 */       IntArray array = new IntArray(true, this.map.size);
/* 855 */       while (this.hasNext)
/* 856 */         array.add(next()); 
/* 857 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\IntMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */