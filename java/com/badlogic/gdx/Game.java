/*    */ package com.badlogic.gdx;
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
/*    */ 
/*    */ public abstract class Game
/*    */   implements ApplicationListener
/*    */ {
/*    */   protected Screen screen;
/*    */   
/*    */   public void dispose() {
/* 31 */     if (this.screen != null) this.screen.hide();
/*    */   
/*    */   }
/*    */   
/*    */   public void pause() {
/* 36 */     if (this.screen != null) this.screen.pause();
/*    */   
/*    */   }
/*    */   
/*    */   public void resume() {
/* 41 */     if (this.screen != null) this.screen.resume();
/*    */   
/*    */   }
/*    */   
/*    */   public void render() {
/* 46 */     if (this.screen != null) this.screen.render(Gdx.graphics.getDeltaTime());
/*    */   
/*    */   }
/*    */   
/*    */   public void resize(int width, int height) {
/* 51 */     if (this.screen != null) this.screen.resize(width, height);
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setScreen(Screen screen) {
/* 58 */     if (this.screen != null) this.screen.hide(); 
/* 59 */     this.screen = screen;
/* 60 */     if (this.screen != null) {
/* 61 */       this.screen.show();
/* 62 */       this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Screen getScreen() {
/* 68 */     return this.screen;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\Game.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */