package echo1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import messageEncoder.UserInfo;

/**
 * Created by lz on 2016/8/10.
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] infos = UserInfo();
        for (UserInfo uinfo : infos){
            ctx.writeAndFlush(uinfo);
        }
    }
    private UserInfo[] UserInfo(){
        UserInfo[] userInfos = new UserInfo[10];
        UserInfo userInfo = null;
        for (int i = 0; i < 10; i++) {
            userInfo = new UserInfo();
            userInfo.setUserName("李钊圣师级的决赛么么哒么么哒李钊圣师级的决赛么么哒么么哒么么的撒大大打得李钊圣师级的决赛么么哒么么哒么么的撒大大打得李钊圣师级的决赛么么哒么么哒么么的撒大大打得么么的撒大大打得李钊圣李钊圣师级的决赛么么哒么么哒么么的撒大大打得李钊圣师级的决赛么么哒么么哒么么的撒大大打得师级的决赛么么哒么么哒么么的撒大大打得李钊圣师级的决赛么么李钊圣师级的决赛么么哒么么哒么么的撒大大打得哒么么哒么么的撒大大打得"+(i+1));
            userInfo.setUserID(i+1);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserInfo userInfo = (UserInfo) msg;
        System.out.println("信息"+ userInfo.getUserID() + userInfo.getUserName());
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
