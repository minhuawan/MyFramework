/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TimSort<T>
/*     */ {
/*     */   private static final int MIN_MERGE = 32;
/*     */   private T[] a;
/*     */   private Comparator<? super T> c;
/*     */   private static final int MIN_GALLOP = 7;
/*  64 */   private int minGallop = 7;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T[] tmp;
/*     */ 
/*     */ 
/*     */   
/*     */   private int tmpCount;
/*     */ 
/*     */ 
/*     */   
/*  82 */   private int stackSize = 0;
/*     */   
/*     */   private final int[] runBase;
/*     */   
/*     */   private final int[] runLen;
/*     */   
/*     */   private static final boolean DEBUG = false;
/*     */   
/*     */   TimSort() {
/*  91 */     this.tmp = (T[])new Object[256];
/*  92 */     this.runBase = new int[40];
/*  93 */     this.runLen = new int[40];
/*     */   }
/*     */   
/*     */   public void doSort(T[] a, Comparator<T> c, int lo, int hi) {
/*  97 */     this.stackSize = 0;
/*  98 */     rangeCheck(a.length, lo, hi);
/*  99 */     int nRemaining = hi - lo;
/* 100 */     if (nRemaining < 2) {
/*     */       return;
/*     */     }
/* 103 */     if (nRemaining < 32) {
/* 104 */       int initRunLen = countRunAndMakeAscending(a, lo, hi, c);
/* 105 */       binarySort(a, lo, hi, lo + initRunLen, c);
/*     */       
/*     */       return;
/*     */     } 
/* 109 */     this.a = a;
/* 110 */     this.c = c;
/* 111 */     this.tmpCount = 0;
/*     */ 
/*     */ 
/*     */     
/* 115 */     int minRun = minRunLength(nRemaining);
/*     */     
/*     */     do {
/* 118 */       int runLen = countRunAndMakeAscending(a, lo, hi, c);
/*     */ 
/*     */       
/* 121 */       if (runLen < minRun) {
/* 122 */         int force = (nRemaining <= minRun) ? nRemaining : minRun;
/* 123 */         binarySort(a, lo, lo + force, lo + runLen, c);
/* 124 */         runLen = force;
/*     */       } 
/*     */ 
/*     */       
/* 128 */       pushRun(lo, runLen);
/* 129 */       mergeCollapse();
/*     */ 
/*     */       
/* 132 */       lo += runLen;
/* 133 */       nRemaining -= runLen;
/* 134 */     } while (nRemaining != 0);
/*     */ 
/*     */ 
/*     */     
/* 138 */     mergeForceCollapse();
/*     */ 
/*     */     
/* 141 */     this.a = null;
/* 142 */     this.c = null;
/* 143 */     T[] tmp = this.tmp;
/* 144 */     for (int i = 0, n = this.tmpCount; i < n; i++) {
/* 145 */       tmp[i] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TimSort(T[] a, Comparator<? super T> c) {
/* 153 */     this.a = a;
/* 154 */     this.c = c;
/*     */ 
/*     */     
/* 157 */     int len = a.length;
/* 158 */     T[] newArray = (T[])new Object[(len < 512) ? (len >>> 1) : 256];
/* 159 */     this.tmp = newArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     int stackLen = (len < 120) ? 5 : ((len < 1542) ? 10 : ((len < 119151) ? 19 : 40));
/* 169 */     this.runBase = new int[stackLen];
/* 170 */     this.runLen = new int[stackLen];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> void sort(T[] a, Comparator<? super T> c) {
/* 179 */     sort(a, 0, a.length, c);
/*     */   }
/*     */   
/*     */   static <T> void sort(T[] a, int lo, int hi, Comparator<? super T> c) {
/* 183 */     if (c == null) {
/* 184 */       Arrays.sort((Object[])a, lo, hi);
/*     */       
/*     */       return;
/*     */     } 
/* 188 */     rangeCheck(a.length, lo, hi);
/* 189 */     int nRemaining = hi - lo;
/* 190 */     if (nRemaining < 2) {
/*     */       return;
/*     */     }
/* 193 */     if (nRemaining < 32) {
/* 194 */       int initRunLen = countRunAndMakeAscending(a, lo, hi, c);
/* 195 */       binarySort(a, lo, hi, lo + initRunLen, c);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 201 */     TimSort<T> ts = new TimSort<T>(a, c);
/* 202 */     int minRun = minRunLength(nRemaining);
/*     */     
/*     */     do {
/* 205 */       int runLen = countRunAndMakeAscending(a, lo, hi, c);
/*     */ 
/*     */       
/* 208 */       if (runLen < minRun) {
/* 209 */         int force = (nRemaining <= minRun) ? nRemaining : minRun;
/* 210 */         binarySort(a, lo, lo + force, lo + runLen, c);
/* 211 */         runLen = force;
/*     */       } 
/*     */ 
/*     */       
/* 215 */       ts.pushRun(lo, runLen);
/* 216 */       ts.mergeCollapse();
/*     */ 
/*     */       
/* 219 */       lo += runLen;
/* 220 */       nRemaining -= runLen;
/* 221 */     } while (nRemaining != 0);
/*     */ 
/*     */ 
/*     */     
/* 225 */     ts.mergeForceCollapse();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> void binarySort(T[] a, int lo, int hi, int start, Comparator<? super T> c) {
/* 243 */     if (start == lo) start++; 
/* 244 */     for (; start < hi; start++) {
/* 245 */       T pivot = a[start];
/*     */ 
/*     */       
/* 248 */       int left = lo;
/* 249 */       int right = start;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 254 */       while (left < right) {
/* 255 */         int mid = left + right >>> 1;
/* 256 */         if (c.compare(pivot, a[mid]) < 0) {
/* 257 */           right = mid; continue;
/*     */         } 
/* 259 */         left = mid + 1;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 268 */       int n = start - left;
/*     */       
/* 270 */       switch (n) {
/*     */         case 2:
/* 272 */           a[left + 2] = a[left + 1];
/*     */         case 1:
/* 274 */           a[left + 1] = a[left];
/*     */           break;
/*     */         default:
/* 277 */           System.arraycopy(a, left, a, left + 1, n); break;
/*     */       } 
/* 279 */       a[left] = pivot;
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
/*     */   private static <T> int countRunAndMakeAscending(T[] a, int lo, int hi, Comparator<? super T> c) {
/* 304 */     int runHi = lo + 1;
/* 305 */     if (runHi == hi) return 1;
/*     */ 
/*     */     
/* 308 */     if (c.compare(a[runHi++], a[lo]) < 0) {
/* 309 */       while (runHi < hi && c.compare(a[runHi], a[runHi - 1]) < 0)
/* 310 */         runHi++; 
/* 311 */       reverseRange((Object[])a, lo, runHi);
/*     */     } else {
/* 313 */       while (runHi < hi && c.compare(a[runHi], a[runHi - 1]) >= 0) {
/* 314 */         runHi++;
/*     */       }
/*     */     } 
/* 317 */     return runHi - lo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reverseRange(Object[] a, int lo, int hi) {
/* 326 */     hi--;
/* 327 */     while (lo < hi) {
/* 328 */       Object t = a[lo];
/* 329 */       a[lo++] = a[hi];
/* 330 */       a[hi--] = t;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int minRunLength(int n) {
/* 349 */     int r = 0;
/* 350 */     while (n >= 32) {
/* 351 */       r |= n & 0x1;
/* 352 */       n >>= 1;
/*     */     } 
/* 354 */     return n + r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushRun(int runBase, int runLen) {
/* 362 */     this.runBase[this.stackSize] = runBase;
/* 363 */     this.runLen[this.stackSize] = runLen;
/* 364 */     this.stackSize++;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeCollapse() {
/* 380 */     while (this.stackSize > 1) {
/* 381 */       int n = this.stackSize - 2;
/* 382 */       if ((n >= 1 && this.runLen[n - 1] <= this.runLen[n] + this.runLen[n + 1]) || (n >= 2 && this.runLen[n - 2] <= this.runLen[n] + this.runLen[n - 1])) {
/* 383 */         if (this.runLen[n - 1] < this.runLen[n + 1]) n--; 
/* 384 */       } else if (this.runLen[n] > this.runLen[n + 1]) {
/*     */         break;
/*     */       } 
/* 387 */       mergeAt(n);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void mergeForceCollapse() {
/* 393 */     while (this.stackSize > 1) {
/* 394 */       int n = this.stackSize - 2;
/* 395 */       if (n > 0 && this.runLen[n - 1] < this.runLen[n + 1]) n--; 
/* 396 */       mergeAt(n);
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
/*     */   private void mergeAt(int i) {
/* 409 */     int base1 = this.runBase[i];
/* 410 */     int len1 = this.runLen[i];
/* 411 */     int base2 = this.runBase[i + 1];
/* 412 */     int len2 = this.runLen[i + 1];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 420 */     this.runLen[i] = len1 + len2;
/* 421 */     if (i == this.stackSize - 3) {
/* 422 */       this.runBase[i + 1] = this.runBase[i + 2];
/* 423 */       this.runLen[i + 1] = this.runLen[i + 2];
/*     */     } 
/* 425 */     this.stackSize--;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     int k = gallopRight(this.a[base2], this.a, base1, len1, 0, this.c);
/*     */     
/* 433 */     base1 += k;
/* 434 */     len1 -= k;
/* 435 */     if (len1 == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 441 */     len2 = gallopLeft(this.a[base1 + len1 - 1], this.a, base2, len2, len2 - 1, this.c);
/*     */     
/* 443 */     if (len2 == 0) {
/*     */       return;
/*     */     }
/* 446 */     if (len1 <= len2) {
/* 447 */       mergeLo(base1, len1, base2, len2);
/*     */     } else {
/* 449 */       mergeHi(base1, len1, base2, len2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> int gallopLeft(T key, T[] a, int base, int len, int hint, Comparator<? super T> c) {
/* 467 */     int lastOfs = 0;
/* 468 */     int ofs = 1;
/* 469 */     if (c.compare(key, a[base + hint]) > 0) {
/*     */       
/* 471 */       int maxOfs = len - hint;
/* 472 */       while (ofs < maxOfs && c.compare(key, a[base + hint + ofs]) > 0) {
/* 473 */         lastOfs = ofs;
/* 474 */         ofs = (ofs << 1) + 1;
/* 475 */         if (ofs <= 0)
/* 476 */           ofs = maxOfs; 
/*     */       } 
/* 478 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 481 */       lastOfs += hint;
/* 482 */       ofs += hint;
/*     */     } else {
/*     */       
/* 485 */       int maxOfs = hint + 1;
/* 486 */       while (ofs < maxOfs && c.compare(key, a[base + hint - ofs]) <= 0) {
/* 487 */         lastOfs = ofs;
/* 488 */         ofs = (ofs << 1) + 1;
/* 489 */         if (ofs <= 0)
/* 490 */           ofs = maxOfs; 
/*     */       } 
/* 492 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 495 */       int tmp = lastOfs;
/* 496 */       lastOfs = hint - ofs;
/* 497 */       ofs = hint - tmp;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 505 */     lastOfs++;
/* 506 */     while (lastOfs < ofs) {
/* 507 */       int m = lastOfs + (ofs - lastOfs >>> 1);
/*     */       
/* 509 */       if (c.compare(key, a[base + m]) > 0) {
/* 510 */         lastOfs = m + 1; continue;
/*     */       } 
/* 512 */       ofs = m;
/*     */     } 
/*     */     
/* 515 */     return ofs;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> int gallopRight(T key, T[] a, int base, int len, int hint, Comparator<? super T> c) {
/* 532 */     int ofs = 1;
/* 533 */     int lastOfs = 0;
/* 534 */     if (c.compare(key, a[base + hint]) < 0) {
/*     */       
/* 536 */       int maxOfs = hint + 1;
/* 537 */       while (ofs < maxOfs && c.compare(key, a[base + hint - ofs]) < 0) {
/* 538 */         lastOfs = ofs;
/* 539 */         ofs = (ofs << 1) + 1;
/* 540 */         if (ofs <= 0)
/* 541 */           ofs = maxOfs; 
/*     */       } 
/* 543 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 546 */       int tmp = lastOfs;
/* 547 */       lastOfs = hint - ofs;
/* 548 */       ofs = hint - tmp;
/*     */     } else {
/*     */       
/* 551 */       int maxOfs = len - hint;
/* 552 */       while (ofs < maxOfs && c.compare(key, a[base + hint + ofs]) >= 0) {
/* 553 */         lastOfs = ofs;
/* 554 */         ofs = (ofs << 1) + 1;
/* 555 */         if (ofs <= 0)
/* 556 */           ofs = maxOfs; 
/*     */       } 
/* 558 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 561 */       lastOfs += hint;
/* 562 */       ofs += hint;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 570 */     lastOfs++;
/* 571 */     while (lastOfs < ofs) {
/* 572 */       int m = lastOfs + (ofs - lastOfs >>> 1);
/*     */       
/* 574 */       if (c.compare(key, a[base + m]) < 0) {
/* 575 */         ofs = m; continue;
/*     */       } 
/* 577 */       lastOfs = m + 1;
/*     */     } 
/*     */     
/* 580 */     return ofs;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeLo(int base1, int len1, int base2, int len2) {
/* 598 */     T[] a = this.a;
/* 599 */     T[] tmp = ensureCapacity(len1);
/* 600 */     System.arraycopy(a, base1, tmp, 0, len1);
/*     */     
/* 602 */     int cursor1 = 0;
/* 603 */     int cursor2 = base2;
/* 604 */     int dest = base1;
/*     */ 
/*     */     
/* 607 */     a[dest++] = a[cursor2++];
/* 608 */     if (--len2 == 0) {
/* 609 */       System.arraycopy(tmp, cursor1, a, dest, len1);
/*     */       return;
/*     */     } 
/* 612 */     if (len1 == 1) {
/* 613 */       System.arraycopy(a, cursor2, a, dest, len2);
/* 614 */       a[dest + len2] = tmp[cursor1];
/*     */       
/*     */       return;
/*     */     } 
/* 618 */     Comparator<? super T> c = this.c;
/* 619 */     int minGallop = this.minGallop;
/*     */     
/*     */     while (true) {
/* 622 */       int count1 = 0;
/* 623 */       int count2 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 630 */         if (c.compare(a[cursor2], tmp[cursor1]) < 0)
/* 631 */         { a[dest++] = a[cursor2++];
/* 632 */           count2++;
/* 633 */           count1 = 0;
/* 634 */           if (--len2 == 0)
/*     */             break;  }
/* 636 */         else { a[dest++] = tmp[cursor1++];
/* 637 */           count1++;
/* 638 */           count2 = 0;
/* 639 */           if (--len1 == 1)
/*     */             break;  }
/* 641 */          if ((count1 | count2) >= minGallop)
/*     */         
/*     */         { 
/*     */           
/*     */           label57: while (true)
/*     */           
/*     */           { 
/*     */             
/* 649 */             count1 = gallopRight(a[cursor2], tmp, cursor1, len1, 0, c);
/* 650 */             if (count1 != 0) {
/* 651 */               System.arraycopy(tmp, cursor1, a, dest, count1);
/* 652 */               dest += count1;
/* 653 */               cursor1 += count1;
/* 654 */               len1 -= count1;
/* 655 */               if (len1 <= 1)
/*     */                 break; 
/*     */             } 
/* 658 */             a[dest++] = a[cursor2++];
/* 659 */             if (--len2 == 0)
/*     */               break; 
/* 661 */             count2 = gallopLeft(tmp[cursor1], a, cursor2, len2, 0, c);
/* 662 */             if (count2 != 0) {
/* 663 */               System.arraycopy(a, cursor2, a, dest, count2);
/* 664 */               dest += count2;
/* 665 */               cursor2 += count2;
/* 666 */               len2 -= count2;
/* 667 */               if (len2 == 0)
/*     */                 break; 
/* 669 */             }  a[dest++] = tmp[cursor1++];
/* 670 */             if (--len1 == 1)
/* 671 */               break;  minGallop--;
/* 672 */             if ((((count1 >= 7) ? 1 : 0) | ((count2 >= 7) ? 1 : 0)) == 0)
/* 673 */             { if (minGallop < 0) { minGallop = 0; break label57; }
/* 674 */                minGallop += 2; }  }  break; } 
/*     */       }  break;
/* 676 */     }  this.minGallop = (minGallop < 1) ? 1 : minGallop;
/*     */     
/* 678 */     if (len1 == 1)
/*     */     
/* 680 */     { System.arraycopy(a, cursor2, a, dest, len2);
/* 681 */       a[dest + len2] = tmp[cursor1]; }
/* 682 */     else { if (len1 == 0) {
/* 683 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/*     */       
/* 687 */       System.arraycopy(tmp, cursor1, a, dest, len1); }
/*     */   
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
/*     */   
/*     */   private void mergeHi(int base1, int len1, int base2, int len2) {
/* 702 */     T[] a = this.a;
/* 703 */     T[] tmp = ensureCapacity(len2);
/* 704 */     System.arraycopy(a, base2, tmp, 0, len2);
/*     */     
/* 706 */     int cursor1 = base1 + len1 - 1;
/* 707 */     int cursor2 = len2 - 1;
/* 708 */     int dest = base2 + len2 - 1;
/*     */ 
/*     */     
/* 711 */     a[dest--] = a[cursor1--];
/* 712 */     if (--len1 == 0) {
/* 713 */       System.arraycopy(tmp, 0, a, dest - len2 - 1, len2);
/*     */       return;
/*     */     } 
/* 716 */     if (len2 == 1) {
/* 717 */       dest -= len1;
/* 718 */       cursor1 -= len1;
/* 719 */       System.arraycopy(a, cursor1 + 1, a, dest + 1, len1);
/* 720 */       a[dest] = tmp[cursor2];
/*     */       
/*     */       return;
/*     */     } 
/* 724 */     Comparator<? super T> c = this.c;
/* 725 */     int minGallop = this.minGallop;
/*     */     
/*     */     while (true) {
/* 728 */       int count1 = 0;
/* 729 */       int count2 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 736 */         if (c.compare(tmp[cursor2], a[cursor1]) < 0)
/* 737 */         { a[dest--] = a[cursor1--];
/* 738 */           count1++;
/* 739 */           count2 = 0;
/* 740 */           if (--len1 == 0)
/*     */             break;  }
/* 742 */         else { a[dest--] = tmp[cursor2--];
/* 743 */           count2++;
/* 744 */           count1 = 0;
/* 745 */           if (--len2 == 1)
/*     */             break;  }
/* 747 */          if ((count1 | count2) >= minGallop)
/*     */         
/*     */         { 
/*     */           
/*     */           label57: while (true)
/*     */           
/*     */           { 
/*     */             
/* 755 */             count1 = len1 - gallopRight(tmp[cursor2], a, base1, len1, len1 - 1, c);
/* 756 */             if (count1 != 0) {
/* 757 */               dest -= count1;
/* 758 */               cursor1 -= count1;
/* 759 */               len1 -= count1;
/* 760 */               System.arraycopy(a, cursor1 + 1, a, dest + 1, count1);
/* 761 */               if (len1 == 0)
/*     */                 break; 
/* 763 */             }  a[dest--] = tmp[cursor2--];
/* 764 */             if (--len2 == 1)
/*     */               break; 
/* 766 */             count2 = len2 - gallopLeft(a[cursor1], tmp, 0, len2, len2 - 1, c);
/* 767 */             if (count2 != 0) {
/* 768 */               dest -= count2;
/* 769 */               cursor2 -= count2;
/* 770 */               len2 -= count2;
/* 771 */               System.arraycopy(tmp, cursor2 + 1, a, dest + 1, count2);
/* 772 */               if (len2 <= 1)
/*     */                 break; 
/*     */             } 
/* 775 */             a[dest--] = a[cursor1--];
/* 776 */             if (--len1 == 0)
/* 777 */               break;  minGallop--;
/* 778 */             if ((((count1 >= 7) ? 1 : 0) | ((count2 >= 7) ? 1 : 0)) == 0)
/* 779 */             { if (minGallop < 0) { minGallop = 0; break label57; }
/* 780 */                minGallop += 2; }  }  break; } 
/*     */       }  break;
/* 782 */     }  this.minGallop = (minGallop < 1) ? 1 : minGallop;
/*     */     
/* 784 */     if (len2 == 1)
/*     */     
/* 786 */     { dest -= len1;
/* 787 */       cursor1 -= len1;
/* 788 */       System.arraycopy(a, cursor1 + 1, a, dest + 1, len1);
/* 789 */       a[dest] = tmp[cursor2]; }
/* 790 */     else { if (len2 == 0) {
/* 791 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/*     */       
/* 795 */       System.arraycopy(tmp, 0, a, dest - len2 - 1, len2); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T[] ensureCapacity(int minCapacity) {
/* 805 */     this.tmpCount = Math.max(this.tmpCount, minCapacity);
/* 806 */     if (this.tmp.length < minCapacity) {
/*     */       
/* 808 */       int newSize = minCapacity;
/* 809 */       newSize |= newSize >> 1;
/* 810 */       newSize |= newSize >> 2;
/* 811 */       newSize |= newSize >> 4;
/* 812 */       newSize |= newSize >> 8;
/* 813 */       newSize |= newSize >> 16;
/* 814 */       newSize++;
/*     */       
/* 816 */       if (newSize < 0) {
/* 817 */         newSize = minCapacity;
/*     */       } else {
/* 819 */         newSize = Math.min(newSize, this.a.length >>> 1);
/*     */       } 
/* 821 */       T[] newArray = (T[])new Object[newSize];
/* 822 */       this.tmp = newArray;
/*     */     } 
/* 824 */     return this.tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void rangeCheck(int arrayLen, int fromIndex, int toIndex) {
/* 835 */     if (fromIndex > toIndex) throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")"); 
/* 836 */     if (fromIndex < 0) throw new ArrayIndexOutOfBoundsException(fromIndex); 
/* 837 */     if (toIndex > arrayLen) throw new ArrayIndexOutOfBoundsException(toIndex); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\TimSort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */