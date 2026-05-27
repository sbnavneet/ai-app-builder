package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.member.InviteMemberRequest;
import com.sbnavneet.projects.ai_app_builder.dto.member.MemberResponse;
import com.sbnavneet.projects.ai_app_builder.dto.member.UpdateMemberRoleRequest;
import com.sbnavneet.projects.ai_app_builder.entity.Project;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMember;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMemberId;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.mapper.ProjectMemberMapper;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectMemberRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectRepository;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
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

    @Override
    public List<MemberResponse> getMembers(Long projectId, Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, projectId).orElseThrow();
        List<MemberResponse> memberResponseList = new ArrayList<>();
        memberResponseList.add(projectMemberMapper.toMemberResponse(project.getOwner()));
        memberResponseList.addAll(projectMemberRepository.findByIdProjectId(projectId)
                                                                                    .stream()
                                                                                    .map(projectMemberMapper::toMemberResponse)
                                                                                    .toList());
        return memberResponseList;    
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
       Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, projectId).orElseThrow();
       
       //Check if user inviting is the project owner
       if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("You are not allowed to invite members");
       }
       
       User invitee = userRepository.findByEmail(request.email()).orElseThrow();
       
       //Check if user inviting himself
       if(invitee.getId().equals(userId)){
            throw new RuntimeException("You are not allowed to invite yourself");
       }
       
       ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), invitee.getId());
       
       //Check if already invited
       if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("User already invited");
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
    public MemberResponse updateMemberRole(Long projectId, UpdateMemberRoleRequest request, Long memberId,
            Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, projectId).orElseThrow();
       
        //Check if user updating is the project owner
        if(!project.getOwner().getId().equals(userId)){
             throw new RuntimeException("Not Allowed");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);

        ProjectMember member = projectMemberRepository.findById(projectMemberId).orElseThrow();
        
        member.setRole(request.newRole());
        
        projectMemberRepository.save(member);
        
        return projectMemberMapper.toMemberResponse(member);
    }

    @Override
    public void deleteMember(Long projectId, Long memberId, Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, projectId).orElseThrow();
        
        //Check if user deleting is the project owner
        if(!project.getOwner().getId().equals(userId)){
             throw new RuntimeException("Not Allowed");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);
        if(projectMemberRepository.existsById(projectMemberId)){
            projectMemberRepository.deleteById(projectMemberId);
       }else{
            throw new RuntimeException("Member doesn't exists");
       }
    }

    public void acceptInvite(Long projectId, Long memberId, Long userId){
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, projectId).orElseThrow();
        //Check if user accepting is the project owner
        if(!project.getOwner().getId().equals(userId)){
             throw new RuntimeException("Not Allowed");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), memberId);
        
        ProjectMember member = projectMemberRepository.findById(projectMemberId).orElseThrow();
        if(member.getInvitedAt() == null){
            throw new RuntimeException("Member is not invited");
        }
        member.setAcceptedAt(Instant.now());
        projectMemberRepository.save(member);
        
    }
}
