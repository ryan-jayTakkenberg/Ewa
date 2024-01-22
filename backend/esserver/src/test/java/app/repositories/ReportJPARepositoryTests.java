package app.repositories;

import app.models.Report;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://oege.ie.hva.nl:3306/zkoktj_test?createDatabaseIfNotExist=true",
        "spring.datasource.username=koktj",
        "spring.datasource.password=Gse1qSX.FUm$NJ+F",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
class ReportJPARepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReportJPARepository reportRepo;

    @Test
    void findAll() {
        Report report1 = new Report(1, "Test body 1", "01/01/2022", 1, "User1", 2);
        Report report2 = new Report(2, "Test body 2", "02/01/2022", 2, "User2", 3);

        entityManager.persist(report1);
        entityManager.persist(report2);
        entityManager.flush();

        List<Report> reports = reportRepo.findAll();

        assertNotNull(reports);
        assertEquals(2, reports.size());
    }

    @Test
    void findById() {
        Report report = new Report(1, "Test body", "01/01/2022", 1, "User1", 2);
        entityManager.persist(report);
        entityManager.flush();

        Report foundReport = reportRepo.findById(report.getId());

        assertNotNull(foundReport);
        assertEquals(report.getBody(), foundReport.getBody());
    }

    @Test
    void save() {
        Report report = new Report(1, "Test body", "01/01/2022", 1, "User1", 2);

        Report savedReport = reportRepo.save(report);

        assertNotNull(savedReport);
        assertEquals(report.getBody(), savedReport.getBody());

        Report foundReport = entityManager.find(Report.class, savedReport.getId());
        assertNotNull(foundReport);
    }

    @Test
    void delete() {
        Report report = new Report(1, "Test body", "01/01/2022", 1, "User1", 2);
        entityManager.persist(report);
        entityManager.flush();

        Report deletedReport = reportRepo.delete(report);

        assertNotNull(deletedReport);
        assertEquals(report.getBody(), deletedReport.getBody());

        Report foundReport = entityManager.find(Report.class, deletedReport.getId());
        assertNull(foundReport);
    }
}