package com.global.vms.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {
	private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@PostConstruct
	public void init() {
		try {
			boolean isConn = jdbcTemplate.query(env.getProperty("sql.connectionCheck"),
					new ResultSetExtractor<Boolean>() {

						@Override
						public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next())
								return true;
							else
								return false;
						}
					});
			if (isConn)
				log.info("DB connection successful !");
			else
				log.error("Error in initializing DB connection !");
		} catch (Exception e) {
			log.error("Error in initializing DB connection", e);
		}
	}
}
