package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.WithdrawInfo;
import cc.mrbird.febs.cos.dao.WithdrawInfoMapper;
import cc.mrbird.febs.cos.service.IWithdrawInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class WithdrawInfoServiceImpl extends ServiceImpl<WithdrawInfoMapper, WithdrawInfo> implements IWithdrawInfoService {

    /**
     * 分页获取提现记录信息
     *
     * @param page         分页对象
     * @param withdrawInfo 提现记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectWithdrawPage(Page<WithdrawInfo> page, WithdrawInfo withdrawInfo) {
        return baseMapper.selectWithdrawPage(page, withdrawInfo);
    }
}
