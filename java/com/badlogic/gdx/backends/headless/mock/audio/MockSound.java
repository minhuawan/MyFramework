/*    */ package com.badlogic.gdx.backends.headless.mock.audio;
/*    */ 
/*    */ import com.badlogic.gdx.audio.Sound;
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
/*    */ public class MockSound
/*    */   implements Sound
/*    */ {
/*    */   public long play() {
/* 27 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long play(float volume) {
/* 32 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long play(float volume, float pitch, float pan) {
/* 37 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long loop() {
/* 42 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long loop(float volume) {
/* 47 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long loop(float volume, float pitch, float pan) {
/* 52 */     return 0L;
/*    */   }
/*    */   
/*    */   public void stop() {}
/*    */   
/*    */   public void pause() {}
/*    */   
/*    */   public void resume() {}
/*    */   
/*    */   public void dispose() {}
/*    */   
/*    */   public void stop(long soundId) {}
/*    */   
/*    */   public void pause(long soundId) {}
/*    */   
/*    */   public void resume(long soundId) {}
/*    */   
/*    */   public void setLooping(long soundId, boolean looping) {}
/*    */   
/*    */   public void setPitch(long soundId, float pitch) {}
/*    */   
/*    */   public void setVolume(long soundId, float volume) {}
/*    */   
/*    */   public void setPan(long soundId, float pan, float volume) {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\mock\audio\MockSound.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */