package com.piles.test;

import com.piles.util.Util;
import com.piles.common.util.BytesUtil;
import com.piles.setting.entity.UpdatePackageRequest;
import com.piles.setting.entity.UpdatePackageResponse;
import com.piles.setting.service.IRequestUpdatePackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class RequestUpdatePackageServiceImpl implements IRequestUpdatePackageService {
    @Override
    public UpdatePackageResponse getUpdatePackage(UpdatePackageRequest updatePackageRequest) {
        log.info( "进入请求升级包service" );
        int index = updatePackageRequest.getIndex();
        log.info( "请求升级包当前索引:{}", index );

        String pileNo = updatePackageRequest.getPileNo();
        log.info( "请求升级包当前桩号:{}", pileNo );
        UpdatePackageResponse response = new UpdatePackageResponse();
        Integer limit = Util.map.get( pileNo );
        if (null==limit){
            limit=512;
        }

        File file = new File( "/piletransfer/soft/AcOneV2.13.bin" );
        int totalIndex=getFileTotal( limit,file );
        if (file.exists()) {
            byte[] re=getFile( limit ,file,index);
            response.setResult( 0 );
            response.setCurrentIndex( index );
            response.setTotalIndex( totalIndex );
            response.setCurrentSegmentLen( re.length );
            response.setActualContent( re );
        }else if (index>totalIndex){
            response.setResult( 2);
            response.setCurrentIndex( index );
            response.setTotalIndex( totalIndex );
            response.setCurrentSegmentLen( 0 );
            response.setActualContent( null );
        }else {
            response.setResult( 1);
            response.setCurrentIndex( index );
            response.setTotalIndex( 0 );
            response.setCurrentSegmentLen( 0 );
            response.setActualContent( null );
        }


        log.info( "请求升级包请求数据:{}", response.toString() );
        return response;
    }

    public static void main(String[] args) {
        File file = new File( "c:/piletransfer/soft/AcOneV2.12.bin" );
        byte[] temp=getFile( 1024 ,file,113);
        for (byte b:temp){
            System.out.print(b);
        }
    }
    public static byte[] getFile(int limit,File file,int index){
        int byteCount = 0;

        byte[] temp = null;
        byte[] bytes = new byte[limit];
        int total=0;
        try {
            InputStream reader = new FileInputStream( file );
            while ((byteCount = reader.read( bytes )) != -1) {

                total++;
                if (total==index){
                    if (byteCount!=limit){
                        temp=new byte[byteCount];
                    }
                    temp= BytesUtil.copyBytes( bytes,0,byteCount );
                }
            }
            System.out.println(total);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
    public static int getFileTotal(int limit,File file){
        int byteCount = 0;

        byte[] bytes = new byte[limit];
        int total=0;
        try {
            InputStream reader = new FileInputStream( file );
            while ((byteCount = reader.read( bytes )) != -1) {
                total++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }
}
