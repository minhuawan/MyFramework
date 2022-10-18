/*     */ package com.sun.jna;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IntegerType
/*     */   extends Number
/*     */   implements NativeMapped
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int size;
/*     */   private Number number;
/*     */   private boolean unsigned;
/*     */   private long value;
/*     */   
/*     */   public IntegerType(int size) {
/*  50 */     this(size, 0L, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegerType(int size, boolean unsigned) {
/*  55 */     this(size, 0L, unsigned);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegerType(int size, long value) {
/*  60 */     this(size, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegerType(int size, long value, boolean unsigned) {
/*  65 */     this.size = size;
/*  66 */     this.unsigned = unsigned;
/*  67 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(long value) {
/*  74 */     long truncated = value;
/*  75 */     this.value = value;
/*  76 */     switch (this.size) {
/*     */       case 1:
/*  78 */         if (this.unsigned) this.value = value & 0xFFL; 
/*  79 */         truncated = (byte)(int)value;
/*  80 */         this.number = Byte.valueOf((byte)(int)value);
/*     */         break;
/*     */       case 2:
/*  83 */         if (this.unsigned) this.value = value & 0xFFFFL; 
/*  84 */         truncated = (short)(int)value;
/*  85 */         this.number = Short.valueOf((short)(int)value);
/*     */         break;
/*     */       case 4:
/*  88 */         if (this.unsigned) this.value = value & 0xFFFFFFFFL; 
/*  89 */         truncated = (int)value;
/*  90 */         this.number = Integer.valueOf((int)value);
/*     */         break;
/*     */       case 8:
/*  93 */         this.number = Long.valueOf(value);
/*     */         break;
/*     */       default:
/*  96 */         throw new IllegalArgumentException("Unsupported size: " + this.size);
/*     */     } 
/*  98 */     if (this.size < 8) {
/*  99 */       long mask = (1L << this.size * 8) - 1L ^ 0xFFFFFFFFFFFFFFFFL;
/* 100 */       if ((value < 0L && truncated != value) || (value >= 0L && (mask & value) != 0L))
/*     */       {
/* 102 */         throw new IllegalArgumentException("Argument value 0x" + 
/* 103 */             Long.toHexString(value) + " exceeds native capacity (" + this.size + " bytes) mask=0x" + 
/* 104 */             Long.toHexString(mask));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object toNative() {
/* 111 */     return this.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object fromNative(Object nativeValue, FromNativeContext context) {
/* 118 */     long value = (nativeValue == null) ? 0L : ((Number)nativeValue).longValue();
/*     */     try {
/* 120 */       IntegerType number = (IntegerType)getClass().newInstance();
/* 121 */       number.setValue(value);
/* 122 */       return number;
/*     */     }
/* 124 */     catch (InstantiationException e) {
/* 125 */       throw new IllegalArgumentException("Can't instantiate " + 
/* 126 */           getClass());
/*     */     }
/* 128 */     catch (IllegalAccessException e) {
/* 129 */       throw new IllegalArgumentException("Not allowed to instantiate " + 
/* 130 */           getClass());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> nativeType() {
/* 136 */     return this.number.getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 141 */     return (int)this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 146 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 151 */     return this.number.floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 156 */     return this.number.doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object rhs) {
/* 161 */     return (rhs instanceof IntegerType && this.number
/* 162 */       .equals(((IntegerType)rhs).number));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return this.number.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 172 */     return this.number.hashCode();
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
/*     */   public static <T extends IntegerType> int compare(T v1, T v2) {
/* 190 */     if (v1 == v2)
/* 191 */       return 0; 
/* 192 */     if (v1 == null)
/* 193 */       return 1; 
/* 194 */     if (v2 == null) {
/* 195 */       return -1;
/*     */     }
/* 197 */     return compare(v1.longValue(), v2.longValue());
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
/*     */   public static int compare(IntegerType v1, long v2) {
/* 213 */     if (v1 == null) {
/* 214 */       return 1;
/*     */     }
/* 216 */     return compare(v1.longValue(), v2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int compare(long v1, long v2) {
/* 222 */     if (v1 == v2)
/* 223 */       return 0; 
/* 224 */     if (v1 < v2) {
/* 225 */       return -1;
/*     */     }
/* 227 */     return 1;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\IntegerType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */