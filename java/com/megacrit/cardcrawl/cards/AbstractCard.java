/*      */ package com.megacrit.cardcrawl.cards;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*      */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*      */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*      */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*      */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*      */ import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
/*      */ import com.megacrit.cardcrawl.blights.AbstractBlight;
/*      */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*      */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*      */ import com.megacrit.cardcrawl.core.OverlayMenu;
/*      */ import com.megacrit.cardcrawl.core.Settings;
/*      */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*      */ import com.megacrit.cardcrawl.helpers.CardHelper;
/*      */ import com.megacrit.cardcrawl.helpers.FontHelper;
/*      */ import com.megacrit.cardcrawl.helpers.GameDataStringBuilder;
/*      */ import com.megacrit.cardcrawl.helpers.GameDictionary;
/*      */ import com.megacrit.cardcrawl.helpers.Hitbox;
/*      */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*      */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*      */ import com.megacrit.cardcrawl.helpers.TipHelper;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*      */ import com.megacrit.cardcrawl.localization.LocalizedStrings;
/*      */ import com.megacrit.cardcrawl.localization.UIStrings;
/*      */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*      */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*      */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*      */ import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*      */ import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
/*      */ import com.megacrit.cardcrawl.stances.AbstractStance;
/*      */ import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
/*      */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*      */ import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
/*      */ import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
/*      */ import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.UUID;
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
/*      */ public abstract class AbstractCard
/*      */   implements Comparable<AbstractCard>
/*      */ {
/*   64 */   private static final Logger logger = LogManager.getLogger(AbstractCard.class.getName()); public CardType type;
/*      */   public int cost;
/*      */   public int costForTurn;
/*      */   public int price;
/*   68 */   public int chargeCost = -1; public boolean isCostModified = false; public boolean isCostModifiedForTurn = false;
/*      */   public boolean retain = false;
/*      */   public boolean selfRetain = false;
/*      */   public boolean dontTriggerOnUseCard = false;
/*      */   public CardRarity rarity;
/*      */   public CardColor color;
/*      */   public boolean isInnate = false;
/*      */   public boolean isLocked = false;
/*      */   public boolean showEvokeValue = false;
/*   77 */   public int showEvokeOrbCount = 0;
/*      */   
/*   79 */   public ArrayList<String> keywords = new ArrayList<>();
/*      */   private static final int COMMON_CARD_PRICE = 50;
/*      */   private static final int UNCOMMON_CARD_PRICE = 75;
/*      */   private static final int RARE_CARD_PRICE = 150;
/*      */   protected boolean isUsed = false;
/*      */   public boolean upgraded = false;
/*   85 */   public int timesUpgraded = 0; public int misc = 0; public int energyOnUse;
/*      */   public boolean ignoreEnergyOnUse = false;
/*      */   public boolean isSeen = true;
/*      */   public boolean upgradedCost = false;
/*      */   public boolean upgradedDamage = false;
/*      */   public boolean upgradedBlock = false;
/*      */   public boolean upgradedMagicNumber = false;
/*      */   public UUID uuid;
/*      */   public boolean isSelected = false;
/*      */   public boolean exhaust = false, returnToHand = false, shuffleBackIntoDrawPile = false;
/*      */   public boolean isEthereal = false;
/*   96 */   public ArrayList<CardTags> tags = new ArrayList<>();
/*      */   public int[] multiDamage;
/*      */   protected boolean isMultiDamage = false;
/*   99 */   public int baseDamage = -1, baseBlock = -1, baseMagicNumber = -1, baseHeal = -1, baseDraw = -1, baseDiscard = -1;
/*  100 */   public int damage = -1; public int block = -1; public int magicNumber = -1; public int heal = -1; public int draw = -1; public int discard = -1;
/*      */   public boolean isDamageModified = false;
/*      */   public boolean isBlockModified = false;
/*      */   public boolean isMagicNumberModified = false;
/*      */   protected DamageInfo.DamageType damageType;
/*      */   public DamageInfo.DamageType damageTypeForTurn;
/*  106 */   public CardTarget target = CardTarget.ENEMY; public boolean purgeOnUse = false; public boolean exhaustOnUseOnce = false; public boolean exhaustOnFire = false; public boolean freeToPlayOnce = false;
/*      */   public boolean isInAutoplay = false;
/*      */   private static TextureAtlas orbAtlas;
/*      */   private static TextureAtlas cardAtlas;
/*      */   private static TextureAtlas oldCardAtlas;
/*      */   public static TextureAtlas.AtlasRegion orb_red;
/*      */   public static TextureAtlas.AtlasRegion orb_green;
/*      */   public static TextureAtlas.AtlasRegion orb_blue;
/*      */   public static TextureAtlas.AtlasRegion orb_purple;
/*      */   public static TextureAtlas.AtlasRegion orb_card;
/*      */   public static TextureAtlas.AtlasRegion orb_potion;
/*      */   public static TextureAtlas.AtlasRegion orb_relic;
/*      */   public static TextureAtlas.AtlasRegion orb_special;
/*      */   public TextureAtlas.AtlasRegion portrait;
/*      */   public TextureAtlas.AtlasRegion jokePortrait;
/*  121 */   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
/*  122 */   public static final String[] TEXT = uiStrings.TEXT; public static float typeWidthAttack;
/*      */   public static float typeWidthSkill;
/*      */   public static float typeWidthPower;
/*      */   public static float typeWidthCurse;
/*      */   public static float typeWidthStatus;
/*      */   public static float typeOffsetAttack;
/*  128 */   public float transparency = 1.0F; public static float typeOffsetSkill; public static float typeOffsetPower; public static float typeOffsetCurse; public static float typeOffsetStatus; public AbstractGameEffect flashVfx; public String assetUrl; public boolean fadingOut = false;
/*  129 */   public float targetTransparency = 1.0F;
/*  130 */   private Color goldColor = Settings.GOLD_COLOR.cpy();
/*  131 */   private Color renderColor = Color.WHITE.cpy();
/*  132 */   private Color textColor = Settings.CREAM_COLOR.cpy();
/*  133 */   private Color typeColor = new Color(0.35F, 0.35F, 0.35F, 0.0F);
/*  134 */   public float targetAngle = 0.0F; private static final float NAME_OFFSET_Y = 175.0F; private static final float ENERGY_TEXT_OFFSET_X = -132.0F;
/*      */   private static final float ENERGY_TEXT_OFFSET_Y = 192.0F;
/*      */   private static final int W = 512;
/*  137 */   public float angle = 0.0F;
/*      */ 
/*      */   
/*  140 */   private ArrayList<CardGlowBorder> glowList = new ArrayList<>();
/*  141 */   private float glowTimer = 0.0F;
/*      */   public boolean isGlowing = false;
/*      */   public static final float SMALL_SCALE = 0.7F;
/*      */   public static final int RAW_W = 300;
/*      */   public static final int RAW_H = 420;
/*  146 */   public static final float IMG_WIDTH = 300.0F * Settings.scale, IMG_HEIGHT = 420.0F * Settings.scale;
/*  147 */   public static final float IMG_WIDTH_S = 300.0F * Settings.scale * 0.7F; public static final float IMG_HEIGHT_S = 420.0F * Settings.scale * 0.7F;
/*      */   
/*  149 */   private static final float SHADOW_OFFSET_X = 18.0F * Settings.scale;
/*  150 */   private static final float SHADOW_OFFSET_Y = 14.0F * Settings.scale; public float current_x; public float current_y; public float target_x;
/*      */   public float target_y;
/*  152 */   protected Texture portraitImg = null;
/*      */   private boolean useSmallTitleFont = false;
/*  154 */   public float drawScale = 0.7F; public float targetDrawScale = 0.7F; private static final int PORTRAIT_WIDTH = 250;
/*      */   private static final int PORTRAIT_HEIGHT = 190;
/*      */   private static final float PORTRAIT_OFFSET_Y = 72.0F;
/*      */   private static final float LINE_SPACING = 1.45F;
/*      */   public boolean isFlipped = false;
/*      */   private boolean darken = false;
/*  160 */   private float darkTimer = 0.0F;
/*      */   
/*      */   private static final float DARKEN_TIME = 0.3F;
/*      */   
/*  164 */   public Hitbox hb = new Hitbox(IMG_WIDTH_S, IMG_HEIGHT_S);
/*  165 */   private static final float HB_W = 300.0F * Settings.scale; private static final float HB_H = 420.0F * Settings.scale;
/*  166 */   public float hoverTimer = 0.0F; private boolean renderTip = false;
/*      */   private boolean hovered = false;
/*  168 */   private float hoverDuration = 0.0F;
/*      */   private static final float HOVER_TIP_TIME = 0.2F;
/*  170 */   private static final GlyphLayout gl = new GlyphLayout();
/*  171 */   private static final StringBuilder sbuilder = new StringBuilder();
/*  172 */   private static final StringBuilder sbuilder2 = new StringBuilder();
/*      */   
/*  174 */   public AbstractCard cardsToPreview = null;
/*      */   
/*      */   protected static final float CARD_TIP_PAD = 16.0F;
/*      */   
/*  178 */   public float newGlowTimer = 0.0F; public String originalName;
/*      */   public String name;
/*      */   public String rawDescription;
/*      */   public String cardID;
/*  182 */   public ArrayList<DescriptionLine> description = new ArrayList<>();
/*      */   public String cantUseMessage;
/*      */   private static final float TYPE_OFFSET_Y = -1.0F;
/*  185 */   private static final float DESC_OFFSET_Y = Settings.BIG_TEXT_MODE ? (IMG_HEIGHT * 0.24F) : (IMG_HEIGHT * 0.255F);
/*      */   private static final float DESC_OFFSET_Y2 = -6.0F;
/*  187 */   private static final float DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? (IMG_WIDTH * 0.95F) : (IMG_WIDTH * 0.79F);
/*  188 */   private static final float CN_DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? (IMG_WIDTH * 0.87F) : (IMG_WIDTH * 0.72F);
/*  189 */   private static final float TITLE_BOX_WIDTH = IMG_WIDTH * 0.6F;
/*  190 */   private static final float TITLE_BOX_WIDTH_NO_COST = IMG_WIDTH * 0.7F;
/*  191 */   private static final float CARD_ENERGY_IMG_WIDTH = 24.0F * Settings.scale;
/*  192 */   private static final float MAGIC_NUM_W = 20.0F * Settings.scale;
/*  193 */   private static final UIStrings cardRenderStrings = CardCrawlGame.languagePack.getUIString("AbstractCard");
/*  194 */   public static final String LOCKED_STRING = cardRenderStrings.TEXT[0];
/*  195 */   public static final String UNKNOWN_STRING = cardRenderStrings.TEXT[1]; private Color bgColor; private Color backColor; private Color frameColor; private Color frameOutlineColor; private Color frameShadowColor;
/*      */   private Color imgFrameColor;
/*      */   private Color descBoxColor;
/*      */   private Color bannerColor;
/*      */   private Color tintColor;
/*  200 */   private static final Color ENERGY_COST_RESTRICTED_COLOR = new Color(1.0F, 0.3F, 0.3F, 1.0F);
/*  201 */   private static final Color ENERGY_COST_MODIFIED_COLOR = new Color(0.4F, 1.0F, 0.4F, 1.0F);
/*  202 */   private static final Color FRAME_SHADOW_COLOR = new Color(0.0F, 0.0F, 0.0F, 0.25F);
/*  203 */   private static final Color IMG_FRAME_COLOR_COMMON = CardHelper.getColor(53, 58, 64);
/*  204 */   private static final Color IMG_FRAME_COLOR_UNCOMMON = CardHelper.getColor(119, 152, 161);
/*  205 */   private static final Color IMG_FRAME_COLOR_RARE = new Color(0.855F, 0.557F, 0.32F, 1.0F);
/*      */   
/*  207 */   private static final Color HOVER_IMG_COLOR = new Color(1.0F, 0.815F, 0.314F, 0.8F);
/*  208 */   private static final Color SELECTED_CARD_COLOR = new Color(0.5F, 0.9F, 0.9F, 1.0F);
/*  209 */   private static final Color BANNER_COLOR_COMMON = CardHelper.getColor(131, 129, 121);
/*  210 */   private static final Color BANNER_COLOR_UNCOMMON = CardHelper.getColor(142, 196, 213);
/*  211 */   private static final Color BANNER_COLOR_RARE = new Color(1.0F, 0.796F, 0.251F, 1.0F);
/*      */ 
/*      */   
/*  214 */   private static final Color CURSE_BG_COLOR = CardHelper.getColor(29, 29, 29);
/*  215 */   private static final Color CURSE_TYPE_BACK_COLOR = new Color(0.23F, 0.23F, 0.23F, 1.0F);
/*  216 */   private static final Color CURSE_FRAME_COLOR = CardHelper.getColor(21, 2, 21);
/*  217 */   private static final Color CURSE_DESC_BOX_COLOR = CardHelper.getColor(52, 58, 64);
/*      */ 
/*      */   
/*  220 */   private static final Color COLORLESS_BG_COLOR = new Color(0.15F, 0.15F, 0.15F, 1.0F);
/*  221 */   private static final Color COLORLESS_TYPE_BACK_COLOR = new Color(0.23F, 0.23F, 0.23F, 1.0F);
/*  222 */   private static final Color COLORLESS_FRAME_COLOR = new Color(0.48F, 0.48F, 0.48F, 1.0F);
/*  223 */   private static final Color COLORLESS_DESC_BOX_COLOR = new Color(0.351F, 0.363F, 0.3745F, 1.0F);
/*      */ 
/*      */   
/*  226 */   private static final Color RED_BG_COLOR = CardHelper.getColor(50, 26, 26);
/*  227 */   private static final Color RED_TYPE_BACK_COLOR = CardHelper.getColor(91, 43, 32);
/*  228 */   private static final Color RED_FRAME_COLOR = CardHelper.getColor(121, 12, 28);
/*  229 */   private static final Color RED_RARE_OUTLINE_COLOR = new Color(1.0F, 0.75F, 0.43F, 1.0F);
/*  230 */   private static final Color RED_DESC_BOX_COLOR = CardHelper.getColor(53, 58, 64);
/*      */ 
/*      */   
/*  233 */   private static final Color GREEN_BG_COLOR = CardHelper.getColor(19, 45, 40);
/*  234 */   private static final Color GREEN_TYPE_BACK_COLOR = CardHelper.getColor(32, 91, 43);
/*  235 */   private static final Color GREEN_FRAME_COLOR = CardHelper.getColor(52, 123, 8);
/*  236 */   private static final Color GREEN_RARE_OUTLINE_COLOR = new Color(1.0F, 0.75F, 0.43F, 1.0F);
/*  237 */   private static final Color GREEN_DESC_BOX_COLOR = CardHelper.getColor(53, 58, 64);
/*      */ 
/*      */   
/*  240 */   private static final Color BLUE_BG_COLOR = CardHelper.getColor(19, 45, 40);
/*  241 */   private static final Color BLUE_TYPE_BACK_COLOR = CardHelper.getColor(32, 91, 43);
/*  242 */   private static final Color BLUE_FRAME_COLOR = CardHelper.getColor(52, 123, 8);
/*  243 */   private static final Color BLUE_RARE_OUTLINE_COLOR = new Color(1.0F, 0.75F, 0.43F, 1.0F);
/*  244 */   private static final Color BLUE_DESC_BOX_COLOR = CardHelper.getColor(53, 58, 64);
/*      */   
/*  246 */   protected static final Color BLUE_BORDER_GLOW_COLOR = new Color(0.2F, 0.9F, 1.0F, 0.25F);
/*  247 */   protected static final Color GREEN_BORDER_GLOW_COLOR = new Color(0.0F, 1.0F, 0.0F, 0.25F);
/*  248 */   protected static final Color GOLD_BORDER_GLOW_COLOR = Color.GOLD.cpy();
/*      */   
/*      */   public boolean inBottleFlame = false;
/*      */   
/*      */   public boolean inBottleLightning = false;
/*      */   public boolean inBottleTornado = false;
/*  254 */   public Color glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
/*      */   
/*      */   public enum CardTarget {
/*  257 */     ENEMY, ALL_ENEMY, SELF, NONE, SELF_AND_ENEMY, ALL;
/*      */   }
/*      */   
/*      */   public enum CardColor {
/*  261 */     RED, GREEN, BLUE, PURPLE, COLORLESS, CURSE;
/*      */   }
/*      */   
/*      */   public enum CardRarity {
/*  265 */     BASIC, SPECIAL, COMMON, UNCOMMON, RARE, CURSE;
/*      */   }
/*      */   
/*      */   public enum CardType {
/*  269 */     ATTACK, SKILL, POWER, STATUS, CURSE;
/*      */   }
/*      */   
/*      */   public enum CardTags {
/*  273 */     HEALING, STRIKE, EMPTY, STARTER_DEFEND, STARTER_STRIKE;
/*      */   }
/*      */   
/*      */   public boolean isStarterStrike() {
/*  277 */     return (hasTag(CardTags.STRIKE) && this.rarity.equals(CardRarity.BASIC));
/*      */   }
/*      */   
/*      */   public boolean isStarterDefend() {
/*  281 */     return (hasTag(CardTags.STARTER_DEFEND) && this.rarity.equals(CardRarity.BASIC));
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
/*      */   public AbstractCard(String id, String name, String imgUrl, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
/*  294 */     this(id, name, imgUrl, cost, rawDescription, type, color, rarity, target, DamageInfo.DamageType.NORMAL);
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
/*      */   public AbstractCard(String id, String name, String deprecatedJokeUrl, String imgUrl, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
/*  322 */     this(id, name, imgUrl, cost, rawDescription, type, color, rarity, target, DamageInfo.DamageType.NORMAL);
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
/*      */   public AbstractCard(String id, String name, String deprecatedJokeUrl, String imgUrl, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, DamageInfo.DamageType dType) {
/*  351 */     this(id, name, imgUrl, cost, rawDescription, type, color, rarity, target, dType);
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
/*      */   public AbstractCard(String id, String name, String imgUrl, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, DamageInfo.DamageType dType) {
/*  366 */     this.originalName = name;
/*  367 */     this.name = name;
/*  368 */     this.cardID = id;
/*      */ 
/*      */     
/*  371 */     this.assetUrl = imgUrl;
/*  372 */     this.portrait = cardAtlas.findRegion(imgUrl);
/*  373 */     this.jokePortrait = oldCardAtlas.findRegion(imgUrl);
/*  374 */     if (this.portrait == null) {
/*  375 */       if (this.jokePortrait != null) {
/*  376 */         this.portrait = this.jokePortrait;
/*      */       } else {
/*  378 */         this.portrait = cardAtlas.findRegion("status/beta");
/*      */       } 
/*      */     }
/*      */     
/*  382 */     this.cost = cost;
/*  383 */     this.costForTurn = cost;
/*  384 */     this.rawDescription = rawDescription;
/*  385 */     this.type = type;
/*  386 */     this.color = color;
/*  387 */     this.rarity = rarity;
/*  388 */     this.target = target;
/*  389 */     this.damageType = dType;
/*  390 */     this.damageTypeForTurn = dType;
/*  391 */     createCardImage();
/*      */     
/*  393 */     if (name == null || rawDescription == null) {
/*  394 */       logger.info("Card initialized incorrecty");
/*      */     }
/*      */     
/*  397 */     initializeTitle();
/*  398 */     initializeDescription();
/*  399 */     updateTransparency();
/*  400 */     this.uuid = UUID.randomUUID();
/*      */   }
/*      */   
/*      */   public static void initialize() {
/*  404 */     long startTime = System.currentTimeMillis();
/*  405 */     cardAtlas = new TextureAtlas(Gdx.files.internal("cards/cards.atlas"));
/*  406 */     oldCardAtlas = new TextureAtlas(Gdx.files.internal("oldCards/cards.atlas"));
/*  407 */     orbAtlas = new TextureAtlas(Gdx.files.internal("orbs/orb.atlas"));
/*  408 */     orb_red = orbAtlas.findRegion("red");
/*  409 */     orb_green = orbAtlas.findRegion("green");
/*  410 */     orb_blue = orbAtlas.findRegion("blue");
/*  411 */     orb_purple = orbAtlas.findRegion("purple");
/*  412 */     orb_card = orbAtlas.findRegion("card");
/*  413 */     orb_potion = orbAtlas.findRegion("potion");
/*  414 */     orb_relic = orbAtlas.findRegion("relic");
/*  415 */     orb_special = orbAtlas.findRegion("special");
/*      */     
/*  417 */     logger.info("Card Image load time: " + (System.currentTimeMillis() - startTime) + "ms");
/*      */   }
/*      */   
/*      */   public static void initializeDynamicFrameWidths() {
/*  421 */     float d = 48.0F * Settings.scale;
/*      */ 
/*      */     
/*  424 */     gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[0]);
/*  425 */     typeOffsetAttack = (gl.width - 48.0F * Settings.scale) / 2.0F;
/*  426 */     typeWidthAttack = (gl.width / d - 1.0F) * 2.0F + 1.0F;
/*      */ 
/*      */     
/*  429 */     gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[1]);
/*  430 */     typeOffsetSkill = (gl.width - 48.0F * Settings.scale) / 2.0F;
/*  431 */     typeWidthSkill = (gl.width / d - 1.0F) * 2.0F + 1.0F;
/*      */ 
/*      */     
/*  434 */     gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[2]);
/*  435 */     typeOffsetPower = (gl.width - 48.0F * Settings.scale) / 2.0F;
/*  436 */     typeWidthPower = (gl.width / d - 1.0F) * 2.0F + 1.0F;
/*      */ 
/*      */     
/*  439 */     gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[3]);
/*  440 */     typeOffsetCurse = (gl.width - 48.0F * Settings.scale) / 2.0F;
/*  441 */     typeWidthCurse = (gl.width / d - 1.0F) * 2.0F + 1.0F;
/*      */ 
/*      */     
/*  444 */     gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[7]);
/*  445 */     typeOffsetStatus = (gl.width - 48.0F * Settings.scale) / 2.0F;
/*  446 */     typeWidthStatus = (gl.width / d - 1.0F) * 2.0F + 1.0F;
/*      */   }
/*      */   
/*      */   protected void initializeTitle() {
/*  450 */     FontHelper.cardTitleFont.getData().setScale(1.0F);
/*  451 */     gl.setText(FontHelper.cardTitleFont, this.name, Color.WHITE, 0.0F, 1, false);
/*  452 */     if (this.cost > 0 || this.cost == -1) {
/*  453 */       if (gl.width > TITLE_BOX_WIDTH) {
/*  454 */         this.useSmallTitleFont = true;
/*      */       }
/*      */     }
/*  457 */     else if (gl.width > TITLE_BOX_WIDTH_NO_COST) {
/*  458 */       this.useSmallTitleFont = true;
/*      */     } 
/*      */     
/*  461 */     gl.reset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initializeDescription() {
/*  469 */     this.keywords.clear();
/*  470 */     if (Settings.lineBreakViaCharacter) {
/*  471 */       initializeDescriptionCN();
/*      */       
/*      */       return;
/*      */     } 
/*  475 */     this.description.clear();
/*  476 */     int numLines = 1;
/*      */ 
/*      */     
/*  479 */     sbuilder.setLength(0);
/*  480 */     float currentWidth = 0.0F;
/*      */ 
/*      */     
/*  483 */     for (String word : this.rawDescription.split(" ")) {
/*  484 */       boolean isKeyword = false;
/*      */ 
/*      */       
/*  487 */       sbuilder2.setLength(0);
/*  488 */       sbuilder2.append(" ");
/*  489 */       if (word.length() > 0 && word.charAt(word.length() - 1) != ']' && !Character.isLetterOrDigit(word
/*  490 */           .charAt(word.length() - 1))) {
/*  491 */         sbuilder2.insert(0, word.charAt(word.length() - 1));
/*  492 */         word = word.substring(0, word.length() - 1);
/*      */       } 
/*      */ 
/*      */       
/*  496 */       String keywordTmp = word.toLowerCase();
/*  497 */       keywordTmp = dedupeKeyword(keywordTmp);
/*      */       
/*  499 */       if (GameDictionary.keywords.containsKey(keywordTmp)) {
/*  500 */         if (!this.keywords.contains(keywordTmp)) {
/*  501 */           this.keywords.add(keywordTmp);
/*      */         }
/*      */         
/*  504 */         gl.reset();
/*  505 */         gl.setText(FontHelper.cardDescFont_N, sbuilder2);
/*  506 */         float tmp = gl.width;
/*  507 */         gl.setText(FontHelper.cardDescFont_N, word);
/*  508 */         gl.width += tmp;
/*  509 */         isKeyword = true;
/*      */       
/*      */       }
/*  512 */       else if (!word.isEmpty() && word.charAt(0) == '[') {
/*  513 */         gl.reset();
/*  514 */         gl.setText(FontHelper.cardDescFont_N, sbuilder2);
/*  515 */         gl.width += CARD_ENERGY_IMG_WIDTH;
/*      */         
/*  517 */         switch (this.color) {
/*      */           case BASIC:
/*  519 */             if (!this.keywords.contains("[R]")) {
/*  520 */               this.keywords.add("[R]");
/*      */             }
/*      */             break;
/*      */           case CURSE:
/*  524 */             if (!this.keywords.contains("[G]")) {
/*  525 */               this.keywords.add("[G]");
/*      */             }
/*      */             break;
/*      */           case SPECIAL:
/*  529 */             if (!this.keywords.contains("[B]")) {
/*  530 */               this.keywords.add("[B]");
/*      */             }
/*      */             break;
/*      */           case COMMON:
/*  534 */             if (!this.keywords.contains("[W]")) {
/*  535 */               this.keywords.add("[W]");
/*      */             }
/*      */             break;
/*      */           case UNCOMMON:
/*  539 */             if (word.equals("[W]") && !this.keywords.contains("[W]")) {
/*  540 */               this.keywords.add("[W]");
/*      */             }
/*      */             break;
/*      */           
/*      */           default:
/*  545 */             logger.info("ERROR: Tried to display an invalid energy type: " + this.color.name());
/*      */             break;
/*      */         } 
/*      */ 
/*      */       
/*  550 */       } else if (word.equals("!D") || word.equals("!B") || word.equals("!M")) {
/*  551 */         gl.setText(FontHelper.cardDescFont_N, word);
/*      */       
/*      */       }
/*  554 */       else if (word.equals("NL")) {
/*  555 */         gl.width = 0.0F;
/*  556 */         word = "";
/*  557 */         this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
/*  558 */         currentWidth = 0.0F;
/*  559 */         numLines++;
/*  560 */         sbuilder.setLength(0);
/*      */       }
/*      */       else {
/*      */         
/*  564 */         gl.setText(FontHelper.cardDescFont_N, word + sbuilder2);
/*      */       } 
/*      */ 
/*      */       
/*  568 */       if (currentWidth + gl.width > DESC_BOX_WIDTH) {
/*  569 */         this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
/*  570 */         numLines++;
/*  571 */         sbuilder.setLength(0);
/*  572 */         currentWidth = gl.width;
/*      */       } else {
/*  574 */         currentWidth += gl.width;
/*      */       } 
/*      */       
/*  577 */       if (isKeyword) {
/*  578 */         sbuilder.append('*');
/*      */       }
/*  580 */       sbuilder.append(word).append(sbuilder2);
/*      */     } 
/*      */     
/*  583 */     if (!sbuilder.toString().trim().isEmpty()) {
/*  584 */       this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
/*      */     }
/*      */     
/*  587 */     if (Settings.isDev && numLines > 5) {
/*  588 */       logger.info("WARNING: Card " + this.name + " has lots of text");
/*      */     }
/*      */   }
/*      */   
/*      */   public void initializeDescriptionCN() {
/*  593 */     this.description.clear();
/*  594 */     int numLines = 1;
/*      */ 
/*      */     
/*  597 */     sbuilder.setLength(0);
/*  598 */     float currentWidth = 0.0F;
/*      */     
/*  600 */     for (String word : this.rawDescription.split(" ")) {
/*  601 */       word = word.trim();
/*      */       
/*  603 */       if (Settings.manualLineBreak || Settings.manualAndAutoLineBreak || !word.contains("NL"))
/*      */       {
/*      */         
/*  606 */         if ((word.equals("NL") && sbuilder.length() == 0) || word.isEmpty()) {
/*  607 */           currentWidth = 0.0F;
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  612 */           String keywordTmp = word.toLowerCase();
/*  613 */           keywordTmp = dedupeKeyword(keywordTmp);
/*  614 */           if (GameDictionary.keywords.containsKey(keywordTmp)) {
/*  615 */             if (!this.keywords.contains(keywordTmp)) {
/*  616 */               this.keywords.add(keywordTmp);
/*      */             }
/*      */             
/*  619 */             gl.setText(FontHelper.cardDescFont_N, word);
/*  620 */             if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
/*  621 */               numLines++;
/*  622 */               this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*  623 */               sbuilder.setLength(0);
/*  624 */               currentWidth = gl.width;
/*  625 */               sbuilder.append(" *").append(word).append(" ");
/*      */             } else {
/*  627 */               sbuilder.append(" *").append(word).append(" ");
/*  628 */               currentWidth += gl.width;
/*      */             }
/*      */           
/*      */           }
/*  632 */           else if (!word.isEmpty() && word.charAt(0) == '[') {
/*  633 */             switch (this.color) {
/*      */               case BASIC:
/*  635 */                 if (!this.keywords.contains("[R]")) {
/*  636 */                   this.keywords.add("[R]");
/*      */                 }
/*      */                 break;
/*      */               case CURSE:
/*  640 */                 if (!this.keywords.contains("[G]")) {
/*  641 */                   this.keywords.add("[G]");
/*      */                 }
/*      */                 break;
/*      */               case SPECIAL:
/*  645 */                 if (!this.keywords.contains("[B]")) {
/*  646 */                   this.keywords.add("[B]");
/*      */                 }
/*      */                 break;
/*      */               case COMMON:
/*  650 */                 if (!this.keywords.contains("[W]")) {
/*  651 */                   this.keywords.add("[W]");
/*      */                 }
/*      */                 break;
/*      */               case UNCOMMON:
/*  655 */                 if (!this.keywords.contains("[W]")) {
/*  656 */                   this.keywords.add("[W]");
/*      */                 }
/*      */                 break;
/*      */               default:
/*  660 */                 logger.info("ERROR: Tried to display an invalid energy type: " + this.color.name());
/*      */                 break;
/*      */             } 
/*      */             
/*  664 */             if (currentWidth + CARD_ENERGY_IMG_WIDTH > CN_DESC_BOX_WIDTH) {
/*  665 */               numLines++;
/*  666 */               this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*  667 */               sbuilder.setLength(0);
/*  668 */               currentWidth = CARD_ENERGY_IMG_WIDTH;
/*  669 */               sbuilder.append(" ").append(word).append(" ");
/*      */             } else {
/*  671 */               sbuilder.append(" ").append(word).append(" ");
/*  672 */               currentWidth += CARD_ENERGY_IMG_WIDTH;
/*      */             }
/*      */           
/*      */           }
/*  676 */           else if (word.equals("!D!")) {
/*  677 */             if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
/*  678 */               numLines++;
/*  679 */               this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*  680 */               sbuilder.setLength(0);
/*  681 */               currentWidth = MAGIC_NUM_W;
/*  682 */               sbuilder.append(" D ");
/*      */             } else {
/*  684 */               sbuilder.append(" D ");
/*  685 */               currentWidth += MAGIC_NUM_W;
/*      */             }
/*      */           
/*      */           }
/*  689 */           else if (word.equals("!B!")) {
/*  690 */             if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
/*  691 */               numLines++;
/*  692 */               this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*  693 */               sbuilder.setLength(0);
/*  694 */               currentWidth = MAGIC_NUM_W;
/*  695 */               sbuilder.append(" ").append(word).append("! ");
/*      */             } else {
/*  697 */               sbuilder.append(" ").append(word).append("! ");
/*  698 */               currentWidth += MAGIC_NUM_W;
/*      */             }
/*      */           
/*      */           }
/*  702 */           else if (word.equals("!M!")) {
/*  703 */             if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
/*  704 */               numLines++;
/*  705 */               this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*  706 */               sbuilder.setLength(0);
/*  707 */               currentWidth = MAGIC_NUM_W;
/*  708 */               sbuilder.append(" ").append(word).append("! ");
/*      */             } else {
/*  710 */               sbuilder.append(" ").append(word).append("! ");
/*  711 */               currentWidth += MAGIC_NUM_W;
/*      */             }
/*      */           
/*      */           }
/*  715 */           else if ((Settings.manualLineBreak || Settings.manualAndAutoLineBreak) && word.equals("NL") && sbuilder
/*  716 */             .length() != 0) {
/*  717 */             gl.width = 0.0F;
/*  718 */             word = "";
/*  719 */             this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
/*  720 */             currentWidth = 0.0F;
/*  721 */             numLines++;
/*  722 */             sbuilder.setLength(0);
/*      */           
/*      */           }
/*  725 */           else if (word.charAt(0) == '*') {
/*  726 */             gl.setText(FontHelper.cardDescFont_N, word.substring(1));
/*  727 */             if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
/*  728 */               numLines++;
/*  729 */               this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*  730 */               sbuilder.setLength(0);
/*  731 */               currentWidth = gl.width;
/*  732 */               sbuilder.append(" ").append(word).append(" ");
/*      */             } else {
/*  734 */               sbuilder.append(" ").append(word).append(" ");
/*  735 */               currentWidth += gl.width;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  740 */             word = word.trim();
/*  741 */             for (char c : word.toCharArray()) {
/*  742 */               gl.setText(FontHelper.cardDescFont_N, String.valueOf(c));
/*  743 */               sbuilder.append(c);
/*      */               
/*  745 */               if (!Settings.manualLineBreak)
/*  746 */                 if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
/*  747 */                   numLines++;
/*  748 */                   this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*  749 */                   sbuilder.setLength(0);
/*  750 */                   currentWidth = gl.width;
/*      */                 } else {
/*  752 */                   currentWidth += gl.width;
/*      */                 }  
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*  759 */     if (sbuilder.length() != 0) {
/*  760 */       this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
/*      */     }
/*      */ 
/*      */     
/*  764 */     int removeLine = -1;
/*  765 */     for (int i = 0; i < this.description.size(); i++) {
/*  766 */       if (((DescriptionLine)this.description.get(i)).text.equals(LocalizedStrings.PERIOD)) {
/*  767 */         ((DescriptionLine)this.description.get(i - 1)).text += LocalizedStrings.PERIOD;
/*  768 */         removeLine = i;
/*      */       } 
/*      */     } 
/*  771 */     if (removeLine != -1) {
/*  772 */       this.description.remove(removeLine);
/*      */     }
/*      */ 
/*      */     
/*  776 */     if (Settings.isDev && numLines > 5) {
/*  777 */       logger.info("WARNING: Card " + this.name + " has lots of text");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasTag(CardTags tagToCheck) {
/*  783 */     if (this.tags.contains(tagToCheck)) {
/*  784 */       return true;
/*      */     }
/*  786 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canUpgrade() {
/*  790 */     if (this.type == CardType.CURSE) {
/*  791 */       return false;
/*      */     }
/*  793 */     if (this.type == CardType.STATUS) {
/*  794 */       return false;
/*      */     }
/*  796 */     if (this.upgraded) {
/*  797 */       return false;
/*      */     }
/*  799 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayUpgrades() {
/*  808 */     if (this.upgradedCost) {
/*  809 */       this.isCostModified = true;
/*      */     }
/*      */     
/*  812 */     if (this.upgradedDamage) {
/*  813 */       this.damage = this.baseDamage;
/*  814 */       this.isDamageModified = true;
/*      */     } 
/*      */     
/*  817 */     if (this.upgradedBlock) {
/*  818 */       this.block = this.baseBlock;
/*  819 */       this.isBlockModified = true;
/*      */     } 
/*      */     
/*  822 */     if (this.upgradedMagicNumber) {
/*  823 */       this.magicNumber = this.baseMagicNumber;
/*  824 */       this.isMagicNumberModified = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void upgradeDamage(int amount) {
/*  834 */     this.baseDamage += amount;
/*  835 */     this.upgradedDamage = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void upgradeBlock(int amount) {
/*  844 */     this.baseBlock += amount;
/*  845 */     this.upgradedBlock = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void upgradeMagicNumber(int amount) {
/*  854 */     this.baseMagicNumber += amount;
/*  855 */     this.magicNumber = this.baseMagicNumber;
/*  856 */     this.upgradedMagicNumber = true;
/*      */   }
/*      */   
/*      */   protected void upgradeName() {
/*  860 */     this.timesUpgraded++;
/*  861 */     this.upgraded = true;
/*  862 */     this.name += "+";
/*  863 */     initializeTitle();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void upgradeBaseCost(int newBaseCost) {
/*  872 */     int diff = this.costForTurn - this.cost;
/*  873 */     this.cost = newBaseCost;
/*      */     
/*  875 */     if (this.costForTurn > 0) {
/*  876 */       this.costForTurn = this.cost + diff;
/*      */     }
/*  878 */     if (this.costForTurn < 0) {
/*  879 */       this.costForTurn = 0;
/*      */     }
/*  881 */     this.upgradedCost = true;
/*      */   }
/*      */   
/*      */   private String dedupeKeyword(String keyword) {
/*  885 */     String retVal = (String)GameDictionary.parentWord.get(keyword);
/*  886 */     if (retVal != null) {
/*  887 */       return retVal;
/*      */     }
/*  889 */     return keyword;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractCard(AbstractCard c) {
/*  898 */     this.name = c.name;
/*  899 */     this.rawDescription = c.rawDescription;
/*  900 */     this.cost = c.cost;
/*  901 */     this.type = c.type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createCardImage() {
/*  908 */     switch (this.color) {
/*      */       case RARE:
/*  910 */         this.bgColor = CURSE_BG_COLOR.cpy();
/*  911 */         this.backColor = CURSE_TYPE_BACK_COLOR.cpy();
/*  912 */         this.frameColor = CURSE_FRAME_COLOR.cpy();
/*  913 */         this.descBoxColor = CURSE_DESC_BOX_COLOR.cpy();
/*      */         break;
/*      */       case UNCOMMON:
/*  916 */         this.bgColor = COLORLESS_BG_COLOR.cpy();
/*  917 */         this.backColor = COLORLESS_TYPE_BACK_COLOR.cpy();
/*  918 */         this.frameColor = COLORLESS_FRAME_COLOR.cpy();
/*  919 */         this.frameOutlineColor = Color.WHITE.cpy();
/*  920 */         this.descBoxColor = COLORLESS_DESC_BOX_COLOR.cpy();
/*      */         break;
/*      */       case BASIC:
/*  923 */         this.bgColor = RED_BG_COLOR.cpy();
/*  924 */         this.backColor = RED_TYPE_BACK_COLOR.cpy();
/*  925 */         this.frameColor = RED_FRAME_COLOR.cpy();
/*  926 */         this.frameOutlineColor = RED_RARE_OUTLINE_COLOR.cpy();
/*  927 */         this.descBoxColor = RED_DESC_BOX_COLOR.cpy();
/*      */         break;
/*      */       case CURSE:
/*  930 */         this.bgColor = GREEN_BG_COLOR.cpy();
/*  931 */         this.backColor = GREEN_TYPE_BACK_COLOR.cpy();
/*  932 */         this.frameColor = GREEN_FRAME_COLOR.cpy();
/*  933 */         this.frameOutlineColor = GREEN_RARE_OUTLINE_COLOR.cpy();
/*  934 */         this.descBoxColor = GREEN_DESC_BOX_COLOR.cpy();
/*      */         break;
/*      */       case SPECIAL:
/*  937 */         this.bgColor = BLUE_BG_COLOR.cpy();
/*  938 */         this.backColor = BLUE_TYPE_BACK_COLOR.cpy();
/*  939 */         this.frameColor = BLUE_FRAME_COLOR.cpy();
/*  940 */         this.frameOutlineColor = BLUE_RARE_OUTLINE_COLOR.cpy();
/*  941 */         this.descBoxColor = BLUE_DESC_BOX_COLOR.cpy();
/*      */       case COMMON:
/*  943 */         this.bgColor = BLUE_BG_COLOR.cpy();
/*  944 */         this.backColor = BLUE_TYPE_BACK_COLOR.cpy();
/*  945 */         this.frameColor = BLUE_FRAME_COLOR.cpy();
/*  946 */         this.frameOutlineColor = BLUE_RARE_OUTLINE_COLOR.cpy();
/*  947 */         this.descBoxColor = BLUE_DESC_BOX_COLOR.cpy();
/*      */         break;
/*      */       default:
/*  950 */         logger.info("ERROR: Card color was NOT set for " + this.name);
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  955 */     if (this.rarity == CardRarity.COMMON || this.rarity == CardRarity.BASIC || this.rarity == CardRarity.CURSE) {
/*  956 */       this.bannerColor = BANNER_COLOR_COMMON.cpy();
/*  957 */       this.imgFrameColor = IMG_FRAME_COLOR_COMMON.cpy();
/*  958 */     } else if (this.rarity == CardRarity.UNCOMMON) {
/*  959 */       this.bannerColor = BANNER_COLOR_UNCOMMON.cpy();
/*  960 */       this.imgFrameColor = IMG_FRAME_COLOR_UNCOMMON.cpy();
/*      */     } else {
/*  962 */       this.bannerColor = BANNER_COLOR_RARE.cpy();
/*  963 */       this.imgFrameColor = IMG_FRAME_COLOR_RARE.cpy();
/*      */     } 
/*      */     
/*  966 */     this.tintColor = CardHelper.getColor(43, 37, 65);
/*  967 */     this.tintColor.a = 0.0F;
/*  968 */     this.frameShadowColor = FRAME_SHADOW_COLOR.cpy();
/*      */   }
/*      */   
/*      */   public AbstractCard makeSameInstanceOf() {
/*  972 */     AbstractCard card = makeStatEquivalentCopy();
/*  973 */     card.uuid = this.uuid;
/*  974 */     return card;
/*      */   }
/*      */   
/*      */   public AbstractCard makeStatEquivalentCopy() {
/*  978 */     AbstractCard card = makeCopy();
/*      */     
/*  980 */     for (int i = 0; i < this.timesUpgraded; i++) {
/*  981 */       card.upgrade();
/*      */     }
/*      */     
/*  984 */     card.name = this.name;
/*  985 */     card.target = this.target;
/*  986 */     card.upgraded = this.upgraded;
/*  987 */     card.timesUpgraded = this.timesUpgraded;
/*  988 */     card.baseDamage = this.baseDamage;
/*  989 */     card.baseBlock = this.baseBlock;
/*  990 */     card.baseMagicNumber = this.baseMagicNumber;
/*  991 */     card.cost = this.cost;
/*  992 */     card.costForTurn = this.costForTurn;
/*  993 */     card.isCostModified = this.isCostModified;
/*  994 */     card.isCostModifiedForTurn = this.isCostModifiedForTurn;
/*  995 */     card.inBottleLightning = this.inBottleLightning;
/*  996 */     card.inBottleFlame = this.inBottleFlame;
/*  997 */     card.inBottleTornado = this.inBottleTornado;
/*  998 */     card.isSeen = this.isSeen;
/*  999 */     card.isLocked = this.isLocked;
/* 1000 */     card.misc = this.misc;
/* 1001 */     card.freeToPlayOnce = this.freeToPlayOnce;
/* 1002 */     return card;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onRemoveFromMasterDeck() {}
/*      */   
/*      */   public boolean cardPlayable(AbstractMonster m) {
/* 1009 */     if (((this.target == CardTarget.ENEMY || this.target == CardTarget.SELF_AND_ENEMY) && m != null && m.isDying) || 
/* 1010 */       AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
/* 1011 */       this.cantUseMessage = null;
/* 1012 */       return false;
/*      */     } 
/* 1014 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hasEnoughEnergy() {
/* 1018 */     if (AbstractDungeon.actionManager.turnHasEnded) {
/* 1019 */       this.cantUseMessage = TEXT[9];
/* 1020 */       return false;
/*      */     } 
/*      */     
/* 1023 */     for (AbstractPower p : AbstractDungeon.player.powers) {
/* 1024 */       if (!p.canPlayCard(this)) {
/* 1025 */         this.cantUseMessage = TEXT[13];
/* 1026 */         return false;
/*      */       } 
/*      */     } 
/*      */     
/* 1030 */     if (AbstractDungeon.player.hasPower("Entangled") && this.type == CardType.ATTACK) {
/* 1031 */       this.cantUseMessage = TEXT[10];
/* 1032 */       return false;
/*      */     } 
/*      */     
/* 1035 */     for (AbstractRelic r : AbstractDungeon.player.relics) {
/* 1036 */       if (!r.canPlay(this)) {
/* 1037 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1041 */     for (AbstractBlight b : AbstractDungeon.player.blights) {
/* 1042 */       if (!b.canPlay(this)) {
/* 1043 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1047 */     for (AbstractCard c : AbstractDungeon.player.hand.group) {
/* 1048 */       if (!c.canPlay(this)) {
/* 1049 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1053 */     if (EnergyPanel.totalCount >= this.costForTurn || freeToPlay() || this.isInAutoplay) {
/* 1054 */       return true;
/*      */     }
/*      */     
/* 1057 */     this.cantUseMessage = TEXT[11];
/* 1058 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void tookDamage() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void didDiscard() {}
/*      */ 
/*      */   
/*      */   public void switchedStance() {}
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void useBlueCandle(AbstractPlayer p) {}
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void useMedicalKit(AbstractPlayer p) {}
/*      */ 
/*      */   
/*      */   public boolean canPlay(AbstractCard card) {
/* 1082 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canUse(AbstractPlayer p, AbstractMonster m) {
/* 1091 */     if (this.type == CardType.STATUS && this.costForTurn < -1 && 
/* 1092 */       !AbstractDungeon.player.hasRelic("Medical Kit")) {
/* 1093 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1098 */     if (this.type == CardType.CURSE && this.costForTurn < -1 && 
/* 1099 */       !AbstractDungeon.player.hasRelic("Blue Candle")) {
/* 1100 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1104 */     if (cardPlayable(m) && hasEnoughEnergy()) {
/* 1105 */       return true;
/*      */     }
/*      */     
/* 1108 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void update() {
/* 1117 */     updateFlashVfx();
/*      */     
/* 1119 */     if (this.hoverTimer != 0.0F) {
/* 1120 */       this.hoverTimer -= Gdx.graphics.getDeltaTime();
/* 1121 */       if (this.hoverTimer < 0.0F) {
/* 1122 */         this.hoverTimer = 0.0F;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1129 */     if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && this == AbstractDungeon.player.hoveredCard) {
/*      */       
/* 1131 */       this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
/* 1132 */       this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
/*      */ 
/*      */       
/* 1135 */       if (AbstractDungeon.player.hasRelic("Necronomicon"))
/*      */       {
/* 1137 */         if (this.cost >= 2 && this.type == CardType.ATTACK && AbstractDungeon.player.getRelic("Necronomicon")
/* 1138 */           .checkTrigger()) {
/* 1139 */           AbstractDungeon.player.getRelic("Necronomicon").beginLongPulse();
/*      */         } else {
/* 1141 */           AbstractDungeon.player.getRelic("Necronomicon").stopPulse();
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1146 */     if (Settings.FAST_MODE) {
/* 1147 */       this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
/* 1148 */       this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
/*      */     } 
/* 1150 */     this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
/* 1151 */     this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
/*      */     
/* 1153 */     this.hb.move(this.current_x, this.current_y);
/* 1154 */     this.hb.resize(HB_W * this.drawScale, HB_H * this.drawScale);
/*      */     
/* 1156 */     if (this.hb.clickStarted && this.hb.hovered) {
/* 1157 */       this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
/* 1158 */       this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
/*      */     } else {
/* 1160 */       this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale);
/*      */     } 
/*      */     
/* 1163 */     if (this.angle != this.targetAngle) {
/* 1164 */       this.angle = MathHelper.angleLerpSnap(this.angle, this.targetAngle);
/*      */     }
/*      */     
/* 1167 */     updateTransparency();
/* 1168 */     updateColor();
/*      */   }
/*      */   
/*      */   private void updateFlashVfx() {
/* 1172 */     if (this.flashVfx != null) {
/* 1173 */       this.flashVfx.update();
/* 1174 */       if (this.flashVfx.isDone) {
/* 1175 */         this.flashVfx = null;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateGlow() {
/* 1181 */     if (this.isGlowing) {
/* 1182 */       this.glowTimer -= Gdx.graphics.getDeltaTime();
/* 1183 */       if (this.glowTimer < 0.0F) {
/* 1184 */         this.glowList.add(new CardGlowBorder(this, this.glowColor));
/* 1185 */         this.glowTimer = 0.3F;
/*      */       } 
/*      */     } 
/*      */     
/* 1189 */     for (Iterator<CardGlowBorder> i = this.glowList.iterator(); i.hasNext(); ) {
/* 1190 */       CardGlowBorder e = i.next();
/* 1191 */       e.update();
/*      */       
/* 1193 */       if (e.isDone) {
/* 1194 */         i.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isHoveredInHand(float scale) {
/* 1200 */     if (this.hoverTimer > 0.0F) {
/* 1201 */       return false;
/*      */     }
/*      */     
/* 1204 */     int x = InputHelper.mX, y = InputHelper.mY;
/* 1205 */     return (x > this.current_x - IMG_WIDTH * scale / 2.0F && x < this.current_x + IMG_WIDTH * scale / 2.0F && y > this.current_y - IMG_HEIGHT * scale / 2.0F && y < this.current_y + IMG_HEIGHT * scale / 2.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isOnScreen() {
/* 1210 */     return (this.current_y >= -200.0F * Settings.scale && this.current_y <= Settings.HEIGHT + 200.0F * Settings.scale);
/*      */   }
/*      */   
/*      */   public void render(SpriteBatch sb) {
/* 1214 */     if (!Settings.hideCards) {
/* 1215 */       render(sb, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderHoverShadow(SpriteBatch sb) {
/* 1220 */     if (!Settings.hideCards) {
/* 1221 */       renderHelper(sb, Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR, ImageMaster.CARD_SUPER_SHADOW, this.current_x, this.current_y, 1.15F);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renderInLibrary(SpriteBatch sb) {
/* 1232 */     if (!isOnScreen()) {
/*      */       return;
/*      */     }
/*      */     
/* 1236 */     if (SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked) {
/* 1237 */       AbstractCard copy = makeCopy();
/* 1238 */       copy.current_x = this.current_x;
/* 1239 */       copy.current_y = this.current_y;
/* 1240 */       copy.drawScale = this.drawScale;
/* 1241 */       copy.upgrade();
/* 1242 */       copy.displayUpgrades();
/* 1243 */       copy.render(sb);
/*      */     } else {
/* 1245 */       updateGlow();
/* 1246 */       renderGlow(sb);
/* 1247 */       renderImage(sb, this.hovered, false);
/* 1248 */       renderType(sb);
/* 1249 */       renderTitle(sb);
/*      */       
/* 1251 */       if (Settings.lineBreakViaCharacter) {
/* 1252 */         renderDescriptionCN(sb);
/*      */       } else {
/* 1254 */         renderDescription(sb);
/*      */       } 
/*      */       
/* 1257 */       renderTint(sb);
/* 1258 */       renderEnergy(sb);
/* 1259 */       this.hb.render(sb);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void render(SpriteBatch sb, boolean selected) {
/* 1265 */     if (!Settings.hideCards) {
/* 1266 */       if (this.flashVfx != null) {
/* 1267 */         this.flashVfx.render(sb);
/*      */       }
/* 1269 */       renderCard(sb, this.hovered, selected);
/* 1270 */       this.hb.render(sb);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void renderUpgradePreview(SpriteBatch sb) {
/* 1275 */     this.upgraded = true;
/* 1276 */     this.name = this.originalName + "+";
/* 1277 */     initializeTitle();
/* 1278 */     renderCard(sb, this.hovered, false);
/* 1279 */     this.name = this.originalName;
/* 1280 */     initializeTitle();
/* 1281 */     this.upgraded = false;
/* 1282 */     resetAttributes();
/*      */   }
/*      */   
/*      */   public void renderWithSelections(SpriteBatch sb) {
/* 1286 */     renderCard(sb, false, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderCard(SpriteBatch sb, boolean hovered, boolean selected) {
/* 1291 */     if (!Settings.hideCards) {
/* 1292 */       if (!isOnScreen()) {
/*      */         return;
/*      */       }
/* 1295 */       if (!this.isFlipped) {
/* 1296 */         updateGlow();
/* 1297 */         renderGlow(sb);
/* 1298 */         renderImage(sb, hovered, selected);
/* 1299 */         renderTitle(sb);
/* 1300 */         renderType(sb);
/* 1301 */         if (Settings.lineBreakViaCharacter) {
/* 1302 */           renderDescriptionCN(sb);
/*      */         } else {
/* 1304 */           renderDescription(sb);
/*      */         } 
/* 1306 */         renderTint(sb);
/* 1307 */         renderEnergy(sb);
/*      */       } else {
/* 1309 */         renderBack(sb, hovered, selected);
/* 1310 */         this.hb.render(sb);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderTint(SpriteBatch sb) {
/* 1316 */     if (!Settings.hideCards) {
/*      */       
/* 1318 */       TextureAtlas.AtlasRegion cardBgImg = getCardBgAtlas();
/* 1319 */       if (cardBgImg != null) {
/* 1320 */         renderHelper(sb, this.tintColor, cardBgImg, this.current_x, this.current_y);
/*      */       } else {
/* 1322 */         renderHelper(sb, this.tintColor, getCardBg(), this.current_x - 256.0F, this.current_y - 256.0F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void renderOuterGlow(SpriteBatch sb) {
/* 1328 */     if (AbstractDungeon.player == null || !Settings.hideCards) {
/*      */       return;
/*      */     }
/*      */     
/* 1332 */     renderHelper(sb, AbstractDungeon.player
/*      */         
/* 1334 */         .getCardRenderColor(), 
/* 1335 */         getCardBgAtlas(), this.current_x, this.current_y, 1.0F + this.tintColor.a / 5.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Texture getCardBg() {
/* 1342 */     int i = 10;
/* 1343 */     if (i < 5) {
/* 1344 */       System.out.println("Add special logic here");
/*      */     }
/* 1346 */     return null;
/*      */   }
/*      */   
/*      */   public TextureAtlas.AtlasRegion getCardBgAtlas() {
/* 1350 */     switch (this.type) {
/*      */       case BASIC:
/* 1352 */         return ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
/*      */ 
/*      */       
/*      */       case CURSE:
/*      */       case SPECIAL:
/*      */       case COMMON:
/* 1358 */         return ImageMaster.CARD_SKILL_BG_SILHOUETTE;
/*      */       case UNCOMMON:
/* 1360 */         return ImageMaster.CARD_POWER_BG_SILHOUETTE;
/*      */     } 
/* 1362 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderGlow(SpriteBatch sb) {
/* 1367 */     if (!Settings.hideCards) {
/* 1368 */       renderMainBorder(sb);
/* 1369 */       for (AbstractGameEffect e : this.glowList) {
/* 1370 */         e.render(sb);
/*      */       }
/* 1372 */       sb.setBlendFunction(770, 771);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void beginGlowing() {
/* 1377 */     this.isGlowing = true;
/*      */   }
/*      */   
/*      */   public void stopGlowing() {
/* 1381 */     this.isGlowing = false;
/* 1382 */     for (CardGlowBorder e : this.glowList) {
/* 1383 */       e.duration /= 5.0F;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderMainBorder(SpriteBatch sb) {
/* 1389 */     if (this.isGlowing) {
/* 1390 */       TextureAtlas.AtlasRegion img; sb.setBlendFunction(770, 1);
/*      */       
/* 1392 */       switch (this.type) {
/*      */         case UNCOMMON:
/* 1394 */           img = ImageMaster.CARD_POWER_BG_SILHOUETTE;
/*      */           break;
/*      */         case BASIC:
/* 1397 */           img = ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
/*      */           break;
/*      */         default:
/* 1400 */           img = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
/*      */           break;
/*      */       } 
/*      */       
/* 1404 */       if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/* 1405 */         sb.setColor(this.glowColor);
/*      */       } else {
/* 1407 */         sb.setColor(GREEN_BORDER_GLOW_COLOR);
/*      */       } 
/*      */       
/* 1410 */       sb.draw((TextureRegion)img, this.current_x + img.offsetX - img.originalWidth / 2.0F, this.current_y + img.offsetY - img.originalWidth / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalWidth / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.drawScale * Settings.scale * 1.04F, this.drawScale * Settings.scale * 1.03F, this.angle);
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
/*      */   
/*      */   private void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
/* 1427 */     sb.setColor(color);
/* 1428 */     sb.draw((TextureRegion)img, drawX + img.offsetX - img.originalWidth / 2.0F, drawY + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
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
/*      */   private void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY, float scale) {
/* 1442 */     sb.setColor(color);
/* 1443 */     sb.draw((TextureRegion)img, drawX + img.offsetX - img.originalWidth / 2.0F, drawY + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle);
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
/*      */   private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY) {
/* 1457 */     sb.setColor(color);
/* 1458 */     sb.draw(img, drawX + 256.0F, drawY + 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY, float scale) {
/* 1479 */     sb.setColor(color);
/* 1480 */     sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle, 0, 0, 512, 512, false, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renderSmallEnergy(SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y) {
/* 1500 */     sb.setColor(this.renderColor);
/* 1501 */     sb.draw(region
/* 1502 */         .getTexture(), this.current_x + x * Settings.scale * this.drawScale + region.offsetX * Settings.scale, this.current_y + y * Settings.scale * this.drawScale + region.offsetY * Settings.scale, 0.0F, 0.0F, region.packedWidth, region.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, 0.0F, region
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1512 */         .getRegionX(), region
/* 1513 */         .getRegionY(), region
/* 1514 */         .getRegionWidth(), region
/* 1515 */         .getRegionHeight(), false, false);
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
/*      */   private void renderImage(SpriteBatch sb, boolean hovered, boolean selected) {
/* 1528 */     if (AbstractDungeon.player != null) {
/* 1529 */       if (selected) {
/* 1530 */         renderHelper(sb, Color.SKY, getCardBgAtlas(), this.current_x, this.current_y, 1.03F);
/*      */       }
/*      */ 
/*      */       
/* 1534 */       renderHelper(sb, this.frameShadowColor, 
/*      */ 
/*      */           
/* 1537 */           getCardBgAtlas(), this.current_x + SHADOW_OFFSET_X * this.drawScale, this.current_y - SHADOW_OFFSET_Y * this.drawScale);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1542 */       if (AbstractDungeon.player.hoveredCard == this && ((AbstractDungeon.player.isDraggingCard && AbstractDungeon.player.isHoveringDropZone) || AbstractDungeon.player.inSingleTargetMode)) {
/*      */         
/* 1544 */         renderHelper(sb, HOVER_IMG_COLOR, getCardBgAtlas(), this.current_x, this.current_y);
/* 1545 */       } else if (selected) {
/* 1546 */         renderHelper(sb, SELECTED_CARD_COLOR, getCardBgAtlas(), this.current_x, this.current_y);
/*      */       } 
/*      */     } 
/*      */     
/* 1550 */     renderCardBg(sb, this.current_x, this.current_y);
/*      */     
/* 1552 */     if (UnlockTracker.betaCardPref.getBoolean(this.cardID, false) || Settings.PLAYTESTER_ART_MODE) {
/* 1553 */       renderJokePortrait(sb);
/*      */     } else {
/* 1555 */       renderPortrait(sb);
/*      */     } 
/*      */     
/* 1558 */     renderPortraitFrame(sb, this.current_x, this.current_y);
/* 1559 */     renderBannerImage(sb, this.current_x, this.current_y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderCardBg(SpriteBatch sb, float x, float y) {
/* 1570 */     switch (this.type) {
/*      */       case BASIC:
/* 1572 */         renderAttackBg(sb, x, y);
/*      */         break;
/*      */       case COMMON:
/* 1575 */         renderSkillBg(sb, x, y);
/*      */         break;
/*      */       case UNCOMMON:
/* 1578 */         renderPowerBg(sb, x, y);
/*      */         break;
/*      */       case CURSE:
/* 1581 */         renderSkillBg(sb, x, y);
/*      */         break;
/*      */       case SPECIAL:
/* 1584 */         renderSkillBg(sb, x, y);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderAttackBg(SpriteBatch sb, float x, float y) {
/* 1592 */     switch (this.color) {
/*      */       case BASIC:
/* 1594 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_RED, x, y);
/*      */         return;
/*      */       case CURSE:
/* 1597 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_GREEN, x, y);
/*      */         return;
/*      */       case SPECIAL:
/* 1600 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_BLUE, x, y);
/*      */         return;
/*      */       case COMMON:
/* 1603 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_PURPLE, x, y);
/*      */         return;
/*      */       case RARE:
/* 1606 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
/*      */         return;
/*      */       case UNCOMMON:
/* 1609 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_GRAY, x, y);
/*      */         return;
/*      */     } 
/* 1612 */     renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderSkillBg(SpriteBatch sb, float x, float y) {
/* 1617 */     switch (this.color) {
/*      */       case BASIC:
/* 1619 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_RED, x, y);
/*      */         return;
/*      */       case CURSE:
/* 1622 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_GREEN, x, y);
/*      */         return;
/*      */       case SPECIAL:
/* 1625 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLUE, x, y);
/*      */         return;
/*      */       case COMMON:
/* 1628 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_PURPLE, x, y);
/*      */         return;
/*      */       case RARE:
/* 1631 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
/*      */         return;
/*      */       case UNCOMMON:
/* 1634 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_GRAY, x, y);
/*      */         return;
/*      */     } 
/* 1637 */     renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderPowerBg(SpriteBatch sb, float x, float y) {
/* 1642 */     switch (this.color) {
/*      */       case BASIC:
/* 1644 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_RED, x, y);
/*      */         return;
/*      */       case CURSE:
/* 1647 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_GREEN, x, y);
/*      */         return;
/*      */       case SPECIAL:
/* 1650 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_BLUE, x, y);
/*      */         return;
/*      */       case COMMON:
/* 1653 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_PURPLE, x, y);
/*      */         return;
/*      */       case RARE:
/* 1656 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
/*      */         return;
/*      */       case UNCOMMON:
/* 1659 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_GRAY, x, y);
/*      */         return;
/*      */     } 
/* 1662 */     renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
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
/*      */   private void renderPortraitFrame(SpriteBatch sb, float x, float y) {
/* 1674 */     float tWidth = 0.0F;
/* 1675 */     float tOffset = 0.0F;
/*      */     
/* 1677 */     switch (this.type) {
/*      */       case BASIC:
/* 1679 */         renderAttackPortrait(sb, x, y);
/* 1680 */         tWidth = typeWidthAttack;
/* 1681 */         tOffset = typeOffsetAttack;
/*      */         break;
/*      */       case CURSE:
/* 1684 */         renderSkillPortrait(sb, x, y);
/* 1685 */         tWidth = typeWidthCurse;
/* 1686 */         tOffset = typeOffsetCurse;
/*      */         break;
/*      */       case SPECIAL:
/* 1689 */         renderSkillPortrait(sb, x, y);
/* 1690 */         tWidth = typeWidthStatus;
/* 1691 */         tOffset = typeOffsetStatus;
/*      */         break;
/*      */       case COMMON:
/* 1694 */         renderSkillPortrait(sb, x, y);
/* 1695 */         tWidth = typeWidthSkill;
/* 1696 */         tOffset = typeOffsetSkill;
/*      */         break;
/*      */       case UNCOMMON:
/* 1699 */         renderPowerPortrait(sb, x, y);
/* 1700 */         tWidth = typeWidthPower;
/* 1701 */         tOffset = typeOffsetPower;
/*      */         break;
/*      */     } 
/*      */     
/* 1705 */     renderDynamicFrame(sb, x, y, tOffset, tWidth);
/*      */   }
/*      */   
/*      */   private void renderAttackPortrait(SpriteBatch sb, float x, float y) {
/* 1709 */     switch (this.rarity) {
/*      */ 
/*      */ 
/*      */       
/*      */       case BASIC:
/*      */       case CURSE:
/*      */       case SPECIAL:
/*      */       case COMMON:
/* 1717 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_ATTACK_COMMON, x, y);
/*      */         return;
/*      */       case UNCOMMON:
/* 1720 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON, x, y);
/*      */         return;
/*      */       case RARE:
/* 1723 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_ATTACK_RARE, x, y);
/*      */         return;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderDynamicFrame(SpriteBatch sb, float x, float y, float typeOffset, float typeWidth) {
/* 1729 */     if (typeWidth <= 1.1F) {
/*      */       return;
/*      */     }
/*      */     
/* 1733 */     switch (this.rarity) {
/*      */ 
/*      */ 
/*      */       
/*      */       case BASIC:
/*      */       case CURSE:
/*      */       case SPECIAL:
/*      */       case COMMON:
/* 1741 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_MID, x, y, 0.0F, typeWidth);
/* 1742 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_LEFT, x, y, -typeOffset, 1.0F);
/* 1743 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_RIGHT, x, y, typeOffset, 1.0F);
/*      */         break;
/*      */       case UNCOMMON:
/* 1746 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_MID, x, y, 0.0F, typeWidth);
/* 1747 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_LEFT, x, y, -typeOffset, 1.0F);
/* 1748 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_RIGHT, x, y, typeOffset, 1.0F);
/*      */         break;
/*      */       case RARE:
/* 1751 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_MID, x, y, 0.0F, typeWidth);
/* 1752 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_LEFT, x, y, -typeOffset, 1.0F);
/* 1753 */         dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_RIGHT, x, y, typeOffset, 1.0F);
/*      */         break;
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
/*      */   private void dynamicFrameRenderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float x, float y, float xOffset, float xScale) {
/* 1766 */     sb.draw((TextureRegion)img, x + img.offsetX - img.originalWidth / 2.0F + xOffset * this.drawScale, y + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.drawScale * Settings.scale * xScale, this.drawScale * Settings.scale, this.angle);
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
/*      */   private void dynamicFrameRenderHelper(SpriteBatch sb, Texture img, float x, float y, float xOffset, float xScale) {
/* 1781 */     sb.draw(img, x + xOffset * this.drawScale, y, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale * xScale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderSkillPortrait(SpriteBatch sb, float x, float y) {
/* 1801 */     switch (this.rarity) {
/*      */       case BASIC:
/* 1803 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
/*      */         return;
/*      */       case COMMON:
/* 1806 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
/*      */         return;
/*      */       case UNCOMMON:
/* 1809 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_UNCOMMON, x, y);
/*      */         return;
/*      */       case RARE:
/* 1812 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_RARE, x, y);
/*      */         return;
/*      */       case CURSE:
/* 1815 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
/*      */         return;
/*      */     } 
/* 1818 */     renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderPowerPortrait(SpriteBatch sb, float x, float y) {
/* 1824 */     switch (this.rarity) {
/*      */ 
/*      */ 
/*      */       
/*      */       case BASIC:
/*      */       case CURSE:
/*      */       case SPECIAL:
/*      */       case COMMON:
/* 1832 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_POWER_COMMON, x, y);
/*      */         break;
/*      */       case UNCOMMON:
/* 1835 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_POWER_UNCOMMON, x, y);
/*      */         break;
/*      */       case RARE:
/* 1838 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_POWER_RARE, x, y);
/*      */         break;
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
/*      */   private void renderBannerImage(SpriteBatch sb, float drawX, float drawY) {
/* 1851 */     switch (this.rarity) {
/*      */       case BASIC:
/* 1853 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
/*      */         return;
/*      */       case COMMON:
/* 1856 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
/*      */         return;
/*      */       case UNCOMMON:
/* 1859 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_UNCOMMON, drawX, drawY);
/*      */         return;
/*      */       case RARE:
/* 1862 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_RARE, drawX, drawY);
/*      */         return;
/*      */       case CURSE:
/* 1865 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
/*      */         return;
/*      */     } 
/* 1868 */     renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
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
/*      */   private void renderBack(SpriteBatch sb, boolean hovered, boolean selected) {
/* 1881 */     renderHelper(sb, this.renderColor, ImageMaster.CARD_BACK, this.current_x, this.current_y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderPortrait(SpriteBatch sb) {
/* 1890 */     float drawX = this.current_x - 125.0F;
/* 1891 */     float drawY = this.current_y - 95.0F;
/*      */     
/* 1893 */     Texture img = null;
/* 1894 */     if (this.portraitImg != null) {
/* 1895 */       img = this.portraitImg;
/*      */     }
/*      */     
/* 1898 */     if (!this.isLocked) {
/* 1899 */       if (this.portrait != null) {
/* 1900 */         drawX = this.current_x - this.portrait.packedWidth / 2.0F;
/* 1901 */         drawY = this.current_y - this.portrait.packedHeight / 2.0F;
/* 1902 */         sb.setColor(this.renderColor);
/* 1903 */         sb.draw((TextureRegion)this.portrait, drawX, drawY + 72.0F, this.portrait.packedWidth / 2.0F, this.portrait.packedHeight / 2.0F - 72.0F, this.portrait.packedWidth, this.portrait.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1914 */       else if (img != null) {
/* 1915 */         sb.setColor(this.renderColor);
/* 1916 */         sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1935 */       sb.draw(this.portraitImg, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
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
/*      */   private void renderJokePortrait(SpriteBatch sb) {
/* 1961 */     float drawX = this.current_x - 125.0F;
/* 1962 */     float drawY = this.current_y - 95.0F;
/*      */     
/* 1964 */     Texture img = null;
/* 1965 */     if (this.portraitImg != null) {
/* 1966 */       img = this.portraitImg;
/*      */     }
/*      */     
/* 1969 */     if (!this.isLocked) {
/* 1970 */       if (this.jokePortrait != null) {
/* 1971 */         drawX = this.current_x - this.portrait.packedWidth / 2.0F;
/* 1972 */         drawY = this.current_y - this.portrait.packedHeight / 2.0F;
/* 1973 */         sb.setColor(this.renderColor);
/* 1974 */         sb.draw((TextureRegion)this.jokePortrait, drawX, drawY + 72.0F, this.jokePortrait.packedWidth / 2.0F, this.jokePortrait.packedHeight / 2.0F - 72.0F, this.jokePortrait.packedWidth, this.jokePortrait.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1985 */       else if (img != null) {
/* 1986 */         sb.setColor(this.renderColor);
/* 1987 */         sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2006 */       sb.draw(this.portraitImg, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderDescription(SpriteBatch sb) {
/* 2030 */     if (!this.isSeen || this.isLocked) {
/* 2031 */       FontHelper.menuBannerFont.getData().setScale(this.drawScale * 1.25F);
/* 2032 */       FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", this.current_x, this.current_y, 0.0F, -200.0F * Settings.scale * this.drawScale / 2.0F, this.angle, true, this.textColor);
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
/* 2043 */       FontHelper.menuBannerFont.getData().setScale(1.0F);
/*      */       
/*      */       return;
/*      */     } 
/* 2047 */     BitmapFont font = getDescFont();
/* 2048 */     float draw_y = this.current_y - IMG_HEIGHT * this.drawScale / 2.0F + DESC_OFFSET_Y * this.drawScale;
/* 2049 */     draw_y += this.description.size() * font.getCapHeight() * 0.775F - font.getCapHeight() * 0.375F;
/* 2050 */     float spacing = 1.45F * -font.getCapHeight() / Settings.scale / this.drawScale;
/*      */     
/* 2052 */     for (int i = 0; i < this.description.size(); i++) {
/* 2053 */       float start_x = this.current_x - ((DescriptionLine)this.description.get(i)).width * this.drawScale / 2.0F;
/*      */       
/* 2055 */       for (String tmp : ((DescriptionLine)this.description.get(i)).getCachedTokenizedText()) {
/*      */         
/* 2057 */         if (tmp.length() > 0 && tmp.charAt(0) == '*') {
/* 2058 */           tmp = tmp.substring(1);
/* 2059 */           String punctuation = "";
/* 2060 */           if (tmp.length() > 1 && tmp.charAt(tmp.length() - 2) != '+' && !Character.isLetter(tmp
/* 2061 */               .charAt(tmp.length() - 2))) {
/* 2062 */             punctuation = punctuation + tmp.charAt(tmp.length() - 2);
/* 2063 */             tmp = tmp.substring(0, tmp.length() - 2);
/* 2064 */             punctuation = punctuation + ' ';
/*      */           } 
/*      */           
/* 2067 */           gl.setText(font, tmp);
/* 2068 */           FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2075 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.goldColor);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2080 */           start_x = Math.round(start_x + gl.width);
/* 2081 */           gl.setText(font, punctuation);
/* 2082 */           FontHelper.renderRotatedText(sb, font, punctuation, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2089 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2094 */           gl.setText(font, punctuation);
/* 2095 */           start_x += gl.width;
/*      */         
/*      */         }
/* 2098 */         else if (tmp.length() > 0 && tmp.charAt(0) == '!') {
/* 2099 */           if (tmp.length() == 4) {
/* 2100 */             start_x += renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, null);
/* 2101 */           } else if (tmp.length() == 5) {
/* 2102 */             start_x += renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, Character.valueOf(tmp.charAt(3)));
/*      */           }
/*      */         
/*      */         }
/* 2106 */         else if (tmp.equals("[R] ")) {
/* 2107 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2108 */           renderSmallEnergy(sb, orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2112 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2114 */           start_x += gl.width;
/*      */         
/*      */         }
/* 2117 */         else if (tmp.equals("[R]. ")) {
/* 2118 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale / Settings.scale;
/* 2119 */           renderSmallEnergy(sb, orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2123 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2125 */           FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2132 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2137 */           start_x += gl.width;
/* 2138 */           gl.setText(font, LocalizedStrings.PERIOD);
/* 2139 */           start_x += gl.width;
/*      */         
/*      */         }
/* 2142 */         else if (tmp.equals("[G] ")) {
/* 2143 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2144 */           renderSmallEnergy(sb, orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2148 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2150 */           start_x += gl.width;
/*      */         }
/* 2152 */         else if (tmp.equals("[G]. ")) {
/* 2153 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2154 */           renderSmallEnergy(sb, orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2158 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2160 */           FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2167 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */           
/* 2171 */           start_x += gl.width;
/*      */         
/*      */         }
/* 2174 */         else if (tmp.equals("[B] ")) {
/* 2175 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2176 */           renderSmallEnergy(sb, orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2180 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2182 */           start_x += gl.width;
/*      */         }
/* 2184 */         else if (tmp.equals("[B]. ")) {
/* 2185 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2186 */           renderSmallEnergy(sb, orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2190 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2192 */           FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2199 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */           
/* 2203 */           start_x += gl.width;
/*      */         
/*      */         }
/* 2206 */         else if (tmp.equals("[W] ")) {
/* 2207 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2208 */           renderSmallEnergy(sb, orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2212 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2214 */           start_x += gl.width;
/*      */         }
/* 2216 */         else if (tmp.equals("[W]. ")) {
/* 2217 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2218 */           renderSmallEnergy(sb, orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2222 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2224 */           FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2231 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */           
/* 2235 */           start_x += gl.width;
/*      */         }
/*      */         else {
/*      */           
/* 2239 */           gl.setText(font, tmp);
/* 2240 */           FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2247 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */           
/* 2251 */           start_x += gl.width;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2255 */     font.getData().setScale(1.0F);
/*      */   }
/*      */   
/*      */   private String getDynamicValue(char key) {
/* 2259 */     switch (key) {
/*      */       case 'B':
/* 2261 */         if (this.isBlockModified) {
/* 2262 */           if (this.block >= this.baseBlock) {
/* 2263 */             return "[#7fff00]" + Integer.toString(this.block) + "[]";
/*      */           }
/* 2265 */           return "[#ff6563]" + Integer.toString(this.block) + "[]";
/*      */         } 
/*      */         
/* 2268 */         return Integer.toString(this.baseBlock);
/*      */       
/*      */       case 'D':
/* 2271 */         if (this.isDamageModified) {
/* 2272 */           if (this.damage >= this.baseDamage) {
/* 2273 */             return "[#7fff00]" + Integer.toString(this.damage) + "[]";
/*      */           }
/* 2275 */           return "[#ff6563]" + Integer.toString(this.damage) + "[]";
/*      */         } 
/*      */         
/* 2278 */         return Integer.toString(this.baseDamage);
/*      */       
/*      */       case 'M':
/* 2281 */         if (this.isMagicNumberModified) {
/* 2282 */           if (this.magicNumber >= this.baseMagicNumber) {
/* 2283 */             return "[#7fff00]" + Integer.toString(this.magicNumber) + "[]";
/*      */           }
/* 2285 */           return "[#ff6563]" + Integer.toString(this.magicNumber) + "[]";
/*      */         } 
/*      */         
/* 2288 */         return Integer.toString(this.baseMagicNumber);
/*      */     } 
/*      */     
/* 2291 */     logger.info("KEY: " + key);
/* 2292 */     return Integer.toString(-99);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderDescriptionCN(SpriteBatch sb) {
/* 2300 */     if (!this.isSeen || this.isLocked) {
/* 2301 */       FontHelper.menuBannerFont.getData().setScale(this.drawScale * 1.25F);
/* 2302 */       FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", this.current_x, this.current_y, 0.0F, -200.0F * Settings.scale * this.drawScale / 2.0F, this.angle, true, this.textColor);
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
/* 2313 */       FontHelper.menuBannerFont.getData().setScale(1.0F);
/*      */       
/*      */       return;
/*      */     } 
/* 2317 */     BitmapFont font = getDescFont();
/* 2318 */     float draw_y = this.current_y - IMG_HEIGHT * this.drawScale / 2.0F + DESC_OFFSET_Y * this.drawScale;
/* 2319 */     draw_y += this.description.size() * font.getCapHeight() * 0.775F - font.getCapHeight() * 0.375F;
/* 2320 */     float spacing = 1.45F * -font.getCapHeight() / Settings.scale / this.drawScale;
/*      */     
/* 2322 */     for (int i = 0; i < this.description.size(); i++) {
/*      */       
/* 2324 */       float start_x = 0.0F;
/* 2325 */       if (Settings.leftAlignCards) {
/* 2326 */         start_x = this.current_x - DESC_BOX_WIDTH * this.drawScale / 2.0F + 2.0F * Settings.scale;
/*      */       } else {
/* 2328 */         start_x = this.current_x - ((DescriptionLine)this.description.get(i)).width * this.drawScale / 2.0F - 14.0F * Settings.scale;
/*      */       } 
/*      */       
/* 2331 */       for (String tmp : ((DescriptionLine)this.description.get(i)).getCachedTokenizedTextCN()) {
/* 2332 */         String updateTmp = null; int j;
/* 2333 */         for (j = 0; j < tmp.length(); j++) {
/* 2334 */           if (tmp.charAt(j) == 'D' || (tmp.charAt(j) == 'B' && !tmp.contains("[B]")) || tmp.charAt(j) == 'M') {
/* 2335 */             updateTmp = tmp.substring(0, j);
/* 2336 */             updateTmp = updateTmp + getDynamicValue(tmp.charAt(j));
/* 2337 */             updateTmp = updateTmp + tmp.substring(j + 1);
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/* 2342 */         if (updateTmp != null) {
/* 2343 */           tmp = updateTmp;
/*      */         }
/*      */ 
/*      */         
/* 2347 */         for (j = 0; j < tmp.length(); j++) {
/* 2348 */           if (tmp.charAt(j) == 'D' || (tmp.charAt(j) == 'B' && !tmp.contains("[B]")) || tmp.charAt(j) == 'M') {
/*      */             
/* 2350 */             updateTmp = tmp.substring(0, j);
/* 2351 */             updateTmp = updateTmp + getDynamicValue(tmp.charAt(j));
/* 2352 */             updateTmp = updateTmp + tmp.substring(j + 1);
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/* 2357 */         if (updateTmp != null) {
/* 2358 */           tmp = updateTmp;
/*      */         }
/*      */ 
/*      */         
/* 2362 */         if (tmp.length() > 0 && tmp.charAt(0) == '*') {
/* 2363 */           tmp = tmp.substring(1);
/* 2364 */           String punctuation = "";
/* 2365 */           if (tmp.length() > 1 && tmp.charAt(tmp.length() - 2) != '+' && !Character.isLetter(tmp
/* 2366 */               .charAt(tmp.length() - 2))) {
/* 2367 */             punctuation = punctuation + tmp.charAt(tmp.length() - 2);
/* 2368 */             tmp = tmp.substring(0, tmp.length() - 2);
/* 2369 */             punctuation = punctuation + ' ';
/*      */           } 
/*      */           
/* 2372 */           gl.setText(font, tmp);
/* 2373 */           FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2380 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.goldColor);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2385 */           start_x = Math.round(start_x + gl.width);
/* 2386 */           gl.setText(font, punctuation);
/* 2387 */           FontHelper.renderRotatedText(sb, font, punctuation, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2394 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2399 */           gl.setText(font, punctuation);
/* 2400 */           start_x += gl.width;
/*      */         
/*      */         }
/* 2403 */         else if (tmp.equals("[R]")) {
/* 2404 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2405 */           renderSmallEnergy(sb, orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2409 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2411 */           start_x += gl.width;
/* 2412 */         } else if (tmp.equals("[G]")) {
/* 2413 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2414 */           renderSmallEnergy(sb, orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2418 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2420 */           start_x += gl.width;
/* 2421 */         } else if (tmp.equals("[B]")) {
/* 2422 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2423 */           renderSmallEnergy(sb, orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2427 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2429 */           start_x += gl.width;
/*      */         }
/* 2431 */         else if (tmp.equals("[W]")) {
/* 2432 */           gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
/* 2433 */           renderSmallEnergy(sb, orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - ((this.description
/*      */ 
/*      */ 
/*      */               
/* 2437 */               .size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
/*      */           
/* 2439 */           start_x += gl.width;
/*      */         }
/*      */         else {
/*      */           
/* 2443 */           gl.setText(font, tmp);
/* 2444 */           FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, i * 1.45F * 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2451 */               -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
/*      */ 
/*      */ 
/*      */           
/* 2455 */           start_x += gl.width;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2459 */     font.getData().setScale(1.0F);
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
/*      */   private float renderDynamicVariable(char key, float start_x, float draw_y, int i, BitmapFont font, SpriteBatch sb, Character end) {
/* 2471 */     sbuilder.setLength(0);
/* 2472 */     Color c = null;
/* 2473 */     int num = 0;
/*      */     
/* 2475 */     switch (key) {
/*      */       case 'D':
/* 2477 */         if (this.isDamageModified) {
/* 2478 */           num = this.damage;
/* 2479 */           if (this.damage >= this.baseDamage) {
/* 2480 */             c = Settings.GREEN_TEXT_COLOR; break;
/*      */           } 
/* 2482 */           c = Settings.RED_TEXT_COLOR;
/*      */           break;
/*      */         } 
/* 2485 */         c = this.textColor;
/* 2486 */         num = this.baseDamage;
/*      */         break;
/*      */       
/*      */       case 'B':
/* 2490 */         if (this.isBlockModified) {
/* 2491 */           num = this.block;
/* 2492 */           if (this.block >= this.baseBlock) {
/* 2493 */             c = Settings.GREEN_TEXT_COLOR; break;
/*      */           } 
/* 2495 */           c = Settings.RED_TEXT_COLOR;
/*      */           break;
/*      */         } 
/* 2498 */         c = this.textColor;
/* 2499 */         num = this.baseBlock;
/*      */         break;
/*      */       
/*      */       case 'M':
/* 2503 */         if (this.isMagicNumberModified) {
/* 2504 */           num = this.magicNumber;
/* 2505 */           if (this.magicNumber >= this.baseMagicNumber) {
/* 2506 */             c = Settings.GREEN_TEXT_COLOR; break;
/*      */           } 
/* 2508 */           c = Settings.RED_TEXT_COLOR;
/*      */           break;
/*      */         } 
/* 2511 */         c = this.textColor;
/* 2512 */         num = this.baseMagicNumber;
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2519 */     sbuilder.append(Integer.toString(num));
/* 2520 */     gl.setText(font, sbuilder);
/* 2521 */     FontHelper.renderRotatedText(sb, font, sbuilder
/*      */ 
/*      */         
/* 2524 */         .toString(), this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, i * 1.45F * 
/*      */ 
/*      */ 
/*      */         
/* 2528 */         -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, c);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2533 */     if (end != null) {
/* 2534 */       FontHelper.renderRotatedText(sb, font, 
/*      */ 
/*      */           
/* 2537 */           Character.toString(end.charValue()), this.current_x, this.current_y, start_x - this.current_x + gl.width + 4.0F * Settings.scale, i * 1.45F * 
/*      */ 
/*      */ 
/*      */           
/* 2541 */           -font.getCapHeight() + draw_y - this.current_y + -6.0F, 0.0F, true, Settings.CREAM_COLOR);
/*      */ 
/*      */ 
/*      */       
/* 2545 */       sbuilder.append(end);
/*      */     } 
/*      */     
/* 2548 */     sbuilder.append(' ');
/* 2549 */     gl.setText(font, sbuilder);
/* 2550 */     return gl.width;
/*      */   }
/*      */   
/*      */   private BitmapFont getDescFont() {
/* 2554 */     BitmapFont font = null;
/*      */     
/* 2556 */     if (this.angle == 0.0F && this.drawScale == 1.0F) {
/* 2557 */       font = FontHelper.cardDescFont_N;
/*      */     } else {
/* 2559 */       font = FontHelper.cardDescFont_L;
/*      */     } 
/*      */     
/* 2562 */     font.getData().setScale(this.drawScale);
/* 2563 */     return font;
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderTitle(SpriteBatch sb) {
/* 2568 */     if (this.isLocked) {
/* 2569 */       FontHelper.cardTitleFont.getData().setScale(this.drawScale);
/* 2570 */       FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, LOCKED_STRING, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, this.renderColor);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2584 */     if (!this.isSeen) {
/* 2585 */       FontHelper.cardTitleFont.getData().setScale(this.drawScale);
/* 2586 */       FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, UNKNOWN_STRING, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, this.renderColor);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2601 */     if (!this.useSmallTitleFont) {
/* 2602 */       FontHelper.cardTitleFont.getData().setScale(this.drawScale);
/*      */     } else {
/* 2604 */       FontHelper.cardTitleFont.getData().setScale(this.drawScale * 0.85F);
/*      */     } 
/*      */     
/* 2607 */     if (this.upgraded) {
/* 2608 */       Color color = Settings.GREEN_TEXT_COLOR.cpy();
/* 2609 */       color.a = this.renderColor.a;
/* 2610 */       FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, this.name, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, color);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2622 */       FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, this.name, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, this.renderColor);
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
/*      */   
/*      */   private void renderType(SpriteBatch sb) {
/*      */     String text;
/* 2640 */     switch (this.type) {
/*      */       case BASIC:
/* 2642 */         text = TEXT[0];
/*      */         break;
/*      */       case COMMON:
/* 2645 */         text = TEXT[1];
/*      */         break;
/*      */       case UNCOMMON:
/* 2648 */         text = TEXT[2];
/*      */         break;
/*      */       case SPECIAL:
/* 2651 */         text = TEXT[7];
/*      */         break;
/*      */       case CURSE:
/* 2654 */         text = TEXT[3];
/*      */         break;
/*      */       default:
/* 2657 */         text = TEXT[5];
/*      */         break;
/*      */     } 
/*      */     
/* 2661 */     BitmapFont font = FontHelper.cardTypeFont;
/* 2662 */     font.getData().setScale(this.drawScale);
/*      */     
/* 2664 */     this.typeColor.a = this.renderColor.a;
/* 2665 */     FontHelper.renderRotatedText(sb, font, text, this.current_x, this.current_y - 22.0F * this.drawScale * Settings.scale, 0.0F, -1.0F * this.drawScale * Settings.scale, this.angle, false, this.typeColor);
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
/*      */   public static int getPrice(CardRarity rarity) {
/* 2679 */     switch (rarity) {
/*      */       case BASIC:
/* 2681 */         logger.info("ERROR: WHY WE SELLIN' BASIC");
/* 2682 */         return 9999;
/*      */       case COMMON:
/* 2684 */         return 50;
/*      */       case UNCOMMON:
/* 2686 */         return 75;
/*      */       case RARE:
/* 2688 */         return 150;
/*      */       case SPECIAL:
/* 2690 */         logger.info("ERROR: WHY WE SELLIN' SPECIAL");
/* 2691 */         return 9999;
/*      */     } 
/* 2693 */     logger.info("No rarity on this card?");
/* 2694 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderEnergy(SpriteBatch sb) {
/* 2699 */     if (this.cost <= -2 || this.darken || this.isLocked || !this.isSeen) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 2704 */     switch (this.color) {
/*      */       case BASIC:
/* 2706 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_RED_ORB, this.current_x, this.current_y);
/*      */         break;
/*      */       case CURSE:
/* 2709 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_GREEN_ORB, this.current_x, this.current_y);
/*      */         break;
/*      */       case SPECIAL:
/* 2712 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_BLUE_ORB, this.current_x, this.current_y);
/*      */         break;
/*      */       case COMMON:
/* 2715 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_PURPLE_ORB, this.current_x, this.current_y);
/*      */         break;
/*      */       case UNCOMMON:
/* 2718 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);
/*      */       default:
/* 2720 */         renderHelper(sb, this.renderColor, ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);
/*      */         break;
/*      */     } 
/*      */     
/* 2724 */     Color costColor = Color.WHITE.cpy();
/* 2725 */     if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(this) && !hasEnoughEnergy()) {
/* 2726 */       costColor = ENERGY_COST_RESTRICTED_COLOR;
/* 2727 */     } else if (this.isCostModified || this.isCostModifiedForTurn || freeToPlay()) {
/* 2728 */       costColor = ENERGY_COST_MODIFIED_COLOR;
/*      */     } 
/* 2730 */     costColor.a = this.transparency;
/*      */     
/* 2732 */     String text = getCost();
/* 2733 */     BitmapFont font = getEnergyFont();
/*      */     
/* 2735 */     if ((this.type != CardType.STATUS || this.cardID.equals("Slimed")) && (this.color != CardColor.CURSE || this.cardID.equals("Pride")))
/*      */     {
/* 2737 */       FontHelper.renderRotatedText(sb, font, text, this.current_x, this.current_y, -132.0F * this.drawScale * Settings.scale, 192.0F * this.drawScale * Settings.scale, this.angle, false, costColor);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateCost(int amt) {
/* 2758 */     if ((this.color != CardColor.CURSE || this.cardID.equals("Pride")) && (this.type != CardType.STATUS || this.cardID.equals("Slimed"))) {
/*      */       
/* 2760 */       int tmpCost = this.cost;
/* 2761 */       int diff = this.cost - this.costForTurn;
/*      */       
/* 2763 */       tmpCost += amt;
/* 2764 */       if (tmpCost < 0) {
/* 2765 */         tmpCost = 0;
/*      */       }
/*      */       
/* 2768 */       if (tmpCost != this.cost) {
/* 2769 */         this.isCostModified = true;
/* 2770 */         this.cost = tmpCost;
/* 2771 */         this.costForTurn = this.cost - diff;
/*      */         
/* 2773 */         if (this.costForTurn < 0) {
/* 2774 */           this.costForTurn = 0;
/*      */         }
/*      */       } 
/*      */     } else {
/* 2778 */       logger.info("Curses/Statuses cannot have their costs modified");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCostForTurn(int amt) {
/* 2788 */     if (this.costForTurn >= 0) {
/* 2789 */       this.costForTurn = amt;
/* 2790 */       if (this.costForTurn < 0) {
/* 2791 */         this.costForTurn = 0;
/*      */       }
/*      */       
/* 2794 */       if (this.costForTurn != this.cost) {
/* 2795 */         this.isCostModifiedForTurn = true;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void modifyCostForCombat(int amt) {
/* 2806 */     if (this.costForTurn > 0) {
/* 2807 */       this.costForTurn += amt;
/* 2808 */       if (this.costForTurn < 0) {
/* 2809 */         this.costForTurn = 0;
/*      */       }
/*      */       
/* 2812 */       if (this.cost != this.costForTurn) {
/* 2813 */         this.isCostModified = true;
/*      */       }
/* 2815 */       this.cost = this.costForTurn;
/*      */     }
/* 2817 */     else if (this.cost >= 0) {
/* 2818 */       this.cost += amt;
/* 2819 */       if (this.cost < 0) {
/* 2820 */         this.cost = 0;
/*      */       }
/* 2822 */       this.costForTurn = 0;
/* 2823 */       if (this.cost != this.costForTurn) {
/* 2824 */         this.isCostModified = true;
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
/*      */   public void resetAttributes() {
/* 2836 */     this.block = this.baseBlock;
/* 2837 */     this.isBlockModified = false;
/* 2838 */     this.damage = this.baseDamage;
/* 2839 */     this.isDamageModified = false;
/* 2840 */     this.magicNumber = this.baseMagicNumber;
/* 2841 */     this.isMagicNumberModified = false;
/* 2842 */     this.damageTypeForTurn = this.damageType;
/* 2843 */     this.costForTurn = this.cost;
/* 2844 */     this.isCostModifiedForTurn = false;
/*      */   }
/*      */   
/*      */   private String getCost() {
/* 2848 */     if (this.cost == -1)
/* 2849 */       return "X"; 
/* 2850 */     if (freeToPlay()) {
/* 2851 */       return "0";
/*      */     }
/* 2853 */     return Integer.toString(this.costForTurn);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean freeToPlay() {
/* 2858 */     if (this.freeToPlayOnce)
/* 2859 */       return true; 
/* 2860 */     if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && 
/* 2861 */       (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && 
/* 2862 */       AbstractDungeon.player.hasPower("FreeAttackPower") && this.type == CardType.ATTACK) {
/* 2863 */       return true;
/*      */     }
/*      */     
/* 2866 */     return false;
/*      */   }
/*      */   
/*      */   private BitmapFont getEnergyFont() {
/* 2870 */     FontHelper.cardEnergyFont_L.getData().setScale(this.drawScale);
/* 2871 */     return FontHelper.cardEnergyFont_L;
/*      */   }
/*      */   
/*      */   public void hover() {
/* 2875 */     if (!this.hovered) {
/* 2876 */       this.hovered = true;
/* 2877 */       this.drawScale = 1.0F;
/* 2878 */       this.targetDrawScale = 1.0F;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void unhover() {
/* 2883 */     if (this.hovered) {
/* 2884 */       this.hovered = false;
/* 2885 */       this.hoverDuration = 0.0F;
/* 2886 */       this.renderTip = false;
/* 2887 */       this.targetDrawScale = 0.75F;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateHoverLogic() {
/* 2892 */     this.hb.update();
/* 2893 */     if (this.hb.hovered) {
/* 2894 */       hover();
/* 2895 */       this.hoverDuration += Gdx.graphics.getDeltaTime();
/* 2896 */       if (this.hoverDuration > 0.2F && !Settings.hideCards) {
/* 2897 */         this.renderTip = true;
/*      */       }
/*      */     } else {
/* 2900 */       unhover();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void untip() {
/* 2905 */     this.hoverDuration = 0.0F;
/* 2906 */     this.renderTip = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveToDiscardPile() {
/* 2913 */     this.target_x = CardGroup.DISCARD_PILE_X;
/* 2914 */     if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/* 2915 */       this.target_y = 0.0F;
/*      */     } else {
/* 2917 */       this.target_y = 0.0F - OverlayMenu.HAND_HIDE_Y;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void teleportToDiscardPile() {
/* 2925 */     this.current_x = CardGroup.DISCARD_PILE_X;
/* 2926 */     this.target_x = this.current_x;
/* 2927 */     if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
/* 2928 */       this.current_y = 0.0F;
/*      */     } else {
/* 2930 */       this.current_y = 0.0F - OverlayMenu.HAND_HIDE_Y;
/*      */     } 
/* 2932 */     this.target_y = this.current_y;
/* 2933 */     onMoveToDiscard();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMoveToDiscard() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renderCardTip(SpriteBatch sb) {
/* 2946 */     if (!Settings.hideCards && this.renderTip) {
/* 2947 */       if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && !Settings.isTouchScreen) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 2952 */       if (this.isLocked) {
/* 2953 */         ArrayList<String> locked = new ArrayList<>();
/* 2954 */         locked.add(0, "locked");
/* 2955 */         TipHelper.renderTipForCard(this, sb, locked); return;
/*      */       } 
/* 2957 */       if (!this.isSeen) {
/* 2958 */         ArrayList<String> unseen = new ArrayList<>();
/* 2959 */         unseen.add(0, "unseen");
/* 2960 */         TipHelper.renderTipForCard(this, sb, unseen);
/*      */         
/*      */         return;
/*      */       } 
/* 2964 */       if (SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked) {
/* 2965 */         AbstractCard copy = makeCopy();
/* 2966 */         copy.current_x = this.current_x;
/* 2967 */         copy.current_y = this.current_y;
/* 2968 */         copy.drawScale = this.drawScale;
/* 2969 */         copy.upgrade();
/*      */         
/* 2971 */         TipHelper.renderTipForCard(copy, sb, copy.keywords);
/*      */       } else {
/* 2973 */         TipHelper.renderTipForCard(this, sb, this.keywords);
/*      */       } 
/*      */       
/* 2976 */       if (this.cardsToPreview != null) {
/* 2977 */         renderCardPreview(sb);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void renderCardPreviewInSingleView(SpriteBatch sb) {
/* 2983 */     this.cardsToPreview.current_x = 1435.0F * Settings.scale;
/* 2984 */     this.cardsToPreview.current_y = 795.0F * Settings.scale;
/* 2985 */     this.cardsToPreview.drawScale = 0.8F;
/* 2986 */     this.cardsToPreview.render(sb);
/*      */   }
/*      */   
/*      */   public void renderCardPreview(SpriteBatch sb) {
/* 2990 */     if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard) {
/*      */       return;
/*      */     }
/*      */     
/* 2994 */     float tmpScale = this.drawScale * 0.8F;
/*      */     
/* 2996 */     if (this.current_x > Settings.WIDTH * 0.75F) {
/* 2997 */       this.current_x += (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
/*      */     } else {
/*      */       
/* 3000 */       this.current_x -= (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
/*      */     } 
/*      */ 
/*      */     
/* 3004 */     this.current_y += (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
/*      */ 
/*      */     
/* 3007 */     this.cardsToPreview.drawScale = tmpScale;
/* 3008 */     this.cardsToPreview.render(sb);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerWhenDrawn() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerWhenCopied() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnEndOfPlayerTurn() {
/* 3027 */     if (this.isEthereal) {
/* 3028 */       addToTop((AbstractGameAction)new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnEndOfTurnForPlayingCard() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnOtherCardPlayed(AbstractCard c) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnGainEnergy(int e, boolean dueToCard) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnManualDiscard() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnCardPlayed(AbstractCard cardPlayed) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnScry() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerExhaustedCardsOnStanceChange(AbstractStance newStance) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerAtStartOfTurn() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onPlayCard(AbstractCard c, AbstractMonster m) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void atTurnStart() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void atTurnStartPreDraw() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onChoseThisOption() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onRetained() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerOnExhaust() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void applyPowers() {
/* 3097 */     applyPowersToBlock();
/* 3098 */     AbstractPlayer player = AbstractDungeon.player;
/* 3099 */     this.isDamageModified = false;
/*      */ 
/*      */     
/* 3102 */     if (!this.isMultiDamage) {
/* 3103 */       float tmp = this.baseDamage;
/*      */ 
/*      */       
/* 3106 */       for (AbstractRelic r : player.relics) {
/* 3107 */         tmp = r.atDamageModify(tmp, this);
/* 3108 */         if (this.baseDamage != (int)tmp) {
/* 3109 */           this.isDamageModified = true;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3115 */       for (AbstractPower p : player.powers) {
/* 3116 */         tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this);
/*      */       }
/*      */ 
/*      */       
/* 3120 */       tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
/* 3121 */       if (this.baseDamage != (int)tmp) {
/* 3122 */         this.isDamageModified = true;
/*      */       }
/*      */ 
/*      */       
/* 3126 */       for (AbstractPower p : player.powers) {
/* 3127 */         tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
/*      */       }
/*      */ 
/*      */       
/* 3131 */       if (tmp < 0.0F) {
/* 3132 */         tmp = 0.0F;
/*      */       }
/*      */ 
/*      */       
/* 3136 */       if (this.baseDamage != MathUtils.floor(tmp)) {
/* 3137 */         this.isDamageModified = true;
/*      */       }
/* 3139 */       this.damage = MathUtils.floor(tmp);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3144 */       ArrayList<AbstractMonster> m = (AbstractDungeon.getCurrRoom()).monsters.monsters;
/* 3145 */       float[] tmp = new float[m.size()];
/*      */       int i;
/* 3147 */       for (i = 0; i < tmp.length; i++) {
/* 3148 */         tmp[i] = this.baseDamage;
/*      */       }
/*      */ 
/*      */       
/* 3152 */       for (i = 0; i < tmp.length; i++) {
/*      */         
/* 3154 */         for (AbstractRelic r : player.relics) {
/* 3155 */           tmp[i] = r.atDamageModify(tmp[i], this);
/* 3156 */           if (this.baseDamage != (int)tmp[i]) {
/* 3157 */             this.isDamageModified = true;
/*      */           }
/*      */         } 
/*      */         
/* 3161 */         for (AbstractPower p : player.powers) {
/* 3162 */           tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn, this);
/*      */         }
/*      */ 
/*      */         
/* 3166 */         tmp[i] = player.stance.atDamageGive(tmp[i], this.damageTypeForTurn, this);
/* 3167 */         if (this.baseDamage != (int)tmp[i]) {
/* 3168 */           this.isDamageModified = true;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 3173 */       for (i = 0; i < tmp.length; i++) {
/* 3174 */         for (AbstractPower p : player.powers) {
/* 3175 */           tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn, this);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 3180 */       for (i = 0; i < tmp.length; i++) {
/* 3181 */         if (tmp[i] < 0.0F) {
/* 3182 */           tmp[i] = 0.0F;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 3187 */       this.multiDamage = new int[tmp.length];
/* 3188 */       for (i = 0; i < tmp.length; i++) {
/* 3189 */         if (this.baseDamage != (int)tmp[i]) {
/* 3190 */           this.isDamageModified = true;
/*      */         }
/* 3192 */         this.multiDamage[i] = MathUtils.floor(tmp[i]);
/*      */       } 
/* 3194 */       this.damage = this.multiDamage[0];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyPowersToBlock() {
/* 3200 */     this.isBlockModified = false;
/* 3201 */     float tmp = this.baseBlock;
/*      */     
/* 3203 */     for (AbstractPower p : AbstractDungeon.player.powers) {
/* 3204 */       tmp = p.modifyBlock(tmp, this);
/*      */     }
/*      */     
/* 3207 */     for (AbstractPower p : AbstractDungeon.player.powers) {
/* 3208 */       tmp = p.modifyBlockLast(tmp);
/*      */     }
/*      */     
/* 3211 */     if (this.baseBlock != MathUtils.floor(tmp)) {
/* 3212 */       this.isBlockModified = true;
/*      */     }
/*      */     
/* 3215 */     if (tmp < 0.0F) {
/* 3216 */       tmp = 0.0F;
/*      */     }
/* 3218 */     this.block = MathUtils.floor(tmp);
/*      */   }
/*      */   
/*      */   public void calculateDamageDisplay(AbstractMonster mo) {
/* 3222 */     calculateCardDamage(mo);
/*      */   }
/*      */   
/*      */   public void calculateCardDamage(AbstractMonster mo) {
/* 3226 */     applyPowersToBlock();
/* 3227 */     AbstractPlayer player = AbstractDungeon.player;
/* 3228 */     this.isDamageModified = false;
/*      */ 
/*      */     
/* 3231 */     if (!this.isMultiDamage && mo != null) {
/* 3232 */       float tmp = this.baseDamage;
/*      */ 
/*      */       
/* 3235 */       for (AbstractRelic r : player.relics) {
/* 3236 */         tmp = r.atDamageModify(tmp, this);
/* 3237 */         if (this.baseDamage != (int)tmp) {
/* 3238 */           this.isDamageModified = true;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3244 */       for (AbstractPower p : player.powers) {
/* 3245 */         tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this);
/*      */       }
/*      */ 
/*      */       
/* 3249 */       tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
/* 3250 */       if (this.baseDamage != (int)tmp) {
/* 3251 */         this.isDamageModified = true;
/*      */       }
/*      */ 
/*      */       
/* 3255 */       for (AbstractPower p : mo.powers) {
/* 3256 */         tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this);
/*      */       }
/*      */ 
/*      */       
/* 3260 */       for (AbstractPower p : player.powers) {
/* 3261 */         tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
/*      */       }
/*      */ 
/*      */       
/* 3265 */       for (AbstractPower p : mo.powers) {
/* 3266 */         tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
/*      */       }
/*      */ 
/*      */       
/* 3270 */       if (tmp < 0.0F) {
/* 3271 */         tmp = 0.0F;
/*      */       }
/*      */ 
/*      */       
/* 3275 */       if (this.baseDamage != MathUtils.floor(tmp)) {
/* 3276 */         this.isDamageModified = true;
/*      */       }
/* 3278 */       this.damage = MathUtils.floor(tmp);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3283 */       ArrayList<AbstractMonster> m = (AbstractDungeon.getCurrRoom()).monsters.monsters;
/* 3284 */       float[] tmp = new float[m.size()];
/*      */       int i;
/* 3286 */       for (i = 0; i < tmp.length; i++) {
/* 3287 */         tmp[i] = this.baseDamage;
/*      */       }
/*      */ 
/*      */       
/* 3291 */       for (i = 0; i < tmp.length; i++) {
/*      */ 
/*      */         
/* 3294 */         for (AbstractRelic r : player.relics) {
/* 3295 */           tmp[i] = r.atDamageModify(tmp[i], this);
/* 3296 */           if (this.baseDamage != (int)tmp[i]) {
/* 3297 */             this.isDamageModified = true;
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 3302 */         for (AbstractPower p : player.powers) {
/* 3303 */           tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn, this);
/*      */         }
/*      */ 
/*      */         
/* 3307 */         tmp[i] = player.stance.atDamageGive(tmp[i], this.damageTypeForTurn, this);
/* 3308 */         if (this.baseDamage != (int)tmp[i]) {
/* 3309 */           this.isDamageModified = true;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 3314 */       for (i = 0; i < tmp.length; i++) {
/* 3315 */         for (AbstractPower p : ((AbstractMonster)m.get(i)).powers) {
/* 3316 */           if (!((AbstractMonster)m.get(i)).isDying && !((AbstractMonster)m.get(i)).isEscaping) {
/* 3317 */             tmp[i] = p.atDamageReceive(tmp[i], this.damageTypeForTurn, this);
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 3323 */       for (i = 0; i < tmp.length; i++) {
/* 3324 */         for (AbstractPower p : player.powers) {
/* 3325 */           tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn, this);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 3330 */       for (i = 0; i < tmp.length; i++) {
/* 3331 */         for (AbstractPower p : ((AbstractMonster)m.get(i)).powers) {
/* 3332 */           if (!((AbstractMonster)m.get(i)).isDying && !((AbstractMonster)m.get(i)).isEscaping) {
/* 3333 */             tmp[i] = p.atDamageFinalReceive(tmp[i], this.damageTypeForTurn, this);
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 3339 */       for (i = 0; i < tmp.length; i++) {
/* 3340 */         if (tmp[i] < 0.0F) {
/* 3341 */           tmp[i] = 0.0F;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 3346 */       this.multiDamage = new int[tmp.length];
/* 3347 */       for (i = 0; i < tmp.length; i++) {
/* 3348 */         if (this.baseDamage != MathUtils.floor(tmp[i])) {
/* 3349 */           this.isDamageModified = true;
/*      */         }
/* 3351 */         this.multiDamage[i] = MathUtils.floor(tmp[i]);
/*      */       } 
/* 3353 */       this.damage = this.multiDamage[0];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAngle(float degrees, boolean immediate) {
/* 3359 */     this.targetAngle = degrees;
/* 3360 */     if (immediate) {
/* 3361 */       this.angle = this.targetAngle;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void shrink() {
/* 3369 */     this.targetDrawScale = 0.12F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void shrink(boolean immediate) {
/* 3376 */     this.targetDrawScale = 0.12F;
/* 3377 */     this.drawScale = 0.12F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void darken(boolean immediate) {
/* 3384 */     this.darken = true;
/* 3385 */     this.darkTimer = 0.3F;
/* 3386 */     if (immediate) {
/* 3387 */       this.tintColor.a = 1.0F;
/* 3388 */       this.darkTimer = 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void lighten(boolean immediate) {
/* 3396 */     this.darken = false;
/* 3397 */     this.darkTimer = 0.3F;
/* 3398 */     if (immediate) {
/* 3399 */       this.tintColor.a = 0.0F;
/* 3400 */       this.darkTimer = 0.0F;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateColor() {
/* 3405 */     if (this.darkTimer != 0.0F) {
/* 3406 */       this.darkTimer -= Gdx.graphics.getDeltaTime();
/* 3407 */       if (this.darkTimer < 0.0F) {
/* 3408 */         this.darkTimer = 0.0F;
/*      */       }
/*      */       
/* 3411 */       if (this.darken) {
/* 3412 */         this.tintColor.a = 1.0F - this.darkTimer * 1.0F / 0.3F;
/*      */       } else {
/*      */         
/* 3415 */         this.tintColor.a = this.darkTimer * 1.0F / 0.3F;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void superFlash(Color c) {
/* 3421 */     this.flashVfx = (AbstractGameEffect)new CardFlashVfx(this, c, true);
/*      */   }
/*      */   
/*      */   public void superFlash() {
/* 3425 */     this.flashVfx = (AbstractGameEffect)new CardFlashVfx(this, true);
/*      */   }
/*      */   
/*      */   public void flash() {
/* 3429 */     this.flashVfx = (AbstractGameEffect)new CardFlashVfx(this);
/*      */   }
/*      */   
/*      */   public void flash(Color c) {
/* 3433 */     this.flashVfx = (AbstractGameEffect)new CardFlashVfx(this, c);
/*      */   }
/*      */   
/*      */   public void unfadeOut() {
/* 3437 */     this.fadingOut = false;
/* 3438 */     this.transparency = 1.0F;
/* 3439 */     this.targetTransparency = 1.0F;
/*      */     
/* 3441 */     this.bannerColor.a = this.transparency;
/* 3442 */     this.backColor.a = this.transparency;
/* 3443 */     this.frameColor.a = this.transparency;
/* 3444 */     this.bgColor.a = this.transparency;
/* 3445 */     this.descBoxColor.a = this.transparency;
/* 3446 */     this.imgFrameColor.a = this.transparency;
/* 3447 */     this.frameShadowColor.a = this.transparency / 4.0F;
/* 3448 */     this.renderColor.a = this.transparency;
/* 3449 */     this.goldColor.a = this.transparency;
/*      */     
/* 3451 */     if (this.frameOutlineColor != null) {
/* 3452 */       this.frameOutlineColor.a = this.transparency;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateTransparency() {
/* 3460 */     if (this.fadingOut && this.transparency != 0.0F) {
/* 3461 */       this.transparency -= Gdx.graphics.getDeltaTime() * 2.0F;
/* 3462 */       if (this.transparency < 0.0F) {
/* 3463 */         this.transparency = 0.0F;
/*      */       }
/* 3465 */     } else if (this.transparency != this.targetTransparency) {
/* 3466 */       this.transparency += Gdx.graphics.getDeltaTime() * 2.0F;
/* 3467 */       if (this.transparency > this.targetTransparency) {
/* 3468 */         this.transparency = this.targetTransparency;
/*      */       }
/*      */     } 
/*      */     
/* 3472 */     this.bannerColor.a = this.transparency;
/* 3473 */     this.backColor.a = this.transparency;
/* 3474 */     this.frameColor.a = this.transparency;
/* 3475 */     this.bgColor.a = this.transparency;
/* 3476 */     this.descBoxColor.a = this.transparency;
/* 3477 */     this.imgFrameColor.a = this.transparency;
/* 3478 */     this.frameShadowColor.a = this.transparency / 4.0F;
/* 3479 */     this.renderColor.a = this.transparency;
/* 3480 */     this.textColor.a = this.transparency;
/* 3481 */     this.goldColor.a = this.transparency;
/*      */     
/* 3483 */     if (this.frameOutlineColor != null) {
/* 3484 */       this.frameOutlineColor.a = this.transparency;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setAngle(float degrees) {
/* 3489 */     setAngle(degrees, false);
/*      */   }
/*      */   
/*      */   protected String getCantPlayMessage() {
/* 3493 */     return TEXT[13];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearPowers() {
/* 3500 */     resetAttributes();
/* 3501 */     this.isDamageModified = false;
/*      */   }
/*      */   
/*      */   public static void debugPrintDetailedCardDataHeader() {
/* 3505 */     logger.info(gameDataUploadHeader());
/*      */   }
/*      */   
/*      */   public static String gameDataUploadHeader() {
/* 3509 */     GameDataStringBuilder builder = new GameDataStringBuilder();
/*      */ 
/*      */     
/* 3512 */     builder.addFieldData("name");
/* 3513 */     builder.addFieldData("cardID");
/* 3514 */     builder.addFieldData("rawDescription");
/* 3515 */     builder.addFieldData("assetURL");
/*      */ 
/*      */ 
/*      */     
/* 3519 */     builder.addFieldData("keywords");
/* 3520 */     builder.addFieldData("color");
/* 3521 */     builder.addFieldData("type");
/* 3522 */     builder.addFieldData("rarity");
/*      */ 
/*      */     
/* 3525 */     builder.addFieldData("cost");
/* 3526 */     builder.addFieldData("target");
/* 3527 */     builder.addFieldData("damageType");
/*      */     
/* 3529 */     builder.addFieldData("baseDamage");
/* 3530 */     builder.addFieldData("baseBlock");
/* 3531 */     builder.addFieldData("baseHeal");
/* 3532 */     builder.addFieldData("baseDraw");
/* 3533 */     builder.addFieldData("baseDiscard");
/* 3534 */     builder.addFieldData("baseMagicNumber");
/* 3535 */     builder.addFieldData("isMultiDamage");
/* 3536 */     return builder.toString();
/*      */   }
/*      */   
/*      */   public void debugPrintDetailedCardData() {
/* 3540 */     logger.info(gameDataUploadData());
/*      */   }
/*      */   
/*      */   protected void addToBot(AbstractGameAction action) {
/* 3544 */     AbstractDungeon.actionManager.addToBottom(action);
/*      */   }
/*      */   
/*      */   protected void addToTop(AbstractGameAction action) {
/* 3548 */     AbstractDungeon.actionManager.addToTop(action);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String gameDataUploadData() {
/* 3555 */     GameDataStringBuilder builder = new GameDataStringBuilder();
/*      */ 
/*      */     
/* 3558 */     builder.addFieldData(this.name);
/* 3559 */     builder.addFieldData(this.cardID);
/* 3560 */     builder.addFieldData(this.rawDescription);
/* 3561 */     builder.addFieldData(this.assetUrl);
/*      */ 
/*      */ 
/*      */     
/* 3565 */     builder.addFieldData(Arrays.toString(this.keywords.toArray()));
/* 3566 */     builder.addFieldData(this.color.name());
/* 3567 */     builder.addFieldData(this.type.name());
/* 3568 */     builder.addFieldData(this.rarity.name());
/*      */ 
/*      */     
/* 3571 */     builder.addFieldData(this.cost);
/* 3572 */     builder.addFieldData(this.target.name());
/* 3573 */     builder.addFieldData(this.damageType.name());
/*      */     
/* 3575 */     builder.addFieldData(this.baseDamage);
/* 3576 */     builder.addFieldData(this.baseBlock);
/* 3577 */     builder.addFieldData(this.baseHeal);
/* 3578 */     builder.addFieldData(this.baseDraw);
/* 3579 */     builder.addFieldData(this.baseDiscard);
/* 3580 */     builder.addFieldData(this.baseMagicNumber);
/* 3581 */     builder.addFieldData(this.isMultiDamage);
/*      */     
/* 3583 */     return builder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 3588 */     return this.name;
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(AbstractCard other) {
/* 3593 */     return this.cardID.compareTo(other.cardID);
/*      */   }
/*      */   
/*      */   public void setLocked() {
/* 3597 */     this.isLocked = true;
/* 3598 */     switch (this.type) {
/*      */       case BASIC:
/* 3600 */         this.portraitImg = ImageMaster.CARD_LOCKED_ATTACK;
/*      */         break;
/*      */       case UNCOMMON:
/* 3603 */         this.portraitImg = ImageMaster.CARD_LOCKED_POWER;
/*      */         break;
/*      */       default:
/* 3606 */         this.portraitImg = ImageMaster.CARD_LOCKED_SKILL;
/*      */         break;
/*      */     } 
/* 3609 */     initializeDescription();
/*      */   }
/*      */   
/*      */   public void unlock() {
/* 3613 */     this.isLocked = false;
/*      */     
/* 3615 */     this.portrait = cardAtlas.findRegion(this.assetUrl);
/* 3616 */     if (this.portrait == null) {
/* 3617 */       this.portrait = oldCardAtlas.findRegion(this.assetUrl);
/*      */     }
/*      */   }
/*      */   
/*      */   public HashMap<String, Serializable> getLocStrings() {
/* 3622 */     HashMap<String, Serializable> cardData = new HashMap<>();
/* 3623 */     initializeDescription();
/* 3624 */     cardData.put("name", this.name);
/* 3625 */     cardData.put("description", this.rawDescription);
/* 3626 */     return cardData;
/*      */   }
/*      */   
/*      */   public String getMetricID() {
/* 3630 */     String id = this.cardID;
/* 3631 */     if (this.upgraded) {
/* 3632 */       id = id + "+";
/* 3633 */       if (this.timesUpgraded > 0) {
/* 3634 */         id = id + this.timesUpgraded;
/*      */       }
/*      */     } 
/* 3637 */     return id;
/*      */   }
/*      */   
/*      */   public void triggerOnGlowCheck() {}
/*      */   
/*      */   public abstract void upgrade();
/*      */   
/*      */   public abstract void use(AbstractPlayer paramAbstractPlayer, AbstractMonster paramAbstractMonster);
/*      */   
/*      */   public abstract AbstractCard makeCopy();
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\AbstractCard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */