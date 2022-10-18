/*      */ package com.megacrit.cardcrawl.core;
/*      */ 
/*      */ import com.badlogic.gdx.ApplicationListener;
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.Preferences;
/*      */ import com.badlogic.gdx.graphics.Camera;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.FPSLogger;
/*      */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*      */ import com.badlogic.gdx.graphics.Pixmap;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
/*      */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*      */ import com.badlogic.gdx.math.Interpolation;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.utils.viewport.FitViewport;
/*      */ import com.codedisaster.steamworks.SteamUtils;
/*      */ import com.codedisaster.steamworks.SteamUtilsCallback;
/*      */ import com.esotericsoftware.spine.SkeletonRendererDebug;
/*      */ import com.megacrit.cardcrawl.audio.MusicMaster;
/*      */ import com.megacrit.cardcrawl.audio.SoundMaster;
/*      */ import com.megacrit.cardcrawl.blights.AbstractBlight;
/*      */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*      */ import com.megacrit.cardcrawl.cards.CardSave;
/*      */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*      */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*      */ import com.megacrit.cardcrawl.characters.CharacterManager;
/*      */ import com.megacrit.cardcrawl.daily.TimeHelper;
/*      */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*      */ import com.megacrit.cardcrawl.dungeons.Exordium;
/*      */ import com.megacrit.cardcrawl.dungeons.TheBeyond;
/*      */ import com.megacrit.cardcrawl.dungeons.TheCity;
/*      */ import com.megacrit.cardcrawl.dungeons.TheEnding;
/*      */ import com.megacrit.cardcrawl.helpers.AsyncSaver;
/*      */ import com.megacrit.cardcrawl.helpers.BlightHelper;
/*      */ import com.megacrit.cardcrawl.helpers.CardHelper;
/*      */ import com.megacrit.cardcrawl.helpers.CardLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.DrawMaster;
/*      */ import com.megacrit.cardcrawl.helpers.FontHelper;
/*      */ import com.megacrit.cardcrawl.helpers.GameDictionary;
/*      */ import com.megacrit.cardcrawl.helpers.GameTips;
/*      */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*      */ import com.megacrit.cardcrawl.helpers.ModHelper;
/*      */ import com.megacrit.cardcrawl.helpers.MonsterHelper;
/*      */ import com.megacrit.cardcrawl.helpers.PotionHelper;
/*      */ import com.megacrit.cardcrawl.helpers.Prefs;
/*      */ import com.megacrit.cardcrawl.helpers.RelicLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.SaveHelper;
/*      */ import com.megacrit.cardcrawl.helpers.ScreenShake;
/*      */ import com.megacrit.cardcrawl.helpers.SeedHelper;
/*      */ import com.megacrit.cardcrawl.helpers.ShaderHelper;
/*      */ import com.megacrit.cardcrawl.helpers.TipHelper;
/*      */ import com.megacrit.cardcrawl.helpers.TipTracker;
/*      */ import com.megacrit.cardcrawl.helpers.TrialHelper;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
/*      */ import com.megacrit.cardcrawl.helpers.input.DevInputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*      */ import com.megacrit.cardcrawl.helpers.steamInput.SteamInputHelper;
/*      */ import com.megacrit.cardcrawl.integrations.DistributorFactory;
/*      */ import com.megacrit.cardcrawl.integrations.PublisherIntegration;
/*      */ import com.megacrit.cardcrawl.integrations.SteelSeries;
/*      */ import com.megacrit.cardcrawl.localization.LocalizedStrings;
/*      */ import com.megacrit.cardcrawl.metrics.BotDataUploader;
/*      */ import com.megacrit.cardcrawl.metrics.MetricData;
/*      */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*      */ import com.megacrit.cardcrawl.potions.AbstractPotion;
/*      */ import com.megacrit.cardcrawl.potions.PotionSlot;
/*      */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*      */ import com.megacrit.cardcrawl.random.Random;
/*      */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*      */ import com.megacrit.cardcrawl.relics.BottledFlame;
/*      */ import com.megacrit.cardcrawl.relics.BottledLightning;
/*      */ import com.megacrit.cardcrawl.relics.BottledTornado;
/*      */ import com.megacrit.cardcrawl.rewards.RewardItem;
/*      */ import com.megacrit.cardcrawl.rewards.RewardSave;
/*      */ import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
/*      */ import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
/*      */ import com.megacrit.cardcrawl.screens.DoorUnlockScreen;
/*      */ import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
/*      */ import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
/*      */ import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
/*      */ import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
/*      */ import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
/*      */ import com.megacrit.cardcrawl.screens.splash.SplashScreen;
/*      */ import com.megacrit.cardcrawl.shop.ShopScreen;
/*      */ import com.megacrit.cardcrawl.trials.AbstractTrial;
/*      */ import com.megacrit.cardcrawl.ui.buttons.CancelButton;
/*      */ import com.megacrit.cardcrawl.ui.panels.SeedPanel;
/*      */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*      */ import de.robojumper.ststwitch.TwitchConfig;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Map;
/*      */ import java.util.Scanner;
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
/*      */ public class CardCrawlGame
/*      */   implements ApplicationListener
/*      */ {
/*  114 */   public static String VERSION_NUM = "[V2.3] (03-07-2022)";
/*  115 */   public static String TRUE_VERSION_NUM = "2022-03-07"; private OrthographicCamera camera;
/*      */   public static FitViewport viewport;
/*      */   public static PolygonSpriteBatch psb;
/*      */   private SpriteBatch sb;
/*      */   public static GameCursor cursor;
/*      */   public static boolean isPopupOpen = false;
/*      */   public static int popupMX;
/*      */   public static int popupMY;
/*  123 */   public static ScreenShake screenShake = new ScreenShake();
/*      */   public static AbstractDungeon dungeon;
/*      */   public static MainMenuScreen mainMenuScreen;
/*      */   public static SplashScreen splashScreen;
/*      */   public static DungeonTransitionScreen dungeonTransitionScreen;
/*      */   public static CancelButton cancelButton;
/*      */   public static MusicMaster music;
/*      */   public static SoundMaster sound;
/*      */   public static GameTips tips;
/*      */   public static SingleCardViewPopup cardPopup;
/*      */   public static SingleRelicViewPopup relicPopup;
/*  134 */   private FPSLogger fpsLogger = new FPSLogger();
/*      */   public boolean prevDebugKeyDown = false;
/*      */   public static String nextDungeon;
/*  137 */   public static GameMode mode = GameMode.CHAR_SELECT;
/*      */   
/*      */   public static boolean startOver = false;
/*      */   
/*      */   private static boolean queueCredits = false;
/*      */   public static boolean playCreditsBgm = false;
/*      */   public static boolean MUTE_IF_BG = false;
/*  144 */   public static AbstractPlayer.PlayerClass chosenCharacter = null;
/*      */   public static boolean loadingSave;
/*  146 */   public static SaveFile saveFile = null; public static Prefs saveSlotPref;
/*      */   public static Prefs playerPref;
/*  148 */   public static int saveSlot = 0;
/*      */   
/*      */   public static String playerName;
/*      */   
/*      */   public static String alias;
/*      */   public static CharacterManager characterManager;
/*  154 */   public static int monstersSlain = 0;
/*  155 */   public static int elites1Slain = 0;
/*  156 */   public static int elites2Slain = 0;
/*  157 */   public static int elites3Slain = 0;
/*  158 */   public static int elitesModdedSlain = 0;
/*  159 */   public static int champion = 0;
/*  160 */   public static int perfect = 0;
/*      */   public static boolean overkill = false;
/*      */   public static boolean combo = false;
/*      */   public static boolean cheater = false;
/*  164 */   public static int goldGained = 0;
/*  165 */   public static int cardsPurged = 0;
/*  166 */   public static int potionsBought = 0;
/*  167 */   public static int mysteryMachine = 0;
/*  168 */   public static float playtime = 0.0F;
/*      */   
/*      */   public static boolean stopClock = false;
/*      */   
/*      */   public static SkeletonRendererDebug debugRenderer;
/*  173 */   public static AbstractTrial trial = null;
/*      */   
/*      */   public static Scanner sControllerScanner;
/*      */   
/*  177 */   SteamInputHelper steamInputHelper = null;
/*      */   public static SteamUtils clientUtils;
/*  179 */   public static Thread sInputDetectThread = null;
/*      */ 
/*      */   
/*  182 */   private static Color screenColor = Color.BLACK.cpy();
/*  183 */   public static float screenTimer = 2.0F; public static float screenTime = 2.0F;
/*      */ 
/*      */   
/*      */   private static boolean fadeIn = true;
/*      */ 
/*      */   
/*      */   public static MetricData metricData;
/*      */   
/*      */   public static PublisherIntegration publisherIntegration;
/*      */   
/*      */   public static SteelSeries steelSeries;
/*      */   
/*      */   public static LocalizedStrings languagePack;
/*      */   
/*      */   private boolean displayCursor = true;
/*      */   
/*      */   public static boolean displayVersion = true;
/*      */   
/*  201 */   public static String preferenceDir = null;
/*  202 */   private static final Logger logger = LogManager.getLogger(CardCrawlGame.class.getName());
/*      */ 
/*      */   
/*      */   private SteamUtilsCallback clUtilsCallback;
/*      */ 
/*      */ 
/*      */   
/*      */   public void create() {
/*  210 */     if (Settings.isAlpha) {
/*  211 */       TRUE_VERSION_NUM += " ALPHA";
/*  212 */       VERSION_NUM += " ALPHA";
/*  213 */     } else if (Settings.isBeta) {
/*  214 */       VERSION_NUM += " BETA";
/*      */     } 
/*      */     
/*      */     try {
/*  218 */       TwitchConfig.createConfig();
/*      */       
/*  220 */       BuildSettings buildSettings = new BuildSettings(Gdx.files.internal("build.properties").reader());
/*  221 */       logger.info("DistributorPlatform=" + buildSettings.getDistributor());
/*  222 */       logger.info("isModded=" + Settings.isModded);
/*  223 */       logger.info("isBeta=" + Settings.isBeta);
/*  224 */       publisherIntegration = DistributorFactory.getEnabledDistributor(buildSettings.getDistributor());
/*      */ 
/*      */       
/*  227 */       saveMigration();
/*      */       
/*  229 */       saveSlotPref = SaveHelper.getPrefs("STSSaveSlots");
/*  230 */       saveSlot = saveSlotPref.getInteger("DEFAULT_SLOT", 0);
/*  231 */       playerPref = SaveHelper.getPrefs("STSPlayer");
/*  232 */       playerName = saveSlotPref.getString(SaveHelper.slotName("PROFILE_NAME", saveSlot), "");
/*  233 */       if (playerName.equals("")) {
/*  234 */         playerName = playerPref.getString("name", "");
/*      */       }
/*  236 */       alias = playerPref.getString("alias", "");
/*      */       
/*  238 */       if (alias.equals("")) {
/*  239 */         alias = generateRandomAlias();
/*  240 */         playerPref.putString("alias", alias);
/*  241 */         playerPref.flush();
/*      */       } 
/*      */ 
/*      */       
/*  245 */       Settings.initialize(false);
/*      */ 
/*      */       
/*  248 */       this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*  249 */       if (Settings.VERT_LETTERBOX_AMT != 0 || Settings.HORIZ_LETTERBOX_AMT != 0) {
/*  250 */         this.camera.position.set(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, 0.0F);
/*  251 */         this.camera.update();
/*  252 */         viewport = new FitViewport(Settings.WIDTH, (Settings.M_H - Settings.HEIGHT / 2), (Camera)this.camera);
/*      */       } else {
/*  254 */         this.camera.position.set(this.camera.viewportWidth / 2.0F, this.camera.viewportHeight / 2.0F, 0.0F);
/*  255 */         this.camera.update();
/*  256 */         viewport = new FitViewport(Settings.WIDTH, Settings.HEIGHT, (Camera)this.camera);
/*  257 */         viewport.apply();
/*      */       } 
/*      */       
/*  260 */       languagePack = new LocalizedStrings();
/*  261 */       cardPopup = new SingleCardViewPopup();
/*  262 */       relicPopup = new SingleRelicViewPopup();
/*      */       
/*  264 */       if (Settings.IS_FULLSCREEN) {
/*  265 */         resize(Settings.M_W, Settings.M_H);
/*      */       }
/*  267 */       Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/blank.png")), 0, 0));
/*  268 */       this.sb = new SpriteBatch();
/*  269 */       psb = new PolygonSpriteBatch();
/*      */       
/*  271 */       music = new MusicMaster();
/*  272 */       sound = new SoundMaster();
/*      */       
/*  274 */       AbstractCreature.initialize();
/*  275 */       AbstractCard.initialize();
/*  276 */       GameDictionary.initialize();
/*  277 */       ImageMaster.initialize();
/*  278 */       AbstractPower.initialize();
/*  279 */       FontHelper.initialize();
/*  280 */       AbstractCard.initializeDynamicFrameWidths();
/*  281 */       UnlockTracker.initialize();
/*  282 */       CardLibrary.initialize();
/*  283 */       RelicLibrary.initialize();
/*  284 */       InputHelper.initialize();
/*  285 */       TipTracker.initialize();
/*  286 */       ModHelper.initialize();
/*  287 */       ShaderHelper.initializeShaders();
/*  288 */       UnlockTracker.retroactiveUnlock();
/*  289 */       CInputHelper.loadSettings();
/*  290 */       clientUtils = new SteamUtils(this.clUtilsCallback);
/*  291 */       this.steamInputHelper = new SteamInputHelper();
/*      */       
/*  293 */       steelSeries = new SteelSeries();
/*  294 */       cursor = new GameCursor();
/*  295 */       metricData = new MetricData();
/*  296 */       cancelButton = new CancelButton();
/*  297 */       tips = new GameTips();
/*  298 */       characterManager = new CharacterManager();
/*  299 */       splashScreen = new SplashScreen();
/*  300 */       mode = GameMode.SPLASH;
/*  301 */     } catch (Exception e) {
/*      */       
/*  303 */       logger.info("Exception occurred in CardCrawlGame create method!");
/*  304 */       ExceptionHandler.handleException(e, logger);
/*  305 */       Gdx.app.exit();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void reloadPrefs() {
/*  310 */     playerPref = SaveHelper.getPrefs("STSPlayer");
/*  311 */     alias = playerPref.getString("alias", "");
/*      */     
/*  313 */     if (alias.equals("")) {
/*  314 */       alias = generateRandomAlias();
/*  315 */       playerPref.putString("alias", alias);
/*      */     } 
/*      */     
/*  318 */     music.fadeOutBGM();
/*  319 */     mainMenuScreen.fadeOutMusic();
/*      */     
/*  321 */     InputActionSet.prefs = SaveHelper.getPrefs("STSInputSettings");
/*  322 */     InputActionSet.load();
/*  323 */     CInputActionSet.prefs = SaveHelper.getPrefs("STSInputSettings_Controller");
/*  324 */     CInputActionSet.load();
/*  325 */     if (SteamInputHelper.numControllers == 1) {
/*  326 */       SteamInputHelper.initActions(SteamInputHelper.controllerHandles[0]);
/*      */     }
/*  328 */     characterManager = new CharacterManager();
/*  329 */     Settings.initialize(true);
/*  330 */     UnlockTracker.initialize();
/*  331 */     CardLibrary.resetForReload();
/*  332 */     CardLibrary.initialize();
/*  333 */     RelicLibrary.resetForReload();
/*  334 */     RelicLibrary.initialize();
/*  335 */     TipTracker.initialize();
/*  336 */     logger.info("TEXTURE COUNT: " + Texture.getNumManagedTextures());
/*      */ 
/*      */     
/*  339 */     screenColor.a = 0.0F;
/*  340 */     screenTime = 0.01F;
/*  341 */     screenTimer = 0.01F;
/*  342 */     fadeIn = false;
/*  343 */     startOver = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void saveMigration() {
/*  348 */     if (!SaveHelper.saveExists()) {
/*  349 */       Preferences p = Gdx.app.getPreferences("STSPlayer");
/*      */ 
/*      */       
/*  352 */       if (p.getString("name", "").equals("") && Gdx.app.getPreferences("STSDataVagabond").getLong("PLAYTIME") == 0L) {
/*      */         
/*  354 */         logger.info("New player, no migration.");
/*      */         return;
/*      */       } 
/*  357 */       logger.info("Migrating Save...");
/*  358 */       migrateHelper("STSPlayer");
/*  359 */       migrateHelper("STSUnlocks");
/*  360 */       migrateHelper("STSUnlockProgress");
/*  361 */       migrateHelper("STSTips");
/*  362 */       migrateHelper("STSSound");
/*  363 */       migrateHelper("STSSeenRelics");
/*  364 */       migrateHelper("STSSeenCards");
/*  365 */       migrateHelper("STSSeenBosses");
/*  366 */       migrateHelper("STSGameplaySettings");
/*  367 */       migrateHelper("STSDataVagabond");
/*  368 */       migrateHelper("STSDataTheSilent");
/*  369 */       migrateHelper("STSAchievements");
/*      */       
/*  371 */       if (MathUtils.randomBoolean(0.5F)) {
/*  372 */         logger.warn("Save Migration");
/*      */       }
/*      */     } else {
/*      */       
/*  376 */       logger.info("No migration");
/*      */     } 
/*      */   }
/*      */   
/*      */   public void migrateHelper(String file) {
/*  381 */     Preferences p = Gdx.app.getPreferences(file);
/*  382 */     Prefs p2 = SaveHelper.getPrefs(file);
/*      */     
/*  384 */     Map<String, ?> map = p.get();
/*  385 */     for (Map.Entry<String, ?> c : map.entrySet()) {
/*  386 */       p2.putString(c.getKey(), p.getString(c.getKey()));
/*      */     }
/*  388 */     p2.flush();
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
/*      */   public void render() {
/*      */     try {
/*  410 */       TimeHelper.update();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  415 */       if (Gdx.graphics.getRawDeltaTime() > 0.1F) {
/*      */         return;
/*      */       }
/*      */       
/*  419 */       if (!SteamInputHelper.alive) {
/*  420 */         CInputHelper.initializeIfAble();
/*      */       }
/*      */       
/*  423 */       update();
/*  424 */       this.sb.setProjectionMatrix(this.camera.combined);
/*  425 */       psb.setProjectionMatrix(this.camera.combined);
/*  426 */       Gdx.gl.glClear(16384);
/*  427 */       this.sb.begin();
/*  428 */       this.sb.setColor(Color.WHITE);
/*      */       
/*  430 */       switch (mode) {
/*      */         case IRONCLAD:
/*  432 */           splashScreen.render(this.sb);
/*      */           break;
/*      */         case THE_SILENT:
/*  435 */           mainMenuScreen.render(this.sb);
/*      */           break;
/*      */         case DEFECT:
/*  438 */           if (dungeonTransitionScreen != null) {
/*  439 */             dungeonTransitionScreen.render(this.sb); break;
/*  440 */           }  if (dungeon != null) {
/*  441 */             dungeon.render(this.sb);
/*      */           }
/*      */           break;
/*      */         
/*      */         case null:
/*      */           break;
/*      */         default:
/*  448 */           logger.info("Unknown Game Mode: " + mode.name());
/*      */           break;
/*      */       } 
/*      */       
/*  452 */       DrawMaster.draw(this.sb);
/*      */       
/*  454 */       if (cardPopup.isOpen) {
/*  455 */         cardPopup.render(this.sb);
/*      */       }
/*      */       
/*  458 */       if (relicPopup.isOpen) {
/*  459 */         relicPopup.render(this.sb);
/*      */       }
/*      */       
/*  462 */       TipHelper.render(this.sb);
/*      */       
/*  464 */       if (mode != GameMode.SPLASH) {
/*  465 */         renderBlackFadeScreen(this.sb);
/*  466 */         if (this.displayCursor) {
/*  467 */           if (isPopupOpen) {
/*  468 */             InputHelper.mX = popupMX;
/*  469 */             InputHelper.mY = popupMY;
/*      */           } 
/*  471 */           cursor.render(this.sb);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  476 */       if (Settings.HORIZ_LETTERBOX_AMT != 0) {
/*  477 */         this.sb.setColor(Color.BLACK);
/*  478 */         this.sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, -Settings.HORIZ_LETTERBOX_AMT);
/*  479 */         this.sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, Settings.HEIGHT, Settings.WIDTH, Settings.HORIZ_LETTERBOX_AMT);
/*  480 */       } else if (Settings.VERT_LETTERBOX_AMT != 0) {
/*  481 */         this.sb.setColor(Color.BLACK);
/*  482 */         this.sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, -Settings.VERT_LETTERBOX_AMT, Settings.HEIGHT);
/*  483 */         this.sb.draw(ImageMaster.WHITE_SQUARE_IMG, Settings.WIDTH, 0.0F, Settings.VERT_LETTERBOX_AMT, Settings.HEIGHT);
/*      */       } 
/*      */       
/*  486 */       this.sb.end();
/*  487 */     } catch (Exception e) {
/*      */       
/*  489 */       logger.info("Exception occurred in CardCrawlGame render method!");
/*  490 */       ExceptionHandler.handleException(e, logger);
/*  491 */       Gdx.app.exit();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderBlackFadeScreen(SpriteBatch sb) {
/*  496 */     sb.setColor(screenColor);
/*  497 */     if (screenColor.a < 0.55F && !mainMenuScreen.bg.activated) {
/*  498 */       mainMenuScreen.bg.activated = true;
/*      */     }
/*  500 */     sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
/*      */   }
/*      */   
/*      */   public void updateFade() {
/*  504 */     if (screenTimer != 0.0F) {
/*  505 */       screenTimer -= Gdx.graphics.getDeltaTime();
/*  506 */       if (screenTimer < 0.0F) {
/*  507 */         screenTimer = 0.0F;
/*      */       }
/*      */       
/*  510 */       if (fadeIn) {
/*  511 */         screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, 1.0F - screenTimer / screenTime);
/*      */       } else {
/*  513 */         screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, 1.0F - screenTimer / screenTime);
/*      */ 
/*      */         
/*  516 */         if (startOver && screenTimer == 0.0F) {
/*  517 */           if (AbstractDungeon.scene != null) {
/*  518 */             AbstractDungeon.scene.fadeOutAmbiance();
/*      */           }
/*      */           
/*  521 */           long startTime = System.currentTimeMillis();
/*  522 */           AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
/*  523 */           AbstractDungeon.reset();
/*  524 */           FontHelper.cardTitleFont.getData().setScale(1.0F);
/*  525 */           AbstractRelic.relicPage = 0;
/*  526 */           SeedPanel.textField = "";
/*  527 */           ModHelper.setModsFalse();
/*  528 */           SeedHelper.cachedSeed = null;
/*  529 */           Settings.seed = null;
/*  530 */           Settings.seedSet = false;
/*  531 */           Settings.specialSeed = null;
/*  532 */           Settings.isTrial = false;
/*  533 */           Settings.isDailyRun = false;
/*  534 */           Settings.isEndless = false;
/*  535 */           Settings.isFinalActAvailable = false;
/*  536 */           Settings.hasRubyKey = false;
/*  537 */           Settings.hasEmeraldKey = false;
/*  538 */           Settings.hasSapphireKey = false;
/*  539 */           CustomModeScreen.finalActAvailable = false;
/*  540 */           trial = null;
/*      */           
/*  542 */           logger.info("Dungeon Reset: " + (System.currentTimeMillis() - startTime) + "ms");
/*  543 */           startTime = System.currentTimeMillis();
/*      */           
/*  545 */           ShopScreen.resetPurgeCost();
/*  546 */           tips.initialize();
/*  547 */           metricData.clearData();
/*      */           
/*  549 */           logger.info("Shop Screen Rest, Tips Initialize, Metric Data Clear: " + (
/*  550 */               System.currentTimeMillis() - startTime) + "ms");
/*      */           
/*  552 */           startTime = System.currentTimeMillis();
/*      */           
/*  554 */           UnlockTracker.refresh();
/*      */           
/*  556 */           logger.info("Unlock Tracker Refresh:  " + (System.currentTimeMillis() - startTime) + "ms");
/*  557 */           startTime = System.currentTimeMillis();
/*      */           
/*  559 */           mainMenuScreen = new MainMenuScreen();
/*  560 */           mainMenuScreen.bg.slideDownInstantly();
/*      */ 
/*      */           
/*  563 */           saveSlotPref.putFloat(
/*  564 */               SaveHelper.slotName("COMPLETION", saveSlot), 
/*  565 */               UnlockTracker.getCompletionPercentage());
/*      */ 
/*      */           
/*  568 */           saveSlotPref.putLong(SaveHelper.slotName("PLAYTIME", saveSlot), UnlockTracker.getTotalPlaytime());
/*      */ 
/*      */           
/*  571 */           saveSlotPref.flush();
/*      */           
/*  573 */           logger.info("New Main Menu Screen: " + (System.currentTimeMillis() - startTime) + "ms");
/*  574 */           startTime = System.currentTimeMillis();
/*      */           
/*  576 */           CardHelper.clear();
/*  577 */           mode = GameMode.CHAR_SELECT;
/*  578 */           nextDungeon = "Exordium";
/*  579 */           dungeonTransitionScreen = new DungeonTransitionScreen("Exordium");
/*  580 */           TipTracker.refresh();
/*      */           
/*  582 */           logger.info("[GC] BEFORE: " + String.valueOf(SystemStats.getUsedMemory()));
/*  583 */           System.gc();
/*  584 */           logger.info("[GC] AFTER: " + String.valueOf(SystemStats.getUsedMemory()));
/*      */           
/*  586 */           logger.info("New Transition Screen, Tip Tracker Refresh: " + (
/*  587 */               System.currentTimeMillis() - startTime) + "ms");
/*      */           
/*  589 */           startTime = System.currentTimeMillis();
/*      */           
/*  591 */           fadeIn(2.0F);
/*      */ 
/*      */           
/*  594 */           if (queueCredits) {
/*  595 */             queueCredits = false;
/*  596 */             mainMenuScreen.creditsScreen.open(playCreditsBgm);
/*  597 */             mainMenuScreen.hideMenuButtons();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void fadeIn(float duration) {
/*  605 */     screenColor.a = 1.0F;
/*  606 */     screenTime = duration;
/*  607 */     screenTimer = duration;
/*  608 */     fadeIn = true;
/*      */   }
/*      */   
/*      */   public static void fadeToBlack(float duration) {
/*  612 */     screenColor.a = 0.0F;
/*  613 */     screenTime = duration;
/*  614 */     screenTimer = duration;
/*  615 */     fadeIn = false;
/*      */   }
/*      */   
/*      */   public static void startOver() {
/*  619 */     startOver = true;
/*  620 */     fadeToBlack(2.0F);
/*      */   }
/*      */   
/*      */   public static void startOverButShowCredits() {
/*  624 */     startOver = true;
/*  625 */     queueCredits = true;
/*  626 */     doorUnlockScreenCheck();
/*  627 */     fadeToBlack(2.0F);
/*      */   }
/*      */   
/*      */   private static void doorUnlockScreenCheck() {
/*  631 */     DoorUnlockScreen.show = false;
/*      */     
/*  633 */     if (!Settings.isStandardRun()) {
/*  634 */       logger.info("[INFO] Non-Standard Run, no check for door.");
/*      */       
/*      */       return;
/*      */     } 
/*  638 */     switch (AbstractDungeon.player.chosenClass) {
/*      */       case IRONCLAD:
/*  640 */         if (!playerPref.getBoolean(AbstractDungeon.player.chosenClass.name() + "_WIN", false)) {
/*  641 */           logger.info("[INFO] Ironclad Victory: Show Door.");
/*  642 */           playerPref.putBoolean(AbstractDungeon.player.chosenClass.name() + "_WIN", true);
/*  643 */           DoorUnlockScreen.show = true; break;
/*      */         } 
/*  645 */         logger.info("[INFO] Ironclad Already Won: No Door.");
/*      */         break;
/*      */       
/*      */       case THE_SILENT:
/*  649 */         if (!playerPref.getBoolean(AbstractDungeon.player.chosenClass.name() + "_WIN", false)) {
/*  650 */           logger.info("[INFO] Silent Victory: Show Door.");
/*  651 */           playerPref.putBoolean(AbstractDungeon.player.chosenClass.name() + "_WIN", true);
/*  652 */           DoorUnlockScreen.show = true; break;
/*      */         } 
/*  654 */         logger.info("[INFO] Silent Already Won: No Door.");
/*      */         break;
/*      */       
/*      */       case DEFECT:
/*  658 */         if (!playerPref.getBoolean(AbstractDungeon.player.chosenClass.name() + "_WIN", false)) {
/*  659 */           logger.info("[INFO] Defect Victory: Show Door.");
/*  660 */           playerPref.putBoolean(AbstractDungeon.player.chosenClass.name() + "_WIN", true);
/*  661 */           DoorUnlockScreen.show = true; break;
/*      */         } 
/*  663 */         logger.info("[INFO] Defect Already Won: No Door.");
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  671 */     if (DoorUnlockScreen.show) {
/*  672 */       playerPref.flush();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void resetScoreVars() {
/*  717 */     monstersSlain = 0;
/*  718 */     elites1Slain = 0;
/*  719 */     elites2Slain = 0;
/*  720 */     elites3Slain = 0;
/*  721 */     if (dungeon != null) {
/*  722 */       AbstractDungeon.bossCount = 0;
/*      */     }
/*  724 */     champion = 0;
/*  725 */     perfect = 0;
/*  726 */     overkill = false;
/*  727 */     combo = false;
/*  728 */     goldGained = 0;
/*  729 */     cardsPurged = 0;
/*  730 */     potionsBought = 0;
/*  731 */     mysteryMachine = 0;
/*  732 */     playtime = 0.0F;
/*  733 */     stopClock = false;
/*      */   }
/*      */   
/*      */   public void update() {
/*  737 */     cursor.update();
/*  738 */     screenShake.update(viewport);
/*  739 */     if (mode != GameMode.SPLASH) {
/*  740 */       updateFade();
/*      */     }
/*  742 */     music.update();
/*  743 */     sound.update();
/*  744 */     if (steelSeries.isEnabled.booleanValue()) {
/*  745 */       steelSeries.update();
/*      */     }
/*      */     
/*  748 */     if (Settings.isDebug) {
/*  749 */       if (DevInputActionSet.toggleCursor.isJustPressed()) {
/*  750 */         this.displayCursor = !this.displayCursor;
/*  751 */       } else if (DevInputActionSet.toggleVersion.isJustPressed()) {
/*  752 */         displayVersion = !displayVersion;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  757 */     if (SteamInputHelper.numControllers == 1) {
/*  758 */       SteamInputHelper.updateFirst();
/*  759 */     } else if (SteamInputHelper.numControllers == 999 && CInputHelper.controllers == null) {
/*  760 */       CInputHelper.initializeIfAble();
/*      */     } 
/*      */     
/*  763 */     InputHelper.updateFirst();
/*      */     
/*  765 */     if (cardPopup.isOpen) {
/*  766 */       cardPopup.update();
/*      */     }
/*  768 */     if (relicPopup.isOpen) {
/*  769 */       relicPopup.update();
/*      */     }
/*      */     
/*  772 */     if (isPopupOpen) {
/*  773 */       popupMX = InputHelper.mX;
/*  774 */       popupMY = InputHelper.mY;
/*  775 */       InputHelper.mX = -9999;
/*  776 */       InputHelper.mY = -9999;
/*      */     } 
/*      */     
/*  779 */     switch (mode) {
/*      */       case IRONCLAD:
/*  781 */         splashScreen.update();
/*  782 */         if (splashScreen.isDone) {
/*  783 */           mode = GameMode.CHAR_SELECT;
/*  784 */           splashScreen = null;
/*  785 */           mainMenuScreen = new MainMenuScreen();
/*      */         } 
/*      */         break;
/*      */       case THE_SILENT:
/*  789 */         mainMenuScreen.update();
/*  790 */         if (mainMenuScreen.fadedOut) {
/*  791 */           AbstractDungeon.pathX = new ArrayList();
/*  792 */           AbstractDungeon.pathY = new ArrayList();
/*      */           
/*  794 */           if (trial == null && Settings.specialSeed != null) {
/*  795 */             trial = TrialHelper.getTrialForSeed(SeedHelper.getString(Settings.specialSeed.longValue()));
/*      */           }
/*      */           
/*  798 */           if (loadingSave) {
/*  799 */             ModHelper.setModsFalse();
/*  800 */             AbstractDungeon.player = createCharacter(chosenCharacter);
/*  801 */             loadPlayerSave(AbstractDungeon.player);
/*      */           } else {
/*  803 */             Settings.setFinalActAvailability();
/*  804 */             logger.info("FINAL ACT AVAILABLE: " + Settings.isFinalActAvailable);
/*      */ 
/*      */             
/*  807 */             if (trial == null) {
/*  808 */               if (Settings.isDailyRun) {
/*  809 */                 AbstractDungeon.ascensionLevel = 0;
/*  810 */                 AbstractDungeon.isAscensionMode = false;
/*      */               } 
/*      */               
/*  813 */               AbstractDungeon.player = createCharacter(chosenCharacter);
/*      */               
/*  815 */               for (AbstractRelic r : AbstractDungeon.player.relics) {
/*  816 */                 r.updateDescription(AbstractDungeon.player.chosenClass);
/*  817 */                 r.onEquip();
/*      */               } 
/*      */ 
/*      */               
/*  821 */               for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
/*  822 */                 if (c.rarity != AbstractCard.CardRarity.BASIC) {
/*  823 */                   CardHelper.obtain(c.cardID, c.rarity, c.color);
/*      */                 }
/*      */               } 
/*      */             } else {
/*      */               
/*  828 */               Settings.isTrial = true;
/*  829 */               Settings.isDailyRun = false;
/*      */ 
/*      */               
/*  832 */               setupTrialMods(trial, chosenCharacter);
/*  833 */               setupTrialPlayer(trial);
/*      */             } 
/*      */           } 
/*  836 */           mode = GameMode.GAMEPLAY;
/*  837 */           nextDungeon = "Exordium";
/*  838 */           dungeonTransitionScreen = new DungeonTransitionScreen("Exordium");
/*      */           
/*  840 */           if (loadingSave) {
/*  841 */             dungeonTransitionScreen.isComplete = true;
/*      */             break;
/*      */           } 
/*  844 */           monstersSlain = 0;
/*  845 */           elites1Slain = 0;
/*  846 */           elites2Slain = 0;
/*  847 */           elites3Slain = 0;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case DEFECT:
/*  852 */         if (dungeonTransitionScreen != null) {
/*  853 */           dungeonTransitionScreen.update();
/*      */           
/*  855 */           if (dungeonTransitionScreen.isComplete) {
/*  856 */             dungeonTransitionScreen = null;
/*      */ 
/*      */             
/*  859 */             if (loadingSave) {
/*  860 */               getDungeon(saveFile.level_name, AbstractDungeon.player, saveFile);
/*  861 */               loadPostCombat(saveFile);
/*  862 */               if (!saveFile.post_combat) {
/*  863 */                 loadingSave = false;
/*      */               }
/*      */             } else {
/*  866 */               getDungeon(nextDungeon, AbstractDungeon.player);
/*      */               
/*  868 */               if (!nextDungeon.equals("Exordium") || Settings.isShowBuild || !((Boolean)TipTracker.tips.get("NEOW_SKIP")).booleanValue()) {
/*      */                 
/*  870 */                 AbstractDungeon.dungeonMapScreen.open(true);
/*  871 */                 TipTracker.neverShowAgain("NEOW_SKIP");
/*      */               } 
/*      */             } 
/*      */           } 
/*  875 */         } else if (dungeon != null) {
/*  876 */           dungeon.update();
/*      */         } else {
/*  878 */           logger.info("Eh-?");
/*      */         } 
/*      */ 
/*      */         
/*  882 */         if (dungeon != null && AbstractDungeon.isDungeonBeaten && AbstractDungeon.fadeColor.a == 1.0F) {
/*  883 */           dungeon = null;
/*  884 */           AbstractDungeon.scene.fadeOutAmbiance();
/*  885 */           dungeonTransitionScreen = new DungeonTransitionScreen(nextDungeon);
/*      */         } 
/*      */         break;
/*      */       
/*      */       case null:
/*      */         break;
/*      */       default:
/*  892 */         logger.info("Unknown Game Mode: " + mode.name());
/*      */         break;
/*      */     } 
/*      */     
/*  896 */     updateDebugSwitch();
/*  897 */     InputHelper.updateLast();
/*      */     
/*  899 */     if (CInputHelper.controller != null) {
/*  900 */       CInputHelper.updateLast();
/*      */     }
/*      */     
/*  903 */     if (Settings.isInfo) {
/*  904 */       this.fpsLogger.log();
/*      */     }
/*      */   }
/*      */   
/*      */   private void setupTrialMods(AbstractTrial trial, AbstractPlayer.PlayerClass chosenClass) {
/*  909 */     if (trial.useRandomDailyMods()) {
/*  910 */       long sourceTime = System.nanoTime();
/*  911 */       Random rng = new Random(Long.valueOf(sourceTime));
/*  912 */       Settings.seed = Long.valueOf(SeedHelper.generateUnoffensiveSeed(rng));
/*  913 */       ModHelper.setTodaysMods(Settings.seed.longValue(), chosenClass);
/*      */     }
/*  915 */     else if (trial.dailyModIDs() != null) {
/*  916 */       ModHelper.setMods(trial.dailyModIDs());
/*  917 */       ModHelper.clearNulls();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setupTrialPlayer(AbstractTrial trial) {
/*  923 */     AbstractDungeon.player = trial.setupPlayer(createCharacter(chosenCharacter));
/*      */ 
/*      */     
/*  926 */     if (!trial.keepStarterRelic()) {
/*  927 */       AbstractDungeon.player.relics.clear();
/*      */     }
/*  929 */     for (String relicID : trial.extraStartingRelicIDs()) {
/*  930 */       AbstractRelic relic = RelicLibrary.getRelic(relicID);
/*  931 */       relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), false);
/*  932 */       AbstractDungeon.relicsToRemoveOnStart.add(relic.relicId);
/*      */     } 
/*      */     
/*  935 */     for (AbstractRelic r : AbstractDungeon.player.relics) {
/*  936 */       r.updateDescription(AbstractDungeon.player.chosenClass);
/*  937 */       r.onEquip();
/*      */     } 
/*      */ 
/*      */     
/*  941 */     if (!trial.keepsStarterCards()) {
/*  942 */       AbstractDungeon.player.masterDeck.clear();
/*      */     }
/*  944 */     for (String cardID : trial.extraStartingCardIDs()) {
/*  945 */       AbstractDungeon.player.masterDeck.addToTop(CardLibrary.getCard(cardID).makeCopy());
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadPostCombat(SaveFile saveFile) {
/*  950 */     if (saveFile.post_combat) {
/*  951 */       (AbstractDungeon.getCurrRoom()).isBattleOver = true;
/*  952 */       AbstractDungeon.overlayMenu.hideCombatPanels();
/*  953 */       AbstractDungeon.loading_post_combat = true;
/*  954 */       (AbstractDungeon.getCurrRoom()).smoked = saveFile.smoked;
/*  955 */       (AbstractDungeon.getCurrRoom()).mugged = saveFile.mugged;
/*      */       
/*  957 */       if ((AbstractDungeon.getCurrRoom()).event != null) {
/*  958 */         (AbstractDungeon.getCurrRoom()).event.postCombatLoad();
/*      */       }
/*      */       
/*  961 */       if ((AbstractDungeon.getCurrRoom()).monsters != null) {
/*  962 */         (AbstractDungeon.getCurrRoom()).monsters.monsters.clear();
/*  963 */         AbstractDungeon.actionManager.actions.clear();
/*      */       } 
/*      */       
/*  966 */       if (!saveFile.smoked) {
/*  967 */         for (RewardSave i : saveFile.combat_rewards) {
/*  968 */           switch (i.type) {
/*      */             case "CARD":
/*      */               continue;
/*      */             
/*      */             case "GOLD":
/*  973 */               AbstractDungeon.getCurrRoom().addGoldToRewards(i.amount);
/*      */               continue;
/*      */             case "RELIC":
/*  976 */               AbstractDungeon.getCurrRoom().addRelicToRewards(RelicLibrary.getRelic(i.id).makeCopy());
/*      */               continue;
/*      */             case "POTION":
/*  979 */               AbstractDungeon.getCurrRoom().addPotionToRewards(PotionHelper.getPotion(i.id));
/*      */               continue;
/*      */             case "STOLEN_GOLD":
/*  982 */               AbstractDungeon.getCurrRoom().addStolenGoldToRewards(i.amount);
/*      */               continue;
/*      */             case "SAPPHIRE_KEY":
/*  985 */               AbstractDungeon.getCurrRoom().addSapphireKey(
/*  986 */                   (AbstractDungeon.getCurrRoom()).rewards.get(
/*  987 */                     (AbstractDungeon.getCurrRoom()).rewards.size() - 1));
/*      */               continue;
/*      */             case "EMERALD_KEY":
/*  990 */               (AbstractDungeon.getCurrRoom()).rewards.add(new RewardItem(
/*      */                     
/*  992 */                     (AbstractDungeon.getCurrRoom()).rewards.get(
/*  993 */                       (AbstractDungeon.getCurrRoom()).rewards.size() - 1), RewardItem.RewardType.EMERALD_KEY));
/*      */               continue;
/*      */           } 
/*      */           
/*  997 */           logger.info("Loading unknown type: " + i.type);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1003 */       if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss) {
/* 1004 */         AbstractDungeon.scene.fadeInAmbiance();
/* 1005 */         music.silenceTempBgmInstantly();
/* 1006 */         music.silenceBGMInstantly();
/* 1007 */         AbstractMonster.playBossStinger();
/* 1008 */       } else if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomElite) {
/* 1009 */         AbstractDungeon.scene.fadeInAmbiance();
/* 1010 */         music.fadeOutTempBGM();
/*      */       } 
/* 1012 */       saveFile.post_combat = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void loadPlayerSave(AbstractPlayer p) {
/* 1017 */     saveFile = SaveAndContinue.loadSaveFile(p.chosenClass);
/* 1018 */     AbstractDungeon.loading_post_combat = false;
/*      */ 
/*      */     
/* 1021 */     Settings.seed = Long.valueOf(saveFile.seed);
/* 1022 */     Settings.isFinalActAvailable = saveFile.is_final_act_on;
/* 1023 */     Settings.hasRubyKey = saveFile.has_ruby_key;
/* 1024 */     Settings.hasEmeraldKey = saveFile.has_emerald_key;
/* 1025 */     Settings.hasSapphireKey = saveFile.has_sapphire_key;
/* 1026 */     Settings.isDailyRun = saveFile.is_daily;
/* 1027 */     if (Settings.isDailyRun) {
/* 1028 */       Settings.dailyDate = saveFile.daily_date;
/*      */     }
/* 1030 */     Settings.specialSeed = Long.valueOf(saveFile.special_seed);
/* 1031 */     Settings.seedSet = saveFile.seed_set;
/* 1032 */     Settings.isTrial = saveFile.is_trial;
/*      */     
/* 1034 */     if (Settings.isTrial) {
/* 1035 */       ModHelper.setTodaysMods(Settings.seed.longValue(), AbstractDungeon.player.chosenClass);
/* 1036 */       AbstractPlayer.customMods = saveFile.custom_mods;
/* 1037 */     } else if (Settings.isDailyRun) {
/* 1038 */       ModHelper.setTodaysMods(Settings.specialSeed.longValue(), AbstractDungeon.player.chosenClass);
/*      */     } 
/*      */     
/* 1041 */     AbstractPlayer.customMods = saveFile.custom_mods;
/*      */     
/* 1043 */     if (AbstractPlayer.customMods == null) {
/* 1044 */       AbstractPlayer.customMods = new ArrayList();
/*      */     }
/*      */ 
/*      */     
/* 1048 */     p.currentHealth = saveFile.current_health;
/* 1049 */     p.maxHealth = saveFile.max_health;
/* 1050 */     p.gold = saveFile.gold;
/* 1051 */     p.displayGold = p.gold;
/* 1052 */     p.masterHandSize = saveFile.hand_size;
/* 1053 */     p.potionSlots = saveFile.potion_slots;
/* 1054 */     if (p.potionSlots == 0) {
/* 1055 */       p.potionSlots = 3;
/*      */     }
/* 1057 */     p.potions.clear();
/* 1058 */     for (int i = 0; i < p.potionSlots; i++) {
/* 1059 */       p.potions.add(new PotionSlot(i));
/*      */     }
/* 1061 */     p.masterMaxOrbs = saveFile.max_orbs;
/* 1062 */     p.energy = new EnergyManager(saveFile.red + saveFile.green + saveFile.blue);
/*      */ 
/*      */     
/* 1065 */     monstersSlain = saveFile.monsters_killed;
/* 1066 */     elites1Slain = saveFile.elites1_killed;
/* 1067 */     elites2Slain = saveFile.elites2_killed;
/* 1068 */     elites3Slain = saveFile.elites3_killed;
/* 1069 */     goldGained = saveFile.gold_gained;
/* 1070 */     champion = saveFile.champions;
/* 1071 */     perfect = saveFile.perfect;
/* 1072 */     combo = saveFile.combo;
/* 1073 */     overkill = saveFile.overkill;
/* 1074 */     mysteryMachine = saveFile.mystery_machine;
/* 1075 */     playtime = (float)saveFile.play_time;
/* 1076 */     AbstractDungeon.ascensionLevel = saveFile.ascension_level;
/* 1077 */     AbstractDungeon.isAscensionMode = saveFile.is_ascension_mode;
/*      */ 
/*      */     
/* 1080 */     p.masterDeck.clear();
/* 1081 */     for (CardSave s : saveFile.cards) {
/* 1082 */       logger.info(s.id + ", " + s.upgrades);
/* 1083 */       p.masterDeck.addToTop(CardLibrary.getCopy(s.id, s.upgrades, s.misc));
/*      */     } 
/*      */ 
/*      */     
/* 1087 */     Settings.isEndless = saveFile.is_endless_mode;
/*      */     
/* 1089 */     int index = 0;
/* 1090 */     p.blights.clear();
/*      */     
/* 1092 */     if (saveFile.blights != null) {
/* 1093 */       for (String b : saveFile.blights) {
/*      */         
/* 1095 */         AbstractBlight blight = BlightHelper.getBlight(b);
/* 1096 */         if (blight != null) {
/*      */           
/* 1098 */           int incrementAmount = ((Integer)saveFile.endless_increments.get(index)).intValue();
/* 1099 */           for (int j = 0; j < incrementAmount; j++) {
/* 1100 */             blight.incrementUp();
/*      */           }
/* 1102 */           blight.setIncrement(incrementAmount);
/* 1103 */           blight.instantObtain(AbstractDungeon.player, index, false);
/*      */         } 
/* 1105 */         index++;
/*      */       } 
/*      */       
/* 1108 */       if (saveFile.blight_counters != null) {
/*      */         
/* 1110 */         index = 0;
/* 1111 */         for (Integer integer : saveFile.blight_counters) {
/* 1112 */           ((AbstractBlight)p.blights.get(index)).setCounter(integer.intValue());
/* 1113 */           ((AbstractBlight)p.blights.get(index)).updateDescription(p.chosenClass);
/* 1114 */           index++;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1120 */     p.relics.clear();
/* 1121 */     index = 0;
/* 1122 */     for (String s : saveFile.relics) {
/* 1123 */       AbstractRelic r = RelicLibrary.getRelic(s).makeCopy();
/* 1124 */       r.instantObtain(p, index, false);
/* 1125 */       if (index < saveFile.relic_counters.size()) {
/* 1126 */         r.setCounter(((Integer)saveFile.relic_counters.get(index)).intValue());
/*      */       }
/* 1128 */       r.updateDescription(p.chosenClass);
/* 1129 */       index++;
/*      */     } 
/*      */ 
/*      */     
/* 1133 */     index = 0;
/* 1134 */     for (String s : saveFile.potions) {
/* 1135 */       AbstractPotion potion = PotionHelper.getPotion(s);
/* 1136 */       if (potion != null) {
/* 1137 */         AbstractDungeon.player.obtainPotion(index, potion);
/*      */       }
/* 1139 */       index++;
/*      */     } 
/*      */ 
/*      */     
/* 1143 */     AbstractCard tmpCard = null;
/* 1144 */     if (saveFile.bottled_flame != null) {
/* 1145 */       for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.group) {
/* 1146 */         if (abstractCard.cardID.equals(saveFile.bottled_flame)) {
/* 1147 */           tmpCard = abstractCard;
/* 1148 */           if (abstractCard.timesUpgraded == saveFile.bottled_flame_upgrade && abstractCard.misc == saveFile.bottled_flame_misc) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1154 */       if (tmpCard != null) {
/* 1155 */         tmpCard.inBottleFlame = true;
/* 1156 */         ((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).card = tmpCard;
/* 1157 */         ((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).setDescriptionAfterLoading();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1162 */     tmpCard = null;
/* 1163 */     if (saveFile.bottled_lightning != null) {
/* 1164 */       for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.group) {
/* 1165 */         if (abstractCard.cardID.equals(saveFile.bottled_lightning)) {
/* 1166 */           tmpCard = abstractCard;
/* 1167 */           if (abstractCard.timesUpgraded == saveFile.bottled_lightning_upgrade && abstractCard.misc == saveFile.bottled_lightning_misc) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1174 */       if (tmpCard != null) {
/* 1175 */         tmpCard.inBottleLightning = true;
/* 1176 */         ((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).card = tmpCard;
/* 1177 */         ((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).setDescriptionAfterLoading();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1182 */     tmpCard = null;
/* 1183 */     if (saveFile.bottled_tornado != null) {
/* 1184 */       for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.group) {
/* 1185 */         if (abstractCard.cardID.equals(saveFile.bottled_tornado)) {
/* 1186 */           tmpCard = abstractCard;
/* 1187 */           if (abstractCard.timesUpgraded == saveFile.bottled_tornado_upgrade && abstractCard.misc == saveFile.bottled_tornado_misc) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1194 */       if (tmpCard != null) {
/* 1195 */         tmpCard.inBottleTornado = true;
/* 1196 */         ((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).card = tmpCard;
/* 1197 */         ((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).setDescriptionAfterLoading();
/*      */       } 
/*      */     } 
/*      */     
/* 1201 */     if (saveFile.daily_mods != null && saveFile.daily_mods.size() > 0) {
/* 1202 */       ModHelper.setMods(saveFile.daily_mods);
/*      */     }
/*      */ 
/*      */     
/* 1206 */     metricData.clearData();
/* 1207 */     metricData.campfire_rested = saveFile.metric_campfire_rested;
/* 1208 */     metricData.campfire_upgraded = saveFile.metric_campfire_upgraded;
/* 1209 */     metricData.purchased_purges = saveFile.metric_purchased_purges;
/* 1210 */     metricData.potions_floor_spawned = saveFile.metric_potions_floor_spawned;
/* 1211 */     metricData.current_hp_per_floor = saveFile.metric_current_hp_per_floor;
/* 1212 */     metricData.max_hp_per_floor = saveFile.metric_max_hp_per_floor;
/* 1213 */     metricData.gold_per_floor = saveFile.metric_gold_per_floor;
/* 1214 */     metricData.path_per_floor = saveFile.metric_path_per_floor;
/* 1215 */     metricData.path_taken = saveFile.metric_path_taken;
/* 1216 */     metricData.items_purchased = saveFile.metric_items_purchased;
/* 1217 */     metricData.items_purged = saveFile.metric_items_purged;
/* 1218 */     metricData.card_choices = saveFile.metric_card_choices;
/* 1219 */     metricData.event_choices = saveFile.metric_event_choices;
/* 1220 */     metricData.damage_taken = saveFile.metric_damage_taken;
/* 1221 */     metricData.boss_relics = saveFile.metric_boss_relics;
/*      */ 
/*      */     
/* 1224 */     if (saveFile.metric_potions_obtained != null) {
/* 1225 */       metricData.potions_obtained = saveFile.metric_potions_obtained;
/*      */     }
/* 1227 */     if (saveFile.metric_relics_obtained != null) {
/* 1228 */       metricData.relics_obtained = saveFile.metric_relics_obtained;
/*      */     }
/* 1230 */     if (saveFile.metric_campfire_choices != null) {
/* 1231 */       metricData.campfire_choices = saveFile.metric_campfire_choices;
/*      */     }
/* 1233 */     if (saveFile.metric_item_purchase_floors != null) {
/* 1234 */       metricData.item_purchase_floors = saveFile.metric_item_purchase_floors;
/*      */     }
/* 1236 */     if (saveFile.metric_items_purged_floors != null) {
/* 1237 */       metricData.items_purged_floors = saveFile.metric_items_purged_floors;
/*      */     }
/* 1239 */     if (saveFile.neow_bonus != null) {
/* 1240 */       metricData.neowBonus = saveFile.neow_bonus;
/*      */     }
/* 1242 */     if (saveFile.neow_cost != null) {
/* 1243 */       metricData.neowCost = saveFile.neow_cost;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static AbstractPlayer createCharacter(AbstractPlayer.PlayerClass selection) {
/* 1252 */     AbstractPlayer p = characterManager.recreateCharacter(selection);
/* 1253 */     for (AbstractCard c : p.masterDeck.group) {
/* 1254 */       UnlockTracker.markCardAsSeen(c.cardID);
/*      */     }
/* 1256 */     return p;
/*      */   }
/*      */   
/*      */   private void updateDebugSwitch() {
/* 1260 */     if (!Settings.isDev) {
/*      */       return;
/*      */     }
/*      */     
/* 1264 */     if (DevInputActionSet.toggleDebug.isJustPressed()) {
/* 1265 */       Settings.isDebug = !Settings.isDebug;
/*      */       
/*      */       return;
/*      */     } 
/* 1269 */     if (DevInputActionSet.toggleInfo.isJustPressed()) {
/* 1270 */       Settings.isInfo = !Settings.isInfo;
/*      */       
/*      */       return;
/*      */     } 
/* 1274 */     if (Settings.isDebug && DevInputActionSet.uploadData.isJustPressed()) {
/* 1275 */       RelicLibrary.uploadRelicData();
/* 1276 */       CardLibrary.uploadCardData();
/* 1277 */       MonsterHelper.uploadEnemyData();
/* 1278 */       PotionHelper.uploadPotionData();
/* 1279 */       ModHelper.uploadModData();
/* 1280 */       BlightHelper.uploadBlightData();
/* 1281 */       BotDataUploader.uploadKeywordData();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1287 */     if (!Settings.isDebug) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1293 */     if (DevInputActionSet.hideTopBar.isJustPressed()) {
/* 1294 */       Settings.hideTopBar = !Settings.hideTopBar;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1299 */     if (DevInputActionSet.hidePopUps.isJustPressed()) {
/* 1300 */       Settings.hidePopupDetails = !Settings.hidePopupDetails;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1305 */     if (DevInputActionSet.hideRelics.isJustPressed()) {
/* 1306 */       Settings.hideRelics = !Settings.hideRelics;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1311 */     if (DevInputActionSet.hideCombatLowUI.isJustPressed()) {
/* 1312 */       Settings.hideLowerElements = !Settings.hideLowerElements;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1317 */     if (DevInputActionSet.hideCards.isJustPressed()) {
/* 1318 */       Settings.hideCards = !Settings.hideCards;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1323 */     if (DevInputActionSet.hideEndTurnButton.isJustPressed()) {
/* 1324 */       Settings.hideEndTurn = !Settings.hideEndTurn;
/* 1325 */       if (AbstractDungeon.getMonsters() == null) {
/*      */         return;
/*      */       }
/* 1328 */       for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
/* 1329 */         m.damage(new DamageInfo((AbstractCreature)AbstractDungeon.player, m.currentHealth, DamageInfo.DamageType.HP_LOSS));
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1335 */     if (DevInputActionSet.hideCombatInfo.isJustPressed()) {
/* 1336 */       Settings.hideCombatElements = !Settings.hideCombatElements;
/*      */     }
/*      */   }
/*      */   
/*      */   public void resize(int width, int height) {}
/*      */   
/*      */   public AbstractDungeon getDungeon(String key, AbstractPlayer p) {
/*      */     ArrayList<String> emptyList;
/* 1344 */     switch (key) {
/*      */       case "Exordium":
/* 1346 */         emptyList = new ArrayList<>();
/* 1347 */         return (AbstractDungeon)new Exordium(p, emptyList);
/*      */       case "TheCity":
/* 1349 */         return (AbstractDungeon)new TheCity(p, AbstractDungeon.specialOneTimeEventList);
/*      */       case "TheBeyond":
/* 1351 */         return (AbstractDungeon)new TheBeyond(p, AbstractDungeon.specialOneTimeEventList);
/*      */       case "TheEnding":
/* 1353 */         return (AbstractDungeon)new TheEnding(p, AbstractDungeon.specialOneTimeEventList);
/*      */     } 
/* 1355 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public AbstractDungeon getDungeon(String key, AbstractPlayer p, SaveFile saveFile) {
/* 1360 */     switch (key) {
/*      */       case "Exordium":
/* 1362 */         return (AbstractDungeon)new Exordium(p, saveFile);
/*      */       case "TheCity":
/* 1364 */         return (AbstractDungeon)new TheCity(p, saveFile);
/*      */       case "TheBeyond":
/* 1366 */         return (AbstractDungeon)new TheBeyond(p, saveFile);
/*      */       case "TheEnding":
/* 1368 */         return (AbstractDungeon)new TheEnding(p, saveFile);
/*      */     } 
/* 1370 */     return null;
/*      */   }
/*      */   
/*      */   public enum GameMode
/*      */   {
/* 1375 */     CHAR_SELECT, GAMEPLAY, DUNGEON_TRANSITION, SPLASH;
/*      */   }
/*      */ 
/*      */   
/*      */   public void pause() {
/* 1380 */     logger.info("PAUSE()");
/* 1381 */     Settings.isControllerMode = false;
/*      */     
/* 1383 */     if (MUTE_IF_BG && 
/* 1384 */       mainMenuScreen != null) {
/* 1385 */       Settings.isBackgrounded = true;
/* 1386 */       if (mode == GameMode.CHAR_SELECT) {
/* 1387 */         mainMenuScreen.muteAmbienceVolume();
/* 1388 */       } else if (AbstractDungeon.scene != null) {
/* 1389 */         AbstractDungeon.scene.muteAmbienceVolume();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void resume() {
/* 1397 */     logger.info("RESUME()");
/* 1398 */     if (MUTE_IF_BG && 
/* 1399 */       mainMenuScreen != null) {
/* 1400 */       Settings.isBackgrounded = false;
/* 1401 */       if (mode == GameMode.CHAR_SELECT) {
/* 1402 */         mainMenuScreen.updateAmbienceVolume();
/* 1403 */       } else if (AbstractDungeon.scene != null) {
/* 1404 */         AbstractDungeon.scene.updateAmbienceVolume();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void dispose() {
/* 1412 */     logger.info("Game shutting down...");
/* 1413 */     logger.info("Flushing saves to disk...");
/* 1414 */     AsyncSaver.shutdownSaveThread();
/*      */     
/* 1416 */     if (SteamInputHelper.alive) {
/* 1417 */       logger.info("Shutting down controller handler...");
/* 1418 */       SteamInputHelper.alive = false;
/* 1419 */       SteamInputHelper.controller.shutdown();
/*      */       
/* 1421 */       if (clientUtils != null) {
/* 1422 */         clientUtils.dispose();
/*      */       }
/*      */     } 
/*      */     
/* 1426 */     if (sInputDetectThread != null) {
/* 1427 */       logger.info("Steam input detection was running! Shutting down...");
/* 1428 */       sInputDetectThread.interrupt();
/*      */     } 
/*      */     
/* 1431 */     logger.info("Shutting down publisher integrations...");
/* 1432 */     publisherIntegration.dispose();
/* 1433 */     logger.info("Flushing logs to disk. Clean shutdown successful.");
/* 1434 */     LogManager.shutdown();
/*      */   }
/*      */   
/*      */   public static String generateRandomAlias() {
/* 1438 */     String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
/* 1439 */     StringBuilder retVal = new StringBuilder();
/* 1440 */     for (int i = 0; i < 16; i++) {
/* 1441 */       retVal.append(alphabet.charAt(MathUtils.random(0, alphabet.length() - 1)));
/*      */     }
/* 1443 */     return retVal.toString();
/*      */   }
/*      */   
/*      */   public static boolean isInARun() {
/* 1447 */     return (mode == GameMode.GAMEPLAY && AbstractDungeon.player != null && !AbstractDungeon.player.isDead);
/*      */   }
/*      */   
/*      */   public static Texture getSaveSlotImg() {
/* 1451 */     switch (saveSlot) {
/*      */       case 0:
/* 1453 */         return ImageMaster.PROFILE_A;
/*      */       case 1:
/* 1455 */         return ImageMaster.PROFILE_B;
/*      */       case 2:
/* 1457 */         return ImageMaster.PROFILE_C;
/*      */     } 
/* 1459 */     return ImageMaster.PROFILE_A;
/*      */   }
/*      */   
/*      */   public CardCrawlGame(String prefDir) {
/* 1463 */     this.clUtilsCallback = new SteamUtilsCallback()
/*      */       {
/*      */         public void onSteamShutdown() {
/* 1466 */           CardCrawlGame.logger.error("Steam client requested to shut down!");
/*      */         }
/*      */       };
/*      */     preferenceDir = prefDir;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\core\CardCrawlGame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */