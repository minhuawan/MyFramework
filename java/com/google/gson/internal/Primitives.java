/*     */ package com.google.gson.internal;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ public final class Primitives
/*     */ {
/*     */   private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
/*     */   private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;
/*     */   
/*     */   private Primitives() {
/*  33 */     throw new UnsupportedOperationException();
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
/*     */   static {
/*  45 */     Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);
/*  46 */     Map<Class<?>, Class<?>> wrapToPrim = new HashMap<Class<?>, Class<?>>(16);
/*     */     
/*  48 */     add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
/*  49 */     add(primToWrap, wrapToPrim, byte.class, Byte.class);
/*  50 */     add(primToWrap, wrapToPrim, char.class, Character.class);
/*  51 */     add(primToWrap, wrapToPrim, double.class, Double.class);
/*  52 */     add(primToWrap, wrapToPrim, float.class, Float.class);
/*  53 */     add(primToWrap, wrapToPrim, int.class, Integer.class);
/*  54 */     add(primToWrap, wrapToPrim, long.class, Long.class);
/*  55 */     add(primToWrap, wrapToPrim, short.class, Short.class);
/*  56 */     add(primToWrap, wrapToPrim, void.class, Void.class);
/*     */     
/*  58 */     PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
/*  59 */     WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
/*  64 */     forward.put(key, value);
/*  65 */     backward.put(value, key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPrimitive(Type type) {
/*  72 */     return PRIMITIVE_TO_WRAPPER_TYPE.containsKey(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWrapperType(Type type) {
/*  82 */     return WRAPPER_TO_PRIMITIVE_TYPE.containsKey($Gson$Preconditions.checkNotNull(type));
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
/*     */   public static <T> Class<T> wrap(Class<T> type) {
/*  98 */     Class<T> wrapped = (Class<T>)PRIMITIVE_TO_WRAPPER_TYPE.get($Gson$Preconditions.checkNotNull(type));
/*     */     
/* 100 */     return (wrapped == null) ? type : wrapped;
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
/*     */   public static <T> Class<T> unwrap(Class<T> type) {
/* 115 */     Class<T> unwrapped = (Class<T>)WRAPPER_TO_PRIMITIVE_TYPE.get($Gson$Preconditions.checkNotNull(type));
/*     */     
/* 117 */     return (unwrapped == null) ? type : unwrapped;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\Primitives.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */