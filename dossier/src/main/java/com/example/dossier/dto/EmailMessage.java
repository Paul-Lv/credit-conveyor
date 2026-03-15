package com.example.dossier.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    private String address;
    private String subject;
    private String text;
    private String theme; // finish-registration, create-documents и т.д.
}