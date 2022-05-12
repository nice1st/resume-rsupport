package com.bahwa.repository;

import com.bahwa.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryDsl {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Notice> findWithPessimisticLockById(Long id);
}
