package work.team.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import work.team.model.dto.TaskClientReportParams;
import work.team.model.dto.TaskClientResponse;
import work.team.model.dto.TeamReportResponse;

import java.util.List;

@Component
public class TaskClient {
    private final RestClient restClient;

    public TaskClient(@Qualifier("restClientTask") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<TaskClientResponse> getTasksByAssigneeIds(List<Long> ids) {
        return restClient.post()
                .uri("/api/v1/task/tasks/by-assignees")
                .body(ids)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TaskClientResponse>>() {});
    }

    public TeamReportResponse teamReport(TaskClientReportParams params) {
        return restClient.post()
                .uri("/api/v1/task/reports/by-assignees")
                .body(params)
                .retrieve()
                .body(new ParameterizedTypeReference<TeamReportResponse>() {});
    }
}