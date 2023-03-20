package net.shyshkin.study.nettysocketio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {

    private Long currentTask;
    private Long totalTasks;

}
