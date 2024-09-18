package com.secure.notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.secure.notes.model.Note;
import com.secure.notes.repository.NoteRepository;


@Service

public class NoteServiceImpl  implements NoteService{
	
	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	private AuditLogService auditLogService;

	@Override
	public Note createNoteForUser(String username, String content) {
		// TODO Auto-generated method stub
		Note note=new Note();
		note.setContent(content);
		note.setOwnUserName(username);
		Note savednote= noteRepository.save(note);
		auditLogService.auditLogCreation(username, savednote);
		return savednote;

		
	}

	@Override
	public List<Note> getNoteUser(String username) {
		// TODO Auto-generated method stub
		return noteRepository.findByOwnUserName(username);
	}

	@Override
	public Note updateNoteForUser(Long id, String username, String content) {
		// TODO Auto-generated method stub
		Note note=noteRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("note has not found"));
		note.setContent(content);
		note.setOwnUserName(username);
		auditLogService.auditLogUpdate(username, note);
		return noteRepository.save(note);
	}

	@Override
	public void deleteNoteForUser(Long id, String username) {
		// TODO Auto-generated method stub
		Note note=noteRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("note has not found"));
       auditLogService.auditLogDeletion(username, id);
		noteRepository.deleteById(id);
		
	}
	


}
