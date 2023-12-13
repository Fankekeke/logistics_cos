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
 * 优惠券管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DiscountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String code;

    /**
     * 满减金额
     */
    private BigDecimal discountPrice;

    /**
     * 门槛金额
     */
    private BigDecimal threshold;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 折扣
     */
    private BigDecimal rebate;

    /**
     * 类型（1.满减 2.折扣）
     */
    private String type;

    /**
     * 备注
     */
    private String content;

    /**
     * 发放时间
     */
    private String createDate;

    /**
     * 状态（0.未使用 1.已使用）
     */
    private String status;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

}
