package com.incapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incapp.entity.DocumentChunk;
import com.incapp.entity.MyUser;
import com.incapp.repo.DocumentRepo;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepo documentRepo;
	@Autowired
	private UserService userService;
	
	public String save(MultipartFile file, MyUser user) {
		String name=file.getOriginalFilename();
		try {
			InputStream is=file.getInputStream();
			System.out.println(is);
			byte doc[]=is.readAllBytes();
			System.out.println(doc.length);
			if(doc.length > 100*1000*1000) {
				return "SizeExceed";
			}else {
				Tika t=new Tika();
				String content=t.parseToString(file.getInputStream()).trim();
				DocumentChunk d=new DocumentChunk();
				d.setName(name);
				d.setContent(content);
				d.setDocument(doc);
				d.setUser(user);
				documentRepo.save(d);
				return "Success";
			}
		} catch (IOException | TikaException e) {
			e.printStackTrace();
			return "Failed";
		}
	}

	public List<DocumentChunk> myDocument(String userName) {
		
		List<DocumentChunk> chunks=documentRepo.findAllByUserUsername(userName);
		
		return chunks;
	}

	public DocumentChunk getDocument(Long id) {
		
		return documentRepo.findById(id).orElse(null);
	}

	public void delete(Long id) {
		documentRepo.deleteById(id);
	}
}

