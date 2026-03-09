package patmal.course.enigma.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.dal.entity.MachineEntity;
import patmal.course.enigma.dal.entity.MachineReflectorEntity;

import java.util.List;
import java.util.UUID;

/**
 * Repository for MachineReflectorEntity database operations.
 */
@Repository
public interface MachineReflectorRepository extends JpaRepository<MachineReflectorEntity, UUID> {

    /**
     * Find all reflectors for a machine
     */
    List<MachineReflectorEntity> findByMachine(MachineEntity machine);

    /**
     * Find all reflectors for a machine by machine ID
     */
    List<MachineReflectorEntity> findByMachineId(UUID machineId);

    /**
     * Insert reflector using native query to handle PostgreSQL enum type
     */
    @Modifying
    @Query(value = "INSERT INTO machines_reflectors (id, machine_id, reflector_id, input, output) " +
                   "VALUES (:id, :machineId, CAST(:reflectorId AS reflector_id_enum), :input, :output)",
           nativeQuery = true)
    void insertReflector(@Param("id") UUID id,
                         @Param("machineId") UUID machineId,
                         @Param("reflectorId") String reflectorId,
                         @Param("input") String input,
                         @Param("output") String output);
}

