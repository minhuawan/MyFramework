/*     */ package com.badlogic.gdx.tools.hiero;
/*     */ 
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.IntIntMap;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Kerning
/*     */ {
/*     */   private TTFInputStream input;
/*     */   private float scale;
/*  36 */   private int headOffset = -1;
/*  37 */   private int kernOffset = -1;
/*  38 */   private int gposOffset = -1;
/*  39 */   private IntIntMap kernings = new IntIntMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(InputStream inputStream, int fontSize) throws IOException {
/*  45 */     if (inputStream == null) throw new IllegalArgumentException("inputStream cannot be null."); 
/*  46 */     this.input = new TTFInputStream(inputStream);
/*  47 */     inputStream.close();
/*     */     
/*  49 */     readTableDirectory();
/*  50 */     if (this.headOffset == -1) throw new IOException("HEAD table not found."); 
/*  51 */     readHEAD(fontSize);
/*     */ 
/*     */ 
/*     */     
/*  55 */     if (this.gposOffset != -1) {
/*  56 */       this.input.seek(this.gposOffset);
/*  57 */       readGPOS();
/*     */     } 
/*  59 */     if (this.kernOffset != -1) {
/*  60 */       this.input.seek(this.kernOffset);
/*  61 */       readKERN();
/*     */     } 
/*  63 */     this.input.close();
/*  64 */     this.input = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IntIntMap getKernings() {
/*  70 */     return this.kernings;
/*     */   }
/*     */ 
/*     */   
/*     */   private void storeKerningOffset(int firstGlyphCode, int secondGlyphCode, int offset) {
/*  75 */     int value = Math.round(offset * this.scale);
/*  76 */     if (value == 0) {
/*     */       return;
/*     */     }
/*  79 */     int key = firstGlyphCode << 16 | secondGlyphCode;
/*  80 */     this.kernings.put(key, value);
/*     */   }
/*     */   
/*     */   private void readTableDirectory() throws IOException {
/*  84 */     this.input.skip(4L);
/*  85 */     int tableCount = this.input.readUnsignedShort();
/*  86 */     this.input.skip(6L);
/*     */     
/*  88 */     byte[] tagBytes = new byte[4];
/*  89 */     for (int i = 0; i < tableCount; i++) {
/*  90 */       tagBytes[0] = this.input.readByte();
/*  91 */       tagBytes[1] = this.input.readByte();
/*  92 */       tagBytes[2] = this.input.readByte();
/*  93 */       tagBytes[3] = this.input.readByte();
/*  94 */       this.input.skip(4L);
/*  95 */       int offset = (int)this.input.readUnsignedLong();
/*  96 */       this.input.skip(4L);
/*     */       
/*  98 */       String tag = new String(tagBytes, "ISO-8859-1");
/*  99 */       if (tag.equals("head")) {
/* 100 */         this.headOffset = offset;
/* 101 */       } else if (tag.equals("kern")) {
/* 102 */         this.kernOffset = offset;
/* 103 */       } else if (tag.equals("GPOS")) {
/* 104 */         this.gposOffset = offset;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readHEAD(int fontSize) throws IOException {
/* 110 */     this.input.seek(this.headOffset + 8 + 8 + 2);
/* 111 */     int unitsPerEm = this.input.readUnsignedShort();
/* 112 */     this.scale = fontSize / unitsPerEm;
/*     */   }
/*     */   
/*     */   private void readKERN() throws IOException {
/* 116 */     this.input.seek(this.kernOffset + 2);
/* 117 */     for (int subTableCount = this.input.readUnsignedShort(); subTableCount > 0; subTableCount--) {
/* 118 */       this.input.skip(4L);
/* 119 */       int tupleIndex = this.input.readUnsignedShort();
/* 120 */       if ((tupleIndex & 0x1) == 0 || (tupleIndex & 0x2) != 0 || (tupleIndex & 0x4) != 0)
/* 121 */         return;  if (tupleIndex >> 8 == 0) {
/*     */         
/* 123 */         int kerningCount = this.input.readUnsignedShort();
/* 124 */         this.input.skip(6L);
/* 125 */         while (kerningCount-- > 0) {
/* 126 */           int firstGlyphCode = this.input.readUnsignedShort();
/* 127 */           int secondGlyphCode = this.input.readUnsignedShort();
/* 128 */           int offset = this.input.readShort();
/* 129 */           storeKerningOffset(firstGlyphCode, secondGlyphCode, offset);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readGPOS() throws IOException {
/* 137 */     this.input.seek(this.gposOffset + 4 + 2 + 2);
/* 138 */     int lookupListOffset = this.input.readUnsignedShort();
/* 139 */     this.input.seek(this.gposOffset + lookupListOffset);
/*     */     
/* 141 */     int lookupListPosition = this.input.getPosition();
/* 142 */     int lookupCount = this.input.readUnsignedShort();
/* 143 */     int[] lookupOffsets = this.input.readUnsignedShortArray(lookupCount);
/*     */     
/* 145 */     for (int i = 0; i < lookupCount; i++) {
/* 146 */       int lookupPosition = lookupListPosition + lookupOffsets[i];
/* 147 */       this.input.seek(lookupPosition);
/* 148 */       int type = this.input.readUnsignedShort();
/* 149 */       readSubtables(type, lookupPosition);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readSubtables(int type, int lookupPosition) throws IOException {
/* 154 */     this.input.skip(2L);
/* 155 */     int subTableCount = this.input.readUnsignedShort();
/* 156 */     int[] subTableOffsets = this.input.readUnsignedShortArray(subTableCount);
/*     */     
/* 158 */     for (int i = 0; i < subTableCount; i++) {
/* 159 */       int subTablePosition = lookupPosition + subTableOffsets[i];
/* 160 */       readSubtable(type, subTablePosition);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readSubtable(int type, int subTablePosition) throws IOException {
/* 165 */     this.input.seek(subTablePosition);
/* 166 */     if (type == 2) {
/* 167 */       readPairAdjustmentSubtable(subTablePosition);
/* 168 */     } else if (type == 9) {
/* 169 */       readExtensionPositioningSubtable(subTablePosition);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readPairAdjustmentSubtable(int subTablePosition) throws IOException {
/* 174 */     int type = this.input.readUnsignedShort();
/* 175 */     if (type == 1) {
/* 176 */       readPairPositioningAdjustmentFormat1(subTablePosition);
/* 177 */     } else if (type == 2) {
/* 178 */       readPairPositioningAdjustmentFormat2(subTablePosition);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readExtensionPositioningSubtable(int subTablePosition) throws IOException {
/* 183 */     int type = this.input.readUnsignedShort();
/* 184 */     if (type == 1) {
/* 185 */       readExtensionPositioningFormat1(subTablePosition);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readPairPositioningAdjustmentFormat1(long subTablePosition) throws IOException {
/* 190 */     int coverageOffset = this.input.readUnsignedShort();
/* 191 */     int valueFormat1 = this.input.readUnsignedShort();
/* 192 */     int valueFormat2 = this.input.readUnsignedShort();
/* 193 */     int pairSetCount = this.input.readUnsignedShort();
/* 194 */     int[] pairSetOffsets = this.input.readUnsignedShortArray(pairSetCount);
/*     */     
/* 196 */     this.input.seek((int)(subTablePosition + coverageOffset));
/* 197 */     int[] coverage = readCoverageTable();
/*     */ 
/*     */     
/* 200 */     pairSetCount = Math.min(pairSetCount, coverage.length);
/*     */     
/* 202 */     for (int i = 0; i < pairSetCount; i++) {
/* 203 */       int firstGlyph = coverage[i];
/* 204 */       this.input.seek((int)(subTablePosition + pairSetOffsets[i]));
/* 205 */       int pairValueCount = this.input.readUnsignedShort();
/* 206 */       for (int j = 0; j < pairValueCount; j++) {
/* 207 */         int secondGlyph = this.input.readUnsignedShort();
/* 208 */         int xAdvance1 = readXAdvanceFromValueRecord(valueFormat1);
/* 209 */         readXAdvanceFromValueRecord(valueFormat2);
/* 210 */         if (xAdvance1 != 0) {
/* 211 */           storeKerningOffset(firstGlyph, secondGlyph, xAdvance1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readPairPositioningAdjustmentFormat2(int subTablePosition) throws IOException {
/* 218 */     int coverageOffset = this.input.readUnsignedShort();
/* 219 */     int valueFormat1 = this.input.readUnsignedShort();
/* 220 */     int valueFormat2 = this.input.readUnsignedShort();
/* 221 */     int classDefOffset1 = this.input.readUnsignedShort();
/* 222 */     int classDefOffset2 = this.input.readUnsignedShort();
/* 223 */     int class1Count = this.input.readUnsignedShort();
/* 224 */     int class2Count = this.input.readUnsignedShort();
/*     */     
/* 226 */     int position = this.input.getPosition();
/*     */     
/* 228 */     this.input.seek(subTablePosition + coverageOffset);
/* 229 */     int[] coverage = readCoverageTable();
/*     */     
/* 231 */     this.input.seek(position);
/* 232 */     IntArray[] glyphsByClass1 = readClassDefinition(subTablePosition + classDefOffset1, class1Count);
/* 233 */     IntArray[] glyphsByClass2 = readClassDefinition(subTablePosition + classDefOffset2, class2Count);
/* 234 */     this.input.seek(position);
/*     */     int i;
/* 236 */     for (i = 0; i < coverage.length; i++) {
/* 237 */       int glyph = coverage[i];
/* 238 */       boolean found = false;
/* 239 */       for (int j = 1; j < class1Count && !found; j++) {
/* 240 */         found = glyphsByClass1[j].contains(glyph);
/*     */       }
/* 242 */       if (!found) {
/* 243 */         glyphsByClass1[0].add(glyph);
/*     */       }
/*     */     } 
/*     */     
/* 247 */     for (i = 0; i < class1Count; i++) {
/* 248 */       for (int j = 0; j < class2Count; j++) {
/* 249 */         int xAdvance1 = readXAdvanceFromValueRecord(valueFormat1);
/* 250 */         readXAdvanceFromValueRecord(valueFormat2);
/* 251 */         if (xAdvance1 != 0)
/* 252 */           for (int k = 0; k < (glyphsByClass1[i]).size; k++) {
/* 253 */             int glyph1 = (glyphsByClass1[i]).items[k];
/* 254 */             for (int l = 0; l < (glyphsByClass2[j]).size; l++) {
/* 255 */               int glyph2 = (glyphsByClass2[j]).items[l];
/* 256 */               storeKerningOffset(glyph1, glyph2, xAdvance1);
/*     */             } 
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readExtensionPositioningFormat1(int subTablePosition) throws IOException {
/* 264 */     int lookupType = this.input.readUnsignedShort();
/* 265 */     int lookupPosition = subTablePosition + (int)this.input.readUnsignedLong();
/* 266 */     readSubtable(lookupType, lookupPosition);
/*     */   }
/*     */   
/*     */   private IntArray[] readClassDefinition(int position, int classCount) throws IOException {
/* 270 */     this.input.seek(position);
/*     */     
/* 272 */     IntArray[] glyphsByClass = new IntArray[classCount];
/* 273 */     for (int i = 0; i < classCount; i++) {
/* 274 */       glyphsByClass[i] = new IntArray();
/*     */     }
/*     */     
/* 277 */     int classFormat = this.input.readUnsignedShort();
/* 278 */     if (classFormat == 1) {
/* 279 */       readClassDefinitionFormat1(glyphsByClass);
/* 280 */     } else if (classFormat == 2) {
/* 281 */       readClassDefinitionFormat2(glyphsByClass);
/*     */     } else {
/* 283 */       throw new IOException("Unknown class definition table type " + classFormat);
/*     */     } 
/* 285 */     return glyphsByClass;
/*     */   }
/*     */   
/*     */   private void readClassDefinitionFormat1(IntArray[] glyphsByClass) throws IOException {
/* 289 */     int startGlyph = this.input.readUnsignedShort();
/* 290 */     int glyphCount = this.input.readUnsignedShort();
/* 291 */     int[] classValueArray = this.input.readUnsignedShortArray(glyphCount);
/* 292 */     for (int i = 0; i < glyphCount; i++) {
/* 293 */       int glyph = startGlyph + i;
/* 294 */       int glyphClass = classValueArray[i];
/* 295 */       if (glyphClass < glyphsByClass.length) {
/* 296 */         glyphsByClass[glyphClass].add(glyph);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readClassDefinitionFormat2(IntArray[] glyphsByClass) throws IOException {
/* 302 */     int classRangeCount = this.input.readUnsignedShort();
/* 303 */     for (int i = 0; i < classRangeCount; i++) {
/* 304 */       int start = this.input.readUnsignedShort();
/* 305 */       int end = this.input.readUnsignedShort();
/* 306 */       int glyphClass = this.input.readUnsignedShort();
/* 307 */       if (glyphClass < glyphsByClass.length) {
/* 308 */         for (int glyph = start; glyph <= end; glyph++) {
/* 309 */           glyphsByClass[glyphClass].add(glyph);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] readCoverageTable() throws IOException {
/* 316 */     int format = this.input.readUnsignedShort();
/* 317 */     if (format == 1) {
/* 318 */       int glyphCount = this.input.readUnsignedShort();
/* 319 */       int[] glyphArray = this.input.readUnsignedShortArray(glyphCount);
/* 320 */       return glyphArray;
/* 321 */     }  if (format == 2) {
/* 322 */       int rangeCount = this.input.readUnsignedShort();
/* 323 */       IntArray glyphArray = new IntArray();
/* 324 */       for (int i = 0; i < rangeCount; i++) {
/* 325 */         int start = this.input.readUnsignedShort();
/* 326 */         int end = this.input.readUnsignedShort();
/* 327 */         this.input.skip(2L);
/* 328 */         for (int glyph = start; glyph <= end; glyph++) {
/* 329 */           glyphArray.add(glyph);
/*     */         }
/*     */       } 
/* 332 */       return glyphArray.shrink();
/*     */     } 
/* 334 */     throw new IOException("Unknown coverage table format " + format);
/*     */   }
/*     */   
/*     */   private int readXAdvanceFromValueRecord(int valueFormat) throws IOException {
/* 338 */     int xAdvance = 0;
/* 339 */     for (int mask = 1; mask <= 32768 && mask <= valueFormat; mask <<= 1) {
/* 340 */       if ((valueFormat & mask) != 0) {
/* 341 */         int value = this.input.readShort();
/* 342 */         if (mask == 4) {
/* 343 */           xAdvance = value;
/*     */         }
/*     */       } 
/*     */     } 
/* 347 */     return xAdvance;
/*     */   }
/*     */   
/*     */   private static class TTFInputStream extends ByteArrayInputStream {
/*     */     public TTFInputStream(InputStream input) throws IOException {
/* 352 */       super(readAllBytes(input));
/*     */     }
/*     */     
/*     */     private static byte[] readAllBytes(InputStream input) throws IOException {
/* 356 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */       
/* 358 */       byte[] buffer = new byte[16384]; int numRead;
/* 359 */       while ((numRead = input.read(buffer, 0, buffer.length)) != -1) {
/* 360 */         out.write(buffer, 0, numRead);
/*     */       }
/* 362 */       return out.toByteArray();
/*     */     }
/*     */     
/*     */     public int getPosition() {
/* 366 */       return this.pos;
/*     */     }
/*     */     
/*     */     public void seek(int position) {
/* 370 */       this.pos = position;
/*     */     }
/*     */     
/*     */     public int readUnsignedByte() throws IOException {
/* 374 */       int b = read();
/* 375 */       if (b == -1) throw new EOFException("Unexpected end of file."); 
/* 376 */       return b;
/*     */     }
/*     */     
/*     */     public byte readByte() throws IOException {
/* 380 */       return (byte)readUnsignedByte();
/*     */     }
/*     */     
/*     */     public int readUnsignedShort() throws IOException {
/* 384 */       return (readUnsignedByte() << 8) + readUnsignedByte();
/*     */     }
/*     */     
/*     */     public short readShort() throws IOException {
/* 388 */       return (short)readUnsignedShort();
/*     */     }
/*     */     
/*     */     public long readUnsignedLong() throws IOException {
/* 392 */       long value = readUnsignedByte();
/* 393 */       value = (value << 8L) + readUnsignedByte();
/* 394 */       value = (value << 8L) + readUnsignedByte();
/* 395 */       value = (value << 8L) + readUnsignedByte();
/* 396 */       return value;
/*     */     }
/*     */     
/*     */     public int[] readUnsignedShortArray(int count) throws IOException {
/* 400 */       int[] shorts = new int[count];
/* 401 */       for (int i = 0; i < count; i++) {
/* 402 */         shorts[i] = readUnsignedShort();
/*     */       }
/* 404 */       return shorts;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hiero\Kerning.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */