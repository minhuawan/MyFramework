/*      */ package com.megacrit.cardcrawl.dungeons;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*      */ import com.badlogic.gdx.math.Interpolation;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.megacrit.cardcrawl.actions.GameActionManager;
/*      */ import com.megacrit.cardcrawl.blights.AbstractBlight;
/*      */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*      */ import com.megacrit.cardcrawl.cards.CardGroup;
/*      */ import com.megacrit.cardcrawl.cards.SoulGroup;
/*      */ import com.megacrit.cardcrawl.cards.colorless.SwiftStrike;
/*      */ import com.megacrit.cardcrawl.cards.curses.AscendersBane;
/*      */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*      */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*      */ import com.megacrit.cardcrawl.core.ExceptionHandler;
/*      */ import com.megacrit.cardcrawl.core.OverlayMenu;
/*      */ import com.megacrit.cardcrawl.core.Settings;
/*      */ import com.megacrit.cardcrawl.credits.CreditsScreen;
/*      */ import com.megacrit.cardcrawl.events.AbstractEvent;
/*      */ import com.megacrit.cardcrawl.events.shrines.NoteForYourself;
/*      */ import com.megacrit.cardcrawl.helpers.BlightHelper;
/*      */ import com.megacrit.cardcrawl.helpers.CardHelper;
/*      */ import com.megacrit.cardcrawl.helpers.CardLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.EventHelper;
/*      */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*      */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*      */ import com.megacrit.cardcrawl.helpers.ModHelper;
/*      */ import com.megacrit.cardcrawl.helpers.MonsterHelper;
/*      */ import com.megacrit.cardcrawl.helpers.PotionHelper;
/*      */ import com.megacrit.cardcrawl.helpers.RelicLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.SaveHelper;
/*      */ import com.megacrit.cardcrawl.helpers.TipTracker;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*      */ import com.megacrit.cardcrawl.localization.UIStrings;
/*      */ import com.megacrit.cardcrawl.map.DungeonMap;
/*      */ import com.megacrit.cardcrawl.map.MapEdge;
/*      */ import com.megacrit.cardcrawl.map.MapGenerator;
/*      */ import com.megacrit.cardcrawl.map.MapRoomNode;
/*      */ import com.megacrit.cardcrawl.map.RoomTypeAssigner;
/*      */ import com.megacrit.cardcrawl.metrics.Metrics;
/*      */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*      */ import com.megacrit.cardcrawl.monsters.MonsterGroup;
/*      */ import com.megacrit.cardcrawl.monsters.MonsterInfo;
/*      */ import com.megacrit.cardcrawl.neow.NeowRoom;
/*      */ import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
/*      */ import com.megacrit.cardcrawl.orbs.AbstractOrb;
/*      */ import com.megacrit.cardcrawl.potions.AbstractPotion;
/*      */ import com.megacrit.cardcrawl.random.Random;
/*      */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*      */ import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
/*      */ import com.megacrit.cardcrawl.rewards.chests.LargeChest;
/*      */ import com.megacrit.cardcrawl.rewards.chests.MediumChest;
/*      */ import com.megacrit.cardcrawl.rewards.chests.SmallChest;
/*      */ import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*      */ import com.megacrit.cardcrawl.rooms.EmptyRoom;
/*      */ import com.megacrit.cardcrawl.rooms.EventRoom;
/*      */ import com.megacrit.cardcrawl.rooms.MonsterRoom;
/*      */ import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
/*      */ import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
/*      */ import com.megacrit.cardcrawl.rooms.RestRoom;
/*      */ import com.megacrit.cardcrawl.rooms.ShopRoom;
/*      */ import com.megacrit.cardcrawl.rooms.TreasureRoom;
/*      */ import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
/*      */ import com.megacrit.cardcrawl.rooms.VictoryRoom;
/*      */ import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
/*      */ import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
/*      */ import com.megacrit.cardcrawl.scenes.AbstractScene;
/*      */ import com.megacrit.cardcrawl.screens.CardRewardScreen;
/*      */ import com.megacrit.cardcrawl.screens.CombatRewardScreen;
/*      */ import com.megacrit.cardcrawl.screens.DeathScreen;
/*      */ import com.megacrit.cardcrawl.screens.DiscardPileViewScreen;
/*      */ import com.megacrit.cardcrawl.screens.DrawPileViewScreen;
/*      */ import com.megacrit.cardcrawl.screens.DungeonMapScreen;
/*      */ import com.megacrit.cardcrawl.screens.ExhaustPileViewScreen;
/*      */ import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
/*      */ import com.megacrit.cardcrawl.screens.VictoryScreen;
/*      */ import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
/*      */ import com.megacrit.cardcrawl.screens.options.SettingsScreen;
/*      */ import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
/*      */ import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
/*      */ import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
/*      */ import com.megacrit.cardcrawl.screens.stats.StatsScreen;
/*      */ import com.megacrit.cardcrawl.shop.ShopScreen;
/*      */ import com.megacrit.cardcrawl.stances.AbstractStance;
/*      */ import com.megacrit.cardcrawl.stances.NeutralStance;
/*      */ import com.megacrit.cardcrawl.ui.FtueTip;
/*      */ import com.megacrit.cardcrawl.ui.buttons.DynamicBanner;
/*      */ import com.megacrit.cardcrawl.ui.buttons.PeekButton;
/*      */ import com.megacrit.cardcrawl.ui.panels.TopPanel;
/*      */ import com.megacrit.cardcrawl.unlock.AbstractUnlock;
/*      */ import com.megacrit.cardcrawl.unlock.UnlockCharacterScreen;
/*      */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*      */ import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
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
/*      */ public abstract class AbstractDungeon
/*      */ {
/*  178 */   protected static final Logger logger = LogManager.getLogger(AbstractDungeon.class.getName());
/*      */   
/*  180 */   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AbstractDungeon");
/*  181 */   public static final String[] TEXT = uiStrings.TEXT;
/*      */   public static String name;
/*      */   public static String levelNum;
/*      */   public static String id;
/*  185 */   public static int floorNum = 0;
/*  186 */   public static int actNum = 0;
/*      */   public static AbstractPlayer player;
/*  188 */   public static ArrayList<AbstractUnlock> unlocks = new ArrayList<>();
/*  189 */   protected static float shrineChance = 0.25F;
/*      */   
/*      */   protected static float cardUpgradedChance;
/*      */   
/*      */   public static AbstractCard transformedCard;
/*      */   
/*      */   public static boolean loading_post_combat = false;
/*      */   
/*      */   public static boolean is_victory = false;
/*      */   
/*      */   public static Texture eventBackgroundImg;
/*      */   public static Random monsterRng;
/*      */   public static Random mapRng;
/*      */   public static Random eventRng;
/*      */   public static Random merchantRng;
/*      */   public static Random cardRng;
/*      */   public static Random treasureRng;
/*      */   public static Random relicRng;
/*      */   public static Random potionRng;
/*      */   public static Random monsterHpRng;
/*      */   public static Random aiRng;
/*      */   public static Random shuffleRng;
/*      */   public static Random cardRandomRng;
/*      */   public static Random miscRng;
/*  213 */   public static CardGroup srcColorlessCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  214 */   public static CardGroup srcCurseCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  215 */   public static CardGroup srcCommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  216 */   public static CardGroup srcUncommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  217 */   public static CardGroup srcRareCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  218 */   public static CardGroup colorlessCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  219 */   public static CardGroup curseCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  220 */   public static CardGroup commonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  221 */   public static CardGroup uncommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*  222 */   public static CardGroup rareCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*      */ 
/*      */   
/*  225 */   public static ArrayList<String> commonRelicPool = new ArrayList<>();
/*  226 */   public static ArrayList<String> uncommonRelicPool = new ArrayList<>();
/*  227 */   public static ArrayList<String> rareRelicPool = new ArrayList<>();
/*  228 */   public static ArrayList<String> shopRelicPool = new ArrayList<>();
/*  229 */   public static ArrayList<String> bossRelicPool = new ArrayList<>();
/*      */ 
/*      */   
/*  232 */   public static String lastCombatMetricKey = null;
/*  233 */   public static ArrayList<String> monsterList = new ArrayList<>();
/*  234 */   public static ArrayList<String> eliteMonsterList = new ArrayList<>();
/*  235 */   public static ArrayList<String> bossList = new ArrayList<>();
/*      */   
/*      */   public static String bossKey;
/*      */   
/*  239 */   public static ArrayList<String> eventList = new ArrayList<>();
/*  240 */   public static ArrayList<String> shrineList = new ArrayList<>();
/*  241 */   public static ArrayList<String> specialOneTimeEventList = new ArrayList<>();
/*  242 */   public static GameActionManager actionManager = new GameActionManager();
/*  243 */   public static ArrayList<AbstractGameEffect> topLevelEffects = new ArrayList<>();
/*  244 */   public static ArrayList<AbstractGameEffect> topLevelEffectsQueue = new ArrayList<>();
/*  245 */   public static ArrayList<AbstractGameEffect> effectList = new ArrayList<>();
/*  246 */   public static ArrayList<AbstractGameEffect> effectsQueue = new ArrayList<>(); public static boolean turnPhaseEffectActive = false; public static float colorlessRareChance; protected static float shopRoomChance; protected static float restRoomChance; protected static float eventRoomChance; protected static float eliteRoomChance; protected static float treasureRoomChance; protected static int smallChestChance; protected static int mediumChestChance; protected static int largeChestChance; protected static int commonRelicChance;
/*      */   protected static int uncommonRelicChance;
/*      */   protected static int rareRelicChance;
/*      */   public static AbstractScene scene;
/*      */   public static MapRoomNode currMapNode;
/*      */   public static ArrayList<ArrayList<MapRoomNode>> map;
/*      */   public static boolean leftRoomAvailable;
/*      */   public static boolean centerRoomAvailable;
/*      */   public static boolean rightRoomAvailable;
/*      */   public static boolean firstRoomChosen = false;
/*      */   public static final int MAP_HEIGHT = 15;
/*      */   public static final int MAP_WIDTH = 7;
/*      */   public static final int MAP_DENSITY = 6;
/*      */   public static final int FINAL_ACT_MAP_HEIGHT = 3;
/*  260 */   public static RenderScene rs = RenderScene.NORMAL;
/*  261 */   public static ArrayList<Integer> pathX = new ArrayList<>();
/*  262 */   public static ArrayList<Integer> pathY = new ArrayList<>();
/*      */ 
/*      */   
/*  265 */   public static Color topGradientColor = new Color(1.0F, 1.0F, 1.0F, 0.25F);
/*  266 */   public static Color botGradientColor = new Color(1.0F, 1.0F, 1.0F, 0.25F);
/*  267 */   public static float floorY = 340.0F * Settings.yScale;
/*  268 */   public static TopPanel topPanel = new TopPanel();
/*  269 */   public static CardRewardScreen cardRewardScreen = new CardRewardScreen();
/*  270 */   public static CombatRewardScreen combatRewardScreen = new CombatRewardScreen();
/*  271 */   public static BossRelicSelectScreen bossRelicScreen = new BossRelicSelectScreen();
/*  272 */   public static MasterDeckViewScreen deckViewScreen = new MasterDeckViewScreen();
/*  273 */   public static DiscardPileViewScreen discardPileViewScreen = new DiscardPileViewScreen();
/*  274 */   public static DrawPileViewScreen gameDeckViewScreen = new DrawPileViewScreen();
/*  275 */   public static ExhaustPileViewScreen exhaustPileViewScreen = new ExhaustPileViewScreen();
/*  276 */   public static SettingsScreen settingsScreen = new SettingsScreen();
/*  277 */   public static InputSettingsScreen inputSettingsScreen = new InputSettingsScreen();
/*  278 */   public static DungeonMapScreen dungeonMapScreen = new DungeonMapScreen();
/*  279 */   public static GridCardSelectScreen gridSelectScreen = new GridCardSelectScreen();
/*  280 */   public static HandCardSelectScreen handCardSelectScreen = new HandCardSelectScreen();
/*  281 */   public static ShopScreen shopScreen = new ShopScreen();
/*  282 */   public static CreditsScreen creditsScreen = null;
/*  283 */   public static FtueTip ftue = null;
/*      */   public static DeathScreen deathScreen;
/*      */   public static VictoryScreen victoryScreen;
/*  286 */   public static UnlockCharacterScreen unlockScreen = new UnlockCharacterScreen();
/*  287 */   public static NeowUnlockScreen gUnlockScreen = new NeowUnlockScreen();
/*      */   
/*      */   public static boolean isScreenUp = false;
/*      */   
/*      */   public static OverlayMenu overlayMenu;
/*      */   
/*      */   public static CurrentScreen screen;
/*      */   public static CurrentScreen previousScreen;
/*      */   public static DynamicBanner dynamicBanner;
/*      */   public static boolean screenSwap = false;
/*      */   public static boolean isDungeonBeaten;
/*  298 */   public static int cardBlizzStartOffset = 5;
/*  299 */   public static int cardBlizzRandomizer = cardBlizzStartOffset;
/*  300 */   public static int cardBlizzGrowth = 1;
/*  301 */   public static int cardBlizzMaxOffset = -40;
/*      */   public static boolean isFadingIn;
/*      */   public static boolean isFadingOut;
/*      */   public static boolean waitingOnFadeOut;
/*      */   protected static float fadeTimer;
/*      */   public static Color fadeColor;
/*      */   public static Color sourceFadeColor;
/*      */   public static MapRoomNode nextRoom;
/*  309 */   public static float sceneOffsetY = 0.0F;
/*  310 */   public static ArrayList<String> relicsToRemoveOnStart = new ArrayList<>();
/*  311 */   public static int bossCount = 0;
/*      */   
/*      */   public static final float SCENE_OFFSET_TIME = 1.3F;
/*      */   
/*      */   public static boolean isAscensionMode = false;
/*  316 */   public static int ascensionLevel = 0;
/*      */ 
/*      */   
/*  319 */   public static ArrayList<AbstractBlight> blightPool = new ArrayList<>();
/*      */   
/*      */   public static boolean ascensionCheck;
/*      */   
/*  323 */   private static final Logger LOGGER = LogManager.getLogger(AbstractDungeon.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractDungeon(String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {
/*  331 */     ascensionCheck = UnlockTracker.isAscensionUnlocked(p);
/*  332 */     CardCrawlGame.dungeon = this;
/*  333 */     long startTime = System.currentTimeMillis();
/*  334 */     AbstractDungeon.name = name;
/*  335 */     id = levelId;
/*  336 */     player = p;
/*  337 */     topPanel.setPlayerName();
/*  338 */     actionManager = new GameActionManager();
/*  339 */     overlayMenu = new OverlayMenu(p);
/*  340 */     dynamicBanner = new DynamicBanner();
/*  341 */     unlocks.clear();
/*      */     
/*  343 */     specialOneTimeEventList = newSpecialOneTimeEventList;
/*      */ 
/*      */     
/*  346 */     isFadingIn = false;
/*  347 */     isFadingOut = false;
/*  348 */     waitingOnFadeOut = false;
/*  349 */     fadeTimer = 1.0F;
/*  350 */     isDungeonBeaten = false;
/*  351 */     isScreenUp = false;
/*      */     
/*  353 */     dungeonTransitionSetup();
/*  354 */     generateMonsters();
/*  355 */     initializeBoss();
/*  356 */     setBoss(bossList.get(0));
/*  357 */     initializeEventList();
/*  358 */     initializeEventImg();
/*  359 */     initializeShrineList();
/*  360 */     initializeCardPools();
/*  361 */     if (floorNum == 0) {
/*  362 */       p.initializeStarterDeck();
/*      */     }
/*  364 */     initializePotions();
/*  365 */     BlightHelper.initialize();
/*      */     
/*  367 */     if (id.equals("Exordium")) {
/*  368 */       screen = CurrentScreen.NONE;
/*  369 */       isScreenUp = false;
/*      */     } else {
/*  371 */       screen = CurrentScreen.MAP;
/*  372 */       isScreenUp = true;
/*      */     } 
/*      */     
/*  375 */     logger.info("Content generation time: " + (System.currentTimeMillis() - startTime) + "ms");
/*      */   }
/*      */ 
/*      */   
/*      */   public AbstractDungeon(String name, AbstractPlayer p, SaveFile saveFile) {
/*  380 */     ascensionCheck = UnlockTracker.isAscensionUnlocked(p);
/*  381 */     id = saveFile.level_name;
/*  382 */     CardCrawlGame.dungeon = this;
/*  383 */     long startTime = System.currentTimeMillis();
/*  384 */     AbstractDungeon.name = name;
/*  385 */     player = p;
/*  386 */     topPanel.setPlayerName();
/*  387 */     actionManager = new GameActionManager();
/*  388 */     overlayMenu = new OverlayMenu(p);
/*  389 */     dynamicBanner = new DynamicBanner();
/*  390 */     isFadingIn = false;
/*  391 */     isFadingOut = false;
/*  392 */     waitingOnFadeOut = false;
/*  393 */     fadeTimer = 1.0F;
/*  394 */     isDungeonBeaten = false;
/*  395 */     isScreenUp = false;
/*  396 */     firstRoomChosen = true;
/*  397 */     unlocks.clear();
/*      */     
/*      */     try {
/*  400 */       loadSave(saveFile);
/*  401 */     } catch (Exception e) {
/*  402 */       logger.info("Exception occurred while loading save!");
/*  403 */       logger.info("Deleting save due to crash!");
/*  404 */       SaveAndContinue.deleteSave(player);
/*  405 */       ExceptionHandler.handleException(e, LOGGER);
/*  406 */       Gdx.app.exit();
/*      */     } 
/*      */     
/*  409 */     initializeEventImg();
/*  410 */     initializeShrineList();
/*  411 */     initializeCardPools();
/*  412 */     initializePotions();
/*  413 */     BlightHelper.initialize();
/*  414 */     screen = CurrentScreen.NONE;
/*  415 */     isScreenUp = false;
/*      */     
/*  417 */     logger.info("Dungeon load time: " + (System.currentTimeMillis() - startTime) + "ms");
/*      */   }
/*      */   
/*      */   private void setBoss(String key) {
/*  421 */     bossKey = key;
/*      */ 
/*      */     
/*  424 */     if (DungeonMap.boss != null && DungeonMap.bossOutline != null) {
/*  425 */       DungeonMap.boss.dispose();
/*  426 */       DungeonMap.bossOutline.dispose();
/*      */     } 
/*      */     
/*  429 */     if (key.equals("The Guardian")) {
/*  430 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/guardian.png");
/*  431 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/guardian.png");
/*  432 */     } else if (key.equals("Hexaghost")) {
/*  433 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/hexaghost.png");
/*  434 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/hexaghost.png");
/*  435 */     } else if (key.equals("Slime Boss")) {
/*  436 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/slime.png");
/*  437 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/slime.png");
/*  438 */     } else if (key.equals("Collector")) {
/*  439 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/collector.png");
/*  440 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/collector.png");
/*  441 */     } else if (key.equals("Automaton")) {
/*  442 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/automaton.png");
/*  443 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/automaton.png");
/*  444 */     } else if (key.equals("Champ")) {
/*  445 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/champ.png");
/*  446 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/champ.png");
/*  447 */     } else if (key.equals("Awakened One")) {
/*  448 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/awakened.png");
/*  449 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/awakened.png");
/*  450 */     } else if (key.equals("Time Eater")) {
/*  451 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/timeeater.png");
/*  452 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/timeeater.png");
/*  453 */     } else if (key.equals("Donu and Deca")) {
/*  454 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/donu.png");
/*  455 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/donu.png");
/*  456 */     } else if (key.equals("The Heart")) {
/*  457 */       DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/heart.png");
/*  458 */       DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/heart.png");
/*      */     } else {
/*  460 */       logger.info("WARNING: UNKNOWN BOSS ICON: " + key);
/*  461 */       DungeonMap.boss = null;
/*      */     } 
/*      */     
/*  464 */     logger.info("[BOSS] " + key);
/*      */   }
/*      */   
/*      */   protected abstract void initializeLevelSpecificChances();
/*      */   
/*      */   public static boolean isPlayerInDungeon() {
/*  470 */     return (CardCrawlGame.dungeon != null);
/*      */   }
/*      */   
/*      */   public static void generateSeeds() {
/*  474 */     logger.info("Generating seeds: " + Settings.seed);
/*  475 */     monsterRng = new Random(Settings.seed);
/*  476 */     eventRng = new Random(Settings.seed);
/*  477 */     merchantRng = new Random(Settings.seed);
/*  478 */     cardRng = new Random(Settings.seed);
/*  479 */     treasureRng = new Random(Settings.seed);
/*  480 */     relicRng = new Random(Settings.seed);
/*  481 */     monsterHpRng = new Random(Settings.seed);
/*  482 */     potionRng = new Random(Settings.seed);
/*  483 */     aiRng = new Random(Settings.seed);
/*  484 */     shuffleRng = new Random(Settings.seed);
/*  485 */     cardRandomRng = new Random(Settings.seed);
/*  486 */     miscRng = new Random(Settings.seed);
/*      */   }
/*      */   
/*      */   public static void loadSeeds(SaveFile save) {
/*  490 */     if (save.is_daily || save.is_trial) {
/*  491 */       Settings.isDailyRun = save.is_daily;
/*  492 */       Settings.isTrial = save.is_trial;
/*  493 */       Settings.specialSeed = Long.valueOf(save.special_seed);
/*  494 */       if (save.is_daily) {
/*  495 */         ModHelper.setTodaysMods(save.special_seed, player.chosenClass);
/*      */       } else {
/*  497 */         ModHelper.setTodaysMods(save.seed, player.chosenClass);
/*      */       } 
/*      */     } 
/*      */     
/*  501 */     monsterRng = new Random(Settings.seed, save.monster_seed_count);
/*  502 */     eventRng = new Random(Settings.seed, save.event_seed_count);
/*  503 */     merchantRng = new Random(Settings.seed, save.merchant_seed_count);
/*  504 */     cardRng = new Random(Settings.seed, save.card_seed_count);
/*  505 */     cardBlizzRandomizer = save.card_random_seed_randomizer;
/*  506 */     treasureRng = new Random(Settings.seed, save.treasure_seed_count);
/*  507 */     relicRng = new Random(Settings.seed, save.relic_seed_count);
/*  508 */     potionRng = new Random(Settings.seed, save.potion_seed_count);
/*      */ 
/*      */     
/*  511 */     logger.info("Loading seeds: " + Settings.seed);
/*  512 */     logger.info("Monster seed:  " + monsterRng.counter);
/*  513 */     logger.info("Event seed:    " + eventRng.counter);
/*  514 */     logger.info("Merchant seed: " + merchantRng.counter);
/*  515 */     logger.info("Card seed:     " + cardRng.counter);
/*  516 */     logger.info("Treasure seed: " + treasureRng.counter);
/*  517 */     logger.info("Relic seed:    " + relicRng.counter);
/*  518 */     logger.info("Potion seed:   " + potionRng.counter);
/*      */   }
/*      */   
/*      */   public void populatePathTaken(SaveFile saveFile) {
/*  522 */     MapRoomNode node = null;
/*      */     
/*  524 */     if (saveFile.current_room.equals(MonsterRoomBoss.class.getName())) {
/*  525 */       node = new MapRoomNode(-1, 15);
/*  526 */       node.room = (AbstractRoom)new MonsterRoomBoss();
/*  527 */       nextRoom = node;
/*  528 */     } else if (saveFile.current_room.equals(TreasureRoomBoss.class.getName())) {
/*  529 */       node = new MapRoomNode(-1, 15);
/*  530 */       node.room = (AbstractRoom)new TreasureRoomBoss();
/*  531 */       nextRoom = node;
/*      */     
/*      */     }
/*  534 */     else if (saveFile.room_y == 15 && saveFile.room_x == -1) {
/*  535 */       node = new MapRoomNode(-1, 15);
/*  536 */       node.room = (AbstractRoom)new VictoryRoom(VictoryRoom.EventType.HEART);
/*  537 */       nextRoom = node;
/*      */     
/*      */     }
/*  540 */     else if (saveFile.current_room.equals(NeowRoom.class.getName())) {
/*  541 */       nextRoom = null;
/*      */     } else {
/*  543 */       nextRoom = ((ArrayList<MapRoomNode>)map.get(saveFile.room_y)).get(saveFile.room_x);
/*      */     } 
/*      */ 
/*      */     
/*  547 */     for (int i = 0; i < pathX.size(); i++) {
/*  548 */       if (((Integer)pathY.get(i)).intValue() == 14) {
/*  549 */         MapRoomNode node2 = ((ArrayList<MapRoomNode>)map.get(((Integer)pathY.get(i)).intValue())).get(((Integer)pathX.get(i)).intValue());
/*  550 */         for (MapEdge e : node2.getEdges()) {
/*  551 */           if (e != null) {
/*  552 */             e.markAsTaken();
/*      */           }
/*      */         } 
/*      */       } 
/*  556 */       if (((Integer)pathY.get(i)).intValue() < 15) {
/*  557 */         ((MapRoomNode)((ArrayList)map.get(((Integer)pathY.get(i)).intValue())).get(((Integer)pathX.get(i)).intValue())).taken = true;
/*  558 */         if (node != null) {
/*  559 */           MapEdge connectedEdge = node.getEdgeConnectedTo(((ArrayList<MapRoomNode>)map
/*  560 */               .get(((Integer)pathY.get(i)).intValue())).get(((Integer)pathX.get(i)).intValue()));
/*  561 */           if (connectedEdge != null) {
/*  562 */             connectedEdge.markAsTaken();
/*      */           }
/*      */         } 
/*  565 */         node = ((ArrayList<MapRoomNode>)map.get(((Integer)pathY.get(i)).intValue())).get(((Integer)pathX.get(i)).intValue());
/*      */       } 
/*      */     } 
/*      */     
/*  569 */     if (isLoadingIntoNeow(saveFile)) {
/*      */       
/*  571 */       logger.info("Loading into Neow");
/*  572 */       currMapNode = new MapRoomNode(0, -1);
/*  573 */       currMapNode.room = (AbstractRoom)new EmptyRoom();
/*  574 */       nextRoom = null;
/*      */     } else {
/*      */       
/*  577 */       logger.info("Loading into: " + saveFile.room_x + "," + saveFile.room_y);
/*  578 */       currMapNode = new MapRoomNode(0, -1);
/*  579 */       currMapNode.room = (AbstractRoom)new EmptyRoom();
/*      */     } 
/*      */     
/*  582 */     nextRoomTransition(saveFile);
/*      */ 
/*      */     
/*  585 */     if (isLoadingIntoNeow(saveFile)) {
/*  586 */       if (saveFile.chose_neow_reward) {
/*  587 */         currMapNode.room = (AbstractRoom)new NeowRoom(true);
/*      */       } else {
/*  589 */         currMapNode.room = (AbstractRoom)new NeowRoom(false);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  594 */     if (currMapNode.room instanceof VictoryRoom && (
/*  595 */       !Settings.isFinalActAvailable || !Settings.hasRubyKey || !Settings.hasEmeraldKey || !Settings.hasSapphireKey))
/*      */     {
/*      */       
/*  598 */       CardCrawlGame.stopClock = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isLoadingIntoNeow(SaveFile saveFile) {
/*  604 */     return (floorNum == 0 || saveFile.current_room.equals(NeowRoom.class.getName()));
/*      */   }
/*      */   
/*      */   public static AbstractChest getRandomChest() {
/*  608 */     int roll = treasureRng.random(0, 99);
/*      */     
/*  610 */     if (roll < smallChestChance)
/*  611 */       return (AbstractChest)new SmallChest(); 
/*  612 */     if (roll < mediumChestChance + smallChestChance) {
/*  613 */       return (AbstractChest)new MediumChest();
/*      */     }
/*  615 */     return (AbstractChest)new LargeChest();
/*      */   }
/*      */ 
/*      */   
/*      */   protected static void generateMap() {
/*  620 */     long startTime = System.currentTimeMillis();
/*      */     
/*  622 */     int mapHeight = 15;
/*  623 */     int mapWidth = 7;
/*  624 */     int mapPathDensity = 6;
/*      */     
/*  626 */     ArrayList<AbstractRoom> roomList = new ArrayList<>();
/*  627 */     map = MapGenerator.generateDungeon(mapHeight, mapWidth, mapPathDensity, mapRng);
/*      */ 
/*      */     
/*  630 */     int count = 0;
/*  631 */     for (ArrayList<MapRoomNode> a : map) {
/*  632 */       for (MapRoomNode n : a) {
/*  633 */         if (!n.hasEdges() || 
/*  634 */           n.y == map.size() - 2) {
/*      */           continue;
/*      */         }
/*  637 */         count++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  643 */     generateRoomTypes(roomList, count);
/*      */     
/*  645 */     RoomTypeAssigner.assignRowAsRoomType(map.get(map.size() - 1), RestRoom.class);
/*  646 */     RoomTypeAssigner.assignRowAsRoomType(map.get(0), MonsterRoom.class);
/*  647 */     if (Settings.isEndless && player.hasBlight("MimicInfestation")) {
/*  648 */       RoomTypeAssigner.assignRowAsRoomType(map.get(8), MonsterRoomElite.class);
/*      */     } else {
/*  650 */       RoomTypeAssigner.assignRowAsRoomType(map.get(8), TreasureRoom.class);
/*      */     } 
/*  652 */     map = RoomTypeAssigner.distributeRoomsAcrossMap(mapRng, map, roomList);
/*      */     
/*  654 */     logger.info("Generated the following dungeon map:");
/*  655 */     logger.info(MapGenerator.toString(map, Boolean.valueOf(true)));
/*  656 */     logger.info("Game Seed: " + Settings.seed);
/*  657 */     logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
/*  658 */     firstRoomChosen = false;
/*      */     
/*  660 */     fadeIn();
/*  661 */     setEmeraldElite();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void setEmeraldElite() {
/*  668 */     if (Settings.isFinalActAvailable && !Settings.hasEmeraldKey) {
/*  669 */       ArrayList<MapRoomNode> eliteNodes = new ArrayList<>();
/*      */       
/*  671 */       for (int i = 0; i < map.size(); i++) {
/*  672 */         for (int j = 0; j < ((ArrayList)map.get(i)).size(); j++) {
/*  673 */           if (((MapRoomNode)((ArrayList)map.get(i)).get(j)).room instanceof MonsterRoomElite) {
/*  674 */             eliteNodes.add(((ArrayList<MapRoomNode>)map.get(i)).get(j));
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  679 */       MapRoomNode chosenNode = eliteNodes.get(mapRng.random(0, eliteNodes.size() - 1));
/*  680 */       chosenNode.hasEmeraldKey = true;
/*      */       
/*  682 */       logger.info("[INFO] Elite nodes identified: " + eliteNodes.size());
/*  683 */       logger.info("[INFO] Emerald Key  placed in: [" + chosenNode.x + "," + chosenNode.y + "]");
/*      */     } 
/*      */   }
/*      */   private static void generateRoomTypes(ArrayList<AbstractRoom> roomList, int availableRoomCount) {
/*      */     int eliteCount;
/*  688 */     logger.info("Generating Room Types! There are " + availableRoomCount + " rooms:");
/*  689 */     int shopCount = Math.round(availableRoomCount * shopRoomChance);
/*  690 */     logger.info(" SHOP (" + toPercentage(shopRoomChance) + "): " + shopCount);
/*  691 */     int restCount = Math.round(availableRoomCount * restRoomChance);
/*  692 */     logger.info(" REST (" + toPercentage(restRoomChance) + "): " + restCount);
/*  693 */     int treasureCount = Math.round(availableRoomCount * treasureRoomChance);
/*  694 */     logger.info(" TRSRE (" + toPercentage(treasureRoomChance) + "): " + treasureCount);
/*      */ 
/*      */ 
/*      */     
/*  698 */     if (ModHelper.isModEnabled("Elite Swarm")) {
/*  699 */       eliteCount = Math.round(availableRoomCount * eliteRoomChance * 2.5F);
/*  700 */       logger.info(" ELITE (" + toPercentage(eliteRoomChance) + "): " + eliteCount);
/*  701 */     } else if (ascensionLevel >= 1) {
/*  702 */       eliteCount = Math.round(availableRoomCount * eliteRoomChance * 1.6F);
/*  703 */       logger.info(" ELITE (" + toPercentage(eliteRoomChance) + "): " + eliteCount);
/*      */     } else {
/*      */       
/*  706 */       eliteCount = Math.round(availableRoomCount * eliteRoomChance);
/*  707 */       logger.info(" ELITE (" + toPercentage(eliteRoomChance) + "): " + eliteCount);
/*      */     } 
/*      */ 
/*      */     
/*  711 */     int eventCount = Math.round(availableRoomCount * eventRoomChance);
/*  712 */     logger.info(" EVNT (" + toPercentage(eventRoomChance) + "): " + eventCount);
/*  713 */     int monsterCount = availableRoomCount - shopCount - restCount - treasureCount - eliteCount - eventCount;
/*  714 */     logger.info(" MSTR (" + 
/*  715 */         toPercentage(1.0F - shopRoomChance - restRoomChance - treasureRoomChance - eliteRoomChance - eventRoomChance) + "): " + monsterCount);
/*      */     
/*      */     int i;
/*      */     
/*  719 */     for (i = 0; i < shopCount; i++) {
/*  720 */       roomList.add(new ShopRoom());
/*      */     }
/*  722 */     for (i = 0; i < restCount; i++) {
/*  723 */       roomList.add(new RestRoom());
/*      */     }
/*  725 */     for (i = 0; i < eliteCount; i++) {
/*  726 */       roomList.add(new MonsterRoomElite());
/*      */     }
/*  728 */     for (i = 0; i < eventCount; i++) {
/*  729 */       roomList.add(new EventRoom());
/*      */     }
/*      */   }
/*      */   
/*      */   private static String toPercentage(float n) {
/*  734 */     return String.format("%.0f", new Object[] { Float.valueOf(n * 100.0F) }) + "%";
/*      */   }
/*      */ 
/*      */   
/*      */   private static void firstRoomLogic() {
/*  739 */     initializeFirstRoom();
/*      */ 
/*      */ 
/*      */     
/*  743 */     leftRoomAvailable = currMapNode.leftNodeAvailable();
/*  744 */     centerRoomAvailable = currMapNode.centerNodeAvailable();
/*  745 */     rightRoomAvailable = currMapNode.rightNodeAvailable();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean passesDonutCheck(ArrayList<ArrayList<MapRoomNode>> map) {
/*  756 */     logger.info("CASEY'S DONUT CHECK: ");
/*  757 */     int width = ((ArrayList)map.get(0)).size();
/*  758 */     int height = map.size();
/*  759 */     logger.info(" HEIGHT: " + height);
/*  760 */     logger.info(" WIDTH:  " + width);
/*      */ 
/*      */     
/*  763 */     int nodeCount = 0;
/*  764 */     boolean[] roomHasNode = new boolean[width];
/*  765 */     for (int i = 0; i < width; i++) {
/*  766 */       roomHasNode[i] = false;
/*      */     }
/*  768 */     ArrayList<MapRoomNode> secondToLastRow = map.get(map.size() - 2);
/*  769 */     for (MapRoomNode n : secondToLastRow) {
/*  770 */       for (MapEdge e : n.getEdges()) {
/*  771 */         roomHasNode[e.dstX] = true;
/*      */       }
/*      */     } 
/*  774 */     for (int j = 0; j < width - 1; j++) {
/*  775 */       if (roomHasNode[j]) {
/*  776 */         nodeCount++;
/*      */       }
/*      */     } 
/*  779 */     if (nodeCount == 1) {
/*  780 */       logger.info(" [SUCCESS] " + nodeCount + " NODE IN LAST ROW");
/*      */     } else {
/*  782 */       logger.info(" [FAIL] " + nodeCount + " NODES IN LAST ROW");
/*  783 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  787 */     int roomCount = 0;
/*  788 */     for (ArrayList<MapRoomNode> rows : map) {
/*  789 */       for (MapRoomNode n : rows) {
/*  790 */         if (n.room != null) {
/*  791 */           roomCount++;
/*      */         }
/*      */       } 
/*      */     } 
/*  795 */     logger.info(" ROOM COUNT: " + roomCount);
/*      */ 
/*      */ 
/*      */     
/*  799 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractRoom getCurrRoom() {
/*  809 */     return currMapNode.getRoom();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MapRoomNode getCurrMapNode() {
/*  818 */     return currMapNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setCurrMapNode(MapRoomNode currMapNode) {
/*  827 */     SoulGroup group = AbstractDungeon.currMapNode.room.souls;
/*      */ 
/*      */     
/*  830 */     if (AbstractDungeon.currMapNode != null && getCurrRoom() != null) {
/*  831 */       getCurrRoom().dispose();
/*      */     }
/*      */     
/*  834 */     AbstractDungeon.currMapNode = currMapNode;
/*      */     
/*  836 */     if (AbstractDungeon.currMapNode.room == null) {
/*  837 */       logger.warn("This player loaded into a room that no longer exists (due to a new map gen?)");
/*  838 */       for (int i = 0; i < 5; i++) {
/*  839 */         if (((MapRoomNode)((ArrayList)map.get(currMapNode.y)).get(i)).room != null) {
/*  840 */           AbstractDungeon.currMapNode = ((ArrayList<MapRoomNode>)map.get(currMapNode.y)).get(i);
/*  841 */           AbstractDungeon.currMapNode.room = ((MapRoomNode)((ArrayList)map.get(currMapNode.y)).get(i)).room;
/*  842 */           nextRoom.room = ((MapRoomNode)((ArrayList)map.get(currMapNode.y)).get(i)).room;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } else {
/*  847 */       AbstractDungeon.currMapNode.room.souls = group;
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
/*      */   public ArrayList<ArrayList<MapRoomNode>> getMap() {
/*  888 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractRelic returnRandomRelic(AbstractRelic.RelicTier tier) {
/*  898 */     logger.info("Returning " + tier.name() + " relic");
/*  899 */     return RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy();
/*      */   }
/*      */   
/*      */   public static AbstractRelic returnRandomScreenlessRelic(AbstractRelic.RelicTier tier) {
/*  903 */     logger.info("Returning " + tier.name() + " relic");
/*  904 */     AbstractRelic tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy();
/*      */     
/*  906 */     while (Objects.equals(tmpRelic.relicId, "Bottled Flame") || Objects.equals(tmpRelic.relicId, "Bottled Lightning") || 
/*      */       
/*  908 */       Objects.equals(tmpRelic.relicId, "Bottled Tornado") || Objects.equals(tmpRelic.relicId, "Whetstone"))
/*      */     {
/*      */ 
/*      */       
/*  912 */       tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy();
/*      */     }
/*      */     
/*  915 */     return tmpRelic;
/*      */   }
/*      */   
/*      */   public static AbstractRelic returnRandomNonCampfireRelic(AbstractRelic.RelicTier tier) {
/*  919 */     logger.info("Returning " + tier.name() + " relic");
/*  920 */     AbstractRelic tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy();
/*  921 */     while (Objects.equals(tmpRelic.relicId, "Peace Pipe") || Objects.equals(tmpRelic.relicId, "Shovel") || 
/*  922 */       Objects.equals(tmpRelic.relicId, "Girya"))
/*      */     {
/*  924 */       tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy();
/*      */     }
/*  926 */     return tmpRelic;
/*      */   }
/*      */   
/*      */   public static AbstractRelic returnRandomRelicEnd(AbstractRelic.RelicTier tier) {
/*  930 */     logger.info("Returning " + tier.name() + " relic");
/*  931 */     return RelicLibrary.getRelic(returnEndRandomRelicKey(tier)).makeCopy();
/*      */   }
/*      */   
/*      */   public static String returnEndRandomRelicKey(AbstractRelic.RelicTier tier) {
/*  935 */     String retVal = null;
/*      */     
/*  937 */     switch (tier) {
/*      */       case NORMAL:
/*  939 */         if (commonRelicPool.isEmpty()) {
/*  940 */           retVal = returnRandomRelicKey(AbstractRelic.RelicTier.UNCOMMON); break;
/*      */         } 
/*  942 */         retVal = commonRelicPool.remove(commonRelicPool.size() - 1);
/*      */         break;
/*      */       
/*      */       case CAMPFIRE:
/*  946 */         if (uncommonRelicPool.isEmpty()) {
/*  947 */           retVal = returnRandomRelicKey(AbstractRelic.RelicTier.RARE); break;
/*      */         } 
/*  949 */         retVal = uncommonRelicPool.remove(uncommonRelicPool.size() - 1);
/*      */         break;
/*      */       
/*      */       case EVENT:
/*  953 */         if (rareRelicPool.isEmpty()) {
/*  954 */           retVal = "Circlet"; break;
/*      */         } 
/*  956 */         retVal = rareRelicPool.remove(rareRelicPool.size() - 1);
/*      */         break;
/*      */       
/*      */       case null:
/*  960 */         if (shopRelicPool.isEmpty()) {
/*  961 */           retVal = returnRandomRelicKey(AbstractRelic.RelicTier.UNCOMMON); break;
/*      */         } 
/*  963 */         retVal = shopRelicPool.remove(shopRelicPool.size() - 1);
/*      */         break;
/*      */       
/*      */       case null:
/*  967 */         if (bossRelicPool.isEmpty()) {
/*  968 */           retVal = "Red Circlet"; break;
/*      */         } 
/*  970 */         retVal = bossRelicPool.remove(0);
/*      */         break;
/*      */       
/*      */       default:
/*  974 */         logger.info("Incorrect relic tier: " + tier.name() + " was called in returnEndRandomRelicKey()");
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  979 */     if (!RelicLibrary.getRelic(retVal).canSpawn()) {
/*  980 */       return returnEndRandomRelicKey(tier);
/*      */     }
/*      */     
/*  983 */     return retVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String returnRandomRelicKey(AbstractRelic.RelicTier tier) {
/*  993 */     String retVal = null;
/*      */     
/*  995 */     switch (tier) {
/*      */       case NORMAL:
/*  997 */         if (commonRelicPool.isEmpty()) {
/*  998 */           retVal = returnRandomRelicKey(AbstractRelic.RelicTier.UNCOMMON); break;
/*      */         } 
/* 1000 */         retVal = commonRelicPool.remove(0);
/*      */         break;
/*      */       
/*      */       case CAMPFIRE:
/* 1004 */         if (uncommonRelicPool.isEmpty()) {
/* 1005 */           retVal = returnRandomRelicKey(AbstractRelic.RelicTier.RARE); break;
/*      */         } 
/* 1007 */         retVal = uncommonRelicPool.remove(0);
/*      */         break;
/*      */       
/*      */       case EVENT:
/* 1011 */         if (rareRelicPool.isEmpty()) {
/* 1012 */           retVal = "Circlet"; break;
/*      */         } 
/* 1014 */         retVal = rareRelicPool.remove(0);
/*      */         break;
/*      */       
/*      */       case null:
/* 1018 */         if (shopRelicPool.isEmpty()) {
/* 1019 */           retVal = returnRandomRelicKey(AbstractRelic.RelicTier.UNCOMMON); break;
/*      */         } 
/* 1021 */         retVal = shopRelicPool.remove(0);
/*      */         break;
/*      */       
/*      */       case null:
/* 1025 */         if (bossRelicPool.isEmpty()) {
/* 1026 */           retVal = "Red Circlet"; break;
/*      */         } 
/* 1028 */         retVal = bossRelicPool.remove(0);
/*      */         break;
/*      */       
/*      */       default:
/* 1032 */         logger.info("Incorrect relic tier: " + tier.name() + " was called in returnRandomRelicKey()");
/*      */         break;
/*      */     } 
/*      */     
/* 1036 */     if (!RelicLibrary.getRelic(retVal).canSpawn()) {
/* 1037 */       return returnEndRandomRelicKey(tier);
/*      */     }
/*      */     
/* 1040 */     return retVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractRelic.RelicTier returnRandomRelicTier() {
/* 1047 */     int roll = relicRng.random(0, 99);
/* 1048 */     if (roll < commonRelicChance)
/* 1049 */       return AbstractRelic.RelicTier.COMMON; 
/* 1050 */     if (roll < commonRelicChance + uncommonRelicChance) {
/* 1051 */       return AbstractRelic.RelicTier.UNCOMMON;
/*      */     }
/* 1053 */     return AbstractRelic.RelicTier.RARE;
/*      */   }
/*      */   
/*      */   public static AbstractPotion returnTotallyRandomPotion() {
/* 1057 */     return PotionHelper.getRandomPotion();
/*      */   }
/*      */   
/*      */   public static AbstractPotion returnRandomPotion() {
/* 1061 */     return returnRandomPotion(false);
/*      */   }
/*      */   
/*      */   public static AbstractPotion returnRandomPotion(boolean limited) {
/* 1065 */     int roll = potionRng.random(0, 99);
/* 1066 */     if (roll < PotionHelper.POTION_COMMON_CHANCE)
/* 1067 */       return returnRandomPotion(AbstractPotion.PotionRarity.COMMON, limited); 
/* 1068 */     if (roll < PotionHelper.POTION_UNCOMMON_CHANCE + PotionHelper.POTION_COMMON_CHANCE) {
/* 1069 */       return returnRandomPotion(AbstractPotion.PotionRarity.UNCOMMON, limited);
/*      */     }
/* 1071 */     return returnRandomPotion(AbstractPotion.PotionRarity.RARE, limited);
/*      */   }
/*      */   
/*      */   public static AbstractPotion returnRandomPotion(AbstractPotion.PotionRarity rarity, boolean limited) {
/* 1075 */     AbstractPotion temp = PotionHelper.getRandomPotion();
/* 1076 */     boolean spamCheck = limited;
/* 1077 */     while (temp.rarity != rarity || spamCheck) {
/* 1078 */       spamCheck = limited;
/* 1079 */       temp = PotionHelper.getRandomPotion();
/* 1080 */       if (temp.ID != "Fruit Juice") {
/* 1081 */         spamCheck = false;
/*      */       }
/*      */     } 
/* 1084 */     return temp;
/*      */   }
/*      */   
/*      */   public static void transformCard(AbstractCard c) {
/* 1088 */     transformCard(c, false);
/*      */   }
/*      */   
/*      */   public static void transformCard(AbstractCard c, boolean autoUpgrade) {
/* 1092 */     transformCard(c, autoUpgrade, new Random());
/*      */   }
/*      */   
/*      */   public static void transformCard(AbstractCard c, boolean autoUpgrade, Random rng) {
/* 1096 */     switch (c.color) {
/*      */       case NORMAL:
/* 1098 */         transformedCard = returnTrulyRandomColorlessCardFromAvailable(c, rng).makeCopy();
/*      */         break;
/*      */       case CAMPFIRE:
/* 1101 */         transformedCard = CardLibrary.getCurse(c, rng).makeCopy();
/*      */         break;
/*      */       default:
/* 1104 */         transformedCard = returnTrulyRandomCardFromAvailable(c, rng).makeCopy();
/*      */         break;
/*      */     } 
/*      */     
/* 1108 */     UnlockTracker.markCardAsSeen(transformedCard.cardID);
/*      */     
/* 1110 */     if (autoUpgrade && transformedCard.canUpgrade()) {
/* 1111 */       transformedCard.upgrade();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void srcTransformCard(AbstractCard c) {
/* 1116 */     logger.info("Transform using SRC pool...");
/* 1117 */     switch (c.rarity) {
/*      */       case NORMAL:
/* 1119 */         transformedCard = srcCommonCardPool.getRandomCard(false).makeCopy();
/*      */         return;
/*      */       case CAMPFIRE:
/* 1122 */         srcCommonCardPool.removeCard(c.cardID);
/* 1123 */         transformedCard = srcCommonCardPool.getRandomCard(false).makeCopy();
/* 1124 */         srcCommonCardPool.addToTop(c.makeCopy());
/*      */         return;
/*      */       case EVENT:
/* 1127 */         srcUncommonCardPool.removeCard(c.cardID);
/* 1128 */         transformedCard = srcUncommonCardPool.getRandomCard(false).makeCopy();
/* 1129 */         srcUncommonCardPool.addToTop(c.makeCopy());
/*      */         return;
/*      */       case null:
/* 1132 */         srcRareCardPool.removeCard(c.cardID);
/* 1133 */         if (srcRareCardPool.isEmpty()) {
/* 1134 */           transformedCard = srcUncommonCardPool.getRandomCard(false).makeCopy();
/*      */         } else {
/* 1136 */           transformedCard = srcRareCardPool.getRandomCard(false).makeCopy();
/*      */         } 
/* 1138 */         srcRareCardPool.addToTop(c.makeCopy());
/*      */         return;
/*      */       case null:
/* 1141 */         if (!srcRareCardPool.isEmpty()) {
/* 1142 */           transformedCard = srcRareCardPool.getRandomCard(false).makeCopy(); break;
/*      */         } 
/* 1144 */         transformedCard = srcUncommonCardPool.getRandomCard(false).makeCopy();
/*      */         break;
/*      */     } 
/* 1147 */     logger.info("Transform called on a strange card type: " + c.type.name());
/* 1148 */     transformedCard = srcCommonCardPool.getRandomCard(false).makeCopy();
/*      */   }
/*      */ 
/*      */   
/*      */   public static CardGroup getEachRare() {
/* 1153 */     CardGroup everyRareCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/* 1154 */     for (AbstractCard c : rareCardPool.group) {
/* 1155 */       everyRareCard.addToBottom(c.makeCopy());
/*      */     }
/* 1157 */     return everyRareCard;
/*      */   }
/*      */   
/*      */   public static AbstractCard returnRandomCard() {
/* 1161 */     ArrayList<AbstractCard> list = new ArrayList<>();
/* 1162 */     AbstractCard.CardRarity rarity = rollRarity();
/* 1163 */     if (rarity.equals(AbstractCard.CardRarity.COMMON)) {
/* 1164 */       list.addAll(srcCommonCardPool.group);
/* 1165 */     } else if (rarity.equals(AbstractCard.CardRarity.UNCOMMON)) {
/* 1166 */       list.addAll(srcUncommonCardPool.group);
/*      */     } else {
/* 1168 */       list.addAll(srcRareCardPool.group);
/*      */     } 
/*      */     
/* 1171 */     return list.get(cardRandomRng.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomCard() {
/* 1175 */     ArrayList<AbstractCard> list = new ArrayList<>();
/* 1176 */     list.addAll(srcCommonCardPool.group);
/* 1177 */     list.addAll(srcUncommonCardPool.group);
/* 1178 */     list.addAll(srcRareCardPool.group);
/* 1179 */     return list.get(cardRandomRng.random(list.size() - 1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractCard returnTrulyRandomCardInCombat() {
/* 1188 */     ArrayList<AbstractCard> list = new ArrayList<>();
/* 1189 */     for (AbstractCard c : srcCommonCardPool.group) {
/* 1190 */       if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
/* 1191 */         list.add(c);
/* 1192 */         UnlockTracker.markCardAsSeen(c.cardID);
/*      */       } 
/*      */     } 
/* 1195 */     for (AbstractCard c : srcUncommonCardPool.group) {
/* 1196 */       if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
/* 1197 */         list.add(c);
/* 1198 */         UnlockTracker.markCardAsSeen(c.cardID);
/*      */       } 
/*      */     } 
/* 1201 */     for (AbstractCard c : srcRareCardPool.group) {
/* 1202 */       if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
/* 1203 */         list.add(c);
/* 1204 */         UnlockTracker.markCardAsSeen(c.cardID);
/*      */       } 
/*      */     } 
/* 1207 */     return list.get(cardRandomRng.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomCardInCombat(AbstractCard.CardType type) {
/* 1211 */     ArrayList<AbstractCard> list = new ArrayList<>();
/* 1212 */     for (AbstractCard c : srcCommonCardPool.group) {
/* 1213 */       if (c.type == type && !c.hasTag(AbstractCard.CardTags.HEALING)) {
/* 1214 */         list.add(c);
/*      */       }
/*      */     } 
/* 1217 */     for (AbstractCard c : srcUncommonCardPool.group) {
/* 1218 */       if (c.type == type && !c.hasTag(AbstractCard.CardTags.HEALING)) {
/* 1219 */         list.add(c);
/*      */       }
/*      */     } 
/* 1222 */     for (AbstractCard c : srcRareCardPool.group) {
/* 1223 */       if (c.type == type && !c.hasTag(AbstractCard.CardTags.HEALING)) {
/* 1224 */         list.add(c);
/*      */       }
/*      */     } 
/* 1227 */     return list.get(cardRandomRng.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomColorlessCardInCombat() {
/* 1231 */     return returnTrulyRandomColorlessCardInCombat(cardRandomRng);
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomColorlessCardInCombat(String prohibitedID) {
/* 1235 */     return returnTrulyRandomColorlessCardFromAvailable(prohibitedID, cardRandomRng);
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomColorlessCardInCombat(Random rng) {
/* 1239 */     ArrayList<AbstractCard> list = new ArrayList<>();
/* 1240 */     for (AbstractCard c : srcColorlessCardPool.group) {
/* 1241 */       if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
/* 1242 */         list.add(c);
/*      */       }
/*      */     } 
/* 1245 */     return list.get(rng.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomColorlessCardFromAvailable(String prohibited, Random rng) {
/* 1249 */     ArrayList<AbstractCard> list = new ArrayList<>();
/* 1250 */     for (AbstractCard c : srcColorlessCardPool.group) {
/* 1251 */       if (c.cardID != prohibited) {
/* 1252 */         list.add(c);
/*      */       }
/*      */     } 
/* 1255 */     return list.get(rng.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomColorlessCardFromAvailable(AbstractCard prohibited, Random rng) {
/* 1259 */     ArrayList<AbstractCard> list = new ArrayList<>();
/* 1260 */     for (AbstractCard c : srcColorlessCardPool.group) {
/* 1261 */       if (!Objects.equals(c.cardID, prohibited.cardID)) {
/* 1262 */         list.add(c);
/*      */       }
/*      */     } 
/* 1265 */     return list.get(rng.random(list.size() - 1));
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomCardFromAvailable(AbstractCard prohibited, Random rng) {
/* 1269 */     ArrayList<AbstractCard> list = new ArrayList<>();
/*      */     
/* 1271 */     switch (prohibited.color)
/*      */     { case NORMAL:
/* 1273 */         for (AbstractCard c : colorlessCardPool.group) {
/* 1274 */           if (!Objects.equals(c.cardID, prohibited.cardID)) {
/* 1275 */             list.add(c);
/*      */           }
/*      */         } 
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
/* 1300 */         return ((AbstractCard)list.get(rng.random(list.size() - 1))).makeCopy();case CAMPFIRE: return CardLibrary.getCurse(); }  for (AbstractCard c : commonCardPool.group) { if (!Objects.equals(c.cardID, prohibited.cardID)) list.add(c);  }  for (AbstractCard c : srcUncommonCardPool.group) { if (!Objects.equals(c.cardID, prohibited.cardID)) list.add(c);  }  for (AbstractCard c : srcRareCardPool.group) { if (!Objects.equals(c.cardID, prohibited.cardID)) list.add(c);  }  return ((AbstractCard)list.get(rng.random(list.size() - 1))).makeCopy();
/*      */   }
/*      */   
/*      */   public static AbstractCard returnTrulyRandomCardFromAvailable(AbstractCard prohibited) {
/* 1304 */     return returnTrulyRandomCardFromAvailable(prohibited, new Random());
/*      */   }
/*      */   
/*      */   public static AbstractCard getTransformedCard() {
/* 1308 */     AbstractCard retVal = transformedCard;
/* 1309 */     transformedCard = null;
/* 1310 */     return retVal;
/*      */   }
/*      */   
/*      */   public void populateFirstStrongEnemy(ArrayList<MonsterInfo> monsters, ArrayList<String> exclusions) {
/*      */     while (true) {
/* 1315 */       String m = MonsterInfo.roll(monsters, monsterRng.random());
/* 1316 */       if (!exclusions.contains(m)) {
/* 1317 */         monsterList.add(m);
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void populateMonsterList(ArrayList<MonsterInfo> monsters, int numMonsters, boolean elites) {
/* 1325 */     if (elites) {
/* 1326 */       for (int i = 0; i < numMonsters; i++) {
/* 1327 */         if (eliteMonsterList.isEmpty()) {
/* 1328 */           eliteMonsterList.add(MonsterInfo.roll(monsters, monsterRng.random()));
/*      */         } else {
/* 1330 */           String toAdd = MonsterInfo.roll(monsters, monsterRng.random());
/* 1331 */           if (!toAdd.equals(eliteMonsterList.get(eliteMonsterList.size() - 1))) {
/* 1332 */             eliteMonsterList.add(toAdd);
/*      */           } else {
/*      */             
/* 1335 */             i--;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1340 */       for (int i = 0; i < numMonsters; i++) {
/* 1341 */         if (monsterList.isEmpty()) {
/* 1342 */           monsterList.add(MonsterInfo.roll(monsters, monsterRng.random()));
/*      */         } else {
/* 1344 */           String toAdd = MonsterInfo.roll(monsters, monsterRng.random());
/* 1345 */           if (!toAdd.equals(monsterList.get(monsterList.size() - 1))) {
/* 1346 */             if (monsterList.size() > 1 && toAdd.equals(monsterList.get(monsterList.size() - 2))) {
/*      */               
/* 1348 */               i--;
/*      */             } else {
/* 1350 */               monsterList.add(toAdd);
/*      */             } 
/*      */           } else {
/*      */             
/* 1354 */             i--;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract ArrayList<String> generateExclusions();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractCard returnColorlessCard(AbstractCard.CardRarity rarity) {
/* 1370 */     Collections.shuffle(colorlessCardPool.group, new Random(shuffleRng.randomLong()));
/*      */ 
/*      */     
/* 1373 */     for (AbstractCard c : colorlessCardPool.group) {
/* 1374 */       if (c.rarity == rarity) {
/* 1375 */         return c.makeCopy();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1380 */     if (rarity == AbstractCard.CardRarity.RARE) {
/* 1381 */       for (AbstractCard c : colorlessCardPool.group) {
/* 1382 */         if (c.rarity == AbstractCard.CardRarity.UNCOMMON) {
/* 1383 */           return c.makeCopy();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1389 */     return (AbstractCard)new SwiftStrike();
/*      */   }
/*      */ 
/*      */   
/*      */   public static AbstractCard returnColorlessCard() {
/* 1394 */     Collections.shuffle(colorlessCardPool.group);
/*      */ 
/*      */     
/* 1397 */     Iterator<AbstractCard> iterator = colorlessCardPool.group.iterator(); if (iterator.hasNext()) { AbstractCard c = iterator.next();
/* 1398 */       return c.makeCopy(); }
/*      */ 
/*      */ 
/*      */     
/* 1402 */     return (AbstractCard)new SwiftStrike();
/*      */   }
/*      */   
/*      */   public static AbstractCard returnRandomCurse() {
/* 1406 */     AbstractCard c = CardLibrary.getCurse().makeCopy();
/* 1407 */     UnlockTracker.markCardAsSeen(c.cardID);
/* 1408 */     return c;
/*      */   }
/*      */   
/*      */   public void initializePotions() {
/* 1412 */     PotionHelper.initialize(player.chosenClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initializeCardPools() {
/* 1419 */     logger.info("INIT CARD POOL");
/* 1420 */     long startTime = System.currentTimeMillis();
/*      */ 
/*      */     
/* 1423 */     commonCardPool.clear();
/* 1424 */     uncommonCardPool.clear();
/* 1425 */     rareCardPool.clear();
/* 1426 */     colorlessCardPool.clear();
/* 1427 */     curseCardPool.clear();
/*      */     
/* 1429 */     ArrayList<AbstractCard> tmpPool = new ArrayList<>();
/* 1430 */     if (ModHelper.isModEnabled("Colorless Cards")) {
/* 1431 */       CardLibrary.addColorlessCards(tmpPool);
/*      */     }
/* 1433 */     if (ModHelper.isModEnabled("Diverse")) {
/* 1434 */       CardLibrary.addRedCards(tmpPool);
/* 1435 */       CardLibrary.addGreenCards(tmpPool);
/* 1436 */       CardLibrary.addBlueCards(tmpPool);
/* 1437 */       if (!UnlockTracker.isCharacterLocked("Watcher")) {
/* 1438 */         CardLibrary.addPurpleCards(tmpPool);
/*      */       }
/*      */     } else {
/* 1441 */       player.getCardPool(tmpPool);
/*      */     } 
/*      */ 
/*      */     
/* 1445 */     addColorlessCards();
/* 1446 */     addCurseCards();
/*      */ 
/*      */     
/* 1449 */     for (AbstractCard c : tmpPool) {
/* 1450 */       switch (c.rarity) {
/*      */         case CAMPFIRE:
/* 1452 */           commonCardPool.addToTop(c);
/*      */           continue;
/*      */         case EVENT:
/* 1455 */           uncommonCardPool.addToTop(c);
/*      */           continue;
/*      */         case null:
/* 1458 */           rareCardPool.addToTop(c);
/*      */           continue;
/*      */         case null:
/* 1461 */           curseCardPool.addToTop(c);
/*      */           continue;
/*      */       } 
/* 1464 */       logger.info("Unspecified rarity: " + c.rarity
/* 1465 */           .name() + " when creating pools! AbstractDungeon: Line 827");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1471 */     srcColorlessCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/* 1472 */     srcCurseCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/* 1473 */     srcRareCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/* 1474 */     srcUncommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/* 1475 */     srcCommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
/*      */     
/* 1477 */     for (AbstractCard c : colorlessCardPool.group) {
/* 1478 */       srcColorlessCardPool.addToBottom(c);
/*      */     }
/* 1480 */     for (AbstractCard c : curseCardPool.group) {
/* 1481 */       srcCurseCardPool.addToBottom(c);
/*      */     }
/* 1483 */     for (AbstractCard c : rareCardPool.group) {
/* 1484 */       srcRareCardPool.addToBottom(c);
/*      */     }
/* 1486 */     for (AbstractCard c : uncommonCardPool.group) {
/* 1487 */       srcUncommonCardPool.addToBottom(c);
/*      */     }
/* 1489 */     for (AbstractCard c : commonCardPool.group) {
/* 1490 */       srcCommonCardPool.addToBottom(c);
/*      */     }
/*      */     
/* 1493 */     logger.info("Cardpool load time: " + (System.currentTimeMillis() - startTime) + "ms");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addColorlessCards() {
/* 1501 */     for (Map.Entry<String, AbstractCard> c : (Iterable<Map.Entry<String, AbstractCard>>)CardLibrary.cards.entrySet()) {
/* 1502 */       AbstractCard card = c.getValue();
/* 1503 */       if (card.color == AbstractCard.CardColor.COLORLESS && card.rarity != AbstractCard.CardRarity.BASIC && card.rarity != AbstractCard.CardRarity.SPECIAL && card.type != AbstractCard.CardType.STATUS)
/*      */       {
/* 1505 */         colorlessCardPool.addToTop(card);
/*      */       }
/*      */     } 
/* 1508 */     logger.info("COLORLESS CARDS: " + colorlessCardPool.size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCurseCards() {
/* 1516 */     for (Map.Entry<String, AbstractCard> c : (Iterable<Map.Entry<String, AbstractCard>>)CardLibrary.cards.entrySet()) {
/* 1517 */       AbstractCard card = c.getValue();
/* 1518 */       if (card.type == AbstractCard.CardType.CURSE && !Objects.equals(card.cardID, "Necronomicurse") && !Objects.equals(card.cardID, "AscendersBane") && 
/*      */         
/* 1520 */         !Objects.equals(card.cardID, "CurseOfTheBell") && !Objects.equals(card.cardID, "Pride"))
/*      */       {
/*      */         
/* 1523 */         curseCardPool.addToTop(card);
/*      */       }
/*      */     } 
/* 1526 */     logger.info("CURSE CARDS: " + curseCardPool.size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initializeRelicList() {
/* 1533 */     commonRelicPool.clear();
/* 1534 */     uncommonRelicPool.clear();
/* 1535 */     rareRelicPool.clear();
/* 1536 */     shopRelicPool.clear();
/* 1537 */     bossRelicPool.clear();
/*      */     
/* 1539 */     RelicLibrary.populateRelicPool(commonRelicPool, AbstractRelic.RelicTier.COMMON, player.chosenClass);
/* 1540 */     RelicLibrary.populateRelicPool(uncommonRelicPool, AbstractRelic.RelicTier.UNCOMMON, player.chosenClass);
/* 1541 */     RelicLibrary.populateRelicPool(rareRelicPool, AbstractRelic.RelicTier.RARE, player.chosenClass);
/* 1542 */     RelicLibrary.populateRelicPool(shopRelicPool, AbstractRelic.RelicTier.SHOP, player.chosenClass);
/* 1543 */     RelicLibrary.populateRelicPool(bossRelicPool, AbstractRelic.RelicTier.BOSS, player.chosenClass);
/*      */     
/* 1545 */     if (floorNum >= 1) {
/* 1546 */       for (AbstractRelic r : player.relics) {
/* 1547 */         relicsToRemoveOnStart.add(r.relicId);
/*      */       }
/*      */     }
/*      */     
/* 1551 */     Collections.shuffle(commonRelicPool, new Random(relicRng.randomLong()));
/* 1552 */     Collections.shuffle(uncommonRelicPool, new Random(relicRng.randomLong()));
/* 1553 */     Collections.shuffle(rareRelicPool, new Random(relicRng.randomLong()));
/* 1554 */     Collections.shuffle(shopRelicPool, new Random(relicRng.randomLong()));
/* 1555 */     Collections.shuffle(bossRelicPool, new Random(relicRng.randomLong()));
/*      */ 
/*      */     
/* 1558 */     if (ModHelper.isModEnabled("Flight") || ModHelper.isModEnabled("Uncertain Future")) {
/* 1559 */       relicsToRemoveOnStart.add("WingedGreaves");
/*      */     }
/*      */     
/* 1562 */     if (ModHelper.isModEnabled("Diverse")) {
/* 1563 */       relicsToRemoveOnStart.add("PrismaticShard");
/*      */     }
/*      */     
/* 1566 */     if (ModHelper.isModEnabled("DeadlyEvents")) {
/* 1567 */       relicsToRemoveOnStart.add("Juzu Bracelet");
/*      */     }
/*      */     
/* 1570 */     if (ModHelper.isModEnabled("Hoarder")) {
/* 1571 */       relicsToRemoveOnStart.add("Smiling Mask");
/*      */     }
/*      */     
/* 1574 */     if (ModHelper.isModEnabled("Draft") || ModHelper.isModEnabled("SealedDeck") || ModHelper.isModEnabled("Shiny") || 
/* 1575 */       ModHelper.isModEnabled("Insanity"))
/*      */     {
/* 1577 */       relicsToRemoveOnStart.add("Pandora's Box");
/*      */     }
/*      */ 
/*      */     
/* 1581 */     for (String remove : relicsToRemoveOnStart) {
/* 1582 */       Iterator<String> s; for (s = commonRelicPool.iterator(); s.hasNext(); ) {
/* 1583 */         String derp = s.next();
/* 1584 */         if (derp.equals(remove)) {
/* 1585 */           s.remove();
/* 1586 */           logger.info(derp + " removed.");
/*      */           break;
/*      */         } 
/*      */       } 
/* 1590 */       for (s = uncommonRelicPool.iterator(); s.hasNext(); ) {
/* 1591 */         String derp = s.next();
/* 1592 */         if (derp.equals(remove)) {
/* 1593 */           s.remove();
/* 1594 */           logger.info(derp + " removed.");
/*      */           break;
/*      */         } 
/*      */       } 
/* 1598 */       for (s = rareRelicPool.iterator(); s.hasNext(); ) {
/* 1599 */         String derp = s.next();
/* 1600 */         if (derp.equals(remove)) {
/* 1601 */           s.remove();
/* 1602 */           logger.info(derp + " removed.");
/*      */           break;
/*      */         } 
/*      */       } 
/* 1606 */       for (s = bossRelicPool.iterator(); s.hasNext(); ) {
/* 1607 */         String derp = s.next();
/* 1608 */         if (derp.equals(remove)) {
/* 1609 */           s.remove();
/* 1610 */           logger.info(derp + " removed.");
/*      */           break;
/*      */         } 
/*      */       } 
/* 1614 */       for (s = shopRelicPool.iterator(); s.hasNext(); ) {
/* 1615 */         String derp = s.next();
/* 1616 */         if (derp.equals(remove)) {
/* 1617 */           s.remove();
/* 1618 */           logger.info(derp + " removed.");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1624 */     if (Settings.isDebug) {
/* 1625 */       logger.info("Relic (Common):");
/* 1626 */       for (String s : commonRelicPool) {
/* 1627 */         logger.info(" " + s);
/*      */       }
/*      */       
/* 1630 */       logger.info("Relic (Uncommon):");
/* 1631 */       for (String s : uncommonRelicPool) {
/* 1632 */         logger.info(" " + s);
/*      */       }
/*      */       
/* 1635 */       logger.info("Relic (Rare):");
/* 1636 */       for (String s : rareRelicPool) {
/* 1637 */         logger.info(" " + s);
/*      */       }
/*      */       
/* 1640 */       logger.info("Relic (Shop):");
/* 1641 */       for (String s : shopRelicPool) {
/* 1642 */         logger.info(" " + s);
/*      */       }
/*      */       
/* 1645 */       logger.info("Relic (Boss):");
/* 1646 */       for (String s : bossRelicPool) {
/* 1647 */         logger.info(" " + s);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void generateMonsters();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void generateWeakEnemies(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void generateStrongEnemies(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void generateElites(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void initializeBoss();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void initializeEventList();
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void initializeEventImg();
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void initializeShrineList();
/*      */ 
/*      */ 
/*      */   
/*      */   public void initializeSpecialOneTimeEventList() {
/* 1693 */     specialOneTimeEventList.clear();
/* 1694 */     specialOneTimeEventList.add("Accursed Blacksmith");
/* 1695 */     specialOneTimeEventList.add("Bonfire Elementals");
/* 1696 */     specialOneTimeEventList.add("Designer");
/* 1697 */     specialOneTimeEventList.add("Duplicator");
/* 1698 */     specialOneTimeEventList.add("FaceTrader");
/* 1699 */     specialOneTimeEventList.add("Fountain of Cleansing");
/* 1700 */     specialOneTimeEventList.add("Knowing Skull");
/* 1701 */     specialOneTimeEventList.add("Lab");
/* 1702 */     specialOneTimeEventList.add("N'loth");
/* 1703 */     if (isNoteForYourselfAvailable()) {
/* 1704 */       specialOneTimeEventList.add("NoteForYourself");
/*      */     }
/* 1706 */     specialOneTimeEventList.add("SecretPortal");
/* 1707 */     specialOneTimeEventList.add("The Joust");
/* 1708 */     specialOneTimeEventList.add("WeMeetAgain");
/* 1709 */     specialOneTimeEventList.add("The Woman in Blue");
/*      */   }
/*      */   
/*      */   private boolean isNoteForYourselfAvailable() {
/* 1713 */     if (Settings.isDailyRun) {
/* 1714 */       logger.info("Note For Yourself is disabled due to Daily Run");
/* 1715 */       return false;
/*      */     } 
/*      */     
/* 1718 */     if (ascensionLevel >= 15) {
/* 1719 */       logger.info("Note For Yourself is disabled beyond Ascension 15+");
/* 1720 */       return false;
/*      */     } 
/*      */     
/* 1723 */     if (ascensionLevel == 0) {
/* 1724 */       logger.info("Note For Yourself is enabled due to No Ascension");
/* 1725 */       return true;
/*      */     } 
/*      */     
/* 1728 */     if (ascensionLevel < player.getPrefs().getInteger("ASCENSION_LEVEL")) {
/* 1729 */       logger.info("Note For Yourself is enabled as it's less than Highest Unlocked Ascension");
/* 1730 */       return true;
/*      */     } 
/*      */     
/* 1733 */     logger.info("Note For Yourself is disabled as requirements aren't met");
/* 1734 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ArrayList<AbstractCard> getColorlessRewardCards() {
/* 1739 */     ArrayList<AbstractCard> retVal = new ArrayList<>();
/*      */     
/* 1741 */     int numCards = 3;
/*      */     
/* 1743 */     for (AbstractRelic r : player.relics) {
/* 1744 */       numCards = r.changeNumberOfCardsInReward(numCards);
/*      */     }
/* 1746 */     if (ModHelper.isModEnabled("Binary")) {
/* 1747 */       numCards--;
/*      */     }
/*      */ 
/*      */     
/* 1751 */     for (int i = 0; i < numCards; i++) {
/* 1752 */       AbstractCard.CardRarity rarity = rollRareOrUncommon(colorlessRareChance);
/* 1753 */       AbstractCard card = null;
/* 1754 */       switch (rarity) {
/*      */         case null:
/* 1756 */           card = getColorlessCardFromPool(rarity);
/* 1757 */           cardBlizzRandomizer = cardBlizzStartOffset;
/*      */           break;
/*      */         case EVENT:
/* 1760 */           card = getColorlessCardFromPool(rarity);
/*      */           break;
/*      */         default:
/* 1763 */           logger.info("WTF?");
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 1768 */       while (retVal.contains(card)) {
/* 1769 */         if (card != null) {
/* 1770 */           logger.info("DUPE: " + card.originalName);
/*      */         }
/* 1772 */         card = getColorlessCardFromPool(rarity);
/*      */       } 
/*      */       
/* 1775 */       if (card != null) {
/* 1776 */         retVal.add(card);
/*      */       }
/*      */     } 
/*      */     
/* 1780 */     ArrayList<AbstractCard> retVal2 = new ArrayList<>();
/* 1781 */     for (AbstractCard c : retVal) {
/* 1782 */       retVal2.add(c.makeCopy());
/*      */     }
/*      */     
/* 1785 */     return retVal2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ArrayList<AbstractCard> getRewardCards() {
/* 1792 */     ArrayList<AbstractCard> retVal = new ArrayList<>();
/*      */     
/* 1794 */     int numCards = 3;
/*      */     
/* 1796 */     for (AbstractRelic r : player.relics) {
/* 1797 */       numCards = r.changeNumberOfCardsInReward(numCards);
/*      */     }
/*      */     
/* 1800 */     if (ModHelper.isModEnabled("Binary")) {
/* 1801 */       numCards--;
/*      */     }
/*      */ 
/*      */     
/* 1805 */     for (int i = 0; i < numCards; i++) {
/* 1806 */       AbstractCard.CardRarity rarity = rollRarity();
/* 1807 */       AbstractCard card = null;
/*      */       
/* 1809 */       switch (rarity) {
/*      */         case null:
/* 1811 */           cardBlizzRandomizer = cardBlizzStartOffset;
/*      */           break;
/*      */         case EVENT:
/*      */           break;
/*      */         case CAMPFIRE:
/* 1816 */           cardBlizzRandomizer -= cardBlizzGrowth;
/* 1817 */           if (cardBlizzRandomizer <= cardBlizzMaxOffset) {
/* 1818 */             cardBlizzRandomizer = cardBlizzMaxOffset;
/*      */           }
/*      */           break;
/*      */         default:
/* 1822 */           logger.info("WTF?");
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 1827 */       boolean containsDupe = true;
/*      */       
/* 1829 */       while (containsDupe) {
/* 1830 */         containsDupe = false;
/* 1831 */         if (player.hasRelic("PrismaticShard")) {
/* 1832 */           card = CardLibrary.getAnyColorCard(rarity);
/*      */         } else {
/* 1834 */           card = getCard(rarity);
/*      */         } 
/*      */         
/* 1837 */         for (AbstractCard c : retVal) {
/* 1838 */           if (c.cardID.equals(card.cardID)) {
/* 1839 */             containsDupe = true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1846 */       if (card != null) {
/* 1847 */         retVal.add(card);
/*      */       }
/*      */     } 
/*      */     
/* 1851 */     ArrayList<AbstractCard> retVal2 = new ArrayList<>();
/* 1852 */     for (AbstractCard c : retVal) {
/* 1853 */       retVal2.add(c.makeCopy());
/*      */     }
/*      */     
/* 1856 */     for (AbstractCard c : retVal2) {
/* 1857 */       if (c.rarity != AbstractCard.CardRarity.RARE && cardRng.randomBoolean(cardUpgradedChance) && c.canUpgrade()) {
/* 1858 */         c.upgrade(); continue;
/*      */       } 
/* 1860 */       for (AbstractRelic r : player.relics) {
/* 1861 */         r.onPreviewObtainCard(c);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1866 */     return retVal2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractCard getCard(AbstractCard.CardRarity rarity) {
/* 1876 */     switch (rarity) {
/*      */       case null:
/* 1878 */         return rareCardPool.getRandomCard(true);
/*      */       case EVENT:
/* 1880 */         return uncommonCardPool.getRandomCard(true);
/*      */       case CAMPFIRE:
/* 1882 */         return commonCardPool.getRandomCard(true);
/*      */       case null:
/* 1884 */         return curseCardPool.getRandomCard(true);
/*      */     } 
/* 1886 */     logger.info("No rarity on getCard in Abstract Dungeon");
/* 1887 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static AbstractCard getCard(AbstractCard.CardRarity rarity, Random rng) {
/* 1892 */     switch (rarity) {
/*      */       case null:
/* 1894 */         return rareCardPool.getRandomCard(rng);
/*      */       case EVENT:
/* 1896 */         return uncommonCardPool.getRandomCard(rng);
/*      */       case CAMPFIRE:
/* 1898 */         return commonCardPool.getRandomCard(rng);
/*      */       case null:
/* 1900 */         return curseCardPool.getRandomCard(rng);
/*      */     } 
/* 1902 */     logger.info("No rarity on getCard in Abstract Dungeon");
/* 1903 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractCard getCardWithoutRng(AbstractCard.CardRarity rarity) {
/* 1914 */     switch (rarity) {
/*      */       case null:
/* 1916 */         return rareCardPool.getRandomCard(false);
/*      */       case EVENT:
/* 1918 */         return uncommonCardPool.getRandomCard(false);
/*      */       case CAMPFIRE:
/* 1920 */         return commonCardPool.getRandomCard(false);
/*      */       case null:
/* 1922 */         return returnRandomCurse();
/*      */     } 
/* 1924 */     logger.info("Check getCardWithoutRng");
/* 1925 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractCard getCardFromPool(AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean useRng) {
/*      */     AbstractCard retVal;
/* 1937 */     switch (rarity) {
/*      */       case null:
/* 1939 */         retVal = rareCardPool.getRandomCard(type, useRng);
/* 1940 */         if (retVal != null) {
/* 1941 */           return retVal;
/*      */         }
/* 1943 */         logger.info("ERROR: Could not find Rare card of type: " + type.name());
/*      */       case EVENT:
/* 1945 */         retVal = uncommonCardPool.getRandomCard(type, useRng);
/* 1946 */         if (retVal != null) {
/* 1947 */           return retVal;
/*      */         }
/*      */ 
/*      */         
/* 1951 */         if (type == AbstractCard.CardType.POWER) {
/* 1952 */           return getCardFromPool(AbstractCard.CardRarity.RARE, type, useRng);
/*      */         }
/*      */         
/* 1955 */         logger.info("ERROR: Could not find Uncommon card of type: " + type.name());
/*      */       case CAMPFIRE:
/* 1957 */         retVal = commonCardPool.getRandomCard(type, useRng);
/* 1958 */         if (retVal != null) {
/* 1959 */           return retVal;
/*      */         }
/*      */ 
/*      */         
/* 1963 */         if (type == AbstractCard.CardType.POWER) {
/* 1964 */           return getCardFromPool(AbstractCard.CardRarity.UNCOMMON, type, useRng);
/*      */         }
/*      */         
/* 1967 */         logger.info("ERROR: Could not find Common card of type: " + type.name());
/*      */       case null:
/* 1969 */         retVal = curseCardPool.getRandomCard(type, useRng);
/* 1970 */         if (retVal != null) {
/* 1971 */           return retVal;
/*      */         }
/*      */         
/* 1974 */         logger.info("ERROR: Could not find Curse card of type: " + type.name()); break;
/*      */     } 
/* 1976 */     logger.info("ERROR: Default in getCardFromPool");
/* 1977 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static AbstractCard getColorlessCardFromPool(AbstractCard.CardRarity rarity) {
/*      */     AbstractCard retVal;
/* 1983 */     switch (rarity) {
/*      */       case null:
/* 1985 */         retVal = colorlessCardPool.getRandomCard(true, rarity);
/* 1986 */         if (retVal != null) {
/* 1987 */           return retVal;
/*      */         }
/*      */       case EVENT:
/* 1990 */         retVal = colorlessCardPool.getRandomCard(true, rarity);
/* 1991 */         if (retVal != null)
/* 1992 */           return retVal; 
/*      */         break;
/*      */     } 
/* 1995 */     logger.info("ERROR: getColorlessCardFromPool");
/* 1996 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static AbstractCard.CardRarity rollRarity(Random rng) {
/* 2001 */     int roll = cardRng.random(99);
/* 2002 */     roll += cardBlizzRandomizer;
/*      */     
/* 2004 */     if (currMapNode == null) {
/* 2005 */       return getCardRarityFallback(roll);
/*      */     }
/* 2007 */     return getCurrRoom().getCardRarity(roll);
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
/*      */   private static AbstractCard.CardRarity getCardRarityFallback(int roll) {
/* 2019 */     int rareRate = 3;
/* 2020 */     if (roll < rareRate)
/* 2021 */       return AbstractCard.CardRarity.RARE; 
/* 2022 */     if (roll < 40) {
/* 2023 */       return AbstractCard.CardRarity.UNCOMMON;
/*      */     }
/* 2025 */     return AbstractCard.CardRarity.COMMON;
/*      */   }
/*      */   
/*      */   public static AbstractCard.CardRarity rollRarity() {
/* 2029 */     return rollRarity(cardRng);
/*      */   }
/*      */   
/*      */   public static AbstractCard.CardRarity rollRareOrUncommon(float rareChance) {
/* 2033 */     if (cardRng.randomBoolean(rareChance)) {
/* 2034 */       return AbstractCard.CardRarity.RARE;
/*      */     }
/* 2036 */     return AbstractCard.CardRarity.UNCOMMON;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractMonster getRandomMonster() {
/* 2046 */     return currMapNode.room.monsters.getRandomMonster(null, true, cardRandomRng);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractMonster getRandomMonster(AbstractMonster except) {
/* 2056 */     return currMapNode.room.monsters.getRandomMonster(except, true, cardRandomRng);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void nextRoomTransitionStart() {
/* 2063 */     fadeOut();
/* 2064 */     waitingOnFadeOut = true;
/* 2065 */     overlayMenu.proceedButton.hide();
/* 2066 */     if (ModHelper.isModEnabled("Terminal")) {
/* 2067 */       player.decreaseMaxHealth(1);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void initializeFirstRoom() {
/* 2072 */     fadeIn();
/*      */ 
/*      */     
/* 2075 */     floorNum++;
/* 2076 */     if (currMapNode.room instanceof MonsterRoom) {
/* 2077 */       if (!CardCrawlGame.loadingSave) {
/* 2078 */         if (SaveHelper.shouldSave()) {
/* 2079 */           SaveHelper.saveIfAppropriate(SaveFile.SaveType.ENTER_ROOM);
/*      */         } else {
/*      */           
/* 2082 */           Metrics metrics = new Metrics();
/* 2083 */           metrics.setValues(false, false, null, Metrics.MetricRequestType.NONE);
/* 2084 */           metrics.gatherAllDataAndSave(false, false, null);
/*      */         } 
/*      */       }
/* 2087 */       floorNum--;
/*      */     } 
/*      */ 
/*      */     
/* 2091 */     scene.nextRoom(currMapNode.room);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void resetPlayer() {
/* 2096 */     player.orbs.clear();
/* 2097 */     player.animX = 0.0F;
/* 2098 */     player.animY = 0.0F;
/* 2099 */     player.hideHealthBar();
/* 2100 */     player.hand.clear();
/* 2101 */     player.powers.clear();
/* 2102 */     player.drawPile.clear();
/* 2103 */     player.discardPile.clear();
/* 2104 */     player.exhaustPile.clear();
/* 2105 */     player.limbo.clear();
/* 2106 */     player.loseBlock(true);
/* 2107 */     player.damagedThisCombat = 0;
/*      */ 
/*      */     
/* 2110 */     if (!player.stance.ID.equals("Neutral")) {
/* 2111 */       player.stance = (AbstractStance)new NeutralStance();
/* 2112 */       player.onStanceChange("Neutral");
/*      */     } 
/*      */     
/* 2115 */     GameActionManager.turn = 1;
/*      */   }
/*      */   
/*      */   public void nextRoomTransition() {
/* 2119 */     nextRoomTransition(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void nextRoomTransition(SaveFile saveFile) {
/* 2126 */     overlayMenu.proceedButton.setLabel(TEXT[0]);
/* 2127 */     combatRewardScreen.clear();
/*      */     
/* 2129 */     if (nextRoom != null && nextRoom.room != null) {
/* 2130 */       nextRoom.room.rewards.clear();
/*      */     }
/*      */     
/* 2133 */     if (getCurrRoom() instanceof MonsterRoomElite) {
/* 2134 */       if (!eliteMonsterList.isEmpty()) {
/* 2135 */         logger.info("Removing elite: " + (String)eliteMonsterList.get(0) + " from monster list.");
/* 2136 */         eliteMonsterList.remove(0);
/*      */       } else {
/* 2138 */         generateElites(10);
/*      */       } 
/* 2140 */     } else if (getCurrRoom() instanceof MonsterRoom) {
/* 2141 */       if (!monsterList.isEmpty()) {
/* 2142 */         logger.info("Removing monster: " + (String)monsterList.get(0) + " from monster list.");
/* 2143 */         monsterList.remove(0);
/*      */       } else {
/* 2145 */         generateStrongEnemies(12);
/*      */       } 
/* 2147 */     } else if (getCurrRoom() instanceof EventRoom && 
/* 2148 */       (getCurrRoom()).event instanceof NoteForYourself) {
/* 2149 */       AbstractCard tmpCard = ((NoteForYourself)(getCurrRoom()).event).saveCard;
/* 2150 */       if (tmpCard != null) {
/* 2151 */         CardCrawlGame.playerPref.putString("NOTE_CARD", tmpCard.cardID);
/* 2152 */         CardCrawlGame.playerPref.putInteger("NOTE_UPGRADE", tmpCard.timesUpgraded);
/* 2153 */         CardCrawlGame.playerPref.flush();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2159 */     if (RestRoom.lastFireSoundId != 0L) {
/* 2160 */       CardCrawlGame.sound.fadeOut("REST_FIRE_WET", RestRoom.lastFireSoundId);
/*      */     }
/* 2162 */     if (!player.stance.ID.equals("Neutral") && player.stance != null)
/*      */     {
/* 2164 */       player.stance.stopIdleSfx();
/*      */     }
/*      */     
/* 2167 */     gridSelectScreen.upgradePreviewCard = null;
/* 2168 */     previousScreen = null;
/* 2169 */     dynamicBanner.hide();
/* 2170 */     dungeonMapScreen.closeInstantly();
/* 2171 */     closeCurrentScreen();
/* 2172 */     topPanel.unhoverHitboxes();
/* 2173 */     fadeIn();
/* 2174 */     player.resetControllerValues();
/* 2175 */     effectList.clear();
/*      */ 
/*      */     
/* 2178 */     for (Iterator<AbstractGameEffect> i = topLevelEffects.iterator(); i.hasNext(); ) {
/* 2179 */       AbstractGameEffect e = i.next();
/*      */       
/* 2181 */       if (!(e instanceof com.megacrit.cardcrawl.vfx.ObtainKeyEffect)) {
/* 2182 */         i.remove();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2187 */     topLevelEffectsQueue.clear();
/* 2188 */     effectsQueue.clear();
/* 2189 */     dungeonMapScreen.dismissable = true;
/* 2190 */     dungeonMapScreen.map.legend.isLegendHighlighted = false;
/*      */     
/* 2192 */     resetPlayer();
/*      */     
/* 2194 */     if (!CardCrawlGame.loadingSave) {
/* 2195 */       incrementFloorBasedMetrics();
/* 2196 */       floorNum++;
/* 2197 */       if (!((Boolean)TipTracker.tips.get("INTENT_TIP")).booleanValue() && floorNum == 6) {
/* 2198 */         TipTracker.neverShowAgain("INTENT_TIP");
/*      */       }
/*      */       
/* 2201 */       StatsScreen.incrementFloorClimbed();
/* 2202 */       SaveHelper.saveIfAppropriate(SaveFile.SaveType.ENTER_ROOM);
/*      */     } 
/*      */     
/* 2205 */     monsterHpRng = new Random(Long.valueOf(Settings.seed.longValue() + floorNum));
/* 2206 */     aiRng = new Random(Long.valueOf(Settings.seed.longValue() + floorNum));
/* 2207 */     shuffleRng = new Random(Long.valueOf(Settings.seed.longValue() + floorNum));
/* 2208 */     cardRandomRng = new Random(Long.valueOf(Settings.seed.longValue() + floorNum));
/* 2209 */     miscRng = new Random(Long.valueOf(Settings.seed.longValue() + floorNum));
/*      */     
/* 2211 */     boolean isLoadingPostCombatSave = (CardCrawlGame.loadingSave && saveFile != null && saveFile.post_combat);
/* 2212 */     boolean isLoadingCompletedEvent = false;
/*      */     
/* 2214 */     if (nextRoom != null && !isLoadingPostCombatSave) {
/* 2215 */       for (AbstractRelic r : player.relics) {
/* 2216 */         r.onEnterRoom(nextRoom.room);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 2221 */     if (!actionManager.actions.isEmpty()) {
/* 2222 */       logger.info("[WARNING] Line:1904: Action Manager was NOT clear! Clearing");
/* 2223 */       actionManager.clear();
/*      */     } 
/*      */     
/* 2226 */     if (nextRoom != null) {
/*      */       
/* 2228 */       String roomMetricKey = nextRoom.room.getMapSymbol();
/*      */       
/* 2230 */       if (nextRoom.room instanceof EventRoom) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2235 */         Random eventRngDuplicate = new Random(Settings.seed, eventRng.counter);
/* 2236 */         EventHelper.RoomResult roomResult = EventHelper.roll(eventRngDuplicate);
/*      */         
/* 2238 */         isLoadingCompletedEvent = (isLoadingPostCombatSave && roomResult == EventHelper.RoomResult.EVENT);
/* 2239 */         if (!isLoadingCompletedEvent) {
/*      */           
/* 2241 */           eventRng = eventRngDuplicate;
/* 2242 */           nextRoom.room = generateRoom(roomResult);
/*      */         } 
/*      */         
/* 2245 */         roomMetricKey = nextRoom.room.getMapSymbol();
/*      */         
/* 2247 */         if (nextRoom.room instanceof MonsterRoom || nextRoom.room instanceof MonsterRoomElite) {
/* 2248 */           nextRoom.room.combatEvent = true;
/*      */         }
/* 2250 */         nextRoom.room.setMapSymbol("?");
/* 2251 */         nextRoom.room.setMapImg(ImageMaster.MAP_NODE_EVENT, ImageMaster.MAP_NODE_EVENT_OUTLINE);
/*      */       } 
/*      */       
/* 2254 */       if (!isLoadingPostCombatSave) {
/* 2255 */         CardCrawlGame.metricData.path_per_floor.add(roomMetricKey);
/*      */       }
/* 2257 */       setCurrMapNode(nextRoom);
/*      */     } 
/*      */     
/* 2260 */     if (getCurrRoom() != null && !isLoadingPostCombatSave) {
/* 2261 */       for (AbstractRelic r : player.relics) {
/* 2262 */         r.justEnteredRoom(getCurrRoom());
/*      */       }
/*      */     }
/*      */     
/* 2266 */     if (isLoadingCompletedEvent) {
/* 2267 */       (getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
/* 2268 */       String eventKey = (String)((HashMap)saveFile.metric_event_choices.get(saveFile.metric_event_choices.size() - 1)).get("event_name");
/*      */       
/* 2270 */       ((EventRoom)getCurrRoom()).event = EventHelper.getEvent(eventKey);
/*      */     } else {
/* 2272 */       if (isAscensionMode) {
/* 2273 */         CardCrawlGame.publisherIntegration.setRichPresenceDisplayPlaying(floorNum, ascensionLevel, player
/*      */ 
/*      */             
/* 2276 */             .getLocalizedCharacterName());
/*      */       } else {
/* 2278 */         CardCrawlGame.publisherIntegration.setRichPresenceDisplayPlaying(floorNum, player
/*      */             
/* 2280 */             .getLocalizedCharacterName());
/*      */       } 
/* 2282 */       getCurrRoom().onPlayerEntry();
/*      */     } 
/*      */     
/* 2285 */     if (getCurrRoom() instanceof MonsterRoom && lastCombatMetricKey.equals("Shield and Spear")) {
/*      */       
/* 2287 */       player.movePosition(Settings.WIDTH / 2.0F, floorY);
/*      */     } else {
/* 2289 */       player.movePosition(Settings.WIDTH * 0.25F, floorY);
/* 2290 */       player.flipHorizontal = false;
/*      */     } 
/*      */ 
/*      */     
/* 2294 */     if (currMapNode.room instanceof MonsterRoom && !isLoadingPostCombatSave) {
/* 2295 */       player.preBattlePrep();
/*      */     }
/*      */ 
/*      */     
/* 2299 */     scene.nextRoom(currMapNode.room);
/*      */     
/* 2301 */     if (currMapNode.room instanceof RestRoom) {
/* 2302 */       rs = RenderScene.CAMPFIRE;
/* 2303 */     } else if (currMapNode.room.event instanceof com.megacrit.cardcrawl.events.AbstractImageEvent) {
/* 2304 */       rs = RenderScene.EVENT;
/*      */     } else {
/* 2306 */       rs = RenderScene.NORMAL;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void incrementFloorBasedMetrics() {
/* 2311 */     if (floorNum != 0) {
/* 2312 */       CardCrawlGame.metricData.current_hp_per_floor.add(Integer.valueOf(player.currentHealth));
/* 2313 */       CardCrawlGame.metricData.max_hp_per_floor.add(Integer.valueOf(player.maxHealth));
/* 2314 */       CardCrawlGame.metricData.gold_per_floor.add(Integer.valueOf(player.gold));
/*      */     } 
/*      */   }
/*      */   
/*      */   private AbstractRoom generateRoom(EventHelper.RoomResult roomType) {
/* 2319 */     logger.info("GENERATING ROOM: " + roomType.name());
/* 2320 */     switch (roomType) {
/*      */       case NORMAL:
/* 2322 */         return (AbstractRoom)new MonsterRoomElite();
/*      */       case CAMPFIRE:
/* 2324 */         return (AbstractRoom)new MonsterRoom();
/*      */       case EVENT:
/* 2326 */         return (AbstractRoom)new ShopRoom();
/*      */       case null:
/* 2328 */         return (AbstractRoom)new TreasureRoom();
/*      */     } 
/* 2330 */     return (AbstractRoom)new EventRoom();
/*      */   }
/*      */ 
/*      */   
/*      */   public static MonsterGroup getMonsters() {
/* 2335 */     return (getCurrRoom()).monsters;
/*      */   }
/*      */   
/*      */   public MonsterGroup getMonsterForRoomCreation() {
/* 2339 */     if (monsterList.isEmpty()) {
/* 2340 */       generateStrongEnemies(12);
/*      */     }
/* 2342 */     logger.info("MONSTER: " + (String)monsterList.get(0));
/* 2343 */     lastCombatMetricKey = monsterList.get(0);
/* 2344 */     return MonsterHelper.getEncounter(monsterList.get(0));
/*      */   }
/*      */   
/*      */   public MonsterGroup getEliteMonsterForRoomCreation() {
/* 2348 */     if (eliteMonsterList.isEmpty()) {
/* 2349 */       generateElites(10);
/*      */     }
/* 2351 */     logger.info("ELITE: " + (String)eliteMonsterList.get(0));
/* 2352 */     lastCombatMetricKey = eliteMonsterList.get(0);
/* 2353 */     return MonsterHelper.getEncounter(eliteMonsterList.get(0));
/*      */   }
/*      */ 
/*      */   
/*      */   public static AbstractEvent generateEvent(Random rng) {
/* 2358 */     if (rng.random(1.0F) < shrineChance) {
/* 2359 */       if (!shrineList.isEmpty() || !specialOneTimeEventList.isEmpty())
/* 2360 */         return getShrine(rng); 
/* 2361 */       if (!eventList.isEmpty()) {
/* 2362 */         return getEvent(rng);
/*      */       }
/* 2364 */       logger.info("No events or shrines left");
/* 2365 */       return null;
/*      */     } 
/*      */ 
/*      */     
/* 2369 */     AbstractEvent retVal = getEvent(rng);
/* 2370 */     if (retVal == null) {
/* 2371 */       return getShrine(rng);
/*      */     }
/* 2373 */     return retVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static AbstractEvent getShrine(Random rng) {
/* 2379 */     ArrayList<String> tmp = new ArrayList<>();
/* 2380 */     tmp.addAll(shrineList);
/*      */ 
/*      */     
/* 2383 */     for (String e : specialOneTimeEventList) {
/* 2384 */       switch (e) {
/*      */         case "Fountain of Cleansing":
/* 2386 */           if (player.isCursed()) {
/* 2387 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "Designer":
/* 2391 */           if ((id.equals("TheCity") || id.equals("TheBeyond")) && player.gold >= 75) {
/* 2392 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "Duplicator":
/* 2396 */           if (id.equals("TheCity") || id.equals("TheBeyond")) {
/* 2397 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "FaceTrader":
/* 2401 */           if (id.equals("TheCity") || id.equals("Exordium")) {
/* 2402 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "Knowing Skull":
/* 2406 */           if (id.equals("TheCity") && player.currentHealth > 12) {
/* 2407 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "N'loth":
/* 2411 */           if ((id.equals("TheCity") || id.equals("TheCity")) && player.relics.size() >= 2) {
/* 2412 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "The Joust":
/* 2416 */           if (id.equals("TheCity") && player.gold >= 50) {
/* 2417 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "The Woman in Blue":
/* 2421 */           if (player.gold >= 50) {
/* 2422 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "SecretPortal":
/* 2426 */           if (CardCrawlGame.playtime >= 800.0F && id.equals("TheBeyond")) {
/* 2427 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */       } 
/* 2431 */       tmp.add(e);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2436 */     String tmpKey = tmp.get(rng.random(tmp.size() - 1));
/* 2437 */     shrineList.remove(tmpKey);
/* 2438 */     specialOneTimeEventList.remove(tmpKey);
/* 2439 */     logger.info("Removed event: " + tmpKey + " from pool.");
/*      */     
/* 2441 */     return EventHelper.getEvent(tmpKey);
/*      */   }
/*      */   
/*      */   public static AbstractEvent getEvent(Random rng) {
/* 2445 */     ArrayList<String> tmp = new ArrayList<>();
/* 2446 */     for (String e : eventList) {
/* 2447 */       switch (e) {
/*      */         case "Dead Adventurer":
/* 2449 */           if (floorNum > 6) {
/* 2450 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "Mushrooms":
/* 2454 */           if (floorNum > 6) {
/* 2455 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "The Moai Head":
/* 2459 */           if (!player.hasRelic("Golden Idol") && player.currentHealth / player.maxHealth > 0.5F) {
/*      */             continue;
/*      */           }
/*      */           
/* 2463 */           tmp.add(e);
/*      */           continue;
/*      */         
/*      */         case "The Cleric":
/* 2467 */           if (player.gold >= 35) {
/* 2468 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "Beggar":
/* 2472 */           if (player.gold >= 75) {
/* 2473 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */         case "Colosseum":
/* 2477 */           if (currMapNode != null && currMapNode.y > map.size() / 2) {
/* 2478 */             tmp.add(e);
/*      */           }
/*      */           continue;
/*      */       } 
/* 2482 */       tmp.add(e);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2487 */     if (tmp.isEmpty()) {
/* 2488 */       return getShrine(rng);
/*      */     }
/*      */     
/* 2491 */     String tmpKey = tmp.get(rng.random(tmp.size() - 1));
/* 2492 */     eventList.remove(tmpKey);
/* 2493 */     logger.info("Removed event: " + tmpKey + " from pool.");
/*      */     
/* 2495 */     return EventHelper.getEvent(tmpKey);
/*      */   }
/*      */   
/*      */   public MonsterGroup getBoss() {
/* 2499 */     lastCombatMetricKey = bossKey;
/* 2500 */     dungeonMapScreen.map.atBoss = true;
/* 2501 */     return MonsterHelper.getEncounter(bossKey);
/*      */   }
/*      */ 
/*      */   
/*      */   public void update() {
/* 2506 */     if (!CardCrawlGame.stopClock) {
/* 2507 */       CardCrawlGame.playtime += Gdx.graphics.getDeltaTime();
/*      */     }
/*      */     
/* 2510 */     if (CardCrawlGame.screenTimer > 0.0F) {
/* 2511 */       InputHelper.justClickedLeft = false;
/* 2512 */       CInputActionSet.select.unpress();
/*      */     } 
/*      */     
/* 2515 */     topPanel.update();
/* 2516 */     dynamicBanner.update();
/* 2517 */     updateFading();
/* 2518 */     currMapNode.room.updateObjects();
/*      */     
/* 2520 */     if (isScreenUp) {
/* 2521 */       topGradientColor.a = MathHelper.fadeLerpSnap(topGradientColor.a, 0.25F);
/* 2522 */       botGradientColor.a = MathHelper.fadeLerpSnap(botGradientColor.a, 0.25F);
/*      */     } else {
/* 2524 */       topGradientColor.a = MathHelper.fadeLerpSnap(topGradientColor.a, 0.1F);
/* 2525 */       botGradientColor.a = MathHelper.fadeLerpSnap(botGradientColor.a, 0.1F);
/*      */     } 
/*      */     
/* 2528 */     switch (screen) {
/*      */       case NORMAL:
/*      */       case CAMPFIRE:
/* 2531 */         dungeonMapScreen.update();
/* 2532 */         currMapNode.room.update();
/* 2533 */         scene.update();
/* 2534 */         currMapNode.room.eventControllerInput();
/*      */         break;
/*      */       case EVENT:
/* 2537 */         ftue.update();
/* 2538 */         InputHelper.justClickedRight = false;
/* 2539 */         InputHelper.justClickedLeft = false;
/* 2540 */         currMapNode.room.update();
/*      */         break;
/*      */       case null:
/* 2543 */         deckViewScreen.update();
/*      */         break;
/*      */       case null:
/* 2546 */         gameDeckViewScreen.update();
/*      */         break;
/*      */       case null:
/* 2549 */         discardPileViewScreen.update();
/*      */         break;
/*      */       case null:
/* 2552 */         exhaustPileViewScreen.update();
/*      */         break;
/*      */       case null:
/* 2555 */         settingsScreen.update();
/*      */         break;
/*      */       case null:
/* 2558 */         inputSettingsScreen.update();
/*      */         break;
/*      */       case null:
/* 2561 */         dungeonMapScreen.update();
/*      */         break;
/*      */       case null:
/* 2564 */         gridSelectScreen.update();
/* 2565 */         if (PeekButton.isPeeking) {
/* 2566 */           currMapNode.room.update();
/*      */         }
/*      */         break;
/*      */       case null:
/* 2570 */         cardRewardScreen.update();
/* 2571 */         if (PeekButton.isPeeking) {
/* 2572 */           currMapNode.room.update();
/*      */         }
/*      */         break;
/*      */       case null:
/* 2576 */         combatRewardScreen.update();
/*      */         break;
/*      */       case null:
/* 2579 */         bossRelicScreen.update();
/* 2580 */         currMapNode.room.update();
/*      */         break;
/*      */       case null:
/* 2583 */         handCardSelectScreen.update();
/* 2584 */         currMapNode.room.update();
/*      */         break;
/*      */       case null:
/* 2587 */         shopScreen.update();
/*      */         break;
/*      */       case null:
/* 2590 */         deathScreen.update();
/*      */         break;
/*      */       case null:
/* 2593 */         victoryScreen.update();
/*      */         break;
/*      */       case null:
/* 2596 */         unlockScreen.update();
/*      */         break;
/*      */       case null:
/* 2599 */         gUnlockScreen.update();
/*      */         break;
/*      */       case null:
/* 2602 */         creditsScreen.update();
/*      */         break;
/*      */       case null:
/* 2605 */         CardCrawlGame.mainMenuScreen.doorUnlockScreen.update();
/*      */         break;
/*      */       default:
/* 2608 */         logger.info("ERROR: UNKNOWN SCREEN TO UPDATE: " + screen.name());
/*      */         break;
/*      */     } 
/* 2611 */     turnPhaseEffectActive = false;
/*      */     
/*      */     Iterator<AbstractGameEffect> i;
/* 2614 */     for (i = topLevelEffects.iterator(); i.hasNext(); ) {
/* 2615 */       AbstractGameEffect e = i.next();
/* 2616 */       e.update();
/*      */       
/* 2618 */       if (e.isDone) {
/* 2619 */         i.remove();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2624 */     for (i = effectList.iterator(); i.hasNext(); ) {
/* 2625 */       AbstractGameEffect e = i.next();
/*      */       
/* 2627 */       e.update();
/* 2628 */       if (e instanceof com.megacrit.cardcrawl.vfx.PlayerTurnEffect) {
/* 2629 */         turnPhaseEffectActive = true;
/*      */       }
/*      */       
/* 2632 */       if (e.isDone) {
/* 2633 */         i.remove();
/*      */       }
/*      */     } 
/*      */     
/* 2637 */     for (i = effectsQueue.iterator(); i.hasNext(); ) {
/* 2638 */       AbstractGameEffect e = i.next();
/* 2639 */       effectList.add(e);
/* 2640 */       i.remove();
/*      */     } 
/*      */     
/* 2643 */     for (i = topLevelEffectsQueue.iterator(); i.hasNext(); ) {
/* 2644 */       AbstractGameEffect e = i.next();
/* 2645 */       topLevelEffects.add(e);
/* 2646 */       i.remove();
/*      */     } 
/*      */     
/* 2649 */     overlayMenu.update();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void render(SpriteBatch sb) {
/* 2658 */     switch (rs) {
/*      */       case NORMAL:
/* 2660 */         scene.renderCombatRoomBg(sb);
/*      */         break;
/*      */       case CAMPFIRE:
/* 2663 */         scene.renderCampfireRoom(sb);
/* 2664 */         renderLetterboxGradient(sb);
/*      */         break;
/*      */       case EVENT:
/* 2667 */         scene.renderEventRoom(sb);
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 2672 */     for (AbstractGameEffect e : effectList) {
/* 2673 */       if (e.renderBehind) {
/* 2674 */         e.render(sb);
/*      */       }
/*      */     } 
/*      */     
/* 2678 */     currMapNode.room.render(sb);
/*      */     
/* 2680 */     if (rs == RenderScene.NORMAL) {
/* 2681 */       scene.renderCombatRoomFg(sb);
/*      */     }
/*      */     
/* 2684 */     if (rs != RenderScene.CAMPFIRE) {
/* 2685 */       renderLetterboxGradient(sb);
/*      */     }
/*      */     
/* 2688 */     AbstractRoom room = getCurrRoom();
/*      */     
/* 2690 */     if (room instanceof EventRoom || room instanceof NeowRoom || room instanceof VictoryRoom) {
/* 2691 */       room.renderEventTexts(sb);
/*      */     }
/*      */ 
/*      */     
/* 2695 */     for (AbstractGameEffect e : effectList) {
/* 2696 */       if (!e.renderBehind) {
/* 2697 */         e.render(sb);
/*      */       }
/*      */     } 
/*      */     
/* 2701 */     overlayMenu.render(sb);
/* 2702 */     overlayMenu.renderBlackScreen(sb);
/*      */     
/* 2704 */     switch (screen) {
/*      */       case CAMPFIRE:
/* 2706 */         dungeonMapScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2709 */         deckViewScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2712 */         discardPileViewScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2715 */         gameDeckViewScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2718 */         exhaustPileViewScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2721 */         settingsScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2724 */         inputSettingsScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2727 */         dungeonMapScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2730 */         gridSelectScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2733 */         cardRewardScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2736 */         combatRewardScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2739 */         bossRelicScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2742 */         handCardSelectScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2745 */         shopScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2748 */         deathScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2751 */         victoryScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2754 */         unlockScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2757 */         CardCrawlGame.mainMenuScreen.doorUnlockScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2760 */         gUnlockScreen.render(sb);
/*      */         break;
/*      */       case null:
/* 2763 */         creditsScreen.render(sb);
/*      */         break;
/*      */       case EVENT:
/*      */       case NORMAL:
/*      */         break;
/*      */       default:
/* 2769 */         logger.info("ERROR: UNKNOWN SCREEN TO RENDER: " + screen.name());
/*      */         break;
/*      */     } 
/*      */     
/* 2773 */     if (screen != CurrentScreen.UNLOCK) {
/* 2774 */       sb.setColor(topGradientColor);
/* 2775 */       if (!Settings.hideTopBar) {
/* 2776 */         sb.draw(ImageMaster.SCROLL_GRADIENT, 0.0F, Settings.HEIGHT - 128.0F * Settings.scale, Settings.WIDTH, 64.0F * Settings.scale);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2783 */       sb.setColor(botGradientColor);
/* 2784 */       if (!Settings.hideTopBar) {
/* 2785 */         sb.draw(ImageMaster.SCROLL_GRADIENT, 0.0F, 64.0F * Settings.scale, Settings.WIDTH, -64.0F * Settings.scale);
/*      */       }
/*      */     } 
/*      */     
/* 2789 */     if (screen == CurrentScreen.FTUE) {
/* 2790 */       ftue.render(sb);
/*      */     }
/*      */     
/* 2793 */     overlayMenu.cancelButton.render(sb);
/* 2794 */     dynamicBanner.render(sb);
/* 2795 */     if (screen != CurrentScreen.UNLOCK) {
/* 2796 */       topPanel.render(sb);
/*      */     }
/* 2798 */     currMapNode.room.renderAboveTopPanel(sb);
/*      */     
/* 2800 */     for (AbstractGameEffect e : topLevelEffects) {
/* 2801 */       if (!e.renderBehind) {
/* 2802 */         e.render(sb);
/*      */       }
/*      */     } 
/*      */     
/* 2806 */     sb.setColor(fadeColor);
/* 2807 */     sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
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
/*      */   private void renderLetterboxGradient(SpriteBatch sb) {}
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
/*      */   public void updateFading() {
/* 2832 */     if (isFadingIn) {
/* 2833 */       fadeTimer -= Gdx.graphics.getDeltaTime();
/* 2834 */       fadeColor.a = Interpolation.fade.apply(0.0F, 1.0F, fadeTimer / 0.8F);
/* 2835 */       if (fadeTimer < 0.0F) {
/* 2836 */         isFadingIn = false;
/* 2837 */         fadeColor.a = 0.0F;
/* 2838 */         fadeTimer = 0.0F;
/*      */       } 
/* 2840 */     } else if (isFadingOut) {
/* 2841 */       fadeTimer -= Gdx.graphics.getDeltaTime();
/* 2842 */       fadeColor.a = Interpolation.fade.apply(1.0F, 0.0F, fadeTimer / 0.8F);
/* 2843 */       if (fadeTimer < 0.0F) {
/* 2844 */         fadeTimer = 0.0F;
/* 2845 */         isFadingOut = false;
/* 2846 */         fadeColor.a = 1.0F;
/* 2847 */         if (!isDungeonBeaten) {
/* 2848 */           nextRoomTransition();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeCurrentScreen() {
/* 2858 */     PeekButton.isPeeking = false;
/*      */     
/* 2860 */     if (previousScreen == screen) {
/* 2861 */       previousScreen = null;
/*      */     }
/*      */     
/* 2864 */     switch (screen) {
/*      */       case null:
/* 2866 */         overlayMenu.cancelButton.hide();
/* 2867 */         genericScreenOverlayReset();
/* 2868 */         for (AbstractCard c : player.masterDeck.group) {
/* 2869 */           c.unhover();
/* 2870 */           c.untip();
/*      */         } 
/*      */         break;
/*      */       case null:
/* 2874 */         overlayMenu.cancelButton.hide();
/* 2875 */         genericScreenOverlayReset();
/* 2876 */         for (AbstractCard c : player.discardPile.group) {
/* 2877 */           c.drawScale = 0.12F;
/* 2878 */           c.targetDrawScale = 0.12F;
/* 2879 */           c.teleportToDiscardPile();
/* 2880 */           c.darken(true);
/* 2881 */           c.unhover();
/*      */         } 
/*      */         break;
/*      */       case EVENT:
/* 2885 */         genericScreenOverlayReset();
/*      */         break;
/*      */       case null:
/* 2888 */         overlayMenu.cancelButton.hide();
/* 2889 */         genericScreenOverlayReset();
/*      */         break;
/*      */       case null:
/* 2892 */         overlayMenu.cancelButton.hide();
/* 2893 */         genericScreenOverlayReset();
/*      */         break;
/*      */       case null:
/* 2896 */         overlayMenu.cancelButton.hide();
/* 2897 */         genericScreenOverlayReset();
/* 2898 */         settingsScreen.abandonPopup.hide();
/* 2899 */         settingsScreen.exitPopup.hide();
/*      */         break;
/*      */       case null:
/* 2902 */         overlayMenu.cancelButton.hide();
/* 2903 */         genericScreenOverlayReset();
/* 2904 */         settingsScreen.abandonPopup.hide();
/* 2905 */         settingsScreen.exitPopup.hide();
/*      */         break;
/*      */       case null:
/* 2908 */         genericScreenOverlayReset();
/* 2909 */         CardCrawlGame.sound.stop("UNLOCK_SCREEN", gUnlockScreen.id);
/*      */         break;
/*      */       case null:
/* 2912 */         genericScreenOverlayReset();
/* 2913 */         if (!combatRewardScreen.rewards.isEmpty()) {
/* 2914 */           previousScreen = CurrentScreen.COMBAT_REWARD;
/*      */         }
/*      */         break;
/*      */       case null:
/* 2918 */         overlayMenu.cancelButton.hide();
/* 2919 */         dynamicBanner.hide();
/* 2920 */         genericScreenOverlayReset();
/* 2921 */         if (!screenSwap) {
/* 2922 */           cardRewardScreen.onClose();
/*      */         }
/*      */         break;
/*      */       case null:
/* 2926 */         dynamicBanner.hide();
/* 2927 */         genericScreenOverlayReset();
/*      */         break;
/*      */       case null:
/* 2930 */         genericScreenOverlayReset();
/* 2931 */         dynamicBanner.hide();
/*      */         break;
/*      */       case null:
/* 2934 */         genericScreenOverlayReset();
/* 2935 */         overlayMenu.showCombatPanels();
/*      */         break;
/*      */       case null:
/* 2938 */         genericScreenOverlayReset();
/* 2939 */         dungeonMapScreen.close();
/* 2940 */         if (!firstRoomChosen && nextRoom != null && !dungeonMapScreen.dismissable) {
/* 2941 */           firstRoomChosen = true;
/* 2942 */           firstRoomLogic();
/*      */         } 
/*      */         break;
/*      */       case null:
/* 2946 */         CardCrawlGame.sound.play("SHOP_CLOSE");
/* 2947 */         genericScreenOverlayReset();
/* 2948 */         overlayMenu.cancelButton.hide();
/*      */         break;
/*      */       case null:
/* 2951 */         CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
/* 2952 */         genericScreenOverlayReset();
/* 2953 */         overlayMenu.cancelButton.hide();
/*      */         break;
/*      */       default:
/* 2956 */         logger.info("UNSPECIFIED CASE: " + screen.name());
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 2961 */     if (previousScreen == null) {
/* 2962 */       screen = CurrentScreen.NONE;
/*      */     }
/* 2964 */     else if (screenSwap) {
/* 2965 */       screenSwap = false;
/*      */     } else {
/*      */       
/* 2968 */       screen = previousScreen;
/* 2969 */       previousScreen = null;
/* 2970 */       if ((getCurrRoom()).rewardTime) {
/* 2971 */         previousScreen = CurrentScreen.COMBAT_REWARD;
/*      */       }
/* 2973 */       isScreenUp = true;
/* 2974 */       openPreviousScreen(screen);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void openPreviousScreen(CurrentScreen s) {
/* 2979 */     switch (s) {
/*      */       case null:
/* 2981 */         deathScreen.reopen();
/*      */         break;
/*      */       case null:
/* 2984 */         victoryScreen.reopen();
/*      */         break;
/*      */       case null:
/* 2987 */         deckViewScreen.open();
/*      */         break;
/*      */       case null:
/* 2990 */         cardRewardScreen.reopen();
/*      */ 
/*      */         
/* 2993 */         if (cardRewardScreen.rItem != null) {
/* 2994 */           previousScreen = CurrentScreen.COMBAT_REWARD;
/*      */         }
/*      */         break;
/*      */       case null:
/* 2998 */         discardPileViewScreen.reopen();
/*      */         break;
/*      */       case null:
/* 3001 */         exhaustPileViewScreen.reopen();
/*      */         break;
/*      */       case null:
/* 3004 */         gameDeckViewScreen.reopen();
/*      */         break;
/*      */       case null:
/* 3007 */         overlayMenu.hideBlackScreen();
/* 3008 */         handCardSelectScreen.reopen();
/*      */         break;
/*      */       case null:
/* 3011 */         combatRewardScreen.reopen();
/*      */         break;
/*      */       case null:
/* 3014 */         bossRelicScreen.reopen();
/*      */         break;
/*      */       case null:
/* 3017 */         shopScreen.open();
/*      */         break;
/*      */       case null:
/* 3020 */         overlayMenu.hideBlackScreen();
/* 3021 */         if (gridSelectScreen.isJustForConfirming) {
/* 3022 */           dynamicBanner.appear();
/*      */         }
/* 3024 */         gridSelectScreen.reopen();
/*      */         break;
/*      */       case null:
/* 3027 */         gUnlockScreen.reOpen();
/*      */         break;
/*      */       case null:
/* 3030 */         if (dungeonMapScreen.dismissable) {
/* 3031 */           overlayMenu.cancelButton.show(DungeonMapScreen.TEXT[1]); break;
/*      */         } 
/* 3033 */         overlayMenu.cancelButton.hide();
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
/*      */   private static void genericScreenOverlayReset() {
/* 3045 */     if (previousScreen == null) {
/* 3046 */       if (player.isDead) {
/* 3047 */         previousScreen = CurrentScreen.DEATH;
/*      */       } else {
/* 3049 */         isScreenUp = false;
/* 3050 */         overlayMenu.hideBlackScreen();
/*      */       } 
/*      */     }
/*      */     
/* 3054 */     if ((getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !player.isDead) {
/* 3055 */       overlayMenu.showCombatPanels();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fadeIn() {
/* 3063 */     if (fadeColor.a != 1.0F) {
/* 3064 */       logger.info("WARNING: Attempting to fade in even though screen is not black");
/*      */     }
/* 3066 */     isFadingIn = true;
/* 3067 */     if (Settings.FAST_MODE) {
/* 3068 */       fadeTimer = 0.001F;
/*      */     } else {
/* 3070 */       fadeTimer = 0.8F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fadeOut() {
/* 3078 */     if (fadeTimer == 0.0F) {
/* 3079 */       if (fadeColor.a != 0.0F) {
/* 3080 */         logger.info("WARNING: Attempting to fade out even though screen is not transparent");
/*      */       }
/* 3082 */       isFadingOut = true;
/* 3083 */       if (Settings.FAST_MODE) {
/* 3084 */         fadeTimer = 0.001F;
/*      */       } else {
/* 3086 */         fadeTimer = 0.8F;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum CurrentScreen
/*      */   {
/* 3096 */     NONE, MASTER_DECK_VIEW, SETTINGS, INPUT_SETTINGS, GRID, MAP, FTUE, CHOOSE_ONE, HAND_SELECT, SHOP, COMBAT_REWARD, DISCARD_VIEW, EXHAUST_VIEW, GAME_DECK_VIEW, BOSS_REWARD, DEATH, CARD_REWARD, TRANSFORM, VICTORY, UNLOCK, DOOR_UNLOCK, CREDITS, NO_INTERACT, NEOW_UNLOCK;
/*      */   }
/*      */   
/*      */   public enum RenderScene {
/* 3100 */     NORMAL, EVENT, CAMPFIRE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void dungeonTransitionSetup() {
/* 3107 */     actNum++;
/*      */ 
/*      */     
/* 3110 */     if (cardRng.counter > 0 && cardRng.counter < 250) {
/* 3111 */       cardRng.setCounter(250);
/* 3112 */     } else if (cardRng.counter > 250 && cardRng.counter < 500) {
/* 3113 */       cardRng.setCounter(500);
/* 3114 */     } else if (cardRng.counter > 500 && cardRng.counter < 750) {
/* 3115 */       cardRng.setCounter(750);
/*      */     } 
/* 3117 */     logger.info("CardRng Counter: " + cardRng.counter);
/*      */     
/* 3119 */     topPanel.unhoverHitboxes();
/*      */     
/* 3121 */     pathX.clear();
/* 3122 */     pathY.clear();
/*      */ 
/*      */     
/* 3125 */     EventHelper.resetProbabilities();
/* 3126 */     eventList.clear();
/* 3127 */     shrineList.clear();
/*      */ 
/*      */     
/* 3130 */     monsterList.clear();
/* 3131 */     eliteMonsterList.clear();
/* 3132 */     bossList.clear();
/*      */ 
/*      */     
/* 3135 */     AbstractRoom.blizzardPotionMod = 0;
/*      */ 
/*      */ 
/*      */     
/* 3139 */     if (ascensionLevel >= 5) {
/* 3140 */       player.heal(MathUtils.round((player.maxHealth - player.currentHealth) * 0.75F), false);
/*      */     } else {
/* 3142 */       player.heal(player.maxHealth, false);
/*      */     } 
/*      */     
/* 3145 */     if (floorNum > 1) {
/* 3146 */       topPanel.panelHealEffect();
/*      */     }
/*      */ 
/*      */     
/* 3150 */     if (floorNum <= 1)
/*      */     {
/* 3152 */       if (CardCrawlGame.dungeon instanceof Exordium) {
/* 3153 */         if (ascensionLevel >= 14) {
/* 3154 */           player.decreaseMaxHealth(player.getAscensionMaxHPLoss());
/*      */         }
/* 3156 */         if (ascensionLevel >= 6) {
/* 3157 */           player.currentHealth = MathUtils.round(player.maxHealth * 0.9F);
/*      */         }
/* 3159 */         if (ascensionLevel >= 10) {
/* 3160 */           player.masterDeck.addToTop((AbstractCard)new AscendersBane());
/* 3161 */           UnlockTracker.markCardAsSeen("AscendersBane");
/*      */         } 
/* 3163 */         CardCrawlGame.playtime = 0.0F;
/*      */       } 
/*      */     }
/*      */     
/* 3167 */     dungeonMapScreen.map.atBoss = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reset() {
/* 3174 */     logger.info("Resetting variables...");
/* 3175 */     CardCrawlGame.resetScoreVars();
/* 3176 */     ModHelper.setModsFalse();
/* 3177 */     floorNum = 0;
/* 3178 */     actNum = 0;
/*      */ 
/*      */     
/* 3181 */     if (currMapNode != null && getCurrRoom() != null) {
/* 3182 */       getCurrRoom().dispose();
/* 3183 */       if ((getCurrRoom()).monsters != null) {
/* 3184 */         for (AbstractMonster m : (getCurrRoom()).monsters.monsters) {
/* 3185 */           m.dispose();
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/* 3190 */     currMapNode = null;
/* 3191 */     shrineList.clear();
/* 3192 */     relicsToRemoveOnStart.clear();
/* 3193 */     previousScreen = null;
/* 3194 */     actionManager.clear();
/* 3195 */     actionManager.clearNextRoomCombatActions();
/* 3196 */     combatRewardScreen.clear();
/* 3197 */     cardRewardScreen.reset();
/*      */     
/* 3199 */     if (dungeonMapScreen != null) {
/* 3200 */       dungeonMapScreen.closeInstantly();
/*      */     }
/*      */     
/* 3203 */     effectList.clear();
/* 3204 */     effectsQueue.clear();
/* 3205 */     topLevelEffectsQueue.clear();
/* 3206 */     topLevelEffects.clear();
/* 3207 */     cardBlizzRandomizer = cardBlizzStartOffset;
/* 3208 */     if (player != null) {
/* 3209 */       player.relics.clear();
/*      */     }
/*      */     
/* 3212 */     rs = RenderScene.NORMAL;
/* 3213 */     blightPool.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void removeRelicFromPool(ArrayList<String> pool, String name) {
/* 3218 */     for (Iterator<String> i = pool.iterator(); i.hasNext(); ) {
/* 3219 */       String s = i.next();
/* 3220 */       if (s.equals(name)) {
/* 3221 */         i.remove();
/* 3222 */         logger.info("Relic" + s + " removed from relic pool.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void onModifyPower() {
/* 3228 */     if (player != null) {
/* 3229 */       player.hand.applyPowers();
/*      */       
/* 3231 */       if (player.hasPower("Focus")) {
/* 3232 */         for (AbstractOrb o : player.orbs) {
/* 3233 */           o.updateDescription();
/*      */         }
/*      */       }
/*      */     } 
/* 3237 */     if ((getCurrRoom()).monsters != null) {
/* 3238 */       for (AbstractMonster m : (getCurrRoom()).monsters.monsters) {
/* 3239 */         m.applyPowers();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkForPactAchievement() {
/* 3245 */     if (player != null)
/*      */     {
/* 3247 */       if (player.exhaustPile.size() >= 20) {
/* 3248 */         UnlockTracker.unlockAchievement("THE_PACT");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void loadSave(SaveFile saveFile) {
/* 3254 */     floorNum = saveFile.floor_num;
/* 3255 */     actNum = saveFile.act_num;
/* 3256 */     Settings.seed = Long.valueOf(saveFile.seed);
/* 3257 */     loadSeeds(saveFile);
/* 3258 */     monsterList = saveFile.monster_list;
/* 3259 */     eliteMonsterList = saveFile.elite_monster_list;
/* 3260 */     bossList = saveFile.boss_list;
/* 3261 */     setBoss(saveFile.boss);
/* 3262 */     commonRelicPool = saveFile.common_relics;
/* 3263 */     uncommonRelicPool = saveFile.uncommon_relics;
/* 3264 */     rareRelicPool = saveFile.rare_relics;
/* 3265 */     shopRelicPool = saveFile.shop_relics;
/* 3266 */     bossRelicPool = saveFile.boss_relics;
/* 3267 */     pathX = saveFile.path_x;
/* 3268 */     pathY = saveFile.path_y;
/* 3269 */     bossCount = saveFile.spirit_count;
/* 3270 */     eventList = saveFile.event_list;
/* 3271 */     specialOneTimeEventList = saveFile.one_time_event_list;
/* 3272 */     EventHelper.setChances(saveFile.event_chances);
/* 3273 */     AbstractRoom.blizzardPotionMod = saveFile.potion_chance;
/* 3274 */     ShopScreen.purgeCost = saveFile.purgeCost;
/* 3275 */     CardHelper.obtainedCards = saveFile.obtained_cards;
/*      */     
/* 3277 */     if (saveFile.daily_mods != null) {
/* 3278 */       ModHelper.setMods(saveFile.daily_mods);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static AbstractBlight getBlight(String targetID) {
/* 3284 */     for (AbstractBlight b : blightPool) {
/* 3285 */       if (b.blightID.equals(targetID)) {
/* 3286 */         return b;
/*      */       }
/*      */     } 
/* 3289 */     return null;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\dungeons\AbstractDungeon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */