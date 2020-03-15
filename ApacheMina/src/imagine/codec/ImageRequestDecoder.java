package imagine.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import imagine.ImageRequest;

public class ImageRequestDecoder extends CumulativeProtocolDecoder{
	
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception{
		if(in.remaining() >=12) {
			int width = in.getInt();
			int height = in.getInt();
			int numberOfCharacters = in.getInt();
			ImageRequest request = new ImageRequest(width, height, numberOfCharacters);
			out.write(request);
			return true;
		}else {
			return false;
		}
	}

}
