/*      */ package com.badlogic.gdx.math;
/*      */ 
/*      */ import com.badlogic.gdx.math.collision.BoundingBox;
/*      */ import com.badlogic.gdx.math.collision.Ray;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.FloatArray;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Intersector
/*      */ {
/*   35 */   private static final Vector3 v0 = new Vector3();
/*   36 */   private static final Vector3 v1 = new Vector3();
/*   37 */   private static final Vector3 v2 = new Vector3();
/*   38 */   private static final FloatArray floatArray = new FloatArray();
/*   39 */   private static final FloatArray floatArray2 = new FloatArray();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPointInTriangle(Vector3 point, Vector3 t1, Vector3 t2, Vector3 t3) {
/*   50 */     v0.set(t1).sub(point);
/*   51 */     v1.set(t2).sub(point);
/*   52 */     v2.set(t3).sub(point);
/*      */     
/*   54 */     float ab = v0.dot(v1);
/*   55 */     float ac = v0.dot(v2);
/*   56 */     float bc = v1.dot(v2);
/*   57 */     float cc = v2.dot(v2);
/*      */     
/*   59 */     if (bc * ac - cc * ab < 0.0F) return false; 
/*   60 */     float bb = v1.dot(v1);
/*   61 */     if (ab * bc - ac * bb < 0.0F) return false; 
/*   62 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isPointInTriangle(Vector2 p, Vector2 a, Vector2 b, Vector2 c) {
/*   67 */     float px1 = p.x - a.x;
/*   68 */     float py1 = p.y - a.y;
/*   69 */     boolean side12 = ((b.x - a.x) * py1 - (b.y - a.y) * px1 > 0.0F);
/*   70 */     if ((((c.x - a.x) * py1 - (c.y - a.y) * px1 > 0.0F)) == side12) return false; 
/*   71 */     if ((((c.x - b.x) * (p.y - b.y) - (c.y - b.y) * (p.x - b.x) > 0.0F)) != side12) return false; 
/*   72 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isPointInTriangle(float px, float py, float ax, float ay, float bx, float by, float cx, float cy) {
/*   77 */     float px1 = px - ax;
/*   78 */     float py1 = py - ay;
/*   79 */     boolean side12 = ((bx - ax) * py1 - (by - ay) * px1 > 0.0F);
/*   80 */     if ((((cx - ax) * py1 - (cy - ay) * px1 > 0.0F)) == side12) return false; 
/*   81 */     if ((((cx - bx) * (py - by) - (cy - by) * (px - bx) > 0.0F)) != side12) return false; 
/*   82 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean intersectSegmentPlane(Vector3 start, Vector3 end, Plane plane, Vector3 intersection) {
/*   86 */     Vector3 dir = v0.set(end).sub(start);
/*   87 */     float denom = dir.dot(plane.getNormal());
/*   88 */     float t = -(start.dot(plane.getNormal()) + plane.getD()) / denom;
/*   89 */     if (t < 0.0F || t > 1.0F) return false;
/*      */     
/*   91 */     intersection.set(start).add(dir.scl(t));
/*   92 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int pointLineSide(Vector2 linePoint1, Vector2 linePoint2, Vector2 point) {
/*   99 */     return (int)Math.signum((linePoint2.x - linePoint1.x) * (point.y - linePoint1.y) - (linePoint2.y - linePoint1.y) * (point.x - linePoint1.x));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int pointLineSide(float linePoint1X, float linePoint1Y, float linePoint2X, float linePoint2Y, float pointX, float pointY) {
/*  105 */     return 
/*  106 */       (int)Math.signum((linePoint2X - linePoint1X) * (pointY - linePoint1Y) - (linePoint2Y - linePoint1Y) * (pointX - linePoint1X));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPointInPolygon(Array<Vector2> polygon, Vector2 point) {
/*  114 */     Vector2 lastVertice = (Vector2)polygon.peek();
/*  115 */     boolean oddNodes = false;
/*  116 */     for (int i = 0; i < polygon.size; i++) {
/*  117 */       Vector2 vertice = (Vector2)polygon.get(i);
/*  118 */       if (((vertice.y < point.y && lastVertice.y >= point.y) || (lastVertice.y < point.y && vertice.y >= point.y)) && 
/*  119 */         vertice.x + (point.y - vertice.y) / (lastVertice.y - vertice.y) * (lastVertice.x - vertice.x) < point.x) {
/*  120 */         oddNodes = !oddNodes;
/*      */       }
/*      */       
/*  123 */       lastVertice = vertice;
/*      */     } 
/*  125 */     return oddNodes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPointInPolygon(float[] polygon, int offset, int count, float x, float y) {
/*  132 */     boolean oddNodes = false;
/*  133 */     int j = offset + count - 2;
/*  134 */     for (int i = offset, n = j; i <= n; i += 2) {
/*  135 */       float yi = polygon[i + 1];
/*  136 */       float yj = polygon[j + 1];
/*  137 */       if ((yi < y && yj >= y) || (yj < y && yi >= y)) {
/*  138 */         float xi = polygon[i];
/*  139 */         if (xi + (y - yi) / (yj - yi) * (polygon[j] - xi) < x) oddNodes = !oddNodes; 
/*      */       } 
/*  141 */       j = i;
/*      */     } 
/*  143 */     return oddNodes;
/*      */   }
/*      */   
/*  146 */   private static final Vector2 ip = new Vector2();
/*  147 */   private static final Vector2 ep1 = new Vector2();
/*  148 */   private static final Vector2 ep2 = new Vector2();
/*  149 */   private static final Vector2 s = new Vector2();
/*  150 */   private static final Vector2 e = new Vector2();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectPolygons(Polygon p1, Polygon p2, Polygon overlap) {
/*  161 */     floatArray2.clear();
/*  162 */     floatArray.clear();
/*  163 */     floatArray2.addAll(p1.getTransformedVertices());
/*  164 */     if ((p1.getVertices()).length == 0 || (p2.getVertices()).length == 0) {
/*  165 */       return false;
/*      */     }
/*  167 */     for (int i = 0; i < (p2.getTransformedVertices()).length; i += 2) {
/*  168 */       ep1.set(p2.getTransformedVertices()[i], p2.getTransformedVertices()[i + 1]);
/*      */       
/*  170 */       if (i < (p2.getTransformedVertices()).length - 2) {
/*  171 */         ep2.set(p2.getTransformedVertices()[i + 2], p2.getTransformedVertices()[i + 3]);
/*      */       } else {
/*  173 */         ep2.set(p2.getTransformedVertices()[0], p2.getTransformedVertices()[1]);
/*      */       } 
/*  175 */       if (floatArray2.size == 0) {
/*  176 */         return false;
/*      */       }
/*  178 */       s.set(floatArray2.get(floatArray2.size - 2), floatArray2.get(floatArray2.size - 1));
/*  179 */       for (int j = 0; j < floatArray2.size; j += 2) {
/*  180 */         e.set(floatArray2.get(j), floatArray2.get(j + 1));
/*      */         
/*  182 */         if (pointLineSide(ep2, ep1, e) > 0) {
/*  183 */           if (pointLineSide(ep2, ep1, s) <= 0) {
/*  184 */             intersectLines(s, e, ep1, ep2, ip);
/*  185 */             if (floatArray.size < 2 || floatArray.get(floatArray.size - 2) != ip.x || floatArray
/*  186 */               .get(floatArray.size - 1) != ip.y) {
/*  187 */               floatArray.add(ip.x);
/*  188 */               floatArray.add(ip.y);
/*      */             } 
/*      */           } 
/*  191 */           floatArray.add(e.x);
/*  192 */           floatArray.add(e.y);
/*  193 */         } else if (pointLineSide(ep2, ep1, s) > 0) {
/*  194 */           intersectLines(s, e, ep1, ep2, ip);
/*  195 */           floatArray.add(ip.x);
/*  196 */           floatArray.add(ip.y);
/*      */         } 
/*  198 */         s.set(e.x, e.y);
/*      */       } 
/*  200 */       floatArray2.clear();
/*  201 */       floatArray2.addAll(floatArray);
/*  202 */       floatArray.clear();
/*      */     } 
/*  204 */     if (floatArray2.size != 0) {
/*  205 */       overlap.setVertices(floatArray2.toArray());
/*  206 */       return true;
/*      */     } 
/*  208 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static float distanceLinePoint(float startX, float startY, float endX, float endY, float pointX, float pointY) {
/*  214 */     float normalLength = (float)Math.sqrt(((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY)));
/*  215 */     return Math.abs((pointX - startX) * (endY - startY) - (pointY - startY) * (endX - startX)) / normalLength;
/*      */   }
/*      */ 
/*      */   
/*      */   public static float distanceSegmentPoint(float startX, float startY, float endX, float endY, float pointX, float pointY) {
/*  220 */     return nearestSegmentPoint(startX, startY, endX, endY, pointX, pointY, v2tmp).dst(pointX, pointY);
/*      */   }
/*      */ 
/*      */   
/*      */   public static float distanceSegmentPoint(Vector2 start, Vector2 end, Vector2 point) {
/*  225 */     return nearestSegmentPoint(start, end, point, v2tmp).dst(point);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Vector2 nearestSegmentPoint(Vector2 start, Vector2 end, Vector2 point, Vector2 nearest) {
/*  230 */     float length2 = start.dst2(end);
/*  231 */     if (length2 == 0.0F) return nearest.set(start); 
/*  232 */     float t = ((point.x - start.x) * (end.x - start.x) + (point.y - start.y) * (end.y - start.y)) / length2;
/*  233 */     if (t < 0.0F) return nearest.set(start); 
/*  234 */     if (t > 1.0F) return nearest.set(end); 
/*  235 */     return nearest.set(start.x + t * (end.x - start.x), start.y + t * (end.y - start.y));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Vector2 nearestSegmentPoint(float startX, float startY, float endX, float endY, float pointX, float pointY, Vector2 nearest) {
/*  241 */     float xDiff = endX - startX;
/*  242 */     float yDiff = endY - startY;
/*  243 */     float length2 = xDiff * xDiff + yDiff * yDiff;
/*  244 */     if (length2 == 0.0F) return nearest.set(startX, startY); 
/*  245 */     float t = ((pointX - startX) * (endX - startX) + (pointY - startY) * (endY - startY)) / length2;
/*  246 */     if (t < 0.0F) return nearest.set(startX, startY); 
/*  247 */     if (t > 1.0F) return nearest.set(endX, endY); 
/*  248 */     return nearest.set(startX + t * (endX - startX), startY + t * (endY - startY));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectSegmentCircle(Vector2 start, Vector2 end, Vector2 center, float squareRadius) {
/*  258 */     tmp.set(end.x - start.x, end.y - start.y, 0.0F);
/*  259 */     tmp1.set(center.x - start.x, center.y - start.y, 0.0F);
/*  260 */     float l = tmp.len();
/*  261 */     float u = tmp1.dot(tmp.nor());
/*  262 */     if (u <= 0.0F) {
/*  263 */       tmp2.set(start.x, start.y, 0.0F);
/*  264 */     } else if (u >= l) {
/*  265 */       tmp2.set(end.x, end.y, 0.0F);
/*      */     } else {
/*  267 */       tmp3.set(tmp.scl(u));
/*  268 */       tmp2.set(tmp3.x + start.x, tmp3.y + start.y, 0.0F);
/*      */     } 
/*      */     
/*  271 */     float x = center.x - tmp2.x;
/*  272 */     float y = center.y - tmp2.y;
/*      */     
/*  274 */     return (x * x + y * y <= squareRadius);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float intersectSegmentCircleDisplace(Vector2 start, Vector2 end, Vector2 point, float radius, Vector2 displacement) {
/*  288 */     float u = (point.x - start.x) * (end.x - start.x) + (point.y - start.y) * (end.y - start.y);
/*  289 */     float d = start.dst(end);
/*  290 */     u /= d * d;
/*  291 */     if (u < 0.0F || u > 1.0F) return Float.POSITIVE_INFINITY; 
/*  292 */     tmp.set(end.x, end.y, 0.0F).sub(start.x, start.y, 0.0F);
/*  293 */     tmp2.set(start.x, start.y, 0.0F).add(tmp.scl(u));
/*  294 */     d = tmp2.dst(point.x, point.y, 0.0F);
/*  295 */     if (d < radius) {
/*  296 */       displacement.set(point).sub(tmp2.x, tmp2.y).nor();
/*  297 */       return d;
/*      */     } 
/*  299 */     return Float.POSITIVE_INFINITY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float intersectRayRay(Vector2 start1, Vector2 direction1, Vector2 start2, Vector2 direction2) {
/*  312 */     float difx = start2.x - start1.x;
/*  313 */     float dify = start2.y - start1.y;
/*  314 */     float d1xd2 = direction1.x * direction2.y - direction1.y * direction2.x;
/*  315 */     if (d1xd2 == 0.0F) {
/*  316 */       return Float.POSITIVE_INFINITY;
/*      */     }
/*  318 */     float d2sx = direction2.x / d1xd2;
/*  319 */     float d2sy = direction2.y / d1xd2;
/*  320 */     return difx * d2sy - dify * d2sx;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayPlane(Ray ray, Plane plane, Vector3 intersection) {
/*  331 */     float denom = ray.direction.dot(plane.getNormal());
/*  332 */     if (denom != 0.0F) {
/*  333 */       float t = -(ray.origin.dot(plane.getNormal()) + plane.getD()) / denom;
/*  334 */       if (t < 0.0F) return false;
/*      */       
/*  336 */       if (intersection != null) intersection.set(ray.origin).add(v0.set(ray.direction).scl(t)); 
/*  337 */       return true;
/*  338 */     }  if (plane.testPoint(ray.origin) == Plane.PlaneSide.OnPlane) {
/*  339 */       if (intersection != null) intersection.set(ray.origin); 
/*  340 */       return true;
/*      */     } 
/*  342 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float intersectLinePlane(float x, float y, float z, float x2, float y2, float z2, Plane plane, Vector3 intersection) {
/*  357 */     Vector3 direction = tmp.set(x2, y2, z2).sub(x, y, z);
/*  358 */     Vector3 origin = tmp2.set(x, y, z);
/*  359 */     float denom = direction.dot(plane.getNormal());
/*  360 */     if (denom != 0.0F) {
/*  361 */       float t = -(origin.dot(plane.getNormal()) + plane.getD()) / denom;
/*  362 */       if (intersection != null) intersection.set(origin).add(direction.scl(t)); 
/*  363 */       return t;
/*  364 */     }  if (plane.testPoint(origin) == Plane.PlaneSide.OnPlane) {
/*  365 */       if (intersection != null) intersection.set(origin); 
/*  366 */       return 0.0F;
/*      */     } 
/*      */     
/*  369 */     return -1.0F;
/*      */   }
/*      */   
/*  372 */   private static final Plane p = new Plane(new Vector3(), 0.0F);
/*  373 */   private static final Vector3 i = new Vector3();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayTriangle(Ray ray, Vector3 t1, Vector3 t2, Vector3 t3, Vector3 intersection) {
/*  384 */     Vector3 edge1 = v0.set(t2).sub(t1);
/*  385 */     Vector3 edge2 = v1.set(t3).sub(t1);
/*      */     
/*  387 */     Vector3 pvec = v2.set(ray.direction).crs(edge2);
/*  388 */     float det = edge1.dot(pvec);
/*  389 */     if (MathUtils.isZero(det)) {
/*  390 */       p.set(t1, t2, t3);
/*  391 */       if (p.testPoint(ray.origin) == Plane.PlaneSide.OnPlane && isPointInTriangle(ray.origin, t1, t2, t3)) {
/*  392 */         if (intersection != null) intersection.set(ray.origin); 
/*  393 */         return true;
/*      */       } 
/*  395 */       return false;
/*      */     } 
/*      */     
/*  398 */     det = 1.0F / det;
/*      */     
/*  400 */     Vector3 tvec = i.set(ray.origin).sub(t1);
/*  401 */     float u = tvec.dot(pvec) * det;
/*  402 */     if (u < 0.0F || u > 1.0F) return false;
/*      */     
/*  404 */     Vector3 qvec = tvec.crs(edge1);
/*  405 */     float v = ray.direction.dot(qvec) * det;
/*  406 */     if (v < 0.0F || u + v > 1.0F) return false;
/*      */     
/*  408 */     float t = edge2.dot(qvec) * det;
/*  409 */     if (t < 0.0F) return false;
/*      */     
/*  411 */     if (intersection != null) {
/*  412 */       if (t <= 1.0E-6F) {
/*  413 */         intersection.set(ray.origin);
/*      */       } else {
/*  415 */         ray.getEndPoint(intersection, t);
/*      */       } 
/*      */     }
/*      */     
/*  419 */     return true;
/*      */   }
/*      */   
/*  422 */   private static final Vector3 dir = new Vector3();
/*  423 */   private static final Vector3 start = new Vector3();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRaySphere(Ray ray, Vector3 center, float radius, Vector3 intersection) {
/*  433 */     float len = ray.direction.dot(center.x - ray.origin.x, center.y - ray.origin.y, center.z - ray.origin.z);
/*  434 */     if (len < 0.0F)
/*  435 */       return false; 
/*  436 */     float dst2 = center.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
/*      */     
/*  438 */     float r2 = radius * radius;
/*  439 */     if (dst2 > r2) return false; 
/*  440 */     if (intersection != null) intersection.set(ray.direction).scl(len - (float)Math.sqrt((r2 - dst2))).add(ray.origin); 
/*  441 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayBounds(Ray ray, BoundingBox box, Vector3 intersection) {
/*  462 */     if (box.contains(ray.origin)) {
/*  463 */       if (intersection != null) intersection.set(ray.origin); 
/*  464 */       return true;
/*      */     } 
/*  466 */     float lowest = 0.0F;
/*  467 */     boolean hit = false;
/*      */ 
/*      */     
/*  470 */     if (ray.origin.x <= box.min.x && ray.direction.x > 0.0F) {
/*  471 */       float t = (box.min.x - ray.origin.x) / ray.direction.x;
/*  472 */       if (t >= 0.0F) {
/*  473 */         v2.set(ray.direction).scl(t).add(ray.origin);
/*  474 */         if (v2.y >= box.min.y && v2.y <= box.max.y && v2.z >= box.min.z && v2.z <= box.max.z && (!hit || t < lowest)) {
/*  475 */           hit = true;
/*  476 */           lowest = t;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  481 */     if (ray.origin.x >= box.max.x && ray.direction.x < 0.0F) {
/*  482 */       float t = (box.max.x - ray.origin.x) / ray.direction.x;
/*  483 */       if (t >= 0.0F) {
/*  484 */         v2.set(ray.direction).scl(t).add(ray.origin);
/*  485 */         if (v2.y >= box.min.y && v2.y <= box.max.y && v2.z >= box.min.z && v2.z <= box.max.z && (!hit || t < lowest)) {
/*  486 */           hit = true;
/*  487 */           lowest = t;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  492 */     if (ray.origin.y <= box.min.y && ray.direction.y > 0.0F) {
/*  493 */       float t = (box.min.y - ray.origin.y) / ray.direction.y;
/*  494 */       if (t >= 0.0F) {
/*  495 */         v2.set(ray.direction).scl(t).add(ray.origin);
/*  496 */         if (v2.x >= box.min.x && v2.x <= box.max.x && v2.z >= box.min.z && v2.z <= box.max.z && (!hit || t < lowest)) {
/*  497 */           hit = true;
/*  498 */           lowest = t;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  503 */     if (ray.origin.y >= box.max.y && ray.direction.y < 0.0F) {
/*  504 */       float t = (box.max.y - ray.origin.y) / ray.direction.y;
/*  505 */       if (t >= 0.0F) {
/*  506 */         v2.set(ray.direction).scl(t).add(ray.origin);
/*  507 */         if (v2.x >= box.min.x && v2.x <= box.max.x && v2.z >= box.min.z && v2.z <= box.max.z && (!hit || t < lowest)) {
/*  508 */           hit = true;
/*  509 */           lowest = t;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  514 */     if (ray.origin.z <= box.min.z && ray.direction.z > 0.0F) {
/*  515 */       float t = (box.min.z - ray.origin.z) / ray.direction.z;
/*  516 */       if (t >= 0.0F) {
/*  517 */         v2.set(ray.direction).scl(t).add(ray.origin);
/*  518 */         if (v2.x >= box.min.x && v2.x <= box.max.x && v2.y >= box.min.y && v2.y <= box.max.y && (!hit || t < lowest)) {
/*  519 */           hit = true;
/*  520 */           lowest = t;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  525 */     if (ray.origin.z >= box.max.z && ray.direction.z < 0.0F) {
/*  526 */       float t = (box.max.z - ray.origin.z) / ray.direction.z;
/*  527 */       if (t >= 0.0F) {
/*  528 */         v2.set(ray.direction).scl(t).add(ray.origin);
/*  529 */         if (v2.x >= box.min.x && v2.x <= box.max.x && v2.y >= box.min.y && v2.y <= box.max.y && (!hit || t < lowest)) {
/*  530 */           hit = true;
/*  531 */           lowest = t;
/*      */         } 
/*      */       } 
/*      */     } 
/*  535 */     if (hit && intersection != null) {
/*  536 */       intersection.set(ray.direction).scl(lowest).add(ray.origin);
/*  537 */       if (intersection.x < box.min.x) {
/*  538 */         intersection.x = box.min.x;
/*  539 */       } else if (intersection.x > box.max.x) {
/*  540 */         intersection.x = box.max.x;
/*      */       } 
/*  542 */       if (intersection.y < box.min.y) {
/*  543 */         intersection.y = box.min.y;
/*  544 */       } else if (intersection.y > box.max.y) {
/*  545 */         intersection.y = box.max.y;
/*      */       } 
/*  547 */       if (intersection.z < box.min.z) {
/*  548 */         intersection.z = box.min.z;
/*  549 */       } else if (intersection.z > box.max.z) {
/*  550 */         intersection.z = box.max.z;
/*      */       } 
/*      */     } 
/*  553 */     return hit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayBoundsFast(Ray ray, BoundingBox box) {
/*  562 */     return intersectRayBoundsFast(ray, box.getCenter(tmp1), box.getDimensions(tmp2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayBoundsFast(Ray ray, Vector3 center, Vector3 dimensions) {
/*  572 */     float divX = 1.0F / ray.direction.x;
/*  573 */     float divY = 1.0F / ray.direction.y;
/*  574 */     float divZ = 1.0F / ray.direction.z;
/*      */     
/*  576 */     float minx = (center.x - dimensions.x * 0.5F - ray.origin.x) * divX;
/*  577 */     float maxx = (center.x + dimensions.x * 0.5F - ray.origin.x) * divX;
/*  578 */     if (minx > maxx) {
/*  579 */       float t = minx;
/*  580 */       minx = maxx;
/*  581 */       maxx = t;
/*      */     } 
/*      */     
/*  584 */     float miny = (center.y - dimensions.y * 0.5F - ray.origin.y) * divY;
/*  585 */     float maxy = (center.y + dimensions.y * 0.5F - ray.origin.y) * divY;
/*  586 */     if (miny > maxy) {
/*  587 */       float t = miny;
/*  588 */       miny = maxy;
/*  589 */       maxy = t;
/*      */     } 
/*      */     
/*  592 */     float minz = (center.z - dimensions.z * 0.5F - ray.origin.z) * divZ;
/*  593 */     float maxz = (center.z + dimensions.z * 0.5F - ray.origin.z) * divZ;
/*  594 */     if (minz > maxz) {
/*  595 */       float t = minz;
/*  596 */       minz = maxz;
/*  597 */       maxz = t;
/*      */     } 
/*      */     
/*  600 */     float min = Math.max(Math.max(minx, miny), minz);
/*  601 */     float max = Math.min(Math.min(maxx, maxy), maxz);
/*      */     
/*  603 */     return (max >= 0.0F && max >= min);
/*      */   }
/*      */   
/*  606 */   static Vector3 best = new Vector3();
/*  607 */   static Vector3 tmp = new Vector3();
/*  608 */   static Vector3 tmp1 = new Vector3();
/*  609 */   static Vector3 tmp2 = new Vector3();
/*  610 */   static Vector3 tmp3 = new Vector3();
/*  611 */   static Vector2 v2tmp = new Vector2();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayTriangles(Ray ray, float[] triangles, Vector3 intersection) {
/*  620 */     float min_dist = Float.MAX_VALUE;
/*  621 */     boolean hit = false;
/*      */     
/*  623 */     if (triangles.length / 3 % 3 != 0) throw new RuntimeException("triangle list size is not a multiple of 3");
/*      */     
/*  625 */     for (int i = 0; i < triangles.length - 6; i += 9) {
/*  626 */       boolean result = intersectRayTriangle(ray, tmp1.set(triangles[i], triangles[i + 1], triangles[i + 2]), tmp2
/*  627 */           .set(triangles[i + 3], triangles[i + 4], triangles[i + 5]), tmp3
/*  628 */           .set(triangles[i + 6], triangles[i + 7], triangles[i + 8]), tmp);
/*      */       
/*  630 */       if (result == true) {
/*  631 */         float dist = ray.origin.dst2(tmp);
/*  632 */         if (dist < min_dist) {
/*  633 */           min_dist = dist;
/*  634 */           best.set(tmp);
/*  635 */           hit = true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  640 */     if (!hit) {
/*  641 */       return false;
/*      */     }
/*  643 */     if (intersection != null) intersection.set(best); 
/*  644 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayTriangles(Ray ray, float[] vertices, short[] indices, int vertexSize, Vector3 intersection) {
/*  658 */     float min_dist = Float.MAX_VALUE;
/*  659 */     boolean hit = false;
/*      */     
/*  661 */     if (indices.length % 3 != 0) throw new RuntimeException("triangle list size is not a multiple of 3");
/*      */     
/*  663 */     for (int i = 0; i < indices.length; i += 3) {
/*  664 */       int i1 = indices[i] * vertexSize;
/*  665 */       int i2 = indices[i + 1] * vertexSize;
/*  666 */       int i3 = indices[i + 2] * vertexSize;
/*      */       
/*  668 */       boolean result = intersectRayTriangle(ray, tmp1.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]), tmp2
/*  669 */           .set(vertices[i2], vertices[i2 + 1], vertices[i2 + 2]), tmp3
/*  670 */           .set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]), tmp);
/*      */       
/*  672 */       if (result == true) {
/*  673 */         float dist = ray.origin.dst2(tmp);
/*  674 */         if (dist < min_dist) {
/*  675 */           min_dist = dist;
/*  676 */           best.set(tmp);
/*  677 */           hit = true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  682 */     if (!hit) {
/*  683 */       return false;
/*      */     }
/*  685 */     if (intersection != null) intersection.set(best); 
/*  686 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRayTriangles(Ray ray, List<Vector3> triangles, Vector3 intersection) {
/*  697 */     float min_dist = Float.MAX_VALUE;
/*  698 */     boolean hit = false;
/*      */     
/*  700 */     if (triangles.size() % 3 != 0) throw new RuntimeException("triangle list size is not a multiple of 3");
/*      */     
/*  702 */     for (int i = 0; i < triangles.size() - 2; i += 3) {
/*  703 */       boolean result = intersectRayTriangle(ray, triangles.get(i), triangles.get(i + 1), triangles.get(i + 2), tmp);
/*      */       
/*  705 */       if (result == true) {
/*  706 */         float dist = ray.origin.dst2(tmp);
/*  707 */         if (dist < min_dist) {
/*  708 */           min_dist = dist;
/*  709 */           best.set(tmp);
/*  710 */           hit = true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  715 */     if (!hit) {
/*  716 */       return false;
/*      */     }
/*  718 */     if (intersection != null) intersection.set(best); 
/*  719 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectLines(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, Vector2 intersection) {
/*  732 */     float x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y, x3 = p3.x, y3 = p3.y, x4 = p4.x, y4 = p4.y;
/*      */     
/*  734 */     float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
/*  735 */     if (d == 0.0F) return false;
/*      */     
/*  737 */     if (intersection != null) {
/*  738 */       float ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / d;
/*  739 */       intersection.set(x1 + (x2 - x1) * ua, y1 + (y2 - y1) * ua);
/*      */     } 
/*  741 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Vector2 intersection) {
/*  749 */     float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
/*  750 */     if (d == 0.0F) return false;
/*      */     
/*  752 */     if (intersection != null) {
/*  753 */       float ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / d;
/*  754 */       intersection.set(x1 + (x2 - x1) * ua, y1 + (y2 - y1) * ua);
/*      */     } 
/*  756 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectLinePolygon(Vector2 p1, Vector2 p2, Polygon polygon) {
/*  765 */     float[] vertices = polygon.getTransformedVertices();
/*  766 */     float x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;
/*  767 */     int n = vertices.length;
/*  768 */     float x3 = vertices[n - 2], y3 = vertices[n - 1];
/*  769 */     for (int i = 0; i < n; i += 2) {
/*  770 */       float x4 = vertices[i], y4 = vertices[i + 1];
/*  771 */       float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
/*  772 */       if (d != 0.0F) {
/*  773 */         float yd = y1 - y3;
/*  774 */         float xd = x1 - x3;
/*  775 */         float ua = ((x4 - x3) * yd - (y4 - y3) * xd) / d;
/*  776 */         if (ua >= 0.0F && ua <= 1.0F) {
/*  777 */           return true;
/*      */         }
/*      */       } 
/*  780 */       x3 = x4;
/*  781 */       y3 = y4;
/*      */     } 
/*  783 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectRectangles(Rectangle rectangle1, Rectangle rectangle2, Rectangle intersection) {
/*  791 */     if (rectangle1.overlaps(rectangle2)) {
/*  792 */       intersection.x = Math.max(rectangle1.x, rectangle2.x);
/*  793 */       intersection.width = Math.min(rectangle1.x + rectangle1.width, rectangle2.x + rectangle2.width) - intersection.x;
/*  794 */       intersection.y = Math.max(rectangle1.y, rectangle2.y);
/*  795 */       intersection.height = Math.min(rectangle1.y + rectangle1.height, rectangle2.y + rectangle2.height) - intersection.y;
/*  796 */       return true;
/*      */     } 
/*  798 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectSegmentPolygon(Vector2 p1, Vector2 p2, Polygon polygon) {
/*  806 */     float[] vertices = polygon.getTransformedVertices();
/*  807 */     float x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;
/*  808 */     int n = vertices.length;
/*  809 */     float x3 = vertices[n - 2], y3 = vertices[n - 1];
/*  810 */     for (int i = 0; i < n; i += 2) {
/*  811 */       float x4 = vertices[i], y4 = vertices[i + 1];
/*  812 */       float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
/*  813 */       if (d != 0.0F) {
/*  814 */         float yd = y1 - y3;
/*  815 */         float xd = x1 - x3;
/*  816 */         float ua = ((x4 - x3) * yd - (y4 - y3) * xd) / d;
/*  817 */         if (ua >= 0.0F && ua <= 1.0F) {
/*  818 */           float ub = ((x2 - x1) * yd - (y2 - y1) * xd) / d;
/*  819 */           if (ub >= 0.0F && ub <= 1.0F) {
/*  820 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*  824 */       x3 = x4;
/*  825 */       y3 = y4;
/*      */     } 
/*  827 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectSegments(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, Vector2 intersection) {
/*  839 */     float x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y, x3 = p3.x, y3 = p3.y, x4 = p4.x, y4 = p4.y;
/*      */     
/*  841 */     float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
/*  842 */     if (d == 0.0F) return false;
/*      */     
/*  844 */     float yd = y1 - y3;
/*  845 */     float xd = x1 - x3;
/*  846 */     float ua = ((x4 - x3) * yd - (y4 - y3) * xd) / d;
/*  847 */     if (ua < 0.0F || ua > 1.0F) return false;
/*      */     
/*  849 */     float ub = ((x2 - x1) * yd - (y2 - y1) * xd) / d;
/*  850 */     if (ub < 0.0F || ub > 1.0F) return false;
/*      */     
/*  852 */     if (intersection != null) intersection.set(x1 + (x2 - x1) * ua, y1 + (y2 - y1) * ua); 
/*  853 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean intersectSegments(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Vector2 intersection) {
/*  859 */     float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
/*  860 */     if (d == 0.0F) return false;
/*      */     
/*  862 */     float yd = y1 - y3;
/*  863 */     float xd = x1 - x3;
/*  864 */     float ua = ((x4 - x3) * yd - (y4 - y3) * xd) / d;
/*  865 */     if (ua < 0.0F || ua > 1.0F) return false;
/*      */     
/*  867 */     float ub = ((x2 - x1) * yd - (y2 - y1) * xd) / d;
/*  868 */     if (ub < 0.0F || ub > 1.0F) return false;
/*      */     
/*  870 */     if (intersection != null) intersection.set(x1 + (x2 - x1) * ua, y1 + (y2 - y1) * ua); 
/*  871 */     return true;
/*      */   }
/*      */   
/*      */   static float det(float a, float b, float c, float d) {
/*  875 */     return a * d - b * c;
/*      */   }
/*      */   
/*      */   static double detd(double a, double b, double c, double d) {
/*  879 */     return a * d - b * c;
/*      */   }
/*      */   
/*      */   public static boolean overlaps(Circle c1, Circle c2) {
/*  883 */     return c1.overlaps(c2);
/*      */   }
/*      */   
/*      */   public static boolean overlaps(Rectangle r1, Rectangle r2) {
/*  887 */     return r1.overlaps(r2);
/*      */   }
/*      */   
/*      */   public static boolean overlaps(Circle c, Rectangle r) {
/*  891 */     float closestX = c.x;
/*  892 */     float closestY = c.y;
/*      */     
/*  894 */     if (c.x < r.x) {
/*  895 */       closestX = r.x;
/*  896 */     } else if (c.x > r.x + r.width) {
/*  897 */       closestX = r.x + r.width;
/*      */     } 
/*      */     
/*  900 */     if (c.y < r.y) {
/*  901 */       closestY = r.y;
/*  902 */     } else if (c.y > r.y + r.height) {
/*  903 */       closestY = r.y + r.height;
/*      */     } 
/*      */     
/*  906 */     closestX -= c.x;
/*  907 */     closestX *= closestX;
/*  908 */     closestY -= c.y;
/*  909 */     closestY *= closestY;
/*      */     
/*  911 */     return (closestX + closestY < c.radius * c.radius);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean overlapConvexPolygons(Polygon p1, Polygon p2) {
/*  920 */     return overlapConvexPolygons(p1, p2, (MinimumTranslationVector)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean overlapConvexPolygons(Polygon p1, Polygon p2, MinimumTranslationVector mtv) {
/*  931 */     return overlapConvexPolygons(p1.getTransformedVertices(), p2.getTransformedVertices(), mtv);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean overlapConvexPolygons(float[] verts1, float[] verts2, MinimumTranslationVector mtv) {
/*  936 */     return overlapConvexPolygons(verts1, 0, verts1.length, verts2, 0, verts2.length, mtv);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean overlapConvexPolygons(float[] verts1, int offset1, int count1, float[] verts2, int offset2, int count2, MinimumTranslationVector mtv) {
/*  949 */     float overlap = Float.MAX_VALUE;
/*  950 */     float smallestAxisX = 0.0F;
/*  951 */     float smallestAxisY = 0.0F;
/*      */ 
/*      */     
/*  954 */     int end1 = offset1 + count1;
/*  955 */     int end2 = offset2 + count2;
/*      */     
/*      */     int i;
/*  958 */     for (i = offset1; i < end1; i += 2) {
/*  959 */       float x1 = verts1[i];
/*  960 */       float y1 = verts1[i + 1];
/*  961 */       float x2 = verts1[(i + 2) % count1];
/*  962 */       float y2 = verts1[(i + 3) % count1];
/*      */       
/*  964 */       float axisX = y1 - y2;
/*  965 */       float axisY = -(x1 - x2);
/*      */       
/*  967 */       float length = (float)Math.sqrt((axisX * axisX + axisY * axisY));
/*  968 */       axisX /= length;
/*  969 */       axisY /= length;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  974 */       float min1 = axisX * verts1[0] + axisY * verts1[1];
/*  975 */       float max1 = min1;
/*  976 */       for (int j = offset1; j < end1; j += 2) {
/*  977 */         float p = axisX * verts1[j] + axisY * verts1[j + 1];
/*  978 */         if (p < min1) {
/*  979 */           min1 = p;
/*  980 */         } else if (p > max1) {
/*  981 */           max1 = p;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  986 */       int numInNormalDir = 0;
/*  987 */       float min2 = axisX * verts2[0] + axisY * verts2[1];
/*  988 */       float max2 = min2;
/*  989 */       for (int k = offset2; k < end2; k += 2) {
/*      */         
/*  991 */         numInNormalDir -= pointLineSide(x1, y1, x2, y2, verts2[k], verts2[k + 1]);
/*  992 */         float p = axisX * verts2[k] + axisY * verts2[k + 1];
/*  993 */         if (p < min2) {
/*  994 */           min2 = p;
/*  995 */         } else if (p > max2) {
/*  996 */           max2 = p;
/*      */         } 
/*      */       } 
/*      */       
/* 1000 */       if ((min1 > min2 || max1 < min2) && (min2 > min1 || max2 < min1)) {
/* 1001 */         return false;
/*      */       }
/* 1003 */       float o = Math.min(max1, max2) - Math.max(min1, min2);
/* 1004 */       if ((min1 < min2 && max1 > max2) || (min2 < min1 && max2 > max1)) {
/* 1005 */         float mins = Math.abs(min1 - min2);
/* 1006 */         float maxs = Math.abs(max1 - max2);
/* 1007 */         if (mins < maxs) {
/* 1008 */           o += mins;
/*      */         } else {
/* 1010 */           o += maxs;
/*      */         } 
/*      */       } 
/* 1013 */       if (o < overlap) {
/* 1014 */         overlap = o;
/*      */         
/* 1016 */         smallestAxisX = (numInNormalDir >= 0) ? axisX : -axisX;
/* 1017 */         smallestAxisY = (numInNormalDir >= 0) ? axisY : -axisY;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1024 */     for (i = offset2; i < end2; i += 2) {
/* 1025 */       float x1 = verts2[i];
/* 1026 */       float y1 = verts2[i + 1];
/* 1027 */       float x2 = verts2[(i + 2) % count2];
/* 1028 */       float y2 = verts2[(i + 3) % count2];
/*      */       
/* 1030 */       float axisX = y1 - y2;
/* 1031 */       float axisY = -(x1 - x2);
/*      */       
/* 1033 */       float length = (float)Math.sqrt((axisX * axisX + axisY * axisY));
/* 1034 */       axisX /= length;
/* 1035 */       axisY /= length;
/*      */ 
/*      */       
/* 1038 */       int numInNormalDir = 0;
/*      */ 
/*      */       
/* 1041 */       float min1 = axisX * verts1[0] + axisY * verts1[1];
/* 1042 */       float max1 = min1;
/* 1043 */       for (int j = offset1; j < end1; j += 2) {
/* 1044 */         float p = axisX * verts1[j] + axisY * verts1[j + 1];
/*      */         
/* 1046 */         numInNormalDir -= pointLineSide(x1, y1, x2, y2, verts1[j], verts1[j + 1]);
/* 1047 */         if (p < min1) {
/* 1048 */           min1 = p;
/* 1049 */         } else if (p > max1) {
/* 1050 */           max1 = p;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1055 */       float min2 = axisX * verts2[0] + axisY * verts2[1];
/* 1056 */       float max2 = min2;
/* 1057 */       for (int k = offset2; k < end2; k += 2) {
/* 1058 */         float p = axisX * verts2[k] + axisY * verts2[k + 1];
/* 1059 */         if (p < min2) {
/* 1060 */           min2 = p;
/* 1061 */         } else if (p > max2) {
/* 1062 */           max2 = p;
/*      */         } 
/*      */       } 
/*      */       
/* 1066 */       if ((min1 > min2 || max1 < min2) && (min2 > min1 || max2 < min1)) {
/* 1067 */         return false;
/*      */       }
/* 1069 */       float o = Math.min(max1, max2) - Math.max(min1, min2);
/*      */       
/* 1071 */       if ((min1 < min2 && max1 > max2) || (min2 < min1 && max2 > max1)) {
/* 1072 */         float mins = Math.abs(min1 - min2);
/* 1073 */         float maxs = Math.abs(max1 - max2);
/* 1074 */         if (mins < maxs) {
/* 1075 */           o += mins;
/*      */         } else {
/* 1077 */           o += maxs;
/*      */         } 
/*      */       } 
/*      */       
/* 1081 */       if (o < overlap) {
/* 1082 */         overlap = o;
/*      */         
/* 1084 */         smallestAxisX = (numInNormalDir < 0) ? axisX : -axisX;
/* 1085 */         smallestAxisY = (numInNormalDir < 0) ? axisY : -axisY;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1090 */     if (mtv != null) {
/* 1091 */       mtv.normal.set(smallestAxisX, smallestAxisY);
/* 1092 */       mtv.depth = overlap;
/*      */     } 
/* 1094 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void splitTriangle(float[] triangle, Plane plane, SplitTriangle split) {
/* 1117 */     int stride = triangle.length / 3;
/* 1118 */     boolean r1 = (plane.testPoint(triangle[0], triangle[1], triangle[2]) == Plane.PlaneSide.Back);
/* 1119 */     boolean r2 = (plane.testPoint(triangle[0 + stride], triangle[1 + stride], triangle[2 + stride]) == Plane.PlaneSide.Back);
/* 1120 */     boolean r3 = (plane.testPoint(triangle[0 + stride * 2], triangle[1 + stride * 2], triangle[2 + stride * 2]) == Plane.PlaneSide.Back);
/*      */ 
/*      */     
/* 1123 */     split.reset();
/*      */ 
/*      */     
/* 1126 */     if (r1 == r2 && r2 == r3) {
/* 1127 */       split.total = 1;
/* 1128 */       if (r1) {
/* 1129 */         split.numBack = 1;
/* 1130 */         System.arraycopy(triangle, 0, split.back, 0, triangle.length);
/*      */       } else {
/* 1132 */         split.numFront = 1;
/* 1133 */         System.arraycopy(triangle, 0, split.front, 0, triangle.length);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1139 */     split.total = 3;
/* 1140 */     split.numFront = (r1 ? 0 : 1) + (r2 ? 0 : 1) + (r3 ? 0 : 1);
/* 1141 */     split.numBack = split.total - split.numFront;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1146 */     split.setSide(!r1);
/*      */ 
/*      */     
/* 1149 */     int first = 0;
/* 1150 */     int second = stride;
/* 1151 */     if (r1 != r2) {
/*      */       
/* 1153 */       splitEdge(triangle, first, second, stride, plane, split.edgeSplit, 0);
/*      */ 
/*      */       
/* 1156 */       split.add(triangle, first, stride);
/* 1157 */       split.add(split.edgeSplit, 0, stride);
/*      */ 
/*      */       
/* 1160 */       split.setSide(!split.getSide());
/* 1161 */       split.add(split.edgeSplit, 0, stride);
/*      */     } else {
/*      */       
/* 1164 */       split.add(triangle, first, stride);
/*      */     } 
/*      */ 
/*      */     
/* 1168 */     first = stride;
/* 1169 */     second = stride + stride;
/* 1170 */     if (r2 != r3) {
/*      */       
/* 1172 */       splitEdge(triangle, first, second, stride, plane, split.edgeSplit, 0);
/*      */ 
/*      */       
/* 1175 */       split.add(triangle, first, stride);
/* 1176 */       split.add(split.edgeSplit, 0, stride);
/*      */ 
/*      */       
/* 1179 */       split.setSide(!split.getSide());
/* 1180 */       split.add(split.edgeSplit, 0, stride);
/*      */     } else {
/*      */       
/* 1183 */       split.add(triangle, first, stride);
/*      */     } 
/*      */ 
/*      */     
/* 1187 */     first = stride + stride;
/* 1188 */     second = 0;
/* 1189 */     if (r3 != r1) {
/*      */       
/* 1191 */       splitEdge(triangle, first, second, stride, plane, split.edgeSplit, 0);
/*      */ 
/*      */       
/* 1194 */       split.add(triangle, first, stride);
/* 1195 */       split.add(split.edgeSplit, 0, stride);
/*      */ 
/*      */       
/* 1198 */       split.setSide(!split.getSide());
/* 1199 */       split.add(split.edgeSplit, 0, stride);
/*      */     } else {
/*      */       
/* 1202 */       split.add(triangle, first, stride);
/*      */     } 
/*      */ 
/*      */     
/* 1206 */     if (split.numFront == 2) {
/* 1207 */       System.arraycopy(split.front, stride * 2, split.front, stride * 3, stride * 2);
/* 1208 */       System.arraycopy(split.front, 0, split.front, stride * 5, stride);
/*      */     } else {
/* 1210 */       System.arraycopy(split.back, stride * 2, split.back, stride * 3, stride * 2);
/* 1211 */       System.arraycopy(split.back, 0, split.back, stride * 5, stride);
/*      */     } 
/*      */   }
/*      */   
/* 1215 */   static Vector3 intersection = new Vector3();
/*      */   
/*      */   private static void splitEdge(float[] vertices, int s, int e, int stride, Plane plane, float[] split, int offset) {
/* 1218 */     float t = intersectLinePlane(vertices[s], vertices[s + 1], vertices[s + 2], vertices[e], vertices[e + 1], vertices[e + 2], plane, intersection);
/*      */     
/* 1220 */     split[offset + 0] = intersection.x;
/* 1221 */     split[offset + 1] = intersection.y;
/* 1222 */     split[offset + 2] = intersection.z;
/* 1223 */     for (int i = 3; i < stride; i++) {
/* 1224 */       float a = vertices[s + i];
/* 1225 */       float b = vertices[e + i];
/* 1226 */       split[offset + i] = a + t * (b - a);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static class SplitTriangle {
/*      */     public float[] front;
/*      */     public float[] back;
/*      */     float[] edgeSplit;
/*      */     public int numFront;
/*      */     public int numBack;
/*      */     public int total;
/*      */     boolean frontCurrent = false;
/* 1238 */     int frontOffset = 0;
/* 1239 */     int backOffset = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     public SplitTriangle(int numAttributes) {
/* 1244 */       this.front = new float[numAttributes * 3 * 2];
/* 1245 */       this.back = new float[numAttributes * 3 * 2];
/* 1246 */       this.edgeSplit = new float[numAttributes];
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1251 */       return "SplitTriangle [front=" + Arrays.toString(this.front) + ", back=" + Arrays.toString(this.back) + ", numFront=" + this.numFront + ", numBack=" + this.numBack + ", total=" + this.total + "]";
/*      */     }
/*      */ 
/*      */     
/*      */     void setSide(boolean front) {
/* 1256 */       this.frontCurrent = front;
/*      */     }
/*      */     
/*      */     boolean getSide() {
/* 1260 */       return this.frontCurrent;
/*      */     }
/*      */     
/*      */     void add(float[] vertex, int offset, int stride) {
/* 1264 */       if (this.frontCurrent) {
/* 1265 */         System.arraycopy(vertex, offset, this.front, this.frontOffset, stride);
/* 1266 */         this.frontOffset += stride;
/*      */       } else {
/* 1268 */         System.arraycopy(vertex, offset, this.back, this.backOffset, stride);
/* 1269 */         this.backOffset += stride;
/*      */       } 
/*      */     }
/*      */     
/*      */     void reset() {
/* 1274 */       this.frontCurrent = false;
/* 1275 */       this.frontOffset = 0;
/* 1276 */       this.backOffset = 0;
/* 1277 */       this.numFront = 0;
/* 1278 */       this.numBack = 0;
/* 1279 */       this.total = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class MinimumTranslationVector
/*      */   {
/* 1286 */     public Vector2 normal = new Vector2();
/*      */     
/* 1288 */     public float depth = 0.0F;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Intersector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */