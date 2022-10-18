/*     */ package com.badlogic.gdx.utils.compression;
/*     */ 
/*     */ import com.badlogic.gdx.utils.compression.lzma.Decoder;
/*     */ import com.badlogic.gdx.utils.compression.lzma.Encoder;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public class Lzma
/*     */ {
/*     */   static class CommandLine
/*     */   {
/*     */     public static final int kEncode = 0;
/*     */     public static final int kDecode = 1;
/*     */     public static final int kBenchmak = 2;
/*  34 */     public int Command = -1;
/*  35 */     public int NumBenchmarkPasses = 10;
/*     */     
/*  37 */     public int DictionarySize = 8388608;
/*     */     
/*     */     public boolean DictionarySizeIsDefined = false;
/*  40 */     public int Lc = 3;
/*  41 */     public int Lp = 0;
/*  42 */     public int Pb = 2;
/*     */     
/*  44 */     public int Fb = 128;
/*     */     
/*     */     public boolean FbIsDefined = false;
/*     */     
/*     */     public boolean Eos = false;
/*  49 */     public int Algorithm = 2;
/*  50 */     public int MatchFinder = 1;
/*     */ 
/*     */     
/*     */     public String InFile;
/*     */ 
/*     */     
/*     */     public String OutFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void compress(InputStream in, OutputStream out) throws IOException {
/*     */     long fileSize;
/*  62 */     CommandLine params = new CommandLine();
/*  63 */     boolean eos = false;
/*  64 */     if (params.Eos) eos = true; 
/*  65 */     Encoder encoder = new Encoder();
/*  66 */     if (!encoder.SetAlgorithm(params.Algorithm)) throw new RuntimeException("Incorrect compression mode"); 
/*  67 */     if (!encoder.SetDictionarySize(params.DictionarySize)) throw new RuntimeException("Incorrect dictionary size"); 
/*  68 */     if (!encoder.SetNumFastBytes(params.Fb)) throw new RuntimeException("Incorrect -fb value"); 
/*  69 */     if (!encoder.SetMatchFinder(params.MatchFinder)) throw new RuntimeException("Incorrect -mf value"); 
/*  70 */     if (!encoder.SetLcLpPb(params.Lc, params.Lp, params.Pb)) throw new RuntimeException("Incorrect -lc or -lp or -pb value"); 
/*  71 */     encoder.SetEndMarkerMode(eos);
/*  72 */     encoder.WriteCoderProperties(out);
/*     */     
/*  74 */     if (eos) {
/*  75 */       fileSize = -1L;
/*     */     }
/*  77 */     else if ((fileSize = in.available()) == 0L) {
/*  78 */       fileSize = -1L;
/*     */     } 
/*     */     
/*  81 */     for (int i = 0; i < 8; i++) {
/*  82 */       out.write((int)(fileSize >>> 8 * i) & 0xFF);
/*     */     }
/*  84 */     encoder.Code(in, out, -1L, -1L, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void decompress(InputStream in, OutputStream out) throws IOException {
/*  93 */     int propertiesSize = 5;
/*  94 */     byte[] properties = new byte[propertiesSize];
/*  95 */     if (in.read(properties, 0, propertiesSize) != propertiesSize) throw new RuntimeException("input .lzma file is too short"); 
/*  96 */     Decoder decoder = new Decoder();
/*  97 */     if (!decoder.SetDecoderProperties(properties)) throw new RuntimeException("Incorrect stream properties"); 
/*  98 */     long outSize = 0L;
/*  99 */     for (int i = 0; i < 8; i++) {
/* 100 */       int v = in.read();
/* 101 */       if (v < 0) {
/* 102 */         throw new RuntimeException("Can't read stream size");
/*     */       }
/* 104 */       outSize |= v << 8 * i;
/*     */     } 
/* 106 */     if (!decoder.Code(in, out, outSize))
/* 107 */       throw new RuntimeException("Error in data stream"); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\Lzma.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */