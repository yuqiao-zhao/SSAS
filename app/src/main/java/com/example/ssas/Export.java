package com.example.ssas;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Export {

    private static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
    private static SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd hh:mm");
    private static int sheetindex = 0;
    private String filePath = "";
    private String filename = "";

//    data/user/0/com.example.ssas/files/
    public void exportRecords(String courseName, String semester, Date classTime, List<Record> recordList) throws IOException, WriteException, BiffException {
        String fileName = "data/data/com.example.ssas/" + courseName + "-" + semester.replace(" ", "") + ".xls";
        filePath = fileName;
        filename = courseName + "-" + semester.replace(" ", "") + ".xls";
        String sheetName = ft.format(classTime);
        String[] headers = new String[] {"registeredStudent", "signInTime", "status"};

        File exportfile = new File(fileName);
        WritableWorkbook book  = null;
        WritableSheet sheet = null;
        if(exportfile.exists()){
            Workbook rw = Workbook.getWorkbook(exportfile);
            book = Workbook.createWorkbook(exportfile, rw);

        }else{
            exportfile.createNewFile();
            book = Workbook.createWorkbook(exportfile);
        }

        if(book.getSheet(sheetName)!=null){
            sheet = book.getSheet(sheetName);
        }else{
            sheet = book.createSheet(sheetName, sheetindex);
            sheetindex += 1;

        }

        for(int i=0; i< headers.length; i++){
            Label lable = new Label(i, 0, headers[i]);
            sheet.addCell(lable);
        }

        for(int  i=1; i<=recordList.size(); i++){

            Record r = recordList.get(i-1);
            List<Label> labelList = new ArrayList<>();
            labelList.add(new Label(0, i, r.getRegisteredStudent().getStudentName()));
            labelList.add(new Label(1, i, ft1.format(r.getSignInTime())));
            labelList.add(new Label(2, i, r.getStatus()));

            for(Label l : labelList){
                sheet.addCell(l);
            }


        }

        book.write();
        book.close();


    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String fileName) {
        this.filename = fileName;
    }
}
