package com.badlogic.gdx.math;

public interface Vector<T extends Vector<T>> {
  T cpy();
  
  float len();
  
  float len2();
  
  T limit(float paramFloat);
  
  T limit2(float paramFloat);
  
  T setLength(float paramFloat);
  
  T setLength2(float paramFloat);
  
  T clamp(float paramFloat1, float paramFloat2);
  
  T set(T paramT);
  
  T sub(T paramT);
  
  T nor();
  
  T add(T paramT);
  
  float dot(T paramT);
  
  T scl(float paramFloat);
  
  T scl(T paramT);
  
  float dst(T paramT);
  
  float dst2(T paramT);
  
  T lerp(T paramT, float paramFloat);
  
  T interpolate(T paramT, float paramFloat, Interpolation paramInterpolation);
  
  T setToRandomDirection();
  
  boolean isUnit();
  
  boolean isUnit(float paramFloat);
  
  boolean isZero();
  
  boolean isZero(float paramFloat);
  
  boolean isOnLine(T paramT, float paramFloat);
  
  boolean isOnLine(T paramT);
  
  boolean isCollinear(T paramT, float paramFloat);
  
  boolean isCollinear(T paramT);
  
  boolean isCollinearOpposite(T paramT, float paramFloat);
  
  boolean isCollinearOpposite(T paramT);
  
  boolean isPerpendicular(T paramT);
  
  boolean isPerpendicular(T paramT, float paramFloat);
  
  boolean hasSameDirection(T paramT);
  
  boolean hasOppositeDirection(T paramT);
  
  boolean epsilonEquals(T paramT, float paramFloat);
  
  T mulAdd(T paramT, float paramFloat);
  
  T mulAdd(T paramT1, T paramT2);
  
  T setZero();
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Vector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */