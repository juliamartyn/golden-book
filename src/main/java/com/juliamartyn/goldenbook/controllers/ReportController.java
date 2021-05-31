package com.juliamartyn.goldenbook.controllers;

import com.juliamartyn.goldenbook.controllers.request.SoldBooksReportRequest;
import com.juliamartyn.goldenbook.services.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.springframework.http.MediaType.APPLICATION_PDF;

@RestController
@RequestMapping("api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PostMapping(path = "/sold-books")
    public ResponseEntity<InputStreamResource> soldBooksReport(@RequestBody SoldBooksReportRequest soldBooksReportRequest) throws FileNotFoundException, JRException {
        reportService.generateSoldBooksReport(soldBooksReportRequest);

        final File reportPdf = reportService.generateSoldBooksReport(soldBooksReportRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_PDF);
        headers.setContentLength(reportPdf.length());
        headers.setContentDispositionFormData("attachment", reportPdf.getName());

        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(reportPdf)), headers, HttpStatus.OK);
    }
}
