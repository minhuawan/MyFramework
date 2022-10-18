/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import java.awt.CardLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ 
/*    */ 
/*    */ public class CustomCardLayout
/*    */   extends CardLayout
/*    */ {
/*    */   public Dimension preferredLayoutSize(Container parent) {
/* 13 */     Component component = getCurrentCard(parent);
/* 14 */     return (component != null) ? component.getPreferredSize() : super.preferredLayoutSize(parent);
/*    */   }
/*    */   
/*    */   public <K> K getCurrentCard(Container container) {
/* 18 */     Component[] c = container.getComponents();
/* 19 */     int i = 0;
/* 20 */     int j = c.length;
/* 21 */     while (i < j) {
/* 22 */       if (c[i].isVisible()) {
/* 23 */         return (K)c[i];
/*    */       }
/*    */       
/* 26 */       i++;
/*    */     } 
/* 28 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\CustomCardLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */