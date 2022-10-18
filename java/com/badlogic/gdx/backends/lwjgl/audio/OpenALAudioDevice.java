/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.audio.AudioDevice;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.openal.AL10;
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
/*     */ public class OpenALAudioDevice
/*     */   implements AudioDevice
/*     */ {
/*     */   private static final int bytesPerSample = 2;
/*     */   private final OpenALAudio audio;
/*     */   private final int channels;
/*     */   private IntBuffer buffers;
/*     */   private int format;
/*     */   private int sampleRate;
/*  38 */   private int sourceID = -1;
/*     */   
/*     */   private boolean isPlaying;
/*  41 */   private float volume = 1.0F;
/*     */   
/*     */   private float renderedSeconds;
/*     */   
/*     */   private float secondsPerBuffer;
/*     */   private byte[] bytes;
/*     */   
/*     */   public OpenALAudioDevice(OpenALAudio audio, int sampleRate, boolean isMono, int bufferSize, int bufferCount) {
/*  49 */     this.audio = audio;
/*  50 */     this.channels = isMono ? 1 : 2;
/*  51 */     this.bufferSize = bufferSize;
/*  52 */     this.bufferCount = bufferCount;
/*  53 */     this.format = (this.channels > 1) ? 4355 : 4353;
/*  54 */     this.sampleRate = sampleRate;
/*  55 */     this.secondsPerBuffer = bufferSize / 2.0F / this.channels / sampleRate;
/*  56 */     this.tempBuffer = BufferUtils.createByteBuffer(bufferSize);
/*     */   }
/*     */   private final int bufferSize; private final int bufferCount; private final ByteBuffer tempBuffer;
/*     */   public void writeSamples(short[] samples, int offset, int numSamples) {
/*  60 */     if (this.bytes == null || this.bytes.length < numSamples * 2) this.bytes = new byte[numSamples * 2]; 
/*  61 */     int end = Math.min(offset + numSamples, samples.length);
/*  62 */     for (int i = offset, ii = 0; i < end; i++) {
/*  63 */       short sample = samples[i];
/*  64 */       this.bytes[ii++] = (byte)(sample & 0xFF);
/*  65 */       this.bytes[ii++] = (byte)(sample >> 8 & 0xFF);
/*     */     } 
/*  67 */     writeSamples(this.bytes, 0, numSamples * 2);
/*     */   }
/*     */   
/*     */   public void writeSamples(float[] samples, int offset, int numSamples) {
/*  71 */     if (this.bytes == null || this.bytes.length < numSamples * 2) this.bytes = new byte[numSamples * 2]; 
/*  72 */     int end = Math.min(offset + numSamples, samples.length);
/*  73 */     for (int i = offset, ii = 0; i < end; i++) {
/*  74 */       float floatSample = samples[i];
/*  75 */       floatSample = MathUtils.clamp(floatSample, -1.0F, 1.0F);
/*  76 */       int intSample = (int)(floatSample * 32767.0F);
/*  77 */       this.bytes[ii++] = (byte)(intSample & 0xFF);
/*  78 */       this.bytes[ii++] = (byte)(intSample >> 8 & 0xFF);
/*     */     } 
/*  80 */     writeSamples(this.bytes, 0, numSamples * 2);
/*     */   }
/*     */   
/*     */   public void writeSamples(byte[] data, int offset, int length) {
/*  84 */     if (length < 0) throw new IllegalArgumentException("length cannot be < 0.");
/*     */     
/*  86 */     if (this.sourceID == -1) {
/*  87 */       this.sourceID = this.audio.obtainSource(true);
/*  88 */       if (this.sourceID == -1)
/*  89 */         return;  if (this.buffers == null) {
/*  90 */         this.buffers = BufferUtils.createIntBuffer(this.bufferCount);
/*  91 */         AL10.alGenBuffers(this.buffers);
/*  92 */         if (AL10.alGetError() != 0) throw new GdxRuntimeException("Unabe to allocate audio buffers."); 
/*     */       } 
/*  94 */       AL10.alSourcei(this.sourceID, 4103, 0);
/*  95 */       AL10.alSourcef(this.sourceID, 4106, this.volume);
/*     */       
/*  97 */       int queuedBuffers = 0; int i;
/*  98 */       for (i = 0; i < this.bufferCount; i++) {
/*  99 */         int bufferID = this.buffers.get(i);
/* 100 */         int written = Math.min(this.bufferSize, length);
/* 101 */         this.tempBuffer.clear();
/* 102 */         this.tempBuffer.put(data, offset, written).flip();
/* 103 */         AL10.alBufferData(bufferID, this.format, this.tempBuffer, this.sampleRate);
/* 104 */         AL10.alSourceQueueBuffers(this.sourceID, bufferID);
/* 105 */         length -= written;
/* 106 */         offset += written;
/* 107 */         queuedBuffers++;
/*     */       } 
/*     */       
/* 110 */       this.tempBuffer.clear().flip();
/* 111 */       for (i = queuedBuffers; i < this.bufferCount; i++) {
/* 112 */         int bufferID = this.buffers.get(i);
/* 113 */         AL10.alBufferData(bufferID, this.format, this.tempBuffer, this.sampleRate);
/* 114 */         AL10.alSourceQueueBuffers(this.sourceID, bufferID);
/*     */       } 
/* 116 */       AL10.alSourcePlay(this.sourceID);
/* 117 */       this.isPlaying = true;
/*     */     } 
/*     */     
/* 120 */     while (length > 0) {
/* 121 */       int written = fillBuffer(data, offset, length);
/* 122 */       length -= written;
/* 123 */       offset += written;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int fillBuffer(byte[] data, int offset, int length) {
/* 129 */     int written = Math.min(this.bufferSize, length);
/*     */ 
/*     */     
/*     */     while (true) {
/* 133 */       int buffers = AL10.alGetSourcei(this.sourceID, 4118);
/* 134 */       if (buffers-- > 0) {
/* 135 */         int bufferID = AL10.alSourceUnqueueBuffers(this.sourceID);
/* 136 */         if (bufferID != 40963) {
/* 137 */           this.renderedSeconds += this.secondsPerBuffer;
/*     */           
/* 139 */           this.tempBuffer.clear();
/* 140 */           this.tempBuffer.put(data, offset, written).flip();
/* 141 */           AL10.alBufferData(bufferID, this.format, this.tempBuffer, this.sampleRate);
/*     */           
/* 143 */           AL10.alSourceQueueBuffers(this.sourceID, bufferID);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       try {
/* 148 */         Thread.sleep((long)(1000.0F * this.secondsPerBuffer / this.bufferCount));
/* 149 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (!this.isPlaying || AL10.alGetSourcei(this.sourceID, 4112) != 4114) {
/* 155 */       AL10.alSourcePlay(this.sourceID);
/* 156 */       this.isPlaying = true;
/*     */     } 
/*     */     
/* 159 */     return written;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 163 */     if (this.sourceID == -1)
/* 164 */       return;  this.audio.freeSource(this.sourceID);
/* 165 */     this.sourceID = -1;
/* 166 */     this.renderedSeconds = 0.0F;
/* 167 */     this.isPlaying = false;
/*     */   }
/*     */   
/*     */   public boolean isPlaying() {
/* 171 */     if (this.sourceID == -1) return false; 
/* 172 */     return this.isPlaying;
/*     */   }
/*     */   
/*     */   public void setVolume(float volume) {
/* 176 */     this.volume = volume;
/* 177 */     if (this.sourceID != -1) AL10.alSourcef(this.sourceID, 4106, volume); 
/*     */   }
/*     */   
/*     */   public float getPosition() {
/* 181 */     if (this.sourceID == -1) return 0.0F; 
/* 182 */     return this.renderedSeconds + AL10.alGetSourcef(this.sourceID, 4132);
/*     */   }
/*     */   
/*     */   public void setPosition(float position) {
/* 186 */     this.renderedSeconds = position;
/*     */   }
/*     */   
/*     */   public int getChannels() {
/* 190 */     return (this.format == 4355) ? 2 : 1;
/*     */   }
/*     */   
/*     */   public int getRate() {
/* 194 */     return this.sampleRate;
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 198 */     if (this.buffers == null)
/* 199 */       return;  if (this.sourceID != -1) {
/* 200 */       this.audio.freeSource(this.sourceID);
/* 201 */       this.sourceID = -1;
/*     */     } 
/* 203 */     AL10.alDeleteBuffers(this.buffers);
/* 204 */     this.buffers = null;
/*     */   }
/*     */   
/*     */   public boolean isMono() {
/* 208 */     return (this.channels == 1);
/*     */   }
/*     */   
/*     */   public int getLatency() {
/* 212 */     return (int)(this.secondsPerBuffer * this.bufferCount * 1000.0F);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\OpenALAudioDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */