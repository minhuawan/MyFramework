/*      */ package com.badlogic.gdx.utils;
/*      */ 
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
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
/*      */ public class JsonValue
/*      */   implements Iterable<JsonValue>
/*      */ {
/*      */   private ValueType type;
/*      */   private String stringValue;
/*      */   private double doubleValue;
/*      */   private long longValue;
/*      */   public String name;
/*      */   public JsonValue child;
/*      */   public JsonValue next;
/*      */   public JsonValue prev;
/*      */   public JsonValue parent;
/*      */   public int size;
/*      */   
/*      */   public JsonValue(ValueType type) {
/*   51 */     this.type = type;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonValue(String value) {
/*   56 */     set(value);
/*      */   }
/*      */   
/*      */   public JsonValue(double value) {
/*   60 */     set(value, (String)null);
/*      */   }
/*      */   
/*      */   public JsonValue(long value) {
/*   64 */     set(value, (String)null);
/*      */   }
/*      */   
/*      */   public JsonValue(double value, String stringValue) {
/*   68 */     set(value, stringValue);
/*      */   }
/*      */   
/*      */   public JsonValue(long value, String stringValue) {
/*   72 */     set(value, stringValue);
/*      */   }
/*      */   
/*      */   public JsonValue(boolean value) {
/*   76 */     set(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue get(int index) {
/*   83 */     JsonValue current = this.child;
/*   84 */     while (current != null && index > 0) {
/*   85 */       index--;
/*   86 */       current = current.next;
/*      */     } 
/*   88 */     return current;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue get(String name) {
/*   94 */     JsonValue current = this.child;
/*   95 */     while (current != null && !current.name.equalsIgnoreCase(name))
/*   96 */       current = current.next; 
/*   97 */     return current;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean has(String name) {
/*  102 */     return (get(name) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue require(int index) {
/*  109 */     JsonValue current = this.child;
/*  110 */     while (current != null && index > 0) {
/*  111 */       index--;
/*  112 */       current = current.next;
/*      */     } 
/*  114 */     if (current == null) throw new IllegalArgumentException("Child not found with index: " + index); 
/*  115 */     return current;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue require(String name) {
/*  121 */     JsonValue current = this.child;
/*  122 */     while (current != null && !current.name.equalsIgnoreCase(name))
/*  123 */       current = current.next; 
/*  124 */     if (current == null) throw new IllegalArgumentException("Child not found with name: " + name); 
/*  125 */     return current;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue remove(int index) {
/*  132 */     JsonValue child = get(index);
/*  133 */     if (child == null) return null; 
/*  134 */     if (child.prev == null) {
/*  135 */       this.child = child.next;
/*  136 */       if (this.child != null) this.child.prev = null; 
/*      */     } else {
/*  138 */       child.prev.next = child.next;
/*  139 */       if (child.next != null) child.next.prev = child.prev; 
/*      */     } 
/*  141 */     this.size--;
/*  142 */     return child;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue remove(String name) {
/*  148 */     JsonValue child = get(name);
/*  149 */     if (child == null) return null; 
/*  150 */     if (child.prev == null) {
/*  151 */       this.child = child.next;
/*  152 */       if (this.child != null) this.child.prev = null; 
/*      */     } else {
/*  154 */       child.prev.next = child.next;
/*  155 */       if (child.next != null) child.next.prev = child.prev; 
/*      */     } 
/*  157 */     this.size--;
/*  158 */     return child;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int size() {
/*  164 */     return this.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String asString() {
/*  171 */     switch (this.type) {
/*      */       case stringValue:
/*  173 */         return this.stringValue;
/*      */       case doubleValue:
/*  175 */         return (this.stringValue != null) ? this.stringValue : Double.toString(this.doubleValue);
/*      */       case longValue:
/*  177 */         return (this.stringValue != null) ? this.stringValue : Long.toString(this.longValue);
/*      */       case booleanValue:
/*  179 */         return (this.longValue != 0L) ? "true" : "false";
/*      */       case nullValue:
/*  181 */         return null;
/*      */     } 
/*  183 */     throw new IllegalStateException("Value cannot be converted to string: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float asFloat() {
/*  189 */     switch (this.type) {
/*      */       case stringValue:
/*  191 */         return Float.parseFloat(this.stringValue);
/*      */       case doubleValue:
/*  193 */         return (float)this.doubleValue;
/*      */       case longValue:
/*  195 */         return (float)this.longValue;
/*      */       case booleanValue:
/*  197 */         return (this.longValue != 0L) ? 1.0F : 0.0F;
/*      */     } 
/*  199 */     throw new IllegalStateException("Value cannot be converted to float: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double asDouble() {
/*  205 */     switch (this.type) {
/*      */       case stringValue:
/*  207 */         return Double.parseDouble(this.stringValue);
/*      */       case doubleValue:
/*  209 */         return this.doubleValue;
/*      */       case longValue:
/*  211 */         return this.longValue;
/*      */       case booleanValue:
/*  213 */         return (this.longValue != 0L) ? 1.0D : 0.0D;
/*      */     } 
/*  215 */     throw new IllegalStateException("Value cannot be converted to double: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long asLong() {
/*  221 */     switch (this.type) {
/*      */       case stringValue:
/*  223 */         return Long.parseLong(this.stringValue);
/*      */       case doubleValue:
/*  225 */         return (long)this.doubleValue;
/*      */       case longValue:
/*  227 */         return this.longValue;
/*      */       case booleanValue:
/*  229 */         return (this.longValue != 0L) ? 1L : 0L;
/*      */     } 
/*  231 */     throw new IllegalStateException("Value cannot be converted to long: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int asInt() {
/*  237 */     switch (this.type) {
/*      */       case stringValue:
/*  239 */         return Integer.parseInt(this.stringValue);
/*      */       case doubleValue:
/*  241 */         return (int)this.doubleValue;
/*      */       case longValue:
/*  243 */         return (int)this.longValue;
/*      */       case booleanValue:
/*  245 */         return (this.longValue != 0L) ? 1 : 0;
/*      */     } 
/*  247 */     throw new IllegalStateException("Value cannot be converted to int: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean asBoolean() {
/*  253 */     switch (this.type) {
/*      */       case stringValue:
/*  255 */         return this.stringValue.equalsIgnoreCase("true");
/*      */       case doubleValue:
/*  257 */         return (this.doubleValue != 0.0D);
/*      */       case longValue:
/*  259 */         return (this.longValue != 0L);
/*      */       case booleanValue:
/*  261 */         return (this.longValue != 0L);
/*      */     } 
/*  263 */     throw new IllegalStateException("Value cannot be converted to boolean: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte asByte() {
/*  269 */     switch (this.type) {
/*      */       case stringValue:
/*  271 */         return Byte.parseByte(this.stringValue);
/*      */       case doubleValue:
/*  273 */         return (byte)(int)this.doubleValue;
/*      */       case longValue:
/*  275 */         return (byte)(int)this.longValue;
/*      */       case booleanValue:
/*  277 */         return (this.longValue != 0L) ? 1 : 0;
/*      */     } 
/*  279 */     throw new IllegalStateException("Value cannot be converted to byte: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short asShort() {
/*  285 */     switch (this.type) {
/*      */       case stringValue:
/*  287 */         return Short.parseShort(this.stringValue);
/*      */       case doubleValue:
/*  289 */         return (short)(int)this.doubleValue;
/*      */       case longValue:
/*  291 */         return (short)(int)this.longValue;
/*      */       case booleanValue:
/*  293 */         return (this.longValue != 0L) ? 1 : 0;
/*      */     } 
/*  295 */     throw new IllegalStateException("Value cannot be converted to short: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char asChar() {
/*  301 */     switch (this.type) {
/*      */       case stringValue:
/*  303 */         return (this.stringValue.length() == 0) ? Character.MIN_VALUE : this.stringValue.charAt(0);
/*      */       case doubleValue:
/*  305 */         return (char)(int)this.doubleValue;
/*      */       case longValue:
/*  307 */         return (char)(int)this.longValue;
/*      */       case booleanValue:
/*  309 */         return (this.longValue != 0L) ? '\001' : Character.MIN_VALUE;
/*      */     } 
/*  311 */     throw new IllegalStateException("Value cannot be converted to char: " + this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] asStringArray() {
/*  317 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  318 */     String[] array = new String[this.size];
/*  319 */     int i = 0;
/*  320 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       String v;
/*  322 */       switch (value.type) {
/*      */         case stringValue:
/*  324 */           v = value.stringValue;
/*      */           break;
/*      */         case doubleValue:
/*  327 */           v = (this.stringValue != null) ? this.stringValue : Double.toString(value.doubleValue);
/*      */           break;
/*      */         case longValue:
/*  330 */           v = (this.stringValue != null) ? this.stringValue : Long.toString(value.longValue);
/*      */           break;
/*      */         case booleanValue:
/*  333 */           v = (value.longValue != 0L) ? "true" : "false";
/*      */           break;
/*      */         case nullValue:
/*  336 */           v = null;
/*      */           break;
/*      */         default:
/*  339 */           throw new IllegalStateException("Value cannot be converted to string: " + value.type);
/*      */       } 
/*  341 */       array[i] = v;
/*      */     } 
/*  343 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] asFloatArray() {
/*  349 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  350 */     float[] array = new float[this.size];
/*  351 */     int i = 0;
/*  352 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       float v;
/*  354 */       switch (value.type) {
/*      */         case stringValue:
/*  356 */           v = Float.parseFloat(value.stringValue);
/*      */           break;
/*      */         case doubleValue:
/*  359 */           v = (float)value.doubleValue;
/*      */           break;
/*      */         case longValue:
/*  362 */           v = (float)value.longValue;
/*      */           break;
/*      */         case booleanValue:
/*  365 */           v = (value.longValue != 0L) ? 1.0F : 0.0F;
/*      */           break;
/*      */         default:
/*  368 */           throw new IllegalStateException("Value cannot be converted to float: " + value.type);
/*      */       } 
/*  370 */       array[i] = v;
/*      */     } 
/*  372 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double[] asDoubleArray() {
/*  378 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  379 */     double[] array = new double[this.size];
/*  380 */     int i = 0;
/*  381 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       double v;
/*  383 */       switch (value.type) {
/*      */         case stringValue:
/*  385 */           v = Double.parseDouble(value.stringValue);
/*      */           break;
/*      */         case doubleValue:
/*  388 */           v = value.doubleValue;
/*      */           break;
/*      */         case longValue:
/*  391 */           v = value.longValue;
/*      */           break;
/*      */         case booleanValue:
/*  394 */           v = (value.longValue != 0L) ? 1.0D : 0.0D;
/*      */           break;
/*      */         default:
/*  397 */           throw new IllegalStateException("Value cannot be converted to double: " + value.type);
/*      */       } 
/*  399 */       array[i] = v;
/*      */     } 
/*  401 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long[] asLongArray() {
/*  407 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  408 */     long[] array = new long[this.size];
/*  409 */     int i = 0;
/*  410 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       long v;
/*  412 */       switch (value.type) {
/*      */         case stringValue:
/*  414 */           v = Long.parseLong(value.stringValue);
/*      */           break;
/*      */         case doubleValue:
/*  417 */           v = (long)value.doubleValue;
/*      */           break;
/*      */         case longValue:
/*  420 */           v = value.longValue;
/*      */           break;
/*      */         case booleanValue:
/*  423 */           v = (value.longValue != 0L) ? 1L : 0L;
/*      */           break;
/*      */         default:
/*  426 */           throw new IllegalStateException("Value cannot be converted to long: " + value.type);
/*      */       } 
/*  428 */       array[i] = v;
/*      */     } 
/*  430 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] asIntArray() {
/*  436 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  437 */     int[] array = new int[this.size];
/*  438 */     int i = 0;
/*  439 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       int v;
/*  441 */       switch (value.type) {
/*      */         case stringValue:
/*  443 */           v = Integer.parseInt(value.stringValue);
/*      */           break;
/*      */         case doubleValue:
/*  446 */           v = (int)value.doubleValue;
/*      */           break;
/*      */         case longValue:
/*  449 */           v = (int)value.longValue;
/*      */           break;
/*      */         case booleanValue:
/*  452 */           v = (value.longValue != 0L) ? 1 : 0;
/*      */           break;
/*      */         default:
/*  455 */           throw new IllegalStateException("Value cannot be converted to int: " + value.type);
/*      */       } 
/*  457 */       array[i] = v;
/*      */     } 
/*  459 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean[] asBooleanArray() {
/*  465 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  466 */     boolean[] array = new boolean[this.size];
/*  467 */     int i = 0;
/*  468 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       boolean v;
/*  470 */       switch (value.type) {
/*      */         case stringValue:
/*  472 */           v = Boolean.parseBoolean(value.stringValue);
/*      */           break;
/*      */         case doubleValue:
/*  475 */           v = (value.doubleValue == 0.0D);
/*      */           break;
/*      */         case longValue:
/*  478 */           v = (value.longValue == 0L);
/*      */           break;
/*      */         case booleanValue:
/*  481 */           v = (value.longValue != 0L);
/*      */           break;
/*      */         default:
/*  484 */           throw new IllegalStateException("Value cannot be converted to boolean: " + value.type);
/*      */       } 
/*  486 */       array[i] = v;
/*      */     } 
/*  488 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] asByteArray() {
/*  494 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  495 */     byte[] array = new byte[this.size];
/*  496 */     int i = 0;
/*  497 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       byte v;
/*  499 */       switch (value.type) {
/*      */         case stringValue:
/*  501 */           v = Byte.parseByte(value.stringValue);
/*      */           break;
/*      */         case doubleValue:
/*  504 */           v = (byte)(int)value.doubleValue;
/*      */           break;
/*      */         case longValue:
/*  507 */           v = (byte)(int)value.longValue;
/*      */           break;
/*      */         case booleanValue:
/*  510 */           v = (value.longValue != 0L) ? 1 : 0;
/*      */           break;
/*      */         default:
/*  513 */           throw new IllegalStateException("Value cannot be converted to byte: " + value.type);
/*      */       } 
/*  515 */       array[i] = v;
/*      */     } 
/*  517 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short[] asShortArray() {
/*  523 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  524 */     short[] array = new short[this.size];
/*  525 */     int i = 0;
/*  526 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       short v;
/*  528 */       switch (value.type) {
/*      */         case stringValue:
/*  530 */           v = Short.parseShort(value.stringValue);
/*      */           break;
/*      */         case doubleValue:
/*  533 */           v = (short)(int)value.doubleValue;
/*      */           break;
/*      */         case longValue:
/*  536 */           v = (short)(int)value.longValue;
/*      */           break;
/*      */         case booleanValue:
/*  539 */           v = (value.longValue != 0L) ? 1 : 0;
/*      */           break;
/*      */         default:
/*  542 */           throw new IllegalStateException("Value cannot be converted to short: " + value.type);
/*      */       } 
/*  544 */       array[i] = v;
/*      */     } 
/*  546 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] asCharArray() {
/*  552 */     if (this.type != ValueType.array) throw new IllegalStateException("Value is not an array: " + this.type); 
/*  553 */     char[] array = new char[this.size];
/*  554 */     int i = 0;
/*  555 */     for (JsonValue value = this.child; value != null; value = value.next, i++) {
/*      */       char v;
/*  557 */       switch (value.type) {
/*      */         case stringValue:
/*  559 */           v = (value.stringValue.length() == 0) ? Character.MIN_VALUE : value.stringValue.charAt(0);
/*      */           break;
/*      */         case doubleValue:
/*  562 */           v = (char)(int)value.doubleValue;
/*      */           break;
/*      */         case longValue:
/*  565 */           v = (char)(int)value.longValue;
/*      */           break;
/*      */         case booleanValue:
/*  568 */           v = (value.longValue != 0L) ? '\001' : Character.MIN_VALUE;
/*      */           break;
/*      */         default:
/*  571 */           throw new IllegalStateException("Value cannot be converted to char: " + value.type);
/*      */       } 
/*  573 */       array[i] = v;
/*      */     } 
/*  575 */     return array;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasChild(String name) {
/*  580 */     return (getChild(name) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue getChild(String name) {
/*  586 */     JsonValue child = get(name);
/*  587 */     return (child == null) ? null : child.child;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String name, String defaultValue) {
/*  593 */     JsonValue child = get(name);
/*  594 */     return (child == null || !child.isValue() || child.isNull()) ? defaultValue : child.asString();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(String name, float defaultValue) {
/*  599 */     JsonValue child = get(name);
/*  600 */     return (child == null || !child.isValue()) ? defaultValue : child.asFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(String name, double defaultValue) {
/*  605 */     JsonValue child = get(name);
/*  606 */     return (child == null || !child.isValue()) ? defaultValue : child.asDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(String name, long defaultValue) {
/*  611 */     JsonValue child = get(name);
/*  612 */     return (child == null || !child.isValue()) ? defaultValue : child.asLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(String name, int defaultValue) {
/*  617 */     JsonValue child = get(name);
/*  618 */     return (child == null || !child.isValue()) ? defaultValue : child.asInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String name, boolean defaultValue) {
/*  623 */     JsonValue child = get(name);
/*  624 */     return (child == null || !child.isValue()) ? defaultValue : child.asBoolean();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(String name, byte defaultValue) {
/*  629 */     JsonValue child = get(name);
/*  630 */     return (child == null || !child.isValue()) ? defaultValue : child.asByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(String name, short defaultValue) {
/*  635 */     JsonValue child = get(name);
/*  636 */     return (child == null || !child.isValue()) ? defaultValue : child.asShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(String name, char defaultValue) {
/*  641 */     JsonValue child = get(name);
/*  642 */     return (child == null || !child.isValue()) ? defaultValue : child.asChar();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String name) {
/*  648 */     JsonValue child = get(name);
/*  649 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  650 */     return child.asString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(String name) {
/*  656 */     JsonValue child = get(name);
/*  657 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  658 */     return child.asFloat();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String name) {
/*  664 */     JsonValue child = get(name);
/*  665 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  666 */     return child.asDouble();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String name) {
/*  672 */     JsonValue child = get(name);
/*  673 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  674 */     return child.asLong();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String name) {
/*  680 */     JsonValue child = get(name);
/*  681 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  682 */     return child.asInt();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String name) {
/*  688 */     JsonValue child = get(name);
/*  689 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  690 */     return child.asBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(String name) {
/*  696 */     JsonValue child = get(name);
/*  697 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  698 */     return child.asByte();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(String name) {
/*  704 */     JsonValue child = get(name);
/*  705 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  706 */     return child.asShort();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char getChar(String name) {
/*  712 */     JsonValue child = get(name);
/*  713 */     if (child == null) throw new IllegalArgumentException("Named value not found: " + name); 
/*  714 */     return child.asChar();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(int index) {
/*  720 */     JsonValue child = get(index);
/*  721 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  722 */     return child.asString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/*  728 */     JsonValue child = get(index);
/*  729 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  730 */     return child.asFloat();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/*  736 */     JsonValue child = get(index);
/*  737 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  738 */     return child.asDouble();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  744 */     JsonValue child = get(index);
/*  745 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  746 */     return child.asLong();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  752 */     JsonValue child = get(index);
/*  753 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  754 */     return child.asInt();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  760 */     JsonValue child = get(index);
/*  761 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  762 */     return child.asBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  768 */     JsonValue child = get(index);
/*  769 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  770 */     return child.asByte();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  776 */     JsonValue child = get(index);
/*  777 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  778 */     return child.asShort();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/*  784 */     JsonValue child = get(index);
/*  785 */     if (child == null) throw new IllegalArgumentException("Indexed value not found: " + this.name); 
/*  786 */     return child.asChar();
/*      */   }
/*      */   
/*      */   public ValueType type() {
/*  790 */     return this.type;
/*      */   }
/*      */   
/*      */   public void setType(ValueType type) {
/*  794 */     if (type == null) throw new IllegalArgumentException("type cannot be null."); 
/*  795 */     this.type = type;
/*      */   }
/*      */   
/*      */   public boolean isArray() {
/*  799 */     return (this.type == ValueType.array);
/*      */   }
/*      */   
/*      */   public boolean isObject() {
/*  803 */     return (this.type == ValueType.object);
/*      */   }
/*      */   
/*      */   public boolean isString() {
/*  807 */     return (this.type == ValueType.stringValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNumber() {
/*  812 */     return (this.type == ValueType.doubleValue || this.type == ValueType.longValue);
/*      */   }
/*      */   
/*      */   public boolean isDouble() {
/*  816 */     return (this.type == ValueType.doubleValue);
/*      */   }
/*      */   
/*      */   public boolean isLong() {
/*  820 */     return (this.type == ValueType.longValue);
/*      */   }
/*      */   
/*      */   public boolean isBoolean() {
/*  824 */     return (this.type == ValueType.booleanValue);
/*      */   }
/*      */   
/*      */   public boolean isNull() {
/*  828 */     return (this.type == ValueType.nullValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isValue() {
/*  833 */     switch (this.type) {
/*      */       case stringValue:
/*      */       case doubleValue:
/*      */       case longValue:
/*      */       case booleanValue:
/*      */       case nullValue:
/*  839 */         return true;
/*      */     } 
/*  841 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String name() {
/*  847 */     return this.name;
/*      */   }
/*      */   
/*      */   public void setName(String name) {
/*  851 */     this.name = name;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue parent() {
/*  857 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue child() {
/*  863 */     return this.child;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addChild(String name, JsonValue value) {
/*  868 */     value.name = name;
/*  869 */     addChild(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addChild(JsonValue value) {
/*  874 */     value.parent = this;
/*  875 */     JsonValue current = this.child;
/*  876 */     if (current == null) {
/*  877 */       this.child = value;
/*      */     } else {
/*      */       while (true) {
/*  880 */         if (current.next == null) {
/*  881 */           current.next = value;
/*      */           return;
/*      */         } 
/*  884 */         current = current.next;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue next() {
/*  892 */     return this.next;
/*      */   }
/*      */   
/*      */   public void setNext(JsonValue next) {
/*  896 */     this.next = next;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonValue prev() {
/*  902 */     return this.prev;
/*      */   }
/*      */   
/*      */   public void setPrev(JsonValue prev) {
/*  906 */     this.prev = prev;
/*      */   }
/*      */ 
/*      */   
/*      */   public void set(String value) {
/*  911 */     this.stringValue = value;
/*  912 */     this.type = (value == null) ? ValueType.nullValue : ValueType.stringValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public void set(double value, String stringValue) {
/*  917 */     this.doubleValue = value;
/*  918 */     this.longValue = (long)value;
/*  919 */     this.stringValue = stringValue;
/*  920 */     this.type = ValueType.doubleValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public void set(long value, String stringValue) {
/*  925 */     this.longValue = value;
/*  926 */     this.doubleValue = value;
/*  927 */     this.stringValue = stringValue;
/*  928 */     this.type = ValueType.longValue;
/*      */   }
/*      */   
/*      */   public void set(boolean value) {
/*  932 */     this.longValue = value ? 1L : 0L;
/*  933 */     this.type = ValueType.booleanValue;
/*      */   }
/*      */   
/*      */   public String toJson(JsonWriter.OutputType outputType) {
/*  937 */     if (isValue()) return asString(); 
/*  938 */     StringBuilder buffer = new StringBuilder(512);
/*  939 */     json(this, buffer, outputType);
/*  940 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private void json(JsonValue object, StringBuilder buffer, JsonWriter.OutputType outputType) {
/*  944 */     if (object.isObject()) {
/*  945 */       if (object.child == null) {
/*  946 */         buffer.append("{}");
/*      */       } else {
/*  948 */         int start = buffer.length();
/*      */         
/*  950 */         buffer.append('{');
/*  951 */         int i = 0;
/*  952 */         for (JsonValue child = object.child; child != null; child = child.next) {
/*  953 */           buffer.append(outputType.quoteName(child.name));
/*  954 */           buffer.append(':');
/*  955 */           json(child, buffer, outputType);
/*  956 */           if (child.next != null) buffer.append(',');
/*      */         
/*      */         } 
/*      */         
/*  960 */         buffer.append('}');
/*      */       } 
/*  962 */     } else if (object.isArray()) {
/*  963 */       if (object.child == null) {
/*  964 */         buffer.append("[]");
/*      */       } else {
/*  966 */         int start = buffer.length();
/*      */         
/*  968 */         buffer.append('[');
/*  969 */         for (JsonValue child = object.child; child != null; child = child.next) {
/*  970 */           json(child, buffer, outputType);
/*  971 */           if (child.next != null) buffer.append(',');
/*      */         
/*      */         } 
/*      */         
/*  975 */         buffer.append(']');
/*      */       } 
/*  977 */     } else if (object.isString()) {
/*  978 */       buffer.append(outputType.quoteValue(object.asString()));
/*  979 */     } else if (object.isDouble()) {
/*  980 */       double doubleValue = object.asDouble();
/*  981 */       long longValue = object.asLong();
/*  982 */       buffer.append((doubleValue == longValue) ? longValue : doubleValue);
/*  983 */     } else if (object.isLong()) {
/*  984 */       buffer.append(object.asLong());
/*  985 */     } else if (object.isBoolean()) {
/*  986 */       buffer.append(object.asBoolean());
/*  987 */     } else if (object.isNull()) {
/*  988 */       buffer.append("null");
/*      */     } else {
/*  990 */       throw new SerializationException("Unknown object type: " + object);
/*      */     } 
/*      */   }
/*      */   public String toString() {
/*  994 */     if (isValue()) return (this.name == null) ? asString() : (this.name + ": " + asString()); 
/*  995 */     return ((this.name == null) ? "" : (this.name + ": ")) + prettyPrint(JsonWriter.OutputType.minimal, 0);
/*      */   }
/*      */   
/*      */   public String prettyPrint(JsonWriter.OutputType outputType, int singleLineColumns) {
/*  999 */     PrettyPrintSettings settings = new PrettyPrintSettings();
/* 1000 */     settings.outputType = outputType;
/* 1001 */     settings.singleLineColumns = singleLineColumns;
/* 1002 */     return prettyPrint(settings);
/*      */   }
/*      */   
/*      */   public String prettyPrint(PrettyPrintSettings settings) {
/* 1006 */     StringBuilder buffer = new StringBuilder(512);
/* 1007 */     prettyPrint(this, buffer, 0, settings);
/* 1008 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private void prettyPrint(JsonValue object, StringBuilder buffer, int indent, PrettyPrintSettings settings) {
/* 1012 */     JsonWriter.OutputType outputType = settings.outputType;
/* 1013 */     if (object.isObject()) {
/* 1014 */       if (object.child == null) {
/* 1015 */         buffer.append("{}");
/*      */       } else {
/* 1017 */         boolean newLines = !isFlat(object);
/* 1018 */         int start = buffer.length();
/*      */         
/*      */         label103: while (true) {
/* 1021 */           buffer.append(newLines ? "{\n" : "{ ");
/* 1022 */           int i = 0;
/* 1023 */           for (JsonValue child = object.child; child != null; child = child.next) {
/* 1024 */             if (newLines) indent(indent, buffer); 
/* 1025 */             buffer.append(outputType.quoteName(child.name));
/* 1026 */             buffer.append(": ");
/* 1027 */             prettyPrint(child, buffer, indent + 1, settings);
/* 1028 */             if ((!newLines || outputType != JsonWriter.OutputType.minimal) && child.next != null) buffer.append(','); 
/* 1029 */             buffer.append(newLines ? 10 : 32);
/* 1030 */             if (!newLines && buffer.length() - start > settings.singleLineColumns) {
/* 1031 */               buffer.setLength(start);
/* 1032 */               newLines = true;
/*      */               continue label103;
/*      */             } 
/*      */           } 
/*      */           break;
/*      */         } 
/* 1038 */         if (newLines) indent(indent - 1, buffer); 
/* 1039 */         buffer.append('}');
/*      */       } 
/* 1041 */     } else if (object.isArray()) {
/* 1042 */       if (object.child == null) {
/* 1043 */         buffer.append("[]");
/*      */       } else {
/* 1045 */         boolean newLines = !isFlat(object);
/* 1046 */         boolean wrap = (settings.wrapNumericArrays || !isNumeric(object));
/* 1047 */         int start = buffer.length();
/*      */         
/*      */         label104: while (true) {
/* 1050 */           buffer.append(newLines ? "[\n" : "[ ");
/* 1051 */           for (JsonValue child = object.child; child != null; child = child.next) {
/* 1052 */             if (newLines) indent(indent, buffer); 
/* 1053 */             prettyPrint(child, buffer, indent + 1, settings);
/* 1054 */             if ((!newLines || outputType != JsonWriter.OutputType.minimal) && child.next != null) buffer.append(','); 
/* 1055 */             buffer.append(newLines ? 10 : 32);
/* 1056 */             if (wrap && !newLines && buffer.length() - start > settings.singleLineColumns) {
/* 1057 */               buffer.setLength(start);
/* 1058 */               newLines = true;
/*      */               continue label104;
/*      */             } 
/*      */           } 
/*      */           break;
/*      */         } 
/* 1064 */         if (newLines) indent(indent - 1, buffer); 
/* 1065 */         buffer.append(']');
/*      */       } 
/* 1067 */     } else if (object.isString()) {
/* 1068 */       buffer.append(outputType.quoteValue(object.asString()));
/* 1069 */     } else if (object.isDouble()) {
/* 1070 */       double doubleValue = object.asDouble();
/* 1071 */       long longValue = object.asLong();
/* 1072 */       buffer.append((doubleValue == longValue) ? longValue : doubleValue);
/* 1073 */     } else if (object.isLong()) {
/* 1074 */       buffer.append(object.asLong());
/* 1075 */     } else if (object.isBoolean()) {
/* 1076 */       buffer.append(object.asBoolean());
/* 1077 */     } else if (object.isNull()) {
/* 1078 */       buffer.append("null");
/*      */     } else {
/* 1080 */       throw new SerializationException("Unknown object type: " + object);
/*      */     } 
/*      */   }
/*      */   private static boolean isFlat(JsonValue object) {
/* 1084 */     for (JsonValue child = object.child; child != null; child = child.next) {
/* 1085 */       if (child.isObject() || child.isArray()) return false; 
/* 1086 */     }  return true;
/*      */   }
/*      */   
/*      */   private static boolean isNumeric(JsonValue object) {
/* 1090 */     for (JsonValue child = object.child; child != null; child = child.next) {
/* 1091 */       if (!child.isNumber()) return false; 
/* 1092 */     }  return true;
/*      */   }
/*      */   
/*      */   private static void indent(int count, StringBuilder buffer) {
/* 1096 */     for (int i = 0; i < count; i++)
/* 1097 */       buffer.append('\t'); 
/*      */   }
/*      */   
/*      */   public enum ValueType {
/* 1101 */     object, array, stringValue, doubleValue, longValue, booleanValue, nullValue;
/*      */   }
/*      */   
/*      */   public JsonIterator iterator() {
/* 1105 */     return new JsonIterator();
/*      */   }
/*      */   
/*      */   public class JsonIterator implements Iterator<JsonValue>, Iterable<JsonValue> {
/* 1109 */     JsonValue entry = JsonValue.this.child;
/*      */     JsonValue current;
/*      */     
/*      */     public boolean hasNext() {
/* 1113 */       return (this.entry != null);
/*      */     }
/*      */     
/*      */     public JsonValue next() {
/* 1117 */       this.current = this.entry;
/* 1118 */       if (this.current == null) throw new NoSuchElementException(); 
/* 1119 */       this.entry = this.current.next;
/* 1120 */       return this.current;
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1124 */       if (this.current.prev == null) {
/* 1125 */         JsonValue.this.child = this.current.next;
/* 1126 */         if (JsonValue.this.child != null) JsonValue.this.child.prev = null; 
/*      */       } else {
/* 1128 */         this.current.prev.next = this.current.next;
/* 1129 */         if (this.current.next != null) this.current.next.prev = this.current.prev; 
/*      */       } 
/* 1131 */       JsonValue.this.size--;
/*      */     }
/*      */     
/*      */     public Iterator<JsonValue> iterator() {
/* 1135 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   public String trace() {
/*      */     String trace;
/* 1141 */     if (this.parent == null) {
/* 1142 */       if (this.type == ValueType.array) return "[]"; 
/* 1143 */       if (this.type == ValueType.object) return "{}"; 
/* 1144 */       return "";
/*      */     } 
/*      */     
/* 1147 */     if (this.parent.type == ValueType.array) {
/* 1148 */       trace = "[]";
/* 1149 */       int i = 0;
/* 1150 */       for (JsonValue child = this.parent.child; child != null; child = child.next, i++) {
/* 1151 */         if (child == this) {
/* 1152 */           trace = "[" + i + "]";
/*      */           break;
/*      */         } 
/*      */       } 
/* 1156 */     } else if (this.name.indexOf('.') != -1) {
/* 1157 */       trace = ".\"" + this.name.replace("\"", "\\\"") + "\"";
/*      */     } else {
/* 1159 */       trace = '.' + this.name;
/* 1160 */     }  return this.parent.trace() + trace;
/*      */   }
/*      */   
/*      */   public static class PrettyPrintSettings {
/*      */     public JsonWriter.OutputType outputType;
/*      */     public int singleLineColumns;
/*      */     public boolean wrapNumericArrays;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\JsonValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */