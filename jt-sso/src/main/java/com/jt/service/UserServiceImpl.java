package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jt.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

	private static Map<Integer,String> columnMap = new HashMap<>();
	static
	{
		columnMap.put(1,"username");
		columnMap.put(2,"phone");
		columnMap.put(3,"email");
	}
	@Autowired
	private UserMapper userMapper;

	/*判断依据：查询数据库 select
	* */
	@Override
	public boolean checkUser(String param, Integer type) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq(columnMap.get(type),param);
		int count = userMapper.selectCount(queryWrapper);
		//int a = 1 / 0; 测试用户名的校验功能
		return count > 0;
	}


	@Override
	public List<User> findAll() {

		return userMapper.selectList(null);
	}


}
