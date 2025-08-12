package vn.tuantrung.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.validation.Valid;
import vn.tuantrung.jobhunter.domain.Resume;
import vn.tuantrung.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuantrung.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.tuantrung.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.tuantrung.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.tuantrung.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.tuantrung.jobhunter.service.ResumeService;
import vn.tuantrung.jobhunter.util.annotation.ApiMessage;
import vn.tuantrung.jobhunter.util.error.IdInvalidException;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new resume")
    public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        //check id exists
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException( "User or Job ID is invalid or does not exist.");
        }
        //create new resume
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.createNewResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        //check id exists
        Optional<Resume> resumeOptional = this.resumeService.fetchById(resume.getId());
        if(resumeOptional.isEmpty()){
            throw new IdInvalidException("Resume with id = "+ resume.getId() + " does not exist.");
        }
        //TODO: process PUT request
        Resume reqResume = resumeOptional.get();
        reqResume.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(this.resumeService.updateResume(reqResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if(resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id = " + id + " does not exist.");
        }

        this.resumeService.deleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get a resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchResumeById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id = " + id + " does not exist");
            
        }

        return ResponseEntity.ok().body(this.resumeService.getResumeById(resumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes with pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
    }
    
    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResymeByUser(Pageable pageable) {
        
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
    
    
    
}
