package org.springframework.httprestservice.ControllerTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.httprestservice.controller.UserController;
import org.springframework.httprestservice.service.UserService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private UserController userController;

    @MockBean
    private UserService userService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        userController = new UserController();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getUserRootByUidTest() throws Exception{

        /*
             Response Body should be
             {"name":"root","uid":0,"gid":0,"comment":"root","home":"/root","shell":"/bin/bash"}
        * */
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/users/0").accept(MediaType.APPLICATION_JSON);

             mockMvc.perform(reqBuilder)
                    .andDo(print())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.name", is("root")))
                    .andExpect(jsonPath("$.uid", is(0)))
                    .andReturn();

    }

    @Test
    public void getUserByNonExistingUidWillFail() throws Exception{

        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/users/99999").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void getAllUsersTest() throws Exception{

        /*
        * Test for first and last elements
        * */
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(reqBuilder)
                .andDo(print())
                .andExpect(jsonPath("$[0].name", is("root")))
                .andExpect(jsonPath("$[0].uid", is(0)))
                .andExpect(jsonPath("$[38].name", is("sabayon")))
                .andExpect(jsonPath("$[38].uid", is(86)))
                .andReturn();

    }

    @Test
    public void getUsersByComment() throws Exception{
        String uri = "http://localhost:3000/users/query?shell=/bin/bash";
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("root")))
                .andExpect(jsonPath("$[0].uid", is(0)))
                .andReturn();
    }

    @Test
    public void getUsersByHome() throws Exception{
        String uri = "http://localhost:3000/users/query?home=/var/adm";
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("adm")))
                .andExpect(jsonPath("$[0].uid", is(3)))
                .andReturn();
    }

    @Test
    public void getUsersByName() throws Exception{
        String uri = "http://localhost:3000/users/query?name=sshd";
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("sshd")))
                .andExpect(jsonPath("$[0].uid", is(74)))
                .andReturn();
    }


    @Test
    public void getUsersByGid() throws Exception{
        String uri = "http://localhost:3000/users/query?gid=0";
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("root")))
                .andExpect(jsonPath("$[0].uid", is(0)))
                .andExpect(jsonPath("$[4].name", is("operator")))
                .andExpect(jsonPath("$[4].uid", is(11)))
                .andReturn();
    }

    @Test
    public void getUsersByMultipleOptions() throws Exception{
        String uri = "http://localhost:3000/users/query?comment=daemon&shell=/sbin/nologin";
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("daemon")))
                .andExpect(jsonPath("$[0].uid", is(2)))
                .andExpect(jsonPath("$[2].name", is("digimon")))
                .andExpect(jsonPath("$[2].uid", is(1001)))
                .andReturn();
    }
    @Test
    public void getUsersByNoOptions() throws Exception{
        String uri = "http://localhost:3000/users/query?"; //will return all users
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("root")))
                .andExpect(jsonPath("$[0].uid", is(0)))
                .andExpect(jsonPath("$[38].name", is("sabayon")))
                .andExpect(jsonPath("$[38].uid", is(86)))
                .andReturn();
    }

    @Test
    public void getUsersByWrongParameters() throws Exception{
        String uri = "http://localhost:3000/users/query?foo=10&bar=30"; //will return all users
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("root")))
                .andExpect(jsonPath("$[0].uid", is(0)))
                .andExpect(jsonPath("$[38].name", is("sabayon")))
                .andExpect(jsonPath("$[38].uid", is(86)))
                .andReturn();
    }

    @Test
    public void getGroupByUserId() throws Exception{
        String uri = "http://localhost:3000/users/0/groups";
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$[0].name", is("root")))
                .andExpect(jsonPath("$[0].gid", is(0)))
                .andExpect(jsonPath("$[1].name", is("bin")))
                .andExpect(jsonPath("$[1].gid", is(1)))
                .andExpect(jsonPath("$[6].name", is("wheel")))
                .andExpect(jsonPath("$[6].gid", is(10)))
                .andReturn();
    }

    @Test
    public void getNoUsersIfNoMatch() throws Exception{
        String uri = "http://localhost:3000/users/query?home=jajaja"; //will return all users
        RequestBuilder reqBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(reqBuilder).
                andDo(print())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }







}
