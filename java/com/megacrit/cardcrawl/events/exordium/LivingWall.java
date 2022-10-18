/*     */ package com.megacrit.cardcrawl.events.exordium;
/*     */ 
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.cards.CardGroup;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.events.AbstractEvent;
/*     */ import com.megacrit.cardcrawl.events.AbstractImageEvent;
/*     */ import com.megacrit.cardcrawl.localization.EventStrings;
/*     */ import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
/*     */ 
/*     */ public class LivingWall
/*     */   extends AbstractImageEvent {
/*     */   public static final String ID = "Living Wall";
/*  18 */   private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Living Wall");
/*  19 */   public static final String NAME = eventStrings.NAME;
/*  20 */   public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
/*  21 */   public static final String[] OPTIONS = eventStrings.OPTIONS;
/*     */   
/*  23 */   private static final String DIALOG_1 = DESCRIPTIONS[0];
/*  24 */   private static final String RESULT_DIALOG = DESCRIPTIONS[1];
/*     */   
/*  26 */   private CurScreen screen = CurScreen.INTRO;
/*     */   private boolean pickCard = false;
/*  28 */   private Choice choice = null;
/*     */   
/*     */   private enum CurScreen {
/*  31 */     INTRO, RESULT;
/*     */   }
/*     */   
/*     */   private enum Choice {
/*  35 */     FORGET, CHANGE, GROW;
/*     */   }
/*     */   
/*     */   public LivingWall() {
/*  39 */     super(NAME, DIALOG_1, "images/events/livingWall.jpg");
/*     */     
/*  41 */     this.imageEventText.setDialogOption(OPTIONS[0]);
/*  42 */     this.imageEventText.setDialogOption(OPTIONS[1]);
/*  43 */     if (AbstractDungeon.player.masterDeck.hasUpgradableCards().booleanValue()) {
/*  44 */       this.imageEventText.setDialogOption(OPTIONS[2]);
/*     */     } else {
/*  46 */       this.imageEventText.setDialogOption(OPTIONS[7], true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnterRoom() {
/*  52 */     if (Settings.AMBIANCE_ON) {
/*  53 */       CardCrawlGame.sound.play("EVENT_LIVING_WALL");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  59 */     super.update();
/*  60 */     if (this.pickCard && 
/*  61 */       !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/*  62 */       AbstractCard c, transCard, upgCard; switch (this.choice) {
/*     */         case INTRO:
/*  64 */           CardCrawlGame.sound.play("CARD_EXHAUST");
/*  65 */           AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards
/*     */                 
/*  67 */                 .get(0), (Settings.WIDTH / 2), (Settings.HEIGHT / 2)));
/*     */ 
/*     */           
/*  70 */           AbstractEvent.logMetricCardRemoval("Living Wall", "Forget", AbstractDungeon.gridSelectScreen.selectedCards
/*     */ 
/*     */               
/*  73 */               .get(0));
/*  74 */           AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards
/*  75 */               .get(0));
/*     */           break;
/*     */         case null:
/*  78 */           c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/*  79 */           AbstractDungeon.player.masterDeck.removeCard(c);
/*  80 */           AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);
/*  81 */           transCard = AbstractDungeon.getTransformedCard();
/*  82 */           AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(transCard, c.current_x, c.current_y));
/*     */           
/*  84 */           AbstractEvent.logMetricTransformCard("Living Wall", "Change", c, transCard);
/*     */           break;
/*     */         case null:
/*  87 */           AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */           
/*  89 */           ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0)).upgrade();
/*  90 */           upgCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/*  91 */           AbstractEvent.logMetricCardUpgrade("Living Wall", "Grow", upgCard);
/*  92 */           AbstractDungeon.player.bottledCardUpgradeCheck(upgCard);
/*     */           break;
/*     */       } 
/*  95 */       AbstractDungeon.gridSelectScreen.selectedCards.clear();
/*  96 */       this.pickCard = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buttonEffect(int buttonPressed) {
/* 103 */     switch (this.screen) {
/*     */       case INTRO:
/* 105 */         switch (buttonPressed) {
/*     */           case 0:
/* 107 */             this.choice = Choice.FORGET;
/* 108 */             if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
/* 109 */               .size() > 0) {
/* 110 */               AbstractDungeon.gridSelectScreen.open(
/* 111 */                   CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
/* 112 */                     .getPurgeableCards()), 1, OPTIONS[3], false, false, false, true);
/*     */             }
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 1:
/* 122 */             this.choice = Choice.CHANGE;
/* 123 */             if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
/* 124 */               .size() > 0) {
/* 125 */               AbstractDungeon.gridSelectScreen.open(
/* 126 */                   CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
/* 127 */                     .getPurgeableCards()), 1, OPTIONS[4], false, true, false, false);
/*     */             }
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           default:
/* 137 */             this.choice = Choice.GROW;
/* 138 */             if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
/* 139 */               .size() > 0) {
/* 140 */               AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
/* 141 */                   .getUpgradableCards(), 1, OPTIONS[5], true, false, false, false);
/*     */             }
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 151 */         this.pickCard = true;
/* 152 */         this.imageEventText.updateBodyText(RESULT_DIALOG);
/* 153 */         this.imageEventText.clearAllDialogs();
/* 154 */         this.imageEventText.setDialogOption(OPTIONS[6]);
/* 155 */         this.screen = CurScreen.RESULT;
/*     */         return;
/*     */     } 
/* 158 */     openMap();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\events\exordium\LivingWall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */