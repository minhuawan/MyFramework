/*     */ package com.google.gson.internal.bind;
/*     */ 
/*     */ import com.google.gson.FieldNamingStrategy;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.annotations.JsonAdapter;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import com.google.gson.internal.;
/*     */ import com.google.gson.internal.ConstructorConstructor;
/*     */ import com.google.gson.internal.Excluder;
/*     */ import com.google.gson.internal.ObjectConstructor;
/*     */ import com.google.gson.internal.Primitives;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
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
/*     */ public final class ReflectiveTypeAdapterFactory
/*     */   implements TypeAdapterFactory
/*     */ {
/*     */   private final ConstructorConstructor constructorConstructor;
/*     */   private final FieldNamingStrategy fieldNamingPolicy;
/*     */   private final Excluder excluder;
/*     */   
/*     */   public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingPolicy, Excluder excluder) {
/*  56 */     this.constructorConstructor = constructorConstructor;
/*  57 */     this.fieldNamingPolicy = fieldNamingPolicy;
/*  58 */     this.excluder = excluder;
/*     */   }
/*     */   
/*     */   public boolean excludeField(Field f, boolean serialize) {
/*  62 */     return excludeField(f, serialize, this.excluder);
/*     */   }
/*     */   
/*     */   static boolean excludeField(Field f, boolean serialize, Excluder excluder) {
/*  66 */     return (!excluder.excludeClass(f.getType(), serialize) && !excluder.excludeField(f, serialize));
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String> getFieldNames(Field f) {
/*  71 */     return getFieldName(this.fieldNamingPolicy, f);
/*     */   }
/*     */ 
/*     */   
/*     */   static List<String> getFieldName(FieldNamingStrategy fieldNamingPolicy, Field f) {
/*  76 */     SerializedName serializedName = f.<SerializedName>getAnnotation(SerializedName.class);
/*  77 */     List<String> fieldNames = new LinkedList<String>();
/*  78 */     if (serializedName == null) {
/*  79 */       fieldNames.add(fieldNamingPolicy.translateName(f));
/*     */     } else {
/*  81 */       fieldNames.add(serializedName.value());
/*  82 */       for (String alternate : serializedName.alternate()) {
/*  83 */         fieldNames.add(alternate);
/*     */       }
/*     */     } 
/*  86 */     return fieldNames;
/*     */   }
/*     */   
/*     */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/*  90 */     Class<? super T> raw = type.getRawType();
/*     */     
/*  92 */     if (!Object.class.isAssignableFrom(raw)) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
/*  97 */     return new Adapter<T>(constructor, getBoundFields(gson, type, raw));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BoundField createBoundField(final Gson context, final Field field, String name, final TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
/* 103 */     final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
/*     */     
/* 105 */     return new BoundField(name, serialize, deserialize) {
/* 106 */         final TypeAdapter<?> typeAdapter = ReflectiveTypeAdapterFactory.this.getFieldAdapter(context, field, fieldType);
/*     */ 
/*     */         
/*     */         void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
/* 110 */           Object fieldValue = field.get(value);
/* 111 */           TypeAdapter t = new TypeAdapterRuntimeTypeWrapper(context, this.typeAdapter, fieldType.getType());
/*     */           
/* 113 */           t.write(writer, fieldValue);
/*     */         }
/*     */         
/*     */         void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
/* 117 */           Object fieldValue = this.typeAdapter.read(reader);
/* 118 */           if (fieldValue != null || !isPrimitive)
/* 119 */             field.set(value, fieldValue); 
/*     */         }
/*     */         
/*     */         public boolean writeField(Object value) throws IOException, IllegalAccessException {
/* 123 */           if (!this.serialized) return false; 
/* 124 */           Object fieldValue = field.get(value);
/* 125 */           return (fieldValue != value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private TypeAdapter<?> getFieldAdapter(Gson gson, Field field, TypeToken<?> fieldType) {
/* 131 */     JsonAdapter annotation = field.<JsonAdapter>getAnnotation(JsonAdapter.class);
/* 132 */     if (annotation != null) {
/* 133 */       TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, fieldType, annotation);
/* 134 */       if (adapter != null) return adapter; 
/*     */     } 
/* 136 */     return gson.getAdapter(fieldType);
/*     */   }
/*     */   
/*     */   private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
/* 140 */     Map<String, BoundField> result = new LinkedHashMap<String, BoundField>();
/* 141 */     if (raw.isInterface()) {
/* 142 */       return result;
/*     */     }
/*     */     
/* 145 */     Type declaredType = type.getType();
/* 146 */     while (raw != Object.class) {
/* 147 */       Field[] fields = raw.getDeclaredFields();
/* 148 */       for (Field field : fields) {
/* 149 */         boolean serialize = excludeField(field, true);
/* 150 */         boolean deserialize = excludeField(field, false);
/* 151 */         if (serialize || deserialize) {
/*     */ 
/*     */           
/* 154 */           field.setAccessible(true);
/* 155 */           Type fieldType = .Gson.Types.resolve(type.getType(), raw, field.getGenericType());
/* 156 */           List<String> fieldNames = getFieldNames(field);
/* 157 */           BoundField previous = null;
/* 158 */           for (int i = 0; i < fieldNames.size(); i++) {
/* 159 */             String name = fieldNames.get(i);
/* 160 */             if (i != 0) serialize = false; 
/* 161 */             BoundField boundField = createBoundField(context, field, name, TypeToken.get(fieldType), serialize, deserialize);
/*     */             
/* 163 */             BoundField replaced = result.put(name, boundField);
/* 164 */             if (previous == null) previous = replaced; 
/*     */           } 
/* 166 */           if (previous != null) {
/* 167 */             throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
/*     */           }
/*     */         } 
/*     */       } 
/* 171 */       type = TypeToken.get(.Gson.Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
/* 172 */       raw = type.getRawType();
/*     */     } 
/* 174 */     return result;
/*     */   }
/*     */   
/*     */   static abstract class BoundField {
/*     */     final String name;
/*     */     final boolean serialized;
/*     */     final boolean deserialized;
/*     */     
/*     */     protected BoundField(String name, boolean serialized, boolean deserialized) {
/* 183 */       this.name = name;
/* 184 */       this.serialized = serialized;
/* 185 */       this.deserialized = deserialized;
/*     */     }
/*     */     
/*     */     abstract boolean writeField(Object param1Object) throws IOException, IllegalAccessException;
/*     */     
/*     */     abstract void write(JsonWriter param1JsonWriter, Object param1Object) throws IOException, IllegalAccessException;
/*     */     
/*     */     abstract void read(JsonReader param1JsonReader, Object param1Object) throws IOException, IllegalAccessException; }
/*     */   
/*     */   public static final class Adapter<T> extends TypeAdapter<T> { private final ObjectConstructor<T> constructor;
/*     */     
/*     */     private Adapter(ObjectConstructor<T> constructor, Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields) {
/* 197 */       this.constructor = constructor;
/* 198 */       this.boundFields = boundFields;
/*     */     }
/*     */     private final Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields;
/*     */     public T read(JsonReader in) throws IOException {
/* 202 */       if (in.peek() == JsonToken.NULL) {
/* 203 */         in.nextNull();
/* 204 */         return null;
/*     */       } 
/*     */       
/* 207 */       T instance = (T)this.constructor.construct();
/*     */       
/*     */       try {
/* 210 */         in.beginObject();
/* 211 */         while (in.hasNext()) {
/* 212 */           String name = in.nextName();
/* 213 */           ReflectiveTypeAdapterFactory.BoundField field = this.boundFields.get(name);
/* 214 */           if (field == null || !field.deserialized) {
/* 215 */             in.skipValue(); continue;
/*     */           } 
/* 217 */           field.read(in, instance);
/*     */         }
/*     */       
/* 220 */       } catch (IllegalStateException e) {
/* 221 */         throw new JsonSyntaxException(e);
/* 222 */       } catch (IllegalAccessException e) {
/* 223 */         throw new AssertionError(e);
/*     */       } 
/* 225 */       in.endObject();
/* 226 */       return instance;
/*     */     }
/*     */     
/*     */     public void write(JsonWriter out, T value) throws IOException {
/* 230 */       if (value == null) {
/* 231 */         out.nullValue();
/*     */         
/*     */         return;
/*     */       } 
/* 235 */       out.beginObject();
/*     */       try {
/* 237 */         for (ReflectiveTypeAdapterFactory.BoundField boundField : this.boundFields.values()) {
/* 238 */           if (boundField.writeField(value)) {
/* 239 */             out.name(boundField.name);
/* 240 */             boundField.write(out, value);
/*     */           } 
/*     */         } 
/* 243 */       } catch (IllegalAccessException e) {
/* 244 */         throw new AssertionError();
/*     */       } 
/* 246 */       out.endObject();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\bind\ReflectiveTypeAdapterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */