/*     */ package com.google.gson.internal;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class $Gson$Types
/*     */ {
/*  44 */   static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
/*     */   
/*     */   private $Gson$Types() {
/*  47 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
/*  58 */     return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GenericArrayType arrayOf(Type componentType) {
/*  68 */     return new GenericArrayTypeImpl(componentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WildcardType subtypeOf(Type bound) {
/*  78 */     return new WildcardTypeImpl(new Type[] { bound }, EMPTY_TYPE_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WildcardType supertypeOf(Type bound) {
/*  87 */     return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { bound });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type canonicalize(Type type) {
/*  96 */     if (type instanceof Class) {
/*  97 */       Class<?> c = (Class)type;
/*  98 */       return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;
/*     */     } 
/* 100 */     if (type instanceof ParameterizedType) {
/* 101 */       ParameterizedType p = (ParameterizedType)type;
/* 102 */       return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), p.getActualTypeArguments());
/*     */     } 
/*     */     
/* 105 */     if (type instanceof GenericArrayType) {
/* 106 */       GenericArrayType g = (GenericArrayType)type;
/* 107 */       return new GenericArrayTypeImpl(g.getGenericComponentType());
/*     */     } 
/* 109 */     if (type instanceof WildcardType) {
/* 110 */       WildcardType w = (WildcardType)type;
/* 111 */       return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
/*     */     } 
/*     */ 
/*     */     
/* 115 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class<?> getRawType(Type type) {
/* 120 */     if (type instanceof Class)
/*     */     {
/* 122 */       return (Class)type;
/*     */     }
/* 124 */     if (type instanceof ParameterizedType) {
/* 125 */       ParameterizedType parameterizedType = (ParameterizedType)type;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 130 */       Type rawType = parameterizedType.getRawType();
/* 131 */       $Gson$Preconditions.checkArgument(rawType instanceof Class);
/* 132 */       return (Class)rawType;
/*     */     } 
/* 134 */     if (type instanceof GenericArrayType) {
/* 135 */       Type componentType = ((GenericArrayType)type).getGenericComponentType();
/* 136 */       return Array.newInstance(getRawType(componentType), 0).getClass();
/*     */     } 
/* 138 */     if (type instanceof TypeVariable)
/*     */     {
/*     */       
/* 141 */       return Object.class;
/*     */     }
/* 143 */     if (type instanceof WildcardType) {
/* 144 */       return getRawType(((WildcardType)type).getUpperBounds()[0]);
/*     */     }
/*     */     
/* 147 */     String className = (type == null) ? "null" : type.getClass().getName();
/* 148 */     throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean equal(Object a, Object b) {
/* 154 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Type a, Type b) {
/* 161 */     if (a == b)
/*     */     {
/* 163 */       return true;
/*     */     }
/* 165 */     if (a instanceof Class)
/*     */     {
/* 167 */       return a.equals(b);
/*     */     }
/* 169 */     if (a instanceof ParameterizedType) {
/* 170 */       if (!(b instanceof ParameterizedType)) {
/* 171 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 175 */       ParameterizedType pa = (ParameterizedType)a;
/* 176 */       ParameterizedType pb = (ParameterizedType)b;
/* 177 */       return (equal(pa.getOwnerType(), pb.getOwnerType()) && pa.getRawType().equals(pb.getRawType()) && Arrays.equals((Object[])pa.getActualTypeArguments(), (Object[])pb.getActualTypeArguments()));
/*     */     } 
/*     */ 
/*     */     
/* 181 */     if (a instanceof GenericArrayType) {
/* 182 */       if (!(b instanceof GenericArrayType)) {
/* 183 */         return false;
/*     */       }
/*     */       
/* 186 */       GenericArrayType ga = (GenericArrayType)a;
/* 187 */       GenericArrayType gb = (GenericArrayType)b;
/* 188 */       return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
/*     */     } 
/* 190 */     if (a instanceof WildcardType) {
/* 191 */       if (!(b instanceof WildcardType)) {
/* 192 */         return false;
/*     */       }
/*     */       
/* 195 */       WildcardType wa = (WildcardType)a;
/* 196 */       WildcardType wb = (WildcardType)b;
/* 197 */       return (Arrays.equals((Object[])wa.getUpperBounds(), (Object[])wb.getUpperBounds()) && Arrays.equals((Object[])wa.getLowerBounds(), (Object[])wb.getLowerBounds()));
/*     */     } 
/*     */     
/* 200 */     if (a instanceof TypeVariable) {
/* 201 */       if (!(b instanceof TypeVariable)) {
/* 202 */         return false;
/*     */       }
/* 204 */       TypeVariable<?> va = (TypeVariable)a;
/* 205 */       TypeVariable<?> vb = (TypeVariable)b;
/* 206 */       return (va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName().equals(vb.getName()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int hashCodeOrZero(Object o) {
/* 216 */     return (o != null) ? o.hashCode() : 0;
/*     */   }
/*     */   
/*     */   public static String typeToString(Type type) {
/* 220 */     return (type instanceof Class) ? ((Class)type).getName() : type.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
/* 229 */     if (toResolve == rawType) {
/* 230 */       return context;
/*     */     }
/*     */ 
/*     */     
/* 234 */     if (toResolve.isInterface()) {
/* 235 */       Class<?>[] interfaces = rawType.getInterfaces();
/* 236 */       for (int i = 0, length = interfaces.length; i < length; i++) {
/* 237 */         if (interfaces[i] == toResolve)
/* 238 */           return rawType.getGenericInterfaces()[i]; 
/* 239 */         if (toResolve.isAssignableFrom(interfaces[i])) {
/* 240 */           return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 246 */     if (!rawType.isInterface()) {
/* 247 */       while (rawType != Object.class) {
/* 248 */         Class<?> rawSupertype = rawType.getSuperclass();
/* 249 */         if (rawSupertype == toResolve)
/* 250 */           return rawType.getGenericSuperclass(); 
/* 251 */         if (toResolve.isAssignableFrom(rawSupertype)) {
/* 252 */           return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
/*     */         }
/* 254 */         rawType = rawSupertype;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 259 */     return toResolve;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
/* 270 */     $Gson$Preconditions.checkArgument(supertype.isAssignableFrom(contextRawType));
/* 271 */     return resolve(context, contextRawType, getGenericSupertype(context, contextRawType, supertype));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getArrayComponentType(Type array) {
/* 280 */     return (array instanceof GenericArrayType) ? ((GenericArrayType)array).getGenericComponentType() : ((Class)array).getComponentType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getCollectionElementType(Type context, Class<?> contextRawType) {
/* 290 */     Type collectionType = getSupertype(context, contextRawType, Collection.class);
/*     */     
/* 292 */     if (collectionType instanceof WildcardType) {
/* 293 */       collectionType = ((WildcardType)collectionType).getUpperBounds()[0];
/*     */     }
/* 295 */     if (collectionType instanceof ParameterizedType) {
/* 296 */       return ((ParameterizedType)collectionType).getActualTypeArguments()[0];
/*     */     }
/* 298 */     return Object.class;
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
/*     */   public static Type[] getMapKeyAndValueTypes(Type context, Class<?> contextRawType) {
/* 311 */     if (context == Properties.class) {
/* 312 */       return new Type[] { String.class, String.class };
/*     */     }
/*     */     
/* 315 */     Type mapType = getSupertype(context, contextRawType, Map.class);
/*     */     
/* 317 */     if (mapType instanceof ParameterizedType) {
/* 318 */       ParameterizedType mapParameterizedType = (ParameterizedType)mapType;
/* 319 */       return mapParameterizedType.getActualTypeArguments();
/*     */     } 
/* 321 */     return new Type[] { Object.class, Object.class };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
/* 327 */     while (toResolve instanceof TypeVariable) {
/* 328 */       TypeVariable<?> typeVariable = (TypeVariable)toResolve;
/* 329 */       toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
/* 330 */       if (toResolve == typeVariable) {
/* 331 */         return toResolve;
/*     */       }
/*     */     } 
/* 334 */     if (toResolve instanceof Class && ((Class)toResolve).isArray()) {
/* 335 */       Class<?> original = (Class)toResolve;
/* 336 */       Type<?> componentType = original.getComponentType();
/* 337 */       Type newComponentType = resolve(context, contextRawType, componentType);
/* 338 */       return (componentType == newComponentType) ? original : arrayOf(newComponentType);
/*     */     } 
/*     */ 
/*     */     
/* 342 */     if (toResolve instanceof GenericArrayType) {
/* 343 */       GenericArrayType original = (GenericArrayType)toResolve;
/* 344 */       Type componentType = original.getGenericComponentType();
/* 345 */       Type newComponentType = resolve(context, contextRawType, componentType);
/* 346 */       return (componentType == newComponentType) ? original : arrayOf(newComponentType);
/*     */     } 
/*     */ 
/*     */     
/* 350 */     if (toResolve instanceof ParameterizedType) {
/* 351 */       ParameterizedType original = (ParameterizedType)toResolve;
/* 352 */       Type ownerType = original.getOwnerType();
/* 353 */       Type newOwnerType = resolve(context, contextRawType, ownerType);
/* 354 */       boolean changed = (newOwnerType != ownerType);
/*     */       
/* 356 */       Type[] args = original.getActualTypeArguments();
/* 357 */       for (int t = 0, length = args.length; t < length; t++) {
/* 358 */         Type resolvedTypeArgument = resolve(context, contextRawType, args[t]);
/* 359 */         if (resolvedTypeArgument != args[t]) {
/* 360 */           if (!changed) {
/* 361 */             args = (Type[])args.clone();
/* 362 */             changed = true;
/*     */           } 
/* 364 */           args[t] = resolvedTypeArgument;
/*     */         } 
/*     */       } 
/*     */       
/* 368 */       return changed ? newParameterizedTypeWithOwner(newOwnerType, original.getRawType(), args) : original;
/*     */     } 
/*     */ 
/*     */     
/* 372 */     if (toResolve instanceof WildcardType) {
/* 373 */       WildcardType original = (WildcardType)toResolve;
/* 374 */       Type[] originalLowerBound = original.getLowerBounds();
/* 375 */       Type[] originalUpperBound = original.getUpperBounds();
/*     */       
/* 377 */       if (originalLowerBound.length == 1) {
/* 378 */         Type lowerBound = resolve(context, contextRawType, originalLowerBound[0]);
/* 379 */         if (lowerBound != originalLowerBound[0]) {
/* 380 */           return supertypeOf(lowerBound);
/*     */         }
/* 382 */       } else if (originalUpperBound.length == 1) {
/* 383 */         Type upperBound = resolve(context, contextRawType, originalUpperBound[0]);
/* 384 */         if (upperBound != originalUpperBound[0]) {
/* 385 */           return subtypeOf(upperBound);
/*     */         }
/*     */       } 
/* 388 */       return original;
/*     */     } 
/*     */     
/* 391 */     return toResolve;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
/* 397 */     Class<?> declaredByRaw = declaringClassOf(unknown);
/*     */ 
/*     */     
/* 400 */     if (declaredByRaw == null) {
/* 401 */       return unknown;
/*     */     }
/*     */     
/* 404 */     Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
/* 405 */     if (declaredBy instanceof ParameterizedType) {
/* 406 */       int index = indexOf((Object[])declaredByRaw.getTypeParameters(), unknown);
/* 407 */       return ((ParameterizedType)declaredBy).getActualTypeArguments()[index];
/*     */     } 
/*     */     
/* 410 */     return unknown;
/*     */   }
/*     */   
/*     */   private static int indexOf(Object[] array, Object toFind) {
/* 414 */     for (int i = 0; i < array.length; i++) {
/* 415 */       if (toFind.equals(array[i])) {
/* 416 */         return i;
/*     */       }
/*     */     } 
/* 419 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
/* 427 */     GenericDeclaration genericDeclaration = (GenericDeclaration)typeVariable.getGenericDeclaration();
/* 428 */     return (genericDeclaration instanceof Class) ? (Class)genericDeclaration : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkNotPrimitive(Type type) {
/* 434 */     $Gson$Preconditions.checkArgument((!(type instanceof Class) || !((Class)type).isPrimitive()));
/*     */   }
/*     */   
/*     */   private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
/*     */     private final Type ownerType;
/*     */     private final Type rawType;
/*     */     private final Type[] typeArguments;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
/* 444 */       if (rawType instanceof Class) {
/* 445 */         Class<?> rawTypeAsClass = (Class)rawType;
/* 446 */         boolean isStaticOrTopLevelClass = (Modifier.isStatic(rawTypeAsClass.getModifiers()) || rawTypeAsClass.getEnclosingClass() == null);
/*     */         
/* 448 */         $Gson$Preconditions.checkArgument((ownerType != null || isStaticOrTopLevelClass));
/*     */       } 
/*     */       
/* 451 */       this.ownerType = (ownerType == null) ? null : $Gson$Types.canonicalize(ownerType);
/* 452 */       this.rawType = $Gson$Types.canonicalize(rawType);
/* 453 */       this.typeArguments = (Type[])typeArguments.clone();
/* 454 */       for (int t = 0; t < this.typeArguments.length; t++) {
/* 455 */         $Gson$Preconditions.checkNotNull(this.typeArguments[t]);
/* 456 */         $Gson$Types.checkNotPrimitive(this.typeArguments[t]);
/* 457 */         this.typeArguments[t] = $Gson$Types.canonicalize(this.typeArguments[t]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Type[] getActualTypeArguments() {
/* 462 */       return (Type[])this.typeArguments.clone();
/*     */     }
/*     */     
/*     */     public Type getRawType() {
/* 466 */       return this.rawType;
/*     */     }
/*     */     
/*     */     public Type getOwnerType() {
/* 470 */       return this.ownerType;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 474 */       return (other instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType)other));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 479 */       return Arrays.hashCode((Object[])this.typeArguments) ^ this.rawType.hashCode() ^ $Gson$Types.hashCodeOrZero(this.ownerType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 485 */       StringBuilder stringBuilder = new StringBuilder(30 * (this.typeArguments.length + 1));
/* 486 */       stringBuilder.append($Gson$Types.typeToString(this.rawType));
/*     */       
/* 488 */       if (this.typeArguments.length == 0) {
/* 489 */         return stringBuilder.toString();
/*     */       }
/*     */       
/* 492 */       stringBuilder.append("<").append($Gson$Types.typeToString(this.typeArguments[0]));
/* 493 */       for (int i = 1; i < this.typeArguments.length; i++) {
/* 494 */         stringBuilder.append(", ").append($Gson$Types.typeToString(this.typeArguments[i]));
/*     */       }
/* 496 */       return stringBuilder.append(">").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class GenericArrayTypeImpl
/*     */     implements GenericArrayType, Serializable {
/*     */     private final Type componentType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public GenericArrayTypeImpl(Type componentType) {
/* 506 */       this.componentType = $Gson$Types.canonicalize(componentType);
/*     */     }
/*     */     
/*     */     public Type getGenericComponentType() {
/* 510 */       return this.componentType;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 514 */       return (o instanceof GenericArrayType && $Gson$Types.equals(this, (GenericArrayType)o));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 519 */       return this.componentType.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 523 */       return $Gson$Types.typeToString(this.componentType) + "[]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WildcardTypeImpl
/*     */     implements WildcardType, Serializable
/*     */   {
/*     */     private final Type upperBound;
/*     */     
/*     */     private final Type lowerBound;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
/* 539 */       $Gson$Preconditions.checkArgument((lowerBounds.length <= 1));
/* 540 */       $Gson$Preconditions.checkArgument((upperBounds.length == 1));
/*     */       
/* 542 */       if (lowerBounds.length == 1) {
/* 543 */         $Gson$Preconditions.checkNotNull(lowerBounds[0]);
/* 544 */         $Gson$Types.checkNotPrimitive(lowerBounds[0]);
/* 545 */         $Gson$Preconditions.checkArgument((upperBounds[0] == Object.class));
/* 546 */         this.lowerBound = $Gson$Types.canonicalize(lowerBounds[0]);
/* 547 */         this.upperBound = Object.class;
/*     */       } else {
/*     */         
/* 550 */         $Gson$Preconditions.checkNotNull(upperBounds[0]);
/* 551 */         $Gson$Types.checkNotPrimitive(upperBounds[0]);
/* 552 */         this.lowerBound = null;
/* 553 */         this.upperBound = $Gson$Types.canonicalize(upperBounds[0]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Type[] getUpperBounds() {
/* 558 */       return new Type[] { this.upperBound };
/*     */     }
/*     */     
/*     */     public Type[] getLowerBounds() {
/* 562 */       (new Type[1])[0] = this.lowerBound; return (this.lowerBound != null) ? new Type[1] : $Gson$Types.EMPTY_TYPE_ARRAY;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 566 */       return (other instanceof WildcardType && $Gson$Types.equals(this, (WildcardType)other));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 572 */       return ((this.lowerBound != null) ? (31 + this.lowerBound.hashCode()) : 1) ^ 31 + this.upperBound.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 577 */       if (this.lowerBound != null)
/* 578 */         return "? super " + $Gson$Types.typeToString(this.lowerBound); 
/* 579 */       if (this.upperBound == Object.class) {
/* 580 */         return "?";
/*     */       }
/* 582 */       return "? extends " + $Gson$Types.typeToString(this.upperBound);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\$Gson$Types.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */