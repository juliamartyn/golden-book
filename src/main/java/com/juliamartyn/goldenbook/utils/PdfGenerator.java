package com.juliamartyn.goldenbook.utils;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

@Service
public class PdfGenerator {

    public void createPdfFile(String fileTemplateName, String outFileName, Map<String, Object> parameters) throws JRException, FileNotFoundException {
        JasperReport jasperDesign = JasperCompileManager.compileReport(fileTemplateName);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameters, new JREmptyDataSource());

        File file = new File(outFileName);
        OutputStream outputStream = new FileOutputStream(file);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }
}
