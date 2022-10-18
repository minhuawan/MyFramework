/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ public interface Library
/*     */ {
/*     */   public static final String OPTION_TYPE_MAPPER = "type-mapper";
/*     */   public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
/*     */   public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
/*     */   public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
/*     */   public static final String OPTION_STRING_ENCODING = "string-encoding";
/*     */   public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
/*     */   public static final String OPTION_CALLING_CONVENTION = "calling-convention";
/*     */   public static final String OPTION_OPEN_FLAGS = "open-flags";
/*     */   public static final String OPTION_CLASSLOADER = "classloader";
/*     */   
/*     */   public static class Handler
/*     */     implements InvocationHandler
/*     */   {
/*     */     static final Method OBJECT_TOSTRING;
/*     */     static final Method OBJECT_HASHCODE;
/*     */     static final Method OBJECT_EQUALS;
/*     */     private final NativeLibrary nativeLibrary;
/*     */     private final Class<?> interfaceClass;
/*     */     private final Map<String, Object> options;
/*     */     private final InvocationMapper invocationMapper;
/*     */     
/*     */     static {
/*     */       try {
/* 123 */         OBJECT_TOSTRING = Object.class.getMethod("toString", new Class[0]);
/* 124 */         OBJECT_HASHCODE = Object.class.getMethod("hashCode", new Class[0]);
/* 125 */         OBJECT_EQUALS = Object.class.getMethod("equals", new Class[] { Object.class });
/* 126 */       } catch (Exception e) {
/* 127 */         throw new Error("Error retrieving Object.toString() method");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static final class FunctionInfo
/*     */     {
/*     */       final InvocationHandler handler;
/*     */       
/*     */       final Function function;
/*     */       
/*     */       final boolean isVarArgs;
/*     */       
/*     */       final Map<String, ?> options;
/*     */       final Class<?>[] parameterTypes;
/*     */       
/*     */       FunctionInfo(InvocationHandler handler, Function function, Class<?>[] parameterTypes, boolean isVarArgs, Map<String, ?> options) {
/* 144 */         this.handler = handler;
/* 145 */         this.function = function;
/* 146 */         this.isVarArgs = isVarArgs;
/* 147 */         this.options = options;
/* 148 */         this.parameterTypes = parameterTypes;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     private final Map<Method, FunctionInfo> functions = new WeakHashMap<Method, FunctionInfo>();
/*     */     
/*     */     public Handler(String libname, Class<?> interfaceClass, Map<String, ?> options) {
/* 160 */       if (libname != null && "".equals(libname.trim())) {
/* 161 */         throw new IllegalArgumentException("Invalid library name \"" + libname + "\"");
/*     */       }
/*     */       
/* 164 */       if (!interfaceClass.isInterface()) {
/* 165 */         throw new IllegalArgumentException(libname + " does not implement an interface: " + interfaceClass.getName());
/*     */       }
/*     */       
/* 168 */       this.interfaceClass = interfaceClass;
/* 169 */       this.options = new HashMap<String, Object>(options);
/* 170 */       int callingConvention = AltCallingConvention.class.isAssignableFrom(interfaceClass) ? 63 : 0;
/*     */ 
/*     */       
/* 173 */       if (this.options.get("calling-convention") == null) {
/* 174 */         this.options.put("calling-convention", Integer.valueOf(callingConvention));
/*     */       }
/* 176 */       if (this.options.get("classloader") == null) {
/* 177 */         this.options.put("classloader", interfaceClass.getClassLoader());
/*     */       }
/* 179 */       this.nativeLibrary = NativeLibrary.getInstance(libname, this.options);
/* 180 */       this.invocationMapper = (InvocationMapper)this.options.get("invocation-mapper");
/*     */     }
/*     */     
/*     */     public NativeLibrary getNativeLibrary() {
/* 184 */       return this.nativeLibrary;
/*     */     }
/*     */     
/*     */     public String getLibraryName() {
/* 188 */       return this.nativeLibrary.getName();
/*     */     }
/*     */     
/*     */     public Class<?> getInterfaceClass() {
/* 192 */       return this.interfaceClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] inArgs) throws Throwable {
/* 200 */       if (OBJECT_TOSTRING.equals(method))
/* 201 */         return "Proxy interface to " + this.nativeLibrary; 
/* 202 */       if (OBJECT_HASHCODE.equals(method))
/* 203 */         return Integer.valueOf(hashCode()); 
/* 204 */       if (OBJECT_EQUALS.equals(method)) {
/* 205 */         Object o = inArgs[0];
/* 206 */         if (o != null && Proxy.isProxyClass(o.getClass())) {
/* 207 */           return Function.valueOf((Proxy.getInvocationHandler(o) == this));
/*     */         }
/* 209 */         return Boolean.FALSE;
/*     */       } 
/*     */ 
/*     */       
/* 213 */       FunctionInfo f = this.functions.get(method);
/* 214 */       if (f == null) {
/* 215 */         synchronized (this.functions) {
/* 216 */           f = this.functions.get(method);
/* 217 */           if (f == null) {
/* 218 */             boolean isVarArgs = Function.isVarArgs(method);
/* 219 */             InvocationHandler handler = null;
/* 220 */             if (this.invocationMapper != null) {
/* 221 */               handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, method);
/*     */             }
/* 223 */             Function function = null;
/* 224 */             Class<?>[] parameterTypes = null;
/* 225 */             Map<String, Object> options = null;
/* 226 */             if (handler == null) {
/*     */               
/* 228 */               function = this.nativeLibrary.getFunction(method.getName(), method);
/* 229 */               parameterTypes = method.getParameterTypes();
/* 230 */               options = new HashMap<String, Object>(this.options);
/* 231 */               options.put("invoking-method", method);
/*     */             } 
/* 233 */             f = new FunctionInfo(handler, function, parameterTypes, isVarArgs, options);
/* 234 */             this.functions.put(method, f);
/*     */           } 
/*     */         } 
/*     */       }
/* 238 */       if (f.isVarArgs) {
/* 239 */         inArgs = Function.concatenateVarArgs(inArgs);
/*     */       }
/* 241 */       if (f.handler != null) {
/* 242 */         return f.handler.invoke(proxy, method, inArgs);
/*     */       }
/* 244 */       return f.function.invoke(method, f.parameterTypes, method.getReturnType(), inArgs, f.options);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\Library.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */