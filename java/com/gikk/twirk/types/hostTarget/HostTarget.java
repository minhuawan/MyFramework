package com.gikk.twirk.types.hostTarget;

import com.gikk.twirk.enums.HOSTTARGET_MODE;
import com.gikk.twirk.types.AbstractType;

public interface HostTarget extends AbstractType {
  HOSTTARGET_MODE getMode();
  
  String getTarget();
  
  int getViewerCount();
}


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\hostTarget\HostTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */