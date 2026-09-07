package com.priyanshu.iims.dto;
import jakarta.validation.constraints.Size;
import com.priyanshu.iims.model.enums.*;
public class IncidentUpdateRequest {
	@Size(max=150, message="Title cannot exceed 150 characters")
	private String title;
	
	@Size(max=2000, message="Description cannot exceed 2000 characters")
	private String description;
	
	private IncidentPriority priority;
	private IncidentStatus status;
	private IncidentCategory category;
	
	
	public IncidentUpdateRequest() {
		
	}
	
	public String getTitle() {
		return title;
	}
	public void steTitle(String title) {
		this.title=title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description=description;
	}
	public IncidentPriority getPriority() {
		return priority;
	}
	
	public void setPriority(IncidentPriority priority) {
		this.priority=priority;
	}
	
	public IncidentStatus getStatus() {
		return status;
	}
	public void setStatus(IncidentStatus status) {
		this.status=status;
	}
	
	public IncidentCategory getCategory() {
		return category;
	}
	
	public void setCategory(IncidentCategory category) {
		this.category=category;
	}
	

}
