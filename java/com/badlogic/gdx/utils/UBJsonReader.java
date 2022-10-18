/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import java.io.DataInputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UBJsonReader
/*     */   implements BaseJsonReader
/*     */ {
/*     */   public boolean oldFormat = true;
/*     */   
/*     */   public JsonValue parse(InputStream input) {
/*  40 */     DataInputStream din = null;
/*     */     try {
/*  42 */       din = new DataInputStream(input);
/*  43 */       return parse(din);
/*  44 */     } catch (IOException ex) {
/*  45 */       throw new SerializationException(ex);
/*     */     } finally {
/*  47 */       StreamUtils.closeQuietly(din);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonValue parse(FileHandle file) {
/*     */     try {
/*  54 */       return parse(file.read(8192));
/*  55 */     } catch (Exception ex) {
/*  56 */       throw new SerializationException("Error parsing file: " + file, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public JsonValue parse(DataInputStream din) throws IOException {
/*     */     try {
/*  62 */       return parse(din, din.readByte());
/*     */     } finally {
/*  64 */       StreamUtils.closeQuietly(din);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected JsonValue parse(DataInputStream din, byte type) throws IOException {
/*  69 */     if (type == 91)
/*  70 */       return parseArray(din); 
/*  71 */     if (type == 123)
/*  72 */       return parseObject(din); 
/*  73 */     if (type == 90)
/*  74 */       return new JsonValue(JsonValue.ValueType.nullValue); 
/*  75 */     if (type == 84)
/*  76 */       return new JsonValue(true); 
/*  77 */     if (type == 70)
/*  78 */       return new JsonValue(false); 
/*  79 */     if (type == 66)
/*  80 */       return new JsonValue(readUChar(din)); 
/*  81 */     if (type == 85)
/*  82 */       return new JsonValue(readUChar(din)); 
/*  83 */     if (type == 105)
/*  84 */       return new JsonValue(this.oldFormat ? din.readShort() : din.readByte()); 
/*  85 */     if (type == 73)
/*  86 */       return new JsonValue(this.oldFormat ? din.readInt() : din.readShort()); 
/*  87 */     if (type == 108)
/*  88 */       return new JsonValue(din.readInt()); 
/*  89 */     if (type == 76)
/*  90 */       return new JsonValue(din.readLong()); 
/*  91 */     if (type == 100)
/*  92 */       return new JsonValue(din.readFloat()); 
/*  93 */     if (type == 68)
/*  94 */       return new JsonValue(din.readDouble()); 
/*  95 */     if (type == 115 || type == 83)
/*  96 */       return new JsonValue(parseString(din, type)); 
/*  97 */     if (type == 97 || type == 65)
/*  98 */       return parseData(din, type); 
/*  99 */     if (type == 67) {
/* 100 */       return new JsonValue(din.readChar());
/*     */     }
/* 102 */     throw new GdxRuntimeException("Unrecognized data type");
/*     */   }
/*     */   
/*     */   protected JsonValue parseArray(DataInputStream din) throws IOException {
/* 106 */     JsonValue result = new JsonValue(JsonValue.ValueType.array);
/* 107 */     byte type = din.readByte();
/* 108 */     byte valueType = 0;
/* 109 */     if (type == 36) {
/* 110 */       valueType = din.readByte();
/* 111 */       type = din.readByte();
/*     */     } 
/* 113 */     long size = -1L;
/* 114 */     if (type == 35) {
/* 115 */       size = parseSize(din, false, -1L);
/* 116 */       if (size < 0L) throw new GdxRuntimeException("Unrecognized data type"); 
/* 117 */       if (size == 0L) return result; 
/* 118 */       type = (valueType == 0) ? din.readByte() : valueType;
/*     */     } 
/* 120 */     JsonValue prev = null;
/* 121 */     long c = 0L;
/* 122 */     while (din.available() > 0 && type != 93) {
/* 123 */       JsonValue val = parse(din, type);
/* 124 */       val.parent = result;
/* 125 */       if (prev != null) {
/* 126 */         val.prev = prev;
/* 127 */         prev.next = val;
/* 128 */         result.size++;
/*     */       } else {
/* 130 */         result.child = val;
/* 131 */         result.size = 1;
/*     */       } 
/* 133 */       prev = val;
/* 134 */       if (size > 0L && ++c >= size)
/* 135 */         break;  type = (valueType == 0) ? din.readByte() : valueType;
/*     */     } 
/* 137 */     return result;
/*     */   }
/*     */   
/*     */   protected JsonValue parseObject(DataInputStream din) throws IOException {
/* 141 */     JsonValue result = new JsonValue(JsonValue.ValueType.object);
/* 142 */     byte type = din.readByte();
/* 143 */     byte valueType = 0;
/* 144 */     if (type == 36) {
/* 145 */       valueType = din.readByte();
/* 146 */       type = din.readByte();
/*     */     } 
/* 148 */     long size = -1L;
/* 149 */     if (type == 35) {
/* 150 */       size = parseSize(din, false, -1L);
/* 151 */       if (size < 0L) throw new GdxRuntimeException("Unrecognized data type"); 
/* 152 */       if (size == 0L) return result; 
/* 153 */       type = din.readByte();
/*     */     } 
/* 155 */     JsonValue prev = null;
/* 156 */     long c = 0L;
/* 157 */     while (din.available() > 0 && type != 125) {
/* 158 */       String key = parseString(din, true, type);
/* 159 */       JsonValue child = parse(din, (valueType == 0) ? din.readByte() : valueType);
/* 160 */       child.setName(key);
/* 161 */       child.parent = result;
/* 162 */       if (prev != null) {
/* 163 */         child.prev = prev;
/* 164 */         prev.next = child;
/* 165 */         result.size++;
/*     */       } else {
/* 167 */         result.child = child;
/* 168 */         result.size = 1;
/*     */       } 
/* 170 */       prev = child;
/* 171 */       if (size > 0L && ++c >= size)
/* 172 */         break;  type = din.readByte();
/*     */     } 
/* 174 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonValue parseData(DataInputStream din, byte blockType) throws IOException {
/* 180 */     byte dataType = din.readByte();
/* 181 */     long size = (blockType == 65) ? readUInt(din) : readUChar(din);
/* 182 */     JsonValue result = new JsonValue(JsonValue.ValueType.array);
/* 183 */     JsonValue prev = null; long i;
/* 184 */     for (i = 0L; i < size; i++) {
/* 185 */       JsonValue val = parse(din, dataType);
/* 186 */       val.parent = result;
/* 187 */       if (prev != null) {
/* 188 */         prev.next = val;
/* 189 */         result.size++;
/*     */       } else {
/* 191 */         result.child = val;
/* 192 */         result.size = 1;
/*     */       } 
/* 194 */       prev = val;
/*     */     } 
/* 196 */     return result;
/*     */   }
/*     */   
/*     */   protected String parseString(DataInputStream din, byte type) throws IOException {
/* 200 */     return parseString(din, false, type);
/*     */   }
/*     */   
/*     */   protected String parseString(DataInputStream din, boolean sOptional, byte type) throws IOException {
/* 204 */     long size = -1L;
/* 205 */     if (type == 83)
/* 206 */     { size = parseSize(din, true, -1L); }
/* 207 */     else if (type == 115)
/* 208 */     { size = readUChar(din); }
/* 209 */     else if (sOptional) { size = parseSize(din, type, false, -1L); }
/* 210 */      if (size < 0L) throw new GdxRuntimeException("Unrecognized data type, string expected"); 
/* 211 */     return (size > 0L) ? readString(din, size) : "";
/*     */   }
/*     */   
/*     */   protected long parseSize(DataInputStream din, boolean useIntOnError, long defaultValue) throws IOException {
/* 215 */     return parseSize(din, din.readByte(), useIntOnError, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long parseSize(DataInputStream din, byte type, boolean useIntOnError, long defaultValue) throws IOException {
/* 220 */     if (type == 105) return readUChar(din); 
/* 221 */     if (type == 73) return readUShort(din); 
/* 222 */     if (type == 108) return readUInt(din); 
/* 223 */     if (type == 76) return din.readLong(); 
/* 224 */     if (useIntOnError) {
/* 225 */       long result = ((short)type & 0xFF) << 24L;
/* 226 */       result |= ((short)din.readByte() & 0xFF) << 16L;
/* 227 */       result |= ((short)din.readByte() & 0xFF) << 8L;
/* 228 */       result |= ((short)din.readByte() & 0xFF);
/* 229 */       return result;
/*     */     } 
/* 231 */     return defaultValue;
/*     */   }
/*     */   
/*     */   protected short readUChar(DataInputStream din) throws IOException {
/* 235 */     return (short)((short)din.readByte() & 0xFF);
/*     */   }
/*     */   
/*     */   protected int readUShort(DataInputStream din) throws IOException {
/* 239 */     return din.readShort() & 0xFFFF;
/*     */   }
/*     */   
/*     */   protected long readUInt(DataInputStream din) throws IOException {
/* 243 */     return din.readInt() & 0xFFFFFFFFFFFFFFFFL;
/*     */   }
/*     */   
/*     */   protected String readString(DataInputStream din, long size) throws IOException {
/* 247 */     byte[] data = new byte[(int)size];
/* 248 */     din.readFully(data);
/* 249 */     return new String(data, "UTF-8");
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\UBJsonReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */