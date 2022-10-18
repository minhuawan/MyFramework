package com.gikk.twirk.types.clearChat;

import com.gikk.twirk.enums.CLEARCHAT_MODE;
import com.gikk.twirk.types.AbstractType;

public interface ClearChat extends AbstractType {
  CLEARCHAT_MODE getMode();
  
  String getTarget();
  
  int getDuration();
  
  String getReason();
}


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\clearChat\ClearChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */