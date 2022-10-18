/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Colors
/*     */ {
/*  27 */   private static final ObjectMap<String, Color> map = new ObjectMap();
/*     */   
/*     */   static {
/*  30 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ObjectMap<String, Color> getColors() {
/*  35 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Color get(String name) {
/*  45 */     return (Color)map.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Color put(String name, Color color) {
/*  56 */     return (Color)map.put(name, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reset() {
/*  61 */     map.clear();
/*  62 */     map.put("CLEAR", Color.CLEAR);
/*  63 */     map.put("BLACK", Color.BLACK);
/*     */     
/*  65 */     map.put("WHITE", Color.WHITE);
/*  66 */     map.put("LIGHT_GRAY", Color.LIGHT_GRAY);
/*  67 */     map.put("GRAY", Color.GRAY);
/*  68 */     map.put("DARK_GRAY", Color.DARK_GRAY);
/*     */     
/*  70 */     map.put("BLUE", Color.BLUE);
/*  71 */     map.put("NAVY", Color.NAVY);
/*  72 */     map.put("ROYAL", Color.ROYAL);
/*  73 */     map.put("SLATE", Color.SLATE);
/*  74 */     map.put("SKY", Color.SKY);
/*  75 */     map.put("CYAN", Color.CYAN);
/*  76 */     map.put("TEAL", Color.TEAL);
/*     */     
/*  78 */     map.put("GREEN", Color.GREEN);
/*  79 */     map.put("CHARTREUSE", Color.CHARTREUSE);
/*  80 */     map.put("LIME", Color.LIME);
/*  81 */     map.put("FOREST", Color.FOREST);
/*  82 */     map.put("OLIVE", Color.OLIVE);
/*     */     
/*  84 */     map.put("YELLOW", Color.YELLOW);
/*  85 */     map.put("GOLD", Color.GOLD);
/*  86 */     map.put("GOLDENROD", Color.GOLDENROD);
/*  87 */     map.put("ORANGE", Color.ORANGE);
/*     */     
/*  89 */     map.put("BROWN", Color.BROWN);
/*  90 */     map.put("TAN", Color.TAN);
/*  91 */     map.put("FIREBRICK", Color.FIREBRICK);
/*     */     
/*  93 */     map.put("RED", Color.RED);
/*  94 */     map.put("SCARLET", Color.SCARLET);
/*  95 */     map.put("CORAL", Color.CORAL);
/*  96 */     map.put("SALMON", Color.SALMON);
/*  97 */     map.put("PINK", Color.PINK);
/*  98 */     map.put("MAGENTA", Color.MAGENTA);
/*     */     
/* 100 */     map.put("PURPLE", Color.PURPLE);
/* 101 */     map.put("VIOLET", Color.VIOLET);
/* 102 */     map.put("MAROON", Color.MAROON);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\Colors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */