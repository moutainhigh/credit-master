package com.zdmoney.credit.framework.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ClassUtils;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 数据库Dao基类，定义一些公共方法（接口实现层）
 * @author Ivan
 *
 */
public abstract class BaseDaoImpl<T extends BaseDomain> 
		extends SqlSessionDaoSupport implements IBaseDao<T> {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private static final String COMMON_MAPPER_NAME_SPANCE = "com.ezendai.credit2.mapper.BaseMapper";
	
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 初始化sqlSessionFactory 对象
	 * @param sqlSessionFactory
	 */
	@Autowired @Qualifier("sqlSessionFactory")
    public void setSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory =sqlSessionFactory;
        super.setSqlSessionFactory(sqlSessionFactory);
    }
	
	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String INSERT = ".insert";
	private static final String DELETE = ".delete";
	private static final String DELETE_LIST = ".deleteByIdList";
	private static final String UPDATE = ".update";
	private static final String GET = ".get";
	private static final String LIST = ".findListByVO";
	private static final String LISTBYMAP = ".findListByMap";
	private static final String COUNT = ".count";
	private static final String COUNTBYMAP = ".countByMap";
	private static final String FIND_WITH_PG = ".findWithPG";
	private static final String FIND_WITH_PGBYMAP = ".findWithPGByMap";
	private static final String EXISTS = ".exists";
	private static final String SELECT_ONE = ".selectOne";
	private static final String AllLIST = ".findAllList";
	private static final String UPDATESELECTIVE = ".updateByPrimaryKeySelective";
	
	/** 扩展SQLMAP XML文件后缀名 通常是复杂查询时用到 **/
	protected static final String SQLMAP_SUFFIX_NAME = "Extend";
	
	/**
	 * 将实体信息插入数据库
	 * @param object 实体对象
	 * @author Ivan
	 */
	@Override
	public T insert(T BaseDomain) {
		BaseDomain baseDomain = (BaseDomain) BaseDomain;
		/** 获取登陆者信息 **/
		User user = UserContext.getUser();
		if (user == null) {
			baseDomain.setCreator("admin");
		} else {
			baseDomain.setCreator(user.getName());
		}
		baseDomain.setCreateTime(new Date());
		this.getSqlSession().insert(getIbatisMapperNameSpace() + INSERT, BaseDomain);
		return BaseDomain;
	}
	
	/**
	 * 跟据PK删除记录
	 * @param id 表PK值
	 * @author Ivan
	 */
	@Override
	public void deleteById(Long id) {
		this.getSqlSession().update(getIbatisMapperNameSpace() + DELETE, id);
	}
	
	/**
	 * 更新数据
	 * @param baseDomain 实体对象
	 * @author Ivan
	 */
	@Override
	public int update(BaseDomain baseDomain) {
		int affectNum = 0;
		/** 获取登陆者信息 **/
		User user = UserContext.getUser();
		if (user == null) {
			baseDomain.setUpdator("admin");
		} else {
			baseDomain.setUpdator(user.getName());
		}
		baseDomain.setUpdateTime(new Date());
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + UPDATE, baseDomain);
		return affectNum;
	}
	
	/**
	 * 更新特定数据
	 * @param baseDomain 实体对象
	 * @author renzheng
	 */
	@Override
	public int updateByPrimaryKeySelective(BaseDomain baseDomain) {
		int affectNum = 0;
		/** 获取登陆者信息 **/
		User user = UserContext.getUser();
		if (user == null) {
			baseDomain.setUpdator("admin");
		} else {
			baseDomain.setUpdator(user.getName());
		}
		baseDomain.setUpdateTime(new Date());
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + UPDATESELECTIVE, baseDomain);
		return affectNum;
	}
	
	/**
	 * 根据id查找实体对象
	 * @param id PK值
	 * @author Ivan
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T get(Long id) {
		if (id == null) {
			return null;
		}
		return (T) getSqlSession().selectOne(getIbatisMapperNameSpace() + GET, id);
	}
	
	/**
	 * 跟据实体信息查询实体对象（通常是组合条件查询）
	 * @param baseDomain 实体对象
	 * @author Ivan
	 */
	public T get(BaseDomain baseDomain) {
		List<T> list = findListByVo(baseDomain);
		T t = null;
		if (list != null && !list.isEmpty() && list.size() == 1) {
			t = list.get(0);
		}
		return t;
	}
	
	/**
	 * 跟据实体信息查询实体集合（通常是组合条件查询）
	 * @param baseDomain 实体对象
	 * @author Ivan
	 */
	public List<T> findListByVo(BaseDomain baseDomain) {
		List<T> rstList;
		rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + LIST, BeanUtils.toMap(baseDomain));
		return rstList;
	}
	
	/**
	 * 跟据Map查询实体集合（通常是组合条件查询）
	 */
	public List<T> findListByMap(Map<String,Object> paramMap) {
		List<T> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + LISTBYMAP, paramMap);
		return rstList;
	}
	
	
	/**
	 * 判断PK对应的记录是否存在
	 * @param id PK值
	 * @author Ivan
	 */
	@Override
	public boolean exists(Long id) {
		return get(id) != null ? true : false;
	}
	
	/**
	 * 根据多个字段查询记录是否存在
	 * @param map 条件集合
	 * @author Ivan
	 */
	@Override
	public boolean exists(Map<String, Object> map) {
		Object count = getSqlSession().selectOne(getIbatisMapperNameSpace() + EXISTS, map);
		int totalCount = Integer.parseInt(count.toString());
		return totalCount > 0 ? true : false;
	}
	
	/**
	 * 跟据多个条件删除记录
	 */
	@Override
	public void deleteByIdList(BaseDomain baseDomain) {
		this.getSqlSession().delete(getIbatisMapperNameSpace() + DELETE_LIST, baseDomain);
	}
	
	/**
	 * 带分页查询
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Pager findWithPg(BaseDomain baseDomain) {
		Pager pager = baseDomain.getPager();
		Assert.notNull(pager, "分页参数");
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + FIND_WITH_PG);
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + COUNT);
		return doPager(pager,BeanUtils.toMap(baseDomain));
	}
	
	/** 根据查询条件Map物理分页查询出数据行：{mapper.xml需要实现} */
	public Pager findWithPgByMap(Map<String,Object> paramMap) {
		Pager pager = (Pager)paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + FIND_WITH_PGBYMAP);
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + COUNTBYMAP);
		return doPager(pager,paramMap);
	}
	
	/**
	 * 
	 * 分页公共方法
	 * 
	 * @param pager 分页参数
	 * @param obj 条件数据
	 * @param countName 查询总记录数 SqlMapID
	 * @param findWithPGName 查询结果集 SqlMapID
	 * @return
	 */
	public Pager doPager(Pager pager,Object obj) {
		Assert.notNull(pager, "分页参数");
		Assert.notNull(obj, "查询条件参数");
		Assert.notNullAndEmpty(pager.getSearchDataSqlMapId(), "查询业务数据结果集 SqlMap Id");
		Assert.notNullAndEmpty(pager.getSearchCountSqlMapId(), "查询业务数据数量 SqlMap Id");
		
		Object count = getSqlSession().selectOne(pager.getSearchCountSqlMapId(), obj);
		int totalCount = Strings.convertValue(count.toString(),Integer.class);
		pager.setTotalCount(totalCount);
		pager.calStart();
		List rstList = new ArrayList();
		if (totalCount > 0) {
//			try {
				rstList = getSqlSession().selectList(pager.getSearchDataSqlMapId(), obj);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
		}
		pager.setResultList(rstList);
		return pager;
	}
	
	/**
	 * 查询单结果集(直接传Sql查询)
	 * @param sql sql语句
	 * @return
	 */
	@Override
	public Object selectOne(String sql) {
		return getSqlSession().selectOne(COMMON_MAPPER_NAME_SPANCE + SELECT_ONE, sql);
	}
	
	/**
	 * 跟据实体查询总记录数
	 * @param baseDomain 实体
	 * @return 总记录数
	 */
	public int queryCount(BaseDomain baseDomain) {
		return (Integer)getSqlSession().selectOne(getIbatisMapperNameSpace() + COUNT, BeanUtils.toMap(baseDomain));
	}
	
	/**
	 * 
	 * 获取执行mapperNameSpace空间路径
	 * @author Ivan
	 * 
	 */
	public String getIbatisMapperNameSpace() {
		return getIbatisMapperNameSpace("");
	}
	
	public String getIbatisMapperNameSpace(String suffix) {
		Class<T> clazz = getEntityClass();
		Package packAge = clazz.getPackage();
		String[] args = packAge.getName().split("\\.");
		StringBuffer sb = new StringBuffer();
		String model = ClassUtils.getShortName(clazz);
		String method = Character.toUpperCase(model.charAt(0)) + model.substring(1);
		for (int i = 0; i < args.length; i++) {
			if (i < args.length) {
				sb.append(args[i] + ".");
			}
		}
		String nameSpace = sb.append("").append(method).append(suffix).append("Mapper").toString();
		return nameSpace;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		return (Class<T>) pt.getActualTypeArguments()[0];
	}
	
	/**
	 * 跟据实体信息查询实体集合（通常是组合条件查询）
	 */
	public List<T> findAllList() {
		List<T> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + AllLIST);
		return rstList;
	}
	
	/**
	 * 查询存储过程
	 * @return
	 */
	public Object selectProcedure(Map<String, Object> paramMap){
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + "."+paramMap.get("procedureName"), paramMap);
		
	}
	
	/**
	 * 获取sql语句
	 * @param params
	 * @param id
	 * @return
	 */
	public String getSql(String id,Object params){
		return  sqlSessionFactory.getConfiguration().getMappedStatement(getIbatisMapperNameSpace() +"."+id).getBoundSql(params).getSql();
	}
}
