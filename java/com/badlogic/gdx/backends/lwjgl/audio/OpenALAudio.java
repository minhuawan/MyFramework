/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.Audio;
/*     */ import com.badlogic.gdx.audio.AudioDevice;
/*     */ import com.badlogic.gdx.audio.AudioRecorder;
/*     */ import com.badlogic.gdx.audio.Music;
/*     */ import com.badlogic.gdx.audio.Sound;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.IntMap;
/*     */ import com.badlogic.gdx.utils.LongMap;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import java.nio.FloatBuffer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.openal.AL;
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
/*     */ public class OpenALAudio
/*     */   implements Audio
/*     */ {
/*     */   private final int deviceBufferSize;
/*     */   private final int deviceBufferCount;
/*     */   private IntArray idleSources;
/*     */   private IntArray allSources;
/*     */   private LongMap<Integer> soundIdToSource;
/*     */   private IntMap<Long> sourceToSoundId;
/*  47 */   private long nextSoundId = 0L;
/*  48 */   private ObjectMap<String, Class<? extends OpenALSound>> extensionToSoundClass = new ObjectMap();
/*  49 */   private ObjectMap<String, Class<? extends OpenALMusic>> extensionToMusicClass = new ObjectMap();
/*     */   private OpenALSound[] recentSounds;
/*  51 */   private int mostRecetSound = -1;
/*     */   
/*  53 */   Array<OpenALMusic> music = new Array(false, 1, OpenALMusic.class);
/*     */   boolean noDevice = false;
/*     */   
/*     */   public OpenALAudio() {
/*  57 */     this(16, 9, 512);
/*     */   }
/*     */   
/*     */   public OpenALAudio(int simultaneousSources, int deviceBufferCount, int deviceBufferSize) {
/*  61 */     this.deviceBufferSize = deviceBufferSize;
/*  62 */     this.deviceBufferCount = deviceBufferCount;
/*     */     
/*  64 */     registerSound("ogg", (Class)Ogg.Sound.class);
/*  65 */     registerMusic("ogg", (Class)Ogg.Music.class);
/*  66 */     registerSound("wav", (Class)Wav.Sound.class);
/*  67 */     registerMusic("wav", (Class)Wav.Music.class);
/*  68 */     registerSound("mp3", (Class)Mp3.Sound.class);
/*  69 */     registerMusic("mp3", (Class)Mp3.Music.class);
/*     */     
/*     */     try {
/*  72 */       AL.create();
/*  73 */     } catch (LWJGLException ex) {
/*  74 */       this.noDevice = true;
/*  75 */       ex.printStackTrace();
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     this.allSources = new IntArray(false, simultaneousSources);
/*  80 */     for (int i = 0; i < simultaneousSources; i++) {
/*  81 */       int sourceID = AL10.alGenSources();
/*  82 */       if (AL10.alGetError() != 0)
/*  83 */         break;  this.allSources.add(sourceID);
/*     */     } 
/*  85 */     this.idleSources = new IntArray(this.allSources);
/*  86 */     this.soundIdToSource = new LongMap();
/*  87 */     this.sourceToSoundId = new IntMap();
/*     */ 
/*     */     
/*  90 */     FloatBuffer orientation = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F }).flip();
/*  91 */     AL10.alListener(4111, orientation);
/*  92 */     FloatBuffer velocity = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0F, 0.0F, 0.0F }).flip();
/*  93 */     AL10.alListener(4102, velocity);
/*  94 */     FloatBuffer position = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0F, 0.0F, 0.0F }).flip();
/*  95 */     AL10.alListener(4100, position);
/*     */     
/*  97 */     this.recentSounds = new OpenALSound[simultaneousSources];
/*     */   }
/*     */   
/*     */   public void registerSound(String extension, Class<? extends OpenALSound> soundClass) {
/* 101 */     if (extension == null) throw new IllegalArgumentException("extension cannot be null."); 
/* 102 */     if (soundClass == null) throw new IllegalArgumentException("soundClass cannot be null."); 
/* 103 */     this.extensionToSoundClass.put(extension, soundClass);
/*     */   }
/*     */   
/*     */   public void registerMusic(String extension, Class<? extends OpenALMusic> musicClass) {
/* 107 */     if (extension == null) throw new IllegalArgumentException("extension cannot be null."); 
/* 108 */     if (musicClass == null) throw new IllegalArgumentException("musicClass cannot be null."); 
/* 109 */     this.extensionToMusicClass.put(extension, musicClass);
/*     */   }
/*     */   
/*     */   public OpenALSound newSound(FileHandle file) {
/* 113 */     if (file == null) throw new IllegalArgumentException("file cannot be null."); 
/* 114 */     Class<? extends OpenALSound> soundClass = (Class<? extends OpenALSound>)this.extensionToSoundClass.get(file.extension().toLowerCase());
/* 115 */     if (soundClass == null) throw new GdxRuntimeException("Unknown file extension for sound: " + file); 
/*     */     try {
/* 117 */       return soundClass.getConstructor(new Class[] { OpenALAudio.class, FileHandle.class }).newInstance(new Object[] { this, file });
/* 118 */     } catch (Exception ex) {
/* 119 */       throw new GdxRuntimeException("Error creating sound " + soundClass.getName() + " for file: " + file, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public OpenALMusic newMusic(FileHandle file) {
/* 124 */     if (file == null) throw new IllegalArgumentException("file cannot be null."); 
/* 125 */     Class<? extends OpenALMusic> musicClass = (Class<? extends OpenALMusic>)this.extensionToMusicClass.get(file.extension().toLowerCase());
/* 126 */     if (musicClass == null) throw new GdxRuntimeException("Unknown file extension for music: " + file); 
/*     */     try {
/* 128 */       return musicClass.getConstructor(new Class[] { OpenALAudio.class, FileHandle.class }).newInstance(new Object[] { this, file });
/* 129 */     } catch (Exception ex) {
/* 130 */       throw new GdxRuntimeException("Error creating music " + musicClass.getName() + " for file: " + file, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   int obtainSource(boolean isMusic) {
/* 135 */     if (this.noDevice) return 0; 
/* 136 */     for (int i = 0, n = this.idleSources.size; i < n; i++) {
/* 137 */       int sourceId = this.idleSources.get(i);
/* 138 */       int state = AL10.alGetSourcei(sourceId, 4112);
/* 139 */       if (state != 4114 && state != 4115) {
/* 140 */         if (isMusic) {
/* 141 */           this.idleSources.removeIndex(i);
/*     */         } else {
/* 143 */           if (this.sourceToSoundId.containsKey(sourceId)) {
/* 144 */             long l = ((Long)this.sourceToSoundId.get(sourceId)).longValue();
/* 145 */             this.sourceToSoundId.remove(sourceId);
/* 146 */             this.soundIdToSource.remove(l);
/*     */           } 
/*     */           
/* 149 */           long soundId = this.nextSoundId++;
/* 150 */           this.sourceToSoundId.put(sourceId, Long.valueOf(soundId));
/* 151 */           this.soundIdToSource.put(soundId, Integer.valueOf(sourceId));
/*     */         } 
/* 153 */         AL10.alSourceStop(sourceId);
/* 154 */         AL10.alSourcei(sourceId, 4105, 0);
/* 155 */         AL10.alSourcef(sourceId, 4106, 1.0F);
/* 156 */         AL10.alSourcef(sourceId, 4099, 1.0F);
/* 157 */         AL10.alSource3f(sourceId, 4100, 0.0F, 0.0F, 1.0F);
/* 158 */         return sourceId;
/*     */       } 
/*     */     } 
/* 161 */     return -1;
/*     */   }
/*     */   
/*     */   void freeSource(int sourceID) {
/* 165 */     if (this.noDevice)
/* 166 */       return;  AL10.alSourceStop(sourceID);
/* 167 */     AL10.alSourcei(sourceID, 4105, 0);
/* 168 */     if (this.sourceToSoundId.containsKey(sourceID)) {
/* 169 */       long soundId = ((Long)this.sourceToSoundId.remove(sourceID)).longValue();
/* 170 */       this.soundIdToSource.remove(soundId);
/*     */     } 
/* 172 */     this.idleSources.add(sourceID);
/*     */   }
/*     */   
/*     */   void freeBuffer(int bufferID) {
/* 176 */     if (this.noDevice)
/* 177 */       return;  for (int i = 0, n = this.idleSources.size; i < n; i++) {
/* 178 */       int sourceID = this.idleSources.get(i);
/* 179 */       if (AL10.alGetSourcei(sourceID, 4105) == bufferID) {
/* 180 */         if (this.sourceToSoundId.containsKey(sourceID)) {
/* 181 */           long soundId = ((Long)this.sourceToSoundId.remove(sourceID)).longValue();
/* 182 */           this.soundIdToSource.remove(soundId);
/*     */         } 
/* 184 */         AL10.alSourceStop(sourceID);
/* 185 */         AL10.alSourcei(sourceID, 4105, 0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void stopSourcesWithBuffer(int bufferID) {
/* 191 */     if (this.noDevice)
/* 192 */       return;  for (int i = 0, n = this.idleSources.size; i < n; i++) {
/* 193 */       int sourceID = this.idleSources.get(i);
/* 194 */       if (AL10.alGetSourcei(sourceID, 4105) == bufferID) {
/* 195 */         if (this.sourceToSoundId.containsKey(sourceID)) {
/* 196 */           long soundId = ((Long)this.sourceToSoundId.remove(sourceID)).longValue();
/* 197 */           this.soundIdToSource.remove(soundId);
/*     */         } 
/* 199 */         AL10.alSourceStop(sourceID);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void pauseSourcesWithBuffer(int bufferID) {
/* 205 */     if (this.noDevice)
/* 206 */       return;  for (int i = 0, n = this.idleSources.size; i < n; i++) {
/* 207 */       int sourceID = this.idleSources.get(i);
/* 208 */       if (AL10.alGetSourcei(sourceID, 4105) == bufferID)
/* 209 */         AL10.alSourcePause(sourceID); 
/*     */     } 
/*     */   }
/*     */   
/*     */   void resumeSourcesWithBuffer(int bufferID) {
/* 214 */     if (this.noDevice)
/* 215 */       return;  for (int i = 0, n = this.idleSources.size; i < n; i++) {
/* 216 */       int sourceID = this.idleSources.get(i);
/* 217 */       if (AL10.alGetSourcei(sourceID, 4105) == bufferID && 
/* 218 */         AL10.alGetSourcei(sourceID, 4112) == 4115) {
/* 219 */         AL10.alSourcePlay(sourceID);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void update() {
/* 225 */     if (this.noDevice)
/* 226 */       return;  for (int i = 0; i < this.music.size; i++)
/* 227 */       ((OpenALMusic[])this.music.items)[i].update(); 
/*     */   }
/*     */   
/*     */   public long getSoundId(int sourceId) {
/* 231 */     if (!this.sourceToSoundId.containsKey(sourceId)) return -1L; 
/* 232 */     return ((Long)this.sourceToSoundId.get(sourceId)).longValue();
/*     */   }
/*     */   
/*     */   public void stopSound(long soundId) {
/* 236 */     if (!this.soundIdToSource.containsKey(soundId))
/* 237 */       return;  int sourceId = ((Integer)this.soundIdToSource.get(soundId)).intValue();
/* 238 */     AL10.alSourceStop(sourceId);
/*     */   }
/*     */   
/*     */   public void pauseSound(long soundId) {
/* 242 */     if (!this.soundIdToSource.containsKey(soundId))
/* 243 */       return;  int sourceId = ((Integer)this.soundIdToSource.get(soundId)).intValue();
/* 244 */     AL10.alSourcePause(sourceId);
/*     */   }
/*     */   
/*     */   public void resumeSound(long soundId) {
/* 248 */     if (!this.soundIdToSource.containsKey(soundId))
/* 249 */       return;  int sourceId = ((Integer)this.soundIdToSource.get(soundId)).intValue();
/* 250 */     if (AL10.alGetSourcei(sourceId, 4112) == 4115)
/* 251 */       AL10.alSourcePlay(sourceId); 
/*     */   }
/*     */   
/*     */   public void setSoundGain(long soundId, float volume) {
/* 255 */     if (!this.soundIdToSource.containsKey(soundId))
/* 256 */       return;  int sourceId = ((Integer)this.soundIdToSource.get(soundId)).intValue();
/* 257 */     AL10.alSourcef(sourceId, 4106, volume);
/*     */   }
/*     */   
/*     */   public void setSoundLooping(long soundId, boolean looping) {
/* 261 */     if (!this.soundIdToSource.containsKey(soundId))
/* 262 */       return;  int sourceId = ((Integer)this.soundIdToSource.get(soundId)).intValue();
/* 263 */     AL10.alSourcei(sourceId, 4103, looping ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void setSoundPitch(long soundId, float pitch) {
/* 267 */     if (!this.soundIdToSource.containsKey(soundId))
/* 268 */       return;  int sourceId = ((Integer)this.soundIdToSource.get(soundId)).intValue();
/* 269 */     AL10.alSourcef(sourceId, 4099, pitch);
/*     */   }
/*     */   
/*     */   public void setSoundPan(long soundId, float pan, float volume) {
/* 273 */     if (!this.soundIdToSource.containsKey(soundId))
/* 274 */       return;  int sourceId = ((Integer)this.soundIdToSource.get(soundId)).intValue();
/*     */     
/* 276 */     AL10.alSource3f(sourceId, 4100, MathUtils.cos((pan - 1.0F) * 3.1415927F / 2.0F), 0.0F, 
/* 277 */         MathUtils.sin((pan + 1.0F) * 3.1415927F / 2.0F));
/* 278 */     AL10.alSourcef(sourceId, 4106, volume);
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 282 */     if (this.noDevice)
/* 283 */       return;  for (int i = 0, n = this.allSources.size; i < n; i++) {
/* 284 */       int sourceID = this.allSources.get(i);
/* 285 */       int state = AL10.alGetSourcei(sourceID, 4112);
/* 286 */       if (state != 4116) AL10.alSourceStop(sourceID); 
/* 287 */       AL10.alDeleteSources(sourceID);
/*     */     } 
/*     */     
/* 290 */     this.sourceToSoundId.clear();
/* 291 */     this.soundIdToSource.clear();
/*     */     
/* 293 */     AL.destroy();
/* 294 */     while (AL.isCreated()) {
/*     */       try {
/* 296 */         Thread.sleep(10L);
/* 297 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioDevice newAudioDevice(int sampleRate, final boolean isMono) {
/* 303 */     if (this.noDevice) return new AudioDevice()
/*     */         {
/*     */           public void writeSamples(float[] samples, int offset, int numSamples) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void writeSamples(short[] samples, int offset, int numSamples) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void setVolume(float volume) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean isMono() {
/* 318 */             return isMono;
/*     */           }
/*     */ 
/*     */           
/*     */           public int getLatency() {
/* 323 */             return 0;
/*     */           }
/*     */ 
/*     */           
/*     */           public void dispose() {}
/*     */         };
/*     */     
/* 330 */     return new OpenALAudioDevice(this, sampleRate, isMono, this.deviceBufferSize, this.deviceBufferCount);
/*     */   }
/*     */   
/*     */   public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
/* 334 */     if (this.noDevice) return new AudioRecorder()
/*     */         {
/*     */           public void read(short[] samples, int offset, int numSamples) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void dispose() {}
/*     */         };
/*     */     
/* 343 */     return new JavaSoundAudioRecorder(samplingRate, isMono);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void retain(OpenALSound sound, boolean stop) {
/* 350 */     this.mostRecetSound++;
/* 351 */     this.mostRecetSound %= this.recentSounds.length;
/*     */     
/* 353 */     if (stop)
/*     */     {
/* 355 */       if (this.recentSounds[this.mostRecetSound] != null) this.recentSounds[this.mostRecetSound].stop();
/*     */     
/*     */     }
/* 358 */     this.recentSounds[this.mostRecetSound] = sound;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forget(OpenALSound sound) {
/* 363 */     for (int i = 0; i < this.recentSounds.length; i++) {
/* 364 */       if (this.recentSounds[i] == sound) this.recentSounds[i] = null; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\OpenALAudio.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */