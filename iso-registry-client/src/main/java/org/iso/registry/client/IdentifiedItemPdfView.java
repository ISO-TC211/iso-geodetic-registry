package org.iso.registry.client;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class IdentifiedItemPdfView extends AbstractPdfView 
{

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request,
								    HttpServletResponse response) throws Exception {

		Paragraph p1 = new Paragraph(new Chunk("PDF Test"));
		
		document.add(p1);
	}

}
