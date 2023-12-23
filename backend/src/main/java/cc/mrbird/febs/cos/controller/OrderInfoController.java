package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.WithdrawInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("/cos/order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoController {

    private final IOrderInfoService orderInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderInfo> page, OrderInfo orderInfo) {
        return R.ok(orderInfoService.selectOrderPage(page, orderInfo));
    }

    /**
     * 员工接单
     *
     * @param orderId 订单ID
     * @param staffId 员工ID
     * @return 结果
     */
    @GetMapping("/checkOrder")
    public R checkOrder(@RequestParam("orderId") Integer orderId, @RequestParam("staffId") Integer staffId) {
        return R.ok(orderInfoService.checkOrder(orderId, staffId));
    }

    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/getPriceTotal")
    public R getPriceTotal(OrderInfo orderInfo) {
        return R.ok(orderInfoService.getPriceTotal(orderInfo));
    }

    /**
     * 查询收益记录详情
     *
     * @param id 记录ID
     * @return 结果
     */
    @GetMapping("/income/{id}")
    public R selectIncomeDetail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.selectIncomeDetail(id));
    }

    /**
     * 提现记录详情
     *
     * @param withdrawId 提现记录ID
     * @return 结果
     */
    @GetMapping("/withdraw/{id}")
    public R selectWithdrawDetail(@PathVariable("id") Integer withdrawId) {
        return R.ok(orderInfoService.selectWithdrawDetail(withdrawId));
    }

    /**
     * 订单收货
     *
     * @param orderCode 订单编号
     * @param status    状态
     * @return 结果
     */
    @GetMapping("/auditOrderFinish")
    public R auditOrderFinish(@RequestParam("orderCode") String orderCode, @RequestParam("status") Integer status) {
        return R.ok(orderInfoService.auditOrderFinish(orderCode, status));
    }

    /**
     * 订单支付
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @GetMapping("/orderPay")
    public R orderPay(@RequestParam("orderCode") String orderCode) {
        return R.ok(orderInfoService.orderPay(orderCode));
    }

    /**
     * 管理员审核提现申请
     *
     * @param withdrawInfo 提现记录
     * @return 结果
     */
    @PostMapping("/auditWithdraw")
    public R auditWithdraw(WithdrawInfo withdrawInfo) throws FebsException {
        return R.ok(orderInfoService.auditWithdraw(withdrawInfo));
    }

    /**
     * 根据用户ID获取优惠券
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/discount/{userId}")
    public R selectDiscountByUser(@PathVariable("userId") Integer userId) {
        return R.ok(orderInfoService.selectDiscountByUser(userId));
    }

    /**
     * 获取ID获取订单详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.selectOrderDetail(id));
    }

    /**
     * 获取ID获取评价详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/evaluate/{id}")
    public R evaluateDetail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.selectEvaluate(id));
    }

    /**
     * 获取订单信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderInfoService.list());
    }

    /**
     * 管理员获取主页统计数据
     *
     * @return 结果
     */
    @GetMapping("/admin/homeData")
    public R homeDataByAdmin() {
        return R.ok(orderInfoService.homeDataByAdmin());
    }

    /**
     * 员工获取统计信息
     *
     * @param userId 员工ID
     * @return 结果
     */
    @GetMapping("/homeData")
    public R selectHomeDataByMerchant(@RequestParam("userId") Integer userId) {
        return R.ok(orderInfoService.selectHomeDataByMerchant(userId));
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderInfo orderInfo) {
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 修改订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderInfo orderInfo) {
        return R.ok(orderInfoService.updateById(orderInfo));
    }

    /**
     * 删除订单信息
     *
     * @param ids ids
     * @return 订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderInfoService.removeByIds(ids));
    }
}
