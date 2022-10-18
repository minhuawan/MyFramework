package com.gikk.twirk.types.notice;

import com.gikk.twirk.enums.NOTICE_EVENT;
import com.gikk.twirk.types.AbstractType;

public interface Notice extends AbstractType {
  NOTICE_EVENT getEvent();
  
  String getMessage();
  
  String getRawNoticeID();
}


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\notice\Notice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */