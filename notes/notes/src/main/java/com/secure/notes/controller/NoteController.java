package com.secure.notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secure.notes.model.Note;
import com.secure.notes.service.NoteService;

@RestController
@RequestMapping("/api/note")
public class NoteController {
	
	@Autowired
	private NoteService noteService;
	
	@PostMapping
	public Note createNoteForUser(Authentication authentication,@RequestBody String content) {
		UserDetails userDetails=(UserDetails) authentication.getPrincipal();
		return noteService.createNoteForUser(userDetails.getUsername(), content);
		
	}
	
	@GetMapping
	public List<Note> getNoteForUser(@AuthenticationPrincipal UserDetails userDetails){
		List<Note> notes=noteService.getNoteUser(userDetails.getUsername());
		System.out.println(notes);
		return notes;
		
	}
	
	@PutMapping("/{id}")
	public Note updateNoteForUser(@PathVariable long id,@AuthenticationPrincipal UserDetails  userDetails,@RequestBody String content) {
		return noteService.updateNoteForUser(id, userDetails.getUsername(), content);
	}

	@DeleteMapping("/{id}")
	public void deleteNoteForUser(@PathVariable long id,@AuthenticationPrincipal UserDetails userDetails) {
		noteService.deleteNoteForUser(id, userDetails.getUsername());
	}
}
