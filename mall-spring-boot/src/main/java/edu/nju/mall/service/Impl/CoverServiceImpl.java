package edu.nju.mall.service.Impl;

import edu.nju.mall.entity.CoverImage;
import edu.nju.mall.repository.CoverImageRepository;
import edu.nju.mall.service.CoverService;
import edu.nju.mall.vo.Cover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CoverServiceImpl implements CoverService {

    @Autowired
    CoverImageRepository coverImageRepository;

    @Override
    public Cover getCover() {
        List<CoverImage> coverImages = coverImageRepository.findAll();

        List<String> imageAddresses = new ArrayList<>();
        for(CoverImage c : coverImages){
            imageAddresses.add(c.getImageLink());
        }

        Cover cover = Cover.builder()
                .imageAddresses(imageAddresses)
                .build();
        return cover;
    }

    @Override
    public Long saveCoverImage(CoverImage coverImage) {
        return coverImageRepository.save(coverImage).getId();
    }

    @Override
    public Long deleteCoverImage(Long coverImageId) {
        coverImageRepository.deleteById(coverImageId);
        return 0l;

    }

    @Transactional
    @Override
    public Long deleteCoverImageByImageLink(String imageLink) {
        Long id = coverImageRepository.findByImageLink("/" + imageLink).getId();
        coverImageRepository.deleteById(id);
        return 0l;
    }
}
