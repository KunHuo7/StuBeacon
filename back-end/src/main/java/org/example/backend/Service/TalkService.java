package org.example.backend.Service;

import org.example.backend.Entity.pojo.Talk;
import org.example.backend.Entity.pojo.TalkComment;
import org.example.backend.Entity.vo.TalkCommentVO;
import org.example.backend.Entity.vo.TalkVO;

import java.util.List;

public interface TalkService {
    int addNewTalk(Talk talk);
    
    List<TalkVO> getOrderByTalks();
    
    TalkVO getTalkById(int id, int userId);
    
    String doNewLike(Integer userId, Integer talkId);
    
    String deleteLike(Integer userId, Integer talkId);
    
    int checkUserLikedTalk(Integer userId, Integer talkId);
    
    List<TalkVO> getTalksByTag(String tag);
    
    int addNewComment(TalkComment comment);
    
    List<TalkCommentVO> getCommentsByTalkId(Integer talkId, Integer userId);

    Integer findTalkIdByTitle(String title);
    
    boolean deleteTalk(Integer talkId, Integer userId);
    
    boolean deleteComment(Integer commentId, Integer userId);
}