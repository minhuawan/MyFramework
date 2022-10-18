/*     */ package com.megacrit.cardcrawl.events.shrines;
/*     */ 
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.cards.CardGroup;
/*     */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.events.AbstractImageEvent;
/*     */ import com.megacrit.cardcrawl.localization.EventStrings;
/*     */ import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class Designer
/*     */   extends AbstractImageEvent {
/*     */   public static final String ID = "Designer";
/*  23 */   private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Designer");
/*  24 */   public static final String NAME = eventStrings.NAME;
/*  25 */   public static final String[] DESC = eventStrings.DESCRIPTIONS;
/*  26 */   public static final String[] OPTIONS = eventStrings.OPTIONS;
/*  27 */   private CurrentScreen curScreen = CurrentScreen.INTRO; public static final int GOLD_REQ = 75; public static final int UPG_AMT = 2; public static final int REMOVE_AMT = 2; private boolean adjustmentUpgradesOne; private boolean cleanUpRemovesCards;
/*  28 */   private OptionChosen option = null;
/*     */   private int adjustCost;
/*     */   private int cleanUpCost;
/*     */   private int fullServiceCost;
/*     */   private int hpLoss;
/*     */   
/*  34 */   private enum CurrentScreen { INTRO, MAIN, DONE; }
/*     */ 
/*     */   
/*     */   private enum OptionChosen {
/*  38 */     UPGRADE, REMOVE, REMOVE_AND_UPGRADE, TRANSFORM, NONE;
/*     */   }
/*     */   
/*     */   public Designer() {
/*  42 */     super(NAME, DESC[0], "images/events/designer2.jpg");
/*  43 */     this.imageEventText.setDialogOption(OPTIONS[0]);
/*  44 */     this.option = OptionChosen.NONE;
/*     */ 
/*     */     
/*  47 */     this.adjustmentUpgradesOne = AbstractDungeon.miscRng.randomBoolean();
/*  48 */     this.cleanUpRemovesCards = AbstractDungeon.miscRng.randomBoolean();
/*     */     
/*  50 */     if (AbstractDungeon.ascensionLevel >= 15) {
/*  51 */       this.adjustCost = 50;
/*  52 */       this.cleanUpCost = 75;
/*  53 */       this.fullServiceCost = 110;
/*  54 */       this.hpLoss = 5;
/*     */     } else {
/*  56 */       this.adjustCost = 40;
/*  57 */       this.cleanUpCost = 60;
/*  58 */       this.fullServiceCost = 90;
/*  59 */       this.hpLoss = 3;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  65 */     super.update();
/*     */ 
/*     */     
/*  68 */     if (this.option != OptionChosen.NONE) {
/*  69 */       switch (this.option) {
/*     */         case INTRO:
/*  71 */           if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/*  72 */             CardCrawlGame.sound.play("CARD_EXHAUST");
/*  73 */             AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/*  74 */             logMetricCardRemovalAtCost("Designer", "Single Remove", c, this.cleanUpCost);
/*  75 */             AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */             
/*  77 */             AbstractDungeon.player.masterDeck.removeCard(c);
/*  78 */             AbstractDungeon.gridSelectScreen.selectedCards.clear();
/*  79 */             this.option = OptionChosen.NONE;
/*     */           } 
/*     */           break;
/*     */         case MAIN:
/*  83 */           if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/*     */ 
/*     */             
/*  86 */             AbstractCard removeCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/*     */             
/*  88 */             CardCrawlGame.sound.play("CARD_EXHAUST");
/*  89 */             AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(removeCard, Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  94 */             AbstractDungeon.player.masterDeck.removeCard(removeCard);
/*  95 */             AbstractDungeon.gridSelectScreen.selectedCards.clear();
/*     */             
/*  97 */             ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
/*  98 */             for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
/*  99 */               if (c.canUpgrade()) {
/* 100 */                 upgradableCards.add(c);
/*     */               }
/*     */             } 
/*     */             
/* 104 */             Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng
/*     */                   
/* 106 */                   .randomLong()));
/*     */             
/* 108 */             if (!upgradableCards.isEmpty()) {
/* 109 */               AbstractCard upgradeCard = upgradableCards.get(0);
/* 110 */               upgradeCard.upgrade();
/* 111 */               AbstractDungeon.player.bottledCardUpgradeCheck(upgradeCard);
/* 112 */               AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradeCard
/* 113 */                     .makeStatEquivalentCopy()));
/* 114 */               AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */               
/* 116 */               logMetricCardUpgradeAndRemovalAtCost("Designer", "Upgrade and Remove", upgradeCard, removeCard, this.fullServiceCost);
/*     */ 
/*     */             
/*     */             }
/*     */             else {
/*     */ 
/*     */               
/* 123 */               logMetricCardRemovalAtCost("Designer", "Removal", removeCard, this.fullServiceCost);
/*     */             } 
/*     */             
/* 126 */             this.option = OptionChosen.NONE;
/*     */           } 
/*     */           break;
/*     */         case DONE:
/* 130 */           if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/*     */             
/* 132 */             List<String> transCards = new ArrayList<>();
/* 133 */             List<String> obtainedCards = new ArrayList<>();
/*     */             
/* 135 */             if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 2) {
/* 136 */               AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/* 137 */               AbstractDungeon.player.masterDeck.removeCard(c);
/* 138 */               transCards.add(c.cardID);
/* 139 */               AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);
/* 140 */               AbstractCard newCard1 = AbstractDungeon.getTransformedCard();
/* 141 */               obtainedCards.add(newCard1.cardID);
/* 142 */               AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(newCard1, Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 147 */               c = AbstractDungeon.gridSelectScreen.selectedCards.get(1);
/* 148 */               AbstractDungeon.player.masterDeck.removeCard(c);
/* 149 */               transCards.add(c.cardID);
/* 150 */               AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);
/* 151 */               AbstractCard newCard2 = AbstractDungeon.getTransformedCard();
/* 152 */               obtainedCards.add(newCard2.cardID);
/* 153 */               AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(newCard2, Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 158 */               AbstractDungeon.gridSelectScreen.selectedCards.clear();
/*     */               
/* 160 */               logMetricTransformCardsAtCost("Designer", "Transformed Cards", transCards, obtainedCards, this.cleanUpCost);
/*     */ 
/*     */             
/*     */             }
/*     */             else {
/*     */ 
/*     */ 
/*     */               
/* 168 */               AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/* 169 */               AbstractDungeon.player.masterDeck.removeCard(c);
/* 170 */               AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);
/* 171 */               AbstractCard transCard = AbstractDungeon.getTransformedCard();
/* 172 */               AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(transCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */               
/* 174 */               AbstractDungeon.gridSelectScreen.selectedCards.clear();
/*     */               
/* 176 */               logMetricTransformCardAtCost("Designer", "Transform", transCard, c, this.cleanUpCost);
/*     */             } 
/* 178 */             this.option = OptionChosen.NONE;
/*     */           } 
/*     */           break;
/*     */         case null:
/* 182 */           if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/* 183 */             logMetricCardUpgradeAtCost("Designer", "Upgrade", AbstractDungeon.gridSelectScreen.selectedCards
/*     */ 
/*     */                 
/* 186 */                 .get(0), this.adjustCost);
/*     */             
/* 188 */             ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0)).upgrade();
/* 189 */             AbstractDungeon.player.bottledCardUpgradeCheck(AbstractDungeon.gridSelectScreen.selectedCards
/* 190 */                 .get(0));
/* 191 */             AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards
/*     */                   
/* 193 */                   .get(0)).makeStatEquivalentCopy()));
/* 194 */             AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */             
/* 196 */             AbstractDungeon.gridSelectScreen.selectedCards.clear();
/* 197 */             this.option = OptionChosen.NONE;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buttonEffect(int buttonPressed) {
/* 208 */     switch (this.curScreen) {
/*     */       case INTRO:
/* 210 */         this.imageEventText.updateBodyText(DESC[1]);
/*     */         
/* 212 */         this.imageEventText.removeDialogOption(0);
/*     */ 
/*     */         
/* 215 */         if (this.adjustmentUpgradesOne) {
/* 216 */           this.imageEventText.updateDialogOption(0, OPTIONS[1] + this.adjustCost + OPTIONS[6] + OPTIONS[9], (AbstractDungeon.player.gold < this.adjustCost || 
/*     */ 
/*     */               
/* 219 */               !AbstractDungeon.player.masterDeck
/* 220 */               .hasUpgradableCards().booleanValue()));
/*     */         } else {
/* 222 */           this.imageEventText.updateDialogOption(0, OPTIONS[1] + this.adjustCost + OPTIONS[6] + OPTIONS[7] + '\002' + OPTIONS[8], (AbstractDungeon.player.gold < this.adjustCost || 
/*     */ 
/*     */               
/* 225 */               !AbstractDungeon.player.masterDeck
/* 226 */               .hasUpgradableCards().booleanValue()));
/*     */         } 
/*     */ 
/*     */         
/* 230 */         if (this.cleanUpRemovesCards) {
/* 231 */           this.imageEventText.setDialogOption(OPTIONS[2] + this.cleanUpCost + OPTIONS[6] + OPTIONS[10], (AbstractDungeon.player.gold < this.cleanUpCost || 
/*     */               
/* 233 */               CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck)
/* 234 */               .size() == 0));
/*     */         } else {
/* 236 */           this.imageEventText.setDialogOption(OPTIONS[2] + this.cleanUpCost + OPTIONS[6] + OPTIONS[11] + '\002' + OPTIONS[12], (AbstractDungeon.player.gold < this.cleanUpCost || 
/*     */               
/* 238 */               CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck)
/* 239 */               .size() < 2));
/*     */         } 
/*     */ 
/*     */         
/* 243 */         this.imageEventText.setDialogOption(OPTIONS[3] + this.fullServiceCost + OPTIONS[6] + OPTIONS[13], (AbstractDungeon.player.gold < this.fullServiceCost || 
/*     */             
/* 245 */             CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck)
/* 246 */             .size() == 0));
/*     */ 
/*     */         
/* 249 */         this.imageEventText.setDialogOption(OPTIONS[4] + this.hpLoss + OPTIONS[5]);
/*     */         
/* 251 */         this.curScreen = CurrentScreen.MAIN;
/*     */         return;
/*     */       case MAIN:
/* 254 */         switch (buttonPressed) {
/*     */           
/*     */           case 0:
/* 257 */             this.imageEventText.updateBodyText(DESC[2]);
/* 258 */             AbstractDungeon.player.loseGold(this.adjustCost);
/*     */             
/* 260 */             if (this.adjustmentUpgradesOne) {
/* 261 */               this.option = OptionChosen.UPGRADE;
/* 262 */               AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
/* 263 */                   .getUpgradableCards(), 1, OPTIONS[15], true, false, false, false);
/*     */ 
/*     */ 
/*     */               
/*     */               break;
/*     */             } 
/*     */ 
/*     */             
/* 271 */             upgradeTwoRandomCards();
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 1:
/* 277 */             this.imageEventText.updateBodyText(DESC[2]);
/* 278 */             AbstractDungeon.player.loseGold(this.cleanUpCost);
/*     */             
/* 280 */             if (this.cleanUpRemovesCards) {
/* 281 */               this.option = OptionChosen.REMOVE;
/* 282 */               AbstractDungeon.gridSelectScreen.open(
/* 283 */                   CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
/* 284 */                     .getPurgeableCards()), 1, OPTIONS[17], false, false, false, true);
/*     */ 
/*     */ 
/*     */               
/*     */               break;
/*     */             } 
/*     */ 
/*     */             
/* 292 */             this.option = OptionChosen.TRANSFORM;
/* 293 */             AbstractDungeon.gridSelectScreen.open(
/* 294 */                 CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
/* 295 */                   .getPurgeableCards()), 2, OPTIONS[16], false, false, false, false);
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 2:
/* 307 */             this.imageEventText.updateBodyText(DESC[2]);
/* 308 */             AbstractDungeon.player.loseGold(this.fullServiceCost);
/* 309 */             this.option = OptionChosen.REMOVE_AND_UPGRADE;
/* 310 */             AbstractDungeon.gridSelectScreen.open(
/* 311 */                 CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
/* 312 */                   .getPurgeableCards()), 1, OPTIONS[17], false, false, false, true);
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 3:
/* 323 */             this.imageEventText.loadImage("images/events/designerPunched2.jpg");
/* 324 */             this.imageEventText.updateBodyText(DESC[3]);
/* 325 */             logMetricTakeDamage("Designer", "Punched", this.hpLoss);
/* 326 */             CardCrawlGame.sound.play("BLUNT_FAST");
/* 327 */             AbstractDungeon.player.damage(new DamageInfo(null, this.hpLoss, DamageInfo.DamageType.HP_LOSS));
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 335 */         this.imageEventText.updateDialogOption(0, OPTIONS[14]);
/* 336 */         this.imageEventText.clearRemainingOptions();
/* 337 */         this.curScreen = CurrentScreen.DONE;
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 342 */     openMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void upgradeTwoRandomCards() {
/* 348 */     ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
/* 349 */     for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
/* 350 */       if (c.canUpgrade()) {
/* 351 */         upgradableCards.add(c);
/*     */       }
/*     */     } 
/*     */     
/* 355 */     Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
/*     */     
/* 357 */     if (upgradableCards.isEmpty()) {
/*     */       
/* 359 */       logMetricLoseGold("Designer", "Tried to Upgrade", this.adjustCost);
/* 360 */     } else if (upgradableCards.size() == 1) {
/* 361 */       ((AbstractCard)upgradableCards.get(0)).upgrade();
/* 362 */       AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
/* 363 */       AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards
/* 364 */             .get(0)).makeStatEquivalentCopy()));
/* 365 */       AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/* 366 */       logMetricCardUpgradeAtCost("Designer", "Tried to Upgrade", upgradableCards.get(0), this.adjustCost);
/*     */     } else {
/* 368 */       List<String> cards = new ArrayList<>();
/* 369 */       cards.add(((AbstractCard)upgradableCards.get(0)).cardID);
/* 370 */       cards.add(((AbstractCard)upgradableCards.get(1)).cardID);
/* 371 */       logMetricUpgradeCardsAtCost("Designer", "Upgraded Two", cards, this.adjustCost);
/* 372 */       ((AbstractCard)upgradableCards.get(0)).upgrade();
/* 373 */       ((AbstractCard)upgradableCards.get(1)).upgrade();
/* 374 */       AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
/* 375 */       AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
/* 376 */       AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards
/*     */             
/* 378 */             .get(0)).makeStatEquivalentCopy(), Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */ 
/*     */       
/* 381 */       AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards
/*     */             
/* 383 */             .get(1)).makeStatEquivalentCopy(), Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */ 
/*     */       
/* 386 */       AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\events\shrines\Designer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */