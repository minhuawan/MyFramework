/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.Array;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Pointer
/*      */ {
/*      */   public static final int SIZE;
/*      */   
/*      */   static {
/*   54 */     if ((SIZE = Native.POINTER_SIZE) == 0) {
/*   55 */       throw new Error("Native library not initialized");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*   60 */   public static final Pointer NULL = null;
/*      */   protected long peer;
/*      */   
/*      */   public static final Pointer createConstant(long peer) {
/*   64 */     return new Opaque(peer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Pointer createConstant(int peer) {
/*   72 */     return new Opaque(peer & 0xFFFFFFFFFFFFFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Pointer() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Pointer(long peer) {
/*   88 */     this.peer = peer;
/*      */   }
/*      */ 
/*      */   
/*      */   public Pointer share(long offset) {
/*   93 */     return share(offset, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Pointer share(long offset, long sz) {
/*  100 */     if (offset == 0L) {
/*  101 */       return this;
/*      */     }
/*  103 */     return new Pointer(this.peer + offset);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear(long size) {
/*  108 */     setMemory(0L, size, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/*  113 */     if (o == this) {
/*  114 */       return true;
/*      */     }
/*  116 */     if (o == null) {
/*  117 */       return false;
/*      */     }
/*  119 */     return (o instanceof Pointer && ((Pointer)o).peer == this.peer);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  124 */     return (int)((this.peer >>> 32L) + (this.peer & 0xFFFFFFFFFFFFFFFFL));
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
/*      */   public long indexOf(long offset, byte value) {
/*  136 */     return Native.indexOf(this, this.peer, offset, value);
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
/*      */   public void read(long offset, byte[] buf, int index, int length) {
/*  149 */     Native.read(this, this.peer, offset, buf, index, length);
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
/*      */   public void read(long offset, short[] buf, int index, int length) {
/*  162 */     Native.read(this, this.peer, offset, buf, index, length);
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
/*      */   public void read(long offset, char[] buf, int index, int length) {
/*  175 */     Native.read(this, this.peer, offset, buf, index, length);
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
/*      */   public void read(long offset, int[] buf, int index, int length) {
/*  188 */     Native.read(this, this.peer, offset, buf, index, length);
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
/*      */   public void read(long offset, long[] buf, int index, int length) {
/*  201 */     Native.read(this, this.peer, offset, buf, index, length);
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
/*      */   public void read(long offset, float[] buf, int index, int length) {
/*  214 */     Native.read(this, this.peer, offset, buf, index, length);
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
/*      */   public void read(long offset, double[] buf, int index, int length) {
/*  227 */     Native.read(this, this.peer, offset, buf, index, length);
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
/*      */   public void read(long offset, Pointer[] buf, int index, int length) {
/*  240 */     for (int i = 0; i < length; i++) {
/*  241 */       Pointer p = getPointer(offset + (i * SIZE));
/*  242 */       Pointer oldp = buf[i + index];
/*      */       
/*  244 */       if (oldp == null || p == null || p.peer != oldp.peer) {
/*  245 */         buf[i + index] = p;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(long offset, byte[] buf, int index, int length) {
/*  266 */     Native.write(this, this.peer, offset, buf, index, length);
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
/*      */   public void write(long offset, short[] buf, int index, int length) {
/*  280 */     Native.write(this, this.peer, offset, buf, index, length);
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
/*      */   public void write(long offset, char[] buf, int index, int length) {
/*  294 */     Native.write(this, this.peer, offset, buf, index, length);
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
/*      */   public void write(long offset, int[] buf, int index, int length) {
/*  308 */     Native.write(this, this.peer, offset, buf, index, length);
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
/*      */   public void write(long offset, long[] buf, int index, int length) {
/*  322 */     Native.write(this, this.peer, offset, buf, index, length);
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
/*      */   public void write(long offset, float[] buf, int index, int length) {
/*  336 */     Native.write(this, this.peer, offset, buf, index, length);
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
/*      */   public void write(long offset, double[] buf, int index, int length) {
/*  350 */     Native.write(this, this.peer, offset, buf, index, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(long bOff, Pointer[] buf, int index, int length) {
/*  361 */     for (int i = 0; i < length; i++) {
/*  362 */       setPointer(bOff + (i * SIZE), buf[index + i]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object getValue(long offset, Class<?> type, Object currentValue) {
/*  372 */     Object result = null;
/*  373 */     if (Structure.class.isAssignableFrom(type)) {
/*  374 */       Structure s = (Structure)currentValue;
/*  375 */       if (Structure.ByReference.class.isAssignableFrom(type)) {
/*  376 */         s = Structure.updateStructureByReference(type, s, getPointer(offset));
/*      */       } else {
/*  378 */         s.useMemory(this, (int)offset, true);
/*  379 */         s.read();
/*      */       } 
/*  381 */       result = s;
/*  382 */     } else if (type == boolean.class || type == Boolean.class) {
/*  383 */       result = Function.valueOf((getInt(offset) != 0));
/*  384 */     } else if (type == byte.class || type == Byte.class) {
/*  385 */       result = Byte.valueOf(getByte(offset));
/*  386 */     } else if (type == short.class || type == Short.class) {
/*  387 */       result = Short.valueOf(getShort(offset));
/*  388 */     } else if (type == char.class || type == Character.class) {
/*  389 */       result = Character.valueOf(getChar(offset));
/*  390 */     } else if (type == int.class || type == Integer.class) {
/*  391 */       result = Integer.valueOf(getInt(offset));
/*  392 */     } else if (type == long.class || type == Long.class) {
/*  393 */       result = Long.valueOf(getLong(offset));
/*  394 */     } else if (type == float.class || type == Float.class) {
/*  395 */       result = Float.valueOf(getFloat(offset));
/*  396 */     } else if (type == double.class || type == Double.class) {
/*  397 */       result = Double.valueOf(getDouble(offset));
/*  398 */     } else if (Pointer.class.isAssignableFrom(type)) {
/*  399 */       Pointer p = getPointer(offset);
/*  400 */       if (p != null) {
/*  401 */         Pointer oldp = (currentValue instanceof Pointer) ? (Pointer)currentValue : null;
/*      */         
/*  403 */         if (oldp == null || p.peer != oldp.peer) {
/*  404 */           result = p;
/*      */         } else {
/*  406 */           result = oldp;
/*      */         } 
/*      */       } 
/*  409 */     } else if (type == String.class) {
/*  410 */       Pointer p = getPointer(offset);
/*  411 */       result = (p != null) ? p.getString(0L) : null;
/*  412 */     } else if (type == WString.class) {
/*  413 */       Pointer p = getPointer(offset);
/*  414 */       result = (p != null) ? new WString(p.getWideString(0L)) : null;
/*  415 */     } else if (Callback.class.isAssignableFrom(type)) {
/*      */ 
/*      */       
/*  418 */       Pointer fp = getPointer(offset);
/*  419 */       if (fp == null) {
/*  420 */         result = null;
/*      */       } else {
/*  422 */         Callback cb = (Callback)currentValue;
/*  423 */         Pointer oldfp = CallbackReference.getFunctionPointer(cb);
/*  424 */         if (!fp.equals(oldfp)) {
/*  425 */           cb = CallbackReference.getCallback(type, fp);
/*      */         }
/*  427 */         result = cb;
/*      */       } 
/*  429 */     } else if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) {
/*  430 */       Pointer bp = getPointer(offset);
/*  431 */       if (bp == null) {
/*  432 */         result = null;
/*      */       } else {
/*      */         
/*  435 */         Pointer oldbp = (currentValue == null) ? null : Native.getDirectBufferPointer((Buffer)currentValue);
/*  436 */         if (oldbp == null || !oldbp.equals(bp)) {
/*  437 */           throw new IllegalStateException("Can't autogenerate a direct buffer on memory read");
/*      */         }
/*  439 */         result = currentValue;
/*      */       } 
/*  441 */     } else if (NativeMapped.class.isAssignableFrom(type)) {
/*  442 */       NativeMapped nm = (NativeMapped)currentValue;
/*  443 */       if (nm != null) {
/*  444 */         Object value = getValue(offset, nm.nativeType(), null);
/*  445 */         result = nm.fromNative(value, new FromNativeContext(type));
/*  446 */         if (nm.equals(result)) {
/*  447 */           result = nm;
/*      */         }
/*      */       } else {
/*  450 */         NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/*  451 */         Object value = getValue(offset, tc.nativeType(), null);
/*  452 */         result = tc.fromNative(value, new FromNativeContext(type));
/*      */       } 
/*  454 */     } else if (type.isArray()) {
/*  455 */       result = currentValue;
/*  456 */       if (result == null) {
/*  457 */         throw new IllegalStateException("Need an initialized array");
/*      */       }
/*  459 */       readArray(offset, result, type.getComponentType());
/*      */     } else {
/*  461 */       throw new IllegalArgumentException("Reading \"" + type + "\" from memory is not supported");
/*      */     } 
/*  463 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private void readArray(long offset, Object o, Class<?> cls) {
/*  468 */     int length = 0;
/*  469 */     length = Array.getLength(o);
/*  470 */     Object result = o;
/*      */     
/*  472 */     if (cls == byte.class) {
/*  473 */       read(offset, (byte[])result, 0, length);
/*      */     }
/*  475 */     else if (cls == short.class) {
/*  476 */       read(offset, (short[])result, 0, length);
/*      */     }
/*  478 */     else if (cls == char.class) {
/*  479 */       read(offset, (char[])result, 0, length);
/*      */     }
/*  481 */     else if (cls == int.class) {
/*  482 */       read(offset, (int[])result, 0, length);
/*      */     }
/*  484 */     else if (cls == long.class) {
/*  485 */       read(offset, (long[])result, 0, length);
/*      */     }
/*  487 */     else if (cls == float.class) {
/*  488 */       read(offset, (float[])result, 0, length);
/*      */     }
/*  490 */     else if (cls == double.class) {
/*  491 */       read(offset, (double[])result, 0, length);
/*      */     }
/*  493 */     else if (Pointer.class.isAssignableFrom(cls)) {
/*  494 */       read(offset, (Pointer[])result, 0, length);
/*      */     }
/*  496 */     else if (Structure.class.isAssignableFrom(cls)) {
/*  497 */       Structure[] sarray = (Structure[])result;
/*  498 */       if (Structure.ByReference.class.isAssignableFrom(cls)) {
/*  499 */         Pointer[] parray = getPointerArray(offset, sarray.length);
/*  500 */         for (int i = 0; i < sarray.length; i++) {
/*  501 */           sarray[i] = Structure.updateStructureByReference(cls, sarray[i], parray[i]);
/*      */         }
/*      */       } else {
/*      */         
/*  505 */         Structure first = sarray[0];
/*  506 */         if (first == null) {
/*  507 */           first = Structure.newInstance(cls, share(offset));
/*  508 */           first.conditionalAutoRead();
/*  509 */           sarray[0] = first;
/*      */         } else {
/*      */           
/*  512 */           first.useMemory(this, (int)offset, true);
/*  513 */           first.read();
/*      */         } 
/*  515 */         Structure[] tmp = first.toArray(sarray.length);
/*  516 */         for (int i = 1; i < sarray.length; i++) {
/*  517 */           if (sarray[i] == null) {
/*      */             
/*  519 */             sarray[i] = tmp[i];
/*      */           } else {
/*      */             
/*  522 */             sarray[i].useMemory(this, (int)(offset + (i * sarray[i].size())), true);
/*  523 */             sarray[i].read();
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*  528 */     } else if (NativeMapped.class.isAssignableFrom(cls)) {
/*  529 */       NativeMapped[] array = (NativeMapped[])result;
/*  530 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
/*  531 */       int size = Native.getNativeSize(result.getClass(), result) / array.length;
/*  532 */       for (int i = 0; i < array.length; i++) {
/*  533 */         Object value = getValue(offset + (size * i), tc.nativeType(), array[i]);
/*  534 */         array[i] = (NativeMapped)tc.fromNative(value, new FromNativeContext(cls));
/*      */       } 
/*      */     } else {
/*      */       
/*  538 */       throw new IllegalArgumentException("Reading array of " + cls + " from memory not supported");
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
/*      */   public byte getByte(long offset) {
/*  553 */     return Native.getByte(this, this.peer, offset);
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
/*      */   public char getChar(long offset) {
/*  565 */     return Native.getChar(this, this.peer, offset);
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
/*      */   public short getShort(long offset) {
/*  577 */     return Native.getShort(this, this.peer, offset);
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
/*      */   public int getInt(long offset) {
/*  589 */     return Native.getInt(this, this.peer, offset);
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
/*      */   public long getLong(long offset) {
/*  601 */     return Native.getLong(this, this.peer, offset);
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
/*      */   public NativeLong getNativeLong(long offset) {
/*  613 */     return new NativeLong((NativeLong.SIZE == 8) ? getLong(offset) : getInt(offset));
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
/*      */   public float getFloat(long offset) {
/*  625 */     return Native.getFloat(this, this.peer, offset);
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
/*      */   public double getDouble(long offset) {
/*  637 */     return Native.getDouble(this, this.peer, offset);
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
/*      */   public Pointer getPointer(long offset) {
/*  651 */     return Native.getPointer(this.peer + offset);
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
/*      */   public ByteBuffer getByteBuffer(long offset, long length) {
/*  663 */     return Native.getDirectByteBuffer(this, this.peer, offset, length).order(ByteOrder.nativeOrder());
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
/*      */   @Deprecated
/*      */   public String getString(long offset, boolean wide) {
/*  680 */     return wide ? getWideString(offset) : getString(offset);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getWideString(long offset) {
/*  685 */     return Native.getWideString(this, this.peer, offset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(long offset) {
/*  696 */     return getString(offset, Native.getDefaultStringEncoding());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(long offset, String encoding) {
/*  707 */     return Native.getString(this, offset, encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getByteArray(long offset, int arraySize) {
/*  714 */     byte[] buf = new byte[arraySize];
/*  715 */     read(offset, buf, 0, arraySize);
/*  716 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] getCharArray(long offset, int arraySize) {
/*  723 */     char[] buf = new char[arraySize];
/*  724 */     read(offset, buf, 0, arraySize);
/*  725 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short[] getShortArray(long offset, int arraySize) {
/*  732 */     short[] buf = new short[arraySize];
/*  733 */     read(offset, buf, 0, arraySize);
/*  734 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getIntArray(long offset, int arraySize) {
/*  741 */     int[] buf = new int[arraySize];
/*  742 */     read(offset, buf, 0, arraySize);
/*  743 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long[] getLongArray(long offset, int arraySize) {
/*  750 */     long[] buf = new long[arraySize];
/*  751 */     read(offset, buf, 0, arraySize);
/*  752 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] getFloatArray(long offset, int arraySize) {
/*  759 */     float[] buf = new float[arraySize];
/*  760 */     read(offset, buf, 0, arraySize);
/*  761 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double[] getDoubleArray(long offset, int arraySize) {
/*  768 */     double[] buf = new double[arraySize];
/*  769 */     read(offset, buf, 0, arraySize);
/*  770 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Pointer[] getPointerArray(long offset) {
/*  777 */     List<Pointer> array = new ArrayList<Pointer>();
/*  778 */     int addOffset = 0;
/*  779 */     Pointer p = getPointer(offset);
/*  780 */     while (p != null) {
/*  781 */       array.add(p);
/*  782 */       addOffset += SIZE;
/*  783 */       p = getPointer(offset + addOffset);
/*      */     } 
/*  785 */     return array.<Pointer>toArray(new Pointer[array.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public Pointer[] getPointerArray(long offset, int arraySize) {
/*  790 */     Pointer[] buf = new Pointer[arraySize];
/*  791 */     read(offset, buf, 0, arraySize);
/*  792 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getStringArray(long offset) {
/*  803 */     return getStringArray(offset, -1, Native.getDefaultStringEncoding());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getStringArray(long offset, String encoding) {
/*  811 */     return getStringArray(offset, -1, encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getStringArray(long offset, int length) {
/*  821 */     return getStringArray(offset, length, Native.getDefaultStringEncoding());
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
/*      */   @Deprecated
/*      */   public String[] getStringArray(long offset, boolean wide) {
/*  834 */     return getStringArray(offset, -1, wide);
/*      */   }
/*      */   
/*      */   public String[] getWideStringArray(long offset) {
/*  838 */     return getWideStringArray(offset, -1);
/*      */   }
/*      */   
/*      */   public String[] getWideStringArray(long offset, int length) {
/*  842 */     return getStringArray(offset, length, "--WIDE-STRING--");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String[] getStringArray(long offset, int length, boolean wide) {
/*  854 */     return getStringArray(offset, length, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getStringArray(long offset, int length, String encoding) {
/*  865 */     List<String> strings = new ArrayList<String>();
/*      */     
/*  867 */     int addOffset = 0;
/*  868 */     if (length != -1) {
/*  869 */       Pointer p = getPointer(offset + addOffset);
/*  870 */       int count = 0;
/*  871 */       while (count++ < length) {
/*      */ 
/*      */ 
/*      */         
/*  875 */         String s = (p == null) ? null : ("--WIDE-STRING--".equals(encoding) ? p.getWideString(0L) : p.getString(0L, encoding));
/*  876 */         strings.add(s);
/*  877 */         if (count < length) {
/*  878 */           addOffset += SIZE;
/*  879 */           p = getPointer(offset + addOffset);
/*      */         } 
/*      */       } 
/*      */     } else {
/*  883 */       Pointer p; while ((p = getPointer(offset + addOffset)) != null) {
/*      */ 
/*      */ 
/*      */         
/*  887 */         String s = (p == null) ? null : ("--WIDE-STRING--".equals(encoding) ? p.getWideString(0L) : p.getString(0L, encoding));
/*  888 */         strings.add(s);
/*  889 */         addOffset += SIZE;
/*      */       } 
/*      */     } 
/*  892 */     return strings.<String>toArray(new String[strings.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setValue(long offset, Object value, Class<?> type) {
/*  902 */     if (type == boolean.class || type == Boolean.class) {
/*  903 */       setInt(offset, Boolean.TRUE.equals(value) ? -1 : 0);
/*  904 */     } else if (type == byte.class || type == Byte.class) {
/*  905 */       setByte(offset, (value == null) ? 0 : ((Byte)value).byteValue());
/*  906 */     } else if (type == short.class || type == Short.class) {
/*  907 */       setShort(offset, (value == null) ? 0 : ((Short)value).shortValue());
/*  908 */     } else if (type == char.class || type == Character.class) {
/*  909 */       setChar(offset, (value == null) ? Character.MIN_VALUE : ((Character)value).charValue());
/*  910 */     } else if (type == int.class || type == Integer.class) {
/*  911 */       setInt(offset, (value == null) ? 0 : ((Integer)value).intValue());
/*  912 */     } else if (type == long.class || type == Long.class) {
/*  913 */       setLong(offset, (value == null) ? 0L : ((Long)value).longValue());
/*  914 */     } else if (type == float.class || type == Float.class) {
/*  915 */       setFloat(offset, (value == null) ? 0.0F : ((Float)value).floatValue());
/*  916 */     } else if (type == double.class || type == Double.class) {
/*  917 */       setDouble(offset, (value == null) ? 0.0D : ((Double)value).doubleValue());
/*  918 */     } else if (type == Pointer.class) {
/*  919 */       setPointer(offset, (Pointer)value);
/*  920 */     } else if (type == String.class) {
/*  921 */       setPointer(offset, (Pointer)value);
/*  922 */     } else if (type == WString.class) {
/*  923 */       setPointer(offset, (Pointer)value);
/*  924 */     } else if (Structure.class.isAssignableFrom(type)) {
/*  925 */       Structure s = (Structure)value;
/*  926 */       if (Structure.ByReference.class.isAssignableFrom(type)) {
/*  927 */         setPointer(offset, (s == null) ? null : s.getPointer());
/*  928 */         if (s != null) {
/*  929 */           s.autoWrite();
/*      */         }
/*      */       } else {
/*      */         
/*  933 */         s.useMemory(this, (int)offset, true);
/*  934 */         s.write();
/*      */       } 
/*  936 */     } else if (Callback.class.isAssignableFrom(type)) {
/*  937 */       setPointer(offset, CallbackReference.getFunctionPointer((Callback)value));
/*  938 */     } else if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) {
/*      */       
/*  940 */       Pointer p = (value == null) ? null : Native.getDirectBufferPointer((Buffer)value);
/*  941 */       setPointer(offset, p);
/*  942 */     } else if (NativeMapped.class.isAssignableFrom(type)) {
/*  943 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/*  944 */       Class<?> nativeType = tc.nativeType();
/*  945 */       setValue(offset, tc.toNative(value, new ToNativeContext()), nativeType);
/*  946 */     } else if (type.isArray()) {
/*  947 */       writeArray(offset, value, type.getComponentType());
/*      */     } else {
/*  949 */       throw new IllegalArgumentException("Writing " + type + " to memory is not supported");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeArray(long offset, Object value, Class<?> cls) {
/*  955 */     if (cls == byte.class) {
/*  956 */       byte[] buf = (byte[])value;
/*  957 */       write(offset, buf, 0, buf.length);
/*  958 */     } else if (cls == short.class) {
/*  959 */       short[] buf = (short[])value;
/*  960 */       write(offset, buf, 0, buf.length);
/*  961 */     } else if (cls == char.class) {
/*  962 */       char[] buf = (char[])value;
/*  963 */       write(offset, buf, 0, buf.length);
/*  964 */     } else if (cls == int.class) {
/*  965 */       int[] buf = (int[])value;
/*  966 */       write(offset, buf, 0, buf.length);
/*  967 */     } else if (cls == long.class) {
/*  968 */       long[] buf = (long[])value;
/*  969 */       write(offset, buf, 0, buf.length);
/*  970 */     } else if (cls == float.class) {
/*  971 */       float[] buf = (float[])value;
/*  972 */       write(offset, buf, 0, buf.length);
/*  973 */     } else if (cls == double.class) {
/*  974 */       double[] buf = (double[])value;
/*  975 */       write(offset, buf, 0, buf.length);
/*  976 */     } else if (Pointer.class.isAssignableFrom(cls)) {
/*  977 */       Pointer[] buf = (Pointer[])value;
/*  978 */       write(offset, buf, 0, buf.length);
/*  979 */     } else if (Structure.class.isAssignableFrom(cls)) {
/*  980 */       Structure[] sbuf = (Structure[])value;
/*  981 */       if (Structure.ByReference.class.isAssignableFrom(cls)) {
/*  982 */         Pointer[] buf = new Pointer[sbuf.length];
/*  983 */         for (int i = 0; i < sbuf.length; i++) {
/*  984 */           if (sbuf[i] == null) {
/*  985 */             buf[i] = null;
/*      */           } else {
/*  987 */             buf[i] = sbuf[i].getPointer();
/*  988 */             sbuf[i].write();
/*      */           } 
/*      */         } 
/*  991 */         write(offset, buf, 0, buf.length);
/*      */       } else {
/*  993 */         Structure first = sbuf[0];
/*  994 */         if (first == null) {
/*  995 */           first = Structure.newInstance(cls, share(offset));
/*  996 */           sbuf[0] = first;
/*      */         } else {
/*  998 */           first.useMemory(this, (int)offset, true);
/*      */         } 
/* 1000 */         first.write();
/* 1001 */         Structure[] tmp = first.toArray(sbuf.length);
/* 1002 */         for (int i = 1; i < sbuf.length; i++) {
/* 1003 */           if (sbuf[i] == null) {
/* 1004 */             sbuf[i] = tmp[i];
/*      */           } else {
/* 1006 */             sbuf[i].useMemory(this, (int)(offset + (i * sbuf[i].size())), true);
/*      */           } 
/* 1008 */           sbuf[i].write();
/*      */         } 
/*      */       } 
/* 1011 */     } else if (NativeMapped.class.isAssignableFrom(cls)) {
/* 1012 */       NativeMapped[] buf = (NativeMapped[])value;
/* 1013 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
/* 1014 */       Class<?> nativeType = tc.nativeType();
/* 1015 */       int size = Native.getNativeSize(value.getClass(), value) / buf.length;
/* 1016 */       for (int i = 0; i < buf.length; i++) {
/* 1017 */         Object element = tc.toNative(buf[i], new ToNativeContext());
/* 1018 */         setValue(offset + (i * size), element, nativeType);
/*      */       } 
/*      */     } else {
/* 1021 */       throw new IllegalArgumentException("Writing array of " + cls + " to memory not supported");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMemory(long offset, long length, byte value) {
/* 1032 */     Native.setMemory(this, this.peer, offset, length, value);
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
/*      */   public void setByte(long offset, byte value) {
/* 1045 */     Native.setByte(this, this.peer, offset, value);
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
/*      */   public void setShort(long offset, short value) {
/* 1058 */     Native.setShort(this, this.peer, offset, value);
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
/*      */   public void setChar(long offset, char value) {
/* 1071 */     Native.setChar(this, this.peer, offset, value);
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
/*      */   public void setInt(long offset, int value) {
/* 1084 */     Native.setInt(this, this.peer, offset, value);
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
/*      */   public void setLong(long offset, long value) {
/* 1097 */     Native.setLong(this, this.peer, offset, value);
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
/*      */   public void setNativeLong(long offset, NativeLong value) {
/* 1110 */     if (NativeLong.SIZE == 8) {
/* 1111 */       setLong(offset, value.longValue());
/*      */     } else {
/* 1113 */       setInt(offset, value.intValue());
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
/*      */   public void setFloat(long offset, float value) {
/* 1127 */     Native.setFloat(this, this.peer, offset, value);
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
/*      */   public void setDouble(long offset, double value) {
/* 1140 */     Native.setDouble(this, this.peer, offset, value);
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
/*      */   public void setPointer(long offset, Pointer value) {
/* 1155 */     Native.setPointer(this, this.peer, offset, (value != null) ? value.peer : 0L);
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
/*      */   @Deprecated
/*      */   public void setString(long offset, String value, boolean wide) {
/* 1173 */     if (wide) {
/* 1174 */       setWideString(offset, value);
/*      */     } else {
/*      */       
/* 1177 */       setString(offset, value);
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
/*      */   public void setWideString(long offset, String value) {
/* 1190 */     Native.setWideString(this, this.peer, offset, value);
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
/*      */   public void setString(long offset, WString value) {
/* 1202 */     setWideString(offset, (value == null) ? null : value.toString());
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
/*      */   public void setString(long offset, String value) {
/* 1215 */     setString(offset, value, Native.getDefaultStringEncoding());
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
/*      */   public void setString(long offset, String value, String encoding) {
/* 1228 */     byte[] data = Native.getBytes(value, encoding);
/* 1229 */     write(offset, data, 0, data.length);
/* 1230 */     setByte(offset + data.length, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public String dump(long offset, int size) {
/* 1235 */     int BYTES_PER_ROW = 4;
/* 1236 */     String TITLE = "memory dump";
/*      */     
/* 1238 */     StringWriter sw = new StringWriter("memory dump".length() + 2 + size * 2 + size / 4 * 4);
/* 1239 */     PrintWriter out = new PrintWriter(sw);
/* 1240 */     out.println("memory dump");
/*      */     
/* 1242 */     for (int i = 0; i < size; i++) {
/*      */       
/* 1244 */       byte b = getByte(offset + i);
/* 1245 */       if (i % 4 == 0) out.print("["); 
/* 1246 */       if (b >= 0 && b < 16)
/* 1247 */         out.print("0"); 
/* 1248 */       out.print(Integer.toHexString(b & 0xFF));
/* 1249 */       if (i % 4 == 3 && i < size - 1)
/* 1250 */         out.println("]"); 
/*      */     } 
/* 1252 */     if (sw.getBuffer().charAt(sw.getBuffer().length() - 2) != ']') {
/* 1253 */       out.println("]");
/*      */     }
/* 1255 */     return sw.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1260 */     return "native@0x" + Long.toHexString(this.peer);
/*      */   }
/*      */ 
/*      */   
/*      */   public static long nativeValue(Pointer p) {
/* 1265 */     return (p == null) ? 0L : p.peer;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void nativeValue(Pointer p, long value) {
/* 1270 */     p.peer = value;
/*      */   }
/*      */   
/*      */   private static class Opaque extends Pointer {
/*      */     private Opaque(long peer) {
/* 1275 */       super(peer);
/* 1276 */       this.MSG = "This pointer is opaque: " + this;
/*      */     } private final String MSG;
/*      */     public Pointer share(long offset, long size) {
/* 1279 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void clear(long size) {
/* 1283 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public long indexOf(long offset, byte value) {
/* 1287 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, byte[] buf, int index, int length) {
/* 1291 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, char[] buf, int index, int length) {
/* 1295 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, short[] buf, int index, int length) {
/* 1299 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, int[] buf, int index, int length) {
/* 1303 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, long[] buf, int index, int length) {
/* 1307 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, float[] buf, int index, int length) {
/* 1311 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, double[] buf, int index, int length) {
/* 1315 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void read(long bOff, Pointer[] buf, int index, int length) {
/* 1319 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, byte[] buf, int index, int length) {
/* 1323 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, char[] buf, int index, int length) {
/* 1327 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, short[] buf, int index, int length) {
/* 1331 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, int[] buf, int index, int length) {
/* 1335 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, long[] buf, int index, int length) {
/* 1339 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, float[] buf, int index, int length) {
/* 1343 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, double[] buf, int index, int length) {
/* 1347 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void write(long bOff, Pointer[] buf, int index, int length) {
/* 1351 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public ByteBuffer getByteBuffer(long offset, long length) {
/* 1355 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public byte getByte(long bOff) {
/* 1359 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public char getChar(long bOff) {
/* 1363 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public short getShort(long bOff) {
/* 1367 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public int getInt(long bOff) {
/* 1371 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public long getLong(long bOff) {
/* 1375 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public float getFloat(long bOff) {
/* 1379 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public double getDouble(long bOff) {
/* 1383 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public Pointer getPointer(long bOff) {
/* 1387 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public String getString(long bOff, String encoding) {
/* 1391 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public String getWideString(long bOff) {
/* 1395 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setByte(long bOff, byte value) {
/* 1399 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setChar(long bOff, char value) {
/* 1403 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setShort(long bOff, short value) {
/* 1407 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setInt(long bOff, int value) {
/* 1411 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setLong(long bOff, long value) {
/* 1415 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setFloat(long bOff, float value) {
/* 1419 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setDouble(long bOff, double value) {
/* 1423 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setPointer(long offset, Pointer value) {
/* 1427 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setString(long offset, String value, String encoding) {
/* 1431 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setWideString(long offset, String value) {
/* 1435 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public void setMemory(long offset, long size, byte value) {
/* 1439 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public String dump(long offset, int size) {
/* 1443 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1447 */       return "const@0x" + Long.toHexString(this.peer);
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\Pointer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */