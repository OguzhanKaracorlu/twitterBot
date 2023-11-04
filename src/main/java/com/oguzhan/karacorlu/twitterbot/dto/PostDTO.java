package com.oguzhan.karacorlu.twitterbot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * @author oguzhan.karacorlu
 * @project twitterBot
 * @created 29.10.2023
 */

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

    private String postLink;
    private String userName;
    private Integer responsesCount;
    private Integer likesCount;
    private Integer retweetsCount;
    private Integer viewsCount;
    private Integer userFollowersCount;
    private Integer userFollowedCount;
    private Boolean userProfileSuitability;
    private Integer totalInteraction;

    @Override
    public String toString() {
        return "PostDTO{" +
                "postLink='" + postLink + '\'' +
                ", userName='" + userName + '\'' +
                ", responsesCount=" + responsesCount +
                ", likesCount=" + likesCount +
                ", retweetsCount=" + retweetsCount +
                ", viewsCount=" + viewsCount +
                ", userFollowersCount=" + userFollowersCount +
                ", userFollowedCount=" + userFollowedCount +
                ", userProfileSuitability=" + userProfileSuitability +
                ", totalInteraction=" + totalInteraction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDTO)) return false;
        PostDTO postDTO = (PostDTO) o;
        return Objects.equals(getPostLink(), postDTO.getPostLink()) && Objects.equals(getUserName(), postDTO.getUserName()) && Objects.equals(getResponsesCount(), postDTO.getResponsesCount()) && Objects.equals(getLikesCount(), postDTO.getLikesCount()) && Objects.equals(getRetweetsCount(), postDTO.getRetweetsCount()) && Objects.equals(getViewsCount(), postDTO.getViewsCount()) && Objects.equals(getUserFollowersCount(), postDTO.getUserFollowersCount()) && Objects.equals(getUserFollowedCount(), postDTO.getUserFollowedCount()) && Objects.equals(getUserProfileSuitability(), postDTO.getUserProfileSuitability()) && Objects.equals(getTotalInteraction(), postDTO.getTotalInteraction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostLink(), getUserName(), getResponsesCount(), getLikesCount(), getRetweetsCount(), getViewsCount(), getUserFollowersCount(), getUserFollowedCount(), getUserProfileSuitability(), getTotalInteraction());
    }
}
