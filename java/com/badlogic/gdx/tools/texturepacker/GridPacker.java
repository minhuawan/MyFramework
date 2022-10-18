/*    */ package com.badlogic.gdx.tools.texturepacker;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ 
/*    */ 
/*    */ public class GridPacker
/*    */   implements TexturePacker.Packer
/*    */ {
/*    */   private TexturePacker.Settings settings;
/*    */   
/*    */   public GridPacker(TexturePacker.Settings settings) {
/* 32 */     this.settings = settings;
/*    */   }
/*    */   
/*    */   public Array<TexturePacker.Page> pack(Array<TexturePacker.Rect> inputRects) {
/* 36 */     if (!this.settings.silent) System.out.print("Packing");
/*    */     
/* 38 */     int cellWidth = 0, cellHeight = 0;
/* 39 */     for (int i = 0, nn = inputRects.size; i < nn; i++) {
/* 40 */       TexturePacker.Rect rect = (TexturePacker.Rect)inputRects.get(i);
/* 41 */       cellWidth = Math.max(cellWidth, rect.width);
/* 42 */       cellHeight = Math.max(cellHeight, rect.height);
/*    */     } 
/* 44 */     cellWidth += this.settings.paddingX;
/* 45 */     cellHeight += this.settings.paddingY;
/*    */     
/* 47 */     inputRects.reverse();
/*    */     
/* 49 */     Array<TexturePacker.Page> pages = new Array();
/* 50 */     while (inputRects.size > 0) {
/* 51 */       TexturePacker.Page result = packPage(inputRects, cellWidth, cellHeight);
/* 52 */       pages.add(result);
/*    */     } 
/* 54 */     return pages;
/*    */   }
/*    */   
/*    */   private TexturePacker.Page packPage(Array<TexturePacker.Rect> inputRects, int cellWidth, int cellHeight) {
/* 58 */     TexturePacker.Page page = new TexturePacker.Page();
/* 59 */     page.outputRects = new Array();
/*    */     
/* 61 */     int maxWidth = this.settings.maxWidth, maxHeight = this.settings.maxHeight;
/* 62 */     if (this.settings.edgePadding) {
/* 63 */       maxWidth -= this.settings.paddingX;
/* 64 */       maxHeight -= this.settings.paddingY;
/*    */     } 
/* 66 */     int x = 0, y = 0; int i;
/* 67 */     for (i = inputRects.size - 1; i >= 0; i--) {
/* 68 */       if (x + cellWidth > maxWidth) {
/* 69 */         y += cellHeight;
/* 70 */         if (y > maxHeight - cellHeight)
/* 71 */           break;  x = 0;
/*    */       } 
/* 73 */       TexturePacker.Rect rect = (TexturePacker.Rect)inputRects.removeIndex(i);
/* 74 */       rect.x = x;
/* 75 */       rect.y = y;
/* 76 */       rect.width += this.settings.paddingX;
/* 77 */       rect.height += this.settings.paddingY;
/* 78 */       page.outputRects.add(rect);
/* 79 */       x += cellWidth;
/* 80 */       page.width = Math.max(page.width, x);
/* 81 */       page.height = Math.max(page.height, y + cellHeight);
/*    */     } 
/*    */ 
/*    */     
/* 85 */     for (i = page.outputRects.size - 1; i >= 0; i--) {
/* 86 */       TexturePacker.Rect rect = (TexturePacker.Rect)page.outputRects.get(i);
/* 87 */       rect.y = page.height - rect.y - rect.height;
/*    */     } 
/* 89 */     return page;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\GridPacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */