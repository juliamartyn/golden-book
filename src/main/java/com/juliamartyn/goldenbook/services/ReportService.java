package com.juliamartyn.goldenbook.services;

import com.juliamartyn.goldenbook.controllers.request.SoldBooksReportRequest;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.FileNotFoundException;

public interface ReportService {
    File generateSoldBooksReport(SoldBooksReportRequest soldBooksReportRequest) throws FileNotFoundException, JRException;
}
