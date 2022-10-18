/*     */ package com.badlogic.gdx.controllers.mappings;
/*     */ 
/*     */ import com.badlogic.gdx.controllers.Controller;
/*     */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Xbox
/*     */ {
/*     */   public static final int A;
/*     */   public static final int B;
/*     */   public static final int X;
/*     */   public static final int Y;
/*     */   public static final int GUIDE;
/*     */   public static final int L_BUMPER;
/*     */   public static final int R_BUMPER;
/*     */   public static final int BACK;
/*     */   public static final int START;
/*     */   public static final int DPAD_UP;
/*     */   public static final int DPAD_DOWN;
/*     */   public static final int DPAD_LEFT;
/*     */   public static final int DPAD_RIGHT;
/*     */   public static final int L_TRIGGER;
/*     */   public static final int R_TRIGGER;
/*     */   public static final int L_STICK_VERTICAL_AXIS;
/*     */   public static final int L_STICK_HORIZONTAL_AXIS;
/*     */   public static final int R_STICK_VERTICAL_AXIS;
/*     */   public static final int R_STICK_HORIZONTAL_AXIS;
/*     */   
/*     */   static {
/*  61 */     if (SharedLibraryLoader.isWindows) {
/*  62 */       A = -1;
/*  63 */       B = -1;
/*  64 */       X = -1;
/*  65 */       Y = -1;
/*  66 */       GUIDE = -1;
/*  67 */       L_BUMPER = -1;
/*  68 */       R_BUMPER = -1;
/*  69 */       BACK = -1;
/*  70 */       START = -1;
/*  71 */       DPAD_UP = -1;
/*  72 */       DPAD_DOWN = -1;
/*  73 */       DPAD_LEFT = -1;
/*  74 */       DPAD_RIGHT = -1;
/*  75 */       L_TRIGGER = -1;
/*  76 */       R_TRIGGER = -1;
/*  77 */       L_STICK_VERTICAL_AXIS = -1;
/*  78 */       L_STICK_HORIZONTAL_AXIS = -1;
/*  79 */       R_STICK_VERTICAL_AXIS = -1;
/*  80 */       R_STICK_HORIZONTAL_AXIS = -1;
/*  81 */     } else if (SharedLibraryLoader.isLinux) {
/*  82 */       A = -1;
/*  83 */       B = -1;
/*  84 */       X = -1;
/*  85 */       Y = -1;
/*  86 */       GUIDE = -1;
/*  87 */       L_BUMPER = -1;
/*  88 */       R_BUMPER = -1;
/*  89 */       BACK = -1;
/*  90 */       START = -1;
/*  91 */       DPAD_UP = -1;
/*  92 */       DPAD_DOWN = -1;
/*  93 */       DPAD_LEFT = -1;
/*  94 */       DPAD_RIGHT = -1;
/*  95 */       L_TRIGGER = -1;
/*  96 */       R_TRIGGER = -1;
/*  97 */       L_STICK_VERTICAL_AXIS = -1;
/*  98 */       L_STICK_HORIZONTAL_AXIS = -1;
/*  99 */       R_STICK_VERTICAL_AXIS = -1;
/* 100 */       R_STICK_HORIZONTAL_AXIS = -1;
/* 101 */     } else if (SharedLibraryLoader.isMac) {
/* 102 */       A = 11;
/* 103 */       B = 12;
/* 104 */       X = 13;
/* 105 */       Y = 14;
/* 106 */       GUIDE = 10;
/* 107 */       L_BUMPER = 8;
/* 108 */       R_BUMPER = 9;
/* 109 */       BACK = 5;
/* 110 */       START = 4;
/* 111 */       DPAD_UP = 0;
/* 112 */       DPAD_DOWN = 1;
/* 113 */       DPAD_LEFT = 2;
/* 114 */       DPAD_RIGHT = 3;
/* 115 */       L_TRIGGER = 0;
/* 116 */       R_TRIGGER = 1;
/* 117 */       L_STICK_VERTICAL_AXIS = 3;
/* 118 */       L_STICK_HORIZONTAL_AXIS = 2;
/* 119 */       R_STICK_VERTICAL_AXIS = 5;
/* 120 */       R_STICK_HORIZONTAL_AXIS = 4;
/*     */     } else {
/* 122 */       A = -1;
/* 123 */       B = -1;
/* 124 */       X = -1;
/* 125 */       Y = -1;
/* 126 */       GUIDE = -1;
/* 127 */       L_BUMPER = -1;
/* 128 */       R_BUMPER = -1;
/* 129 */       L_TRIGGER = -1;
/* 130 */       R_TRIGGER = -1;
/* 131 */       BACK = -1;
/* 132 */       START = -1;
/* 133 */       DPAD_UP = -1;
/* 134 */       DPAD_DOWN = -1;
/* 135 */       DPAD_LEFT = -1;
/* 136 */       DPAD_RIGHT = -1;
/* 137 */       L_STICK_VERTICAL_AXIS = -1;
/* 138 */       L_STICK_HORIZONTAL_AXIS = -1;
/* 139 */       R_STICK_VERTICAL_AXIS = -1;
/* 140 */       R_STICK_HORIZONTAL_AXIS = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isXboxController(Controller controller) {
/* 147 */     return controller.getName().contains("Xbox");
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\mappings\Xbox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */