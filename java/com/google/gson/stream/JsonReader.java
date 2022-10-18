/*      */ package com.google.gson.stream;
/*      */ 
/*      */ import com.google.gson.internal.JsonReaderInternalAccess;
/*      */ import com.google.gson.internal.bind.JsonTreeReader;
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JsonReader
/*      */   implements Closeable
/*      */ {
/*  192 */   private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
/*      */   
/*      */   private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
/*      */   
/*      */   private static final int PEEKED_NONE = 0;
/*      */   
/*      */   private static final int PEEKED_BEGIN_OBJECT = 1;
/*      */   
/*      */   private static final int PEEKED_END_OBJECT = 2;
/*      */   
/*      */   private static final int PEEKED_BEGIN_ARRAY = 3;
/*      */   
/*      */   private static final int PEEKED_END_ARRAY = 4;
/*      */   
/*      */   private static final int PEEKED_TRUE = 5;
/*      */   
/*      */   private static final int PEEKED_FALSE = 6;
/*      */   
/*      */   private static final int PEEKED_NULL = 7;
/*      */   
/*      */   private static final int PEEKED_SINGLE_QUOTED = 8;
/*      */   
/*      */   private static final int PEEKED_DOUBLE_QUOTED = 9;
/*      */   
/*      */   private static final int PEEKED_UNQUOTED = 10;
/*      */   
/*      */   private static final int PEEKED_BUFFERED = 11;
/*      */   
/*      */   private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
/*      */   
/*      */   private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
/*      */   
/*      */   private static final int PEEKED_UNQUOTED_NAME = 14;
/*      */   private static final int PEEKED_LONG = 15;
/*      */   private static final int PEEKED_NUMBER = 16;
/*      */   private static final int PEEKED_EOF = 17;
/*      */   private static final int NUMBER_CHAR_NONE = 0;
/*      */   private static final int NUMBER_CHAR_SIGN = 1;
/*      */   private static final int NUMBER_CHAR_DIGIT = 2;
/*      */   private static final int NUMBER_CHAR_DECIMAL = 3;
/*      */   private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
/*      */   private static final int NUMBER_CHAR_EXP_E = 5;
/*      */   private static final int NUMBER_CHAR_EXP_SIGN = 6;
/*      */   private static final int NUMBER_CHAR_EXP_DIGIT = 7;
/*      */   private final Reader in;
/*      */   private boolean lenient = false;
/*  238 */   private final char[] buffer = new char[1024];
/*  239 */   private int pos = 0;
/*  240 */   private int limit = 0;
/*      */   
/*  242 */   private int lineNumber = 0;
/*  243 */   private int lineStart = 0;
/*      */   
/*  245 */   private int peeked = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long peekedLong;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int peekedNumberLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String peekedString;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  269 */   private int[] stack = new int[32];
/*  270 */   private int stackSize = 0; private String[] pathNames; private int[] pathIndices;
/*      */   public JsonReader(Reader in) {
/*  272 */     this.stack[this.stackSize++] = 6;
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
/*  283 */     this.pathNames = new String[32];
/*  284 */     this.pathIndices = new int[32];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  290 */     if (in == null) {
/*  291 */       throw new NullPointerException("in == null");
/*      */     }
/*  293 */     this.in = in;
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
/*      */   public final void setLenient(boolean lenient) {
/*  326 */     this.lenient = lenient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isLenient() {
/*  333 */     return this.lenient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginArray() throws IOException {
/*  341 */     int p = this.peeked;
/*  342 */     if (p == 0) {
/*  343 */       p = doPeek();
/*      */     }
/*  345 */     if (p == 3) {
/*  346 */       push(1);
/*  347 */       this.pathIndices[this.stackSize - 1] = 0;
/*  348 */       this.peeked = 0;
/*      */     } else {
/*  350 */       throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endArray() throws IOException {
/*  360 */     int p = this.peeked;
/*  361 */     if (p == 0) {
/*  362 */       p = doPeek();
/*      */     }
/*  364 */     if (p == 4) {
/*  365 */       this.stackSize--;
/*  366 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  367 */       this.peeked = 0;
/*      */     } else {
/*  369 */       throw new IllegalStateException("Expected END_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginObject() throws IOException {
/*  379 */     int p = this.peeked;
/*  380 */     if (p == 0) {
/*  381 */       p = doPeek();
/*      */     }
/*  383 */     if (p == 1) {
/*  384 */       push(3);
/*  385 */       this.peeked = 0;
/*      */     } else {
/*  387 */       throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endObject() throws IOException {
/*  397 */     int p = this.peeked;
/*  398 */     if (p == 0) {
/*  399 */       p = doPeek();
/*      */     }
/*  401 */     if (p == 2) {
/*  402 */       this.stackSize--;
/*  403 */       this.pathNames[this.stackSize] = null;
/*  404 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  405 */       this.peeked = 0;
/*      */     } else {
/*  407 */       throw new IllegalStateException("Expected END_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNext() throws IOException {
/*  416 */     int p = this.peeked;
/*  417 */     if (p == 0) {
/*  418 */       p = doPeek();
/*      */     }
/*  420 */     return (p != 2 && p != 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonToken peek() throws IOException {
/*  427 */     int p = this.peeked;
/*  428 */     if (p == 0) {
/*  429 */       p = doPeek();
/*      */     }
/*      */     
/*  432 */     switch (p) {
/*      */       case 1:
/*  434 */         return JsonToken.BEGIN_OBJECT;
/*      */       case 2:
/*  436 */         return JsonToken.END_OBJECT;
/*      */       case 3:
/*  438 */         return JsonToken.BEGIN_ARRAY;
/*      */       case 4:
/*  440 */         return JsonToken.END_ARRAY;
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*  444 */         return JsonToken.NAME;
/*      */       case 5:
/*      */       case 6:
/*  447 */         return JsonToken.BOOLEAN;
/*      */       case 7:
/*  449 */         return JsonToken.NULL;
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*  454 */         return JsonToken.STRING;
/*      */       case 15:
/*      */       case 16:
/*  457 */         return JsonToken.NUMBER;
/*      */       case 17:
/*  459 */         return JsonToken.END_DOCUMENT;
/*      */     } 
/*  461 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   
/*      */   private int doPeek() throws IOException {
/*  466 */     int peekStack = this.stack[this.stackSize - 1];
/*  467 */     if (peekStack == 1)
/*  468 */     { this.stack[this.stackSize - 1] = 2; }
/*  469 */     else if (peekStack == 2)
/*      */     
/*  471 */     { int i = nextNonWhitespace(true);
/*  472 */       switch (i) {
/*      */         case 93:
/*  474 */           return this.peeked = 4;
/*      */         case 59:
/*  476 */           checkLenient(); break;
/*      */         case 44:
/*      */           break;
/*      */         default:
/*  480 */           throw syntaxError("Unterminated array");
/*      */       }  }
/*  482 */     else { if (peekStack == 3 || peekStack == 5) {
/*  483 */         this.stack[this.stackSize - 1] = 4;
/*      */         
/*  485 */         if (peekStack == 5) {
/*  486 */           int j = nextNonWhitespace(true);
/*  487 */           switch (j) {
/*      */             case 125:
/*  489 */               return this.peeked = 2;
/*      */             case 59:
/*  491 */               checkLenient(); break;
/*      */             case 44:
/*      */               break;
/*      */             default:
/*  495 */               throw syntaxError("Unterminated object");
/*      */           } 
/*      */         } 
/*  498 */         int i = nextNonWhitespace(true);
/*  499 */         switch (i) {
/*      */           case 34:
/*  501 */             return this.peeked = 13;
/*      */           case 39:
/*  503 */             checkLenient();
/*  504 */             return this.peeked = 12;
/*      */           case 125:
/*  506 */             if (peekStack != 5) {
/*  507 */               return this.peeked = 2;
/*      */             }
/*  509 */             throw syntaxError("Expected name");
/*      */         } 
/*      */         
/*  512 */         checkLenient();
/*  513 */         this.pos--;
/*  514 */         if (isLiteral((char)i)) {
/*  515 */           return this.peeked = 14;
/*      */         }
/*  517 */         throw syntaxError("Expected name");
/*      */       } 
/*      */       
/*  520 */       if (peekStack == 4) {
/*  521 */         this.stack[this.stackSize - 1] = 5;
/*      */         
/*  523 */         int i = nextNonWhitespace(true);
/*  524 */         switch (i) {
/*      */           case 58:
/*      */             break;
/*      */           case 61:
/*  528 */             checkLenient();
/*  529 */             if ((this.pos < this.limit || fillBuffer(1)) && this.buffer[this.pos] == '>') {
/*  530 */               this.pos++;
/*      */             }
/*      */             break;
/*      */           default:
/*  534 */             throw syntaxError("Expected ':'");
/*      */         } 
/*  536 */       } else if (peekStack == 6) {
/*  537 */         if (this.lenient) {
/*  538 */           consumeNonExecutePrefix();
/*      */         }
/*  540 */         this.stack[this.stackSize - 1] = 7;
/*  541 */       } else if (peekStack == 7) {
/*  542 */         int i = nextNonWhitespace(false);
/*  543 */         if (i == -1) {
/*  544 */           return this.peeked = 17;
/*      */         }
/*  546 */         checkLenient();
/*  547 */         this.pos--;
/*      */       }
/*  549 */       else if (peekStack == 8) {
/*  550 */         throw new IllegalStateException("JsonReader is closed");
/*      */       }  }
/*      */     
/*  553 */     int c = nextNonWhitespace(true);
/*  554 */     switch (c) {
/*      */       case 93:
/*  556 */         if (peekStack == 1) {
/*  557 */           return this.peeked = 4;
/*      */         }
/*      */ 
/*      */       
/*      */       case 44:
/*      */       case 59:
/*  563 */         if (peekStack == 1 || peekStack == 2) {
/*  564 */           checkLenient();
/*  565 */           this.pos--;
/*  566 */           return this.peeked = 7;
/*      */         } 
/*  568 */         throw syntaxError("Unexpected value");
/*      */       
/*      */       case 39:
/*  571 */         checkLenient();
/*  572 */         return this.peeked = 8;
/*      */       case 34:
/*  574 */         if (this.stackSize == 1) {
/*  575 */           checkLenient();
/*      */         }
/*  577 */         return this.peeked = 9;
/*      */       case 91:
/*  579 */         return this.peeked = 3;
/*      */       case 123:
/*  581 */         return this.peeked = 1;
/*      */     } 
/*  583 */     this.pos--;
/*      */ 
/*      */     
/*  586 */     if (this.stackSize == 1) {
/*  587 */       checkLenient();
/*      */     }
/*      */     
/*  590 */     int result = peekKeyword();
/*  591 */     if (result != 0) {
/*  592 */       return result;
/*      */     }
/*      */     
/*  595 */     result = peekNumber();
/*  596 */     if (result != 0) {
/*  597 */       return result;
/*      */     }
/*      */     
/*  600 */     if (!isLiteral(this.buffer[this.pos])) {
/*  601 */       throw syntaxError("Expected value");
/*      */     }
/*      */     
/*  604 */     checkLenient();
/*  605 */     return this.peeked = 10;
/*      */   }
/*      */   private int peekKeyword() throws IOException {
/*      */     String keyword, keywordUpper;
/*      */     int peeking;
/*  610 */     char c = this.buffer[this.pos];
/*      */ 
/*      */ 
/*      */     
/*  614 */     if (c == 't' || c == 'T') {
/*  615 */       keyword = "true";
/*  616 */       keywordUpper = "TRUE";
/*  617 */       peeking = 5;
/*  618 */     } else if (c == 'f' || c == 'F') {
/*  619 */       keyword = "false";
/*  620 */       keywordUpper = "FALSE";
/*  621 */       peeking = 6;
/*  622 */     } else if (c == 'n' || c == 'N') {
/*  623 */       keyword = "null";
/*  624 */       keywordUpper = "NULL";
/*  625 */       peeking = 7;
/*      */     } else {
/*  627 */       return 0;
/*      */     } 
/*      */ 
/*      */     
/*  631 */     int length = keyword.length();
/*  632 */     for (int i = 1; i < length; i++) {
/*  633 */       if (this.pos + i >= this.limit && !fillBuffer(i + 1)) {
/*  634 */         return 0;
/*      */       }
/*  636 */       c = this.buffer[this.pos + i];
/*  637 */       if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
/*  638 */         return 0;
/*      */       }
/*      */     } 
/*      */     
/*  642 */     if ((this.pos + length < this.limit || fillBuffer(length + 1)) && isLiteral(this.buffer[this.pos + length]))
/*      */     {
/*  644 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  648 */     this.pos += length;
/*  649 */     return this.peeked = peeking;
/*      */   }
/*      */   
/*      */   private int peekNumber() throws IOException {
/*      */     int j;
/*  654 */     char[] buffer = this.buffer;
/*  655 */     int p = this.pos;
/*  656 */     int l = this.limit;
/*      */     
/*  658 */     long value = 0L;
/*  659 */     boolean negative = false;
/*  660 */     boolean fitsInLong = true;
/*  661 */     int last = 0;
/*      */     
/*  663 */     int i = 0;
/*      */ 
/*      */     
/*  666 */     for (;; i++) {
/*  667 */       if (p + i == l) {
/*  668 */         if (i == buffer.length)
/*      */         {
/*      */           
/*  671 */           return 0;
/*      */         }
/*  673 */         if (!fillBuffer(i + 1)) {
/*      */           break;
/*      */         }
/*  676 */         p = this.pos;
/*  677 */         l = this.limit;
/*      */       } 
/*      */       
/*  680 */       char c = buffer[p + i];
/*  681 */       switch (c) {
/*      */         case '-':
/*  683 */           if (last == 0) {
/*  684 */             negative = true;
/*  685 */             last = 1; break;
/*      */           } 
/*  687 */           if (last == 5) {
/*  688 */             last = 6;
/*      */             break;
/*      */           } 
/*  691 */           return 0;
/*      */         
/*      */         case '+':
/*  694 */           if (last == 5) {
/*  695 */             last = 6;
/*      */             break;
/*      */           } 
/*  698 */           return 0;
/*      */         
/*      */         case 'E':
/*      */         case 'e':
/*  702 */           if (last == 2 || last == 4) {
/*  703 */             last = 5;
/*      */             break;
/*      */           } 
/*  706 */           return 0;
/*      */         
/*      */         case '.':
/*  709 */           if (last == 2) {
/*  710 */             last = 3;
/*      */             break;
/*      */           } 
/*  713 */           return 0;
/*      */         
/*      */         default:
/*  716 */           if (c < '0' || c > '9') {
/*  717 */             if (!isLiteral(c)) {
/*      */               break;
/*      */             }
/*  720 */             return 0;
/*      */           } 
/*  722 */           if (last == 1 || last == 0) {
/*  723 */             value = -(c - 48);
/*  724 */             last = 2; break;
/*  725 */           }  if (last == 2) {
/*  726 */             if (value == 0L) {
/*  727 */               return 0;
/*      */             }
/*  729 */             long newValue = value * 10L - (c - 48);
/*  730 */             j = fitsInLong & ((value > -922337203685477580L || (value == -922337203685477580L && newValue < value)) ? 1 : 0);
/*      */             
/*  732 */             value = newValue; break;
/*  733 */           }  if (last == 3) {
/*  734 */             last = 4; break;
/*  735 */           }  if (last == 5 || last == 6) {
/*  736 */             last = 7;
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  742 */     if (last == 2 && j != 0 && (value != Long.MIN_VALUE || negative)) {
/*  743 */       this.peekedLong = negative ? value : -value;
/*  744 */       this.pos += i;
/*  745 */       return this.peeked = 15;
/*  746 */     }  if (last == 2 || last == 4 || last == 7) {
/*      */       
/*  748 */       this.peekedNumberLength = i;
/*  749 */       return this.peeked = 16;
/*      */     } 
/*  751 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isLiteral(char c) throws IOException {
/*  756 */     switch (c) {
/*      */       case '#':
/*      */       case '/':
/*      */       case ';':
/*      */       case '=':
/*      */       case '\\':
/*  762 */         checkLenient();
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*      */       case ' ':
/*      */       case ',':
/*      */       case ':':
/*      */       case '[':
/*      */       case ']':
/*      */       case '{':
/*      */       case '}':
/*  774 */         return false;
/*      */     } 
/*  776 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextName() throws IOException {
/*      */     String result;
/*  788 */     int p = this.peeked;
/*  789 */     if (p == 0) {
/*  790 */       p = doPeek();
/*      */     }
/*      */     
/*  793 */     if (p == 14) {
/*  794 */       result = nextUnquotedValue();
/*  795 */     } else if (p == 12) {
/*  796 */       result = nextQuotedValue('\'');
/*  797 */     } else if (p == 13) {
/*  798 */       result = nextQuotedValue('"');
/*      */     } else {
/*  800 */       throw new IllegalStateException("Expected a name but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */     
/*  803 */     this.peeked = 0;
/*  804 */     this.pathNames[this.stackSize - 1] = result;
/*  805 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextString() throws IOException {
/*      */     String result;
/*  817 */     int p = this.peeked;
/*  818 */     if (p == 0) {
/*  819 */       p = doPeek();
/*      */     }
/*      */     
/*  822 */     if (p == 10) {
/*  823 */       result = nextUnquotedValue();
/*  824 */     } else if (p == 8) {
/*  825 */       result = nextQuotedValue('\'');
/*  826 */     } else if (p == 9) {
/*  827 */       result = nextQuotedValue('"');
/*  828 */     } else if (p == 11) {
/*  829 */       result = this.peekedString;
/*  830 */       this.peekedString = null;
/*  831 */     } else if (p == 15) {
/*  832 */       result = Long.toString(this.peekedLong);
/*  833 */     } else if (p == 16) {
/*  834 */       result = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  835 */       this.pos += this.peekedNumberLength;
/*      */     } else {
/*  837 */       throw new IllegalStateException("Expected a string but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */     
/*  840 */     this.peeked = 0;
/*  841 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  842 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nextBoolean() throws IOException {
/*  853 */     int p = this.peeked;
/*  854 */     if (p == 0) {
/*  855 */       p = doPeek();
/*      */     }
/*  857 */     if (p == 5) {
/*  858 */       this.peeked = 0;
/*  859 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  860 */       return true;
/*  861 */     }  if (p == 6) {
/*  862 */       this.peeked = 0;
/*  863 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  864 */       return false;
/*      */     } 
/*  866 */     throw new IllegalStateException("Expected a boolean but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
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
/*      */   public void nextNull() throws IOException {
/*  878 */     int p = this.peeked;
/*  879 */     if (p == 0) {
/*  880 */       p = doPeek();
/*      */     }
/*  882 */     if (p == 7) {
/*  883 */       this.peeked = 0;
/*  884 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*      */     } else {
/*  886 */       throw new IllegalStateException("Expected null but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
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
/*      */   
/*      */   public double nextDouble() throws IOException {
/*  901 */     int p = this.peeked;
/*  902 */     if (p == 0) {
/*  903 */       p = doPeek();
/*      */     }
/*      */     
/*  906 */     if (p == 15) {
/*  907 */       this.peeked = 0;
/*  908 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  909 */       return this.peekedLong;
/*      */     } 
/*      */     
/*  912 */     if (p == 16) {
/*  913 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  914 */       this.pos += this.peekedNumberLength;
/*  915 */     } else if (p == 8 || p == 9) {
/*  916 */       this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*  917 */     } else if (p == 10) {
/*  918 */       this.peekedString = nextUnquotedValue();
/*  919 */     } else if (p != 11) {
/*  920 */       throw new IllegalStateException("Expected a double but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */ 
/*      */     
/*  924 */     this.peeked = 11;
/*  925 */     double result = Double.parseDouble(this.peekedString);
/*  926 */     if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
/*  927 */       throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     }
/*      */     
/*  930 */     this.peekedString = null;
/*  931 */     this.peeked = 0;
/*  932 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  933 */     return result;
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
/*      */   public long nextLong() throws IOException {
/*  947 */     int p = this.peeked;
/*  948 */     if (p == 0) {
/*  949 */       p = doPeek();
/*      */     }
/*      */     
/*  952 */     if (p == 15) {
/*  953 */       this.peeked = 0;
/*  954 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  955 */       return this.peekedLong;
/*      */     } 
/*      */     
/*  958 */     if (p == 16) {
/*  959 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  960 */       this.pos += this.peekedNumberLength;
/*  961 */     } else if (p == 8 || p == 9) {
/*  962 */       this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*      */       try {
/*  964 */         long l = Long.parseLong(this.peekedString);
/*  965 */         this.peeked = 0;
/*  966 */         this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  967 */         return l;
/*  968 */       } catch (NumberFormatException ignored) {}
/*      */     }
/*      */     else {
/*      */       
/*  972 */       throw new IllegalStateException("Expected a long but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */ 
/*      */     
/*  976 */     this.peeked = 11;
/*  977 */     double asDouble = Double.parseDouble(this.peekedString);
/*  978 */     long result = (long)asDouble;
/*  979 */     if (result != asDouble) {
/*  980 */       throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     }
/*      */     
/*  983 */     this.peekedString = null;
/*  984 */     this.peeked = 0;
/*  985 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  986 */     return result;
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
/*      */   private String nextQuotedValue(char quote) throws IOException {
/* 1001 */     char[] buffer = this.buffer;
/* 1002 */     StringBuilder builder = new StringBuilder();
/*      */     while (true) {
/* 1004 */       int p = this.pos;
/* 1005 */       int l = this.limit;
/*      */       
/* 1007 */       int start = p;
/* 1008 */       while (p < l) {
/* 1009 */         int c = buffer[p++];
/*      */         
/* 1011 */         if (c == quote) {
/* 1012 */           this.pos = p;
/* 1013 */           builder.append(buffer, start, p - start - 1);
/* 1014 */           return builder.toString();
/* 1015 */         }  if (c == 92) {
/* 1016 */           this.pos = p;
/* 1017 */           builder.append(buffer, start, p - start - 1);
/* 1018 */           builder.append(readEscapeCharacter());
/* 1019 */           p = this.pos;
/* 1020 */           l = this.limit;
/* 1021 */           start = p; continue;
/* 1022 */         }  if (c == 10) {
/* 1023 */           this.lineNumber++;
/* 1024 */           this.lineStart = p;
/*      */         } 
/*      */       } 
/*      */       
/* 1028 */       builder.append(buffer, start, p - start);
/* 1029 */       this.pos = p;
/* 1030 */       if (!fillBuffer(1)) {
/* 1031 */         throw syntaxError("Unterminated string");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String nextUnquotedValue() throws IOException {
/*      */     String result;
/* 1041 */     StringBuilder builder = null;
/* 1042 */     int i = 0;
/*      */ 
/*      */     
/*      */     label34: while (true) {
/* 1046 */       for (; this.pos + i < this.limit; i++)
/* 1047 */       { switch (this.buffer[this.pos + i])
/*      */         { case '#':
/*      */           case '/':
/*      */           case ';':
/*      */           case '=':
/*      */           case '\\':
/* 1053 */             checkLenient(); break label34;
/*      */           case '\t': break label34;
/*      */           case '\n': break label34;
/*      */           case '\f': break label34;
/*      */           case '\r': break label34;
/*      */           case ' ': break label34;
/*      */           case ',':
/*      */             break label34;
/*      */           case ':':
/*      */             break label34;
/*      */           case '[':
/*      */             break label34;
/*      */           case ']':
/*      */             break label34;
/*      */           case '{':
/*      */             break label34;
/*      */           case '}':
/* 1070 */             break label34; }  }  if (i < this.buffer.length) {
/* 1071 */         if (fillBuffer(i + 1)) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/* 1079 */       if (builder == null) {
/* 1080 */         builder = new StringBuilder();
/*      */       }
/* 1082 */       builder.append(this.buffer, this.pos, i);
/* 1083 */       this.pos += i;
/* 1084 */       i = 0;
/* 1085 */       if (!fillBuffer(1)) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1091 */     if (builder == null) {
/* 1092 */       result = new String(this.buffer, this.pos, i);
/*      */     } else {
/* 1094 */       builder.append(this.buffer, this.pos, i);
/* 1095 */       result = builder.toString();
/*      */     } 
/* 1097 */     this.pos += i;
/* 1098 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private void skipQuotedValue(char quote) throws IOException {
/* 1103 */     char[] buffer = this.buffer;
/*      */     while (true) {
/* 1105 */       int p = this.pos;
/* 1106 */       int l = this.limit;
/*      */       
/* 1108 */       while (p < l) {
/* 1109 */         int c = buffer[p++];
/* 1110 */         if (c == quote) {
/* 1111 */           this.pos = p; return;
/*      */         } 
/* 1113 */         if (c == 92) {
/* 1114 */           this.pos = p;
/* 1115 */           readEscapeCharacter();
/* 1116 */           p = this.pos;
/* 1117 */           l = this.limit; continue;
/* 1118 */         }  if (c == 10) {
/* 1119 */           this.lineNumber++;
/* 1120 */           this.lineStart = p;
/*      */         } 
/*      */       } 
/* 1123 */       this.pos = p;
/* 1124 */       if (!fillBuffer(1))
/* 1125 */         throw syntaxError("Unterminated string"); 
/*      */     } 
/*      */   }
/*      */   private void skipUnquotedValue() throws IOException {
/*      */     do {
/* 1130 */       int i = 0;
/* 1131 */       for (; this.pos + i < this.limit; i++) {
/* 1132 */         switch (this.buffer[this.pos + i]) {
/*      */           case '#':
/*      */           case '/':
/*      */           case ';':
/*      */           case '=':
/*      */           case '\\':
/* 1138 */             checkLenient();
/*      */           case '\t':
/*      */           case '\n':
/*      */           case '\f':
/*      */           case '\r':
/*      */           case ' ':
/*      */           case ',':
/*      */           case ':':
/*      */           case '[':
/*      */           case ']':
/*      */           case '{':
/*      */           case '}':
/* 1150 */             this.pos += i;
/*      */             return;
/*      */         } 
/*      */       } 
/* 1154 */       this.pos += i;
/* 1155 */     } while (fillBuffer(1));
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
/*      */   public int nextInt() throws IOException {
/* 1169 */     int p = this.peeked;
/* 1170 */     if (p == 0) {
/* 1171 */       p = doPeek();
/*      */     }
/*      */ 
/*      */     
/* 1175 */     if (p == 15) {
/* 1176 */       int result = (int)this.peekedLong;
/* 1177 */       if (this.peekedLong != result) {
/* 1178 */         throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */       }
/*      */       
/* 1181 */       this.peeked = 0;
/* 1182 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1183 */       return result;
/*      */     } 
/*      */     
/* 1186 */     if (p == 16) {
/* 1187 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/* 1188 */       this.pos += this.peekedNumberLength;
/* 1189 */     } else if (p == 8 || p == 9) {
/* 1190 */       this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*      */       try {
/* 1192 */         int result = Integer.parseInt(this.peekedString);
/* 1193 */         this.peeked = 0;
/* 1194 */         this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1195 */         return result;
/* 1196 */       } catch (NumberFormatException ignored) {}
/*      */     }
/*      */     else {
/*      */       
/* 1200 */       throw new IllegalStateException("Expected an int but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     } 
/*      */ 
/*      */     
/* 1204 */     this.peeked = 11;
/* 1205 */     double asDouble = Double.parseDouble(this.peekedString);
/* 1206 */     int i = (int)asDouble;
/* 1207 */     if (i != asDouble) {
/* 1208 */       throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */     }
/*      */     
/* 1211 */     this.peekedString = null;
/* 1212 */     this.peeked = 0;
/* 1213 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1214 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/* 1221 */     this.peeked = 0;
/* 1222 */     this.stack[0] = 8;
/* 1223 */     this.stackSize = 1;
/* 1224 */     this.in.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void skipValue() throws IOException {
/* 1233 */     int count = 0;
/*      */     do {
/* 1235 */       int p = this.peeked;
/* 1236 */       if (p == 0) {
/* 1237 */         p = doPeek();
/*      */       }
/*      */       
/* 1240 */       if (p == 3) {
/* 1241 */         push(1);
/* 1242 */         count++;
/* 1243 */       } else if (p == 1) {
/* 1244 */         push(3);
/* 1245 */         count++;
/* 1246 */       } else if (p == 4) {
/* 1247 */         this.stackSize--;
/* 1248 */         count--;
/* 1249 */       } else if (p == 2) {
/* 1250 */         this.stackSize--;
/* 1251 */         count--;
/* 1252 */       } else if (p == 14 || p == 10) {
/* 1253 */         skipUnquotedValue();
/* 1254 */       } else if (p == 8 || p == 12) {
/* 1255 */         skipQuotedValue('\'');
/* 1256 */       } else if (p == 9 || p == 13) {
/* 1257 */         skipQuotedValue('"');
/* 1258 */       } else if (p == 16) {
/* 1259 */         this.pos += this.peekedNumberLength;
/*      */       } 
/* 1261 */       this.peeked = 0;
/* 1262 */     } while (count != 0);
/*      */     
/* 1264 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1265 */     this.pathNames[this.stackSize - 1] = "null";
/*      */   }
/*      */   
/*      */   private void push(int newTop) {
/* 1269 */     if (this.stackSize == this.stack.length) {
/* 1270 */       int[] newStack = new int[this.stackSize * 2];
/* 1271 */       int[] newPathIndices = new int[this.stackSize * 2];
/* 1272 */       String[] newPathNames = new String[this.stackSize * 2];
/* 1273 */       System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
/* 1274 */       System.arraycopy(this.pathIndices, 0, newPathIndices, 0, this.stackSize);
/* 1275 */       System.arraycopy(this.pathNames, 0, newPathNames, 0, this.stackSize);
/* 1276 */       this.stack = newStack;
/* 1277 */       this.pathIndices = newPathIndices;
/* 1278 */       this.pathNames = newPathNames;
/*      */     } 
/* 1280 */     this.stack[this.stackSize++] = newTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fillBuffer(int minimum) throws IOException {
/* 1289 */     char[] buffer = this.buffer;
/* 1290 */     this.lineStart -= this.pos;
/* 1291 */     if (this.limit != this.pos) {
/* 1292 */       this.limit -= this.pos;
/* 1293 */       System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
/*      */     } else {
/* 1295 */       this.limit = 0;
/*      */     } 
/*      */     
/* 1298 */     this.pos = 0;
/*      */     int total;
/* 1300 */     while ((total = this.in.read(buffer, this.limit, buffer.length - this.limit)) != -1) {
/* 1301 */       this.limit += total;
/*      */ 
/*      */       
/* 1304 */       if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer[0] == '﻿') {
/* 1305 */         this.pos++;
/* 1306 */         this.lineStart++;
/* 1307 */         minimum++;
/*      */       } 
/*      */       
/* 1310 */       if (this.limit >= minimum) {
/* 1311 */         return true;
/*      */       }
/*      */     } 
/* 1314 */     return false;
/*      */   }
/*      */   
/*      */   private int getLineNumber() {
/* 1318 */     return this.lineNumber + 1;
/*      */   }
/*      */   
/*      */   private int getColumnNumber() {
/* 1322 */     return this.pos - this.lineStart + 1;
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
/*      */ 
/*      */   
/*      */   private int nextNonWhitespace(boolean throwOnEof) throws IOException {
/* 1340 */     char[] buffer = this.buffer;
/* 1341 */     int p = this.pos;
/* 1342 */     int l = this.limit;
/*      */     while (true) {
/* 1344 */       if (p == l) {
/* 1345 */         this.pos = p;
/* 1346 */         if (!fillBuffer(1)) {
/*      */           break;
/*      */         }
/* 1349 */         p = this.pos;
/* 1350 */         l = this.limit;
/*      */       } 
/*      */       
/* 1353 */       int c = buffer[p++];
/* 1354 */       if (c == 10) {
/* 1355 */         this.lineNumber++;
/* 1356 */         this.lineStart = p; continue;
/*      */       } 
/* 1358 */       if (c == 32 || c == 13 || c == 9) {
/*      */         continue;
/*      */       }
/*      */       
/* 1362 */       if (c == 47) {
/* 1363 */         this.pos = p;
/* 1364 */         if (p == l) {
/* 1365 */           this.pos--;
/* 1366 */           boolean charsLoaded = fillBuffer(2);
/* 1367 */           this.pos++;
/* 1368 */           if (!charsLoaded) {
/* 1369 */             return c;
/*      */           }
/*      */         } 
/*      */         
/* 1373 */         checkLenient();
/* 1374 */         char peek = buffer[this.pos];
/* 1375 */         switch (peek) {
/*      */           
/*      */           case '*':
/* 1378 */             this.pos++;
/* 1379 */             if (!skipTo("*/")) {
/* 1380 */               throw syntaxError("Unterminated comment");
/*      */             }
/* 1382 */             p = this.pos + 2;
/* 1383 */             l = this.limit;
/*      */             continue;
/*      */ 
/*      */           
/*      */           case '/':
/* 1388 */             this.pos++;
/* 1389 */             skipToEndOfLine();
/* 1390 */             p = this.pos;
/* 1391 */             l = this.limit;
/*      */             continue;
/*      */         } 
/*      */         
/* 1395 */         return c;
/*      */       } 
/* 1397 */       if (c == 35) {
/* 1398 */         this.pos = p;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1404 */         checkLenient();
/* 1405 */         skipToEndOfLine();
/* 1406 */         p = this.pos;
/* 1407 */         l = this.limit; continue;
/*      */       } 
/* 1409 */       this.pos = p;
/* 1410 */       return c;
/*      */     } 
/*      */     
/* 1413 */     if (throwOnEof) {
/* 1414 */       throw new EOFException("End of input at line " + getLineNumber() + " column " + getColumnNumber());
/*      */     }
/*      */     
/* 1417 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkLenient() throws IOException {
/* 1422 */     if (!this.lenient) {
/* 1423 */       throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void skipToEndOfLine() throws IOException {
/* 1433 */     while (this.pos < this.limit || fillBuffer(1)) {
/* 1434 */       char c = this.buffer[this.pos++];
/* 1435 */       if (c == '\n') {
/* 1436 */         this.lineNumber++;
/* 1437 */         this.lineStart = this.pos; break;
/*      */       } 
/* 1439 */       if (c == '\r') {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipTo(String toFind) throws IOException {
/* 1450 */     for (; this.pos + toFind.length() <= this.limit || fillBuffer(toFind.length()); this.pos++) {
/* 1451 */       if (this.buffer[this.pos] == '\n') {
/* 1452 */         this.lineNumber++;
/* 1453 */         this.lineStart = this.pos + 1;
/*      */       } else {
/*      */         
/* 1456 */         int c = 0; while (true) { if (c < toFind.length()) {
/* 1457 */             if (this.buffer[this.pos + c] != toFind.charAt(c))
/*      */               break;  c++;
/*      */             continue;
/*      */           } 
/* 1461 */           return true; } 
/*      */       } 
/* 1463 */     }  return false;
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1467 */     return getClass().getSimpleName() + " at line " + getLineNumber() + " column " + getColumnNumber();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPath() {
/* 1476 */     StringBuilder result = (new StringBuilder()).append('$');
/* 1477 */     for (int i = 0, size = this.stackSize; i < size; i++) {
/* 1478 */       switch (this.stack[i]) {
/*      */         case 1:
/*      */         case 2:
/* 1481 */           result.append('[').append(this.pathIndices[i]).append(']');
/*      */           break;
/*      */         
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/* 1487 */           result.append('.');
/* 1488 */           if (this.pathNames[i] != null) {
/* 1489 */             result.append(this.pathNames[i]);
/*      */           }
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/* 1499 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char readEscapeCharacter() throws IOException {
/*      */     char result;
/*      */     int i, end;
/* 1512 */     if (this.pos == this.limit && !fillBuffer(1)) {
/* 1513 */       throw syntaxError("Unterminated escape sequence");
/*      */     }
/*      */     
/* 1516 */     char escaped = this.buffer[this.pos++];
/* 1517 */     switch (escaped) {
/*      */       case 'u':
/* 1519 */         if (this.pos + 4 > this.limit && !fillBuffer(4)) {
/* 1520 */           throw syntaxError("Unterminated escape sequence");
/*      */         }
/*      */         
/* 1523 */         result = Character.MIN_VALUE;
/* 1524 */         for (i = this.pos, end = i + 4; i < end; i++) {
/* 1525 */           char c = this.buffer[i];
/* 1526 */           result = (char)(result << 4);
/* 1527 */           if (c >= '0' && c <= '9') {
/* 1528 */             result = (char)(result + c - 48);
/* 1529 */           } else if (c >= 'a' && c <= 'f') {
/* 1530 */             result = (char)(result + c - 97 + 10);
/* 1531 */           } else if (c >= 'A' && c <= 'F') {
/* 1532 */             result = (char)(result + c - 65 + 10);
/*      */           } else {
/* 1534 */             throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
/*      */           } 
/*      */         } 
/* 1537 */         this.pos += 4;
/* 1538 */         return result;
/*      */       
/*      */       case 't':
/* 1541 */         return '\t';
/*      */       
/*      */       case 'b':
/* 1544 */         return '\b';
/*      */       
/*      */       case 'n':
/* 1547 */         return '\n';
/*      */       
/*      */       case 'r':
/* 1550 */         return '\r';
/*      */       
/*      */       case 'f':
/* 1553 */         return '\f';
/*      */       
/*      */       case '\n':
/* 1556 */         this.lineNumber++;
/* 1557 */         this.lineStart = this.pos;
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1564 */     return escaped;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IOException syntaxError(String message) throws IOException {
/* 1573 */     throw new MalformedJsonException(message + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void consumeNonExecutePrefix() throws IOException {
/* 1582 */     nextNonWhitespace(true);
/* 1583 */     this.pos--;
/*      */     
/* 1585 */     if (this.pos + NON_EXECUTE_PREFIX.length > this.limit && !fillBuffer(NON_EXECUTE_PREFIX.length)) {
/*      */       return;
/*      */     }
/*      */     
/* 1589 */     for (int i = 0; i < NON_EXECUTE_PREFIX.length; i++) {
/* 1590 */       if (this.buffer[this.pos + i] != NON_EXECUTE_PREFIX[i]) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1596 */     this.pos += NON_EXECUTE_PREFIX.length;
/*      */   }
/*      */   
/*      */   static {
/* 1600 */     JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
/*      */         public void promoteNameToValue(JsonReader reader) throws IOException {
/* 1602 */           if (reader instanceof JsonTreeReader) {
/* 1603 */             ((JsonTreeReader)reader).promoteNameToValue();
/*      */             return;
/*      */           } 
/* 1606 */           int p = reader.peeked;
/* 1607 */           if (p == 0) {
/* 1608 */             p = reader.doPeek();
/*      */           }
/* 1610 */           if (p == 13) {
/* 1611 */             reader.peeked = 9;
/* 1612 */           } else if (p == 12) {
/* 1613 */             reader.peeked = 8;
/* 1614 */           } else if (p == 14) {
/* 1615 */             reader.peeked = 10;
/*      */           } else {
/* 1617 */             throw new IllegalStateException("Expected a name but was " + reader.peek() + " " + " at line " + reader.getLineNumber() + " column " + reader.getColumnNumber() + " path " + reader.getPath());
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\stream\JsonReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */