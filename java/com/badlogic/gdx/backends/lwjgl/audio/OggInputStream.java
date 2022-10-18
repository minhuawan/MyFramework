/*     */ package com.badlogic.gdx.backends.lwjgl.audio;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import com.jcraft.jogg.Packet;
/*     */ import com.jcraft.jogg.Page;
/*     */ import com.jcraft.jogg.StreamState;
/*     */ import com.jcraft.jogg.SyncState;
/*     */ import com.jcraft.jorbis.Block;
/*     */ import com.jcraft.jorbis.Comment;
/*     */ import com.jcraft.jorbis.DspState;
/*     */ import com.jcraft.jorbis.Info;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import org.lwjgl.BufferUtils;
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
/*     */ 
/*     */ 
/*     */ public class OggInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 512;
/*  50 */   private int convsize = 2048;
/*     */   
/*     */   private byte[] convbuffer;
/*     */   
/*     */   private InputStream input;
/*     */   
/*  56 */   private Info oggInfo = new Info();
/*     */ 
/*     */   
/*     */   private boolean endOfStream;
/*     */   
/*  61 */   private SyncState syncState = new SyncState();
/*     */   
/*  63 */   private StreamState streamState = new StreamState();
/*     */   
/*  65 */   private Page page = new Page();
/*     */   
/*  67 */   private Packet packet = new Packet();
/*     */ 
/*     */   
/*  70 */   private Comment comment = new Comment();
/*     */   
/*  72 */   private DspState dspState = new DspState();
/*     */   
/*  74 */   private Block vorbisBlock = new Block(this.dspState);
/*     */ 
/*     */   
/*     */   byte[] buffer;
/*     */   
/*  79 */   int bytes = 0;
/*     */   
/*  81 */   boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
/*     */ 
/*     */   
/*     */   boolean endOfBitStream = true;
/*     */ 
/*     */   
/*     */   boolean inited = false;
/*     */ 
/*     */   
/*     */   private int readIndex;
/*     */   
/*     */   private ByteBuffer pcmBuffer;
/*     */   
/*     */   private int total;
/*     */ 
/*     */   
/*     */   public OggInputStream(InputStream input) {
/*  98 */     this(input, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   OggInputStream(InputStream input, OggInputStream previousStream) {
/* 108 */     if (previousStream == null) {
/* 109 */       this.convbuffer = new byte[this.convsize];
/* 110 */       this.pcmBuffer = BufferUtils.createByteBuffer(2048000);
/*     */     } else {
/* 112 */       this.convbuffer = previousStream.convbuffer;
/* 113 */       this.pcmBuffer = previousStream.pcmBuffer;
/*     */     } 
/*     */     
/* 116 */     this.input = input;
/*     */     try {
/* 118 */       this.total = input.available();
/* 119 */     } catch (IOException ex) {
/* 120 */       throw new GdxRuntimeException(ex);
/*     */     } 
/*     */     
/* 123 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/* 130 */     return this.total;
/*     */   }
/*     */   
/*     */   public int getChannels() {
/* 134 */     return this.oggInfo.channels;
/*     */   }
/*     */   
/*     */   public int getSampleRate() {
/* 138 */     return this.oggInfo.rate;
/*     */   }
/*     */ 
/*     */   
/*     */   private void init() {
/* 143 */     initVorbis();
/* 144 */     readPCM();
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 149 */     return this.endOfStream ? 0 : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private void initVorbis() {
/* 154 */     this.syncState.init();
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
/*     */   private boolean getPageAndPacket() {
/* 167 */     int index = this.syncState.buffer(512);
/* 168 */     if (index == -1) return false;
/*     */     
/* 170 */     this.buffer = this.syncState.data;
/* 171 */     if (this.buffer == null) {
/* 172 */       this.endOfStream = true;
/* 173 */       return false;
/*     */     } 
/*     */     
/*     */     try {
/* 177 */       this.bytes = this.input.read(this.buffer, index, 512);
/* 178 */     } catch (Exception e) {
/* 179 */       throw new GdxRuntimeException("Failure reading Vorbis.", e);
/*     */     } 
/* 181 */     this.syncState.wrote(this.bytes);
/*     */ 
/*     */     
/* 184 */     if (this.syncState.pageout(this.page) != 1) {
/*     */       
/* 186 */       if (this.bytes < 512) return false;
/*     */ 
/*     */       
/* 189 */       throw new GdxRuntimeException("Input does not appear to be an Ogg bitstream.");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 194 */     this.streamState.init(this.page.serialno());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     this.oggInfo.init();
/* 205 */     this.comment.init();
/* 206 */     if (this.streamState.pagein(this.page) < 0)
/*     */     {
/* 208 */       throw new GdxRuntimeException("Error reading first page of Ogg bitstream.");
/*     */     }
/*     */     
/* 211 */     if (this.streamState.packetout(this.packet) != 1)
/*     */     {
/* 213 */       throw new GdxRuntimeException("Error reading initial header packet.");
/*     */     }
/*     */     
/* 216 */     if (this.oggInfo.synthesis_headerin(this.comment, this.packet) < 0)
/*     */     {
/* 218 */       throw new GdxRuntimeException("Ogg bitstream does not contain Vorbis audio data.");
/*     */     }
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
/* 231 */     int i = 0;
/* 232 */     while (i < 2) {
/* 233 */       while (i < 2) {
/* 234 */         int result = this.syncState.pageout(this.page);
/* 235 */         if (result == 0) {
/*     */           break;
/*     */         }
/*     */         
/* 239 */         if (result == 1) {
/* 240 */           this.streamState.pagein(this.page);
/*     */ 
/*     */           
/* 243 */           while (i < 2) {
/* 244 */             result = this.streamState.packetout(this.packet);
/* 245 */             if (result == 0)
/* 246 */               break;  if (result == -1)
/*     */             {
/*     */               
/* 249 */               throw new GdxRuntimeException("Corrupt secondary header.");
/*     */             }
/*     */             
/* 252 */             this.oggInfo.synthesis_headerin(this.comment, this.packet);
/* 253 */             i++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 258 */       index = this.syncState.buffer(512);
/* 259 */       if (index == -1) return false; 
/* 260 */       this.buffer = this.syncState.data;
/*     */       try {
/* 262 */         this.bytes = this.input.read(this.buffer, index, 512);
/* 263 */       } catch (Exception e) {
/* 264 */         throw new GdxRuntimeException("Failed to read Vorbis.", e);
/*     */       } 
/* 266 */       if (this.bytes == 0 && i < 2) {
/* 267 */         throw new GdxRuntimeException("End of file before finding all Vorbis headers.");
/*     */       }
/* 269 */       this.syncState.wrote(this.bytes);
/*     */     } 
/*     */     
/* 272 */     this.convsize = 512 / this.oggInfo.channels;
/*     */ 
/*     */ 
/*     */     
/* 276 */     this.dspState.synthesis_init(this.oggInfo);
/* 277 */     this.vorbisBlock.init(this.dspState);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void readPCM() {
/* 288 */     boolean wrote = false;
/*     */     
/*     */     while (true) {
/* 291 */       if (this.endOfBitStream) {
/* 292 */         if (!getPageAndPacket()) {
/*     */           break;
/*     */         }
/* 295 */         this.endOfBitStream = false;
/*     */       } 
/*     */       
/* 298 */       if (!this.inited) {
/* 299 */         this.inited = true;
/*     */         
/*     */         return;
/*     */       } 
/* 303 */       float[][][] _pcm = new float[1][][];
/* 304 */       int[] _index = new int[this.oggInfo.channels];
/*     */       
/* 306 */       while (!this.endOfBitStream) {
/* 307 */         while (!this.endOfBitStream) {
/* 308 */           int result = this.syncState.pageout(this.page);
/*     */           
/* 310 */           if (result == 0) {
/*     */             break;
/*     */           }
/*     */           
/* 314 */           if (result == -1) {
/*     */             
/* 316 */             Gdx.app.log("gdx-audio", "Error reading OGG: Corrupt or missing data in bitstream."); continue;
/*     */           } 
/* 318 */           this.streamState.pagein(this.page);
/*     */           
/*     */           while (true) {
/* 321 */             result = this.streamState.packetout(this.packet);
/*     */             
/* 323 */             if (result == 0)
/* 324 */               break;  if (result == -1) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */             
/* 329 */             if (this.vorbisBlock.synthesis(this.packet) == 0) {
/* 330 */               this.dspState.synthesis_blockin(this.vorbisBlock);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             int samples;
/*     */ 
/*     */             
/* 338 */             while ((samples = this.dspState.synthesis_pcmout(_pcm, _index)) > 0) {
/* 339 */               float[][] pcm = _pcm[0];
/*     */               
/* 341 */               int bout = (samples < this.convsize) ? samples : this.convsize;
/*     */ 
/*     */ 
/*     */               
/* 345 */               for (int i = 0; i < this.oggInfo.channels; i++) {
/* 346 */                 int ptr = i * 2;
/*     */                 
/* 348 */                 int mono = _index[i];
/* 349 */                 for (int j = 0; j < bout; j++) {
/* 350 */                   int val = (int)(pcm[i][mono + j] * 32767.0D);
/*     */                   
/* 352 */                   if (val > 32767) {
/* 353 */                     val = 32767;
/*     */                   }
/* 355 */                   if (val < -32768) {
/* 356 */                     val = -32768;
/*     */                   }
/* 358 */                   if (val < 0) val |= 0x8000;
/*     */                   
/* 360 */                   if (this.bigEndian) {
/* 361 */                     this.convbuffer[ptr] = (byte)(val >>> 8);
/* 362 */                     this.convbuffer[ptr + 1] = (byte)val;
/*     */                   } else {
/* 364 */                     this.convbuffer[ptr] = (byte)val;
/* 365 */                     this.convbuffer[ptr + 1] = (byte)(val >>> 8);
/*     */                   } 
/* 367 */                   ptr += 2 * this.oggInfo.channels;
/*     */                 } 
/*     */               } 
/*     */               
/* 371 */               int bytesToWrite = 2 * this.oggInfo.channels * bout;
/* 372 */               if (bytesToWrite > this.pcmBuffer.remaining()) {
/* 373 */                 throw new GdxRuntimeException("Ogg block too big to be buffered: " + bytesToWrite + " :: " + this.pcmBuffer.remaining());
/*     */               }
/* 375 */               this.pcmBuffer.put(this.convbuffer, 0, bytesToWrite);
/*     */ 
/*     */               
/* 378 */               wrote = true;
/* 379 */               this.dspState.synthesis_read(bout);
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 385 */           if (this.page.eos() != 0) {
/* 386 */             this.endOfBitStream = true;
/*     */           }
/*     */           
/* 389 */           if (!this.endOfBitStream && wrote) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 395 */         if (!this.endOfBitStream) {
/* 396 */           this.bytes = 0;
/* 397 */           int index = this.syncState.buffer(512);
/* 398 */           if (index >= 0) {
/* 399 */             this.buffer = this.syncState.data;
/*     */             try {
/* 401 */               this.bytes = this.input.read(this.buffer, index, 512);
/* 402 */             } catch (Exception e) {
/* 403 */               throw new GdxRuntimeException("Error during Vorbis decoding.", e);
/*     */             } 
/*     */           } else {
/* 406 */             this.bytes = 0;
/*     */           } 
/* 408 */           this.syncState.wrote(this.bytes);
/* 409 */           if (this.bytes == 0) {
/* 410 */             this.endOfBitStream = true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 417 */       this.streamState.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 422 */       this.vorbisBlock.clear();
/* 423 */       this.dspState.clear();
/* 424 */       this.oggInfo.clear();
/*     */     } 
/*     */ 
/*     */     
/* 428 */     this.syncState.clear();
/* 429 */     this.endOfStream = true;
/*     */   }
/*     */   
/*     */   public int read() {
/* 433 */     if (this.readIndex >= this.pcmBuffer.position()) {
/* 434 */       this.pcmBuffer.clear();
/* 435 */       readPCM();
/* 436 */       this.readIndex = 0;
/*     */     } 
/* 438 */     if (this.readIndex >= this.pcmBuffer.position()) {
/* 439 */       return -1;
/*     */     }
/*     */     
/* 442 */     int value = this.pcmBuffer.get(this.readIndex);
/* 443 */     if (value < 0) {
/* 444 */       value = 256 + value;
/*     */     }
/* 446 */     this.readIndex++;
/*     */     
/* 448 */     return value;
/*     */   }
/*     */   
/*     */   public boolean atEnd() {
/* 452 */     return (this.endOfStream && this.readIndex >= this.pcmBuffer.position());
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) {
/* 456 */     for (int i = 0; i < len; i++) {
/* 457 */       int value = read();
/* 458 */       if (value >= 0) {
/* 459 */         b[i] = (byte)value;
/*     */       } else {
/* 461 */         if (i == 0) {
/* 462 */           return -1;
/*     */         }
/* 464 */         return i;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 469 */     return len;
/*     */   }
/*     */   
/*     */   public int read(byte[] b) {
/* 473 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void close() {
/* 477 */     StreamUtils.closeQuietly(this.input);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\OggInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */