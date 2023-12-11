package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.WithdrawInfo;
import cc.mrbird.febs.cos.service.IWithdrawInfoService;
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
@RequestMapping("/cos/withdraw-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WithdrawInfoController {

    private final IWithdrawInfoService withdrawInfoService;

    /**
     * 分页获取提现记录信息
     *
     * @param page         分页对象
     * @param withdrawInfo 提现记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<WithdrawInfo> page, WithdrawInfo withdrawInfo) {
        return R.ok(withdrawInfoService.selectWithdrawPage(page, withdrawInfo));
    }

    /**
     * 获取ID获取提现记录详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(withdrawInfoService.getById(id));
    }

    /**
     * 获取提现记录信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(withdrawInfoService.list());
    }

    /**
     * 新增提现记录信息
     *
     * @param withdrawInfo 提现记录信息
     * @return 结果
     */
    @PostMapping
    public R save(WithdrawInfo withdrawInfo) {
        withdrawInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(withdrawInfoService.save(withdrawInfo));
    }

    /**
     * 修改提现记录信息
     *
     * @param withdrawInfo 提现记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(WithdrawInfo withdrawInfo) {
        return R.ok(withdrawInfoService.updateById(withdrawInfo));
    }

    /**
     * 删除提现记录信息
     *
     * @param ids ids
     * @return 提现记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(withdrawInfoService.removeByIds(ids));
    }
}
