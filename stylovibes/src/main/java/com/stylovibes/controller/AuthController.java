package com.stylovibes.controller;

import com.stylovibes.dto.ForgotPasswordRequest;
import com.stylovibes.dto.RegistroRequest;
import com.stylovibes.dto.ResetPasswordRequest;
import com.stylovibes.exception.BusinessException;
import com.stylovibes.exception.DuplicateResourceException;
import com.stylovibes.service.PasswordResetService;
import com.stylovibes.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordResetService passwordResetService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // ---------- Registro ----------
    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registro", new RegistroRequest());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registro") RegistroRequest registro,
                            BindingResult result, RedirectAttributes ra) {
        if (!result.hasFieldErrors("password") && !result.hasFieldErrors("confirmarPassword")
                && !registro.getPassword().equals(registro.getConfirmarPassword())) {
            result.rejectValue("confirmarPassword", "mismatch", "Las contraseñas no coinciden");
        }
        if (result.hasErrors()) {
            return "auth/registro";
        }
        try {
            usuarioService.registrar(registro);
        } catch (DuplicateResourceException ex) {
            result.rejectValue("email", "duplicate", ex.getMessage());
            return "auth/registro";
        }
        ra.addFlashAttribute("ok", "¡Cuenta creada! Ya puedes iniciar sesión.");
        return "redirect:/login";
    }

    // ---------- Recuperación ----------
    @GetMapping("/recuperar")
    public String recuperarForm(Model model) {
        model.addAttribute("forgot", new ForgotPasswordRequest());
        return "auth/recuperar";
    }

    @PostMapping("/recuperar")
    public String recuperar(@Valid @ModelAttribute("forgot") ForgotPasswordRequest forgot,
                            BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "auth/recuperar";
        }
        passwordResetService.solicitar(forgot.getEmail());
        ra.addFlashAttribute("ok", "Si el correo está registrado, recibirás un enlace para restablecer tu contraseña.");
        return "redirect:/login";
    }

    @GetMapping("/reset")
    public String resetForm(@RequestParam String token, Model model, RedirectAttributes ra) {
        if (!passwordResetService.tokenValido(token)) {
            ra.addFlashAttribute("ko", "El enlace es inválido o ya expiró. Solicita uno nuevo.");
            return "redirect:/recuperar";
        }
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setToken(token);
        model.addAttribute("reset", req);
        return "auth/reset";
    }

    @PostMapping("/reset")
    public String reset(@Valid @ModelAttribute("reset") ResetPasswordRequest reset,
                        BindingResult result, RedirectAttributes ra) {
        if (!result.hasFieldErrors("password") && !result.hasFieldErrors("confirmarPassword")
                && !reset.getPassword().equals(reset.getConfirmarPassword())) {
            result.rejectValue("confirmarPassword", "mismatch", "Las contraseñas no coinciden");
        }
        if (result.hasErrors()) {
            return "auth/reset";
        }
        try {
            passwordResetService.restablecer(reset.getToken(), reset.getPassword());
        } catch (BusinessException ex) {
            ra.addFlashAttribute("ko", ex.getMessage());
            return "redirect:/recuperar";
        }
        ra.addFlashAttribute("ok", "Contraseña actualizada. Inicia sesión con tu nueva clave.");
        return "redirect:/login";
    }
}
