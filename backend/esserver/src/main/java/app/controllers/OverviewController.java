package app.controllers;
import app.models.Report;
import app.repositories.ProjectJPARepository;

//import app.repositories.ProductJPARepository;
//import app.repositories.TeamJPARepository;
//import app.repositories.WarehouseJPARepository;
// Import other repositories as needed

import app.repositories.ReportJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adminview")
public class OverviewController {

//    @Autowired
//    private ProductJPARepository productJPARepository;
//    @Autowired
//    private WarehouseJPARepository warehouseJPARepository;
//    @Autowired
//    private TeamJPARepository teamJPARepository;
    @Autowired
    ReportJPARepository reportJPARepository;
    @Autowired
    ProjectJPARepository projectJPARepository;

    @GetMapping("count")
    public Long getTeamsCount() {
        // Fetches the count of teams (or projects, based on the repository used)
        return projectJPARepository.count();
    }
    @GetMapping("/viewreports")
    public List<Report> getViewReports() {
        return reportJPARepository.findAll();
    }

    // Autowire other repositories as needed

//    @GetMapping("/products/count")
//    public Long getProductsCount() {
//        // Fetches the count of products
//        return productJPARepository.count();
//    }
//
//    @GetMapping("/warehouses/count")
//    public Long getWarehousesCount() {
//        // Fetches the count of warehouses
//        return warehouseJPARepository.count();
//    }

}
