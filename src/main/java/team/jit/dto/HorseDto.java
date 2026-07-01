package team.jit.dto;

import lombok.Getter;
import lombok.Setter;
import team.jit.entity.Horse;

@Getter
@Setter
public class HorseDto {

    private String name;

    public static HorseDto of(Horse horse) {
        var horsedto = new HorseDto();
        horsedto.setName(horse.getName());
        return horsedto;
    }
}
