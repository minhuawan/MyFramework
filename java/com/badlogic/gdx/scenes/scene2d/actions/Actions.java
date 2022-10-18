/*     */ package com.badlogic.gdx.scenes.scene2d.actions;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.scenes.scene2d.Action;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import com.badlogic.gdx.utils.Pools;
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
/*     */ public class Actions
/*     */ {
/*     */   public static <T extends Action> T action(Class<T> type) {
/*  33 */     Pool<T> pool = Pools.get(type);
/*  34 */     Action action = (Action)pool.obtain();
/*  35 */     action.setPool(pool);
/*  36 */     return (T)action;
/*     */   }
/*     */   
/*     */   public static AddAction addAction(Action action) {
/*  40 */     AddAction addAction = action(AddAction.class);
/*  41 */     addAction.setAction(action);
/*  42 */     return addAction;
/*     */   }
/*     */   
/*     */   public static AddAction addAction(Action action, Actor targetActor) {
/*  46 */     AddAction addAction = action(AddAction.class);
/*  47 */     addAction.setTarget(targetActor);
/*  48 */     addAction.setAction(action);
/*  49 */     return addAction;
/*     */   }
/*     */   
/*     */   public static RemoveAction removeAction(Action action) {
/*  53 */     RemoveAction removeAction = action(RemoveAction.class);
/*  54 */     removeAction.setAction(action);
/*  55 */     return removeAction;
/*     */   }
/*     */   
/*     */   public static RemoveAction removeAction(Action action, Actor targetActor) {
/*  59 */     RemoveAction removeAction = action(RemoveAction.class);
/*  60 */     removeAction.setTarget(targetActor);
/*  61 */     removeAction.setAction(action);
/*  62 */     return removeAction;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MoveToAction moveTo(float x, float y) {
/*  67 */     return moveTo(x, y, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static MoveToAction moveTo(float x, float y, float duration) {
/*  71 */     return moveTo(x, y, duration, null);
/*     */   }
/*     */   
/*     */   public static MoveToAction moveTo(float x, float y, float duration, Interpolation interpolation) {
/*  75 */     MoveToAction action = action(MoveToAction.class);
/*  76 */     action.setPosition(x, y);
/*  77 */     action.setDuration(duration);
/*  78 */     action.setInterpolation(interpolation);
/*  79 */     return action;
/*     */   }
/*     */   
/*     */   public static MoveToAction moveToAligned(float x, float y, int alignment) {
/*  83 */     return moveToAligned(x, y, alignment, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static MoveToAction moveToAligned(float x, float y, int alignment, float duration) {
/*  87 */     return moveToAligned(x, y, alignment, duration, null);
/*     */   }
/*     */   
/*     */   public static MoveToAction moveToAligned(float x, float y, int alignment, float duration, Interpolation interpolation) {
/*  91 */     MoveToAction action = action(MoveToAction.class);
/*  92 */     action.setPosition(x, y, alignment);
/*  93 */     action.setDuration(duration);
/*  94 */     action.setInterpolation(interpolation);
/*  95 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MoveByAction moveBy(float amountX, float amountY) {
/* 100 */     return moveBy(amountX, amountY, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static MoveByAction moveBy(float amountX, float amountY, float duration) {
/* 104 */     return moveBy(amountX, amountY, duration, null);
/*     */   }
/*     */   
/*     */   public static MoveByAction moveBy(float amountX, float amountY, float duration, Interpolation interpolation) {
/* 108 */     MoveByAction action = action(MoveByAction.class);
/* 109 */     action.setAmount(amountX, amountY);
/* 110 */     action.setDuration(duration);
/* 111 */     action.setInterpolation(interpolation);
/* 112 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SizeToAction sizeTo(float x, float y) {
/* 117 */     return sizeTo(x, y, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static SizeToAction sizeTo(float x, float y, float duration) {
/* 121 */     return sizeTo(x, y, duration, null);
/*     */   }
/*     */   
/*     */   public static SizeToAction sizeTo(float x, float y, float duration, Interpolation interpolation) {
/* 125 */     SizeToAction action = action(SizeToAction.class);
/* 126 */     action.setSize(x, y);
/* 127 */     action.setDuration(duration);
/* 128 */     action.setInterpolation(interpolation);
/* 129 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SizeByAction sizeBy(float amountX, float amountY) {
/* 134 */     return sizeBy(amountX, amountY, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static SizeByAction sizeBy(float amountX, float amountY, float duration) {
/* 138 */     return sizeBy(amountX, amountY, duration, null);
/*     */   }
/*     */   
/*     */   public static SizeByAction sizeBy(float amountX, float amountY, float duration, Interpolation interpolation) {
/* 142 */     SizeByAction action = action(SizeByAction.class);
/* 143 */     action.setAmount(amountX, amountY);
/* 144 */     action.setDuration(duration);
/* 145 */     action.setInterpolation(interpolation);
/* 146 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ScaleToAction scaleTo(float x, float y) {
/* 151 */     return scaleTo(x, y, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static ScaleToAction scaleTo(float x, float y, float duration) {
/* 155 */     return scaleTo(x, y, duration, null);
/*     */   }
/*     */   
/*     */   public static ScaleToAction scaleTo(float x, float y, float duration, Interpolation interpolation) {
/* 159 */     ScaleToAction action = action(ScaleToAction.class);
/* 160 */     action.setScale(x, y);
/* 161 */     action.setDuration(duration);
/* 162 */     action.setInterpolation(interpolation);
/* 163 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ScaleByAction scaleBy(float amountX, float amountY) {
/* 168 */     return scaleBy(amountX, amountY, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static ScaleByAction scaleBy(float amountX, float amountY, float duration) {
/* 172 */     return scaleBy(amountX, amountY, duration, null);
/*     */   }
/*     */   
/*     */   public static ScaleByAction scaleBy(float amountX, float amountY, float duration, Interpolation interpolation) {
/* 176 */     ScaleByAction action = action(ScaleByAction.class);
/* 177 */     action.setAmount(amountX, amountY);
/* 178 */     action.setDuration(duration);
/* 179 */     action.setInterpolation(interpolation);
/* 180 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static RotateToAction rotateTo(float rotation) {
/* 185 */     return rotateTo(rotation, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static RotateToAction rotateTo(float rotation, float duration) {
/* 189 */     return rotateTo(rotation, duration, null);
/*     */   }
/*     */   
/*     */   public static RotateToAction rotateTo(float rotation, float duration, Interpolation interpolation) {
/* 193 */     RotateToAction action = action(RotateToAction.class);
/* 194 */     action.setRotation(rotation);
/* 195 */     action.setDuration(duration);
/* 196 */     action.setInterpolation(interpolation);
/* 197 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static RotateByAction rotateBy(float rotationAmount) {
/* 202 */     return rotateBy(rotationAmount, 0.0F, null);
/*     */   }
/*     */   
/*     */   public static RotateByAction rotateBy(float rotationAmount, float duration) {
/* 206 */     return rotateBy(rotationAmount, duration, null);
/*     */   }
/*     */   
/*     */   public static RotateByAction rotateBy(float rotationAmount, float duration, Interpolation interpolation) {
/* 210 */     RotateByAction action = action(RotateByAction.class);
/* 211 */     action.setAmount(rotationAmount);
/* 212 */     action.setDuration(duration);
/* 213 */     action.setInterpolation(interpolation);
/* 214 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ColorAction color(Color color) {
/* 219 */     return color(color, 0.0F, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ColorAction color(Color color, float duration) {
/* 224 */     return color(color, duration, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ColorAction color(Color color, float duration, Interpolation interpolation) {
/* 229 */     ColorAction action = action(ColorAction.class);
/* 230 */     action.setEndColor(color);
/* 231 */     action.setDuration(duration);
/* 232 */     action.setInterpolation(interpolation);
/* 233 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static AlphaAction alpha(float a) {
/* 238 */     return alpha(a, 0.0F, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static AlphaAction alpha(float a, float duration) {
/* 243 */     return alpha(a, duration, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static AlphaAction alpha(float a, float duration, Interpolation interpolation) {
/* 248 */     AlphaAction action = action(AlphaAction.class);
/* 249 */     action.setAlpha(a);
/* 250 */     action.setDuration(duration);
/* 251 */     action.setInterpolation(interpolation);
/* 252 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static AlphaAction fadeOut(float duration) {
/* 257 */     return alpha(0.0F, duration, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static AlphaAction fadeOut(float duration, Interpolation interpolation) {
/* 262 */     AlphaAction action = action(AlphaAction.class);
/* 263 */     action.setAlpha(0.0F);
/* 264 */     action.setDuration(duration);
/* 265 */     action.setInterpolation(interpolation);
/* 266 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public static AlphaAction fadeIn(float duration) {
/* 271 */     return alpha(1.0F, duration, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static AlphaAction fadeIn(float duration, Interpolation interpolation) {
/* 276 */     AlphaAction action = action(AlphaAction.class);
/* 277 */     action.setAlpha(1.0F);
/* 278 */     action.setDuration(duration);
/* 279 */     action.setInterpolation(interpolation);
/* 280 */     return action;
/*     */   }
/*     */   
/*     */   public static VisibleAction show() {
/* 284 */     return visible(true);
/*     */   }
/*     */   
/*     */   public static VisibleAction hide() {
/* 288 */     return visible(false);
/*     */   }
/*     */   
/*     */   public static VisibleAction visible(boolean visible) {
/* 292 */     VisibleAction action = action(VisibleAction.class);
/* 293 */     action.setVisible(visible);
/* 294 */     return action;
/*     */   }
/*     */   
/*     */   public static TouchableAction touchable(Touchable touchable) {
/* 298 */     TouchableAction action = action(TouchableAction.class);
/* 299 */     action.setTouchable(touchable);
/* 300 */     return action;
/*     */   }
/*     */   
/*     */   public static RemoveActorAction removeActor() {
/* 304 */     return action(RemoveActorAction.class);
/*     */   }
/*     */   
/*     */   public static RemoveActorAction removeActor(Actor removeActor) {
/* 308 */     RemoveActorAction action = action(RemoveActorAction.class);
/* 309 */     action.setTarget(removeActor);
/* 310 */     return action;
/*     */   }
/*     */   
/*     */   public static DelayAction delay(float duration) {
/* 314 */     DelayAction action = action(DelayAction.class);
/* 315 */     action.setDuration(duration);
/* 316 */     return action;
/*     */   }
/*     */   
/*     */   public static DelayAction delay(float duration, Action delayedAction) {
/* 320 */     DelayAction action = action(DelayAction.class);
/* 321 */     action.setDuration(duration);
/* 322 */     action.setAction(delayedAction);
/* 323 */     return action;
/*     */   }
/*     */   
/*     */   public static TimeScaleAction timeScale(float scale, Action scaledAction) {
/* 327 */     TimeScaleAction action = action(TimeScaleAction.class);
/* 328 */     action.setScale(scale);
/* 329 */     action.setAction(scaledAction);
/* 330 */     return action;
/*     */   }
/*     */   
/*     */   public static SequenceAction sequence(Action action1) {
/* 334 */     SequenceAction action = action(SequenceAction.class);
/* 335 */     action.addAction(action1);
/* 336 */     return action;
/*     */   }
/*     */   
/*     */   public static SequenceAction sequence(Action action1, Action action2) {
/* 340 */     SequenceAction action = action(SequenceAction.class);
/* 341 */     action.addAction(action1);
/* 342 */     action.addAction(action2);
/* 343 */     return action;
/*     */   }
/*     */   
/*     */   public static SequenceAction sequence(Action action1, Action action2, Action action3) {
/* 347 */     SequenceAction action = action(SequenceAction.class);
/* 348 */     action.addAction(action1);
/* 349 */     action.addAction(action2);
/* 350 */     action.addAction(action3);
/* 351 */     return action;
/*     */   }
/*     */   
/*     */   public static SequenceAction sequence(Action action1, Action action2, Action action3, Action action4) {
/* 355 */     SequenceAction action = action(SequenceAction.class);
/* 356 */     action.addAction(action1);
/* 357 */     action.addAction(action2);
/* 358 */     action.addAction(action3);
/* 359 */     action.addAction(action4);
/* 360 */     return action;
/*     */   }
/*     */   
/*     */   public static SequenceAction sequence(Action action1, Action action2, Action action3, Action action4, Action action5) {
/* 364 */     SequenceAction action = action(SequenceAction.class);
/* 365 */     action.addAction(action1);
/* 366 */     action.addAction(action2);
/* 367 */     action.addAction(action3);
/* 368 */     action.addAction(action4);
/* 369 */     action.addAction(action5);
/* 370 */     return action;
/*     */   }
/*     */   
/*     */   public static SequenceAction sequence(Action... actions) {
/* 374 */     SequenceAction action = action(SequenceAction.class);
/* 375 */     for (int i = 0, n = actions.length; i < n; i++)
/* 376 */       action.addAction(actions[i]); 
/* 377 */     return action;
/*     */   }
/*     */   
/*     */   public static SequenceAction sequence() {
/* 381 */     return action(SequenceAction.class);
/*     */   }
/*     */   
/*     */   public static ParallelAction parallel(Action action1) {
/* 385 */     ParallelAction action = action(ParallelAction.class);
/* 386 */     action.addAction(action1);
/* 387 */     return action;
/*     */   }
/*     */   
/*     */   public static ParallelAction parallel(Action action1, Action action2) {
/* 391 */     ParallelAction action = action(ParallelAction.class);
/* 392 */     action.addAction(action1);
/* 393 */     action.addAction(action2);
/* 394 */     return action;
/*     */   }
/*     */   
/*     */   public static ParallelAction parallel(Action action1, Action action2, Action action3) {
/* 398 */     ParallelAction action = action(ParallelAction.class);
/* 399 */     action.addAction(action1);
/* 400 */     action.addAction(action2);
/* 401 */     action.addAction(action3);
/* 402 */     return action;
/*     */   }
/*     */   
/*     */   public static ParallelAction parallel(Action action1, Action action2, Action action3, Action action4) {
/* 406 */     ParallelAction action = action(ParallelAction.class);
/* 407 */     action.addAction(action1);
/* 408 */     action.addAction(action2);
/* 409 */     action.addAction(action3);
/* 410 */     action.addAction(action4);
/* 411 */     return action;
/*     */   }
/*     */   
/*     */   public static ParallelAction parallel(Action action1, Action action2, Action action3, Action action4, Action action5) {
/* 415 */     ParallelAction action = action(ParallelAction.class);
/* 416 */     action.addAction(action1);
/* 417 */     action.addAction(action2);
/* 418 */     action.addAction(action3);
/* 419 */     action.addAction(action4);
/* 420 */     action.addAction(action5);
/* 421 */     return action;
/*     */   }
/*     */   
/*     */   public static ParallelAction parallel(Action... actions) {
/* 425 */     ParallelAction action = action(ParallelAction.class);
/* 426 */     for (int i = 0, n = actions.length; i < n; i++)
/* 427 */       action.addAction(actions[i]); 
/* 428 */     return action;
/*     */   }
/*     */   
/*     */   public static ParallelAction parallel() {
/* 432 */     return action(ParallelAction.class);
/*     */   }
/*     */   
/*     */   public static RepeatAction repeat(int count, Action repeatedAction) {
/* 436 */     RepeatAction action = action(RepeatAction.class);
/* 437 */     action.setCount(count);
/* 438 */     action.setAction(repeatedAction);
/* 439 */     return action;
/*     */   }
/*     */   
/*     */   public static RepeatAction forever(Action repeatedAction) {
/* 443 */     RepeatAction action = action(RepeatAction.class);
/* 444 */     action.setCount(-1);
/* 445 */     action.setAction(repeatedAction);
/* 446 */     return action;
/*     */   }
/*     */   
/*     */   public static RunnableAction run(Runnable runnable) {
/* 450 */     RunnableAction action = action(RunnableAction.class);
/* 451 */     action.setRunnable(runnable);
/* 452 */     return action;
/*     */   }
/*     */   
/*     */   public static LayoutAction layout(boolean enabled) {
/* 456 */     LayoutAction action = action(LayoutAction.class);
/* 457 */     action.setLayoutEnabled(enabled);
/* 458 */     return action;
/*     */   }
/*     */   
/*     */   public static AfterAction after(Action action) {
/* 462 */     AfterAction afterAction = action(AfterAction.class);
/* 463 */     afterAction.setAction(action);
/* 464 */     return afterAction;
/*     */   }
/*     */   
/*     */   public static AddListenerAction addListener(EventListener listener, boolean capture) {
/* 468 */     AddListenerAction addAction = action(AddListenerAction.class);
/* 469 */     addAction.setListener(listener);
/* 470 */     addAction.setCapture(capture);
/* 471 */     return addAction;
/*     */   }
/*     */   
/*     */   public static AddListenerAction addListener(EventListener listener, boolean capture, Actor targetActor) {
/* 475 */     AddListenerAction addAction = action(AddListenerAction.class);
/* 476 */     addAction.setTarget(targetActor);
/* 477 */     addAction.setListener(listener);
/* 478 */     addAction.setCapture(capture);
/* 479 */     return addAction;
/*     */   }
/*     */   
/*     */   public static RemoveListenerAction removeListener(EventListener listener, boolean capture) {
/* 483 */     RemoveListenerAction addAction = action(RemoveListenerAction.class);
/* 484 */     addAction.setListener(listener);
/* 485 */     addAction.setCapture(capture);
/* 486 */     return addAction;
/*     */   }
/*     */   
/*     */   public static RemoveListenerAction removeListener(EventListener listener, boolean capture, Actor targetActor) {
/* 490 */     RemoveListenerAction addAction = action(RemoveListenerAction.class);
/* 491 */     addAction.setTarget(targetActor);
/* 492 */     addAction.setListener(listener);
/* 493 */     addAction.setCapture(capture);
/* 494 */     return addAction;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\Actions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */