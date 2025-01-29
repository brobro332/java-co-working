package kr.co.co_working.member.service;

import ch.qos.logback.core.util.StringUtil;
import kr.co.co_working.member.Member;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.repository.MemberDslRepository;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.memberTeam.MemberTeam;
import kr.co.co_working.memberTeam.repository.MemberTeamRepository;
import kr.co.co_working.team.dto.TeamRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;
    private final MemberDslRepository dslRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * createMember : Member 등록
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public String createMember(MemberRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. Member 빌드
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = Member.builder()
            .email(email)
            .password(passwordEncoder.encode(dto.getPassword()))
            .name(dto.getName())
            .description(StringUtil.nullStringToEmpty(dto.getDescription()))
            .build();

        // 2. Member 등록
        repository.save(member);

        // 3. Email 반환
        return member.getEmail();
    }

    /**
     * readMemberByTeam : 특정 Team 소속 MemberList 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<MemberResponseDto> readMemberListByTeam(TeamRequestDto.READ dto) throws Exception {
        // QueryDSL 동적 쿼리 결과 반환
        return dslRepository.readMemberListByTeam(dto);
    }

    /**
     * readMemberList : MemberList 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<MemberResponseDto> readMemberList(MemberRequestDto.READ dto) throws Exception {
        return dslRepository.readMemberList(dto);
    }

    /**
     * updateMember : Member 수정
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void updateMember(MemberRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Member> selectedMember = repository.findById(email);

        // 2. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("수정하려는 멤버가 존재하지 않습니다. EMAIL : " + email);
        }

        // 3. Member 추출
        Member member = selectedMember.get();

        // 4. Member 수정
        member.updateMember(dto.getName(), dto.getDescription());
    }

    /**
     * deleteMember : Member 삭제
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void deleteMember() throws NoSuchElementException, Exception {
        // 1. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Member> selectedMember = repository.findById(email);

        // 2. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("수정하려는 멤버가 존재하지 않습니다. EMAIL : " + email);
        }

        // 3. Member 객체 추출
        Member member = selectedMember.get();

        // 4. MemberTeam 조회
        List<MemberTeam> memberTeams = memberTeamRepository.findByMemberEmail(member.getEmail());

        // 5. 조회한 Member 객체에 해당하는 MemberTeam 삭제
        for (MemberTeam memberTeam : memberTeams) {
            memberTeamRepository.delete(memberTeam);
        }

        // 6. Member 삭제
        repository.delete(member);
    }
}