package by.karpovich.security.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "avatar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "data")
    private byte[] data;


    public UserPhoto(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
