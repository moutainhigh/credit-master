package com.zdmoney.credit.common.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Pager {

	private static final long serialVersionUID = 3687438840805341687L;

	private List resultList = Collections.emptyList();;

	public final static String DIRECTION_DESC = "DESC";
	public final static String DIRECTION_ASC = "ASC";

	/** 当前页，真实页数，取值：1、2、3.... */
	private int page = 1;

	/** 记录开始的rowNum，从零开始*/
	private int start;

	/** 每页显示数量limit */
	private int rows = 50;

	/** 排序asc,desc */
	private String sort;

	/** 排序字段 */
	private String sidx;

	private boolean needCount;

	private long totalCount;
	/** 查询业务数据结果集 SqlMap Id **/
	private String searchDataSqlMapId;
	/** 查询业务数据数量 SqlMap Id **/
	private String searchCountSqlMapId;
	private Map<String ,Object> mapData;

	final public long getTotalCount() {
		return totalCount;
	}

	final public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	final public void calStart() {
		if (start >= totalCount) {
			start = ((int) ((totalCount - 1) / rows)) * rows;
		}
	}

	@Deprecated
	final public void setPgNumber(int pgNumber) {
		if (pgNumber <= 0) {
			start = 0;
		} else {
			start = pgNumber * rows;
		}
	}

	final public int getPgNumber() {
		return start / rows + 1;
	}

	final public int getEnd() {
		return this.start + this.rows;
	}

	final public int getStart() {
		return start;
	}

	final public void setStart(int start) {
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		if(rows < 1)
			throw new IllegalArgumentException("每页显示数量小于1");
		this.rows = rows;
		this.start = (page - 1) * rows;
	}

	final public boolean isNeedCount() {
		return needCount;
	}

	final public void setNeedCount(boolean needCount) {
		this.needCount = needCount;
	}

	final public String getSort() {
		return sort;
	}

	final public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public int getPage() {
		return start / rows;
	}

	public void setPage(int page) {
		if (page < 1) {
			page = 1;
//			throw new IllegalArgumentException("页数小于1");
		}
		this.page = page;
		this.start = (page - 1) * rows;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	/** 获取到总页数 */
	public int getTotalPage() {
		if (totalCount <= rows) {
			return 1;
		} else {
			return ((int) (totalCount % rows)) != 0 ? ((int) (totalCount / rows)) + 1
				: ((int) (totalCount / rows));
		}
	}

	public String getSearchDataSqlMapId() {
		return searchDataSqlMapId;
	}

	public void setSearchDataSqlMapId(String searchDataSqlMapId) {
		this.searchDataSqlMapId = searchDataSqlMapId;
	}

	public String getSearchCountSqlMapId() {
		return searchCountSqlMapId;
	}

	public void setSearchCountSqlMapId(String searchCountSqlMapId) {
		this.searchCountSqlMapId = searchCountSqlMapId;
	}

	public Map<String, Object> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, Object> mapData) {
		this.mapData = mapData;
	}
	
}
