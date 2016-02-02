package org.devefx.mirror.sqlmap.client.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.devefx.mirror.sqlmap.client.SqlMapClient;

public class SqlMapClientImpl implements SqlMapClient {

	public SqlMapExecutorDelegate delegate;
	
	public SqlMapClientImpl(SqlMapExecutorDelegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void startTransaction() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startTransaction(int transactionIsolation) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commitTransaction() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTransaction() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserConnection(Connection connnection) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection getCurrentConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
