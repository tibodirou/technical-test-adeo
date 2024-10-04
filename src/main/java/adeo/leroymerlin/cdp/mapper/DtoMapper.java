package adeo.leroymerlin.cdp.mapper;

import adeo.leroymerlin.cdp.dto.BandDTO;
import adeo.leroymerlin.cdp.dto.EventDTO;
import adeo.leroymerlin.cdp.dto.MemberDTO;
import adeo.leroymerlin.cdp.entity.Band;
import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.entity.Member;

import java.util.stream.Collectors;

public class DtoMapper {
    private DtoMapper() {
    }

    public static EventDTO toEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle(event.getTitle());
        eventDTO.setImgUrl(event.getImgUrl());
        eventDTO.setBands(event.getBands().stream()
                .map(DtoMapper::toBandDTO)
                .collect(Collectors.toSet()));
        return eventDTO;
    }

    public static BandDTO toBandDTO(Band band) {
        BandDTO bandDTO = new BandDTO();
        bandDTO.setName(band.getName());
        bandDTO.setMembers(band.getMembers().stream()
                .map(DtoMapper::toMemberDTO)
                .collect(Collectors.toSet()));
        return bandDTO;
    }

    public static MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName(member.getName());
        return memberDTO;
    }
}
