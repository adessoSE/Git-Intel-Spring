package de.adesso.gitstalker.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.memberID_Resources.ResponseMemberID;
import de.adesso.gitstalker.core.resources.member_Resources.ResponseMember;
import de.adesso.gitstalker.resources.MemberIDResources;
import de.adesso.gitstalker.resources.MemberResources;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class MemberProcessorTest {

    private MemberProcessor memberProcessor;
    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query testQuery = new Query("adessoAG", "testContent", RequestType.MEMBER, 1);
    private ResponseMember responseMember;
    private MemberResources memberResources;

    @Before
    public void setUp() throws Exception {
        this.memberResources = new MemberResources();
        this.responseMember = new ObjectMapper().readValue(this.memberResources.getExpectedGeneratedQueryContent(), ResponseMember.class);
        this.requestRepository = Mockito.mock(RequestRepository.class);
        this.organizationRepository = Mockito.mock(OrganizationRepository.class);
        this.memberProcessor = new MemberProcessor();
    }

    @Test
    public void checkIfQueryResponseIsProcessedCorrectly() {

    }
}