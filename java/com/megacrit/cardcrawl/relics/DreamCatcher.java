/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ 
/*    */ public class DreamCatcher extends AbstractRelic {
/*    */   public static final String ID = "Dream Catcher";
/*    */   
/*    */   public DreamCatcher() {
/* 10 */     super("Dream Catcher", "dreamCatcher.png", AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.MAGICAL);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 15 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 20 */     return new DreamCatcher();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSpawn() {
/* 25 */     return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\DreamCatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */