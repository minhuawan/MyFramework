/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.utils.NumberUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Color
/*     */ {
/*  26 */   public static final Color CLEAR = new Color(0.0F, 0.0F, 0.0F, 0.0F);
/*  27 */   public static final Color BLACK = new Color(0.0F, 0.0F, 0.0F, 1.0F);
/*     */   
/*  29 */   public static final Color WHITE = new Color(-1);
/*  30 */   public static final Color LIGHT_GRAY = new Color(-1077952513);
/*  31 */   public static final Color GRAY = new Color(2139062271);
/*  32 */   public static final Color DARK_GRAY = new Color(1061109759);
/*     */   
/*  34 */   public static final Color BLUE = new Color(0.0F, 0.0F, 1.0F, 1.0F);
/*  35 */   public static final Color NAVY = new Color(0.0F, 0.0F, 0.5F, 1.0F);
/*  36 */   public static final Color ROYAL = new Color(1097458175);
/*  37 */   public static final Color SLATE = new Color(1887473919);
/*  38 */   public static final Color SKY = new Color(-2016482305);
/*  39 */   public static final Color CYAN = new Color(0.0F, 1.0F, 1.0F, 1.0F);
/*  40 */   public static final Color TEAL = new Color(0.0F, 0.5F, 0.5F, 1.0F);
/*     */   
/*  42 */   public static final Color GREEN = new Color(16711935);
/*  43 */   public static final Color CHARTREUSE = new Color(2147418367);
/*  44 */   public static final Color LIME = new Color(852308735);
/*  45 */   public static final Color FOREST = new Color(579543807);
/*  46 */   public static final Color OLIVE = new Color(1804477439);
/*     */   
/*  48 */   public static final Color YELLOW = new Color(-65281);
/*  49 */   public static final Color GOLD = new Color(-2686721);
/*  50 */   public static final Color GOLDENROD = new Color(-626712321);
/*  51 */   public static final Color ORANGE = new Color(-5963521);
/*     */   
/*  53 */   public static final Color BROWN = new Color(-1958407169);
/*  54 */   public static final Color TAN = new Color(-759919361);
/*  55 */   public static final Color FIREBRICK = new Color(-1306385665);
/*     */   
/*  57 */   public static final Color RED = new Color(-16776961);
/*  58 */   public static final Color SCARLET = new Color(-13361921);
/*  59 */   public static final Color CORAL = new Color(-8433409);
/*  60 */   public static final Color SALMON = new Color(-92245249);
/*  61 */   public static final Color PINK = new Color(-9849601);
/*  62 */   public static final Color MAGENTA = new Color(1.0F, 0.0F, 1.0F, 1.0F);
/*     */   
/*  64 */   public static final Color PURPLE = new Color(-1608453889);
/*  65 */   public static final Color VIOLET = new Color(-293409025);
/*  66 */   public static final Color MAROON = new Color(-1339006721);
/*     */   
/*     */   public float r;
/*     */   
/*     */   public float g;
/*     */   public float b;
/*     */   public float a;
/*     */   
/*     */   public Color() {}
/*     */   
/*     */   public Color(int rgba8888) {
/*  77 */     rgba8888ToColor(this, rgba8888);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color(float r, float g, float b, float a) {
/*  87 */     this.r = r;
/*  88 */     this.g = g;
/*  89 */     this.b = b;
/*  90 */     this.a = a;
/*  91 */     clamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color(Color color) {
/*  98 */     set(color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color set(Color color) {
/* 105 */     this.r = color.r;
/* 106 */     this.g = color.g;
/* 107 */     this.b = color.b;
/* 108 */     this.a = color.a;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color mul(Color color) {
/* 117 */     this.r *= color.r;
/* 118 */     this.g *= color.g;
/* 119 */     this.b *= color.b;
/* 120 */     this.a *= color.a;
/* 121 */     return clamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color mul(float value) {
/* 129 */     this.r *= value;
/* 130 */     this.g *= value;
/* 131 */     this.b *= value;
/* 132 */     this.a *= value;
/* 133 */     return clamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color add(Color color) {
/* 141 */     this.r += color.r;
/* 142 */     this.g += color.g;
/* 143 */     this.b += color.b;
/* 144 */     this.a += color.a;
/* 145 */     return clamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color sub(Color color) {
/* 153 */     this.r -= color.r;
/* 154 */     this.g -= color.g;
/* 155 */     this.b -= color.b;
/* 156 */     this.a -= color.a;
/* 157 */     return clamp();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Color clamp() {
/* 163 */     if (this.r < 0.0F)
/* 164 */     { this.r = 0.0F; }
/* 165 */     else if (this.r > 1.0F) { this.r = 1.0F; }
/*     */     
/* 167 */     if (this.g < 0.0F)
/* 168 */     { this.g = 0.0F; }
/* 169 */     else if (this.g > 1.0F) { this.g = 1.0F; }
/*     */     
/* 171 */     if (this.b < 0.0F)
/* 172 */     { this.b = 0.0F; }
/* 173 */     else if (this.b > 1.0F) { this.b = 1.0F; }
/*     */     
/* 175 */     if (this.a < 0.0F)
/* 176 */     { this.a = 0.0F; }
/* 177 */     else if (this.a > 1.0F) { this.a = 1.0F; }
/* 178 */      return this;
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
/*     */   public Color set(float r, float g, float b, float a) {
/* 190 */     this.r = r;
/* 191 */     this.g = g;
/* 192 */     this.b = b;
/* 193 */     this.a = a;
/* 194 */     return clamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color set(int rgba) {
/* 202 */     rgba8888ToColor(this, rgba);
/* 203 */     return this;
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
/*     */   public Color add(float r, float g, float b, float a) {
/* 215 */     this.r += r;
/* 216 */     this.g += g;
/* 217 */     this.b += b;
/* 218 */     this.a += a;
/* 219 */     return clamp();
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
/*     */   public Color sub(float r, float g, float b, float a) {
/* 231 */     this.r -= r;
/* 232 */     this.g -= g;
/* 233 */     this.b -= b;
/* 234 */     this.a -= a;
/* 235 */     return clamp();
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
/*     */   public Color mul(float r, float g, float b, float a) {
/* 247 */     this.r *= r;
/* 248 */     this.g *= g;
/* 249 */     this.b *= b;
/* 250 */     this.a *= a;
/* 251 */     return clamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color lerp(Color target, float t) {
/* 260 */     this.r += t * (target.r - this.r);
/* 261 */     this.g += t * (target.g - this.g);
/* 262 */     this.b += t * (target.b - this.b);
/* 263 */     this.a += t * (target.a - this.a);
/* 264 */     return clamp();
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
/*     */   public Color lerp(float r, float g, float b, float a, float t) {
/* 276 */     this.r += t * (r - this.r);
/* 277 */     this.g += t * (g - this.g);
/* 278 */     this.b += t * (b - this.b);
/* 279 */     this.a += t * (a - this.a);
/* 280 */     return clamp();
/*     */   }
/*     */ 
/*     */   
/*     */   public Color premultiplyAlpha() {
/* 285 */     this.r *= this.a;
/* 286 */     this.g *= this.a;
/* 287 */     this.b *= this.a;
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 293 */     if (this == o) return true; 
/* 294 */     if (o == null || getClass() != o.getClass()) return false; 
/* 295 */     Color color = (Color)o;
/* 296 */     return (toIntBits() == color.toIntBits());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 301 */     int result = (this.r != 0.0F) ? NumberUtils.floatToIntBits(this.r) : 0;
/* 302 */     result = 31 * result + ((this.g != 0.0F) ? NumberUtils.floatToIntBits(this.g) : 0);
/* 303 */     result = 31 * result + ((this.b != 0.0F) ? NumberUtils.floatToIntBits(this.b) : 0);
/* 304 */     result = 31 * result + ((this.a != 0.0F) ? NumberUtils.floatToIntBits(this.a) : 0);
/* 305 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float toFloatBits() {
/* 312 */     int color = (int)(255.0F * this.a) << 24 | (int)(255.0F * this.b) << 16 | (int)(255.0F * this.g) << 8 | (int)(255.0F * this.r);
/* 313 */     return NumberUtils.intToFloatColor(color);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int toIntBits() {
/* 319 */     int color = (int)(255.0F * this.a) << 24 | (int)(255.0F * this.b) << 16 | (int)(255.0F * this.g) << 8 | (int)(255.0F * this.r);
/* 320 */     return color;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     String value = Integer.toHexString((int)(255.0F * this.r) << 24 | (int)(255.0F * this.g) << 16 | (int)(255.0F * this.b) << 8 | (int)(255.0F * this.a));
/* 327 */     while (value.length() < 8)
/* 328 */       value = "0" + value; 
/* 329 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Color valueOf(String hex) {
/* 335 */     hex = (hex.charAt(0) == '#') ? hex.substring(1) : hex;
/* 336 */     int r = Integer.valueOf(hex.substring(0, 2), 16).intValue();
/* 337 */     int g = Integer.valueOf(hex.substring(2, 4), 16).intValue();
/* 338 */     int b = Integer.valueOf(hex.substring(4, 6), 16).intValue();
/* 339 */     int a = (hex.length() != 8) ? 255 : Integer.valueOf(hex.substring(6, 8), 16).intValue();
/* 340 */     return new Color(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
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
/*     */   public static float toFloatBits(int r, int g, int b, int a) {
/* 352 */     int color = a << 24 | b << 16 | g << 8 | r;
/* 353 */     float floatColor = NumberUtils.intToFloatColor(color);
/* 354 */     return floatColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float toFloatBits(float r, float g, float b, float a) {
/* 361 */     int color = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/* 362 */     return NumberUtils.intToFloatColor(color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toIntBits(int r, int g, int b, int a) {
/* 373 */     return a << 24 | b << 16 | g << 8 | r;
/*     */   }
/*     */   
/*     */   public static int alpha(float alpha) {
/* 377 */     return (int)(alpha * 255.0F);
/*     */   }
/*     */   
/*     */   public static int luminanceAlpha(float luminance, float alpha) {
/* 381 */     return (int)(luminance * 255.0F) << 8 | (int)(alpha * 255.0F);
/*     */   }
/*     */   
/*     */   public static int rgb565(float r, float g, float b) {
/* 385 */     return (int)(r * 31.0F) << 11 | (int)(g * 63.0F) << 5 | (int)(b * 31.0F);
/*     */   }
/*     */   
/*     */   public static int rgba4444(float r, float g, float b, float a) {
/* 389 */     return (int)(r * 15.0F) << 12 | (int)(g * 15.0F) << 8 | (int)(b * 15.0F) << 4 | (int)(a * 15.0F);
/*     */   }
/*     */   
/*     */   public static int rgb888(float r, float g, float b) {
/* 393 */     return (int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F);
/*     */   }
/*     */   
/*     */   public static int rgba8888(float r, float g, float b, float a) {
/* 397 */     return (int)(r * 255.0F) << 24 | (int)(g * 255.0F) << 16 | (int)(b * 255.0F) << 8 | (int)(a * 255.0F);
/*     */   }
/*     */   
/*     */   public static int argb8888(float a, float r, float g, float b) {
/* 401 */     return (int)(a * 255.0F) << 24 | (int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F);
/*     */   }
/*     */   
/*     */   public static int rgb565(Color color) {
/* 405 */     return (int)(color.r * 31.0F) << 11 | (int)(color.g * 63.0F) << 5 | (int)(color.b * 31.0F);
/*     */   }
/*     */   
/*     */   public static int rgba4444(Color color) {
/* 409 */     return (int)(color.r * 15.0F) << 12 | (int)(color.g * 15.0F) << 8 | (int)(color.b * 15.0F) << 4 | (int)(color.a * 15.0F);
/*     */   }
/*     */   
/*     */   public static int rgb888(Color color) {
/* 413 */     return (int)(color.r * 255.0F) << 16 | (int)(color.g * 255.0F) << 8 | (int)(color.b * 255.0F);
/*     */   }
/*     */   
/*     */   public static int rgba8888(Color color) {
/* 417 */     return (int)(color.r * 255.0F) << 24 | (int)(color.g * 255.0F) << 16 | (int)(color.b * 255.0F) << 8 | (int)(color.a * 255.0F);
/*     */   }
/*     */   
/*     */   public static int argb8888(Color color) {
/* 421 */     return (int)(color.a * 255.0F) << 24 | (int)(color.r * 255.0F) << 16 | (int)(color.g * 255.0F) << 8 | (int)(color.b * 255.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rgb565ToColor(Color color, int value) {
/* 430 */     color.r = ((value & 0xF800) >>> 11) / 31.0F;
/* 431 */     color.g = ((value & 0x7E0) >>> 5) / 63.0F;
/* 432 */     color.b = ((value & 0x1F) >>> 0) / 31.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rgba4444ToColor(Color color, int value) {
/* 441 */     color.r = ((value & 0xF000) >>> 12) / 15.0F;
/* 442 */     color.g = ((value & 0xF00) >>> 8) / 15.0F;
/* 443 */     color.b = ((value & 0xF0) >>> 4) / 15.0F;
/* 444 */     color.a = (value & 0xF) / 15.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rgb888ToColor(Color color, int value) {
/* 453 */     color.r = ((value & 0xFF0000) >>> 16) / 255.0F;
/* 454 */     color.g = ((value & 0xFF00) >>> 8) / 255.0F;
/* 455 */     color.b = (value & 0xFF) / 255.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rgba8888ToColor(Color color, int value) {
/* 464 */     color.r = ((value & 0xFF000000) >>> 24) / 255.0F;
/* 465 */     color.g = ((value & 0xFF0000) >>> 16) / 255.0F;
/* 466 */     color.b = ((value & 0xFF00) >>> 8) / 255.0F;
/* 467 */     color.a = (value & 0xFF) / 255.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void argb8888ToColor(Color color, int value) {
/* 476 */     color.a = ((value & 0xFF000000) >>> 24) / 255.0F;
/* 477 */     color.r = ((value & 0xFF0000) >>> 16) / 255.0F;
/* 478 */     color.g = ((value & 0xFF00) >>> 8) / 255.0F;
/* 479 */     color.b = (value & 0xFF) / 255.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void abgr8888ToColor(Color color, float value) {
/* 485 */     int c = NumberUtils.floatToIntColor(value);
/* 486 */     color.a = ((c & 0xFF000000) >>> 24) / 255.0F;
/* 487 */     color.b = ((c & 0xFF0000) >>> 16) / 255.0F;
/* 488 */     color.g = ((c & 0xFF00) >>> 8) / 255.0F;
/* 489 */     color.r = (c & 0xFF) / 255.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Color cpy() {
/* 494 */     return new Color(this);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\Color.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */