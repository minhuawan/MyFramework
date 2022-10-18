/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends Writer
/*     */ {
/*     */   final Writer writer;
/*  29 */   private final Array<JsonObject> stack = new Array<JsonObject>();
/*     */   private JsonObject current;
/*     */   private boolean named;
/*  32 */   private OutputType outputType = OutputType.json;
/*     */   private boolean quoteLongValues = false;
/*     */   
/*     */   public JsonWriter(Writer writer) {
/*  36 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   public Writer getWriter() {
/*  40 */     return this.writer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOutputType(OutputType outputType) {
/*  45 */     this.outputType = outputType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuoteLongValues(boolean quoteLongValues) {
/*  51 */     this.quoteLongValues = quoteLongValues;
/*     */   }
/*     */   
/*     */   public JsonWriter name(String name) throws IOException {
/*  55 */     if (this.current == null || this.current.array) throw new IllegalStateException("Current item must be an object."); 
/*  56 */     if (!this.current.needsComma) {
/*  57 */       this.current.needsComma = true;
/*     */     } else {
/*  59 */       this.writer.write(44);
/*  60 */     }  this.writer.write(this.outputType.quoteName(name));
/*  61 */     this.writer.write(58);
/*  62 */     this.named = true;
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public JsonWriter object() throws IOException {
/*  67 */     requireCommaOrName();
/*  68 */     this.stack.add(this.current = new JsonObject(false));
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public JsonWriter array() throws IOException {
/*  73 */     requireCommaOrName();
/*  74 */     this.stack.add(this.current = new JsonObject(true));
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public JsonWriter value(Object value) throws IOException {
/*  79 */     if (this.quoteLongValues && (value instanceof Long || value instanceof Double || value instanceof java.math.BigDecimal || value instanceof java.math.BigInteger)) {
/*     */       
/*  81 */       value = value.toString();
/*  82 */     } else if (value instanceof Number) {
/*  83 */       Number number = (Number)value;
/*  84 */       long longValue = number.longValue();
/*  85 */       if (number.doubleValue() == longValue) value = Long.valueOf(longValue); 
/*     */     } 
/*  87 */     requireCommaOrName();
/*  88 */     this.writer.write(this.outputType.quoteValue(value));
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonWriter json(String json) throws IOException {
/*  94 */     requireCommaOrName();
/*  95 */     this.writer.write(json);
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   private void requireCommaOrName() throws IOException {
/* 100 */     if (this.current == null)
/* 101 */       return;  if (this.current.array)
/* 102 */     { if (!this.current.needsComma) {
/* 103 */         this.current.needsComma = true;
/*     */       } else {
/* 105 */         this.writer.write(44);
/*     */       }  }
/* 107 */     else { if (!this.named) throw new IllegalStateException("Name must be set."); 
/* 108 */       this.named = false; }
/*     */   
/*     */   }
/*     */   
/*     */   public JsonWriter object(String name) throws IOException {
/* 113 */     return name(name).object();
/*     */   }
/*     */   
/*     */   public JsonWriter array(String name) throws IOException {
/* 117 */     return name(name).array();
/*     */   }
/*     */   
/*     */   public JsonWriter set(String name, Object value) throws IOException {
/* 121 */     return name(name).value(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonWriter json(String name, String json) throws IOException {
/* 126 */     return name(name).json(json);
/*     */   }
/*     */   
/*     */   public JsonWriter pop() throws IOException {
/* 130 */     if (this.named) throw new IllegalStateException("Expected an object, array, or value since a name was set."); 
/* 131 */     ((JsonObject)this.stack.pop()).close();
/* 132 */     this.current = (this.stack.size == 0) ? null : this.stack.peek();
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 137 */     this.writer.write(cbuf, off, len);
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 141 */     this.writer.flush();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 145 */     while (this.stack.size > 0)
/* 146 */       pop(); 
/* 147 */     this.writer.close();
/*     */   }
/*     */   
/*     */   private class JsonObject {
/*     */     final boolean array;
/*     */     boolean needsComma;
/*     */     
/*     */     JsonObject(boolean array) throws IOException {
/* 155 */       this.array = array;
/* 156 */       JsonWriter.this.writer.write(array ? 91 : 123);
/*     */     }
/*     */     
/*     */     void close() throws IOException {
/* 160 */       JsonWriter.this.writer.write(this.array ? 93 : 125);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum OutputType
/*     */   {
/* 166 */     json,
/*     */     
/* 168 */     javascript,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     minimal;
/*     */     
/* 181 */     private static Pattern javascriptPattern = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
/* 182 */     private static Pattern minimalNamePattern = Pattern.compile("^[^\":,}/ ][^:]*$");
/* 183 */     private static Pattern minimalValuePattern = Pattern.compile("^[^\":,{\\[\\]/ ][^}\\],]*$");
/*     */     
/*     */     public String quoteValue(Object value) {
/* 186 */       if (value == null) return "null"; 
/* 187 */       String string = value.toString();
/* 188 */       if (value instanceof Number || value instanceof Boolean) return string; 
/* 189 */       StringBuilder buffer = new StringBuilder(string);
/* 190 */       buffer.replace('\\', "\\\\").replace('\r', "\\r").replace('\n', "\\n").replace('\t', "\\t");
/* 191 */       if (this == minimal && !string.equals("true") && !string.equals("false") && !string.equals("null") && 
/* 192 */         !string.contains("//") && !string.contains("/*")) {
/* 193 */         int length = buffer.length();
/* 194 */         if (length > 0 && buffer.charAt(length - 1) != ' ' && minimalValuePattern.matcher(buffer).matches())
/* 195 */           return buffer.toString(); 
/*     */       } 
/* 197 */       return '"' + buffer.replace('"', "\\\"").toString() + '"';
/*     */     } static {
/*     */     
/*     */     } public String quoteName(String value) {
/* 201 */       StringBuilder buffer = new StringBuilder(value);
/* 202 */       buffer.replace('\\', "\\\\").replace('\r', "\\r").replace('\n', "\\n").replace('\t', "\\t");
/* 203 */       switch (this) {
/*     */         case minimal:
/* 205 */           if (!value.contains("//") && !value.contains("/*") && minimalNamePattern.matcher(buffer).matches())
/* 206 */             return buffer.toString(); 
/*     */         case javascript:
/* 208 */           if (javascriptPattern.matcher(buffer).matches()) return buffer.toString();  break;
/*     */       } 
/* 210 */       return '"' + buffer.replace('"', "\\\"").toString() + '"';
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\JsonWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */