package com.chat_app.repositary;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.chat_app.model.Chat;
import com.chat_app.model.User;

public interface ChatRepo extends JpaRepository<Chat, Integer> {

    @Query("select c from Chat c join c.users u where u.id = :userId")
    public List<Chat> findChatByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Chat c WHERE c.isGroup = false AND :user MEMBER OF c.users AND :reqUser MEMBER OF c.users")
    public Chat findSingleChatUserByIds(@Param("user") User user, @Param("reqUser") User reqUser);

}