/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.TextureData;
/*     */ import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.OrderedMap;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ public class PixmapPacker
/*     */   implements Disposable
/*     */ {
/*     */   boolean packToTexture;
/*     */   boolean disposed;
/*     */   int pageWidth;
/*     */   int pageHeight;
/*     */   Pixmap.Format pageFormat;
/*     */   int padding;
/*     */   boolean duplicateBorder;
/* 104 */   Color transparentColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
/* 105 */   final Array<Page> pages = new Array();
/*     */   
/*     */   PackStrategy packStrategy;
/*     */ 
/*     */   
/*     */   public PixmapPacker(int pageWidth, int pageHeight, Pixmap.Format pageFormat, int padding, boolean duplicateBorder) {
/* 111 */     this(pageWidth, pageHeight, pageFormat, padding, duplicateBorder, new GuillotineStrategy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PixmapPacker(int pageWidth, int pageHeight, Pixmap.Format pageFormat, int padding, boolean duplicateBorder, PackStrategy packStrategy) {
/* 121 */     this.pageWidth = pageWidth;
/* 122 */     this.pageHeight = pageHeight;
/* 123 */     this.pageFormat = pageFormat;
/* 124 */     this.padding = padding;
/* 125 */     this.duplicateBorder = duplicateBorder;
/* 126 */     this.packStrategy = packStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sort(Array<Pixmap> images) {
/* 132 */     this.packStrategy.sort(images);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Rectangle pack(Pixmap image) {
/* 138 */     return pack(null, image);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Rectangle pack(String name, Pixmap image) {
/* 148 */     if (this.disposed) return null; 
/* 149 */     if (name != null && getRect(name) != null) {
/* 150 */       throw new GdxRuntimeException("Pixmap has already been packed with name: " + name);
/*     */     }
/* 152 */     Rectangle rect = new Rectangle(0.0F, 0.0F, image.getWidth(), image.getHeight());
/* 153 */     if (rect.getWidth() > this.pageWidth || rect.getHeight() > this.pageHeight) {
/* 154 */       if (name == null) throw new GdxRuntimeException("Page size too small for pixmap."); 
/* 155 */       throw new GdxRuntimeException("Page size too small for pixmap: " + name);
/*     */     } 
/*     */     
/* 158 */     Page page = this.packStrategy.pack(this, name, rect);
/* 159 */     if (name != null) {
/* 160 */       page.rects.put(name, rect);
/* 161 */       page.addedRects.add(name);
/*     */     } 
/*     */     
/* 164 */     int rectX = (int)rect.x, rectY = (int)rect.y, rectWidth = (int)rect.width, rectHeight = (int)rect.height;
/*     */     
/* 166 */     if (this.packToTexture && !this.duplicateBorder && page.texture != null && !page.dirty) {
/* 167 */       page.texture.bind();
/* 168 */       Gdx.gl.glTexSubImage2D(page.texture.glTarget, 0, rectX, rectY, rectWidth, rectHeight, image.getGLFormat(), image
/* 169 */           .getGLType(), image.getPixels());
/*     */     } else {
/* 171 */       page.dirty = true;
/*     */     } 
/* 173 */     Pixmap.Blending blending = Pixmap.getBlending();
/* 174 */     Pixmap.setBlending(Pixmap.Blending.None);
/*     */     
/* 176 */     page.image.drawPixmap(image, rectX, rectY);
/*     */     
/* 178 */     if (this.duplicateBorder) {
/* 179 */       int imageWidth = image.getWidth(), imageHeight = image.getHeight();
/*     */       
/* 181 */       page.image.drawPixmap(image, 0, 0, 1, 1, rectX - 1, rectY - 1, 1, 1);
/* 182 */       page.image.drawPixmap(image, imageWidth - 1, 0, 1, 1, rectX + rectWidth, rectY - 1, 1, 1);
/* 183 */       page.image.drawPixmap(image, 0, imageHeight - 1, 1, 1, rectX - 1, rectY + rectHeight, 1, 1);
/* 184 */       page.image.drawPixmap(image, imageWidth - 1, imageHeight - 1, 1, 1, rectX + rectWidth, rectY + rectHeight, 1, 1);
/*     */       
/* 186 */       page.image.drawPixmap(image, 0, 0, imageWidth, 1, rectX, rectY - 1, rectWidth, 1);
/* 187 */       page.image.drawPixmap(image, 0, imageHeight - 1, imageWidth, 1, rectX, rectY + rectHeight, rectWidth, 1);
/* 188 */       page.image.drawPixmap(image, 0, 0, 1, imageHeight, rectX - 1, rectY, 1, rectHeight);
/* 189 */       page.image.drawPixmap(image, imageWidth - 1, 0, 1, imageHeight, rectX + rectWidth, rectY, 1, rectHeight);
/*     */     } 
/*     */     
/* 192 */     Pixmap.setBlending(blending);
/*     */     
/* 194 */     return rect;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<Page> getPages() {
/* 200 */     return this.pages;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Rectangle getRect(String name) {
/* 206 */     for (Page page : this.pages) {
/* 207 */       Rectangle rect = (Rectangle)page.rects.get(name);
/* 208 */       if (rect != null) return rect; 
/*     */     } 
/* 210 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Page getPage(String name) {
/* 216 */     for (Page page : this.pages) {
/* 217 */       Rectangle rect = (Rectangle)page.rects.get(name);
/* 218 */       if (rect != null) return page; 
/*     */     } 
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getPageIndex(String name) {
/* 227 */     for (int i = 0; i < this.pages.size; i++) {
/* 228 */       Rectangle rect = (Rectangle)((Page)this.pages.get(i)).rects.get(name);
/* 229 */       if (rect != null) return i; 
/*     */     } 
/* 231 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void dispose() {
/* 237 */     for (Page page : this.pages) {
/* 238 */       if (page.texture == null) {
/* 239 */         page.image.dispose();
/*     */       }
/*     */     } 
/* 242 */     this.disposed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized TextureAtlas generateTextureAtlas(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean useMipMaps) {
/* 248 */     TextureAtlas atlas = new TextureAtlas();
/* 249 */     updateTextureAtlas(atlas, minFilter, magFilter, useMipMaps);
/* 250 */     return atlas;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void updateTextureAtlas(TextureAtlas atlas, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean useMipMaps) {
/* 259 */     updatePageTextures(minFilter, magFilter, useMipMaps);
/* 260 */     for (Page page : this.pages) {
/* 261 */       if (page.addedRects.size > 0) {
/* 262 */         for (String name : page.addedRects) {
/* 263 */           Rectangle rect = (Rectangle)page.rects.get(name);
/* 264 */           TextureRegion region = new TextureRegion(page.texture, (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
/*     */           
/* 266 */           atlas.addRegion(name, region);
/*     */         } 
/* 268 */         page.addedRects.clear();
/* 269 */         atlas.getTextures().add(page.texture);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void updateTextureRegions(Array<TextureRegion> regions, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean useMipMaps) {
/* 278 */     updatePageTextures(minFilter, magFilter, useMipMaps);
/* 279 */     while (regions.size < this.pages.size) {
/* 280 */       regions.add(new TextureRegion(((Page)this.pages.get(regions.size)).texture));
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void updatePageTextures(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean useMipMaps) {
/* 285 */     for (Page page : this.pages)
/* 286 */       page.updateTexture(minFilter, magFilter, useMipMaps); 
/*     */   }
/*     */   
/*     */   public int getPageWidth() {
/* 290 */     return this.pageWidth;
/*     */   }
/*     */   
/*     */   public void setPageWidth(int pageWidth) {
/* 294 */     this.pageWidth = pageWidth;
/*     */   }
/*     */   
/*     */   public int getPageHeight() {
/* 298 */     return this.pageHeight;
/*     */   }
/*     */   
/*     */   public void setPageHeight(int pageHeight) {
/* 302 */     this.pageHeight = pageHeight;
/*     */   }
/*     */   
/*     */   public Pixmap.Format getPageFormat() {
/* 306 */     return this.pageFormat;
/*     */   }
/*     */   
/*     */   public void setPageFormat(Pixmap.Format pageFormat) {
/* 310 */     this.pageFormat = pageFormat;
/*     */   }
/*     */   
/*     */   public int getPadding() {
/* 314 */     return this.padding;
/*     */   }
/*     */   
/*     */   public void setPadding(int padding) {
/* 318 */     this.padding = padding;
/*     */   }
/*     */   
/*     */   public boolean getDuplicateBorder() {
/* 322 */     return this.duplicateBorder;
/*     */   }
/*     */   
/*     */   public void setDuplicateBorder(boolean duplicateBorder) {
/* 326 */     this.duplicateBorder = duplicateBorder;
/*     */   }
/*     */   
/*     */   public boolean getPackToTexture() {
/* 330 */     return this.packToTexture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackToTexture(boolean packToTexture) {
/* 337 */     this.packToTexture = packToTexture;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Page
/*     */   {
/* 344 */     OrderedMap<String, Rectangle> rects = new OrderedMap();
/*     */     Pixmap image;
/*     */     Texture texture;
/* 347 */     final Array<String> addedRects = new Array();
/*     */     
/*     */     boolean dirty;
/*     */     
/*     */     public Page(PixmapPacker packer) {
/* 352 */       this.image = new Pixmap(packer.pageWidth, packer.pageHeight, packer.pageFormat);
/* 353 */       Color transparentColor = packer.getTransparentColor();
/* 354 */       this.image.setColor(transparentColor);
/* 355 */       this.image.fill();
/*     */     }
/*     */     
/*     */     public Pixmap getPixmap() {
/* 359 */       return this.image;
/*     */     }
/*     */     
/*     */     public OrderedMap<String, Rectangle> getRects() {
/* 363 */       return this.rects;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Texture getTexture() {
/* 369 */       return this.texture;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean updateTexture(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean useMipMaps) {
/* 376 */       if (this.texture != null) {
/* 377 */         if (!this.dirty) return false; 
/* 378 */         this.texture.load(this.texture.getTextureData());
/*     */       } else {
/* 380 */         this.texture = new Texture((TextureData)new PixmapTextureData(this.image, this.image.getFormat(), useMipMaps, false, true))
/*     */           {
/*     */             public void dispose() {
/* 383 */               super.dispose();
/* 384 */               PixmapPacker.Page.this.image.dispose();
/*     */             }
/*     */           };
/* 387 */         this.texture.setFilter(minFilter, magFilter);
/*     */       } 
/* 389 */       this.dirty = false;
/* 390 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface PackStrategy
/*     */   {
/*     */     void sort(Array<Pixmap> param1Array);
/*     */ 
/*     */     
/*     */     PixmapPacker.Page pack(PixmapPacker param1PixmapPacker, String param1String, Rectangle param1Rectangle);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class GuillotineStrategy
/*     */     implements PackStrategy
/*     */   {
/*     */     Comparator<Pixmap> comparator;
/*     */ 
/*     */     
/*     */     public void sort(Array<Pixmap> pixmaps) {
/* 412 */       if (this.comparator == null) {
/* 413 */         this.comparator = new Comparator<Pixmap>() {
/*     */             public int compare(Pixmap o1, Pixmap o2) {
/* 415 */               return Math.max(o1.getWidth(), o1.getHeight()) - Math.max(o2.getWidth(), o2.getHeight());
/*     */             }
/*     */           };
/*     */       }
/* 419 */       pixmaps.sort(this.comparator);
/*     */     }
/*     */     
/*     */     public PixmapPacker.Page pack(PixmapPacker packer, String name, Rectangle rect) {
/*     */       GuillotinePage page;
/* 424 */       if (packer.pages.size == 0) {
/*     */         
/* 426 */         page = new GuillotinePage(packer);
/* 427 */         packer.pages.add(page);
/*     */       } else {
/*     */         
/* 430 */         page = (GuillotinePage)packer.pages.peek();
/*     */       } 
/*     */       
/* 433 */       int padding = packer.padding;
/* 434 */       rect.width += padding;
/* 435 */       rect.height += padding;
/* 436 */       Node node = insert(page.root, rect);
/* 437 */       if (node == null) {
/*     */         
/* 439 */         page = new GuillotinePage(packer);
/* 440 */         packer.pages.add(page);
/* 441 */         node = insert(page.root, rect);
/*     */       } 
/* 443 */       node.full = true;
/* 444 */       rect.set(node.rect.x, node.rect.y, node.rect.width - padding, node.rect.height - padding);
/* 445 */       return page;
/*     */     }
/*     */     
/*     */     private Node insert(Node node, Rectangle rect) {
/* 449 */       if (!node.full && node.leftChild != null && node.rightChild != null) {
/* 450 */         Node newNode = insert(node.leftChild, rect);
/* 451 */         if (newNode == null) newNode = insert(node.rightChild, rect); 
/* 452 */         return newNode;
/*     */       } 
/* 454 */       if (node.full) return null; 
/* 455 */       if (node.rect.width == rect.width && node.rect.height == rect.height) return node; 
/* 456 */       if (node.rect.width < rect.width || node.rect.height < rect.height) return null;
/*     */       
/* 458 */       node.leftChild = new Node();
/* 459 */       node.rightChild = new Node();
/*     */       
/* 461 */       int deltaWidth = (int)node.rect.width - (int)rect.width;
/* 462 */       int deltaHeight = (int)node.rect.height - (int)rect.height;
/* 463 */       if (deltaWidth > deltaHeight) {
/* 464 */         node.leftChild.rect.x = node.rect.x;
/* 465 */         node.leftChild.rect.y = node.rect.y;
/* 466 */         node.leftChild.rect.width = rect.width;
/* 467 */         node.leftChild.rect.height = node.rect.height;
/*     */         
/* 469 */         node.rect.x += rect.width;
/* 470 */         node.rightChild.rect.y = node.rect.y;
/* 471 */         node.rect.width -= rect.width;
/* 472 */         node.rightChild.rect.height = node.rect.height;
/*     */       } else {
/* 474 */         node.leftChild.rect.x = node.rect.x;
/* 475 */         node.leftChild.rect.y = node.rect.y;
/* 476 */         node.leftChild.rect.width = node.rect.width;
/* 477 */         node.leftChild.rect.height = rect.height;
/*     */         
/* 479 */         node.rightChild.rect.x = node.rect.x;
/* 480 */         node.rect.y += rect.height;
/* 481 */         node.rightChild.rect.width = node.rect.width;
/* 482 */         node.rect.height -= rect.height;
/*     */       } 
/*     */       
/* 485 */       return insert(node.leftChild, rect);
/*     */     }
/*     */     
/*     */     static final class Node
/*     */     {
/*     */       public Node leftChild;
/*     */       public Node rightChild;
/* 492 */       public final Rectangle rect = new Rectangle();
/*     */       public boolean full;
/*     */     }
/*     */     
/*     */     static class GuillotinePage extends PixmapPacker.Page {
/*     */       PixmapPacker.GuillotineStrategy.Node root;
/*     */       
/*     */       public GuillotinePage(PixmapPacker packer) {
/* 500 */         super(packer);
/* 501 */         this.root = new PixmapPacker.GuillotineStrategy.Node();
/* 502 */         this.root.rect.x = packer.padding;
/* 503 */         this.root.rect.y = packer.padding;
/* 504 */         this.root.rect.width = (packer.pageWidth - packer.padding * 2);
/* 505 */         this.root.rect.height = (packer.pageHeight - packer.padding * 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SkylineStrategy
/*     */     implements PackStrategy
/*     */   {
/*     */     Comparator<Pixmap> comparator;
/*     */     
/*     */     public void sort(Array<Pixmap> images) {
/* 516 */       if (this.comparator == null) {
/* 517 */         this.comparator = new Comparator<Pixmap>() {
/*     */             public int compare(Pixmap o1, Pixmap o2) {
/* 519 */               return o1.getHeight() - o2.getHeight();
/*     */             }
/*     */           };
/*     */       }
/* 523 */       images.sort(this.comparator);
/*     */     }
/*     */     
/*     */     public PixmapPacker.Page pack(PixmapPacker packer, String name, Rectangle rect) {
/* 527 */       int padding = packer.padding;
/* 528 */       int pageWidth = packer.pageWidth - padding * 2, pageHeight = packer.pageHeight - padding * 2;
/* 529 */       int rectWidth = (int)rect.width + padding, rectHeight = (int)rect.height + padding;
/* 530 */       for (int i = 0, n = packer.pages.size; i < n; i++) {
/* 531 */         SkylinePage skylinePage = (SkylinePage)packer.pages.get(i);
/* 532 */         SkylinePage.Row bestRow = null;
/*     */         
/* 534 */         for (int ii = 0, nn = skylinePage.rows.size - 1; ii < nn; ii++) {
/* 535 */           SkylinePage.Row row1 = (SkylinePage.Row)skylinePage.rows.get(ii);
/* 536 */           if (row1.x + rectWidth < pageWidth && 
/* 537 */             row1.y + rectHeight < pageHeight && 
/* 538 */             rectHeight <= row1.height && (
/* 539 */             bestRow == null || row1.height < bestRow.height)) bestRow = row1; 
/*     */         } 
/* 541 */         if (bestRow == null) {
/*     */           
/* 543 */           SkylinePage.Row row1 = (SkylinePage.Row)skylinePage.rows.peek();
/* 544 */           if (row1.y + rectHeight >= pageHeight)
/* 545 */             continue;  if (row1.x + rectWidth < pageWidth) {
/* 546 */             row1.height = Math.max(row1.height, rectHeight);
/* 547 */             bestRow = row1;
/*     */           } else {
/*     */             
/* 550 */             bestRow = new SkylinePage.Row();
/* 551 */             row1.y += row1.height;
/* 552 */             bestRow.height = rectHeight;
/* 553 */             skylinePage.rows.add(bestRow);
/*     */           } 
/*     */         } 
/* 556 */         if (bestRow != null) {
/* 557 */           rect.x = bestRow.x;
/* 558 */           rect.y = bestRow.y;
/* 559 */           bestRow.x += rectWidth;
/* 560 */           return skylinePage;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 564 */       SkylinePage page = new SkylinePage(packer);
/* 565 */       packer.pages.add(page);
/* 566 */       SkylinePage.Row row = new SkylinePage.Row();
/* 567 */       row.x = padding + rectWidth;
/* 568 */       row.y = padding;
/* 569 */       row.height = rectHeight;
/* 570 */       page.rows.add(row);
/* 571 */       rect.x = padding;
/* 572 */       rect.y = padding;
/* 573 */       return page;
/*     */     }
/*     */     
/*     */     static class SkylinePage extends PixmapPacker.Page {
/* 577 */       Array<Row> rows = new Array(); static class Row {
/*     */         int x; int y; int height; }
/*     */       public SkylinePage(PixmapPacker packer) {
/* 580 */         super(packer);
/*     */       }
/*     */     }
/*     */     
/*     */     static class Row {
/*     */       int x;
/*     */       int y;
/*     */       int height;
/*     */     }
/*     */   }
/*     */   
/*     */   public Color getTransparentColor() {
/* 592 */     return this.transparentColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransparentColor(Color color) {
/* 599 */     this.transparentColor.set(color);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\PixmapPacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */