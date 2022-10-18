/*     */ package com.badlogic.gdx.input;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Input;
/*     */ import com.badlogic.gdx.InputProcessor;
/*     */ import java.io.DataOutputStream;
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
/*     */ public class RemoteSender
/*     */   implements InputProcessor
/*     */ {
/*     */   private DataOutputStream out;
/*     */   private boolean connected = false;
/*     */   public static final int KEY_DOWN = 0;
/*     */   public static final int KEY_UP = 1;
/*     */   public static final int KEY_TYPED = 2;
/*     */   public static final int TOUCH_DOWN = 3;
/*     */   public static final int TOUCH_UP = 4;
/*     */   public static final int TOUCH_DRAGGED = 5;
/*     */   public static final int ACCEL = 6;
/*     */   public static final int COMPASS = 7;
/*     */   public static final int SIZE = 8;
/*     */   public static final int GYRO = 9;
/*     */   
/*     */   public RemoteSender(String ip, int port) {
/*     */     try {
/*  49 */       Socket socket = new Socket(ip, port);
/*  50 */       socket.setTcpNoDelay(true);
/*  51 */       socket.setSoTimeout(3000);
/*  52 */       this.out = new DataOutputStream(socket.getOutputStream());
/*  53 */       this.out.writeBoolean(Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen));
/*  54 */       this.connected = true;
/*  55 */       Gdx.input.setInputProcessor(this);
/*  56 */     } catch (Exception e) {
/*  57 */       Gdx.app.log("RemoteSender", "couldn't connect to " + ip + ":" + port);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendUpdate() {
/*  62 */     synchronized (this) {
/*  63 */       if (!this.connected)
/*     */         return; 
/*     */     }  try {
/*  66 */       this.out.writeInt(6);
/*  67 */       this.out.writeFloat(Gdx.input.getAccelerometerX());
/*  68 */       this.out.writeFloat(Gdx.input.getAccelerometerY());
/*  69 */       this.out.writeFloat(Gdx.input.getAccelerometerZ());
/*  70 */       this.out.writeInt(7);
/*  71 */       this.out.writeFloat(Gdx.input.getAzimuth());
/*  72 */       this.out.writeFloat(Gdx.input.getPitch());
/*  73 */       this.out.writeFloat(Gdx.input.getRoll());
/*  74 */       this.out.writeInt(8);
/*  75 */       this.out.writeFloat(Gdx.graphics.getWidth());
/*  76 */       this.out.writeFloat(Gdx.graphics.getHeight());
/*  77 */       this.out.writeInt(9);
/*  78 */       this.out.writeFloat(Gdx.input.getGyroscopeX());
/*  79 */       this.out.writeFloat(Gdx.input.getGyroscopeY());
/*  80 */       this.out.writeFloat(Gdx.input.getGyroscopeZ());
/*  81 */     } catch (Throwable t) {
/*  82 */       this.out = null;
/*  83 */       this.connected = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyDown(int keycode) {
/*  89 */     synchronized (this) {
/*  90 */       if (!this.connected) return false;
/*     */     
/*     */     } 
/*     */     try {
/*  94 */       this.out.writeInt(0);
/*  95 */       this.out.writeInt(keycode);
/*  96 */     } catch (Throwable t) {
/*  97 */       synchronized (this) {
/*  98 */         this.connected = false;
/*     */       } 
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyUp(int keycode) {
/* 106 */     synchronized (this) {
/* 107 */       if (!this.connected) return false;
/*     */     
/*     */     } 
/*     */     try {
/* 111 */       this.out.writeInt(1);
/* 112 */       this.out.writeInt(keycode);
/* 113 */     } catch (Throwable t) {
/* 114 */       synchronized (this) {
/* 115 */         this.connected = false;
/*     */       } 
/*     */     } 
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyTyped(char character) {
/* 123 */     synchronized (this) {
/* 124 */       if (!this.connected) return false;
/*     */     
/*     */     } 
/*     */     try {
/* 128 */       this.out.writeInt(2);
/* 129 */       this.out.writeChar(character);
/* 130 */     } catch (Throwable t) {
/* 131 */       synchronized (this) {
/* 132 */         this.connected = false;
/*     */       } 
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchDown(int x, int y, int pointer, int button) {
/* 140 */     synchronized (this) {
/* 141 */       if (!this.connected) return false;
/*     */     
/*     */     } 
/*     */     try {
/* 145 */       this.out.writeInt(3);
/* 146 */       this.out.writeInt(x);
/* 147 */       this.out.writeInt(y);
/* 148 */       this.out.writeInt(pointer);
/* 149 */     } catch (Throwable t) {
/* 150 */       synchronized (this) {
/* 151 */         this.connected = false;
/*     */       } 
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchUp(int x, int y, int pointer, int button) {
/* 159 */     synchronized (this) {
/* 160 */       if (!this.connected) return false;
/*     */     
/*     */     } 
/*     */     try {
/* 164 */       this.out.writeInt(4);
/* 165 */       this.out.writeInt(x);
/* 166 */       this.out.writeInt(y);
/* 167 */       this.out.writeInt(pointer);
/* 168 */     } catch (Throwable t) {
/* 169 */       synchronized (this) {
/* 170 */         this.connected = false;
/*     */       } 
/*     */     } 
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchDragged(int x, int y, int pointer) {
/* 178 */     synchronized (this) {
/* 179 */       if (!this.connected) return false;
/*     */     
/*     */     } 
/*     */     try {
/* 183 */       this.out.writeInt(5);
/* 184 */       this.out.writeInt(x);
/* 185 */       this.out.writeInt(y);
/* 186 */       this.out.writeInt(pointer);
/* 187 */     } catch (Throwable t) {
/* 188 */       synchronized (this) {
/* 189 */         this.connected = false;
/*     */       } 
/*     */     } 
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseMoved(int x, int y) {
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean scrolled(int amount) {
/* 202 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 206 */     synchronized (this) {
/* 207 */       return this.connected;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\input\RemoteSender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */