/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.Input;
/*     */ import com.badlogic.gdx.InputProcessor;
/*     */ import com.badlogic.gdx.utils.IntSet;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.Robot;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import java.awt.event.MouseWheelListener;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowFocusListener;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.OverlayLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
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
/*     */ public class LwjglAWTInput
/*     */   implements Input, MouseMotionListener, MouseListener, MouseWheelListener, KeyListener
/*     */ {
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
/*     */     static final int TOUCH_MOVED = 3;
/*     */     static final int TOUCH_SCROLLED = 4;
/*     */     long timeStamp;
/*     */     int type;
/*     */     int x;
/*     */     int y;
/*     */     int pointer;
/*     */     int button;
/*     */     int scrollAmount;
/*     */   }
/*     */   
/*  90 */   Pool<KeyEvent> usedKeyEvents = new Pool<KeyEvent>(16, 1000) {
/*     */       protected LwjglAWTInput.KeyEvent newObject() {
/*  92 */         return new LwjglAWTInput.KeyEvent();
/*     */       }
/*     */     };
/*     */   
/*  96 */   Pool<TouchEvent> usedTouchEvents = new Pool<TouchEvent>(16, 1000) {
/*     */       protected LwjglAWTInput.TouchEvent newObject() {
/*  98 */         return new LwjglAWTInput.TouchEvent();
/*     */       }
/*     */     };
/*     */   
/*     */   private final LwjglAWTCanvas lwjglAwtCanvas;
/* 103 */   List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
/* 104 */   List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
/* 105 */   int touchX = 0;
/* 106 */   int touchY = 0;
/* 107 */   int deltaX = 0;
/* 108 */   int deltaY = 0;
/*     */   boolean touchDown = false;
/*     */   boolean justTouched = false;
/* 111 */   int keyCount = 0;
/* 112 */   boolean[] keys = new boolean[256];
/*     */   boolean keyJustPressed = false;
/* 114 */   boolean[] justPressedKeys = new boolean[256];
/* 115 */   IntSet pressedButtons = new IntSet();
/*     */   InputProcessor processor;
/*     */   Canvas canvas;
/*     */   boolean catched = false;
/* 119 */   Robot robot = null;
/*     */   long currentEventTimeStamp;
/*     */   
/*     */   public LwjglAWTInput(LwjglAWTCanvas lwjglAwtCanvas) {
/* 123 */     this.lwjglAwtCanvas = lwjglAwtCanvas;
/* 124 */     setListeners(lwjglAwtCanvas.getCanvas());
/*     */     
/* 126 */     try { this.robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()); }
/* 127 */     catch (HeadlessException headlessException) {  }
/* 128 */     catch (AWTException aWTException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void setListeners(Canvas canvas) {
/* 133 */     if (this.canvas != null) {
/* 134 */       canvas.removeMouseListener(this);
/* 135 */       canvas.removeMouseMotionListener(this);
/* 136 */       canvas.removeMouseWheelListener(this);
/* 137 */       canvas.removeKeyListener(this);
/*     */     } 
/* 139 */     canvas.addMouseListener(this);
/* 140 */     canvas.addMouseMotionListener(this);
/* 141 */     canvas.addMouseWheelListener(this);
/* 142 */     canvas.addKeyListener(this);
/* 143 */     canvas.setFocusTraversalKeysEnabled(false);
/* 144 */     this.canvas = canvas;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerX() {
/* 149 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerY() {
/* 154 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerZ() {
/* 159 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public void getTextInput(final Input.TextInputListener listener, final String title, final String text, final String hint) {
/* 163 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run() {
/* 166 */             JPanel panel = new JPanel(new FlowLayout());
/*     */             
/* 168 */             JPanel textPanel = new JPanel() {
/*     */                 public boolean isOptimizedDrawingEnabled() {
/* 170 */                   return false;
/*     */                 }
/*     */               };
/*     */             
/* 174 */             textPanel.setLayout(new OverlayLayout(textPanel));
/* 175 */             panel.add(textPanel);
/*     */             
/* 177 */             final JTextField textField = new JTextField(20);
/* 178 */             textField.setText(text);
/* 179 */             textField.setAlignmentX(0.0F);
/* 180 */             textPanel.add(textField);
/*     */             
/* 182 */             final JLabel placeholderLabel = new JLabel(hint);
/* 183 */             placeholderLabel.setForeground(Color.GRAY);
/* 184 */             placeholderLabel.setAlignmentX(0.0F);
/* 185 */             textPanel.add(placeholderLabel, 0);
/*     */             
/* 187 */             textField.getDocument().addDocumentListener(new DocumentListener()
/*     */                 {
/*     */                   public void removeUpdate(DocumentEvent arg0)
/*     */                   {
/* 191 */                     updated();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void insertUpdate(DocumentEvent arg0) {
/* 196 */                     updated();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void changedUpdate(DocumentEvent arg0) {
/* 201 */                     updated();
/*     */                   }
/*     */                   
/*     */                   private void updated() {
/* 205 */                     if (textField.getText().length() == 0) {
/* 206 */                       placeholderLabel.setVisible(true);
/*     */                     } else {
/* 208 */                       placeholderLabel.setVisible(false);
/*     */                     } 
/*     */                   }
/*     */                 });
/* 212 */             JOptionPane pane = new JOptionPane(panel, 3, 2, null, null, null);
/*     */ 
/*     */             
/* 215 */             pane.setInitialValue((Object)null);
/* 216 */             pane.setComponentOrientation(JOptionPane.getRootFrame().getComponentOrientation());
/*     */             
/* 218 */             Border border = textField.getBorder();
/* 219 */             placeholderLabel.setBorder(new EmptyBorder(border.getBorderInsets(textField)));
/*     */             
/* 221 */             JDialog dialog = pane.createDialog((Component)null, title);
/* 222 */             pane.selectInitialValue();
/*     */             
/* 224 */             dialog.addWindowFocusListener(new WindowFocusListener()
/*     */                 {
/*     */                   public void windowLostFocus(WindowEvent arg0) {}
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void windowGainedFocus(WindowEvent arg0) {
/* 232 */                     textField.requestFocusInWindow();
/*     */                   }
/*     */                 });
/*     */             
/* 236 */             dialog.setVisible(true);
/* 237 */             dialog.dispose();
/*     */             
/* 239 */             Object selectedValue = pane.getValue();
/*     */             
/* 241 */             if (selectedValue != null && selectedValue instanceof Integer && ((Integer)selectedValue)
/* 242 */               .intValue() == 0) {
/* 243 */               listener.input(textField.getText());
/*     */             } else {
/* 245 */               listener.canceled();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getX() {
/* 254 */     return this.touchX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX(int pointer) {
/* 259 */     if (pointer == 0) {
/* 260 */       return this.touchX;
/*     */     }
/* 262 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/* 267 */     return this.touchY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY(int pointer) {
/* 272 */     if (pointer == 0) {
/* 273 */       return this.touchY;
/*     */     }
/* 275 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isKeyPressed(int key) {
/* 280 */     if (key == -1) {
/* 281 */       return (this.keyCount > 0);
/*     */     }
/* 283 */     if (key < 0 || key > 255) {
/* 284 */       return false;
/*     */     }
/* 286 */     return this.keys[key];
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isKeyJustPressed(int key) {
/* 291 */     if (key == -1) {
/* 292 */       return this.keyJustPressed;
/*     */     }
/* 294 */     if (key < 0 || key > 255) {
/* 295 */       return false;
/*     */     }
/* 297 */     return this.justPressedKeys[key];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouched() {
/* 302 */     return this.touchDown;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouched(int pointer) {
/* 307 */     if (pointer == 0) {
/* 308 */       return this.touchDown;
/*     */     }
/* 310 */     return false;
/*     */   }
/*     */   
/*     */   void processEvents() {
/* 314 */     synchronized (this) {
/* 315 */       this.justTouched = false;
/* 316 */       if (this.keyJustPressed) {
/* 317 */         this.keyJustPressed = false;
/* 318 */         for (int i = 0; i < this.justPressedKeys.length; i++) {
/* 319 */           this.justPressedKeys[i] = false;
/*     */         }
/*     */       } 
/*     */       
/* 323 */       if (this.processor != null) {
/* 324 */         InputProcessor processor = this.processor;
/*     */         
/* 326 */         int len = this.keyEvents.size(); int i;
/* 327 */         for (i = 0; i < len; i++) {
/* 328 */           KeyEvent e = this.keyEvents.get(i);
/* 329 */           this.currentEventTimeStamp = e.timeStamp;
/* 330 */           switch (e.type) {
/*     */             case 0:
/* 332 */               processor.keyDown(e.keyCode);
/* 333 */               this.keyJustPressed = true;
/* 334 */               this.justPressedKeys[e.keyCode] = true;
/*     */               break;
/*     */             case 1:
/* 337 */               processor.keyUp(e.keyCode);
/*     */               break;
/*     */             case 2:
/* 340 */               processor.keyTyped(e.keyChar); break;
/*     */           } 
/* 342 */           this.usedKeyEvents.free(e);
/*     */         } 
/*     */         
/* 345 */         len = this.touchEvents.size();
/* 346 */         for (i = 0; i < len; i++) {
/* 347 */           TouchEvent e = this.touchEvents.get(i);
/* 348 */           this.currentEventTimeStamp = e.timeStamp;
/* 349 */           switch (e.type) {
/*     */             case 0:
/* 351 */               processor.touchDown(e.x, e.y, e.pointer, e.button);
/* 352 */               this.justTouched = true;
/*     */               break;
/*     */             case 1:
/* 355 */               processor.touchUp(e.x, e.y, e.pointer, e.button);
/*     */               break;
/*     */             case 2:
/* 358 */               processor.touchDragged(e.x, e.y, e.pointer);
/*     */               break;
/*     */             case 3:
/* 361 */               processor.mouseMoved(e.x, e.y);
/*     */               break;
/*     */             case 4:
/* 364 */               processor.scrolled(e.scrollAmount);
/*     */               break;
/*     */           } 
/* 367 */           this.usedTouchEvents.free(e);
/*     */         } 
/*     */       } else {
/* 370 */         int len = this.touchEvents.size(); int i;
/* 371 */         for (i = 0; i < len; i++) {
/* 372 */           TouchEvent event = this.touchEvents.get(i);
/* 373 */           if (event.type == 0) this.justTouched = true; 
/* 374 */           this.usedTouchEvents.free(event);
/*     */         } 
/*     */         
/* 377 */         len = this.keyEvents.size();
/* 378 */         for (i = 0; i < len; i++) {
/* 379 */           this.usedKeyEvents.free(this.keyEvents.get(i));
/*     */         }
/*     */       } 
/*     */       
/* 383 */       if (this.touchEvents.size() == 0) {
/* 384 */         this.deltaX = 0;
/* 385 */         this.deltaY = 0;
/*     */       } 
/*     */       
/* 388 */       this.keyEvents.clear();
/* 389 */       this.touchEvents.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchBackKey(boolean catchBack) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCatchBackKey() {
/* 400 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchMenuKey(boolean catchMenu) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCatchMenuKey() {
/* 410 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnscreenKeyboardVisible(boolean visible) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseDragged(MouseEvent e) {
/* 420 */     synchronized (this) {
/* 421 */       TouchEvent event = (TouchEvent)this.usedTouchEvents.obtain();
/* 422 */       event.pointer = 0;
/* 423 */       event.x = e.getX();
/* 424 */       event.y = e.getY();
/* 425 */       event.type = 2;
/* 426 */       event.timeStamp = System.nanoTime();
/* 427 */       this.touchEvents.add(event);
/*     */       
/* 429 */       this.deltaX = event.x - this.touchX;
/* 430 */       this.deltaY = event.y - this.touchY;
/* 431 */       this.touchX = event.x;
/* 432 */       this.touchY = event.y;
/* 433 */       checkCatched(e);
/* 434 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseMoved(MouseEvent e) {
/* 440 */     synchronized (this) {
/* 441 */       TouchEvent event = (TouchEvent)this.usedTouchEvents.obtain();
/* 442 */       event.pointer = 0;
/* 443 */       event.x = e.getX();
/* 444 */       event.y = e.getY();
/* 445 */       event.type = 3;
/* 446 */       event.timeStamp = System.nanoTime();
/* 447 */       this.touchEvents.add(event);
/*     */       
/* 449 */       this.deltaX = event.x - this.touchX;
/* 450 */       this.deltaY = event.y - this.touchY;
/* 451 */       this.touchX = event.x;
/* 452 */       this.touchY = event.y;
/* 453 */       checkCatched(e);
/* 454 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseClicked(MouseEvent arg0) {}
/*     */ 
/*     */   
/*     */   public void mouseEntered(MouseEvent e) {
/* 464 */     this.touchX = e.getX();
/* 465 */     this.touchY = e.getY();
/* 466 */     checkCatched(e);
/* 467 */     this.lwjglAwtCanvas.graphics.requestRendering();
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseExited(MouseEvent e) {
/* 472 */     checkCatched(e);
/* 473 */     this.lwjglAwtCanvas.graphics.requestRendering();
/*     */   }
/*     */   
/*     */   private void checkCatched(MouseEvent e) {
/* 477 */     if (this.catched && this.robot != null && this.canvas.isShowing()) {
/* 478 */       int x = Math.max(0, Math.min(e.getX(), this.canvas.getWidth()) - 1) + (this.canvas.getLocationOnScreen()).x;
/* 479 */       int y = Math.max(0, Math.min(e.getY(), this.canvas.getHeight()) - 1) + (this.canvas.getLocationOnScreen()).y;
/* 480 */       if (e.getX() < 0 || e.getX() >= this.canvas.getWidth() || e.getY() < 0 || e.getY() >= this.canvas.getHeight()) {
/* 481 */         this.robot.mouseMove(x, y);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int toGdxButton(int swingButton) {
/* 487 */     if (swingButton == 1) return 0; 
/* 488 */     if (swingButton == 2) return 2; 
/* 489 */     if (swingButton == 3) return 1; 
/* 490 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void mousePressed(MouseEvent e) {
/* 495 */     synchronized (this) {
/* 496 */       TouchEvent event = (TouchEvent)this.usedTouchEvents.obtain();
/* 497 */       event.pointer = 0;
/* 498 */       event.x = e.getX();
/* 499 */       event.y = e.getY();
/* 500 */       event.type = 0;
/* 501 */       event.button = toGdxButton(e.getButton());
/* 502 */       event.timeStamp = System.nanoTime();
/* 503 */       this.touchEvents.add(event);
/*     */       
/* 505 */       this.deltaX = event.x - this.touchX;
/* 506 */       this.deltaY = event.y - this.touchY;
/* 507 */       this.touchX = event.x;
/* 508 */       this.touchY = event.y;
/* 509 */       this.touchDown = true;
/* 510 */       this.pressedButtons.add(event.button);
/* 511 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(MouseEvent e) {
/* 517 */     synchronized (this) {
/* 518 */       TouchEvent event = (TouchEvent)this.usedTouchEvents.obtain();
/* 519 */       event.pointer = 0;
/* 520 */       event.x = e.getX();
/* 521 */       event.y = e.getY();
/* 522 */       event.button = toGdxButton(e.getButton());
/* 523 */       event.type = 1;
/* 524 */       event.timeStamp = System.nanoTime();
/* 525 */       this.touchEvents.add(event);
/*     */       
/* 527 */       this.deltaX = event.x - this.touchX;
/* 528 */       this.deltaY = event.y - this.touchY;
/* 529 */       this.touchX = event.x;
/* 530 */       this.touchY = event.y;
/* 531 */       this.pressedButtons.remove(event.button);
/* 532 */       if (this.pressedButtons.size == 0) this.touchDown = false; 
/* 533 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseWheelMoved(MouseWheelEvent e) {
/* 539 */     synchronized (this) {
/* 540 */       TouchEvent event = (TouchEvent)this.usedTouchEvents.obtain();
/* 541 */       event.pointer = 0;
/* 542 */       event.type = 4;
/* 543 */       event.scrollAmount = e.getWheelRotation();
/* 544 */       event.timeStamp = System.nanoTime();
/* 545 */       this.touchEvents.add(event);
/* 546 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyPressed(java.awt.event.KeyEvent e) {
/* 552 */     synchronized (this) {
/* 553 */       KeyEvent event = (KeyEvent)this.usedKeyEvents.obtain();
/* 554 */       event.keyChar = Character.MIN_VALUE;
/* 555 */       event.keyCode = translateKeyCode(e.getKeyCode());
/* 556 */       event.type = 0;
/* 557 */       event.timeStamp = System.nanoTime();
/* 558 */       this.keyEvents.add(event);
/* 559 */       if (!this.keys[event.keyCode]) {
/* 560 */         this.keyCount++;
/* 561 */         this.keys[event.keyCode] = true;
/*     */       } 
/* 563 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyReleased(java.awt.event.KeyEvent e) {
/* 569 */     synchronized (this) {
/* 570 */       KeyEvent event = (KeyEvent)this.usedKeyEvents.obtain();
/* 571 */       event.keyChar = Character.MIN_VALUE;
/* 572 */       event.keyCode = translateKeyCode(e.getKeyCode());
/* 573 */       event.type = 1;
/* 574 */       event.timeStamp = System.nanoTime();
/* 575 */       this.keyEvents.add(event);
/* 576 */       if (this.keys[event.keyCode]) {
/* 577 */         this.keyCount--;
/* 578 */         this.keys[event.keyCode] = false;
/*     */       } 
/* 580 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyTyped(java.awt.event.KeyEvent e) {
/* 586 */     synchronized (this) {
/* 587 */       KeyEvent event = (KeyEvent)this.usedKeyEvents.obtain();
/* 588 */       event.keyChar = e.getKeyChar();
/* 589 */       event.keyCode = 0;
/* 590 */       event.type = 2;
/* 591 */       event.timeStamp = System.nanoTime();
/* 592 */       this.keyEvents.add(event);
/* 593 */       this.lwjglAwtCanvas.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static int translateKeyCode(int keyCode) {
/* 598 */     switch (keyCode) {
/*     */       case 107:
/* 600 */         return 81;
/*     */       case 109:
/* 602 */         return 69;
/*     */       case 48:
/* 604 */         return 7;
/*     */       case 49:
/* 606 */         return 8;
/*     */       case 50:
/* 608 */         return 9;
/*     */       case 51:
/* 610 */         return 10;
/*     */       case 52:
/* 612 */         return 11;
/*     */       case 53:
/* 614 */         return 12;
/*     */       case 54:
/* 616 */         return 13;
/*     */       case 55:
/* 618 */         return 14;
/*     */       case 56:
/* 620 */         return 15;
/*     */       case 57:
/* 622 */         return 16;
/*     */       case 65:
/* 624 */         return 29;
/*     */       case 66:
/* 626 */         return 30;
/*     */       case 67:
/* 628 */         return 31;
/*     */       case 68:
/* 630 */         return 32;
/*     */       case 69:
/* 632 */         return 33;
/*     */       case 70:
/* 634 */         return 34;
/*     */       case 71:
/* 636 */         return 35;
/*     */       case 72:
/* 638 */         return 36;
/*     */       case 73:
/* 640 */         return 37;
/*     */       case 74:
/* 642 */         return 38;
/*     */       case 75:
/* 644 */         return 39;
/*     */       case 76:
/* 646 */         return 40;
/*     */       case 77:
/* 648 */         return 41;
/*     */       case 78:
/* 650 */         return 42;
/*     */       case 79:
/* 652 */         return 43;
/*     */       case 80:
/* 654 */         return 44;
/*     */       case 81:
/* 656 */         return 45;
/*     */       case 82:
/* 658 */         return 46;
/*     */       case 83:
/* 660 */         return 47;
/*     */       case 84:
/* 662 */         return 48;
/*     */       case 85:
/* 664 */         return 49;
/*     */       case 86:
/* 666 */         return 50;
/*     */       case 87:
/* 668 */         return 51;
/*     */       case 88:
/* 670 */         return 52;
/*     */       case 89:
/* 672 */         return 53;
/*     */       case 90:
/* 674 */         return 54;
/*     */       case 18:
/* 676 */         return 57;
/*     */       case 65406:
/* 678 */         return 58;
/*     */       case 92:
/* 680 */         return 73;
/*     */       case 44:
/* 682 */         return 55;
/*     */       case 127:
/* 684 */         return 112;
/*     */       case 37:
/* 686 */         return 21;
/*     */       case 39:
/* 688 */         return 22;
/*     */       case 38:
/* 690 */         return 19;
/*     */       case 40:
/* 692 */         return 20;
/*     */       case 10:
/* 694 */         return 66;
/*     */       case 36:
/* 696 */         return 3;
/*     */       case 45:
/* 698 */         return 69;
/*     */       case 46:
/* 700 */         return 56;
/*     */       case 521:
/* 702 */         return 81;
/*     */       case 59:
/* 704 */         return 74;
/*     */       case 16:
/* 706 */         return 59;
/*     */       case 47:
/* 708 */         return 76;
/*     */       case 32:
/* 710 */         return 62;
/*     */       case 9:
/* 712 */         return 61;
/*     */       case 8:
/* 714 */         return 67;
/*     */       case 17:
/* 716 */         return 129;
/*     */       case 27:
/* 718 */         return 131;
/*     */       case 35:
/* 720 */         return 132;
/*     */       case 155:
/* 722 */         return 133;
/*     */       case 33:
/* 724 */         return 92;
/*     */       case 34:
/* 726 */         return 93;
/*     */       case 112:
/* 728 */         return 244;
/*     */       case 113:
/* 730 */         return 245;
/*     */       case 114:
/* 732 */         return 246;
/*     */       case 115:
/* 734 */         return 247;
/*     */       case 116:
/* 736 */         return 248;
/*     */       case 117:
/* 738 */         return 249;
/*     */       case 118:
/* 740 */         return 250;
/*     */       case 119:
/* 742 */         return 251;
/*     */       case 120:
/* 744 */         return 252;
/*     */       case 121:
/* 746 */         return 253;
/*     */       case 122:
/* 748 */         return 254;
/*     */       case 123:
/* 750 */         return 255;
/*     */       case 513:
/* 752 */         return 243;
/*     */       case 96:
/* 754 */         return 7;
/*     */       case 97:
/* 756 */         return 8;
/*     */       case 98:
/* 758 */         return 9;
/*     */       case 99:
/* 760 */         return 10;
/*     */       case 100:
/* 762 */         return 11;
/*     */       case 101:
/* 764 */         return 12;
/*     */       case 102:
/* 766 */         return 13;
/*     */       case 103:
/* 768 */         return 14;
/*     */       case 104:
/* 770 */         return 15;
/*     */       case 105:
/* 772 */         return 16;
/*     */     } 
/* 774 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInputProcessor(InputProcessor processor) {
/* 779 */     synchronized (this) {
/* 780 */       this.processor = processor;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputProcessor getInputProcessor() {
/* 786 */     return this.processor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void vibrate(int milliseconds) {}
/*     */ 
/*     */   
/*     */   public boolean justTouched() {
/* 795 */     return this.justTouched;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isButtonPressed(int button) {
/* 800 */     return this.pressedButtons.contains(button);
/*     */   }
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
/*     */   public float getAzimuth() {
/* 813 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 818 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRoll() {
/* 823 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPeripheralAvailable(Input.Peripheral peripheral) {
/* 828 */     if (peripheral == Input.Peripheral.HardwareKeyboard) return true; 
/* 829 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRotation() {
/* 834 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input.Orientation getNativeOrientation() {
/* 839 */     return Input.Orientation.Landscape;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorCatched(boolean catched) {
/* 844 */     this.catched = catched;
/* 845 */     showCursor(!catched);
/*     */   }
/*     */   
/*     */   private void showCursor(boolean visible) {
/* 849 */     if (!visible) {
/* 850 */       Toolkit t = Toolkit.getDefaultToolkit();
/* 851 */       Image i = new BufferedImage(1, 1, 2);
/* 852 */       Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none");
/* 853 */       JFrame frame = findJFrame(this.canvas);
/* 854 */       frame.setCursor(noCursor);
/*     */     } else {
/* 856 */       JFrame frame = findJFrame(this.canvas);
/* 857 */       frame.setCursor(Cursor.getDefaultCursor());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static JFrame findJFrame(Component component) {
/* 862 */     Container parent = component.getParent();
/* 863 */     while (parent != null) {
/* 864 */       if (parent instanceof JFrame) {
/* 865 */         return (JFrame)parent;
/*     */       }
/* 867 */       parent = parent.getParent();
/*     */     } 
/*     */     
/* 870 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCursorCatched() {
/* 875 */     return this.catched;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaX() {
/* 880 */     return this.deltaX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaX(int pointer) {
/* 885 */     if (pointer == 0) return this.deltaX; 
/* 886 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaY() {
/* 891 */     return this.deltaY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaY(int pointer) {
/* 896 */     if (pointer == 0) return this.deltaY; 
/* 897 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorPosition(int x, int y) {
/* 902 */     if (this.robot != null) {
/* 903 */       this.robot.mouseMove((this.canvas.getLocationOnScreen()).x + x, (this.canvas.getLocationOnScreen()).y + y);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCurrentEventTime() {
/* 909 */     return this.currentEventTimeStamp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getRotationMatrix(float[] matrix) {}
/*     */ 
/*     */   
/*     */   public float getGyroscopeX() {
/* 918 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeY() {
/* 923 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeZ() {
/* 928 */     return 0.0F;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglAWTInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */