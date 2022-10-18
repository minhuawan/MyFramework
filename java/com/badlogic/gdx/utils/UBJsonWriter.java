/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UBJsonWriter
/*     */   implements Closeable
/*     */ {
/*     */   final DataOutputStream out;
/*     */   private JsonObject current;
/*     */   private boolean named;
/*  32 */   private final Array<JsonObject> stack = new Array<JsonObject>();
/*     */   
/*     */   public UBJsonWriter(OutputStream out) {
/*  35 */     if (!(out instanceof DataOutputStream)) out = new DataOutputStream(out); 
/*  36 */     this.out = (DataOutputStream)out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter object() throws IOException {
/*  42 */     if (this.current != null && 
/*  43 */       !this.current.array) {
/*  44 */       if (!this.named) throw new IllegalStateException("Name must be set."); 
/*  45 */       this.named = false;
/*     */     } 
/*     */     
/*  48 */     this.stack.add(this.current = new JsonObject(false));
/*  49 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter object(String name) throws IOException {
/*  55 */     name(name).object();
/*  56 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter array() throws IOException {
/*  62 */     if (this.current != null && 
/*  63 */       !this.current.array) {
/*  64 */       if (!this.named) throw new IllegalStateException("Name must be set."); 
/*  65 */       this.named = false;
/*     */     } 
/*     */     
/*  68 */     this.stack.add(this.current = new JsonObject(true));
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter array(String name) throws IOException {
/*  75 */     name(name).array();
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter name(String name) throws IOException {
/*  82 */     if (this.current == null || this.current.array) throw new IllegalStateException("Current item must be an object."); 
/*  83 */     byte[] bytes = name.getBytes("UTF-8");
/*  84 */     if (bytes.length <= 127) {
/*  85 */       this.out.writeByte(105);
/*  86 */       this.out.writeByte(bytes.length);
/*  87 */     } else if (bytes.length <= 32767) {
/*  88 */       this.out.writeByte(73);
/*  89 */       this.out.writeShort(bytes.length);
/*     */     } else {
/*  91 */       this.out.writeByte(108);
/*  92 */       this.out.writeInt(bytes.length);
/*     */     } 
/*  94 */     this.out.write(bytes);
/*  95 */     this.named = true;
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(byte value) throws IOException {
/* 102 */     checkName();
/* 103 */     this.out.writeByte(105);
/* 104 */     this.out.writeByte(value);
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(short value) throws IOException {
/* 111 */     checkName();
/* 112 */     this.out.writeByte(73);
/* 113 */     this.out.writeShort(value);
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(int value) throws IOException {
/* 120 */     checkName();
/* 121 */     this.out.writeByte(108);
/* 122 */     this.out.writeInt(value);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(long value) throws IOException {
/* 129 */     checkName();
/* 130 */     this.out.writeByte(76);
/* 131 */     this.out.writeLong(value);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(float value) throws IOException {
/* 138 */     checkName();
/* 139 */     this.out.writeByte(100);
/* 140 */     this.out.writeFloat(value);
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(double value) throws IOException {
/* 148 */     checkName();
/* 149 */     this.out.writeByte(68);
/* 150 */     this.out.writeDouble(value);
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(boolean value) throws IOException {
/* 158 */     checkName();
/* 159 */     this.out.writeByte(value ? 84 : 70);
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(char value) throws IOException {
/* 167 */     checkName();
/* 168 */     this.out.writeByte(73);
/* 169 */     this.out.writeChar(value);
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(String value) throws IOException {
/* 176 */     checkName();
/* 177 */     byte[] bytes = value.getBytes("UTF-8");
/* 178 */     this.out.writeByte(83);
/* 179 */     if (bytes.length <= 127) {
/* 180 */       this.out.writeByte(105);
/* 181 */       this.out.writeByte(bytes.length);
/* 182 */     } else if (bytes.length <= 32767) {
/* 183 */       this.out.writeByte(73);
/* 184 */       this.out.writeShort(bytes.length);
/*     */     } else {
/* 186 */       this.out.writeByte(108);
/* 187 */       this.out.writeInt(bytes.length);
/*     */     } 
/* 189 */     this.out.write(bytes);
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(byte[] values) throws IOException {
/* 197 */     array();
/* 198 */     this.out.writeByte(36);
/* 199 */     this.out.writeByte(105);
/* 200 */     this.out.writeByte(35);
/* 201 */     value(values.length);
/* 202 */     for (int i = 0, n = values.length; i < n; i++) {
/* 203 */       this.out.writeByte(values[i]);
/*     */     }
/* 205 */     pop(true);
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(short[] values) throws IOException {
/* 213 */     array();
/* 214 */     this.out.writeByte(36);
/* 215 */     this.out.writeByte(73);
/* 216 */     this.out.writeByte(35);
/* 217 */     value(values.length);
/* 218 */     for (int i = 0, n = values.length; i < n; i++) {
/* 219 */       this.out.writeShort(values[i]);
/*     */     }
/* 221 */     pop(true);
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(int[] values) throws IOException {
/* 229 */     array();
/* 230 */     this.out.writeByte(36);
/* 231 */     this.out.writeByte(108);
/* 232 */     this.out.writeByte(35);
/* 233 */     value(values.length);
/* 234 */     for (int i = 0, n = values.length; i < n; i++) {
/* 235 */       this.out.writeInt(values[i]);
/*     */     }
/* 237 */     pop(true);
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(long[] values) throws IOException {
/* 245 */     array();
/* 246 */     this.out.writeByte(36);
/* 247 */     this.out.writeByte(76);
/* 248 */     this.out.writeByte(35);
/* 249 */     value(values.length);
/* 250 */     for (int i = 0, n = values.length; i < n; i++) {
/* 251 */       this.out.writeLong(values[i]);
/*     */     }
/* 253 */     pop(true);
/* 254 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(float[] values) throws IOException {
/* 261 */     array();
/* 262 */     this.out.writeByte(36);
/* 263 */     this.out.writeByte(100);
/* 264 */     this.out.writeByte(35);
/* 265 */     value(values.length);
/* 266 */     for (int i = 0, n = values.length; i < n; i++) {
/* 267 */       this.out.writeFloat(values[i]);
/*     */     }
/* 269 */     pop(true);
/* 270 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(double[] values) throws IOException {
/* 278 */     array();
/* 279 */     this.out.writeByte(36);
/* 280 */     this.out.writeByte(68);
/* 281 */     this.out.writeByte(35);
/* 282 */     value(values.length);
/* 283 */     for (int i = 0, n = values.length; i < n; i++) {
/* 284 */       this.out.writeDouble(values[i]);
/*     */     }
/* 286 */     pop(true);
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(boolean[] values) throws IOException {
/* 293 */     array();
/* 294 */     for (int i = 0, n = values.length; i < n; i++) {
/* 295 */       this.out.writeByte(values[i] ? 84 : 70);
/*     */     }
/* 297 */     pop();
/* 298 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(char[] values) throws IOException {
/* 305 */     array();
/* 306 */     this.out.writeByte(36);
/* 307 */     this.out.writeByte(67);
/* 308 */     this.out.writeByte(35);
/* 309 */     value(values.length);
/* 310 */     for (int i = 0, n = values.length; i < n; i++) {
/* 311 */       this.out.writeChar(values[i]);
/*     */     }
/* 313 */     pop(true);
/* 314 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(String[] values) throws IOException {
/* 321 */     array();
/* 322 */     this.out.writeByte(36);
/* 323 */     this.out.writeByte(83);
/* 324 */     this.out.writeByte(35);
/* 325 */     value(values.length);
/* 326 */     for (int i = 0, n = values.length; i < n; i++) {
/* 327 */       byte[] bytes = values[i].getBytes("UTF-8");
/* 328 */       if (bytes.length <= 127) {
/* 329 */         this.out.writeByte(105);
/* 330 */         this.out.writeByte(bytes.length);
/* 331 */       } else if (bytes.length <= 32767) {
/* 332 */         this.out.writeByte(73);
/* 333 */         this.out.writeShort(bytes.length);
/*     */       } else {
/* 335 */         this.out.writeByte(108);
/* 336 */         this.out.writeInt(bytes.length);
/*     */       } 
/* 338 */       this.out.write(bytes);
/*     */     } 
/* 340 */     pop(true);
/* 341 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(JsonValue value) throws IOException {
/* 347 */     if (value.isObject()) {
/* 348 */       if (value.name != null) {
/* 349 */         object(value.name);
/*     */       } else {
/* 351 */         object();
/* 352 */       }  for (JsonValue child = value.child; child != null; child = child.next)
/* 353 */         value(child); 
/* 354 */       pop();
/* 355 */     } else if (value.isArray()) {
/* 356 */       if (value.name != null) {
/* 357 */         array(value.name);
/*     */       } else {
/* 359 */         array();
/* 360 */       }  for (JsonValue child = value.child; child != null; child = child.next)
/* 361 */         value(child); 
/* 362 */       pop();
/* 363 */     } else if (value.isBoolean()) {
/* 364 */       if (value.name != null) name(value.name); 
/* 365 */       value(value.asBoolean());
/* 366 */     } else if (value.isDouble()) {
/* 367 */       if (value.name != null) name(value.name); 
/* 368 */       value(value.asDouble());
/* 369 */     } else if (value.isLong()) {
/* 370 */       if (value.name != null) name(value.name); 
/* 371 */       value(value.asLong());
/* 372 */     } else if (value.isString()) {
/* 373 */       if (value.name != null) name(value.name); 
/* 374 */       value(value.asString());
/* 375 */     } else if (value.isNull()) {
/* 376 */       if (value.name != null) name(value.name); 
/* 377 */       value();
/*     */     } else {
/* 379 */       throw new IOException("Unhandled JsonValue type");
/*     */     } 
/* 381 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value(Object object) throws IOException {
/* 388 */     if (object == null)
/* 389 */       return value(); 
/* 390 */     if (object instanceof Number)
/* 391 */     { Number number = (Number)object;
/* 392 */       if (object instanceof Byte) return value(number.byteValue()); 
/* 393 */       if (object instanceof Short) return value(number.shortValue()); 
/* 394 */       if (object instanceof Integer) return value(number.intValue()); 
/* 395 */       if (object instanceof Long) return value(number.longValue()); 
/* 396 */       if (object instanceof Float) return value(number.floatValue()); 
/* 397 */       if (object instanceof Double) return value(number.doubleValue());  }
/* 398 */     else { if (object instanceof Character)
/* 399 */         return value(((Character)object).charValue()); 
/* 400 */       if (object instanceof CharSequence) {
/* 401 */         return value(object.toString());
/*     */       }
/* 403 */       throw new IOException("Unknown object type."); }
/*     */     
/* 405 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter value() throws IOException {
/* 411 */     checkName();
/* 412 */     this.out.writeByte(90);
/* 413 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, byte value) throws IOException {
/* 419 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, short value) throws IOException {
/* 425 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, int value) throws IOException {
/* 431 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, long value) throws IOException {
/* 437 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, float value) throws IOException {
/* 443 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, double value) throws IOException {
/* 449 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, boolean value) throws IOException {
/* 455 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, char value) throws IOException {
/* 461 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, String value) throws IOException {
/* 467 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, byte[] value) throws IOException {
/* 473 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, short[] value) throws IOException {
/* 479 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, int[] value) throws IOException {
/* 485 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, long[] value) throws IOException {
/* 491 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, float[] value) throws IOException {
/* 497 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, double[] value) throws IOException {
/* 503 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, boolean[] value) throws IOException {
/* 509 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, char[] value) throws IOException {
/* 515 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name, String[] value) throws IOException {
/* 521 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter set(String name) throws IOException {
/* 527 */     return name(name).value();
/*     */   }
/*     */   
/*     */   private void checkName() {
/* 531 */     if (this.current != null && 
/* 532 */       !this.current.array) {
/* 533 */       if (!this.named) throw new IllegalStateException("Name must be set."); 
/* 534 */       this.named = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UBJsonWriter pop() throws IOException {
/* 542 */     return pop(false);
/*     */   }
/*     */   
/*     */   protected UBJsonWriter pop(boolean silent) throws IOException {
/* 546 */     if (this.named) throw new IllegalStateException("Expected an object, array, or value since a name was set."); 
/* 547 */     if (silent) {
/* 548 */       this.stack.pop();
/*     */     } else {
/* 550 */       ((JsonObject)this.stack.pop()).close();
/* 551 */     }  this.current = (this.stack.size == 0) ? null : this.stack.peek();
/* 552 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 557 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 562 */     while (this.stack.size > 0)
/* 563 */       pop(); 
/* 564 */     this.out.close();
/*     */   }
/*     */   
/*     */   private class JsonObject {
/*     */     final boolean array;
/*     */     
/*     */     JsonObject(boolean array) throws IOException {
/* 571 */       this.array = array;
/* 572 */       UBJsonWriter.this.out.writeByte(array ? 91 : 123);
/*     */     }
/*     */     
/*     */     void close() throws IOException {
/* 576 */       UBJsonWriter.this.out.writeByte(this.array ? 93 : 125);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\UBJsonWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */