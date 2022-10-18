/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Window;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.WeakHashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Native
/*      */   implements Version
/*      */ {
/*  110 */   public static final String DEFAULT_ENCODING = Charset.defaultCharset().name();
/*  111 */   public static boolean DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
/*  112 */   public static boolean DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
/*      */ 
/*      */   
/*  115 */   static String jnidispatchPath = null;
/*  116 */   private static final Map<Class<?>, Map<String, Object>> typeOptions = new WeakHashMap<Class<?>, Map<String, Object>>();
/*  117 */   private static final Map<Class<?>, Reference<?>> libraries = new WeakHashMap<Class<?>, Reference<?>>(); private static final String _OPTION_ENCLOSING_LIBRARY = "enclosing-library";
/*      */   
/*  119 */   private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler()
/*      */     {
/*      */       public void uncaughtException(Callback c, Throwable e)
/*      */       {
/*  123 */         System.err.println("JNA: Callback " + c + " threw the following exception:");
/*  124 */         e.printStackTrace();
/*      */       }
/*      */     };
/*  127 */   private static Callback.UncaughtExceptionHandler callbackExceptionHandler = DEFAULT_HANDLER;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static float parseVersion(String v) {
/*  153 */     return Float.parseFloat(v.substring(0, v.lastIndexOf(".")));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isCompatibleVersion(String expectedVersion, String nativeVersion) {
/*  167 */     String[] expectedVersionParts = expectedVersion.split("\\.");
/*  168 */     String[] nativeVersionParts = nativeVersion.split("\\.");
/*  169 */     if (expectedVersionParts.length < 3 || nativeVersionParts.length < 3) {
/*  170 */       return false;
/*      */     }
/*      */     
/*  173 */     int expectedMajor = Integer.parseInt(expectedVersionParts[0]);
/*  174 */     int nativeMajor = Integer.parseInt(nativeVersionParts[0]);
/*  175 */     int expectedMinor = Integer.parseInt(expectedVersionParts[1]);
/*  176 */     int nativeMinor = Integer.parseInt(nativeVersionParts[1]);
/*      */     
/*  178 */     if (expectedMajor != nativeMajor) {
/*  179 */       return false;
/*      */     }
/*      */     
/*  182 */     if (expectedMinor > nativeMinor) {
/*  183 */       return false;
/*      */     }
/*      */     
/*  186 */     return true;
/*      */   }
/*      */   
/*      */   static {
/*  190 */     loadNativeDispatchLibrary();
/*      */     
/*  192 */     if (!isCompatibleVersion("5.2.0", getNativeVersion())) {
/*  193 */       String LS = System.getProperty("line.separator");
/*  194 */       throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system" + LS + "Expected: " + "5.2.0" + LS + "Found:    " + 
/*      */ 
/*      */           
/*  197 */           getNativeVersion() + LS + ((jnidispatchPath != null) ? ("(at " + jnidispatchPath + ")") : 
/*      */           
/*  199 */           System.getProperty("java.library.path")) + "." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  208 */   public static final int POINTER_SIZE = sizeof(0);
/*  209 */   public static final int LONG_SIZE = sizeof(1);
/*  210 */   public static final int WCHAR_SIZE = sizeof(2);
/*  211 */   public static final int SIZE_T_SIZE = sizeof(3);
/*  212 */   public static final int BOOL_SIZE = sizeof(4); private static final int TYPE_VOIDP = 0; private static final int TYPE_LONG = 1; private static final int TYPE_WCHAR_T = 2; private static final int TYPE_SIZE_T = 3;
/*      */   private static final int TYPE_BOOL = 4;
/*      */   
/*      */   static {
/*  216 */     initIDs();
/*  217 */     if (Boolean.getBoolean("jna.protected"))
/*  218 */       setProtected(true); 
/*      */   }
/*  220 */   static final int MAX_ALIGNMENT = (Platform.isSPARC() || Platform.isWindows() || (
/*  221 */     Platform.isLinux() && (Platform.isARM() || Platform.isPPC() || Platform.isMIPS())) || 
/*  222 */     Platform.isAIX() || 
/*  223 */     Platform.isAndroid()) ? 8 : LONG_SIZE;
/*      */   
/*  225 */   static final int MAX_PADDING = (Platform.isMac() && Platform.isPPC()) ? 8 : MAX_ALIGNMENT; static {
/*  226 */     System.setProperty("jna.loaded", "true");
/*      */   }
/*      */ 
/*      */   
/*  230 */   private static final Object finalizer = new Object()
/*      */     {
/*      */       protected void finalize() {
/*  233 */         Native.dispose();
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*      */   static final String JNA_TMPLIB_PREFIX = "jna";
/*      */ 
/*      */   
/*      */   private static void dispose() {
/*  242 */     CallbackReference.disposeAll();
/*  243 */     Memory.disposeAll();
/*  244 */     NativeLibrary.disposeAll();
/*  245 */     unregisterAll();
/*  246 */     jnidispatchPath = null;
/*  247 */     System.setProperty("jna.loaded", "false");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean deleteLibrary(File lib) {
/*  262 */     if (lib.delete()) {
/*  263 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  267 */     markTemporaryFile(lib);
/*      */     
/*  269 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setPreserveLastError(boolean enable) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean getPreserveLastError() {
/*  315 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getWindowID(Window w) throws HeadlessException {
/*  324 */     return AWT.getWindowID(w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getComponentID(Component c) throws HeadlessException {
/*  334 */     return AWT.getComponentID(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Pointer getWindowPointer(Window w) throws HeadlessException {
/*  344 */     return new Pointer(AWT.getWindowID(w));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Pointer getComponentPointer(Component c) throws HeadlessException {
/*  354 */     return new Pointer(AWT.getComponentID(c));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Pointer getDirectBufferPointer(Buffer b) {
/*  363 */     long peer = _getDirectBufferPointer(b);
/*  364 */     return (peer == 0L) ? null : new Pointer(peer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] buf) {
/*  378 */     return toString(buf, getDefaultStringEncoding());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] buf, String encoding) {
/*  395 */     int len = buf.length;
/*      */     
/*  397 */     for (int index = 0; index < len; index++) {
/*  398 */       if (buf[index] == 0) {
/*  399 */         len = index;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  404 */     if (len == 0) {
/*  405 */       return "";
/*      */     }
/*      */     
/*  408 */     if (encoding != null) {
/*      */       try {
/*  410 */         return new String(buf, 0, len, encoding);
/*      */       }
/*  412 */       catch (UnsupportedEncodingException e) {
/*  413 */         System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  418 */     System.err.println("JNA Warning: Decoding with fallback " + System.getProperty("file.encoding"));
/*  419 */     return new String(buf, 0, len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(char[] buf) {
/*  429 */     int len = buf.length;
/*  430 */     for (int index = 0; index < len; index++) {
/*  431 */       if (buf[index] == '\000') {
/*  432 */         len = index;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  437 */     if (len == 0) {
/*  438 */       return "";
/*      */     }
/*  440 */     return new String(buf, 0, len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> toStringList(char[] buf) {
/*  454 */     return toStringList(buf, 0, buf.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> toStringList(char[] buf, int offset, int len) {
/*  468 */     List<String> list = new ArrayList<String>();
/*  469 */     int lastPos = offset;
/*  470 */     int maxPos = offset + len;
/*  471 */     for (int curPos = offset; curPos < maxPos; curPos++) {
/*  472 */       if (buf[curPos] == '\000') {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  477 */         if (lastPos == curPos) {
/*  478 */           return list;
/*      */         }
/*      */         
/*  481 */         String value = new String(buf, lastPos, curPos - lastPos);
/*  482 */         list.add(value);
/*  483 */         lastPos = curPos + 1;
/*      */       } 
/*      */     } 
/*      */     
/*  487 */     if (lastPos < maxPos) {
/*  488 */       String value = new String(buf, lastPos, maxPos - lastPos);
/*  489 */       list.add(value);
/*      */     } 
/*      */     
/*  492 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T loadLibrary(Class<T> interfaceClass) {
/*  507 */     return loadLibrary((String)null, interfaceClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T loadLibrary(Class<T> interfaceClass, Map<String, ?> options) {
/*  526 */     return loadLibrary(null, interfaceClass, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T loadLibrary(String name, Class<T> interfaceClass) {
/*  544 */     return loadLibrary(name, interfaceClass, Collections.emptyMap());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T loadLibrary(String name, Class<T> interfaceClass, Map<String, ?> options) {
/*  564 */     if (!Library.class.isAssignableFrom(interfaceClass)) {
/*  565 */       throw new IllegalArgumentException("Interface (" + interfaceClass.getSimpleName() + ") of library=" + name + " does not extend " + Library.class
/*  566 */           .getSimpleName());
/*      */     }
/*      */     
/*  569 */     Library.Handler handler = new Library.Handler(name, interfaceClass, options);
/*  570 */     ClassLoader loader = interfaceClass.getClassLoader();
/*  571 */     Object proxy = Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
/*  572 */     cacheOptions(interfaceClass, options, proxy);
/*  573 */     return interfaceClass.cast(proxy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void loadLibraryInstance(Class<?> cls) {
/*  582 */     synchronized (libraries) {
/*  583 */       if (cls != null && !libraries.containsKey(cls)) {
/*      */         try {
/*  585 */           Field[] fields = cls.getFields();
/*  586 */           for (int i = 0; i < fields.length; i++) {
/*  587 */             Field field = fields[i];
/*  588 */             if (field.getType() == cls && 
/*  589 */               Modifier.isStatic(field.getModifiers())) {
/*      */               
/*  591 */               libraries.put(cls, new WeakReference(field.get(null)));
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*  596 */         } catch (Exception e) {
/*  597 */           throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e + ")");
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Class<?> findEnclosingLibraryClass(Class<?> cls) {
/*  612 */     if (cls == null) {
/*  613 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  617 */     synchronized (libraries) {
/*  618 */       if (typeOptions.containsKey(cls)) {
/*  619 */         Map<String, ?> libOptions = typeOptions.get(cls);
/*  620 */         Class<?> enclosingClass = (Class)libOptions.get("enclosing-library");
/*  621 */         if (enclosingClass != null) {
/*  622 */           return enclosingClass;
/*      */         }
/*  624 */         return cls;
/*      */       } 
/*      */     } 
/*  627 */     if (Library.class.isAssignableFrom(cls)) {
/*  628 */       return cls;
/*      */     }
/*  630 */     if (Callback.class.isAssignableFrom(cls)) {
/*  631 */       cls = CallbackReference.findCallbackClass(cls);
/*      */     }
/*  633 */     Class<?> declaring = cls.getDeclaringClass();
/*  634 */     Class<?> fromDeclaring = findEnclosingLibraryClass(declaring);
/*  635 */     if (fromDeclaring != null) {
/*  636 */       return fromDeclaring;
/*      */     }
/*  638 */     return findEnclosingLibraryClass(cls.getSuperclass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> getLibraryOptions(Class<?> type) {
/*  657 */     synchronized (libraries) {
/*  658 */       Map<String, Object> libraryOptions = typeOptions.get(type);
/*  659 */       if (libraryOptions != null) {
/*  660 */         return libraryOptions;
/*      */       }
/*      */     } 
/*      */     
/*  664 */     Class<?> mappingClass = findEnclosingLibraryClass(type);
/*  665 */     if (mappingClass != null) {
/*  666 */       loadLibraryInstance(mappingClass);
/*      */     } else {
/*  668 */       mappingClass = type;
/*      */     } 
/*      */     
/*  671 */     synchronized (libraries) {
/*  672 */       Map<String, Object> libraryOptions = typeOptions.get(mappingClass);
/*  673 */       if (libraryOptions != null) {
/*  674 */         typeOptions.put(type, libraryOptions);
/*  675 */         return libraryOptions;
/*      */       } 
/*      */       
/*      */       try {
/*  679 */         Field field = mappingClass.getField("OPTIONS");
/*  680 */         field.setAccessible(true);
/*  681 */         libraryOptions = (Map<String, Object>)field.get(null);
/*  682 */         if (libraryOptions == null) {
/*  683 */           throw new IllegalStateException("Null options field");
/*      */         }
/*  685 */       } catch (NoSuchFieldException e) {
/*  686 */         libraryOptions = Collections.emptyMap();
/*  687 */       } catch (Exception e) {
/*  688 */         throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e + "): " + mappingClass);
/*      */       } 
/*      */       
/*  691 */       libraryOptions = new HashMap<String, Object>(libraryOptions);
/*  692 */       if (!libraryOptions.containsKey("type-mapper")) {
/*  693 */         libraryOptions.put("type-mapper", lookupField(mappingClass, "TYPE_MAPPER", TypeMapper.class));
/*      */       }
/*  695 */       if (!libraryOptions.containsKey("structure-alignment")) {
/*  696 */         libraryOptions.put("structure-alignment", lookupField(mappingClass, "STRUCTURE_ALIGNMENT", Integer.class));
/*      */       }
/*  698 */       if (!libraryOptions.containsKey("string-encoding")) {
/*  699 */         libraryOptions.put("string-encoding", lookupField(mappingClass, "STRING_ENCODING", String.class));
/*      */       }
/*  701 */       libraryOptions = cacheOptions(mappingClass, libraryOptions, null);
/*      */       
/*  703 */       if (type != mappingClass) {
/*  704 */         typeOptions.put(type, libraryOptions);
/*      */       }
/*  706 */       return libraryOptions;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Object lookupField(Class<?> mappingClass, String fieldName, Class<?> resultClass) {
/*      */     try {
/*  712 */       Field field = mappingClass.getField(fieldName);
/*  713 */       field.setAccessible(true);
/*  714 */       return field.get(null);
/*      */     }
/*  716 */     catch (NoSuchFieldException e) {
/*  717 */       return null;
/*      */     }
/*  719 */     catch (Exception e) {
/*  720 */       throw new IllegalArgumentException(fieldName + " must be a public field of type " + resultClass
/*  721 */           .getName() + " (" + e + "): " + mappingClass);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TypeMapper getTypeMapper(Class<?> cls) {
/*  730 */     Map<String, ?> options = getLibraryOptions(cls);
/*  731 */     return (TypeMapper)options.get("type-mapper");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getStringEncoding(Class<?> cls) {
/*  741 */     Map<String, ?> options = getLibraryOptions(cls);
/*  742 */     String encoding = (String)options.get("string-encoding");
/*  743 */     return (encoding != null) ? encoding : getDefaultStringEncoding();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getDefaultStringEncoding() {
/*  751 */     return System.getProperty("jna.encoding", DEFAULT_ENCODING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getStructureAlignment(Class<?> cls) {
/*  760 */     Integer alignment = (Integer)getLibraryOptions(cls).get("structure-alignment");
/*  761 */     return (alignment == null) ? 0 : alignment.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] getBytes(String s) {
/*  770 */     return getBytes(s, getDefaultStringEncoding());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] getBytes(String s, String encoding) {
/*  782 */     if (encoding != null) {
/*      */       try {
/*  784 */         return s.getBytes(encoding);
/*      */       }
/*  786 */       catch (UnsupportedEncodingException e) {
/*  787 */         System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
/*      */       } 
/*      */     }
/*      */     
/*  791 */     System.err.println("JNA Warning: Encoding with fallback " + 
/*  792 */         System.getProperty("file.encoding"));
/*  793 */     return s.getBytes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(String s) {
/*  803 */     return toByteArray(s, getDefaultStringEncoding());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(String s, String encoding) {
/*  813 */     byte[] bytes = getBytes(s, encoding);
/*  814 */     byte[] buf = new byte[bytes.length + 1];
/*  815 */     System.arraycopy(bytes, 0, buf, 0, bytes.length);
/*  816 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(String s) {
/*  824 */     char[] chars = s.toCharArray();
/*  825 */     char[] buf = new char[chars.length + 1];
/*  826 */     System.arraycopy(chars, 0, buf, 0, chars.length);
/*  827 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void loadNativeDispatchLibrary() {
/*  836 */     if (!Boolean.getBoolean("jna.nounpack")) {
/*      */       try {
/*  838 */         removeTemporaryFiles();
/*      */       }
/*  840 */       catch (IOException e) {
/*  841 */         System.err.println("JNA Warning: IOException removing temporary files: " + e.getMessage());
/*      */       } 
/*      */     }
/*      */     
/*  845 */     String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
/*  846 */     String bootPath = System.getProperty("jna.boot.library.path");
/*  847 */     if (bootPath != null) {
/*      */       
/*  849 */       StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
/*  850 */       while (dirs.hasMoreTokens()) {
/*  851 */         String dir = dirs.nextToken();
/*  852 */         File file = new File(new File(dir), System.mapLibraryName(libName).replace(".dylib", ".jnilib"));
/*  853 */         String path = file.getAbsolutePath();
/*  854 */         if (DEBUG_JNA_LOAD) {
/*  855 */           System.out.println("Looking in " + path);
/*      */         }
/*  857 */         if (file.exists()) {
/*      */           try {
/*  859 */             if (DEBUG_JNA_LOAD) {
/*  860 */               System.out.println("Trying " + path);
/*      */             }
/*  862 */             System.setProperty("jnidispatch.path", path);
/*  863 */             System.load(path);
/*  864 */             jnidispatchPath = path;
/*  865 */             if (DEBUG_JNA_LOAD) {
/*  866 */               System.out.println("Found jnidispatch at " + path);
/*      */             }
/*      */             return;
/*  869 */           } catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  875 */         if (Platform.isMac()) {
/*      */           String orig, ext;
/*  877 */           if (path.endsWith("dylib")) {
/*  878 */             orig = "dylib";
/*  879 */             ext = "jnilib";
/*      */           } else {
/*  881 */             orig = "jnilib";
/*  882 */             ext = "dylib";
/*      */           } 
/*  884 */           path = path.substring(0, path.lastIndexOf(orig)) + ext;
/*  885 */           if (DEBUG_JNA_LOAD) {
/*  886 */             System.out.println("Looking in " + path);
/*      */           }
/*  888 */           if ((new File(path)).exists()) {
/*      */             try {
/*  890 */               if (DEBUG_JNA_LOAD) {
/*  891 */                 System.out.println("Trying " + path);
/*      */               }
/*  893 */               System.setProperty("jnidispatch.path", path);
/*  894 */               System.load(path);
/*  895 */               jnidispatchPath = path;
/*  896 */               if (DEBUG_JNA_LOAD) {
/*  897 */                 System.out.println("Found jnidispatch at " + path);
/*      */               }
/*      */               return;
/*  900 */             } catch (UnsatisfiedLinkError ex) {
/*  901 */               System.err.println("File found at " + path + " but not loadable: " + ex.getMessage());
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  907 */     if (!Boolean.getBoolean("jna.nosys")) {
/*      */       try {
/*  909 */         if (DEBUG_JNA_LOAD) {
/*  910 */           System.out.println("Trying (via loadLibrary) " + libName);
/*      */         }
/*  912 */         System.loadLibrary(libName);
/*  913 */         if (DEBUG_JNA_LOAD) {
/*  914 */           System.out.println("Found jnidispatch on system path");
/*      */         }
/*      */         
/*      */         return;
/*  918 */       } catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
/*      */     }
/*      */     
/*  921 */     if (!Boolean.getBoolean("jna.noclasspath")) {
/*  922 */       loadNativeDispatchLibraryFromClasspath();
/*      */     } else {
/*      */       
/*  925 */       throw new UnsatisfiedLinkError("Unable to locate JNA native support library");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void loadNativeDispatchLibraryFromClasspath() {
/*      */     try {
/*  936 */       String libName = "/com/sun/jna/" + Platform.RESOURCE_PREFIX + "/" + System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
/*  937 */       File lib = extractFromResourcePath(libName, Native.class.getClassLoader());
/*  938 */       if (lib == null && 
/*  939 */         lib == null) {
/*  940 */         throw new UnsatisfiedLinkError("Could not find JNA native support");
/*      */       }
/*      */       
/*  943 */       if (DEBUG_JNA_LOAD) {
/*  944 */         System.out.println("Trying " + lib.getAbsolutePath());
/*      */       }
/*  946 */       System.setProperty("jnidispatch.path", lib.getAbsolutePath());
/*  947 */       System.load(lib.getAbsolutePath());
/*  948 */       jnidispatchPath = lib.getAbsolutePath();
/*  949 */       if (DEBUG_JNA_LOAD) {
/*  950 */         System.out.println("Found jnidispatch at " + jnidispatchPath);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  956 */       if (isUnpacked(lib) && 
/*  957 */         !Boolean.getBoolean("jnidispatch.preserve")) {
/*  958 */         deleteLibrary(lib);
/*      */       }
/*      */     }
/*  961 */     catch (IOException e) {
/*  962 */       throw new UnsatisfiedLinkError(e.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isUnpacked(File file) {
/*  968 */     return file.getName().startsWith("jna");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File extractFromResourcePath(String name) throws IOException {
/*  983 */     return extractFromResourcePath(name, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File extractFromResourcePath(String name, ClassLoader loader) throws IOException {
/* 1000 */     boolean DEBUG = (DEBUG_LOAD || (DEBUG_JNA_LOAD && name.indexOf("jnidispatch") != -1));
/* 1001 */     if (loader == null) {
/* 1002 */       loader = Thread.currentThread().getContextClassLoader();
/*      */       
/* 1004 */       if (loader == null) {
/* 1005 */         loader = Native.class.getClassLoader();
/*      */       }
/*      */     } 
/* 1008 */     if (DEBUG) {
/* 1009 */       System.out.println("Looking in classpath from " + loader + " for " + name);
/*      */     }
/* 1011 */     String libname = name.startsWith("/") ? name : NativeLibrary.mapSharedLibraryName(name);
/* 1012 */     String resourcePath = name.startsWith("/") ? name : (Platform.RESOURCE_PREFIX + "/" + libname);
/* 1013 */     if (resourcePath.startsWith("/")) {
/* 1014 */       resourcePath = resourcePath.substring(1);
/*      */     }
/* 1016 */     URL url = loader.getResource(resourcePath);
/* 1017 */     if (url == null && resourcePath.startsWith(Platform.RESOURCE_PREFIX))
/*      */     {
/* 1019 */       url = loader.getResource(libname);
/*      */     }
/* 1021 */     if (url == null) {
/* 1022 */       String path = System.getProperty("java.class.path");
/* 1023 */       if (loader instanceof URLClassLoader) {
/* 1024 */         path = Arrays.<URL>asList(((URLClassLoader)loader).getURLs()).toString();
/*      */       }
/* 1026 */       throw new IOException("Native library (" + resourcePath + ") not found in resource path (" + path + ")");
/*      */     } 
/* 1028 */     if (DEBUG) {
/* 1029 */       System.out.println("Found library resource at " + url);
/*      */     }
/*      */     
/* 1032 */     File lib = null;
/* 1033 */     if (url.getProtocol().toLowerCase().equals("file")) {
/*      */       try {
/* 1035 */         lib = new File(new URI(url.toString()));
/*      */       }
/* 1037 */       catch (URISyntaxException e) {
/* 1038 */         lib = new File(url.getPath());
/*      */       } 
/* 1040 */       if (DEBUG) {
/* 1041 */         System.out.println("Looking in " + lib.getAbsolutePath());
/*      */       }
/* 1043 */       if (!lib.exists()) {
/* 1044 */         throw new IOException("File URL " + url + " could not be properly decoded");
/*      */       }
/*      */     }
/* 1047 */     else if (!Boolean.getBoolean("jna.nounpack")) {
/* 1048 */       InputStream is = loader.getResourceAsStream(resourcePath);
/* 1049 */       if (is == null) {
/* 1050 */         throw new IOException("Can't obtain InputStream for " + resourcePath);
/*      */       }
/*      */       
/* 1053 */       FileOutputStream fos = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1058 */         File dir = getTempDir();
/* 1059 */         lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
/* 1060 */         if (!Boolean.getBoolean("jnidispatch.preserve")) {
/* 1061 */           lib.deleteOnExit();
/*      */         }
/* 1063 */         fos = new FileOutputStream(lib);
/*      */         
/* 1065 */         byte[] buf = new byte[1024]; int count;
/* 1066 */         while ((count = is.read(buf, 0, buf.length)) > 0) {
/* 1067 */           fos.write(buf, 0, count);
/*      */         }
/*      */       }
/* 1070 */       catch (IOException e) {
/* 1071 */         throw new IOException("Failed to create temporary file for " + name + " library: " + e.getMessage());
/*      */       } finally {
/*      */         
/* 1074 */         try { is.close(); } catch (IOException iOException) {}
/* 1075 */         if (fos != null) {
/* 1076 */           try { fos.close(); } catch (IOException iOException) {}
/*      */         }
/*      */       } 
/*      */     } 
/* 1080 */     return lib;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Library synchronizedLibrary(final Library library) {
/* 1123 */     Class<?> cls = library.getClass();
/* 1124 */     if (!Proxy.isProxyClass(cls)) {
/* 1125 */       throw new IllegalArgumentException("Library must be a proxy class");
/*      */     }
/* 1127 */     InvocationHandler ih = Proxy.getInvocationHandler(library);
/* 1128 */     if (!(ih instanceof Library.Handler)) {
/* 1129 */       throw new IllegalArgumentException("Unrecognized proxy handler: " + ih);
/*      */     }
/* 1131 */     final Library.Handler handler = (Library.Handler)ih;
/* 1132 */     InvocationHandler newHandler = new InvocationHandler()
/*      */       {
/*      */         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 1135 */           synchronized (handler.getNativeLibrary()) {
/* 1136 */             return handler.invoke(library, method, args);
/*      */           } 
/*      */         }
/*      */       };
/* 1140 */     return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls
/* 1141 */         .getInterfaces(), newHandler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getWebStartLibraryPath(String libName) {
/* 1161 */     if (System.getProperty("javawebstart.version") == null) {
/* 1162 */       return null;
/*      */     }
/*      */     try {
/* 1165 */       ClassLoader cl = Native.class.getClassLoader();
/* 1166 */       Method m = AccessController.<Method>doPrivileged(new PrivilegedAction<Method>()
/*      */           {
/*      */             public Method run() {
/*      */               try {
/* 1170 */                 Method m = ClassLoader.class.getDeclaredMethod("findLibrary", new Class[] { String.class });
/* 1171 */                 m.setAccessible(true);
/* 1172 */                 return m;
/*      */               }
/* 1174 */               catch (Exception e) {
/* 1175 */                 return null;
/*      */               } 
/*      */             }
/*      */           });
/* 1179 */       String libpath = (String)m.invoke(cl, new Object[] { libName });
/* 1180 */       if (libpath != null) {
/* 1181 */         return (new File(libpath)).getParent();
/*      */       }
/* 1183 */       return null;
/*      */     }
/* 1185 */     catch (Exception e) {
/* 1186 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void markTemporaryFile(File file) {
/*      */     try {
/* 1196 */       File marker = new File(file.getParentFile(), file.getName() + ".x");
/* 1197 */       marker.createNewFile();
/*      */     } catch (IOException e) {
/* 1199 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static File getTempDir() throws IOException {
/*      */     File jnatmp;
/* 1207 */     String prop = System.getProperty("jna.tmpdir");
/* 1208 */     if (prop != null) {
/* 1209 */       jnatmp = new File(prop);
/* 1210 */       jnatmp.mkdirs();
/*      */     } else {
/*      */       
/* 1213 */       File tmp = new File(System.getProperty("java.io.tmpdir"));
/*      */ 
/*      */ 
/*      */       
/* 1217 */       jnatmp = new File(tmp, "jna-" + System.getProperty("user.name").hashCode());
/* 1218 */       jnatmp.mkdirs();
/* 1219 */       if (!jnatmp.exists() || !jnatmp.canWrite()) {
/* 1220 */         jnatmp = tmp;
/*      */       }
/*      */     } 
/* 1223 */     if (!jnatmp.exists()) {
/* 1224 */       throw new IOException("JNA temporary directory '" + jnatmp + "' does not exist");
/*      */     }
/* 1226 */     if (!jnatmp.canWrite()) {
/* 1227 */       throw new IOException("JNA temporary directory '" + jnatmp + "' is not writable");
/*      */     }
/* 1229 */     return jnatmp;
/*      */   }
/*      */ 
/*      */   
/*      */   static void removeTemporaryFiles() throws IOException {
/* 1234 */     File dir = getTempDir();
/* 1235 */     FilenameFilter filter = new FilenameFilter()
/*      */       {
/*      */         public boolean accept(File dir, String name) {
/* 1238 */           return (name.endsWith(".x") && name.startsWith("jna"));
/*      */         }
/*      */       };
/* 1241 */     File[] files = dir.listFiles(filter);
/* 1242 */     for (int i = 0; files != null && i < files.length; i++) {
/* 1243 */       File marker = files[i];
/* 1244 */       String name = marker.getName();
/* 1245 */       name = name.substring(0, name.length() - 2);
/* 1246 */       File target = new File(marker.getParentFile(), name);
/* 1247 */       if (!target.exists() || target.delete()) {
/* 1248 */         marker.delete();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getNativeSize(Class<?> type, Object value) {
/* 1260 */     if (type.isArray()) {
/* 1261 */       int len = Array.getLength(value);
/* 1262 */       if (len > 0) {
/* 1263 */         Object o = Array.get(value, 0);
/* 1264 */         return len * getNativeSize(type.getComponentType(), o);
/*      */       } 
/*      */       
/* 1267 */       throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
/*      */     } 
/* 1269 */     if (Structure.class.isAssignableFrom(type) && 
/* 1270 */       !Structure.ByReference.class.isAssignableFrom(type)) {
/* 1271 */       return Structure.size(type, (Structure)value);
/*      */     }
/*      */     try {
/* 1274 */       return getNativeSize(type);
/*      */     }
/* 1276 */     catch (IllegalArgumentException e) {
/* 1277 */       throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e
/*      */           
/* 1279 */           .getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getNativeSize(Class<?> cls) {
/* 1292 */     if (NativeMapped.class.isAssignableFrom(cls)) {
/* 1293 */       cls = NativeMappedConverter.getInstance(cls).nativeType();
/*      */     }
/*      */     
/* 1296 */     if (cls == boolean.class || cls == Boolean.class) return 4; 
/* 1297 */     if (cls == byte.class || cls == Byte.class) return 1; 
/* 1298 */     if (cls == short.class || cls == Short.class) return 2; 
/* 1299 */     if (cls == char.class || cls == Character.class) return WCHAR_SIZE; 
/* 1300 */     if (cls == int.class || cls == Integer.class) return 4; 
/* 1301 */     if (cls == long.class || cls == Long.class) return 8; 
/* 1302 */     if (cls == float.class || cls == Float.class) return 4; 
/* 1303 */     if (cls == double.class || cls == Double.class) return 8; 
/* 1304 */     if (Structure.class.isAssignableFrom(cls)) {
/* 1305 */       if (Structure.ByValue.class.isAssignableFrom(cls)) {
/* 1306 */         return Structure.size(cls);
/*      */       }
/* 1308 */       return POINTER_SIZE;
/*      */     } 
/* 1310 */     if (Pointer.class.isAssignableFrom(cls) || (Platform.HAS_BUFFERS && 
/* 1311 */       Buffers.isBuffer(cls)) || Callback.class
/* 1312 */       .isAssignableFrom(cls) || String.class == cls || WString.class == cls)
/*      */     {
/*      */       
/* 1315 */       return POINTER_SIZE;
/*      */     }
/* 1317 */     throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSupportedNativeType(Class<?> cls) {
/* 1326 */     if (Structure.class.isAssignableFrom(cls)) {
/* 1327 */       return true;
/*      */     }
/*      */     try {
/* 1330 */       return (getNativeSize(cls) != 0);
/*      */     }
/* 1332 */     catch (IllegalArgumentException e) {
/* 1333 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler eh) {
/* 1345 */     callbackExceptionHandler = (eh == null) ? DEFAULT_HANDLER : eh;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
/* 1350 */     return callbackExceptionHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(String libName) {
/* 1360 */     register(findDirectMappedClass(getCallingClass()), libName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(NativeLibrary lib) {
/* 1370 */     register(findDirectMappedClass(getCallingClass()), lib);
/*      */   }
/*      */ 
/*      */   
/*      */   static Class<?> findDirectMappedClass(Class<?> cls) {
/* 1375 */     Method[] methods = cls.getDeclaredMethods();
/* 1376 */     for (Method m : methods) {
/* 1377 */       if ((m.getModifiers() & 0x100) != 0) {
/* 1378 */         return cls;
/*      */       }
/*      */     } 
/* 1381 */     int idx = cls.getName().lastIndexOf("$");
/* 1382 */     if (idx != -1) {
/* 1383 */       String name = cls.getName().substring(0, idx);
/*      */       try {
/* 1385 */         return findDirectMappedClass(Class.forName(name, true, cls.getClassLoader()));
/* 1386 */       } catch (ClassNotFoundException classNotFoundException) {}
/*      */     } 
/*      */ 
/*      */     
/* 1390 */     throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Class<?> getCallingClass() {
/* 1402 */     Class<?>[] context = (new SecurityManager() { public Class<?>[] getClassContext() { return super.getClassContext(); } }).getClassContext();
/* 1403 */     if (context == null) {
/* 1404 */       throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
/*      */     }
/* 1406 */     if (context.length < 4) {
/* 1407 */       throw new IllegalStateException("This method must be called from the static initializer of a class");
/*      */     }
/* 1409 */     return context[3];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) {
/* 1419 */     CallbackReference.setCallbackThreadInitializer(cb, initializer);
/*      */   }
/*      */   
/* 1422 */   private static Map<Class<?>, long[]> registeredClasses = (Map)new WeakHashMap<Class<?>, long>();
/* 1423 */   private static Map<Class<?>, NativeLibrary> registeredLibraries = new WeakHashMap<Class<?>, NativeLibrary>(); static final int CB_HAS_INITIALIZER = 1; private static final int CVT_UNSUPPORTED = -1; private static final int CVT_DEFAULT = 0; private static final int CVT_POINTER = 1; private static final int CVT_STRING = 2; private static final int CVT_STRUCTURE = 3; private static final int CVT_STRUCTURE_BYVAL = 4; private static final int CVT_BUFFER = 5; private static final int CVT_ARRAY_BYTE = 6; private static final int CVT_ARRAY_SHORT = 7; private static final int CVT_ARRAY_CHAR = 8; private static final int CVT_ARRAY_INT = 9; private static final int CVT_ARRAY_LONG = 10; private static final int CVT_ARRAY_FLOAT = 11; private static final int CVT_ARRAY_DOUBLE = 12; private static final int CVT_ARRAY_BOOLEAN = 13; private static final int CVT_BOOLEAN = 14; private static final int CVT_CALLBACK = 15; private static final int CVT_FLOAT = 16; private static final int CVT_NATIVE_MAPPED = 17; private static final int CVT_NATIVE_MAPPED_STRING = 18; private static final int CVT_NATIVE_MAPPED_WSTRING = 19; private static final int CVT_WSTRING = 20; private static final int CVT_INTEGER_TYPE = 21; private static final int CVT_POINTER_TYPE = 22; private static final int CVT_TYPE_MAPPER = 23; private static final int CVT_TYPE_MAPPER_STRING = 24; private static final int CVT_TYPE_MAPPER_WSTRING = 25; private static final int CVT_OBJECT = 26; private static final int CVT_JNIENV = 27; static final int CB_OPTION_DIRECT = 1; static final int CB_OPTION_IN_DLL = 2;
/*      */   
/*      */   private static void unregisterAll() {
/* 1426 */     synchronized (registeredClasses) {
/* 1427 */       for (Map.Entry<Class<?>, long[]> e : registeredClasses.entrySet()) {
/* 1428 */         unregister(e.getKey(), e.getValue());
/*      */       }
/*      */       
/* 1431 */       registeredClasses.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unregister() {
/* 1440 */     unregister(findDirectMappedClass(getCallingClass()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unregister(Class<?> cls) {
/* 1448 */     synchronized (registeredClasses) {
/* 1449 */       long[] handles = registeredClasses.get(cls);
/* 1450 */       if (handles != null) {
/* 1451 */         unregister(cls, handles);
/* 1452 */         registeredClasses.remove(cls);
/* 1453 */         registeredLibraries.remove(cls);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean registered(Class<?> cls) {
/* 1463 */     synchronized (registeredClasses) {
/* 1464 */       return registeredClasses.containsKey(cls);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getSignature(Class<?> cls) {
/* 1472 */     if (cls.isArray()) {
/* 1473 */       return "[" + getSignature(cls.getComponentType());
/*      */     }
/* 1475 */     if (cls.isPrimitive()) {
/* 1476 */       if (cls == void.class) return "V"; 
/* 1477 */       if (cls == boolean.class) return "Z"; 
/* 1478 */       if (cls == byte.class) return "B"; 
/* 1479 */       if (cls == short.class) return "S"; 
/* 1480 */       if (cls == char.class) return "C"; 
/* 1481 */       if (cls == int.class) return "I"; 
/* 1482 */       if (cls == long.class) return "J"; 
/* 1483 */       if (cls == float.class) return "F"; 
/* 1484 */       if (cls == double.class) return "D"; 
/*      */     } 
/* 1486 */     return "L" + replace(".", "/", cls.getName()) + ";";
/*      */   }
/*      */ 
/*      */   
/*      */   static String replace(String s1, String s2, String str) {
/* 1491 */     StringBuilder buf = new StringBuilder();
/*      */     while (true) {
/* 1493 */       int idx = str.indexOf(s1);
/* 1494 */       if (idx == -1) {
/* 1495 */         buf.append(str);
/*      */         
/*      */         break;
/*      */       } 
/* 1499 */       buf.append(str.substring(0, idx));
/* 1500 */       buf.append(s2);
/* 1501 */       str = str.substring(idx + s1.length());
/*      */     } 
/*      */     
/* 1504 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getConversion(Class<?> type, TypeMapper mapper, boolean allowObjects) {
/* 1541 */     if (type == Boolean.class) { type = boolean.class; }
/* 1542 */     else if (type == Byte.class) { type = byte.class; }
/* 1543 */     else if (type == Short.class) { type = short.class; }
/* 1544 */     else if (type == Character.class) { type = char.class; }
/* 1545 */     else if (type == Integer.class) { type = int.class; }
/* 1546 */     else if (type == Long.class) { type = long.class; }
/* 1547 */     else if (type == Float.class) { type = float.class; }
/* 1548 */     else if (type == Double.class) { type = double.class; }
/* 1549 */     else if (type == Void.class) { type = void.class; }
/*      */     
/* 1551 */     if (mapper != null) {
/* 1552 */       FromNativeConverter fromNative = mapper.getFromNativeConverter(type);
/* 1553 */       ToNativeConverter toNative = mapper.getToNativeConverter(type);
/* 1554 */       if (fromNative != null) {
/* 1555 */         Class<?> nativeType = fromNative.nativeType();
/* 1556 */         if (nativeType == String.class) {
/* 1557 */           return 24;
/*      */         }
/* 1559 */         if (nativeType == WString.class) {
/* 1560 */           return 25;
/*      */         }
/* 1562 */         return 23;
/*      */       } 
/* 1564 */       if (toNative != null) {
/* 1565 */         Class<?> nativeType = toNative.nativeType();
/* 1566 */         if (nativeType == String.class) {
/* 1567 */           return 24;
/*      */         }
/* 1569 */         if (nativeType == WString.class) {
/* 1570 */           return 25;
/*      */         }
/* 1572 */         return 23;
/*      */       } 
/*      */     } 
/*      */     
/* 1576 */     if (Pointer.class.isAssignableFrom(type)) {
/* 1577 */       return 1;
/*      */     }
/* 1579 */     if (String.class == type) {
/* 1580 */       return 2;
/*      */     }
/* 1582 */     if (WString.class.isAssignableFrom(type)) {
/* 1583 */       return 20;
/*      */     }
/* 1585 */     if (Platform.HAS_BUFFERS && Buffers.isBuffer(type)) {
/* 1586 */       return 5;
/*      */     }
/* 1588 */     if (Structure.class.isAssignableFrom(type)) {
/* 1589 */       if (Structure.ByValue.class.isAssignableFrom(type)) {
/* 1590 */         return 4;
/*      */       }
/* 1592 */       return 3;
/*      */     } 
/* 1594 */     if (type.isArray()) {
/* 1595 */       switch (type.getName().charAt(1)) { case 'Z':
/* 1596 */           return 13;
/* 1597 */         case 'B': return 6;
/* 1598 */         case 'S': return 7;
/* 1599 */         case 'C': return 8;
/* 1600 */         case 'I': return 9;
/* 1601 */         case 'J': return 10;
/* 1602 */         case 'F': return 11;
/* 1603 */         case 'D': return 12; }
/*      */ 
/*      */     
/*      */     }
/* 1607 */     if (type.isPrimitive()) {
/* 1608 */       return (type == boolean.class) ? 14 : 0;
/*      */     }
/* 1610 */     if (Callback.class.isAssignableFrom(type)) {
/* 1611 */       return 15;
/*      */     }
/* 1613 */     if (IntegerType.class.isAssignableFrom(type)) {
/* 1614 */       return 21;
/*      */     }
/* 1616 */     if (PointerType.class.isAssignableFrom(type)) {
/* 1617 */       return 22;
/*      */     }
/* 1619 */     if (NativeMapped.class.isAssignableFrom(type)) {
/* 1620 */       Class<?> nativeType = NativeMappedConverter.getInstance(type).nativeType();
/* 1621 */       if (nativeType == String.class) {
/* 1622 */         return 18;
/*      */       }
/* 1624 */       if (nativeType == WString.class) {
/* 1625 */         return 19;
/*      */       }
/* 1627 */       return 17;
/*      */     } 
/* 1629 */     if (JNIEnv.class == type) {
/* 1630 */       return 27;
/*      */     }
/* 1632 */     return allowObjects ? 26 : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(Class<?> cls, String libName) {
/* 1647 */     NativeLibrary library = NativeLibrary.getInstance(libName, Collections.singletonMap("classloader", cls.getClassLoader()));
/* 1648 */     register(cls, library);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(Class<?> cls, NativeLibrary lib) {
/* 1661 */     Method[] methods = cls.getDeclaredMethods();
/* 1662 */     List<Method> mlist = new ArrayList<Method>();
/* 1663 */     Map<String, ?> options = lib.getOptions();
/* 1664 */     TypeMapper mapper = (TypeMapper)options.get("type-mapper");
/* 1665 */     boolean allowObjects = Boolean.TRUE.equals(options.get("allow-objects"));
/* 1666 */     options = cacheOptions(cls, options, null);
/*      */     
/* 1668 */     for (Method m : methods) {
/* 1669 */       if ((m.getModifiers() & 0x100) != 0) {
/* 1670 */         mlist.add(m);
/*      */       }
/*      */     } 
/*      */     
/* 1674 */     long[] handles = new long[mlist.size()];
/* 1675 */     for (int i = 0; i < handles.length; i++) {
/* 1676 */       long rtype, closure_rtype; Method method = mlist.get(i);
/* 1677 */       String sig = "(";
/* 1678 */       Class<?> rclass = method.getReturnType();
/*      */       
/* 1680 */       Class<?>[] ptypes = method.getParameterTypes();
/* 1681 */       long[] atypes = new long[ptypes.length];
/* 1682 */       long[] closure_atypes = new long[ptypes.length];
/* 1683 */       int[] cvt = new int[ptypes.length];
/* 1684 */       ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
/* 1685 */       FromNativeConverter fromNative = null;
/* 1686 */       int rcvt = getConversion(rclass, mapper, allowObjects);
/* 1687 */       boolean throwLastError = false;
/* 1688 */       switch (rcvt) {
/*      */         case -1:
/* 1690 */           throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/* 1694 */           fromNative = mapper.getFromNativeConverter(rclass);
/*      */ 
/*      */ 
/*      */           
/* 1698 */           closure_rtype = (Structure.FFIType.get(rclass.isPrimitive() ? rclass : Pointer.class)).peer;
/* 1699 */           rtype = (Structure.FFIType.get(fromNative.nativeType())).peer;
/*      */           break;
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 21:
/*      */         case 22:
/* 1706 */           closure_rtype = (Structure.FFIType.get(Pointer.class)).peer;
/* 1707 */           rtype = (Structure.FFIType.get(NativeMappedConverter.getInstance(rclass).nativeType())).peer;
/*      */           break;
/*      */         case 3:
/*      */         case 26:
/* 1711 */           closure_rtype = rtype = (Structure.FFIType.get(Pointer.class)).peer;
/*      */         
/*      */         case 4:
/* 1714 */           closure_rtype = (Structure.FFIType.get(Pointer.class)).peer;
/* 1715 */           rtype = (Structure.FFIType.get(rclass)).peer;
/*      */           break;
/*      */         default:
/* 1718 */           closure_rtype = rtype = (Structure.FFIType.get(rclass)).peer;
/*      */           break;
/*      */       } 
/* 1721 */       for (int t = 0; t < ptypes.length; t++) {
/* 1722 */         Class<?> type = ptypes[t];
/* 1723 */         sig = sig + getSignature(type);
/* 1724 */         int conversionType = getConversion(type, mapper, allowObjects);
/* 1725 */         cvt[t] = conversionType;
/* 1726 */         if (conversionType == -1) {
/* 1727 */           throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")");
/*      */         }
/* 1729 */         if (conversionType == 17 || conversionType == 18 || conversionType == 19 || conversionType == 21) {
/*      */ 
/*      */ 
/*      */           
/* 1733 */           type = NativeMappedConverter.getInstance(type).nativeType();
/* 1734 */         } else if (conversionType == 23 || conversionType == 24 || conversionType == 25) {
/*      */ 
/*      */           
/* 1737 */           toNative[t] = mapper.getToNativeConverter(type);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1743 */         switch (conversionType) {
/*      */           case 4:
/*      */           case 17:
/*      */           case 18:
/*      */           case 19:
/*      */           case 21:
/*      */           case 22:
/* 1750 */             atypes[t] = (Structure.FFIType.get(type)).peer;
/* 1751 */             closure_atypes[t] = (Structure.FFIType.get(Pointer.class)).peer;
/*      */             break;
/*      */           case 23:
/*      */           case 24:
/*      */           case 25:
/* 1756 */             closure_atypes[t] = (Structure.FFIType.get(type.isPrimitive() ? type : Pointer.class)).peer;
/* 1757 */             atypes[t] = (Structure.FFIType.get(toNative[t].nativeType())).peer;
/*      */             break;
/*      */           case 0:
/* 1760 */             atypes[t] = (Structure.FFIType.get(type)).peer; closure_atypes[t] = (Structure.FFIType.get(type)).peer;
/*      */           
/*      */           default:
/* 1763 */             atypes[t] = (Structure.FFIType.get(Pointer.class)).peer; closure_atypes[t] = (Structure.FFIType.get(Pointer.class)).peer; break;
/*      */         } 
/*      */       } 
/* 1766 */       sig = sig + ")";
/* 1767 */       sig = sig + getSignature(rclass);
/*      */       
/* 1769 */       Class<?>[] etypes = method.getExceptionTypes();
/* 1770 */       for (int e = 0; e < etypes.length; e++) {
/* 1771 */         if (LastErrorException.class.isAssignableFrom(etypes[e])) {
/* 1772 */           throwLastError = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1777 */       Function f = lib.getFunction(method.getName(), method);
/*      */       try {
/* 1779 */         handles[i] = registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, method, f.peer, f
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1784 */             .getCallingConvention(), throwLastError, toNative, fromNative, f.encoding);
/*      */ 
/*      */       
/*      */       }
/* 1788 */       catch (NoSuchMethodError noSuchMethodError) {
/* 1789 */         throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
/*      */       } 
/*      */     } 
/* 1792 */     synchronized (registeredClasses) {
/* 1793 */       registeredClasses.put(cls, handles);
/* 1794 */       registeredLibraries.put(cls, lib);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, Object> cacheOptions(Class<?> cls, Map<String, ?> options, Object proxy) {
/* 1802 */     Map<String, Object> libOptions = new HashMap<String, Object>(options);
/* 1803 */     libOptions.put("enclosing-library", cls);
/* 1804 */     synchronized (libraries) {
/* 1805 */       typeOptions.put(cls, libOptions);
/* 1806 */       if (proxy != null) {
/* 1807 */         libraries.put(cls, new WeakReference(proxy));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1813 */       if (!cls.isInterface() && Library.class
/* 1814 */         .isAssignableFrom(cls)) {
/* 1815 */         Class<?>[] ifaces = cls.getInterfaces();
/* 1816 */         for (Class<?> ifc : ifaces) {
/* 1817 */           if (Library.class.isAssignableFrom(ifc)) {
/* 1818 */             cacheOptions(ifc, libOptions, proxy);
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1824 */     return libOptions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static NativeMapped fromNative(Class<?> cls, Object value) {
/* 1848 */     return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
/*      */   }
/*      */   
/*      */   private static NativeMapped fromNative(Method m, Object value) {
/* 1852 */     Class<?> cls = m.getReturnType();
/* 1853 */     return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new MethodResultContext(cls, null, null, m));
/*      */   }
/*      */   
/*      */   private static Class<?> nativeType(Class<?> cls) {
/* 1857 */     return NativeMappedConverter.getInstance(cls).nativeType();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object toNative(ToNativeConverter cvt, Object o) {
/* 1863 */     return cvt.toNative(o, new ToNativeContext());
/*      */   }
/*      */   
/*      */   private static Object fromNative(FromNativeConverter cvt, Object o, Method m) {
/* 1867 */     return cvt.fromNative(o, new MethodResultContext(m.getReturnType(), null, null, m));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/* 1886 */     String DEFAULT_TITLE = "Java Native Access (JNA)";
/* 1887 */     String DEFAULT_VERSION = "4.5.1";
/* 1888 */     String DEFAULT_BUILD = "4.5.1 (package information missing)";
/* 1889 */     Package pkg = Native.class.getPackage();
/*      */     
/* 1891 */     String title = (pkg != null) ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
/* 1892 */     if (title == null) title = "Java Native Access (JNA)";
/*      */     
/* 1894 */     String version = (pkg != null) ? pkg.getSpecificationVersion() : "4.5.1";
/* 1895 */     if (version == null) version = "4.5.1"; 
/* 1896 */     title = title + " API Version " + version;
/* 1897 */     System.out.println(title);
/*      */     
/* 1899 */     version = (pkg != null) ? pkg.getImplementationVersion() : "4.5.1 (package information missing)";
/* 1900 */     if (version == null) version = "4.5.1 (package information missing)"; 
/* 1901 */     System.out.println("Version: " + version);
/* 1902 */     System.out.println(" Native: " + getNativeVersion() + " (" + 
/* 1903 */         getAPIChecksum() + ")");
/* 1904 */     System.out.println(" Prefix: " + Platform.RESOURCE_PREFIX);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Structure invokeStructure(Function function, long fp, int callFlags, Object[] args, Structure s) {
/* 2029 */     invokeStructure(function, fp, callFlags, args, (s.getPointer()).peer, 
/* 2030 */         (s.getTypeInfo()).peer);
/* 2031 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long open(String name) {
/* 2049 */     return open(name, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Pointer getPointer(long addr) {
/* 2138 */     long peer = _getPointer(addr);
/* 2139 */     return (peer == 0L) ? null : new Pointer(peer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getString(Pointer pointer, long offset) {
/* 2147 */     return getString(pointer, offset, getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */   static String getString(Pointer pointer, long offset, String encoding) {
/* 2151 */     byte[] data = getStringBytes(pointer, pointer.peer, offset);
/* 2152 */     if (encoding != null) {
/*      */       try {
/* 2154 */         return new String(data, encoding);
/*      */       }
/* 2156 */       catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*      */     }
/*      */     
/* 2159 */     return new String(data);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2213 */   private static final ThreadLocal<Memory> nativeThreadTerminationFlag = new ThreadLocal<Memory>()
/*      */     {
/*      */       protected Memory initialValue()
/*      */       {
/* 2217 */         Memory m = new Memory(4L);
/* 2218 */         m.clear();
/* 2219 */         return m;
/*      */       }
/*      */     };
/* 2222 */   private static final Map<Thread, Pointer> nativeThreads = Collections.synchronizedMap(new WeakHashMap<Thread, Pointer>()); private static native void initIDs(); public static synchronized native void setProtected(boolean paramBoolean); public static synchronized native boolean isProtected(); static native long getWindowHandle0(Component paramComponent); private static native long _getDirectBufferPointer(Buffer paramBuffer); private static native int sizeof(int paramInt);
/*      */   private static native String getNativeVersion();
/*      */   private static native String getAPIChecksum();
/*      */   public static native int getLastError();
/*      */   public static native void setLastError(int paramInt);
/*      */   private static native void unregister(Class<?> paramClass, long[] paramArrayOflong);
/*      */   private static native long registerMethod(Class<?> paramClass, String paramString1, String paramString2, int[] paramArrayOfint, long[] paramArrayOflong1, long[] paramArrayOflong2, int paramInt1, long paramLong1, long paramLong2, Method paramMethod, long paramLong3, int paramInt2, boolean paramBoolean, ToNativeConverter[] paramArrayOfToNativeConverter, FromNativeConverter paramFromNativeConverter, String paramString3);
/*      */   public static native long ffi_prep_cif(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   public static native void ffi_call(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   public static native long ffi_prep_closure(long paramLong, ffi_callback paramffi_callback);
/*      */   public static native void ffi_free_closure(long paramLong);
/*      */   static native int initialize_ffi_type(long paramLong);
/*      */   static synchronized native void freeNativeCallback(long paramLong);
/*      */   public static void detach(boolean detach) {
/* 2236 */     Thread thread = Thread.currentThread();
/* 2237 */     if (detach) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2244 */       nativeThreads.remove(thread);
/* 2245 */       Pointer p = nativeThreadTerminationFlag.get();
/* 2246 */       setDetachState(true, 0L);
/*      */     
/*      */     }
/* 2249 */     else if (!nativeThreads.containsKey(thread)) {
/* 2250 */       Pointer p = nativeThreadTerminationFlag.get();
/* 2251 */       nativeThreads.put(thread, p);
/* 2252 */       setDetachState(false, p.peer);
/*      */     } 
/*      */   } static synchronized native long createNativeCallback(Callback paramCallback, Method paramMethod, Class<?>[] paramArrayOfClass, Class<?> paramClass, int paramInt1, int paramInt2, String paramString); static native int invokeInt(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native long invokeLong(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native void invokeVoid(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native float invokeFloat(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native double invokeDouble(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native long invokePointer(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); private static native void invokeStructure(Function paramFunction, long paramLong1, int paramInt, Object[] paramArrayOfObject, long paramLong2, long paramLong3); static native Object invokeObject(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native long open(String paramString, int paramInt); static native void close(long paramLong); static native long findSymbol(long paramLong, String paramString); static native long indexOf(Pointer paramPointer, long paramLong1, long paramLong2, byte paramByte); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, short[] paramArrayOfshort, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, char[] paramArrayOfchar, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, int[] paramArrayOfint, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, long[] paramArrayOflong, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, float[] paramArrayOffloat, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, double[] paramArrayOfdouble, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, short[] paramArrayOfshort, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, char[] paramArrayOfchar, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, int[] paramArrayOfint, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, long[] paramArrayOflong, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, float[] paramArrayOffloat, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, double[] paramArrayOfdouble, int paramInt1, int paramInt2); static native byte getByte(Pointer paramPointer, long paramLong1, long paramLong2); static native char getChar(Pointer paramPointer, long paramLong1, long paramLong2); static native short getShort(Pointer paramPointer, long paramLong1, long paramLong2); static native int getInt(Pointer paramPointer, long paramLong1, long paramLong2); static native long getLong(Pointer paramPointer, long paramLong1, long paramLong2); static native float getFloat(Pointer paramPointer, long paramLong1, long paramLong2); static native double getDouble(Pointer paramPointer, long paramLong1, long paramLong2);
/*      */   private static native long _getPointer(long paramLong);
/*      */   static native String getWideString(Pointer paramPointer, long paramLong1, long paramLong2);
/*      */   static Pointer getTerminationFlag(Thread t) {
/* 2258 */     return nativeThreads.get(t);
/*      */   } static native byte[] getStringBytes(Pointer paramPointer, long paramLong1, long paramLong2); static native void setMemory(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3, byte paramByte); static native void setByte(Pointer paramPointer, long paramLong1, long paramLong2, byte paramByte); static native void setShort(Pointer paramPointer, long paramLong1, long paramLong2, short paramShort); static native void setChar(Pointer paramPointer, long paramLong1, long paramLong2, char paramChar); static native void setInt(Pointer paramPointer, long paramLong1, long paramLong2, int paramInt); static native void setLong(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3); static native void setFloat(Pointer paramPointer, long paramLong1, long paramLong2, float paramFloat); static native void setDouble(Pointer paramPointer, long paramLong1, long paramLong2, double paramDouble); static native void setPointer(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3); static native void setWideString(Pointer paramPointer, long paramLong1, long paramLong2, String paramString); static native ByteBuffer getDirectByteBuffer(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3); public static native long malloc(long paramLong); public static native void free(long paramLong); @Deprecated
/*      */   public static native ByteBuffer getDirectByteBuffer(long paramLong1, long paramLong2);
/*      */   private static native void setDetachState(boolean paramBoolean, long paramLong);
/*      */   public static interface ffi_callback {
/*      */     void invoke(long param1Long1, long param1Long2, long param1Long3); }
/*      */   private static class Buffers { static boolean isBuffer(Class<?> cls) {
/* 2265 */       return Buffer.class.isAssignableFrom(cls);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AWT
/*      */   {
/*      */     static long getWindowID(Window w) throws HeadlessException {
/* 2274 */       return getComponentID(w);
/*      */     }
/*      */ 
/*      */     
/*      */     static long getComponentID(Object o) throws HeadlessException {
/* 2279 */       if (GraphicsEnvironment.isHeadless()) {
/* 2280 */         throw new HeadlessException("No native windows when headless");
/*      */       }
/* 2282 */       Component c = (Component)o;
/* 2283 */       if (c.isLightweight()) {
/* 2284 */         throw new IllegalArgumentException("Component must be heavyweight");
/*      */       }
/* 2286 */       if (!c.isDisplayable()) {
/* 2287 */         throw new IllegalStateException("Component must be displayable");
/*      */       }
/* 2289 */       if (Platform.isX11() && 
/* 2290 */         System.getProperty("java.version").startsWith("1.4") && 
/* 2291 */         !c.isVisible()) {
/* 2292 */         throw new IllegalStateException("Component must be visible");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2298 */       return Native.getWindowHandle0(c);
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */