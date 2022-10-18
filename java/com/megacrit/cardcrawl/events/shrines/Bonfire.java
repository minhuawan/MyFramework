/*     */ package com.megacrit.cardcrawl.events.shrines;
/*     */ 
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.cards.CardGroup;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.events.AbstractImageEvent;
/*     */ import com.megacrit.cardcrawl.helpers.RelicLibrary;
/*     */ import com.megacrit.cardcrawl.localization.EventStrings;
/*     */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*     */ import com.megacrit.cardcrawl.relics.Circlet;
/*     */ import com.megacrit.cardcrawl.relics.SpiritPoop;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
/*     */ 
/*     */ public class Bonfire
/*     */   extends AbstractImageEvent {
/*     */   public static final String ID = "Bonfire Elementals";
/*  19 */   private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Bonfire Elementals");
/*  20 */   public static final String NAME = eventStrings.NAME;
/*  21 */   public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
/*  22 */   public static final String[] OPTIONS = eventStrings.OPTIONS;
/*     */   
/*  24 */   private static final String DIALOG_1 = DESCRIPTIONS[0];
/*  25 */   private static final String DIALOG_2 = DESCRIPTIONS[1];
/*  26 */   private static final String DIALOG_3 = DESCRIPTIONS[2];
/*     */   
/*  28 */   private CUR_SCREEN screen = CUR_SCREEN.INTRO;
/*  29 */   private AbstractCard offeredCard = null;
/*     */   private boolean cardSelect = false;
/*     */   
/*     */   private enum CUR_SCREEN {
/*  33 */     INTRO, CHOOSE, COMPLETE;
/*     */   }
/*     */   
/*     */   public Bonfire() {
/*  37 */     super(NAME, DIALOG_1, "images/events/bonfire.jpg");
/*  38 */     this.imageEventText.setDialogOption(OPTIONS[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnterRoom() {
/*  44 */     if (Settings.AMBIANCE_ON) {
/*  45 */       CardCrawlGame.sound.play("EVENT_GOOP");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  51 */     super.update();
/*     */     
/*  53 */     if (this.cardSelect && 
/*  54 */       !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/*  55 */       int heal, heal2; this.offeredCard = AbstractDungeon.gridSelectScreen.selectedCards.remove(0);
/*     */       
/*  57 */       switch (this.offeredCard.rarity) {
/*     */         case INTRO:
/*  59 */           logMetricRemoveCardAndObtainRelic("Bonfire Elementals", "Offered Curse", this.offeredCard, (AbstractRelic)new SpiritPoop());
/*     */           break;
/*     */         case CHOOSE:
/*  62 */           logMetricCardRemoval("Bonfire Elementals", "Offered Basic", this.offeredCard);
/*     */           break;
/*     */         case COMPLETE:
/*  65 */           logMetricCardRemovalAndHeal("Bonfire Elementals", "Offered Common", this.offeredCard, 5);
/*     */         case null:
/*  67 */           logMetricCardRemovalAndHeal("Bonfire Elementals", "Offered Special", this.offeredCard, 5);
/*     */           break;
/*     */         case null:
/*  70 */           heal = AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth;
/*  71 */           logMetricCardRemovalAndHeal("Bonfire Elementals", "Offered Uncommon", this.offeredCard, heal);
/*     */           break;
/*     */         case null:
/*  74 */           heal2 = AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth;
/*  75 */           logMetricCardRemovalHealMaxHPUp("Bonfire Elementals", "Offered Rare", this.offeredCard, heal2, 10);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  81 */       setReward(this.offeredCard.rarity);
/*  82 */       AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(this.offeredCard, (Settings.WIDTH / 2), (Settings.HEIGHT / 2)));
/*     */       
/*  84 */       AbstractDungeon.player.masterDeck.removeCard(this.offeredCard);
/*  85 */       this.imageEventText.updateDialogOption(0, OPTIONS[1]);
/*  86 */       this.screen = CUR_SCREEN.COMPLETE;
/*  87 */       this.cardSelect = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buttonEffect(int buttonPressed) {
/*  94 */     switch (this.screen) {
/*     */       
/*     */       case INTRO:
/*  97 */         this.imageEventText.updateBodyText(DIALOG_2);
/*  98 */         this.imageEventText.updateDialogOption(0, OPTIONS[2]);
/*  99 */         this.screen = CUR_SCREEN.CHOOSE;
/*     */         break;
/*     */       case CHOOSE:
/* 102 */         if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
/* 103 */           .size() > 0) {
/* 104 */           AbstractDungeon.gridSelectScreen.open(
/* 105 */               CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[3], false, false, false, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 112 */           this.cardSelect = true; break;
/*     */         } 
/* 114 */         this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
/* 115 */         this.imageEventText.updateDialogOption(0, OPTIONS[1]);
/* 116 */         this.screen = CUR_SCREEN.COMPLETE;
/*     */         break;
/*     */       
/*     */       case COMPLETE:
/* 120 */         openMap();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setReward(AbstractCard.CardRarity rarity) {
/* 126 */     String dialog = DIALOG_3;
/* 127 */     switch (rarity) {
/*     */       case INTRO:
/* 129 */         dialog = dialog + DESCRIPTIONS[3];
/* 130 */         if (!AbstractDungeon.player.hasRelic("Spirit Poop")) {
/* 131 */           AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, 
/*     */ 
/*     */               
/* 134 */               RelicLibrary.getRelic("Spirit Poop").makeCopy()); break;
/*     */         } 
/* 136 */         AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, (AbstractRelic)new Circlet());
/*     */         break;
/*     */       
/*     */       case CHOOSE:
/* 140 */         dialog = dialog + DESCRIPTIONS[4];
/*     */         break;
/*     */       case COMPLETE:
/*     */       case null:
/* 144 */         dialog = dialog + DESCRIPTIONS[5];
/* 145 */         AbstractDungeon.player.heal(5);
/*     */         break;
/*     */       case null:
/* 148 */         dialog = dialog + DESCRIPTIONS[6];
/* 149 */         AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
/*     */         break;
/*     */       case null:
/* 152 */         dialog = dialog + DESCRIPTIONS[7];
/* 153 */         AbstractDungeon.player.increaseMaxHp(10, false);
/* 154 */         AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.imageEventText.updateBodyText(dialog);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\events\shrines\Bonfire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */