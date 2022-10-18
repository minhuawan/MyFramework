/*    */ package com.badlogic.gdx.utils.reflect;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
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
/*    */ public final class Constructor
/*    */ {
/*    */   private final Constructor constructor;
/*    */   
/*    */   Constructor(Constructor constructor) {
/* 28 */     this.constructor = constructor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class[] getParameterTypes() {
/* 33 */     return this.constructor.getParameterTypes();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class getDeclaringClass() {
/* 38 */     return this.constructor.getDeclaringClass();
/*    */   }
/*    */   
/*    */   public boolean isAccessible() {
/* 42 */     return this.constructor.isAccessible();
/*    */   }
/*    */   
/*    */   public void setAccessible(boolean accessible) {
/* 46 */     this.constructor.setAccessible(accessible);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object newInstance(Object... args) throws ReflectionException {
/*    */     try {
/* 53 */       return this.constructor.newInstance(args);
/* 54 */     } catch (IllegalArgumentException e) {
/* 55 */       throw new ReflectionException("Illegal argument(s) supplied to constructor for class: " + getDeclaringClass().getName(), e);
/*    */     }
/* 57 */     catch (InstantiationException e) {
/* 58 */       throw new ReflectionException("Could not instantiate instance of class: " + getDeclaringClass().getName(), e);
/* 59 */     } catch (IllegalAccessException e) {
/* 60 */       throw new ReflectionException("Could not instantiate instance of class: " + getDeclaringClass().getName(), e);
/* 61 */     } catch (InvocationTargetException e) {
/* 62 */       throw new ReflectionException("Exception occurred in constructor for class: " + getDeclaringClass().getName(), e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\reflect\Constructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */