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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlReader
/*     */ {
/*  38 */   private final Array<Element> elements = new Array<Element>(8); private Element root;
/*     */   private Element current;
/*  40 */   private final StringBuilder textBuffer = new StringBuilder(64);
/*     */   
/*     */   public Element parse(String xml) {
/*  43 */     char[] data = xml.toCharArray();
/*  44 */     return parse(data, 0, data.length);
/*     */   }
/*     */   
/*     */   public Element parse(Reader reader) throws IOException {
/*     */     try {
/*  49 */       char[] data = new char[1024];
/*  50 */       int offset = 0;
/*     */       while (true) {
/*  52 */         int length = reader.read(data, offset, data.length - offset);
/*  53 */         if (length == -1)
/*  54 */           break;  if (length == 0) {
/*  55 */           char[] newData = new char[data.length * 2];
/*  56 */           System.arraycopy(data, 0, newData, 0, data.length);
/*  57 */           data = newData; continue;
/*     */         } 
/*  59 */         offset += length;
/*     */       } 
/*  61 */       return parse(data, 0, offset);
/*  62 */     } catch (IOException ex) {
/*  63 */       throw new SerializationException(ex);
/*     */     } finally {
/*  65 */       StreamUtils.closeQuietly(reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Element parse(InputStream input) throws IOException {
/*     */     try {
/*  71 */       return parse(new InputStreamReader(input, "UTF-8"));
/*  72 */     } catch (IOException ex) {
/*  73 */       throw new SerializationException(ex);
/*     */     } finally {
/*  75 */       StreamUtils.closeQuietly(input);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Element parse(FileHandle file) throws IOException {
/*     */     try {
/*  81 */       return parse(file.reader("UTF-8"));
/*  82 */     } catch (Exception ex) {
/*  83 */       throw new SerializationException("Error parsing file: " + file, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Element parse(char[] data, int offset, int length) {
/*  88 */     int p = offset, pe = length;
/*     */     
/*  90 */     int s = 0;
/*  91 */     String attributeName = null;
/*  92 */     boolean hasBody = false;
/*     */ 
/*     */ 
/*     */     
/*  96 */     int cs = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     int _trans = 0;
/*     */ 
/*     */ 
/*     */     
/* 106 */     int _goto_targ = 0;
/*     */     label156: while (true) {
/*     */       int _klen;
/*     */       int _keys;
/* 110 */       switch (_goto_targ) {
/*     */         case 0:
/* 112 */           if (p == pe) {
/* 113 */             _goto_targ = 4;
/*     */             continue;
/*     */           } 
/* 116 */           if (cs == 0) {
/* 117 */             _goto_targ = 5;
/*     */             continue;
/*     */           } 
/*     */ 
/*     */         
/*     */         case 1:
/* 123 */           _keys = _xml_key_offsets[cs];
/* 124 */           _trans = _xml_index_offsets[cs];
/* 125 */           _klen = _xml_single_lengths[cs];
/* 126 */           if (_klen > 0) {
/* 127 */             int _lower = _keys;
/*     */             
/* 129 */             int _upper = _keys + _klen - 1;
/*     */             
/* 131 */             while (_upper >= _lower) {
/*     */               
/* 133 */               int _mid = _lower + (_upper - _lower >> 1);
/* 134 */               if (data[p] < _xml_trans_keys[_mid]) {
/* 135 */                 _upper = _mid - 1; continue;
/* 136 */               }  if (data[p] > _xml_trans_keys[_mid]) {
/* 137 */                 _lower = _mid + 1; continue;
/*     */               } 
/* 139 */               _trans += _mid - _keys;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 169 */               _trans = _xml_indicies[_trans];
/* 170 */               cs = _xml_trans_targs[_trans];
/*     */               
/* 172 */               if (_xml_trans_actions[_trans] != 0)
/* 173 */               { int _acts = _xml_trans_actions[_trans];
/* 174 */                 int _nacts = _xml_actions[_acts++]; while (true)
/* 175 */                 { if (_nacts-- > 0)
/* 176 */                   { char c; int end; int current; boolean entityFound; int i; switch (_xml_actions[_acts++])
/*     */                     
/*     */                     { 
/*     */                       case 0:
/* 180 */                         s = p;
/*     */                         continue;
/*     */ 
/*     */ 
/*     */                       
/*     */                       case 1:
/* 186 */                         c = data[s];
/* 187 */                         if (c == '?' || c == '!') {
/* 188 */                           if (data[s + 1] == '[' && data[s + 2] == 'C' && data[s + 3] == 'D' && data[s + 4] == 'A' && data[s + 5] == 'T' && data[s + 6] == 'A' && data[s + 7] == '[') {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                             
/* 195 */                             s += 8;
/* 196 */                             p = s + 2;
/* 197 */                             while (data[p - 2] != ']' || data[p - 1] != ']' || data[p] != '>')
/* 198 */                               p++; 
/* 199 */                             text(new String(data, s, p - s - 2));
/* 200 */                           } else if (c == '!' && data[s + 1] == '-' && data[s + 2] == '-') {
/* 201 */                             p = s + 3;
/* 202 */                             while (data[p] != '-' || data[p + 1] != '-' || data[p + 2] != '>')
/* 203 */                               p++; 
/* 204 */                             p += 2;
/*     */                           } else {
/* 206 */                             while (data[p] != '>')
/* 207 */                               p++; 
/*     */                           } 
/* 209 */                           cs = 15;
/* 210 */                           _goto_targ = 2;
/*     */                           
/*     */                           continue label156;
/*     */                         } 
/* 214 */                         hasBody = true;
/* 215 */                         open(new String(data, s, p - s));
/*     */                         continue;
/*     */ 
/*     */ 
/*     */                       
/*     */                       case 2:
/* 221 */                         hasBody = false;
/* 222 */                         close();
/*     */                         
/* 224 */                         cs = 15;
/* 225 */                         _goto_targ = 2;
/*     */                         continue label156;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/*     */                       case 3:
/* 233 */                         close();
/*     */                         
/* 235 */                         cs = 15;
/* 236 */                         _goto_targ = 2;
/*     */                         continue label156;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/*     */                       case 4:
/* 244 */                         if (hasBody) {
/* 245 */                           cs = 15;
/* 246 */                           _goto_targ = 2;
/*     */                           continue label156;
/*     */                         } 
/*     */                         continue;
/*     */ 
/*     */ 
/*     */                       
/*     */                       case 5:
/* 254 */                         attributeName = new String(data, s, p - s);
/*     */                         continue;
/*     */ 
/*     */ 
/*     */                       
/*     */                       case 6:
/* 260 */                         attribute(attributeName, new String(data, s, p - s));
/*     */                         continue;
/*     */ 
/*     */ 
/*     */                       
/*     */                       case 7:
/* 266 */                         end = p;
/* 267 */                         while (end != s) {
/* 268 */                           switch (data[end - 1]) {
/*     */                             case '\t':
/*     */                             case '\n':
/*     */                             case '\r':
/*     */                             case ' ':
/* 273 */                               end--;
/*     */                           } 
/*     */ 
/*     */                         
/*     */                         } 
/* 278 */                         current = s;
/* 279 */                         entityFound = false;
/* 280 */                         while (current != end) {
/* 281 */                           if (data[current++] != '&')
/* 282 */                             continue;  int entityStart = current;
/* 283 */                           while (current != end) {
/* 284 */                             if (data[current++] != ';')
/* 285 */                               continue;  this.textBuffer.append(data, s, entityStart - s - 1);
/* 286 */                             String name = new String(data, entityStart, current - entityStart - 1);
/* 287 */                             String value = entity(name);
/* 288 */                             if (value != null); this.textBuffer.append(name);
/* 289 */                             s = current;
/* 290 */                             i = 1;
/*     */                           } 
/*     */                         } 
/*     */                         
/* 294 */                         if (i != 0) {
/* 295 */                           if (s < end) this.textBuffer.append(data, s, end - s); 
/* 296 */                           text(this.textBuffer.toString());
/* 297 */                           this.textBuffer.setLength(0); continue;
/*     */                         } 
/* 299 */                         text(new String(data, s, end - s)); continue; }  continue; }  continue label156; }  } else { continue; } 
/*     */             }  _keys += _klen; _trans += _klen;
/*     */           }  _klen = _xml_range_lengths[cs]; if (_klen > 0) { int _lower = _keys; int i = _keys + (_klen << 1) - 2; while (true) { if (i < _lower) { _trans += _klen; break; }
/*     */                int _mid = _lower + (i - _lower >> 1 & 0xFFFFFFFE); if (data[p] < _xml_trans_keys[_mid]) { i = _mid - 2; continue; }
/*     */                if (data[p] > _xml_trans_keys[_mid + 1]) { _lower = _mid + 2; continue; }
/*     */                _trans += _mid - _keys >> 1; break; }
/*     */              continue; }
/*     */            continue;
/*     */         case 2:
/* 308 */           if (cs == 0) {
/* 309 */             _goto_targ = 5;
/*     */             continue;
/*     */           } 
/* 312 */           if (++p != pe) {
/* 313 */             _goto_targ = 1;
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       break;
/*     */     } 
/* 325 */     if (p < pe) {
/* 326 */       int lineNumber = 1;
/* 327 */       for (int i = 0; i < p; i++) {
/* 328 */         if (data[i] == '\n') lineNumber++; 
/* 329 */       }  throw new SerializationException("Error parsing XML on line " + lineNumber + " near: " + new String(data, p, 
/* 330 */             Math.min(32, pe - p)));
/* 331 */     }  if (this.elements.size != 0) {
/* 332 */       Element element = this.elements.peek();
/* 333 */       this.elements.clear();
/* 334 */       throw new SerializationException("Error parsing XML, unclosed element: " + element.getName());
/*     */     } 
/* 336 */     Element root = this.root;
/* 337 */     this.root = null;
/* 338 */     return root;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] init__xml_actions_0() {
/* 343 */     return new byte[] { 0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 2, 0, 6, 2, 1, 4, 2, 2, 4 };
/*     */   }
/*     */   
/* 346 */   private static final byte[] _xml_actions = init__xml_actions_0();
/*     */   
/*     */   private static byte[] init__xml_key_offsets_0() {
/* 349 */     return new byte[] { 0, 0, 4, 9, 14, 20, 26, 30, 35, 36, 37, 42, 46, 50, 51, 52, 56, 57, 62, 67, 73, 79, 83, 88, 89, 90, 95, 99, 103, 104, 108, 109, 110, 111, 112, 115 };
/*     */   }
/*     */ 
/*     */   
/* 353 */   private static final byte[] _xml_key_offsets = init__xml_key_offsets_0();
/*     */   
/*     */   private static char[] init__xml_trans_keys_0() {
/* 356 */     return new char[] { ' ', '<', '\t', '\r', ' ', '/', '>', '\t', '\r', ' ', '/', '>', '\t', '\r', ' ', '/', '=', '>', '\t', '\r', ' ', '/', '=', '>', '\t', '\r', ' ', '=', '\t', '\r', ' ', '"', '\'', '\t', '\r', '"', '"', ' ', '/', '>', '\t', '\r', ' ', '>', '\t', '\r', ' ', '>', '\t', '\r', '\'', '\'', ' ', '<', '\t', '\r', '<', ' ', '/', '>', '\t', '\r', ' ', '/', '>', '\t', '\r', ' ', '/', '=', '>', '\t', '\r', ' ', '/', '=', '>', '\t', '\r', ' ', '=', '\t', '\r', ' ', '"', '\'', '\t', '\r', '"', '"', ' ', '/', '>', '\t', '\r', ' ', '>', '\t', '\r', ' ', '>', '\t', '\r', '<', ' ', '/', '\t', '\r', '>', '>', '\'', '\'', ' ', '\t', '\r', Character.MIN_VALUE };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 362 */   private static final char[] _xml_trans_keys = init__xml_trans_keys_0();
/*     */   
/*     */   private static byte[] init__xml_single_lengths_0() {
/* 365 */     return new byte[] { 0, 2, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 1, 1, 2, 1, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 1, 2, 1, 1, 1, 1, 1, 0 };
/*     */   }
/*     */ 
/*     */   
/* 369 */   private static final byte[] _xml_single_lengths = init__xml_single_lengths_0();
/*     */   
/*     */   private static byte[] init__xml_range_lengths_0() {
/* 372 */     return new byte[] { 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0 };
/*     */   }
/*     */ 
/*     */   
/* 376 */   private static final byte[] _xml_range_lengths = init__xml_range_lengths_0();
/*     */   
/*     */   private static short[] init__xml_index_offsets_0() {
/* 379 */     return new short[] { 0, 0, 4, 9, 14, 20, 26, 30, 35, 37, 39, 44, 48, 52, 54, 56, 60, 62, 67, 72, 78, 84, 88, 93, 95, 97, 102, 106, 110, 112, 116, 118, 120, 122, 124, 127 };
/*     */   }
/*     */ 
/*     */   
/* 383 */   private static final short[] _xml_index_offsets = init__xml_index_offsets_0();
/*     */   
/*     */   private static byte[] init__xml_indicies_0() {
/* 386 */     return new byte[] { 0, 2, 0, 1, 2, 1, 1, 2, 3, 5, 6, 7, 5, 4, 9, 10, 1, 11, 9, 8, 13, 1, 14, 1, 13, 12, 15, 16, 15, 1, 16, 17, 18, 16, 1, 20, 19, 22, 21, 9, 10, 11, 9, 1, 23, 24, 23, 1, 25, 11, 25, 1, 20, 26, 22, 27, 29, 30, 29, 28, 32, 31, 30, 34, 1, 30, 33, 36, 37, 38, 36, 35, 40, 41, 1, 42, 40, 39, 44, 1, 45, 1, 44, 43, 46, 47, 46, 1, 47, 48, 49, 47, 1, 51, 50, 53, 52, 40, 41, 42, 40, 1, 54, 55, 54, 1, 56, 42, 56, 1, 57, 1, 57, 34, 57, 1, 1, 58, 59, 58, 51, 60, 53, 61, 62, 62, 1, 1, 0 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 393 */   private static final byte[] _xml_indicies = init__xml_indicies_0();
/*     */   
/*     */   private static byte[] init__xml_trans_targs_0() {
/* 396 */     return new byte[] { 1, 0, 2, 3, 3, 4, 11, 34, 5, 4, 11, 34, 5, 6, 7, 6, 7, 8, 13, 9, 10, 9, 10, 12, 34, 12, 14, 14, 16, 15, 17, 16, 17, 18, 30, 18, 19, 26, 28, 20, 19, 26, 28, 20, 21, 22, 21, 22, 23, 32, 24, 25, 24, 25, 27, 28, 27, 29, 31, 35, 33, 33, 34 };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 401 */   private static final byte[] _xml_trans_targs = init__xml_trans_targs_0();
/*     */   
/*     */   private static byte[] init__xml_trans_actions_0() {
/* 404 */     return new byte[] { 0, 0, 0, 1, 0, 3, 3, 20, 1, 0, 0, 9, 0, 11, 11, 0, 0, 0, 0, 1, 17, 0, 13, 5, 23, 0, 1, 0, 1, 0, 0, 0, 15, 1, 0, 0, 3, 3, 20, 1, 0, 0, 9, 0, 11, 11, 0, 0, 0, 0, 1, 17, 0, 13, 5, 23, 0, 0, 0, 7, 1, 0, 0 };
/*     */   }
/*     */ 
/*     */   
/* 408 */   private static final byte[] _xml_trans_actions = init__xml_trans_actions_0();
/*     */   
/*     */   static final int xml_start = 1;
/*     */   
/*     */   static final int xml_first_final = 34;
/*     */   
/*     */   static final int xml_error = 0;
/*     */   
/*     */   static final int xml_en_elementBody = 15;
/*     */   static final int xml_en_main = 1;
/*     */   
/*     */   protected void open(String name) {
/* 420 */     Element child = new Element(name, this.current);
/* 421 */     Element parent = this.current;
/* 422 */     if (parent != null) parent.addChild(child); 
/* 423 */     this.elements.add(child);
/* 424 */     this.current = child;
/*     */   }
/*     */   
/*     */   protected void attribute(String name, String value) {
/* 428 */     this.current.setAttribute(name, value);
/*     */   }
/*     */   
/*     */   protected String entity(String name) {
/* 432 */     if (name.equals("lt")) return "<"; 
/* 433 */     if (name.equals("gt")) return ">"; 
/* 434 */     if (name.equals("amp")) return "&"; 
/* 435 */     if (name.equals("apos")) return "'"; 
/* 436 */     if (name.equals("quot")) return "\""; 
/* 437 */     if (name.startsWith("#x")) return Character.toString((char)Integer.parseInt(name.substring(2), 16)); 
/* 438 */     return null;
/*     */   }
/*     */   
/*     */   protected void text(String text) {
/* 442 */     String existing = this.current.getText();
/* 443 */     this.current.setText((existing != null) ? (existing + text) : text);
/*     */   }
/*     */   
/*     */   protected void close() {
/* 447 */     this.root = this.elements.pop();
/* 448 */     this.current = (this.elements.size > 0) ? this.elements.peek() : null;
/*     */   }
/*     */   
/*     */   public static class Element {
/*     */     private final String name;
/*     */     private ObjectMap<String, String> attributes;
/*     */     private Array<Element> children;
/*     */     private String text;
/*     */     private Element parent;
/*     */     
/*     */     public Element(String name, Element parent) {
/* 459 */       this.name = name;
/* 460 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 464 */       return this.name;
/*     */     }
/*     */     
/*     */     public ObjectMap<String, String> getAttributes() {
/* 468 */       return this.attributes;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getAttribute(String name) {
/* 473 */       if (this.attributes == null) throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute: " + name); 
/* 474 */       String value = this.attributes.get(name);
/* 475 */       if (value == null) throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute: " + name); 
/* 476 */       return value;
/*     */     }
/*     */     
/*     */     public String getAttribute(String name, String defaultValue) {
/* 480 */       if (this.attributes == null) return defaultValue; 
/* 481 */       String value = this.attributes.get(name);
/* 482 */       if (value == null) return defaultValue; 
/* 483 */       return value;
/*     */     }
/*     */     
/*     */     public void setAttribute(String name, String value) {
/* 487 */       if (this.attributes == null) this.attributes = new ObjectMap<String, String>(8); 
/* 488 */       this.attributes.put(name, value);
/*     */     }
/*     */     
/*     */     public int getChildCount() {
/* 492 */       if (this.children == null) return 0; 
/* 493 */       return this.children.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public Element getChild(int index) {
/* 498 */       if (this.children == null) throw new GdxRuntimeException("Element has no children: " + this.name); 
/* 499 */       return this.children.get(index);
/*     */     }
/*     */     
/*     */     public void addChild(Element element) {
/* 503 */       if (this.children == null) this.children = new Array<Element>(8); 
/* 504 */       this.children.add(element);
/*     */     }
/*     */     
/*     */     public String getText() {
/* 508 */       return this.text;
/*     */     }
/*     */     
/*     */     public void setText(String text) {
/* 512 */       this.text = text;
/*     */     }
/*     */     
/*     */     public void removeChild(int index) {
/* 516 */       if (this.children != null) this.children.removeIndex(index); 
/*     */     }
/*     */     
/*     */     public void removeChild(Element child) {
/* 520 */       if (this.children != null) this.children.removeValue(child, true); 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 524 */       this.parent.removeChild(this);
/*     */     }
/*     */     
/*     */     public Element getParent() {
/* 528 */       return this.parent;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 532 */       return toString("");
/*     */     }
/*     */     
/*     */     public String toString(String indent) {
/* 536 */       StringBuilder buffer = new StringBuilder(128);
/* 537 */       buffer.append(indent);
/* 538 */       buffer.append('<');
/* 539 */       buffer.append(this.name);
/* 540 */       if (this.attributes != null) {
/* 541 */         for (ObjectMap.Entries<ObjectMap.Entry<String, String>> entries = this.attributes.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, String> entry = entries.next();
/* 542 */           buffer.append(' ');
/* 543 */           buffer.append((String)entry.key);
/* 544 */           buffer.append("=\"");
/* 545 */           buffer.append((String)entry.value);
/* 546 */           buffer.append('"'); }
/*     */       
/*     */       }
/* 549 */       if (this.children == null && (this.text == null || this.text.length() == 0)) {
/* 550 */         buffer.append("/>");
/*     */       } else {
/* 552 */         buffer.append(">\n");
/* 553 */         String childIndent = indent + '\t';
/* 554 */         if (this.text != null && this.text.length() > 0) {
/* 555 */           buffer.append(childIndent);
/* 556 */           buffer.append(this.text);
/* 557 */           buffer.append('\n');
/*     */         } 
/* 559 */         if (this.children != null) {
/* 560 */           for (Element child : this.children) {
/* 561 */             buffer.append(child.toString(childIndent));
/* 562 */             buffer.append('\n');
/*     */           } 
/*     */         }
/* 565 */         buffer.append(indent);
/* 566 */         buffer.append("</");
/* 567 */         buffer.append(this.name);
/* 568 */         buffer.append('>');
/*     */       } 
/* 570 */       return buffer.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Element getChildByName(String name) {
/* 576 */       if (this.children == null) return null; 
/* 577 */       for (int i = 0; i < this.children.size; i++) {
/* 578 */         Element element = this.children.get(i);
/* 579 */         if (element.name.equals(name)) return element; 
/*     */       } 
/* 581 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Element getChildByNameRecursive(String name) {
/* 587 */       if (this.children == null) return null; 
/* 588 */       for (int i = 0; i < this.children.size; i++) {
/* 589 */         Element element = this.children.get(i);
/* 590 */         if (element.name.equals(name)) return element; 
/* 591 */         Element found = element.getChildByNameRecursive(name);
/* 592 */         if (found != null) return found; 
/*     */       } 
/* 594 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Array<Element> getChildrenByName(String name) {
/* 600 */       Array<Element> result = new Array<Element>();
/* 601 */       if (this.children == null) return result; 
/* 602 */       for (int i = 0; i < this.children.size; i++) {
/* 603 */         Element child = this.children.get(i);
/* 604 */         if (child.name.equals(name)) result.add(child); 
/*     */       } 
/* 606 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Array<Element> getChildrenByNameRecursively(String name) {
/* 612 */       Array<Element> result = new Array<Element>();
/* 613 */       getChildrenByNameRecursively(name, result);
/* 614 */       return result;
/*     */     }
/*     */     
/*     */     private void getChildrenByNameRecursively(String name, Array<Element> result) {
/* 618 */       if (this.children == null)
/* 619 */         return;  for (int i = 0; i < this.children.size; i++) {
/* 620 */         Element child = this.children.get(i);
/* 621 */         if (child.name.equals(name)) result.add(child); 
/* 622 */         child.getChildrenByNameRecursively(name, result);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public float getFloatAttribute(String name) {
/* 628 */       return Float.parseFloat(getAttribute(name));
/*     */     }
/*     */     
/*     */     public float getFloatAttribute(String name, float defaultValue) {
/* 632 */       String value = getAttribute(name, null);
/* 633 */       if (value == null) return defaultValue; 
/* 634 */       return Float.parseFloat(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIntAttribute(String name) {
/* 639 */       return Integer.parseInt(getAttribute(name));
/*     */     }
/*     */     
/*     */     public int getIntAttribute(String name, int defaultValue) {
/* 643 */       String value = getAttribute(name, null);
/* 644 */       if (value == null) return defaultValue; 
/* 645 */       return Integer.parseInt(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getBooleanAttribute(String name) {
/* 650 */       return Boolean.parseBoolean(getAttribute(name));
/*     */     }
/*     */     
/*     */     public boolean getBooleanAttribute(String name, boolean defaultValue) {
/* 654 */       String value = getAttribute(name, null);
/* 655 */       if (value == null) return defaultValue; 
/* 656 */       return Boolean.parseBoolean(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String get(String name) {
/* 662 */       String value = get(name, null);
/* 663 */       if (value == null) throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name); 
/* 664 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String get(String name, String defaultValue) {
/* 670 */       if (this.attributes != null) {
/* 671 */         String str = this.attributes.get(name);
/* 672 */         if (str != null) return str; 
/*     */       } 
/* 674 */       Element child = getChildByName(name);
/* 675 */       if (child == null) return defaultValue; 
/* 676 */       String value = child.getText();
/* 677 */       if (value == null) return defaultValue; 
/* 678 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getInt(String name) {
/* 684 */       String value = get(name, null);
/* 685 */       if (value == null) throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name); 
/* 686 */       return Integer.parseInt(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getInt(String name, int defaultValue) {
/* 692 */       String value = get(name, null);
/* 693 */       if (value == null) return defaultValue; 
/* 694 */       return Integer.parseInt(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public float getFloat(String name) {
/* 700 */       String value = get(name, null);
/* 701 */       if (value == null) throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name); 
/* 702 */       return Float.parseFloat(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public float getFloat(String name, float defaultValue) {
/* 708 */       String value = get(name, null);
/* 709 */       if (value == null) return defaultValue; 
/* 710 */       return Float.parseFloat(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getBoolean(String name) {
/* 716 */       String value = get(name, null);
/* 717 */       if (value == null) throw new GdxRuntimeException("Element " + this.name + " doesn't have attribute or child: " + name); 
/* 718 */       return Boolean.parseBoolean(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getBoolean(String name, boolean defaultValue) {
/* 724 */       String value = get(name, null);
/* 725 */       if (value == null) return defaultValue; 
/* 726 */       return Boolean.parseBoolean(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\XmlReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */