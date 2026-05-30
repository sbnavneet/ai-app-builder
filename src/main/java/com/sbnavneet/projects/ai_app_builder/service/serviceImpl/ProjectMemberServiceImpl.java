package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.member.InviteMemberRequest;
import com.sbnavneet.projects.ai_app_builder.dto.member.MemberResponse;
import com.sbnavneet.projects.ai_app_builder.dto.member.UpdateMemberRoleRequest;
import com.sbnavneet.projects.ai_app_builder.entity.Project;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMember;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMemberId;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.error.BadRequestException;
import com.sbnavneet.projects.ai_app_builder.mapper.ProjectMemberMapper;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectMemberRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectRepository;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.ProjectMemberService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberMapper projectMemberMapper;
    private final UserRepository userRepository;
    private final AuthUtility authUtility;

    @Override
    @PreAuthorize("@security.canViewMembers(#projectId)")
    public List<MemberResponse> getMembers(Long projectId) {
        List<MemberResponse> memberResponseList = new ArrayList<>();
        memberResponseList.addAll(projectMemberRepository.findByIdProjectIdAndAcceptedAtIsNotNull(projectId)
                                                                                    .stream()
                                                                                    .map(projectMemberMapper::toMemberResponse)
                                                                                    .toList());
        return memberResponseList;    
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
       Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(authUtility.getCurrentUser(), projectId).orElseThrow();
       
       
       User invitee = userRepository.findByUsername(request.email()).orElseThrow();
       
       //Check if user inviting himself
       if(invitee.getId().equals(authUtility.getCurrentUser())){
            throw new RuntimeException("You are not allowed to invite yourself");
       }
       
       ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), invitee.getId());
       
       //Check if already invited
       if(projectMemberRepository.existsById(projectMemberId)){
            throw new BadRequestException("User already invited");
       }
       ProjectMember projectMember = ProjectMember.builder()
                                                            .id(projectMemberId)
                                                            .user(invitee)
                                                            .project(project)
                                                            .role(request.role())
                                                            .invitedAt(Instant.now())
                                                            .build();
        projectMemberRepository.save(projectMember);
       return projectMemberMapper.toMemberResponse(projectMember);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse updateMemberRole(Long projectId, UpdateMemberRoleRequest request, Long memberId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(authUtility.getCurrentUser(), projectId).orElseThrow();
       

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        ProjectMember member = projectMemberRepository.findById(projectMemberId).orElseThrow();
        
        member.setRole(request.newRole());
        
        projectMemberRepository.save(member);
        
        return projectMemberMapper.toMemberResponse(member);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public void deleteMember(Long projectId, Long memberId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(authUtility.getCurrentUser(), projectId).orElseThrow(() -> new BadRequestException("You are not the owner of the project"));

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);
        if(projectMemberRepository.existsById(projectMemberId)){
            projectMemberRepository.deleteById(projectMemberId);
       }else{
            throw new RuntimeException("Member doesn't exists");
       }
    }

    public void acceptInvite(Long projectId){
        Long currentUserId = authUtility.getCurrentUser();
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, currentUserId);

        ProjectMember member = projectMemberRepository.findById(projectMemberId).orElseThrow(() -> new BadRequestException("User not invited"));
        if(member.getAcceptedAt() != null){
            throw new RuntimeException("Invite already accepted");
        }
        member.setAcceptedAt(Instant.now());
        projectMemberRepository.save(member);
    }
}
