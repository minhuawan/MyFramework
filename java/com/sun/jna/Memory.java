/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
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
/*     */ public class Memory
/*     */   extends Pointer
/*     */ {
/*  57 */   private static final Map<Memory, Reference<Memory>> allocatedMemory = Collections.synchronizedMap(new WeakHashMap<Memory, Reference<Memory>>());
/*     */   
/*  59 */   private static final WeakMemoryHolder buffers = new WeakMemoryHolder();
/*     */   
/*     */   protected long size;
/*     */ 
/*     */   
/*     */   public static void purge() {
/*  65 */     buffers.clean();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void disposeAll() {
/*  71 */     Collection<Memory> refs = new LinkedList<Memory>(allocatedMemory.keySet());
/*  72 */     for (Memory r : refs) {
/*  73 */       r.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SharedMemory
/*     */     extends Memory
/*     */   {
/*     */     public SharedMemory(long offset, long size) {
/*  84 */       this.size = size;
/*  85 */       this.peer = Memory.this.peer + offset;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void dispose() {
/*  90 */       this.peer = 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void boundsCheck(long off, long sz) {
/*  95 */       Memory.this.boundsCheck(this.peer - Memory.this.peer + off, sz);
/*     */     }
/*     */     
/*     */     public String toString() {
/*  99 */       return super.toString() + " (shared from " + Memory.this.toString() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Memory(long size) {
/* 109 */     this.size = size;
/* 110 */     if (size <= 0L) {
/* 111 */       throw new IllegalArgumentException("Allocation size must be greater than zero");
/*     */     }
/* 113 */     this.peer = malloc(size);
/* 114 */     if (this.peer == 0L) {
/* 115 */       throw new OutOfMemoryError("Cannot allocate " + size + " bytes");
/*     */     }
/* 117 */     allocatedMemory.put(this, new WeakReference<Memory>(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Memory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointer share(long offset) {
/* 132 */     return share(offset, size() - offset);
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
/*     */   public Pointer share(long offset, long sz) {
/* 144 */     boundsCheck(offset, sz);
/* 145 */     return new SharedMemory(offset, sz);
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
/*     */   public Memory align(int byteBoundary) {
/* 157 */     if (byteBoundary <= 0) {
/* 158 */       throw new IllegalArgumentException("Byte boundary must be positive: " + byteBoundary);
/*     */     }
/* 160 */     for (int i = 0; i < 32; i++) {
/* 161 */       if (byteBoundary == 1 << i) {
/* 162 */         long mask = byteBoundary - 1L ^ 0xFFFFFFFFFFFFFFFFL;
/*     */         
/* 164 */         if ((this.peer & mask) != this.peer) {
/* 165 */           long newPeer = this.peer + byteBoundary - 1L & mask;
/* 166 */           long newSize = this.peer + this.size - newPeer;
/* 167 */           if (newSize <= 0L) {
/* 168 */             throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
/*     */           }
/* 170 */           return (Memory)share(newPeer - this.peer, newSize);
/*     */         } 
/* 172 */         return this;
/*     */       } 
/*     */     } 
/* 175 */     throw new IllegalArgumentException("Byte boundary must be a power of two");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() {
/* 181 */     dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void dispose() {
/*     */     try {
/* 187 */       free(this.peer);
/*     */     } finally {
/* 189 */       allocatedMemory.remove(this);
/* 190 */       this.peer = 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 196 */     clear(this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean valid() {
/* 201 */     return (this.peer != 0L);
/*     */   }
/*     */   
/*     */   public long size() {
/* 205 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void boundsCheck(long off, long sz) {
/* 214 */     if (off < 0L) {
/* 215 */       throw new IndexOutOfBoundsException("Invalid offset: " + off);
/*     */     }
/* 217 */     if (off + sz > this.size) {
/* 218 */       String msg = "Bounds exceeds available space : size=" + this.size + ", offset=" + (off + sz);
/*     */       
/* 220 */       throw new IndexOutOfBoundsException(msg);
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
/*     */   public void read(long bOff, byte[] buf, int index, int length) {
/* 238 */     boundsCheck(bOff, length * 1L);
/* 239 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, short[] buf, int index, int length) {
/* 252 */     boundsCheck(bOff, length * 2L);
/* 253 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, char[] buf, int index, int length) {
/* 266 */     boundsCheck(bOff, length * 2L);
/* 267 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, int[] buf, int index, int length) {
/* 280 */     boundsCheck(bOff, length * 4L);
/* 281 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, long[] buf, int index, int length) {
/* 294 */     boundsCheck(bOff, length * 8L);
/* 295 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, float[] buf, int index, int length) {
/* 308 */     boundsCheck(bOff, length * 4L);
/* 309 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, double[] buf, int index, int length) {
/* 322 */     boundsCheck(bOff, length * 8L);
/* 323 */     super.read(bOff, buf, index, length);
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
/*     */   public void write(long bOff, byte[] buf, int index, int length) {
/* 340 */     boundsCheck(bOff, length * 1L);
/* 341 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, short[] buf, int index, int length) {
/* 354 */     boundsCheck(bOff, length * 2L);
/* 355 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, char[] buf, int index, int length) {
/* 368 */     boundsCheck(bOff, length * 2L);
/* 369 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, int[] buf, int index, int length) {
/* 382 */     boundsCheck(bOff, length * 4L);
/* 383 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, long[] buf, int index, int length) {
/* 396 */     boundsCheck(bOff, length * 8L);
/* 397 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, float[] buf, int index, int length) {
/* 410 */     boundsCheck(bOff, length * 4L);
/* 411 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, double[] buf, int index, int length) {
/* 424 */     boundsCheck(bOff, length * 8L);
/* 425 */     super.write(bOff, buf, index, length);
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
/*     */   public byte getByte(long offset) {
/* 442 */     boundsCheck(offset, 1L);
/* 443 */     return super.getByte(offset);
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
/*     */   public char getChar(long offset) {
/* 456 */     boundsCheck(offset, 1L);
/* 457 */     return super.getChar(offset);
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
/*     */   public short getShort(long offset) {
/* 470 */     boundsCheck(offset, 2L);
/* 471 */     return super.getShort(offset);
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
/*     */   public int getInt(long offset) {
/* 484 */     boundsCheck(offset, 4L);
/* 485 */     return super.getInt(offset);
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
/*     */   public long getLong(long offset) {
/* 498 */     boundsCheck(offset, 8L);
/* 499 */     return super.getLong(offset);
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
/*     */   public float getFloat(long offset) {
/* 512 */     boundsCheck(offset, 4L);
/* 513 */     return super.getFloat(offset);
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
/*     */   public double getDouble(long offset) {
/* 526 */     boundsCheck(offset, 8L);
/* 527 */     return super.getDouble(offset);
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
/*     */   public Pointer getPointer(long offset) {
/* 540 */     boundsCheck(offset, Pointer.SIZE);
/* 541 */     return super.getPointer(offset);
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
/*     */   public ByteBuffer getByteBuffer(long offset, long length) {
/* 558 */     boundsCheck(offset, length);
/* 559 */     ByteBuffer b = super.getByteBuffer(offset, length);
/*     */ 
/*     */     
/* 562 */     buffers.put(b, this);
/* 563 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(long offset, String encoding) {
/* 569 */     boundsCheck(offset, 0L);
/* 570 */     return super.getString(offset, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWideString(long offset) {
/* 576 */     boundsCheck(offset, 0L);
/* 577 */     return super.getWideString(offset);
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
/*     */   public void setByte(long offset, byte value) {
/* 594 */     boundsCheck(offset, 1L);
/* 595 */     super.setByte(offset, value);
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
/*     */   public void setChar(long offset, char value) {
/* 608 */     boundsCheck(offset, Native.WCHAR_SIZE);
/* 609 */     super.setChar(offset, value);
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
/*     */   public void setShort(long offset, short value) {
/* 622 */     boundsCheck(offset, 2L);
/* 623 */     super.setShort(offset, value);
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
/*     */   public void setInt(long offset, int value) {
/* 636 */     boundsCheck(offset, 4L);
/* 637 */     super.setInt(offset, value);
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
/*     */   public void setLong(long offset, long value) {
/* 650 */     boundsCheck(offset, 8L);
/* 651 */     super.setLong(offset, value);
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
/*     */   public void setFloat(long offset, float value) {
/* 664 */     boundsCheck(offset, 4L);
/* 665 */     super.setFloat(offset, value);
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
/*     */   public void setDouble(long offset, double value) {
/* 678 */     boundsCheck(offset, 8L);
/* 679 */     super.setDouble(offset, value);
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
/*     */   public void setPointer(long offset, Pointer value) {
/* 692 */     boundsCheck(offset, Pointer.SIZE);
/* 693 */     super.setPointer(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setString(long offset, String value, String encoding) {
/* 698 */     boundsCheck(offset, (Native.getBytes(value, encoding)).length + 1L);
/* 699 */     super.setString(offset, value, encoding);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWideString(long offset, String value) {
/* 704 */     boundsCheck(offset, (value.length() + 1L) * Native.WCHAR_SIZE);
/* 705 */     super.setWideString(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 710 */     return "allocated@0x" + Long.toHexString(this.peer) + " (" + this.size + " bytes)";
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void free(long p) {
/* 715 */     if (p != 0L) {
/* 716 */       Native.free(p);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static long malloc(long size) {
/* 721 */     return Native.malloc(size);
/*     */   }
/*     */ 
/*     */   
/*     */   public String dump() {
/* 726 */     return dump(0L, (int)size());
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\Memory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */