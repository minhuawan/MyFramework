/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import javazoom.jl.decoder.Bitstream;
/*     */ import javazoom.jl.decoder.BitstreamException;
/*     */ import javazoom.jl.decoder.Header;
/*     */ import javazoom.jl.decoder.MP3Decoder;
/*     */ import javazoom.jl.decoder.OutputBuffer;
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
/*     */ public class Mp3
/*     */ {
/*     */   public static class Music
/*     */     extends OpenALMusic
/*     */   {
/*     */     private Bitstream bitstream;
/*     */     private OutputBuffer outputBuffer;
/*     */     private MP3Decoder decoder;
/*     */     
/*     */     public Music(OpenALAudio audio, FileHandle file) {
/*  40 */       super(audio, file);
/*  41 */       if (audio.noDevice)
/*  42 */         return;  this.bitstream = new Bitstream(file.read());
/*  43 */       this.decoder = new MP3Decoder();
/*  44 */       this.bufferOverhead = 4096;
/*     */       try {
/*  46 */         Header header = this.bitstream.readFrame();
/*  47 */         if (header == null) throw new GdxRuntimeException("Empty MP3"); 
/*  48 */         int channels = (header.mode() == 3) ? 1 : 2;
/*  49 */         this.outputBuffer = new OutputBuffer(channels, false);
/*  50 */         this.decoder.setOutputBuffer(this.outputBuffer);
/*  51 */         setup(channels, header.getSampleRate());
/*  52 */       } catch (BitstreamException e) {
/*  53 */         throw new GdxRuntimeException("error while preloading mp3", e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int read(byte[] buffer) {
/*     */       try {
/*  59 */         boolean setup = (this.bitstream == null);
/*  60 */         if (setup) {
/*  61 */           this.bitstream = new Bitstream(this.file.read());
/*  62 */           this.decoder = new MP3Decoder();
/*     */         } 
/*     */         
/*  65 */         int totalLength = 0;
/*  66 */         int minRequiredLength = buffer.length - 4608;
/*  67 */         while (totalLength <= minRequiredLength) {
/*  68 */           Header header = this.bitstream.readFrame();
/*  69 */           if (header == null)
/*  70 */             break;  if (setup) {
/*  71 */             int channels = (header.mode() == 3) ? 1 : 2;
/*  72 */             this.outputBuffer = new OutputBuffer(channels, false);
/*  73 */             this.decoder.setOutputBuffer(this.outputBuffer);
/*  74 */             setup(channels, header.getSampleRate());
/*  75 */             setup = false;
/*     */           } 
/*     */           try {
/*  78 */             this.decoder.decodeFrame(header, this.bitstream);
/*  79 */           } catch (Exception exception) {}
/*     */ 
/*     */           
/*  82 */           this.bitstream.closeFrame();
/*     */           
/*  84 */           int length = this.outputBuffer.reset();
/*  85 */           System.arraycopy(this.outputBuffer.getBuffer(), 0, buffer, totalLength, length);
/*  86 */           totalLength += length;
/*     */         } 
/*  88 */         return totalLength;
/*  89 */       } catch (Throwable ex) {
/*  90 */         reset();
/*  91 */         throw new GdxRuntimeException("Error reading audio data.", ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void reset() {
/*  96 */       if (this.bitstream == null)
/*     */         return;  try {
/*  98 */         this.bitstream.close();
/*  99 */       } catch (BitstreamException bitstreamException) {}
/*     */       
/* 101 */       this.bitstream = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Sound
/*     */     extends OpenALSound
/*     */   {
/*     */     public Sound(OpenALAudio audio, FileHandle file) {
/* 109 */       super(audio);
/* 110 */       if (audio.noDevice)
/* 111 */         return;  ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
/*     */       
/* 113 */       Bitstream bitstream = new Bitstream(file.read());
/* 114 */       MP3Decoder decoder = new MP3Decoder();
/*     */       
/*     */       try {
/* 117 */         OutputBuffer outputBuffer = null;
/* 118 */         int sampleRate = -1, channels = -1;
/*     */         while (true) {
/* 120 */           Header header = bitstream.readFrame();
/* 121 */           if (header == null)
/* 122 */             break;  if (outputBuffer == null) {
/* 123 */             channels = (header.mode() == 3) ? 1 : 2;
/* 124 */             outputBuffer = new OutputBuffer(channels, false);
/* 125 */             decoder.setOutputBuffer(outputBuffer);
/* 126 */             sampleRate = header.getSampleRate();
/*     */           } 
/*     */           try {
/* 129 */             decoder.decodeFrame(header, bitstream);
/* 130 */           } catch (Exception exception) {}
/*     */ 
/*     */           
/* 133 */           bitstream.closeFrame();
/* 134 */           output.write(outputBuffer.getBuffer(), 0, outputBuffer.reset());
/*     */         } 
/* 136 */         bitstream.close();
/* 137 */         setup(output.toByteArray(), channels, sampleRate);
/* 138 */       } catch (Throwable ex) {
/* 139 */         throw new GdxRuntimeException("Error reading audio data.", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\Mp3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */