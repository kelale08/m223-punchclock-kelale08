package ch.zli.m223.controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.TagDto;
import ch.zli.m223.mapper.TagMapper;
import ch.zli.m223.service.TagService;

@Path("/tags")
@Tag(name = "Tags", description = "Handling of tags")
public class TagController {

    @Inject
    TagService tagService;

    @Inject
    TagMapper tagMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all Tags.", description = "Returns a list of all tags.")
    public List<TagDto> index() {
        return tagService.findAll().stream()
                .map(tagMapper::toDto)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Shows one tag.", description = "Returns the tag with the given id.")
    public TagDto show(@PathParam("id") Long id) {
        return tagMapper.toDto(tagService.findById(id));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new tag.", description = "Creates a new tag and returns the newly added tag.")
    public TagDto create(@NotNull @Valid TagDto dto) {
        return tagMapper.toDto(tagService.createTag(tagMapper.toEntity(dto)));
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates a tag.", description = "Updates the tag with the given id and returns the updated tag.")
    public TagDto update(@PathParam("id") Long id, @NotNull @Valid TagDto dto) {
        return tagMapper.toDto(tagService.updateTag(id, tagMapper.toEntity(dto)));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletes a tag.", description = "Deletes the tag with the given id.")
    public void delete(@PathParam("id") Long id) {
        tagService.deleteTag(id);
    }
}
