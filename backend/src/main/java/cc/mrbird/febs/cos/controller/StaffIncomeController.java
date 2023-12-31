package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.StaffIncome;
import cc.mrbird.febs.cos.service.IStaffIncomeService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/staff-income")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffIncomeController {

    private final IStaffIncomeService staffIncomeService;

    /**
     * 分页获取公告信息
     *
     * @param page        分页对象
     * @param staffIncome 公告信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<StaffIncome> page, StaffIncome staffIncome) {
        return R.ok(staffIncomeService.selectStaffIncomePage(page, staffIncome));
    }

    /**
     * 获取ID获取公告详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(staffIncomeService.getById(id));
    }

    /**
     * 获取公告信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(staffIncomeService.list());
    }

    /**
     * 新增公告信息
     *
     * @param staffIncome 公告信息
     * @return 结果
     */
    @PostMapping
    public R save(StaffIncome staffIncome) {
        staffIncome.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(staffIncomeService.save(staffIncome));
    }

    /**
     * 修改公告信息
     *
     * @param staffIncome 公告信息
     * @return 结果
     */
    @PutMapping
    public R edit(StaffIncome staffIncome) {
        return R.ok(staffIncomeService.updateById(staffIncome));
    }

    /**
     * 删除公告信息
     *
     * @param ids ids
     * @return 公告信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(staffIncomeService.removeByIds(ids));
    }
}
