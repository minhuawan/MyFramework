/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.CubemapData;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.TextureData;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KTXTextureData
/*     */   implements TextureData, CubemapData
/*     */ {
/*     */   private FileHandle file;
/*     */   private int glType;
/*     */   private int glTypeSize;
/*     */   private int glFormat;
/*     */   private int glInternalFormat;
/*     */   private int glBaseInternalFormat;
/*  43 */   private int pixelWidth = -1;
/*  44 */   private int pixelHeight = -1;
/*  45 */   private int pixelDepth = -1;
/*     */   private int numberOfArrayElements;
/*     */   private int numberOfFaces;
/*     */   private int numberOfMipmapLevels;
/*     */   private int imagePos;
/*     */   private ByteBuffer compressedData;
/*     */   private boolean useMipMaps;
/*     */   private static final int GL_TEXTURE_1D = 4660;
/*     */   private static final int GL_TEXTURE_3D = 4660;
/*     */   private static final int GL_TEXTURE_1D_ARRAY_EXT = 4660;
/*     */   private static final int GL_TEXTURE_2D_ARRAY_EXT = 4660;
/*     */   
/*     */   public KTXTextureData(FileHandle file, boolean genMipMaps) {
/*  58 */     this.file = file;
/*  59 */     this.useMipMaps = genMipMaps;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureData.TextureDataType getType() {
/*  64 */     return TextureData.TextureDataType.Custom;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/*  69 */     return (this.compressedData != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() {
/*  74 */     if (this.compressedData != null) throw new GdxRuntimeException("Already prepared"); 
/*  75 */     if (this.file == null) throw new GdxRuntimeException("Need a file to load from");
/*     */     
/*  77 */     if (this.file.name().endsWith(".zktx")) {
/*  78 */       byte[] buffer = new byte[10240];
/*  79 */       DataInputStream in = null;
/*     */       try {
/*  81 */         in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(this.file.read())));
/*  82 */         int fileSize = in.readInt();
/*  83 */         this.compressedData = BufferUtils.newUnsafeByteBuffer(fileSize);
/*  84 */         int readBytes = 0;
/*  85 */         while ((readBytes = in.read(buffer)) != -1)
/*  86 */           this.compressedData.put(buffer, 0, readBytes); 
/*  87 */         this.compressedData.position(0);
/*  88 */         this.compressedData.limit(this.compressedData.capacity());
/*  89 */       } catch (Exception e) {
/*  90 */         throw new GdxRuntimeException("Couldn't load zktx file '" + this.file + "'", e);
/*     */       } finally {
/*  92 */         StreamUtils.closeQuietly(in);
/*     */       } 
/*     */     } else {
/*  95 */       this.compressedData = ByteBuffer.wrap(this.file.readBytes());
/*     */     } 
/*  97 */     if (this.compressedData.get() != -85) throw new GdxRuntimeException("Invalid KTX Header"); 
/*  98 */     if (this.compressedData.get() != 75) throw new GdxRuntimeException("Invalid KTX Header"); 
/*  99 */     if (this.compressedData.get() != 84) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 100 */     if (this.compressedData.get() != 88) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 101 */     if (this.compressedData.get() != 32) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 102 */     if (this.compressedData.get() != 49) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 103 */     if (this.compressedData.get() != 49) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 104 */     if (this.compressedData.get() != -69) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 105 */     if (this.compressedData.get() != 13) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 106 */     if (this.compressedData.get() != 10) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 107 */     if (this.compressedData.get() != 26) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 108 */     if (this.compressedData.get() != 10) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 109 */     int endianTag = this.compressedData.getInt();
/* 110 */     if (endianTag != 67305985 && endianTag != 16909060) throw new GdxRuntimeException("Invalid KTX Header"); 
/* 111 */     if (endianTag != 67305985)
/* 112 */       this.compressedData.order((this.compressedData.order() == ByteOrder.BIG_ENDIAN) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN); 
/* 113 */     this.glType = this.compressedData.getInt();
/* 114 */     this.glTypeSize = this.compressedData.getInt();
/* 115 */     this.glFormat = this.compressedData.getInt();
/* 116 */     this.glInternalFormat = this.compressedData.getInt();
/* 117 */     this.glBaseInternalFormat = this.compressedData.getInt();
/* 118 */     this.pixelWidth = this.compressedData.getInt();
/* 119 */     this.pixelHeight = this.compressedData.getInt();
/* 120 */     this.pixelDepth = this.compressedData.getInt();
/* 121 */     this.numberOfArrayElements = this.compressedData.getInt();
/* 122 */     this.numberOfFaces = this.compressedData.getInt();
/* 123 */     this.numberOfMipmapLevels = this.compressedData.getInt();
/* 124 */     if (this.numberOfMipmapLevels == 0) {
/* 125 */       this.numberOfMipmapLevels = 1;
/* 126 */       this.useMipMaps = true;
/*     */     } 
/* 128 */     int bytesOfKeyValueData = this.compressedData.getInt();
/* 129 */     this.imagePos = this.compressedData.position() + bytesOfKeyValueData;
/* 130 */     if (!this.compressedData.isDirect()) {
/* 131 */       int pos = this.imagePos;
/* 132 */       for (int level = 0; level < this.numberOfMipmapLevels; level++) {
/* 133 */         int faceLodSize = this.compressedData.getInt(pos);
/* 134 */         int faceLodSizeRounded = faceLodSize + 3 & 0xFFFFFFFC;
/* 135 */         pos += faceLodSizeRounded * this.numberOfFaces + 4;
/*     */       } 
/* 137 */       this.compressedData.limit(pos);
/* 138 */       this.compressedData.position(0);
/* 139 */       ByteBuffer directBuffer = BufferUtils.newUnsafeByteBuffer(pos);
/* 140 */       directBuffer.order(this.compressedData.order());
/* 141 */       directBuffer.put(this.compressedData);
/* 142 */       this.compressedData = directBuffer;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeCubemapData() {
/* 153 */     consumeCustomData(34067);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeCustomData(int target) {
/* 158 */     if (this.compressedData == null) throw new GdxRuntimeException("Call prepare() before calling consumeCompressedData()"); 
/* 159 */     IntBuffer buffer = BufferUtils.newIntBuffer(16);
/*     */ 
/*     */     
/* 162 */     boolean compressed = false;
/* 163 */     if (this.glType == 0 || this.glFormat == 0) {
/* 164 */       if (this.glType + this.glFormat != 0) throw new GdxRuntimeException("either both or none of glType, glFormat must be zero"); 
/* 165 */       compressed = true;
/*     */     } 
/*     */ 
/*     */     
/* 169 */     int textureDimensions = 1;
/* 170 */     int glTarget = 4660;
/* 171 */     if (this.pixelHeight > 0) {
/* 172 */       textureDimensions = 2;
/* 173 */       glTarget = 3553;
/*     */     } 
/* 175 */     if (this.pixelDepth > 0) {
/* 176 */       textureDimensions = 3;
/* 177 */       glTarget = 4660;
/*     */     } 
/* 179 */     if (this.numberOfFaces == 6) {
/* 180 */       if (textureDimensions == 2)
/* 181 */       { glTarget = 34067; }
/*     */       else
/* 183 */       { throw new GdxRuntimeException("cube map needs 2D faces"); } 
/* 184 */     } else if (this.numberOfFaces != 1) {
/* 185 */       throw new GdxRuntimeException("numberOfFaces must be either 1 or 6");
/*     */     } 
/* 187 */     if (this.numberOfArrayElements > 0) {
/* 188 */       if (glTarget == 4660) {
/* 189 */         glTarget = 4660;
/* 190 */       } else if (glTarget == 3553) {
/* 191 */         glTarget = 4660;
/*     */       } else {
/* 193 */         throw new GdxRuntimeException("No API for 3D and cube arrays yet");
/* 194 */       }  textureDimensions++;
/*     */     } 
/* 196 */     if (glTarget == 4660) {
/* 197 */       throw new GdxRuntimeException("Unsupported texture format (only 2D texture are supported in LibGdx for the time being)");
/*     */     }
/* 199 */     int singleFace = -1;
/* 200 */     if (this.numberOfFaces == 6 && target != 34067) {
/*     */       
/* 202 */       if (34069 > target || target > 34074) {
/* 203 */         throw new GdxRuntimeException("You must specify either GL_TEXTURE_CUBE_MAP to bind all 6 faces of the cube or the requested face GL_TEXTURE_CUBE_MAP_POSITIVE_X and followings.");
/*     */       }
/* 205 */       singleFace = target - 34069;
/* 206 */       target = 34069;
/* 207 */     } else if (this.numberOfFaces == 6 && target == 34067) {
/*     */       
/* 209 */       target = 34069;
/*     */     
/*     */     }
/* 212 */     else if (target != glTarget && (34069 > target || target > 34074 || target != 3553)) {
/*     */       
/* 214 */       throw new GdxRuntimeException("Invalid target requested : 0x" + Integer.toHexString(target) + ", expecting : 0x" + 
/* 215 */           Integer.toHexString(glTarget));
/*     */     } 
/*     */ 
/*     */     
/* 219 */     Gdx.gl.glGetIntegerv(3317, buffer);
/* 220 */     int previousUnpackAlignment = buffer.get(0);
/* 221 */     if (previousUnpackAlignment != 4) Gdx.gl.glPixelStorei(3317, 4); 
/* 222 */     int glInternalFormat = this.glInternalFormat;
/* 223 */     int glFormat = this.glFormat;
/* 224 */     int pos = this.imagePos;
/* 225 */     for (int level = 0; level < this.numberOfMipmapLevels; level++) {
/* 226 */       int pixelWidth = Math.max(1, this.pixelWidth >> level);
/* 227 */       int pixelHeight = Math.max(1, this.pixelHeight >> level);
/* 228 */       int pixelDepth = Math.max(1, this.pixelDepth >> level);
/* 229 */       this.compressedData.position(pos);
/* 230 */       int faceLodSize = this.compressedData.getInt();
/* 231 */       int faceLodSizeRounded = faceLodSize + 3 & 0xFFFFFFFC;
/* 232 */       pos += 4;
/* 233 */       for (int face = 0; face < this.numberOfFaces; face++) {
/* 234 */         this.compressedData.position(pos);
/* 235 */         pos += faceLodSizeRounded;
/* 236 */         if (singleFace == -1 || singleFace == face) {
/* 237 */           ByteBuffer data = this.compressedData.slice();
/* 238 */           data.limit(faceLodSizeRounded);
/* 239 */           if (textureDimensions != 1)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 245 */             if (textureDimensions == 2)
/* 246 */             { if (this.numberOfArrayElements > 0) pixelHeight = this.numberOfArrayElements; 
/* 247 */               if (compressed)
/* 248 */               { if (glInternalFormat == ETC1.ETC1_RGB8_OES) {
/* 249 */                   if (!Gdx.graphics.supportsExtension("GL_OES_compressed_ETC1_RGB8_texture")) {
/* 250 */                     ETC1.ETC1Data etcData = new ETC1.ETC1Data(pixelWidth, pixelHeight, data, 0);
/* 251 */                     Pixmap pixmap = ETC1.decodeImage(etcData, Pixmap.Format.RGB888);
/* 252 */                     Gdx.gl.glTexImage2D(target + face, level, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap
/* 253 */                         .getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/* 254 */                     pixmap.dispose();
/*     */                   } else {
/* 256 */                     Gdx.gl.glCompressedTexImage2D(target + face, level, glInternalFormat, pixelWidth, pixelHeight, 0, faceLodSize, data);
/*     */                   }
/*     */                 
/*     */                 } else {
/*     */                   
/* 261 */                   Gdx.gl.glCompressedTexImage2D(target + face, level, glInternalFormat, pixelWidth, pixelHeight, 0, faceLodSize, data);
/*     */                 }  }
/*     */               else
/*     */               
/* 265 */               { Gdx.gl.glTexImage2D(target + face, level, glInternalFormat, pixelWidth, pixelHeight, 0, glFormat, this.glType, data); }  }
/* 266 */             else if (textureDimensions == 3 && 
/* 267 */               this.numberOfArrayElements > 0) { pixelDepth = this.numberOfArrayElements; }
/*     */           
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     if (previousUnpackAlignment != 4) Gdx.gl.glPixelStorei(3317, previousUnpackAlignment); 
/* 278 */     if (useMipMaps()) Gdx.gl.glGenerateMipmap(target);
/*     */ 
/*     */     
/* 281 */     disposePreparedData();
/*     */   }
/*     */   
/*     */   public void disposePreparedData() {
/* 285 */     if (this.compressedData != null) BufferUtils.disposeUnsafeByteBuffer(this.compressedData); 
/* 286 */     this.compressedData = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap consumePixmap() {
/* 291 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disposePixmap() {
/* 296 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 301 */     return this.pixelWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 306 */     return this.pixelHeight;
/*     */   }
/*     */   
/*     */   public int getNumberOfMipMapLevels() {
/* 310 */     return this.numberOfMipmapLevels;
/*     */   }
/*     */   
/*     */   public int getNumberOfFaces() {
/* 314 */     return this.numberOfFaces;
/*     */   }
/*     */   
/*     */   public int getGlInternalFormat() {
/* 318 */     return this.glInternalFormat;
/*     */   }
/*     */   
/*     */   public ByteBuffer getData(int requestedLevel, int requestedFace) {
/* 322 */     int pos = this.imagePos;
/* 323 */     for (int level = 0; level < this.numberOfMipmapLevels; level++) {
/* 324 */       int faceLodSize = this.compressedData.getInt(pos);
/* 325 */       int faceLodSizeRounded = faceLodSize + 3 & 0xFFFFFFFC;
/* 326 */       pos += 4;
/* 327 */       if (level == requestedLevel) {
/* 328 */         for (int face = 0; face < this.numberOfFaces; face++) {
/* 329 */           if (face == requestedFace) {
/* 330 */             this.compressedData.position(pos);
/* 331 */             ByteBuffer data = this.compressedData.slice();
/* 332 */             data.limit(faceLodSizeRounded);
/* 333 */             return data;
/*     */           } 
/* 335 */           pos += faceLodSizeRounded;
/*     */         } 
/*     */       } else {
/* 338 */         pos += faceLodSizeRounded * this.numberOfFaces;
/*     */       } 
/*     */     } 
/* 341 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap.Format getFormat() {
/* 346 */     throw new GdxRuntimeException("This TextureData implementation directly handles texture formats.");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useMipMaps() {
/* 351 */     return this.useMipMaps;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 356 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\KTXTextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */