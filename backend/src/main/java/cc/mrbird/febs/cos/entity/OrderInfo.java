package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 总价格
     */
    private BigDecimal total;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 分配员工
     */
    private String statfIds;

    /**
     * 下单时间
     */
    private LocalDateTime payDate;

    /**
     * 物品类型
     */
    private String goodsType;

    /**
     * 物品图片
     */
    private String images;

    /**
     * 物品重量
     */
    private BigDecimal weight;

    /**
     * 物品高度
     */
    private BigDecimal height;

    /**
     * 物品宽度
     */
    private BigDecimal width;

    /**
     * 开始地址
     */
    private Integer startAddressId;

    /**
     * 送货地址
     */
    private Integer endAddressId;

    /**
     * 配送时间
     */
    private LocalDateTime deliveryDate;

    /**
     * 物流信息
     */
    private String logisticsInfo;

    /**
     * 完成时间
     */
    private LocalDateTime finishDate;


}
