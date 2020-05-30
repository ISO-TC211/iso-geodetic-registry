package org.iso.registry.client.configuration.web;

import de.geoinfoffm.registry.client.web.AbstractWebApplicationInitializer;
import de.geoinfoffm.registry.persistence.PersistenceConfiguration;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfigurationImpl;
import org.apache.commons.lang3.StringUtils;
import org.iso.registry.client.configuration.security.SecurityConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
			int maxCount = 3_600_000;//1 hours
			String waitStr = System.getenv("WAIT_IN_SECONDS");
			int waitSecs = StringUtils.isNotBlank(waitStr) ? Integer.parseInt(waitStr) : 0;
			waitSecs = Math.min(waitSecs * 1000, maxCount); //to mili-sec
			while (count < waitSecs) {
				Thread.sleep(10_000);//wait for each 10sec
				count += 10_000;
				System.out.println("Waiting count= " + count);
			}

			if (count >= maxCount || !this.isDbConnected(connection)) {
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
		String checkSqlQuery = "SELECT 1;";//hasWaitSql ? waitSql : "SELECT 1;";

		System.out.println("Checking DB Connection SQL= " + checkSqlQuery);

		boolean isConnected = false;
		try {
			final PreparedStatement statement = connection.prepareStatement(checkSqlQuery);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();

			isConnected = true;
		} catch (Exception e) {
			System.out.println("DB is not ready => waiting, error: " + e.getMessage());
		}

		System.out.println("DB IsConnected= " + isConnected);

		return isConnected;
	}
}
