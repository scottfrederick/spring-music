package org.cloudfoundry.samples.music.web.controllers;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/albums")
public class AlbumController {
    private AlbumRepository repository;

    @Autowired
    public AlbumController(AlbumRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Album> albums() {
        return repository.findAll();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Long add(@RequestBody @Valid Album album) {
        return repository.save(album).getId();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Long update(@RequestBody @Valid Album album) {
        return repository.save(album).getId();
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Album getById(@PathVariable Long id) {
        return repository.findOne(id);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable Long id) {
        repository.delete(id);
    }
}