package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.LocationUtils;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final IUserInfoService userInfoService;

    private final IAddressInfoService addressInfoService;

    private final IDiscountInfoService discountInfoService;

    private final IWithdrawInfoService withdrawInfoService;

    private final IStaffInfoService staffInfoService;

    private final IStaffIncomeService staffIncomeService;

    private final IEvaluateInfoService evaluateInfoService;

    private final TemplateEngine templateEngine;

    private final IMailService mailService;

    private final IBulletinInfoService bulletinInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.selectOrderPage(page, orderInfo);
    }

    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public OrderInfo getPriceTotal(OrderInfo orderInfo) {
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));

        orderInfo.setUseDiscount(false);
        // 获取收货地址
        AddressInfo startAddressInfo = addressInfoService.getById(orderInfo.getStartAddressId());
        // 获取送货地址
        AddressInfo endAddressInfo = addressInfoService.getById(orderInfo.getEndAddressId());

        // 计算公里数与配送费用
        double distance = LocationUtils.getDistance(startAddressInfo.getLongitude().doubleValue(), startAddressInfo.getLatitude().doubleValue(), endAddressInfo.getLongitude().doubleValue(), endAddressInfo.getLatitude().doubleValue());
        orderInfo.setKilometre(NumberUtil.round(NumberUtil.div(new BigDecimal(distance), 1000), 2));

        // 基础价格30米
        orderInfo.setOrderPrice(new BigDecimal(30));
        // 每公里两米
        orderInfo.setDistributionPrice(NumberUtil.mul(orderInfo.getKilometre(), 5));
        orderInfo.setOrderPrice(NumberUtil.add(orderInfo.getOrderPrice(), orderInfo.getDistributionPrice()));
        orderInfo.setAfterOrderPrice(orderInfo.getOrderPrice());

        // 判断是有可用优惠券
        List<DiscountInfo> discountInfoList = discountInfoService.list(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getUserId, userInfo.getId()).eq(DiscountInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(discountInfoList)) {
            List<DiscountInfo> discount1s = discountInfoList.stream().filter(e -> "2".equals(e.getType())).collect(Collectors.toList());
            List<DiscountInfo> discount2s = discountInfoList.stream().filter(e -> "1".equals(e.getType()) && orderInfo.getOrderPrice().compareTo(e.getThreshold()) >= 0).collect(Collectors.toList());

            discount1s.addAll(discount2s);
            orderInfo.setDiscountInfos(discount1s);

            boolean discountCheck = (discountInfoList.stream().anyMatch(e -> "2".equals(e.getType())) || discountInfoList.stream().anyMatch(e -> "1".equals(e.getType()) && orderInfo.getOrderPrice().compareTo(e.getThreshold()) >= 0));
            orderInfo.setUseDiscount(discountCheck);
        }
        return orderInfo;
    }

    /**
     * 员工接单，并通过邮箱向用户发送接单通知。
     *
     * @param orderId 订单ID
     * @param staffId 员工ID
     * @return 如果接单操作成功则返回true，否则返回false
     */
    @Override
    public boolean checkOrder(Integer orderId, Integer staffId) {
        // 防止订单为空导致的异常
        OrderInfo orderInfo = this.getById(orderId);
        if (orderInfo == null) {
            // 可以记录日志或返回错误信息
            return false;
        }

        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        if (userInfo == null) {
            // 可以记录日志或返回错误信息
            return false;
        }

        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, staffId));
        if (staffInfo == null) {
            // 可以记录日志或返回错误信息
            return false;
        }

        // 邮箱通知
        if (StrUtil.isNotEmpty(userInfo.getMail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + "，您好，消费订单已被接单，联系方式：" + staffInfo.getPhone());
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getMail(), DateUtil.formatDate(new Date()) + "订单接单", emailContent);
        }

        return this.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getStaffIds, staffInfo.getId()).set(OrderInfo::getStatus, 2).eq(OrderInfo::getId, orderId));
    }

    /**
     * 查询收益记录详情
     *
     * @param incomeId 记录ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectIncomeDetail(Integer incomeId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("income", null);
                put("staff", null);
                put("order", null);
                put("startAddress", null);
                put("endAddress", null);
                put("user", null);
            }
        };

        // 收益信息
        StaffIncome staffIncome = staffIncomeService.getById(incomeId);
        result.put("income", staffIncome);
        // 订单信息
        OrderInfo orderInfo = this.getById(staffIncome.getOrderId());
        result.put("order", orderInfo);
        // 员工信息
        StaffInfo staffInfo = staffInfoService.getById(staffIncome.getStaffId());
        result.put("staff", staffInfo);
        // 用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        result.put("user", userInfo);

        // 开始配送地址
        AddressInfo startAddress = addressInfoService.getById(orderInfo.getStartAddressId());
        result.put("startAddress", startAddress);
        // 收货地址
        AddressInfo endAddress = addressInfoService.getById(orderInfo.getEndAddressId());
        result.put("endAddress", endAddress);

        return result;
    }

    /**
     * 提现记录详情
     *
     * @param withdrawId 提现记录ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectWithdrawDetail(Integer withdrawId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("withdraw", null);
                put("staff", null);
            }
        };
        // 提现记录
        WithdrawInfo withdrawInfo = withdrawInfoService.getById(withdrawId);
        result.put("withdraw", withdrawInfo);

        // 员工信息
        StaffInfo staffInfo = staffInfoService.getById(withdrawInfo.getStaffId());
        result.put("staff", staffInfo);
        return result;
    }

    /**
     * 添加订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public boolean saveOrder(OrderInfo orderInfo) {
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));

        orderInfo.setUserId(userInfo.getId());

        if (orderInfo.getDiscountId() != null) {
            discountInfoService.update(Wrappers.<DiscountInfo>lambdaUpdate().set(DiscountInfo::getStatus, "1").eq(DiscountInfo::getId, orderInfo.getDiscountId()));
        }

        // 添加订单
        orderInfo.setStatus("0");
        return this.save(orderInfo);
    }

    /**
     * 订单收货
     *
     * @param orderCode 订单编号
     * @param status    状态
     * @return 结果
     */
    @Override
    public boolean auditOrderFinish(String orderCode, Integer status) {
        OrderInfo order = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));

        UserInfo userInfo = userInfoService.getById(order.getUserId());

        // 保存订单收益
        StaffIncome staffIncome = new StaffIncome();
        staffIncome.setStaffId(Integer.parseInt(order.getStaffIds()));
        staffIncome.setOrderId(order.getId());
        staffIncome.setCreateDate(DateUtil.formatDateTime(new Date()));

        // 订单价格
        staffIncome.setIncome(NumberUtil.mul(new BigDecimal(30), 0.8));
        staffIncome.setDeliveryPrice(NumberUtil.mul(order.getDistributionPrice(), 0.8));
        staffIncome.setTotalPrice(NumberUtil.add(staffIncome.getIncome(), staffIncome.getDeliveryPrice()));
        staffIncomeService.save(staffIncome);

        // 更新员工收益
        StaffInfo staffInfo = staffInfoService.getById(order.getStaffIds());
        staffInfo.setPrice(NumberUtil.add(staffInfo.getPrice(), staffIncome.getTotalPrice()));
        staffInfoService.updateById(staffInfo);

        // 邮箱通知
        if (StrUtil.isNotEmpty(userInfo.getMail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + "，您好， 消费订单 " + order.getCode() + " 已完成，可进行评价");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getMail(), DateUtil.formatDate(new Date()) + "订单完成", emailContent);
        }
        return this.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getStatus, status).eq(OrderInfo::getCode, orderCode));
    }

    /**
     * 订单支付
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @Override
    public boolean orderPay(String orderCode) {
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        orderInfo.setStatus("1");
        orderInfo.setPayDate(DateUtil.formatDateTime(new Date()));

        // 用户添加积分
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        userInfo.setIntegral(NumberUtil.add(userInfo.getIntegral(), orderInfo.getIntegral()));

        // 用户下单发送邮件
        if (StrUtil.isNotEmpty(userInfo.getMail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + "，您好，消费订单 " + orderCode + "，已支付" + orderInfo.getAfterOrderPrice() + "元。");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getMail(), DateUtil.formatDate(new Date()) + "支付通知", emailContent);
        }

        // 更新用户积分
        userInfoService.updateById(userInfo);
        // 更新订单状态
        return this.updateById(orderInfo);
    }

    /**
     * 管理员审核提现申请
     *
     * @param withdrawInfo 提现记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditWithdraw(WithdrawInfo withdrawInfo) throws FebsException {
        // 员工信息
        StaffInfo staffInfo = staffInfoService.getById(withdrawInfo.getStaffId());

        if (withdrawInfo.getWithdrawPrice().compareTo(staffInfo.getPrice()) > 0) {
            throw new FebsException("员工余额不足");
        }
        // 更新员工余额
        if ("1".equals(withdrawInfo.getStatus())) {
            BigDecimal staffPrice = NumberUtil.sub(staffInfo.getPrice(), withdrawInfo.getWithdrawPrice());
            staffInfo.setPrice(staffPrice);
            staffInfoService.updateById(staffInfo);
        }
        return withdrawInfoService.updateById(withdrawInfo);
    }

    /**
     * 根据用户ID获取优惠券
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public List<DiscountInfo> selectDiscountByUser(Integer userId) {
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return Collections.emptyList();
        }

        return discountInfoService.list(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getUserId, userInfo.getId()).eq(DiscountInfo::getStatus, "0"));
    }

    /**
     * 获取ID获取评价详情
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectEvaluate(Integer id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user", null);
                put("order", null);
                put("startAddress", null);
                put("endAddress", null);
                put("staff", null);
                put("evaluate", null);
                put("discount", null);
            }
        };
        // 评价信息
        EvaluateInfo evaluateInfo = evaluateInfoService.getById(id);
        result.put("evaluate", evaluateInfo);

        // 订单信息
        OrderInfo orderInfo = this.getById(evaluateInfo.getOrderId());
        result.put("order", orderInfo);

        // 用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        result.put("user", userInfo);

        // 送货地址
        AddressInfo startAddress = addressInfoService.getById(orderInfo.getStartAddressId());
        result.put("startAddress", startAddress);
        // 收货地址
        AddressInfo endAddress = addressInfoService.getById(orderInfo.getEndAddressId());
        result.put("endAddress", endAddress);

        // 员工信息
        if (StrUtil.isNotEmpty(orderInfo.getStaffIds())) {
            StaffInfo staffInfo = staffInfoService.getById(orderInfo.getStaffIds());
            result.put("staff", staffInfo);
        }

        // 优惠券信息
        if (orderInfo.getDiscountId() != null) {
            DiscountInfo discountInfo = discountInfoService.getById(orderInfo.getDiscountId());
            result.put("discount", discountInfo);
        }
        return result;
    }

    /**
     * 获取ID获取订单详情
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderDetail(Integer id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user", null);
                put("order", null);
                put("startAddress", null);
                put("endAddress", null);
                put("staff", null);
                put("evaluate", null);
                put("discount", null);
            }
        };
        // 订单信息
        OrderInfo orderInfo = this.getById(id);
        result.put("order", orderInfo);

        // 用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        result.put("user", userInfo);

        // 送货地址
        AddressInfo startAddress = addressInfoService.getById(orderInfo.getStartAddressId());
        result.put("startAddress", startAddress);
        // 收货地址
        AddressInfo endAddress = addressInfoService.getById(orderInfo.getEndAddressId());
        result.put("endAddress", endAddress);

        // 员工信息
        if (StrUtil.isNotEmpty(orderInfo.getStaffIds())) {
            StaffInfo staffInfo = staffInfoService.getById(orderInfo.getStaffIds());
            result.put("staff", staffInfo);
        }

        // 评价信息
        EvaluateInfo evaluateInfo = evaluateInfoService.getOne(Wrappers.<EvaluateInfo>lambdaQuery().eq(EvaluateInfo::getOrderId, orderInfo));
        result.put("evaluate", evaluateInfo);

        // 优惠券信息
        if (orderInfo.getDiscountId() != null) {
            DiscountInfo discountInfo = discountInfoService.getById(orderInfo.getDiscountId());
            result.put("discount", discountInfo);
        }
        return result;
    }

    /**
     * 员工获取统计信息
     *
     * @param userId 员工ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectHomeDataByMerchant(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("orderNum", 0);
                put("orderPrice", 0);
                put("staffNum", 0);
                put("memberNum", 0);
            }
        };

        // 员工信息
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getUserId, userId));

        List<OrderInfo> orderInfoList = this.list(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStaffIds, staffInfo.getId()).ne(OrderInfo::getStatus, "0"));

        BigDecimal totalPrice = orderInfoList.stream().map(OrderInfo::getAfterOrderPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("orderNum", orderInfoList.size());
        result.put("orderPrice", totalPrice);
        result.put("staffNum", staffInfoService.count(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getStatus, "1")));
        result.put("memberNum", userInfoService.count());

        // 本月订单数量
        List<OrderInfo> orderMonthList = baseMapper.selectOrderByMonth(staffInfo.getId());
        result.put("monthOrderNum", CollectionUtil.isEmpty(orderMonthList) ? 0 : orderMonthList.size());
        BigDecimal orderPrice = orderMonthList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderTotal", orderPrice);

        // 本年订单数量
        List<OrderInfo> orderYearList = baseMapper.selectOrderByYear(staffInfo.getId());
        result.put("yearOrderNum", CollectionUtil.isEmpty(orderYearList) ? 0 : orderYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderTotal", orderYearPrice);

        // 近十天销售订单统计
        result.put("orderNumDayList", baseMapper.selectOrderNumWithinDays(staffInfo.getId()));
        // 近十天销售金额统计
        result.put("priceDayList", baseMapper.selectOrderPriceWithinDays(staffInfo.getId()));
        // todo 销售菜品统计
        // result.put("orderDrugType", baseMapper.selectOrderDishesType(merchantInfo.getId()));
        // 公告信息
        result.put("bulletinInfoList", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        return result;
    }

    /**
     * 管理员获取主页统计数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeDataByAdmin() {
// 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("orderNum", 0);
                put("orderPrice", 0);
                put("staffNum", 0);
                put("merchantNum", 0);
            }
        };

        List<OrderInfo> orderInfoList = this.list(Wrappers.<OrderInfo>lambdaQuery().ne(OrderInfo::getStatus, "0"));

        BigDecimal totalPrice = orderInfoList.stream().map(OrderInfo::getAfterOrderPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("orderNum", orderInfoList.size());
        result.put("orderPrice", totalPrice);
        result.put("staffNum", staffInfoService.count(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getStatus, "1")));
        result.put("merchantNum", userInfoService.count());

        // 本月订单数量
        List<OrderInfo> orderMonthList = baseMapper.selectOrderByMonth(null);
        result.put("monthOrderNum", CollectionUtil.isEmpty(orderMonthList) ? 0 : orderMonthList.size());
        BigDecimal orderPrice = orderMonthList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderTotal", orderPrice);

        // 本年订单数量
        List<OrderInfo> orderYearList = baseMapper.selectOrderByYear(null);
        result.put("yearOrderNum", CollectionUtil.isEmpty(orderYearList) ? 0 : orderYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(OrderInfo::getAfterOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderTotal", orderYearPrice);

        // 近十天销售订单统计
        result.put("orderNumDayList", baseMapper.selectOrderNumWithinDays(null));
        // 近十天销售金额统计
        result.put("priceDayList", baseMapper.selectOrderPriceWithinDays(null));
        // 销售菜品统计
        // result.put("orderDrugType", baseMapper.selectOrderDishesType(null));
        // 公告信息
        result.put("bulletinInfoList", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        return result;
    }
}
