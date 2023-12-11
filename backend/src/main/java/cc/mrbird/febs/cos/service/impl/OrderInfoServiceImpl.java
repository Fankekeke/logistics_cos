package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.utils.LocationUtils;
import cc.mrbird.febs.cos.entity.AddressInfo;
import cc.mrbird.febs.cos.entity.DiscountInfo;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IAddressInfoService;
import cc.mrbird.febs.cos.service.IDiscountInfoService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        List<DiscountInfo> discountInfoList = discountInfoService.list(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getUserId, userInfo.getId()));
        if (CollectionUtil.isNotEmpty(discountInfoList)) {
            if (discountInfoList.stream().anyMatch(e -> "2".equals(e.getType()))) {
                orderInfo.setUseDiscount(true);
            } else if (discountInfoList.stream().anyMatch(e -> "1".equals(e.getType()) && orderInfo.getOrderPrice().compareTo(e.getThreshold()) >= 0)) {
                orderInfo.setUseDiscount(true);
            }
        }
        return orderInfo;
    }
}
