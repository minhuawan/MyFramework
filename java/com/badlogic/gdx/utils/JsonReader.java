/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonReader
/*     */   implements BaseJsonReader
/*     */ {
/*     */   public JsonValue parse(String json) {
/*  37 */     char[] data = json.toCharArray();
/*  38 */     return parse(data, 0, data.length);
/*     */   }
/*     */   
/*     */   public JsonValue parse(Reader reader) {
/*     */     try {
/*  43 */       char[] data = new char[1024];
/*  44 */       int offset = 0;
/*     */       while (true) {
/*  46 */         int length = reader.read(data, offset, data.length - offset);
/*  47 */         if (length == -1)
/*  48 */           break;  if (length == 0) {
/*  49 */           char[] newData = new char[data.length * 2];
/*  50 */           System.arraycopy(data, 0, newData, 0, data.length);
/*  51 */           data = newData; continue;
/*     */         } 
/*  53 */         offset += length;
/*     */       } 
/*  55 */       return parse(data, 0, offset);
/*  56 */     } catch (IOException ex) {
/*  57 */       throw new SerializationException(ex);
/*     */     } finally {
/*  59 */       StreamUtils.closeQuietly(reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public JsonValue parse(InputStream input) {
/*     */     try {
/*  65 */       return parse(new InputStreamReader(input, "UTF-8"));
/*  66 */     } catch (IOException ex) {
/*  67 */       throw new SerializationException(ex);
/*     */     } finally {
/*  69 */       StreamUtils.closeQuietly(input);
/*     */     } 
/*     */   }
/*     */   
/*     */   public JsonValue parse(FileHandle file) {
/*     */     try {
/*  75 */       return parse(file.reader("UTF-8"));
/*  76 */     } catch (Exception ex) {
/*  77 */       throw new SerializationException("Error parsing file: " + file, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public JsonValue parse(char[] data, int offset, int length) {
/*  82 */     int p = offset, pe = length, eof = pe, top = 0;
/*  83 */     int[] stack = new int[4];
/*     */     
/*  85 */     int s = 0;
/*  86 */     Array<String> names = new Array<String>(8);
/*  87 */     boolean needsUnescape = false, stringIsName = false, stringIsUnquoted = false;
/*  88 */     RuntimeException parseRuntimeEx = null;
/*     */     
/*  90 */     boolean debug = false;
/*  91 */     if (debug) System.out.println();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  97 */       int cs = 1;
/*  98 */       top = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       int _trans = 0;
/*     */ 
/*     */ 
/*     */       
/* 108 */       int _goto_targ = 0;
/*     */       label308: while (true) {
/*     */         int _klen;
/*     */         int _keys;
/* 112 */         switch (_goto_targ) {
/*     */           case 0:
/* 114 */             if (p == pe) {
/* 115 */               _goto_targ = 4;
/*     */               continue;
/*     */             } 
/* 118 */             if (cs == 0) {
/* 119 */               _goto_targ = 5;
/*     */               continue;
/*     */             } 
/*     */ 
/*     */           
/*     */           case 1:
/* 125 */             _keys = _json_key_offsets[cs];
/* 126 */             _trans = _json_index_offsets[cs];
/* 127 */             _klen = _json_single_lengths[cs];
/* 128 */             if (_klen > 0) {
/* 129 */               int _lower = _keys;
/*     */               
/* 131 */               int _upper = _keys + _klen - 1;
/*     */               
/* 133 */               while (_upper >= _lower) {
/*     */                 
/* 135 */                 int _mid = _lower + (_upper - _lower >> 1);
/* 136 */                 if (data[p] < _json_trans_keys[_mid]) {
/* 137 */                   _upper = _mid - 1; continue;
/* 138 */                 }  if (data[p] > _json_trans_keys[_mid]) {
/* 139 */                   _lower = _mid + 1; continue;
/*     */                 } 
/* 141 */                 _trans += _mid - _keys;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 171 */                 _trans = _json_indicies[_trans];
/* 172 */                 cs = _json_trans_targs[_trans];
/*     */                 
/* 174 */                 if (_json_trans_actions[_trans] != 0)
/* 175 */                 { int _acts = _json_trans_actions[_trans];
/* 176 */                   int _nacts = _json_actions[_acts++]; while (true)
/* 177 */                   { if (_nacts-- > 0)
/* 178 */                     { String value; String name; int start; switch (_json_actions[_acts++])
/*     */                       
/*     */                       { 
/*     */                         case 0:
/* 182 */                           stringIsName = true;
/*     */                           continue;
/*     */ 
/*     */ 
/*     */                         
/*     */                         case 1:
/* 188 */                           value = new String(data, s, p - s);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         case 2:
/* 265 */                           name = (names.size > 0) ? names.pop() : null;
/* 266 */                           if (debug) System.out.println("startObject: " + name); 
/* 267 */                           startObject(name);
/*     */                           
/* 269 */                           if (top == stack.length) {
/* 270 */                             int[] newStack = new int[stack.length * 2];
/* 271 */                             System.arraycopy(stack, 0, newStack, 0, stack.length);
/* 272 */                             stack = newStack;
/*     */                           } 
/*     */                           
/* 275 */                           stack[top++] = cs;
/* 276 */                           cs = 5;
/* 277 */                           _goto_targ = 2;
/*     */                           continue label308;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         case 3:
/* 286 */                           if (debug) System.out.println("endObject"); 
/* 287 */                           pop();
/*     */                           
/* 289 */                           cs = stack[--top];
/* 290 */                           _goto_targ = 2;
/*     */                           continue label308;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         case 4:
/* 298 */                           name = (names.size > 0) ? names.pop() : null;
/* 299 */                           if (debug) System.out.println("startArray: " + name); 
/* 300 */                           startArray(name);
/*     */                           
/* 302 */                           if (top == stack.length) {
/* 303 */                             int[] newStack = new int[stack.length * 2];
/* 304 */                             System.arraycopy(stack, 0, newStack, 0, stack.length);
/* 305 */                             stack = newStack;
/*     */                           } 
/*     */                           
/* 308 */                           stack[top++] = cs;
/* 309 */                           cs = 23;
/* 310 */                           _goto_targ = 2;
/*     */                           continue label308;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         case 5:
/* 319 */                           if (debug) System.out.println("endArray"); 
/* 320 */                           pop();
/*     */                           
/* 322 */                           cs = stack[--top];
/* 323 */                           _goto_targ = 2;
/*     */                           continue label308;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         case 6:
/* 331 */                           start = p - 1;
/* 332 */                           if (data[p++] == '/') {
/* 333 */                             while (p != eof && data[p] != '\n')
/* 334 */                               p++; 
/* 335 */                             p--;
/*     */                           } else {
/* 337 */                             while ((p + 1 < eof && data[p] != '*') || data[p + 1] != '/')
/* 338 */                               p++; 
/* 339 */                             p++;
/*     */                           } 
/* 341 */                           if (debug) System.out.println("comment " + new String(data, start, p - start));
/*     */                           
/*     */                           continue;
/*     */ 
/*     */                         
/*     */                         case 7:
/* 347 */                           if (debug) System.out.println("unquotedChars"); 
/* 348 */                           s = p;
/* 349 */                           needsUnescape = false;
/* 350 */                           stringIsUnquoted = true;
/* 351 */                           if (stringIsName) {
/*     */                             do {
/*     */                               char c;
/* 354 */                               switch (data[p]) {
/*     */                                 case '\\':
/* 356 */                                   needsUnescape = true;
/*     */                                   break;
/*     */                                 case '/':
/* 359 */                                   if (p + 1 == eof)
/* 360 */                                     break;  c = data[p + 1];
/* 361 */                                   if (c == '/' || c == '*')
/*     */                                     break;  break;
/*     */                                 case '\n':
/*     */                                 case '\r':
/*     */                                 case ':':
/*     */                                   break;
/*     */                               } 
/* 368 */                               if (!debug) continue;  System.out.println("unquotedChar (name): '" + data[p] + "'");
/* 369 */                               ++p;
/* 370 */                             } while (p != eof);
/*     */                           } else {
/*     */                             do {
/*     */                               char c;
/*     */                               
/* 375 */                               switch (data[p]) {
/*     */                                 case '\\':
/* 377 */                                   needsUnescape = true;
/*     */                                   break;
/*     */                                 case '/':
/* 380 */                                   if (p + 1 == eof)
/* 381 */                                     break;  c = data[p + 1];
/* 382 */                                   if (c == '/' || c == '*')
/*     */                                     break;  break;
/*     */                                 case '\n':
/*     */                                 case '\r':
/*     */                                 case ',':
/*     */                                 case ']':
/*     */                                 case '}':
/*     */                                   break;
/*     */                               } 
/* 391 */                               if (!debug) continue;  System.out.println("unquotedChar (value): '" + data[p] + "'");
/* 392 */                               ++p;
/* 393 */                             } while (p != eof);
/*     */                           } 
/*     */                           
/* 396 */                           p--;
/* 397 */                           while (Character.isSpace(data[p])) {
/* 398 */                             p--;
/*     */                           }
/*     */                           continue;
/*     */ 
/*     */                         
/*     */                         case 8:
/* 404 */                           if (debug) System.out.println("quotedChars"); 
/* 405 */                           s = ++p;
/* 406 */                           needsUnescape = false;
/*     */                           
/*     */                           do {
/* 409 */                             switch (data[p]) {
/*     */                               case '\\':
/* 411 */                                 needsUnescape = true;
/* 412 */                                 p++;
/*     */                                 break;
/*     */                               
/*     */                               case '"':
/*     */                                 break;
/*     */                             } 
/* 418 */                             ++p;
/* 419 */                           } while (p != eof);
/*     */                           
/* 421 */                           p--; continue; }  continue; }  continue label308; }  } else { continue; } 
/*     */               }  _keys += _klen; _trans += _klen;
/*     */             }  _klen = _json_range_lengths[cs]; if (_klen > 0) { int _lower = _keys; int _upper = _keys + (_klen << 1) - 2; while (true) { if (_upper < _lower) { _trans += _klen; break; }
/*     */                  int _mid = _lower + (_upper - _lower >> 1 & 0xFFFFFFFE); if (data[p] < _json_trans_keys[_mid]) { _upper = _mid - 2; continue; }
/*     */                  if (data[p] > _json_trans_keys[_mid + 1]) { _lower = _mid + 2; continue; }
/*     */                  _trans += _mid - _keys >> 1; break; }
/*     */                continue; }
/*     */              continue;
/*     */           case 2:
/* 430 */             if (cs == 0) {
/* 431 */               _goto_targ = 5;
/*     */               continue;
/*     */             } 
/* 434 */             if (++p != pe) {
/* 435 */               _goto_targ = 1;
/*     */               continue;
/*     */             } 
/*     */           case 4:
/* 439 */             if (p == eof) {
/* 440 */               int __acts = _json_eof_actions[cs];
/* 441 */               int __nacts = _json_actions[__acts++];
/* 442 */               while (__nacts-- > 0) {
/* 443 */                 String value; switch (_json_actions[__acts++]) {
/*     */ 
/*     */                   
/*     */                   case 1:
/* 447 */                     value = new String(data, s, p - s);
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               } 
/*     */             } 
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/* 534 */     } catch (RuntimeException ex) {
/* 535 */       parseRuntimeEx = ex;
/*     */     } 
/*     */     
/* 538 */     JsonValue root = this.root;
/* 539 */     this.root = null;
/* 540 */     this.current = null;
/* 541 */     this.lastChild.clear();
/*     */     
/* 543 */     if (p < pe) {
/* 544 */       int lineNumber = 1;
/* 545 */       for (int i = 0; i < p; i++) {
/* 546 */         if (data[i] == '\n') lineNumber++; 
/* 547 */       }  int start = Math.max(0, p - 32);
/* 548 */       throw new SerializationException("Error parsing JSON on line " + lineNumber + " near: " + new String(data, start, p - start) + "*ERROR*" + new String(data, p, 
/* 549 */             Math.min(64, pe - p)), parseRuntimeEx);
/* 550 */     }  if (this.elements.size != 0) {
/* 551 */       JsonValue element = this.elements.peek();
/* 552 */       this.elements.clear();
/* 553 */       if (element != null && element.isObject()) {
/* 554 */         throw new SerializationException("Error parsing JSON, unmatched brace.");
/*     */       }
/* 556 */       throw new SerializationException("Error parsing JSON, unmatched bracket.");
/* 557 */     }  if (parseRuntimeEx != null) {
/* 558 */       throw new SerializationException("Error parsing JSON: " + new String(data), parseRuntimeEx);
/*     */     }
/* 560 */     return root;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] init__json_actions_0() {
/* 565 */     return new byte[] { 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 2, 0, 7, 2, 0, 8, 2, 1, 3, 2, 1, 5 };
/*     */   }
/*     */   
/* 568 */   private static final byte[] _json_actions = init__json_actions_0();
/*     */   
/*     */   private static short[] init__json_key_offsets_0() {
/* 571 */     return new short[] { 0, 0, 11, 13, 14, 16, 25, 31, 37, 39, 50, 57, 64, 73, 74, 83, 85, 87, 96, 98, 100, 101, 103, 105, 116, 123, 130, 141, 142, 153, 155, 157, 168, 170, 172, 174, 179, 184, 184 };
/*     */   }
/*     */ 
/*     */   
/* 575 */   private static final short[] _json_key_offsets = init__json_key_offsets_0();
/*     */   
/*     */   private static char[] init__json_trans_keys_0() {
/* 578 */     return new char[] { '\r', ' ', '"', ',', '/', ':', '[', ']', '{', '\t', '\n', '*', '/', '"', '*', '/', '\r', ' ', '"', ',', '/', ':', '}', '\t', '\n', '\r', ' ', '/', ':', '\t', '\n', '\r', ' ', '/', ':', '\t', '\n', '*', '/', '\r', ' ', '"', ',', '/', ':', '[', ']', '{', '\t', '\n', '\t', '\n', '\r', ' ', ',', '/', '}', '\t', '\n', '\r', ' ', ',', '/', '}', '\r', ' ', '"', ',', '/', ':', '}', '\t', '\n', '"', '\r', ' ', '"', ',', '/', ':', '}', '\t', '\n', '*', '/', '*', '/', '\r', ' ', '"', ',', '/', ':', '}', '\t', '\n', '*', '/', '*', '/', '"', '*', '/', '*', '/', '\r', ' ', '"', ',', '/', ':', '[', ']', '{', '\t', '\n', '\t', '\n', '\r', ' ', ',', '/', ']', '\t', '\n', '\r', ' ', ',', '/', ']', '\r', ' ', '"', ',', '/', ':', '[', ']', '{', '\t', '\n', '"', '\r', ' ', '"', ',', '/', ':', '[', ']', '{', '\t', '\n', '*', '/', '*', '/', '\r', ' ', '"', ',', '/', ':', '[', ']', '{', '\t', '\n', '*', '/', '*', '/', '*', '/', '\r', ' ', '/', '\t', '\n', '\r', ' ', '/', '\t', '\n', Character.MIN_VALUE };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 587 */   private static final char[] _json_trans_keys = init__json_trans_keys_0();
/*     */   
/*     */   private static byte[] init__json_single_lengths_0() {
/* 590 */     return new byte[] { 0, 9, 2, 1, 2, 7, 4, 4, 2, 9, 7, 7, 7, 1, 7, 2, 2, 7, 2, 2, 1, 2, 2, 9, 7, 7, 9, 1, 9, 2, 2, 9, 2, 2, 2, 3, 3, 0, 0 };
/*     */   }
/*     */ 
/*     */   
/* 594 */   private static final byte[] _json_single_lengths = init__json_single_lengths_0();
/*     */   
/*     */   private static byte[] init__json_range_lengths_0() {
/* 597 */     return new byte[] { 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0 };
/*     */   }
/*     */ 
/*     */   
/* 601 */   private static final byte[] _json_range_lengths = init__json_range_lengths_0();
/*     */   
/*     */   private static short[] init__json_index_offsets_0() {
/* 604 */     return new short[] { 0, 0, 11, 14, 16, 19, 28, 34, 40, 43, 54, 62, 70, 79, 81, 90, 93, 96, 105, 108, 111, 113, 116, 119, 130, 138, 146, 157, 159, 170, 173, 176, 187, 190, 193, 196, 201, 206, 207 };
/*     */   }
/*     */ 
/*     */   
/* 608 */   private static final short[] _json_index_offsets = init__json_index_offsets_0();
/*     */   
/*     */   private static byte[] init__json_indicies_0() {
/* 611 */     return new byte[] { 1, 1, 2, 3, 4, 3, 5, 3, 6, 1, 0, 7, 7, 3, 8, 3, 9, 9, 3, 11, 11, 12, 13, 14, 3, 15, 11, 10, 16, 16, 17, 18, 16, 3, 19, 19, 20, 21, 19, 3, 22, 22, 3, 21, 21, 24, 3, 25, 3, 26, 3, 27, 21, 23, 28, 29, 28, 28, 30, 31, 32, 3, 33, 34, 33, 33, 13, 35, 15, 3, 34, 34, 12, 36, 37, 3, 15, 34, 10, 16, 3, 36, 36, 12, 3, 38, 3, 3, 36, 10, 39, 39, 3, 40, 40, 3, 13, 13, 12, 3, 41, 3, 15, 13, 10, 42, 42, 3, 43, 43, 3, 28, 3, 44, 44, 3, 45, 45, 3, 47, 47, 48, 49, 50, 3, 51, 52, 53, 47, 46, 54, 55, 54, 54, 56, 57, 58, 3, 59, 60, 59, 59, 49, 61, 52, 3, 60, 60, 48, 62, 63, 3, 51, 52, 53, 60, 46, 54, 3, 62, 62, 48, 3, 64, 3, 51, 3, 53, 62, 46, 65, 65, 3, 66, 66, 3, 49, 49, 48, 3, 67, 3, 51, 52, 53, 49, 46, 68, 68, 3, 69, 69, 3, 70, 70, 3, 8, 8, 71, 8, 3, 72, 72, 73, 72, 3, 3, 3, 0 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 620 */   private static final byte[] _json_indicies = init__json_indicies_0();
/*     */   
/*     */   private static byte[] init__json_trans_targs_0() {
/* 623 */     return new byte[] { 35, 1, 3, 0, 4, 36, 36, 36, 36, 1, 6, 5, 13, 17, 22, 37, 7, 8, 9, 7, 8, 9, 7, 10, 20, 21, 11, 11, 11, 12, 17, 19, 37, 11, 12, 19, 14, 16, 15, 14, 12, 18, 17, 11, 9, 5, 24, 23, 27, 31, 34, 25, 38, 25, 25, 26, 31, 33, 38, 25, 26, 33, 28, 30, 29, 28, 26, 32, 31, 25, 23, 2, 36, 2 };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 628 */   private static final byte[] _json_trans_targs = init__json_trans_targs_0();
/*     */   
/*     */   private static byte[] init__json_trans_actions_0() {
/* 631 */     return new byte[] { 13, 0, 15, 0, 0, 7, 3, 11, 1, 11, 17, 0, 20, 0, 0, 5, 1, 1, 1, 0, 0, 0, 11, 13, 15, 0, 7, 3, 1, 1, 1, 1, 23, 0, 0, 0, 0, 0, 0, 11, 11, 0, 11, 11, 11, 11, 13, 0, 15, 0, 0, 7, 9, 3, 1, 1, 1, 1, 26, 0, 0, 0, 0, 0, 0, 11, 11, 0, 11, 11, 11, 1, 0, 0 };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 636 */   private static final byte[] _json_trans_actions = init__json_trans_actions_0();
/*     */   
/*     */   private static byte[] init__json_eof_actions_0() {
/* 639 */     return new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 };
/*     */   }
/*     */ 
/*     */   
/* 643 */   private static final byte[] _json_eof_actions = init__json_eof_actions_0();
/*     */   
/*     */   static final int json_start = 1;
/*     */   
/*     */   static final int json_first_final = 35;
/*     */   
/*     */   static final int json_error = 0;
/*     */   
/*     */   static final int json_en_object = 5;
/*     */   
/*     */   static final int json_en_array = 23;
/*     */   static final int json_en_main = 1;
/* 655 */   private final Array<JsonValue> elements = new Array<JsonValue>(8);
/* 656 */   private final Array<JsonValue> lastChild = new Array<JsonValue>(8); private JsonValue root;
/*     */   private JsonValue current;
/*     */   
/*     */   private void addChild(String name, JsonValue child) {
/* 660 */     child.setName(name);
/* 661 */     if (this.current == null) {
/* 662 */       this.current = child;
/* 663 */       this.root = child;
/* 664 */     } else if (this.current.isArray() || this.current.isObject()) {
/* 665 */       child.parent = this.current;
/* 666 */       if (this.current.size == 0) {
/* 667 */         this.current.child = child;
/*     */       } else {
/* 669 */         JsonValue last = this.lastChild.pop();
/* 670 */         last.next = child;
/* 671 */         child.prev = last;
/*     */       } 
/* 673 */       this.lastChild.add(child);
/* 674 */       this.current.size++;
/*     */     } else {
/* 676 */       this.root = this.current;
/*     */     } 
/*     */   }
/*     */   protected void startObject(String name) {
/* 680 */     JsonValue value = new JsonValue(JsonValue.ValueType.object);
/* 681 */     if (this.current != null) addChild(name, value); 
/* 682 */     this.elements.add(value);
/* 683 */     this.current = value;
/*     */   }
/*     */   
/*     */   protected void startArray(String name) {
/* 687 */     JsonValue value = new JsonValue(JsonValue.ValueType.array);
/* 688 */     if (this.current != null) addChild(name, value); 
/* 689 */     this.elements.add(value);
/* 690 */     this.current = value;
/*     */   }
/*     */   
/*     */   protected void pop() {
/* 694 */     this.root = this.elements.pop();
/* 695 */     if (this.current.size > 0) this.lastChild.pop(); 
/* 696 */     this.current = (this.elements.size > 0) ? this.elements.peek() : null;
/*     */   }
/*     */   
/*     */   protected void string(String name, String value) {
/* 700 */     addChild(name, new JsonValue(value));
/*     */   }
/*     */   
/*     */   protected void number(String name, double value, String stringValue) {
/* 704 */     addChild(name, new JsonValue(value, stringValue));
/*     */   }
/*     */   
/*     */   protected void number(String name, long value, String stringValue) {
/* 708 */     addChild(name, new JsonValue(value, stringValue));
/*     */   }
/*     */   
/*     */   protected void bool(String name, boolean value) {
/* 712 */     addChild(name, new JsonValue(value));
/*     */   }
/*     */   
/*     */   private String unescape(String value) {
/* 716 */     int length = value.length();
/* 717 */     StringBuilder buffer = new StringBuilder(length + 16);
/* 718 */     for (int i = 0; i < length; ) {
/* 719 */       char c = value.charAt(i++);
/* 720 */       if (c != '\\') {
/* 721 */         buffer.append(c);
/*     */         continue;
/*     */       } 
/* 724 */       if (i == length)
/* 725 */         break;  c = value.charAt(i++);
/* 726 */       if (c == 'u') {
/* 727 */         buffer.append(Character.toChars(Integer.parseInt(value.substring(i, i + 4), 16)));
/* 728 */         i += 4;
/*     */         continue;
/*     */       } 
/* 731 */       switch (c) {
/*     */         case '"':
/*     */         case '/':
/*     */         case '\\':
/*     */           break;
/*     */         case 'b':
/* 737 */           c = '\b';
/*     */           break;
/*     */         case 'f':
/* 740 */           c = '\f';
/*     */           break;
/*     */         case 'n':
/* 743 */           c = '\n';
/*     */           break;
/*     */         case 'r':
/* 746 */           c = '\r';
/*     */           break;
/*     */         case 't':
/* 749 */           c = '\t';
/*     */           break;
/*     */         default:
/* 752 */           throw new SerializationException("Illegal escaped character: \\" + c);
/*     */       } 
/* 754 */       buffer.append(c);
/*     */     } 
/* 756 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\JsonReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */