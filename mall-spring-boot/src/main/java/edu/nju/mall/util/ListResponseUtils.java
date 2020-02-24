package edu.nju.mall.util;

import edu.nju.mall.common.ListResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 获取分页内容
 * @Author: qianen.yin
 * @CreateDate: 2019-12-20 21:21
 */
public class ListResponseUtils {

    public static <T> ListResponse getListResponse(List<T> responseList, int pageIndex, int pageSize) {
        ListResponse res = new ListResponse();
        int extra = responseList.size() % pageSize == 0 ? 0 : 1;
        int totalPage = responseList.size() / pageSize + extra;
        res.setPageIndex(pageIndex);
        res.setPageSize(pageSize);
        res.setTotalPages(totalPage);
        res.setResult(responseList.stream()
                .skip(pageSize * (pageIndex - 1))
                .limit(pageSize).collect(Collectors.toList()));
        return res;
    }

    public static ListResponse generateResponse(Page page, int pageIndex, int pageSize) {
        ListResponse listResponse = new ListResponse();
        listResponse.setTotalPages(page.getTotalPages());
        listResponse.setPageSize(pageSize);
        listResponse.setPageIndex(pageIndex);
        listResponse.setResult(page.getContent());
        return listResponse;
    }
}
