package patmal.course.enigma.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.dal.entity.MachineEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for MachineEntity database operations.
 */
@Repository
public interface MachineRepository extends JpaRepository<MachineEntity, UUID> {

    /**
     * Find machine by name
     */
    Optional<MachineEntity> findByName(String name);

    /**
     * Check if machine with given name exists
     */
    boolean existsByName(String name);
}

