package com.chat_app.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chat_app.model.Message;

public interface MessageRepositary extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m join m.chat c WHERE c.id = :chatId")
    public List<Message> findByChatId(@Param("chatId") Integer chatId);
}
