package com.example.dossier.service;

import com.example.dossier.dto.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public void sendEmail(EmailMessage message) {
        // ЗАГЛУШКА: просто логируем в консоль
        log.info("=========================================");
        log.info("📧 ОТПРАВКА EMAIL (заглушка)");
        log.info("📨 Кому: {}", message.getAddress());
        log.info("📌 Тема: {}", message.getSubject());
        log.info("📝 Текст:\n{}", message.getText());
        log.info("🎯 Тип события: {}", message.getTheme());
        log.info("=========================================");

        // Здесь потом будет реальная отправка через JavaMailSender
    }
}