/*    */ package com.badlogic.gdx.graphics.g3d.model;
/*    */ 
/*    */ import com.badlogic.gdx.math.Quaternion;
/*    */ import com.badlogic.gdx.math.Vector3;
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
/*    */ public class NodeAnimation
/*    */ {
/*    */   public Node node;
/* 31 */   public Array<NodeKeyframe<Vector3>> translation = null;
/*    */   
/* 33 */   public Array<NodeKeyframe<Quaternion>> rotation = null;
/*    */   
/* 35 */   public Array<NodeKeyframe<Vector3>> scaling = null;
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\NodeAnimation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */