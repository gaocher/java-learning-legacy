package learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Created by isaac on 23/08/2017.
 */
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CmpKey {
    public enum KeyType {
        A,B;
    }
    private Long ref;
    private KeyType keyType;
}
