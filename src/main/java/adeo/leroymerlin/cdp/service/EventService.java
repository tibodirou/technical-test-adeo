package adeo.leroymerlin.cdp.service;

import adeo.leroymerlin.cdp.dto.EventDTO;
import adeo.leroymerlin.cdp.entity.Band;
import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.entity.Member;
import adeo.leroymerlin.cdp.exception.ResourceNotFoundException;
import adeo.leroymerlin.cdp.mapper.DtoMapper;
import adeo.leroymerlin.cdp.repository.EventRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Returns a list of all events.
     *
     * @return a list of all events
     */
    @Transactional(readOnly = true)
    @Cacheable("events")
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    /**
     * Deletes the event with the given id.
     * If the event does not exist, a ResourceNotFoundException is thrown.
     *
     * @param id the id of the event to delete
     * @throws ResourceNotFoundException if the event with the given id does not exist
     */
    @Transactional
    @CacheEvict(value = "events", allEntries = true)
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    /**
     * Updates the event with the given id.
     * The event is updated with the comment and number of stars from the given event.
     *
     * @param id    the id of the event to update
     * @param event the event with the comment and number of stars to update
     */
    @Transactional
    @CacheEvict(value = "events", allEntries = true)
    public void updateEvent(Long id, Event event) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        existingEvent.setComment(event.getComment());
        existingEvent.setNbStars(event.getNbStars());
        eventRepository.save(existingEvent);
    }

    /**
     * Returns a list of events filtered by the query parameter.
     * The query parameter is used to filter the events, bands and members by their names.
     * The result is a list of events with bands and members that contain the query parameter in their names.
     * The result is sorted by the number of bands and members that match the query parameter.
     *
     * @param query the query parameter used to filter the events, bands and members
     * @return a list of events filtered by the query parameter
     */
    @Transactional(readOnly = true)
    public List<EventDTO> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAll();
        String lowerCaseQuery = query.toLowerCase();

        List<Event> filteredEvents = filterEvents(events, lowerCaseQuery);
        return addCounts(filteredEvents).stream()
                .map(DtoMapper::toEventDTO)
                .toList();
    }

    /**
     * Filters the events by the query parameter.
     * The query parameter is used to filter the events, bands and members by their names.
     * The result is a list of events with bands and members that contain the query parameter in their names.
     *
     * @param events the list of events to filter
     * @param query  the query parameter used to filter the events, bands and members
     * @return a list of events filtered by the query parameter
     */
    private List<Event> filterEvents(List<Event> events, String query) {
        return events.stream()
                .map(event -> {
                    Set<Band> filteredBands = filterBands(event.getBands(), query);
                    event.setBands(filteredBands);
                    return event;
                })
                .filter(event -> !event.getBands().isEmpty())
                .toList();
    }

    /**
     * Filters the bands by the query parameter.
     * The query parameter is used to filter the bands and members by their names.
     * The result is a set of bands with members that contain the query parameter in their names.
     *
     * @param bands the set of bands to filter
     * @param query the query parameter used to filter the bands and members
     * @return a set of bands filtered by the query parameter
     */
    private Set<Band> filterBands(Set<Band> bands, String query) {
        return bands.stream()
                .map(band -> {
                    Set<Member> filteredMembers = filterMembers(band.getMembers(), query);
                    band.setMembers(filteredMembers);
                    return band;
                })
                .filter(band -> !band.getMembers().isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Filters the members by the query parameter.
     * The query parameter is used to filter the members by their names.
     * The result is a set of members that contain the query parameter in their names.
     *
     * @param members the set of members to filter
     * @param query   the query parameter used to filter the members
     * @return a set of members filtered by the query parameter
     */
    private Set<Member> filterMembers(Set<Member> members, String query) {
        return members.stream()
                .filter(member -> member.getName().toLowerCase().contains(query))
                .collect(Collectors.toSet());
    }

    /**
     * Adds the counts of bands and members to the event title and band name.
     *
     * @param events the list of events to add the counts to
     * @return a list of events with the counts of bands and members added to the title and name
     */
    private List<Event> addCounts(List<Event> events) {
        return events.stream()
                .map(event -> {
                    long bandCount = event.getBands().size();
                    event.setTitle(event.getTitle() + " [" + bandCount + "]");
                    Set<Band> updatedBands = event.getBands().stream()
                            .map(band -> {
                                long memberCount = band.getMembers().size();
                                Band updatedBand = new Band();
                                updatedBand.setName(band.getName() + " [" + memberCount + "]");
                                updatedBand.setMembers(band.getMembers());
                                return updatedBand;
                            })
                            .collect(Collectors.toSet());
                    event.setBands(updatedBands);
                    return event;
                })
                .toList();
    }
}
