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
/*     */ public class ObjectMap<K, V>
/*     */   implements Iterable<ObjectMap.Entry<K, V>>
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
/*     */   public ObjectMap() {
/*  53 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMap(int initialCapacity) {
/*  59 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMap(int initialCapacity, float loadFactor) {
/*  66 */     if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity); 
/*  67 */     initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil((initialCapacity / loadFactor)));
/*  68 */     if (initialCapacity > 1073741824) throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity); 
/*  69 */     this.capacity = initialCapacity;
/*     */     
/*  71 */     if (loadFactor <= 0.0F) throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor); 
/*  72 */     this.loadFactor = loadFactor;
/*     */     
/*  74 */     this.threshold = (int)(this.capacity * loadFactor);
/*  75 */     this.mask = this.capacity - 1;
/*  76 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
/*  77 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) * 2);
/*  78 */     this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
/*     */     
/*  80 */     this.keyTable = (K[])new Object[this.capacity + this.stashCapacity];
/*  81 */     this.valueTable = (V[])new Object[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectMap(ObjectMap<? extends K, ? extends V> map) {
/*  86 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  87 */     this.stashSize = map.stashSize;
/*  88 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  89 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  90 */     this.size = map.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  95 */     if (key == null) throw new IllegalArgumentException("key cannot be null."); 
/*  96 */     return put_internal(key, value);
/*     */   }
/*     */   
/*     */   private V put_internal(K key, V value) {
/* 100 */     K[] keyTable = this.keyTable;
/*     */ 
/*     */     
/* 103 */     int hashCode = key.hashCode();
/* 104 */     int index1 = hashCode & this.mask;
/* 105 */     K key1 = keyTable[index1];
/* 106 */     if (key.equals(key1)) {
/* 107 */       V oldValue = this.valueTable[index1];
/* 108 */       this.valueTable[index1] = value;
/* 109 */       return oldValue;
/*     */     } 
/*     */     
/* 112 */     int index2 = hash2(hashCode);
/* 113 */     K key2 = keyTable[index2];
/* 114 */     if (key.equals(key2)) {
/* 115 */       V oldValue = this.valueTable[index2];
/* 116 */       this.valueTable[index2] = value;
/* 117 */       return oldValue;
/*     */     } 
/*     */     
/* 120 */     int index3 = hash3(hashCode);
/* 121 */     K key3 = keyTable[index3];
/* 122 */     if (key.equals(key3)) {
/* 123 */       V oldValue = this.valueTable[index3];
/* 124 */       this.valueTable[index3] = value;
/* 125 */       return oldValue;
/*     */     } 
/*     */ 
/*     */     
/* 129 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 130 */       if (key.equals(keyTable[i])) {
/* 131 */         V oldValue = this.valueTable[i];
/* 132 */         this.valueTable[i] = value;
/* 133 */         return oldValue;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 138 */     if (key1 == null) {
/* 139 */       keyTable[index1] = key;
/* 140 */       this.valueTable[index1] = value;
/* 141 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 142 */       return null;
/*     */     } 
/*     */     
/* 145 */     if (key2 == null) {
/* 146 */       keyTable[index2] = key;
/* 147 */       this.valueTable[index2] = value;
/* 148 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 149 */       return null;
/*     */     } 
/*     */     
/* 152 */     if (key3 == null) {
/* 153 */       keyTable[index3] = key;
/* 154 */       this.valueTable[index3] = value;
/* 155 */       if (this.size++ >= this.threshold) resize(this.capacity << 1); 
/* 156 */       return null;
/*     */     } 
/*     */     
/* 159 */     push(key, value, index1, key1, index2, key2, index3, key3);
/* 160 */     return null;
/*     */   }
/*     */   
/*     */   public void putAll(ObjectMap<K, V> map) {
/* 164 */     ensureCapacity(map.size);
/* 165 */     for (Entries<K, V> entries = map.iterator(); entries.hasNext(); ) { Entry<K, V> entry = (Entry<K, V>)entries.next();
/* 166 */       put(entry.key, entry.value); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void putResize(K key, V value) {
/* 172 */     int hashCode = key.hashCode();
/* 173 */     int index1 = hashCode & this.mask;
/* 174 */     K key1 = this.keyTable[index1];
/* 175 */     if (key1 == null) {
/* 176 */       this.keyTable[index1] = key;
/* 177 */       this.valueTable[index1] = value;
/* 178 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 182 */     int index2 = hash2(hashCode);
/* 183 */     K key2 = this.keyTable[index2];
/* 184 */     if (key2 == null) {
/* 185 */       this.keyTable[index2] = key;
/* 186 */       this.valueTable[index2] = value;
/* 187 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 191 */     int index3 = hash3(hashCode);
/* 192 */     K key3 = this.keyTable[index3];
/* 193 */     if (key3 == null) {
/* 194 */       this.keyTable[index3] = key;
/* 195 */       this.valueTable[index3] = value;
/* 196 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 200 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   } private void push(K insertKey, V insertValue, int index1, K key1, int index2, K key2, int index3, K key3) {
/*     */     K evictedKey;
/*     */     V evictedValue;
/* 204 */     K[] keyTable = this.keyTable;
/* 205 */     V[] valueTable = this.valueTable;
/* 206 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 214 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 216 */           evictedKey = key1;
/* 217 */           evictedValue = valueTable[index1];
/* 218 */           keyTable[index1] = insertKey;
/* 219 */           valueTable[index1] = insertValue;
/*     */           break;
/*     */         case 1:
/* 222 */           evictedKey = key2;
/* 223 */           evictedValue = valueTable[index2];
/* 224 */           keyTable[index2] = insertKey;
/* 225 */           valueTable[index2] = insertValue;
/*     */           break;
/*     */         default:
/* 228 */           evictedKey = key3;
/* 229 */           evictedValue = valueTable[index3];
/* 230 */           keyTable[index3] = insertKey;
/* 231 */           valueTable[index3] = insertValue;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 236 */       int hashCode = evictedKey.hashCode();
/* 237 */       index1 = hashCode & mask;
/* 238 */       key1 = keyTable[index1];
/* 239 */       if (key1 == null) {
/* 240 */         keyTable[index1] = evictedKey;
/* 241 */         valueTable[index1] = evictedValue;
/* 242 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 246 */       index2 = hash2(hashCode);
/* 247 */       key2 = keyTable[index2];
/* 248 */       if (key2 == null) {
/* 249 */         keyTable[index2] = evictedKey;
/* 250 */         valueTable[index2] = evictedValue;
/* 251 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 255 */       index3 = hash3(hashCode);
/* 256 */       key3 = keyTable[index3];
/* 257 */       if (key3 == null) {
/* 258 */         keyTable[index3] = evictedKey;
/* 259 */         valueTable[index3] = evictedValue;
/* 260 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 264 */       if (++i == pushIterations)
/*     */         break; 
/* 266 */       insertKey = evictedKey;
/* 267 */       insertValue = evictedValue;
/*     */     } 
/*     */     
/* 270 */     putStash(evictedKey, evictedValue);
/*     */   }
/*     */   
/*     */   private void putStash(K key, V value) {
/* 274 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 276 */       resize(this.capacity << 1);
/* 277 */       put_internal(key, value);
/*     */       
/*     */       return;
/*     */     } 
/* 281 */     int index = this.capacity + this.stashSize;
/* 282 */     this.keyTable[index] = key;
/* 283 */     this.valueTable[index] = value;
/* 284 */     this.stashSize++;
/* 285 */     this.size++;
/*     */   }
/*     */   
/*     */   public V get(K key) {
/* 289 */     int hashCode = key.hashCode();
/* 290 */     int index = hashCode & this.mask;
/* 291 */     if (!key.equals(this.keyTable[index])) {
/* 292 */       index = hash2(hashCode);
/* 293 */       if (!key.equals(this.keyTable[index])) {
/* 294 */         index = hash3(hashCode);
/* 295 */         if (!key.equals(this.keyTable[index])) return getStash(key); 
/*     */       } 
/*     */     } 
/* 298 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private V getStash(K key) {
/* 302 */     K[] keyTable = this.keyTable;
/* 303 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 304 */       if (key.equals(keyTable[i])) return this.valueTable[i]; 
/* 305 */     }  return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(K key, V defaultValue) {
/* 310 */     int hashCode = key.hashCode();
/* 311 */     int index = hashCode & this.mask;
/* 312 */     if (!key.equals(this.keyTable[index])) {
/* 313 */       index = hash2(hashCode);
/* 314 */       if (!key.equals(this.keyTable[index])) {
/* 315 */         index = hash3(hashCode);
/* 316 */         if (!key.equals(this.keyTable[index])) return getStash(key, defaultValue); 
/*     */       } 
/*     */     } 
/* 319 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private V getStash(K key, V defaultValue) {
/* 323 */     K[] keyTable = this.keyTable;
/* 324 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 325 */       if (key.equals(keyTable[i])) return this.valueTable[i]; 
/* 326 */     }  return defaultValue;
/*     */   }
/*     */   
/*     */   public V remove(K key) {
/* 330 */     int hashCode = key.hashCode();
/* 331 */     int index = hashCode & this.mask;
/* 332 */     if (key.equals(this.keyTable[index])) {
/* 333 */       this.keyTable[index] = null;
/* 334 */       V oldValue = this.valueTable[index];
/* 335 */       this.valueTable[index] = null;
/* 336 */       this.size--;
/* 337 */       return oldValue;
/*     */     } 
/*     */     
/* 340 */     index = hash2(hashCode);
/* 341 */     if (key.equals(this.keyTable[index])) {
/* 342 */       this.keyTable[index] = null;
/* 343 */       V oldValue = this.valueTable[index];
/* 344 */       this.valueTable[index] = null;
/* 345 */       this.size--;
/* 346 */       return oldValue;
/*     */     } 
/*     */     
/* 349 */     index = hash3(hashCode);
/* 350 */     if (key.equals(this.keyTable[index])) {
/* 351 */       this.keyTable[index] = null;
/* 352 */       V oldValue = this.valueTable[index];
/* 353 */       this.valueTable[index] = null;
/* 354 */       this.size--;
/* 355 */       return oldValue;
/*     */     } 
/*     */     
/* 358 */     return removeStash(key);
/*     */   }
/*     */   
/*     */   V removeStash(K key) {
/* 362 */     K[] keyTable = this.keyTable;
/* 363 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 364 */       if (key.equals(keyTable[i])) {
/* 365 */         V oldValue = this.valueTable[i];
/* 366 */         removeStashIndex(i);
/* 367 */         this.size--;
/* 368 */         return oldValue;
/*     */       } 
/*     */     } 
/* 371 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 376 */     this.stashSize--;
/* 377 */     int lastIndex = this.capacity + this.stashSize;
/* 378 */     if (index < lastIndex) {
/* 379 */       this.keyTable[index] = this.keyTable[lastIndex];
/* 380 */       this.valueTable[index] = this.valueTable[lastIndex];
/* 381 */       this.valueTable[lastIndex] = null;
/*     */     } else {
/* 383 */       this.valueTable[index] = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 389 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 390 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 391 */     if (this.capacity <= maximumCapacity)
/* 392 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 393 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 398 */     if (this.capacity <= maximumCapacity) {
/* 399 */       clear();
/*     */       return;
/*     */     } 
/* 402 */     this.size = 0;
/* 403 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 407 */     if (this.size == 0)
/* 408 */       return;  K[] keyTable = this.keyTable;
/* 409 */     V[] valueTable = this.valueTable;
/* 410 */     for (int i = this.capacity + this.stashSize; i-- > 0; ) {
/* 411 */       keyTable[i] = null;
/* 412 */       valueTable[i] = null;
/*     */     } 
/* 414 */     this.size = 0;
/* 415 */     this.stashSize = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value, boolean identity) {
/* 423 */     V[] valueTable = this.valueTable;
/* 424 */     if (value == null)
/* 425 */     { K[] keyTable = this.keyTable;
/* 426 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 427 */       { if (keyTable[i] != null && valueTable[i] == null) return true;  }  }
/* 428 */     else if (identity)
/* 429 */     { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 430 */         if (valueTable[i] == value) return true; 
/*     */       }  }
/* 432 */     else { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 433 */         if (value.equals(valueTable[i])) return true; 
/*     */       }  }
/* 435 */      return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(K key) {
/* 439 */     int hashCode = key.hashCode();
/* 440 */     int index = hashCode & this.mask;
/* 441 */     if (!key.equals(this.keyTable[index])) {
/* 442 */       index = hash2(hashCode);
/* 443 */       if (!key.equals(this.keyTable[index])) {
/* 444 */         index = hash3(hashCode);
/* 445 */         if (!key.equals(this.keyTable[index])) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 448 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(K key) {
/* 452 */     K[] keyTable = this.keyTable;
/* 453 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 454 */       if (key.equals(keyTable[i])) return true; 
/* 455 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K findKey(Object value, boolean identity) {
/* 463 */     V[] valueTable = this.valueTable;
/* 464 */     if (value == null)
/* 465 */     { K[] keyTable = this.keyTable;
/* 466 */       for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 467 */       { if (keyTable[i] != null && valueTable[i] == null) return keyTable[i];  }  }
/* 468 */     else if (identity)
/* 469 */     { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 470 */         if (valueTable[i] == value) return this.keyTable[i]; 
/*     */       }  }
/* 472 */     else { for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 473 */         if (value.equals(valueTable[i])) return this.keyTable[i]; 
/*     */       }  }
/* 475 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 481 */     int sizeNeeded = this.size + additionalCapacity;
/* 482 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 486 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 488 */     this.capacity = newSize;
/* 489 */     this.threshold = (int)(newSize * this.loadFactor);
/* 490 */     this.mask = newSize - 1;
/* 491 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 492 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 493 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 495 */     K[] oldKeyTable = this.keyTable;
/* 496 */     V[] oldValueTable = this.valueTable;
/*     */     
/* 498 */     this.keyTable = (K[])new Object[newSize + this.stashCapacity];
/* 499 */     this.valueTable = (V[])new Object[newSize + this.stashCapacity];
/*     */     
/* 501 */     int oldSize = this.size;
/* 502 */     this.size = 0;
/* 503 */     this.stashSize = 0;
/* 504 */     if (oldSize > 0)
/* 505 */       for (int i = 0; i < oldEndIndex; i++) {
/* 506 */         K key = oldKeyTable[i];
/* 507 */         if (key != null) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 513 */     h *= -1262997959;
/* 514 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 518 */     h *= -825114047;
/* 519 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 523 */     int h = 0;
/* 524 */     K[] keyTable = this.keyTable;
/* 525 */     V[] valueTable = this.valueTable;
/* 526 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 527 */       K key = keyTable[i];
/* 528 */       if (key != null) {
/* 529 */         h += key.hashCode() * 31;
/*     */         
/* 531 */         V value = valueTable[i];
/* 532 */         if (value != null) {
/* 533 */           h += value.hashCode();
/*     */         }
/*     */       } 
/*     */     } 
/* 537 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 541 */     if (obj == this) return true; 
/* 542 */     if (!(obj instanceof ObjectMap)) return false; 
/* 543 */     ObjectMap<K, V> other = (ObjectMap<K, V>)obj;
/* 544 */     if (other.size != this.size) return false; 
/* 545 */     K[] keyTable = this.keyTable;
/* 546 */     V[] valueTable = this.valueTable;
/* 547 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 548 */       K key = keyTable[i];
/* 549 */       if (key != null) {
/* 550 */         V value = valueTable[i];
/* 551 */         if (value == null) {
/* 552 */           if (!other.containsKey(key) || other.get(key) != null) {
/* 553 */             return false;
/*     */           }
/*     */         }
/* 556 */         else if (!value.equals(other.get(key))) {
/* 557 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 562 */     return true;
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 566 */     return toString(separator, false);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 570 */     return toString(", ", true);
/*     */   }
/*     */   
/*     */   private String toString(String separator, boolean braces) {
/* 574 */     if (this.size == 0) return braces ? "{}" : ""; 
/* 575 */     StringBuilder buffer = new StringBuilder(32);
/* 576 */     if (braces) buffer.append('{'); 
/* 577 */     K[] keyTable = this.keyTable;
/* 578 */     V[] valueTable = this.valueTable;
/* 579 */     int i = keyTable.length;
/* 580 */     while (i-- > 0) {
/* 581 */       K key = keyTable[i];
/* 582 */       if (key == null)
/* 583 */         continue;  buffer.append(key);
/* 584 */       buffer.append('=');
/* 585 */       buffer.append(valueTable[i]);
/*     */     } 
/*     */     
/* 588 */     while (i-- > 0) {
/* 589 */       K key = keyTable[i];
/* 590 */       if (key == null)
/* 591 */         continue;  buffer.append(separator);
/* 592 */       buffer.append(key);
/* 593 */       buffer.append('=');
/* 594 */       buffer.append(valueTable[i]);
/*     */     } 
/* 596 */     if (braces) buffer.append('}'); 
/* 597 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Entries<K, V> iterator() {
/* 601 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries<K, V> entries() {
/* 607 */     if (this.entries1 == null) {
/* 608 */       this.entries1 = new Entries<K, V>(this);
/* 609 */       this.entries2 = new Entries<K, V>(this);
/*     */     } 
/* 611 */     if (!this.entries1.valid) {
/* 612 */       this.entries1.reset();
/* 613 */       this.entries1.valid = true;
/* 614 */       this.entries2.valid = false;
/* 615 */       return this.entries1;
/*     */     } 
/* 617 */     this.entries2.reset();
/* 618 */     this.entries2.valid = true;
/* 619 */     this.entries1.valid = false;
/* 620 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values<V> values() {
/* 626 */     if (this.values1 == null) {
/* 627 */       this.values1 = new Values<V>(this);
/* 628 */       this.values2 = new Values<V>(this);
/*     */     } 
/* 630 */     if (!this.values1.valid) {
/* 631 */       this.values1.reset();
/* 632 */       this.values1.valid = true;
/* 633 */       this.values2.valid = false;
/* 634 */       return this.values1;
/*     */     } 
/* 636 */     this.values2.reset();
/* 637 */     this.values2.valid = true;
/* 638 */     this.values1.valid = false;
/* 639 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys<K> keys() {
/* 645 */     if (this.keys1 == null) {
/* 646 */       this.keys1 = new Keys<K>(this);
/* 647 */       this.keys2 = new Keys<K>(this);
/*     */     } 
/* 649 */     if (!this.keys1.valid) {
/* 650 */       this.keys1.reset();
/* 651 */       this.keys1.valid = true;
/* 652 */       this.keys2.valid = false;
/* 653 */       return this.keys1;
/*     */     } 
/* 655 */     this.keys2.reset();
/* 656 */     this.keys2.valid = true;
/* 657 */     this.keys1.valid = false;
/* 658 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry<K, V> {
/*     */     public K key;
/*     */     public V value;
/*     */     
/*     */     public String toString() {
/* 666 */       return (new java.lang.StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class MapIterator<K, V, I> implements Iterable<I>, Iterator<I> {
/*     */     public boolean hasNext;
/*     */     final ObjectMap<K, V> map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(ObjectMap<K, V> map) {
/* 678 */       this.map = map;
/* 679 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 683 */       this.currentIndex = -1;
/* 684 */       this.nextIndex = -1;
/* 685 */       findNextIndex();
/*     */     }
/*     */     
/*     */     void findNextIndex() {
/* 689 */       this.hasNext = false;
/* 690 */       K[] keyTable = this.map.keyTable;
/* 691 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 692 */         if (keyTable[this.nextIndex] != null) {
/* 693 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 700 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 701 */       if (this.currentIndex >= this.map.capacity) {
/* 702 */         this.map.removeStashIndex(this.currentIndex);
/* 703 */         this.nextIndex = this.currentIndex - 1;
/* 704 */         findNextIndex();
/*     */       } else {
/* 706 */         this.map.keyTable[this.currentIndex] = null;
/* 707 */         this.map.valueTable[this.currentIndex] = null;
/*     */       } 
/* 709 */       this.currentIndex = -1;
/* 710 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries<K, V> extends MapIterator<K, V, Entry<K, V>> {
/* 715 */     ObjectMap.Entry<K, V> entry = new ObjectMap.Entry<K, V>();
/*     */     
/*     */     public Entries(ObjectMap<K, V> map) {
/* 718 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectMap.Entry<K, V> next() {
/* 723 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 724 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 725 */       K[] keyTable = this.map.keyTable;
/* 726 */       this.entry.key = keyTable[this.nextIndex];
/* 727 */       this.entry.value = this.map.valueTable[this.nextIndex];
/* 728 */       this.currentIndex = this.nextIndex;
/* 729 */       findNextIndex();
/* 730 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 734 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 735 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Entries<K, V> iterator() {
/* 739 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values<V> extends MapIterator<Object, V, V> {
/*     */     public Values(ObjectMap<?, V> map) {
/* 745 */       super((ObjectMap)map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 749 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 750 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public V next() {
/* 754 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 755 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 756 */       V value = this.map.valueTable[this.nextIndex];
/* 757 */       this.currentIndex = this.nextIndex;
/* 758 */       findNextIndex();
/* 759 */       return value;
/*     */     }
/*     */     
/*     */     public Values<V> iterator() {
/* 763 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<V> toArray() {
/* 768 */       return toArray(new Array<V>(true, this.map.size));
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<V> toArray(Array<V> array) {
/* 773 */       while (this.hasNext)
/* 774 */         array.add(next()); 
/* 775 */       return array;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys<K> extends MapIterator<K, Object, K> {
/*     */     public Keys(ObjectMap<K, ?> map) {
/* 781 */       super((ObjectMap)map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 785 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 786 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public K next() {
/* 790 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 791 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 792 */       K key = this.map.keyTable[this.nextIndex];
/* 793 */       this.currentIndex = this.nextIndex;
/* 794 */       findNextIndex();
/* 795 */       return key;
/*     */     }
/*     */     
/*     */     public Keys<K> iterator() {
/* 799 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray() {
/* 804 */       return toArray(new Array<K>(true, this.map.size));
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray(Array<K> array) {
/* 809 */       while (this.hasNext)
/* 810 */         array.add(next()); 
/* 811 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */