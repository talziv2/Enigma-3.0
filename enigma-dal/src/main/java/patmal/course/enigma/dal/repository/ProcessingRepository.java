package patmal.course.enigma.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.dal.entity.MachineEntity;
import patmal.course.enigma.dal.entity.ProcessingEntity;

import java.util.List;
import java.util.UUID;

/**
 * Repository for ProcessingEntity database operations.
 */
@Repository
public interface ProcessingRepository extends JpaRepository<ProcessingEntity, UUID> {

    /**
     * Find all processing entries for a session
     */
    List<ProcessingEntity> findBySessionId(String sessionId);

    /**
     * Find all processing entries for a machine
     */
    List<ProcessingEntity> findByMachine(MachineEntity machine);

    /**
     * Find all processing entries for a machine by machine ID
     */
    List<ProcessingEntity> findByMachineId(UUID machineId);

    /**
     * Find all processing entries for a machine by machine name
     */
    @Query("SELECT p FROM ProcessingEntity p WHERE p.machine.name = :machineName ORDER BY p.time")
    List<ProcessingEntity> findByMachineName(@Param("machineName") String machineName);

    /**
     * Count processing entries for a session
     */
    long countBySessionId(String sessionId);

    /**
     * Insert processing record using native query
     */
    @Modifying
    @Query(value = "INSERT INTO processing (id, machine_id, session_id, code, input, output, time) " +
                   "VALUES (:id, :machineId, :sessionId, :code, :input, :output, :time)",
           nativeQuery = true)
    void insertProcessing(@Param("id") UUID id,
                          @Param("machineId") UUID machineId,
                          @Param("sessionId") String sessionId,
                          @Param("code") String code,
                          @Param("input") String input,
                          @Param("output") String output,
                          @Param("time") long time);
}

