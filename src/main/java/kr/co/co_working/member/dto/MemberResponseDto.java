package kr.co.co_working.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.co_working.team.Team;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponseDto {
        private String email;
        private String name;
        private String description;
        private Team team;
        private LocalDateTime createAt;
        private LocalDateTime modifiedAt;

        @QueryProjection
        public MemberResponseDto(String email, String name, String description, LocalDateTime createAt, LocalDateTime modifiedAt) {
                this.email = email;
                this.name = name;
                this.description = description;
                this.createAt = createAt;
                this.modifiedAt = modifiedAt;
        }
}
