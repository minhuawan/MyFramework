/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class XmlWriter
/*     */   extends Writer
/*     */ {
/*     */   private final Writer writer;
/*  44 */   private final Array<String> stack = new Array<String>();
/*     */   
/*     */   private String currentElement;
/*     */   private boolean indentNextClose;
/*     */   public int indent;
/*     */   
/*     */   public XmlWriter(Writer writer) {
/*  51 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   private void indent() throws IOException {
/*  55 */     int count = this.indent;
/*  56 */     if (this.currentElement != null) count++; 
/*  57 */     for (int i = 0; i < count; i++)
/*  58 */       this.writer.write(9); 
/*     */   }
/*     */   
/*     */   public XmlWriter element(String name) throws IOException {
/*  62 */     if (startElementContent()) this.writer.write(10); 
/*  63 */     indent();
/*  64 */     this.writer.write(60);
/*  65 */     this.writer.write(name);
/*  66 */     this.currentElement = name;
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public XmlWriter element(String name, Object text) throws IOException {
/*  71 */     return element(name).text(text).pop();
/*     */   }
/*     */   
/*     */   private boolean startElementContent() throws IOException {
/*  75 */     if (this.currentElement == null) return false; 
/*  76 */     this.indent++;
/*  77 */     this.stack.add(this.currentElement);
/*  78 */     this.currentElement = null;
/*  79 */     this.writer.write(">");
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   public XmlWriter attribute(String name, Object value) throws IOException {
/*  84 */     if (this.currentElement == null) throw new IllegalStateException(); 
/*  85 */     this.writer.write(32);
/*  86 */     this.writer.write(name);
/*  87 */     this.writer.write("=\"");
/*  88 */     this.writer.write((value == null) ? "null" : value.toString());
/*  89 */     this.writer.write(34);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public XmlWriter text(Object text) throws IOException {
/*  94 */     startElementContent();
/*  95 */     String string = (text == null) ? "null" : text.toString();
/*  96 */     this.indentNextClose = (string.length() > 64);
/*  97 */     if (this.indentNextClose) {
/*  98 */       this.writer.write(10);
/*  99 */       indent();
/*     */     } 
/* 101 */     this.writer.write(string);
/* 102 */     if (this.indentNextClose) this.writer.write(10); 
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public XmlWriter pop() throws IOException {
/* 107 */     if (this.currentElement != null) {
/* 108 */       this.writer.write("/>\n");
/* 109 */       this.currentElement = null;
/*     */     } else {
/* 111 */       this.indent = Math.max(this.indent - 1, 0);
/* 112 */       if (this.indentNextClose) indent(); 
/* 113 */       this.writer.write("</");
/* 114 */       this.writer.write(this.stack.pop());
/* 115 */       this.writer.write(">\n");
/*     */     } 
/* 117 */     this.indentNextClose = true;
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 123 */     while (this.stack.size != 0)
/* 124 */       pop(); 
/* 125 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 129 */     startElementContent();
/* 130 */     this.writer.write(cbuf, off, len);
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 134 */     this.writer.flush();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\XmlWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */