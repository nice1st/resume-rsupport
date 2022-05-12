package com.bahwa.repository;

import com.bahwa.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryHint;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryDsl {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Notice> findWithPessimisticLockById(Long id);
}
