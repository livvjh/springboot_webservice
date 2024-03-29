package com.jhlee.book.springboot.web;

import com.jhlee.book.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//스프링 부트 테스트 JUnit간 연결 기능
@RunWith(SpringRunner.class)
//web(mvc 패턴)에 집중할 수 있는 스프링 테스트 어노테이션 @Controller 사용 가능
@WebMvcTest(controllers = HelloController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class HelloControllerTest {

    //스프링이 관리하는 빈 주입
    @Autowired
    //웹 api 테스트시 사용, http protocol api 테스트
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "USER")
    public void returnHello() throws Exception{
        String hello = "hello";
        //get요청, http header status 검증, 결과 페이지 검증
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void returnHelloDto() throws Exception {
        String name = "hello";
        int amount = 1000;
        mvc.perform(
                get("/hello/dto")
                        .param("name",name)
                        .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}
