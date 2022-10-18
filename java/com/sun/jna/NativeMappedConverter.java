/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
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
/*    */ 
/*    */ public class NativeMappedConverter
/*    */   implements TypeConverter
/*    */ {
/* 33 */   private static final Map<Class<?>, Reference<NativeMappedConverter>> converters = new WeakHashMap<Class<?>, Reference<NativeMappedConverter>>();
/*    */   
/*    */   private final Class<?> type;
/*    */   private final Class<?> nativeType;
/*    */   private final NativeMapped instance;
/*    */   
/*    */   public static NativeMappedConverter getInstance(Class<?> cls) {
/* 40 */     synchronized (converters) {
/* 41 */       Reference<NativeMappedConverter> r = converters.get(cls);
/* 42 */       NativeMappedConverter nmc = (r != null) ? r.get() : null;
/* 43 */       if (nmc == null) {
/* 44 */         nmc = new NativeMappedConverter(cls);
/* 45 */         converters.put(cls, new SoftReference<NativeMappedConverter>(nmc));
/*    */       } 
/* 47 */       return nmc;
/*    */     } 
/*    */   }
/*    */   
/*    */   public NativeMappedConverter(Class<?> type) {
/* 52 */     if (!NativeMapped.class.isAssignableFrom(type))
/* 53 */       throw new IllegalArgumentException("Type must derive from " + NativeMapped.class); 
/* 54 */     this.type = type;
/* 55 */     this.instance = defaultValue();
/* 56 */     this.nativeType = this.instance.nativeType();
/*    */   }
/*    */   
/*    */   public NativeMapped defaultValue() {
/*    */     try {
/* 61 */       return (NativeMapped)this.type.newInstance();
/* 62 */     } catch (InstantiationException e) {
/* 63 */       String msg = "Can't create an instance of " + this.type + ", requires a no-arg constructor: " + e;
/*    */       
/* 65 */       throw new IllegalArgumentException(msg);
/* 66 */     } catch (IllegalAccessException e) {
/* 67 */       String msg = "Not allowed to create an instance of " + this.type + ", requires a public, no-arg constructor: " + e;
/*    */       
/* 69 */       throw new IllegalArgumentException(msg);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Object fromNative(Object nativeValue, FromNativeContext context) {
/* 74 */     return this.instance.fromNative(nativeValue, context);
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> nativeType() {
/* 79 */     return this.nativeType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object toNative(Object value, ToNativeContext context) {
/* 84 */     if (value == null) {
/* 85 */       if (Pointer.class.isAssignableFrom(this.nativeType)) {
/* 86 */         return null;
/*    */       }
/* 88 */       value = defaultValue();
/*    */     } 
/* 90 */     return ((NativeMapped)value).toNative();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\NativeMappedConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */