package com.oguzhan.karacorlu.twitterbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author oguzhan.karacorlu
 * @project twitterBot
 * @created 29.10.2023
 */

@Getter
@Setter
@AllArgsConstructor
public class PostDTO {

    private String postLink;
    private String userName;
    private Integer likesCount;
    private Integer retweetsCount;
    private Integer viewsCount;
    private Integer userFollowersCount;
    private Integer userFollowedCount;

}
