/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.audio.Music;
/*     */ import com.badlogic.gdx.files.FileHandle;
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
/*     */ 
/*     */ public abstract class OpenALMusic
/*     */   implements Music
/*     */ {
/*     */   private static final int bufferSize = 40960;
/*     */   private static final int bufferCount = 3;
/*     */   private static final int bytesPerSample = 2;
/*  37 */   private static final byte[] tempBytes = new byte[40960];
/*  38 */   private static final ByteBuffer tempBuffer = BufferUtils.createByteBuffer(40960);
/*     */   
/*     */   private final OpenALAudio audio;
/*     */   private IntBuffer buffers;
/*  42 */   private int sourceID = -1;
/*     */   private int format;
/*     */   private int sampleRate;
/*  45 */   private float volume = 1.0F; private boolean isLooping; private boolean isPlaying;
/*  46 */   private float pan = 0.0F;
/*     */   private float renderedSeconds;
/*     */   private float secondsPerBuffer;
/*     */   protected final FileHandle file;
/*  50 */   protected int bufferOverhead = 0;
/*     */   
/*     */   private Music.OnCompletionListener onCompletionListener;
/*     */   
/*     */   public OpenALMusic(OpenALAudio audio, FileHandle file) {
/*  55 */     this.audio = audio;
/*  56 */     this.file = file;
/*  57 */     this.onCompletionListener = null;
/*     */   }
/*     */   
/*     */   protected void setup(int channels, int sampleRate) {
/*  61 */     this.format = (channels > 1) ? 4355 : 4353;
/*  62 */     this.sampleRate = sampleRate;
/*  63 */     this.secondsPerBuffer = (40960 - this.bufferOverhead) / (2 * channels * sampleRate);
/*     */   }
/*     */   
/*     */   public void play() {
/*  67 */     if (this.audio.noDevice)
/*  68 */       return;  if (this.sourceID == -1) {
/*  69 */       this.sourceID = this.audio.obtainSource(true);
/*  70 */       if (this.sourceID == -1)
/*     */         return; 
/*  72 */       this.audio.music.add(this);
/*     */       
/*  74 */       if (this.buffers == null) {
/*  75 */         this.buffers = BufferUtils.createIntBuffer(3);
/*  76 */         AL10.alGenBuffers(this.buffers);
/*  77 */         int errorCode = AL10.alGetError();
/*  78 */         if (errorCode != 0) throw new GdxRuntimeException("Unable to allocate audio buffers. AL Error: " + errorCode); 
/*     */       } 
/*  80 */       AL10.alSourcei(this.sourceID, 4103, 0);
/*  81 */       setPan(this.pan, this.volume);
/*     */       
/*  83 */       boolean filled = false;
/*  84 */       for (int i = 0; i < 3; i++) {
/*  85 */         int bufferID = this.buffers.get(i);
/*  86 */         if (!fill(bufferID))
/*  87 */           break;  filled = true;
/*  88 */         AL10.alSourceQueueBuffers(this.sourceID, bufferID);
/*     */       } 
/*  90 */       if (!filled && this.onCompletionListener != null) this.onCompletionListener.onCompletion(this);
/*     */       
/*  92 */       if (AL10.alGetError() != 0) {
/*  93 */         stop();
/*     */         return;
/*     */       } 
/*     */     } 
/*  97 */     if (!this.isPlaying) {
/*  98 */       AL10.alSourcePlay(this.sourceID);
/*  99 */       this.isPlaying = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 104 */     if (this.audio.noDevice)
/* 105 */       return;  if (this.sourceID == -1)
/* 106 */       return;  this.audio.music.removeValue(this, true);
/* 107 */     reset();
/* 108 */     this.audio.freeSource(this.sourceID);
/* 109 */     this.sourceID = -1;
/* 110 */     this.renderedSeconds = 0.0F;
/* 111 */     this.isPlaying = false;
/*     */   }
/*     */   
/*     */   public void pause() {
/* 115 */     if (this.audio.noDevice)
/* 116 */       return;  if (this.sourceID != -1) AL10.alSourcePause(this.sourceID); 
/* 117 */     this.isPlaying = false;
/*     */   }
/*     */   
/*     */   public boolean isPlaying() {
/* 121 */     if (this.audio.noDevice) return false; 
/* 122 */     if (this.sourceID == -1) return false; 
/* 123 */     return this.isPlaying;
/*     */   }
/*     */   
/*     */   public void setLooping(boolean isLooping) {
/* 127 */     this.isLooping = isLooping;
/*     */   }
/*     */   
/*     */   public boolean isLooping() {
/* 131 */     return this.isLooping;
/*     */   }
/*     */   
/*     */   public void setVolume(float volume) {
/* 135 */     this.volume = volume;
/* 136 */     if (this.audio.noDevice)
/* 137 */       return;  if (this.sourceID != -1) AL10.alSourcef(this.sourceID, 4106, volume); 
/*     */   }
/*     */   
/*     */   public float getVolume() {
/* 141 */     return this.volume;
/*     */   }
/*     */   
/*     */   public void setPan(float pan, float volume) {
/* 145 */     this.volume = volume;
/* 146 */     this.pan = pan;
/* 147 */     if (this.audio.noDevice)
/* 148 */       return;  if (this.sourceID == -1)
/* 149 */       return;  AL10.alSource3f(this.sourceID, 4100, MathUtils.cos((pan - 1.0F) * 3.1415927F / 2.0F), 0.0F, 
/* 150 */         MathUtils.sin((pan + 1.0F) * 3.1415927F / 2.0F));
/* 151 */     AL10.alSourcef(this.sourceID, 4106, volume);
/*     */   }
/*     */   
/*     */   public void setPosition(float position) {
/* 155 */     if (this.audio.noDevice)
/* 156 */       return;  if (this.sourceID == -1)
/* 157 */       return;  boolean wasPlaying = this.isPlaying;
/* 158 */     this.isPlaying = false;
/* 159 */     AL10.alSourceStop(this.sourceID);
/* 160 */     AL10.alSourceUnqueueBuffers(this.sourceID, this.buffers);
/* 161 */     this.renderedSeconds += this.secondsPerBuffer * 3.0F;
/* 162 */     if (position <= this.renderedSeconds) {
/* 163 */       reset();
/* 164 */       this.renderedSeconds = 0.0F;
/*     */     } 
/* 166 */     while (this.renderedSeconds < position - this.secondsPerBuffer && 
/* 167 */       read(tempBytes) > 0) {
/* 168 */       this.renderedSeconds += this.secondsPerBuffer;
/*     */     }
/* 170 */     boolean filled = false;
/* 171 */     for (int i = 0; i < 3; i++) {
/* 172 */       int bufferID = this.buffers.get(i);
/* 173 */       if (!fill(bufferID))
/* 174 */         break;  filled = true;
/* 175 */       AL10.alSourceQueueBuffers(this.sourceID, bufferID);
/*     */     } 
/* 177 */     if (!filled) {
/* 178 */       stop();
/* 179 */       if (this.onCompletionListener != null) this.onCompletionListener.onCompletion(this); 
/*     */     } 
/* 181 */     AL10.alSourcef(this.sourceID, 4132, position - this.renderedSeconds);
/* 182 */     if (wasPlaying) {
/* 183 */       AL10.alSourcePlay(this.sourceID);
/* 184 */       this.isPlaying = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getPosition() {
/* 189 */     if (this.audio.noDevice) return 0.0F; 
/* 190 */     if (this.sourceID == -1) return 0.0F; 
/* 191 */     return this.renderedSeconds + AL10.alGetSourcef(this.sourceID, 4132);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int read(byte[] paramArrayOfbyte);
/*     */ 
/*     */   
/*     */   public abstract void reset();
/*     */ 
/*     */   
/*     */   protected void loop() {
/* 203 */     reset();
/*     */   }
/*     */   
/*     */   public int getChannels() {
/* 207 */     return (this.format == 4355) ? 2 : 1;
/*     */   }
/*     */   
/*     */   public int getRate() {
/* 211 */     return this.sampleRate;
/*     */   }
/*     */   
/*     */   public void update() {
/* 215 */     if (this.audio.noDevice)
/* 216 */       return;  if (this.sourceID == -1)
/*     */       return; 
/* 218 */     boolean end = false;
/* 219 */     int buffers = AL10.alGetSourcei(this.sourceID, 4118);
/* 220 */     while (buffers-- > 0) {
/* 221 */       int bufferID = AL10.alSourceUnqueueBuffers(this.sourceID);
/* 222 */       if (bufferID == 40963)
/* 223 */         break;  this.renderedSeconds += this.secondsPerBuffer;
/* 224 */       if (end)
/* 225 */         continue;  if (fill(bufferID)) {
/* 226 */         AL10.alSourceQueueBuffers(this.sourceID, bufferID); continue;
/*     */       } 
/* 228 */       end = true;
/*     */     } 
/* 230 */     if (end && AL10.alGetSourcei(this.sourceID, 4117) == 0) {
/* 231 */       stop();
/* 232 */       if (this.onCompletionListener != null) this.onCompletionListener.onCompletion(this);
/*     */     
/*     */     } 
/*     */     
/* 236 */     if (this.isPlaying && AL10.alGetSourcei(this.sourceID, 4112) != 4114) AL10.alSourcePlay(this.sourceID); 
/*     */   }
/*     */   
/*     */   private boolean fill(int bufferID) {
/* 240 */     tempBuffer.clear();
/* 241 */     int length = read(tempBytes);
/* 242 */     if (length <= 0)
/* 243 */       if (this.isLooping) {
/* 244 */         loop();
/* 245 */         this.renderedSeconds = 0.0F;
/* 246 */         length = read(tempBytes);
/* 247 */         if (length <= 0) return false; 
/*     */       } else {
/* 249 */         return false;
/*     */       }  
/* 251 */     tempBuffer.put(tempBytes, 0, length).flip();
/* 252 */     AL10.alBufferData(bufferID, this.format, tempBuffer, this.sampleRate);
/* 253 */     return true;
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 257 */     stop();
/* 258 */     if (this.audio.noDevice)
/* 259 */       return;  if (this.buffers == null)
/* 260 */       return;  AL10.alDeleteBuffers(this.buffers);
/* 261 */     this.buffers = null;
/* 262 */     this.onCompletionListener = null;
/*     */   }
/*     */   
/*     */   public void setOnCompletionListener(Music.OnCompletionListener listener) {
/* 266 */     this.onCompletionListener = listener;
/*     */   }
/*     */   
/*     */   public int getSourceId() {
/* 270 */     return this.sourceID;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\OpenALMusic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */