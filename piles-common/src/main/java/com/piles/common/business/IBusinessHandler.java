package com.piles.common.business;

import io.netty.channel.Channel;


public interface IBusinessHandler {

	byte[] process(byte[] msg, Channel incoming);

}
