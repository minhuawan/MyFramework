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
/*     */ public class Base64Coder
/*     */ {
/*     */   private static final String systemLineSeparator = "\n";
/*     */   
/*     */   public static class CharMap
/*     */   {
/*  34 */     protected final char[] encodingMap = new char[64];
/*  35 */     protected final byte[] decodingMap = new byte[128];
/*     */     
/*     */     public CharMap(char char63, char char64) {
/*  38 */       int i = 0; char c;
/*  39 */       for (c = 'A'; c <= 'Z'; c = (char)(c + 1)) {
/*  40 */         this.encodingMap[i++] = c;
/*     */       }
/*  42 */       for (c = 'a'; c <= 'z'; c = (char)(c + 1)) {
/*  43 */         this.encodingMap[i++] = c;
/*     */       }
/*  45 */       for (c = '0'; c <= '9'; c = (char)(c + 1)) {
/*  46 */         this.encodingMap[i++] = c;
/*     */       }
/*  48 */       this.encodingMap[i++] = char63;
/*  49 */       this.encodingMap[i++] = char64;
/*  50 */       for (i = 0; i < this.decodingMap.length; i++) {
/*  51 */         this.decodingMap[i] = -1;
/*     */       }
/*  53 */       for (i = 0; i < 64; i++) {
/*  54 */         this.decodingMap[this.encodingMap[i]] = (byte)i;
/*     */       }
/*     */     }
/*     */     
/*     */     public byte[] getDecodingMap() {
/*  59 */       return this.decodingMap;
/*     */     }
/*     */     
/*     */     public char[] getEncodingMap() {
/*  63 */       return this.encodingMap;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final CharMap regularMap = new CharMap('+', '/'), urlsafeMap = new CharMap('-', '_');
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeString(String s) {
/*  76 */     return encodeString(s, false);
/*     */   }
/*     */   
/*     */   public static String encodeString(String s, boolean useUrlsafeEncoding) {
/*  80 */     return new String(encode(s.getBytes(), useUrlsafeEncoding ? urlsafeMap.encodingMap : regularMap.encodingMap));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeLines(byte[] in) {
/*  88 */     return encodeLines(in, 0, in.length, 76, "\n", regularMap.encodingMap);
/*     */   }
/*     */   
/*     */   public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator, CharMap charMap) {
/*  92 */     return encodeLines(in, iOff, iLen, lineLen, lineSeparator, charMap.encodingMap);
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
/*     */   public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator, char[] charMap) {
/* 104 */     int blockLen = lineLen * 3 / 4;
/* 105 */     if (blockLen <= 0) {
/* 106 */       throw new IllegalArgumentException();
/*     */     }
/* 108 */     int lines = (iLen + blockLen - 1) / blockLen;
/* 109 */     int bufLen = (iLen + 2) / 3 * 4 + lines * lineSeparator.length();
/* 110 */     StringBuilder buf = new StringBuilder(bufLen);
/* 111 */     int ip = 0;
/* 112 */     while (ip < iLen) {
/* 113 */       int l = Math.min(iLen - ip, blockLen);
/* 114 */       buf.append(encode(in, iOff + ip, l, charMap));
/* 115 */       buf.append(lineSeparator);
/* 116 */       ip += l;
/*     */     } 
/* 118 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] encode(byte[] in) {
/* 125 */     return encode(in, regularMap.encodingMap);
/*     */   }
/*     */   
/*     */   public static char[] encode(byte[] in, CharMap charMap) {
/* 129 */     return encode(in, 0, in.length, charMap);
/*     */   }
/*     */   
/*     */   public static char[] encode(byte[] in, char[] charMap) {
/* 133 */     return encode(in, 0, in.length, charMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] encode(byte[] in, int iLen) {
/* 141 */     return encode(in, 0, iLen, regularMap.encodingMap);
/*     */   }
/*     */   
/*     */   public static char[] encode(byte[] in, int iOff, int iLen, CharMap charMap) {
/* 145 */     return encode(in, iOff, iLen, charMap.encodingMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] encode(byte[] in, int iOff, int iLen, char[] charMap) {
/* 155 */     int oDataLen = (iLen * 4 + 2) / 3;
/* 156 */     int oLen = (iLen + 2) / 3 * 4;
/* 157 */     char[] out = new char[oLen];
/* 158 */     int ip = iOff;
/* 159 */     int iEnd = iOff + iLen;
/* 160 */     int op = 0;
/* 161 */     while (ip < iEnd) {
/* 162 */       int i0 = in[ip++] & 0xFF;
/* 163 */       int i1 = (ip < iEnd) ? (in[ip++] & 0xFF) : 0;
/* 164 */       int i2 = (ip < iEnd) ? (in[ip++] & 0xFF) : 0;
/* 165 */       int o0 = i0 >>> 2;
/* 166 */       int o1 = (i0 & 0x3) << 4 | i1 >>> 4;
/* 167 */       int o2 = (i1 & 0xF) << 2 | i2 >>> 6;
/* 168 */       int o3 = i2 & 0x3F;
/* 169 */       out[op++] = charMap[o0];
/* 170 */       out[op++] = charMap[o1];
/* 171 */       out[op] = (op < oDataLen) ? charMap[o2] : '=';
/* 172 */       op++;
/* 173 */       out[op] = (op < oDataLen) ? charMap[o3] : '=';
/* 174 */       op++;
/*     */     } 
/* 176 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String decodeString(String s) {
/* 184 */     return decodeString(s, false);
/*     */   }
/*     */   
/*     */   public static String decodeString(String s, boolean useUrlSafeEncoding) {
/* 188 */     return new String(decode(s.toCharArray(), useUrlSafeEncoding ? urlsafeMap.decodingMap : regularMap.decodingMap));
/*     */   }
/*     */   
/*     */   public static byte[] decodeLines(String s) {
/* 192 */     return decodeLines(s, regularMap.decodingMap);
/*     */   }
/*     */   
/*     */   public static byte[] decodeLines(String s, CharMap inverseCharMap) {
/* 196 */     return decodeLines(s, inverseCharMap.decodingMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeLines(String s, byte[] inverseCharMap) {
/* 206 */     char[] buf = new char[s.length()];
/* 207 */     int p = 0;
/* 208 */     for (int ip = 0; ip < s.length(); ip++) {
/* 209 */       char c = s.charAt(ip);
/* 210 */       if (c != ' ' && c != '\r' && c != '\n' && c != '\t') {
/* 211 */         buf[p++] = c;
/*     */       }
/*     */     } 
/* 214 */     return decode(buf, 0, p, inverseCharMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(String s) {
/* 222 */     return decode(s.toCharArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(String s, CharMap inverseCharMap) {
/* 231 */     return decode(s.toCharArray(), inverseCharMap);
/*     */   }
/*     */   
/*     */   public static byte[] decode(char[] in, byte[] inverseCharMap) {
/* 235 */     return decode(in, 0, in.length, inverseCharMap);
/*     */   }
/*     */   
/*     */   public static byte[] decode(char[] in, CharMap inverseCharMap) {
/* 239 */     return decode(in, 0, in.length, inverseCharMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(char[] in) {
/* 247 */     return decode(in, 0, in.length, regularMap.decodingMap);
/*     */   }
/*     */   
/*     */   public static byte[] decode(char[] in, int iOff, int iLen, CharMap inverseCharMap) {
/* 251 */     return decode(in, iOff, iLen, inverseCharMap.decodingMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(char[] in, int iOff, int iLen, byte[] inverseCharMap) {
/* 262 */     if (iLen % 4 != 0) {
/* 263 */       throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
/*     */     }
/* 265 */     while (iLen > 0 && in[iOff + iLen - 1] == '=') {
/* 266 */       iLen--;
/*     */     }
/* 268 */     int oLen = iLen * 3 / 4;
/* 269 */     byte[] out = new byte[oLen];
/* 270 */     int ip = iOff;
/* 271 */     int iEnd = iOff + iLen;
/* 272 */     int op = 0;
/* 273 */     while (ip < iEnd) {
/* 274 */       int i0 = in[ip++];
/* 275 */       int i1 = in[ip++];
/* 276 */       int i2 = (ip < iEnd) ? in[ip++] : 65;
/* 277 */       int i3 = (ip < iEnd) ? in[ip++] : 65;
/* 278 */       if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
/* 279 */         throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
/*     */       }
/* 281 */       int b0 = inverseCharMap[i0];
/* 282 */       int b1 = inverseCharMap[i1];
/* 283 */       int b2 = inverseCharMap[i2];
/* 284 */       int b3 = inverseCharMap[i3];
/* 285 */       if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
/* 286 */         throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
/*     */       }
/* 288 */       int o0 = b0 << 2 | b1 >>> 4;
/* 289 */       int o1 = (b1 & 0xF) << 4 | b2 >>> 2;
/* 290 */       int o2 = (b2 & 0x3) << 6 | b3;
/* 291 */       out[op++] = (byte)o0;
/* 292 */       if (op < oLen) {
/* 293 */         out[op++] = (byte)o1;
/*     */       }
/* 295 */       if (op < oLen) {
/* 296 */         out[op++] = (byte)o2;
/*     */       }
/*     */     } 
/* 299 */     return out;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Base64Coder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */