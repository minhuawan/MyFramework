/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SerializationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private StringBuffer trace;
/*    */   
/*    */   public SerializationException() {}
/*    */   
/*    */   public SerializationException(String message, Throwable cause) {
/* 29 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public SerializationException(String message) {
/* 33 */     super(message);
/*    */   }
/*    */   
/*    */   public SerializationException(Throwable cause) {
/* 37 */     super("", cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean causedBy(Class type) {
/* 42 */     return causedBy(this, type);
/*    */   }
/*    */   
/*    */   private boolean causedBy(Throwable ex, Class type) {
/* 46 */     Throwable cause = ex.getCause();
/* 47 */     if (cause == null || cause == ex) return false; 
/* 48 */     if (type.isAssignableFrom(cause.getClass())) return true; 
/* 49 */     return causedBy(cause, type);
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 53 */     if (this.trace == null) return super.getMessage(); 
/* 54 */     StringBuffer buffer = new StringBuffer(512);
/* 55 */     buffer.append(super.getMessage());
/* 56 */     if (buffer.length() > 0) buffer.append('\n'); 
/* 57 */     buffer.append("Serialization trace:");
/* 58 */     buffer.append(this.trace);
/* 59 */     return buffer.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTrace(String info) {
/* 65 */     if (info == null) throw new IllegalArgumentException("info cannot be null."); 
/* 66 */     if (this.trace == null) this.trace = new StringBuffer(512); 
/* 67 */     this.trace.append('\n');
/* 68 */     this.trace.append(info);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\SerializationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */