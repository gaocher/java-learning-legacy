package learning.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

/**
 * Created by isaac on 21/04/2017.
 */
public class PageableResponse<T> extends PageImpl<T> {

    public PageableResponse() {
        super(Collections.<T>emptyList());
    }

    public PageableResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageableResponse(List<T> content) {
        super(content);
    }

    public static <T> PageableResponse<T> of(Page<T> page){
        return new PageableResponse<T>(page.getContent(),new PageableRequest(page.getNumber(),page.getSize(),page.getSort()),page.getTotalElements());
    }
}
