/*     */ package com.badlogic.gdx.utils.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
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
/*     */ public final class ClassReflection
/*     */ {
/*     */   public static Class forName(String name) throws ReflectionException {
/*     */     try {
/*  28 */       return Class.forName(name);
/*  29 */     } catch (ClassNotFoundException e) {
/*  30 */       throw new ReflectionException("Class not found: " + name, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getSimpleName(Class c) {
/*  36 */     return c.getSimpleName();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInstance(Class c, Object obj) {
/*  41 */     return c.isInstance(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAssignableFrom(Class c1, Class<?> c2) {
/*  47 */     return c1.isAssignableFrom(c2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isMemberClass(Class c) {
/*  52 */     return c.isMemberClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isStaticClass(Class c) {
/*  57 */     return Modifier.isStatic(c.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isArray(Class c) {
/*  62 */     return c.isArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T newInstance(Class<T> c) throws ReflectionException {
/*     */     try {
/*  68 */       return c.newInstance();
/*  69 */     } catch (InstantiationException e) {
/*  70 */       throw new ReflectionException("Could not instantiate instance of class: " + c.getName(), e);
/*  71 */     } catch (IllegalAccessException e) {
/*  72 */       throw new ReflectionException("Could not instantiate instance of class: " + c.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Constructor[] getConstructors(Class c) {
/*  78 */     Constructor[] constructors = (Constructor[])c.getConstructors();
/*  79 */     Constructor[] result = new Constructor[constructors.length];
/*  80 */     for (int i = 0, j = constructors.length; i < j; i++) {
/*  81 */       result[i] = new Constructor(constructors[i]);
/*     */     }
/*  83 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Constructor getConstructor(Class c, Class... parameterTypes) throws ReflectionException {
/*     */     try {
/*  90 */       return new Constructor(c.getConstructor(parameterTypes));
/*  91 */     } catch (SecurityException e) {
/*  92 */       throw new ReflectionException("Security violation occurred while getting constructor for class: '" + c.getName() + "'.", e);
/*     */     }
/*  94 */     catch (NoSuchMethodException e) {
/*  95 */       throw new ReflectionException("Constructor not found for class: " + c.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Constructor getDeclaredConstructor(Class c, Class... parameterTypes) throws ReflectionException {
/*     */     try {
/* 103 */       return new Constructor(c.getDeclaredConstructor(parameterTypes));
/* 104 */     } catch (SecurityException e) {
/* 105 */       throw new ReflectionException("Security violation while getting constructor for class: " + c.getName(), e);
/* 106 */     } catch (NoSuchMethodException e) {
/* 107 */       throw new ReflectionException("Constructor not found for class: " + c.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method[] getMethods(Class c) {
/* 113 */     Method[] methods = c.getMethods();
/* 114 */     Method[] result = new Method[methods.length];
/* 115 */     for (int i = 0, j = methods.length; i < j; i++) {
/* 116 */       result[i] = new Method(methods[i]);
/*     */     }
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getMethod(Class c, String name, Class... parameterTypes) throws ReflectionException {
/*     */     try {
/* 125 */       return new Method(c.getMethod(name, parameterTypes));
/* 126 */     } catch (SecurityException e) {
/* 127 */       throw new ReflectionException("Security violation while getting method: " + name + ", for class: " + c.getName(), e);
/* 128 */     } catch (NoSuchMethodException e) {
/* 129 */       throw new ReflectionException("Method not found: " + name + ", for class: " + c.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method[] getDeclaredMethods(Class c) {
/* 135 */     Method[] methods = c.getDeclaredMethods();
/* 136 */     Method[] result = new Method[methods.length];
/* 137 */     for (int i = 0, j = methods.length; i < j; i++) {
/* 138 */       result[i] = new Method(methods[i]);
/*     */     }
/* 140 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method getDeclaredMethod(Class c, String name, Class... parameterTypes) throws ReflectionException {
/*     */     try {
/* 146 */       return new Method(c.getDeclaredMethod(name, parameterTypes));
/* 147 */     } catch (SecurityException e) {
/* 148 */       throw new ReflectionException("Security violation while getting method: " + name + ", for class: " + c.getName(), e);
/* 149 */     } catch (NoSuchMethodException e) {
/* 150 */       throw new ReflectionException("Method not found: " + name + ", for class: " + c.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Field[] getFields(Class c) {
/* 156 */     Field[] fields = c.getFields();
/* 157 */     Field[] result = new Field[fields.length];
/* 158 */     for (int i = 0, j = fields.length; i < j; i++) {
/* 159 */       result[i] = new Field(fields[i]);
/*     */     }
/* 161 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Field getField(Class c, String name) throws ReflectionException {
/*     */     try {
/* 167 */       return new Field(c.getField(name));
/* 168 */     } catch (SecurityException e) {
/* 169 */       throw new ReflectionException("Security violation while getting field: " + name + ", for class: " + c.getName(), e);
/* 170 */     } catch (NoSuchFieldException e) {
/* 171 */       throw new ReflectionException("Field not found: " + name + ", for class: " + c.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Field[] getDeclaredFields(Class c) {
/* 177 */     Field[] fields = c.getDeclaredFields();
/* 178 */     Field[] result = new Field[fields.length];
/* 179 */     for (int i = 0, j = fields.length; i < j; i++) {
/* 180 */       result[i] = new Field(fields[i]);
/*     */     }
/* 182 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Field getDeclaredField(Class c, String name) throws ReflectionException {
/*     */     try {
/* 188 */       return new Field(c.getDeclaredField(name));
/* 189 */     } catch (SecurityException e) {
/* 190 */       throw new ReflectionException("Security violation while getting field: " + name + ", for class: " + c.getName(), e);
/* 191 */     } catch (NoSuchFieldException e) {
/* 192 */       throw new ReflectionException("Field not found: " + name + ", for class: " + c.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAnnotationPresent(Class c, Class<? extends Annotation> annotationType) {
/* 198 */     return c.isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Annotation[] getAnnotations(Class c) {
/* 204 */     Annotation[] annotations = c.getAnnotations();
/* 205 */     Annotation[] result = new Annotation[annotations.length];
/* 206 */     for (int i = 0; i < annotations.length; i++) {
/* 207 */       result[i] = new Annotation(annotations[i]);
/*     */     }
/* 209 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Annotation getAnnotation(Class c, Class<? extends Annotation> annotationType) {
/* 215 */     Annotation annotation = (Annotation)c.getAnnotation(annotationType);
/* 216 */     if (annotation != null) return new Annotation(annotation); 
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Annotation[] getDeclaredAnnotations(Class c) {
/* 223 */     Annotation[] annotations = c.getDeclaredAnnotations();
/* 224 */     Annotation[] result = new Annotation[annotations.length];
/* 225 */     for (int i = 0; i < annotations.length; i++) {
/* 226 */       result[i] = new Annotation(annotations[i]);
/*     */     }
/* 228 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Annotation getDeclaredAnnotation(Class c, Class<? extends Annotation> annotationType) {
/* 234 */     Annotation[] annotations = c.getDeclaredAnnotations();
/* 235 */     for (Annotation annotation : annotations) {
/* 236 */       if (annotation.annotationType().equals(annotationType)) return new Annotation(annotation); 
/*     */     } 
/* 238 */     return null;
/*     */   }
/*     */   
/*     */   public static Class[] getInterfaces(Class c) {
/* 242 */     return c.getInterfaces();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\reflect\ClassReflection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */