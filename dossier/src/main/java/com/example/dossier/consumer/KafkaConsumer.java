package com.example.dossier.consumer;

import com.example.dossier.dto.EmailMessage;
import com.example.dossier.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "finish-registration", groupId = "dossier-group")
    public void handleFinishRegistration(EmailMessage message) {
        log.info("Received finish-registration event: {}", message);
        message.setSubject("Завершите регистрацию");
        message.setText("Ваша заявка предварительно одобрена. Пожалуйста, завершите регистрацию.");
        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "create-documents", groupId = "dossier-group")
    public void handleCreateDocuments(EmailMessage message) {
        log.info("Received create-documents event: {}", message);
        message.setSubject("Создание документов");
        message.setText("Ваши документы готовы. Пожалуйста, запросите их отправку.");
        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "send-documents", groupId = "dossier-group")
    public void handleSendDocuments(EmailMessage message) {
        log.info("Received send-documents event: {}", message);
        message.setSubject("Документы для подписания");
        message.setText("Кредитный договор и график платежей во вложении.\n\nСсылка для подписания: http://localhost:8082/application/offer");
        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "send-ses", groupId = "dossier-group")
    public void handleSendSes(EmailMessage message) {
        log.info("Received send-ses event: {}", message);
        String code = generateSesCode();
        message.setSubject("Код подписания");
        message.setText("Ваш код для подписания: " + code + "\n\nОтправьте его на http://localhost:8081/deal/document/{applicationId}/code");
        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "credit-issued", groupId = "dossier-group")
    public void handleCreditIssued(EmailMessage message) {
        log.info("Received credit-issued event: {}", message);
        message.setSubject("Кредит одобрен");
        message.setText("Поздравляем! Кредит выдан. Деньги поступят на ваш счет в течение 24 часов.");
        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "application-denied", groupId = "dossier-group")
    public void handleApplicationDenied(EmailMessage message) {
        log.info("Received application-denied event: {}", message);
        message.setSubject("Заявка отклонена");
        message.setText("К сожалению, ваша заявка отклонена по результатам скоринга.");
        emailService.sendEmail(message);
    }

    private String generateSesCode() {
        return String.valueOf((int) (Math.random() * 9000 + 1000));
    }
}