/*     */ package com.badlogic.gdx.utils.compression.lz;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InWindow
/*     */ {
/*     */   public byte[] _bufferBase;
/*     */   InputStream _stream;
/*     */   int _posLimit;
/*     */   boolean _streamEndWasReached;
/*     */   int _pointerToLastSafePosition;
/*     */   public int _bufferOffset;
/*     */   public int _blockSize;
/*     */   public int _pos;
/*     */   int _keepSizeBefore;
/*     */   int _keepSizeAfter;
/*     */   public int _streamPos;
/*     */   
/*     */   public void MoveBlock() {
/*  24 */     int offset = this._bufferOffset + this._pos - this._keepSizeBefore;
/*     */     
/*  26 */     if (offset > 0) offset--;
/*     */     
/*  28 */     int numBytes = this._bufferOffset + this._streamPos - offset;
/*     */ 
/*     */     
/*  31 */     for (int i = 0; i < numBytes; i++)
/*  32 */       this._bufferBase[i] = this._bufferBase[offset + i]; 
/*  33 */     this._bufferOffset -= offset;
/*     */   }
/*     */   
/*     */   public void ReadBlock() throws IOException {
/*  37 */     if (this._streamEndWasReached)
/*     */       return;  while (true) {
/*  39 */       int size = 0 - this._bufferOffset + this._blockSize - this._streamPos;
/*  40 */       if (size == 0)
/*  41 */         return;  int numReadBytes = this._stream.read(this._bufferBase, this._bufferOffset + this._streamPos, size);
/*  42 */       if (numReadBytes == -1) {
/*  43 */         this._posLimit = this._streamPos;
/*  44 */         int pointerToPostion = this._bufferOffset + this._posLimit;
/*  45 */         if (pointerToPostion > this._pointerToLastSafePosition) this._posLimit = this._pointerToLastSafePosition - this._bufferOffset;
/*     */         
/*  47 */         this._streamEndWasReached = true;
/*     */         return;
/*     */       } 
/*  50 */       this._streamPos += numReadBytes;
/*  51 */       if (this._streamPos >= this._pos + this._keepSizeAfter) this._posLimit = this._streamPos - this._keepSizeAfter; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void Free() {
/*  56 */     this._bufferBase = null;
/*     */   }
/*     */   
/*     */   public void Create(int keepSizeBefore, int keepSizeAfter, int keepSizeReserv) {
/*  60 */     this._keepSizeBefore = keepSizeBefore;
/*  61 */     this._keepSizeAfter = keepSizeAfter;
/*  62 */     int blockSize = keepSizeBefore + keepSizeAfter + keepSizeReserv;
/*  63 */     if (this._bufferBase == null || this._blockSize != blockSize) {
/*  64 */       Free();
/*  65 */       this._blockSize = blockSize;
/*  66 */       this._bufferBase = new byte[this._blockSize];
/*     */     } 
/*  68 */     this._pointerToLastSafePosition = this._blockSize - keepSizeAfter;
/*     */   }
/*     */   
/*     */   public void SetStream(InputStream stream) {
/*  72 */     this._stream = stream;
/*     */   }
/*     */   
/*     */   public void ReleaseStream() {
/*  76 */     this._stream = null;
/*     */   }
/*     */   
/*     */   public void Init() throws IOException {
/*  80 */     this._bufferOffset = 0;
/*  81 */     this._pos = 0;
/*  82 */     this._streamPos = 0;
/*  83 */     this._streamEndWasReached = false;
/*  84 */     ReadBlock();
/*     */   }
/*     */   
/*     */   public void MovePos() throws IOException {
/*  88 */     this._pos++;
/*  89 */     if (this._pos > this._posLimit) {
/*  90 */       int pointerToPostion = this._bufferOffset + this._pos;
/*  91 */       if (pointerToPostion > this._pointerToLastSafePosition) MoveBlock(); 
/*  92 */       ReadBlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte GetIndexByte(int index) {
/*  97 */     return this._bufferBase[this._bufferOffset + this._pos + index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int GetMatchLen(int index, int distance, int limit) {
/* 102 */     if (this._streamEndWasReached && this._pos + index + limit > this._streamPos) limit = this._streamPos - this._pos + index; 
/* 103 */     distance++;
/*     */     
/* 105 */     int pby = this._bufferOffset + this._pos + index;
/*     */     
/*     */     int i;
/* 108 */     for (i = 0; i < limit && this._bufferBase[pby + i] == this._bufferBase[pby + i - distance]; i++);
/*     */     
/* 110 */     return i;
/*     */   }
/*     */   
/*     */   public int GetNumAvailableBytes() {
/* 114 */     return this._streamPos - this._pos;
/*     */   }
/*     */   
/*     */   public void ReduceOffsets(int subValue) {
/* 118 */     this._bufferOffset += subValue;
/* 119 */     this._posLimit -= subValue;
/* 120 */     this._pos -= subValue;
/* 121 */     this._streamPos -= subValue;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\lz\InWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */