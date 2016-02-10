package com.vw.visitreporting.reports;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class CustomPdfView extends AbstractPdfView {


	private static final Font HEADLINE_FONT = new Font( Font.HELVETICA, 18, Font.BOLD, Color.blue );
	private static final Font HEADING_FONT = new Font( Font.HELVETICA, 12, Font.ITALIC, Color.black );
	private static final Font HEADING_DATA_FONT = new Font( Font.HELVETICA, 12, Font.ITALIC, Color.blue );
	private static final Font DATA_HEAD_FONT = new Font( Font.HELVETICA, 10, Font.ITALIC, Color.black );
	private static final Font TEXT_FONT = new Font( Font.TIMES_ROMAN, 8, Font.NORMAL, Color.black );
	private static final Font BOLD_FONT = new Font( Font.TIMES_ROMAN, 8, Font.BOLD, Color.black );
	private static final int MARGIN = 32;
	
	// table properties
	private static final int TABLE_WIDTH_PERCENTAGE = 50;
	private static final int TABLE_BORDER_WIDTH = 1;
	private static final int TABLE_PADDING = 4;
	
	public static PdfPTable formatTable(int columns){
		
		PdfPTable table = new PdfPTable(columns);
		table.setWidthPercentage(TABLE_WIDTH_PERCENTAGE);
		table.getDefaultCell().setBorderWidth(TABLE_BORDER_WIDTH);
		table.getDefaultCell().setBorderColor(Color.black);
		table.getDefaultCell().setPadding(TABLE_PADDING);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment( Element.ALIGN_MIDDLE);

		return table;
	}


	public void eventHandler(PdfWriter writer, Document document){
		
		// We create and add the event handler.
		// So we can well paging, ensuring that only entire cells are printed
		// at end of pages (the latter is useless in this example as records
		// keep in one row, but in your own developpment it's not always the case).
		MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
		writer.setPageEvent(events);
		events.onOpenDocument(writer, document);

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
					bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
					cb = writer.getDirectContent();
					template = cb.createTemplate(50, 50);
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

		@Override
		protected void buildPdfDocument(Map<String, Object> model,
				Document document, PdfWriter writer,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			// TODO Auto-generated method stub
			
		}

}