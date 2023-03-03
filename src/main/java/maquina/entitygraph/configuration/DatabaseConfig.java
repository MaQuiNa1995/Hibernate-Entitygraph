package maquina.entitygraph.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

	@Bean
	public DataSource datasource(@Value(value = "${database.user:root}") String user,
			@Value(value = "${database.password:pass}") String pass,
			@Value(value = "${database.driver:com.mysql.jdbc.Driver}") String driver,
			@Value(value = "${database.url:jdbc:mysql://localhost:3306/EntityGraph}") String url,
			@Value(value = "${database.poolName:MaQuiNa1995-HikariCP}") String poolName) {

		HikariDataSource dataSource = new HikariDataSource();

		dataSource.setUsername(user);
		dataSource.setPassword(pass);
		dataSource.setDriverClassName(driver);
		dataSource.setJdbcUrl(url);

		dataSource.setMaximumPoolSize(5);
		dataSource.setPoolName(poolName);

		dataSource.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		dataSource.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		dataSource.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		dataSource.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

		return dataSource;
	}

}
