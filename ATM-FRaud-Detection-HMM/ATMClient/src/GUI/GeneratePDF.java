package GUI;

import com.itextpdf.text.BaseColor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.StringTokenizer;

public class GeneratePDF {

    public GeneratePDF(String data) {
        try {
            
            data=data.substring(6,data.length()-1);
            OutputStream file = new FileOutputStream(new File("print.pdf"));
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, file);
            document.open();
            PdfPTable table = new PdfPTable(4);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("MINI STATEMENT"));
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            //
            cell = new PdfPCell(new Phrase("Acc No"));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            //
            cell = new PdfPCell(new Phrase("Tr_Date"));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            //
            cell = new PdfPCell(new Phrase("Tr_Type"));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            //
            cell = new PdfPCell(new Phrase("Amount"));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            StringTokenizer st = new StringTokenizer(data, ";");
            String amt;
            String dt;
            String ttype;

            while (st.hasMoreTokens()) {
                //
                amt = st.nextToken();
                dt = st.nextToken();
                ttype = st.nextToken();
                cell = new PdfPCell(new Phrase(MainFrame.Acc_no));
                table.addCell(cell);
                //
                cell = new PdfPCell(new Phrase(dt));
                table.addCell(cell);
                //
                if (ttype.toLowerCase().equals("w")) {
                    ttype = "Withdraw";
                } else {
                    ttype = "Deposite";
                }
                cell = new PdfPCell(new Phrase(ttype));
                table.addCell(cell);
                //
                cell = new PdfPCell(new Phrase(amt));
                table.addCell(cell);
            }
            document.add(table);
            document.close();
            file.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}