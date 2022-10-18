/*    */ package com.badlogic.gdx.graphics.g3d.decals;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.IntMap;
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
/*    */ public abstract class PluggableGroupStrategy
/*    */   implements GroupStrategy
/*    */ {
/* 25 */   private IntMap<GroupPlug> plugs = new IntMap();
/*    */ 
/*    */   
/*    */   public void beforeGroup(int group, Array<Decal> contents) {
/* 29 */     ((GroupPlug)this.plugs.get(group)).beforeGroup(contents);
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterGroup(int group) {
/* 34 */     ((GroupPlug)this.plugs.get(group)).afterGroup();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void plugIn(GroupPlug plug, int group) {
/* 41 */     this.plugs.put(group, plug);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GroupPlug unPlug(int group) {
/* 48 */     return (GroupPlug)this.plugs.remove(group);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\decals\PluggableGroupStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */