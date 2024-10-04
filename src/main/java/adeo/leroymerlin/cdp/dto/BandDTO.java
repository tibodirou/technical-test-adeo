package adeo.leroymerlin.cdp.dto;

import java.util.Objects;
import java.util.Set;

public class BandDTO {
    private String name;
    private Set<MemberDTO> members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MemberDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<MemberDTO> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BandDTO bandDTO = (BandDTO) o;
        return Objects.equals(name, bandDTO.name) && Objects.equals(members, bandDTO.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, members);
    }
}
