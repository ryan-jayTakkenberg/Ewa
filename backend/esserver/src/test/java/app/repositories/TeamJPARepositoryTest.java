package app.repositories;

import app.models.Team;
import app.models.Warehouse;
import app.repositories.TeamJPARepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamJPARepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TeamJPARepository teamRepository;

    @Test
    void findByIdShouldReturnTeamWithID() {
        // Arrange
        long teamId = 1902;
        Warehouse warehouse = new Warehouse(1, "Test", "Amsterdam", "Bijlmer", "9012 AF", 100, 20, 0);
        Team expectedTeam = new Team("Team Green", warehouse);

        when(entityManager.find(Team.class, teamId)).thenReturn(expectedTeam);

        // Act
        Optional<Team> actualTeam = Optional.ofNullable(teamRepository.findById(teamId));

        // Assert
        assertEquals(Optional.of(expectedTeam), actualTeam);
        verify(entityManager).find(Team.class, teamId);
    }
}
