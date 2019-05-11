package org.springframework.httprestservice.ControllerTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.httprestservice.controller.GroupController;
import org.springframework.httprestservice.service.GroupService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = GroupController.class, secure = false)
public class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private GroupController groupController;

    @MockBean
    private GroupService groupService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        groupController = new GroupController();
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    public void getFirstGroupByGid() throws Exception{

        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/groups/0").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name", is("root")))
                .andExpect(jsonPath("$.members.[0]", is("root")))
                .andReturn();

    }

    @Test
    public void getAllGroups() throws Exception{
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/groups").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].name", is("bin")))
                .andExpect(jsonPath("$[1].members.[0]", is("root")))
                .andExpect(jsonPath("$[56].name").exists())
                .andExpect(jsonPath("$[56].name", is("opcgrp")))
                .andExpect(jsonPath("$[56].members.[0]", is("")))
                .andReturn();
    }

    @Test
    public void getGroupsByOneMember() throws Exception{
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/groups/query?member=daemon")
                                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name", is("bin")))
                .andExpect(jsonPath("$[0].members.[2]", is("daemon")))
                .andExpect(jsonPath("$[3].name").exists())
                .andExpect(jsonPath("$[3].name", is("lp")))
                .andExpect(jsonPath("$[3].members.[0]", is("daemon")))
                .andReturn();
    }

    @Test
    public void getGroupsByTwoMembers() throws Exception{
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/groups/query?member=daemon&member=root")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name", is("bin")))
                .andExpect(jsonPath("$[0].members.[2]", is("daemon")))
                .andExpect(jsonPath("$[2].name").exists())
                .andExpect(jsonPath("$[2].name", is("adm")))
                .andExpect(jsonPath("$[2].members.[0]", is("root")))
                .andReturn();
    }

    @Test
    public void getGroupByName() throws Exception{
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/groups/query?name=wheel")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name", is("wheel")))
                .andExpect(jsonPath("$[0].members.[0]", is("root")))
                .andReturn();
    }

    @Test
    public void getGroupsWithNoOptions() throws Exception{
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/groups/query?") // return all groups
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].name", is("bin")))
                .andExpect(jsonPath("$[1].members.[0]", is("root")))
                .andExpect(jsonPath("$[56].name").exists())
                .andExpect(jsonPath("$[56].name", is("opcgrp")))
                .andExpect(jsonPath("$[56].members.[0]", is("")))
                .andReturn();
    }

    @Test
    public void getNoGroupsWithWrongOptions() throws Exception{
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/groups/query?name=raphtalia") // return all groups
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }
}
