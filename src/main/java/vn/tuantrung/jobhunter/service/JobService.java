package vn.tuantrung.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.tuantrung.jobhunter.domain.Job;
import vn.tuantrung.jobhunter.domain.Skill;
import vn.tuantrung.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuantrung.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.tuantrung.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.tuantrung.jobhunter.repository.JobRepository;
import vn.tuantrung.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public void deleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResCreateJobDTO createJob(Job job) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkill = job.getSkills().stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkills);
        }

        // create job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skillList = currentJob.getSkills().stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skillList);
        }
        return dto;
    }

    public ResUpdateJobDTO updateJob(Job job) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkill = job.getSkills().stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkills);
        }

        // update job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setUpdatedAt(currentJob.getUpdatedAt());
        dto.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skillList = currentJob.getSkills().stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skillList);
        }
        return dto;
    }

    public ResultPaginationDTO fetchAll(Specification<Job> spec, Pageable pageable){
        Page<Job> jobPage = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(jobPage.getTotalPages());
        mt.setTotal(jobPage.getTotalElements());

        result.setMeta(mt);

        result.setResult(jobPage.getContent());

        return result;

    }
    

}
