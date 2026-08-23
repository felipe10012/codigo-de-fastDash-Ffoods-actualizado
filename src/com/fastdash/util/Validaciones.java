package com.fastdash.util;

import java.math.BigDecimal;

public class Validaciones {

    private static final String PATRON_EMAIL = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$";

    private Validaciones() {
    }

    public static boolean textoValido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    public static boolean enteroPositivo(int valor) {
        return valor > 0;
    }

    public static boolean decimalNoNegativo(BigDecimal valor) {
        return valor != null && valor.signum() >= 0;
    }

    public static boolean emailValido(String email) {
        return textoValido(email) && email.trim().matches(PATRON_EMAIL);
    }
}
