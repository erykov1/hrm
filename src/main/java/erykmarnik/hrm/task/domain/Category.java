package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.task.dto.CategoryDto;
import erykmarnik.hrm.task.dto.ModifyTaskDto;
import erykmarnik.hrm.task.exception.TaskNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long categoryId;
  String categoryName;
  Long createdBy;
  Instant createdAt;
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Task> tasks = new ArrayList<>();

  CategoryDto categoryDto() {
    return CategoryDto.builder()
            .categoryId(categoryId)
            .categoryName(categoryName)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .build();
  }

  void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  Category addTask(Task task) {
    if (tasks == null) {
      this.tasks = new ArrayList<>();
    }
    this.tasks.add(task);
    return this;
  }

  Category removeTask(Task task) {
    this.tasks.remove(task);
    return this;
  }

  List<Task> getTasks() {
    return tasks;
  }

  Task getTask(UUID taskId) {
    return this.tasks.stream()
            .filter(task -> task.dto().getTaskId().equals(taskId))
            .findFirst().orElseThrow(() -> new TaskNotFoundException(taskId));
  }

  Task modifyTask(ModifyTaskDto modifyTask, UUID taskId) {
    Task task = getTask(taskId);
    this.tasks.remove(task);
    Task modifiedTask = task.modifyTask(modifyTask, this);
    this.tasks.add(modifiedTask);
    return modifiedTask;
  }

  Category changeName(String name) {
    return Category.builder()
            .categoryId(categoryId)
            .categoryName(name)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .tasks(tasks)
            .build();
  }
}
