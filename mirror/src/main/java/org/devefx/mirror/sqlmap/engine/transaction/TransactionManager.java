package org.devefx.mirror.sqlmap.engine.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.devefx.mirror.sqlmap.client.SqlMapSession;
import org.devefx.mirror.sqlmap.engine.transaction.jdbc.JdbcTransaction;

public class TransactionManager {

	private DataSource dataSource;
	
	public TransactionManager(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void begin(SqlMapSession session) throws TransactionException {
		begin(session, Connection.TRANSACTION_NONE);
	}
	
	public void begin(SqlMapSession session, int transactionIsolation) throws TransactionException {
		TransactionState state = session.getTransactionState();
		if (state == TransactionState.STATE_STARTED) {
			throw new TransactionException("TransactionManager could not start a new transaction.  " +
					"A transaction is already started.");
		} else if (state == TransactionState.STATE_USER_PROVIDED) {
			throw new TransactionException("TransactionManager could not start a new transaction.  " +
					"A user provided connection is currently being used by this session.  " +
					"The calling .setUserConnection (null) will clear the user provided transaction.");
		}
		Transaction transaction = new JdbcTransaction(dataSource, transactionIsolation);
		session.setCommitRequired(false);
		
		session.setTransaction(transaction);
		session.setTransactionState(TransactionState.STATE_STARTED);
	}
	
	public void commit(SqlMapSession session) throws TransactionException, SQLException {
		TransactionState state = session.getTransactionState();
		if (state == TransactionState.STATE_USER_PROVIDED) {
			throw new TransactionException("TransactionManager could not commit.  " +
					"A user provided connection is currently being used by this session.  " +
					"You must call the commit() method of the Connection directly.  " +
					"The calling .setUserConnection (null) will clear the user provided transaction.");
		} else if (state != TransactionState.STATE_STARTED && state != TransactionState.STATE_COMMITTED ) {
			throw new TransactionException("TransactionManager could not commit.  No transaction is started.");
		}
		if (session.isCommitRequired()) {
			session.getTransaction().commit();
			session.setCommitRequired(false);
		}
		session.setTransactionState(TransactionState.STATE_COMMITTED);
	}
	
	public void end(SqlMapSession session) throws TransactionException, SQLException {
		TransactionState state = session.getTransactionState();
		if (state == TransactionState.STATE_USER_PROVIDED) {
			throw new TransactionException("TransactionManager could not end this transaction.  " +
					"A user provided connection is currently being used by this session.  " +
					"You must call the rollback() method of the Connection directly.  " +
					"The calling .setUserConnection (null) will clear the user provided transaction.");
		}
		try {
			Transaction trans = session.getTransaction();
			if (trans != null) {
				try {
					if (state != TransactionState.STATE_COMMITTED) {
						if (session.isCommitRequired()) {
							trans.rollback();
							session.setCommitRequired(false);
						}
					}
				} finally {
					trans.close();
				}
			}
		} finally {
			session.setTransaction(null);
			session.setTransactionState(TransactionState.STATE_ENDED);
		}
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
}
