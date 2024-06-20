package org.example.forum.dto.Opinions;

public class CommentAddDto {

    private int user_adder_id;

    private String comment_text;

    private long article_id;

    public CommentAddDto(long user_adder_id, String comment_text, long article_id) {
            this.user_adder_id = (int) user_adder_id;
            this.comment_text = comment_text;
            this.article_id = article_id;
    }

    public int getUser_adder_id() {
        return user_adder_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public long getArticle_id() {
        return article_id;
    }

    public void setArticle_id(long article_id)
    {
        this.article_id = article_id;
    }

    public void setUser_adder_id(int user_adder_id)
    {
        this.user_adder_id = user_adder_id;
    }

    public void setComment_text(String comment_text)
    {
        this.comment_text = comment_text;
    }

}
