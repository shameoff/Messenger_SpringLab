package ru.shameoff.jessenger.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shameoff.jessenger.friends.entity.FriendEntity;

import java.util.List;
import java.util.UUID;

public interface FriendsRepository extends JpaRepository<FriendEntity, UUID> {
    public List<FriendEntity> findAllByUserId(UUID targetUserId);

    public FriendEntity findByUserIdAndFriendId(UUID targetUserId, UUID friendId);
}
