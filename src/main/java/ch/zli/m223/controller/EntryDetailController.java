package ch.zli.m223.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.EntryDetailDto;
import ch.zli.m223.mapper.EntryMapper;
import ch.zli.m223.service.EntryService;

@Path("/entry-details")
@Tag(name = "Entry Details", description = "Entries with their category and tags")
public class EntryDetailController {

    @Inject
    EntryService entryService;

    @Inject
    EntryMapper entryMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all entries with details.", description = "Returns all entries with their category and tags.")
    public List<EntryDetailDto> index() {
        return entryService.findAll().stream()
                .map(entryMapper::toDetailDto)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Shows one entry with details.", description = "Returns the entry with the given id, or 404 if it does not exist.")
    public EntryDetailDto show(@PathParam("id") Long id) {
        return entryMapper.toDetailDto(entryService.findById(id));
    }
}
