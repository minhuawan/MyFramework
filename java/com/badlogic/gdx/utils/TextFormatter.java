/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
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
/*     */ class TextFormatter
/*     */ {
/*     */   private MessageFormat messageFormat;
/*  31 */   private StringBuilder buffer = new StringBuilder(); public TextFormatter(Locale locale, boolean useMessageFormat) {
/*  32 */     if (useMessageFormat) this.messageFormat = new MessageFormat("", locale);
/*     */   
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(String pattern, Object... args) {
/*  60 */     if (this.messageFormat != null) {
/*  61 */       this.messageFormat.applyPattern(replaceEscapeChars(pattern));
/*  62 */       return this.messageFormat.format(args);
/*     */     } 
/*  64 */     return simpleFormat(pattern, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String replaceEscapeChars(String pattern) {
/*  73 */     this.buffer.setLength(0);
/*  74 */     boolean changed = false;
/*  75 */     int len = pattern.length();
/*  76 */     for (int i = 0; i < len; i++) {
/*  77 */       char ch = pattern.charAt(i);
/*  78 */       if (ch == '\'') {
/*  79 */         changed = true;
/*  80 */         this.buffer.append("''");
/*  81 */       } else if (ch == '{') {
/*  82 */         int j = i + 1;
/*  83 */         while (j < len && pattern.charAt(j) == '{')
/*  84 */           j++; 
/*  85 */         int escaped = (j - i) / 2;
/*  86 */         if (escaped > 0) {
/*  87 */           changed = true;
/*  88 */           this.buffer.append('\'');
/*     */           while (true) {
/*  90 */             this.buffer.append('{');
/*  91 */             if (--escaped <= 0)
/*  92 */             { this.buffer.append('\''); break; } 
/*     */           } 
/*  94 */         }  if ((j - i) % 2 != 0) this.buffer.append('{'); 
/*  95 */         i = j - 1;
/*     */       } else {
/*  97 */         this.buffer.append(ch);
/*     */       } 
/*     */     } 
/* 100 */     return changed ? this.buffer.toString() : pattern;
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
/*     */   private String simpleFormat(String pattern, Object... args) {
/* 116 */     this.buffer.setLength(0);
/* 117 */     boolean changed = false;
/* 118 */     int placeholder = -1;
/* 119 */     int patternLength = pattern.length();
/* 120 */     for (int i = 0; i < patternLength; i++) {
/* 121 */       char ch = pattern.charAt(i);
/* 122 */       if (placeholder < 0) {
/* 123 */         if (ch == '{') {
/* 124 */           changed = true;
/* 125 */           if (i + 1 < patternLength && pattern.charAt(i + 1) == '{') {
/* 126 */             this.buffer.append(ch);
/* 127 */             i++;
/*     */           } else {
/* 129 */             placeholder = 0;
/*     */           } 
/*     */         } else {
/* 132 */           this.buffer.append(ch);
/*     */         }
/*     */       
/* 135 */       } else if (ch == '}') {
/* 136 */         if (placeholder >= args.length)
/* 137 */           throw new IllegalArgumentException("Argument index out of bounds: " + placeholder); 
/* 138 */         if (pattern.charAt(i - 1) == '{')
/* 139 */           throw new IllegalArgumentException("Missing argument index after a left curly brace"); 
/* 140 */         if (args[placeholder] == null) {
/* 141 */           this.buffer.append("null");
/*     */         } else {
/* 143 */           this.buffer.append(args[placeholder].toString());
/* 144 */         }  placeholder = -1;
/*     */       } else {
/* 146 */         if (ch < '0' || ch > '9')
/* 147 */           throw new IllegalArgumentException("Unexpected '" + ch + "' while parsing argument index"); 
/* 148 */         placeholder = placeholder * 10 + ch - 48;
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     if (placeholder >= 0) throw new IllegalArgumentException("Unmatched braces in the pattern.");
/*     */     
/* 154 */     return changed ? this.buffer.toString() : pattern;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\TextFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */