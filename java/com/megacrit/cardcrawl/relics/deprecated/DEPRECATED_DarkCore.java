/*    */ package com.megacrit.cardcrawl.relics.deprecated;
/*    */ 
/*    */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*    */ 
/*    */ public class DEPRECATED_DarkCore extends AbstractRelic {
/*    */   public static final String ID = "Dark Core";
/*    */   
/*    */   public DEPRECATED_DarkCore() {
/*  9 */     super("Dark Core", "vCore.png", AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 14 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 19 */     return new DEPRECATED_DarkCore();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\deprecated\DEPRECATED_DarkCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */