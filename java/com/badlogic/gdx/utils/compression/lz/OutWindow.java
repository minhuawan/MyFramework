/*    */ package com.badlogic.gdx.utils.compression.lz;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class OutWindow
/*    */ {
/*    */   byte[] _buffer;
/*    */   int _pos;
/* 10 */   int _windowSize = 0;
/*    */   int _streamPos;
/*    */   OutputStream _stream;
/*    */   
/*    */   public void Create(int windowSize) {
/* 15 */     if (this._buffer == null || this._windowSize != windowSize) this._buffer = new byte[windowSize]; 
/* 16 */     this._windowSize = windowSize;
/* 17 */     this._pos = 0;
/* 18 */     this._streamPos = 0;
/*    */   }
/*    */   
/*    */   public void SetStream(OutputStream stream) throws IOException {
/* 22 */     ReleaseStream();
/* 23 */     this._stream = stream;
/*    */   }
/*    */   
/*    */   public void ReleaseStream() throws IOException {
/* 27 */     Flush();
/* 28 */     this._stream = null;
/*    */   }
/*    */   
/*    */   public void Init(boolean solid) {
/* 32 */     if (!solid) {
/* 33 */       this._streamPos = 0;
/* 34 */       this._pos = 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void Flush() throws IOException {
/* 39 */     int size = this._pos - this._streamPos;
/* 40 */     if (size == 0)
/* 41 */       return;  this._stream.write(this._buffer, this._streamPos, size);
/* 42 */     if (this._pos >= this._windowSize) this._pos = 0; 
/* 43 */     this._streamPos = this._pos;
/*    */   }
/*    */   
/*    */   public void CopyBlock(int distance, int len) throws IOException {
/* 47 */     int pos = this._pos - distance - 1;
/* 48 */     if (pos < 0) pos += this._windowSize; 
/* 49 */     for (; len != 0; len--) {
/* 50 */       if (pos >= this._windowSize) pos = 0; 
/* 51 */       this._buffer[this._pos++] = this._buffer[pos++];
/* 52 */       if (this._pos >= this._windowSize) Flush(); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void PutByte(byte b) throws IOException {
/* 57 */     this._buffer[this._pos++] = b;
/* 58 */     if (this._pos >= this._windowSize) Flush(); 
/*    */   }
/*    */   
/*    */   public byte GetByte(int distance) {
/* 62 */     int pos = this._pos - distance - 1;
/* 63 */     if (pos < 0) pos += this._windowSize; 
/* 64 */     return this._buffer[pos];
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\compression\lz\OutWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */