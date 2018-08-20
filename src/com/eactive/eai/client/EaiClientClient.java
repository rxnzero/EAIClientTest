package com.eactive.eai.client;

import com.eactive.eai.adapter.EAIException;
import com.eactive.eai.adapter.socket2.config.SocketAdapterContext;
import com.eactive.eai.adapter.socket2.config.SocketAdapterFactory;
import com.eactive.eai.adapter.socket2.protocol.ISocketAdapter;

public class EaiClientClient {
	
	static ISocketAdapter outbound = null;
	public EaiClientClient() {
		// TODO Auto-generated constructor stub
	}
	
	public static void startup(String remoteHost, int port) {
		SocketAdapterContext context = new SocketAdapterContext();
		context = new SocketAdapterContext();
		context.setProtocol("FIXED_LENGTH_HEADER");
		context.setAdapterGroupName("ToUpperClient");
		context.setAdapterName("outboundAdapter");
		context.setBoundUsage("OUTBOUND");
		context.setSocketType("CLIENT");
		context.setHostName(remoteHost);
		context.setPortNumber(port);
		context.setResponseType("SYNC");
		context.setTimeout(60);
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
		System.out.println("Start TOUPPER Socket Client - port : " + 8000);
		EaiClientClient.startup("localhost", 8000);
		byte[] response = (byte[])outbound.sendMessage("hello".getBytes());
		System.out.println("TOUPPER : " + new String(response) );
		try {
			outbound.stop().join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
