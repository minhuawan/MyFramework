/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
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
/*     */ public class Wav
/*     */ {
/*     */   public static class Music
/*     */     extends OpenALMusic
/*     */   {
/*     */     private Wav.WavInputStream input;
/*     */     
/*     */     public Music(OpenALAudio audio, FileHandle file) {
/*  32 */       super(audio, file);
/*  33 */       this.input = new Wav.WavInputStream(file);
/*  34 */       if (audio.noDevice)
/*  35 */         return;  setup(this.input.channels, this.input.sampleRate);
/*     */     }
/*     */     
/*     */     public int read(byte[] buffer) {
/*  39 */       if (this.input == null) {
/*  40 */         this.input = new Wav.WavInputStream(this.file);
/*  41 */         setup(this.input.channels, this.input.sampleRate);
/*     */       } 
/*     */       try {
/*  44 */         return this.input.read(buffer);
/*  45 */       } catch (IOException ex) {
/*  46 */         throw new GdxRuntimeException("Error reading WAV file: " + this.file, ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void reset() {
/*  51 */       StreamUtils.closeQuietly(this.input);
/*  52 */       this.input = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Sound extends OpenALSound {
/*     */     public Sound(OpenALAudio audio, FileHandle file) {
/*  58 */       super(audio);
/*  59 */       if (audio.noDevice)
/*     */         return; 
/*  61 */       Wav.WavInputStream input = null;
/*     */       try {
/*  63 */         input = new Wav.WavInputStream(file);
/*  64 */         setup(StreamUtils.copyStreamToByteArray(input, input.dataRemaining), input.channels, input.sampleRate);
/*  65 */       } catch (IOException ex) {
/*  66 */         throw new GdxRuntimeException("Error reading WAV file: " + file, ex);
/*     */       } finally {
/*  68 */         StreamUtils.closeQuietly(input);
/*     */       } 
/*     */     } }
/*     */   
/*     */   private static class WavInputStream extends FilterInputStream {
/*     */     int channels;
/*     */     int sampleRate;
/*     */     int dataRemaining;
/*     */     
/*     */     WavInputStream(FileHandle file) {
/*  78 */       super(file.read());
/*     */       try {
/*  80 */         if (read() != 82 || read() != 73 || read() != 70 || read() != 70) {
/*  81 */           throw new GdxRuntimeException("RIFF header not found: " + file);
/*     */         }
/*  83 */         skipFully(4);
/*     */         
/*  85 */         if (read() != 87 || read() != 65 || read() != 86 || read() != 69) {
/*  86 */           throw new GdxRuntimeException("Invalid wave file header: " + file);
/*     */         }
/*  88 */         int fmtChunkLength = seekToChunk('f', 'm', 't', ' ');
/*     */         
/*  90 */         int type = read() & 0xFF | (read() & 0xFF) << 8;
/*  91 */         if (type != 1) throw new GdxRuntimeException("WAV files must be PCM: " + type);
/*     */         
/*  93 */         this.channels = read() & 0xFF | (read() & 0xFF) << 8;
/*  94 */         if (this.channels != 1 && this.channels != 2) {
/*  95 */           throw new GdxRuntimeException("WAV files must have 1 or 2 channels: " + this.channels);
/*     */         }
/*  97 */         this.sampleRate = read() & 0xFF | (read() & 0xFF) << 8 | (read() & 0xFF) << 16 | (read() & 0xFF) << 24;
/*     */         
/*  99 */         skipFully(6);
/*     */         
/* 101 */         int bitsPerSample = read() & 0xFF | (read() & 0xFF) << 8;
/* 102 */         if (bitsPerSample != 16) throw new GdxRuntimeException("WAV files must have 16 bits per sample: " + bitsPerSample);
/*     */         
/* 104 */         skipFully(fmtChunkLength - 16);
/*     */         
/* 106 */         this.dataRemaining = seekToChunk('d', 'a', 't', 'a');
/* 107 */       } catch (Throwable ex) {
/* 108 */         StreamUtils.closeQuietly(this);
/* 109 */         throw new GdxRuntimeException("Error reading WAV file: " + file, ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private int seekToChunk(char c1, char c2, char c3, char c4) throws IOException {
/*     */       while (true) {
/* 115 */         boolean found = (read() == c1);
/* 116 */         int i = found & ((read() == c2) ? 1 : 0);
/* 117 */         i &= (read() == c3) ? 1 : 0;
/* 118 */         i &= (read() == c4) ? 1 : 0;
/* 119 */         int chunkLength = read() & 0xFF | (read() & 0xFF) << 8 | (read() & 0xFF) << 16 | (read() & 0xFF) << 24;
/* 120 */         if (chunkLength == -1) throw new IOException("Chunk not found: " + c1 + c2 + c3 + c4); 
/* 121 */         if (i != 0) return chunkLength; 
/* 122 */         skipFully(chunkLength);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void skipFully(int count) throws IOException {
/* 127 */       while (count > 0) {
/* 128 */         long skipped = this.in.skip(count);
/* 129 */         if (skipped <= 0L) throw new EOFException("Unable to skip."); 
/* 130 */         count = (int)(count - skipped);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int read(byte[] buffer) throws IOException {
/* 135 */       if (this.dataRemaining == 0) return -1; 
/* 136 */       int length = Math.min(super.read(buffer), this.dataRemaining);
/* 137 */       if (length == -1) return -1; 
/* 138 */       this.dataRemaining -= length;
/* 139 */       return length;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\Wav.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */