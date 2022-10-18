/*    */ package com.megacrit.cardcrawl.exceptions;
/*    */ 
/*    */ public class SaveFileLoadError
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public SaveFileLoadError() {}
/*    */   
/*    */   public SaveFileLoadError(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public SaveFileLoadError(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public SaveFileLoadError(Throwable cause) {
/* 19 */     super(cause);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\exceptions\SaveFileLoadError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */