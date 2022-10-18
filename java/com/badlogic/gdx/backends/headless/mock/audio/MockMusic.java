/*    */ package com.badlogic.gdx.backends.headless.mock.audio;
/*    */ 
/*    */ import com.badlogic.gdx.audio.Music;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MockMusic
/*    */   implements Music
/*    */ {
/*    */   public void play() {}
/*    */   
/*    */   public void pause() {}
/*    */   
/*    */   public void stop() {}
/*    */   
/*    */   public boolean isPlaying() {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLooping(boolean isLooping) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isLooping() {
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVolume(float volume) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public float getVolume() {
/* 62 */     return 0.0F;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPan(float pan, float volume) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPosition(float position) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public float getPosition() {
/* 77 */     return 0.0F;
/*    */   }
/*    */   
/*    */   public void dispose() {}
/*    */   
/*    */   public void setOnCompletionListener(Music.OnCompletionListener listener) {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\mock\audio\MockMusic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */