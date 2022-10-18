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
/*     */ public class ObjectFloatMap<K>
/*     */   implements Iterable<ObjectFloatMap.Entry<K>>
/*     */ {
/*     */   private static final int PRIME1 = -1105259343;
/*     */   private static final int PRIME2 = -1262997959;
/*     */   private static final int PRIME3 = -825114047;
/*     */   public int size;
/*     */   K[] keyTable;
/*     */   float[] valueTable;
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
/*     */   public ObjectFloatMap() {
/*  53 */     this(51, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectFloatMap(int initialCapacity) {
/*  59 */     this(initialCapacity, 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectFloatMap(int initialCapacity, float loadFactor) {
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
/*  81 */     this.valueTable = new float[this.keyTable.length];
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectFloatMap(ObjectFloatMap<? extends K> map) {
/*  86 */     this((int)Math.floor((map.capacity * map.loadFactor)), map.loadFactor);
/*  87 */     this.stashSize = map.stashSize;
/*  88 */     System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
/*  89 */     System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
/*  90 */     this.size = map.size;
/*     */   }
/*     */   
/*     */   public void put(K key, float value) {
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
/*     */   public void putAll(ObjectFloatMap<K> map) {
/* 154 */     for (Entries<Entry<K>> entries = map.entries().iterator(); entries.hasNext(); ) { Entry<K> entry = entries.next();
/* 155 */       put(entry.key, entry.value); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void putResize(K key, float value) {
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
/*     */   } private void push(K insertKey, float insertValue, int index1, K key1, int index2, K key2, int index3, K key3) {
/*     */     K evictedKey;
/*     */     float evictedValue;
/* 193 */     K[] keyTable = this.keyTable;
/* 194 */     float[] valueTable = this.valueTable;
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
/*     */   private void putStash(K key, float value) {
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
/*     */   public float get(K key, float defaultValue) {
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
/*     */   private float getStash(K key, float defaultValue) {
/* 292 */     K[] keyTable = this.keyTable;
/* 293 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 294 */       if (key.equals(keyTable[i])) return this.valueTable[i]; 
/* 295 */     }  return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAndIncrement(K key, float defaultValue, float increment) {
/* 301 */     int hashCode = key.hashCode();
/* 302 */     int index = hashCode & this.mask;
/* 303 */     if (!key.equals(this.keyTable[index])) {
/* 304 */       index = hash2(hashCode);
/* 305 */       if (!key.equals(this.keyTable[index])) {
/* 306 */         index = hash3(hashCode);
/* 307 */         if (!key.equals(this.keyTable[index])) return getAndIncrementStash(key, defaultValue, increment); 
/*     */       } 
/*     */     } 
/* 310 */     float value = this.valueTable[index];
/* 311 */     this.valueTable[index] = value + increment;
/* 312 */     return value;
/*     */   }
/*     */   
/*     */   private float getAndIncrementStash(K key, float defaultValue, float increment) {
/* 316 */     K[] keyTable = this.keyTable;
/* 317 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 318 */       if (key.equals(keyTable[i])) {
/* 319 */         float value = this.valueTable[i];
/* 320 */         this.valueTable[i] = value + increment;
/* 321 */         return value;
/*     */       } 
/* 323 */     }  put(key, defaultValue + increment);
/* 324 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public float remove(K key, float defaultValue) {
/* 328 */     int hashCode = key.hashCode();
/* 329 */     int index = hashCode & this.mask;
/* 330 */     if (key.equals(this.keyTable[index])) {
/* 331 */       this.keyTable[index] = null;
/* 332 */       float oldValue = this.valueTable[index];
/* 333 */       this.size--;
/* 334 */       return oldValue;
/*     */     } 
/*     */     
/* 337 */     index = hash2(hashCode);
/* 338 */     if (key.equals(this.keyTable[index])) {
/* 339 */       this.keyTable[index] = null;
/* 340 */       float oldValue = this.valueTable[index];
/* 341 */       this.size--;
/* 342 */       return oldValue;
/*     */     } 
/*     */     
/* 345 */     index = hash3(hashCode);
/* 346 */     if (key.equals(this.keyTable[index])) {
/* 347 */       this.keyTable[index] = null;
/* 348 */       float oldValue = this.valueTable[index];
/* 349 */       this.size--;
/* 350 */       return oldValue;
/*     */     } 
/*     */     
/* 353 */     return removeStash(key, defaultValue);
/*     */   }
/*     */   
/*     */   float removeStash(K key, float defaultValue) {
/* 357 */     K[] keyTable = this.keyTable;
/* 358 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 359 */       if (key.equals(keyTable[i])) {
/* 360 */         float oldValue = this.valueTable[i];
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
/*     */   public boolean containsValue(float value) {
/* 411 */     K[] keyTable = this.keyTable;
/* 412 */     float[] valueTable = this.valueTable;
/* 413 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 414 */       if (keyTable[i] != null && valueTable[i] == value) return true; 
/* 415 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean containsKey(K key) {
/* 419 */     int hashCode = key.hashCode();
/* 420 */     int index = hashCode & this.mask;
/* 421 */     if (!key.equals(this.keyTable[index])) {
/* 422 */       index = hash2(hashCode);
/* 423 */       if (!key.equals(this.keyTable[index])) {
/* 424 */         index = hash3(hashCode);
/* 425 */         if (!key.equals(this.keyTable[index])) return containsKeyStash(key); 
/*     */       } 
/*     */     } 
/* 428 */     return true;
/*     */   }
/*     */   
/*     */   private boolean containsKeyStash(K key) {
/* 432 */     K[] keyTable = this.keyTable;
/* 433 */     for (int i = this.capacity, n = i + this.stashSize; i < n; i++) {
/* 434 */       if (key.equals(keyTable[i])) return true; 
/* 435 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K findKey(float value) {
/* 441 */     K[] keyTable = this.keyTable;
/* 442 */     float[] valueTable = this.valueTable;
/* 443 */     for (int i = this.capacity + this.stashSize; i-- > 0;) {
/* 444 */       if (keyTable[i] != null && valueTable[i] == value) return keyTable[i]; 
/* 445 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 451 */     int sizeNeeded = this.size + additionalCapacity;
/* 452 */     if (sizeNeeded >= this.threshold) resize(MathUtils.nextPowerOfTwo((int)Math.ceil((sizeNeeded / this.loadFactor)))); 
/*     */   }
/*     */   
/*     */   private void resize(int newSize) {
/* 456 */     int oldEndIndex = this.capacity + this.stashSize;
/*     */     
/* 458 */     this.capacity = newSize;
/* 459 */     this.threshold = (int)(newSize * this.loadFactor);
/* 460 */     this.mask = newSize - 1;
/* 461 */     this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
/* 462 */     this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) * 2);
/* 463 */     this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
/*     */     
/* 465 */     K[] oldKeyTable = this.keyTable;
/* 466 */     float[] oldValueTable = this.valueTable;
/*     */     
/* 468 */     this.keyTable = (K[])new Object[newSize + this.stashCapacity];
/* 469 */     this.valueTable = new float[newSize + this.stashCapacity];
/*     */     
/* 471 */     int oldSize = this.size;
/* 472 */     this.size = 0;
/* 473 */     this.stashSize = 0;
/* 474 */     if (oldSize > 0)
/* 475 */       for (int i = 0; i < oldEndIndex; i++) {
/* 476 */         K key = oldKeyTable[i];
/* 477 */         if (key != null) putResize(key, oldValueTable[i]);
/*     */       
/*     */       }  
/*     */   }
/*     */   
/*     */   private int hash2(int h) {
/* 483 */     h *= -1262997959;
/* 484 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   private int hash3(int h) {
/* 488 */     h *= -825114047;
/* 489 */     return (h ^ h >>> this.hashShift) & this.mask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 493 */     int h = 0;
/* 494 */     K[] keyTable = this.keyTable;
/* 495 */     float[] valueTable = this.valueTable;
/* 496 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 497 */       K key = keyTable[i];
/* 498 */       if (key != null) {
/* 499 */         h += key.hashCode() * 31;
/*     */         
/* 501 */         float value = valueTable[i];
/* 502 */         h += Float.floatToIntBits(value);
/*     */       } 
/*     */     } 
/* 505 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 509 */     if (obj == this) return true; 
/* 510 */     if (!(obj instanceof ObjectFloatMap)) return false; 
/* 511 */     ObjectFloatMap<K> other = (ObjectFloatMap<K>)obj;
/* 512 */     if (other.size != this.size) return false; 
/* 513 */     K[] keyTable = this.keyTable;
/* 514 */     float[] valueTable = this.valueTable;
/* 515 */     for (int i = 0, n = this.capacity + this.stashSize; i < n; i++) {
/* 516 */       K key = keyTable[i];
/* 517 */       if (key != null) {
/* 518 */         float otherValue = other.get(key, 0.0F);
/* 519 */         if (otherValue == 0.0F && !other.containsKey(key)) return false; 
/* 520 */         float value = valueTable[i];
/* 521 */         if (otherValue != value) return false; 
/*     */       } 
/*     */     } 
/* 524 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 528 */     if (this.size == 0) return "{}"; 
/* 529 */     StringBuilder buffer = new StringBuilder(32);
/* 530 */     buffer.append('{');
/* 531 */     K[] keyTable = this.keyTable;
/* 532 */     float[] valueTable = this.valueTable;
/* 533 */     int i = keyTable.length;
/* 534 */     while (i-- > 0) {
/* 535 */       K key = keyTable[i];
/* 536 */       if (key == null)
/* 537 */         continue;  buffer.append(key);
/* 538 */       buffer.append('=');
/* 539 */       buffer.append(valueTable[i]);
/*     */     } 
/*     */     
/* 542 */     while (i-- > 0) {
/* 543 */       K key = keyTable[i];
/* 544 */       if (key == null)
/* 545 */         continue;  buffer.append(", ");
/* 546 */       buffer.append(key);
/* 547 */       buffer.append('=');
/* 548 */       buffer.append(valueTable[i]);
/*     */     } 
/* 550 */     buffer.append('}');
/* 551 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Entries<K> iterator() {
/* 555 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries<K> entries() {
/* 561 */     if (this.entries1 == null) {
/* 562 */       this.entries1 = new Entries<K>(this);
/* 563 */       this.entries2 = new Entries<K>(this);
/*     */     } 
/* 565 */     if (!this.entries1.valid) {
/* 566 */       this.entries1.reset();
/* 567 */       this.entries1.valid = true;
/* 568 */       this.entries2.valid = false;
/* 569 */       return this.entries1;
/*     */     } 
/* 571 */     this.entries2.reset();
/* 572 */     this.entries2.valid = true;
/* 573 */     this.entries1.valid = false;
/* 574 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values values() {
/* 580 */     if (this.values1 == null) {
/* 581 */       this.values1 = new Values(this);
/* 582 */       this.values2 = new Values(this);
/*     */     } 
/* 584 */     if (!this.values1.valid) {
/* 585 */       this.values1.reset();
/* 586 */       this.values1.valid = true;
/* 587 */       this.values2.valid = false;
/* 588 */       return this.values1;
/*     */     } 
/* 590 */     this.values2.reset();
/* 591 */     this.values2.valid = true;
/* 592 */     this.values1.valid = false;
/* 593 */     return this.values2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys<K> keys() {
/* 599 */     if (this.keys1 == null) {
/* 600 */       this.keys1 = new Keys<K>(this);
/* 601 */       this.keys2 = new Keys<K>(this);
/*     */     } 
/* 603 */     if (!this.keys1.valid) {
/* 604 */       this.keys1.reset();
/* 605 */       this.keys1.valid = true;
/* 606 */       this.keys2.valid = false;
/* 607 */       return this.keys1;
/*     */     } 
/* 609 */     this.keys2.reset();
/* 610 */     this.keys2.valid = true;
/* 611 */     this.keys1.valid = false;
/* 612 */     return this.keys2;
/*     */   }
/*     */   
/*     */   public static class Entry<K> {
/*     */     public K key;
/*     */     public float value;
/*     */     
/*     */     public String toString() {
/* 620 */       return (new java.lang.StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MapIterator<K> {
/*     */     public boolean hasNext;
/*     */     final ObjectFloatMap<K> map;
/*     */     int nextIndex;
/*     */     int currentIndex;
/*     */     boolean valid = true;
/*     */     
/*     */     public MapIterator(ObjectFloatMap<K> map) {
/* 632 */       this.map = map;
/* 633 */       reset();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 637 */       this.currentIndex = -1;
/* 638 */       this.nextIndex = -1;
/* 639 */       findNextIndex();
/*     */     }
/*     */     
/*     */     void findNextIndex() {
/* 643 */       this.hasNext = false;
/* 644 */       K[] keyTable = this.map.keyTable;
/* 645 */       for (int n = this.map.capacity + this.map.stashSize; ++this.nextIndex < n;) {
/* 646 */         if (keyTable[this.nextIndex] != null) {
/* 647 */           this.hasNext = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 654 */       if (this.currentIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 655 */       if (this.currentIndex >= this.map.capacity) {
/* 656 */         this.map.removeStashIndex(this.currentIndex);
/* 657 */         this.nextIndex = this.currentIndex - 1;
/* 658 */         findNextIndex();
/*     */       } else {
/* 660 */         this.map.keyTable[this.currentIndex] = null;
/*     */       } 
/* 662 */       this.currentIndex = -1;
/* 663 */       this.map.size--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Entries<K> extends MapIterator<K> implements Iterable<Entry<K>>, Iterator<Entry<K>> {
/* 668 */     private ObjectFloatMap.Entry<K> entry = new ObjectFloatMap.Entry<K>();
/*     */     
/*     */     public Entries(ObjectFloatMap<K> map) {
/* 671 */       super(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectFloatMap.Entry<K> next() {
/* 676 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 677 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 678 */       K[] keyTable = this.map.keyTable;
/* 679 */       this.entry.key = keyTable[this.nextIndex];
/* 680 */       this.entry.value = this.map.valueTable[this.nextIndex];
/* 681 */       this.currentIndex = this.nextIndex;
/* 682 */       findNextIndex();
/* 683 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 687 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 688 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public Entries<K> iterator() {
/* 692 */       return this;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 696 */       super.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values extends MapIterator<Object> {
/*     */     public Values(ObjectFloatMap<?> map) {
/* 702 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 706 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 707 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public float next() {
/* 711 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 712 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 713 */       float value = this.map.valueTable[this.nextIndex];
/* 714 */       this.currentIndex = this.nextIndex;
/* 715 */       findNextIndex();
/* 716 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public FloatArray toArray() {
/* 721 */       FloatArray array = new FloatArray(true, this.map.size);
/* 722 */       while (this.hasNext)
/* 723 */         array.add(next()); 
/* 724 */       return array;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys<K> extends MapIterator<K> implements Iterable<K>, Iterator<K> {
/*     */     public Keys(ObjectFloatMap<K> map) {
/* 730 */       super(map);
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 734 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 735 */       return this.hasNext;
/*     */     }
/*     */     
/*     */     public K next() {
/* 739 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 740 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 741 */       K key = this.map.keyTable[this.nextIndex];
/* 742 */       this.currentIndex = this.nextIndex;
/* 743 */       findNextIndex();
/* 744 */       return key;
/*     */     }
/*     */     
/*     */     public Keys<K> iterator() {
/* 748 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray() {
/* 753 */       Array<K> array = new Array(true, this.map.size);
/* 754 */       while (this.hasNext)
/* 755 */         array.add(next()); 
/* 756 */       return array;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<K> toArray(Array<K> array) {
/* 761 */       while (this.hasNext)
/* 762 */         array.add(next()); 
/* 763 */       return array;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 767 */       super.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ObjectFloatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */