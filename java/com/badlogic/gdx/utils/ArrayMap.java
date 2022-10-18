/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.reflect.ArrayReflection;
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
/*     */ public class ArrayMap<K, V>
/*     */   implements Iterable<ObjectMap.Entry<K, V>>
/*     */ {
/*     */   public K[] keys;
/*     */   public V[] values;
/*     */   public int size;
/*     */   public boolean ordered;
/*     */   private Entries entries1;
/*     */   private Entries entries2;
/*     */   private Values valuesIter1;
/*     */   private Values valuesIter2;
/*     */   private Keys keysIter1;
/*     */   private Keys keysIter2;
/*     */   
/*     */   public ArrayMap() {
/*  44 */     this(true, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayMap(int capacity) {
/*  49 */     this(true, capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayMap(boolean ordered, int capacity) {
/*  56 */     this.ordered = ordered;
/*  57 */     this.keys = (K[])new Object[capacity];
/*  58 */     this.values = (V[])new Object[capacity];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayMap(boolean ordered, int capacity, Class keyArrayType, Class valueArrayType) {
/*  66 */     this.ordered = ordered;
/*  67 */     this.keys = (K[])ArrayReflection.newInstance(keyArrayType, capacity);
/*  68 */     this.values = (V[])ArrayReflection.newInstance(valueArrayType, capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayMap(Class keyArrayType, Class valueArrayType) {
/*  73 */     this(false, 16, keyArrayType, valueArrayType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayMap(ArrayMap array) {
/*  80 */     this(array.ordered, array.size, array.keys.getClass().getComponentType(), array.values.getClass().getComponentType());
/*  81 */     this.size = array.size;
/*  82 */     System.arraycopy(array.keys, 0, this.keys, 0, this.size);
/*  83 */     System.arraycopy(array.values, 0, this.values, 0, this.size);
/*     */   }
/*     */   
/*     */   public int put(K key, V value) {
/*  87 */     int index = indexOfKey(key);
/*  88 */     if (index == -1) {
/*  89 */       if (this.size == this.keys.length) resize(Math.max(8, (int)(this.size * 1.75F))); 
/*  90 */       index = this.size++;
/*     */     } 
/*  92 */     this.keys[index] = key;
/*  93 */     this.values[index] = value;
/*  94 */     return index;
/*     */   }
/*     */   
/*     */   public int put(K key, V value, int index) {
/*  98 */     int existingIndex = indexOfKey(key);
/*  99 */     if (existingIndex != -1) {
/* 100 */       removeIndex(existingIndex);
/* 101 */     } else if (this.size == this.keys.length) {
/* 102 */       resize(Math.max(8, (int)(this.size * 1.75F)));
/* 103 */     }  System.arraycopy(this.keys, index, this.keys, index + 1, this.size - index);
/* 104 */     System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
/* 105 */     this.keys[index] = key;
/* 106 */     this.values[index] = value;
/* 107 */     this.size++;
/* 108 */     return index;
/*     */   }
/*     */   
/*     */   public void putAll(ArrayMap map) {
/* 112 */     putAll(map, 0, map.size);
/*     */   }
/*     */   
/*     */   public void putAll(ArrayMap map, int offset, int length) {
/* 116 */     if (offset + length > map.size)
/* 117 */       throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + map.size); 
/* 118 */     int sizeNeeded = this.size + length - offset;
/* 119 */     if (sizeNeeded >= this.keys.length) resize(Math.max(8, (int)(sizeNeeded * 1.75F))); 
/* 120 */     System.arraycopy(map.keys, offset, this.keys, this.size, length);
/* 121 */     System.arraycopy(map.values, offset, this.values, this.size, length);
/* 122 */     this.size += length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(K key) {
/* 128 */     K[] arrayOfK = this.keys;
/* 129 */     int i = this.size - 1;
/* 130 */     if (key == null)
/* 131 */     { for (; i >= 0; i--) {
/* 132 */         if (arrayOfK[i] == key) return this.values[i]; 
/*     */       }  }
/* 134 */     else { for (; i >= 0; i--) {
/* 135 */         if (key.equals(arrayOfK[i])) return this.values[i]; 
/*     */       }  }
/* 137 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K getKey(V value, boolean identity) {
/* 144 */     V[] arrayOfV = this.values;
/* 145 */     int i = this.size - 1;
/* 146 */     if (identity || value == null)
/* 147 */     { for (; i >= 0; i--) {
/* 148 */         if (arrayOfV[i] == value) return this.keys[i]; 
/*     */       }  }
/* 150 */     else { for (; i >= 0; i--) {
/* 151 */         if (value.equals(arrayOfV[i])) return this.keys[i]; 
/*     */       }  }
/* 153 */      return null;
/*     */   }
/*     */   
/*     */   public K getKeyAt(int index) {
/* 157 */     if (index >= this.size) throw new IndexOutOfBoundsException(String.valueOf(index)); 
/* 158 */     return this.keys[index];
/*     */   }
/*     */   
/*     */   public V getValueAt(int index) {
/* 162 */     if (index >= this.size) throw new IndexOutOfBoundsException(String.valueOf(index)); 
/* 163 */     return this.values[index];
/*     */   }
/*     */   
/*     */   public K firstKey() {
/* 167 */     if (this.size == 0) throw new IllegalStateException("Map is empty."); 
/* 168 */     return this.keys[0];
/*     */   }
/*     */   
/*     */   public V firstValue() {
/* 172 */     if (this.size == 0) throw new IllegalStateException("Map is empty."); 
/* 173 */     return this.values[0];
/*     */   }
/*     */   
/*     */   public void setKey(int index, K key) {
/* 177 */     if (index >= this.size) throw new IndexOutOfBoundsException(String.valueOf(index)); 
/* 178 */     this.keys[index] = key;
/*     */   }
/*     */   
/*     */   public void setValue(int index, V value) {
/* 182 */     if (index >= this.size) throw new IndexOutOfBoundsException(String.valueOf(index)); 
/* 183 */     this.values[index] = value;
/*     */   }
/*     */   
/*     */   public void insert(int index, K key, V value) {
/* 187 */     if (index > this.size) throw new IndexOutOfBoundsException(String.valueOf(index)); 
/* 188 */     if (this.size == this.keys.length) resize(Math.max(8, (int)(this.size * 1.75F))); 
/* 189 */     if (this.ordered) {
/* 190 */       System.arraycopy(this.keys, index, this.keys, index + 1, this.size - index);
/* 191 */       System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
/*     */     } else {
/* 193 */       this.keys[this.size] = this.keys[index];
/* 194 */       this.values[this.size] = this.values[index];
/*     */     } 
/* 196 */     this.size++;
/* 197 */     this.keys[index] = key;
/* 198 */     this.values[index] = value;
/*     */   }
/*     */   
/*     */   public boolean containsKey(K key) {
/* 202 */     K[] keys = this.keys;
/* 203 */     int i = this.size - 1;
/* 204 */     if (key == null)
/* 205 */     { while (i >= 0) {
/* 206 */         if (keys[i--] == key) return true; 
/*     */       }  }
/* 208 */     else { while (i >= 0) {
/* 209 */         if (key.equals(keys[i--])) return true; 
/*     */       }  }
/* 211 */      return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(V value, boolean identity) {
/* 216 */     V[] values = this.values;
/* 217 */     int i = this.size - 1;
/* 218 */     if (identity || value == null)
/* 219 */     { while (i >= 0) {
/* 220 */         if (values[i--] == value) return true; 
/*     */       }  }
/* 222 */     else { while (i >= 0) {
/* 223 */         if (value.equals(values[i--])) return true; 
/*     */       }  }
/* 225 */      return false;
/*     */   }
/*     */   
/*     */   public int indexOfKey(K key) {
/* 229 */     K[] arrayOfK = this.keys;
/* 230 */     if (key == null)
/* 231 */     { for (int i = 0, n = this.size; i < n; i++) {
/* 232 */         if (arrayOfK[i] == key) return i; 
/*     */       }  }
/* 234 */     else { for (int i = 0, n = this.size; i < n; i++) {
/* 235 */         if (key.equals(arrayOfK[i])) return i; 
/*     */       }  }
/* 237 */      return -1;
/*     */   }
/*     */   
/*     */   public int indexOfValue(V value, boolean identity) {
/* 241 */     V[] arrayOfV = this.values;
/* 242 */     if (identity || value == null)
/* 243 */     { for (int i = 0, n = this.size; i < n; i++) {
/* 244 */         if (arrayOfV[i] == value) return i; 
/*     */       }  }
/* 246 */     else { for (int i = 0, n = this.size; i < n; i++) {
/* 247 */         if (value.equals(arrayOfV[i])) return i; 
/*     */       }  }
/* 249 */      return -1;
/*     */   }
/*     */   
/*     */   public V removeKey(K key) {
/* 253 */     K[] arrayOfK = this.keys;
/* 254 */     if (key == null) {
/* 255 */       for (int i = 0, n = this.size; i < n; i++) {
/* 256 */         if (arrayOfK[i] == key) {
/* 257 */           V value = this.values[i];
/* 258 */           removeIndex(i);
/* 259 */           return value;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 263 */       for (int i = 0, n = this.size; i < n; i++) {
/* 264 */         if (key.equals(arrayOfK[i])) {
/* 265 */           V value = this.values[i];
/* 266 */           removeIndex(i);
/* 267 */           return value;
/*     */         } 
/*     */       } 
/*     */     } 
/* 271 */     return null;
/*     */   }
/*     */   
/*     */   public boolean removeValue(V value, boolean identity) {
/* 275 */     V[] arrayOfV = this.values;
/* 276 */     if (identity || value == null) {
/* 277 */       for (int i = 0, n = this.size; i < n; i++) {
/* 278 */         if (arrayOfV[i] == value) {
/* 279 */           removeIndex(i);
/* 280 */           return true;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 284 */       for (int i = 0, n = this.size; i < n; i++) {
/* 285 */         if (value.equals(arrayOfV[i])) {
/* 286 */           removeIndex(i);
/* 287 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 291 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeIndex(int index) {
/* 296 */     if (index >= this.size) throw new IndexOutOfBoundsException(String.valueOf(index)); 
/* 297 */     K[] arrayOfK = this.keys;
/* 298 */     this.size--;
/* 299 */     if (this.ordered) {
/* 300 */       System.arraycopy(arrayOfK, index + 1, arrayOfK, index, this.size - index);
/* 301 */       System.arraycopy(this.values, index + 1, this.values, index, this.size - index);
/*     */     } else {
/* 303 */       arrayOfK[index] = arrayOfK[this.size];
/* 304 */       this.values[index] = this.values[this.size];
/*     */     } 
/* 306 */     arrayOfK[this.size] = null;
/* 307 */     this.values[this.size] = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public K peekKey() {
/* 312 */     return this.keys[this.size - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public V peekValue() {
/* 317 */     return this.values[this.size - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int maximumCapacity) {
/* 322 */     if (this.keys.length <= maximumCapacity) {
/* 323 */       clear();
/*     */       return;
/*     */     } 
/* 326 */     this.size = 0;
/* 327 */     resize(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 331 */     K[] keys = this.keys;
/* 332 */     V[] values = this.values;
/* 333 */     for (int i = 0, n = this.size; i < n; i++) {
/* 334 */       keys[i] = null;
/* 335 */       values[i] = null;
/*     */     } 
/* 337 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shrink() {
/* 343 */     if (this.keys.length == this.size)
/* 344 */       return;  resize(this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additionalCapacity) {
/* 350 */     int sizeNeeded = this.size + additionalCapacity;
/* 351 */     if (sizeNeeded >= this.keys.length) resize(Math.max(8, sizeNeeded)); 
/*     */   }
/*     */   
/*     */   protected void resize(int newSize) {
/* 355 */     K[] newKeys = (K[])ArrayReflection.newInstance(this.keys.getClass().getComponentType(), newSize);
/* 356 */     System.arraycopy(this.keys, 0, newKeys, 0, Math.min(this.size, newKeys.length));
/* 357 */     this.keys = newKeys;
/*     */     
/* 359 */     V[] newValues = (V[])ArrayReflection.newInstance(this.values.getClass().getComponentType(), newSize);
/* 360 */     System.arraycopy(this.values, 0, newValues, 0, Math.min(this.size, newValues.length));
/* 361 */     this.values = newValues;
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 365 */     for (int i = 0, lastIndex = this.size - 1, n = this.size / 2; i < n; i++) {
/* 366 */       int ii = lastIndex - i;
/* 367 */       K tempKey = this.keys[i];
/* 368 */       this.keys[i] = this.keys[ii];
/* 369 */       this.keys[ii] = tempKey;
/*     */       
/* 371 */       V tempValue = this.values[i];
/* 372 */       this.values[i] = this.values[ii];
/* 373 */       this.values[ii] = tempValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shuffle() {
/* 378 */     for (int i = this.size - 1; i >= 0; i--) {
/* 379 */       int ii = MathUtils.random(i);
/* 380 */       K tempKey = this.keys[i];
/* 381 */       this.keys[i] = this.keys[ii];
/* 382 */       this.keys[ii] = tempKey;
/*     */       
/* 384 */       V tempValue = this.values[i];
/* 385 */       this.values[i] = this.values[ii];
/* 386 */       this.values[ii] = tempValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void truncate(int newSize) {
/* 393 */     if (this.size <= newSize)
/* 394 */       return;  for (int i = newSize; i < this.size; i++) {
/* 395 */       this.keys[i] = null;
/* 396 */       this.values[i] = null;
/*     */     } 
/* 398 */     this.size = newSize;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 402 */     K[] keys = this.keys;
/* 403 */     V[] values = this.values;
/* 404 */     int h = 0;
/* 405 */     for (int i = 0, n = this.size; i < n; i++) {
/* 406 */       K key = keys[i];
/* 407 */       V value = values[i];
/* 408 */       if (key != null) h += key.hashCode() * 31; 
/* 409 */       if (value != null) h += value.hashCode(); 
/*     */     } 
/* 411 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 415 */     if (obj == this) return true; 
/* 416 */     if (!(obj instanceof ArrayMap)) return false; 
/* 417 */     ArrayMap<K, V> other = (ArrayMap<K, V>)obj;
/* 418 */     if (other.size != this.size) return false; 
/* 419 */     K[] keys = this.keys;
/* 420 */     V[] values = this.values;
/* 421 */     for (int i = 0, n = this.size; i < n; i++) {
/* 422 */       K key = keys[i];
/* 423 */       V value = values[i];
/* 424 */       if (value == null) {
/* 425 */         if (!other.containsKey(key) || other.get(key) != null) {
/* 426 */           return false;
/*     */         }
/*     */       }
/* 429 */       else if (!value.equals(other.get(key))) {
/* 430 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 434 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 438 */     if (this.size == 0) return "{}"; 
/* 439 */     K[] keys = this.keys;
/* 440 */     V[] values = this.values;
/* 441 */     StringBuilder buffer = new StringBuilder(32);
/* 442 */     buffer.append('{');
/* 443 */     buffer.append(keys[0]);
/* 444 */     buffer.append('=');
/* 445 */     buffer.append(values[0]);
/* 446 */     for (int i = 1; i < this.size; i++) {
/* 447 */       buffer.append(", ");
/* 448 */       buffer.append(keys[i]);
/* 449 */       buffer.append('=');
/* 450 */       buffer.append(values[i]);
/*     */     } 
/* 452 */     buffer.append('}');
/* 453 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public Iterator<ObjectMap.Entry<K, V>> iterator() {
/* 457 */     return entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entries<K, V> entries() {
/* 463 */     if (this.entries1 == null) {
/* 464 */       this.entries1 = new Entries<K, V>(this);
/* 465 */       this.entries2 = new Entries<K, V>(this);
/*     */     } 
/* 467 */     if (!this.entries1.valid) {
/* 468 */       this.entries1.index = 0;
/* 469 */       this.entries1.valid = true;
/* 470 */       this.entries2.valid = false;
/* 471 */       return this.entries1;
/*     */     } 
/* 473 */     this.entries2.index = 0;
/* 474 */     this.entries2.valid = true;
/* 475 */     this.entries1.valid = false;
/* 476 */     return this.entries2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Values<V> values() {
/* 482 */     if (this.valuesIter1 == null) {
/* 483 */       this.valuesIter1 = new Values<V>((ArrayMap)this);
/* 484 */       this.valuesIter2 = new Values<V>((ArrayMap)this);
/*     */     } 
/* 486 */     if (!this.valuesIter1.valid) {
/* 487 */       this.valuesIter1.index = 0;
/* 488 */       this.valuesIter1.valid = true;
/* 489 */       this.valuesIter2.valid = false;
/* 490 */       return this.valuesIter1;
/*     */     } 
/* 492 */     this.valuesIter2.index = 0;
/* 493 */     this.valuesIter2.valid = true;
/* 494 */     this.valuesIter1.valid = false;
/* 495 */     return this.valuesIter2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Keys<K> keys() {
/* 501 */     if (this.keysIter1 == null) {
/* 502 */       this.keysIter1 = new Keys<K>((ArrayMap)this);
/* 503 */       this.keysIter2 = new Keys<K>((ArrayMap)this);
/*     */     } 
/* 505 */     if (!this.keysIter1.valid) {
/* 506 */       this.keysIter1.index = 0;
/* 507 */       this.keysIter1.valid = true;
/* 508 */       this.keysIter2.valid = false;
/* 509 */       return this.keysIter1;
/*     */     } 
/* 511 */     this.keysIter2.index = 0;
/* 512 */     this.keysIter2.valid = true;
/* 513 */     this.keysIter1.valid = false;
/* 514 */     return this.keysIter2;
/*     */   }
/*     */   
/*     */   public static class Entries<K, V> implements Iterable<ObjectMap.Entry<K, V>>, Iterator<ObjectMap.Entry<K, V>> {
/*     */     private final ArrayMap<K, V> map;
/* 519 */     ObjectMap.Entry<K, V> entry = new ObjectMap.Entry<K, V>();
/*     */     int index;
/*     */     boolean valid = true;
/*     */     
/*     */     public Entries(ArrayMap<K, V> map) {
/* 524 */       this.map = map;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 528 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 529 */       return (this.index < this.map.size);
/*     */     }
/*     */     
/*     */     public Iterator<ObjectMap.Entry<K, V>> iterator() {
/* 533 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectMap.Entry<K, V> next() {
/* 538 */       if (this.index >= this.map.size) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 539 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 540 */       this.entry.key = this.map.keys[this.index];
/* 541 */       this.entry.value = this.map.values[this.index++];
/* 542 */       return this.entry;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 546 */       this.index--;
/* 547 */       this.map.removeIndex(this.index);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 551 */       this.index = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Values<V> implements Iterable<V>, Iterator<V> {
/*     */     private final ArrayMap<Object, V> map;
/*     */     int index;
/*     */     boolean valid = true;
/*     */     
/*     */     public Values(ArrayMap<Object, V> map) {
/* 561 */       this.map = map;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 565 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 566 */       return (this.index < this.map.size);
/*     */     }
/*     */     
/*     */     public Iterator<V> iterator() {
/* 570 */       return this;
/*     */     }
/*     */     
/*     */     public V next() {
/* 574 */       if (this.index >= this.map.size) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 575 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 576 */       return this.map.values[this.index++];
/*     */     }
/*     */     
/*     */     public void remove() {
/* 580 */       this.index--;
/* 581 */       this.map.removeIndex(this.index);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 585 */       this.index = 0;
/*     */     }
/*     */     
/*     */     public Array<V> toArray() {
/* 589 */       return new Array<V>(true, this.map.values, this.index, this.map.size - this.index);
/*     */     }
/*     */     
/*     */     public Array<V> toArray(Array<V> array) {
/* 593 */       array.addAll(this.map.values, this.index, this.map.size - this.index);
/* 594 */       return array;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Keys<K> implements Iterable<K>, Iterator<K> {
/*     */     private final ArrayMap<K, Object> map;
/*     */     int index;
/*     */     boolean valid = true;
/*     */     
/*     */     public Keys(ArrayMap<K, Object> map) {
/* 604 */       this.map = map;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 608 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 609 */       return (this.index < this.map.size);
/*     */     }
/*     */     
/*     */     public Iterator<K> iterator() {
/* 613 */       return this;
/*     */     }
/*     */     
/*     */     public K next() {
/* 617 */       if (this.index >= this.map.size) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 618 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 619 */       return this.map.keys[this.index++];
/*     */     }
/*     */     
/*     */     public void remove() {
/* 623 */       this.index--;
/* 624 */       this.map.removeIndex(this.index);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 628 */       this.index = 0;
/*     */     }
/*     */     
/*     */     public Array<K> toArray() {
/* 632 */       return new Array<K>(true, this.map.keys, this.index, this.map.size - this.index);
/*     */     }
/*     */     
/*     */     public Array<K> toArray(Array<K> array) {
/* 636 */       array.addAll(this.map.keys, this.index, this.map.size - this.index);
/* 637 */       return array;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ArrayMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */