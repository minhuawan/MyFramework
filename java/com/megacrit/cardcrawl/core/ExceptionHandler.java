/*    */ package com.megacrit.cardcrawl.core;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExceptionHandler
/*    */ {
/*    */   public static void handleException(Exception e, Logger logger) {
/* 12 */     logger.error("Exception caught", e);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\core\ExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */