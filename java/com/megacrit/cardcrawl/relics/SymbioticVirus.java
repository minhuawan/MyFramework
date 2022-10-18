/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ import com.megacrit.cardcrawl.orbs.AbstractOrb;
/*    */ import com.megacrit.cardcrawl.orbs.Dark;
/*    */ 
/*    */ public class SymbioticVirus
/*    */   extends AbstractRelic {
/*    */   public SymbioticVirus() {
/* 10 */     super("Symbiotic Virus", "virus.png", AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL);
/*    */   }
/*    */   public static final String ID = "Symbiotic Virus";
/*    */   
/*    */   public String getUpdatedDescription() {
/* 15 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void atPreBattle() {
/* 20 */     AbstractDungeon.player.channelOrb((AbstractOrb)new Dark());
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 25 */     return new SymbioticVirus();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\SymbioticVirus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */