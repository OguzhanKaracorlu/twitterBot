package com.oguzhan.karacorlu.twitterbot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
