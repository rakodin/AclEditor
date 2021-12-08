package org.rakovsky.acl.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class AppDataSource {

	private final HikariDataSource ds;
	private static AppDataSource instance;
	private final static Lock instanceLock = new ReentrantLock(true);
	private String datasourceSchema;
	private static final String confPrefix = "dataSource.";
	private static final String schemaPropName = String.format("%sschemaName", confPrefix);
	private AppDataSource(String config) {
		//HikariConfig cfg = new HikariConfig(config);
		Properties poolProps = new Properties();
		final Properties p = new Properties();
		try {
			InputStream is = new FileInputStream(config);
			p.load(is);
			this.datasourceSchema = p.getProperty(schemaPropName);
			p.stringPropertyNames().stream()
			.filter(n -> (n.startsWith(confPrefix) && !n.equals(schemaPropName)))
			.collect(Collectors.toList())
			.forEach(pv -> poolProps.setProperty(pv.replaceFirst(confPrefix, "")
					, p.getProperty(pv)));
		} catch (IOException e) {
			throw new RuntimeException("Can't read properties from file '" + config + "'", e);
		}
		
		HikariConfig cfg = new HikariConfig(poolProps);
		if (this.datasourceSchema  != null) {
			//System.out.println("Set current schema to " + datasourceSchema);
			cfg.setConnectionInitSql("alter session set current_schema = " + datasourceSchema);
		}
		this.ds = new HikariDataSource(cfg);
		ds.setAutoCommit(false);
	}

	public static void configure(String config) {
		if (instance == null) {
			instanceLock.lock();
			if (instance == null) {
				instance = new AppDataSource(config);
			}
			instanceLock.unlock();
		}
	}

	private Connection _getConnection() {
		try {
			Connection c = instance.ds.getConnection();
			return c;
		} catch (SQLException e) {
			throw new RuntimeException("Can't obtain new connection from pool", e);
		}
	}

	private void _closeConnection(Connection c) {
		try {
			if (c != null && !c.isClosed()) {
				if (!c.isReadOnly() && !c.getAutoCommit()) {
					System.out.println("Commit...");
					c.commit();
				}
				c.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Can't close connection", e);
		}
	}
	
	
	public static Connection get() {
		return instance._getConnection();
	}
	
	public static void close(Connection c) {
		instance._closeConnection(c);
	}
}
