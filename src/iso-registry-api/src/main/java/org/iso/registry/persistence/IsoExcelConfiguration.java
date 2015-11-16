package org.iso.registry.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import de.bespire.io.excel.ColumnConfiguration;
import de.bespire.io.excel.ExcelConfiguration;
import de.bespire.io.excel.SheetConfiguration;

@Configuration
public class IsoExcelConfiguration
{
	@Autowired
	private ExcelConfiguration excelConfiguration;
	
	public ExcelConfiguration getConfiguration() {
		return excelConfiguration;
	}
	
	public static ExcelConfiguration reload() throws IOException, JAXBException {
		final String pathToTemplate = "isoExcelConfiguration.xml";
		final URL url = IsoExcelConfiguration.class.getClassLoader().getResource(pathToTemplate);
		if (url == null) {
			throw new IllegalStateException(String.format("Could not load resource path %s", pathToTemplate));
		}

//		JAXBContext ctx = com.sun.xml.internal.bind.v2.ContextFactory.createContext(new Class[] { ExcelConfiguration.class, SheetConfiguration.class, ColumnConfiguration.class }, null);
		JAXBContext ctx = JAXBContext.newInstance(new Class[] { ExcelConfiguration.class, SheetConfiguration.class, ColumnConfiguration.class }, null);
		Path path;
		try {
			path = Paths.get(url.toURI());
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		StreamSource source = new StreamSource(Files.newInputStream(path, StandardOpenOption.READ));
		
		return ctx.createUnmarshaller().unmarshal(source, ExcelConfiguration.class).getValue();
	}

}
