package org.devefx.mirror.sqlmap.client;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public interface SqlMapTransactionManager {
	
	public void startTransaction() throws SQLException;
	
	public void startTransaction(int transactionIsolation) throws SQLException;
	
	public void commitTransaction() throws SQLException;
	
	public void endTransaction() throws SQLException;
	
	public void setUserConnection(Connection connnection) throws SQLException;
	
	public Connection getCurrentConnection() throws SQLException;
	
	public DataSource getDataSource();
}
