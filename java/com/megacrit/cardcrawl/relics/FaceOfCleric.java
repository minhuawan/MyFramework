/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ 
/*    */ public class FaceOfCleric extends AbstractRelic {
/*    */   public static final String ID = "FaceOfCleric";
/*    */   
/*    */   public FaceOfCleric() {
/*  9 */     super("FaceOfCleric", "clericFace.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 14 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void onVictory() {
/* 19 */     flash();
/* 20 */     AbstractDungeon.player.increaseMaxHp(1, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 25 */     return new FaceOfCleric();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\FaceOfCleric.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */