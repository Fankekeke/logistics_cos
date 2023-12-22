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
 * 员工管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StaffInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 员工编号
     */
    private String code;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 性别（1.男 2.女）
     */
    private Integer sex;

    /**
     * 状态（1.在职 2.离职）
     */
    private Integer status;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 照片
     */
    private String images;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 所属账户
     */
    private Long userId;

    /**
     * 余额
     */
    private BigDecimal price;

    @TableField(exist = false)
    private Integer merchantId;

}
