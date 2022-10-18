/*     */ package com.megacrit.cardcrawl.integrations.steam;
/*     */ 
/*     */ import com.codedisaster.steamworks.SteamAPI;
/*     */ import com.codedisaster.steamworks.SteamApps;
/*     */ import com.codedisaster.steamworks.SteamException;
/*     */ import com.codedisaster.steamworks.SteamFriends;
/*     */ import com.codedisaster.steamworks.SteamID;
/*     */ import com.codedisaster.steamworks.SteamLeaderboardHandle;
/*     */ import com.codedisaster.steamworks.SteamRemoteStorage;
/*     */ import com.codedisaster.steamworks.SteamUser;
/*     */ import com.codedisaster.steamworks.SteamUserStats;
/*     */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.daily.TimeHelper;
/*     */ import com.megacrit.cardcrawl.integrations.DistributorFactory;
/*     */ import com.megacrit.cardcrawl.integrations.PublisherIntegration;
/*     */ import com.megacrit.cardcrawl.screens.leaderboards.FilterButton;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
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
/*     */ public class SteamIntegration
/*     */   implements PublisherIntegration
/*     */ {
/*  37 */   private static final Logger logger = LogManager.getLogger(SteamIntegration.class.getName());
/*  38 */   private static String[] TEXT = null;
/*     */   static SteamUserStats steamStats;
/*     */   private static SteamUser steamUser;
/*     */   private static SteamApps steamApps;
/*     */   static SteamFriends steamFriends;
/*     */   private static Thread thread;
/*  44 */   static int accountId = -1;
/*     */ 
/*     */   
/*  47 */   static SteamLeaderboardHandle lbHandle = null;
/*  48 */   static LeaderboardTask task = null;
/*     */   private static boolean retrieveGlobal = true;
/*     */   static boolean gettingTime = false;
/*  51 */   private static int lbScore = 0; private static int startIndex = 0; private static int endIndex = 0;
/*     */   
/*     */   private static boolean isUploadingScore = false;
/*  54 */   private static Queue<StatTuple> statsToUpload = new LinkedList<>();
/*     */   
/*     */   enum LeaderboardTask {
/*  57 */     RETRIEVE, RETRIEVE_DAILY, UPLOAD, UPLOAD_DAILY;
/*     */   }
/*     */   
/*     */   private static class StatTuple {
/*     */     String stat;
/*     */     int score;
/*     */     
/*     */     StatTuple(String statName, int scoreVal) {
/*  65 */       this.stat = statName;
/*  66 */       this.score = scoreVal;
/*     */     }
/*     */   }
/*     */   
/*     */   public SteamIntegration() {
/*  71 */     if (!Settings.isDev || Settings.isModded) {
/*     */       try {
/*  73 */         SteamAPI.loadLibraries();
/*  74 */         if (SteamAPI.init()) {
/*  75 */           logger.info("[SUCCESS] Steam API initialized successfully.");
/*  76 */           steamStats = new SteamUserStats(new SSCallback(this));
/*  77 */           steamUser = new SteamUser(new SUCallback());
/*  78 */           steamApps = new SteamApps();
/*  79 */           steamFriends = new SteamFriends(new SFCallback());
/*     */           
/*  81 */           logger.info("BUILD ID: " + steamApps.getAppBuildId());
/*  82 */           logger.info("CURRENT LANG: " + steamApps.getCurrentGameLanguage());
/*  83 */           SteamID id = steamApps.getAppOwner();
/*     */           
/*  85 */           accountId = id.getAccountID();
/*  86 */           logger.info("ACCOUNT ID: " + accountId);
/*  87 */           thread = new Thread(new SteamTicker());
/*  88 */           thread.setName("SteamTicker");
/*  89 */           thread.start();
/*     */         } else {
/*  91 */           logger.info("[FAILURE] Steam API failed to initialize correctly.");
/*     */         } 
/*  93 */       } catch (SteamException e) {
/*  94 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*  97 */     if (SteamAPI.isSteamRunning()) {
/*  98 */       requestGlobalStats(365);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isInitialized() {
/* 103 */     return (steamUser != null && steamStats != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<String> getAllCloudFiles() {
/* 112 */     SteamRemoteStorage remoteStorage = new SteamRemoteStorage(new SRCallback());
/* 113 */     int numFiles = remoteStorage.getFileCount();
/* 114 */     logger.info("Num of files: " + numFiles);
/* 115 */     ArrayList<String> files = new ArrayList<>();
/* 116 */     for (int i = 0; i < numFiles; i++) {
/* 117 */       int[] sizes = new int[1];
/* 118 */       String file = remoteStorage.getFileNameAndSize(i, sizes);
/* 119 */       boolean exists = remoteStorage.fileExists(file);
/* 120 */       if (exists) {
/* 121 */         files.add(file);
/*     */       }
/* 123 */       logger.info("# " + i + " : name=" + file + ", size=" + sizes[0] + ", exists=" + (exists ? "yes" : "no"));
/*     */     } 
/* 125 */     remoteStorage.dispose();
/* 126 */     return files;
/*     */   }
/*     */   
/*     */   public void deleteAllCloudFiles() {
/* 130 */     deleteCloudFiles(getAllCloudFiles());
/* 131 */     logger.info("Deleted all Cloud Files");
/*     */   }
/*     */   
/*     */   private void deleteCloudFiles(ArrayList<String> files) {
/* 135 */     SteamRemoteStorage remoteStorage = new SteamRemoteStorage(new SRCallback());
/* 136 */     for (String file : files) {
/* 137 */       logger.info("Deleting file: " + file);
/* 138 */       remoteStorage.fileDelete(file);
/*     */     } 
/* 140 */     remoteStorage.dispose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String basename(String path) {
/* 148 */     Path p = Paths.get(path, new String[0]);
/* 149 */     return p.getFileName().toString();
/*     */   }
/*     */   
/*     */   public void unlockAchievement(String id) {
/* 153 */     logger.info("unlockAchievement: " + id);
/* 154 */     if (steamStats != null) {
/* 155 */       if (steamStats.setAchievement(id)) {
/* 156 */         steamStats.storeStats();
/*     */       } else {
/* 158 */         logger.info("[ERROR] Could not find achievement " + id);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeAllAchievementsBeCarefulNotToPush() {
/* 167 */     if (Settings.isDev && Settings.isBeta && 
/* 168 */       steamStats != null && 
/* 169 */       steamStats.resetAllStats(true)) {
/* 170 */       steamStats.storeStats();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean incrementStat(String id, int incrementAmt) {
/* 177 */     logger.info("incrementStat: " + id);
/* 178 */     if (steamStats != null) {
/* 179 */       if (steamStats.setStatI(id, getStat(id) + incrementAmt)) {
/* 180 */         return true;
/*     */       }
/* 182 */       logger.info("Stat: " + id + " not found.");
/* 183 */       return false;
/*     */     } 
/*     */     
/* 186 */     logger.info("[ERROR] Could not find stat " + id);
/*     */     
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public int getStat(String id) {
/* 192 */     logger.info("getStat: " + id);
/* 193 */     if (steamStats != null) {
/* 194 */       return steamStats.getStatI(id, 0);
/*     */     }
/* 196 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean setStat(String id, int value) {
/* 200 */     logger.info("setStat: " + id);
/* 201 */     if (steamStats != null) {
/* 202 */       if (steamStats.setStatI(id, value)) {
/* 203 */         logger.info(id + " stat set to " + value);
/* 204 */         return true;
/*     */       } 
/* 206 */       logger.info("Stat: " + id + " not found.");
/* 207 */       return false;
/*     */     } 
/*     */     
/* 210 */     logger.info("[ERROR] Could not find stat " + id);
/*     */     
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getGlobalStat(String id) {
/* 222 */     logger.info("getGlobalStat");
/* 223 */     if (steamStats != null) {
/* 224 */       return steamStats.getGlobalStat(id, 0L);
/*     */     }
/* 226 */     return -1L;
/*     */   }
/*     */   
/*     */   private static void requestGlobalStats(int i) {
/* 230 */     logger.info("requestGlobalStats");
/* 231 */     if (steamStats != null) {
/* 232 */       steamStats.requestGlobalStats(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getLeaderboardEntries(AbstractPlayer.PlayerClass pClass, FilterButton.RegionSetting rSetting, FilterButton.LeaderboardType lType, int start, int end) {
/* 243 */     task = LeaderboardTask.RETRIEVE;
/* 244 */     startIndex = start;
/* 245 */     endIndex = end;
/*     */     
/* 247 */     if (lType == FilterButton.LeaderboardType.FASTEST_WIN) {
/* 248 */       gettingTime = true;
/*     */     } else {
/* 250 */       gettingTime = false;
/*     */     } 
/*     */     
/* 253 */     if (rSetting == FilterButton.RegionSetting.GLOBAL) {
/* 254 */       retrieveGlobal = true;
/*     */     } else {
/* 256 */       retrieveGlobal = false;
/*     */     } 
/*     */     
/* 259 */     if (steamStats != null) {
/* 260 */       steamStats.findLeaderboard(createGetLeaderboardString(pClass, lType));
/*     */     }
/*     */   }
/*     */   
/*     */   public void getDailyLeaderboard(long date, int start, int end) {
/* 265 */     task = LeaderboardTask.RETRIEVE_DAILY;
/* 266 */     startIndex = start;
/* 267 */     endIndex = end;
/* 268 */     retrieveGlobal = true;
/* 269 */     gettingTime = false;
/*     */     
/* 271 */     if (steamStats != null) {
/* 272 */       StringBuilder leaderboardRetrieveString = new StringBuilder("DAILY_");
/* 273 */       leaderboardRetrieveString.append(Long.toString(date));
/* 274 */       if (Settings.isBeta) {
/* 275 */         leaderboardRetrieveString.append("_BETA");
/*     */       }
/* 277 */       steamStats.findOrCreateLeaderboard(leaderboardRetrieveString
/* 278 */           .toString(), SteamUserStats.LeaderboardSortMethod.Descending, SteamUserStats.LeaderboardDisplayType.Numeric);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String createGetLeaderboardString(AbstractPlayer.PlayerClass pClass, FilterButton.LeaderboardType lType) {
/* 286 */     String retVal = "";
/* 287 */     switch (pClass) {
/*     */       case AVG_FLOOR:
/* 289 */         retVal = retVal + "IRONCLAD";
/*     */         break;
/*     */       case AVG_SCORE:
/* 292 */         retVal = retVal + "SILENT";
/*     */         break;
/*     */       case CONSECUTIVE_WINS:
/* 295 */         retVal = retVal + "DEFECT";
/*     */         break;
/*     */       case FASTEST_WIN:
/* 298 */         retVal = retVal + "WATCHER";
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 304 */     switch (lType) {
/*     */       case AVG_FLOOR:
/* 306 */         retVal = retVal + "_AVG_FLOOR";
/*     */         break;
/*     */       case AVG_SCORE:
/* 309 */         retVal = retVal + "_AVG_SCORE";
/*     */         break;
/*     */       case CONSECUTIVE_WINS:
/* 312 */         retVal = retVal + "_CONSECUTIVE_WINS";
/*     */         break;
/*     */       case FASTEST_WIN:
/* 315 */         retVal = retVal + "_FASTEST_WIN";
/*     */         break;
/*     */       case HIGH_SCORE:
/* 318 */         retVal = retVal + "_HIGH_SCORE";
/*     */         break;
/*     */       case SPIRE_LEVEL:
/* 321 */         retVal = retVal + "_SPIRE_LEVEL";
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 327 */     if (Settings.isBeta) {
/* 328 */       retVal = retVal + "_BETA";
/*     */     }
/*     */     
/* 331 */     return retVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uploadLeaderboardScore(String name, int score) {
/* 342 */     if (steamUser == null || steamStats == null) {
/*     */       return;
/*     */     }
/*     */     
/* 346 */     if (isUploadingScore) {
/* 347 */       statsToUpload.add(new StatTuple(name, score));
/*     */     } else {
/* 349 */       logger.info(String.format("Uploading Steam Leaderboard score (%s: %d)", new Object[] { name, Integer.valueOf(score) }));
/* 350 */       isUploadingScore = true;
/* 351 */       task = LeaderboardTask.UPLOAD;
/* 352 */       lbScore = score;
/* 353 */       steamStats.findLeaderboard(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uploadDailyLeaderboardScore(String name, int score) {
/* 364 */     if (!TimeHelper.isOfflineMode()) {
/*     */       
/* 366 */       if (steamUser == null || steamStats == null) {
/* 367 */         logger.info("User is NOT connected to Steam, unable to upload daily score.");
/*     */         return;
/*     */       } 
/* 370 */       if (isUploadingScore) {
/* 371 */         statsToUpload.add(new StatTuple(name, score));
/*     */       } else {
/* 373 */         logger.info(String.format("Uploading [DAILY] Steam Leaderboard score (%s: %d)", new Object[] { name, Integer.valueOf(score) }));
/* 374 */         isUploadingScore = true;
/* 375 */         task = LeaderboardTask.UPLOAD_DAILY;
/* 376 */         lbScore = score;
/* 377 */         steamStats.findOrCreateLeaderboard(name, SteamUserStats.LeaderboardSortMethod.Descending, SteamUserStats.LeaderboardDisplayType.Numeric);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void didCompleteCallback(boolean success) {
/* 389 */     logger.info("didCompleteCallback");
/* 390 */     isUploadingScore = false;
/* 391 */     if (statsToUpload.size() > 0) {
/* 392 */       StatTuple uploadMe = statsToUpload.remove();
/* 393 */       uploadLeaderboardScore(uploadMe.stat, uploadMe.score);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void uploadLeaderboardHelper() {
/* 401 */     logger.info("uploadLeaderboardHelper");
/* 402 */     steamStats.uploadLeaderboardScore(lbHandle, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, lbScore, new int[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void uploadDailyLeaderboardHelper() {
/* 409 */     logger.info("uploadDailyLeaderboardHelper");
/* 410 */     steamStats.uploadLeaderboardScore(lbHandle, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, lbScore, new int[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void getLeaderboardEntryHelper() {
/* 418 */     if (task == LeaderboardTask.RETRIEVE) {
/* 419 */       if (retrieveGlobal) {
/* 420 */         logger.info("Downloading GLOBAL entries: " + startIndex + " - " + endIndex);
/* 421 */         if (CardCrawlGame.mainMenuScreen.leaderboardsScreen.viewMyScore) {
/* 422 */           steamStats.downloadLeaderboardEntries(lbHandle, SteamUserStats.LeaderboardDataRequest.GlobalAroundUser, -9, 10);
/* 423 */           CardCrawlGame.mainMenuScreen.leaderboardsScreen.viewMyScore = false;
/*     */         } else {
/* 425 */           steamStats.downloadLeaderboardEntries(lbHandle, SteamUserStats.LeaderboardDataRequest.Global, startIndex, endIndex);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 432 */         logger.info("Downloading FRIEND entries: " + startIndex + " - " + endIndex);
/* 433 */         steamStats.downloadLeaderboardEntries(lbHandle, SteamUserStats.LeaderboardDataRequest.Friends, startIndex, endIndex);
/*     */       }
/*     */     
/*     */     }
/* 437 */     else if (task == LeaderboardTask.RETRIEVE_DAILY) {
/* 438 */       if (CardCrawlGame.mainMenuScreen.dailyScreen.viewMyScore) {
/* 439 */         steamStats.downloadLeaderboardEntries(lbHandle, SteamUserStats.LeaderboardDataRequest.GlobalAroundUser, -9, 10);
/* 440 */         CardCrawlGame.mainMenuScreen.dailyScreen.viewMyScore = false;
/*     */       } else {
/* 442 */         logger.info("Downloading GLOBAL entries: " + startIndex + " - " + endIndex);
/* 443 */         steamStats.downloadLeaderboardEntries(lbHandle, SteamUserStats.LeaderboardDataRequest.Global, startIndex, endIndex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRichPresenceDisplayPlaying(int floor, int ascension, String character) {
/* 450 */     if (TEXT == null) {
/* 451 */       TEXT = (CardCrawlGame.languagePack.getUIString("RichPresence")).TEXT;
/*     */     }
/*     */     
/* 454 */     if (Settings.isDailyRun) {
/* 455 */       String msg = String.format(TEXT[0], new Object[] { Integer.valueOf(floor) });
/* 456 */       logger.debug("Setting Rich Presence: " + msg);
/* 457 */       setRichPresenceData("status", msg);
/* 458 */     } else if (Settings.isTrial) {
/* 459 */       String msg = String.format(TEXT[1], new Object[] { Integer.valueOf(floor) });
/* 460 */       logger.debug("Setting Rich Presence: " + msg);
/* 461 */       setRichPresenceData("status", msg);
/*     */     }
/* 463 */     else if (Settings.language == Settings.GameLanguage.ENG || Settings.language == Settings.GameLanguage.DEU || Settings.language == Settings.GameLanguage.THA || Settings.language == Settings.GameLanguage.TUR || Settings.language == Settings.GameLanguage.KOR || Settings.language == Settings.GameLanguage.RUS || Settings.language == Settings.GameLanguage.SPA || Settings.language == Settings.GameLanguage.DUT) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 468 */       String msg = String.format(TEXT[4] + character + TEXT[2], new Object[] { Integer.valueOf(ascension), Integer.valueOf(floor) });
/* 469 */       logger.debug("Setting Rich Presence: " + msg);
/* 470 */       setRichPresenceData("status", msg);
/*     */     } else {
/* 472 */       String msg = String.format(character + TEXT[2] + TEXT[4], new Object[] { Integer.valueOf(floor), Integer.valueOf(ascension) });
/* 473 */       logger.debug("Setting Rich Presence: " + msg);
/* 474 */       setRichPresenceData("status", msg);
/*     */     } 
/*     */     
/* 477 */     setRichPresenceData("steam_display", "#Status");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRichPresenceDisplayPlaying(int floor, String character) {
/* 482 */     if (TEXT == null) {
/* 483 */       TEXT = (CardCrawlGame.languagePack.getUIString("RichPresence")).TEXT;
/*     */     }
/*     */     
/* 486 */     if (Settings.isDailyRun) {
/* 487 */       String msg = String.format(TEXT[0], new Object[] { Integer.valueOf(floor) });
/* 488 */       logger.debug("Setting Rich Presence: " + msg);
/* 489 */       setRichPresenceData("status", msg);
/* 490 */     } else if (Settings.isTrial) {
/* 491 */       String msg = String.format(TEXT[1], new Object[] { Integer.valueOf(floor) });
/* 492 */       logger.debug("Setting Rich Presence: " + msg);
/* 493 */       setRichPresenceData("status", msg);
/*     */     } else {
/* 495 */       String msg = String.format(character + TEXT[2], new Object[] { Integer.valueOf(floor) });
/* 496 */       logger.debug("Setting Rich Presence: " + msg);
/* 497 */       setRichPresenceData("status", msg);
/*     */     } 
/* 499 */     setRichPresenceData("steam_display", "#Status");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRichPresenceDisplayInMenu() {
/* 504 */     if (TEXT == null) {
/* 505 */       TEXT = (CardCrawlGame.languagePack.getUIString("RichPresence")).TEXT;
/*     */     }
/*     */     
/* 508 */     logger.debug("Setting Rich Presence: " + String.format(TEXT[3], new Object[0]));
/* 509 */     setRichPresenceData("status", TEXT[3]);
/* 510 */     setRichPresenceData("steam_display", "#Status");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumUnlockedAchievements() {
/* 515 */     int retVal = 0;
/* 516 */     ArrayList<String> keys = new ArrayList<>();
/* 517 */     keys.add("ADRENALINE");
/* 518 */     keys.add("ASCEND_0");
/* 519 */     keys.add("ASCEND_10");
/* 520 */     keys.add("ASCEND_20");
/* 521 */     keys.add("AUTOMATON");
/* 522 */     keys.add("BARRICADED");
/* 523 */     keys.add("CATALYST");
/* 524 */     keys.add("CHAMP");
/* 525 */     keys.add("COLLECTOR");
/* 526 */     keys.add("COME_AT_ME");
/* 527 */     keys.add("COMMON_SENSE");
/* 528 */     keys.add("CROW");
/* 529 */     keys.add("DONUT");
/* 530 */     keys.add("EMERALD");
/* 531 */     keys.add("EMERALD_PLUS");
/* 532 */     keys.add("FOCUSED");
/* 533 */     keys.add("GHOST_GUARDIAN");
/* 534 */     keys.add("GUARDIAN");
/* 535 */     keys.add("IMPERVIOUS");
/* 536 */     keys.add("INFINITY");
/* 537 */     keys.add("JAXXED");
/* 538 */     keys.add("LUCKY_DAY");
/* 539 */     keys.add("MINIMALIST");
/* 540 */     keys.add("NEON");
/* 541 */     keys.add("NINJA");
/* 542 */     keys.add("ONE_RELIC");
/* 543 */     keys.add("PERFECT");
/* 544 */     keys.add("PLAGUE");
/* 545 */     keys.add("POWERFUL");
/* 546 */     keys.add("PURITY");
/* 547 */     keys.add("RUBY");
/* 548 */     keys.add("RUBY_PLUS");
/* 549 */     keys.add("SAPPHIRE");
/* 550 */     keys.add("SAPPHIRE_PLUS");
/* 551 */     keys.add("AMETHYST");
/* 552 */     keys.add("AMETHYST_PLUS");
/* 553 */     keys.add("SHAPES");
/* 554 */     keys.add("SHRUG_IT_OFF");
/* 555 */     keys.add("SLIME_BOSS");
/* 556 */     keys.add("SPEED_CLIMBER");
/* 557 */     keys.add("THE_ENDING");
/* 558 */     keys.add("THE_PACT");
/* 559 */     keys.add("TIME_EATER");
/* 560 */     keys.add("TRANSIENT");
/* 561 */     keys.add("YOU_ARE_NOTHING");
/*     */     
/* 563 */     for (String s : keys) {
/* 564 */       if (steamStats.isAchieved(s, false)) {
/* 565 */         retVal++;
/*     */       }
/*     */     } 
/*     */     
/* 569 */     return retVal;
/*     */   }
/*     */ 
/*     */   
/*     */   public DistributorFactory.Distributor getType() {
/* 574 */     return DistributorFactory.Distributor.STEAM;
/*     */   }
/*     */   
/*     */   private void setRichPresenceData(String key, String value) {
/* 578 */     if (steamFriends != null && 
/* 579 */       !steamFriends.setRichPresence(key, value)) {
/* 580 */       logger.info("Failed to set Steam Rich Presence: key=" + key + " value=" + value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 586 */     if (isInitialized())
/* 587 */       SteamAPI.shutdown(); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\integrations\steam\SteamIntegration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */