package com.piles.common.business;

import io.netty.channel.Channel;

public interface IBusiness {

	byte[] process(byte[] msg,Channel incoming);
	
}
