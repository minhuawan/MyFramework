/*     */ package com.badlogic.gdx.utils.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Method
/*     */ {
/*     */   private final Method method;
/*     */   
/*     */   Method(Method method) {
/*  29 */     this.method = method;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  34 */     return this.method.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class getReturnType() {
/*  39 */     return this.method.getReturnType();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class[] getParameterTypes() {
/*  44 */     return this.method.getParameterTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class getDeclaringClass() {
/*  49 */     return this.method.getDeclaringClass();
/*     */   }
/*     */   
/*     */   public boolean isAccessible() {
/*  53 */     return this.method.isAccessible();
/*     */   }
/*     */   
/*     */   public void setAccessible(boolean accessible) {
/*  57 */     this.method.setAccessible(accessible);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  62 */     return Modifier.isAbstract(this.method.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDefaultAccess() {
/*  67 */     return (!isPrivate() && !isProtected() && !isPublic());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/*  72 */     return Modifier.isFinal(this.method.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrivate() {
/*  77 */     return Modifier.isPrivate(this.method.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProtected() {
/*  82 */     return Modifier.isProtected(this.method.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublic() {
/*  87 */     return Modifier.isPublic(this.method.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNative() {
/*  92 */     return Modifier.isNative(this.method.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  97 */     return Modifier.isStatic(this.method.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVarArgs() {
/* 102 */     return this.method.isVarArgs();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object invoke(Object obj, Object... args) throws ReflectionException {
/*     */     try {
/* 108 */       return this.method.invoke(obj, args);
/* 109 */     } catch (IllegalArgumentException e) {
/* 110 */       throw new ReflectionException("Illegal argument(s) supplied to method: " + getName(), e);
/* 111 */     } catch (IllegalAccessException e) {
/* 112 */       throw new ReflectionException("Illegal access to method: " + getName(), e);
/* 113 */     } catch (InvocationTargetException e) {
/* 114 */       throw new ReflectionException("Exception occurred in method: " + getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
/* 120 */     return this.method.isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getDeclaredAnnotations() {
/* 127 */     Annotation[] annotations = this.method.getDeclaredAnnotations();
/* 128 */     Annotation[] result = new Annotation[annotations.length];
/* 129 */     for (int i = 0; i < annotations.length; i++) {
/* 130 */       result[i] = new Annotation(annotations[i]);
/*     */     }
/* 132 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation getDeclaredAnnotation(Class<? extends Annotation> annotationType) {
/* 139 */     Annotation[] annotations = this.method.getDeclaredAnnotations();
/* 140 */     if (annotations == null) {
/* 141 */       return null;
/*     */     }
/* 143 */     for (Annotation annotation : annotations) {
/* 144 */       if (annotation.annotationType().equals(annotationType)) {
/* 145 */         return new Annotation(annotation);
/*     */       }
/*     */     } 
/* 148 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\reflect\Method.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */