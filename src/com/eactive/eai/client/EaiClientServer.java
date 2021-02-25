package com.eactive.eai.client;

import com.eactive.eai.adapter.EAIException;
import com.eactive.eai.adapter.socket2.config.SocketAdapterContext;
import com.eactive.eai.adapter.socket2.config.SocketAdapterFactory;
import com.eactive.eai.adapter.socket2.protocol.ISocketAdapter;

public class EaiClientServer {

	public EaiClientServer() {
		
	}
	// -DEAISocket -Dlog4j.debug=false -Dlog4j.configuration=file:/D:/workspace/EAIClientTest/config/log4j.xml
	public static void startup(int port) {
		SocketAdapterContext context = new SocketAdapterContext();
		context = new SocketAdapterContext();
		context.setProtocol("FIXED_LENGTH_HEADER");
		context.setAdapterGroupName("ToUpperServer");
		context.setAdapterName("InboundAdapter");
		context.setBoundUsage("INBOUND");
		context.setSocketType("SERVER");		
		context.setPortNumber(port);
		context.setResponseType("SYNC");
		context.setTimeout(60);
		context.setTraceLevel(context.TRACE_LEVEL_TRACE);
		context.setRequestProcessor(new ToUpperRequestProcessor());
		
		ISocketAdapter inboundServer = null;
		try {
			inboundServer = SocketAdapterFactory.createSocketAdapter(context);
		} catch (EAIException e) {
			e.printStackTrace();
		}
		try {
			inboundServer.start().join();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	static public void main(String[] args) throws Exception {
		int port = 8000;
		EaiClientServer.startup(port);
		System.out.println("Start TOUPPER Socket Server : port - "+ port);
	}
	
}
