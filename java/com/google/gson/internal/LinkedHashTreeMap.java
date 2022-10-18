/*     */ package com.google.gson.internal;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ public final class LinkedHashTreeMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements Serializable
/*     */ {
/*  42 */   private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>() {
/*     */       public int compare(Comparable<Comparable> a, Comparable b) {
/*  44 */         return a.compareTo(b);
/*     */       }
/*     */     };
/*     */   
/*     */   Comparator<? super K> comparator;
/*     */   Node<K, V>[] table;
/*     */   final Node<K, V> header;
/*  51 */   int size = 0;
/*  52 */   int modCount = 0;
/*     */   
/*     */   int threshold;
/*     */   
/*     */   private EntrySet entrySet;
/*     */   
/*     */   private KeySet keySet;
/*     */   
/*     */   public LinkedHashTreeMap() {
/*  61 */     this((Comparator)NATURAL_ORDER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedHashTreeMap(Comparator<? super K> comparator) {
/*  73 */     this.comparator = (comparator != null) ? comparator : (Comparator)NATURAL_ORDER;
/*     */ 
/*     */     
/*  76 */     this.header = new Node<K, V>();
/*  77 */     this.table = (Node<K, V>[])new Node[16];
/*  78 */     this.threshold = this.table.length / 2 + this.table.length / 4;
/*     */   }
/*     */   
/*     */   public int size() {
/*  82 */     return this.size;
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/*  86 */     Node<K, V> node = findByObject(key);
/*  87 */     return (node != null) ? node.value : null;
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  91 */     return (findByObject(key) != null);
/*     */   }
/*     */   
/*     */   public V put(K key, V value) {
/*  95 */     if (key == null) {
/*  96 */       throw new NullPointerException("key == null");
/*     */     }
/*  98 */     Node<K, V> created = find(key, true);
/*  99 */     V result = created.value;
/* 100 */     created.value = value;
/* 101 */     return result;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 105 */     Arrays.fill((Object[])this.table, (Object)null);
/* 106 */     this.size = 0;
/* 107 */     this.modCount++;
/*     */ 
/*     */     
/* 110 */     Node<K, V> header = this.header;
/* 111 */     for (Node<K, V> e = header.next; e != header; ) {
/* 112 */       Node<K, V> next = e.next;
/* 113 */       e.next = e.prev = null;
/* 114 */       e = next;
/*     */     } 
/*     */     
/* 117 */     header.next = header.prev = header;
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 121 */     Node<K, V> node = removeInternalByKey(key);
/* 122 */     return (node != null) ? node.value : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Node<K, V> find(K key, boolean create) {
/*     */     Node<K, V> created;
/* 132 */     Comparator<? super K> comparator = this.comparator;
/* 133 */     Node<K, V>[] table = this.table;
/* 134 */     int hash = secondaryHash(key.hashCode());
/* 135 */     int index = hash & table.length - 1;
/* 136 */     Node<K, V> nearest = table[index];
/* 137 */     int comparison = 0;
/*     */     
/* 139 */     if (nearest != null) {
/*     */ 
/*     */       
/* 142 */       Comparable<Object> comparableKey = (comparator == NATURAL_ORDER) ? (Comparable<Object>)key : null;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 147 */         comparison = (comparableKey != null) ? comparableKey.compareTo(nearest.key) : comparator.compare(key, nearest.key);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         if (comparison == 0) {
/* 153 */           return nearest;
/*     */         }
/*     */ 
/*     */         
/* 157 */         Node<K, V> child = (comparison < 0) ? nearest.left : nearest.right;
/* 158 */         if (child == null) {
/*     */           break;
/*     */         }
/*     */         
/* 162 */         nearest = child;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 167 */     if (!create) {
/* 168 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 172 */     Node<K, V> header = this.header;
/*     */     
/* 174 */     if (nearest == null) {
/*     */       
/* 176 */       if (comparator == NATURAL_ORDER && !(key instanceof Comparable)) {
/* 177 */         throw new ClassCastException(key.getClass().getName() + " is not Comparable");
/*     */       }
/* 179 */       created = new Node<K, V>(nearest, key, hash, header, header.prev);
/* 180 */       table[index] = created;
/*     */     } else {
/* 182 */       created = new Node<K, V>(nearest, key, hash, header, header.prev);
/* 183 */       if (comparison < 0) {
/* 184 */         nearest.left = created;
/*     */       } else {
/* 186 */         nearest.right = created;
/*     */       } 
/* 188 */       rebalance(nearest, true);
/*     */     } 
/*     */     
/* 191 */     if (this.size++ > this.threshold) {
/* 192 */       doubleCapacity();
/*     */     }
/* 194 */     this.modCount++;
/*     */     
/* 196 */     return created;
/*     */   }
/*     */ 
/*     */   
/*     */   Node<K, V> findByObject(Object key) {
/*     */     try {
/* 202 */       return (key != null) ? find((K)key, false) : null;
/* 203 */     } catch (ClassCastException e) {
/* 204 */       return null;
/*     */     } 
/*     */   }
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
/*     */   Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
/* 218 */     Node<K, V> mine = findByObject(entry.getKey());
/* 219 */     boolean valuesEqual = (mine != null && equal(mine.value, entry.getValue()));
/* 220 */     return valuesEqual ? mine : null;
/*     */   }
/*     */   
/*     */   private boolean equal(Object a, Object b) {
/* 224 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int secondaryHash(int h) {
/* 235 */     h ^= h >>> 20 ^ h >>> 12;
/* 236 */     return h ^ h >>> 7 ^ h >>> 4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeInternal(Node<K, V> node, boolean unlink) {
/* 246 */     if (unlink) {
/* 247 */       node.prev.next = node.next;
/* 248 */       node.next.prev = node.prev;
/* 249 */       node.next = node.prev = null;
/*     */     } 
/*     */     
/* 252 */     Node<K, V> left = node.left;
/* 253 */     Node<K, V> right = node.right;
/* 254 */     Node<K, V> originalParent = node.parent;
/* 255 */     if (left != null && right != null) {
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
/* 266 */       Node<K, V> adjacent = (left.height > right.height) ? left.last() : right.first();
/* 267 */       removeInternal(adjacent, false);
/*     */       
/* 269 */       int leftHeight = 0;
/* 270 */       left = node.left;
/* 271 */       if (left != null) {
/* 272 */         leftHeight = left.height;
/* 273 */         adjacent.left = left;
/* 274 */         left.parent = adjacent;
/* 275 */         node.left = null;
/*     */       } 
/* 277 */       int rightHeight = 0;
/* 278 */       right = node.right;
/* 279 */       if (right != null) {
/* 280 */         rightHeight = right.height;
/* 281 */         adjacent.right = right;
/* 282 */         right.parent = adjacent;
/* 283 */         node.right = null;
/*     */       } 
/* 285 */       adjacent.height = Math.max(leftHeight, rightHeight) + 1;
/* 286 */       replaceInParent(node, adjacent); return;
/*     */     } 
/* 288 */     if (left != null) {
/* 289 */       replaceInParent(node, left);
/* 290 */       node.left = null;
/* 291 */     } else if (right != null) {
/* 292 */       replaceInParent(node, right);
/* 293 */       node.right = null;
/*     */     } else {
/* 295 */       replaceInParent(node, null);
/*     */     } 
/*     */     
/* 298 */     rebalance(originalParent, false);
/* 299 */     this.size--;
/* 300 */     this.modCount++;
/*     */   }
/*     */   
/*     */   Node<K, V> removeInternalByKey(Object key) {
/* 304 */     Node<K, V> node = findByObject(key);
/* 305 */     if (node != null) {
/* 306 */       removeInternal(node, true);
/*     */     }
/* 308 */     return node;
/*     */   }
/*     */   
/*     */   private void replaceInParent(Node<K, V> node, Node<K, V> replacement) {
/* 312 */     Node<K, V> parent = node.parent;
/* 313 */     node.parent = null;
/* 314 */     if (replacement != null) {
/* 315 */       replacement.parent = parent;
/*     */     }
/*     */     
/* 318 */     if (parent != null) {
/* 319 */       if (parent.left == node) {
/* 320 */         parent.left = replacement;
/*     */       } else {
/* 322 */         assert parent.right == node;
/* 323 */         parent.right = replacement;
/*     */       } 
/*     */     } else {
/* 326 */       int index = node.hash & this.table.length - 1;
/* 327 */       this.table[index] = replacement;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rebalance(Node<K, V> unbalanced, boolean insert) {
/* 339 */     for (Node<K, V> node = unbalanced; node != null; node = node.parent) {
/* 340 */       Node<K, V> left = node.left;
/* 341 */       Node<K, V> right = node.right;
/* 342 */       int leftHeight = (left != null) ? left.height : 0;
/* 343 */       int rightHeight = (right != null) ? right.height : 0;
/*     */       
/* 345 */       int delta = leftHeight - rightHeight;
/* 346 */       if (delta == -2) {
/* 347 */         Node<K, V> rightLeft = right.left;
/* 348 */         Node<K, V> rightRight = right.right;
/* 349 */         int rightRightHeight = (rightRight != null) ? rightRight.height : 0;
/* 350 */         int rightLeftHeight = (rightLeft != null) ? rightLeft.height : 0;
/*     */         
/* 352 */         int rightDelta = rightLeftHeight - rightRightHeight;
/* 353 */         if (rightDelta == -1 || (rightDelta == 0 && !insert)) {
/* 354 */           rotateLeft(node);
/*     */         } else {
/* 356 */           assert rightDelta == 1;
/* 357 */           rotateRight(right);
/* 358 */           rotateLeft(node);
/*     */         } 
/* 360 */         if (insert) {
/*     */           break;
/*     */         }
/*     */       }
/* 364 */       else if (delta == 2) {
/* 365 */         Node<K, V> leftLeft = left.left;
/* 366 */         Node<K, V> leftRight = left.right;
/* 367 */         int leftRightHeight = (leftRight != null) ? leftRight.height : 0;
/* 368 */         int leftLeftHeight = (leftLeft != null) ? leftLeft.height : 0;
/*     */         
/* 370 */         int leftDelta = leftLeftHeight - leftRightHeight;
/* 371 */         if (leftDelta == 1 || (leftDelta == 0 && !insert)) {
/* 372 */           rotateRight(node);
/*     */         } else {
/* 374 */           assert leftDelta == -1;
/* 375 */           rotateLeft(left);
/* 376 */           rotateRight(node);
/*     */         } 
/* 378 */         if (insert) {
/*     */           break;
/*     */         }
/*     */       }
/* 382 */       else if (delta == 0) {
/* 383 */         node.height = leftHeight + 1;
/* 384 */         if (insert) {
/*     */           break;
/*     */         }
/*     */       } else {
/*     */         
/* 389 */         assert delta == -1 || delta == 1;
/* 390 */         node.height = Math.max(leftHeight, rightHeight) + 1;
/* 391 */         if (!insert) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rotateLeft(Node<K, V> root) {
/* 402 */     Node<K, V> left = root.left;
/* 403 */     Node<K, V> pivot = root.right;
/* 404 */     Node<K, V> pivotLeft = pivot.left;
/* 405 */     Node<K, V> pivotRight = pivot.right;
/*     */ 
/*     */     
/* 408 */     root.right = pivotLeft;
/* 409 */     if (pivotLeft != null) {
/* 410 */       pivotLeft.parent = root;
/*     */     }
/*     */     
/* 413 */     replaceInParent(root, pivot);
/*     */ 
/*     */     
/* 416 */     pivot.left = root;
/* 417 */     root.parent = pivot;
/*     */ 
/*     */     
/* 420 */     root.height = Math.max((left != null) ? left.height : 0, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
/*     */     
/* 422 */     pivot.height = Math.max(root.height, (pivotRight != null) ? pivotRight.height : 0) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rotateRight(Node<K, V> root) {
/* 430 */     Node<K, V> pivot = root.left;
/* 431 */     Node<K, V> right = root.right;
/* 432 */     Node<K, V> pivotLeft = pivot.left;
/* 433 */     Node<K, V> pivotRight = pivot.right;
/*     */ 
/*     */     
/* 436 */     root.left = pivotRight;
/* 437 */     if (pivotRight != null) {
/* 438 */       pivotRight.parent = root;
/*     */     }
/*     */     
/* 441 */     replaceInParent(root, pivot);
/*     */ 
/*     */     
/* 444 */     pivot.right = root;
/* 445 */     root.parent = pivot;
/*     */ 
/*     */     
/* 448 */     root.height = Math.max((right != null) ? right.height : 0, (pivotRight != null) ? pivotRight.height : 0) + 1;
/*     */     
/* 450 */     pivot.height = Math.max(root.height, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 458 */     EntrySet result = this.entrySet;
/* 459 */     return (result != null) ? result : (this.entrySet = new EntrySet());
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 463 */     KeySet result = this.keySet;
/* 464 */     return (result != null) ? result : (this.keySet = new KeySet());
/*     */   }
/*     */   
/*     */   static final class Node<K, V>
/*     */     implements Map.Entry<K, V> {
/*     */     Node<K, V> parent;
/*     */     Node<K, V> left;
/*     */     Node<K, V> right;
/*     */     Node<K, V> next;
/*     */     Node<K, V> prev;
/*     */     final K key;
/*     */     final int hash;
/*     */     V value;
/*     */     int height;
/*     */     
/*     */     Node() {
/* 480 */       this.key = null;
/* 481 */       this.hash = -1;
/* 482 */       this.next = this.prev = this;
/*     */     }
/*     */ 
/*     */     
/*     */     Node(Node<K, V> parent, K key, int hash, Node<K, V> next, Node<K, V> prev) {
/* 487 */       this.parent = parent;
/* 488 */       this.key = key;
/* 489 */       this.hash = hash;
/* 490 */       this.height = 1;
/* 491 */       this.next = next;
/* 492 */       this.prev = prev;
/* 493 */       prev.next = this;
/* 494 */       next.prev = this;
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 498 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 502 */       return this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V value) {
/* 506 */       V oldValue = this.value;
/* 507 */       this.value = value;
/* 508 */       return oldValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 513 */       if (o instanceof Map.Entry) {
/* 514 */         Map.Entry other = (Map.Entry)o;
/* 515 */         return (((this.key == null) ? (other.getKey() == null) : this.key.equals(other.getKey())) && ((this.value == null) ? (other.getValue() == null) : this.value.equals(other.getValue())));
/*     */       } 
/*     */       
/* 518 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 522 */       return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 527 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node<K, V> first() {
/* 534 */       Node<K, V> node = this;
/* 535 */       Node<K, V> child = node.left;
/* 536 */       while (child != null) {
/* 537 */         node = child;
/* 538 */         child = node.left;
/*     */       } 
/* 540 */       return node;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node<K, V> last() {
/* 547 */       Node<K, V> node = this;
/* 548 */       Node<K, V> child = node.right;
/* 549 */       while (child != null) {
/* 550 */         node = child;
/* 551 */         child = node.right;
/*     */       } 
/* 553 */       return node;
/*     */     }
/*     */   }
/*     */   
/*     */   private void doubleCapacity() {
/* 558 */     this.table = doubleCapacity(this.table);
/* 559 */     this.threshold = this.table.length / 2 + this.table.length / 4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Node<K, V>[] doubleCapacity(Node<K, V>[] oldTable) {
/* 568 */     int oldCapacity = oldTable.length;
/*     */     
/* 570 */     Node[] arrayOfNode = new Node[oldCapacity * 2];
/* 571 */     AvlIterator<K, V> iterator = new AvlIterator<K, V>();
/* 572 */     AvlBuilder<K, V> leftBuilder = new AvlBuilder<K, V>();
/* 573 */     AvlBuilder<K, V> rightBuilder = new AvlBuilder<K, V>();
/*     */ 
/*     */     
/* 576 */     for (int i = 0; i < oldCapacity; i++) {
/* 577 */       Node<K, V> root = oldTable[i];
/* 578 */       if (root != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 583 */         iterator.reset(root);
/* 584 */         int leftSize = 0;
/* 585 */         int rightSize = 0; Node<K, V> node;
/* 586 */         while ((node = iterator.next()) != null) {
/* 587 */           if ((node.hash & oldCapacity) == 0) {
/* 588 */             leftSize++; continue;
/*     */           } 
/* 590 */           rightSize++;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 595 */         leftBuilder.reset(leftSize);
/* 596 */         rightBuilder.reset(rightSize);
/* 597 */         iterator.reset(root);
/* 598 */         while ((node = iterator.next()) != null) {
/* 599 */           if ((node.hash & oldCapacity) == 0) {
/* 600 */             leftBuilder.add(node); continue;
/*     */           } 
/* 602 */           rightBuilder.add(node);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 607 */         arrayOfNode[i] = (leftSize > 0) ? leftBuilder.root() : null;
/* 608 */         arrayOfNode[i + oldCapacity] = (rightSize > 0) ? rightBuilder.root() : null;
/*     */       } 
/* 610 */     }  return (Node<K, V>[])arrayOfNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class AvlIterator<K, V>
/*     */   {
/*     */     private LinkedHashTreeMap.Node<K, V> stackTop;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void reset(LinkedHashTreeMap.Node<K, V> root) {
/* 627 */       LinkedHashTreeMap.Node<K, V> stackTop = null;
/* 628 */       for (LinkedHashTreeMap.Node<K, V> n = root; n != null; n = n.left) {
/* 629 */         n.parent = stackTop;
/* 630 */         stackTop = n;
/*     */       } 
/* 632 */       this.stackTop = stackTop;
/*     */     }
/*     */     
/*     */     public LinkedHashTreeMap.Node<K, V> next() {
/* 636 */       LinkedHashTreeMap.Node<K, V> stackTop = this.stackTop;
/* 637 */       if (stackTop == null) {
/* 638 */         return null;
/*     */       }
/* 640 */       LinkedHashTreeMap.Node<K, V> result = stackTop;
/* 641 */       stackTop = result.parent;
/* 642 */       result.parent = null;
/* 643 */       for (LinkedHashTreeMap.Node<K, V> n = result.right; n != null; n = n.left) {
/* 644 */         n.parent = stackTop;
/* 645 */         stackTop = n;
/*     */       } 
/* 647 */       this.stackTop = stackTop;
/* 648 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AvlBuilder<K, V>
/*     */   {
/*     */     private LinkedHashTreeMap.Node<K, V> stack;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int leavesToSkip;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int leavesSkipped;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int size;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void reset(int targetSize) {
/* 679 */       int treeCapacity = Integer.highestOneBit(targetSize) * 2 - 1;
/* 680 */       this.leavesToSkip = treeCapacity - targetSize;
/* 681 */       this.size = 0;
/* 682 */       this.leavesSkipped = 0;
/* 683 */       this.stack = null;
/*     */     }
/*     */     
/*     */     void add(LinkedHashTreeMap.Node<K, V> node) {
/* 687 */       node.left = node.parent = node.right = null;
/* 688 */       node.height = 1;
/*     */ 
/*     */       
/* 691 */       if (this.leavesToSkip > 0 && (this.size & 0x1) == 0) {
/* 692 */         this.size++;
/* 693 */         this.leavesToSkip--;
/* 694 */         this.leavesSkipped++;
/*     */       } 
/*     */       
/* 697 */       node.parent = this.stack;
/* 698 */       this.stack = node;
/* 699 */       this.size++;
/*     */ 
/*     */       
/* 702 */       if (this.leavesToSkip > 0 && (this.size & 0x1) == 0) {
/* 703 */         this.size++;
/* 704 */         this.leavesToSkip--;
/* 705 */         this.leavesSkipped++;
/*     */       } 
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
/* 721 */       for (int scale = 4; (this.size & scale - 1) == scale - 1; scale *= 2) {
/* 722 */         if (this.leavesSkipped == 0) {
/*     */           
/* 724 */           LinkedHashTreeMap.Node<K, V> right = this.stack;
/* 725 */           LinkedHashTreeMap.Node<K, V> center = right.parent;
/* 726 */           LinkedHashTreeMap.Node<K, V> left = center.parent;
/* 727 */           center.parent = left.parent;
/* 728 */           this.stack = center;
/*     */           
/* 730 */           center.left = left;
/* 731 */           center.right = right;
/* 732 */           right.height++;
/* 733 */           left.parent = center;
/* 734 */           right.parent = center;
/* 735 */         } else if (this.leavesSkipped == 1) {
/*     */           
/* 737 */           LinkedHashTreeMap.Node<K, V> right = this.stack;
/* 738 */           LinkedHashTreeMap.Node<K, V> center = right.parent;
/* 739 */           this.stack = center;
/*     */           
/* 741 */           center.right = right;
/* 742 */           right.height++;
/* 743 */           right.parent = center;
/* 744 */           this.leavesSkipped = 0;
/* 745 */         } else if (this.leavesSkipped == 2) {
/* 746 */           this.leavesSkipped = 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     LinkedHashTreeMap.Node<K, V> root() {
/* 752 */       LinkedHashTreeMap.Node<K, V> stackTop = this.stack;
/* 753 */       if (stackTop.parent != null) {
/* 754 */         throw new IllegalStateException();
/*     */       }
/* 756 */       return stackTop;
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
/* 761 */     LinkedHashTreeMap.Node<K, V> next = LinkedHashTreeMap.this.header.next;
/* 762 */     LinkedHashTreeMap.Node<K, V> lastReturned = null;
/* 763 */     int expectedModCount = LinkedHashTreeMap.this.modCount;
/*     */     
/*     */     public final boolean hasNext() {
/* 766 */       return (this.next != LinkedHashTreeMap.this.header);
/*     */     }
/*     */     
/*     */     final LinkedHashTreeMap.Node<K, V> nextNode() {
/* 770 */       LinkedHashTreeMap.Node<K, V> e = this.next;
/* 771 */       if (e == LinkedHashTreeMap.this.header) {
/* 772 */         throw new NoSuchElementException();
/*     */       }
/* 774 */       if (LinkedHashTreeMap.this.modCount != this.expectedModCount) {
/* 775 */         throw new ConcurrentModificationException();
/*     */       }
/* 777 */       this.next = e.next;
/* 778 */       return this.lastReturned = e;
/*     */     }
/*     */     private LinkedTreeMapIterator() {}
/*     */     public final void remove() {
/* 782 */       if (this.lastReturned == null) {
/* 783 */         throw new IllegalStateException();
/*     */       }
/* 785 */       LinkedHashTreeMap.this.removeInternal(this.lastReturned, true);
/* 786 */       this.lastReturned = null;
/* 787 */       this.expectedModCount = LinkedHashTreeMap.this.modCount;
/*     */     }
/*     */   }
/*     */   
/*     */   final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
/*     */     public int size() {
/* 793 */       return LinkedHashTreeMap.this.size;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 797 */       return new LinkedHashTreeMap<K, V>.LinkedTreeMapIterator<Map.Entry<K, V>>() {
/*     */           public Map.Entry<K, V> next() {
/* 799 */             return nextNode();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean contains(Object o) {
/* 805 */       return (o instanceof Map.Entry && LinkedHashTreeMap.this.findByEntry((Map.Entry<?, ?>)o) != null);
/*     */     }
/*     */     
/*     */     public boolean remove(Object o) {
/* 809 */       if (!(o instanceof Map.Entry)) {
/* 810 */         return false;
/*     */       }
/*     */       
/* 813 */       LinkedHashTreeMap.Node<K, V> node = LinkedHashTreeMap.this.findByEntry((Map.Entry<?, ?>)o);
/* 814 */       if (node == null) {
/* 815 */         return false;
/*     */       }
/* 817 */       LinkedHashTreeMap.this.removeInternal(node, true);
/* 818 */       return true;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 822 */       LinkedHashTreeMap.this.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   final class KeySet extends AbstractSet<K> {
/*     */     public int size() {
/* 828 */       return LinkedHashTreeMap.this.size;
/*     */     }
/*     */     
/*     */     public Iterator<K> iterator() {
/* 832 */       return new LinkedHashTreeMap<K, V>.LinkedTreeMapIterator<K>() {
/*     */           public K next() {
/* 834 */             return (nextNode()).key;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean contains(Object o) {
/* 840 */       return LinkedHashTreeMap.this.containsKey(o);
/*     */     }
/*     */     
/*     */     public boolean remove(Object key) {
/* 844 */       return (LinkedHashTreeMap.this.removeInternalByKey(key) != null);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 848 */       LinkedHashTreeMap.this.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object writeReplace() throws ObjectStreamException {
/* 859 */     return new LinkedHashMap<K, V>(this);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\LinkedHashTreeMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */