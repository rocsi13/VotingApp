package myApp.myApp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="votes")
public class Votes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable=false, unique=true)
    private String emailUser;
    @Column(nullable=false)
    private String emailCandidate;

    public Votes(String emailUser, String emailCandidate) {
        this.emailUser = emailUser;
        this.emailCandidate = emailCandidate;
    }
}
