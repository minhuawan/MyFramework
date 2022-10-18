package com.badlogic.gdx.audio;

import com.badlogic.gdx.utils.Disposable;

public interface Music extends Disposable {
  void play();
  
  void pause();
  
  void stop();
  
  boolean isPlaying();
  
  void setLooping(boolean paramBoolean);
  
  boolean isLooping();
  
  void setVolume(float paramFloat);
  
  float getVolume();
  
  void setPan(float paramFloat1, float paramFloat2);
  
  void setPosition(float paramFloat);
  
  float getPosition();
  
  void dispose();
  
  void setOnCompletionListener(OnCompletionListener paramOnCompletionListener);
  
  public static interface OnCompletionListener {
    void onCompletion(Music param1Music);
  }
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\audio\Music.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */