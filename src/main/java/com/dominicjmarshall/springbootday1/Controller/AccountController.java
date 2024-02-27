package com.dominicjmarshall.springbootday1.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.dominicjmarshall.springbootday1.models.Account;
import com.dominicjmarshall.springbootday1.services.AccountService;
import com.dominicjmarshall.springbootday1.services.EmailService;
import com.dominicjmarshall.springbootday1.util.AppUtil;
import com.dominicjmarshall.springbootday1.util.email.EmailDetails;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;
    
    @Value("${password.token.reset.timeout.minutes}")
    private int passwordTokenTimeout;

    @Value("${site.domain}")
    private String siteDomain;

    @GetMapping("/register")    
    public String register(Model model){
        
        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    // TODO: send verification email and require clicking a link in order to complete registration.    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()){
            return "account_views/register";
        }

        accountService.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model) {
        
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // if user is logged in
        if (currentUser != null) {
            Optional<Account> optionalAccount = accountService.findOneByEmail(currentUser);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                model.addAttribute("account", account);
                model.addAttribute("photo", account.getPhoto());
                System.out.println("\n\n\n\n\nUser photo: " + account.getPhoto() + "\n\n\n\n\n");
                return "account_views/profile";
            } else {
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String saveProfileEdit(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()){
            System.out.println(result.getFieldError());
        }

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Current user: " + currentUser);

        Optional<Account> optionalAccount = accountService.findOneByEmail(currentUser);
        System.out.println("Optional account: " + optionalAccount.get());

        if(optionalAccount.isPresent()){
            Account existingAccount = optionalAccount.get();
            System.out.println("Account email: " + existingAccount.getEmail());
            existingAccount.setEmail(existingAccount.getEmail());
            System.out.println("Account password: " + existingAccount.getPassword());
            // existingAccount.setPassword(existingAccount.getPassword());
            existingAccount.setFirstName(account.getFirstName());
            existingAccount.setLastName(account.getLastName());
            existingAccount.setGender(account.getGender());
            existingAccount.setAge(account.getAge());
            existingAccount.setBirthday(account.getBirthday());   
            accountService.update(existingAccount);  
            System.out.println("Account updated with pw " + existingAccount.getPassword()); 
            return "redirect:/";      
        } else {
            return "account_views/profile";
        }

    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String updatePhoto(@RequestParam("file") MultipartFile file, RedirectAttributes attributes){
        if(file.isEmpty()){
            attributes.addFlashAttribute("error", "Please upload a valid file");
            return "redirect:/profile";
        } else {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            try {
                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;
                String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
                String finalPhotoName = generatedString + filename;

                String photoFilepath = AppUtil.getUploadPath(finalPhotoName);

                // this is the same as photoFilepath - remove?
                Path path = Paths.get(photoFilepath);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message", "File uploaded");

                String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

                Optional<Account> optionalAccount = accountService.findOneByEmail(currentUser);
                if(optionalAccount.isPresent()){
                    Account existingAccount = optionalAccount.get();
                    
                    /* the absolute path shouldn't be saved to the db.
                    in the UploadsConfig class I add /uploads as a resource location. */

                    existingAccount.setPhoto("/uploads/" + finalPhotoName);
                    accountService.update(existingAccount);  
                }
                return "redirect:/profile";

            } catch (Exception e) {
                return "account_views/profile";
            }
        }
    }

    @GetMapping("/forgot_password")
    public String forgotPassword(Model model) {
        return "account_views/forgot_password";
    }

    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam("email") String _email, RedirectAttributes attributes, Model model) {
        Optional<Account> optionalAccount = accountService.findOneByEmail(_email);
        if(optionalAccount.isPresent()){
            
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            String resetToken = UUID.randomUUID().toString();
            account.setPasswordResetToken(resetToken);
            account.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(passwordTokenTimeout));
            accountService.save(account);
            
            String resetMessage = "Reset password link: " + siteDomain + "change_password?token=" + resetToken;
            EmailDetails emailDetails = new EmailDetails(account.getEmail(), resetMessage, "Jesus Is Lord password reset");
            if(emailService.sendSimpleEmail(emailDetails) == false){
                attributes.addFlashAttribute("error", "Something went wrong");
                return "redirect:/forgot_password";
            };
            attributes.addFlashAttribute("message", "Password reset email sent");
            return "redirect:/login";
        
        } else {
            attributes.addFlashAttribute("error", "Invalid email");
            return "redirect:/forgot_password";
        }
    }

    @GetMapping("/change_password")
    public String changePassword(Model model, @RequestParam("token") String token, RedirectAttributes attributes) {
        
        // first check if there even is a token
        if(token.equals("")){
            attributes.addFlashAttribute("error", "Invalid token");
            return "redirect:/forgot_password";
        }

        Optional<Account> optionalAccount = accountService.findByToken(token);
        if(optionalAccount.isPresent()){
            
            Account account = accountService.findById(optionalAccount.get().getId()).get();

            // check if the token has expired
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(optionalAccount.get().getPasswordResetTokenExpiry())){
                model.addAttribute("error", "Password reset token has expired");
            };
            
            // if not, pass the account id into the Model interface 
            model.addAttribute("account", account);
            return "account_views/change_password";

        } else {
            attributes.addFlashAttribute("error", "Invalid token");
            return "redirect:/forgot_password";
        }
    } 

    @PostMapping("/change_password")
    public String postChangePassword(@ModelAttribute Account accountEdit, RedirectAttributes attributes){
        Account existingAccount = accountService.findById(accountEdit.getId()).get();
        existingAccount.setPassword(accountEdit.getPassword());
        existingAccount.setPasswordResetToken("");
        accountService.save(existingAccount);
        attributes.addFlashAttribute("message", "Password updated");
        return "redirect:/login";
    }

    @GetMapping("/test")
    public String test(Model model) {
        return "account_views/test";
    }

    @GetMapping("/editor")
    public String editor(Model model) {
        return "account_views/editor";
    }
    
}
