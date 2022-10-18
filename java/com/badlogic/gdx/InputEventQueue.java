/*     */ package com.badlogic.gdx;
/*     */ 
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
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
/*     */ public class InputEventQueue
/*     */   implements InputProcessor
/*     */ {
/*     */   private static final int KEY_DOWN = 0;
/*     */   private static final int KEY_UP = 1;
/*     */   private static final int KEY_TYPED = 2;
/*     */   private static final int TOUCH_DOWN = 3;
/*     */   private static final int TOUCH_UP = 4;
/*     */   private static final int TOUCH_DRAGGED = 5;
/*     */   private static final int MOUSE_MOVED = 6;
/*     */   private static final int SCROLLED = 7;
/*     */   private InputProcessor processor;
/*  35 */   private final IntArray queue = new IntArray();
/*  36 */   private final IntArray processingQueue = new IntArray();
/*     */   
/*     */   private long currentEventTime;
/*     */   
/*     */   public InputEventQueue() {}
/*     */   
/*     */   public InputEventQueue(InputProcessor processor) {
/*  43 */     this.processor = processor;
/*     */   }
/*     */   
/*     */   public void setProcessor(InputProcessor processor) {
/*  47 */     this.processor = processor;
/*     */   }
/*     */   
/*     */   public InputProcessor getProcessor() {
/*  51 */     return this.processor;
/*     */   }
/*     */   
/*     */   public void drain() {
/*  55 */     IntArray q = this.processingQueue;
/*  56 */     synchronized (this) {
/*  57 */       if (this.processor == null) {
/*  58 */         this.queue.clear();
/*     */         return;
/*     */       } 
/*  61 */       q.addAll(this.queue);
/*  62 */       this.queue.clear();
/*     */     } 
/*  64 */     InputProcessor localProcessor = this.processor;
/*  65 */     for (int i = 0, n = q.size; i < n; ) {
/*  66 */       this.currentEventTime = q.get(i++) << 32L | q.get(i++) & 0xFFFFFFFFL;
/*  67 */       switch (q.get(i++)) {
/*     */         case 0:
/*  69 */           localProcessor.keyDown(q.get(i++));
/*     */         
/*     */         case 1:
/*  72 */           localProcessor.keyUp(q.get(i++));
/*     */         
/*     */         case 2:
/*  75 */           localProcessor.keyTyped((char)q.get(i++));
/*     */         
/*     */         case 3:
/*  78 */           localProcessor.touchDown(q.get(i++), q.get(i++), q.get(i++), q.get(i++));
/*     */         
/*     */         case 4:
/*  81 */           localProcessor.touchUp(q.get(i++), q.get(i++), q.get(i++), q.get(i++));
/*     */         
/*     */         case 5:
/*  84 */           localProcessor.touchDragged(q.get(i++), q.get(i++), q.get(i++));
/*     */         
/*     */         case 6:
/*  87 */           localProcessor.mouseMoved(q.get(i++), q.get(i++));
/*     */         
/*     */         case 7:
/*  90 */           localProcessor.scrolled(q.get(i++));
/*     */       } 
/*     */     
/*     */     } 
/*  94 */     q.clear();
/*     */   }
/*     */   
/*     */   private void queueTime() {
/*  98 */     long time = TimeUtils.nanoTime();
/*  99 */     this.queue.add((int)(time >> 32L));
/* 100 */     this.queue.add((int)time);
/*     */   }
/*     */   
/*     */   public synchronized boolean keyDown(int keycode) {
/* 104 */     queueTime();
/* 105 */     this.queue.add(0);
/* 106 */     this.queue.add(keycode);
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized boolean keyUp(int keycode) {
/* 111 */     queueTime();
/* 112 */     this.queue.add(1);
/* 113 */     this.queue.add(keycode);
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized boolean keyTyped(char character) {
/* 118 */     queueTime();
/* 119 */     this.queue.add(2);
/* 120 */     this.queue.add(character);
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized boolean touchDown(int screenX, int screenY, int pointer, int button) {
/* 125 */     queueTime();
/* 126 */     this.queue.add(3);
/* 127 */     this.queue.add(screenX);
/* 128 */     this.queue.add(screenY);
/* 129 */     this.queue.add(pointer);
/* 130 */     this.queue.add(button);
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized boolean touchUp(int screenX, int screenY, int pointer, int button) {
/* 135 */     queueTime();
/* 136 */     this.queue.add(4);
/* 137 */     this.queue.add(screenX);
/* 138 */     this.queue.add(screenY);
/* 139 */     this.queue.add(pointer);
/* 140 */     this.queue.add(button);
/* 141 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized boolean touchDragged(int screenX, int screenY, int pointer) {
/* 145 */     queueTime();
/* 146 */     this.queue.add(5);
/* 147 */     this.queue.add(screenX);
/* 148 */     this.queue.add(screenY);
/* 149 */     this.queue.add(pointer);
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized boolean mouseMoved(int screenX, int screenY) {
/* 154 */     queueTime();
/* 155 */     this.queue.add(6);
/* 156 */     this.queue.add(screenX);
/* 157 */     this.queue.add(screenY);
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized boolean scrolled(int amount) {
/* 162 */     queueTime();
/* 163 */     this.queue.add(7);
/* 164 */     this.queue.add(amount);
/* 165 */     return false;
/*     */   }
/*     */   
/*     */   public long getCurrentEventTime() {
/* 169 */     return this.currentEventTime;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\InputEventQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */