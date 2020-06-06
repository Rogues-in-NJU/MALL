package edu.nju.mall.service;

import edu.nju.mall.entity.CoverImage;
import edu.nju.mall.vo.Cover;

public interface CoverService {

    Cover getCover();

    Long saveCoverImage(CoverImage coverImage);

    Long deleteCoverImage(Long coverImageId);

    Long deleteCoverImageByImageLink(String imageLink);
}
