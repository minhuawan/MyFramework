/*    */ package com.badlogic.gdx.backends.lwjgl.audio;
/*    */ 
/*    */ import com.badlogic.gdx.audio.AudioRecorder;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import javax.sound.sampled.AudioSystem;
/*    */ import javax.sound.sampled.TargetDataLine;
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
/*    */ 
/*    */ 
/*    */ public class JavaSoundAudioRecorder
/*    */   implements AudioRecorder
/*    */ {
/*    */   private TargetDataLine line;
/* 30 */   private byte[] buffer = new byte[4096];
/*    */   
/*    */   public JavaSoundAudioRecorder(int samplingRate, boolean isMono) {
/*    */     try {
/* 34 */       AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, samplingRate, 16, isMono ? 1 : 2, isMono ? 2 : 4, samplingRate, false);
/*    */       
/* 36 */       this.line = AudioSystem.getTargetDataLine(format);
/* 37 */       this.line.open(format, this.buffer.length);
/* 38 */       this.line.start();
/* 39 */     } catch (Exception ex) {
/* 40 */       throw new GdxRuntimeException("Error creating JavaSoundAudioRecorder.", ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void read(short[] samples, int offset, int numSamples) {
/* 45 */     if (this.buffer.length < numSamples * 2) this.buffer = new byte[numSamples * 2];
/*    */     
/* 47 */     int toRead = numSamples * 2;
/* 48 */     int read = 0;
/* 49 */     while (read != toRead) {
/* 50 */       read += this.line.read(this.buffer, read, toRead - read);
/*    */     }
/* 52 */     for (int i = 0, j = 0; i < numSamples * 2; i += 2, j++)
/* 53 */       samples[offset + j] = (short)(this.buffer[i + 1] << 8 | this.buffer[i] & 0xFF); 
/*    */   }
/*    */   
/*    */   public void dispose() {
/* 57 */     this.line.close();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\audio\JavaSoundAudioRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */