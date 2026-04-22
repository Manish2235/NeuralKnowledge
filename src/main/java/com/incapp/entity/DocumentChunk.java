package com.incapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class DocumentChunk {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(length = 1000000, nullable = false )
	    private String content;
	    
	    @Column(length = 100, nullable = false )
	    private String name;
	    
	    @Column(nullable = false ,columnDefinition = "longblob")
	    private byte[] document;
	    @ManyToOne
	    private MyUser user;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public byte[] getDocument() {
			return document;
		}
		public void setDocument(byte[] document) {
			this.document = document;
		}
		public MyUser getUser() {
			return user;
		}
		public void setUser(MyUser user) {
			this.user = user;
		}
	    
}
