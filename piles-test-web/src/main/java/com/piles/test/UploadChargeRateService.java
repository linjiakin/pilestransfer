package com.piles.test;

import com.piles.record.entity.UploadChargeRateRequest;
import com.piles.record.service.IUploadChargeRateService;
import org.springframework.stereotype.Service;

@Service
public class UploadChargeRateService implements IUploadChargeRateService {
    @Override
    public boolean uploadChargeRate(UploadChargeRateRequest uploadChargeRateRequest) {
        return false;
    }
}
