package com.woong.daangnmarket.post.service;


import com.woong.daangnmarket.post.domain.entity.Category;

public interface CategoryService {

    public Category findCategoryByName(String categoryName);
}
