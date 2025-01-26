package kr.co.co_working.team.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TeamResponseDto {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime createAt;
        private LocalDateTime modifiedAt;
        private String email;
        private String memberName;

        @QueryProjection
        public TeamResponseDto(Long id, String name, String description, LocalDateTime createAt, LocalDateTime modifiedAt, String email, String memberName) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.createAt = createAt;
                this.modifiedAt = modifiedAt;
                this.email = email;
                this.memberName = memberName;
        }
}
