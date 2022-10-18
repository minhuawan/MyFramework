/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Clipboard;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.ClipboardOwner;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import java.awt.datatransfer.Transferable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LwjglClipboard
/*    */   implements Clipboard, ClipboardOwner
/*    */ {
/*    */   public String getContents() {
/* 34 */     String result = "";
/*    */     try {
/* 36 */       Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 37 */       Transferable contents = clipboard.getContents(null);
/* 38 */       boolean hasTransferableText = (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor));
/* 39 */       if (hasTransferableText) {
/*    */         try {
/* 41 */           result = (String)contents.getTransferData(DataFlavor.stringFlavor);
/* 42 */         } catch (Exception exception) {}
/*    */       }
/*    */     }
/* 45 */     catch (Exception exception) {}
/*    */     
/* 47 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setContents(String content) {
/*    */     try {
/* 53 */       StringSelection stringSelection = new StringSelection(content);
/* 54 */       Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 55 */       clipboard.setContents(stringSelection, this);
/* 56 */     } catch (Exception exception) {}
/*    */   }
/*    */   
/*    */   public void lostOwnership(Clipboard arg0, Transferable arg1) {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglClipboard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */