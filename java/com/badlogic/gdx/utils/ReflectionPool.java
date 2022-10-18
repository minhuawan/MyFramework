/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*    */ import com.badlogic.gdx.utils.reflect.Constructor;
/*    */ import com.badlogic.gdx.utils.reflect.ReflectionException;
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
/*    */ public class ReflectionPool<T>
/*    */   extends Pool<T>
/*    */ {
/*    */   private final Constructor constructor;
/*    */   
/*    */   public ReflectionPool(Class<T> type) {
/* 30 */     this(type, 16, 2147483647);
/*    */   }
/*    */   
/*    */   public ReflectionPool(Class<T> type, int initialCapacity) {
/* 34 */     this(type, initialCapacity, 2147483647);
/*    */   }
/*    */   
/*    */   public ReflectionPool(Class<T> type, int initialCapacity, int max) {
/* 38 */     super(initialCapacity, max);
/* 39 */     this.constructor = findConstructor(type);
/* 40 */     if (this.constructor == null)
/* 41 */       throw new RuntimeException("Class cannot be created (missing no-arg constructor): " + type.getName()); 
/*    */   }
/*    */   
/*    */   private Constructor findConstructor(Class<T> type) {
/*    */     try {
/* 46 */       return ClassReflection.getConstructor(type, (Class[])null);
/* 47 */     } catch (Exception ex1) {
/*    */       try {
/* 49 */         Constructor constructor = ClassReflection.getDeclaredConstructor(type, (Class[])null);
/* 50 */         constructor.setAccessible(true);
/* 51 */         return constructor;
/* 52 */       } catch (ReflectionException ex2) {
/* 53 */         return null;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected T newObject() {
/*    */     try {
/* 60 */       return (T)this.constructor.newInstance((Object[])null);
/* 61 */     } catch (Exception ex) {
/* 62 */       throw new GdxRuntimeException("Unable to create new instance: " + this.constructor.getDeclaringClass().getName(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ReflectionPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */