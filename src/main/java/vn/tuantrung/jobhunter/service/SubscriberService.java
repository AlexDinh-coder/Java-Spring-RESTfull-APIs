package vn.tuantrung.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.tuantrung.jobhunter.domain.Skill;
import vn.tuantrung.jobhunter.domain.Subscriber;
import vn.tuantrung.jobhunter.repository.SkillRepository;
import vn.tuantrung.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isExistEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber createSubscriber(Subscriber sub) {
        // check skills
        if (sub.getSkills() != null) {
            List<Long> reqSkills = sub.getSkills().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            sub.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(sub);
    }

    public Subscriber findById(long id) {
        Optional<Subscriber> subsOptional = this.subscriberRepository.findById(id);
        if (subsOptional.isPresent()) {
            return subsOptional.get();
        }
        return null;

    }

    public Subscriber updateSubscriber(Subscriber subsDB, Subscriber subsRequest) {
        // check skills
        if (subsRequest.getSkills() != null) {
            List<Long> reqSkills = subsRequest.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subsDB.setSkills(dbSkills);

        }

        return this.subscriberRepository.save(subsDB);
    }

}
