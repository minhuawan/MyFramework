/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.ConstructorConstructor;
/*     */ import com.google.gson.internal.Excluder;
/*     */ import com.google.gson.internal.Primitives;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.internal.bind.ArrayTypeAdapter;
/*     */ import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
/*     */ import com.google.gson.internal.bind.DateTypeAdapter;
/*     */ import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
/*     */ import com.google.gson.internal.bind.JsonTreeReader;
/*     */ import com.google.gson.internal.bind.JsonTreeWriter;
/*     */ import com.google.gson.internal.bind.MapTypeAdapterFactory;
/*     */ import com.google.gson.internal.bind.ObjectTypeAdapter;
/*     */ import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
/*     */ import com.google.gson.internal.bind.SqlDateTypeAdapter;
/*     */ import com.google.gson.internal.bind.TimeTypeAdapter;
/*     */ import com.google.gson.internal.bind.TypeAdapters;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import com.google.gson.stream.MalformedJsonException;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Type;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Gson
/*     */ {
/*     */   static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
/*     */   private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
/* 112 */   private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();
/*     */ 
/*     */   
/* 115 */   private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = Collections.synchronizedMap(new HashMap<TypeToken<?>, TypeAdapter<?>>());
/*     */   
/*     */   private final List<TypeAdapterFactory> factories;
/*     */   
/*     */   private final ConstructorConstructor constructorConstructor;
/*     */   
/*     */   private final boolean serializeNulls;
/*     */   private final boolean htmlSafe;
/*     */   private final boolean generateNonExecutableJson;
/*     */   private final boolean prettyPrinting;
/*     */   
/* 126 */   final JsonDeserializationContext deserializationContext = new JsonDeserializationContext()
/*     */     {
/*     */       public <T> T deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
/* 129 */         return Gson.this.fromJson(json, typeOfT);
/*     */       }
/*     */     };
/*     */   
/* 133 */   final JsonSerializationContext serializationContext = new JsonSerializationContext() {
/*     */       public JsonElement serialize(Object src) {
/* 135 */         return Gson.this.toJsonTree(src);
/*     */       }
/*     */       public JsonElement serialize(Object src, Type typeOfSrc) {
/* 138 */         return Gson.this.toJsonTree(src, typeOfSrc);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Gson() {
/* 177 */     this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
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
/*     */   Gson(Excluder excluder, FieldNamingStrategy fieldNamingPolicy, Map<Type, InstanceCreator<?>> instanceCreators, boolean serializeNulls, boolean complexMapKeySerialization, boolean generateNonExecutableGson, boolean htmlSafe, boolean prettyPrinting, boolean serializeSpecialFloatingPointValues, LongSerializationPolicy longSerializationPolicy, List<TypeAdapterFactory> typeAdapterFactories) {
/* 189 */     this.constructorConstructor = new ConstructorConstructor(instanceCreators);
/* 190 */     this.serializeNulls = serializeNulls;
/* 191 */     this.generateNonExecutableJson = generateNonExecutableGson;
/* 192 */     this.htmlSafe = htmlSafe;
/* 193 */     this.prettyPrinting = prettyPrinting;
/*     */     
/* 195 */     List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
/*     */ 
/*     */     
/* 198 */     factories.add(TypeAdapters.JSON_ELEMENT_FACTORY);
/* 199 */     factories.add(ObjectTypeAdapter.FACTORY);
/*     */ 
/*     */     
/* 202 */     factories.add(excluder);
/*     */ 
/*     */     
/* 205 */     factories.addAll(typeAdapterFactories);
/*     */ 
/*     */     
/* 208 */     factories.add(TypeAdapters.STRING_FACTORY);
/* 209 */     factories.add(TypeAdapters.INTEGER_FACTORY);
/* 210 */     factories.add(TypeAdapters.BOOLEAN_FACTORY);
/* 211 */     factories.add(TypeAdapters.BYTE_FACTORY);
/* 212 */     factories.add(TypeAdapters.SHORT_FACTORY);
/* 213 */     factories.add(TypeAdapters.newFactory(long.class, Long.class, longAdapter(longSerializationPolicy)));
/*     */     
/* 215 */     factories.add(TypeAdapters.newFactory(double.class, Double.class, doubleAdapter(serializeSpecialFloatingPointValues)));
/*     */     
/* 217 */     factories.add(TypeAdapters.newFactory(float.class, Float.class, floatAdapter(serializeSpecialFloatingPointValues)));
/*     */     
/* 219 */     factories.add(TypeAdapters.NUMBER_FACTORY);
/* 220 */     factories.add(TypeAdapters.CHARACTER_FACTORY);
/* 221 */     factories.add(TypeAdapters.STRING_BUILDER_FACTORY);
/* 222 */     factories.add(TypeAdapters.STRING_BUFFER_FACTORY);
/* 223 */     factories.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
/* 224 */     factories.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
/* 225 */     factories.add(TypeAdapters.URL_FACTORY);
/* 226 */     factories.add(TypeAdapters.URI_FACTORY);
/* 227 */     factories.add(TypeAdapters.UUID_FACTORY);
/* 228 */     factories.add(TypeAdapters.LOCALE_FACTORY);
/* 229 */     factories.add(TypeAdapters.INET_ADDRESS_FACTORY);
/* 230 */     factories.add(TypeAdapters.BIT_SET_FACTORY);
/* 231 */     factories.add(DateTypeAdapter.FACTORY);
/* 232 */     factories.add(TypeAdapters.CALENDAR_FACTORY);
/* 233 */     factories.add(TimeTypeAdapter.FACTORY);
/* 234 */     factories.add(SqlDateTypeAdapter.FACTORY);
/* 235 */     factories.add(TypeAdapters.TIMESTAMP_FACTORY);
/* 236 */     factories.add(ArrayTypeAdapter.FACTORY);
/* 237 */     factories.add(TypeAdapters.CLASS_FACTORY);
/*     */ 
/*     */     
/* 240 */     factories.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
/* 241 */     factories.add(new MapTypeAdapterFactory(this.constructorConstructor, complexMapKeySerialization));
/* 242 */     factories.add(new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor));
/* 243 */     factories.add(TypeAdapters.ENUM_FACTORY);
/* 244 */     factories.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingPolicy, excluder));
/*     */ 
/*     */     
/* 247 */     this.factories = Collections.unmodifiableList(factories);
/*     */   }
/*     */   
/*     */   private TypeAdapter<Number> doubleAdapter(boolean serializeSpecialFloatingPointValues) {
/* 251 */     if (serializeSpecialFloatingPointValues) {
/* 252 */       return TypeAdapters.DOUBLE;
/*     */     }
/* 254 */     return new TypeAdapter<Number>() {
/*     */         public Double read(JsonReader in) throws IOException {
/* 256 */           if (in.peek() == JsonToken.NULL) {
/* 257 */             in.nextNull();
/* 258 */             return null;
/*     */           } 
/* 260 */           return Double.valueOf(in.nextDouble());
/*     */         }
/*     */         public void write(JsonWriter out, Number value) throws IOException {
/* 263 */           if (value == null) {
/* 264 */             out.nullValue();
/*     */             return;
/*     */           } 
/* 267 */           double doubleValue = value.doubleValue();
/* 268 */           Gson.this.checkValidFloatingPoint(doubleValue);
/* 269 */           out.value(value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private TypeAdapter<Number> floatAdapter(boolean serializeSpecialFloatingPointValues) {
/* 275 */     if (serializeSpecialFloatingPointValues) {
/* 276 */       return TypeAdapters.FLOAT;
/*     */     }
/* 278 */     return new TypeAdapter<Number>() {
/*     */         public Float read(JsonReader in) throws IOException {
/* 280 */           if (in.peek() == JsonToken.NULL) {
/* 281 */             in.nextNull();
/* 282 */             return null;
/*     */           } 
/* 284 */           return Float.valueOf((float)in.nextDouble());
/*     */         }
/*     */         public void write(JsonWriter out, Number value) throws IOException {
/* 287 */           if (value == null) {
/* 288 */             out.nullValue();
/*     */             return;
/*     */           } 
/* 291 */           float floatValue = value.floatValue();
/* 292 */           Gson.this.checkValidFloatingPoint(floatValue);
/* 293 */           out.value(value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private void checkValidFloatingPoint(double value) {
/* 299 */     if (Double.isNaN(value) || Double.isInfinite(value)) {
/* 300 */       throw new IllegalArgumentException(value + " is not a valid double value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
/* 307 */     if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
/* 308 */       return TypeAdapters.LONG;
/*     */     }
/* 310 */     return new TypeAdapter<Number>() {
/*     */         public Number read(JsonReader in) throws IOException {
/* 312 */           if (in.peek() == JsonToken.NULL) {
/* 313 */             in.nextNull();
/* 314 */             return null;
/*     */           } 
/* 316 */           return Long.valueOf(in.nextLong());
/*     */         }
/*     */         public void write(JsonWriter out, Number value) throws IOException {
/* 319 */           if (value == null) {
/* 320 */             out.nullValue();
/*     */             return;
/*     */           } 
/* 323 */           out.value(value.toString());
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
/*     */   public <T> TypeAdapter<T> getAdapter(TypeToken<T> type) {
/* 336 */     TypeAdapter<?> cached = this.typeTokenCache.get(type);
/* 337 */     if (cached != null) {
/* 338 */       return (TypeAdapter)cached;
/*     */     }
/*     */     
/* 341 */     Map<TypeToken<?>, FutureTypeAdapter<?>> threadCalls = this.calls.get();
/* 342 */     boolean requiresThreadLocalCleanup = false;
/* 343 */     if (threadCalls == null) {
/* 344 */       threadCalls = new HashMap<TypeToken<?>, FutureTypeAdapter<?>>();
/* 345 */       this.calls.set(threadCalls);
/* 346 */       requiresThreadLocalCleanup = true;
/*     */     } 
/*     */ 
/*     */     
/* 350 */     FutureTypeAdapter<T> ongoingCall = (FutureTypeAdapter<T>)threadCalls.get(type);
/* 351 */     if (ongoingCall != null) {
/* 352 */       return ongoingCall;
/*     */     }
/*     */     
/*     */     try {
/* 356 */       FutureTypeAdapter<T> call = new FutureTypeAdapter<T>();
/* 357 */       threadCalls.put(type, call);
/*     */       
/* 359 */       for (TypeAdapterFactory factory : this.factories) {
/* 360 */         TypeAdapter<T> candidate = factory.create(this, type);
/* 361 */         if (candidate != null) {
/* 362 */           call.setDelegate(candidate);
/* 363 */           this.typeTokenCache.put(type, candidate);
/* 364 */           return candidate;
/*     */         } 
/*     */       } 
/* 367 */       throw new IllegalArgumentException("GSON cannot handle " + type);
/*     */     } finally {
/* 369 */       threadCalls.remove(type);
/*     */       
/* 371 */       if (requiresThreadLocalCleanup) {
/* 372 */         this.calls.remove();
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type) {
/* 428 */     boolean skipPastFound = false;
/*     */ 
/*     */ 
/*     */     
/* 432 */     if (!this.factories.contains(skipPast)) skipPastFound = true;
/*     */     
/* 434 */     for (TypeAdapterFactory factory : this.factories) {
/* 435 */       if (!skipPastFound) {
/* 436 */         if (factory == skipPast) {
/* 437 */           skipPastFound = true;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 442 */       TypeAdapter<T> candidate = factory.create(this, type);
/* 443 */       if (candidate != null) {
/* 444 */         return candidate;
/*     */       }
/*     */     } 
/* 447 */     throw new IllegalArgumentException("GSON cannot serialize " + type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> TypeAdapter<T> getAdapter(Class<T> type) {
/* 457 */     return getAdapter(TypeToken.get(type));
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
/*     */   public JsonElement toJsonTree(Object src) {
/* 474 */     if (src == null) {
/* 475 */       return JsonNull.INSTANCE;
/*     */     }
/* 477 */     return toJsonTree(src, src.getClass());
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
/*     */   public JsonElement toJsonTree(Object src, Type typeOfSrc) {
/* 497 */     JsonTreeWriter writer = new JsonTreeWriter();
/* 498 */     toJson(src, typeOfSrc, (JsonWriter)writer);
/* 499 */     return writer.get();
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
/*     */   public String toJson(Object src) {
/* 516 */     if (src == null) {
/* 517 */       return toJson(JsonNull.INSTANCE);
/*     */     }
/* 519 */     return toJson(src, src.getClass());
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
/*     */   public String toJson(Object src, Type typeOfSrc) {
/* 538 */     StringWriter writer = new StringWriter();
/* 539 */     toJson(src, typeOfSrc, writer);
/* 540 */     return writer.toString();
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
/*     */   public void toJson(Object src, Appendable writer) throws JsonIOException {
/* 558 */     if (src != null) {
/* 559 */       toJson(src, src.getClass(), writer);
/*     */     } else {
/* 561 */       toJson(JsonNull.INSTANCE, writer);
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
/*     */   public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
/*     */     try {
/* 583 */       JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer));
/* 584 */       toJson(src, typeOfSrc, jsonWriter);
/* 585 */     } catch (IOException e) {
/* 586 */       throw new JsonIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
/* 597 */     TypeAdapter<?> adapter = getAdapter(TypeToken.get(typeOfSrc));
/* 598 */     boolean oldLenient = writer.isLenient();
/* 599 */     writer.setLenient(true);
/* 600 */     boolean oldHtmlSafe = writer.isHtmlSafe();
/* 601 */     writer.setHtmlSafe(this.htmlSafe);
/* 602 */     boolean oldSerializeNulls = writer.getSerializeNulls();
/* 603 */     writer.setSerializeNulls(this.serializeNulls);
/*     */     try {
/* 605 */       adapter.write(writer, src);
/* 606 */     } catch (IOException e) {
/* 607 */       throw new JsonIOException(e);
/*     */     } finally {
/* 609 */       writer.setLenient(oldLenient);
/* 610 */       writer.setHtmlSafe(oldHtmlSafe);
/* 611 */       writer.setSerializeNulls(oldSerializeNulls);
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
/*     */   public String toJson(JsonElement jsonElement) {
/* 623 */     StringWriter writer = new StringWriter();
/* 624 */     toJson(jsonElement, writer);
/* 625 */     return writer.toString();
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
/*     */   public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
/*     */     try {
/* 638 */       JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer));
/* 639 */       toJson(jsonElement, jsonWriter);
/* 640 */     } catch (IOException e) {
/* 641 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter newJsonWriter(Writer writer) throws IOException {
/* 649 */     if (this.generateNonExecutableJson) {
/* 650 */       writer.write(")]}'\n");
/*     */     }
/* 652 */     JsonWriter jsonWriter = new JsonWriter(writer);
/* 653 */     if (this.prettyPrinting) {
/* 654 */       jsonWriter.setIndent("  ");
/*     */     }
/* 656 */     jsonWriter.setSerializeNulls(this.serializeNulls);
/* 657 */     return jsonWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
/* 665 */     boolean oldLenient = writer.isLenient();
/* 666 */     writer.setLenient(true);
/* 667 */     boolean oldHtmlSafe = writer.isHtmlSafe();
/* 668 */     writer.setHtmlSafe(this.htmlSafe);
/* 669 */     boolean oldSerializeNulls = writer.getSerializeNulls();
/* 670 */     writer.setSerializeNulls(this.serializeNulls);
/*     */     try {
/* 672 */       Streams.write(jsonElement, writer);
/* 673 */     } catch (IOException e) {
/* 674 */       throw new JsonIOException(e);
/*     */     } finally {
/* 676 */       writer.setLenient(oldLenient);
/* 677 */       writer.setHtmlSafe(oldHtmlSafe);
/* 678 */       writer.setSerializeNulls(oldSerializeNulls);
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
/*     */   
/*     */   public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
/* 700 */     Object object = fromJson(json, classOfT);
/* 701 */     return Primitives.wrap(classOfT).cast(object);
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
/*     */   public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
/* 724 */     if (json == null) {
/* 725 */       return null;
/*     */     }
/* 727 */     StringReader reader = new StringReader(json);
/* 728 */     T target = fromJson(reader, typeOfT);
/* 729 */     return target;
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
/*     */   public <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
/* 751 */     JsonReader jsonReader = new JsonReader(json);
/* 752 */     Object object = fromJson(jsonReader, classOfT);
/* 753 */     assertFullConsumption(object, jsonReader);
/* 754 */     return Primitives.wrap(classOfT).cast(object);
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
/*     */   public <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
/* 778 */     JsonReader jsonReader = new JsonReader(json);
/* 779 */     T object = fromJson(jsonReader, typeOfT);
/* 780 */     assertFullConsumption(object, jsonReader);
/* 781 */     return object;
/*     */   }
/*     */   
/*     */   private static void assertFullConsumption(Object obj, JsonReader reader) {
/*     */     try {
/* 786 */       if (obj != null && reader.peek() != JsonToken.END_DOCUMENT) {
/* 787 */         throw new JsonIOException("JSON document was not fully consumed.");
/*     */       }
/* 789 */     } catch (MalformedJsonException e) {
/* 790 */       throw new JsonSyntaxException(e);
/* 791 */     } catch (IOException e) {
/* 792 */       throw new JsonIOException(e);
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
/*     */   public <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
/* 806 */     boolean isEmpty = true;
/* 807 */     boolean oldLenient = reader.isLenient();
/* 808 */     reader.setLenient(true);
/*     */     try {
/* 810 */       reader.peek();
/* 811 */       isEmpty = false;
/* 812 */       TypeToken<T> typeToken = TypeToken.get(typeOfT);
/* 813 */       TypeAdapter<T> typeAdapter = getAdapter(typeToken);
/* 814 */       T object = typeAdapter.read(reader);
/* 815 */       return object;
/* 816 */     } catch (EOFException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 821 */       if (isEmpty) {
/* 822 */         return null;
/*     */       }
/* 824 */       throw new JsonSyntaxException(e);
/* 825 */     } catch (IllegalStateException e) {
/* 826 */       throw new JsonSyntaxException(e);
/* 827 */     } catch (IOException e) {
/*     */       
/* 829 */       throw new JsonSyntaxException(e);
/*     */     } finally {
/* 831 */       reader.setLenient(oldLenient);
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
/*     */   public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
/* 852 */     Object object = fromJson(json, classOfT);
/* 853 */     return Primitives.wrap(classOfT).cast(object);
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
/*     */   public <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
/* 876 */     if (json == null) {
/* 877 */       return null;
/*     */     }
/* 879 */     return fromJson((JsonReader)new JsonTreeReader(json), typeOfT);
/*     */   }
/*     */   
/*     */   static class FutureTypeAdapter<T> extends TypeAdapter<T> {
/*     */     private TypeAdapter<T> delegate;
/*     */     
/*     */     public void setDelegate(TypeAdapter<T> typeAdapter) {
/* 886 */       if (this.delegate != null) {
/* 887 */         throw new AssertionError();
/*     */       }
/* 889 */       this.delegate = typeAdapter;
/*     */     }
/*     */     
/*     */     public T read(JsonReader in) throws IOException {
/* 893 */       if (this.delegate == null) {
/* 894 */         throw new IllegalStateException();
/*     */       }
/* 896 */       return this.delegate.read(in);
/*     */     }
/*     */     
/*     */     public void write(JsonWriter out, T value) throws IOException {
/* 900 */       if (this.delegate == null) {
/* 901 */         throw new IllegalStateException();
/*     */       }
/* 903 */       this.delegate.write(out, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 909 */     return "{serializeNulls:" + this.serializeNulls + "factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\Gson.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */