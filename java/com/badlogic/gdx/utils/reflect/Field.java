/*     */ package com.badlogic.gdx.utils.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Field
/*     */ {
/*     */   private final Field field;
/*     */   
/*     */   Field(Field field) {
/*  31 */     this.field = field;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  36 */     return this.field.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class getType() {
/*  41 */     return this.field.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class getDeclaringClass() {
/*  46 */     return this.field.getDeclaringClass();
/*     */   }
/*     */   
/*     */   public boolean isAccessible() {
/*  50 */     return this.field.isAccessible();
/*     */   }
/*     */   
/*     */   public void setAccessible(boolean accessible) {
/*  54 */     this.field.setAccessible(accessible);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDefaultAccess() {
/*  59 */     return (!isPrivate() && !isProtected() && !isPublic());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/*  64 */     return Modifier.isFinal(this.field.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrivate() {
/*  69 */     return Modifier.isPrivate(this.field.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProtected() {
/*  74 */     return Modifier.isProtected(this.field.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublic() {
/*  79 */     return Modifier.isPublic(this.field.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  84 */     return Modifier.isStatic(this.field.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTransient() {
/*  89 */     return Modifier.isTransient(this.field.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVolatile() {
/*  94 */     return Modifier.isVolatile(this.field.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSynthetic() {
/*  99 */     return this.field.isSynthetic();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getElementType(int index) {
/* 105 */     Type genericType = this.field.getGenericType();
/* 106 */     if (genericType instanceof ParameterizedType) {
/* 107 */       Type[] actualTypes = ((ParameterizedType)genericType).getActualTypeArguments();
/* 108 */       if (actualTypes.length - 1 >= index) {
/* 109 */         Type actualType = actualTypes[index];
/* 110 */         if (actualType instanceof Class)
/* 111 */           return (Class)actualType; 
/* 112 */         if (actualType instanceof ParameterizedType)
/* 113 */           return (Class)((ParameterizedType)actualType).getRawType(); 
/* 114 */         if (actualType instanceof GenericArrayType) {
/* 115 */           Type componentType = ((GenericArrayType)actualType).getGenericComponentType();
/* 116 */           if (componentType instanceof Class) return ArrayReflection.newInstance((Class)componentType, 0).getClass(); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
/* 125 */     return this.field.isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getDeclaredAnnotations() {
/* 131 */     Annotation[] annotations = this.field.getDeclaredAnnotations();
/* 132 */     Annotation[] result = new Annotation[annotations.length];
/* 133 */     for (int i = 0; i < annotations.length; i++) {
/* 134 */       result[i] = new Annotation(annotations[i]);
/*     */     }
/* 136 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation getDeclaredAnnotation(Class<? extends Annotation> annotationType) {
/* 143 */     Annotation[] annotations = this.field.getDeclaredAnnotations();
/* 144 */     if (annotations == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     for (Annotation annotation : annotations) {
/* 148 */       if (annotation.annotationType().equals(annotationType)) {
/* 149 */         return new Annotation(annotation);
/*     */       }
/*     */     } 
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(Object obj) throws ReflectionException {
/*     */     try {
/* 158 */       return this.field.get(obj);
/* 159 */     } catch (IllegalArgumentException e) {
/* 160 */       throw new ReflectionException("Object is not an instance of " + getDeclaringClass(), e);
/* 161 */     } catch (IllegalAccessException e) {
/* 162 */       throw new ReflectionException("Illegal access to field: " + getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Object obj, Object value) throws ReflectionException {
/*     */     try {
/* 169 */       this.field.set(obj, value);
/* 170 */     } catch (IllegalArgumentException e) {
/* 171 */       throw new ReflectionException("Argument not valid for field: " + getName(), e);
/* 172 */     } catch (IllegalAccessException e) {
/* 173 */       throw new ReflectionException("Illegal access to field: " + getName(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\reflect\Field.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */