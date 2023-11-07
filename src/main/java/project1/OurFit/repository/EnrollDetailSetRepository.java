package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.EnrollDetailSet;

import java.util.List;

public interface EnrollDetailSetRepository extends JpaRepository<EnrollDetailSet, Long> {

    List<EnrollDetailSet> findByEnrollDetailIdIn(List<Long> enrollDetailIds);
}
