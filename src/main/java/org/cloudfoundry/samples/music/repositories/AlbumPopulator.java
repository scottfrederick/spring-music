package org.cloudfoundry.samples.music.repositories;

import org.cloudfoundry.samples.music.domain.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AlbumPopulator implements ApplicationListener<ContextRefreshedEvent> {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumPopulator(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (albumRepository.count() == 0) {
            List<Album> albums = buildSeedData();
            albumRepository.save(albums);
        }
    }

    private List<Album> buildSeedData() {
        return Arrays.asList(
                new Album("Nirvana", "Nevermind", "1991", "Rock"),
                new Album("The Beach Boys", "Pet Sounds", "1966", "Rock"),
                new Album("Marvin Gaye", "What's Going On", "1971", "Rock"),
                new Album("Jimi Hendrix Experience", "Are You Experienced?", "1967", "Rock"),
                new Album("U2", "The Joshua Tree", "1987", "Rock"),
                new Album("The Beatles", "Sgt. Pepper's Lonely Hearts Club Band", "1967", "Rock"),
                new Album("Fleetwood Mac", "Rumours", "1977", "Rock"),
                new Album("Elvis Presley", "Sun Sessions", "1976", "Rock"),
                new Album("Michael Jackson", "Thriller", "1982", "Pop"),
                new Album("The Rolling Stones", "Exile on Main Street", "1972", "Rock"),
                new Album("Bruce Springsteen", "Born to Run", "1975", "Rock"),
                new Album("The Clash", "London Calling", "1980", "Rock"),
                new Album("The Eagles", "Hotel California", "1976", "Rock"),
                new Album("Led Zeppelin", "Led Zeppelin", "1969", "Rock"),
                new Album("Led Zeppelin", "IV", "1971", "Rock"),
                new Album("Police", "Synchronicity", "1983", "Rock"),
                new Album("U2", "Achtung Baby", "1991", "Rock"),
                new Album("The Rolling Stones", "Let it Bleed", "1969", "Rock"),
                new Album("The Beatles", "Rubber Soul", "1965", "Rock"),
                new Album("The Ramones", "The Ramones", "1976", "Rock"),
                new Album("Queen", "A Night At The Opera", "1975", "Rock"),
                new Album("Boston", "Don't Look Back", "1978", "Rock"),
                new Album("BB King", "Singin' The Blues", "1956", "Blues"),
                new Album("Albert King", "Born Under A Bad Sign", "1967", "Blues"),
                new Album("Muddy Waters", "Folk Singer", "1964", "Blues"),
                new Album("The Fabulous Thunderbirds", "Rock With Me", "1979", "Blues"),
                new Album("Robert Johnson", "King of the Delta Blues", "1961", "Blues"),
                new Album("Texas Flood", "Stevie Ray Vaughan", "1983", "Blues"),
                new Album("Couldn't Stand The Weather", "Stevie Ray Vaughan", "1984", "Blues")
        );
    }
}
