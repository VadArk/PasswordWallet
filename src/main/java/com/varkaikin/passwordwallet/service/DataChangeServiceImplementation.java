package com.varkaikin.passwordwallet.service;

import com.varkaikin.passwordwallet.dto.DataChangeResponse;
import com.varkaikin.passwordwallet.model.DataChange;
import com.varkaikin.passwordwallet.repository.DataChangeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataChangeServiceImplementation implements DataChangeService {

    private final DataChangeRepository dataChangeRepository;
    private final ModelMapper modelMapper;

    public DataChangeServiceImplementation(DataChangeRepository dataChangeRepository, ModelMapper modelMapper) {
        this.dataChangeRepository = dataChangeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DataChangeResponse> findAllByUserLoginAndPasswordId(String login, String passwordId) {

        List<DataChange> allByUser_loginAndPassword_id = dataChangeRepository.findAllByUser_LoginAndPassword_Id(login,
                Long.valueOf(passwordId));
        List<DataChangeResponse> dataChangeResponseList = new ArrayList<>();

        for (DataChange dataChange : allByUser_loginAndPassword_id) {

            DataChangeResponse map = modelMapper.map(dataChange, DataChangeResponse.class);
            dataChangeResponseList.add(map);
        }
        return dataChangeResponseList;
    }


    @Override
    public DataChange findById(String id) {

        return dataChangeRepository.findById(Long.valueOf(id)).get();
    }

}
