package vn.tuantrung.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.tuantrung.jobhunter.domain.Skill;
import vn.tuantrung.jobhunter.domain.User;
import vn.tuantrung.jobhunter.domain.response.ResUserDTO;
import vn.tuantrung.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuantrung.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }
    public Skill handleCreateSkill(Skill s) {
        return this.skillRepository.save(s);
    }

    public Skill fetchSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

    public Skill updateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public void deleteSkill(long id) {
        //delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        //delete subscriber (inside subscriber_skill table)
        currentSkill.getSubscribers().forEach(subs -> subs.getSkills().remove(currentSkill));

        //delete skill
        this.skillRepository.delete(currentSkill);
    }

     public ResultPaginationDTO fetchAllSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageSkill.getContent());
        return rs;
    }
    
}
