package com.sbnavneet.projects.ai_app_builder.service;

import java.util.List;


import com.sbnavneet.projects.ai_app_builder.dto.member.InviteMemberRequest;
import com.sbnavneet.projects.ai_app_builder.dto.member.MemberResponse;
import com.sbnavneet.projects.ai_app_builder.dto.member.UpdateMemberRoleRequest;

public interface ProjectMemberService {

    List<MemberResponse> getMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, UpdateMemberRoleRequest request, Long memberId);

    void deleteMember(Long projectId, Long memberId);

    void acceptInvite(Long projectId);


}
