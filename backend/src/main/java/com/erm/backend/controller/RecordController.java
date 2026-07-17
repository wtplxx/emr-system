package com.erm.backend.controller;

import com.erm.backend.VO.RecordVO;
import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Record;
import com.erm.backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/search")
    public Map<String, Object> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String office,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return recordService.searchRecords(name, phone, office, pageNo, pageSize);
    }

    /** 获取待确认档案数量 */
    @GetMapping("/count")
    public Map<String, Object> count(@RequestParam(defaultValue = "0") int affirm) {
        Map<String, Object> result = new java.util.HashMap<>();
        long count = recordService.count(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Record>().eq("affirm", affirm));
        result.put("count", count);
        return result;
    }

    @GetMapping("/detail/{id}")
    public RecordVO detail(@PathVariable Integer id) {
        return recordService.getRecordDetail(id);
    }

    /** 医生确认档案 */
    @PostMapping("/confirm/{id}")
    public ResultVO confirm(@PathVariable Integer id) {
        ResultVO result = new ResultVO();
        Record record = recordService.getById(id);
        if (record == null) {
            result.setCode(-1);
            result.setData("档案不存在");
            return result;
        }
        record.setAffirm(1);
        recordService.updateById(record);
        result.setCode(0);
        result.setData("success");
        return result;
    }

    /** 创建档案（包含患者 + 病历记录） */
    @PostMapping("/create")
    public ResultVO create(@RequestBody Map<String, Object> params) {
        ResultVO result = new ResultVO();

        // 数据校验
        if (params.get("name") == null || ((String) params.get("name")).trim().isEmpty()) {
            result.setCode(-1); result.setData("name_required"); return result;
        }
        if (params.get("phone") == null || ((String) params.get("phone")).toString().trim().isEmpty()) {
            result.setCode(-1); result.setData("phone_required"); return result;
        }
        if (params.get("age") == null) {
            result.setCode(-1); result.setData("age_required"); return result;
        }
        if (params.get("sex") == null) {
            result.setCode(-1); result.setData("sex_required"); return result;
        }

        Integer age;
        Integer sex;
        try {
            age = Integer.parseInt(params.get("age").toString());
            sex = Integer.parseInt(params.get("sex").toString());
        } catch (NumberFormatException e) {
            result.setCode(-1); result.setData("invalid_number"); return result;
        }

        if (age < 0 || age > 150) {
            result.setCode(-1); result.setData("age_invalid"); return result;
        }
        if (sex != 1 && sex != 2) {
            result.setCode(-1); result.setData("sex_invalid"); return result;
        }
        if (params.get("doctorId") == null) {
            result.setCode(-1); result.setData("doctor_required"); return result;
        }

        try {
            recordService.createFullRecord(params);
            result.setCode(0);
            result.setData("success");
        } catch (Exception e) {
            result.setCode(-1);
            result.setData("创建失败: " + e.getMessage());
        }
        return result;
    }
}
