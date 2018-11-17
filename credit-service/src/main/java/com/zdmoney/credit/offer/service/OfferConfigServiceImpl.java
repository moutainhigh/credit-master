package com.zdmoney.credit.offer.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.DebitTypeEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.offer.dao.pub.IOfferConfigDao;
import com.zdmoney.credit.offer.domain.OfferConfig;
import com.zdmoney.credit.offer.service.pub.IOfferConfigService;

@Service
public class OfferConfigServiceImpl implements IOfferConfigService {

    @Autowired
    private IOfferConfigDao offerConfigDao;

    public OfferConfig insert(OfferConfig vo) {
        return offerConfigDao.insert(vo);
    }

    public int update(OfferConfig vo) {
        return offerConfigDao.update(vo);
    }

    public List<OfferConfig> findListByVo(OfferConfig vo) {
        return offerConfigDao.findListByVo(vo);
    }
    
    /**
     * 是否生成正常、提前结清、提前扣款报盘
     * @return
     */
    public boolean isCanCreateNormalOffer() {
        List<OfferConfig> offerConfigList = this.queryOfferConfigList();
        if (CollectionUtils.isNotEmpty(offerConfigList)) {
            for (OfferConfig offerConfig : offerConfigList) {
                if (DebitTypeEnum.P0_A.getValue().equals(offerConfig.getDebitType())
                    || DebitTypeEnum.P0_B.getValue().equals(offerConfig.getDebitType())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 是否生成逾期扣款报盘
     * @return
     */
    public boolean isCanCreateOverdueOffer() {
        List<OfferConfig> offerConfigList = this.queryOfferConfigList();
        if (CollectionUtils.isNotEmpty(offerConfigList)) {
            for (OfferConfig offerConfig : offerConfigList) {
                if (!DebitTypeEnum.P0_A.getValue().equals(offerConfig.getDebitType())
                    && !DebitTypeEnum.P0_B.getValue().equals(offerConfig.getDebitType())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 查询当天发送报盘的所有不重复划扣类型
     */
    public List<String> queryAllDayDebitTypeList() {
        List<String> debitTypeList = new ArrayList<String>();
        List<OfferConfig> offerConfigList = this.queryOfferConfigList();
        if (CollectionUtils.isNotEmpty(offerConfigList)) {
            for (OfferConfig offerConfig : offerConfigList) {
                if(!debitTypeList.contains(offerConfig.getDebitType())){
                    debitTypeList.add(offerConfig.getDebitType());
                }
            }
        }
        return debitTypeList;
    }
    
    /**
     * 是否实时划扣
     * @return
     */
    public boolean isCanDebit(){
        List<OfferConfig> offerConfigList = this.queryOfferConfigList();
        if (CollectionUtils.isNotEmpty(offerConfigList)) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            for (OfferConfig offerConfig : offerConfigList) {
                if (hour == Integer.valueOf(offerConfig.getExecTime().substring(0, 2))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 查询当次发送报盘的划扣类型
     */
    public List<String> queryDebitTypeList() {
        List<String> debitTypeList = new ArrayList<String>();
        List<OfferConfig> offerConfigList = this.queryOfferConfigList();
        if (CollectionUtils.isNotEmpty(offerConfigList)) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            for (OfferConfig offerConfig : offerConfigList) {
                if (hour == Integer.valueOf(offerConfig.getExecTime().substring(0, 2))) {
                    debitTypeList.add(offerConfig.getDebitType());
                }
            }
        }
        return debitTypeList;
    }
    
    /**
     * 查询当天报盘的所有划扣时间（除重）
     * @return
     */
    public List<String> queryDebitTimeList() {
        List<String> debitTimeList = new ArrayList<String>();
        List<OfferConfig> offerConfigList = this.queryOfferConfigList();
        if (CollectionUtils.isNotEmpty(offerConfigList)) {
            for (OfferConfig offerConfig : offerConfigList) {
                if(!debitTimeList.contains(offerConfig.getExecTime())){
                    debitTimeList.add(offerConfig.getExecTime());
                }
            }
        }
        return debitTimeList;
    }
    
    /**
     * 查询当次发送报盘的划扣类型和划扣合同来源
     */
    public Map<String,Object> queryDebitParams() {
        Map<String,Object> result = new HashMap<String,Object>();
        // 当次划扣类型
        List<String> debitTypeList = new ArrayList<String>();
        // 当次划扣合同来源
        List<String> fundsSourcesList = new ArrayList<String>();
        // 查询当天的报盘配置信息
        List<OfferConfig> offerConfigList = this.queryOfferConfigList();
        if (CollectionUtils.isNotEmpty(offerConfigList)) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            for (OfferConfig offerConfig : offerConfigList) {
                if (hour == Integer.valueOf(offerConfig.getExecTime().substring(0, 2))) {
                    debitTypeList.add(offerConfig.getDebitType());
                    if(Strings.isNotEmpty(offerConfig.getFundsSources())){
                        for(String fundsSource : offerConfig.getFundsSources().split(",")){
                            if(!fundsSourcesList.contains(fundsSource)){
                                fundsSourcesList.add(fundsSource);
                            }
                        }
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(debitTypeList)){
            result.put("debitTypeList", debitTypeList);
        }
        if(CollectionUtils.isNotEmpty(fundsSourcesList)){
            result.put("fundsSourcesList", fundsSourcesList);
        }
        return result;
    }
    
    /**
     * 查询当日报盘配置信息
     * @return
     */
    private List<OfferConfig> queryOfferConfigList() {
        OfferConfig vo = new OfferConfig();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        vo.setExecDay(Long.valueOf(day));
        vo.setIsValid("t");
        // 查询报盘配置信息
        return offerConfigDao.findListByVo(vo);
    }
}
