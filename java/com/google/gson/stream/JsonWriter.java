/*     */ package com.google.gson.stream;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class JsonWriter
/*     */   implements Closeable, Flushable
/*     */ {
/* 145 */   private static final String[] REPLACEMENT_CHARS = new String[128]; static {
/* 146 */     for (int i = 0; i <= 31; i++) {
/* 147 */       REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[] { Integer.valueOf(i) });
/*     */     } 
/* 149 */     REPLACEMENT_CHARS[34] = "\\\"";
/* 150 */     REPLACEMENT_CHARS[92] = "\\\\";
/* 151 */     REPLACEMENT_CHARS[9] = "\\t";
/* 152 */     REPLACEMENT_CHARS[8] = "\\b";
/* 153 */     REPLACEMENT_CHARS[10] = "\\n";
/* 154 */     REPLACEMENT_CHARS[13] = "\\r";
/* 155 */     REPLACEMENT_CHARS[12] = "\\f";
/* 156 */   } private static final String[] HTML_SAFE_REPLACEMENT_CHARS = (String[])REPLACEMENT_CHARS.clone(); static {
/* 157 */     HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
/* 158 */     HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
/* 159 */     HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
/* 160 */     HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
/* 161 */     HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
/*     */   }
/*     */ 
/*     */   
/*     */   private final Writer out;
/*     */   
/* 167 */   private int[] stack = new int[32];
/* 168 */   private int stackSize = 0; private String indent;
/*     */   public JsonWriter(Writer out) {
/* 170 */     push(6);
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
/* 182 */     this.separator = ":";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     this.serializeNulls = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     if (out == null) {
/* 199 */       throw new NullPointerException("out == null");
/*     */     }
/* 201 */     this.out = out;
/*     */   }
/*     */ 
/*     */   
/*     */   private String separator;
/*     */   
/*     */   private boolean lenient;
/*     */   private boolean htmlSafe;
/*     */   private String deferredName;
/*     */   private boolean serializeNulls;
/*     */   
/*     */   public final void setIndent(String indent) {
/* 213 */     if (indent.length() == 0) {
/* 214 */       this.indent = null;
/* 215 */       this.separator = ":";
/*     */     } else {
/* 217 */       this.indent = indent;
/* 218 */       this.separator = ": ";
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
/*     */   public final void setLenient(boolean lenient) {
/* 235 */     this.lenient = lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLenient() {
/* 242 */     return this.lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHtmlSafe(boolean htmlSafe) {
/* 253 */     this.htmlSafe = htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isHtmlSafe() {
/* 261 */     return this.htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSerializeNulls(boolean serializeNulls) {
/* 269 */     this.serializeNulls = serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean getSerializeNulls() {
/* 277 */     return this.serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter beginArray() throws IOException {
/* 287 */     writeDeferredName();
/* 288 */     return open(1, "[");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter endArray() throws IOException {
/* 297 */     return close(1, 2, "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter beginObject() throws IOException {
/* 307 */     writeDeferredName();
/* 308 */     return open(3, "{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter endObject() throws IOException {
/* 317 */     return close(3, 5, "}");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonWriter open(int empty, String openBracket) throws IOException {
/* 325 */     beforeValue(true);
/* 326 */     push(empty);
/* 327 */     this.out.write(openBracket);
/* 328 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonWriter close(int empty, int nonempty, String closeBracket) throws IOException {
/* 337 */     int context = peek();
/* 338 */     if (context != nonempty && context != empty) {
/* 339 */       throw new IllegalStateException("Nesting problem.");
/*     */     }
/* 341 */     if (this.deferredName != null) {
/* 342 */       throw new IllegalStateException("Dangling name: " + this.deferredName);
/*     */     }
/*     */     
/* 345 */     this.stackSize--;
/* 346 */     if (context == nonempty) {
/* 347 */       newline();
/*     */     }
/* 349 */     this.out.write(closeBracket);
/* 350 */     return this;
/*     */   }
/*     */   
/*     */   private void push(int newTop) {
/* 354 */     if (this.stackSize == this.stack.length) {
/* 355 */       int[] newStack = new int[this.stackSize * 2];
/* 356 */       System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
/* 357 */       this.stack = newStack;
/*     */     } 
/* 359 */     this.stack[this.stackSize++] = newTop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int peek() {
/* 366 */     if (this.stackSize == 0) {
/* 367 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 369 */     return this.stack[this.stackSize - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void replaceTop(int topOfStack) {
/* 376 */     this.stack[this.stackSize - 1] = topOfStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter name(String name) throws IOException {
/* 386 */     if (name == null) {
/* 387 */       throw new NullPointerException("name == null");
/*     */     }
/* 389 */     if (this.deferredName != null) {
/* 390 */       throw new IllegalStateException();
/*     */     }
/* 392 */     if (this.stackSize == 0) {
/* 393 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 395 */     this.deferredName = name;
/* 396 */     return this;
/*     */   }
/*     */   
/*     */   private void writeDeferredName() throws IOException {
/* 400 */     if (this.deferredName != null) {
/* 401 */       beforeName();
/* 402 */       string(this.deferredName);
/* 403 */       this.deferredName = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(String value) throws IOException {
/* 414 */     if (value == null) {
/* 415 */       return nullValue();
/*     */     }
/* 417 */     writeDeferredName();
/* 418 */     beforeValue(false);
/* 419 */     string(value);
/* 420 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter jsonValue(String value) throws IOException {
/* 431 */     if (value == null) {
/* 432 */       return nullValue();
/*     */     }
/* 434 */     writeDeferredName();
/* 435 */     beforeValue(false);
/* 436 */     this.out.append(value);
/* 437 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter nullValue() throws IOException {
/* 446 */     if (this.deferredName != null) {
/* 447 */       if (this.serializeNulls) {
/* 448 */         writeDeferredName();
/*     */       } else {
/* 450 */         this.deferredName = null;
/* 451 */         return this;
/*     */       } 
/*     */     }
/* 454 */     beforeValue(false);
/* 455 */     this.out.write("null");
/* 456 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(boolean value) throws IOException {
/* 465 */     writeDeferredName();
/* 466 */     beforeValue(false);
/* 467 */     this.out.write(value ? "true" : "false");
/* 468 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(double value) throws IOException {
/* 479 */     if (Double.isNaN(value) || Double.isInfinite(value)) {
/* 480 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 482 */     writeDeferredName();
/* 483 */     beforeValue(false);
/* 484 */     this.out.append(Double.toString(value));
/* 485 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(long value) throws IOException {
/* 494 */     writeDeferredName();
/* 495 */     beforeValue(false);
/* 496 */     this.out.write(Long.toString(value));
/* 497 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(Number value) throws IOException {
/* 508 */     if (value == null) {
/* 509 */       return nullValue();
/*     */     }
/*     */     
/* 512 */     writeDeferredName();
/* 513 */     String string = value.toString();
/* 514 */     if (!this.lenient && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN")))
/*     */     {
/* 516 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 518 */     beforeValue(false);
/* 519 */     this.out.append(string);
/* 520 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 528 */     if (this.stackSize == 0) {
/* 529 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 531 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 540 */     this.out.close();
/*     */     
/* 542 */     int size = this.stackSize;
/* 543 */     if (size > 1 || (size == 1 && this.stack[size - 1] != 7)) {
/* 544 */       throw new IOException("Incomplete document");
/*     */     }
/* 546 */     this.stackSize = 0;
/*     */   }
/*     */   
/*     */   private void string(String value) throws IOException {
/* 550 */     String[] replacements = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
/* 551 */     this.out.write("\"");
/* 552 */     int last = 0;
/* 553 */     int length = value.length();
/* 554 */     for (int i = 0; i < length; i++) {
/* 555 */       String replacement; char c = value.charAt(i);
/*     */       
/* 557 */       if (c < '') {
/* 558 */         replacement = replacements[c];
/* 559 */         if (replacement == null) {
/*     */           continue;
/*     */         }
/* 562 */       } else if (c == ' ') {
/* 563 */         replacement = "\\u2028";
/* 564 */       } else if (c == ' ') {
/* 565 */         replacement = "\\u2029";
/*     */       } else {
/*     */         continue;
/*     */       } 
/* 569 */       if (last < i) {
/* 570 */         this.out.write(value, last, i - last);
/*     */       }
/* 572 */       this.out.write(replacement);
/* 573 */       last = i + 1; continue;
/*     */     } 
/* 575 */     if (last < length) {
/* 576 */       this.out.write(value, last, length - last);
/*     */     }
/* 578 */     this.out.write("\"");
/*     */   }
/*     */   
/*     */   private void newline() throws IOException {
/* 582 */     if (this.indent == null) {
/*     */       return;
/*     */     }
/*     */     
/* 586 */     this.out.write("\n");
/* 587 */     for (int i = 1, size = this.stackSize; i < size; i++) {
/* 588 */       this.out.write(this.indent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void beforeName() throws IOException {
/* 597 */     int context = peek();
/* 598 */     if (context == 5) {
/* 599 */       this.out.write(44);
/* 600 */     } else if (context != 3) {
/* 601 */       throw new IllegalStateException("Nesting problem.");
/*     */     } 
/* 603 */     newline();
/* 604 */     replaceTop(4);
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
/*     */   private void beforeValue(boolean root) throws IOException {
/* 617 */     switch (peek()) {
/*     */       case 7:
/* 619 */         if (!this.lenient) {
/* 620 */           throw new IllegalStateException("JSON must have only one top-level value.");
/*     */         }
/*     */ 
/*     */       
/*     */       case 6:
/* 625 */         if (!this.lenient && !root) {
/* 626 */           throw new IllegalStateException("JSON must start with an array or an object.");
/*     */         }
/*     */         
/* 629 */         replaceTop(7);
/*     */         return;
/*     */       
/*     */       case 1:
/* 633 */         replaceTop(2);
/* 634 */         newline();
/*     */         return;
/*     */       
/*     */       case 2:
/* 638 */         this.out.append(',');
/* 639 */         newline();
/*     */         return;
/*     */       
/*     */       case 4:
/* 643 */         this.out.append(this.separator);
/* 644 */         replaceTop(5);
/*     */         return;
/*     */     } 
/*     */     
/* 648 */     throw new IllegalStateException("Nesting problem.");
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\stream\JsonWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */