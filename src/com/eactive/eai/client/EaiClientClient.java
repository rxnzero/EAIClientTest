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
		/** 
		 * Http Server  
		 */
		context = new SocketAdapterContext();
		context.setAdapterGroupName("AdminAdapterGroup");
		context.setAdapterName("HttpAdapter");
		context.setProtocol("HTTP");
		context.setBoundUsage("IOBOUND");
		context.setSocketType("SERVER");
		context.setHostName("localhost");
		context.setPortNumber(8282);
		context.setAckSupport("N");
		context.setPermanent("N");
		context.setResponseType("SYNC");
		context.setTimeout(60);
		context.setTraceLevel(context.TRACE_LEVEL_DEFAULT);		
		ISocketAdapter httpAdapter;
		try {
			httpAdapter = SocketAdapterFactory.createSocketAdapter(context);
			httpAdapter.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
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
		context.setMaxConnection(3);
		context.setMaxThread(3);
		context.setIdleTimeout(10);
		
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
		context.setMaxConnection(4);
		context.setMaxThread(4);
		context.setPollingUse("Y");
		context.setConnLimitPerIp(1);
		
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
			while(true) {
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			outbound.stop().join();
		}
		
	}
}
