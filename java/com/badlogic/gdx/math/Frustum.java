/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
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
/*     */ public class Frustum
/*     */ {
/*  28 */   protected static final Vector3[] clipSpacePlanePoints = new Vector3[] { new Vector3(-1.0F, -1.0F, -1.0F), new Vector3(1.0F, -1.0F, -1.0F), new Vector3(1.0F, 1.0F, -1.0F), new Vector3(-1.0F, 1.0F, -1.0F), new Vector3(-1.0F, -1.0F, 1.0F), new Vector3(1.0F, -1.0F, 1.0F), new Vector3(1.0F, 1.0F, 1.0F), new Vector3(-1.0F, 1.0F, 1.0F) };
/*     */ 
/*     */   
/*  31 */   protected static final float[] clipSpacePlanePointsArray = new float[24];
/*     */   
/*     */   static {
/*  34 */     int j = 0;
/*  35 */     for (Vector3 v : clipSpacePlanePoints) {
/*  36 */       clipSpacePlanePointsArray[j++] = v.x;
/*  37 */       clipSpacePlanePointsArray[j++] = v.y;
/*  38 */       clipSpacePlanePointsArray[j++] = v.z;
/*     */     } 
/*     */   }
/*     */   
/*  42 */   private static final Vector3 tmpV = new Vector3();
/*     */ 
/*     */   
/*  45 */   public final Plane[] planes = new Plane[6];
/*     */ 
/*     */   
/*  48 */   public final Vector3[] planePoints = new Vector3[] { new Vector3(), new Vector3(), new Vector3(), new Vector3(), new Vector3(), new Vector3(), new Vector3(), new Vector3() };
/*     */   
/*  50 */   protected final float[] planePointsArray = new float[24];
/*     */   
/*     */   public Frustum() {
/*  53 */     for (int i = 0; i < 6; i++) {
/*  54 */       this.planes[i] = new Plane(new Vector3(), 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Matrix4 inverseProjectionView) {
/*  62 */     System.arraycopy(clipSpacePlanePointsArray, 0, this.planePointsArray, 0, clipSpacePlanePointsArray.length);
/*  63 */     Matrix4.prj(inverseProjectionView.val, this.planePointsArray, 0, 8, 3);
/*  64 */     for (int i = 0, j = 0; i < 8; i++) {
/*  65 */       Vector3 v = this.planePoints[i];
/*  66 */       v.x = this.planePointsArray[j++];
/*  67 */       v.y = this.planePointsArray[j++];
/*  68 */       v.z = this.planePointsArray[j++];
/*     */     } 
/*     */     
/*  71 */     this.planes[0].set(this.planePoints[1], this.planePoints[0], this.planePoints[2]);
/*  72 */     this.planes[1].set(this.planePoints[4], this.planePoints[5], this.planePoints[7]);
/*  73 */     this.planes[2].set(this.planePoints[0], this.planePoints[4], this.planePoints[3]);
/*  74 */     this.planes[3].set(this.planePoints[5], this.planePoints[1], this.planePoints[6]);
/*  75 */     this.planes[4].set(this.planePoints[2], this.planePoints[3], this.planePoints[6]);
/*  76 */     this.planes[5].set(this.planePoints[4], this.planePoints[0], this.planePoints[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean pointInFrustum(Vector3 point) {
/*  84 */     for (int i = 0; i < this.planes.length; i++) {
/*  85 */       Plane.PlaneSide result = this.planes[i].testPoint(point);
/*  86 */       if (result == Plane.PlaneSide.Back) return false; 
/*     */     } 
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean pointInFrustum(float x, float y, float z) {
/*  98 */     for (int i = 0; i < this.planes.length; i++) {
/*  99 */       Plane.PlaneSide result = this.planes[i].testPoint(x, y, z);
/* 100 */       if (result == Plane.PlaneSide.Back) return false; 
/*     */     } 
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean sphereInFrustum(Vector3 center, float radius) {
/* 111 */     for (int i = 0; i < 6; i++) {
/* 112 */       if ((this.planes[i]).normal.x * center.x + (this.planes[i]).normal.y * center.y + (this.planes[i]).normal.z * center.z < -radius - (this.planes[i]).d)
/* 113 */         return false; 
/* 114 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean sphereInFrustum(float x, float y, float z, float radius) {
/* 125 */     for (int i = 0; i < 6; i++) {
/* 126 */       if ((this.planes[i]).normal.x * x + (this.planes[i]).normal.y * y + (this.planes[i]).normal.z * z < -radius - (this.planes[i]).d) return false; 
/* 127 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean sphereInFrustumWithoutNearFar(Vector3 center, float radius) {
/* 136 */     for (int i = 2; i < 6; i++) {
/* 137 */       if ((this.planes[i]).normal.x * center.x + (this.planes[i]).normal.y * center.y + (this.planes[i]).normal.z * center.z < -radius - (this.planes[i]).d)
/* 138 */         return false; 
/* 139 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean sphereInFrustumWithoutNearFar(float x, float y, float z, float radius) {
/* 150 */     for (int i = 2; i < 6; i++) {
/* 151 */       if ((this.planes[i]).normal.x * x + (this.planes[i]).normal.y * y + (this.planes[i]).normal.z * z < -radius - (this.planes[i]).d) return false; 
/* 152 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean boundsInFrustum(BoundingBox bounds) {
/* 160 */     for (int i = 0, len2 = this.planes.length; i < len2; ) {
/* 161 */       if (this.planes[i].testPoint(bounds.getCorner000(tmpV)) != Plane.PlaneSide.Back || 
/* 162 */         this.planes[i].testPoint(bounds.getCorner001(tmpV)) != Plane.PlaneSide.Back || 
/* 163 */         this.planes[i].testPoint(bounds.getCorner010(tmpV)) != Plane.PlaneSide.Back || 
/* 164 */         this.planes[i].testPoint(bounds.getCorner011(tmpV)) != Plane.PlaneSide.Back || 
/* 165 */         this.planes[i].testPoint(bounds.getCorner100(tmpV)) != Plane.PlaneSide.Back || 
/* 166 */         this.planes[i].testPoint(bounds.getCorner101(tmpV)) != Plane.PlaneSide.Back || 
/* 167 */         this.planes[i].testPoint(bounds.getCorner110(tmpV)) != Plane.PlaneSide.Back || 
/* 168 */         this.planes[i].testPoint(bounds.getCorner111(tmpV)) != Plane.PlaneSide.Back) { i++; continue; }
/* 169 */        return false;
/*     */     } 
/*     */     
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean boundsInFrustum(Vector3 center, Vector3 dimensions) {
/* 178 */     return boundsInFrustum(center.x, center.y, center.z, dimensions.x / 2.0F, dimensions.y / 2.0F, dimensions.z / 2.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean boundsInFrustum(float x, float y, float z, float halfWidth, float halfHeight, float halfDepth) {
/* 184 */     for (int i = 0, len2 = this.planes.length; i < len2; ) {
/* 185 */       if (this.planes[i].testPoint(x + halfWidth, y + halfHeight, z + halfDepth) != Plane.PlaneSide.Back || 
/* 186 */         this.planes[i].testPoint(x + halfWidth, y + halfHeight, z - halfDepth) != Plane.PlaneSide.Back || 
/* 187 */         this.planes[i].testPoint(x + halfWidth, y - halfHeight, z + halfDepth) != Plane.PlaneSide.Back || 
/* 188 */         this.planes[i].testPoint(x + halfWidth, y - halfHeight, z - halfDepth) != Plane.PlaneSide.Back || 
/* 189 */         this.planes[i].testPoint(x - halfWidth, y + halfHeight, z + halfDepth) != Plane.PlaneSide.Back || 
/* 190 */         this.planes[i].testPoint(x - halfWidth, y + halfHeight, z - halfDepth) != Plane.PlaneSide.Back || 
/* 191 */         this.planes[i].testPoint(x - halfWidth, y - halfHeight, z + halfDepth) != Plane.PlaneSide.Back || 
/* 192 */         this.planes[i].testPoint(x - halfWidth, y - halfHeight, z - halfDepth) != Plane.PlaneSide.Back) { i++; continue; }
/* 193 */        return false;
/*     */     } 
/*     */     
/* 196 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Frustum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */