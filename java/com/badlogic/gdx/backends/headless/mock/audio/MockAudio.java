/*    */ package com.badlogic.gdx.backends.headless.mock.audio;
/*    */ 
/*    */ import com.badlogic.gdx.Audio;
/*    */ import com.badlogic.gdx.audio.AudioDevice;
/*    */ import com.badlogic.gdx.audio.AudioRecorder;
/*    */ import com.badlogic.gdx.audio.Music;
/*    */ import com.badlogic.gdx.audio.Sound;
/*    */ import com.badlogic.gdx.files.FileHandle;
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
/*    */ 
/*    */ 
/*    */ public class MockAudio
/*    */   implements Audio
/*    */ {
/*    */   public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
/* 33 */     return new MockAudioDevice();
/*    */   }
/*    */ 
/*    */   
/*    */   public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
/* 38 */     return new MockAudioRecorder();
/*    */   }
/*    */ 
/*    */   
/*    */   public Sound newSound(FileHandle fileHandle) {
/* 43 */     return new MockSound();
/*    */   }
/*    */ 
/*    */   
/*    */   public Music newMusic(FileHandle file) {
/* 48 */     return new MockMusic();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\mock\audio\MockAudio.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */