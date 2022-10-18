/*     */ package com.megacrit.cardcrawl.helpers;
/*     */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.metrics.BotDataUploader;
/*     */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*     */ import com.megacrit.cardcrawl.relics.BirdFacedUrn;
/*     */ import com.megacrit.cardcrawl.relics.CoffeeDripper;
/*     */ import com.megacrit.cardcrawl.relics.Duality;
/*     */ import com.megacrit.cardcrawl.relics.Lantern;
/*     */ import com.megacrit.cardcrawl.relics.PrismaticShard;
/*     */ import com.megacrit.cardcrawl.relics.RunicPyramid;
/*     */ import com.megacrit.cardcrawl.relics.TwistedFunnel;
/*     */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ 
/*     */ public class RelicLibrary {
/*  21 */   private static final Logger logger = LogManager.getLogger(RelicLibrary.class.getName());
/*  22 */   public static int totalRelicCount = 0;
/*  23 */   public static int seenRelics = 0;
/*     */   
/*  25 */   private static HashMap<String, AbstractRelic> sharedRelics = new HashMap<>();
/*  26 */   private static HashMap<String, AbstractRelic> redRelics = new HashMap<>();
/*  27 */   private static HashMap<String, AbstractRelic> greenRelics = new HashMap<>();
/*  28 */   private static HashMap<String, AbstractRelic> blueRelics = new HashMap<>();
/*  29 */   private static HashMap<String, AbstractRelic> purpleRelics = new HashMap<>();
/*     */ 
/*     */   
/*  32 */   public static ArrayList<AbstractRelic> starterList = new ArrayList<>();
/*  33 */   public static ArrayList<AbstractRelic> commonList = new ArrayList<>();
/*  34 */   public static ArrayList<AbstractRelic> uncommonList = new ArrayList<>();
/*  35 */   public static ArrayList<AbstractRelic> rareList = new ArrayList<>();
/*  36 */   public static ArrayList<AbstractRelic> bossList = new ArrayList<>();
/*  37 */   public static ArrayList<AbstractRelic> specialList = new ArrayList<>();
/*  38 */   public static ArrayList<AbstractRelic> shopList = new ArrayList<>();
/*  39 */   public static ArrayList<AbstractRelic> redList = new ArrayList<>();
/*  40 */   public static ArrayList<AbstractRelic> greenList = new ArrayList<>();
/*  41 */   public static ArrayList<AbstractRelic> blueList = new ArrayList<>();
/*  42 */   public static ArrayList<AbstractRelic> whiteList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initialize() {
/*  48 */     long startTime = System.currentTimeMillis();
/*     */     
/*  50 */     add((AbstractRelic)new Abacus());
/*  51 */     add((AbstractRelic)new Akabeko());
/*  52 */     add((AbstractRelic)new Anchor());
/*  53 */     add((AbstractRelic)new AncientTeaSet());
/*  54 */     add((AbstractRelic)new ArtOfWar());
/*  55 */     add((AbstractRelic)new Astrolabe());
/*  56 */     add((AbstractRelic)new BagOfMarbles());
/*  57 */     add((AbstractRelic)new BagOfPreparation());
/*  58 */     add((AbstractRelic)new BirdFacedUrn());
/*  59 */     add((AbstractRelic)new BlackStar());
/*  60 */     add((AbstractRelic)new BloodVial());
/*  61 */     add((AbstractRelic)new BloodyIdol());
/*  62 */     add((AbstractRelic)new BlueCandle());
/*  63 */     add((AbstractRelic)new Boot());
/*  64 */     add((AbstractRelic)new BottledFlame());
/*  65 */     add((AbstractRelic)new BottledLightning());
/*  66 */     add((AbstractRelic)new BottledTornado());
/*  67 */     add((AbstractRelic)new BronzeScales());
/*  68 */     add((AbstractRelic)new BustedCrown());
/*  69 */     add((AbstractRelic)new Calipers());
/*  70 */     add((AbstractRelic)new CallingBell());
/*  71 */     add((AbstractRelic)new CaptainsWheel());
/*  72 */     add((AbstractRelic)new Cauldron());
/*  73 */     add((AbstractRelic)new CentennialPuzzle());
/*  74 */     add((AbstractRelic)new CeramicFish());
/*  75 */     add((AbstractRelic)new ChemicalX());
/*  76 */     add((AbstractRelic)new ClockworkSouvenir());
/*  77 */     add((AbstractRelic)new CoffeeDripper());
/*  78 */     add((AbstractRelic)new Courier());
/*  79 */     add((AbstractRelic)new CultistMask());
/*  80 */     add((AbstractRelic)new CursedKey());
/*  81 */     add((AbstractRelic)new DarkstonePeriapt());
/*  82 */     add((AbstractRelic)new DeadBranch());
/*  83 */     add((AbstractRelic)new DollysMirror());
/*  84 */     add((AbstractRelic)new DreamCatcher());
/*  85 */     add((AbstractRelic)new DuVuDoll());
/*  86 */     add((AbstractRelic)new Ectoplasm());
/*  87 */     add((AbstractRelic)new EmptyCage());
/*  88 */     add((AbstractRelic)new Enchiridion());
/*  89 */     add((AbstractRelic)new EternalFeather());
/*  90 */     add((AbstractRelic)new FaceOfCleric());
/*  91 */     add((AbstractRelic)new FossilizedHelix());
/*  92 */     add((AbstractRelic)new FrozenEgg2());
/*  93 */     add((AbstractRelic)new FrozenEye());
/*  94 */     add((AbstractRelic)new FusionHammer());
/*  95 */     add((AbstractRelic)new GamblingChip());
/*  96 */     add((AbstractRelic)new Ginger());
/*  97 */     add((AbstractRelic)new Girya());
/*  98 */     add((AbstractRelic)new GoldenIdol());
/*  99 */     add((AbstractRelic)new GremlinHorn());
/* 100 */     add((AbstractRelic)new GremlinMask());
/* 101 */     add((AbstractRelic)new HandDrill());
/* 102 */     add((AbstractRelic)new HappyFlower());
/* 103 */     add((AbstractRelic)new HornCleat());
/* 104 */     add((AbstractRelic)new IceCream());
/* 105 */     add((AbstractRelic)new IncenseBurner());
/* 106 */     add((AbstractRelic)new InkBottle());
/* 107 */     add((AbstractRelic)new JuzuBracelet());
/* 108 */     add((AbstractRelic)new Kunai());
/* 109 */     add((AbstractRelic)new Lantern());
/* 110 */     add((AbstractRelic)new LetterOpener());
/* 111 */     add((AbstractRelic)new LizardTail());
/* 112 */     add((AbstractRelic)new Mango());
/* 113 */     add((AbstractRelic)new MarkOfTheBloom());
/* 114 */     add((AbstractRelic)new Matryoshka());
/* 115 */     add((AbstractRelic)new MawBank());
/* 116 */     add((AbstractRelic)new MealTicket());
/* 117 */     add((AbstractRelic)new MeatOnTheBone());
/* 118 */     add((AbstractRelic)new MedicalKit());
/* 119 */     add((AbstractRelic)new MembershipCard());
/* 120 */     add((AbstractRelic)new MercuryHourglass());
/* 121 */     add((AbstractRelic)new MoltenEgg2());
/* 122 */     add((AbstractRelic)new MummifiedHand());
/* 123 */     add((AbstractRelic)new MutagenicStrength());
/* 124 */     add((AbstractRelic)new Necronomicon());
/* 125 */     add((AbstractRelic)new NeowsLament());
/* 126 */     add((AbstractRelic)new NilrysCodex());
/* 127 */     add((AbstractRelic)new NlothsGift());
/* 128 */     add((AbstractRelic)new NlothsMask());
/* 129 */     add((AbstractRelic)new Nunchaku());
/* 130 */     add((AbstractRelic)new OddlySmoothStone());
/* 131 */     add((AbstractRelic)new OddMushroom());
/* 132 */     add((AbstractRelic)new OldCoin());
/* 133 */     add((AbstractRelic)new Omamori());
/* 134 */     add((AbstractRelic)new OrangePellets());
/* 135 */     add((AbstractRelic)new Orichalcum());
/* 136 */     add((AbstractRelic)new OrnamentalFan());
/* 137 */     add((AbstractRelic)new Orrery());
/* 138 */     add((AbstractRelic)new PandorasBox());
/* 139 */     add((AbstractRelic)new Pantograph());
/* 140 */     add((AbstractRelic)new PeacePipe());
/* 141 */     add((AbstractRelic)new Pear());
/* 142 */     add((AbstractRelic)new PenNib());
/* 143 */     add((AbstractRelic)new PhilosopherStone());
/* 144 */     add((AbstractRelic)new Pocketwatch());
/* 145 */     add((AbstractRelic)new PotionBelt());
/* 146 */     add((AbstractRelic)new PrayerWheel());
/* 147 */     add((AbstractRelic)new PreservedInsect());
/* 148 */     add((AbstractRelic)new PrismaticShard());
/* 149 */     add((AbstractRelic)new QuestionCard());
/* 150 */     add((AbstractRelic)new RedMask());
/* 151 */     add((AbstractRelic)new RegalPillow());
/* 152 */     add((AbstractRelic)new RunicDome());
/* 153 */     add((AbstractRelic)new RunicPyramid());
/* 154 */     add((AbstractRelic)new SacredBark());
/* 155 */     add((AbstractRelic)new Shovel());
/* 156 */     add((AbstractRelic)new Shuriken());
/* 157 */     add((AbstractRelic)new SingingBowl());
/* 158 */     add((AbstractRelic)new SlaversCollar());
/* 159 */     add((AbstractRelic)new Sling());
/* 160 */     add((AbstractRelic)new SmilingMask());
/* 161 */     add((AbstractRelic)new SneckoEye());
/* 162 */     add((AbstractRelic)new Sozu());
/* 163 */     add((AbstractRelic)new SpiritPoop());
/* 164 */     add((AbstractRelic)new SsserpentHead());
/* 165 */     add((AbstractRelic)new StoneCalendar());
/* 166 */     add((AbstractRelic)new StrangeSpoon());
/* 167 */     add((AbstractRelic)new Strawberry());
/* 168 */     add((AbstractRelic)new StrikeDummy());
/* 169 */     add((AbstractRelic)new Sundial());
/* 170 */     add((AbstractRelic)new ThreadAndNeedle());
/* 171 */     add((AbstractRelic)new TinyChest());
/* 172 */     add((AbstractRelic)new TinyHouse());
/* 173 */     add((AbstractRelic)new Toolbox());
/* 174 */     add((AbstractRelic)new Torii());
/* 175 */     add((AbstractRelic)new ToxicEgg2());
/* 176 */     add((AbstractRelic)new ToyOrnithopter());
/* 177 */     add((AbstractRelic)new TungstenRod());
/* 178 */     add((AbstractRelic)new Turnip());
/* 179 */     add((AbstractRelic)new UnceasingTop());
/* 180 */     add((AbstractRelic)new Vajra());
/* 181 */     add((AbstractRelic)new VelvetChoker());
/* 182 */     add((AbstractRelic)new Waffle());
/* 183 */     add((AbstractRelic)new WarPaint());
/* 184 */     add((AbstractRelic)new WarpedTongs());
/* 185 */     add((AbstractRelic)new Whetstone());
/* 186 */     add((AbstractRelic)new WhiteBeast());
/* 187 */     add((AbstractRelic)new WingBoots());
/*     */ 
/*     */     
/* 190 */     addGreen((AbstractRelic)new HoveringKite());
/* 191 */     addGreen((AbstractRelic)new NinjaScroll());
/* 192 */     addGreen((AbstractRelic)new PaperCrane());
/* 193 */     addGreen((AbstractRelic)new RingOfTheSerpent());
/* 194 */     addGreen((AbstractRelic)new SnakeRing());
/* 195 */     addGreen((AbstractRelic)new SneckoSkull());
/* 196 */     addGreen((AbstractRelic)new TheSpecimen());
/* 197 */     addGreen((AbstractRelic)new Tingsha());
/* 198 */     addGreen((AbstractRelic)new ToughBandages());
/* 199 */     addGreen((AbstractRelic)new TwistedFunnel());
/* 200 */     addGreen((AbstractRelic)new WristBlade());
/*     */ 
/*     */     
/* 203 */     addRed((AbstractRelic)new BlackBlood());
/* 204 */     addRed((AbstractRelic)new Brimstone());
/* 205 */     addRed((AbstractRelic)new BurningBlood());
/* 206 */     addRed((AbstractRelic)new ChampionsBelt());
/* 207 */     addRed((AbstractRelic)new CharonsAshes());
/* 208 */     addRed((AbstractRelic)new MagicFlower());
/* 209 */     addRed((AbstractRelic)new MarkOfPain());
/* 210 */     addRed((AbstractRelic)new PaperFrog());
/* 211 */     addRed((AbstractRelic)new RedSkull());
/* 212 */     addRed((AbstractRelic)new RunicCube());
/* 213 */     addRed((AbstractRelic)new SelfFormingClay());
/*     */ 
/*     */     
/* 216 */     addBlue((AbstractRelic)new CrackedCore());
/* 217 */     addBlue((AbstractRelic)new DataDisk());
/* 218 */     addBlue((AbstractRelic)new EmotionChip());
/* 219 */     addBlue((AbstractRelic)new FrozenCore());
/* 220 */     addBlue((AbstractRelic)new GoldPlatedCables());
/* 221 */     addBlue((AbstractRelic)new Inserter());
/* 222 */     addBlue((AbstractRelic)new NuclearBattery());
/* 223 */     addBlue((AbstractRelic)new RunicCapacitor());
/* 224 */     addBlue((AbstractRelic)new SymbioticVirus());
/*     */ 
/*     */     
/* 227 */     addPurple((AbstractRelic)new CloakClasp());
/* 228 */     addPurple((AbstractRelic)new Damaru());
/* 229 */     addPurple((AbstractRelic)new GoldenEye());
/* 230 */     addPurple((AbstractRelic)new HolyWater());
/* 231 */     addPurple((AbstractRelic)new Melange());
/* 232 */     addPurple((AbstractRelic)new PureWater());
/* 233 */     addPurple((AbstractRelic)new VioletLotus());
/* 234 */     addPurple((AbstractRelic)new TeardropLocket());
/* 235 */     addPurple((AbstractRelic)new Duality());
/*     */ 
/*     */ 
/*     */     
/* 239 */     if (Settings.isBeta);
/*     */ 
/*     */     
/* 242 */     logger.info("Relic load time: " + (System.currentTimeMillis() - startTime) + "ms");
/* 243 */     sortLists();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetForReload() {
/* 249 */     totalRelicCount = 0;
/* 250 */     seenRelics = 0;
/* 251 */     sharedRelics.clear();
/* 252 */     redRelics.clear();
/* 253 */     greenRelics.clear();
/* 254 */     blueRelics.clear();
/* 255 */     purpleRelics.clear();
/* 256 */     starterList.clear();
/* 257 */     commonList.clear();
/* 258 */     uncommonList.clear();
/* 259 */     rareList.clear();
/* 260 */     bossList.clear();
/* 261 */     specialList.clear();
/* 262 */     shopList.clear();
/* 263 */     redList.clear();
/* 264 */     greenList.clear();
/* 265 */     blueList.clear();
/* 266 */     whiteList.clear();
/*     */   }
/*     */   
/*     */   private static void sortLists() {
/* 270 */     Collections.sort(starterList);
/* 271 */     Collections.sort(commonList);
/* 272 */     Collections.sort(uncommonList);
/* 273 */     Collections.sort(rareList);
/* 274 */     Collections.sort(bossList);
/* 275 */     Collections.sort(specialList);
/* 276 */     Collections.sort(shopList);
/*     */     
/* 278 */     if (Settings.isDev) {
/* 279 */       logger.info(starterList);
/* 280 */       logger.info(commonList);
/* 281 */       logger.info(uncommonList);
/* 282 */       logger.info(rareList);
/* 283 */       logger.info(bossList);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void printRelicsMissingLargeArt() {
/* 289 */     int common = 0, uncommon = 0, rare = 0, boss = 0, shop = 0, other = 0;
/*     */     
/* 291 */     logger.info("[ART] START DISPLAYING RELICS WITH MISSING HIGH RES ART");
/*     */     
/* 293 */     for (Map.Entry<String, AbstractRelic> r : sharedRelics.entrySet()) {
/* 294 */       AbstractRelic relic = r.getValue();
/* 295 */       if (ImageMaster.loadImage("images/largeRelics/" + relic.imgUrl) == null) {
/* 296 */         logger.info(relic.name);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void printRelicCount() {
/* 303 */     int common = 0, uncommon = 0, rare = 0, boss = 0, shop = 0, other = 0;
/*     */     
/* 305 */     for (Map.Entry<String, AbstractRelic> r : sharedRelics.entrySet()) {
/* 306 */       switch (((AbstractRelic)r.getValue()).tier) {
/*     */         case IRONCLAD:
/* 308 */           common++;
/*     */           continue;
/*     */         case THE_SILENT:
/* 311 */           uncommon++;
/*     */           continue;
/*     */         case DEFECT:
/* 314 */           rare++;
/*     */           continue;
/*     */         case WATCHER:
/* 317 */           boss++;
/*     */           continue;
/*     */         case null:
/* 320 */           shop++;
/*     */           continue;
/*     */       } 
/* 323 */       other++;
/*     */     } 
/*     */ 
/*     */     
/* 327 */     if (Settings.isDev) {
/* 328 */       logger.info("RELIC COUNTS");
/* 329 */       logger.info("Common: " + common);
/* 330 */       logger.info("Uncommon: " + uncommon);
/* 331 */       logger.info("Rare: " + rare);
/* 332 */       logger.info("Boss: " + boss);
/* 333 */       logger.info("Shop: " + shop);
/* 334 */       logger.info("Other: " + other);
/* 335 */       logger.info("Red: " + redRelics.size());
/* 336 */       logger.info("Green: " + greenRelics.size());
/* 337 */       logger.info("Blue: " + blueRelics.size());
/* 338 */       logger.info("Purple: " + purpleRelics.size());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void add(AbstractRelic relic) {
/* 346 */     if (UnlockTracker.isRelicSeen(relic.relicId)) {
/* 347 */       seenRelics++;
/*     */     }
/* 349 */     relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
/* 350 */     sharedRelics.put(relic.relicId, relic);
/* 351 */     addToTierList(relic);
/* 352 */     totalRelicCount++;
/*     */   }
/*     */   
/*     */   public static void addRed(AbstractRelic relic) {
/* 356 */     if (UnlockTracker.isRelicSeen(relic.relicId)) {
/* 357 */       seenRelics++;
/*     */     }
/* 359 */     relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
/* 360 */     redRelics.put(relic.relicId, relic);
/* 361 */     addToTierList(relic);
/* 362 */     redList.add(relic);
/* 363 */     totalRelicCount++;
/*     */   }
/*     */   
/*     */   public static void addGreen(AbstractRelic relic) {
/* 367 */     if (UnlockTracker.isRelicSeen(relic.relicId)) {
/* 368 */       seenRelics++;
/*     */     }
/* 370 */     relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
/* 371 */     greenRelics.put(relic.relicId, relic);
/* 372 */     addToTierList(relic);
/* 373 */     greenList.add(relic);
/* 374 */     totalRelicCount++;
/*     */   }
/*     */   
/*     */   public static void addBlue(AbstractRelic relic) {
/* 378 */     if (UnlockTracker.isRelicSeen(relic.relicId)) {
/* 379 */       seenRelics++;
/*     */     }
/* 381 */     relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
/* 382 */     blueRelics.put(relic.relicId, relic);
/* 383 */     addToTierList(relic);
/* 384 */     blueList.add(relic);
/* 385 */     totalRelicCount++;
/*     */   }
/*     */   
/*     */   public static void addPurple(AbstractRelic relic) {
/* 389 */     if (UnlockTracker.isRelicSeen(relic.relicId)) {
/* 390 */       seenRelics++;
/*     */     }
/* 392 */     relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
/* 393 */     purpleRelics.put(relic.relicId, relic);
/* 394 */     addToTierList(relic);
/* 395 */     whiteList.add(relic);
/* 396 */     totalRelicCount++;
/*     */   }
/*     */   
/*     */   public static void addToTierList(AbstractRelic relic) {
/* 400 */     switch (relic.tier) {
/*     */       case null:
/* 402 */         starterList.add(relic);
/*     */         return;
/*     */       case IRONCLAD:
/* 405 */         commonList.add(relic);
/*     */         return;
/*     */       case THE_SILENT:
/* 408 */         uncommonList.add(relic);
/*     */         return;
/*     */       case DEFECT:
/* 411 */         rareList.add(relic);
/*     */         return;
/*     */       case null:
/* 414 */         shopList.add(relic);
/*     */         return;
/*     */       case null:
/* 417 */         specialList.add(relic);
/*     */         return;
/*     */       case WATCHER:
/* 420 */         bossList.add(relic);
/*     */         return;
/*     */       case null:
/* 423 */         logger.info(relic.relicId + " is deprecated.");
/*     */         return;
/*     */     } 
/* 426 */     logger.info(relic.relicId + " is undefined tier.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static AbstractRelic getRelic(String key) {
/* 432 */     if (sharedRelics.containsKey(key))
/* 433 */       return sharedRelics.get(key); 
/* 434 */     if (redRelics.containsKey(key))
/* 435 */       return redRelics.get(key); 
/* 436 */     if (greenRelics.containsKey(key))
/* 437 */       return greenRelics.get(key); 
/* 438 */     if (blueRelics.containsKey(key))
/* 439 */       return blueRelics.get(key); 
/* 440 */     if (purpleRelics.containsKey(key)) {
/* 441 */       return purpleRelics.get(key);
/*     */     }
/*     */     
/* 444 */     return (AbstractRelic)new Circlet();
/*     */   }
/*     */   
/*     */   public static boolean isARelic(String key) {
/* 448 */     return (sharedRelics.containsKey(key) || redRelics.containsKey(key) || greenRelics.containsKey(key) || blueRelics
/* 449 */       .containsKey(key) || purpleRelics.containsKey(key));
/*     */   }
/*     */   
/*     */   public static void populateRelicPool(ArrayList<String> pool, AbstractRelic.RelicTier tier, AbstractPlayer.PlayerClass c) {
/* 453 */     for (Map.Entry<String, AbstractRelic> r : sharedRelics.entrySet()) {
/* 454 */       if (((AbstractRelic)r.getValue()).tier == tier && (
/* 455 */         !UnlockTracker.isRelicLocked(r.getKey()) || Settings.treatEverythingAsUnlocked())) {
/* 456 */         pool.add(r.getKey());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 461 */     switch (c) {
/*     */       case IRONCLAD:
/* 463 */         for (Map.Entry<String, AbstractRelic> r : redRelics.entrySet()) {
/* 464 */           if (((AbstractRelic)r.getValue()).tier == tier && (
/* 465 */             !UnlockTracker.isRelicLocked(r.getKey()) || Settings.treatEverythingAsUnlocked())) {
/* 466 */             pool.add(r.getKey());
/*     */           }
/*     */         } 
/*     */         break;
/*     */       
/*     */       case THE_SILENT:
/* 472 */         for (Map.Entry<String, AbstractRelic> r : greenRelics.entrySet()) {
/* 473 */           if (((AbstractRelic)r.getValue()).tier == tier && (
/* 474 */             !UnlockTracker.isRelicLocked(r.getKey()) || Settings.treatEverythingAsUnlocked())) {
/* 475 */             pool.add(r.getKey());
/*     */           }
/*     */         } 
/*     */         break;
/*     */       
/*     */       case DEFECT:
/* 481 */         for (Map.Entry<String, AbstractRelic> r : blueRelics.entrySet()) {
/* 482 */           if (((AbstractRelic)r.getValue()).tier == tier && (
/* 483 */             !UnlockTracker.isRelicLocked(r.getKey()) || Settings.treatEverythingAsUnlocked())) {
/* 484 */             pool.add(r.getKey());
/*     */           }
/*     */         } 
/*     */         break;
/*     */       
/*     */       case WATCHER:
/* 490 */         for (Map.Entry<String, AbstractRelic> r : purpleRelics.entrySet()) {
/* 491 */           if (((AbstractRelic)r.getValue()).tier == tier && (
/* 492 */             !UnlockTracker.isRelicLocked(r.getKey()) || Settings.treatEverythingAsUnlocked())) {
/* 493 */             pool.add(r.getKey());
/*     */           }
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addSharedRelics(ArrayList<AbstractRelic> relicPool) {
/* 504 */     if (Settings.isDev) {
/* 505 */       logger.info("[RELIC] Adding " + sharedRelics.size() + " shared relics...");
/*     */     }
/* 507 */     for (Map.Entry<String, AbstractRelic> r : sharedRelics.entrySet()) {
/* 508 */       relicPool.add(r.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addClassSpecificRelics(ArrayList<AbstractRelic> relicPool) {
/* 513 */     switch (AbstractDungeon.player.chosenClass) {
/*     */       case IRONCLAD:
/* 515 */         if (Settings.isDev) {
/* 516 */           logger.info("[RELIC] Adding " + redRelics.size() + " red relics...");
/*     */         }
/* 518 */         for (Map.Entry<String, AbstractRelic> r : redRelics.entrySet()) {
/* 519 */           relicPool.add(r.getValue());
/*     */         }
/*     */         break;
/*     */       case THE_SILENT:
/* 523 */         if (Settings.isDev) {
/* 524 */           logger.info("[RELIC] Adding " + greenRelics.size() + " green relics...");
/*     */         }
/* 526 */         for (Map.Entry<String, AbstractRelic> r : greenRelics.entrySet()) {
/* 527 */           relicPool.add(r.getValue());
/*     */         }
/*     */         break;
/*     */       case DEFECT:
/* 531 */         if (Settings.isDev) {
/* 532 */           logger.info("[RELIC] Adding " + blueRelics.size() + " blue relics...");
/*     */         }
/* 534 */         for (Map.Entry<String, AbstractRelic> r : blueRelics.entrySet()) {
/* 535 */           relicPool.add(r.getValue());
/*     */         }
/*     */         break;
/*     */       case WATCHER:
/* 539 */         if (Settings.isDev) {
/* 540 */           logger.info("[RELIC] Adding " + purpleRelics.size() + " purple relics...");
/*     */         }
/* 542 */         for (Map.Entry<String, AbstractRelic> r : purpleRelics.entrySet()) {
/* 543 */           relicPool.add(r.getValue());
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void uploadRelicData() {
/* 552 */     ArrayList<String> data = new ArrayList<>();
/* 553 */     for (Map.Entry<String, AbstractRelic> r : sharedRelics.entrySet()) {
/* 554 */       data.add(((AbstractRelic)r.getValue()).gameDataUploadData("All"));
/*     */     }
/* 556 */     for (Map.Entry<String, AbstractRelic> r : redRelics.entrySet()) {
/* 557 */       data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Red"));
/*     */     }
/* 559 */     for (Map.Entry<String, AbstractRelic> r : greenRelics.entrySet()) {
/* 560 */       data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Green"));
/*     */     }
/* 562 */     for (Map.Entry<String, AbstractRelic> r : blueRelics.entrySet()) {
/* 563 */       data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Blue"));
/*     */     }
/* 565 */     for (Map.Entry<String, AbstractRelic> r : purpleRelics.entrySet()) {
/* 566 */       data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Purple"));
/*     */     }
/*     */     
/* 569 */     BotDataUploader.uploadDataAsync(BotDataUploader.GameDataType.RELIC_DATA, AbstractRelic.gameDataUploadHeader(), data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ArrayList<AbstractRelic> sortByName(ArrayList<AbstractRelic> group, boolean ascending) {
/* 580 */     ArrayList<AbstractRelic> tmp = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 584 */     for (AbstractRelic r : group) {
/* 585 */       int addIndex = 0;
/* 586 */       for (AbstractRelic r2 : tmp) {
/* 587 */         if (!ascending ? (
/* 588 */           r.name.compareTo(r2.name) < 0) : (
/*     */ 
/*     */ 
/*     */           
/* 592 */           r.name.compareTo(r2.name) > 0)) {
/*     */           break;
/*     */         }
/*     */         
/* 596 */         addIndex++;
/*     */       } 
/* 598 */       tmp.add(addIndex, r);
/*     */     } 
/* 600 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ArrayList<AbstractRelic> sortByStatus(ArrayList<AbstractRelic> group, boolean ascending) {
/* 611 */     ArrayList<AbstractRelic> tmp = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 616 */     for (AbstractRelic r : group) {
/* 617 */       int addIndex = 0;
/* 618 */       for (AbstractRelic r2 : tmp) {
/* 619 */         if (!ascending) {
/* 620 */           String a; String b; if (UnlockTracker.isRelicLocked(r.relicId)) {
/* 621 */             a = "LOCKED";
/* 622 */           } else if (UnlockTracker.isRelicSeen(r.relicId)) {
/* 623 */             a = "UNSEEN";
/*     */           } else {
/* 625 */             a = "SEEN";
/*     */           } 
/*     */           
/* 628 */           if (UnlockTracker.isRelicLocked(r2.relicId)) {
/* 629 */             b = "LOCKED";
/* 630 */           } else if (UnlockTracker.isRelicSeen(r2.relicId)) {
/* 631 */             b = "UNSEEN";
/*     */           } else {
/* 633 */             b = "SEEN";
/*     */           } 
/*     */           
/* 636 */           if (a.compareTo(b) > 0)
/*     */             break; 
/*     */         } else {
/*     */           String a; String b;
/* 640 */           if (UnlockTracker.isRelicLocked(r.relicId)) {
/* 641 */             a = "LOCKED";
/* 642 */           } else if (UnlockTracker.isRelicSeen(r.relicId)) {
/* 643 */             a = "UNSEEN";
/*     */           } else {
/* 645 */             a = "SEEN";
/*     */           } 
/*     */           
/* 648 */           if (UnlockTracker.isRelicLocked(r2.relicId)) {
/* 649 */             b = "LOCKED";
/* 650 */           } else if (UnlockTracker.isRelicSeen(r2.relicId)) {
/* 651 */             b = "UNSEEN";
/*     */           } else {
/* 653 */             b = "SEEN";
/*     */           } 
/*     */           
/* 656 */           if (a.compareTo(b) < 0) {
/*     */             break;
/*     */           }
/*     */         } 
/* 660 */         addIndex++;
/*     */       } 
/* 662 */       tmp.add(addIndex, r);
/*     */     } 
/* 664 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unlockAndSeeAllRelics() {
/* 672 */     for (String s : UnlockTracker.lockedRelics) {
/* 673 */       UnlockTracker.hardUnlockOverride(s);
/*     */     }
/*     */ 
/*     */     
/* 677 */     for (Map.Entry<String, AbstractRelic> r : sharedRelics.entrySet()) {
/* 678 */       UnlockTracker.markRelicAsSeen(r.getKey());
/*     */     }
/* 680 */     for (Map.Entry<String, AbstractRelic> r : redRelics.entrySet()) {
/* 681 */       UnlockTracker.markRelicAsSeen(r.getKey());
/*     */     }
/* 683 */     for (Map.Entry<String, AbstractRelic> r : greenRelics.entrySet()) {
/* 684 */       UnlockTracker.markRelicAsSeen(r.getKey());
/*     */     }
/* 686 */     for (Map.Entry<String, AbstractRelic> r : blueRelics.entrySet()) {
/* 687 */       UnlockTracker.markRelicAsSeen(r.getKey());
/*     */     }
/* 689 */     for (Map.Entry<String, AbstractRelic> r : purpleRelics.entrySet())
/* 690 */       UnlockTracker.markRelicAsSeen(r.getKey()); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\helpers\RelicLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */