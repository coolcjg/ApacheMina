package imagine;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.integration.jmx.IoFilterMBean;
import org.apache.mina.integration.jmx.IoServiceMBean;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import imagine.codec.ImageCodecFactory;

public class ImageServer {
	public static final int PORT = 33790;
	
	public static void main(String[] args) throws Exception{
		
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		
		
		ImageServerIoHandler handler = new ImageServerIoHandler(mBeanServer);
		
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		
		IoServiceMBean acceptorMBean = new IoServiceMBean(acceptor);
		
		ObjectName acceptorName = new ObjectName(acceptor.getClass().getPackage().getName() + " :type=acceptor,name=" + acceptor.getClass().getSimpleName());
		
		mBeanServer.registerMBean(acceptorMBean,  acceptorName);
		
		ProtocolCodecFilter protocolFilter = new ProtocolCodecFilter(new ImageCodecFactory(false));
		
		IoFilterMBean protocolFilterMBean = new IoFilterMBean(protocolFilter);
		
		ObjectName protocolFilterName = new ObjectName(protocolFilter.getClass().getPackage().getName() + ":type=protocolfilter, name = " + protocolFilter.getClass().getSimpleName());

		mBeanServer.registerMBean(protocolFilterMBean,  protocolFilterName);
		
		acceptor.getFilterChain().addLast("protocol", protocolFilter);
		
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		
		
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		
		acceptor.setHandler(handler);
		
		acceptor.bind(new InetSocketAddress(PORT));
		
		System.out.println("Step 3 server is listening at port : " + PORT);
		
		
		
	}

}
