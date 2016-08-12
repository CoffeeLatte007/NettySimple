package messageEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

/**
 * Created by lz on 2016/8/11.
 */
public class KryoDecoder extends MessageToMessageDecoder<ByteBuf> {
    Class cl ;
    public KryoDecoder(Class<UserInfo> userInfoClass) {
        cl = userInfoClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(),array,0,length);
        list.add(KryoUtils.readKryoObject(cl, array));
    }
}
