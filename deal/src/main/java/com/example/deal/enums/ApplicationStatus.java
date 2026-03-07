package com.example.deal.enums;

public enum ApplicationStatus {
    PREAPPROVAL,           // Заявка создана, ожидается выбор предложения
    APPROVED,              // Предложение выбрано
    CLIENT_DOCUMENT_REQUESTED, // Запрошены документы
    DOCUMENT_CREATED,      // Документы созданы
    CLIENT_DOCUMENT_SIGNED, // Документы подписаны
    CREDIT_ISSUED          // Кредит выдан
}
