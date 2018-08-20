package com.eactive.eai.client;

import com.eactive.eai.adapter.EAIException;
import com.eactive.eai.adapter.socket2.config.SocketAdapterContext;
import com.eactive.eai.adapter.socket2.config.SocketAdapterFactory;
import com.eactive.eai.adapter.socket2.protocol.ISocketAdapter;

public class EaiClientInbound {
	
	static ISocketAdapter outbound = null;
	public EaiClientInbound() {
		// TODO Auto-generated constructor stub
	}
	
	public static void startup(String remoteHost, int port) {
		SocketAdapterContext context = new SocketAdapterContext();
		context = new SocketAdapterContext();
		context.setProtocol("FIXED_LENGTH_HEADER");
		context.setAdapterGroupName("ToUpperClient");
		context.setAdapterName("InboundAdapter");
		context.setBoundUsage("INBOUND");
		context.setSocketType("CLIENT");
		context.setHostName(remoteHost);
		context.setPortNumber(port);
		context.setResponseType("SYNC");
		context.setTimeout(60);
		context.setPermanent("Y");
		context.setTraceLevel(context.TRACE_LEVEL_TRACE);
		context.setRequestProcessor(new ToUpperRequestProcessor());
		
		
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
		System.out.println("Start TOUPPER Socket Client  INBOUND - port : " + port);
		EaiClientInbound.startup("localhost", port);
	}
}
