package org.iso.registry.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import de.bespire.registry.io.excel.ExcelAdapterConfiguration.ColumnConfiguration;
import de.bespire.registry.io.excel.ExcelAdapterConfiguration.DocumentRoot;
import de.bespire.registry.io.excel.ExcelAdapterConfiguration.ExcelAdapterConfigurationFactory;
import de.bespire.registry.io.excel.ExcelAdapterConfiguration.ExcelAdapterConfigurationPackage;
import de.bespire.registry.io.excel.ExcelAdapterConfiguration.ExcelConfiguration;
import de.bespire.registry.io.excel.ExcelAdapterConfiguration.SheetConfiguration;

@Configuration
public class IsoExcelConfiguration
{
	@Autowired
	private ExcelConfiguration excelConfiguration;
	
	public ExcelConfiguration getConfiguration() {
		return excelConfiguration;
	}
	
	public static ExcelConfiguration reload() throws IOException, JAXBException, URISyntaxException {
		final String pathToTemplate = "isoExcelConfiguration.xml";
		final URL url = IsoExcelConfiguration.class.getClassLoader().getResource(pathToTemplate);
		if (url == null) {
			throw new IllegalStateException(String.format("Could not load resource path %s", pathToTemplate));
		}

//		JAXBContext ctx = com.sun.xml.internal.bind.v2.ContextFactory.createContext(new Class[] { ExcelConfiguration.class, SheetConfiguration.class, ColumnConfiguration.class }, null);
//		JAXBContext ctx = JAXBContext.newInstance(new Class[] { ExcelConfiguration.class, SheetConfiguration.class, ColumnConfiguration.class }, null);
//		Path path;
//		try {
//			path = Paths.get(url.toURI());
//		}
//		catch (URISyntaxException e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//		StreamSource source = new StreamSource(Files.newInputStream(path, StandardOpenOption.READ));
//		
//		return ctx.createUnmarshaller().unmarshal(source, ExcelConfiguration.class).getValue();
		
		EPackage.Registry.INSTANCE.put(ExcelAdapterConfigurationPackage.eNS_URI, ExcelAdapterConfigurationPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI(url.toURI().toString()));

		Map<Object, Object> options = new HashMap<>();
		options.put(XMLResource.OPTION_EXTENDED_META_DATA, true);
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");

		resource.load(options);
		DocumentRoot loadedRoot = (DocumentRoot)resource.getContents().get(0);
		ExcelConfiguration loadedConfig = loadedRoot.getExcelConfiguration();

		return loadedConfig;
	}

}
