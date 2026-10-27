package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.model.Category;
import ch.zli.m223.model.Entry;
import ch.zli.m223.repository.CategoryRepository;

@ApplicationScoped
public class CategoryService {
    @Inject
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.listAll();
    }

    public Category findById(Long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }
        return category;
    }

    @Transactional
    public Category createCategory(Category category) {
        categoryRepository.persist(category);
        return category;
    }

    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category existing = findById(id);
        existing.setTitle(category.getTitle());
        return existing;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category existing = findById(id);
        // Einträge behalten, nur die Verknüpfung entfernen (category_id = NULL)
        for (Entry entry : existing.getEntries()) {
            entry.setCategory(null);
        }
        categoryRepository.delete(existing);
    }
}
