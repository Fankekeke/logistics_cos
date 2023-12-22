package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, @Param("orderInfo") OrderInfo orderInfo);

    /**
     * 本月订单信息
     *
     * @param staffId 员工ID
     * @return 结果
     */
    List<OrderInfo> selectOrderByMonth(@Param("staffId") Integer staffId);

    /**
     * 本年订单信息
     *
     * @param staffId 员工ID
     * @return 结果
     */
    List<OrderInfo> selectOrderByYear(@Param("staffId") Integer staffId);

    /**
     * 十天内订单数量统计
     *
     * @param staffId 员工ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderNumWithinDays(@Param("staffId") Integer staffId);

    /**
     * 十天内订单收益统计
     *
     * @param staffId 员工ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderPriceWithinDays(@Param("staffId") Integer staffId);
}
