/*      */ package com.badlogic.gdx.utils;
/*      */ 
/*      */ import com.badlogic.gdx.files.FileHandle;
/*      */ import com.badlogic.gdx.utils.reflect.ArrayReflection;
/*      */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*      */ import com.badlogic.gdx.utils.reflect.Constructor;
/*      */ import com.badlogic.gdx.utils.reflect.Field;
/*      */ import com.badlogic.gdx.utils.reflect.ReflectionException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.security.AccessControlException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Json
/*      */ {
/*      */   private static final boolean debug = false;
/*      */   private JsonWriter writer;
/*   50 */   private String typeName = "class";
/*      */   private boolean usePrototypes = true;
/*      */   private JsonWriter.OutputType outputType;
/*      */   private boolean quoteLongValues;
/*      */   private boolean ignoreUnknownFields;
/*      */   private boolean ignoreDeprecated;
/*      */   private boolean enumNames = true;
/*      */   private Serializer defaultSerializer;
/*   58 */   private final ObjectMap<Class, OrderedMap<String, FieldMetadata>> typeToFields = new ObjectMap<Class<?>, OrderedMap<String, FieldMetadata>>();
/*   59 */   private final ObjectMap<String, Class> tagToClass = new ObjectMap<String, Class<?>>();
/*   60 */   private final ObjectMap<Class, String> classToTag = new ObjectMap<Class<?>, String>();
/*   61 */   private final ObjectMap<Class, Serializer> classToSerializer = new ObjectMap<Class<?>, Serializer>();
/*   62 */   private final ObjectMap<Class, Object[]> classToDefaultValues = (ObjectMap)new ObjectMap<Class<?>, Object>();
/*   63 */   private final Object[] equals1 = new Object[] { null }, equals2 = new Object[] { null };
/*      */   
/*      */   public Json() {
/*   66 */     this.outputType = JsonWriter.OutputType.minimal;
/*      */   }
/*      */   
/*      */   public Json(JsonWriter.OutputType outputType) {
/*   70 */     this.outputType = outputType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIgnoreUnknownFields(boolean ignoreUnknownFields) {
/*   76 */     this.ignoreUnknownFields = ignoreUnknownFields;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIgnoreDeprecated(boolean ignoreDeprecated) {
/*   81 */     this.ignoreDeprecated = ignoreDeprecated;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOutputType(JsonWriter.OutputType outputType) {
/*   86 */     this.outputType = outputType;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setQuoteLongValues(boolean quoteLongValues) {
/*   91 */     this.quoteLongValues = quoteLongValues;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnumNames(boolean enumNames) {
/*   97 */     this.enumNames = enumNames;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addClassTag(String tag, Class type) {
/*  102 */     this.tagToClass.put(tag, type);
/*  103 */     this.classToTag.put(type, tag);
/*      */   }
/*      */ 
/*      */   
/*      */   public Class getClass(String tag) {
/*  108 */     return this.tagToClass.get(tag);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTag(Class type) {
/*  113 */     return this.classToTag.get(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTypeName(String typeName) {
/*  120 */     this.typeName = typeName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultSerializer(Serializer defaultSerializer) {
/*  126 */     this.defaultSerializer = defaultSerializer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> void setSerializer(Class<T> type, Serializer<T> serializer) {
/*  132 */     this.classToSerializer.put(type, serializer);
/*      */   }
/*      */   
/*      */   public <T> Serializer<T> getSerializer(Class<T> type) {
/*  136 */     return this.classToSerializer.get(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setUsePrototypes(boolean usePrototypes) {
/*  141 */     this.usePrototypes = usePrototypes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setElementType(Class type, String fieldName, Class elementType) {
/*  147 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  148 */     FieldMetadata metadata = fields.get(fieldName);
/*  149 */     if (metadata == null) throw new SerializationException("Field not found: " + fieldName + " (" + type.getName() + ")"); 
/*  150 */     metadata.elementType = elementType;
/*      */   }
/*      */   
/*      */   private OrderedMap<String, FieldMetadata> getFields(Class<Object> type) {
/*  154 */     OrderedMap<String, FieldMetadata> fields = this.typeToFields.get(type);
/*  155 */     if (fields != null) return fields;
/*      */     
/*  157 */     Array<Class<?>> classHierarchy = new Array<Class<?>>();
/*  158 */     Class<Object> nextClass = type;
/*  159 */     while (nextClass != Object.class) {
/*  160 */       classHierarchy.add(nextClass);
/*  161 */       nextClass = (Class)nextClass.getSuperclass();
/*      */     } 
/*  163 */     ArrayList<Field> allFields = new ArrayList<Field>();
/*  164 */     for (int i = classHierarchy.size - 1; i >= 0; i--) {
/*  165 */       Collections.addAll(allFields, ClassReflection.getDeclaredFields(classHierarchy.get(i)));
/*      */     }
/*  167 */     OrderedMap<String, FieldMetadata> nameToField = new OrderedMap<String, FieldMetadata>(allFields.size());
/*  168 */     for (int j = 0, n = allFields.size(); j < n; j++) {
/*  169 */       Field field = allFields.get(j);
/*      */       
/*  171 */       if (!field.isTransient() && 
/*  172 */         !field.isStatic() && 
/*  173 */         !field.isSynthetic()) {
/*      */         
/*  175 */         if (!field.isAccessible()) {
/*      */           try {
/*  177 */             field.setAccessible(true);
/*  178 */           } catch (AccessControlException ex) {}
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  183 */         if (!this.ignoreDeprecated || !field.isAnnotationPresent(Deprecated.class))
/*      */         {
/*  185 */           nameToField.put(field.getName(), new FieldMetadata(field)); } 
/*      */       } 
/*  187 */     }  this.typeToFields.put(type, nameToField);
/*  188 */     return nameToField;
/*      */   }
/*      */   
/*      */   public String toJson(Object object) {
/*  192 */     return toJson(object, (object == null) ? null : object.getClass(), (Class)null);
/*      */   }
/*      */   
/*      */   public String toJson(Object object, Class knownType) {
/*  196 */     return toJson(object, knownType, (Class)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toJson(Object object, Class knownType, Class elementType) {
/*  202 */     StringWriter buffer = new StringWriter();
/*  203 */     toJson(object, knownType, elementType, buffer);
/*  204 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   public void toJson(Object object, FileHandle file) {
/*  208 */     toJson(object, (object == null) ? null : object.getClass(), (Class)null, file);
/*      */   }
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, FileHandle file) {
/*  213 */     toJson(object, knownType, (Class)null, file);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, Class elementType, FileHandle file) {
/*  219 */     Writer writer = null;
/*      */     try {
/*  221 */       writer = file.writer(false, "UTF-8");
/*  222 */       toJson(object, knownType, elementType, writer);
/*  223 */     } catch (Exception ex) {
/*  224 */       throw new SerializationException("Error writing file: " + file, ex);
/*      */     } finally {
/*  226 */       StreamUtils.closeQuietly(writer);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void toJson(Object object, Writer writer) {
/*  231 */     toJson(object, (object == null) ? null : object.getClass(), (Class)null, writer);
/*      */   }
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, Writer writer) {
/*  236 */     toJson(object, knownType, (Class)null, writer);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(Object object, Class knownType, Class elementType, Writer writer) {
/*  242 */     setWriter(writer);
/*      */     try {
/*  244 */       writeValue(object, knownType, elementType);
/*      */     } finally {
/*  246 */       StreamUtils.closeQuietly(this.writer);
/*  247 */       this.writer = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWriter(Writer writer) {
/*  253 */     if (!(writer instanceof JsonWriter)) writer = new JsonWriter(writer); 
/*  254 */     this.writer = (JsonWriter)writer;
/*  255 */     this.writer.setOutputType(this.outputType);
/*  256 */     this.writer.setQuoteLongValues(this.quoteLongValues);
/*      */   }
/*      */   
/*      */   public JsonWriter getWriter() {
/*  260 */     return this.writer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeFields(Object object) {
/*  265 */     Class<?> type = object.getClass();
/*      */     
/*  267 */     Object[] defaultValues = getDefaultValues(type);
/*      */     
/*  269 */     OrderedMap<String, FieldMetadata> fields = getFields(type);
/*  270 */     int i = 0;
/*  271 */     for (ObjectMap.Values<?> values = (new OrderedMap.OrderedMapValues(fields)).iterator(); values.hasNext(); ) { FieldMetadata metadata = (FieldMetadata)values.next();
/*  272 */       Field field = metadata.field;
/*      */       try {
/*  274 */         Object value = field.get(object);
/*  275 */         if (defaultValues != null) {
/*  276 */           Object defaultValue = defaultValues[i++];
/*  277 */           if (value == null && defaultValue == null)
/*  278 */             continue;  if (value != null && defaultValue != null) {
/*  279 */             if (value.equals(defaultValue))
/*  280 */               continue;  if (value.getClass().isArray() && defaultValue.getClass().isArray()) {
/*  281 */               this.equals1[0] = value;
/*  282 */               this.equals2[0] = defaultValue;
/*  283 */               if (Arrays.deepEquals(this.equals1, this.equals2)) {
/*      */                 continue;
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*  289 */         this.writer.name(field.getName());
/*  290 */         writeValue(value, field.getType(), metadata.elementType);
/*  291 */       } catch (ReflectionException ex) {
/*  292 */         throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  293 */       } catch (SerializationException ex) {
/*  294 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  295 */         throw ex;
/*  296 */       } catch (Exception runtimeEx) {
/*  297 */         SerializationException ex = new SerializationException(runtimeEx);
/*  298 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  299 */         throw ex;
/*      */       }  }
/*      */   
/*      */   }
/*      */   private Object[] getDefaultValues(Class type) {
/*      */     Object object;
/*  305 */     if (!this.usePrototypes) return null; 
/*  306 */     if (this.classToDefaultValues.containsKey(type)) return this.classToDefaultValues.get(type);
/*      */     
/*      */     try {
/*  309 */       object = newInstance(type);
/*  310 */     } catch (Exception ex) {
/*  311 */       this.classToDefaultValues.put(type, null);
/*  312 */       return null;
/*      */     } 
/*      */     
/*  315 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  316 */     Object[] values = new Object[fields.size];
/*  317 */     this.classToDefaultValues.put(type, values);
/*      */     
/*  319 */     int i = 0;
/*  320 */     for (ObjectMap.Values<FieldMetadata> values1 = fields.values().iterator(); values1.hasNext(); ) { FieldMetadata metadata = values1.next();
/*  321 */       Field field = metadata.field;
/*      */       try {
/*  323 */         values[i++] = field.get(object);
/*  324 */       } catch (ReflectionException ex) {
/*  325 */         throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  326 */       } catch (SerializationException ex) {
/*  327 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  328 */         throw ex;
/*  329 */       } catch (RuntimeException runtimeEx) {
/*  330 */         SerializationException ex = new SerializationException(runtimeEx);
/*  331 */         ex.addTrace(field + " (" + type.getName() + ")");
/*  332 */         throw ex;
/*      */       }  }
/*      */     
/*  335 */     return values;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String name) {
/*  340 */     writeField(object, name, name, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String name, Class elementType) {
/*  346 */     writeField(object, name, name, elementType);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String fieldName, String jsonName) {
/*  351 */     writeField(object, fieldName, jsonName, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(Object object, String fieldName, String jsonName, Class elementType) {
/*  357 */     Class<?> type = object.getClass();
/*  358 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  359 */     FieldMetadata metadata = fields.get(fieldName);
/*  360 */     if (metadata == null) throw new SerializationException("Field not found: " + fieldName + " (" + type.getName() + ")"); 
/*  361 */     Field field = metadata.field;
/*  362 */     if (elementType == null) elementType = metadata.elementType;
/*      */     
/*      */     try {
/*  365 */       this.writer.name(jsonName);
/*  366 */       writeValue(field.get(object), field.getType(), elementType);
/*  367 */     } catch (ReflectionException ex) {
/*  368 */       throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  369 */     } catch (SerializationException ex) {
/*  370 */       ex.addTrace(field + " (" + type.getName() + ")");
/*  371 */       throw ex;
/*  372 */     } catch (Exception runtimeEx) {
/*  373 */       SerializationException ex = new SerializationException(runtimeEx);
/*  374 */       ex.addTrace(field + " (" + type.getName() + ")");
/*  375 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(String name, Object value) {
/*      */     try {
/*  384 */       this.writer.name(name);
/*  385 */     } catch (IOException ex) {
/*  386 */       throw new SerializationException(ex);
/*      */     } 
/*  388 */     if (value == null) {
/*  389 */       writeValue(value, (Class)null, (Class)null);
/*      */     } else {
/*  391 */       writeValue(value, value.getClass(), (Class)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(String name, Object value, Class knownType) {
/*      */     try {
/*  401 */       this.writer.name(name);
/*  402 */     } catch (IOException ex) {
/*  403 */       throw new SerializationException(ex);
/*      */     } 
/*  405 */     writeValue(value, knownType, (Class)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(String name, Object value, Class knownType, Class elementType) {
/*      */     try {
/*  415 */       this.writer.name(name);
/*  416 */     } catch (IOException ex) {
/*  417 */       throw new SerializationException(ex);
/*      */     } 
/*  419 */     writeValue(value, knownType, elementType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(Object value) {
/*  425 */     if (value == null) {
/*  426 */       writeValue(value, (Class)null, (Class)null);
/*      */     } else {
/*  428 */       writeValue(value, value.getClass(), (Class)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(Object value, Class knownType) {
/*  435 */     writeValue(value, knownType, (Class)null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeValue(Object value, Class<String> knownType, Class<?> elementType) {
/*      */     try {
/*      */       Class<ObjectMap> clazz2;
/*      */       Class<ArrayMap> clazz1;
/*      */       Class<HashMap> clazz;
/*  445 */       if (value == null) {
/*  446 */         this.writer.value(null);
/*      */         
/*      */         return;
/*      */       } 
/*  450 */       if ((knownType != null && knownType.isPrimitive()) || knownType == String.class || knownType == Integer.class || knownType == Boolean.class || knownType == Float.class || knownType == Long.class || knownType == Double.class || knownType == Short.class || knownType == Byte.class || knownType == Character.class) {
/*      */ 
/*      */         
/*  453 */         this.writer.value(value);
/*      */         
/*      */         return;
/*      */       } 
/*  457 */       Class<?> actualType = value.getClass();
/*      */       
/*  459 */       if (actualType.isPrimitive() || actualType == String.class || actualType == Integer.class || actualType == Boolean.class || actualType == Float.class || actualType == Long.class || actualType == Double.class || actualType == Short.class || actualType == Byte.class || actualType == Character.class) {
/*      */ 
/*      */         
/*  462 */         writeObjectStart(actualType, null);
/*  463 */         writeValue("value", value);
/*  464 */         writeObjectEnd();
/*      */         
/*      */         return;
/*      */       } 
/*  468 */       if (value instanceof Serializable) {
/*  469 */         writeObjectStart(actualType, knownType);
/*  470 */         ((Serializable)value).write(this);
/*  471 */         writeObjectEnd();
/*      */         
/*      */         return;
/*      */       } 
/*  475 */       Serializer<Object> serializer = this.classToSerializer.get(actualType);
/*  476 */       if (serializer != null) {
/*  477 */         serializer.write(this, value, knownType);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  482 */       if (value instanceof Array) {
/*  483 */         if (knownType != null && actualType != knownType && actualType != Array.class) {
/*  484 */           throw new SerializationException("Serialization of an Array other than the known type is not supported.\nKnown type: " + knownType + "\nActual type: " + actualType);
/*      */         }
/*  486 */         writeArrayStart();
/*  487 */         Array array = (Array)value;
/*  488 */         for (int i = 0, n = array.size; i < n; i++)
/*  489 */           writeValue(array.get(i), elementType, (Class)null); 
/*  490 */         writeArrayEnd();
/*      */         return;
/*      */       } 
/*  493 */       if (value instanceof Queue) {
/*  494 */         if (knownType != null && actualType != knownType && actualType != Queue.class) {
/*  495 */           throw new SerializationException("Serialization of a Queue other than the known type is not supported.\nKnown type: " + knownType + "\nActual type: " + actualType);
/*      */         }
/*  497 */         writeArrayStart();
/*  498 */         Queue queue = (Queue)value;
/*  499 */         for (int i = 0, n = queue.size; i < n; i++)
/*  500 */           writeValue(queue.get(i), elementType, (Class)null); 
/*  501 */         writeArrayEnd();
/*      */         return;
/*      */       } 
/*  504 */       if (value instanceof Collection) {
/*  505 */         if (this.typeName != null && actualType != ArrayList.class && (knownType == null || knownType != actualType)) {
/*  506 */           writeObjectStart(actualType, knownType);
/*  507 */           writeArrayStart("items");
/*  508 */           for (Object item : value)
/*  509 */             writeValue(item, elementType, (Class)null); 
/*  510 */           writeArrayEnd();
/*  511 */           writeObjectEnd();
/*      */         } else {
/*  513 */           writeArrayStart();
/*  514 */           for (Object item : value)
/*  515 */             writeValue(item, elementType, (Class)null); 
/*  516 */           writeArrayEnd();
/*      */         } 
/*      */         return;
/*      */       } 
/*  520 */       if (actualType.isArray()) {
/*  521 */         if (elementType == null) elementType = actualType.getComponentType(); 
/*  522 */         int length = ArrayReflection.getLength(value);
/*  523 */         writeArrayStart();
/*  524 */         for (int i = 0; i < length; i++)
/*  525 */           writeValue(ArrayReflection.get(value, i), elementType, (Class)null); 
/*  526 */         writeArrayEnd();
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  531 */       if (value instanceof ObjectMap) {
/*  532 */         if (knownType == null) clazz2 = ObjectMap.class; 
/*  533 */         writeObjectStart(actualType, clazz2);
/*  534 */         for (ObjectMap.Entries<ObjectMap.Entry> entries = ((ObjectMap)value).entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry entry = entries.next();
/*  535 */           this.writer.name(convertToString(entry.key));
/*  536 */           writeValue(entry.value, elementType, (Class)null); }
/*      */         
/*  538 */         writeObjectEnd();
/*      */         return;
/*      */       } 
/*  541 */       if (value instanceof ArrayMap) {
/*  542 */         if (clazz2 == null) clazz1 = ArrayMap.class; 
/*  543 */         writeObjectStart(actualType, clazz1);
/*  544 */         ArrayMap map = (ArrayMap)value;
/*  545 */         for (int i = 0, n = map.size; i < n; i++) {
/*  546 */           this.writer.name(convertToString(map.keys[i]));
/*  547 */           writeValue(map.values[i], elementType, (Class)null);
/*      */         } 
/*  549 */         writeObjectEnd();
/*      */         return;
/*      */       } 
/*  552 */       if (value instanceof Map) {
/*  553 */         if (clazz1 == null) clazz = HashMap.class; 
/*  554 */         writeObjectStart(actualType, clazz);
/*  555 */         for (Map.Entry entry : ((Map)value).entrySet()) {
/*  556 */           this.writer.name(convertToString(entry.getKey()));
/*  557 */           writeValue(entry.getValue(), elementType, (Class)null);
/*      */         } 
/*  559 */         writeObjectEnd();
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  564 */       if (ClassReflection.isAssignableFrom(Enum.class, actualType)) {
/*  565 */         if (this.typeName != null && (clazz == null || clazz != actualType)) {
/*      */           
/*  567 */           if (actualType.getEnumConstants() == null) actualType = actualType.getSuperclass();
/*      */           
/*  569 */           writeObjectStart(actualType, null);
/*  570 */           this.writer.name("value");
/*  571 */           this.writer.value(convertToString((Enum)value));
/*  572 */           writeObjectEnd();
/*      */         } else {
/*  574 */           this.writer.value(convertToString((Enum)value));
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*  579 */       writeObjectStart(actualType, clazz);
/*  580 */       writeFields(value);
/*  581 */       writeObjectEnd();
/*  582 */     } catch (IOException ex) {
/*  583 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeObjectStart(String name) {
/*      */     try {
/*  589 */       this.writer.name(name);
/*  590 */     } catch (IOException ex) {
/*  591 */       throw new SerializationException(ex);
/*      */     } 
/*  593 */     writeObjectStart();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeObjectStart(String name, Class actualType, Class knownType) {
/*      */     try {
/*  599 */       this.writer.name(name);
/*  600 */     } catch (IOException ex) {
/*  601 */       throw new SerializationException(ex);
/*      */     } 
/*  603 */     writeObjectStart(actualType, knownType);
/*      */   }
/*      */   
/*      */   public void writeObjectStart() {
/*      */     try {
/*  608 */       this.writer.object();
/*  609 */     } catch (IOException ex) {
/*  610 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeObjectStart(Class actualType, Class knownType) {
/*      */     try {
/*  618 */       this.writer.object();
/*  619 */     } catch (IOException ex) {
/*  620 */       throw new SerializationException(ex);
/*      */     } 
/*  622 */     if (knownType == null || knownType != actualType) writeType(actualType); 
/*      */   }
/*      */   
/*      */   public void writeObjectEnd() {
/*      */     try {
/*  627 */       this.writer.pop();
/*  628 */     } catch (IOException ex) {
/*  629 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeArrayStart(String name) {
/*      */     try {
/*  635 */       this.writer.name(name);
/*  636 */       this.writer.array();
/*  637 */     } catch (IOException ex) {
/*  638 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeArrayStart() {
/*      */     try {
/*  644 */       this.writer.array();
/*  645 */     } catch (IOException ex) {
/*  646 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeArrayEnd() {
/*      */     try {
/*  652 */       this.writer.pop();
/*  653 */     } catch (IOException ex) {
/*  654 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void writeType(Class type) {
/*  659 */     if (this.typeName == null)
/*  660 */       return;  String className = getTag(type);
/*  661 */     if (className == null) className = type.getName(); 
/*      */     try {
/*  663 */       this.writer.set(this.typeName, className);
/*  664 */     } catch (IOException ex) {
/*  665 */       throw new SerializationException(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Reader reader) {
/*  673 */     return readValue(type, (Class)null, (new JsonReader()).parse(reader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, Reader reader) {
/*  680 */     return readValue(type, elementType, (new JsonReader()).parse(reader));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, InputStream input) {
/*  686 */     return readValue(type, (Class)null, (new JsonReader()).parse(input));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, InputStream input) {
/*  693 */     return readValue(type, elementType, (new JsonReader()).parse(input));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, FileHandle file) {
/*      */     try {
/*  700 */       return readValue(type, (Class)null, (new JsonReader()).parse(file));
/*  701 */     } catch (Exception ex) {
/*  702 */       throw new SerializationException("Error reading file: " + file, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, FileHandle file) {
/*      */     try {
/*  711 */       return readValue(type, elementType, (new JsonReader()).parse(file));
/*  712 */     } catch (Exception ex) {
/*  713 */       throw new SerializationException("Error reading file: " + file, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, char[] data, int offset, int length) {
/*  720 */     return readValue(type, (Class)null, (new JsonReader()).parse(data, offset, length));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, char[] data, int offset, int length) {
/*  727 */     return readValue(type, elementType, (new JsonReader()).parse(data, offset, length));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, String json) {
/*  733 */     return readValue(type, (Class)null, (new JsonReader()).parse(json));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Class<T> type, Class elementType, String json) {
/*  739 */     return readValue(type, elementType, (new JsonReader()).parse(json));
/*      */   }
/*      */   
/*      */   public void readField(Object object, String name, JsonValue jsonData) {
/*  743 */     readField(object, name, name, (Class)null, jsonData);
/*      */   }
/*      */   
/*      */   public void readField(Object object, String name, Class elementType, JsonValue jsonData) {
/*  747 */     readField(object, name, name, elementType, jsonData);
/*      */   }
/*      */   
/*      */   public void readField(Object object, String fieldName, String jsonName, JsonValue jsonData) {
/*  751 */     readField(object, fieldName, jsonName, (Class)null, jsonData);
/*      */   }
/*      */ 
/*      */   
/*      */   public void readField(Object object, String fieldName, String jsonName, Class elementType, JsonValue jsonMap) {
/*  756 */     Class<?> type = object.getClass();
/*  757 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  758 */     FieldMetadata metadata = fields.get(fieldName);
/*  759 */     if (metadata == null) throw new SerializationException("Field not found: " + fieldName + " (" + type.getName() + ")"); 
/*  760 */     Field field = metadata.field;
/*  761 */     if (elementType == null) elementType = metadata.elementType; 
/*  762 */     readField(object, field, jsonName, elementType, jsonMap);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void readField(Object object, Field field, String jsonName, Class elementType, JsonValue jsonMap) {
/*  768 */     JsonValue jsonValue = jsonMap.get(jsonName);
/*  769 */     if (jsonValue == null)
/*      */       return;  try {
/*  771 */       field.set(object, readValue(field.getType(), elementType, jsonValue));
/*  772 */     } catch (ReflectionException ex) {
/*  773 */       throw new SerializationException("Error accessing field: " + field
/*  774 */           .getName() + " (" + field.getDeclaringClass().getName() + ")", ex);
/*  775 */     } catch (SerializationException ex) {
/*  776 */       ex.addTrace(field.getName() + " (" + field.getDeclaringClass().getName() + ")");
/*  777 */       throw ex;
/*  778 */     } catch (RuntimeException runtimeEx) {
/*  779 */       SerializationException ex = new SerializationException(runtimeEx);
/*  780 */       ex.addTrace(jsonValue.trace());
/*  781 */       ex.addTrace(field.getName() + " (" + field.getDeclaringClass().getName() + ")");
/*  782 */       throw ex;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void readFields(Object object, JsonValue jsonMap) {
/*  787 */     Class<?> type = object.getClass();
/*  788 */     ObjectMap<String, FieldMetadata> fields = getFields(type);
/*  789 */     for (JsonValue child = jsonMap.child; child != null; child = child.next) {
/*  790 */       FieldMetadata metadata = fields.get(child.name);
/*  791 */       if (metadata == null) {
/*  792 */         if (!child.name.equals(this.typeName) && 
/*  793 */           !this.ignoreUnknownFields) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  798 */           SerializationException ex = new SerializationException("Field not found: " + child.name + " (" + type.getName() + ")");
/*  799 */           ex.addTrace(child.trace());
/*  800 */           throw ex;
/*      */         } 
/*      */       } else {
/*  803 */         Field field = metadata.field;
/*      */         try {
/*  805 */           field.set(object, readValue(field.getType(), metadata.elementType, child));
/*  806 */         } catch (ReflectionException ex) {
/*  807 */           throw new SerializationException("Error accessing field: " + field.getName() + " (" + type.getName() + ")", ex);
/*  808 */         } catch (SerializationException ex) {
/*  809 */           ex.addTrace(field.getName() + " (" + type.getName() + ")");
/*  810 */           throw ex;
/*  811 */         } catch (RuntimeException runtimeEx) {
/*  812 */           SerializationException ex = new SerializationException(runtimeEx);
/*  813 */           ex.addTrace(child.trace());
/*  814 */           ex.addTrace(field.getName() + " (" + type.getName() + ")");
/*  815 */           throw ex;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, JsonValue jsonMap) {
/*  823 */     return readValue(type, (Class)null, jsonMap.get(name));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, T defaultValue, JsonValue jsonMap) {
/*  829 */     JsonValue jsonValue = jsonMap.get(name);
/*  830 */     if (jsonValue == null) return defaultValue; 
/*  831 */     return readValue(type, (Class)null, jsonValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, Class elementType, JsonValue jsonMap) {
/*  838 */     return readValue(type, elementType, jsonMap.get(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(String name, Class<T> type, Class elementType, T defaultValue, JsonValue jsonMap) {
/*  845 */     JsonValue jsonValue = jsonMap.get(name);
/*  846 */     return readValue(type, elementType, defaultValue, jsonValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Class<T> type, Class elementType, T defaultValue, JsonValue jsonData) {
/*  853 */     if (jsonData == null) return defaultValue; 
/*  854 */     return readValue(type, elementType, jsonData);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Class<T> type, JsonValue jsonData) {
/*  860 */     return readValue(type, (Class)null, jsonData);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readValue(Class<T> type, Class<?> elementType, JsonValue jsonData) {
/*      */     Class<Array> clazz;
/*  867 */     if (jsonData == null) return null;
/*      */     
/*  869 */     if (jsonData.isObject()) {
/*  870 */       String className = (this.typeName == null) ? null : jsonData.getString(this.typeName, null);
/*  871 */       if (className != null) {
/*  872 */         type = getClass(className);
/*  873 */         if (type == null) {
/*      */           try {
/*  875 */             type = ClassReflection.forName(className);
/*  876 */           } catch (ReflectionException ex) {
/*  877 */             throw new SerializationException(ex);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*  882 */       if (type == null) {
/*  883 */         if (this.defaultSerializer != null) return this.defaultSerializer.read(this, jsonData, type); 
/*  884 */         return (T)jsonData;
/*      */       } 
/*      */       
/*  887 */       if (this.typeName != null && ClassReflection.isAssignableFrom(Collection.class, type)) {
/*      */         
/*  889 */         jsonData = jsonData.get("items");
/*      */       } else {
/*  891 */         Serializer<T> serializer = this.classToSerializer.get(type);
/*  892 */         if (serializer != null) return serializer.read(this, jsonData, type);
/*      */         
/*  894 */         if (type == String.class || type == Integer.class || type == Boolean.class || type == Float.class || type == Long.class || type == Double.class || type == Short.class || type == Byte.class || type == Character.class || 
/*      */           
/*  896 */           ClassReflection.isAssignableFrom(Enum.class, type)) {
/*  897 */           return readValue("value", type, jsonData);
/*      */         }
/*      */         
/*  900 */         Object object = newInstance(type);
/*      */         
/*  902 */         if (object instanceof Serializable) {
/*  903 */           ((Serializable)object).read(this, jsonData);
/*  904 */           return (T)object;
/*      */         } 
/*      */ 
/*      */         
/*  908 */         if (object instanceof ObjectMap) {
/*  909 */           ObjectMap result = (ObjectMap)object;
/*  910 */           for (JsonValue child = jsonData.child; child != null; child = child.next) {
/*  911 */             result.put(child.name, readValue(elementType, (Class)null, child));
/*      */           }
/*  913 */           return (T)result;
/*      */         } 
/*  915 */         if (object instanceof ArrayMap) {
/*  916 */           ArrayMap result = (ArrayMap)object;
/*  917 */           for (JsonValue child = jsonData.child; child != null; child = child.next) {
/*  918 */             result.put(child.name, readValue(elementType, (Class)null, child));
/*      */           }
/*  920 */           return (T)result;
/*      */         } 
/*  922 */         if (object instanceof Map) {
/*  923 */           Map result = (Map)object;
/*  924 */           for (JsonValue child = jsonData.child; child != null; child = child.next) {
/*  925 */             if (!child.name.equals(this.typeName))
/*      */             {
/*      */               
/*  928 */               result.put(child.name, readValue(elementType, (Class)null, child)); } 
/*      */           } 
/*  930 */           return (T)result;
/*      */         } 
/*      */         
/*  933 */         readFields(object, jsonData);
/*  934 */         return (T)object;
/*      */       } 
/*      */     } 
/*      */     
/*  938 */     if (type != null) {
/*  939 */       Serializer<T> serializer = this.classToSerializer.get(type);
/*  940 */       if (serializer != null) return serializer.read(this, jsonData, type);
/*      */     
/*      */     } 
/*  943 */     if (jsonData.isArray()) {
/*      */       
/*  945 */       if (type == null || type == Object.class) clazz = Array.class; 
/*  946 */       if (ClassReflection.isAssignableFrom(Array.class, clazz)) {
/*  947 */         Array result = (clazz == Array.class) ? new Array() : (Array)newInstance(clazz);
/*  948 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  949 */           result.add(readValue(elementType, (Class)null, child)); 
/*  950 */         return (T)result;
/*      */       } 
/*  952 */       if (ClassReflection.isAssignableFrom(Queue.class, clazz)) {
/*  953 */         Queue result = (clazz == Queue.class) ? new Queue() : (Queue)newInstance(clazz);
/*  954 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  955 */           result.addLast(readValue(elementType, (Class)null, child)); 
/*  956 */         return (T)result;
/*      */       } 
/*  958 */       if (ClassReflection.isAssignableFrom(Collection.class, clazz)) {
/*  959 */         Collection result = clazz.isInterface() ? new ArrayList() : (Collection)newInstance(clazz);
/*  960 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  961 */           result.add(readValue(elementType, (Class)null, child)); 
/*  962 */         return (T)result;
/*      */       } 
/*  964 */       if (clazz.isArray()) {
/*  965 */         Class<?> componentType = clazz.getComponentType();
/*  966 */         if (elementType == null) elementType = componentType; 
/*  967 */         Object result = ArrayReflection.newInstance(componentType, jsonData.size);
/*  968 */         int i = 0;
/*  969 */         for (JsonValue child = jsonData.child; child != null; child = child.next)
/*  970 */           ArrayReflection.set(result, i++, readValue(elementType, (Class)null, child)); 
/*  971 */         return (T)result;
/*      */       } 
/*  973 */       throw new SerializationException("Unable to convert value to required type: " + jsonData + " (" + clazz.getName() + ")");
/*      */     } 
/*      */     
/*  976 */     if (jsonData.isNumber()) {
/*      */       try {
/*  978 */         if (clazz == null || clazz == float.class || clazz == Float.class) return (T)Float.valueOf(jsonData.asFloat()); 
/*  979 */         if (clazz == int.class || clazz == Integer.class) return (T)Integer.valueOf(jsonData.asInt()); 
/*  980 */         if (clazz == long.class || clazz == Long.class) return (T)Long.valueOf(jsonData.asLong()); 
/*  981 */         if (clazz == double.class || clazz == Double.class) return (T)Double.valueOf(jsonData.asDouble()); 
/*  982 */         if (clazz == String.class) return (T)jsonData.asString(); 
/*  983 */         if (clazz == short.class || clazz == Short.class) return (T)Short.valueOf(jsonData.asShort()); 
/*  984 */         if (clazz == byte.class || clazz == Byte.class) return (T)Byte.valueOf(jsonData.asByte()); 
/*  985 */       } catch (NumberFormatException numberFormatException) {}
/*      */       
/*  987 */       jsonData = new JsonValue(jsonData.asString());
/*      */     } 
/*      */     
/*  990 */     if (jsonData.isBoolean()) {
/*      */       try {
/*  992 */         if (clazz == null || clazz == boolean.class || clazz == Boolean.class) return (T)Boolean.valueOf(jsonData.asBoolean()); 
/*  993 */       } catch (NumberFormatException numberFormatException) {}
/*      */       
/*  995 */       jsonData = new JsonValue(jsonData.asString());
/*      */     } 
/*      */     
/*  998 */     if (jsonData.isString()) {
/*  999 */       String string = jsonData.asString();
/* 1000 */       if (clazz == null || clazz == String.class) return (T)string; 
/*      */       try {
/* 1002 */         if (clazz == int.class || clazz == Integer.class) return (T)Integer.valueOf(string); 
/* 1003 */         if (clazz == float.class || clazz == Float.class) return (T)Float.valueOf(string); 
/* 1004 */         if (clazz == long.class || clazz == Long.class) return (T)Long.valueOf(string); 
/* 1005 */         if (clazz == double.class || clazz == Double.class) return (T)Double.valueOf(string); 
/* 1006 */         if (clazz == short.class || clazz == Short.class) return (T)Short.valueOf(string); 
/* 1007 */         if (clazz == byte.class || clazz == Byte.class) return (T)Byte.valueOf(string); 
/* 1008 */       } catch (NumberFormatException numberFormatException) {}
/*      */       
/* 1010 */       if (clazz == boolean.class || clazz == Boolean.class) return (T)Boolean.valueOf(string); 
/* 1011 */       if (clazz == char.class || clazz == Character.class) return (T)Character.valueOf(string.charAt(0)); 
/* 1012 */       if (ClassReflection.isAssignableFrom(Enum.class, clazz)) {
/* 1013 */         Enum[] constants = (Enum[])clazz.getEnumConstants();
/* 1014 */         for (int i = 0, n = constants.length; i < n; i++) {
/* 1015 */           Enum e = constants[i];
/* 1016 */           if (string.equals(convertToString(e))) return (T)e; 
/*      */         } 
/*      */       } 
/* 1019 */       if (clazz == CharSequence.class) return (T)string; 
/* 1020 */       throw new SerializationException("Unable to convert value to required type: " + jsonData + " (" + clazz.getName() + ")");
/*      */     } 
/*      */     
/* 1023 */     return null;
/*      */   }
/*      */   
/*      */   private String convertToString(Enum e) {
/* 1027 */     return this.enumNames ? e.name() : e.toString();
/*      */   }
/*      */   
/*      */   private String convertToString(Object object) {
/* 1031 */     if (object instanceof Enum) return convertToString((Enum)object); 
/* 1032 */     if (object instanceof Class) return ((Class)object).getName(); 
/* 1033 */     return String.valueOf(object);
/*      */   }
/*      */   
/*      */   protected Object newInstance(Class type) {
/*      */     try {
/* 1038 */       return ClassReflection.newInstance(type);
/* 1039 */     } catch (Exception ex) {
/*      */ 
/*      */       
/* 1042 */       try { Constructor constructor = ClassReflection.getDeclaredConstructor(type, new Class[0]);
/* 1043 */         constructor.setAccessible(true);
/* 1044 */         return constructor.newInstance(new Object[0]); }
/* 1045 */       catch (SecurityException securityException) {  }
/* 1046 */       catch (ReflectionException ignored)
/* 1047 */       { if (ClassReflection.isAssignableFrom(Enum.class, type)) {
/* 1048 */           if (type.getEnumConstants() == null) type = type.getSuperclass(); 
/* 1049 */           return type.getEnumConstants()[0];
/*      */         } 
/* 1051 */         if (type.isArray())
/* 1052 */           throw new SerializationException("Encountered JSON object when expected array of type: " + type.getName(), ex); 
/* 1053 */         if (ClassReflection.isMemberClass(type) && !ClassReflection.isStaticClass(type)) {
/* 1054 */           throw new SerializationException("Class cannot be created (non-static member class): " + type.getName(), ex);
/*      */         }
/* 1056 */         throw new SerializationException("Class cannot be created (missing no-arg constructor): " + type.getName(), ex); }
/* 1057 */       catch (Exception privateConstructorException)
/* 1058 */       { ex = privateConstructorException; }
/*      */       
/* 1060 */       throw new SerializationException("Error constructing instance of class: " + type.getName(), ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String prettyPrint(Object object) {
/* 1065 */     return prettyPrint(object, 0);
/*      */   }
/*      */   
/*      */   public String prettyPrint(String json) {
/* 1069 */     return prettyPrint(json, 0);
/*      */   }
/*      */   
/*      */   public String prettyPrint(Object object, int singleLineColumns) {
/* 1073 */     return prettyPrint(toJson(object), singleLineColumns);
/*      */   }
/*      */   
/*      */   public String prettyPrint(String json, int singleLineColumns) {
/* 1077 */     return (new JsonReader()).parse(json).prettyPrint(this.outputType, singleLineColumns);
/*      */   }
/*      */   
/*      */   public String prettyPrint(Object object, JsonValue.PrettyPrintSettings settings) {
/* 1081 */     return prettyPrint(toJson(object), settings);
/*      */   }
/*      */   
/*      */   public String prettyPrint(String json, JsonValue.PrettyPrintSettings settings) {
/* 1085 */     return (new JsonReader()).parse(json).prettyPrint(settings);
/*      */   }
/*      */   
/*      */   private static class FieldMetadata {
/*      */     Field field;
/*      */     Class elementType;
/*      */     
/*      */     public FieldMetadata(Field field) {
/* 1093 */       this.field = field;
/*      */       
/* 1095 */       int index = (ClassReflection.isAssignableFrom(ObjectMap.class, field.getType()) || ClassReflection.isAssignableFrom(Map.class, field.getType())) ? 1 : 0;
/* 1096 */       this.elementType = field.getElementType(index);
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface Serializer<T> {
/*      */     void write(Json param1Json, T param1T, Class param1Class);
/*      */     
/*      */     T read(Json param1Json, JsonValue param1JsonValue, Class param1Class);
/*      */   }
/*      */   
/*      */   public static abstract class ReadOnlySerializer<T> implements Serializer<T> {
/*      */     public void write(Json json, T object, Class knownType) {}
/*      */     
/*      */     public abstract T read(Json param1Json, JsonValue param1JsonValue, Class param1Class);
/*      */   }
/*      */   
/*      */   public static interface Serializable {
/*      */     void write(Json param1Json);
/*      */     
/*      */     void read(Json param1Json, JsonValue param1JsonValue);
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Json.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */