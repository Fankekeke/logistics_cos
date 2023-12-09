package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 员工收益
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StaffIncome implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    private Integer staffId;

    /**
     * 所属订单
     */
    private Integer orderId;

    /**
     * 订单收益
     */
    private BigDecimal income;

    /**
     * 配送费用
     */
    private BigDecimal deliveryPrice;

    /**
     * 总收益
     */
    private BigDecimal totalPrice;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


}
