package com.gikk.twirk.types.usernotice.subtype;

import java.util.Optional;

public interface Subscription {
  SubscriptionPlan getSubscriptionPlan();
  
  int getMonths();
  
  int getStreak();
  
  boolean isSharedStreak();
  
  String getSubscriptionPlanName();
  
  boolean isResub();
  
  boolean isGift();
  
  Optional<SubscriptionGift> getSubscriptionGift();
}


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\type\\usernotice\subtype\Subscription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */