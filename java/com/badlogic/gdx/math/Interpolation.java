/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Interpolation
/*     */ {
/*     */   public abstract float apply(float paramFloat);
/*     */   
/*     */   public float apply(float start, float end, float a) {
/*  27 */     return start + (end - start) * apply(a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public static final Interpolation linear = new Interpolation() {
/*     */       public float apply(float a) {
/*  34 */         return a;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final Interpolation smooth = new Interpolation() {
/*     */       public float apply(float a) {
/*  43 */         return a * a * (3.0F - 2.0F * a);
/*     */       }
/*     */     };
/*  46 */   public static final Interpolation smooth2 = new Interpolation() {
/*     */       public float apply(float a) {
/*  48 */         a = a * a * (3.0F - 2.0F * a);
/*  49 */         return a * a * (3.0F - 2.0F * a);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  54 */   public static final Interpolation smoother = new Interpolation() {
/*     */       public float apply(float a) {
/*  56 */         return MathUtils.clamp(a * a * a * (a * (a * 6.0F - 15.0F) + 10.0F), 0.0F, 1.0F);
/*     */       }
/*     */     };
/*  59 */   public static final Interpolation fade = smoother;
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final Pow pow2 = new Pow(2);
/*     */   
/*  65 */   public static final PowIn pow2In = new PowIn(2);
/*     */   
/*  67 */   public static final PowOut pow2Out = new PowOut(2);
/*  68 */   public static final Interpolation pow2InInverse = new Interpolation() {
/*     */       public float apply(float a) {
/*  70 */         return (float)Math.sqrt(a);
/*     */       }
/*     */     };
/*  73 */   public static final Interpolation pow2OutInverse = new Interpolation() {
/*     */       public float apply(float a) {
/*  75 */         return 1.0F - (float)Math.sqrt(-(a - 1.0F));
/*     */       }
/*     */     };
/*     */   
/*  79 */   public static final Pow pow3 = new Pow(3);
/*  80 */   public static final PowIn pow3In = new PowIn(3);
/*  81 */   public static final PowOut pow3Out = new PowOut(3);
/*  82 */   public static final Interpolation pow3InInverse = new Interpolation() {
/*     */       public float apply(float a) {
/*  84 */         return (float)Math.cbrt(a);
/*     */       }
/*     */     };
/*  87 */   public static final Interpolation pow3OutInverse = new Interpolation() {
/*     */       public float apply(float a) {
/*  89 */         return 1.0F - (float)Math.cbrt(-(a - 1.0F));
/*     */       }
/*     */     };
/*     */   
/*  93 */   public static final Pow pow4 = new Pow(4);
/*  94 */   public static final PowIn pow4In = new PowIn(4);
/*  95 */   public static final PowOut pow4Out = new PowOut(4);
/*     */   
/*  97 */   public static final Pow pow5 = new Pow(5);
/*  98 */   public static final PowIn pow5In = new PowIn(5);
/*  99 */   public static final PowOut pow5Out = new PowOut(5);
/*     */   
/* 101 */   public static final Interpolation sine = new Interpolation() {
/*     */       public float apply(float a) {
/* 103 */         return (1.0F - MathUtils.cos(a * 3.1415927F)) / 2.0F;
/*     */       }
/*     */     };
/*     */   
/* 107 */   public static final Interpolation sineIn = new Interpolation() {
/*     */       public float apply(float a) {
/* 109 */         return 1.0F - MathUtils.cos(a * 3.1415927F / 2.0F);
/*     */       }
/*     */     };
/*     */   
/* 113 */   public static final Interpolation sineOut = new Interpolation() {
/*     */       public float apply(float a) {
/* 115 */         return MathUtils.sin(a * 3.1415927F / 2.0F);
/*     */       }
/*     */     };
/*     */   
/* 119 */   public static final Exp exp10 = new Exp(2.0F, 10.0F);
/* 120 */   public static final ExpIn exp10In = new ExpIn(2.0F, 10.0F);
/* 121 */   public static final ExpOut exp10Out = new ExpOut(2.0F, 10.0F);
/*     */   
/* 123 */   public static final Exp exp5 = new Exp(2.0F, 5.0F);
/* 124 */   public static final ExpIn exp5In = new ExpIn(2.0F, 5.0F);
/* 125 */   public static final ExpOut exp5Out = new ExpOut(2.0F, 5.0F);
/*     */   
/* 127 */   public static final Interpolation circle = new Interpolation() {
/*     */       public float apply(float a) {
/* 129 */         if (a <= 0.5F) {
/* 130 */           a *= 2.0F;
/* 131 */           return (1.0F - (float)Math.sqrt((1.0F - a * a))) / 2.0F;
/*     */         } 
/* 133 */         a--;
/* 134 */         a *= 2.0F;
/* 135 */         return ((float)Math.sqrt((1.0F - a * a)) + 1.0F) / 2.0F;
/*     */       }
/*     */     };
/*     */   
/* 139 */   public static final Interpolation circleIn = new Interpolation() {
/*     */       public float apply(float a) {
/* 141 */         return 1.0F - (float)Math.sqrt((1.0F - a * a));
/*     */       }
/*     */     };
/*     */   
/* 145 */   public static final Interpolation circleOut = new Interpolation() {
/*     */       public float apply(float a) {
/* 147 */         a--;
/* 148 */         return (float)Math.sqrt((1.0F - a * a));
/*     */       }
/*     */     };
/*     */   
/* 152 */   public static final Elastic elastic = new Elastic(2.0F, 10.0F, 7, 1.0F);
/* 153 */   public static final ElasticIn elasticIn = new ElasticIn(2.0F, 10.0F, 6, 1.0F);
/* 154 */   public static final ElasticOut elasticOut = new ElasticOut(2.0F, 10.0F, 7, 1.0F);
/*     */   
/* 156 */   public static final Swing swing = new Swing(1.5F);
/* 157 */   public static final SwingIn swingIn = new SwingIn(2.0F);
/* 158 */   public static final SwingOut swingOut = new SwingOut(2.0F);
/*     */   
/* 160 */   public static final Bounce bounce = new Bounce(4);
/* 161 */   public static final BounceIn bounceIn = new BounceIn(4);
/* 162 */   public static final BounceOut bounceOut = new BounceOut(4);
/*     */   
/*     */   public static class Pow
/*     */     extends Interpolation
/*     */   {
/*     */     final int power;
/*     */     
/*     */     public Pow(int power) {
/* 170 */       this.power = power;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 174 */       if (a <= 0.5F) return (float)Math.pow((a * 2.0F), this.power) / 2.0F; 
/* 175 */       return (float)Math.pow(((a - 1.0F) * 2.0F), this.power) / ((this.power % 2 == 0) ? -2 : 2) + 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PowIn extends Pow {
/*     */     public PowIn(int power) {
/* 181 */       super(power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 185 */       return (float)Math.pow(a, this.power);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PowOut extends Pow {
/*     */     public PowOut(int power) {
/* 191 */       super(power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 195 */       return (float)Math.pow((a - 1.0F), this.power) * ((this.power % 2 == 0) ? -1 : true) + 1.0F;
/*     */     } }
/*     */   
/*     */   public static class Exp extends Interpolation {
/*     */     final float value;
/*     */     final float power;
/*     */     final float min;
/*     */     final float scale;
/*     */     
/*     */     public Exp(float value, float power) {
/* 205 */       this.value = value;
/* 206 */       this.power = power;
/* 207 */       this.min = (float)Math.pow(value, -power);
/* 208 */       this.scale = 1.0F / (1.0F - this.min);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 212 */       if (a <= 0.5F) return ((float)Math.pow(this.value, (this.power * (a * 2.0F - 1.0F))) - this.min) * this.scale / 2.0F; 
/* 213 */       return (2.0F - ((float)Math.pow(this.value, (-this.power * (a * 2.0F - 1.0F))) - this.min) * this.scale) / 2.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ExpIn extends Exp {
/*     */     public ExpIn(float value, float power) {
/* 219 */       super(value, power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 223 */       return ((float)Math.pow(this.value, (this.power * (a - 1.0F))) - this.min) * this.scale;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ExpOut extends Exp {
/*     */     public ExpOut(float value, float power) {
/* 229 */       super(value, power);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 233 */       return 1.0F - ((float)Math.pow(this.value, (-this.power * a)) - this.min) * this.scale;
/*     */     } }
/*     */   
/*     */   public static class Elastic extends Interpolation {
/*     */     final float value;
/*     */     final float power;
/*     */     final float scale;
/*     */     final float bounces;
/*     */     
/*     */     public Elastic(float value, float power, int bounces, float scale) {
/* 243 */       this.value = value;
/* 244 */       this.power = power;
/* 245 */       this.scale = scale;
/* 246 */       this.bounces = bounces * 3.1415927F * ((bounces % 2 == 0) ? true : -1);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 250 */       if (a <= 0.5F) {
/* 251 */         a *= 2.0F;
/* 252 */         return (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale / 2.0F;
/*     */       } 
/* 254 */       a = 1.0F - a;
/* 255 */       a *= 2.0F;
/* 256 */       return 1.0F - (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale / 2.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ElasticIn extends Elastic {
/*     */     public ElasticIn(float value, float power, int bounces, float scale) {
/* 262 */       super(value, power, bounces, scale);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 266 */       if (a >= 0.99D) return 1.0F; 
/* 267 */       return (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ElasticOut extends Elastic {
/*     */     public ElasticOut(float value, float power, int bounces, float scale) {
/* 273 */       super(value, power, bounces, scale);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 277 */       if (a == 0.0F) return 0.0F; 
/* 278 */       a = 1.0F - a;
/* 279 */       return 1.0F - (float)Math.pow(this.value, (this.power * (a - 1.0F))) * MathUtils.sin(a * this.bounces) * this.scale;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Bounce
/*     */     extends BounceOut
/*     */   {
/*     */     public Bounce(float[] widths, float[] heights) {
/* 287 */       super(widths, heights);
/*     */     }
/*     */     
/*     */     public Bounce(int bounces) {
/* 291 */       super(bounces);
/*     */     }
/*     */     
/*     */     private float out(float a) {
/* 295 */       float test = a + this.widths[0] / 2.0F;
/* 296 */       if (test < this.widths[0]) return test / this.widths[0] / 2.0F - 1.0F; 
/* 297 */       return super.apply(a);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 301 */       if (a <= 0.5F) return (1.0F - out(1.0F - a * 2.0F)) / 2.0F; 
/* 302 */       return out(a * 2.0F - 1.0F) / 2.0F + 0.5F;
/*     */     } }
/*     */   
/*     */   public static class BounceOut extends Interpolation {
/*     */     final float[] widths;
/*     */     final float[] heights;
/*     */     
/*     */     public BounceOut(float[] widths, float[] heights) {
/* 310 */       if (widths.length != heights.length)
/* 311 */         throw new IllegalArgumentException("Must be the same number of widths and heights."); 
/* 312 */       this.widths = widths;
/* 313 */       this.heights = heights;
/*     */     }
/*     */     
/*     */     public BounceOut(int bounces) {
/* 317 */       if (bounces < 2 || bounces > 5) throw new IllegalArgumentException("bounces cannot be < 2 or > 5: " + bounces); 
/* 318 */       this.widths = new float[bounces];
/* 319 */       this.heights = new float[bounces];
/* 320 */       this.heights[0] = 1.0F;
/* 321 */       switch (bounces) {
/*     */         case 2:
/* 323 */           this.widths[0] = 0.6F;
/* 324 */           this.widths[1] = 0.4F;
/* 325 */           this.heights[1] = 0.33F;
/*     */           break;
/*     */         case 3:
/* 328 */           this.widths[0] = 0.4F;
/* 329 */           this.widths[1] = 0.4F;
/* 330 */           this.widths[2] = 0.2F;
/* 331 */           this.heights[1] = 0.33F;
/* 332 */           this.heights[2] = 0.1F;
/*     */           break;
/*     */         case 4:
/* 335 */           this.widths[0] = 0.34F;
/* 336 */           this.widths[1] = 0.34F;
/* 337 */           this.widths[2] = 0.2F;
/* 338 */           this.widths[3] = 0.15F;
/* 339 */           this.heights[1] = 0.26F;
/* 340 */           this.heights[2] = 0.11F;
/* 341 */           this.heights[3] = 0.03F;
/*     */           break;
/*     */         case 5:
/* 344 */           this.widths[0] = 0.3F;
/* 345 */           this.widths[1] = 0.3F;
/* 346 */           this.widths[2] = 0.2F;
/* 347 */           this.widths[3] = 0.1F;
/* 348 */           this.widths[4] = 0.1F;
/* 349 */           this.heights[1] = 0.45F;
/* 350 */           this.heights[2] = 0.3F;
/* 351 */           this.heights[3] = 0.15F;
/* 352 */           this.heights[4] = 0.06F;
/*     */           break;
/*     */       } 
/* 355 */       this.widths[0] = this.widths[0] * 2.0F;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 359 */       if (a == 1.0F) return 1.0F; 
/* 360 */       a += this.widths[0] / 2.0F;
/* 361 */       float width = 0.0F, height = 0.0F;
/* 362 */       for (int i = 0, n = this.widths.length; i < n; i++) {
/* 363 */         width = this.widths[i];
/* 364 */         if (a <= width) {
/* 365 */           height = this.heights[i];
/*     */           break;
/*     */         } 
/* 368 */         a -= width;
/*     */       } 
/* 370 */       a /= width;
/* 371 */       float z = 4.0F / width * height * a;
/* 372 */       return 1.0F - (z - z * a) * width;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BounceIn extends BounceOut {
/*     */     public BounceIn(float[] widths, float[] heights) {
/* 378 */       super(widths, heights);
/*     */     }
/*     */     
/*     */     public BounceIn(int bounces) {
/* 382 */       super(bounces);
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 386 */       return 1.0F - super.apply(1.0F - a);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Swing
/*     */     extends Interpolation
/*     */   {
/*     */     private final float scale;
/*     */     
/*     */     public Swing(float scale) {
/* 396 */       this.scale = scale * 2.0F;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 400 */       if (a <= 0.5F) {
/* 401 */         a *= 2.0F;
/* 402 */         return a * a * ((this.scale + 1.0F) * a - this.scale) / 2.0F;
/*     */       } 
/* 404 */       a--;
/* 405 */       a *= 2.0F;
/* 406 */       return a * a * ((this.scale + 1.0F) * a + this.scale) / 2.0F + 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SwingOut extends Interpolation {
/*     */     private final float scale;
/*     */     
/*     */     public SwingOut(float scale) {
/* 414 */       this.scale = scale;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 418 */       a--;
/* 419 */       return a * a * ((this.scale + 1.0F) * a + this.scale) + 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SwingIn extends Interpolation {
/*     */     private final float scale;
/*     */     
/*     */     public SwingIn(float scale) {
/* 427 */       this.scale = scale;
/*     */     }
/*     */     
/*     */     public float apply(float a) {
/* 431 */       return a * a * ((this.scale + 1.0F) * a - this.scale);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Interpolation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */