/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Group;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
/*     */ public class Window
/*     */   extends Table
/*     */ {
/*  40 */   private static final Vector2 tmpPosition = new Vector2();
/*  41 */   private static final Vector2 tmpSize = new Vector2(); private static final int MOVE = 32;
/*     */   private WindowStyle style;
/*     */   boolean isMovable = true;
/*     */   boolean isModal;
/*     */   boolean isResizable;
/*  46 */   int resizeBorder = 8;
/*     */   
/*     */   boolean keepWithinStage = true;
/*     */   Label titleLabel;
/*     */   Table titleTable;
/*     */   boolean drawTitleTable;
/*     */   protected int edge;
/*     */   protected boolean dragging;
/*     */   
/*     */   public Window(String title, Skin skin) {
/*  56 */     this(title, skin.<WindowStyle>get(WindowStyle.class));
/*  57 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public Window(String title, Skin skin, String styleName) {
/*  61 */     this(title, skin.<WindowStyle>get(styleName, WindowStyle.class));
/*  62 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public Window(String title, WindowStyle style) {
/*  66 */     if (title == null) throw new IllegalArgumentException("title cannot be null."); 
/*  67 */     setTouchable(Touchable.enabled);
/*  68 */     setClip(true);
/*     */     
/*  70 */     this.titleLabel = new Label(title, new Label.LabelStyle(style.titleFont, style.titleFontColor));
/*  71 */     this.titleLabel.setEllipsis(true);
/*     */     
/*  73 */     this.titleTable = new Table() {
/*     */         public void draw(Batch batch, float parentAlpha) {
/*  75 */           if (Window.this.drawTitleTable) super.draw(batch, parentAlpha); 
/*     */         }
/*     */       };
/*  78 */     this.titleTable.<Label>add(this.titleLabel).expandX().fillX().minWidth(0.0F);
/*  79 */     addActor((Actor)this.titleTable);
/*     */     
/*  81 */     setStyle(style);
/*  82 */     setWidth(150.0F);
/*  83 */     setHeight(150.0F);
/*     */     
/*  85 */     addCaptureListener((EventListener)new InputListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  87 */             Window.this.toFront();
/*  88 */             return false;
/*     */           }
/*     */         });
/*  91 */     addListener((EventListener)new InputListener() { float startX;
/*     */           float startY;
/*     */           
/*     */           private void updateEdge(float x, float y) {
/*  95 */             float border = Window.this.resizeBorder / 2.0F;
/*  96 */             float width = Window.this.getWidth(), height = Window.this.getHeight();
/*  97 */             float padTop = Window.this.getPadTop(), padLeft = Window.this.getPadLeft(), padBottom = Window.this.getPadBottom(), padRight = Window.this.getPadRight();
/*  98 */             float left = padLeft, right = width - padRight, bottom = padBottom;
/*  99 */             Window.this.edge = 0;
/* 100 */             if (Window.this.isResizable && x >= left - border && x <= right + border && y >= bottom - border) {
/* 101 */               if (x < left + border) Window.this.edge |= 0x8; 
/* 102 */               if (x > right - border) Window.this.edge |= 0x10; 
/* 103 */               if (y < bottom + border) Window.this.edge |= 0x4; 
/* 104 */               if (Window.this.edge != 0) border += 25.0F; 
/* 105 */               if (x < left + border) Window.this.edge |= 0x8; 
/* 106 */               if (x > right - border) Window.this.edge |= 0x10; 
/* 107 */               if (y < bottom + border) Window.this.edge |= 0x4; 
/*     */             } 
/* 109 */             if (Window.this.isMovable && Window.this.edge == 0 && y <= height && y >= height - padTop && x >= left && x <= right) Window.this.edge = 32; 
/*     */           }
/*     */           float lastX; float lastY;
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/* 113 */             if (button == 0) {
/* 114 */               updateEdge(x, y);
/* 115 */               Window.this.dragging = (Window.this.edge != 0);
/* 116 */               this.startX = x;
/* 117 */               this.startY = y;
/* 118 */               this.lastX = x - Window.this.getWidth();
/* 119 */               this.lastY = y - Window.this.getHeight();
/*     */             } 
/* 121 */             return (Window.this.edge != 0 || Window.this.isModal);
/*     */           }
/*     */           
/*     */           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/* 125 */             Window.this.dragging = false;
/*     */           }
/*     */           
/*     */           public void touchDragged(InputEvent event, float x, float y, int pointer) {
/* 129 */             if (!Window.this.dragging)
/* 130 */               return;  float width = Window.this.getWidth(), height = Window.this.getHeight();
/* 131 */             float windowX = Window.this.getX(), windowY = Window.this.getY();
/*     */             
/* 133 */             float minWidth = Window.this.getMinWidth(), maxWidth = Window.this.getMaxWidth();
/* 134 */             float minHeight = Window.this.getMinHeight(), maxHeight = Window.this.getMaxHeight();
/* 135 */             Stage stage = Window.this.getStage();
/* 136 */             boolean clampPosition = (Window.this.keepWithinStage && Window.this.getParent() == stage.getRoot());
/*     */             
/* 138 */             if ((Window.this.edge & 0x20) != 0) {
/* 139 */               float amountX = x - this.startX, amountY = y - this.startY;
/* 140 */               windowX += amountX;
/* 141 */               windowY += amountY;
/*     */             } 
/* 143 */             if ((Window.this.edge & 0x8) != 0) {
/* 144 */               float amountX = x - this.startX;
/* 145 */               if (width - amountX < minWidth) amountX = -(minWidth - width); 
/* 146 */               if (clampPosition && windowX + amountX < 0.0F) amountX = -windowX; 
/* 147 */               width -= amountX;
/* 148 */               windowX += amountX;
/*     */             } 
/* 150 */             if ((Window.this.edge & 0x4) != 0) {
/* 151 */               float amountY = y - this.startY;
/* 152 */               if (height - amountY < minHeight) amountY = -(minHeight - height); 
/* 153 */               if (clampPosition && windowY + amountY < 0.0F) amountY = -windowY; 
/* 154 */               height -= amountY;
/* 155 */               windowY += amountY;
/*     */             } 
/* 157 */             if ((Window.this.edge & 0x10) != 0) {
/* 158 */               float amountX = x - this.lastX - width;
/* 159 */               if (width + amountX < minWidth) amountX = minWidth - width; 
/* 160 */               if (clampPosition && windowX + width + amountX > stage.getWidth()) amountX = stage.getWidth() - windowX - width; 
/* 161 */               width += amountX;
/*     */             } 
/* 163 */             if ((Window.this.edge & 0x2) != 0) {
/* 164 */               float amountY = y - this.lastY - height;
/* 165 */               if (height + amountY < minHeight) amountY = minHeight - height; 
/* 166 */               if (clampPosition && windowY + height + amountY > stage.getHeight())
/* 167 */                 amountY = stage.getHeight() - windowY - height; 
/* 168 */               height += amountY;
/*     */             } 
/* 170 */             Window.this.setBounds(Math.round(windowX), Math.round(windowY), Math.round(width), Math.round(height));
/*     */           }
/*     */           
/*     */           public boolean mouseMoved(InputEvent event, float x, float y) {
/* 174 */             updateEdge(x, y);
/* 175 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean scrolled(InputEvent event, float x, float y, int amount) {
/* 179 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean keyDown(InputEvent event, int keycode) {
/* 183 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean keyUp(InputEvent event, int keycode) {
/* 187 */             return Window.this.isModal;
/*     */           }
/*     */           
/*     */           public boolean keyTyped(InputEvent event, char character) {
/* 191 */             return Window.this.isModal;
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   public void setStyle(WindowStyle style) {
/* 197 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/* 198 */     this.style = style;
/* 199 */     setBackground(style.background);
/* 200 */     this.titleLabel.setStyle(new Label.LabelStyle(style.titleFont, style.titleFontColor));
/* 201 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public WindowStyle getStyle() {
/* 207 */     return this.style;
/*     */   }
/*     */   
/*     */   void keepWithinStage() {
/* 211 */     if (!this.keepWithinStage)
/* 212 */       return;  Stage stage = getStage();
/* 213 */     Camera camera = stage.getCamera();
/* 214 */     if (camera instanceof OrthographicCamera) {
/* 215 */       OrthographicCamera orthographicCamera = (OrthographicCamera)camera;
/* 216 */       float parentWidth = stage.getWidth();
/* 217 */       float parentHeight = stage.getHeight();
/* 218 */       if (getX(16) - camera.position.x > parentWidth / 2.0F / orthographicCamera.zoom)
/* 219 */         setPosition(camera.position.x + parentWidth / 2.0F / orthographicCamera.zoom, getY(16), 16); 
/* 220 */       if (getX(8) - camera.position.x < -parentWidth / 2.0F / orthographicCamera.zoom)
/* 221 */         setPosition(camera.position.x - parentWidth / 2.0F / orthographicCamera.zoom, getY(8), 8); 
/* 222 */       if (getY(2) - camera.position.y > parentHeight / 2.0F / orthographicCamera.zoom)
/* 223 */         setPosition(getX(2), camera.position.y + parentHeight / 2.0F / orthographicCamera.zoom, 2); 
/* 224 */       if (getY(4) - camera.position.y < -parentHeight / 2.0F / orthographicCamera.zoom)
/* 225 */         setPosition(getX(4), camera.position.y - parentHeight / 2.0F / orthographicCamera.zoom, 4); 
/* 226 */     } else if (getParent() == stage.getRoot()) {
/* 227 */       float parentWidth = stage.getWidth();
/* 228 */       float parentHeight = stage.getHeight();
/* 229 */       if (getX() < 0.0F) setX(0.0F); 
/* 230 */       if (getRight() > parentWidth) setX(parentWidth - getWidth()); 
/* 231 */       if (getY() < 0.0F) setY(0.0F); 
/* 232 */       if (getTop() > parentHeight) setY(parentHeight - getHeight()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 237 */     Stage stage = getStage();
/* 238 */     if (stage.getKeyboardFocus() == null) stage.setKeyboardFocus((Actor)this);
/*     */     
/* 240 */     keepWithinStage();
/*     */     
/* 242 */     if (this.style.stageBackground != null) {
/* 243 */       stageToLocalCoordinates(tmpPosition.set(0.0F, 0.0F));
/* 244 */       stageToLocalCoordinates(tmpSize.set(stage.getWidth(), stage.getHeight()));
/* 245 */       drawStageBackground(batch, parentAlpha, getX() + tmpPosition.x, getY() + tmpPosition.y, getX() + tmpSize.x, 
/* 246 */           getY() + tmpSize.y);
/*     */     } 
/*     */     
/* 249 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   protected void drawStageBackground(Batch batch, float parentAlpha, float x, float y, float width, float height) {
/* 253 */     Color color = getColor();
/* 254 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/* 255 */     this.style.stageBackground.draw(batch, x, y, width, height);
/*     */   }
/*     */   
/*     */   protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
/* 259 */     super.drawBackground(batch, parentAlpha, x, y);
/*     */ 
/*     */     
/* 262 */     (this.titleTable.getColor()).a = (getColor()).a;
/* 263 */     float padTop = getPadTop(), padLeft = getPadLeft();
/* 264 */     this.titleTable.setSize(getWidth() - padLeft - getPadRight(), padTop);
/* 265 */     this.titleTable.setPosition(padLeft, getHeight() - padTop);
/* 266 */     this.drawTitleTable = true;
/* 267 */     this.titleTable.draw(batch, parentAlpha);
/* 268 */     this.drawTitleTable = false;
/*     */   }
/*     */   
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 272 */     Actor hit = super.hit(x, y, touchable);
/* 273 */     if (hit == null && this.isModal && (!touchable || getTouchable() == Touchable.enabled)) return (Actor)this; 
/* 274 */     float height = getHeight();
/* 275 */     if (hit == null || hit == this) return hit; 
/* 276 */     if (y <= height && y >= height - getPadTop() && x >= 0.0F && x <= getWidth()) {
/*     */       Group group;
/* 278 */       Actor current = hit;
/* 279 */       while (current.getParent() != this)
/* 280 */         group = current.getParent(); 
/* 281 */       if (getCell(group) != null) return (Actor)this; 
/*     */     } 
/* 283 */     return hit;
/*     */   }
/*     */   
/*     */   public boolean isMovable() {
/* 287 */     return this.isMovable;
/*     */   }
/*     */   
/*     */   public void setMovable(boolean isMovable) {
/* 291 */     this.isMovable = isMovable;
/*     */   }
/*     */   
/*     */   public boolean isModal() {
/* 295 */     return this.isModal;
/*     */   }
/*     */   
/*     */   public void setModal(boolean isModal) {
/* 299 */     this.isModal = isModal;
/*     */   }
/*     */   
/*     */   public void setKeepWithinStage(boolean keepWithinStage) {
/* 303 */     this.keepWithinStage = keepWithinStage;
/*     */   }
/*     */   
/*     */   public boolean isResizable() {
/* 307 */     return this.isResizable;
/*     */   }
/*     */   
/*     */   public void setResizable(boolean isResizable) {
/* 311 */     this.isResizable = isResizable;
/*     */   }
/*     */   
/*     */   public void setResizeBorder(int resizeBorder) {
/* 315 */     this.resizeBorder = resizeBorder;
/*     */   }
/*     */   
/*     */   public boolean isDragging() {
/* 319 */     return this.dragging;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 323 */     return Math.max(super.getPrefWidth(), this.titleLabel.getPrefWidth() + getPadLeft() + getPadRight());
/*     */   }
/*     */   
/*     */   public Table getTitleTable() {
/* 327 */     return this.titleTable;
/*     */   }
/*     */   
/*     */   public Label getTitleLabel() {
/* 331 */     return this.titleLabel;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class WindowStyle
/*     */   {
/*     */     public Drawable background;
/*     */     
/*     */     public BitmapFont titleFont;
/*     */     
/* 341 */     public Color titleFontColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*     */     public Drawable stageBackground;
/*     */ 
/*     */     
/*     */     public WindowStyle() {}
/*     */     
/*     */     public WindowStyle(BitmapFont titleFont, Color titleFontColor, Drawable background) {
/* 349 */       this.background = background;
/* 350 */       this.titleFont = titleFont;
/* 351 */       this.titleFontColor.set(titleFontColor);
/*     */     }
/*     */     
/*     */     public WindowStyle(WindowStyle style) {
/* 355 */       this.background = style.background;
/* 356 */       this.titleFont = style.titleFont;
/* 357 */       this.titleFontColor = new Color(style.titleFontColor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Window.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */