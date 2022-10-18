/*     */ package com.badlogic.gdx.input;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Input;
/*     */ import com.badlogic.gdx.InputProcessor;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteInput
/*     */   implements Runnable, Input
/*     */ {
/*     */   public static interface RemoteInputListener
/*     */   {
/*     */     void onConnected();
/*     */     
/*     */     void onDisconnected();
/*     */   }
/*     */   
/*     */   class KeyEvent
/*     */   {
/*     */     static final int KEY_DOWN = 0;
/*     */     static final int KEY_UP = 1;
/*     */     static final int KEY_TYPED = 2;
/*     */     long timeStamp;
/*     */     int type;
/*     */     int keyCode;
/*     */     char keyChar;
/*     */   }
/*     */   
/*     */   class TouchEvent
/*     */   {
/*     */     static final int TOUCH_DOWN = 0;
/*     */     static final int TOUCH_UP = 1;
/*     */     static final int TOUCH_DRAGGED = 2;
/*     */     long timeStamp;
/*     */     int type;
/*     */     int x;
/*     */     int y;
/*     */     int pointer;
/*     */   }
/*     */   
/*     */   class EventTrigger
/*     */     implements Runnable
/*     */   {
/*     */     RemoteInput.TouchEvent touchEvent;
/*     */     RemoteInput.KeyEvent keyEvent;
/*     */     
/*     */     public EventTrigger(RemoteInput.TouchEvent touchEvent, RemoteInput.KeyEvent keyEvent) {
/*  84 */       this.touchEvent = touchEvent;
/*  85 */       this.keyEvent = keyEvent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  90 */       RemoteInput.this.justTouched = false;
/*  91 */       if (RemoteInput.this.keyJustPressed) {
/*  92 */         RemoteInput.this.keyJustPressed = false;
/*  93 */         for (int i = 0; i < RemoteInput.this.justPressedKeys.length; i++) {
/*  94 */           RemoteInput.this.justPressedKeys[i] = false;
/*     */         }
/*     */       } 
/*     */       
/*  98 */       if (RemoteInput.this.processor != null) {
/*  99 */         if (this.touchEvent != null) {
/* 100 */           RemoteInput.this.touchX[this.touchEvent.pointer] = this.touchEvent.x;
/* 101 */           RemoteInput.this.touchY[this.touchEvent.pointer] = this.touchEvent.y;
/* 102 */           switch (this.touchEvent.type) {
/*     */             case 0:
/* 104 */               RemoteInput.this.processor.touchDown(this.touchEvent.x, this.touchEvent.y, this.touchEvent.pointer, 0);
/* 105 */               RemoteInput.this.isTouched[this.touchEvent.pointer] = true;
/* 106 */               RemoteInput.this.justTouched = true;
/*     */               break;
/*     */             case 1:
/* 109 */               RemoteInput.this.processor.touchUp(this.touchEvent.x, this.touchEvent.y, this.touchEvent.pointer, 0);
/* 110 */               RemoteInput.this.isTouched[this.touchEvent.pointer] = false;
/*     */               break;
/*     */             case 2:
/* 113 */               RemoteInput.this.processor.touchDragged(this.touchEvent.x, this.touchEvent.y, this.touchEvent.pointer);
/*     */               break;
/*     */           } 
/*     */         } 
/* 117 */         if (this.keyEvent != null) {
/* 118 */           switch (this.keyEvent.type) {
/*     */             case 0:
/* 120 */               RemoteInput.this.processor.keyDown(this.keyEvent.keyCode);
/* 121 */               if (!RemoteInput.this.keys[this.keyEvent.keyCode]) {
/* 122 */                 RemoteInput.this.keyCount++;
/* 123 */                 RemoteInput.this.keys[this.keyEvent.keyCode] = true;
/*     */               } 
/* 125 */               RemoteInput.this.keyJustPressed = true;
/* 126 */               RemoteInput.this.justPressedKeys[this.keyEvent.keyCode] = true;
/*     */               break;
/*     */             case 1:
/* 129 */               RemoteInput.this.processor.keyUp(this.keyEvent.keyCode);
/* 130 */               if (RemoteInput.this.keys[this.keyEvent.keyCode]) {
/* 131 */                 RemoteInput.this.keyCount--;
/* 132 */                 RemoteInput.this.keys[this.keyEvent.keyCode] = false;
/*     */               } 
/*     */               break;
/*     */             case 2:
/* 136 */               RemoteInput.this.processor.keyTyped(this.keyEvent.keyChar);
/*     */               break;
/*     */           } 
/*     */         }
/*     */       } else {
/* 141 */         if (this.touchEvent != null) {
/* 142 */           RemoteInput.this.touchX[this.touchEvent.pointer] = this.touchEvent.x;
/* 143 */           RemoteInput.this.touchY[this.touchEvent.pointer] = this.touchEvent.y;
/* 144 */           if (this.touchEvent.type == 0) {
/* 145 */             RemoteInput.this.isTouched[this.touchEvent.pointer] = true;
/* 146 */             RemoteInput.this.justTouched = true;
/*     */           } 
/* 148 */           if (this.touchEvent.type == 1) {
/* 149 */             RemoteInput.this.isTouched[this.touchEvent.pointer] = false;
/*     */           }
/*     */         } 
/* 152 */         if (this.keyEvent != null) {
/* 153 */           if (this.keyEvent.type == 0) {
/* 154 */             if (!RemoteInput.this.keys[this.keyEvent.keyCode]) {
/* 155 */               RemoteInput.this.keyCount++;
/* 156 */               RemoteInput.this.keys[this.keyEvent.keyCode] = true;
/*     */             } 
/* 158 */             RemoteInput.this.keyJustPressed = true;
/* 159 */             RemoteInput.this.justPressedKeys[this.keyEvent.keyCode] = true;
/*     */           } 
/* 161 */           if (this.keyEvent.type == 1 && 
/* 162 */             RemoteInput.this.keys[this.keyEvent.keyCode]) {
/* 163 */             RemoteInput.this.keyCount--;
/* 164 */             RemoteInput.this.keys[this.keyEvent.keyCode] = false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 172 */   public static int DEFAULT_PORT = 8190;
/*     */   private ServerSocket serverSocket;
/* 174 */   private float[] accel = new float[3];
/* 175 */   private float[] gyrate = new float[3];
/* 176 */   private float[] compass = new float[3];
/*     */   private boolean multiTouch = false;
/* 178 */   private float remoteWidth = 0.0F;
/* 179 */   private float remoteHeight = 0.0F;
/*     */   private boolean connected = false;
/*     */   private RemoteInputListener listener;
/* 182 */   int keyCount = 0;
/* 183 */   boolean[] keys = new boolean[256];
/*     */   boolean keyJustPressed = false;
/* 185 */   boolean[] justPressedKeys = new boolean[256];
/* 186 */   int[] touchX = new int[20];
/* 187 */   int[] touchY = new int[20];
/* 188 */   boolean[] isTouched = new boolean[20];
/*     */   boolean justTouched = false;
/* 190 */   InputProcessor processor = null;
/*     */   private final int port;
/*     */   public final String[] ips;
/*     */   
/*     */   public RemoteInput() {
/* 195 */     this(DEFAULT_PORT);
/*     */   }
/*     */   
/*     */   public RemoteInput(RemoteInputListener listener) {
/* 199 */     this(DEFAULT_PORT, listener);
/*     */   }
/*     */   
/*     */   public RemoteInput(int port) {
/* 203 */     this(port, null);
/*     */   }
/*     */   
/*     */   public RemoteInput(int port, RemoteInputListener listener) {
/* 207 */     this.listener = listener;
/*     */     try {
/* 209 */       this.port = port;
/* 210 */       this.serverSocket = new ServerSocket(port);
/* 211 */       Thread thread = new Thread(this);
/* 212 */       thread.setDaemon(true);
/* 213 */       thread.start();
/* 214 */       InetAddress[] allByName = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
/* 215 */       this.ips = new String[allByName.length];
/* 216 */       for (int i = 0; i < allByName.length; i++) {
/* 217 */         this.ips[i] = allByName[i].getHostAddress();
/*     */       }
/* 219 */     } catch (Exception e) {
/* 220 */       throw new GdxRuntimeException("Couldn't open listening socket at port '" + port + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       try {
/* 228 */         this.connected = false;
/* 229 */         if (this.listener != null) this.listener.onDisconnected();
/*     */         
/* 231 */         System.out.println("listening, port " + this.port);
/* 232 */         Socket socket = null;
/*     */         
/* 234 */         socket = this.serverSocket.accept();
/* 235 */         socket.setTcpNoDelay(true);
/* 236 */         socket.setSoTimeout(3000);
/* 237 */         this.connected = true;
/* 238 */         if (this.listener != null) this.listener.onConnected();
/*     */         
/* 240 */         DataInputStream in = new DataInputStream(socket.getInputStream());
/* 241 */         this.multiTouch = in.readBoolean();
/*     */         while (true) {
/* 243 */           int event = in.readInt();
/* 244 */           KeyEvent keyEvent = null;
/* 245 */           TouchEvent touchEvent = null;
/* 246 */           switch (event) {
/*     */             case 6:
/* 248 */               this.accel[0] = in.readFloat();
/* 249 */               this.accel[1] = in.readFloat();
/* 250 */               this.accel[2] = in.readFloat();
/*     */               break;
/*     */             case 7:
/* 253 */               this.compass[0] = in.readFloat();
/* 254 */               this.compass[1] = in.readFloat();
/* 255 */               this.compass[2] = in.readFloat();
/*     */               break;
/*     */             case 8:
/* 258 */               this.remoteWidth = in.readFloat();
/* 259 */               this.remoteHeight = in.readFloat();
/*     */               break;
/*     */             case 9:
/* 262 */               this.gyrate[0] = in.readFloat();
/* 263 */               this.gyrate[1] = in.readFloat();
/* 264 */               this.gyrate[2] = in.readFloat();
/*     */               break;
/*     */             case 0:
/* 267 */               keyEvent = new KeyEvent();
/* 268 */               keyEvent.keyCode = in.readInt();
/* 269 */               keyEvent.type = 0;
/*     */               break;
/*     */             case 1:
/* 272 */               keyEvent = new KeyEvent();
/* 273 */               keyEvent.keyCode = in.readInt();
/* 274 */               keyEvent.type = 1;
/*     */               break;
/*     */             case 2:
/* 277 */               keyEvent = new KeyEvent();
/* 278 */               keyEvent.keyChar = in.readChar();
/* 279 */               keyEvent.type = 2;
/*     */               break;
/*     */             case 3:
/* 282 */               touchEvent = new TouchEvent();
/* 283 */               touchEvent.x = (int)(in.readInt() / this.remoteWidth * Gdx.graphics.getWidth());
/* 284 */               touchEvent.y = (int)(in.readInt() / this.remoteHeight * Gdx.graphics.getHeight());
/* 285 */               touchEvent.pointer = in.readInt();
/* 286 */               touchEvent.type = 0;
/*     */               break;
/*     */             case 4:
/* 289 */               touchEvent = new TouchEvent();
/* 290 */               touchEvent.x = (int)(in.readInt() / this.remoteWidth * Gdx.graphics.getWidth());
/* 291 */               touchEvent.y = (int)(in.readInt() / this.remoteHeight * Gdx.graphics.getHeight());
/* 292 */               touchEvent.pointer = in.readInt();
/* 293 */               touchEvent.type = 1;
/*     */               break;
/*     */             case 5:
/* 296 */               touchEvent = new TouchEvent();
/* 297 */               touchEvent.x = (int)(in.readInt() / this.remoteWidth * Gdx.graphics.getWidth());
/* 298 */               touchEvent.y = (int)(in.readInt() / this.remoteHeight * Gdx.graphics.getHeight());
/* 299 */               touchEvent.pointer = in.readInt();
/* 300 */               touchEvent.type = 2;
/*     */               break;
/*     */           } 
/*     */           
/* 304 */           Gdx.app.postRunnable(new EventTrigger(touchEvent, keyEvent));
/*     */         }  break;
/* 306 */       } catch (IOException e) {
/* 307 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 313 */     return this.connected;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerX() {
/* 318 */     return this.accel[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerY() {
/* 323 */     return this.accel[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerZ() {
/* 328 */     return this.accel[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeX() {
/* 333 */     return this.gyrate[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeY() {
/* 338 */     return this.gyrate[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeZ() {
/* 343 */     return this.gyrate[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/* 348 */     return this.touchX[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX(int pointer) {
/* 353 */     return this.touchX[pointer];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/* 358 */     return this.touchY[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY(int pointer) {
/* 363 */     return this.touchY[pointer];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouched() {
/* 368 */     return this.isTouched[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean justTouched() {
/* 373 */     return this.justTouched;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouched(int pointer) {
/* 378 */     return this.isTouched[pointer];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isButtonPressed(int button) {
/* 383 */     if (button != 0) return false; 
/* 384 */     for (int i = 0; i < this.isTouched.length; i++) {
/* 385 */       if (this.isTouched[i]) return true; 
/* 386 */     }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeyPressed(int key) {
/* 391 */     if (key == -1) {
/* 392 */       return (this.keyCount > 0);
/*     */     }
/* 394 */     if (key < 0 || key > 255) {
/* 395 */       return false;
/*     */     }
/* 397 */     return this.keys[key];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeyJustPressed(int key) {
/* 402 */     if (key == -1) {
/* 403 */       return this.keyJustPressed;
/*     */     }
/* 405 */     if (key < 0 || key > 255) {
/* 406 */       return false;
/*     */     }
/* 408 */     return this.justPressedKeys[key];
/*     */   }
/*     */ 
/*     */   
/*     */   public void getTextInput(Input.TextInputListener listener, String title, String text, String hint) {
/* 413 */     Gdx.app.getInput().getTextInput(listener, title, text, hint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnscreenKeyboardVisible(boolean visible) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void vibrate(int milliseconds) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void vibrate(long[] pattern, int repeat) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelVibrate() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAzimuth() {
/* 437 */     return this.compass[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 442 */     return this.compass[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRoll() {
/* 447 */     return this.compass[2];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchBackKey(boolean catchBack) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCatchBackKey() {
/* 457 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchMenuKey(boolean catchMenu) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCatchMenuKey() {
/* 467 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputProcessor(InputProcessor processor) {
/* 473 */     this.processor = processor;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputProcessor getInputProcessor() {
/* 478 */     return this.processor;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getIPs() {
/* 483 */     return this.ips;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPeripheralAvailable(Input.Peripheral peripheral) {
/* 488 */     if (peripheral == Input.Peripheral.Accelerometer) return true; 
/* 489 */     if (peripheral == Input.Peripheral.Compass) return true; 
/* 490 */     if (peripheral == Input.Peripheral.MultitouchScreen) return this.multiTouch; 
/* 491 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRotation() {
/* 496 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input.Orientation getNativeOrientation() {
/* 501 */     return Input.Orientation.Landscape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorCatched(boolean catched) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCursorCatched() {
/* 511 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDeltaX() {
/* 517 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaX(int pointer) {
/* 522 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaY() {
/* 527 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaY(int pointer) {
/* 532 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorPosition(int x, int y) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCurrentEventTime() {
/* 542 */     return 0L;
/*     */   }
/*     */   
/*     */   public void getRotationMatrix(float[] matrix) {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\input\RemoteInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */