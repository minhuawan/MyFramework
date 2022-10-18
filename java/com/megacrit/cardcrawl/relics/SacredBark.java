/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ import com.megacrit.cardcrawl.potions.AbstractPotion;
/*    */ 
/*    */ public class SacredBark extends AbstractRelic {
/*    */   public static final String ID = "SacredBark";
/*    */   
/*    */   public SacredBark() {
/* 10 */     super("SacredBark", "bark.png", AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.MAGICAL);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 15 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEquip() {
/* 20 */     for (AbstractPotion p : AbstractDungeon.player.potions) {
/* 21 */       p.initializeData();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 27 */     return new SacredBark();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\SacredBark.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */