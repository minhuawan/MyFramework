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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomXS128
/*     */   extends Random
/*     */ {
/*     */   private static final double NORM_DOUBLE = 1.1102230246251565E-16D;
/*     */   private static final double NORM_FLOAT = 5.9604644775390625E-8D;
/*     */   private long seed0;
/*     */   private long seed1;
/*     */   
/*     */   public RandomXS128() {
/*  49 */     setSeed((new Random()).nextLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomXS128(long seed) {
/*  55 */     setSeed(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomXS128(long seed0, long seed1) {
/*  62 */     setState(seed0, seed1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long nextLong() {
/*  70 */     long s1 = this.seed0;
/*  71 */     long s0 = this.seed1;
/*  72 */     this.seed0 = s0;
/*  73 */     s1 ^= s1 << 23L;
/*  74 */     return (this.seed1 = s1 ^ s0 ^ s1 >>> 17L ^ s0 >>> 26L) + s0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int next(int bits) {
/*  80 */     return (int)(nextLong() & (1L << bits) - 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextInt() {
/*  88 */     return (int)nextLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextInt(int n) {
/*  99 */     return (int)nextLong(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long nextLong(long n) {
/* 110 */     if (n <= 0L) throw new IllegalArgumentException("n must be positive"); 
/*     */     while (true) {
/* 112 */       long bits = nextLong() >>> 1L;
/* 113 */       long value = bits % n;
/* 114 */       if (bits - value + n - 1L >= 0L) return value;
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double nextDouble() {
/* 124 */     return (nextLong() >>> 11L) * 1.1102230246251565E-16D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float nextFloat() {
/* 133 */     return (float)((nextLong() >>> 40L) * 5.9604644775390625E-8D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nextBoolean() {
/* 141 */     return ((nextLong() & 0x1L) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextBytes(byte[] bytes) {
/* 150 */     int n = 0;
/* 151 */     int i = bytes.length;
/* 152 */     while (i != 0) {
/* 153 */       n = (i < 8) ? i : 8; long bits;
/* 154 */       for (bits = nextLong(); n-- != 0; bits >>= 8L) {
/* 155 */         bytes[--i] = (byte)(int)bits;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long seed) {
/* 166 */     long seed0 = murmurHash3((seed == 0L) ? Long.MIN_VALUE : seed);
/* 167 */     setState(seed0, murmurHash3(seed0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(long seed0, long seed1) {
/* 174 */     this.seed0 = seed0;
/* 175 */     this.seed1 = seed1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getState(int seed) {
/* 184 */     return (seed == 0) ? this.seed0 : this.seed1;
/*     */   }
/*     */   
/*     */   private static final long murmurHash3(long x) {
/* 188 */     x ^= x >>> 33L;
/* 189 */     x *= -49064778989728563L;
/* 190 */     x ^= x >>> 33L;
/* 191 */     x *= -4265267296055464877L;
/* 192 */     x ^= x >>> 33L;
/*     */     
/* 194 */     return x;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\RandomXS128.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */