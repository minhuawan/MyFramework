/*      */ package com.megacrit.cardcrawl.shop;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*      */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*      */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*      */ import com.megacrit.cardcrawl.cards.CardGroup;
/*      */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*      */ import com.megacrit.cardcrawl.core.Settings;
/*      */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*      */ import com.megacrit.cardcrawl.helpers.FontHelper;
/*      */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*      */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*      */ import com.megacrit.cardcrawl.helpers.ModHelper;
/*      */ import com.megacrit.cardcrawl.helpers.TipHelper;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*      */ import com.megacrit.cardcrawl.localization.CharacterStrings;
/*      */ import com.megacrit.cardcrawl.localization.TutorialStrings;
/*      */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*      */ import com.megacrit.cardcrawl.ui.DialogWord;
/*      */ import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;
/*      */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*      */ import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
/*      */ import com.megacrit.cardcrawl.vfx.FloatyEffect;
/*      */ import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
/*      */ import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
/*      */ import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ShopScreen
/*      */ {
/*   50 */   private static final Logger logger = LogManager.getLogger(ShopScreen.class.getName());
/*   51 */   private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Shop Tip");
/*   52 */   public static final String[] MSG = tutorialStrings.TEXT;
/*   53 */   public static final String[] LABEL = tutorialStrings.LABEL;
/*      */   
/*   55 */   private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("Shop Screen");
/*      */   
/*   57 */   public static final String[] NAMES = characterStrings.NAMES;
/*   58 */   public static final String[] TEXT = characterStrings.TEXT;
/*      */ 
/*      */   
/*      */   public boolean isActive = true;
/*      */ 
/*      */   
/*   64 */   private static Texture rugImg = null; private static Texture removeServiceImg = null; private static Texture soldOutImg = null; private static Texture handImg = null;
/*   65 */   private float rugY = Settings.HEIGHT / 2.0F + 540.0F * Settings.yScale;
/*      */   
/*      */   private static final float RUG_SPEED = 5.0F;
/*      */   
/*   69 */   public ArrayList<AbstractCard> coloredCards = new ArrayList<>();
/*   70 */   public ArrayList<AbstractCard> colorlessCards = new ArrayList<>();
/*   71 */   private static final float DRAW_START_X = Settings.WIDTH * 0.16F;
/*      */   private static final int NUM_CARDS_PER_LINE = 5;
/*      */   private static final float CARD_PRICE_JITTER = 0.1F;
/*   74 */   private static final float TOP_ROW_Y = 760.0F * Settings.yScale;
/*   75 */   private static final float BOTTOM_ROW_Y = 337.0F * Settings.yScale;
/*      */ 
/*      */   
/*   78 */   private float speechTimer = 0.0F;
/*      */   private static final float MIN_IDLE_MSG_TIME = 40.0F;
/*      */   private static final float MAX_IDLE_MSG_TIME = 60.0F;
/*      */   private static final float SPEECH_DURATION = 4.0F;
/*   82 */   private static final float SPEECH_TEXT_R_X = 164.0F * Settings.scale;
/*   83 */   private static final float SPEECH_TEXT_L_X = -166.0F * Settings.scale;
/*   84 */   private static final float SPEECH_TEXT_Y = 126.0F * Settings.scale;
/*   85 */   private ShopSpeechBubble speechBubble = null;
/*   86 */   private SpeechTextEffect dialogTextEffect = null;
/*   87 */   private static final String WELCOME_MSG = NAMES[0];
/*   88 */   private ArrayList<String> idleMessages = new ArrayList<>();
/*      */   
/*      */   private boolean saidWelcome = false;
/*      */   private boolean somethingHovered = false;
/*   92 */   private ArrayList<StoreRelic> relics = new ArrayList<>();
/*      */   
/*      */   private static final float RELIC_PRICE_JITTER = 0.05F;
/*      */   
/*   96 */   private ArrayList<StorePotion> potions = new ArrayList<>();
/*      */   
/*      */   private static final float POTION_PRICE_JITTER = 0.05F;
/*      */   
/*      */   public boolean purgeAvailable = false;
/*  101 */   public static int purgeCost = 75; public static int actualPurgeCost = 75;
/*      */   private static final int PURGE_COST_RAMP = 25;
/*      */   private boolean purgeHovered = false;
/*  104 */   private float purgeCardScale = 1.0F;
/*      */   private float purgeCardX;
/*      */   private float purgeCardY;
/*  107 */   private FloatyEffect f_effect = new FloatyEffect(20.0F, 0.1F);
/*  108 */   private float handTimer = 1.0F;
/*  109 */   private float handX = Settings.WIDTH / 2.0F;
/*  110 */   private float handY = Settings.HEIGHT;
/*  111 */   private float handTargetX = 0.0F;
/*  112 */   private float handTargetY = Settings.HEIGHT; private static final float HAND_SPEED = 6.0F;
/*      */   private static float HAND_W;
/*      */   private static float HAND_H;
/*  115 */   private float notHoveredTimer = 0.0F;
/*      */ 
/*      */   
/*  118 */   private static final float GOLD_IMG_WIDTH = ImageMaster.UI_GOLD.getWidth() * Settings.scale;
/*      */   private static final float COLORLESS_PRICE_BUMP = 1.2F;
/*      */   private OnSaleTag saleTag;
/*  121 */   private static final float GOLD_IMG_OFFSET_X = -50.0F * Settings.scale;
/*  122 */   private static final float GOLD_IMG_OFFSET_Y = -215.0F * Settings.scale;
/*  123 */   private static final float PRICE_TEXT_OFFSET_X = 16.0F * Settings.scale;
/*  124 */   private static final float PRICE_TEXT_OFFSET_Y = -180.0F * Settings.scale;
/*      */ 
/*      */   
/*  127 */   public ConfirmButton confirmButton = new ConfirmButton();
/*  128 */   public StoreRelic touchRelic = null;
/*  129 */   public StorePotion touchPotion = null;
/*  130 */   private AbstractCard touchCard = null;
/*      */   
/*      */   private boolean touchPurge = false;
/*      */ 
/*      */   
/*      */   public void init(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
/*  136 */     this.idleMessages.clear();
/*  137 */     if (AbstractDungeon.id.equals("TheEnding")) {
/*  138 */       Collections.addAll(this.idleMessages, Merchant.ENDING_TEXT);
/*      */     } else {
/*  140 */       Collections.addAll(this.idleMessages, TEXT);
/*      */     } 
/*      */     
/*  143 */     if (rugImg == null) {
/*  144 */       switch (Settings.language) {
/*      */         case COLOR_CARD:
/*  146 */           rugImg = ImageMaster.loadImage("images/npcs/rug/deu.png");
/*  147 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/deu.png");
/*  148 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/deu.png");
/*      */           break;
/*      */         case COLORLESS_CARD:
/*  151 */           rugImg = ImageMaster.loadImage("images/npcs/rug/epo.png");
/*  152 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/epo.png");
/*  153 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/epo.png");
/*      */           break;
/*      */         case RELIC:
/*  156 */           rugImg = ImageMaster.loadImage("images/npcs/rug/fin.png");
/*  157 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/fin.png");
/*  158 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/fin.png");
/*      */           break;
/*      */         case POTION:
/*  161 */           rugImg = ImageMaster.loadImage("images/npcs/rug/fra.png");
/*  162 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/fra.png");
/*  163 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/fra.png");
/*      */           break;
/*      */         case PURGE:
/*  166 */           rugImg = ImageMaster.loadImage("images/npcs/rug/ita.png");
/*  167 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ita.png");
/*  168 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ita.png");
/*      */           break;
/*      */         case null:
/*  171 */           rugImg = ImageMaster.loadImage("images/npcs/rug/jpn.png");
/*  172 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/jpn.png");
/*  173 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/jpn.png");
/*      */           break;
/*      */         case null:
/*  176 */           rugImg = ImageMaster.loadImage("images/npcs/rug/kor.png");
/*  177 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/kor.png");
/*  178 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/kor.png");
/*      */           break;
/*      */         case null:
/*  181 */           rugImg = ImageMaster.loadImage("images/npcs/rug/rus.png");
/*  182 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/rus.png");
/*  183 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/rus.png");
/*      */           break;
/*      */         case null:
/*  186 */           rugImg = ImageMaster.loadImage("images/npcs/rug/tha.png");
/*  187 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/tha.png");
/*  188 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/tha.png");
/*      */           break;
/*      */         case null:
/*  191 */           rugImg = ImageMaster.loadImage("images/npcs/rug/ukr.png");
/*  192 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ukr.png");
/*  193 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ukr.png");
/*      */           break;
/*      */         case null:
/*  196 */           rugImg = ImageMaster.loadImage("images/npcs/rug/zhs.png");
/*  197 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/zhs.png");
/*  198 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/zhs.png");
/*      */           break;
/*      */         default:
/*  201 */           rugImg = ImageMaster.loadImage("images/npcs/rug/eng.png");
/*  202 */           removeServiceImg = ImageMaster.loadImage("images/npcs/purge/eng.png");
/*  203 */           soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/eng.png");
/*      */           break;
/*      */       } 
/*  206 */       handImg = ImageMaster.loadImage("images/npcs/merchantHand.png");
/*      */     } 
/*      */     
/*  209 */     HAND_W = handImg.getWidth() * Settings.scale;
/*  210 */     HAND_H = handImg.getHeight() * Settings.scale;
/*      */ 
/*      */     
/*  213 */     this.coloredCards.clear();
/*  214 */     this.colorlessCards.clear();
/*  215 */     this.coloredCards = coloredCards;
/*  216 */     this.colorlessCards = colorlessCards;
/*  217 */     initCards();
/*      */ 
/*      */     
/*  220 */     initRelics();
/*      */ 
/*      */     
/*  223 */     initPotions();
/*      */ 
/*      */     
/*  226 */     this.purgeAvailable = true;
/*  227 */     this.purgeCardY = -1000.0F;
/*  228 */     this.purgeCardX = Settings.WIDTH * 0.73F * Settings.scale;
/*  229 */     this.purgeCardScale = 0.7F;
/*  230 */     actualPurgeCost = purgeCost;
/*      */ 
/*      */     
/*  233 */     if (AbstractDungeon.ascensionLevel >= 16) {
/*  234 */       applyDiscount(1.1F, false);
/*      */     }
/*      */     
/*  237 */     if (AbstractDungeon.player.hasRelic("The Courier")) {
/*  238 */       applyDiscount(0.8F, true);
/*      */     }
/*  240 */     if (AbstractDungeon.player.hasRelic("Membership Card")) {
/*  241 */       applyDiscount(0.5F, true);
/*      */     }
/*      */     
/*  244 */     if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
/*  245 */       actualPurgeCost = 50;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void resetPurgeCost() {
/*  250 */     purgeCost = 75;
/*  251 */     actualPurgeCost = 75;
/*      */   }
/*      */   
/*      */   private void initCards() {
/*  255 */     int tmp = (int)(Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
/*      */     
/*  257 */     float padX = (int)(tmp + AbstractCard.IMG_WIDTH_S);
/*      */     
/*      */     int i;
/*  260 */     for (i = 0; i < this.coloredCards.size(); i++) {
/*  261 */       float tmpPrice = AbstractCard.getPrice(((AbstractCard)this.coloredCards.get(i)).rarity) * AbstractDungeon.merchantRng.random(0.9F, 1.1F);
/*      */ 
/*      */ 
/*      */       
/*  265 */       AbstractCard c = this.coloredCards.get(i);
/*  266 */       c.price = (int)tmpPrice;
/*  267 */       c.current_x = (Settings.WIDTH / 2);
/*  268 */       c.target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
/*      */       
/*  270 */       for (AbstractRelic r : AbstractDungeon.player.relics) {
/*  271 */         r.onPreviewObtainCard(c);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  276 */     for (i = 0; i < this.colorlessCards.size(); i++) {
/*  277 */       float tmpPrice = AbstractCard.getPrice(((AbstractCard)this.colorlessCards.get(i)).rarity) * AbstractDungeon.merchantRng.random(0.9F, 1.1F);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  282 */       tmpPrice *= 1.2F;
/*      */       
/*  284 */       AbstractCard c = this.colorlessCards.get(i);
/*      */       
/*  286 */       c.price = (int)tmpPrice;
/*  287 */       c.current_x = (Settings.WIDTH / 2);
/*  288 */       c.target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
/*      */       
/*  290 */       for (AbstractRelic r : AbstractDungeon.player.relics) {
/*  291 */         r.onPreviewObtainCard(c);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  296 */     AbstractCard saleCard = this.coloredCards.get(AbstractDungeon.merchantRng.random(0, 4));
/*  297 */     saleCard.price /= 2;
/*  298 */     this.saleTag = new OnSaleTag(saleCard);
/*  299 */     setStartingCardPositions();
/*      */   }
/*      */   
/*      */   public static void purgeCard() {
/*  303 */     AbstractDungeon.player.loseGold(actualPurgeCost);
/*  304 */     CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
/*  305 */     purgeCost += 25;
/*  306 */     actualPurgeCost = purgeCost;
/*      */     
/*  308 */     if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
/*  309 */       actualPurgeCost = 50;
/*  310 */       AbstractDungeon.player.getRelic("Smiling Mask").stopPulse();
/*      */     }
/*  312 */     else if (AbstractDungeon.player.hasRelic("The Courier") && AbstractDungeon.player.hasRelic("Membership Card")) {
/*  313 */       actualPurgeCost = MathUtils.round(purgeCost * 0.8F * 0.5F);
/*  314 */     } else if (AbstractDungeon.player.hasRelic("The Courier")) {
/*  315 */       actualPurgeCost = MathUtils.round(purgeCost * 0.8F);
/*  316 */     } else if (AbstractDungeon.player.hasRelic("Membership Card")) {
/*  317 */       actualPurgeCost = MathUtils.round(purgeCost * 0.5F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updatePurge() {
/*  323 */     if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
/*  324 */       purgeCard();
/*  325 */       for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
/*  326 */         CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
/*  327 */         AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
/*      */         
/*  329 */         AbstractDungeon.player.masterDeck.removeCard(card);
/*      */       } 
/*  331 */       AbstractDungeon.gridSelectScreen.selectedCards.clear();
/*  332 */       AbstractDungeon.shopScreen.purgeAvailable = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String getCantBuyMsg() {
/*  337 */     ArrayList<String> list = new ArrayList<>();
/*  338 */     list.add(NAMES[1]);
/*  339 */     list.add(NAMES[2]);
/*  340 */     list.add(NAMES[3]);
/*  341 */     list.add(NAMES[4]);
/*  342 */     list.add(NAMES[5]);
/*  343 */     list.add(NAMES[6]);
/*      */     
/*  345 */     return list.get(MathUtils.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   public static String getBuyMsg() {
/*  349 */     ArrayList<String> list = new ArrayList<>();
/*  350 */     list.add(NAMES[7]);
/*  351 */     list.add(NAMES[8]);
/*  352 */     list.add(NAMES[9]);
/*  353 */     list.add(NAMES[10]);
/*  354 */     list.add(NAMES[11]);
/*      */     
/*  356 */     return list.get(MathUtils.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void applyUpgrades(AbstractCard.CardType type) {
/*  361 */     for (AbstractCard c : this.coloredCards) {
/*  362 */       if (c.type == type) {
/*  363 */         c.upgrade();
/*      */       }
/*      */     } 
/*  366 */     for (AbstractCard c : this.colorlessCards) {
/*  367 */       if (c.type == type) {
/*  368 */         c.upgrade();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void applyDiscount(float multiplier, boolean affectPurge) {
/*  374 */     for (StoreRelic r : this.relics) {
/*  375 */       r.price = MathUtils.round(r.price * multiplier);
/*      */     }
/*  377 */     for (StorePotion p : this.potions) {
/*  378 */       p.price = MathUtils.round(p.price * multiplier);
/*      */     }
/*  380 */     for (AbstractCard c : this.coloredCards) {
/*  381 */       c.price = MathUtils.round(c.price * multiplier);
/*      */     }
/*  383 */     for (AbstractCard c : this.colorlessCards) {
/*  384 */       c.price = MathUtils.round(c.price * multiplier);
/*      */     }
/*  386 */     if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
/*  387 */       actualPurgeCost = 50;
/*      */     }
/*  389 */     else if (affectPurge) {
/*  390 */       actualPurgeCost = MathUtils.round(purgeCost * multiplier);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void initRelics() {
/*  396 */     this.relics.clear();
/*  397 */     this.relics = new ArrayList<>();
/*      */     
/*  399 */     for (int i = 0; i < 3; i++) {
/*  400 */       AbstractRelic tempRelic = null;
/*      */ 
/*      */       
/*  403 */       if (i != 2) {
/*  404 */         tempRelic = AbstractDungeon.returnRandomRelicEnd(rollRelicTier());
/*      */       } else {
/*      */         
/*  407 */         tempRelic = AbstractDungeon.returnRandomRelicEnd(AbstractRelic.RelicTier.SHOP);
/*      */       } 
/*      */       
/*  410 */       StoreRelic relic = new StoreRelic(tempRelic, i, this);
/*  411 */       if (!Settings.isDailyRun) {
/*  412 */         relic.price = MathUtils.round(relic.price * AbstractDungeon.merchantRng
/*  413 */             .random(0.95F, 1.05F));
/*      */       }
/*  415 */       this.relics.add(relic);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initPotions() {
/*  420 */     this.potions.clear();
/*  421 */     this.potions = new ArrayList<>();
/*  422 */     for (int i = 0; i < 3; i++) {
/*  423 */       StorePotion potion = new StorePotion(AbstractDungeon.returnRandomPotion(), i, this);
/*  424 */       if (!Settings.isDailyRun) {
/*  425 */         potion.price = MathUtils.round(potion.price * AbstractDungeon.merchantRng
/*  426 */             .random(0.95F, 1.05F));
/*      */       }
/*      */ 
/*      */       
/*  430 */       this.potions.add(potion);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void getNewPrice(StoreRelic r) {
/*  436 */     int retVal = r.price;
/*      */ 
/*      */     
/*  439 */     if (!Settings.isDailyRun) {
/*  440 */       retVal = MathUtils.round(retVal * AbstractDungeon.merchantRng
/*  441 */           .random(0.95F, 1.05F));
/*      */     }
/*      */ 
/*      */     
/*  445 */     if (AbstractDungeon.player.hasRelic("The Courier")) {
/*  446 */       retVal = applyDiscountToRelic(retVal, 0.8F);
/*      */     }
/*  448 */     if (AbstractDungeon.player.hasRelic("Membership Card")) {
/*  449 */       retVal = applyDiscountToRelic(retVal, 0.5F);
/*      */     }
/*      */ 
/*      */     
/*  453 */     r.price = retVal;
/*      */   }
/*      */ 
/*      */   
/*      */   public void getNewPrice(StorePotion r) {
/*  458 */     int retVal = r.price;
/*      */ 
/*      */     
/*  461 */     if (!Settings.isDailyRun) {
/*  462 */       retVal = MathUtils.round(retVal * AbstractDungeon.merchantRng
/*  463 */           .random(0.95F, 1.05F));
/*      */     }
/*      */ 
/*      */     
/*  467 */     if (AbstractDungeon.player.hasRelic("The Courier")) {
/*  468 */       retVal = applyDiscountToRelic(retVal, 0.8F);
/*      */     }
/*  470 */     if (AbstractDungeon.player.hasRelic("Membership Card")) {
/*  471 */       retVal = applyDiscountToRelic(retVal, 0.5F);
/*      */     }
/*      */ 
/*      */     
/*  475 */     r.price = retVal;
/*      */   }
/*      */   
/*      */   private int applyDiscountToRelic(int price, float multiplier) {
/*  479 */     return MathUtils.round(price * multiplier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractRelic.RelicTier rollRelicTier() {
/*  488 */     int roll = AbstractDungeon.merchantRng.random(99);
/*  489 */     logger.info("ROLL " + roll);
/*      */ 
/*      */     
/*  492 */     if (roll < 48) {
/*  493 */       return AbstractRelic.RelicTier.COMMON;
/*      */     }
/*  495 */     if (roll < 82) {
/*  496 */       return AbstractRelic.RelicTier.UNCOMMON;
/*      */     }
/*      */     
/*  499 */     return AbstractRelic.RelicTier.RARE;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setStartingCardPositions() {
/*  504 */     int tmp = (int)(Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
/*      */     
/*  506 */     float padX = (int)(tmp + AbstractCard.IMG_WIDTH_S) + 10.0F * Settings.scale;
/*      */     int i;
/*  508 */     for (i = 0; i < this.coloredCards.size(); i++) {
/*  509 */       ((AbstractCard)this.coloredCards.get(i)).updateHoverLogic();
/*  510 */       ((AbstractCard)this.coloredCards.get(i)).targetDrawScale = 0.75F;
/*  511 */       ((AbstractCard)this.coloredCards.get(i)).current_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
/*  512 */       ((AbstractCard)this.coloredCards.get(i)).target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
/*  513 */       ((AbstractCard)this.coloredCards.get(i)).target_y = 9999.0F * Settings.scale;
/*  514 */       ((AbstractCard)this.coloredCards.get(i)).current_y = 9999.0F * Settings.scale;
/*      */     } 
/*      */     
/*  517 */     for (i = 0; i < this.colorlessCards.size(); i++) {
/*  518 */       ((AbstractCard)this.colorlessCards.get(i)).updateHoverLogic();
/*  519 */       ((AbstractCard)this.colorlessCards.get(i)).targetDrawScale = 0.75F;
/*  520 */       ((AbstractCard)this.colorlessCards.get(i)).current_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
/*  521 */       ((AbstractCard)this.colorlessCards.get(i)).target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i;
/*  522 */       ((AbstractCard)this.colorlessCards.get(i)).target_y = 9999.0F * Settings.scale;
/*  523 */       ((AbstractCard)this.colorlessCards.get(i)).current_y = 9999.0F * Settings.scale;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void open() {
/*  529 */     resetTouchscreenVars();
/*      */     
/*  531 */     CardCrawlGame.sound.play("SHOP_OPEN");
/*  532 */     setStartingCardPositions();
/*  533 */     this.purgeCardY = -1000.0F;
/*  534 */     AbstractDungeon.isScreenUp = true;
/*  535 */     AbstractDungeon.screen = AbstractDungeon.CurrentScreen.SHOP;
/*  536 */     AbstractDungeon.overlayMenu.proceedButton.hide();
/*  537 */     AbstractDungeon.overlayMenu.cancelButton.show(NAMES[12]);
/*  538 */     for (StoreRelic r : this.relics) {
/*  539 */       r.hide();
/*      */     }
/*  541 */     for (StorePotion p : this.potions) {
/*  542 */       p.hide();
/*      */     }
/*  544 */     this.rugY = Settings.HEIGHT;
/*  545 */     this.handX = Settings.WIDTH / 2.0F;
/*  546 */     this.handY = Settings.HEIGHT;
/*  547 */     this.handTargetX = this.handX;
/*  548 */     this.handTargetY = this.handY;
/*  549 */     this.handTimer = 1.0F;
/*  550 */     this.speechTimer = 1.5F;
/*  551 */     this.speechBubble = null;
/*  552 */     this.dialogTextEffect = null;
/*  553 */     AbstractDungeon.overlayMenu.showBlackScreen();
/*      */ 
/*      */     
/*  556 */     for (AbstractCard c : this.coloredCards) {
/*  557 */       UnlockTracker.markCardAsSeen(c.cardID);
/*      */     }
/*  559 */     for (AbstractCard c : this.colorlessCards) {
/*  560 */       UnlockTracker.markCardAsSeen(c.cardID);
/*      */     }
/*  562 */     for (StoreRelic r : this.relics) {
/*  563 */       if (r.relic != null) {
/*  564 */         UnlockTracker.markRelicAsSeen(r.relic.relicId);
/*      */       }
/*      */     } 
/*  567 */     if (ModHelper.isModEnabled("Hoarder")) {
/*  568 */       this.purgeAvailable = false;
/*      */     }
/*      */   }
/*      */   
/*      */   private void resetTouchscreenVars() {
/*  573 */     if (Settings.isTouchScreen) {
/*  574 */       this.confirmButton.hide();
/*  575 */       this.confirmButton.isDisabled = false;
/*  576 */       this.touchRelic = null;
/*  577 */       this.touchCard = null;
/*  578 */       this.touchPotion = null;
/*  579 */       this.touchPurge = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void update() {
/*  584 */     if (Settings.isTouchScreen) {
/*  585 */       this.confirmButton.update();
/*  586 */       if (InputHelper.justClickedLeft && 
/*  587 */         this.confirmButton.hb.hovered) {
/*  588 */         this.confirmButton.hb.clickStarted = true;
/*      */       }
/*      */ 
/*      */       
/*  592 */       if (InputHelper.justReleasedClickLeft && !this.confirmButton.hb.hovered) {
/*  593 */         resetTouchscreenVars();
/*  594 */       } else if (this.confirmButton.hb.clicked) {
/*  595 */         this.confirmButton.hb.clicked = false;
/*  596 */         if (this.touchRelic != null) {
/*  597 */           this.touchRelic.purchaseRelic();
/*  598 */         } else if (this.touchCard != null) {
/*  599 */           purchaseCard(this.touchCard);
/*  600 */         } else if (this.touchPotion != null) {
/*  601 */           this.touchPotion.purchasePotion();
/*  602 */         } else if (this.touchPurge) {
/*  603 */           purchasePurge();
/*      */         } 
/*  605 */         resetTouchscreenVars();
/*      */       } 
/*      */     } 
/*      */     
/*  609 */     if (this.handTimer != 0.0F) {
/*  610 */       this.handTimer -= Gdx.graphics.getDeltaTime();
/*  611 */       if (this.handTimer < 0.0F) {
/*  612 */         this.handTimer = 0.0F;
/*      */       }
/*      */     } 
/*      */     
/*  616 */     this.f_effect.update();
/*  617 */     this.somethingHovered = false;
/*      */     
/*  619 */     updateControllerInput();
/*  620 */     updatePurgeCard();
/*  621 */     updatePurge();
/*  622 */     updateRelics();
/*  623 */     updatePotions();
/*  624 */     updateRug();
/*  625 */     updateSpeech();
/*  626 */     updateCards();
/*      */ 
/*      */     
/*  629 */     updateHand();
/*      */     
/*  631 */     AbstractCard hoveredCard = null;
/*  632 */     for (AbstractCard c : this.coloredCards) {
/*  633 */       if (c.hb.hovered) {
/*  634 */         hoveredCard = c;
/*  635 */         this.somethingHovered = true;
/*  636 */         moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  641 */     for (AbstractCard c : this.colorlessCards) {
/*  642 */       if (c.hb.hovered) {
/*  643 */         hoveredCard = c;
/*  644 */         this.somethingHovered = true;
/*  645 */         moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  650 */     if (!this.somethingHovered) {
/*  651 */       this.notHoveredTimer += Gdx.graphics.getDeltaTime();
/*  652 */       if (this.notHoveredTimer > 1.0F) {
/*  653 */         this.handTargetY = Settings.HEIGHT;
/*      */       }
/*      */     } else {
/*  656 */       this.notHoveredTimer = 0.0F;
/*      */     } 
/*      */ 
/*      */     
/*  660 */     if (hoveredCard != null && InputHelper.justClickedLeft) {
/*  661 */       hoveredCard.hb.clickStarted = true;
/*      */     }
/*      */     
/*  664 */     if (hoveredCard != null && (InputHelper.justClickedRight || CInputActionSet.proceed.isJustPressed())) {
/*  665 */       InputHelper.justClickedRight = false;
/*  666 */       CardCrawlGame.cardPopup.open(hoveredCard);
/*      */     } 
/*      */     
/*  669 */     if (hoveredCard != null && (hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed())) {
/*  670 */       hoveredCard.hb.clicked = false;
/*  671 */       if (!Settings.isTouchScreen) {
/*  672 */         purchaseCard(hoveredCard);
/*  673 */       } else if (this.touchCard == null) {
/*  674 */         if (AbstractDungeon.player.gold < hoveredCard.price) {
/*  675 */           this.speechTimer = MathUtils.random(40.0F, 60.0F);
/*  676 */           playCantBuySfx();
/*  677 */           createSpeech(getCantBuyMsg());
/*      */         } else {
/*  679 */           this.confirmButton.hideInstantly();
/*  680 */           this.confirmButton.show();
/*  681 */           this.confirmButton.isDisabled = false;
/*  682 */           this.confirmButton.hb.clickStarted = false;
/*  683 */           this.touchCard = hoveredCard;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void purchaseCard(AbstractCard hoveredCard) {
/*  691 */     if (AbstractDungeon.player.gold >= hoveredCard.price) {
/*  692 */       CardCrawlGame.metricData.addShopPurchaseData(hoveredCard.getMetricID());
/*  693 */       AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
/*      */       
/*  695 */       AbstractDungeon.player.loseGold(hoveredCard.price);
/*  696 */       CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
/*      */       
/*  698 */       if (AbstractDungeon.player.hasRelic("The Courier")) {
/*  699 */         if (hoveredCard.color == AbstractCard.CardColor.COLORLESS) {
/*  700 */           AbstractCard.CardRarity tempRarity = AbstractCard.CardRarity.UNCOMMON;
/*  701 */           if (AbstractDungeon.merchantRng.random() < AbstractDungeon.colorlessRareChance) {
/*  702 */             tempRarity = AbstractCard.CardRarity.RARE;
/*      */           }
/*  704 */           AbstractCard c = AbstractDungeon.getColorlessCardFromPool(tempRarity).makeCopy();
/*  705 */           for (AbstractRelic r : AbstractDungeon.player.relics) {
/*  706 */             r.onPreviewObtainCard(c);
/*      */           }
/*  708 */           c.current_x = hoveredCard.current_x;
/*  709 */           c.current_y = hoveredCard.current_y;
/*  710 */           c.target_x = c.current_x;
/*  711 */           c.target_y = c.current_y;
/*  712 */           setPrice(c);
/*      */           
/*  714 */           this.colorlessCards.set(this.colorlessCards.indexOf(hoveredCard), c);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  719 */           AbstractCard c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy();
/*  720 */           while (c.color == AbstractCard.CardColor.COLORLESS)
/*      */           {
/*  722 */             c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy();
/*      */           }
/*  724 */           for (AbstractRelic r : AbstractDungeon.player.relics) {
/*  725 */             r.onPreviewObtainCard(c);
/*      */           }
/*  727 */           c.current_x = hoveredCard.current_x;
/*  728 */           c.current_y = hoveredCard.current_y;
/*  729 */           c.target_x = c.current_x;
/*  730 */           c.target_y = c.current_y;
/*  731 */           setPrice(c);
/*      */           
/*  733 */           this.coloredCards.set(this.coloredCards.indexOf(hoveredCard), c);
/*      */         } 
/*      */       } else {
/*  736 */         this.coloredCards.remove(hoveredCard);
/*  737 */         this.colorlessCards.remove(hoveredCard);
/*      */       } 
/*      */       
/*  740 */       hoveredCard = null;
/*  741 */       InputHelper.justClickedLeft = false;
/*  742 */       this.notHoveredTimer = 1.0F;
/*  743 */       this.speechTimer = MathUtils.random(40.0F, 60.0F);
/*  744 */       playBuySfx();
/*  745 */       createSpeech(getBuyMsg());
/*      */     } else {
/*  747 */       this.speechTimer = MathUtils.random(40.0F, 60.0F);
/*  748 */       playCantBuySfx();
/*  749 */       createSpeech(getCantBuyMsg());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateCards() {
/*      */     int i;
/*  755 */     for (i = 0; i < this.coloredCards.size(); i++) {
/*  756 */       ((AbstractCard)this.coloredCards.get(i)).update();
/*  757 */       ((AbstractCard)this.coloredCards.get(i)).updateHoverLogic();
/*  758 */       ((AbstractCard)this.coloredCards.get(i)).current_y = this.rugY + TOP_ROW_Y;
/*  759 */       ((AbstractCard)this.coloredCards.get(i)).target_y = ((AbstractCard)this.coloredCards.get(i)).current_y;
/*      */     } 
/*      */ 
/*      */     
/*  763 */     for (i = 0; i < this.colorlessCards.size(); i++) {
/*  764 */       ((AbstractCard)this.colorlessCards.get(i)).update();
/*  765 */       ((AbstractCard)this.colorlessCards.get(i)).updateHoverLogic();
/*  766 */       ((AbstractCard)this.colorlessCards.get(i)).current_y = this.rugY + BOTTOM_ROW_Y;
/*  767 */       ((AbstractCard)this.colorlessCards.get(i)).target_y = ((AbstractCard)this.colorlessCards.get(i)).current_y;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setPrice(AbstractCard card) {
/*  777 */     float tmpPrice = AbstractCard.getPrice(card.rarity) * AbstractDungeon.merchantRng.random(0.9F, 1.1F);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  782 */     if (card.color == AbstractCard.CardColor.COLORLESS) {
/*  783 */       tmpPrice *= 1.2F;
/*      */     }
/*      */ 
/*      */     
/*  787 */     if (AbstractDungeon.player.hasRelic("The Courier")) {
/*  788 */       tmpPrice *= 0.8F;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  793 */     if (AbstractDungeon.player.hasRelic("Membership Card")) {
/*  794 */       tmpPrice *= 0.5F;
/*      */     }
/*      */ 
/*      */     
/*  798 */     card.price = (int)tmpPrice;
/*      */   }
/*      */   
/*      */   public void moveHand(float x, float y) {
/*  802 */     this.handTargetX = x - 50.0F * Settings.xScale;
/*  803 */     this.handTargetY = y + 90.0F * Settings.yScale;
/*      */   }
/*      */   
/*      */   private enum StoreSelectionType {
/*  807 */     RELIC, COLOR_CARD, COLORLESS_CARD, POTION, PURGE;
/*      */   }
/*      */   
/*      */   private void updateControllerInput() {
/*  811 */     if (!Settings.isControllerMode || AbstractDungeon.topPanel.selectPotionMode || !AbstractDungeon.topPanel.potionUi.isHidden || AbstractDungeon.player.viewingRelics) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  816 */     StoreSelectionType type = null;
/*  817 */     int index = 0;
/*      */ 
/*      */     
/*  820 */     for (AbstractCard c : this.coloredCards) {
/*  821 */       if (c.hb.hovered) {
/*  822 */         type = StoreSelectionType.COLOR_CARD;
/*      */         break;
/*      */       } 
/*  825 */       index++;
/*      */     } 
/*      */ 
/*      */     
/*  829 */     if (type == null) {
/*  830 */       index = 0;
/*  831 */       for (StoreRelic r : this.relics) {
/*  832 */         if (r.relic.hb.hovered) {
/*  833 */           type = StoreSelectionType.RELIC;
/*      */           break;
/*      */         } 
/*  836 */         index++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  841 */     if (type == null) {
/*  842 */       index = 0;
/*  843 */       for (AbstractCard c : this.colorlessCards) {
/*  844 */         if (c.hb.hovered) {
/*  845 */           type = StoreSelectionType.COLORLESS_CARD;
/*      */           break;
/*      */         } 
/*  848 */         index++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  853 */     if (type == null) {
/*  854 */       index = 0;
/*  855 */       for (StorePotion p : this.potions) {
/*  856 */         if (p.potion.hb.hovered) {
/*  857 */           type = StoreSelectionType.POTION;
/*      */           break;
/*      */         } 
/*  860 */         index++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  865 */     if (type == null && 
/*  866 */       this.purgeHovered) {
/*  867 */       type = StoreSelectionType.PURGE;
/*      */     }
/*      */ 
/*      */     
/*  871 */     if (type == null) {
/*  872 */       if (!this.coloredCards.isEmpty()) {
/*  873 */         Gdx.input.setCursorPosition(
/*  874 */             (int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - 
/*  875 */             (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
/*  876 */       } else if (!this.colorlessCards.isEmpty()) {
/*  877 */         Gdx.input.setCursorPosition(
/*  878 */             (int)((AbstractCard)this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT - 
/*  879 */             (int)((AbstractCard)this.colorlessCards.get(0)).hb.cY);
/*  880 */       } else if (!this.relics.isEmpty()) {
/*  881 */         Gdx.input.setCursorPosition(
/*  882 */             (int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - 
/*  883 */             (int)((StoreRelic)this.relics.get(0)).relic.hb.cY);
/*  884 */       } else if (!this.potions.isEmpty()) {
/*  885 */         Gdx.input.setCursorPosition(
/*  886 */             (int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - 
/*  887 */             (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
/*  888 */       } else if (this.purgeAvailable) {
/*  889 */         Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
/*      */       } 
/*      */     } else {
/*  892 */       switch (type) {
/*      */         case COLOR_CARD:
/*  894 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/*  895 */             index--;
/*  896 */             if (index < 0) {
/*  897 */               index = 0;
/*      */             }
/*  899 */             Gdx.input.setCursorPosition(
/*  900 */                 (int)((AbstractCard)this.coloredCards.get(index)).hb.cX, Settings.HEIGHT - 
/*  901 */                 (int)((AbstractCard)this.coloredCards.get(index)).hb.cY); break;
/*  902 */           }  if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/*  903 */             index++;
/*  904 */             if (index > this.coloredCards.size() - 1) {
/*  905 */               index--;
/*      */             }
/*  907 */             Gdx.input.setCursorPosition(
/*  908 */                 (int)((AbstractCard)this.coloredCards.get(index)).hb.cX, Settings.HEIGHT - 
/*  909 */                 (int)((AbstractCard)this.coloredCards.get(index)).hb.cY);
/*      */             break;
/*      */           } 
/*  912 */           if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
/*  913 */             if (((AbstractCard)this.coloredCards.get(index)).hb.cX < 550.0F * Settings.scale && 
/*  914 */               !this.colorlessCards.isEmpty()) {
/*  915 */               Gdx.input.setCursorPosition(
/*  916 */                   (int)((AbstractCard)this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT - 
/*  917 */                   (int)((AbstractCard)this.colorlessCards.get(0)).hb.cY);
/*      */               
/*      */               return;
/*      */             } 
/*      */             
/*  922 */             if (((AbstractCard)this.coloredCards.get(index)).hb.cX < 850.0F * Settings.scale) {
/*  923 */               if (!this.colorlessCards.isEmpty()) {
/*  924 */                 if (this.colorlessCards.size() > 1) {
/*  925 */                   Gdx.input.setCursorPosition(
/*  926 */                       (int)((AbstractCard)this.colorlessCards.get(1)).hb.cX, Settings.HEIGHT - 
/*  927 */                       (int)((AbstractCard)this.colorlessCards.get(1)).hb.cY);
/*      */                 } else {
/*  929 */                   Gdx.input.setCursorPosition(
/*  930 */                       (int)((AbstractCard)this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT - 
/*  931 */                       (int)((AbstractCard)this.colorlessCards.get(0)).hb.cY);
/*      */                 }  return;
/*      */               } 
/*  934 */               if (!this.relics.isEmpty()) {
/*  935 */                 Gdx.input.setCursorPosition(
/*  936 */                     (int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - 
/*  937 */                     (int)((StoreRelic)this.relics.get(0)).relic.hb.cY); return;
/*      */               } 
/*  939 */               if (!this.potions.isEmpty()) {
/*  940 */                 Gdx.input.setCursorPosition(
/*  941 */                     (int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - 
/*  942 */                     (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
/*  943 */               } else if (this.purgeAvailable) {
/*  944 */                 Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*  949 */             if (((AbstractCard)this.coloredCards.get(index)).hb.cX < 1400.0F * Settings.scale) {
/*  950 */               if (!this.relics.isEmpty()) {
/*  951 */                 Gdx.input.setCursorPosition(
/*  952 */                     (int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - 
/*  953 */                     (int)((StoreRelic)this.relics.get(0)).relic.hb.cY); return;
/*      */               } 
/*  955 */               if (!this.potions.isEmpty()) {
/*  956 */                 Gdx.input.setCursorPosition(
/*  957 */                     (int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - 
/*  958 */                     (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
/*      */               }
/*      */             } 
/*      */             
/*  962 */             Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
/*      */           } 
/*      */           break;
/*      */         case COLORLESS_CARD:
/*  966 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/*  967 */             index--;
/*  968 */             if (index < 0) {
/*  969 */               index = 0;
/*      */             }
/*  971 */             Gdx.input.setCursorPosition(
/*  972 */                 (int)((AbstractCard)this.colorlessCards.get(index)).hb.cX, Settings.HEIGHT - 
/*  973 */                 (int)((AbstractCard)this.colorlessCards.get(index)).hb.cY); break;
/*  974 */           }  if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/*  975 */             index++;
/*  976 */             if (index > this.colorlessCards.size() - 1) {
/*  977 */               if (!this.relics.isEmpty()) {
/*  978 */                 Gdx.input.setCursorPosition(
/*  979 */                     (int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - 
/*  980 */                     (int)((StoreRelic)this.relics.get(0)).relic.hb.cY); break;
/*  981 */               }  if (!this.potions.isEmpty()) {
/*  982 */                 Gdx.input.setCursorPosition(
/*  983 */                     (int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - 
/*  984 */                     (int)((StorePotion)this.potions.get(0)).potion.hb.cY); break;
/*  985 */               }  if (this.purgeAvailable) {
/*  986 */                 Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY); break;
/*      */               } 
/*  988 */               index = 0;
/*      */               break;
/*      */             } 
/*  991 */             Gdx.input.setCursorPosition(
/*  992 */                 (int)((AbstractCard)this.colorlessCards.get(index)).hb.cX, Settings.HEIGHT - 
/*  993 */                 (int)((AbstractCard)this.colorlessCards.get(index)).hb.cY);
/*      */             
/*      */             break;
/*      */           } 
/*  997 */           if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && 
/*  998 */             !this.coloredCards.isEmpty()) {
/*  999 */             if (((AbstractCard)this.colorlessCards.get(index)).hb.cX < 550.0F * Settings.scale) {
/* 1000 */               Gdx.input.setCursorPosition(
/* 1001 */                   (int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - 
/* 1002 */                   (int)((AbstractCard)this.coloredCards.get(0)).hb.cY); break;
/*      */             } 
/* 1004 */             if (this.coloredCards.size() > 1) {
/* 1005 */               Gdx.input.setCursorPosition(
/* 1006 */                   (int)((AbstractCard)this.coloredCards.get(1)).hb.cX, Settings.HEIGHT - 
/* 1007 */                   (int)((AbstractCard)this.coloredCards.get(1)).hb.cY); break;
/*      */             } 
/* 1009 */             Gdx.input.setCursorPosition(
/* 1010 */                 (int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - 
/* 1011 */                 (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
/*      */           } 
/*      */           break;
/*      */ 
/*      */         
/*      */         case RELIC:
/* 1017 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/* 1018 */             index--;
/* 1019 */             if (index < 0 && !this.colorlessCards.isEmpty()) {
/* 1020 */               Gdx.input.setCursorPosition(
/* 1021 */                   (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT - 
/* 1022 */                   (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY); break;
/*      */             } 
/* 1024 */             if (index < 0) {
/* 1025 */               index = 0;
/*      */             }
/* 1027 */             Gdx.input.setCursorPosition(
/* 1028 */                 (int)((StoreRelic)this.relics.get(index)).relic.hb.cX, Settings.HEIGHT - 
/* 1029 */                 (int)((StoreRelic)this.relics.get(index)).relic.hb.cY); break;
/*      */           } 
/* 1031 */           if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/* 1032 */             index++;
/* 1033 */             if (index > this.relics.size() - 1 && this.purgeAvailable) {
/* 1034 */               Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY); break;
/*      */             } 
/* 1036 */             if (index <= this.relics.size() - 1)
/* 1037 */               Gdx.input.setCursorPosition(
/* 1038 */                   (int)((StoreRelic)this.relics.get(index)).relic.hb.cX, Settings.HEIGHT - 
/* 1039 */                   (int)((StoreRelic)this.relics.get(index)).relic.hb.cY); 
/*      */             break;
/*      */           } 
/* 1042 */           if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && 
/* 1043 */             !this.potions.isEmpty()) {
/* 1044 */             if (this.potions.size() - 1 >= index) {
/* 1045 */               Gdx.input.setCursorPosition(
/* 1046 */                   (int)((StorePotion)this.potions.get(index)).potion.hb.cX, Settings.HEIGHT - 
/* 1047 */                   (int)((StorePotion)this.potions.get(index)).potion.hb.cY); break;
/*      */             } 
/* 1049 */             Gdx.input.setCursorPosition(
/* 1050 */                 (int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - 
/* 1051 */                 (int)((StorePotion)this.potions.get(0)).potion.hb.cY); break;
/*      */           } 
/* 1053 */           if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && 
/* 1054 */             !this.coloredCards.isEmpty()) {
/* 1055 */             if (this.coloredCards.size() > 3) {
/* 1056 */               Gdx.input.setCursorPosition(
/* 1057 */                   (int)((AbstractCard)this.coloredCards.get(2)).hb.cX, Settings.HEIGHT - 
/* 1058 */                   (int)((AbstractCard)this.coloredCards.get(2)).hb.cY); break;
/*      */             } 
/* 1060 */             Gdx.input.setCursorPosition(
/* 1061 */                 (int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - 
/* 1062 */                 (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
/*      */           } 
/*      */           break;
/*      */         
/*      */         case POTION:
/* 1067 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/* 1068 */             index--;
/* 1069 */             if (index < 0 && !this.colorlessCards.isEmpty()) {
/* 1070 */               Gdx.input.setCursorPosition(
/* 1071 */                   (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT - 
/* 1072 */                   (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY); break;
/*      */             } 
/* 1074 */             if (index < 0) {
/* 1075 */               index = 0;
/*      */             }
/* 1077 */             Gdx.input.setCursorPosition(
/* 1078 */                 (int)((StorePotion)this.potions.get(index)).potion.hb.cX, Settings.HEIGHT - 
/* 1079 */                 (int)((StorePotion)this.potions.get(index)).potion.hb.cY); break;
/*      */           } 
/* 1081 */           if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/* 1082 */             index++;
/* 1083 */             if (index > this.potions.size() - 1 && this.purgeAvailable) {
/* 1084 */               Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY); break;
/*      */             } 
/* 1086 */             if (index <= this.potions.size() - 1)
/* 1087 */               Gdx.input.setCursorPosition(
/* 1088 */                   (int)((StorePotion)this.potions.get(index)).potion.hb.cX, Settings.HEIGHT - 
/* 1089 */                   (int)((StorePotion)this.potions.get(index)).potion.hb.cY); 
/*      */             break;
/*      */           } 
/* 1092 */           if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
/* 1093 */             if (!this.relics.isEmpty()) {
/* 1094 */               if (this.relics.size() - 1 >= index) {
/* 1095 */                 Gdx.input.setCursorPosition(
/* 1096 */                     (int)((StoreRelic)this.relics.get(index)).relic.hb.cX, Settings.HEIGHT - 
/* 1097 */                     (int)((StoreRelic)this.relics.get(index)).relic.hb.cY); break;
/*      */               } 
/* 1099 */               Gdx.input.setCursorPosition(
/* 1100 */                   (int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - 
/* 1101 */                   (int)((StoreRelic)this.relics.get(0)).relic.hb.cY); break;
/*      */             } 
/* 1103 */             if (!this.coloredCards.isEmpty()) {
/* 1104 */               if (this.coloredCards.size() > 3) {
/* 1105 */                 Gdx.input.setCursorPosition(
/* 1106 */                     (int)((AbstractCard)this.coloredCards.get(2)).hb.cX, Settings.HEIGHT - 
/* 1107 */                     (int)((AbstractCard)this.coloredCards.get(2)).hb.cY); break;
/*      */               } 
/* 1109 */               Gdx.input.setCursorPosition(
/* 1110 */                   (int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - 
/* 1111 */                   (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
/*      */             } 
/*      */           } 
/*      */           break;
/*      */         
/*      */         case PURGE:
/* 1117 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/* 1118 */             if (!this.relics.isEmpty()) {
/* 1119 */               Gdx.input.setCursorPosition(
/* 1120 */                   (int)((StoreRelic)this.relics.get(this.relics.size() - 1)).relic.hb.cX, Settings.HEIGHT - 
/* 1121 */                   (int)((StoreRelic)this.relics.get(this.relics.size() - 1)).relic.hb.cY); break;
/* 1122 */             }  if (!this.potions.isEmpty()) {
/* 1123 */               Gdx.input.setCursorPosition(
/* 1124 */                   (int)((StorePotion)this.potions.get(this.potions.size() - 1)).potion.hb.cX, Settings.HEIGHT - 
/* 1125 */                   (int)((StorePotion)this.potions.get(this.potions.size() - 1)).potion.hb.cY); break;
/* 1126 */             }  if (this.colorlessCards.isEmpty())
/* 1127 */               Gdx.input.setCursorPosition(
/* 1128 */                   (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT - 
/* 1129 */                   (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY);  break;
/*      */           } 
/* 1131 */           if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && 
/* 1132 */             !this.coloredCards.isEmpty()) {
/* 1133 */             Gdx.input.setCursorPosition(
/* 1134 */                 (int)((AbstractCard)this.coloredCards.get(this.coloredCards.size() - 1)).hb.cX, Settings.HEIGHT - 
/* 1135 */                 (int)((AbstractCard)this.coloredCards.get(this.coloredCards.size() - 1)).hb.cY);
/*      */           }
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updatePurgeCard() {
/* 1147 */     this.purgeCardX = 1554.0F * Settings.xScale;
/* 1148 */     this.purgeCardY = this.rugY + BOTTOM_ROW_Y;
/*      */     
/* 1150 */     if (this.purgeAvailable) {
/* 1151 */       float CARD_W = 110.0F * Settings.scale;
/* 1152 */       float CARD_H = 150.0F * Settings.scale;
/*      */       
/* 1154 */       if (InputHelper.mX > this.purgeCardX - CARD_W && InputHelper.mX < this.purgeCardX + CARD_W && InputHelper.mY > this.purgeCardY - CARD_H && InputHelper.mY < this.purgeCardY + CARD_H) {
/*      */         
/* 1156 */         this.purgeHovered = true;
/* 1157 */         moveHand(this.purgeCardX - AbstractCard.IMG_WIDTH / 2.0F, this.purgeCardY);
/* 1158 */         this.somethingHovered = true;
/* 1159 */         this.purgeCardScale = Settings.scale;
/*      */       } else {
/* 1161 */         this.purgeHovered = false;
/*      */       } 
/*      */       
/* 1164 */       if (!this.purgeHovered) {
/* 1165 */         this.purgeCardScale = MathHelper.cardScaleLerpSnap(this.purgeCardScale, 0.75F * Settings.scale);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1170 */         if (InputHelper.justReleasedClickLeft || CInputActionSet.select.isJustPressed()) {
/* 1171 */           if (!Settings.isTouchScreen) {
/* 1172 */             CInputActionSet.select.unpress();
/* 1173 */             purchasePurge();
/* 1174 */           } else if (!this.touchPurge) {
/* 1175 */             if (AbstractDungeon.player.gold < actualPurgeCost) {
/* 1176 */               playCantBuySfx();
/* 1177 */               createSpeech(getCantBuyMsg());
/*      */             } else {
/* 1179 */               this.confirmButton.hideInstantly();
/* 1180 */               this.confirmButton.show();
/* 1181 */               this.confirmButton.hb.clickStarted = false;
/* 1182 */               this.confirmButton.isDisabled = false;
/* 1183 */               this.touchPurge = true;
/*      */             } 
/*      */           } 
/*      */         }
/* 1187 */         TipHelper.renderGenericTip(InputHelper.mX - 360.0F * Settings.scale, InputHelper.mY - 70.0F * Settings.scale, LABEL[0], MSG[0] + '\031' + MSG[1]);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1194 */       this.purgeCardScale = MathHelper.cardScaleLerpSnap(this.purgeCardScale, 0.75F * Settings.scale);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void purchasePurge() {
/* 1199 */     this.purgeHovered = false;
/* 1200 */     if (AbstractDungeon.player.gold >= actualPurgeCost) {
/* 1201 */       AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
/*      */       
/* 1203 */       AbstractDungeon.gridSelectScreen.open(
/* 1204 */           CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, NAMES[13], false, false, true, true);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/* 1212 */       playCantBuySfx();
/* 1213 */       createSpeech(getCantBuyMsg());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateRelics() {
/* 1218 */     for (Iterator<StoreRelic> i = this.relics.iterator(); i.hasNext(); ) {
/* 1219 */       StoreRelic r = i.next();
/*      */       
/* 1221 */       if (Settings.isFourByThree) {
/* 1222 */         r.update(this.rugY + 50.0F * Settings.yScale);
/*      */       } else {
/* 1224 */         r.update(this.rugY);
/*      */       } 
/*      */       
/* 1227 */       if (r.isPurchased) {
/* 1228 */         i.remove();
/*      */         break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updatePotions() {
/* 1235 */     for (Iterator<StorePotion> i = this.potions.iterator(); i.hasNext(); ) {
/* 1236 */       StorePotion p = i.next();
/* 1237 */       if (Settings.isFourByThree) {
/* 1238 */         p.update(this.rugY + 50.0F * Settings.scale);
/*      */       } else {
/* 1240 */         p.update(this.rugY);
/*      */       } 
/* 1242 */       if (p.isPurchased) {
/* 1243 */         i.remove();
/*      */         break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void createSpeech(String msg) {
/* 1250 */     boolean isRight = MathUtils.randomBoolean();
/* 1251 */     float x = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
/* 1252 */     float y = Settings.HEIGHT - 380.0F * Settings.scale;
/* 1253 */     this.speechBubble = new ShopSpeechBubble(x, y, 4.0F, msg, isRight);
/* 1254 */     float offset_x = 0.0F;
/*      */     
/* 1256 */     if (isRight) {
/* 1257 */       offset_x = SPEECH_TEXT_R_X;
/*      */     } else {
/* 1259 */       offset_x = SPEECH_TEXT_L_X;
/*      */     } 
/*      */     
/* 1262 */     this.dialogTextEffect = new SpeechTextEffect(x + offset_x, y + SPEECH_TEXT_Y, 4.0F, msg, DialogWord.AppearEffect.BUMP_IN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateSpeech() {
/* 1271 */     if (this.speechBubble != null) {
/* 1272 */       this.speechBubble.update();
/* 1273 */       if (this.speechBubble.hb.hovered && this.speechBubble.duration > 0.3F) {
/* 1274 */         this.speechBubble.duration = 0.3F;
/* 1275 */         this.dialogTextEffect.duration = 0.3F;
/*      */       } 
/* 1277 */       if (this.speechBubble.isDone) {
/* 1278 */         this.speechBubble = null;
/*      */       }
/*      */     } 
/* 1281 */     if (this.dialogTextEffect != null) {
/* 1282 */       this.dialogTextEffect.update();
/* 1283 */       if (this.dialogTextEffect.isDone) {
/* 1284 */         this.dialogTextEffect = null;
/*      */       }
/*      */     } 
/*      */     
/* 1288 */     this.speechTimer -= Gdx.graphics.getDeltaTime();
/* 1289 */     if (this.speechBubble == null && this.dialogTextEffect == null && this.speechTimer <= 0.0F) {
/* 1290 */       this.speechTimer = MathUtils.random(40.0F, 60.0F);
/* 1291 */       if (!this.saidWelcome) {
/* 1292 */         createSpeech(WELCOME_MSG);
/* 1293 */         this.saidWelcome = true;
/* 1294 */         welcomeSfx();
/*      */       } else {
/* 1296 */         playMiscSfx();
/* 1297 */         createSpeech(getIdleMsg());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void welcomeSfx() {
/* 1303 */     int roll = MathUtils.random(2);
/* 1304 */     if (roll == 0) {
/* 1305 */       CardCrawlGame.sound.play("VO_MERCHANT_3A");
/* 1306 */     } else if (roll == 1) {
/* 1307 */       CardCrawlGame.sound.play("VO_MERCHANT_3B");
/*      */     } else {
/* 1309 */       CardCrawlGame.sound.play("VO_MERCHANT_3C");
/*      */     } 
/*      */   }
/*      */   
/*      */   private void playMiscSfx() {
/* 1314 */     int roll = MathUtils.random(5);
/* 1315 */     if (roll == 0) {
/* 1316 */       CardCrawlGame.sound.play("VO_MERCHANT_MA");
/* 1317 */     } else if (roll == 1) {
/* 1318 */       CardCrawlGame.sound.play("VO_MERCHANT_MB");
/* 1319 */     } else if (roll == 2) {
/* 1320 */       CardCrawlGame.sound.play("VO_MERCHANT_MC");
/* 1321 */     } else if (roll == 3) {
/* 1322 */       CardCrawlGame.sound.play("VO_MERCHANT_3A");
/* 1323 */     } else if (roll == 4) {
/* 1324 */       CardCrawlGame.sound.play("VO_MERCHANT_3B");
/*      */     } else {
/* 1326 */       CardCrawlGame.sound.play("VO_MERCHANT_3C");
/*      */     } 
/*      */   }
/*      */   
/*      */   public void playBuySfx() {
/* 1331 */     int roll = MathUtils.random(2);
/* 1332 */     if (roll == 0) {
/* 1333 */       CardCrawlGame.sound.play("VO_MERCHANT_KA");
/* 1334 */     } else if (roll == 1) {
/* 1335 */       CardCrawlGame.sound.play("VO_MERCHANT_KB");
/*      */     } else {
/* 1337 */       CardCrawlGame.sound.play("VO_MERCHANT_KC");
/*      */     } 
/*      */   }
/*      */   
/*      */   public void playCantBuySfx() {
/* 1342 */     int roll = MathUtils.random(2);
/* 1343 */     if (roll == 0) {
/* 1344 */       CardCrawlGame.sound.play("VO_MERCHANT_2A");
/* 1345 */     } else if (roll == 1) {
/* 1346 */       CardCrawlGame.sound.play("VO_MERCHANT_2B");
/*      */     } else {
/* 1348 */       CardCrawlGame.sound.play("VO_MERCHANT_2C");
/*      */     } 
/*      */   }
/*      */   
/*      */   private String getIdleMsg() {
/* 1353 */     return this.idleMessages.get(MathUtils.random(this.idleMessages.size() - 1));
/*      */   }
/*      */   
/*      */   private void updateRug() {
/* 1357 */     if (this.rugY != 0.0F) {
/* 1358 */       this.rugY = MathUtils.lerp(this.rugY, Settings.HEIGHT / 2.0F - 540.0F * Settings.yScale, Gdx.graphics
/*      */ 
/*      */           
/* 1361 */           .getDeltaTime() * 5.0F);
/* 1362 */       if (Math.abs(this.rugY - 0.0F) < 0.5F) {
/* 1363 */         this.rugY = 0.0F;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateHand() {
/* 1369 */     if (this.handTimer == 0.0F) {
/* 1370 */       if (this.handX != this.handTargetX) {
/* 1371 */         this.handX = MathUtils.lerp(this.handX, this.handTargetX, Gdx.graphics.getDeltaTime() * 6.0F);
/*      */       }
/* 1373 */       if (this.handY != this.handTargetY) {
/* 1374 */         if (this.handY > this.handTargetY) {
/* 1375 */           this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * 6.0F);
/*      */         } else {
/* 1377 */           this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * 6.0F / 4.0F);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void render(SpriteBatch sb) {
/* 1385 */     sb.setColor(Color.WHITE);
/* 1386 */     sb.draw(rugImg, 0.0F, this.rugY, Settings.WIDTH, Settings.HEIGHT);
/*      */     
/* 1388 */     renderCardsAndPrices(sb);
/* 1389 */     renderRelics(sb);
/* 1390 */     renderPotions(sb);
/* 1391 */     renderPurge(sb);
/*      */ 
/*      */     
/* 1394 */     sb.draw(handImg, this.handX + this.f_effect.x, this.handY + this.f_effect.y, HAND_W, HAND_H);
/*      */     
/* 1396 */     if (this.speechBubble != null) {
/* 1397 */       this.speechBubble.render(sb);
/*      */     }
/* 1399 */     if (this.dialogTextEffect != null) {
/* 1400 */       this.dialogTextEffect.render(sb);
/*      */     }
/*      */     
/* 1403 */     if (Settings.isTouchScreen) {
/* 1404 */       this.confirmButton.render(sb);
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderRelics(SpriteBatch sb) {
/* 1409 */     for (StoreRelic r : this.relics) {
/* 1410 */       r.render(sb);
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderPotions(SpriteBatch sb) {
/* 1415 */     for (StorePotion p : this.potions) {
/* 1416 */       p.render(sb);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderCardsAndPrices(SpriteBatch sb) {
/* 1427 */     for (AbstractCard c : this.coloredCards) {
/* 1428 */       c.render(sb);
/*      */ 
/*      */       
/* 1431 */       sb.setColor(Color.WHITE);
/* 1432 */       sb.draw(ImageMaster.UI_GOLD, c.current_x + GOLD_IMG_OFFSET_X, c.current_y + GOLD_IMG_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1440 */       Color color = Color.WHITE.cpy();
/* 1441 */       if (c.price > AbstractDungeon.player.gold) {
/* 1442 */         color = Color.SALMON.cpy();
/* 1443 */       } else if (c.equals(this.saleTag.card)) {
/* 1444 */         color = Color.SKY.cpy();
/*      */       } 
/*      */ 
/*      */       
/* 1448 */       FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, 
/*      */ 
/*      */           
/* 1451 */           Integer.toString(c.price), c.current_x + PRICE_TEXT_OFFSET_X, c.current_y + PRICE_TEXT_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, color);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1458 */     for (AbstractCard c : this.colorlessCards) {
/* 1459 */       c.render(sb);
/*      */ 
/*      */       
/* 1462 */       sb.setColor(Color.WHITE);
/* 1463 */       sb.draw(ImageMaster.UI_GOLD, c.current_x + GOLD_IMG_OFFSET_X, c.current_y + GOLD_IMG_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1471 */       Color color = Color.WHITE.cpy();
/* 1472 */       if (c.price > AbstractDungeon.player.gold) {
/* 1473 */         color = Color.SALMON.cpy();
/* 1474 */       } else if (c.equals(this.saleTag.card)) {
/* 1475 */         color = Color.SKY.cpy();
/*      */       } 
/*      */ 
/*      */       
/* 1479 */       FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, 
/*      */ 
/*      */           
/* 1482 */           Integer.toString(c.price), c.current_x + PRICE_TEXT_OFFSET_X, c.current_y + PRICE_TEXT_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, color);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1489 */     if (this.coloredCards.contains(this.saleTag.card)) {
/* 1490 */       this.saleTag.render(sb);
/*      */     }
/* 1492 */     if (this.colorlessCards.contains(this.saleTag.card)) {
/* 1493 */       this.saleTag.render(sb);
/*      */     }
/*      */ 
/*      */     
/* 1497 */     for (AbstractCard c : this.coloredCards) {
/* 1498 */       c.renderCardTip(sb);
/*      */     }
/*      */ 
/*      */     
/* 1502 */     for (AbstractCard c : this.colorlessCards) {
/* 1503 */       c.renderCardTip(sb);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderPurge(SpriteBatch sb) {
/* 1514 */     sb.setColor(Settings.QUARTER_TRANSPARENT_BLACK_COLOR);
/* 1515 */     TextureAtlas.AtlasRegion tmpImg = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
/* 1516 */     sb.draw((TextureRegion)tmpImg, this.purgeCardX + 18.0F * Settings.scale + tmpImg.offsetX - tmpImg.originalWidth / 2.0F, this.purgeCardY - 14.0F * Settings.scale + tmpImg.offsetY - tmpImg.originalHeight / 2.0F, tmpImg.originalWidth / 2.0F - tmpImg.offsetX, tmpImg.originalHeight / 2.0F - tmpImg.offsetY, tmpImg.packedWidth, tmpImg.packedHeight, this.purgeCardScale, this.purgeCardScale, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1528 */     sb.setColor(Color.WHITE);
/* 1529 */     if (this.purgeAvailable) {
/* 1530 */       sb.draw(removeServiceImg, this.purgeCardX - 256.0F, this.purgeCardY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.purgeCardScale, this.purgeCardScale, 0.0F, 0, 0, 512, 512, false, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1549 */       sb.draw(ImageMaster.UI_GOLD, this.purgeCardX + GOLD_IMG_OFFSET_X, this.purgeCardY + GOLD_IMG_OFFSET_Y - (this.purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1559 */       Color color = Color.WHITE;
/* 1560 */       if (actualPurgeCost > AbstractDungeon.player.gold) {
/* 1561 */         color = Color.SALMON;
/*      */       }
/*      */       
/* 1564 */       FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, 
/*      */ 
/*      */           
/* 1567 */           Integer.toString(actualPurgeCost), this.purgeCardX + PRICE_TEXT_OFFSET_X, this.purgeCardY + PRICE_TEXT_OFFSET_Y - (this.purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale, color);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1574 */       sb.draw(soldOutImg, this.purgeCardX - 256.0F, this.purgeCardY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.purgeCardScale, this.purgeCardScale, 0.0F, 0, 0, 512, 512, false, false);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\shop\ShopScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */