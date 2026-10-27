package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.model.Entry;
import ch.zli.m223.repository.EntryRepository;

@ApplicationScoped
public class EntryService {
    @Inject
    private EntryRepository entryRepository;

    @Transactional
    public Entry createEntry(Entry entry) {
        entryRepository.persist(entry);
        return entry;
    }

    @Transactional
    public void deleteEntry(Long id) {
        if (!entryRepository.deleteById(id)) {
            throw new NotFoundException();
        }
    }

    @Transactional
    public Entry updateEntry(Long id, Entry entry) {
        Entry existing = entryRepository.findById(id);
        if (existing == null) {
            throw new NotFoundException();
        }
        existing.setCheckIn(entry.getCheckIn());
        existing.setCheckOut(entry.getCheckOut());
        existing.setCategory(entry.getCategory());
        existing.setTags(entry.getTags());
        return existing;
    }

    public List<Entry> findAll() {
        return entryRepository.listAll();
    }

    public Entry findById(Long id) {
        Entry entry = entryRepository.findById(id);
        if (entry == null) {
            throw new NotFoundException("Entry not found");
        }
        return entry;
    }
}
