package com.gikk.twirk.types.twitchMessage;

import com.gikk.twirk.types.AbstractEmoteMessage;
import com.gikk.twirk.types.TagMap;
import com.gikk.twirk.types.cheer.Cheer;
import java.util.List;

public interface TwitchMessage extends AbstractEmoteMessage {
  String getTag();
  
  String getPrefix();
  
  String getCommand();
  
  String getTarget();
  
  String getContent();
  
  boolean isCheer();
  
  List<Cheer> getCheers();
  
  int getBits();
  
  TagMap getTagMap();
}


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\twitchMessage\TwitchMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */