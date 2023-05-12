// 
// 
// 

package exam.dao.base;

import java.util.Iterator;
import java.math.BigInteger;
import java.util.Map;
import exam.util.DataUtil;
import exam.model.page.PageBean;
import java.util.HashMap;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDaoImpl<T> implements BaseDao<T>
{
    @Resource
    protected JdbcTemplate jdbcTemplate;
    protected RowMapper<T> rowMapper;
    protected String sql;
    protected String countSql;
    
    public BaseDaoImpl() {
        this.rowMapper = this.getRowMapper();
        this.sql = this.getSql();
        this.countSql = this.getCountSql();
    }
    
    @Override
    public List<T> find(final T entity) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void executeSql(final String sql) {
        this.jdbcTemplate.update(sql);
    }
    
    @Override
    public void executeSql(final String sql, final Object[] params) {
        this.jdbcTemplate.update(sql, params);
    }
    
    @Override
    public List<T> queryBySQL(final String sql, final Object... params) {
        return (List<T>)this.jdbcTemplate.query(sql, params, (RowMapper)this.rowMapper);
    }
    
    @Override
    public List<T> queryBySQL(final String sql) {
        return (List<T>)this.jdbcTemplate.query(sql, (RowMapper)this.rowMapper);
    }
    
    @Override
    public Object queryForObject(final String sql, final Class<?> clazz) {
        return this.jdbcTemplate.queryForObject(sql, (Class)clazz);
    }
    
    @Override
    public int[] batchUpdate(final String... sqls) {
        return this.jdbcTemplate.batchUpdate(sqls);
    }
    
    @Override
    public int getKeyHelper(final String sql, final GenerateKeyCallback callback, final Object object) {
        final KeyHolder keyHolder = (KeyHolder)new GeneratedKeyHolder();
        this.jdbcTemplate.update((PreparedStatementCreator)new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(sql, 1);
                callback.setParameters(ps, object);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }
    
    @Override
    public PageBean<T> pageSearch(final int pageCode, final int pageSize, final int pageNumber, final String where, final List<Object> params, final HashMap<String, String> orderbys) {
        final String whereSql = DataUtil.isValid(where) ? where : "";
        final StringBuilder sqlBuilder = new StringBuilder(this.sql).append(" ").append(whereSql);
        final StringBuilder countSqlBuilder = new StringBuilder(this.countSql).append(" ").append(whereSql);
        PageBean<T> pageBean = null;
        if (DataUtil.isValid(orderbys)) {
            sqlBuilder.append(" order by ");
            for (final String key : orderbys.keySet()) {
                sqlBuilder.append(key).append(" ").append(orderbys.get(key)).append(",");
            }
            sqlBuilder.deleteCharAt(this.sql.length() - 1);
        }
        final int begin = (pageCode - 1) * pageSize;
        sqlBuilder.append(" limit ").append(begin).append(", ").append(pageSize);
        if (DataUtil.isValid(params)) {
            final Object[] paramsArray = params.toArray();
            pageBean = new PageBean<T>(this.jdbcTemplate.query(sqlBuilder.toString(), paramsArray, (RowMapper)this.rowMapper), pageSize, pageCode, ((BigInteger)this.jdbcTemplate.queryForObject(countSqlBuilder.toString(), paramsArray, (Class)BigInteger.class)).intValue(), pageNumber);
        }
        else {
            pageBean = new PageBean<T>(this.jdbcTemplate.query(sqlBuilder.toString(), (RowMapper)this.rowMapper), pageSize, pageCode, ((BigInteger)this.jdbcTemplate.queryForObject(countSqlBuilder.toString(), (Class)BigInteger.class)).intValue(), pageNumber);
        }
        return pageBean;
    }
}
