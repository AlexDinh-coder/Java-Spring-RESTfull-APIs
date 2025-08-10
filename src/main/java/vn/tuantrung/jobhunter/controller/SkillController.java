package vn.tuantrung.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.tuantrung.jobhunter.domain.Skill;
import vn.tuantrung.jobhunter.domain.User;
import vn.tuantrung.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuantrung.jobhunter.service.SkillService;
import vn.tuantrung.jobhunter.util.annotation.ApiMessage;
import vn.tuantrung.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill postSkill) throws IdInvalidException {
        // check name
        if (postSkill.getName() != null && this.skillService.isNameExist(postSkill.getName())) {
            throw new IdInvalidException("Skill name " + postSkill.getName() + " is invalid, please use correct name");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(postSkill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill updateSkill) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.fetchSkillById(updateSkill.getId());
        if(currentSkill == null) {
            throw new IdInvalidException("Skill with id = " + updateSkill.getId() + " is not existted");
        }
        //check name
        if (updateSkill.getName() == null && this.skillService.isNameExist(updateSkill.getName())) {
            throw new IdInvalidException("Skill name " + updateSkill.getName() + " is invalid, please use correct name");

        }
        currentSkill.setName(updateSkill.getName());
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.updateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> specification,
            Pageable pageable) {
                return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkills(specification, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException {
        //check id
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if(currentSkill == null) {
            throw new IdInvalidException("Skill with id = " + id + "is not existed");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }
    

}
