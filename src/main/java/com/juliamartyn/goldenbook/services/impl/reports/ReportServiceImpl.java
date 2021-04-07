package com.juliamartyn.goldenbook.services.impl.reports;

import com.juliamartyn.goldenbook.controllers.request.SoldBooksReportRequest;
import com.juliamartyn.goldenbook.repository.BookCategoryRepository;
import com.juliamartyn.goldenbook.repository.BookRepository;
import com.juliamartyn.goldenbook.services.ReportService;
import com.juliamartyn.goldenbook.utils.PdfGenerator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.now;


@Service
public class ReportServiceImpl implements ReportService {

    @Value("${REPORT_REPOSITORY}")
    private String reportRepository;

    private final PdfGenerator pdfGenerator;
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;

    public ReportServiceImpl(PdfGenerator pdfGenerator, BookRepository bookRepository, BookCategoryRepository bookCategoryRepository) {
        this.pdfGenerator = pdfGenerator;
        this.bookRepository = bookRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @Override
    public File generateSoldBooksReport(SoldBooksReportRequest soldBooksReportRequest) throws FileNotFoundException, JRException {
        List<SoldBooksReportValues> totalValues = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<String, Object>();

        String startDate = soldBooksReportRequest.getStartDate().toString();
        String endDate = soldBooksReportRequest.getEndDate().toString();

        for(int i = 1; i <= bookCategoryRepository.findAll().size(); i++){
            totalValues.add(bookRepository.getTotalValuesOfSoldBooksByCategory(i, startDate, endDate));
        }

        parameters.put("dataSource", new JRBeanCollectionDataSource(totalValues));
        parameters.put("fromDate", startDate);
        parameters.put("toDate", endDate);

        String outFileName = reportRepository + "GoldenBookReport" + now() + ".pdf";
        return pdfGenerator.createPdfFile("src/main/resources/templates/sold-books-report-template.jrxml", outFileName, parameters);
    }
}
