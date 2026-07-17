package com.erm.backend.service;
import java.util.Map;


import com.erm.backend.VO.RecordVO;
import com.erm.backend.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RecordService extends IService<Record> {
    Map<String, Object> searchRecords(String name, String phone, String office, int pageNo, int pageSize);
    RecordVO getRecordDetail(Integer id);
    void createFullRecord(Map<String, Object> params);
}