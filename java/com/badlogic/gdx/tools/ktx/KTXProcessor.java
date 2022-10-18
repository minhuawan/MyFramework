/*     */ package com.badlogic.gdx.tools.ktx;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationAdapter;
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.backends.headless.HeadlessApplication;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.glutils.ETC1;
/*     */ import com.badlogic.gdx.graphics.glutils.KTXTextureData;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KTXProcessor
/*     */ {
/*  29 */   static final byte[] HEADER_MAGIC = new byte[] { -85, 75, 84, 88, 32, 49, 49, -69, 13, 10, 26, 10 };
/*     */   private static final int DISPOSE_DONT = 0;
/*     */   private static final int DISPOSE_PACK = 1;
/*     */   
/*     */   public static void convert(String input, String output, boolean genMipmaps, boolean packETC1, boolean genAlphaAtlas) throws Exception {
/*  34 */     Array<String> opts = new Array(String.class);
/*  35 */     opts.add(input);
/*  36 */     opts.add(output);
/*  37 */     if (genMipmaps) opts.add("-mipmaps"); 
/*  38 */     if (packETC1 && !genAlphaAtlas) opts.add("-etc1"); 
/*  39 */     if (packETC1 && genAlphaAtlas) opts.add("-etc1a"); 
/*  40 */     main((String[])opts.toArray());
/*     */   }
/*     */   private static final int DISPOSE_FACE = 2; private static final int DISPOSE_LEVEL = 4;
/*     */   
/*     */   public static void convert(String inPx, String inNx, String inPy, String inNy, String inPz, String inNz, String output, boolean genMipmaps, boolean packETC1, boolean genAlphaAtlas) throws Exception {
/*  45 */     Array<String> opts = new Array(String.class);
/*  46 */     opts.add(inPx);
/*  47 */     opts.add(inNx);
/*  48 */     opts.add(inPy);
/*  49 */     opts.add(inNy);
/*  50 */     opts.add(inPz);
/*  51 */     opts.add(inNz);
/*  52 */     opts.add(output);
/*  53 */     if (genMipmaps) opts.add("-mipmaps"); 
/*  54 */     if (packETC1 && !genAlphaAtlas) opts.add("-etc1"); 
/*  55 */     if (packETC1 && genAlphaAtlas) opts.add("-etc1a"); 
/*  56 */     main((String[])opts.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  65 */     new HeadlessApplication((ApplicationListener)new KTXProcessorListener(args));
/*     */   }
/*     */   
/*     */   public static class KTXProcessorListener extends ApplicationAdapter {
/*     */     String[] args;
/*     */     
/*     */     KTXProcessorListener(String[] args) {
/*  72 */       this.args = args;
/*     */     }
/*     */     
/*     */     public void create() {
/*     */       int glType, glTypeSize, glFormat, glInternalFormat, glBaseInternalFormat;
/*  77 */       boolean isCubemap = (this.args.length == 7 || this.args.length == 8 || this.args.length == 9);
/*  78 */       boolean isTexture = (this.args.length == 2 || this.args.length == 3 || this.args.length == 4);
/*  79 */       boolean isPackETC1 = false, isAlphaAtlas = false, isGenMipMaps = false;
/*  80 */       if (!isCubemap && !isTexture) {
/*  81 */         System.out.println("usage : KTXProcessor input_file output_file [-etc1|-etc1a] [-mipmaps]");
/*  82 */         System.out.println("  input_file  is the texture file to include in the output KTX or ZKTX file.");
/*  83 */         System.out
/*  84 */           .println("              for cube map, just provide 6 input files corresponding to the faces in the following order : X+, X-, Y+, Y-, Z+, Z-");
/*  85 */         System.out
/*  86 */           .println("  output_file is the path to the output file, its type is based on the extension which must be either KTX or ZKTX");
/*  87 */         System.out.println();
/*  88 */         System.out.println("  options:");
/*  89 */         System.out.println("    -etc1    input file will be packed using ETC1 compression, dropping the alpha channel");
/*  90 */         System.out
/*  91 */           .println("    -etc1a   input file will be packed using ETC1 compression, doubling the height and placing the alpha channel in the bottom half");
/*  92 */         System.out.println("    -mipmaps input file will be processed to generate mipmaps");
/*  93 */         System.out.println();
/*  94 */         System.out.println("  examples:");
/*  95 */         System.out
/*  96 */           .println("    KTXProcessor in.png out.ktx                                        Create a KTX file with the provided 2D texture");
/*  97 */         System.out
/*  98 */           .println("    KTXProcessor in.png out.zktx                                       Create a Zipped KTX file with the provided 2D texture");
/*  99 */         System.out
/* 100 */           .println("    KTXProcessor in.png out.zktx -mipmaps                              Create a Zipped KTX file with the provided 2D texture, generating all mipmap levels");
/* 101 */         System.out
/* 102 */           .println("    KTXProcessor px.ktx nx.ktx py.ktx ny.ktx pz.ktx nz.ktx out.zktx    Create a Zipped KTX file with the provided cubemap textures");
/* 103 */         System.out
/* 104 */           .println("    KTXProcessor in.ktx out.zktx                                       Convert a KTX file to a Zipped KTX file");
/* 105 */         System.exit(-1);
/*     */       } 
/*     */       
/* 108 */       LwjglNativesLoader.load();
/*     */ 
/*     */       
/* 111 */       for (int i = 0; i < this.args.length; i++) {
/* 112 */         System.out.println(i + " = " + this.args[i]);
/* 113 */         if ((!isTexture || i >= 2) && (
/* 114 */           !isCubemap || i >= 7)) {
/* 115 */           if ("-etc1".equals(this.args[i])) isPackETC1 = true; 
/* 116 */           if ("-etc1a".equals(this.args[i])) isAlphaAtlas = isPackETC1 = true; 
/* 117 */           if ("-mipmaps".equals(this.args[i])) isGenMipMaps = true; 
/*     */         } 
/*     */       } 
/* 120 */       File output = new File(this.args[isCubemap ? 6 : 1]);
/*     */ 
/*     */       
/* 123 */       int ktxDispose = 0;
/* 124 */       KTXTextureData ktx = null;
/* 125 */       FileHandle file = new FileHandle(this.args[0]);
/* 126 */       if (file.name().toLowerCase().endsWith(".ktx") || file.name().toLowerCase().endsWith(".zktx")) {
/* 127 */         ktx = new KTXTextureData(file, false);
/* 128 */         if (ktx.getNumberOfFaces() == 6) isCubemap = true; 
/* 129 */         ktxDispose = 1;
/*     */       } 
/*     */ 
/*     */       
/* 133 */       int nFaces = isCubemap ? 6 : 1;
/* 134 */       KTXProcessor.Image[][] images = new KTXProcessor.Image[nFaces][];
/* 135 */       Pixmap.setBlending(Pixmap.Blending.None);
/* 136 */       Pixmap.setFilter(Pixmap.Filter.BiLinear);
/* 137 */       int texWidth = -1, texHeight = -1, texFormat = -1, nLevels = 0;
/* 138 */       for (int face = 0; face < nFaces; face++) {
/* 139 */         ETC1.ETC1Data etc1 = null;
/* 140 */         Pixmap facePixmap = null;
/* 141 */         int ktxFace = 0;
/*     */ 
/*     */         
/* 144 */         if (ktx != null && ktx.getNumberOfFaces() == 6) {
/*     */           
/* 146 */           nLevels = ktx.getNumberOfMipMapLevels();
/* 147 */           ktxFace = face;
/*     */         } else {
/* 149 */           file = new FileHandle(this.args[face]);
/* 150 */           System.out.println("Processing : " + file + " for face #" + face);
/* 151 */           if (file.name().toLowerCase().endsWith(".ktx") || file.name().toLowerCase().endsWith(".zktx")) {
/* 152 */             if (ktx == null || ktx.getNumberOfFaces() != 6) {
/* 153 */               ktxDispose = 2;
/* 154 */               ktx = new KTXTextureData(file, false);
/* 155 */               ktx.prepare();
/*     */             } 
/* 157 */             nLevels = ktx.getNumberOfMipMapLevels();
/* 158 */             texWidth = ktx.getWidth();
/* 159 */             texHeight = ktx.getHeight();
/* 160 */           } else if (file.name().toLowerCase().endsWith(".etc1")) {
/* 161 */             etc1 = new ETC1.ETC1Data(file);
/* 162 */             nLevels = 1;
/* 163 */             texWidth = etc1.width;
/* 164 */             texHeight = etc1.height;
/*     */           } else {
/* 166 */             facePixmap = new Pixmap(file);
/* 167 */             nLevels = 1;
/* 168 */             texWidth = facePixmap.getWidth();
/* 169 */             texHeight = facePixmap.getHeight();
/*     */           } 
/* 171 */           if (isGenMipMaps) {
/* 172 */             if (!MathUtils.isPowerOfTwo(texWidth) || !MathUtils.isPowerOfTwo(texHeight)) {
/* 173 */               throw new GdxRuntimeException("Invalid input : mipmap generation is only available for power of two textures : " + file);
/*     */             }
/* 175 */             nLevels = Math.max(32 - Integer.numberOfLeadingZeros(texWidth), 32 - 
/* 176 */                 Integer.numberOfLeadingZeros(texHeight));
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 181 */         images[face] = new KTXProcessor.Image[nLevels];
/* 182 */         for (int j = 0; j < nLevels; j++) {
/* 183 */           int levelWidth = Math.max(1, texWidth >> j);
/* 184 */           int levelHeight = Math.max(1, texHeight >> j);
/*     */ 
/*     */           
/* 187 */           Pixmap levelPixmap = null;
/* 188 */           ETC1.ETC1Data levelETCData = null;
/* 189 */           if (ktx != null) {
/* 190 */             ByteBuffer ktxData = ktx.getData(j, ktxFace);
/* 191 */             if (ktxData != null && ktx.getGlInternalFormat() == ETC1.ETC1_RGB8_OES)
/* 192 */               levelETCData = new ETC1.ETC1Data(levelWidth, levelHeight, ktxData, 0); 
/*     */           } 
/* 194 */           if (ktx != null && levelETCData == null && facePixmap == null) {
/* 195 */             ByteBuffer ktxData = ktx.getData(0, ktxFace);
/* 196 */             if (ktxData != null && ktx.getGlInternalFormat() == ETC1.ETC1_RGB8_OES)
/* 197 */               facePixmap = ETC1.decodeImage(new ETC1.ETC1Data(levelWidth, levelHeight, ktxData, 0), Pixmap.Format.RGB888); 
/*     */           } 
/* 199 */           if (j == 0 && etc1 != null) {
/* 200 */             levelETCData = etc1;
/*     */           }
/* 202 */           if (levelETCData == null && etc1 != null && facePixmap == null) {
/* 203 */             facePixmap = ETC1.decodeImage(etc1, Pixmap.Format.RGB888);
/*     */           }
/* 205 */           if (levelETCData == null) {
/* 206 */             levelPixmap = new Pixmap(levelWidth, levelHeight, facePixmap.getFormat());
/* 207 */             levelPixmap.drawPixmap(facePixmap, 0, 0, facePixmap.getWidth(), facePixmap.getHeight(), 0, 0, levelPixmap
/* 208 */                 .getWidth(), levelPixmap.getHeight());
/*     */           } 
/* 210 */           if (levelETCData == null && levelPixmap == null) {
/* 211 */             throw new GdxRuntimeException("Failed to load data for face " + face + " / mipmap level " + j);
/*     */           }
/*     */           
/* 214 */           if (isAlphaAtlas) {
/* 215 */             if (levelPixmap == null) levelPixmap = ETC1.decodeImage(levelETCData, Pixmap.Format.RGB888); 
/* 216 */             int w = levelPixmap.getWidth(), h = levelPixmap.getHeight();
/* 217 */             Pixmap pm = new Pixmap(w, h * 2, levelPixmap.getFormat());
/* 218 */             pm.drawPixmap(levelPixmap, 0, 0);
/* 219 */             for (int y = 0; y < h; y++) {
/* 220 */               for (int x = 0; x < w; x++) {
/* 221 */                 int alpha = levelPixmap.getPixel(x, y) & 0xFF;
/* 222 */                 pm.drawPixel(x, y + h, alpha << 24 | alpha << 16 | alpha << 8 | 0xFF);
/*     */               } 
/*     */             } 
/* 225 */             levelPixmap.dispose();
/* 226 */             levelPixmap = pm;
/* 227 */             levelETCData = null;
/*     */           } 
/*     */ 
/*     */           
/* 231 */           if (levelETCData == null && isPackETC1) {
/* 232 */             if (levelPixmap.getFormat() != Pixmap.Format.RGB888 && levelPixmap.getFormat() != Pixmap.Format.RGB565) {
/* 233 */               if (!isAlphaAtlas)
/* 234 */                 System.out.println("Converting from " + levelPixmap.getFormat() + " to RGB888 for ETC1 compression"); 
/* 235 */               Pixmap tmp = new Pixmap(levelPixmap.getWidth(), levelPixmap.getHeight(), Pixmap.Format.RGB888);
/* 236 */               tmp.drawPixmap(levelPixmap, 0, 0, 0, 0, levelPixmap.getWidth(), levelPixmap.getHeight());
/* 237 */               levelPixmap.dispose();
/* 238 */               levelPixmap = tmp;
/*     */             } 
/*     */             
/* 241 */             levelETCData = ETC1.encodeImagePKM(levelPixmap);
/* 242 */             levelPixmap.dispose();
/* 243 */             levelPixmap = null;
/*     */           } 
/*     */ 
/*     */           
/* 247 */           images[face][j] = new KTXProcessor.Image();
/* 248 */           (images[face][j]).etcData = levelETCData;
/* 249 */           (images[face][j]).pixmap = levelPixmap;
/* 250 */           if (levelPixmap != null) {
/* 251 */             levelPixmap.dispose();
/* 252 */             facePixmap = null;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 257 */         if (facePixmap != null) {
/* 258 */           facePixmap.dispose();
/* 259 */           facePixmap = null;
/*     */         } 
/* 261 */         if (etc1 != null) {
/* 262 */           etc1.dispose();
/* 263 */           etc1 = null;
/*     */         } 
/* 265 */         if (ktx != null && ktxDispose == 2) {
/* 266 */           ktx.disposePreparedData();
/* 267 */           ktx = null;
/*     */         } 
/*     */       } 
/* 270 */       if (ktx != null) {
/* 271 */         ktx.disposePreparedData();
/* 272 */         ktx = null;
/*     */       } 
/*     */ 
/*     */       
/* 276 */       if (isPackETC1) {
/* 277 */         glType = glFormat = 0;
/* 278 */         glTypeSize = 1;
/* 279 */         glInternalFormat = ETC1.ETC1_RGB8_OES;
/* 280 */         glBaseInternalFormat = 6407;
/* 281 */       } else if ((images[0][0]).pixmap != null) {
/* 282 */         glType = (images[0][0]).pixmap.getGLType();
/* 283 */         glTypeSize = 1;
/* 284 */         glFormat = (images[0][0]).pixmap.getGLFormat();
/* 285 */         glInternalFormat = (images[0][0]).pixmap.getGLInternalFormat();
/* 286 */         glBaseInternalFormat = glFormat;
/*     */       } else {
/* 288 */         throw new GdxRuntimeException("Unsupported output format");
/*     */       } 
/* 290 */       int totalSize = 64;
/* 291 */       for (int level = 0; level < nLevels; level++) {
/* 292 */         System.out.println("Level: " + level);
/* 293 */         int faceLodSize = images[0][level].getSize();
/* 294 */         int faceLodSizeRounded = faceLodSize + 3 & 0xFFFFFFFC;
/* 295 */         totalSize += 4;
/* 296 */         totalSize += nFaces * faceLodSizeRounded;
/*     */       } 
/*     */       
/*     */       try {
/*     */         DataOutputStream out;
/* 301 */         if (output.getName().toLowerCase().endsWith(".zktx")) {
/* 302 */           out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(output)));
/* 303 */           out.writeInt(totalSize);
/*     */         } else {
/* 305 */           out = new DataOutputStream(new FileOutputStream(output));
/*     */         } 
/* 307 */         out.write(KTXProcessor.HEADER_MAGIC);
/* 308 */         out.writeInt(67305985);
/* 309 */         out.writeInt(glType);
/* 310 */         out.writeInt(glTypeSize);
/* 311 */         out.writeInt(glFormat);
/* 312 */         out.writeInt(glInternalFormat);
/* 313 */         out.writeInt(glBaseInternalFormat);
/* 314 */         out.writeInt(texWidth);
/* 315 */         out.writeInt(isAlphaAtlas ? (2 * texHeight) : texHeight);
/* 316 */         out.writeInt(0);
/* 317 */         out.writeInt(0);
/* 318 */         out.writeInt(nFaces);
/* 319 */         out.writeInt(nLevels);
/* 320 */         out.writeInt(0);
/* 321 */         for (int j = 0; j < nLevels; j++) {
/* 322 */           int faceLodSize = images[0][j].getSize();
/* 323 */           int faceLodSizeRounded = faceLodSize + 3 & 0xFFFFFFFC;
/* 324 */           out.writeInt(faceLodSize);
/* 325 */           for (int k = 0; k < nFaces; k++) {
/* 326 */             byte[] bytes = images[k][j].getBytes();
/* 327 */             out.write(bytes);
/* 328 */             for (int m = bytes.length; m < faceLodSizeRounded; m++) {
/* 329 */               out.write(0);
/*     */             }
/*     */           } 
/*     */         } 
/* 333 */         out.close();
/* 334 */       } catch (Exception e) {
/* 335 */         Gdx.app.error("KTXProcessor", "Error writing to file: " + output.getName(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Image
/*     */   {
/*     */     public ETC1.ETC1Data etcData;
/*     */     
/*     */     public Pixmap pixmap;
/*     */ 
/*     */     
/*     */     public int getSize() {
/* 349 */       if (this.etcData != null) return this.etcData.compressedData.limit() - this.etcData.dataOffset; 
/* 350 */       throw new GdxRuntimeException("Unsupported output format, try adding '-etc1' as argument");
/*     */     }
/*     */     
/*     */     public byte[] getBytes() {
/* 354 */       if (this.etcData != null) {
/* 355 */         byte[] result = new byte[getSize()];
/* 356 */         this.etcData.compressedData.position(this.etcData.dataOffset);
/* 357 */         this.etcData.compressedData.get(result);
/* 358 */         return result;
/*     */       } 
/* 360 */       throw new GdxRuntimeException("Unsupported output format, try adding '-etc1' as argument");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\ktx\KTXProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */