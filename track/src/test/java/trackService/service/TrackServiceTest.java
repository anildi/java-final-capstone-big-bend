package trackService.service;

import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import trackService.model.track.Track;
import trackService.model.track.TrackBuilder;

import java.time.LocalDate;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TrackServiceTest {

    @Autowired
    TrackService trackService;

    @MockBean
    PricingClient pricingClient;

    @BeforeEach
    public void setup() {
        this.trackService.clearDatabase();
        this.trackService.initDatabase();
        Track sadSong = new TrackBuilder().startBuilder("Sad Song").addDurationInSeconds(108).addIssueDate(LocalDate.now()).build();
        this.trackService.create(sadSong);
    }

    @Test
    public void testCreateTrack() {
        Track sadSong = new TrackBuilder().startBuilder("Sad Song").addDurationInSeconds(108).addIssueDate(LocalDate.now()).build();
        Track newTrack = this.trackService.create(sadSong);

        Assertions.assertEquals(1, newTrack.getId());
        Assertions.assertEquals(sadSong.getTitle(), newTrack.getTitle());
        Assertions.assertEquals(sadSong.getDurationInSeconds(), newTrack.getDurationInSeconds());
    }

    @Test
    public void testUpdateTrack() {
        doReturn(1.23).when(pricingClient).getTrackPrice(0);

        String trackNewTitle = "Sun Song";
        Track track = this.trackService.getTrackById(0);
        track.setTitle(trackNewTitle);
        this.trackService.updateTrack(track);


        Track updatedTrackTitle = this.trackService.getTrackById(0);

        Assertions.assertEquals(trackNewTitle, updatedTrackTitle.getTitle());
    }

    @Test
    public void testDeleteTrack() {
        this.trackService.deleteTrack(0);

        Track trackRemoval = this.trackService.getTrackById(0);

        Assertions.assertNull(trackRemoval);

    }


    @Test
    public void testGeTrackById() {
        doReturn(1.23).when(pricingClient).getTrackPrice(0);
        Track trackById = this.trackService.getTrackById(0);


        Assertions.assertEquals("Sad Song", trackById.getTitle());
    }
}


//    @Test
//    public void testGetTrackByArtist(){
//        List<Track> resultTracks = trackService.getTracksByArtist(null);
//        Assertions.assertEquals(2, resultTracks.size());
//    }
//}



    // public List<Track> getTracksByMediaType (Track.TrackMediaType trackMediaType){
    // public List<Track> getTracksByIssueYear (LocalDate issueDate){
    // public List<Track> getByDuration ( int durationInSeconds){
    // public List<Track> getByDurationRange ( int minDuration, int maxDuration){



