package com.pProject.ganada;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VocaDao {
    @Query("SELECT * FROM Voca")
    List<Voca> getAll();
//    LiveData<List<Voca>> getAll();

    @Query("SELECT * FROM Voca WHERE id LIKE :vocaId")
    Voca findById(int vocaId);

    @Query("SELECT * FROM Voca WHERE picture_uri LIKE :pictureUri")
    Voca findByUri(String pictureUri);

    @Insert
    void insert(Voca... voca);

    @Delete
    void delete(Voca voca);
}
