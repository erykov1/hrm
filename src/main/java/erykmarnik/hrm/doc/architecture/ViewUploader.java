package erykmarnik.hrm.doc.architecture;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.api.WorkspaceApiClient;
import com.structurizr.configuration.WorkspaceScope;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ViewUploader {
  static void upload(Workspace workspace) throws StructurizrClientException {
    long workspaceId = Long.parseLong(System.getenv("workspace.id"));
    String apiKey = System.getenv().get("api.key");
    String apiSecret = System.getenv().get("api.secret");
    WorkspaceApiClient apiClient = new WorkspaceApiClient(apiKey, apiSecret);
    workspace.getConfiguration().setScope(WorkspaceScope.SoftwareSystem);
    apiClient.putWorkspace(workspaceId, workspace);
  }
}
