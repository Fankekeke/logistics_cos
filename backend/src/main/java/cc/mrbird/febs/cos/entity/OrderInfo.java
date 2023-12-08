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

    private String temp1;

    private String temp2;

    private String temp3;

    private String temp4;

    private String temp5;

    private String temp6;

    private String temp7;

    private String temp8;

    private String temp9;

    private String temp10;


}
