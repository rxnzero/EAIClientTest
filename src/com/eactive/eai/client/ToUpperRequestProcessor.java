package com.eactive.eai.client;

import com.eactive.eai.adapter.socket2.protocol.IRequestProcessor;

public class ToUpperRequestProcessor implements IRequestProcessor {

	public ToUpperRequestProcessor() {		
	}

	public byte[] handle(byte[] request) throws Exception {
		// 내부처리로직을 구현한다.
		String req = new String(request);
		String res = req.toUpperCase();
		System.out.println(">> Read : " + req);
		System.out.println("<< Write : " + res);
		return res.getBytes();
	}
	
}
