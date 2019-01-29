package org.iso.registry.client.configuration.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import de.geoinfoffm.registry.persistence.jpa.HibernateConfigurationImpl;
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
			while (!this.isDbConnected(connection) && count < 10) {
				Thread.sleep(10_000);
				System.out.println("Waiting count= " + count++);
			}

			if (count >= 10) {
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
		final String CHECK_SQL_QUERY = "SELECT 1;";
		boolean isConnected = false;
		try {
			final PreparedStatement statement = connection.prepareStatement(CHECK_SQL_QUERY);
//			ResultSet resultSet = statement.executeQuery();
//			resultSet.next();
//			System.out.println("Count registryuser= " + resultSet.getInt(1));
			isConnected = true;
		} catch (Exception e) {
			// handle SQL error here!
			System.err.println(e);
			System.out.println("DB is not ready => waiting");
		}
		return isConnected;
	}
}
