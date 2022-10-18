/*    */ package com.megacrit.cardcrawl.integrations.microsoft;
/*    */ 
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.integrations.DistributorFactory;
/*    */ import com.megacrit.cardcrawl.integrations.PublisherIntegration;
/*    */ import com.megacrit.cardcrawl.screens.leaderboards.FilterButton;
/*    */ 
/*    */ public class MicrosoftIntegration
/*    */   implements PublisherIntegration
/*    */ {
/*    */   public boolean isInitialized() {
/* 12 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void dispose() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void deleteAllCloudFiles() {}
/*    */ 
/*    */   
/*    */   public boolean setStat(String id, int value) {
/* 25 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStat(String id) {
/* 30 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean incrementStat(String id, int incrementAmt) {
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getGlobalStat(String id) {
/* 40 */     return 0L;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uploadDailyLeaderboardScore(String name, int score) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uploadLeaderboardScore(String name, int score) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void unlockAchievement(String id) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void getLeaderboardEntries(AbstractPlayer.PlayerClass pClass, FilterButton.RegionSetting rSetting, FilterButton.LeaderboardType lType, int start, int end) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void getDailyLeaderboard(long date, int start, int end) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRichPresenceDisplayPlaying(int floor, String character) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRichPresenceDisplayPlaying(int floor, int ascension, String character) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRichPresenceDisplayInMenu() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumUnlockedAchievements() {
/* 88 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public DistributorFactory.Distributor getType() {
/* 93 */     return DistributorFactory.Distributor.MICROSOFT;
/*    */   }
/*    */   
/*    */   private void setRichPresenceData(String state, String details) {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\integrations\microsoft\MicrosoftIntegration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */