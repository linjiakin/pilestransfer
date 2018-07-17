package com.piles.test;

import com.piles.setting.entity.QrSetRequest;
import com.piles.setting.service.IQrSetService;
import org.springframework.stereotype.Service;

@Service
public class QrSetServiceImpl implements IQrSetService {
    @Override
    public String qrSet(QrSetRequest qrSetRequest) {
        return qrSetRequest.getPileNo();
    }
}
