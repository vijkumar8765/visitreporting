/**
 * 
 */
package com.vw.visitreporting.reports;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author FOX0BVI
 *
 */
public class CustomPdfReportGenerator extends PdfPageEventHelper {

	public void onEndPage (PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        switch(writer.getPageNumber() % 2) {
        case 0:
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase("even header"),
                    rect.getRight(), rect.getTop(), 0);
            break;
        case 1:
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("odd header"),
                    rect.getLeft(), rect.getTop(), 0);
            break;
        }
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(String.format("page %d", writer.getPageNumber())),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
    }

}
