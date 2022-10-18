/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Bits
/*     */ {
/*  27 */   long[] bits = new long[] { 0L };
/*     */ 
/*     */ 
/*     */   
/*     */   public Bits() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Bits(int nbits) {
/*  36 */     checkCapacity(nbits >>> 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean get(int index) {
/*  43 */     int word = index >>> 6;
/*  44 */     if (word >= this.bits.length) return false; 
/*  45 */     return ((this.bits[word] & 1L << (index & 0x3F)) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAndClear(int index) {
/*  53 */     int word = index >>> 6;
/*  54 */     if (word >= this.bits.length) return false; 
/*  55 */     long oldBits = this.bits[word];
/*  56 */     this.bits[word] = this.bits[word] & (1L << (index & 0x3F) ^ 0xFFFFFFFFFFFFFFFFL);
/*  57 */     return (this.bits[word] != oldBits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAndSet(int index) {
/*  65 */     int word = index >>> 6;
/*  66 */     checkCapacity(word);
/*  67 */     long oldBits = this.bits[word];
/*  68 */     this.bits[word] = this.bits[word] | 1L << (index & 0x3F);
/*  69 */     return (this.bits[word] == oldBits);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int index) {
/*  75 */     int word = index >>> 6;
/*  76 */     checkCapacity(word);
/*  77 */     this.bits[word] = this.bits[word] | 1L << (index & 0x3F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flip(int index) {
/*  82 */     int word = index >>> 6;
/*  83 */     checkCapacity(word);
/*  84 */     this.bits[word] = this.bits[word] ^ 1L << (index & 0x3F);
/*     */   }
/*     */   
/*     */   private void checkCapacity(int len) {
/*  88 */     if (len >= this.bits.length) {
/*  89 */       long[] newBits = new long[len + 1];
/*  90 */       System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
/*  91 */       this.bits = newBits;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear(int index) {
/*  98 */     int word = index >>> 6;
/*  99 */     if (word >= this.bits.length)
/* 100 */       return;  this.bits[word] = this.bits[word] & (1L << (index & 0x3F) ^ 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 105 */     long[] bits = this.bits;
/* 106 */     int length = bits.length;
/* 107 */     for (int i = 0; i < length; i++) {
/* 108 */       bits[i] = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int numBits() {
/* 114 */     return this.bits.length << 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 122 */     long[] bits = this.bits;
/* 123 */     for (int word = bits.length - 1; word >= 0; word--) {
/* 124 */       long bitsAtWord = bits[word];
/* 125 */       if (bitsAtWord != 0L) {
/* 126 */         for (int bit = 63; bit >= 0; bit--) {
/* 127 */           if ((bitsAtWord & 1L << (bit & 0x3F)) != 0L) {
/* 128 */             return (word << 6) + bit + 1;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 133 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 138 */     long[] bits = this.bits;
/* 139 */     int length = bits.length;
/* 140 */     for (int i = 0; i < length; i++) {
/* 141 */       if (bits[i] != 0L) {
/* 142 */         return false;
/*     */       }
/*     */     } 
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextSetBit(int fromIndex) {
/* 151 */     long[] bits = this.bits;
/* 152 */     int word = fromIndex >>> 6;
/* 153 */     int bitsLength = bits.length;
/* 154 */     if (word >= bitsLength) return -1; 
/* 155 */     long bitsAtWord = bits[word];
/* 156 */     if (bitsAtWord != 0L) {
/* 157 */       for (int i = fromIndex & 0x3F; i < 64; i++) {
/* 158 */         if ((bitsAtWord & 1L << (i & 0x3F)) != 0L) {
/* 159 */           return (word << 6) + i;
/*     */         }
/*     */       } 
/*     */     }
/* 163 */     for (; ++word < bitsLength; word++) {
/* 164 */       if (word != 0) {
/* 165 */         bitsAtWord = bits[word];
/* 166 */         if (bitsAtWord != 0L) {
/* 167 */           for (int i = 0; i < 64; i++) {
/* 168 */             if ((bitsAtWord & 1L << (i & 0x3F)) != 0L) {
/* 169 */               return (word << 6) + i;
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 175 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextClearBit(int fromIndex) {
/* 180 */     long[] bits = this.bits;
/* 181 */     int word = fromIndex >>> 6;
/* 182 */     int bitsLength = bits.length;
/* 183 */     if (word >= bitsLength) return bits.length << 6; 
/* 184 */     long bitsAtWord = bits[word]; int i;
/* 185 */     for (i = fromIndex & 0x3F; i < 64; i++) {
/* 186 */       if ((bitsAtWord & 1L << (i & 0x3F)) == 0L) {
/* 187 */         return (word << 6) + i;
/*     */       }
/*     */     } 
/* 190 */     for (; ++word < bitsLength; word++) {
/* 191 */       if (word == 0) {
/* 192 */         return word << 6;
/*     */       }
/* 194 */       bitsAtWord = bits[word];
/* 195 */       for (i = 0; i < 64; i++) {
/* 196 */         if ((bitsAtWord & 1L << (i & 0x3F)) == 0L) {
/* 197 */           return (word << 6) + i;
/*     */         }
/*     */       } 
/*     */     } 
/* 201 */     return bits.length << 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void and(Bits other) {
/* 209 */     int commonWords = Math.min(this.bits.length, other.bits.length); int i;
/* 210 */     for (i = 0; commonWords > i; i++) {
/* 211 */       this.bits[i] = this.bits[i] & other.bits[i];
/*     */     }
/*     */     
/* 214 */     if (this.bits.length > commonWords) {
/* 215 */       int s; for (i = commonWords, s = this.bits.length; s > i; i++) {
/* 216 */         this.bits[i] = 0L;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void andNot(Bits other) {
/* 225 */     for (int i = 0, j = this.bits.length, k = other.bits.length; i < j && i < k; i++) {
/* 226 */       this.bits[i] = this.bits[i] & (other.bits[i] ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void or(Bits other) {
/* 235 */     int commonWords = Math.min(this.bits.length, other.bits.length); int i;
/* 236 */     for (i = 0; commonWords > i; i++) {
/* 237 */       this.bits[i] = this.bits[i] | other.bits[i];
/*     */     }
/*     */     
/* 240 */     if (commonWords < other.bits.length) {
/* 241 */       checkCapacity(other.bits.length); int s;
/* 242 */       for (i = commonWords, s = other.bits.length; s > i; i++) {
/* 243 */         this.bits[i] = other.bits[i];
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void xor(Bits other) {
/* 256 */     int commonWords = Math.min(this.bits.length, other.bits.length);
/*     */     int i;
/* 258 */     for (i = 0; commonWords > i; i++) {
/* 259 */       this.bits[i] = this.bits[i] ^ other.bits[i];
/*     */     }
/*     */     
/* 262 */     if (commonWords < other.bits.length) {
/* 263 */       checkCapacity(other.bits.length); int s;
/* 264 */       for (i = commonWords, s = other.bits.length; s > i; i++) {
/* 265 */         this.bits[i] = other.bits[i];
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Bits other) {
/* 275 */     long[] bits = this.bits;
/* 276 */     long[] otherBits = other.bits;
/* 277 */     for (int i = Math.min(bits.length, otherBits.length) - 1; i >= 0; i--) {
/* 278 */       if ((bits[i] & otherBits[i]) != 0L) {
/* 279 */         return true;
/*     */       }
/*     */     } 
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Bits other) {
/* 291 */     long[] bits = this.bits;
/* 292 */     long[] otherBits = other.bits;
/* 293 */     int otherBitsLength = otherBits.length;
/* 294 */     int bitsLength = bits.length;
/*     */     int i;
/* 296 */     for (i = bitsLength; i < otherBitsLength; i++) {
/* 297 */       if (otherBits[i] != 0L) {
/* 298 */         return false;
/*     */       }
/*     */     } 
/* 301 */     for (i = Math.min(bitsLength, otherBitsLength) - 1; i >= 0; i--) {
/* 302 */       if ((bits[i] & otherBits[i]) != otherBits[i]) {
/* 303 */         return false;
/*     */       }
/*     */     } 
/* 306 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 311 */     int word = length() >>> 6;
/* 312 */     int hash = 0;
/* 313 */     for (int i = 0; word >= i; i++) {
/* 314 */       hash = 127 * hash + (int)(this.bits[i] ^ this.bits[i] >>> 32L);
/*     */     }
/* 316 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 321 */     if (this == obj)
/* 322 */       return true; 
/* 323 */     if (obj == null)
/* 324 */       return false; 
/* 325 */     if (getClass() != obj.getClass()) {
/* 326 */       return false;
/*     */     }
/* 328 */     Bits other = (Bits)obj;
/* 329 */     long[] otherBits = other.bits;
/*     */     
/* 331 */     int commonWords = Math.min(this.bits.length, otherBits.length);
/* 332 */     for (int i = 0; commonWords > i; i++) {
/* 333 */       if (this.bits[i] != otherBits[i]) {
/* 334 */         return false;
/*     */       }
/*     */     } 
/* 337 */     if (this.bits.length == otherBits.length) {
/* 338 */       return true;
/*     */     }
/* 340 */     return (length() == other.length());
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Bits.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */