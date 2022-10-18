/*    */ package com.megacrit.cardcrawl.daily.mods;
/*    */ 
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.RunModStrings;
/*    */ 
/*    */ public class BlightChests extends AbstractDailyMod {
/*    */   public static final String ID = "Blight Chests";
/*  8 */   private static final RunModStrings modStrings = CardCrawlGame.languagePack.getRunModString("Blight Chests");
/*  9 */   public static final String NAME = modStrings.NAME, DESC = modStrings.DESCRIPTION;
/*    */   
/*    */   public BlightChests() {
/* 12 */     super("Blight Chests", NAME, DESC, "endless.png", true);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\daily\mods\BlightChests.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */