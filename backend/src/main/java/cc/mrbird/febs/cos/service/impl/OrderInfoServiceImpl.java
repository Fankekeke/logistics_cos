package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.LocationUtils;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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

        // 每公里两米
        orderInfo.setDistributionPrice(NumberUtil.mul(orderInfo.getKilometre(), 2));
        orderInfo.setOrderPrice(NumberUtil.add(orderInfo.getOrderPrice(), orderInfo.getDistributionPrice()));

        // 判断是有可用优惠券
        List<DiscountInfo> discountInfoList = discountInfoService.list(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getUserId, userInfo.getId()).eq(DiscountInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(discountInfoList)) {
            boolean discountCheck = (discountInfoList.stream().anyMatch(e -> "2".equals(e.getType())) || discountInfoList.stream().anyMatch(e -> "1".equals(e.getType()) && orderInfo.getOrderPrice().compareTo(e.getThreshold()) >= 0));
            orderInfo.setUseDiscount(discountCheck);
        }
        return orderInfo;
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
        return null;
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
}
