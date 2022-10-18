/*     */ package com.badlogic.gdx.tools.texturepacker;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Sort;
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
/*     */ public class MaxRectsPacker
/*     */   implements TexturePacker.Packer
/*     */ {
/*  33 */   private RectComparator rectComparator = new RectComparator();
/*  34 */   private FreeRectChoiceHeuristic[] methods = FreeRectChoiceHeuristic.values();
/*  35 */   private MaxRects maxRects = new MaxRects();
/*     */   TexturePacker.Settings settings;
/*  37 */   private Sort sort = new Sort();
/*     */   
/*     */   public MaxRectsPacker(TexturePacker.Settings settings) {
/*  40 */     this.settings = settings;
/*  41 */     if (settings.minWidth > settings.maxWidth) throw new RuntimeException("Page min width cannot be higher than max width."); 
/*  42 */     if (settings.minHeight > settings.maxHeight)
/*  43 */       throw new RuntimeException("Page min height cannot be higher than max height."); 
/*     */   }
/*     */   
/*     */   public Array<TexturePacker.Page> pack(Array<TexturePacker.Rect> inputRects) {
/*  47 */     for (int i = 0, nn = inputRects.size; i < nn; i++) {
/*  48 */       TexturePacker.Rect rect = (TexturePacker.Rect)inputRects.get(i);
/*  49 */       rect.width += this.settings.paddingX;
/*  50 */       rect.height += this.settings.paddingY;
/*     */     } 
/*     */     
/*  53 */     if (this.settings.fast) {
/*  54 */       if (this.settings.rotation) {
/*     */         
/*  56 */         this.sort.sort(inputRects, new Comparator<TexturePacker.Rect>() {
/*     */               public int compare(TexturePacker.Rect o1, TexturePacker.Rect o2) {
/*  58 */                 int n1 = (o1.width > o1.height) ? o1.width : o1.height;
/*  59 */                 int n2 = (o2.width > o2.height) ? o2.width : o2.height;
/*  60 */                 return n2 - n1;
/*     */               }
/*     */             });
/*     */       } else {
/*     */         
/*  65 */         this.sort.sort(inputRects, new Comparator<TexturePacker.Rect>() {
/*     */               public int compare(TexturePacker.Rect o1, TexturePacker.Rect o2) {
/*  67 */                 return o2.width - o1.width;
/*     */               }
/*     */             });
/*     */       } 
/*     */     }
/*     */     
/*  73 */     Array<TexturePacker.Page> pages = new Array();
/*  74 */     while (inputRects.size > 0) {
/*     */ 
/*     */       
/*  77 */       TexturePacker.Page result = packPage(inputRects);
/*  78 */       pages.add(result);
/*  79 */       inputRects = result.remainingRects;
/*     */     } 
/*  81 */     return pages;
/*     */   }
/*     */ 
/*     */   
/*     */   private TexturePacker.Page packPage(Array<TexturePacker.Rect> inputRects) {
/*  86 */     int paddingX = this.settings.paddingX, paddingY = this.settings.paddingY;
/*  87 */     float maxWidth = this.settings.maxWidth, maxHeight = this.settings.maxHeight;
/*  88 */     int edgePaddingX = 0, edgePaddingY = 0;
/*  89 */     if (this.settings.edgePadding) {
/*  90 */       if (this.settings.duplicatePadding) {
/*  91 */         maxWidth -= paddingX;
/*  92 */         maxHeight -= paddingY;
/*     */       } else {
/*  94 */         maxWidth -= (paddingX * 2);
/*  95 */         maxHeight -= (paddingY * 2);
/*  96 */         edgePaddingX = paddingX;
/*  97 */         edgePaddingY = paddingY;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 102 */     int minWidth = Integer.MAX_VALUE, minHeight = Integer.MAX_VALUE;
/* 103 */     for (int i = 0, nn = inputRects.size; i < nn; i++) {
/* 104 */       TexturePacker.Rect rect = (TexturePacker.Rect)inputRects.get(i);
/* 105 */       minWidth = Math.min(minWidth, rect.width);
/* 106 */       minHeight = Math.min(minHeight, rect.height);
/* 107 */       float f1 = (rect.width - paddingX), f2 = (rect.height - paddingY);
/* 108 */       if (this.settings.rotation) {
/* 109 */         if ((f1 > maxWidth || f2 > maxHeight) && (f1 > maxHeight || f2 > maxWidth)) {
/* 110 */           String paddingMessage = (edgePaddingX > 0 || edgePaddingY > 0) ? (" and edge padding " + paddingX + "," + paddingY) : "";
/*     */           
/* 112 */           throw new RuntimeException("Image does not fit with max page size " + this.settings.maxWidth + "x" + this.settings.maxHeight + paddingMessage + ": " + rect.name + "[" + f1 + "," + f2 + "]");
/*     */         } 
/*     */       } else {
/*     */         
/* 116 */         if (f1 > maxWidth) {
/* 117 */           String paddingMessage = (edgePaddingX > 0) ? (" and X edge padding " + paddingX) : "";
/* 118 */           throw new RuntimeException("Image does not fit with max page width " + this.settings.maxWidth + paddingMessage + ": " + rect.name + "[" + f1 + "," + f2 + "]");
/*     */         } 
/*     */         
/* 121 */         if (f2 > maxHeight && (!this.settings.rotation || f1 > maxHeight)) {
/* 122 */           String paddingMessage = (edgePaddingY > 0) ? (" and Y edge padding " + paddingY) : "";
/* 123 */           throw new RuntimeException("Image does not fit in max page height " + this.settings.maxHeight + paddingMessage + ": " + rect.name + "[" + f1 + "," + f2 + "]");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     minWidth = Math.max(minWidth, this.settings.minWidth);
/* 129 */     minHeight = Math.max(minHeight, this.settings.minHeight);
/*     */     
/* 131 */     if (!this.settings.silent) System.out.print("Packing");
/*     */ 
/*     */     
/* 134 */     TexturePacker.Page bestResult = null;
/* 135 */     if (this.settings.square) {
/* 136 */       int minSize = Math.max(minWidth, minHeight);
/* 137 */       int maxSize = Math.min(this.settings.maxWidth, this.settings.maxHeight);
/* 138 */       BinarySearch sizeSearch = new BinarySearch(minSize, maxSize, this.settings.fast ? 25 : 15, this.settings.pot);
/* 139 */       int size = sizeSearch.reset(), k = 0;
/* 140 */       while (size != -1) {
/* 141 */         TexturePacker.Page result = packAtSize(true, size - edgePaddingX, size - edgePaddingY, inputRects);
/* 142 */         if (!this.settings.silent) {
/* 143 */           if (++k % 70 == 0) System.out.println(); 
/* 144 */           System.out.print(".");
/*     */         } 
/* 146 */         bestResult = getBest(bestResult, result);
/* 147 */         size = sizeSearch.next((result == null));
/*     */       } 
/* 149 */       if (!this.settings.silent) System.out.println();
/*     */       
/* 151 */       if (bestResult == null) bestResult = packAtSize(false, maxSize - edgePaddingX, maxSize - edgePaddingY, inputRects); 
/* 152 */       this.sort.sort(bestResult.outputRects, this.rectComparator);
/* 153 */       bestResult.width = Math.max(bestResult.width, bestResult.height);
/* 154 */       bestResult.height = Math.max(bestResult.width, bestResult.height);
/* 155 */       return bestResult;
/*     */     } 
/* 157 */     BinarySearch widthSearch = new BinarySearch(minWidth, this.settings.maxWidth, this.settings.fast ? 25 : 15, this.settings.pot);
/* 158 */     BinarySearch heightSearch = new BinarySearch(minHeight, this.settings.maxHeight, this.settings.fast ? 25 : 15, this.settings.pot);
/* 159 */     int width = widthSearch.reset(), j = 0;
/* 160 */     int height = this.settings.square ? width : heightSearch.reset();
/*     */     while (true) {
/* 162 */       TexturePacker.Page bestWidthResult = null;
/* 163 */       while (width != -1) {
/* 164 */         TexturePacker.Page result = packAtSize(true, width - edgePaddingX, height - edgePaddingY, inputRects);
/* 165 */         if (!this.settings.silent) {
/* 166 */           if (++j % 70 == 0) System.out.println(); 
/* 167 */           System.out.print(".");
/*     */         } 
/* 169 */         bestWidthResult = getBest(bestWidthResult, result);
/* 170 */         width = widthSearch.next((result == null));
/* 171 */         if (this.settings.square) height = width; 
/*     */       } 
/* 173 */       bestResult = getBest(bestResult, bestWidthResult);
/* 174 */       if (this.settings.square)
/* 175 */         break;  height = heightSearch.next((bestWidthResult == null));
/* 176 */       if (height == -1)
/* 177 */         break;  width = widthSearch.reset();
/*     */     } 
/* 179 */     if (!this.settings.silent) System.out.println();
/*     */     
/* 181 */     if (bestResult == null)
/* 182 */       bestResult = packAtSize(false, this.settings.maxWidth - edgePaddingX, this.settings.maxHeight - edgePaddingY, inputRects); 
/* 183 */     this.sort.sort(bestResult.outputRects, this.rectComparator);
/* 184 */     return bestResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TexturePacker.Page packAtSize(boolean fully, int width, int height, Array<TexturePacker.Rect> inputRects) {
/* 191 */     TexturePacker.Page bestResult = null;
/* 192 */     for (int i = 0, n = this.methods.length; i < n; i++) {
/* 193 */       TexturePacker.Page result; this.maxRects.init(width, height);
/*     */       
/* 195 */       if (!this.settings.fast) {
/* 196 */         result = this.maxRects.pack(inputRects, this.methods[i]);
/*     */       } else {
/* 198 */         Array<TexturePacker.Rect> remaining = new Array();
/* 199 */         for (int ii = 0, nn = inputRects.size; ii < nn; ii++) {
/* 200 */           TexturePacker.Rect rect = (TexturePacker.Rect)inputRects.get(ii);
/* 201 */           if (this.maxRects.insert(rect, this.methods[i]) == null)
/* 202 */             while (ii < nn) {
/* 203 */               remaining.add(inputRects.get(ii++));
/*     */             } 
/*     */         } 
/* 206 */         result = this.maxRects.getResult();
/* 207 */         result.remainingRects = remaining;
/*     */       } 
/* 209 */       if ((!fully || result.remainingRects.size <= 0) && 
/* 210 */         result.outputRects.size != 0)
/* 211 */         bestResult = getBest(bestResult, result); 
/*     */     } 
/* 213 */     return bestResult;
/*     */   }
/*     */   
/*     */   private TexturePacker.Page getBest(TexturePacker.Page result1, TexturePacker.Page result2) {
/* 217 */     if (result1 == null) return result2; 
/* 218 */     if (result2 == null) return result1; 
/* 219 */     return (result1.occupancy > result2.occupancy) ? result1 : result2;
/*     */   }
/*     */   
/*     */   static class BinarySearch { int min;
/*     */     int max;
/*     */     int fuzziness;
/*     */     
/*     */     public BinarySearch(int min, int max, int fuzziness, boolean pot) {
/* 227 */       this.pot = pot;
/* 228 */       this.fuzziness = pot ? 0 : fuzziness;
/* 229 */       this.min = pot ? (int)(Math.log(MathUtils.nextPowerOfTwo(min)) / Math.log(2.0D)) : min;
/* 230 */       this.max = pot ? (int)(Math.log(MathUtils.nextPowerOfTwo(max)) / Math.log(2.0D)) : max;
/*     */     }
/*     */     int low; int high; int current; boolean pot;
/*     */     public int reset() {
/* 234 */       this.low = this.min;
/* 235 */       this.high = this.max;
/* 236 */       this.current = this.low + this.high >>> 1;
/* 237 */       return this.pot ? (int)Math.pow(2.0D, this.current) : this.current;
/*     */     }
/*     */     
/*     */     public int next(boolean result) {
/* 241 */       if (this.low >= this.high) return -1; 
/* 242 */       if (result) {
/* 243 */         this.low = this.current + 1;
/*     */       } else {
/* 245 */         this.high = this.current - 1;
/* 246 */       }  this.current = this.low + this.high >>> 1;
/* 247 */       if (Math.abs(this.low - this.high) < this.fuzziness) return -1; 
/* 248 */       return this.pot ? (int)Math.pow(2.0D, this.current) : this.current;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   class MaxRects
/*     */   {
/*     */     private int binWidth;
/*     */     
/*     */     private int binHeight;
/*     */     
/* 259 */     private final Array<TexturePacker.Rect> usedRectangles = new Array();
/* 260 */     private final Array<TexturePacker.Rect> freeRectangles = new Array();
/*     */     
/*     */     public void init(int width, int height) {
/* 263 */       this.binWidth = width;
/* 264 */       this.binHeight = height;
/*     */       
/* 266 */       this.usedRectangles.clear();
/* 267 */       this.freeRectangles.clear();
/* 268 */       TexturePacker.Rect n = new TexturePacker.Rect();
/* 269 */       n.x = 0;
/* 270 */       n.y = 0;
/* 271 */       n.width = width;
/* 272 */       n.height = height;
/* 273 */       this.freeRectangles.add(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public TexturePacker.Rect insert(TexturePacker.Rect rect, MaxRectsPacker.FreeRectChoiceHeuristic method) {
/* 278 */       TexturePacker.Rect newNode = scoreRect(rect, method);
/* 279 */       if (newNode.height == 0) return null;
/*     */       
/* 281 */       int numRectanglesToProcess = this.freeRectangles.size;
/* 282 */       for (int i = 0; i < numRectanglesToProcess; i++) {
/* 283 */         if (splitFreeNode((TexturePacker.Rect)this.freeRectangles.get(i), newNode)) {
/* 284 */           this.freeRectangles.removeIndex(i);
/* 285 */           i--;
/* 286 */           numRectanglesToProcess--;
/*     */         } 
/*     */       } 
/*     */       
/* 290 */       pruneFreeList();
/*     */       
/* 292 */       TexturePacker.Rect bestNode = new TexturePacker.Rect();
/* 293 */       bestNode.set(rect);
/* 294 */       bestNode.score1 = newNode.score1;
/* 295 */       bestNode.score2 = newNode.score2;
/* 296 */       bestNode.x = newNode.x;
/* 297 */       bestNode.y = newNode.y;
/* 298 */       bestNode.width = newNode.width;
/* 299 */       bestNode.height = newNode.height;
/* 300 */       bestNode.rotated = newNode.rotated;
/*     */       
/* 302 */       this.usedRectangles.add(bestNode);
/* 303 */       return bestNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public TexturePacker.Page pack(Array<TexturePacker.Rect> rects, MaxRectsPacker.FreeRectChoiceHeuristic method) {
/* 308 */       rects = new Array(rects);
/* 309 */       while (rects.size > 0) {
/* 310 */         int bestRectIndex = -1;
/* 311 */         TexturePacker.Rect bestNode = new TexturePacker.Rect();
/* 312 */         bestNode.score1 = Integer.MAX_VALUE;
/* 313 */         bestNode.score2 = Integer.MAX_VALUE;
/*     */ 
/*     */         
/* 316 */         for (int i = 0; i < rects.size; i++) {
/* 317 */           TexturePacker.Rect newNode = scoreRect((TexturePacker.Rect)rects.get(i), method);
/* 318 */           if (newNode.score1 < bestNode.score1 || (newNode.score1 == bestNode.score1 && newNode.score2 < bestNode.score2)) {
/* 319 */             bestNode.set((TexturePacker.Rect)rects.get(i));
/* 320 */             bestNode.score1 = newNode.score1;
/* 321 */             bestNode.score2 = newNode.score2;
/* 322 */             bestNode.x = newNode.x;
/* 323 */             bestNode.y = newNode.y;
/* 324 */             bestNode.width = newNode.width;
/* 325 */             bestNode.height = newNode.height;
/* 326 */             bestNode.rotated = newNode.rotated;
/* 327 */             bestRectIndex = i;
/*     */           } 
/*     */         } 
/*     */         
/* 331 */         if (bestRectIndex == -1)
/*     */           break; 
/* 333 */         placeRect(bestNode);
/* 334 */         rects.removeIndex(bestRectIndex);
/*     */       } 
/*     */       
/* 337 */       TexturePacker.Page result = getResult();
/* 338 */       result.remainingRects = rects;
/* 339 */       return result;
/*     */     }
/*     */     
/*     */     public TexturePacker.Page getResult() {
/* 343 */       int w = 0, h = 0;
/* 344 */       for (int i = 0; i < this.usedRectangles.size; i++) {
/* 345 */         TexturePacker.Rect rect = (TexturePacker.Rect)this.usedRectangles.get(i);
/* 346 */         w = Math.max(w, rect.x + rect.width);
/* 347 */         h = Math.max(h, rect.y + rect.height);
/*     */       } 
/* 349 */       TexturePacker.Page result = new TexturePacker.Page();
/* 350 */       result.outputRects = new Array(this.usedRectangles);
/* 351 */       result.occupancy = getOccupancy();
/* 352 */       result.width = w;
/* 353 */       result.height = h;
/* 354 */       return result;
/*     */     }
/*     */     
/*     */     private void placeRect(TexturePacker.Rect node) {
/* 358 */       int numRectanglesToProcess = this.freeRectangles.size;
/* 359 */       for (int i = 0; i < numRectanglesToProcess; i++) {
/* 360 */         if (splitFreeNode((TexturePacker.Rect)this.freeRectangles.get(i), node)) {
/* 361 */           this.freeRectangles.removeIndex(i);
/* 362 */           i--;
/* 363 */           numRectanglesToProcess--;
/*     */         } 
/*     */       } 
/*     */       
/* 367 */       pruneFreeList();
/*     */       
/* 369 */       this.usedRectangles.add(node);
/*     */     }
/*     */     
/*     */     private TexturePacker.Rect scoreRect(TexturePacker.Rect rect, MaxRectsPacker.FreeRectChoiceHeuristic method) {
/* 373 */       int width = rect.width;
/* 374 */       int height = rect.height;
/* 375 */       int rotatedWidth = height - MaxRectsPacker.this.settings.paddingY + MaxRectsPacker.this.settings.paddingX;
/* 376 */       int rotatedHeight = width - MaxRectsPacker.this.settings.paddingX + MaxRectsPacker.this.settings.paddingY;
/* 377 */       boolean rotate = (rect.canRotate && MaxRectsPacker.this.settings.rotation);
/*     */       
/* 379 */       TexturePacker.Rect newNode = null;
/* 380 */       switch (method) {
/*     */         case BestShortSideFit:
/* 382 */           newNode = findPositionForNewNodeBestShortSideFit(width, height, rotatedWidth, rotatedHeight, rotate);
/*     */           break;
/*     */         case BottomLeftRule:
/* 385 */           newNode = findPositionForNewNodeBottomLeft(width, height, rotatedWidth, rotatedHeight, rotate);
/*     */           break;
/*     */         case ContactPointRule:
/* 388 */           newNode = findPositionForNewNodeContactPoint(width, height, rotatedWidth, rotatedHeight, rotate);
/* 389 */           newNode.score1 = -newNode.score1;
/*     */           break;
/*     */         case BestLongSideFit:
/* 392 */           newNode = findPositionForNewNodeBestLongSideFit(width, height, rotatedWidth, rotatedHeight, rotate);
/*     */           break;
/*     */         case BestAreaFit:
/* 395 */           newNode = findPositionForNewNodeBestAreaFit(width, height, rotatedWidth, rotatedHeight, rotate);
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 400 */       if (newNode.height == 0) {
/* 401 */         newNode.score1 = Integer.MAX_VALUE;
/* 402 */         newNode.score2 = Integer.MAX_VALUE;
/*     */       } 
/*     */       
/* 405 */       return newNode;
/*     */     }
/*     */ 
/*     */     
/*     */     private float getOccupancy() {
/* 410 */       int usedSurfaceArea = 0;
/* 411 */       for (int i = 0; i < this.usedRectangles.size; i++)
/* 412 */         usedSurfaceArea += ((TexturePacker.Rect)this.usedRectangles.get(i)).width * ((TexturePacker.Rect)this.usedRectangles.get(i)).height; 
/* 413 */       return usedSurfaceArea / (this.binWidth * this.binHeight);
/*     */     }
/*     */     
/*     */     private TexturePacker.Rect findPositionForNewNodeBottomLeft(int width, int height, int rotatedWidth, int rotatedHeight, boolean rotate) {
/* 417 */       TexturePacker.Rect bestNode = new TexturePacker.Rect();
/*     */       
/* 419 */       bestNode.score1 = Integer.MAX_VALUE;
/*     */       
/* 421 */       for (int i = 0; i < this.freeRectangles.size; i++) {
/*     */         
/* 423 */         if (((TexturePacker.Rect)this.freeRectangles.get(i)).width >= width && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= height) {
/* 424 */           int topSideY = ((TexturePacker.Rect)this.freeRectangles.get(i)).y + height;
/* 425 */           if (topSideY < bestNode.score1 || (topSideY == bestNode.score1 && ((TexturePacker.Rect)this.freeRectangles.get(i)).x < bestNode.score2)) {
/* 426 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 427 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 428 */             bestNode.width = width;
/* 429 */             bestNode.height = height;
/* 430 */             bestNode.score1 = topSideY;
/* 431 */             bestNode.score2 = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 432 */             bestNode.rotated = false;
/*     */           } 
/*     */         } 
/* 435 */         if (rotate && ((TexturePacker.Rect)this.freeRectangles.get(i)).width >= rotatedWidth && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= rotatedHeight) {
/* 436 */           int topSideY = ((TexturePacker.Rect)this.freeRectangles.get(i)).y + rotatedHeight;
/* 437 */           if (topSideY < bestNode.score1 || (topSideY == bestNode.score1 && ((TexturePacker.Rect)this.freeRectangles.get(i)).x < bestNode.score2)) {
/* 438 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 439 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 440 */             bestNode.width = rotatedWidth;
/* 441 */             bestNode.height = rotatedHeight;
/* 442 */             bestNode.score1 = topSideY;
/* 443 */             bestNode.score2 = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 444 */             bestNode.rotated = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 448 */       return bestNode;
/*     */     }
/*     */ 
/*     */     
/*     */     private TexturePacker.Rect findPositionForNewNodeBestShortSideFit(int width, int height, int rotatedWidth, int rotatedHeight, boolean rotate) {
/* 453 */       TexturePacker.Rect bestNode = new TexturePacker.Rect();
/* 454 */       bestNode.score1 = Integer.MAX_VALUE;
/*     */       
/* 456 */       for (int i = 0; i < this.freeRectangles.size; i++) {
/*     */         
/* 458 */         if (((TexturePacker.Rect)this.freeRectangles.get(i)).width >= width && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= height) {
/* 459 */           int leftoverHoriz = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).width - width);
/* 460 */           int leftoverVert = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).height - height);
/* 461 */           int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
/* 462 */           int longSideFit = Math.max(leftoverHoriz, leftoverVert);
/*     */           
/* 464 */           if (shortSideFit < bestNode.score1 || (shortSideFit == bestNode.score1 && longSideFit < bestNode.score2)) {
/* 465 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 466 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 467 */             bestNode.width = width;
/* 468 */             bestNode.height = height;
/* 469 */             bestNode.score1 = shortSideFit;
/* 470 */             bestNode.score2 = longSideFit;
/* 471 */             bestNode.rotated = false;
/*     */           } 
/*     */         } 
/*     */         
/* 475 */         if (rotate && ((TexturePacker.Rect)this.freeRectangles.get(i)).width >= rotatedWidth && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= rotatedHeight) {
/* 476 */           int flippedLeftoverHoriz = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).width - rotatedWidth);
/* 477 */           int flippedLeftoverVert = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).height - rotatedHeight);
/* 478 */           int flippedShortSideFit = Math.min(flippedLeftoverHoriz, flippedLeftoverVert);
/* 479 */           int flippedLongSideFit = Math.max(flippedLeftoverHoriz, flippedLeftoverVert);
/*     */           
/* 481 */           if (flippedShortSideFit < bestNode.score1 || (flippedShortSideFit == bestNode.score1 && flippedLongSideFit < bestNode.score2)) {
/*     */             
/* 483 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 484 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 485 */             bestNode.width = rotatedWidth;
/* 486 */             bestNode.height = rotatedHeight;
/* 487 */             bestNode.score1 = flippedShortSideFit;
/* 488 */             bestNode.score2 = flippedLongSideFit;
/* 489 */             bestNode.rotated = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 494 */       return bestNode;
/*     */     }
/*     */ 
/*     */     
/*     */     private TexturePacker.Rect findPositionForNewNodeBestLongSideFit(int width, int height, int rotatedWidth, int rotatedHeight, boolean rotate) {
/* 499 */       TexturePacker.Rect bestNode = new TexturePacker.Rect();
/*     */       
/* 501 */       bestNode.score2 = Integer.MAX_VALUE;
/*     */       
/* 503 */       for (int i = 0; i < this.freeRectangles.size; i++) {
/*     */         
/* 505 */         if (((TexturePacker.Rect)this.freeRectangles.get(i)).width >= width && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= height) {
/* 506 */           int leftoverHoriz = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).width - width);
/* 507 */           int leftoverVert = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).height - height);
/* 508 */           int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
/* 509 */           int longSideFit = Math.max(leftoverHoriz, leftoverVert);
/*     */           
/* 511 */           if (longSideFit < bestNode.score2 || (longSideFit == bestNode.score2 && shortSideFit < bestNode.score1)) {
/* 512 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 513 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 514 */             bestNode.width = width;
/* 515 */             bestNode.height = height;
/* 516 */             bestNode.score1 = shortSideFit;
/* 517 */             bestNode.score2 = longSideFit;
/* 518 */             bestNode.rotated = false;
/*     */           } 
/*     */         } 
/*     */         
/* 522 */         if (rotate && ((TexturePacker.Rect)this.freeRectangles.get(i)).width >= rotatedWidth && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= rotatedHeight) {
/* 523 */           int leftoverHoriz = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).width - rotatedWidth);
/* 524 */           int leftoverVert = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).height - rotatedHeight);
/* 525 */           int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
/* 526 */           int longSideFit = Math.max(leftoverHoriz, leftoverVert);
/*     */           
/* 528 */           if (longSideFit < bestNode.score2 || (longSideFit == bestNode.score2 && shortSideFit < bestNode.score1)) {
/* 529 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 530 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 531 */             bestNode.width = rotatedWidth;
/* 532 */             bestNode.height = rotatedHeight;
/* 533 */             bestNode.score1 = shortSideFit;
/* 534 */             bestNode.score2 = longSideFit;
/* 535 */             bestNode.rotated = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 539 */       return bestNode;
/*     */     }
/*     */ 
/*     */     
/*     */     private TexturePacker.Rect findPositionForNewNodeBestAreaFit(int width, int height, int rotatedWidth, int rotatedHeight, boolean rotate) {
/* 544 */       TexturePacker.Rect bestNode = new TexturePacker.Rect();
/*     */       
/* 546 */       bestNode.score1 = Integer.MAX_VALUE;
/*     */       
/* 548 */       for (int i = 0; i < this.freeRectangles.size; i++) {
/* 549 */         int areaFit = ((TexturePacker.Rect)this.freeRectangles.get(i)).width * ((TexturePacker.Rect)this.freeRectangles.get(i)).height - width * height;
/*     */ 
/*     */         
/* 552 */         if (((TexturePacker.Rect)this.freeRectangles.get(i)).width >= width && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= height) {
/* 553 */           int leftoverHoriz = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).width - width);
/* 554 */           int leftoverVert = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).height - height);
/* 555 */           int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
/*     */           
/* 557 */           if (areaFit < bestNode.score1 || (areaFit == bestNode.score1 && shortSideFit < bestNode.score2)) {
/* 558 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 559 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 560 */             bestNode.width = width;
/* 561 */             bestNode.height = height;
/* 562 */             bestNode.score2 = shortSideFit;
/* 563 */             bestNode.score1 = areaFit;
/* 564 */             bestNode.rotated = false;
/*     */           } 
/*     */         } 
/*     */         
/* 568 */         if (rotate && ((TexturePacker.Rect)this.freeRectangles.get(i)).width >= rotatedWidth && ((TexturePacker.Rect)this.freeRectangles.get(i)).height >= rotatedHeight) {
/* 569 */           int leftoverHoriz = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).width - rotatedWidth);
/* 570 */           int leftoverVert = Math.abs(((TexturePacker.Rect)this.freeRectangles.get(i)).height - rotatedHeight);
/* 571 */           int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
/*     */           
/* 573 */           if (areaFit < bestNode.score1 || (areaFit == bestNode.score1 && shortSideFit < bestNode.score2)) {
/* 574 */             bestNode.x = ((TexturePacker.Rect)this.freeRectangles.get(i)).x;
/* 575 */             bestNode.y = ((TexturePacker.Rect)this.freeRectangles.get(i)).y;
/* 576 */             bestNode.width = rotatedWidth;
/* 577 */             bestNode.height = rotatedHeight;
/* 578 */             bestNode.score2 = shortSideFit;
/* 579 */             bestNode.score1 = areaFit;
/* 580 */             bestNode.rotated = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 584 */       return bestNode;
/*     */     }
/*     */ 
/*     */     
/*     */     private int commonIntervalLength(int i1start, int i1end, int i2start, int i2end) {
/* 589 */       if (i1end < i2start || i2end < i1start) return 0; 
/* 590 */       return Math.min(i1end, i2end) - Math.max(i1start, i2start);
/*     */     }
/*     */     
/*     */     private int contactPointScoreNode(int x, int y, int width, int height) {
/* 594 */       int score = 0;
/*     */       
/* 596 */       if (x == 0 || x + width == this.binWidth) score += height; 
/* 597 */       if (y == 0 || y + height == this.binHeight) score += width;
/*     */       
/* 599 */       Array<TexturePacker.Rect> usedRectangles = this.usedRectangles;
/* 600 */       for (int i = 0, n = usedRectangles.size; i < n; i++) {
/* 601 */         TexturePacker.Rect rect = (TexturePacker.Rect)usedRectangles.get(i);
/* 602 */         if (rect.x == x + width || rect.x + rect.width == x)
/* 603 */           score += commonIntervalLength(rect.y, rect.y + rect.height, y, y + height); 
/* 604 */         if (rect.y == y + height || rect.y + rect.height == y)
/* 605 */           score += commonIntervalLength(rect.x, rect.x + rect.width, x, x + width); 
/*     */       } 
/* 607 */       return score;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private TexturePacker.Rect findPositionForNewNodeContactPoint(int width, int height, int rotatedWidth, int rotatedHeight, boolean rotate) {
/* 613 */       TexturePacker.Rect bestNode = new TexturePacker.Rect();
/* 614 */       bestNode.score1 = -1;
/*     */       
/* 616 */       Array<TexturePacker.Rect> freeRectangles = this.freeRectangles;
/* 617 */       for (int i = 0, n = freeRectangles.size; i < n; i++) {
/*     */         
/* 619 */         TexturePacker.Rect free = (TexturePacker.Rect)freeRectangles.get(i);
/* 620 */         if (free.width >= width && free.height >= height) {
/* 621 */           int score = contactPointScoreNode(free.x, free.y, width, height);
/* 622 */           if (score > bestNode.score1) {
/* 623 */             bestNode.x = free.x;
/* 624 */             bestNode.y = free.y;
/* 625 */             bestNode.width = width;
/* 626 */             bestNode.height = height;
/* 627 */             bestNode.score1 = score;
/* 628 */             bestNode.rotated = false;
/*     */           } 
/*     */         } 
/* 631 */         if (rotate && free.width >= rotatedWidth && free.height >= rotatedHeight) {
/* 632 */           int score = contactPointScoreNode(free.x, free.y, rotatedWidth, rotatedHeight);
/* 633 */           if (score > bestNode.score1) {
/* 634 */             bestNode.x = free.x;
/* 635 */             bestNode.y = free.y;
/* 636 */             bestNode.width = rotatedWidth;
/* 637 */             bestNode.height = rotatedHeight;
/* 638 */             bestNode.score1 = score;
/* 639 */             bestNode.rotated = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 643 */       return bestNode;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean splitFreeNode(TexturePacker.Rect freeNode, TexturePacker.Rect usedNode) {
/* 648 */       if (usedNode.x >= freeNode.x + freeNode.width || usedNode.x + usedNode.width <= freeNode.x || usedNode.y >= freeNode.y + freeNode.height || usedNode.y + usedNode.height <= freeNode.y) {
/* 649 */         return false;
/*     */       }
/* 651 */       if (usedNode.x < freeNode.x + freeNode.width && usedNode.x + usedNode.width > freeNode.x) {
/*     */         
/* 653 */         if (usedNode.y > freeNode.y && usedNode.y < freeNode.y + freeNode.height) {
/* 654 */           TexturePacker.Rect newNode = new TexturePacker.Rect(freeNode);
/* 655 */           newNode.height = usedNode.y - newNode.y;
/* 656 */           this.freeRectangles.add(newNode);
/*     */         } 
/*     */ 
/*     */         
/* 660 */         if (usedNode.y + usedNode.height < freeNode.y + freeNode.height) {
/* 661 */           TexturePacker.Rect newNode = new TexturePacker.Rect(freeNode);
/* 662 */           usedNode.y += usedNode.height;
/* 663 */           newNode.height = freeNode.y + freeNode.height - usedNode.y + usedNode.height;
/* 664 */           this.freeRectangles.add(newNode);
/*     */         } 
/*     */       } 
/*     */       
/* 668 */       if (usedNode.y < freeNode.y + freeNode.height && usedNode.y + usedNode.height > freeNode.y) {
/*     */         
/* 670 */         if (usedNode.x > freeNode.x && usedNode.x < freeNode.x + freeNode.width) {
/* 671 */           TexturePacker.Rect newNode = new TexturePacker.Rect(freeNode);
/* 672 */           newNode.width = usedNode.x - newNode.x;
/* 673 */           this.freeRectangles.add(newNode);
/*     */         } 
/*     */ 
/*     */         
/* 677 */         if (usedNode.x + usedNode.width < freeNode.x + freeNode.width) {
/* 678 */           TexturePacker.Rect newNode = new TexturePacker.Rect(freeNode);
/* 679 */           usedNode.x += usedNode.width;
/* 680 */           newNode.width = freeNode.x + freeNode.width - usedNode.x + usedNode.width;
/* 681 */           this.freeRectangles.add(newNode);
/*     */         } 
/*     */       } 
/*     */       
/* 685 */       return true;
/*     */     }
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
/*     */     private void pruneFreeList() {
/* 702 */       Array<TexturePacker.Rect> freeRectangles = this.freeRectangles;
/* 703 */       for (int i = 0, n = freeRectangles.size; i < n; i++) {
/* 704 */         for (int j = i + 1; j < n; j++) {
/* 705 */           TexturePacker.Rect rect1 = (TexturePacker.Rect)freeRectangles.get(i);
/* 706 */           TexturePacker.Rect rect2 = (TexturePacker.Rect)freeRectangles.get(j);
/* 707 */           if (isContainedIn(rect1, rect2)) {
/* 708 */             freeRectangles.removeIndex(i);
/* 709 */             i--;
/* 710 */             n--;
/*     */             break;
/*     */           } 
/* 713 */           if (isContainedIn(rect2, rect1)) {
/* 714 */             freeRectangles.removeIndex(j);
/* 715 */             j--;
/* 716 */             n--;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     private boolean isContainedIn(TexturePacker.Rect a, TexturePacker.Rect b) {
/* 722 */       return (a.x >= b.x && a.y >= b.y && a.x + a.width <= b.x + b.width && a.y + a.height <= b.y + b.height);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum FreeRectChoiceHeuristic
/*     */   {
/* 728 */     BestShortSideFit,
/*     */     
/* 730 */     BestLongSideFit,
/*     */     
/* 732 */     BestAreaFit,
/*     */     
/* 734 */     BottomLeftRule,
/*     */     
/* 736 */     ContactPointRule;
/*     */   }
/*     */   
/*     */   class RectComparator implements Comparator<TexturePacker.Rect> {
/*     */     public int compare(TexturePacker.Rect o1, TexturePacker.Rect o2) {
/* 741 */       return TexturePacker.Rect.getAtlasName(o1.name, MaxRectsPacker.this.settings.flattenPaths).compareTo(TexturePacker.Rect.getAtlasName(o2.name, MaxRectsPacker.this.settings.flattenPaths));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\MaxRectsPacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */