package com.secure.notes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secure.notes.model.AuditLog;
import com.secure.notes.model.Note;
import com.secure.notes.repository.AuditLogRepository;

@Service
public class AuditLogServiceImpl implements AuditLogService{
	
	@Autowired
	private AuditLogRepository auditLogRepository;

	@Override
	public void auditLogCreation(String username, Note note) {
		
		AuditLog log=new AuditLog();
		log.setAction("CREATE");
		log.setUsername(username);
		log.setNoteId(note.getId());
		log.setNoteContent(note.getContent());
		log.setTimestamp(LocalDateTime.now());
		auditLogRepository.save(log);
		

	}
	@Override
	public void auditLogUpdate(String username, Note note) {
		AuditLog log=new AuditLog();
		log.setAction("UPDATE");
		log.setUsername(username);
		log.setNoteId(note.getId());
		log.setNoteContent(note.getContent());
		log.setTimestamp(LocalDateTime.now());
		auditLogRepository.save(log);
	}
	@Override
	public void auditLogDeletion(String username, Long noteId) {
		AuditLog log=new AuditLog();
		log.setAction("DELETE");
		log.setUsername(username);
		log.setNoteId(noteId);
		log.setTimestamp(LocalDateTime.now());
		auditLogRepository.save(log);
	}
	@Override
	public List<AuditLog> getAllAuditLog() {
		// TODO Auto-generated method stub
		return auditLogRepository.findAll();
	}
	@Override
	public AuditLog getAuditLogForNoteId(long id) {
		// TODO Auto-generated method stub
		return auditLogRepository.findByNoteId(id);
	}

}
