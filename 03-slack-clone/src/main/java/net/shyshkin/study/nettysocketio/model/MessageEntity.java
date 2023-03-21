package net.shyshkin.study.nettysocketio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    private String text;
    private Long time;
    private String username;
    private String avatar;

}
