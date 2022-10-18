/*    */ package com.badlogic.gdx.backends.lwjgl.audio;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.utils.StreamUtils;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Ogg
/*    */ {
/*    */   public static class Music
/*    */     extends OpenALMusic
/*    */   {
/*    */     private OggInputStream input;
/*    */     private OggInputStream previousInput;
/*    */     
/*    */     public Music(OpenALAudio audio, FileHandle file) {
/* 31 */       super(audio, file);
/* 32 */       if (audio.noDevice)
/* 33 */         return;  this.input = new OggInputStream(file.read());
/* 34 */       setup(this.input.getChannels(), this.input.getSampleRate());
/*    */     }
/*    */     
/*    */     public int read(byte[] buffer) {
/* 38 */       if (this.input == null) {
/* 39 */         this.input = new OggInputStream(this.file.read(), this.previousInput);
/* 40 */         setup(this.input.getChannels(), this.input.getSampleRate());
/* 41 */         this.previousInput = null;
/*    */       } 
/* 43 */       return this.input.read(buffer);
/*    */     }
/*    */     
/*    */     public void reset() {
/* 47 */       StreamUtils.closeQuietly(this.input);
/* 48 */       this.previousInput = null;
/* 49 */       this.input = null;
/*    */     }
/*    */ 
/*    */     
/*    */     protected void loop() {
/* 54 */       StreamUtils.closeQuietly(this.input);
/* 55 */       this.previousInput = this.input;
/* 56 */       this.input = null;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Sound extends OpenALSound {
/*    */     public Sound(OpenALAudio audio, FileHandle file) {
/* 62 */       super(audio);
/* 63 */       if (audio.noDevice)
/* 64 */         return;  OggInputStream input = null;
/*    */       try {
/* 66 */         input = new OggInputStream(file.read());
/* 67 */         ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
/* 68 */         byte[] buffer = new byte[2048];
/* 69 */         while (!input.atEnd()) {
/* 70 */           int length = input.read(buffer);
/* 71 */           if (length == -1)
/* 72 */             break;  output.write(buffer, 0, length);
/*    */         } 
/* 74 */         setup(output.toByteArray(), input.getChannels(), input.getSampleRate());
/*    */       } finally {
/* 76 */         StreamUtils.closeQuietly(input);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\Ogg.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */