package app.repositories;

import app.models.Report;
import app.models.Warehouse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportJPARepositoryTests {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ReportJPARepository reportRepo;

    // FIRST
    @Test
    void findAll() {

        // Arrange
        TypedQuery<Report> query = mock(TypedQuery.class);
        List<Report> newReports = Arrays.asList(
                new Report(1, "Test", "2024-01-23", 1, "Tobi", 2),
                new Report(2, "Test", "2024-01-23", 1, "Tobi", 2));

        when(entityManager.createQuery("SELECT r FROM Report r", Report.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(newReports);

        // Act
        List<Report> actualReports = reportRepo.findAll();

        // Assert
        assertEquals(newReports, actualReports);
        verify(query).getResultList();
    }

    // FIRST
    @Test
    void findById() {

        // Arrange
        long reportId = 1;
        Report expectedReport = new Report(3, "Test", "2024-01-23", 1, "Tobi", 2);

        when(entityManager.find(Report.class, reportId)).thenReturn(expectedReport);

        // Act
        Report actualReport = reportRepo.findById(reportId);

        // Assert
        assertEquals(expectedReport, actualReport);
        verify(entityManager).find(Report.class, reportId);
    }

    // FIRST
    @Test
    void delete() {

        // Arrange
        long reportIdToDelete = 1;
        Report reportToDelete = new Report(reportIdToDelete, "Test", "2024-01-23", 1, "Tobi", 2);

        // Act
        Report deletedReport = reportRepo.delete(reportToDelete);

        // Assert
        assertNotNull(deletedReport);
        assertEquals(reportToDelete, deletedReport);

        // Verify
        verify(entityManager).remove(reportToDelete);
        verifyNoMoreInteractions(entityManager);
    }

}