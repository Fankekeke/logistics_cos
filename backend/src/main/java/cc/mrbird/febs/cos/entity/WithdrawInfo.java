package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提现记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WithdrawInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    private Integer staffId;

    /**
     * 提现金额
     */
    private BigDecimal withdrawPrice;

    /**
     * 账户余额
     */
    private BigDecimal accountPrice;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


}
