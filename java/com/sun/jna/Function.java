/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
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
/*     */ public class Function
/*     */   extends Pointer
/*     */ {
/*     */   public static final int MAX_NARGS = 256;
/*     */   public static final int C_CONVENTION = 0;
/*     */   public static final int ALT_CONVENTION = 63;
/*     */   private static final int MASK_CC = 63;
/*     */   public static final int THROW_LAST_ERROR = 64;
/*     */   public static final int USE_VARARGS = 384;
/*  76 */   static final Integer INTEGER_TRUE = Integer.valueOf(-1);
/*  77 */   static final Integer INTEGER_FALSE = Integer.valueOf(0);
/*     */ 
/*     */   
/*     */   private NativeLibrary library;
/*     */ 
/*     */   
/*     */   private final String functionName;
/*     */   
/*     */   final String encoding;
/*     */   
/*     */   final int callFlags;
/*     */   
/*     */   final Map<String, ?> options;
/*     */   
/*     */   static final String OPTION_INVOKING_METHOD = "invoking-method";
/*     */ 
/*     */   
/*     */   public static Function getFunction(String libraryName, String functionName) {
/*  95 */     return NativeLibrary.getInstance(libraryName).getFunction(functionName);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Function getFunction(String libraryName, String functionName, int callFlags) {
/* 116 */     return NativeLibrary.getInstance(libraryName).getFunction(functionName, callFlags, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Function getFunction(String libraryName, String functionName, int callFlags, String encoding) {
/* 140 */     return NativeLibrary.getInstance(libraryName).getFunction(functionName, callFlags, encoding);
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
/*     */   public static Function getFunction(Pointer p) {
/* 155 */     return getFunction(p, 0, (String)null);
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
/*     */ 
/*     */   
/*     */   public static Function getFunction(Pointer p, int callFlags) {
/* 173 */     return getFunction(p, callFlags, (String)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Function getFunction(Pointer p, int callFlags, String encoding) {
/* 194 */     return new Function(p, callFlags, encoding);
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
/* 209 */   private static final VarArgsChecker IS_VARARGS = VarArgsChecker.create();
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
/*     */   Function(NativeLibrary library, String functionName, int callFlags, String encoding) {
/* 231 */     checkCallingConvention(callFlags & 0x3F);
/* 232 */     if (functionName == null) {
/* 233 */       throw new NullPointerException("Function name must not be null");
/*     */     }
/* 235 */     this.library = library;
/* 236 */     this.functionName = functionName;
/* 237 */     this.callFlags = callFlags;
/* 238 */     this.options = library.options;
/* 239 */     this.encoding = (encoding != null) ? encoding : Native.getDefaultStringEncoding();
/*     */     try {
/* 241 */       this.peer = library.getSymbolAddress(functionName);
/* 242 */     } catch (UnsatisfiedLinkError e) {
/* 243 */       throw new UnsatisfiedLinkError("Error looking up function '" + functionName + "': " + e
/*     */           
/* 245 */           .getMessage());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   Function(Pointer functionAddress, int callFlags, String encoding) {
/* 265 */     checkCallingConvention(callFlags & 0x3F);
/* 266 */     if (functionAddress == null || functionAddress.peer == 0L)
/*     */     {
/* 268 */       throw new NullPointerException("Function address may not be null");
/*     */     }
/* 270 */     this.functionName = functionAddress.toString();
/* 271 */     this.callFlags = callFlags;
/* 272 */     this.peer = functionAddress.peer;
/* 273 */     this.options = Collections.EMPTY_MAP;
/* 274 */     this
/* 275 */       .encoding = (encoding != null) ? encoding : Native.getDefaultStringEncoding();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkCallingConvention(int convention) throws IllegalArgumentException {
/* 281 */     if ((convention & 0x3F) != convention) {
/* 282 */       throw new IllegalArgumentException("Unrecognized calling convention: " + convention);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 288 */     return this.functionName;
/*     */   }
/*     */   
/*     */   public int getCallingConvention() {
/* 292 */     return this.callFlags & 0x3F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Class<?> returnType, Object[] inArgs) {
/* 299 */     return invoke(returnType, inArgs, this.options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Class<?> returnType, Object[] inArgs, Map<String, ?> options) {
/* 306 */     Method invokingMethod = (Method)options.get("invoking-method");
/* 307 */     Class<?>[] paramTypes = (invokingMethod != null) ? invokingMethod.getParameterTypes() : null;
/* 308 */     return invoke(invokingMethod, paramTypes, returnType, inArgs, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object invoke(Method invokingMethod, Class<?>[] paramTypes, Class<?> returnType, Object[] inArgs, Map<String, ?> options) {
/* 319 */     Object[] args = new Object[0];
/* 320 */     if (inArgs != null) {
/* 321 */       if (inArgs.length > 256) {
/* 322 */         throw new UnsupportedOperationException("Maximum argument count is 256");
/*     */       }
/* 324 */       args = new Object[inArgs.length];
/* 325 */       System.arraycopy(inArgs, 0, args, 0, args.length);
/*     */     } 
/*     */     
/* 328 */     TypeMapper mapper = (TypeMapper)options.get("type-mapper");
/* 329 */     boolean allowObjects = Boolean.TRUE.equals(options.get("allow-objects"));
/* 330 */     boolean isVarArgs = (args.length > 0 && invokingMethod != null) ? isVarArgs(invokingMethod) : false;
/* 331 */     int fixedArgs = (args.length > 0 && invokingMethod != null) ? fixedArgs(invokingMethod) : 0;
/* 332 */     for (int i = 0; i < args.length; i++) {
/*     */ 
/*     */       
/* 335 */       Class<?> paramType = (invokingMethod != null) ? ((isVarArgs && i >= paramTypes.length - 1) ? paramTypes[paramTypes.length - 1].getComponentType() : paramTypes[i]) : null;
/*     */ 
/*     */       
/* 338 */       args[i] = convertArgument(args, i, invokingMethod, mapper, allowObjects, paramType);
/*     */     } 
/*     */     
/* 341 */     Class<?> nativeReturnType = returnType;
/* 342 */     FromNativeConverter resultConverter = null;
/* 343 */     if (NativeMapped.class.isAssignableFrom(returnType)) {
/* 344 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(returnType);
/* 345 */       resultConverter = tc;
/* 346 */       nativeReturnType = tc.nativeType();
/* 347 */     } else if (mapper != null) {
/* 348 */       resultConverter = mapper.getFromNativeConverter(returnType);
/* 349 */       if (resultConverter != null) {
/* 350 */         nativeReturnType = resultConverter.nativeType();
/*     */       }
/*     */     } 
/*     */     
/* 354 */     Object result = invoke(args, nativeReturnType, allowObjects, fixedArgs);
/*     */     
/* 356 */     if (resultConverter != null) {
/*     */       FromNativeContext context;
/* 358 */       if (invokingMethod != null) {
/* 359 */         context = new MethodResultContext(returnType, this, inArgs, invokingMethod);
/*     */       } else {
/* 361 */         context = new FunctionResultContext(returnType, this, inArgs);
/*     */       } 
/* 363 */       result = resultConverter.fromNative(result, context);
/*     */     } 
/*     */ 
/*     */     
/* 367 */     if (inArgs != null) {
/* 368 */       for (int j = 0; j < inArgs.length; j++) {
/* 369 */         Object inArg = inArgs[j];
/* 370 */         if (inArg != null)
/*     */         {
/* 372 */           if (inArg instanceof Structure) {
/* 373 */             if (!(inArg instanceof Structure.ByValue)) {
/* 374 */               ((Structure)inArg).autoRead();
/*     */             }
/* 376 */           } else if (args[j] instanceof PostCallRead) {
/* 377 */             ((PostCallRead)args[j]).read();
/* 378 */             if (args[j] instanceof PointerArray) {
/* 379 */               PointerArray array = (PointerArray)args[j];
/* 380 */               if (Structure.ByReference[].class.isAssignableFrom(inArg.getClass())) {
/* 381 */                 Class<?> type = inArg.getClass().getComponentType();
/* 382 */                 Structure[] ss = (Structure[])inArg;
/* 383 */                 for (int si = 0; si < ss.length; si++) {
/* 384 */                   Pointer p = array.getPointer((Pointer.SIZE * si));
/* 385 */                   ss[si] = Structure.updateStructureByReference(type, ss[si], p);
/*     */                 } 
/*     */               } 
/*     */             } 
/* 389 */           } else if (Structure[].class.isAssignableFrom(inArg.getClass())) {
/* 390 */             Structure.autoRead((Structure[])inArg);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/* 395 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   Object invoke(Object[] args, Class<?> returnType, boolean allowObjects) {
/* 400 */     return invoke(args, returnType, allowObjects, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   Object invoke(Object[] args, Class<?> returnType, boolean allowObjects, int fixedArgs) {
/* 405 */     Object result = null;
/* 406 */     int callFlags = this.callFlags | (fixedArgs & 0x3) << 7;
/* 407 */     if (returnType == null || returnType == void.class || returnType == Void.class) {
/* 408 */       Native.invokeVoid(this, this.peer, callFlags, args);
/* 409 */       result = null;
/* 410 */     } else if (returnType == boolean.class || returnType == Boolean.class) {
/* 411 */       result = valueOf((Native.invokeInt(this, this.peer, callFlags, args) != 0));
/* 412 */     } else if (returnType == byte.class || returnType == Byte.class) {
/* 413 */       result = Byte.valueOf((byte)Native.invokeInt(this, this.peer, callFlags, args));
/* 414 */     } else if (returnType == short.class || returnType == Short.class) {
/* 415 */       result = Short.valueOf((short)Native.invokeInt(this, this.peer, callFlags, args));
/* 416 */     } else if (returnType == char.class || returnType == Character.class) {
/* 417 */       result = Character.valueOf((char)Native.invokeInt(this, this.peer, callFlags, args));
/* 418 */     } else if (returnType == int.class || returnType == Integer.class) {
/* 419 */       result = Integer.valueOf(Native.invokeInt(this, this.peer, callFlags, args));
/* 420 */     } else if (returnType == long.class || returnType == Long.class) {
/* 421 */       result = Long.valueOf(Native.invokeLong(this, this.peer, callFlags, args));
/* 422 */     } else if (returnType == float.class || returnType == Float.class) {
/* 423 */       result = Float.valueOf(Native.invokeFloat(this, this.peer, callFlags, args));
/* 424 */     } else if (returnType == double.class || returnType == Double.class) {
/* 425 */       result = Double.valueOf(Native.invokeDouble(this, this.peer, callFlags, args));
/* 426 */     } else if (returnType == String.class) {
/* 427 */       result = invokeString(callFlags, args, false);
/* 428 */     } else if (returnType == WString.class) {
/* 429 */       String s = invokeString(callFlags, args, true);
/* 430 */       if (s != null)
/* 431 */         result = new WString(s); 
/*     */     } else {
/* 433 */       if (Pointer.class.isAssignableFrom(returnType))
/* 434 */         return invokePointer(callFlags, args); 
/* 435 */       if (Structure.class.isAssignableFrom(returnType)) {
/* 436 */         if (Structure.ByValue.class.isAssignableFrom(returnType)) {
/*     */           
/* 438 */           Structure s = Native.invokeStructure(this, this.peer, callFlags, args, 
/* 439 */               Structure.newInstance(returnType));
/* 440 */           s.autoRead();
/* 441 */           result = s;
/*     */         } else {
/* 443 */           result = invokePointer(callFlags, args);
/* 444 */           if (result != null) {
/* 445 */             Structure s = Structure.newInstance(returnType, (Pointer)result);
/* 446 */             s.conditionalAutoRead();
/* 447 */             result = s;
/*     */           } 
/*     */         } 
/* 450 */       } else if (Callback.class.isAssignableFrom(returnType)) {
/* 451 */         result = invokePointer(callFlags, args);
/* 452 */         if (result != null) {
/* 453 */           result = CallbackReference.getCallback(returnType, (Pointer)result);
/*     */         }
/* 455 */       } else if (returnType == String[].class) {
/* 456 */         Pointer p = invokePointer(callFlags, args);
/* 457 */         if (p != null) {
/* 458 */           result = p.getStringArray(0L, this.encoding);
/*     */         }
/* 460 */       } else if (returnType == WString[].class) {
/* 461 */         Pointer p = invokePointer(callFlags, args);
/* 462 */         if (p != null) {
/* 463 */           String[] arr = p.getWideStringArray(0L);
/* 464 */           WString[] warr = new WString[arr.length];
/* 465 */           for (int i = 0; i < arr.length; i++) {
/* 466 */             warr[i] = new WString(arr[i]);
/*     */           }
/* 468 */           result = warr;
/*     */         } 
/* 470 */       } else if (returnType == Pointer[].class) {
/* 471 */         Pointer p = invokePointer(callFlags, args);
/* 472 */         if (p != null) {
/* 473 */           result = p.getPointerArray(0L);
/*     */         }
/* 475 */       } else if (allowObjects) {
/* 476 */         result = Native.invokeObject(this, this.peer, callFlags, args);
/* 477 */         if (result != null && 
/* 478 */           !returnType.isAssignableFrom(result.getClass())) {
/* 479 */           throw new ClassCastException("Return type " + returnType + " does not match result " + result
/*     */               
/* 481 */               .getClass());
/*     */         }
/*     */       } else {
/* 484 */         throw new IllegalArgumentException("Unsupported return type " + returnType + " in function " + getName());
/*     */       } 
/* 486 */     }  return result;
/*     */   }
/*     */   
/*     */   private Pointer invokePointer(int callFlags, Object[] args) {
/* 490 */     long ptr = Native.invokePointer(this, this.peer, callFlags, args);
/* 491 */     return (ptr == 0L) ? null : new Pointer(ptr);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object convertArgument(Object[] args, int index, Method invokingMethod, TypeMapper mapper, boolean allowObjects, Class<?> expectedType) {
/* 497 */     Object arg = args[index];
/* 498 */     if (arg != null) {
/* 499 */       Class<?> type = arg.getClass();
/* 500 */       ToNativeConverter converter = null;
/* 501 */       if (NativeMapped.class.isAssignableFrom(type)) {
/* 502 */         converter = NativeMappedConverter.getInstance(type);
/* 503 */       } else if (mapper != null) {
/* 504 */         converter = mapper.getToNativeConverter(type);
/*     */       } 
/* 506 */       if (converter != null) {
/*     */         ToNativeContext context;
/* 508 */         if (invokingMethod != null) {
/* 509 */           context = new MethodParameterContext(this, args, index, invokingMethod);
/*     */         } else {
/*     */           
/* 512 */           context = new FunctionParameterContext(this, args, index);
/*     */         } 
/* 514 */         arg = converter.toNative(arg, context);
/*     */       } 
/*     */     } 
/* 517 */     if (arg == null || isPrimitiveArray(arg.getClass())) {
/* 518 */       return arg;
/*     */     }
/*     */     
/* 521 */     Class<?> argClass = arg.getClass();
/*     */     
/* 523 */     if (arg instanceof Structure) {
/* 524 */       Structure struct = (Structure)arg;
/* 525 */       struct.autoWrite();
/* 526 */       if (struct instanceof Structure.ByValue) {
/*     */         
/* 528 */         Class<?> ptype = struct.getClass();
/* 529 */         if (invokingMethod != null) {
/* 530 */           Class<?>[] ptypes = invokingMethod.getParameterTypes();
/* 531 */           if (IS_VARARGS.isVarArgs(invokingMethod)) {
/* 532 */             if (index < ptypes.length - 1) {
/* 533 */               ptype = ptypes[index];
/*     */             } else {
/* 535 */               Class<?> etype = ptypes[ptypes.length - 1].getComponentType();
/* 536 */               if (etype != Object.class) {
/* 537 */                 ptype = etype;
/*     */               }
/*     */             } 
/*     */           } else {
/* 541 */             ptype = ptypes[index];
/*     */           } 
/*     */         } 
/* 544 */         if (Structure.ByValue.class.isAssignableFrom(ptype)) {
/* 545 */           return struct;
/*     */         }
/*     */       } 
/* 548 */       return struct.getPointer();
/* 549 */     }  if (arg instanceof Callback)
/*     */     {
/* 551 */       return CallbackReference.getFunctionPointer((Callback)arg); } 
/* 552 */     if (arg instanceof String)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 557 */       return (new NativeString((String)arg, false)).getPointer(); } 
/* 558 */     if (arg instanceof WString)
/*     */     {
/* 560 */       return (new NativeString(arg.toString(), true)).getPointer(); } 
/* 561 */     if (arg instanceof Boolean)
/*     */     {
/*     */       
/* 564 */       return Boolean.TRUE.equals(arg) ? INTEGER_TRUE : INTEGER_FALSE; } 
/* 565 */     if (String[].class == argClass)
/* 566 */       return new StringArray((String[])arg, this.encoding); 
/* 567 */     if (WString[].class == argClass)
/* 568 */       return new StringArray((WString[])arg); 
/* 569 */     if (Pointer[].class == argClass)
/* 570 */       return new PointerArray((Pointer[])arg); 
/* 571 */     if (NativeMapped[].class.isAssignableFrom(argClass))
/* 572 */       return new NativeMappedArray((NativeMapped[])arg); 
/* 573 */     if (Structure[].class.isAssignableFrom(argClass)) {
/*     */ 
/*     */       
/* 576 */       Structure[] ss = (Structure[])arg;
/* 577 */       Class<?> type = argClass.getComponentType();
/* 578 */       boolean byRef = Structure.ByReference.class.isAssignableFrom(type);
/* 579 */       if (expectedType != null && 
/* 580 */         !Structure.ByReference[].class.isAssignableFrom(expectedType)) {
/* 581 */         if (byRef) {
/* 582 */           throw new IllegalArgumentException("Function " + getName() + " declared Structure[] at parameter " + index + " but array of " + type + " was passed");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 587 */         for (int i = 0; i < ss.length; i++) {
/* 588 */           if (ss[i] instanceof Structure.ByReference) {
/* 589 */             throw new IllegalArgumentException("Function " + getName() + " declared Structure[] at parameter " + index + " but element " + i + " is of Structure.ByReference type");
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 597 */       if (byRef) {
/* 598 */         Structure.autoWrite(ss);
/* 599 */         Pointer[] pointers = new Pointer[ss.length + 1];
/* 600 */         for (int i = 0; i < ss.length; i++) {
/* 601 */           pointers[i] = (ss[i] != null) ? ss[i].getPointer() : null;
/*     */         }
/* 603 */         return new PointerArray(pointers);
/* 604 */       }  if (ss.length == 0)
/* 605 */         throw new IllegalArgumentException("Structure array must have non-zero length"); 
/* 606 */       if (ss[0] == null) {
/* 607 */         Structure.newInstance(type).toArray(ss);
/* 608 */         return ss[0].getPointer();
/*     */       } 
/* 610 */       Structure.autoWrite(ss);
/* 611 */       return ss[0].getPointer();
/*     */     } 
/* 613 */     if (argClass.isArray())
/* 614 */       throw new IllegalArgumentException("Unsupported array argument type: " + argClass
/* 615 */           .getComponentType()); 
/* 616 */     if (allowObjects)
/* 617 */       return arg; 
/* 618 */     if (!Native.isSupportedNativeType(arg.getClass())) {
/* 619 */       throw new IllegalArgumentException("Unsupported argument type " + arg
/* 620 */           .getClass().getName() + " at parameter " + index + " of function " + 
/*     */           
/* 622 */           getName());
/*     */     }
/* 624 */     return arg;
/*     */   }
/*     */   
/*     */   private boolean isPrimitiveArray(Class<?> argClass) {
/* 628 */     return (argClass.isArray() && argClass
/* 629 */       .getComponentType().isPrimitive());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invoke(Object[] args) {
/* 639 */     invoke(Void.class, args);
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
/*     */   private String invokeString(int callFlags, Object[] args, boolean wide) {
/* 654 */     Pointer ptr = invokePointer(callFlags, args);
/* 655 */     String s = null;
/* 656 */     if (ptr != null) {
/* 657 */       if (wide) {
/* 658 */         s = ptr.getWideString(0L);
/*     */       } else {
/*     */         
/* 661 */         s = ptr.getString(0L, this.encoding);
/*     */       } 
/*     */     }
/* 664 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 670 */     if (this.library != null) {
/* 671 */       return "native function " + this.functionName + "(" + this.library.getName() + ")@0x" + 
/* 672 */         Long.toHexString(this.peer);
/*     */     }
/* 674 */     return "native function@0x" + Long.toHexString(this.peer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeObject(Object[] args) {
/* 681 */     return invoke(Object.class, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointer invokePointer(Object[] args) {
/* 688 */     return (Pointer)invoke(Pointer.class, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String invokeString(Object[] args, boolean wide) {
/* 699 */     Object o = invoke(wide ? WString.class : String.class, args);
/* 700 */     return (o != null) ? o.toString() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int invokeInt(Object[] args) {
/* 707 */     return ((Integer)invoke(Integer.class, args)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long invokeLong(Object[] args) {
/* 713 */     return ((Long)invoke(Long.class, args)).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float invokeFloat(Object[] args) {
/* 719 */     return ((Float)invoke(Float.class, args)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double invokeDouble(Object[] args) {
/* 725 */     return ((Double)invoke(Double.class, args)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invokeVoid(Object[] args) {
/* 731 */     invoke(Void.class, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 739 */     if (o == this) return true; 
/* 740 */     if (o == null) return false; 
/* 741 */     if (o.getClass() == getClass()) {
/* 742 */       Function other = (Function)o;
/* 743 */       return (other.callFlags == this.callFlags && other.options
/* 744 */         .equals(this.options) && other.peer == this.peer);
/*     */     } 
/*     */     
/* 747 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 755 */     return this.callFlags + this.options.hashCode() + super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object[] concatenateVarArgs(Object[] inArgs) {
/* 765 */     if (inArgs != null && inArgs.length > 0) {
/* 766 */       Object lastArg = inArgs[inArgs.length - 1];
/* 767 */       Class<?> argType = (lastArg != null) ? lastArg.getClass() : null;
/* 768 */       if (argType != null && argType.isArray()) {
/* 769 */         Object[] varArgs = (Object[])lastArg;
/*     */         
/* 771 */         for (int i = 0; i < varArgs.length; i++) {
/* 772 */           if (varArgs[i] instanceof Float) {
/* 773 */             varArgs[i] = Double.valueOf(((Float)varArgs[i]).floatValue());
/*     */           }
/*     */         } 
/* 776 */         Object[] fullArgs = new Object[inArgs.length + varArgs.length];
/* 777 */         System.arraycopy(inArgs, 0, fullArgs, 0, inArgs.length - 1);
/* 778 */         System.arraycopy(varArgs, 0, fullArgs, inArgs.length - 1, varArgs.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 784 */         fullArgs[fullArgs.length - 1] = null;
/* 785 */         inArgs = fullArgs;
/*     */       } 
/*     */     } 
/* 788 */     return inArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isVarArgs(Method m) {
/* 793 */     return IS_VARARGS.isVarArgs(m);
/*     */   }
/*     */ 
/*     */   
/*     */   static int fixedArgs(Method m) {
/* 798 */     return IS_VARARGS.fixedArgs(m);
/*     */   }
/*     */   public static interface PostCallRead {
/*     */     void read(); }
/*     */   
/*     */   private static class NativeMappedArray extends Memory implements PostCallRead { public NativeMappedArray(NativeMapped[] arg) {
/* 804 */       super(Native.getNativeSize(arg.getClass(), arg));
/* 805 */       this.original = arg;
/* 806 */       setValue(0L, this.original, this.original.getClass());
/*     */     }
/*     */     private final NativeMapped[] original;
/*     */     public void read() {
/* 810 */       getValue(0L, this.original.getClass(), this.original);
/*     */     } }
/*     */   
/*     */   private static class PointerArray extends Memory implements PostCallRead {
/*     */     private final Pointer[] original;
/*     */     
/*     */     public PointerArray(Pointer[] arg) {
/* 817 */       super((Pointer.SIZE * (arg.length + 1)));
/* 818 */       this.original = arg;
/* 819 */       for (int i = 0; i < arg.length; i++) {
/* 820 */         setPointer((i * Pointer.SIZE), arg[i]);
/*     */       }
/* 822 */       setPointer((Pointer.SIZE * arg.length), (Pointer)null);
/*     */     }
/*     */     
/*     */     public void read() {
/* 826 */       read(0L, this.original, 0, this.original.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static Boolean valueOf(boolean b) {
/* 832 */     return b ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */