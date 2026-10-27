package ch.zli.m223.mapper;

import jakarta.enterprise.context.ApplicationScoped;

import ch.zli.m223.dto.TagDto;
import ch.zli.m223.model.Tag;

@ApplicationScoped
public class TagMapper {

    public Tag toEntity(TagDto dto) {
        Tag tag = new Tag();
        tag.setTitle(dto.getTitle());
        return tag;
    }

    public TagDto toDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setTitle(tag.getTitle());
        return dto;
    }
}
