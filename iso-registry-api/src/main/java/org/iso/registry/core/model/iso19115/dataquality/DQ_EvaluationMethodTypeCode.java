package org.iso.registry.core.model.iso19115.dataquality;

import javax.xml.namespace.QName;

import de.geoinfoffm.registry.core.model.iso19103.CodeListValue;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
public class DQ_EvaluationMethodTypeCode extends CodeListValue 
{
	private static final long serialVersionUID = -3373852806119046539L;
	
	public static final DQ_EvaluationMethodTypeCode DIRECT_INTERNAL = new DQ_EvaluationMethodTypeCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#DQ_EvaluationMethodTypeCode", "directInternal", "directInternal");
	public static final DQ_EvaluationMethodTypeCode DIRECT_EXTERNAL = new DQ_EvaluationMethodTypeCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#DQ_EvaluationMethodTypeCode", "directExternal", "directExternal");
	public static final DQ_EvaluationMethodTypeCode INDIRECT = new DQ_EvaluationMethodTypeCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#DQ_EvaluationMethodTypeCode", "indirect", "indirect");

	protected  DQ_EvaluationMethodTypeCode() {	}
	
	public DQ_EvaluationMethodTypeCode(String codeList, String codeListValue, String code) {
		super(codeList, codeListValue, code, new QName("http://www.isotc211.org/2005/gmd", "DQ_EvaluationMethodTypeCode", "gmd")); 
	}
}//end DQ_EvaluationMethodTypeCode