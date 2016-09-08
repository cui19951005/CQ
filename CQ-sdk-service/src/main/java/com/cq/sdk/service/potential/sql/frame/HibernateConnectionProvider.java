package com.cq.sdk.service.potential.sql.frame;

import com.cq.sdk.service.potential.annotation.Autowired;
import net.sf.cglib.proxy.InvocationHandler;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by admin on 2016/9/7.
 */
public class HibernateConnectionProvider implements InvocationHandler {
    @Autowired
    private DataSource dataSource;
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Class clazz=HibernateConnectionProvider.class;
        for(Method m : clazz.getMethods()){
            if(m.getName().equals(method.getName())){
                return m.invoke(this,objects);
            }
        }
        return null;
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    public boolean supportsAggressiveRelease() {
        return false;
    }

    
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }


}
