package com.eactive.eai.client;

import com.eactive.eai.adapter.EAIException;
import com.eactive.eai.adapter.socket2.config.SocketAdapterContext;
import com.eactive.eai.adapter.socket2.config.SocketAdapterFactory;
import com.eactive.eai.adapter.socket2.protocol.ISocketAdapter;

public class EaiServerOutbound {
	
	static ISocketAdapter outbound = null;
	public EaiServerOutbound() {
		
	}
	
	public static void startup(int port) {
		SocketAdapterContext context = new SocketAdapterContext();
		context = new SocketAdapterContext();
		context.setProtocol("FIXED_LENGTH_HEADER");
		context.setAdapterGroupName("ToUpperServerOutbound");
		context.setAdapterName("OutboundAdapter");
		context.setBoundUsage("OUTBOUND");
		context.setSocketType("SERVER");		
		context.setPortNumber(port);
		context.setResponseType("SYNC");
		context.setTimeout(60);
		context.setPermanent("Y");
		context.setTraceLevel(context.TRACE_LEVEL_TRACE);
		
		
		
		try {
			outbound = SocketAdapterFactory.createSocketAdapter(context);
		} catch (EAIException e) {
			e.printStackTrace();
		}
		try {
			outbound.start().join();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	static public void main(String[] args) throws Exception {
		int port = 9000;
		EaiServerOutbound.startup(port);
		System.out.println("Start TOUPPER Socket Server : port - "+ port);
		
		while(true) {
			while(outbound.getSessionPool().size() == 0) {
				System.out.println("Wait Connection... ");
				Thread.sleep(1000);
			}
			System.out.println("SEND ");
			byte[] response = (byte[])outbound.sendMessage("hello".getBytes());
			System.out.println("TOUPPER : " + new String(response) );
			Thread.sleep(1000);
		}
		
	}
	
}
