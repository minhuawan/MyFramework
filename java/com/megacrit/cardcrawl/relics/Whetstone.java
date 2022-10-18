/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
/*    */ import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class Whetstone
/*    */   extends AbstractRelic
/*    */ {
/*    */   public static final String ID = "Whetstone";
/*    */   private static final int CARD_AMT = 2;
/*    */   
/*    */   public Whetstone() {
/* 19 */     super("Whetstone", "whetstone.png", AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.SOLID);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 24 */     return this.DESCRIPTIONS[0] + '\002' + this.DESCRIPTIONS[1];
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEquip() {
/* 29 */     ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
/* 30 */     for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
/* 31 */       if (c.canUpgrade() && c.type == AbstractCard.CardType.ATTACK) {
/* 32 */         upgradableCards.add(c);
/*    */       }
/*    */     } 
/* 35 */     Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
/*    */     
/* 37 */     if (!upgradableCards.isEmpty())
/*    */     {
/* 39 */       if (upgradableCards.size() == 1) {
/* 40 */         ((AbstractCard)upgradableCards.get(0)).upgrade();
/* 41 */         AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
/* 42 */         AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards
/* 43 */               .get(0)).makeStatEquivalentCopy()));
/* 44 */         AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*    */       } else {
/* 46 */         ((AbstractCard)upgradableCards.get(0)).upgrade();
/* 47 */         ((AbstractCard)upgradableCards.get(1)).upgrade();
/* 48 */         AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
/* 49 */         AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
/* 50 */         AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards
/*    */               
/* 52 */               .get(0)).makeStatEquivalentCopy(), Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*    */ 
/*    */         
/* 55 */         AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards
/*    */               
/* 57 */               .get(1)).makeStatEquivalentCopy(), Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*    */ 
/*    */         
/* 60 */         AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 66 */     return new Whetstone();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\Whetstone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */