/*     */ package com.badlogic.gdx;
/*     */ 
/*     */ import com.badlogic.gdx.utils.ObjectIntMap;
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
/*     */ public interface Input
/*     */ {
/*     */   float getAccelerometerX();
/*     */   
/*     */   float getAccelerometerY();
/*     */   
/*     */   float getAccelerometerZ();
/*     */   
/*     */   float getGyroscopeX();
/*     */   
/*     */   float getGyroscopeY();
/*     */   
/*     */   float getGyroscopeZ();
/*     */   
/*     */   int getX();
/*     */   
/*     */   int getX(int paramInt);
/*     */   
/*     */   int getDeltaX();
/*     */   
/*     */   int getDeltaX(int paramInt);
/*     */   
/*     */   int getY();
/*     */   
/*     */   int getY(int paramInt);
/*     */   
/*     */   int getDeltaY();
/*     */   
/*     */   int getDeltaY(int paramInt);
/*     */   
/*     */   boolean isTouched();
/*     */   
/*     */   boolean justTouched();
/*     */   
/*     */   boolean isTouched(int paramInt);
/*     */   
/*     */   boolean isButtonPressed(int paramInt);
/*     */   
/*     */   boolean isKeyPressed(int paramInt);
/*     */   
/*     */   boolean isKeyJustPressed(int paramInt);
/*     */   
/*     */   void getTextInput(TextInputListener paramTextInputListener, String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   void setOnscreenKeyboardVisible(boolean paramBoolean);
/*     */   
/*     */   void vibrate(int paramInt);
/*     */   
/*     */   void vibrate(long[] paramArrayOflong, int paramInt);
/*     */   
/*     */   void cancelVibrate();
/*     */   
/*     */   float getAzimuth();
/*     */   
/*     */   float getPitch();
/*     */   
/*     */   float getRoll();
/*     */   
/*     */   void getRotationMatrix(float[] paramArrayOffloat);
/*     */   
/*     */   long getCurrentEventTime();
/*     */   
/*     */   void setCatchBackKey(boolean paramBoolean);
/*     */   
/*     */   boolean isCatchBackKey();
/*     */   
/*     */   void setCatchMenuKey(boolean paramBoolean);
/*     */   
/*     */   boolean isCatchMenuKey();
/*     */   
/*     */   void setInputProcessor(InputProcessor paramInputProcessor);
/*     */   
/*     */   InputProcessor getInputProcessor();
/*     */   
/*     */   boolean isPeripheralAvailable(Peripheral paramPeripheral);
/*     */   
/*     */   int getRotation();
/*     */   
/*     */   Orientation getNativeOrientation();
/*     */   
/*     */   void setCursorCatched(boolean paramBoolean);
/*     */   
/*     */   boolean isCursorCatched();
/*     */   
/*     */   void setCursorPosition(int paramInt1, int paramInt2);
/*     */   
/*     */   public static interface TextInputListener
/*     */   {
/*     */     void input(String param1String);
/*     */     
/*     */     void canceled();
/*     */   }
/*     */   
/*     */   public static class Buttons
/*     */   {
/*     */     public static final int LEFT = 0;
/*     */     public static final int RIGHT = 1;
/*     */     public static final int MIDDLE = 2;
/*     */     public static final int BACK = 3;
/*     */     public static final int FORWARD = 4;
/*     */   }
/*     */   
/*     */   public static class Keys
/*     */   {
/*     */     public static final int ANY_KEY = -1;
/*     */     public static final int NUM_0 = 7;
/*     */     public static final int NUM_1 = 8;
/*     */     public static final int NUM_2 = 9;
/*     */     public static final int NUM_3 = 10;
/*     */     public static final int NUM_4 = 11;
/*     */     public static final int NUM_5 = 12;
/*     */     public static final int NUM_6 = 13;
/*     */     public static final int NUM_7 = 14;
/*     */     public static final int NUM_8 = 15;
/*     */     public static final int NUM_9 = 16;
/*     */     public static final int A = 29;
/*     */     public static final int ALT_LEFT = 57;
/*     */     public static final int ALT_RIGHT = 58;
/*     */     public static final int APOSTROPHE = 75;
/*     */     public static final int AT = 77;
/*     */     public static final int B = 30;
/*     */     public static final int BACK = 4;
/*     */     public static final int BACKSLASH = 73;
/*     */     public static final int C = 31;
/*     */     public static final int CALL = 5;
/*     */     public static final int CAMERA = 27;
/*     */     public static final int CLEAR = 28;
/*     */     public static final int COMMA = 55;
/*     */     public static final int D = 32;
/*     */     public static final int DEL = 67;
/*     */     public static final int BACKSPACE = 67;
/*     */     public static final int FORWARD_DEL = 112;
/*     */     public static final int DPAD_CENTER = 23;
/*     */     public static final int DPAD_DOWN = 20;
/*     */     public static final int DPAD_LEFT = 21;
/*     */     public static final int DPAD_RIGHT = 22;
/*     */     public static final int DPAD_UP = 19;
/*     */     public static final int CENTER = 23;
/*     */     public static final int DOWN = 20;
/*     */     public static final int LEFT = 21;
/*     */     public static final int RIGHT = 22;
/*     */     public static final int UP = 19;
/*     */     public static final int E = 33;
/*     */     public static final int ENDCALL = 6;
/*     */     public static final int ENTER = 66;
/*     */     public static final int ENVELOPE = 65;
/*     */     public static final int EQUALS = 70;
/*     */     public static final int EXPLORER = 64;
/*     */     public static final int F = 34;
/*     */     public static final int FOCUS = 80;
/*     */     public static final int G = 35;
/*     */     public static final int GRAVE = 68;
/*     */     public static final int H = 36;
/*     */     public static final int HEADSETHOOK = 79;
/*     */     public static final int HOME = 3;
/*     */     public static final int I = 37;
/*     */     public static final int J = 38;
/*     */     public static final int K = 39;
/*     */     public static final int L = 40;
/*     */     public static final int LEFT_BRACKET = 71;
/*     */     public static final int M = 41;
/*     */     public static final int MEDIA_FAST_FORWARD = 90;
/*     */     public static final int MEDIA_NEXT = 87;
/*     */     public static final int MEDIA_PLAY_PAUSE = 85;
/*     */     public static final int MEDIA_PREVIOUS = 88;
/*     */     public static final int MEDIA_REWIND = 89;
/*     */     public static final int MEDIA_STOP = 86;
/*     */     public static final int MENU = 82;
/*     */     public static final int MINUS = 69;
/*     */     public static final int MUTE = 91;
/*     */     public static final int N = 42;
/*     */     public static final int NOTIFICATION = 83;
/*     */     public static final int NUM = 78;
/*     */     public static final int O = 43;
/*     */     public static final int P = 44;
/*     */     public static final int PERIOD = 56;
/*     */     public static final int PLUS = 81;
/*     */     public static final int POUND = 18;
/*     */     public static final int POWER = 26;
/*     */     public static final int Q = 45;
/*     */     public static final int R = 46;
/*     */     public static final int RIGHT_BRACKET = 72;
/*     */     
/*     */     public static String toString(int keycode) {
/* 248 */       if (keycode < 0) throw new IllegalArgumentException("keycode cannot be negative, keycode: " + keycode); 
/* 249 */       if (keycode > 255) throw new IllegalArgumentException("keycode cannot be greater than 255, keycode: " + keycode); 
/* 250 */       switch (keycode) {
/*     */         
/*     */         case 0:
/* 253 */           return "Unknown";
/*     */         case 1:
/* 255 */           return "Soft Left";
/*     */         case 2:
/* 257 */           return "Soft Right";
/*     */         case 3:
/* 259 */           return "Home";
/*     */         case 4:
/* 261 */           return "Back";
/*     */         case 5:
/* 263 */           return "Call";
/*     */         case 6:
/* 265 */           return "End Call";
/*     */         case 7:
/* 267 */           return "0";
/*     */         case 8:
/* 269 */           return "1";
/*     */         case 9:
/* 271 */           return "2";
/*     */         case 10:
/* 273 */           return "3";
/*     */         case 11:
/* 275 */           return "4";
/*     */         case 12:
/* 277 */           return "5";
/*     */         case 13:
/* 279 */           return "6";
/*     */         case 14:
/* 281 */           return "7";
/*     */         case 15:
/* 283 */           return "8";
/*     */         case 16:
/* 285 */           return "9";
/*     */         case 17:
/* 287 */           return "*";
/*     */         case 18:
/* 289 */           return "#";
/*     */         case 19:
/* 291 */           return "Up";
/*     */         case 20:
/* 293 */           return "Down";
/*     */         case 21:
/* 295 */           return "Left";
/*     */         case 22:
/* 297 */           return "Right";
/*     */         case 23:
/* 299 */           return "Center";
/*     */         case 24:
/* 301 */           return "Volume Up";
/*     */         case 25:
/* 303 */           return "Volume Down";
/*     */         case 26:
/* 305 */           return "Power";
/*     */         case 27:
/* 307 */           return "Camera";
/*     */         case 28:
/* 309 */           return "Clear";
/*     */         case 29:
/* 311 */           return "A";
/*     */         case 30:
/* 313 */           return "B";
/*     */         case 31:
/* 315 */           return "C";
/*     */         case 32:
/* 317 */           return "D";
/*     */         case 33:
/* 319 */           return "E";
/*     */         case 34:
/* 321 */           return "F";
/*     */         case 35:
/* 323 */           return "G";
/*     */         case 36:
/* 325 */           return "H";
/*     */         case 37:
/* 327 */           return "I";
/*     */         case 38:
/* 329 */           return "J";
/*     */         case 39:
/* 331 */           return "K";
/*     */         case 40:
/* 333 */           return "L";
/*     */         case 41:
/* 335 */           return "M";
/*     */         case 42:
/* 337 */           return "N";
/*     */         case 43:
/* 339 */           return "O";
/*     */         case 44:
/* 341 */           return "P";
/*     */         case 45:
/* 343 */           return "Q";
/*     */         case 46:
/* 345 */           return "R";
/*     */         case 47:
/* 347 */           return "S";
/*     */         case 48:
/* 349 */           return "T";
/*     */         case 49:
/* 351 */           return "U";
/*     */         case 50:
/* 353 */           return "V";
/*     */         case 51:
/* 355 */           return "W";
/*     */         case 52:
/* 357 */           return "X";
/*     */         case 53:
/* 359 */           return "Y";
/*     */         case 54:
/* 361 */           return "Z";
/*     */         case 55:
/* 363 */           return ",";
/*     */         case 56:
/* 365 */           return ".";
/*     */         case 57:
/* 367 */           return "L-Alt";
/*     */         case 58:
/* 369 */           return "R-Alt";
/*     */         case 59:
/* 371 */           return "L-Shift";
/*     */         case 60:
/* 373 */           return "R-Shift";
/*     */         case 61:
/* 375 */           return "Tab";
/*     */         case 62:
/* 377 */           return "Space";
/*     */         case 63:
/* 379 */           return "SYM";
/*     */         case 64:
/* 381 */           return "Explorer";
/*     */         case 65:
/* 383 */           return "Envelope";
/*     */         case 66:
/* 385 */           return "Enter";
/*     */         case 67:
/* 387 */           return "Delete";
/*     */         case 68:
/* 389 */           return "`";
/*     */         case 69:
/* 391 */           return "-";
/*     */         case 70:
/* 393 */           return "=";
/*     */         case 71:
/* 395 */           return "[";
/*     */         case 72:
/* 397 */           return "]";
/*     */         case 73:
/* 399 */           return "\\";
/*     */         case 74:
/* 401 */           return ";";
/*     */         case 75:
/* 403 */           return "'";
/*     */         case 76:
/* 405 */           return "/";
/*     */         case 77:
/* 407 */           return "@";
/*     */         case 78:
/* 409 */           return "Num";
/*     */         case 79:
/* 411 */           return "Headset Hook";
/*     */         case 80:
/* 413 */           return "Focus";
/*     */         case 81:
/* 415 */           return "Plus";
/*     */         case 82:
/* 417 */           return "Menu";
/*     */         case 83:
/* 419 */           return "Notification";
/*     */         case 84:
/* 421 */           return "Search";
/*     */         case 85:
/* 423 */           return "Play/Pause";
/*     */         case 86:
/* 425 */           return "Stop Media";
/*     */         case 87:
/* 427 */           return "Next Media";
/*     */         case 88:
/* 429 */           return "Prev Media";
/*     */         case 89:
/* 431 */           return "Rewind";
/*     */         case 90:
/* 433 */           return "Fast Forward";
/*     */         case 91:
/* 435 */           return "Mute";
/*     */         case 92:
/* 437 */           return "Page Up";
/*     */         case 93:
/* 439 */           return "Page Down";
/*     */         case 94:
/* 441 */           return "PICTSYMBOLS";
/*     */         case 95:
/* 443 */           return "SWITCH_CHARSET";
/*     */         case 96:
/* 445 */           return "A Button";
/*     */         case 97:
/* 447 */           return "B Button";
/*     */         case 98:
/* 449 */           return "C Button";
/*     */         case 99:
/* 451 */           return "X Button";
/*     */         case 100:
/* 453 */           return "Y Button";
/*     */         case 101:
/* 455 */           return "Z Button";
/*     */         case 102:
/* 457 */           return "L1 Button";
/*     */         case 103:
/* 459 */           return "R1 Button";
/*     */         case 104:
/* 461 */           return "L2 Button";
/*     */         case 105:
/* 463 */           return "R2 Button";
/*     */         case 106:
/* 465 */           return "Left Thumb";
/*     */         case 107:
/* 467 */           return "Right Thumb";
/*     */         case 108:
/* 469 */           return "Start";
/*     */         case 109:
/* 471 */           return "Select";
/*     */         case 110:
/* 473 */           return "Button Mode";
/*     */         case 112:
/* 475 */           return "Forward Delete";
/*     */         case 129:
/* 477 */           return "L-Ctrl";
/*     */         case 130:
/* 479 */           return "R-Ctrl";
/*     */         case 131:
/* 481 */           return "Escape";
/*     */         case 132:
/* 483 */           return "End";
/*     */         case 133:
/* 485 */           return "Insert";
/*     */         case 144:
/* 487 */           return "Numpad 0";
/*     */         case 145:
/* 489 */           return "Numpad 1";
/*     */         case 146:
/* 491 */           return "Numpad 2";
/*     */         case 147:
/* 493 */           return "Numpad 3";
/*     */         case 148:
/* 495 */           return "Numpad 4";
/*     */         case 149:
/* 497 */           return "Numpad 5";
/*     */         case 150:
/* 499 */           return "Numpad 6";
/*     */         case 151:
/* 501 */           return "Numpad 7";
/*     */         case 152:
/* 503 */           return "Numpad 8";
/*     */         case 153:
/* 505 */           return "Numpad 9";
/*     */         case 243:
/* 507 */           return ":";
/*     */         case 244:
/* 509 */           return "F1";
/*     */         case 245:
/* 511 */           return "F2";
/*     */         case 246:
/* 513 */           return "F3";
/*     */         case 247:
/* 515 */           return "F4";
/*     */         case 248:
/* 517 */           return "F5";
/*     */         case 249:
/* 519 */           return "F6";
/*     */         case 250:
/* 521 */           return "F7";
/*     */         case 251:
/* 523 */           return "F8";
/*     */         case 252:
/* 525 */           return "F9";
/*     */         case 253:
/* 527 */           return "F10";
/*     */         case 254:
/* 529 */           return "F11";
/*     */         case 255:
/* 531 */           return "F12";
/*     */       } 
/*     */ 
/*     */       
/* 535 */       return null;
/*     */     }
/*     */     public static final int S = 47; public static final int SEARCH = 84; public static final int SEMICOLON = 74; public static final int SHIFT_LEFT = 59; public static final int SHIFT_RIGHT = 60; public static final int SLASH = 76; public static final int SOFT_LEFT = 1; public static final int SOFT_RIGHT = 2; public static final int SPACE = 62; public static final int STAR = 17; public static final int SYM = 63; public static final int T = 48; public static final int TAB = 61; public static final int U = 49; public static final int UNKNOWN = 0; public static final int V = 50; public static final int VOLUME_DOWN = 25; public static final int VOLUME_UP = 24; public static final int W = 51; public static final int X = 52; public static final int Y = 53; public static final int Z = 54; public static final int META_ALT_LEFT_ON = 16; public static final int META_ALT_ON = 2; public static final int META_ALT_RIGHT_ON = 32; public static final int META_SHIFT_LEFT_ON = 64; public static final int META_SHIFT_ON = 1; public static final int META_SHIFT_RIGHT_ON = 128; public static final int META_SYM_ON = 4; public static final int CONTROL_LEFT = 129; public static final int CONTROL_RIGHT = 130; public static final int ESCAPE = 131; public static final int END = 132; public static final int INSERT = 133; public static final int PAGE_UP = 92; public static final int PAGE_DOWN = 93; public static final int PICTSYMBOLS = 94; public static final int SWITCH_CHARSET = 95; public static final int BUTTON_CIRCLE = 255; public static final int BUTTON_A = 96; public static final int BUTTON_B = 97; public static final int BUTTON_C = 98; public static final int BUTTON_X = 99; public static final int BUTTON_Y = 100; public static final int BUTTON_Z = 101; public static final int BUTTON_L1 = 102; public static final int BUTTON_R1 = 103; public static final int BUTTON_L2 = 104; public static final int BUTTON_R2 = 105; public static final int BUTTON_THUMBL = 106; public static final int BUTTON_THUMBR = 107; public static final int BUTTON_START = 108; public static final int BUTTON_SELECT = 109; public static final int BUTTON_MODE = 110; public static final int NUMPAD_0 = 144; public static final int NUMPAD_1 = 145; public static final int NUMPAD_2 = 146; public static final int NUMPAD_3 = 147; public static final int NUMPAD_4 = 148; public static final int NUMPAD_5 = 149; public static final int NUMPAD_6 = 150; public static final int NUMPAD_7 = 151; public static final int NUMPAD_8 = 152; public static final int NUMPAD_9 = 153; public static final int COLON = 243; public static final int F1 = 244; public static final int F2 = 245; public static final int F3 = 246; public static final int F4 = 247; public static final int F5 = 248; public static final int F6 = 249; public static final int F7 = 250; public static final int F8 = 251; public static final int F9 = 252;
/*     */     public static final int F10 = 253;
/*     */     public static final int F11 = 254;
/*     */     public static final int F12 = 255;
/*     */     private static ObjectIntMap<String> keyNames;
/*     */     
/*     */     public static int valueOf(String keyname) {
/* 544 */       if (keyNames == null) initializeKeyNames(); 
/* 545 */       return keyNames.get(keyname, -1);
/*     */     }
/*     */ 
/*     */     
/*     */     private static void initializeKeyNames() {
/* 550 */       keyNames = new ObjectIntMap();
/* 551 */       for (int i = 0; i < 256; i++) {
/* 552 */         String name = toString(i);
/* 553 */         if (name != null) keyNames.put(name, i);
/*     */       
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Peripheral
/*     */   {
/* 561 */     HardwareKeyboard, OnscreenKeyboard, MultitouchScreen, Accelerometer, Compass, Vibrator, Gyroscope;
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
/*     */   public enum Orientation
/*     */   {
/* 753 */     Landscape, Portrait;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\Input.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */