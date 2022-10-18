/*      */ package com.badlogic.gdx.backends.lwjgl;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.Input;
/*      */ import com.badlogic.gdx.InputProcessor;
/*      */ import com.badlogic.gdx.utils.IntSet;
/*      */ import com.badlogic.gdx.utils.Pool;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowFocusListener;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.OverlayLayout;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.Display;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class LwjglInput
/*      */   implements Input
/*      */ {
/*   60 */   public static float keyRepeatInitialTime = 0.4F;
/*   61 */   public static float keyRepeatTime = 0.1F;
/*      */   
/*   63 */   List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
/*   64 */   List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
/*      */   
/*      */   boolean mousePressed = false;
/*      */   int mouseX;
/*   68 */   int pressedKeys = 0; int mouseY; int deltaX; int deltaY;
/*      */   boolean keyJustPressed = false;
/*   70 */   boolean[] justPressedKeys = new boolean[256];
/*      */   boolean justTouched = false;
/*   72 */   IntSet pressedButtons = new IntSet();
/*      */   InputProcessor processor;
/*      */   char lastKeyCharPressed;
/*      */   float keyRepeatTimer;
/*      */   long currentEventTimeStamp;
/*      */   float deltaTime;
/*      */   long lastTime;
/*      */   
/*   80 */   Pool<KeyEvent> usedKeyEvents = new Pool<KeyEvent>(16, 1000) {
/*      */       protected LwjglInput.KeyEvent newObject() {
/*   82 */         return new LwjglInput.KeyEvent();
/*      */       }
/*      */     };
/*      */   
/*   86 */   Pool<TouchEvent> usedTouchEvents = new Pool<TouchEvent>(16, 1000) {
/*      */       protected LwjglInput.TouchEvent newObject() {
/*   88 */         return new LwjglInput.TouchEvent();
/*      */       }
/*      */     };
/*      */   
/*      */   public LwjglInput() {
/*   93 */     Keyboard.enableRepeatEvents(false);
/*   94 */     Mouse.setClipMouseCoordinatesToWindow(false);
/*      */   }
/*      */   
/*      */   public float getAccelerometerX() {
/*   98 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public float getAccelerometerY() {
/*  102 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public float getAccelerometerZ() {
/*  106 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public float getGyroscopeX() {
/*  110 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public float getGyroscopeY() {
/*  114 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public float getGyroscopeZ() {
/*  118 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public void getTextInput(final Input.TextInputListener listener, final String title, final String text, final String hint) {
/*  122 */     SwingUtilities.invokeLater(new Runnable()
/*      */         {
/*      */           public void run() {
/*  125 */             JPanel panel = new JPanel(new FlowLayout());
/*      */             
/*  127 */             JPanel textPanel = new JPanel() {
/*      */                 public boolean isOptimizedDrawingEnabled() {
/*  129 */                   return false;
/*      */                 }
/*      */               };
/*      */             
/*  133 */             textPanel.setLayout(new OverlayLayout(textPanel));
/*  134 */             panel.add(textPanel);
/*      */             
/*  136 */             final JTextField textField = new JTextField(20);
/*  137 */             textField.setText(text);
/*  138 */             textField.setAlignmentX(0.0F);
/*  139 */             textPanel.add(textField);
/*      */             
/*  141 */             final JLabel placeholderLabel = new JLabel(hint);
/*  142 */             placeholderLabel.setForeground(Color.GRAY);
/*  143 */             placeholderLabel.setAlignmentX(0.0F);
/*  144 */             textPanel.add(placeholderLabel, 0);
/*      */             
/*  146 */             textField.getDocument().addDocumentListener(new DocumentListener()
/*      */                 {
/*      */                   public void removeUpdate(DocumentEvent arg0)
/*      */                   {
/*  150 */                     updated();
/*      */                   }
/*      */ 
/*      */                   
/*      */                   public void insertUpdate(DocumentEvent arg0) {
/*  155 */                     updated();
/*      */                   }
/*      */ 
/*      */                   
/*      */                   public void changedUpdate(DocumentEvent arg0) {
/*  160 */                     updated();
/*      */                   }
/*      */                   
/*      */                   private void updated() {
/*  164 */                     if (textField.getText().length() == 0) {
/*  165 */                       placeholderLabel.setVisible(true);
/*      */                     } else {
/*  167 */                       placeholderLabel.setVisible(false);
/*      */                     } 
/*      */                   }
/*      */                 });
/*  171 */             JOptionPane pane = new JOptionPane(panel, 3, 2, null, null, null);
/*      */ 
/*      */             
/*  174 */             pane.setInitialValue((Object)null);
/*  175 */             pane.setComponentOrientation(JOptionPane.getRootFrame().getComponentOrientation());
/*      */             
/*  177 */             Border border = textField.getBorder();
/*  178 */             placeholderLabel.setBorder(new EmptyBorder(border.getBorderInsets(textField)));
/*      */             
/*  180 */             JDialog dialog = pane.createDialog((Component)null, title);
/*  181 */             pane.selectInitialValue();
/*      */             
/*  183 */             dialog.addWindowFocusListener(new WindowFocusListener()
/*      */                 {
/*      */                   public void windowLostFocus(WindowEvent arg0) {}
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*      */                   public void windowGainedFocus(WindowEvent arg0) {
/*  191 */                     textField.requestFocusInWindow();
/*      */                   }
/*      */                 });
/*      */             
/*  195 */             dialog.setVisible(true);
/*  196 */             dialog.dispose();
/*      */             
/*  198 */             Object selectedValue = pane.getValue();
/*      */             
/*  200 */             if (selectedValue != null && selectedValue instanceof Integer && ((Integer)selectedValue)
/*  201 */               .intValue() == 0) {
/*  202 */               listener.input(textField.getText());
/*      */             } else {
/*  204 */               listener.canceled();
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public int getX() {
/*  212 */     return (int)(Mouse.getX() * Display.getPixelScaleFactor());
/*      */   }
/*      */   
/*      */   public int getY() {
/*  216 */     return Gdx.graphics.getHeight() - 1 - (int)(Mouse.getY() * Display.getPixelScaleFactor());
/*      */   }
/*      */   
/*      */   public boolean isAccelerometerAvailable() {
/*  220 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isGyroscopeAvailable() {
/*  224 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isKeyPressed(int key) {
/*  228 */     if (!Keyboard.isCreated()) return false;
/*      */     
/*  230 */     if (key == -1) {
/*  231 */       return (this.pressedKeys > 0);
/*      */     }
/*  233 */     return Keyboard.isKeyDown(getLwjglKeyCode(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isKeyJustPressed(int key) {
/*  238 */     if (key == -1) {
/*  239 */       return this.keyJustPressed;
/*      */     }
/*  241 */     if (key < 0 || key > 255) {
/*  242 */       return false;
/*      */     }
/*  244 */     return this.justPressedKeys[key];
/*      */   }
/*      */   
/*      */   public boolean isTouched() {
/*  248 */     boolean button = (Mouse.isButtonDown(0) || Mouse.isButtonDown(1) || Mouse.isButtonDown(2));
/*  249 */     return button;
/*      */   }
/*      */   
/*      */   public int getX(int pointer) {
/*  253 */     if (pointer > 0) {
/*  254 */       return 0;
/*      */     }
/*  256 */     return getX();
/*      */   }
/*      */   
/*      */   public int getY(int pointer) {
/*  260 */     if (pointer > 0) {
/*  261 */       return 0;
/*      */     }
/*  263 */     return getY();
/*      */   }
/*      */   
/*      */   public boolean isTouched(int pointer) {
/*  267 */     if (pointer > 0) {
/*  268 */       return false;
/*      */     }
/*  270 */     return isTouched();
/*      */   }
/*      */   
/*      */   public boolean supportsMultitouch() {
/*  274 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOnscreenKeyboardVisible(boolean visible) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCatchBackKey(boolean catchBack) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCatchBackKey() {
/*  289 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCatchMenuKey(boolean catchMenu) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCatchMenuKey() {
/*  299 */     return false;
/*      */   }
/*      */   
/*      */   void processEvents() {
/*  303 */     synchronized (this) {
/*  304 */       if (this.processor != null) {
/*  305 */         InputProcessor processor = this.processor;
/*  306 */         int len = this.keyEvents.size(); int i;
/*  307 */         for (i = 0; i < len; i++) {
/*  308 */           KeyEvent e = this.keyEvents.get(i);
/*  309 */           this.currentEventTimeStamp = e.timeStamp;
/*  310 */           switch (e.type) {
/*      */             case 0:
/*  312 */               processor.keyDown(e.keyCode);
/*      */               break;
/*      */             case 1:
/*  315 */               processor.keyUp(e.keyCode);
/*      */               break;
/*      */             case 2:
/*  318 */               processor.keyTyped(e.keyChar); break;
/*      */           } 
/*  320 */           this.usedKeyEvents.free(e);
/*      */         } 
/*      */         
/*  323 */         len = this.touchEvents.size();
/*  324 */         for (i = 0; i < len; i++) {
/*  325 */           TouchEvent e = this.touchEvents.get(i);
/*  326 */           this.currentEventTimeStamp = e.timeStamp;
/*  327 */           switch (e.type) {
/*      */             case 0:
/*  329 */               processor.touchDown(e.x, e.y, e.pointer, e.button);
/*      */               break;
/*      */             case 1:
/*  332 */               processor.touchUp(e.x, e.y, e.pointer, e.button);
/*      */               break;
/*      */             case 2:
/*  335 */               processor.touchDragged(e.x, e.y, e.pointer);
/*      */               break;
/*      */             case 4:
/*  338 */               processor.mouseMoved(e.x, e.y);
/*      */               break;
/*      */             case 3:
/*  341 */               processor.scrolled(e.scrollAmount); break;
/*      */           } 
/*  343 */           this.usedTouchEvents.free(e);
/*      */         } 
/*      */       } else {
/*  346 */         int len = this.touchEvents.size(); int i;
/*  347 */         for (i = 0; i < len; i++) {
/*  348 */           this.usedTouchEvents.free(this.touchEvents.get(i));
/*      */         }
/*      */         
/*  351 */         len = this.keyEvents.size();
/*  352 */         for (i = 0; i < len; i++) {
/*  353 */           this.usedKeyEvents.free(this.keyEvents.get(i));
/*      */         }
/*      */       } 
/*      */       
/*  357 */       this.keyEvents.clear();
/*  358 */       this.touchEvents.clear();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int getGdxKeyCode(int lwjglKeyCode) {
/*  363 */     switch (lwjglKeyCode) {
/*      */       case 26:
/*  365 */         return 71;
/*      */       case 27:
/*  367 */         return 72;
/*      */       case 41:
/*  369 */         return 68;
/*      */       case 55:
/*  371 */         return 17;
/*      */       case 69:
/*  373 */         return 78;
/*      */       case 83:
/*  375 */         return 56;
/*      */       case 181:
/*  377 */         return 76;
/*      */       case 219:
/*  379 */         return 63;
/*      */       case 220:
/*  381 */         return 63;
/*      */       case 141:
/*  383 */         return 70;
/*      */       case 145:
/*  385 */         return 77;
/*      */       case 13:
/*  387 */         return 70;
/*      */       case 179:
/*  389 */         return 55;
/*      */       case 156:
/*  391 */         return 66;
/*      */       case 11:
/*  393 */         return 7;
/*      */       case 2:
/*  395 */         return 8;
/*      */       case 3:
/*  397 */         return 9;
/*      */       case 4:
/*  399 */         return 10;
/*      */       case 5:
/*  401 */         return 11;
/*      */       case 6:
/*  403 */         return 12;
/*      */       case 7:
/*  405 */         return 13;
/*      */       case 8:
/*  407 */         return 14;
/*      */       case 9:
/*  409 */         return 15;
/*      */       case 10:
/*  411 */         return 16;
/*      */       case 30:
/*  413 */         return 29;
/*      */       case 48:
/*  415 */         return 30;
/*      */       case 46:
/*  417 */         return 31;
/*      */       case 32:
/*  419 */         return 32;
/*      */       case 18:
/*  421 */         return 33;
/*      */       case 33:
/*  423 */         return 34;
/*      */       case 34:
/*  425 */         return 35;
/*      */       case 35:
/*  427 */         return 36;
/*      */       case 23:
/*  429 */         return 37;
/*      */       case 36:
/*  431 */         return 38;
/*      */       case 37:
/*  433 */         return 39;
/*      */       case 38:
/*  435 */         return 40;
/*      */       case 50:
/*  437 */         return 41;
/*      */       case 49:
/*  439 */         return 42;
/*      */       case 24:
/*  441 */         return 43;
/*      */       case 25:
/*  443 */         return 44;
/*      */       case 16:
/*  445 */         return 45;
/*      */       case 19:
/*  447 */         return 46;
/*      */       case 31:
/*  449 */         return 47;
/*      */       case 20:
/*  451 */         return 48;
/*      */       case 22:
/*  453 */         return 49;
/*      */       case 47:
/*  455 */         return 50;
/*      */       case 17:
/*  457 */         return 51;
/*      */       case 45:
/*  459 */         return 52;
/*      */       case 21:
/*  461 */         return 53;
/*      */       case 44:
/*  463 */         return 54;
/*      */       case 56:
/*  465 */         return 57;
/*      */       case 184:
/*  467 */         return 58;
/*      */       case 43:
/*  469 */         return 73;
/*      */       case 51:
/*  471 */         return 55;
/*      */       case 211:
/*  473 */         return 112;
/*      */       case 203:
/*  475 */         return 21;
/*      */       case 205:
/*  477 */         return 22;
/*      */       case 200:
/*  479 */         return 19;
/*      */       case 208:
/*  481 */         return 20;
/*      */       case 28:
/*  483 */         return 66;
/*      */       case 199:
/*  485 */         return 3;
/*      */       case 12:
/*  487 */         return 69;
/*      */       case 52:
/*  489 */         return 56;
/*      */       case 78:
/*  491 */         return 81;
/*      */       case 39:
/*  493 */         return 74;
/*      */       case 42:
/*  495 */         return 59;
/*      */       case 54:
/*  497 */         return 60;
/*      */       case 53:
/*  499 */         return 76;
/*      */       case 57:
/*  501 */         return 62;
/*      */       case 15:
/*  503 */         return 61;
/*      */       case 29:
/*  505 */         return 129;
/*      */       case 157:
/*  507 */         return 130;
/*      */       case 209:
/*  509 */         return 93;
/*      */       case 201:
/*  511 */         return 92;
/*      */       case 1:
/*  513 */         return 131;
/*      */       case 207:
/*  515 */         return 132;
/*      */       case 210:
/*  517 */         return 133;
/*      */       case 14:
/*  519 */         return 67;
/*      */       case 74:
/*  521 */         return 69;
/*      */       case 40:
/*  523 */         return 75;
/*      */       case 59:
/*  525 */         return 244;
/*      */       case 60:
/*  527 */         return 245;
/*      */       case 61:
/*  529 */         return 246;
/*      */       case 62:
/*  531 */         return 247;
/*      */       case 63:
/*  533 */         return 248;
/*      */       case 64:
/*  535 */         return 249;
/*      */       case 65:
/*  537 */         return 250;
/*      */       case 66:
/*  539 */         return 251;
/*      */       case 67:
/*  541 */         return 252;
/*      */       case 68:
/*  543 */         return 253;
/*      */       case 87:
/*  545 */         return 254;
/*      */       case 88:
/*  547 */         return 255;
/*      */       case 146:
/*  549 */         return 243;
/*      */       case 82:
/*  551 */         return 144;
/*      */       case 79:
/*  553 */         return 145;
/*      */       case 80:
/*  555 */         return 146;
/*      */       case 81:
/*  557 */         return 147;
/*      */       case 75:
/*  559 */         return 148;
/*      */       case 76:
/*  561 */         return 149;
/*      */       case 77:
/*  563 */         return 150;
/*      */       case 71:
/*  565 */         return 151;
/*      */       case 72:
/*  567 */         return 152;
/*      */       case 73:
/*  569 */         return 153;
/*      */     } 
/*  571 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getLwjglKeyCode(int gdxKeyCode) {
/*  576 */     switch (gdxKeyCode) {
/*      */       case 75:
/*  578 */         return 40;
/*      */       case 71:
/*  580 */         return 26;
/*      */       case 72:
/*  582 */         return 27;
/*      */       case 68:
/*  584 */         return 41;
/*      */       case 17:
/*  586 */         return 55;
/*      */       case 78:
/*  588 */         return 69;
/*      */       case 77:
/*  590 */         return 145;
/*      */       case 70:
/*  592 */         return 13;
/*      */       case 63:
/*  594 */         return 219;
/*      */       case 7:
/*  596 */         return 11;
/*      */       case 8:
/*  598 */         return 2;
/*      */       case 9:
/*  600 */         return 3;
/*      */       case 10:
/*  602 */         return 4;
/*      */       case 11:
/*  604 */         return 5;
/*      */       case 12:
/*  606 */         return 6;
/*      */       case 13:
/*  608 */         return 7;
/*      */       case 14:
/*  610 */         return 8;
/*      */       case 15:
/*  612 */         return 9;
/*      */       case 16:
/*  614 */         return 10;
/*      */       case 29:
/*  616 */         return 30;
/*      */       case 30:
/*  618 */         return 48;
/*      */       case 31:
/*  620 */         return 46;
/*      */       case 32:
/*  622 */         return 32;
/*      */       case 33:
/*  624 */         return 18;
/*      */       case 34:
/*  626 */         return 33;
/*      */       case 35:
/*  628 */         return 34;
/*      */       case 36:
/*  630 */         return 35;
/*      */       case 37:
/*  632 */         return 23;
/*      */       case 38:
/*  634 */         return 36;
/*      */       case 39:
/*  636 */         return 37;
/*      */       case 40:
/*  638 */         return 38;
/*      */       case 41:
/*  640 */         return 50;
/*      */       case 42:
/*  642 */         return 49;
/*      */       case 43:
/*  644 */         return 24;
/*      */       case 44:
/*  646 */         return 25;
/*      */       case 45:
/*  648 */         return 16;
/*      */       case 46:
/*  650 */         return 19;
/*      */       case 47:
/*  652 */         return 31;
/*      */       case 48:
/*  654 */         return 20;
/*      */       case 49:
/*  656 */         return 22;
/*      */       case 50:
/*  658 */         return 47;
/*      */       case 51:
/*  660 */         return 17;
/*      */       case 52:
/*  662 */         return 45;
/*      */       case 53:
/*  664 */         return 21;
/*      */       case 54:
/*  666 */         return 44;
/*      */       case 57:
/*  668 */         return 56;
/*      */       case 58:
/*  670 */         return 184;
/*      */       case 73:
/*  672 */         return 43;
/*      */       case 55:
/*  674 */         return 51;
/*      */       case 112:
/*  676 */         return 211;
/*      */       case 21:
/*  678 */         return 203;
/*      */       case 22:
/*  680 */         return 205;
/*      */       case 19:
/*  682 */         return 200;
/*      */       case 20:
/*  684 */         return 208;
/*      */       case 66:
/*  686 */         return 28;
/*      */       case 3:
/*  688 */         return 199;
/*      */       case 132:
/*  690 */         return 207;
/*      */       case 93:
/*  692 */         return 209;
/*      */       case 92:
/*  694 */         return 201;
/*      */       case 133:
/*  696 */         return 210;
/*      */       case 69:
/*  698 */         return 12;
/*      */       case 56:
/*  700 */         return 52;
/*      */       case 81:
/*  702 */         return 78;
/*      */       case 74:
/*  704 */         return 39;
/*      */       case 59:
/*  706 */         return 42;
/*      */       case 60:
/*  708 */         return 54;
/*      */       case 76:
/*  710 */         return 53;
/*      */       case 62:
/*  712 */         return 57;
/*      */       case 61:
/*  714 */         return 15;
/*      */       case 67:
/*  716 */         return 14;
/*      */       case 129:
/*  718 */         return 29;
/*      */       case 130:
/*  720 */         return 157;
/*      */       case 131:
/*  722 */         return 1;
/*      */       case 244:
/*  724 */         return 59;
/*      */       case 245:
/*  726 */         return 60;
/*      */       case 246:
/*  728 */         return 61;
/*      */       case 247:
/*  730 */         return 62;
/*      */       case 248:
/*  732 */         return 63;
/*      */       case 249:
/*  734 */         return 64;
/*      */       case 250:
/*  736 */         return 65;
/*      */       case 251:
/*  738 */         return 66;
/*      */       case 252:
/*  740 */         return 67;
/*      */       case 253:
/*  742 */         return 68;
/*      */       case 254:
/*  744 */         return 87;
/*      */       case 255:
/*  746 */         return 88;
/*      */       case 243:
/*  748 */         return 146;
/*      */       case 144:
/*  750 */         return 82;
/*      */       case 145:
/*  752 */         return 79;
/*      */       case 146:
/*  754 */         return 80;
/*      */       case 147:
/*  756 */         return 81;
/*      */       case 148:
/*  758 */         return 75;
/*      */       case 149:
/*  760 */         return 76;
/*      */       case 150:
/*  762 */         return 77;
/*      */       case 151:
/*  764 */         return 71;
/*      */       case 152:
/*  766 */         return 72;
/*      */       case 153:
/*  768 */         return 73;
/*      */     } 
/*  770 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void update() {
/*  775 */     updateTime();
/*  776 */     updateMouse();
/*  777 */     updateKeyboard();
/*      */   }
/*      */   
/*      */   private int toGdxButton(int button) {
/*  781 */     if (button == 0) return 0; 
/*  782 */     if (button == 1) return 1; 
/*  783 */     if (button == 2) return 2; 
/*  784 */     if (button == 3) return 3; 
/*  785 */     if (button == 4) return 4; 
/*  786 */     return -1;
/*      */   }
/*      */   
/*      */   void updateTime() {
/*  790 */     long thisTime = System.nanoTime();
/*  791 */     this.deltaTime = (float)(thisTime - this.lastTime) / 1.0E9F;
/*  792 */     this.lastTime = thisTime;
/*      */   }
/*      */   
/*      */   void updateMouse() {
/*  796 */     this.justTouched = false;
/*  797 */     if (Mouse.isCreated()) {
/*  798 */       int events = 0;
/*  799 */       while (Mouse.next()) {
/*  800 */         events++;
/*  801 */         int x = (int)(Mouse.getEventX() * Display.getPixelScaleFactor());
/*  802 */         int y = Gdx.graphics.getHeight() - (int)(Mouse.getEventY() * Display.getPixelScaleFactor()) - 1;
/*  803 */         int button = Mouse.getEventButton();
/*  804 */         int gdxButton = toGdxButton(button);
/*  805 */         if (button != -1 && gdxButton == -1)
/*      */           continue; 
/*  807 */         TouchEvent event = (TouchEvent)this.usedTouchEvents.obtain();
/*  808 */         event.x = x;
/*  809 */         event.y = y;
/*  810 */         event.button = gdxButton;
/*  811 */         event.pointer = 0;
/*  812 */         event.timeStamp = Mouse.getEventNanoseconds();
/*      */ 
/*      */         
/*  815 */         if (button == -1) {
/*  816 */           if (Mouse.getEventDWheel() != 0) {
/*  817 */             event.type = 3;
/*  818 */             event.scrollAmount = (int)-Math.signum(Mouse.getEventDWheel());
/*  819 */           } else if (this.pressedButtons.size > 0) {
/*  820 */             event.type = 2;
/*      */           } else {
/*  822 */             event.type = 4;
/*      */           }
/*      */         
/*      */         }
/*  826 */         else if (Mouse.getEventButtonState()) {
/*  827 */           event.type = 0;
/*  828 */           this.pressedButtons.add(event.button);
/*  829 */           this.justTouched = true;
/*      */         } else {
/*  831 */           event.type = 1;
/*  832 */           this.pressedButtons.remove(event.button);
/*      */         } 
/*      */ 
/*      */         
/*  836 */         this.touchEvents.add(event);
/*  837 */         this.mouseX = event.x;
/*  838 */         this.mouseY = event.y;
/*  839 */         this.deltaX = (int)(Mouse.getEventDX() * Display.getPixelScaleFactor());
/*  840 */         this.deltaY = (int)(Mouse.getEventDY() * Display.getPixelScaleFactor());
/*      */       } 
/*      */       
/*  843 */       if (events == 0) {
/*  844 */         this.deltaX = 0;
/*  845 */         this.deltaY = 0;
/*      */       } else {
/*  847 */         Gdx.graphics.requestRendering();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   void updateKeyboard() {
/*  853 */     if (this.keyJustPressed) {
/*  854 */       this.keyJustPressed = false;
/*  855 */       for (int i = 0; i < this.justPressedKeys.length; i++) {
/*  856 */         this.justPressedKeys[i] = false;
/*      */       }
/*      */     } 
/*  859 */     if (this.lastKeyCharPressed != '\000') {
/*  860 */       this.keyRepeatTimer -= this.deltaTime;
/*  861 */       if (this.keyRepeatTimer < 0.0F) {
/*  862 */         this.keyRepeatTimer = keyRepeatTime;
/*      */         
/*  864 */         KeyEvent event = (KeyEvent)this.usedKeyEvents.obtain();
/*  865 */         event.keyCode = 0;
/*  866 */         event.keyChar = this.lastKeyCharPressed;
/*  867 */         event.type = 2;
/*  868 */         event.timeStamp = System.nanoTime();
/*  869 */         this.keyEvents.add(event);
/*  870 */         Gdx.graphics.requestRendering();
/*      */       } 
/*      */     } 
/*      */     
/*  874 */     if (Keyboard.isCreated()) {
/*  875 */       while (Keyboard.next()) {
/*  876 */         int keyCode = getGdxKeyCode(Keyboard.getEventKey());
/*  877 */         char keyChar = Keyboard.getEventCharacter();
/*  878 */         if (Keyboard.getEventKeyState() || (keyCode == 0 && keyChar != '\000' && Character.isDefined(keyChar))) {
/*  879 */           long timeStamp = Keyboard.getEventNanoseconds();
/*      */           
/*  881 */           switch (keyCode) {
/*      */             case 67:
/*  883 */               keyChar = '\b';
/*      */               break;
/*      */             case 112:
/*  886 */               keyChar = '';
/*      */               break;
/*      */           } 
/*      */           
/*  890 */           if (keyCode != 0) {
/*  891 */             KeyEvent keyEvent = (KeyEvent)this.usedKeyEvents.obtain();
/*  892 */             keyEvent.keyCode = keyCode;
/*  893 */             keyEvent.keyChar = Character.MIN_VALUE;
/*  894 */             keyEvent.type = 0;
/*  895 */             keyEvent.timeStamp = timeStamp;
/*  896 */             this.keyEvents.add(keyEvent);
/*      */             
/*  898 */             this.pressedKeys++;
/*  899 */             this.keyJustPressed = true;
/*  900 */             this.justPressedKeys[keyCode] = true;
/*  901 */             this.lastKeyCharPressed = keyChar;
/*  902 */             this.keyRepeatTimer = keyRepeatInitialTime;
/*      */           } 
/*      */           
/*  905 */           KeyEvent event = (KeyEvent)this.usedKeyEvents.obtain();
/*  906 */           event.keyCode = 0;
/*  907 */           event.keyChar = keyChar;
/*  908 */           event.type = 2;
/*  909 */           event.timeStamp = timeStamp;
/*  910 */           this.keyEvents.add(event);
/*      */         } else {
/*  912 */           KeyEvent event = (KeyEvent)this.usedKeyEvents.obtain();
/*  913 */           event.keyCode = keyCode;
/*  914 */           event.keyChar = Character.MIN_VALUE;
/*  915 */           event.type = 1;
/*  916 */           event.timeStamp = Keyboard.getEventNanoseconds();
/*  917 */           this.keyEvents.add(event);
/*      */           
/*  919 */           this.pressedKeys--;
/*  920 */           this.lastKeyCharPressed = Character.MIN_VALUE;
/*      */         } 
/*  922 */         Gdx.graphics.requestRendering();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInputProcessor(InputProcessor processor) {
/*  929 */     this.processor = processor;
/*      */   }
/*      */ 
/*      */   
/*      */   public InputProcessor getInputProcessor() {
/*  934 */     return this.processor;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void vibrate(int milliseconds) {}
/*      */ 
/*      */   
/*      */   public boolean justTouched() {
/*  943 */     return this.justTouched;
/*      */   }
/*      */   
/*      */   private int toLwjglButton(int button) {
/*  947 */     switch (button) {
/*      */       case 0:
/*  949 */         return 0;
/*      */       case 1:
/*  951 */         return 1;
/*      */       case 2:
/*  953 */         return 2;
/*      */       case 3:
/*  955 */         return 3;
/*      */       case 4:
/*  957 */         return 4;
/*      */     } 
/*  959 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isButtonPressed(int button) {
/*  964 */     return Mouse.isButtonDown(toLwjglButton(button));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void vibrate(long[] pattern, int repeat) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void cancelVibrate() {}
/*      */ 
/*      */   
/*      */   public float getAzimuth() {
/*  977 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPitch() {
/*  982 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRoll() {
/*  987 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPeripheralAvailable(Input.Peripheral peripheral) {
/*  992 */     if (peripheral == Input.Peripheral.HardwareKeyboard) return true; 
/*  993 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRotation() {
/*  998 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public Input.Orientation getNativeOrientation() {
/* 1003 */     return Input.Orientation.Landscape;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorCatched(boolean catched) {
/* 1008 */     Mouse.setGrabbed(catched);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCursorCatched() {
/* 1013 */     return Mouse.isGrabbed();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDeltaX() {
/* 1018 */     return this.deltaX;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDeltaX(int pointer) {
/* 1023 */     if (pointer == 0) {
/* 1024 */       return this.deltaX;
/*      */     }
/* 1026 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDeltaY() {
/* 1031 */     return -this.deltaY;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDeltaY(int pointer) {
/* 1036 */     if (pointer == 0) {
/* 1037 */       return -this.deltaY;
/*      */     }
/* 1039 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorPosition(int x, int y) {
/* 1044 */     Mouse.setCursorPosition(x, Gdx.graphics.getHeight() - 1 - y);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getCurrentEventTime() {
/* 1049 */     return this.currentEventTimeStamp;
/*      */   }
/*      */   
/*      */   public void getRotationMatrix(float[] matrix) {}
/*      */   
/*      */   class KeyEvent {
/*      */     static final int KEY_DOWN = 0;
/*      */     static final int KEY_UP = 1;
/*      */     static final int KEY_TYPED = 2;
/*      */     long timeStamp;
/*      */     int type;
/*      */     int keyCode;
/*      */     char keyChar;
/*      */   }
/*      */   
/*      */   class TouchEvent {
/*      */     static final int TOUCH_DOWN = 0;
/*      */     static final int TOUCH_UP = 1;
/*      */     static final int TOUCH_DRAGGED = 2;
/*      */     static final int TOUCH_SCROLLED = 3;
/*      */     static final int TOUCH_MOVED = 4;
/*      */     long timeStamp;
/*      */     int type;
/*      */     int x;
/*      */     int y;
/*      */     int scrollAmount;
/*      */     int button;
/*      */     int pointer;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */