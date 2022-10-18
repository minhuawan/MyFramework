/*      */ package com.megacrit.cardcrawl.screens.runHistory;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*      */ import com.google.gson.Gson;
/*      */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*      */ import com.megacrit.cardcrawl.cards.CardGroup;
/*      */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*      */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*      */ import com.megacrit.cardcrawl.core.GameCursor;
/*      */ import com.megacrit.cardcrawl.core.Settings;
/*      */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*      */ import com.megacrit.cardcrawl.helpers.CardLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.FontHelper;
/*      */ import com.megacrit.cardcrawl.helpers.Hitbox;
/*      */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*      */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*      */ import com.megacrit.cardcrawl.helpers.MonsterHelper;
/*      */ import com.megacrit.cardcrawl.helpers.RelicLibrary;
/*      */ import com.megacrit.cardcrawl.helpers.SeedHelper;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputActionSet;
/*      */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*      */ import com.megacrit.cardcrawl.localization.UIStrings;
/*      */ import com.megacrit.cardcrawl.metrics.Metrics;
/*      */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*      */ import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
/*      */ import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
/*      */ import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
/*      */ import com.megacrit.cardcrawl.screens.options.DropdownMenu;
/*      */ import com.megacrit.cardcrawl.screens.options.DropdownMenuListener;
/*      */ import com.megacrit.cardcrawl.screens.stats.CharStat;
/*      */ import com.megacrit.cardcrawl.screens.stats.RunData;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
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
/*      */ public class RunHistoryScreen
/*      */   implements DropdownMenuListener
/*      */ {
/*   55 */   private static final Logger logger = LogManager.getLogger(RunHistoryScreen.class.getName());
/*   56 */   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RunHistoryScreen");
/*   57 */   public static final String[] TEXT = uiStrings.TEXT;
/*      */ 
/*      */   
/*   60 */   private static final AbstractCard.CardRarity[] orderedRarity = new AbstractCard.CardRarity[] { AbstractCard.CardRarity.SPECIAL, AbstractCard.CardRarity.RARE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.BASIC, AbstractCard.CardRarity.CURSE };
/*      */ 
/*      */   
/*   63 */   private static final AbstractRelic.RelicTier[] orderedRelicRarity = new AbstractRelic.RelicTier[] { AbstractRelic.RelicTier.BOSS, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.RelicTier.RARE, AbstractRelic.RelicTier.SHOP, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.RelicTier.COMMON, AbstractRelic.RelicTier.STARTER, AbstractRelic.RelicTier.DEPRECATED };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean SHOULD_SHOW_PATH = true;
/*      */ 
/*      */   
/*   70 */   private static final String IRONCLAD_NAME = TEXT[0];
/*   71 */   private static final String SILENT_NAME = TEXT[1];
/*   72 */   private static final String DEFECT_NAME = TEXT[2];
/*   73 */   private static final String WATCHER_NAME = TEXT[35];
/*   74 */   private static final String ALL_CHARACTERS_TEXT = TEXT[23];
/*   75 */   private static final String WINS_AND_LOSSES_TEXT = TEXT[24];
/*   76 */   private static final String WINS_TEXT = TEXT[25];
/*   77 */   private static final String LOSSES_TEXT = TEXT[26];
/*      */   
/*   79 */   private static final String RUN_TYPE_ALL = TEXT[28];
/*   80 */   private static final String RUN_TYPE_NORMAL = TEXT[29];
/*   81 */   private static final String RUN_TYPE_ASCENSION = TEXT[30];
/*   82 */   private static final String RUN_TYPE_DAILY = TEXT[31];
/*      */   
/*   84 */   private static final String RARITY_LABEL_STARTER = TEXT[11];
/*   85 */   private static final String RARITY_LABEL_COMMON = TEXT[12];
/*   86 */   private static final String RARITY_LABEL_UNCOMMON = TEXT[13];
/*   87 */   private static final String RARITY_LABEL_RARE = TEXT[14];
/*   88 */   private static final String RARITY_LABEL_SPECIAL = TEXT[15];
/*   89 */   private static final String RARITY_LABEL_CURSE = TEXT[16];
/*   90 */   private static final String RARITY_LABEL_BOSS = TEXT[17];
/*   91 */   private static final String RARITY_LABEL_SHOP = TEXT[18];
/*   92 */   private static final String RARITY_LABEL_UNKNOWN = TEXT[19];
/*   93 */   private static final String COUNT_WITH_LABEL = TEXT[20];
/*   94 */   private static final String LABEL_WITH_COUNT_IN_PARENS = TEXT[21];
/*   95 */   private static final String SEED_LABEL = TEXT[32];
/*   96 */   private static final String CUSTOM_SEED_LABEL = TEXT[33];
/*      */   
/*   98 */   public MenuCancelButton button = new MenuCancelButton();
/*   99 */   private static Gson gson = new Gson();
/*  100 */   private ArrayList<RunData> unfilteredRuns = new ArrayList<>();
/*  101 */   private ArrayList<RunData> filteredRuns = new ArrayList<>();
/*  102 */   private int runIndex = 0;
/*  103 */   private RunData viewedRun = null;
/*      */   
/*      */   public boolean screenUp = false;
/*      */   
/*  107 */   public AbstractPlayer.PlayerClass currentChar = null;
/*  108 */   private static final float SHOW_X = 300.0F * Settings.xScale; private static final float HIDE_X = -800.0F * Settings.xScale;
/*  109 */   private float screenX = HIDE_X; private float targetX = HIDE_X;
/*      */   
/*      */   private RunHistoryPath runPath;
/*      */   
/*      */   private ModIcons modIcons;
/*      */   
/*      */   private CopyableTextElement seedElement;
/*      */   private CopyableTextElement secondSeedElement;
/*      */   private boolean grabbedScreen = false;
/*  118 */   private float grabStartY = 0.0F, scrollTargetY = 0.0F, scrollY = 0.0F;
/*  119 */   private float scrollLowerBound = 0.0F;
/*  120 */   private float scrollUpperBound = 0.0F;
/*      */   
/*      */   private DropdownMenu characterFilter;
/*      */   
/*      */   private DropdownMenu winLossFilter;
/*      */   
/*      */   private DropdownMenu runTypeFilter;
/*      */   
/*      */   private Hitbox prevHb;
/*      */   
/*      */   private Hitbox nextHb;
/*  131 */   private ArrayList<AbstractRelic> relics = new ArrayList<>();
/*  132 */   private ArrayList<TinyCard> cards = new ArrayList<>();
/*  133 */   private String cardCountByRarityString = "";
/*  134 */   private String relicCountByRarityString = "";
/*  135 */   private int circletCount = 0;
/*      */ 
/*      */   
/*      */   private DropdownMenu runsDropdown;
/*      */ 
/*      */   
/*      */   private static final float ARROW_SIDE_PADDING = 180.0F;
/*      */   
/*      */   private Hitbox currentHb;
/*      */   
/*  145 */   private static final float RELIC_SPACE = 64.0F * Settings.scale;
/*      */ 
/*      */   
/*  148 */   private Color controllerUiColor = new Color(0.7F, 0.9F, 1.0F, 0.25F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   AbstractRelic hoveredRelic;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   AbstractRelic clickStartedRelic;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void refreshData() {
/*      */     // Byte code:
/*      */     //   0: getstatic com/badlogic/gdx/Gdx.files : Lcom/badlogic/gdx/Files;
/*      */     //   3: new java/lang/StringBuilder
/*      */     //   6: dup
/*      */     //   7: invokespecial <init> : ()V
/*      */     //   10: ldc 'runs'
/*      */     //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   15: getstatic java/io/File.separator : Ljava/lang/String;
/*      */     //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   21: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   24: invokeinterface local : (Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;
/*      */     //   29: invokevirtual list : ()[Lcom/badlogic/gdx/files/FileHandle;
/*      */     //   32: astore_1
/*      */     //   33: aload_0
/*      */     //   34: getfield unfilteredRuns : Ljava/util/ArrayList;
/*      */     //   37: invokevirtual clear : ()V
/*      */     //   40: aload_1
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: arraylength
/*      */     //   44: istore_3
/*      */     //   45: iconst_0
/*      */     //   46: istore #4
/*      */     //   48: iload #4
/*      */     //   50: iload_3
/*      */     //   51: if_icmpge -> 453
/*      */     //   54: aload_2
/*      */     //   55: iload #4
/*      */     //   57: aaload
/*      */     //   58: astore #5
/*      */     //   60: getstatic com/megacrit/cardcrawl/core/CardCrawlGame.saveSlot : I
/*      */     //   63: lookupswitch default -> 122, 0 -> 80
/*      */     //   80: aload #5
/*      */     //   82: invokevirtual name : ()Ljava/lang/String;
/*      */     //   85: ldc '0_'
/*      */     //   87: invokevirtual contains : (Ljava/lang/CharSequence;)Z
/*      */     //   90: ifne -> 447
/*      */     //   93: aload #5
/*      */     //   95: invokevirtual name : ()Ljava/lang/String;
/*      */     //   98: ldc '1_'
/*      */     //   100: invokevirtual contains : (Ljava/lang/CharSequence;)Z
/*      */     //   103: ifne -> 447
/*      */     //   106: aload #5
/*      */     //   108: invokevirtual name : ()Ljava/lang/String;
/*      */     //   111: ldc '2_'
/*      */     //   113: invokevirtual contains : (Ljava/lang/CharSequence;)Z
/*      */     //   116: ifeq -> 157
/*      */     //   119: goto -> 447
/*      */     //   122: aload #5
/*      */     //   124: invokevirtual name : ()Ljava/lang/String;
/*      */     //   127: new java/lang/StringBuilder
/*      */     //   130: dup
/*      */     //   131: invokespecial <init> : ()V
/*      */     //   134: getstatic com/megacrit/cardcrawl/core/CardCrawlGame.saveSlot : I
/*      */     //   137: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*      */     //   140: ldc '_'
/*      */     //   142: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   145: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   148: invokevirtual contains : (Ljava/lang/CharSequence;)Z
/*      */     //   151: ifne -> 157
/*      */     //   154: goto -> 447
/*      */     //   157: aload #5
/*      */     //   159: invokevirtual list : ()[Lcom/badlogic/gdx/files/FileHandle;
/*      */     //   162: astore #6
/*      */     //   164: aload #6
/*      */     //   166: arraylength
/*      */     //   167: istore #7
/*      */     //   169: iconst_0
/*      */     //   170: istore #8
/*      */     //   172: iload #8
/*      */     //   174: iload #7
/*      */     //   176: if_icmpge -> 447
/*      */     //   179: aload #6
/*      */     //   181: iload #8
/*      */     //   183: aaload
/*      */     //   184: astore #9
/*      */     //   186: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.gson : Lcom/google/gson/Gson;
/*      */     //   189: aload #9
/*      */     //   191: invokevirtual readString : ()Ljava/lang/String;
/*      */     //   194: ldc com/megacrit/cardcrawl/screens/stats/RunData
/*      */     //   196: invokevirtual fromJson : (Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
/*      */     //   199: checkcast com/megacrit/cardcrawl/screens/stats/RunData
/*      */     //   202: astore #10
/*      */     //   204: aload #10
/*      */     //   206: ifnull -> 332
/*      */     //   209: aload #10
/*      */     //   211: getfield timestamp : Ljava/lang/String;
/*      */     //   214: ifnonnull -> 332
/*      */     //   217: aload #10
/*      */     //   219: aload #9
/*      */     //   221: invokevirtual nameWithoutExtension : ()Ljava/lang/String;
/*      */     //   224: putfield timestamp : Ljava/lang/String;
/*      */     //   227: ldc '17586'
/*      */     //   229: astore #11
/*      */     //   231: aload #10
/*      */     //   233: getfield timestamp : Ljava/lang/String;
/*      */     //   236: invokevirtual length : ()I
/*      */     //   239: aload #11
/*      */     //   241: invokevirtual length : ()I
/*      */     //   244: if_icmpne -> 251
/*      */     //   247: iconst_1
/*      */     //   248: goto -> 252
/*      */     //   251: iconst_0
/*      */     //   252: istore #12
/*      */     //   254: iload #12
/*      */     //   256: ifeq -> 332
/*      */     //   259: ldc2_w 86400
/*      */     //   262: lstore #13
/*      */     //   264: aload #10
/*      */     //   266: getfield timestamp : Ljava/lang/String;
/*      */     //   269: invokestatic parseLong : (Ljava/lang/String;)J
/*      */     //   272: lstore #15
/*      */     //   274: aload #10
/*      */     //   276: lload #15
/*      */     //   278: ldc2_w 86400
/*      */     //   281: lmul
/*      */     //   282: invokestatic toString : (J)Ljava/lang/String;
/*      */     //   285: putfield timestamp : Ljava/lang/String;
/*      */     //   288: goto -> 332
/*      */     //   291: astore #13
/*      */     //   293: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.logger : Lorg/apache/logging/log4j/Logger;
/*      */     //   296: new java/lang/StringBuilder
/*      */     //   299: dup
/*      */     //   300: invokespecial <init> : ()V
/*      */     //   303: ldc 'Run file '
/*      */     //   305: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   308: aload #9
/*      */     //   310: invokevirtual path : ()Ljava/lang/String;
/*      */     //   313: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   316: ldc ' name is could not be parsed into a Timestamp.'
/*      */     //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   321: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   324: invokeinterface info : (Ljava/lang/String;)V
/*      */     //   329: aconst_null
/*      */     //   330: astore #10
/*      */     //   332: aload #10
/*      */     //   334: ifnull -> 405
/*      */     //   337: aload #10
/*      */     //   339: getfield character_chosen : Ljava/lang/String;
/*      */     //   342: invokestatic valueOf : (Ljava/lang/String;)Lcom/megacrit/cardcrawl/characters/AbstractPlayer$PlayerClass;
/*      */     //   345: pop
/*      */     //   346: aload_0
/*      */     //   347: getfield unfilteredRuns : Ljava/util/ArrayList;
/*      */     //   350: aload #10
/*      */     //   352: invokevirtual add : (Ljava/lang/Object;)Z
/*      */     //   355: pop
/*      */     //   356: goto -> 405
/*      */     //   359: astore #11
/*      */     //   361: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.logger : Lorg/apache/logging/log4j/Logger;
/*      */     //   364: new java/lang/StringBuilder
/*      */     //   367: dup
/*      */     //   368: invokespecial <init> : ()V
/*      */     //   371: ldc 'Run file '
/*      */     //   373: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   376: aload #9
/*      */     //   378: invokevirtual path : ()Ljava/lang/String;
/*      */     //   381: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   384: ldc ' does not use a real character: '
/*      */     //   386: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   389: aload #10
/*      */     //   391: getfield character_chosen : Ljava/lang/String;
/*      */     //   394: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   397: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   400: invokeinterface info : (Ljava/lang/String;)V
/*      */     //   405: goto -> 441
/*      */     //   408: astore #10
/*      */     //   410: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.logger : Lorg/apache/logging/log4j/Logger;
/*      */     //   413: new java/lang/StringBuilder
/*      */     //   416: dup
/*      */     //   417: invokespecial <init> : ()V
/*      */     //   420: ldc 'Failed to load RunData from JSON file: '
/*      */     //   422: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   425: aload #9
/*      */     //   427: invokevirtual path : ()Ljava/lang/String;
/*      */     //   430: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   433: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   436: invokeinterface info : (Ljava/lang/String;)V
/*      */     //   441: iinc #8, 1
/*      */     //   444: goto -> 172
/*      */     //   447: iinc #4, 1
/*      */     //   450: goto -> 48
/*      */     //   453: aload_0
/*      */     //   454: getfield unfilteredRuns : Ljava/util/ArrayList;
/*      */     //   457: invokevirtual size : ()I
/*      */     //   460: ifle -> 488
/*      */     //   463: aload_0
/*      */     //   464: getfield unfilteredRuns : Ljava/util/ArrayList;
/*      */     //   467: getstatic com/megacrit/cardcrawl/screens/stats/RunData.orderByTimestampDesc : Ljava/util/Comparator;
/*      */     //   470: invokevirtual sort : (Ljava/util/Comparator;)V
/*      */     //   473: aload_0
/*      */     //   474: aload_0
/*      */     //   475: getfield unfilteredRuns : Ljava/util/ArrayList;
/*      */     //   478: iconst_0
/*      */     //   479: invokevirtual get : (I)Ljava/lang/Object;
/*      */     //   482: checkcast com/megacrit/cardcrawl/screens/stats/RunData
/*      */     //   485: putfield viewedRun : Lcom/megacrit/cardcrawl/screens/stats/RunData;
/*      */     //   488: iconst_5
/*      */     //   489: anewarray java/lang/String
/*      */     //   492: dup
/*      */     //   493: iconst_0
/*      */     //   494: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.ALL_CHARACTERS_TEXT : Ljava/lang/String;
/*      */     //   497: aastore
/*      */     //   498: dup
/*      */     //   499: iconst_1
/*      */     //   500: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.IRONCLAD_NAME : Ljava/lang/String;
/*      */     //   503: aastore
/*      */     //   504: dup
/*      */     //   505: iconst_2
/*      */     //   506: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.SILENT_NAME : Ljava/lang/String;
/*      */     //   509: aastore
/*      */     //   510: dup
/*      */     //   511: iconst_3
/*      */     //   512: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.DEFECT_NAME : Ljava/lang/String;
/*      */     //   515: aastore
/*      */     //   516: dup
/*      */     //   517: iconst_4
/*      */     //   518: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.WATCHER_NAME : Ljava/lang/String;
/*      */     //   521: aastore
/*      */     //   522: astore_2
/*      */     //   523: aload_0
/*      */     //   524: new com/megacrit/cardcrawl/screens/options/DropdownMenu
/*      */     //   527: dup
/*      */     //   528: aload_0
/*      */     //   529: aload_2
/*      */     //   530: getstatic com/megacrit/cardcrawl/helpers/FontHelper.cardDescFont_N : Lcom/badlogic/gdx/graphics/g2d/BitmapFont;
/*      */     //   533: getstatic com/megacrit/cardcrawl/core/Settings.CREAM_COLOR : Lcom/badlogic/gdx/graphics/Color;
/*      */     //   536: invokespecial <init> : (Lcom/megacrit/cardcrawl/screens/options/DropdownMenuListener;[Ljava/lang/String;Lcom/badlogic/gdx/graphics/g2d/BitmapFont;Lcom/badlogic/gdx/graphics/Color;)V
/*      */     //   539: putfield characterFilter : Lcom/megacrit/cardcrawl/screens/options/DropdownMenu;
/*      */     //   542: iconst_3
/*      */     //   543: anewarray java/lang/String
/*      */     //   546: dup
/*      */     //   547: iconst_0
/*      */     //   548: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.WINS_AND_LOSSES_TEXT : Ljava/lang/String;
/*      */     //   551: aastore
/*      */     //   552: dup
/*      */     //   553: iconst_1
/*      */     //   554: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.WINS_TEXT : Ljava/lang/String;
/*      */     //   557: aastore
/*      */     //   558: dup
/*      */     //   559: iconst_2
/*      */     //   560: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.LOSSES_TEXT : Ljava/lang/String;
/*      */     //   563: aastore
/*      */     //   564: astore_3
/*      */     //   565: aload_0
/*      */     //   566: new com/megacrit/cardcrawl/screens/options/DropdownMenu
/*      */     //   569: dup
/*      */     //   570: aload_0
/*      */     //   571: aload_3
/*      */     //   572: getstatic com/megacrit/cardcrawl/helpers/FontHelper.cardDescFont_N : Lcom/badlogic/gdx/graphics/g2d/BitmapFont;
/*      */     //   575: getstatic com/megacrit/cardcrawl/core/Settings.CREAM_COLOR : Lcom/badlogic/gdx/graphics/Color;
/*      */     //   578: invokespecial <init> : (Lcom/megacrit/cardcrawl/screens/options/DropdownMenuListener;[Ljava/lang/String;Lcom/badlogic/gdx/graphics/g2d/BitmapFont;Lcom/badlogic/gdx/graphics/Color;)V
/*      */     //   581: putfield winLossFilter : Lcom/megacrit/cardcrawl/screens/options/DropdownMenu;
/*      */     //   584: iconst_4
/*      */     //   585: anewarray java/lang/String
/*      */     //   588: dup
/*      */     //   589: iconst_0
/*      */     //   590: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.RUN_TYPE_ALL : Ljava/lang/String;
/*      */     //   593: aastore
/*      */     //   594: dup
/*      */     //   595: iconst_1
/*      */     //   596: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.RUN_TYPE_NORMAL : Ljava/lang/String;
/*      */     //   599: aastore
/*      */     //   600: dup
/*      */     //   601: iconst_2
/*      */     //   602: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.RUN_TYPE_ASCENSION : Ljava/lang/String;
/*      */     //   605: aastore
/*      */     //   606: dup
/*      */     //   607: iconst_3
/*      */     //   608: getstatic com/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen.RUN_TYPE_DAILY : Ljava/lang/String;
/*      */     //   611: aastore
/*      */     //   612: astore #4
/*      */     //   614: aload_0
/*      */     //   615: new com/megacrit/cardcrawl/screens/options/DropdownMenu
/*      */     //   618: dup
/*      */     //   619: aload_0
/*      */     //   620: aload #4
/*      */     //   622: getstatic com/megacrit/cardcrawl/helpers/FontHelper.cardDescFont_N : Lcom/badlogic/gdx/graphics/g2d/BitmapFont;
/*      */     //   625: getstatic com/megacrit/cardcrawl/core/Settings.CREAM_COLOR : Lcom/badlogic/gdx/graphics/Color;
/*      */     //   628: invokespecial <init> : (Lcom/megacrit/cardcrawl/screens/options/DropdownMenuListener;[Ljava/lang/String;Lcom/badlogic/gdx/graphics/g2d/BitmapFont;Lcom/badlogic/gdx/graphics/Color;)V
/*      */     //   631: putfield runTypeFilter : Lcom/megacrit/cardcrawl/screens/options/DropdownMenu;
/*      */     //   634: aload_0
/*      */     //   635: invokespecial resetRunsDropdown : ()V
/*      */     //   638: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #162	-> 0
/*      */     //   #163	-> 33
/*      */     //   #165	-> 40
/*      */     //   #166	-> 60
/*      */     //   #168	-> 80
/*      */     //   #170	-> 119
/*      */     //   #174	-> 122
/*      */     //   #175	-> 154
/*      */     //   #180	-> 157
/*      */     //   #182	-> 186
/*      */     //   #183	-> 204
/*      */     //   #186	-> 217
/*      */     //   #189	-> 227
/*      */     //   #190	-> 231
/*      */     //   #191	-> 254
/*      */     //   #193	-> 259
/*      */     //   #194	-> 264
/*      */     //   #195	-> 274
/*      */     //   #200	-> 288
/*      */     //   #196	-> 291
/*      */     //   #197	-> 293
/*      */     //   #198	-> 310
/*      */     //   #197	-> 324
/*      */     //   #199	-> 329
/*      */     //   #204	-> 332
/*      */     //   #206	-> 337
/*      */     //   #207	-> 346
/*      */     //   #211	-> 356
/*      */     //   #208	-> 359
/*      */     //   #209	-> 361
/*      */     //   #210	-> 378
/*      */     //   #209	-> 400
/*      */     //   #215	-> 405
/*      */     //   #213	-> 408
/*      */     //   #214	-> 410
/*      */     //   #180	-> 441
/*      */     //   #165	-> 447
/*      */     //   #219	-> 453
/*      */     //   #220	-> 463
/*      */     //   #221	-> 473
/*      */     //   #224	-> 488
/*      */     //   #226	-> 523
/*      */     //   #228	-> 542
/*      */     //   #229	-> 565
/*      */     //   #231	-> 584
/*      */     //   #233	-> 614
/*      */     //   #235	-> 634
/*      */     //   #236	-> 638
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   264	24	13	secondsInDay	J
/*      */     //   274	14	15	days	J
/*      */     //   293	39	13	ex	Ljava/lang/NumberFormatException;
/*      */     //   231	101	11	exampleDaysSinceUnixStr	Ljava/lang/String;
/*      */     //   254	78	12	assumeDaysSinceUnix	Z
/*      */     //   361	44	11	ex	Ljava/lang/RuntimeException;
/*      */     //   204	201	10	data	Lcom/megacrit/cardcrawl/screens/stats/RunData;
/*      */     //   410	31	10	ex	Lcom/google/gson/JsonSyntaxException;
/*      */     //   186	255	9	file	Lcom/badlogic/gdx/files/FileHandle;
/*      */     //   60	387	5	subFolder	Lcom/badlogic/gdx/files/FileHandle;
/*      */     //   0	639	0	this	Lcom/megacrit/cardcrawl/screens/runHistory/RunHistoryScreen;
/*      */     //   33	606	1	subfolders	[Lcom/badlogic/gdx/files/FileHandle;
/*      */     //   523	116	2	charFilterOptions	[Ljava/lang/String;
/*      */     //   565	74	3	winLossFilterOptions	[Ljava/lang/String;
/*      */     //   614	25	4	runTypeFilterOptions	[Ljava/lang/String;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   186	405	408	com/google/gson/JsonSyntaxException
/*      */     //   259	288	291	java/lang/NumberFormatException
/*      */     //   337	356	359	java/lang/IllegalArgumentException
/*      */     //   337	356	359	java/lang/NullPointerException
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
/*      */   private void resetRunsDropdown() {
/*      */     SimpleDateFormat dateFormat;
/*  239 */     this.filteredRuns.clear();
/*      */     
/*  241 */     boolean only_wins = (this.winLossFilter.getSelectedIndex() == 1);
/*  242 */     boolean only_losses = (this.winLossFilter.getSelectedIndex() == 2);
/*      */     
/*  244 */     boolean only_ironclad = (this.characterFilter.getSelectedIndex() == 1);
/*  245 */     boolean only_silent = (this.characterFilter.getSelectedIndex() == 2);
/*  246 */     boolean only_defect = (this.characterFilter.getSelectedIndex() == 3);
/*  247 */     boolean only_watcher = (this.characterFilter.getSelectedIndex() == 4);
/*      */     
/*  249 */     boolean only_normal = (this.runTypeFilter.getSelectedIndex() == 1);
/*  250 */     boolean only_ascension = (this.runTypeFilter.getSelectedIndex() == 2);
/*  251 */     boolean only_daily = (this.runTypeFilter.getSelectedIndex() == 3);
/*      */     
/*  253 */     for (RunData data : this.unfilteredRuns) {
/*  254 */       boolean includeMe = true;
/*      */ 
/*      */       
/*  257 */       if (only_wins) {
/*  258 */         includeMe = (includeMe && data.victory);
/*  259 */       } else if (only_losses) {
/*  260 */         includeMe = (includeMe && !data.victory);
/*      */       } 
/*      */ 
/*      */       
/*  264 */       String runCharacter = data.character_chosen;
/*  265 */       if (only_ironclad) {
/*  266 */         includeMe = (includeMe && runCharacter.equals(AbstractPlayer.PlayerClass.IRONCLAD.name()));
/*  267 */       } else if (only_silent) {
/*  268 */         includeMe = (includeMe && runCharacter.equals(AbstractPlayer.PlayerClass.THE_SILENT.name()));
/*  269 */       } else if (only_defect) {
/*  270 */         includeMe = (includeMe && runCharacter.equals(AbstractPlayer.PlayerClass.DEFECT.name()));
/*  271 */       } else if (only_watcher) {
/*  272 */         includeMe = (includeMe && runCharacter.equals(AbstractPlayer.PlayerClass.WATCHER.name()));
/*      */       } 
/*      */ 
/*      */       
/*  276 */       if (only_normal) {
/*  277 */         includeMe = (includeMe && !data.is_ascension_mode && !data.is_daily);
/*  278 */       } else if (only_ascension) {
/*  279 */         includeMe = (includeMe && data.is_ascension_mode);
/*  280 */       } else if (only_daily) {
/*  281 */         includeMe = (includeMe && data.is_daily);
/*      */       } 
/*      */       
/*  284 */       if (includeMe) {
/*  285 */         this.filteredRuns.add(data);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  290 */     ArrayList<String> options = new ArrayList<>();
/*      */ 
/*      */     
/*  293 */     if (Settings.language == Settings.GameLanguage.JPN) {
/*  294 */       dateFormat = new SimpleDateFormat(TEXT[34], Locale.JAPAN);
/*      */     } else {
/*  296 */       dateFormat = new SimpleDateFormat(TEXT[34]);
/*      */     } 
/*      */     
/*  299 */     for (RunData run : this.filteredRuns) {
/*      */       
/*      */       try {
/*  302 */         if (run.local_time != null) {
/*  303 */           dateTimeStr = dateFormat.format(Metrics.timestampFormatter.parse(run.local_time));
/*      */         } else {
/*  305 */           dateTimeStr = dateFormat.format(Long.valueOf(Long.valueOf(run.timestamp).longValue() * 1000L));
/*      */         } 
/*  307 */         String dateTimeStr = dateTimeStr + " - " + run.score;
/*  308 */         options.add(dateTimeStr);
/*  309 */       } catch (Exception e) {
/*  310 */         logger.info(e.getMessage());
/*      */       } 
/*      */     } 
/*  313 */     this.runsDropdown = new DropdownMenu(this, options, FontHelper.panelNameFont, Settings.CREAM_COLOR);
/*  314 */     this.runIndex = 0;
/*      */     
/*  316 */     if (this.filteredRuns.size() > 0) {
/*  317 */       reloadWithRunData(this.filteredRuns.get(this.runIndex));
/*      */     } else {
/*  319 */       this.viewedRun = null;
/*  320 */       reloadWithRunData(null);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String baseCardSuffixForCharacter(String character) {
/*  325 */     switch (AbstractPlayer.PlayerClass.valueOf(character)) {
/*      */       case DROPDOWN:
/*  327 */         return "_R";
/*      */       case ROOM:
/*  329 */         return "_G";
/*      */       case RELIC:
/*  331 */         return "_B";
/*      */       case CARD:
/*  333 */         return "_W";
/*      */     } 
/*      */ 
/*      */     
/*  337 */     return "";
/*      */   }
/*      */   
/*      */   public void reloadWithRunData(RunData runData) {
/*  341 */     if (runData == null) {
/*  342 */       logger.info("Attempted to load Run History with 0 runs.");
/*      */       return;
/*      */     } 
/*  345 */     this.scrollUpperBound = 0.0F;
/*  346 */     this.viewedRun = runData;
/*      */     
/*  348 */     reloadRelics(runData);
/*  349 */     reloadCards(runData);
/*  350 */     this.runPath.setRunData(runData);
/*      */     
/*  352 */     this.modIcons.setRunData(runData);
/*      */     
/*      */     try {
/*  355 */       if (this.viewedRun.special_seed == null || this.viewedRun.special_seed.longValue() == 0L || this.viewedRun.is_daily) {
/*  356 */         String seedFormat = this.viewedRun.chose_seed ? CUSTOM_SEED_LABEL : SEED_LABEL;
/*  357 */         String seedText = SeedHelper.getString(Long.parseLong(runData.seed_played));
/*  358 */         this.seedElement.setText(String.format(seedFormat, new Object[] { seedText }), seedText);
/*  359 */         this.secondSeedElement.setText("", "");
/*      */       } else {
/*  361 */         String seedText = SeedHelper.getString(runData.special_seed.longValue());
/*  362 */         this.seedElement.setText(String.format(CUSTOM_SEED_LABEL, new Object[] { seedText }), seedText);
/*  363 */         String secondSeedText = SeedHelper.getString(Long.parseLong(runData.seed_played));
/*  364 */         this.secondSeedElement.setText(String.format(SEED_LABEL, new Object[] { secondSeedText }), secondSeedText);
/*      */       } 
/*  366 */     } catch (NumberFormatException ex) {
/*  367 */       this.seedElement.setText("", "");
/*  368 */       this.secondSeedElement.setText("", "");
/*      */     } 
/*  370 */     this.scrollTargetY = 0.0F;
/*  371 */     resetScrolling();
/*  372 */     if (this.runsDropdown != null) {
/*  373 */       this.runsDropdown.setSelectedIndex(this.filteredRuns.indexOf(runData));
/*      */     }
/*      */   }
/*      */   
/*      */   private void reloadRelics(RunData runData) {
/*  378 */     this.relics.clear();
/*  379 */     this.circletCount = runData.circlet_count;
/*  380 */     boolean circletCountSet = (this.circletCount > 0);
/*      */     
/*  382 */     Hashtable<AbstractRelic.RelicTier, Integer> relicRarityCounts = new Hashtable<>();
/*      */     
/*  384 */     AbstractRelic circlet = null;
/*  385 */     for (String relicName : runData.relics) {
/*      */       try {
/*  387 */         AbstractRelic relic = RelicLibrary.getRelic(relicName).makeCopy();
/*  388 */         relic.isSeen = true;
/*  389 */         if (relic instanceof com.megacrit.cardcrawl.relics.Circlet) {
/*  390 */           if (relicName.equals("Circlet")) {
/*  391 */             if (!circletCountSet) {
/*  392 */               this.circletCount++;
/*      */             }
/*  394 */             if (circlet == null) {
/*  395 */               circlet = relic;
/*  396 */               this.relics.add(relic);
/*      */             } 
/*      */           } else {
/*  399 */             logger.info("Could not find relic for: " + relicName);
/*      */           } 
/*      */         } else {
/*  402 */           this.relics.add(relic);
/*      */         } 
/*  404 */         int newCount = relicRarityCounts.containsKey(relic.tier) ? (((Integer)relicRarityCounts.get(relic.tier)).intValue() + 1) : 1;
/*  405 */         relicRarityCounts.put(relic.tier, Integer.valueOf(newCount));
/*  406 */       } catch (NullPointerException ex) {
/*  407 */         logger.info("NPE while loading: " + relicName);
/*      */       } 
/*      */     } 
/*      */     
/*  411 */     if (circlet != null && this.circletCount > 1) {
/*  412 */       circlet.setCounter(this.circletCount);
/*      */     }
/*      */     
/*  415 */     StringBuilder bldr = new StringBuilder();
/*  416 */     for (AbstractRelic.RelicTier rarity : orderedRelicRarity) {
/*  417 */       if (relicRarityCounts.containsKey(rarity)) {
/*  418 */         if (bldr.length() > 0) {
/*  419 */           bldr.append(", ");
/*      */         }
/*  421 */         bldr.append(String.format(COUNT_WITH_LABEL, new Object[] { relicRarityCounts.get(rarity), rarityLabel(rarity) }));
/*      */       } 
/*      */     } 
/*  424 */     this.relicCountByRarityString = bldr.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private void reloadCards(RunData runData) {
/*  429 */     Hashtable<String, AbstractCard> rawNameToCards = new Hashtable<>();
/*  430 */     Hashtable<AbstractCard, Integer> cardCounts = new Hashtable<>();
/*  431 */     Hashtable<AbstractCard.CardRarity, Integer> cardRarityCounts = new Hashtable<>();
/*  432 */     CardGroup sortedMasterDeck = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*      */ 
/*      */     
/*  435 */     for (String cardID : runData.master_deck) {
/*      */       AbstractCard card;
/*  437 */       if (rawNameToCards.containsKey(cardID)) {
/*  438 */         card = rawNameToCards.get(cardID);
/*      */       } else {
/*  440 */         card = cardForName(runData, cardID);
/*      */       } 
/*  442 */       if (card != null) {
/*  443 */         int value = cardCounts.containsKey(card) ? (((Integer)cardCounts.get(card)).intValue() + 1) : 1;
/*  444 */         cardCounts.put(card, Integer.valueOf(value));
/*  445 */         rawNameToCards.put(cardID, card);
/*      */         
/*  447 */         int rarityCount = cardRarityCounts.containsKey(card.rarity) ? (((Integer)cardRarityCounts.get(card.rarity)).intValue() + 1) : 1;
/*  448 */         cardRarityCounts.put(card.rarity, Integer.valueOf(rarityCount));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  453 */     sortedMasterDeck.clear();
/*  454 */     for (AbstractCard card : rawNameToCards.values()) {
/*  455 */       sortedMasterDeck.addToTop(card);
/*      */     }
/*      */     
/*  458 */     sortedMasterDeck.sortAlphabetically(true);
/*  459 */     sortedMasterDeck.sortByRarityPlusStatusCardType(false);
/*  460 */     sortedMasterDeck = sortedMasterDeck.getGroupedByColor();
/*      */     
/*  462 */     this.cards.clear();
/*      */     
/*  464 */     for (AbstractCard card : sortedMasterDeck.group) {
/*  465 */       this.cards.add(new TinyCard(card, ((Integer)cardCounts.get(card)).intValue()));
/*      */     }
/*      */     
/*  468 */     StringBuilder bldr = new StringBuilder();
/*  469 */     for (AbstractCard.CardRarity rarity : orderedRarity) {
/*  470 */       if (cardRarityCounts.containsKey(rarity)) {
/*  471 */         if (bldr.length() > 0) {
/*  472 */           bldr.append(", ");
/*      */         }
/*      */         
/*  475 */         bldr.append(String.format(COUNT_WITH_LABEL, new Object[] { cardRarityCounts.get(rarity), rarityLabel(rarity) }));
/*      */       } 
/*      */     } 
/*  478 */     this.cardCountByRarityString = bldr.toString();
/*      */   }
/*      */   
/*      */   private String rarityLabel(AbstractCard.CardRarity rarity) {
/*  482 */     switch (rarity) {
/*      */       case DROPDOWN:
/*  484 */         return RARITY_LABEL_STARTER;
/*      */       case ROOM:
/*  486 */         return RARITY_LABEL_SPECIAL;
/*      */       case RELIC:
/*  488 */         return RARITY_LABEL_COMMON;
/*      */       case CARD:
/*  490 */         return RARITY_LABEL_UNCOMMON;
/*      */       case null:
/*  492 */         return RARITY_LABEL_RARE;
/*      */       case null:
/*  494 */         return RARITY_LABEL_CURSE;
/*      */     } 
/*      */     
/*  497 */     return RARITY_LABEL_UNKNOWN;
/*      */   }
/*      */ 
/*      */   
/*      */   private String rarityLabel(AbstractRelic.RelicTier rarity) {
/*  502 */     switch (rarity) {
/*      */       case DROPDOWN:
/*  504 */         return RARITY_LABEL_STARTER;
/*      */       case ROOM:
/*  506 */         return RARITY_LABEL_COMMON;
/*      */       case RELIC:
/*  508 */         return RARITY_LABEL_UNCOMMON;
/*      */       case CARD:
/*  510 */         return RARITY_LABEL_RARE;
/*      */       case null:
/*  512 */         return RARITY_LABEL_SPECIAL;
/*      */       case null:
/*  514 */         return RARITY_LABEL_BOSS;
/*      */       case null:
/*  516 */         return RARITY_LABEL_SHOP;
/*      */     } 
/*      */     
/*  519 */     return RARITY_LABEL_UNKNOWN;
/*      */   }
/*      */ 
/*      */   
/*      */   private void layoutTinyCards(ArrayList<TinyCard> cards, float x, float y) {
/*  524 */     float originX = x + screenPosX(60.0F);
/*  525 */     float originY = y - screenPosY(64.0F);
/*  526 */     float rowHeight = screenPosY(48.0F);
/*  527 */     float columnWidth = screenPosX(340.0F);
/*  528 */     int row = 0, column = 0;
/*      */     
/*  530 */     TinyCard.desiredColumns = (cards.size() <= 36) ? 3 : 4;
/*  531 */     int cardsPerColumn = cards.size() / TinyCard.desiredColumns;
/*  532 */     int remainderCards = cards.size() - cardsPerColumn * TinyCard.desiredColumns;
/*      */     
/*  534 */     int[] columnSizes = new int[TinyCard.desiredColumns];
/*  535 */     Arrays.fill(columnSizes, cardsPerColumn);
/*  536 */     for (int i = 0; i < remainderCards; i++) {
/*  537 */       columnSizes[i % TinyCard.desiredColumns] = columnSizes[i % TinyCard.desiredColumns] + 1;
/*      */     }
/*      */     
/*  540 */     for (TinyCard card : cards) {
/*  541 */       if (row >= columnSizes[column]) {
/*  542 */         row = 0;
/*  543 */         column++;
/*      */       } 
/*      */       
/*  546 */       float cardY = originY - row * rowHeight;
/*  547 */       card.hb.move(originX + column * columnWidth + card.hb.width / 2.0F, cardY);
/*      */ 
/*      */       
/*  550 */       if (card.col == -1) {
/*  551 */         card.col = column;
/*  552 */         card.row = row;
/*      */       } 
/*      */       
/*  555 */       row++;
/*  556 */       this.scrollUpperBound = Math.max(this.scrollUpperBound, this.scrollY - cardY + screenPosY(50.0F));
/*      */     } 
/*      */   }
/*      */   
/*      */   private AbstractCard cardForName(RunData runData, String cardID) {
/*  561 */     String libraryLookupName = cardID;
/*  562 */     if (cardID.endsWith("+")) {
/*  563 */       libraryLookupName = cardID.substring(0, cardID.length() - 1);
/*      */     }
/*  565 */     if (libraryLookupName.equals("Defend") || libraryLookupName.equals("Strike")) {
/*  566 */       libraryLookupName = libraryLookupName + baseCardSuffixForCharacter(runData.character_chosen);
/*      */     }
/*      */     
/*  569 */     AbstractCard card = CardLibrary.getCard(libraryLookupName);
/*  570 */     int upgrades = 0;
/*  571 */     if (card != null) {
/*  572 */       if (cardID.endsWith("+")) {
/*  573 */         upgrades = 1;
/*      */       }
/*  575 */     } else if (libraryLookupName.contains("+")) {
/*      */       
/*  577 */       String[] split = libraryLookupName.split("\\+", -1);
/*  578 */       libraryLookupName = split[0];
/*  579 */       upgrades = Integer.parseInt(split[1]);
/*  580 */       card = CardLibrary.getCard(libraryLookupName);
/*      */     } 
/*  582 */     if (card != null) {
/*  583 */       card = card.makeCopy();
/*  584 */       for (int i = 0; i < upgrades; i++) {
/*  585 */         card.upgrade();
/*      */       }
/*  587 */       return card;
/*      */     } 
/*  589 */     logger.info("Could not find card named: " + cardID);
/*  590 */     return null;
/*      */   }
/*      */   
/*      */   public void update() {
/*  594 */     updateControllerInput();
/*      */     
/*  596 */     if (Settings.isControllerMode && !CardCrawlGame.isPopupOpen && this.currentHb != null) {
/*  597 */       if (Gdx.input.getY() > Settings.HEIGHT * 0.8F) {
/*  598 */         this.scrollTargetY += Settings.SCROLL_SPEED / 2.0F;
/*  599 */         if (this.scrollTargetY > this.scrollUpperBound) {
/*  600 */           this.scrollTargetY = this.scrollUpperBound;
/*      */         }
/*  602 */       } else if (Gdx.input.getY() < Settings.HEIGHT * 0.2F && this.scrollY > 100.0F) {
/*  603 */         this.scrollTargetY -= Settings.SCROLL_SPEED / 2.0F;
/*  604 */         if (this.scrollTargetY < this.scrollLowerBound) {
/*  605 */           this.scrollTargetY = this.scrollLowerBound;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  611 */     if (this.runsDropdown.isOpen) {
/*  612 */       this.runsDropdown.update(); return;
/*      */     } 
/*  614 */     if (this.winLossFilter.isOpen) {
/*  615 */       this.winLossFilter.update(); return;
/*      */     } 
/*  617 */     if (this.characterFilter.isOpen) {
/*  618 */       this.characterFilter.update(); return;
/*      */     } 
/*  620 */     if (this.runTypeFilter.isOpen) {
/*  621 */       this.runTypeFilter.update();
/*      */       return;
/*      */     } 
/*  624 */     this.runsDropdown.update();
/*  625 */     this.winLossFilter.update();
/*  626 */     this.characterFilter.update();
/*  627 */     this.runTypeFilter.update();
/*      */ 
/*      */     
/*  630 */     this.button.update();
/*  631 */     updateScrolling();
/*  632 */     updateArrows();
/*  633 */     this.modIcons.update();
/*  634 */     this.runPath.update();
/*      */     
/*  636 */     if (!this.seedElement.getText().isEmpty()) {
/*  637 */       this.seedElement.update();
/*      */     }
/*  639 */     if (!this.secondSeedElement.getText().isEmpty()) {
/*  640 */       this.secondSeedElement.update();
/*      */     }
/*      */     
/*  643 */     if (this.button.hb.clicked || InputHelper.pressedEscape) {
/*  644 */       InputHelper.pressedEscape = false;
/*  645 */       hide();
/*      */     } 
/*      */     
/*  648 */     this.screenX = MathHelper.uiLerpSnap(this.screenX, this.targetX);
/*      */     
/*  650 */     if (this.filteredRuns.size() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  654 */     boolean isAPopupOpen = (CardCrawlGame.cardPopup.isOpen || CardCrawlGame.relicPopup.isOpen);
/*  655 */     if (!isAPopupOpen) {
/*  656 */       if (InputActionSet.left.isJustPressed()) {
/*  657 */         this.runIndex = Math.max(0, this.runIndex - 1);
/*  658 */         reloadWithRunData(this.filteredRuns.get(this.runIndex));
/*      */       } 
/*  660 */       if (InputActionSet.right.isJustPressed()) {
/*  661 */         this.runIndex = Math.min(this.runIndex + 1, this.filteredRuns.size() - 1);
/*  662 */         reloadWithRunData(this.filteredRuns.get(this.runIndex));
/*      */       } 
/*      */     } 
/*      */     
/*  666 */     handleRelicInteraction();
/*      */     
/*  668 */     for (TinyCard card : this.cards) {
/*  669 */       boolean didClick = card.updateDidClick();
/*  670 */       if (didClick) {
/*  671 */         CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*  672 */         for (TinyCard addMe : this.cards) {
/*  673 */           cardGroup.addToTop(addMe.card);
/*      */         }
/*  675 */         CardCrawlGame.cardPopup.open(card.card, cardGroup);
/*      */       } 
/*      */     } 
/*      */     
/*  679 */     if (Settings.isControllerMode && this.currentHb != null)
/*  680 */       CInputHelper.setCursor(this.currentHb); 
/*      */   }
/*      */   
/*      */   private enum InputSection
/*      */   {
/*  685 */     DROPDOWN, ROOM, RELIC, CARD;
/*      */   }
/*      */   
/*      */   private void updateControllerInput() {
/*  689 */     if (!Settings.isControllerMode || this.runsDropdown.isOpen || this.winLossFilter.isOpen || this.characterFilter.isOpen || this.runTypeFilter.isOpen) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  694 */     InputSection section = null;
/*  695 */     boolean anyHovered = false;
/*  696 */     int index = 0;
/*      */ 
/*      */     
/*  699 */     ArrayList<Hitbox> hbs = new ArrayList<>();
/*  700 */     if (!this.runsDropdown.rows.isEmpty()) {
/*  701 */       hbs.add(this.runsDropdown.getHitbox());
/*      */     }
/*  703 */     hbs.add(this.winLossFilter.getHitbox());
/*  704 */     hbs.add(this.characterFilter.getHitbox());
/*  705 */     hbs.add(this.runTypeFilter.getHitbox());
/*      */     
/*  707 */     for (Hitbox hb : hbs) {
/*  708 */       if (hb.hovered) {
/*  709 */         section = InputSection.DROPDOWN;
/*  710 */         anyHovered = true;
/*      */         break;
/*      */       } 
/*  713 */       index++;
/*      */     } 
/*      */ 
/*      */     
/*  717 */     if (!anyHovered) {
/*  718 */       index = 0;
/*  719 */       for (RunPathElement e : this.runPath.pathElements) {
/*  720 */         if (e.hb.hovered) {
/*  721 */           section = InputSection.ROOM;
/*  722 */           anyHovered = true;
/*      */           break;
/*      */         } 
/*  725 */         index++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  730 */     if (!anyHovered) {
/*  731 */       index = 0;
/*  732 */       for (AbstractRelic r : this.relics) {
/*  733 */         if (r.hb.hovered) {
/*  734 */           section = InputSection.RELIC;
/*  735 */           anyHovered = true;
/*      */           break;
/*      */         } 
/*  738 */         index++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  743 */     if (!anyHovered) {
/*  744 */       index = 0;
/*  745 */       for (TinyCard card : this.cards) {
/*  746 */         if (card.hb.hovered) {
/*  747 */           section = InputSection.CARD;
/*  748 */           anyHovered = true;
/*      */           break;
/*      */         } 
/*  751 */         index++;
/*      */       } 
/*      */     } 
/*      */     
/*  755 */     if (!anyHovered) {
/*  756 */       CInputHelper.setCursor(hbs.get(0));
/*  757 */       this.currentHb = hbs.get(0);
/*  758 */       this.scrollTargetY = 0.0F;
/*      */     } else {
/*  760 */       int c; int r; switch (section) {
/*      */         case DROPDOWN:
/*  762 */           if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
/*  763 */             index--;
/*  764 */             if (index != -1) {
/*  765 */               CInputHelper.setCursor(hbs.get(index));
/*  766 */               this.currentHb = hbs.get(index);
/*      */             }  break;
/*  768 */           }  if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
/*  769 */             index++;
/*  770 */             if (hbs.size() == 4) {
/*  771 */               if (index > hbs.size() - 1 || index == 1) {
/*      */                 
/*  773 */                 if (!this.runPath.pathElements.isEmpty()) {
/*  774 */                   CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(0)).hb);
/*  775 */                   this.currentHb = ((RunPathElement)this.runPath.pathElements.get(0)).hb;
/*      */                   
/*      */                   break;
/*      */                 } 
/*  779 */                 if (!this.relics.isEmpty()) {
/*  780 */                   CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*  781 */                   this.currentHb = ((AbstractRelic)this.relics.get(0)).hb; break;
/*      */                 } 
/*  783 */                 CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  784 */                 this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */                 
/*      */                 break;
/*      */               } 
/*  788 */               CInputHelper.setCursor(hbs.get(index));
/*  789 */               this.currentHb = hbs.get(index);
/*      */               break;
/*      */             } 
/*  792 */             if (index > hbs.size() - 1) {
/*  793 */               index = 0;
/*      */             }
/*  795 */             CInputHelper.setCursor(hbs.get(index));
/*  796 */             this.currentHb = hbs.get(index); break;
/*      */           } 
/*  798 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/*  799 */             if (index == 0) {
/*  800 */               CInputHelper.setCursor(hbs.get(1));
/*  801 */               this.currentHb = hbs.get(1);
/*      */             }  break;
/*  803 */           }  if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/*  804 */             if (index == 1) {
/*  805 */               CInputHelper.setCursor(hbs.get(0));
/*  806 */               this.currentHb = hbs.get(0);
/*  807 */               this.scrollTargetY = 0.0F; break;
/*  808 */             }  if (index > 1) {
/*      */               
/*  810 */               if (!this.runPath.pathElements.isEmpty()) {
/*  811 */                 CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(0)).hb);
/*  812 */                 this.currentHb = ((RunPathElement)this.runPath.pathElements.get(0)).hb;
/*      */                 
/*      */                 break;
/*      */               } 
/*  816 */               if (!this.relics.isEmpty()) {
/*  817 */                 CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*  818 */                 this.currentHb = ((AbstractRelic)this.relics.get(0)).hb; break;
/*  819 */               }  if (!this.cards.isEmpty()) {
/*  820 */                 CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  821 */                 this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           break;
/*      */         
/*      */         case ROOM:
/*  828 */           if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
/*  829 */             int j = ((RunPathElement)this.runPath.pathElements.get(index)).col;
/*  830 */             int k = ((RunPathElement)this.runPath.pathElements.get(index)).row - 1;
/*      */             
/*  832 */             if (k < 0) {
/*      */               
/*  834 */               CInputHelper.setCursor(hbs.get(0));
/*  835 */               this.currentHb = hbs.get(0);
/*  836 */               this.scrollTargetY = 0.0F; break;
/*      */             } 
/*  838 */             boolean foundNode = false;
/*      */             
/*      */             int i;
/*  841 */             for (i = 0; i < this.runPath.pathElements.size(); i++) {
/*  842 */               if (((RunPathElement)this.runPath.pathElements.get(i)).row == k && ((RunPathElement)this.runPath.pathElements.get(i)).col == j) {
/*  843 */                 CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(i)).hb);
/*  844 */                 this.currentHb = ((RunPathElement)this.runPath.pathElements.get(i)).hb;
/*  845 */                 foundNode = true;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */             
/*  851 */             if (!foundNode) {
/*  852 */               for (i = this.runPath.pathElements.size() - 1; i > 0; i--) {
/*  853 */                 if (((RunPathElement)this.runPath.pathElements.get(i)).row == k) {
/*  854 */                   CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(i)).hb);
/*  855 */                   this.currentHb = ((RunPathElement)this.runPath.pathElements.get(i)).hb;
/*  856 */                   foundNode = true;
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */             }
/*      */             
/*  863 */             if (!foundNode) {
/*  864 */               CInputHelper.setCursor(hbs.get(0));
/*  865 */               this.currentHb = hbs.get(0);
/*  866 */               this.scrollTargetY = 0.0F;
/*      */             }  break;
/*      */           } 
/*  869 */           if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
/*  870 */             int j = ((RunPathElement)this.runPath.pathElements.get(index)).col;
/*  871 */             int k = ((RunPathElement)this.runPath.pathElements.get(index)).row + 1;
/*      */ 
/*      */             
/*  874 */             if (k > ((RunPathElement)this.runPath.pathElements.get(this.runPath.pathElements.size() - 1)).row) {
/*  875 */               if (!this.relics.isEmpty()) {
/*  876 */                 CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*  877 */                 this.currentHb = ((AbstractRelic)this.relics.get(0)).hb; break;
/*      */               } 
/*  879 */               CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  880 */               this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */               break;
/*      */             } 
/*  883 */             boolean foundNode = false;
/*      */             
/*      */             int i;
/*  886 */             for (i = this.runPath.pathElements.size() - 1; i > 0; i--) {
/*  887 */               if (((RunPathElement)this.runPath.pathElements.get(i)).row == k && ((RunPathElement)this.runPath.pathElements.get(i)).col == j) {
/*  888 */                 CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(i)).hb);
/*  889 */                 this.currentHb = ((RunPathElement)this.runPath.pathElements.get(i)).hb;
/*  890 */                 foundNode = true;
/*      */ 
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */             
/*  897 */             if (!foundNode) {
/*  898 */               for (i = this.runPath.pathElements.size() - 1; i > 0; i--) {
/*  899 */                 if (((RunPathElement)this.runPath.pathElements.get(i)).row == k) {
/*  900 */                   CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(i)).hb);
/*  901 */                   this.currentHb = ((RunPathElement)this.runPath.pathElements.get(i)).hb;
/*  902 */                   foundNode = true;
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */             }
/*      */             
/*  909 */             if (!foundNode) {
/*  910 */               if (!this.relics.isEmpty()) {
/*  911 */                 CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*  912 */                 this.currentHb = ((AbstractRelic)this.relics.get(0)).hb; break;
/*      */               } 
/*  914 */               CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  915 */               this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */             } 
/*      */             
/*      */             break;
/*      */           } 
/*  920 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/*  921 */             index--;
/*  922 */             if (index < 0) {
/*  923 */               if (hbs.size() > 3) {
/*  924 */                 CInputHelper.setCursor(hbs.get(3));
/*  925 */                 this.currentHb = hbs.get(3);
/*      */               }  break;
/*      */             } 
/*  928 */             CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(index)).hb);
/*  929 */             this.currentHb = ((RunPathElement)this.runPath.pathElements.get(index)).hb; break;
/*      */           } 
/*  931 */           if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/*  932 */             index++;
/*  933 */             if (index > this.runPath.pathElements.size() - 1) {
/*  934 */               if (!this.relics.isEmpty()) {
/*  935 */                 CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/*  936 */                 this.currentHb = ((AbstractRelic)this.relics.get(0)).hb; break;
/*      */               } 
/*  938 */               CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  939 */               this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */               break;
/*      */             } 
/*  942 */             CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(index)).hb);
/*  943 */             this.currentHb = ((RunPathElement)this.runPath.pathElements.get(index)).hb;
/*      */           } 
/*      */           break;
/*      */         
/*      */         case RELIC:
/*  948 */           if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
/*  949 */             index -= 15;
/*  950 */             if (index < 0) {
/*  951 */               if (!this.runPath.pathElements.isEmpty()) {
/*  952 */                 CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(0)).hb);
/*  953 */                 this.currentHb = ((RunPathElement)this.runPath.pathElements.get(0)).hb; break;
/*      */               } 
/*  955 */               CInputHelper.setCursor(hbs.get(0));
/*  956 */               this.currentHb = hbs.get(0);
/*  957 */               this.scrollTargetY = 0.0F;
/*      */               break;
/*      */             } 
/*  960 */             CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
/*  961 */             this.currentHb = ((AbstractRelic)this.relics.get(index)).hb; break;
/*      */           } 
/*  963 */           if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
/*  964 */             index += 15;
/*  965 */             if (index > this.relics.size() - 1) {
/*  966 */               if (!this.cards.isEmpty()) {
/*  967 */                 CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  968 */                 this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */               }  break;
/*      */             } 
/*  971 */             CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
/*  972 */             this.currentHb = ((AbstractRelic)this.relics.get(index)).hb; break;
/*      */           } 
/*  974 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/*  975 */             index--;
/*  976 */             if (index < 0) {
/*  977 */               if (!this.cards.isEmpty()) {
/*  978 */                 CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  979 */                 this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */               }  break;
/*  981 */             }  if (!this.relics.isEmpty()) {
/*  982 */               CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
/*  983 */               this.currentHb = ((AbstractRelic)this.relics.get(index)).hb;
/*      */             }  break;
/*  985 */           }  if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/*  986 */             index++;
/*  987 */             if (index > this.relics.size() - 1) {
/*  988 */               if (!this.cards.isEmpty()) {
/*  989 */                 CInputHelper.setCursor(((TinyCard)this.cards.get(0)).hb);
/*  990 */                 this.currentHb = ((TinyCard)this.cards.get(0)).hb;
/*      */               }  break;
/*  992 */             }  if (!this.relics.isEmpty()) {
/*  993 */               CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
/*  994 */               this.currentHb = ((AbstractRelic)this.relics.get(index)).hb;
/*      */             }  break;
/*  996 */           }  if (CInputActionSet.select.isJustPressed()) {
/*  997 */             CardCrawlGame.relicPopup.open(this.relics.get(index), this.relics);
/*      */           }
/*      */           break;
/*      */         case CARD:
/* 1001 */           c = ((TinyCard)this.cards.get(index)).col;
/* 1002 */           r = ((TinyCard)this.cards.get(index)).row;
/*      */           
/* 1004 */           if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/* 1005 */             c--;
/*      */             
/* 1007 */             if (c < 0) {
/* 1008 */               for (int j = this.cards.size() - 1; j > 0; j--) {
/* 1009 */                 if ((((TinyCard)this.cards.get(j)).col == TinyCard.desiredColumns - 1 || ((TinyCard)this.cards.get(j)).col == 1) && ((TinyCard)this.cards
/* 1010 */                   .get(j)).row == r) {
/* 1011 */                   CInputHelper.setCursor(((TinyCard)this.cards.get(j)).hb);
/* 1012 */                   this.currentHb = ((TinyCard)this.cards.get(j)).hb; break;
/*      */                 } 
/*      */               } 
/*      */               break;
/*      */             } 
/* 1017 */             boolean foundNode = false;
/* 1018 */             for (int i = 0; i < this.cards.size(); i++) {
/* 1019 */               if (((TinyCard)this.cards.get(i)).col == c && ((TinyCard)this.cards.get(i)).row == r) {
/* 1020 */                 CInputHelper.setCursor(((TinyCard)this.cards.get(i)).hb);
/* 1021 */                 this.currentHb = ((TinyCard)this.cards.get(i)).hb;
/* 1022 */                 foundNode = true;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/* 1027 */             if (!foundNode) {
/* 1028 */               if (!this.relics.isEmpty()) {
/* 1029 */                 CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
/* 1030 */                 this.currentHb = ((AbstractRelic)this.relics.get(0)).hb; break;
/*      */               } 
/* 1032 */               CInputHelper.setCursor(this.runsDropdown.getHitbox());
/* 1033 */               this.currentHb = this.runsDropdown.getHitbox();
/*      */             } 
/*      */             break;
/*      */           } 
/* 1037 */           if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/* 1038 */             c++;
/* 1039 */             if (c > TinyCard.desiredColumns - 1) {
/* 1040 */               c = 0; break;
/*      */             } 
/* 1042 */             boolean foundNode = false; int i;
/* 1043 */             for (i = 0; i < this.cards.size(); i++) {
/* 1044 */               if (((TinyCard)this.cards.get(i)).col == c && ((TinyCard)this.cards.get(i)).row == r) {
/* 1045 */                 CInputHelper.setCursor(((TinyCard)this.cards.get(i)).hb);
/* 1046 */                 this.currentHb = ((TinyCard)this.cards.get(i)).hb;
/* 1047 */                 foundNode = true;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/* 1052 */             if (!foundNode) {
/* 1053 */               c = 0;
/* 1054 */               for (i = 0; i < this.cards.size(); i++) {
/* 1055 */                 if (((TinyCard)this.cards.get(i)).col == c && ((TinyCard)this.cards.get(i)).row == r) {
/* 1056 */                   CInputHelper.setCursor(((TinyCard)this.cards.get(i)).hb);
/* 1057 */                   this.currentHb = ((TinyCard)this.cards.get(i)).hb; break;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             break;
/*      */           } 
/* 1063 */           if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
/* 1064 */             index--;
/* 1065 */             if (index < 0) {
/* 1066 */               if (!this.relics.isEmpty()) {
/* 1067 */                 CInputHelper.setCursor(((AbstractRelic)this.relics.get(this.relics.size() - 1)).hb);
/* 1068 */                 this.currentHb = ((AbstractRelic)this.relics.get(this.relics.size() - 1)).hb; break;
/* 1069 */               }  if (!this.runPath.pathElements.isEmpty()) {
/* 1070 */                 CInputHelper.setCursor(((RunPathElement)this.runPath.pathElements.get(0)).hb);
/* 1071 */                 this.currentHb = ((RunPathElement)this.runPath.pathElements.get(0)).hb; break;
/*      */               } 
/* 1073 */               CInputHelper.setCursor(this.runsDropdown.getHitbox());
/* 1074 */               this.currentHb = this.runsDropdown.getHitbox();
/*      */               break;
/*      */             } 
/* 1077 */             CInputHelper.setCursor(((TinyCard)this.cards.get(index)).hb);
/* 1078 */             this.currentHb = ((TinyCard)this.cards.get(index)).hb; break;
/*      */           } 
/* 1080 */           if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
/* 1081 */             index++;
/* 1082 */             if (index <= this.cards.size() - 1) {
/* 1083 */               CInputHelper.setCursor(((TinyCard)this.cards.get(index)).hb);
/* 1084 */               this.currentHb = ((TinyCard)this.cards.get(index)).hb;
/*      */             }  break;
/* 1086 */           }  if (CInputActionSet.select.isJustPressed()) {
/* 1087 */             CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/* 1088 */             for (TinyCard addMe : this.cards) {
/* 1089 */               cardGroup.addToTop(addMe.card);
/*      */             }
/* 1091 */             CardCrawlGame.cardPopup.open(((TinyCard)this.cards.get(index)).card, cardGroup);
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void open() {
/* 1101 */     this.currentHb = null;
/* 1102 */     CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.RUN_HISTORY;
/* 1103 */     SingleCardViewPopup.enableUpgradeToggle = false;
/*      */     
/* 1105 */     refreshData();
/*      */     
/* 1107 */     this.targetX = SHOW_X;
/* 1108 */     this.button.show(TEXT[3]);
/* 1109 */     this.screenUp = true;
/*      */     
/* 1111 */     this.scrollY = this.scrollLowerBound;
/* 1112 */     this.scrollTargetY = this.scrollLowerBound;
/*      */   }
/*      */   
/*      */   public void hide() {
/* 1116 */     this.targetX = HIDE_X;
/* 1117 */     this.button.hide();
/* 1118 */     this.screenUp = false;
/* 1119 */     this.currentChar = null;
/* 1120 */     CardCrawlGame.mainMenuScreen.panelScreen.refresh();
/* 1121 */     SingleCardViewPopup.enableUpgradeToggle = true;
/*      */   }
/*      */   
/*      */   public void render(SpriteBatch sb) {
/* 1125 */     renderRunHistoryScreen(sb);
/* 1126 */     renderArrows(sb);
/* 1127 */     renderFilters(sb);
/* 1128 */     this.button.render(sb);
/* 1129 */     renderControllerUi(sb, this.currentHb);
/*      */   }
/*      */   
/*      */   private void renderControllerUi(SpriteBatch sb, Hitbox hb) {
/* 1133 */     if (Settings.isControllerMode && hb != null) {
/* 1134 */       sb.setBlendFunction(770, 1);
/* 1135 */       sb.setColor(this.controllerUiColor);
/* 1136 */       sb.draw(ImageMaster.CONTROLLER_HB_HIGHLIGHT, hb.cX - hb.width / 2.0F, hb.cY - hb.height / 2.0F, hb.width, hb.height);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1142 */       sb.setBlendFunction(770, 771);
/*      */     } 
/*      */   }
/*      */   
/*      */   private String characterText(String chosenCharacter) {
/* 1147 */     if (chosenCharacter.equals(AbstractPlayer.PlayerClass.IRONCLAD.name()))
/* 1148 */       return IRONCLAD_NAME; 
/* 1149 */     if (chosenCharacter.equals(AbstractPlayer.PlayerClass.THE_SILENT.name()))
/* 1150 */       return SILENT_NAME; 
/* 1151 */     if (chosenCharacter.equals(AbstractPlayer.PlayerClass.DEFECT.name()))
/* 1152 */       return DEFECT_NAME; 
/* 1153 */     if (chosenCharacter.equals(AbstractPlayer.PlayerClass.WATCHER.name())) {
/* 1154 */       return WATCHER_NAME;
/*      */     }
/* 1156 */     return chosenCharacter;
/*      */   }
/*      */   
/*      */   private void renderArrows(SpriteBatch sb) {
/* 1160 */     if (this.runIndex < this.filteredRuns.size() - 1) {
/* 1161 */       sb.setColor(Color.WHITE);
/* 1162 */       sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, true, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1180 */       if (this.nextHb.hovered) {
/* 1181 */         sb.setBlendFunction(770, 1);
/* 1182 */         sb.setColor(Color.WHITE);
/* 1183 */         sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, true, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1200 */         sb.setBlendFunction(770, 771);
/*      */       } 
/*      */       
/* 1203 */       if (Settings.isControllerMode) {
/* 1204 */         sb.draw(CInputActionSet.pageRightViewExhaust
/* 1205 */             .getKeyImg(), this.nextHb.cX - 32.0F, this.nextHb.cY - 32.0F - 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1223 */       this.nextHb.render(sb);
/*      */     } 
/*      */     
/* 1226 */     if (this.runIndex > 0) {
/* 1227 */       sb.setColor(Color.WHITE);
/* 1228 */       sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, false, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1246 */       if (this.prevHb.hovered) {
/* 1247 */         sb.setBlendFunction(770, 1);
/* 1248 */         sb.setColor(Color.WHITE);
/* 1249 */         sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, false, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1266 */         sb.setBlendFunction(770, 771);
/*      */       } 
/*      */       
/* 1269 */       if (Settings.isControllerMode) {
/* 1270 */         sb.draw(CInputActionSet.pageLeftViewDeck
/* 1271 */             .getKeyImg(), this.prevHb.cX - 32.0F, this.prevHb.cY - 32.0F - 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1288 */       this.prevHb.render(sb);
/*      */     } 
/*      */   } private void renderRunHistoryScreen(SpriteBatch sb) {
/*      */     String resultText;
/*      */     Color resultTextColor;
/* 1293 */     float TOP_POSITION = 1020.0F;
/*      */     
/* 1295 */     if (this.viewedRun == null) {
/* 1296 */       FontHelper.renderSmartText(sb, FontHelper.panelNameFont, TEXT[4], Settings.WIDTH * 0.43F, Settings.HEIGHT * 0.53F, Settings.GOLD_COLOR);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1306 */     float header1x = this.screenX + screenPosX(100.0F);
/* 1307 */     float header2x = this.screenX + screenPosX(120.0F);
/* 1308 */     float yOffset = this.scrollY + screenPosY(TOP_POSITION);
/*      */     
/* 1310 */     String characterName = characterText(this.viewedRun.character_chosen);
/* 1311 */     renderHeader1(sb, characterName, header1x, yOffset);
/* 1312 */     float approxCharNameWidth = approximateHeader1Width(characterName);
/*      */ 
/*      */     
/* 1315 */     if (!this.seedElement.getText().isEmpty()) {
/* 1316 */       this.seedElement.render(sb, this.screenX + 1200.0F * Settings.scale, yOffset);
/* 1317 */       if (!this.secondSeedElement.getText().isEmpty()) {
/* 1318 */         this.secondSeedElement.render(sb, 1200.0F * Settings.scale, yOffset - screenPosY(36.0F));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1323 */     yOffset -= screenPosY(50.0F);
/* 1324 */     String specialModeText = "";
/*      */     
/* 1326 */     if (this.viewedRun.is_daily) {
/* 1327 */       specialModeText = " (" + TEXT[27] + ")";
/* 1328 */     } else if (this.viewedRun.is_ascension_mode) {
/* 1329 */       specialModeText = " (" + TEXT[5] + this.viewedRun.ascension_level + ")";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1334 */     if (this.viewedRun.victory) {
/* 1335 */       resultText = TEXT[8] + specialModeText;
/* 1336 */       resultTextColor = Settings.GREEN_TEXT_COLOR;
/*      */     } else {
/* 1338 */       resultTextColor = Settings.RED_TEXT_COLOR;
/* 1339 */       if (this.viewedRun.killed_by == null) {
/*      */         
/* 1341 */         resultText = String.format(TEXT[7], new Object[] { Integer.valueOf(this.viewedRun.floor_reached) }) + specialModeText;
/*      */       } else {
/* 1343 */         resultText = String.format(TEXT[6], new Object[] {
/*      */               
/* 1345 */               Integer.valueOf(this.viewedRun.floor_reached), 
/* 1346 */               MonsterHelper.getEncounterName(this.viewedRun.killed_by) }) + specialModeText;
/*      */       } 
/*      */     } 
/* 1349 */     renderSubHeading(sb, resultText, header1x, yOffset, resultTextColor);
/*      */     
/* 1351 */     if (this.viewedRun.victory) {
/* 1352 */       sb.setColor(Color.WHITE);
/* 1353 */       sb.draw(ImageMaster.TIMER_ICON, header1x + 
/*      */           
/* 1355 */           approximateSubHeaderWidth(resultText) - 32.0F + 54.0F * Settings.scale, yOffset - 32.0F - 10.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1371 */       renderSubHeading(sb, 
/*      */           
/* 1373 */           CharStat.formatHMSM(this.viewedRun.playtime), header1x + 
/* 1374 */           approximateSubHeaderWidth(resultText) + 80.0F * Settings.scale, yOffset, Settings.CREAM_COLOR);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1379 */     float scoreLineXOffset = header1x;
/*      */     
/* 1381 */     yOffset -= screenPosY(40.0F);
/* 1382 */     String scoreText = String.format(TEXT[22], new Object[] { Integer.valueOf(this.viewedRun.score) });
/* 1383 */     renderSubHeading(sb, scoreText, header1x, yOffset, Settings.GOLD_COLOR);
/* 1384 */     scoreLineXOffset += approximateSubHeaderWidth(scoreText);
/*      */     
/* 1386 */     if (this.modIcons.hasMods()) {
/* 1387 */       this.modIcons.renderDailyMods(sb, scoreLineXOffset, yOffset);
/* 1388 */       scoreLineXOffset += this.modIcons.approximateWidth();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1393 */     yOffset -= screenPosY(18.0F);
/* 1394 */     this.runPath.render(sb, header2x + 52.0F * Settings.scale, yOffset);
/* 1395 */     yOffset -= this.runPath.approximateHeight();
/*      */ 
/*      */ 
/*      */     
/* 1399 */     yOffset -= screenPosY(35.0F);
/* 1400 */     float relicBottom = renderRelics(sb, header2x, yOffset);
/* 1401 */     yOffset = relicBottom - screenPosY(70.0F);
/* 1402 */     renderDeck(sb, header2x, yOffset);
/*      */ 
/*      */     
/* 1405 */     this.runsDropdown.render(sb, header1x + approxCharNameWidth + 
/*      */         
/* 1407 */         screenPosX(30.0F), this.scrollY + 
/* 1408 */         screenPosY(TOP_POSITION + 6.0F));
/*      */   }
/*      */   
/*      */   private void renderHeader1(SpriteBatch sb, String text, float x, float y) {
/* 1412 */     FontHelper.renderSmartText(sb, FontHelper.charTitleFont, text, x, y, 9999.0F, 36.0F * Settings.scale, Settings.GOLD_COLOR);
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
/*      */   private float approximateHeader1Width(String text) {
/* 1424 */     return FontHelper.getSmartWidth(FontHelper.charTitleFont, text, 9999.0F, 36.0F * Settings.scale);
/*      */   }
/*      */   
/*      */   private float approximateSubHeaderWidth(String text) {
/* 1428 */     return FontHelper.getSmartWidth(FontHelper.buttonLabelFont, text, 9999.0F, 36.0F * Settings.scale);
/*      */   }
/*      */   
/*      */   private void renderSubHeading(SpriteBatch sb, String text, float x, float y, Color color) {
/* 1432 */     FontHelper.renderSmartText(sb, FontHelper.buttonLabelFont, text, x, y, 9999.0F, 36.0F * Settings.scale, color);
/*      */   }
/*      */   
/*      */   private void renderSubHeadingWithMessage(SpriteBatch sb, String main, String description, float x, float y) {
/* 1436 */     FontHelper.renderFontLeftTopAligned(sb, FontHelper.buttonLabelFont, main, x, y, Settings.GOLD_COLOR);
/* 1437 */     FontHelper.renderFontLeftTopAligned(sb, FontHelper.cardDescFont_N, description, x + 
/*      */ 
/*      */ 
/*      */         
/* 1441 */         FontHelper.getSmartWidth(FontHelper.buttonLabelFont, main, 99999.0F, 0.0F), y - 4.0F * Settings.scale, Settings.CREAM_COLOR);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderDeck(SpriteBatch sb, float x, float y) {
/* 1447 */     layoutTinyCards(this.cards, this.screenX + screenPosX(90.0F), y);
/* 1448 */     int cardCount = 0;
/*      */     
/* 1450 */     for (TinyCard card : this.cards) {
/* 1451 */       card.render(sb);
/* 1452 */       cardCount += card.count;
/*      */     } 
/*      */     
/* 1455 */     String mainText = String.format(LABEL_WITH_COUNT_IN_PARENS, new Object[] { TEXT[9], Integer.valueOf(cardCount) });
/* 1456 */     renderSubHeadingWithMessage(sb, mainText, this.cardCountByRarityString, x, y);
/*      */   }
/*      */   
/*      */   private float renderRelics(SpriteBatch sb, float x, float y) {
/* 1460 */     String mainText = String.format(LABEL_WITH_COUNT_IN_PARENS, new Object[] { TEXT[10], Integer.valueOf(this.relics.size()) });
/* 1461 */     renderSubHeadingWithMessage(sb, mainText, this.relicCountByRarityString, x, y);
/*      */     
/* 1463 */     int col = 0;
/* 1464 */     int row = 0;
/*      */     
/* 1466 */     float relicStartX = x + screenPosX(30.0F) + RELIC_SPACE / 2.0F;
/* 1467 */     float relicStartY = y - RELIC_SPACE - screenPosY(10.0F);
/*      */     
/* 1469 */     for (AbstractRelic r : this.relics) {
/*      */       
/* 1471 */       if (col == 15) {
/* 1472 */         col = 0;
/* 1473 */         row++;
/*      */       } 
/* 1475 */       r.currentX = relicStartX + RELIC_SPACE * col;
/* 1476 */       r.currentY = relicStartY - RELIC_SPACE * row;
/* 1477 */       r.hb.move(r.currentX, r.currentY);
/* 1478 */       r.render(sb, false, Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
/* 1479 */       col++;
/*      */     } 
/* 1481 */     return relicStartY - RELIC_SPACE * row;
/*      */   }
/*      */   
/* 1484 */   public RunHistoryScreen() { this.hoveredRelic = null;
/* 1485 */     this.clickStartedRelic = null; this.runPath = new RunHistoryPath(); this.modIcons = new ModIcons(); this.seedElement = new CopyableTextElement(FontHelper.cardDescFont_N); this.secondSeedElement = new CopyableTextElement(FontHelper.cardDescFont_N); this.prevHb = new Hitbox(110.0F * Settings.scale, 110.0F * Settings.scale);
/*      */     this.prevHb.move(180.0F * Settings.scale, Settings.HEIGHT / 2.0F);
/*      */     this.nextHb = new Hitbox(110.0F * Settings.scale, 110.0F * Settings.scale);
/* 1488 */     this.nextHb.move(Settings.WIDTH - 180.0F * Settings.xScale, Settings.HEIGHT / 2.0F); } private void handleRelicInteraction() { for (AbstractRelic r : this.relics) {
/*      */ 
/*      */       
/* 1491 */       boolean wasScreenUp = AbstractDungeon.isScreenUp;
/* 1492 */       AbstractDungeon.isScreenUp = true;
/* 1493 */       r.update();
/* 1494 */       AbstractDungeon.isScreenUp = wasScreenUp;
/*      */       
/* 1496 */       if (r.hb.hovered) {
/* 1497 */         this.hoveredRelic = r;
/*      */       }
/*      */     } 
/*      */     
/* 1501 */     if (this.hoveredRelic != null) {
/* 1502 */       CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
/* 1503 */       if (InputHelper.justClickedLeft) {
/* 1504 */         this.clickStartedRelic = this.hoveredRelic;
/*      */       }
/* 1506 */       if (InputHelper.justReleasedClickLeft && 
/* 1507 */         this.hoveredRelic == this.clickStartedRelic) {
/* 1508 */         CardCrawlGame.relicPopup.open(this.hoveredRelic, this.relics);
/* 1509 */         this.clickStartedRelic = null;
/*      */       } 
/*      */     } else {
/*      */       
/* 1513 */       this.clickStartedRelic = null;
/*      */     } 
/* 1515 */     this.hoveredRelic = null; }
/*      */ 
/*      */ 
/*      */   
/*      */   private float screenPos(float val) {
/* 1520 */     return val * Settings.scale;
/*      */   }
/*      */   
/*      */   private float screenPosX(float val) {
/* 1524 */     return val * Settings.xScale;
/*      */   }
/*      */   
/*      */   private float screenPosY(float val) {
/* 1528 */     return val * Settings.yScale;
/*      */   }
/*      */   
/*      */   private void updateArrows() {
/* 1532 */     if (this.runIndex < this.filteredRuns.size() - 1) {
/* 1533 */       this.nextHb.update();
/* 1534 */       if (this.nextHb.justHovered) {
/* 1535 */         CardCrawlGame.sound.play("UI_HOVER");
/* 1536 */       } else if (this.nextHb.hovered && InputHelper.justClickedLeft) {
/* 1537 */         this.nextHb.clickStarted = true;
/* 1538 */         CardCrawlGame.sound.play("UI_CLICK_1");
/* 1539 */       } else if (this.nextHb.clicked || (CInputActionSet.pageRightViewExhaust.isJustPressed() && !CardCrawlGame.isPopupOpen)) {
/*      */         
/* 1541 */         this.nextHb.clicked = false;
/* 1542 */         this.runIndex = Math.min(this.runIndex + 1, this.filteredRuns.size() - 1);
/* 1543 */         reloadWithRunData(this.filteredRuns.get(this.runIndex));
/*      */       } 
/*      */     } 
/*      */     
/* 1547 */     if (this.runIndex > 0) {
/* 1548 */       this.prevHb.update();
/* 1549 */       if (this.prevHb.justHovered) {
/* 1550 */         CardCrawlGame.sound.play("UI_HOVER");
/* 1551 */       } else if (this.prevHb.hovered && InputHelper.justClickedLeft) {
/* 1552 */         this.prevHb.clickStarted = true;
/* 1553 */         CardCrawlGame.sound.play("UI_CLICK_1");
/* 1554 */       } else if (this.prevHb.clicked || (CInputActionSet.pageLeftViewDeck.isJustPressed() && !CardCrawlGame.isPopupOpen)) {
/*      */         
/* 1556 */         this.prevHb.clicked = false;
/* 1557 */         this.runIndex = Math.max(0, this.runIndex - 1);
/* 1558 */         reloadWithRunData(this.filteredRuns.get(this.runIndex));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderFilters(SpriteBatch sb) {
/* 1565 */     float filterX = this.screenX + screenPosX(-270.0F);
/* 1566 */     float winLossY = this.scrollY + screenPosY(1000.0F);
/* 1567 */     float charY = winLossY - screenPosY(54.0F);
/* 1568 */     float runTypeY = charY - screenPosY(54.0F);
/*      */ 
/*      */     
/* 1571 */     this.runTypeFilter.render(sb, filterX, runTypeY);
/* 1572 */     this.characterFilter.render(sb, filterX, charY);
/* 1573 */     this.winLossFilter.render(sb, filterX, winLossY);
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateScrolling() {
/* 1578 */     int y = InputHelper.mY;
/*      */     
/* 1580 */     if (this.scrollUpperBound > 0.0F) {
/* 1581 */       if (!this.grabbedScreen) {
/* 1582 */         if (InputHelper.scrolledDown) {
/* 1583 */           this.scrollTargetY += Settings.SCROLL_SPEED;
/* 1584 */         } else if (InputHelper.scrolledUp) {
/* 1585 */           this.scrollTargetY -= Settings.SCROLL_SPEED;
/*      */         } 
/*      */         
/* 1588 */         if (InputHelper.justClickedLeft) {
/* 1589 */           this.grabbedScreen = true;
/* 1590 */           this.grabStartY = y - this.scrollTargetY;
/*      */         }
/*      */       
/* 1593 */       } else if (InputHelper.isMouseDown) {
/* 1594 */         this.scrollTargetY = y - this.grabStartY;
/*      */       } else {
/* 1596 */         this.grabbedScreen = false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1601 */     this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.scrollTargetY);
/* 1602 */     resetScrolling();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void resetScrolling() {
/* 1609 */     if (this.scrollTargetY < this.scrollLowerBound) {
/* 1610 */       this.scrollTargetY = MathHelper.scrollSnapLerpSpeed(this.scrollTargetY, this.scrollLowerBound);
/* 1611 */     } else if (this.scrollTargetY > this.scrollUpperBound) {
/* 1612 */       this.scrollTargetY = MathHelper.scrollSnapLerpSpeed(this.scrollTargetY, this.scrollUpperBound);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void changedSelectionTo(DropdownMenu dropdownMenu, int index, String optionText) {
/* 1618 */     if (dropdownMenu == this.runsDropdown) {
/* 1619 */       runDropdownChangedTo(index);
/* 1620 */     } else if (isFilterDropdown(dropdownMenu)) {
/* 1621 */       resetRunsDropdown();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isFilterDropdown(DropdownMenu dropdownMenu) {
/* 1626 */     return (dropdownMenu == this.winLossFilter || dropdownMenu == this.characterFilter || dropdownMenu == this.runTypeFilter);
/*      */   }
/*      */   
/*      */   private void runDropdownChangedTo(int index) {
/* 1630 */     if (this.runIndex == index) {
/*      */       return;
/*      */     }
/*      */     
/* 1634 */     this.runIndex = index;
/* 1635 */     reloadWithRunData(this.filteredRuns.get(this.runIndex));
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\screens\runHistory\RunHistoryScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */