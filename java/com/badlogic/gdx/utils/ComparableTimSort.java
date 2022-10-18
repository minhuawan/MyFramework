/*     */ package com.badlogic.gdx.utils;
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
/*     */ class ComparableTimSort
/*     */ {
/*     */   private static final int MIN_MERGE = 32;
/*     */   private Object[] a;
/*     */   private static final int MIN_GALLOP = 7;
/*  44 */   private int minGallop = 7;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] tmp;
/*     */ 
/*     */ 
/*     */   
/*     */   private int tmpCount;
/*     */ 
/*     */ 
/*     */   
/*  62 */   private int stackSize = 0;
/*     */   
/*     */   private final int[] runBase;
/*     */   
/*     */   private final int[] runLen;
/*     */   
/*     */   private static final boolean DEBUG = false;
/*     */   
/*     */   ComparableTimSort() {
/*  71 */     this.tmp = new Object[256];
/*  72 */     this.runBase = new int[40];
/*  73 */     this.runLen = new int[40];
/*     */   }
/*     */   
/*     */   public void doSort(Object[] a, int lo, int hi) {
/*  77 */     this.stackSize = 0;
/*  78 */     rangeCheck(a.length, lo, hi);
/*  79 */     int nRemaining = hi - lo;
/*  80 */     if (nRemaining < 2) {
/*     */       return;
/*     */     }
/*  83 */     if (nRemaining < 32) {
/*  84 */       int initRunLen = countRunAndMakeAscending(a, lo, hi);
/*  85 */       binarySort(a, lo, hi, lo + initRunLen);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     this.a = a;
/*  90 */     this.tmpCount = 0;
/*     */ 
/*     */ 
/*     */     
/*  94 */     int minRun = minRunLength(nRemaining);
/*     */     
/*     */     do {
/*  97 */       int runLen = countRunAndMakeAscending(a, lo, hi);
/*     */ 
/*     */       
/* 100 */       if (runLen < minRun) {
/* 101 */         int force = (nRemaining <= minRun) ? nRemaining : minRun;
/* 102 */         binarySort(a, lo, lo + force, lo + runLen);
/* 103 */         runLen = force;
/*     */       } 
/*     */ 
/*     */       
/* 107 */       pushRun(lo, runLen);
/* 108 */       mergeCollapse();
/*     */ 
/*     */       
/* 111 */       lo += runLen;
/* 112 */       nRemaining -= runLen;
/* 113 */     } while (nRemaining != 0);
/*     */ 
/*     */ 
/*     */     
/* 117 */     mergeForceCollapse();
/*     */ 
/*     */     
/* 120 */     this.a = null;
/* 121 */     Object[] tmp = this.tmp;
/* 122 */     for (int i = 0, n = this.tmpCount; i < n; i++) {
/* 123 */       tmp[i] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ComparableTimSort(Object[] a) {
/* 130 */     this.a = a;
/*     */ 
/*     */     
/* 133 */     int len = a.length;
/* 134 */     Object[] newArray = new Object[(len < 512) ? (len >>> 1) : 256];
/* 135 */     this.tmp = newArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     int stackLen = (len < 120) ? 5 : ((len < 1542) ? 10 : ((len < 119151) ? 19 : 40));
/* 145 */     this.runBase = new int[stackLen];
/* 146 */     this.runLen = new int[stackLen];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void sort(Object[] a) {
/* 155 */     sort(a, 0, a.length);
/*     */   }
/*     */   
/*     */   static void sort(Object[] a, int lo, int hi) {
/* 159 */     rangeCheck(a.length, lo, hi);
/* 160 */     int nRemaining = hi - lo;
/* 161 */     if (nRemaining < 2) {
/*     */       return;
/*     */     }
/* 164 */     if (nRemaining < 32) {
/* 165 */       int initRunLen = countRunAndMakeAscending(a, lo, hi);
/* 166 */       binarySort(a, lo, hi, lo + initRunLen);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 172 */     ComparableTimSort ts = new ComparableTimSort(a);
/* 173 */     int minRun = minRunLength(nRemaining);
/*     */     
/*     */     do {
/* 176 */       int runLen = countRunAndMakeAscending(a, lo, hi);
/*     */ 
/*     */       
/* 179 */       if (runLen < minRun) {
/* 180 */         int force = (nRemaining <= minRun) ? nRemaining : minRun;
/* 181 */         binarySort(a, lo, lo + force, lo + runLen);
/* 182 */         runLen = force;
/*     */       } 
/*     */ 
/*     */       
/* 186 */       ts.pushRun(lo, runLen);
/* 187 */       ts.mergeCollapse();
/*     */ 
/*     */       
/* 190 */       lo += runLen;
/* 191 */       nRemaining -= runLen;
/* 192 */     } while (nRemaining != 0);
/*     */ 
/*     */ 
/*     */     
/* 196 */     ts.mergeForceCollapse();
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
/*     */   private static void binarySort(Object[] a, int lo, int hi, int start) {
/* 213 */     if (start == lo) start++; 
/* 214 */     for (; start < hi; start++) {
/*     */       
/* 216 */       Comparable<Object> pivot = (Comparable<Object>)a[start];
/*     */ 
/*     */       
/* 219 */       int left = lo;
/* 220 */       int right = start;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 225 */       while (left < right) {
/* 226 */         int mid = left + right >>> 1;
/* 227 */         if (pivot.compareTo(a[mid]) < 0) {
/* 228 */           right = mid; continue;
/*     */         } 
/* 230 */         left = mid + 1;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 239 */       int n = start - left;
/*     */       
/* 241 */       switch (n) {
/*     */         case 2:
/* 243 */           a[left + 2] = a[left + 1];
/*     */         case 1:
/* 245 */           a[left + 1] = a[left];
/*     */           break;
/*     */         default:
/* 248 */           System.arraycopy(a, left, a, left + 1, n); break;
/*     */       } 
/* 250 */       a[left] = pivot;
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
/*     */   private static int countRunAndMakeAscending(Object[] a, int lo, int hi) {
/* 275 */     int runHi = lo + 1;
/* 276 */     if (runHi == hi) return 1;
/*     */ 
/*     */     
/* 279 */     if (((Comparable<Object>)a[runHi++]).compareTo(a[lo]) < 0) {
/* 280 */       while (runHi < hi && ((Comparable<Object>)a[runHi]).compareTo(a[runHi - 1]) < 0)
/* 281 */         runHi++; 
/* 282 */       reverseRange(a, lo, runHi);
/*     */     } else {
/* 284 */       while (runHi < hi && ((Comparable<Object>)a[runHi]).compareTo(a[runHi - 1]) >= 0) {
/* 285 */         runHi++;
/*     */       }
/*     */     } 
/* 288 */     return runHi - lo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reverseRange(Object[] a, int lo, int hi) {
/* 297 */     hi--;
/* 298 */     while (lo < hi) {
/* 299 */       Object t = a[lo];
/* 300 */       a[lo++] = a[hi];
/* 301 */       a[hi--] = t;
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
/* 320 */     int r = 0;
/* 321 */     while (n >= 32) {
/* 322 */       r |= n & 0x1;
/* 323 */       n >>= 1;
/*     */     } 
/* 325 */     return n + r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushRun(int runBase, int runLen) {
/* 333 */     this.runBase[this.stackSize] = runBase;
/* 334 */     this.runLen[this.stackSize] = runLen;
/* 335 */     this.stackSize++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeCollapse() {
/* 345 */     while (this.stackSize > 1) {
/* 346 */       int n = this.stackSize - 2;
/* 347 */       if (n > 0 && this.runLen[n - 1] <= this.runLen[n] + this.runLen[n + 1]) {
/* 348 */         if (this.runLen[n - 1] < this.runLen[n + 1]) n--; 
/* 349 */         mergeAt(n); continue;
/* 350 */       }  if (this.runLen[n] <= this.runLen[n + 1]) {
/* 351 */         mergeAt(n);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeForceCollapse() {
/* 360 */     while (this.stackSize > 1) {
/* 361 */       int n = this.stackSize - 2;
/* 362 */       if (n > 0 && this.runLen[n - 1] < this.runLen[n + 1]) n--; 
/* 363 */       mergeAt(n);
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
/*     */   private void mergeAt(int i) {
/* 377 */     int base1 = this.runBase[i];
/* 378 */     int len1 = this.runLen[i];
/* 379 */     int base2 = this.runBase[i + 1];
/* 380 */     int len2 = this.runLen[i + 1];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 388 */     this.runLen[i] = len1 + len2;
/* 389 */     if (i == this.stackSize - 3) {
/* 390 */       this.runBase[i + 1] = this.runBase[i + 2];
/* 391 */       this.runLen[i + 1] = this.runLen[i + 2];
/*     */     } 
/* 393 */     this.stackSize--;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 399 */     int k = gallopRight((Comparable<Object>)this.a[base2], this.a, base1, len1, 0);
/*     */     
/* 401 */     base1 += k;
/* 402 */     len1 -= k;
/* 403 */     if (len1 == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 409 */     len2 = gallopLeft((Comparable<Object>)this.a[base1 + len1 - 1], this.a, base2, len2, len2 - 1);
/*     */     
/* 411 */     if (len2 == 0) {
/*     */       return;
/*     */     }
/* 414 */     if (len1 <= len2) {
/* 415 */       mergeLo(base1, len1, base2, len2);
/*     */     } else {
/* 417 */       mergeHi(base1, len1, base2, len2);
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
/*     */   private static int gallopLeft(Comparable<Object> key, Object[] a, int base, int len, int hint) {
/* 435 */     int lastOfs = 0;
/* 436 */     int ofs = 1;
/* 437 */     if (key.compareTo(a[base + hint]) > 0) {
/*     */       
/* 439 */       int maxOfs = len - hint;
/* 440 */       while (ofs < maxOfs && key.compareTo(a[base + hint + ofs]) > 0) {
/* 441 */         lastOfs = ofs;
/* 442 */         ofs = (ofs << 1) + 1;
/* 443 */         if (ofs <= 0)
/* 444 */           ofs = maxOfs; 
/*     */       } 
/* 446 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 449 */       lastOfs += hint;
/* 450 */       ofs += hint;
/*     */     } else {
/*     */       
/* 453 */       int maxOfs = hint + 1;
/* 454 */       while (ofs < maxOfs && key.compareTo(a[base + hint - ofs]) <= 0) {
/* 455 */         lastOfs = ofs;
/* 456 */         ofs = (ofs << 1) + 1;
/* 457 */         if (ofs <= 0)
/* 458 */           ofs = maxOfs; 
/*     */       } 
/* 460 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 463 */       int tmp = lastOfs;
/* 464 */       lastOfs = hint - ofs;
/* 465 */       ofs = hint - tmp;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 473 */     lastOfs++;
/* 474 */     while (lastOfs < ofs) {
/* 475 */       int m = lastOfs + (ofs - lastOfs >>> 1);
/*     */       
/* 477 */       if (key.compareTo(a[base + m]) > 0) {
/* 478 */         lastOfs = m + 1; continue;
/*     */       } 
/* 480 */       ofs = m;
/*     */     } 
/*     */     
/* 483 */     return ofs;
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
/*     */   private static int gallopRight(Comparable<Object> key, Object[] a, int base, int len, int hint) {
/* 499 */     int ofs = 1;
/* 500 */     int lastOfs = 0;
/* 501 */     if (key.compareTo(a[base + hint]) < 0) {
/*     */       
/* 503 */       int maxOfs = hint + 1;
/* 504 */       while (ofs < maxOfs && key.compareTo(a[base + hint - ofs]) < 0) {
/* 505 */         lastOfs = ofs;
/* 506 */         ofs = (ofs << 1) + 1;
/* 507 */         if (ofs <= 0)
/* 508 */           ofs = maxOfs; 
/*     */       } 
/* 510 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 513 */       int tmp = lastOfs;
/* 514 */       lastOfs = hint - ofs;
/* 515 */       ofs = hint - tmp;
/*     */     } else {
/*     */       
/* 518 */       int maxOfs = len - hint;
/* 519 */       while (ofs < maxOfs && key.compareTo(a[base + hint + ofs]) >= 0) {
/* 520 */         lastOfs = ofs;
/* 521 */         ofs = (ofs << 1) + 1;
/* 522 */         if (ofs <= 0)
/* 523 */           ofs = maxOfs; 
/*     */       } 
/* 525 */       if (ofs > maxOfs) ofs = maxOfs;
/*     */ 
/*     */       
/* 528 */       lastOfs += hint;
/* 529 */       ofs += hint;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 537 */     lastOfs++;
/* 538 */     while (lastOfs < ofs) {
/* 539 */       int m = lastOfs + (ofs - lastOfs >>> 1);
/*     */       
/* 541 */       if (key.compareTo(a[base + m]) < 0) {
/* 542 */         ofs = m; continue;
/*     */       } 
/* 544 */       lastOfs = m + 1;
/*     */     } 
/*     */     
/* 547 */     return ofs;
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
/*     */   private void mergeLo(int base1, int len1, int base2, int len2) {
/* 566 */     Object[] a = this.a;
/* 567 */     Object[] tmp = ensureCapacity(len1);
/* 568 */     System.arraycopy(a, base1, tmp, 0, len1);
/*     */     
/* 570 */     int cursor1 = 0;
/* 571 */     int cursor2 = base2;
/* 572 */     int dest = base1;
/*     */ 
/*     */     
/* 575 */     a[dest++] = a[cursor2++];
/* 576 */     if (--len2 == 0) {
/* 577 */       System.arraycopy(tmp, cursor1, a, dest, len1);
/*     */       return;
/*     */     } 
/* 580 */     if (len1 == 1) {
/* 581 */       System.arraycopy(a, cursor2, a, dest, len2);
/* 582 */       a[dest + len2] = tmp[cursor1];
/*     */       
/*     */       return;
/*     */     } 
/* 586 */     int minGallop = this.minGallop;
/*     */     
/*     */     while (true) {
/* 589 */       int count1 = 0;
/* 590 */       int count2 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 597 */         if (((Comparable<Object>)a[cursor2]).compareTo(tmp[cursor1]) < 0)
/* 598 */         { a[dest++] = a[cursor2++];
/* 599 */           count2++;
/* 600 */           count1 = 0;
/* 601 */           if (--len2 == 0)
/*     */             break;  }
/* 603 */         else { a[dest++] = tmp[cursor1++];
/* 604 */           count1++;
/* 605 */           count2 = 0;
/* 606 */           if (--len1 == 1)
/*     */             break;  }
/* 608 */          if ((count1 | count2) >= minGallop)
/*     */         
/*     */         { 
/*     */           
/*     */           label57: while (true)
/*     */           
/*     */           { 
/*     */             
/* 616 */             count1 = gallopRight((Comparable<Object>)a[cursor2], tmp, cursor1, len1, 0);
/* 617 */             if (count1 != 0) {
/* 618 */               System.arraycopy(tmp, cursor1, a, dest, count1);
/* 619 */               dest += count1;
/* 620 */               cursor1 += count1;
/* 621 */               len1 -= count1;
/* 622 */               if (len1 <= 1)
/*     */                 break; 
/*     */             } 
/* 625 */             a[dest++] = a[cursor2++];
/* 626 */             if (--len2 == 0)
/*     */               break; 
/* 628 */             count2 = gallopLeft((Comparable<Object>)tmp[cursor1], a, cursor2, len2, 0);
/* 629 */             if (count2 != 0) {
/* 630 */               System.arraycopy(a, cursor2, a, dest, count2);
/* 631 */               dest += count2;
/* 632 */               cursor2 += count2;
/* 633 */               len2 -= count2;
/* 634 */               if (len2 == 0)
/*     */                 break; 
/* 636 */             }  a[dest++] = tmp[cursor1++];
/* 637 */             if (--len1 == 1)
/* 638 */               break;  minGallop--;
/* 639 */             if ((((count1 >= 7) ? 1 : 0) | ((count2 >= 7) ? 1 : 0)) == 0)
/* 640 */             { if (minGallop < 0) { minGallop = 0; break label57; }
/* 641 */                minGallop += 2; }  }  break; } 
/*     */       }  break;
/* 643 */     }  this.minGallop = (minGallop < 1) ? 1 : minGallop;
/*     */     
/* 645 */     if (len1 == 1)
/*     */     
/* 647 */     { System.arraycopy(a, cursor2, a, dest, len2);
/* 648 */       a[dest + len2] = tmp[cursor1]; }
/* 649 */     else { if (len1 == 0) {
/* 650 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/*     */       
/* 654 */       System.arraycopy(tmp, cursor1, a, dest, len1); }
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
/*     */   
/*     */   private void mergeHi(int base1, int len1, int base2, int len2) {
/* 670 */     Object[] a = this.a;
/* 671 */     Object[] tmp = ensureCapacity(len2);
/* 672 */     System.arraycopy(a, base2, tmp, 0, len2);
/*     */     
/* 674 */     int cursor1 = base1 + len1 - 1;
/* 675 */     int cursor2 = len2 - 1;
/* 676 */     int dest = base2 + len2 - 1;
/*     */ 
/*     */     
/* 679 */     a[dest--] = a[cursor1--];
/* 680 */     if (--len1 == 0) {
/* 681 */       System.arraycopy(tmp, 0, a, dest - len2 - 1, len2);
/*     */       return;
/*     */     } 
/* 684 */     if (len2 == 1) {
/* 685 */       dest -= len1;
/* 686 */       cursor1 -= len1;
/* 687 */       System.arraycopy(a, cursor1 + 1, a, dest + 1, len1);
/* 688 */       a[dest] = tmp[cursor2];
/*     */       
/*     */       return;
/*     */     } 
/* 692 */     int minGallop = this.minGallop;
/*     */     
/*     */     while (true) {
/* 695 */       int count1 = 0;
/* 696 */       int count2 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 703 */         if (((Comparable<Object>)tmp[cursor2]).compareTo(a[cursor1]) < 0)
/* 704 */         { a[dest--] = a[cursor1--];
/* 705 */           count1++;
/* 706 */           count2 = 0;
/* 707 */           if (--len1 == 0)
/*     */             break;  }
/* 709 */         else { a[dest--] = tmp[cursor2--];
/* 710 */           count2++;
/* 711 */           count1 = 0;
/* 712 */           if (--len2 == 1)
/*     */             break;  }
/* 714 */          if ((count1 | count2) >= minGallop)
/*     */         
/*     */         { 
/*     */           
/*     */           label57: while (true)
/*     */           
/*     */           { 
/*     */             
/* 722 */             count1 = len1 - gallopRight((Comparable<Object>)tmp[cursor2], a, base1, len1, len1 - 1);
/* 723 */             if (count1 != 0) {
/* 724 */               dest -= count1;
/* 725 */               cursor1 -= count1;
/* 726 */               len1 -= count1;
/* 727 */               System.arraycopy(a, cursor1 + 1, a, dest + 1, count1);
/* 728 */               if (len1 == 0)
/*     */                 break; 
/* 730 */             }  a[dest--] = tmp[cursor2--];
/* 731 */             if (--len2 == 1)
/*     */               break; 
/* 733 */             count2 = len2 - gallopLeft((Comparable<Object>)a[cursor1], tmp, 0, len2, len2 - 1);
/* 734 */             if (count2 != 0) {
/* 735 */               dest -= count2;
/* 736 */               cursor2 -= count2;
/* 737 */               len2 -= count2;
/* 738 */               System.arraycopy(tmp, cursor2 + 1, a, dest + 1, count2);
/* 739 */               if (len2 <= 1)
/*     */                 break; 
/* 741 */             }  a[dest--] = a[cursor1--];
/* 742 */             if (--len1 == 0)
/* 743 */               break;  minGallop--;
/* 744 */             if ((((count1 >= 7) ? 1 : 0) | ((count2 >= 7) ? 1 : 0)) == 0)
/* 745 */             { if (minGallop < 0) { minGallop = 0; break label57; }
/* 746 */                minGallop += 2; }  }  break; } 
/*     */       }  break;
/* 748 */     }  this.minGallop = (minGallop < 1) ? 1 : minGallop;
/*     */     
/* 750 */     if (len2 == 1)
/*     */     
/* 752 */     { dest -= len1;
/* 753 */       cursor1 -= len1;
/* 754 */       System.arraycopy(a, cursor1 + 1, a, dest + 1, len1);
/* 755 */       a[dest] = tmp[cursor2]; }
/* 756 */     else { if (len2 == 0) {
/* 757 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/*     */       
/* 761 */       System.arraycopy(tmp, 0, a, dest - len2 - 1, len2); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] ensureCapacity(int minCapacity) {
/* 771 */     this.tmpCount = Math.max(this.tmpCount, minCapacity);
/* 772 */     if (this.tmp.length < minCapacity) {
/*     */       
/* 774 */       int newSize = minCapacity;
/* 775 */       newSize |= newSize >> 1;
/* 776 */       newSize |= newSize >> 2;
/* 777 */       newSize |= newSize >> 4;
/* 778 */       newSize |= newSize >> 8;
/* 779 */       newSize |= newSize >> 16;
/* 780 */       newSize++;
/*     */       
/* 782 */       if (newSize < 0) {
/* 783 */         newSize = minCapacity;
/*     */       } else {
/* 785 */         newSize = Math.min(newSize, this.a.length >>> 1);
/*     */       } 
/* 787 */       Object[] newArray = new Object[newSize];
/* 788 */       this.tmp = newArray;
/*     */     } 
/* 790 */     return this.tmp;
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
/* 801 */     if (fromIndex > toIndex) throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")"); 
/* 802 */     if (fromIndex < 0) throw new ArrayIndexOutOfBoundsException(fromIndex); 
/* 803 */     if (toIndex > arrayLen) throw new ArrayIndexOutOfBoundsException(toIndex); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ComparableTimSort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */