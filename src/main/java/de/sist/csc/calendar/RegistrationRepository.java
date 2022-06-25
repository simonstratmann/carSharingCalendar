package de.sist.csc.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {


    List<Registration> findAllByStartAfter(Instant instant);

    List<Registration> findAllByStartAfterAndEndBefore(Instant startAfter, Instant endBefore);
}
