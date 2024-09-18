package com.secure.notes.service;

import java.util.List;

import com.secure.notes.model.Note;

public interface NoteService {
	
	public Note createNoteForUser(String username,String content);
	
	public List<Note> getNoteUser(String username);
	
	public Note updateNoteForUser(Long id,String username,String content);
	
	public void deleteNoteForUser(Long id,String username);
	

}
