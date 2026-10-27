package ch.zli.m223.dto;

import jakarta.validation.constraints.NotBlank;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(readOnly = true)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
