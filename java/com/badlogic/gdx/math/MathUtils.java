/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MathUtils
/*     */ {
/*     */   public static final float nanoToSec = 1.0E-9F;
/*     */   public static final float FLOAT_ROUNDING_ERROR = 1.0E-6F;
/*     */   public static final float PI = 3.1415927F;
/*     */   public static final float PI2 = 6.2831855F;
/*     */   public static final float E = 2.7182817F;
/*     */   private static final int SIN_BITS = 14;
/*     */   private static final int SIN_MASK = 16383;
/*     */   private static final int SIN_COUNT = 16384;
/*     */   private static final float radFull = 6.2831855F;
/*     */   private static final float degFull = 360.0F;
/*     */   private static final float radToIndex = 2607.5945F;
/*     */   private static final float degToIndex = 45.511112F;
/*     */   public static final float radiansToDegrees = 57.295776F;
/*     */   public static final float radDeg = 57.295776F;
/*     */   public static final float degreesToRadians = 0.017453292F;
/*     */   public static final float degRad = 0.017453292F;
/*     */   
/*     */   private static class Sin
/*     */   {
/*  52 */     static final float[] table = new float[16384];
/*     */     static {
/*     */       int i;
/*  55 */       for (i = 0; i < 16384; i++)
/*  56 */         table[i] = (float)Math.sin(((i + 0.5F) / 16384.0F * 6.2831855F)); 
/*  57 */       for (i = 0; i < 360; i += 90) {
/*  58 */         table[(int)(i * 45.511112F) & 0x3FFF] = (float)Math.sin((i * 0.017453292F));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static float sin(float radians) {
/*  64 */     return Sin.table[(int)(radians * 2607.5945F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */   
/*     */   public static float cos(float radians) {
/*  69 */     return Sin.table[(int)((radians + 1.5707964F) * 2607.5945F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */   
/*     */   public static float sinDeg(float degrees) {
/*  74 */     return Sin.table[(int)(degrees * 45.511112F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */   
/*     */   public static float cosDeg(float degrees) {
/*  79 */     return Sin.table[(int)((degrees + 90.0F) * 45.511112F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float atan2(float y, float x) {
/*  87 */     if (x == 0.0F) {
/*  88 */       if (y > 0.0F) return 1.5707964F; 
/*  89 */       if (y == 0.0F) return 0.0F; 
/*  90 */       return -1.5707964F;
/*     */     } 
/*  92 */     float z = y / x;
/*  93 */     if (Math.abs(z) < 1.0F) {
/*  94 */       float f = z / (1.0F + 0.28F * z * z);
/*  95 */       if (x < 0.0F) return f + ((y < 0.0F) ? -3.1415927F : 3.1415927F); 
/*  96 */       return f;
/*     */     } 
/*  98 */     float atan = 1.5707964F - z / (z * z + 0.28F);
/*  99 */     return (y < 0.0F) ? (atan - 3.1415927F) : atan;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static Random random = new RandomXS128(); private static final int BIG_ENOUGH_INT = 16384;
/*     */   private static final double BIG_ENOUGH_FLOOR = 16384.0D;
/*     */   
/*     */   public static int random(int range) {
/* 108 */     return random.nextInt(range + 1);
/*     */   }
/*     */   private static final double CEIL = 0.9999999D; private static final double BIG_ENOUGH_CEIL = 16384.999999999996D; private static final double BIG_ENOUGH_ROUND = 16384.5D;
/*     */   
/*     */   public static int random(int start, int end) {
/* 113 */     return start + random.nextInt(end - start + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long random(long range) {
/* 118 */     return (long)(random.nextDouble() * range);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long random(long start, long end) {
/* 123 */     return start + (long)(random.nextDouble() * (end - start));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean randomBoolean() {
/* 128 */     return random.nextBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean randomBoolean(float chance) {
/* 133 */     return (random() < chance);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float random() {
/* 138 */     return random.nextFloat();
/*     */   }
/*     */ 
/*     */   
/*     */   public static float random(float range) {
/* 143 */     return random.nextFloat() * range;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float random(float start, float end) {
/* 148 */     return start + random.nextFloat() * (end - start);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int randomSign() {
/* 153 */     return 0x1 | random.nextInt() >> 31;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float randomTriangular() {
/* 161 */     return random.nextFloat() - random.nextFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float randomTriangular(float max) {
/* 170 */     return (random.nextFloat() - random.nextFloat()) * max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float randomTriangular(float min, float max) {
/* 180 */     return randomTriangular(min, max, (min + max) * 0.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float randomTriangular(float min, float max, float mode) {
/* 189 */     float u = random.nextFloat();
/* 190 */     float d = max - min;
/* 191 */     if (u <= (mode - min) / d) return min + (float)Math.sqrt((u * d * (mode - min))); 
/* 192 */     return max - (float)Math.sqrt(((1.0F - u) * d * (max - mode)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nextPowerOfTwo(int value) {
/* 199 */     if (value == 0) return 1; 
/* 200 */     value--;
/* 201 */     value |= value >> 1;
/* 202 */     value |= value >> 2;
/* 203 */     value |= value >> 4;
/* 204 */     value |= value >> 8;
/* 205 */     value |= value >> 16;
/* 206 */     return value + 1;
/*     */   }
/*     */   
/*     */   public static boolean isPowerOfTwo(int value) {
/* 210 */     return (value != 0 && (value & value - 1) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static short clamp(short value, short min, short max) {
/* 216 */     if (value < min) return min; 
/* 217 */     if (value > max) return max; 
/* 218 */     return value;
/*     */   }
/*     */   
/*     */   public static int clamp(int value, int min, int max) {
/* 222 */     if (value < min) return min; 
/* 223 */     if (value > max) return max; 
/* 224 */     return value;
/*     */   }
/*     */   
/*     */   public static long clamp(long value, long min, long max) {
/* 228 */     if (value < min) return min; 
/* 229 */     if (value > max) return max; 
/* 230 */     return value;
/*     */   }
/*     */   
/*     */   public static float clamp(float value, float min, float max) {
/* 234 */     if (value < min) return min; 
/* 235 */     if (value > max) return max; 
/* 236 */     return value;
/*     */   }
/*     */   
/*     */   public static double clamp(double value, double min, double max) {
/* 240 */     if (value < min) return min; 
/* 241 */     if (value > max) return max; 
/* 242 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float lerp(float fromValue, float toValue, float progress) {
/* 249 */     return fromValue + (toValue - fromValue) * progress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float lerpAngle(float fromRadians, float toRadians, float progress) {
/* 260 */     float delta = (toRadians - fromRadians + 6.2831855F + 3.1415927F) % 6.2831855F - 3.1415927F;
/* 261 */     return (fromRadians + delta * progress + 6.2831855F) % 6.2831855F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float lerpAngleDeg(float fromDegrees, float toDegrees, float progress) {
/* 272 */     float delta = (toDegrees - fromDegrees + 360.0F + 180.0F) % 360.0F - 180.0F;
/* 273 */     return (fromDegrees + delta * progress + 360.0F) % 360.0F;
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
/*     */   public static int floor(float value) {
/* 287 */     return (int)(value + 16384.0D) - 16384;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int floorPositive(float value) {
/* 293 */     return (int)value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ceil(float value) {
/* 299 */     return 16384 - (int)(16384.0D - value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ceilPositive(float value) {
/* 305 */     return (int)(value + 0.9999999D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int round(float value) {
/* 311 */     return (int)(value + 16384.5D) - 16384;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int roundPositive(float value) {
/* 316 */     return (int)(value + 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero(float value) {
/* 321 */     return (Math.abs(value) <= 1.0E-6F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isZero(float value, float tolerance) {
/* 327 */     return (Math.abs(value) <= tolerance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqual(float a, float b) {
/* 334 */     return (Math.abs(a - b) <= 1.0E-6F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqual(float a, float b, float tolerance) {
/* 342 */     return (Math.abs(a - b) <= tolerance);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float log(float a, float value) {
/* 347 */     return (float)(Math.log(value) / Math.log(a));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float log2(float value) {
/* 352 */     return log(2.0F, value);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\MathUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */