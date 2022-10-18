/*    */ package com.megacrit.cardcrawl.localization;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MonsterStrings
/*    */ {
/*    */   public String NAME;
/*    */   public String[] DIALOG;
/*    */   public String[] MOVES;
/*    */   
/*    */   public static MonsterStrings getMockMonsterString() {
/* 12 */     MonsterStrings retVal = new MonsterStrings();
/* 13 */     retVal.NAME = "[MISSING_NAME]";
/* 14 */     retVal.DIALOG = LocalizedStrings.createMockStringArray(5);
/* 15 */     retVal.MOVES = LocalizedStrings.createMockStringArray(5);
/* 16 */     return retVal;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\localization\MonsterStrings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */