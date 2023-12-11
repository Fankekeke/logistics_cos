package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.StaffIncome;
import cc.mrbird.febs.cos.dao.StaffIncomeMapper;
import cc.mrbird.febs.cos.service.IStaffIncomeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class StaffIncomeServiceImpl extends ServiceImpl<StaffIncomeMapper, StaffIncome> implements IStaffIncomeService {

    /**
     * 分页获取公告信息
     *
     * @param page        分页对象
     * @param staffIncome 公告信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStaffIncomePage(Page<StaffIncome> page, StaffIncome staffIncome) {
        return baseMapper.selectStaffIncomePage(page, staffIncome);
    }
}
