package org.devefx.mirror.sqlmap.client;

import org.devefx.mirror.sqlmap.engine.transaction.Transaction;
import org.devefx.mirror.sqlmap.engine.transaction.TransactionState;

public interface SqlMapSession extends SqlMapExecutor, SqlMapTransactionManager {
	
	public void open();
	
	public void close();
	
	public boolean isClosed();
	
	
	public boolean isCommitRequired();
	
	public void setCommitRequired(boolean commitRequired);
	
	public Transaction getTransaction();
	
	public void setTransaction(Transaction transaction);
	
	public TransactionState getTransactionState();
	
	public void setTransactionState(TransactionState transactionState);
}
