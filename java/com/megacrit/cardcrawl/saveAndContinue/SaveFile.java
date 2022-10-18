/*     */ package com.megacrit.cardcrawl.saveAndContinue;
/*     */ 
/*     */ import com.megacrit.cardcrawl.blights.AbstractBlight;
/*     */ import com.megacrit.cardcrawl.cards.CardSave;
/*     */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.helpers.CardHelper;
/*     */ import com.megacrit.cardcrawl.helpers.EventHelper;
/*     */ import com.megacrit.cardcrawl.helpers.ModHelper;
/*     */ import com.megacrit.cardcrawl.potions.AbstractPotion;
/*     */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*     */ import com.megacrit.cardcrawl.relics.BottledFlame;
/*     */ import com.megacrit.cardcrawl.relics.BottledLightning;
/*     */ import com.megacrit.cardcrawl.relics.BottledTornado;
/*     */ import com.megacrit.cardcrawl.rewards.RewardItem;
/*     */ import com.megacrit.cardcrawl.rewards.RewardSave;
/*     */ import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*     */ import com.megacrit.cardcrawl.shop.ShopScreen;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class SaveFile
/*     */ {
/*  30 */   private static final Logger logger = LogManager.getLogger(SaveFile.class.getName());
/*     */   
/*     */   public String name;
/*     */   
/*     */   public String loadout;
/*     */   
/*     */   public int current_health;
/*     */   
/*     */   public int max_health;
/*     */   
/*     */   public int max_orbs;
/*     */   
/*     */   public int gold;
/*     */   
/*     */   public int hand_size;
/*     */   
/*     */   public int potion_slots;
/*     */   
/*     */   public int red;
/*     */   
/*     */   public int green;
/*     */   
/*     */   public int blue;
/*     */   
/*     */   public ArrayList<CardSave> cards;
/*     */   
/*     */   public HashMap<String, Integer> obtained_cards;
/*     */   
/*     */   public ArrayList<String> relics;
/*     */   
/*     */   public ArrayList<Integer> relic_counters;
/*     */   public ArrayList<String> blights;
/*     */   public ArrayList<Integer> blight_counters;
/*     */   public ArrayList<String> potions;
/*     */   public boolean is_ascension_mode;
/*     */   public int ascension_level;
/*     */   public boolean chose_neow_reward;
/*     */   public String level_name;
/*     */   public long play_time;
/*     */   public long save_date;
/*     */   public long daily_date;
/*     */   public int floor_num;
/*     */   public int act_num;
/*     */   public long seed;
/*     */   public long special_seed;
/*     */   public boolean seed_set;
/*     */   public boolean is_trial;
/*     */   public boolean is_daily;
/*     */   public boolean is_final_act_on;
/*     */   public boolean has_ruby_key;
/*     */   public boolean has_emerald_key;
/*     */   public boolean has_sapphire_key;
/*     */   public ArrayList<String> custom_mods;
/*     */   public ArrayList<String> daily_mods;
/*     */   public int monster_seed_count;
/*     */   public int event_seed_count;
/*     */   public int merchant_seed_count;
/*     */   public int card_seed_count;
/*     */   public int treasure_seed_count;
/*     */   public int relic_seed_count;
/*     */   public int potion_seed_count;
/*     */   public int monster_hp_seed_count;
/*     */   public int ai_seed_count;
/*     */   public int shuffle_seed_count;
/*     */   public int card_random_seed_count;
/*     */   public int card_random_seed_randomizer;
/*     */   public int potion_chance;
/*     */   public int purgeCost;
/*     */   public ArrayList<String> monster_list;
/*     */   public ArrayList<String> elite_monster_list;
/*     */   public ArrayList<String> boss_list;
/*     */   public ArrayList<String> event_list;
/*     */   public ArrayList<String> one_time_event_list;
/*     */   public ArrayList<Float> event_chances;
/*     */   public ArrayList<Integer> path_x;
/*     */   public ArrayList<Integer> path_y;
/*     */   public int room_x;
/*     */   public int room_y;
/*     */   public int spirit_count;
/*     */   public String boss;
/*     */   public String current_room;
/*     */   public ArrayList<String> common_relics;
/*     */   public ArrayList<String> uncommon_relics;
/*     */   public ArrayList<String> rare_relics;
/*     */   public ArrayList<String> shop_relics;
/*     */   public ArrayList<String> boss_relics;
/*     */   public String bottled_flame;
/*     */   public String bottled_lightning;
/*     */   public String bottled_tornado;
/*     */   public int bottled_flame_upgrade;
/*     */   public int bottled_lightning_upgrade;
/*     */   public int bottled_tornado_upgrade;
/*     */   public int bottled_flame_misc;
/*     */   public int bottled_lightning_misc;
/*     */   public int bottled_tornado_misc;
/*     */   public boolean is_endless_mode;
/*     */   public ArrayList<Integer> endless_increments;
/*     */   public boolean post_combat;
/*     */   public boolean mugged;
/*     */   public boolean smoked;
/*     */   public ArrayList<RewardSave> combat_rewards;
/*     */   public int monsters_killed;
/*     */   public int elites1_killed;
/*     */   public int elites2_killed;
/*     */   public int elites3_killed;
/*     */   public int champions;
/*     */   public int perfect;
/*     */   public boolean overkill;
/*     */   public boolean combo;
/*     */   public boolean cheater;
/*     */   public int gold_gained;
/*     */   public int mystery_machine;
/*     */   public int metric_campfire_rested;
/*     */   public int metric_campfire_upgraded;
/*     */   public int metric_campfire_rituals;
/*     */   public int metric_campfire_meditates;
/*     */   public int metric_purchased_purges;
/*     */   public ArrayList<Integer> metric_potions_floor_spawned;
/*     */   public ArrayList<Integer> metric_potions_floor_usage;
/*     */   public ArrayList<Integer> metric_current_hp_per_floor;
/*     */   public ArrayList<Integer> metric_max_hp_per_floor;
/*     */   public ArrayList<Integer> metric_gold_per_floor;
/*     */   public ArrayList<String> metric_path_per_floor;
/*     */   public ArrayList<String> metric_path_taken;
/*     */   public ArrayList<String> metric_items_purchased;
/*     */   public ArrayList<Integer> metric_item_purchase_floors;
/*     */   public ArrayList<String> metric_items_purged;
/*     */   public ArrayList<Integer> metric_items_purged_floors;
/*     */   public ArrayList<HashMap> metric_card_choices;
/*     */   public ArrayList<HashMap> metric_event_choices;
/*     */   public ArrayList<HashMap> metric_boss_relics;
/*     */   public ArrayList<HashMap> metric_damage_taken;
/*     */   public ArrayList<HashMap> metric_potions_obtained;
/*     */   public ArrayList<HashMap> metric_relics_obtained;
/*     */   public ArrayList<HashMap> metric_campfire_choices;
/*     */   public String metric_build_version;
/*     */   public String metric_seed_played;
/*     */   public int metric_floor_reached;
/*     */   public long metric_playtime;
/*     */   public String neow_bonus;
/*     */   public String neow_cost;
/*     */   
/*     */   public enum SaveType
/*     */   {
/* 174 */     ENTER_ROOM, POST_NEOW, POST_COMBAT, AFTER_BOSS_RELIC, ENDLESS_NEOW;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SaveFile() {}
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SaveFile(SaveType type) {
/* 229 */     AbstractPlayer p = AbstractDungeon.player;
/* 230 */     this.name = p.name;
/* 231 */     this.current_health = p.currentHealth;
/* 232 */     this.max_health = p.maxHealth;
/* 233 */     this.max_orbs = p.masterMaxOrbs;
/* 234 */     this.gold = p.gold;
/* 235 */     this.hand_size = p.masterHandSize;
/* 236 */     this.red = p.energy.energyMaster;
/* 237 */     this.green = 0;
/* 238 */     this.blue = 0;
/*     */ 
/*     */     
/* 241 */     this.monsters_killed = CardCrawlGame.monstersSlain;
/* 242 */     this.elites1_killed = CardCrawlGame.elites1Slain;
/* 243 */     this.elites2_killed = CardCrawlGame.elites2Slain;
/* 244 */     this.elites3_killed = CardCrawlGame.elites3Slain;
/* 245 */     this.champions = CardCrawlGame.champion;
/* 246 */     this.perfect = CardCrawlGame.perfect;
/* 247 */     this.overkill = CardCrawlGame.overkill;
/* 248 */     this.combo = CardCrawlGame.combo;
/* 249 */     this.cheater = CardCrawlGame.cheater;
/* 250 */     this.gold_gained = CardCrawlGame.goldGained;
/* 251 */     this.mystery_machine = CardCrawlGame.mysteryMachine;
/* 252 */     this.play_time = (long)CardCrawlGame.playtime;
/*     */ 
/*     */     
/* 255 */     this.cards = p.masterDeck.getCardDeck();
/* 256 */     this.obtained_cards = CardHelper.obtainedCards;
/*     */ 
/*     */     
/* 259 */     this.relics = new ArrayList<>();
/* 260 */     this.relic_counters = new ArrayList<>();
/* 261 */     for (AbstractRelic r : p.relics) {
/* 262 */       this.relics.add(r.relicId);
/* 263 */       this.relic_counters.add(Integer.valueOf(r.counter));
/*     */     } 
/*     */ 
/*     */     
/* 267 */     this.is_endless_mode = Settings.isEndless;
/*     */ 
/*     */     
/* 270 */     this.blights = new ArrayList<>();
/* 271 */     this.blight_counters = new ArrayList<>();
/* 272 */     for (AbstractBlight b : p.blights) {
/* 273 */       this.blights.add(b.blightID);
/* 274 */       this.blight_counters.add(Integer.valueOf(b.counter));
/*     */     } 
/*     */     
/* 277 */     this.endless_increments = new ArrayList<>();
/* 278 */     for (AbstractBlight b : p.blights) {
/* 279 */       this.endless_increments.add(Integer.valueOf(b.increment));
/*     */     }
/*     */ 
/*     */     
/* 283 */     this.potion_slots = AbstractDungeon.player.potionSlots;
/* 284 */     this.potions = new ArrayList<>();
/* 285 */     for (AbstractPotion pot : AbstractDungeon.player.potions) {
/* 286 */       this.potions.add(pot.ID);
/*     */     }
/*     */ 
/*     */     
/* 290 */     this.is_ascension_mode = AbstractDungeon.isAscensionMode;
/* 291 */     this.ascension_level = AbstractDungeon.ascensionLevel;
/* 292 */     this.chose_neow_reward = false;
/* 293 */     this.level_name = AbstractDungeon.id;
/* 294 */     this.floor_num = AbstractDungeon.floorNum;
/* 295 */     this.act_num = AbstractDungeon.actNum;
/* 296 */     this.monster_list = AbstractDungeon.monsterList;
/* 297 */     this.elite_monster_list = AbstractDungeon.eliteMonsterList;
/* 298 */     this.boss_list = AbstractDungeon.bossList;
/* 299 */     this.event_list = AbstractDungeon.eventList;
/* 300 */     this.one_time_event_list = AbstractDungeon.specialOneTimeEventList;
/* 301 */     this.potion_chance = AbstractRoom.blizzardPotionMod;
/* 302 */     this.event_chances = (type == SaveType.POST_COMBAT) ? EventHelper.getChancesPreRoll() : EventHelper.getChances();
/* 303 */     this.save_date = Calendar.getInstance().getTimeInMillis();
/* 304 */     this.seed = Settings.seed.longValue();
/* 305 */     if (Settings.specialSeed != null) {
/* 306 */       this.special_seed = Settings.specialSeed.longValue();
/*     */     }
/* 308 */     this.seed_set = Settings.seedSet;
/* 309 */     this.is_daily = Settings.isDailyRun;
/* 310 */     this.is_final_act_on = Settings.isFinalActAvailable;
/* 311 */     this.has_ruby_key = Settings.hasRubyKey;
/* 312 */     this.has_emerald_key = Settings.hasEmeraldKey;
/* 313 */     this.has_sapphire_key = Settings.hasSapphireKey;
/* 314 */     this.daily_date = Settings.dailyDate;
/* 315 */     this.is_trial = Settings.isTrial;
/* 316 */     this.daily_mods = ModHelper.getEnabledModIDs();
/*     */     
/* 318 */     if (AbstractPlayer.customMods == null) {
/* 319 */       if (CardCrawlGame.trial != null) {
/* 320 */         AbstractPlayer.customMods = CardCrawlGame.trial.dailyModIDs();
/*     */       } else {
/* 322 */         AbstractPlayer.customMods = new ArrayList();
/*     */       } 
/*     */     }
/*     */     
/* 326 */     this.custom_mods = AbstractPlayer.customMods;
/*     */     
/* 328 */     this.boss = AbstractDungeon.bossKey;
/* 329 */     this.purgeCost = ShopScreen.purgeCost;
/* 330 */     this.monster_seed_count = AbstractDungeon.monsterRng.counter;
/* 331 */     this.event_seed_count = AbstractDungeon.eventRng.counter;
/* 332 */     this.merchant_seed_count = AbstractDungeon.merchantRng.counter;
/* 333 */     this.card_seed_count = AbstractDungeon.cardRng.counter;
/* 334 */     this.card_random_seed_randomizer = AbstractDungeon.cardBlizzRandomizer;
/* 335 */     this.treasure_seed_count = AbstractDungeon.treasureRng.counter;
/* 336 */     this.relic_seed_count = AbstractDungeon.relicRng.counter;
/* 337 */     this.potion_seed_count = AbstractDungeon.potionRng.counter;
/* 338 */     this.path_x = AbstractDungeon.pathX;
/* 339 */     this.path_y = AbstractDungeon.pathY;
/*     */ 
/*     */     
/* 342 */     if (AbstractDungeon.nextRoom == null || type == SaveType.ENDLESS_NEOW) {
/* 343 */       this.room_x = (AbstractDungeon.getCurrMapNode()).x;
/* 344 */       this.room_y = (AbstractDungeon.getCurrMapNode()).y;
/* 345 */       this.current_room = AbstractDungeon.getCurrRoom().getClass().getName();
/*     */     } else {
/* 347 */       this.room_x = AbstractDungeon.nextRoom.x;
/* 348 */       this.room_y = AbstractDungeon.nextRoom.y;
/* 349 */       this.current_room = AbstractDungeon.nextRoom.room.getClass().getName();
/*     */     } 
/*     */     
/* 352 */     this.spirit_count = AbstractDungeon.bossCount;
/* 353 */     logger.info("Next Room: " + this.current_room);
/* 354 */     this.common_relics = AbstractDungeon.commonRelicPool;
/* 355 */     this.uncommon_relics = AbstractDungeon.uncommonRelicPool;
/* 356 */     this.rare_relics = AbstractDungeon.rareRelicPool;
/* 357 */     this.shop_relics = AbstractDungeon.shopRelicPool;
/* 358 */     this.boss_relics = AbstractDungeon.bossRelicPool;
/* 359 */     this.post_combat = false;
/* 360 */     this.mugged = false;
/* 361 */     this.smoked = false;
/* 362 */     switch (type) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case POST_COMBAT:
/* 368 */         this.post_combat = true;
/* 369 */         this.mugged = (AbstractDungeon.getCurrRoom()).mugged;
/* 370 */         this.smoked = (AbstractDungeon.getCurrRoom()).smoked;
/* 371 */         this.combat_rewards = new ArrayList<>();
/*     */         
/* 373 */         for (RewardItem i : (AbstractDungeon.getCurrRoom()).rewards) {
/* 374 */           switch (i.type) {
/*     */ 
/*     */             
/*     */             case AFTER_BOSS_RELIC:
/*     */             case ENTER_ROOM:
/*     */             case POST_COMBAT:
/* 380 */               this.combat_rewards.add(new RewardSave(i.type.toString(), null));
/*     */             
/*     */             case POST_NEOW:
/* 383 */               this.combat_rewards.add(new RewardSave(i.type.toString(), null, i.goldAmt, i.bonusGold));
/*     */             
/*     */             case null:
/* 386 */               this.combat_rewards.add(new RewardSave(i.type.toString(), i.potion.ID));
/*     */             
/*     */             case null:
/* 389 */               this.combat_rewards.add(new RewardSave(i.type.toString(), i.relic.relicId));
/*     */             
/*     */             case null:
/* 392 */               this.combat_rewards.add(new RewardSave(i.type.toString(), null, i.goldAmt, 0));
/*     */           } 
/*     */ 
/*     */         
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case POST_NEOW:
/* 401 */         this.chose_neow_reward = true;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 408 */     if (AbstractDungeon.player.hasRelic("Bottled Flame")) {
/* 409 */       if (((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).card != null) {
/* 410 */         this.bottled_flame = ((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).card.cardID;
/*     */       } else {
/* 412 */         this.bottled_flame = null;
/*     */       } 
/*     */     } else {
/* 415 */       this.bottled_flame = null;
/*     */     } 
/*     */ 
/*     */     
/* 419 */     if (AbstractDungeon.player.hasRelic("Bottled Lightning")) {
/* 420 */       if (((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).card != null) {
/* 421 */         this.bottled_lightning = ((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).card.cardID;
/*     */       } else {
/*     */         
/* 424 */         this.bottled_lightning = null;
/*     */       } 
/*     */     } else {
/* 427 */       this.bottled_lightning = null;
/*     */     } 
/*     */ 
/*     */     
/* 431 */     if (AbstractDungeon.player.hasRelic("Bottled Tornado")) {
/* 432 */       if (((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).card != null) {
/* 433 */         this.bottled_tornado = ((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).card.cardID;
/*     */       } else {
/* 435 */         this.bottled_tornado = null;
/*     */       } 
/*     */     } else {
/* 438 */       this.bottled_tornado = null;
/*     */     } 
/*     */ 
/*     */     
/* 442 */     this.metric_campfire_rested = CardCrawlGame.metricData.campfire_rested;
/* 443 */     this.metric_campfire_upgraded = CardCrawlGame.metricData.campfire_upgraded;
/* 444 */     this.metric_purchased_purges = CardCrawlGame.metricData.purchased_purges;
/* 445 */     this.metric_potions_floor_spawned = CardCrawlGame.metricData.potions_floor_spawned;
/* 446 */     this.metric_potions_floor_usage = CardCrawlGame.metricData.potions_floor_usage;
/* 447 */     this.metric_current_hp_per_floor = CardCrawlGame.metricData.current_hp_per_floor;
/* 448 */     this.metric_max_hp_per_floor = CardCrawlGame.metricData.max_hp_per_floor;
/* 449 */     this.metric_gold_per_floor = CardCrawlGame.metricData.gold_per_floor;
/* 450 */     this.metric_path_per_floor = CardCrawlGame.metricData.path_per_floor;
/* 451 */     this.metric_path_taken = CardCrawlGame.metricData.path_taken;
/* 452 */     this.metric_items_purchased = CardCrawlGame.metricData.items_purchased;
/* 453 */     this.metric_item_purchase_floors = CardCrawlGame.metricData.item_purchase_floors;
/* 454 */     this.metric_items_purged = CardCrawlGame.metricData.items_purged;
/* 455 */     this.metric_items_purged_floors = CardCrawlGame.metricData.items_purged_floors;
/* 456 */     this.metric_card_choices = CardCrawlGame.metricData.card_choices;
/* 457 */     this.metric_event_choices = CardCrawlGame.metricData.event_choices;
/* 458 */     this.metric_boss_relics = CardCrawlGame.metricData.boss_relics;
/* 459 */     this.metric_potions_obtained = CardCrawlGame.metricData.potions_obtained;
/* 460 */     this.metric_relics_obtained = CardCrawlGame.metricData.relics_obtained;
/* 461 */     this.metric_campfire_choices = CardCrawlGame.metricData.campfire_choices;
/* 462 */     this.metric_damage_taken = CardCrawlGame.metricData.damage_taken;
/* 463 */     this.metric_build_version = CardCrawlGame.TRUE_VERSION_NUM;
/* 464 */     this.metric_seed_played = Settings.seed.toString();
/* 465 */     this.metric_floor_reached = AbstractDungeon.floorNum;
/* 466 */     this.metric_playtime = (long)CardCrawlGame.playtime;
/* 467 */     this.neow_bonus = CardCrawlGame.metricData.neowBonus;
/* 468 */     this.neow_cost = CardCrawlGame.metricData.neowCost;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\saveAndContinue\SaveFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */