package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DiscountInfo;
import cc.mrbird.febs.cos.dao.DiscountInfoMapper;
import cc.mrbird.febs.cos.service.IDiscountInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class DiscountInfoServiceImpl extends ServiceImpl<DiscountInfoMapper, DiscountInfo> implements IDiscountInfoService {

    /**
     * 分页获取优惠券信息
     *
     * @param page         分页对象
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDiscountPage(Page<DiscountInfo> page, DiscountInfo discountInfo) {
        return baseMapper.selectDiscountPage(page, discountInfo);
    }
}
