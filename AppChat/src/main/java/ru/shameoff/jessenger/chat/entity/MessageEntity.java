package ru.shameoff.jessenger.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class MessageEntity {

        @Id
        @NonNull
        @Column(name = "id", columnDefinition = "VARCHAR(255)", nullable = false)
        private UUID id;
        @NonNull
        @Column(nullable = false)
        private UUID chatId;
        @Temporal(TemporalType.DATE)
        @Column
        private Date sendingDate;
        @NonNull
        @Column(nullable = false)
        private String messageText;
}

/*
Сообщение

Идентификатор (задаётся автоматически)
Ссылка на чат
Дата отправки
Текст сообщения (не длиннее 500 символов)
 */