/*     */ package com.megacrit.cardcrawl.screens.compendium;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.cards.CardGroup;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.GameCursor;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.helpers.CardLibrary;
/*     */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*     */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*     */ import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
/*     */ import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
/*     */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*     */ import com.megacrit.cardcrawl.localization.UIStrings;
/*     */ import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
/*     */ import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
/*     */ import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
/*     */ import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
/*     */ import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
/*     */ import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
/*     */ import com.megacrit.cardcrawl.screens.mainMenu.TabBarListener;
/*     */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CardLibraryScreen
/*     */   implements TabBarListener, ScrollBarListener
/*     */ {
/*  38 */   private static final Logger logger = LogManager.getLogger(CardLibraryScreen.class.getName());
/*  39 */   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CardLibraryScreen");
/*  40 */   public static final String[] TEXT = uiStrings.TEXT;
/*     */   private static float drawStartX;
/*  42 */   private static float drawStartY = Settings.HEIGHT * 0.66F; private static float padX;
/*  43 */   private static final int CARDS_PER_LINE = (int)(Settings.WIDTH / (AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X * 3.0F));
/*     */   private static float padY;
/*     */   private boolean grabbedScreen = false;
/*  46 */   private float grabStartY = 0.0F, currentDiffY = 0.0F;
/*  47 */   private float scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
/*  48 */   private float scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
/*  49 */   private AbstractCard hoveredCard = null; private AbstractCard clickStartedCard = null;
/*     */   
/*     */   private ColorTabBar colorBar;
/*  52 */   public MenuCancelButton button = new MenuCancelButton();
/*  53 */   private CardGroup redCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*  54 */   private CardGroup greenCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*  55 */   private CardGroup blueCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*  56 */   private CardGroup purpleCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*  57 */   private CardGroup colorlessCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*  58 */   private CardGroup curseCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
/*     */   
/*     */   private CardLibSortHeader sortHeader;
/*     */   
/*     */   private CardGroup visibleCards;
/*     */   private ScrollBar scrollBar;
/*  64 */   private CardLibSelectionType type = CardLibSelectionType.NONE;
/*  65 */   private Texture filterSelectionImg = null;
/*  66 */   private int selectionIndex = 0;
/*  67 */   private AbstractCard controllerCard = null;
/*  68 */   private Color highlightBoxColor = new Color(1.0F, 0.95F, 0.5F, 0.0F);
/*     */   
/*     */   public CardLibraryScreen() {
/*  71 */     drawStartX = Settings.WIDTH;
/*  72 */     drawStartX -= CARDS_PER_LINE * AbstractCard.IMG_WIDTH * 0.75F;
/*  73 */     drawStartX -= (CARDS_PER_LINE - 1) * Settings.CARD_VIEW_PAD_X;
/*  74 */     drawStartX /= 2.0F;
/*  75 */     drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;
/*  76 */     padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
/*  77 */     padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
/*  78 */     this.colorBar = new ColorTabBar(this);
/*  79 */     this.sortHeader = new CardLibSortHeader(null);
/*  80 */     this.scrollBar = new ScrollBar(this);
/*     */   }
/*     */   
/*     */   public void initialize() {
/*  84 */     logger.info("Initializing card library screen.");
/*     */     
/*  86 */     this.redCards.group = CardLibrary.getCardList(CardLibrary.LibraryType.RED);
/*  87 */     this.greenCards.group = CardLibrary.getCardList(CardLibrary.LibraryType.GREEN);
/*  88 */     this.blueCards.group = CardLibrary.getCardList(CardLibrary.LibraryType.BLUE);
/*  89 */     this.purpleCards.group = CardLibrary.getCardList(CardLibrary.LibraryType.PURPLE);
/*  90 */     this.colorlessCards.group = CardLibrary.getCardList(CardLibrary.LibraryType.COLORLESS);
/*  91 */     this.curseCards.group = CardLibrary.getCardList(CardLibrary.LibraryType.CURSE);
/*     */     
/*  93 */     this.visibleCards = this.redCards;
/*  94 */     this.sortHeader.setGroup(this.visibleCards);
/*  95 */     calculateScrollBounds();
/*     */   }
/*     */   
/*     */   private void setLockStatus() {
/*  99 */     lockStatusHelper(this.redCards);
/* 100 */     lockStatusHelper(this.greenCards);
/* 101 */     lockStatusHelper(this.blueCards);
/* 102 */     lockStatusHelper(this.purpleCards);
/* 103 */     lockStatusHelper(this.colorlessCards);
/* 104 */     lockStatusHelper(this.curseCards);
/*     */   }
/*     */   
/*     */   private void lockStatusHelper(CardGroup group) {
/* 108 */     ArrayList<AbstractCard> toAdd = new ArrayList<>();
/* 109 */     for (Iterator<AbstractCard> i = group.group.iterator(); i.hasNext(); ) {
/* 110 */       AbstractCard c = i.next();
/* 111 */       if (UnlockTracker.isCardLocked(c.cardID)) {
/* 112 */         AbstractCard tmp = CardLibrary.getCopy(c.cardID);
/* 113 */         tmp.setLocked();
/* 114 */         toAdd.add(tmp);
/* 115 */         i.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     group.group.addAll(toAdd);
/*     */   }
/*     */   
/*     */   public void open() {
/* 123 */     this.controllerCard = null;
/* 124 */     if (Settings.isInfo) {
/* 125 */       CardLibrary.unlockAndSeeAllCards();
/*     */     }
/*     */     
/* 128 */     if (this.filterSelectionImg == null) {
/* 129 */       this.filterSelectionImg = ImageMaster.loadImage("images/ui/cardlibrary/selectBox.png");
/*     */     }
/*     */     
/* 132 */     setLockStatus();
/* 133 */     sortOnOpen();
/* 134 */     this.button.show(TEXT[0]);
/* 135 */     this.currentDiffY = this.scrollLowerBound;
/*     */     
/* 137 */     SingleCardViewPopup.isViewingUpgrade = false;
/* 138 */     CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.CARD_LIBRARY;
/*     */   }
/*     */   
/*     */   private void sortOnOpen() {
/* 142 */     this.sortHeader.justSorted = true;
/* 143 */     this.visibleCards.sortAlphabetically(true);
/* 144 */     this.visibleCards.sortByRarity(true);
/* 145 */     this.visibleCards.sortByStatus(true);
/*     */     
/* 147 */     for (AbstractCard c : this.visibleCards.group) {
/* 148 */       c.drawScale = MathUtils.random(0.6F, 0.65F);
/* 149 */       c.targetDrawScale = 0.75F;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void update() {
/* 154 */     updateControllerInput();
/* 155 */     if (Settings.isControllerMode && this.controllerCard != null && !CardCrawlGame.isPopupOpen) {
/* 156 */       if (Gdx.input.getY() > Settings.HEIGHT * 0.75F) {
/* 157 */         this.currentDiffY += Settings.SCROLL_SPEED;
/* 158 */       } else if (Gdx.input.getY() < Settings.HEIGHT * 0.25F) {
/* 159 */         this.currentDiffY -= Settings.SCROLL_SPEED;
/*     */       } 
/*     */     }
/*     */     
/* 163 */     this.colorBar.update((this.visibleCards.getBottomCard()).current_y + 230.0F * Settings.yScale);
/* 164 */     this.sortHeader.update();
/*     */     
/* 166 */     if (this.hoveredCard != null) {
/* 167 */       CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
/* 168 */       if (InputHelper.justClickedLeft) {
/* 169 */         this.clickStartedCard = this.hoveredCard;
/*     */       }
/* 171 */       if ((InputHelper.justReleasedClickLeft && this.clickStartedCard != null && this.hoveredCard != null) || (this.hoveredCard != null && CInputActionSet.select
/* 172 */         .isJustPressed())) {
/*     */         
/* 174 */         if (Settings.isControllerMode) {
/* 175 */           this.clickStartedCard = this.hoveredCard;
/*     */         }
/*     */         
/* 178 */         InputHelper.justReleasedClickLeft = false;
/* 179 */         CardCrawlGame.cardPopup.open(this.clickStartedCard, this.visibleCards);
/* 180 */         this.clickStartedCard = null;
/*     */       } 
/*     */     } else {
/* 183 */       this.clickStartedCard = null;
/*     */     } 
/*     */     
/* 186 */     boolean isScrollBarScrolling = this.scrollBar.update();
/* 187 */     if (!CardCrawlGame.cardPopup.isOpen && !isScrollBarScrolling) {
/* 188 */       updateScrolling();
/*     */     }
/* 190 */     updateCards();
/*     */     
/* 192 */     this.button.update();
/* 193 */     if (this.button.hb.clicked || InputHelper.pressedEscape) {
/* 194 */       InputHelper.pressedEscape = false;
/* 195 */       this.button.hb.clicked = false;
/* 196 */       this.button.hide();
/* 197 */       CardCrawlGame.mainMenuScreen.panelScreen.refresh();
/*     */     } 
/*     */     
/* 200 */     if (Settings.isControllerMode && this.controllerCard != null)
/* 201 */       CInputHelper.setCursor(this.controllerCard.hb); 
/*     */   }
/*     */   
/*     */   private enum CardLibSelectionType
/*     */   {
/* 206 */     NONE, FILTERS, CARDS;
/*     */   }
/*     */   
/*     */   private void updateControllerInput() {
/* 210 */     if (!Settings.isControllerMode) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     this.selectionIndex = 0;
/* 215 */     boolean anyHovered = false;
/* 216 */     this.type = CardLibSelectionType.NONE;
/*     */     
/* 218 */     if (this.colorBar.viewUpgradeHb.hovered) {
/* 219 */       anyHovered = true;
/* 220 */       this.type = CardLibSelectionType.FILTERS;
/* 221 */       this.selectionIndex = 4;
/* 222 */       this.controllerCard = null;
/* 223 */     } else if (this.sortHeader.updateControllerInput() != null) {
/* 224 */       anyHovered = true;
/* 225 */       this.controllerCard = null;
/* 226 */       this.type = CardLibSelectionType.FILTERS;
/* 227 */       this.selectionIndex = this.sortHeader.getHoveredIndex();
/*     */     } else {
/* 229 */       for (AbstractCard c : this.visibleCards.group) {
/* 230 */         if (c.hb.hovered) {
/* 231 */           anyHovered = true;
/* 232 */           this.type = CardLibSelectionType.CARDS;
/*     */           break;
/*     */         } 
/* 235 */         this.selectionIndex++;
/*     */       } 
/*     */     } 
/*     */     
/* 239 */     if (!anyHovered) {
/* 240 */       CInputHelper.setCursor(((AbstractCard)this.visibleCards.group.get(0)).hb);
/*     */       
/*     */       return;
/*     */     } 
/* 244 */     switch (this.type) {
/*     */       case RED:
/* 246 */         if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && this.visibleCards
/* 247 */           .size() > CARDS_PER_LINE) {
/*     */           
/* 249 */           if (this.selectionIndex < CARDS_PER_LINE) {
/* 250 */             CInputHelper.setCursor((this.sortHeader.buttons[0]).hb);
/* 251 */             this.controllerCard = null;
/*     */             
/*     */             return;
/*     */           } 
/* 255 */           this.selectionIndex -= 5;
/*     */ 
/*     */           
/* 258 */           CInputHelper.setCursor(((AbstractCard)this.visibleCards.group.get(this.selectionIndex)).hb);
/* 259 */           this.controllerCard = this.visibleCards.group.get(this.selectionIndex); break;
/* 260 */         }  if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && this.visibleCards
/* 261 */           .size() > CARDS_PER_LINE) {
/* 262 */           if (this.selectionIndex < this.visibleCards.size() - CARDS_PER_LINE) {
/* 263 */             this.selectionIndex += CARDS_PER_LINE;
/*     */           } else {
/* 265 */             this.selectionIndex %= CARDS_PER_LINE;
/*     */           } 
/*     */           
/* 268 */           CInputHelper.setCursor(((AbstractCard)this.visibleCards.group.get(this.selectionIndex)).hb);
/* 269 */           this.controllerCard = this.visibleCards.group.get(this.selectionIndex); break;
/* 270 */         }  if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/* 271 */           if (this.selectionIndex % CARDS_PER_LINE > 0) {
/* 272 */             this.selectionIndex--;
/*     */           } else {
/* 274 */             this.selectionIndex += CARDS_PER_LINE - 1;
/* 275 */             if (this.selectionIndex > this.visibleCards.size() - 1) {
/* 276 */               this.selectionIndex = this.visibleCards.size() - 1;
/*     */             }
/*     */           } 
/*     */           
/* 280 */           CInputHelper.setCursor(((AbstractCard)this.visibleCards.group.get(this.selectionIndex)).hb);
/* 281 */           this.controllerCard = this.visibleCards.group.get(this.selectionIndex); break;
/* 282 */         }  if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/* 283 */           if (this.selectionIndex % CARDS_PER_LINE < CARDS_PER_LINE - 1) {
/* 284 */             this.selectionIndex++;
/* 285 */             if (this.selectionIndex > this.visibleCards.size() - 1) {
/* 286 */               this.selectionIndex -= this.visibleCards.size() % CARDS_PER_LINE;
/*     */             }
/*     */           } else {
/* 289 */             this.selectionIndex -= CARDS_PER_LINE - 1;
/* 290 */             if (this.selectionIndex < 0) {
/* 291 */               this.selectionIndex = 0;
/*     */             }
/*     */           } 
/*     */           
/* 295 */           CInputHelper.setCursor(((AbstractCard)this.visibleCards.group.get(this.selectionIndex)).hb);
/* 296 */           this.controllerCard = this.visibleCards.group.get(this.selectionIndex);
/*     */         } 
/*     */         break;
/*     */       case GREEN:
/* 300 */         if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
/* 301 */           CInputHelper.setCursor(((AbstractCard)this.visibleCards.group.get(0)).hb); break;
/* 302 */         }  if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
/* 303 */           this.selectionIndex++;
/* 304 */           if (this.selectionIndex == this.sortHeader.buttons.length) {
/* 305 */             CInputHelper.setCursor(this.colorBar.viewUpgradeHb); break;
/*     */           } 
/* 307 */           if (this.selectionIndex > this.sortHeader.buttons.length) {
/* 308 */             this.selectionIndex = 0;
/*     */           }
/* 310 */           CInputHelper.setCursor((this.sortHeader.buttons[this.selectionIndex]).hb); break;
/*     */         } 
/* 312 */         if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
/* 313 */           this.selectionIndex--;
/* 314 */           if (this.selectionIndex == -1) {
/* 315 */             CInputHelper.setCursor(this.colorBar.viewUpgradeHb); break;
/*     */           } 
/* 317 */           if (this.selectionIndex > this.sortHeader.buttons.length - 1) {
/* 318 */             this.selectionIndex = this.sortHeader.buttons.length - 1;
/*     */           }
/* 320 */           CInputHelper.setCursor((this.sortHeader.buttons[this.selectionIndex]).hb);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     if (this.type == CardLibSelectionType.FILTERS) {
/* 330 */       this.sortHeader.selectionIndex = this.selectionIndex;
/*     */     } else {
/* 332 */       this.sortHeader.selectionIndex = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateCards() {
/* 337 */     this.hoveredCard = null;
/* 338 */     int lineNum = 0;
/*     */     
/* 340 */     ArrayList<AbstractCard> cards = this.visibleCards.group;
/*     */     
/* 342 */     for (int i = 0; i < cards.size(); i++) {
/* 343 */       int mod = i % CARDS_PER_LINE;
/* 344 */       if (mod == 0 && i != 0) {
/* 345 */         lineNum++;
/*     */       }
/* 347 */       ((AbstractCard)cards.get(i)).target_x = drawStartX + mod * padX;
/* 348 */       ((AbstractCard)cards.get(i)).target_y = drawStartY + this.currentDiffY - lineNum * padY;
/* 349 */       ((AbstractCard)cards.get(i)).update();
/* 350 */       ((AbstractCard)cards.get(i)).updateHoverLogic();
/*     */       
/* 352 */       if (((AbstractCard)cards.get(i)).hb.hovered) {
/* 353 */         this.hoveredCard = cards.get(i);
/*     */       }
/*     */     } 
/* 356 */     if (this.sortHeader.justSorted) {
/* 357 */       for (AbstractCard c : cards) {
/* 358 */         c.current_x = c.target_x;
/* 359 */         c.current_y = c.target_y;
/*     */       } 
/* 361 */       this.sortHeader.justSorted = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateScrolling() {
/* 366 */     int y = InputHelper.mY;
/*     */     
/* 368 */     if (!this.grabbedScreen) {
/* 369 */       if (InputHelper.scrolledDown) {
/* 370 */         this.currentDiffY += Settings.SCROLL_SPEED;
/* 371 */       } else if (InputHelper.scrolledUp) {
/* 372 */         this.currentDiffY -= Settings.SCROLL_SPEED;
/*     */       } 
/*     */       
/* 375 */       if (InputHelper.justClickedLeft) {
/* 376 */         this.grabbedScreen = true;
/* 377 */         this.grabStartY = y - this.currentDiffY;
/*     */       }
/*     */     
/* 380 */     } else if (InputHelper.isMouseDown) {
/* 381 */       this.currentDiffY = y - this.grabStartY;
/*     */     } else {
/* 383 */       this.grabbedScreen = false;
/*     */     } 
/*     */ 
/*     */     
/* 387 */     resetScrolling();
/* 388 */     updateBarPosition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculateScrollBounds() {
/* 395 */     int size = this.visibleCards.size();
/*     */     
/* 397 */     int scrollTmp = 0;
/* 398 */     if (size > CARDS_PER_LINE * 2) {
/* 399 */       scrollTmp = size / CARDS_PER_LINE - 2;
/* 400 */       if (size % CARDS_PER_LINE != 0) {
/* 401 */         scrollTmp++;
/*     */       }
/* 403 */       this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * padY;
/*     */     } else {
/* 405 */       this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetScrolling() {
/* 413 */     if (this.currentDiffY < this.scrollLowerBound) {
/* 414 */       this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
/* 415 */     } else if (this.currentDiffY > this.scrollUpperBound) {
/* 416 */       this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void render(SpriteBatch sb) {
/* 421 */     this.scrollBar.render(sb);
/* 422 */     this.colorBar.render(sb, (this.visibleCards.getBottomCard()).current_y + 230.0F * Settings.yScale);
/* 423 */     this.sortHeader.render(sb);
/* 424 */     renderGroup(sb, this.visibleCards);
/*     */     
/* 426 */     if (this.hoveredCard != null) {
/* 427 */       this.hoveredCard.renderHoverShadow(sb);
/* 428 */       this.hoveredCard.renderInLibrary(sb);
/*     */     } 
/*     */     
/* 431 */     this.button.render(sb);
/* 432 */     if (Settings.isControllerMode) {
/* 433 */       renderControllerUi(sb);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderControllerUi(SpriteBatch sb) {
/* 439 */     sb.draw(CInputActionSet.pageLeftViewDeck
/* 440 */         .getKeyImg(), 280.0F * Settings.xScale - 32.0F, 
/*     */         
/* 442 */         (this.sortHeader.group.getBottomCard()).current_y + 280.0F * Settings.yScale - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
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
/* 457 */     sb.draw(CInputActionSet.pageRightViewExhaust
/* 458 */         .getKeyImg(), 1640.0F * Settings.xScale - 32.0F, 
/*     */         
/* 460 */         (this.sortHeader.group.getBottomCard()).current_y + 280.0F * Settings.yScale - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
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
/* 476 */     if (this.type == CardLibSelectionType.FILTERS && (
/* 477 */       this.selectionIndex == 4 || (this.selectionIndex == 3 && Settings.removeAtoZSort))) {
/* 478 */       this.highlightBoxColor.a = 0.7F + MathUtils.cosDeg((float)(System.currentTimeMillis() / 2L % 360L)) / 5.0F;
/* 479 */       sb.setColor(this.highlightBoxColor);
/* 480 */       float doop = 1.0F + (1.0F + MathUtils.cosDeg((float)(System.currentTimeMillis() / 2L % 360L))) / 50.0F;
/* 481 */       sb.draw(this.filterSelectionImg, this.colorBar.viewUpgradeHb.cX - 100.0F, this.colorBar.viewUpgradeHb.cY - 43.0F, 100.0F, 43.0F, 200.0F, 86.0F, Settings.scale * doop * this.colorBar.viewUpgradeHb.width / 150.0F / Settings.scale, Settings.scale * doop, 0.0F, 0, 0, 200, 86, false, false);
/*     */     } 
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
/*     */   private void renderGroup(SpriteBatch sb, CardGroup group) {
/* 503 */     group.renderInLibrary(sb);
/* 504 */     group.renderTip(sb);
/*     */   }
/*     */ 
/*     */   
/*     */   public void didChangeTab(ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection) {
/* 509 */     CardGroup oldSelection = this.visibleCards;
/* 510 */     switch (newSelection) {
/*     */       case RED:
/* 512 */         this.visibleCards = this.redCards;
/*     */         break;
/*     */       case GREEN:
/* 515 */         this.visibleCards = this.greenCards;
/*     */         break;
/*     */       case BLUE:
/* 518 */         this.visibleCards = this.blueCards;
/*     */         break;
/*     */       case PURPLE:
/* 521 */         this.visibleCards = this.purpleCards;
/*     */         break;
/*     */       case COLORLESS:
/* 524 */         this.visibleCards = this.colorlessCards;
/*     */         break;
/*     */       case CURSE:
/* 527 */         this.visibleCards = this.curseCards;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 532 */     if (oldSelection != this.visibleCards) {
/* 533 */       this.sortHeader.setGroup(this.visibleCards);
/* 534 */       calculateScrollBounds();
/*     */     } 
/* 536 */     this.sortHeader.justSorted = true;
/*     */     
/* 538 */     for (AbstractCard c : this.visibleCards.group) {
/* 539 */       c.drawScale = MathUtils.random(0.6F, 0.65F);
/* 540 */       c.targetDrawScale = 0.75F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void scrolledUsingBar(float newPercent) {
/* 546 */     this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
/* 547 */     updateBarPosition();
/*     */   }
/*     */   
/*     */   private void updateBarPosition() {
/* 551 */     float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
/* 552 */     this.scrollBar.parentScrolledToPercent(percent);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\screens\compendium\CardLibraryScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */