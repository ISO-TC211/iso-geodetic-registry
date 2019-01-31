package org.iso.registry.client.configuration.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import de.geoinfoffm.registry.persistence.jpa.HibernateConfigurationImpl;
import org.apache.commons.lang3.StringUtils;
import org.iso.registry.client.configuration.security.SecurityConfiguration;
import org.springframework.context.annotation.Configuration;

import de.geoinfoffm.registry.client.web.AbstractWebApplicationInitializer;
import de.geoinfoffm.registry.persistence.PersistenceConfiguration;

import java.sql.*;

/**
 * Initializer for the Spring web application.
 * 
 * @author Florian Esser
 *
 */
@Configuration
public class WebApplicationInitializer extends AbstractWebApplicationInitializer 
{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { SecurityConfiguration.class, PersistenceConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { IsoClientConfiguration.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

		HibernateConfigurationImpl conf = new HibernateConfigurationImpl();

		try {
			Connection connection  = conf.dataSource().getConnection();
			int count = 0;
			int maxCount = 360;//1 hours
			while (!this.isDbConnected(connection) && count < maxCount) {
				Thread.sleep(10_000);//wait for each 10sec
				System.out.println("Waiting count= " + count++);
			}

			if (count >= maxCount) {
				throw new ServletException("DB failed (count expired)");
			}

			System.out.println("DB is ready!!");
		} catch (Exception e) {
			System.out.println("Connection Failed! Check output console");
			throw new ServletException("DB failed", e);

		}


		servletContext.addListener(new SessionListener());
    }

	public boolean isDbConnected(Connection connection) {
		String waitSql = System.getenv("WAIT_SQL");
		boolean hasWaitSql = StringUtils.isNotBlank(waitSql);
		String checkSqlQuery = hasWaitSql ? waitSql : "SELECT 1;";

		System.out.println("Checking DB Connection SQL= " + checkSqlQuery);

		boolean isConnected = false;
		try {
			final PreparedStatement statement = connection.prepareStatement(checkSqlQuery);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();

			if (hasWaitSql) {
				String waitResult = System.getenv("WAIT_RESULT");
				isConnected = waitResult.equals(resultSet.getString(1));
			}
			else {
				isConnected = true;
			}
		} catch (Exception e) {
			System.out.println("DB is not ready => waiting, error: " + e.getMessage());
		}

		System.out.println("DB IsConnected= " + isConnected);

		return isConnected;
	}
}
