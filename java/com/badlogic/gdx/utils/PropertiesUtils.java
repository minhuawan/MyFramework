/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PropertiesUtils
/*     */ {
/*     */   private static final int NONE = 0;
/*     */   private static final int SLASH = 1;
/*     */   private static final int UNICODE = 2;
/*     */   private static final int CONTINUE = 3;
/*     */   private static final int KEY_DONE = 4;
/*     */   private static final int IGNORE = 5;
/*     */   private static final String LINE_SEPARATOR = "\n";
/*     */   
/*     */   public static void load(ObjectMap<String, String> properties, Reader reader) throws IOException {
/*  54 */     if (properties == null) throw new NullPointerException("ObjectMap cannot be null"); 
/*  55 */     if (reader == null) throw new NullPointerException("Reader cannot be null"); 
/*  56 */     int mode = 0, unicode = 0, count = 0;
/*  57 */     char[] buf = new char[40];
/*  58 */     int offset = 0, keyLength = -1;
/*  59 */     boolean firstChar = true;
/*     */     
/*  61 */     BufferedReader br = new BufferedReader(reader);
/*     */     
/*     */     label104: while (true) {
/*  64 */       int intVal = br.read();
/*  65 */       if (intVal == -1) {
/*     */         break;
/*     */       }
/*  68 */       char nextChar = (char)intVal;
/*     */       
/*  70 */       if (offset == buf.length) {
/*  71 */         char[] newBuf = new char[buf.length * 2];
/*  72 */         System.arraycopy(buf, 0, newBuf, 0, offset);
/*  73 */         buf = newBuf;
/*     */       } 
/*  75 */       if (mode == 2) {
/*  76 */         int digit = Character.digit(nextChar, 16);
/*  77 */         if (digit >= 0) {
/*  78 */           unicode = (unicode << 4) + digit;
/*  79 */           if (++count < 4) {
/*     */             continue;
/*     */           }
/*  82 */         } else if (count <= 4) {
/*  83 */           throw new IllegalArgumentException("Invalid Unicode sequence: illegal character");
/*     */         } 
/*  85 */         mode = 0;
/*  86 */         buf[offset++] = (char)unicode;
/*  87 */         if (nextChar != '\n') {
/*     */           continue;
/*     */         }
/*     */       } 
/*  91 */       if (mode == 1) {
/*  92 */         mode = 0;
/*  93 */         switch (nextChar) {
/*     */           case '\r':
/*  95 */             mode = 3;
/*     */             continue;
/*     */           case '\n':
/*  98 */             mode = 5;
/*     */             continue;
/*     */           case 'b':
/* 101 */             nextChar = '\b';
/*     */             break;
/*     */           case 'f':
/* 104 */             nextChar = '\f';
/*     */             break;
/*     */           case 'n':
/* 107 */             nextChar = '\n';
/*     */             break;
/*     */           case 'r':
/* 110 */             nextChar = '\r';
/*     */             break;
/*     */           case 't':
/* 113 */             nextChar = '\t';
/*     */             break;
/*     */           case 'u':
/* 116 */             mode = 2;
/* 117 */             unicode = count = 0;
/*     */             continue;
/*     */         } 
/*     */       } else {
/* 121 */         switch (nextChar) {
/*     */           case '!':
/*     */           case '#':
/* 124 */             if (firstChar)
/*     */               while (true) {
/* 126 */                 intVal = br.read();
/* 127 */                 if (intVal == -1) {
/*     */                   continue label104;
/*     */                 }
/* 130 */                 nextChar = (char)intVal;
/* 131 */                 if (nextChar != '\r') { if (nextChar == '\n')
/*     */                     continue label104; 
/*     */                   continue; }
/*     */                 
/*     */                 continue label104;
/*     */               }  
/*     */             break;
/*     */           case '\n':
/* 139 */             if (mode == 3) {
/* 140 */               mode = 5;
/*     */               continue;
/*     */             } 
/*     */           
/*     */           case '\r':
/* 145 */             mode = 0;
/* 146 */             firstChar = true;
/* 147 */             if (offset > 0 || (offset == 0 && keyLength == 0)) {
/* 148 */               if (keyLength == -1) {
/* 149 */                 keyLength = offset;
/*     */               }
/* 151 */               String temp = new String(buf, 0, offset);
/* 152 */               properties.put(temp.substring(0, keyLength), temp.substring(keyLength));
/*     */             } 
/* 154 */             keyLength = -1;
/* 155 */             offset = 0;
/*     */             continue;
/*     */           case '\\':
/* 158 */             if (mode == 4) {
/* 159 */               keyLength = offset;
/*     */             }
/* 161 */             mode = 1;
/*     */             continue;
/*     */           case ':':
/*     */           case '=':
/* 165 */             if (keyLength == -1) {
/* 166 */               mode = 0;
/* 167 */               keyLength = offset;
/*     */               continue;
/*     */             } 
/*     */             break;
/*     */         } 
/*     */         
/* 173 */         if (Character.isSpace(nextChar)) {
/* 174 */           if (mode == 3) {
/* 175 */             mode = 5;
/*     */           }
/*     */           
/* 178 */           if (offset == 0 || offset == keyLength || mode == 5) {
/*     */             continue;
/*     */           }
/* 181 */           if (keyLength == -1) {
/* 182 */             mode = 4;
/*     */             continue;
/*     */           } 
/*     */         } 
/* 186 */         if (mode == 5 || mode == 3) {
/* 187 */           mode = 0;
/*     */         }
/*     */       } 
/* 190 */       firstChar = false;
/* 191 */       if (mode == 4) {
/* 192 */         keyLength = offset;
/* 193 */         mode = 0;
/*     */       } 
/* 195 */       buf[offset++] = nextChar;
/*     */     } 
/* 197 */     if (mode == 2 && count <= 4) {
/* 198 */       throw new IllegalArgumentException("Invalid Unicode sequence: expected format \\uxxxx");
/*     */     }
/* 200 */     if (keyLength == -1 && offset > 0) {
/* 201 */       keyLength = offset;
/*     */     }
/* 203 */     if (keyLength >= 0) {
/* 204 */       String temp = new String(buf, 0, offset);
/* 205 */       String key = temp.substring(0, keyLength);
/* 206 */       String value = temp.substring(keyLength);
/* 207 */       if (mode == 1) {
/* 208 */         value = value + "\000";
/*     */       }
/* 210 */       properties.put(key, value);
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
/*     */   public static void store(ObjectMap<String, String> properties, Writer writer, String comment) throws IOException {
/* 231 */     storeImpl(properties, writer, comment, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void storeImpl(ObjectMap<String, String> properties, Writer writer, String comment, boolean escapeUnicode) throws IOException {
/* 236 */     if (comment != null) {
/* 237 */       writeComment(writer, comment);
/*     */     }
/* 239 */     writer.write("#");
/* 240 */     writer.write((new Date()).toString());
/* 241 */     writer.write("\n");
/*     */     
/* 243 */     StringBuilder sb = new StringBuilder(200);
/* 244 */     for (ObjectMap.Entries<ObjectMap.Entry<String, String>> entries = properties.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, String> entry = entries.next();
/* 245 */       dumpString(sb, (String)entry.key, true, escapeUnicode);
/* 246 */       sb.append('=');
/* 247 */       dumpString(sb, (String)entry.value, false, escapeUnicode);
/* 248 */       writer.write("\n");
/* 249 */       writer.write(sb.toString());
/* 250 */       sb.setLength(0); }
/*     */     
/* 252 */     writer.flush();
/*     */   }
/*     */   
/*     */   private static void dumpString(StringBuilder outBuffer, String string, boolean escapeSpace, boolean escapeUnicode) {
/* 256 */     int len = string.length();
/* 257 */     for (int i = 0; i < len; i++) {
/* 258 */       char ch = string.charAt(i);
/*     */       
/* 260 */       if (ch > '=' && ch < '') {
/* 261 */         outBuffer.append((ch == '\\') ? "\\\\" : Character.valueOf(ch));
/*     */       } else {
/*     */         
/* 264 */         switch (ch) {
/*     */           case ' ':
/* 266 */             if (i == 0 || escapeSpace) outBuffer.append("\\ "); 
/*     */             break;
/*     */           case '\n':
/* 269 */             outBuffer.append("\\n");
/*     */             break;
/*     */           case '\r':
/* 272 */             outBuffer.append("\\r");
/*     */             break;
/*     */           case '\t':
/* 275 */             outBuffer.append("\\t");
/*     */             break;
/*     */           case '\f':
/* 278 */             outBuffer.append("\\f");
/*     */             break;
/*     */           case '!':
/*     */           case '#':
/*     */           case ':':
/*     */           case '=':
/* 284 */             outBuffer.append('\\').append(ch);
/*     */             break;
/*     */           default:
/* 287 */             if ((((ch < ' ' || ch > '~') ? 1 : 0) & escapeUnicode) != 0) {
/* 288 */               String hex = Integer.toHexString(ch);
/* 289 */               outBuffer.append("\\u");
/* 290 */               for (int j = 0; j < 4 - hex.length(); j++) {
/* 291 */                 outBuffer.append('0');
/*     */               }
/* 293 */               outBuffer.append(hex); break;
/*     */             } 
/* 295 */             outBuffer.append(ch);
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void writeComment(Writer writer, String comment) throws IOException {
/* 303 */     writer.write("#");
/* 304 */     int len = comment.length();
/* 305 */     int curIndex = 0;
/* 306 */     int lastIndex = 0;
/* 307 */     while (curIndex < len) {
/* 308 */       char c = comment.charAt(curIndex);
/* 309 */       if (c > 'ÿ' || c == '\n' || c == '\r') {
/* 310 */         if (lastIndex != curIndex) writer.write(comment.substring(lastIndex, curIndex)); 
/* 311 */         if (c > 'ÿ') {
/* 312 */           String hex = Integer.toHexString(c);
/* 313 */           writer.write("\\u");
/* 314 */           for (int j = 0; j < 4 - hex.length(); j++) {
/* 315 */             writer.write(48);
/*     */           }
/* 317 */           writer.write(hex);
/*     */         } else {
/* 319 */           writer.write("\n");
/* 320 */           if (c == '\r' && curIndex != len - 1 && comment.charAt(curIndex + 1) == '\n') {
/* 321 */             curIndex++;
/*     */           }
/* 323 */           if (curIndex == len - 1 || (comment.charAt(curIndex + 1) != '#' && comment.charAt(curIndex + 1) != '!'))
/* 324 */             writer.write("#"); 
/*     */         } 
/* 326 */         lastIndex = curIndex + 1;
/*     */       } 
/* 328 */       curIndex++;
/*     */     } 
/* 330 */     if (lastIndex != curIndex) writer.write(comment.substring(lastIndex, curIndex)); 
/* 331 */     writer.write("\n");
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\PropertiesUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */