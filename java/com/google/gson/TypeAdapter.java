/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.bind.JsonTreeReader;
/*     */ import com.google.gson.internal.bind.JsonTreeWriter;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
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
/*     */ public abstract class TypeAdapter<T>
/*     */ {
/*     */   public abstract void write(JsonWriter paramJsonWriter, T paramT) throws IOException;
/*     */   
/*     */   public final void toJson(Writer out, T value) throws IOException {
/* 141 */     JsonWriter writer = new JsonWriter(out);
/* 142 */     write(writer, value);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeAdapter<T> nullSafe() {
/* 186 */     return new TypeAdapter<T>() {
/*     */         public void write(JsonWriter out, T value) throws IOException {
/* 188 */           if (value == null) {
/* 189 */             out.nullValue();
/*     */           } else {
/* 191 */             TypeAdapter.this.write(out, value);
/*     */           } 
/*     */         }
/*     */         public T read(JsonReader reader) throws IOException {
/* 195 */           if (reader.peek() == JsonToken.NULL) {
/* 196 */             reader.nextNull();
/* 197 */             return null;
/*     */           } 
/* 199 */           return TypeAdapter.this.read(reader);
/*     */         }
/*     */       };
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
/*     */   public final String toJson(T value) {
/* 215 */     StringWriter stringWriter = new StringWriter();
/*     */     try {
/* 217 */       toJson(stringWriter, value);
/* 218 */     } catch (IOException e) {
/* 219 */       throw new AssertionError(e);
/*     */     } 
/* 221 */     return stringWriter.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JsonElement toJsonTree(T value) {
/*     */     try {
/* 233 */       JsonTreeWriter jsonWriter = new JsonTreeWriter();
/* 234 */       write((JsonWriter)jsonWriter, value);
/* 235 */       return jsonWriter.get();
/* 236 */     } catch (IOException e) {
/* 237 */       throw new JsonIOException(e);
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
/*     */   public abstract T read(JsonReader paramJsonReader) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T fromJson(Reader in) throws IOException {
/* 259 */     JsonReader reader = new JsonReader(in);
/* 260 */     return read(reader);
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
/*     */   public final T fromJson(String json) throws IOException {
/* 273 */     return fromJson(new StringReader(json));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T fromJsonTree(JsonElement jsonTree) {
/*     */     try {
/* 284 */       JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonTree);
/* 285 */       return read((JsonReader)jsonTreeReader);
/* 286 */     } catch (IOException e) {
/* 287 */       throw new JsonIOException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\TypeAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */