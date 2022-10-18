/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ import com.megacrit.cardcrawl.orbs.AbstractOrb;
/*    */ import com.megacrit.cardcrawl.orbs.Lightning;
/*    */ 
/*    */ public class CrackedCore
/*    */   extends AbstractRelic {
/*    */   public CrackedCore() {
/* 10 */     super("Cracked Core", "crackedOrb.png", AbstractRelic.RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
/*    */   }
/*    */   public static final String ID = "Cracked Core";
/*    */   
/*    */   public String getUpdatedDescription() {
/* 15 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void atPreBattle() {
/* 20 */     AbstractDungeon.player.channelOrb((AbstractOrb)new Lightning());
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 25 */     return new CrackedCore();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\CrackedCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */