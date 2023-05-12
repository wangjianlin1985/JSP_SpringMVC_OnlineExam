// 
// 
// 

package exam.service.base;

import exam.model.page.PageBean;
import java.util.HashMap;
import java.util.List;
import exam.dao.base.BaseDao;

public abstract class BaseServiceImpl<T> implements BaseService<T>
{
    protected BaseDao<T> baseDao;
    
    protected abstract void setBaseDao(final BaseDao<T> p0);
    
    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void delete(final Object id) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void saveOrUpdate(final T entity) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<T> find(final T entity) {
        return this.baseDao.find(entity);
    }
    
    @Override
    public PageBean<T> pageSearch(final int pageCode, final int pageSize, final int pageNumber, final String where, final List<Object> params, final HashMap<String, String> orderbys) {
        return this.baseDao.pageSearch(pageCode, pageSize, pageNumber, where, params, orderbys);
    }
}
