/*    */ package com.megacrit.cardcrawl.trials;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
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
/*    */ public class StarterDeckTrial
/*    */   extends AbstractTrial
/*    */ {
/*    */   public List<String> extraStartingRelicIDs() {
/* 19 */     return Collections.singletonList("Busted Crown");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ArrayList<String> dailyModIDs() {
/* 29 */     ArrayList<String> retVal = new ArrayList<>();
/* 30 */     retVal.add("Binary");
/* 31 */     return retVal;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\trials\StarterDeckTrial.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */