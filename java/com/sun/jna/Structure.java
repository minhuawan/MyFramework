/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.nio.Buffer;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Structure
/*      */ {
/*      */   public static final int ALIGN_DEFAULT = 0;
/*      */   public static final int ALIGN_NONE = 1;
/*      */   public static final int ALIGN_GNUC = 2;
/*      */   public static final int ALIGN_MSVC = 3;
/*      */   protected static final int CALCULATE_SIZE = -1;
/*  136 */   static final Map<Class<?>, LayoutInfo> layoutInfo = new WeakHashMap<Class<?>, LayoutInfo>();
/*  137 */   static final Map<Class<?>, List<String>> fieldOrder = new WeakHashMap<Class<?>, List<String>>();
/*      */   
/*      */   private Pointer memory;
/*      */   
/*  141 */   private int size = -1;
/*      */   
/*      */   private int alignType;
/*      */   
/*      */   private String encoding;
/*      */   private int actualAlignType;
/*      */   private int structAlignment;
/*      */   private Map<String, StructField> structFields;
/*  149 */   private final Map<String, Object> nativeStrings = new HashMap<String, Object>();
/*      */   
/*      */   private TypeMapper typeMapper;
/*      */   
/*      */   private long typeInfo;
/*      */   
/*      */   private boolean autoRead = true;
/*      */   private boolean autoWrite = true;
/*      */   private Structure[] array;
/*      */   private boolean readCalled;
/*      */   
/*      */   protected Structure() {
/*  161 */     this(0);
/*      */   }
/*      */   
/*      */   protected Structure(TypeMapper mapper) {
/*  165 */     this(null, 0, mapper);
/*      */   }
/*      */   
/*      */   protected Structure(int alignType) {
/*  169 */     this((Pointer)null, alignType);
/*      */   }
/*      */   
/*      */   protected Structure(int alignType, TypeMapper mapper) {
/*  173 */     this(null, alignType, mapper);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Structure(Pointer p) {
/*  178 */     this(p, 0);
/*      */   }
/*      */   
/*      */   protected Structure(Pointer p, int alignType) {
/*  182 */     this(p, alignType, null);
/*      */   }
/*      */   
/*      */   protected Structure(Pointer p, int alignType, TypeMapper mapper) {
/*  186 */     setAlignType(alignType);
/*  187 */     setStringEncoding(Native.getStringEncoding(getClass()));
/*  188 */     initializeTypeMapper(mapper);
/*  189 */     validateFields();
/*  190 */     if (p != null) {
/*  191 */       useMemory(p, 0, true);
/*      */     } else {
/*      */       
/*  194 */       allocateMemory(-1);
/*      */     } 
/*  196 */     initializeFields();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Map<String, StructField> fields() {
/*  207 */     return this.structFields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TypeMapper getTypeMapper() {
/*  214 */     return this.typeMapper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeTypeMapper(TypeMapper mapper) {
/*  224 */     if (mapper == null) {
/*  225 */       mapper = Native.getTypeMapper(getClass());
/*      */     }
/*  227 */     this.typeMapper = mapper;
/*  228 */     layoutChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void layoutChanged() {
/*  235 */     if (this.size != -1) {
/*  236 */       this.size = -1;
/*  237 */       if (this.memory instanceof AutoAllocated) {
/*  238 */         this.memory = null;
/*      */       }
/*      */       
/*  241 */       ensureAllocated();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setStringEncoding(String encoding) {
/*  250 */     this.encoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getStringEncoding() {
/*  258 */     return this.encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setAlignType(int alignType) {
/*  267 */     this.alignType = alignType;
/*  268 */     if (alignType == 0) {
/*  269 */       alignType = Native.getStructureAlignment(getClass());
/*  270 */       if (alignType == 0)
/*  271 */         if (Platform.isWindows()) {
/*  272 */           alignType = 3;
/*      */         } else {
/*  274 */           alignType = 2;
/*      */         }  
/*      */     } 
/*  277 */     this.actualAlignType = alignType;
/*  278 */     layoutChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Memory autoAllocate(int size) {
/*  287 */     return new AutoAllocated(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void useMemory(Pointer m) {
/*  297 */     useMemory(m, 0);
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
/*      */   protected void useMemory(Pointer m, int offset) {
/*  309 */     useMemory(m, offset, false);
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
/*      */   void useMemory(Pointer m, int offset, boolean force) {
/*      */     try {
/*  325 */       this.nativeStrings.clear();
/*      */       
/*  327 */       if (this instanceof ByValue && !force) {
/*      */ 
/*      */         
/*  330 */         byte[] buf = new byte[size()];
/*  331 */         m.read(0L, buf, 0, buf.length);
/*  332 */         this.memory.write(0L, buf, 0, buf.length);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  337 */         this.memory = m.share(offset);
/*  338 */         if (this.size == -1) {
/*  339 */           this.size = calculateSize(false);
/*      */         }
/*  341 */         if (this.size != -1) {
/*  342 */           this.memory = m.share(offset, this.size);
/*      */         }
/*      */       } 
/*  345 */       this.array = null;
/*  346 */       this.readCalled = false;
/*      */     }
/*  348 */     catch (IndexOutOfBoundsException e) {
/*  349 */       throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureAllocated() {
/*  356 */     ensureAllocated(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void ensureAllocated(boolean avoidFFIType) {
/*  365 */     if (this.memory == null) {
/*  366 */       allocateMemory(avoidFFIType);
/*      */     }
/*  368 */     else if (this.size == -1) {
/*  369 */       this.size = calculateSize(true, avoidFFIType);
/*  370 */       if (!(this.memory instanceof AutoAllocated)) {
/*      */         
/*      */         try {
/*  373 */           this.memory = this.memory.share(0L, this.size);
/*      */         }
/*  375 */         catch (IndexOutOfBoundsException e) {
/*  376 */           throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void allocateMemory() {
/*  386 */     allocateMemory(false);
/*      */   }
/*      */   
/*      */   private void allocateMemory(boolean avoidFFIType) {
/*  390 */     allocateMemory(calculateSize(true, avoidFFIType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void allocateMemory(int size) {
/*  401 */     if (size == -1) {
/*      */       
/*  403 */       size = calculateSize(false);
/*      */     }
/*  405 */     else if (size <= 0) {
/*  406 */       throw new IllegalArgumentException("Structure size must be greater than zero: " + size);
/*      */     } 
/*      */ 
/*      */     
/*  410 */     if (size != -1) {
/*  411 */       if (this.memory == null || this.memory instanceof AutoAllocated)
/*      */       {
/*  413 */         this.memory = autoAllocate(size);
/*      */       }
/*  415 */       this.size = size;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  423 */     ensureAllocated();
/*  424 */     return this.size;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  429 */     ensureAllocated();
/*  430 */     this.memory.clear(size());
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
/*      */   public Pointer getPointer() {
/*  444 */     ensureAllocated();
/*  445 */     return this.memory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  454 */   private static final ThreadLocal<Map<Pointer, Structure>> reads = new ThreadLocal<Map<Pointer, Structure>>()
/*      */     {
/*      */       protected synchronized Map<Pointer, Structure> initialValue() {
/*  457 */         return new HashMap<Pointer, Structure>();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*  463 */   private static final ThreadLocal<Set<Structure>> busy = new ThreadLocal<Set<Structure>>()
/*      */     {
/*      */       protected synchronized Set<Structure> initialValue() {
/*  466 */         return new Structure.StructureSet();
/*      */       }
/*      */     };
/*      */   
/*      */   static class StructureSet
/*      */     extends AbstractCollection<Structure>
/*      */     implements Set<Structure> {
/*      */     Structure[] elements;
/*      */     private int count;
/*      */     
/*      */     private void ensureCapacity(int size) {
/*  477 */       if (this.elements == null) {
/*  478 */         this.elements = new Structure[size * 3 / 2];
/*      */       }
/*  480 */       else if (this.elements.length < size) {
/*  481 */         Structure[] e = new Structure[size * 3 / 2];
/*  482 */         System.arraycopy(this.elements, 0, e, 0, this.elements.length);
/*  483 */         this.elements = e;
/*      */       } 
/*      */     }
/*      */     public Structure[] getElements() {
/*  487 */       return this.elements;
/*      */     }
/*      */     public int size() {
/*  490 */       return this.count;
/*      */     }
/*      */     public boolean contains(Object o) {
/*  493 */       return (indexOf((Structure)o) != -1);
/*      */     }
/*      */     
/*      */     public boolean add(Structure o) {
/*  497 */       if (!contains(o)) {
/*  498 */         ensureCapacity(this.count + 1);
/*  499 */         this.elements[this.count++] = o;
/*      */       } 
/*  501 */       return true;
/*      */     }
/*      */     private int indexOf(Structure s1) {
/*  504 */       for (int i = 0; i < this.count; i++) {
/*  505 */         Structure s2 = this.elements[i];
/*  506 */         if (s1 == s2 || (s1
/*  507 */           .getClass() == s2.getClass() && s1
/*  508 */           .size() == s2.size() && s1
/*  509 */           .getPointer().equals(s2.getPointer()))) {
/*  510 */           return i;
/*      */         }
/*      */       } 
/*  513 */       return -1;
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  517 */       int idx = indexOf((Structure)o);
/*  518 */       if (idx != -1) {
/*  519 */         if (--this.count >= 0) {
/*  520 */           this.elements[idx] = this.elements[this.count];
/*  521 */           this.elements[this.count] = null;
/*      */         } 
/*  523 */         return true;
/*      */       } 
/*  525 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Structure> iterator() {
/*  532 */       Structure[] e = new Structure[this.count];
/*  533 */       if (this.count > 0) {
/*  534 */         System.arraycopy(this.elements, 0, e, 0, this.count);
/*      */       }
/*  536 */       return Arrays.<Structure>asList(e).iterator();
/*      */     }
/*      */   }
/*      */   
/*      */   static Set<Structure> busy() {
/*  541 */     return busy.get();
/*      */   }
/*      */   static Map<Pointer, Structure> reading() {
/*  544 */     return reads.get();
/*      */   }
/*      */ 
/*      */   
/*      */   void conditionalAutoRead() {
/*  549 */     if (!this.readCalled) {
/*  550 */       autoRead();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void read() {
/*  559 */     if (this.memory == PLACEHOLDER_MEMORY) {
/*      */       return;
/*      */     }
/*  562 */     this.readCalled = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  568 */     ensureAllocated();
/*      */ 
/*      */     
/*  571 */     if (busy().contains(this)) {
/*      */       return;
/*      */     }
/*  574 */     busy().add(this);
/*  575 */     if (this instanceof ByReference) {
/*  576 */       reading().put(getPointer(), this);
/*      */     }
/*      */     try {
/*  579 */       for (StructField structField : fields().values()) {
/*  580 */         readField(structField);
/*      */       }
/*      */     } finally {
/*      */       
/*  584 */       busy().remove(this);
/*  585 */       if (reading().get(getPointer()) == this) {
/*  586 */         reading().remove(getPointer());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int fieldOffset(String name) {
/*  596 */     ensureAllocated();
/*  597 */     StructField f = fields().get(name);
/*  598 */     if (f == null)
/*  599 */       throw new IllegalArgumentException("No such field: " + name); 
/*  600 */     return f.offset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object readField(String name) {
/*  610 */     ensureAllocated();
/*  611 */     StructField f = fields().get(name);
/*  612 */     if (f == null)
/*  613 */       throw new IllegalArgumentException("No such field: " + name); 
/*  614 */     return readField(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object getFieldValue(Field field) {
/*      */     try {
/*  624 */       return field.get(this);
/*      */     }
/*  626 */     catch (Exception e) {
/*  627 */       throw new Error("Exception reading field '" + field.getName() + "' in " + getClass(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setFieldValue(Field field, Object value) {
/*  636 */     setFieldValue(field, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setFieldValue(Field field, Object value, boolean overrideFinal) {
/*      */     try {
/*  642 */       field.set(this, value);
/*      */     }
/*  644 */     catch (IllegalAccessException e) {
/*  645 */       int modifiers = field.getModifiers();
/*  646 */       if (Modifier.isFinal(modifiers)) {
/*  647 */         if (overrideFinal)
/*      */         {
/*      */           
/*  650 */           throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + field.getName() + "' within " + getClass() + ")", e);
/*      */         }
/*  652 */         throw new UnsupportedOperationException("Attempt to write to read-only field '" + field.getName() + "' within " + getClass(), e);
/*      */       } 
/*  654 */       throw new Error("Unexpectedly unable to write to field '" + field.getName() + "' within " + getClass(), e);
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
/*      */   static Structure updateStructureByReference(Class<?> type, Structure s, Pointer address) {
/*  666 */     if (address == null) {
/*  667 */       s = null;
/*      */     
/*      */     }
/*  670 */     else if (s == null || !address.equals(s.getPointer())) {
/*  671 */       Structure s1 = reading().get(address);
/*  672 */       if (s1 != null && type.equals(s1.getClass())) {
/*  673 */         s = s1;
/*  674 */         s.autoRead();
/*      */       } else {
/*      */         
/*  677 */         s = newInstance(type, address);
/*  678 */         s.conditionalAutoRead();
/*      */       } 
/*      */     } else {
/*      */       
/*  682 */       s.autoRead();
/*      */     } 
/*      */     
/*  685 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object readField(StructField structField) {
/*      */     Object result;
/*  697 */     int offset = structField.offset;
/*      */ 
/*      */     
/*  700 */     Class<?> fieldType = structField.type;
/*  701 */     FromNativeConverter readConverter = structField.readConverter;
/*  702 */     if (readConverter != null) {
/*  703 */       fieldType = readConverter.nativeType();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  712 */     Object currentValue = (Structure.class.isAssignableFrom(fieldType) || Callback.class.isAssignableFrom(fieldType) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(fieldType)) || Pointer.class.isAssignableFrom(fieldType) || NativeMapped.class.isAssignableFrom(fieldType) || fieldType.isArray()) ? getFieldValue(structField.field) : null;
/*      */ 
/*      */     
/*  715 */     if (fieldType == String.class) {
/*  716 */       Pointer p = this.memory.getPointer(offset);
/*  717 */       result = (p == null) ? null : p.getString(0L, this.encoding);
/*      */     } else {
/*      */       
/*  720 */       result = this.memory.getValue(offset, fieldType, currentValue);
/*      */     } 
/*  722 */     if (readConverter != null) {
/*  723 */       result = readConverter.fromNative(result, structField.context);
/*  724 */       if (currentValue != null && currentValue.equals(result)) {
/*  725 */         result = currentValue;
/*      */       }
/*      */     } 
/*      */     
/*  729 */     if (fieldType.equals(String.class) || fieldType
/*  730 */       .equals(WString.class)) {
/*  731 */       this.nativeStrings.put(structField.name + ".ptr", this.memory.getPointer(offset));
/*  732 */       this.nativeStrings.put(structField.name + ".val", result);
/*      */     } 
/*      */ 
/*      */     
/*  736 */     setFieldValue(structField.field, result, true);
/*  737 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write() {
/*  745 */     if (this.memory == PLACEHOLDER_MEMORY) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  752 */     ensureAllocated();
/*      */ 
/*      */     
/*  755 */     if (this instanceof ByValue) {
/*  756 */       getTypeInfo();
/*      */     }
/*      */ 
/*      */     
/*  760 */     if (busy().contains(this)) {
/*      */       return;
/*      */     }
/*  763 */     busy().add(this);
/*      */     
/*      */     try {
/*  766 */       for (StructField sf : fields().values()) {
/*  767 */         if (!sf.isVolatile) {
/*  768 */           writeField(sf);
/*      */         }
/*      */       } 
/*      */     } finally {
/*      */       
/*  773 */       busy().remove(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(String name) {
/*  783 */     ensureAllocated();
/*  784 */     StructField f = fields().get(name);
/*  785 */     if (f == null)
/*  786 */       throw new IllegalArgumentException("No such field: " + name); 
/*  787 */     writeField(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(String name, Object value) {
/*  798 */     ensureAllocated();
/*  799 */     StructField structField = fields().get(name);
/*  800 */     if (structField == null)
/*  801 */       throw new IllegalArgumentException("No such field: " + name); 
/*  802 */     setFieldValue(structField.field, value);
/*  803 */     writeField(structField);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeField(StructField structField) {
/*  811 */     if (structField.isReadOnly) {
/*      */       return;
/*      */     }
/*      */     
/*  815 */     int offset = structField.offset;
/*      */ 
/*      */     
/*  818 */     Object value = getFieldValue(structField.field);
/*      */ 
/*      */     
/*  821 */     Class<?> fieldType = structField.type;
/*  822 */     ToNativeConverter converter = structField.writeConverter;
/*  823 */     if (converter != null) {
/*  824 */       value = converter.toNative(value, new StructureWriteContext(this, structField.field));
/*  825 */       fieldType = converter.nativeType();
/*      */     } 
/*      */ 
/*      */     
/*  829 */     if (String.class == fieldType || WString.class == fieldType) {
/*      */ 
/*      */       
/*  832 */       boolean wide = (fieldType == WString.class);
/*  833 */       if (value != null) {
/*      */ 
/*      */         
/*  836 */         if (this.nativeStrings.containsKey(structField.name + ".ptr") && value
/*  837 */           .equals(this.nativeStrings.get(structField.name + ".val"))) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  842 */         NativeString nativeString = wide ? new NativeString(value.toString(), true) : new NativeString(value.toString(), this.encoding);
/*      */ 
/*      */         
/*  845 */         this.nativeStrings.put(structField.name, nativeString);
/*  846 */         value = nativeString.getPointer();
/*      */       } else {
/*      */         
/*  849 */         this.nativeStrings.remove(structField.name);
/*      */       } 
/*  851 */       this.nativeStrings.remove(structField.name + ".ptr");
/*  852 */       this.nativeStrings.remove(structField.name + ".val");
/*      */     } 
/*      */     
/*      */     try {
/*  856 */       this.memory.setValue(offset, value, fieldType);
/*      */     }
/*  858 */     catch (IllegalArgumentException e) {
/*  859 */       String msg = "Structure field \"" + structField.name + "\" was declared as " + structField.type + ((structField.type == fieldType) ? "" : (" (native type " + fieldType + ")")) + ", which is not supported within a Structure";
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  864 */       throw new IllegalArgumentException(msg, e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   protected final void setFieldOrder(String[] fields) {
/*  901 */     throw new Error("This method is obsolete, use getFieldOrder() instead");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sortFields(List<Field> fields, List<String> names) {
/*  909 */     for (int i = 0; i < names.size(); i++) {
/*  910 */       String name = names.get(i);
/*  911 */       for (int f = 0; f < fields.size(); f++) {
/*  912 */         Field field = fields.get(f);
/*  913 */         if (name.equals(field.getName())) {
/*  914 */           Collections.swap(fields, i, f);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Field> getFieldList() {
/*  926 */     List<Field> flist = new ArrayList<Field>();
/*  927 */     Class<?> cls = getClass();
/*  928 */     for (; !cls.equals(Structure.class); 
/*  929 */       cls = cls.getSuperclass()) {
/*  930 */       List<Field> classFields = new ArrayList<Field>();
/*  931 */       Field[] fields = cls.getDeclaredFields();
/*  932 */       for (int i = 0; i < fields.length; i++) {
/*  933 */         int modifiers = fields[i].getModifiers();
/*  934 */         if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
/*      */         {
/*      */           
/*  937 */           classFields.add(fields[i]); } 
/*      */       } 
/*  939 */       flist.addAll(0, classFields);
/*      */     } 
/*  941 */     return flist;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<String> fieldOrder() {
/*  948 */     Class<?> clazz = getClass();
/*  949 */     synchronized (fieldOrder) {
/*  950 */       List<String> list = fieldOrder.get(clazz);
/*  951 */       if (list == null) {
/*  952 */         list = getFieldOrder();
/*  953 */         fieldOrder.put(clazz, list);
/*      */       } 
/*  955 */       return list;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static List<String> createFieldsOrder(List<String> baseFields, String... extraFields) {
/*  960 */     return createFieldsOrder(baseFields, Arrays.asList(extraFields));
/*      */   }
/*      */   
/*      */   public static List<String> createFieldsOrder(List<String> baseFields, List<String> extraFields) {
/*  964 */     List<String> fields = new ArrayList<String>(baseFields.size() + extraFields.size());
/*  965 */     fields.addAll(baseFields);
/*  966 */     fields.addAll(extraFields);
/*  967 */     return Collections.unmodifiableList(fields);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> createFieldsOrder(String field) {
/*  975 */     return Collections.unmodifiableList(Collections.singletonList(field));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> createFieldsOrder(String... fields) {
/*  983 */     return Collections.unmodifiableList(Arrays.asList(fields));
/*      */   }
/*      */   
/*      */   private static <T extends Comparable<T>> List<T> sort(Collection<? extends T> c) {
/*  987 */     List<T> list = new ArrayList<T>(c);
/*  988 */     Collections.sort(list);
/*  989 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Field> getFields(boolean force) {
/* 1000 */     List<Field> flist = getFieldList();
/* 1001 */     Set<String> names = new HashSet<String>();
/* 1002 */     for (Field f : flist) {
/* 1003 */       names.add(f.getName());
/*      */     }
/*      */     
/* 1006 */     List<String> fieldOrder = fieldOrder();
/* 1007 */     if (fieldOrder.size() != flist.size() && flist.size() > 1) {
/* 1008 */       if (force) {
/* 1009 */         throw new Error("Structure.getFieldOrder() on " + getClass() + " does not provide enough names [" + fieldOrder
/* 1010 */             .size() + "] (" + 
/*      */             
/* 1012 */             sort(fieldOrder) + ") to match declared fields [" + flist
/* 1013 */             .size() + "] (" + 
/*      */             
/* 1015 */             sort(names) + ")");
/*      */       }
/*      */       
/* 1018 */       return null;
/*      */     } 
/*      */     
/* 1021 */     Set<String> orderedNames = new HashSet<String>(fieldOrder);
/* 1022 */     if (!orderedNames.equals(names)) {
/* 1023 */       throw new Error("Structure.getFieldOrder() on " + getClass() + " returns names (" + 
/*      */           
/* 1025 */           sort(fieldOrder) + ") which do not match declared field names (" + 
/*      */           
/* 1027 */           sort(names) + ")");
/*      */     }
/*      */     
/* 1030 */     sortFields(flist, fieldOrder);
/* 1031 */     return flist;
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
/*      */   protected int calculateSize(boolean force) {
/* 1049 */     return calculateSize(force, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int size(Class<?> type) {
/* 1057 */     return size(type, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int size(Class<?> type, Structure value) {
/*      */     LayoutInfo info;
/* 1067 */     synchronized (layoutInfo) {
/* 1068 */       info = layoutInfo.get(type);
/*      */     } 
/* 1070 */     int sz = (info != null && !info.variable) ? info.size : -1;
/* 1071 */     if (sz == -1) {
/* 1072 */       if (value == null) {
/* 1073 */         value = newInstance(type, PLACEHOLDER_MEMORY);
/*      */       }
/* 1075 */       sz = value.size();
/*      */     } 
/* 1077 */     return sz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int calculateSize(boolean force, boolean avoidFFIType) {
/*      */     LayoutInfo info;
/* 1088 */     int size = -1;
/* 1089 */     Class<?> clazz = getClass();
/*      */     
/* 1091 */     synchronized (layoutInfo) {
/* 1092 */       info = layoutInfo.get(clazz);
/*      */     } 
/* 1094 */     if (info == null || this.alignType != info
/* 1095 */       .alignType || this.typeMapper != info
/* 1096 */       .typeMapper) {
/* 1097 */       info = deriveLayout(force, avoidFFIType);
/*      */     }
/* 1099 */     if (info != null) {
/* 1100 */       this.structAlignment = info.alignment;
/* 1101 */       this.structFields = info.fields;
/*      */       
/* 1103 */       if (!info.variable) {
/* 1104 */         synchronized (layoutInfo) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1110 */           if (!layoutInfo.containsKey(clazz) || this.alignType != 0 || this.typeMapper != null)
/*      */           {
/*      */             
/* 1113 */             layoutInfo.put(clazz, info);
/*      */           }
/*      */         } 
/*      */       }
/* 1117 */       size = info.size;
/*      */     } 
/* 1119 */     return size;
/*      */   }
/*      */   
/*      */   private static class LayoutInfo
/*      */   {
/*      */     private LayoutInfo() {}
/*      */     
/* 1126 */     private int size = -1;
/* 1127 */     private int alignment = 1;
/* 1128 */     private final Map<String, Structure.StructField> fields = Collections.synchronizedMap(new LinkedHashMap<String, Structure.StructField>());
/* 1129 */     private int alignType = 0;
/*      */     
/*      */     private TypeMapper typeMapper;
/*      */     private boolean variable;
/*      */     private Structure.StructField typeInfoField;
/*      */   }
/*      */   
/*      */   private void validateField(String name, Class<?> type) {
/* 1137 */     if (this.typeMapper != null) {
/* 1138 */       ToNativeConverter toNative = this.typeMapper.getToNativeConverter(type);
/* 1139 */       if (toNative != null) {
/* 1140 */         validateField(name, toNative.nativeType());
/*      */         return;
/*      */       } 
/*      */     } 
/* 1144 */     if (type.isArray()) {
/* 1145 */       validateField(name, type.getComponentType());
/*      */     } else {
/*      */       
/*      */       try {
/* 1149 */         getNativeSize(type);
/*      */       }
/* 1151 */       catch (IllegalArgumentException e) {
/* 1152 */         String msg = "Invalid Structure field in " + getClass() + ", field name '" + name + "' (" + type + "): " + e.getMessage();
/* 1153 */         throw new IllegalArgumentException(msg, e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void validateFields() {
/* 1160 */     List<Field> fields = getFieldList();
/* 1161 */     for (Field f : fields) {
/* 1162 */       validateField(f.getName(), f.getType());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LayoutInfo deriveLayout(boolean force, boolean avoidFFIType) {
/* 1171 */     int calculatedSize = 0;
/* 1172 */     List<Field> fields = getFields(force);
/* 1173 */     if (fields == null) {
/* 1174 */       return null;
/*      */     }
/*      */     
/* 1177 */     LayoutInfo info = new LayoutInfo();
/* 1178 */     info.alignType = this.alignType;
/* 1179 */     info.typeMapper = this.typeMapper;
/*      */     
/* 1181 */     boolean firstField = true;
/* 1182 */     for (Iterator<Field> i = fields.iterator(); i.hasNext(); firstField = false) {
/* 1183 */       Field field = i.next();
/* 1184 */       int modifiers = field.getModifiers();
/*      */       
/* 1186 */       Class<?> type = field.getType();
/* 1187 */       if (type.isArray()) {
/* 1188 */         info.variable = true;
/*      */       }
/* 1190 */       StructField structField = new StructField();
/* 1191 */       structField.isVolatile = Modifier.isVolatile(modifiers);
/* 1192 */       structField.isReadOnly = Modifier.isFinal(modifiers);
/* 1193 */       if (structField.isReadOnly) {
/* 1194 */         if (!Platform.RO_FIELDS) {
/* 1195 */           throw new IllegalArgumentException("This VM does not support read-only fields (field '" + field
/* 1196 */               .getName() + "' within " + getClass() + ")");
/*      */         }
/*      */ 
/*      */         
/* 1200 */         field.setAccessible(true);
/*      */       } 
/* 1202 */       structField.field = field;
/* 1203 */       structField.name = field.getName();
/* 1204 */       structField.type = type;
/*      */ 
/*      */       
/* 1207 */       if (Callback.class.isAssignableFrom(type) && !type.isInterface()) {
/* 1208 */         throw new IllegalArgumentException("Structure Callback field '" + field
/* 1209 */             .getName() + "' must be an interface");
/*      */       }
/*      */       
/* 1212 */       if (type.isArray() && Structure.class
/* 1213 */         .equals(type.getComponentType())) {
/* 1214 */         String msg = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
/*      */ 
/*      */         
/* 1217 */         throw new IllegalArgumentException(msg);
/*      */       } 
/*      */       
/* 1220 */       int fieldAlignment = 1;
/* 1221 */       if (Modifier.isPublic(field.getModifiers())) {
/*      */ 
/*      */ 
/*      */         
/* 1225 */         Object value = getFieldValue(structField.field);
/* 1226 */         if (value == null && type.isArray()) {
/* 1227 */           if (force) {
/* 1228 */             throw new IllegalStateException("Array fields must be initialized");
/*      */           }
/*      */           
/* 1231 */           return null;
/*      */         } 
/* 1233 */         Class<?> nativeType = type;
/* 1234 */         if (NativeMapped.class.isAssignableFrom(type)) {
/* 1235 */           NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1236 */           nativeType = tc.nativeType();
/* 1237 */           structField.writeConverter = tc;
/* 1238 */           structField.readConverter = tc;
/* 1239 */           structField.context = new StructureReadContext(this, field);
/*      */         }
/* 1241 */         else if (this.typeMapper != null) {
/* 1242 */           ToNativeConverter writeConverter = this.typeMapper.getToNativeConverter(type);
/* 1243 */           FromNativeConverter readConverter = this.typeMapper.getFromNativeConverter(type);
/* 1244 */           if (writeConverter != null && readConverter != null) {
/* 1245 */             value = writeConverter.toNative(value, new StructureWriteContext(this, structField.field));
/*      */             
/* 1247 */             nativeType = (value != null) ? value.getClass() : Pointer.class;
/* 1248 */             structField.writeConverter = writeConverter;
/* 1249 */             structField.readConverter = readConverter;
/* 1250 */             structField.context = new StructureReadContext(this, field);
/*      */           }
/* 1252 */           else if (writeConverter != null || readConverter != null) {
/* 1253 */             String msg = "Structures require bidirectional type conversion for " + type;
/* 1254 */             throw new IllegalArgumentException(msg);
/*      */           } 
/*      */         } 
/*      */         
/* 1258 */         if (value == null) {
/* 1259 */           value = initializeField(structField.field, type);
/*      */         }
/*      */         
/*      */         try {
/* 1263 */           structField.size = getNativeSize(nativeType, value);
/* 1264 */           fieldAlignment = getNativeAlignment(nativeType, value, firstField);
/*      */         }
/* 1266 */         catch (IllegalArgumentException e) {
/*      */           
/* 1268 */           if (!force && this.typeMapper == null) {
/* 1269 */             return null;
/*      */           }
/* 1271 */           String msg = "Invalid Structure field in " + getClass() + ", field name '" + structField.name + "' (" + structField.type + "): " + e.getMessage();
/* 1272 */           throw new IllegalArgumentException(msg, e);
/*      */         } 
/*      */ 
/*      */         
/* 1276 */         if (fieldAlignment == 0) {
/* 1277 */           throw new Error("Field alignment is zero for field '" + structField.name + "' within " + getClass());
/*      */         }
/* 1279 */         info.alignment = Math.max(info.alignment, fieldAlignment);
/* 1280 */         if (calculatedSize % fieldAlignment != 0) {
/* 1281 */           calculatedSize += fieldAlignment - calculatedSize % fieldAlignment;
/*      */         }
/* 1283 */         if (this instanceof Union) {
/* 1284 */           structField.offset = 0;
/* 1285 */           calculatedSize = Math.max(calculatedSize, structField.size);
/*      */         } else {
/*      */           
/* 1288 */           structField.offset = calculatedSize;
/* 1289 */           calculatedSize += structField.size;
/*      */         } 
/*      */ 
/*      */         
/* 1293 */         info.fields.put(structField.name, structField);
/*      */         
/* 1295 */         if (info.typeInfoField == null || 
/* 1296 */           info.typeInfoField.size < structField.size || (
/* 1297 */           info.typeInfoField.size == structField.size && Structure.class
/* 1298 */           .isAssignableFrom(structField.type))) {
/* 1299 */           info.typeInfoField = structField;
/*      */         }
/*      */       } 
/*      */     } 
/* 1303 */     if (calculatedSize > 0) {
/* 1304 */       int size = addPadding(calculatedSize, info.alignment);
/*      */       
/* 1306 */       if (this instanceof ByValue && !avoidFFIType) {
/* 1307 */         getTypeInfo();
/*      */       }
/* 1309 */       info.size = size;
/* 1310 */       return info;
/*      */     } 
/*      */     
/* 1313 */     throw new IllegalArgumentException("Structure " + getClass() + " has unknown or zero size (ensure all fields are public)");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeFields() {
/* 1324 */     List<Field> flist = getFieldList();
/* 1325 */     for (Field f : flist) {
/*      */       try {
/* 1327 */         Object o = f.get(this);
/* 1328 */         if (o == null) {
/* 1329 */           initializeField(f, f.getType());
/*      */         }
/*      */       }
/* 1332 */       catch (Exception e) {
/* 1333 */         throw new Error("Exception reading field '" + f.getName() + "' in " + getClass(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private Object initializeField(Field field, Class<?> type) {
/* 1339 */     Object value = null;
/* 1340 */     if (Structure.class.isAssignableFrom(type) && 
/* 1341 */       !ByReference.class.isAssignableFrom(type)) {
/*      */       try {
/* 1343 */         value = newInstance(type, PLACEHOLDER_MEMORY);
/* 1344 */         setFieldValue(field, value);
/*      */       }
/* 1346 */       catch (IllegalArgumentException e) {
/* 1347 */         String msg = "Can't determine size of nested structure";
/* 1348 */         throw new IllegalArgumentException(msg, e);
/*      */       }
/*      */     
/* 1351 */     } else if (NativeMapped.class.isAssignableFrom(type)) {
/* 1352 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1353 */       value = tc.defaultValue();
/* 1354 */       setFieldValue(field, value);
/*      */     } 
/* 1356 */     return value;
/*      */   }
/*      */   
/*      */   private int addPadding(int calculatedSize) {
/* 1360 */     return addPadding(calculatedSize, this.structAlignment);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int addPadding(int calculatedSize, int alignment) {
/* 1366 */     if (this.actualAlignType != 1 && 
/* 1367 */       calculatedSize % alignment != 0) {
/* 1368 */       calculatedSize += alignment - calculatedSize % alignment;
/*      */     }
/*      */     
/* 1371 */     return calculatedSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getStructAlignment() {
/* 1378 */     if (this.size == -1)
/*      */     {
/* 1380 */       calculateSize(true);
/*      */     }
/* 1382 */     return this.structAlignment;
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
/*      */   protected int getNativeAlignment(Class<?> type, Object value, boolean isFirstElement) {
/* 1396 */     int alignment = 1;
/* 1397 */     if (NativeMapped.class.isAssignableFrom(type)) {
/* 1398 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1399 */       type = tc.nativeType();
/* 1400 */       value = tc.toNative(value, new ToNativeContext());
/*      */     } 
/* 1402 */     int size = Native.getNativeSize(type, value);
/* 1403 */     if (type.isPrimitive() || Long.class == type || Integer.class == type || Short.class == type || Character.class == type || Byte.class == type || Boolean.class == type || Float.class == type || Double.class == type) {
/*      */ 
/*      */ 
/*      */       
/* 1407 */       alignment = size;
/*      */     }
/* 1409 */     else if ((Pointer.class.isAssignableFrom(type) && !Function.class.isAssignableFrom(type)) || (Platform.HAS_BUFFERS && Buffer.class
/* 1410 */       .isAssignableFrom(type)) || Callback.class
/* 1411 */       .isAssignableFrom(type) || WString.class == type || String.class == type) {
/*      */ 
/*      */       
/* 1414 */       alignment = Pointer.SIZE;
/*      */     }
/* 1416 */     else if (Structure.class.isAssignableFrom(type)) {
/* 1417 */       if (ByReference.class.isAssignableFrom(type)) {
/* 1418 */         alignment = Pointer.SIZE;
/*      */       } else {
/*      */         
/* 1421 */         if (value == null)
/* 1422 */           value = newInstance(type, PLACEHOLDER_MEMORY); 
/* 1423 */         alignment = ((Structure)value).getStructAlignment();
/*      */       }
/*      */     
/* 1426 */     } else if (type.isArray()) {
/* 1427 */       alignment = getNativeAlignment(type.getComponentType(), null, isFirstElement);
/*      */     } else {
/*      */       
/* 1430 */       throw new IllegalArgumentException("Type " + type + " has unknown native alignment");
/*      */     } 
/*      */     
/* 1433 */     if (this.actualAlignType == 1) {
/* 1434 */       alignment = 1;
/*      */     }
/* 1436 */     else if (this.actualAlignType == 3) {
/* 1437 */       alignment = Math.min(8, alignment);
/*      */     }
/* 1439 */     else if (this.actualAlignType == 2) {
/*      */ 
/*      */       
/* 1442 */       if (!isFirstElement || !Platform.isMac() || !Platform.isPPC()) {
/* 1443 */         alignment = Math.min(Native.MAX_ALIGNMENT, alignment);
/*      */       }
/* 1445 */       if (!isFirstElement && Platform.isAIX() && (type == double.class || type == Double.class)) {
/* 1446 */         alignment = 4;
/*      */       }
/*      */     } 
/* 1449 */     return alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1459 */     return toString(Boolean.getBoolean("jna.dump_memory"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(boolean debug) {
/* 1468 */     return toString(0, true, debug);
/*      */   }
/*      */   
/*      */   private String format(Class<?> type) {
/* 1472 */     String s = type.getName();
/* 1473 */     int dot = s.lastIndexOf(".");
/* 1474 */     return s.substring(dot + 1);
/*      */   }
/*      */   
/*      */   private String toString(int indent, boolean showContents, boolean dumpMemory) {
/* 1478 */     ensureAllocated();
/* 1479 */     String LS = System.getProperty("line.separator");
/* 1480 */     String name = format(getClass()) + "(" + getPointer() + ")";
/* 1481 */     if (!(getPointer() instanceof Memory)) {
/* 1482 */       name = name + " (" + size() + " bytes)";
/*      */     }
/* 1484 */     String prefix = "";
/* 1485 */     for (int idx = 0; idx < indent; idx++) {
/* 1486 */       prefix = prefix + "  ";
/*      */     }
/* 1488 */     String contents = LS;
/* 1489 */     if (!showContents) {
/* 1490 */       contents = "...}";
/*      */     } else {
/* 1492 */       for (Iterator<StructField> i = fields().values().iterator(); i.hasNext(); ) {
/* 1493 */         StructField sf = i.next();
/* 1494 */         Object value = getFieldValue(sf.field);
/* 1495 */         String type = format(sf.type);
/* 1496 */         String index = "";
/* 1497 */         contents = contents + prefix;
/* 1498 */         if (sf.type.isArray() && value != null) {
/* 1499 */           type = format(sf.type.getComponentType());
/* 1500 */           index = "[" + Array.getLength(value) + "]";
/*      */         } 
/* 1502 */         contents = contents + String.format("  %s %s%s@0x%X", new Object[] { type, sf.name, index, Integer.valueOf(sf.offset) });
/* 1503 */         if (value instanceof Structure) {
/* 1504 */           value = ((Structure)value).toString(indent + 1, !(value instanceof ByReference), dumpMemory);
/*      */         }
/* 1506 */         contents = contents + "=";
/* 1507 */         if (value instanceof Long) {
/* 1508 */           contents = contents + String.format("0x%08X", new Object[] { value });
/*      */         }
/* 1510 */         else if (value instanceof Integer) {
/* 1511 */           contents = contents + String.format("0x%04X", new Object[] { value });
/*      */         }
/* 1513 */         else if (value instanceof Short) {
/* 1514 */           contents = contents + String.format("0x%02X", new Object[] { value });
/*      */         }
/* 1516 */         else if (value instanceof Byte) {
/* 1517 */           contents = contents + String.format("0x%01X", new Object[] { value });
/*      */         } else {
/*      */           
/* 1520 */           contents = contents + String.valueOf(value).trim();
/*      */         } 
/* 1522 */         contents = contents + LS;
/* 1523 */         if (!i.hasNext())
/* 1524 */           contents = contents + prefix + "}"; 
/*      */       } 
/* 1526 */     }  if (indent == 0 && dumpMemory) {
/* 1527 */       int BYTES_PER_ROW = 4;
/* 1528 */       contents = contents + LS + "memory dump" + LS;
/* 1529 */       byte[] buf = getPointer().getByteArray(0L, size());
/* 1530 */       for (int i = 0; i < buf.length; i++) {
/* 1531 */         if (i % 4 == 0) contents = contents + "["; 
/* 1532 */         if (buf[i] >= 0 && buf[i] < 16)
/* 1533 */           contents = contents + "0"; 
/* 1534 */         contents = contents + Integer.toHexString(buf[i] & 0xFF);
/* 1535 */         if (i % 4 == 3 && i < buf.length - 1)
/* 1536 */           contents = contents + "]" + LS; 
/*      */       } 
/* 1538 */       contents = contents + "]";
/*      */     } 
/* 1540 */     return name + " {" + contents;
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
/*      */   public Structure[] toArray(Structure[] array) {
/* 1552 */     ensureAllocated();
/* 1553 */     if (this.memory instanceof AutoAllocated) {
/*      */       
/* 1555 */       Memory m = (Memory)this.memory;
/* 1556 */       int requiredSize = array.length * size();
/* 1557 */       if (m.size() < requiredSize) {
/* 1558 */         useMemory(autoAllocate(requiredSize));
/*      */       }
/*      */     } 
/*      */     
/* 1562 */     array[0] = this;
/* 1563 */     int size = size();
/* 1564 */     for (int i = 1; i < array.length; i++) {
/* 1565 */       array[i] = newInstance(getClass(), this.memory.share((i * size), size));
/* 1566 */       array[i].conditionalAutoRead();
/*      */     } 
/*      */     
/* 1569 */     if (!(this instanceof ByValue))
/*      */     {
/* 1571 */       this.array = array;
/*      */     }
/*      */     
/* 1574 */     return array;
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
/*      */   public Structure[] toArray(int size) {
/* 1587 */     return toArray((Structure[])Array.newInstance(getClass(), size));
/*      */   }
/*      */   
/*      */   private Class<?> baseClass() {
/* 1591 */     if ((this instanceof ByReference || this instanceof ByValue) && Structure.class
/*      */       
/* 1593 */       .isAssignableFrom(getClass().getSuperclass())) {
/* 1594 */       return getClass().getSuperclass();
/*      */     }
/* 1596 */     return getClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean dataEquals(Structure s) {
/* 1605 */     return dataEquals(s, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean dataEquals(Structure s, boolean clear) {
/* 1615 */     if (clear) {
/* 1616 */       s.getPointer().clear(s.size());
/* 1617 */       s.write();
/* 1618 */       getPointer().clear(size());
/* 1619 */       write();
/*      */     } 
/* 1621 */     byte[] data = s.getPointer().getByteArray(0L, s.size());
/* 1622 */     byte[] ref = getPointer().getByteArray(0L, size());
/* 1623 */     if (data.length == ref.length) {
/* 1624 */       for (int i = 0; i < data.length; i++) {
/* 1625 */         if (data[i] != ref[i]) {
/* 1626 */           return false;
/*      */         }
/*      */       } 
/* 1629 */       return true;
/*      */     } 
/* 1631 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/* 1639 */     return (o instanceof Structure && o
/* 1640 */       .getClass() == getClass() && ((Structure)o)
/* 1641 */       .getPointer().equals(getPointer()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1649 */     Pointer p = getPointer();
/* 1650 */     if (p != null) {
/* 1651 */       return getPointer().hashCode();
/*      */     }
/* 1653 */     return getClass().hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cacheTypeInfo(Pointer p) {
/* 1660 */     this.typeInfo = p.peer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Pointer getFieldTypeInfo(StructField f) {
/* 1668 */     Class<?> type = f.type;
/* 1669 */     Object value = getFieldValue(f.field);
/* 1670 */     if (this.typeMapper != null) {
/* 1671 */       ToNativeConverter nc = this.typeMapper.getToNativeConverter(type);
/* 1672 */       if (nc != null) {
/* 1673 */         type = nc.nativeType();
/* 1674 */         value = nc.toNative(value, new ToNativeContext());
/*      */       } 
/*      */     } 
/* 1677 */     return FFIType.get(value, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Pointer getTypeInfo() {
/* 1684 */     Pointer p = getTypeInfo(this);
/* 1685 */     cacheTypeInfo(p);
/* 1686 */     return p;
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
/*      */   public void setAutoSynch(boolean auto) {
/* 1710 */     setAutoRead(auto);
/* 1711 */     setAutoWrite(auto);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoRead(boolean auto) {
/* 1719 */     this.autoRead = auto;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoRead() {
/* 1727 */     return this.autoRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoWrite(boolean auto) {
/* 1735 */     this.autoWrite = auto;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoWrite() {
/* 1743 */     return this.autoWrite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Pointer getTypeInfo(Object obj) {
/* 1751 */     return FFIType.get(obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Structure newInstance(Class<?> type, long init) {
/*      */     try {
/* 1760 */       Structure s = newInstance(type, (init == 0L) ? PLACEHOLDER_MEMORY : new Pointer(init));
/* 1761 */       if (init != 0L) {
/* 1762 */         s.conditionalAutoRead();
/*      */       }
/* 1764 */       return s;
/*      */     }
/* 1766 */     catch (Throwable e) {
/* 1767 */       System.err.println("JNA: Error creating structure: " + e);
/* 1768 */       return null;
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
/*      */   public static Structure newInstance(Class<?> type, Pointer init) throws IllegalArgumentException {
/*      */     try {
/* 1781 */       Constructor<?> ctor = type.getConstructor(new Class[] { Pointer.class });
/* 1782 */       return (Structure)ctor.newInstance(new Object[] { init });
/*      */     }
/* 1784 */     catch (NoSuchMethodException noSuchMethodException) {
/*      */ 
/*      */     
/* 1787 */     } catch (SecurityException securityException) {
/*      */ 
/*      */     
/* 1790 */     } catch (InstantiationException e) {
/* 1791 */       String msg = "Can't instantiate " + type;
/* 1792 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/* 1794 */     catch (IllegalAccessException e) {
/* 1795 */       String msg = "Instantiation of " + type + " (Pointer) not allowed, is it public?";
/* 1796 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/* 1798 */     catch (InvocationTargetException e) {
/* 1799 */       String msg = "Exception thrown while instantiating an instance of " + type;
/* 1800 */       e.printStackTrace();
/* 1801 */       throw new IllegalArgumentException(msg, e);
/*      */     } 
/* 1803 */     Structure s = newInstance(type);
/* 1804 */     if (init != PLACEHOLDER_MEMORY) {
/* 1805 */       s.useMemory(init);
/*      */     }
/* 1807 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Structure newInstance(Class<?> type) throws IllegalArgumentException {
/*      */     try {
/* 1817 */       Structure s = (Structure)type.newInstance();
/* 1818 */       if (s instanceof ByValue) {
/* 1819 */         s.allocateMemory();
/*      */       }
/* 1821 */       return s;
/*      */     }
/* 1823 */     catch (InstantiationException e) {
/* 1824 */       String msg = "Can't instantiate " + type;
/* 1825 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/* 1827 */     catch (IllegalAccessException e) {
/* 1828 */       String msg = "Instantiation of " + type + " not allowed, is it public?";
/*      */       
/* 1830 */       throw new IllegalArgumentException(msg, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   StructField typeInfoField() {
/*      */     LayoutInfo info;
/* 1840 */     synchronized (layoutInfo) {
/* 1841 */       info = layoutInfo.get(getClass());
/*      */     } 
/* 1843 */     if (info != null) {
/* 1844 */       return info.typeInfoField;
/*      */     }
/* 1846 */     return null;
/*      */   }
/*      */   
/*      */   protected static class StructField {
/*      */     public String name;
/*      */     public Class<?> type;
/*      */     public Field field;
/* 1853 */     public int size = -1;
/* 1854 */     public int offset = -1;
/*      */     public boolean isVolatile;
/*      */     public boolean isReadOnly;
/*      */     public FromNativeConverter readConverter;
/*      */     public ToNativeConverter writeConverter;
/*      */     public FromNativeContext context;
/*      */     
/*      */     public String toString() {
/* 1862 */       return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   static class FFIType
/*      */     extends Structure {
/*      */     public static class size_t
/*      */       extends IntegerType {
/*      */       private static final long serialVersionUID = 1L;
/*      */       
/*      */       public size_t() {
/* 1873 */         this(0L); } public size_t(long value) {
/* 1874 */         super(Native.SIZE_T_SIZE, value);
/*      */       }
/*      */     }
/* 1877 */     private static final Map<Object, Object> typeInfoMap = new WeakHashMap<Object, Object>(); private static final int FFI_TYPE_STRUCT = 13;
/*      */     public size_t size;
/*      */     public short alignment;
/*      */     public short type;
/*      */     public Pointer elements;
/*      */     
/*      */     private static class FFITypes {
/*      */       private static Pointer ffi_type_void;
/*      */       private static Pointer ffi_type_float;
/*      */       private static Pointer ffi_type_double;
/*      */       private static Pointer ffi_type_longdouble;
/*      */       private static Pointer ffi_type_uint8;
/*      */       private static Pointer ffi_type_sint8;
/*      */       private static Pointer ffi_type_uint16;
/*      */       private static Pointer ffi_type_sint16;
/*      */       private static Pointer ffi_type_uint32;
/*      */       private static Pointer ffi_type_sint32;
/*      */       private static Pointer ffi_type_uint64;
/*      */       private static Pointer ffi_type_sint64;
/*      */       private static Pointer ffi_type_pointer; }
/*      */     
/*      */     static {
/* 1899 */       if (Native.POINTER_SIZE == 0)
/* 1900 */         throw new Error("Native library not initialized"); 
/* 1901 */       if (FFITypes.ffi_type_void == null)
/* 1902 */         throw new Error("FFI types not initialized"); 
/* 1903 */       typeInfoMap.put(void.class, FFITypes.ffi_type_void);
/* 1904 */       typeInfoMap.put(Void.class, FFITypes.ffi_type_void);
/* 1905 */       typeInfoMap.put(float.class, FFITypes.ffi_type_float);
/* 1906 */       typeInfoMap.put(Float.class, FFITypes.ffi_type_float);
/* 1907 */       typeInfoMap.put(double.class, FFITypes.ffi_type_double);
/* 1908 */       typeInfoMap.put(Double.class, FFITypes.ffi_type_double);
/* 1909 */       typeInfoMap.put(long.class, FFITypes.ffi_type_sint64);
/* 1910 */       typeInfoMap.put(Long.class, FFITypes.ffi_type_sint64);
/* 1911 */       typeInfoMap.put(int.class, FFITypes.ffi_type_sint32);
/* 1912 */       typeInfoMap.put(Integer.class, FFITypes.ffi_type_sint32);
/* 1913 */       typeInfoMap.put(short.class, FFITypes.ffi_type_sint16);
/* 1914 */       typeInfoMap.put(Short.class, FFITypes.ffi_type_sint16);
/*      */       
/* 1916 */       Pointer ctype = (Native.WCHAR_SIZE == 2) ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
/* 1917 */       typeInfoMap.put(char.class, ctype);
/* 1918 */       typeInfoMap.put(Character.class, ctype);
/* 1919 */       typeInfoMap.put(byte.class, FFITypes.ffi_type_sint8);
/* 1920 */       typeInfoMap.put(Byte.class, FFITypes.ffi_type_sint8);
/* 1921 */       typeInfoMap.put(Pointer.class, FFITypes.ffi_type_pointer);
/* 1922 */       typeInfoMap.put(String.class, FFITypes.ffi_type_pointer);
/* 1923 */       typeInfoMap.put(WString.class, FFITypes.ffi_type_pointer);
/* 1924 */       typeInfoMap.put(boolean.class, FFITypes.ffi_type_uint32);
/* 1925 */       typeInfoMap.put(Boolean.class, FFITypes.ffi_type_uint32);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private FFIType(Structure ref) {
/*      */       Pointer[] els;
/* 1932 */       this.type = 13;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1937 */       ref.ensureAllocated(true);
/*      */       
/* 1939 */       if (ref instanceof Union) {
/* 1940 */         Structure.StructField sf = ((Union)ref).typeInfoField();
/*      */         
/* 1942 */         els = new Pointer[] { get(ref.getFieldValue(sf.field), sf.type), null };
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1947 */         els = new Pointer[ref.fields().size() + 1];
/* 1948 */         int idx = 0;
/* 1949 */         for (Structure.StructField sf : ref.fields().values()) {
/* 1950 */           els[idx++] = ref.getFieldTypeInfo(sf);
/*      */         }
/*      */       } 
/* 1953 */       init(els);
/*      */     }
/*      */     private FFIType(Object array, Class<?> type) {
/*      */       this.type = 13;
/* 1957 */       int length = Array.getLength(array);
/* 1958 */       Pointer[] els = new Pointer[length + 1];
/* 1959 */       Pointer p = get((Object)null, type.getComponentType());
/* 1960 */       for (int i = 0; i < length; i++) {
/* 1961 */         els[i] = p;
/*      */       }
/* 1963 */       init(els);
/*      */     }
/*      */ 
/*      */     
/*      */     protected List<String> getFieldOrder() {
/* 1968 */       return Arrays.asList(new String[] { "size", "alignment", "type", "elements" });
/*      */     }
/*      */     private void init(Pointer[] els) {
/* 1971 */       this.elements = new Memory((Pointer.SIZE * els.length));
/* 1972 */       this.elements.write(0L, els, 0, els.length);
/* 1973 */       write();
/*      */     }
/*      */ 
/*      */     
/*      */     static Pointer get(Object obj) {
/* 1978 */       if (obj == null)
/* 1979 */         return FFITypes.ffi_type_pointer; 
/* 1980 */       if (obj instanceof Class)
/* 1981 */         return get((Object)null, (Class)obj); 
/* 1982 */       return get(obj, obj.getClass());
/*      */     }
/*      */     
/*      */     private static Pointer get(Object obj, Class<?> cls) {
/* 1986 */       TypeMapper mapper = Native.getTypeMapper(cls);
/* 1987 */       if (mapper != null) {
/* 1988 */         ToNativeConverter nc = mapper.getToNativeConverter(cls);
/* 1989 */         if (nc != null) {
/* 1990 */           cls = nc.nativeType();
/*      */         }
/*      */       } 
/* 1993 */       synchronized (typeInfoMap) {
/* 1994 */         Object o = typeInfoMap.get(cls);
/* 1995 */         if (o instanceof Pointer) {
/* 1996 */           return (Pointer)o;
/*      */         }
/* 1998 */         if (o instanceof FFIType) {
/* 1999 */           return ((FFIType)o).getPointer();
/*      */         }
/* 2001 */         if ((Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(cls)) || Callback.class
/* 2002 */           .isAssignableFrom(cls)) {
/* 2003 */           typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
/* 2004 */           return FFITypes.ffi_type_pointer;
/*      */         } 
/* 2006 */         if (Structure.class.isAssignableFrom(cls)) {
/* 2007 */           if (obj == null) obj = newInstance(cls, Structure.PLACEHOLDER_MEMORY); 
/* 2008 */           if (Structure.ByReference.class.isAssignableFrom(cls)) {
/* 2009 */             typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
/* 2010 */             return FFITypes.ffi_type_pointer;
/*      */           } 
/* 2012 */           FFIType type = new FFIType((Structure)obj);
/* 2013 */           typeInfoMap.put(cls, type);
/* 2014 */           return type.getPointer();
/*      */         } 
/* 2016 */         if (NativeMapped.class.isAssignableFrom(cls)) {
/* 2017 */           NativeMappedConverter c = NativeMappedConverter.getInstance(cls);
/* 2018 */           return get(c.toNative(obj, new ToNativeContext()), c.nativeType());
/*      */         } 
/* 2020 */         if (cls.isArray()) {
/* 2021 */           FFIType type = new FFIType(obj, cls);
/*      */           
/* 2023 */           typeInfoMap.put(obj, type);
/* 2024 */           return type.getPointer();
/*      */         } 
/* 2026 */         throw new IllegalArgumentException("Unsupported type " + cls);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class AutoAllocated extends Memory {
/*      */     public AutoAllocated(int size) {
/* 2033 */       super(size);
/*      */       
/* 2035 */       clear();
/*      */     }
/*      */     
/*      */     public String toString() {
/* 2039 */       return "auto-" + super.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void structureArrayCheck(Structure[] ss) {
/* 2044 */     if (ByReference[].class.isAssignableFrom(ss.getClass())) {
/*      */       return;
/*      */     }
/* 2047 */     Pointer base = ss[0].getPointer();
/* 2048 */     int size = ss[0].size();
/* 2049 */     for (int si = 1; si < ss.length; si++) {
/* 2050 */       if ((ss[si].getPointer()).peer != base.peer + (size * si)) {
/* 2051 */         String msg = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + si + ")";
/*      */         
/* 2053 */         throw new IllegalArgumentException(msg);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void autoRead(Structure[] ss) {
/* 2059 */     structureArrayCheck(ss);
/* 2060 */     if ((ss[0]).array == ss) {
/* 2061 */       ss[0].autoRead();
/*      */     } else {
/*      */       
/* 2064 */       for (int si = 0; si < ss.length; si++) {
/* 2065 */         if (ss[si] != null) {
/* 2066 */           ss[si].autoRead();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void autoRead() {
/* 2073 */     if (getAutoRead()) {
/* 2074 */       read();
/* 2075 */       if (this.array != null) {
/* 2076 */         for (int i = 1; i < this.array.length; i++) {
/* 2077 */           this.array[i].autoRead();
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void autoWrite(Structure[] ss) {
/* 2084 */     structureArrayCheck(ss);
/* 2085 */     if ((ss[0]).array == ss) {
/* 2086 */       ss[0].autoWrite();
/*      */     } else {
/*      */       
/* 2089 */       for (int si = 0; si < ss.length; si++) {
/* 2090 */         if (ss[si] != null) {
/* 2091 */           ss[si].autoWrite();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void autoWrite() {
/* 2098 */     if (getAutoWrite()) {
/* 2099 */       write();
/* 2100 */       if (this.array != null) {
/* 2101 */         for (int i = 1; i < this.array.length; i++) {
/* 2102 */           this.array[i].autoWrite();
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
/*      */   protected int getNativeSize(Class<?> nativeType) {
/* 2114 */     return getNativeSize(nativeType, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getNativeSize(Class<?> nativeType, Object value) {
/* 2124 */     return Native.getNativeSize(nativeType, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2130 */   private static final Pointer PLACEHOLDER_MEMORY = new Pointer(0L) {
/*      */       public Pointer share(long offset, long sz) {
/* 2132 */         return this;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*      */   static void validate(Class<?> cls) {
/* 2139 */     newInstance(cls, PLACEHOLDER_MEMORY);
/*      */   }
/*      */   
/*      */   protected abstract List<String> getFieldOrder();
/*      */   
/*      */   public static interface ByReference {}
/*      */   
/*      */   public static interface ByValue {}
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\Structure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */