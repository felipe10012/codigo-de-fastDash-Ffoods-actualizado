"use strict";

const formulario = document.getElementById("formulario-login");
const campoCorreo = document.getElementById("correo");
const campoContrasena = document.getElementById("contrasena");
const errorCorreo = document.getElementById("error-correo");
const errorContrasena = document.getElementById("error-contrasena");
const mensajeEstado = document.getElementById("mensaje-estado");

const EXPRESION_CORREO = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

function validarCorreo() {
    const valor = campoCorreo.value.trim();
    if (valor === "") {
        marcarCampo(campoCorreo, errorCorreo, "Ingresa tu correo electronico.");
        return false;
    }
    if (!EXPRESION_CORREO.test(valor)) {
        marcarCampo(campoCorreo, errorCorreo, "El formato del correo no es valido (ejemplo: nombre@dominio.com).");
        return false;
    }
    limpiarCampo(campoCorreo, errorCorreo);
    return true;
}

function validarContrasena() {
    const valor = campoContrasena.value;
    if (valor === "") {
        marcarCampo(campoContrasena, errorContrasena, "Ingresa tu contrasena.");
        return false;
    }
    if (valor.length < 6) {
        marcarCampo(campoContrasena, errorContrasena, "La contrasena debe tener al menos 6 caracteres.");
        return false;
    }
    limpiarCampo(campoContrasena, errorContrasena);
    return true;
}

function marcarCampo(campo, mensaje, texto) {
    campo.classList.add("invalido");
    mensaje.textContent = texto;
}

function limpiarCampo(campo, mensaje) {
    campo.classList.remove("invalido");
    mensaje.textContent = "";
}

function mostrarMensaje(texto, tipo) {
    mensajeEstado.textContent = texto;
    mensajeEstado.classList.add("visible", tipo);
}

function ocultarMensaje() {
    mensajeEstado.textContent = "";
    mensajeEstado.className = "mensaje-estado";
}

formulario.addEventListener("submit", function (evento) {
    evento.preventDefault();
    ocultarMensaje();

    const correoValido = validarCorreo();
    const contrasenaValida = validarContrasena();

    if (!correoValido || !contrasenaValida) {
        mostrarMensaje("Revisa los campos marcados para continuar.", "info");
        return;
    }

    mostrarMensaje("Bienvenido a FastDash, " + campoCorreo.value.trim() + "!", "exito");
});

campoCorreo.addEventListener("blur", validarCorreo);
campoContrasena.addEventListener("input", function () {
    if (campoContrasena.classList.contains("invalido")) {
        validarContrasena();
    }
});

document.getElementById("boton-recuperar").addEventListener("click", function () {
    mostrarMensaje("Pronto podras recuperar tu contrasena desde tu correo electronico.", "info");
});

document.getElementById("boton-crear-cuenta").addEventListener("click", function () {
    mostrarMensaje("El registro de nuevos usuarios estara disponible muy pronto.", "info");
});
