package com.vw.visitreporting.reports;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.util.IConstants;

/**
 * @author FOX0BVI
 *
 */
public class OrganisationLevelsExcelView extends AbstractExcelView {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.document.AbstractExcelView#
	 * buildExcelDocument(java.util.Map,
	 * org.apache.poi.hssf.usermodel.HSSFWorkbook,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HSSFSheet sheet = workbook.createSheet(IConstants.STR_ORGANISATION_REP);

		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_CENTER);

		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setAlignment(CellStyle.ALIGN_CENTER);

		HSSFRow header = sheet.createRow(0);
		header.createCell((short) 0).setCellValue("");
		HSSFCell cell2 = header.createCell((short) 1);
		cell2.setCellStyle(style);
		cell2.setCellValue(IConstants.STR_HEADER_LEVELS);

		Region region = new Region(0, (short) 1, 0, (short) 5);
		sheet.addMergedRegion(region);

		HSSFRow firstRow = sheet.createRow(1);
		HSSFCell cell = firstRow.createCell((short) 0);
		cell.setCellStyle(style);
		cell.setCellValue(IConstants.STR_HEADER_ORGANISATION);

		Object[] levelArray = (Object[]) model.get(IConstants.STR_LEVELS);

		for (int i = 0; i < levelArray.length; i++) {
			Level level = (Level) levelArray[i];
			HSSFCell cell1 = firstRow.createCell((short) (i + 1));
			cell1.setCellStyle(style);
			cell1.setCellValue((short) level.getId().shortValue());
		}

		@SuppressWarnings("unchecked")
		List<Organisation> orgList = (List<Organisation>) model
				.get(IConstants.STR_ORGANISATIONS);
		int k = 2;
		for (Iterator iterator = orgList.iterator(); iterator.hasNext();) {

			HSSFRow secondRow = sheet.createRow(k);
			Organisation organisation = (Organisation) iterator.next();
			Set<Level> levelSet = organisation.getLevels();

			secondRow.createCell((short) 0)
					.setCellValue(organisation.getName());

			for (int i = 0; i < levelArray.length; i++) {
				Level level = (Level) levelArray[i];
				if (levelSet.contains(level)) {
					HSSFCell cell3 = secondRow.createCell((short) (i + 1));
					cell3.setCellStyle(style1);
					cell3.setCellValue(IConstants.STR_YES);
				} else {
					HSSFCell cell3 = secondRow.createCell((short) (i + 1));
					cell3.setCellStyle(style1);
					cell3.setCellValue(IConstants.STR_NO);
				}
			}
			k++;
		}
	}
}