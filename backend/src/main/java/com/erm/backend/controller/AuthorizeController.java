package com.erm.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Authorize;
import com.erm.backend.entity.Record;
import com.erm.backend.service.AuthorizeService;
import com.erm.backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/authorize")
public class AuthorizeController {

    @Autowired
    private AuthorizeService authorizeService;

    @Autowired
    private RecordService recordService;

    /** 查询患者的所有授权记录（含历史） */
    @GetMapping("/list/{patientId}")
    public List<Authorize> list(@PathVariable Integer patientId) {
        QueryWrapper<Authorize> qw = new QueryWrapper<>();
        qw.eq("patient_id", patientId);
        return authorizeService.list(qw);
    }

    /** 医生申请授权 */
    @PostMapping("/apply")
    public ResultVO apply(@RequestBody java.util.Map<String, Integer> params) {
        ResultVO result = new ResultVO();
        Integer doctorId = params.get("doctorId");
        Integer recordId = params.get("recordId");

        // 通过 recordId 查出患者ID
        Record record = recordService.getById(recordId);
        if (record == null) {
            result.setCode(-1); result.setData("record_not_found"); return result;
        }
        Integer patientId = record.getPatientId();

        // 检查是否已申请
        QueryWrapper<Authorize> qw = new QueryWrapper<>();
        qw.eq("doctor_id", doctorId);
        qw.eq("patient_id", patientId);
        qw.eq("record_id", recordId);
        List<Authorize> exists = authorizeService.list(qw);
        if (!exists.isEmpty()) {
            result.setCode(-1); result.setData("already_applied"); return result;
        }

        Authorize auth = new Authorize();
        auth.setDoctorId(doctorId);
        auth.setPatientId(patientId);
        auth.setRecordId(recordId);
        auth.setAuthTime(LocalDateTime.now());
        auth.setStatus(0); // 0=待审批
        authorizeService.save(auth);
        result.setCode(0);
        result.setData("success");
        return result;
    }

    /** 患者同意授权 */
    @PostMapping("/approve/{id}")
    public ResultVO approve(@PathVariable Integer id) {
        ResultVO result = new ResultVO();
        Authorize auth = authorizeService.getById(id);
        if (auth == null) {
            result.setCode(-1); result.setData("not_found"); return result;
        }
        auth.setStatus(1); // 1=有效
        authorizeService.updateById(auth);
        result.setCode(0);
        result.setData("success");
        return result;
    }

    /** 患者拒绝授权 */
    @PostMapping("/reject/{id}")
    public ResultVO reject(@PathVariable Integer id) {
        ResultVO result = new ResultVO();
        Authorize auth = authorizeService.getById(id);
        if (auth == null) {
            result.setCode(-1); result.setData("not_found"); return result;
        }
        auth.setStatus(2); // 2=已拒绝
        authorizeService.updateById(auth);
        result.setCode(0);
        result.setData("success");
        return result;
    }

    /** 查询患者的待审批授权 */
    @GetMapping("/pending/{patientId}")
    public List<Authorize> pending(@PathVariable Integer patientId) {
        QueryWrapper<Authorize> qw = new QueryWrapper<>();
        qw.eq("patient_id", patientId);
        qw.eq("status", 0);
        return authorizeService.list(qw);
    }

    /** 查询医生已获得授权 */
    @GetMapping("/myauth/{doctorId}")
    public List<Authorize> myAuth(@PathVariable Integer doctorId) {
        QueryWrapper<Authorize> qw = new QueryWrapper<>();
        qw.eq("doctor_id", doctorId);
        qw.eq("status", 1);
        return authorizeService.list(qw);
    }
}
