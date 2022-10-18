package com.badlogic.gdx.audio;

import com.badlogic.gdx.utils.Disposable;

public interface Sound extends Disposable {
  long play();
  
  long play(float paramFloat);
  
  long play(float paramFloat1, float paramFloat2, float paramFloat3);
  
  long loop();
  
  long loop(float paramFloat);
  
  long loop(float paramFloat1, float paramFloat2, float paramFloat3);
  
  void stop();
  
  void pause();
  
  void resume();
  
  void dispose();
  
  void stop(long paramLong);
  
  void pause(long paramLong);
  
  void resume(long paramLong);
  
  void setLooping(long paramLong, boolean paramBoolean);
  
  void setPitch(long paramLong, float paramFloat);
  
  void setVolume(long paramLong, float paramFloat);
  
  void setPan(long paramLong, float paramFloat1, float paramFloat2);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\audio\Sound.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */