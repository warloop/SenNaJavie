package org.example.forum.dto.Opinions;

public class CommentLikesAndDislikesCounterDto {

    private int likes;
    private int dislikes;
    private long coment_id;

    public CommentLikesAndDislikesCounterDto(){
        this.likes = 0;
        this.dislikes = 0;
        this.coment_id = 0;
    }

    public CommentLikesAndDislikesCounterDto(long coment_id,int likes, int dislikes) {
        this.likes = likes;
        this.dislikes = dislikes;
        this.coment_id = coment_id;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public long getComent_id() {
        return coment_id;
    }

    public void setComent_id(long coment_id) {
        this.coment_id = coment_id;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
