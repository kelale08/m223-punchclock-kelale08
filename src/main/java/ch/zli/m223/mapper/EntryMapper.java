package ch.zli.m223.mapper;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import ch.zli.m223.dto.EntryDetailDto;
import ch.zli.m223.dto.EntryDto;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.service.CategoryService;
import ch.zli.m223.service.TagService;

@ApplicationScoped
public class EntryMapper {

    @Inject
    CategoryService categoryService;

    @Inject
    TagService tagService;

    @Inject
    CategoryMapper categoryMapper;

    @Inject
    TagMapper tagMapper;

    public Entry toEntity(EntryDto dto) {
        Entry entry = new Entry();
        entry.setCheckIn(dto.getCheckIn());
        entry.setCheckOut(dto.getCheckOut());
        // findById wirft bei unbekannter ID eine NotFoundException -> HTTP 404
        if (dto.getCategoryId() != null) {
            entry.setCategory(categoryService.findById(dto.getCategoryId()));
        }
        Set<Tag> tags = new HashSet<>();
        if (dto.getTagIds() != null) {
            for (Long tagId : dto.getTagIds()) {
                tags.add(tagService.findById(tagId));
            }
        }
        entry.setTags(tags);
        return entry;
    }

    public EntryDto toDto(Entry entry) {
        EntryDto dto = new EntryDto();
        dto.setId(entry.getId());
        dto.setCheckIn(entry.getCheckIn());
        dto.setCheckOut(entry.getCheckOut());
        dto.setCategoryId(entry.getCategory() == null ? null : entry.getCategory().getId());
        dto.setTagIds(entry.getTags().stream()
                .map(Tag::getId)
                .sorted()
                .toList());
        return dto;
    }

    public EntryDetailDto toDetailDto(Entry entry) {
        EntryDetailDto dto = new EntryDetailDto();
        dto.setId(entry.getId());
        dto.setCheckIn(entry.getCheckIn());
        dto.setCheckOut(entry.getCheckOut());
        dto.setCategory(entry.getCategory() == null ? null : categoryMapper.toDto(entry.getCategory()));
        dto.setTags(entry.getTags().stream()
                .sorted(Comparator.comparing(Tag::getId))
                .map(tagMapper::toDto)
                .toList());
        return dto;
    }
}
