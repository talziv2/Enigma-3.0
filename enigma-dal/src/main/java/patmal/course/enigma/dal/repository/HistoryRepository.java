package patmal.course.enigma.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.dal.entity.HistoryEntity;
import patmal.course.enigma.dal.entity.SessionEntity;

import java.util.List;

/**
 * Repository for HistoryEntity database operations.
 */
@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

    /**
     * Find all history entries for a session
     */
    List<HistoryEntity> findBySession(SessionEntity session);

    /**
     * Find all history entries for a session by session ID string
     * Uses custom JPQL query (Java Persistence Query Language)
     */
    @Query("SELECT h FROM HistoryEntity h WHERE h.session.sessionId = :sessionId ORDER BY h.processedAt")
    List<HistoryEntity> findBySessionId(@Param("sessionId") String sessionId);

    /**
     * Find all history entries for a machine by machine name
     * This gets history from ALL sessions that used this machine
     */
    @Query("SELECT h FROM HistoryEntity h WHERE h.session.machine.name = :machineName ORDER BY h.processedAt")
    List<HistoryEntity> findByMachineName(@Param("machineName") String machineName);

    /**
     * Count how many operations were performed in a session
     */
    long countBySession(SessionEntity session);
}

