package learning.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Created by 刘一波 on 15/4/5.
 * E-Mail:obiteaaron@gmail.com
 * 此类用于修复PageRequest的构造函数在hessian反序列化时会抛出异常,所有pageable的接口全部用此类代替
 */
public class PageableRequest extends PageRequest {

    public PageableRequest(){
        super(0,10);
    }

    public PageableRequest(int page, int size) {
        super(page, size);
    }

    public PageableRequest(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public PageableRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }
}
