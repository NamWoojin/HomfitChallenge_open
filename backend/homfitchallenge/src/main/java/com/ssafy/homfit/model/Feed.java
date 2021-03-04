package com.ssafy.homfit.model;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Feed : 피드(인증 내용)", description = "피드(인증 내용)을 저장 하고 있는 데이터")
public class Feed {
    @ApiModelProperty(value = "피드 식별번호")
    private int feed_id;
    @ApiModelProperty(value = "피드 사진 url")
    private String feed_picture;
    @ApiModelProperty(value = "좋아요 수")
    private int like_count;
    @ApiModelProperty(value = "피드 생성자")
    private String maked_uid;
    @ApiModelProperty(value = "챌린지 식별번호")
    private int challenge_id;
    @ApiModelProperty(value = "피드 등록일")
    private Timestamp register_date;
    @ApiModelProperty(value = "피드 내용")
    private String feed_contents;
    private MultipartFile imgFile;
    private String challenge_title;
    private String nick_name;
    private String user_img;
    private boolean user_liked;

    public boolean isUser_liked() {
        return this.user_liked;
    }

    public boolean getUser_liked() {
        return this.user_liked;
    }

    public void setUser_liked(boolean user_liked) {
        this.user_liked = user_liked;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_img() {
        return this.user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getChallenge_title() {
        return this.challenge_title;
    }

    public void setChallenge_title(String challenge_title) {
        this.challenge_title = challenge_title;
    }

    public MultipartFile getImgFile() {
        return this.imgFile;
    }

    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }

    public int getFeed_id() {
        return this.feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public String getFeed_picture() {
        return this.feed_picture;
    }

    public void setFeed_picture(String feed_picture) {
        this.feed_picture = feed_picture;
    }

    public int getLike_count() {
        return this.like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getMaked_uid() {
        return this.maked_uid;
    }

    public void setMaked_uid(String maked_uid) {
        this.maked_uid = maked_uid;
    }

    public int getChallenge_id() {
        return this.challenge_id;
    }

    public void setChallenge_id(int challenge_id) {
        this.challenge_id = challenge_id;
    }

    public Timestamp getRegister_date() {
        return this.register_date;
    }

    public void setRegister_date(Timestamp register_date) {
        this.register_date = register_date;
    }

    public String getFeed_contents() {
        return this.feed_contents;
    }

    public void setFeed_contents(String feed_contents) {
        this.feed_contents = feed_contents;
    }


    @Override
    public String toString() {
        return "Feed {" +
            " feed_id='" + getFeed_id() + "'" +
            ", feed_picture='" + getFeed_picture() + "'" +
            ", like_count='" + getLike_count() + "'" +
            ", maked_uid='" + getMaked_uid() + "'" +
            ", challenge_id='" + getChallenge_id() + "'" +
            ", register_date='" + getRegister_date() + "'" +
            ", feed_contents='" + getFeed_contents() + "'" +
            "}";
    }

}
