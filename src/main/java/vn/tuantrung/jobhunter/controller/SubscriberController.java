package vn.tuantrung.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.tuantrung.jobhunter.domain.Subscriber;
import vn.tuantrung.jobhunter.service.SubscriberService;
import vn.tuantrung.jobhunter.util.SecurityUtil;
import vn.tuantrung.jobhunter.util.annotation.ApiMessage;
import vn.tuantrung.jobhunter.util.error.IdInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber) throws IdInvalidException {
        //check email
        boolean isExist = this.subscriberService.isExistEmail(subscriber.getEmail());
        if ( isExist == true) {
            throw new IdInvalidException(("Email " + subscriber.getEmail() + " already exists")); 
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.createSubscriber(subscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subsRequest) throws IdInvalidException {
        //check id
        Subscriber subsDB = this.subscriberService.findById(subsRequest.getId());
        if (subsDB == null) {
            throw new IdInvalidException("Id " + subsRequest.getId() + " does not exist");
        }
        
        return ResponseEntity.ok().body(this.subscriberService.updateSubscriber(subsDB, subsRequest));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true 
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        
        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }
    
    
}