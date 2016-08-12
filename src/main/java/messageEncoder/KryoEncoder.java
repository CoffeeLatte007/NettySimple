package messageEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by lz on 2016/8/11.
 */
public class KryoEncoder extends MessageToByteEncoder<Object> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] raw = KryoUtils.writeKryoObject(o);
        System.out.println(raw.length);
        byteBuf.writeBytes(raw);
    }
}
