package com.eactive.eai.client;

import com.eactive.eai.adapter.EAIException;
import com.eactive.eai.adapter.socket2.config.SocketAdapterContext;
import com.eactive.eai.adapter.socket2.config.SocketAdapterFactory;
import com.eactive.eai.adapter.socket2.protocol.ISocketAdapter;

public class EaiClientPolling {
	static ISocketAdapter outbound = null;
	public EaiClientPolling() {
		
	}
	// -DEAISocket -Dlog4j.debug=false -Dlog4j.configuration=file:/D:/workspace/EAIClientTest/config/log4j.xml
	public static void startup(String remoteHost, int port, boolean startServer) {
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
		
		String pollingRequest = "HDRREQPOLL%d{MMddHHmmss}";
		String pollingRequestPattern = "(HDR)?(REQPOLL)([0-9]{10})";
		String pollingReplacement = "$1RESPOLL$3";
		String pollingResponsePattern = "(HDR)?(RESPOLL)([0-9]{10})";
		
		if(startServer) {
			context = new SocketAdapterContext();
			context.setProtocol("FIXED_LENGTH_HEADER");
			context.setAdapterGroupName("PollingServer");
			context.setAdapterName("InboundAdapter");
			context.setBoundUsage("INBOUND");
			context.setSocketType("SERVER");		
			context.setPortNumber(port);
			context.setResponseType("SYNC");
			context.setTimeout(20);
			context.setTraceLevel(context.TRACE_LEVEL_TRACE);
			context.setRequestProcessor(new ToUpperRequestProcessor());
			context.setMaxConnection(4);
			context.setMaxThread(4);
	//		context.setIdleTimeout(10);
			context.setPollingUse("Y");
			context.setPollingRequest(pollingRequest);
			context.setPollingRequestPattern(pollingRequestPattern);
			context.setPollingReplacement(pollingReplacement);
			context.setPollingResponsePattern(pollingResponsePattern);
			
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
		context = new SocketAdapterContext();
		context.setProtocol("FIXED_LENGTH_HEADER");
		context.setAdapterGroupName("PollingClient");
		context.setAdapterName("outboundAdapter");
		context.setBoundUsage("OUTBOUND");
		context.setSocketType("CLIENT");
		context.setHostName(remoteHost);
		context.setPortNumber(port);
		context.setResponseType("SYNC");
		context.setTimeout(20);
//		context.setIdleTimeout(30);
		context.setTraceLevel(context.TRACE_LEVEL_TRACE);
//		context.setRequestProcessor(new ToUpperRequestProcessor());
		context.setMaxConnection(4);
		context.setMaxThread(4);
//		context.setConnLimitPerIp(1);
		
		context.setPollingUse("Y");
		context.setPollingTimeout(30);
		context.setPollingRequest(pollingRequest);
		context.setPollingRequestPattern(pollingRequestPattern);
		context.setPollingReplacement(pollingReplacement);
		context.setPollingResponsePattern(pollingResponsePattern);
		
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
		int port = 8000;
		
		if(args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			}
			catch(Exception e) {
				;
			}
		}
		EaiClientPolling.startup("localhost", port, true);
		System.out.println("Start Polling Socket Server : port - "+ port);
	}
	
}
