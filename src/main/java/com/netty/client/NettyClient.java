/**
 * 
 */
package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**   
 * @ClassName:  NettyClient   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: dbr 
 * @date:   2018年8月10日 下午4:59:30   
 *      
 */
public class NettyClient {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口号
     */
    private int port;

    /**
     * 构造函数
     * 
     * @param host
     * @param port
     */
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 连接方法
     */
    public void connect(String msg) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new NettyClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            // 发送json字符串
            channel.writeAndFlush(msg);
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 测试入口
     * 
     * @param args
     */
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9991;
        String msg = "{\"name\":\"admin\",\"age\":27}\n";

        NettyClient nettyClient = new NettyClient(host, port);
        nettyClient.connect(msg);
    }

}
