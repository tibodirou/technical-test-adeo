package adeo.leroymerlin.cdp.dto;

import java.util.Objects;
import java.util.Set;


public class EventDTO {
    private String title;
    private String imgUrl;
    private Set<BandDTO> bands;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<BandDTO> getBands() {
        return bands;
    }

    public void setBands(Set<BandDTO> bands) {
        this.bands = bands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO eventDTO = (EventDTO) o;
        return Objects.equals(title, eventDTO.title) && Objects.equals(imgUrl, eventDTO.imgUrl) && Objects.equals(bands, eventDTO.bands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, imgUrl, bands);
    }
}
