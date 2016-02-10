package com.vw.visitreporting.reports;

import java.awt.Color;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.util.IConstants;

/**
 * @author FOX0BVI
 * 
 */
public class OrganisationLevelsPdfView extends AbstractPdfView {

	private static final Font HEADLINE_FONT = new Font( Font.HELVETICA, 18, Font.BOLD, Color.blue );
	private static final Font HEADING_FONT = new Font( Font.HELVETICA, 12, Font.ITALIC, Color.black );
	private static final Font HEADING_DATA_FONT = new Font( Font.HELVETICA, 12, Font.ITALIC, Color.blue );
	private static final Font DATA_HEAD_FONT = new Font( Font.HELVETICA, 10, Font.ITALIC, Color.black );
	private static final Font TEXT_FONT = new Font( Font.TIMES_ROMAN, 8, Font.NORMAL, Color.black );
	private static final Font BOLD_FONT = new Font( Font.TIMES_ROMAN, 8, Font.BOLD, Color.black );
	private static final int MARGIN = 32;

	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.document.AbstractPdfView#buildPdfMetadata(java.util.Map, com.lowagie.text.Document, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void buildPdfMetadata(java.util.Map<String,Object> model, Document document, HttpServletRequest request) {
		System.out.println("In dealer_contact.jsp =========== ");
		document.addTitle("Organisations List");
		document.addCreationDate();
		document.addCreator("fox0bvi");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.document.AbstractPdfView#
	 * buildPdfDocument(java.util.Map, com.lowagie.text.Document,
	 * com.lowagie.text.pdf.PdfWriter, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void buildPdfDocument(Map model, Document document,
			PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Enumeration en = (Enumeration)request.getParameterNames();
		
		System.out.println("Request Headers ============ ");
		while (en.hasMoreElements()) {
			String string = (String) en.nextElement();
			System.out.println("one === " + request.getParameter(string));
		}
		
		System.out.println(request.getParameterNames());
		
		Enumeration en1 = (Enumeration)request.getParameterNames();
		
		System.out.println("Request Parameters ============ ");
		while (en1.hasMoreElements()) {
			String string = (String) en.nextElement();
			System.out.println("one === " + request.getParameter(string));
		}
		
//		createTemplate(document);
		
		MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
		writer.setPageEvent(events);
		events.onOpenDocument(writer, document);
		
		writePdf(model, createTemplate(document));
		
	}

	
	
	public BaseFont addFont() {
		
		// various fonts
//        BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
//        BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
		BaseFont bf_courier = null;
        try {
			bf_courier = BaseFont.createFont(BaseFont.COURIER, "Cp1252", false);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252", false);
        return bf_courier;
	}
	
	public Document formatHeader (Document document){
		
		System.out.println("header called ============= ");
		
		HeaderFooter header = new HeaderFooter(
	            new Phrase("This is a header without a page number", new Font(addFont())), false);
		header.setAlignment(Element.ALIGN_CENTER);
		document.setHeader(header);
		return document;
	}
	
	public Document formatFooter (Document document){
		
		System.out.println("footer called =========== ");
		
		HeaderFooter footer = new HeaderFooter(
	            new Phrase("This is page: ", new Font(addFont())), true);
		footer.setBorder(Rectangle.NO_BORDER);
		footer.setAlignment(Element.ALIGN_CENTER);
		document.setFooter(footer);
		return document;
		
	}
	
	public PdfPTable formatTable() throws DocumentException, NoSuchMessageException {
		
		System.out.println("In format Table ============= ");
		PdfPTable table1 = new PdfPTable(IConstants.INTEGER_TWO);
		table1.setWidthPercentage(IConstants.INTEGER_HUNDRED);
		table1.getDefaultCell().setBorderWidth(IConstants.INTEGER_ONE);
		table1.getDefaultCell().setBorderColor(Color.black);
		table1.getDefaultCell().setPadding(IConstants.INTEGER_FOUR);
		table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table1.getDefaultCell().setVerticalAlignment( Element.ALIGN_MIDDLE);

		// We can go now on the countries list
		PdfPTable table = new PdfPTable(2);
		table = new PdfPTable(2);
		int headerwidths[] = {20, 80};
		table.setWidths(headerwidths);
		table.setWidthPercentage(60);
		table.getDefaultCell().setBorderWidth(2);
		table.getDefaultCell().setBorderColor(Color.black);
		table.getDefaultCell().setGrayFill(0.75f);
		table.getDefaultCell().setPadding(3);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(new Phrase(getMessageSourceAccessor().getMessage( "code"), DATA_HEAD_FONT));
		table.addCell(new Phrase(getMessageSourceAccessor().getMessage( "name"), DATA_HEAD_FONT));

		// We set the above row as remaining title
		// and adjust properties for normal cells
		table.setHeaderRows(1);
		table.getDefaultCell().setBorderWidth(1);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
				
		return table;
	}

	public Document createTemplate(Document document){
		
		System.out.println("create template called");
		
		return formatFooter(formatHeader(document));
		
	}
	
	
	public void writePdf(Map<String,Object> model, Document document) 
			throws Exception {
		
		System.out.println("In writePdf =========== ");
		
		
		PdfPTable table = CustomPdfView.formatTable(IConstants.INTEGER_SIX);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(IConstants.STR_EMPTY);

		Font myfont = new Font(FontFactory.getFont(FontFactory.HELVETICA_BOLD,
				IConstants.INTEGER_ELEVEN, Font.NORMAL));
		Font myfont1 = new Font(FontFactory.getFont(FontFactory.HELVETICA, IConstants.INTEGER_ELEVEN,
				Font.NORMAL));

		PdfPCell headerCell = new PdfPCell(new Paragraph(IConstants.STR_HEADER_LEVELS, myfont));
		headerCell.setColspan(IConstants.INTEGER_FIVE);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(headerCell);

		PdfPCell cell = new PdfPCell(new Paragraph(IConstants.STR_HEADER_ORGANISATION,
				myfont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);

		Object[] levelArray = (Object[]) model.get(IConstants.STR_LEVELS);

		for (int i = 0; i < levelArray.length; i++) {
			Level level = (Level) levelArray[i];
			PdfPCell cell4 = new PdfPCell(new PdfPCell(new Paragraph(level.getName(), myfont)));
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell4.setVerticalAlignment(Element.ALIGN_TOP);
			table.addCell(cell4);
		}

		@SuppressWarnings("unchecked")
		List<Organisation> orgList = (List<Organisation>) model
				.get(IConstants.STR_ORGANISATIONS);

		for (Iterator iterator = orgList.iterator(); iterator.hasNext();) {
			Organisation organisation = (Organisation) iterator.next();
			Set<Level> levelSet = organisation.getLevels();

			PdfPCell cell1 = new PdfPCell(new Paragraph(organisation.getName(), myfont1));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(Element.ALIGN_TOP);
			table.addCell(cell1);

			for (int i = 0; i < levelArray.length; i++) {
				Level level = (Level) levelArray[i];
				if (levelSet.contains(level)) {
					PdfPCell cell2 = new PdfPCell(new Paragraph(IConstants.STR_YES, myfont1));
					cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell2.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell2);
				} else {
					PdfPCell cell3 = new PdfPCell(new Paragraph(IConstants.STR_NO, myfont1));
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell3.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell3);
				}
			}
		}
		// headers and footers must be added before the document is opened
		document.add(table);
	}
	
	//~ Inner Classes ----------------------------------------------------------

		private static class MyPageEvents extends PdfPageEventHelper {

			private MessageSourceAccessor messageSourceAccessor;

			// This is the PdfContentByte object of the writer
			private PdfContentByte cb;

			// We will put the final number of pages in a template
			private PdfTemplate template;

			// This is the BaseFont we are going to use for the header / footer
			private BaseFont bf = null;
			
			public MyPageEvents(MessageSourceAccessor messageSourceAccessor) {
				this.messageSourceAccessor = messageSourceAccessor;
			}

			// we override the onOpenDocument method
			public void onOpenDocument(PdfWriter writer, Document document) {
				try	{
					System.out.println("on open document ===========");
					bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
					cb = writer.getDirectContent();
					template = cb.createTemplate(50, 50);
					
					System.out.println("on applying header ===========");
					HeaderFooter header = new HeaderFooter(
				            new Phrase("This is a header without a page number"), false);
					header.setAlignment(Element.ALIGN_CENTER);
					document.setHeader(header);
					
					System.out.println("on applying footer ===========");
					HeaderFooter footer = new HeaderFooter(
				            new Phrase("This is page: "), true);
					footer.setBorder(Rectangle.NO_BORDER);
					footer.setAlignment(Element.ALIGN_CENTER);
					document.setFooter(footer);
					
				} catch (DocumentException de) {
				} catch (IOException ioe) {}
			}

			// we override the onEndPage method
			public void onEndPage(PdfWriter writer, Document document) {
				int pageN = writer.getPageNumber();
				String text = messageSourceAccessor.getMessage("page", "page") + " " + pageN + " " +
				    messageSourceAccessor.getMessage("on", "on") + " ";
				float  len = bf.getWidthPoint( text, 8 );
				cb.beginText();
				cb.setFontAndSize(bf, 8);

				cb.setTextMatrix(MARGIN, 16);
				cb.showText(text);
				cb.endText();

				cb.addTemplate(template, MARGIN + len, 16);
				cb.beginText();
				cb.setFontAndSize(bf, 8);

				cb.endText();
			}

			// we override the onCloseDocument method
			public void onCloseDocument(PdfWriter writer, Document document) {
				template.beginText();
				template.setFontAndSize(bf, 8);
				template.showText(String.valueOf( writer.getPageNumber() - 1 ));
				template.endText();
			}
		}
	
}