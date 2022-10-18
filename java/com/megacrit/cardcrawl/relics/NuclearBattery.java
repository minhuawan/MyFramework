/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ import com.megacrit.cardcrawl.orbs.AbstractOrb;
/*    */ import com.megacrit.cardcrawl.orbs.Plasma;
/*    */ 
/*    */ public class NuclearBattery
/*    */   extends AbstractRelic {
/*    */   public NuclearBattery() {
/* 10 */     super("Nuclear Battery", "battery.png", AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
/*    */   }
/*    */   public static final String ID = "Nuclear Battery";
/*    */   
/*    */   public String getUpdatedDescription() {
/* 15 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void atPreBattle() {
/* 20 */     AbstractDungeon.player.channelOrb((AbstractOrb)new Plasma());
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 25 */     return new NuclearBattery();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\NuclearBattery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */