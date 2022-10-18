/*     */ package com.google.gson;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JsonArray
/*     */   extends JsonElement
/*     */   implements Iterable<JsonElement>
/*     */ {
/*  40 */   private final List<JsonElement> elements = new ArrayList<JsonElement>();
/*     */ 
/*     */ 
/*     */   
/*     */   JsonArray deepCopy() {
/*  45 */     JsonArray result = new JsonArray();
/*  46 */     for (JsonElement element : this.elements) {
/*  47 */       result.add(element.deepCopy());
/*     */     }
/*  49 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Boolean bool) {
/*  58 */     this.elements.add((bool == null) ? JsonNull.INSTANCE : new JsonPrimitive(bool));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Character character) {
/*  67 */     this.elements.add((character == null) ? JsonNull.INSTANCE : new JsonPrimitive(character));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Number number) {
/*  76 */     this.elements.add((number == null) ? JsonNull.INSTANCE : new JsonPrimitive(number));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(String string) {
/*  85 */     this.elements.add((string == null) ? JsonNull.INSTANCE : new JsonPrimitive(string));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(JsonElement element) {
/*  94 */     if (element == null) {
/*  95 */       element = JsonNull.INSTANCE;
/*     */     }
/*  97 */     this.elements.add(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(JsonArray array) {
/* 106 */     this.elements.addAll(array.elements);
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
/*     */   public JsonElement set(int index, JsonElement element) {
/* 118 */     return this.elements.set(index, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(JsonElement element) {
/* 129 */     return this.elements.remove(element);
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
/*     */   public JsonElement remove(int index) {
/* 142 */     return this.elements.remove(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(JsonElement element) {
/* 152 */     return this.elements.contains(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 161 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JsonElement> iterator() {
/* 171 */     return this.elements.iterator();
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
/*     */   public JsonElement get(int i) {
/* 183 */     return this.elements.get(i);
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
/*     */   public Number getAsNumber() {
/* 196 */     if (this.elements.size() == 1) {
/* 197 */       return ((JsonElement)this.elements.get(0)).getAsNumber();
/*     */     }
/* 199 */     throw new IllegalStateException();
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
/*     */   public String getAsString() {
/* 212 */     if (this.elements.size() == 1) {
/* 213 */       return ((JsonElement)this.elements.get(0)).getAsString();
/*     */     }
/* 215 */     throw new IllegalStateException();
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
/*     */   public double getAsDouble() {
/* 228 */     if (this.elements.size() == 1) {
/* 229 */       return ((JsonElement)this.elements.get(0)).getAsDouble();
/*     */     }
/* 231 */     throw new IllegalStateException();
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
/*     */   public BigDecimal getAsBigDecimal() {
/* 245 */     if (this.elements.size() == 1) {
/* 246 */       return ((JsonElement)this.elements.get(0)).getAsBigDecimal();
/*     */     }
/* 248 */     throw new IllegalStateException();
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
/*     */   public BigInteger getAsBigInteger() {
/* 262 */     if (this.elements.size() == 1) {
/* 263 */       return ((JsonElement)this.elements.get(0)).getAsBigInteger();
/*     */     }
/* 265 */     throw new IllegalStateException();
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
/*     */   public float getAsFloat() {
/* 278 */     if (this.elements.size() == 1) {
/* 279 */       return ((JsonElement)this.elements.get(0)).getAsFloat();
/*     */     }
/* 281 */     throw new IllegalStateException();
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
/*     */   public long getAsLong() {
/* 294 */     if (this.elements.size() == 1) {
/* 295 */       return ((JsonElement)this.elements.get(0)).getAsLong();
/*     */     }
/* 297 */     throw new IllegalStateException();
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
/*     */   public int getAsInt() {
/* 310 */     if (this.elements.size() == 1) {
/* 311 */       return ((JsonElement)this.elements.get(0)).getAsInt();
/*     */     }
/* 313 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 318 */     if (this.elements.size() == 1) {
/* 319 */       return ((JsonElement)this.elements.get(0)).getAsByte();
/*     */     }
/* 321 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public char getAsCharacter() {
/* 326 */     if (this.elements.size() == 1) {
/* 327 */       return ((JsonElement)this.elements.get(0)).getAsCharacter();
/*     */     }
/* 329 */     throw new IllegalStateException();
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
/*     */   public short getAsShort() {
/* 342 */     if (this.elements.size() == 1) {
/* 343 */       return ((JsonElement)this.elements.get(0)).getAsShort();
/*     */     }
/* 345 */     throw new IllegalStateException();
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
/*     */   public boolean getAsBoolean() {
/* 358 */     if (this.elements.size() == 1) {
/* 359 */       return ((JsonElement)this.elements.get(0)).getAsBoolean();
/*     */     }
/* 361 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 366 */     return (o == this || (o instanceof JsonArray && ((JsonArray)o).elements.equals(this.elements)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 371 */     return this.elements.hashCode();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\JsonArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */