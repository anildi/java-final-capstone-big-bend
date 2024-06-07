package trackService.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackService.exception.AlbumNotFoundException;
import trackService.model.track.Track;
import trackService.service.TrackService;

import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/track")

public class TrackController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private UriCreator uriCreator;

    @GetMapping
    public ResponseEntity<List<Track>> getAllTracks() {
        List<Track> tracks = trackService.getAll();

        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrack(@PathVariable int id) {
        Track resultTrack =  trackService.getTrackById(id);
        if (resultTrack == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultTrack);
    }

    @PostMapping
    public ResponseEntity<?> createTrack(@RequestBody Track track) {
        try {
            Track resultTrack = this.trackService.create(track);
            URI newResource = uriCreator.getURI(resultTrack.getId());
            return ResponseEntity.created(newResource).body(resultTrack);
        } catch (AlbumNotFoundException e)  {
           return ResponseEntity.badRequest().body("album not found");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTrack(@RequestBody Track track) {
        boolean updated = this.trackService.updateTrack(track);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        URI newResource = uriCreator.getURI(track.getId());
        return ResponseEntity.ok(newResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrack(@PathVariable("id") int id) {
        boolean updated = this.trackService.deleteTrack(id);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        URI newResource = uriCreator.getURI(id);
        return ResponseEntity.ok(newResource);
    }


}