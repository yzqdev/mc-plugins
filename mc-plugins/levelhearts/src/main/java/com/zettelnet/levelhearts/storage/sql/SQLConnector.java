package com.zettelnet.levelhearts.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yanni
 */
public interface SQLConnector {
    /**
     * 创建数据接口链接
     * @return Connection
     * @throws SQLException
     */
    Connection createConnection() throws SQLException;
}
