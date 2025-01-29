package kr.co.co_working.team.dto;

import kr.co.co_working.task.dto.TaskRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class TeamRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String name;
        private String description;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class READ {
        private Long id;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UPDATE {
        private String name;
        private String description;

        @Builder
        public UPDATE(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
