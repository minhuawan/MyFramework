/*     */ package com.megacrit.cardcrawl.neow;
/*     */ 
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.helpers.PotionHelper;
/*     */ import com.megacrit.cardcrawl.helpers.SaveHelper;
/*     */ import com.megacrit.cardcrawl.localization.CharacterStrings;
/*     */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*     */ import com.megacrit.cardcrawl.relics.NeowsLament;
/*     */ import com.megacrit.cardcrawl.rewards.RewardItem;
/*     */ import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
/*     */ import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
/*     */ import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NeowReward
/*     */ {
/*     */   public static class NeowRewardDef
/*     */   {
/*     */     public NeowReward.NeowRewardType type;
/*     */     public String desc;
/*     */     
/*     */     public NeowRewardDef(NeowReward.NeowRewardType type, String desc) {
/*  47 */       this.type = type;
/*  48 */       this.desc = desc;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NeowRewardDrawbackDef
/*     */   {
/*     */     public NeowReward.NeowRewardDrawback type;
/*     */     public String desc;
/*     */     
/*     */     public NeowRewardDrawbackDef(NeowReward.NeowRewardDrawback type, String desc) {
/*  58 */       this.type = type;
/*  59 */       this.desc = desc;
/*     */     } }
/*     */   
/*     */   public NeowReward(boolean firstMini) {
/*     */     NeowRewardDef reward;
/*  64 */     this.optionLabel = "";
/*  65 */     this.drawback = NeowRewardDrawback.NONE;
/*  66 */     this.activated = false;
/*  67 */     this.hp_bonus = 0;
/*  68 */     this.cursed = false;
/*  69 */     this.hp_bonus = (int)(AbstractDungeon.player.maxHealth * 0.1F);
/*     */ 
/*     */     
/*  72 */     if (firstMini) {
/*  73 */       reward = new NeowRewardDef(NeowRewardType.THREE_ENEMY_KILL, TEXT[28]);
/*     */     } else {
/*  75 */       reward = new NeowRewardDef(NeowRewardType.TEN_PERCENT_HP_BONUS, TEXT[7] + this.hp_bonus + " ]");
/*     */     } 
/*     */     
/*  78 */     this.optionLabel += reward.desc;
/*  79 */     this.type = reward.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public NeowReward(int category) {
/*  84 */     this.optionLabel = "";
/*  85 */     this.drawback = NeowRewardDrawback.NONE;
/*  86 */     this.activated = false;
/*  87 */     this.hp_bonus = 0;
/*  88 */     this.cursed = false;
/*  89 */     this.hp_bonus = (int)(AbstractDungeon.player.maxHealth * 0.1F);
/*     */ 
/*     */     
/*  92 */     ArrayList<NeowRewardDef> possibleRewards = getRewardOptions(category);
/*     */ 
/*     */     
/*  95 */     NeowRewardDef reward = possibleRewards.get(NeowEvent.rng.random(0, possibleRewards.size() - 1));
/*  96 */     if (this.drawback != NeowRewardDrawback.NONE && this.drawbackDef != null) {
/*  97 */       this.optionLabel += this.drawbackDef.desc;
/*     */     }
/*  99 */     this.optionLabel += reward.desc;
/* 100 */     this.type = reward.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList<NeowRewardDrawbackDef> getRewardDrawbackOptions() {
/* 106 */     ArrayList<NeowRewardDrawbackDef> drawbackOptions = new ArrayList<>();
/* 107 */     drawbackOptions.add(new NeowRewardDrawbackDef(NeowRewardDrawback.TEN_PERCENT_HP_LOSS, TEXT[17] + this.hp_bonus + TEXT[18]));
/*     */ 
/*     */ 
/*     */     
/* 111 */     drawbackOptions.add(new NeowRewardDrawbackDef(NeowRewardDrawback.NO_GOLD, TEXT[19]));
/* 112 */     drawbackOptions.add(new NeowRewardDrawbackDef(NeowRewardDrawback.CURSE, TEXT[20]));
/* 113 */     drawbackOptions.add(new NeowRewardDrawbackDef(NeowRewardDrawback.PERCENT_DAMAGE, TEXT[21] + (AbstractDungeon.player.currentHealth / 10 * 3) + TEXT[29] + " "));
/*     */ 
/*     */ 
/*     */     
/* 117 */     return drawbackOptions;
/*     */   }
/*     */ 
/*     */   
/*     */   private ArrayList<NeowRewardDef> getRewardOptions(int category) {
/*     */     ArrayList<NeowRewardDrawbackDef> drawbackOptions;
/* 123 */     ArrayList<NeowRewardDef> rewardOptions = new ArrayList<>();
/* 124 */     switch (category) {
/*     */       case 0:
/* 126 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_CARDS, TEXT[0]));
/* 127 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.ONE_RANDOM_RARE_CARD, TEXT[1]));
/* 128 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.REMOVE_CARD, TEXT[2]));
/* 129 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.UPGRADE_CARD, TEXT[3]));
/* 130 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.TRANSFORM_CARD, TEXT[4]));
/* 131 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.RANDOM_COLORLESS, TEXT[30]));
/*     */         break;
/*     */       case 1:
/* 134 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_SMALL_POTIONS, TEXT[5]));
/* 135 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.RANDOM_COMMON_RELIC, TEXT[6]));
/* 136 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.TEN_PERCENT_HP_BONUS, TEXT[7] + this.hp_bonus + " ]"));
/*     */         
/* 138 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_ENEMY_KILL, TEXT[28]));
/* 139 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.HUNDRED_GOLD, TEXT[8] + 'd' + TEXT[9]));
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 146 */         drawbackOptions = getRewardDrawbackOptions();
/* 147 */         this.drawbackDef = drawbackOptions.get(NeowEvent.rng.random(0, drawbackOptions.size() - 1));
/* 148 */         this.drawback = this.drawbackDef.type;
/*     */         
/* 150 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.RANDOM_COLORLESS_2, TEXT[31]));
/* 151 */         if (this.drawback != NeowRewardDrawback.CURSE) {
/* 152 */           rewardOptions.add(new NeowRewardDef(NeowRewardType.REMOVE_TWO, TEXT[10]));
/*     */         }
/* 154 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.ONE_RARE_RELIC, TEXT[11]));
/* 155 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_RARE_CARDS, TEXT[12]));
/* 156 */         if (this.drawback != NeowRewardDrawback.NO_GOLD) {
/* 157 */           rewardOptions.add(new NeowRewardDef(NeowRewardType.TWO_FIFTY_GOLD, TEXT[13] + 'ú' + TEXT[14]));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 162 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.TRANSFORM_TWO_CARDS, TEXT[15]));
/* 163 */         if (this.drawback != NeowRewardDrawback.TEN_PERCENT_HP_LOSS) {
/* 164 */           rewardOptions.add(new NeowRewardDef(NeowRewardType.TWENTY_PERCENT_HP_BONUS, TEXT[16] + (this.hp_bonus * 2) + " ]"));
/*     */         }
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 171 */         rewardOptions.add(new NeowRewardDef(NeowRewardType.BOSS_RELIC, UNIQUE_REWARDS[0]));
/*     */         break;
/*     */     } 
/* 174 */     return rewardOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 180 */     if (this.activated) {
/* 181 */       if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/* 182 */         AbstractCard c, c2, c3, t1, t2; switch (this.type) {
/*     */           case RARE:
/* 184 */             c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/* 185 */             c.upgrade();
/* 186 */             AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
/* 187 */             AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */             break;
/*     */           
/*     */           case UNCOMMON:
/* 191 */             CardCrawlGame.sound.play("CARD_EXHAUST");
/* 192 */             AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards
/*     */                   
/* 194 */                   .get(0), (Settings.WIDTH / 2), (Settings.HEIGHT / 2)));
/*     */ 
/*     */             
/* 197 */             AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards
/* 198 */                 .get(0));
/*     */             break;
/*     */           case COMMON:
/* 201 */             CardCrawlGame.sound.play("CARD_EXHAUST");
/* 202 */             c2 = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/* 203 */             c3 = AbstractDungeon.gridSelectScreen.selectedCards.get(1);
/* 204 */             AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c2, Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 30.0F * Settings.scale, (Settings.HEIGHT / 2)));
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 209 */             AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c3, Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 30.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 214 */             AbstractDungeon.player.masterDeck.removeCard(c2);
/* 215 */             AbstractDungeon.player.masterDeck.removeCard(c3);
/*     */             break;
/*     */           case null:
/* 218 */             AbstractDungeon.transformCard(AbstractDungeon.gridSelectScreen.selectedCards
/* 219 */                 .get(0), false, NeowEvent.rng);
/*     */ 
/*     */             
/* 222 */             AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards
/* 223 */                 .get(0));
/* 224 */             AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(
/*     */                   
/* 226 */                   AbstractDungeon.getTransformedCard(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */             break;
/*     */ 
/*     */           
/*     */           case null:
/* 231 */             t1 = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
/* 232 */             t2 = AbstractDungeon.gridSelectScreen.selectedCards.get(1);
/* 233 */             AbstractDungeon.player.masterDeck.removeCard(t1);
/* 234 */             AbstractDungeon.player.masterDeck.removeCard(t2);
/* 235 */             AbstractDungeon.transformCard(t1, false, NeowEvent.rng);
/* 236 */             AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(
/*     */                   
/* 238 */                   AbstractDungeon.getTransformedCard(), Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 30.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */ 
/*     */             
/* 241 */             AbstractDungeon.transformCard(t2, false, NeowEvent.rng);
/* 242 */             AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(
/*     */                   
/* 244 */                   AbstractDungeon.getTransformedCard(), Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 30.0F * Settings.scale, Settings.HEIGHT / 2.0F));
/*     */             break;
/*     */ 
/*     */           
/*     */           default:
/* 249 */             logger.info("[ERROR] Missing Neow Reward Type: " + this.type.name());
/*     */             break;
/*     */         } 
/* 252 */         AbstractDungeon.gridSelectScreen.selectedCards.clear();
/* 253 */         AbstractDungeon.overlayMenu.cancelButton.hide();
/* 254 */         SaveHelper.saveIfAppropriate(SaveFile.SaveType.POST_NEOW);
/* 255 */         this.activated = false;
/*     */       } 
/* 257 */       if (this.cursed) {
/* 258 */         this.cursed = !this.cursed;
/* 259 */         AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(
/*     */               
/* 261 */               AbstractDungeon.getCardWithoutRng(AbstractCard.CardRarity.CURSE), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void activate() {
/*     */     int i, remove, j;
/* 269 */     this.activated = true;
/* 270 */     switch (this.drawback) {
/*     */       case RARE:
/* 272 */         this.cursed = true;
/*     */         break;
/*     */       case UNCOMMON:
/* 275 */         AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
/*     */         break;
/*     */       case COMMON:
/* 278 */         AbstractDungeon.player.decreaseMaxHealth(this.hp_bonus);
/*     */         break;
/*     */       case null:
/* 281 */         AbstractDungeon.player.damage(new DamageInfo(null, AbstractDungeon.player.currentHealth / 10 * 3, DamageInfo.DamageType.HP_LOSS));
/*     */         break;
/*     */       
/*     */       default:
/* 285 */         logger.info("[ERROR] Missing Neow Reward Drawback: " + this.drawback.name());
/*     */         break;
/*     */     } 
/* 288 */     switch (this.type) {
/*     */       case null:
/* 290 */         AbstractDungeon.cardRewardScreen.open(
/* 291 */             getColorlessRewardCards(true), null, 
/*     */             
/* 293 */             (CardCrawlGame.languagePack.getUIString("CardRewardScreen")).TEXT[1]);
/*     */         break;
/*     */       case null:
/* 296 */         AbstractDungeon.cardRewardScreen.open(
/* 297 */             getColorlessRewardCards(false), null, 
/*     */             
/* 299 */             (CardCrawlGame.languagePack.getUIString("CardRewardScreen")).TEXT[1]);
/*     */         break;
/*     */       case null:
/* 302 */         AbstractDungeon.cardRewardScreen.open(getRewardCards(true), null, TEXT[22]);
/*     */         break;
/*     */       case null:
/* 305 */         CardCrawlGame.sound.play("GOLD_JINGLE");
/* 306 */         AbstractDungeon.player.gainGold(100);
/*     */         break;
/*     */       case null:
/* 309 */         AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(
/*     */               
/* 311 */               AbstractDungeon.getCard(AbstractCard.CardRarity.RARE, NeowEvent.rng).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 316 */         AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), 
/*     */ 
/*     */             
/* 319 */             AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON));
/*     */         break;
/*     */       case null:
/* 322 */         AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), 
/*     */ 
/*     */             
/* 325 */             AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE));
/*     */         break;
/*     */       case null:
/* 328 */         AbstractDungeon.player.loseRelic(((AbstractRelic)AbstractDungeon.player.relics.get(0)).relicId);
/* 329 */         AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), 
/*     */ 
/*     */             
/* 332 */             AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS));
/*     */         break;
/*     */       case null:
/* 335 */         AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), (AbstractRelic)new NeowsLament());
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case UNCOMMON:
/* 341 */         AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
/* 342 */             .getPurgeableCards(), 1, TEXT[23], false, false, false, true);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case COMMON:
/* 351 */         AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
/* 352 */             .getPurgeableCards(), 2, TEXT[24], false, false, false, false);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case null:
/* 361 */         AbstractDungeon.player.increaseMaxHp(this.hp_bonus, true);
/*     */         break;
/*     */       case null:
/* 364 */         AbstractDungeon.cardRewardScreen.open(
/* 365 */             getRewardCards(false), null, 
/*     */             
/* 367 */             (CardCrawlGame.languagePack.getUIString("CardRewardScreen")).TEXT[1]);
/*     */         break;
/*     */       case null:
/* 370 */         CardCrawlGame.sound.play("POTION_1");
/* 371 */         for (i = 0; i < 3; i++) {
/* 372 */           AbstractDungeon.getCurrRoom().addPotionToRewards(PotionHelper.getRandomPotion());
/*     */         }
/* 374 */         AbstractDungeon.combatRewardScreen.open();
/* 375 */         (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.0F;
/* 376 */         remove = -1;
/* 377 */         for (j = 0; j < AbstractDungeon.combatRewardScreen.rewards.size(); j++) {
/* 378 */           if (((RewardItem)AbstractDungeon.combatRewardScreen.rewards.get(j)).type == RewardItem.RewardType.CARD) {
/* 379 */             remove = j;
/*     */             break;
/*     */           } 
/*     */         } 
/* 383 */         if (remove != -1) {
/* 384 */           AbstractDungeon.combatRewardScreen.rewards.remove(remove);
/*     */         }
/*     */         break;
/*     */       
/*     */       case null:
/* 389 */         AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
/* 390 */             .getPurgeableCards(), 1, TEXT[25], false, true, false, false);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case null:
/* 399 */         AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
/* 400 */             .getPurgeableCards(), 2, TEXT[26], false, false, false, false);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case null:
/* 409 */         AbstractDungeon.player.increaseMaxHp(this.hp_bonus * 2, true);
/*     */         break;
/*     */       case null:
/* 412 */         CardCrawlGame.sound.play("GOLD_JINGLE");
/* 413 */         AbstractDungeon.player.gainGold(250);
/*     */         break;
/*     */       case RARE:
/* 416 */         AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
/* 417 */             .getUpgradableCards(), 1, TEXT[27], true, false, false, false);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 426 */     CardCrawlGame.metricData.addNeowData(this.type.name(), this.drawback.name());
/*     */   }
/*     */   
/*     */   public ArrayList<AbstractCard> getColorlessRewardCards(boolean rareOnly) {
/* 430 */     ArrayList<AbstractCard> retVal = new ArrayList<>();
/* 431 */     for (int numCards = 3, i = 0; i < numCards; i++) {
/* 432 */       AbstractCard.CardRarity rarity = rollRarity();
/* 433 */       if (rareOnly) {
/* 434 */         rarity = AbstractCard.CardRarity.RARE;
/* 435 */       } else if (rarity == AbstractCard.CardRarity.COMMON) {
/* 436 */         rarity = AbstractCard.CardRarity.UNCOMMON;
/*     */       } 
/* 438 */       AbstractCard card = AbstractDungeon.getColorlessCardFromPool(rarity);
/*     */       
/* 440 */       while (retVal.contains(card)) {
/* 441 */         card = AbstractDungeon.getColorlessCardFromPool(rarity);
/*     */       }
/* 443 */       retVal.add(card);
/*     */     } 
/* 445 */     ArrayList<AbstractCard> retVal2 = new ArrayList<>();
/* 446 */     for (AbstractCard c : retVal) {
/* 447 */       retVal2.add(c.makeCopy());
/*     */     }
/* 449 */     return retVal2;
/*     */   }
/*     */   
/*     */   public ArrayList<AbstractCard> getRewardCards(boolean rareOnly) {
/* 453 */     ArrayList<AbstractCard> retVal = new ArrayList<>();
/* 454 */     for (int numCards = 3, i = 0; i < numCards; i++) {
/* 455 */       AbstractCard.CardRarity rarity = rollRarity();
/* 456 */       if (rareOnly) {
/* 457 */         rarity = AbstractCard.CardRarity.RARE;
/*     */       }
/* 459 */       AbstractCard card = null;
/* 460 */       switch (rarity) {
/*     */         case RARE:
/* 462 */           card = getCard(rarity);
/*     */           break;
/*     */         case UNCOMMON:
/* 465 */           card = getCard(rarity);
/*     */           break;
/*     */         case COMMON:
/* 468 */           card = getCard(rarity);
/*     */           break;
/*     */         default:
/* 471 */           logger.info("WTF?");
/*     */           break;
/*     */       } 
/*     */       
/* 475 */       while (retVal.contains(card)) {
/* 476 */         card = getCard(rarity);
/*     */       }
/* 478 */       retVal.add(card);
/*     */     } 
/* 480 */     ArrayList<AbstractCard> retVal2 = new ArrayList<>();
/* 481 */     for (AbstractCard c : retVal) {
/* 482 */       retVal2.add(c.makeCopy());
/*     */     }
/* 484 */     return retVal2;
/*     */   }
/*     */   
/*     */   public AbstractCard.CardRarity rollRarity() {
/* 488 */     if (NeowEvent.rng.randomBoolean(0.33F)) {
/* 489 */       return AbstractCard.CardRarity.UNCOMMON;
/*     */     }
/* 491 */     return AbstractCard.CardRarity.COMMON;
/*     */   }
/*     */   
/*     */   public AbstractCard getCard(AbstractCard.CardRarity rarity) {
/* 495 */     switch (rarity) {
/*     */       case RARE:
/* 497 */         return AbstractDungeon.rareCardPool.getRandomCard(NeowEvent.rng);
/*     */       case UNCOMMON:
/* 499 */         return AbstractDungeon.uncommonCardPool.getRandomCard(NeowEvent.rng);
/*     */       case COMMON:
/* 501 */         return AbstractDungeon.commonCardPool.getRandomCard(NeowEvent.rng);
/*     */     } 
/* 503 */     logger.info("Error in getCard in Neow Reward");
/* 504 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 509 */   private static final Logger logger = LogManager.getLogger(NeowReward.class.getName());
/* 510 */   private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("Neow Reward");
/* 511 */   public static final String[] NAMES = characterStrings.NAMES;
/* 512 */   public static final String[] TEXT = characterStrings.TEXT; public String optionLabel; public NeowRewardType type; public NeowRewardDrawback drawback; private boolean activated; private int hp_bonus;
/* 513 */   public static final String[] UNIQUE_REWARDS = characterStrings.UNIQUE_REWARDS; private boolean cursed; private static final int GOLD_BONUS = 100;
/*     */   private static final int LARGE_GOLD_BONUS = 250;
/*     */   private NeowRewardDrawbackDef drawbackDef;
/*     */   
/* 517 */   public enum NeowRewardType { RANDOM_COLORLESS_2, THREE_CARDS, ONE_RANDOM_RARE_CARD, REMOVE_CARD, UPGRADE_CARD, RANDOM_COLORLESS, TRANSFORM_CARD, THREE_SMALL_POTIONS, RANDOM_COMMON_RELIC, TEN_PERCENT_HP_BONUS, HUNDRED_GOLD, THREE_ENEMY_KILL, REMOVE_TWO, TRANSFORM_TWO_CARDS, ONE_RARE_RELIC, THREE_RARE_CARDS, TWO_FIFTY_GOLD, TWENTY_PERCENT_HP_BONUS, BOSS_RELIC; }
/*     */ 
/*     */   
/*     */   public enum NeowRewardDrawback {
/* 521 */     NONE, TEN_PERCENT_HP_LOSS, NO_GOLD, CURSE, PERCENT_DAMAGE;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\neow\NeowReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */