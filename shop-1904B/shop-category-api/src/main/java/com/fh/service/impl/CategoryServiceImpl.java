package com.fh.service.impl;

import com.fh.bean.CategoryBean;
import com.fh.dao.ICategoryMapper;
import com.fh.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryMapper categoryMapper;

    @Override
    public List<Map<String, Object>> queryCategoryList() {
        List<CategoryBean> categoryBeans = categoryMapper.queryCategoryList();
        return getCategory(0,categoryBeans);
    }

    private List<Map<String, Object>> getCategory(Integer pid, List<CategoryBean> categoryBeans) {
        List<Map<String, Object>> list = new ArrayList<>();
        categoryBeans.forEach(cate -> {
            Map<String, Object> map = null;
            if (pid.equals(cate.getPid())) {
                map = new HashMap<>();
                map.put("id", cate.getId());
                map.put("name", cate.getName());
                map.put("pid", cate.getPid());
                map.put("children", getCategory(cate.getId(), categoryBeans));
            }
            if (map != null) {
                list.add(map);
            }
        });
        return list;
    }
}
