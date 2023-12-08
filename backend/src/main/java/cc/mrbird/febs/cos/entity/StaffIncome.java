package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
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

    private String temp1;

    private String temp2;

    private String temp3;

    private String temp4;

    private String temp5;


}
