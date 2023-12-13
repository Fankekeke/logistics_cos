package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DiscountInfo;
import cc.mrbird.febs.cos.service.IDiscountInfoService;
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
@RequestMapping("/cos/discount-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscountInfoController {

    private final IDiscountInfoService discountInfoService;

    /**
     * 分页获取优惠券信息
     *
     * @param page         分页对象
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DiscountInfo> page, DiscountInfo discountInfo) {
        return R.ok(discountInfoService.selectDiscountPage(page, discountInfo));
    }

    /**
     * 获取ID获取优惠券详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(discountInfoService.getById(id));
    }

    /**
     * 获取优惠券信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(discountInfoService.list());
    }

    /**
     * 新增优惠券信息
     *
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @PostMapping
    public R save(DiscountInfo discountInfo) {
        discountInfo.setCode("DC-" + System.currentTimeMillis());
        discountInfo.setStatus("0");
        discountInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(discountInfoService.save(discountInfo));
    }

    /**
     * 修改优惠券信息
     *
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @PutMapping
    public R edit(DiscountInfo discountInfo) {
        return R.ok(discountInfoService.updateById(discountInfo));
    }

    /**
     * 删除优惠券信息
     *
     * @param ids ids
     * @return 优惠券信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(discountInfoService.removeByIds(ids));
    }
}
