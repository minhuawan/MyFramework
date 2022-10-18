/*     */ package com.badlogic.gdx.utils;
/*     */ 
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
/*     */ public class Queue<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   protected T[] values;
/*  32 */   protected int head = 0;
/*     */ 
/*     */ 
/*     */   
/*  36 */   protected int tail = 0;
/*     */ 
/*     */   
/*  39 */   public int size = 0;
/*     */   
/*     */   private QueueIterable iterable;
/*     */ 
/*     */   
/*     */   public Queue() {
/*  45 */     this(16);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue(int initialSize) {
/*  51 */     this.values = (T[])new Object[initialSize];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue(int initialSize, Class<T> type) {
/*  58 */     this.values = (T[])ArrayReflection.newInstance(type, initialSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLast(T object) {
/*  64 */     T[] values = this.values;
/*     */     
/*  66 */     if (this.size == values.length) {
/*  67 */       resize(values.length << 1);
/*  68 */       values = this.values;
/*     */     } 
/*     */     
/*  71 */     values[this.tail++] = object;
/*  72 */     if (this.tail == values.length) {
/*  73 */       this.tail = 0;
/*     */     }
/*  75 */     this.size++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(T object) {
/*  82 */     T[] values = this.values;
/*     */     
/*  84 */     if (this.size == values.length) {
/*  85 */       resize(values.length << 1);
/*  86 */       values = this.values;
/*     */     } 
/*     */     
/*  89 */     int head = this.head;
/*  90 */     head--;
/*  91 */     if (head == -1) {
/*  92 */       head = values.length - 1;
/*     */     }
/*  94 */     values[head] = object;
/*     */     
/*  96 */     this.head = head;
/*  97 */     this.size++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int additional) {
/* 103 */     int needed = this.size + additional;
/* 104 */     if (this.values.length < needed) {
/* 105 */       resize(needed);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resize(int newSize) {
/* 111 */     T[] values = this.values;
/* 112 */     int head = this.head;
/* 113 */     int tail = this.tail;
/*     */ 
/*     */     
/* 116 */     T[] newArray = (T[])ArrayReflection.newInstance(values.getClass().getComponentType(), newSize);
/* 117 */     if (head < tail) {
/*     */       
/* 119 */       System.arraycopy(values, head, newArray, 0, tail - head);
/* 120 */     } else if (this.size > 0) {
/*     */       
/* 122 */       int rest = values.length - head;
/* 123 */       System.arraycopy(values, head, newArray, 0, rest);
/* 124 */       System.arraycopy(values, 0, newArray, rest, tail);
/*     */     } 
/* 126 */     this.values = newArray;
/* 127 */     this.head = 0;
/* 128 */     this.tail = this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T removeFirst() {
/* 135 */     if (this.size == 0)
/*     */     {
/* 137 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/*     */     
/* 140 */     T[] values = this.values;
/*     */     
/* 142 */     T result = values[this.head];
/* 143 */     values[this.head] = null;
/* 144 */     this.head++;
/* 145 */     if (this.head == values.length) {
/* 146 */       this.head = 0;
/*     */     }
/* 148 */     this.size--;
/*     */     
/* 150 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T removeLast() {
/* 158 */     if (this.size == 0) {
/* 159 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/*     */     
/* 162 */     T[] values = this.values;
/* 163 */     int tail = this.tail;
/* 164 */     tail--;
/* 165 */     if (tail == -1) {
/* 166 */       tail = values.length - 1;
/*     */     }
/* 168 */     T result = values[tail];
/* 169 */     values[tail] = null;
/* 170 */     this.tail = tail;
/* 171 */     this.size--;
/*     */     
/* 173 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(T value, boolean identity) {
/* 180 */     if (this.size == 0) return -1; 
/* 181 */     T[] values = this.values;
/* 182 */     int head = this.head, tail = this.tail;
/* 183 */     if (identity || value == null)
/* 184 */     { if (head < tail)
/* 185 */       { for (int i = head; i < tail; i++) {
/* 186 */           if (values[i] == value) return i; 
/*     */         }  }
/* 188 */       else { int i; int n; for (i = head, n = values.length; i < n; i++) {
/* 189 */           if (values[i] == value) return i - head; 
/* 190 */         }  for (i = 0; i < tail; i++) {
/* 191 */           if (values[i] == value) return i + values.length - head; 
/*     */         }  }
/*     */        }
/* 194 */     else if (head < tail)
/* 195 */     { for (int i = head; i < tail; i++) {
/* 196 */         if (value.equals(values[i])) return i; 
/*     */       }  }
/* 198 */     else { int i; int n; for (i = head, n = values.length; i < n; i++) {
/* 199 */         if (value.equals(values[i])) return i - head; 
/* 200 */       }  for (i = 0; i < tail; i++) {
/* 201 */         if (value.equals(values[i])) return i + values.length - head; 
/*     */       }  }
/*     */     
/* 204 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeValue(T value, boolean identity) {
/* 211 */     int index = indexOf(value, identity);
/* 212 */     if (index == -1) return false; 
/* 213 */     removeIndex(index);
/* 214 */     return true;
/*     */   }
/*     */   
/*     */   public T removeIndex(int index) {
/*     */     T value;
/* 219 */     if (index < 0) throw new IndexOutOfBoundsException("index can't be < 0: " + index); 
/* 220 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
/*     */     
/* 222 */     T[] values = this.values;
/* 223 */     int head = this.head, tail = this.tail;
/* 224 */     index += head;
/*     */     
/* 226 */     if (head < tail) {
/* 227 */       value = values[index];
/* 228 */       System.arraycopy(values, index + 1, values, index, tail - index);
/* 229 */       values[tail] = null;
/* 230 */       this.tail--;
/* 231 */     } else if (index >= values.length) {
/* 232 */       index -= values.length;
/* 233 */       value = values[index];
/* 234 */       System.arraycopy(values, index + 1, values, index, tail - index);
/* 235 */       this.tail--;
/*     */     } else {
/* 237 */       value = values[index];
/* 238 */       System.arraycopy(values, head, values, head + 1, index - head);
/* 239 */       values[head] = null;
/* 240 */       this.head++;
/* 241 */       if (this.head == values.length) {
/* 242 */         this.head = 0;
/*     */       }
/*     */     } 
/* 245 */     this.size--;
/* 246 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T first() {
/* 254 */     if (this.size == 0)
/*     */     {
/* 256 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/* 258 */     return this.values[this.head];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T last() {
/* 266 */     if (this.size == 0)
/*     */     {
/* 268 */       throw new NoSuchElementException("Queue is empty.");
/*     */     }
/* 270 */     T[] values = this.values;
/* 271 */     int tail = this.tail;
/* 272 */     tail--;
/* 273 */     if (tail == -1) {
/* 274 */       tail = values.length - 1;
/*     */     }
/* 276 */     return values[tail];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(int index) {
/* 283 */     if (index < 0) throw new IndexOutOfBoundsException("index can't be < 0: " + index); 
/* 284 */     if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size); 
/* 285 */     T[] values = this.values;
/*     */     
/* 287 */     int i = this.head + index;
/* 288 */     if (i >= values.length) {
/* 289 */       i -= values.length;
/*     */     }
/* 291 */     return values[i];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 297 */     if (this.size == 0)
/* 298 */       return;  T[] values = this.values;
/* 299 */     int head = this.head;
/* 300 */     int tail = this.tail;
/*     */     
/* 302 */     if (head < tail) {
/*     */       
/* 304 */       for (int i = head; i < tail; i++) {
/* 305 */         values[i] = null;
/*     */       }
/*     */     } else {
/*     */       int i;
/* 309 */       for (i = head; i < values.length; i++) {
/* 310 */         values[i] = null;
/*     */       }
/* 312 */       for (i = 0; i < tail; i++) {
/* 313 */         values[i] = null;
/*     */       }
/*     */     } 
/* 316 */     this.head = 0;
/* 317 */     this.tail = 0;
/* 318 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 324 */     if (this.iterable == null) this.iterable = new QueueIterable<T>(this); 
/* 325 */     return this.iterable.iterator();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 329 */     if (this.size == 0) {
/* 330 */       return "[]";
/*     */     }
/* 332 */     T[] values = this.values;
/* 333 */     int head = this.head;
/* 334 */     int tail = this.tail;
/*     */     
/* 336 */     StringBuilder sb = new StringBuilder(64);
/* 337 */     sb.append('[');
/* 338 */     sb.append(values[head]); int i;
/* 339 */     for (i = (head + 1) % values.length; i != tail; i = (i + 1) % values.length) {
/* 340 */       sb.append(", ").append(values[i]);
/*     */     }
/* 342 */     sb.append(']');
/* 343 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 347 */     int size = this.size;
/* 348 */     T[] values = this.values;
/* 349 */     int backingLength = values.length;
/* 350 */     int index = this.head;
/*     */     
/* 352 */     int hash = size + 1;
/* 353 */     for (int s = 0; s < size; s++) {
/* 354 */       T value = values[index];
/*     */       
/* 356 */       hash *= 31;
/* 357 */       if (value != null) hash += value.hashCode();
/*     */       
/* 359 */       index++;
/* 360 */       if (index == backingLength) index = 0;
/*     */     
/*     */     } 
/* 363 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 367 */     if (this == o) return true; 
/* 368 */     if (o == null || !(o instanceof Queue)) return false;
/*     */     
/* 370 */     Queue<?> q = (Queue)o;
/* 371 */     int size = this.size;
/*     */     
/* 373 */     if (q.size != size) return false;
/*     */     
/* 375 */     T[] myValues = this.values;
/* 376 */     int myBackingLength = myValues.length;
/* 377 */     T[] arrayOfT1 = q.values;
/* 378 */     int itsBackingLength = arrayOfT1.length;
/*     */     
/* 380 */     int myIndex = this.head;
/* 381 */     int itsIndex = q.head;
/* 382 */     for (int s = 0; s < size; ) {
/* 383 */       T myValue = myValues[myIndex];
/* 384 */       Object itsValue = arrayOfT1[itsIndex];
/*     */       
/* 386 */       if ((myValue == null) ? (itsValue == null) : myValue.equals(itsValue)) {
/* 387 */         myIndex++;
/* 388 */         itsIndex++;
/* 389 */         if (myIndex == myBackingLength) myIndex = 0; 
/* 390 */         if (itsIndex == itsBackingLength) itsIndex = 0;  s++;
/*     */       }  return false;
/* 392 */     }  return true;
/*     */   }
/*     */   
/*     */   public static class QueueIterator<T>
/*     */     implements Iterator<T>, Iterable<T>
/*     */   {
/*     */     private final Queue<T> queue;
/*     */     private final boolean allowRemove;
/*     */     int index;
/*     */     boolean valid = true;
/*     */     
/*     */     public QueueIterator(Queue<T> queue) {
/* 404 */       this(queue, true);
/*     */     }
/*     */     
/*     */     public QueueIterator(Queue<T> queue, boolean allowRemove) {
/* 408 */       this.queue = queue;
/* 409 */       this.allowRemove = allowRemove;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 413 */       if (!this.valid)
/*     */       {
/* 415 */         throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       }
/* 417 */       return (this.index < this.queue.size);
/*     */     }
/*     */     
/*     */     public T next() {
/* 421 */       if (this.index >= this.queue.size) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 422 */       if (!this.valid)
/*     */       {
/* 424 */         throw new GdxRuntimeException("#iterator() cannot be used nested.");
/*     */       }
/* 426 */       return this.queue.get(this.index++);
/*     */     }
/*     */     
/*     */     public void remove() {
/* 430 */       if (!this.allowRemove) throw new GdxRuntimeException("Remove not allowed."); 
/* 431 */       this.index--;
/* 432 */       this.queue.removeIndex(this.index);
/*     */     }
/*     */     
/*     */     public void reset() {
/* 436 */       this.index = 0;
/*     */     }
/*     */     
/*     */     public Iterator<T> iterator() {
/* 440 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class QueueIterable<T>
/*     */     implements Iterable<T> {
/*     */     private final Queue<T> queue;
/*     */     private final boolean allowRemove;
/*     */     private Queue.QueueIterator iterator1;
/*     */     private Queue.QueueIterator iterator2;
/*     */     
/*     */     public QueueIterable(Queue<T> queue) {
/* 452 */       this(queue, true);
/*     */     }
/*     */     
/*     */     public QueueIterable(Queue<T> queue, boolean allowRemove) {
/* 456 */       this.queue = queue;
/* 457 */       this.allowRemove = allowRemove;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 463 */       if (this.iterator1 == null) {
/* 464 */         this.iterator1 = new Queue.QueueIterator<T>(this.queue, this.allowRemove);
/* 465 */         this.iterator2 = new Queue.QueueIterator<T>(this.queue, this.allowRemove);
/*     */       } 
/*     */ 
/*     */       
/* 469 */       if (!this.iterator1.valid) {
/* 470 */         this.iterator1.index = 0;
/* 471 */         this.iterator1.valid = true;
/* 472 */         this.iterator2.valid = false;
/* 473 */         return this.iterator1;
/*     */       } 
/* 475 */       this.iterator2.index = 0;
/* 476 */       this.iterator2.valid = true;
/* 477 */       this.iterator1.valid = false;
/* 478 */       return this.iterator2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Queue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */