package sumup;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSessionHandler  extends IoHandlerAdapter{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientSessionHandler.class);
	
	private final int[] values;
	
	private boolean finished;
	
	public ClientSessionHandler(int[] values) {
		this.values = values;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	@Override
	public void sessionOpened(IoSession session) {
		
		for(int i =0; i<values.length;i++) {
			AddMessage m = new AddMessage();
			m.setSequence(i);
			m.setValue(values[i]);
			session.write(m);
		}
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) {
		ResultMessage rm = (ResultMessage) message;
		
		if(rm.isOk()) {
			
			if(rm.getSequence() == values.length -1){
				LOGGER.info("The sum: " + rm.getValue());
				session.closeNow();
				finished=true;
			}
		}else {
			LOGGER.warn("Server error, disconnecting....");
			session.closeNow();
			finished = true;
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.closeNow();
	}

}
