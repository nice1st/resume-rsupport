package com.bahwa.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.bahwa.dto.FileDto;
import com.bahwa.entity.Attachments;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.properties.FileProperties;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileUtilities {

	private final FileProperties fileProperties;

	public FileDto uploadFile(MultipartFile multipartFile) throws Exception {

		String now = new SimpleDateFormat("yyMMdd_hhmmss").format(new Date());
		String originFileName = multipartFile.getOriginalFilename();
		String fileName = now + "_" + originFileName;
		String filePath = Paths.get(fileProperties.getRoot(), fileName).toString();

		File file = new File(filePath);
		multipartFile.transferTo(file);

		// 파일 권한 설정(쓰기, 읽기)
		file.setWritable(true);
		file.setReadable(true);

		return FileDto.builder()
			.originFileName(originFileName)
			.fileName(fileName)
			.filePath(filePath)
			.fileSize(multipartFile.getSize())
			.build();
	}

	public void removeFile(String path) {

		File file = new File(path);

		try {
			file.delete();
		} catch (Exception e) {
			// 삭제 실패
		}
	}

	public File getFile(String path) throws Exception {

		return new File(path);
	}
}