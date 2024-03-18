package com.example.crud2.repository;

import com.example.crud2.decorater.PaginationRequest;
import com.example.crud2.model.User;
import com.example.crud2.utils.NullAwareBeanUtilBeans;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryCustom {


    private MongoTemplate mongoTemplate;
    private final NullAwareBeanUtilBeans beanUtilBeans;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public UserRepositoryCustom(MongoTemplate mongoTemplate, NullAwareBeanUtilBeans beanUtilBeans, ModelMapper modelMapper, UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.beanUtilBeans = beanUtilBeans;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }


    public Page<User> GetAllDataWithPagination(PaginationRequest paginationRequest)
    {
        Pageable paging = PageRequest.of(paginationRequest.getPagination().getPageNumber(), paginationRequest.getPagination().getPageSize());
        Query query = new Query();
        Sort.Direction direction = paginationRequest.getSort().getOrderBy().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        query.with(paging).with(Sort.by(direction,paginationRequest.getSort().getSortBy()));
        query.addCriteria(Criteria.where(paginationRequest.getSort().getSortBy()).regex(paginationRequest.getFilter().getSearchBy()));
        query.addCriteria(Criteria.where("softDelete").is(false));
        List<User> userDetails = mongoTemplate.find(query,User.class);
        return new PageImpl<>(userDetails, paging, mongoTemplate.count(query, User.class));
    }

    public List<User> findAdminData()
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("Roles").is("ADMIN"));
        query.addCriteria(Criteria.where("softDelete").is(false));
        List<User> userDetails = mongoTemplate.find(query, User.class);
        System.out.println(userDetails.size());
        System.out.println(new ArrayList<>(userDetails));
        return userDetails;
    }
 }

//List<User> user = new ArrayList<>();
//Query query2 = new Query();
//query2.addCriteria(Criteria.where("username").regex(charName));
//user = mongoTemplate.find(query2,User.class);

        /*Criteria criteria = new Criteria();
        criteria.where("username").regex(paginationDecorater.getSearchBy());
        criteria.where("softDelete").equals(false);*/