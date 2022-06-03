package by.karpovich.security.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserPhoto {

    private String name;
    private String url;
    private String type;
    private long size;

}
