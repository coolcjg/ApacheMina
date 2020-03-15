package sumup;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;



public abstract class AbstractMessageDecoder implements MessageDecoder{
	private final int type;
	
	private int sequence;
	
	private boolean readHeader;
	
	protected AbstractMessageDecoder(int type) {
		this.type = type;
	}
	
	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		if(in.remaining() < Constants.HEADER_LEN) {
			return MessageDecoderResult.NEED_DATA;
		}
		
		if(type == in.getShort()) {
			return MessageDecoderResult.OK;			
		}
		
		return MessageDecoderResult.NOT_OK;
	}
	
	public MessageDecoderResult decode ( IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception{
		if(!readHeader) {
			in.getShort();
			sequence = in.getInt();
			readHeader = true;
		}
		
		AbstractMessage m = decodeBody(session, in);
		
		if(m == null) {
			return MessageDecoderResult.NEED_DATA;
		}else {
			readHeader = false;
		}
		m.setSequence(sequence);
		out.write(m);
		
		return MessageDecoderResult.OK;
	}
	
	protected abstract AbstractMessage decodeBody(IoSession session, IoBuffer in);

}
