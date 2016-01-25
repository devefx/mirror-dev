package org.devefx.mirror.sqlmap.engine.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class MultiDataSource implements DataSource {
	
	public enum Mode { READER, WRITE }
	
	private DataSource useDataSource;
	private List<DataSource> readerDataSources;
	private List<DataSource> writeDataSources;
	
	private ChoosePolicy readerChoosePolicy;
	private ChoosePolicy writeChoosePolicy;
	
	public void setReaderChoosePolicy(ChoosePolicy readerChoosePolicy) {
		this.readerChoosePolicy = readerChoosePolicy;
	}

	public ChoosePolicy getReaderChoosePolicy() {
		return readerChoosePolicy;
	}
	
	public void setWriteChoosePolicy(ChoosePolicy writeChoosePolicy) {
		this.writeChoosePolicy = writeChoosePolicy;
	}
	
	public ChoosePolicy getWriteChoosePolicy() {
		return writeChoosePolicy;
	}

	public void setReaderDataSources(List<DataSource> readerDataSources) {
		this.readerDataSources = readerDataSources;
	}

	public void setWriteDataSources(List<DataSource> writeDataSources) {
		this.writeDataSources = writeDataSources;
	}
	
	public void chooseDataSource(Mode mode, int index) {
		if (mode == Mode.READER) {
			useDataSource = (readerDataSources.size() < index + 1) ? 
					readerDataSources.get(0) : readerDataSources.get(index);
			return;
		}
		useDataSource = (writeDataSources.size() < index + 1) ? 
				writeDataSources.get(0) : readerDataSources.get(index);
	}
	
	public DataSource getUseDataSource() {
		if (useDataSource == null) {
			useDataSource = readerDataSources.get(0);
		}
		return useDataSource;
	}
	
	public Connection getConnection() throws SQLException {
		return getUseDataSource().getConnection();
	}
	
	public Connection getConnection(String username, String password) throws SQLException {
		return getUseDataSource().getConnection(username, password);
	}
	
	public PrintWriter getLogWriter() throws SQLException {
		return getUseDataSource().getLogWriter();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		getUseDataSource().setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		getUseDataSource().setLoginTimeout(seconds);
	}

	public int getLoginTimeout() throws SQLException {
		return getUseDataSource().getLoginTimeout();
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return getUseDataSource().getParentLogger();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getUseDataSource().unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getUseDataSource().isWrapperFor(iface);
	}
}
