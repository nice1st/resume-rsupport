package com.bahwa.repository;

import java.util.List;

import com.bahwa.entity.Attachments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {
    
    public List<Attachments> findAllByNoticeId(long noticeId);
    
    public void deleteByNoticeId(long noticeId);
}
