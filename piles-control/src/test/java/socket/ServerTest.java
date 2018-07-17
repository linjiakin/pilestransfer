package socket;

import com.alibaba.fastjson.JSON;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.control.entity.RemoteClosePushRequest;
import com.piles.control.entity.RemoteStartPushRequest;
import com.piles.control.service.IRemoteClosePushService;
import com.piles.control.service.IRemoteStartPushService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

public class ServerTest {
    private static final String SRPING_ROOT_XML = "classpath:application.xml";


    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTest.class);

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(SRPING_ROOT_XML)) {
            context.registerShutdownHook();
            context.start();


            LOGGER.info("启动成功");

            while (true){
                Channel channel=ChannelMapByEntity.getChannel(1,"1000025484561835");
                if (null!=channel){
//                    IRemoteClosePushService service= context.getBean( IRemoteClosePushService.class );
//                    RemoteClosePushRequest remoteClosePushRequest= new RemoteClosePushRequest();
//                    remoteClosePushRequest.setGunNo( 1 );
//                    remoteClosePushRequest.setOrderNo( 123456L );
//                    remoteClosePushRequest.setPileNo( "1000025484561835" );
//                    remoteClosePushRequest.setSerial( "0000" );
//                    System.out.println("远程结束充电返回信息"+ JSON.toJSONString(  service.doPush( remoteClosePushRequest)));

                    IRemoteStartPushService service= context.getBean( IRemoteStartPushService.class );
                    RemoteStartPushRequest remoteClosePushRequest= new RemoteStartPushRequest();
                    remoteClosePushRequest.setGunNo( 1 );
                    remoteClosePushRequest.setOrderNo( 123456L );
                    remoteClosePushRequest.setPileNo( "1000025484561835" );
                    remoteClosePushRequest.setSerial( "0" );
                    remoteClosePushRequest.setChargeData( new BigDecimal( 1 ) );
                    remoteClosePushRequest.setChargeModel( 1 );
                    remoteClosePushRequest.setChargeStopCode( "646444" );
                    System.out.println("远程启动充电返回信息"+ JSON.toJSONString(  service.doPush( remoteClosePushRequest)));

                }
                Thread.sleep(10000L);
            }

        } catch (Exception e) {
            LOGGER.error("Spring 启动错误", e);
        }

    }

}
