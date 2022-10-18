/*     */ package com.badlogic.gdx.input;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.InputAdapter;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
/*     */ import com.badlogic.gdx.utils.Timer;
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
/*     */ public class GestureDetector
/*     */   extends InputAdapter
/*     */ {
/*     */   final GestureListener listener;
/*     */   private float tapSquareSize;
/*     */   private long tapCountInterval;
/*     */   private float longPressSeconds;
/*     */   private long maxFlingDelay;
/*     */   private boolean inTapSquare;
/*     */   private int tapCount;
/*     */   private long lastTapTime;
/*     */   private float lastTapX;
/*     */   private float lastTapY;
/*     */   private int lastTapButton;
/*     */   private int lastTapPointer;
/*     */   boolean longPressFired;
/*     */   private boolean pinching;
/*     */   private boolean panning;
/*  46 */   private final VelocityTracker tracker = new VelocityTracker(); private float tapSquareCenterX;
/*     */   private float tapSquareCenterY;
/*     */   private long gestureStartTime;
/*  49 */   Vector2 pointer1 = new Vector2();
/*  50 */   private final Vector2 pointer2 = new Vector2();
/*  51 */   private final Vector2 initialPointer1 = new Vector2();
/*  52 */   private final Vector2 initialPointer2 = new Vector2();
/*     */   
/*  54 */   private final Timer.Task longPressTask = new Timer.Task()
/*     */     {
/*     */       public void run() {
/*  57 */         if (!GestureDetector.this.longPressFired) GestureDetector.this.longPressFired = GestureDetector.this.listener.longPress(GestureDetector.this.pointer1.x, GestureDetector.this.pointer1.y);
/*     */       
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public GestureDetector(GestureListener listener) {
/*  64 */     this(20.0F, 0.4F, 1.1F, 0.15F, listener);
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
/*     */   public GestureDetector(float halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay, GestureListener listener) {
/*  77 */     this.tapSquareSize = halfTapSquareSize;
/*  78 */     this.tapCountInterval = (long)(tapCountInterval * 1.0E9F);
/*  79 */     this.longPressSeconds = longPressDuration;
/*  80 */     this.maxFlingDelay = (long)(maxFlingDelay * 1.0E9F);
/*  81 */     this.listener = listener;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchDown(int x, int y, int pointer, int button) {
/*  86 */     return touchDown(x, y, pointer, button);
/*     */   }
/*     */   
/*     */   public boolean touchDown(float x, float y, int pointer, int button) {
/*  90 */     if (pointer > 1) return false;
/*     */     
/*  92 */     if (pointer == 0) {
/*  93 */       this.pointer1.set(x, y);
/*  94 */       this.gestureStartTime = Gdx.input.getCurrentEventTime();
/*  95 */       this.tracker.start(x, y, this.gestureStartTime);
/*  96 */       if (Gdx.input.isTouched(1)) {
/*     */         
/*  98 */         this.inTapSquare = false;
/*  99 */         this.pinching = true;
/* 100 */         this.initialPointer1.set(this.pointer1);
/* 101 */         this.initialPointer2.set(this.pointer2);
/* 102 */         this.longPressTask.cancel();
/*     */       } else {
/*     */         
/* 105 */         this.inTapSquare = true;
/* 106 */         this.pinching = false;
/* 107 */         this.longPressFired = false;
/* 108 */         this.tapSquareCenterX = x;
/* 109 */         this.tapSquareCenterY = y;
/* 110 */         if (!this.longPressTask.isScheduled()) Timer.schedule(this.longPressTask, this.longPressSeconds);
/*     */       
/*     */       } 
/*     */     } else {
/* 114 */       this.pointer2.set(x, y);
/* 115 */       this.inTapSquare = false;
/* 116 */       this.pinching = true;
/* 117 */       this.initialPointer1.set(this.pointer1);
/* 118 */       this.initialPointer2.set(this.pointer2);
/* 119 */       this.longPressTask.cancel();
/*     */     } 
/* 121 */     return this.listener.touchDown(x, y, pointer, button);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchDragged(int x, int y, int pointer) {
/* 126 */     return touchDragged(x, y, pointer);
/*     */   }
/*     */   
/*     */   public boolean touchDragged(float x, float y, int pointer) {
/* 130 */     if (pointer > 1) return false; 
/* 131 */     if (this.longPressFired) return false;
/*     */     
/* 133 */     if (pointer == 0) {
/* 134 */       this.pointer1.set(x, y);
/*     */     } else {
/* 136 */       this.pointer2.set(x, y);
/*     */     } 
/*     */     
/* 139 */     if (this.pinching) {
/* 140 */       if (this.listener != null) {
/* 141 */         boolean result = this.listener.pinch(this.initialPointer1, this.initialPointer2, this.pointer1, this.pointer2);
/* 142 */         return (this.listener.zoom(this.initialPointer1.dst(this.initialPointer2), this.pointer1.dst(this.pointer2)) || result);
/*     */       } 
/* 144 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     this.tracker.update(x, y, Gdx.input.getCurrentEventTime());
/*     */ 
/*     */     
/* 151 */     if (this.inTapSquare && !isWithinTapSquare(x, y, this.tapSquareCenterX, this.tapSquareCenterY)) {
/* 152 */       this.longPressTask.cancel();
/* 153 */       this.inTapSquare = false;
/*     */     } 
/*     */ 
/*     */     
/* 157 */     if (!this.inTapSquare) {
/* 158 */       this.panning = true;
/* 159 */       return this.listener.pan(x, y, this.tracker.deltaX, this.tracker.deltaY);
/*     */     } 
/*     */     
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchUp(int x, int y, int pointer, int button) {
/* 167 */     return touchUp(x, y, pointer, button);
/*     */   }
/*     */   
/*     */   public boolean touchUp(float x, float y, int pointer, int button) {
/* 171 */     if (pointer > 1) return false;
/*     */ 
/*     */     
/* 174 */     if (this.inTapSquare && !isWithinTapSquare(x, y, this.tapSquareCenterX, this.tapSquareCenterY)) this.inTapSquare = false;
/*     */     
/* 176 */     boolean wasPanning = this.panning;
/* 177 */     this.panning = false;
/*     */     
/* 179 */     this.longPressTask.cancel();
/* 180 */     if (this.longPressFired) return false;
/*     */     
/* 182 */     if (this.inTapSquare) {
/*     */       
/* 184 */       if (this.lastTapButton != button || this.lastTapPointer != pointer || TimeUtils.nanoTime() - this.lastTapTime > this.tapCountInterval || 
/* 185 */         !isWithinTapSquare(x, y, this.lastTapX, this.lastTapY)) this.tapCount = 0; 
/* 186 */       this.tapCount++;
/* 187 */       this.lastTapTime = TimeUtils.nanoTime();
/* 188 */       this.lastTapX = x;
/* 189 */       this.lastTapY = y;
/* 190 */       this.lastTapButton = button;
/* 191 */       this.lastTapPointer = pointer;
/* 192 */       this.gestureStartTime = 0L;
/* 193 */       return this.listener.tap(x, y, this.tapCount, button);
/*     */     } 
/*     */     
/* 196 */     if (this.pinching) {
/*     */       
/* 198 */       this.pinching = false;
/* 199 */       this.listener.pinchStop();
/* 200 */       this.panning = true;
/*     */       
/* 202 */       if (pointer == 0) {
/*     */         
/* 204 */         this.tracker.start(this.pointer2.x, this.pointer2.y, Gdx.input.getCurrentEventTime());
/*     */       } else {
/*     */         
/* 207 */         this.tracker.start(this.pointer1.x, this.pointer1.y, Gdx.input.getCurrentEventTime());
/*     */       } 
/* 209 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 213 */     boolean handled = false;
/* 214 */     if (wasPanning && !this.panning) handled = this.listener.panStop(x, y, pointer, button);
/*     */ 
/*     */     
/* 217 */     this.gestureStartTime = 0L;
/* 218 */     long time = Gdx.input.getCurrentEventTime();
/* 219 */     if (time - this.tracker.lastTime < this.maxFlingDelay) {
/* 220 */       this.tracker.update(x, y, time);
/* 221 */       handled = (this.listener.fling(this.tracker.getVelocityX(), this.tracker.getVelocityY(), button) || handled);
/*     */     } 
/* 223 */     return handled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 228 */     this.longPressTask.cancel();
/* 229 */     this.longPressFired = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLongPressed() {
/* 234 */     return isLongPressed(this.longPressSeconds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLongPressed(float duration) {
/* 240 */     if (this.gestureStartTime == 0L) return false; 
/* 241 */     return (TimeUtils.nanoTime() - this.gestureStartTime > (long)(duration * 1.0E9F));
/*     */   }
/*     */   
/*     */   public boolean isPanning() {
/* 245 */     return this.panning;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 249 */     this.gestureStartTime = 0L;
/* 250 */     this.panning = false;
/* 251 */     this.inTapSquare = false;
/*     */   }
/*     */   
/*     */   private boolean isWithinTapSquare(float x, float y, float centerX, float centerY) {
/* 255 */     return (Math.abs(x - centerX) < this.tapSquareSize && Math.abs(y - centerY) < this.tapSquareSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateTapSquare() {
/* 260 */     this.inTapSquare = false;
/*     */   }
/*     */   
/*     */   public void setTapSquareSize(float halfTapSquareSize) {
/* 264 */     this.tapSquareSize = halfTapSquareSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTapCountInterval(float tapCountInterval) {
/* 269 */     this.tapCountInterval = (long)(tapCountInterval * 1.0E9F);
/*     */   }
/*     */   
/*     */   public void setLongPressSeconds(float longPressSeconds) {
/* 273 */     this.longPressSeconds = longPressSeconds;
/*     */   }
/*     */   
/*     */   public void setMaxFlingDelay(long maxFlingDelay) {
/* 277 */     this.maxFlingDelay = maxFlingDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface GestureListener
/*     */   {
/*     */     boolean touchDown(float param1Float1, float param1Float2, int param1Int1, int param1Int2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean tap(float param1Float1, float param1Float2, int param1Int1, int param1Int2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean longPress(float param1Float1, float param1Float2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean fling(float param1Float1, float param1Float2, int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean pan(float param1Float1, float param1Float2, float param1Float3, float param1Float4);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean panStop(float param1Float1, float param1Float2, int param1Int1, int param1Int2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean zoom(float param1Float1, float param1Float2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean pinch(Vector2 param1Vector21, Vector2 param1Vector22, Vector2 param1Vector23, Vector2 param1Vector24);
/*     */ 
/*     */ 
/*     */     
/*     */     void pinchStop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class GestureAdapter
/*     */     implements GestureListener
/*     */   {
/*     */     public boolean touchDown(float x, float y, int pointer, int button) {
/* 333 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tap(float x, float y, int count, int button) {
/* 338 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean longPress(float x, float y) {
/* 343 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean fling(float velocityX, float velocityY, int button) {
/* 348 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean pan(float x, float y, float deltaX, float deltaY) {
/* 353 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean panStop(float x, float y, int pointer, int button) {
/* 358 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean zoom(float initialDistance, float distance) {
/* 363 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
/* 368 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void pinchStop() {}
/*     */   }
/*     */   
/*     */   static class VelocityTracker
/*     */   {
/* 377 */     int sampleSize = 10;
/*     */     
/*     */     float lastX;
/*     */     float lastY;
/*     */     float deltaX;
/* 382 */     float[] meanX = new float[this.sampleSize]; float deltaY; long lastTime; int numSamples;
/* 383 */     float[] meanY = new float[this.sampleSize];
/* 384 */     long[] meanTime = new long[this.sampleSize];
/*     */     
/*     */     public void start(float x, float y, long timeStamp) {
/* 387 */       this.lastX = x;
/* 388 */       this.lastY = y;
/* 389 */       this.deltaX = 0.0F;
/* 390 */       this.deltaY = 0.0F;
/* 391 */       this.numSamples = 0;
/* 392 */       for (int i = 0; i < this.sampleSize; i++) {
/* 393 */         this.meanX[i] = 0.0F;
/* 394 */         this.meanY[i] = 0.0F;
/* 395 */         this.meanTime[i] = 0L;
/*     */       } 
/* 397 */       this.lastTime = timeStamp;
/*     */     }
/*     */     
/*     */     public void update(float x, float y, long timeStamp) {
/* 401 */       long currTime = timeStamp;
/* 402 */       this.deltaX = x - this.lastX;
/* 403 */       this.deltaY = y - this.lastY;
/* 404 */       this.lastX = x;
/* 405 */       this.lastY = y;
/* 406 */       long deltaTime = currTime - this.lastTime;
/* 407 */       this.lastTime = currTime;
/* 408 */       int index = this.numSamples % this.sampleSize;
/* 409 */       this.meanX[index] = this.deltaX;
/* 410 */       this.meanY[index] = this.deltaY;
/* 411 */       this.meanTime[index] = deltaTime;
/* 412 */       this.numSamples++;
/*     */     }
/*     */     
/*     */     public float getVelocityX() {
/* 416 */       float meanX = getAverage(this.meanX, this.numSamples);
/* 417 */       float meanTime = (float)getAverage(this.meanTime, this.numSamples) / 1.0E9F;
/* 418 */       if (meanTime == 0.0F) return 0.0F; 
/* 419 */       return meanX / meanTime;
/*     */     }
/*     */     
/*     */     public float getVelocityY() {
/* 423 */       float meanY = getAverage(this.meanY, this.numSamples);
/* 424 */       float meanTime = (float)getAverage(this.meanTime, this.numSamples) / 1.0E9F;
/* 425 */       if (meanTime == 0.0F) return 0.0F; 
/* 426 */       return meanY / meanTime;
/*     */     }
/*     */     
/*     */     private float getAverage(float[] values, int numSamples) {
/* 430 */       numSamples = Math.min(this.sampleSize, numSamples);
/* 431 */       float sum = 0.0F;
/* 432 */       for (int i = 0; i < numSamples; i++) {
/* 433 */         sum += values[i];
/*     */       }
/* 435 */       return sum / numSamples;
/*     */     }
/*     */     
/*     */     private long getAverage(long[] values, int numSamples) {
/* 439 */       numSamples = Math.min(this.sampleSize, numSamples);
/* 440 */       long sum = 0L;
/* 441 */       for (int i = 0; i < numSamples; i++) {
/* 442 */         sum += values[i];
/*     */       }
/* 444 */       if (numSamples == 0) return 0L; 
/* 445 */       return sum / numSamples;
/*     */     }
/*     */     
/*     */     private float getSum(float[] values, int numSamples) {
/* 449 */       numSamples = Math.min(this.sampleSize, numSamples);
/* 450 */       float sum = 0.0F;
/* 451 */       for (int i = 0; i < numSamples; i++) {
/* 452 */         sum += values[i];
/*     */       }
/* 454 */       if (numSamples == 0) return 0.0F; 
/* 455 */       return sum;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\input\GestureDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */