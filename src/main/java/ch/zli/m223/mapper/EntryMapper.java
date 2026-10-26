package ch.zli.m223.mapper;

import jakarta.enterprise.context.ApplicationScoped;

import ch.zli.m223.dto.EntryDto;
import ch.zli.m223.model.Entry;

@ApplicationScoped
public class EntryMapper {

    public Entry toEntity(EntryDto dto) {
        Entry entry = new Entry();
        entry.setCheckIn(dto.getCheckIn());
        entry.setCheckOut(dto.getCheckOut());
        return entry;
    }

    public EntryDto toDto(Entry entry) {
        EntryDto dto = new EntryDto();
        dto.setId(entry.getId());
        dto.setCheckIn(entry.getCheckIn());
        dto.setCheckOut(entry.getCheckOut());
        return dto;
    }
}
