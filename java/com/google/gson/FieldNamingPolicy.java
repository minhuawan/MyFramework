/*     */ package com.google.gson;
/*     */ 
/*     */ import java.lang.reflect.Field;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum FieldNamingPolicy
/*     */   implements FieldNamingStrategy
/*     */ {
/*  37 */   IDENTITY {
/*     */     public String translateName(Field f) {
/*  39 */       return f.getName();
/*     */     }
/*     */   },
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
/*  53 */   UPPER_CAMEL_CASE {
/*     */     public String translateName(Field f) {
/*  55 */       return upperCaseFirstLetter(f.getName());
/*     */     }
/*     */   },
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
/*  72 */   UPPER_CAMEL_CASE_WITH_SPACES {
/*     */     public String translateName(Field f) {
/*  74 */       return upperCaseFirstLetter(separateCamelCase(f.getName(), " "));
/*     */     }
/*     */   },
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
/*  90 */   LOWER_CASE_WITH_UNDERSCORES {
/*     */     public String translateName(Field f) {
/*  92 */       return separateCamelCase(f.getName(), "_").toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   },
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
/* 113 */   LOWER_CASE_WITH_DASHES {
/*     */     public String translateName(Field f) {
/* 115 */       return separateCamelCase(f.getName(), "-").toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String separateCamelCase(String name, String separator) {
/* 124 */     StringBuilder translation = new StringBuilder();
/* 125 */     for (int i = 0; i < name.length(); i++) {
/* 126 */       char character = name.charAt(i);
/* 127 */       if (Character.isUpperCase(character) && translation.length() != 0) {
/* 128 */         translation.append(separator);
/*     */       }
/* 130 */       translation.append(character);
/*     */     } 
/* 132 */     return translation.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String upperCaseFirstLetter(String name) {
/* 139 */     StringBuilder fieldNameBuilder = new StringBuilder();
/* 140 */     int index = 0;
/* 141 */     char firstCharacter = name.charAt(index);
/*     */     
/* 143 */     while (index < name.length() - 1 && 
/* 144 */       !Character.isLetter(firstCharacter)) {
/*     */ 
/*     */ 
/*     */       
/* 148 */       fieldNameBuilder.append(firstCharacter);
/* 149 */       firstCharacter = name.charAt(++index);
/*     */     } 
/*     */     
/* 152 */     if (index == name.length()) {
/* 153 */       return fieldNameBuilder.toString();
/*     */     }
/*     */     
/* 156 */     if (!Character.isUpperCase(firstCharacter)) {
/* 157 */       String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter), name, ++index);
/* 158 */       return fieldNameBuilder.append(modifiedTarget).toString();
/*     */     } 
/* 160 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
/* 165 */     return (indexOfSubstring < srcString.length()) ? (firstCharacter + srcString.substring(indexOfSubstring)) : String.valueOf(firstCharacter);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\FieldNamingPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */