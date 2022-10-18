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
/*     */ public class IdentityMap<K, V>
/*     */   implements Iterable<IdentityMap.Entry<K, V>>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   public int size;
/*     */   K[] keyTable;
/*     */   V[] valueTable;
/*     */   int capacity;
/*     */   int stashSize;
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
/*     */   public IdentityMap() {
/*  54 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IdentityMap(int initialCapacity) {
/*  60 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdentityMap(int initialCapacity, float loadFactor) {
/*  67 */     if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity); 
/*  68 */     initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil((initialCapacity / loadFactor)));
/*  69 */     if (initialCapacity > 1073741824) throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity); 
/*  70 */     this.capacity = initialCapacity;
/*     */     
/*  72 */     if (loadFactor <= 0.0F) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor); 
/*  73 */     this.loadFactor = loadFactor;
/*     */     
/*  75 */     this.threshold = (int)(this.capacity * loadFactor);
/*  76 */     this.mask = this.capacity - 1;
/*  77 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
/*  78 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  79 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  81 */     this.keyTable = (K[])new Object[this.capacity + this.stashCapacity];
/*  82 */     this.valueTable = (V[])new Object[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public IdentityMap(IdentityMap map) {
/*  87 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  88 */     this.stashSize = map.stashSize;
/*  89 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  90 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  91 */     this.size = map.size;
/*     */   }
/*     */   
/*     */   public V put(K key, V value) {
/*  95 */     if (key == null) throw new IllegalArgumentException("key cannot be null."); 
/*  96 */     K[] keyTable = this.keyTable;
/*     */ 
/*     */     
/*  99 */     int hashCode = System.identityHashCode(key);
/* 100 */     int index1 = hashCode & this.mask;
/* 101 */     K key1 = keyTable[index1];
/* 102 */     if (key1 == key) {
/* 103 */       V oldValue = this.valueTable[index1];
/* 104 */       this.valueTable[index1] = value;
/* 105 */       return oldValue;
/*     */     } 
/*     */     
/* 108 */     int index2 = hash2(hashCode);
/* 109 */     K key2 = keyTable[index2];
/* 110 */     if (key2 == key) {
/* 111 */       V oldValue = this.valueTable[index2];
/* 112 */       this.valueTable[index2] = value;
/* 113 */       return oldValue;
/*     */     } 
/*     */     
/* 116 */     int index3 = hash3(hashCode);
/* 117 */     K key3 = keyTable[index3];
/* 118 */     if (key3 == key) {
/* 119 */       V oldValue = this.valueTable[index3];
/* 120 */       this.valueTable[index3] = value;
/* 121 */       return oldValue;
/*     */     } 
/*     */ 
/*     */     
/* 125 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 126 */       if (keyTable[i] == key) {
/* 127 */         V oldValue = this.valueTable[i];
/* 128 */         this.valueTable[i] = value;
/* 129 */         return oldValue;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (key1 == null) {
/* 135 */       keyTable[index1] = key;
/* 136 */       this.valueTable[index1] = value;
/* 137 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 138 */       return null;
/*     */     } 
/*     */     
/* 141 */     if (key2 == null) {
/* 142 */       keyTable[index2] = key;
/* 143 */       this.valueTable[index2] = value;
/* 144 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 145 */       return null;
/*     */     } 
/*     */     
/* 148 */     if (key3 == null) {
/* 149 */       keyTable[index3] = key;
/* 150 */       this.valueTable[index3] = value;
/* 151 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 152 */       return null;
/*     */     } 
/*     */     
/* 155 */     push(key, value, index1, key1, index2, key2, index3, key3);
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void putResize(K key, V value) {
/* 162 */     int hashCode = System.identityHashCode(key);
/* 163 */     int index1 = hashCode & this.mask;
/* 164 */     K key1 = this.keyTable[index1];
/* 165 */     if (key1 == null) {
/* 166 */       this.keyTable[index1] = key;
/* 167 */       this.valueTable[index1] = value;
/* 168 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 172 */     int index2 = hash2(hashCode);
/* 173 */     K key2 = this.keyTable[index2];
/* 174 */     if (key2 == null) {
/* 175 */       this.keyTable[index2] = key;
/* 176 */       this.valueTable[index2] = value;
/* 177 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 181 */     int index3 = hash3(hashCode);
/* 182 */     K key3 = this.keyTable[index3];
/* 183 */     if (key3 == null) {
/* 184 */       this.keyTable[index3] = key;
/* 185 */       this.valueTable[index3] = value;
/* 186 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 190 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   } private void push(K insertKey, V insertValue, int index1, K key1, int index2, K key2, int index3, K key3) {
/*     */     K evictedKey;
/*     */     V evictedValue;
/* 194 */     K[] keyTable = this.keyTable;
/* 195 */     V[] valueTable = this.valueTable;
/* 196 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 204 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 206 */           evictedKey = key1;
/* 207 */           evictedValue = valueTable[index1];
/* 208 */           keyTable[index1] = insertKey;
/* 209 */           valueTable[index1] = insertValue;
/*     */           break;
/*     */         case 1:
/* 212 */           evictedKey = key2;
/* 213 */           evictedValue = valueTable[index2];
/* 214 */           keyTable[index2] = insertKey;
/* 215 */           valueTable[index2] = insertValue;
/*     */           break;
/*     */         default:
/* 218 */           evictedKey = key3;
/* 219 */           evictedValue = valueTable[index3];
/* 220 */           keyTable[index3] = insertKey;
/* 221 */           valueTable[index3] = insertValue;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 226 */       int hashCode = System.identityHashCode(evictedKey);
/* 227 */       index1 = hashCode & mask;
/* 228 */       key1 = keyTable[index1];
/* 229 */       if (key1 == null) {
/* 230 */         keyTable[index1] = evictedKey;
/* 231 */         valueTable[index1] = evictedValue;
/* 232 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 236 */       index2 = hash2(hashCode);
/* 237 */       key2 = keyTable[index2];
/* 238 */       if (key2 == null) {
/* 239 */         keyTable[index2] = evictedKey;
/* 240 */         valueTable[index2] = evictedValue;
/* 241 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 245 */       index3 = hash3(hashCode);
/* 246 */       key3 = keyTable[index3];
/* 247 */       if (key3 == null) {
/* 248 */         keyTable[index3] = evictedKey;
/* 249 */         valueTable[index3] = evictedValue;
/* 250 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 254 */       if (++i == pushIterations)
/*     */         break; 
/* 256 */       insertKey = evictedKey;
/* 257 */       insertValue = evictedValue;
/*     */     } 
/*     */     
/* 260 */     putStash(evictedKey, evictedValue);
/*     */   }
/*     */   
/*     */   private void putStash(K key, V value) {
/* 264 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 266 */       resize(this.capacity << 1);
/* 267 */       put(key, value);
/*     */       
/*     */       return;
/*     */     } 
/* 271 */     int index = this.capacity + this.stashSize;
/* 272 */     this.keyTable[index] = key;
/* 273 */     this.valueTable[index] = value;
/* 274 */     this.stashSize++;
/* 275 */     this.size++;
/*     */   }
/*     */   
/*     */   public V get(K key) {
/* 279 */     int hashCode = System.identityHashCode(key);
/* 280 */     int index = hashCode & this.mask;
/* 281 */     if (key != this.keyTable[index]) {
/* 282 */       index = hash2(hashCode);
/* 283 */       if (key != this.keyTable[index]) {
/* 284 */         index = hash3(hashCode);
/* 285 */         if (key != this.keyTable[index]) return getStash(key, null); 
/*     */       } 
/*     */     } 
/* 288 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   public V get(K key, V defaultValue) {
/* 292 */     int hashCode = System.identityHashCode(key);
/* 293 */     int index = hashCode & this.mask;
/* 294 */     if (key != this.keyTable[index]) {
/* 295 */       index = hash2(hashCode);
/* 296 */       if (key != this.keyTable[index]) {
/* 297 */         index = hash3(hashCode);
/* 298 */         if (key != this.keyTable[index]) return getStash(key, defaultValue); 
/*     */       } 
/*     */     } 
/* 301 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private V getStash(K key, V defaultValue) {
/* 305 */     K[] keyTable = this.keyTable;
/* 306 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 307 */       if (keyTable[i] == key) return this.valueTable[i]; 
/* 308 */     }  return defaultValue;
/*     */   }
/*     */   
/*     */   public V remove(K key) {
/* 312 */     int hashCode = System.identityHashCode(key);
/* 313 */     int index = hashCode & this.mask;
/* 314 */     if (this.keyTable[index] == key) {
/* 315 */       this.keyTable[index] = null;
/* 316 */       V oldValue = this.valueTable[index];
/* 317 */       this.valueTable[index] = null;
/* 318 */       this.size--;
/* 319 */       return oldValue;
/*     */     } 
/*     */     
/* 322 */     index = hash2(hashCode);
/* 323 */     if (this.keyTable[index] == key) {
/* 324 */       this.keyTable[index] = null;
/* 325 */       V oldValue = this.valueTable[index];
/* 326 */       this.valueTable[index] = null;
/* 327 */       this.size--;
/* 328 */       return oldValue;
/*     */     } 
/*     */     
/* 331 */     index = hash3(hashCode);
/* 332 */     if (this.keyTable[index] == key) {
/* 333 */       this.keyTable[index] = null;
/* 334 */       V oldValue = this.valueTable[index];
/* 335 */       this.valueTable[index] = null;
/* 336 */       this.size--;
/* 337 */       return oldValue;
/*     */     } 
/*     */     
/* 340 */     return removeStash(key);
/*     */   }
/*     */   
/*     */   V removeStash(K key) {
/* 344 */     K[] keyTable = this.keyTable;
/* 345 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 346 */       if (keyTable[i] == key) {
/* 347 */         V oldValue = this.valueTable[i];
/* 348 */         removeStashIndex(i);
/* 349 */         this.size--;
/* 350 */         return oldValue;
/*     */       } 
/*     */     } 
/* 353 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 358 */     this.stashSize--;
/* 359 */     int lastIndex = this.capacity + this.stashSize;
/* 360 */     if (index < lastIndex) {
/* 361 */       this.keyTable[index] = this.keyTable[lastIndex];
/* 362 */       this.valueTable[index] = this.valueTable[lastIndex];
/* 363 */       this.valueTable[lastIndex] = null;
/*     */     } else {
/* 365 */       this.valueTable[index] = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 371 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 372 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 373 */     if (this.capacity <= maximumCapacity)
/* 374 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 375 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 380 */     if (this.capacity <= maximumCapacity) {
/* 381 */       clear();
/*     */       return;
/*     */     } 
/* 384 */     this.size = 0;
/* 385 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 389 */     if (this.size == 0)
/* 390 */       return;  K[] keyTable = this.keyTable;
/* 391 */     V[] valueTable = this.valueTable;
/* 392 */     for (int i = this.capacity + this.stashSize; i-- > 0; ) {
/* 393 */       keyTable[i] = null;
/* 394 */       valueTable[i] = null;
/*     */     } 
/* 396 */     this.size = 0;
/* 397 */     this.stashSize = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value, boolean identity) {
/* 405 */     V[] valueTable = this.valueTable;
/* 406 */     if (value == null)
/* 407 */     { K[] keyTable = this.keyTable;
/* 408 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 409 */       { if (keyTable[i] != null && valueTable[i] == null) return true;  }  }
/* 410 */     else if (identity)
/* 411 */     { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 412 */         if (valueTable[i] == value) return true; 
/*     */       }  }
/* 414 */     else { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 415 */         if (value.equals(valueTable[i])) return true; 
/*     */       }  }
/* 417 */      return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(K key) {
/* 421 */     int hashCode = System.identityHashCode(key);
/* 422 */     int index = hashCode & this.mask;
/* 423 */     if (key != this.keyTable[index]) {
/* 424 */       index = hash2(hashCode);
/* 425 */       if (key != this.keyTable[index]) {
/* 426 */         index = hash3(hashCode);
/* 427 */         if (key != this.keyTable[index]) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 430 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(K key) {
/* 434 */     K[] keyTable = this.keyTable;
/* 435 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 436 */       if (keyTable[i] == key) return true; 
/* 437 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K findKey(Object value, boolean identity) {
/* 445 */     V[] valueTable = this.valueTable;
/* 446 */     if (value == null)
/* 447 */     { K[] keyTable = this.keyTable;
/* 448 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 449 */       { if (keyTable[i] != null && valueTable[i] == null) return keyTable[i];  }  }
/* 450 */     else if (identity)
/* 451 */     { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 452 */         if (valueTable[i] == value) return this.keyTable[i]; 
/*     */       }  }
/* 454 */     else { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 455 */         if (value.equals(valueTable[i])) return this.keyTable[i]; 
/*     */       }  }
/* 457 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 463 */     int sizeNeeded = this.size + additionalCapacity;
/* 464 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 468 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 470 */     this.capacity = newSize;
/* 471 */     this.threshold = (int)(newSize * this.loadFactor);
/* 472 */     this.mask = newSize - 1;
/* 473 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 474 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 475 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 477 */     K[] oldKeyTable = this.keyTable;
/* 478 */     V[] oldValueTable = this.valueTable;
/*     */     
/* 480 */     this.keyTable = (K[])new Object[newSize + this.stashCapacity];
/* 481 */     this.valueTable = (V[])new Object[newSize + this.stashCapacity];
/*     */     
/* 483 */     int oldSize = this.size;
/* 484 */     this.size = 0;
/* 485 */     this.stashSize = 0;
/* 486 */     if (oldSize > 0)
/* 487 */       for (int i = 0; i < oldEndIndex; i++) {
/* 488 */         K key = oldKeyTable[i];
/* 489 */         if (key != null) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 495 */     h *= -1262997959;
/* 496 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 500 */     h *= -825114047;
/* 501 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 505 */     int h = 0;
/* 506 */     K[] keyTable = this.keyTable;
/* 507 */     V[] valueTable = this.valueTable;
/* 508 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 509 */       K key = keyTable[i];
/* 510 */       if (key != null) {
/* 511 */         h += key.hashCode() * 31;
/*     */         
/* 513 */         V value = valueTable[i];
/* 514 */         if (value != null) {
/* 515 */           h += value.hashCode();
/*     */         }
/*     */       } 
/*     */     } 
/* 519 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 523 */     if (obj == this) return true; 
/* 524 */     if (!(obj instanceof IdentityMap)) return false; 
/* 525 */     IdentityMap<K, V> other = (IdentityMap<K, V>)obj;
/* 526 */     if (other.size != this.size) return false; 
/* 527 */     K[] keyTable = this.keyTable;
/* 528 */     V[] valueTable = this.valueTable;
/* 529 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 530 */       K key = keyTable[i];
/* 531 */       if (key != null) {
/* 532 */         V value = valueTable[i];
/* 533 */         if (value == null) {
/* 534 */           if (!other.containsKey(key) || other.get(key) != null) {
/* 535 */             return false;
/*     */           }
/*     */         }
/* 538 */         else if (!value.equals(other.get(key))) {
/* 539 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 544 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 548 */     if (this.size == 0) return "[]"; 
/* 549 */     StringBuilder buffer = new StringBuilder(32);
/* 550 */     buffer.append('[');
/* 551 */     K[] keyTable = this.keyTable;
/* 552 */     V[] valueTable = this.valueTable;
/* 553 */     int i = keyTable.length;
/* 554 */     while (i-- > 0) {
/* 555 */       K key = keyTable[i];
/* 556 */       if (key == null)
/* 557 */         continue;  buffer.append(key);
/* 558 */       buffer.append('=');
/* 559 */       buffer.append(valueTable[i]);
/*     */     } 
/*     */     
/* 562 */     while (i-- > 0) {
/* 563 */       K key = keyTable[i];
/* 564 */       if (key == null)
/* 565 */         continue;  buffer.append(", ");
/* 566 */       buffer.append(key);
/* 567 */       buffer.append('=');
/* 568 */       buffer.append(valueTable[i]);
/*     */     } 
/* 570 */     buffer.append(']');
/* 571 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Iterator<Entry<K, V>> iterator() {
/* 575 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries<K, V> entries() {
/* 581 */     if (this.entries1 == null) {
/* 582 */       this.entries1 = new Entries<K, V>(this);
/* 583 */       this.entries2 = new Entries<K, V>(this);
/*     */     } 
/* 585 */     if (!this.entries1.valid) {
/* 586 */       this.entries1.reset();
/* 587 */       this.entries1.valid = true;
/* 588 */       this.entries2.valid = false;
/* 589 */       return this.entries1;
/*     */     } 
/* 591 */     this.entries2.reset();
/* 592 */     this.entries2.valid = true;
/* 593 */     this.entries1.valid = false;
/* 594 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values<V> values() {
/* 600 */     if (this.values1 == null) {
/* 601 */       this.values1 = new Values<V>(this);
/* 602 */       this.values2 = new Values<V>(this);
/*     */     } 
/* 604 */     if (!this.values1.valid) {
/* 605 */       this.values1.reset();
/* 606 */       this.values1.valid = true;
/* 607 */       this.values2.valid = false;
/* 608 */       return this.values1;
/*     */     } 
/* 610 */     this.values2.reset();
/* 611 */     this.values2.valid = true;
/* 612 */     this.values1.valid = false;
/* 613 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys<K> keys() {
/* 619 */     if (this.keys1 == null) {
/* 620 */       this.keys1 = new Keys<K>(this);
/* 621 */       this.keys2 = new Keys<K>(this);
/*     */     } 
/* 623 */     if (!this.keys1.valid) {
/* 624 */       this.keys1.reset();
/* 625 */       this.keys1.valid = true;
/* 626 */       this.keys2.valid = false;
/* 627 */       return this.keys1;
/*     */     } 
/* 629 */     this.keys2.reset();
/* 630 */     this.keys2.valid = true;
/* 631 */     this.keys1.valid = false;
/* 632 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry<K, V> {
/*     */     public K key;
/*     */     public V value;
/*     */     
/*     */     public String toString() {
/* 640 */       return (new java.lang.StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class MapIterator<K, V, I> implements Iterable<I>, Iterator<I> {
/*     */     public boolean hasNext;
/*     */     final IdentityMap<K, V> map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(IdentityMap<K, V> map) {
/* 652 */       this.map = map;
/* 653 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 657 */       this.currentIndex = -1;
/* 658 */       this.nextIndex = -1;
/* 659 */       findNextIndex();
/*     */     }
/*     */     
/*     */     void findNextIndex() {
/* 663 */       this.hasNext = false;
/* 664 */       K[] keyTable = this.map.keyTable;
/* 665 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 666 */         if (keyTable[this.nextIndex] != null) {
/* 667 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 674 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 675 */       if (this.currentIndex >= this.map.capacity) {
/* 676 */         this.map.removeStashIndex(this.currentIndex);
/* 677 */         this.nextIndex = this.currentIndex - 1;
/* 678 */         findNextIndex();
/*     */       } else {
/* 680 */         this.map.keyTable[this.currentIndex] = null;
/* 681 */         this.map.valueTable[this.currentIndex] = null;
/*     */       } 
/* 683 */       this.currentIndex = -1;
/* 684 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries<K, V> extends MapIterator<K, V, Entry<K, V>> {
/* 689 */     private IdentityMap.Entry<K, V> entry = new IdentityMap.Entry<K, V>();
/*     */     
/*     */     public Entries(IdentityMap<K, V> map) {
/* 692 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public IdentityMap.Entry<K, V> next() {
/* 697 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 698 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 699 */       K[] keyTable = this.map.keyTable;
/* 700 */       this.entry.key = keyTable[this.nextIndex];
/* 701 */       this.entry.value = this.map.valueTable[this.nextIndex];
/* 702 */       this.currentIndex = this.nextIndex;
/* 703 */       findNextIndex();
/* 704 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 708 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 709 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Iterator<IdentityMap.Entry<K, V>> iterator() {
/* 713 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values<V> extends MapIterator<Object, V, V> {
/*     */     public Values(IdentityMap<?, V> map) {
/* 719 */       super((IdentityMap)map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 723 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 724 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public V next() {
/* 728 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 729 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 730 */       V value = this.map.valueTable[this.nextIndex];
/* 731 */       this.currentIndex = this.nextIndex;
/* 732 */       findNextIndex();
/* 733 */       return value;
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 737 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<V> toArray() {
/* 742 */       Array<V> array = new Array(true, this.map.size);
/* 743 */       while (this.hasNext)
/* 744 */         array.add(next()); 
/* 745 */       return array;
/*     */     }
/*     */ 
/*     */     
/*     */     public void toArray(Array<V> array) {
/* 750 */       while (this.hasNext)
/* 751 */         array.add(next()); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys<K> extends MapIterator<K, Object, K> {
/*     */     public Keys(IdentityMap<K, ?> map) {
/* 757 */       super((IdentityMap)map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 761 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 762 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public K next() {
/* 766 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 767 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 768 */       K key = this.map.keyTable[this.nextIndex];
/* 769 */       this.currentIndex = this.nextIndex;
/* 770 */       findNextIndex();
/* 771 */       return key;
/*     */     }
/*     */     
/*     */     public Iterator<K> iterator() {
/* 775 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray() {
/* 780 */       Array<K> array = new Array(true, this.map.size);
/* 781 */       while (this.hasNext)
/* 782 */         array.add(next()); 
/* 783 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\IdentityMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */