package socket.client;

import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.record.entity.UploadChargeMonitorRequest;
import com.piles.record.entity.UploadChargeRateRequest;
import com.piles.record.entity.UploadRecordRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {

    public static String host = "127.0.0.1";
    public static int port = 10240;
    public static LinkedBlockingQueue queue = new LinkedBlockingQueue();
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer());

            // 连接服务端
            Channel ch = b.connect(host, port).sync().channel();
//            byte[] msg= new byte[]{0x68,0x01,0x00,0x00,0x00,0x1D,0x10,0x00,0x02,0x54,(byte)0x84,0x56,0x18,0x35,0x02,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
//                    0x01,0x00,0x01,0x02,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x01,0x2B,(byte)0xD9};
            //请求体
            byte[] request = new byte[]{};
            /******************心跳*******************/
//            request = Bytes.concat(request,new byte[]{0x0C});
//            byte[] msg =packHeartBeatRequest();
            /******************心跳*******************/
            /******************上传监控*******************/
//            request = Bytes.concat(request,new byte[]{0x0A});
//            byte[] msg =packUploadChargeMonitorRequest();
            /******************上传监控*******************/
            /******************上传记录*******************/
//            request = Bytes.concat(request,new byte[]{0x08});
//            byte[] msg =packUploadRecordRequest();
            /******************上传记录*******************/
            /******************上传充电进度*******************/
            request = Bytes.concat(request,new byte[]{0x09});
            byte[] msg =packUploadChargeRateRequest();
            /******************上传充电进度*******************/
            //流水号
            request = Bytes.concat(request,BytesUtil.intToBytes(1,2));
            //报文体长度
            request = Bytes.concat(request,BytesUtil.intToBytes(msg.length,2));
            request = Bytes.concat(request,msg);
            int crc = CRC16Util.getCRC(request);
            request = Bytes.concat(request,BytesUtil.intToBytes(crc,2));
            request = Bytes.concat(new byte[]{0x68},request);
            ch.writeAndFlush(request);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
        } finally {
            group.shutdownGracefully();
        }
    }

    //心跳
    private static byte[] packHeartBeatRequest(){
        byte[] msg = new byte[]{};
        int gunLen = 3;
        msg = Bytes.concat(msg,BytesUtil.intToBytes(gunLen,1));
        int[] statas = new int[gunLen];
        for(int i =0;i<gunLen;i++){
            statas[i]= i+1;
        }
        for(int i =0;i<statas.length;i++) {
            msg = Bytes.concat(msg,BytesUtil.intToBytes(statas[i],1));
        }

        return msg;
    }
    //上传监控
    private static byte[] packUploadChargeMonitorRequest(){
        UploadChargeMonitorRequest request = new UploadChargeMonitorRequest();
        request.setBatteryNominalEnergy(new BigDecimal(30));
        request.setBatteryRatedEnergy(new BigDecimal(31));
        request.setBatteryRatedVoltage(new BigDecimal(32));
        request.setBatteryProducer(32423423);
        request.setBatteryProduceTime("171108");
        request.setBatteryCycleCount(80);
        request.setHighestAllowElectricity(new BigDecimal(36));
        request.setHighestAllowVoltage(new BigDecimal(37));
        request.setHighestAllowTemperature(new BigDecimal(38));
        request.setSingleAllowHighestVoltage(new BigDecimal(39));
        request.setGunNo(1);
        request.setOrderNo(111111333);
        request.setBmsVersion(123);
        request.setBmsType(1);
        request.setAxVoltage(new BigDecimal(54));
        request.setBxVoltage(new BigDecimal(55));
        request.setCxVoltage(new BigDecimal(56));
        request.setAxElectricity(new BigDecimal(57));
        request.setBxElectricity(new BigDecimal(58));
        request.setCxElectricity(new BigDecimal(59));
        request.setSingleHighestVoltage(new BigDecimal(61));
        request.setSingleLowestVoltage(new BigDecimal(62));
        request.setSingleHighestTemperature(new BigDecimal(63));
        request.setSingleLowestTemperature(new BigDecimal(64));
        request.setChargerTemperature(new BigDecimal(65.434));
        request.setChargerGunTemperature(new BigDecimal(66.343));
        request.setChargerImportVoltage(new BigDecimal(67.543));
        request.setChargerImportElectricity(new BigDecimal(68));
        request.setChargerImportPower(new BigDecimal(69));
        request.setChargerExportVoltage(new BigDecimal(70.343));
        request.setChargerExportElectricity(new BigDecimal(71.343));
        request.setChargerExportPower(new BigDecimal(72));
        request.setVoltageRequire(new BigDecimal(73));
        request.setElectricityRequire(new BigDecimal(74));
        System.out.println(request.toString());
        return UploadChargeMonitorRequest.packBytes(request);
    }

    //上传记录
    public static byte[] packUploadRecordRequest(){
        UploadRecordRequest request = new UploadRecordRequest();
        request.setGunNo(1);
        request.setOrderNo(3232323L);
        request.setChargeModel(1);
        request.setCardNo("3828348438283484");
        request.setVin("12345123451234567");
        request.setSoc(3);
        request.setEndReason(1);
        request.setStartTime("171101224021");
        request.setEndTime("171101225021");
        request.setStartAmmeterDegree(new BigDecimal(38));
        request.setEndAmmeterDegree(new BigDecimal(39));
        request.setTotalAmmeterDegree(new BigDecimal(40));
        request.setPointElectricQuantity(new BigDecimal(41));
        request.setPeakElectricQuantity(new BigDecimal(42));
        request.setOrdinaryElectricQuantity(new BigDecimal(43));
        request.setDipElectricQuantity(new BigDecimal(44));
        request.setTotalElectricAmount(new BigDecimal(45));
        request.setPointElectricAmount(new BigDecimal(46));
        request.setPeakElectricAmount(new BigDecimal(47));
        request.setOrdinaryElectricAmount(new BigDecimal(48));
        request.setDipElectricAmount(new BigDecimal(49));
        request.setSubscriptionAmount(new BigDecimal(50));
        request.setServiceAmount(new BigDecimal(51));
        request.setParkingAmount(new BigDecimal(52));
        System.out.println(request.toString());

        return UploadRecordRequest.packBytes(request);
    }

    //上传记录
    public static byte[] packUploadChargeRateRequest(){
        UploadChargeRateRequest request = new UploadChargeRateRequest();

        request.setGunNo(1);
        request.setOrderNo(34343434342L);
        request.setChargeModel(1);
        request.setCardNo("1234567812345678");
        request.setVin("12345123451234567");//17位
        request.setSoc(4);
        request.setStartTime("171101224021");
        request.setEndTime("171101225021");
        request.setTotalAmmeterDegree(new BigDecimal(34));
        request.setPointElectricQuantity(new BigDecimal(35));
        request.setPeakElectricQuantity(new BigDecimal(36));
        request.setOrdinaryElectricQuantity(new BigDecimal(37));
        request.setDipElectricQuantity(new BigDecimal(38));
        request.setTotalElectricAmount(new BigDecimal(39));
        request.setPointElectricAmount(new BigDecimal(40));
        request.setPeakElectricAmount(new BigDecimal(41));
        request.setOrdinaryElectricAmount(new BigDecimal(42));
        request.setDipElectricAmount(new BigDecimal(43));
        request.setSubscriptionAmount(new BigDecimal(44));
        request.setServiceAmount(new BigDecimal(45));
        request.setParkingAmount(new BigDecimal(46));

        System.out.println(request.toString());

        return UploadChargeRateRequest.packBytes(request);
    }
}
