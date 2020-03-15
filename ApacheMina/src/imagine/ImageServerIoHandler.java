package imagine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.integration.jmx.IoSessionMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageServerIoHandler extends IoHandlerAdapter{
	
	private final static String characters = "mina rocks abcdefghijklmnopqrstuvwxyz0123456789";
	
	public static final String INDEX_KEY = ImageServerIoHandler.class.getName() + ".INDEX";
	
	private static Logger LOGGER = LoggerFactory.getLogger(ImageServerIoHandler.class);
	
	private MBeanServer mBeanServer;
	
	public ImageServerIoHandler(MBeanServer mBeanServer) {
		this.mBeanServer = mBeanServer;
	}
	
    public void sessionCreated( IoSession session ) throws Exception
    {
        // create a session MBean in order to load into the MBeanServer and allow
        // this session to be managed by the JMX subsystem.
        IoSessionMBean sessionMBean = new IoSessionMBean( session );
        
        // create a JMX ObjectName.  This has to be in a specific format.  
        ObjectName sessionName = new ObjectName( session.getClass().getPackage().getName() + 
            ":type=session,name=" + session.getClass().getSimpleName() + "-" + session.getId());
        
        // register the bean on the MBeanServer.  Without this line, no JMX will happen for
        // this session
        mBeanServer.registerMBean( sessionMBean, sessionName );
    }
	
	
    public void sessionOpened(IoSession session) throws Exception {
        
        // set the index to zero.  This is used to determine how the build the
        // string that is sent to the client.
        session.setAttribute(INDEX_KEY, 0);
    }
	
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception{
		LOGGER.warn(cause.getMessage(), cause);
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception{
		ImageRequest request = (ImageRequest)message;
		String text1 = generateString(session, request.getNumberOfCharacters());
		String text2 = generateString(session, request.getNumberOfCharacters());
		BufferedImage image1 = createImage(request, text1);
		BufferedImage image2 = createImage(request, text2);
		ImageResponse response = new ImageResponse(image1, image2);
		session.write(response);
	}
	
	
	private BufferedImage createImage(ImageRequest request, String text) {
		BufferedImage image = new BufferedImage(request.getWith(), request.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
		Graphics graphics = image.createGraphics();
		graphics.setColor(Color.YELLOW);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		Font serif = new Font("serif", Font.PLAIN, 30);
		graphics.setFont(serif);
		graphics.setColor(Color.BLUE);
		graphics.drawString(text, 10, 50);
		return image;
	}
	
	private String generateString(IoSession session, int length) {
		Integer index = (Integer) session.getAttribute(INDEX_KEY);
		StringBuffer buffer = new StringBuffer(length);
		while(buffer.length()<length) {
			buffer.append(characters.charAt(index));
			index++;
			if(index >= characters.length()) {
				index = 0;
			}
		}
		
		session.setAttribute(INDEX_KEY, index);
		return buffer.toString();
		
	}
	
	
	

}
