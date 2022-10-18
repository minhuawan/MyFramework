/*     */ package com.badlogic.gdx.utils.compression.lz;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class BinTree
/*     */   extends InWindow
/*     */ {
/*     */   int _cyclicBufferPos;
/*   9 */   int _cyclicBufferSize = 0;
/*     */   
/*     */   int _matchMaxLen;
/*     */   
/*     */   int[] _son;
/*     */   int[] _hash;
/*  15 */   int _cutValue = 255;
/*     */   int _hashMask;
/*  17 */   int _hashSizeSum = 0;
/*     */   
/*     */   boolean HASH_ARRAY = true;
/*     */   
/*     */   static final int kHash2Size = 1024;
/*     */   
/*     */   static final int kHash3Size = 65536;
/*     */   static final int kBT2HashSize = 65536;
/*     */   static final int kStartMaxLen = 1;
/*     */   static final int kHash3Offset = 1024;
/*     */   static final int kEmptyHashValue = 0;
/*     */   static final int kMaxValForNormalize = 1073741823;
/*  29 */   int kNumHashDirectBytes = 0;
/*  30 */   int kMinMatchCheck = 4;
/*  31 */   int kFixHashSize = 66560;
/*     */   
/*     */   public void SetType(int numHashBytes) {
/*  34 */     this.HASH_ARRAY = (numHashBytes > 2);
/*  35 */     if (this.HASH_ARRAY) {
/*  36 */       this.kNumHashDirectBytes = 0;
/*  37 */       this.kMinMatchCheck = 4;
/*  38 */       this.kFixHashSize = 66560;
/*     */     } else {
/*  40 */       this.kNumHashDirectBytes = 2;
/*  41 */       this.kMinMatchCheck = 3;
/*  42 */       this.kFixHashSize = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void Init() throws IOException {
/*  47 */     super.Init();
/*  48 */     for (int i = 0; i < this._hashSizeSum; i++)
/*  49 */       this._hash[i] = 0; 
/*  50 */     this._cyclicBufferPos = 0;
/*  51 */     ReduceOffsets(-1);
/*     */   }
/*     */   
/*     */   public void MovePos() throws IOException {
/*  55 */     if (++this._cyclicBufferPos >= this._cyclicBufferSize) this._cyclicBufferPos = 0; 
/*  56 */     super.MovePos();
/*  57 */     if (this._pos == 1073741823) Normalize(); 
/*     */   }
/*     */   
/*     */   public boolean Create(int historySize, int keepAddBufferBefore, int matchMaxLen, int keepAddBufferAfter) {
/*  61 */     if (historySize > 1073741567) return false; 
/*  62 */     this._cutValue = 16 + (matchMaxLen >> 1);
/*     */     
/*  64 */     int windowReservSize = (historySize + keepAddBufferBefore + matchMaxLen + keepAddBufferAfter) / 2 + 256;
/*     */     
/*  66 */     Create(historySize + keepAddBufferBefore, matchMaxLen + keepAddBufferAfter, windowReservSize);
/*     */     
/*  68 */     this._matchMaxLen = matchMaxLen;
/*     */     
/*  70 */     int cyclicBufferSize = historySize + 1;
/*  71 */     if (this._cyclicBufferSize != cyclicBufferSize) this._son = new int[(this._cyclicBufferSize = cyclicBufferSize) * 2];
/*     */     
/*  73 */     int hs = 65536;
/*     */     
/*  75 */     if (this.HASH_ARRAY) {
/*  76 */       hs = historySize - 1;
/*  77 */       hs |= hs >> 1;
/*  78 */       hs |= hs >> 2;
/*  79 */       hs |= hs >> 4;
/*  80 */       hs |= hs >> 8;
/*  81 */       hs >>= 1;
/*  82 */       hs |= 0xFFFF;
/*  83 */       if (hs > 16777216) hs >>= 1; 
/*  84 */       this._hashMask = hs;
/*  85 */       hs++;
/*  86 */       hs += this.kFixHashSize;
/*     */     } 
/*  88 */     if (hs != this._hashSizeSum) this._hash = new int[this._hashSizeSum = hs]; 
/*  89 */     return true;
/*     */   }
/*     */   
/*     */   public int GetMatches(int[] distances) throws IOException {
/*     */     int lenLimit, hashValue;
/*  94 */     if (this._pos + this._matchMaxLen <= this._streamPos) {
/*  95 */       lenLimit = this._matchMaxLen;
/*     */     } else {
/*  97 */       lenLimit = this._streamPos - this._pos;
/*  98 */       if (lenLimit < this.kMinMatchCheck) {
/*  99 */         MovePos();
/* 100 */         return 0;
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     int offset = 0;
/* 105 */     int matchMinPos = (this._pos > this._cyclicBufferSize) ? (this._pos - this._cyclicBufferSize) : 0;
/* 106 */     int cur = this._bufferOffset + this._pos;
/* 107 */     int maxLen = 1;
/* 108 */     int hash2Value = 0, hash3Value = 0;
/*     */     
/* 110 */     if (this.HASH_ARRAY) {
/* 111 */       int temp = CrcTable[this._bufferBase[cur] & 0xFF] ^ this._bufferBase[cur + 1] & 0xFF;
/* 112 */       hash2Value = temp & 0x3FF;
/* 113 */       temp ^= (this._bufferBase[cur + 2] & 0xFF) << 8;
/* 114 */       hash3Value = temp & 0xFFFF;
/* 115 */       hashValue = (temp ^ CrcTable[this._bufferBase[cur + 3] & 0xFF] << 5) & this._hashMask;
/*     */     } else {
/* 117 */       hashValue = this._bufferBase[cur] & 0xFF ^ (this._bufferBase[cur + 1] & 0xFF) << 8;
/*     */     } 
/* 119 */     int curMatch = this._hash[this.kFixHashSize + hashValue];
/* 120 */     if (this.HASH_ARRAY) {
/* 121 */       int curMatch2 = this._hash[hash2Value];
/* 122 */       int curMatch3 = this._hash[1024 + hash3Value];
/* 123 */       this._hash[hash2Value] = this._pos;
/* 124 */       this._hash[1024 + hash3Value] = this._pos;
/* 125 */       if (curMatch2 > matchMinPos && this._bufferBase[this._bufferOffset + curMatch2] == this._bufferBase[cur]) {
/* 126 */         distances[offset++] = maxLen = 2;
/* 127 */         distances[offset++] = this._pos - curMatch2 - 1;
/*     */       } 
/* 129 */       if (curMatch3 > matchMinPos && this._bufferBase[this._bufferOffset + curMatch3] == this._bufferBase[cur]) {
/* 130 */         if (curMatch3 == curMatch2) offset -= 2; 
/* 131 */         distances[offset++] = maxLen = 3;
/* 132 */         distances[offset++] = this._pos - curMatch3 - 1;
/* 133 */         curMatch2 = curMatch3;
/*     */       } 
/* 135 */       if (offset != 0 && curMatch2 == curMatch) {
/* 136 */         offset -= 2;
/* 137 */         maxLen = 1;
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     this._hash[this.kFixHashSize + hashValue] = this._pos;
/*     */     
/* 143 */     int ptr0 = (this._cyclicBufferPos << 1) + 1;
/* 144 */     int ptr1 = this._cyclicBufferPos << 1;
/*     */ 
/*     */     
/* 147 */     int len1 = this.kNumHashDirectBytes, len0 = len1;
/*     */     
/* 149 */     if (this.kNumHashDirectBytes != 0 && 
/* 150 */       curMatch > matchMinPos && 
/* 151 */       this._bufferBase[this._bufferOffset + curMatch + this.kNumHashDirectBytes] != this._bufferBase[cur + this.kNumHashDirectBytes]) {
/* 152 */       distances[offset++] = maxLen = this.kNumHashDirectBytes;
/* 153 */       distances[offset++] = this._pos - curMatch - 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 158 */     int count = this._cutValue;
/*     */     
/*     */     while (true) {
/* 161 */       if (curMatch <= matchMinPos || count-- == 0) {
/* 162 */         this._son[ptr1] = 0; this._son[ptr0] = 0;
/*     */         break;
/*     */       } 
/* 165 */       int delta = this._pos - curMatch;
/* 166 */       int cyclicPos = ((delta <= this._cyclicBufferPos) ? (this._cyclicBufferPos - delta) : (this._cyclicBufferPos - delta + this._cyclicBufferSize)) << 1;
/*     */ 
/*     */       
/* 169 */       int pby1 = this._bufferOffset + curMatch;
/* 170 */       int len = Math.min(len0, len1);
/* 171 */       if (this._bufferBase[pby1 + len] == this._bufferBase[cur + len]) { do {  }
/* 172 */         while (++len != lenLimit && 
/* 173 */           this._bufferBase[pby1 + len] == this._bufferBase[cur + len]);
/* 174 */         if (maxLen < len) {
/* 175 */           distances[offset++] = maxLen = len;
/* 176 */           distances[offset++] = delta - 1;
/* 177 */           if (len == lenLimit) {
/* 178 */             this._son[ptr1] = this._son[cyclicPos];
/* 179 */             this._son[ptr0] = this._son[cyclicPos + 1];
/*     */             break;
/*     */           } 
/*     */         }  }
/*     */       
/* 184 */       if ((this._bufferBase[pby1 + len] & 0xFF) < (this._bufferBase[cur + len] & 0xFF)) {
/* 185 */         this._son[ptr1] = curMatch;
/* 186 */         ptr1 = cyclicPos + 1;
/* 187 */         curMatch = this._son[ptr1];
/* 188 */         len1 = len; continue;
/*     */       } 
/* 190 */       this._son[ptr0] = curMatch;
/* 191 */       ptr0 = cyclicPos;
/* 192 */       curMatch = this._son[ptr0];
/* 193 */       len0 = len;
/*     */     } 
/*     */     
/* 196 */     MovePos();
/* 197 */     return offset;
/*     */   }
/*     */   
/*     */   public void Skip(int num) throws IOException {
/*     */     do {
/*     */       int lenLimit, hashValue;
/* 203 */       if (this._pos + this._matchMaxLen <= this._streamPos) {
/* 204 */         lenLimit = this._matchMaxLen;
/*     */       } else {
/* 206 */         lenLimit = this._streamPos - this._pos;
/* 207 */         if (lenLimit < this.kMinMatchCheck) {
/* 208 */           MovePos();
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/* 213 */       int matchMinPos = (this._pos > this._cyclicBufferSize) ? (this._pos - this._cyclicBufferSize) : 0;
/* 214 */       int cur = this._bufferOffset + this._pos;
/*     */ 
/*     */ 
/*     */       
/* 218 */       if (this.HASH_ARRAY) {
/* 219 */         int temp = CrcTable[this._bufferBase[cur] & 0xFF] ^ this._bufferBase[cur + 1] & 0xFF;
/* 220 */         int hash2Value = temp & 0x3FF;
/* 221 */         this._hash[hash2Value] = this._pos;
/* 222 */         temp ^= (this._bufferBase[cur + 2] & 0xFF) << 8;
/* 223 */         int hash3Value = temp & 0xFFFF;
/* 224 */         this._hash[1024 + hash3Value] = this._pos;
/* 225 */         hashValue = (temp ^ CrcTable[this._bufferBase[cur + 3] & 0xFF] << 5) & this._hashMask;
/*     */       } else {
/* 227 */         hashValue = this._bufferBase[cur] & 0xFF ^ (this._bufferBase[cur + 1] & 0xFF) << 8;
/*     */       } 
/* 229 */       int curMatch = this._hash[this.kFixHashSize + hashValue];
/* 230 */       this._hash[this.kFixHashSize + hashValue] = this._pos;
/*     */       
/* 232 */       int ptr0 = (this._cyclicBufferPos << 1) + 1;
/* 233 */       int ptr1 = this._cyclicBufferPos << 1;
/*     */ 
/*     */       
/* 236 */       int len1 = this.kNumHashDirectBytes, len0 = len1;
/*     */       
/* 238 */       int count = this._cutValue;
/*     */       while (true) {
/* 240 */         if (curMatch <= matchMinPos || count-- == 0) {
/* 241 */           this._son[ptr1] = 0; this._son[ptr0] = 0;
/*     */           
/*     */           break;
/*     */         } 
/* 245 */         int delta = this._pos - curMatch;
/* 246 */         int cyclicPos = ((delta <= this._cyclicBufferPos) ? (this._cyclicBufferPos - delta) : (this._cyclicBufferPos - delta + this._cyclicBufferSize)) << 1;
/*     */ 
/*     */         
/* 249 */         int pby1 = this._bufferOffset + curMatch;
/* 250 */         int len = Math.min(len0, len1);
/* 251 */         if (this._bufferBase[pby1 + len] == this._bufferBase[cur + len]) { do {  }
/* 252 */           while (++len != lenLimit && 
/* 253 */             this._bufferBase[pby1 + len] == this._bufferBase[cur + len]);
/* 254 */           if (len == lenLimit) {
/* 255 */             this._son[ptr1] = this._son[cyclicPos];
/* 256 */             this._son[ptr0] = this._son[cyclicPos + 1];
/*     */             break;
/*     */           }  }
/*     */         
/* 260 */         if ((this._bufferBase[pby1 + len] & 0xFF) < (this._bufferBase[cur + len] & 0xFF)) {
/* 261 */           this._son[ptr1] = curMatch;
/* 262 */           ptr1 = cyclicPos + 1;
/* 263 */           curMatch = this._son[ptr1];
/* 264 */           len1 = len; continue;
/*     */         } 
/* 266 */         this._son[ptr0] = curMatch;
/* 267 */         ptr0 = cyclicPos;
/* 268 */         curMatch = this._son[ptr0];
/* 269 */         len0 = len;
/*     */       } 
/*     */       
/* 272 */       MovePos();
/* 273 */     } while (--num != 0);
/*     */   }
/*     */   
/*     */   void NormalizeLinks(int[] items, int numItems, int subValue) {
/* 277 */     for (int i = 0; i < numItems; i++) {
/* 278 */       int value = items[i];
/* 279 */       if (value <= subValue) {
/* 280 */         value = 0;
/*     */       } else {
/* 282 */         value -= subValue;
/* 283 */       }  items[i] = value;
/*     */     } 
/*     */   }
/*     */   
/*     */   void Normalize() {
/* 288 */     int subValue = this._pos - this._cyclicBufferSize;
/* 289 */     NormalizeLinks(this._son, this._cyclicBufferSize * 2, subValue);
/* 290 */     NormalizeLinks(this._hash, this._hashSizeSum, subValue);
/* 291 */     ReduceOffsets(subValue);
/*     */   }
/*     */   
/*     */   public void SetCutValue(int cutValue) {
/* 295 */     this._cutValue = cutValue;
/*     */   }
/*     */   
/* 298 */   private static final int[] CrcTable = new int[256];
/*     */   
/*     */   static {
/* 301 */     for (int i = 0; i < 256; i++) {
/* 302 */       int r = i;
/* 303 */       for (int j = 0; j < 8; j++) {
/* 304 */         if ((r & 0x1) != 0)
/* 305 */         { r = r >>> 1 ^ 0xEDB88320; }
/*     */         else
/* 307 */         { r >>>= 1; } 
/* 308 */       }  CrcTable[i] = r;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\lz\BinTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */