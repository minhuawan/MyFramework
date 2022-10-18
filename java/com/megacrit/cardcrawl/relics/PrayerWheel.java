/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ 
/*    */ public class PrayerWheel extends AbstractRelic {
/*    */   public static final String ID = "Prayer Wheel";
/*    */   
/*    */   public PrayerWheel() {
/* 10 */     super("Prayer Wheel", "prayerWheel.png", AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.CLINK);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 15 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 20 */     return new PrayerWheel();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSpawn() {
/* 25 */     return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\PrayerWheel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */