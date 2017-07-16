package com.pmdcodereview.daoLayer;

import com.pmdcodereview.model.PMDStructure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Nagendra on 15-07-2017.
 */
public interface PMDStructureDao extends MongoRepository<PMDStructure, String> {

    @Override
    <S extends PMDStructure> List<S> save(Iterable<S> iterable);

    List<PMDStructure> findBydate(String date);
}
