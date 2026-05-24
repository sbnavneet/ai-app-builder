package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.util.List;

import com.sbnavneet.projects.ai_app_builder.dto.member.InviteMemberRequest;
import com.sbnavneet.projects.ai_app_builder.dto.member.MemberResponse;
import com.sbnavneet.projects.ai_app_builder.dto.member.UpdateMemberRoleRequest;
import com.sbnavneet.projects.ai_app_builder.service.ProjectMemberService;

public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Override
    public List<MemberResponse> getMembers(Long projectId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMembers'");
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inviteMember'");
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, UpdateMemberRoleRequest request, Long memberId,
            Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemberRole'");
    }

    @Override
    public MemberResponse deleteMember(Long projectId, Long memberId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMember'");
    }
}
