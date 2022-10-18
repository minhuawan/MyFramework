/*    */ package com.megacrit.cardcrawl.localization;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PowerStrings
/*    */ {
/*    */   public String NAME;
/*    */   public String[] DESCRIPTIONS;
/*    */   
/*    */   public static PowerStrings getMockPowerString() {
/* 11 */     PowerStrings retVal = new PowerStrings();
/* 12 */     retVal.NAME = "[MISSING_NAME]";
/* 13 */     retVal.DESCRIPTIONS = LocalizedStrings.createMockStringArray(6);
/* 14 */     return retVal;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\localization\PowerStrings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */