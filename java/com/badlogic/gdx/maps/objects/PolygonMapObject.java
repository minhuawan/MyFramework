/*    */ package com.badlogic.gdx.maps.objects;
/*    */ 
/*    */ import com.badlogic.gdx.maps.MapObject;
/*    */ import com.badlogic.gdx.math.Polygon;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PolygonMapObject
/*    */   extends MapObject
/*    */ {
/*    */   private Polygon polygon;
/*    */   
/*    */   public Polygon getPolygon() {
/* 17 */     return this.polygon;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPolygon(Polygon polygon) {
/* 22 */     this.polygon = polygon;
/*    */   }
/*    */ 
/*    */   
/*    */   public PolygonMapObject() {
/* 27 */     this(new float[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public PolygonMapObject(float[] vertices) {
/* 32 */     this.polygon = new Polygon(vertices);
/*    */   }
/*    */ 
/*    */   
/*    */   public PolygonMapObject(Polygon polygon) {
/* 37 */     this.polygon = polygon;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\objects\PolygonMapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */