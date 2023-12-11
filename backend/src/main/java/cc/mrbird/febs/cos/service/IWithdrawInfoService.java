package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.WithdrawInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IWithdrawInfoService extends IService<WithdrawInfo> {

    /**
     * 分页获取提现记录信息
     *
     * @param page         分页对象
     * @param withdrawInfo 提现记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectWithdrawPage(Page<WithdrawInfo> page, WithdrawInfo withdrawInfo);
}
