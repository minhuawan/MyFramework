/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.audio.Sound;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ public class OpenALSound
/*     */   implements Sound
/*     */ {
/*  28 */   private int bufferID = -1;
/*     */   private final OpenALAudio audio;
/*     */   private float duration;
/*     */   
/*     */   public OpenALSound(OpenALAudio audio) {
/*  33 */     this.audio = audio;
/*     */   }
/*     */   
/*     */   void setup(byte[] pcm, int channels, int sampleRate) {
/*  37 */     int bytes = pcm.length - pcm.length % ((channels > 1) ? 4 : 2);
/*  38 */     int samples = bytes / 2 * channels;
/*  39 */     this.duration = samples / sampleRate;
/*     */     
/*  41 */     ByteBuffer buffer = ByteBuffer.allocateDirect(bytes);
/*  42 */     buffer.order(ByteOrder.nativeOrder());
/*  43 */     buffer.put(pcm, 0, bytes);
/*  44 */     buffer.flip();
/*     */     
/*  46 */     if (this.bufferID == -1) {
/*  47 */       this.bufferID = AL10.alGenBuffers();
/*  48 */       AL10.alBufferData(this.bufferID, (channels > 1) ? 4355 : 4353, buffer.asShortBuffer(), sampleRate);
/*     */     } 
/*     */   }
/*     */   
/*     */   public long play() {
/*  53 */     return play(1.0F);
/*     */   }
/*     */   
/*     */   public long play(float volume) {
/*  57 */     if (this.audio.noDevice) return 0L; 
/*  58 */     int sourceID = this.audio.obtainSource(false);
/*  59 */     if (sourceID == -1)
/*     */     
/*  61 */     { this.audio.retain(this, true);
/*  62 */       sourceID = this.audio.obtainSource(false); }
/*  63 */     else { this.audio.retain(this, false); }
/*     */     
/*  65 */     if (sourceID == -1) return -1L; 
/*  66 */     long soundId = this.audio.getSoundId(sourceID);
/*  67 */     AL10.alSourcei(sourceID, 4105, this.bufferID);
/*  68 */     AL10.alSourcei(sourceID, 4103, 0);
/*  69 */     AL10.alSourcef(sourceID, 4106, volume);
/*  70 */     AL10.alSourcePlay(sourceID);
/*  71 */     return soundId;
/*     */   }
/*     */   
/*     */   public long loop() {
/*  75 */     return loop(1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public long loop(float volume) {
/*  80 */     if (this.audio.noDevice) return 0L; 
/*  81 */     int sourceID = this.audio.obtainSource(false);
/*  82 */     if (sourceID == -1) return -1L; 
/*  83 */     long soundId = this.audio.getSoundId(sourceID);
/*  84 */     AL10.alSourcei(sourceID, 4105, this.bufferID);
/*  85 */     AL10.alSourcei(sourceID, 4103, 1);
/*  86 */     AL10.alSourcef(sourceID, 4106, volume);
/*  87 */     AL10.alSourcePlay(sourceID);
/*  88 */     return soundId;
/*     */   }
/*     */   
/*     */   public void stop() {
/*  92 */     if (this.audio.noDevice)
/*  93 */       return;  this.audio.stopSourcesWithBuffer(this.bufferID);
/*     */   }
/*     */   
/*     */   public void dispose() {
/*  97 */     if (this.audio.noDevice)
/*  98 */       return;  if (this.bufferID == -1)
/*  99 */       return;  this.audio.freeBuffer(this.bufferID);
/* 100 */     AL10.alDeleteBuffers(this.bufferID);
/* 101 */     this.bufferID = -1;
/* 102 */     this.audio.forget(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop(long soundId) {
/* 107 */     if (this.audio.noDevice)
/* 108 */       return;  this.audio.stopSound(soundId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pause() {
/* 113 */     if (this.audio.noDevice)
/* 114 */       return;  this.audio.pauseSourcesWithBuffer(this.bufferID);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pause(long soundId) {
/* 119 */     if (this.audio.noDevice)
/* 120 */       return;  this.audio.pauseSound(soundId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resume() {
/* 125 */     if (this.audio.noDevice)
/* 126 */       return;  this.audio.resumeSourcesWithBuffer(this.bufferID);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resume(long soundId) {
/* 131 */     if (this.audio.noDevice)
/* 132 */       return;  this.audio.resumeSound(soundId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPitch(long soundId, float pitch) {
/* 137 */     if (this.audio.noDevice)
/* 138 */       return;  this.audio.setSoundPitch(soundId, pitch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVolume(long soundId, float volume) {
/* 143 */     if (this.audio.noDevice)
/* 144 */       return;  this.audio.setSoundGain(soundId, volume);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLooping(long soundId, boolean looping) {
/* 149 */     if (this.audio.noDevice)
/* 150 */       return;  this.audio.setSoundLooping(soundId, looping);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPan(long soundId, float pan, float volume) {
/* 155 */     if (this.audio.noDevice)
/* 156 */       return;  this.audio.setSoundPan(soundId, pan, volume);
/*     */   }
/*     */ 
/*     */   
/*     */   public long play(float volume, float pitch, float pan) {
/* 161 */     long id = play();
/* 162 */     setPitch(id, pitch);
/* 163 */     setPan(id, pan, volume);
/* 164 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   public long loop(float volume, float pitch, float pan) {
/* 169 */     long id = loop();
/* 170 */     setPitch(id, pitch);
/* 171 */     setPan(id, pan, volume);
/* 172 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   public float duration() {
/* 177 */     return this.duration;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\OpenALSound.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */