package com.bahwa.service;

import com.bahwa.repository.AttachmentsRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttachmentsServiceTest {
    
    @InjectMocks
    private AttachmentsService target;
    
    @Mock
    private AttachmentsRepository attachmentsRepository;

    @Test
    public void 등록() {
        
    }

    @Test
    public void 조회() {
        
    }

    @Test
    public void 삭제() {
        
    }
}
