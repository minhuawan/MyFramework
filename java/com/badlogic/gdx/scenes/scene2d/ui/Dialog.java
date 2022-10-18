/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.scenes.scene2d.Action;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Group;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.actions.Actions;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
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
/*     */ public class Dialog
/*     */   extends Window
/*     */ {
/*     */   Table contentTable;
/*     */   Table buttonTable;
/*     */   private Skin skin;
/*  44 */   ObjectMap<Actor, Object> values = new ObjectMap();
/*     */   
/*     */   boolean cancelHide;
/*     */   Actor previousKeyboardFocus;
/*     */   
/*  49 */   protected InputListener ignoreTouchDown = new InputListener() {
/*     */       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  51 */         event.cancel();
/*  52 */         return false;
/*     */       }
/*     */     };
/*     */   Actor previousScrollFocus; FocusListener focusListener;
/*     */   public Dialog(String title, Skin skin) {
/*  57 */     super(title, skin.<Window.WindowStyle>get(Window.WindowStyle.class));
/*  58 */     setSkin(skin);
/*  59 */     this.skin = skin;
/*  60 */     initialize();
/*     */   }
/*     */   
/*     */   public Dialog(String title, Skin skin, String windowStyleName) {
/*  64 */     super(title, skin.<Window.WindowStyle>get(windowStyleName, Window.WindowStyle.class));
/*  65 */     setSkin(skin);
/*  66 */     this.skin = skin;
/*  67 */     initialize();
/*     */   }
/*     */   
/*     */   public Dialog(String title, Window.WindowStyle windowStyle) {
/*  71 */     super(title, windowStyle);
/*  72 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  76 */     setModal(true);
/*     */     
/*  78 */     defaults().space(6.0F);
/*  79 */     add(this.contentTable = new Table(this.skin)).expand().fill();
/*  80 */     row();
/*  81 */     add(this.buttonTable = new Table(this.skin)).fillX();
/*     */     
/*  83 */     this.contentTable.defaults().space(6.0F);
/*  84 */     this.buttonTable.defaults().space(6.0F);
/*     */     
/*  86 */     this.buttonTable.addListener((EventListener)new ChangeListener() { public void changed(ChangeListener.ChangeEvent event, Actor actor) {
/*     */             Group group;
/*  88 */             if (!Dialog.this.values.containsKey(actor))
/*  89 */               return;  while (actor.getParent() != Dialog.this.buttonTable)
/*  90 */               group = actor.getParent(); 
/*  91 */             Dialog.this.result(Dialog.this.values.get(group));
/*  92 */             if (!Dialog.this.cancelHide) Dialog.this.hide(); 
/*  93 */             Dialog.this.cancelHide = false;
/*     */           } }
/*     */       );
/*     */     
/*  97 */     this.focusListener = new FocusListener() {
/*     */         public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
/*  99 */           if (!focused) focusChanged(event); 
/*     */         }
/*     */         
/*     */         public void scrollFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
/* 103 */           if (!focused) focusChanged(event); 
/*     */         }
/*     */         
/*     */         private void focusChanged(FocusListener.FocusEvent event) {
/* 107 */           Stage stage = Dialog.this.getStage();
/* 108 */           if (Dialog.this.isModal && stage != null && (stage.getRoot().getChildren()).size > 0 && stage
/* 109 */             .getRoot().getChildren().peek() == Dialog.this) {
/* 110 */             Actor newFocusedActor = event.getRelatedActor();
/* 111 */             if (newFocusedActor != null && !newFocusedActor.isDescendantOf((Actor)Dialog.this) && 
/* 112 */               !newFocusedActor.equals(Dialog.this.previousKeyboardFocus) && !newFocusedActor.equals(Dialog.this.previousScrollFocus))
/* 113 */               event.cancel(); 
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected void setStage(Stage stage) {
/* 120 */     if (stage == null) {
/* 121 */       addListener((EventListener)this.focusListener);
/*     */     } else {
/* 123 */       removeListener((EventListener)this.focusListener);
/* 124 */     }  super.setStage(stage);
/*     */   }
/*     */   
/*     */   public Table getContentTable() {
/* 128 */     return this.contentTable;
/*     */   }
/*     */   
/*     */   public Table getButtonTable() {
/* 132 */     return this.buttonTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dialog text(String text) {
/* 137 */     if (this.skin == null)
/* 138 */       throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin."); 
/* 139 */     return text(text, this.skin.<Label.LabelStyle>get(Label.LabelStyle.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public Dialog text(String text, Label.LabelStyle labelStyle) {
/* 144 */     return text(new Label(text, labelStyle));
/*     */   }
/*     */ 
/*     */   
/*     */   public Dialog text(Label label) {
/* 149 */     this.contentTable.add(label);
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dialog button(String text) {
/* 156 */     return button(text, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dialog button(String text, Object object) {
/* 162 */     if (this.skin == null)
/* 163 */       throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin."); 
/* 164 */     return button(text, object, this.skin.<TextButton.TextButtonStyle>get(TextButton.TextButtonStyle.class));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dialog button(String text, Object object, TextButton.TextButtonStyle buttonStyle) {
/* 170 */     return button(new TextButton(text, buttonStyle), object);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dialog button(Button button) {
/* 175 */     return button(button, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dialog button(Button button, Object object) {
/* 181 */     this.buttonTable.add(button);
/* 182 */     setObject((Actor)button, object);
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dialog show(Stage stage, Action action) {
/* 188 */     clearActions();
/* 189 */     removeCaptureListener((EventListener)this.ignoreTouchDown);
/*     */     
/* 191 */     this.previousKeyboardFocus = null;
/* 192 */     Actor actor = stage.getKeyboardFocus();
/* 193 */     if (actor != null && !actor.isDescendantOf((Actor)this)) this.previousKeyboardFocus = actor;
/*     */     
/* 195 */     this.previousScrollFocus = null;
/* 196 */     actor = stage.getScrollFocus();
/* 197 */     if (actor != null && !actor.isDescendantOf((Actor)this)) this.previousScrollFocus = actor;
/*     */     
/* 199 */     pack();
/* 200 */     stage.addActor((Actor)this);
/* 201 */     stage.setKeyboardFocus((Actor)this);
/* 202 */     stage.setScrollFocus((Actor)this);
/* 203 */     if (action != null) addAction(action);
/*     */     
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dialog show(Stage stage) {
/* 210 */     show(stage, (Action)Actions.sequence((Action)Actions.alpha(0.0F), (Action)Actions.fadeIn(0.4F, Interpolation.fade)));
/* 211 */     setPosition(Math.round((stage.getWidth() - getWidth()) / 2.0F), Math.round((stage.getHeight() - getHeight()) / 2.0F));
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void hide(Action action) {
/* 217 */     Stage stage = getStage();
/* 218 */     if (stage != null) {
/* 219 */       removeListener((EventListener)this.focusListener);
/* 220 */       if (this.previousKeyboardFocus != null && this.previousKeyboardFocus.getStage() == null) this.previousKeyboardFocus = null; 
/* 221 */       Actor actor = stage.getKeyboardFocus();
/* 222 */       if (actor == null || actor.isDescendantOf((Actor)this)) stage.setKeyboardFocus(this.previousKeyboardFocus);
/*     */       
/* 224 */       if (this.previousScrollFocus != null && this.previousScrollFocus.getStage() == null) this.previousScrollFocus = null; 
/* 225 */       actor = stage.getScrollFocus();
/* 226 */       if (actor == null || actor.isDescendantOf((Actor)this)) stage.setScrollFocus(this.previousScrollFocus); 
/*     */     } 
/* 228 */     if (action != null) {
/* 229 */       addCaptureListener((EventListener)this.ignoreTouchDown);
/* 230 */       addAction((Action)Actions.sequence(action, (Action)Actions.removeListener((EventListener)this.ignoreTouchDown, true), (Action)Actions.removeActor()));
/*     */     } else {
/* 232 */       remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void hide() {
/* 238 */     hide((Action)Actions.sequence((Action)Actions.fadeOut(0.4F, Interpolation.fade), (Action)Actions.removeListener((EventListener)this.ignoreTouchDown, true), (Action)Actions.removeActor()));
/*     */   }
/*     */   
/*     */   public void setObject(Actor actor, Object object) {
/* 242 */     this.values.put(actor, object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dialog key(final int keycode, final Object object) {
/* 248 */     addListener((EventListener)new InputListener() {
/*     */           public boolean keyDown(InputEvent event, int keycode2) {
/* 250 */             if (keycode == keycode2) {
/* 251 */               Dialog.this.result(object);
/* 252 */               if (!Dialog.this.cancelHide) Dialog.this.hide(); 
/* 253 */               Dialog.this.cancelHide = false;
/*     */             } 
/* 255 */             return false;
/*     */           }
/*     */         });
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void result(Object object) {}
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 267 */     this.cancelHide = true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Dialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */