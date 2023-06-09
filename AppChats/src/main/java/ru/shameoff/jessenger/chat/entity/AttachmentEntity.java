package ru.shameoff.jessenger.chat.entity;


/*
Вложение

Идентификатор (задаётся автоматически)
Ссылка на сообщение
Идентификатор файла в хранилище
Наименование файла
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.shameoff.jessenger.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attachments")
public class AttachmentEntity extends BaseEntity {

    @NonNull
    @Column(nullable = false)
    private String messageLink;
    @NonNull
    @Column(nullable = false)
    private String fileId;
    @NonNull
    @Column(nullable = false)
    private String fileName;
}
