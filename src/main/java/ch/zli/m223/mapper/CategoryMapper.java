package ch.zli.m223.mapper;

import jakarta.enterprise.context.ApplicationScoped;

import ch.zli.m223.dto.CategoryDto;
import ch.zli.m223.model.Category;

@ApplicationScoped
public class CategoryMapper {

    public Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setTitle(dto.getTitle());
        return category;
    }

    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        return dto;
    }
}
