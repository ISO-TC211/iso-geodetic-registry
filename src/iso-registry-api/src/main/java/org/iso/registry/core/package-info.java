@XmlSchema(
    namespace = "http://registry.gdi-de.org",
    	    elementFormDefault = XmlNsForm.QUALIFIED,
    	    attributeFormDefault = XmlNsForm.UNQUALIFIED,
   	    	    xmlns={
    				@XmlNs(prefix="gdireg", namespaceURI="http://registry.gdi-de.org"),
    				@XmlNs(prefix="gmd", namespaceURI="http://www.isotc211.org/2005/gmd"),
    	    		@XmlNs(prefix="gco", namespaceURI="http://www.isotc211.org/2005/gco"),
    	    		@XmlNs(prefix="grg", namespaceURI="http://www.isotc211.org/2005/grg"),
    	    	  })
package org.iso.registry.core;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
