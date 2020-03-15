package imagine;

import java.awt.image.BufferedImage;

public interface ImageListener {
	void onImage(BufferedImage image1, BufferedImage image2);
	
	void onException(Throwable throwable);
	
	void sessionOpened();
	
	void sessionClosed();

}
