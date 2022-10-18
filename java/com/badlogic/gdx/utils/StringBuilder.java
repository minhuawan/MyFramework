/*      */ package com.badlogic.gdx.utils;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StringBuilder
/*      */   implements Appendable, CharSequence
/*      */ {
/*      */   static final int INITIAL_CAPACITY = 16;
/*      */   public char[] chars;
/*      */   public int length;
/*   33 */   private static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
/*      */ 
/*      */   
/*      */   public static int numChars(int value, int radix) {
/*   37 */     int result = (value < 0) ? 2 : 1;
/*   38 */     while ((value /= radix) != 0)
/*   39 */       result++; 
/*   40 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int numChars(long value, int radix) {
/*   45 */     int result = (value < 0L) ? 2 : 1;
/*   46 */     while ((value /= radix) != 0L)
/*   47 */       result++; 
/*   48 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final char[] getValue() {
/*   55 */     return this.chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder() {
/*   62 */     this.chars = new char[16];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder(int capacity) {
/*   71 */     if (capacity < 0) {
/*   72 */       throw new NegativeArraySizeException();
/*      */     }
/*   74 */     this.chars = new char[capacity];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder(CharSequence seq) {
/*   83 */     this(seq.toString());
/*      */   }
/*      */   
/*      */   public StringBuilder(StringBuilder builder) {
/*   87 */     this.length = builder.length;
/*   88 */     this.chars = new char[this.length + 16];
/*   89 */     System.arraycopy(builder.chars, 0, this.chars, 0, this.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder(String string) {
/*   98 */     this.length = string.length();
/*   99 */     this.chars = new char[this.length + 16];
/*  100 */     string.getChars(0, this.length, this.chars, 0);
/*      */   }
/*      */   
/*      */   private void enlargeBuffer(int min) {
/*  104 */     int newSize = (this.chars.length >> 1) + this.chars.length + 2;
/*  105 */     char[] newData = new char[(min > newSize) ? min : newSize];
/*  106 */     System.arraycopy(this.chars, 0, newData, 0, this.length);
/*  107 */     this.chars = newData;
/*      */   }
/*      */   
/*      */   final void appendNull() {
/*  111 */     int newSize = this.length + 4;
/*  112 */     if (newSize > this.chars.length) {
/*  113 */       enlargeBuffer(newSize);
/*      */     }
/*  115 */     this.chars[this.length++] = 'n';
/*  116 */     this.chars[this.length++] = 'u';
/*  117 */     this.chars[this.length++] = 'l';
/*  118 */     this.chars[this.length++] = 'l';
/*      */   }
/*      */   
/*      */   final void append0(char[] value) {
/*  122 */     int newSize = this.length + value.length;
/*  123 */     if (newSize > this.chars.length) {
/*  124 */       enlargeBuffer(newSize);
/*      */     }
/*  126 */     System.arraycopy(value, 0, this.chars, this.length, value.length);
/*  127 */     this.length = newSize;
/*      */   }
/*      */ 
/*      */   
/*      */   final void append0(char[] value, int offset, int length) {
/*  132 */     if (offset > value.length || offset < 0) {
/*  133 */       throw new ArrayIndexOutOfBoundsException("Offset out of bounds: " + offset);
/*      */     }
/*  135 */     if (length < 0 || value.length - offset < length) {
/*  136 */       throw new ArrayIndexOutOfBoundsException("Length out of bounds: " + length);
/*      */     }
/*      */     
/*  139 */     int newSize = this.length + length;
/*  140 */     if (newSize > this.chars.length) {
/*  141 */       enlargeBuffer(newSize);
/*      */     }
/*  143 */     System.arraycopy(value, offset, this.chars, this.length, length);
/*  144 */     this.length = newSize;
/*      */   }
/*      */   
/*      */   final void append0(char ch) {
/*  148 */     if (this.length == this.chars.length) {
/*  149 */       enlargeBuffer(this.length + 1);
/*      */     }
/*  151 */     this.chars[this.length++] = ch;
/*      */   }
/*      */   
/*      */   final void append0(String string) {
/*  155 */     if (string == null) {
/*  156 */       appendNull();
/*      */       return;
/*      */     } 
/*  159 */     int adding = string.length();
/*  160 */     int newSize = this.length + adding;
/*  161 */     if (newSize > this.chars.length) {
/*  162 */       enlargeBuffer(newSize);
/*      */     }
/*  164 */     string.getChars(0, adding, this.chars, this.length);
/*  165 */     this.length = newSize;
/*      */   }
/*      */   
/*      */   final void append0(CharSequence s, int start, int end) {
/*  169 */     if (s == null) {
/*  170 */       s = "null";
/*      */     }
/*  172 */     if (start < 0 || end < 0 || start > end || end > s.length()) {
/*  173 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/*  176 */     append0(s.subSequence(start, end).toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int capacity() {
/*  185 */     return this.chars.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char charAt(int index) {
/*  194 */     if (index < 0 || index >= this.length) {
/*  195 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  197 */     return this.chars[index];
/*      */   }
/*      */   
/*      */   final void delete0(int start, int end) {
/*  201 */     if (start >= 0) {
/*  202 */       if (end > this.length) {
/*  203 */         end = this.length;
/*      */       }
/*  205 */       if (end == start) {
/*      */         return;
/*      */       }
/*  208 */       if (end > start) {
/*  209 */         int count = this.length - end;
/*  210 */         if (count >= 0) System.arraycopy(this.chars, end, this.chars, start, count); 
/*  211 */         this.length -= end - start;
/*      */         return;
/*      */       } 
/*      */     } 
/*  215 */     throw new StringIndexOutOfBoundsException();
/*      */   }
/*      */   
/*      */   final void deleteCharAt0(int location) {
/*  219 */     if (0 > location || location >= this.length) {
/*  220 */       throw new StringIndexOutOfBoundsException(location);
/*      */     }
/*  222 */     int count = this.length - location - 1;
/*  223 */     if (count > 0) {
/*  224 */       System.arraycopy(this.chars, location + 1, this.chars, location, count);
/*      */     }
/*  226 */     this.length--;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ensureCapacity(int min) {
/*  236 */     if (min > this.chars.length) {
/*  237 */       int twice = (this.chars.length << 1) + 2;
/*  238 */       enlargeBuffer((twice > min) ? twice : min);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getChars(int start, int end, char[] dest, int destStart) {
/*  252 */     if (start > this.length || end > this.length || start > end) {
/*  253 */       throw new StringIndexOutOfBoundsException();
/*      */     }
/*  255 */     System.arraycopy(this.chars, start, dest, destStart, end - start);
/*      */   }
/*      */   
/*      */   final void insert0(int index, char[] value) {
/*  259 */     if (0 > index || index > this.length) {
/*  260 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  262 */     if (value.length != 0) {
/*  263 */       move(value.length, index);
/*  264 */       System.arraycopy(value, 0, value, index, value.length);
/*  265 */       this.length += value.length;
/*      */     } 
/*      */   }
/*      */   
/*      */   final void insert0(int index, char[] value, int start, int length) {
/*  270 */     if (0 <= index && index <= length) {
/*      */       
/*  272 */       if (start >= 0 && 0 <= length && length <= value.length - start) {
/*  273 */         if (length != 0) {
/*  274 */           move(length, index);
/*  275 */           System.arraycopy(value, start, this.chars, index, length);
/*  276 */           this.length += length;
/*      */         } 
/*      */         return;
/*      */       } 
/*  280 */       throw new StringIndexOutOfBoundsException("offset " + start + ", length " + length + ", char[].length " + value.length);
/*      */     } 
/*  282 */     throw new StringIndexOutOfBoundsException(index);
/*      */   }
/*      */   
/*      */   final void insert0(int index, char ch) {
/*  286 */     if (0 > index || index > this.length)
/*      */     {
/*  288 */       throw new ArrayIndexOutOfBoundsException(index);
/*      */     }
/*  290 */     move(1, index);
/*  291 */     this.chars[index] = ch;
/*  292 */     this.length++;
/*      */   }
/*      */   
/*      */   final void insert0(int index, String string) {
/*  296 */     if (0 <= index && index <= this.length) {
/*  297 */       if (string == null) {
/*  298 */         string = "null";
/*      */       }
/*  300 */       int min = string.length();
/*  301 */       if (min != 0) {
/*  302 */         move(min, index);
/*  303 */         string.getChars(0, min, this.chars, index);
/*  304 */         this.length += min;
/*      */       } 
/*      */     } else {
/*  307 */       throw new StringIndexOutOfBoundsException(index);
/*      */     } 
/*      */   }
/*      */   
/*      */   final void insert0(int index, CharSequence s, int start, int end) {
/*  312 */     if (s == null) {
/*  313 */       s = "null";
/*      */     }
/*  315 */     if (index < 0 || index > this.length || start < 0 || end < 0 || start > end || end > s.length()) {
/*  316 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  318 */     insert0(index, s.subSequence(start, end).toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  325 */     return this.length;
/*      */   }
/*      */   
/*      */   private void move(int size, int index) {
/*  329 */     if (this.chars.length - this.length >= size) {
/*  330 */       System.arraycopy(this.chars, index, this.chars, index + size, this.length - index);
/*      */       return;
/*      */     } 
/*  333 */     int a = this.length + size, b = (this.chars.length << 1) + 2;
/*  334 */     int newSize = (a > b) ? a : b;
/*  335 */     char[] newData = new char[newSize];
/*  336 */     System.arraycopy(this.chars, 0, newData, 0, index);
/*      */     
/*  338 */     System.arraycopy(this.chars, index, newData, index + size, this.length - index);
/*  339 */     this.chars = newData;
/*      */   }
/*      */   
/*      */   final void replace0(int start, int end, String string) {
/*  343 */     if (start >= 0) {
/*  344 */       if (end > this.length) {
/*  345 */         end = this.length;
/*      */       }
/*  347 */       if (end > start) {
/*  348 */         int stringLength = string.length();
/*  349 */         int diff = end - start - stringLength;
/*  350 */         if (diff > 0) {
/*      */           
/*  352 */           System.arraycopy(this.chars, end, this.chars, start + stringLength, this.length - end);
/*  353 */         } else if (diff < 0) {
/*      */           
/*  355 */           move(-diff, end);
/*      */         } 
/*  357 */         string.getChars(0, stringLength, this.chars, start);
/*  358 */         this.length -= diff;
/*      */         return;
/*      */       } 
/*  361 */       if (start == end) {
/*  362 */         if (string == null) {
/*  363 */           throw new NullPointerException();
/*      */         }
/*  365 */         insert0(start, string);
/*      */         return;
/*      */       } 
/*      */     } 
/*  369 */     throw new StringIndexOutOfBoundsException();
/*      */   }
/*      */   
/*      */   final void reverse0() {
/*  373 */     if (this.length < 2) {
/*      */       return;
/*      */     }
/*  376 */     int end = this.length - 1;
/*  377 */     char frontHigh = this.chars[0];
/*  378 */     char endLow = this.chars[end];
/*  379 */     boolean allowFrontSur = true, allowEndSur = true;
/*  380 */     for (int i = 0, mid = this.length / 2; i < mid; i++, end--) {
/*  381 */       char frontLow = this.chars[i + 1];
/*  382 */       char endHigh = this.chars[end - 1];
/*  383 */       boolean surAtFront = (allowFrontSur && frontLow >= '?' && frontLow <= '?' && frontHigh >= '?' && frontHigh <= '?');
/*      */       
/*  385 */       if (surAtFront && this.length < 3) {
/*      */         return;
/*      */       }
/*  388 */       boolean surAtEnd = (allowEndSur && endHigh >= '?' && endHigh <= '?' && endLow >= '?' && endLow <= '?');
/*  389 */       allowFrontSur = allowEndSur = true;
/*  390 */       if (surAtFront == surAtEnd) {
/*  391 */         if (surAtFront) {
/*      */           
/*  393 */           this.chars[end] = frontLow;
/*  394 */           this.chars[end - 1] = frontHigh;
/*  395 */           this.chars[i] = endHigh;
/*  396 */           this.chars[i + 1] = endLow;
/*  397 */           frontHigh = this.chars[i + 2];
/*  398 */           endLow = this.chars[end - 2];
/*  399 */           i++;
/*  400 */           end--;
/*      */         } else {
/*      */           
/*  403 */           this.chars[end] = frontHigh;
/*  404 */           this.chars[i] = endLow;
/*  405 */           frontHigh = frontLow;
/*  406 */           endLow = endHigh;
/*      */         }
/*      */       
/*  409 */       } else if (surAtFront) {
/*      */         
/*  411 */         this.chars[end] = frontLow;
/*  412 */         this.chars[i] = endLow;
/*  413 */         endLow = endHigh;
/*  414 */         allowFrontSur = false;
/*      */       } else {
/*      */         
/*  417 */         this.chars[end] = frontHigh;
/*  418 */         this.chars[i] = endHigh;
/*  419 */         frontHigh = frontLow;
/*  420 */         allowEndSur = false;
/*      */       } 
/*      */     } 
/*      */     
/*  424 */     if ((this.length & 0x1) == 1 && (!allowFrontSur || !allowEndSur)) {
/*  425 */       this.chars[end] = allowFrontSur ? endLow : frontHigh;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharAt(int index, char ch) {
/*  435 */     if (0 > index || index >= this.length) {
/*  436 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  438 */     this.chars[index] = ch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLength(int newLength) {
/*  448 */     if (newLength < 0) {
/*  449 */       throw new StringIndexOutOfBoundsException(newLength);
/*      */     }
/*  451 */     if (newLength > this.chars.length) {
/*  452 */       enlargeBuffer(newLength);
/*      */     }
/*  454 */     else if (this.length < newLength) {
/*  455 */       Arrays.fill(this.chars, this.length, newLength, false);
/*      */     } 
/*      */     
/*  458 */     this.length = newLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String substring(int start) {
/*  467 */     if (0 <= start && start <= this.length) {
/*  468 */       if (start == this.length) {
/*  469 */         return "";
/*      */       }
/*      */ 
/*      */       
/*  473 */       return new String(this.chars, start, this.length - start);
/*      */     } 
/*  475 */     throw new StringIndexOutOfBoundsException(start);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String substring(int start, int end) {
/*  486 */     if (0 <= start && start <= end && end <= this.length) {
/*  487 */       if (start == end) {
/*  488 */         return "";
/*      */       }
/*      */ 
/*      */       
/*  492 */       return new String(this.chars, start, end - start);
/*      */     } 
/*  494 */     throw new StringIndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  502 */     if (this.length == 0) return ""; 
/*  503 */     return new String(this.chars, 0, this.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharSequence subSequence(int start, int end) {
/*  515 */     return substring(start, end);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(String string) {
/*  526 */     return indexOf(string, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(String subString, int start) {
/*  538 */     if (start < 0) {
/*  539 */       start = 0;
/*      */     }
/*  541 */     int subCount = subString.length();
/*  542 */     if (subCount > 0) {
/*  543 */       if (subCount + start > this.length) {
/*  544 */         return -1;
/*      */       }
/*  546 */       char firstChar = subString.charAt(0);
/*      */       while (true) {
/*  548 */         int i = start;
/*  549 */         boolean found = false;
/*  550 */         for (; i < this.length; i++) {
/*  551 */           if (this.chars[i] == firstChar) {
/*  552 */             found = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*  556 */         if (!found || subCount + i > this.length) {
/*  557 */           return -1;
/*      */         }
/*  559 */         int o1 = i, o2 = 0;
/*  560 */         while (++o2 < subCount && this.chars[++o1] == subString.charAt(o2));
/*      */ 
/*      */         
/*  563 */         if (o2 == subCount) {
/*  564 */           return i;
/*      */         }
/*  566 */         start = i + 1;
/*      */       } 
/*      */     } 
/*  569 */     return (start < this.length || start == 0) ? start : this.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(String string) {
/*  581 */     return lastIndexOf(string, this.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(String subString, int start) {
/*  594 */     int subCount = subString.length();
/*  595 */     if (subCount <= this.length && start >= 0) {
/*  596 */       if (subCount > 0) {
/*  597 */         if (start > this.length - subCount) {
/*  598 */           start = this.length - subCount;
/*      */         }
/*      */         
/*  601 */         char firstChar = subString.charAt(0);
/*      */         while (true) {
/*  603 */           int i = start;
/*  604 */           boolean found = false;
/*  605 */           for (; i >= 0; i--) {
/*  606 */             if (this.chars[i] == firstChar) {
/*  607 */               found = true;
/*      */               break;
/*      */             } 
/*      */           } 
/*  611 */           if (!found) {
/*  612 */             return -1;
/*      */           }
/*  614 */           int o1 = i, o2 = 0;
/*  615 */           while (++o2 < subCount && this.chars[++o1] == subString.charAt(o2));
/*      */ 
/*      */           
/*  618 */           if (o2 == subCount) {
/*  619 */             return i;
/*      */           }
/*  621 */           start = i - 1;
/*      */         } 
/*      */       } 
/*  624 */       return (start < this.length) ? start : this.length;
/*      */     } 
/*  626 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trimToSize() {
/*  634 */     if (this.length < this.chars.length) {
/*  635 */       char[] newValue = new char[this.length];
/*  636 */       System.arraycopy(this.chars, 0, newValue, 0, this.length);
/*  637 */       this.chars = newValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int codePointAt(int index) {
/*  650 */     if (index < 0 || index >= this.length) {
/*  651 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  653 */     return Character.codePointAt(this.chars, index, this.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int codePointBefore(int index) {
/*  665 */     if (index < 1 || index > this.length) {
/*  666 */       throw new StringIndexOutOfBoundsException(index);
/*      */     }
/*  668 */     return Character.codePointBefore(this.chars, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int codePointCount(int beginIndex, int endIndex) {
/*  682 */     if (beginIndex < 0 || endIndex > this.length || beginIndex > endIndex) {
/*  683 */       throw new StringIndexOutOfBoundsException();
/*      */     }
/*  685 */     return Character.codePointCount(this.chars, beginIndex, endIndex - beginIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int offsetByCodePoints(int index, int codePointOffset) {
/*  699 */     return Character.offsetByCodePoints(this.chars, 0, this.length, index, codePointOffset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(boolean b) {
/*  709 */     append0(b ? "true" : "false");
/*  710 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(char c) {
/*  720 */     append0(c);
/*  721 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(int value) {
/*  731 */     return append(value, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(int value, int minLength) {
/*  742 */     return append(value, minLength, '0');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(int value, int minLength, char prefix) {
/*  754 */     if (value == Integer.MIN_VALUE) {
/*  755 */       append0("-2147483648");
/*  756 */       return this;
/*      */     } 
/*  758 */     if (value < 0) {
/*  759 */       append0('-');
/*  760 */       value = -value;
/*      */     } 
/*  762 */     if (minLength > 1)
/*  763 */       for (int j = minLength - numChars(value, 10); j > 0; j--) {
/*  764 */         append(prefix);
/*      */       } 
/*  766 */     if (value >= 10000) {
/*  767 */       if (value >= 1000000000) append0(digits[(int)(value % 10000000000L / 1000000000L)]); 
/*  768 */       if (value >= 100000000) append0(digits[value % 1000000000 / 100000000]); 
/*  769 */       if (value >= 10000000) append0(digits[value % 100000000 / 10000000]); 
/*  770 */       if (value >= 1000000) append0(digits[value % 10000000 / 1000000]); 
/*  771 */       if (value >= 100000) append0(digits[value % 1000000 / 100000]); 
/*  772 */       append0(digits[value % 100000 / 10000]);
/*      */     } 
/*  774 */     if (value >= 1000) append0(digits[value % 10000 / 1000]); 
/*  775 */     if (value >= 100) append0(digits[value % 1000 / 100]); 
/*  776 */     if (value >= 10) append0(digits[value % 100 / 10]); 
/*  777 */     append0(digits[value % 10]);
/*  778 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(long value) {
/*  787 */     return append(value, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(long value, int minLength) {
/*  797 */     return append(value, minLength, '0');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(long value, int minLength, char prefix) {
/*  808 */     if (value == Long.MIN_VALUE) {
/*  809 */       append0("-9223372036854775808");
/*  810 */       return this;
/*      */     } 
/*  812 */     if (value < 0L) {
/*  813 */       append0('-');
/*  814 */       value = -value;
/*      */     } 
/*  816 */     if (minLength > 1)
/*  817 */       for (int j = minLength - numChars(value, 10); j > 0; j--) {
/*  818 */         append(prefix);
/*      */       } 
/*  820 */     if (value >= 10000L) {
/*  821 */       if (value >= 1000000000000000000L) append0(digits[(int)(value % 1.0E19D / 1.0E18D)]); 
/*  822 */       if (value >= 100000000000000000L) append0(digits[(int)(value % 1000000000000000000L / 100000000000000000L)]); 
/*  823 */       if (value >= 10000000000000000L) append0(digits[(int)(value % 100000000000000000L / 10000000000000000L)]); 
/*  824 */       if (value >= 1000000000000000L) append0(digits[(int)(value % 10000000000000000L / 1000000000000000L)]); 
/*  825 */       if (value >= 100000000000000L) append0(digits[(int)(value % 1000000000000000L / 100000000000000L)]); 
/*  826 */       if (value >= 10000000000000L) append0(digits[(int)(value % 100000000000000L / 10000000000000L)]); 
/*  827 */       if (value >= 1000000000000L) append0(digits[(int)(value % 10000000000000L / 1000000000000L)]); 
/*  828 */       if (value >= 100000000000L) append0(digits[(int)(value % 1000000000000L / 100000000000L)]); 
/*  829 */       if (value >= 10000000000L) append0(digits[(int)(value % 100000000000L / 10000000000L)]); 
/*  830 */       if (value >= 1000000000L) append0(digits[(int)(value % 10000000000L / 1000000000L)]); 
/*  831 */       if (value >= 100000000L) append0(digits[(int)(value % 1000000000L / 100000000L)]); 
/*  832 */       if (value >= 10000000L) append0(digits[(int)(value % 100000000L / 10000000L)]); 
/*  833 */       if (value >= 1000000L) append0(digits[(int)(value % 10000000L / 1000000L)]); 
/*  834 */       if (value >= 100000L) append0(digits[(int)(value % 1000000L / 100000L)]); 
/*  835 */       append0(digits[(int)(value % 100000L / 10000L)]);
/*      */     } 
/*  837 */     if (value >= 1000L) append0(digits[(int)(value % 10000L / 1000L)]); 
/*  838 */     if (value >= 100L) append0(digits[(int)(value % 1000L / 100L)]); 
/*  839 */     if (value >= 10L) append0(digits[(int)(value % 100L / 10L)]); 
/*  840 */     append0(digits[(int)(value % 10L)]);
/*  841 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(float f) {
/*  850 */     append0(Float.toString(f));
/*  851 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(double d) {
/*  861 */     append0(Double.toString(d));
/*  862 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(Object obj) {
/*  872 */     if (obj == null) {
/*  873 */       appendNull();
/*      */     } else {
/*  875 */       append0(obj.toString());
/*      */     } 
/*  877 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(String str) {
/*  885 */     append0(str);
/*  886 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(char[] ch) {
/*  896 */     append0(ch);
/*  897 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(char[] str, int offset, int len) {
/*  910 */     append0(str, offset, len);
/*  911 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(CharSequence csq) {
/*  920 */     if (csq == null) {
/*  921 */       appendNull();
/*  922 */     } else if (csq instanceof StringBuilder) {
/*  923 */       StringBuilder builder = (StringBuilder)csq;
/*  924 */       append0(builder.chars, 0, builder.length);
/*      */     } else {
/*  926 */       append0(csq.toString());
/*      */     } 
/*  928 */     return this;
/*      */   }
/*      */   
/*      */   public StringBuilder append(StringBuilder builder) {
/*  932 */     if (builder == null) {
/*  933 */       appendNull();
/*      */     } else {
/*  935 */       append0(builder.chars, 0, builder.length);
/*  936 */     }  return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder append(CharSequence csq, int start, int end) {
/*  949 */     append0(csq, start, end);
/*  950 */     return this;
/*      */   }
/*      */   
/*      */   public StringBuilder append(StringBuilder builder, int start, int end) {
/*  954 */     if (builder == null) {
/*  955 */       appendNull();
/*      */     } else {
/*  957 */       append0(builder.chars, start, end);
/*  958 */     }  return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder appendCodePoint(int codePoint) {
/*  968 */     append0(Character.toChars(codePoint));
/*  969 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder delete(int start, int end) {
/*  980 */     delete0(start, end);
/*  981 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder deleteCharAt(int index) {
/*  991 */     deleteCharAt0(index);
/*  992 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, boolean b) {
/* 1004 */     insert0(offset, b ? "true" : "false");
/* 1005 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, char c) {
/* 1017 */     insert0(offset, c);
/* 1018 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, int i) {
/* 1030 */     insert0(offset, Integer.toString(i));
/* 1031 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, long l) {
/* 1043 */     insert0(offset, Long.toString(l));
/* 1044 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, float f) {
/* 1056 */     insert0(offset, Float.toString(f));
/* 1057 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, double d) {
/* 1069 */     insert0(offset, Double.toString(d));
/* 1070 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, Object obj) {
/* 1082 */     insert0(offset, (obj == null) ? "null" : obj.toString());
/* 1083 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, String str) {
/* 1094 */     insert0(offset, str);
/* 1095 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, char[] ch) {
/* 1107 */     insert0(offset, ch);
/* 1108 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, char[] str, int strOffset, int strLen) {
/* 1123 */     insert0(offset, str, strOffset, strLen);
/* 1124 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, CharSequence s) {
/* 1137 */     insert0(offset, (s == null) ? "null" : s.toString());
/* 1138 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder insert(int offset, CharSequence s, int start, int end) {
/* 1154 */     insert0(offset, s, start, end);
/* 1155 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuilder replace(int start, int end, String str) {
/* 1168 */     replace0(start, end, str);
/* 1169 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public StringBuilder replace(String find, String replace) {
/* 1174 */     int findLength = find.length(), replaceLength = replace.length();
/* 1175 */     int index = 0;
/*      */     while (true) {
/* 1177 */       index = indexOf(find, index);
/* 1178 */       if (index == -1)
/* 1179 */         break;  replace0(index, index + findLength, replace);
/* 1180 */       index += replaceLength;
/*      */     } 
/* 1182 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public StringBuilder replace(char find, String replace) {
/* 1187 */     int replaceLength = replace.length();
/* 1188 */     int index = 0;
/*      */     
/*      */     while (true) {
/* 1191 */       if (index == this.length) return this; 
/* 1192 */       if (this.chars[index] == find) {
/*      */ 
/*      */         
/* 1195 */         replace0(index, index + 1, replace);
/* 1196 */         index += replaceLength;
/*      */         continue;
/*      */       } 
/*      */       index++;
/*      */     } 
/*      */   }
/*      */   
/*      */   public StringBuilder reverse() {
/* 1204 */     reverse0();
/* 1205 */     return this;
/*      */   }
/*      */   
/*      */   public int hashCode() {
/* 1209 */     int prime = 31;
/* 1210 */     int result = 1;
/* 1211 */     result = 31 + this.length;
/* 1212 */     result = 31 * result + Arrays.hashCode(this.chars);
/* 1213 */     return result;
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1217 */     if (this == obj) return true; 
/* 1218 */     if (obj == null) return false; 
/* 1219 */     if (getClass() != obj.getClass()) return false; 
/* 1220 */     StringBuilder other = (StringBuilder)obj;
/* 1221 */     int length = this.length;
/* 1222 */     if (length != other.length) return false; 
/* 1223 */     char[] chars = this.chars;
/* 1224 */     char[] chars2 = other.chars;
/* 1225 */     if (chars == chars2) return true; 
/* 1226 */     if (chars == null || chars2 == null) return false; 
/* 1227 */     for (int i = 0; i < length; i++) {
/* 1228 */       if (chars[i] != chars2[i]) return false; 
/* 1229 */     }  return true;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\StringBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */