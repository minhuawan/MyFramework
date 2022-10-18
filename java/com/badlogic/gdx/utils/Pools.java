/*    */ package com.badlogic.gdx.utils;
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
/*    */ public class Pools
/*    */ {
/* 22 */   private static final ObjectMap<Class, Pool> typePools = new ObjectMap<Class<?>, Pool>();
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Pool<T> get(Class<T> type, int max) {
/* 27 */     Pool<T> pool = typePools.get(type);
/* 28 */     if (pool == null) {
/* 29 */       pool = new ReflectionPool<T>(type, 4, max);
/* 30 */       typePools.put(type, pool);
/*    */     } 
/* 32 */     return pool;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Pool<T> get(Class<T> type) {
/* 38 */     return get(type, 100);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> void set(Class<T> type, Pool<T> pool) {
/* 43 */     typePools.put(type, pool);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> T obtain(Class<T> type) {
/* 48 */     return get(type).obtain();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void free(Object object) {
/* 53 */     if (object == null) throw new IllegalArgumentException("Object cannot be null."); 
/* 54 */     Pool<Object> pool = typePools.get(object.getClass());
/* 55 */     if (pool == null)
/* 56 */       return;  pool.free(object);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void freeAll(Array objects) {
/* 62 */     freeAll(objects, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void freeAll(Array objects, boolean samePool) {
/* 68 */     if (objects == null) throw new IllegalArgumentException("Objects cannot be null."); 
/* 69 */     Pool<Object> pool = null;
/* 70 */     for (int i = 0, n = objects.size; i < n; i++) {
/* 71 */       Object object = objects.get(i);
/* 72 */       if (object == null)
/* 73 */         continue;  if (pool == null) {
/* 74 */         pool = typePools.get(object.getClass());
/* 75 */         if (pool == null)
/*    */           continue; 
/* 77 */       }  pool.free(object);
/* 78 */       if (!samePool) pool = null; 
/*    */       continue;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Pools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */