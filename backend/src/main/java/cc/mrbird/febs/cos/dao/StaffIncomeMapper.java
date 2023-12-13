package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.StaffIncome;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface StaffIncomeMapper extends BaseMapper<StaffIncome> {

    /**
     * 分页获取收益信息
     *
     * @param page        分页对象
     * @param staffIncome 公告信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStaffIncomePage(Page<StaffIncome> page, @Param("staffIncome") StaffIncome staffIncome);
}
