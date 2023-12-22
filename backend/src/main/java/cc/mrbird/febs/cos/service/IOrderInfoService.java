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
     * 员工接单
     *
     * @param orderId 订单ID
     * @param staffId 员工ID
     * @return 结果
     */
    boolean checkOrder(Integer orderId, Integer staffId);

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
     * 添加订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    boolean saveOrder(OrderInfo orderInfo);

    /**
     * 订单收货
     *
     * @param orderCode 订单编号
     * @param status    状态
     * @return 结果
     */
    boolean auditOrderFinish(String orderCode, Integer status);

    /**
     * 订单支付
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    boolean orderPay(String orderCode);

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

    /**
     * 获取ID获取评价详情
     *
     * @param id 主键
     * @return 结果
     */
    LinkedHashMap<String, Object> selectEvaluate(Integer id);

    /**
     * 获取ID获取订单详情
     *
     * @param id 主键
     * @return 结果
     */
    LinkedHashMap<String, Object> selectOrderDetail(Integer id);

    /**
     * 员工获取统计信息
     *
     * @param userId 员工ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectHomeDataByMerchant(Integer userId);

    /**
     * 管理员获取主页统计数据
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> homeDataByAdmin();
}
