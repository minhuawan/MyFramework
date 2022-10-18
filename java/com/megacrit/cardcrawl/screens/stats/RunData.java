/*    */ package com.megacrit.cardcrawl.screens.stats;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RunData
/*    */ {
/*    */   public String character_chosen;
/*    */   public String loadout;
/*    */   public String build_version;
/*    */   public String seed_played;
/*    */   public boolean chose_seed;
/*    */   public String timestamp;
/*    */   public String local_time;
/*    */   public boolean victory;
/*    */   public boolean is_daily;
/*    */   public boolean is_trial;
/*    */   public boolean is_endless;
/*    */   public boolean is_ascension_mode;
/*    */   public boolean is_special_run;
/*    */   public Long special_seed;
/*    */   public boolean isUploaded;
/*    */   public int score;
/*    */   public int ascension_level;
/*    */   public int floor_reached;
/*    */   public int gold;
/*    */   public int playtime;
/*    */   public int purchased_purges;
/*    */   public String killed_by;
/*    */   public String neow_bonus;
/*    */   public String neow_cost;
/*    */   public int rested;
/*    */   public int rituals;
/*    */   public int upgraded;
/*    */   public int meditates;
/*    */   public List<String> master_deck;
/*    */   public List<String> relics;
/*    */   public int circlet_count;
/*    */   public List<String> path_taken;
/*    */   public List<String> path_per_floor;
/*    */   public List<Integer> current_hp_per_floor;
/*    */   public List<Integer> max_hp_per_floor;
/*    */   public List<String> items_purchased;
/*    */   public List<Integer> item_purchase_floors;
/*    */   public List<String> items_purged;
/*    */   public List<Integer> items_purged_floors;
/*    */   public List<Integer> gold_per_floor;
/*    */   public List<String> daily_mods;
/*    */   public List<BattleStats> damage_taken;
/*    */   public List<EventStats> event_choices;
/*    */   public List<CardChoiceStats> card_choices;
/*    */   public List<ObtainStats> relics_obtained;
/*    */   public List<ObtainStats> potions_obtained;
/*    */   public List<BossRelicChoiceStats> boss_relics;
/*    */   public List<CampfireChoice> campfire_choices;
/*    */   public static Comparator<RunData> orderByTimestampDesc;
/*    */   
/*    */   static {
/* 75 */     orderByTimestampDesc = ((o1, o2) -> o2.timestamp.compareTo(o1.timestamp));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\screens\stats\RunData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */