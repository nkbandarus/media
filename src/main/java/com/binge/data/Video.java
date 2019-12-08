package com.binge.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Video {

    @Id
    public String id;

    public String firstName;
    public String lastName;
}
