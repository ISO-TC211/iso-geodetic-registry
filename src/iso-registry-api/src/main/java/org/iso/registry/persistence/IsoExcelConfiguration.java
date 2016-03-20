package org.iso.registry.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import de.bespire.registry.io.excel.configuration.DocumentRoot;
import de.bespire.registry.io.excel.configuration.ExcelAdapterConfigurationPackage;
import de.bespire.registry.io.excel.configuration.ExcelConfiguration;

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
