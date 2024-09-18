package com.secure.notes.service;

import java.util.List;

import com.secure.notes.model.AuditLog;
import com.secure.notes.model.Note;

public interface AuditLogService {

	void auditLogCreation(String username, Note note);

	void auditLogUpdate(String username, Note note);


	void auditLogDeletion(String username, Long noteId);

	List<AuditLog> getAllAuditLog();

	AuditLog getAuditLogForNoteId(long id);

}
