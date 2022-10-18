/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.Matrix3;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BufferUtils
/*     */ {
/*  36 */   static Array<ByteBuffer> unsafeBuffers = new Array<ByteBuffer>();
/*  37 */   static int allocatedUnsafe = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(float[] src, Buffer dst, int numFloats, int offset) {
/*  50 */     if (dst instanceof ByteBuffer)
/*  51 */     { dst.limit(numFloats << 2); }
/*  52 */     else if (dst instanceof FloatBuffer) { dst.limit(numFloats); }
/*     */     
/*  54 */     copyJni(src, dst, numFloats, offset);
/*  55 */     dst.position(0);
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
/*     */   public static void copy(byte[] src, int srcOffset, Buffer dst, int numElements) {
/*  68 */     dst.limit(dst.position() + bytesToElements(dst, numElements));
/*  69 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements);
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
/*     */   public static void copy(short[] src, int srcOffset, Buffer dst, int numElements) {
/*  82 */     dst.limit(dst.position() + bytesToElements(dst, numElements << 1));
/*  83 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 1);
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
/*     */   public static void copy(char[] src, int srcOffset, int numElements, Buffer dst) {
/*  95 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 1);
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
/*     */   public static void copy(int[] src, int srcOffset, int numElements, Buffer dst) {
/* 107 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 2);
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
/*     */   public static void copy(long[] src, int srcOffset, int numElements, Buffer dst) {
/* 119 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 3);
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
/*     */   public static void copy(float[] src, int srcOffset, int numElements, Buffer dst) {
/* 131 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 2);
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
/*     */   public static void copy(double[] src, int srcOffset, int numElements, Buffer dst) {
/* 143 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 3);
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
/*     */   public static void copy(char[] src, int srcOffset, Buffer dst, int numElements) {
/* 156 */     dst.limit(dst.position() + bytesToElements(dst, numElements << 1));
/* 157 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 1);
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
/*     */   public static void copy(int[] src, int srcOffset, Buffer dst, int numElements) {
/* 170 */     dst.limit(dst.position() + bytesToElements(dst, numElements << 2));
/* 171 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 2);
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
/*     */   public static void copy(long[] src, int srcOffset, Buffer dst, int numElements) {
/* 184 */     dst.limit(dst.position() + bytesToElements(dst, numElements << 3));
/* 185 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 3);
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
/*     */   public static void copy(float[] src, int srcOffset, Buffer dst, int numElements) {
/* 198 */     dst.limit(dst.position() + bytesToElements(dst, numElements << 2));
/* 199 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 2);
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
/*     */   public static void copy(double[] src, int srcOffset, Buffer dst, int numElements) {
/* 212 */     dst.limit(dst.position() + bytesToElements(dst, numElements << 3));
/* 213 */     copyJni(src, srcOffset, dst, positionInBytes(dst), numElements << 3);
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
/*     */   public static void copy(Buffer src, Buffer dst, int numElements) {
/* 226 */     int numBytes = elementsToBytes(src, numElements);
/* 227 */     dst.limit(dst.position() + bytesToElements(dst, numBytes));
/* 228 */     copyJni(src, positionInBytes(src), dst, positionInBytes(dst), numBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void transform(Buffer data, int dimensions, int strideInBytes, int count, Matrix4 matrix) {
/* 239 */     transform(data, dimensions, strideInBytes, count, matrix, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void transform(float[] data, int dimensions, int strideInBytes, int count, Matrix4 matrix) {
/* 250 */     transform(data, dimensions, strideInBytes, count, matrix, 0);
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
/*     */   public static void transform(Buffer data, int dimensions, int strideInBytes, int count, Matrix4 matrix, int offset) {
/* 262 */     switch (dimensions) {
/*     */       case 4:
/* 264 */         transformV4M4Jni(data, strideInBytes, count, matrix.val, positionInBytes(data) + offset);
/*     */         return;
/*     */       case 3:
/* 267 */         transformV3M4Jni(data, strideInBytes, count, matrix.val, positionInBytes(data) + offset);
/*     */         return;
/*     */       case 2:
/* 270 */         transformV2M4Jni(data, strideInBytes, count, matrix.val, positionInBytes(data) + offset);
/*     */         return;
/*     */     } 
/* 273 */     throw new IllegalArgumentException();
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
/*     */   public static void transform(float[] data, int dimensions, int strideInBytes, int count, Matrix4 matrix, int offset) {
/* 286 */     switch (dimensions) {
/*     */       case 4:
/* 288 */         transformV4M4Jni(data, strideInBytes, count, matrix.val, offset);
/*     */         return;
/*     */       case 3:
/* 291 */         transformV3M4Jni(data, strideInBytes, count, matrix.val, offset);
/*     */         return;
/*     */       case 2:
/* 294 */         transformV2M4Jni(data, strideInBytes, count, matrix.val, offset);
/*     */         return;
/*     */     } 
/* 297 */     throw new IllegalArgumentException();
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
/*     */   public static void transform(Buffer data, int dimensions, int strideInBytes, int count, Matrix3 matrix) {
/* 309 */     transform(data, dimensions, strideInBytes, count, matrix, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void transform(float[] data, int dimensions, int strideInBytes, int count, Matrix3 matrix) {
/* 320 */     transform(data, dimensions, strideInBytes, count, matrix, 0);
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
/*     */   public static void transform(Buffer data, int dimensions, int strideInBytes, int count, Matrix3 matrix, int offset) {
/* 332 */     switch (dimensions) {
/*     */       case 3:
/* 334 */         transformV3M3Jni(data, strideInBytes, count, matrix.val, positionInBytes(data) + offset);
/*     */         return;
/*     */       case 2:
/* 337 */         transformV2M3Jni(data, strideInBytes, count, matrix.val, positionInBytes(data) + offset);
/*     */         return;
/*     */     } 
/* 340 */     throw new IllegalArgumentException();
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
/*     */   public static void transform(float[] data, int dimensions, int strideInBytes, int count, Matrix3 matrix, int offset) {
/* 353 */     switch (dimensions) {
/*     */       case 3:
/* 355 */         transformV3M3Jni(data, strideInBytes, count, matrix.val, offset);
/*     */         return;
/*     */       case 2:
/* 358 */         transformV2M3Jni(data, strideInBytes, count, matrix.val, offset);
/*     */         return;
/*     */     } 
/* 361 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public static long findFloats(Buffer vertex, int strideInBytes, Buffer vertices, int numVertices) {
/* 366 */     return find(vertex, positionInBytes(vertex), strideInBytes, vertices, positionInBytes(vertices), numVertices);
/*     */   }
/*     */   
/*     */   public static long findFloats(float[] vertex, int strideInBytes, Buffer vertices, int numVertices) {
/* 370 */     return find(vertex, 0, strideInBytes, vertices, positionInBytes(vertices), numVertices);
/*     */   }
/*     */   
/*     */   public static long findFloats(Buffer vertex, int strideInBytes, float[] vertices, int numVertices) {
/* 374 */     return find(vertex, positionInBytes(vertex), strideInBytes, vertices, 0, numVertices);
/*     */   }
/*     */   
/*     */   public static long findFloats(float[] vertex, int strideInBytes, float[] vertices, int numVertices) {
/* 378 */     return find(vertex, 0, strideInBytes, vertices, 0, numVertices);
/*     */   }
/*     */   
/*     */   public static long findFloats(Buffer vertex, int strideInBytes, Buffer vertices, int numVertices, float epsilon) {
/* 382 */     return find(vertex, positionInBytes(vertex), strideInBytes, vertices, positionInBytes(vertices), numVertices, epsilon);
/*     */   }
/*     */   
/*     */   public static long findFloats(float[] vertex, int strideInBytes, Buffer vertices, int numVertices, float epsilon) {
/* 386 */     return find(vertex, 0, strideInBytes, vertices, positionInBytes(vertices), numVertices, epsilon);
/*     */   }
/*     */   
/*     */   public static long findFloats(Buffer vertex, int strideInBytes, float[] vertices, int numVertices, float epsilon) {
/* 390 */     return find(vertex, positionInBytes(vertex), strideInBytes, vertices, 0, numVertices, epsilon);
/*     */   }
/*     */   
/*     */   public static long findFloats(float[] vertex, int strideInBytes, float[] vertices, int numVertices, float epsilon) {
/* 394 */     return find(vertex, 0, strideInBytes, vertices, 0, numVertices, epsilon);
/*     */   }
/*     */   
/*     */   private static int positionInBytes(Buffer dst) {
/* 398 */     if (dst instanceof ByteBuffer)
/* 399 */       return dst.position(); 
/* 400 */     if (dst instanceof ShortBuffer)
/* 401 */       return dst.position() << 1; 
/* 402 */     if (dst instanceof CharBuffer)
/* 403 */       return dst.position() << 1; 
/* 404 */     if (dst instanceof IntBuffer)
/* 405 */       return dst.position() << 2; 
/* 406 */     if (dst instanceof LongBuffer)
/* 407 */       return dst.position() << 3; 
/* 408 */     if (dst instanceof FloatBuffer)
/* 409 */       return dst.position() << 2; 
/* 410 */     if (dst instanceof DoubleBuffer) {
/* 411 */       return dst.position() << 3;
/*     */     }
/* 413 */     throw new GdxRuntimeException("Can't copy to a " + dst.getClass().getName() + " instance");
/*     */   }
/*     */   
/*     */   private static int bytesToElements(Buffer dst, int bytes) {
/* 417 */     if (dst instanceof ByteBuffer)
/* 418 */       return bytes; 
/* 419 */     if (dst instanceof ShortBuffer)
/* 420 */       return bytes >>> 1; 
/* 421 */     if (dst instanceof CharBuffer)
/* 422 */       return bytes >>> 1; 
/* 423 */     if (dst instanceof IntBuffer)
/* 424 */       return bytes >>> 2; 
/* 425 */     if (dst instanceof LongBuffer)
/* 426 */       return bytes >>> 3; 
/* 427 */     if (dst instanceof FloatBuffer)
/* 428 */       return bytes >>> 2; 
/* 429 */     if (dst instanceof DoubleBuffer) {
/* 430 */       return bytes >>> 3;
/*     */     }
/* 432 */     throw new GdxRuntimeException("Can't copy to a " + dst.getClass().getName() + " instance");
/*     */   }
/*     */   
/*     */   private static int elementsToBytes(Buffer dst, int elements) {
/* 436 */     if (dst instanceof ByteBuffer)
/* 437 */       return elements; 
/* 438 */     if (dst instanceof ShortBuffer)
/* 439 */       return elements << 1; 
/* 440 */     if (dst instanceof CharBuffer)
/* 441 */       return elements << 1; 
/* 442 */     if (dst instanceof IntBuffer)
/* 443 */       return elements << 2; 
/* 444 */     if (dst instanceof LongBuffer)
/* 445 */       return elements << 3; 
/* 446 */     if (dst instanceof FloatBuffer)
/* 447 */       return elements << 2; 
/* 448 */     if (dst instanceof DoubleBuffer) {
/* 449 */       return elements << 3;
/*     */     }
/* 451 */     throw new GdxRuntimeException("Can't copy to a " + dst.getClass().getName() + " instance");
/*     */   }
/*     */   
/*     */   public static FloatBuffer newFloatBuffer(int numFloats) {
/* 455 */     ByteBuffer buffer = ByteBuffer.allocateDirect(numFloats * 4);
/* 456 */     buffer.order(ByteOrder.nativeOrder());
/* 457 */     return buffer.asFloatBuffer();
/*     */   }
/*     */   
/*     */   public static DoubleBuffer newDoubleBuffer(int numDoubles) {
/* 461 */     ByteBuffer buffer = ByteBuffer.allocateDirect(numDoubles * 8);
/* 462 */     buffer.order(ByteOrder.nativeOrder());
/* 463 */     return buffer.asDoubleBuffer();
/*     */   }
/*     */   
/*     */   public static ByteBuffer newByteBuffer(int numBytes) {
/* 467 */     ByteBuffer buffer = ByteBuffer.allocateDirect(numBytes);
/* 468 */     buffer.order(ByteOrder.nativeOrder());
/* 469 */     return buffer;
/*     */   }
/*     */   
/*     */   public static ShortBuffer newShortBuffer(int numShorts) {
/* 473 */     ByteBuffer buffer = ByteBuffer.allocateDirect(numShorts * 2);
/* 474 */     buffer.order(ByteOrder.nativeOrder());
/* 475 */     return buffer.asShortBuffer();
/*     */   }
/*     */   
/*     */   public static CharBuffer newCharBuffer(int numChars) {
/* 479 */     ByteBuffer buffer = ByteBuffer.allocateDirect(numChars * 2);
/* 480 */     buffer.order(ByteOrder.nativeOrder());
/* 481 */     return buffer.asCharBuffer();
/*     */   }
/*     */   
/*     */   public static IntBuffer newIntBuffer(int numInts) {
/* 485 */     ByteBuffer buffer = ByteBuffer.allocateDirect(numInts * 4);
/* 486 */     buffer.order(ByteOrder.nativeOrder());
/* 487 */     return buffer.asIntBuffer();
/*     */   }
/*     */   
/*     */   public static LongBuffer newLongBuffer(int numLongs) {
/* 491 */     ByteBuffer buffer = ByteBuffer.allocateDirect(numLongs * 8);
/* 492 */     buffer.order(ByteOrder.nativeOrder());
/* 493 */     return buffer.asLongBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void disposeUnsafeByteBuffer(ByteBuffer buffer) {
/* 504 */     int size = buffer.capacity();
/* 505 */     synchronized (unsafeBuffers) {
/* 506 */       if (!unsafeBuffers.removeValue(buffer, true))
/* 507 */         throw new IllegalArgumentException("buffer not allocated with newUnsafeByteBuffer or already disposed"); 
/*     */     } 
/* 509 */     allocatedUnsafe -= size;
/* 510 */     freeMemory(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer newUnsafeByteBuffer(int numBytes) {
/* 517 */     ByteBuffer buffer = newDisposableByteBuffer(numBytes);
/* 518 */     buffer.order(ByteOrder.nativeOrder());
/* 519 */     allocatedUnsafe += numBytes;
/* 520 */     synchronized (unsafeBuffers) {
/* 521 */       unsafeBuffers.add(buffer);
/*     */     } 
/* 523 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getUnsafeBufferAddress(Buffer buffer) {
/* 532 */     return getBufferAddress(buffer) + buffer.position();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer newUnsafeByteBuffer(ByteBuffer buffer) {
/* 543 */     allocatedUnsafe += buffer.capacity();
/* 544 */     synchronized (unsafeBuffers) {
/* 545 */       unsafeBuffers.add(buffer);
/*     */     } 
/* 547 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getAllocatedBytesUnsafe() {
/* 554 */     return allocatedUnsafe;
/*     */   }
/*     */   
/*     */   private static native void freeMemory(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private static native ByteBuffer newDisposableByteBuffer(int paramInt);
/*     */   
/*     */   private static native long getBufferAddress(Buffer paramBuffer);
/*     */   
/*     */   public static native void clear(ByteBuffer paramByteBuffer, int paramInt);
/*     */   
/*     */   private static native void copyJni(float[] paramArrayOffloat, Buffer paramBuffer, int paramInt1, int paramInt2);
/*     */   
/*     */   private static native void copyJni(byte[] paramArrayOfbyte, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void copyJni(char[] paramArrayOfchar, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void copyJni(short[] paramArrayOfshort, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void copyJni(int[] paramArrayOfint, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void copyJni(long[] paramArrayOflong, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void copyJni(float[] paramArrayOffloat, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void copyJni(double[] paramArrayOfdouble, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void copyJni(Buffer paramBuffer1, int paramInt1, Buffer paramBuffer2, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void transformV4M4Jni(Buffer paramBuffer, int paramInt1, int paramInt2, float[] paramArrayOffloat, int paramInt3);
/*     */   
/*     */   private static native void transformV4M4Jni(float[] paramArrayOffloat1, int paramInt1, int paramInt2, float[] paramArrayOffloat2, int paramInt3);
/*     */   
/*     */   private static native void transformV3M4Jni(Buffer paramBuffer, int paramInt1, int paramInt2, float[] paramArrayOffloat, int paramInt3);
/*     */   
/*     */   private static native void transformV3M4Jni(float[] paramArrayOffloat1, int paramInt1, int paramInt2, float[] paramArrayOffloat2, int paramInt3);
/*     */   
/*     */   private static native void transformV2M4Jni(Buffer paramBuffer, int paramInt1, int paramInt2, float[] paramArrayOffloat, int paramInt3);
/*     */   
/*     */   private static native void transformV2M4Jni(float[] paramArrayOffloat1, int paramInt1, int paramInt2, float[] paramArrayOffloat2, int paramInt3);
/*     */   
/*     */   private static native void transformV3M3Jni(Buffer paramBuffer, int paramInt1, int paramInt2, float[] paramArrayOffloat, int paramInt3);
/*     */   
/*     */   private static native void transformV3M3Jni(float[] paramArrayOffloat1, int paramInt1, int paramInt2, float[] paramArrayOffloat2, int paramInt3);
/*     */   
/*     */   private static native void transformV2M3Jni(Buffer paramBuffer, int paramInt1, int paramInt2, float[] paramArrayOffloat, int paramInt3);
/*     */   
/*     */   private static native void transformV2M3Jni(float[] paramArrayOffloat1, int paramInt1, int paramInt2, float[] paramArrayOffloat2, int paramInt3);
/*     */   
/*     */   private static native long find(Buffer paramBuffer1, int paramInt1, int paramInt2, Buffer paramBuffer2, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native long find(float[] paramArrayOffloat, int paramInt1, int paramInt2, Buffer paramBuffer, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native long find(Buffer paramBuffer, int paramInt1, int paramInt2, float[] paramArrayOffloat, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native long find(float[] paramArrayOffloat1, int paramInt1, int paramInt2, float[] paramArrayOffloat2, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native long find(Buffer paramBuffer1, int paramInt1, int paramInt2, Buffer paramBuffer2, int paramInt3, int paramInt4, float paramFloat);
/*     */   
/*     */   private static native long find(float[] paramArrayOffloat, int paramInt1, int paramInt2, Buffer paramBuffer, int paramInt3, int paramInt4, float paramFloat);
/*     */   
/*     */   private static native long find(Buffer paramBuffer, int paramInt1, int paramInt2, float[] paramArrayOffloat, int paramInt3, int paramInt4, float paramFloat);
/*     */   
/*     */   private static native long find(float[] paramArrayOffloat1, int paramInt1, int paramInt2, float[] paramArrayOffloat2, int paramInt3, int paramInt4, float paramFloat);
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\BufferUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */