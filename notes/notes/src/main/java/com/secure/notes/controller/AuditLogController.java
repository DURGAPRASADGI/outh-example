package com.secure.notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secure.notes.model.AuditLog;
import com.secure.notes.service.AuditLogService;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {
	
	@Autowired
	private AuditLogService auditLogService;
	
	
	@GetMapping
	public List<AuditLog> getAllAuditLog(){
		return auditLogService.getAllAuditLog();
	}
	
	@GetMapping("/note/{id}")
	public AuditLog getAuditLog(@PathVariable long id) {
		return auditLogService.getAuditLogForNoteId(id);
		
	}

}
