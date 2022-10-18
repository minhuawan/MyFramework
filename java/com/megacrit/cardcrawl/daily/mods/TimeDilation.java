/*    */ package com.megacrit.cardcrawl.daily.mods;
/*    */ 
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.RunModStrings;
/*    */ 
/*    */ public class TimeDilation extends AbstractDailyMod {
/*    */   public static final String ID = "Time Dilation";
/*  8 */   private static final RunModStrings modStrings = CardCrawlGame.languagePack.getRunModString("Time Dilation");
/*  9 */   public static final String NAME = modStrings.NAME, DESC = modStrings.DESCRIPTION;
/*    */   
/*    */   public TimeDilation() {
/* 12 */     super("Time Dilation", NAME, DESC, "slow_start.png", true);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\daily\mods\TimeDilation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */