/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ELFAnalyser
/*     */ {
/*  19 */   private static final byte[] ELF_MAGIC = new byte[] { Byte.MAX_VALUE, 69, 76, 70 };
/*     */   
/*     */   private static final int EF_ARM_ABI_FLOAT_HARD = 1024;
/*     */   
/*     */   private static final int EF_ARM_ABI_FLOAT_SOFT = 512;
/*     */   
/*     */   private static final int EI_DATA_BIG_ENDIAN = 2;
/*     */   
/*     */   private static final int E_MACHINE_ARM = 40;
/*     */   
/*     */   private static final int EI_CLASS_64BIT = 2;
/*     */   
/*     */   private final String filename;
/*     */ 
/*     */   
/*     */   public static ELFAnalyser analyse(String filename) throws IOException {
/*  35 */     ELFAnalyser res = new ELFAnalyser(filename);
/*  36 */     res.runDetection();
/*  37 */     return res;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean ELF = false;
/*     */   
/*     */   private boolean _64Bit = false;
/*     */   
/*     */   private boolean bigEndian = false;
/*     */   
/*     */   private boolean armHardFloat = false;
/*     */   private boolean armSoftFloat = false;
/*     */   private boolean arm = false;
/*     */   
/*     */   public boolean isELF() {
/*  52 */     return this.ELF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is64Bit() {
/*  60 */     return this._64Bit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBigEndian() {
/*  68 */     return this.bigEndian;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/*  75 */     return this.filename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArmHardFloat() {
/*  83 */     return this.armHardFloat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArmSoftFloat() {
/*  91 */     return this.armSoftFloat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArm() {
/*  99 */     return this.arm;
/*     */   }
/*     */   
/*     */   private ELFAnalyser(String filename) {
/* 103 */     this.filename = filename;
/*     */   }
/*     */   
/*     */   private void runDetection() throws IOException {
/* 107 */     RandomAccessFile raf = new RandomAccessFile(this.filename, "r");
/*     */ 
/*     */     
/*     */     try {
/* 111 */       if (raf.length() > 4L) {
/* 112 */         byte[] magic = new byte[4];
/* 113 */         raf.seek(0L);
/* 114 */         raf.read(magic);
/* 115 */         if (Arrays.equals(magic, ELF_MAGIC)) {
/* 116 */           this.ELF = true;
/*     */         }
/*     */       } 
/* 119 */       if (!this.ELF) {
/*     */         return;
/*     */       }
/* 122 */       raf.seek(4L);
/*     */ 
/*     */       
/* 125 */       byte sizeIndicator = raf.readByte();
/* 126 */       this._64Bit = (sizeIndicator == 2);
/* 127 */       raf.seek(0L);
/* 128 */       ByteBuffer headerData = ByteBuffer.allocate(this._64Bit ? 64 : 52);
/* 129 */       raf.getChannel().read(headerData, 0L);
/* 130 */       this.bigEndian = (headerData.get(5) == 2);
/* 131 */       headerData.order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
/*     */       
/* 133 */       this.arm = (headerData.get(18) == 40);
/*     */       
/* 135 */       if (this.arm) {
/* 136 */         int flags = headerData.getInt(this._64Bit ? 48 : 36);
/* 137 */         this.armHardFloat = ((flags & 0x400) == 1024);
/* 138 */         this.armSoftFloat = !this.armHardFloat;
/*     */       } 
/*     */     } finally {
/*     */       try {
/* 142 */         raf.close();
/* 143 */       } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ELFAnalyser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */