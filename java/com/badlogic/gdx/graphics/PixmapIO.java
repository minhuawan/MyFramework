/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.utils.ByteArray;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.CheckedOutputStream;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PixmapIO
/*     */ {
/*     */   public static void writeCIM(FileHandle file, Pixmap pixmap) {
/*  48 */     CIM.write(file, pixmap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pixmap readCIM(FileHandle file) {
/*  55 */     return CIM.read(file);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writePNG(FileHandle file, Pixmap pixmap) {
/*     */     try {
/*  62 */       PNG writer = new PNG((int)((pixmap.getWidth() * pixmap.getHeight()) * 1.5F));
/*     */       try {
/*  64 */         writer.setFlipY(false);
/*  65 */         writer.write(file, pixmap);
/*     */       } finally {
/*  67 */         writer.dispose();
/*     */       } 
/*  69 */     } catch (IOException ex) {
/*  70 */       throw new GdxRuntimeException("Error writing PNG: " + file, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class CIM
/*     */   {
/*     */     private static final int BUFFER_SIZE = 32000;
/*  77 */     private static final byte[] writeBuffer = new byte[32000];
/*  78 */     private static final byte[] readBuffer = new byte[32000];
/*     */     
/*     */     public static void write(FileHandle file, Pixmap pixmap) {
/*  81 */       DataOutputStream out = null;
/*     */ 
/*     */       
/*     */       try {
/*  85 */         DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(file.write(false));
/*  86 */         out = new DataOutputStream(deflaterOutputStream);
/*  87 */         out.writeInt(pixmap.getWidth());
/*  88 */         out.writeInt(pixmap.getHeight());
/*  89 */         out.writeInt(Pixmap.Format.toGdx2DPixmapFormat(pixmap.getFormat()));
/*     */         
/*  91 */         ByteBuffer pixelBuf = pixmap.getPixels();
/*  92 */         pixelBuf.position(0);
/*  93 */         pixelBuf.limit(pixelBuf.capacity());
/*     */         
/*  95 */         int remainingBytes = pixelBuf.capacity() % 32000;
/*  96 */         int iterations = pixelBuf.capacity() / 32000;
/*     */         
/*  98 */         synchronized (writeBuffer) {
/*  99 */           for (int i = 0; i < iterations; i++) {
/* 100 */             pixelBuf.get(writeBuffer);
/* 101 */             out.write(writeBuffer);
/*     */           } 
/*     */           
/* 104 */           pixelBuf.get(writeBuffer, 0, remainingBytes);
/* 105 */           out.write(writeBuffer, 0, remainingBytes);
/*     */         } 
/*     */         
/* 108 */         pixelBuf.position(0);
/* 109 */         pixelBuf.limit(pixelBuf.capacity());
/*     */       
/*     */       }
/* 112 */       catch (Exception e) {
/* 113 */         throw new GdxRuntimeException("Couldn't write Pixmap to file '" + file + "'", e);
/*     */       } finally {
/* 115 */         StreamUtils.closeQuietly(out);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static Pixmap read(FileHandle file) {
/* 120 */       DataInputStream in = null;
/*     */ 
/*     */       
/*     */       try {
/* 124 */         in = new DataInputStream(new InflaterInputStream(new BufferedInputStream(file.read())));
/* 125 */         int width = in.readInt();
/* 126 */         int height = in.readInt();
/* 127 */         Pixmap.Format format = Pixmap.Format.fromGdx2DPixmapFormat(in.readInt());
/* 128 */         Pixmap pixmap = new Pixmap(width, height, format);
/*     */         
/* 130 */         ByteBuffer pixelBuf = pixmap.getPixels();
/* 131 */         pixelBuf.position(0);
/* 132 */         pixelBuf.limit(pixelBuf.capacity());
/*     */         
/* 134 */         synchronized (readBuffer) {
/* 135 */           int readBytes = 0;
/* 136 */           while ((readBytes = in.read(readBuffer)) > 0) {
/* 137 */             pixelBuf.put(readBuffer, 0, readBytes);
/*     */           }
/*     */         } 
/*     */         
/* 141 */         pixelBuf.position(0);
/* 142 */         pixelBuf.limit(pixelBuf.capacity());
/*     */         
/* 144 */         return pixmap;
/* 145 */       } catch (Exception e) {
/* 146 */         throw new GdxRuntimeException("Couldn't read Pixmap from file '" + file + "'", e);
/*     */       } finally {
/* 148 */         StreamUtils.closeQuietly(in);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PNG
/*     */     implements Disposable
/*     */   {
/* 180 */     private static final byte[] SIGNATURE = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
/*     */     
/*     */     private static final int IHDR = 1229472850;
/*     */     
/*     */     private static final int IDAT = 1229209940;
/*     */     
/*     */     private static final int IEND = 1229278788;
/*     */     
/*     */     private static final byte COLOR_ARGB = 6;
/*     */     
/*     */     private static final byte COMPRESSION_DEFLATE = 0;
/*     */     private static final byte FILTER_NONE = 0;
/*     */     private static final byte INTERLACE_NONE = 0;
/*     */     
/*     */     public PNG() {
/* 195 */       this(16384);
/*     */     }
/*     */     private static final byte PAETH = 4; private final ChunkBuffer buffer; private final Deflater deflater; private ByteArray lineOutBytes; private ByteArray curLineBytes; private ByteArray prevLineBytes; private boolean flipY = true; private int lastLineLen;
/*     */     public PNG(int initialBufferSize) {
/* 199 */       this.buffer = new ChunkBuffer(initialBufferSize);
/* 200 */       this.deflater = new Deflater();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFlipY(boolean flipY) {
/* 205 */       this.flipY = flipY;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCompression(int level) {
/* 210 */       this.deflater.setLevel(level);
/*     */     }
/*     */     
/*     */     public void write(FileHandle file, Pixmap pixmap) throws IOException {
/* 214 */       OutputStream output = file.write(false);
/*     */       try {
/* 216 */         write(output, pixmap);
/*     */       } finally {
/* 218 */         StreamUtils.closeQuietly(output);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void write(OutputStream output, Pixmap pixmap) throws IOException {
/*     */       byte[] lineOut, curLine, prevLine;
/* 224 */       DeflaterOutputStream deflaterOutput = new DeflaterOutputStream(this.buffer, this.deflater);
/* 225 */       DataOutputStream dataOutput = new DataOutputStream(output);
/* 226 */       dataOutput.write(SIGNATURE);
/*     */       
/* 228 */       this.buffer.writeInt(1229472850);
/* 229 */       this.buffer.writeInt(pixmap.getWidth());
/* 230 */       this.buffer.writeInt(pixmap.getHeight());
/* 231 */       this.buffer.writeByte(8);
/* 232 */       this.buffer.writeByte(6);
/* 233 */       this.buffer.writeByte(0);
/* 234 */       this.buffer.writeByte(0);
/* 235 */       this.buffer.writeByte(0);
/* 236 */       this.buffer.endChunk(dataOutput);
/*     */       
/* 238 */       this.buffer.writeInt(1229209940);
/* 239 */       this.deflater.reset();
/*     */       
/* 241 */       int lineLen = pixmap.getWidth() * 4;
/*     */       
/* 243 */       if (this.lineOutBytes == null) {
/* 244 */         lineOut = (this.lineOutBytes = new ByteArray(lineLen)).items;
/* 245 */         curLine = (this.curLineBytes = new ByteArray(lineLen)).items;
/* 246 */         prevLine = (this.prevLineBytes = new ByteArray(lineLen)).items;
/*     */       } else {
/* 248 */         lineOut = this.lineOutBytes.ensureCapacity(lineLen);
/* 249 */         curLine = this.curLineBytes.ensureCapacity(lineLen);
/* 250 */         prevLine = this.prevLineBytes.ensureCapacity(lineLen);
/* 251 */         for (int i = 0, n = this.lastLineLen; i < n; i++)
/* 252 */           prevLine[i] = 0; 
/*     */       } 
/* 254 */       this.lastLineLen = lineLen;
/*     */       
/* 256 */       ByteBuffer pixels = pixmap.getPixels();
/* 257 */       int oldPosition = pixels.position();
/* 258 */       boolean rgba8888 = (pixmap.getFormat() == Pixmap.Format.RGBA8888);
/* 259 */       for (int y = 0, h = pixmap.getHeight(); y < h; y++) {
/* 260 */         int py = this.flipY ? (h - y - 1) : y;
/* 261 */         if (rgba8888) {
/* 262 */           pixels.position(py * lineLen);
/* 263 */           pixels.get(curLine, 0, lineLen);
/*     */         } else {
/* 265 */           for (int px = 0, i = 0; px < pixmap.getWidth(); px++) {
/* 266 */             int pixel = pixmap.getPixel(px, py);
/* 267 */             curLine[i++] = (byte)(pixel >> 24 & 0xFF);
/* 268 */             curLine[i++] = (byte)(pixel >> 16 & 0xFF);
/* 269 */             curLine[i++] = (byte)(pixel >> 8 & 0xFF);
/* 270 */             curLine[i++] = (byte)(pixel & 0xFF);
/*     */           } 
/*     */         } 
/*     */         
/* 274 */         lineOut[0] = (byte)(curLine[0] - prevLine[0]);
/* 275 */         lineOut[1] = (byte)(curLine[1] - prevLine[1]);
/* 276 */         lineOut[2] = (byte)(curLine[2] - prevLine[2]);
/* 277 */         lineOut[3] = (byte)(curLine[3] - prevLine[3]);
/*     */         
/* 279 */         for (int x = 4; x < lineLen; x++) {
/* 280 */           int a = curLine[x - 4] & 0xFF;
/* 281 */           int b = prevLine[x] & 0xFF;
/* 282 */           int c = prevLine[x - 4] & 0xFF;
/* 283 */           int p = a + b - c;
/* 284 */           int pa = p - a;
/* 285 */           if (pa < 0) pa = -pa; 
/* 286 */           int pb = p - b;
/* 287 */           if (pb < 0) pb = -pb; 
/* 288 */           int pc = p - c;
/* 289 */           if (pc < 0) pc = -pc; 
/* 290 */           if (pa <= pb && pa <= pc) {
/* 291 */             c = a;
/* 292 */           } else if (pb <= pc) {
/* 293 */             c = b;
/* 294 */           }  lineOut[x] = (byte)(curLine[x] - c);
/*     */         } 
/*     */         
/* 297 */         deflaterOutput.write(4);
/* 298 */         deflaterOutput.write(lineOut, 0, lineLen);
/*     */         
/* 300 */         byte[] temp = curLine;
/* 301 */         curLine = prevLine;
/* 302 */         prevLine = temp;
/*     */       } 
/* 304 */       pixels.position(oldPosition);
/* 305 */       deflaterOutput.finish();
/* 306 */       this.buffer.endChunk(dataOutput);
/*     */       
/* 308 */       this.buffer.writeInt(1229278788);
/* 309 */       this.buffer.endChunk(dataOutput);
/*     */       
/* 311 */       output.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 316 */       this.deflater.end();
/*     */     }
/*     */     
/*     */     static class ChunkBuffer extends DataOutputStream {
/*     */       final ByteArrayOutputStream buffer;
/*     */       final CRC32 crc;
/*     */       
/*     */       ChunkBuffer(int initialSize) {
/* 324 */         this(new ByteArrayOutputStream(initialSize), new CRC32());
/*     */       }
/*     */       
/*     */       private ChunkBuffer(ByteArrayOutputStream buffer, CRC32 crc) {
/* 328 */         super(new CheckedOutputStream(buffer, crc));
/* 329 */         this.buffer = buffer;
/* 330 */         this.crc = crc;
/*     */       }
/*     */       
/*     */       public void endChunk(DataOutputStream target) throws IOException {
/* 334 */         flush();
/* 335 */         target.writeInt(this.buffer.size() - 4);
/* 336 */         this.buffer.writeTo(target);
/* 337 */         target.writeInt((int)this.crc.getValue());
/* 338 */         this.buffer.reset();
/* 339 */         this.crc.reset();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\PixmapIO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */