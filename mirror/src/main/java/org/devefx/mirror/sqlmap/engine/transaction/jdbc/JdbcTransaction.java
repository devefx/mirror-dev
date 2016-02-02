package org.devefx.mirror.sqlmap.engine.transaction.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.devefx.mirror.sqlmap.engine.transaction.Transaction;
import org.devefx.mirror.sqlmap.engine.transaction.TransactionException;

public class JdbcTransaction implements Transaction {

	private DataSource dataSource;
	private Connection connection;
	private int isolationLevel;
	private int originalIsolationLevel;
	
	public JdbcTransaction(DataSource ds, int isolationLevel) throws TransactionException {
		this.dataSource = ds;
		if (dataSource == null) {
			throw new TransactionException("JdbcTransaction initialization failed.  DataSource was null.");
		}
		this.isolationLevel = isolationLevel;
	}
	
	private void init() throws SQLException, TransactionException {
		connection = dataSource.getConnection();
		if (connection == null) {
			throw new TransactionException("JdbcTransaction could not start transaction.  Cause: The DataSource returned a null connection.");
		}
		originalIsolationLevel = connection.getTransactionIsolation();
		if (isolationLevel != originalIsolationLevel) {
			connection.setTransactionIsolation(isolationLevel);
		}
		if (connection.getAutoCommit()) {
			connection.setAutoCommit(false);
		}
	}
	
	@Override
	public void commit() throws SQLException, TransactionException {
		if (connection != null) {
			connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException, TransactionException {
		if (connection != null) {
			connection.rollback();
		}
	}

	@Override
	public void close() throws SQLException, TransactionException {
		if (connection != null) {
			try {
				if (originalIsolationLevel != isolationLevel) {
					connection.setTransactionIsolation(originalIsolationLevel);
				}
			} finally {
				connection.close();
				connection = null;
			}
		}
	}

	@Override
	public Connection getConnection() throws SQLException, TransactionException {
		if (connection == null) {
			init();
		}
		return connection;
	}

}
