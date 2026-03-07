package patmal.course.enigma.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.dal.entity.MachineEntity;

import java.util.Optional;

/**
 * Repository for MachineEntity database operations.
 *
 * Extends JpaRepository which provides:
 * - save(), findById(), findAll(), delete(), etc.
 *
 * Custom methods below are automatically implemented by Spring Data JPA
 * based on the method name!
 */
@Repository
public interface MachineRepository extends JpaRepository<MachineEntity, Long> {

    /**
     * Find machine by name
     * Spring automatically creates: SELECT * FROM machines WHERE name = ?
     */
    Optional<MachineEntity> findByName(String name);

    /**
     * Check if machine with given name exists
     * Spring automatically creates: SELECT EXISTS(SELECT 1 FROM machines WHERE name = ?)
     */
    boolean existsByName(String name);
}

