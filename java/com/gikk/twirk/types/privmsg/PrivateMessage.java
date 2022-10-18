package com.gikk.twirk.types.privmsg;

import com.gikk.twirk.types.AbstractEmoteMessage;
import com.gikk.twirk.types.cheer.Cheer;
import java.util.List;

public interface PrivateMessage extends AbstractEmoteMessage {
  boolean isCheer();
  
  List<Cheer> getCheers();
  
  int getBits();
}


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\privmsg\PrivateMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */