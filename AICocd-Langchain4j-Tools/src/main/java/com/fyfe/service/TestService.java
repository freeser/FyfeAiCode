package com.fyfe.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    @Tool("根据用户名查询用户数的数量")
    public Integer getUserNameCount(@P("姓名") String name) {
        System.out.println("根据用户名查询对应人数业务执行");
        System.out.println("您输入的姓名为："+ name);
        return 20;
    }

    @Tool("根据用户名查询对应学生信息")
    public List<String> getAllUserNames(@P("姓名") String name) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("张三");
        strings.add("李四");
        strings.add("王五");
        System.out.println("传入的姓名为：" + name);
        return strings;
    }
}
