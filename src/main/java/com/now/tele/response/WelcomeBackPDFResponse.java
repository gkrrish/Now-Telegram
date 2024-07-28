package com.now.tele.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Base64;

@Data
@EqualsAndHashCode(callSuper = true)
public class WelcomeBackPDFResponse extends WelcomeResponse {
    private byte[] invoice;
    private String invoiceBase64;
    private String delta;

    public WelcomeBackPDFResponse(String message) {
        super(message);
    }

    public WelcomeBackPDFResponse(String message, byte[] invoice, String delta) {
        super(message);
        this.invoice = invoice;
        this.delta = delta;
        this.setInvoiceBase64();
    }

    private void setInvoiceBase64() {
        this.invoiceBase64 = Base64.getEncoder().encodeToString(this.invoice);
    }
}
