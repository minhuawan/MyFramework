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
/*     */ public class ObjectIntMap<K>
/*     */   implements Iterable<ObjectIntMap.Entry<K>>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   public int size;
/*     */   K[] keyTable;
/*     */   int[] valueTable;
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
/*     */   public ObjectIntMap() {
/*  53 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIntMap(int initialCapacity) {
/*  59 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIntMap(int initialCapacity, float loadFactor) {
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
/*  81 */     this.valueTable = new int[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectIntMap(ObjectIntMap<? extends K> map) {
/*  86 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  87 */     this.stashSize = map.stashSize;
/*  88 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  89 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  90 */     this.size = map.size;
/*     */   }
/*     */   
/*     */   public void put(K key, int value) {
/*  94 */     if (key == null) throw new IllegalArgumentException("key cannot be null."); 
/*  95 */     K[] keyTable = this.keyTable;
/*     */ 
/*     */     
/*  98 */     int hashCode = key.hashCode();
/*  99 */     int index1 = hashCode & this.mask;
/* 100 */     K key1 = keyTable[index1];
/* 101 */     if (key.equals(key1)) {
/* 102 */       this.valueTable[index1] = value;
/*     */       
/*     */       return;
/*     */     } 
/* 106 */     int index2 = hash2(hashCode);
/* 107 */     K key2 = keyTable[index2];
/* 108 */     if (key.equals(key2)) {
/* 109 */       this.valueTable[index2] = value;
/*     */       
/*     */       return;
/*     */     } 
/* 113 */     int index3 = hash3(hashCode);
/* 114 */     K key3 = keyTable[index3];
/* 115 */     if (key.equals(key3)) {
/* 116 */       this.valueTable[index3] = value;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 121 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 122 */       if (key.equals(keyTable[i])) {
/* 123 */         this.valueTable[i] = value;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     if (key1 == null) {
/* 130 */       keyTable[index1] = key;
/* 131 */       this.valueTable[index1] = value;
/* 132 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 136 */     if (key2 == null) {
/* 137 */       keyTable[index2] = key;
/* 138 */       this.valueTable[index2] = value;
/* 139 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 143 */     if (key3 == null) {
/* 144 */       keyTable[index3] = key;
/* 145 */       this.valueTable[index3] = value;
/* 146 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 150 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   }
/*     */   
/*     */   public void putAll(ObjectIntMap<K> map) {
/* 154 */     for (Entries<Entry<K>> entries = map.entries().iterator(); entries.hasNext(); ) { Entry<K> entry = entries.next();
/* 155 */       put(entry.key, entry.value); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void putResize(K key, int value) {
/* 161 */     int hashCode = key.hashCode();
/* 162 */     int index1 = hashCode & this.mask;
/* 163 */     K key1 = this.keyTable[index1];
/* 164 */     if (key1 == null) {
/* 165 */       this.keyTable[index1] = key;
/* 166 */       this.valueTable[index1] = value;
/* 167 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 171 */     int index2 = hash2(hashCode);
/* 172 */     K key2 = this.keyTable[index2];
/* 173 */     if (key2 == null) {
/* 174 */       this.keyTable[index2] = key;
/* 175 */       this.valueTable[index2] = value;
/* 176 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 180 */     int index3 = hash3(hashCode);
/* 181 */     K key3 = this.keyTable[index3];
/* 182 */     if (key3 == null) {
/* 183 */       this.keyTable[index3] = key;
/* 184 */       this.valueTable[index3] = value;
/* 185 */       if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */       
/*     */       return;
/*     */     } 
/* 189 */     push(key, value, index1, key1, index2, key2, index3, key3);
/*     */   } private void push(K insertKey, int insertValue, int index1, K key1, int index2, K key2, int index3, K key3) {
/*     */     K evictedKey;
/*     */     int evictedValue;
/* 193 */     K[] keyTable = this.keyTable;
/* 194 */     int[] valueTable = this.valueTable;
/* 195 */     int mask = this.mask;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     int i = 0, pushIterations = this.pushIterations;
/*     */     
/*     */     while (true) {
/* 203 */       switch (MathUtils.random(2)) {
/*     */         case 0:
/* 205 */           evictedKey = key1;
/* 206 */           evictedValue = valueTable[index1];
/* 207 */           keyTable[index1] = insertKey;
/* 208 */           valueTable[index1] = insertValue;
/*     */           break;
/*     */         case 1:
/* 211 */           evictedKey = key2;
/* 212 */           evictedValue = valueTable[index2];
/* 213 */           keyTable[index2] = insertKey;
/* 214 */           valueTable[index2] = insertValue;
/*     */           break;
/*     */         default:
/* 217 */           evictedKey = key3;
/* 218 */           evictedValue = valueTable[index3];
/* 219 */           keyTable[index3] = insertKey;
/* 220 */           valueTable[index3] = insertValue;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 225 */       int hashCode = evictedKey.hashCode();
/* 226 */       index1 = hashCode & mask;
/* 227 */       key1 = keyTable[index1];
/* 228 */       if (key1 == null) {
/* 229 */         keyTable[index1] = evictedKey;
/* 230 */         valueTable[index1] = evictedValue;
/* 231 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 235 */       index2 = hash2(hashCode);
/* 236 */       key2 = keyTable[index2];
/* 237 */       if (key2 == null) {
/* 238 */         keyTable[index2] = evictedKey;
/* 239 */         valueTable[index2] = evictedValue;
/* 240 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 244 */       index3 = hash3(hashCode);
/* 245 */       key3 = keyTable[index3];
/* 246 */       if (key3 == null) {
/* 247 */         keyTable[index3] = evictedKey;
/* 248 */         valueTable[index3] = evictedValue;
/* 249 */         if (this.size++ >= this.threshold) resize(this.capacity << 1);
/*     */         
/*     */         return;
/*     */       } 
/* 253 */       if (++i == pushIterations)
/*     */         break; 
/* 255 */       insertKey = evictedKey;
/* 256 */       insertValue = evictedValue;
/*     */     } 
/*     */     
/* 259 */     putStash(evictedKey, evictedValue);
/*     */   }
/*     */   
/*     */   private void putStash(K key, int value) {
/* 263 */     if (this.stashSize == this.stashCapacity) {
/*     */       
/* 265 */       resize(this.capacity << 1);
/* 266 */       put(key, value);
/*     */       
/*     */       return;
/*     */     } 
/* 270 */     int index = this.capacity + this.stashSize;
/* 271 */     this.keyTable[index] = key;
/* 272 */     this.valueTable[index] = value;
/* 273 */     this.stashSize++;
/* 274 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int get(K key, int defaultValue) {
/* 279 */     int hashCode = key.hashCode();
/* 280 */     int index = hashCode & this.mask;
/* 281 */     if (!key.equals(this.keyTable[index])) {
/* 282 */       index = hash2(hashCode);
/* 283 */       if (!key.equals(this.keyTable[index])) {
/* 284 */         index = hash3(hashCode);
/* 285 */         if (!key.equals(this.keyTable[index])) return getStash(key, defaultValue); 
/*     */       } 
/*     */     } 
/* 288 */     return this.valueTable[index];
/*     */   }
/*     */   
/*     */   private int getStash(K key, int defaultValue) {
/* 292 */     K[] keyTable = this.keyTable;
/* 293 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 294 */       if (key.equals(keyTable[i])) return this.valueTable[i]; 
/* 295 */     }  return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAndIncrement(K key, int defaultValue, int increment) {
/* 301 */     int hashCode = key.hashCode();
/* 302 */     int index = hashCode & this.mask;
/* 303 */     if (!key.equals(this.keyTable[index])) {
/* 304 */       index = hash2(hashCode);
/* 305 */       if (!key.equals(this.keyTable[index])) {
/* 306 */         index = hash3(hashCode);
/* 307 */         if (!key.equals(this.keyTable[index])) return getAndIncrementStash(key, defaultValue, increment); 
/*     */       } 
/*     */     } 
/* 310 */     int value = this.valueTable[index];
/* 311 */     this.valueTable[index] = value + increment;
/* 312 */     return value;
/*     */   }
/*     */   
/*     */   private int getAndIncrementStash(K key, int defaultValue, int increment) {
/* 316 */     K[] keyTable = this.keyTable;
/* 317 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 318 */       if (key.equals(keyTable[i])) {
/* 319 */         int value = this.valueTable[i];
/* 320 */         this.valueTable[i] = value + increment;
/* 321 */         return value;
/*     */       } 
/* 323 */     }  put(key, defaultValue + increment);
/* 324 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public int remove(K key, int defaultValue) {
/* 328 */     int hashCode = key.hashCode();
/* 329 */     int index = hashCode & this.mask;
/* 330 */     if (key.equals(this.keyTable[index])) {
/* 331 */       this.keyTable[index] = null;
/* 332 */       int oldValue = this.valueTable[index];
/* 333 */       this.size--;
/* 334 */       return oldValue;
/*     */     } 
/*     */     
/* 337 */     index = hash2(hashCode);
/* 338 */     if (key.equals(this.keyTable[index])) {
/* 339 */       this.keyTable[index] = null;
/* 340 */       int oldValue = this.valueTable[index];
/* 341 */       this.size--;
/* 342 */       return oldValue;
/*     */     } 
/*     */     
/* 345 */     index = hash3(hashCode);
/* 346 */     if (key.equals(this.keyTable[index])) {
/* 347 */       this.keyTable[index] = null;
/* 348 */       int oldValue = this.valueTable[index];
/* 349 */       this.size--;
/* 350 */       return oldValue;
/*     */     } 
/*     */     
/* 353 */     return removeStash(key, defaultValue);
/*     */   }
/*     */   
/*     */   int removeStash(K key, int defaultValue) {
/* 357 */     K[] keyTable = this.keyTable;
/* 358 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 359 */       if (key.equals(keyTable[i])) {
/* 360 */         int oldValue = this.valueTable[i];
/* 361 */         removeStashIndex(i);
/* 362 */         this.size--;
/* 363 */         return oldValue;
/*     */       } 
/*     */     } 
/* 366 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   void removeStashIndex(int index) {
/* 371 */     this.stashSize--;
/* 372 */     int lastIndex = this.capacity + this.stashSize;
/* 373 */     if (index < lastIndex) {
/* 374 */       this.keyTable[index] = this.keyTable[lastIndex];
/* 375 */       this.valueTable[index] = this.valueTable[lastIndex];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shrink(int maximumCapacity) {
/* 382 */     if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity); 
/* 383 */     if (this.size > maximumCapacity) maximumCapacity = this.size; 
/* 384 */     if (this.capacity <= maximumCapacity)
/* 385 */       return;  maximumCapacity = MathUtils.nextPowerOfTwo(maximumCapacity);
/* 386 */     resize(maximumCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 391 */     if (this.capacity <= maximumCapacity) {
/* 392 */       clear();
/*     */       return;
/*     */     } 
/* 395 */     this.size = 0;
/* 396 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 400 */     if (this.size == 0)
/* 401 */       return;  K[] keyTable = this.keyTable;
/* 402 */     for (int i = this.capacity + this.stashSize; i-- > 0;)
/* 403 */       keyTable[i] = null; 
/* 404 */     this.size = 0;
/* 405 */     this.stashSize = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(int value) {
/* 411 */     K[] keyTable = this.keyTable;
/* 412 */     int[] valueTable = this.valueTable;
/* 413 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 414 */       if (keyTable[i] != null && valueTable[i] == value) return true; 
/* 415 */     }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(K key) {
/* 420 */     int hashCode = key.hashCode();
/* 421 */     int index = hashCode & this.mask;
/* 422 */     if (!key.equals(this.keyTable[index])) {
/* 423 */       index = hash2(hashCode);
/* 424 */       if (!key.equals(this.keyTable[index])) {
/* 425 */         index = hash3(hashCode);
/* 426 */         if (!key.equals(this.keyTable[index])) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 429 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(K key) {
/* 433 */     K[] keyTable = this.keyTable;
/* 434 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 435 */       if (key.equals(keyTable[i])) return true; 
/* 436 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K findKey(int value) {
/* 442 */     K[] keyTable = this.keyTable;
/* 443 */     int[] valueTable = this.valueTable;
/* 444 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 445 */       if (keyTable[i] != null && valueTable[i] == value) return keyTable[i]; 
/* 446 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 452 */     int sizeNeeded = this.size + additionalCapacity;
/* 453 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 457 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 459 */     this.capacity = newSize;
/* 460 */     this.threshold = (int)(newSize * this.loadFactor);
/* 461 */     this.mask = newSize - 1;
/* 462 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 463 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 464 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 466 */     K[] oldKeyTable = this.keyTable;
/* 467 */     int[] oldValueTable = this.valueTable;
/*     */     
/* 469 */     this.keyTable = (K[])new Object[newSize + this.stashCapacity];
/* 470 */     this.valueTable = new int[newSize + this.stashCapacity];
/*     */     
/* 472 */     int oldSize = this.size;
/* 473 */     this.size = 0;
/* 474 */     this.stashSize = 0;
/* 475 */     if (oldSize > 0)
/* 476 */       for (int i = 0; i < oldEndIndex; i++) {
/* 477 */         K key = oldKeyTable[i];
/* 478 */         if (key != null) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 484 */     h *= -1262997959;
/* 485 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 489 */     h *= -825114047;
/* 490 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 494 */     int h = 0;
/* 495 */     K[] keyTable = this.keyTable;
/* 496 */     int[] valueTable = this.valueTable;
/* 497 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 498 */       K key = keyTable[i];
/* 499 */       if (key != null) {
/* 500 */         h += key.hashCode() * 31;
/*     */         
/* 502 */         int value = valueTable[i];
/* 503 */         h += value;
/*     */       } 
/*     */     } 
/* 506 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 510 */     if (obj == this) return true; 
/* 511 */     if (!(obj instanceof ObjectIntMap)) return false; 
/* 512 */     ObjectIntMap<K> other = (ObjectIntMap<K>)obj;
/* 513 */     if (other.size != this.size) return false; 
/* 514 */     K[] keyTable = this.keyTable;
/* 515 */     int[] valueTable = this.valueTable;
/* 516 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 517 */       K key = keyTable[i];
/* 518 */       if (key != null) {
/* 519 */         int otherValue = other.get(key, 0);
/* 520 */         if (otherValue == 0 && !other.containsKey(key)) return false; 
/* 521 */         int value = valueTable[i];
/* 522 */         if (otherValue != value) return false; 
/*     */       } 
/*     */     } 
/* 525 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 529 */     if (this.size == 0) return "{}"; 
/* 530 */     StringBuilder buffer = new StringBuilder(32);
/* 531 */     buffer.append('{');
/* 532 */     K[] keyTable = this.keyTable;
/* 533 */     int[] valueTable = this.valueTable;
/* 534 */     int i = keyTable.length;
/* 535 */     while (i-- > 0) {
/* 536 */       K key = keyTable[i];
/* 537 */       if (key == null)
/* 538 */         continue;  buffer.append(key);
/* 539 */       buffer.append('=');
/* 540 */       buffer.append(valueTable[i]);
/*     */     } 
/*     */     
/* 543 */     while (i-- > 0) {
/* 544 */       K key = keyTable[i];
/* 545 */       if (key == null)
/* 546 */         continue;  buffer.append(", ");
/* 547 */       buffer.append(key);
/* 548 */       buffer.append('=');
/* 549 */       buffer.append(valueTable[i]);
/*     */     } 
/* 551 */     buffer.append('}');
/* 552 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Entries<K> iterator() {
/* 556 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries<K> entries() {
/* 562 */     if (this.entries1 == null) {
/* 563 */       this.entries1 = new Entries<K>(this);
/* 564 */       this.entries2 = new Entries<K>(this);
/*     */     } 
/* 566 */     if (!this.entries1.valid) {
/* 567 */       this.entries1.reset();
/* 568 */       this.entries1.valid = true;
/* 569 */       this.entries2.valid = false;
/* 570 */       return this.entries1;
/*     */     } 
/* 572 */     this.entries2.reset();
/* 573 */     this.entries2.valid = true;
/* 574 */     this.entries1.valid = false;
/* 575 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values values() {
/* 581 */     if (this.values1 == null) {
/* 582 */       this.values1 = new Values(this);
/* 583 */       this.values2 = new Values(this);
/*     */     } 
/* 585 */     if (!this.values1.valid) {
/* 586 */       this.values1.reset();
/* 587 */       this.values1.valid = true;
/* 588 */       this.values2.valid = false;
/* 589 */       return this.values1;
/*     */     } 
/* 591 */     this.values2.reset();
/* 592 */     this.values2.valid = true;
/* 593 */     this.values1.valid = false;
/* 594 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys<K> keys() {
/* 600 */     if (this.keys1 == null) {
/* 601 */       this.keys1 = new Keys<K>(this);
/* 602 */       this.keys2 = new Keys<K>(this);
/*     */     } 
/* 604 */     if (!this.keys1.valid) {
/* 605 */       this.keys1.reset();
/* 606 */       this.keys1.valid = true;
/* 607 */       this.keys2.valid = false;
/* 608 */       return this.keys1;
/*     */     } 
/* 610 */     this.keys2.reset();
/* 611 */     this.keys2.valid = true;
/* 612 */     this.keys1.valid = false;
/* 613 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry<K> {
/*     */     public K key;
/*     */     public int value;
/*     */     
/*     */     public String toString() {
/* 621 */       return (new java.lang.StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MapIterator<K> {
/*     */     public boolean hasNext;
/*     */     final ObjectIntMap<K> map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(ObjectIntMap<K> map) {
/* 633 */       this.map = map;
/* 634 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 638 */       this.currentIndex = -1;
/* 639 */       this.nextIndex = -1;
/* 640 */       findNextIndex();
/*     */     }
/*     */     
/*     */     void findNextIndex() {
/* 644 */       this.hasNext = false;
/* 645 */       K[] keyTable = this.map.keyTable;
/* 646 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 647 */         if (keyTable[this.nextIndex] != null) {
/* 648 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 655 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 656 */       if (this.currentIndex >= this.map.capacity) {
/* 657 */         this.map.removeStashIndex(this.currentIndex);
/* 658 */         this.nextIndex = this.currentIndex - 1;
/* 659 */         findNextIndex();
/*     */       } else {
/* 661 */         this.map.keyTable[this.currentIndex] = null;
/*     */       } 
/* 663 */       this.currentIndex = -1;
/* 664 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries<K> extends MapIterator<K> implements Iterable<Entry<K>>, Iterator<Entry<K>> {
/* 669 */     private ObjectIntMap.Entry<K> entry = new ObjectIntMap.Entry<K>();
/*     */     
/*     */     public Entries(ObjectIntMap<K> map) {
/* 672 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectIntMap.Entry<K> next() {
/* 677 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 678 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 679 */       K[] keyTable = this.map.keyTable;
/* 680 */       this.entry.key = keyTable[this.nextIndex];
/* 681 */       this.entry.value = this.map.valueTable[this.nextIndex];
/* 682 */       this.currentIndex = this.nextIndex;
/* 683 */       findNextIndex();
/* 684 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 688 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 689 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Entries<K> iterator() {
/* 693 */       return this;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 697 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values extends MapIterator<Object> {
/*     */     public Values(ObjectIntMap<?> map) {
/* 703 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 707 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 708 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public int next() {
/* 712 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 713 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 714 */       int value = this.map.valueTable[this.nextIndex];
/* 715 */       this.currentIndex = this.nextIndex;
/* 716 */       findNextIndex();
/* 717 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntArray toArray() {
/* 722 */       IntArray array = new IntArray(true, this.map.size);
/* 723 */       while (this.hasNext)
/* 724 */         array.add(next()); 
/* 725 */       return array;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys<K> extends MapIterator<K> implements Iterable<K>, Iterator<K> {
/*     */     public Keys(ObjectIntMap<K> map) {
/* 731 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 735 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 736 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public K next() {
/* 740 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 741 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 742 */       K key = this.map.keyTable[this.nextIndex];
/* 743 */       this.currentIndex = this.nextIndex;
/* 744 */       findNextIndex();
/* 745 */       return key;
/*     */     }
/*     */     
/*     */     public Keys<K> iterator() {
/* 749 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray() {
/* 754 */       Array<K> array = new Array(true, this.map.size);
/* 755 */       while (this.hasNext)
/* 756 */         array.add(next()); 
/* 757 */       return array;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray(Array<K> array) {
/* 762 */       while (this.hasNext)
/* 763 */         array.add(next()); 
/* 764 */       return array;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 768 */       super.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ObjectIntMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */