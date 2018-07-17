package com.piles.record.service.impl;

import com.piles.record.domain.UploadRecord;
import com.piles.record.entity.UploadRecordRequest;
import com.piles.record.service.IUploadRecordService;
import org.springframework.stereotype.Service;

@Service
public class UploadRecordService implements IUploadRecordService {


    @Override
    public boolean uploadRecord(UploadRecord uploadRecord) {
        return false;
    }
}
