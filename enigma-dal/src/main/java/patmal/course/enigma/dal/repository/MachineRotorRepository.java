package patmal.course.enigma.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.dal.entity.MachineEntity;
import patmal.course.enigma.dal.entity.MachineRotorEntity;

import java.util.List;
import java.util.UUID;

/**
 * Repository for MachineRotorEntity database operations.
 */
@Repository
public interface MachineRotorRepository extends JpaRepository<MachineRotorEntity, UUID> {

    /**
     * Find all rotors for a machine
     */
    List<MachineRotorEntity> findByMachine(MachineEntity machine);

    /**
     * Find all rotors for a machine by machine ID
     */
    List<MachineRotorEntity> findByMachineId(UUID machineId);
}

