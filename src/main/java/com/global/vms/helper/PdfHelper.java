package com.global.vms.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.global.vms.model.VisitorProfileResponse;
import com.global.vms.service.ImageService;
import com.global.vms.util.ImageUtils;
import com.global.vms.util.VMSUtil;

@Service
public class PdfHelper {

	@Autowired
	QRCodeHelper qrCodeHelper;

	@Autowired
	ImageService imageService;

	@Autowired
	Environment env;

	public byte[] generatePDF() throws MalformedURLException, IOException {
		Document document = new Document();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.open();
			Paragraph paragraph = new Paragraph();

			Chunk chunk = new Chunk("Hi John,");
			paragraph.add(chunk);
			paragraph.setSpacingAfter(20);
			document.add(paragraph);

			paragraph = new Paragraph();

			chunk = new Chunk(
					"Creating a pdf with a use of the iText library is based on manipulating objects implementing Elements interface in Document (in version 5.5.10 there are 45 of those implementations).The smallest element which can be added to the document and used is called Chunk, which is basically a string with applied font. Additionally, Chunk‘s can be combined with other elements like Paragraphs, Section etc. resulting in nice looking documents.");
			paragraph.add(chunk);
			paragraph.setSpacingAfter(20);
			document.add(paragraph);

			Image img = Image.getInstance(qrCodeHelper.getQRCodeImage("Sample Text", 200, 200));
			document.add(img);

			document.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return byteArrayOutputStream.toByteArray();
	}

	public byte[] generateGatePass(VisitorProfileResponse visitor) {
		Document document = new Document();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			byte[] qrCode = qrCodeHelper.getQRCodeImageForVisitor(visitor.getVisitorId());
			visitor.setQrCode(ImageUtils.imageToBase64(qrCode));
			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.open();

			PdfPTable table = new PdfPTable(3);
			PdfPCell cell = new PdfPCell(Image.getInstance(ImageUtils.base64ToImage(visitor.getPhoto())), true);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setRowspan(4);
			cell.setPaddingRight(20);
			cell.setPaddingTop(5);

			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(visitor.getName(), new Font(Font.FontFamily.HELVETICA, 20)));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingBottom(10);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(visitor.getEmail(), new Font(Font.FontFamily.HELVETICA, 16)));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingBottom(10);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(visitor.getMobile(), new Font(Font.FontFamily.HELVETICA, 16)));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingBottom(10);
			cell.setColspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(visitor.getVisitorType(), new Font(Font.FontFamily.HELVETICA, 16)));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			table.addCell(cell);

			PdfPTable innerTable = new PdfPTable(1);

			if (visitor.getReferredBy() != null && !visitor.getReferredBy().isEmpty()) {
				cell = new PdfPCell(new Paragraph("Referred By: " + visitor.getReferredBy(),
						new Font(Font.FontFamily.HELVETICA, 16)));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setPaddingBottom(5);
				innerTable.addCell(cell);
			}
			cell = new PdfPCell(new Paragraph(
					"Exit Time: " + VMSUtil.formatStringDate(visitor.getExpectedExit(),
							env.getProperty("dateformat.default"), env.getProperty("dateformat.ui")),
					new Font(Font.FontFamily.HELVETICA, 14)));
			cell.setBorder(Rectangle.NO_BORDER);

			innerTable.addCell(cell);

			cell = new PdfPCell(innerTable);
			cell.setColspan(2);
			cell.setRowspan(2);
			cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			innerTable = new PdfPTable(1);
			cell = new PdfPCell(Image.getInstance(qrCode), true);
			cell.setBorder(Rectangle.NO_BORDER);
			innerTable.addCell(cell);
			cell = new PdfPCell(
					new Paragraph(String.valueOf(visitor.getVisitorId()), new Font(Font.FontFamily.HELVETICA, 14)));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			innerTable.addCell(cell);

			cell = new PdfPCell(innerTable);
			cell.setPaddingLeft(20);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);

			PdfPTable outerTable = new PdfPTable(1);
			cell = new PdfPCell(table);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setPadding(10);
			outerTable.addCell(cell);
			document.add(outerTable);

			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArrayOutputStream.toByteArray();
	}
}
