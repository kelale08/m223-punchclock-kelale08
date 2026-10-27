package ch.zli.m223.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EntryDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(readOnly = true)
    private Long id;

    @NotNull(message = "Check-in is required")
    private LocalDateTime checkIn;

    @NotNull(message = "Check-out is required")
    private LocalDateTime checkOut;

    private Long categoryId;

    private List<Long> tagIds = new ArrayList<>();

    @AssertTrue(message = "Check-in must be before check-out")
    @JsonIgnore
    public boolean isCheckInBeforeCheckOut() {
        if (checkIn == null || checkOut == null) {
            // Fehlende Werte werden bereits durch @NotNull geprüft.
            return true;
        }
        return checkIn.isBefore(checkOut);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
