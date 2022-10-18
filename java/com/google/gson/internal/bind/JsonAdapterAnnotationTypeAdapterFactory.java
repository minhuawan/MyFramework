/*    */ package com.google.gson.internal.bind;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import com.google.gson.annotations.JsonAdapter;
/*    */ import com.google.gson.internal.ConstructorConstructor;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JsonAdapterAnnotationTypeAdapterFactory
/*    */   implements TypeAdapterFactory
/*    */ {
/*    */   private final ConstructorConstructor constructorConstructor;
/*    */   
/*    */   public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
/* 37 */     this.constructorConstructor = constructorConstructor;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
/* 42 */     JsonAdapter annotation = (JsonAdapter)targetType.getRawType().getAnnotation(JsonAdapter.class);
/* 43 */     if (annotation == null) {
/* 44 */       return null;
/*    */     }
/* 46 */     return (TypeAdapter)getTypeAdapter(this.constructorConstructor, gson, targetType, annotation);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> fieldType, JsonAdapter annotation) {
/* 52 */     Class<?> value = annotation.value();
/* 53 */     if (TypeAdapter.class.isAssignableFrom(value)) {
/* 54 */       Class<TypeAdapter<?>> typeAdapter = (Class)value;
/* 55 */       return (TypeAdapter)constructorConstructor.get(TypeToken.get(typeAdapter)).construct();
/*    */     } 
/* 57 */     if (TypeAdapterFactory.class.isAssignableFrom(value)) {
/* 58 */       Class<TypeAdapterFactory> typeAdapterFactory = (Class)value;
/* 59 */       return ((TypeAdapterFactory)constructorConstructor.get(TypeToken.get(typeAdapterFactory)).construct()).create(gson, fieldType);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 64 */     throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\bind\JsonAdapterAnnotationTypeAdapterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */