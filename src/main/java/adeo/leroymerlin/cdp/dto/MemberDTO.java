package adeo.leroymerlin.cdp.dto;

import java.util.Objects;

public class MemberDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTO memberDTO = (MemberDTO) o;
        return Objects.equals(name, memberDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
