package learning.thirdparty;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

/**
 * Created by isaac on 21/04/2017.
 */
public class PageableResponse<T> extends PageImpl<T> {

    private PageableResponse() {
        super(Collections.<T>emptyList());
    }

    public PageableResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageableResponse(List<T> content) {
        super(content);
    }
}
