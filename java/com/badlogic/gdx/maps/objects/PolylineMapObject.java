/*    */ package com.badlogic.gdx.maps.objects;
/*    */ 
/*    */ import com.badlogic.gdx.maps.MapObject;
/*    */ import com.badlogic.gdx.math.Polyline;
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
/*    */ public class PolylineMapObject
/*    */   extends MapObject
/*    */ {
/*    */   private Polyline polyline;
/*    */   
/*    */   public Polyline getPolyline() {
/* 29 */     return this.polyline;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPolyline(Polyline polyline) {
/* 34 */     this.polyline = polyline;
/*    */   }
/*    */ 
/*    */   
/*    */   public PolylineMapObject() {
/* 39 */     this(new float[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public PolylineMapObject(float[] vertices) {
/* 44 */     this.polyline = new Polyline(vertices);
/*    */   }
/*    */ 
/*    */   
/*    */   public PolylineMapObject(Polyline polyline) {
/* 49 */     this.polyline = polyline;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\objects\PolylineMapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */