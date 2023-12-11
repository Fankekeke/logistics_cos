package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DiscountInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IDiscountInfoService extends IService<DiscountInfo> {

    /**
     * 分页获取优惠券信息
     *
     * @param page         分页对象
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDiscountPage(Page<DiscountInfo> page, DiscountInfo discountInfo);
}
