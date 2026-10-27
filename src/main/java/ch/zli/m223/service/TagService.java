package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.repository.TagRepository;

@ApplicationScoped
public class TagService {
    @Inject
    private TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.listAll();
    }

    public Tag findById(Long id) {
        Tag tag = tagRepository.findById(id);
        if (tag == null) {
            throw new NotFoundException("Tag not found");
        }
        return tag;
    }

    @Transactional
    public Tag createTag(Tag tag) {
        tagRepository.persist(tag);
        return tag;
    }

    @Transactional
    public Tag updateTag(Long id, Tag tag) {
        Tag existing = findById(id);
        existing.setTitle(tag.getTitle());
        return existing;
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag existing = findById(id);
        // Zeilen in der Zwischentabelle entry_tag entfernen
        for (Entry entry : existing.getEntries()) {
            entry.getTags().remove(existing);
        }
        tagRepository.delete(existing);
    }
}
