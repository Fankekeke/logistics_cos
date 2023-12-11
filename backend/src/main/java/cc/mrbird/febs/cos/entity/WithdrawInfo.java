package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
    private String createDate;

    /**
     * 审核状态（0.待审核 1.通过 2.驳回）
     */
    private String status;

    /**
     * 员工姓名
     */
    @TableField(exist = false)
    private String staffName;

    /**
     * 员工编号
     */
    @TableField(exist = false)
    private String code;
}
