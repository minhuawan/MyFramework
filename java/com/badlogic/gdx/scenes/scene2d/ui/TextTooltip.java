/*    */ package com.badlogic.gdx.scenes.scene2d.ui;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*    */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
/*    */ public class TextTooltip
/*    */   extends Tooltip<Label>
/*    */ {
/*    */   public TextTooltip(String text, Skin skin) {
/* 27 */     this(text, TooltipManager.getInstance(), skin.<TextTooltipStyle>get(TextTooltipStyle.class));
/*    */   }
/*    */   
/*    */   public TextTooltip(String text, Skin skin, String styleName) {
/* 31 */     this(text, TooltipManager.getInstance(), skin.<TextTooltipStyle>get(styleName, TextTooltipStyle.class));
/*    */   }
/*    */   
/*    */   public TextTooltip(String text, TextTooltipStyle style) {
/* 35 */     this(text, TooltipManager.getInstance(), style);
/*    */   }
/*    */   
/*    */   public TextTooltip(String text, TooltipManager manager, Skin skin) {
/* 39 */     this(text, manager, skin.<TextTooltipStyle>get(TextTooltipStyle.class));
/*    */   }
/*    */   
/*    */   public TextTooltip(String text, TooltipManager manager, Skin skin, String styleName) {
/* 43 */     this(text, manager, skin.<TextTooltipStyle>get(styleName, TextTooltipStyle.class));
/*    */   }
/*    */   
/*    */   public TextTooltip(String text, final TooltipManager manager, TextTooltipStyle style) {
/* 47 */     super(null, manager);
/*    */     
/* 49 */     Label label = new Label(text, style.label);
/* 50 */     label.setWrap(true);
/*    */     
/* 52 */     this.container.setActor(label);
/* 53 */     this.container.width(new Value() {
/*    */           public float get(Actor context) {
/* 55 */             return Math.min(manager.maxWidth, (((Label)TextTooltip.this.container.getActor()).getGlyphLayout()).width);
/*    */           }
/*    */         });
/*    */     
/* 59 */     setStyle(style);
/*    */   }
/*    */   
/*    */   public void setStyle(TextTooltipStyle style) {
/* 63 */     if (style == null) throw new NullPointerException("style cannot be null"); 
/* 64 */     if (!(style instanceof TextTooltipStyle)) throw new IllegalArgumentException("style must be a TextTooltipStyle."); 
/* 65 */     ((Label)this.container.getActor()).setStyle(style.label);
/* 66 */     this.container.setBackground(style.background);
/* 67 */     this.container.maxWidth(style.wrapWidth);
/*    */   }
/*    */ 
/*    */   
/*    */   public static class TextTooltipStyle
/*    */   {
/*    */     public Label.LabelStyle label;
/*    */     
/*    */     public Drawable background;
/*    */     
/*    */     public float wrapWidth;
/*    */ 
/*    */     
/*    */     public TextTooltipStyle() {}
/*    */     
/*    */     public TextTooltipStyle(Label.LabelStyle label, Drawable background) {
/* 83 */       this.label = label;
/* 84 */       this.background = background;
/*    */     }
/*    */     
/*    */     public TextTooltipStyle(TextTooltipStyle style) {
/* 88 */       this.label = new Label.LabelStyle(style.label);
/* 89 */       this.background = style.background;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\TextTooltip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */