package com.zdmoney.credit.repay.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.vo.RequestFileOperateRecord;

/**
 * 申请书管理菜单文件上传下载操作
 */
public interface IRequestFileOperateRecordDao extends IBaseDao<RequestFileOperateRecord> {
     /**
     * 查询申请书管理导出文件的文件批次号
     * @param params
     * @return
     */
    public String findRequestFileBatchNum(Map<String,Object> params);

    /***
     * 查询文件当天操作记录 g
     * 根据债权批次号
     * @param requestFileOperateRecord
     * @return
     */
    public List<RequestFileOperateRecord> findRequestFileOperateRecord(RequestFileOperateRecord requestFileOperateRecord);

    /**
     * 根据条件查询文件批次号
     * @param paramMap
     * @return
     */
	public String findFileSeqByParam(Map<String, Object> paramMap);

    /**
     * 查询当天的文件批次信息 升序排列
     * @param paramMap
     * @return
     */
    public List<RequestFileOperateRecord> queryCurrentDayRequestFileOperateRecordByAsc(Map<String,Object> paramMap) ;

    }
