package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.DiscountInfo;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.WithdrawInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo);

    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    OrderInfo getPriceTotal(OrderInfo orderInfo);

    /**
     * 查询收益记录详情
     *
     * @param incomeId 记录ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectIncomeDetail(Integer incomeId);

    /**
     * 提现记录详情
     *
     * @param withdrawId 提现记录ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectWithdrawDetail(Integer withdrawId);

    /**
     * 管理员审核提现申请
     *
     * @param withdrawInfo 提现记录
     * @return 结果
     */
    boolean auditWithdraw(WithdrawInfo withdrawInfo) throws FebsException;

    /**
     * 根据用户ID获取优惠券
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<DiscountInfo> selectDiscountByUser(Integer userId);
}
