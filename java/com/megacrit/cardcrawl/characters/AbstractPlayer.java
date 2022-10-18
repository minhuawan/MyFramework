/*      */ package com.megacrit.cardcrawl.characters;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*      */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*      */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*      */ import com.badlogic.gdx.math.Bezier;
/*      */ import com.badlogic.gdx.math.Interpolation;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Vector;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*      */ import com.megacrit.cardcrawl.actions.GameActionManager;
/*      */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*      */ import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
/*      */ import com.megacrit.cardcrawl.actions.defect.ChannelAction;
/*      */ import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
/*      */ import com.megacrit.cardcrawl.actions.utility.UseCardAction;
/*      */ import com.megacrit.cardcrawl.actions.utility.WaitAction;
/*      */ import com.megacrit.cardcrawl.blights.AbstractBlight;
/*      */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*      */ import com.megacrit.cardcrawl.cards.CardGroup;
/*      */ import com.megacrit.cardcrawl.cards.CardQueueItem;
/*      */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*      */ import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
/*      */ import com.megacrit.cardcrawl.cards.blue.Zap;
/*      */ import com.megacrit.cardcrawl.cards.green.Defend_Green;
/*      */ import com.megacrit.cardcrawl.cards.green.Strike_Green;
/*      */ import com.megacrit.cardcrawl.cards.green.Survivor;
/*      */ import com.megacrit.cardcrawl.cards.purple.Defend_Watcher;
/*      */ import com.megacrit.cardcrawl.cards.purple.Eruption;
/*      */ import com.megacrit.cardcrawl.cards.red.Bash;
/*      */ import com.megacrit.cardcrawl.cards.red.Defend_Red;
/*      */ import com.megacrit.cardcrawl.cards.red.Strike_Red;
/*      */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*      */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*      */ import com.megacrit.cardcrawl.core.EnergyManager;
/*      */ import com.megacrit.cardcrawl.core.GameCursor;
/*      */ import com.megacrit.cardcrawl.core.Settings;
/*      */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*      */ import com.megacrit.cardcrawl.helpers.CardLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.FontHelper;
/*      */ import com.megacrit.cardcrawl.helpers.Hitbox;
/*      */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*      */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*      */ import com.megacrit.cardcrawl.helpers.ModHelper;
/*      */ import com.megacrit.cardcrawl.helpers.PowerTip;
/*      */ import com.megacrit.cardcrawl.helpers.Prefs;
/*      */ import com.megacrit.cardcrawl.helpers.RelicLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.TipHelper;
/*      */ import com.megacrit.cardcrawl.helpers.TipTracker;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*      */ import com.megacrit.cardcrawl.localization.CharacterStrings;
/*      */ import com.megacrit.cardcrawl.localization.TutorialStrings;
/*      */ import com.megacrit.cardcrawl.localization.UIStrings;
/*      */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*      */ import com.megacrit.cardcrawl.orbs.AbstractOrb;
/*      */ import com.megacrit.cardcrawl.orbs.Dark;
/*      */ import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
/*      */ import com.megacrit.cardcrawl.potions.AbstractPotion;
/*      */ import com.megacrit.cardcrawl.potions.PotionSlot;
/*      */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*      */ import com.megacrit.cardcrawl.powers.PlatedArmorPower;
/*      */ import com.megacrit.cardcrawl.powers.StrengthPower;
/*      */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*      */ import com.megacrit.cardcrawl.relics.BottledFlame;
/*      */ import com.megacrit.cardcrawl.relics.BottledLightning;
/*      */ import com.megacrit.cardcrawl.relics.BottledTornado;
/*      */ import com.megacrit.cardcrawl.relics.LizardTail;
/*      */ import com.megacrit.cardcrawl.relics.SlaversCollar;
/*      */ import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*      */ import com.megacrit.cardcrawl.rooms.CampfireUI;
/*      */ import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
/*      */ import com.megacrit.cardcrawl.screens.CharSelectInfo;
/*      */ import com.megacrit.cardcrawl.screens.DeathScreen;
/*      */ import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
/*      */ import com.megacrit.cardcrawl.screens.stats.CharStat;
/*      */ import com.megacrit.cardcrawl.stances.AbstractStance;
/*      */ import com.megacrit.cardcrawl.stances.NeutralStance;
/*      */ import com.megacrit.cardcrawl.ui.FtueTip;
/*      */ import com.megacrit.cardcrawl.ui.MultiPageFtue;
/*      */ import com.megacrit.cardcrawl.ui.buttons.PeekButton;
/*      */ import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
/*      */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*      */ import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
/*      */ import com.megacrit.cardcrawl.vfx.ThoughtBubble;
/*      */ import com.megacrit.cardcrawl.vfx.cardManip.CardDisappearEffect;
/*      */ import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
/*      */ import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
/*      */ import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Random;
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
/*      */ public abstract class AbstractPlayer
/*      */   extends AbstractCreature
/*      */ {
/*  142 */   private static final Logger logger = LogManager.getLogger(AbstractPlayer.class.getName());
/*  143 */   private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Player Tips");
/*  144 */   public static final String[] MSG = tutorialStrings.TEXT;
/*  145 */   public static final String[] LABEL = tutorialStrings.LABEL;
/*      */   
/*      */   public PlayerClass chosenClass;
/*      */   
/*      */   public int gameHandSize;
/*      */   public int masterHandSize;
/*      */   public int startingMaxHP;
/*  152 */   public CardGroup masterDeck = new CardGroup(CardGroup.CardGroupType.MASTER_DECK); public CardGroup drawPile = new CardGroup(CardGroup.CardGroupType.DRAW_PILE); public CardGroup hand = new CardGroup(CardGroup.CardGroupType.HAND); public CardGroup discardPile = new CardGroup(CardGroup.CardGroupType.DISCARD_PILE); public CardGroup exhaustPile = new CardGroup(CardGroup.CardGroupType.EXHAUST_PILE); public CardGroup limbo = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*      */ 
/*      */ 
/*      */   
/*  156 */   public ArrayList<AbstractRelic> relics = new ArrayList<>();
/*  157 */   public ArrayList<AbstractBlight> blights = new ArrayList<>();
/*  158 */   public int potionSlots = 3;
/*  159 */   public ArrayList<AbstractPotion> potions = new ArrayList<>();
/*      */   public EnergyManager energy;
/*      */   public boolean isEndingTurn = false;
/*  162 */   public Hitbox inspectHb = null; public boolean viewingRelics = false; public boolean inspectMode = false;
/*  163 */   public static int poisonKillCount = 0;
/*  164 */   public int damagedThisCombat = 0;
/*      */   
/*      */   public String title;
/*      */   
/*  168 */   public ArrayList<AbstractOrb> orbs = new ArrayList<>();
/*      */   
/*      */   public int masterMaxOrbs;
/*      */   public int maxOrbs;
/*  172 */   public AbstractStance stance = (AbstractStance)new NeutralStance();
/*      */   
/*      */   @Deprecated
/*  175 */   public int cardsPlayedThisTurn = 0;
/*      */ 
/*      */   
/*      */   private boolean isHoveringCard = false;
/*      */ 
/*      */   
/*      */   public boolean isHoveringDropZone = false;
/*      */   
/*  183 */   private float hoverStartLine = 0.0F;
/*      */   private boolean passedHesitationLine = false;
/*  185 */   public AbstractCard hoveredCard = null; public AbstractCard toHover = null; public AbstractCard cardInUse = null;
/*      */   public boolean isDraggingCard = false;
/*      */   private boolean isUsingClickDragControl = false;
/*  188 */   private float clickDragTimer = 0.0F;
/*      */   public boolean inSingleTargetMode = false;
/*  190 */   private AbstractMonster hoveredMonster = null;
/*  191 */   public float hoverEnemyWaitTimer = 0.0F;
/*      */   
/*      */   private static final float HOVER_ENEMY_WAIT_TIME = 1.0F;
/*      */   
/*      */   public boolean isInKeyboardMode = false;
/*      */   
/*      */   private boolean skipMouseModeOnce = false;
/*      */   
/*  199 */   private int keyboardCardIndex = -1;
/*  200 */   public static ArrayList<String> customMods = null;
/*      */ 
/*      */   
/*  203 */   private int touchscreenInspectCount = 0;
/*      */   
/*      */   public Texture img;
/*      */   public Texture shoulderImg;
/*      */   public Texture shoulder2Img;
/*      */   public Texture corpseImg;
/*  209 */   private static final Color ARROW_COLOR = new Color(1.0F, 0.2F, 0.3F, 1.0F); private float arrowScale;
/*  210 */   private float arrowScaleTimer = 0.0F; private float arrowX; private float arrowY;
/*      */   private static final float ARROW_TARGET_SCALE = 1.2F;
/*      */   private static final int TARGET_ARROW_W = 256;
/*  213 */   public static final float HOVER_CARD_Y_POSITION = 210.0F * Settings.scale;
/*      */   public boolean endTurnQueued = false;
/*      */   private static final int SEGMENTS = 20;
/*  216 */   private Vector2[] points = new Vector2[20];
/*  217 */   private Vector2 controlPoint = new Vector2(); private Vector2 arrowTmp = new Vector2(); private Vector2 startArrowVector = new Vector2(); private Vector2 endArrowVector = new Vector2();
/*      */ 
/*      */   
/*      */   private boolean renderCorpse = false;
/*      */   
/*  222 */   public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AbstractPlayer");
/*      */   
/*      */   public AbstractPlayer(String name, PlayerClass setClass) {
/*  225 */     this.name = name;
/*  226 */     this.title = getTitle(setClass);
/*  227 */     this.drawX = Settings.WIDTH * 0.25F;
/*  228 */     this.drawY = AbstractDungeon.floorY;
/*  229 */     this.chosenClass = setClass;
/*  230 */     this.isPlayer = true;
/*  231 */     initializeStarterRelics(setClass);
/*  232 */     loadPrefs();
/*      */     
/*  234 */     if (AbstractDungeon.ascensionLevel >= 11) {
/*  235 */       this.potionSlots--;
/*      */     }
/*      */     
/*  238 */     this.potions.clear(); int i;
/*  239 */     for (i = 0; i < this.potionSlots; i++) {
/*  240 */       this.potions.add(new PotionSlot(i));
/*      */     }
/*      */     
/*  243 */     for (i = 0; i < this.points.length; i++) {
/*  244 */       this.points[i] = new Vector2();
/*      */     }
/*      */   }
/*      */   
/*      */   public abstract String getPortraitImageName();
/*      */   
/*      */   public abstract ArrayList<String> getStartingDeck();
/*      */   
/*      */   public abstract ArrayList<String> getStartingRelics();
/*      */   
/*      */   public abstract CharSelectInfo getLoadout();
/*      */   
/*      */   public abstract String getTitle(PlayerClass paramPlayerClass);
/*      */   
/*      */   public abstract AbstractCard.CardColor getCardColor();
/*      */   
/*      */   public abstract Color getCardRenderColor();
/*      */   
/*      */   public abstract String getAchievementKey();
/*      */   
/*      */   public abstract ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> paramArrayList);
/*      */   
/*      */   public abstract AbstractCard getStartCardForEvent();
/*      */   
/*      */   public abstract Color getCardTrailColor();
/*      */   
/*      */   public abstract String getLeaderboardCharacterName();
/*      */   
/*      */   public abstract Texture getEnergyImage();
/*      */   
/*      */   public abstract int getAscensionMaxHPLoss();
/*      */   
/*      */   public abstract BitmapFont getEnergyNumFont();
/*      */   
/*      */   public abstract void renderOrb(SpriteBatch paramSpriteBatch, boolean paramBoolean, float paramFloat1, float paramFloat2);
/*      */   
/*      */   public abstract void updateOrb(int paramInt);
/*      */   
/*      */   public abstract Prefs getPrefs();
/*      */   
/*      */   public abstract void loadPrefs();
/*      */   
/*      */   public abstract CharStat getCharStat();
/*      */   
/*      */   public abstract int getUnlockedCardCount();
/*      */   
/*      */   public abstract int getSeenCardCount();
/*      */   
/*      */   public abstract int getCardCount();
/*      */   
/*      */   public abstract boolean saveFileExists();
/*      */   
/*      */   public abstract String getWinStreakKey();
/*      */   
/*      */   public abstract String getLeaderboardWinStreakKey();
/*      */   
/*      */   public abstract void renderStatScreen(SpriteBatch paramSpriteBatch, float paramFloat1, float paramFloat2);
/*      */   
/*      */   public abstract void doCharSelectScreenSelectEffect();
/*      */   
/*      */   public abstract String getCustomModeCharacterButtonSoundKey();
/*      */   
/*      */   public abstract Texture getCustomModeCharacterButtonImage();
/*      */   
/*      */   public abstract CharacterStrings getCharacterString();
/*      */   
/*      */   public abstract String getLocalizedCharacterName();
/*      */   
/*      */   public abstract void refreshCharStat();
/*      */   
/*      */   public abstract AbstractPlayer newInstance();
/*      */   
/*      */   public abstract TextureAtlas.AtlasRegion getOrb();
/*      */   
/*      */   public abstract String getSpireHeartText();
/*      */   
/*      */   public abstract Color getSlashAttackColor();
/*      */   
/*      */   public abstract AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect();
/*      */   
/*      */   public abstract String getVampireText();
/*      */   
/*      */   public String getSaveFilePath() {
/*  327 */     return SaveAndContinue.getPlayerSavePath(this.chosenClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dispose() {
/*  334 */     if (this.atlas != null) {
/*  335 */       this.atlas.dispose();
/*      */     }
/*  337 */     if (this.img != null) {
/*  338 */       this.img.dispose();
/*      */     }
/*  340 */     if (this.shoulderImg != null) {
/*  341 */       this.shoulderImg.dispose();
/*      */     }
/*  343 */     if (this.shoulder2Img != null) {
/*  344 */       this.shoulder2Img.dispose();
/*      */     }
/*  346 */     if (this.corpseImg != null) {
/*  347 */       this.corpseImg.dispose();
/*      */     }
/*      */   }
/*      */   
/*      */   public void adjustPotionPositions() {
/*  352 */     for (int i = 0; i < this.potions.size() - 1; i++) {
/*  353 */       ((AbstractPotion)this.potions.get(i)).adjustPosition(i);
/*      */     }
/*      */   }
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
/*      */   protected void initializeClass(String imgUrl, String shoulder2ImgUrl, String shouldImgUrl, String corpseImgUrl, CharSelectInfo info, float hb_x, float hb_y, float hb_w, float hb_h, EnergyManager energy) {
/*  369 */     if (imgUrl != null) {
/*  370 */       this.img = ImageMaster.loadImage(imgUrl);
/*      */     }
/*      */     
/*  373 */     if (this.img != null) {
/*  374 */       this.atlas = null;
/*      */     }
/*      */     
/*  377 */     this.shoulderImg = ImageMaster.loadImage(shouldImgUrl);
/*  378 */     this.shoulder2Img = ImageMaster.loadImage(shoulder2ImgUrl);
/*  379 */     this.corpseImg = ImageMaster.loadImage(corpseImgUrl);
/*      */     
/*  381 */     if (Settings.isMobile) {
/*  382 */       hb_w *= 1.17F;
/*      */     }
/*      */     
/*  385 */     this.maxHealth = info.maxHp;
/*  386 */     this.startingMaxHP = this.maxHealth;
/*  387 */     this.currentHealth = info.currentHp;
/*  388 */     this.masterMaxOrbs = info.maxOrbs;
/*  389 */     this.energy = energy;
/*  390 */     this.masterHandSize = info.cardDraw;
/*  391 */     this.gameHandSize = this.masterHandSize;
/*  392 */     this.gold = info.gold;
/*  393 */     this.displayGold = this.gold;
/*  394 */     this.hb_h = hb_h * Settings.xScale;
/*  395 */     this.hb_w = hb_w * Settings.scale;
/*  396 */     this.hb_x = hb_x * Settings.scale;
/*  397 */     this.hb_y = hb_y * Settings.scale;
/*  398 */     this.hb = new Hitbox(this.hb_w, this.hb_h);
/*  399 */     this.healthHb = new Hitbox(this.hb.width, 72.0F * Settings.scale);
/*  400 */     refreshHitboxLocation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initializeStarterDeck() {
/*  410 */     ArrayList<String> cards = getStartingDeck();
/*      */ 
/*      */     
/*  413 */     boolean addBaseCards = true;
/*      */     
/*  415 */     if (ModHelper.isModEnabled("Draft") || ModHelper.isModEnabled("Chimera") || ModHelper.isModEnabled("SealedDeck") || 
/*  416 */       ModHelper.isModEnabled("Shiny") || ModHelper.isModEnabled("Insanity"))
/*      */     {
/*  418 */       addBaseCards = false;
/*      */     }
/*      */     
/*  421 */     if (ModHelper.isModEnabled("Chimera")) {
/*  422 */       this.masterDeck.addToTop((AbstractCard)new Bash());
/*  423 */       this.masterDeck.addToTop((AbstractCard)new Survivor());
/*  424 */       this.masterDeck.addToTop((AbstractCard)new Zap());
/*  425 */       this.masterDeck.addToTop((AbstractCard)new Eruption());
/*  426 */       this.masterDeck.addToTop((AbstractCard)new Strike_Red());
/*  427 */       this.masterDeck.addToTop((AbstractCard)new Strike_Green());
/*  428 */       this.masterDeck.addToTop((AbstractCard)new Strike_Blue());
/*  429 */       this.masterDeck.addToTop((AbstractCard)new Defend_Red());
/*  430 */       this.masterDeck.addToTop((AbstractCard)new Defend_Green());
/*  431 */       this.masterDeck.addToTop((AbstractCard)new Defend_Watcher());
/*      */     } 
/*      */     
/*  434 */     if (ModHelper.isModEnabled("Insanity")) {
/*  435 */       for (int i = 0; i < 50; i++) {
/*  436 */         this.masterDeck.addToTop(AbstractDungeon.returnRandomCard().makeCopy());
/*      */       }
/*      */     }
/*      */     
/*  440 */     if (ModHelper.isModEnabled("Shiny")) {
/*  441 */       CardGroup group = AbstractDungeon.getEachRare();
/*  442 */       for (AbstractCard c : group.group) {
/*  443 */         this.masterDeck.addToTop(c);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  448 */     if (addBaseCards) {
/*  449 */       for (String s : cards) {
/*  450 */         this.masterDeck.addToTop(CardLibrary.getCard(this.chosenClass, s).makeCopy());
/*      */       }
/*      */     }
/*      */     
/*  454 */     for (AbstractCard c : this.masterDeck.group) {
/*  455 */       UnlockTracker.markCardAsSeen(c.cardID);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initializeStarterRelics(PlayerClass chosenClass) {
/*  464 */     ArrayList<String> relics = getStartingRelics();
/*      */     
/*  466 */     if (ModHelper.isModEnabled("Cursed Run")) {
/*  467 */       relics.clear();
/*  468 */       relics.add("Cursed Key");
/*  469 */       relics.add("Darkstone Periapt");
/*  470 */       relics.add("Du-Vu Doll");
/*      */     } 
/*      */     
/*  473 */     if (ModHelper.isModEnabled("ControlledChaos")) {
/*  474 */       relics.add("Frozen Eye");
/*      */     }
/*      */     
/*  477 */     int index = 0;
/*  478 */     for (String s : relics) {
/*  479 */       RelicLibrary.getRelic(s).makeCopy().instantObtain(this, index, false);
/*  480 */       index++;
/*      */     } 
/*      */     
/*  483 */     AbstractDungeon.relicsToRemoveOnStart.addAll(relics);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void combatUpdate() {
/*  490 */     if (this.cardInUse != null) {
/*  491 */       this.cardInUse.update();
/*      */     }
/*      */     
/*  494 */     this.limbo.update();
/*  495 */     this.exhaustPile.update();
/*      */     
/*  497 */     for (AbstractPower p : this.powers) {
/*  498 */       p.updateParticles();
/*      */     }
/*      */     
/*  501 */     for (AbstractOrb o : this.orbs) {
/*  502 */       o.update();
/*      */     }
/*      */     
/*  505 */     this.stance.update();
/*      */   }
/*      */   
/*      */   public void update() {
/*  509 */     updateControllerInput();
/*  510 */     this.hb.update();
/*  511 */     updateHealthBar();
/*  512 */     updatePowers();
/*  513 */     this.healthHb.update();
/*  514 */     updateReticle();
/*  515 */     this.tint.update();
/*      */     
/*  517 */     if ((AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.EVENT) {
/*  518 */       for (AbstractOrb o : this.orbs) {
/*  519 */         o.updateAnimation();
/*      */       }
/*      */     }
/*      */     
/*  523 */     updateEscapeAnimation();
/*      */   }
/*      */   
/*      */   private void updateControllerInput() {
/*  527 */     if (!Settings.isControllerMode || AbstractDungeon.topPanel.selectPotionMode || !AbstractDungeon.topPanel.potionUi.isHidden) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  532 */     boolean anyHovered = false;
/*  533 */     boolean orbHovered = false;
/*  534 */     int orbIndex = 0;
/*      */     
/*  536 */     for (AbstractOrb o : this.orbs) {
/*  537 */       if (o.hb.hovered) {
/*  538 */         orbHovered = true;
/*      */         break;
/*      */       } 
/*  541 */       orbIndex++;
/*      */     } 
/*      */     
/*  544 */     if (orbHovered && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed())) {
/*  545 */       CInputActionSet.up.unpress();
/*  546 */       CInputActionSet.altUp.unpress();
/*      */       
/*  548 */       this.inspectMode = false;
/*  549 */       this.inspectHb = null;
/*  550 */       orbHovered = false;
/*  551 */       this.viewingRelics = true;
/*  552 */       if (!this.blights.isEmpty()) {
/*  553 */         CInputHelper.setCursor(((AbstractBlight)this.blights.get(0)).hb);
/*      */       } else {
/*  555 */         CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*      */       } 
/*  557 */     } else if (orbHovered && (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed())) {
/*  558 */       orbIndex++;
/*  559 */       if (orbIndex > this.orbs.size() - 1) {
/*  560 */         orbIndex = 0;
/*      */       }
/*  562 */       this.inspectHb = ((AbstractOrb)this.orbs.get(orbIndex)).hb;
/*  563 */       Gdx.input.setCursorPosition(
/*  564 */           (int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cX, Settings.HEIGHT - 
/*  565 */           (int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cY);
/*  566 */     } else if (orbHovered && (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed())) {
/*  567 */       orbIndex--;
/*  568 */       if (orbIndex < 0) {
/*  569 */         orbIndex = this.orbs.size() - 1;
/*      */       }
/*  571 */       this.inspectHb = ((AbstractOrb)this.orbs.get(orbIndex)).hb;
/*  572 */       Gdx.input.setCursorPosition(
/*  573 */           (int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cX, Settings.HEIGHT - 
/*  574 */           (int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cY);
/*  575 */     } else if (this.inspectMode && (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed())) {
/*  576 */       if (orbHovered) {
/*  577 */         this.inspectHb = this.hb;
/*  578 */         CInputHelper.setCursor(this.inspectHb);
/*      */       } else {
/*  580 */         this.inspectMode = false;
/*  581 */         this.inspectHb = null;
/*  582 */         if (!this.hand.isEmpty() && !AbstractDungeon.actionManager.turnHasEnded) {
/*  583 */           this.hoveredCard = this.hand.group.get(0);
/*  584 */           hoverCardInHand(this.hoveredCard);
/*  585 */           this.keyboardCardIndex = 0;
/*      */         } 
/*      */       } 
/*  588 */     } else if (!this.inspectMode && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && 
/*  589 */       (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
/*      */       
/*  591 */       if (Gdx.input.getX() < Settings.WIDTH / 2.0F) {
/*  592 */         this.inspectHb = this.hb;
/*      */       }
/*  594 */       else if (!(AbstractDungeon.getMonsters()).monsters.isEmpty()) {
/*  595 */         ArrayList<Hitbox> hbs = new ArrayList<>();
/*  596 */         for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
/*  597 */           if (!m.isDying && !m.isEscaping) {
/*  598 */             hbs.add(m.hb);
/*      */           }
/*      */         } 
/*  601 */         if (!hbs.isEmpty()) {
/*  602 */           this.inspectHb = hbs.get(0);
/*      */         } else {
/*  604 */           this.inspectHb = this.hb;
/*      */         } 
/*      */       } else {
/*  607 */         this.inspectHb = this.hb;
/*      */       } 
/*      */ 
/*      */       
/*  611 */       CInputHelper.setCursor(this.inspectHb);
/*  612 */       this.inspectMode = true;
/*  613 */       releaseCard();
/*  614 */     } else if (this.inspectMode && (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) && 
/*  615 */       (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/*  616 */       ArrayList<Hitbox> hbs = new ArrayList<>();
/*      */       
/*  618 */       hbs.add(this.hb);
/*  619 */       for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
/*  620 */         if (!m.isDying && !m.isEscaping) {
/*  621 */           hbs.add(m.hb);
/*      */         }
/*      */       } 
/*      */       
/*  625 */       int index = 0;
/*  626 */       for (Hitbox h : hbs) {
/*  627 */         h.update();
/*  628 */         if (h.hovered) {
/*  629 */           anyHovered = true;
/*      */           break;
/*      */         } 
/*  632 */         index++;
/*      */       } 
/*      */       
/*  635 */       if (!anyHovered) {
/*  636 */         CInputHelper.setCursor(hbs.get(0));
/*  637 */         this.inspectHb = hbs.get(0);
/*      */       } else {
/*  639 */         index++;
/*  640 */         if (index > hbs.size() - 1) {
/*  641 */           index = 0;
/*      */         }
/*  643 */         CInputHelper.setCursor(hbs.get(index));
/*  644 */         this.inspectHb = hbs.get(index);
/*      */       } 
/*      */       
/*  647 */       this.inspectMode = true;
/*  648 */       releaseCard();
/*  649 */     } else if (this.inspectMode && (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) && 
/*  650 */       (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/*  651 */       ArrayList<Hitbox> hbs = new ArrayList<>();
/*      */       
/*  653 */       hbs.add(this.hb);
/*  654 */       for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
/*  655 */         if (!m.isDying && !m.isEscaping) {
/*  656 */           hbs.add(m.hb);
/*      */         }
/*      */       } 
/*      */       
/*  660 */       int index = 0;
/*  661 */       for (Hitbox h : hbs) {
/*  662 */         if (h.hovered) {
/*  663 */           anyHovered = true;
/*      */           break;
/*      */         } 
/*  666 */         index++;
/*      */       } 
/*      */       
/*  669 */       if (!anyHovered) {
/*  670 */         CInputHelper.setCursor(hbs.get(hbs.size() - 1));
/*  671 */         this.inspectHb = hbs.get(hbs.size() - 1);
/*      */       } else {
/*  673 */         index--;
/*  674 */         if (index < 0) {
/*  675 */           index = hbs.size() - 1;
/*      */         }
/*  677 */         CInputHelper.setCursor(hbs.get(index));
/*  678 */         this.inspectHb = hbs.get(index);
/*      */       } 
/*      */       
/*  681 */       this.inspectMode = true;
/*  682 */       releaseCard();
/*  683 */     } else if (this.inspectMode && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && 
/*  684 */       (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/*      */       
/*  686 */       CInputActionSet.up.unpress();
/*  687 */       CInputActionSet.altUp.unpress();
/*      */       
/*  689 */       if (!orbHovered && !this.orbs.isEmpty()) {
/*  690 */         CInputHelper.setCursor(((AbstractOrb)this.orbs.get(0)).hb);
/*  691 */         this.inspectHb = ((AbstractOrb)this.orbs.get(0)).hb;
/*      */       } else {
/*  693 */         this.inspectMode = false;
/*  694 */         this.inspectHb = null;
/*  695 */         this.viewingRelics = true;
/*      */         
/*  697 */         if (!this.blights.isEmpty()) {
/*  698 */           CInputHelper.setCursor(((AbstractBlight)this.blights.get(0)).hb);
/*      */         } else {
/*  700 */           CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private enum RHoverType {
/*  707 */     RELIC, BLIGHT;
/*      */   }
/*      */   
/*      */   public void updateViewRelicControls() {
/*  711 */     int index = 0;
/*  712 */     boolean anyHovered = false;
/*  713 */     RHoverType type = RHoverType.RELIC;
/*      */     
/*  715 */     for (AbstractRelic r : this.relics) {
/*  716 */       if (r.hb.hovered) {
/*  717 */         anyHovered = true;
/*      */         break;
/*      */       } 
/*  720 */       index++;
/*      */     } 
/*      */     
/*  723 */     if (!anyHovered) {
/*  724 */       index = 0;
/*  725 */       for (AbstractBlight b : this.blights) {
/*  726 */         if (b.hb.hovered) {
/*  727 */           anyHovered = true;
/*  728 */           type = RHoverType.BLIGHT;
/*      */           break;
/*      */         } 
/*  731 */         index++;
/*      */       } 
/*      */     } 
/*      */     
/*  735 */     if (!anyHovered) {
/*  736 */       CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*      */     }
/*  738 */     else if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/*  739 */       index--;
/*  740 */       if (type == RHoverType.RELIC) {
/*  741 */         if (index < AbstractRelic.relicPage * AbstractRelic.MAX_RELICS_PER_PAGE) {
/*  742 */           AbstractRelic.relicPage--;
/*      */           
/*  744 */           if (AbstractRelic.relicPage < 0) {
/*  745 */             if (this.relics.size() <= AbstractRelic.MAX_RELICS_PER_PAGE) {
/*  746 */               AbstractRelic.relicPage = 0;
/*      */             } else {
/*  748 */               AbstractRelic.relicPage = this.relics.size() / AbstractRelic.MAX_RELICS_PER_PAGE;
/*  749 */               AbstractDungeon.topPanel.adjustRelicHbs();
/*      */             } 
/*  751 */             index = this.relics.size() - 1;
/*      */           } else {
/*  753 */             index = (AbstractRelic.relicPage + 1) * AbstractRelic.MAX_RELICS_PER_PAGE - 1;
/*  754 */             AbstractDungeon.topPanel.adjustRelicHbs();
/*      */           } 
/*      */         } 
/*  757 */         CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
/*      */       } else {
/*  759 */         if (index < 0) {
/*  760 */           index = this.blights.size() - 1;
/*      */         }
/*  762 */         CInputHelper.setCursor(((AbstractBlight)this.blights.get(index)).hb);
/*      */       } 
/*  764 */     } else if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/*  765 */       index++;
/*  766 */       if (type == RHoverType.RELIC) {
/*  767 */         if (index > this.relics.size() - 1) {
/*  768 */           index = 0;
/*  769 */         } else if (index > (AbstractRelic.relicPage + 1) * AbstractRelic.MAX_RELICS_PER_PAGE - 1) {
/*  770 */           AbstractRelic.relicPage++;
/*  771 */           AbstractDungeon.topPanel.adjustRelicHbs();
/*  772 */           index = AbstractRelic.relicPage * AbstractRelic.MAX_RELICS_PER_PAGE;
/*      */         } 
/*  774 */         CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
/*      */       } else {
/*  776 */         if (index > this.blights.size() - 1) {
/*  777 */           index = 0;
/*      */         }
/*  779 */         CInputHelper.setCursor(((AbstractBlight)this.blights.get(index)).hb);
/*      */       } 
/*  781 */     } else if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
/*  782 */       CInputActionSet.up.unpress();
/*  783 */       if (type == RHoverType.RELIC) {
/*  784 */         this.viewingRelics = false;
/*  785 */         AbstractDungeon.topPanel.selectPotionMode = true;
/*  786 */         CInputHelper.setCursor(((AbstractPotion)this.potions.get(0)).hb);
/*      */       } else {
/*  788 */         CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*      */       } 
/*  790 */     } else if (CInputActionSet.cancel.isJustPressed()) {
/*  791 */       this.viewingRelics = false;
/*  792 */       Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
/*      */     
/*      */     }
/*  795 */     else if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
/*  796 */       if (type == RHoverType.RELIC) {
/*  797 */         if (this.blights.isEmpty()) {
/*  798 */           CInputActionSet.down.unpress();
/*  799 */           CInputActionSet.altDown.unpress();
/*  800 */           this.viewingRelics = false;
/*  801 */           this.inspectMode = true;
/*  802 */           if (!this.orbs.isEmpty()) {
/*  803 */             this.inspectHb = ((AbstractOrb)this.orbs.get(0)).hb;
/*      */           } else {
/*  805 */             this.inspectHb = this.hb;
/*      */           } 
/*  807 */           CInputHelper.setCursor(this.inspectHb);
/*      */         } else {
/*  809 */           CInputHelper.setCursor(((AbstractBlight)this.blights.get(0)).hb);
/*      */         } 
/*      */       } else {
/*  812 */         CInputActionSet.down.unpress();
/*  813 */         CInputActionSet.altDown.unpress();
/*  814 */         this.viewingRelics = false;
/*  815 */         this.inspectMode = true;
/*  816 */         if (!this.orbs.isEmpty()) {
/*  817 */           this.inspectHb = ((AbstractOrb)this.orbs.get(0)).hb;
/*      */         } else {
/*  819 */           this.inspectHb = this.hb;
/*      */         } 
/*  821 */         CInputHelper.setCursor(this.inspectHb);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loseGold(int goldAmount) {
/*  830 */     if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.ShopRoom) {
/*  831 */       for (AbstractRelic r : this.relics) {
/*  832 */         r.onSpendGold();
/*      */       }
/*      */     }
/*  835 */     if (!(AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.ShopRoom) && 
/*  836 */       (AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMBAT) {
/*  837 */       CardCrawlGame.sound.play("EVENT_PURCHASE");
/*      */     }
/*  839 */     if (goldAmount > 0) {
/*  840 */       this.gold -= goldAmount;
/*  841 */       if (this.gold < 0) {
/*  842 */         this.gold = 0;
/*      */       }
/*  844 */       for (AbstractRelic r : this.relics) {
/*  845 */         r.onLoseGold();
/*      */       }
/*      */     } else {
/*  848 */       logger.info("NEGATIVE MONEY???");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void gainGold(int amount) {
/*  855 */     if (hasRelic("Ectoplasm")) {
/*  856 */       getRelic("Ectoplasm").flash();
/*      */       
/*      */       return;
/*      */     } 
/*  860 */     if (amount <= 0) {
/*  861 */       logger.info("NEGATIVE MONEY???");
/*      */     } else {
/*  863 */       CardCrawlGame.goldGained += amount;
/*  864 */       this.gold += amount;
/*  865 */       for (AbstractRelic r : this.relics) {
/*  866 */         r.onGainGold();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void playDeathAnimation() {
/*  872 */     this.img = this.corpseImg;
/*  873 */     this.renderCorpse = true;
/*      */   }
/*      */   
/*      */   public boolean isCursed() {
/*  877 */     boolean cursed = false;
/*  878 */     for (AbstractCard c : this.masterDeck.group) {
/*  879 */       if (c.type == AbstractCard.CardType.CURSE && c.cardID != "Necronomicurse" && c.cardID != "CurseOfTheBell" && c.cardID != "AscendersBane")
/*      */       {
/*  881 */         cursed = true;
/*      */       }
/*      */     } 
/*  884 */     return cursed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateInput() {
/*  891 */     if (this.viewingRelics) {
/*      */       return;
/*      */     }
/*      */     
/*  895 */     if (this.hoverEnemyWaitTimer > 0.0F) {
/*  896 */       this.hoverEnemyWaitTimer -= Gdx.graphics.getDeltaTime();
/*      */     }
/*      */     
/*  899 */     if (this.inSingleTargetMode) {
/*  900 */       updateSingleTargetInput();
/*      */     } else {
/*      */       
/*  903 */       int y = InputHelper.mY;
/*      */       
/*  905 */       boolean hMonster = false;
/*  906 */       for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
/*  907 */         m.hb.update();
/*  908 */         if (m.hb.hovered && !m.isDying && !m.isEscaping && m.currentHealth > 0) {
/*  909 */           hMonster = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  914 */       boolean tmp = this.isHoveringDropZone;
/*  915 */       if (!Settings.isTouchScreen) {
/*  916 */         this.isHoveringDropZone = (((y > this.hoverStartLine || y > 300.0F * Settings.scale) && y < Settings.CARD_DROP_END_Y) || hMonster);
/*      */       } else {
/*      */         
/*  919 */         this.isHoveringDropZone = ((y > 350.0F * Settings.scale && y < Settings.CARD_DROP_END_Y) || hMonster);
/*      */       } 
/*      */       
/*  922 */       if (!tmp && this.isHoveringDropZone && this.isDraggingCard) {
/*  923 */         this.hoveredCard.flash(Color.SKY.cpy());
/*  924 */         if (this.hoveredCard.showEvokeValue) {
/*  925 */           if (this.hoveredCard.showEvokeOrbCount == 0) {
/*  926 */             for (AbstractOrb o : this.orbs) {
/*  927 */               o.showEvokeValue();
/*      */             }
/*      */           } else {
/*  930 */             int tmpShowCount = this.hoveredCard.showEvokeOrbCount;
/*  931 */             int emptyCount = 0;
/*  932 */             for (AbstractOrb o : this.orbs) {
/*  933 */               if (o instanceof EmptyOrbSlot) {
/*  934 */                 emptyCount++;
/*      */               }
/*      */             } 
/*  937 */             tmpShowCount -= emptyCount;
/*  938 */             if (tmpShowCount > 0) {
/*  939 */               for (AbstractOrb o : this.orbs) {
/*  940 */                 o.showEvokeValue();
/*  941 */                 tmpShowCount--;
/*  942 */                 if (tmpShowCount <= 0) {
/*      */                   break;
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*  951 */       if (this.isDraggingCard && this.isHoveringDropZone && this.hoveredCard != null) {
/*  952 */         this.passedHesitationLine = true;
/*      */       }
/*      */ 
/*      */       
/*  956 */       if (this.isDraggingCard && y < 50.0F * Settings.scale && this.passedHesitationLine) {
/*  957 */         if (Settings.isTouchScreen) {
/*  958 */           InputHelper.moveCursorToNeutralPosition();
/*      */         }
/*  960 */         releaseCard();
/*  961 */         CardCrawlGame.sound.play("UI_CLICK_1");
/*      */       } 
/*      */       
/*  964 */       updateFullKeyboardCardSelection();
/*      */       
/*  966 */       if (this.isInKeyboardMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.topPanel.potionUi.targetMode && !AbstractDungeon.isScreenUp) {
/*      */ 
/*      */         
/*  969 */         if (this.toHover != null) {
/*  970 */           releaseCard();
/*  971 */           this.hoveredCard = this.toHover;
/*  972 */           this.toHover = null;
/*      */           
/*  974 */           this.isHoveringCard = true;
/*  975 */           this.hoveredCard.current_y = HOVER_CARD_Y_POSITION;
/*  976 */           this.hoveredCard.target_y = HOVER_CARD_Y_POSITION;
/*  977 */           this.hoveredCard.setAngle(0.0F, true);
/*  978 */           this.hand.hoverCardPush(this.hoveredCard);
/*      */         }
/*      */       
/*      */       }
/*  982 */       else if (this.hoveredCard == null && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE && !AbstractDungeon.topPanel.selectPotionMode) {
/*      */         
/*  984 */         this.isHoveringCard = false;
/*      */ 
/*      */         
/*  987 */         if (this.toHover != null) {
/*  988 */           this.hoveredCard = this.toHover;
/*  989 */           this.toHover = null;
/*      */         } else {
/*  991 */           this.hoveredCard = this.hand.getHoveredCard();
/*      */         } 
/*  993 */         if (this.hoveredCard != null) {
/*  994 */           this.isHoveringCard = true;
/*  995 */           this.hoveredCard.current_y = HOVER_CARD_Y_POSITION;
/*  996 */           this.hoveredCard.target_y = HOVER_CARD_Y_POSITION;
/*  997 */           this.hoveredCard.setAngle(0.0F, true);
/*  998 */           this.hand.hoverCardPush(this.hoveredCard);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1003 */       if (this.hoveredCard != null) {
/* 1004 */         this.hoveredCard.drawScale = 1.0F;
/* 1005 */         this.hoveredCard.targetDrawScale = 1.0F;
/*      */       } 
/*      */ 
/*      */       
/* 1009 */       if (!this.isDraggingCard && 
/* 1010 */         hasRelic("Necronomicon")) {
/* 1011 */         getRelic("Necronomicon").stopPulse();
/*      */       }
/*      */ 
/*      */       
/* 1015 */       if (!this.endTurnQueued) {
/*      */         
/* 1017 */         if (!AbstractDungeon.actionManager.turnHasEnded && clickAndDragCards()) {
/*      */           return;
/*      */         }
/*      */         
/* 1021 */         if (!this.isInKeyboardMode && this.isHoveringCard && this.hoveredCard != null && !this.hoveredCard.isHoveredInHand(1.0F)) {
/*      */           
/* 1023 */           for (int i = 0; i < this.hand.group.size(); i++) {
/* 1024 */             if (this.hand.group.get(i) == this.hoveredCard && i != 0 && (
/* 1025 */               (AbstractCard)this.hand.group.get(i - 1)).isHoveredInHand(1.0F)) {
/* 1026 */               this.toHover = this.hand.group.get(i - 1);
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/* 1031 */           releaseCard();
/*      */         } 
/*      */         
/* 1034 */         if (this.hoveredCard != null) {
/* 1035 */           this.hoveredCard.updateHoverLogic();
/*      */         }
/*      */       }
/* 1038 */       else if (AbstractDungeon.actionManager.cardQueue.isEmpty() && !AbstractDungeon.actionManager.hasControl) {
/* 1039 */         this.endTurnQueued = false;
/* 1040 */         this.isEndingTurn = true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateSingleTargetInput() {
/* 1048 */     if (Settings.isTouchScreen && !this.isUsingClickDragControl && !InputHelper.isMouseDown) {
/* 1049 */       Gdx.input.setCursorPosition(
/* 1050 */           (int)MathUtils.lerp(Gdx.input.getX(), Settings.WIDTH / 2.0F, Gdx.graphics.getDeltaTime() * 10.0F), 
/* 1051 */           (int)MathUtils.lerp(Gdx.input.getY(), Settings.HEIGHT * 1.1F, Gdx.graphics.getDeltaTime() * 4.0F));
/*      */     }
/*      */ 
/*      */     
/* 1055 */     if (this.isInKeyboardMode) {
/* 1056 */       if (InputActionSet.releaseCard.isJustPressed() || CInputActionSet.cancel.isJustPressed()) {
/* 1057 */         AbstractCard card = this.hoveredCard;
/* 1058 */         this.inSingleTargetMode = false;
/* 1059 */         this.hoveredMonster = null;
/* 1060 */         hoverCardInHand(card);
/*      */       } else {
/* 1062 */         updateTargetArrowWithKeyboard(false);
/*      */       } 
/*      */     } else {
/* 1065 */       this.hoveredMonster = null;
/* 1066 */       for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
/* 1067 */         m.hb.update();
/* 1068 */         if (m.hb.hovered && !m.isDying && !m.isEscaping && m.currentHealth > 0) {
/* 1069 */           this.hoveredMonster = m;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1076 */     if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead() || InputHelper.justClickedRight || InputHelper.mY < 50.0F * Settings.scale || InputHelper.mY < this.hoverStartLine - 400.0F * Settings.scale) {
/*      */       
/* 1078 */       if (Settings.isTouchScreen) {
/* 1079 */         InputHelper.moveCursorToNeutralPosition();
/*      */       }
/* 1081 */       releaseCard();
/* 1082 */       CardCrawlGame.sound.play("UI_CLICK_2");
/* 1083 */       this.isUsingClickDragControl = false;
/* 1084 */       this.inSingleTargetMode = false;
/* 1085 */       GameCursor.hidden = false;
/* 1086 */       this.hoveredMonster = null;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1091 */     AbstractCard cardFromHotkey = InputHelper.getCardSelectedByHotkey(this.hand);
/* 1092 */     if (cardFromHotkey != null && !isCardQueued(cardFromHotkey)) {
/* 1093 */       boolean isSameCard = (cardFromHotkey == this.hoveredCard);
/*      */       
/* 1095 */       releaseCard();
/* 1096 */       this.hoveredMonster = null;
/* 1097 */       if (isSameCard) {
/* 1098 */         GameCursor.hidden = false;
/*      */       } else {
/*      */         
/* 1101 */         this.hoveredCard = cardFromHotkey;
/* 1102 */         this.hoveredCard.setAngle(0.0F, false);
/* 1103 */         this.isUsingClickDragControl = true;
/* 1104 */         this.isDraggingCard = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1109 */     if (InputHelper.justClickedLeft || InputActionSet.confirm.isJustPressed() || CInputActionSet.select
/* 1110 */       .isJustPressed()) {
/* 1111 */       InputHelper.justClickedLeft = false;
/*      */ 
/*      */       
/* 1114 */       if (this.hoveredMonster == null) {
/* 1115 */         CardCrawlGame.sound.play("UI_CLICK_1");
/*      */         return;
/*      */       } 
/* 1118 */       if (this.hoveredCard.canUse(this, this.hoveredMonster)) {
/* 1119 */         playCard();
/*      */       } else {
/* 1121 */         AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
/*      */         
/* 1123 */         energyTip(this.hoveredCard);
/* 1124 */         releaseCard();
/*      */       } 
/*      */ 
/*      */       
/* 1128 */       this.isUsingClickDragControl = false;
/* 1129 */       this.inSingleTargetMode = false;
/* 1130 */       GameCursor.hidden = false;
/* 1131 */       this.hoveredMonster = null;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1136 */     if (!this.isUsingClickDragControl && InputHelper.justReleasedClickLeft && this.hoveredMonster != null) {
/* 1137 */       if (this.hoveredCard.canUse(this, this.hoveredMonster)) {
/* 1138 */         playCard();
/*      */       } else {
/* 1140 */         AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
/*      */         
/* 1142 */         energyTip(this.hoveredCard);
/* 1143 */         releaseCard();
/*      */       } 
/*      */       
/* 1146 */       this.inSingleTargetMode = false;
/* 1147 */       GameCursor.hidden = false;
/* 1148 */       this.hoveredMonster = null;
/*      */       return;
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isCardQueued(AbstractCard card) {
/* 1154 */     for (CardQueueItem item : AbstractDungeon.actionManager.cardQueue) {
/* 1155 */       if (item.card == card) {
/* 1156 */         return true;
/*      */       }
/*      */     } 
/* 1159 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void energyTip(AbstractCard cardToCheck) {
/* 1164 */     int availableEnergy = EnergyPanel.totalCount;
/*      */     
/* 1166 */     if (cardToCheck.cost > availableEnergy)
/*      */     {
/* 1168 */       if (!((Boolean)TipTracker.tips.get("ENERGY_USE_TIP")).booleanValue()) {
/* 1169 */         TipTracker.energyUseCounter++;
/* 1170 */         if (TipTracker.energyUseCounter >= 2) {
/* 1171 */           AbstractDungeon.ftue = new FtueTip(LABEL[1], MSG[1], 330.0F * Settings.scale, 400.0F * Settings.scale, FtueTip.TipType.ENERGY);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1177 */           TipTracker.neverShowAgain("ENERGY_USE_TIP");
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean updateFullKeyboardCardSelection() {
/* 1184 */     if (Settings.isControllerMode || InputActionSet.left.isJustPressed() || InputActionSet.right.isJustPressed() || InputActionSet.confirm
/* 1185 */       .isJustPressed()) {
/* 1186 */       this.isInKeyboardMode = true;
/* 1187 */       this.skipMouseModeOnce = true;
/* 1188 */       GameCursor.hidden = true;
/*      */     } 
/*      */     
/* 1191 */     if (this.isInKeyboardMode && InputHelper.didMoveMouse()) {
/* 1192 */       if (this.skipMouseModeOnce) {
/* 1193 */         this.skipMouseModeOnce = false;
/*      */       } else {
/* 1195 */         this.isInKeyboardMode = false;
/* 1196 */         GameCursor.hidden = false;
/*      */       } 
/*      */     }
/*      */     
/* 1200 */     if (!this.isInKeyboardMode || this.hand.isEmpty() || this.inspectMode) {
/* 1201 */       return false;
/*      */     }
/*      */     
/* 1204 */     if (this.keyboardCardIndex == -2) {
/* 1205 */       if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft
/* 1206 */         .isJustPressed()) {
/* 1207 */         this.keyboardCardIndex = this.hand.size() - 1;
/* 1208 */       } else if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight
/* 1209 */         .isJustPressed()) {
/* 1210 */         this.keyboardCardIndex = 0;
/*      */       } 
/* 1212 */       return false;
/*      */     } 
/* 1214 */     if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft
/* 1215 */       .isJustPressed()) {
/* 1216 */       this.keyboardCardIndex--;
/* 1217 */     } else if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight
/* 1218 */       .isJustPressed()) {
/* 1219 */       this.keyboardCardIndex++;
/*      */     } 
/*      */ 
/*      */     
/* 1223 */     this.keyboardCardIndex = (this.keyboardCardIndex + this.hand.size()) % this.hand.size();
/*      */     
/* 1225 */     if (!AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.topPanel.potionUi.targetMode) {
/*      */       
/* 1227 */       AbstractCard card = this.hand.group.get(this.keyboardCardIndex);
/* 1228 */       if (card != this.hoveredCard && Math.abs(card.current_x - card.target_x) < 400.0F * Settings.scale) {
/* 1229 */         hoverCardInHand(card);
/* 1230 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 1234 */     return false;
/*      */   }
/*      */   
/*      */   private void hoverCardInHand(AbstractCard card) {
/* 1238 */     this.toHover = card;
/* 1239 */     if (Settings.isControllerMode && AbstractDungeon.actionManager.turnHasEnded) {
/* 1240 */       this.toHover = null;
/*      */     }
/* 1242 */     if (card != null && !this.inspectMode) {
/* 1243 */       Gdx.input.setCursorPosition((int)card.hb.cX, (int)(Settings.HEIGHT - HOVER_CARD_Y_POSITION));
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateTargetArrowWithKeyboard(boolean autoTargetFirst) {
/* 1248 */     int directionIndex = 0;
/*      */     
/* 1250 */     if (autoTargetFirst) {
/* 1251 */       directionIndex++;
/*      */     }
/* 1253 */     if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft
/* 1254 */       .isJustPressed()) {
/* 1255 */       directionIndex--;
/*      */     }
/* 1257 */     if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight
/* 1258 */       .isJustPressed()) {
/* 1259 */       directionIndex++;
/*      */     }
/*      */     
/* 1262 */     if (directionIndex != 0) {
/*      */ 
/*      */       
/* 1265 */       ArrayList<AbstractMonster> prefiltered = (AbstractDungeon.getCurrRoom()).monsters.monsters;
/*      */       
/* 1267 */       ArrayList<AbstractMonster> sortedMonsters = new ArrayList<>((AbstractDungeon.getCurrRoom()).monsters.monsters);
/*      */       
/* 1269 */       for (AbstractMonster mons : prefiltered) {
/* 1270 */         if (mons.isDying) {
/* 1271 */           sortedMonsters.remove(mons);
/*      */         }
/*      */       } 
/*      */       
/* 1275 */       sortedMonsters.sort(AbstractMonster.sortByHitbox);
/*      */       
/* 1277 */       if (sortedMonsters.isEmpty()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1282 */       for (AbstractMonster m : sortedMonsters) {
/* 1283 */         if (m.hb.hovered) {
/* 1284 */           this.hoveredMonster = m;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1289 */       AbstractMonster newTarget = null;
/* 1290 */       if (this.hoveredMonster == null) {
/*      */         
/* 1292 */         if (directionIndex == 1) {
/*      */           
/* 1294 */           newTarget = sortedMonsters.get(0);
/*      */         } else {
/*      */           
/* 1297 */           newTarget = sortedMonsters.get(sortedMonsters.size() - 1);
/*      */         } 
/*      */       } else {
/*      */         
/* 1301 */         int currentTargetIndex = sortedMonsters.indexOf(this.hoveredMonster);
/* 1302 */         int newTargetIndex = currentTargetIndex + directionIndex;
/* 1303 */         newTargetIndex = (newTargetIndex + sortedMonsters.size()) % sortedMonsters.size();
/* 1304 */         newTarget = sortedMonsters.get(newTargetIndex);
/*      */       } 
/*      */       
/* 1307 */       if (newTarget != null) {
/* 1308 */         Hitbox target = newTarget.hb;
/* 1309 */         Gdx.input.setCursorPosition((int)target.cX, Settings.HEIGHT - (int)target.cY);
/* 1310 */         this.hoveredMonster = newTarget;
/* 1311 */         this.isUsingClickDragControl = true;
/* 1312 */         this.isDraggingCard = true;
/*      */       } 
/*      */       
/* 1315 */       if (this.hoveredMonster.halfDead) {
/* 1316 */         this.hoveredMonster = null;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderCardHotKeyText(SpriteBatch sb) {
/* 1322 */     int index = 0;
/* 1323 */     for (AbstractCard card : this.hand.group) {
/* 1324 */       if (index < InputActionSet.selectCardActions.length) {
/* 1325 */         float width = AbstractCard.IMG_WIDTH * card.drawScale / 2.0F;
/* 1326 */         float height = AbstractCard.IMG_HEIGHT * card.drawScale / 2.0F;
/*      */         
/* 1328 */         float topOfCard = card.current_y + height;
/* 1329 */         float textSpacing = 50.0F * Settings.scale;
/* 1330 */         float textY = topOfCard + textSpacing;
/*      */         
/* 1332 */         float sin = (float)Math.sin((card.angle / 180.0F) * Math.PI);
/* 1333 */         float xOffset = sin * width;
/*      */         
/* 1335 */         FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, InputActionSet.selectCardActions[index]
/*      */ 
/*      */             
/* 1338 */             .getKeyString(), card.current_x - xOffset, textY, Settings.CREAM_COLOR);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1343 */       index++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean clickAndDragCards() {
/* 1352 */     boolean simulateRightClickDrop = false;
/* 1353 */     AbstractCard cardFromHotkey = InputHelper.getCardSelectedByHotkey(this.hand);
/* 1354 */     if (cardFromHotkey != null && !isCardQueued(cardFromHotkey)) {
/* 1355 */       if (this.isDraggingCard) {
/* 1356 */         simulateRightClickDrop = (cardFromHotkey == this.hoveredCard);
/* 1357 */         CardCrawlGame.sound.play("UI_CLICK_2");
/* 1358 */         releaseCard();
/*      */       } 
/*      */       
/* 1361 */       if (!simulateRightClickDrop) {
/* 1362 */         manuallySelectCard(cardFromHotkey);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1367 */     if (CInputActionSet.select.isJustPressed() && this.hoveredCard != null && !isCardQueued(this.hoveredCard) && !this.isDraggingCard) {
/*      */       
/* 1369 */       manuallySelectCard(this.hoveredCard);
/* 1370 */       if (this.hoveredCard.target == AbstractCard.CardTarget.ENEMY) {
/* 1371 */         updateTargetArrowWithKeyboard(true);
/*      */       } else {
/* 1373 */         InputHelper.moveCursorToNeutralPosition();
/*      */       } 
/* 1375 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1379 */     if (InputHelper.justClickedLeft && this.isHoveringCard && !this.isDraggingCard) {
/*      */       
/* 1381 */       this.hoverStartLine = InputHelper.mY + 140.0F * Settings.scale;
/* 1382 */       InputHelper.justClickedLeft = false;
/*      */       
/* 1384 */       if (this.hoveredCard != null) {
/* 1385 */         CardCrawlGame.sound.play("CARD_OBTAIN");
/* 1386 */         this.isDraggingCard = true;
/* 1387 */         this.passedHesitationLine = false;
/* 1388 */         this.hoveredCard.targetDrawScale = 0.7F;
/*      */         
/* 1390 */         if (Settings.isTouchScreen && this.touchscreenInspectCount == 0) {
/* 1391 */           this.hoveredCard.current_y = AbstractCard.IMG_HEIGHT / 2.0F;
/* 1392 */           this.hoveredCard.target_y = AbstractCard.IMG_HEIGHT / 2.0F;
/* 1393 */           Gdx.input.setCursorPosition((int)this.hoveredCard.current_x, (int)(Settings.HEIGHT - AbstractCard.IMG_HEIGHT / 2.0F));
/*      */ 
/*      */           
/* 1396 */           this.touchscreenInspectCount = 0;
/*      */         } 
/*      */         
/* 1399 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 1403 */     if (InputHelper.isMouseDown) {
/* 1404 */       this.clickDragTimer += Gdx.graphics.getDeltaTime();
/*      */     } else {
/* 1406 */       this.clickDragTimer = 0.0F;
/*      */     } 
/*      */ 
/*      */     
/* 1410 */     if ((InputHelper.justClickedLeft || InputActionSet.confirm.isJustPressed() || CInputActionSet.select
/* 1411 */       .isJustPressed()) && this.isUsingClickDragControl) {
/*      */       
/* 1413 */       if (InputHelper.justClickedRight || simulateRightClickDrop) {
/* 1414 */         CardCrawlGame.sound.play("UI_CLICK_2");
/* 1415 */         releaseCard();
/* 1416 */         return true;
/*      */       } 
/*      */       
/* 1419 */       InputHelper.justClickedLeft = false;
/*      */ 
/*      */       
/* 1422 */       if (this.isHoveringDropZone && this.hoveredCard.canUse(this, null) && this.hoveredCard.target != AbstractCard.CardTarget.ENEMY && this.hoveredCard.target != AbstractCard.CardTarget.SELF_AND_ENEMY) {
/*      */         
/* 1424 */         playCard();
/*      */       } else {
/* 1426 */         CardCrawlGame.sound.play("CARD_OBTAIN");
/* 1427 */         releaseCard();
/*      */       } 
/* 1429 */       this.isUsingClickDragControl = false;
/* 1430 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1434 */     if (this.isInKeyboardMode) {
/* 1435 */       if (InputActionSet.releaseCard.isJustPressed() || CInputActionSet.cancel.isJustPressed()) {
/* 1436 */         hoverCardInHand(this.hoveredCard);
/* 1437 */       } else if ((InputActionSet.confirm.isJustPressed() || CInputActionSet.select.isJustPressed()) && this.hoveredCard != null) {
/*      */ 
/*      */         
/* 1440 */         manuallySelectCard(this.hoveredCard);
/* 1441 */         if (this.hoveredCard.target == AbstractCard.CardTarget.ENEMY) {
/* 1442 */           updateTargetArrowWithKeyboard(true);
/*      */         } else {
/* 1444 */           Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1450 */     if (this.isDraggingCard && (InputHelper.isMouseDown || this.isUsingClickDragControl)) {
/* 1451 */       if (InputHelper.justClickedRight || simulateRightClickDrop) {
/* 1452 */         CardCrawlGame.sound.play("UI_CLICK_2");
/* 1453 */         releaseCard();
/* 1454 */         return true;
/*      */       } 
/*      */       
/* 1457 */       if (!Settings.isTouchScreen) {
/* 1458 */         this.hoveredCard.target_x = InputHelper.mX;
/* 1459 */         this.hoveredCard.target_y = InputHelper.mY;
/*      */       } else {
/* 1461 */         this.hoveredCard.target_x = InputHelper.mX;
/* 1462 */         this.hoveredCard.target_y = InputHelper.mY + 270.0F * Settings.scale;
/*      */       } 
/*      */ 
/*      */       
/* 1466 */       if (!this.hoveredCard.hasEnoughEnergy() && this.isHoveringDropZone) {
/* 1467 */         AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
/*      */         
/* 1469 */         energyTip(this.hoveredCard);
/* 1470 */         releaseCard();
/* 1471 */         CardCrawlGame.sound.play("CARD_REJECT");
/* 1472 */         return true;
/* 1473 */       }  if (this.isHoveringDropZone && (this.hoveredCard.target == AbstractCard.CardTarget.ENEMY || this.hoveredCard.target == AbstractCard.CardTarget.SELF_AND_ENEMY)) {
/*      */         
/* 1475 */         this.inSingleTargetMode = true;
/* 1476 */         this.arrowX = InputHelper.mX;
/* 1477 */         this.arrowY = InputHelper.mY;
/* 1478 */         GameCursor.hidden = true;
/* 1479 */         this.hoveredCard.untip();
/* 1480 */         this.hand.refreshHandLayout();
/*      */         
/* 1482 */         if (!Settings.isTouchScreen) {
/* 1483 */           this.hoveredCard.target_y = AbstractCard.IMG_HEIGHT * 0.75F / 2.5F;
/* 1484 */           this.hoveredCard.target_x = Settings.WIDTH / 2.0F;
/*      */         } else {
/* 1486 */           this.hoveredCard.target_y = 260.0F * Settings.scale;
/* 1487 */           this.hoveredCard.target_x = Settings.WIDTH / 2.0F;
/* 1488 */           this.hoveredCard.targetDrawScale = 1.0F;
/*      */         } 
/* 1490 */         this.isDraggingCard = false;
/*      */       } 
/* 1492 */       return true;
/*      */     } 
/*      */     
/* 1495 */     if (this.isDraggingCard && InputHelper.justReleasedClickLeft && !Settings.isTouchScreen) {
/* 1496 */       if (this.isHoveringDropZone) {
/* 1497 */         if (this.hoveredCard.target == AbstractCard.CardTarget.ENEMY || this.hoveredCard.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
/* 1498 */           this.inSingleTargetMode = true;
/* 1499 */           this.arrowX = InputHelper.mX;
/* 1500 */           this.arrowY = InputHelper.mY;
/* 1501 */           GameCursor.hidden = true;
/* 1502 */           this.hoveredCard.untip();
/* 1503 */           this.hand.refreshHandLayout();
/* 1504 */           this.hoveredCard.target_y = AbstractCard.IMG_HEIGHT * 0.75F / 2.5F;
/* 1505 */           this.hoveredCard.target_x = Settings.WIDTH / 2.0F;
/* 1506 */           this.isDraggingCard = false;
/* 1507 */           return true;
/* 1508 */         }  if (this.hoveredCard.canUse(this, null)) {
/* 1509 */           playCard();
/* 1510 */           return true;
/*      */         } 
/* 1512 */         AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
/*      */         
/* 1514 */         energyTip(this.hoveredCard);
/* 1515 */         releaseCard();
/* 1516 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1520 */       if (this.clickDragTimer < 0.4F) {
/* 1521 */         this.isUsingClickDragControl = true;
/* 1522 */         return true;
/*      */       } 
/* 1524 */       if (AbstractDungeon.actionManager.currentAction == null) {
/* 1525 */         releaseCard();
/* 1526 */         CardCrawlGame.sound.play("CARD_OBTAIN");
/* 1527 */         return true;
/*      */       }
/*      */     
/*      */     }
/* 1531 */     else if (Settings.isTouchScreen && InputHelper.justReleasedClickLeft && this.hoveredCard != null) {
/* 1532 */       this.touchscreenInspectCount++;
/* 1533 */       if (this.isHoveringDropZone && this.hoveredCard.hasEnoughEnergy() && this.hoveredCard.canUse(this, null)) {
/* 1534 */         playCard();
/* 1535 */         return true;
/* 1536 */       }  if (this.touchscreenInspectCount > 1) {
/* 1537 */         AbstractCard newHoveredCard = null;
/* 1538 */         for (AbstractCard c : this.hand.group) {
/* 1539 */           c.updateHoverLogic();
/* 1540 */           if (c.hb.hovered && c != this.hoveredCard) {
/* 1541 */             newHoveredCard = c;
/*      */             break;
/*      */           } 
/*      */         } 
/* 1545 */         releaseCard();
/* 1546 */         if (newHoveredCard == null) {
/* 1547 */           InputHelper.moveCursorToNeutralPosition();
/*      */         } else {
/* 1549 */           newHoveredCard.current_y = AbstractCard.IMG_HEIGHT / 2.0F;
/* 1550 */           newHoveredCard.target_y = AbstractCard.IMG_HEIGHT / 2.0F;
/* 1551 */           newHoveredCard.angle = 0.0F;
/* 1552 */           Gdx.input.setCursorPosition((int)newHoveredCard.current_x, (int)(Settings.HEIGHT - AbstractCard.IMG_HEIGHT / 2.0F));
/*      */ 
/*      */           
/* 1555 */           this.touchscreenInspectCount = 1;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1560 */     return false;
/*      */   }
/*      */   
/*      */   private void manuallySelectCard(AbstractCard card) {
/* 1564 */     this.hoveredCard = card;
/* 1565 */     this.hoveredCard.setAngle(0.0F, false);
/* 1566 */     this.isUsingClickDragControl = true;
/* 1567 */     this.isDraggingCard = true;
/*      */     
/* 1569 */     this.hoveredCard.flash(Color.SKY.cpy());
/* 1570 */     if (this.hoveredCard.showEvokeValue) {
/* 1571 */       if (this.hoveredCard.showEvokeOrbCount == 0) {
/* 1572 */         for (AbstractOrb o : this.orbs) {
/* 1573 */           o.showEvokeValue();
/*      */         }
/*      */       } else {
/* 1576 */         int tmpShowCount = this.hoveredCard.showEvokeOrbCount;
/* 1577 */         int emptyCount = 0;
/* 1578 */         for (AbstractOrb o : this.orbs) {
/* 1579 */           if (o instanceof EmptyOrbSlot) {
/* 1580 */             emptyCount++;
/*      */           }
/*      */         } 
/* 1583 */         tmpShowCount -= emptyCount;
/* 1584 */         if (tmpShowCount > 0) {
/* 1585 */           for (AbstractOrb o : this.orbs) {
/* 1586 */             o.showEvokeValue();
/* 1587 */             tmpShowCount--;
/* 1588 */             if (tmpShowCount <= 0) {
/*      */               break;
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void playCard() {
/* 1598 */     InputHelper.justClickedLeft = false;
/* 1599 */     this.hoverEnemyWaitTimer = 1.0F;
/* 1600 */     this.hoveredCard.unhover();
/*      */     
/* 1602 */     if (!queueContains(this.hoveredCard)) {
/* 1603 */       if (this.hoveredCard.target == AbstractCard.CardTarget.ENEMY || this.hoveredCard.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
/* 1604 */         if (hasPower("Surrounded")) {
/* 1605 */           this.flipHorizontal = (this.hoveredMonster.drawX < this.drawX);
/*      */         }
/* 1607 */         AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.hoveredCard, this.hoveredMonster));
/*      */       } else {
/* 1609 */         AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.hoveredCard, null));
/*      */       } 
/*      */     }
/*      */     
/* 1613 */     this.isUsingClickDragControl = false;
/* 1614 */     this.hoveredCard = null;
/* 1615 */     this.isDraggingCard = false;
/*      */   }
/*      */   
/*      */   private boolean queueContains(AbstractCard card) {
/* 1619 */     for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
/* 1620 */       if (i.card == card) {
/* 1621 */         return true;
/*      */       }
/*      */     } 
/* 1624 */     return false;
/*      */   }
/*      */   
/*      */   public void releaseCard() {
/* 1628 */     for (AbstractOrb o : this.orbs) {
/* 1629 */       o.hideEvokeValues();
/*      */     }
/*      */     
/* 1632 */     this.passedHesitationLine = false;
/* 1633 */     InputHelper.justClickedLeft = false;
/* 1634 */     InputHelper.justReleasedClickLeft = false;
/* 1635 */     InputHelper.isMouseDown = false;
/* 1636 */     this.inSingleTargetMode = false;
/* 1637 */     if (!this.isInKeyboardMode) {
/* 1638 */       GameCursor.hidden = false;
/*      */     }
/* 1640 */     this.isUsingClickDragControl = false;
/* 1641 */     this.isHoveringDropZone = false;
/* 1642 */     this.isDraggingCard = false;
/* 1643 */     this.isHoveringCard = false;
/*      */     
/* 1645 */     if (this.hoveredCard != null) {
/* 1646 */       if (this.hoveredCard.canUse(this, null)) {
/* 1647 */         this.hoveredCard.beginGlowing();
/*      */       }
/* 1649 */       this.hoveredCard.untip();
/* 1650 */       this.hoveredCard.hoverTimer = 0.25F;
/* 1651 */       this.hoveredCard.unhover();
/*      */     } 
/* 1653 */     this.hoveredCard = null;
/* 1654 */     this.hand.refreshHandLayout();
/* 1655 */     this.touchscreenInspectCount = 0;
/*      */   }
/*      */   
/*      */   public void onCardDrawOrDiscard() {
/* 1659 */     for (AbstractPower p : this.powers) {
/* 1660 */       p.onDrawOrDiscard();
/*      */     }
/*      */     
/* 1663 */     for (AbstractRelic r : this.relics) {
/* 1664 */       r.onDrawOrDiscard();
/*      */     }
/*      */ 
/*      */     
/* 1668 */     if (hasPower("Corruption")) {
/* 1669 */       for (AbstractCard c : this.hand.group) {
/* 1670 */         if (c.type == AbstractCard.CardType.SKILL && c.costForTurn != 0) {
/* 1671 */           c.modifyCostForCombat(-9);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1676 */     this.hand.applyPowers();
/* 1677 */     this.hand.glowCheck();
/*      */   }
/*      */   
/*      */   public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
/* 1681 */     if (c.type == AbstractCard.CardType.ATTACK) {
/* 1682 */       useFastAttackAnimation();
/*      */     }
/*      */     
/* 1685 */     c.calculateCardDamage(monster);
/*      */ 
/*      */     
/* 1688 */     if (c.cost == -1 && EnergyPanel.totalCount < energyOnUse && !c.ignoreEnergyOnUse) {
/* 1689 */       c.energyOnUse = EnergyPanel.totalCount;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1694 */     if (c.cost == -1 && c.isInAutoplay) {
/* 1695 */       c.freeToPlayOnce = true;
/*      */     }
/*      */     
/* 1698 */     c.use(this, monster);
/*      */     
/* 1700 */     AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new UseCardAction(c, (AbstractCreature)monster));
/* 1701 */     if (!c.dontTriggerOnUseCard) {
/* 1702 */       this.hand.triggerOnOtherCardPlayed(c);
/*      */     }
/* 1704 */     this.hand.removeCard(c);
/* 1705 */     this.cardInUse = c;
/*      */     
/* 1707 */     c.target_x = (Settings.WIDTH / 2);
/* 1708 */     c.target_y = (Settings.HEIGHT / 2);
/*      */     
/* 1710 */     if (c.costForTurn > 0 && !c.freeToPlay() && !c.isInAutoplay && (
/* 1711 */       !hasPower("Corruption") || c.type != AbstractCard.CardType.SKILL)) {
/* 1712 */       this.energy.use(c.costForTurn);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1717 */     if (!this.hand.canUseAnyCard() && !this.endTurnQueued) {
/* 1718 */       AbstractDungeon.overlayMenu.endTurnButton.isGlowing = true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void damage(DamageInfo info) {
/* 1725 */     int damageAmount = info.output;
/* 1726 */     boolean hadBlock = true;
/*      */     
/* 1728 */     if (this.currentBlock == 0) {
/* 1729 */       hadBlock = false;
/*      */     }
/*      */     
/* 1732 */     if (damageAmount < 0) {
/* 1733 */       damageAmount = 0;
/*      */     }
/*      */     
/* 1736 */     if (damageAmount > 1 && hasPower("IntangiblePlayer")) {
/* 1737 */       damageAmount = 1;
/*      */     }
/*      */ 
/*      */     
/* 1741 */     damageAmount = decrementBlock(info, damageAmount);
/*      */ 
/*      */     
/* 1744 */     if (info.owner == this) {
/* 1745 */       for (AbstractRelic r : this.relics) {
/* 1746 */         damageAmount = r.onAttackToChangeDamage(info, damageAmount);
/*      */       }
/*      */     }
/* 1749 */     if (info.owner != null) {
/* 1750 */       for (AbstractPower p : info.owner.powers) {
/* 1751 */         damageAmount = p.onAttackToChangeDamage(info, damageAmount);
/*      */       }
/*      */     }
/* 1754 */     for (AbstractRelic r : this.relics) {
/* 1755 */       damageAmount = r.onAttackedToChangeDamage(info, damageAmount);
/*      */     }
/* 1757 */     for (AbstractPower p : this.powers) {
/* 1758 */       damageAmount = p.onAttackedToChangeDamage(info, damageAmount);
/*      */     }
/*      */     
/* 1761 */     if (info.owner == this) {
/* 1762 */       for (AbstractRelic r : this.relics) {
/* 1763 */         r.onAttack(info, damageAmount, this);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1768 */     if (info.owner != null) {
/* 1769 */       for (AbstractPower p : info.owner.powers) {
/* 1770 */         p.onAttack(info, damageAmount, this);
/*      */       }
/* 1772 */       for (AbstractPower p : this.powers) {
/* 1773 */         damageAmount = p.onAttacked(info, damageAmount);
/*      */       }
/* 1775 */       for (AbstractRelic r : this.relics) {
/* 1776 */         damageAmount = r.onAttacked(info, damageAmount);
/*      */       }
/*      */     } else {
/* 1779 */       logger.info("NO OWNER, DON'T TRIGGER POWERS");
/*      */     } 
/*      */     
/* 1782 */     for (AbstractRelic r : this.relics) {
/* 1783 */       damageAmount = r.onLoseHpLast(damageAmount);
/*      */     }
/*      */     
/* 1786 */     this.lastDamageTaken = Math.min(damageAmount, this.currentHealth);
/*      */     
/* 1788 */     if (damageAmount > 0) {
/* 1789 */       for (AbstractPower p : this.powers) {
/* 1790 */         damageAmount = p.onLoseHp(damageAmount);
/*      */       }
/*      */       
/* 1793 */       for (AbstractRelic r : this.relics) {
/* 1794 */         r.onLoseHp(damageAmount);
/*      */       }
/*      */       
/* 1797 */       for (AbstractPower p : this.powers) {
/* 1798 */         p.wasHPLost(info, damageAmount);
/*      */       }
/*      */       
/* 1801 */       for (AbstractRelic r : this.relics) {
/* 1802 */         r.wasHPLost(damageAmount);
/*      */       }
/*      */       
/* 1805 */       if (info.owner != null) {
/* 1806 */         for (AbstractPower p : info.owner.powers) {
/* 1807 */           p.onInflictDamage(info, damageAmount, this);
/*      */         }
/*      */       }
/*      */       
/* 1811 */       if (info.owner != this) {
/* 1812 */         useStaggerAnimation();
/*      */       }
/*      */       
/* 1815 */       if (info.type == DamageInfo.DamageType.HP_LOSS) {
/* 1816 */         GameActionManager.hpLossThisCombat += damageAmount;
/*      */       }
/*      */       
/* 1819 */       GameActionManager.damageReceivedThisTurn += damageAmount;
/* 1820 */       GameActionManager.damageReceivedThisCombat += damageAmount;
/*      */       
/* 1822 */       this.currentHealth -= damageAmount;
/*      */       
/* 1824 */       if (damageAmount > 0 && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/* 1825 */         updateCardsOnDamage();
/* 1826 */         this.damagedThisCombat++;
/*      */       } 
/*      */       
/* 1829 */       AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));
/*      */       
/* 1831 */       if (this.currentHealth < 0) {
/* 1832 */         this.currentHealth = 0;
/* 1833 */       } else if (this.currentHealth < this.maxHealth / 4) {
/* 1834 */         AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(1.0F, 0.1F, 0.05F, 0.0F)));
/*      */       } 
/*      */       
/* 1837 */       healthBarUpdatedEvent();
/*      */       
/* 1839 */       if (this.currentHealth <= this.maxHealth / 2.0F && 
/* 1840 */         !this.isBloodied) {
/* 1841 */         this.isBloodied = true;
/* 1842 */         for (AbstractRelic r : this.relics) {
/* 1843 */           if (r != null) {
/* 1844 */             r.onBloodied();
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1850 */       if (this.currentHealth < 1) {
/* 1851 */         if (!hasRelic("Mark of the Bloom"))
/*      */         {
/* 1853 */           if (hasPotion("FairyPotion")) {
/*      */             
/* 1855 */             for (AbstractPotion p : this.potions) {
/* 1856 */               if (p.ID.equals("FairyPotion")) {
/* 1857 */                 p.flash();
/* 1858 */                 this.currentHealth = 0;
/* 1859 */                 p.use(this);
/* 1860 */                 AbstractDungeon.topPanel.destroyPotion(p.slot);
/*      */                 return;
/*      */               } 
/*      */             } 
/* 1864 */           } else if (hasRelic("Lizard Tail")) {
/*      */             
/* 1866 */             if (((LizardTail)getRelic("Lizard Tail")).counter == -1) {
/* 1867 */               this.currentHealth = 0;
/* 1868 */               getRelic("Lizard Tail").onTrigger();
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */         }
/* 1874 */         this.isDead = true;
/* 1875 */         AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
/* 1876 */         this.currentHealth = 0;
/* 1877 */         if (this.currentBlock > 0) {
/* 1878 */           loseBlock();
/* 1879 */           AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1886 */     else if (this.currentBlock > 0) {
/* 1887 */       AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, uiStrings.TEXT[0]));
/* 1888 */     } else if (hadBlock) {
/* 1889 */       AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, uiStrings.TEXT[0]));
/* 1890 */       AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1895 */       AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateCardsOnDamage() {
/* 1901 */     if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/* 1902 */       for (AbstractCard c : this.hand.group) {
/* 1903 */         c.tookDamage();
/*      */       }
/* 1905 */       for (AbstractCard c : this.discardPile.group) {
/* 1906 */         c.tookDamage();
/*      */       }
/* 1908 */       for (AbstractCard c : this.drawPile.group) {
/* 1909 */         c.tookDamage();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateCardsOnDiscard() {
/* 1915 */     for (AbstractCard c : this.hand.group) {
/* 1916 */       c.didDiscard();
/*      */     }
/* 1918 */     for (AbstractCard c : this.discardPile.group) {
/* 1919 */       c.didDiscard();
/*      */     }
/* 1921 */     for (AbstractCard c : this.drawPile.group) {
/* 1922 */       c.didDiscard();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void heal(int healAmount) {
/* 1929 */     super.heal(healAmount);
/* 1930 */     if (this.currentHealth > this.maxHealth / 2.0F && 
/* 1931 */       this.isBloodied) {
/* 1932 */       this.isBloodied = false;
/* 1933 */       for (AbstractRelic r : this.relics) {
/* 1934 */         r.onNotBloodied();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void gainEnergy(int e) {
/* 1941 */     EnergyPanel.addEnergy(e);
/* 1942 */     this.hand.glowCheck();
/*      */   }
/*      */   
/*      */   public void loseEnergy(int e) {
/* 1946 */     EnergyPanel.useEnergy(e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void preBattlePrep() {
/* 1956 */     if (!((Boolean)TipTracker.tips.get("COMBAT_TIP")).booleanValue()) {
/* 1957 */       AbstractDungeon.ftue = (FtueTip)new MultiPageFtue();
/* 1958 */       TipTracker.neverShowAgain("COMBAT_TIP");
/*      */     } 
/*      */ 
/*      */     
/* 1962 */     AbstractDungeon.actionManager.clear();
/* 1963 */     this.damagedThisCombat = 0;
/* 1964 */     this.cardsPlayedThisTurn = 0;
/*      */ 
/*      */     
/* 1967 */     this.maxOrbs = 0;
/* 1968 */     this.orbs.clear();
/* 1969 */     increaseMaxOrbSlots(this.masterMaxOrbs, false);
/*      */     
/* 1971 */     this.isBloodied = (this.currentHealth <= this.maxHealth / 2);
/*      */     
/* 1973 */     poisonKillCount = 0;
/*      */     
/* 1975 */     GameActionManager.playerHpLastTurn = this.currentHealth;
/* 1976 */     this.endTurnQueued = false;
/* 1977 */     this.gameHandSize = this.masterHandSize;
/* 1978 */     this.isDraggingCard = false;
/* 1979 */     this.isHoveringDropZone = false;
/* 1980 */     this.hoveredCard = null;
/* 1981 */     this.cardInUse = null;
/* 1982 */     this.drawPile.initializeDeck(this.masterDeck);
/* 1983 */     AbstractDungeon.overlayMenu.endTurnButton.enabled = false;
/* 1984 */     this.hand.clear();
/* 1985 */     this.discardPile.clear();
/* 1986 */     this.exhaustPile.clear();
/*      */     
/* 1988 */     if (AbstractDungeon.player.hasRelic("SlaversCollar")) {
/* 1989 */       ((SlaversCollar)AbstractDungeon.player.getRelic("SlaversCollar")).beforeEnergyPrep();
/*      */     }
/*      */     
/* 1992 */     this.energy.prep();
/* 1993 */     this.powers.clear();
/* 1994 */     this.isEndingTurn = false;
/* 1995 */     healthBarUpdatedEvent();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2000 */     if (ModHelper.isModEnabled("Lethality")) {
/* 2001 */       AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(this, this, (AbstractPower)new StrengthPower(this, 3), 3));
/*      */     }
/*      */ 
/*      */     
/* 2005 */     if (ModHelper.isModEnabled("Terminal")) {
/* 2006 */       AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(this, this, (AbstractPower)new PlatedArmorPower(this, 5), 5));
/*      */     }
/*      */     
/* 2009 */     (AbstractDungeon.getCurrRoom()).monsters.usePreBattleAction();
/* 2010 */     if (Settings.isFinalActAvailable && (AbstractDungeon.getCurrMapNode()).hasEmeraldKey) {
/* 2011 */       AbstractDungeon.getCurrRoom().applyEmeraldEliteBuff();
/*      */     }
/* 2013 */     AbstractDungeon.actionManager.addToTop((AbstractGameAction)new WaitAction(1.0F));
/*      */ 
/*      */     
/* 2016 */     applyPreCombatLogic();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<String> getRelicNames() {
/* 2025 */     ArrayList<String> arr = new ArrayList<>();
/* 2026 */     for (AbstractRelic relic : this.relics) {
/* 2027 */       arr.add(relic.relicId);
/*      */     }
/* 2029 */     return arr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCircletCount() {
/* 2038 */     int count = 0;
/* 2039 */     int counterSum = 0;
/* 2040 */     for (AbstractRelic relic : this.relics) {
/* 2041 */       if (relic.relicId.equals("Circlet")) {
/* 2042 */         count++;
/* 2043 */         counterSum += relic.counter;
/*      */       } 
/*      */     } 
/* 2046 */     if (counterSum > 0) {
/* 2047 */       return counterSum;
/*      */     }
/* 2049 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(int numCards) {
/* 2060 */     for (int i = 0; i < numCards; i++) {
/* 2061 */       if (!this.drawPile.isEmpty()) {
/* 2062 */         AbstractCard c = this.drawPile.getTopCard();
/* 2063 */         c.current_x = CardGroup.DRAW_PILE_X;
/* 2064 */         c.current_y = CardGroup.DRAW_PILE_Y;
/* 2065 */         c.setAngle(0.0F, true);
/* 2066 */         c.lighten(false);
/* 2067 */         c.drawScale = 0.12F;
/* 2068 */         c.targetDrawScale = 0.75F;
/* 2069 */         c.triggerWhenDrawn();
/*      */         
/* 2071 */         this.hand.addToHand(c);
/* 2072 */         this.drawPile.removeTopCard();
/*      */         
/* 2074 */         for (AbstractPower p : this.powers) {
/* 2075 */           p.onCardDraw(c);
/*      */         }
/*      */         
/* 2078 */         for (AbstractRelic r : this.relics) {
/* 2079 */           r.onCardDraw(c);
/*      */         }
/*      */       } else {
/*      */         
/* 2083 */         logger.info("ERROR: How did this happen? No cards in draw pile?? Player.java");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw() {
/* 2092 */     if (this.hand.size() == 10) {
/* 2093 */       createHandIsFullDialog();
/*      */       
/*      */       return;
/*      */     } 
/* 2097 */     CardCrawlGame.sound.playAV("CARD_DRAW_8", -0.12F, 0.25F);
/* 2098 */     draw(1);
/* 2099 */     onCardDrawOrDiscard();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void render(SpriteBatch sb) {
/* 2107 */     this.stance.render(sb);
/*      */     
/* 2109 */     if (((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT || 
/* 2110 */       AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoom) && !this.isDead) {
/* 2111 */       renderHealth(sb);
/*      */       
/* 2113 */       if (!this.orbs.isEmpty()) {
/* 2114 */         for (AbstractOrb o : this.orbs) {
/* 2115 */           o.render(sb);
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/* 2120 */     if (!(AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.RestRoom)) {
/* 2121 */       if (this.atlas == null || this.renderCorpse) {
/* 2122 */         sb.setColor(Color.WHITE);
/* 2123 */         sb.draw(this.img, this.drawX - this.img
/*      */             
/* 2125 */             .getWidth() * Settings.scale / 2.0F + this.animX, this.drawY, this.img
/*      */             
/* 2127 */             .getWidth() * Settings.scale, this.img
/* 2128 */             .getHeight() * Settings.scale, 0, 0, this.img
/*      */ 
/*      */             
/* 2131 */             .getWidth(), this.img
/* 2132 */             .getHeight(), this.flipHorizontal, this.flipVertical);
/*      */       }
/*      */       else {
/*      */         
/* 2136 */         renderPlayerImage(sb);
/*      */       } 
/*      */       
/* 2139 */       this.hb.render(sb);
/* 2140 */       this.healthHb.render(sb);
/*      */     } else {
/* 2142 */       sb.setColor(Color.WHITE);
/* 2143 */       renderShoulderImg(sb);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void renderShoulderImg(SpriteBatch sb) {
/* 2148 */     if (CampfireUI.hidden) {
/* 2149 */       sb.draw(this.shoulder2Img, 0.0F, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
/*      */     } else {
/* 2151 */       sb.draw(this.shoulderImg, this.animX, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void renderPlayerImage(SpriteBatch sb) {
/* 2156 */     if (this.atlas != null) {
/* 2157 */       this.state.update(Gdx.graphics.getDeltaTime());
/* 2158 */       this.state.apply(this.skeleton);
/* 2159 */       this.skeleton.updateWorldTransform();
/* 2160 */       this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
/*      */ 
/*      */       
/* 2163 */       this.skeleton.setColor(this.tint.color);
/* 2164 */       this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
/* 2165 */       sb.end();
/* 2166 */       CardCrawlGame.psb.begin();
/* 2167 */       sr.draw(CardCrawlGame.psb, this.skeleton);
/* 2168 */       CardCrawlGame.psb.end();
/* 2169 */       sb.begin();
/*      */     } else {
/* 2171 */       sb.setColor(Color.WHITE);
/* 2172 */       sb.draw(this.img, this.drawX - this.img
/*      */           
/* 2174 */           .getWidth() * Settings.scale / 2.0F + this.animX, this.drawY, this.img
/*      */           
/* 2176 */           .getWidth() * Settings.scale, this.img
/* 2177 */           .getHeight() * Settings.scale, 0, 0, this.img
/*      */ 
/*      */           
/* 2180 */           .getWidth(), this.img
/* 2181 */           .getHeight(), this.flipHorizontal, this.flipVertical);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renderPlayerBattleUi(SpriteBatch sb) {
/* 2191 */     if ((this.hb.hovered || this.healthHb.hovered) && !AbstractDungeon.isScreenUp) {
/* 2192 */       renderPowerTips(sb);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderPowerTips(SpriteBatch sb) {
/* 2198 */     ArrayList<PowerTip> tips = new ArrayList<>();
/*      */     
/* 2200 */     if (!this.stance.ID.equals("Neutral")) {
/* 2201 */       tips.add(new PowerTip(this.stance.name, this.stance.description));
/*      */     }
/*      */     
/* 2204 */     for (AbstractPower p : this.powers) {
/* 2205 */       if (p.region48 != null) {
/* 2206 */         tips.add(new PowerTip(p.name, p.description, p.region48)); continue;
/*      */       } 
/* 2208 */       tips.add(new PowerTip(p.name, p.description, p.img));
/*      */     } 
/*      */ 
/*      */     
/* 2212 */     if (!tips.isEmpty())
/*      */     {
/* 2214 */       if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
/* 2215 */         TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + 
/*      */             
/* 2217 */             TipHelper.calculateAdditionalOffset(tips, this.hb.cY), tips);
/*      */       } else {
/*      */         
/* 2220 */         TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + 
/*      */             
/* 2222 */             TipHelper.calculateAdditionalOffset(tips, this.hb.cY), tips);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderHand(SpriteBatch sb) {
/* 2229 */     if (Settings.SHOW_CARD_HOTKEYS) {
/* 2230 */       renderCardHotKeyText(sb);
/*      */     }
/*      */     
/* 2233 */     if (this.inspectMode && 
/* 2234 */       this.inspectHb != null) {
/* 2235 */       renderReticle(sb, this.inspectHb);
/*      */     }
/*      */ 
/*      */     
/* 2239 */     if (this.hoveredCard != null) {
/* 2240 */       int aliveMonsters = 0;
/* 2241 */       this.hand.renderHand(sb, this.hoveredCard);
/* 2242 */       this.hoveredCard.renderHoverShadow(sb);
/*      */       
/* 2244 */       if ((this.isDraggingCard || this.inSingleTargetMode) && this.isHoveringDropZone) {
/*      */         
/* 2246 */         if (this.isDraggingCard && !this.inSingleTargetMode) {
/* 2247 */           AbstractMonster theMonster = null;
/* 2248 */           for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
/* 2249 */             if (!m.isDying && m.currentHealth > 0) {
/* 2250 */               aliveMonsters++;
/* 2251 */               theMonster = m;
/*      */             } 
/*      */           } 
/*      */           
/* 2255 */           if (aliveMonsters == 1 && this.hoveredMonster == null) {
/* 2256 */             this.hoveredCard.calculateCardDamage(theMonster);
/* 2257 */             this.hoveredCard.render(sb);
/* 2258 */             this.hoveredCard.applyPowers();
/*      */           } else {
/* 2260 */             this.hoveredCard.render(sb);
/*      */           } 
/*      */         } 
/* 2263 */         if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
/* 2264 */           renderHoverReticle(sb);
/*      */         }
/*      */       } 
/*      */       
/* 2268 */       if (this.hoveredMonster != null) {
/* 2269 */         this.hoveredCard.calculateCardDamage(this.hoveredMonster);
/* 2270 */         this.hoveredCard.render(sb);
/* 2271 */         this.hoveredCard.applyPowers();
/* 2272 */       } else if (aliveMonsters != 1) {
/* 2273 */         this.hoveredCard.render(sb);
/*      */       }
/*      */     
/* 2276 */     } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
/* 2277 */       this.hand.render(sb);
/*      */     } else {
/* 2279 */       this.hand.renderHand(sb, this.cardInUse);
/*      */     } 
/*      */ 
/*      */     
/* 2283 */     if (this.cardInUse != null && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT && !PeekButton.isPeeking) {
/* 2284 */       this.cardInUse.render(sb);
/* 2285 */       if ((AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMBAT) {
/* 2286 */         AbstractDungeon.effectList.add(new CardDisappearEffect(this.cardInUse
/* 2287 */               .makeCopy(), this.cardInUse.current_x, this.cardInUse.current_y));
/* 2288 */         this.cardInUse = null;
/*      */       } 
/*      */     } 
/*      */     
/* 2292 */     this.limbo.render(sb);
/*      */     
/* 2294 */     if (this.inSingleTargetMode && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && 
/* 2295 */       !AbstractDungeon.getCurrRoom().isBattleEnding()) {
/* 2296 */       renderTargetingUi(sb);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderTargetingUi(SpriteBatch sb) {
/* 2306 */     this.arrowX = MathHelper.mouseLerpSnap(this.arrowX, InputHelper.mX);
/* 2307 */     this.arrowY = MathHelper.mouseLerpSnap(this.arrowY, InputHelper.mY);
/* 2308 */     this.controlPoint.x = this.hoveredCard.current_x - (this.arrowX - this.hoveredCard.current_x) / 4.0F;
/* 2309 */     this.controlPoint.y = this.arrowY + (this.arrowY - this.hoveredCard.current_y) / 2.0F;
/*      */     
/* 2311 */     if (this.hoveredMonster == null) {
/* 2312 */       this.arrowScale = Settings.scale;
/* 2313 */       this.arrowScaleTimer = 0.0F;
/* 2314 */       sb.setColor(Color.WHITE);
/*      */     } else {
/* 2316 */       this.arrowScaleTimer += Gdx.graphics.getDeltaTime();
/* 2317 */       if (this.arrowScaleTimer > 1.0F) {
/* 2318 */         this.arrowScaleTimer = 1.0F;
/*      */       }
/*      */       
/* 2321 */       this.arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, this.arrowScaleTimer);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2326 */       sb.setColor(ARROW_COLOR);
/*      */     } 
/*      */ 
/*      */     
/* 2330 */     this.controlPoint.x -= this.arrowX;
/* 2331 */     this.controlPoint.y -= this.arrowY;
/* 2332 */     this.arrowTmp.nor();
/*      */     
/* 2334 */     this.startArrowVector.x = this.hoveredCard.current_x;
/* 2335 */     this.startArrowVector.y = this.hoveredCard.current_y;
/* 2336 */     this.endArrowVector.x = this.arrowX;
/* 2337 */     this.endArrowVector.y = this.arrowY;
/* 2338 */     drawCurvedLine(sb, this.startArrowVector, this.endArrowVector, this.controlPoint);
/*      */     
/* 2340 */     sb.draw(ImageMaster.TARGET_UI_ARROW, this.arrowX - 128.0F, this.arrowY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.arrowScale, this.arrowScale, this.arrowTmp
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2350 */         .angle() + 90.0F, 0, 0, 256, 256, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control) {
/* 2360 */     float radius = 7.0F * Settings.scale;
/*      */     
/* 2362 */     for (int i = 0; i < this.points.length - 1; i++) {
/* 2363 */       this.points[i] = (Vector2)Bezier.quadratic((Vector)this.points[i], i / 20.0F, (Vector)start, (Vector)control, (Vector)end, (Vector)this.arrowTmp);
/* 2364 */       radius += 0.4F * Settings.scale;
/*      */       
/* 2366 */       if (i != 0) {
/* 2367 */         (this.points[i - 1]).x -= (this.points[i]).x;
/* 2368 */         (this.points[i - 1]).y -= (this.points[i]).y;
/* 2369 */         sb.draw(ImageMaster.TARGET_UI_CIRCLE, (this.points[i]).x - 64.0F, (this.points[i]).y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, this.arrowTmp
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2379 */             .nor().angle() + 90.0F, 0, 0, 128, 128, false, false);
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/* 2387 */         this.controlPoint.x -= (this.points[i]).x;
/* 2388 */         this.controlPoint.y -= (this.points[i]).y;
/* 2389 */         sb.draw(ImageMaster.TARGET_UI_CIRCLE, (this.points[i]).x - 64.0F, (this.points[i]).y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, this.arrowTmp
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2399 */             .nor().angle() + 270.0F, 0, 0, 128, 128, false, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void createHandIsFullDialog() {
/* 2411 */     AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[2], true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderHoverReticle(SpriteBatch sb) {
/* 2420 */     switch (this.hoveredCard.target) {
/*      */       case ENEMY:
/* 2422 */         if (this.inSingleTargetMode && this.hoveredMonster != null) {
/* 2423 */           this.hoveredMonster.renderReticle(sb);
/*      */         }
/*      */         break;
/*      */       case ALL_ENEMY:
/* 2427 */         (AbstractDungeon.getCurrRoom()).monsters.renderReticle(sb);
/*      */         break;
/*      */       case SELF:
/* 2430 */         renderReticle(sb);
/*      */         break;
/*      */       case SELF_AND_ENEMY:
/* 2433 */         renderReticle(sb);
/* 2434 */         if (this.inSingleTargetMode && this.hoveredMonster != null) {
/* 2435 */           this.hoveredMonster.renderReticle(sb);
/*      */         }
/*      */         break;
/*      */       case ALL:
/* 2439 */         renderReticle(sb);
/* 2440 */         (AbstractDungeon.getCurrRoom()).monsters.renderReticle(sb);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void applyPreCombatLogic() {
/* 2451 */     for (AbstractRelic r : this.relics) {
/* 2452 */       if (r != null) {
/* 2453 */         r.atPreBattle();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyStartOfCombatLogic() {
/* 2460 */     for (AbstractRelic r : this.relics) {
/* 2461 */       if (r != null) {
/* 2462 */         r.atBattleStart();
/*      */       }
/*      */     } 
/*      */     
/* 2466 */     for (AbstractBlight b : this.blights) {
/* 2467 */       if (b != null) {
/* 2468 */         b.atBattleStart();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyStartOfCombatPreDrawLogic() {
/* 2475 */     for (AbstractRelic r : this.relics) {
/* 2476 */       if (r != null) {
/* 2477 */         r.atBattleStartPreDraw();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyStartOfTurnRelics() {
/* 2484 */     this.stance.atStartOfTurn();
/*      */     
/* 2486 */     for (AbstractRelic r : this.relics) {
/* 2487 */       if (r != null) {
/* 2488 */         r.atTurnStart();
/*      */       }
/*      */     } 
/*      */     
/* 2492 */     for (AbstractBlight b : this.blights) {
/* 2493 */       if (b != null) {
/* 2494 */         b.atTurnStart();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void applyStartOfTurnPostDrawRelics() {
/* 2500 */     for (AbstractRelic r : this.relics) {
/* 2501 */       if (r != null) {
/* 2502 */         r.atTurnStartPostDraw();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void applyStartOfTurnPreDrawCards() {
/* 2508 */     for (AbstractCard c : this.hand.group) {
/* 2509 */       if (c != null) {
/* 2510 */         c.atTurnStartPreDraw();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyStartOfTurnCards() {
/* 2517 */     for (AbstractCard c : this.drawPile.group) {
/* 2518 */       if (c != null) {
/* 2519 */         c.atTurnStart();
/*      */       }
/*      */     } 
/* 2522 */     for (AbstractCard c : this.hand.group) {
/* 2523 */       if (c != null) {
/* 2524 */         c.atTurnStart();
/*      */       }
/*      */     } 
/* 2527 */     for (AbstractCard c : this.discardPile.group) {
/* 2528 */       if (c != null) {
/* 2529 */         c.atTurnStart();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onVictory() {
/* 2538 */     if (!this.isDying) {
/* 2539 */       for (AbstractRelic r : this.relics) {
/* 2540 */         r.onVictory();
/*      */       }
/* 2542 */       for (AbstractBlight b : this.blights) {
/* 2543 */         b.onVictory();
/*      */       }
/* 2545 */       for (AbstractPower p : this.powers) {
/* 2546 */         p.onVictory();
/*      */       }
/*      */     } 
/* 2549 */     this.damagedThisCombat = 0;
/*      */   }
/*      */   
/*      */   public enum PlayerClass {
/* 2553 */     IRONCLAD, THE_SILENT, DEFECT, WATCHER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasRelic(String targetID) {
/* 2563 */     for (AbstractRelic r : this.relics) {
/* 2564 */       if (r.relicId.equals(targetID)) {
/* 2565 */         return true;
/*      */       }
/*      */     } 
/* 2568 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasBlight(String targetID) {
/* 2578 */     for (AbstractBlight b : this.blights) {
/* 2579 */       if (b.blightID.equals(targetID)) {
/* 2580 */         return true;
/*      */       }
/*      */     } 
/* 2583 */     return false;
/*      */   }
/*      */   
/*      */   public boolean hasPotion(String id) {
/* 2587 */     for (AbstractPotion p : this.potions) {
/* 2588 */       if (p.ID.equals(id)) {
/* 2589 */         return true;
/*      */       }
/*      */     } 
/* 2592 */     return false;
/*      */   }
/*      */   
/*      */   public boolean hasAnyPotions() {
/* 2596 */     for (AbstractPotion p : this.potions) {
/* 2597 */       if (!(p instanceof PotionSlot)) {
/* 2598 */         return true;
/*      */       }
/*      */     } 
/* 2601 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loseRandomRelics(int amount) {
/* 2611 */     if (amount > this.relics.size()) {
/* 2612 */       for (AbstractRelic r : this.relics) {
/* 2613 */         r.onUnequip();
/*      */       }
/* 2615 */       this.relics.clear();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 2620 */     for (int i = 0; i < amount; i++) {
/* 2621 */       int index = MathUtils.random(0, this.relics.size() - 1);
/* 2622 */       ((AbstractRelic)this.relics.get(index)).onUnequip();
/* 2623 */       this.relics.remove(index);
/*      */     } 
/*      */     
/* 2626 */     reorganizeRelics();
/*      */   }
/*      */   
/*      */   public boolean loseRelic(String targetID) {
/* 2630 */     if (!hasRelic(targetID)) {
/* 2631 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2635 */     AbstractRelic toRemove = null;
/* 2636 */     for (AbstractRelic r : this.relics) {
/* 2637 */       if (r.relicId.equals(targetID)) {
/* 2638 */         r.onUnequip();
/* 2639 */         toRemove = r;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2644 */     if (toRemove == null) {
/* 2645 */       logger.info("WHY WAS RELIC: " + this.name + " NOT FOUND???");
/* 2646 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2650 */     this.relics.remove(toRemove);
/*      */     
/* 2652 */     reorganizeRelics();
/* 2653 */     return true;
/*      */   }
/*      */   
/*      */   public void reorganizeRelics() {
/* 2657 */     logger.info("Reorganizing relics");
/*      */     
/* 2659 */     ArrayList<AbstractRelic> tmpRelics = new ArrayList<>();
/* 2660 */     tmpRelics.addAll(this.relics);
/* 2661 */     this.relics.clear();
/*      */ 
/*      */     
/* 2664 */     for (int i = 0; i < tmpRelics.size(); i++) {
/* 2665 */       ((AbstractRelic)tmpRelics.get(i)).reorganizeObtain(this, i, false, tmpRelics.size());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractRelic getRelic(String targetID) {
/* 2673 */     for (AbstractRelic r : this.relics) {
/* 2674 */       if (r.relicId.equals(targetID)) {
/* 2675 */         return r;
/*      */       }
/*      */     } 
/* 2678 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractBlight getBlight(String targetID) {
/* 2685 */     for (AbstractBlight b : this.blights) {
/* 2686 */       if (b.blightID.equals(targetID)) {
/* 2687 */         return b;
/*      */       }
/*      */     } 
/* 2690 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void obtainPotion(int slot, AbstractPotion potionToObtain) {
/* 2700 */     if (slot > this.potionSlots) {
/*      */       return;
/*      */     }
/* 2703 */     this.potions.set(slot, potionToObtain);
/* 2704 */     potionToObtain.setAsObtained(slot);
/*      */   }
/*      */   
/*      */   public boolean obtainPotion(AbstractPotion potionToObtain) {
/* 2708 */     int index = 0;
/* 2709 */     for (AbstractPotion p : this.potions) {
/* 2710 */       if (p instanceof PotionSlot) {
/*      */         break;
/*      */       }
/* 2713 */       index++;
/*      */     } 
/*      */     
/* 2716 */     if (index < this.potionSlots) {
/* 2717 */       this.potions.set(index, potionToObtain);
/* 2718 */       potionToObtain.setAsObtained(index);
/* 2719 */       potionToObtain.flash();
/* 2720 */       AbstractPotion.playPotionSound();
/* 2721 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2725 */     logger.info("NOT ENOUGH POTION SLOTS");
/* 2726 */     AbstractDungeon.topPanel.flashRed();
/* 2727 */     return false;
/*      */   }
/*      */   
/*      */   public void renderRelics(SpriteBatch sb) {
/* 2731 */     for (int i = 0; i < this.relics.size(); i++) {
/* 2732 */       if (i / AbstractRelic.MAX_RELICS_PER_PAGE == AbstractRelic.relicPage) {
/* 2733 */         ((AbstractRelic)this.relics.get(i)).renderInTopPanel(sb);
/*      */       }
/*      */     } 
/*      */     
/* 2737 */     for (AbstractRelic r : this.relics) {
/* 2738 */       if (r.hb.hovered) {
/* 2739 */         r.renderTip(sb);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void renderBlights(SpriteBatch sb) {
/* 2745 */     for (AbstractBlight b : this.blights) {
/* 2746 */       b.renderInTopPanel(sb);
/*      */     }
/*      */     
/* 2749 */     for (AbstractBlight b : this.blights) {
/* 2750 */       if (b.hb.hovered) {
/* 2751 */         b.renderTip(sb);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void bottledCardUpgradeCheck(AbstractCard c) {
/* 2757 */     if (c.inBottleFlame && hasRelic("Bottled Flame")) {
/* 2758 */       ((BottledFlame)getRelic("Bottled Flame")).setDescriptionAfterLoading();
/*      */     }
/*      */     
/* 2761 */     if (c.inBottleLightning && hasRelic("Bottled Lightning")) {
/* 2762 */       ((BottledLightning)getRelic("Bottled Lightning")).setDescriptionAfterLoading();
/*      */     }
/*      */     
/* 2765 */     if (c.inBottleTornado && hasRelic("Bottled Tornado")) {
/* 2766 */       ((BottledTornado)getRelic("Bottled Tornado")).setDescriptionAfterLoading();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerEvokeAnimation(int slot) {
/* 2774 */     if (this.maxOrbs <= 0) {
/*      */       return;
/*      */     }
/* 2777 */     ((AbstractOrb)this.orbs.get(slot)).triggerEvokeAnimation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evokeOrb() {
/* 2784 */     if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
/* 2785 */       ((AbstractOrb)this.orbs.get(0)).onEvoke();
/* 2786 */       EmptyOrbSlot emptyOrbSlot = new EmptyOrbSlot();
/*      */       int i;
/* 2788 */       for (i = 1; i < this.orbs.size(); i++) {
/* 2789 */         Collections.swap(this.orbs, i, i - 1);
/*      */       }
/*      */       
/* 2792 */       this.orbs.set(this.orbs.size() - 1, emptyOrbSlot);
/*      */       
/* 2794 */       for (i = 0; i < this.orbs.size(); i++) {
/* 2795 */         ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evokeNewestOrb() {
/* 2804 */     if (!this.orbs.isEmpty() && !(this.orbs.get(this.orbs.size() - 1) instanceof EmptyOrbSlot)) {
/* 2805 */       ((AbstractOrb)this.orbs.get(this.orbs.size() - 1)).onEvoke();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evokeWithoutLosingOrb() {
/* 2813 */     if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
/* 2814 */       ((AbstractOrb)this.orbs.get(0)).onEvoke();
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeNextOrb() {
/* 2819 */     if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
/* 2820 */       EmptyOrbSlot emptyOrbSlot = new EmptyOrbSlot(((AbstractOrb)this.orbs.get(0)).cX, ((AbstractOrb)this.orbs.get(0)).cY);
/*      */       int i;
/* 2822 */       for (i = 1; i < this.orbs.size(); i++) {
/* 2823 */         Collections.swap(this.orbs, i, i - 1);
/*      */       }
/*      */       
/* 2826 */       this.orbs.set(this.orbs.size() - 1, emptyOrbSlot);
/*      */       
/* 2828 */       for (i = 0; i < this.orbs.size(); i++) {
/* 2829 */         ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasEmptyOrb() {
/* 2835 */     if (this.orbs.isEmpty()) {
/* 2836 */       return false;
/*      */     }
/*      */     
/* 2839 */     for (AbstractOrb o : this.orbs) {
/* 2840 */       if (o instanceof EmptyOrbSlot) {
/* 2841 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 2845 */     return false;
/*      */   }
/*      */   
/*      */   public boolean hasOrb() {
/* 2849 */     if (this.orbs.isEmpty()) {
/* 2850 */       return false;
/*      */     }
/*      */     
/* 2853 */     if (this.orbs.get(0) instanceof EmptyOrbSlot) {
/* 2854 */       return false;
/*      */     }
/*      */     
/* 2857 */     return true;
/*      */   }
/*      */   
/*      */   public int filledOrbCount() {
/* 2861 */     int orbCount = 0;
/* 2862 */     for (AbstractOrb o : this.orbs) {
/* 2863 */       if (!(o instanceof EmptyOrbSlot)) {
/* 2864 */         orbCount++;
/*      */       }
/*      */     } 
/* 2867 */     return orbCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void channelOrb(AbstractOrb orbToSet) {
/* 2874 */     if (this.maxOrbs <= 0) {
/* 2875 */       AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[4], true));
/*      */       
/*      */       return;
/*      */     } 
/* 2879 */     if (this.maxOrbs > 0) {
/*      */       Dark dark;
/* 2881 */       if (hasRelic("Dark Core") && !(orbToSet instanceof Dark)) {
/* 2882 */         dark = new Dark();
/*      */       }
/*      */       
/* 2885 */       int index = -1;
/* 2886 */       for (int i = 0; i < this.orbs.size(); i++) {
/* 2887 */         if (this.orbs.get(i) instanceof EmptyOrbSlot) {
/* 2888 */           index = i;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 2893 */       if (index != -1) {
/* 2894 */         ((AbstractOrb)dark).cX = ((AbstractOrb)this.orbs.get(index)).cX;
/* 2895 */         ((AbstractOrb)dark).cY = ((AbstractOrb)this.orbs.get(index)).cY;
/* 2896 */         this.orbs.set(index, dark);
/* 2897 */         ((AbstractOrb)this.orbs.get(index)).setSlot(index, this.maxOrbs);
/* 2898 */         dark.playChannelSFX();
/*      */         
/* 2900 */         for (AbstractPower p : this.powers) {
/* 2901 */           p.onChannel((AbstractOrb)dark);
/*      */         }
/*      */         
/* 2904 */         AbstractDungeon.actionManager.orbsChanneledThisCombat.add(dark);
/*      */ 
/*      */         
/* 2907 */         AbstractDungeon.actionManager.orbsChanneledThisTurn.add(dark);
/* 2908 */         int plasmaCount = 0;
/* 2909 */         for (AbstractOrb o : AbstractDungeon.actionManager.orbsChanneledThisTurn) {
/* 2910 */           if (o instanceof com.megacrit.cardcrawl.orbs.Plasma) {
/* 2911 */             plasmaCount++;
/*      */           }
/*      */         } 
/* 2914 */         if (plasmaCount == 9) {
/* 2915 */           UnlockTracker.unlockAchievement("NEON");
/*      */         }
/*      */ 
/*      */         
/* 2919 */         dark.applyFocus();
/*      */       } else {
/* 2921 */         AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ChannelAction((AbstractOrb)dark));
/* 2922 */         AbstractDungeon.actionManager.addToTop((AbstractGameAction)new EvokeOrbAction(1));
/* 2923 */         AbstractDungeon.actionManager.addToTop((AbstractGameAction)new AnimateOrbAction(1));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void increaseMaxOrbSlots(int amount, boolean playSfx) {
/* 2930 */     if (this.maxOrbs == 10) {
/* 2931 */       AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[3], true));
/*      */       
/*      */       return;
/*      */     } 
/* 2935 */     if (playSfx) {
/* 2936 */       CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1F);
/*      */     }
/* 2938 */     this.maxOrbs += amount; int i;
/* 2939 */     for (i = 0; i < amount; i++) {
/* 2940 */       this.orbs.add(new EmptyOrbSlot());
/*      */     }
/* 2942 */     for (i = 0; i < this.orbs.size(); i++) {
/* 2943 */       ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
/*      */     }
/*      */   }
/*      */   
/*      */   public void decreaseMaxOrbSlots(int amount) {
/* 2948 */     if (this.maxOrbs <= 0) {
/*      */       return;
/*      */     }
/*      */     
/* 2952 */     this.maxOrbs -= amount;
/*      */     
/* 2954 */     if (this.maxOrbs < 0) {
/* 2955 */       this.maxOrbs = 0;
/*      */     }
/*      */     
/* 2958 */     if (!this.orbs.isEmpty()) {
/* 2959 */       this.orbs.remove(this.orbs.size() - 1);
/*      */     }
/*      */     
/* 2962 */     for (int i = 0; i < this.orbs.size(); i++) {
/* 2963 */       ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
/*      */     }
/*      */   }
/*      */   
/*      */   public void applyStartOfTurnOrbs() {
/* 2968 */     if (!this.orbs.isEmpty()) {
/* 2969 */       for (AbstractOrb o : this.orbs) {
/* 2970 */         o.onStartOfTurn();
/*      */       }
/*      */       
/* 2973 */       if (hasRelic("Cables") && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
/* 2974 */         ((AbstractOrb)this.orbs.get(0)).onStartOfTurn();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateEscapeAnimation() {
/* 2983 */     if (this.escapeTimer != 0.0F) {
/* 2984 */       this.escapeTimer -= Gdx.graphics.getDeltaTime();
/* 2985 */       if (this.flipHorizontal) {
/* 2986 */         this.drawX -= Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
/*      */       } else {
/* 2988 */         this.drawX += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
/*      */       } 
/*      */     } 
/* 2991 */     if (this.escapeTimer < 0.0F) {
/* 2992 */       AbstractDungeon.getCurrRoom().endBattle();
/* 2993 */       this.flipHorizontal = false;
/* 2994 */       this.isEscaping = false;
/* 2995 */       this.escapeTimer = 0.0F;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean relicsDoneAnimating() {
/* 3000 */     for (AbstractRelic r : this.relics) {
/* 3001 */       if (!r.isDone) {
/* 3002 */         return false;
/*      */       }
/*      */     } 
/* 3005 */     return true;
/*      */   }
/*      */   
/*      */   public void resetControllerValues() {
/* 3009 */     if (Settings.isControllerMode) {
/* 3010 */       this.toHover = null;
/* 3011 */       this.hoveredCard = null;
/* 3012 */       this.inspectMode = false;
/* 3013 */       this.inspectHb = null;
/* 3014 */       this.keyboardCardIndex = -1;
/* 3015 */       this.hand.refreshHandLayout();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractPotion getRandomPotion() {
/* 3025 */     ArrayList<AbstractPotion> list = new ArrayList<>();
/* 3026 */     for (AbstractPotion p : this.potions) {
/* 3027 */       if (!(p instanceof PotionSlot)) {
/* 3028 */         list.add(p);
/*      */       }
/*      */     } 
/*      */     
/* 3032 */     if (list.isEmpty()) {
/* 3033 */       return null;
/*      */     }
/*      */     
/* 3036 */     Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));
/* 3037 */     return list.get(0);
/*      */   }
/*      */   
/*      */   public void removePotion(AbstractPotion potionOption) {
/* 3041 */     int slot = this.potions.indexOf(potionOption);
/* 3042 */     if (slot >= 0) {
/* 3043 */       this.potions.set(slot, new PotionSlot(slot));
/*      */     }
/*      */   }
/*      */   
/*      */   public void movePosition(float x, float y) {
/* 3048 */     this.drawX = x;
/* 3049 */     this.drawY = y;
/* 3050 */     this.dialogX = this.drawX + 0.0F * Settings.scale;
/* 3051 */     this.dialogY = this.drawY + 170.0F * Settings.scale;
/* 3052 */     this.animX = 0.0F;
/* 3053 */     this.animY = 0.0F;
/* 3054 */     refreshHitboxLocation();
/*      */   }
/*      */   
/*      */   public void switchedStance() {
/* 3058 */     for (AbstractCard c : this.hand.group) {
/* 3059 */       c.switchedStance();
/*      */     }
/* 3061 */     for (AbstractCard c : this.discardPile.group) {
/* 3062 */       c.switchedStance();
/*      */     }
/* 3064 */     for (AbstractCard c : this.drawPile.group) {
/* 3065 */       c.switchedStance();
/*      */     }
/*      */   }
/*      */   
/*      */   public CharacterOption getCharacterSelectOption() {
/* 3070 */     return null;
/*      */   }
/*      */   
/*      */   public void onStanceChange(String id) {}
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\characters\AbstractPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */