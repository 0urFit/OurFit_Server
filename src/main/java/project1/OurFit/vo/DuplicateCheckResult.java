package project1.OurFit.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateCheckResult {
    private boolean duplicate;
    private String field;
}
