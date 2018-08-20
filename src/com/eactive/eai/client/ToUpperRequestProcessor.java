package com.eactive.eai.client;

import com.eactive.eai.adapter.socket2.protocol.IRequestProcessor;

public class ToUpperRequestProcessor implements IRequestProcessor {

	public ToUpperRequestProcessor() {		
	}

	public byte[] handle(byte[] request) throws Exception {
		// ����ó�������� �����Ѵ�.
		String req = new String(request);
		String res = req.toUpperCase();
		System.out.println(">> Read : " + req);
		System.out.println("<< Write : " + res);
		return res.getBytes();
	}
	
}
