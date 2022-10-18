/*     */ package com.google.gson.internal.bind;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonIOException;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import com.google.gson.internal.LazilyParsedNumber;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.BitSet;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TypeAdapters
/*     */ {
/*     */   private TypeAdapters() {
/*  59 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*  63 */   public static final TypeAdapter<Class> CLASS = new TypeAdapter<Class>()
/*     */     {
/*     */       public void write(JsonWriter out, Class value) throws IOException {
/*  66 */         if (value == null) {
/*  67 */           out.nullValue();
/*     */         } else {
/*  69 */           throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + value.getName() + ". Forgot to register a type adapter?");
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Class read(JsonReader in) throws IOException {
/*  75 */         if (in.peek() == JsonToken.NULL) {
/*  76 */           in.nextNull();
/*  77 */           return null;
/*     */         } 
/*  79 */         throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  84 */   public static final TypeAdapterFactory CLASS_FACTORY = newFactory(Class.class, CLASS);
/*     */   
/*  86 */   public static final TypeAdapter<BitSet> BIT_SET = new TypeAdapter<BitSet>() {
/*     */       public BitSet read(JsonReader in) throws IOException {
/*  88 */         if (in.peek() == JsonToken.NULL) {
/*  89 */           in.nextNull();
/*  90 */           return null;
/*     */         } 
/*     */         
/*  93 */         BitSet bitset = new BitSet();
/*  94 */         in.beginArray();
/*  95 */         int i = 0;
/*  96 */         JsonToken tokenType = in.peek();
/*  97 */         while (tokenType != JsonToken.END_ARRAY) {
/*     */           boolean set; String stringValue;
/*  99 */           switch (tokenType) {
/*     */             case NUMBER:
/* 101 */               set = (in.nextInt() != 0);
/*     */               break;
/*     */             case BOOLEAN:
/* 104 */               set = in.nextBoolean();
/*     */               break;
/*     */             case STRING:
/* 107 */               stringValue = in.nextString();
/*     */               try {
/* 109 */                 set = (Integer.parseInt(stringValue) != 0);
/* 110 */               } catch (NumberFormatException e) {
/* 111 */                 throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + stringValue);
/*     */               } 
/*     */               break;
/*     */             
/*     */             default:
/* 116 */               throw new JsonSyntaxException("Invalid bitset value type: " + tokenType);
/*     */           } 
/* 118 */           if (set) {
/* 119 */             bitset.set(i);
/*     */           }
/* 121 */           i++;
/* 122 */           tokenType = in.peek();
/*     */         } 
/* 124 */         in.endArray();
/* 125 */         return bitset;
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BitSet src) throws IOException {
/* 129 */         if (src == null) {
/* 130 */           out.nullValue();
/*     */           
/*     */           return;
/*     */         } 
/* 134 */         out.beginArray();
/* 135 */         for (int i = 0; i < src.length(); i++) {
/* 136 */           int value = src.get(i) ? 1 : 0;
/* 137 */           out.value(value);
/*     */         } 
/* 139 */         out.endArray();
/*     */       }
/*     */     };
/*     */   
/* 143 */   public static final TypeAdapterFactory BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
/*     */   
/* 145 */   public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>()
/*     */     {
/*     */       public Boolean read(JsonReader in) throws IOException {
/* 148 */         if (in.peek() == JsonToken.NULL) {
/* 149 */           in.nextNull();
/* 150 */           return null;
/* 151 */         }  if (in.peek() == JsonToken.STRING)
/*     */         {
/* 153 */           return Boolean.valueOf(Boolean.parseBoolean(in.nextString()));
/*     */         }
/* 155 */         return Boolean.valueOf(in.nextBoolean());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Boolean value) throws IOException {
/* 159 */         if (value == null) {
/* 160 */           out.nullValue();
/*     */           return;
/*     */         } 
/* 163 */         out.value(value.booleanValue());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
/*     */       public Boolean read(JsonReader in) throws IOException {
/* 173 */         if (in.peek() == JsonToken.NULL) {
/* 174 */           in.nextNull();
/* 175 */           return null;
/*     */         } 
/* 177 */         return Boolean.valueOf(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Boolean value) throws IOException {
/* 181 */         out.value((value == null) ? "null" : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 185 */   public static final TypeAdapterFactory BOOLEAN_FACTORY = newFactory(boolean.class, (Class)Boolean.class, (TypeAdapter)BOOLEAN);
/*     */ 
/*     */   
/* 188 */   public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 191 */         if (in.peek() == JsonToken.NULL) {
/* 192 */           in.nextNull();
/* 193 */           return null;
/*     */         } 
/*     */         try {
/* 196 */           int intValue = in.nextInt();
/* 197 */           return Byte.valueOf((byte)intValue);
/* 198 */         } catch (NumberFormatException e) {
/* 199 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 204 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 208 */   public static final TypeAdapterFactory BYTE_FACTORY = newFactory(byte.class, (Class)Byte.class, (TypeAdapter)BYTE);
/*     */ 
/*     */   
/* 211 */   public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 214 */         if (in.peek() == JsonToken.NULL) {
/* 215 */           in.nextNull();
/* 216 */           return null;
/*     */         } 
/*     */         try {
/* 219 */           return Short.valueOf((short)in.nextInt());
/* 220 */         } catch (NumberFormatException e) {
/* 221 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 226 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 230 */   public static final TypeAdapterFactory SHORT_FACTORY = newFactory(short.class, (Class)Short.class, (TypeAdapter)SHORT);
/*     */ 
/*     */   
/* 233 */   public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 236 */         if (in.peek() == JsonToken.NULL) {
/* 237 */           in.nextNull();
/* 238 */           return null;
/*     */         } 
/*     */         try {
/* 241 */           return Integer.valueOf(in.nextInt());
/* 242 */         } catch (NumberFormatException e) {
/* 243 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 248 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 252 */   public static final TypeAdapterFactory INTEGER_FACTORY = newFactory(int.class, (Class)Integer.class, (TypeAdapter)INTEGER);
/*     */ 
/*     */   
/* 255 */   public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 258 */         if (in.peek() == JsonToken.NULL) {
/* 259 */           in.nextNull();
/* 260 */           return null;
/*     */         } 
/*     */         try {
/* 263 */           return Long.valueOf(in.nextLong());
/* 264 */         } catch (NumberFormatException e) {
/* 265 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 270 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 274 */   public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 277 */         if (in.peek() == JsonToken.NULL) {
/* 278 */           in.nextNull();
/* 279 */           return null;
/*     */         } 
/* 281 */         return Float.valueOf((float)in.nextDouble());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 285 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 289 */   public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 292 */         if (in.peek() == JsonToken.NULL) {
/* 293 */           in.nextNull();
/* 294 */           return null;
/*     */         } 
/* 296 */         return Double.valueOf(in.nextDouble());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 300 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 304 */   public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 307 */         JsonToken jsonToken = in.peek();
/* 308 */         switch (jsonToken) {
/*     */           case NULL:
/* 310 */             in.nextNull();
/* 311 */             return null;
/*     */           case NUMBER:
/* 313 */             return (Number)new LazilyParsedNumber(in.nextString());
/*     */         } 
/* 315 */         throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 320 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 324 */   public static final TypeAdapterFactory NUMBER_FACTORY = newFactory(Number.class, NUMBER);
/*     */   
/* 326 */   public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>()
/*     */     {
/*     */       public Character read(JsonReader in) throws IOException {
/* 329 */         if (in.peek() == JsonToken.NULL) {
/* 330 */           in.nextNull();
/* 331 */           return null;
/*     */         } 
/* 333 */         String str = in.nextString();
/* 334 */         if (str.length() != 1) {
/* 335 */           throw new JsonSyntaxException("Expecting character, got: " + str);
/*     */         }
/* 337 */         return Character.valueOf(str.charAt(0));
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Character value) throws IOException {
/* 341 */         out.value((value == null) ? null : String.valueOf(value));
/*     */       }
/*     */     };
/*     */   
/* 345 */   public static final TypeAdapterFactory CHARACTER_FACTORY = newFactory(char.class, (Class)Character.class, (TypeAdapter)CHARACTER);
/*     */ 
/*     */   
/* 348 */   public static final TypeAdapter<String> STRING = new TypeAdapter<String>()
/*     */     {
/*     */       public String read(JsonReader in) throws IOException {
/* 351 */         JsonToken peek = in.peek();
/* 352 */         if (peek == JsonToken.NULL) {
/* 353 */           in.nextNull();
/* 354 */           return null;
/*     */         } 
/*     */         
/* 357 */         if (peek == JsonToken.BOOLEAN) {
/* 358 */           return Boolean.toString(in.nextBoolean());
/*     */         }
/* 360 */         return in.nextString();
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, String value) throws IOException {
/* 364 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 368 */   public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
/*     */       public BigDecimal read(JsonReader in) throws IOException {
/* 370 */         if (in.peek() == JsonToken.NULL) {
/* 371 */           in.nextNull();
/* 372 */           return null;
/*     */         } 
/*     */         try {
/* 375 */           return new BigDecimal(in.nextString());
/* 376 */         } catch (NumberFormatException e) {
/* 377 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BigDecimal value) throws IOException {
/* 382 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 386 */   public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() {
/*     */       public BigInteger read(JsonReader in) throws IOException {
/* 388 */         if (in.peek() == JsonToken.NULL) {
/* 389 */           in.nextNull();
/* 390 */           return null;
/*     */         } 
/*     */         try {
/* 393 */           return new BigInteger(in.nextString());
/* 394 */         } catch (NumberFormatException e) {
/* 395 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BigInteger value) throws IOException {
/* 400 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 404 */   public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, STRING);
/*     */   
/* 406 */   public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>()
/*     */     {
/*     */       public StringBuilder read(JsonReader in) throws IOException {
/* 409 */         if (in.peek() == JsonToken.NULL) {
/* 410 */           in.nextNull();
/* 411 */           return null;
/*     */         } 
/* 413 */         return new StringBuilder(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, StringBuilder value) throws IOException {
/* 417 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 421 */   public static final TypeAdapterFactory STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
/*     */ 
/*     */   
/* 424 */   public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>()
/*     */     {
/*     */       public StringBuffer read(JsonReader in) throws IOException {
/* 427 */         if (in.peek() == JsonToken.NULL) {
/* 428 */           in.nextNull();
/* 429 */           return null;
/*     */         } 
/* 431 */         return new StringBuffer(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, StringBuffer value) throws IOException {
/* 435 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 439 */   public static final TypeAdapterFactory STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
/*     */ 
/*     */   
/* 442 */   public static final TypeAdapter<URL> URL = new TypeAdapter<URL>()
/*     */     {
/*     */       public URL read(JsonReader in) throws IOException {
/* 445 */         if (in.peek() == JsonToken.NULL) {
/* 446 */           in.nextNull();
/* 447 */           return null;
/*     */         } 
/* 449 */         String nextString = in.nextString();
/* 450 */         return "null".equals(nextString) ? null : new URL(nextString);
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, URL value) throws IOException {
/* 454 */         out.value((value == null) ? null : value.toExternalForm());
/*     */       }
/*     */     };
/*     */   
/* 458 */   public static final TypeAdapterFactory URL_FACTORY = newFactory(URL.class, URL);
/*     */   
/* 460 */   public static final TypeAdapter<URI> URI = new TypeAdapter<URI>()
/*     */     {
/*     */       public URI read(JsonReader in) throws IOException {
/* 463 */         if (in.peek() == JsonToken.NULL) {
/* 464 */           in.nextNull();
/* 465 */           return null;
/*     */         } 
/*     */         try {
/* 468 */           String nextString = in.nextString();
/* 469 */           return "null".equals(nextString) ? null : new URI(nextString);
/* 470 */         } catch (URISyntaxException e) {
/* 471 */           throw new JsonIOException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, URI value) throws IOException {
/* 476 */         out.value((value == null) ? null : value.toASCIIString());
/*     */       }
/*     */     };
/*     */   
/* 480 */   public static final TypeAdapterFactory URI_FACTORY = newFactory(URI.class, URI);
/*     */   
/* 482 */   public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>()
/*     */     {
/*     */       public InetAddress read(JsonReader in) throws IOException {
/* 485 */         if (in.peek() == JsonToken.NULL) {
/* 486 */           in.nextNull();
/* 487 */           return null;
/*     */         } 
/*     */         
/* 490 */         return InetAddress.getByName(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, InetAddress value) throws IOException {
/* 494 */         out.value((value == null) ? null : value.getHostAddress());
/*     */       }
/*     */     };
/*     */   
/* 498 */   public static final TypeAdapterFactory INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
/*     */ 
/*     */   
/* 501 */   public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>()
/*     */     {
/*     */       public UUID read(JsonReader in) throws IOException {
/* 504 */         if (in.peek() == JsonToken.NULL) {
/* 505 */           in.nextNull();
/* 506 */           return null;
/*     */         } 
/* 508 */         return UUID.fromString(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, UUID value) throws IOException {
/* 512 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 516 */   public static final TypeAdapterFactory UUID_FACTORY = newFactory(UUID.class, UUID);
/*     */   
/* 518 */   public static final TypeAdapterFactory TIMESTAMP_FACTORY = new TypeAdapterFactory()
/*     */     {
/*     */       public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 521 */         if (typeToken.getRawType() != Timestamp.class) {
/* 522 */           return null;
/*     */         }
/*     */         
/* 525 */         final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
/* 526 */         return (TypeAdapter)new TypeAdapter<Timestamp>() {
/*     */             public Timestamp read(JsonReader in) throws IOException {
/* 528 */               Date date = (Date)dateTypeAdapter.read(in);
/* 529 */               return (date != null) ? new Timestamp(date.getTime()) : null;
/*     */             }
/*     */             
/*     */             public void write(JsonWriter out, Timestamp value) throws IOException {
/* 533 */               dateTypeAdapter.write(out, value);
/*     */             }
/*     */           };
/*     */       }
/*     */     };
/*     */   
/* 539 */   public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>()
/*     */     {
/*     */       private static final String YEAR = "year";
/*     */       private static final String MONTH = "month";
/*     */       private static final String DAY_OF_MONTH = "dayOfMonth";
/*     */       private static final String HOUR_OF_DAY = "hourOfDay";
/*     */       private static final String MINUTE = "minute";
/*     */       private static final String SECOND = "second";
/*     */       
/*     */       public Calendar read(JsonReader in) throws IOException {
/* 549 */         if (in.peek() == JsonToken.NULL) {
/* 550 */           in.nextNull();
/* 551 */           return null;
/*     */         } 
/* 553 */         in.beginObject();
/* 554 */         int year = 0;
/* 555 */         int month = 0;
/* 556 */         int dayOfMonth = 0;
/* 557 */         int hourOfDay = 0;
/* 558 */         int minute = 0;
/* 559 */         int second = 0;
/* 560 */         while (in.peek() != JsonToken.END_OBJECT) {
/* 561 */           String name = in.nextName();
/* 562 */           int value = in.nextInt();
/* 563 */           if ("year".equals(name)) {
/* 564 */             year = value; continue;
/* 565 */           }  if ("month".equals(name)) {
/* 566 */             month = value; continue;
/* 567 */           }  if ("dayOfMonth".equals(name)) {
/* 568 */             dayOfMonth = value; continue;
/* 569 */           }  if ("hourOfDay".equals(name)) {
/* 570 */             hourOfDay = value; continue;
/* 571 */           }  if ("minute".equals(name)) {
/* 572 */             minute = value; continue;
/* 573 */           }  if ("second".equals(name)) {
/* 574 */             second = value;
/*     */           }
/*     */         } 
/* 577 */         in.endObject();
/* 578 */         return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Calendar value) throws IOException {
/* 583 */         if (value == null) {
/* 584 */           out.nullValue();
/*     */           return;
/*     */         } 
/* 587 */         out.beginObject();
/* 588 */         out.name("year");
/* 589 */         out.value(value.get(1));
/* 590 */         out.name("month");
/* 591 */         out.value(value.get(2));
/* 592 */         out.name("dayOfMonth");
/* 593 */         out.value(value.get(5));
/* 594 */         out.name("hourOfDay");
/* 595 */         out.value(value.get(11));
/* 596 */         out.name("minute");
/* 597 */         out.value(value.get(12));
/* 598 */         out.name("second");
/* 599 */         out.value(value.get(13));
/* 600 */         out.endObject();
/*     */       }
/*     */     };
/*     */   
/* 604 */   public static final TypeAdapterFactory CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, (Class)GregorianCalendar.class, CALENDAR);
/*     */ 
/*     */   
/* 607 */   public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>()
/*     */     {
/*     */       public Locale read(JsonReader in) throws IOException {
/* 610 */         if (in.peek() == JsonToken.NULL) {
/* 611 */           in.nextNull();
/* 612 */           return null;
/*     */         } 
/* 614 */         String locale = in.nextString();
/* 615 */         StringTokenizer tokenizer = new StringTokenizer(locale, "_");
/* 616 */         String language = null;
/* 617 */         String country = null;
/* 618 */         String variant = null;
/* 619 */         if (tokenizer.hasMoreElements()) {
/* 620 */           language = tokenizer.nextToken();
/*     */         }
/* 622 */         if (tokenizer.hasMoreElements()) {
/* 623 */           country = tokenizer.nextToken();
/*     */         }
/* 625 */         if (tokenizer.hasMoreElements()) {
/* 626 */           variant = tokenizer.nextToken();
/*     */         }
/* 628 */         if (country == null && variant == null)
/* 629 */           return new Locale(language); 
/* 630 */         if (variant == null) {
/* 631 */           return new Locale(language, country);
/*     */         }
/* 633 */         return new Locale(language, country, variant);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Locale value) throws IOException {
/* 638 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 642 */   public static final TypeAdapterFactory LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
/*     */   
/* 644 */   public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() { public JsonElement read(JsonReader in) throws IOException { String number; JsonArray array;
/*     */         JsonObject object;
/* 646 */         switch (in.peek()) {
/*     */           case STRING:
/* 648 */             return (JsonElement)new JsonPrimitive(in.nextString());
/*     */           case NUMBER:
/* 650 */             number = in.nextString();
/* 651 */             return (JsonElement)new JsonPrimitive((Number)new LazilyParsedNumber(number));
/*     */           case BOOLEAN:
/* 653 */             return (JsonElement)new JsonPrimitive(Boolean.valueOf(in.nextBoolean()));
/*     */           case NULL:
/* 655 */             in.nextNull();
/* 656 */             return (JsonElement)JsonNull.INSTANCE;
/*     */           case BEGIN_ARRAY:
/* 658 */             array = new JsonArray();
/* 659 */             in.beginArray();
/* 660 */             while (in.hasNext()) {
/* 661 */               array.add(read(in));
/*     */             }
/* 663 */             in.endArray();
/* 664 */             return (JsonElement)array;
/*     */           case BEGIN_OBJECT:
/* 666 */             object = new JsonObject();
/* 667 */             in.beginObject();
/* 668 */             while (in.hasNext()) {
/* 669 */               object.add(in.nextName(), read(in));
/*     */             }
/* 671 */             in.endObject();
/* 672 */             return (JsonElement)object;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 678 */         throw new IllegalArgumentException(); }
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, JsonElement value) throws IOException {
/* 683 */         if (value == null || value.isJsonNull()) {
/* 684 */           out.nullValue();
/* 685 */         } else if (value.isJsonPrimitive()) {
/* 686 */           JsonPrimitive primitive = value.getAsJsonPrimitive();
/* 687 */           if (primitive.isNumber()) {
/* 688 */             out.value(primitive.getAsNumber());
/* 689 */           } else if (primitive.isBoolean()) {
/* 690 */             out.value(primitive.getAsBoolean());
/*     */           } else {
/* 692 */             out.value(primitive.getAsString());
/*     */           }
/*     */         
/* 695 */         } else if (value.isJsonArray()) {
/* 696 */           out.beginArray();
/* 697 */           for (JsonElement e : value.getAsJsonArray()) {
/* 698 */             write(out, e);
/*     */           }
/* 700 */           out.endArray();
/*     */         }
/* 702 */         else if (value.isJsonObject()) {
/* 703 */           out.beginObject();
/* 704 */           for (Map.Entry<String, JsonElement> e : (Iterable<Map.Entry<String, JsonElement>>)value.getAsJsonObject().entrySet()) {
/* 705 */             out.name(e.getKey());
/* 706 */             write(out, e.getValue());
/*     */           } 
/* 708 */           out.endObject();
/*     */         } else {
/*     */           
/* 711 */           throw new IllegalArgumentException("Couldn't write " + value.getClass());
/*     */         } 
/*     */       } }
/*     */   ;
/*     */   
/* 716 */   public static final TypeAdapterFactory JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
/*     */   
/*     */   private static final class EnumTypeAdapter<T extends Enum<T>>
/*     */     extends TypeAdapter<T> {
/* 720 */     private final Map<String, T> nameToConstant = new HashMap<String, T>();
/* 721 */     private final Map<T, String> constantToName = new HashMap<T, String>();
/*     */     
/*     */     public EnumTypeAdapter(Class<T> classOfT) {
/*     */       try {
/* 725 */         for (Enum enum_ : (Enum[])classOfT.getEnumConstants()) {
/* 726 */           String name = enum_.name();
/* 727 */           SerializedName annotation = classOfT.getField(name).<SerializedName>getAnnotation(SerializedName.class);
/* 728 */           if (annotation != null) {
/* 729 */             name = annotation.value();
/* 730 */             for (String alternate : annotation.alternate()) {
/* 731 */               this.nameToConstant.put(alternate, (T)enum_);
/*     */             }
/*     */           } 
/* 734 */           this.nameToConstant.put(name, (T)enum_);
/* 735 */           this.constantToName.put((T)enum_, name);
/*     */         } 
/* 737 */       } catch (NoSuchFieldException e) {
/* 738 */         throw new AssertionError();
/*     */       } 
/*     */     }
/*     */     public T read(JsonReader in) throws IOException {
/* 742 */       if (in.peek() == JsonToken.NULL) {
/* 743 */         in.nextNull();
/* 744 */         return null;
/*     */       } 
/* 746 */       return this.nameToConstant.get(in.nextString());
/*     */     }
/*     */     
/*     */     public void write(JsonWriter out, T value) throws IOException {
/* 750 */       out.value((value == null) ? null : this.constantToName.get(value));
/*     */     }
/*     */   }
/*     */   
/* 754 */   public static final TypeAdapterFactory ENUM_FACTORY = new TypeAdapterFactory()
/*     */     {
/*     */       public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 757 */         Class<? super T> rawType = typeToken.getRawType();
/* 758 */         if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
/* 759 */           return null;
/*     */         }
/* 761 */         if (!rawType.isEnum()) {
/* 762 */           rawType = rawType.getSuperclass();
/*     */         }
/* 764 */         return new TypeAdapters.EnumTypeAdapter<T>(rawType);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
/* 770 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 773 */           return typeToken.equals(type) ? typeAdapter : null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
/* 780 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 783 */           return (typeToken.getRawType() == type) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 786 */           return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
/* 793 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 796 */           Class<? super T> rawType = typeToken.getRawType();
/* 797 */           return (rawType == unboxed || rawType == boxed) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 800 */           return "Factory[type=" + boxed.getName() + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
/* 808 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 811 */           Class<? super T> rawType = typeToken.getRawType();
/* 812 */           return (rawType == base || rawType == sub) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 815 */           return "Factory[type=" + base.getName() + "+" + sub.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newTypeHierarchyFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
/* 823 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 826 */           return clazz.isAssignableFrom(typeToken.getRawType()) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 829 */           return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\bind\TypeAdapters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */