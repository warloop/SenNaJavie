package org.example.forum.dto.Opinions;

public class CommentEditDto {

    private int user_adder_id;

    private long comment_id;

    private String new_comment_text;

    public CommentEditDto(long user_adder_id, String comment_text, long comment_id) {
        this.user_adder_id = (int) user_adder_id;
        this.new_comment_text = comment_text;
        this.comment_id = comment_id;
    }

    public int getUser_adder_id() {
        return user_adder_id;
    }

    public String getNew_comment_text() {
        return new_comment_text;
    }

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public void setUser_adder_id(int user_adder_id)
    {
        this.user_adder_id = user_adder_id;
    }

    public void setNew_adder_id(String text)
    {
        this.new_comment_text = text;
    }

}
