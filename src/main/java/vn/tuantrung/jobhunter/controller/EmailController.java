package vn.tuantrung.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.tuantrung.jobhunter.service.EmailService;
import vn.tuantrung.jobhunter.service.SubscriberService;
import vn.tuantrung.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
       // this.emailService.sendSimpleEmail();
      // this.emailService.sendEmailSync("dttrung2k1@gmail.com", "test send email", "<h1> <b> Hello </b> </h1>", false, true);
      //this.emailService.sendEmailFromTemplateSync("dttrung2k1@gmail.com", "test send email", "job");
      this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
    
    
}
