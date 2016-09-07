package com.cq.sdk.service.test;

import com.cq.sdk.service.utils.Logger;
import net.sf.cglib.proxy.InvocationHandler;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by admin on 2016/9/7.
 */
public class InvocationHandlerClass implements InvocationHandler,ConnectionProvider {
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Logger.info(method.getName());
        return null;
    }
    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {

    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
