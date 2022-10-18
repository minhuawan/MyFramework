/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ETC1
/*     */ {
/*  40 */   public static int PKM_HEADER_SIZE = 16;
/*  41 */   public static int ETC1_RGB8_OES = 36196;
/*     */ 
/*     */   
/*     */   public static final class ETC1Data
/*     */     implements Disposable
/*     */   {
/*     */     public final int width;
/*     */     
/*     */     public final int height;
/*     */     
/*     */     public final ByteBuffer compressedData;
/*     */     
/*     */     public final int dataOffset;
/*     */     
/*     */     public ETC1Data(int width, int height, ByteBuffer compressedData, int dataOffset) {
/*  56 */       this.width = width;
/*  57 */       this.height = height;
/*  58 */       this.compressedData = compressedData;
/*  59 */       this.dataOffset = dataOffset;
/*  60 */       checkNPOT();
/*     */     }
/*     */     
/*     */     public ETC1Data(FileHandle pkmFile) {
/*  64 */       byte[] buffer = new byte[10240];
/*  65 */       DataInputStream in = null;
/*     */       try {
/*  67 */         in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(pkmFile.read())));
/*  68 */         int fileSize = in.readInt();
/*  69 */         this.compressedData = BufferUtils.newUnsafeByteBuffer(fileSize);
/*  70 */         int readBytes = 0;
/*  71 */         while ((readBytes = in.read(buffer)) != -1) {
/*  72 */           this.compressedData.put(buffer, 0, readBytes);
/*     */         }
/*  74 */         this.compressedData.position(0);
/*  75 */         this.compressedData.limit(this.compressedData.capacity());
/*  76 */       } catch (Exception e) {
/*  77 */         throw new GdxRuntimeException("Couldn't load pkm file '" + pkmFile + "'", e);
/*     */       } finally {
/*  79 */         StreamUtils.closeQuietly(in);
/*     */       } 
/*     */       
/*  82 */       this.width = ETC1.getWidthPKM(this.compressedData, 0);
/*  83 */       this.height = ETC1.getHeightPKM(this.compressedData, 0);
/*  84 */       this.dataOffset = ETC1.PKM_HEADER_SIZE;
/*  85 */       this.compressedData.position(this.dataOffset);
/*  86 */       checkNPOT();
/*     */     }
/*     */     
/*     */     private void checkNPOT() {
/*  90 */       if (!MathUtils.isPowerOfTwo(this.width) || !MathUtils.isPowerOfTwo(this.height)) {
/*  91 */         System.out.println("ETC1Data warning: non-power-of-two ETC1 textures may crash the driver of PowerVR GPUs");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPKMHeader() {
/*  97 */       return (this.dataOffset == 16);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(FileHandle file) {
/* 103 */       DataOutputStream write = null;
/* 104 */       byte[] buffer = new byte[10240];
/* 105 */       int writtenBytes = 0;
/* 106 */       this.compressedData.position(0);
/* 107 */       this.compressedData.limit(this.compressedData.capacity());
/*     */       try {
/* 109 */         write = new DataOutputStream(new GZIPOutputStream(file.write(false)));
/* 110 */         write.writeInt(this.compressedData.capacity());
/* 111 */         while (writtenBytes != this.compressedData.capacity()) {
/* 112 */           int bytesToWrite = Math.min(this.compressedData.remaining(), buffer.length);
/* 113 */           this.compressedData.get(buffer, 0, bytesToWrite);
/* 114 */           write.write(buffer, 0, bytesToWrite);
/* 115 */           writtenBytes += bytesToWrite;
/*     */         } 
/* 117 */       } catch (Exception e) {
/* 118 */         throw new GdxRuntimeException("Couldn't write PKM file to '" + file + "'", e);
/*     */       } finally {
/* 120 */         StreamUtils.closeQuietly(write);
/*     */       } 
/* 122 */       this.compressedData.position(this.dataOffset);
/* 123 */       this.compressedData.limit(this.compressedData.capacity());
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 128 */       BufferUtils.disposeUnsafeByteBuffer(this.compressedData);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 132 */       if (hasPKMHeader()) {
/* 133 */         return (ETC1.isValidPKM(this.compressedData, 0) ? "valid" : "invalid") + " pkm [" + ETC1.getWidthPKM(this.compressedData, 0) + "x" + 
/* 134 */           ETC1.getHeightPKM(this.compressedData, 0) + "], compressed: " + (this.compressedData
/* 135 */           .capacity() - ETC1.PKM_HEADER_SIZE);
/*     */       }
/* 137 */       return "raw [" + this.width + "x" + this.height + "], compressed: " + (this.compressedData.capacity() - ETC1.PKM_HEADER_SIZE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getPixelSize(Pixmap.Format format) {
/* 143 */     if (format == Pixmap.Format.RGB565) return 2; 
/* 144 */     if (format == Pixmap.Format.RGB888) return 3; 
/* 145 */     throw new GdxRuntimeException("Can only handle RGB565 or RGB888 images");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ETC1Data encodeImage(Pixmap pixmap) {
/* 152 */     int pixelSize = getPixelSize(pixmap.getFormat());
/* 153 */     ByteBuffer compressedData = encodeImage(pixmap.getPixels(), 0, pixmap.getWidth(), pixmap.getHeight(), pixelSize);
/* 154 */     BufferUtils.newUnsafeByteBuffer(compressedData);
/* 155 */     return new ETC1Data(pixmap.getWidth(), pixmap.getHeight(), compressedData, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ETC1Data encodeImagePKM(Pixmap pixmap) {
/* 163 */     int pixelSize = getPixelSize(pixmap.getFormat());
/* 164 */     ByteBuffer compressedData = encodeImagePKM(pixmap.getPixels(), 0, pixmap.getWidth(), pixmap.getHeight(), pixelSize);
/* 165 */     BufferUtils.newUnsafeByteBuffer(compressedData);
/* 166 */     return new ETC1Data(pixmap.getWidth(), pixmap.getHeight(), compressedData, 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pixmap decodeImage(ETC1Data etc1Data, Pixmap.Format format) {
/* 175 */     int dataOffset = 0;
/* 176 */     int width = 0;
/* 177 */     int height = 0;
/*     */     
/* 179 */     if (etc1Data.hasPKMHeader()) {
/* 180 */       dataOffset = 16;
/* 181 */       width = getWidthPKM(etc1Data.compressedData, 0);
/* 182 */       height = getHeightPKM(etc1Data.compressedData, 0);
/*     */     } else {
/* 184 */       dataOffset = 0;
/* 185 */       width = etc1Data.width;
/* 186 */       height = etc1Data.height;
/*     */     } 
/*     */     
/* 189 */     int pixelSize = getPixelSize(format);
/* 190 */     Pixmap pixmap = new Pixmap(width, height, format);
/* 191 */     decodeImage(etc1Data.compressedData, dataOffset, pixmap.getPixels(), 0, width, height, pixelSize);
/* 192 */     return pixmap;
/*     */   }
/*     */   
/*     */   public static native int getCompressedDataSize(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void formatHeader(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   static native int getWidthPKM(ByteBuffer paramByteBuffer, int paramInt);
/*     */   
/*     */   static native int getHeightPKM(ByteBuffer paramByteBuffer, int paramInt);
/*     */   
/*     */   static native boolean isValidPKM(ByteBuffer paramByteBuffer, int paramInt);
/*     */   
/*     */   private static native void decodeImage(ByteBuffer paramByteBuffer1, int paramInt1, ByteBuffer paramByteBuffer2, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native ByteBuffer encodeImage(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native ByteBuffer encodeImagePKM(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\ETC1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */